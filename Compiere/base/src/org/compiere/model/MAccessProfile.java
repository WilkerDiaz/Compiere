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
 * 	Media Access Profile
 *	
 *  @author Jorg Janke
 *  @version $Id: MAccessProfile.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MAccessProfile extends X_CM_AccessProfile
{
    /** Logger for class MAccessProfile */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAccessProfile.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Access to Container
	 *	@param ctx context
	 *	@param CM_Container_ID
	 *	@param AD_User_ID user to check
	 *	@return true if access to container
	 */
	public static boolean isAccessContainer (Ctx ctx, int CM_Container_ID,
		int AD_User_ID)
	{
		//	NIT
		return true;
	}	//	isAccessContainer

	/**
	 * 	Access to Container
	 *	@param ctx context
	 *	@param CM_Container_ID
	 *	@param AD_Role_ID 0 or role to checl
	 *	@param C_BPGroup_ID 0 or bpartner to check
	 *	@return true if access to container
	 */
	public static boolean isAccessContainer (Ctx ctx, int CM_Container_ID,
		int AD_Role_ID, int C_BPGroup_ID)
	{
		//	NIT
		return true;
	}	//	isAccessContainer
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param CM_AccessProfile_ID id
	 *	@param trx transaction
	 */
	public MAccessProfile (Ctx ctx, int CM_AccessProfile_ID,
		Trx trx)
	{
		super (ctx, CM_AccessProfile_ID, trx);
	}	//	MAccessProfile

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAccessProfile (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MAccessProfile
	
	
}	//	MAccessProfile
