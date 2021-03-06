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

import org.compiere.util.*;

/**
 *	Dunning Run Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDunningRunLine.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MDunningRunLine extends X_C_DunningRunLine
{
    /** Logger for class MDunningRunLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDunningRunLine.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx ctx
	 *	@param C_DunningRunLine_ID id
	 *	@param trx transaction
	 */
	public MDunningRunLine (Ctx ctx, int C_DunningRunLine_ID, Trx trx)
	{
		super (ctx, C_DunningRunLine_ID, trx);
		if (C_DunningRunLine_ID == 0)
		{
			setAmt (Env.ZERO);
			setOpenAmt(Env.ZERO);
			setConvertedAmt (Env.ZERO);
			setFeeAmt (Env.ZERO);
			setInterestAmt (Env.ZERO);
			setTotalAmt (Env.ZERO);
			setDaysDue (0);
			setTimesDunned (0);
			setIsInDispute(false);
			setProcessed (false);
		}
	}	//	MDunningRunLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MDunningRunLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDunningRunLine

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public MDunningRunLine (MDunningRunEntry parent)
	{
		this(parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		
		if (parent.getC_DunningRunEntry_ID()!=0)
			setC_DunningRunEntry_ID(parent.getC_DunningRunEntry_ID());
		//
		m_parent = parent;
		m_C_CurrencyTo_ID = parent.getC_Currency_ID();
	}	//	MDunningRunLine

	private MDunningRunEntry	m_parent = null;
	private MInvoice			m_invoice = null;
	private MPayment			m_payment = null;
	private int					m_C_CurrencyFrom_ID = 0;
	private int					m_C_CurrencyTo_ID = 0;
	
	/**
	 * 	Get Parent 
	 *	@return parent
	 */
	public MDunningRunEntry getParent()
	{
		if (m_parent == null)
			m_parent = new MDunningRunEntry (getCtx(), getC_DunningRunEntry_ID(), get_Trx());
		return m_parent;
	}	//	getParent
	
	/**
	 * 	Get Invoice
	 *	@return Returns the invoice.
	 */
	public MInvoice getInvoice ()
	{
		if (getC_Invoice_ID() == 0)
			m_invoice = null;
		else if (m_invoice == null)
			m_invoice = new MInvoice (getCtx(), getC_Invoice_ID(), get_Trx());
		return m_invoice;
	}	//	getInvoice
	
	/**
	 * 	Set Invoice
	 *	@param invoice The invoice to set.
	 */
	public void setInvoice (MInvoice invoice)
	{
		m_invoice = invoice;
		if (invoice != null)
		{
			m_C_CurrencyFrom_ID = invoice.getC_Currency_ID();
			setAmt(invoice.getGrandTotal());
			setOpenAmt(getAmt());	//	not correct
			setConvertedAmt (MConversionRate.convert(getCtx(), getOpenAmt(), 
				getC_CurrencyFrom_ID(), getC_CurrencyTo_ID(), getAD_Client_ID(), getAD_Org_ID()));
		}
		else
		{
			m_C_CurrencyFrom_ID = 0;
			setAmt(Env.ZERO);
			setOpenAmt(Env.ZERO);
			setConvertedAmt(Env.ZERO);
		}
	}	//	setInvoice
	
	/**
	 * 	Set Invoice
	 *	@param C_Invoice_ID invoice
	 *	@param C_Currency_ID currency
	 *	@param GrandTotal total
	 *	@param Open open amount
	 *	@param DaysDue days due
	 *	@param IsInDispute in dispute
	 *	@param C_BPartner_ID bp
	 *	@param TimesDunned nuber of dunnings
	 *	@param DaysAfterLast not used
	 */
	public void setInvoice (int C_Invoice_ID, int C_Currency_ID, 
		BigDecimal GrandTotal, BigDecimal Open, 
		BigDecimal FeeAmount, 
		int DaysDue, boolean IsInDispute, 
		int TimesDunned, int DaysAfterLast)
	{
		setC_Invoice_ID(C_Invoice_ID);
		m_C_CurrencyFrom_ID = C_Currency_ID;
		setAmt (GrandTotal);
		setOpenAmt (Open);
		setFeeAmt (FeeAmount);
		setConvertedAmt (MConversionRate.convert(getCtx(), getOpenAmt(), 
			C_Currency_ID, getC_CurrencyTo_ID(), getAD_Client_ID(), getAD_Org_ID()));
		setIsInDispute(IsInDispute);
		setDaysDue(DaysDue);
		setTimesDunned(TimesDunned);
	}	//	setInvoice
	
	
	/**
	 * 	Set Fee
	 *	@param C_Currency_ID
	 *  @param FeeAmount 
	 */
	public void setFee (int C_Currency_ID, 
		BigDecimal FeeAmount)
	{
		m_C_CurrencyFrom_ID = C_Currency_ID;
		setAmt (FeeAmount);
		setOpenAmt (FeeAmount);
		setFeeAmt (FeeAmount);
		setConvertedAmt (MConversionRate.convert(getCtx(), getOpenAmt(), 
			C_Currency_ID, getC_CurrencyTo_ID(), getAD_Client_ID(), getAD_Org_ID()));
	}	//	setInvoice
	
	/**
	 * 	Get Payment
	 *	@return Returns the payment.
	 */
	public MPayment getPayment ()
	{
		if (getC_Payment_ID() == 0)
			m_payment = null;
		else if (m_payment == null)
			m_payment = new MPayment (getCtx(), getC_Payment_ID(), get_Trx());
		return m_payment;
	}	//	getPayment
	
	/**
	 * 	Set Payment
	 *
	public void setPayment (MPayment payment)
	{
		m_payment = payment;
		if (payment != null)
		{
			m_C_CurrencyFrom_ID = payment.getC_Currency_ID();
			setAmt(payment.getPayAmt());	//	need to reverse
			setOpenAmt(getAmt());	//	not correct
			setConvertedAmt (MConversionRate.convert(getCtx(), getOpenAmt(), 
				getC_CurrencyFrom_ID(), getC_CurrencyTo_ID(), getAD_Client_ID(), getAD_Org_ID()));
		}
		else
		{
			m_C_CurrencyFrom_ID = 0;
			setAmt(Env.ZERO);
			setConvertedAmt(Env.ZERO);
		}
	}	//	setPayment
	
	/**
	 * 	Set Payment
	 *	@param C_Payment_ID payment
	 *	@param C_Currency_ID currency
	 *	@param PayAmt amount
	 *	@param OpenAmt open
	 */
	public void setPayment (int C_Payment_ID, int C_Currency_ID, 
		BigDecimal PayAmt, BigDecimal OpenAmt)
	{
		setC_Payment_ID(C_Payment_ID);
		m_C_CurrencyFrom_ID = C_Currency_ID;
		setAmt (PayAmt);
		setOpenAmt (OpenAmt);
		setConvertedAmt (MConversionRate.convert(getCtx(), getOpenAmt(), 
			C_Currency_ID, getC_CurrencyTo_ID(), getAD_Client_ID(), getAD_Org_ID()));
	}	//	setPayment

	
	/**
	 * 	Get Currency From (Invoice/Payment)
	 *	@return Returns the Currency From
	 */
	public int getC_CurrencyFrom_ID ()
	{
		if (m_C_CurrencyFrom_ID == 0)
		{
			if (getC_Invoice_ID() != 0)
				m_C_CurrencyFrom_ID = getInvoice().getC_Currency_ID();
			else if (getC_Payment_ID() != 0)
				m_C_CurrencyFrom_ID = getPayment().getC_Currency_ID();
		}
		return m_C_CurrencyFrom_ID;
	}	//	getC_CurrencyFrom_ID
	
	/**
	 * 	Get Currency To from Parent
	 *	@return Returns the Currency To
	 */
	public int getC_CurrencyTo_ID ()
	{
		if (m_C_CurrencyTo_ID == 0)
			m_C_CurrencyTo_ID = getParent().getC_Currency_ID();
		return m_C_CurrencyTo_ID;
	}	//	getC_CurrencyTo_ID
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Set Amt
		if (getC_Invoice_ID() == 0 && getC_Payment_ID() == 0)
		{
			setAmt(Env.ZERO);
			setOpenAmt(Env.ZERO);
		}
		//	Converted Amt
		if (Env.ZERO.compareTo(getOpenAmt()) == 0)
			setConvertedAmt (Env.ZERO);
		else if (Env.ZERO.compareTo(getConvertedAmt()) == 0)
			setConvertedAmt (MConversionRate.convert(getCtx(), getOpenAmt(), 
				getC_CurrencyFrom_ID(), getC_CurrencyTo_ID(), getAD_Client_ID(), getAD_Org_ID()));
		//	Total
		setTotalAmt(getConvertedAmt().add(getFeeAmt()).add(getInterestAmt()));
		// Reset Collection Status only if null
		if (getInvoice()!=null && getInvoice().getInvoiceCollectionType ()==null)
		{
			if (m_invoice!=null)
			{
				m_invoice.setInvoiceCollectionType (X_C_Invoice.INVOICECOLLECTIONTYPE_Dunning);
				m_invoice.save ();
			}
		}
		//
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
		updateEntry();
		return success;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		updateEntry();
		return success;
	}	//	afterDelete
	
	/**
	 * 	Update Entry.
	 *	Calculate/update Amt/Qty 
	 */
	private void updateEntry()
	{
		// we do not count the fee line as an item, but it sum it up.
		String sql = "UPDATE C_DunningRunEntry e "
			+ "SET Amt=(SELECT SUM(ConvertedAmt)+SUM(FeeAmt)"
			+ " FROM C_DunningRunLine l "
				+ "WHERE e.C_DunningRunEntry_ID=l.C_DunningRunEntry_ID), "
			+ "QTY=(SELECT COUNT(*)"
			+ " FROM C_DunningRunLine l "
				+ "WHERE e.C_DunningRunEntry_ID=l.C_DunningRunEntry_ID "
				+ " AND (NOT C_Invoice_ID IS NULL OR NOT C_Payment_ID IS NULL))"
			+ " WHERE C_DunningRunEntry_ID=? ";
		
		DB.executeUpdate(get_Trx(), sql,getC_DunningRunEntry_ID());
	}	//	updateEntry
	
}	//	MDunningRunLine
