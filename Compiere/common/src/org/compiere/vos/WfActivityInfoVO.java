package org.compiere.vos;

import java.io.*;

import org.compiere.util.*;

public class WfActivityInfoVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * constants were copied over from X_AD_WF_Node.java - since this is
	 * generated manual code comparison are necessary when setting this type on
	 * the server and passing to the client (see WorkflowServiceImpl).
	 */
	/** User Workbench = B */
	public static final String ACTION_UserWorkbench = "B";
	/** User Choice = C */
	public static final String ACTION_UserChoice = "C";

	/** User Window = W */
	public static final String ACTION_UserWindow = "W";
	/** User Form = X */
	public static final String ACTION_UserForm = "X";

	public WfActivityInfoVO() {
	};

	public int AD_WF_Activity_ID = 0;
	public String name;
	public String help;
	public String description;
	public String history;
	public ValueNamePair[] values;
	public FieldVO forwardVO;
	public int displayType;
	public String action;
	public int AD_Table_ID;
	public int Record_ID;
	public int nodeID;
	public String columnName;
}
