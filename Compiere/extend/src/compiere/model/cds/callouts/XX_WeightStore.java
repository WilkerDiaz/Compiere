package compiere.model.cds.callouts;

import java.math.BigDecimal;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;
import org.compiere.util.Env;

import compiere.model.cds.X_XX_VMR_WarehouseWeight;

public class XX_WeightStore extends CalloutEngine {

	public String WeightStore (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
			GridField campoPeso  =  mTab.getField("XX_WarehouseWeight");  
		    BigDecimal tiendaPeso = (BigDecimal)campoPeso.getValue();
		 
		    GridField campoID  =  mTab.getField("XX_VMR_WAREHOUSEWEIGHT_ID");  
		    Integer campoWeightID = (Integer)campoID.getValue();
		    
		    X_XX_VMR_WarehouseWeight warehoseWeight = new X_XX_VMR_WarehouseWeight(Env.getCtx(), campoWeightID, null);
		    
		    if(tiendaPeso.compareTo(new BigDecimal(0)) == 0)
		    	warehoseWeight.setXX_WeightCheck(false);
		    else
		    	warehoseWeight.setXX_WeightCheck(true);
		    
		    warehoseWeight.save();
		    	    
		    return "";

	}
	
}
