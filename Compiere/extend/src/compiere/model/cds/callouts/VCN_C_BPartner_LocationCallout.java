package compiere.model.cds.callouts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.X_Ref_XX_Ref_VendorClass;

public class VCN_C_BPartner_LocationCallout extends CalloutEngine {
		
	private void ActualizarVendorClassCBPartner (String PaymentRule_ID, Integer C_BPartner_ID)
	{	
		//Coloca el estado de la O/C a Proforma cuando se Salva
		String sql = "UPDATE C_BPARTNER " +
					 "SET XX_VENDORCLASS = "+ PaymentRule_ID + " "+
					 "WHERE C_BPARTNER_ID = " + C_BPartner_ID;
		
		DB.executeUpdate( null, sql);
	}
	
	public String ActualizaVendorClass (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{	
		if (isCalloutActive() || value==null)
			return "";
		
		GridField campoC_BPartner_Location_ID = mTab.getField("C_BPartner_Location_ID");
		Integer AuxC_BPartner_Location_ID = (Integer) campoC_BPartner_Location_ID.getValue();
		String sql =   "select Upper(name) "+
					   "from c_country "+
					   "where C_COUNTRY_ID = (select C_COUNTRY_ID "+ 
						                     "from C_LOCATION "+ 
						                     "where C_LOCATION_ID =(select C_LOCATION_ID "+ 
						                                           "from C_BPartner_Location "+ 
						                                           "where C_BPartner_Location_ID = " + AuxC_BPartner_Location_ID +"  ) )";
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			String AuxName = new String ("VACIO");
			
			if(rs.next()){
				AuxName = rs.getString("Upper(name)");
			}
			rs.close();
			pstmt.close();
			if (! AuxName.equals("null")){
				if (AuxName.equals("VENEZUELA")){
					ActualizarVendorClassCBPartner(X_Ref_XX_Ref_VendorClass.NACIONAL.getValue(), (Integer)mTab.getValue("C_BPartner_ID"));
				}else{
					ActualizarVendorClassCBPartner(X_Ref_XX_Ref_VendorClass.IMPORTADO.getValue(), (Integer)mTab.getValue("C_BPartner_ID"));
				}
				return "";
			}
			else
				return Msg.getMsg(Env.getCtx(), "XX_AddLocation");
				//"Remember to add the location";
			}
		catch (SQLException e){
				return e.getMessage();
		}			
	}
}