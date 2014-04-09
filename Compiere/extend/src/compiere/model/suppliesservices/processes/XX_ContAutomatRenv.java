package compiere.model.suppliesservices.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.apps.ProcessCtl;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.Utilities;
import compiere.model.suppliesservices.X_XX_Contract;
import compiere.model.suppliesservices.X_XX_PayContract;

/**
 * XX_ContractAutomaticRenovation: 
 * Este proceso debe actualizar en la cabera del contrato las fechas período 
 * desde y período hasta por un periodo igual al vencido, fecha  notificación 
 * de  renovación a partir del nuevo periodo fecha hasta, en la pestaña de pagos 
 * las fechas de período desde y período hasta de acuerdo las fechas del contrato, 
 * crear en cuentas por pagar estimadas los pagos correspondientes.   
 * Se tiene campo de Observaciones de renovación (solo lectura) donde se guarda 
 * la información de la renovación
 * 
 * @author María Vintimilla
 * @since 13/07/2011
 */
public class XX_ContAutomatRenv  extends SvrProcess{

	@Override
	protected void prepare() {

	}// fin prepare

	@Override
	protected String doIt() throws Exception {
		String msj = "";
		String SQLContratos = "";
		int Contract_ID = 0;
		PreparedStatement pstmtContrato = null;
		ResultSet rsContrato = null;

		SQLContratos = " SELECT C.XX_Contract_ID ID," +
				" XX_DATETO + 1 Fecha_Desde_nueva, " +
				" XX_DATETO + 1 + (XX_DATETO - XX_DATEFROM) Fecha_hasta_nueva," +
				" (XX_DATETO + 1 + (XX_DATETO - XX_DATEFROM)) - XX_RENEWALNOTIFICATIONDAYS Fecha_NotifRen_Nueva" +
				" FROM XX_CONTRACT C " +
				" WHERE TRUNC(C.XX_DATETO) = TRUNC(SYSDATE) " +
				" AND C.ISACTIVE = 'Y' AND C.XX_AUTOMATICRENOVATION = 'Y' " + 
				" AND C.XX_STATUS = 'C' " +
				" AND C.AD_CLIENT_ID = " + Env.getCtx().getAD_Client_ID();
		//			System.out.println("SQL1: "+SQL1);

		try {
			pstmtContrato = DB.prepareStatement(SQLContratos, null); 
			rsContrato = pstmtContrato.executeQuery();

			while(rsContrato.next()){
				Contract_ID = rsContrato.getInt("ID");
				
				procesarContrato(Contract_ID, 
						rsContrato.getTimestamp("Fecha_Desde_nueva"), 
						rsContrato.getTimestamp("Fecha_hasta_nueva"),  
						rsContrato.getTimestamp("Fecha_NotifRen_Nueva"));
			}// while
		}// try
		catch (Exception e) {
			log.log(Level.SEVERE, SQLContratos);
		}
		finally {
			DB.closeResultSet(rsContrato);
			DB.closeStatement(pstmtContrato);
		}//finally


		return msj;
	}// Fin doIt
	
