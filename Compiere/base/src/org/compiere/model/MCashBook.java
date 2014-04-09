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
 *	Cash Book Model
 *
 *  @author Jorg Janke
 *  @version $Id: MCashBook.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MCashBook extends X_C_CashBook
{
    /** Logger for class MCashBook */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCashBook.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get MCashBook from Cache
	 *	@param ctx context
	 *	@param C_CashBook_ID id
	 *	@return MCashBook
	 */
	public static MCashBook get (Ctx ctx, int C_CashBook_ID)
	{
		Integer key = Integer.valueOf (C_CashBook_ID);
		MCashBook retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MCashBook (ctx, C_CashBook_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get CashBook for Org and Currency
	 *	@param ctx context
	 *	@param AD_Org_ID org
	 *	@param C_Currency_ID currency
	 *	@return cash book or null
	 */
	public static MCashBook get (Ctx ctx, int AD_Org_ID, int C_Currency_ID)
	{
		//	Try from cache
		Iterator<MCashBook> it = s_cache.values().iterator();
		while (it.hasNext())
		{
			MCashBook cb = it.next();
			if ((cb.getAD_Org_ID() == AD_Org_ID) && (cb.getC_Currency_ID() == C_Currency_ID))
				return cb;
		}

		//	Get from DB
		MCashBook retValue = null;
		String sql = "SELECT * FROM C_CashBook "
			+ "WHERE AD_Org_ID=? AND C_Currency_ID=? AND IsActive = 'Y' "
			+ "ORDER BY ASCII(IsDefault) DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Org_ID);
			pstmt.setInt (2, C_Currency_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MCashBook (ctx, rs, null);
				Integer key = Integer.valueOf (retValue.getC_CashBook_ID());
				s_cache.put (key, retValue);
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "get", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retValue;
	}	//	get


	/**	Cache						*/
	private static final CCache<Integer,MCashBook> s_cache
		= new CCache<Integer,MCashBook>("", 20);
	/**	Static Logger	*/
	private static final CLogger	s_log	= CLogger.getCLogger (MCashBook.class);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_CashBook_ID id
	 *	@param trx transaction
	 */
	public MCashBook (Ctx ctx, int C_CashBook_ID, Trx trx)
	{
		super (ctx, C_CashBook_ID, trx);
		if (C_CashBook_ID == 0)
			setIsDefault(false);
	}	//	MCashBook

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MCashBook (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MCashBook

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
			success = insert_Accounting("C_CashBook_Acct", "C_AcctSchema_Default", null);

		return success;
	}	//	afterSave

	/**
	 * 	Before Delete
	 *	@return true
	 */
	@Override
	protected boolean beforeDelete ()
	{
		return delete_Accounting("C_Cashbook_Acct");
	}	//	beforeDelete

	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MCashBook[")
	    	.append(get_ID())
	        .append("-").append(getName());
	    sb.append("]");
	    return sb.toString();
    }	//	toString

}	//	MCashBook
