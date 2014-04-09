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
 *	Private Access
 *	
 *  @author Jorg Janke
 *  @version $Id: MPrivateAccess.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MPrivateAccess extends X_AD_Private_Access
{
    /** Logger for class MPrivateAccess */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPrivateAccess.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static CCache<String, String> s_lockedRecords = new CCache<String, String>("AD_Private_Access");
	/**
	 * 	Load Pricate Access
	 *	@param ctx context 
	 *	@param AD_User_ID user
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@return access or null if not found
	 */
	public static MPrivateAccess get (Ctx ctx, int AD_User_ID, int AD_Table_ID, int Record_ID)
	{
		MPrivateAccess retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM AD_Private_Access WHERE AD_User_ID=? AND AD_Table_ID=? AND Record_ID=?";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_User_ID);
			pstmt.setInt(2, AD_Table_ID);
			pstmt.setInt(3, Record_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MPrivateAccess (ctx, rs, null); 
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, "MPrivateAccess", e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get

	/**
	 * 	Get Where Clause of Locked Records for Table
	 *	@param AD_Table_ID table
	 *	@param AD_User_ID user requesting info
	 *	@return "<>1" or " NOT IN (1,2)" or ""
	 */
	public static String getLockedRecordWhere (int AD_Table_ID, int AD_User_ID)
	{
		String key = AD_Table_ID+"."+AD_User_ID;
		String ret = s_lockedRecords.get(null, key);
		if(ret != null) {
			return ret;
		}
		ArrayList<Integer> list = new ArrayList<Integer>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT Record_ID FROM AD_Private_Access WHERE AD_Table_ID=? AND AD_User_ID<>? AND IsActive='Y'";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Table_ID);
			pstmt.setInt(2, AD_User_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(Integer.valueOf(rs.getInt(1))); 
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (list.size() == 0) {

			ret = "";
		}
		else if (list.size() == 1)
			ret =  "<>" + list.get(0);
		else {
			StringBuffer sb = new StringBuffer(" NOT IN(");
			for (int i = 0; i < list.size(); i++)
			{
				if (i > 0)
					sb.append(",");
				sb.append(list.get(i));
			}
			sb.append(")");
			ret = sb.toString();
		}
		s_lockedRecords.put(key, ret);
		return ret;
	}	//	get


	/**	Logger					*/
	private static CLogger		s_log = CLogger.getCLogger(MPrivateAccess.class);

	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trx transaction
	 */
	public MPrivateAccess (Ctx ctx, int ignored, Trx trx)
	{
		super(ctx, 0, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MPrivateAccess

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPrivateAccess(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPrivateAccess

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param AD_User_ID user
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 */
	public MPrivateAccess (Ctx ctx, int AD_User_ID, int AD_Table_ID, int Record_ID)
	{
		super(ctx, 0, null);
		setAD_User_ID (AD_User_ID);
		setAD_Table_ID (AD_Table_ID);
		setRecord_ID (Record_ID);
	}	//	MPrivateAccess
}	//	MPrivateAccess
