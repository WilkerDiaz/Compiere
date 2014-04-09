package compiere.model.cds;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MVMRPrld01 extends X_XX_VMR_Prld01{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MVMRPrld01(Ctx ctx, int XX_VMR_Prld01_ID, Trx trx) {
		super(ctx, XX_VMR_Prld01_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	public MVMRPrld01(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}

}
