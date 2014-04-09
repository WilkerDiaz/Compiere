package compiere.model.cds.callouts;


import java.math.BigDecimal;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

import org.compiere.util.Ctx;
import compiere.model.cds.X_XX_VLO_Fleet;

/**
 * 
 * @author Jorge E. Pires G.
 */
public class VLO_TravelCallout extends CalloutEngine {

	/** Logger					*/
	//private CLogger		log = CLogger.getCLogger(getClass());
	
	public String fleet(Ctx ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue){
		
		GridField campoFleet = 	mTab.getField("XX_VLO_Fleet_ID");
		if (campoFleet.getValue() != null){
			X_XX_VLO_Fleet fleet = new X_XX_VLO_Fleet(ctx, (Integer)campoFleet.getValue(), null);
			BigDecimal auxEquivalentPackage = fleet.getXX_AdjustmentsAmount().multiply(new BigDecimal(fleet.getXX_PackageQuantity()));
			
			mTab.setValue("XX_EquivalentPackageQuantity", auxEquivalentPackage.setScale(0, BigDecimal.ROUND_HALF_UP));
		}		
		
		return "";
	}	
}
