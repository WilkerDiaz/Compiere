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
import java.util.ArrayList;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Product Category Model
 *
 *  @author Jorg Janke
 *  @version $Id: MProductCategory.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MProductCategory extends X_M_Product_Category
{
    /** Logger for class MProductCategory */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProductCategory.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get from Cache
	 *	@param ctx context
	 *	@param M_Product_Category_ID id
	 *	@return category
	 */
	public static MProductCategory get (Ctx ctx, int M_Product_Category_ID)
	{
		Integer ii = Integer.valueOf (M_Product_Category_ID);
		MProductCategory pc = s_cache.get(ctx, ii);
		if (pc == null) {
			pc = new MProductCategory (ctx, M_Product_Category_ID, null);
			s_cache.put(M_Product_Category_ID, pc);
		}
		return pc;
	}	//	get

	/**
	 * 	Get MProductCategory from Product
	 *	@param ctx context
	 *	@param M_Product_ID Product id
	 *	@return MProductCategory
	 */
	public static MProductCategory getOfProduct (Ctx ctx, int M_Product_ID)
	{
		MProductCategory retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM M_Product_Category pc "
					+ "WHERE EXISTS (SELECT * FROM M_Product p "
					+ "WHERE p.M_Product_ID=? AND p.M_Product_Category_ID=pc.M_Product_Category_ID)";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, M_Product_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MProductCategory (ctx, rs, null);
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
		return retValue;
	}	//	getOfProduct

	/**
	 * 	Is Product in Category
	 *	@param M_Product_Category_ID category
	 *	@param M_Product_ID product
	 *	@return true if product has category
	 */
	public static boolean isCategory (int M_Product_Category_ID, int M_Product_ID)
	{
		if ((M_Product_ID == 0) || (M_Product_Category_ID == 0))
			return false;
		//	Look up
		Integer product = Integer.valueOf (M_Product_ID);
		Integer category = s_products.get(null, product);
		if (category != null)
			return category.intValue() == M_Product_Category_ID;

		String sql = "SELECT M_Product_Category_ID FROM M_Product WHERE M_Product_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, M_Product_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				category = Integer.valueOf(rs.getInt(1));
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
		if (category != null)
		{
			//	TODO: LRU logic
			s_products.put(product, category);
			//
			s_log.fine("M_Product_ID=" + M_Product_ID + "(" + category
				+ ") in M_Product_Category_ID=" + M_Product_Category_ID
				+ " - " + (category.intValue() == M_Product_Category_ID));
			return category.intValue() == M_Product_Category_ID;
		}
		s_log.log(Level.SEVERE, "Not found M_Product_ID=" + M_Product_ID);
		return false;
	}	//	isCategory

	/**
	 * 	Get Default Category for Client
	 *	@param ctx context
	 *	@return oldest default
	 */
	public static MProductCategory getDefault (Ctx ctx)
	{
		MProductCategory retValue = null;
		String sql = "SELECT * FROM M_Product_Category "
			+ "WHERE AD_Client_ID=? "
			+ "ORDER BY ASCII(IsDefault) DESC, M_Product_Category_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, ctx.getAD_Client_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MProductCategory(ctx, rs, null);
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
		return retValue;
	}	//	getDefault

	/**	Categopry Cache				*/
	private static final CCache<Integer,MProductCategory>	s_cache = new CCache<Integer,MProductCategory>("M_Product_Category", 20);
	/**	Product Cache				*/
	private static final CCache<Integer,Integer> s_products = new CCache<Integer,Integer>("M_Product", 100);
	/**	Static Logger	*/
	private static final CLogger	s_log	= CLogger.getCLogger (MProductCategory.class);


	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_Product_Category_ID id
	 *	@param trx transaction
	 */
	public MProductCategory (Ctx ctx, int M_Product_Category_ID, Trx trx)
	{
		super(ctx, M_Product_Category_ID, trx);
		if (M_Product_Category_ID == 0)
		{
		//	setName (null);
		//	setValue (null);
			setMMPolicy (MMPOLICY_FiFo);	// F
			setPlannedMargin (Env.ZERO);
			setIsDefault (false);
			setIsSelfService (true);	// Y
		}
	}	//	MProductCategory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MProductCategory(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProductCategory

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord & success)
			success = insert_Accounting("M_Product_Category_Acct", "C_AcctSchema_Default", null);

		return success;
	}	//	afterSave

	/**
	 * 	Before Delete
	 *	@return true
	 */
	@Override
	protected boolean beforeDelete ()
	{
		return delete_Accounting("M_Product_Category_Acct");
	}	//	beforeDelete

	/**
	 * 	FiFo Material Movement Policy
	 *	@return true if FiFo
	 */
	public boolean isFiFo()
	{
		return MMPOLICY_FiFo.equals(getMMPolicy());
	}	//	isFiFo
	
	public MProduct[] getProductsofCategory(String WhereClause, Trx trx)
	{
		ArrayList <MProduct> list = new ArrayList<MProduct> ();
		StringBuffer sql = new StringBuffer (" SELECT * FROM M_Product WHERE M_Product_Category_ID = ? ");
		
		if (WhereClause !=null && WhereClause.length()!=0)
			sql.append(WhereClause);
		MRole role = MRole.getDefault(getCtx(), false);
		String stmt = role.addAccessSQL(sql.toString(), "M_Product", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(stmt, trx);
			pstmt.setInt(1,getM_Product_Category_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
			  MProduct	product = new MProduct(getCtx(), rs, trx);
			  list.add(product);
			}
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MProduct retVal[] = new MProduct[list.size()];
		list.toArray(retVal);
		return retVal;
	}


}	//	MProductCategory
