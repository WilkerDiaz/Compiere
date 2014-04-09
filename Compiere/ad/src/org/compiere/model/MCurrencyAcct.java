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
 *	Currency Account Model 
 *	
 *  @author Jorg Janke
 *  @version $Id: MCurrencyAcct.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public class MCurrencyAcct extends X_C_Currency_Acct
{
    /** Logger for class MCurrencyAcct */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCurrencyAcct.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Static Logger					*/
	private static CLogger s_log = CLogger.getCLogger(MCurrencyAcct.class);
	
	/**
	 * 	Get Currency Account for Currency
	 *	@param as accounting schema default
	 *	@param C_Currency_ID currency
	 *	@return Currency Account or null
	 */
	public static MCurrencyAcct get (MAcctSchemaDefault as, int C_Currency_ID)
	{
		MCurrencyAcct retValue = null;
		String sql = "SELECT * FROM C_Currency_Acct "
			+ "WHERE C_AcctSchema_ID=? AND C_Currency_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, as.getC_AcctSchema_ID());
			pstmt.setInt(2, C_Currency_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = new MCurrencyAcct (as.getCtx(), rs, null);
			}
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, "get", e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get
	
	
	/**************************************************************************
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MCurrencyAcct(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MCurrencyAcct

}	//	MCurrencyAcct
