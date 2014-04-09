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
import org.compiere.util.*;

/**
 * 	InOut Line
 *
 *  @author Jorg Janke
 *  @version $Id: MInOutLine.java,v 1.5 2006/07/30 00:51:03 jjanke Exp $
 */
public class MInOutLine extends X_M_InOutLine
{
    /** Logger for class MInOutLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInOutLine.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Ship lines Of Order Line
	 *	@param ctx context
	 *	@param C_OrderLine_ID line
	 *	@param where optional addition where clause
	 *  @param trx transaction
	 *	@return array of receipt lines
	 */
	public static MInOutLine[] getOfOrderLine (Ctx ctx, 
		int C_OrderLine_ID, String where, Trx trx)
	{
		ArrayList<MInOutLine> list = new ArrayList<MInOutLine>();
		String sql = "SELECT * FROM M_InOutLine WHERE C_OrderLine_ID=?";
		if (where != null && where.length() > 0)
			sql += " AND " + where;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, C_OrderLine_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MInOutLine(ctx, rs, trx));
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
		MInOutLine[] retValue = new MInOutLine[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfOrderLine

	/**
	 * 	Get Ship lines Of Order Line
	 *	@param ctx context
	 *	@param C_OrderLine_ID line
	 *  @param trx transaction
	 *	@return array of receipt lines2
	 */
	public static MInOutLine[] get (Ctx ctx, int C_OrderLine_ID, Trx trx)
	{
		ArrayList<MInOutLine> list = new ArrayList<MInOutLine>();
		String sql = "SELECT * FROM M_InOutLine WHERE C_OrderLine_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, C_OrderLine_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MInOutLine(ctx, rs, trx));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		MInOutLine[] retValue = new MInOutLine[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfOrderLine

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MInOutLine.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOutLine_ID id
	 *	@param trx p_trx name
	 */
	public MInOutLine (Ctx ctx, int M_InOutLine_ID, Trx trx)
	{
		super (ctx, M_InOutLine_ID, trx);
		if (M_InOutLine_ID == 0)
		{
		//	setLine (0);
		//	setM_Locator_ID (0);
		//	setC_UOM_ID (0);
		//	setM_Product_ID (0);
			setM_AttributeSetInstance_ID(0);
		//	setMovementQty (Env.ZERO);
			setConfirmedQty(Env.ZERO);
			setPickedQty(Env.ZERO);
			setScrappedQty(Env.ZERO);
			setTargetQty(Env.ZERO);
			setIsInvoiced (false);
			setIsDescription (false);
		}
	}	//	MInOutLine

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trx transaction
	 */
	public MInOutLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MInOutLine

	/**
	 *  Parent Constructor
	 *  @param inout parent
	 */
	public MInOutLine (MInOut inout)
	{
		this (inout.getCtx(), 0, inout.get_Trx());
		setClientOrg (inout);
		if(inout.getM_InOut_ID()!=0)
			setM_InOut_ID (inout.getM_InOut_ID());
		setM_Warehouse_ID (inout.getM_Warehouse_ID());
		setC_Project_ID(inout.getC_Project_ID());
		m_parent = inout;
	}	//	MInOutLine

	/**	Product					*/
	private MProduct 		m_product = null;
	/** Warehouse				*/
	private int				m_M_Warehouse_ID = 0;
	/** Parent					*/
	private MInOut			m_parent = null;
	/** Matched Invoices		*/
	private MMatchInv[] 	m_matchInv = null;
	/** Matched Purchase Orders	*/
	private MMatchPO[]	 	m_matchPO = null;

	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MInOut getParent()
	{
		if (m_parent == null)
			m_parent = new MInOut (getCtx(), getM_InOut_ID(), get_Trx());
		return m_parent;
	}	//	getParent
	
	/**
	 * 	Set Order Line.
	 * 	Does not set Quantity!
	 *	@param oLine order line
	 *	@param M_Locator_ID locator
	 * 	@param Qty used only to find suitable locator
	 */
	public void setOrderLine (MOrderLine oLine, int M_Locator_ID, BigDecimal Qty)
	{
		setC_OrderLine_ID(oLine.getC_OrderLine_ID());
		setLine(oLine.getLine());
		setC_UOM_ID(oLine.getC_UOM_ID());
		MProduct product = oLine.getProduct();
		if (product == null)
		{
			setM_Product_ID(0);
			setM_AttributeSetInstance_ID(0);
			super.setM_Locator_ID(0);
		}
		else
		{
			setM_Product_ID(oLine.getM_Product_ID());
			setM_AttributeSetInstance_ID(oLine.getM_AttributeSetInstance_ID());
			//
			if (product.isItem())
			{
				if (M_Locator_ID == 0)
					setM_Locator_ID(Qty);	//	requires warehouse, product, asi
				else
					setM_Locator_ID(M_Locator_ID);
			}
			else
				super.setM_Locator_ID(0);
		}
		setC_Charge_ID(oLine.getC_Charge_ID());
		setDescription(oLine.getDescription());
		setIsDescription(oLine.isDescription());
		//
		setAD_Org_ID(oLine.getAD_Org_ID());
		setC_Project_ID(oLine.getC_Project_ID());
		setC_ProjectPhase_ID(oLine.getC_ProjectPhase_ID());
		setC_ProjectTask_ID(oLine.getC_ProjectTask_ID());
		setC_Activity_ID(oLine.getC_Activity_ID());
		setC_Campaign_ID(oLine.getC_Campaign_ID());
		setAD_OrgTrx_ID(oLine.getAD_OrgTrx_ID());
		setUser1_ID(oLine.getUser1_ID());
		setUser2_ID(oLine.getUser2_ID());
	}	//	setOrderLine
	
	/**
	 * 	Set Order Line - Callout
	 *	@param oldC_OrderLine_ID old BP
	 *	@param newC_OrderLine_ID new BP
	 *	@param windowNo window no
	 */
	@UICallout public void setC_OrderLine_ID (String oldC_OrderLine_ID, 
			String newC_OrderLine_ID, int windowNo) throws Exception
	{
		if (newC_OrderLine_ID == null || newC_OrderLine_ID.length() == 0)
			return;
		int C_OrderLine_ID = Integer.parseInt(newC_OrderLine_ID);
		if (C_OrderLine_ID == 0)
			return;
		MOrderLine ol = new MOrderLine (getCtx(), C_OrderLine_ID, null);
		if (ol.get_ID() != 0)
		{
			setC_OrderLine_ID(C_OrderLine_ID);
			setDescription(ol.getDescription());
			BigDecimal MovementQty = ol.getQtyOrdered().subtract(ol.getQtyDelivered());
			setMovementQty(MovementQty);
			setOrderLine(ol, 0, MovementQty);
			BigDecimal QtyEntered = MovementQty;
			if (ol.getQtyEntered().compareTo(ol.getQtyOrdered()) != 0)
				QtyEntered = QtyEntered.multiply(ol.getQtyEntered())
					.divide(ol.getQtyOrdered(), 12, BigDecimal.ROUND_HALF_UP);
			setQtyEntered(QtyEntered);
			
			if(ol.getParent().isReturnTrx())
			{
				MInOutLine ioLine = new MInOutLine (getCtx(), ol.getOrig_InOutLine_ID(), null);	
				setM_Locator_ID(ioLine.getM_Locator_ID());
			}
			
		}
	}	//	setC_OrderLine_ID
	
	/**
	 * 	Set Invoice Line.
	 * 	Does not set Quantity!
	 *	@param iLine invoice line
	 *	@param M_Locator_ID locator
	 *	@param Qty qty only fo find suitable locator
	 */
	public void setInvoiceLine (MInvoiceLine iLine, int M_Locator_ID, BigDecimal Qty)
	{
		setC_OrderLine_ID(iLine.getC_OrderLine_ID());
		setLine(iLine.getLine());
		setC_UOM_ID(iLine.getC_UOM_ID());
		int M_Product_ID = iLine.getM_Product_ID();
		if (M_Product_ID == 0)
		{
			set_ValueNoCheck("M_Product_ID", null);
			set_ValueNoCheck("M_Locator_ID", null);
			set_ValueNoCheck("M_AttributeSetInstance_ID", null);
		}
		else
		{
			setM_Product_ID(M_Product_ID);
			setM_AttributeSetInstance_ID(iLine.getM_AttributeSetInstance_ID());			
			if (M_Locator_ID == 0)
				setM_Locator_ID(Qty);	//	requires warehouse, product, asi
			else
				setM_Locator_ID(M_Locator_ID);
		}
		setC_Charge_ID(iLine.getC_Charge_ID());
		setDescription(iLine.getDescription());
		setIsDescription(iLine.isDescription());
		//
		setC_Project_ID(iLine.getC_Project_ID());
		setC_ProjectPhase_ID(iLine.getC_ProjectPhase_ID());
		setC_ProjectTask_ID(iLine.getC_ProjectTask_ID());
		setC_Activity_ID(iLine.getC_Activity_ID());
		setC_Campaign_ID(iLine.getC_Campaign_ID());
		setAD_OrgTrx_ID(iLine.getAD_OrgTrx_ID());
		setUser1_ID(iLine.getUser1_ID());
		setUser2_ID(iLine.getUser2_ID());
	}	//	setInvoiceLine
	
	/**
	 * 	Get Warehouse
	 *	@return Returns the m_Warehouse_ID.
	 */
	public int getM_Warehouse_ID()
	{
		if (m_M_Warehouse_ID == 0)
			m_M_Warehouse_ID = getParent().getM_Warehouse_ID();
		return m_M_Warehouse_ID;
	}	//	getM_Warehouse_ID
	
	/**
	 * 	Set Warehouse
	 *	@param warehouse_ID The m_Warehouse_ID to set.
	 */
	public void setM_Warehouse_ID (int warehouse_ID)
	{
		m_M_Warehouse_ID = warehouse_ID;
	}	//	setM_Warehouse_ID

	/**
	 * 	Set M_Locator_ID
	 *	@param M_Locator_ID id
	 */
	@Override
	public void setM_Locator_ID (int M_Locator_ID)
	{
		if (M_Locator_ID < 0) 
			throw new IllegalArgumentException ("M_Locator_ID is mandatory.");
		//	set to 0 explicitly to reset
		set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));	
	}	//	setM_Locator_ID
	
