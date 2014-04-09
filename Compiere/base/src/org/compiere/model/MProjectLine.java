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
 * 	Project Line Model
 *
 *	@author Jorg Janke
 *	@version $Id: MProjectLine.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MProjectLine extends X_C_ProjectLine
{
    /** Logger for class MProjectLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProjectLine.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_ProjectLine_ID id
	 *	@param trx transaction
	 */
	public MProjectLine (Ctx ctx, int C_ProjectLine_ID, Trx trx)
	{
		super (ctx, C_ProjectLine_ID, trx);
		if (C_ProjectLine_ID == 0)
		{
		//  setC_Project_ID (0);
		//	setC_ProjectLine_ID (0);
			setLine (0);
			setIsPrinted(true);
			setProcessed(false);
			setInvoicedAmt (Env.ZERO);
			setInvoicedQty (Env.ZERO);
			//
			setPlannedAmt (Env.ZERO);
			setPlannedMarginAmt (Env.ZERO);
			setPlannedPrice (Env.ZERO);
			setPlannedQty (Env.ONE);
		}
	}	//	MProjectLine

	/**
	 * 	Load Constructor
	 * 	@param ctx context
	 * 	@param rs result set
	 *	@param trx transaction
	 */
	public MProjectLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProjectLine

	/**
	 * 	Parent Constructor
	 *	@param project parent
	 */
	public MProjectLine (MProject project)
	{
		this (project.getCtx(), 0, project.get_Trx());
		setClientOrg(project);
		setC_Project_ID (project.getC_Project_ID());	// Parent
		setLine();
	}	//	MProjectLine

	/** Parent				*/
	private MProject	m_parent = null;
	
	/**
	 *	Get the next Line No
	 */
	private void setLine()
	{
		setLine(QueryUtil.getSQLValue(get_Trx(), 
			"SELECT COALESCE(MAX(Line),0)+10 FROM C_ProjectLine WHERE C_Project_ID=?", getC_Project_ID()));
	}	//	setLine

	/**
	 * 	Set Product, committed qty, etc.
	 *	@param pi project issue
	 */
	public void setMProjectIssue (MProjectIssue pi)
	{
		setC_ProjectIssue_ID(pi.getC_ProjectIssue_ID());
		setM_Product_ID(pi.getM_Product_ID());
		setCommittedQty(pi.getMovementQty());
		if (getDescription() != null)
			setDescription(pi.getDescription());
	}	//	setMProjectIssue

	/**
	 *	Set PO
	 *	@param C_OrderPO_ID po id
	 */
	@Override
	public void setC_OrderPO_ID (int C_OrderPO_ID)
	{
		super.setC_OrderPO_ID(C_OrderPO_ID);
	}	//	setC_OrderPO_ID

	/**
	 * 	Get Project
	 *	@return parent
	 */
	public MProject getProject()
	{
		if (m_parent == null && getC_Project_ID() != 0)
		{
			m_parent = new MProject (getCtx(), getC_Project_ID(), get_Trx());
			if (get_Trx() != null)
				m_parent.load(get_Trx());
		}
		return m_parent;
	}	//	getProject
	
	/**
	 * 	Get Limit Price if exists
	 *	@return limit
	 */
	public BigDecimal getLimitPrice()
	{
		BigDecimal limitPrice = getPlannedPrice();
		if (getM_Product_ID() == 0)
			return limitPrice;
		if (getProject() == null)
			return limitPrice;
		boolean isSOTrx = true;
		MProductPricing pp = new MProductPricing (getAD_Client_ID(), getAD_Org_ID(),
			getM_Product_ID(), m_parent.getC_BPartner_ID(), getPlannedQty(), isSOTrx);
		pp.setM_PriceList_ID(m_parent.getM_PriceList_ID());
		if (pp.calculatePrice())
			limitPrice = pp.getPriceLimit();
		return limitPrice;
	}	//	getLimitPrice
	
	/**
	 * 	Get Currency Precision
	 * 	@return 2 (hardcoded)
	 */
	protected int getCurPrecision()
	{
		return 2;
	}	//	getCurPrecision
	
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
		//
		int M_PriceList_Version_ID = getCtx().getContextAsInt(windowNo, "M_PriceList_Version_ID");
		if (M_PriceList_Version_ID == 0)
			return;	

		int C_BPartner_ID = getCtx().getContextAsInt(windowNo, "C_BPartner_ID");
		BigDecimal Qty = getPlannedQty();
		boolean IsSOTrx = true;
		MProductPricing pp = new MProductPricing (getAD_Client_ID(), getAD_Org_ID(),
				M_Product_ID, C_BPartner_ID, Qty, IsSOTrx);
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
		Timestamp date = getPlannedDate();
		if (date == null)
			date = new Timestamp(getCtx().getContextAsTime(windowNo, "DateContract"));
		pp.setPriceDate(date);
		//
		BigDecimal PriceList = pp.getPriceList();
		setPriceList(PriceList);
		BigDecimal PlannedPrice = pp.getPriceStd();
		setPlannedPrice(PlannedPrice);
		BigDecimal Discount = pp.getDiscount();
		setDiscount(Discount);
		//
		BigDecimal PlannedAmt = pp.getLineAmt(getCurPrecision());
		setPlannedAmt(PlannedAmt);
		//	
		p_changeVO.setContext(getCtx(), windowNo, "StdPrecision", pp.getPrecision());
		log.fine("PlannedQty=" + Qty + " * PlannedPrice=" + PlannedPrice + " -> PlannedAmt=" + PlannedAmt);
	}	//	setM_Product_ID
	
	/**
	 * 	Set Discount - Callout
	 *	@param oldDiscount old value
	 *	@param newDiscount new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setDiscount (String oldDiscount, 
			String newDiscount, int windowNo) throws Exception
	{
		if (newDiscount == null || newDiscount.length() == 0)
			return;
		BigDecimal Discount = new BigDecimal(newDiscount);
		super.setDiscount(Discount);
		setAmt(windowNo, "Discount");
	}	//	setDiscount

	/**
	 * 	Set PriceList - Callout
	 *	@param oldPriceList old value
	 *	@param newPriceList new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setPriceList (String oldPriceList, 
			String newPriceList, int windowNo) throws Exception
	{
		if (newPriceList == null || newPriceList.length() == 0)
			return;
		BigDecimal PriceList = new BigDecimal(newPriceList);
		super.setPriceList(PriceList);
		setAmt(windowNo, "PriceList");
	}	//	setPriceList

	/**
	 * 	Set PlannedPrice - Callout
	 *	@param oldPlannedPrice old value
	 *	@param newPlannedPrice new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setPlannedPrice (String oldPlannedPrice, 
			String newPlannedPrice, int windowNo) throws Exception
	{
		if (newPlannedPrice == null || newPlannedPrice.length() == 0)
			return;
		BigDecimal PlannedPrice = new BigDecimal(newPlannedPrice);
		super.setPlannedPrice(PlannedPrice);
		setAmt(windowNo, "PlannedPrice");
	}	//	setPlannedPrice

	/**
	 * 	Set PlannedQty - Callout
	 *	@param oldPlannedQty old value
	 *	@param newPlannedQty new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setPlannedQty (String oldPlannedQty, 
			String newPlannedQty, int windowNo) throws Exception
	{
		if (newPlannedQty == null || newPlannedQty.length() == 0)
			return;
		BigDecimal PlannedQty = new BigDecimal(newPlannedQty);
		super.setPlannedQty(PlannedQty);
		setAmt(windowNo, "PlannedQty");
	}	//	setPlannedQty

	/**
	 * 	Set Amount (Callout)
	 *	@param windowNo window
	 *	@param columnName changed column
	 */
	private void setAmt(int windowNo, String columnName)
	{
		if(CThreadUtil.isCalloutActive())
			return;
		int curPrecision = getCurPrecision();
		int plPrecision = getCtx().getContextAsInt(windowNo, "StdPrecision");

		//	get values
		BigDecimal PlannedQty = getPlannedQty();
		if (PlannedQty == null)
			PlannedQty = Env.ONE;
		BigDecimal PlannedPrice = getPlannedPrice();
		if (PlannedPrice == null)
			PlannedPrice = Env.ZERO;
		BigDecimal PriceList = getPriceList();
		if (PriceList == null)
			PriceList = PlannedPrice;
		BigDecimal Discount = getDiscount();
		if (Discount == null)
			Discount = Env.ZERO;
		
		if (columnName.equals("PlannedPrice"))
		{
			if (PriceList.signum() == 0)
				Discount = Env.ZERO;
			else
			{
				BigDecimal multiplier = PlannedPrice.multiply(Env.ONEHUNDRED)
					.divide(PriceList, plPrecision, BigDecimal.ROUND_HALF_UP);
				Discount = Env.ONEHUNDRED.subtract(multiplier);
			}
			setDiscount(Discount);
			log.fine("PriceList=" + PriceList + " - Discount=" + Discount 
				+ " -> [PlannedPrice=" + PlannedPrice + "] (Precision=" + plPrecision+ ")");
		}
		else if (columnName.equals("PriceList"))
		{
			if (PriceList.signum() == 0)
				Discount = Env.ZERO;
			else
			{
				BigDecimal multiplier = PlannedPrice.multiply(Env.ONEHUNDRED)
					.divide(PriceList, plPrecision, BigDecimal.ROUND_HALF_UP);
				Discount = Env.ONEHUNDRED.subtract(multiplier);
			}
			setDiscount(Discount);
			log.fine("[PriceList=" + PriceList + "] - Discount=" + Discount 
				+ " -> PlannedPrice=" + PlannedPrice + " (Precision=" + plPrecision+ ")");
		}
		else if (columnName.equals("Discount"))
		{
			BigDecimal multiplier = Discount.divide(Env.ONEHUNDRED, 10, BigDecimal.ROUND_HALF_UP);
			multiplier = Env.ONE.subtract(multiplier);
			//
			PlannedPrice = PriceList.multiply(multiplier);			
			if (PlannedPrice.scale() > plPrecision)
				PlannedPrice = PlannedPrice.setScale(plPrecision, BigDecimal.ROUND_HALF_UP);
			setPlannedPrice(PlannedPrice);
			log.fine("PriceList=" + PriceList + " - [Discount=" + Discount 
				+ "] -> PlannedPrice=" + PlannedPrice + " (Precision=" + plPrecision+ ")");
		}
		
		//	Calculate Line Amount
		BigDecimal PlannedAmt = PlannedQty.multiply(PlannedPrice);
		if (PlannedAmt.scale() > curPrecision)
			PlannedAmt = PlannedAmt.setScale(curPrecision, BigDecimal.ROUND_HALF_UP);
		//
		log.fine("PlannedQty=" + PlannedQty + " * PlannedPrice=" + PlannedPrice 
			+ " -> PlannedAmt=" + PlannedAmt + " (Precision=" + curPrecision+ ")");
		setPlannedAmt(PlannedAmt);
	}	//	setAmt
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MProjectLine[");
			sb.append (get_ID()).append ("-")
				.append (getLine())
				.append(",C_Project_ID=").append(getC_Project_ID())
				.append(",C_ProjectPhase_ID=").append(getC_ProjectPhase_ID())
				.append(",C_ProjectTask_ID=").append(getC_ProjectTask_ID())
				.append(",C_ProjectIssue_ID=").append(getC_ProjectIssue_ID())
				.append(", M_Product_ID=").append(getM_Product_ID())
				.append(", PlannedQty=").append(getPlannedQty())
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
		if (getLine() == 0)
			setLine();
		
		//	Planned Amount	- Currency Precision
		BigDecimal PlannedAmt = getPlannedQty().multiply(getPlannedPrice());
		if (PlannedAmt.scale() > getCurPrecision())
			PlannedAmt = PlannedAmt.setScale(getCurPrecision(), BigDecimal.ROUND_HALF_UP);
		setPlannedAmt(PlannedAmt);
		
		//	Planned Margin
		if (is_ValueChanged("M_Product_ID") || is_ValueChanged("M_Product_Category_ID")
			|| is_ValueChanged("PlannedQty") || is_ValueChanged("PlannedPrice"))
		{
			if (getM_Product_ID() != 0)
			{
				BigDecimal marginEach = getPlannedPrice().subtract(getLimitPrice());
				setPlannedMarginAmt(marginEach.multiply(getPlannedQty()));
			}
			else if (getM_Product_Category_ID() != 0)
			{
				MProductCategory category = MProductCategory.get(getCtx(), getM_Product_Category_ID());
				BigDecimal marginEach = category.getPlannedMargin();
				setPlannedMarginAmt(marginEach.multiply(getPlannedQty()));
			}
		}
		
		//	Phase/Task
		if (is_ValueChanged("C_ProjectTask_ID") && getC_ProjectTask_ID() != 0)
		{
			MProjectTask pt = new MProjectTask(getCtx(), getC_ProjectTask_ID(), get_Trx());
			if (pt == null || pt.get_ID() == 0)
			{
				log.warning("Project Task Not Found - ID=" + getC_ProjectTask_ID());
				return false;
			}
			else
				setC_ProjectPhase_ID(pt.getC_ProjectPhase_ID());
		}
		if (is_ValueChanged("C_ProjectPhase_ID") && getC_ProjectPhase_ID() != 0)
		{
			MProjectPhase pp = new MProjectPhase(getCtx(), getC_ProjectPhase_ID(), get_Trx());
			if (pp == null || pp.get_ID() == 0)
			{
				log.warning("Project Phase Not Found - " + getC_ProjectPhase_ID());
				return false;
			}
			else
				setC_Project_ID(pp.getC_Project_ID());
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
		updateHeader();
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
		updateHeader();
		return success;
	}	//	afterDelete
	
	/**
	 * 	Update Header
	 */
	private void updateHeader()
	{
		String sql = "UPDATE C_Project p "
			+ "SET (PlannedAmt,PlannedQty,PlannedMarginAmt,"
				+ "CommittedAmt,CommittedQty,"
				+ "InvoicedAmt, InvoicedQty) = "
				+ "(SELECT COALESCE(SUM(pl.PlannedAmt),0), COALESCE(SUM(pl.PlannedQty),0), COALESCE(SUM(pl.PlannedMarginAmt),0),"
				+ " COALESCE(SUM(pl.CommittedAmt),0), COALESCE(SUM(pl.CommittedQty),0),"
				+ " COALESCE(SUM(pl.InvoicedAmt),0), COALESCE(SUM(pl.InvoicedQty),0) "
				+ "FROM C_ProjectLine pl "
				+ "WHERE pl.C_Project_ID=p.C_Project_ID AND pl.IsActive='Y') "
			+ "WHERE C_Project_ID=? ";
		int no = DB.executeUpdate(get_Trx(), sql,getC_Project_ID());
		if (no != 1)
			log.log(Level.SEVERE, "updateHeader - #" + no);
	}	//	updateHeader
	
}	//	MProjectLine
