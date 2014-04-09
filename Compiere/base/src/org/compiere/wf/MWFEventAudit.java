/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.wf;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Workflow Event Audit
 *	
 *  @author Jorg Janke
 *  @version $Id: MWFEventAudit.java,v 1.3 2006/07/30 00:51:06 jjanke Exp $
 */
public class MWFEventAudit extends X_AD_WF_EventAudit
{
    /** Logger for class MWFEventAudit */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MWFEventAudit.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Event Audit for node
	 *	@param ctx context
	 *	@param AD_WF_Process_ID process
	 *	@param AD_WF_Node_ID optional node
	 *	@return event audit or null
	 */
	public static MWFEventAudit[] get (Ctx ctx, int AD_WF_Process_ID, int AD_WF_Node_ID)
	{
		ArrayList<MWFEventAudit> list = new ArrayList<MWFEventAudit>();
		String sql = "SELECT * FROM AD_WF_EventAudit "
			+ "WHERE AD_WF_Process_ID=?";
		if (AD_WF_Node_ID > 0)
			sql += " AND AD_WF_Node_ID=?";
		sql += " ORDER BY CREATED";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_WF_Process_ID);
			if (AD_WF_Node_ID > 0)
				pstmt.setInt (2, AD_WF_Node_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MWFEventAudit (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "get", e);
		}
		finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		MWFEventAudit[] retValue = new MWFEventAudit[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Event Audit for node
	 *	@param ctx context
	 *	@param AD_WF_Process_ID process
	 *	@return event audit or null
	 */
	public static MWFEventAudit[] get (Ctx ctx, int AD_WF_Process_ID)
	{
		return get(ctx, AD_WF_Process_ID, 0);
	}	//	get
	
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MWFEventAudit.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 *	@param AD_WF_EventAudit_ID id
	 * 	@param trx transaction
	 */
	public MWFEventAudit (Ctx ctx, int AD_WF_EventAudit_ID, Trx trx)
	{
		super (ctx, AD_WF_EventAudit_ID, trx);
	}	//	MWFEventAudit

	/**
	 * 	Load Cosntructors
	 *	@param ctx context
	 *	@param rs result set
	 * 	@param trx transaction
	 */
	public MWFEventAudit (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MWFEventAudit

	/**
	 * 	Activity Constructor
	 *	@param activity activity
	 */
	public MWFEventAudit (MWFActivity activity)
	{
		super (activity.getCtx(), 0, activity.get_Trx());
		setAD_WF_Process_ID (activity.getAD_WF_Process_ID());
		setAD_WF_Node_ID (activity.getAD_WF_Node_ID());
		setAD_Table_ID (activity.getAD_Table_ID());
		setRecord_ID (activity.getRecord_ID());
		//
		setAD_WF_Responsible_ID (activity.getAD_WF_Responsible_ID());
		setAD_User_ID(activity.getAD_User_ID());
		//
		setWFState (activity.getWFState());
		setEventType (EVENTTYPE_ProcessCreated);
		setElapsedTimeMS (Env.ZERO);
		//
		MWFNode node = activity.getNode();
		if (node != null && node.get_ID() != 0)
		{
			String action = node.getAction();
			if (X_AD_WF_Node.ACTION_SetVariable.equals(action)
				|| X_AD_WF_Node.ACTION_UserChoice.equals(action))
			{
				setAttributeName(node.getAttributeName());
				setOldValue(String.valueOf(activity.getAttributeValue()));
				if (X_AD_WF_Node.ACTION_SetVariable.equals(action))
					setNewValue(node.getAttributeValue());
			}
		}
	}	//	MWFEventAudit
	
	/**
	 * 	Get Node Name
	 *	@return node name
	 */
	public String getNodeName()
	{
		MWFNode node = MWFNode.get(getCtx(), getAD_WF_Node_ID());
		if (node.get_ID() == 0)
			return "?";
		return node.getName(true);
	}	//	getNodeName
	
	
}	//	MWFEventAudit
