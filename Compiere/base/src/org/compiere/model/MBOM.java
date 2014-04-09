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
 * 	BOM Model
 *  @author Jorg Janke
 *  @version $Id: MBOM.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MBOM extends X_M_BOM
{
    /** Logger for class MBOM */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBOM.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get BOM from Cache
	 *	@param ctx context
	 *	@param M_BOM_ID id
	 *	@return MBOM
	 */
	public static MBOM get (Ctx ctx, int M_BOM_ID)
	{
		Integer key = Integer.valueOf (M_BOM_ID);
		MBOM retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MBOM (ctx, M_BOM_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get BOMs Of Product
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param trx p_trx
	 *	@param whereClause optional WHERE clause w/o AND
	 *	@return array of BOMs
	 */
	public static MBOM[] getOfProduct (Ctx ctx, int M_Product_ID, 
			Trx trx, String whereClause)
	{
		ArrayList<MBOM> list = new ArrayList<MBOM>();
		String sql = "SELECT * FROM M_BOM WHERE M_Product_ID=?";
		if (whereClause != null && whereClause.length() > 0)
			sql += " AND " + whereClause;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_Product_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MBOM (ctx, rs, trx));
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

		MBOM[] retValue = new MBOM[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfProduct

	/**	Cache						*/
	private static final CCache<Integer,MBOM>	s_cache	
	= new CCache<Integer,MBOM>("M_BOM", 20);
	/**	Logger						*/
	private static CLogger	s_log	= CLogger.getCLogger (MBOM.class);


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_BOM_ID id
	 *	@param trx p_trx
	 */
	public MBOM (Ctx ctx, int M_BOM_ID, Trx trx)
	{
		super (ctx, M_BOM_ID, trx);
		if (M_BOM_ID == 0)
		{
			//	setM_Product_ID (0);
			//	setName (null);
			setBOMType (BOMTYPE_CurrentActive);	// A
			setBOMUse (BOMUSE_Master);	// A
		}
	}	//	MBOM

	/**
	 * 	Load Constructor
	 *	@param ctx ctx
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MBOM (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MBOM

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true/false
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	BOM Type
		if (newRecord || is_ValueChanged("BOMType") || is_ValueChanged("BOMUse") || is_ValueChanged("IsActive"))
		{
			//	Only one Current Active per BOM Use
			if (getBOMType().equals(BOMTYPE_CurrentActive))
			{
				MBOM[] boms = getOfProduct(getCtx(), getM_Product_ID(), get_Trx(),
						"BOMType='A' AND BOMUse='" + getBOMUse() + "' AND IsActive='Y'");
				if (boms.length == 0	//	only one = this 
						|| (boms.length == 1 && boms[0].getM_BOM_ID() == getM_BOM_ID()))
					;
				else
				{
					log.saveError("Error", Msg.getMsg(getCtx(), 
					"BOMCurrentActive")); 
					return false;
				}
			}
			//	Only one MTO
			else if (getBOMType().equals(BOMTYPE_Make_To_Order))
			{
				MBOM[] boms = getOfProduct(getCtx(), getM_Product_ID(), get_Trx(), 
				"BOMType='O' AND IsActive='Y'");
				if (boms.length == 0	//	only one = this 
						|| (boms.length == 1 && boms[0].getM_BOM_ID() == getM_BOM_ID()))
					;
				else
				{
					log.saveError("Error", Msg.getMsg(getCtx(), 
					"BOMMakeToOrder"));
					return false;
				}
			}
		}	//	BOM Type

		return true;

	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	BOM Type or Use was changed
		if (newRecord || is_ValueChanged("BOMType") || is_ValueChanged("BOMUse") || is_ValueChanged("IsActive"))
		{
			//	Invalidate BOM
			MProduct product = new MProduct (getCtx(), getM_Product_ID(), get_Trx());
			if (get_Trx() != null)
				product.load(get_Trx());
			if (product.isVerified())
			{
				product.setIsVerified(false);
				product.save(get_Trx());
			}

			//TODO: Invalidate products where this BOM is used (in a multi-level scenario)


		}
		return success;
	}	//	afterSave

}	//	MBOM
