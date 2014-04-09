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
import java.util.*;
import java.util.logging.*;

import org.compiere.api.*;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.util.*;

/**
 * 	Payment Allocate Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPaymentAllocate.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MPaymentAllocate extends X_C_PaymentAllocate
{
    /** Logger for class MPaymentAllocate */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPaymentAllocate.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get active Payment Allocation of Payment
	 *	@param parent payment
	 *	@return array of allocations
	 */
	public static MPaymentAllocate[] get (MPayment parent)
	{
		ArrayList<MPaymentAllocate> list = new ArrayList<MPaymentAllocate>();
		String sql = "SELECT * FROM C_PaymentAllocate WHERE C_Payment_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, parent.get_Trx());
			pstmt.setInt (1, parent.getC_Payment_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MPaymentAllocate (parent.getCtx(), rs, parent.get_Trx()));
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MPaymentAllocate[] retValue = new MPaymentAllocate[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get

	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MPaymentAllocate.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_PaymentAllocate_ID id
	 *	@param trx p_trx
	 */
	public MPaymentAllocate (Ctx ctx, int C_PaymentAllocate_ID, Trx trx)
	{
		super (ctx, C_PaymentAllocate_ID, trx);
		if (C_PaymentAllocate_ID == 0)
		{
		//	setC_Payment_ID (0);	//	Parent
		//	setC_Invoice_ID (0);
			setAmount (Env.ZERO);
			setDiscountAmt (Env.ZERO);
			setOverUnderAmt (Env.ZERO);
			setWriteOffAmt (Env.ZERO);
			setInvoiceAmt(Env.ZERO);
		}	
	}	//	MPaymentAllocate

	/**	The Invoice				*/
	private MInvoice	m_invoice = null;
	
	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MPaymentAllocate (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MPaymentAllocate
	
	/**
	 * 	Set C_Invoice_ID
	 *	@param C_Invoice_ID id
	 */
	@Override
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		super.setC_Invoice_ID (C_Invoice_ID);
		m_invoice = null;
	}	//	setC_Invoice_ID
	
	/**
	 * 	Get Invoice
	 *	@return invoice
	 */
	public MInvoice getInvoice()
	{
		if (m_invoice == null && getC_Invoice_ID() != 0)
			m_invoice = new MInvoice(getCtx(), getC_Invoice_ID(), get_Trx());
		return m_invoice;
	}	//	getInvoice
	
	/**
	 * 	Get BPartner of Invoice
	 *	@return bp
	 */
	public int getC_BPartner_ID()
	{
		if (m_invoice == null)
			getInvoice();
		if (m_invoice == null)
			return 0;
		return m_invoice.getC_BPartner_ID();
	}	//	getC_BPartner_ID
	
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
		setC_Invoice_ID(C_Invoice_ID);
		if (C_Invoice_ID == 0)
			return;
		//	Check Payment
		int C_Payment_ID = getC_Payment_ID();
		MPayment payment = new MPayment (getCtx(), C_Payment_ID, null);
		if (payment.getC_Charge_ID() != 0 
			|| payment.getC_Invoice_ID() != 0 
			|| payment.getC_Order_ID() != 0)
		{
			p_changeVO.addError(Msg.getMsg(getCtx(), "PaymentIsAllocated"));
			return;
		}

		setDiscountAmt(Env.ZERO);
		setWriteOffAmt(Env.ZERO);
		setOverUnderAmt(Env.ZERO);

		int C_InvoicePaySchedule_ID = 0;
		if (getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_Invoice_ID") == C_Invoice_ID
			&& getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_InvoicePaySchedule_ID") != 0)
			C_InvoicePaySchedule_ID = getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_InvoicePaySchedule_ID");

		//  Payment Date
		Timestamp ts = new Timestamp(getCtx().getContextAsTime(windowNo, "DateTrx"));
		if (ts == null)
			ts = new Timestamp(System.currentTimeMillis());
		//
		String sql = "SELECT C_BPartner_ID,C_Currency_ID,"		//	1..2
			+ " invoiceOpen(C_Invoice_ID, ?),"					//	3		#1
			+ " invoiceDiscount(C_Invoice_ID,?,?), IsSOTrx "	//	4..5	#2/3
			+ "FROM C_Invoice WHERE C_Invoice_ID=?";			//			#4
		int C_Currency_ID = 0;		//	Invoice Currency
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, C_InvoicePaySchedule_ID);
			pstmt.setTimestamp(2, ts);
			pstmt.setInt(3, C_InvoicePaySchedule_ID);
			pstmt.setInt(4, C_Invoice_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
			//	setC_BPartner_ID(rs.getInt(1));
				C_Currency_ID = rs.getInt(2);					//	Set Invoice Currency
			//	setC_Currency_ID(C_Currency_ID);
				//
				BigDecimal InvoiceOpen = rs.getBigDecimal(3);	//	Set Invoice Open Amount
				if (InvoiceOpen == null)
					InvoiceOpen = Env.ZERO;
				BigDecimal DiscountAmt = rs.getBigDecimal(4);	//	Set Discount Amt
				if (DiscountAmt == null)
					DiscountAmt = Env.ZERO;
				MInvoice invoice = new MInvoice(getCtx(), C_Invoice_ID, get_Trx());
				MDocType docType = MDocType.get(getCtx(), invoice.getC_DocType_ID());
				if (docType.isReturnTrx())
				{
					// Adjust discount amount for credit memos. Invoice Open Amt is already adjusted.
					DiscountAmt = DiscountAmt.negate();
				}
				//
				setInvoiceAmt(InvoiceOpen);
				setAmount(InvoiceOpen.subtract(DiscountAmt));
				setDiscountAmt(DiscountAmt);
				//  reset as dependent fields get reset
				getCtx().setContext(windowNo, "C_Invoice_ID", C_Invoice_ID);
				//IsSOTrx, Project
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
		//	Check Invoice/Payment Currency - may not be an issue(??)
		if (C_Currency_ID != 0)
		{
			int currency_ID = getCtx().getContextAsInt(windowNo, "C_Currency_ID");
			if (currency_ID != C_Currency_ID)
			{
				String msg = Msg.parseTranslation(getCtx(), "@C_Currency_ID@: @C_Invoice_ID@ <> @C_Payment_ID@");
				p_changeVO.addError(msg);
			}
		}		
	}	//	setC_Invoice_ID

	
	/**
	 * 	Set Allocation Amt - Callout
	 *	@param oldAmount old value
	 *	@param newAmount new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setAmount (String oldAmount, 
			String newAmount, int windowNo) throws Exception
	{
		if (newAmount == null || newAmount.length() == 0)
			return;
		BigDecimal Amount = PO.convertToBigDecimal(newAmount);
		setAmount(Amount);
		checkAmt(windowNo, "PayAmt");
	}	//	setPayAmt

	/**
	 * 	Set Discount - Callout
	 *	@param oldDiscountAmt old value
	 *	@param newDiscountAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setDiscountAmt (String oldDiscountAmt, 
			String newDiscountAmt, int windowNo) throws Exception
	{
		if (newDiscountAmt == null || newDiscountAmt.length() == 0)
			return;
		BigDecimal DiscountAmt = PO.convertToBigDecimal(newDiscountAmt);
		setDiscountAmt(DiscountAmt);
		checkAmt(windowNo, "DiscountAmt");
	}	//	setDiscountAmt

	/**
	 * 	Set Over Under Amt - Callout
	 *	@param oldOverUnderAmt old value
	 *	@param newOverUnderAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setOverUnderAmt (String oldOverUnderAmt, 
			String newOverUnderAmt, int windowNo) throws Exception
	{
		if (newOverUnderAmt == null || newOverUnderAmt.length() == 0)
			return;
		BigDecimal OverUnderAmt = PO.convertToBigDecimal(newOverUnderAmt);
		setOverUnderAmt(OverUnderAmt);
		checkAmt(windowNo, "OverUnderAmt");
	}	//	setOverUnderAmt
	
	/**
	 * 	Set WriteOff Amt - Callout
	 *	@param oldWriteOffAmt old value
	 *	@param newWriteOffAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setWriteOffAmt (String oldWriteOffAmt, 
			String newWriteOffAmt, int windowNo) throws Exception
	{
		if (newWriteOffAmt == null || newWriteOffAmt.length() == 0)
			return;
		BigDecimal WriteOffAmt = PO.convertToBigDecimal(newWriteOffAmt);
		setWriteOffAmt(WriteOffAmt);
		checkAmt(windowNo, "WriteOffAmt");
	}	//	setWriteOffAmt
	
	/**
	 * 	Check amount (Callout)
	 *	@param windowNo window
	 *	@param columnName columnName
	 */
	private void checkAmt (int windowNo, String columnName)
	{
		int C_Invoice_ID = getC_Invoice_ID();
		//	No Payment
		if (C_Invoice_ID == 0)
			return;

		//	Get Info from Tab
		BigDecimal Amount = getAmount();
		BigDecimal DiscountAmt = getDiscountAmt();
		BigDecimal WriteOffAmt = getWriteOffAmt();
		BigDecimal OverUnderAmt = getOverUnderAmt();
		BigDecimal InvoiceAmt = getInvoiceAmt();
		log.fine("Amt=" + Amount + ", Discount=" + DiscountAmt
			+ ", WriteOff=" + WriteOffAmt + ", OverUnder=" + OverUnderAmt
			+ ", Invoice=" + InvoiceAmt);

		//  PayAmt - calculate write off
		if (columnName.equals("Amount"))
		{
			WriteOffAmt = InvoiceAmt.subtract(Amount).subtract(DiscountAmt).subtract(OverUnderAmt);
			setWriteOffAmt(WriteOffAmt);
		}
		else    //  calculate Amount
		{
			Amount = InvoiceAmt.subtract(DiscountAmt).subtract(WriteOffAmt).subtract(OverUnderAmt);
			setAmount(Amount);
		}
	}	//	checkAmt

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		MPayment payment = new MPayment (getCtx(), getC_Payment_ID(), get_Trx());
		if ((newRecord || is_ValueChanged("C_Invoice_ID"))
			&& (payment.getC_Charge_ID() != 0 
				|| payment.getC_Invoice_ID() != 0 
				|| payment.getC_Order_ID() != 0))
		{
			log.saveError("PaymentIsAllocated", "");
			return false;
		}
		
		BigDecimal check = getAmount()
			.add(getDiscountAmt())
			.add(getWriteOffAmt())
			.add(getOverUnderAmt());
		if (check.compareTo(getInvoiceAmt()) != 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), 
				"@InvoiceAmt@(" + getInvoiceAmt()
				+ ") <> @Totals@(" + check + ")"));
			return false;
		}
		
		//	Org
		if (newRecord || is_ValueChanged("C_Invoice_ID"))
		{
			getInvoice();
			if (m_invoice != null)
				setAD_Org_ID(m_invoice.getAD_Org_ID());
		}
		
		return true;
	}	//	beforeSave
	
}	//	MPaymentAllocate
