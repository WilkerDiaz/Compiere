package org.compiere.common.constants;

public class WarehouseManagementConstants {
	public static final int ORDER_SELECTION_INFO_WINDOW = 122;
	public static final int RELEASE_WAVE_INFO_WINDOW = 120;
	public static final int RELEASE_WAVE_SUMMARY_INFO_WINDOW = 121;
	public static final int TASK_MANAGEMENT_INFO_WINDOW = 119;
	public static final int SHIPMENT_MANAGEMENT_INFO_WINDOW = 123;

	public static final String ORDER = "C_Order_ID";
	public static final String ZONE = "M_Zone_ID";
	public static final String SOURCE_ZONE = "M_SourceZone_ID";
	public static final String DESTINATION_ZONE = "M_DestZone_ID";
	public static final String LOCATOR = "M_Locator_ID";
	public static final String DELIVERYRULEOVERRIDE = "DeliveryRuleOverride";
	public static final String DESTINATION_LOCATOR = "StagingLocator";
	public static final String WAVE_SORT = "C_WaveSortCriteria_ID";
	public static final String PROCESS_ROWS = "ProcessRows";
	public static final String GENERATE_PICKLIST= "IsGeneratePickList";
	public static final String ADD_SERVICE_LINES= "AddServiceLines";
	public static final String DOCTYPETASK = "C_DocTypeTask_ID";
	public static final String WAVEDOCBASETYPE = "WaveDocBaseType";
	
	
	public static final String PICK_METHOD = "PickMethod";
	public static final String[] PICK_METHODS = {"ClusterPicking" , "OrderPicking" };
	public static final String CLUSTER_SIZE = "ClusterSize";
	
	public static final int I_CLUSTER_PICKMETHOD = 0;
	public static final int I_ORDER_PICKMETHOD = 1;
	public static final int I_NO_PICKMETHOD = -1;
	
	public static final String ORDERTYPEGROUP = "C_DocTypeGroup_ID";
	public static final String WAVE_SORT_CRITERIA = "C_WaveSortCriteria_ID";
	public static final String PRIORITY_RULE = "PriorityRule";
	public static final String TASK_TYPE= "C_DocTypeTask_ID";
	public static final String TASK_LIST= "M_TaskList_ID";
	public static final String DOCUMENT_TYPE= "C_DocType_ID";
	public static final String SUGGESTED_FROM_LOCATOR = "M_Locator_ID";
	public static final String SUGGESTED_TO_LOCATOR = "M_LocatorTo_ID";
	public static final String ACTUAL_FROM_LOCATOR = "M_ActualLocator_ID";
	public static final String ACTUAL_TO_LOCATOR = "M_ActualLocatorTo_ID";
	public static final String ACTUAL_QUANTITY = "QtyEntered";
	public static final String OPERATOR = "Operator";
	public static final String WORKORDERFULFILLMENTRULE = "WOFulfillRule";
	
}