	/**
	 * Se actualiza la cabecera del contrato y se calculan los pagos y distribucion
	 * de gastos nuevos.
	 * 
	 * @param Contract_ID
	 * @param fechaDesdeNueva
	 * @param fechaHastaNueva
	 * @param fechaRenovNueva
	 */
	public void procesarContrato(int Contract_ID, Timestamp fechaDesdeNueva, 
			Timestamp fechaHastaNueva, Timestamp fechaRenovNueva){
		String SQLPagos = "";
		PreparedStatement pstmtPagos = null;
		ResultSet rsPagos = null;
		
		// Se coloca en el contexto el valor del id del contrato
		Env.getCtx().setContext( "#XX_Contract_ID", Contract_ID);
//		Env.getCtx().setContext( "#AD_Role_ID", 1000343);
		
		// Se toman los valores para la actualizacion de las fechas desde/hasta 
		// de los pagos del contrato, tomando la cantidad de dias de las fechas 
		// originales
		
		SQLPagos = " SELECT " +
				" P.XX_PAYCONTRACT_ID IDPAGO, " +
				DB.TO_DATE(fechaDesdeNueva) + " + (P.XX_DATEFROM - C.XX_DATEFROM) FECHASDESDE, " +
				DB.TO_DATE(fechaDesdeNueva) + " + ((P.XX_DATEFROM - C.XX_DATEFROM) + " +
											" (P.XX_DATETO - P.XX_DATEFROM)) FECHAHASTA " +
				"FROM XX_PAYCONTRACT P INNER JOIN XX_VCN_CONTRACTINVOICE CI  ON (P.XX_VCN_CONTRACTINVOICE_ID = CI.XX_VCN_CONTRACTINVOICE_ID) " +
				"INNER JOIN XX_CONTRACT C ON (C.XX_CONTRACT_ID = CI.XX_CONTRACT_ID) " +
				" WHERE C.XX_CONTRACT_ID = " + Contract_ID;
//		System.out.println(SQLPagos);
		try {
			pstmtPagos = DB.prepareStatement(SQLPagos, null); 
			rsPagos = pstmtPagos.executeQuery();

			while(rsPagos.next()){
				X_XX_PayContract pago = new X_XX_PayContract(Env.getCtx(), rsPagos.getInt("IDPAGO"), get_Trx());
				pago.setXX_DateFrom(rsPagos.getTimestamp("FECHASDESDE"));
				pago.setXX_DateTo(rsPagos.getTimestamp("FECHAHASTA"));
				pago.save();
				commit();
			}// while
		}// try
		catch (Exception e) {
			log.log(Level.SEVERE, SQLPagos);
			return;
		}
		finally {
			DB.closeResultSet(rsPagos);
			DB.closeStatement(pstmtPagos);
		}//finally


		// Se actualiza la cabecera del Contrato: las fechas desde/hasta por un 
		// periodo igual al vencido, fecha  notificación de  renovación a partir 
		// del nuevo periodo fecha hasta		
		X_XX_Contract contrato = new X_XX_Contract(Env.getCtx(), Contract_ID, get_Trx());
		contrato.setXX_DateFrom(fechaDesdeNueva);
		contrato.setXX_DateTo(fechaHastaNueva);
		contrato.setXX_RenewalNotificationDate(fechaRenovNueva);
		contrato.save();
		commit();
		
		// Se generen las cuentas por pagar estimadas 
		try{
			MPInstance mpi = new MPInstance(Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_ESTIMATEDCONTRACT_ID"), 0); 
			mpi.save();
			ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_ESTIMATEDCONTRACT_ID")); 
			pi.setRecord_ID(mpi.getRecord_ID());
			pi.setAD_PInstance_ID(mpi.get_ID());
			pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_ESTIMATEDCONTRACT_ID")); 
			pi.setClassName(""); 
			pi.setTitle(""); 
			ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
			list.add (new ProcessInfoParameter("XX_Contract_ID", String.valueOf(Contract_ID), null, String.valueOf(Contract_ID), null));
			ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
			list.toArray(pars);
			pi.setParameter(pars);
			ProcessCtl pc = new ProcessCtl(null ,pi,null); 
			pc.start();
		}catch(Exception e){
			log.log(Level.SEVERE,e.getMessage());
		}
		
		// Se envía el correo informando que se renovó el contrato
		//sendMailAP(Contract_ID);
		
		// Se remueve del contexto el ID del contrato
		(Env.getCtx()).remove("#XX_Contract_ID");
		
	}// Fin procesarContrato
	

	/** sendMailAP
	 * Se envía un correo a legal informando la renovación automatica de un contrato
	 * @param Contract_ID Identificador del Contrato
	 *  */
	private void sendMailAP(Integer Contract_ID){
		int Legal_ID = Env.getCtx().getContextAsInt("#XX_L_ROLELEGAL_ID");
		String message = Msg.getMsg( Env.getCtx(), "XX_ContractAutomRenov", 
				new String[]{Contract_ID.toString()});
		String sql = " SELECT AD_USER_ID " +
					" FROM AD_User_Roles" +
					" WHERE IsActive = 'Y' " +
					" AND AD_ROLE_ID = "+ Legal_ID +
					" AND AD_USER_ID <> 100 ";
		//System.out.println("sql role: "+sql);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Utilities f = new Utilities(Env.getCtx(), null,
						Env.getCtx().getContextAsInt("#XX_L_MT_CONTRACTAUTOMRENOV_ID"), message, -1, 
						Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, 
						rs.getInt("AD_USER_ID"), null);
				try {
					f.ejecutarMail();
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
				f = null;
			}
		}//try
		catch (Exception e){
			log.saveError("ErrorSql Rol Legal", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}//finally
	}// Fin sendMailAP

} // Fin XX_ContractAutomaticRenovation
