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

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Price List Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPriceList.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MPriceList extends X_M_PriceList
{
    /** Logger for class MPriceList */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPriceList.class);
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Price List (cached)
	 *	@param ctx context
	 *	@param M_PriceList_ID id
	 *	@param trx transaction
	 *	@return PriceList
	 */
	public static MPriceList get (Ctx ctx, int M_PriceList_ID, Trx trx)
	{
		Integer key = Integer.valueOf (M_PriceList_ID);
		MPriceList retValue = s_cache.get(ctx, key);
		if (retValue == null)
		{
			retValue = new MPriceList (ctx, M_PriceList_ID, trx);
			s_cache.put(key, retValue);
		}
		return retValue;		
	}	//	get
	
	/**
	 * 	Get Default Price List for Client (cached)
	 *	@param ctx context
	 *	@param IsSOPriceList SO or PO
	 *	@return PriceList or null
	 */
	public static MPriceList getDefault (Ctx ctx, boolean IsSOPriceList)
	{
		int AD_Client_ID = ctx.getAD_Client_ID();
		MPriceList retValue = null;
		//	Search for it in cache
		Iterator<MPriceList> it = s_cache.values().iterator();
		while (it.hasNext())
		{
			retValue = it.next();
			if (retValue.isDefault() && retValue.getAD_Client_ID() == AD_Client_ID 
					&& retValue.isSOPriceList()== IsSOPriceList)
				return retValue;
		}
		
		/**	Get from DB **/
		retValue = null;
		String sql = "SELECT * FROM M_PriceList "
			+ "WHERE AD_Client_ID=?"
			+ " AND IsDefault='Y'"
			+ " AND IsSOPriceList=?" // YS: Changed from hard code to Parameter
			+ "ORDER BY M_PriceList_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			if (IsSOPriceList)
				pstmt.setString (2, "Y");
			else
				pstmt.setString (2, "N");
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MPriceList (ctx, rs, null);
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
		//	Return value
		if (retValue != null)
		{
			Integer key = Integer.valueOf (retValue.getM_PriceList_ID());
			s_cache.put(key, retValue);
		}
		return retValue;
	}	//	getDefault
	
	/**
	 * 	Get Standard Currency Precision
	 *	@param ctx context 
	 *	@param M_PriceList_ID price list
	 *	@return precision
	 */
	public static int getStandardPrecision (Ctx ctx, int M_PriceList_ID)
	{
		MPriceList pl = MPriceList.get(ctx, M_PriceList_ID, null);
		return pl.getStandardPrecision();
	}	//	getStandardPrecision
	
	/**
	 * 	Get Price List Precision
	 *	@param ctx context 
	 *	@param M_PriceList_ID price list
	 *	@return precision
	 */
	public static int getPricePrecision (Ctx ctx, int M_PriceList_ID)
	{
		MPriceList pl = MPriceList.get(ctx, M_PriceList_ID, null);
		return pl.getPricePrecision();
	}	//	getPricePrecision
	
	/** Static Logger					*/
	private static final CLogger 	s_log = CLogger.getCLogger(MPriceList.class);
	/** Cache of Price Lists			*/
	private static final CCache<Integer,MPriceList> s_cache = new CCache<Integer,MPriceList>("M_PriceList", 5);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_PriceList_ID id
	 *	@param trx transaction
	 */
	public MPriceList(Ctx ctx, int M_PriceList_ID, Trx trx)
	{
		super(ctx, M_PriceList_ID, trx);
		if (M_PriceList_ID == 0)
		{
			setEnforcePriceLimit (false);
			setIsDefault (false);
			setIsSOPriceList (false);
			setIsTaxIncluded (false);
			setPricePrecision (2);	// 2
		//	setName (null);
		//	setC_Currency_ID (0);
		}
	}	//	MPriceList

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPriceList (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPriceList

	/**	Cached PLV					*/
	private MPriceListVersion	m_plv = null;
	/** Cached Precision			*/
	private Integer				m_precision = null;

	/**
	 * 	Get Price List Version
	 *	@param valid date where PLV must be valid or today if null
	 *	@return PLV
	 */
	public MPriceListVersion getPriceListVersion (Timestamp valid)
	{
		if (valid == null)
			valid = new Timestamp (System.currentTimeMillis());
		//	Assume there is no later
		if (m_plv != null && m_plv.getValidFrom().before(valid))
			return m_plv;

		String sql = "SELECT * FROM M_PriceList_Version "
			+ "WHERE M_PriceList_ID=?"
			+ " AND TRUNC(ValidFrom,'DD')<=? AND IsActive='Y'"
			+ "ORDER BY ValidFrom DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getM_PriceList_ID());
			pstmt.setTimestamp(2, valid);
			rs = pstmt.executeQuery();
			if (rs.next())
				m_plv = new MPriceListVersion (getCtx(), rs, get_Trx());
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
		if (m_plv == null)
			log.warning("None found M_PriceList_ID=" 
				+ getM_PriceList_ID() + " - " + valid + " - " + sql);
		else
			log.fine(m_plv.toString());
		return m_plv;
	}	//	getPriceListVersion

	/**
	 * 	Get Standard Currency Precision
	 *	@return precision
	 */
	public int getStandardPrecision()
	{
		if (m_precision == null)
		{
			MCurrency c = MCurrency.get(getCtx(), getC_Currency_ID());
			m_precision = Integer.valueOf (c.getStdPrecision());
		}
		return m_precision.intValue();
	}	//	getStandardPrecision
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MPriceList[").append(get_ID())
	        .append("-").append(getName());
	    sb.append("]");
	    return sb.toString();
    }	//	toString
	
}	//	MPriceList
