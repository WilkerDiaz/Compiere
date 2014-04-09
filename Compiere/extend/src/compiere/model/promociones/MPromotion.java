package compiere.model.promociones;

import org.compiere.framework.PO;
import org.compiere.util.Ctx;
import org.compiere.util.Trx;

import compiere.model.promociones.X_XX_VMR_Promotion;

public class MPromotion extends X_XX_VMR_Promotion {
	
	private int XX_VMR_Promotion_ID;

	
	public int getXX_VMR_Promotion_ID() {
		return XX_VMR_Promotion_ID;
	}

	public void setXX_VMR_Promotion_ID(int XX_VMR_Promotion_ID) {
		this.XX_VMR_Promotion_ID = XX_VMR_Promotion_ID;
	}
		
	public MPromotion(Ctx ctx, int XX_VMR_Promotion_ID, Trx trx) {
		super(ctx, XX_VMR_Promotion_ID, trx);
		// TODO Auto-generated constructor stub
	}
	
	public MPromotion copyPromotion()
	{
		MPromotion promotionNew =  new MPromotion(getCtx(), 0, get_Trx());
		//promotionNew.save();
		PO.copyValues(this, promotionNew);
		promotionNew.save(get_Trx());
		
		return promotionNew;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
