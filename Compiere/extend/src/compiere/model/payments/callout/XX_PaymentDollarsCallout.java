package compiere.model.payments.callout;


import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.payments.X_XX_VCN_PaymentDollars;

/**
 * Callout para las validaciones de Pago en Dolares
 * @author Jessica Mendoza
 *
 */
public class XX_PaymentDollarsCallout extends CalloutEngine{
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(XX_PaymentDollarsCallout.class);
	X_XX_VCN_PaymentDollars payDollars;
	List<X_XX_VCN_PaymentDollars> listPayDollars = new ArrayList <X_XX_VCN_PaymentDollars> ();
	BigDecimal rate = new BigDecimal(0);
	BigDecimal amountForeign = new BigDecimal(0);
	BigDecimal amountLocal = new BigDecimal(0);
	BigDecimal balanceForeign = new BigDecimal(0);
	BigDecimal balanceLocal = new BigDecimal(0);
	
	/**
	 * Se encarga de buscar el valor de la tasa oficial
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @param oldValue
	 * @return			compiere.model.payments.callout.XX_PaymentDollarsCallout.rateOfficial
	 */
	public String rateOfficial(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue) {
		String rateType = (String) mField.getValue();
		
		if (rateType == null)
			return "";
		else{
			if (rateType.equals("O")){
				Timestamp fecha = (Timestamp) mTab.getValue("Date1");
				mTab.setValue("XX_Rate", valueRateOfficial(fecha));	
			}	
		}
		return "";
	}

