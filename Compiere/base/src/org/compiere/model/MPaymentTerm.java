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

import org.compiere.util.*;


/**
 *	Payment Term Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPaymentTerm.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MPaymentTerm extends X_C_PaymentTerm
{
    /** Logger for class MPaymentTerm */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPaymentTerm.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_PaymentTerm_ID id
	 *	@param trx transaction
	 */
	public MPaymentTerm(Ctx ctx, int C_PaymentTerm_ID, Trx trx)
	{
		super(ctx, C_PaymentTerm_ID, trx);
		if (C_PaymentTerm_ID == 0)
		{
			setAfterDelivery (false);
			setNetDays (0);
			setDiscount (Env.ZERO);
			setDiscount2 (Env.ZERO);
			setDiscountDays (0);
			setDiscountDays2 (0);
			setGraceDays (0);
			setIsDueFixed (false);
			setIsValid (false);
		}	}	//	MPaymentTerm

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPaymentTerm(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPaymentTerm

	/** 100									*/
	private final static BigDecimal		HUNDRED = new BigDecimal(100);

	/**	Payment Schedule children			*/
	private MPaySchedule[]				m_schedule;

	/**
	 * 	Get Payment Schedule
	 * 	@param requery if true re-query
	 *	@return array of schedule
	 */
	public MPaySchedule[] getSchedule (boolean requery)
	{
		if (m_schedule != null && !requery)
			return m_schedule;
		String sql = "SELECT * FROM C_PaySchedule WHERE C_PaymentTerm_ID=? ORDER BY NetDays";
		ArrayList<MPaySchedule> list = new ArrayList<MPaySchedule>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_PaymentTerm_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MPaySchedule ps = new MPaySchedule(getCtx(), rs, get_Trx());
				ps.setParent(this);
				list.add (ps);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getSchedule", e); 
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		m_schedule = new MPaySchedule[list.size()];
		list.toArray(m_schedule);
		return m_schedule;
	}	//	getSchedule

	/**
	 * 	Validate Payment Term & Schedule
	 *	@return Validation Message @OK@ or error
	 */
	public String validate()
	{
		getSchedule(true);
		if (m_schedule.length == 0)
		{
			setIsValid(true);
			String msg = "@OK@";
			return Msg.parseTranslation(getCtx(), msg);
		}
		//	Add up
		BigDecimal total = Env.ZERO;
		for (MPaySchedule element : m_schedule) {
			BigDecimal percent = element.getPercentage();
			if (percent != null)
				total = total.add(percent);
		}
		boolean valid = total.compareTo(HUNDRED) == 0;
		setIsValid (valid);
		for (MPaySchedule element : m_schedule) {
			if (element.isValid() != valid)
			{
				element.setIsValid(valid);
				element.save();
			}
		}
		String msg = "@OK@";
		if (!valid)
			msg = "@Total@ = " + total + " - @Difference@ = " + HUNDRED.subtract(total); 
		return Msg.parseTranslation(getCtx(), msg);
	}	//	validate


	/*************************************************************************
	 * 	Apply Payment Term to Invoice -
	 *	@param C_Invoice_ID invoice
	 *	@return true if payment schedule is valid
	 */
	public boolean apply (int C_Invoice_ID)
	{
		MInvoice invoice = new MInvoice (getCtx(), C_Invoice_ID, get_Trx());
		if (invoice == null || invoice.get_ID() == 0)
		{
			log.log(Level.SEVERE, "apply - Not valid C_Invoice_ID=" + C_Invoice_ID);
			return false;
		}
		return apply (invoice);
	}	//	apply
	
	/**
	 * 	Apply Payment Term to Invoice
	 *	@param invoice invoice
	 *	@return true if payment schedule is valid
	 */
	public boolean apply (MInvoice invoice)
	{
		if (invoice == null || invoice.get_ID() == 0)
		{
			log.log(Level.SEVERE, "No valid invoice - " + invoice);
			return false;
		}

		if (!isValid())
			return applyNoSchedule (invoice);
		//
		getSchedule(true);
		if (m_schedule.length <= 0)
			return applyNoSchedule (invoice);
		else	//	only if valid
			return applySchedule(invoice);		
	}	//	apply

	/**
	 * 	Apply Payment Term without schedule to Invoice
	 *	@param invoice invoice
	 *	@return false as no payment schedule
	 */
	private boolean applyNoSchedule (MInvoice invoice)
	{
		deleteInvoicePaySchedule (invoice.getC_Invoice_ID(), invoice.get_Trx());
		//	updateInvoice
		if (invoice.getC_PaymentTerm_ID() != getC_PaymentTerm_ID())
			invoice.setC_PaymentTerm_ID(getC_PaymentTerm_ID());
		if (invoice.isPayScheduleValid())
			invoice.setIsPayScheduleValid(false);
		return false;
	}	//	applyNoSchedule

	/**
	 * 	Apply Payment Term with schedule to Invoice
	 *	@param invoice invoice
	 *	@return true if payment schedule is valid
	 */
	private boolean applySchedule (MInvoice invoice)
	{
		deleteInvoicePaySchedule (invoice.getC_Invoice_ID(), invoice.get_Trx());
		//	Create Schedule
		MInvoicePaySchedule ips = null;
		BigDecimal remainder = invoice.getGrandTotal();
		for (MPaySchedule element : m_schedule) {
			ips = new MInvoicePaySchedule (invoice, element);
			ips.save(invoice.get_Trx());
			log.fine(ips.toString());
			remainder = remainder.subtract(ips.getDueAmt());
		}	//	for all schedules
		//	Remainder - update last
		if (remainder.compareTo(Env.ZERO) != 0 && ips != null)
		{
			ips.setDueAmt(ips.getDueAmt().add(remainder));
			ips.save(invoice.get_Trx());
			log.fine("Remainder=" + remainder + " - " + ips);
		}
		
		//	updateInvoice
		if (invoice.getC_PaymentTerm_ID() != getC_PaymentTerm_ID())
			invoice.setC_PaymentTerm_ID(getC_PaymentTerm_ID());
		return invoice.validatePaySchedule();
	}	//	applySchedule

	/**
	 * 	Delete existing Invoice Payment Schedule
	 *	@param C_Invoice_ID id
	 *	@param trx transaction
	 */
	private void deleteInvoicePaySchedule (int C_Invoice_ID, Trx trx)
	{
		String sql = "DELETE FROM C_InvoicePaySchedule WHERE C_Invoice_ID=? ";
		int no = DB.executeUpdate(trx, sql,C_Invoice_ID);
		log.fine("C_Invoice_ID=" + C_Invoice_ID + " - #" + no);
	}	//	deleteInvoicePaySchedule

	
	/**************************************************************************
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPaymentTerm[");
		sb.append(get_ID()).append("-").append(getName())
			.append(",Valid=").append(isValid())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (isDueFixed())
		{
			int dd = getFixMonthDay();
			if (dd < 1 || dd > 31)
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@Invalid@ @FixMonthDay@"));
				return false;
			}
			dd = getFixMonthCutoff();
			if (dd < 1 || dd > 31)
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@Invalid@ @FixMonthCutoff@"));
				return false;
			}
		}
		
		if (!newRecord || !isValid())
			validate();
		return true;
	}	//	beforeSave
	
}	//	MPaymentTerm
