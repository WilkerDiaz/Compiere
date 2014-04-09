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
 *	Movement Material Allocation
 *	
 *  @author Jorg Janke
 *  @version $Id: MMovementLineMA.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MMovementLineMA extends X_M_MovementLineMA
{
    /** Logger for class MMovementLineMA */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MMovementLineMA.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Material Allocations for Line
	 *	@param ctx context
	 *	@param M_MovementLine_ID line
	 *	@param trx p_trx
	 *	@return allocations
	 */
	public static MMovementLineMA[] get (Ctx ctx, int M_MovementLine_ID, Trx trx)
	{
		ArrayList<MMovementLineMA> list = new ArrayList<MMovementLineMA>();
		String sql = "SELECT * FROM M_MovementLineMA WHERE M_MovementLine_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_MovementLine_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MMovementLineMA (ctx, rs, trx));
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
		MMovementLineMA[] retValue = new MMovementLineMA[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Delete all Material Allocation for Movement
	 *	@param M_Movement_ID movement
	 *	@param trx transaction
	 *	@return number of rows deleted or -1 for error
	 */
	public static int deleteMovementMA (int M_Movement_ID, Trx trx)
	{
		String sql = "DELETE FROM M_MovementLineMA ma WHERE EXISTS "
			+ "(SELECT * FROM M_MovementLine l WHERE l.M_MovementLine_ID=ma.M_MovementLine_ID"
			+ " AND M_Movement_ID= ? )";
		return DB.executeUpdate(trx, sql,M_Movement_ID);
	}	//	deleteInOutMA
	
	/**
	 * 	Delete all Material Allocation for Movement Line
	 *	@param M_MovementLine_ID movement line
	 *	@param trx transaction
	 *	@return number of rows deleted or -1 for error
	 */
	public static int deleteMovementLineMA (int M_MovementLine_ID, Trx trx)
	{
		String sql = "DELETE FROM M_MovementLineMA ma WHERE EXISTS "
			+ "(SELECT * FROM M_MovementLine l WHERE l.M_MovementLine_ID=ma.M_MovementLine_ID"
			+ " AND M_MovementLine_ID=? )";
		return DB.executeUpdate(trx, sql,M_MovementLine_ID);
	}	//	deleteInOutMA
	
	
	/**	Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MMovementLineMA.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_MovementLineMA_ID ignored
	 *	@param trx p_trx
	 */
	public MMovementLineMA (Ctx ctx, int M_MovementLineMA_ID,
		Trx trx)
	{
		super (ctx, M_MovementLineMA_ID, trx);
		if (M_MovementLineMA_ID != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MMovementLineMA

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result ser
	 *	@param trx p_trx
	 */
	public MMovementLineMA (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MMovementLineMA
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param MovementQty qty
	 */
	public MMovementLineMA (MMovementLine parent, int M_AttributeSetInstance_ID, BigDecimal MovementQty)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setM_MovementLine_ID(parent.getM_MovementLine_ID());
		//
		setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
		setMovementQty(MovementQty);
	}	//	MMovementLineMA
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MMovementLineMA[");
		sb.append("M_MovementLine_ID=").append(getM_MovementLine_ID())
			.append(",M_AttributeSetInstance_ID=").append(getM_AttributeSetInstance_ID())
			.append(", Qty=").append(getMovementQty())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MMovementLineMA
