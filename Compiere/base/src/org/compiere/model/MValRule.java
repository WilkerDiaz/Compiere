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
import java.util.logging.Level;

import org.compiere.util.*;

/**
 * @author Jorg Janke
 */
public class MValRule extends X_AD_Val_Rule
{
    /** Logger for class MValRule */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MValRule.class);
	/** */
	private static final long serialVersionUID = 1L;
	
	
	/**
     * 	Get MValRule from Cache
     *	@param ctx context
     *	@param AD_Val_Rule_ID id
     *	@return MValRule
     */
    public static MValRule get(Ctx ctx, int AD_Val_Rule_ID)
    {
	    Integer key = Integer.valueOf(AD_Val_Rule_ID);
	    MValRule retValue = s_cache.get(ctx, key);
	    if (retValue != null)
		    return retValue;
	    retValue = new MValRule(ctx, AD_Val_Rule_ID, null);
	    if (retValue.get_ID() != 0)
		    s_cache.put(key, retValue);
	    return retValue;
    }	//	get

    /**	Cache						*/
    private static final CCache<Integer, MValRule> s_cache 
    	= new CCache<Integer, MValRule>("AD_Val_Rule", 20);

	/**
	 * @param ctx
	 * @param AD_Val_Rule_ID
	 * @param trx
	 */
	public MValRule(Ctx ctx, int AD_Val_Rule_ID, Trx trx)
	{
		super(ctx, AD_Val_Rule_ID, trx);
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trx
	 */
	public MValRule(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}

	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MValRule[")
			.append(get_ID()).append("-").append(getName()).append("]");
		return sb.toString();
	}	//	toString

	public static ArrayList<KeyNamePair> getAllValRules() {
		String sql = "SELECT AD_Val_Rule_ID, Name FROM AD_Val_Rule WHERE IsActive='Y' ORDER BY Name";
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			while( rs.next() )
			{
				list.add( new KeyNamePair( rs.getInt( 1 ), rs.getString( 2 ) ) );
			}
		}
		catch(SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}		
		return list;
	}

}
