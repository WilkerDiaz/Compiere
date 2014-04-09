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
 *	Distribution List Line
 *	
 *  @author Jorg Janke
 *  @version $Id: MDistributionListLine.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MDistributionListLine extends X_M_DistributionListLine
{
    /** Logger for class MDistributionListLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDistributionListLine.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_DistributionListLine_ID id
	 *	@param trx transaction
	 */
	public MDistributionListLine (Ctx ctx, int M_DistributionListLine_ID, Trx trx)
	{
		super (ctx, M_DistributionListLine_ID, trx);
	}	//	MDistributionListLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MDistributionListLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDistributionListLine
	
	
	/**
	 * 	Get Min Qty
	 *	@return min Qty or 0
	 */
	@Override
	public BigDecimal getMinQty ()
	{
		BigDecimal minQty = super.getMinQty ();
		if (minQty == null)
			return Env.ZERO;
		return minQty;
	}	//	getMinQty
	
	
	/**
	 * 	Get Ratio
	 *	@return ratio or 0
	 */
	@Override
	public BigDecimal getRatio ()
	{
		BigDecimal ratio = super.getRatio();
		if (ratio == null)
			return Env.ZERO;
		return ratio;
	}	//	getRatio
	
}	//	MDistributionListLine
