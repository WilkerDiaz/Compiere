package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MVLODeliveryProduct extends X_XX_VLO_DeliveryProduct{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVLODeliveryProduct.class);

	public MVLODeliveryProduct(Ctx ctx, int XX_VLO_DeliveryProduct_ID,
			Trx trx) {
		super(ctx, XX_VLO_DeliveryProduct_ID, trx);
		// TODO Auto-generated constructor stub
	}

	public MVLODeliveryProduct(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		// TODO Auto-generated constructor stub
	}
	public boolean beforeSave(boolean newRecord){
		
		String sql = "SELECT Count(*) " +
		"FROM XX_VLO_DeliveryProduct p " +
		"WHERE p.XX_VLO_ClientDelivery_ID = "+ getXX_VLO_ClientDelivery_ID()+
		" and p.M_Product_ID = "+ getM_Product_ID();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{

			pstmt = DB.prepareStatement(sql, get_Trx());
			rs = pstmt.executeQuery();
			
			if (rs.next()){
				int repeated = rs.getInt(1);
				if (repeated > 0){
					log.saveError("No es posible agregar el producto ", Msg.getMsg(getCtx(), "XX_VLO_RepeatedProduct", 
							new String[] {""}));
					return false; 
				}
			}
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}finally{
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		if(getQuantity() < 1){
			log.saveError("No es posible agregar el producto ", Msg.getMsg(getCtx(), "XX_VLO_ProductQuantity", 
					new String[] {""}));
			return false; 
		}
		return true;
	}
	
}
