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
 * 	Request Group Model
 *  @author Jorg Janke
 *  @version $Id: MGroup.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MGroup extends X_R_Group
{
    /** Logger for class MGroup */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MGroup.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MGroup from Cache
	 *	@param ctx context
	 *	@param R_Group_ID id
	 *	@return MGroup
	 */
	public static MGroup get (Ctx ctx, int R_Group_ID)
	{
		Integer key = Integer.valueOf (R_Group_ID);
		MGroup retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MGroup (ctx, R_Group_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static final CCache<Integer,MGroup> s_cache = new CCache<Integer,MGroup>("R_Group", 20);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_Group_ID group
	 *	@param trx p_trx
	 */
	public MGroup (Ctx ctx, int R_Group_ID, Trx trx)
	{
		super (ctx, R_Group_ID, trx);
	}	//	MGroup

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MGroup (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MGroup
	
}	//	MGroup
