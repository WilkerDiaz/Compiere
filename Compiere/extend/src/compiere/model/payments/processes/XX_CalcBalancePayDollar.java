package compiere.model.payments.processes;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import compiere.model.payments.X_XX_VCN_PaymentDollars;

/**
 * Proceso que se encarga de generar los saldos finale e iniciales en el Pago en Dolares
 * @author Jessica Mendoza, Modificado por JTRIAS
 *
 */
public class XX_CalcBalancePayDollar extends SvrProcess{

	X_XX_VCN_PaymentDollars paymentDollars = null;
	Timestamp fechaActual = new Timestamp(Calendar.getInstance().getTimeInMillis());	
	BigDecimal rate = new BigDecimal(0);
	BigDecimal amountForeign = new BigDecimal(0);
	BigDecimal amountLocal = new BigDecimal(0);
	BigDecimal balanceForeign = new BigDecimal(0);
	BigDecimal balanceLocal = new BigDecimal(0);
	BigDecimal averageExchange = new BigDecimal(0);
	BigDecimal rateCons = new BigDecimal(0);
	BigDecimal amountForeignCons = new BigDecimal(0);
	Calendar dateInit = Calendar.getInstance();
	Calendar dateFinal = Calendar.getInstance();
	Calendar dateFirstFinal = Calendar.getInstance();
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();
		Integer sMonth = 0;
		Integer sYear = 0;
		
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_Month"))
				sMonth = new Integer(element.getParameter().toString());
			else if (name.equals("XX_Year"))
				sYear = new Integer(element.getParameter().toString());
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		sMonth = sMonth - 1;
		
		//EL MES SELECCIONADO SERÁ USADO PARA CREAR EL SALDO FINAL (EJMP: SELECCIONADO "ENERO" ENTONCES ENERO -> SALDO FINAL, FEBRERO -> SALDO INICIAL)
		if(sMonth == 11){
			
			dateFinal.set(1, sYear);
			dateFinal.set(2, sMonth);
			dateInit.set(1, sYear + 1);
			dateInit.set(2, 1);
		}
		else{
			dateFinal.set(1, sYear);
			dateFinal.set(2, sMonth);
			dateInit.set(1, sYear);
			dateInit.set(2, sMonth + 1);
		}
		
		dateInit.set(Calendar.DAY_OF_MONTH, 1);
		dateInit.set(Calendar.HOUR_OF_DAY, 0);
		dateInit.set(Calendar.MINUTE, 0);
		dateFinal.set(Calendar.DAY_OF_MONTH, dateFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
		dateFinal.set(Calendar.HOUR_OF_DAY, 23);
		dateFinal.set(Calendar.MINUTE, 59);
		
		dateFirstFinal.setTime(dateFinal.getTime());
		dateFirstFinal.set(Calendar.DAY_OF_MONTH, 1);
		dateFirstFinal.set(Calendar.HOUR_OF_DAY, 0);
		dateFirstFinal.set(Calendar.MINUTE, 0);
	}

	@Override
	protected String doIt() throws Exception {
		
		//Calculo de saldos
		calc("Final", "S", dateFirstFinal, dateFinal);
		calc("Final", "O", dateFirstFinal, dateFinal);
		calc("Final", "B", dateFirstFinal, dateFinal);
			
		calc("Inicial", "S", dateFirstFinal, dateFinal);
		calc("Inicial", "O", dateFirstFinal, dateFinal);
		calc("Inicial", "B", dateFirstFinal, dateFinal);
	
		return "Finalizado";
	}
	
