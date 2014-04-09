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
 *	Sales Region Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MSalesRegion.java 8898 2010-06-06 16:14:49Z ragrawal $
 */
public class MSalesRegion extends X_C_SalesRegion
{
    /** Logger for class MSalesRegion */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MSalesRegion.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get SalesRegion from Cache
	 *	@param ctx context
	 *	@param C_SalesRegion_ID id
	 *	@return MSalesRegion
	 */
	public static MSalesRegion get (Ctx ctx, int C_SalesRegion_ID)
	{
		Integer key = Integer.valueOf (C_SalesRegion_ID);
		MSalesRegion retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MSalesRegion (ctx, C_SalesRegion_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer,MSalesRegion>	s_cache	= new CCache<Integer,MSalesRegion>("C_SalesRegion", 10);
	
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param C_SalesRegion_ID id
	 *	@param trx transaction
	 */
	public MSalesRegion (Ctx ctx, int C_SalesRegion_ID, Trx trx)
	{
		super (ctx, C_SalesRegion_ID, trx);
		if (C_SalesRegion_ID == 0)
		{
			setIsDefault(false);
			setIsSummary(false);
		}
	}	//	MSalesRegion

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MSalesRegion (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MSalesRegion

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
	 * 	After Save.
	 * 	Insert
	 * 	- create tree
	 *	@param newRecord insert
	 *	@param success save success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		//	Value/Name change
		if (!newRecord && (is_ValueChanged("Value") || is_ValueChanged("Name")))
			MAccount.updateValueDescription(getCtx(), "C_SalesRegion_ID= ? " ,
					get_Trx(),new Object[]{getC_SalesRegion_ID()});

		return true;
	}	//	afterSave
	
}	//	MSalesRegion
