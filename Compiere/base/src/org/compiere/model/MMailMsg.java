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
 * 	Wab Store Mail Message Model
 *  @author Jorg Janke
 *  @version $Id: MMailMsg.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MMailMsg extends X_W_MailMsg
{
    /** Logger for class MMailMsg */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MMailMsg.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param W_MailMsg_ID id
	 *	@param trx p_trx
	 */
	public MMailMsg (Ctx ctx, int W_MailMsg_ID, Trx trx)
	{
		super (ctx, W_MailMsg_ID, trx);
		if (W_MailMsg_ID == 0)
		{
		//	setW_Store_ID (0);
		//	setMailMsgType (null);
		//	setName (null);
		//	setSubject (null);
		//	setMessage (null);
		}
	}	//	MMailMsg

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MMailMsg (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MMailMsg
	
	/**
	 * 	Full Constructor
	 *	@param parent store
	 *	@param MailMsgType msg type
	 *	@param Name name
	 *	@param Subject subject
	 *	@param Message message
	 *	@param Message2 message
	 *	@param Message3 message
	 */
	public MMailMsg (MStore parent, String MailMsgType, 
		String Name, String Subject, String Message, String Message2, String Message3)
	{
		this (parent.getCtx(), 0, parent.get_Trx());;
		setClientOrg(parent);
		setW_Store_ID(parent.getW_Store_ID());
		setMailMsgType (MailMsgType);
		setName (Name);
		setSubject (Subject);
		setMessage (Message);
		setMessage2 (Message2);
		setMessage3 (Message3);
	}	//	MMailMsg
	
}	//	MMailMsg
