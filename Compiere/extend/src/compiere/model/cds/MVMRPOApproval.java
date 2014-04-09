package compiere.model.cds;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MVMRPOApproval extends  X_XX_VMR_PO_Approval{


	public MVMRPOApproval(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		
	}
	
	   public MVMRPOApproval(Ctx ctx, int XX_VMR_PO_Approval_ID, Trx trx) {
			super(ctx, XX_VMR_PO_Approval_ID, trx);
		
		}

	public MVMRPOApproval (Ctx ctx, int orden, Trx trx, int ok)
    {
        super (ctx, 0, trx);
        set_ValueNoCheck("C_Order_ID", orden);

    }  
	
	protected boolean beforeSave(boolean newRecord)
	{	

		
		
		return true;
			

	}//beforeSave
}
