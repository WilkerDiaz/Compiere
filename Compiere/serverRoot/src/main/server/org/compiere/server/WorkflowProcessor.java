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

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.wf.*;


/**
 *	Workflow Processor
 *	
 *  @author Jorg Janke
 *  @version $Id: WorkflowProcessor.java 7648 2009-05-11 18:00:45Z gwu $
 */
public class WorkflowProcessor extends CompiereServer
{

	/** The thread pool */
	private final ExecutorService m_executor;

	/**
	 * 	WorkflowProcessor
	 *	@param model model
	 */
	public WorkflowProcessor (MWorkflowProcessor model)
	{
		super (model, 120);		//	2 minute dalay
		m_model = model;
		m_client = MClient.get(model.getCtx(), model.getAD_Client_ID());
		
		if( m_model.getMaxProcesses() > 0 )
			m_executor = Executors.newFixedThreadPool(m_model.getMaxProcesses());
		else
			m_executor = Executors.newCachedThreadPool();
	}	//	WorkflowProcessor
	
	/**	The Concrete Model			*/
	private	MWorkflowProcessor	m_model = null;
	/**	Last Summary				*/
	private StringBuffer 		m_summary = new StringBuffer();
	/** Client onfo					*/
	private MClient 			m_client = null;



	/**
	 * Return an existing WorkflowProcessor based on ID.
	 * @param AD_WorkflowProcessor_ID If zero, will return the first Workflow Processor found.
	 * @return
	 */
	public static WorkflowProcessor getWorkflowProcessor(int AD_WorkflowProcessor_ID) {

		// find exact match first if an ID is specified
		if (AD_WorkflowProcessor_ID > 0) {
			for (CompiereServer server : CompiereServerMgr.get().getAll()) {
				if (server instanceof WorkflowProcessor) {
					WorkflowProcessor wp = (WorkflowProcessor) server;
					if (wp.m_model.getAD_WorkflowProcessor_ID() == AD_WorkflowProcessor_ID)
						return wp;
				}
			}
		}
		
		// no exact match found; return any Workflow Processor found
		for (CompiereServer server : CompiereServerMgr.get().getAll()) {
			if (server instanceof WorkflowProcessor) {
				return (WorkflowProcessor) server;
			}
		}
		
		// no Workflow Processor found at all; fatal condition
		return null;
	}
	
	
	/**
	 * 	Work
	 */
	@Override
	protected void doWork ()
	{
		m_summary = new StringBuffer();
		
		Trx trx = Trx.get("WorkflowProcessor");
		//
		wakeup(trx);
		dynamicPriority(trx);
		sendAlerts(trx);
		
		trx.commit();
		trx.close();
		//
		int no = m_model.deleteLog();
		m_summary.append("Logs deleted=").append(no);
		//
		MWorkflowProcessorLog pLog = new MWorkflowProcessorLog(m_model, m_summary.toString());
		pLog.setReference("#" + String.valueOf(p_runCount) 
			+ " - " + TimeUtil.formatElapsed(new Timestamp(p_startWork)));
		pLog.save();
	}	//	doWork

