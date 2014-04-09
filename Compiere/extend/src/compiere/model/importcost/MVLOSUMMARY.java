package compiere.model.importcost;

import java.math.BigDecimal;
import java.sql.ResultSet;
import org.compiere.model.X_C_Tax;
import org.compiere.util.Ctx;
import org.compiere.util.Env;
import org.compiere.util.Trx;

public class MVLOSUMMARY extends X_XX_VLO_SUMMARY{
	
	private static final long serialVersionUID = 1L;
	
	public MVLOSUMMARY (Ctx ctx, int id, Trx trx)
	{
		super (ctx, id, trx);		
	}	
	
	public MVLOSUMMARY (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{	
		X_C_Tax tax = new X_C_Tax( Env.getCtx(), getC_Tax_ID(), null);
		
		BigDecimal taxVar = tax.getRate();
		
		if(tax.getRate().compareTo(BigDecimal.ZERO)>0){
			taxVar = tax.getRate().divide(BigDecimal.valueOf(100));
			setXX_VLO_TaxCost(getXX_Cosant().multiply(taxVar));
		}else
			setXX_VLO_TaxCost(BigDecimal.ZERO);
		
		return true;
					
	}//beforeSave

}
