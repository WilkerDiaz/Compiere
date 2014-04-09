package compiere.model.suppliesservices.processes;

import java.awt.Container;
import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.X_XX_VSI_Client;
import compiere.model.suppliesservices.X_Ref_XX_Status_LV;
import compiere.model.suppliesservices.X_XX_Contract;
import compiere.model.suppliesservices.X_XX_PayContract;

/** Purchase of Supplies and Services 
 * Maria Vintimilla Funcion 33
 * Early Termination of a contract.
 * Funcion 
 * @author Maria Vintimila
 * @version 2.0**/
public class XX_TerminContract extends SvrProcess{
	private Timestamp terminDate;
		
	@Override
	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();
			
			if (element.getParameter() == null) ;
			// Fecha de terminacion
			else if (name.equals("XX_TerminationDate")) { 
				if (element.getParameter() != null) {					
					terminDate = (Timestamp)element.getParameter();
				}
				else{
					ADialog.error(1, new Container(), "XX_TerminationDate" );
					return;	
				}
			}
			// Desconocidos
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}// for	
	}// fin prepare
	
	@Override
	protected String doIt() throws Exception {
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
		String User = getName();
		int User_ID = 0; 
		
		User_ID = Env.getCtx().getAD_User_ID();
		User = get_Name(User_ID);
		X_XX_Contract contrato= new X_XX_Contract(Env.getCtx(),getRecord_ID(),null);
		Role = Env.getCtx().getAD_Role_ID();
		
		// Obtencion de roles para terminacion anticipada
		Sales_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLESALES_ID"));
		Merchandising_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLEMERCHANDISING_ID"));
		Personal_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLEPERSONAL_ID"));
		Logistic_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLELOGISTIC_ID"));
		System_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLESYSTEM_ID"));
		Control_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLECONTROL_ID"));
		Merchan_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLEMERCHAN_ID"));
		Legal_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLELEGAL_ID"));
		Advisor_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLELEGALADVISOR_ID"));
		

		if(Role.equals(Legal_ID) || Role.equals(Advisor_ID) || Role.equals(Sales_ID) || 
				Role.equals(Merchandising_ID) || Role.equals(Personal_ID) || Role.equals(Logistic_ID) || 
				Role.equals(System_ID) || Role.equals(Control_ID) || Role.equals(Merchan_ID)){
			//Denied by Roles
			if(contrato.getXX_Status().equals(X_Ref_XX_Status_LV.APPROVED_BY_LEGAL.getValue()) || 
					contrato.getXX_Status().equals(X_Ref_XX_Status_LV.UNSIGNED_APPROVED.getValue())){
				contrato.setXX_Status("A");
				contrato.setXX_TerminationUser(User);
				contrato.setXX_TerminationDate(terminDate);
				contrato.setProcessed(true);
				contrato.save();
				commit();
				procesarCtasEstimadas(contrato.get_ID());
				
			}//if
			else{
				log.saveError("Error", Msg.translate(Env.getCtx(), "XX_TermContract"));
			}
		}//if roles
		else{
			log.saveError("Error", Msg.translate(Env.getCtx(), "XX_ContractTermination"));
		}
		return "";
	}// fin doIt

	
	
	/** get_Name
	 * @param role_ID
	 * @return User's Name
	 *  */
	private String get_Name(Integer role_ID){
		String UserAuxID = "";
		String sql = "SELECT NAME "
					+" FROM AD_User "
					+" WHERE AD_User_ID="+role_ID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
		
			while(rs.next()){
				UserAuxID = rs.getString("NAME");
			}
		}
		catch (Exception e){
			log.saveError("ErrorSql Obtencion de nombre de usuario en terminacion anticipada de contrato ", 
					Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return UserAuxID;
	} // Fin get_AD_User
	
	/**
	 * procesarCtasEstimadas
	 * Se deben eliminar las cuentas por pagar estimadas asociadas al contrato
	 * cuya fecha sea posterior a la fecha de terminación del mismo.
	 * 
	 * Debe tomarse en cuenta que el tipo de pago (anticipado o mes vencido)
	 * @param Contract_ID Identificador del contrato
	 * @param terminationDate Fecha de terminacion del contrato
	 */
	private void procesarCtasEstimadas(int Contract_ID){
		int cantMeses = 0;
		int cantPagos = 0;
		
		String SQLPagos = " SELECT C.XX_CONTRACT_ID IDCONT, " +
				" C.XX_TERMINATIONDATE TERMIN, " +
				" P.XX_PAYCONTRACT_ID ID, " +
				" P.XX_ANTICIPATED1 ANT1," +
				" P.XX_ANTICIPATED2 ANT2," +
				" P.XX_DATEFROM INI," +
				" P.XX_DATETO FIN, " +
				" P.XX_PAYDAY1 PAY1," +
				" P.XX_PAYDAY2 PAY2," +
				" CASE WHEN P.XX_PAYMENTRECURRENCY1 IS NULL THEN 'NUL' " +
				" ELSE P.XX_PAYMENTRECURRENCY1 END PAYREC1, " +
				" CASE WHEN P.XX_PAYMENTRECORRENCY2 IS NULL THEN 'NUL' " +
				" ELSE P.XX_PAYMENTRECORRENCY2 END PAYREC2 " +
				" FROM XX_PAYCONTRACT P INNER JOIN XX_CONTRACT C " +
				" ON (P.XX_CONTRACT_ID = C.XX_CONTRACT_ID)" +
				" WHERE C.XX_CONTRACT_ID = " + Contract_ID +
				" AND AD_Client_ID = " + 
				Env.getCtx().getAD_Client_ID();
//		System.out.println("SQL PAGOS: "+SQLPagos);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(SQLPagos, get_Trx());
			rs = pstmt.executeQuery();
		
			while(rs.next()){
				cantMeses = mesesPeriodos(rs.getTimestamp("INI"),rs.getTimestamp("FIN"));
				cantPagos = recurrenciaContrato(rs.getString("PAYREC1"), cantMeses);

				if (cantPagos != 0){
					// Dependiendo del tipo de pago se realiza el calculo para
					// eliminar las cuentas que correspondan
					if(!rs.getString("PAYREC1").equals("NUL")){
						eliminarCtasEstimadas(rs.getTimestamp("INI"),rs.getTimestamp("FIN"),
								rs.getTimestamp("TERMIN"),cantPagos,rs.getString("PAYREC1"),
								rs.getInt("PAY1"),rs.getString("ANT1"),rs.getInt("IDCONT"));
					}
					else{
						eliminarCtasEstimadas(rs.getTimestamp("INI"),rs.getTimestamp("FIN"),
								rs.getTimestamp("TERMIN"), cantPagos,rs.getString("PAYREC2"),
								rs.getInt("PAY2"),rs.getString("ANT2"), rs.getInt("IDCONT"));
					}
				} // cantPagos
			}// while
		}
		catch (Exception e){
			log.saveError("ErrorSql Borrando cuentas por pagar estimadas de contrato ", 
					Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	
	} // Fin deleteAcountsPayable
	
	/**
	 * Se encarga de buscar la cantidad de meses entre la fecha desde 
	 * y la fecha hasta del pago del contrato
	 * @author Jessica Mendoza
	 * @param dateFrom fecha desde
	 * @param dateTo fecha hasta
	 * @return
	 */
	public int mesesPeriodos(Timestamp dateFrom, Timestamp dateTo){
		Calendar calendarFrom = Calendar.getInstance(); 
		Calendar calendarTo = Calendar.getInstance(); 
		calendarFrom.setTime(dateFrom); 
		calendarTo.setTime(dateTo);
		int mesFrom = calendarFrom.get(Calendar.MONTH) + 1;
		int mesTo = calendarTo.get(Calendar.MONTH) + 1;
		int meses = 0;
		int dif = 0;
		
		if (calendarTo.get(Calendar.YEAR) == calendarFrom.get(Calendar.YEAR)){
			if (mesTo != mesFrom)
				meses = mesTo - mesFrom;
		}else if (calendarFrom.get(Calendar.YEAR) < calendarTo.get(Calendar.YEAR)){
			dif = 12 - mesFrom;
			meses = dif + mesTo;
		}
		return meses;
	}

	/**
	 * Se encarga de buscar la cantidad de pagos dependiendo del tipo de recurrencia
	 * @author Jessica Mendoza
	 * @param recurrencia tipo de recurrencia
	 * @param meses cantidad de meses entre el período
	 * @return
	 */
	public int recurrenciaContrato(String recurrencia, int meses){
		int cantPagos = 0;

		if (recurrencia.equals("BIA")) //Semestral
			cantPagos = meses/6;
		if (recurrencia.equals("MON")) //Mensual
			cantPagos = meses;
		if (recurrencia.equals("TRI")) //Trimestral
			cantPagos = meses/3;
		if (recurrencia.equals("UNI")) //Pago Unico
			cantPagos = 1;
		
		return cantPagos;
	}

	/**
	 * Se encarga de construir la fecha estimada, y calcular el monto, 
	 * para poder crear el contrato como registro en la tabla de 
	 * estimación de las cuentas por pagar
	 * @param Fecha de inicio del pago
	 * @param Fecha de finalizacion del pago
	 * @param Fecha de terminacion del contrato
	 * @param cantPagos cantidad de pagos en que se deben realizar
	 * @param recurrencia tipo de recurrencia
	 * @param dia dia en que se debera colocar la fecha estimada
	 * @param anticipo anticipo
	 * @param Contract_ID identificador del contrato
	 * @author Maria Vintimilla (Basado en implementacion de Jessica Mendoza)
	 */
	public void eliminarCtasEstimadas(Timestamp DateFrom, Timestamp DateTo, Timestamp 
			terminationDate, int cantPagos, String recurrencia, int dia, 
			String anticipo, int Contract_ID) {
		int mes = 0;
		Calendar dateFrom = Calendar.getInstance();
		Calendar dateTo = Calendar.getInstance();
		Calendar dateTermin = Calendar.getInstance();
		Calendar fecha = Calendar.getInstance();
		
		dateTo.setTime(DateTo);
		dateFrom.setTime(DateFrom);
		dateTermin.setTime(terminationDate);
		
		// Se toma el mes para las cuentas por pagar estimadas dependiendo de
		// si el pago se realiza a mes vencido/anticipado
		if (anticipo.equals("Y")){
			// Pago anticipado
			mes = dateTermin.get(Calendar.MONTH);
		}
		else{
			// Pago mes vencido
			if (dateTermin.get(Calendar.DATE) > dia)
				mes = dateTermin.get(Calendar.MONTH)+2;
			else
				mes = dateTermin.get(Calendar.MONTH)+1;
		}

		while (cantPagos != 0){
			// Se define el día de acuerdo al día de pago definido por el usuario
			fecha.set(Calendar.DATE, dia);
			fecha.set(Calendar.YEAR, dateTo.get(Calendar.YEAR));

			if (recurrencia.equals("BIA")){ //Semestral
				fecha.set(Calendar.MONTH, mes);
				mes = mes + 6;				
			}
			if (recurrencia.equals("MON")){ //Mensual
				fecha.set(Calendar.MONTH, mes);
				mes = mes + 1;
			}
			if (recurrencia.equals("TRI")){ //Trimestral
				fecha.set(Calendar.MONTH, mes);
				mes = mes + 3;
			}
			if (recurrencia.equals("UNI")){ //Pago único
				fecha.set(Calendar.MONTH, mes);
			}

			Timestamp fechaEstimada = new Timestamp(fecha.getTimeInMillis());

			// Se compara la fecha estimada calculada con la fecha de terminacion
			// para definir si las cuentas asociadas deben eliminarse
			if(fechaEstimada.after(terminationDate) && fechaEstimada.before(DateTo)){
				buscarCuentas(fechaEstimada,Contract_ID);
				deleteInvoice(Contract_ID, fechaEstimada);
			}

			cantPagos--;
		}// while de pagos
	}// Fin estimacion
	
	/**
	 * buscarCuentas
	 * Elimina la cuenta estimada por pagar relacionada al contrato y cuya
	 * fecha estimada sea la fecha calculada desde pago
	 * @param fechaEstimada Fecha estimada de la cuenta por pagar
	 * @param idContrato Identificador del contrato
	 */
	public void buscarCuentas(Timestamp fechaEstimada, int idContrato){
		int result = 0;

		String SQLDelete = " DELETE FROM XX_VCN_EstimatedAPayable" +
					" WHERE XX_Contract_ID = " + idContrato + " " +
					" AND TRUNC(XX_DateEstimated) =  TRUNC(" + DB.TO_DATE(fechaEstimada) +")";		
		try {
			result = DB.executeUpdateEx(SQLDelete, get_Trx());
			commit();
		} 
		catch (SQLException e) { e.printStackTrace(); }
		
		if(result == -1){
			ADialog.error(1, new Container(), "XX_ErrorDelteInvoice");
		}
	}// fin BuscarEstimacion
	
	/**
	 * deleteInvoice
	 * Eliminación de facturas asociadas a un contrato a ser terminado anticipadamente,
	 * de estado borrador y con fecha de vencimiento igual a la fecha estimada de la
	 * cuenta por pgar
	 * @param Contract_ID Identificador del contrato
	 * @param estimatedDate Fecha estimada de la cuenta por pagar
	 */
	private boolean deleteInvoice(int Contract_ID, Timestamp estimatedDate){
		boolean resultado = false;
		String SQLDelete = " DELETE FROM C_INVOICE WHERE XX_CONTRACT_ID = " + Contract_ID
							+ " AND ISSOTRX = 'N' AND DOCSTATUS = 'DR' "
							+ " AND XX_DUEDATE = " + DB.TO_DATE(estimatedDate);
		int result = 0;
		try {
			result = DB.executeUpdateEx(SQLDelete, get_Trx());
			commit();
		} catch (SQLException e) {e.printStackTrace();}
		
		if(result == -1){
			ADialog.error(1, new Container(), "XX_ErrorDelteInvoice");
			resultado = false;
		}
		else {
			resultado = true;
		}
		return resultado;
	} // fin deleteInvoice

} // Fin XX_TerminContract
