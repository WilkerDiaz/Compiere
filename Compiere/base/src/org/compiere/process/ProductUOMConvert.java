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
import org.compiere.util.*;


/**
 *	Product UOM Conversion
 *	
 *  @author Jorg Janke
 *  @version $Id: ProductUOMConvert.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class ProductUOMConvert extends SvrProcess
{
	/** Product From			*/
	private int			p_M_Product_ID = 0;
	/** Product To				*/
	private int			p_M_Product_To_ID = 0;
	/** Locator					*/
	private int			p_M_Locator_ID = 0;
	/** Quantity				*/
	private BigDecimal	p_Qty = null;

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
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = element.getParameterAsInt();
			else if (name.equals("M_Product_To_ID"))
				p_M_Product_To_ID = element.getParameterAsInt();
			else if (name.equals("M_Locator_ID"))
				p_M_Locator_ID = element.getParameterAsInt();
			else if (name.equals("Qty"))
				p_Qty = (BigDecimal)element.getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		if (p_M_Product_ID == 0 || p_M_Product_To_ID == 0
			|| p_M_Locator_ID == 0 
			|| p_Qty == null || Env.ZERO.compareTo(p_Qty) == 0)
			throw new CompiereUserException("Invalid Parameter");
		//
		MProduct product = MProduct.get(getCtx(), p_M_Product_ID);
		MProduct productTo = MProduct.get(getCtx(), p_M_Product_To_ID);
		log.info("Product=" + product + ", ProductTo=" + productTo 
			+ ", M_Locator_ID=" + p_M_Locator_ID + ", Qty=" + p_Qty);
		
		MUOMConversion[] conversions = MUOMConversion.getProductConversions(getCtx(), 
				product.getM_Product_ID(), false);
		MUOMConversion conversion = null;
		for (MUOMConversion element : conversions) {
			if (element.getC_UOM_To_ID() == productTo.getC_UOM_ID())
				conversion = element;
		}
		if (conversion == null)
			throw new CompiereUserException("@NotFound@: @C_UOM_Conversion_ID@");
		
		MUOM uomTo = MUOM.get(getCtx(), productTo.getC_UOM_ID());
		BigDecimal qtyTo = p_Qty.divide(conversion.getDivideRate(), uomTo.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
		BigDecimal qtyTo6 = p_Qty.divide(conversion.getDivideRate(), 6, BigDecimal.ROUND_HALF_UP);
		if (qtyTo.compareTo(qtyTo6) != 0)
			throw new CompiereUserException("@StdPrecision@: " + qtyTo + " <> " + qtyTo6 
				+ " (" + p_Qty + "/" + conversion.getDivideRate() + ")");
		log.info(conversion + " -> " + qtyTo); 
		
		
		//	Set to Beta
		return "Not completed yet";
	}	//	doIt
	
}	//	ProductUOMConvert
