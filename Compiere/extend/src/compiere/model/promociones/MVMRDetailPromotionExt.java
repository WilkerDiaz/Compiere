package compiere.model.promociones;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.compiere.framework.PO;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

public class MVMRDetailPromotionExt extends X_XX_VMR_DetailPromotionExt {

	
	public MVMRDetailPromotionExt(Ctx ctx, int XX_VMR_DetailPromotionExt_ID,
			Trx trx) {
		super(ctx, XX_VMR_DetailPromotionExt_ID, trx);
		// TODO Auto-generated constructor stub
	}

	
	public MVMRDetailPromotionExt(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}

	public MVMRDetailPromotionExt (MPromotion promotion)
	{
		this (promotion.getCtx(), 0, promotion.get_Trx());
		if (promotion.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected boolean beforeDelete()
	{
		boolean delete = super.beforeDelete();
		return delete;
		
	}	//	beforeDelete	

	private ArrayList<MVMRDetailPromotionExt> getConsultDetailPromotion(int promotion_id, int conditionPromotion_ID)
	{
		String sql = "SELECT XX_VMR_detailPromotionExt_ID FROM XX_VMR_detailPromotionExt WHERE XX_VMR_Promotion_ID = " +promotion_id+ " AND XX_VMR_PromoConditionValue_ID = "+conditionPromotion_ID+ " AND isActive = 'Y' and AD_Client_ID = " +getCtx().getAD_Client_ID()+"";
		PreparedStatement psStatement = DB.prepareStatement(sql, null);
		ResultSet resultSet = null;
		ArrayList<MVMRDetailPromotionExt> ListlinesDetailPromotion = new ArrayList<MVMRDetailPromotionExt>(); 
		MVMRDetailPromotionExt linesDetailPromotion_ID = null;
		
		try {
			resultSet = psStatement.executeQuery();
			while (resultSet.next()) {
				linesDetailPromotion_ID = new MVMRDetailPromotionExt(getCtx(), resultSet.getInt(1), null);
				ListlinesDetailPromotion.add(linesDetailPromotion_ID);	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DB.closeStatement(psStatement);
			DB.closeResultSet(resultSet);
		}
		
		return ListlinesDetailPromotion;
		
	}
	
	public void createDetailPromotion (MPromotion promotionNew, int promotionOld_ID, int oldConditionPromotion_ID, int newConditionPromotion_ID)
	{
		ArrayList<MVMRDetailPromotionExt> detail = getConsultDetailPromotion(promotionOld_ID, oldConditionPromotion_ID);
		MVMRDetailPromotionExt lineDetailNew = null;
		for (MVMRDetailPromotionExt lineDetailOld : detail)
		{
			lineDetailNew = new MVMRDetailPromotionExt (promotionNew);
			PO.copyValues(lineDetailOld, lineDetailNew);
			lineDetailNew.setXX_VMR_Promotion_ID (promotionNew.get_ID());
			lineDetailNew.setXX_VMR_PromoConditionValue_ID(newConditionPromotion_ID);
			lineDetailNew.save(get_Trx());
		}
	}
	
}

