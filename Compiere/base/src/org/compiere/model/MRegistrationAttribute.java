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
 *	Asset Registration Attribute
 *	
 *  @author Jorg Janke
 *  @version $Id: MRegistrationAttribute.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MRegistrationAttribute extends X_A_RegistrationAttribute
{
    /** Logger for class MRegistrationAttribute */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRegistrationAttribute.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get All Asset Registration Attributes (not cached).
	 * 	Refreshes Cache for direct addess
	 *	@param ctx context
	 *	@return array of Registration Attributes
	 */
	public static MRegistrationAttribute[] getAll (Ctx ctx)
	{
		//	Store/Refresh Cache and add to List
		ArrayList<MRegistrationAttribute> list = new ArrayList<MRegistrationAttribute>();
		String sql = "SELECT * FROM A_RegistrationAttribute "
			+ "WHERE AD_Client_ID=? "
			+ "ORDER BY SeqNo";
		int AD_Client_ID = ctx.getAD_Client_ID();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MRegistrationAttribute value = new MRegistrationAttribute(ctx, rs, null);
				Integer key = Integer.valueOf(value.getA_RegistrationAttribute_ID());
				s_cache.put(key, value);
				list.add(value);
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		MRegistrationAttribute[] retValue = new MRegistrationAttribute[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getAll

	/**
	 * 	Get Registration Attribute (cached)
	 *	@param ctx context
	 *	@param A_RegistrationAttribute_ID id
	 *	@return Registration Attribute
	 */
	public static MRegistrationAttribute get (Ctx ctx, int A_RegistrationAttribute_ID, Trx trx)
	{
		Integer key = Integer.valueOf(A_RegistrationAttribute_ID);
		MRegistrationAttribute retValue = s_cache.get(ctx, key);
		if (retValue == null)
		{
			retValue = new MRegistrationAttribute (ctx, A_RegistrationAttribute_ID, trx);
			s_cache.put(key, retValue);
		}
		return retValue;
	}	//	getAll

	/** Static Logger					*/
	private static final CLogger s_log = CLogger.getCLogger(MRegistrationAttribute.class);
	/**	Cache						*/
	private static final CCache<Integer,MRegistrationAttribute> s_cache 
		= new CCache<Integer,MRegistrationAttribute>("A_RegistrationAttribute", 20);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param A_RegistrationAttribute_ID id
	 */
	public MRegistrationAttribute (Ctx ctx, int A_RegistrationAttribute_ID, Trx trx)
	{
		super(ctx, A_RegistrationAttribute_ID, trx);
	}	//	MRegistrationAttribute

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRegistrationAttribute (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MRegistrationAttribute

}	//	MRegistrationAttribute
