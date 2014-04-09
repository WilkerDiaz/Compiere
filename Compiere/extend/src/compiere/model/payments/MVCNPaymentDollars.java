package compiere.model.payments;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

/**
 * Modelo de Pago en Dolares
 * @author Jessica Mendoza
 *
 */
public class MVCNPaymentDollars extends X_XX_VCN_PaymentDollars{

	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVCNPaymentDollars.class);

	public MVCNPaymentDollars(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}
	
	public MVCNPaymentDollars(Ctx ctx, int XX_VCN_PaymentDollars_ID, Trx trx) {
		super(ctx, XX_VCN_PaymentDollars_ID, trx);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord){
		boolean flag = true;
		
		if (getXX_RateType().equals("O")){
			if (getXX_AmountForeign().compareTo(new BigDecimal(0)) != 0){
				setXX_AmountLocal(getXX_AmountForeign().multiply(getXX_Rate()));
				X_XX_VCN_PaymentDollars payDollars = new X_XX_VCN_PaymentDollars(Env.getCtx(), maxRecord(getXX_RateType()), get_Trx());
				setXX_BalanceForeign(getXX_AmountForeign().add(payDollars.getXX_BalanceForeign()));
				setXX_BalanceLocal((getXX_AmountForeign().multiply(getXX_Rate())).add(payDollars.getXX_BalanceLocal()));
				setXX_AverageExchange(getXX_Rate());
			}else{
				log.saveError("Error", Msg.getMsg(Env.getCtx(), "El monto $ debe ser distinto de cero (0)"));
				flag = false;
			}
		}else{
			if ((getXX_AmountLocal().compareTo(new BigDecimal(0)) == 0) || 
					(getXX_Rate().compareTo(new BigDecimal(0)) == 0)){
				log.saveError("Error", Msg.getMsg(Env.getCtx(), "XX_CompleteField"));
				flag = false;
			}
		}		
		return flag;
	}
	
	/**
	 * 
	 * Busca el último registro dependiendo de la condición del parámetro
	 * @param typeRate tipo de tasa (sitme-oficial-consolidado
	 * @return
	 */
	public int maxRecord(String typeRate){
		int idPayment = 0;
		String sql = "select max(XX_VCN_PaymentDollars_ID) " +
					 "from XX_VCN_PaymentDollars " +
					 "where XX_RateType = '" + typeRate + "' " +
					 " AND XX_RATETYPESITME <> 'IU'";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){		
				idPayment = rs.getInt(1);
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
		return idPayment;
	}
}
