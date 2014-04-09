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
 * 	Tax Declaration Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTaxDeclaration.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MTaxDeclaration extends X_C_TaxDeclaration
{
    /** Logger for class MTaxDeclaration */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTaxDeclaration.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructors
	 *	@param ctx context
	 *	@param C_TaxDeclaration_ID ic
	 *	@param trx p_trx
	 */
	public MTaxDeclaration (Ctx ctx, int C_TaxDeclaration_ID, Trx trx)
	{
		super (ctx, C_TaxDeclaration_ID, trx);
	}	//	MTaxDeclaration

	/**
	 * 	Load Constructor
	 *	@param ctx context 
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MTaxDeclaration (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MTaxDeclaration
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (is_ValueChanged("DateFrom"))
			setDateFrom(TimeUtil.getDay(getDateFrom()));
		if (is_ValueChanged("DateTo"))
			setDateTo(TimeUtil.getDay(getDateTo()));
		return true;
	}	//	beforeSave
	
}	//	MTaxDeclaration
