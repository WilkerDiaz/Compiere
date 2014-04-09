package compiere.model.cds.callouts;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;

import compiere.model.cds.X_Ref_XX_ServiceApplies;

public class VLO_DeliveryProductCallout extends CalloutEngine {

	public String UpdateProductDetails(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{	
		GridField fieldM_Product_ID = mTab.getField("M_Product_ID");
		Integer auxM_Product_ID = (Integer) fieldM_Product_ID.getValue();
		System.out.println("PRODUCT: "+auxM_Product_ID);
		String sql = "SELECT p.XX_VMR_Brand_ID, p.XX_VMR_Line_ID, p.XX_VMR_Section_ID  " +
				"FROM M_Product p " +
				"WHERE  p.M_Product_ID = "+auxM_Product_ID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{

			pstmt = DB.prepareStatement(sql,null);
			rs = pstmt.executeQuery();
		
			if (rs.next()){
				Integer brand = rs.getInt("XX_VMR_Brand_ID");
				Integer line = rs.getInt("XX_VMR_Line_ID");
				Integer section = rs.getInt("XX_VMR_Section_ID");
				mTab.setValue("XX_VMR_Brand_ID", brand);
				mTab.setValue("XX_VMR_Line_ID", line);
				mTab.setValue("XX_VMR_Section_ID", section);
			}
		}catch (Exception e){
			return "Error SQL " +e.getMessage();
		}finally{
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return "";
	}
	
	public String UpdateUrbanization(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{	
		GridField fieldM = mTab.getField("XX_VLO_Sector_ID");
		Integer aux_ID = (Integer) fieldM.getValue();
		System.out.println("Sector: "+aux_ID);
		String sql = "SELECT XX_ServiceApplies  " +
				"FROM XX_VLO_Sector " +
				"WHERE  XX_VLO_Sector_ID = "+aux_ID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{

			pstmt = DB.prepareStatement(sql,null);
			rs = pstmt.executeQuery();
		
			if (rs.next()){
				String serviceApplies = rs.getString(1);
				mTab.setValue("XX_ServiceAppliesSector", serviceApplies);
				mTab.setValue("XX_VLO_Urbanization_ID",0);
				mTab.setValue("XX_VLO_Street_ID",0);
			}else {
				mTab.setValue("XX_ServiceAppliesSector",X_Ref_XX_ServiceApplies.APLICA);
			}
		}catch (Exception e){
			return "Error SQL " +e.getMessage();
		}finally{
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		mTab.setValue("XX_ServiceAppliesUrban", X_Ref_XX_ServiceApplies.APLICA);
		return "";
	}
	
	public String UpdateStreet(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{	
		GridField fieldM = mTab.getField("XX_VLO_Urbanization_ID");
		Integer aux_ID = (Integer) fieldM.getValue();
		System.out.println("Urbanizazión: "+aux_ID);
		String sql = "SELECT XX_ServiceApplies  " +
				"FROM XX_VLO_Urbanization " +
				"WHERE  XX_VLO_Urbanization_ID = "+aux_ID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{

			pstmt = DB.prepareStatement(sql,null);
			rs = pstmt.executeQuery();
		
			if (rs.next()){
				String serviceApplies = rs.getString(1);
				mTab.setValue("XX_ServiceAppliesUrban", serviceApplies);
				mTab.setValue("XX_VLO_Street_ID",0);
			}else {
				mTab.setValue("XX_ServiceAppliesUrban",X_Ref_XX_ServiceApplies.APLICA);
			}
		}catch (Exception e){
			return "Error SQL " +e.getMessage();
		}finally{
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return "";
	}
}
