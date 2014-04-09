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
package org.compiere.server;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Alert Processor
 *	
 *  @author Jorg Janke
 *  @version $Id: AlertProcessor.java 8778 2010-05-19 19:00:10Z ragrawal $
 */
public class AlertProcessor extends CompiereServer
{
	/**
	 * 	Alert Processor
	 *	@param model model
	 */
	public AlertProcessor (MAlertProcessor model)
	{
		super (model, 180);		//	3 minute delay 
		m_model = model;
		m_client = MClient.get(model.getCtx(), model.getAD_Client_ID());
	}	//	AlertProcessor

	/**	The Concrete Model			*/
	private MAlertProcessor		m_model = null;
	/**	Last Summary				*/
	private StringBuffer 		m_summary = new StringBuffer();
	/**	Last Error Msg				*/
	private StringBuffer 		m_errors = new StringBuffer();
	/** Client info					*/
	private MClient 			m_client = null;
	/** Mail/Notice Recipients			*/
	private ArrayList<Integer>	m_recipients = new ArrayList<Integer>();

	/**
	 * 	Work
	 */
	@Override
	protected void doWork ()
	{
		m_summary = new StringBuffer();
		m_errors = new StringBuffer();
		//
		int count = 0;
		int countError = 0;
		MAlert[] alerts = m_model.getAlerts(true);
		for (int i = 0; i < alerts.length; i++)
		{
			if (!processAlert(alerts[i]))
				countError++;
			count++;
		}
		//
		String summary = "Alerts=" + count;
		if (countError > 0)
			summary += ", Not processed=" + countError;
		summary += " - ";
		m_summary.insert(0, summary);
		//
		int no = m_model.deleteLog();
		m_summary.append("Logs deleted=").append(no);
		//
		MAlertProcessorLog pLog = new MAlertProcessorLog(m_model, m_summary.toString());
		pLog.setReference("#" + String.valueOf(p_runCount) 
			+ " - " + TimeUtil.formatElapsed(new Timestamp(p_startWork)));
		pLog.setTextMsg(m_errors.toString());
		pLog.save();
	}	//	doWork

	/**
	 * 	Process Alert
	 *	@param alert alert
	 *	@return true if processed
	 */
	private boolean processAlert (MAlert alert)
	{
		if (!alert.isValid())
		{
			log.info("Invalid: " + alert);
			return false;
		}
		log.info("" + alert);
		m_recipients.clear();

		StringBuffer message = new StringBuffer(alert.getAlertMessage())
			.append(Env.NL);
		//	Context
		Ctx ctx = alert.getCtx();
		ctx.setAD_Client_ID(alert.getAD_Client_ID());
		ctx.setAD_Org_ID(alert.getAD_Org_ID());
		//
		boolean valid = true;
		boolean processed = false;
		MAlertRule[] rules = alert.getRules(false);
		for (int i = 0; i < rules.length; i++)
		{
			if (i > 0)
				message.append(Env.NL).append("================================").append(Env.NL);
			Trx trx = null;		//	assume r/o
			
			MAlertRule rule = rules[i];
			if (!rule.isValid())
			{
				log.config("Invalid: " + rule);
				continue;
			}
			log.fine("" + rule);
			
			//	Pre
			String sql = rule.getPreProcessing();
			if (sql != null && sql.length() > 0)
			{
				int no = DB.executeUpdate(trx, sql);
				if (no == -1)
				{
					ValueNamePair error = CLogger.retrieveError();
					rule.setErrorMsg("Pre=" + error.getName());
					m_errors.append("Pre=" + error.getName());
					rule.setIsValid(false);
					rule.save();
					valid = false;
					break;
				}
			}	//	Pre
			
			//	The processing
			ctx.setAD_Role_ID(0);
			ctx.setAD_User_ID(0);
			sql = rule.getSql();
			if (alert.isEnforceRoleSecurity()
				|| alert.isEnforceClientSecurity())
			{
				int AD_Role_ID = alert.getFirstAD_Role_ID();
				if (AD_Role_ID == -1)
					AD_Role_ID = alert.getFirstUserAD_Role_ID();
				if (AD_Role_ID != -1)
				{
					String tableName = rule.getTableName();
					boolean fullyQualified = MRole.SQL_FULLYQUALIFIED;
					if (Util.isEmpty(tableName))
						fullyQualified = MRole.SQL_NOTQUALIFIED;
					MRole role = MRole.get(ctx, AD_Role_ID, 0 , false);
					sql = role.addAccessSQL(sql, tableName, 
						fullyQualified, MRole.SQL_RO);
					ctx.setAD_Role_ID(AD_Role_ID);
				}
				if (alert.getFirstAD_User_ID() != -1)
					ctx.setAD_User_ID(alert.getFirstAD_User_ID());
			}
			
			try
			{
				String text = listSqlSelect(sql, trx);
				if (text != null && text.length() > 0)
				{
					message.append(text);
					processed = true;
					int index = text.indexOf(":");
					if (index > 0 && index < 5)
						m_summary.append(text.subSequence(0, index));
				}
			}
			catch (Exception e)
			{
				rule.setErrorMsg("Select=" + e.getLocalizedMessage());
				m_errors.append("Select=" + e.getLocalizedMessage());
				rule.setIsValid(false);
				rule.save();
				valid = false;
				break;
			}

			//	Post
			sql = rule.getPostProcessing();
			if (sql != null && sql.length() > 0)
			{
				int no = DB.executeUpdate(trx, sql);
				if (no == -1)
				{
					ValueNamePair error = CLogger.retrieveError();
					rule.setErrorMsg("Post=" + error.getName());
					m_errors.append("Post=" + error.getName());
					rule.setIsValid(false);
					rule.save();
					valid = false;
					break;
				}
			}	//	Post
			
			/**	Trx				*/
			if (trx != null)
			{
				Trx p_trx = trx;
				if (p_trx != null)
				{
					p_trx.commit();
					p_trx.close();
				}
			}
		}	//	 for all rules
		
		//	Update header if error
		if (!valid)
		{
			alert.setIsValid(false);
			alert.save();
			return false;
		}
		
		//	Nothing to report
		if (!processed)
		{
			m_summary.append(alert.getName()).append("=No Result - ");
			return true;
		}
		
		//	Send Message
		int countRecipient = 0;
		MAlertRecipient[] recipients = alert.getRecipients(false);
		for (int i = 0; i < recipients.length; i++)
		{
			MAlertRecipient recipient = recipients[i];
			if (recipient.getAD_User_ID() >= 0)		//	System == 0
				if (sendInfo(recipient.getAD_User_ID(), alert, message.toString()))
					countRecipient++;
			if (recipient.getAD_Role_ID() >= 0)		//	SystemAdministrator == 0
			{
				MUserRoles[] urs = MUserRoles.getOfRole(getCtx(), recipient.getAD_Role_ID());
				for (int j = 0; j < urs.length; j++)
				{
					MUserRoles ur = urs[j];
					if (!ur.isActive())
						continue;
					if (sendInfo (ur.getAD_User_ID(), alert, message.toString()))
						countRecipient++;
				}
			}
		}
		
		m_summary.append(alert.getName()).append(" (Recipients=").append(countRecipient).append(") - ");
		return valid;
	}	//	processAlert
	
