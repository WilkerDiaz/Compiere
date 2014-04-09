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

import org.compiere.api.*;
import org.compiere.common.constants.*;
import org.compiere.util.*;

/**
 * 	Production Plan Model.
 * 	(old)
 *	@author Jorg Janke
 */
public class MProductionLine extends X_M_ProductionLine
{
    /** Logger for class MProductionLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProductionLine.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Std Constructor
	 *	@param ctx
	 *	@param M_ProductionLine_ID
	 *	@param trx
	 */
	public MProductionLine(Ctx ctx, int M_ProductionLine_ID, Trx trx)
	{
		super(ctx, M_ProductionLine_ID, trx);
	}	//	MProductionLine

	/**
	 * 	Load Constructor
	 * @param ctx
	 * @param rs
	 * @param trx
	 */
	public MProductionLine(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProductionLine

	/**
	 * 	Set Product - Callout
	 *	@param oldM_Product_ID old value
	 *	@param newM_Product_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setM_Product_ID (String oldM_Product_ID, 
			String newM_Product_ID, int windowNo) throws Exception
	{
		if (newM_Product_ID == null || newM_Product_ID.length() == 0)
			return;
		int M_Product_ID = Integer.parseInt(newM_Product_ID);
		super.setM_Product_ID(M_Product_ID);
		if (M_Product_ID == 0)
		{
			setM_AttributeSetInstance_ID(0);
			return;
		}
		//	Set Attribute
		int M_AttributeSetInstance_ID = getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID");
		if (getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Product_ID") == M_Product_ID
			&& M_AttributeSetInstance_ID != 0)
			setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
		else
			setM_AttributeSetInstance_ID(0);
	}	//	setM_Product_ID
	
}	//	MProductionLine
