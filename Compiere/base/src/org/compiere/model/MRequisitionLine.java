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
import org.compiere.common.constants.*;
import org.compiere.util.*;
/**
 *	Requisition Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRequisitionLine.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MRequisitionLine extends X_M_RequisitionLine
{
    /** Logger for class MRequisitionLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRequisitionLine.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_RequisitionLine_ID id
	 *	@param trx transaction
	 */
	public MRequisitionLine (Ctx ctx, int M_RequisitionLine_ID, Trx trx)
	{
		super (ctx, M_RequisitionLine_ID, trx);
		if (M_RequisitionLine_ID == 0)
		{
		//	setM_Requisition_ID (0);
			setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM M_RequisitionLine WHERE M_Requisition_ID=@M_Requisition_ID@
			setLineNetAmt (Env.ZERO);
			setPriceActual (Env.ZERO);
			setQty (Env.ONE);	// 1
		}
		
	}	//	MRequisitionLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MRequisitionLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MRequisitionLine

	/**
	 * 	Parent Constructor
	 *	@param req requisition
	 */
	public MRequisitionLine (MRequisition req)
	{
		this (req.getCtx(), 0, req.get_Trx());
		setClientOrg(req);
		setM_Requisition_ID(req.getM_Requisition_ID());
		m_M_PriceList_ID = req.getM_PriceList_ID();
		m_parent = req;
	}	//	MRequisitionLine

	/** Parent					*/
	private MRequisition	m_parent = null;
	
	/**	PriceList				*/
	private int 	m_M_PriceList_ID = 0;
	/** Temp BPartner			*/
	private int		m_C_BPartner_ID = 0;
	
	/**
	 * @return Returns the c_BPartner_ID.
	 */
	public int getC_BPartner_ID ()
	{
		return m_C_BPartner_ID;
	}
	/**
	 * @param partner_ID The c_BPartner_ID to set.
	 */
	public void setC_BPartner_ID (int partner_ID)
	{
		m_C_BPartner_ID = partner_ID;
	}
	
	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MRequisition getParent()
	{
		if (m_parent == null)
			m_parent = new MRequisition (getCtx(), getM_Requisition_ID(), get_Trx());
		return m_parent;
	}	//	getParent
	
	/**
	 * 	Set Price
	 */
	public void setPrice()
	{
		if (getC_Charge_ID() != 0)
		{
			MCharge charge = MCharge.get(getCtx(), getC_Charge_ID());
			setPriceActual(charge.getChargeAmt());
		}
		if (getM_Product_ID() == 0)
			return;
		if (m_M_PriceList_ID == 0)
			m_M_PriceList_ID = getParent().getM_PriceList_ID();
		if (m_M_PriceList_ID == 0)
		{
			log.log(Level.SEVERE, "PriceList unknown!");
			return;
		}
		setPrice (m_M_PriceList_ID);
	}	//	setPrice
	
	/**
	 * 	Set Price for Product and PriceList
	 * 	@param M_PriceList_ID price list
	 */
	public void setPrice (int M_PriceList_ID)
	{
		if (getM_Product_ID() == 0)
			return;
		//
		log.fine("M_PriceList_ID=" + M_PriceList_ID);
		boolean isSOTrx = false;
		MProductPricing pp = new MProductPricing (getAD_Client_ID(), getAD_Org_ID(),
			getM_Product_ID(), getC_BPartner_ID(), getQty(), isSOTrx);
		pp.setM_PriceList_ID(M_PriceList_ID);
	//	pp.setPriceDate(getDateOrdered());
		//
		setPriceActual (pp.getPriceStd());
	}	//	setPrice

	/**
	 * 	Calculate Line Net Amt
	 */
	public void setLineNetAmt ()
	{
		BigDecimal lineNetAmt = getQty().multiply(getPriceActual());
		super.setLineNetAmt (lineNetAmt);
	}	//	setLineNetAmt
	
	
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
		{
			setM_AttributeSetInstance_ID(0);
			return;
		}
		//	Set Attribute
		int M_AttributeSetInstance_ID = getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID");
		if (getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Product_ID") == M_Product_ID
			&& M_AttributeSetInstance_ID != 0)
			setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
		else
			setM_AttributeSetInstance_ID(0);

		int C_BPartner_ID = getC_BPartner_ID();
		BigDecimal Qty = getQty();
		boolean isSOTrx = false;
		MProductPricing pp = new MProductPricing (getAD_Client_ID(), getAD_Org_ID(),
			M_Product_ID, C_BPartner_ID, Qty, isSOTrx);
		//
		int M_PriceList_ID = getCtx().getContextAsInt(windowNo, "M_PriceList_ID");
		pp.setM_PriceList_ID(M_PriceList_ID);
		int M_PriceList_Version_ID = getCtx().getContextAsInt(windowNo, "M_PriceList_Version_ID");
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
		Timestamp orderDate = new Timestamp(getCtx().getContextAsTime(windowNo, "DateRequired"));
		pp.setPriceDate(orderDate);
		//		
		setPriceActual(pp.getPriceStd());
		p_changeVO.setContext(getCtx(), windowNo, "EnforcePriceLimit", pp.isEnforcePriceLimit());	//	not used
		p_changeVO.setContext(getCtx(), windowNo, "DiscountSchema", pp.isDiscountSchema());
	}	//	setM_Product_ID
	
	/**
	 * 	Set PriceActual - Callout
	 *	@param oldPriceActual old value
	 *	@param newPriceActual new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setPriceActual (String oldPriceActual, 
			String newPriceActual, int windowNo) throws Exception
	{
		if (newPriceActual == null || newPriceActual.length() == 0)
			return;
		BigDecimal PriceActual = new BigDecimal(newPriceActual);
		super.setPriceActual(PriceActual);
		setAmt(windowNo, "PriceActual");
	}	//	setPriceActual

	/**
	 * 	Set Qty - Callout
	 *	@param oldQty old value
	 *	@param newQty new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setQty (String oldQty, 
			String newQty, int windowNo) throws Exception
	{
		if (newQty == null || newQty.length() == 0)
			return;
		BigDecimal Qty = new BigDecimal(newQty);
		super.setQty(Qty);
		setAmt(windowNo, "Qty");
	}	//	setQty

	/**
	 * 	Set Amount (Callout)
	 *	@param windowNo window
	 *	@param columnName changed column
	 */
	private void setAmt(int windowNo, String columnName)
	{
		BigDecimal Qty = getQty();
		//	Qty changed - recalc price
		if (columnName.equals("Qty") 
			&& "Y".equals(getCtx().getContext(windowNo, "DiscountSchema")))
		{
			int M_Product_ID = getM_Product_ID();
			int C_BPartner_ID = getC_BPartner_ID();
			boolean isSOTrx = false;
			MProductPricing pp = new MProductPricing (getAD_Client_ID(), getAD_Org_ID(),
				M_Product_ID, C_BPartner_ID, Qty, isSOTrx);
			//
			int M_PriceList_ID = getCtx().getContextAsInt(windowNo, "M_PriceList_ID");
			pp.setM_PriceList_ID(M_PriceList_ID);
			int M_PriceList_Version_ID = getCtx().getContextAsInt(windowNo, "M_PriceList_Version_ID");
			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
			Timestamp orderDate = new Timestamp(getCtx().getContextAsTime(windowNo, "DateInvoiced"));
			pp.setPriceDate(orderDate);
			//
			setPriceActual(pp.getPriceStd());
		}

		int StdPrecision = getCtx().getStdPrecision();
		BigDecimal PriceActual = getPriceActual();

		//	get values
		log.fine("Qty=" + Qty + ", Price=" + PriceActual + ", Precision=" + StdPrecision);

		//	Multiply
		BigDecimal LineNetAmt = Qty.multiply(PriceActual);
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		setLineNetAmt(LineNetAmt);
		log.info("LineNetAmt=" + LineNetAmt);
	}	//	setAmt	
	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM M_RequisitionLine WHERE M_Requisition_ID=?";
			int ii = QueryUtil.getSQLValue (get_Trx(), sql, getM_Requisition_ID());
			setLine (ii);
		}
		//	Product & ASI - Charge
		if (getM_Product_ID() != 0 && getC_Charge_ID() != 0)
			setC_Charge_ID(0);
		if (getM_AttributeSetInstance_ID() != 0 && getC_Charge_ID() != 0)
			setM_AttributeSetInstance_ID(0);
		//
		if (getPriceActual().compareTo(Env.ZERO) == 0)
			setPrice();
		setLineNetAmt();
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save.
	 * 	Update Total on Header
	 *	@param newRecord if new record
	 *	@param success save was success
	 *	@return true if saved
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		return updateHeader();
	}	//	afterSave

	
	/**
	 * 	After Delete
	 *	@param success
	 *	@return true/false
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		return updateHeader();
	}	//	afterDelete
	
	/**
	 * 	Update Header
	 *	@return header updated
	 */
	private boolean updateHeader()
	{
		log.fine("");
		String sql = "UPDATE M_Requisition r"
			+ " SET TotalLines="
				+ "(SELECT COALESCE(SUM(LineNetAmt),0) FROM M_RequisitionLine rl "
				+ "WHERE r.M_Requisition_ID=rl.M_Requisition_ID) "
			+ "WHERE M_Requisition_ID= ?" ;
		int no = DB.executeUpdate(get_Trx(), sql,getM_Requisition_ID());
		if (no != 1)
			log.log(Level.SEVERE, "Header update #" + no);
		m_parent = null;
		
		if (get_Trx() != null)
			get_Trx().commit();
		
		return no == 1;
	}	//	updateHeader
	
}	//	MRequisitionLine
