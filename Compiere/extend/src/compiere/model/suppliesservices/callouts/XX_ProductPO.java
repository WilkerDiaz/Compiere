package compiere.model.suppliesservices.callouts;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MOrder;

public class XX_ProductPO extends CalloutEngine{

	/**
	 * Verify. Doesn't allow products repeated in a PO
	 * @param ctx context
	 * @param WindowNo window no
	 * @param mTab tab
	 * @param mField field
	 * @param value value
	 */
	public String Verify(Ctx ctx, int WindowNo, GridTab mTab,GridField mField, Object value){
		String error_msg = "No puede repetir productos en una orden de compra";
		// Product ID
		Integer Product_ID = (Integer)mTab.getValue("M_Product_ID");
		 // Order ID
		Integer Order_ID = (Integer)mTab.getValue("C_Order_ID");
		 // Order Line ID
		Integer OrderLine_ID = (Integer)mTab.getValue("C_OrderLine_ID");
		// Product ID
		Integer Products_ID = 0; 
		 // DocType ID
		String DocTypePurchase = "";
		
		MOrder Order = new MOrder(ctx, Order_ID, null);
		DocTypePurchase = Order.getXX_POType();
		
		if (isCalloutActive() || value==null)
			return "";
		
		// If Assets/Services PO
		if(DocTypePurchase.equals(Env.getCtx().getContext("#XX_L_POTYPE"))){
			String sql = " Select M_Product_ID " +
					" From C_OrderLine " +
					" Where C_Order_ID = "+Order_ID +
					" AND AD_Client_ID = " + 
					Env.getCtx().getAD_Client_ID();
			//System.out.println(sql);
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				while(rs.next()){
					Products_ID = rs.getInt("M_Product_ID");
					if(Product_ID.equals(Products_ID)){
						ADialog.info(1, new Container(),error_msg);
					}
				}//While
			}//try
			catch (SQLException e){
				e.printStackTrace();			
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}// DocType
		setCalloutActive(false);
		return "";
		
	}// fin Verify	
}// Fin XX_ProductPO
