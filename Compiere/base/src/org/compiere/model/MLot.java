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
 *  Product Lot
 *
 *	@author Jorg Janke
 *	@version $Id: MLot.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MLot extends X_M_Lot
{
    /** Logger for class MLot */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MLot.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Logger					*/
	private static CLogger		s_log = CLogger.getCLogger(MLot.class);

	/**
	 * 	Get Lots for Product
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param trx transaction
	 *	@return Array of Lots for Product
	 */
	public static MLot[] getProductLots (Ctx ctx, int M_Product_ID, Trx trx)
	{
		String sql = "SELECT * FROM M_Lot WHERE M_Product_ID=?";
		ArrayList<MLot> list = new ArrayList<MLot>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_Product_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MLot (ctx, rs, trx));
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		MLot[] retValue = new MLot[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getProductLots

	/**
	 * 	Get Lot for Product
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param lot
	 *	@param trx transaction
	 *	@return Array of Lots for Product
	 */
	public static MLot getProductLot (Ctx ctx, int M_Product_ID, String lot, Trx trx)
	{
		String sql = "SELECT * FROM M_Lot WHERE M_Product_ID=? AND Name=?";
		MLot retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_Product_ID);
			pstmt.setString(2, lot);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				retValue = new MLot (ctx, rs, trx);
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retValue;
	}	//	getProductLot

	/**
	 * 	Get Lot Key Name Pairs for Product
	 *	@param M_Product_ID product
	 *	@param trx transaction
	 *	@return Array of Lot Key Name Pairs for Product
	 */
	public static KeyNamePair[] getProductLotPairs (int M_Product_ID, Trx trx)
	{
		String sql = "SELECT M_Lot_ID, Name FROM M_Lot WHERE M_Product_ID=?";
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_Product_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new KeyNamePair (rs.getInt(1), rs.getString(2)));
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		//
		KeyNamePair[] retValue = new KeyNamePair[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getProductLotPairs

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param M_Lot_ID ID
	 *	@param trx transaction
	 */
	public MLot (Ctx ctx, int M_Lot_ID, Trx trx)
	{
		super (ctx, M_Lot_ID, trx);
		/** if (M_Lot_ID == 0)
		{
			setM_Lot_ID (0);
			setM_Product_ID (0);
			setName (null);
		}
		**/
	}	//	MLot

	/**
	 * 	Load Constructor
	 * 	@param ctx context
	 * 	@param rs result set
	 *	@param trx transaction
	 */
	public MLot (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MLot

	/**
	 * 	Parent Constructor
	 * 	@param ctl lot control
	 * 	@param M_Product_ID product
	 * 	@param Name name
	 */
	public MLot (MLotCtl ctl, int M_Product_ID, String Name)
	{
		this (ctl.getCtx(), 0, ctl.get_Trx());
		setClientOrg(ctl);
		setM_LotCtl_ID(ctl.getM_LotCtl_ID());
		setM_Product_ID (M_Product_ID);
		setName (Name);
	}	//	MLot

	/**
	 *	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		return getName();
	}	//	toString

}	//	MLot
