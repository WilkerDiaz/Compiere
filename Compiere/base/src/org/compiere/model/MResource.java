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
 *	Resource Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MResource.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MResource extends X_S_Resource
{
    /** Logger for class MResource */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MResource.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param S_Resource_ID id
	 */
	public MResource (Ctx ctx, int S_Resource_ID, Trx trx)
	{
		super (ctx, S_Resource_ID, trx);
	}	//	MResource

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MResource (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MResource
	
	
	/** Cached Resource Type	*/
	private MResourceType	m_resourceType = null;
	/** Cached Product			*/
	private MProduct		m_product = null;
	
	
	/**
	 * 	Get cached Resource Type
	 *	@return Resource Type
	 */
	public MResourceType getResourceType()
	{
		if (m_resourceType == null && getS_ResourceType_ID() != 0)
			m_resourceType = new MResourceType (getCtx(), getS_ResourceType_ID(), get_Trx());
		return m_resourceType;
	}	//	getResourceType
	
	/**
	 * 	Get Product
	 *	@return product
	 */
	public MProduct getProduct()
	{
		if (m_product == null)
		{
			ArrayList<MProduct> products = MProduct.findAll(getCtx(), 
				"S_Resource_ID=" + getS_Resource_ID(), get_Trx());
			if (products.size() > 0)
				m_product = products.get(0);
		}
		return m_product;
	}	//	getProduct
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if it can be saved
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord)
		{
			if (getValue() == null || getValue().length() == 0)
				setValue(getName());
			m_product = new MProduct(this, getResourceType());
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
		if (prod.setResource(this))
			prod.save(get_Trx());
		
		return success;
	}	//	afterSave
	
}	//	MResource