	/**
	 * 	Set (default) Locator based on qty.
	 * 	@param Qty quantity
	 * 	Assumes Warehouse is set
	 */
	public void setM_Locator_ID(BigDecimal Qty)
	{
		//	Locator esatblished
		if (getM_Locator_ID() != 0)
			return;
		//	No Product
		if (getM_Product_ID() == 0)
		{
			set_ValueNoCheck("M_Locator_ID", null);
			return;
		}
		
		//	Get existing Location
		int M_Locator_ID = Storage.getLocatorID(getM_Warehouse_ID(), 
				getM_Product_ID(), getM_AttributeSetInstance_ID(), 
				Qty, get_Trx());
		//	Get default Location
		if (M_Locator_ID == 0)
		{
			MProduct product = MProduct.get(getCtx(), getM_Product_ID());
			M_Locator_ID = MProductLocator.getFirstM_Locator_ID (product, getM_Warehouse_ID());
			if (M_Locator_ID == 0)
			{
				MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
				if(wh.isWMSEnabled())
					M_Locator_ID = wh.getM_RcvLocator_ID();
				
				if(M_Locator_ID == 0)
					M_Locator_ID = wh.getDefaultM_Locator_ID();
			}
		}
		setM_Locator_ID(M_Locator_ID);
	}	//	setM_Locator_ID
	
