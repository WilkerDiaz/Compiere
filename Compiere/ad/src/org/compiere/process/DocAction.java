/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.process;

import java.io.*;
import java.math.*;
import java.sql.*;

import org.compiere.util.*;

/**
 *	Document Action Interface
 *	
 *  @author Jorg Janke
 *  @version $Id: DocAction.java 8244 2009-12-04 23:25:29Z freyes $
 */
public interface DocAction
{
	/**
	 * 	Set Doc Status
	 *	@param newStatus new Status
	 */
	public void setDocStatus (String newStatus);

	/**
	 * 	Get Doc Status
	 *	@return Document Status
	 */
	public String getDocStatus();
	
	
	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt();
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt();
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt();
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean approveIt();
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt();
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt();
	/**
	 * 	Void Document
	 * 	@return true if success 
	 */
	public boolean voidIt();
	/**
	 * 	Close Document
	 * 	@return true if success 
	 */
	public boolean closeIt();
	/**
	 * 	Reverse Correction
	 * 	@return true if success 
	 */
	public boolean reverseCorrectIt();
	/**
	 * 	Reverse Accrual
	 * 	@return true if success 
	 */
	public boolean reverseAccrualIt();
	/** 
	 * 	Re-activate
	 * 	@return true if success 
	 */
	public boolean reActivateIt();

	/**************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary();

	/**
	 * 	Get Document No
	 *	@return Document No
	 */
	public String getDocumentNo();

	/**
	 * 	Get Document Info
	 *	@return Type and Document No
	 */
	public String getDocumentInfo();

	/**
	 * 	Create PDF
	 *	@return file
	 */
	public File createPDF ();
	
	/**
	 * 	Get Process Message
	 *	@return clear text message
	 */
	public String getProcessMsg ();
	
	/**
	 * 	Get Document Owner
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID();
	
	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID();

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt();

	/**
	 * 	Get Document Client
	 *	@return AD_Client_ID
	 */
	public int getAD_Client_ID();

	/**
	 * 	Get Document Organization
	 *	@return AD_Org_ID
	 */
	public int getAD_Org_ID();

	/**
	 * 	Get Doc Action
	 *	@return Document Action
	 */
	public String getDocAction();

	/**
	 * 	Save Document
	 *	@return true if saved
	 */
	public boolean save();
	
	/**
	 * 	Get Context
	 *	@return context
	 */
	public Ctx getCtx();
	
	/**
	 * 	Get ID of record
	 *	@return ID
	 */
	public int get_ID();
	
	/**
	 * 	Get AD_Table_ID
	 *	@return AD_Table_ID
	 */
	public int get_Table_ID();
	
	/**
	 * 	Get Logger
	 *	@return logger
	 */
	public CLogger get_Logger();

	/**
	 * 	Get Transaction
	 *	@return p_trx name
	 */
	public Trx get_Trx();

	/**
	 * Set process message
	 * @param processMsg
	 */
	public void setProcessMsg(String processMsg);

	
	/**
	 * 	Set DocAction
	 *	@param DocAction doc action
	 */
	public void setDocAction (String docAction);

	/**
	 * 	Set Processed.
	 * 	Propagate to Lines/Taxes
	 *	@param processed processed
	 */
	public void setProcessed (boolean processed);

	
	/**
	 * Return the date to use for checking whether the periods are open.
	 * 
	 * @return
	 */
	public Timestamp getDocumentDate();
	
	/**
	 * Return the document base type for checking whether the periods are open.
	 * 
	 * @return
	 */
	public String getDocBaseType();
	
	/**
	 * Return the SQL to use to retrieve the document line organizations. This
	 * is used to check whether the periods are open.
	 * 
	 * @return list of AD_Org_ID.  null if the lines do not need to be checked.
	 */
	public Env.QueryParams getLineOrgsQueryInfo();	
	
}	//	DocAction
