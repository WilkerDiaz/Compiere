package compiere.model.cds.callouts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.tag.X_XX_VCN_TradeAgrCategory;

public class VCN_TradeAgreementsActivateCallout extends CalloutEngine{
	
	public String deactivateOrActivate (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue) 
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);
		int tradeAgID=0;
		if(mTab.get_ValueAsString("IsActive").equals("N")){
			tradeAgID = (Integer) mTab.getValue("XX_VCN_TradeAgreements_ID");
			String SQL= "select XX_VCN_TRADEAGRCATEGORY_ID " +
					"from XX_VCN_TRADEAGRCATEGORY " +
					"where XX_VCN_TradeAgreements_ID="+tradeAgID + 
					" and AD_CLIENT_ID="+Env.getCtx().getAD_Client_ID();
			PreparedStatement prst = DB.prepareStatement(SQL,null);
			try {
				ResultSet rs = prst.executeQuery();
				while(rs.next()){
					X_XX_VCN_TradeAgrCategory tradeCat = new X_XX_VCN_TradeAgrCategory(ctx, rs.getInt(1), null);
					tradeCat.setIsActive(false);
					tradeCat.save();
				}
			} catch (SQLException e){
				 System.out.print(e.getMessage());
		}
		}
		else{
			tradeAgID = (Integer) mTab.getValue("XX_VCN_TradeAgreements_ID");
			String SQL= "select XX_VCN_TRADEAGRCATEGORY_ID " +
					"from XX_VCN_TRADEAGRCATEGORY " +
					"where XX_VCN_TradeAgreements_ID="+tradeAgID + 
					" and AD_CLIENT_ID="+Env.getCtx().getAD_Client_ID();
			PreparedStatement prst = DB.prepareStatement(SQL,null);
			try {
				ResultSet rs = prst.executeQuery();
				while(rs.next()){
					X_XX_VCN_TradeAgrCategory tradeCat = new X_XX_VCN_TradeAgrCategory(ctx, rs.getInt(1), null);
					tradeCat.setIsActive(true);
					tradeCat.save();
				}
			} catch (SQLException e){
				 System.out.print(e.getMessage());
			}
		}
		setCalloutActive(false);	
		return "";
	}
	

}
