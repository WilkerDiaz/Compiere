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
 *	Product Locator Model
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class MProductLocator extends X_M_ProductLocator
{
    /** Logger for class MProductLocator */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProductLocator.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get array of active Locators for Product and warehouse ordered by priority
	 *	@param product product
	 *	@param M_Warehouse_ID wh
	 *	@return product locators
	 */
	public static MLocator[] getLocators (MProduct product, int M_Warehouse_ID)
	{
		ArrayList<MLocator> list = new ArrayList<MLocator>();
		String sql = "SELECT * FROM M_Locator l "
			+ "WHERE l.IsActive='Y'"
			+ " AND (M_Locator_ID IN (SELECT M_Locator_ID FROM M_Product WHERE M_Product_ID=?)"
			+ " OR M_Locator_ID IN (SELECT M_Locator_ID FROM M_ProductLocator WHERE M_Product_ID=? AND IsActive='Y'))"
			+ " AND M_Warehouse_ID=? "
			+ "ORDER BY PriorityNo DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, product.get_Trx());
			pstmt.setInt (1, product.getM_Product_ID());
			pstmt.setInt (2, product.getM_Product_ID());
			pstmt.setInt (3, M_Warehouse_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MLocator (product.getCtx(), rs, product.get_Trx()));
			}
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MLocator[] retValue = new MLocator[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getLocator
	
	/**
	 * 	Get First M_Locator_ID for product and Warehouse ordered by priority of Locator
	 *	@param product product
	 *	@param M_Warehouse_ID wh
	 *	@return locator or 0 if none
	 */
	public static int getFirstM_Locator_ID (MProduct product, int M_Warehouse_ID)
	{
		if (product == null || M_Warehouse_ID == 0)
			return 0;
		//
		int M_Locator_ID = 0;
		String sql = "SELECT M_Locator_ID FROM M_Locator l "
			+ "WHERE l.IsActive='Y'"
			+ " AND (M_Locator_ID IN (SELECT M_Locator_ID FROM M_Product WHERE M_Product_ID=?)"
			+ " OR M_Locator_ID IN (SELECT M_Locator_ID FROM M_ProductLocator WHERE M_Product_ID=? AND IsActive='Y'))"
			+ " AND M_Warehouse_ID=? "
			+ "ORDER BY PriorityNo DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, product.get_Trx());
			pstmt.setInt (1, product.getM_Product_ID());
			pstmt.setInt (2, product.getM_Product_ID());
			pstmt.setInt (3, M_Warehouse_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				M_Locator_ID = rs.getInt(1);
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
		return M_Locator_ID;
	}	//	getFirstM_Locator_ID
	
	/**
	 * 	Get MProductLocator from Product and Locator
	 *	@param ctx context
	 *	@param M_Product_ID Product id
	 *  @param M_Locator_ID Product id
	 *	@return MProductLocator if exists, otherwise null
	 */
	public static MProductLocator getOfProductLocator(Ctx ctx, int M_Product_ID, int M_Locator_ID)
	{
		if(M_Product_ID==0 || M_Locator_ID==0)
			return null;
		
		int M_ProductLocator_ID=0;
		String sql = "SELECT M_ProductLocator_ID FROM M_ProductLocator l "
			+ "WHERE l.IsActive='Y'"
			+ " AND M_Locator_ID=?"
			+ " AND M_Product_ID=?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, M_Locator_ID);
			pstmt.setInt (2, M_Product_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				M_ProductLocator_ID = rs.getInt(1);
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if(M_ProductLocator_ID == 0)
			return null;
		
		return new MProductLocator(ctx, M_ProductLocator_ID, null);

	}
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MProductLocator.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_ProductLocator_ID id
	 *	@param trx p_trx
	 */
	public MProductLocator(Ctx ctx, int M_ProductLocator_ID,
		Trx trx)
	{
		super (ctx, M_ProductLocator_ID, trx);
	}	//	MProductLocator

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MProductLocator(Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MProductLocator
	
	public MProductLocator(Ctx ctx, int M_Product_ID, int M_Locator_ID, Trx trx)
	{
		super(ctx, 0, trx);
		setM_Product_ID(M_Product_ID);
		setM_Locator_ID(M_Locator_ID);
	}
	
	@Override
	protected boolean beforeSave(boolean newRecord) 
	{
		if(newRecord || is_ValueChanged("M_Locator_ID"))
		{
			int ii = QueryUtil.getSQLValue(get_Trx(), "SELECT count(*) " +
				"FROM M_ProductLocator pl WHERE pl.M_Product_ID=? " +
				"AND pl.M_Locator_ID=? ", getM_Product_ID(), getM_Locator_ID());
			if(ii > 0)
			{
				log.saveError("Error", Msg.getMsg(getCtx(), "LocatorAlreadyLinkedToProduct"));
				return false;
			}	
		}
		return true;
	}
}	//	MProductLocator
