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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Landed Cost Allocation Model
 *  @author Jorg Janke
 *  @version $Id: MLandedCostAllocation.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MLandedCostAllocation extends X_C_LandedCostAllocation
{	
    /** Logger for class MLandedCostAllocation */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MLandedCostAllocation.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Cost Allocations for invoice Line
	 *	@param ctx context
	 *	@param C_InvoiceLine_ID invoice line
	 *	@param trx p_trx
	 *	@return landed cost alloc
	 */
	public static MLandedCostAllocation[] getOfInvoiceLine (Ctx ctx, 
		int C_InvoiceLine_ID, Trx trx)
	{
		ArrayList<MLandedCostAllocation> list = new ArrayList<MLandedCostAllocation>();
		String sql = "SELECT * FROM C_LandedCostAllocation WHERE C_InvoiceLine_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, C_InvoiceLine_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MLandedCostAllocation (ctx, rs, trx));
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
		MLandedCostAllocation[] retValue = new MLandedCostAllocation[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfInvliceLine
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MLandedCostAllocation.class);
	
	
	/***************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_LandedCostAllocation_ID id
	 *	@param trx p_trx
	 */
	public MLandedCostAllocation (Ctx ctx, int C_LandedCostAllocation_ID, Trx trx)
	{
		super (ctx, C_LandedCostAllocation_ID, trx);
		if (C_LandedCostAllocation_ID == 0)
		{
		//	setM_CostElement_ID(0);
			setAmt (Env.ZERO);
			setQty (Env.ZERO);
			setBase (Env.ZERO);
		}
	}	//	MLandedCostAllocation

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result name
	 *	@param trx p_trx
	 */
	public MLandedCostAllocation (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MLandedCostAllocation

	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param M_CostElement_ID cost element
	 */
	public MLandedCostAllocation (MInvoiceLine parent, int M_CostElement_ID)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setC_InvoiceLine_ID(parent.getC_InvoiceLine_ID());
		setM_CostElement_ID(M_CostElement_ID);
	}	//	MLandedCostAllocation
	
	/**
	 * 	Set Amt
	 *	@param Amt amount
	 *	@param precision precision
	 */
	public void setAmt (double Amt, int precision)
	{
		BigDecimal bd = new BigDecimal(Amt);
		if (bd.scale() > precision)
			bd = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
		super.setAmt(bd);
	}	//	setAmt

	/**
	 * 	Set Allocation Qty (e.g. free products)
	 *	@param Qty
	 */
	@Override
	public void setQty (BigDecimal Qty)
	{
		super.setQty (Qty);
	}	//	setQty
	
	
}	//	MLandedCostAllocation
