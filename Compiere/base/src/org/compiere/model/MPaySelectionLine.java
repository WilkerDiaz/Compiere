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

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.api.*;
import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *	Payment Selection Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPaySelectionLine.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MPaySelectionLine extends X_C_PaySelectionLine
{
    /** Logger for class MPaySelectionLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPaySelectionLine.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_PaySelectionLine_ID id
	 *	@param trx transaction
	 */
	public MPaySelectionLine (Ctx ctx, int C_PaySelectionLine_ID, Trx trx)
	{
		super(ctx, C_PaySelectionLine_ID, trx);
		if (C_PaySelectionLine_ID == 0)
		{
		//	setC_PaySelection_ID (0);
		//	setPaymentRule (null);	// S
		//	setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_PaySelectionLine WHERE C_PaySelection_ID=@C_PaySelection_ID@
		//	setC_Invoice_ID (0);
			setIsSOTrx (false);
			setOpenAmt(Env.ZERO);
			setPayAmt (Env.ZERO);
			setDiscountAmt(Env.ZERO);
			setDifferenceAmt (Env.ZERO);
			setIsManual (false);
		}
	}	//	MPaySelectionLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPaySelectionLine(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPaySelectionLine

	/**
	 * 	Parent Constructor
	 *	@param ps parent
	 *	@param Line line
	 *	@param PaymentRule payment rule
	 */
	public MPaySelectionLine (MPaySelection ps, int Line, String PaymentRule)
	{
		this (ps.getCtx(), 0, ps.get_Trx());
		setClientOrg(ps);
		setC_PaySelection_ID(ps.getC_PaySelection_ID());
		setLine(Line);
		setPaymentRule(PaymentRule);
	}	//	MPaySelectionLine

	/**
	 * 	Get Payment Selection Line for Invoice and Check
	 *	@param ctx context
	 *	@param C_Invoice_ID id
	 *  @param M_PaySelectionCheck_ID id
	 *	@param trx transaction
	 *	@return pay selection line for invoice or null
	 */
	public static MPaySelectionLine getOfInvoiceCheck (Ctx ctx, int C_Invoice_ID, int C_PaySelectionCheck_ID, Trx trx)
	{
		MPaySelectionLine retValue = null;
		String sql = "SELECT * FROM C_PaySelectionLine WHERE C_Invoice_ID=? AND C_PaySelectionCheck_ID=? AND COALESCE(IsCancelled,'N')='N'";
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, C_Invoice_ID);
			pstmt.setInt (2, C_PaySelectionCheck_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MPaySelectionLine psl = new MPaySelectionLine (ctx, rs, trx);
				if (retValue == null)
					retValue = psl;
				else if (!retValue.isProcessed() && psl.isProcessed())
					retValue = psl;
				count++;
			}
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
		if (count > 1)
			s_log.warning ("More then one for C_Invoice_ID=" + C_Invoice_ID + ", C_PaySelectionCheck_ID=" + C_PaySelectionCheck_ID);
		return retValue;
	}	//	getOfPayment

	
	/**	Invoice					*/
	private MInvoice 	m_invoice = null;

	/** Logger								*/
	static private CLogger	s_log = CLogger.getCLogger (MPaySelectionLine.class);

	
	/**
	 * 	Set Invoice Info
	 *	@param C_Invoice_ID invoice
	 *	@param isSOTrx sales p_trx
	 *	@param PayAmt payment
	 *	@param OpenAmt open
	 *	@param DiscountAmt discount
	 */
	public void setInvoice (int C_Invoice_ID, boolean isSOTrx, BigDecimal OpenAmt, 
		BigDecimal PayAmt, BigDecimal DiscountAmt)
	{
		setC_Invoice_ID (C_Invoice_ID);
		setIsSOTrx(isSOTrx);
		setOpenAmt(OpenAmt);
		setPayAmt (PayAmt);
		setDiscountAmt(DiscountAmt);
		setDifferenceAmt(OpenAmt.subtract(PayAmt).subtract(DiscountAmt));
	}	//	setInvoice

	/**
	 * 	Set Invoice - Callout
	 *	@param oldC_Invoice_ID old BP
	 *	@param newC_Invoice_ID new BP
	 *	@param windowNo window no
	 */
	@UICallout public void setC_Invoice_ID (String oldC_Invoice_ID, 
			String newC_Invoice_ID, int windowNo) throws Exception
	{
		if (newC_Invoice_ID == null || newC_Invoice_ID.length() == 0)
			return;
		int C_Invoice_ID = Integer.parseInt(newC_Invoice_ID);
		//  reset as dependent fields get reset
		//p_changeVO.setContext(getCtx(), windowNo, "C_Invoice_ID", C_Invoice_ID);
		getCtx().setContext(windowNo, "C_Invoice_ID", C_Invoice_ID);
		setC_Invoice_ID(C_Invoice_ID);
		if (C_Invoice_ID == 0)
		{
			setPayAmt(Env.ZERO);
			setDiscountAmt(Env.ZERO);
			setDifferenceAmt(Env.ZERO);
			return;
		}

		int C_BankAccount_ID = getCtx().getContextAsInt(windowNo, "C_BankAccount_ID");
		Timestamp PayDate = new Timestamp(getCtx().getContextAsTime(windowNo, "PayDate"));

		BigDecimal OpenAmt = Env.ZERO;
		BigDecimal DiscountAmt = Env.ZERO;
		boolean IsSOTrx = Boolean.FALSE;
		String sql = "SELECT currencyConvert(invoiceOpen(i.C_Invoice_ID, 0), i.C_Currency_ID,"
				+ "ba.C_Currency_ID, i.DateInvoiced, i.C_ConversionType_ID, i.AD_Client_ID, i.AD_Org_ID),"
			+ " paymentTermDiscount(i.GrandTotal,i.C_Currency_ID,i.C_PaymentTerm_ID,i.DateInvoiced, ?), i.IsSOTrx " // #1
			+ "FROM C_Invoice_v i, C_BankAccount ba "
			+ "WHERE i.C_Invoice_ID=? AND ba.C_BankAccount_ID=?";	//	#2..3
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setTimestamp(1, PayDate);
			pstmt.setInt(2, C_Invoice_ID);
			pstmt.setInt(3, C_BankAccount_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				OpenAmt = rs.getBigDecimal(1);
				DiscountAmt = rs.getBigDecimal(2);
				IsSOTrx = "Y".equals(rs.getString(3));
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		log.fine(" - OpenAmt=" + OpenAmt + " (Invoice=" + C_Invoice_ID + ",BankAcct=" + C_BankAccount_ID + ")");
		setInvoice(C_Invoice_ID, IsSOTrx, OpenAmt, OpenAmt.subtract(DiscountAmt), DiscountAmt);
	}	//	setC_Invoice_ID
	
	/**
	 * 	Set Pay Amt - Callout
	 *	@param oldPayAmt old value
	 *	@param newPayAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setPayAmt (String oldPayAmt, 
			String newPayAmt, int windowNo) throws Exception
	{
		if (newPayAmt == null || newPayAmt.length() == 0)
			return;
		BigDecimal PayAmt = PO.convertToBigDecimal(newPayAmt);

		BigDecimal OpenAmt = getOpenAmt();
		BigDecimal DiscountAmt = getDiscountAmt();
		BigDecimal DifferenceAmt = OpenAmt.subtract(PayAmt).subtract(DiscountAmt);

		//	get invoice info
		int C_Invoice_ID = getC_Invoice_ID();
		if (C_Invoice_ID == 0)
		{
			PayAmt = Env.ZERO;
			DifferenceAmt = Env.ZERO;
			DiscountAmt = Env.ZERO;
			setDiscountAmt(DiscountAmt);
		}
		log.fine("OpenAmt=" + OpenAmt + " - PayAmt=" + PayAmt
			+ ", Discount=" + DiscountAmt + ", Difference=" + DifferenceAmt);
		
		setPayAmt(PayAmt);
		setDifferenceAmt(DifferenceAmt);
	}	//	setPayAmt

	
	/**
	 * 	Get Invoice
	 *	@return invoice
	 */
	public MInvoice getInvoice()
	{
		if (m_invoice == null)
			m_invoice = new MInvoice (getCtx(), getC_Invoice_ID(), get_Trx());
		return m_invoice;
	}	//	getInvoice
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		setDifferenceAmt(getOpenAmt().subtract(getPayAmt()).subtract(getDiscountAmt()));
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		setHeader();
		return success;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return sucess
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		setHeader();
		return success;
	}	//	afterDelete
	
	/**
	 * 	Recalculate Header Sum
	 */
	private void setHeader()
	{
		//	Update Header
		String sql = "UPDATE C_PaySelection ps "
			+ "SET TotalAmt = (SELECT COALESCE(SUM(psl.PayAmt),0) "
				+ "FROM C_PaySelectionLine psl "
				+ "WHERE ps.C_PaySelection_ID=psl.C_PaySelection_ID AND psl.IsActive='Y') "
			+ "WHERE C_PaySelection_ID=?" ;
		
		DB.executeUpdate(get_Trx(), sql,getC_PaySelection_ID());
		
	}	//	setHeader
	
	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MPaySelectionLine[");
		sb.append(get_ID()).append(",C_Invoice_ID=").append(getC_Invoice_ID())
			.append(",PayAmt=").append(getPayAmt())
			.append(",DifferenceAmt=").append(getDifferenceAmt())
			.append("]");
		return sb.toString();
	}	//	toString

}	//	MPaySelectionLine
