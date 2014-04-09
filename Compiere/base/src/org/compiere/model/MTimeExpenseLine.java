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
import org.compiere.util.*;

/**
 * 	Time + Expense Line Model
 *
 *	@author Jorg Janke
 *	@version $Id: MTimeExpenseLine.java,v 1.4 2006/09/25 00:59:41 jjanke Exp $
 */
public class MTimeExpenseLine extends X_S_TimeExpenseLine
{
    /** Logger for class MTimeExpenseLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTimeExpenseLine.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param S_TimeExpenseLine_ID id
	 *	@param trx transaction
	 */
	public MTimeExpenseLine (Ctx ctx, int S_TimeExpenseLine_ID, Trx trx)
	{
		super (ctx, S_TimeExpenseLine_ID, trx);
		if (S_TimeExpenseLine_ID == 0)
		{
		//	setS_TimeExpenseLine_ID (0);		//	PK
		//	setS_TimeExpense_ID (0);			//	Parent
			setQty(Env.ONE);
			setQtyInvoiced(Env.ZERO);
			setQtyReimbursed(Env.ZERO);
			//
			setExpenseAmt(Env.ZERO);
			setConvertedAmt(Env.ZERO);
			setPriceReimbursed(Env.ZERO);
			setInvoicePrice(Env.ZERO);
			setPriceInvoiced(Env.ZERO);
			//
			setDateExpense (new Timestamp(System.currentTimeMillis()));
			setIsInvoiced (false);
			setIsTimeReport (false);
			setLine (10);
			setProcessed(false);
		}
	}	//	MTimeExpenseLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MTimeExpenseLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MTimeExpenseLine

	/**	Currency of Report			*/
	private int m_C_Currency_Report_ID = 0;

	
	/**
	 * 	Get Qty Invoiced
	 *	@return entered or qty
	 */
	@Override
	public BigDecimal getQtyInvoiced ()
	{
		BigDecimal bd = super.getQtyInvoiced ();
		if (Env.ZERO.compareTo(bd) == 0)
			return getQty();
		return bd;
	}	//	getQtyInvoiced

	/**
	 * 	Get Qty Reimbursed
	 *	@return entered or qty
	 */
	@Override
	public BigDecimal getQtyReimbursed ()
	{
		BigDecimal bd = super.getQtyReimbursed ();
		if (Env.ZERO.compareTo(bd) == 0)
			return getQty();
		return bd;
	}	//	getQtyReimbursed
	
	
	/**
	 * 	Get Price Invoiced
	 *	@return entered or invoice price
	 */
	@Override
	public BigDecimal getPriceInvoiced ()
	{
		BigDecimal bd = super.getPriceInvoiced ();
		if (Env.ZERO.compareTo(bd) == 0)
			return getInvoicePrice();
		return bd;
	}	//	getPriceInvoiced
	
	/**
	 * 	Get Price Reimbursed
	 *	@return entered or converted amt
	 */
	@Override
	public BigDecimal getPriceReimbursed ()
	{
		BigDecimal bd = super.getPriceReimbursed ();
		if (Env.ZERO.compareTo(bd) == 0)
			return getConvertedAmt();
		return bd;
	}	//	getPriceReimbursed
	
	
	/**
	 * 	Get Approval Amt
	 *	@return qty * converted amt
	 */
	public BigDecimal getApprovalAmt()
	{
		return getQty().multiply(getConvertedAmt());
	}	//	getApprovalAmt
	
	
	/**
	 * 	Get C_Currency_ID of Report (Price List)
	 *	@return currency
	 */
	public int getC_Currency_Report_ID()
	{
		if (m_C_Currency_Report_ID != 0)
			return m_C_Currency_Report_ID;
		//	Get it from header
		MTimeExpense te = new MTimeExpense (getCtx(), getS_TimeExpense_ID(), get_Trx());
		m_C_Currency_Report_ID = te.getC_Currency_ID();
		return m_C_Currency_Report_ID;
	}	//	getC_Currency_Report_ID