	/**
	 * 	Continue Workflow After Sleep
	 * @param trx TODO
	 */
	private void wakeup(final Trx trx)
	{
		String sql = "SELECT * " 
			+ " FROM AD_WF_Process P " 	 
			+ " WHERE Processed='N' " 
			+ " AND WFState='ON'"	//	Not Started
			+ " AND (DateScheduledStart IS NULL OR DateScheduledStart <= SYSDATE)" 
			+ " AND (P.AD_WorkflowProcessor_ID IS NULL OR P.AD_WorkflowProcessor_ID=?)";
		PreparedStatement pstmt = null;
		int count = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt (1, m_model.getAD_WorkflowProcessor_ID());
			final ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				// need to set the context to that of the user/client/org who submitted the request
				final Ctx ctx = new Ctx(getCtx());
				final MWFProcess process = new MWFProcess(ctx, rs, (Trx) null);
				ctx.setAD_Client_ID(process.getAD_Client_ID());
				ctx.setAD_Org_ID(process.getAD_Org_ID());
				ctx.setAD_User_ID(process.getAD_User_ID());
				
				log.fine("Submit Process ID=" + rs.getString("AD_WF_Process_ID"));
				// spawn new threads to run the workflow processes
				m_executor.submit(new Callable<MWFProcess>() {
					@Override
					public MWFProcess call() throws Exception {
						final Trx trx = Trx.get("WorkflowProcessor");
						process.set_Trx(trx);
						try {
							if (process.lock()) {
								process.startWork();
								log.fine("Completed Process ID=" + process.getAD_WF_Process_ID());
								trx.commit();
								return process;
							} else {
								log.fine("Lock Failed Process ID=" + process.getAD_WF_Process_ID());
								return null;
							}
						} catch (Exception e) {
							trx.rollback();
							throw e;
						} finally {
							trx.close();
						}
					}
				});
				count++;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "wakeup", e);
		}
		m_summary.append("Wakeup Workflow Processes #").append(count).append (" - ");
		
		

		sql = "SELECT * "
			+ "FROM AD_WF_Activity a "
			+ "WHERE Processed='N' AND WFState='OS'"	//	suspended
			+ " AND EndWaitTime > SysDate"
			+ " AND AD_Client_ID=?"
			+ " AND EXISTS (SELECT * FROM AD_Workflow wf "
				+ " INNER JOIN AD_WF_Node wfn ON (wf.AD_Workflow_ID=wfn.AD_Workflow_ID) "
				+ "WHERE a.AD_WF_Node_ID=wfn.AD_WF_Node_ID"
				+ " AND wfn.Action='Z'"		//	sleeping
				+ " AND wf.AD_WorkflowProcessor_ID IS NULL OR wf.AD_WorkflowProcessor_ID=?)";
		pstmt = null;
		count = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt (1, m_model.getAD_Client_ID());
			pstmt.setInt (2, m_model.getAD_WorkflowProcessor_ID());
			final ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				final MWFActivity activity = new MWFActivity (getCtx(), rs, (Trx) null);
				// spawn new threads to run the workflow processes
				m_executor.submit(new Runnable() {
					@Override
					public void run() {
						activity.setWFState (StateEngine.STATE_Completed);	
						// saves and calls MWFProcess.checkActivities();
					}
				});
				count++;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "wakeup", e);
		}
		m_summary.append("Wakeup Workflow Activities #").append(count).append (" - ");
	}	//	wakeup
	
	/**
	 * 	Set/Increase Priority dynamically
	 * @param trx TODO
	 */
	private void dynamicPriority(Trx trx)
	{
		//	suspened activities with dynamic priority node
		String sql = "SELECT * "
			+ "FROM AD_WF_Activity a "
			+ "WHERE Processed='N' AND WFState='OS'"	//	suspended
			+ " AND EXISTS (SELECT * FROM AD_Workflow wf"
				+ " INNER JOIN AD_WF_Node wfn ON (wf.AD_Workflow_ID=wfn.AD_Workflow_ID) "
				+ "WHERE a.AD_WF_Node_ID=wfn.AD_WF_Node_ID AND wf.AD_WorkflowProcessor_ID=?"
				+ " AND wfn.DynPriorityUnit IS NOT NULL AND wfn.DynPriorityChange IS NOT NULL)";
		PreparedStatement pstmt = null;
		int count = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, m_model.getAD_WorkflowProcessor_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MWFActivity activity = new MWFActivity (getCtx(), rs, trx);
				if (activity.getDynPriorityStart() == 0)
					activity.setDynPriorityStart(activity.getPriority());
				long ms = System.currentTimeMillis() - activity.getCreated().getTime();
				MWFNode node = activity.getNode();
				int prioDiff = node.calculateDynamicPriority ((int)(ms / 1000));
				activity.setPriority(activity.getDynPriorityStart() + prioDiff);
				activity.save();
				count++;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		m_summary.append("DynPriority #").append(count).append (" - ");
		    
		//	Clean-up
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
	}	//	setPriority
	
	
	/**
	 * 	Send Alerts
	 * @param trx TODO
	 */
	private void sendAlerts(Trx trx)
	{
		//	Alert over Priority
		if (m_model.getAlertOverPriority() > 0)
		{
			String sql = "SELECT * "
				+ "FROM AD_WF_Activity a "
				+ "WHERE Processed='N' AND WFState='OS'"	//	suspended
				+ " AND Priority >= ?"				//	##1
				+ " AND (DateLastAlert IS NULL";
			if (m_model.getRemindDays() > 0)
				sql += " OR (DateLastAlert+" + m_model.getRemindDays() 
					+ ") < SysDate";
			sql += ") AND EXISTS (SELECT * FROM AD_Workflow wf "
					+ " INNER JOIN AD_WF_Node wfn ON (wf.AD_Workflow_ID=wfn.AD_Workflow_ID) "
					+ "WHERE a.AD_WF_Node_ID=wfn.AD_WF_Node_ID"
					+ " AND wf.AD_WorkflowProcessor_ID IS NULL OR wf.AD_WorkflowProcessor_ID=?)";
			int count = 0;
			int countEMails = 0;
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, trx);
				pstmt.setInt (1, m_model.getAlertOverPriority());
				pstmt.setInt (2, m_model.getAD_WorkflowProcessor_ID());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					MWFActivity activity = new MWFActivity (getCtx(), rs, trx);
					boolean escalate = activity.getDateLastAlert() != null; 
					countEMails += sendEmail (activity, "ActivityOverPriority",
						escalate, true);
					activity.setDateLastAlert(new Timestamp(System.currentTimeMillis()));
					activity.save();
					count++;
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, "(Priority) - " + sql, e);
			}
			m_summary.append("OverPriority #").append(count);
			if (countEMails > 0)
				m_summary.append(" (").append(countEMails).append(" EMail)");
			m_summary.append (" - ");
		}	//	Alert over Priority

		/**
		 * 	Over End Wait
		 */
		String sql = "SELECT * "
			+ "FROM AD_WF_Activity a "
			+ "WHERE Processed='N' AND WFState='OS'"	//	suspended
			+ " AND EndWaitTime > SysDate"
			+ " AND (DateLastAlert IS NULL";
		if (m_model.getRemindDays() > 0)
			sql += " OR (DateLastAlert+" + m_model.getRemindDays() 
				+ ") < SysDate";
		sql += ") AND EXISTS (SELECT * FROM AD_Workflow wf "
				+ " INNER JOIN AD_WF_Node wfn ON (wf.AD_Workflow_ID=wfn.AD_Workflow_ID) "
				+ "WHERE a.AD_WF_Node_ID=wfn.AD_WF_Node_ID"
				+ " AND wfn.Action<>'Z'"	//	not sleeping
				+ " AND wf.AD_WorkflowProcessor_ID IS NULL OR wf.AD_WorkflowProcessor_ID=?)";
		PreparedStatement pstmt = null;
		int count = 0;
		int countEMails = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, m_model.getAD_WorkflowProcessor_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MWFActivity activity = new MWFActivity (getCtx(), rs, trx);
				boolean escalate = activity.getDateLastAlert() != null; 
				countEMails += sendEmail (activity, "ActivityEndWaitTime", 
					escalate, false);
				activity.setDateLastAlert(new Timestamp(System.currentTimeMillis()));
				activity.save();
				count++;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "(EndWaitTime) - " + sql, e);
		}
		m_summary.append("EndWaitTime #").append(count);
		if (countEMails > 0)
			m_summary.append(" (").append(countEMails).append(" EMail)");
		m_summary.append (" - ");

		/**
		 *  Send inactivity alerts
		 */
		if (m_model.getInactivityAlertDays() > 0)
		{
			sql = "SELECT * "
				+ "FROM AD_WF_Activity a "
				+ "WHERE Processed='N' AND WFState='OS'"	//	suspended
				+ " AND (Updated+" + m_model.getInactivityAlertDays() + ") < SysDate"
				+ " AND (DateLastAlert IS NULL";
			if (m_model.getRemindDays() > 0)
				sql += " OR (DateLastAlert+" + m_model.getRemindDays() 
					+ ") < SysDate";
			sql += ") AND EXISTS (SELECT * FROM AD_Workflow wf "
					+ " INNER JOIN AD_WF_Node wfn ON (wf.AD_Workflow_ID=wfn.AD_Workflow_ID) "
					+ "WHERE a.AD_WF_Node_ID=wfn.AD_WF_Node_ID"
					+ " AND wf.AD_WorkflowProcessor_ID IS NULL OR wf.AD_WorkflowProcessor_ID=?)";
			count = 0;
			countEMails = 0;
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt (1, m_model.getAD_WorkflowProcessor_ID());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					MWFActivity activity = new MWFActivity (getCtx(), rs, null);
					boolean escalate = activity.getDateLastAlert() != null; 
					countEMails += sendEmail (activity, "ActivityInactivity",
						escalate, false);
					activity.setDateLastAlert(new Timestamp(System.currentTimeMillis()));
					activity.save();
					count++;
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, "(Inactivity): " + sql, e);
			}
			m_summary.append("Inactivity #").append(count);
			if (countEMails > 0)
				m_summary.append(" (").append(countEMails).append(" EMail)");
			m_summary.append (" - ");
		}	//	Inactivity

		
		//	Clean-up
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
	}	//	sendAlerts
	
	/**
	 *  Send Alert EMail
	 *  @param activity activity
	 *  @param AD_Message message
	 *  @param toProcess true if to process owner
	 *  @param toSupervisor true if to Supervisor
	 * 	@return number of mails sent
	 */
	private int sendEmail (MWFActivity activity, String AD_Message,
		boolean toProcess, boolean toSupervisor)
	{
		if (m_client == null || m_client.getAD_Client_ID() != activity.getAD_Client_ID())
			m_client = MClient.get(getCtx(), activity.getAD_Client_ID());
			
		MWFProcess process = new MWFProcess (getCtx(), activity.getAD_WF_Process_ID(), null);

		String subjectVar = activity.getNode().getName();
		String message = activity.getTextMsg();
		if (message == null || message.length() == 0)
			message = process.getTextMsg();
		File pdf = null; 
		PO po = activity.getPO();
		if (po instanceof DocAction)
		{
			message = ((DocAction)po).getDocumentInfo() + "\n" + message;
			pdf = ((DocAction)po).createPDF();
		}
		
		//  Inactivity Alert: Workflow Activity {0}
		String subject = Msg.getMsg(m_client.getAD_Language(), AD_Message, 
			new Object[] {subjectVar});
		
		//	Prevent duplicates
		ArrayList<Integer> list = new ArrayList<Integer>();
		int counter = 0;
		
		//	To Activity Owner
		if (m_client.sendEMail(activity.getAD_User_ID(), subject, message, pdf))
			counter++;
		list.add (new Integer(activity.getAD_User_ID()));

		//	To Process Owner
		if (toProcess
			&& process.getAD_User_ID() != activity.getAD_User_ID())
		{
			if (m_client.sendEMail(process.getAD_User_ID(), subject, message, pdf))
				counter++;
			list.add (new Integer(process.getAD_User_ID()));
		}
		
		//	To Activity Responsible
		MWFResponsible responsible = MWFResponsible.get(getCtx(), activity.getAD_WF_Responsible_ID());
		counter += sendAlertToResponsible (responsible, list, process,
			subject, message, pdf);
		
		//	To Process Responsible
		if (toProcess
			&& process.getAD_WF_Responsible_ID() != activity.getAD_WF_Responsible_ID())
		{
			responsible = MWFResponsible.get(getCtx(), process.getAD_WF_Responsible_ID());
			counter += sendAlertToResponsible (responsible, list, process,
				subject, message, pdf);
		}
		
		//	Processor SuperVisor
		if (toSupervisor 
			&& m_model.getSupervisor_ID() != 0
			&& !list.contains(new Integer(m_model.getSupervisor_ID())))
		{
			if (m_client.sendEMail(m_model.getSupervisor_ID(), subject, message, pdf))
				counter++;
			list.add (new Integer(m_model.getSupervisor_ID()));
		}

		return counter;
	}   //  sendAlert
	
	/**
	 * 	Send Alert To Responsible
	 *	@param responsible responsible
	 *	@param list list of already sent users
	 *	@param process process
	 *	@param subject subject
	 *	@param message message
	 *	@param pdf optional pdf
	 *	@return number of mail sent
	 */
	private int sendAlertToResponsible (MWFResponsible responsible, 
		ArrayList<Integer> list, MWFProcess process,
		String subject, String message, File pdf)
	{
		int counter = 0;
		if (responsible.isInvoker())
			;
		//	Human
		else if (X_AD_WF_Responsible.RESPONSIBLETYPE_Human.equals(responsible.getResponsibleType())
			&& responsible.getAD_User_ID() != 0
			&& !list.contains(new Integer(responsible.getAD_User_ID())))
		{
			if (m_client.sendEMail(responsible.getAD_User_ID(), subject, message, pdf))
				counter++;
			list.add (new Integer(responsible.getAD_User_ID()));
		}
		//	Org of the Document
		else if (X_AD_WF_Responsible.RESPONSIBLETYPE_Organization.equals(responsible.getResponsibleType()))
		{
			PO document = process.getPO();
			if (document != null)
			{
				MOrgInfo org = MOrgInfo.get (getCtx(), document.getAD_Org_ID(), null);
				if (org.getSupervisor_ID() != 0
					&& !list.contains(new Integer(org.getSupervisor_ID())))
				{
					if (m_client.sendEMail(org.getSupervisor_ID(), subject, message, pdf))
						counter++;
					list.add (new Integer(org.getSupervisor_ID()));
				}
			}
		}
		//	Role
		else if (X_AD_WF_Responsible.RESPONSIBLETYPE_Role.equals(responsible.getResponsibleType())
			&& responsible.getAD_Role_ID() != 0)
		{
			MUserRoles[] userRoles = MUserRoles.getOfRole(getCtx(), responsible.getAD_Role_ID());
			for (int i = 0; i < userRoles.length; i++)
			{
				MUserRoles roles = userRoles[i];
				if (!roles.isActive())
					continue;
				int AD_User_ID = roles.getAD_User_ID();
				if (!list.contains(new Integer(AD_User_ID)))
				{
					if (m_client.sendEMail(AD_User_ID, subject, message, pdf))
						counter++;
					list.add (new Integer(AD_User_ID));
				}
			}
		}
		return counter;
	}	//	sendAlertToResponsible
	
	
	/**
	 * 	Get Server Info
	 *	@return info
	 */
	@Override
	public String getServerInfo()
	{
		return "#" + p_runCount + " - Last=" + m_summary.toString();
	}	//	getServerInfo


	/**
	 * Get the associated thread pool
	 * @return
	 */
	public ExecutorService getExecutor() {
		return m_executor;
	}


	/**************************************************************************
	 * 	Test
	 *	@param args
	 */
	public static void main(String[] args)
	{
		Compiere.startup (true);
		// CLogMgt.setLevel (Level.FINE);
		

		MWorkflowProcessor[] processors = MWorkflowProcessor.getActive(Env.getCtx());
		for( MWorkflowProcessor proc : processors ){
			WorkflowProcessor wp = new WorkflowProcessor(proc);
			wp.runNow();
		}
		
		
	}	//	main

	
	
}	//	WorkflowProcessor
