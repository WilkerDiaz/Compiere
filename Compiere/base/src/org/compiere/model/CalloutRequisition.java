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
 *	Requisition Callouts
 *
 *  @author Jorg Janke
 *  @version $Id: CalloutRequisition.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class CalloutRequisition extends CalloutEngine
{
	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());

	/**
	 *	Requisition Line - Product.
	 *		- PriceStd
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String product (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer M_Product_ID = (Integer)value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";
	//	setCalloutActive(true);
		//
		/**	Set Attribute
		if (ctx.getContextAsInt( Env.WINDOW_INFO, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
			&& ctx.getContextAsInt( Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
			mTab.setValue("M_AttributeSetInstance_ID", Integer.valueOf(ctx.getContextAsInt( Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID")));
		else
			mTab.setValue("M_AttributeSetInstance_ID", null);
		**/
		int C_BPartner_ID = ctx.getContextAsInt( WindowNo, WindowNo, "C_BPartner_ID");
		BigDecimal Qty = (BigDecimal)mTab.getValue("Qty");
		boolean isSOTrx = false;
		MProductPricing pp = new MProductPricing (ctx.getAD_Client_ID(), ctx.getAD_Org_ID(),
			M_Product_ID.intValue(), C_BPartner_ID, Qty, isSOTrx);
		//
		int M_PriceList_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_ID");
		pp.setM_PriceList_ID(M_PriceList_ID);
		int M_PriceList_Version_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_Version_ID");
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
		Timestamp orderDate = (Timestamp)mTab.getValue("DateRequired");
		pp.setPriceDate(orderDate);
		//
		mTab.setValue("PriceActual", pp.getPriceStd());
		ctx.setContext( WindowNo, "EnforcePriceLimit", pp.isEnforcePriceLimit() ? "Y" : "N");	//	not used
		ctx.setContext( WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");

	//	setCalloutActive(false);
		return "";
	}	//	product

	/**
	 *	Order Line - Amount.
	 *		- called from Qty, PriceActual
	 *		- calculates LineNetAmt
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String amt (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		//	Qty changed - recalc price
		if (mField.getColumnName().equals("Qty")
			&& "Y".equals(ctx.getContext( WindowNo, "DiscountSchema")))
		{
			int M_Product_ID = ctx.getContextAsInt( WindowNo, WindowNo, "M_Product_ID");
			int C_BPartner_ID = ctx.getContextAsInt( WindowNo, WindowNo, "C_BPartner_ID");
			BigDecimal Qty = (BigDecimal)value;
			boolean isSOTrx = false;
			MProductPricing pp = new MProductPricing (ctx.getAD_Client_ID(), ctx.getAD_Org_ID(),
				M_Product_ID, C_BPartner_ID, Qty, isSOTrx);
			//
			int M_PriceList_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_ID");
			pp.setM_PriceList_ID(M_PriceList_ID);
			int M_PriceList_Version_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_Version_ID");
			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
			Timestamp orderDate = (Timestamp)mTab.getValue("DateInvoiced");
			pp.setPriceDate(orderDate);
			//
			mTab.setValue("PriceActual", pp.getPriceStd());
		}

		int StdPrecision = ctx.getStdPrecision();
		BigDecimal Qty = (BigDecimal)mTab.getValue("Qty");
		BigDecimal PriceActual = (BigDecimal)mTab.getValue("PriceActual");

		//	get values
		log.fine("amt - Qty=" + Qty + ", Price=" + PriceActual + ", Precision=" + StdPrecision);

		//	Multiply
		BigDecimal LineNetAmt = Qty.multiply(PriceActual);
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		mTab.setValue("LineNetAmt", LineNetAmt);
		log.info("amt - LineNetAmt=" + LineNetAmt);
		//
		setCalloutActive(false);
		return "";
	}	//	amt


}	//	CalloutRequisition
