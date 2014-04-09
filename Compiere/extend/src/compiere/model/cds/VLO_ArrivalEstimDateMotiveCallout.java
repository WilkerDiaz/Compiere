package compiere.model.cds;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;

/**
 * Arrival Estimate Date Motive Callout
 * 
 * @author Patricia Ayuso M.
 * 
 */
public class VLO_ArrivalEstimDateMotiveCallout extends CalloutEngine {

	public String displayMotive(Ctx ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {

		try {
			GridField motive = mTab.getField("XX_ArrivalEstimDateMotive");
			
			if((value == null) || (oldValue == null)){
				motive.setDisplayed(false);
							
			}else{
				motive.setDisplayed(true);
				
			}
			
		} catch (Exception e) {
			return "";
		}
		
		return "";
	}
}
