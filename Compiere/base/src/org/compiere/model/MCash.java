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
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.util.Env.*;
import org.compiere.vos.*;

/**
 *	Cash Journal Model
 *
 *  @author Jorg Janke
 *  @version $Id: MCash.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MCash extends X_C_Cash implements DocAction
{
    /** Logger for class MCash */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCash.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static MCash get (Ctx ctx, int AD_Org_ID,
			Timestamp dateAcct, int C_Currency_ID, Trx trx)
	{
		return MCash.get(ctx, AD_Org_ID, dateAcct,C_Currency_ID,0, trx);
	}

	/**
	 * 	Get Cash Journal for currency, org and date
	 *	@param ctx context
	 *	@param C_Currency_ID currency
	 *	@param AD_Org_ID org
	 *	@param dateAcct date
	 *	@param trx transaction
	 *	@return cash
	 */
	public static MCash get (Ctx ctx, int AD_Org_ID,
		Timestamp dateAcct, int C_Currency_ID, int C_CashBook_ID, Trx trx)
	{
		MCash retValue = null;
		//	Existing Journal
		String sql = "SELECT * FROM C_Cash c "
			+ "WHERE c.AD_Org_ID=?"						
			+ " AND TRUNC(c.StatementDate, 'DD')=?"			
			+ " AND c.Processed='N'";
		if(C_CashBook_ID >0)
			sql+= " AND c.C_CashBook_ID = ? ";      
			
		sql+= " AND EXISTS (SELECT * FROM C_CashBook cb "
				        + " WHERE c.C_CashBook_ID=cb.C_CashBook_ID AND cb.AD_Org_ID=c.AD_Org_ID"
				        + " AND cb.C_Currency_ID=?)";		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int counter =1;
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (counter++, AD_Org_ID);
			pstmt.setTimestamp (counter++, TimeUtil.getDay(dateAcct));
			if(C_CashBook_ID > 0)
				pstmt.setInt(counter++ ,C_CashBook_ID);
			pstmt.setInt (counter++, C_Currency_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MCash (ctx, rs, trx);
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
		if (retValue != null)
			return retValue;

		//	Get CashBook
		MCashBook cb = null;
		if(C_CashBook_ID > 0)
			cb = MCashBook.get(ctx, C_CashBook_ID);
		else
			cb = MCashBook.get (ctx, AD_Org_ID, C_Currency_ID);
		if (cb == null)
		{
			s_log.warning("No CashBook for AD_Org_ID=" + AD_Org_ID + ", C_Currency_ID=" + C_Currency_ID);
			return null;
		}

		//	Create New Journal
		retValue = new MCash (cb, dateAcct);
		retValue.save(trx);
		return retValue;
	}	//	get

	/**
	 * 	Get Cash Journal for CashBook and date
	 *	@param ctx context
	 *	@param C_CashBook_ID cashbook
	 *	@param dateAcct date
	 *	@param trx transaction
	 *	@return cash
	 */
	public static MCash get (Ctx ctx, int C_CashBook_ID,
		Timestamp dateAcct, Trx trx)
	{
		MCash retValue = null;
		//	Existing Journal
		String sql = "SELECT * FROM C_Cash c "
			+ "WHERE c.C_CashBook_ID=?"					//	#1
			+ " AND TRUNC(c.StatementDate,'DD')=?"			//	#2
			+ " AND c.Processed='N'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, C_CashBook_ID);
			pstmt.setTimestamp (2, TimeUtil.getDay(dateAcct));
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MCash (ctx, rs, trx);
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
		if (retValue != null)
			return retValue;

		//	Get CashBook
		MCashBook cb = new MCashBook (ctx, C_CashBook_ID, trx);
		if (cb.get_ID() ==0)
		{
			s_log.warning("Not found C_CashBook_ID=" + C_CashBook_ID);
			return null;
		}

		//	Create New Journal
		retValue = new MCash (cb, dateAcct);
		retValue.save(trx);
		return retValue;
	}	//	get

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MCash.class);


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Cash_ID id
	 *	@param trx transaction
	 */
	public MCash (Ctx ctx, int C_Cash_ID, Trx trx)
	{
		super (ctx, C_Cash_ID, trx);
		if (C_Cash_ID == 0)
		{
		//	setC_CashBook_ID (0);		//	FK
			setBeginningBalance (Env.ZERO);
			setEndingBalance (Env.ZERO);
			setStatementDifference(Env.ZERO);
			setDocAction(DOCACTION_Complete);
			setDocStatus(DOCSTATUS_Drafted);
			//
			Timestamp today = TimeUtil.getDay(System.currentTimeMillis());
			setStatementDate (today);	// @#Date@
			setDateAcct (today);	// @#Date@
			String name = DisplayType.getDateFormat(DisplayTypeConstants.Date).format(today)
				+ " " + MOrg.get(ctx, getAD_Org_ID()).getValue();
			setName (name);
			setIsApproved(false);
			setPosted (false);	// N
			setProcessed (false);
		}
	}	//	MCash

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MCash (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MCash

	/**
	 * 	Parent Constructor
	 *	@param cb cash book
	 *	@param today date - if null today
	 */
	public MCash (MCashBook cb, Timestamp today)
	{
		this (cb.getCtx(), 0, cb.get_Trx());
		setClientOrg(cb);
		setC_CashBook_ID(cb.getC_CashBook_ID());
		if (today != null)
		{
			setStatementDate (today);
			setDateAcct (today);
			String name = DisplayType.getDateFormat(DisplayTypeConstants.Date).format(today)
				+ " " + cb.getName();
			setName (name);
		}
		m_book = cb;
	}	//	MCash

	/**	Lines					*/
	private MCashLine[]		m_lines = null;
	/** CashBook				*/
	private MCashBook		m_book = null;

	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return lines
	 */
	public MCashLine[] getLines (boolean requery)
	{
		if ((m_lines != null) && !requery)
			return m_lines;
		ArrayList<MCashLine> list = new ArrayList<MCashLine>();
		String sql = "SELECT * FROM C_CashLine WHERE C_Cash_ID=? ORDER BY Line";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_Cash_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MCashLine (getCtx(), rs, get_Trx()));
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

		m_lines = new MCashLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines

	/**
	 * 	Get Cash Book
	 *	@return cash book
	 */
	public MCashBook getCashBook()
	{
		if (m_book == null)
			m_book = MCashBook.get(getCtx(), getC_CashBook_ID());
		return m_book;
	}	//	getCashBook

	/**
	 * 	Get Document No
	 *	@return name
	 */
	public String getDocumentNo()
	{
		return getName();
	}	//	getDocumentNo

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		return Msg.getElement(getCtx(), "C_Cash_ID") + " " + getDocumentNo();
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
	 * 	Set StatementDate - Callout
	 *	@param oldStatementDate old
	 *	@param newStatementDate new
	 *	@param windowNo window no
	 */
	@UICallout public void setStatementDate (String oldStatementDate,
			String newStatementDate, int windowNo) throws Exception
	{
		if ((newStatementDate == null) || (newStatementDate.length() == 0))
			return;
		Timestamp statementDate = PO.convertToTimestamp(newStatementDate);
		if (statementDate == null)
			return;
		setStatementDate(statementDate);
	}	//	setStatementDate

	/**
	 *	Set Statement Date and Acct Date
	 */
	@Override
	public void setStatementDate(Timestamp statementDate)
	{
		super.setStatementDate(statementDate);
		super.setDateAcct(statementDate);
	}	//	setStatementDate

	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		setAD_Org_ID(getCashBook().getAD_Org_ID());
		if (getAD_Org_ID() == 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@AD_Org_ID@"));
			return false;
		}
		//	Calculate End Balance
		setEndingBalance(getBeginningBalance().add(getStatementDifference()));
		return true;
	}	//	beforeSave


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
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt

	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
		//	Lines
		MCashLine[] lines = getLines(false);
		//	Add up Amounts
		BigDecimal difference = Env.ZERO;
		int C_Currency_ID = getC_Currency_ID();
		for (MCashLine line : lines) {
			if (!line.isActive())
				continue;
			if (C_Currency_ID == line.getC_Currency_ID())
				difference = difference.add(line.getAmount());
			else
			{
				BigDecimal amt = MConversionRate.convert(getCtx(), line.getAmount(),
					line.getC_Currency_ID(), C_Currency_ID, getDateAcct(), line.getC_ConversionType_ID(),
					getAD_Client_ID(), getAD_Org_ID());
				if (amt == null)
				{
					m_processMsg = "No Conversion Rate found - @C_CashLine_ID@= " + line.getLine();
					return DocActionConstants.STATUS_Invalid;
				}
				difference = difference.add(amt);
			}
		}
		setStatementDifference(difference);
	//	setEndingBalance(getBeginningBalance().add(getStatementDifference()));
		//
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
		boolean allocCreated = false;
		
		//	Allocation Header
		MAllocationHdr alloc = null;
		
		//
		MCashLine[] lines = getLines(true);
		for (MCashLine cLine : lines) {
			if (X_C_CashLine.CASHTYPE_Invoice.equals(cLine.getCashType()))
			{
				if (!allocCreated)
				{
					alloc = new MAllocationHdr(getCtx(), false,
							getDateAcct(), getC_Currency_ID(),
							Msg.translate(getCtx(), "C_Cash_ID") + ": " + getName(), get_Trx());
					alloc.setAD_Org_ID(getAD_Org_ID());
					if (!alloc.save())
					{
						m_processMsg = "Could not create Allocation Hdr";
						return DocActionConstants.STATUS_Invalid;
					}
					allocCreated = true;
				}
					
				boolean differentCurrency = getC_Currency_ID() != cLine.getC_Currency_ID();
				MAllocationHdr hdr = alloc;
				if (differentCurrency)
				{
					hdr = new MAllocationHdr(getCtx(), false,
						getDateAcct(), cLine.getC_Currency_ID(),
						Msg.translate(getCtx(), "C_Cash_ID") + ": " + getName(), get_Trx());
					hdr.setAD_Org_ID(getAD_Org_ID());
					if (!hdr.save())
					{
						m_processMsg = "Could not create Allocation Hdr";
						return DocActionConstants.STATUS_Invalid;
					}
				}
				//	Allocation Line
				MAllocationLine aLine = new MAllocationLine (hdr, cLine.getAmount(),
					cLine.getDiscountAmt(), cLine.getWriteOffAmt(), cLine.getOverUnderAmt());
				aLine.setAD_Org_ID(cLine.getAD_Org_ID());
				aLine.setC_Invoice_ID(cLine.getC_Invoice_ID());
				aLine.setC_CashLine_ID(cLine.getC_CashLine_ID());
				if (!aLine.save())
				{
					m_processMsg = "Could not create Allocation Line";
					return DocActionConstants.STATUS_Invalid;
				}
				if (differentCurrency)
				{
					//	Should start WF
					DocumentEngine.processIt(hdr, DocActionConstants.ACTION_Complete);
					hdr.save();
				}
			}
			else if (X_C_CashLine.CASHTYPE_BankAccountTransfer.equals(cLine.getCashType()))
			{
				//	Payment just as intermediate info
				MPayment pay = new MPayment (getCtx(), 0, get_Trx());
				pay.setAD_Org_ID(getAD_Org_ID());
				String documentNo = getName();
				pay.setDocumentNo(documentNo);
				pay.setR_PnRef(documentNo);
				pay.set_Value("TrxType", "X");		//	Transfer
				pay.set_Value("TenderType", "X");
				//
				pay.setC_BankAccount_ID(cLine.getC_BankAccount_ID());
				pay.setC_DocType_ID(true);	//	Receipt
				pay.setDateTrx(getStatementDate());
				pay.setDateAcct(getDateAcct());
				pay.setC_ConversionType_ID(cLine.getC_ConversionType_ID());
				pay.setAmount(cLine.getC_Currency_ID(), cLine.getAmount().negate());	//	Transfer
				pay.setDescription(cLine.getDescription());
				pay.setDocStatus(X_C_Payment.DOCSTATUS_Closed);
				pay.setDocAction(X_C_Payment.DOCACTION_None);
				pay.setPosted(true);
				pay.setIsAllocated(true);	//	Has No Allocation!
				pay.setProcessed(true);
				if (!pay.save())
				{
					m_processMsg = "Could not create Payment";
					return DocActionConstants.STATUS_Invalid;
				}
			}
		}
		
		if (allocCreated)
		{
			//	Should start WF
			DocumentEngine.processIt(alloc, DocActionConstants.ACTION_Complete);
			alloc.save();
		}

		return DocActionConstants.STATUS_Completed;
	}	//	completeIt

	/**
	 * 	Void Document.
	 * 	Same as Close.
	 * 	@return true if success
	 */
	public boolean voidIt()
	{
		log.info(toString());
		setDocAction(DOCACTION_None);
		return false;
	}	//	voidIt

	/**
	 * 	Close Document.
	 * 	Cancel not delivered Quantities
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info(toString());
		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction
	 * 	@return true if success
	 */
	public boolean reverseCorrectIt()
	{
		log.info(toString());
		return false;
	}	//	reverseCorrectionIt

	/**
	 * 	Reverse Accrual - none
	 * 	@return true if success
	 */
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		return false;
	}	//	reverseAccrualIt

	/**
	 * 	Re-activate
	 * 	@return true if success
	 */
	public boolean reActivateIt()
	{
		log.info(toString());
		setProcessed(false);
		return DocumentEngine.processIt(this, DocActionConstants.ACTION_Reverse_Correct);
	}	//	reActivateIt

	/**
	 * 	Set Processed
	 *	@param processed processed
	 */
	@Override
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		String sql = "UPDATE C_CashLine SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE C_Cash_ID=? ";
		int noLine = DB.executeUpdate (get_Trx(), sql,getC_Cash_ID());
		m_lines = null;
		log.fine(processed + " - Lines=" + noLine);
	}	//	setProcessed

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MCash[");
		sb.append (get_ID ())
			.append ("-").append (getName())
			.append(", Balance=").append(getBeginningBalance())
			.append("->").append(getEndingBalance())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getName());
		//	: Total Lines = 123.00 (#1)
		sb.append(": ")
			.append(Msg.translate(getCtx(),"BeginningBalance")).append("=").append(getBeginningBalance())
			.append(",")
			.append(Msg.translate(getCtx(),"EndingBalance")).append("=").append(getEndingBalance())
			.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if ((getDescription() != null) && (getDescription().length() > 0))
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

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
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getCreatedBy();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount difference
	 */
	public BigDecimal getApprovalAmt()
	{
		return getStatementDifference();
	}	//	getApprovalAmt

	/**
	 * 	Get Currency
	 *	@return Currency
	 */
	public int getC_Currency_ID ()
	{
		return getCashBook().getC_Currency_ID();
	}	//	getC_Currency_ID

	@Override
	public void setProcessMsg(String processMsg) {
		m_processMsg = processMsg;
	}

	@Override
	public String getDocBaseType() {
		return MDocBaseType.DOCBASETYPE_CashJournal;
	}

	@Override
	public Timestamp getDocumentDate() {
		return getDateAcct();
	}

	@Override
	public QueryParams getLineOrgsQueryInfo() {
		return new QueryParams("SELECT DISTINCT AD_Org_ID FROM C_CashLine WHERE C_Cash_ID = ?",
				new Object[] { getC_Cash_ID() });
	}
	
}	//	MCash
