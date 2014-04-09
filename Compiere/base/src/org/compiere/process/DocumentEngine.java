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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.*;

import javax.naming.*;

import org.compiere.api.*;
import org.compiere.db.*;
import org.compiere.framework.*;
import org.compiere.interfaces.*;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.util.Env.*;
import org.compiere.vos.*;

/**
 *	Document Action Engine
 *	
 *  @author Jorg Janke
 *  @version $Id: DocumentEngine.java,v 1.2 2006/07/30 00:54:44 jjanke Exp $
 */
public class DocumentEngine
{

	static final ThreadLocal<Integer> stackDepth = new ThreadLocal<Integer>();
	static final boolean LOG_TIMING_STATISTICS = false;
	
	/**
	 * Process document.  This replaces DocAction.processIt().
	 * @param doc
	 * @param processAction 
	 * @return true if performed
	 */
	public static boolean processIt(DocAction doc, String processAction) {
	
		if (LOG_TIMING_STATISTICS) {
			if(stackDepth.get() == null)
				stackDepth.set(0);
			for (int i = 0; i < stackDepth.get(); ++i) {
				System.err.print("  ");
			}
			System.err.println(doc + " " + processAction + ": Begin");

			stackDepth.set(stackDepth.get() + 1);
		}
		
		long time = System.currentTimeMillis();
		
		boolean success = false;
		Ctx ctx = doc.getCtx();
		boolean oldIsBatchMode = ctx.isBatchMode();
		ctx.setBatchMode(true);
		DocumentEngine engine = new DocumentEngine(doc, doc.getDocStatus());
		success = engine.processIt(processAction, doc.getDocAction());
		ctx.setBatchMode(oldIsBatchMode);
		

		if (LOG_TIMING_STATISTICS) {
			stackDepth.set(stackDepth.get() - 1);

			for (int i = 0; i < stackDepth.get(); ++i) {
				System.err.print("  ");
			}
			System.err.println(doc + " " + processAction + ": Completed in " + (System.currentTimeMillis() - time)
					+ " ms");
		}

		return success;
	}	
	
	/**
	 * 	Doc Engine
	 * 	@param po document
	 * 	@param docStatus initial document status
	 */
	private DocumentEngine (DocAction po, String docStatus)
	{
		m_document = po;
		if (docStatus != null)
			m_status = docStatus;
	}	//	DocActionEngine

	/** Persistent Document 	*/
	private final DocAction m_document;
	/** Document Status			*/
	private String		m_status = DocActionConstants.STATUS_Drafted;

	private static final CLogger log = CLogger.getCLogger (DocumentEngine.class);
	
	/**
	 * 	Get Doc Status
	 *	@return document status
	 */
	private String getDocStatus()
	{
		return m_status;
	}	//	getDocStatus

	/**
	 * 	Document is Drafted
	 *	@return true if drafted
	 */
	private boolean isDrafted()
	{
		return DocActionConstants.STATUS_Drafted.equals(m_status);
	}	//	isDrafted
	
	/**
	 * 	Document is Invalid
	 *	@return true if Invalid
	 */
	private boolean isInvalid()
	{
		return DocActionConstants.STATUS_Invalid.equals(m_status);
	}	//	isInvalid
	
	/**
	 * 	Document is In Progress
	 *	@return true if In Progress
	 */
	private boolean isInProgress()
	{
		return DocActionConstants.STATUS_InProgress.equals(m_status);
	}	//	isInProgress
	
	/**
	 * 	Document is Approved
	 *	@return true if Approved
	 */
	private boolean isApproved()
	{
		return DocActionConstants.STATUS_Approved.equals(m_status);
	}	//	isApproved
	
	/**
	 * 	Document is Not Approved
	 *	@return true if Not Approved
	 */
	private boolean isNotApproved()
	{
		return DocActionConstants.STATUS_NotApproved.equals(m_status);
	}	//	isNotApproved
	
	/**
	 * 	Document is Waiting Payment or Confirmation
	 *	@return true if Waiting Payment
	 */
	private boolean isWaiting()
	{
		return DocActionConstants.STATUS_WaitingPayment.equals(m_status)
			|| DocActionConstants.STATUS_WaitingConfirmation.equals(m_status);
	}	//	isWaitingPayment
	
