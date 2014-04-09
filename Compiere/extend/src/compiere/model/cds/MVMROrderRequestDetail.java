package compiere.model.cds;


import org.compiere.util.Ctx;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVMROrderRequestDetail extends X_XX_VMR_OrderRequestDetail {
	
	private static final long serialVersionUID = 2107457197456215315L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMROrderRequestDetail.class);

	public MVMROrderRequestDetail(Ctx ctx, int XX_VMR_OrderRequestDetail_ID,
			Trx trx) {
		super(ctx, XX_VMR_OrderRequestDetail_ID, trx);
	}
	
	/**
	 * 	Called before Save for Pre-Save Operation
	 * 	@param newRecord new record
	 *	@return true if record can be saved
	 */
	protected boolean beforeSave(boolean newRecord) {		
		if (!newRecord) {
			//Solo cuando se ha modificado desde pantalla
			if (getXX_ProductQuantity() > getQtyReserved()) {
				log.saveError("Mayor que solicitado", Msg.translate(getCtx(), "QtyEnteredExceedsQtyOrdered"));
				return false;
			}
		}
		return true;
	}	

}
