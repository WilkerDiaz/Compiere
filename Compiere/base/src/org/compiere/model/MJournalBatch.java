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

import org.compiere.api.*;
import org.compiere.common.*;
import org.compiere.framework.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.util.Env.*;
import org.compiere.vos.*;

/**
 *  Journal Batch Model
 *
 *	@author Jorg Janke
 *	@version $Id: MJournalBatch.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MJournalBatch extends X_GL_JournalBatch implements DocAction
{
    /** Logger for class MJournalBatch */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MJournalBatch.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	private int I_GLJournal_ID=0;

	/**
	 * 	Create new Journal Batch by copying
	 * 	@param ctx context
	 *	@param GL_JournalBatch_ID journal batch
	 * 	@param dateDoc date of the document date
	 *	@param trx transaction
	 *	@return Journal Batch
	 */
	public static MJournalBatch copyFrom (Ctx ctx, int GL_JournalBatch_ID,
		Timestamp dateDoc, Trx trx)
	{
		MJournalBatch from = new MJournalBatch (ctx, GL_JournalBatch_ID, trx);
		if (from.getGL_JournalBatch_ID() == 0)
			throw new IllegalArgumentException ("From Journal Batch not found GL_JournalBatch_ID=" + GL_JournalBatch_ID);
		//
		MJournalBatch to = new MJournalBatch (ctx, 0, trx);
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("DocumentNo", null);
		to.set_ValueNoCheck ("C_Period_ID", null);
		to.setDateAcct(dateDoc);
		to.setDateDoc(dateDoc);
		to.setDocStatus(DOCSTATUS_Drafted);
		to.setDocAction(DOCACTION_Complete);
		to.setIsApproved(false);
		to.setProcessed (false);
		//
		if (!to.save())
			throw new CompiereStateException("Could not create Journal Batch");

		if (to.copyDetailsFrom(from) == 0)
			throw new CompiereStateException("Could not create Journal Batch Details");

		return to;
	}	//	copyFrom


	/**************************************************************************
	 * 	Standard Construvtore
	 *	@param ctx context
	 *	@param GL_JournalBatch_ID id if 0 - create actual batch
	 *	@param trx transaction
	 */
	public MJournalBatch (Ctx ctx, int GL_JournalBatch_ID, Trx trx)
	{
		super (ctx, GL_JournalBatch_ID, trx);
		if (GL_JournalBatch_ID == 0)
		{
		//	setGL_JournalBatch_ID (0);	PK
		//	setDescription (null);
		//	setDocumentNo (null);
		//	setC_DocType_ID (0);
			setPostingType (POSTINGTYPE_Actual);
			setDocAction (DOCACTION_Complete);
			setDocStatus (DOCSTATUS_Drafted);
			setTotalCr (Env.ZERO);
			setTotalDr (Env.ZERO);
			setProcessed (false);
			setProcessing (false);
			setIsApproved(false);
		}
	}	//	MJournalBatch
	
	public MJournalBatch(X_I_GLJournal imp)
	{
		this(imp.getCtx(), 0, imp.get_Trx());
	    PO.copyValues(imp, this, imp.getAD_Client_ID(), imp.getAD_Org_ID());
		I_GLJournal_ID=imp.getI_GLJournal_ID();
		
		if(imp.getPostingType()==null)
			setPostingType (POSTINGTYPE_Actual);
		
		if (imp.getDateAcct()!=null)
			setDateAcct(imp.getDateAcct());
		
		setC_Period_ID(imp.getC_Period_ID());
		
		if (imp.getBatchDocumentNo() != null 
				&& imp.getBatchDocumentNo().length() > 0)
				setDocumentNo (imp.getBatchDocumentNo());

	}

	public int getI_GLJournal_ID()
	{
		return I_GLJournal_ID;
	}
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MJournalBatch (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MJournalBatch

	/**
	 * 	Copy Constructor.
	 * 	Dos not copy: Dates/Period
	 *	@param original original
	 */
	public MJournalBatch (MJournalBatch original)
	{
		this (original.getCtx(), 0, original.get_Trx());
		setClientOrg(original);
		setGL_JournalBatch_ID(original.getGL_JournalBatch_ID());
		//
	//	setC_AcctSchema_ID(original.getC_AcctSchema_ID());
	//	setGL_Budget_ID(original.getGL_Budget_ID());
		setGL_Category_ID(original.getGL_Category_ID());
		setPostingType(original.getPostingType());
		setDescription(original.getDescription());
		setC_DocType_ID(original.getC_DocType_ID());
		setControlAmt(original.getControlAmt());
		//
		setC_Currency_ID(original.getC_Currency_ID());
	//	setC_ConversionType_ID(original.getC_ConversionType_ID());
	//	setCurrencyRate(original.getCurrencyRate());

	//	setDateDoc(original.getDateDoc());
	//	setDateAcct(original.getDateAcct());
	//	setC_Period_ID(original.getC_Period_ID());
	}	//	MJournal



	/**
	 * 	Overwrite Client/Org if required
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	@Override
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg


	/**
	 * 	Get Journal Lines
	 * 	@param requery requery
	 *	@return Array of lines
	 */
	public MJournal[] getJournals (boolean requery)
	{
		ArrayList<MJournal> list = new ArrayList<MJournal>();
		String sql = "SELECT * FROM GL_Journal WHERE GL_JournalBatch_ID=? ORDER BY DocumentNo";
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getGL_JournalBatch_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MJournal (getCtx(), rs, get_Trx()));
	
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
		MJournal[] retValue = new MJournal[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getJournals
	
	
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MJournalBatch that = (MJournalBatch) o;
		if (this.getGL_JournalBatch_ID() == 0
				|| that.getGL_JournalBatch_ID() == 0)
			return false;
		super.equals(o);
		return true;
	}

	/**
	 * 	Copy Journal/Lines from other Journal Batch
	 *	@param jb Journal Batch
	 *	@return number of journals + lines copied
	 */
	public int copyDetailsFrom (MJournalBatch jb)
	{
		if (isProcessed() || (jb == null))
			return 0;
		int count = 0;
		int lineCount = 0;
		MJournal[] fromJournals = jb.getJournals(false);
		for (MJournal element : fromJournals) {
			MJournal toJournal = new MJournal (getCtx(), 0, jb.get_Trx());
			PO.copyValues(element, toJournal, getAD_Client_ID(), getAD_Org_ID());
			toJournal.setGL_JournalBatch_ID(getGL_JournalBatch_ID());
			toJournal.set_ValueNoCheck ("DocumentNo", null);	//	create new
			toJournal.set_ValueNoCheck ("C_Period_ID", null);
			toJournal.setDateDoc(getDateDoc());		//	dates from this Batch
			toJournal.setDateAcct(getDateAcct());
			toJournal.setDocStatus(X_GL_Journal.DOCSTATUS_Drafted);
			toJournal.setDocAction(X_GL_Journal.DOCACTION_Complete);
			toJournal.setTotalCr(Env.ZERO);
			toJournal.setTotalDr(Env.ZERO);
			toJournal.setIsApproved(false);
			toJournal.setIsPrinted(false);
			toJournal.setPosted(false);
			toJournal.setProcessed(false);
			if (toJournal.save())
			{
				count++;
				lineCount += toJournal.copyLinesFrom(element, getDateAcct(), 'x');
			}
		}
		if (fromJournals.length != count)
			log.log(Level.SEVERE, "Line difference - Journals=" + fromJournals.length + " <> Saved=" + count);

		return count + lineCount;
	}	//	copyLinesFrom

	/**
	 * 	Get Period
	 * 	@return period or null
	 */
	public MPeriod getPeriod()
	{
		int C_Period_ID = getC_Period_ID();
		if (C_Period_ID != 0)
			return MPeriod.get(getCtx(), C_Period_ID);
		return null;
	}	//	getPeriod

	/**
	 * 	Set Doc Date - Callout.
	 * 	Sets also acct date and period
	 *	@param oldDateDoc old
	 *	@param newDateDoc new
	 *	@param windowNo window no
	 */
	@UICallout public void setDateDoc (String oldDateDoc,
			String newDateDoc, int windowNo) throws Exception
	{
		if ((newDateDoc == null) || (newDateDoc.length() == 0))
			return;
		Timestamp dateDoc = PO.convertToTimestamp(newDateDoc);
		if (dateDoc == null)
			return;
		setDateDoc(dateDoc);
		setDateAcct(dateDoc);
	}	//	setDateDoc

	/**
	 * 	Set Acct Date - Callout.
	 * 	Sets Period
	 *	@param oldDateAcct old
	 *	@param newDateAcct new
	 *	@param windowNo window no
	 */
	@UICallout public void setDateAcct (String oldDateAcct,
			String newDateAcct, int windowNo) throws Exception
	{
		if ((newDateAcct == null) || (newDateAcct.length() == 0))
			return;
		Timestamp dateAcct = PO.convertToTimestamp(newDateAcct);
		if (dateAcct == null)
			return;
		setDateAcct(dateAcct);
	}	//	setDateAcct

	/**
	 * 	Set Period - Callout.
	 * 	Set Acct Date if required
	 *	@param oldC_Period_ID old
	 *	@param newC_Period_ID new
	 *	@param windowNo window no
	 */
	@UICallout public void setC_Period_ID (String oldC_Period_ID,
			String newC_Period_ID, int windowNo) throws Exception
	{
		if ((newC_Period_ID == null) || (newC_Period_ID.length() == 0))
			return;
		int C_Period_ID = Integer.parseInt(newC_Period_ID);
		if (C_Period_ID == 0)
			return;
		setC_Period_ID(C_Period_ID);
	}	//	setC_Period_ID

	/**
	 * 	Set Accounting Date.
	 * 	Set also Period if not set earlier
	 *	@param DateAcct date
	 */
	@Override
	public void setDateAcct (Timestamp DateAcct)
	{
		super.setDateAcct(DateAcct);
		if (DateAcct == null)
			return;
		if (getC_Period_ID() != 0)
			return;
		int C_Period_ID = MPeriod.getC_Period_ID(getCtx(), getAD_Org_ID(), DateAcct);
		if (C_Period_ID == 0)
			log.warning("Period not found");
		else
			super.setC_Period_ID(C_Period_ID);
	}	//	setDateAcct

	/**
	 * 	Set Period
	 * 	@param C_Period_ID period
	 */
	@Override
	public void setC_Period_ID(int C_Period_ID)
	{
		super.setC_Period_ID(C_Period_ID);
		if (C_Period_ID == 0)
			return;
		Timestamp dateAcct = getDateAcct();
		//
		MPeriod period = getPeriod();
		if ((period != null) && period.isStandardPeriod())
		{
			if (!period.isInPeriod(dateAcct))
				super.setDateAcct(period.getEndDate());
		}
	}	//	setC_Period_ID

	/**	Process Message 			*/
	private String		m_processMsg = null;

	/**
	 * 	Unlock Document.
	 * 	@return true if success
	 */
	public boolean unlockIt()
	{
		log.info("unlockIt - " + toString());
		setProcessing(false);
		return true;
	}	//	unlockIt

	/**
	 * 	Invalidate Document
	 * 	@return true if success
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
		return true;
	}	//	invalidateIt

	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
		MPeriod period = new MPeriod(getCtx(), getC_Period_ID(), null);
		if (period.isStandardPeriod())
			period = MPeriod.getOfOrg(getCtx(), getAD_Org_ID(), getDateAcct());
		if (period == null)
		{
			log.warning("No Period for " + getDateAcct());
			m_processMsg = "@PeriodNotFound@";
			return DocActionConstants.STATUS_Invalid;
		}
		setC_Period_ID(period.getC_Period_ID());

		//	Add up Amounts & prepare them
		MJournal[] journals = getJournals(false);
		if (journals.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocActionConstants.STATUS_Invalid;
		}

		BigDecimal TotalDr = Env.ZERO;
		BigDecimal TotalCr = Env.ZERO;
		for (MJournal journal : journals) {
			if (!journal.isActive())
				continue;
			//	Prepare if not closed
			if (DOCSTATUS_Closed.equals(journal.getDocStatus())
				|| DOCSTATUS_Voided.equals(journal.getDocStatus())
				|| DOCSTATUS_Reversed.equals(journal.getDocStatus())
				|| DOCSTATUS_Completed.equals(journal.getDocStatus()))
				;
			else
			{
				boolean success = DocumentEngine.processIt(journal, DocActionConstants.ACTION_Prepare);
				journal.save();
				if (!success) {
					m_processMsg = journal.getProcessMsg();
					return journal.getDocStatus();
				}
			}
			//
			TotalDr = TotalDr.add(journal.getTotalDr());
			TotalCr = TotalCr.add(journal.getTotalCr());
		}
		setTotalDr(TotalDr);
		setTotalCr(TotalCr);

		//	Control Amount
		if ((Env.ZERO.compareTo(getControlAmt()) != 0)
			&& (getControlAmt().compareTo(getTotalDr()) != 0))
		{
			m_processMsg = "@ControlAmtError@";
			return DocActionConstants.STATUS_Invalid;
		}

		//	Add up Amounts
		return DocActionConstants.STATUS_InProgress;
	}	//	prepareIt

	/**
	 * 	Approve Document
	 * 	@return true if success
	 */
	public boolean  approveIt()
	{
		log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	}	//	approveIt

	/**
	 * 	Reject Approval
	 * 	@return true if success
	 */
	public boolean rejectIt()
	{
		log.info("rejectIt - " + toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt

	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Add up Amounts & complete them
		MJournal[] journals = getJournals(true);
		BigDecimal TotalDr = Env.ZERO;
		BigDecimal TotalCr = Env.ZERO;
		for (MJournal journal : journals)
		{
			if (!journal.isActive())
			{
				journal.setProcessed(true);
				journal.setDocStatus(DOCSTATUS_Voided);
				journal.setDocAction(DOCACTION_None);
				journal.save();
				continue;
			}
			//	Complete if not closed
			if (DOCSTATUS_Closed.equals(journal.getDocStatus())
				|| DOCSTATUS_Voided.equals(journal.getDocStatus())
				|| DOCSTATUS_Reversed.equals(journal.getDocStatus())
				|| DOCSTATUS_Completed.equals(journal.getDocStatus()))
				;
			else
			{
				boolean success = DocumentEngine.processIt(journal, DocActionConstants.ACTION_Complete);
				journal.save();
				if (!success)
				{
					m_processMsg = journal.getProcessMsg();
					return journal.getDocStatus();
				}
			}
			//
			TotalDr = TotalDr.add(journal.getTotalDr());
			TotalCr = TotalCr.add(journal.getTotalCr());
		}
		setTotalDr(TotalDr);
		setTotalCr(TotalCr);
		return DocActionConstants.STATUS_Completed;
	}	//	completeIt

	/**
	 * 	Void Document.
	 * 	@return false
	 */
	public boolean voidIt()
	{
		log.info("voidIt - " + toString());
		log.info(toString());
	
		
		if (DOCSTATUS_Drafted.equals(getDocStatus())
				|| DOCSTATUS_Invalid.equals(getDocStatus())) {

			MJournal[] journals = getJournals(false);
			for (MJournal journal : journals) {
				journal.voidIt();
				journal.save(get_Trx());
			}
			setProcessed(true);
			setDocAction(DOCACTION_None);
			return true;	
		}
		
		return false;
	
	}	//	voidIt

	/**
	 * 	Close Document.
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info("closeIt - " + toString());
		MJournal[] journals = getJournals(true);
		for (MJournal journal : journals) {
			if (!journal.isActive() && !journal.isProcessed())
			{
				journal.setProcessed(true);
				journal.setDocStatus(DOCSTATUS_Voided);
				journal.setDocAction(DOCACTION_None);
				journal.save();
				continue;
			}
			if (DOCSTATUS_Drafted.equals(journal.getDocStatus())
				|| DOCSTATUS_InProgress.equals(journal.getDocStatus())
				|| DOCSTATUS_Invalid.equals(journal.getDocStatus()))
			{
				m_processMsg = "Journal not Completed: " + journal.getSummary();
				return false;
			}

			//	Close if not closed
			if (DOCSTATUS_Closed.equals(journal.getDocStatus())
				|| DOCSTATUS_Voided.equals(journal.getDocStatus())
				|| DOCSTATUS_Reversed.equals(journal.getDocStatus()))
				;
			else
			{
				if (!DocumentEngine.processIt(journal, DocActionConstants.ACTION_Close))
				{
					m_processMsg = "Cannot close: " + journal.getSummary();
					return false;
				}
				journal.save();
			}
		}
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction.
	 * 	As if nothing happened - same date
	 * 	@return true if success
	 */
	public boolean reverseCorrectIt()
	{
		log.info("reverseCorrectIt - " + toString());
		MJournal[] journals = getJournals(true);
		//	check prerequisites
		for (MJournal journal : journals) {
			if (!journal.isActive())
				continue;
			//	All need to be closed/Completed
			if (DOCSTATUS_Completed.equals(journal.getDocStatus()))
				;
			else
			{
				m_processMsg = "All Journals need to be Compleded: " + journal.getSummary();
				return false;
			}
		}

		//	Reverse it
		MJournalBatch reverse = new MJournalBatch (this);
		reverse.setDateDoc(getDateDoc());
		reverse.setC_Period_ID(getC_Period_ID());
		reverse.setDateAcct(getDateAcct());
		//	Reverse indicator
		String description = reverse.getDescription();
		if (description == null)
			description = "** " + getDocumentNo() + " **";
		else
			description += " ** " + getDocumentNo() + " **";
		reverse.setDescription(description);
		reverse.save();
		//

		//	Reverse Journals
		for (MJournal journal : journals) {
			if (!journal.isActive())
				continue;
			if (journal.reverseCorrectIt(reverse.getGL_JournalBatch_ID()) == null)
			{
				m_processMsg = "Could not reverse " + journal;
				return false;
			}
			journal.save();
		}
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	reverseCorrectionIt

	/**
	 * 	Reverse Accrual.
	 * 	Flip Dr/Cr - Use Today's date
	 * 	@return true if success
	 */
	public boolean reverseAccrualIt()
	{
		log.info("reverseAccrualIt - " + toString());
		MJournal[] journals = getJournals(true);
		//	check prerequisites
		for (MJournal journal : journals) {
			if (!journal.isActive())
				continue;
			//	All need to be closed/Completed
			if (DOCSTATUS_Completed.equals(journal.getDocStatus()))
				;
			else
			{
				m_processMsg = "All Journals need to be Compleded: " + journal.getSummary();
				return false;
			}
		}
		//	Reverse it
		MJournalBatch reverse = new MJournalBatch (this);
		reverse.setC_Period_ID(0);
		reverse.setDateDoc(new Timestamp(System.currentTimeMillis()));
		reverse.setDateAcct(reverse.getDateDoc());
		//	Reverse indicator
		String description = reverse.getDescription();
		if (description == null)
			description = "** " + getDocumentNo() + " **";
		else
			description += " ** " + getDocumentNo() + " **";
		reverse.setDescription(description);
		reverse.save();

		//	Reverse Journals
		for (MJournal journal : journals) {
			if (!journal.isActive())
				continue;
			if (journal.reverseAccrualIt(reverse.getGL_JournalBatch_ID()) == null)
			{
				m_processMsg = "Could not reverse " + journal;
				return false;
			}
			journal.save();
		}
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	reverseAccrualIt

	/**
	 * 	Re-activate - same as reverse correct
	 * 	@return true if success
	 */
	public boolean reActivateIt()
	{
		log.info("reActivateIt - " + toString());
	//	setProcessed(false);
		return DocumentEngine.processIt(this, DocActionConstants.ACTION_Reverse_Correct);
	}	//	reActivateIt


	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		//	: Total Lines = 123.00 (#1)
		sb.append(": ")
		.append(Msg.translate(getCtx(),"TotalDr")).append("=").append(getTotalDr())
		.append(" ")
		.append(Msg.translate(getCtx(),"TotalCR")).append("=").append(getTotalCr())
		.append(" (#").append(getJournals(false).length).append(")");
		//	 - Description
		if ((getDescription() != null) && (getDescription().length() > 0))
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MJournalBatch[");
		sb.append(get_ID()).append(",").append(getDescription())
			.append(",DR=").append(getTotalDr())
			.append(",CR=").append(getTotalCr())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF


	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg

	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID (Created By)
	 */
	public int getDoc_User_ID()
	{
		return getCreatedBy();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return DR amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return getTotalDr();
	}	//	getApprovalAmt

	@Override
	public void setProcessMsg(String processMsg) {
		m_processMsg = processMsg;
	}


	@Override
	public String getDocBaseType() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getDocBaseType();
	}


	@Override
	public Timestamp getDocumentDate() {
		return getDateAcct();
	}


	@Override
	public QueryParams getLineOrgsQueryInfo() {
		return null;
	}
	
}	//	MJournalBatch
