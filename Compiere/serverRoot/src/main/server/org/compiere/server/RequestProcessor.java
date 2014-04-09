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

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Request Processor
 *
 *  @author Jorg Janke
 *  @version $Id: RequestProcessor.java 8778 2010-05-19 19:00:10Z ragrawal $
 */
public class RequestProcessor extends CompiereServer
{
	/**
	 * 	RequestProcessor
	 *	@param model model
	 */
	public RequestProcessor (MRequestProcessor model)
	{
		super (model, 60);	//	1 minute delay
		m_model = model;
		m_client = MClient.get(model.getCtx(), model.getAD_Client_ID());
	}	//	RequestProcessor

	/**	The Concrete Model			*/
	private MRequestProcessor	m_model = null;
	/**	Last Summary				*/
	private StringBuffer 		m_summary = new StringBuffer();
	/** Client info					*/
	private MClient 			m_client = null;

	/**************************************************************************
	 * 	Do the actual Work
	 */
	@Override
	protected void doWork()
	{
		m_summary = new StringBuffer();
		//
		processEMail();
		findSalesRep ();
		processRequests ();
		processStatus();
		processECR();
		//
		int no = m_model.deleteLog();
		m_summary.append("Logs deleted=").append(no);
		//
		MRequestProcessorLog pLog = new MRequestProcessorLog(m_model, m_summary.toString());
		pLog.setReference("#" + String.valueOf(p_runCount)
			+ " - " + TimeUtil.formatElapsed(new Timestamp(p_startWork)));
		pLog.save();
	}	//	doWork


	/**************************************************************************
	 *  Process requests.
	 *  Scheduled - are they due?
	 */
	private void processRequests ()
	{
		/**
		 *  Due Requests (Scheduled -> Due)
		 */
		String sql = "SELECT * FROM R_Request "
			+ "WHERE DueType= ? AND Processed='N'"
			+ " AND DateNextAction > SysDate"
			+ " AND AD_Client_ID=?";
		if (m_model.getR_RequestType_ID() != 0)
			sql += " AND R_RequestType_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		int countEMails = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, X_R_Request.DUETYPE_Scheduled );
			pstmt.setInt (2, m_model.getAD_Client_ID());
			if (m_model.getR_RequestType_ID() != 0)
				pstmt.setInt(3, m_model.getR_RequestType_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MRequest request = new MRequest (getCtx(), rs, null);
				request.setDueType();
				if (request.isDue())
				{
					if (request.getRequestType().isEMailWhenDue())
					{
						if (sendEmail (request, "RequestDue"))
						{
							request.setDateLastAlert();
							countEMails++;
						}
					}
					request.save();
					count++;
				}
			}
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

		m_summary.append("New Due #").append(count);
		if (countEMails > 0)
			m_summary.append(" (").append(countEMails).append(" EMail)");
		m_summary.append (" - ");

		/**
		 *  Overdue Requests.
		 *  Due Requests - are they overdue? (Send EMail)
		 */
		rs = null;
		pstmt = null;
		sql = "SELECT * FROM R_Request r "
			+ "WHERE r.DueType= ? AND r.Processed='N'"
			+ " AND AD_Client_ID=?"
			+ " AND EXISTS (SELECT * FROM R_RequestType rt "
				+ "WHERE r.R_RequestType_ID=rt.R_RequestType_ID"
				+ " AND addDays(r.DateNextAction,rt.DueDateTolerance) > SysDate)";
		if (m_model.getR_RequestType_ID() != 0)
			sql += " AND r.R_RequestType_ID=?";
		count = 0;
		countEMails = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, X_R_Request.DUETYPE_Due);
			pstmt.setInt (2, m_model.getAD_Client_ID());
			if (m_model.getR_RequestType_ID() != 0)
				pstmt.setInt(3, m_model.getR_RequestType_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MRequest request = new MRequest (getCtx(), rs, null);
				request.setDueType();
				if (request.isOverdue())
				{
					if (request.getRequestType().isEMailWhenOverdue()
						&& !TimeUtil.isSameDay(request.getDateLastAlert(), null))
					{
						if (sendEmail (request, "RequestDue"))
						{
							request.setDateLastAlert();
							countEMails++;
						}
					}
					request.save();
					count++;
				}
			}
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
		
		m_summary.append("New Overdue #").append(count);
		if (countEMails > 0)
			m_summary.append(" (").append(countEMails).append(" EMail)");
		m_summary.append (" - ");

