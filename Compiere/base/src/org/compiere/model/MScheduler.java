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

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;


/**
 *	Scheduler Model
 *
 *  @author Jorg Janke
 *  @version $Id: MScheduler.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MScheduler extends X_AD_Scheduler
	implements CompiereProcessor
{
    /** Logger for class MScheduler */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MScheduler.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Active
	 *	@param ctx context
	 *	@return active processors
	 */
	public static MScheduler[] getActive (Ctx ctx)
	{
		ArrayList<MScheduler> list = new ArrayList<MScheduler>();
		String sql = "SELECT * FROM AD_Scheduler WHERE IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MScheduler (ctx, rs, null));
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
		MScheduler[] retValue = new MScheduler[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getActive

	/**	Static Logger			*/
	private static CLogger	s_log	= CLogger.getCLogger (MScheduler.class);
	/** Scheduler Result		*/
	private static final int AD_Message_ID = 884;		//	HARDCODED SchedulerResult

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Scheduler_ID id
	 *	@param trx transaction
	 */
	public MScheduler (Ctx ctx, int AD_Scheduler_ID, Trx trx)
	{
		super (ctx, AD_Scheduler_ID, trx);
		if (AD_Scheduler_ID == 0)
		{
		//	setAD_Process_ID (0);
		//	setName (null);
			setFrequencyType (FREQUENCYTYPE_Day);
			setFrequency (1);
			//
			setKeepLogDays (7);
		//	setSupervisor_ID (0);
		}
	}	//	MScheduler

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MScheduler (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MScheduler

	/** Process to be executed		*/
	private MProcess m_process = null;
	/**	Scheduler Parameter			*/
	private MSchedulerPara[] 		m_parameter = null;
	/** Scheduler Recipients		*/
	private MSchedulerRecipient[]	m_recipients = null;
	/** The Supervisor				*/
	private MUser					m_supervisor = null;

	/**
	 * 	Get Server ID
	 *	@return id
	 */
	public String getServerID ()
	{
		return "Scheduler" + get_ID();
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
	public CompiereProcessorLog[] getLogs()
	{
		ArrayList<MSchedulerLog> list = new ArrayList<MSchedulerLog>();
		String sql = "SELECT * "
			+ "FROM AD_SchedulerLog "
			+ "WHERE AD_Scheduler_ID=? "
			+ "ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getAD_Scheduler_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MSchedulerLog (getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		MSchedulerLog[] retValue = new MSchedulerLog[list.size ()];
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
		String sql = "DELETE FROM AD_SchedulerLog "
			+ "WHERE AD_Scheduler_ID= ? " 
			+ " AND Created < SysDate- ? ";
		Object[] params = new Object[]{getAD_Scheduler_ID(),getKeepLogDays()};
		int no = DB.executeUpdate(get_Trx(), sql,params);
		return no;
	}	//	deleteLog

	/**
	 * 	Get Process
	 *	@return process
	 */
	public MProcess getProcess()
	{
		if (m_process == null)
			m_process = new MProcess (getCtx(), getAD_Process_ID(), null);
		return m_process;
	}	//	getProcess

	/**
	 * 	Get Parameters
	 *	@param reload reload
	 *	@return parameter
	 */
	public MSchedulerPara[] getParameters (boolean reload)
	{
		if (!reload && (m_parameter != null))
			return m_parameter;
		ArrayList<MSchedulerPara> list = new ArrayList<MSchedulerPara>();
		String sql = "SELECT * FROM AD_Scheduler_Para WHERE AD_Scheduler_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, getAD_Scheduler_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MSchedulerPara (getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		m_parameter = new MSchedulerPara[list.size()];
		list.toArray(m_parameter);
		return m_parameter;
	}	//	getParameter

	/**
	 * 	Get Recipients
	 *	@param reload reload
	 *	@return Recipients
	 */
	public MSchedulerRecipient[] getRecipients (boolean reload)
	{
		if (!reload && (m_recipients != null))
			return m_recipients;
		ArrayList<MSchedulerRecipient> list = new ArrayList<MSchedulerRecipient>();
		String sql = "SELECT * FROM AD_SchedulerRecipient WHERE AD_Scheduler_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, getAD_Scheduler_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MSchedulerRecipient (getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		m_recipients = new MSchedulerRecipient[list.size()];
		list.toArray(m_recipients);
		return m_recipients;
	}	//	getRecipients

	/**
	 * 	Get Recipient AD_User_IDs
	 *	@return array of user IDs
	 */
	public Integer[] getRecipientAD_User_IDs()
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		MSchedulerRecipient[] recipients = getRecipients(false);
		for (MSchedulerRecipient recipient : recipients)
		{
			if (!recipient.isActive())
				continue;
			if (recipient.getAD_User_ID() != 0)
			{
				Integer ii = Integer.valueOf(recipient.getAD_User_ID());
				if (!list.contains(ii))
					list.add(ii);
			}
			if (recipient.getAD_Role_ID() != 0)
			{
				MUserRoles[] urs = MUserRoles.getOfRole(getCtx(), recipient.getAD_Role_ID());
				for (MUserRoles ur : urs) {
					if (!ur.isActive())
						continue;
					Integer ii = Integer.valueOf(ur.getAD_User_ID());
					if (!list.contains(ii))
						list.add(ii);
				}
			}
		}
		//	Add Updater
		if (list.size() == 0)
		{
			Integer ii = Integer.valueOf(getUpdatedBy());
			list.add(ii);
		}
		//
		Integer[] recipientIDs = new Integer[list.size()];
		list.toArray(recipientIDs);
		return recipientIDs;
	}	//	getRecipientAD_User_IDs


	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MScheduler[");
		sb.append (get_ID()).append ("-").append (getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Run Scheduler
	 *	@param p_trx transaction
	 *	@return Summary
	 *	@throws Exception
	 */
	public String execute (Trx p_trx) throws Exception
	{
		if (m_process == null)
			getProcess();
		if (m_process.isReport())
			return runReport(p_trx);
		else
			return runProcess(p_trx);
	}	//	execute

	/**
	 * 	Run Report
	 *	@param p_trx p_trx
	 *	@return summary
	 *	@throws Exception
	 */
	private String runReport (Trx p_trx) throws Exception
	{
		log.info(m_process.toString());
		if (!m_process.isReport() || (m_process.getAD_ReportView_ID() == 0))
			return "Not a Report: AD_Process_ID=" + m_process.getAD_Process_ID()
				+ " - " + m_process.getName();
		//	Process
		int AD_Table_ID = 0;
		int Record_ID = 0;
		//
		MPInstance pInstance = new MPInstance(m_process, Record_ID);
		String error = fillParameter(pInstance);
		if (error != null)
		{
			notifySupervisor(false, error, null);
			return error;
		}

		//
		ProcessInfo pi = new ProcessInfo (m_process.getName(), m_process.getAD_Process_ID(),
			AD_Table_ID, Record_ID);
		pi.setAD_User_ID(getUpdatedBy());
		pi.setAD_Client_ID(getAD_Client_ID());
		pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());
		if (!m_process.processIt(pi, p_trx) && (pi.getClassName() != null))
		{
			String msg = "Process failed: (" + pi.getClassName() + ") " + pi.getSummary();
			notifySupervisor(false, msg, null);
			return msg;
		}

		//	Report
		ReportEngine re = ReportEngine.get(getCtx(), pi);
		if (re == null)
		{
			String msg = "Cannot create Report AD_Process_ID=" + m_process.getAD_Process_ID()
				+ " - " + m_process.getName();
			notifySupervisor(false, msg, null);
			return msg;
		}
		File report = re.getPDF();
		//	Notice
		Integer[] userIDs = getRecipientAD_User_IDs();
		for (Integer userID : userIDs)
		{
			MNote note = new MNote(getCtx(),
					AD_Message_ID, userID.intValue(), p_trx);
			note.setClientOrg(getAD_Client_ID(), getAD_Org_ID());
			note.setTextMsg(getName());
			note.setDescription(getDescription());
			note.setRecord(AD_Table_ID, Record_ID);
			note.save();
			//	Attachment
			MAttachment attachment = new MAttachment (getCtx(),
					X_AD_Note.Table_ID, note.getAD_Note_ID(), p_trx);
			attachment.setClientOrg(getAD_Client_ID(), getAD_Org_ID());
			attachment.addEntry(report);
			attachment.setTextMsg(getName());
			attachment.save();
		}
		//
		notifySupervisor(true, pi.getSummary(), report);
		return pi.getSummary();
	}	//	runReport

	/**
	 * 	Run Process
	 *	@param transaction
	 *	@return summary
	 *	@throws Exception
	 */
	private String runProcess (Trx p_trx) throws Exception
	{
		log.info(m_process.toString());
		//	Process (see also MWFActivity.performWork
		int AD_Table_ID = 0;
		int Record_ID = 0;
		//
		MPInstance pInstance = new MPInstance(m_process, Record_ID);
		String error = fillParameter(pInstance);
		if (error != null)
		{
			notifySupervisor(false, error, null);
			return error;
		}
		//
		ProcessInfo pi = new ProcessInfo (m_process.getName(), m_process.getAD_Process_ID(),
			AD_Table_ID, Record_ID);
		pi.setAD_User_ID(getUpdatedBy());
		pi.setAD_Client_ID(getAD_Client_ID());
		pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());
		m_process.processIt(pi, p_trx);
		notifySupervisor(!pi.isError(), pi.getSummary(), null);
		return pi.getSummary();
	}	//	runProcess

	/**
	 * 	Fill Parameter
	 *	@param pInstance process instance
	 *	@return error message - null if no error
	 */
	private String fillParameter(MPInstance pInstance)
	{
		StringBuffer sb = null;
		MSchedulerPara[] sParams = getParameters (false);
		MPInstancePara[] iParams = pInstance.getParameters();
		for (MPInstancePara iPara : iParams)
		{
			for (MSchedulerPara sPara : sParams)
			{
				if (iPara.getParameterName().equals(sPara.getColumnName()))
				{
					String variable = sPara.getParameterDefault();
					log.fine(sPara.getColumnName() + " = " + variable);
					//	Value - Constant/Variable
					String value = variable;
					if ((variable == null)
						|| ((variable != null) && (variable.length() == 0)))
						value = null;
					else if (variable.indexOf("@") != -1)	//	we may have a variable
					{
						//	Strip
						int index = variable.indexOf("@");
						String columnName = variable.substring(index+1);
						index = columnName.indexOf("@");
						if (index != -1)
						{
							columnName = columnName.substring(0, index);
							//	try Env
							String env = getCtx().getContext( columnName);
							if (env.length() == 0)
							{
								log.warning(sPara.getColumnName()
									+ " - not in environment =" + columnName
									+ "(" + variable + ") - ignored");
								break;
							}
							else
								value = env;
						}
					}	//	@variable@

					//	No Value
					if (value == null)
					{
						log.fine(sPara.getColumnName() + " - empty");
						break;
					}

					//	Convert to Type
					try
					{
						if (FieldType.isNumeric(sPara.getDisplayType())
							|| FieldType.isID(sPara.getDisplayType()))
						{
							BigDecimal bd = new BigDecimal (value);
							iPara.setP_Number(bd);
							log.fine(sPara.getColumnName()
								+ " = " + variable + " (=" + bd + "=)");
						}
						else if (FieldType.isDate(sPara.getDisplayType()))
						{
							Timestamp ts = Timestamp.valueOf(value);
							iPara.setP_Date(ts);
							log.fine(sPara.getColumnName()
								+ " = " + variable + " (=" + ts + "=)");
						}
						else
						{
							iPara.setP_String(value);
							log.fine(sPara.getColumnName()
								+ " = " + variable
								+ " (=" + value + "=) " + value.getClass().getName());
						}
						if (!iPara.save())
							log.warning("Not Saved - " + sPara.getColumnName());
					}
					catch (Exception e)
					{
						String msg = sPara.getColumnName()
							+ " = " + variable + " (" + value
							+ ") " + value.getClass().getName()
							+ " - " + e.getLocalizedMessage();
						log.warning(msg);
						if (sb == null)
							sb = new StringBuffer(msg);
						else
							sb.append("; ").append(msg);
					}
					break;
				}	//	parameter match
			}	//	scheduler parameter loop
		}	//	instance parameter loop
		if (sb == null)
			return null;
		return sb.toString();
	}	//	fillParameter

	/**
	 * 	Send EMail/Notice to Supervisor
	 *	@param success run a success
	 *	@param message message
	 *	@param attachmentFile optional attachment
	 *	@return true if sent
	 */
	private boolean notifySupervisor(boolean success, String message, File attachmentFile)
	{
		if (m_supervisor == null)
			m_supervisor = MUser.get(getCtx(), getSupervisor_ID());
		//	Send Mail
		if (m_supervisor.isNotificationEMail())
		{
			MClient client = MClient.get(getCtx(), getAD_Client_ID());
			String subject = client.getName() + ": " + getName()
				+ (success ? " OK" : " Error");
			if (client.sendEMail(getSupervisor_ID(), subject, message, attachmentFile))
				return true;
		}
		//	Create Notice
		MNote note = new MNote(getCtx(), AD_Message_ID, getSupervisor_ID(), null);
		note.setClientOrg(getAD_Client_ID(), getAD_Org_ID());
		note.setTextMsg(getName());
		note.setDescription(message);
		note.setRecord(Table_ID, get_ID());		//	point to this
		boolean ok = note.save();
		//	Attachment
		if (ok && (attachmentFile != null))
		{
			MAttachment attachment = new MAttachment (getCtx(),
				X_AD_Note.Table_ID, note.getAD_Note_ID(), null);
			attachment.setClientOrg(getAD_Client_ID(), getAD_Org_ID());
			attachment.addEntry(attachmentFile);
			attachment.setTextMsg(getName());
			attachment.save();
		}
		return ok;
	}	//	sendEMail

}	//	MScheduler
