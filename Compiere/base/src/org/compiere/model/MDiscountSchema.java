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
 *	Discount Schema Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDiscountSchema.java,v 1.3 2006/07/30 00:51:04 jjanke Exp $
 */
public class MDiscountSchema extends X_M_DiscountSchema
{
    /** Logger for class MDiscountSchema */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDiscountSchema.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Discount Schema from Cache
	 *	@param ctx context
	 *	@param M_DiscountSchema_ID id
	 *	@return MDiscountSchema
	 */
	public static MDiscountSchema get (Ctx ctx, int M_DiscountSchema_ID)
	{
		Integer key = Integer.valueOf (M_DiscountSchema_ID);
		MDiscountSchema retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MDiscountSchema (ctx, M_DiscountSchema_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer,MDiscountSchema>	s_cache
		= new CCache<Integer,MDiscountSchema>("M_DiscountSchema", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_DiscountSchema_ID id
	 *	@param trx transaction
	 */
	public MDiscountSchema (Ctx ctx, int M_DiscountSchema_ID, Trx trx)
	{
		super (ctx, M_DiscountSchema_ID, trx);
		if (M_DiscountSchema_ID == 0)
		{
		//	setName();
			setDiscountType (DISCOUNTTYPE_FlatPercent);
			setFlatDiscount(Env.ZERO);
			setIsBPartnerFlatDiscount (false);
			setIsQuantityBased (true);	// Y
			setCumulativeLevel(CUMULATIVELEVEL_Line);
		//	setValidFrom (new Timestamp(System.currentTimeMillis()));
		}	
	}	//	MDiscountSchema

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MDiscountSchema (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDiscountSchema

	/**	Breaks							*/
	private MDiscountSchemaBreak[]	m_breaks  = null;
	/**	Lines							*/
	private MDiscountSchemaLine[]	m_lines  = null;
	
	/**
	 * 	Get Breaks
	 *	@param reload reload
	 *	@return breaks
	 */
	public MDiscountSchemaBreak[] getBreaks(boolean reload)
	{
		if (m_breaks != null && !reload)
			return m_breaks;
		
		String sql = "SELECT * FROM M_DiscountSchemaBreak WHERE M_DiscountSchema_ID=? ORDER BY SeqNo";
		ArrayList<MDiscountSchemaBreak> list = new ArrayList<MDiscountSchemaBreak>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getM_DiscountSchema_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MDiscountSchemaBreak(getCtx(), rs, get_Trx()));
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
		m_breaks = new MDiscountSchemaBreak[list.size ()];
		list.toArray (m_breaks);
		return m_breaks;
	}	//	getBreaks
	
	/**
	 * 	Get Lines
	 *	@param reload reload
	 *	@return lines
	 */
	public MDiscountSchemaLine[] getLines (boolean reload)
	{
		if (m_lines != null && !reload)
			return m_lines;
		
		String sql = "SELECT * FROM M_DiscountSchemaLine WHERE M_DiscountSchema_ID=? ORDER BY SeqNo";
		ArrayList<MDiscountSchemaLine> list = new ArrayList<MDiscountSchemaLine>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getM_DiscountSchema_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MDiscountSchemaLine(getCtx(), rs, get_Trx()));
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
		m_lines = new MDiscountSchemaLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getBreaks

	/**
	 * 	Calculate Discounted Price
	 *	@param Qty quantity
	 *	@param Price price
	 *	@param M_Product_ID product
	 *	@param M_Product_Category_ID category
	 *	@param BPartnerFlatDiscount flat discount
	 *	@return discount or zero
	 */
	public BigDecimal calculatePrice (BigDecimal Qty, BigDecimal Price,  
		int M_Product_ID, int M_Product_Category_ID,  
		BigDecimal BPartnerFlatDiscount)
	{
		log.fine("Price=" + Price + ",Qty=" + Qty);
		if (Price == null || Env.ZERO.compareTo(Price) == 0)
			return Price;
		//
		BigDecimal discount = calculateDiscount(Qty, Price, 
			M_Product_ID, M_Product_Category_ID, BPartnerFlatDiscount);
		//	nothing to calculate
		if (discount == null || discount.signum() == 0)
			return Price;
		//
		BigDecimal onehundred = new BigDecimal(100);
		BigDecimal multiplier = (onehundred).subtract(discount);
		multiplier = multiplier.divide(onehundred, 6, BigDecimal.ROUND_HALF_UP);
		BigDecimal newPrice = Price.multiply(multiplier);
		log.fine("=>" + newPrice);
		return newPrice;
	}	//	calculatePrice

	/**
	 * 	Calculate Discount Percentage
	 *	@param Qty quantity
	 *	@param Price price
	 *	@param M_Product_ID product
	 *	@param M_Product_Category_ID category
	 *	@param BPartnerFlatDiscount flat discount
	 *	@return discount or zero
	 */
	public BigDecimal calculateDiscount (BigDecimal Qty, BigDecimal Price,  
		int M_Product_ID, int M_Product_Category_ID,
		BigDecimal BPartnerFlatDiscount)
	{
		if (BPartnerFlatDiscount == null)
			BPartnerFlatDiscount = Env.ZERO;
		
		//
		if (DISCOUNTTYPE_FlatPercent.equals(getDiscountType()))
		{
			if (isBPartnerFlatDiscount())
				return BPartnerFlatDiscount;
			return getFlatDiscount();
		}
		//	Not supported
		else if (DISCOUNTTYPE_Formula.equals(getDiscountType())
			|| DISCOUNTTYPE_Pricelist.equals(getDiscountType()))
		{
			log.info ("Not supported (yet) DiscountType=" + getDiscountType());
			return Env.ZERO;
		}
		
		//	Price Breaks
		getBreaks(false);
		BigDecimal Amt = Price.multiply(Qty);
		if (isQuantityBased())
			log.finer("Qty=" + Qty + ",M_Product_ID=" + M_Product_ID + ",M_Product_Category_ID=" + M_Product_Category_ID);
		else
			log.finer("Amt=" + Amt + ",M_Product_ID=" + M_Product_ID + ",M_Product_Category_ID=" + M_Product_Category_ID);
		for (MDiscountSchemaBreak br : m_breaks) {
			if (!br.isActive())
				continue;
			
			if (isQuantityBased())
			{
				if (!br.applies(Qty, M_Product_ID, M_Product_Category_ID))
				{
					log.finer("No: " + br);
					continue;
				}
				log.finer("Yes: " + br);
			}
			else
			{
				if (!br.applies(Amt, M_Product_ID, M_Product_Category_ID))
				{
					log.finer("No: " + br);
					continue;
				}
				log.finer("Yes: " + br);
			}
			
			//	Line applies
			BigDecimal discount = null;
			if (br.isBPartnerFlatDiscount())
				discount = BPartnerFlatDiscount;
			else
				discount = br.getBreakDiscount();
			log.fine("Discount=>" + discount);
			return discount;
		}	//	for all breaks
		
		return Env.ZERO;
	}	//	calculateDiscount
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getValidFrom() == null)
			setValidFrom (TimeUtil.getDay(null));

		return true;
	}	//	beforeSave
	
	/**
	 * 	Renumber
	 *	@return lines updated
	 */
	public int reSeq()
	{
		int count = 0;
		//	Lines
		MDiscountSchemaLine[] lines = getLines(true);
		for (int i = 0; i < lines.length; i++)
		{
			int line = (i+1) * 10;
			if (line != lines[i].getSeqNo())
			{
				lines[i].setSeqNo(line);
				if (lines[i].save())
					count++;
			}
		}
		m_lines = null;
		
		//	Breaks
		MDiscountSchemaBreak[] breaks = getBreaks(true);
		for (int i = 0; i < breaks.length; i++)
		{
			int line = (i+1) * 10;
			if (line != breaks[i].getSeqNo())
			{
				breaks[i].setSeqNo(line);
				if (breaks[i].save())
					count++;
			}
		}
		m_breaks = null;
		return count;
	}	//	reSeq
	
}	//	MDiscountSchema
