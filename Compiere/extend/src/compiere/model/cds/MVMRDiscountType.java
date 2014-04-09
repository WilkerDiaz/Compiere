package compiere.model.cds;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MVMRDiscountType extends X_XX_VMR_DiscountType{

	/**
	*  Realizado por Rosmaira Arvelo 
	*/
	private static final long serialVersionUID = 1L;
		
	public MVMRDiscountType(Ctx ctx, int XX_VMR_DiscountType_ID,
			Trx trx) {
		super(ctx, XX_VMR_DiscountType_ID, trx);
		// TODO Auto-generated constructor stub
	}
		
	/**
	 * 	Load constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MVMRDiscountType (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
}
