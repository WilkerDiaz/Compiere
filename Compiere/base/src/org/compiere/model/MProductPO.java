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
 *	Product PO Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MProductPO.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MProductPO extends X_M_Product_PO
{
    /** Logger for class MProductPO */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProductPO.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get current PO of Product
	 * 	@param ctx context
	 *	@param M_Product_ID product
	 *	@param trx transaction
	 *	@return PO - current vendor first
	 */
	public static MProductPO[] getOfProduct (Ctx ctx, int M_Product_ID, Trx trx)
	{
		ArrayList<MProductPO> list = new ArrayList<MProductPO>();
		String sql = "SELECT * FROM M_Product_PO "
			+ "WHERE M_Product_ID=? AND IsActive='Y' "
			+ "ORDER BY IsCurrentVendor DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_Product_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				list.add(new MProductPO (ctx, rs, trx));
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
		MProductPO[] retValue = new MProductPO[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfProduct


	
	/**
	 * Get PO for the product Vendor
	 * @param ctx context 
	 * @param C_BPartner_ID partner
	 * @param M_Product_ID product
	 * @param trx trx
	 * @return
	 */
	public static MProductPO getOfVendorProduct (Ctx ctx, int C_BPartner_ID, int M_Product_ID,Trx trx)
	{
		MProductPO productPO = null;
		String sql = "SELECT * FROM M_Product_PO "
			+ "WHERE C_BPartner_ID=? AND M_Product_ID = ? ";
			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, C_BPartner_ID);
			pstmt.setInt(2,M_Product_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				productPO = (new MProductPO (ctx, rs, trx));
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
		
		return productPO;
	}	//	getOfVendorProduct
	
	/** Static Logger					*/
	private static CLogger s_log = CLogger.getCLogger(MProductPO.class);

	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trx transaction
	 */
	public MProductPO (Ctx ctx, int ignored, Trx trx)
	{
		super(ctx, 0, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		else
		{
			//	setM_Product_ID (0);	// @M_Product_ID@
			//	setC_BPartner_ID (0);	// 0
			//	setVendorProductNo (null);	// @Value@
			setIsCurrentVendor (true);	// Y
		}
	}	//	MProduct_PO


	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MProductPO(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProductPO

}	//	MProductPO
