package compiere.model.cds.callouts;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Ctx;


public class VLO_CheckupDaysCallout extends CalloutEngine {

	
	public String onSelectOrder(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		if (isCalloutActive() || value==null)
			return "";
		 // limpio los de orden
		 /*mTab.setValue("XX_VMR_Collection_ID",null);
		 mTab.setValue("XX_VMR_Package_ID",null);
		 mTab.setValue("XX_ReceptionDate",null);
		 mTab.setValue("XX_DaysHoursMinutes","");
		 mTab.setValue("XX_ReceptionDateUntil",null);
		 mTab.setValue("XX_Orderstatus",null);*/
		 
		 // limpio los de pedido
		 mTab.setValue("XX_VMR_Order_ID",null);
		 mTab.setValue("XX_OrderRequestStatus",null);
		 mTab.setValue("XX_DateOrderRequest",null);
		 mTab.setValue("XX_CollectionOrderRequest_ID",null);
		 mTab.setValue("XX_PackageOrderRequest_ID",null);
		 mTab.setValue("XX_VMR_DistributionHeader_ID",null);
		 mTab.setValue("XX_DateOrderRequestUntil",null);
		 
		 setCalloutActive(false);
		 
		 return "";
	}
	
	public String clearOrder(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		if (isCalloutActive() || value==null)
			return "";
		// limpio los de la orden
		 //mTab.setValue("C_Order_ID",null);
		 //mTab.setValue("XX_DaysHoursMinutes","");
		 
		 // limpio los de pedido
		 mTab.setValue("XX_VMR_Order_ID",null);
		 mTab.setValue("XX_OrderRequestStatus",null);
		 mTab.setValue("XX_DateOrderRequest",null);
		 mTab.setValue("XX_DateOrderRequestUntil",null);
		 mTab.setValue("XX_CollectionOrderRequest_ID",null);
		 mTab.setValue("XX_PackageOrderRequest_ID",null);
		 mTab.setValue("XX_VMR_DistributionHeader_ID",null);
		 
		 setCalloutActive(false);
		 
		 return "";
	}
	
	// al escoger numero de pedido
	public String onSelectRequest(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		if (isCalloutActive() || value==null)
			return "";
		 // limpio los de orden
		 mTab.setValue("XX_VMR_Collection_ID",null);
		 mTab.setValue("XX_VMR_Package_ID",null);
		 mTab.setValue("XX_ReceptionDate",null);
		 mTab.setValue("XX_DaysHoursMinutes","");
		 mTab.setValue("C_Order_ID",null);
		 mTab.setValue("XX_ReceptionDateUntil",null);
		 mTab.setValue("XX_Orderstatus",null);
		 
		 // limpio los de pedido
		 /*mTab.setValue("XX_OrderRequestStatus",null);
		 mTab.setValue("XX_DateOrderRequest",null);
		 mTab.setValue("XX_DateOrderRequestUntil",null);
		 mTab.setValue("XX_CollectionOrderRequest_ID",null);
		 mTab.setValue("XX_PackageOrderRequest_ID",null);
		 mTab.setValue("XX_VMR_DistributionHeader_ID",null);*/
		 
		 setCalloutActive(false);
		 
		 return "";
	}
	
	public String onSelectDistribution(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		if (isCalloutActive() || value==null)
			return "";
		 // limpio los de orden
		 mTab.setValue("XX_VMR_Collection_ID",null);
		 mTab.setValue("XX_VMR_Package_ID",null);
		 mTab.setValue("XX_ReceptionDate",null);
		 mTab.setValue("XX_DaysHoursMinutes","");
		 mTab.setValue("C_Order_ID",null);
		 mTab.setValue("XX_ReceptionDateUntil",null);
		 mTab.setValue("XX_Orderstatus",null);
		 
		 // limpio los de pedido
		 /*mTab.setValue("XX_OrderRequestStatus",null);
		 mTab.setValue("XX_DateOrderRequest",null);
		 mTab.setValue("XX_DateOrderRequestUntil",null);
		 mTab.setValue("XX_CollectionOrderRequest_ID",null);
		 mTab.setValue("XX_PackageOrderRequest_ID",null);
		 mTab.setValue("XX_VMR_Order_ID",null);*/
		 
		 setCalloutActive(false);
		 
		 return "";
	}
	
	public String onSelectElseRequest(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		if (isCalloutActive() || value==null)
			return "";
		 // limpio los de orden
		 mTab.setValue("XX_VMR_Collection_ID",null);
		 mTab.setValue("XX_VMR_Package_ID",null);
		 mTab.setValue("XX_ReceptionDate",null);
		 mTab.setValue("XX_DaysHoursMinutes","");
		 mTab.setValue("C_Order_ID",null);
		 mTab.setValue("XX_ReceptionDateUntil",null);
		 mTab.setValue("XX_Orderstatus",null);
		 
		 // limpio los de pedido
		 //mTab.setValue("XX_VMR_DistributionHeader_ID",null);
		 //mTab.setValue("XX_VMR_Order_ID",null);
		 
		 setCalloutActive(false);
		 
		 return "";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
}
