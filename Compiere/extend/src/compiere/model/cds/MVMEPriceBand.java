package compiere.model.cds;

import java.math.BigDecimal;
import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMEPriceBand extends X_XX_VME_PriceBand {

	public MVMEPriceBand(Ctx ctx, int XX_VME_PriceBand_ID, Trx trx) {
		super(ctx, XX_VME_PriceBand_ID, trx);
		// TODO Auto-generated constructor stub
	}

	public MVMEPriceBand(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);

	public boolean beforeSave(boolean newRecord)
	{
		if(getXX_HIGHRANK().compareTo(new BigDecimal (0.0))<=0 )
		{
			log.saveError("Error", Msg.getMsg(getCtx(),"High Rank no puede ser 0.0 "));
			return false;
		}
		if( getXX_LOWRANK().compareTo(new BigDecimal (0.0))<=0 )
		{
			log.saveError("Error", Msg.getMsg(getCtx(),"Low Rank no puede ser 0.0 "));
			return false;
		}
		return true;
	}
}
