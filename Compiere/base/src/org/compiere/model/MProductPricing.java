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
 *  Product Price Calculations
 *
 *  @author Jorg Janke
 *  @version $Id: MProductPricing.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MProductPricing
{
    /** Logger for class MProductPricing */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProductPricing.class);
	/**
	 * 	Constructor
	 * 	@param M_Product_ID product
	 * 	@param C_BPartner_ID partner
	 * 	@param Qty quantity
	 * 	@param isSOTrx SO or PO
	 */
	public MProductPricing (int AD_Client_ID, int AD_Org_ID,
		int M_Product_ID, int C_BPartner_ID,
		BigDecimal Qty, boolean isSOTrx)
	{
		m_AD_Client_ID = AD_Client_ID;
		m_AD_Org_ID = AD_Org_ID;
		m_M_Product_ID = M_Product_ID;
		m_C_BPartner_ID = C_BPartner_ID;
		if ((Qty != null) && (Env.ZERO.compareTo(Qty) != 0))
			m_Qty = Qty;
		m_isSOTrx = isSOTrx;
	}	//	MProductPricing

	private final int 		m_AD_Client_ID;
	private final int 		m_AD_Org_ID;
	private final int 		m_M_Product_ID;
	private final int 		m_C_BPartner_ID;
	private BigDecimal 	m_Qty = Env.ONE;
	private boolean		m_isSOTrx = true;
	//
	private int			m_M_PriceList_ID = 0;
	private int 		m_M_PriceList_Version_ID = 0;
	private Timestamp 	m_PriceDate;
	/** Precision -1 = no rounding		*/
	private int		 	m_precision = -1;


	private boolean 	m_calculated = false;
	private Boolean		m_found = null;

	private BigDecimal 	m_PriceList = Env.ZERO;
	private BigDecimal 	m_PriceStd = Env.ZERO;
	private BigDecimal 	m_PriceLimit = Env.ZERO;
	private int 		m_C_Currency_ID = 0;
	private boolean		m_enforcePriceLimit = false;
	private int 		m_C_UOM_ID = 0;
	private int 		m_M_Product_Category_ID;
	private boolean		m_discountSchema = false;
	private boolean		m_isTaxIncluded = false;
	private boolean     m_isBOM = false;

	private Boolean		m_userPricing = null;
	private UserPricingInterface 	m_api = null;



	/**
	 * 	Calculate Price
	 * 	@return true if calculated
	 */
	public boolean calculatePrice ()
	{
		if ((m_M_Product_ID == 0)
			|| ((m_found != null) && !m_found.booleanValue()))	//	previously not found
			return false;
		//	Customer Pricing Engine
		if (!m_calculated)
			m_calculated = calculateUser();

		String sql = "SELECT IsBOM FROM M_Product"
			+ " WHERE M_Product_ID=?"
			+ " AND IsBOM= 'Y' AND IsVerified='Y'";
		String isBOM = QueryUtil.getSQLValueString(null, sql, m_M_Product_ID);

		if ((isBOM != null) && ("Y".equals(isBOM)))
			m_isBOM = true;

		//	Price List Version known
		if (!m_calculated)
			m_calculated = calculatePLV();
		//	Price List known
		if (!m_calculated)
			m_calculated = calculatePL();
		//	Base Price List used
		if (!m_calculated)
			m_calculated = calculateBPL();
		//	Set UOM, Prod.Category
		if (!m_calculated)
			setBaseInfo();
		//	User based Discount
		if (m_calculated)
			calculateDiscount();
		setPrecision();		//	from Price List
		//
		m_found = Boolean.valueOf (m_calculated);
		return m_calculated;
	}	//	calculatePrice

	/**
	 * 	Calculate User Price
	 *	@return true if calculated
	 */
	private boolean calculateUser()
	{
		if (m_userPricing == null)
		{
			MClientInfo client = MClientInfo.get(Env.getCtx(), m_AD_Client_ID);
			String userClass = client.getPricingEngineClass();
			try
			{
				Class<?> clazz = null;
				if (userClass != null)
					clazz = Class.forName(userClass);
				if (clazz != null)
					m_api = (UserPricingInterface)clazz.newInstance();
			}
			catch (Exception e)
			{
				log.warning("No User Pricing Engine (" + userClass + ") " + e.toString());
				m_userPricing = Boolean.FALSE;
				return false;
			}
			m_userPricing = m_api != null;
		}
		if (!m_userPricing.booleanValue())
			return false;

		UserPricingVO vo = null;
		if (m_api != null)
		{
			try
			{
				vo = m_api.price(m_AD_Org_ID, m_isSOTrx, m_M_PriceList_ID,
					m_C_BPartner_ID, m_M_Product_ID, m_Qty, m_PriceDate);
			}
			catch (Exception e)
			{
				log.warning("Error User Pricing - " + e.toString());
				return false;
			}
		}

		if ((vo != null) && vo.isValid())
		{
			m_PriceList = vo.getPriceList();
			m_PriceStd = vo.getPriceStd();
			m_PriceLimit = vo.getPriceLimit();
			m_found = true;
			//	Optional
			m_C_UOM_ID = vo.getC_UOM_ID();
			m_C_Currency_ID = vo.getC_Currency_ID();
			m_enforcePriceLimit = vo.isEnforcePriceLimit();
			if ((m_C_UOM_ID == 0) || (m_C_Currency_ID == 0))
				setBaseInfo();
		}
		return false;
	}	//	calculateUser


	/**
	 * 	Calculate Price based on Price List Version
	 * 	@return true if calculated
	 */
	private boolean calculatePLV()
	{
		if ((m_M_Product_ID == 0) || (m_M_PriceList_Version_ID == 0))
			return false;

        if (m_isBOM && !checkBOM())
        {
        	log.finer("BOM Component not found on PLV");
            return m_calculated;
        }

		//
		String sql = "SELECT bomPriceStd(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceStd,"	//	1
			+ " bomPriceList(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceList,"		//	2
			+ " bomPriceLimit(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceLimit,"	//	3
			+ " p.C_UOM_ID,pv.ValidFrom,pl.C_Currency_ID,p.M_Product_Category_ID,"	//	4..7
			+ " pl.EnforcePriceLimit, pl.IsTaxIncluded "	// 8..9
			+ "FROM M_Product p"
			+ " INNER JOIN M_ProductPrice pp ON (p.M_Product_ID=pp.M_Product_ID)"
			+ " INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID)"
			+ " INNER JOIN M_PriceList pl ON (pv.M_PriceList_ID=pl.M_PriceList_ID) "
			+ "WHERE pv.IsActive='Y'"
			+ " AND pp.IsActive='Y'"
			+ " AND pl.IsActive='Y'"
			+ " AND p.M_Product_ID=?"				//	#1
			+ " AND pv.M_PriceList_Version_ID=?";	//	#2
		m_calculated = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, m_M_Product_ID);
			pstmt.setInt(2, m_M_PriceList_Version_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Prices
				m_PriceStd = rs.getBigDecimal(1);
				if (rs.wasNull())
					m_PriceStd = Env.ZERO;
				m_PriceList = rs.getBigDecimal(2);
				if (rs.wasNull())
					m_PriceList = Env.ZERO;
				m_PriceLimit = rs.getBigDecimal(3);
				if (rs.wasNull())
					m_PriceLimit = Env.ZERO;
				//
				m_C_UOM_ID = rs.getInt(4);
				m_C_Currency_ID = rs.getInt(6);
				m_M_Product_Category_ID = rs.getInt(7);
				m_enforcePriceLimit = "Y".equals(rs.getString(8));
				m_isTaxIncluded = "Y".equals(rs.getString(9));
				//
				log.fine("M_PriceList_Version_ID=" + m_M_PriceList_Version_ID + " - " + m_PriceStd);
				m_calculated = true;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
			m_calculated = false;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return m_calculated;
	}	//	calculatePLV

	/**
	 * 	Calculate Price based on Price List
	 * 	@return true if calculated
	 */
	private boolean calculatePL()
	{
		if (m_M_Product_ID == 0)
			return false;

		//	Get Price List
		if (m_M_PriceList_ID == 0)
		{
			log.log(Level.WARNING, "No PriceList");
			Trace.printStack();
			return false;
		}

		//	Get Price List Version
		String sql = "SELECT pv.M_PriceList_Version_ID,"	//	1
			+ " p.C_UOM_ID,pv.ValidFrom,pl.C_Currency_ID,p.M_Product_Category_ID,pl.EnforcePriceLimit "	// 2..6
			+ "FROM M_Product p"
			+ " INNER JOIN M_ProductPrice pp ON (p.M_Product_ID=pp.M_Product_ID)"
			+ " INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID)"
			+ " INNER JOIN M_PriceList pl ON (pv.M_PriceList_ID=pl.M_PriceList_ID) "
			+ "WHERE pv.IsActive='Y'"
			+ " AND pp.IsActive='Y'"
			+ " AND pl.IsActive='Y'"
			+ " AND p.M_Product_ID=?"				//	#1
			+ " AND pv.M_PriceList_ID=?"			//	#2
			+ " ORDER BY pv.ValidFrom DESC";
		m_calculated = false;
		boolean foundPriceListVersion = false;
		if (m_PriceDate == null)
			m_PriceDate = new Timestamp (System.currentTimeMillis());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, m_M_Product_ID);
			pstmt.setInt(2, m_M_PriceList_ID);
			rs = pstmt.executeQuery();
			while (!foundPriceListVersion && rs.next())
			{
				Timestamp plDate = rs.getTimestamp(3);
				//	we have the price list
				//	if order date is after or equal PriceList validFrom
				if ((plDate == null) || !m_PriceDate.before(plDate))
				{
					m_M_PriceList_Version_ID = rs.getInt(1);
					m_C_UOM_ID = rs.getInt (2);
					m_C_Currency_ID = rs.getInt (4);
					m_M_Product_Category_ID = rs.getInt(5);
					m_enforcePriceLimit = "Y".equals(rs.getString(6));
					//
					log.fine("M_PriceList_ID=" + m_M_PriceList_ID
						+ "(" + plDate + ")" + ", M_PriceList_Version_ID=" + m_M_PriceList_Version_ID);
					foundPriceListVersion = true;
					break;
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
			m_calculated = false;
			log.finer("SQL Error (PL)");
			return m_calculated;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (!foundPriceListVersion)
			return m_calculated;  // false
		else
		{
			if (m_isBOM && !checkBOM())
			{
				log.finer("BOM Component not found on PL");
				return m_calculated;  // false
			}

            // Get prices using price list version
            sql = "SELECT bomPriceStd(?, ?) AS PriceStd,"       	// 1, 2
                + " bomPriceList(?, ?) AS PriceList,"           	// 3, 4
                + " bomPriceLimit(?, ?) AS PriceLimit FROM DUAL";	// 5, 6
            try
            {
                pstmt = DB.prepareStatement(sql, (Trx) null);
                pstmt.setInt(1, m_M_Product_ID);
                pstmt.setInt(2, m_M_PriceList_Version_ID);
                pstmt.setInt(3, m_M_Product_ID);
                pstmt.setInt(4, m_M_PriceList_Version_ID);
                pstmt.setInt(5, m_M_Product_ID);
                pstmt.setInt(6, m_M_PriceList_Version_ID);
                rs = pstmt.executeQuery();
                if (rs.next())
                {
                	m_PriceStd = rs.getBigDecimal (1);
                    if (rs.wasNull ())
                    	m_PriceStd = Env.ZERO;
                    m_PriceList = rs.getBigDecimal (2);
                    if (rs.wasNull ())
                    	m_PriceList = Env.ZERO;
                    m_PriceLimit = rs.getBigDecimal (3);
                    if (rs.wasNull ())
                    	m_PriceLimit = Env.ZERO;
                    m_calculated = true;
                }
            }
            catch (Exception e)
            {
                log.log(Level.SEVERE, sql, e);
                m_calculated = false;
                return m_calculated;
            }
            finally
            {
            	DB.closeResultSet(rs);
            	DB.closeStatement(pstmt);
            }

            if (!m_calculated)
            	log.finer("Not found (PL)");
            return m_calculated;
		}
	}	//	calculatePL

	/**
	 * 	Calculate Price based on Base Price List
	 * 	@return true if calculated
	 */
	private boolean calculateBPL()
	{
		if ((m_M_Product_ID == 0) || (m_M_PriceList_ID == 0))
			return false;
		//
		String sql = "SELECT bomPriceStd(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceStd,"	//	1
			+ " bomPriceList(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceList,"		//	2
			+ " bomPriceLimit(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceLimit,"	//	3
			+ " p.C_UOM_ID,pv.ValidFrom,pl.C_Currency_ID,p.M_Product_Category_ID,"	//	4..7
			+ " pl.EnforcePriceLimit, pl.IsTaxIncluded "	// 8..9
			+ "FROM M_Product p"
			+ " INNER JOIN M_ProductPrice pp ON (p.M_Product_ID=pp.M_Product_ID)"
			+ " INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID)"
			+ " INNER JOIN M_PriceList bpl ON (pv.M_PriceList_ID=bpl.M_PriceList_ID)"
			+ " INNER JOIN M_PriceList pl ON (bpl.M_PriceList_ID=pl.BasePriceList_ID) "
			+ "WHERE pv.IsActive='Y'"
			+ " AND pp.IsActive='Y'"
			+ " AND pl.IsActive='Y'"
			+ " AND bpl.IsActive='Y'"
			+ " AND p.M_Product_ID=?"				//	#1
			+ " AND pl.M_PriceList_ID=? "			//	#2
			+ "ORDER BY pv.ValidFrom DESC";
		m_calculated = false;
		if (m_PriceDate == null)
			m_PriceDate = new Timestamp (System.currentTimeMillis());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, m_M_Product_ID);
			pstmt.setInt(2, m_M_PriceList_ID);
			rs = pstmt.executeQuery();
			while (!m_calculated && rs.next())
			{
				Timestamp plDate = rs.getTimestamp(5);
				//	we have the price list
				//	if order date is after or equal PriceList validFrom
				if ((plDate == null) || !m_PriceDate.before(plDate))
				{
					//	Prices
					m_PriceStd = rs.getBigDecimal (1);
					if (rs.wasNull ())
						m_PriceStd = Env.ZERO;
					m_PriceList = rs.getBigDecimal (2);
					if (rs.wasNull ())
						m_PriceList = Env.ZERO;
					m_PriceLimit = rs.getBigDecimal (3);
					if (rs.wasNull ())
						m_PriceLimit = Env.ZERO;
					//
					m_C_UOM_ID = rs.getInt (4);
					m_C_Currency_ID = rs.getInt (6);
					m_M_Product_Category_ID = rs.getInt(7);
					m_enforcePriceLimit = "Y".equals(rs.getString(8));
					m_isTaxIncluded = "Y".equals(rs.getString(9));
					//
					log.fine("M_PriceList_ID=" + m_M_PriceList_ID
						+ "(" + plDate + ")" + " - " + m_PriceStd);
					m_calculated = true;
					break;
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
			m_calculated = false;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (!m_calculated)
			log.finer("Not found (BPL)");
		return m_calculated;
	}	//	calculateBPL

    /**
     * Traverse BOM components and check if they are on the price list version
     * @return false if any BOM component is not on the price list version
     */
    private boolean checkBOM()
    {
    	boolean retvalue = false;

    	if ((m_M_Product_ID == 0) || (m_M_PriceList_Version_ID == 0))
    		return retvalue;

    	String sql = "SELECT bomPriceCheck(?, ?) FROM DUAL";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
    	try
        {
    		pstmt = DB.prepareStatement(sql, (Trx) null);
            pstmt.setInt(1, m_M_Product_ID);
            pstmt.setInt(2, m_M_PriceList_Version_ID);
            rs = pstmt.executeQuery();
            if (rs.next())
            {
            	String result = rs.getString(1);
                if (result.equals("Y"))
                {
                	retvalue = true;
                }
            }
         }
         catch (Exception e)
         {
        	 log.log(Level.SEVERE, sql, e);
             retvalue = false;
         }
         finally
         {
        	 DB.closeResultSet(rs);
        	 DB.closeStatement(pstmt);
         }
         return retvalue;
    }           // checkBOM

    /**
	 * 	Set Base Info (UOM)
	 */
	private void setBaseInfo()
	{
		if (m_M_Product_ID == 0)
			return;
		//
		String sql = "SELECT C_UOM_ID, M_Product_Category_ID FROM M_Product WHERE M_Product_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, m_M_Product_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				m_C_UOM_ID = rs.getInt (1);
				m_M_Product_Category_ID = rs.getInt(2);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	setBaseInfo

	/**
	 * 	Is Tax Included
	 *	@return tax included
	 */
	public boolean isTaxIncluded()
	{
		return m_isTaxIncluded;
	}	//	isTaxIncluded


	/**************************************************************************
	 * 	Calculate (Business Partner) Discount
	 */
	private void calculateDiscount()
	{
		m_discountSchema = false;
		if ((m_C_BPartner_ID == 0) || (m_M_Product_ID == 0))
			return;

		int M_DiscountSchema_ID = 0;
		BigDecimal FlatDiscount = null;
		String sql = "SELECT COALESCE(p.M_DiscountSchema_ID,g.M_DiscountSchema_ID),"
			+ " COALESCE(p.PO_DiscountSchema_ID,g.PO_DiscountSchema_ID), p.FlatDiscount "
			+ "FROM C_BPartner p"
			+ " INNER JOIN C_BP_Group g ON (p.C_BP_Group_ID=g.C_BP_Group_ID) "
			+ "WHERE p.C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, m_C_BPartner_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				M_DiscountSchema_ID = rs.getInt(m_isSOTrx ? 1 : 2);
				FlatDiscount = rs.getBigDecimal(3);
				if (FlatDiscount == null)
					FlatDiscount = Env.ZERO;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//	No Discount Schema
		if (M_DiscountSchema_ID == 0)
			return;

		MDiscountSchema sd = MDiscountSchema.get(Env.getCtx(), M_DiscountSchema_ID);	//	not correct
		if ((sd.get_ID() == 0) || (m_PriceDate==null))
			return;


		if (sd.getValidFrom().after(m_PriceDate))
			return;
		//
		m_discountSchema = true;
		m_PriceStd = sd.calculatePrice(m_Qty, m_PriceStd, m_M_Product_ID,
			m_M_Product_Category_ID, FlatDiscount);

	}	//	calculateDiscount


	/**************************************************************************
	 * 	Calculate Discount Percentage based on Standard/List Price
	 * 	@return Discount
	 */
	public BigDecimal getDiscount()
	{
		BigDecimal Discount = Env.ZERO;
		if (m_PriceList.intValue() != 0)
			Discount = new BigDecimal ((m_PriceList.doubleValue() - m_PriceStd.doubleValue())
				/ m_PriceList.doubleValue() * 100.0);
		if (Discount.scale() > 2)
			Discount = Discount.setScale(2, BigDecimal.ROUND_HALF_UP);
		return Discount;
	}	//	getDiscount


	/**
	 * 	Get Line Amt
	 * 	@param currencyPrecision precision -1 = ignore
	 *	@return Standard Price * Qty
	 */
	public BigDecimal getLineAmt(int currencyPrecision)
	{
		BigDecimal amt = getPriceStd().multiply(m_Qty);
		//	Currency Precision
		if ((currencyPrecision >= 0) && (amt.scale() > currencyPrecision))
			amt = amt.setScale(currencyPrecision, BigDecimal.ROUND_HALF_UP);
		return amt;
	}	//	getLineAmt


	/**************************************************************************
	 * 	Get Product ID
	 *	@return id
	 */
	public int getM_Product_ID()
	{
		return m_M_Product_ID;
	}

	/**
	 * 	Get PriceList ID
	 *	@return pl
	 */
	public int getM_PriceList_ID()
	{
		return m_M_PriceList_ID;
	}	//	getM_PriceList_ID

	/**
	 * 	Set PriceList
	 *	@param M_PriceList_ID pl
	 */
	public void setM_PriceList_ID(int M_PriceList_ID)
	{
		m_M_PriceList_ID = M_PriceList_ID;
		m_calculated = false;
	}	//	setM_PriceList_ID

	/**
	 * 	Get PriceList Version
	 *	@return plv
	 */
	public int getM_PriceList_Version_ID()
	{
		return m_M_PriceList_Version_ID;
	}	//	getM_PriceList_Version_ID

	/**
	 * 	Set PriceList Version
	 *	@param M_PriceList_Version_ID plv
	 */
	public void setM_PriceList_Version_ID (int M_PriceList_Version_ID)
	{
		m_M_PriceList_Version_ID = M_PriceList_Version_ID;
		m_calculated = false;
	}	//	setM_PriceList_Version_ID

	/**
	 * 	Get Price Date
	 *	@return date
	 */
	public Timestamp getPriceDate()
	{
		return m_PriceDate;
	}	//	getPriceDate

	/**
	 * 	Set Price Date
	 *	@param priceDate date
	 */
	public void setPriceDate(Timestamp priceDate)
	{
		m_PriceDate = priceDate;
		m_calculated = false;
	}	//	setPriceDate

	/**
	 * 	Set Price Date
	 *	@param priceTime date
	 */
	public void setPriceDate(long priceTime)
	{
		setPriceDate (new Timestamp(priceTime));
	}	//	setPriceDate

	/**
	 * 	Set Price List Precision.
	 */
	private void setPrecision()
	{
		if (m_M_PriceList_ID != 0)
			m_precision = MPriceList.getPricePrecision(Env.getCtx(), getM_PriceList_ID());
	}	//	setPrecision

	/**
	 * 	Get Price List Precision
	 *	@return precision - -1 = no rounding
	 */
	public int getPrecision()
	{
		return m_precision;
	}	//	getPrecision

	/**
	 * 	Round
	 *	@param bd number
	 *	@return rounded number
	 */
	private BigDecimal round (BigDecimal bd)
	{
		if ((m_precision >= 0	//	-1 = no rounding
)
			&& (bd.scale() > m_precision))
			return bd.setScale(m_precision, BigDecimal.ROUND_HALF_UP);
		return bd;
	}	//	round

	/**************************************************************************
	 * 	Get C_UOM_ID
	 *	@return uom
	 */
	public int getC_UOM_ID()
	{
		if (!m_calculated)
			calculatePrice();
		return m_C_UOM_ID;
	}

	/**
	 * 	Get Price List
	 *	@return list
	 */
	public BigDecimal getPriceList()
	{
		if (!m_calculated)
			calculatePrice();
		return round(m_PriceList);
	}
	/**
	 * 	Get Price Std
	 *	@return std
	 */
	public BigDecimal getPriceStd()
	{
		if (!m_calculated)
			calculatePrice();
		return round(m_PriceStd);
	}
	/**
	 * 	Get Price Limit
	 *	@return limit
	 */
	public BigDecimal getPriceLimit()
	{
		if (!m_calculated)
			calculatePrice();
		return round(m_PriceLimit);
	}
	/**
	 * 	Get Price List Currency
	 *	@return currency
	 */
	public int getC_Currency_ID()
	{
		if (!m_calculated)
			calculatePrice();
		return m_C_Currency_ID;
	}
	/**
	 * 	Is Price List enforded?
	 *	@return enforce limit
	 */
	public boolean isEnforcePriceLimit()
	{
		if (!m_calculated)
			calculatePrice();
		return m_enforcePriceLimit;
	}	//	isEnforcePriceLimit

	/**
	 * 	Is a DiscountSchema active?
	 *	@return active Discount Schema
	 */
	public boolean isDiscountSchema()
	{
		return m_discountSchema;
	}	//	isDiscountSchema

	/**
	 * 	Is the Price Calculated (i.e. found)?
	 *	@return calculated
	 */
	public boolean isCalculated()
	{
		return m_calculated;
	}	//	isCalculated

}	//	MProductPrice
