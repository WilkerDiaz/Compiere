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
import org.compiere.framework.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.util.Env.*;
import org.compiere.vos.*;

/**
 *  GL Journal Model
 *
 *	@author Jorg Janke
 *	@version $Id: MJournal.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MJournal extends X_GL_Journal implements DocAction
{
    /** Logger for class MJournal */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MJournal.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int I_GLJournal_ID=0;
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param GL_Journal_ID id
	 *	@param trx transaction
	 */
	public MJournal (Ctx ctx, int GL_Journal_ID, Trx trx)
	{
		super (ctx, GL_Journal_ID, trx);
		if (GL_Journal_ID == 0)
		{
		//	setGL_Journal_ID (0);		//	PK
		//	setC_AcctSchema_ID (0);
		//	setC_Currency_ID (0);
		//	setC_DocType_ID (0);
		//	setC_Period_ID (0);
			//
			setCurrencyRate (Env.ONE);
		//	setC_ConversionType_ID(0);
			setDateAcct (new Timestamp(System.currentTimeMillis()));
			setDateDoc (new Timestamp(System.currentTimeMillis()));
		//	setDescription (null);
			setDocAction (DOCACTION_Complete);
			setDocStatus (DOCSTATUS_Drafted);
		//	setDocumentNo (null);
		//	setGL_Category_ID (0);
			setPostingType (POSTINGTYPE_Actual);
			setTotalCr (Env.ZERO);
			setTotalDr (Env.ZERO);
			setIsApproved (false);
			setIsPrinted (false);
			setIsManual(true);
			setPosted (false);
			setProcessed(false);
		}
	}	//	MJournal
	
	public MJournal(X_I_GLJournal imp)
	{		
		this(imp.getCtx(), 0, imp.get_Trx());	  
		PO.copyValues(imp, this, imp.getAD_Client_ID(), imp.getAD_Org_ID());
		I_GLJournal_ID = imp.getI_GLJournal_ID();
		
		if (imp.getJournalDocumentNo() != null && imp.getJournalDocumentNo().length() > 0)
			setDocumentNo (imp.getJournalDocumentNo());
		
		if (imp.getDateAcct()!=null)
			setDateAcct(imp.getDateAcct());
		
		setC_Period_ID(imp.getC_Period_ID());

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
	public MJournal (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MJournal

	/**
	 * 	Parent Constructor.
	 *	@param parent batch
	 */
	public MJournal (MJournalBatch parent)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setGL_JournalBatch_ID(parent.getGL_JournalBatch_ID());
		setC_DocType_ID(parent.getC_DocType_ID());
		setGL_Category_ID(parent.getGL_Category_ID());
		setPostingType(parent.getPostingType());
		//
		setDateDoc(parent.getDateDoc());
		setC_Period_ID(parent.getC_Period_ID());
		setDateAcct(parent.getDateAcct());
		setC_Currency_ID(parent.getC_Currency_ID());
	}	//	MJournal
	
	/**
	 * 	Copy Constructor.
	 * 	Dos not copy: Dates/Period
	 *	@param original original
	 */
	public MJournal (MJournal original)
	{
		this (original.getCtx(), 0, original.get_Trx());
		setClientOrg(original);
		setGL_JournalBatch_ID(original.getGL_JournalBatch_ID());
		//
		setC_AcctSchema_ID(original.getC_AcctSchema_ID());
		setGL_Budget_ID(original.getGL_Budget_ID());
		setGL_Category_ID(original.getGL_Category_ID());
		setPostingType(original.getPostingType());
		setDescription(original.getDescription());
		setC_DocType_ID(original.getC_DocType_ID());
		setControlAmt(original.getControlAmt());
		//
		setC_Currency_ID(original.getC_Currency_ID());
		setC_ConversionType_ID(original.getC_ConversionType_ID());
		setCurrencyRate(original.getCurrencyRate());
		
	//	setDateDoc(original.getDateDoc());
	//	setDateAcct(original.getDateAcct());
	//	setC_Period_ID(original.getC_Period_ID());
	}	//	MJournal
	
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(o==null || getClass() != o.getClass())
			return false;
		MJournal that = (MJournal)o;
		if(this.getGL_Journal_ID() == 0 || that.getGL_Journal_ID() ==0)
			return false;
		super.equals(o);
		return true;
	}
	
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
		if (newDateDoc == null || newDateDoc.length() == 0)
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
		if (newDateAcct == null || newDateAcct.length() == 0)
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
		if (newC_Period_ID == null || newC_Period_ID.length() == 0)
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
		int C_Period_ID = MPeriod.getC_Period_ID(getCtx(), getAD_Org_ID(), DateAcct);
		if (C_Period_ID == 0)
			log.warning("Period not found");
		else
		{
			super.setC_Period_ID(C_Period_ID);
			setRate();
		}
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
		if (period != null)
		{
			if (period.isStandardPeriod()
				&& !period.isInPeriod(dateAcct))
					super.setDateAcct(period.getEndDate());
		}
	}	//	setC_Period_ID

	
	/**
	 * 	Set Currency Info
	 *	@param C_Currency_ID currenct
	 *	@param C_ConversionType_ID type
	 *	@param CurrencyRate rate
	 */
	public void setCurrency (int C_Currency_ID, int C_ConversionType_ID, BigDecimal CurrencyRate)
	{
		if (C_Currency_ID != 0)
			setC_Currency_ID(C_Currency_ID);
		if (C_ConversionType_ID != 0)
			setC_ConversionType_ID(C_ConversionType_ID);
		if (CurrencyRate != null && CurrencyRate.compareTo(Env.ZERO) == 0)
			setCurrencyRate(CurrencyRate);
	}	//	setCurrency

	
	/**
	 * 	Set Rate - Callout.
	 *	@param oldC_ConversionType_ID old
	 *	@param newC_ConversionType_ID new
	 *	@param windowNo window no
	 */
	@UICallout public void setC_ConversionType_ID (String oldC_ConversionType_ID, 
			String newC_ConversionType_ID, int windowNo) throws Exception
	{
		if (newC_ConversionType_ID == null || newC_ConversionType_ID.length() == 0)
			return;
		int C_ConversionType_ID = Integer.parseInt(newC_ConversionType_ID);
		if (C_ConversionType_ID == 0)
			return;
		setC_ConversionType_ID(C_ConversionType_ID);
		setRate();
	}	//	setC_ConversionType_ID
	
	/**
	 * 	Set Currency - Callout.
	 *	@param oldC_Currency_ID old
	 *	@param newC_Currency_ID new
	 *	@param windowNo window no
	 */
	@UICallout public void setC_Currency_ID (String oldC_Currency_ID, 
			String newC_Currency_ID, int windowNo) throws Exception
	{
		if (newC_Currency_ID == null || newC_Currency_ID.length() == 0)
			return;
		int C_Currency_ID = Integer.parseInt(newC_Currency_ID);
		if (C_Currency_ID == 0)
			return;
		setC_Currency_ID(C_Currency_ID);
		setRate();
	}	//	setC_Currency_ID

	/**
	 * 	Set Rate
	 */
	private void setRate()
	{
		//  Source info
		int C_Currency_ID = getC_Currency_ID();
		int C_ConversionType_ID = getC_ConversionType_ID();
		if (C_Currency_ID == 0 || C_ConversionType_ID == 0)
			return;
		Timestamp DateAcct = getDateAcct();
		if (DateAcct == null)
			DateAcct = new Timestamp(System.currentTimeMillis());
		//
		int C_AcctSchema_ID = getC_AcctSchema_ID();
		MAcctSchema as = MAcctSchema.get (getCtx(), C_AcctSchema_ID);
		int AD_Client_ID = getAD_Client_ID();
		int AD_Org_ID = getAD_Org_ID();

		BigDecimal CurrencyRate = MConversionRate.getRate(C_Currency_ID, as.getC_Currency_ID(), 
			DateAcct, C_ConversionType_ID, AD_Client_ID, AD_Org_ID);
		log.fine("rate = " + CurrencyRate);
		if (CurrencyRate == null)
			CurrencyRate = Env.ZERO;
		setCurrencyRate(CurrencyRate);
	}	//	setRate
	
	/**************************************************************************
	 * 	Get Journal Lines
	 * 	@param requery requery
	 *	@return Array of lines
	 */
	public MJournalLine[] getLines (boolean requery)
	{
		ArrayList<MJournalLine> list = new ArrayList<MJournalLine>();
		String sql = "SELECT * FROM GL_JournalLine WHERE GL_Journal_ID=? ORDER BY Line";
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getGL_Journal_ID());
			rs= pstmt.executeQuery();
			while (rs.next())
				list.add(new MJournalLine (getCtx(), rs, get_Trx()));
			
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, "getLines", ex); 
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//
		MJournalLine[] retValue = new MJournalLine[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getLines

	/**
	 * 	Copy Lines from other Journal
	 *	@param fromJournal Journal
	 *	@param dateAcct date used - if null original
	 *	@param typeCR type of copying (C)orrect=negate - (R)everse=flip dr/cr - otherwise just copy
	 *	@return number of lines copied
	 */
	public int copyLinesFrom (MJournal fromJournal, Timestamp dateAcct, char typeCR)
	{
		if (isProcessed() || fromJournal == null)
			return 0;
		int count = 0;
		MJournalLine[] fromLines = fromJournal.getLines(false);
		for (MJournalLine element : fromLines) {
			MJournalLine toLine = new MJournalLine (getCtx(), 0, fromJournal.get_Trx());
			PO.copyValues(element, toLine, getAD_Client_ID(), getAD_Org_ID());
			toLine.setGL_Journal_ID(getGL_Journal_ID());
			//
			if (dateAcct != null)
				toLine.setDateAcct(dateAcct);
			//	Amounts
			if (typeCR == 'C')			//	correct
			{
				toLine.setAmtSourceDr(element.getAmtSourceDr().negate());
				toLine.setAmtSourceCr(element.getAmtSourceCr().negate());
			}
			else if (typeCR == 'R')		//	reverse
			{
				toLine.setAmtSourceDr(element.getAmtSourceCr());
				toLine.setAmtSourceCr(element.getAmtSourceDr());
			}
			toLine.setIsGenerated(true);
			toLine.setProcessed(false);
			if (toLine.save())
				count++;
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "Line difference - JournalLines=" + fromLines.length + " <> Saved=" + count);

		return count;
	}	//	copyLinesFrom

	/**
	 * 	Set Processed.
	 * 	Propergate to Lines/Taxes
	 *	@param processed processed
	 */
	@Override
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String sql = "UPDATE GL_JournalLine SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE GL_Journal_ID= ? " ;
		int noLine = DB.executeUpdate(get_Trx(), sql,getGL_Journal_ID());
		log.fine(processed + " - Lines=" + noLine);
	}	//	setProcessed

	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Imported Journals may not have date
		if (getDateDoc() == null)
		{
			if (getDateAcct() == null)
				setDateDoc(new Timestamp(System.currentTimeMillis()));
			else
				setDateDoc(getDateAcct());
		}
		if (getDateAcct() == null)
			setDateAcct(getDateDoc());
		return true;
	}	//	beforeSave
	
	
	/**
	 * 	After Save.
	 * 	Update Batch Total
	 *	@param newRecord true if new record
	 *	@param success true if success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (!getCtx().isBatchMode() && getGL_JournalBatch_ID() != 0) //
			return updateBatch();
		
		return success;
		
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success true if deleted
	 *	@return true if success
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		
		if (getGL_JournalBatch_ID() != 0)
			return updateBatch();
		
		return success;
	}	//	afterDelete
	
	/**
	 * 	Update Batch total
	 *	@return true if ok
	 */
	private boolean updateBatch()
	{
		String sql = "UPDATE GL_JournalBatch jb"
			+ " SET TotalDr = (SELECT COALESCE(SUM(TotalDr),0)"
				+ " FROM GL_Journal j WHERE j.IsActive='Y' AND jb.GL_JournalBatch_ID=j.GL_JournalBatch_ID), "
			+ " TotalCr = (SELECT COALESCE(SUM(TotalCr),0)"
				+ " FROM GL_Journal j WHERE j.IsActive='Y' AND jb.GL_JournalBatch_ID=j.GL_JournalBatch_ID) "	
			+ "WHERE GL_JournalBatch_ID= ? ";
		int no = DB.executeUpdate(get_Trx(), sql,getGL_JournalBatch_ID());
		if (no != 1)
			log.warning("afterSave - Update Batch #" + no);
		return no == 1;
	}	//	updateBatch
	
	
	/**	Process Message 			*/
	private String		m_processMsg = null;

	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt()
	{
		log.info(toString());
		setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		log.info(toString());
		return true;
	}	//	invalidateIt
	
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());

		//	Lines
		MJournalLine[] lines = getLines(true);
		if (lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocActionConstants.STATUS_Invalid;
		}
		//	Get Period
		MPeriod period = new MPeriod(getCtx(), getC_Period_ID(), null);
		if (period.isStandardPeriod())
			period = MPeriod.getOfOrg(getCtx(), getAD_Org_ID(), getDateAcct());
		if (period == null)
		{
			if (m_processMsg != null)
				return DocActionConstants.STATUS_Invalid;
			log.warning("No Period for " + getDateAcct());
			m_processMsg = "@PeriodNotFound@";
			return DocActionConstants.STATUS_Invalid;
		}
		//	Standard Period
		if (period.getC_Period_ID() != getC_Period_ID()
			&& period.isStandardPeriod())
		{
			m_processMsg = "@PeriodNotValid@";
			return DocActionConstants.STATUS_Invalid;
		}
		m_processMsg = period.isOpen(dt.getDocBaseType(), getDateAcct());
		if (m_processMsg != null)
		{
			log.warning(m_processMsg + ": " + period.getName());
			return DocActionConstants.STATUS_Invalid;
		}

		//	Add up Amounts
		BigDecimal AmtSourceDr = Env.ZERO;
		BigDecimal AmtSourceCr = Env.ZERO;
		for (MJournalLine line : lines) {
			if (!isActive())
				continue;
			if (!IsAccountValid(line))
			{
				return DocActionConstants.STATUS_Invalid;
			}
			//
			if (isManual() && line.isDocControlled())
			{
				m_processMsg = "@DocControlledError@ - @Line@=" + line.getLine()
					+ " - " + line.getAccountElementValue();
				return DocActionConstants.STATUS_Invalid;
			}
			//
			AmtSourceDr = AmtSourceDr.add(line.getAmtSourceDr());
			AmtSourceCr = AmtSourceCr.add(line.getAmtSourceCr());
		}
		setTotalDr(AmtSourceDr);
		setTotalCr(AmtSourceCr);

		//	Control Amount
		if (Env.ZERO.compareTo(getControlAmt()) != 0
			&& getControlAmt().compareTo(getTotalDr()) != 0)
		{
			m_processMsg = "@ControlAmtError@";
			return DocActionConstants.STATUS_Invalid;
		}
		
		//	Unbalanced Jornal & Not Suspense
		if (AmtSourceDr.compareTo(AmtSourceCr) != 0)
		{
			MAcctSchemaGL gl = MAcctSchemaGL.get(getCtx(), getC_AcctSchema_ID());
			if (gl == null || !gl.isUseSuspenseBalancing())
			{
				m_processMsg = "@UnbalancedJornal@";
				return DocActionConstants.STATUS_Invalid;
			}
		}
		
		if (!DOCACTION_Complete.equals(getDocAction())) 
			setDocAction(DOCACTION_Complete);
		return DocActionConstants.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt()
	{
		log.info(toString());
		setIsApproved(true);
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		log.info(toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		return DocActionConstants.STATUS_Completed;
	}	//	completeIt
	
	/**
	 * 	Void Document.
	 * 	@return true if success 
	 */
	public boolean voidIt()
	{
		log.info(toString());
		
		
		if (DOCSTATUS_Drafted.equals(getDocStatus())
				|| DOCSTATUS_Invalid.equals(getDocStatus())) {

			MJournalLine[] lines = getLines(false);
			for (MJournalLine line : lines) {
				BigDecimal AmtSourceCr = line.getAmtSourceCr();
				BigDecimal AmtSourceDr = line.getAmtSourceDr();

				if ((AmtSourceCr.compareTo(Env.ZERO) != 0)
						|| (AmtSourceDr.compareTo(Env.ZERO) != 0)) {
					line.setAmtSourceCr(Env.ZERO);
					line.setAmtSourceDr(Env.ZERO);
					line.setQty(Env.ZERO);

				}
				line.save(get_Trx());
			}			
			
			setProcessed(true);
			setDocAction(DOCACTION_None);
			return true;
		}
	return false;

	
	}	//	voidIt
	
	/**
	 * 	Close Document.
	 * 	Cancel not delivered Qunatities
	 * 	@return true if success 
	 */
	public boolean closeIt()
	{
		log.info(toString());
		if (DOCSTATUS_Completed.equals(getDocStatus())) 
		{
			setProcessed(true);
			setDocAction(DOCACTION_None);
			return true;
		}
		return false;
	}	//	closeIt
	
	/**
	 * 	Reverse Correction (in same batch).
	 * 	As if nothing happened - same date
	 * 	@return true if success 
	 */
	public boolean reverseCorrectIt()
	{
		return reverseCorrectIt(getGL_JournalBatch_ID()) != null;
	}	//	reverseCorrectIt

	/**
	 * 	Reverse Correction.
	 * 	As if nothing happened - same date
	 * 	@param GL_JournalBatch_ID reversal batch
	 * 	@return reversed Journal or null
	 */
	public MJournal reverseCorrectIt (int GL_JournalBatch_ID)
	{
		log.info(toString());
		//	Journal
		MJournal reverse = new MJournal (this);
		reverse.setGL_JournalBatch_ID(GL_JournalBatch_ID);
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
		
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		//	Lines
		MJournalLine[] lines = getLines(true);
		if (lines.length == 0)
		{
			// Should not reach here
			m_processMsg = "@NoLines@";
			return null;
		}
	
		m_processMsg = MPeriod.isOpen(getCtx(), getAD_Client_ID(), getOrgs(lines), reverse.getDateAcct(), dt.getDocBaseType());

		
		if (m_processMsg != null)
			log.warning(m_processMsg);
		//	Get Period
		MPeriod period = new MPeriod(getCtx(), reverse.getC_Period_ID(), null);
		if (period.isStandardPeriod())
			period = MPeriod.getOfOrg(getCtx(), getAD_Org_ID(), reverse.getDateAcct());
		if (period == null)
		{
			if (m_processMsg != null)
				return null;
			log.warning("No Period for " + reverse.getDateAcct());
			m_processMsg = "@PeriodNotFound@";
			return null;
		}
		//	Standard Period
		if (period.getC_Period_ID() != reverse.getC_Period_ID()
			&& period.isStandardPeriod())
		{
			m_processMsg = "@PeriodNotValid@";
			return null;
		}
		m_processMsg = period.isOpen(dt.getDocBaseType(), reverse.getDateAcct());
		if (m_processMsg != null)
		{
			log.warning(m_processMsg + " " + period.getName());
			return null;
		}
		
		if (!reverse.save(get_Trx()))
			return null;
		
		//	Lines
		reverse.copyLinesFrom(this, null, 'C');
		
		if (!DocumentEngine.processIt(reverse, DocActionConstants.ACTION_Complete))
		{
			m_processMsg = "Reversal ERROR: " + reverse.getProcessMsg();
			return null;
		}
		DocumentEngine.processIt(reverse, DocActionConstants.ACTION_Close);
		reverse.setProcessing (false);
		reverse.setDocStatus(DOCSTATUS_Reversed);
		reverse.setDocAction(DOCACTION_None);
		reverse.save(get_Trx());
		//
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return reverse;
	}	//	reverseCorrectionIt
	
	/**
	 * 	Reverse Accrual (sane batch).
	 * 	Flip Dr/Cr - Use Today's date
	 * 	@return true if success 
	 */
	public boolean reverseAccrualIt()
	{
		return reverseAccrualIt (getGL_JournalBatch_ID()) != null;
	}	//	reverseAccrualIt
	
	/**
	 * 	Reverse Accrual.
	 * 	Flip Dr/Cr - Use Today's date
	 * 	@param GL_JournalBatch_ID reversal batch
	 * 	@return reversed journal or null 
	 */
	public MJournal reverseAccrualIt (int GL_JournalBatch_ID)
	{
		log.info(toString());
		//	Journal
		MJournal reverse = new MJournal (this);
		reverse.setGL_JournalBatch_ID(GL_JournalBatch_ID);
		reverse.setDateDoc(new Timestamp(System.currentTimeMillis()));
		reverse.set_ValueNoCheck ("C_Period_ID", null);		//	reset
		reverse.setDateAcct(reverse.getDateDoc());
		//	Reverse indicator
		String description = reverse.getDescription();
		if (description == null)
			description = "** " + getDocumentNo() + " **";
		else
			description += " ** " + getDocumentNo() + " **";
		reverse.setDescription(description);
		
		
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		//	Lines - okay to use original lines since we are only referring to org
		MJournalLine[] lines = getLines(true);
		if (lines.length == 0)
		{
			// Should not reach here
			m_processMsg = "@NoLines@";
			return null;
		}
		//	
		
		m_processMsg = MPeriod.isOpen(getCtx(), getAD_Client_ID(), getOrgs(lines), reverse.getDateAcct(), dt.getDocBaseType());

		if (m_processMsg != null)
			log.warning(m_processMsg);
		//	Get Period
		MPeriod period = new MPeriod(getCtx(), reverse.getC_Period_ID(), null);
		if (period.isStandardPeriod())
			period = MPeriod.getOfOrg(getCtx(), getAD_Org_ID(), reverse.getDateAcct());
		if (period == null)
		{
			if (m_processMsg != null)
				return null;
			log.warning("No Period for " + reverse.getDateAcct());
			m_processMsg = "@PeriodNotFound@";
			return null;
		}
		//	Standard Period
		if (period.getC_Period_ID() != reverse.getC_Period_ID()
			&& period.isStandardPeriod())
		{
			m_processMsg = "@PeriodNotValid@";
			return null;
		}
		m_processMsg = period.isOpen(dt.getDocBaseType(), reverse.getDateAcct());
		if (m_processMsg != null)
		{
			log.warning(m_processMsg + " " + period.getName());
			return null;
		}
		
		if (!reverse.save(get_Trx()))
			return null;
		
		//	Lines
		reverse.copyLinesFrom(this, reverse.getDateAcct(), 'R');

		if (!DocumentEngine.processIt(reverse, DocActionConstants.ACTION_Complete))
		{
			m_processMsg = "Reversal ERROR: " + reverse.getProcessMsg();
			return null;
		}
		DocumentEngine.processIt(reverse, DocActionConstants.ACTION_Close);
		reverse.setProcessing (false);
		reverse.setDocStatus(DOCSTATUS_Reversed);
		reverse.setDocAction(DOCACTION_None);
		reverse.save(get_Trx());
		//
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return reverse;
	}	//	reverseAccrualIt
	
	
	private ArrayList<Integer> getOrgs(MJournalLine[] lines)
	{
		ArrayList<Integer> orgs = new ArrayList<Integer>();
		orgs.add(this.getAD_Org_ID());
		if (lines != null) {
			for (MJournalLine line : lines) {
				int AD_Org_ID = line.getAD_Org_ID();
				if (!orgs.contains(AD_Org_ID))
					orgs.add(AD_Org_ID);
			}
		}
		
		return orgs;

	}
	
	/** 
	 * 	Re-activate
	 * 	@return true if success 
	 */
	public boolean reActivateIt()
	{
		log.info(toString());
		return false;
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
			.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
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
		StringBuffer sb = new StringBuffer ("MJournal[");
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
	 *	@return AD_User_ID (Created)
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
	
	public boolean IsAccountValid (MJournalLine line)
	{
		boolean retVal = true;
		MAccount acct = MAccount.get(getCtx(), line.getC_ValidCombination_ID());
		if (acct.getAD_Org_ID() != 0)
		{
			MOrg org = MOrg.get(getCtx(), acct.getAD_Org_ID());	
			if (!org.isActive())
			{
				m_processMsg = "Element Value " + org.getName()+ " is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}
		}
		if (acct.getAccount_ID() != 0)
		{
			MElementValue ev = new MElementValue(getCtx(), acct.getAccount_ID(), get_Trx());
			if (!ev.isActive())
			{
				m_processMsg = "Element Value " + ev.getName()+ " is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}

		}
		if (acct.getC_SubAcct_ID() != 0)
		{
			X_C_SubAcct sa = new X_C_SubAcct(getCtx(), acct.getC_SubAcct_ID(), get_Trx());
			if (!sa.isActive())
			{
				m_processMsg = "Element Value " + sa.getName()+ " is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}

		}
		if (acct.getM_Product_ID() != 0)
		{
			X_M_Product product = new X_M_Product (getCtx(), acct.getM_Product_ID(), get_Trx());
			if (!product.isActive())
			{
				m_processMsg = "Element Value " + product.getName()+ " is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}

		}
		if (acct.getC_BPartner_ID() != 0)
		{
			X_C_BPartner partner = new X_C_BPartner (getCtx(), acct.getC_BPartner_ID(),get_Trx());
			if (!partner.isActive())
			{
				m_processMsg = "Element Value " + partner.getName()+ " is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}

		}
		if (acct.getAD_OrgTrx_ID() != 0)
		{
			MOrg org = MOrg.get(getCtx(), acct.getAD_Org_ID());
			if (!org.isActive())
			{
				m_processMsg = "Element Value " + org.getName()+ " is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}

		}
		if (acct.getC_LocFrom_ID() != 0)
		{
			MLocation loc = new MLocation(getCtx(), acct.getC_LocFrom_ID(), get_Trx());	//	in Trx!
			if (!loc.isActive())
			{
				m_processMsg = "Element Value Location From is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}

		}
		if (acct.getC_LocTo_ID() != 0)
		{
			MLocation loc = new MLocation(getCtx(), acct.getC_LocFrom_ID(), get_Trx());	//	in Trx!
			if (!loc.isActive())
			{
				m_processMsg = "Element Value Location to is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}

		}
		if (acct.getC_SalesRegion_ID() != 0)
		{
			MSalesRegion loc = new MSalesRegion(getCtx(), acct.getC_SalesRegion_ID(), get_Trx());
			if (!loc.isActive())
			{
				m_processMsg = "Element Value Sales Region is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}

		}
		if (acct.getC_Project_ID() != 0)
		{
			X_C_Project project = new X_C_Project (getCtx(), acct.getC_Project_ID(), get_Trx());
			if (!project.isActive())
			{
				m_processMsg = "Element Value " + project.getName()+ " is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}
		}
		if (acct.getC_Campaign_ID() != 0)
		{
			X_C_Campaign campaign = new X_C_Campaign (getCtx(), acct.getC_Campaign_ID(), get_Trx());
			if (!campaign.isActive())
			{
				m_processMsg = "Element Value " + campaign.getName()+ " is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}
		}
		if (acct.getC_Activity_ID() != 0)
		{
			X_C_Activity act = new X_C_Activity (getCtx(), acct.getC_Activity_ID(), get_Trx());
			if (!act.isActive())
			{
				m_processMsg = "Element Value " + act.getName()+ " is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}
		}
		if (acct.getUser1_ID() != 0)
		{
			MElementValue ev = new MElementValue(getCtx(), acct.getUser1_ID(), get_Trx());
			if (!ev.isActive())
			{
				m_processMsg = "Element Value " + ev.getName()+ " is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}
		}
		if (acct.getUser2_ID() != 0)
		{
			MElementValue ev = new MElementValue(getCtx(), acct.getUser2_ID(), get_Trx());
			if (!ev.isActive())
			{
				m_processMsg = "Element Value " + ev.getName()+ " is not Active for account " + acct.getCombination() ;
				retVal = false;
				return retVal;
			}
		}

		return retVal;
	}
	
	@Override
	public void setProcessMsg(String processMsg) {
		m_processMsg = processMsg;
	}

	@Override
	public String getDocBaseType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getDocumentDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryParams getLineOrgsQueryInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	
}	//	MJournal