	/**
	 * 	Set Movement/Movement Qty
	 *	@param Qty Entered/Movement Qty
	 */
	public void setQty (BigDecimal Qty)
	{
		setQtyEntered(Qty);
		setMovementQty(getQtyEntered());
	}	//	setQtyInvoiced

	/**
	 * 	Set Qty Entered - enforce entered UOM 
	 *	@param QtyEntered
	 */
	@Override
	public void setQtyEntered (BigDecimal QtyEntered)
	{
		if (QtyEntered != null && getC_UOM_ID() != 0)
		{
			int precision = MUOM.getPrecision(getCtx(), getC_UOM_ID());
			QtyEntered = QtyEntered.setScale(precision, BigDecimal.ROUND_HALF_UP);
		}
		super.setQtyEntered (QtyEntered);
	}	//	setQtyEntered

	/**
	 * 	Set Movement Qty - enforce Product UOM 
	 *	@param MovementQty
	 */
	@Override
	public void setMovementQty (BigDecimal MovementQty)
	{
		MProduct product = getProduct();
		if (MovementQty != null && product != null)
		{
			int precision = product.getUOMPrecision();
			MovementQty = MovementQty.setScale(precision, BigDecimal.ROUND_HALF_UP);
		}
		super.setMovementQty(MovementQty);
	}	//	setMovementQty

