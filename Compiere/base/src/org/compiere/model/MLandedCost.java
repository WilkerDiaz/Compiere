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
 * 	Landed Cost Model
 *  @author Jorg Janke
 *  @version $Id: MLandedCost.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MLandedCost extends X_C_LandedCost
{
    /** Logger for class MLandedCost */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MLandedCost.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *	Get Costs of Invoice Line
	 * 	@param il invoice line
	 *	@return array of landed cost lines
	 */
	public static MLandedCost[] getLandedCosts (MInvoiceLine il)
	{
		ArrayList<MLandedCost> list = new ArrayList<MLandedCost> ();
		String sql = "SELECT * FROM C_LandedCost WHERE C_InvoiceLine_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, il.get_Trx());
			pstmt.setInt (1, il.getC_InvoiceLine_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MLandedCost (il.getCtx(), rs, il.get_Trx()));
			}
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
		//
		MLandedCost[] retValue = new MLandedCost[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	// getLandedCosts

	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MLandedCost.class);

	
	/***************************************************************************
	 * Standard Constructor
	 * 
	 * @param ctx context
	 * @param C_LandedCost_ID id
	 * @param trx p_trx
	 */
	public MLandedCost (Ctx ctx, int C_LandedCost_ID, Trx trx)
	{
		super (ctx, C_LandedCost_ID, trx);
		if (C_LandedCost_ID == 0)
		{
		//	setC_InvoiceLine_ID (0);
		//	setM_CostElement_ID (0);
			setLandedCostDistribution (LANDEDCOSTDISTRIBUTION_Quantity);	// Q
		}
	}	//	MLandedCost

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MLandedCost (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MLandedCost
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if ok
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	One Reference
		if (getM_Product_ID() == 0 
			&& getM_InOut_ID() == 0 
			&& getM_InOutLine_ID() == 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), 
				"@NotFound@ @M_Product_ID@ | @M_InOut_ID@ | @M_InOutLine_ID@"));
			return false;
		}
		//	No Product if Line entered
		if (getM_InOutLine_ID() != 0 && getM_Product_ID() != 0)
			setM_Product_ID(0);
				
		return true;
	}	//	beforeSave
	
	/**
	 * 	Allocate Costs.
	 * 	Done at Invoice Line Level
	 * 	@return error message or ""
	 */
	public String allocateCosts()
	{
		MInvoiceLine il = new MInvoiceLine (getCtx(), getC_InvoiceLine_ID(), get_Trx());
		return il.allocateLandedCosts();
	}	//	allocateCosts
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MLandedCost[");
		sb.append (get_ID ())
			.append (",CostDistribution=").append (getLandedCostDistribution())
			.append(",M_CostElement_ID=").append(getM_CostElement_ID());
		if (getM_InOut_ID() != 0)
			sb.append (",M_InOut_ID=").append (getM_InOut_ID());
		if (getM_InOutLine_ID() != 0)
			sb.append (",M_InOutLine_ID=").append (getM_InOutLine_ID());
		if (getM_Product_ID() != 0)
			sb.append (",M_Product_ID=").append (getM_Product_ID());
		sb.append ("]");
		return sb.toString ();
	} //	toString
	
}	//	MLandedCost