		/**
		 *  Send (over)due alerts
		 */
		if (m_model.getOverdueAlertDays() > 0)
		{
			rs = null;
			pstmt = null;
			
			sql = "SELECT * FROM R_Request "
				+ "WHERE Processed='N'"
				+ " AND AD_Client_ID=?"
				+ " AND addDays(DateNextAction,?) > SysDate "
				+ " AND (DateLastAlert IS NULL";
			if (m_model.getRemindDays() > 0)
				sql += " OR addDays(DateLastAlert, ?) > SysDate ";
			sql += ")";
			if (m_model.getR_RequestType_ID() != 0)
				sql += " AND R_RequestType_ID=?";
			count = 0;
			countEMails = 0;
			try
			{
				int index = 1;
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt(index++, m_model.getAD_Client_ID());
				pstmt.setInt(index++,m_model.getOverdueAlertDays());
				if (m_model.getRemindDays() > 0)
					pstmt.setInt(index++,m_model.getRemindDays());
				if (m_model.getR_RequestType_ID() != 0)
					pstmt.setInt(index++, m_model.getR_RequestType_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					MRequest request = new MRequest (getCtx(), rs, null);
					request.setDueType();
					if (request.getRequestType().isEMailWhenOverdue()
						&& ((request.getDateLastAlert() == null)
							|| !TimeUtil.isSameDay(request.getDateLastAlert(), null)))
					{
						if (sendEmail (request, "RequestAlert"))
						{
							request.setDateLastAlert();
							countEMails++;
						}
					}
					request.save();
					count++;
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			
			m_summary.append("Alerts #").append(count);
			if (countEMails > 0)
				m_summary.append(" (").append(countEMails).append(" EMail)");
			m_summary.append (" - ");
		}	//	Overdue

		/**
		 *  Escalate if Date Next Action + Overdue Assign Days > SysDate
		 */
		if (m_model.getOverdueAssignDays() > 0)
		{
			rs = null;
			pstmt = null;
			
			sql = "SELECT * FROM R_Request "
				+ "WHERE Processed='N'"
				+ " AND AD_Client_ID=?"
				+ " AND IsEscalated='N'"
				+ " AND addDays(DateNextAction, ? ) > SysDate";
			if (m_model.getR_RequestType_ID() != 0)
				sql += " AND R_RequestType_ID=?";
			count = 0;
			countEMails = 0;
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt(1, m_model.getAD_Client_ID());
				pstmt.setInt(2, m_model.getOverdueAssignDays());
				if (m_model.getR_RequestType_ID() != 0)
					pstmt.setInt(3, m_model.getR_RequestType_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					MRequest request = new MRequest (getCtx(), rs, null);
					if (escalate(request))
						count++;
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			m_summary.append("Escalated #").append(count).append(" - ");
		}	//	Esacalate

		/**
		 *  Send Inactivity alerts
		 */
		if (m_model.getInactivityAlertDays() > 0)
		{
			sql = "SELECT * FROM R_Request r "
				+ "WHERE r.Processed='N'"
				+ " AND r.AD_Client_ID=?"
				//	Nothing happening for x days
				+ " AND addDays(r.Updated,?) < SysDate "
				+ " AND (r.DateLastAlert IS NULL";
			if (m_model.getRemindDays() > 0)
				sql += " OR addDays(r.DateLastAlert,?) < SysDate ";
			sql += ")";
			//	Next Date & Updated over due date tolerance
			sql += " AND EXISTS (SELECT * FROM R_RequestType rt "
				+ "WHERE r.R_RequestType_ID=rt.R_RequestType_ID"
				+ " AND addDays(COALESCE(r.DateNextAction,Updated),rt.DueDateTolerance) > SysDate)";
			if (m_model.getR_RequestType_ID() != 0)
				sql += " AND r.R_RequestType_ID=?";
			count = 0;
			countEMails = 0;
			try
			{
				int index = 1;
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt(index++, m_model.getAD_Client_ID());
				pstmt.setInt(index++, m_model.getInactivityAlertDays());
				if (m_model.getRemindDays() > 0)
					pstmt.setInt(index++, m_model.getRemindDays());
				if (m_model.getR_RequestType_ID() != 0)
					pstmt.setInt(index++, m_model.getR_RequestType_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					MRequest request = new MRequest (getCtx(), rs, null);
					request.setDueType();
					//	only once per day
					if (!TimeUtil.isSameDay(request.getDateLastAlert(), null))
					{
						if (sendEmail (request, "RequestInactive"))
						{
							request.setDateLastAlert();
							countEMails++;
						}
						request.save();
						count++;
					}
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			m_summary.append("Inactivity #").append(count);
			if (countEMails > 0)
				m_summary.append(" (").append(countEMails).append(" EMail)");
			m_summary.append (" - ");
		}	//	Inactivity

		//
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
	}	//  processRequests

	/**
	 *  Send Alert EMail
	 *  @param request request
	 *  @param AD_Message message
	 *  @return true if sent
	 */
	private boolean sendEmail (MRequest request, String AD_Message)
	{
		//  Alert: Request {0} overdue
		String subject = Msg.getMsg(m_client.getAD_Language(), AD_Message,
			new String[] {request.getDocumentNo()});
		return m_client.sendEMail(request.getSalesRep_ID(),
			subject, request.getSummary(), request.createPDF());
	}   //  sendAlert

	/**
	 *  Escalate
	 *  @param request request
	 * 	@return true if saved
	 */
	private boolean escalate (MRequest request)
	{
		//  Get Supervisor
		MUser supervisor = request.getSalesRep();	//	self
		int supervisor_ID = request.getSalesRep().getSupervisor_ID();
		if ((supervisor_ID == 0) && (m_model.getSupervisor_ID() != 0))
			supervisor_ID = m_model.getSupervisor_ID();
		if ((supervisor_ID != 0) && (supervisor_ID != request.getAD_User_ID()))
			supervisor = MUser.get(getCtx(), supervisor_ID);

		//  Escalated: Request {0} to {1}
		String subject = Msg.getMsg(m_client.getAD_Language(), "RequestEscalate",
			new String[] {request.getDocumentNo(), supervisor.getName()});
		String to = request.getSalesRep().getEMail();
		if ((to == null) || (to.length() == 0))
			log.warning("SalesRep has no EMail - " + request.getSalesRep());
		else
			m_client.sendEMail(request.getSalesRep_ID(),
				subject, request.getSummary(), request.createPDF());

		//	Not the same - send mail to supervisor
		if (request.getSalesRep_ID() != supervisor.getAD_User_ID())
		{
			to = supervisor.getEMail();
			if ((to == null) || (to.length() == 0))
				log.warning("Supervisor has no EMail - " + supervisor);
			else
				m_client.sendEMail(supervisor.getAD_User_ID(),
					subject, request.getSummary(), request.createPDF());
		}

		//  ----------------
		request.setDueType();
		request.setIsEscalated(true);
		request.setResult(subject);
		return request.save();
	}   //  escalate


	/**************************************************************************
	 * 	Process Request Status
	 */
	private void processStatus()
	{
		int count = 0;
		//	Requests with status with after timeout
		String sql = "SELECT * FROM R_Request r WHERE EXISTS ("
			+ "SELECT * FROM R_Status s "
			+ "WHERE r.R_Status_ID=s.R_Status_ID"
			+ " AND s.TimeoutDays > 0 AND s.Next_Status_ID > 0"
			+ " AND addDays(r.Updated,s.TimeoutDays) < SysDate "
			+ ") "
			+ "ORDER BY R_Status_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MStatus status = null;
		MStatus next = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MRequest r = new MRequest(getCtx(), rs, null);
				//	Get/Check Status
				if ((status == null) || (status.getR_Status_ID() != r.getR_Status_ID()))
					status = MStatus.get(getCtx(), r.getR_Status_ID());
				if ((status.getTimeoutDays() <= 0)
					|| (status.getNext_Status_ID() == 0))
					continue;
				//	Next Status
				if ((next == null) || (next.getR_Status_ID() != status.getNext_Status_ID()))
					next = MStatus.get(getCtx(), status.getNext_Status_ID());
				//
				String result = Msg.getMsg(getCtx(), "RequestStatusTimeout")
					+ ": " + status.getName() + " -> " + next.getName();
				r.setResult(result);
				r.setR_Status_ID(status.getNext_Status_ID());
				if (r.save())
					count++;
			}
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
		m_summary.append("Status Timeout #").append(count)
			.append(" - ");
	}	//	processStatus

	/**
	 * 	Create ECR
	 */
	private void processECR()
	{
		//	Get Requests with Request Type-AutoChangeRequest and Group with info
		String sql = "SELECT * FROM R_Request r "
			+ "WHERE M_ChangeRequest_ID IS NULL"
			+ " AND EXISTS ("
				+ "SELECT * FROM R_RequestType rt "
				+ "WHERE rt.R_RequestType_ID=r.R_RequestType_ID"
				+ " AND rt.IsAutoChangeRequest='Y')"
			+ "AND EXISTS ("
				+ "SELECT * FROM R_Group g "
				+ "WHERE g.R_Group_ID=r.R_Group_ID"
				+ " AND (g.M_BOM_ID IS NOT NULL OR g.M_ChangeNotice_ID IS NOT NULL)	)";
		//
		int count = 0;
		int failure = 0;
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MRequest r = new MRequest (getCtx(), rs, null);
				MGroup rg = MGroup.get(getCtx(), r.getR_Group_ID());
				MChangeRequest ecr = new MChangeRequest (r, rg);
				if (r.save())
				{
					r.setM_ChangeRequest_ID(ecr.getM_ChangeRequest_ID());
					if (r.save())
						count++;
					else
						failure++;
				}
				else
					failure++;
			}
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
		m_summary.append("Auto Change Request #").append(count);
		if (failure > 0)
			m_summary.append("(fail=").append(failure).append(")");
		m_summary.append(" - ");
	}	//	processECR


	/**************************************************************************
	 *	Create Reauest / Updates from EMail
	 */
	private void processEMail ()
	{
	//	m_summary.append("Mail #").append(count)
	//		.append(" - ");
	}   //  processEMail


	/**************************************************************************
	 * 	Allocate Sales Rep
	 */
	private void findSalesRep ()
	{
		int changed = 0;
		int notFound = 0;
		Ctx ctx = new Ctx();
		//
		String sql = "SELECT * FROM R_Request "
			+ "WHERE AD_Client_ID=?"
			+ " AND SalesRep_ID=0 AND Processed='N'";
		if (m_model.getR_RequestType_ID() != 0)
			sql += " AND R_RequestType_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, m_model.getAD_Client_ID());
			if (m_model.getR_RequestType_ID() != 0)
				pstmt.setInt(2, m_model.getR_RequestType_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MRequest request = new MRequest (ctx, rs, null);
				if (request.getSalesRep_ID() != 0)
					continue;
				int SalesRep_ID = findSalesRep(request);
				if (SalesRep_ID != 0)
				{
					request.setSalesRep_ID(SalesRep_ID);
					request.save();
					changed++;
				}
				else
					notFound++;
			}
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		if ((changed == 0) && (notFound == 0))
			m_summary.append("No unallocated Requests");
		else
			m_summary.append("Allocated SalesRep=").append(changed);
		if (notFound > 0)
			m_summary.append(",Not=").append(notFound);
		m_summary.append(" - ");
	}	//	findSalesRep

	/**
	 *  Find SalesRep/User based on Request Type and Question.
	 *  @param request request
	 *  @return SalesRep_ID user
	 */
	private int findSalesRep (MRequest request)
	{
		String QText = request.getSummary();
		if (QText == null)
			QText = "";
		else
			QText = QText.toUpperCase();
		//
		MRequestProcessorRoute[] routes = m_model.getRoutes(false);
		for (int i = 0; i < routes.length; i++)
		{
			MRequestProcessorRoute route = routes[i];

			//	Match first on Request Type
			if ((request.getR_RequestType_ID() == route.getR_RequestType_ID())
				&& (route.getR_RequestType_ID() != 0))
				return route.getAD_User_ID();

			//	Match on element of keyword
			String keyword = route.getKeyword();
			if (keyword != null)
			{
				StringTokenizer st = new StringTokenizer(keyword.toUpperCase(), " ,;\t\n\r\f");
				while (st.hasMoreElements())
				{
					if (QText.indexOf(st.nextToken()) != -1)
						return route.getAD_User_ID();
				}
			}
		}	//	for all routes

		return m_model.getSupervisor_ID();
	}   //  findSalesRep

	/**
	 * 	Get Server Info
	 *	@return info
	 */
	@Override
	public String getServerInfo()
	{
		return "#" + p_runCount + " - Last=" + m_summary.toString();
	}	//	getServerInfo

}	//	RequestProcessor
