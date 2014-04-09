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
import org.compiere.util.Msg;

import compiere.model.cds.MInvoice;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VSI_Client;

/**
 * Se encarga de crear las cuentas estimadas y la factura de un contrato
 * @author Jessica Mendoza
 *
 */
public class XX_EstimatedAPContract extends SvrProcess{

	Utilities util = new Utilities();
	String idContract = "";
	
	@Override
	protected String doIt() throws Exception {
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
					 "and cont.XX_Contract_ID = " + idContract;

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
		
		return Msg.getMsg(Env.getCtx(), "XX_ProcessComplete");
	}

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_Contract_ID"))
				idContract = element.getInfo();
		}
	}
	
	/**
	 * Agrupa los valores de monto fijo y del variable para llamar al 
	 * método que se encarga de crear la estimación del contrato
	 * @param rsContrato ResultSet del query principal
	 * @throws SQLException
	 */
	public void tipoPagoContrato(ResultSet rsContrato, String tipo) throws SQLException{

		Timestamp dateFrom = rsContrato.getTimestamp("XX_DateFrom"); 
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

		if ((cantPagosF != 0) || (cantPagosV != 0)){
			estimacion(rsContrato,cantPagosF,rsContrato.getBigDecimal(10),recurrenciaF,
					rsContrato.getInt("XX_PayDay1"),rsContrato.getString("XX_Anticipated1"),new BigDecimal(0), rsContrato.getTimestamp("XX_SpecificPayDate1"));
			estimacion(rsContrato,cantPagosV,new BigDecimal(0),recurrenciaV,
					rsContrato.getInt("XX_PayDay2"),rsContrato.getString("XX_Anticipated2"),
					rsContrato.getBigDecimal("XX_CPercentage"), rsContrato.getTimestamp("XX_SpecificPayDate2"));
		}
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
		dateFrom.setTime(rs.getTimestamp("XX_DateFrom"));
		
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
			
			//Fecha Estimada (Especifica)
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
			boolean boolContrato = buscarEstimacion(fechaEstimada,rs.getInt("XX_Contract_ID"), rs.getInt("C_BPartner_ID"));
			
			if (cantPagos == 1){
				if (((fecha.get(Calendar.DATE) < dateTo.get(Calendar.DATE)) && 
						(fecha.get(Calendar.MONTH) == dateTo.get(Calendar.MONTH))) || 
							((fecha.get(Calendar.YEAR) <= dateTo.get(Calendar.YEAR)))){
					
					if (!boolContrato)
						util.crearEstimacion(vecIContrato, vecS, monto, fechaEstimada, "Contrato",rs.getString("Description"), rs.getInt("XX_PayContract_ID"));
				}else{
					//System.out.println("El último pago no está dentro del período establecido");
					ADialog.error(1, new Container(), "XX_LastPayNoRequired");
				}				
			}else{
				
				if (!boolContrato)
					util.crearEstimacion(vecIContrato, vecS, monto, fechaEstimada, "Contrato",rs.getString("Description"), rs.getInt("XX_PayContract_ID"));
			}	
			cantPagos--;
		}
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
	 * Verifica si el contrato para esa fecha estimada fue o no creado
	 * @param fechaEstimada
	 * @param idContrato
	 * @return bool si es true, es porque ya esta creada la estimacion del contrato para la fecha
	 */
	public boolean buscarEstimacion(Timestamp fechaEstimada, int idContrato, int partner){
		boolean bool = false;
		Calendar fechaECXP = Calendar.getInstance();
		Calendar fechaE = Calendar.getInstance();
		fechaE.setTime(fechaEstimada);
		String sql = "select XX_DateEstimated " +
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
				if (rs.getTimestamp(1) == null)
					bool = false;
				else{
					fechaECXP.setTime(rs.getTimestamp(1));
					if (fechaECXP.get(Calendar.MONTH) == fechaE.get(Calendar.MONTH))
						bool = true;
					else
						bool = false;
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
		return bool;
	}

	/**
	 * Busca si ya esta creada la factura del contrato
	 * @param idContrato id del contrato
	 * @param fecha fecha estimada
	 * @return bool si bool es true, es porque ya esta creada la factura para dicho contrato
	 */
	public boolean buscarFactura(Timestamp fecha, int idContrato){
		boolean bool = false;
		Calendar fechaF = Calendar.getInstance();
		Calendar fechaE = Calendar.getInstance();
		fechaE.setTime(fecha);

		String sql = "select XX_DueDate " +
					 "from C_Invoice " +
					 "where XX_Contract_ID = " + idContrato + " " +
					 "and XX_DueDate = to_date ('" + fechaE.get(Calendar.YEAR) + "-" +
					 ((fechaE.get(Calendar.MONTH))+1)+"-"+fechaE.get(Calendar.DATE)+"','yyyy-MM-dd') ";	
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				if (rs.getTimestamp(1) == null)
					bool = false;
				else{
					fechaF.setTime(rs.getTimestamp("XX_DueDate"));
					if (fechaF.get(Calendar.MONTH) == fechaE.get(Calendar.MONTH))
						bool = true;
					else
						bool = false;
				}
			}
			
		}catch (SQLException e){
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
		
		return bool;
	}
	
	/**
	 * Busca la localidad del Socio de Negocio
	 * @param idPartner id del socio de negocio
	 * @return id de la localidad
	 */
	public int buscarLocaProveedor(int idPartner){
		String sql = "select C_BPartner_Location_ID " +
					 "from C_BPartner_Location " +
					 "where C_BPartner_ID = " + idPartner;
		int idLocation = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				idLocation = rs.getInt(1);
			}
			
		}catch (SQLException e){
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
		return idLocation;
	}
	
	/**
	 * Busca el valor de la categoria del impuesto del proveedor
	 * @param idPartner id de proveedor
	 * @return
	 */
	public BigDecimal buscarCategoriaIP(int idPartner){
		BigDecimal impuesto = new BigDecimal(0);
		String sql = "select tax.rate " +
					 "from C_BPartner par, C_TaxCategory cat, C_Tax tax " +
					 "where par.C_TaxCategory_ID = cat.C_TaxCategory_ID " +
					 "and cat.C_TaxCategory_ID = tax.C_TaxCategory_ID " +
					 "and par.C_BPartner_ID = " + idPartner;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				impuesto = rs.getBigDecimal(1);
			}
			
		}catch (SQLException e){
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
		return impuesto;
	}
	
	/**
	 * Crea la factura para un contrato
	 * @param vector contiene algunos datos para crear la factura
	 * @param monto
	 * @param fecha
	 */
	public void crearFactura(Vector<Integer> vector, BigDecimal monto, Timestamp fecha){
		Date fechaS = new Date();
		Timestamp fechaServidor = new Timestamp(fechaS.getTime());
		BigDecimal cero = new BigDecimal(0);
		MInvoice invoice = new MInvoice(Env.getCtx(),0,get_TrxName());
		invoice.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
		invoice.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
		invoice.setXX_Contract_ID(vector.get(0));
		invoice.setC_BPartner_ID(vector.get(1));
		invoice.setC_Currency_ID(vector.get(2));
		invoice.setC_BPartner_Location_ID(buscarLocaProveedor(vector.get(1)));
		invoice.setC_DocTypeTarget_ID(Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID"));
		invoice.setC_PaymentTerm_ID(vector.get(3));
		invoice.setDateAcct(fechaServidor);
		invoice.setDateInvoiced(fechaServidor);
		invoice.setXX_DatePaid(fecha);
		invoice.setXX_DueDate(fecha);
		
		//FALTA: fecha y usuario de aprobacion del contrato y el estado de la facturación
		invoice.setAD_User_ID(personContactVendor(vector.get(1))); //persona contacto del socio de negocio			
		invoice.setDocStatus("DR");
				
		invoice.setTotalLines(monto);
		invoice.setXX_InvoiceType("S");
		invoice.setIsSOTrx(false);
		BigDecimal impuesto = new BigDecimal(100);
		impuesto = buscarCategoriaIP(vector.get(1)).divide(impuesto);
		
		if (impuesto.equals(cero)){
			invoice.setXX_TaxAmount(new BigDecimal(0));
			invoice.setGrandTotal(monto);
		}else{
			invoice.setXX_TaxAmount(monto.multiply(impuesto));
			invoice.setGrandTotal((monto.multiply(impuesto)).add(monto));
		}
		invoice.setXX_ParcelQty(1);
		invoice.save();
		commit();
		
		if (vector.get(2) == 205)
			invoice.setXX_GrandTotalLocal(invoice.getGrandTotal().
					subtract(new BigDecimal(util.retencion(invoice.getC_Invoice_ID()))));
		else
			invoice.setXX_GrandTotalLocal(new BigDecimal(0));
		//revisar
		invoice.save();
		commit();
	}
	
	/**
	 * Se encarga de buscar la persona contacto de un proveedor específico
	 * @param idPartner identificador de socio de negocio
	 * @return
	 */
	public int personContactVendor(int idPartner){
		int idContact = 0;
		String sql = "select max(AD_User_ID) " +
					 "from AD_User " +
					 "where C_BPartner_ID = " + idPartner;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				idContact = rs.getInt(1);
			}
		}catch (SQLException e){
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
		return idContact;
	}
}
