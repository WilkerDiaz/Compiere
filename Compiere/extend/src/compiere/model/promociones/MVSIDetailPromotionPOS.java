package compiere.model.promociones;

import java.sql.ResultSet;

import org.compiere.framework.PO;
import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MVSIDetailPromotionPOS extends X_XX_VSI_DetailPromPOS {

	public int id;
	public int detail;
	public String dep;
	public String lin;
	public String prod;
	public int porc;
	public double pric;
	
	public MVSIDetailPromotionPOS(Ctx ctx, ResultSet rs, Trx trx){
		super (ctx, rs, trx);
	}
	
	public int getId(){
		return get_ValueAsInt("XX_VMR_Promotion_ID");
	}
	
	public int getDeparment(){
		return get_ValueAsInt("Department");
	}
	
	public int getCategory(){
		return get_ValueAsInt("Category");
	}
	public int getLine(){
		return get_ValueAsInt("Line");
	}
	public int getProduct(){
		return get_ValueAsInt("Product");
	}
	public int getDiscRate(){
		return get_ValueAsInt("XX_DiscountRate");
	}
	public int getDiscAmount(){
		return get_ValueAsInt("XX_DiscountAmount");
	}

	@Override
	public int get_Table_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
