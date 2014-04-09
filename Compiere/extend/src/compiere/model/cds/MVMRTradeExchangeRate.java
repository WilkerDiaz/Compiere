package compiere.model.cds;

import java.awt.Container;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

/**
 *	XX_VMR_TradeExchangeRate Model
 *	
 *  @author ghuchet 
 */

public class MVMRTradeExchangeRate extends X_XX_VMR_TradeExchangeRate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public  MVMRTradeExchangeRate(Ctx ctx, int XX_VMR_TradeExchangeRate_ID, Trx trx) {
		super(ctx, XX_VMR_TradeExchangeRate_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	public MVMRTradeExchangeRate(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}



	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{	
		boolean save = super.beforeSave(newRecord);

		if(save){
			if(isActive()){
				String msg = "¿Está seguro de querer actualizar la tasa de cambio comercial?. " +
						"\nEsta tasa será usada para el cálculo del factor comercial de periodo igual o posterior al actual.";
				Boolean ask = ADialog.ask(1, new Container(), msg);
				if(ask){
				  	setXX_InitDate(getCreated());
	
					String SQL = "Select XX_VMR_TradeExchangeRate_ID "+
								"\nFROM XX_VMR_TradeExchangeRate WHERE XX_InitDate = " +
								"\n(Select max(XX_InitDate) FROM XX_VMR_TradeExchangeRate) " +
								"\n and ISACTIVE='Y' AND XX_EndDate IS NULL AND AD_CLIENT_ID = "  +Env.getCtx().getAD_Client_ID();
					
					System.out.println(SQL);
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try 
					{	
						pstmt = DB.prepareStatement(SQL, null);
						rs = pstmt.executeQuery();
						while (rs.next()) {	
							//Se actualiza la fecha final de la ultima tasa de cambio comercial y se inactiva
							X_XX_VMR_TradeExchangeRate oldRate = new X_XX_VMR_TradeExchangeRate(Env.getCtx(),rs.getInt("XX_VMR_TradeExchangeRate_ID"), null);
							if(getXX_Rate().compareTo(oldRate.getXX_Rate())==0){
								msg = "La nueva tasa debe ser distinta a la actual : "+getXX_Rate().setScale(2, RoundingMode.HALF_EVEN)+".";
								ADialog.info(1, new Container(), msg);
								return false;
							}
							
							oldRate.setXX_EndDate(getUpdated());
							oldRate.setIsActive(false);
							oldRate.save();
						}

						msg = "Se actualizó la tasa de cambio comercial. " +
								"\nLa nueva tasa usada para periodo igual o posterior al actual es: "+getXX_Rate().setScale(2, RoundingMode.HALF_EVEN)+
								"\nRefresque la ventana.";
						ADialog.info(1, new Container(), msg);
	
					} catch (Exception e) {
						e.printStackTrace();
					} finally
					{
						DB.closeResultSet(rs);
						DB.closeStatement(pstmt);
					}
				}else {
					return false;
				}
			}
		}
		return save;

	}
}
