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
package org.compiere.cm.cache;

import org.compiere.cm.*;
import org.compiere.model.MContainerElement;

/**
 *	Container Element Cache
 *	
 *  @author Yves Sandfort
 *  @version $Id$
 */
public class ContainerElement extends CO {
	/**
	 * 	getCM_Container_Element
	 *	@param ID
	 *	@param CM_WebProject_ID
	 *	@return Container Element
	 */
	public MContainerElement getCM_Container_Element(String ID, int CM_WebProject_ID) {
		return getCM_Container_Element(Integer.parseInt(ID), CM_WebProject_ID);
	}
	
	/**
	 * 	getCM_Container_Element
	 *	@param ID
	 *	@param CM_WebProject_ID
	 *	@return Container Element
	 */
	public MContainerElement getCM_Container_Element(int ID, int CM_WebProject_ID) {
		if (cache.containsKey(ID)) {
			use(ID);
			return (MContainerElement) cache.get(ID);
		} else {
			MContainerElement thisContainerElement = MContainerElement.get(ctx, ID, HttpServletCM.getTrx());
			if (thisContainerElement==null) {
				// No Elements in DB found, needs to get handled
				return null;
			} else {
				put ("" + thisContainerElement.get_ID(),thisContainerElement);
				return thisContainerElement;
			}
		}
	}

}
