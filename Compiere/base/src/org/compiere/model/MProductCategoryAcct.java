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
 * 	Product Category Account Model
 *  @author Jorg Janke
 *  @version $Id: MProductCategoryAcct.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MProductCategoryAcct extends X_M_Product_Category_Acct
{
    /** Logger for class MProductCategoryAcct */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProductCategoryAcct.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Category Acct
	 *	@param ctx context
	 *	@param M_Product_Category_ID category
	 *	@param C_AcctSchema_ID acct schema
	 *	@param trx p_trx
	 *	@return category acct
	 */
	public static MProductCategoryAcct get (Ctx ctx, 
		int M_Product_Category_ID, int C_AcctSchema_ID, Trx trx)
	{
		MProductCategoryAcct retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM  M_Product_Category_Acct "
			+ "WHERE M_Product_Category_ID=? AND C_AcctSchema_ID=?";
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_Product_Category_ID);
			pstmt.setInt (2, C_AcctSchema_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MProductCategoryAcct (ctx, rs, trx);
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retValue;
	}	//	get
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MProductCategoryAcct.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param ignored ignored 
	 *	@param trx
	 */
	public MProductCategoryAcct (Ctx ctx, int ignored, Trx trx)
	{
		super (ctx, ignored, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MProductCategoryAcct

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MProductCategoryAcct (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MProductCategoryAcct

	/**
	 * 	Check Costing Setup
	 */
	public void checkCosting()
	{
		//	Create Cost Elements
		if (getCostingMethod() != null && getCostingMethod().length() > 0)
			MCostElement.getMaterialCostElement(this, getCostingMethod());
	}	//	checkCosting

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		checkCosting();
		return success;
	}	//	afterSave
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MProductCategoryAcct[");
		sb.append (get_ID())
			.append (",M_Product_Category_ID=").append (getM_Product_Category_ID())
			.append (",C_AcctSchema_ID=").append(getC_AcctSchema_ID())
			.append (",CostingLevel=").append(getCostingLevel())
			.append (",CostingMethod=").append(getCostingMethod())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MProductCategoryAcct