	/**
	 * Calcula los saldos finales e iniciales
	 */
	public void calc(String typeBalance, String typeRate, Calendar date1, Calendar date2){
		
		String sql = "select SUM(XX_AMOUNTLOCAL), SUM(XX_AMOUNTFOREIGN), SUM(XX_AMOUNTLOCAL)/SUM(XX_AMOUNTFOREIGN) from XX_VCN_PaymentDollars " +
				  	 "where XX_FinalBalance = 'N' " +
				  	 "AND TO_DATE(TO_CHAR(DATE1, 'dd-mm-yyyy'), 'dd-mm-yyyy') <= ? AND TO_DATE(TO_CHAR(DATE1, 'dd-mm-yyyy'), 'dd-mm-yyyy') >= ? " +
				  	 "AND XX_RATETYPE = '" + typeRate + "' AND XX_FinalBalance = 'N' ";
			
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		balanceLocal = BigDecimal.ZERO;
		balanceForeign = BigDecimal.ZERO;
		averageExchange = BigDecimal.ZERO;
		
		try{
			pstmt = DB.prepareStatement(sql, null); 
			pstmt.setDate(1, new java.sql.Date(date2.getTime().getTime()));
			pstmt.setDate(2, new java.sql.Date(date1.getTime().getTime()));
			
			rs = pstmt.executeQuery();
			
			if(rs.next() && rs.getBigDecimal(1)!=null){	
				
				balanceLocal = rs.getBigDecimal(1);
				balanceForeign = rs.getBigDecimal(2);
				averageExchange = rs.getBigDecimal(3);
			}
			else
				return;
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		if (typeBalance.equals("Final")){
			
			Timestamp date = new Timestamp(date2.getTimeInMillis());
			createdBalanceInPayDollars("Saldo Final al ", typeRate, date, new java.sql.Date(date2.getTime().getTime()), true);
			
		}else{
			Timestamp date = new Timestamp(dateInit.getTimeInMillis());
			createdBalanceInPayDollars("Saldo Inicial al ", typeRate, date, new java.sql.Date(dateInit.getTime().getTime()), false);			
		}
	}
	
	/**
	 * Se encarga de crear el saldo en Pago en Dolares
	 * @param concept concepto
	 * @param type tipo de tasa
	 * @param fecha
	 */
	public void createdBalanceInPayDollars(String concept, String type, Timestamp fecha, Date dateToFind, boolean isFinal){
		
		paymentDollars = new X_XX_VCN_PaymentDollars(Env.getCtx(), getBalance(dateToFind, isFinal, type), null);
		paymentDollars.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
		paymentDollars.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
		paymentDollars.setXX_IsAmount(true);
		paymentDollars.setDate1(fecha);
		
		String typeConcept = "";
		if(type.equalsIgnoreCase("S"))
			typeConcept = "SITME";
		else if(type.equalsIgnoreCase("B"))
			typeConcept = "BONO";
		else
			typeConcept = "DIFERENTE";
		
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern("dd/MM/yyyy");

		paymentDollars.setXX_Concept(concept + " "+ df.format(fecha.getTime()) + " - " + typeConcept);
		paymentDollars.setXX_RateType(type);
		paymentDollars.setXX_RateTypeSitme("CP");
		
		if(isFinal)
			paymentDollars.setXX_FinalBalance(true);
		else
			paymentDollars.setXX_InitialBalance(true);
		
		paymentDollars.setXX_AmountForeign(balanceForeign);			
		paymentDollars.setXX_AmountLocal(balanceLocal);
		paymentDollars.setXX_Rate(averageExchange);		
				
		paymentDollars.save();		
	}
	
	private int getBalance(Date date, boolean isFinal, String type){
		
		String sql = "select XX_VCN_PaymentDollars_ID from XX_VCN_PaymentDollars " +
				  	 "where XX_RATETYPE = '"+ type +"' AND TO_DATE(TO_CHAR(DATE1, 'dd-mm-yyyy'), 'dd-mm-yyyy') = ? AND ";
		
		if(isFinal)
			sql += "XX_FinalBalance = 'Y'";
		else
			sql += "XX_InitialBalance = 'Y'";
			
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		int id=0;
		
		try{
			pstmt = DB.prepareStatement(sql, null); 
			pstmt.setDate(1, date);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				id = rs.getInt("XX_VCN_PaymentDollars_ID");
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return id;
	}

}
