package compiere.model.promociones.callout;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;

public class Validation extends CalloutEngine{
		
	public String cantidadDeProductos (Ctx ctx, int WindowNo,GridTab mTab
			, GridField mField, Object value, Object oldValue){
			if (Integer.parseInt(mTab.get_ValueAsString("XX_QuantityPurchase"))<
				Integer.parseInt(mTab.get_ValueAsString("XX_AmountGifted"))){
				mTab.setValue("XX_AmountGifted", 0);
				return Msg.translate(ctx, "Quantity discounts wrong");
			}
			return "";
		}
	public String unicidadEnCombos (Ctx ctx, int WindowNo,GridTab mTab
			, GridField mField, Object value, Object oldValue){
		String codigo=mTab.get_ValueAsString("XX_VMR_Promotion_ID");
		String detalle=mTab.get_ValueAsString("XX_VMR_DetailPromotionExt_ID");
		if (detalle.equals("")) detalle="0";
		String sql = "SELECT XX_GroupDiscount, XX_QuantityPurchase, XX_AmountGifted, XX_WarehouseBecoNumber, XX_DiscountRate " +
				"from XX_VMR_DetailPromotionExt a, XX_VMR_Promotion b where a.XX_VMR_Promotion_ID=b.XX_VMR_Promotion_ID and a.XX_VMR_Promotion_ID="+codigo+" " +
				"and XX_VMR_DetailPromotionExt_ID<>"+detalle+" and b.XX_TypePromotion=1000400 order by  XX_VMR_DetailPromotionExt_ID desc";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			//
			if (rs.next()){
				
				String grupo = rs.getString("XX_GroupDiscount");
				String cant2 = rs.getString("XX_QuantityPurchase");
				String cant1 = rs.getString("XX_AmountGifted");
				String tienda = rs.getString("XX_WarehouseBecoNumber");
				String porcdescuento = rs.getString("XX_DiscountRate");
				mTab.setValue("XX_GroupDiscount", Integer.parseInt(grupo));

				if (mTab.get_ValueAsString("XX_WarehouseBecoNumber").equals(tienda) 
						&& mTab.get_ValueAsString("XX_GroupDiscount").equals(grupo)){
					if(mTab.get_ValueAsString("XX_QuantityPurchase").equals(cant2)
							&& mTab.get_ValueAsString("XX_AmountGifted").equals(cant1)
							&& mTab.get_ValueAsString("XX_DiscountRate").equals(porcdescuento))
						return "";
					else {
						mTab.setValue("XX_QuantityPurchase", Integer.parseInt(cant2));
						mTab.setValue("XX_DiscountRate", Double.parseDouble(porcdescuento));
						mTab.setValue("XX_AmountGifted", Integer.parseInt(cant1));
						return "";
					} 
				}
			}
		}catch (SQLException e){
			e.printStackTrace();
			return e.getLocalizedMessage();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return "";
	}
	
	public String validarFecha (Ctx ctx, int WindowNo,GridTab mTab
			, GridField mField, Object value, Object oldValue){
		 if(new Date().after(new Date(ctx.getContextAsTime(WindowNo, "DateFrom")))){
			 mTab.setValue("DateFrom", null);
			 return Msg.translate(ctx,"The date after today's date");
		 } else if (ctx.getContextAsTime(WindowNo,"DateFinish") <= ctx.getContextAsTime(WindowNo, "DateFrom") &&
				 !ctx.get_ValueAsString("DateFinish").isEmpty()){
			 mTab.setValue("FechaInicio",oldValue);
			 return Msg.translate(ctx,"The earlier start date end date!");
		 }
		 return "";
	}
	public String validarFecha2 (Ctx ctx, int WindowNo,GridTab mTab
			, GridField mField, Object value, Object oldValue){
		if(ctx.getContextAsTime(WindowNo,"DateFinish") < ctx.getContextAsTime(WindowNo, "DateFrom")){
			if (mTab.get_ValueAsString("DateFinish").isEmpty())
				return "";
			mTab.setValue("DateFinish",null);
			return Msg.translate(ctx, "The final date must be after start date");
		}
		return "";
	}
	public String validarHoras (Ctx ctx, int WindowNo,GridTab mTab
			, GridField mField, Object value, Object oldValue){
		if(ctx.getContextAsTime(WindowNo,"TimeSlotEnd") < ctx.getContextAsTime(WindowNo, "TimeSlotStart")){
			if (mTab.get_ValueAsString("TimeSlotEnd").isEmpty())
				return "";
			mTab.setValue("FechaFin",null);
			return Msg.translate(ctx, "Final time must be after start time");
		}
		return "";
	}
	
	public String infoDesactivar(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Boolean value, Object oldValue)
	{
		if (!value)
			ADialog.info(1, new Container(), Msg.getMsg(ctx, "XX_IsActivePromotion"));
		
		return "";
	}
}
