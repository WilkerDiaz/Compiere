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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Create Price List
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class PriceListCreate extends SvrProcess 
{
	/** Delete Old Prices			*/
	private boolean 	p_DeleteOld = false;
	/** Price List Version			*/
	private int			p_M_PriceList_Version_ID = 0;
	/** Price List Version			*/
	private MPriceListVersion 	m_plv = null;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("DeleteOld"))
				p_DeleteOld = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_M_PriceList_Version_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info ("M_PriceList_Version_ID=" + p_M_PriceList_Version_ID 
				+ ", DeleteOld=" + p_DeleteOld);
		m_plv = new MPriceListVersion (getCtx(), p_M_PriceList_Version_ID, get_TrxName());
		if (m_plv.get_ID() == 0 || m_plv.get_ID() != p_M_PriceList_Version_ID)
			throw new CompiereUserException("@NotFound@  @M_PriceList_Version_ID@=" + p_M_PriceList_Version_ID);
		//	
		String error = checkPrerequisites();
		if (error != null && error.length() > 0)
			throw new CompiereUserException(error);
		return create();
	}	//	doIt

	/**
	 * 	Prepare Calculations
	 *	@return error message
	 */
	private String checkPrerequisites()
	{
		String clientWhere = " AND AD_Client_ID= ? ";
		
		DB.executeUpdate(
			get_TrxName(),
			"UPDATE M_Product_PO SET PriceList = 0 WHERE PriceList IS NULL" + clientWhere,
			m_plv.getAD_Client_ID());
		DB.executeUpdate(
			get_TrxName(),
			"UPDATE M_Product_PO SET PriceLastPO = 0 WHERE PriceLastPO IS NULL" + clientWhere,
			m_plv.getAD_Client_ID());
		DB.executeUpdate(
			get_TrxName(),
			"UPDATE M_Product_PO SET PricePO = PriceLastPO "
			+ "WHERE (PricePO IS NULL OR PricePO = 0) AND PriceLastPO <> 0" + clientWhere,
			m_plv.getAD_Client_ID());
		DB.executeUpdate(
			get_TrxName(),
			"UPDATE	M_Product_PO SET PricePO = 0 WHERE PricePO IS NULL" + clientWhere,
			m_plv.getAD_Client_ID());
		DB.executeUpdate(
			get_TrxName(),
			"UPDATE M_Product_PO p SET IsCurrentVendor = 'Y' "
			+ "WHERE IsCurrentVendor = 'N'"
			+ " AND NOT EXISTS "
				+ "(SELECT pp.M_Product_ID FROM M_Product_PO pp "
				+ "WHERE pp.M_Product_ID=p.M_Product_ID "
				+ "GROUP BY pp.M_Product_ID HAVING COUNT(*) > 1)" + clientWhere,
				m_plv.getAD_Client_ID());

		/**
		 *	Make sure that we have only one active product vendor
		 */
		String sql = "SELECT * FROM M_Product_PO po "
			+ "WHERE IsCurrentVendor='Y' AND IsActive='Y'"
			+ clientWhere
			+ " AND EXISTS (SELECT M_Product_ID FROM M_Product_PO x "
				+ "WHERE x.M_Product_ID=po.M_Product_ID"
				+ " AND IsCurrentVendor='Y' AND IsActive='Y' "
				+ "GROUP BY M_Product_ID HAVING COUNT(*) > 1) "
			+ "ORDER BY M_Product_ID, Created";
		
		int success = 0;
		int errors = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt(1, m_plv.getAD_Client_ID());
			rs = pstmt.executeQuery ();
			int M_Product_ID = 0;
			while (rs.next ())
			{
				MProductPO po = new MProductPO (getCtx(), rs, get_TrxName());
				if (M_Product_ID != po.getM_Product_ID())
				{
					M_Product_ID = po.getM_Product_ID();
					continue;
				}
				po.setIsCurrentVendor (false);
				if (po.save())
					success++;
				else
				{
					errors++;
					log.warning("Not updated " + po);
				}
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		log.info ("Current Vendor - Changes=" + success + ", Errors=" + errors);
		return null;
	}	//	checkPrerequisites
	
	/**
	 * 	Create Price List
	 *	@return info message
	 */
	private String create() throws Exception
	{
		StringBuffer info = new StringBuffer();
		
		/**	Delete Old Data	*/
		if (p_DeleteOld)
		{
			int no = DB.executeUpdate(
				get_TrxName(),
				"DELETE FROM M_ProductPrice "
				+ "WHERE M_PriceList_Version_ID= ? ", p_M_PriceList_Version_ID);
			log.info("Deleted=" + no);
			info.append("@Deleted@=").append(no).append(" - ");
		}

		int M_Pricelist_Version_Base_ID = m_plv.getM_Pricelist_Version_Base_ID();
		MPriceList pl = m_plv.getPriceList();
		int curPrecision = pl.getStandardPrecision();
		
		/**
		 *	For All Discount Lines in Sequence
		 */
		MDiscountSchema ds = new MDiscountSchema(getCtx(), m_plv.getM_DiscountSchema_ID(), get_TrxName());
		MDiscountSchemaLine[] dsl = ds.getLines(false);
		for (MDiscountSchemaLine dsLine : dsl) {
			// ignore inactive discount schema lines
			if( !dsLine.isActive() )
				continue;

			String message = "#" + dsLine.getSeqNo();
			String dd = dsLine.getDescription();
			if (dd != null && dd.length() > 0)
				message += " " + dd;
			
			//	Clear Temporary Table
			int noDeleted = DB.executeUpdate(get_TrxName(), "DELETE FROM T_Selection");

			//	Create Selection in Temporary Table
			String sql = null;
			int M_DiscountSchemaLine_ID = dsLine.getM_DiscountSchemaLine_ID();
			int p2 = M_Pricelist_Version_Base_ID;
			if (p2 == 0)	//	Create from PO	**
			{
				sql = "INSERT INTO T_Selection (T_Selection_ID) "
					+ "SELECT DISTINCT po.M_Product_ID "
					+ "FROM M_Product_PO po "
					+ " INNER JOIN M_Product p ON (p.M_Product_ID=po.M_Product_ID)"
					+ " INNER JOIN M_DiscountSchemaLine dl ON (dl.M_DiscountSchemaLine_ID=?) "	//	#1
					+ "WHERE p.AD_Client_ID IN (?, 0)"		//	#2
					+ " AND p.IsActive='Y' AND po.IsActive='Y' AND po.IsCurrentVendor='Y'"
					//	Optional Restrictions
					+ " AND (dl.M_Product_Category_ID IS NULL OR p.M_Product_Category_ID=dl.M_Product_Category_ID)"
					+ " AND (dl.C_BPartner_ID IS NULL OR po.C_BPartner_ID=dl.C_BPartner_ID)"
					+ " AND (dl.M_Product_ID IS NULL OR p.M_Product_ID=dl.M_Product_ID)";
				p2 = dsLine.getAD_Client_ID();
			}
			else			//	Create from Price List **
			{
				sql = "INSERT INTO T_Selection (T_Selection_ID) "
					+ "SELECT DISTINCT p.M_Product_ID "
					+ "FROM M_ProductPrice pp"
					+ " INNER JOIN M_Product p ON (p.M_Product_ID=pp.M_Product_ID)"
					+ " INNER JOIN M_DiscountSchemaLine dl ON (dl.M_DiscountSchemaLine_ID=?) "	//	#1
					+ "WHERE pp.M_PriceList_Version_ID=?"	//	#2 PriceList_Version_Base_ID
					+ " AND p.IsActive='Y' AND pp.IsActive='Y'"
					//	Optional Restrictions
					+ " AND (dl.M_Product_Category_ID IS NULL OR p.M_Product_Category_ID=dl.M_Product_Category_ID)"
					+ " AND (dl.C_BPartner_ID IS NULL OR EXISTS "
						+ "(SELECT * FROM M_Product_PO po "
						+ "WHERE po.M_Product_ID=p.M_Product_ID AND po.C_BPartner_ID=dl.C_BPartner_ID))"
					+ " AND (dl.M_Product_ID IS NULL OR p.M_Product_ID=dl.M_Product_ID)";
			}
			PreparedStatement pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, M_DiscountSchemaLine_ID);
			pstmt.setInt (2, p2);
			int noSelected = pstmt.executeUpdate();
			pstmt.close();
			message += ": @Selected@=" + noSelected;
			
			//	Delete Prices in Selection, so that we can insert
			if (M_Pricelist_Version_Base_ID == 0
				|| M_Pricelist_Version_Base_ID != p_M_PriceList_Version_ID)
			{
				sql = "DELETE FROM M_ProductPrice pp "
					+ "WHERE pp.M_PriceList_Version_ID= ? "
					+ " AND EXISTS (SELECT * FROM T_Selection s WHERE pp.M_Product_ID=s.T_Selection_ID)";
				noDeleted = DB.executeUpdate (get_TrxName(), sql,p_M_PriceList_Version_ID);
				message += ", @Deleted@=" + noDeleted;
			}
			
			//	Copy (Insert) Prices
			int noInserted = 0;
			sql = "INSERT INTO M_ProductPrice "
				+ "(M_PriceList_Version_ID, M_Product_ID,"
				+ " AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,"
				+ " PriceList, PriceStd, PriceLimit) ";
			//
			if (M_Pricelist_Version_Base_ID == p_M_PriceList_Version_ID)
				sql = null;	//	We have Prices already
			else if (M_Pricelist_Version_Base_ID == 0)
			{
				/**	Copy and Convert from Product_PO	*/
				sql	+= "SELECT plv.M_PriceList_Version_ID, po.M_Product_ID,"
					+ " plv.AD_Client_ID, plv.AD_Org_ID, 'Y', SysDate, plv.UpdatedBy, SysDate, plv.UpdatedBy,"
				//	Price List
					+ " COALESCE(currencyConvert(po.PriceList,"
					+ " po.C_Currency_ID, pl.C_Currency_ID, dl.ConversionDate, dl.C_ConversionType_ID, plv.AD_Client_ID, plv.AD_Org_ID), -po.PriceList)," 
				//	Price Std
					+ " COALESCE(currencyConvert(po.PriceList,"
					+ "	po.C_Currency_ID, pl.C_Currency_ID, dl.ConversionDate, dl.C_ConversionType_ID, plv.AD_Client_ID, plv.AD_Org_ID), -po.PriceList),"
				//	Price Limit
					+ " COALESCE(currencyConvert(po.PricePO,"
					+ " po.C_Currency_ID, pl.C_Currency_ID, dl.ConversionDate, dl.C_ConversionType_ID, plv.AD_Client_ID, plv.AD_Org_ID), -po.PricePO) "
				//
					+ "FROM M_Product_PO po"
					+ " INNER JOIN M_PriceList_Version plv ON (plv.M_PriceList_Version_ID=?)"	//	#1
					+ " INNER JOIN M_PriceList pl ON (pl.M_PriceList_ID=plv.M_PriceList_ID)"
					+ " INNER JOIN M_DiscountSchemaLine dl ON (dl.M_DiscountSchemaLine_ID=?) "	//	#2
				//
					+ "WHERE EXISTS (SELECT * FROM T_Selection s WHERE po.M_Product_ID=s.T_Selection_ID)"
					+ " AND po.IsCurrentVendor='Y' AND po.IsActive='Y'";
			}
			else
			{
				/**	Copy and Convert from other PriceList_Version	*/
				sql += "SELECT plv.M_PriceList_Version_ID, pp.M_Product_ID,"
					+ " plv.AD_Client_ID, plv.AD_Org_ID, 'Y', SysDate, plv.UpdatedBy, SysDate, plv.UpdatedBy,"
				//	Price List
					+ " COALESCE(currencyConvert(pp.PriceList,"
					+ " bpl.C_Currency_ID, pl.C_Currency_ID, dl.ConversionDate, dl.C_ConversionType_ID, plv.AD_Client_ID, plv.AD_Org_ID), -pp.PriceList)," 
				//	Price Std
					+ " COALESCE(currencyConvert(pp.PriceStd,"
					+ " bpl.C_Currency_ID, pl.C_Currency_ID, dl.ConversionDate, dl.C_ConversionType_ID, plv.AD_Client_ID, plv.AD_Org_ID), -pp.PriceStd)," 
				//	Price Limit
					+ " COALESCE(currencyConvert(pp.PriceLimit,"
					+ " bpl.C_Currency_ID, pl.C_Currency_ID, dl.ConversionDate, dl.C_ConversionType_ID, plv.AD_Client_ID, plv.AD_Org_ID), -pp.PriceLimit) "
				//
					+ "FROM M_ProductPrice pp"
					+ " INNER JOIN M_PriceList_Version plv ON (plv.M_PriceList_Version_ID=?)"	//	#1
					+ " INNER JOIN M_PriceList pl ON (pl.M_PriceList_ID=plv.M_PriceList_ID)"
					+ " INNER JOIN M_PriceList_Version bplv ON (pp.M_PriceList_Version_ID=bplv.M_PriceList_Version_ID)"
					+ " INNER JOIN M_PriceList bpl ON (bplv.M_PriceList_ID=bpl.M_PriceList_ID)"
					+ " INNER JOIN M_DiscountSchemaLine dl ON (dl.M_DiscountSchemaLine_ID=?) "	//	#2
				//
					+ "WHERE pp.M_PriceList_Version_ID=?"	//	#3 M_PriceList_Version_Base_ID
					+ " AND EXISTS (SELECT * FROM T_Selection s WHERE pp.M_Product_ID=s.T_Selection_ID)"
					+ " AND pp.IsActive='Y'";
			}
			if (sql != null)
			{
				pstmt = DB.prepareStatement (sql, get_TrxName());
				pstmt.setInt (1, p_M_PriceList_Version_ID);
				pstmt.setInt (2, M_DiscountSchemaLine_ID);
				if (M_Pricelist_Version_Base_ID != 0)
					pstmt.setInt (3, M_Pricelist_Version_Base_ID);
				noInserted = pstmt.executeUpdate();
				pstmt.close();
				message += " @Inserted@=" + noInserted;
			}
			
			/** Calculations	**/
			MProductPrice[] pp = m_plv.getProductPrice(
				"AND EXISTS (SELECT * FROM T_Selection s "
				+ "WHERE s.T_Selection_ID=M_ProductPrice.M_Product_ID)");
			for (MProductPrice price : pp) {
				BigDecimal priceList = price.getPriceList();
				BigDecimal priceStd = price.getPriceStd();
				BigDecimal priceLimit = price.getPriceLimit();
				//
				price.setPriceList(calculate (dsLine.getList_Base(),
					priceList, priceStd, priceLimit, dsLine.getList_Fixed(),
					dsLine.getList_AddAmt(), dsLine.getList_Discount(),
					dsLine.getList_Rounding(), curPrecision));
				price.setPriceStd (calculate (dsLine.getStd_Base(),
					priceList, priceStd, priceLimit, dsLine.getStd_Fixed(),
					dsLine.getStd_AddAmt(), dsLine.getStd_Discount(),
					dsLine.getStd_Rounding(), curPrecision));
				price.setPriceLimit(calculate (dsLine.getLimit_Base(),
					priceList, priceStd, priceLimit, dsLine.getLimit_Fixed(),
					dsLine.getLimit_AddAmt(), dsLine.getLimit_Discount(),
					dsLine.getLimit_Rounding(), curPrecision));
				price.save();
			}	//	for all products
			
			//	Clear Temporary Table
			noDeleted = DB.executeUpdate(get_TrxName(), "DELETE FROM T_Selection");
			//
			addLog(message);
		}	//	for all lines
		
		MProductPrice[] pp = m_plv.getProductPrice(true);
		info.append(" - @Records@=").append(pp.length);
		return info.toString();
	}	//	create
	
	/**
	 * 	Calculate Price
	 *	@param base rule
	 *	@param list price
	 *	@param std price
	 *	@param limit price
	 *	@param fix amount
	 *	@param add amount
	 *	@param discount percent
	 *	@param round rule
	 *	@return calculated price
	 */
	private BigDecimal calculate (String base, 
		BigDecimal list, BigDecimal std, BigDecimal limit, BigDecimal fix, 
		BigDecimal add, BigDecimal discount, String round, int curPrecision)
	{
		BigDecimal calc = null;
		double dd = 0.0;
		if (X_M_DiscountSchemaLine.LIST_BASE_ListPrice.equals(base))
			dd = list.doubleValue();
		else if (X_M_DiscountSchemaLine.LIST_BASE_StandardPrice.equals(base))
			dd = std.doubleValue();
		else if (X_M_DiscountSchemaLine.LIST_BASE_LimitPOPrice.equals(base))
			dd = limit.doubleValue();
		else if (X_M_DiscountSchemaLine.LIST_BASE_FixedPrice.equals(base))
			calc = fix;
		else
			throw new IllegalArgumentException("Unknown Base=" + base);
		
		if (calc == null)
		{
			if (add.signum() != 0)
				dd += add.doubleValue();
			if (discount.signum() != 0)
				dd *= 1 - (discount.doubleValue()/100.0);
			calc = new BigDecimal(dd);
		}
		//	Rounding
		if (X_M_DiscountSchemaLine.LIST_ROUNDING_CurrencyPrecision.equals(round))
			calc = calc.setScale(curPrecision, BigDecimal.ROUND_HALF_UP);
		else if (X_M_DiscountSchemaLine.LIST_ROUNDING_Dime102030.equals(round))
			calc = calc.setScale(1, BigDecimal.ROUND_HALF_UP);
		else if (X_M_DiscountSchemaLine.LIST_ROUNDING_Hundred.equals(round))
			calc = calc.setScale(-2, BigDecimal.ROUND_HALF_UP);
		else if (X_M_DiscountSchemaLine.LIST_ROUNDING_Nickel051015.equals(round))
		{
			BigDecimal mm = new BigDecimal(20);
			calc = calc.multiply(mm); 
			calc = calc.setScale(0, BigDecimal.ROUND_HALF_UP);
			calc = calc.divide(mm, 2, BigDecimal.ROUND_HALF_UP);
		}
		else if (X_M_DiscountSchemaLine.LIST_ROUNDING_NoRounding.equals(round))
			;
		else if (X_M_DiscountSchemaLine.LIST_ROUNDING_Quarter255075.equals(round))
		{
			BigDecimal mm = new BigDecimal(4);
			calc = calc.multiply(mm); 
			calc = calc.setScale(0, BigDecimal.ROUND_HALF_UP);
			calc = calc.divide(mm, 2, BigDecimal.ROUND_HALF_UP);
		}
		else if (X_M_DiscountSchemaLine.LIST_ROUNDING_Ten10002000.equals(round))
			calc = calc.setScale(-1, BigDecimal.ROUND_HALF_UP);
		else if (X_M_DiscountSchemaLine.LIST_ROUNDING_Thousand.equals(round))
			calc = calc.setScale(-3, BigDecimal.ROUND_HALF_UP);
		else if (X_M_DiscountSchemaLine.LIST_ROUNDING_WholeNumber00.equals(round))
			calc = calc.setScale(0, BigDecimal.ROUND_HALF_UP);
		
		return calc;
	}	//	calculate
	
}	//	PriceListCreate
