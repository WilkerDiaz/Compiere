package compiere.model.suppliesservices;

import java.math.BigDecimal;
import java.sql.ResultSet;
import org.compiere.util.Ctx;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.cds.MInvoiceLine;

public class MProductPercentDistrib extends X_XX_ProductPercentDistrib{
	
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProductPercentDistrib.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param XX_Contract_ID id
	 *	@param trxName transaction
	 */
    public MProductPercentDistrib(Ctx ctx, int XX_Contract_ID, Trx trxName) {
		super(ctx, XX_Contract_ID, trxName);
	}
    
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MProductPercentDistrib(Ctx ctx, ResultSet rs, Trx trxName)	{
		super(ctx, rs, trxName);
	}
    
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord) {

		if(getXX_PercentagePerCC().compareTo(BigDecimal.ZERO)!=0 || getXX_AmountPerCC().compareTo(BigDecimal.ZERO)!=0)
		{		
			//Porcentaje
			if(getXX_PercentagePerCC().compareTo(BigDecimal.ZERO)!=0){

				MInvoiceLine line = new MInvoiceLine( Env.getCtx(), get_ValueAsInt("C_InvoiceLine_ID"), null);
				setXX_AmountPerCC(getXX_PercentagePerCC().multiply(line.getPriceActual()).divide(BigDecimal.valueOf(100)));
			}
		}
		
		return true;
	}
}