	/**
	 * Calcula la tasa de cambio, montos y saldos, dependiendo del tipo de cambio seleccionado
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @param oldValue
	 * @return			compiere.model.payments.callout.XX_PaymentDollarsCallout.calculationRate
	 */
	public String calculationRate(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue) {
		
		String typeRateSitme = (String) mField.getValue();
		String sql = "";
		boolean flag = false;

		if (typeRateSitme == null)
			return "";
		else{ 
			amountForeign = (BigDecimal) mTab.getValue("XX_AmountForeign");
			amountLocal = (BigDecimal) mTab.getValue("XX_AmountLocal");	
			rate = (BigDecimal) mTab.getValue("XX_Rate");
			
			if (typeRateSitme.equals("CP")){			
				if (amountForeign.compareTo(new BigDecimal(0)) == 0)
					ADialog.error(WindowNo, new Container(), Msg.getMsg(Env.getCtx(), "XX_EnterAmountForeign"));
				else{
					//Busca el ID del último registro
					sql = "select max(XX_VCN_PaymentDollars_ID) " +
						  "from XX_VCN_PaymentDollars " +
						  "where XX_RateType = '" + mTab.getValue("XX_RateType") + "' ";
					PreparedStatement pstmt = null; 
					ResultSet rs = null;
					try{
						pstmt = DB.prepareStatement(sql, null); 
						rs = pstmt.executeQuery();
						if(rs.next()){		
							payDollars = new X_XX_VCN_PaymentDollars(Env.getCtx(),rs.getInt(1),null);
							/*if (payDollars.isXX_IsAmount()){
								//mTab.setValue("XX_IsTypeRate", true);
								String rateType = (String) mTab.getValue("XX_RateType");
								listPayDollars = maxRecord();
								int id = 0;
								for (int i=0; i<listPayDollars.size(); i++){
									if (listPayDollars.get(i).getXX_RateTypeSitme().equals(rateType))
										id = listPayDollars.get(i).getXX_VCN_PaymentDollars_ID();
								}
								payDollars = new X_XX_VCN_PaymentDollars(Env.getCtx(),id,null);
								mTab.setValue("XX_Rate", payDollars.getXX_Rate());
								mTab.setValue("XX_AmountLocal", amountForeign.multiply(payDollars.getXX_Rate()));
								amountLocal = amountForeign.multiply(payDollars.getXX_Rate());
								flag = true;
							}else{*/
								mTab.setValue("XX_Rate", payDollars.getXX_Rate());
								mTab.setValue("XX_AmountLocal", amountForeign.multiply(payDollars.getXX_Rate()));
								amountLocal = amountForeign.multiply(payDollars.getXX_Rate());
								flag = true;
							//}
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
				}
			}else if (typeRateSitme.equals("IU")){
				if (amountForeign.compareTo(new BigDecimal(0)) == 0)
					ADialog.error(WindowNo, new Container(), Msg.getMsg(Env.getCtx(), "XX_EnterAmountForeign"));
				else if (rate.compareTo(new BigDecimal(0)) == 0)
					ADialog.error(WindowNo, new Container(), Msg.getMsg(Env.getCtx(), "XX_EnterRate"));
			}else if (typeRateSitme.equals("MS")){			
				if (amountForeign.compareTo(new BigDecimal(0)) == 0)
					ADialog.error(WindowNo, new Container(), Msg.getMsg(Env.getCtx(), "XX_EnterAmountForeign"));
				else if (amountLocal.compareTo(new BigDecimal(0)) == 0)
					ADialog.error(WindowNo, new Container(), Msg.getMsg(Env.getCtx(), "XX_EnterAmountLocal"));
			}
			//Calcula los Saldos
			if (flag){
				balanceForeign = payDollars.getXX_BalanceForeign().add(amountForeign);
				balanceLocal = payDollars.getXX_BalanceLocal().add(amountLocal);
				mTab.setValue("XX_BalanceForeign", balanceForeign);
				mTab.setValue("XX_BalanceLocal", balanceLocal);			
				mTab.setValue("XX_AverageExchange", balanceLocal.divide(balanceForeign, RoundingMode.DOWN));
			}
		}
		return "";
	}
	
	/**
	 * Calcula los montos y saldos, para el tipo de cambio que es ingresado por el usuario
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @param oldValue
	 * @return			compiere.model.payments.callout.XX_PaymentDollarsCallout.typeRateIU
	 */
	public String typeRateIU(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue) {		
		rate = (BigDecimal) mField.getValue();
		amountForeign = (BigDecimal) mTab.getValue("XX_AmountForeign");
		amountLocal = (BigDecimal) mTab.getValue("XX_AmountLocal");
		String typeRateSitme = (String) mTab.getValue("XX_RateTypeSitme");
		boolean flag = false;
		
		if (typeRateSitme == null)
			return "";
		else{
			if (typeRateSitme.equals("IU")){
				if (amountForeign.compareTo(new BigDecimal(0)) == 0)
					ADialog.error(WindowNo, new Container(), Msg.getMsg(Env.getCtx(), "XX_EnterAmountForeign"));
				else if (rate.compareTo(new BigDecimal(0)) == 0)
					ADialog.error(WindowNo, new Container(), Msg.getMsg(Env.getCtx(), "XX_EnterRate"));
				else{
					mTab.setValue("XX_AmountLocal", amountForeign.multiply(rate));
					amountLocal = amountForeign.multiply(rate);
					flag = true;
				}
			}
		}
		
		if (flag){	
			//Busca el ID del último registro
			String sql = "select max(XX_VCN_PaymentDollars_ID) from XX_VCN_PaymentDollars ";
			PreparedStatement pstmt = null; 
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(sql, null); 
				rs = pstmt.executeQuery();
				if(rs.next()){		
					payDollars = new X_XX_VCN_PaymentDollars(Env.getCtx(),rs.getInt(1),null);
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
			
			//Validar si el anterior es un saldo
			if (payDollars.isXX_IsAmount()){
				//mTab.setValue("XX_IsTypeRate", true);
				String rateType = (String) mTab.getValue("XX_RateType");
				listPayDollars = maxRecord();
				int id = 0;
				for (int i=0; i<listPayDollars.size(); i++){
					if (listPayDollars.get(i).getXX_RateTypeSitme().equals(rateType))
						id = listPayDollars.get(i).getXX_VCN_PaymentDollars_ID();
				}
				payDollars = new X_XX_VCN_PaymentDollars(Env.getCtx(),id,null);	
			}
			
		}
		return "";
	}
	
	/**
	 * Calcula los montos y saldos, para el tipo de cambio que es Monto$/MontoBs
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @param oldValue
	 * @return			compiere.model.payments.callout.XX_PaymentDollarsCallout.typeRateMS
	 */
	public String typeRateMS(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue) {
		
		amountLocal = (BigDecimal) mField.getValue();
		amountForeign = (BigDecimal) mTab.getValue("XX_AmountForeign");
		String typeRateSitme = (String) mTab.getValue("XX_RateTypeSitme");
		boolean flag = false;
		
		if (typeRateSitme == null)
			return "";
		else{
			if (typeRateSitme.equals("MS")){
				if (amountForeign.compareTo(new BigDecimal(0)) == 0)
					ADialog.error(WindowNo, new Container(), Msg.getMsg(Env.getCtx(), "XX_EnterAmountForeign"));
				else if (amountLocal.compareTo(new BigDecimal(0)) == 0)
					ADialog.error(WindowNo, new Container(), Msg.getMsg(Env.getCtx(), "XX_EnterAmountLocal"));
				else{
					mTab.setValue("XX_Rate", amountLocal.divide(amountForeign, RoundingMode.DOWN));
					flag = true;
				}
			}
		}
		
		if (flag){	
			//Busca el ID del último registro
			String sql = "select max(XX_VCN_PaymentDollars_ID) from XX_VCN_PaymentDollars ";
			PreparedStatement pstmt = null; 
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(sql, null); 
				rs = pstmt.executeQuery();
				if(rs.next()){		
					payDollars = new X_XX_VCN_PaymentDollars(Env.getCtx(),rs.getInt(1),null);
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
			
			if (payDollars.isXX_IsAmount()){
				//mTab.setValue("XX_IsTypeRate", true);
				String rateType = (String) mTab.getValue("XX_RateType");
				listPayDollars = maxRecord();
				int id = 0;
				for (int i=0; i<listPayDollars.size(); i++){
					if (listPayDollars.get(i).getXX_RateTypeSitme().equals(rateType))
						id = listPayDollars.get(i).getXX_VCN_PaymentDollars_ID();
				}
				payDollars = new X_XX_VCN_PaymentDollars(Env.getCtx(),id,null);	
			}
			
			//Calcula los Saldos
			balanceForeign = payDollars.getXX_BalanceForeign().add(amountForeign);
			balanceLocal = payDollars.getXX_BalanceLocal().add(amountLocal);
			mTab.setValue("XX_BalanceForeign", balanceForeign);
			mTab.setValue("XX_BalanceLocal", balanceLocal);			
			mTab.setValue("XX_AverageExchange", balanceLocal.divide(balanceForeign, RoundingMode.DOWN));
			
		}
		return "";
	}
	
	//colocar este proceso en utilities
	public BigDecimal valueRateOfficial(Timestamp fecha){
		BigDecimal rate = new BigDecimal(4.3);
		//hacer select para buscar el valor de la tasa, con la fecha, en la tabla tasa de cambio
		
		return rate;
	}
	
	/**
	 * Se encarga de buscar los últimos dos registros de la tabla de Pago en Dolares
	 * @return
	 */
	public List<X_XX_VCN_PaymentDollars> maxRecord(){
		List<X_XX_VCN_PaymentDollars> list = new ArrayList <X_XX_VCN_PaymentDollars> ();
		X_XX_VCN_PaymentDollars paymentDollars = null;
		String sql = "select rownum, XX_VCN_PaymentDollars_ID " +
					 "from XX_VCN_PaymentDollars " +
					 "where rownum < 3 " +
					 "order by XX_VCN_PaymentDollars_ID desc ";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){		
				paymentDollars = new X_XX_VCN_PaymentDollars(Env.getCtx(),rs.getInt(2),null);
				list.add(paymentDollars);
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
		return list;
	}

}
