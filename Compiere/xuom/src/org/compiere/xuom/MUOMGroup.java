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
package org.compiere.xuom;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	UOM Group Model
 *	@author Jorg Janke
 */
public class MUOMGroup extends X_C_UOMGroup 
{
    /** Logger for class MUOMGroup */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MUOMGroup.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Products with Group
	 *	@param ctx context
	 *	@param C_UOMGroup_ID group or 0 for all with a group
	 *	@return array of products
	 */
	static MProduct[] getProducts(Ctx ctx, int C_UOMGroup_ID)
	{
		ArrayList<MProduct> list = new ArrayList<MProduct>();
		String sql = "SELECT * FROM M_Product WHERE C_UOMGroup_ID=?";
		if (C_UOMGroup_ID == 0)
			sql = "SELECT * FROM M_Product WHERE C_UOMGroup_ID IS NOT NULL ORDER BY C_UOMGroup_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			if (C_UOMGroup_ID != 0)
				pstmt.setInt(1, C_UOMGroup_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MProduct(ctx, rs, null));
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		pstmt = null;
		rs = null;
		//
		MProduct[] retValue = new MProduct[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getProducts

	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(MUOMGroup.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_UOMGroup_ID id
	 *	@param trx p_trx
	 */
	public MUOMGroup(Ctx ctx, int C_UOMGroup_ID, Trx trx) 
	{
		super(ctx, C_UOMGroup_ID, trx);
	}	//	MUOMGroup

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MUOMGroup(Ctx ctx, ResultSet rs, Trx trx) 
	{
		super(ctx, rs, trx);
	}	//	MUOMGroup

	/** Conversions				*/
	private MUOMGroupConversion[]	m_conversions = null;
	
	/**
	 * 	Get Group Conversions
	 *	@param reload reload
	 *	@return array of conversions
	 */
	public MUOMGroupConversion[] getConversions (boolean reload)
	{
		if (m_conversions != null && !reload)
			return m_conversions;
		
		ArrayList<MUOMGroupConversion> list = new ArrayList<MUOMGroupConversion>();
		String sql = "SELECT * FROM C_UOMGroupConversion WHERE C_UOMGroup_ID=? ORDER BY C_UOM_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_UOMGroup_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MUOMGroupConversion(getCtx(), rs, get_Trx()));
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		pstmt = null;
		rs = null;
		//
		m_conversions = new MUOMGroupConversion[list.size()];
		list.toArray(m_conversions);
		return m_conversions;
	}	//	getConversions
	
	/**
	 * 	Update Product UOMs.
	 *	@param product product
	 *	@param forceSynchronization if false new only - if true update
	 *	@return number of updates
	 */
	public int updateProduct(MProduct product, boolean forceSynchronization)
	{
		if (product == null)
			return 0;
		
		int added = 0;
		int updated = 0;
		MUOMConversion[] productConversions = MUOMConversion.getProductConversions(
				getCtx(), product.getM_Product_ID(), true);
		getConversions(false);
		for (MUOMGroupConversion groupConv : m_conversions) {
			boolean found = false;
			for (MUOMConversion productConv : productConversions) {
				if (groupConv.getC_UOM_ID() == productConv.getC_UOM_To_ID())
				{
					if (forceSynchronization)
					{
						productConv.setMultiplyRate(groupConv.getMultiplyRate());
						productConv.setDivideRate(groupConv.getDivideRate());
						productConv.setIsActive(true);
						if (productConv.save())
							updated++;
					}
					found = true;
					break;
				}
			}
			if (!found)
			{
				MUOMConversion productConv = new MUOMConversion(product);
				productConv.setC_UOM_To_ID (groupConv.getC_UOM_ID());
				productConv.setMultiplyRate(groupConv.getMultiplyRate());
				productConv.setDivideRate(groupConv.getDivideRate());
				if (productConv.save())
					added++;
			}
		}
		
		//	Deactivate Product Conversions
		int deactivated = 0;
		if (forceSynchronization)
		{
			String sql = "UPDATE C_UOM_Conversion pc SET IsActive='N' "
				+ "WHERE pc.C_UOM_To_ID NOT IN (SELECT gc.C_UOM_ID "
					+ "FROM M_Product p"
					+ " INNER JOIN C_UOMGroupConversion gc ON (p.C_UOMGroup_ID=gc.C_UOMGroup_ID) "
					+ "WHERE p.M_Product_ID=pc.M_Product_ID)";
			deactivated = DB.executeUpdate((Trx) null, sql);
		}
		
		log.info(getName() + ": Added #" + added + " Updated #" + updated + " Deactivated #" + deactivated);
		return added + updated;
	}	//	updateProduct
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MUOMGroup[").append(get_ID())
			.append("-").append(getName())
			.append(",C_UOM_ID=").append(getC_UOM_ID())
			;
		sb.append("]");
		return sb.toString();
	}	//	toString
	
}	//	MUOMGroup
