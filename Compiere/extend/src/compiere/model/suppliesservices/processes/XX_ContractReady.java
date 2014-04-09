package compiere.model.suppliesservices.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
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

/** Función 108: Revision de Contratos
 * Setea el Campo contrato listo, envía correo a Abogado y Asesor legal
 * para informar que un contrato está listo para su revisión.
 * Se verifica que el contrato tenga definido el tipo de pago, el responsable,
 * la gerencia, la cuenta contable y que tenga definida la distribución de los
 * gastos y los pagos.
 * 
 * @author Maria Vintimilla
 * */
public class XX_ContractReady extends SvrProcess{


	@Override
	protected void prepare() {
	}// fin prepare

	@Override
	protected String doIt() throws Exception {
		String msj = " Contrato Listo";
		Integer Contract_ID;
		Integer Legal_ID = 0;
		Integer Advisor_ID = 0;
		Integer Role = 0;
		Integer Sales_ID = 0;
		Integer Merchandising_ID = 0;
		Integer Personal_ID = 0;
		Integer Logistic_ID = 0;
		Integer System_ID = 0;
		Integer Control_ID = 0;
		Integer Merchan_ID = 0;
		Contract_ID = getRecord_ID();
		X_XX_Contract contrato= new X_XX_Contract(Env.getCtx(),Contract_ID,get_Trx());

		// Toma de roles. Si el rol lo permite podrá aprobar de una vez el contrato
		Role = Env.getCtx().getContextAsInt("#AD_Role_ID"); 
		Legal_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLELEGAL_ID"));
		Advisor_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLELEGALADVISOR_ID"));
		Sales_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLESALES_ID"));
		Merchandising_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLEMERCHANDISING_ID"));
		Personal_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLEPERSONAL_ID"));
		Logistic_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLELOGISTIC_ID"));
		System_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLESYSTEM_ID"));
		Control_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLECONTROL_ID"));
		Merchan_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLEMERCHAN_ID"));

//		System.out.println("responsable: "+contrato.getXX_Responsable_ID()+ " gerencia:  " + contrato.getXX_Management_ID() 
//				+ " cuenta contable: "+ contrato.getXX_PExpenses_ID() + " tipo de pago: "+ contrato.getXX_PaymentTypeDetail()
//				+" socio: "+ contrato.getC_BPartner_ID() + " fecha inicio: "+ contrato.getXX_DateFrom() + " fecha fin:" +
//				contrato.getXX_DateTo() + " dist monto: " + contrato.isXX_IsDistrbAmount() + " dist percent: "+
//				contrato.isXX_IsDistrbPercentage());
		
		// Verificaciones para contrato listo
		if( contrato.getC_BPartner_ID() != 0 && contrato.getXX_Responsable_ID() != 0 
				&& contrato.getXX_Management_ID() != 0 
				&& contrato.getXX_PaymentTypeDetail().compareTo("") != 0 && contrato.getDescription().compareTo("") != 0
				&&  contrato.getXX_DateFrom().getYear() != 0	&& contrato.getXX_DateTo().getYear() != 0 
				&& (contrato.isXX_IsDistrbAmount() || contrato.isXX_IsDistrbPercentage())
				&& verifyPayments(Contract_ID, contrato.getXX_PaymentTypeDetail())){
			contrato.setXX_IsContractReady(true);
			contrato.save();
			commit();
		}
		else {
			String msg = "Falta por definir: ";
			if(contrato.getC_BPartner_ID() == 0)
				msg += " Socio de Negocio";
			if(contrato.getXX_Responsable_ID() == 0)
				msg += " Responsable";
			if(contrato.getXX_Management_ID() == 0)
				msg += " Gerencia";	
			if(contrato.getXX_PaymentTypeDetail().compareTo("") == 0)
				msg += " Tipo de pago";
			if(contrato.getDescription().compareTo("") == 0)
				msg += " Descripcion";
			if(contrato.getXX_DateFrom().getYear() == 0	|| contrato.getXX_DateTo().getYear() == 0 )
				msg += " error en periodo de duracion";
			if(!contrato.isXX_IsDistrbAmount() && !contrato.isXX_IsDistrbPercentage())
				msg += " no ha definido la distribucion";
			if(!verifyPayments(Contract_ID, contrato.getXX_PaymentTypeDetail()))
				msg += " No tiene distribucion de gastos asociada";
			ADialog.error(1, new Container(), Msg.getMsg( Env.getCtx(), "XX_ApproveContractPay",
					new String[]{msg}));
			return msg;
		}
		// Se coloca en el contexto el valor del id del contrato
		Env.getCtx().setContext( "#XX_Contract_ID", Contract_ID);

		// Se manda a aprobar si tiene el rol para ello
		if(Role.compareTo(Advisor_ID) == 0 || Role.compareTo(Sales_ID) == 0 || Role.compareTo(Merchandising_ID) == 0 || 
				Role.compareTo(Personal_ID) == 0 || Role.compareTo(Logistic_ID) == 0 || 
				Role.compareTo(System_ID) == 0 || Role.compareTo(Control_ID) == 0 || 
				Role.compareTo(Control_ID) == 0 || Role.compareTo(Merchan_ID) == 0){
			// Se aprueba el contrato para que se generen las cuentas estinadas por pagar
			try{ 
				MPInstance mpi = new MPInstance(Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_APPROVECONTRACTPROCESS_ID"), 0); 
				mpi.save();
				ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_APPROVECONTRACTPROCESS_ID"));
				pi.setRecord_ID(mpi.getRecord_ID());
				pi.setAD_PInstance_ID(mpi.get_ID());
				pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_APPROVECONTRACTPROCESS_ID")); 
				pi.setClassName(""); 
				pi.setTitle(""); 
				ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
				list.add (new ProcessInfoParameter("XX_Contract_ID", Contract_ID, null, String.valueOf(Contract_ID), null));
				ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
				list.toArray(pars);
				pi.setParameter(pars);
				ProcessCtl pc = new ProcessCtl(null ,pi, null); 
				pc.start();
			}
			catch(Exception e){
				log.log(Level.SEVERE,e.getMessage());
			}
		} // if roles
		else{
			// Se envía correo a Abogado y Asesor legal para informar que un 
			// contrato está listo para su revisión
			sendMailAP(Legal_ID, contrato.getValue());
			sendMailAP(Advisor_ID, contrato.getValue());
		}

		// Se remueve del contexto el ID del contrato
		(Env.getCtx()).remove("#XX_Contract_ID");

		return msj;
	}

	/** verifyPayments
	 * Verifica que el contrato tiene pagos y distribución de gastos asociados
	 * @param Contract_ID Contract Code
	 * @param tipoPago Tipo de Pago
	 * @return hasPayments Indica si el contrato tiene pagos y distribución de gastos asociados
	 * @author Maria Vintimilla
	 * @since 12/06/2012
	 *  */
	private boolean verifyPayments(Integer Contract_ID, String tipoPago){
		boolean hasPayments = false;
		int qtyPay = 0;
		String SQLPay = "";
		ResultSet rs = null;
		PreparedStatement pstmt = null; 
	
		SQLPay = " SELECT COUNT(*) QTY" +
					" FROM XX_CONTRACT CONTRACT INNER JOIN XX_CONTRACTDETAIL DETAIL " +
					" ON (CONTRACT.XX_CONTRACT_ID = DETAIL.XX_CONTRACT_ID) " +
					" WHERE CONTRACT.XX_CONTRACT_ID = " + Contract_ID; 

		try{
			pstmt = DB.prepareStatement(SQLPay, get_Trx()); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				
				qtyPay = rs.getInt("QTY");
				if(qtyPay > 0)
					hasPayments = true;
				
			}
		}
		catch (Exception e){
			log.log(Level.SEVERE, SQLPay);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return hasPayments;
	}// Fin verifyPayments

	/** sendMailAP
	 * @param message E-mail's text
	 * @param user_ID Recipient User
	 * @param Contract_ID Contract Code
	 *  */
	private void sendMailAP(Integer user_ID, String Contract){
		String message = Msg.getMsg( Env.getCtx(), "XX_ContractReady", new String[]{Contract});

		String sql = " SELECT AD_USER_ID " +
		" FROM AD_User_Roles" +
		" WHERE IsActive = 'Y' " +
		" AND AD_ROLE_ID = "+ user_ID +
		" AND AD_USER_ID <> 100 " +
		" AND AD_Client_ID = " + Env.getCtx().getAD_Client_ID() ;
		//System.out.println("sql role: "+sql);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			rs = pstmt.executeQuery();
			while(rs.next()){
				Utilities f = new Utilities(getCtx(), null,
						Env.getCtx().getContextAsInt("#XX_L_MT_CONTRACTREADY_ID"), message, 
						-1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, 
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
			log.saveError("ErrorSql Contrato Listo", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}//finally
	}// Fin sendMailAP

} // Fin extends SvrProcess