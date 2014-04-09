package compiere.model.promociones;

import java.sql.ResultSet;

import org.compiere.framework.PO;
import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MVSIDetailPromotionExtPOS  extends X_XX_VSI_DetailPromExtPOS {
	
	public MVSIDetailPromotionExtPOS(Ctx ctx, ResultSet rs, Trx trx){
		super (ctx, rs, trx);
	}
	public int getId(){
		return get_ValueAsInt("XX_VMR_Promotion_ID");
	}
	public String getCategory(){
		return get_ValueAsString("Category");
	}
	public String getDeparment(){
		return get_ValueAsString("Department");
	}
	public String getLine(){
		return get_ValueAsString("Line");
	}
	public String getSection(){
		return get_ValueAsString("Section");
	}
	public String getProduct(){
		return get_ValueAsString("Product");
	}
	public int getDiscRate(){
		return get_ValueAsInt("XX_DiscountRate");
	}
	public int getPriceConsecutive(){
		return get_ValueAsInt("XX_PriceConsecutive");
	}
	public String getBrand(){
		return get_ValueAsString("XX_VMR_BRAND_ID");
	}
	public String getRefPro(){
		return get_ValueAsString("RefPro");
	}	
	public double getMinimuPurchase(){
		return get_ValueAsBigDecimal("XX_MinimuPurchase").doubleValue();
	}	
	public int getQuantityPurchase(){
		return get_ValueAsInt("QuantityPurchase");
	}	
	public int getAmaountGifted(){
		return get_ValueAsInt("XX_AmaountGifted");
	}	
	public int getGroupDiscount(){
		return get_ValueAsInt("XX_GroupDiscount");
	}	
	public String getGiftAccumulate(){
		return get_ValueAsString("XX_GiftAccumulate");
	}	
	public String getGift(){
		return get_ValueAsString("XX_Gift");
	}
	

	@Override
	public int get_Table_ID() {
		// TODO Auto-generated method stub
		return 0;
	}


}
