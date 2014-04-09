/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.compiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@compiere.org 
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;

import org.compiere.util.*;


/**
 *	Window Access Log Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MWindowLog.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MWindowLog extends X_AD_WindowLog
{
    /** Logger for class MWindowLog */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MWindowLog.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_WindowLog_ID id
	 *	@param trx p_trx
	 */
	public MWindowLog(Ctx ctx, int AD_WindowLog_ID, Trx trx)
	{
		super(ctx, AD_WindowLog_ID, trx);
		if (AD_WindowLog_ID == 0)
		{
			int AD_Role_ID = ctx.getAD_Role_ID();
			setAD_Role_ID(AD_Role_ID);
		}
	}	//	MWindowLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MWindowLog(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MWindowLog
	
	/**
	 * 	Window Log
	 *	@param ctx context
	 *	@param AD_Session_ID session
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 *	@param AD_Window_ID window 
	 *	@param AD_Form_ID form
	 */
	public MWindowLog(Ctx ctx, int AD_Session_ID, 
		int AD_Client_ID, int AD_Org_ID,
		int AD_Window_ID, int AD_Form_ID)
	{
		this (ctx, 0, null);		//	out of p_trx
		setAD_Session_ID(AD_Session_ID);
		setClientOrg (AD_Client_ID, AD_Org_ID);
		//
		if (AD_Window_ID != 0)
			setAD_Window_ID(AD_Window_ID);
		else if (AD_Form_ID != 0)
			setAD_Form_ID(AD_Form_ID);
		else
			log.severe("No Window/Form");
	}	//	MWindowLog
	
}	//	MWindowLog
