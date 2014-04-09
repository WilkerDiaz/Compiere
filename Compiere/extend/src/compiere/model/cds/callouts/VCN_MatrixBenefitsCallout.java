package compiere.model.cds.callouts;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class VCN_MatrixBenefitsCallout extends CalloutEngine {
	
	private BigDecimal cero = new BigDecimal(0);

	private String BuscarName (Integer C_BPartner_ID) {
		String sql1 =  "SELECT Upper(name) "+ 
					   "FROM C_BPartner "+
					   "WHERE C_BPartner_ID = " + C_BPartner_ID;
		
		try {
			//System.out.println(sql1);
			PreparedStatement pstmt = DB.prepareStatement(sql1, null);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			String AuxName = rs.getString("Upper(name)");
			rs.close();
			pstmt.close();
			return AuxName;
			
		} catch (Exception e) {
			return null;
		}
	}

	private Integer BuscarVendorType(Integer C_BPartner_ID) {
		String sql1 =  "SELECT XX_VendorType_ID "+ 
					   "FROM C_BPartner "+
					   "WHERE C_BPartner_ID = " + C_BPartner_ID;
		
		try {
			//System.out.println(sql1);
			PreparedStatement pstmt = DB.prepareStatement(sql1, null);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			int AuxVendorType = rs.getInt("XX_VendorType_ID");
			rs.close();
			pstmt.close();
			return new Integer(AuxVendorType);
			
		} catch (Exception e) {
			return null;
		}
	}

	public String ActualizaVendorType(Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{	
		GridField campoC_BPartner_ID = 	mTab.getField("C_BPartner_ID");
		Integer AuxC_BPartner_ID = (Integer) campoC_BPartner_ID.getValue(); 
		
		if (AuxC_BPartner_ID != null){
			String VendorName = BuscarName(AuxC_BPartner_ID);
			Integer VendorType = BuscarVendorType(AuxC_BPartner_ID);
		
			if (!VendorName.equals("ALL VENDOR")){
				//mTab.getField("XX_VendorType_ID").setDisplayed(true);
				mTab.setValue("XX_VendorType_ID", VendorType);
				return "";
			}else{
				//mTab.setValue("XX_VendorType_ID", "");
				//mTab.getField("XX_VendorType_ID").setDisplayed(false);
				mTab.setValue("XX_VendorType_ID", "");
				return "";
			}
		}
		return "";
	}

	public String LowerLimit (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
		 BigDecimal lowRank = (BigDecimal)mField.getValue();
		    
			GridField campoHighRank = 	mTab.getField("XX_HigherLimit");	
			BigDecimal  higtRank = (BigDecimal) campoHighRank.getValue();
						
			try{
				if(lowRank.compareTo(higtRank) >= 0 && higtRank.compareTo(cero) != 0 ){
					mTab.setValue("XX_LowerLimit", cero);
					
					return Msg.getMsg(Env.getCtx(), "XX_CheckRange");
					//"Verifique los Rangos";
				}
				else{
					return"";
				}
			}catch(Exception e){
				mTab.setValue("XX_LowerLimit", cero);
				
				return Msg.getMsg(Env.getCtx(), "XX_CheckRange");	
			}
	}	
	
	public String HighLimit (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue){
		BigDecimal higtRank = (BigDecimal)mField.getValue();
		
		GridField campoLowRank = 	mTab.getField("XX_LowerLimit");
		BigDecimal  lowRank = (BigDecimal) campoLowRank.getValue();
		
		try{
			if(lowRank.compareTo(higtRank)>=0 && higtRank.compareTo(cero) != 0){
				mTab.setValue("XX_HigherLimit", cero);
				
				return Msg.getMsg(Env.getCtx(), "XX_CheckRange");
			}
			else{
				return"";
			}
		}catch(Exception e){
			mTab.setValue("XX_HigherLimit", cero);
			
			return Msg.getMsg(Env.getCtx(), "XX_CheckRange");	
		}
	}	

}
