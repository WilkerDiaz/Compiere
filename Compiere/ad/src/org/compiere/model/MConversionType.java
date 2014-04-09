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

import org.compiere.util.*;

/**
 *	Currency Conversion Type Model
 *
 *  @author Jorg Janke
 *  @version $Id: MConversionType.java 8780 2010-05-19 23:08:57Z nnayak $
 */
public class MConversionType extends X_C_ConversionType
{
    /** Logger for class MConversionType */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MConversionType.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Default Conversion Rate for Client/Org
	 *	@param AD_Client_ID client
	 *	@return C_ConversionType_ID or 0 if not found
	 */
	public static int getDefault (int AD_Client_ID)
	{
		//	Try Cache
		Integer key = Integer.valueOf (AD_Client_ID);
		Integer ii = s_cache.get(null, key);
		if (ii != null)
			return ii.intValue();

		//	Get from DB
		int C_ConversionType_ID = 0;
		String sql = "SELECT C_ConversionType_ID "
			+ "FROM C_ConversionType "
			+ "WHERE IsActive='Y'"
			+ " AND AD_Client_ID IN (0,?) "		//	#1
			+ "ORDER BY ASCII(IsDefault) DESC, AD_Client_ID DESC";
		C_ConversionType_ID = QueryUtil.getSQLValue(null, sql, AD_Client_ID);
		//	Return
		s_cache.put(key, Integer.valueOf(C_ConversionType_ID));
		return C_ConversionType_ID;
	}	//	getDefault

	/**	Cache Client-ID					*/
	private static final CCache<Integer,Integer> s_cache = new CCache<Integer,Integer>("C_ConversionType", 4);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_ConversionType_ID id
	 *	@param trx transaction
	 */
	public MConversionType(Ctx ctx, int C_ConversionType_ID, Trx trx)
	{
		super(ctx, C_ConversionType_ID, trx);
	}	//	MConversionType

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MConversionType(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MConversionType

}	//	MConversionType
