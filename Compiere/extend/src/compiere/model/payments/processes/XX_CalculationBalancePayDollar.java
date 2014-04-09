package compiere.model.payments.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.payments.X_XX_VCN_PaymentDollars;

/**
 * Proceso que se encarga de generar los saldos finale e iniciales en el Pago en Dolares
 * @author Jessica Mendoza
 *
 */
public class XX_CalculationBalancePayDollar extends SvrProcess{

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
	BigDecimal amountLocalCons = new BigDecimal(0);
	BigDecimal balanceForeignCons = new BigDecimal(0);
	BigDecimal balanceLocalCons = new BigDecimal(0);
	BigDecimal averageExchangeCons = new BigDecimal(0);
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {

		if (fechaActual.getDate() == 1){
			//buscar valor de la tasa de cambio, según la fecha
			maxRecord(" < 4.3 ", "Final", "Oficial");
			maxRecord(" > 4.3 ", "Final", "Sitme");
			maxRecord(" < 4.3 ", "Inicial", "Oficial");
			maxRecord(" > 4.3 ", "Inicial", "Sitme");			
			return Msg.getMsg(Env.getCtx(), "XX_ProcessComplete");
		}else{
			Logger.getLogger(Msg.getMsg(Env.getCtx(), "XX_ErrorProcessPayDollars"));
			return Msg.getMsg(Env.getCtx(), "XX_ErrorProcessPayDollars");
		}
	}
	
	/**
	 * Busca el último registro dependiendo de la condición del parámetro
	 * @param where condición (<=4.3) o (>4.3)
	 * @param typeBalance tipo de saldo (final-inicial)
	 * @param typeRate tipo de tasa (sitme-oficial-consolidado)
	 */
	public void maxRecord(String condition, String typeBalance, String typeRate){
		String sql = "select max(XX_VCN_PaymentDollars_ID) from XX_VCN_PaymentDollars where XX_Rate " + condition;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){		
				paymentDollars = new X_XX_VCN_PaymentDollars(Env.getCtx(), rs.getInt(1), null);
				rate = paymentDollars.getXX_Rate();
				rateCons = rateCons.add(rate);
				amountForeign = paymentDollars.getXX_AmountForeign();
				amountForeignCons = amountForeignCons.add(amountForeign);
				amountLocal = paymentDollars.getXX_AmountLocal();
				amountLocalCons = amountLocalCons.add(amountLocal);
				balanceForeign = paymentDollars.getXX_BalanceForeign();
				balanceForeignCons = balanceForeignCons.add(balanceForeign);
				balanceLocal = paymentDollars.getXX_BalanceLocal();
				balanceLocalCons = balanceLocalCons.add(balanceLocal);
				averageExchange = paymentDollars.getXX_AverageExchange();
				averageExchangeCons = averageExchangeCons.add(averageExchange);
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
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
		
		if (typeBalance.equals("Final")){
			GregorianCalendar calendar = new GregorianCalendar(); 
			calendar.setTime(fechaActual); 
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)-1);
			Timestamp fechaFinal = new Timestamp(calendar.getTimeInMillis());
			createdBalanceInPayDollars("Saldo Final al ", "Consolidado", fechaFinal);
			if (typeRate.equals("Sitme"))
				createdBalanceInPayDollars("Saldo Final al ", "Sitme", fechaFinal);
			else
				createdBalanceInPayDollars("Saldo Final al ", "Oficial", fechaFinal);		
		}else{
			createdBalanceInPayDollars("Saldo Inicial al ", "Consolidado", fechaActual);
			if (typeRate.equals("Sitme"))
				createdBalanceInPayDollars("Saldo Inicial al ", "Sitme", fechaActual);
			else
				createdBalanceInPayDollars("Saldo Inicial al ", "Oficial", fechaActual);			
		}
	}
	
	/**
	 * Se encarga de crear el saldo en Pago en Dolares
	 * @param concept concepto
	 * @param type tipo de tasa
	 * @param fecha
	 */
	public void createdBalanceInPayDollars(String concept, String type, Timestamp fecha){
		paymentDollars = new X_XX_VCN_PaymentDollars(Env.getCtx(), 0, null);
		paymentDollars.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
		paymentDollars.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
		paymentDollars.setXX_IsAmount(true);
		paymentDollars.setDate1(fecha);
		paymentDollars.setXX_Concept(concept + fecha + " - " + type);
		
		if (type.equals("Consolidado"))
			paymentDollars.setXX_ConsolidatedRate(true);
		else if (type.equals("Sitme")){
			paymentDollars.setXX_RateTypeSitme("S");
		}else{
			paymentDollars.setXX_RateTypeSitme("O");
		}
		
		if (type.equals("Consolidado")){
			paymentDollars.setXX_AmountForeign(amountForeignCons);
			paymentDollars.setXX_AmountLocal(amountLocalCons);
			paymentDollars.setXX_Rate(rateCons);
			paymentDollars.setXX_BalanceForeign(balanceForeignCons);
			paymentDollars.setXX_BalanceLocal(balanceLocalCons);
			paymentDollars.setXX_AverageExchange(averageExchangeCons);
		}else{
			paymentDollars.setXX_AmountForeign(amountForeign);
			paymentDollars.setXX_AmountLocal(amountLocal);
			paymentDollars.setXX_Rate(rate);
			paymentDollars.setXX_BalanceForeign(balanceForeign);
			paymentDollars.setXX_BalanceLocal(balanceLocal);
			paymentDollars.setXX_AverageExchange(averageExchange);			
		}
		paymentDollars.save();		
	}

}
