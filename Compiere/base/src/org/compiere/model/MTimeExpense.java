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

import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.util.Env.*;
import org.compiere.vos.*;

/**
 * 	Time + Expense Model
 *
 *	@author Jorg Janke
 *	@version $Id: MTimeExpense.java,v 1.4 2006/07/30 00:51:03 jjanke Exp $
 */
public class MTimeExpense extends X_S_TimeExpense implements DocAction
{
    /** Logger for class MTimeExpense */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTimeExpense.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param S_TimeExpense_ID id
	 *	@param trx transaction
	 */
	public MTimeExpense (Ctx ctx, int S_TimeExpense_ID, Trx trx)
	{
		super (ctx, S_TimeExpense_ID, trx);
		if (S_TimeExpense_ID == 0)
		{
		//	setC_BPartner_ID (0);
			setDateReport (new Timestamp (System.currentTimeMillis ()));
		//	setDocumentNo (null);
			setIsApproved (false);
		//	setM_PriceList_ID (0);
		//	setM_Warehouse_ID (0);
			super.setProcessed (false);
			setProcessing(false);
		}
	}	//	MTimeExpense

	/**
	 * 	Load Constructor
	 * 	@param ctx context
	 * 	@param rs result set
	 *	@param trx transaction
	 */
	public MTimeExpense (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MTimeExpense

	/** Default Locator				*/
	private int					m_M_Locator_ID = 0;
	/**	Lines						*/
	private MTimeExpenseLine[]	m_lines = null;
	/** Cached User					*/
	private int					m_AD_User_ID = 0;


	/**
	 * 	Get Lines Convenience Wrapper
	 *	@return array of lines
	 */
	public MTimeExpenseLine[] getLines ()
	{
		return getLines(true);
	}

	/**
	 * 	Get Lines
	 * 	@param requery true requeries
	 *	@return array of lines
	 */
	public MTimeExpenseLine[] getLines (boolean requery)
	{
		if ((m_lines != null) && !requery)
			return m_lines;
		//
		int C_Currency_ID = getC_Currency_ID();
		ArrayList<MTimeExpenseLine> list = new ArrayList<MTimeExpenseLine>();
		//
		String sql = "SELECT * FROM S_TimeExpenseLine WHERE S_TimeExpense_ID=? ORDER BY Line";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getS_TimeExpense_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MTimeExpenseLine te = new MTimeExpenseLine(getCtx(), rs, get_Trx());
				te.setC_Currency_Report_ID(C_Currency_ID);
				list.add(te);
			}
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
		m_lines = new MTimeExpenseLine[list.size()];
		list.toArray(m_lines);
		return m_lines;
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
	 *	Get Default Locator (from Warehouse)
	 *	@return locator
	 */
	public int getM_Locator_ID()
	{
		if (m_M_Locator_ID != 0)
			return m_M_Locator_ID;
		//
		String sql = "SELECT M_Locator_ID FROM M_Locator "
			+ "WHERE M_Warehouse_ID=? AND IsActive='Y' ORDER BY ASCII(IsDefault) DESC, Created";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, getM_Warehouse_ID());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				m_M_Locator_ID = rs.getInt(1);
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, "getM_Locator_ID", ex);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		return m_M_Locator_ID;
	}	//	getM_Locator_ID

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
		String sql = "UPDATE S_TimeExpenseLine SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE S_TimeExpense_ID= ?";
		int noLine = DB.executeUpdate(get_Trx(), sql,getS_TimeExpense_ID());
		m_lines = null;
		log.fine(processed + " - Lines=" + noLine);
	}	//	setProcessed

	/**
	 * 	Get Document Info
	 *	@return document info
	 */
	public String getDocumentInfo()
	{
		return Msg.getElement(getCtx(), "S_TimeExpense_ID") + " " + getDocumentNo();
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
		MTimeExpenseLine[] lines = getLines(false);

		//	Add up Amounts
		BigDecimal amt = Env.ZERO;
		for (MTimeExpenseLine line : lines) {
			amt = amt.add(line.getApprovalAmt());
		}
		setApprovalAmt(amt);

		//	Invoiced but no BP
		for (MTimeExpenseLine line : lines) {
			if (line.isInvoiced() && (line.getC_BPartner_ID() == 0))
			{
				m_processMsg = "@Line@ " + line.getLine() + ": Invoiced, but no Business Partner";
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
		getLines(true);

		return DocActionConstants.STATUS_Completed;
	}	//	completeIt

	/**
	 * 	Void Document.
	 * 	Same as Close.
	 * 	@return true if success
	 */
	public boolean voidIt()
	{
		log.info("voidIt - " + toString());
		return DocumentEngine.processIt(this, DocActionConstants.ACTION_Close);
	}	//	voidIt

	/**
	 * 	Close Document.
	 * 	Cancel not delivered Qunatities
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info("closeIt - " + toString());

		//	Close Not delivered Qty
	//	setDocAction(DOCACTION_None);
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
	 * 	Reverse Accrual - none
	 * 	@return false
	 */
	public boolean reverseAccrualIt()
	{
		log.info("reverseAccrualIt - " + toString());
		return false;
	}	//	reverseAccrualIt

	/**
	 * 	Re-activate
	 * 	@return true if success
	 */
	public boolean reActivateIt()
	{
		log.info("reActivateIt - " + toString());
	//	setProcessed(false);
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
			.append(Msg.translate(getCtx(),"ApprovalAmt")).append("=").append(getApprovalAmt())
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
		if (m_AD_User_ID != 0)
			return m_AD_User_ID;
		if (getC_BPartner_ID() != 0)
		{
			MUser[] users = MUser.getOfBPartner(getCtx(), getC_BPartner_ID());
			if (users.length > 0)
			{
				m_AD_User_ID = users[0].getAD_User_ID();
				return m_AD_User_ID;
			}
		}
		return getCreatedBy();
	}	//	getDoc_User_ID


	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID()
	{
		MPriceList pl = MPriceList.get(getCtx(), getM_PriceList_ID(), get_Trx());
		return pl.getC_Currency_ID();
	}	//	getC_Currency_ID

	@Override
	public void setProcessMsg(String processMsg) {
		m_processMsg = processMsg;
	}

	@Override
	public String getDocBaseType() {
		return MDocBaseType.DOCBASETYPE_APInvoice;
	}

	@Override
	public Timestamp getDocumentDate() {
		return getDateReport();
	}

	@Override
	public QueryParams getLineOrgsQueryInfo() {
		return new QueryParams("SELECT DISTINCT AD_Org_ID FROM S_TimeExpenseLine WHERE S_TimeExpense_ID = ?",
				new Object[] { getS_TimeExpense_ID() });
	}
	
}	//	MTimeExpense
