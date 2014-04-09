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
 *	Discount Schema Line (Price List) Model 
 *	
 *  @author Jorg Janke
 *  @version $Id: MDiscountSchemaLine.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MDiscountSchemaLine extends X_M_DiscountSchemaLine
{
    /** Logger for class MDiscountSchemaLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDiscountSchemaLine.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_DiscountSchemaLine_ID id
	 *	@param trx transaction
	 */
	public MDiscountSchemaLine (Ctx ctx, int M_DiscountSchemaLine_ID, Trx trx)
	{
		super (ctx, M_DiscountSchemaLine_ID, trx);
	}	//	MDiscountSchemaLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MDiscountSchemaLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDiscountSchemaLine

}	//	MDiscountSchemaLine
