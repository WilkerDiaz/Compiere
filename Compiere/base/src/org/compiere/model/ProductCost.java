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

import org.compiere.util.*;

/**
 * 	Product Cost Model.
 *	Summarizes Info in MCost
 *	
 *  @author Jorg Janke
 *  @version $Id: ProductCost.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class ProductCost
{
	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param trx p_trx
	 */
	public ProductCost (Ctx ctx, int M_Product_ID, int M_AttributeSetInstance_ID, 
		Trx trx)
	{
		m_M_Product_ID = M_Product_ID;
		if (m_M_Product_ID != 0)
			m_product = MProduct.get (ctx, M_Product_ID);
		m_M_AttributeSetInstance_ID = M_AttributeSetInstance_ID;
		m_trxName = trx;
	}	//	ProductCost
	
	/** The ID					*/
	private int				m_M_Product_ID = 0;
	/** ASI						*/
	private int				m_M_AttributeSetInstance_ID = 0;
	/** The Product				*/
	private MProduct		m_product = null;
	/** Transaction				*/
	private Trx 			m_trxName = null;
	
	private int             m_C_UOM_ID = 0;
	private BigDecimal      m_qty = Env.ZERO;

	/**	Logger					*/
	private static CLogger 	log = CLogger.getCLogger (ProductCost.class);

	/**
	 *  Get Product
	 *  @return Product might be null
	 */
	public MProduct getProduct()
	{
		return m_product;
	}   //  getProduct

	/**
	 * 	Is this a Service
	 *	@return true if service
	 */
	public boolean isService()
	{
		if (m_product != null)
			return m_product.isService();
		return false;
	}	//	isService
	
	/**
	 *  Set Quantity in Storage UOM
	 *  @param qty quantity
	 */
	public void setQty (BigDecimal qty)
	{
		m_qty = qty;
	}   //  setQty

	/**
	 *  Set Quantity in UOM
	 *  @param qty quantity
	 *  @param C_UOM_ID UOM
	 */
	public void setQty (BigDecimal qty, int C_UOM_ID)
	{
		m_qty = MUOMConversion.convert (C_UOM_ID, m_C_UOM_ID, qty, true);    //  StdPrecision
		if (qty != null && m_qty == null)   //  conversion error
		{
			log.severe ("Conversion error - set to " + qty);
			m_qty = qty;
		}
		else
			m_C_UOM_ID = C_UOM_ID;
	}   //  setQty

	
	
	
	/** Product Revenue Acct    */
	public static final int ACCTTYPE_P_Revenue      = 1;
	/** Product Expense Acct    */
	public static final int ACCTTYPE_P_Expense      = 2;
	/** Product Asset Acct      */
	public static final int ACCTTYPE_P_Asset        = 3;
	/** Product COGS Acct       */
	public static final int ACCTTYPE_P_Cogs         = 4;
	/** Purchase Price Variance */
	public static final int ACCTTYPE_P_PPV          = 5;
	/** Invoice Price Variance  */
	public static final int ACCTTYPE_P_IPV          = 6;
	/** Trade Discount Revenue  */
	public static final int ACCTTYPE_P_TDiscountRec = 7;
	/** Trade Discount Costs    */
	public static final int ACCTTYPE_P_TDiscountGrant = 8;
	/** Cost Adjustment			*/
	public static final int ACCTTYPE_P_CostAdjustment = 9;
	/** Inventory Clearing		*/
	public static final int ACCTTYPE_P_InventoryClearing = 10;
	/** Resource Absorption (CMFG) */
	public static final int ACCTTYPE_P_ResourceAbsorption = 11;
	/** Material Overhead (CMFG) */
	public static final int ACCTTYPE_P_MaterialOvhd = 12;
	
	/**
	 *  Line Account from Product
	 *
	 *  @param  AcctType see ACCTTYPE_* (1..12)
	 *  @param as Accounting Schema
	 *  @return Requested Product Account
	 */
	public MAccount getAccount(int AcctType, MAcctSchema as)
	{
		if (AcctType < 1 || AcctType > 12)
			return null;

		//  No Product - get Default from Product Category
		if (m_M_Product_ID == 0)
			return getAccountDefault(AcctType, as);

		String sql = "SELECT COALESCE(a.P_Revenue_Acct, b.P_Revenue_Acct), " //1
			+ " COALESCE(a.P_Expense_Acct, b.P_Expense_Acct), "              //2
			+ " COALESCE(a.P_Asset_Acct, b.P_Asset_Acct), "                  //3
			+ " COALESCE(a.P_Cogs_Acct, b.P_Cogs_Acct), "	                 //4
			+ " COALESCE(a.P_PurchasePriceVariance_Acct, b.P_PurchasePriceVariance_Acct), "  //5
			+ " COALESCE(a.P_InvoicePriceVariance_Acct, b.P_InvoicePriceVariance_Acct), "	 //6
			+ " COALESCE(a.P_TradeDiscountRec_Acct, b.P_TradeDiscountRec_Acct), "            //7
			+ " COALESCE(a.P_TradeDiscountGrant_Acct, b.P_TradeDiscountGrant_Acct), "	     //8
			+ " COALESCE(a.P_CostAdjustment_Acct, b.P_CostAdjustment_Acct), "                //9
			+ " COALESCE(a.P_InventoryClearing_Acct, b.P_InventoryClearing_Acct), "          //10
			+ " COALESCE(a.P_Resource_Absorption_Acct, b.P_Resource_Absorption_Acct), "      //11
			+ " COALESCE(a.P_MaterialOverhd_Acct, b.P_MaterialOverhd_Acct) "	             //12
			+ " FROM C_AcctSchema_Default b "
			+ " LEFT OUTER JOIN M_Product_Acct a  ON (a.C_AcctSchema_ID = b.C_AcctSchema_ID "
			+                                   " AND a.M_Product_ID=? "
					                          + " AND a.IsActive = 'Y') "
			+ " WHERE b.C_AcctSchema_ID=? ";

		//
		int validCombination_ID = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, as.get_Trx());
			pstmt.setInt(1, m_M_Product_ID);
			pstmt.setInt(2, as.getC_AcctSchema_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				validCombination_ID = rs.getInt(AcctType);
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
		if (validCombination_ID == 0)
			return null;
		return MAccount.get(as.getCtx(), validCombination_ID);
	}   //  getAccount

	/**
	 *  Account from Default Product Category
	 *
	 *  @param  AcctType see ACCTTYPE_* (1..12)
	 *  @param as accounting schema
	 *  @return Requested Product Account
	 */
	public MAccount getAccountDefault (int AcctType, MAcctSchema as)
	{
		if (AcctType < 1 || AcctType > 12)
			return null;
		String sql = "SELECT COALESCE(a.P_Revenue_Acct, b.P_Revenue_Acct), " //1
			+ " COALESCE(a.P_Expense_Acct, b.P_Expense_Acct), "              //2
			+ " COALESCE(a.P_Asset_Acct, b.P_Asset_Acct), "                  //3
			+ " COALESCE(a.P_Cogs_Acct, b.P_Cogs_Acct), "	                 //4
			+ " COALESCE(a.P_PurchasePriceVariance_Acct, b.P_PurchasePriceVariance_Acct), "  //5
			+ " COALESCE(a.P_InvoicePriceVariance_Acct, b.P_InvoicePriceVariance_Acct), "	 //6
			+ " COALESCE(a.P_TradeDiscountRec_Acct, b.P_TradeDiscountRec_Acct), "            //7
			+ " COALESCE(a.P_TradeDiscountGrant_Acct, b.P_TradeDiscountGrant_Acct), "	     //8
			+ " COALESCE(a.P_CostAdjustment_Acct, b.P_CostAdjustment_Acct), "                //9
			+ " COALESCE(a.P_InventoryClearing_Acct, b.P_InventoryClearing_Acct), "          //10
			+ " COALESCE(a.P_Resource_Absorption_Acct, b.P_Resource_Absorption_Acct), "      //11
			+ " COALESCE(a.P_MaterialOverhd_Acct, b.P_MaterialOverhd_Acct) "	             //12
			+ " FROM C_AcctSchema_Default b "
			+ " LEFT OUTER JOIN M_Product_Category_Acct a ON (a.C_AcctSchema_ID = b.C_AcctSchema_ID "
			                                          + " AND a.IsActive = 'Y' ),"
            + " M_Product_Category pc "			                                          
			+ " WHERE pc.M_Product_Category_ID=a.M_Product_Category_ID"
			+ " AND b.C_AcctSchema_ID = ? "
			+ " ORDER BY pc.IsDefault DESC, pc.Created";

		int validCombination_ID = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, as.get_Trx());
			pstmt.setInt(1, as.getC_AcctSchema_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				validCombination_ID = rs.getInt(AcctType);
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
		if (validCombination_ID == 0)
			return null;
		return MAccount.get(as.getCtx(), validCombination_ID);
	}   //  getAccountDefault
	
	
	/**************************************************************************
	 *  Get Total Costs (amt*qty) in Accounting Schema Currency
	 *  @param as accounting schema
	 *  @param AD_Org_ID p_trx org
	 *  @param costingMethod if null uses Accounting Schema - AcctSchema.COSTINGMETHOD_*
	 *  @param C_OrderLine_ID optional order line
	 *	@param zeroCostsOK zero/no costs are OK
	 *  @return cost or null, if qty or costs cannot be determined
	 */
	public BigDecimal getProductCosts (MAcctSchema as, int AD_Org_ID, 
		String costingMethod, int C_OrderLine_ID, boolean zeroCostsOK)
	{
		if (m_qty == null)
		{
			log.fine("No Qty");
			return null;
		}
		/**	Old Costing
		MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
		if (!client.isUseBetaFunctions())
		{
			BigDecimal itemCost = getProductItemCostOld(as, costingMethod);
			BigDecimal cost = m_qty.multiply(itemCost);
			cost = cost.setScale(as.getCostingPrecision(), BigDecimal.ROUND_HALF_UP);
			log.fine("Qty(" + m_qty + ") * Cost(" + itemCost + ") = " + cost);
			return cost;
		}
		**/
		
		//	No Product
		if (m_product == null)
		{
			log.fine("No Product");
			return null;
		}
		//
		BigDecimal cost = MCost.getCurrentCost (m_product, m_M_AttributeSetInstance_ID, 
			as, AD_Org_ID, costingMethod, m_qty, C_OrderLine_ID, zeroCostsOK, m_trxName);
		if (cost == null)
		{
			log.fine("No Costs");
			return null;
		}
		return cost;
	}   //  getProductCosts


	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("ProductCost[");
		sb.append ("M_Product_ID=").append(m_M_Product_ID)
			.append(",M_AttributeSetInstance_ID").append(m_M_AttributeSetInstance_ID)
			.append (",Qty=").append(m_qty)
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	ProductCost
