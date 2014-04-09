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
 *  Generate Sales Order from Project.
 *
 *	@author Jorg Janke
 *	@version $Id: ProjectGenOrder.java,v 1.3 2006/07/30 00:51:01 jjanke Exp $
 */
public class ProjectGenOrder extends SvrProcess
{
	/**	Project ID from project directly		*/
	private int		m_C_Project_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		m_C_Project_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("C_Project_ID=" + m_C_Project_ID);
		if (m_C_Project_ID == 0)
			throw new IllegalArgumentException("C_Project_ID == 0");
		MProject fromProject = getProject (getCtx(), m_C_Project_ID, get_TrxName());
		getCtx().setIsSOTrx(true);	//	Set SO context

		/** @todo duplicate invoice prevention */

		MOrder order = new MOrder (fromProject, true, MOrder.DocSubTypeSO_OnCredit);
		if (!order.save())
			throw new Exception("Could not create Order");

		//	***	Lines ***
		int count = 0;
		
		//	Service Project	
		if (X_C_Project.PROJECTCATEGORY_ServiceChargeProject.equals(fromProject.getProjectCategory()))
		{
			/** @todo service project invoicing */
			throw new Exception("Service Charge Projects are on the TODO List");
		}	//	Service Lines

		else	//	Order Lines
		{
			MProjectLine[] lines = fromProject.getLines ();
			for (MProjectLine element : lines) {
				MOrderLine ol = new MOrderLine(order);
				ol.setLine(element.getLine());
				ol.setDescription(element.getDescription());
				//
				ol.setM_Product_ID(element.getM_Product_ID(), true);
				ol.setQty(element.getPlannedQty().subtract(element.getInvoicedQty()));
				ol.setPrice();
				if (element.getPlannedPrice() != null && element.getPlannedPrice().compareTo(Env.ZERO) != 0)
					ol.setPrice(element.getPlannedPrice());
				ol.setDiscount();
				ol.setTax();
				if (ol.save())
					count++;
			}	//	for all lines
			if (lines.length != count)
				log.log(Level.SEVERE, "Lines difference - ProjectLines=" + lines.length + " <> Saved=" + count);
		}	//	Order Lines

		// touch order to recalculate tax and totals
		order.setIsActive(order.isActive());
		order.save();

		return "@C_Order_ID@ " + order.getDocumentNo() + " (" + count + ")";
	}	//	doIt

	/**
	 * 	Get and validate Project
	 * 	@param ctx context
	 * 	@param C_Project_ID id
	 * 	@return valid project
	 * 	@param trx transaction
	 */
	static protected MProject getProject (Ctx ctx, int C_Project_ID, Trx trx)
	{
		MProject fromProject = new MProject (ctx, C_Project_ID, trx);
		if (fromProject.getC_Project_ID() == 0)
			throw new IllegalArgumentException("Project not found C_Project_ID=" + C_Project_ID);
		if (fromProject.getM_PriceList_Version_ID() == 0)
			throw new IllegalArgumentException("Project has no Price List");
		if (fromProject.getM_Warehouse_ID() == 0)
			throw new IllegalArgumentException("Project has no Warehouse");
		if (fromProject.getC_BPartner_ID() == 0 || fromProject.getC_BPartner_Location_ID() == 0)
			throw new IllegalArgumentException("Project has no Business Partner/Location");
		return fromProject;
	}	//	getProject

}	//	ProjectGenOrder
