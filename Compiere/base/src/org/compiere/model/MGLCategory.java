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

import org.compiere.common.CompiereStateException;
import org.compiere.util.*;

/**
 *	GL Category
 *	
 *  @author Jorg Janke
 *  @version $Id: MGLCategory.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MGLCategory extends X_GL_Category
{
    /** Logger for class MGLCategory */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MGLCategory.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MGLCategory from Cache
	 *	@param ctx context
	 *	@param GL_Category_ID id
	 *	@return MGLCategory
	 */
	public static MGLCategory get (Ctx ctx, int GL_Category_ID)
	{
		Integer key = Integer.valueOf (GL_Category_ID);
		MGLCategory retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MGLCategory (ctx, GL_Category_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Default Category
	 *	@param ctx context
	 *	@param CategoryType optional CategoryType (ignored, if not exists)
	 *	@return GL Category or null
	 */
	public static MGLCategory getDefault (Ctx ctx, String CategoryType)
	{
		MGLCategory retValue = null;
		String sql = "SELECT * FROM GL_Category "
			+ "WHERE AD_Client_ID=? AND IsDefault='Y'";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, ctx.getAD_Client_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MGLCategory temp = new MGLCategory (ctx, rs, null);
				if (CategoryType != null && CategoryType.equals(temp.getCategoryType()))
				{
					retValue = temp;
					break;
				}
				if (retValue == null)
					retValue = temp;
			}
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
		return retValue;
	}	//	getDefault

	/**
	 * 	Get Default System Category
	 *	@param ctx context
	 *	@return GL Category
	 */
	public static MGLCategory getDefaultSystem (Ctx ctx)
	{
		MGLCategory retValue = getDefault(ctx, CATEGORYTYPE_SystemGenerated);
		if (retValue == null 
			|| !retValue.getCategoryType().equals(CATEGORYTYPE_SystemGenerated))
		{
			retValue = new MGLCategory(ctx, 0, null);
			retValue.setName("Default System");
			retValue.setCategoryType(CATEGORYTYPE_SystemGenerated);
			retValue.setIsDefault(true);
			if (!retValue.save())
				throw new CompiereStateException("Could not save default system GL Category");
		}
		return retValue;
	}	//	getDefaultSystem

	
	/**	Logger						*/
	private static final CLogger s_log = CLogger.getCLogger (MGLCategory.class);
	/**	Cache						*/
	private static final CCache<Integer, MGLCategory> s_cache 
		= new CCache<Integer, MGLCategory> ("GL_Category", 5);
	

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param GL_Category_ID id
	 *	@param trx transaction
	 */
	public MGLCategory (Ctx ctx, int GL_Category_ID, Trx trx)
	{
		super (ctx, GL_Category_ID, trx);
		if (GL_Category_ID == 0)
		{
		//	setName (null);
			setCategoryType (CATEGORYTYPE_Manual);
			setIsDefault (false);
		}
	}	//	MGLCategory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MGLCategory (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MGLCategory
	
}	//	MGLCategory
