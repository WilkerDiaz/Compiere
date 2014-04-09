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

import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Session Model.
 *	Maintained in AMenu.
 *
 *  @author Jorg Janke
 *  @version $Id: MSession.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MSession extends X_AD_Session
{
    /** Logger for class MSession */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MSession.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Retrieve existing session
	 *	@param ctx context
	 *	@param createNew create if not found
	 *	@return session or null if session was ended or does not exist
	 */
	public static MSession get (Ctx ctx)
	{
		int AD_Session_ID = ctx.getContextAsInt("#AD_Session_ID");
		MSession session = null;
		if (AD_Session_ID > 0)
		{
			session = s_sessions.get(ctx, Integer.valueOf(AD_Session_ID));
			if (session == null)
			{
				session = new MSession(ctx, AD_Session_ID, null);
				if (session.getAD_Session_ID() != AD_Session_ID)
					session = null;
			}
		}
		if (session == null)
			s_log.fine("No Session");
		else if (session.isProcessed())
		{
			s_log.log(Level.WARNING, "Session Processed=" + session);
			s_sessions.remove(AD_Session_ID);
			return null;
		}
		return session;
	}	//	get

	/**
	 * 	Get existing or create remote session
	 *	@param ctx context
	 *	@param SessionType type of session
	 *	@param createNew create new session
	 *	@param createNew create if not found
	 *	@param Remote_Addr remote address
	 *	@param Remote_Host remote host
	 *	@param WebSession web session id
	 *	@return session
	 */
	public static MSession get (Ctx ctx, String SessionType, boolean createNew,
		String Remote_Addr, String Remote_Host, String WebSession)
	{
		int AD_Session_ID = ctx.getContextAsInt("#AD_Session_ID");
		MSession session = null;
		if (AD_Session_ID > 0)
		{
			session = s_sessions.get(ctx, Integer.valueOf(AD_Session_ID));
			if (session == null)
				session = new MSession(ctx, AD_Session_ID, null);
			if (session.isProcessed())
			{
				s_log.log(Level.WARNING, "Processed=" + session, new IllegalArgumentException("Processed"));
				s_sessions.remove(AD_Session_ID);
				return null;
			}
		}
		if (session == null)
		{
			if (createNew)
			{
				session = new MSession (ctx, SessionType,
					Remote_Addr, Remote_Host, WebSession, null);
				session.save();
				AD_Session_ID = session.getAD_Session_ID();
				ctx.setContext("#AD_Session_ID", AD_Session_ID);
				s_sessions.put(AD_Session_ID, session);
			}
			else
				s_log.warning("No Session!");
		}
		return session;
	}	//	get

	/**
	 * 	Persist status of current sessions
	 */
	public static void updateSessions()
	{
		int counter = 0;
		Collection<MSession> activeSessions = s_sessions.values();
		for (MSession session : activeSessions)
        {
	        if (!session.isProcessed())
	        {
	        	session.save();
	        	counter++;
	        }
        }
		s_log.info("#" + counter);
	}	//	updateSession

	/**
	 * 	Reset Session (logoff)
	 *	@param ctx context
	 *	@param type session type SESSIONTYPE_ or null for all
	 *	Database trigger<code>
	CREATE OR REPLACE TRIGGER CleanupSessions
	  AFTER STARTUP ON DATABASE
	BEGIN
	  UPDATE AD_Session
	  SET Processed='Y', LastActivityType='DBStartup'
	  WHERE Processed='N';
	  COMMIT;
	END;
	</code>
	 */

	public static void reset(Ctx ctx, String type)
	{
		int counter = 0;
		String sql = "SELECT * FROM AD_Session WHERE Processed='N'";
		if (!Util.isEmpty(type))
			sql += " AND SessionType=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
	        pstmt = DB.prepareStatement(sql, (Trx) null);
			if (!Util.isEmpty(type))
				pstmt.setString(1, type);
	        rs = pstmt.executeQuery();
	        while (rs.next())
	        {
	        	MSession session = new MSession(ctx, rs, null);
	        	session.logout("Reset");
	        	counter++;
	        }
        }
        catch (Exception e) {
        	s_log.log(Level.SEVERE, sql, e);
        }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

        s_log.info("Type=" + type + " #" + counter);
	}	//	reset

	/**	Sessions					*/
	private static final CCachePerm<Integer, MSession> s_sessions =
		new CCachePerm<Integer, MSession>("AD_Session_ID", 30, 0);	//	no time-out

	/**	Logger	*/
    private static CLogger s_log = CLogger.getCLogger(MSession.class);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Session_ID id
	 *	@param trx transaction
	 */
	public MSession (Ctx ctx, int AD_Session_ID, Trx trx)
	{
		super(ctx, AD_Session_ID, trx);
		if (AD_Session_ID == 0)
		{
			setProcessed (false);
			int AD_Role_ID = ctx.getAD_Role_ID();
			setAD_Role_ID(AD_Role_ID);
			setSessionType(SESSIONTYPE_Other);
			//
			try
			{
				InetAddress lh = InetAddress.getLocalHost();
				setRemote_Addr(lh.getHostAddress());
				setRemote_Host(lh.getHostName());
			}
			catch (UnknownHostException e)
			{
			}
		}
	}	//	MSession

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MSession(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MSession

	/**
	 * 	New (remote) Constructor
	 *	@param ctx context
	 *	@param optional Remote_Addr remote address
	 *	@param optional Remote_Host remote host
	 *	@param optional WebSession web session
	 *	@param trx transaction
	 */
	public MSession (Ctx ctx, String SessionType,
		String Remote_Addr, String Remote_Host, String WebSession, Trx trx)
	{
		this (ctx, 0, trx);
		setSessionType(SessionType);
		if (Remote_Addr != null)
			setRemote_Addr(Remote_Addr);
		if (Remote_Host != null)
			setRemote_Host(Remote_Host);
		if (WebSession != null)
			setWebSession(WebSession);
	}	//	MSession

	/**
	 * 	Set Last Activity
	 *	@param type LastActivityType
	 */
	private void setLastActivity(String type)
	{
		if (isProcessed())
		{
			log.warning("Processed <> Type=" + type);
			s_sessions.remove(Integer.valueOf(getAD_Session_ID()));
		}
		else
		{
			setLastActivityTime(new Timestamp(System.currentTimeMillis()));
			setLastActivityType(type);
		}
	}	//	setLastActivity

	/**
	 * 	Is it a Web Store Session
	 *	@return Returns true if Web Store Session.
	 */
	public boolean isWebStoreSession()
	{
		return SESSIONTYPE_WebStore.equals(getSessionType());
	}	//	isWebStoreSession

	/**
	 * 	Add to Description
	 *	@param Description description
	 */
	public void addDescription(String Description)
	{
		if (Util.isEmpty(Description))
			return;
		String dd = getDescription();
		if (Util.isEmpty(dd))
			dd = Description;
		else
			dd += "; " + Description;
		setDescription(dd);
	}	//	addDescription

	/**
	 * 	Before Save
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		if (isProcessed()						//	No update after Processed
			&& !is_ValueChanged("Processed"))	//	Otherwise Updated not accurate
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@Processed@"));
			return false;
		}
	    return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 */
	@Override
	protected boolean afterSave(boolean newRecord, boolean success)
	{
		if (success && isProcessed())
			s_sessions.remove(Integer.valueOf(getAD_Session_ID()));
	    return success;
	}	//	afterSave

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MSession[")
			.append(getAD_Session_ID())
			.append(",AD_User_ID=").append(getCreatedBy())
			.append(",").append(getCreated())
			.append(",Remote=").append(getRemote_Addr())
			.append("Type=").append(getSessionType());
		String s = getRemote_Host();
		if (s != null && s.length() > 0)
			sb.append(",").append(s);
		sb.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Session Logout
	 */
	public void logout()
	{
		logout("Logout");
	}	//	logout

	/**
	 * 	Session Logout
	 */
	public void logout(String activity)
	{
		setLastActivity(activity);
		setProcessed(true);
		save();
		log.info(TimeUtil.formatElapsed(getCreated(), getUpdated()));
	}	//	logout

	/**
	 * 	Is the information logged?
	 *	@param AD_Table_ID table id
	 *	@param tableName table name
	 *	@param type change type
	 *	@return true if table is logged
	 */
	public boolean isLogged(int AD_Table_ID, String tableName, String type)
	{
		//	No Log
		if (MChangeLog.isNotLogged(AD_Table_ID, tableName, 0, type))
			return false;
		//	Role Logging
		MRole role = MRole.getDefault(getCtx(), false);
		//	Do we need to log
		if (isWebStoreSession()						//	log if WebStore
			|| MChangeLog.isLogged(AD_Table_ID, type)		//	im/explicit log
			|| role != null && role.isChangeLog())	//	Role Logging
			return true;
		//
		return false;
	}	//	isLogged


	/**
	 * 	Create Change Log only if table is logged
	 * 	@param trx transaction
	 *	@param AD_ChangeLog_ID 0 for new change log
	 *	@param AD_Table_ID table
	 *	@param AD_Column_ID column
	 *	@param keyInfo key value(s)
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 *	@param OldValue old
	 *	@param NewValue new
	 *	@return saved change log or null
	 */
	public MChangeLog changeLog (Trx trx, int AD_ChangeLog_ID,
		int AD_Table_ID, int AD_Column_ID, Object keyInfo,
		int AD_Client_ID, int AD_Org_ID,
		Object OldValue, Object NewValue,
		String tableName, String type)
	{
		setLastActivity("ChangeLog");
		//	Null handling
		if (OldValue == null && NewValue == null)
			return null;
		//	Equal Value
		if (OldValue != null && NewValue != null && OldValue.equals(NewValue))
			return null;

		//	No Log
		if (MChangeLog.isNotLogged(AD_Table_ID, tableName, AD_Column_ID, type))
			return null;

		//	Role Logging
		MRole role = MRole.getDefault(getCtx(), false);
		//	Do we need to log
		if (isWebStoreSession()						//	log if WebStore
			|| MChangeLog.isLogged(AD_Table_ID, type)		//	im/explicit log
			|| role != null && role.isChangeLog())	//	Role Logging
			;
		else
			return null;
		//
		log.finest("AD_ChangeLog_ID=" + AD_ChangeLog_ID
				+ ", AD_Session_ID=" + getAD_Session_ID()
				+ ", AD_Table_ID=" + AD_Table_ID + ", AD_Column_ID=" + AD_Column_ID
				+ ": " + OldValue + " -> " + NewValue);

		try
		{
			String trxName = null;
			if( trx != null )
				trxName = trx.getTrxName();
			MChangeLog cl = new MChangeLog(getCtx(),
				AD_ChangeLog_ID, trxName, getAD_Session_ID(),
				AD_Table_ID, AD_Column_ID, keyInfo, AD_Client_ID, AD_Org_ID,
				OldValue, NewValue, type);
			if (cl.save())
				return cl;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "AD_ChangeLog_ID=" + AD_ChangeLog_ID
				+ ", AD_Session_ID=" + getAD_Session_ID()
				+ ", AD_Table_ID=" + AD_Table_ID + ", AD_Column_ID=" + AD_Column_ID, e);
			return null;
		}
		log.log(Level.SEVERE, "AD_ChangeLog_ID=" + AD_ChangeLog_ID
			+ ", AD_Session_ID=" + getAD_Session_ID()
			+ ", AD_Table_ID=" + AD_Table_ID + ", AD_Column_ID=" + AD_Column_ID);
		return null;
	}	//	changeLog

	/**
	 * 	Create Query Log
	 *	@param AD_Client_ID login client
	 *	@param AD_Org_ID login org
	 *	@param AD_Table_ID table
	 *	@param whereClause where
	 *	@param recordCount records
	 *	@param parameter parameter
	 *	@return Log
	 */
	public MQueryLog queryLog(int AD_Client_ID, int AD_Org_ID,
		int AD_Table_ID, String whereClause, int recordCount, String parameter)
	{
		setLastActivity("Query");
		//	Filter Where Clause
		int index = whereClause.indexOf(" WHERE ");
		if (index >= 0)
			whereClause = whereClause.substring(index+7);
		index = whereClause.indexOf(" ORDER BY ");
		if (index >= 0)
			whereClause = whereClause.substring(0, index);
		//
		MQueryLog qlog = null;
		try
		{
			qlog = new MQueryLog(getCtx(), getAD_Session_ID(),
				AD_Client_ID, AD_Org_ID,
				AD_Table_ID, whereClause, recordCount, parameter);
			qlog.save();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "AD_Session_ID=" + getAD_Session_ID()
				+ ", AD_Table_ID=" + AD_Table_ID + ", Where=" + whereClause
				, e);
		}
		return qlog;
	}	//	queryLog

	/**
	 * 	Create Query Log
	 *	@param AD_Client_ID login client
	 *	@param AD_Org_ID login org
	 *	@param AD_Table_ID table
	 *	@param whereClause where or full sql clause
	 *	@param recordCount records
	 *	@return Log
	 */
	public MQueryLog queryLog(int AD_Client_ID, int AD_Org_ID,
		int AD_Table_ID, String whereClause, int recordCount)
	{
		return queryLog(AD_Client_ID, AD_Org_ID, AD_Table_ID,
			whereClause, recordCount, (String)null);
	}	//	queryLog

	/**
	 * 	Create Query Log
	 *	@param AD_Client_ID login client
	 *	@param AD_Org_ID login org
	 *	@param AD_Table_ID table
	 *	@param whereClause where
	 *	@param recordCount records
	 *	@param parameter parameter
	 *	@return Log
	 */
	public MQueryLog queryLog(int AD_Client_ID, int AD_Org_ID,
		int AD_Table_ID, String whereClause, int recordCount, Object parameter)
	{
		String para = null;
		if (parameter != null)
			para = parameter.toString();
		return queryLog(AD_Client_ID, AD_Org_ID, AD_Table_ID,
			whereClause, recordCount, para);
	}	//	queryLog

	/**
	 * 	Create Query Log
	 *	@param AD_Client_ID login client
	 *	@param AD_Org_ID login org
	 *	@param AD_Table_ID table
	 *	@param whereClause where
	 *	@param recordCount records
	 *	@param parameters parameter
	 *	@return Log
	 */
	public MQueryLog queryLog(int AD_Client_ID, int AD_Org_ID,
		int AD_Table_ID, String whereClause, int recordCount, Object[] parameters)
	{
		String para = null;
		if (parameters != null && parameters.length > 0)
		{
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < parameters.length; i++)
			{
				if (i > 0)
					sb.append(", ");
				if (parameters[i] == null)
					sb.append("NULL");
				else
					sb.append(parameters[i].toString());
			}
			para = sb.toString();
		}
		return queryLog(AD_Client_ID, AD_Org_ID, AD_Table_ID,
			whereClause, recordCount, para);
	}	//	queryLog

	/**
	 * 	Create Window Log
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 *	@param AD_Window_ID window
	 *	@param AD_Form_ID form
	 *	@return Log
	 */
	public MWindowLog windowLog(int AD_Client_ID, int AD_Org_ID,
		int AD_Window_ID, int AD_Form_ID)
	{
		setLastActivity("Window");
		MWindowLog wlog = null;
		try
		{
			wlog = new MWindowLog(getCtx(), getAD_Session_ID(),
				AD_Client_ID, AD_Org_ID,
				AD_Window_ID, AD_Form_ID);
			wlog.save();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "AD_Session_ID=" + getAD_Session_ID()
				+ ", AD_Window_ID=" + AD_Window_ID + ", AD_Form_ID=" + AD_Form_ID
				, e);
		}
		return wlog;
	}	//	windowLog

}	//	MSession