	/**
	 * 	Send Email / Notice
	 * 	@param AD_User_ID user
	 *	@param alert alert
	 *	@param message message text
	 *	@return true if sent (or previously sent)
	 */
	private boolean sendInfo(int AD_User_ID, MAlert alert, String message)
	{
		if (m_recipients.contains(AD_User_ID))
			return false;
		m_recipients.add(AD_User_ID);
		//
		boolean success = false;
		MUser to = MUser.get (alert.getCtx(), AD_User_ID);
		String NotificationType = to.getNotificationType();
		if (Util.isEmpty(NotificationType))
			NotificationType = X_AD_User.NOTIFICATIONTYPE_EMail;
		//	Send Mail
		if (X_AD_User.NOTIFICATIONTYPE_EMail.equals(NotificationType)
			|| X_AD_User.NOTIFICATIONTYPE_EMailPlusNotice.equals(NotificationType))
		{
			success = m_client.sendEMail(AD_User_ID, alert.getAlertSubject(), message, null);
			if (!success)
			{
				log.warning("EMail failed: " + to);
				NotificationType = X_AD_User.NOTIFICATIONTYPE_Notice;
			}
		}
		//	Send Note
		if (X_AD_User.NOTIFICATIONTYPE_Notice.equals(NotificationType)
			|| X_AD_User.NOTIFICATIONTYPE_EMailPlusNotice.equals(NotificationType))
		{
			int AD_Message_ID = 1040;	//	AlertNotice
			MNote note = new MNote(alert.getCtx(), AD_Message_ID, AD_User_ID,
				X_AD_Alert.Table_ID, alert.getAD_Alert_ID(), 
				alert.getAlertSubject(), message, null);
			success = note.save();
		}
		return success;
	}	//	sendInfo
	
	/**
	 * 	List Sql Select
	 *	@param sql sql select
	 *	@param trx transaction
	 *	@return list of rows & values
	 *	@throws Exception
	 */
	private String listSqlSelect (String sql, Trx trx) throws Exception
	{
		StringBuffer result = new StringBuffer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Exception error = null;
		int count = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			rs = pstmt.executeQuery ();
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next ())
			{
				result.append("------------------").append(Env.NL);
				for (int col = 1; col <= meta.getColumnCount(); col++)
				{
					result.append(meta.getColumnLabel(col)).append(" = ");
					result.append(rs.getString(col));
					result.append(Env.NL);
				}	//	for all columns
				count++;
			}
			if (result.length() == 0)
				log.fine("No rows selected");
		}
		catch (Exception e)
		{
			if (DB.isOracle() || sql.indexOf(" DBA_Free_Space")==-1)
			{
				log.log(Level.SEVERE, sql, e);
				error = e;
			}
			else
			{
				log.log(Level.WARNING, sql, e);
			}
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//	Error occured
		if (error != null)
			throw new Exception ("(" + sql + ") " + Env.NL 
				+ error.getLocalizedMessage());
		
		if (count > 0)
			result.insert(0, "#" + count + ": ");
		return result.toString();
	}	//	listSqlSelect
	
	
	
	/**
	 * 	Get Server Info
	 *	@return info
	 */
	@Override
	public String getServerInfo()
	{
		return "#" + p_runCount + " - Last=" + m_summary.toString();
	}	//	getServerInfo

	
	/***************************************************************************
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		Compiere.startup(true);
		int AD_AlertProcessor_ID = 1000000;	// 100
		MAlertProcessor model = new MAlertProcessor (Env.getCtx(), AD_AlertProcessor_ID, null);
		AlertProcessor ap = new AlertProcessor(model);
		ap.start();
		
		
	}	//	main
	
}	//	AlertProcessor
