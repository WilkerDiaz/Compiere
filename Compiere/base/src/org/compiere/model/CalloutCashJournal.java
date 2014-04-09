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

import org.compiere.common.constants.EnvConstants;
import org.compiere.util.*;

/**
 *	Cash Book Journal Callouts
 *
 *  @author Jorg Janke
 *  @version $Id: CalloutCashJournal.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class CalloutCashJournal extends CalloutEngine
{
	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());

	/**
	 *  Cash Journal Line Invoice.
	 *  when Invoice selected
	 *  - set C_Currency, DiscountAnt, Amount, WriteOffAmt
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String invoice (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		setCalloutActive(true);

		Integer C_Invoice_ID = (Integer)value;
		if (C_Invoice_ID == null || C_Invoice_ID.intValue() == 0)
		{
			mTab.setValue("C_Currency_ID", null);
			setCalloutActive(false);
			return "";
		}
		int C_InvoicePaySchedule_ID = 0;
		if (ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_Invoice_ID") == C_Invoice_ID.intValue()
			&& ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_InvoicePaySchedule_ID") != 0)
			C_InvoicePaySchedule_ID = ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_InvoicePaySchedule_ID");

		//  Date
		Timestamp ts = new Timestamp(ctx.getContextAsTime(WindowNo, "DateAcct"));     //  from C_Cash
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
				mTab.setValue("C_Currency_ID", Integer.valueOf(rs.getInt(2)));
				BigDecimal PayAmt = rs.getBigDecimal(3);
				BigDecimal DiscountAmt = rs.getBigDecimal(4);
				boolean isSOTrx = "Y".equals(rs.getString(5));
				if (!isSOTrx)
				{
					PayAmt = PayAmt.negate();
					DiscountAmt = DiscountAmt.negate();
				}
				//
				mTab.setValue("Amount", PayAmt.subtract(DiscountAmt));
				mTab.setValue("DiscountAmt", DiscountAmt);
				mTab.setValue("WriteOffAmt", Env.ZERO);
				ctx.setContext( WindowNo, "InvTotalAmt", PayAmt.toString());
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "invoice", e);
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
	}	//	CashJournal_Invoice


	/**
	 *  Cash Journal Line Invoice Amounts.
	 *  when DiscountAnt, Amount, WriteOffAmt change
	 *  making sure that add up to InvTotalAmt (created by CashJournal_Invoice)
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String amounts (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		//  Needs to be Invoice
		if (isCalloutActive() || !"I".equals(mTab.getValue("CashType")))
			return "";
		//  Check, if InvTotalAmt exists
		String total = ctx.getContext( WindowNo, "InvTotalAmt");
		if (total == null || total.length() == 0)
			return "";
		BigDecimal InvTotalAmt = new BigDecimal(total);
		setCalloutActive(true);

		BigDecimal PayAmt = (BigDecimal)mTab.getValue("Amount");
		BigDecimal DiscountAmt = (BigDecimal)mTab.getValue("DiscountAmt");
		BigDecimal WriteOffAmt = (BigDecimal)mTab.getValue("WriteOffAmt");
		boolean isOverUnderPayment = "Y".equals(mTab.get_ValueAsString("IsOverUnderPayment"));
		BigDecimal OverUnderAmt = (BigDecimal)mTab.getValue("OverUnderAmt");
		
		if(WriteOffAmt == null)
			WriteOffAmt = BigDecimal.ZERO;
		if(DiscountAmt==null)
			DiscountAmt = BigDecimal.ZERO;
		if(PayAmt == null)
			PayAmt = BigDecimal.ZERO;
		if(OverUnderAmt==null)
			OverUnderAmt = BigDecimal.ZERO;
		String colName = mField.getColumnName();
		log.fine(colName + " - Invoice=" + InvTotalAmt
			+ " - Amount=" + PayAmt + ", Discount=" + DiscountAmt + ", WriteOff=" + WriteOffAmt);

		if(isOverUnderPayment)
		{
			if (colName.equals("Amount"))
			{
				OverUnderAmt = InvTotalAmt.subtract(PayAmt).subtract(DiscountAmt).subtract(WriteOffAmt);
				mTab.setValue("OverUnderAmt", OverUnderAmt);
			}			
			else if(colName.equals("OverUnderAmt")
					||colName.equals("WriteOffAmt")
					||colName.equals("DiscountAmt"))
			{
				PayAmt = InvTotalAmt.subtract(DiscountAmt).subtract(OverUnderAmt).subtract(WriteOffAmt);
				mTab.setValue("Amount", PayAmt);
			}
		}
		else //No Over/Under Payment
		{
			mTab.setValue("OverUnderAmt", BigDecimal.ZERO);
			//  Amount - calculate write off
			if (colName.equals("Amount"))
			{
				WriteOffAmt = InvTotalAmt.subtract(PayAmt).subtract(DiscountAmt);
				mTab.setValue("WriteOffAmt", WriteOffAmt);
			}
			else    //  calculate PayAmt
			{
				PayAmt = InvTotalAmt.subtract(DiscountAmt).subtract(WriteOffAmt);
				mTab.setValue("Amount", PayAmt);
			}
		}
		setCalloutActive(false);
		return "";
	}	//	amounts
	
	public String CashType (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		setCalloutActive(true);
		
		String CashType = (String)value;
		if(CashType == null || CashType.length()==0)
		{
			setCalloutActive(false);
			return "";
		}
		
		if(!(CashType.equals("E")||CashType.equals("R")))
		{
			setCalloutActive(false);
			return "";
		}
		
		String sql = " SELECT MIN(a.C_Cash_ExpenseReceiptType_ID) "
			       + " FROM C_Cash_ExpenseReceiptType a "
			       + " WHERE IsDefault = 'Y' "
			       + " AND AD_Client_ID = ? "
			       + " AND IsExpense = CASE  WHEN ? = 'E' THEN 'Y' "
			                             + " WHEN ? = 'R' THEN 'N' "
			                             + " ELSE 'Z' END ";

		int C_Cash_ExpenseReceiptType_ID = QueryUtil.getSQLValue(null, sql, ctx.getAD_Client_ID(),CashType,CashType);
		if(C_Cash_ExpenseReceiptType_ID !=0)
			mTab.setValue("C_Cash_ExpenseReceiptType_ID", C_Cash_ExpenseReceiptType_ID);
		
		setCalloutActive(false);
		return "";

	}
}	//	CalloutCashJournal
