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

import java.math.*;
import java.sql.*;

import org.compiere.util.*;

/**
 * 	Material Transaction Model
 *
 *	@author Jorg Janke
 *	@version $Id: MTransaction.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MTransaction extends X_M_Transaction
{
    /** Logger for class MTransaction */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTransaction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Transaction_ID id
	 *	@param trx transaction
	 */
	public MTransaction (Ctx ctx, int M_Transaction_ID, Trx trx)
	{
		super (ctx, M_Transaction_ID, trx);
		if (M_Transaction_ID == 0)
		{
		//	setM_Transaction_ID (0);		//	PK
		//	setM_Locator_ID (0);
		//	setM_Product_ID (0);
			setMovementDate (new Timestamp(System.currentTimeMillis()));
			setMovementQty (Env.ZERO);
		//	setMovementType (MOVEMENTTYPE_CustomerShipment);
		}
	}	//	MTransaction

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MTransaction (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MTransaction

	/**
	 * 	Detail Constructor
	 *	@param ctx context
	 *	@param AD_Org_ID org
	 * 	@param MovementType movement type
	 * 	@param M_Locator_ID locator
	 * 	@param M_Product_ID product
	 * 	@param M_AttributeSetInstance_ID attribute
	 * 	@param MovementQty qty
	 * 	@param MovementDate optional date
	 *	@param trx transaction
	 */
	public MTransaction (Ctx ctx, int AD_Org_ID, 
		String MovementType, 
		int M_Locator_ID, int M_Product_ID, int M_AttributeSetInstance_ID, 
		BigDecimal MovementQty, Timestamp MovementDate, Trx trx)
	{
		super(ctx, 0, trx);
		setAD_Org_ID(AD_Org_ID);
		setMovementType (MovementType);
		if (M_Locator_ID == 0)
			throw new IllegalArgumentException("No Locator");
		setM_Locator_ID (M_Locator_ID);
		if (M_Product_ID == 0)
			throw new IllegalArgumentException("No Product");
		setM_Product_ID (M_Product_ID);
		setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
		//
		if (MovementQty != null)		//	Can be 0
			setMovementQty (MovementQty);
		if (MovementDate == null)
			setMovementDate (new Timestamp(System.currentTimeMillis()));
		else
			setMovementDate(MovementDate);
	}	//	MTransaction

	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MTransaction[");
		sb.append(get_ID()).append(",").append(getMovementType())
			.append(",Qty=").append(getMovementQty())
			.append(",M_Product_ID=").append(getM_Product_ID())
			.append(",ASI=").append(getM_AttributeSetInstance_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MTransaction
