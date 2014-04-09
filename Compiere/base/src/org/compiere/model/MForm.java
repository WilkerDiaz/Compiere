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
 *	Form Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MForm.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MForm extends X_AD_Form
{
    /** Logger for class MForm */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MForm.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_Form_ID id
	 *	@param trx transaction
	 */
	public MForm (Ctx ctx, int AD_Form_ID, Trx trx)
	{
		super (ctx, AD_Form_ID, trx);
	}	//	MForm

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MForm (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MForm
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord)
		{
			int AD_Role_ID = getCtx().getAD_Role_ID();
			MFormAccess pa = new MFormAccess(this, AD_Role_ID);
			pa.save();
		}
		return success;
	}	//	afterSave
	
}	//	MForm
