package compiere.model.cds;

import java.math.BigDecimal;
import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMRPriceConsecutive extends X_XX_VMR_PriceConsecutive {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MVMRPriceConsecutive(Ctx ctx, int XX_VME_PriceConsecutive_ID, Trx trxName) {
		super(ctx, XX_VME_PriceConsecutive_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MVMRPriceConsecutive(Ctx ctx, ResultSet rs, Trx trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public boolean beforeSave(boolean newRecord)
	{
		if(getXX_PriceConsecutive() != 0 && !getXX_SalePrice().equals(0))	{
			int price = getXX_PriceConsecutive();
			BigDecimal sale = getXX_SalePrice();
			String SalePlusPrice = sale.toString();
			SalePlusPrice += price;
			setXX_SalePlusPriceConsecutive(SalePlusPrice);
		}
		return true;
	}
	

}// Fin MVMRPriceConsecutive
