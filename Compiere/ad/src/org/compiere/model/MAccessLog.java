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
 *	Access Log Model.
 *	Maintains it's own context
 *
 *  @author Jorg Janke
 *  @version $Id: MAccessLog.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MAccessLog extends X_AD_AccessLog
{
    /** Logger for class MAccessLog */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAccessLog.class);
	/** */
    private static final long serialVersionUID = -8680529488968907188L;


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_AccessLog_ID id
	 *	@param trx transaction
	 */
	public MAccessLog (Ctx ctx, int AD_AccessLog_ID, Trx trx)
	{
		super (new Ctx(ctx), AD_AccessLog_ID, trx);
	}	//	MAccessLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAccessLog (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(new Ctx(ctx), rs, trx);
	}	//	MAccessLog

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param email mail
	 *	@param Remote_Host host
	 *	@param Remote_Addr address
	 *	@param TextMsg text message
	 *	@param trx transaction
	 */
	public MAccessLog (Ctx ctx, String email,
		String Remote_Host, String Remote_Addr,
		String TextMsg,
		Trx trx)
	{
		this (ctx, 0, trx);
		setRemote_Addr(Remote_Addr);
		setRemote_Host(Remote_Host);
		setTextMsg(TextMsg);
		setCreatedBy(email);	//	Sets AD_User
	}	//	MAccessLog

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param email mail
	 *	@param AD_Table_ID table
	 *	@param AD_Column_ID column
	 *	@param Record_ID record
	 *	@param trx transaction
	 */
	public MAccessLog (Ctx ctx, String email,
		int AD_Table_ID, int AD_Column_ID, int Record_ID,
		Trx trx)
	{
		this (ctx, 0, trx);
		setAD_Table_ID(AD_Table_ID);
		setAD_Column_ID(AD_Column_ID);
		setRecord_ID(Record_ID);
		setCreatedBy(email);
	}	//	MAccessLog

	/**
	 * 	Set User, Created/Updated By & Context
	 *	@param email mail address
	 */
	void setCreatedBy (String email)
	{
		if (email == null || email.length() == 0)
			return;
		int AD_User_ID = MUser.getAD_User_ID (email, getAD_Client_ID());
		set_ValueNoCheck ("CreatedBy", Integer.valueOf(AD_User_ID));
		setUpdatedBy (AD_User_ID);
		getCtx().setAD_User_ID(AD_User_ID);
		setAD_User_ID(AD_User_ID);
	}	//	setCreatedBy


	/**
	 * 	Add to Reply
	 *	@param Reply
	 */
	public void addReply (String Reply)
	{
		if (Reply == null || Reply.length() == 0)
			return;
		String old = getReply();
		if (old == null || old.length() == 0)
			setReply(Reply);
		else
			setReply(old + " - " + Reply);
	}	//	addReply

}	//	MAccessLog
