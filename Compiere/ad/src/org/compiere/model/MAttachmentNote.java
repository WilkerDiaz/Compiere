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
 *	Attachment Note
 *	
 *  @author Jorg Janke
 *  @version $Id: MAttachmentNote.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MAttachmentNote extends X_AD_AttachmentNote
{
    /** Logger for class MAttachmentNote */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttachmentNote.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param AD_AttachmentNote_ID id
	 *	@param trx transaction
	 */
	public MAttachmentNote (Ctx ctx, int AD_AttachmentNote_ID, Trx trx)
	{
		super (ctx, AD_AttachmentNote_ID, trx);
		/**
		if (AD_AttachmentNote_ID == 0)
		{
			setAD_Attachment_ID (0);
			setAD_User_ID (0);
			setTextMsg (null);
			setTitle (null);
		}
		/**/
	}	//	MAttachmentNote
	
	/** 
	 * 	Load Constructor 
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAttachmentNote (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAttachmentNote

	/**
	 * 	Parent Constructor.
	 * 	Sets current user.
	 *	@param attach attachment
	 *	@param Title title
	 *	@param TextMsg ext message
	 */
	public MAttachmentNote (MAttachment attach, String Title, String TextMsg)
	{
		this (attach.getCtx(), 0, attach.get_Trx());
		setClientOrg(attach);
		setAD_Attachment_ID (attach.getAD_Attachment_ID());
		setAD_User_ID(attach.getCtx().getAD_User_ID());
		setTitle (Title);
		setTextMsg (TextMsg);
	}	//	MAttachmentNote
	
}	//	MAttachmentNote
