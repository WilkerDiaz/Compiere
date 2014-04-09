package compiere.model.bank.callouts;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;

public class XX_VCN_CallOutManualCheck extends CalloutEngine{
	
	
	public String manualCheck(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {

			if(mTab.get_ValueAsString("XX_VCN_ManualCheck").equals("Y"))		
			mTab.setValue("XX_IsAdvance", 'N');
			
			return "";
	}
	
	public String isAdvance(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {

			if(mTab.get_ValueAsString("XX_IsAdvance").equals("Y"))		
			mTab.setValue("XX_VCN_ManualCheck", 'N');
			
			return "";
	}
	


}
