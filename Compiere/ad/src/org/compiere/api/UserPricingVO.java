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
package org.compiere.api;

import java.math.*;

/**
 * 	User Pricing Info.
 * 	You need to provide at least the List price.
 * 	If info available, you should also set Currency and UOM
 *  - otherwise it is derived from Price List and Product
 *	
 *  @author Jorg Janke
 *  @version $Id: UserPricingVO.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class UserPricingVO
{
	/**
	 * 	User Pricing VO
	 *	@param price one price
	 */
	public UserPricingVO (BigDecimal price)
	{
		setPriceList(price);
	}	//	UserPricingVO
	
	/**
	 * 	User Pricing VO
	 *	@param priceList list
	 *	@param priceStd std
	 *	@param priceLimit limit
	 */
	public UserPricingVO (BigDecimal priceList, BigDecimal priceStd, BigDecimal priceLimit)
	{
		setPriceList(priceList);
		setPriceStd(priceStd);
		setPriceLimit(priceLimit);
	}	//	UserPricingVO
	
	/**	Std	(Actual)	*/
	private BigDecimal m_PriceStd = null;
	/** List		*/
	private BigDecimal m_PriceList = null;
	/** Limit		*/
	private BigDecimal m_PriceLimit = null;
	/** Currency		*/
	private int 		m_C_Currency_ID = 0;
	/** Enforce Price Limit	*/
	private boolean		m_enforcePriceLimit = false;
	/** Product UOM		*/
	private int 		m_C_UOM_ID = 0;

	
	/**
	 * 	Do we have valid Price Info
	 *	@return true if there is a list price
	 */
	public boolean isValid()
	{
		return m_PriceList != null;
	}	//	isValid
	
	/**
	 * 	Get Price Std
	 *	@return Std or list
	 */
    public BigDecimal getPriceStd()
    {
    	if (m_PriceStd != null)
    		return m_PriceStd;
    	return m_PriceList;
    }	//	getPriceStd
	
    /**
     * 	Set Price Std
     *	@param priceStd
     */
    public void setPriceStd(BigDecimal priceStd)
    {
    	m_PriceStd = priceStd;
    }
	
    /**
     * 	Get Price Limit
     *	@return limit or Std or list
     */
    public BigDecimal getPriceLimit()
    {
    	if (m_PriceLimit != null)
    		return m_PriceLimit;
    	if (m_PriceStd != null)
    		return m_PriceStd;
    	return m_PriceList;
    }	//	getPriceLimit
	
    /**
     * 	Set Price Limit
     *	@param priceLimit
     */
    public void setPriceLimit(BigDecimal priceLimit)
    {
    	m_PriceLimit = priceLimit;
    }
	
    /**
     * 	Get Price List
     *	@return list
     */
    public BigDecimal getPriceList()
    {
    	return m_PriceList;
    }
	
    /**
     * 	Set Price List
     *	@param priceList
     */
    public void setPriceList(BigDecimal priceList)
    {
    	m_PriceList = priceList;
    }

    public int getC_Currency_ID()
    {
    	return m_C_Currency_ID;
    }

    public void setC_Currency_ID(int currency_ID)
    {
    	m_C_Currency_ID = currency_ID;
    }

	
    public int getC_UOM_ID()
    {
    	return m_C_UOM_ID;
    }
	
    public void setC_UOM_ID(int uom_id)
    {
    	m_C_UOM_ID = uom_id;
    }

	
    public boolean isEnforcePriceLimit()
    {
    	return m_enforcePriceLimit;
    }

    public void setEnforcePriceLimit(boolean priceLimit)
    {
    	m_enforcePriceLimit = priceLimit;
    }
    
}	//	UserPricingVO
