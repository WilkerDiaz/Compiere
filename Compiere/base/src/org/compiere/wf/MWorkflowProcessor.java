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
 *	Workflow Processor Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MWorkflowProcessor.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MWorkflowProcessor extends X_AD_WorkflowProcessor
	implements CompiereProcessor
{
    /** Logger for class MWorkflowProcessor */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MWorkflowProcessor.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Active
	 *	@param ctx context
	 *	@return active processors
	 */
	public static MWorkflowProcessor[] getActive (Ctx ctx)
	{
		ArrayList<MWorkflowProcessor> list = new ArrayList<MWorkflowProcessor>();
		String sql = "SELECT * FROM AD_WorkflowProcessor WHERE IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MWorkflowProcessor (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		MWorkflowProcessor[] retValue = new MWorkflowProcessor[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getActive
	
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MWorkflowProcessor.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_WorkflowProcessor_ID id
	 * 	@param trx transaction
	 */
	public MWorkflowProcessor (Ctx ctx, int AD_WorkflowProcessor_ID, Trx trx)
	{
		super (ctx, AD_WorkflowProcessor_ID, trx);
	}	//	MWorkflowProcessor

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 * 	@param trx transaction
	 */
	public MWorkflowProcessor (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MWorkflowProcessor

	/**
	 * 	Get Server ID
	 *	@return id
	 */
	public String getServerID ()
	{
		return "WorkflowProcessor" + get_ID();
	}	//	getServerID

	/**
	 * 	Get Date Next Run
	 *	@param requery requery
	 *	@return date next run
	 */
	public Timestamp getDateNextRun (boolean requery)
	{
		if (requery)
			load(get_Trx());
		return getDateNextRun();
	}	//	getDateNextRun

	/**
	 * 	Get Logs
	 *	@return logs
	 */
	public CompiereProcessorLog[] getLogs ()
	{
		ArrayList<MWorkflowProcessorLog> list = new ArrayList<MWorkflowProcessorLog>();
		String sql = "SELECT * "
			+ "FROM AD_WorkflowProcessorLog "
			+ "WHERE AD_WorkflowProcessor_ID=? " 
			+ "ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getAD_WorkflowProcessor_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MWorkflowProcessorLog (getCtx(), rs, get_Trx()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		MWorkflowProcessorLog[] retValue = new MWorkflowProcessorLog[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getLogs
	
	/**
	 * 	Delete old Request Log
	 *	@return number of records
	 */
	public int deleteLog()
	{
		if (getKeepLogDays() < 1)
			return 0;
		String sql = "DELETE FROM AD_WorkflowProcessorLog "
			+ "WHERE AD_WorkflowProcessor_ID=" + getAD_WorkflowProcessor_ID() 
			//jz + " AND (Created+" + getKeepLogDays() + ") < SysDate";
			+ " AND addDays(Created," + getKeepLogDays() + ") < SysDate";
		DB.executeUpdate(get_Trx(), sql);
		return 0;
	}	//	deleteLog

}	//	MWorkflowProcessor
