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
 *	Alert Processor
 *	
 *  @author Jorg Janke
 *  @version $Id: MAlertProcessor.java 8824 2010-05-26 18:03:02Z ragrawal $
 */
public class MAlertProcessor extends X_AD_AlertProcessor
	implements CompiereProcessor
{
    /** Logger for class MAlertProcessor */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAlertProcessor.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Active
	 *	@param ctx context
	 *	@return active processors
	 */
	public static MAlertProcessor[] getActive (Ctx ctx)
	{
		ArrayList<MAlertProcessor> list = new ArrayList<MAlertProcessor>();
		String sql = "SELECT * FROM AD_AlertProcessor WHERE IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MAlertProcessor (ctx, rs, null));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MAlertProcessor[] retValue = new MAlertProcessor[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getActive

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MAlertProcessor.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_AlertProcessor_ID id
	 *	@param trx transaction
	 */
	public MAlertProcessor (Ctx ctx, int AD_AlertProcessor_ID, Trx trx)
	{
		super (ctx, AD_AlertProcessor_ID, trx);
	}	//	MAlertProcessor

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAlertProcessor (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAlertProcessor

	/**	The Alerts						*/
	private MAlert[]		m_alerts = null;

	/**
	 * 	Get Server ID
	 *	@return id
	 */
	public String getServerID ()
	{
		return "AlertProcessor" + get_ID();
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
		ArrayList<MAlertProcessorLog> list = new ArrayList<MAlertProcessorLog>();
		String sql = "SELECT * "
			+ "FROM AD_AlertProcessorLog "
			+ "WHERE AD_AlertProcessor_ID=? " 
			+ "ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, getAD_AlertProcessor_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MAlertProcessorLog (getCtx(), rs, get_Trx()));
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
		MAlertProcessorLog[] retValue = new MAlertProcessorLog[list.size ()];
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
		String sql = "DELETE FROM AD_AlertProcessorLog "
			+ "WHERE AD_AlertProcessor_ID= ? AND addDays(Created,?) < SysDate";
		Object[] params = new Object[]{getAD_AlertProcessor_ID(),getKeepLogDays()};
		DB.executeUpdate(get_Trx(), sql,params);
		return 0;
	}	//	deleteLog

	
	/**
	 * 	Get Alerts
	 *	@param reload reload data
	 *	@return array of alerts
	 */
	public MAlert[] getAlerts (boolean reload)
	{
		if (m_alerts != null && !reload)
			return m_alerts;
		String sql = "SELECT * FROM AD_Alert "
			+ "WHERE AD_AlertProcessor_ID=?" 
			+ "AND IsActive='Y'";
		ArrayList<MAlert> list = new ArrayList<MAlert>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, getAD_AlertProcessor_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MAlert (getCtx(), rs, get_Trx()));
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
		//
		m_alerts = new MAlert[list.size ()];
		list.toArray (m_alerts);
		return m_alerts;
	}	//	getAlerts

}	//	MAlertProcessor
