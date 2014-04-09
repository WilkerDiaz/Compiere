/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;

import org.compiere.util.*;


/**
 * 	Login Message Log Model
 *	@author Jorg Janke
 */
public class MLoginMsgLog extends X_AD_LoginMsgLog
{
    /** Logger for class MLoginMsgLog */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MLoginMsgLog.class);

	/** */
    private static final long serialVersionUID = -5501718165298207287L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_LoginMsgLog_ID id
	 *	@param trx transaction
	 */
	public MLoginMsgLog(Ctx ctx, int AD_LoginMsgLog_ID, Trx trx)
	{
		super(ctx, AD_LoginMsgLog_ID, trx);
	}	//	MLoginMsgLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MLoginMsgLog(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MLoginMsgLog
	
	/**
	 * 	Parent Constructor
	 *	@param parent login message
	 *	@param sess session
	 *	@param answer optional answer
	 */
	public MLoginMsgLog(MLoginMsg parent)
	{
		this(parent.getCtx(), 0, parent.get_Trx());
	//	setClientOrg(parent);	do NOT set this
		setAD_LoginMsg_ID(parent.getAD_LoginMsg_ID());
	}	//	MLoginMsgLog
	
	
	/**
	 * 	Used Excepted.
	 * 	Note that the user may not have been asked
	 *	@return true if the user was asked and accepted
	 */
	public boolean isUserAccepted()
	{
		String s = getIsUserAccepted();
		return s != null && ISUSERACCEPTED_Yes.equals(s);
	}	//	isUserAccepted
	
	/**
	 * 	Used Rejected.
	 * 	Note that the user may not have been asked
	 *	@return true if the user was asked and rejected
	 */
	public boolean isUserRejected()
	{
		String s = getIsUserAccepted();
		return s != null && ISUSERACCEPTED_No.equals(s);
	}	//	isUserRejected

	/**
	 * 	Set User
	 * 	@param AD_User_ID user
	 */
	@Override
	public void setAD_User_ID(int AD_User_ID)
	{
		if (AD_User_ID == 0)
			set_ValueNoCheck("AD_User_ID", Integer.valueOf (0));
		else
			super.setAD_User_ID(AD_User_ID);
	}	//	setAD_User_ID
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MLoginMsgLog[")
	    	.append(get_ID())
	        .append(",AD_User_ID=").append(getAD_User_ID());
	    if (getIsUserAccepted() != null)
	        sb.append(",Accepted=").append(getIsUserAccepted());
	    
	        ;
	    sb.append("]");
	    return sb.toString();
    } //	toString
	
}	//	MLoginMsgLog
