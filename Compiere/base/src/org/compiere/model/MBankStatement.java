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
import org.compiere.framework.PO;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.util.Env.*;
import org.compiere.vos.*;

/**
*	Bank Statement Model
*
*	@author Eldir Tomassen/Jorg Janke
*	@version $Id: MBankStatement.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
*/
public class MBankStatement extends X_C_BankStatement implements DocAction
{
    /** Logger for class MBankStatement */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBankStatement.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankStatement_ID id
	 *	@param trx transaction
	 */
	public MBankStatement (Ctx ctx, int C_BankStatement_ID, Trx trx)
	{
		super (ctx, C_BankStatement_ID, trx);
		if (C_BankStatement_ID == 0)
		{
		//	setC_BankAccount_ID (0);	//	parent
			setStatementDate (new Timestamp(System.currentTimeMillis()));	// @Date@
			setDocAction (DOCACTION_Complete);	// CO
			setDocStatus (DOCSTATUS_Drafted);	// DR
			setBeginningBalance(Env.ZERO);
			setStatementDifference(Env.ZERO);
			setEndingBalance (Env.ZERO);
			setIsApproved (false);	// N
			setIsManual (true);	// Y
			setPosted (false);	// N
			super.setProcessed (false);
		}
	}	//	MBankStatement

	/**
	 * 	Load Constructor
	 * 	@param ctx Current context
	 * 	@param rs result set
	 *	@param trx transaction
	 */
	public MBankStatement(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MBankStatement

 	/**
 	 * 	Parent Constructor
	 *	@param account Bank Account
 	 * 	@param isManual Manual statement
 	 **/
	public MBankStatement (MBankAccount account, boolean isManual)
	{
		this (account.getCtx(), 0, account.get_Trx());
		setClientOrg(account);
		setC_BankAccount_ID(account.getC_BankAccount_ID());
		setStatementDate(new Timestamp(System.currentTimeMillis()));
		setBeginningBalance(account.getCurrentBalance());
		setName(getStatementDate().toString());
		setIsManual(isManual);
	}	//	MBankStatement

	
	
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(o==null || getClass() != o.getClass())
			return false;
		MBankStatement that = (MBankStatement)o;
		if(this.getC_BankStatement_ID()== 0 || that.getC_BankStatement_ID() ==0)
			return false;
		super.equals(o);
		return true;
	}
	
	
	public MBankStatement (MBankAccount account, X_I_BankStatement imp)
	{
		this (imp.getCtx(), 0, imp.get_Trx());
		
	    PO.copyValues(imp, this, imp.getAD_Client_ID(), imp.getAD_Org_ID());
	    
		setC_BankAccount_ID(account.getC_BankAccount_ID());
		
		setBeginningBalance(account.getCurrentBalance());
		
		setIsManual(false);
	

		if (imp.getStatementDate() == null)
		{
			setStatementDate(new Timestamp(System.currentTimeMillis()));
			imp.setStatementDate(getStatementDate());
		}
		
		if (imp.getName() == null)
		{
			setName(getStatementDate().toString());
			imp.setName(getStatementDate().toString());
		}


		setDescription(imp.getDescription());
		setEftStatementReference(imp.getEftStatementReference());
		setEftStatementDate(imp.getEftStatementDate());
		
		setEndingBalance(Env.ZERO);

	}	//	MBankStatement

	
	/**
	 * 	Create a new Bank Statement
	 *	@param account Bank Account
	 */
	public MBankStatement(MBankAccount account)
	{
		this(account, false);
	}	//	MBankStatement

	/**	Lines							*/
	private MBankStatementLine[] 	m_lines = null;

