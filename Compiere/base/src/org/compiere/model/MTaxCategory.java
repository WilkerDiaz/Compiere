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
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Tax Category Model
 *
 *  @author Jorg Janke
 *  @version $Id: MTaxCategory.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MTaxCategory extends X_C_TaxCategory
{
    /** Logger for class MTaxCategory */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTaxCategory.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Default Tax Category
	 *	@param ctx context
	 *	@return tax oldest (default) category
	 */
	public static MTaxCategory getDefault (Ctx ctx)
	{
		String sql = "SELECT * FROM C_TaxCategory "
			+ "WHERE AD_Client_ID=? "
			+ "ORDER BY ASCII(IsDefault) DESC, C_TaxCategory_ID";
		MTaxCategory retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, ctx.getAD_Client_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MTaxCategory(ctx, rs, null);
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

	/**	Logger						*/
	static CLogger s_log = CLogger.getCLogger(MTaxCategory.class);


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_TaxCategory_ID id
	 *	@param trx p_trx
	 */
	public MTaxCategory (Ctx ctx, int C_TaxCategory_ID, Trx trx)
	{
		super (ctx, C_TaxCategory_ID, trx);
		if (C_TaxCategory_ID == 0)
		{
		//	setName (null);
			setIsDefault (false);
		}
	}	//	MTaxCategory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs resukt set
	 *	@param trx p_trx
	 */
	public MTaxCategory (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MTaxCategory

}	//	MTaxCategory
