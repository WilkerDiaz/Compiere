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
 *	Payment Batch Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPaymentBatch.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MPaymentBatch extends X_C_PaymentBatch
{
    /** Logger for class MPaymentBatch */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPaymentBatch.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Payment Batch for PaySelection
	 *	@param ctx context
	 *	@param C_PaySelection_ID id
	 *	@param trx transaction
	 *	@return payment batch
	 */
	public static MPaymentBatch getForPaySelection (Ctx ctx, int C_PaySelection_ID, Trx trx)
	{
		MPaySelection ps = new MPaySelection (ctx, C_PaySelection_ID, trx);
		MPaymentBatch retValue = new MPaymentBatch (ps);
		return retValue;
	}	//	getForPaySelection
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_PaymentBatch_ID id
	 *	@param trx transaction
	 */
	public MPaymentBatch (Ctx ctx, int C_PaymentBatch_ID, Trx trx)
	{
		super(ctx, C_PaymentBatch_ID, trx);
		if (C_PaymentBatch_ID == 0)
		{
		//	setName (null);
			setProcessed (false);
			setProcessing (false);
		}
	}	//	MPaymentBatch

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPaymentBatch (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPaymentBatch

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param Name name
	 *	@param trx p_trx
	 */
	public MPaymentBatch (Ctx ctx, String Name, Trx trx)
	{
		this (ctx, 0, trx);
		setName (Name);
	}	//	MPaymentBatch

	/**
	 * 	Parent Constructor
	 *	@param ps Pay Selection
	 */
	public MPaymentBatch (MPaySelection ps)
	{
		this (ps.getCtx(), 0, ps.get_Trx());
		setClientOrg(ps);
		setName (ps.getName());
	}	//	MPaymentBatch

}	//	MPaymentBatch
