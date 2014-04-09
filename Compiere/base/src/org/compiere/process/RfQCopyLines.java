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

import java.math.*;
import java.util.logging.*;

import org.compiere.model.*;


/**
 *	Copy Lines	
 *	
 *  @author Jorg Janke
 *  @version $Id: RfQCopyLines.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class RfQCopyLines extends SvrProcess
{
	/**	From RfQ 			*/
	private int		p_From_RfQ_ID = 0;
	/**	From RfQ 			*/
	private int		p_To_RfQ_ID = 0;

	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_RfQ_ID"))
				p_From_RfQ_ID = ((BigDecimal)element.getParameter()).intValue();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_To_RfQ_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@see org.compiere.process.SvrProcess#doIt()
	 *	@return message
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("doIt - From_RfQ_ID=" + p_From_RfQ_ID + ", To_RfQ_ID=" + p_To_RfQ_ID);
		//
		MRfQ to = new MRfQ (getCtx(), p_To_RfQ_ID, get_TrxName());
		if (to.get_ID() == 0)
			throw new IllegalArgumentException("No To RfQ found");
		MRfQ from = new MRfQ (getCtx(), p_From_RfQ_ID, get_TrxName());
		if (from.get_ID() == 0)
			throw new IllegalArgumentException("No From RfQ found");
		
		//	Copy Lines
		int counter = 0;
		MRfQLine[] lines = from.getLines();
		for (MRfQLine element : lines) {
			MRfQLine newLine = new MRfQLine (to);
			newLine.setLine(element.getLine());
			newLine.setDescription(element.getDescription());
			newLine.setHelp(element.getHelp());
			newLine.setM_Product_ID(element.getM_Product_ID());
			newLine.setM_AttributeSetInstance_ID(element.getM_AttributeSetInstance_ID());
			newLine.setDeliveryDays(element.getDeliveryDays());
			newLine.save();
			//	Copy Qtys
			MRfQLineQty[] qtys = element.getQtys();
			for (MRfQLineQty element2 : qtys) {
				MRfQLineQty newQty = new MRfQLineQty (newLine);
				newQty.setC_UOM_ID(element2.getC_UOM_ID());
				newQty.setQty(element2.getQty());
				newQty.setIsOfferQty(element2.isOfferQty());
				newQty.setIsPurchaseQty(element2.isPurchaseQty());
				newQty.setMargin(element2.getMargin());
				newQty.save();
			}
			counter++;
		}	//	copy all lines	
		
		//
		return "# " + counter;
	}	//	doIt
}
