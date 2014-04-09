package compiere.model.payments.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VSI_Client;
import compiere.model.payments.X_XX_VCN_EstimatedAPayable;
import compiere.model.suppliesservices.MPayContract;
import compiere.model.suppliesservices.X_XX_PayContract;

public class XX_RecalculateEPayment extends SvrProcess {

	Utilities util = new Utilities();
	int payContractID = 0;
	Timestamp newDateFrom = null;
	BigDecimal newAmount = BigDecimal.ZERO;
	BigDecimal newPercentage = BigDecimal.ZERO;
	BigDecimal remainder = BigDecimal.ZERO;
	BigDecimal percentageRemainder = BigDecimal.ZERO;
	int alreadyPaid = 0;
	
	@Override
	protected void prepare() {
		
		payContractID = getRecord_ID();
		
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_DateFrom"))
				newDateFrom = (Timestamp) element.getParameter();
			else if (name.equals("XX_Amount"))
				newAmount = (BigDecimal) element.getParameter();
			else if (name.equals("XX_Percentage"))
				newPercentage = (BigDecimal) element.getParameter();
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		if(newAmount.compareTo(BigDecimal.ZERO) == 0 && newPercentage.compareTo(BigDecimal.ZERO) == 0)
		{
			ADialog.error(1, new Container(), "Debe indicar un monto o un porcentaje");
			return "ERROR: Debe indicar un monto o un porcentaje";
		}
		
		if(newAmount.compareTo(BigDecimal.ZERO) != 0 && newPercentage.compareTo(BigDecimal.ZERO) != 0)
		{
			ADialog.error(1, new Container(), "Debe indicar solo un monto o un porcentaje");
			return "ERROR: Debe indicar solo un monto o un porcentaje";
		}
		
		MPayContract paycontract = new MPayContract( Env.getCtx(), getRecord_ID(), null);
		paycontract.setXX_ContractAmount(newAmount);
		paycontract.setXX_NewDateFrom(newDateFrom);
		paycontract.setXX_NewContractAmount(newAmount);
		paycontract.setXX_CPercentage(newPercentage);
		paycontract.setXX_NewCPercentage(newPercentage);
		paycontract.setXX_RecalculateEPayment("Y");
		paycontract.save();
		
		remainder = newAmount.subtract(paycontract.getXX_ContractAmount());
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select cont.XX_Contract_ID, cinv.C_BPartner_ID, cont.C_Currency_ID, " +
					 "cont.XX_PaymentTypeDetail, cont.XX_ContractAmount, cont.XX_ApplicablePercentage, " +
					 "cont.XX_VMR_Brand_ID, pcon.XX_DateFrom, pcon.XX_DateTo, pcon.XX_ContractAmount, " +
					 "pcon.XX_Anticipated1, pcon.XX_Anticipated2, pcon.XX_PayDay1, pcon.XX_PayDay2, " +
					 "pcon.XX_PaymentRecurrency1, pcon.XX_PaymentRecorrency2, pcon.XX_CPercentage, " +
					 "pcon.XX_PayContract_ID, par.XX_VendorType_ID, par.PO_PaymentTerm_ID, cont.M_Warehouse_ID, " +
					 "cinv.Description, XX_SpecificPayDate1, XX_SpecificPayDate2 " +
					 "from XX_Contract cont, XX_PayContract pcon, C_BPartner par, XX_VCN_ContractInvoice cinv " +
					 "where cont.XX_Contract_ID = cinv.XX_Contract_ID " +
					 "and par.C_BPartner_ID = cinv.C_BPartner_ID " +
					 "AND cinv.XX_VCN_ContractInvoice_ID = pcon.XX_VCN_ContractInvoice_ID " +
					 "and pcon.XX_PayContract_ID = " + payContractID;

		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()){
				switch(rs.getString("XX_PaymentTypeDetail").hashCode()) {
				 	case 'A': 
				 		//System.out.println("Fijo y Variable");
				 		//tipoPagoContrato(rs,"A");
				 		break;
				 	case 'F': 
				 		//System.out.println("Fijo");
				 		tipoPagoContrato(rs,"F");
				 		break;				 		
				 	case 'G': 
				 		System.out.println("Variable con Fijo Garantizado");
				 		break;				 		
				 	case 'V': 
				 		//System.out.println("Variable");
				 		tipoPagoContrato(rs,"V");
				 		break;
				}
			}
			
		}catch (SQLException e){
			log.log(Level.SEVERE, sql, e);
		}finally{
			pstmt.close();
			rs.close();
		}
		
		//Distribucion 
		if(newAmount.compareTo(BigDecimal.ZERO)!=0)
		{
			String distrib = "UPDATE XX_ContractDetail SET XX_CONTRACTAMOUNT = ((XX_PERCENAMOUNT/100) * "+ newAmount +") WHERE XX_PAYCONTRACT_ID = " + getRecord_ID();
			DB.executeUpdate(null, distrib);
		}
		
		return "Recalculado";
	}
	
	/**
	 * Se encarga de construir la fecha estimada, y calcular el monto, 
	 * para poder crear el contrato como registro en la tabla de 
	 * estimación de las cuentas por pagar
	 * @param rs Resultset del query principal
	 * @param cantPagos cantidad de pagos en que se deben realizar
	 * @param monto monto del pago del contrato
	 * @param recurrencia tipo de recurrencia
	 * @param dia dia en que se debera colocar la fecha estimada
	 * @param anticipo anticipo
	 * @param porcentaje porcentaje del pago del contrato
	 * @throws SQLException
	 */
	public void estimacion(ResultSet rs, int cantPagos, BigDecimal monto, 
			String recurrencia, int dia, String anticipo, BigDecimal porcentaje, Timestamp specificPayDate) throws SQLException{
		
		int mes = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Vector<Integer> vecIContrato = new Vector<Integer>(6);
		Vector<String> vecS = new Vector<String>(0);
		Calendar dateFrom = Calendar.getInstance();
		
		//dateFrom.setTime(rs.getTimestamp("XX_DateFrom"));
		dateFrom.setTime(newDateFrom);
		
		Calendar dateTo = Calendar.getInstance();
		dateTo.setTime(rs.getTimestamp("XX_DateTo"));
		
		Calendar fecha = Calendar.getInstance();
		Calendar fechaAux = Calendar.getInstance();
			
		if (anticipo.equals("Y")){
			//Verificar el día de la fecha desde
			if (dateFrom.get(Calendar.DATE) < 7){
				mes = dateFrom.get(Calendar.MONTH);
				/*dia = diaAnticipado(dateFrom.get(Calendar.DATE) + "/" +
						(mes+1) + "/" + dateFrom.get(Calendar.YEAR));*/
			}else{
				mes = dateFrom.get(Calendar.MONTH) + 1;
				//dia = diaAnticipado("01/" + (mes+1) + dateFrom.get(Calendar.YEAR));
			} 
		}else{
			/*if (dateFrom.get(Calendar.DATE) > dia)
				mes = dateFrom.get(Calendar.MONTH) + 1;
			else
				mes = dateFrom.get(Calendar.MONTH);*/	
			mes = dateFrom.get(Calendar.MONTH) + 1;
		}

		while (cantPagos != 0){
			if (anticipo.equals("Y")){
				//Verificar el día de la fecha desde				
				fechaAux.set(Calendar.MONTH, mes);
				fechaAux.set(Calendar.YEAR, dateFrom.get(Calendar.YEAR));
				if (dateFrom.get(Calendar.DATE) < 7){		
					fechaAux.set(Calendar.DATE, dateFrom.get(Calendar.DATE));
					dia = diaAnticipado(sdf.format(fechaAux.getTime()));
				}else{
					fechaAux.set(Calendar.DATE, 1);
					dia = diaAnticipado(sdf.format(fechaAux.getTime()));
				} 
			}
			fecha.set(Calendar.DATE, dia);
			fecha.set(Calendar.MONTH, mes);
			fecha.set(Calendar.YEAR, dateFrom.get(Calendar.YEAR));
			if (recurrencia.equals("BIA")) //Semestral				
				mes = mes + 6;							
			else if (recurrencia.equals("MON")) //Mensual
				mes = mes + 1;			
			else if (recurrencia.equals("TRI")) //Trimestral
				mes = mes + 3;
			//else/*if (recurrencia.equals("UNI")) *///Pago único
				//fecha.set(Calendar.MONTH, mes);
			
			if (rs.getBigDecimal(17).compareTo(new BigDecimal(0)) != 0){
				monto = calcularMonto(rs.getString("XX_ApplicablePercentage"), fecha.get(Calendar.YEAR), 
						fecha.get(Calendar.MONTH)+1, rs.getInt("M_Warehouse_ID"));
				if (monto.compareTo(new BigDecimal(0)) != 0)
					monto = monto.multiply(porcentaje).divide(new BigDecimal(100));
			}
			
			Timestamp fechaEstimada = new Timestamp(fecha.getTimeInMillis());
			
			if(specificPayDate!=null)
				fechaEstimada = specificPayDate;
			
			X_XX_VSI_Client client = new X_XX_VSI_Client(Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_IMPORTCOMPANY_ID"), null);
			vecIContrato.add(rs.getInt("XX_Contract_ID"));
			vecIContrato.add(rs.getInt("C_BPartner_ID"));
			vecIContrato.add(rs.getInt("PO_PaymentTerm_ID"));
			vecIContrato.add(rs.getInt("C_Currency_ID"));
			vecIContrato.add(rs.getInt("XX_VendorType_ID"));
			vecIContrato.add(client.getXX_Company());

			/*Verifica si la cuenta está creada en las cuentas por pagar estimadas 
			  a partir de identificador del contrato y la fecha estimada*/
			int estimacionID = buscarEstimacion(fechaEstimada,rs.getInt("XX_Contract_ID"), rs.getInt("C_BPartner_ID"));
			
			if (cantPagos == 1){
				if (((fecha.get(Calendar.DATE) < dateTo.get(Calendar.DATE)) && 
						(fecha.get(Calendar.MONTH) == dateTo.get(Calendar.MONTH))) || 
							((fecha.get(Calendar.YEAR) <= dateTo.get(Calendar.YEAR)))){
					
					if (estimacionID == 0){
						//Anexar monto a Estimacion Final
						BigDecimal oldAmount = BigDecimal.ZERO;
						if (rs.getBigDecimal(17).compareTo(new BigDecimal(0)) != 0){
							oldAmount = calcularMonto(rs.getString("XX_ApplicablePercentage"), fecha.get(Calendar.YEAR), 
									fecha.get(Calendar.MONTH)+1, rs.getInt("M_Warehouse_ID"));
							if (oldAmount.compareTo(new BigDecimal(0)) != 0)
								oldAmount = oldAmount.multiply(porcentaje).divide(new BigDecimal(100));
							
							percentageRemainder = percentageRemainder.add(monto.subtract(oldAmount));
						}
						
						alreadyPaid++;
					}
					else{
						//ModificarContrato
						modificarEstimacion(estimacionID, monto);
						//System.out.println("Modificar - Monto nuevo: " + monto);
					}
					
				}else{
					//System.out.println("El último pago no está dentro del período establecido");
					ADialog.error(1, new Container(), "XX_LastPayNoRequired");
				}				
			}else{
				
				if (estimacionID == 0){
					//Anexar monto a Estimacion Final
					BigDecimal oldAmount = BigDecimal.ZERO;
					if (rs.getBigDecimal(17).compareTo(new BigDecimal(0)) != 0){
						oldAmount = calcularMonto(rs.getString("XX_ApplicablePercentage"), fecha.get(Calendar.YEAR), 
								fecha.get(Calendar.MONTH)+1, rs.getInt("M_Warehouse_ID"));
						if (oldAmount.compareTo(new BigDecimal(0)) != 0)
							oldAmount = oldAmount.multiply(porcentaje).divide(new BigDecimal(100));
					
						percentageRemainder = percentageRemainder.add(monto.subtract(oldAmount));
					}
					
					alreadyPaid++;
				}
				else{
					//Modificar Estimacion
					modificarEstimacion(estimacionID, monto);
					//System.out.println("Modificar - Monto nuevo: " + monto);
				}
			}
			
			cantPagos--;
		}
		
		//Pago del remanente
		if(cantPagos==0 && alreadyPaid>0){
			java.util.Date date= new java.util.Date();
			
			if(newAmount.compareTo(BigDecimal.ZERO) != 0){
				//System.out.println("Remanente (Monto) - Monto Final: " + remainder.multiply(BigDecimal.valueOf(alreadyPaid)));
				if(remainder.compareTo(BigDecimal.ZERO)>0)
					util.crearEstimacion(vecIContrato, vecS, remainder.multiply(BigDecimal.valueOf(alreadyPaid)), new Timestamp(date.getTime()), "Contrato", rs.getString("Description") + " (remanente recalculado)", rs.getInt("XX_PayContract_ID"));
			}
			else{
				//System.out.println("Remanente (%) - Monto Final: ");
				util.crearEstimacion(vecIContrato, vecS, percentageRemainder, new Timestamp(date.getTime()), "Contrato", rs.getString("Description"), rs.getInt("XX_PayContract_ID"));
			}
		}
	}
	
	/**
	 * Verifica si el contrato para esa fecha estimada fue o no creado
	 * @param fechaEstimada
	 * @param idContrato
	 * @return bool si es true, es porque ya esta creada la estimacion del contrato para la fecha
	 */
	public int buscarEstimacion(Timestamp fechaEstimada, int idContrato, int partner){
		
		int ID = 0;

		Calendar fechaE = Calendar.getInstance();
		fechaE.setTime(fechaEstimada);
		String sql = "select XX_VCN_EstimatedAPayable_ID " +
					 "from XX_VCN_EstimatedAPayable " +
					 "where XX_Contract_ID = " + idContrato + " AND C_BPartner_ID = " + partner +
					 " and XX_DateEstimated = to_date ('" + fechaE.get(Calendar.YEAR) + "-" +
					 ((fechaE.get(Calendar.MONTH))+1)+"-"+fechaE.get(Calendar.DATE)+"','yyyy-MM-dd') ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				ID = rs.getInt(1);
			}
			
		}catch (SQLException e){
			log.log(Level.SEVERE, sql, e);			
		}finally{
			try {
				pstmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ID;
	}
	
	/**
	 * Se encarga de retornar la fecha (día) del día jueves, 
	 * de la primera semana de una fecha dada
	 * @param fecha
	 * @return
	 */
	public int diaAnticipado(String fecha){
		int dia = 0;
		Calendar dateCalendar = new GregorianCalendar();
		Date date = new Date();
		String sql = "select next_day('" + fecha + "','jueves') from dual";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				date = rs.getDate(1);
				dateCalendar.setTimeInMillis(date.getTime());
				dia = dateCalendar.get(Calendar.DATE);
				if (dia == 8){
					dia = Integer.valueOf(fecha.substring(1, 2));
				}
			}
		}catch (SQLException e){
			log.log(Level.SEVERE, sql, e);			
		}finally{
			try {
				pstmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dia;
	}
	
	/**
	 * Se encarga de calcular el monto de acuerdo al porcentaje aplicable
	 * @param porcentajeAplicable porcentaje aplicable
	 * @param dateFrom fecha desde
	 * @param dateTo fecha hasta
	 * @param idTienda identificador de la tienda
	 * @return
	 */
	public BigDecimal calcularMonto(String porcentajeAplicable, int año, int mes, int idTienda){
		BigDecimal monto = new BigDecimal(0);
		String sql = "";
		String mesP = String.valueOf(mes);
		
		if (mesP.length() == 1)
			mesP = "0" + mesP;		
		
		switch(porcentajeAplicable.hashCode()){
 		case 'V':
 			//System.out.println("Ventas Brutas");			
 			sql = "select sum(XX_SalesAmountBud2) " +
 					"from XX_VMR_Prld01 " +
 					"where XX_BudgetYearMonth = '" + año + "" + mesP + "' ";
 			break;
 		case 'M':
 			//System.out.println("Ventas por Marca");
 			monto = new BigDecimal(0);
 			return monto;
 		case 'W':
 			//System.out.println("Ventas Brutas por Tienda");
 			sql = "select sum(XX_SalesAmountBud2) " +
 					"from XX_VMR_Prld01 " +
 					"where XX_BudgetYearMonth = '" + año + "" + mesP + "' " +
 					"and M_Warehouse_ID = " + idTienda;
 			break;
 		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				monto = rs.getBigDecimal(1);
			}else{
				monto = new BigDecimal(0);
			}
			
		}catch(SQLException e){
			log.log(Level.SEVERE, sql, e);
		}finally{
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (monto == null)
			monto = new BigDecimal(0);
		
		return monto;
	}
	
	/**
	 * Agrupa los valores de monto fijo y del variable para llamar al 
	 * método que se encarga de crear la estimación del contrato
	 * @param rsContrato ResultSet del query principal
	 * @throws SQLException
	 */
	public void tipoPagoContrato(ResultSet rsContrato, String tipo) throws SQLException{

		Timestamp dateFrom = rsContrato.getTimestamp("XX_DateFrom");
		
		//Fecha desde ingresada
		dateFrom = newDateFrom;
		
		Timestamp dateTo = rsContrato.getTimestamp("XX_DateTo");
		int cantPagosF = 0;
		int cantPagosV = 0;
		String recurrenciaF = rsContrato.getString("XX_PaymentRecurrency1"); 
		String recurrenciaV = rsContrato.getString("XX_PaymentRecorrency2"); 			
		int meses = mesesPeriodos(dateFrom, dateTo);
		
		if (tipo.equals("A")){
			cantPagosF = recurrenciaContrato(recurrenciaF, meses);
			cantPagosV = recurrenciaContrato(recurrenciaV, meses);
		}
		if (tipo.equals("V")){
			cantPagosV = recurrenciaContrato(recurrenciaV, meses);
		}
		if (tipo.equals("F")){
			cantPagosF = recurrenciaContrato(recurrenciaF, meses);
		}

		//Pagos Fijos
		if ((cantPagosF != 0)){
			estimacion(rsContrato,cantPagosF, newAmount,recurrenciaF, rsContrato.getInt("XX_PayDay1"), 
					rsContrato.getString("XX_Anticipated1"), new BigDecimal(0), rsContrato.getTimestamp("XX_SpecificPayDate1"));
		}
		
		//Pagos Variables
		if((cantPagosV != 0)){
			estimacion(rsContrato,cantPagosV,new BigDecimal(0),recurrenciaV, rsContrato.getInt("XX_PayDay2"),
					rsContrato.getString("XX_Anticipated2"), newPercentage, rsContrato.getTimestamp("XX_SpecificPayDate2"));
		}
	}
	
	/**
	 * Se encarga de buscar la cantidad de pagos dependiendo del tipo de recurrencia
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
	 * Se encarga de buscar la cantidad de meses entre la fecha desde 
	 * y la fecha hasta, del pago del contrato
	 * @param dateFrom fecha desde
	 * @param dateTo fecha hasta
	 * @return
	 */
	public int mesesPeriodos(Timestamp dateFrom, Timestamp dateTo){
		int meses = 0;		
		String sql = "select round(months_between(" + DB.TO_DATE(dateTo) + ", " +
				  		DB.TO_DATE(dateFrom) + ")) meses " +
				  	 "from dual";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {				
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();	
			if(rs.next()) {	
				meses = rs.getInt(1);
			}
		}
		catch(SQLException e){	
			e.getMessage();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return meses;
	}
	
	/**
	 * Verifica si el contrato para esa fecha estimada fue o no creado
	 * @param fechaEstimada
	 * @param idContrato
	 * @return bool si es true, es porque ya esta creada la estimacion del contrato para la fecha
	 */
	private void modificarEstimacion(int estimacionID, BigDecimal monto){

		X_XX_VCN_EstimatedAPayable estimacion = new X_XX_VCN_EstimatedAPayable( Env.getCtx(), estimacionID, null); 
		
		if (estimacion.getC_Currency_ID() == 205){
			estimacion.setXX_AmountLocal(monto);
			estimacion.setXX_AmountForeign(BigDecimal.ZERO);
		}else{
			estimacion.setXX_AmountForeign(monto);
			estimacion.setXX_AmountLocal(BigDecimal.ZERO);
		}
		
		estimacion.save();
	}
}
