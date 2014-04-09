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
 *	Alert Recipient
 *	
 *  @author Jorg Janke
 *  @version $Id: MAlertRecipient.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MAlertRecipient extends X_AD_AlertRecipient
{
    /** Logger for class MAlertRecipient */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAlertRecipient.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_AlertRecipient_ID id
	 *	@param trx transaction
	 */
	public MAlertRecipient (Ctx ctx, int AD_AlertRecipient_ID, Trx trx)
	{
		super (ctx, AD_AlertRecipient_ID, trx);
	}	//	MAlertRecipient

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAlertRecipient (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAlertRecipient

	
	
	/**
	 * 	Get User
	 *	@return	AD_User_ID or -1 if none
	 */
	@Override
	public int getAD_User_ID ()
	{
		Integer ii = (Integer)get_Value("AD_User_ID");
		if (ii == null) 
			return -1;
		return ii.intValue();
	}	//	getAD_User_ID
	
	
	/**
	 * 	Get Role
	 *	@return AD_Role_ID or -1 if none
	 */
	@Override
	public int getAD_Role_ID ()
	{
		Integer ii = (Integer)get_Value("AD_Role_ID");
		if (ii == null) 
			return -1;
		return ii.intValue();
	}	//	getAD_Role_ID
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAlertRecipient[");
		sb.append(get_ID())
			.append(",AD_User_ID=").append(getAD_User_ID())
			.append(",AD_Role_ID=").append(getAD_Role_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MAlertRecipient
