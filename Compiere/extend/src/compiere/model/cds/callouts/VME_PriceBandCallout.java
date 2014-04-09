package compiere.model.cds.callouts;

import java.math.BigDecimal;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;

public class VME_PriceBandCallout extends CalloutEngine {
	
	BigDecimal cero = new BigDecimal(0);

	public String lowRank (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue) 
	{					
		 BigDecimal lowRank = (BigDecimal)mField.getValue();
		    
			GridField campoHighRank = 	mTab.getField("XX_HIGHRANK");	
			BigDecimal  higtRank = (BigDecimal) campoHighRank.getValue();
						
			try 
			{
				if(lowRank.compareTo(higtRank) >= 0 && higtRank.compareTo(cero) != 0 )
				{
					mTab.setValue("XX_LOWRANK", cero);
					//mTab.setValue("XX_HIGHRANK", cero);
					
					return "Verifique los Rangos";
				}
				else
				{
					return"";
				}
			}
			catch(Exception e)
			{
				mTab.setValue("XX_LOWRANK", cero);
				//mTab.setValue("XX_HIGHRANK", cero);
				
				return "Verifique los Rangos";	
			}
	}	
public String highRank (Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
{
	BigDecimal higtRank = (BigDecimal)mField.getValue();
	
	GridField campoLowRank = 	mTab.getField("XX_LOWRANK");
	BigDecimal  lowRank = (BigDecimal) campoLowRank.getValue();
	
	try
	{
		if(lowRank.compareTo(higtRank)>=0 && higtRank.compareTo(cero) != 0)
		{
			mTab.setValue("XX_HIGHRANK", cero);
			//mTab.setValue("XX_LOWRANK", cero);
			
			return "Verifique los Rangos";
		}
		else
		{
			return"";
		}
	}
	catch(Exception e)
	{
		mTab.setValue("XX_HIGHRANK", cero);
		//mTab.setValue("XX_LOWRANK", cero);
		
		return "Verifique los Rangos";	
	}
}	
}
