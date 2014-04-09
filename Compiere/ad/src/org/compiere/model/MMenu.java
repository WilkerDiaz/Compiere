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
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Menu Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MMenu.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MMenu extends X_AD_Menu
{
    /** Logger for class MMenu */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MMenu.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get menus with where clause
	 *	@param ctx context
	 *	@param whereClause where clause w/o the actual WHERE
	 *	@return MMenu
	 */
	public static MMenu[] get (Ctx ctx, String whereClause)
	{
		String sql = "SELECT * FROM AD_Menu";
		if (whereClause != null && whereClause.length() > 0)
			sql += " WHERE " + whereClause;
		ArrayList<MMenu> list = new ArrayList<MMenu>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MMenu (ctx, rs, null));
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		MMenu[] retValue = new MMenu[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MMenu.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Menu_ID id
	 *	@param trx transaction
	 */
	public MMenu (Ctx ctx, int AD_Menu_ID, Trx trx)
	{
		super (ctx, AD_Menu_ID, trx);
		if (AD_Menu_ID == 0)
		{
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsReadOnly (false);	// N
			setIsSummary (false);
		//	setName (null);
		}
	}	//	MMenu

	/**
	 * 	Load Contrusctor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MMenu (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MMenu

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Reset info
		if (isSummary() && getAction() != null)
			setAction(null);
		String action = getAction();
		if (action == null)
			action = "";
		//	Clean up references
		if (getAD_Window_ID() != 0 && !action.equals(ACTION_Window))
			setAD_Window_ID(0);
		if (getAD_Form_ID() != 0 && !action.equals(ACTION_Form))
			setAD_Form_ID(0);
		if (getAD_Workflow_ID() != 0 && !action.equals(ACTION_WorkFlow))
			setAD_Workflow_ID(0);
		if (getAD_Workbench_ID() != 0 && !action.equals(ACTION_Workbench))
			setAD_Workbench_ID(0);
		if (getAD_Task_ID() != 0 && !action.equals(ACTION_Task))
			setAD_Task_ID(0);
		if (getAD_Process_ID() != 0 
			&& !(action.equals(ACTION_Process) || action.equals(ACTION_Report)))
			setAD_Process_ID(0);
		return true;
	}	//	beforeSave
	
	/**
	 * 	String Info
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MMenu[");
		sb.append(get_ID())
			.append("-").append(getAction())
			.append("-").append(getName())
			.append("]");
		return sb.toString();
	}	//	toString

}	//	MMenu
