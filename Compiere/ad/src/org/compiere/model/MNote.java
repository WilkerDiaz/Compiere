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
import java.util.logging.*;
import org.compiere.util.*;


/**
 *  Note Model
 *
 *  @author Jorg Janke
 *  @version $Id: MNote.java 8780 2010-05-19 23:08:57Z nnayak $
 */
public class MNote extends X_AD_Note
{
    /** Logger for class MNote */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MNote.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param AD_Note_ID id
	 *	@param trx transaction
	 */
	public MNote (Ctx ctx, int AD_Note_ID, Trx trx)
	{
		super (ctx, AD_Note_ID, trx);
		if (AD_Note_ID == 0)
		{
			setProcessed (false);
			setProcessing(false);
		}
	}	//	MNote

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MNote(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MNote

	/**
	 *  New Mandatory Constructor
	 * 	@param ctx context
	 *  @param AD_Message_ID message
	 *  @param AD_User_ID targeted user
	 *	@param trx transaction
	 */
	public MNote (Ctx ctx, int AD_Message_ID, int AD_User_ID, Trx trx) 
	{
		this (ctx, 0, trx);
		setAD_Message_ID (AD_Message_ID);
		setAD_User_ID(AD_User_ID);
	}	//	MNote

	/**
	 *  New Mandatory Constructor
	 * 	@param ctx context
	 *  @param AD_MessageValue message
	 *  @param AD_User_ID targeted user
	 *	@param trx transaction
	 */
	public MNote (Ctx ctx, String AD_MessageValue, int AD_User_ID, Trx trx) 
	{
		this (ctx, MMessage.getAD_Message_ID(ctx, AD_MessageValue), AD_User_ID, trx);
	}	//	MNote

	/**
	 * 	Create Note
	 *	@param ctx context
	 *	@param AD_Message_ID message
	 *	@param AD_User_ID user
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@param TextMsg text message
	 *	@param Reference reference
	 *	@param trx transaction
	 */
	public MNote (Ctx ctx, int AD_Message_ID, int AD_User_ID,
		int AD_Table_ID, int Record_ID, 
		String Reference, String TextMsg, Trx trx)
	{
		this (ctx, AD_Message_ID, AD_User_ID, trx);
		setRecord(AD_Table_ID, Record_ID);
		setReference(Reference);
		setTextMsg(TextMsg);
	}	//	MNote

	/**
	 *  New Constructor
	 * 	@param ctx context
	 *  @param AD_MessageValue message
	 *  @param AD_User_ID targeted user
	 *  @param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 *	@param trx transaction
	 */
	public MNote (Ctx ctx, String AD_MessageValue, int AD_User_ID, 
		int AD_Client_ID, int AD_Org_ID, Trx trx) 
	{
		this (ctx, MMessage.getAD_Message_ID(ctx, AD_MessageValue), AD_User_ID, trx);
		setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	MNote


	/**************************************************************************
	 * 	Set Record.
	 * 	(Ss Button and defaults to String)
	 *	@param AD_Message AD_Message
	 */
	public void setAD_Message_ID (String AD_Message)
	{
		int AD_Message_ID = QueryUtil.getSQLValue(null,
			"SELECT AD_Message_ID FROM AD_Message WHERE Value=?", AD_Message);
		if (AD_Message_ID != -1)
			super.setAD_Message_ID(AD_Message_ID);
		else
		{
			super.setAD_Message_ID(240); //	Error
			log.log(Level.SEVERE, "setAD_Message_ID - ID not found for '" + AD_Message + "'");
		}
	}	//	setRecord_ID

	/**
	 * 	Set AD_Message_ID.
	 * 	Looks up No Message Found if 0
	 *	@param AD_Message_ID id
	 */
	@Override
	public void setAD_Message_ID (int AD_Message_ID)
	{
		if (AD_Message_ID == 0)
			super.setAD_Message_ID(MMessage.getAD_Message_ID(getCtx(), "NoMessageFound"));
		else
			super.setAD_Message_ID(AD_Message_ID);
	}	//	setAD_Message_ID

	/**
	 * 	Get Message
	 *	@return message
	 */
	public String getMessage()
	{
		int AD_Message_ID = getAD_Message_ID();
		MMessage msg = MMessage.get(getCtx(), AD_Message_ID);
		return msg.getMsgText();
	}	//	getMessage

	/**
	 * 	Set Client Org
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 */
	@Override
	public void setClientOrg(int AD_Client_ID, int AD_Org_ID) 
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg
	
	/**
	 * 	Set Record
	 * 	@param AD_Table_ID table
	 * 	@param Record_ID record
	 */
	public void setRecord (int AD_Table_ID, int Record_ID)
	{
		setAD_Table_ID(AD_Table_ID);
		setRecord_ID(Record_ID);
	}	//	setRecord


	/**
	 * 	String Representation
	 *	@return	info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MNote[")
			.append(get_ID()).append(",AD_Message_ID=").append(getAD_Message_ID())
			.append(",").append(getReference())
			.append(",Processed=").append(isProcessed())
			.append("]");
		return sb.toString();
	}	//	toString

}	//	MNote
