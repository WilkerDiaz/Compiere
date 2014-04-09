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
import java.util.*;

import org.compiere.util.*;


/**
 *	Expense Type Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MExpenseType.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MExpenseType extends X_S_ExpenseType
{
    /** Logger for class MExpenseType */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MExpenseType.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param S_ExpenseType_ID id
	 *	@param trx transaction
	 */
	public MExpenseType (Ctx ctx, int S_ExpenseType_ID, Trx trx)
	{
		super (ctx, S_ExpenseType_ID, trx);
	}	//	MExpenseType

	/**
	 * 	MExpenseType
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MExpenseType (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MExpenseType
	
	/** Cached Product			*/
	private MProduct	m_product = null;
	
	/**
	 * 	Get Product
	 *	@return product
	 */
	public MProduct getProduct()
	{
		if (m_product == null)
		{
			ArrayList<MProduct> products = MProduct.findAll(getCtx(), 
				"S_ExpenseType_ID=" + getS_ExpenseType_ID(), get_Trx());
			if (products.size() > 0)
				m_product = products.get(0);
		}
		return m_product;
	}	//	getProduct
	
	
	/**
	 * 	beforeSave
	 *	@see org.compiere.model.PO#beforeSave(boolean)
	 *	@param newRecord
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord)
		{
			if (getValue() == null || getValue().length() == 0)
				setValue(getName());
			m_product = new MProduct(this);
			return m_product.save(get_Trx());
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
				
		MProduct prod = getProduct();
		if (prod.setExpenseType(this))
			prod.save(get_Trx());
		
		return success;
	}	//	afterSave
	
	
}	//	MExpenseType
