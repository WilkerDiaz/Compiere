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
 *	Product Costing Model (old).
 *	deprecated old costing
 *
 *  @author Jorg Janke
 *  @version $Id: MProductCosting.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MProductCosting extends X_M_Product_Costing
{
    /** Logger for class MProductCosting */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProductCosting.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Costing Of Product
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param trx p_trx
	 *	@return array of costs
	 */
	public static MProductCosting[] getOfProduct (Ctx ctx, int M_Product_ID, Trx trx)
	{
		String sql = "SELECT * FROM M_Product_Costing WHERE M_Product_ID=?";
		ArrayList<MProductCosting> list = new ArrayList<MProductCosting>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_Product_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MProductCosting (ctx, rs, trx));
			}
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
		MProductCosting[] retValue = new MProductCosting[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfProduct

	/**
	 * 	Get Costing
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param C_AcctSchema_ID as
	 *	@param trx p_trx
	 *	@return array of costs
	 */
	public static MProductCosting get (Ctx ctx, int M_Product_ID, 
		int C_AcctSchema_ID, Trx trx)
	{
		MProductCosting retValue = null;
		String sql = "SELECT * FROM M_Product_Costing WHERE M_Product_ID=? AND C_AcctSchema_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_Product_ID);
			pstmt.setInt (2, C_AcctSchema_ID);
			rs = pstmt.executeQuery ();
			if (rs.next())
				retValue = new MProductCosting (ctx, rs, trx);
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
	}	//	get

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MProductCosting.class);

	
	/**************************************************************************
	 * 	Standard Constructor (odl)
	 *	@param ctx context
	 *	@param ignored (multi key)
	 *	@param trx transaction
	 */
	public MProductCosting (Ctx ctx, int ignored, Trx trx)
	{
		super (ctx, ignored, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		else
		{
		//	setM_Product_ID (0);
		//	setC_AcctSchema_ID (0);
			//
			setCostAverage (Env.ZERO);
			setCostAverageCumAmt (Env.ZERO);
			setCostAverageCumQty (Env.ZERO);
			setCostStandard (Env.ZERO);
			setCostStandardCumAmt (Env.ZERO);
			setCostStandardCumQty (Env.ZERO);
			setCostStandardPOAmt (Env.ZERO);
			setCostStandardPOQty (Env.ZERO);
			setCurrentCostPrice (Env.ZERO);
			setFutureCostPrice (Env.ZERO);
			setPriceLastInv (Env.ZERO);
			setPriceLastPO (Env.ZERO);
			setTotalInvAmt (Env.ZERO);
			setTotalInvQty (Env.ZERO);
		}
	}	//	MProductCosting

	/**
	 * 	Parent Constructor (old)
	 *	@param product parent
	 *	@param C_AcctSchema_ID accounting schema
	 */
	public MProductCosting (MProduct product, int C_AcctSchema_ID)
	{
		super (product.getCtx(), 0, product.get_Trx());
		setClientOrg(product);
		setM_Product_ID (product.getM_Product_ID());
		setC_AcctSchema_ID (C_AcctSchema_ID);
	}	//	MProductCosting
	
	
	/**
	 * 	Load Constructor (old)
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MProductCosting (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProductCosting
	
}	//	MProductCosting

