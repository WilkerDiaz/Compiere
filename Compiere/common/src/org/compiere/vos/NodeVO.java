package org.compiere.vos;

import java.io.*;
import java.util.ArrayList;

import org.compiere.common.*;
import org.compiere.util.ValueNamePair;

public class NodeVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NodeVO(){}
	
	public enum Mode { UNSPECIFIED, CREATE, SINGLE, MULTI };
	public Mode mode = Mode.UNSPECIFIED;

	/**	Level			*/
	public int     	level;
	
	
	/** Name			*/
	public String  		name;
	/** Description		*/
	public String  		description;
	/**	Parent ID		*/
	public int     		parentId;
	/** Node Id			*/
	public int 		nodeId;
	/** component Id	*/
	public int 		componentId;
	
	public int 		entityID;
	
	public String 	entityKey;
	
	public int 		nodeType;
	
	public boolean 	summary;
	
	public boolean 	hasChildren;
	public boolean isFavorite;
	
	public boolean  isEditable;  // Indicates if Edit hyperlink should be shown
	 
	/**	Window - 1			*/
	public static final int		TYPE_WINDOW = 1;
	/**	Report - 2			*/
	public static final int		TYPE_REPORT = 2;
	/**	Process - 3			*/
	public static final int		TYPE_PROCESS = 3;
	/**	Workflow - 4		*/
	public static final int		TYPE_WORKFLOW = 4;
	/**	Workbench - 5		*/
	public static final int		TYPE_WORKBENCH = 5;
	/**	Variable - 6		*/
	public static final int		TYPE_SETVARIABLE = 6;
	/**	Choice - 7			*/
	public static final int		TYPE_USERCHOICE = 7;
	/**	Action - 8			*/
	public static final int		TYPE_DOCACTION = 8;
	/**	Action - 9			*/
	public static final int		TYPE_ENTITY = 9;
	/**	Action - 10			*/
	public static final int		TYPE_COMPONENT = 10;
	
	public static final int		TYPE_NOTES = 14;
	
	public static final int		TYPE_REQUESTS = 13;
	
	public static final int 	TYPE_FORM = 11;

	public static final int 	TYPE_TASK = 12;	
	
	public static final int		TYPE_OTHER = 15;

	public static final int TYPE_INFO_WINDOW = 16;

	public static final int TYPE_PREFERENCES = 17;
	
	
	public QueryVO query = null;
	public ArrayList<ValueNamePair> fieldValuePairs = null; 

	public boolean isAdhoc = false;
	
	public boolean isExternal=false;
	
	public boolean isDashboard=false;
}
