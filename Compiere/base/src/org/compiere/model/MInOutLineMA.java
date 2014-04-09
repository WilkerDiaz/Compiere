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
 *	Shipment Material Allocation
 *	
 *  @author Jorg Janke
 *  @version $Id: MInOutLineMA.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MInOutLineMA extends X_M_InOutLineMA
{
    /** Logger for class MInOutLineMA */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInOutLineMA.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Material Allocations for Line
	 *	@param ctx context
	 *	@param M_InOutLine_ID line
	 *	@param trx p_trx
	 *	@return allocations
	 */
	public static MInOutLineMA[] get (Ctx ctx, int M_InOutLine_ID, Trx trx)
	{
		ArrayList<MInOutLineMA> list = new ArrayList<MInOutLineMA>();
		String sql = "SELECT * FROM M_InOutLineMA WHERE M_InOutLine_ID=? ORDER BY M_AttributeSetInstance_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_InOutLine_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MInOutLineMA (ctx, rs, trx));
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
		MInOutLineMA[] retValue = new MInOutLineMA[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Delete all Material Allocation for InOut
	 *	@param M_InOut_ID shipment
	 *	@param trx transaction
	 *	@return number of rows deleted or -1 for error
	 */
	public static int deleteInOutMA (int M_InOut_ID, Trx trx)
	{
		String sql = "DELETE FROM M_InOutLineMA ma WHERE EXISTS "
			+ "(SELECT * FROM M_InOutLine l WHERE l.M_InOutLine_ID=ma.M_InOutLine_ID"
			+ " AND M_InOut_ID=? )";
		return DB.executeUpdate(trx, sql,M_InOut_ID);
	}	//	deleteInOutMA
	
	/**
	 * 	Delete all Material Allocation for InOutLine
	 *	@param M_InOutLine_ID Shipment Line
	 *	@param trx transaction
	 *	@return number of rows deleted or -1 for error
	 */
	public static int deleteInOutLineMA (int M_InOutLine_ID, Trx trx)
	{
		String sql = "DELETE FROM M_InOutLineMA ma WHERE EXISTS "
			+ "(SELECT * FROM M_InOutLine l WHERE l.M_InOutLine_ID=ma.M_InOutLine_ID"
			+ " AND M_InOutLine_ID=? )";
		return DB.executeUpdate(trx, sql,M_InOutLine_ID);
	}	//	deleteInOutLineMA
		
	
	/**	Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MInOutLineMA.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOutLineMA_ID ignored
	 *	@param trx p_trx
	 */
	public MInOutLineMA (Ctx ctx, int M_InOutLineMA_ID, Trx trx)
	{
		super (ctx, M_InOutLineMA_ID, trx);
		if (M_InOutLineMA_ID != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MInOutLineMA

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MInOutLineMA (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MInOutLineMA
	
	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MInOutLineMA (Ctx ctx, int M_InOutLine_ID, int AD_Org_ID, 
							int M_AttributeSetInstance_ID, BigDecimal MovementQty, Trx trx)
	{
		super (ctx, 0, trx);
		setAD_Org_ID(AD_Org_ID);
		setM_InOutLine_ID(M_InOutLine_ID);
		//
		setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
		setMovementQty(MovementQty);
	}	//	MInOutLineMA

	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param MovementQty qty
	 */
	public MInOutLineMA (MInOutLine parent, int M_AttributeSetInstance_ID, BigDecimal MovementQty)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setM_InOutLine_ID(parent.getM_InOutLine_ID());
		//
		setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
		setMovementQty(MovementQty);
	}	//	MInOutLineMA

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param MovementQty qty
	 *  @param QtyAllocated qty
	 */
	public MInOutLineMA (MInOutLine parent, int M_AttributeSetInstance_ID, 
							BigDecimal MovementQty, BigDecimal QtyAllocated)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setM_InOutLine_ID(parent.getM_InOutLine_ID());
		//
		setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
		setMovementQty(MovementQty);
		setQtyAllocated(QtyAllocated);
	}	//	MInOutLineMA

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInOutLineMA[");
		sb.append("M_InOutLine_ID=").append(getM_InOutLine_ID())
			.append(",M_AttributeSetInstance_ID=").append(getM_AttributeSetInstance_ID())
			.append(", Qty=").append(getMovementQty())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MInOutLineMA
