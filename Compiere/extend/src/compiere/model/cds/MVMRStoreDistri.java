package compiere.model.cds;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MVMRStoreDistri extends X_XX_VMR_StoreDistri{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MVMRStoreDistri(Ctx ctx, int XX_VMR_StoreDistri_ID, Trx trx) {
		super(ctx, XX_VMR_StoreDistri_ID, trx);
		
	}
	
	public MVMRStoreDistri(Ctx ctx, int XX_VMR_StoreDistri_ID, Trx trx, int XX_VMR_PURCHASEPLAN_ID)
	{
	    super(ctx, 0, trx);
		set_ValueNoCheck("XX_VMR_PURCHASEPLAN_ID", XX_VMR_PURCHASEPLAN_ID);
	}

	
}
