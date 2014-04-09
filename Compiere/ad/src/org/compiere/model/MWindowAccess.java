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
 *	Window Access Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MWindowAccess.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MWindowAccess extends X_AD_Window_Access
{
    /** Logger for class MWindowAccess */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MWindowAccess.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param ignored -
	 *	@param trx transaction
	 */
	public MWindowAccess (Ctx ctx, int ignored, Trx trx)
	{
		super(ctx, 0, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		else
		{
		//	setAD_Role_ID (0);
		//	setAD_Window_ID (0);
			setIsReadWrite (true);
		}
	}	//	MWindowAccess

	/**
	 * 	MWindowAccess
	 *	@param ctx
	 *	@param rs
	 *	@param trx transaction
	 */
	public MWindowAccess (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MWindowAccess

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param AD_Role_ID role id
	 */
	public MWindowAccess (MWindow parent, int AD_Role_ID)
	{
		super (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setAD_Window_ID(parent.getAD_Window_ID());
		setAD_Role_ID (AD_Role_ID);
	}	//	MWindowAccess

	/**
	 * 	String Info
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MWindowAccess[");
		sb.append("AD_Window_ID=").append(getAD_Window_ID())
			.append(",AD_Role_ID=").append(getAD_Role_ID())
			.append("]");
		return sb.toString();
	}	//	toString

}	//	MWindowAccess
