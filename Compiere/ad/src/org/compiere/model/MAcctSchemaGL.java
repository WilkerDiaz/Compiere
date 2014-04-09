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
 *	Accounting Schema GL info
 *	
 *  @author Jorg Janke
 *  @version $Id: MAcctSchemaGL.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public class MAcctSchemaGL extends X_C_AcctSchema_GL
{
    /** Logger for class MAcctSchemaGL */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAcctSchemaGL.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Accounting Schema GL Info
	 *	@param ctx context
	 *	@param C_AcctSchema_ID id
	 *	@return defaults
	 */
	public static MAcctSchemaGL get (Ctx ctx, int C_AcctSchema_ID)
	{
		MAcctSchemaGL retValue = null;
		String sql = "SELECT * FROM C_AcctSchema_GL WHERE C_AcctSchema_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_AcctSchema_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = new MAcctSchemaGL (ctx, rs, null);
			}
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get
	
	/**	Logger							*/
	protected static final CLogger			s_log = CLogger.getCLogger(MAcctSchemaGL.class);
	
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param C_AcctSchema_ID AcctSchema 
	 *	@param trx transaction
	 */
	public MAcctSchemaGL (Ctx ctx, int C_AcctSchema_ID, Trx trx)
	{
		super(ctx, C_AcctSchema_ID, trx);
		if (C_AcctSchema_ID == 0)
		{
			setUseCurrencyBalancing (false);
			setUseSuspenseBalancing (false);
			setUseSuspenseError (false);
		}
	}	//	MAcctSchemaGL

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAcctSchemaGL (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAcctSchemaGL

	/**
	 * 	Get Acct Info list 
	 *	@return list
	 */
	public ArrayList<KeyNamePair> getAcctInfo()
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		for (int i = 0; i < get_ColumnCount(); i++)
		{
			String columnName = get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				int id = ((Integer)get_Value(i));
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getAcctInfo
	
	/**
	 * 	Set Value (don't use)
	 *	@param columnName column name
	 *	@param value value
	 *	@return true if set
	 */
	public boolean setValue (String columnName, Integer value)
	{
		return super.set_Value (columnName, value);
	}	//	setValue
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		return true;
	}	//	beforeSave

}	//	MAcctSchemaGL
