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
 * Language Context Area Model
 * @author Jorg Janke
 */
public class MCtxArea extends X_AD_CtxArea
{
    /** Logger for class MCtxArea */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCtxArea.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Standard Constructor
	 * @param ctx context
	 * @param AD_CtxArea_ID id
	 * @param trx transaction
	 */
	public MCtxArea(Ctx ctx, int AD_CtxArea_ID, Trx trx)
	{
		super(ctx, AD_CtxArea_ID, trx);
		if (AD_CtxArea_ID == 0)
		{
			setEntityType(ENTITYTYPE_UserMaintained); // U
			setIsSOTrx(false);
		}
	} // MCtxArea

	/**
	 * Load Constructor
	 * @param ctx context
	 * @param rs result set
	 * @param trx transaction
	 */
	public MCtxArea(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	} // MCtxArea

	
	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MCtxArea[")
			.append(get_ID()).append("-").append(getName()).append("]");
		return sb.toString();
	}	//	toString
	
}	//	MCtxArea
