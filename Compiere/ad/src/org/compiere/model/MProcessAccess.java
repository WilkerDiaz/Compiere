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
 *	Process Access Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MProcessAccess.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MProcessAccess extends X_AD_Process_Access
{
    /** Logger for class MProcessAccess */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProcessAccess.class);
	/** */
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trx transaction
	 */
	public MProcessAccess (Ctx ctx, int ignored, Trx trx)
	{
		super(ctx, 0, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		else
		{
		//	setAD_Process_ID (0);
		//	setAD_Role_ID (0);
			setIsReadWrite (true);
		}
	}	//	MProcessAccess

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MProcessAccess (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProcessAccess

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param AD_Role_ID role id
	 */
	public MProcessAccess (MProcess parent, int AD_Role_ID)
	{
		super (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setAD_Process_ID (parent.getAD_Process_ID());
		setAD_Role_ID (AD_Role_ID);
	}	//	MProcessAccess

	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MProcessAccess[")
	        .append("AD_Process_ID=").append(getAD_Process_ID())
	        .append(",AD_Role_ID=").append(getAD_Role_ID());
	    sb.append("]");
	    return sb.toString();
    }	//	toString
    
}	//	MProcessAccess
