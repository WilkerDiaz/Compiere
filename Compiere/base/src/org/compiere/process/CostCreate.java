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
package org.compiere.process;

import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Create/Update Costing for Product
 *	
 *  @author Jorg Janke
 *  @version $Id: CostCreate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class CostCreate extends SvrProcess
{
	/**	Product				*/
	private int 	p_M_Product_ID = 0; 

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
		//	log.fine("prepare - " + para[i]);
			if (element.getParameter() == null)
				;
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);		
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (text with variables)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("M_Product_ID=" + p_M_Product_ID);
		if (p_M_Product_ID == 0)
			throw new CompiereUserException("@NotFound@: @M_Product_ID@ = " + p_M_Product_ID);
		MProduct product = MProduct.get(getCtx(), p_M_Product_ID);
		if (product.get_ID() != p_M_Product_ID)
			throw new CompiereUserException("@NotFound@: @M_Product_ID@ = " + p_M_Product_ID);
		//
		if (MCostDetail.processProduct(product, get_TrxName()))
			return "@OK@";
		return "@Error@";
	}	//	doIt
	
}	//	CostCreate
