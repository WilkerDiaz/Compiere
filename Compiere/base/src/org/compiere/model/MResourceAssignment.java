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

import org.compiere.util.*;


/**
 *	Resource Assignment Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MResourceAssignment.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MResourceAssignment extends X_S_ResourceAssignment
{
    /** Logger for class MResourceAssignment */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MResourceAssignment.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Stnadard Constructor
	 *	@param ctx
	 *	@param S_ResourceAssignment_ID
	 */
	public MResourceAssignment (Ctx ctx, int S_ResourceAssignment_ID, Trx trx)
	{
		super (ctx, S_ResourceAssignment_ID, trx);
		p_info.setUpdateable(true);		//	default table is not updateable
		//	Default values
		if (S_ResourceAssignment_ID == 0)
		{
			setAssignDateFrom(new Timestamp(System.currentTimeMillis()));
			setQty(new BigDecimal(1.0));
			setName(".");
			setIsConfirmed(false);
		}
	}	//	MResourceAssignment

	/**
	 * 	Load Contsructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MResourceAssignment (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MResourceAssignment
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		return success;
	}	//	afterSave
	
	/**
	 *  String Representation
	 *  @return string
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MResourceAssignment[ID=");
		sb.append(get_ID())
			.append(",S_Resource_ID=").append(getS_Resource_ID())
			.append(",From=").append(getAssignDateFrom())
			.append(",To=").append(getAssignDateTo())
			.append(",Qty=").append(getQty())
			.append("]");
		return sb.toString();
	}   //  toString

	/**
	 * 	Before Delete
	 *	@return true if not confirmed
	 */
	@Override
	protected boolean beforeDelete ()
	{
		//	 allow to delete, when not confirmed
		if (isConfirmed())
			return false;
		
		return true;
	}	//	beforeDelete
	
}	//	MResourceAssignment
