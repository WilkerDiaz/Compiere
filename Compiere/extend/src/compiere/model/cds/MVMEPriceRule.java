package compiere.model.cds;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMEPriceRule extends X_XX_VME_PriceRule {

	public MVMEPriceRule(Ctx ctx, int XX_VME_PriceRule_ID, Trx trx) {
		super(ctx, XX_VME_PriceRule_ID, trx);
		// TODO Auto-generated constructor stub
	}

	public MVMEPriceRule(Ctx ctx, ResultSet rs, Trx trx) {
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
		if(getXX_HIGHRANK()<=0 )
		{
			log.saveError("Error", Msg.getMsg(getCtx(),"High Rank debe ser un valor valido "));
			return false;
		}
		if(getXX_LOWRANK()<0 )
		{
			log.saveError("Error", Msg.getMsg(getCtx(),"Low Rank debe ser un valor valido "));
			return false;
		}
		if(getXX_LOWRANK()>getXX_HIGHRANK())
		{
			log.saveError("Error", Msg.getMsg(getCtx(),"Low Rank no puede ser mayor que High Rank "));
			return false;
		}
		return true;
	}
}
