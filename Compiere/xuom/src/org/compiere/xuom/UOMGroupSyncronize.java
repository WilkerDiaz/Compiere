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
package org.compiere.xuom;

import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;

/**
 * 	Synchronize UOM Conversions of Products of UOM group 
 *	@author Jorg Janke
 */
public class UOMGroupSyncronize extends SvrProcess 
{
	/** UOM Group				*/
	private int		p_C_UOMGroup_ID = 0;	
	/** Force Synchronization	*/
	private boolean	p_ForceSynchronization = false;
	
	
	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare() 
	{
		p_C_UOMGroup_ID = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_UOMGroup_ID"))
				p_C_UOMGroup_ID = element.getParameterAsInt();
			else if (name.equals("ForceSynchronization"))
				p_ForceSynchronization = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 * 	@return summary
	 */
	@Override
	protected String doIt() throws Exception 
	{
		log.info("C_UOMGroup_ID=" + p_C_UOMGroup_ID 
			+ ", ForceSynchronization=" + p_ForceSynchronization);
		
		int count = 0;
		MUOMGroup group = null;
		MProduct[] products = MUOMGroup.getProducts(getCtx(), p_C_UOMGroup_ID);
		for (MProduct product : products) {
			int C_UOMGroup_ID = 0;
			Object gr = product.get_Value("C_UOMGroup_ID");	//	get Value
			if (gr instanceof Integer)
				C_UOMGroup_ID = ((Integer)gr).intValue();
			if (C_UOMGroup_ID == 0)
				continue;	//	No Group
			if (group == null || group.getC_UOMGroup_ID() != C_UOMGroup_ID)
				group = new MUOMGroup(getCtx(), C_UOMGroup_ID, null);
			if (group.getC_UOMGroup_ID() != C_UOMGroup_ID)
				continue;	//	Group not found
			//
			count += group.updateProduct(product, p_ForceSynchronization);
		}
		return "@M_Product_ID@ #" + products.length + " - @Updated@ #" + count;
	}	//	doIt

}	//	UOMGroupSyncronize