	/**
	 * 	Get Product
	 *	@return product or null
	 */
	public MProduct getProduct()
	{
		if (m_product == null && getM_Product_ID() != 0)
			m_product = MProduct.get (getCtx(), getM_Product_ID());
		return m_product;
	}	//	getProduct
	
	/**
	 * 	Set Product
	 *	@param product product
	 */
	public void setProduct (MProduct product)
	{
		m_product = product;
		if (m_product != null)
		{
			setM_Product_ID(m_product.getM_Product_ID());
			setC_UOM_ID (m_product.getC_UOM_ID());
		}
		else
		{
			setM_Product_ID(0);
			setC_UOM_ID (0);
		}
		setM_AttributeSetInstance_ID(0);
	}	//	setProduct
	
	/**
	 * 	Set M_Product_ID
	 *	@param M_Product_ID product
	 *	@param setUOM also set UOM from product
	 */
	public void setM_Product_ID (int M_Product_ID, boolean setUOM)
	{
		if (setUOM)
			setProduct(MProduct.get(getCtx(), M_Product_ID));
		else
			super.setM_Product_ID (M_Product_ID);
		setM_AttributeSetInstance_ID(0);
	}	//	setM_Product_ID
	
	/**
	 * 	Set Product and UOM
	 *	@param M_Product_ID product
	 *	@param C_UOM_ID uom
	 */
	public void setM_Product_ID (int M_Product_ID, int C_UOM_ID)
	{
		if (M_Product_ID != 0)
			super.setM_Product_ID (M_Product_ID);
		super.setC_UOM_ID(C_UOM_ID);
		setM_AttributeSetInstance_ID(0);
		m_product = null;
	}	//	setM_Product_ID

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
		if (M_Product_ID == 0)
		{
			setM_AttributeSetInstance_ID(0);
			return;
		}
		//
		super.setM_Product_ID(M_Product_ID);
		setC_Charge_ID(0);
		