	/**
	 * 	Document is Completed
	 *	@return true if Completed
	 */
	private boolean isCompleted()
	{
		return DocActionConstants.STATUS_Completed.equals(m_status);
	}	//	isCompleted
	
	/**
	 * 	Document is Reversed
	 *	@return true if Reversed
	 */
	private boolean isReversed()
	{
		return DocActionConstants.STATUS_Reversed.equals(m_status);
	}	//	isReversed
	
	/**
	 * 	Document is Closed
	 *	@return true if Closed
	 */
	private boolean isClosed()
	{
		return DocActionConstants.STATUS_Closed.equals(m_status);
	}	//	isClosed
	
	/**
	 * 	Document is Voided
	 *	@return true if Voided
	 */
	private boolean isVoided()
	{
		return DocActionConstants.STATUS_Voided.equals(m_status);
	}	//	isVoided
	
	/**
	 * 	Process actual document.
	 * 	Checks if user (document) action is valid and then process action 
	 * 	Calls the individual actions which call the document action
	 *	@param processAction document action based on workflow
	 *	@param docAction document action based on document
	 *	@return true if performed
	 */
	private boolean processIt (String processAction, String docAction)
	{
		String action = null;
		//	Std User Workflows - see MWFNodeNext.isValidFor
		
		if (isValidAction(processAction))	//	WF Selection first
			action = processAction;
		//
		else if (isValidAction(docAction))	//	User Selection second
			action = docAction;
		//	Nothing to do
		else if (processAction.equals(DocActionConstants.ACTION_None)
			|| docAction.equals(DocActionConstants.ACTION_None))
		{
			if (m_document != null)
				m_document.get_Logger().info ("**** No Action (Prc=" + processAction + "/Doc=" + docAction + ") " + m_document);
			return true;	
		}
		else
		{
			throw new IllegalStateException("Status=" + getDocStatus() 
				+ " - Invalid Actions: Process="  + processAction + ", Doc=" + docAction);
		}
		if (m_document != null)
			m_document.get_Logger().info ("**** Action=" + action + " (Prc=" + processAction + "/Doc=" + docAction + ") " + m_document);
		boolean success = processIt (action);
		if (m_document != null)
			m_document.get_Logger().fine("**** Action=" + action + " - Success=" + success);
		return success;
	}	//	process
	
	/**
	 * 	Process actual document - do not call directly.
	 * 	Calls the individual actions which call the document action
	 *	@param action document action
	 *	@return true if performed
	 */
	private boolean processIt (String action)
	{
		//
		if (DocActionConstants.ACTION_Unlock.equals(action))
			return unlockIt();
		if (DocActionConstants.ACTION_Invalidate.equals(action))
			return invalidateIt();
		if (DocActionConstants.ACTION_Prepare.equals(action))
			return DocActionConstants.STATUS_InProgress.equals(prepareIt());
		if (DocActionConstants.ACTION_Approve.equals(action))
			return approveIt();
		if (DocActionConstants.ACTION_Reject.equals(action))
			return rejectIt();
		if (DocActionConstants.ACTION_Complete.equals(action) || DocActionConstants.ACTION_WaitComplete.equals(action))
		{
			String status = null;
			if (isDrafted() || isInvalid())		//	prepare if not prepared yet
			{
				status = prepareIt();
				if (!DocActionConstants.STATUS_InProgress.equals(status))
					return false;
			}
			status = completeIt();
			if (m_document != null 
				&& !Ini.isClient())		//	Post Immediate if on Server
			{
				MClient client = MClient.get(m_document.getCtx(), m_document.getAD_Client_ID());
				if (DocActionConstants.STATUS_Completed.equals(status) && client.isPostImmediate())
				{
					m_document.save();
					postIt();
				}
			}
			return DocActionConstants.STATUS_Completed.equals(status)
				|| DocActionConstants.STATUS_InProgress.equals(status)
				|| DocActionConstants.STATUS_WaitingPayment.equals(status)
				|| DocActionConstants.STATUS_WaitingConfirmation.equals(status);
		}
		if (DocActionConstants.ACTION_ReActivate.equals(action))
			return reActivateIt();
		if (DocActionConstants.ACTION_Reverse_Accrual.equals(action))
			return reverseAccrualIt();
		if (DocActionConstants.ACTION_Reverse_Correct.equals(action))
			return reverseCorrectIt();
		if (DocActionConstants.ACTION_Close.equals(action))
			return closeIt();
		if (DocActionConstants.ACTION_Void.equals(action))
			return voidIt();
		if (DocActionConstants.ACTION_Post.equals(action))
			return postIt();
		//
		return false;
	}	//	processDocument
	
