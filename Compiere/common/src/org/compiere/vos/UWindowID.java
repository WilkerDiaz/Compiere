/**
 * 
 */
package org.compiere.vos;

import java.io.*;

/**
 * @author gwu
 *
 */
public class UWindowID implements Serializable
{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	// this section should match AD_Form.AD_Form_ID
	public static final int FORM_GENERATE_INVOICE = 100;
	public static final int FORM_IMPORT_FILE_LOADER = 101;
	public static final int FORM_SETUP = 102;
	public static final int FORM_MATERIAL_TRX = 103;
	public static final int FORM_PAYMENT_ALLOCATION = 104;
	public static final int FORM_GENERATE_CHARGES = 105;
	public static final int FORM_PAYMENT_PRINT = 106;
	public static final int FORM_PAYMENT_SELECTION = 107;
	public static final int FORM_INVOICE_MATCHING = 108;
	public static final int FORM_GENERATE_SHIPMENT = 110;
	public static final int FORM_BOM_DROP = 114;
	public static final int FORM_TREE_MAINTAINENCE = 115;
	public static final int FORM_WORKFLOW_ACTIVITIES = 117;
	public static final int FORM_TASK_MANAGEMENT = 121;
	public static final int FORM_RELEASE_WAVE = 122;
	public static final int FORM_RELEASE_WAVE_SUMMARIZED = 123;
	public static final int FORM_ORDER_SELECTION = 124;
	public static final int FORM_WORKFLOW_EDITOR = 116;
	public static final int FORM_APPLICATION_EDITOR = 129;
	public static final int FORM_APPLICATION_PRINT_FORMAT_EDITOR = 130;
	
	// this section should map to AD_InfoWindow.AD_InfoWindow_ID
	public static final int INFO_BPARTNER = 100;
	public static final int INFO_PRODUCT = 101;
	public static final int INFO_PATTRIBUTE_INSTANCE = 116;
	public static final int INFO_LOCATOR = 118;
	public static final int INFO_GENERIC = -1;

	// this section should map to AD_Window.AD_Window_ID
	public static final int WINDOW_MY_NOTICE = 193;//ad_window_id for notice
	public static final int WINDOW_MY_REQUEST = 201; //ad_window_id for request
	public static final int WINDOW_MATERIAL_TRANSACTIONS = 223;

	// dialog boxes on the web UI, not mapped to any physical table
	public static final int DIALOG_DOCACTION_DIALOG = 400;
	public static final int DIALOG_PAYMENT_DIALOG = 401;
	public static final int DIALOG_ACCT_VIEWER = 402;
	public static final int DIALOG_CREATE_FROM_SHIPMENT = 403;
	public static final int DIALOG_CREATE_FROM_INVOICE = 404;
	public static final int DIALOG_CREATE_FROM_BANK_STATEMENT = 405;
	public static final int DIALOG_CREATE_FROM_PAYMENT_SELECTION = 406;
	public static final int DIALOG_CREATE_BUSINESS_PARTNER = 420; 
	public static final int DIALOG_CREATE_LOCATION = 421;
	public static final int DIALOG_PATTRIBUTE_WINDOW = 422;
	public static final int DIALOG_PRICE_HISTORY_WINDOW = 423;
	public static final int DIALOG_VALUE_PREFERENCE = 424;
	public static final int DIALOG_EMAIL_DIALOG = 425;
	public static final int DIALOG_LOCATOR_DETAIL_WINDOW = 426;
	public static final int DIALOG_ACCT_EDITOR = 427;
	public static final int DIALOG_PRINT_FORMAT = 428;
	public static final int DIALOG_CREATE_PRINT_FORMAT = 429;
	
	// others not mapped to any physical table	
	public static final int OTHER_HOME_WINDOW = 1;
	public static final int OTHER_REPORT_WINDOW = 2;




	
	
	public enum WindowType { WINDOW, FORM, INFO_WINDOW, OTHER, PROCESS, DIALOG };
	
	private WindowType m_type;
	private int m_entityID;
	private int m_mode;
	private FieldVO m_fieldVO;
	

	public UWindowID( WindowType type, int entityID )
	{
		this.m_type = type;
		this.m_entityID = entityID;
	}
	
	public UWindowID( WindowType type )
	{
		this( type, 0 );
	}
	
	public UWindowID()
	{
		this( null, 0 );
	}

	
	
	public WindowType getType()
	{
		return m_type;
	}
	
	// this is here only to prevent m_type from being made final
	protected void setType( WindowType type )
	{
		m_type = type; 
	}

	// this is here only to prevent m_entityID from being made final
	protected void setEntityID(int entityID){
		m_entityID = entityID;
	}
	
	public int getMode()
	{
		return m_mode;
	}

	public void setMode( int mode )
	{
		m_mode = mode;
	}
	
	public FieldVO getFieldVO()
	{
		return m_fieldVO;
	}

	public void setFieldVO( FieldVO fieldVO )
	{
		m_fieldVO = fieldVO;
	}

	
	public int getAD_Form_ID()
	{
		if( m_type != WindowType.FORM )
			throw new IllegalArgumentException();
		return m_entityID;
	}
	
	public int getAD_Window_ID()
	{
		if( m_type != WindowType.WINDOW )
			throw new IllegalArgumentException();
		return m_entityID;
	}

	public int getAD_InfoWindow_ID()
	{
		if( m_type != WindowType.INFO_WINDOW )
			throw new IllegalArgumentException();
		return m_entityID;
	}  

	public int getDialogID()
	{
		if( m_type != WindowType.DIALOG )
			throw new IllegalArgumentException();
		return m_entityID;
	}
	
	public int getOtherID()
	{
		if( m_type != WindowType.OTHER )
			throw new IllegalArgumentException();
		return m_entityID;
	}
	

	
	public static UWindowID getFromNode( NodeVO node )
	{
		UWindowID uid = null;
		switch( node.nodeType )
		{
		case NodeVO.TYPE_WINDOW:
			uid = new UWindowID( WindowType.WINDOW, node.entityID );
			break;
		case NodeVO.TYPE_REPORT:
			break;
		case NodeVO.TYPE_PROCESS:
			uid = new UWindowID( WindowType.PROCESS, node.entityID );
			break;
		case NodeVO.TYPE_WORKFLOW:
			break;
		case NodeVO.TYPE_WORKBENCH:
			break;
		case NodeVO.TYPE_SETVARIABLE:
			break;
		case NodeVO.TYPE_USERCHOICE:
			break;
		case NodeVO.TYPE_DOCACTION:
			break;
		case NodeVO.TYPE_ENTITY:
			break;
		case NodeVO.TYPE_COMPONENT:
			break;
		case NodeVO.TYPE_FORM:
			uid = new UWindowID( WindowType.FORM, node.entityID );
			break;
		case NodeVO.TYPE_TASK:	
			break;
		case NodeVO.TYPE_REQUESTS:
			break;
		case NodeVO.TYPE_NOTES:
			break;
		case NodeVO.TYPE_OTHER:
			uid = new UWindowID( WindowType.OTHER, node.entityID );
			break;
		}
		
		return uid;
	}

	public int getUniqueID() {
		return m_entityID*10+m_type.ordinal();
	}

}
