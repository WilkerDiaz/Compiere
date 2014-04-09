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

public class VCN_C_BPartnerCallout extends CalloutEngine {
    
	public String ActualizaVendorClass (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{	
		if (isCalloutActive() || value==null)
			return "";
		
		GridField campoC_BPartner_ID = 	mTab.getField("C_BPartner_ID");
		Integer AuxC_BPartner_ID = (Integer) campoC_BPartner_ID.getValue(); 
		
		/** Purchase of Supplies and Services
		 * Maria Vintimilla Funcion 1 **/
		Boolean Vendor = (Boolean)mTab.getValue("IsVendor");
		if (!Vendor){
			mTab.setValue("XX_CBPartner_ProductType",null);
		}
		//Fin Maria
		
		String sql =   "select Upper(name) "+
					   "from c_country "+
					   "where C_COUNTRY_ID = (select C_COUNTRY_ID "+ 
						                     "from C_LOCATION "+ 
						                     "where C_LOCATION_ID =(select C_LOCATION_ID "+ 
						                                           "from C_BPartner_Location "+ 
						                                           "where C_BPartner_Location_ID = (select C_BPartner_Location_ID "+ 
						                                                                           "from C_BPartner_Location "+ 
						                                                                           "where C_BPartner_ID = " + AuxC_BPartner_ID +" ) ) )";
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
				
			String AuxName = new String ("VACIO");
				
			if(rs.next()){
				AuxName = rs.getString("Upper(name)");
			}
			rs.close();
			pstmt.close();
				
			if (!AuxName.equals("VACIO")){
				if (AuxName.equals("VENEZUELA")){
					mTab.setValue("XX_VendorClass", X_Ref_XX_Ref_VendorClass.NACIONAL.getValue());
				}else{
					mTab.setValue("XX_VendorClass", X_Ref_XX_Ref_VendorClass.IMPORTADO.getValue());
				}
				return "";
			}
			else
				return Msg.getMsg(Env.getCtx(), "XX_AddLocation");
					//"Remember to add the location";
		}
		catch (SQLException e){
			// TODO: handle exception
			System.out.println("ERROR EN C_BPARTNER");
			return e.getMessage();
		}	
	}
	
	
	public String typePersonISLR(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{	
		if (isCalloutActive() || value==null)
			return "";
		
		if (value.equals(false))
			return "";
		
		GridField vendorClass = mTab.getField("XX_VendorClass");
		GridField typePersonField = mTab.getField("XX_TypePerson");
		
		if(isCalloutActive() || vendorClass.getValue()==null)
			return "";
		
		if(isCalloutActive() || typePersonField.getValue()==null)
			return "";
		
		String typePerson = typePersonField.getValue().toString();
		
		if(typePerson.equalsIgnoreCase("G")){
			
			mTab.setValue("XX_TypePersonISLR", "PJD");
		}
		else if(typePerson.equalsIgnoreCase("P")){
				
			mTab.setValue("XX_TypePersonISLR", "PNNR");
		}
		else if(typePerson.equalsIgnoreCase("V") || typePerson.equalsIgnoreCase("E")){
				
			mTab.setValue("XX_TypePersonISLR", "PNR");
		}
		else if(typePerson.equalsIgnoreCase("PJD")){
				
			mTab.setValue("XX_TypePersonISLR", "PJD");
		}
		else if(typePerson.equalsIgnoreCase("J")){
				
			if(vendorClass.getValue().equals("10000005"))
				mTab.setValue("XX_TypePersonISLR", "PJD");
			else
				mTab.setValue("XX_TypePersonISLR", "PJND");
		}
		
		return "";
	}
	
	public String typePersonISLRCountry(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{	
		if (isCalloutActive() || value==null)
			return "";
		
		GridField typePersonField = mTab.getField("XX_TypePerson");
		
		if(isCalloutActive() || typePersonField.getValue()==null)
			return "";
		
		String typePerson = typePersonField.getValue().toString();
		
		String vendorClass = value.toString();
		
		if(typePerson.equalsIgnoreCase("G")){
			
			mTab.setValue("XX_TypePersonISLR", "PJD");
		}
		else if(typePerson.equalsIgnoreCase("P")){
				
			mTab.setValue("XX_TypePersonISLR", "PNNR");
		}
		else if(typePerson.equalsIgnoreCase("V") || typePerson.equalsIgnoreCase("E")){
				
			mTab.setValue("XX_TypePersonISLR", "PNR");
		}
		else if(typePerson.equalsIgnoreCase("PJD")){
				
			mTab.setValue("XX_TypePersonISLR", "PJD");
		}
		else if(typePerson.equalsIgnoreCase("J")){
				
			if(vendorClass.equals("10000005"))
				mTab.setValue("XX_TypePersonISLR", "PJD");
			else
				mTab.setValue("XX_TypePersonISLR", "PJND");
		}
		
		return "";
	}
}
