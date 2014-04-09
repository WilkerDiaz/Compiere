package compiere.model.promociones.process;

import org.compiere.apps.ADialog;
import org.compiere.apps.form.FormFrame;
import org.compiere.process.SvrProcess;
import compiere.model.promociones.X_XX_VMR_Promotion;

public class XX_ApprovePromoMarket extends SvrProcess{

	private String msg;
	private int m_WindowNo = 0;
	/**	FormFrame */
	private FormFrame m_frame;
	@Override
	protected void prepare() {

	}

	@Override
	protected String doIt() throws Exception {
		msg = "";
		X_XX_VMR_Promotion promotion = new X_XX_VMR_Promotion(getCtx(), getRecord_ID(),get_Trx());
		XX_ApproveMethods validator = new XX_ApproveMethods();
		if(promotion.get_ValueAsBoolean("XX_ApproveMar")){
			promotion.setXX_ApproveMar(false);
			
		}else{
			if(!validator.checkCollisions(getRecord_ID())
					||!validator.validateProducts(getRecord_ID())
					||!validator.validatePriority(promotion)
					||!validator.validateConditions(getRecord_ID())
					||!validator.validateSponsoredProducts(promotion)){
				msg = validator.getMsg();
				ADialog.info(1, m_frame, msg);
			}else{
				promotion.setXX_ApproveMar(true);
				promotion.setXX_Synchronized(false);
			}
		}
		promotion.save();
		
		return msg;
	}

}