		//	Set Attribute & Locator
		int M_Locator_ID = 0;
		if (getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Product_ID") == M_Product_ID
			&& getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
		{
			setM_AttributeSetInstance_ID(getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID"));
			//	Locator from Info Window - ASI
			M_Locator_ID = getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Locator_ID");
			if (M_Locator_ID != 0)
				setM_Locator_ID(M_Locator_ID);
		}
		else
			setM_AttributeSetInstance_ID(0);
		//
		boolean IsSOTrx = getCtx().isSOTrx(windowNo);
		if (IsSOTrx)
			return;

		//	PO - Set UOM/Locator/Qty
		MProduct product = getProduct();
		setC_UOM_ID(product.getC_UOM_ID());
		BigDecimal QtyEntered = getQtyEntered();
		setMovementQty(QtyEntered);
		if (M_Locator_ID != 0)
			;		//	already set via ASI
		else
		{
			int M_Warehouse_ID = getCtx().getContextAsInt(windowNo, "M_Warehouse_ID");
			M_Locator_ID = MProductLocator.getFirstM_Locator_ID (product, M_Warehouse_ID);
			if (M_Locator_ID != 0)
				setM_Locator_ID(M_Locator_ID);
			else
			{
				MWarehouse wh = MWarehouse.get (getCtx(), M_Warehouse_ID);
				if(wh.isWMSEnabled())
					M_Locator_ID = wh.getM_RcvLocator_ID();
				
				if(M_Locator_ID == 0)
					M_Locator_ID = wh.getDefaultM_Locator_ID();
				
				setM_Locator_ID(M_Locator_ID);
			}
		}
	}	//	setM_Product_ID

	/**
	 * 	Set Product - Callout
	 *	@param oldM_AttributeSetInstance_ID old value
	 *	@param newM_AttributeSetInstance_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setM_AttributeSetInstance_ID (String oldM_AttributeSetInstance_ID, 
			String newM_AttributeSetInstance_ID, int windowNo) throws Exception
	{
		if (newM_AttributeSetInstance_ID == null || newM_AttributeSetInstance_ID.length() == 0)
			return;
		int M_AttributeSetInstance_ID = Integer.parseInt(newM_AttributeSetInstance_ID);
		setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
		if (M_AttributeSetInstance_ID == 0)
			return;
		//
		int M_Product_ID = getM_Product_ID();
		int M_Warehouse_ID = getCtx().getContextAsInt(windowNo, "M_Warehouse_ID");
		int M_Locator_ID = getM_Locator_ID();
		log.fine("M_Product_ID=" + M_Product_ID
			+ ", M_ASI_ID=" + M_AttributeSetInstance_ID
			+ " - M_Warehouse_ID=" + M_Warehouse_ID 
			+ ", M_Locator_ID=" + M_Locator_ID);
		//	Check Selection
		int M_ASI_ID =	Env.getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID");
		if (M_ASI_ID == M_AttributeSetInstance_ID)
		{
			int selectedM_Locator_ID = Env.getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Locator_ID");
			if (selectedM_Locator_ID != 0)
			{
				log.fine("Selected M_Locator_ID=" + selectedM_Locator_ID);
				setM_Locator_ID(selectedM_Locator_ID);
			}
		}
	}	//	setM_AttributeSetInstance_ID
	
	/**
	 * 	Set UOM - Callout
	 *	@param oldC_UOM_ID old value
	 *	@param newC_UOM_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setC_UOM_ID (String oldC_UOM_ID, 
			String newC_UOM_ID, int windowNo) throws Exception
	{
		if (newC_UOM_ID == null || newC_UOM_ID.length() == 0)
			return;
		int C_UOM_ID = Integer.parseInt(newC_UOM_ID);
		if (C_UOM_ID == 0)
			return;
		//
		super.setC_UOM_ID(C_UOM_ID);
		setQty(windowNo, "C_UOM_ID");
	}	//	setC_UOM_ID

	/**
	 * 	Set QtyEntered - Callout
	 *	@param oldQtyEntered old value
	 *	@param newQtyEntered new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setQtyEntered (String oldQtyEntered, 
			String newQtyEntered, int windowNo) throws Exception
	{
		if (newQtyEntered == null || newQtyEntered.length() == 0)
			return;
		BigDecimal QtyEntered = new BigDecimal(newQtyEntered);
		super.setQtyEntered(QtyEntered);
		setQty(windowNo, "QtyEntered");
	}	//	setQtyEntered
	
	/**
	 * 	Set MovementQty - Callout
	 *	@param oldMovementQty old value
	 *	@param newMovementQty new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setMovementQty (String oldMovementQty, 
			String newMovementQty, int windowNo) throws Exception
	{
		if (newMovementQty == null || newMovementQty.length() == 0)
			return;
		BigDecimal MovementQty = new BigDecimal(newMovementQty);
		super.setMovementQty(MovementQty);
		setQty(windowNo, "MovementQty");
	}	//	setMovementQty

	/**
	 * check is line has been allocated - done if created by a Warehouse Task.
	 * @return
	 */
	public boolean isAllocated()
	{
		if(getQtyAllocated().signum()!=0)
			return true;
		
		return false;
	}
	/**
	 * 	Set Qty
	 *	@param windowNo window
	 *	@param columnName column
	 */
	private void setQty (int windowNo, String columnName)
	{
		int M_Product_ID = getM_Product_ID();
		//	log.log(Level.WARNING,"qty - init - M_Product_ID=" + M_Product_ID);
		BigDecimal MovementQty, QtyEntered;
		int C_UOM_To_ID = getC_UOM_ID();
		
		//	No Product
		if (M_Product_ID == 0)
		{
			QtyEntered = getQtyEntered();
			setMovementQty(QtyEntered);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (columnName.equals("C_UOM_ID"))
		{
			QtyEntered = getQtyEntered();
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision
				(getCtx(), C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID 
					+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);  
				QtyEntered = QtyEntered1;
				setQtyEntered(QtyEntered);
			}
			MovementQty = MUOMConversion.convertProductFrom (getCtx(), 
				M_Product_ID, C_UOM_To_ID, QtyEntered);
			if (MovementQty == null)
				MovementQty = QtyEntered;
			boolean conversion = QtyEntered.compareTo(MovementQty) != 0;
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion 
				+ " MovementQty=" + MovementQty);
			p_changeVO.setContext(getCtx(), windowNo, "UOMConversion", conversion);
			setMovementQty(MovementQty);
		}
		//	No UOM defined
		else if (C_UOM_To_ID == 0)
		{
			QtyEntered = getQtyEntered();
			setMovementQty(QtyEntered);
		}
		//	QtyEntered changed - calculate MovementQty
		else if (columnName.equals("QtyEntered"))
		{
			QtyEntered = getQtyEntered();
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision
				(getCtx(), C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID 
					+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);  
				QtyEntered = QtyEntered1;
				setQtyEntered(QtyEntered);
			}
			MovementQty = MUOMConversion.convertProductFrom (getCtx(), 
				M_Product_ID, C_UOM_To_ID, QtyEntered);
			if (MovementQty == null)
				MovementQty = QtyEntered;
			boolean conversion = QtyEntered.compareTo(MovementQty) != 0;
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion 
				+ " MovementQty=" + MovementQty);
			p_changeVO.setContext(getCtx(), windowNo, "UOMConversion", conversion);
			setMovementQty(MovementQty);
		}
		//	MovementQty changed - calculate QtyEntered (should not happen)
		else if (columnName.equals("MovementQty"))
		{
			MovementQty = getMovementQty();
			int precision = MProduct.get(getCtx(), M_Product_ID).getUOMPrecision(); 
			BigDecimal MovementQty1 = MovementQty.setScale(precision, BigDecimal.ROUND_HALF_UP);
			if (MovementQty.compareTo(MovementQty1) != 0)
			{
				log.fine("Corrected MovementQty " 
					+ MovementQty + "->" + MovementQty1);  
				MovementQty = MovementQty1;
				setMovementQty(MovementQty);
			}
			QtyEntered = MUOMConversion.convertProductTo (getCtx(), 
				M_Product_ID, C_UOM_To_ID, MovementQty);
			if (QtyEntered == null)
				QtyEntered = MovementQty;
			boolean conversion = MovementQty.compareTo(QtyEntered) != 0;
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", MovementQty=" + MovementQty
				+ " -> " + conversion 
				+ " QtyEntered=" + QtyEntered);
			p_changeVO.setContext(getCtx(), windowNo, "UOMConversion", conversion);
			setQtyEntered(QtyEntered);
		}
		
		// RMA : Check qty returned is more than qty shipped
		boolean IsReturnTrx = getParent().isReturnTrx();
		if(M_Product_ID != 0 
		   && IsReturnTrx) 
		{
			int C_OrderLine_ID = getC_OrderLine_ID();
			int M_InOut_ID = getM_InOut_ID();
			int M_InOutLine_ID = getM_InOutLine_ID();
			
			if(C_OrderLine_ID > 0)
			{
				BigDecimal openQty = MInOutLine.getInOutOpenQty(getCtx(), M_InOut_ID, M_InOutLine_ID, C_OrderLine_ID);
				MovementQty = getMovementQty();
				if(openQty.compareTo(MovementQty)<0)
				{
					if(getCtx().isSOTrx(windowNo))
						p_changeVO.addError(Msg.getMsg(getCtx(), "QtyShippedLessThanQtyReturned", openQty));
					else
						p_changeVO.addError(Msg.getMsg(getCtx(), "QtyReceivedLessThanQtyReturned", openQty));

					setMovementQty(openQty);
					MovementQty = openQty;

					QtyEntered = MUOMConversion.convertProductTo (getCtx(), M_Product_ID, 
							C_UOM_To_ID, MovementQty);
					if (QtyEntered == null)
						QtyEntered = MovementQty;
					setQtyEntered(QtyEntered);
					log.fine("QtyEntered : "+ QtyEntered.toString() +
							"MovementQty : " + MovementQty.toString());
				}
			}
		}

	}	//	setQty
	
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
	 * 	Get C_Project_ID
	 *	@return project
	 */
	@Override
	public int getC_Project_ID()
	{
		int ii = super.getC_Project_ID ();
		if (ii == 0)
			ii = getParent().getC_Project_ID();
		return ii;
	}	//	getC_Project_ID
	
	/**
	 * 	Get C_Activity_ID
	 *	@return Activity
	 */
	@Override
	public int getC_Activity_ID()
	{
		int ii = super.getC_Activity_ID ();
		if (ii == 0)
			ii = getParent().getC_Activity_ID();
		return ii;
	}	//	getC_Activity_ID
	
	/**
	 * 	Get C_Campaign_ID
	 *	@return Campaign
	 */
	@Override
	public int getC_Campaign_ID()
	{
		int ii = super.getC_Campaign_ID ();
		if (ii == 0)
			ii = getParent().getC_Campaign_ID();
		return ii;
	}	//	getC_Campaign_ID
	
	/**
	 * 	Get User2_ID
	 *	@return User2
	 */
	@Override
	public int getUser1_ID ()
	{
		int ii = super.getUser1_ID ();
		if (ii == 0)
			ii = getParent().getUser1_ID();
		return ii;
	}	//	getUser1_ID

	/**
	 * 	Get User2_ID
	 *	@return User2
	 */
	@Override
	public int getUser2_ID ()
	{
		int ii = super.getUser2_ID ();
		if (ii == 0)
			ii = getParent().getUser2_ID();
		return ii;
	}	//	getUser2_ID

	/**
	 * 	Get AD_OrgTrx_ID
	 *	@return p_trx org
	 */
	@Override
	public int getAD_OrgTrx_ID()
	{
		int ii = super.getAD_OrgTrx_ID();
		if (ii == 0)
			ii = getParent().getAD_OrgTrx_ID();
		return ii;
	}	//	getAD_OrgTrx_ID

	/**
	 * 	Get Match POs
	 *	@return matched purchase orders
	 */
	public MMatchPO[] getMatchPO()
	{
		if (m_matchPO == null)
			m_matchPO = MMatchPO.get (getCtx(), getM_InOutLine_ID(), get_Trx());
		return m_matchPO;		
	}	//	getMatchPO
	
	/**
	 * 	Get Match PO Difference
	 *	@return not matched qty (positive not - negative over)
	 */
	public BigDecimal getMatchPODifference()
	{
		if (isDescription())
			return Env.ZERO;
		BigDecimal retValue = getMovementQty();
		MMatchPO[] po = getMatchPO();
		for (MMatchPO matchPO : po) {
			retValue = retValue.subtract (matchPO.getQty());
		}
		log.finer("#" + retValue);
		return retValue;
	}	//	getMatchPODifference

	/**
	 * 	Is Match PO posted
	 *	@return true if posed
	 */
	public boolean isMatchPOPosted()
	{
		MMatchPO[] po = getMatchPO();
		for (MMatchPO matchPO : po) {
			if (!matchPO.isPosted())
				return false;
		}
		return true;
	}	//	isMatchPOposted

	/**
	 * 	Get Match Inv
	 *	@return matched invoices
	 */
	public MMatchInv[] getMatchInv()
	{
		if (m_matchInv == null)
			m_matchInv = MMatchInv.get (getCtx(), getM_InOutLine_ID(), get_Trx());
		return m_matchInv;		
	}	//	getMatchInv
	
	/**
	 * 	Get Match Inv Difference
	 *	@return not matched qty (positive not - negative over)
	 */
	public BigDecimal getMatchInvDifference()
	{
		if (isDescription())
			return Env.ZERO;
		BigDecimal retValue = getMovementQty();
		MMatchInv[] inv = getMatchInv();
		for (MMatchInv matchInv : inv) {
			retValue = retValue.subtract (matchInv.getQty());
		}
		log.finer("#" + retValue);
		return retValue;
	}	//	getMatchInvDifference
	
	/**
	 * 	Is Match Inv posted
	 *	@return true if posed
	 */
	public boolean isMatchInvPosted()
	{
		MMatchInv[] inv = getMatchInv();
		for (MMatchInv matchInv : inv) {
			if (!matchInv.isPosted())
				return false;
		}
		return true;
	}	//	isMatchPOposted
	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if(newRecord)
		{
			MInOut ship = getParent();
			MDocType dt = MDocType.get(getCtx(), ship.getC_DocType_ID());
			boolean isPick = dt.isPickQAConfirm();
			boolean isShip = dt.isShipConfirm();
			if (isPick || isShip)
			{
				MInOutConfirm [] confirmations = ship.getConfirmations(true);
				for(MInOutConfirm confirmation : confirmations)
				{
					if(confirmation.isProcessed())
					{
						log.saveError("Confirmation document Already Processed", "Can not add new line as the confirmation document has already been processed");
						return false;
					}
				}

			}
		}
	
		log.fine("");
		//	Get Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM M_InOutLine WHERE M_InOut_ID=?";
			int ii = QueryUtil.getSQLValue (get_Trx(), sql, getM_InOut_ID());
			setLine (ii);
		}
		//	UOM
		if (getC_UOM_ID() == 0)
			setC_UOM_ID (getCtx().getContextAsInt( "#C_UOM_ID"));
		if (getC_UOM_ID() == 0)
		{
			int C_UOM_ID = MUOM.getDefault_UOM_ID(getCtx());
			if (C_UOM_ID > 0)
				setC_UOM_ID (C_UOM_ID);
		}
		//	Qty Precision
		if (newRecord || is_ValueChanged("QtyEntered"))
			setQtyEntered(getQtyEntered());
		if (newRecord || is_ValueChanged("MovementQty"))
			setMovementQty(getMovementQty());
		
		//	Order Line
		if (getC_OrderLine_ID() == 0)
		{
			if (getParent().isSOTrx())
			{
				log.saveError("FillMandatory", Msg.translate(getCtx(), "C_Order_ID"));
				return false;
			}
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	Before Delete
	 *	@return true if drafted
	 */
	@Override
	protected boolean beforeDelete ()
	{
		if (getParent().getDocStatus().equals(X_M_InOut.DOCSTATUS_Drafted) ||
			getParent().getDocStatus().equals(X_M_InOut.DOCSTATUS_InProgress))
			return true;
		log.saveError("Error", Msg.getMsg(getCtx(), "CannotDelete"));
		return false;
	}	//	beforeDelete

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInOutLine[").append (get_ID())
			.append(",M_Product_ID=").append(getM_Product_ID())
			.append(",QtyEntered=").append(getQtyEntered())
			.append(",MovementQty=").append(getMovementQty())
			.append(",M_AttributeSetInstance_ID=").append(getM_AttributeSetInstance_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Base value for Cost Distribution
	 *	@param CostDistribution cost Distribution
	 *	@return base number
	 */
	public BigDecimal getBase (String CostDistribution)
	{
		if (X_C_LandedCost.LANDEDCOSTDISTRIBUTION_Costs.equals(CostDistribution))
		{
			//	TODO Costs!
			log.severe("Not Implemented yet - Cost");
			return Env.ZERO;
		}
		else if (X_C_LandedCost.LANDEDCOSTDISTRIBUTION_Line.equals(CostDistribution))
			return Env.ONE;
		else if (X_C_LandedCost.LANDEDCOSTDISTRIBUTION_Quantity.equals(CostDistribution))
			return getMovementQty();
		else if (X_C_LandedCost.LANDEDCOSTDISTRIBUTION_Volume.equals(CostDistribution))
		{
			MProduct product = getProduct();
			if (product == null)
			{
				log.severe("No Product");
				return Env.ZERO;
			}
			return getMovementQty().multiply(product.getVolume());
		}
		else if (X_C_LandedCost.LANDEDCOSTDISTRIBUTION_Weight.equals(CostDistribution))
		{
			MProduct product = getProduct();
			if (product == null)
			{
				log.severe("No Product");
				return Env.ZERO;
			}
			return getMovementQty().multiply(product.getWeight());
		}
		//
		log.severe("Invalid Criteria: " + CostDistribution);
		return Env.ZERO;
	}	//	getBase

	/**
	 * 	Get InOut open quantity
	 *	@param ctx context
	 *	@param M_InOutLine_ID shipment line
	 *	@param excludeC_OrderLine_ID exclude C_OrderLine_ID
	 *	@return QtyReturn
	 */
	public static BigDecimal getInOutOpenQty(Ctx ctx, int M_InOut_ID, int M_InOutLine_ID, int C_OrderLine_ID)
	{

		MOrderLine oLine = new MOrderLine (ctx, C_OrderLine_ID, null);
		BigDecimal QtyOpen = oLine.getQtyOrdered();
		
		BigDecimal retValue = Env.ZERO;
		String sql = "SELECT SUM(MovementQty) "
			+ " FROM M_InOutLine il"
			+ " WHERE il.M_InOut_ID=?"	//	#1
			+ " AND il.M_InOutLine_ID<>?"
			+ " AND il.C_OrderLine_ID=?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, M_InOut_ID);
			pstmt.setInt (2, M_InOutLine_ID);
			pstmt.setInt (3, C_OrderLine_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = rs.getBigDecimal(1);
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		if (retValue == null)
			s_log.fine("-");
		else
			s_log.fine(retValue.toString());
		
		if (retValue != null)
			return QtyOpen.subtract(retValue);
		
		return QtyOpen;
	}	//	getQtyRMA

	@Override
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(o==null || getClass() != o.getClass())
			return false;
		MInOutLine that = (MInOutLine)o;
		if(this.getM_InOutLine_ID() == 0 || that.getM_InOutLine_ID() ==0)
			return false;
		super.equals(o);
		return true;
	}
}	//	MInOutLine
