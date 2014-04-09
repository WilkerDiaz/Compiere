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

import org.compiere.common.constants.*;
import org.compiere.util.*;

/**
 * 	Callout for Allocate Payments
 *
 *  @author Jorg Janke
 *  @version $Id: CalloutPaymentAllocate.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class CalloutPaymentAllocate extends CalloutEngine
{
	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());

	/**
	 *  Payment_Invoice.
	 *  when Invoice selected
	 *  - set InvoiceAmt = invoiceOpen
	 *  	- DiscountAmt = C_Invoice_Discount (ID, DateTrx)
	 *   	- Amount = invoiceOpen (ID) - Discount
	 * 		- WriteOffAmt,OverUnderAmt = 0
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String invoice (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_Invoice_ID = (Integer)value;
		if (isCalloutActive()		//	assuming it is resetting value
			|| C_Invoice_ID == null || C_Invoice_ID.intValue() == 0)
			return "";

		//	Check Payment
		int C_Payment_ID = ctx.getContextAsInt( WindowNo, "C_Payment_ID");
		MPayment payment = new MPayment (ctx, C_Payment_ID, null);
		if (payment.getC_Charge_ID() != 0 || payment.getC_Invoice_ID() != 0
			|| payment.getC_Order_ID() != 0)
			return Msg.getMsg(ctx, "PaymentIsAllocated");

		setCalloutActive(true);
		//
		mTab.setValue("DiscountAmt", Env.ZERO);
		mTab.setValue("WriteOffAmt", Env.ZERO);
		mTab.setValue("OverUnderAmt", Env.ZERO);

		int C_InvoicePaySchedule_ID = 0;
		if (ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_Invoice_ID") == C_Invoice_ID.intValue()
			&& ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_InvoicePaySchedule_ID") != 0)
			C_InvoicePaySchedule_ID = ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_InvoicePaySchedule_ID");

		//  Payment Date
		Timestamp ts = new Timestamp(ctx.getContextAsTime( WindowNo, "DateTrx"));
		String sql = "SELECT C_BPartner_ID,C_Currency_ID,"		//	1..2
			+ " invoiceOpen(C_Invoice_ID, ?),"					//	3		#1
			+ " invoiceDiscount(C_Invoice_ID,?,?), IsSOTrx "	//	4..5	#2/3
			+ "FROM C_Invoice WHERE C_Invoice_ID=?";			//			#4
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_InvoicePaySchedule_ID);
			pstmt.setTimestamp(2, ts);
			pstmt.setInt(3, C_InvoicePaySchedule_ID);
			pstmt.setInt(4, C_Invoice_ID.intValue());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
			//	mTab.setValue("C_BPartner_ID", Integer.valueOf(rs.getInt(1)));
			//	int C_Currency_ID = rs.getInt(2);					//	Set Invoice Currency
			//	mTab.setValue("C_Currency_ID", Integer.valueOf(C_Currency_ID));
				//
				BigDecimal InvoiceOpen = rs.getBigDecimal(3);		//	Set Invoice OPen Amount
				if (InvoiceOpen == null)
					InvoiceOpen = Env.ZERO;
				BigDecimal DiscountAmt = rs.getBigDecimal(4);		//	Set Discount Amt
				if (DiscountAmt == null)
					DiscountAmt = Env.ZERO;
				MInvoice invoice = new MInvoice(ctx, C_Invoice_ID, null);
				MDocType docType = MDocType.get(ctx, invoice.getC_DocType_ID());
				if (docType.isReturnTrx())
				{
					// Adjust discount amount for credit memos. Invoice Open Amt is already adjusted.
					DiscountAmt = DiscountAmt.negate();
				}

				mTab.setValue("InvoiceAmt", InvoiceOpen);
				mTab.setValue("Amount", InvoiceOpen.subtract(DiscountAmt));
				mTab.setValue("DiscountAmt", DiscountAmt);
				//  reset as dependent fields get reset
				ctx.setContext( WindowNo, "C_Invoice_ID", C_Invoice_ID.toString());
				mTab.setValue("C_Invoice_ID", C_Invoice_ID);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			setCalloutActive(false);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		setCalloutActive(false);
		return "";
	}	//	invoice

	/**
	 *  Payment_Amounts.
	 *	Change of:
	 *		- IsOverUnderPayment -> set OverUnderAmt to 0
	 *		- C_Currency_ID, C_ConvesionRate_ID -> convert all
	 *		- PayAmt, DiscountAmt, WriteOffAmt, OverUnderAmt -> PayAmt
	 *			make sure that add up to InvoiceOpenAmt
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @param oldValue Old Value
	 *  @return null or error message
	 */
	public String amounts (Ctx ctx, int WindowNo, GridTab mTab, GridField mField,
		Object value, Object oldValue)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		//	No Invoice
		int C_Invoice_ID = ctx.getContextAsInt( WindowNo, "C_Invoice_ID");
		if (C_Invoice_ID == 0)
			return "";
		setCalloutActive(true);
		//	Get Info from Tab
		BigDecimal Amount = (BigDecimal)mTab.getValue("Amount");
		BigDecimal DiscountAmt = (BigDecimal)mTab.getValue("DiscountAmt");
		BigDecimal WriteOffAmt = (BigDecimal)mTab.getValue("WriteOffAmt");
		BigDecimal OverUnderAmt = (BigDecimal)mTab.getValue("OverUnderAmt");
		BigDecimal InvoiceAmt = (BigDecimal)mTab.getValue("InvoiceAmt");
		log.fine("Amt=" + Amount + ", Discount=" + DiscountAmt
			+ ", WriteOff=" + WriteOffAmt + ", OverUnder=" + OverUnderAmt
			+ ", Invoice=" + InvoiceAmt);

		//	Changed Column
		String colName = mField.getColumnName();
		//  PayAmt - calculate write off
		if (colName.equals("Amount"))
		{
			WriteOffAmt = InvoiceAmt.subtract(Amount).subtract(DiscountAmt).subtract(OverUnderAmt);
			mTab.setValue("WriteOffAmt", WriteOffAmt);
		}
		else    //  calculate Amount
		{
			Amount = InvoiceAmt.subtract(DiscountAmt).subtract(WriteOffAmt).subtract(OverUnderAmt);
			mTab.setValue("Amount", Amount);
		}

		setCalloutActive(false);
		return "";
	}	//	amounts


}	//	CalloutPaymentAllocate
