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

import org.compiere.common.CompiereStateException;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 * Workflow Process
 * 
 * @author Jorg Janke
 * @version $Id: MWFProcess.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MWFProcess extends X_AD_WF_Process {
    /** Logger for class MWFProcess */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MWFProcess.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Standard Constructor
	 * 
	 * @param ctx
	 *            context
	 * @param AD_WF_Process_ID
	 *            process
	 * @param trx
	 *            transaction
	 */
	public MWFProcess(Ctx ctx, int AD_WF_Process_ID, Trx trx) {
		super(ctx, AD_WF_Process_ID, trx);
		if (AD_WF_Process_ID == 0)
			throw new IllegalArgumentException(
			"Cannot create new WF Process directly");
		m_state = new StateEngine(getWFState());

	} // MWFProcess

	/**
	 * Load Constructor
	 * 
	 * @param ctx
	 *            context
	 * @param rs
	 *            result set
	 * @param trx
	 *            transaction
	 */
	public MWFProcess(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
		m_state = new StateEngine(getWFState());

	} // MWFProcess

	/**
	 * New Constructor
	 * 
	 * @param wf
	 *            workflow
	 * @param pi
	 *            Process Info (Record_ID)
	 * @throws Exception
	 */
	public MWFProcess(MWorkflow wf, ProcessInfo pi) {
		super(wf.getCtx(), 0, wf.get_Trx());

		if (!TimeUtil.isValid(wf.getValidFrom(), wf.getValidTo()))
			throw new CompiereStateException("Workflow not valid");
		m_wf = wf;
		setAD_Workflow_ID(wf.getAD_Workflow_ID());
		setPriority(wf.getPriority());
		super.setWFState(WFSTATE_NotStarted);

		// Document
		setAD_Table_ID(wf.getAD_Table_ID());
		setRecord_ID(pi.getRecord_ID());
		setDateScheduledStart(pi.getDateScheduledStart());

		// Workflow Processor ID
		if (pi.isBatch()) {
			setAD_WorkflowProcessor_ID(wf.getBackground_WFProcessor_ID());
		} else {
			setAD_WorkflowProcessor_ID(wf.getAD_WorkflowProcessor_ID());
		}

		if (getPO() == null) {
			setTextMsg("No PO with ID=" + pi.getRecord_ID());
			super.setWFState(WFSTATE_Terminated);
		} else {
			setTextMsg(getPO());
			String docAction = m_po.get_ValueAsString("DocAction");
			if (X_Ref__Document_Action.isValid(docAction))
				setRequestDocAction(docAction);
			setRequestDocumentNo(m_po.get_ValueAsString("DocumentNo"));
		}
		// Responsible/User
		if (wf.getAD_WF_Responsible_ID() == 0)
			setAD_WF_Responsible_ID();
		else
			setAD_WF_Responsible_ID(wf.getAD_WF_Responsible_ID());
		setUser_ID(pi.getAD_User_ID()); // user starting
		//
		m_state = new StateEngine(getWFState());
		setProcessed(false);
		// Lock Entity
	} // MWFProcess

	public boolean lockDocument() {
		getPO();
		if (m_po != null) {
			return m_po.lock();
		}
		return true;
	}

	public boolean unlockDocument() {
		getPO();
		if (m_po != null)
			return m_po.unlock(get_Trx());
		return true;
	}

	/** State Machine */
	private StateEngine m_state = null;
	/** Activities */
	private MWFActivity[] m_activities = null;
	/** Workflow */
	private MWorkflow m_wf = null;
	/** Persistent Object */
	private PO m_po = null;
	/** Message from Activity */
	private String m_processMsg = null;

	/**
	 * Get active Activities of Process
	 * 
	 * @param requery
	 *            if true requery
	 * @param onlyActive
	 *            only active activities
	 * @return array of activities
	 */
	public MWFActivity[] getActivities(boolean requery, boolean onlyActive) {
		if (!requery && (m_activities != null))
			return m_activities;
		//
		ArrayList<MWFActivity> list = new ArrayList<MWFActivity>();
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM AD_WF_Activity WHERE AD_WF_Process_ID=?";
		if (onlyActive)
			sql += " AND Processed='N'";
		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getAD_WF_Process_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MWFActivity(getCtx(), rs, get_Trx()));
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
		}
		m_activities = new MWFActivity[list.size()];
		list.toArray(m_activities);
		return m_activities;
	} // getActivities

	/**
	 * Get State
	 * 
	 * @return state
	 */
	public StateEngine getState() {
		return m_state;
	} // getState

	/**
	 * Get Action Options
	 * 
	 * @return array of valid actions
	 */
	public String[] getActionOptions() {
		return m_state.getActionOptions();
	} // getActionOptions

	/**
	 * Set Process State and update Actions
	 * 
	 * @param WFState
	 */
	@Override
	public void setWFState(String WFState) {
		if (m_state == null)
			m_state = new StateEngine(getWFState());
		if (m_state.isClosed())
			return;
		if (getWFState().equals(WFState))
			return;
		//
		if (m_state.isValidNewState(WFState)) {
			log.fine(toString() + " => " + WFState);
			super.setWFState(WFState);
			m_state = new StateEngine(getWFState());
			if (m_state.isClosed()) {
				setProcessed(true);
				unlockDocument();
			}
			save();
			// Force close to all Activities
			if (m_state.isClosed()) {
				MWFActivity[] activities = getActivities(true, true); // requery
				// only
				// active
				for (int i = 0; i < activities.length; i++) {
					if (!activities[i].isClosed()) {
						activities[i].setTextMsg("Process:" + WFState);
						activities[i].setWFState(WFState);
					}
					if (!activities[i].isProcessed())
						activities[i].setProcessed(true);
					activities[i].save();
				}
			} // closed
		} else
			log.log(Level.SEVERE, "Ignored Invalid Transformation "
					+ toString() + " => " + WFState);
	} // setWFState

	/**************************************************************************
	 * Check Status of Activities. - update Process if required - start new
	 * activity
	 * 
	 * @param trx
	 *            transaction
	 */
	public void checkActivities() {
		log.info(toString());
		if (m_state.isClosed())
			return;
		//
		MWFActivity[] activities = getActivities(true, true); // requery active
		String closedState = null;
		boolean suspended = false;
		boolean running = false;
		for (MWFActivity activity : activities) {
			StateEngine activityState = activity.getState();

			// Completed - Start Next
			if (activityState.isCompleted()) {
				if (startNext(activity, activities))
					continue;
			}
			//
			String activityWFState = activity.getWFState();
			if (activityState.isClosed()) {
				// eliminate from active processed
				activity.setProcessed(true);
				activity.save();
				//
				if (closedState == null)
					closedState = activityWFState;
				else if (!closedState.equals(activityState.toString())) {
					// Overwrite if terminated
					if (WFSTATE_Terminated.equals(activityState.toString()))
						closedState = activityWFState;
					// Overwrite if activity aborted and no other terminated
					else if (WFSTATE_Aborted.equals(activityState.toString())
							&& !WFSTATE_Terminated.equals(closedState))
						closedState = activityWFState;
				}
			} else // not closed
			{
				closedState = null; // all need to be closed
				if (activityState.isSuspended())
					suspended = true;
				if (activityState.isRunning())
					running = true;
			}
		} // for all activities
		if (activities.length == 0) {
			setTextMsg("No Active Processed found");
			closedState = WFSTATE_Terminated;
		}
		
		if (closedState != null) {
			setWFState(closedState);
			unlockDocument();
			unlock(get_Trx());
			setDateFinished(new Timestamp(System.currentTimeMillis()));
			save();
		} else if (suspended) {
			setWFState(WFSTATE_Suspended);
			// AGREGADO GMARQUES - Para no dejar el documento bloqueado en caso de espera
			unlockDocument();
			unlock(get_Trx());
			save();
			// FIN GMARQUES
		} else if (running)
			setWFState(WFSTATE_Running);
	} // checkActivities

	/**
	 * Start Next Activity
	 * 
	 * @param last
	 *            last activity
	 * @param activities
	 *            all activities
	 * @return true if there is a next activity
	 */
	private boolean startNext(MWFActivity last, MWFActivity[] activities) {
		log.config("Last=" + last);
		// transitions from the last processed node
		MWFNodeNext[] transitions = getWorkflow().getNodeNexts(
				last.getAD_WF_Node_ID(), last.getAD_Client_ID());
		if ((transitions == null) || (transitions.length == 0)) {
			log.config("none");
			return false; // done
		}
		
		// We need to wait for last activity
		if (X_AD_WF_Node.JOINELEMENT_AND
				.equals(last.getNode().getJoinElement())) {
			// get previous nodes
			// check if all have closed activities
			// return false for all but the last
		}
		// eliminate from active processed
		last.setProcessed(true);
		last.save();

		// Start next activity
		String split = last.getNode().getSplitElement();
		int countOfInvalidTransitions = 0;
		for (int i = 0; i < transitions.length; i++) {
			// Is this a valid transition?
			if (!transitions[i].isValidFor(last))
			{
				countOfInvalidTransitions++;
				continue;
			}
			// Start new Activity in same thread
			MWFActivity activity = new MWFActivity(this, transitions[i]
			                                                         .getAD_WF_Next_ID());
			activity.run(get_Trx());

			// only the first valid if XOR
			if (X_AD_WF_Node.SPLITELEMENT_XOR.equals(split))
				return true;
		} // for all transitions
		if(countOfInvalidTransitions == transitions.length)//no transition is valid - return false, there is no next activity.
			return false;		
		return true;
	} // startNext

	/**************************************************************************
	 * Set Workflow Responsible. Searches for a Invoker.
	 */
	public void setAD_WF_Responsible_ID() {
		int AD_WF_Responsible_ID = QueryUtil
		.getSQLValue(
				null,
				MRole
				.getDefault(getCtx(), false)
				.addAccessSQL(
						"SELECT AD_WF_Responsible_ID FROM AD_WF_Responsible "
						+ "WHERE ResponsibleType='H' AND COALESCE(AD_User_ID,0)=0 "
						+ "ORDER BY AD_Client_ID DESC",
						"AD_WF_Responsible",
						MRole.SQL_NOTQUALIFIED, MRole.SQL_RO));
		setAD_WF_Responsible_ID(AD_WF_Responsible_ID);
	} // setAD_WF_Responsible_ID

	/**
	 * Set User from - (1) Responsible - (2) Document Sales Rep - (3) Document
	 * UpdatedBy - (4) Process invoker
	 * 
	 * @param User_ID
	 *            process invoker
	 */
	private void setUser_ID(Integer User_ID) {
		// Responsible
		MWFResponsible resp = MWFResponsible.get(getCtx(),
				getAD_WF_Responsible_ID());
		// (1) User - Directly responsible
		int AD_User_ID = resp.getAD_User_ID();

		// Invoker - get Sales Rep or last updater of Document
		if ((AD_User_ID == 0) && resp.isInvoker()) {
			getPO();
			// (2) Doc Owner
			if ((m_po != null) && (m_po instanceof DocAction)) {
				DocAction da = (DocAction) m_po;
				AD_User_ID = da.getDoc_User_ID();
			}
			// (2) Sales Rep
			if ((AD_User_ID == 0) && (m_po != null)
					&& (m_po.get_ColumnIndex("SalesRep_ID") != -1)) {
				Object sr = m_po.get_Value("SalesRep_ID");
				if ((sr != null) && (sr instanceof Integer))
					AD_User_ID = ((Integer) sr).intValue();
			}
			// (3) UpdatedBy
			if ((AD_User_ID == 0) && (m_po != null))
				AD_User_ID = m_po.getUpdatedBy();
		}

		// (4) Process Owner
		if ((AD_User_ID == 0) && (User_ID != null))
			AD_User_ID = User_ID.intValue();
		// Fallback
		if (AD_User_ID == 0)
			AD_User_ID = getCtx().getAD_User_ID();
		//
		setAD_User_ID(AD_User_ID);
	} // setUser_ID

	/**
	 * Get Workflow
	 * 
	 * @return workflow
	 */
	private MWorkflow getWorkflow() {
		if (m_wf == null)
			m_wf = MWorkflow.get(getCtx(), getAD_Workflow_ID());
		if (m_wf.get_ID() == 0)
			throw new CompiereStateException("Not found - AD_Workflow_ID="
					+ getAD_Workflow_ID());
		return m_wf;
	} // getWorkflow

	/**
	 * Start WF Execution async
	 * 
	 * @return true if success
	 */
	public boolean startWork() {
		setDateActualStart(new Timestamp(System.currentTimeMillis()));
		save();
		if (!m_state.isValidAction(StateEngine.ACTION_Start)) {
			log.warning("State=" + getWFState() + " - cannot start");
			return false;
		}
		int AD_WF_Node_ID = getWorkflow().getAD_WF_Node_ID();
		log.fine("AD_WF_Node_ID=" + AD_WF_Node_ID);
		setWFState(WFSTATE_Running);
		try {
			// Start first Activity with first Node
			MWFActivity activity = new MWFActivity(this, AD_WF_Node_ID);
			activity.run(get_Trx());
		} catch (Exception e) {
			log.log(Level.SEVERE, "AD_WF_Node_ID=" + AD_WF_Node_ID, e);
			setTextMsg(e.toString());
			setWFState(StateEngine.STATE_Terminated);
			return false;
		}
		return true;
	} // startWork

	/**************************************************************************
	 * Get Persistent Object
	 * 
	 * @return po
	 */
	public PO getPO() {
		if (m_po != null)
			return m_po;
		if (getRecord_ID() == 0)
			return null;

		MTable table = MTable.get(getCtx(), getAD_Table_ID());
		m_po = table.getPO(getCtx(), getRecord_ID(), get_Trx());
		return m_po;
	} // getPO

	/**
	 * Set Text Msg (add to existing)
	 * 
	 * @param po
	 *            base object
	 */
	public void setTextMsg(PO po) {
		if ((po != null) && (po instanceof DocAction))
			setTextMsg(((DocAction) po).getSummary());
	} // setTextMsg

	/**
	 * Set Text Msg (add to existing)
	 * 
	 * @param TextMsg
	 *            msg
	 */
	@Override
	public void setTextMsg(String TextMsg) {
		String oldText = getTextMsg();
		if ((oldText == null) || (oldText.length() == 0))
			super.setTextMsg(TextMsg);
		else if ((TextMsg != null) && (TextMsg.length() > 0))
			super.setTextMsg(oldText + "\n - " + TextMsg);
	} // setTextMsg

	/**
	 * Set Runtime (Error) Message
	 * 
	 * @param msg
	 *            message
	 */
	public void setProcessMsg(String msg) {
		m_processMsg = msg;
		if ((msg != null) && (msg.length() > 0))
			setTextMsg(msg);
	} // setProcessMsg

	/**
	 * Get Runtime (Error) Message
	 * 
	 * @return msg
	 */
	public String getProcessMsg() {
		return m_processMsg;
	} // getProcessMsg

	/**
	 * String Representation
	 * 
	 * @return info
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("MWFProcess[").append(get_ID())
		.append(", AD_Workflow_ID=").append(getAD_Workflow_ID())
		.append(", WFState=").append(getWFState()).append("]");
		return sb.toString();
	} // totString

	public boolean hasProcessScheduledAtSameMinute(
			final int AD_WorkflowProcessor_ID) {

		if (AD_WorkflowProcessor_ID <= 0)
			return false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM AD_WF_Process "
			+ "WHERE AD_WorkflowProcessor_ID = ? AND "
			+ "AD_WF_Process_ID <> ? AND " + "Processed='N' AND "
			+ "WFState='ON' AND "
			+ "DateScheduledStart >= ? AND DateScheduledStart < ?";

		int count = 0;
		try {
			Calendar cal = Calendar.getInstance();
			if (getDateScheduledStart() != null)
				cal.setTime(getDateScheduledStart());
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, AD_WorkflowProcessor_ID);
			pstmt.setInt(2, getAD_WF_Process_ID());
			pstmt.setTimestamp(3, new Timestamp(cal.getTimeInMillis()));
			pstmt.setTimestamp(4, new Timestamp(
					cal.getTimeInMillis() + 1000 * 60));
			rs = pstmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
				log.log(Level.SEVERE, sql, e);
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				log.log(Level.SEVERE, sql, e);
			}
		}

		return count > 0;
	}

	public int getPendingProcessCount(final int AD_WorkflowProcessor_ID) {

		if (AD_WorkflowProcessor_ID <= 0)
			return 0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM AD_WF_Process "
			+ "WHERE AD_WorkflowProcessor_ID = ? AND "
			+ "AD_WF_Process_ID <> ? AND " + "Processed='N' AND "
			+ "WFState='ON' ";

		int count = 0;
		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, AD_WorkflowProcessor_ID);
			pstmt.setInt(2, getAD_WF_Process_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
				log.log(Level.SEVERE, sql, e);
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				log.log(Level.SEVERE, sql, e);
			}
		}

		return count;
	}

} // MWFProcess
