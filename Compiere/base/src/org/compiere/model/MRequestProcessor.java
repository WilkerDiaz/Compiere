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
 *	Request Processor Model
 *
 *  @author Jorg Janke
 *  @version $Id: MRequestProcessor.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MRequestProcessor extends X_R_RequestProcessor
	implements CompiereProcessor
{
    /** Logger for class MRequestProcessor */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRequestProcessor.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Active Request Processors (all Clients)
	 *	@param ctx context
	 *	@return array of Request
	 */
	public static MRequestProcessor[] getActive (Ctx ctx)
	{
		ArrayList<MRequestProcessor> list = new ArrayList<MRequestProcessor>();
		String sql = "SELECT * FROM R_RequestProcessor WHERE IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRequestProcessor (ctx, rs, null));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		MRequestProcessor[] retValue = new MRequestProcessor[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getActive

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MRequestProcessor.class);


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_RequestProcessor_ID id
	 */
	public MRequestProcessor (Ctx ctx, int R_RequestProcessor_ID, Trx trx)
	{
		super (ctx, R_RequestProcessor_ID, trx);
		if (R_RequestProcessor_ID == 0)
		{
		//	setName (null);
			setFrequencyType (FREQUENCYTYPE_Day);
			setFrequency (0);
			setKeepLogDays (7);
			setOverdueAlertDays (0);
			setOverdueAssignDays (0);
			setRemindDays (0);
		//	setSupervisor_ID (0);
		}
	}	//	MRequestProcessor

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRequestProcessor (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MRequestProcessor

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param Supervisor_ID Supervisor
	 */
	public MRequestProcessor (MClient parent, int Supervisor_ID)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setName (parent.getName() + " - "
			+ Msg.translate(getCtx(), "R_RequestProcessor_ID"));
		setSupervisor_ID (Supervisor_ID);
	}	//	MRequestProcessor


	/**	The Lines						*/
	private MRequestProcessorRoute[]	m_routes = null;

	/**
	 * 	Get Routes
	 *	@param reload reload data
	 *	@return array of routes
	 */
	public MRequestProcessorRoute[] getRoutes (boolean reload)
	{
		if ((m_routes != null) && !reload)
			return m_routes;

		String sql = "SELECT * FROM R_RequestProcessor_Route WHERE R_RequestProcessor_ID=? ORDER BY SeqNo";
		ArrayList<MRequestProcessorRoute> list = new ArrayList<MRequestProcessorRoute>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getR_RequestProcessor_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRequestProcessorRoute (getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		m_routes = new MRequestProcessorRoute[list.size ()];
		list.toArray (m_routes);
		return m_routes;
	}	//	getRoutes

	/**
	 * 	Get Logs
	 *	@return Array of Logs
	 */
	public CompiereProcessorLog[] getLogs()
	{
		ArrayList<MRequestProcessorLog> list = new ArrayList<MRequestProcessorLog>();
		String sql = "SELECT * "
			+ "FROM R_RequestProcessorLog "
			+ "WHERE R_RequestProcessor_ID=? "
			+ "ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getR_RequestProcessor_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRequestProcessorLog (getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MRequestProcessorLog[] retValue = new MRequestProcessorLog[list.size ()];
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
		String sql = "DELETE FROM R_RequestProcessorLog "
			+ "WHERE R_RequestProcessor_ID=? AND addDays(Created,?) < SysDate";
		Object[] params = new Object[]{getR_RequestProcessor_ID(),getKeepLogDays()};
		int no = DB.executeUpdate(get_Trx(), sql,params);
		return no;
	}	//	deleteLog

	/**
	 * 	Get the date Next run
	 * 	@param requery requery database
	 * 	@return date next run
	 */
	public Timestamp getDateNextRun (boolean requery)
	{
		if (requery)
			load(get_Trx());
		return getDateNextRun();
	}	//	getDateNextRun

	/**
	 * 	Get Unique ID
	 *	@return Unique ID
	 */
	public String getServerID()
	{
		return "RequestProcessor" + get_ID();
	}	//	getServerID

}	//	MRequestProcessor