 	/**
 	 * 	Get Bank Statement Lines
 	 * 	@param requery requery
 	 *	@return line array
 	 */
 	public MBankStatementLine[] getLines (boolean requery)
 	{
		if ((m_lines != null) && !requery)
			return m_lines;
		//
 		ArrayList<MBankStatementLine> list = new ArrayList<MBankStatementLine>();
 		String sql = "SELECT * FROM C_BankStatementLine "
 			+ "WHERE C_BankStatement_ID=?"
 			+ "ORDER BY Line";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_BankStatement_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add (new MBankStatementLine(getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getLines", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		MBankStatementLine[] retValue = new MBankStatementLine[list.size()];
		list.toArray(retValue);
		return retValue;
 	}	//	getLines

 	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription

	/**
	 * 	Set Processed.
	 * 	Propagate to Lines/Taxes
	 *	@param processed processed
	 */
	@Override
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String sql = "UPDATE C_BankStatementLine SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE C_BankStatement_ID=? " ;
		int noLine = DB.executeUpdate(get_Trx(), sql,getC_BankStatement_ID());
		m_lines = null;
		log.fine("setProcessed - " + processed + " - Lines=" + noLine);
	}	//	setProcessed

	/**
	 * 	Set Org - Callout
	 *	@param oldAD_Org_ID old org
	 *	@param newAD_Org_ID new org
	 *	@param windowNo window no
	 */
	@UICallout public void setAD_Org_ID (String oldAD_Org_ID,
			String newAD_Org_ID, int windowNo) throws Exception
	{
		// reset bank account when org is changed
		setC_BankAccount_ID(1);
	}	//	setAD_Org_ID

	/**
	 * 	Get Bank Account
	 *	@return bank Account
	 */
	public MBankAccount getBankAccount()
	{
		return MBankAccount.get(getCtx(), getC_BankAccount_ID());
	}	//	getBankAccount

	/**
	 *	Set Bank Account
	 *	@param C_BankAccount_ID bank Account
	 */
	@Override
	public void setC_BankAccount_ID(int C_BankAccount_ID)
	{
		super.setC_BankAccount_ID(C_BankAccount_ID);
	}

	/**
	 * 	Set Bank Account - Callout
	 *	@param oldC_BankAccount_ID old Bank
	 *	@param newC_BankAccount_ID new Bank
	 *	@param windowNo window no
	 */
	@UICallout public void setC_BankAccount_ID (String oldC_BankAccount_ID,
			String newC_BankAccount_ID, int windowNo) throws Exception
	{
		if ((newC_BankAccount_ID == null) || (newC_BankAccount_ID.length() == 0))
			return;
		int C_BankAccount_ID = Integer.parseInt(newC_BankAccount_ID);
		if (C_BankAccount_ID == 0)
			return;
		setC_BankAccount_ID(C_BankAccount_ID);
		//
		MBankAccount ba = getBankAccount();
		setBeginningBalance(ba.getCurrentBalance());
		
		//Setting Virtual Field
		p_changeVO.addChangedValue("CurrentBalance", ba.getCurrentBalance());
		p_changeVO.addChangedValue("C_Currency_Bank_ID", ba.getC_Currency_ID());

	}	//	setC_BankAccount_ID

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
		return getBankAccount().getName() + " " + getDocumentNo();
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
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (!getCtx().isBatchMode())
		{
			MBankAccount ba=new MBankAccount(getCtx(),getC_BankAccount_ID(),get_Trx());
			BigDecimal total = Env.ZERO;
			MBankStatementLine[] lines = getLines(true);
			
			for (MBankStatementLine line : lines) {
				BigDecimal lineAmt = Env.ZERO;
				lineAmt= MConversionRate.convert (getCtx(),
						line.getStmtAmt(), line.getC_Currency_ID(), ba.getC_Currency_ID(),
						line.getDateAcct(),line.getC_ConversionType_ID(), line.getAD_Client_ID(),line.getAD_Org_ID());
				if (lineAmt == null)
				{
					log.saveError("Error","Could not convert C_Currency_ID=" + getC_Currency_ID()
						+ " to base C_Currency_ID=" + MClient.get(Env.getCtx()).getC_Currency_ID());
					return false;
				}
				
				total = total.add(lineAmt);
			}
			setStatementDifference(total);
			if (!isProcessed() && !(getBeginningBalance().compareTo(ba.getCurrentBalance())==0))
			{
				log.saveInfo("",Msg.getMsg(getCtx(), "BankBeginBalanceUpdated"));

			}
			setEndingBalance(getBeginningBalance().add(getStatementDifference()));

		}

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
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt

	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
		MBankStatementLine[] lines = getLines(true);

		for (MBankStatementLine line : lines) {
			if( (line.getC_Invoice_ID() != 0) && (line.getC_Payment_ID() == 0) )
			{
				m_processMsg = "@LineHasInvoiceButNoPayment@";
				return DocActionConstants.STATUS_Invalid;
			}
		}

		
		ArrayList<Integer> orgs = new ArrayList<Integer>();
		orgs.add(this.getAD_Org_ID());
	
		//	Lines
		Timestamp minDate = getStatementDate();
		Timestamp maxDate = minDate;
		for (MBankStatementLine line : lines) {
			int AD_Org_ID = line.getAD_Org_ID();
			if (!orgs.contains(AD_Org_ID))
				orgs.add(AD_Org_ID);
			
			if (line.getDateAcct().before(minDate))
				minDate = line.getDateAcct();
			if (line.getDateAcct().after(maxDate))
				maxDate = line.getDateAcct();
		}
		
		m_processMsg = MPeriod.isOpen(getCtx(), getAD_Client_ID(), orgs, minDate, MDocBaseType.DOCBASETYPE_BankStatement);
		if (m_processMsg == null)
			m_processMsg = MPeriod.isOpen(getCtx(), getAD_Client_ID(), orgs, maxDate, MDocBaseType.DOCBASETYPE_BankStatement);
		if (m_processMsg != null)
			return DocActionConstants.STATUS_Invalid;

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
		//	Set Payment reconciled
		MBankStatementLine[] lines = getLines(true);
		for (MBankStatementLine line : lines)
		{
			if (line.getC_Payment_ID() != 0)
			{
				MPayment payment = new MPayment (getCtx(), line.getC_Payment_ID(), get_Trx());
				payment.setIsReconciled(true);
				payment.save(get_Trx());
			}
		}
		//	Update Bank Account
		MBankAccount ba = MBankAccount.get(getCtx(), getC_BankAccount_ID());
		ba.setCurrentBalance(getEndingBalance());
		ba.save(get_Trx());

		return DocActionConstants.STATUS_Completed;
	}	//	completeIt