	/**
	 * 	Set C_Currency_ID of Report (Price List)
	 *	@param C_Currency_ID currency
	 */
	protected void setC_Currency_Report_ID (int C_Currency_ID)
	{
		m_C_Currency_Report_ID = C_Currency_ID;
	}	//	getC_Currency_Report_ID

	
	/**
	 * 	Set Resource Assignment - Callout
	 *	@param oldS_ResourceAssignment_ID old value
	 *	@param newS_ResourceAssignment_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setS_ResourceAssignment_ID (String oldS_ResourceAssignment_ID, 
			String newS_ResourceAssignment_ID, int windowNo) throws Exception
	{
		if (newS_ResourceAssignment_ID == null || newS_ResourceAssignment_ID.length() == 0)
			return;
		int S_ResourceAssignment_ID = Integer.parseInt(newS_ResourceAssignment_ID);
		if (S_ResourceAssignment_ID == 0)
			return;
		//
		super.setS_ResourceAssignment_ID(S_ResourceAssignment_ID);

		int M_Product_ID = 0;
		String Name = null;
		String Description = null;
		BigDecimal Qty = null;
		String sql = "SELECT p.M_Product_ID, ra.Name, ra.Description, ra.Qty "
			+ "FROM S_ResourceAssignment ra"
			+ " INNER JOIN M_Product p ON (p.S_Resource_ID=ra.S_Resource_ID) "
			+ "WHERE ra.S_ResourceAssignment_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, S_ResourceAssignment_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				M_Product_ID = rs.getInt (1);
				Name = rs.getString(2);
				Description = rs.getString(3);
				Qty = rs.getBigDecimal(4);
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
		log.fine("S_ResourceAssignment_ID=" + S_ResourceAssignment_ID 
				+ " - M_Product_ID=" + M_Product_ID);
		if (M_Product_ID != 0)
		{
			setM_Product_ID(M_Product_ID);
			if (Description != null)
				Name += " (" + Description + ")";
			if (!".".equals(Name))
				setDescription(Name);
			if (Qty != null)
				setQty(Qty);
		}
	}	//	setS_ResourceAssignment_ID

	/**
	 * 	Set Product - Callout
	 *	@param oldM_Product_ID old value
	 *	@param newM_Product_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setM_Product_ID (String oldM_Product_ID, 
			String newM_Product_ID, int windowNo) throws Exception
	{
		if (newM_Product_ID == null || newM_Product_ID.length() == 0)
			return;
		int M_Product_ID = Integer.parseInt(newM_Product_ID);
		
		super.setM_Product_ID(M_Product_ID);
		if (M_Product_ID == 0)
			return;
		
		//	Employee
		MTimeExpense hdr = new MTimeExpense(getCtx(), getS_TimeExpense_ID(), null);
		int C_BPartner_ID = hdr.getC_BPartner_ID();
		BigDecimal Qty = getQty();
		boolean IsSOTrx = true;
		MProductPricing pp = new MProductPricing (getAD_Client_ID(), getAD_Org_ID(),
				M_Product_ID, C_BPartner_ID, Qty, IsSOTrx);
		//
		int M_PriceList_ID = hdr.getM_PriceList_ID();
		pp.setM_PriceList_ID(M_PriceList_ID);
		Timestamp orderDate = getDateExpense();
		pp.setPriceDate(orderDate);
		//
		setExpenseAmt(pp.getPriceStd());
		setC_Currency_ID(pp.getC_Currency_ID());
		setAmt(windowNo, "M_Product_ID");		
		setC_UOM_ID(pp.getC_UOM_ID()); //Setting UOM for the Selected product
	}	//	setM_Product_ID
	
	/**
	 * 	Set Currency - Callout
	 *	@param oldC_Currency_ID old value
	 *	@param newC_Currency_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setC_Currency_ID (String oldC_Currency_ID, 
			String newC_Currency_ID, int windowNo) throws Exception
	{
		if (newC_Currency_ID == null || newC_Currency_ID.length() == 0)
			return;
		int C_Currency_ID = Integer.parseInt(newC_Currency_ID);
		super.setC_Currency_ID(C_Currency_ID);
		setAmt(windowNo, "C_Currency_ID");
	}	//	setC_Currency_ID
	
	/**
	 * 	Set ExpenseAmt - Callout
	 *	@param oldExpenseAmt old value
	 *	@param newExpenseAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setExpenseAmt (String oldExpenseAmt, 
			String newExpenseAmt, int windowNo) throws Exception
	{
		if (newExpenseAmt == null || newExpenseAmt.length() == 0)
			return;
		BigDecimal ExpenseAmt = new BigDecimal(newExpenseAmt);
		super.setExpenseAmt(ExpenseAmt);
		setAmt(windowNo, "ExpenseAmt");
	}	//	setExpenseAmt

	/**
	 * 	Set Amount (Callout)
	 *	@param windowNo window
	 *	@param columnName changed column
	 */
	private void setAmt(int windowNo, String columnName)
	{
		//	get values
		BigDecimal ExpenseAmt = getExpenseAmt();
		int C_Currency_From_ID = getC_Currency_ID();
		int C_Currency_To_ID = getCtx().getContextAsInt("$C_Currency_ID");
		Timestamp DateExpense = getDateExpense();
		//
		log.fine("Amt=" + ExpenseAmt + ", C_Currency_ID=" + C_Currency_From_ID);
		//	Converted Amount = Unit price
		BigDecimal ConvertedAmt = ExpenseAmt;
		//	convert if required
		if (ConvertedAmt.signum() != 0 && C_Currency_To_ID != C_Currency_From_ID)
		{
			ConvertedAmt = MConversionRate.convert (getCtx(),
				ConvertedAmt, C_Currency_From_ID, C_Currency_To_ID, 
				DateExpense, 0, getAD_Client_ID(), getAD_Org_ID());
		}
		setConvertedAmt(ConvertedAmt);
		log.fine("ConvertedAmt=" + ConvertedAmt);
	}	//	setAmt
	
