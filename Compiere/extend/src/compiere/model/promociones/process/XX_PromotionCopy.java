package compiere.model.promociones.process;

import java.util.ArrayList;
import org.compiere.process.SvrProcess;
import compiere.model.promociones.MPromotion;
import compiere.model.promociones.MVMRDetailPromotionExt;
import compiere.model.promociones.MVMRPromoConditionValue;

public class  XX_PromotionCopy extends SvrProcess {

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Entro a Programar");
		
		int oldConditionPromotion = 0, newConditionPromotion = 0;
		
		MPromotion promotionOld = new MPromotion(getCtx(), getRecord_ID(), get_Trx());
		MVMRDetailPromotionExt detailPromotion = new MVMRDetailPromotionExt(getCtx(), 0, get_Trx());
		MVMRPromoConditionValue conditionPromotion = new MVMRPromoConditionValue(getCtx(), 0, get_Trx());
		
		MPromotion promotionNew = promotionOld.copyPromotion();
		
		ArrayList<ArrayList<MVMRPromoConditionValue>> detailOldNewConditionPromotion = conditionPromotion.getCopyConditionPromotion(promotionNew, promotionOld.get_ID());
		
		ArrayList<MVMRPromoConditionValue> detailOldConditionPromotion = detailOldNewConditionPromotion.get(0);
		ArrayList<MVMRPromoConditionValue> detailNewConditionPromotion = detailOldNewConditionPromotion.get(1);

		for(int i = 0; i<detailOldConditionPromotion.size(); i++)
		{
			oldConditionPromotion = detailOldConditionPromotion.get(i).get_ID();
			newConditionPromotion = detailNewConditionPromotion.get(i).get_ID();
			detailPromotion.createDetailPromotion(promotionNew, promotionOld.get_ID(), oldConditionPromotion, newConditionPromotion);		
		}
		
		return "TODO BIEN Aqui hace el Commit";
	}
	
	
	
	

}