	/**
	 * 	Void Document.
	 * 	@return false
	 */
	public boolean voidIt()
	{
		log.info(toString());
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			setDocAction(DOCACTION_None);
			return false;
		}

		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
			|| DOCSTATUS_Invalid.equals(getDocStatus())
			|| DOCSTATUS_InProgress.equals(getDocStatus())
			|| DOCSTATUS_Approved.equals(getDocStatus())
			|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
			;
		//	Std Period open?
		else
		{
			m_processMsg = DocumentEngine.isPeriodOpen(this);
			if (m_processMsg != null)
				return false;
			if (MFactAcct.delete(Table_ID, getC_BankStatement_ID(), get_Trx()) < 0)
				return false;	//	could not delete
		}

		//	Set lines to 0
		MBankStatementLine[] lines = getLines(true);
		for (MBankStatementLine line : lines) {
			//	BigDecimal old = line.getStmtAmt();
			if (line.getStmtAmt().compareTo(Env.ZERO) != 0)
			{
				String description = Msg.getMsg(getCtx(), "Voided") + " ("
					+ Msg.translate(getCtx(), "StmtAmt") + "=" + line.getStmtAmt();
				if (line.getTrxAmt().compareTo(Env.ZERO) != 0)
					description += ", " + Msg.translate(getCtx(), "TrxAmt") + "=" + line.getTrxAmt();
				if (line.getChargeAmt().compareTo(Env.ZERO) != 0)
					description += ", " + Msg.translate(getCtx(), "ChargeAmt") + "=" + line.getChargeAmt();
				if (line.getInterestAmt().compareTo(Env.ZERO) != 0)
					description += ", " + Msg.translate(getCtx(), "InterestAmt") + "=" + line.getInterestAmt();
				description += ")";
				line.addDescription(description);
				//
				line.setStmtAmt(Env.ZERO);
				line.setTrxAmt(Env.ZERO);
				line.setChargeAmt(Env.ZERO);
				line.setInterestAmt(Env.ZERO);
				line.save(get_Trx());
				//
				if (line.getC_Payment_ID() != 0)
				{
					MPayment payment = new MPayment (getCtx(), line.getC_Payment_ID(), get_Trx());
					payment.setIsReconciled(false);
					payment.save(get_Trx());
				}
			}
		}
		addDescription(Msg.getMsg(getCtx(), "Voided"));
		BigDecimal voidedDifference = getStatementDifference();
		setStatementDifference(Env.ZERO);

		if (isProcessed())
		{
			//	Update Bank Account only if document was Completed previously
			MBankAccount ba = MBankAccount.get(getCtx(), getC_BankAccount_ID());
			ba.setCurrentBalance( ba.getCurrentBalance().subtract( voidedDifference ) );
			ba.save(get_Trx());
		}

		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt

	/**
	 * 	Close Document.
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info("closeIt - " + toString());

		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction
	 * 	@return false
	 */
	public boolean reverseCorrectIt()
	{
		log.info("reverseCorrectIt - " + toString());
		return false;
	}	//	reverseCorrectionIt

	/**
	 * 	Reverse Accrual
	 * 	@return false
	 */
	public boolean reverseAccrualIt()
	{
		log.info("reverseAccrualIt - " + toString());
		return false;
	}	//	reverseAccrualIt

	/**
	 * 	Re-activate
	 * 	@return false
	 */
	public boolean reActivateIt()
	{
		log.info("reActivateIt - " + toString());
		return false;
	}	//	reActivateIt


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
			.append(Msg.translate(getCtx(),"StatementDifference")).append("=").append(getStatementDifference())
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
		return getUpdatedBy();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount.
	 * 	Statement Difference
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return getStatementDifference();
	}	//	getApprovalAmt

	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID()
	{
	//	MPriceList pl = MPriceList.get(getCtx(), getM_PriceList_ID());
	//	return pl.getC_Currency_ID();
		return 0;
	}	//	getC_Currency_ID

	@Override
	public void setProcessMsg(String processMsg) {
		m_processMsg = processMsg;
	}

	@Override
	public String getDocBaseType() {
		return MDocBaseType.DOCBASETYPE_BankStatement;
	}

	@Override
	public Timestamp getDocumentDate() {
		return getStatementDate();
	}

	@Override
	public QueryParams getLineOrgsQueryInfo() {
		return new QueryParams("SELECT DISTINCT AD_Org_ID FROM C_BankStatementLine WHERE C_BankStatement_ID = ?",
				new Object[] { getC_BankStatement_ID() });
	}
	
 }	//	MBankStatement