	/**
	 * 	Unlock Document.
	 * 	Status: Drafted
	 * 	@return true if success 
	 * 	@see org.compiere.process.DocAction#unlockIt()
	 */
	private boolean unlockIt()
	{
		if (!isValidAction(DocActionConstants.ACTION_Unlock))
			return false;
		if (m_document != null)
		{
			if (m_document.unlockIt())
			{
				m_status = DocActionConstants.STATUS_Drafted;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocActionConstants.STATUS_Drafted;
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document.
	 * 	Status: Invalid
	 * 	@return true if success 
	 * 	@see org.compiere.process.DocAction#invalidateIt()
	 */
	private boolean invalidateIt()
	{
		if (!isValidAction(DocActionConstants.ACTION_Invalidate))
			return false;
		if (m_document != null)
		{
			if (m_document.invalidateIt())
			{
				m_status = DocActionConstants.STATUS_Invalid;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocActionConstants.STATUS_Invalid;
		return true;
	}	//	invalidateIt
	
	/**
	 *	Process Document.
	 * 	Status is set by process
	 * 	@return new status (In Progress or Invalid) 
	 * 	@see org.compiere.process.DocAction#prepareIt()
	 */
	private String prepareIt()
	{
		if (!isValidAction(DocActionConstants.ACTION_Prepare))
			return m_status;
		if (m_document != null) {
			log.info(m_document.toString());
			
			String errorMsg = null;
			
			if (errorMsg == null) {
				// BEFORE_PREPARE model validation
				// m_document should always be a PO; cast blindly
				errorMsg = ModelValidationEngine.get().fireDocValidate((PO) m_document,
						ModelValidator.DOCTIMING_BEFORE_PREPARE);
			}
			
			if (errorMsg == null) {
				errorMsg = isPeriodOpen(m_document);
			}			
			
			if (errorMsg == null) {
				// run actual prepareIt()
				m_status = m_document.prepareIt();
			} 
			
			if (errorMsg != null) {
				m_document.setProcessMsg(errorMsg);
				m_status = DocActionConstants.STATUS_Invalid;
			}
			m_document.setDocStatus(m_status);
		}
		return m_status;
	}	//	processIt


	/**
	 * Check to see if the appropriate periods are open for this document
	 * @param doc
	 * @return null if all periods open; otherwise the error message
	 */
	public static String isPeriodOpen(DocAction doc) {
		ArrayList<Integer> docOrgs = new ArrayList<Integer>();
		String errorMsg = null;
		if (errorMsg == null) {
			// check if lines exist

			// get all the orgs stamped on the document lines
			QueryParams params = doc.getLineOrgsQueryInfo();
			if (params != null) {
				Object[][] result = QueryUtil.executeQuery(
										doc.get_Trx(), params.sql, 
										params.params.toArray());
				for (Object[] row : result) {
					docOrgs.add(((BigDecimal) row[0]).intValue());
				}
				// check if lines are missing
				if (result.length == 0) {
					errorMsg = "@NoLines@";
				}
			}
		}

		if (errorMsg == null) {

			Timestamp docDate = doc.getDocumentDate();
			String docBaseType = doc.getDocBaseType();

			if (docDate != null && docBaseType != null) {
				// check if period is open

				// add doc header org to the list of orgs
				if (!docOrgs.contains(doc.getAD_Org_ID()))
					docOrgs.add(doc.getAD_Org_ID());

				// Std Period open?
				errorMsg = MPeriod.isOpen(doc.getCtx(), doc.getAD_Client_ID(), docOrgs, docDate, docBaseType);
			}
		}
		return errorMsg;
	}

	/**
	 * 	Approve Document.
	 * 	Status: Approved
	 * 	@return true if success 
	 * 	@see org.compiere.process.DocAction#approveIt()
	 */
	private boolean approveIt()
	{
		if (!isValidAction(DocActionConstants.ACTION_Approve))
			return false;
		if (m_document != null)
		{
			if (m_document.approveIt())
			{
				m_status = DocActionConstants.STATUS_Approved;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocActionConstants.STATUS_Approved;
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval.
	 * 	Status: Not Approved
	 * 	@return true if success 
	 * 	@see org.compiere.process.DocAction#rejectIt()
	 */
	private boolean rejectIt()
	{
		if (!isValidAction(DocActionConstants.ACTION_Reject))
			return false;
		if (m_document != null)
		{
			if (m_document.rejectIt())
			{
				m_status = DocActionConstants.STATUS_NotApproved;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocActionConstants.STATUS_NotApproved;
		return true;
	}	//	rejectIt
	
	/**
	 * 	Complete Document.
	 * 	Status is set by process
	 * @return new document status (Complete, In Progress, Invalid, Waiting ..)
	 * 	@see org.compiere.process.DocAction#completeIt()
	 */
	private String completeIt()
	{
		if (!isValidAction(DocActionConstants.ACTION_Complete))
			return m_status;
		if (m_document != null)
		{
			// Implicit Approval
			approveIt();
			log.info(toString());
			
			
			m_status = m_document.completeIt();
			
			//	User Validation
			String processMsg = null;
			if (DocActionConstants.STATUS_Completed.equals(m_status)) {
				processMsg = ModelValidationEngine.get().fireDocValidate((PO) m_document,
						ModelValidator.DOCTIMING_AFTER_COMPLETE);
				if (processMsg == null) {
					m_document.setProcessed(true);
					m_document.setDocAction(X_Ref__Document_Action.CLOSE.getValue());
				} else {
					m_status = DocActionConstants.STATUS_Invalid;
					m_document.setProcessMsg(processMsg);
				}
			} else {
				m_document.setProcessed(true);
			}
			//
			m_document.setDocStatus(m_status);
		}
		return m_status;
	}	//	completeIt
	
	/**
	 * 	Post Document
	 * 	Does not change status
	 * 	@return true if success 
	 */
	private boolean postIt()
	{
		if (!isValidAction(DocActionConstants.ACTION_Post) 
			|| m_document == null)
			return false;
		try
		{
			//	Should work on Client and Server
			InitialContext ctx = CConnection.get().getInitialContext(true);
			ServerHome serverHome = (ServerHome)ctx.lookup (ServerHome.JNDI_NAME);
			if (serverHome != null)
			{
				Server server = serverHome.create();
				if (server != null)
				{
					String error = server.postImmediate(Env.getCtx(), 
						m_document.getAD_Client_ID(),
						m_document.get_Table_ID(), m_document.get_ID(), 
						true, m_document.get_Trx());
					m_document.get_Logger().config("Server: " + error == null ? "OK" : error);
					return error == null;
				}
			}
			else
				m_document.get_Logger().config("NoServerHome");
		}
		catch (Exception e)
		{
			m_document.get_Logger().config("(ex) " + e.getMessage());
		}
		return false;
	}	//	postIt
	
	/**
	 * 	Void Document.
	 * 	Status: Voided
	 * 	@return true if success 
	 * 	@see org.compiere.process.DocAction#voidIt()
	 */
	private boolean voidIt()
	{
		if (!isValidAction(DocActionConstants.ACTION_Void))
			return false;
		if (m_document != null)
		{
			if (m_document.voidIt())
			{
				m_status = DocActionConstants.STATUS_Voided;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocActionConstants.STATUS_Voided;
		return true;
	}	//	voidIt
	
	/**
	 * 	Close Document.
	 * 	Status: Closed
	 * 	@return true if success 
	 * 	@see org.compiere.process.DocAction#closeIt()
	 */
	private boolean closeIt()
	{
		if (m_document != null 	//	orders can be closed any time
			&& m_document.get_Table_ID() == X_C_Order.Table_ID)
			;
		else if (!isValidAction(DocActionConstants.ACTION_Close))
			return false;
		if (m_document != null)
		{
			if (m_document.closeIt())
			{
				m_status = DocActionConstants.STATUS_Closed;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocActionConstants.STATUS_Closed;
		return true;
	}	//	closeIt
	
	/**
	 * 	Reverse Correct Document.
	 * 	Status: Reversed
	 * 	@return true if success 
	 * 	@see org.compiere.process.DocAction#reverseCorrectIt()
	 */
	private boolean reverseCorrectIt()
	{
		if (!isValidAction(DocActionConstants.ACTION_Reverse_Correct))
			return false;
		if (m_document != null)
		{
			if (m_document.reverseCorrectIt())
			{
				m_status = DocActionConstants.STATUS_Reversed;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocActionConstants.STATUS_Reversed;
		return true;
	}	//	reverseCorrectIt
	
	/**
	 * 	Reverse Accrual Document.
	 * 	Status: Reversed
	 * 	@return true if success 
	 * 	@see org.compiere.process.DocAction#reverseAccrualIt()
	 */
	private boolean reverseAccrualIt()
	{
		if (!isValidAction(DocActionConstants.ACTION_Reverse_Accrual))
			return false;
		if (m_document != null)
		{
			if (m_document.reverseAccrualIt())
			{
				m_status = DocActionConstants.STATUS_Reversed;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocActionConstants.STATUS_Reversed;
		return true;
	}	//	reverseAccrualIt
	
	/** 
	 * 	Re-activate Document.
	 * 	Status: In Progress
	 * 	@return true if success 
	 * 	@see org.compiere.process.DocAction#reActivateIt()
	 */
	private boolean reActivateIt()
	{
		if (!isValidAction(DocActionConstants.ACTION_ReActivate))
			return false;
		if (m_document != null)
		{
			if (m_document.reActivateIt())
			{
				m_status = DocActionConstants.STATUS_InProgress;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocActionConstants.STATUS_InProgress;
		return true;
	}	//	reActivateIt

	
	
	/**************************************************************************
	 * 	Get Action Options based on current Status
	 *	@return array of actions
	 */
	private String[] getActionOptions()
	{
		if (isInvalid())
			return new String[] {DocActionConstants.ACTION_Prepare, DocActionConstants.ACTION_Invalidate, 
				DocActionConstants.ACTION_Unlock, DocActionConstants.ACTION_Void};

		if (isDrafted())
			return new String[] {DocActionConstants.ACTION_Prepare, DocActionConstants.ACTION_Invalidate, DocActionConstants.ACTION_Complete, 
				DocActionConstants.ACTION_Unlock, DocActionConstants.ACTION_Void};
		
		if (isInProgress() || isApproved())
			return new String[] {DocActionConstants.ACTION_Complete, DocActionConstants.ACTION_WaitComplete, 
				DocActionConstants.ACTION_Approve, DocActionConstants.ACTION_Reject, 
				DocActionConstants.ACTION_Unlock, DocActionConstants.ACTION_Void, DocActionConstants.ACTION_Prepare};
		
		if (isNotApproved())
			return new String[] {DocActionConstants.ACTION_Reject, DocActionConstants.ACTION_Prepare, 
				DocActionConstants.ACTION_Unlock, DocActionConstants.ACTION_Void};
		
		if (isWaiting())
			return new String[] {DocActionConstants.ACTION_Complete, DocActionConstants.ACTION_WaitComplete,
				DocActionConstants.ACTION_ReActivate, DocActionConstants.ACTION_Void, DocActionConstants.ACTION_Close};
		
		if (isCompleted())
			return new String[] {DocActionConstants.ACTION_Close, DocActionConstants.ACTION_ReActivate, 
				DocActionConstants.ACTION_Reverse_Accrual, DocActionConstants.ACTION_Reverse_Correct, 
				DocActionConstants.ACTION_Post, DocActionConstants.ACTION_Void};
		
		if (isClosed())
			return new String[] {DocActionConstants.ACTION_Post, DocActionConstants.ACTION_ReOpen};
		
		if (isReversed() || isVoided())
			return new String[] {DocActionConstants.ACTION_Post};
		
		return new String[] {};
	}	//	getActionOptions

	/**
	 * 	Is The Action Valid based on current state
	 *	@param action action
	 *	@return true if valid
	 */
	private boolean isValidAction (String action)
	{
		String[] options = getActionOptions();
		for (String element : options) {
			if (element.equals(action))
				return true;
		}
		return false;
	}	//	isValidAction


}	//	DocumentEnine
