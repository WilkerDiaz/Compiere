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
 * 	Request Resolution Model
 *  @author Jorg Janke
 *  @version $Id: MResolution.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MResolution extends X_R_Resolution
{
    /** Logger for class MResolution */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MResolution.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	/**
	 * 	Get MResolution from Cache
	 *	@param ctx context
	 *	@param R_Resolution_ID id
	 *	@return MResolution
	 */
	public static MResolution get (Ctx ctx, int R_Resolution_ID)
	{
		Integer key = Integer.valueOf (R_Resolution_ID);
		MResolution retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MResolution (ctx, R_Resolution_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer,MResolution>	s_cache	= new CCache<Integer,MResolution>("R_Resolution", 10);
	
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_Resolution_ID id
	 *	@param trx
	 */
	public MResolution (Ctx ctx, int R_Resolution_ID, Trx trx)
	{
		super (ctx, R_Resolution_ID, trx);
	}	//	MResolution

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MResolution (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MResolution
	
}	//	MResolution
