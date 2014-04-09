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

import org.compiere.util.*;

/**
 * 	Cost Type Model
 *  @author Jorg Janke
 *  @version $Id: MCostType.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MCostType extends X_M_CostType
{
    /** Logger for class MCostType */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCostType.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_CostType_ID id
	 *	@param trx p_trx
	 */
	public MCostType (Ctx ctx, int M_CostType_ID, Trx trx)
	{
		super (ctx, M_CostType_ID, trx);
	}	//	MCostType

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MCostType (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MCostType
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MCostType[");
		sb.append (get_ID()).append ("-").append (getName ()).append ("]");
		return sb.toString ();
	}	//	toString
	
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

	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	@Override
	protected boolean beforeDelete ()
	{
		MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(getCtx(), getAD_Client_ID());
		for (MAcctSchema element : ass) {
			if (element.getM_CostType_ID() == getM_CostType_ID())
			{
				log.saveError("CannotDelete", Msg.getElement(getCtx(), "C_AcctSchema_ID")
					+ " - " + element.getName());
				return false;
			}
		}
		return true;
	}	//	beforeDelete
	
}	//	MCostType