	/**
	 * 	Before Save.
	 * 	Calculate converted amt
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Calculate Converted Amount
		if (newRecord || is_ValueChanged("ExpenseAmt") || is_ValueChanged("C_Currency_ID"))
		{
			if (getC_Currency_ID() == getC_Currency_Report_ID())
				setConvertedAmt(getExpenseAmt());
			else
			{
				setConvertedAmt(MConversionRate.convert (getCtx(),
					getExpenseAmt(), getC_Currency_ID(), getC_Currency_Report_ID(), 
					getDateExpense(), 0, getAD_Client_ID(), getAD_Org_ID()) );
			}
		}
		if (isTimeReport())
		{
			setExpenseAmt(Env.ZERO);
			setConvertedAmt(Env.ZERO);
		}
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
		if (success)
		{
			updateHeader();
			if (newRecord || is_ValueChanged("S_ResourceAssignment_ID"))
			{
				int S_ResourceAssignment_ID = getS_ResourceAssignment_ID();
				int old_S_ResourceAssignment_ID = 0;
				if (!newRecord)
				{
					Object ii = get_ValueOld("S_ResourceAssignment_ID");
					if (ii instanceof Integer)
					{
						old_S_ResourceAssignment_ID = ((Integer)ii).intValue();
						//	Changed Assignment
						if (old_S_ResourceAssignment_ID != S_ResourceAssignment_ID
							&& old_S_ResourceAssignment_ID != 0)
						{
							MResourceAssignment ra = new MResourceAssignment (getCtx(), 
								old_S_ResourceAssignment_ID, get_Trx());
							ra.delete(false);
						}
					}
				}
				//	Sync Assignment
				if (S_ResourceAssignment_ID != 0)
				{
					MResourceAssignment ra = new MResourceAssignment (getCtx(), 
						S_ResourceAssignment_ID, get_Trx());
					if (getQty().compareTo(ra.getQty()) != 0)
					{
						ra.setQty(getQty());
						if (getDescription() != null && getDescription().length() > 0)
							ra.setDescription(getDescription());
						ra.save();
					}
				}
			}
		}
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
		if (success)
		{
			updateHeader();
			//
			Object ii = get_ValueOld("S_ResourceAssignment_ID");
			if (ii instanceof Integer)
			{
				int old_S_ResourceAssignment_ID = ((Integer)ii).intValue();
				//	Deleted Assignment
				if (old_S_ResourceAssignment_ID != 0)
				{
					MResourceAssignment ra = new MResourceAssignment (getCtx(), 
						old_S_ResourceAssignment_ID, get_Trx());
					ra.delete(false);
				}
			}
		}
		return success;
	}	//	afterDelete
	
	/**
	 * 	Update Header.
	 * 	Set Approved Amount
	 */
	private void updateHeader()
	{
		String sql = "UPDATE S_TimeExpense te"
			+ " SET ApprovalAmt = "
				+ "(SELECT SUM(Qty*ConvertedAmt) FROM S_TimeExpenseLine tel "
				+ "WHERE te.S_TimeExpense_ID=tel.S_TimeExpense_ID) "
			+ "WHERE S_TimeExpense_ID=? ";
		DB.executeUpdate(get_Trx(), sql,getS_TimeExpense_ID());
		
		if (get_Trx() != null)
			get_Trx().commit();

	}	//	updateHeader
	
}	//	MTimeExpenseLine
