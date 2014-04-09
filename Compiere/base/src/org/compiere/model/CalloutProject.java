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
 *	Project Callouts
 *
 *  @author Jorg Janke
 *  @version $Id: CalloutProject.java,v 1.3 2006/07/30 00:51:04 jjanke Exp $
 */
public class CalloutProject extends CalloutEngine
{
	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());

	/**
	 *	Project Line Planned - Price + Qty.
	 *		- called from PlannedPrice, PlannedQty, PriceList, Discount
	 *		- calculates PlannedAmt
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public  String planned (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		BigDecimal PlannedQty, PlannedPrice, PriceList, Discount;
		int StdPrecision = ctx.getStdPrecision();

		//	get values
		PlannedQty = (BigDecimal)mTab.getValue("PlannedQty");
		if (PlannedQty == null)
			PlannedQty = Env.ONE;
		PlannedPrice = (BigDecimal)mTab.getValue("PlannedPrice");
		if (PlannedPrice == null)
			PlannedPrice = Env.ZERO;
		PriceList = (BigDecimal)mTab.getValue("PriceList");
		if (PriceList == null)
			PriceList = PlannedPrice;
		Discount = (BigDecimal)mTab.getValue("Discount");
		if (Discount == null)
			Discount = Env.ZERO;

		String columnName = mField.getColumnName();
		if (columnName.equals("PlannedPrice"))
		{
			if (PriceList.signum() == 0)
				Discount = Env.ZERO;
			else
			{
				BigDecimal multiplier = PlannedPrice.multiply(Env.ONEHUNDRED)
					.divide(PriceList, StdPrecision, BigDecimal.ROUND_HALF_UP);
				Discount = Env.ONEHUNDRED.subtract(multiplier);
			}
			mTab.setValue("Discount", Discount);
			log.fine("PriceList=" + PriceList + " - Discount=" + Discount
				+ " -> [PlannedPrice=" + PlannedPrice + "] (Precision=" + StdPrecision+ ")");
		}
		else if (columnName.equals("PriceList"))
		{
			if (PriceList.signum() == 0)
				Discount = Env.ZERO;
			else
			{
				BigDecimal multiplier = PlannedPrice.multiply(Env.ONEHUNDRED)
					.divide(PriceList, StdPrecision, BigDecimal.ROUND_HALF_UP);
				Discount = Env.ONEHUNDRED.subtract(multiplier);
			}
			mTab.setValue("Discount", Discount);
			log.fine("[PriceList=" + PriceList + "] - Discount=" + Discount
				+ " -> PlannedPrice=" + PlannedPrice + " (Precision=" + StdPrecision+ ")");
		}
		else if (columnName.equals("Discount"))
		{
			BigDecimal multiplier = Discount.divide(Env.ONEHUNDRED, 10, BigDecimal.ROUND_HALF_UP);
			multiplier = Env.ONE.subtract(multiplier);
			//
			PlannedPrice = PriceList.multiply(multiplier);
			if (PlannedPrice.scale() > StdPrecision)
				PlannedPrice = PlannedPrice.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
			mTab.setValue("PlannedPrice", PlannedPrice);
			log.fine("PriceList=" + PriceList + " - [Discount=" + Discount
				+ "] -> PlannedPrice=" + PlannedPrice + " (Precision=" + StdPrecision+ ")");
		}

		//	Calculate Amount
		BigDecimal PlannedAmt = PlannedQty.multiply(PlannedPrice);
		if (PlannedAmt.scale() > StdPrecision)
			PlannedAmt = PlannedAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		//
		log.fine("PlannedQty=" + PlannedQty + " * PlannedPrice=" + PlannedPrice + " -> PlannedAmt=" + PlannedAmt + " (Precision=" + StdPrecision+ ")");
		mTab.setValue("PlannedAmt", PlannedAmt);
		setCalloutActive(false);
		return "";
	}	//	planned

	/**
	 *	Project Line Product
	 *		- called from Product
	 *		- calculates PlannedPrice, PriceList, Discount
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
		int M_PriceList_Version_ID = ctx.getContextAsInt( WindowNo, "M_PriceList_Version_ID");
		if (M_Product_ID == null || M_Product_ID.intValue() == 0
			|| M_PriceList_Version_ID == 0)
			return "";
		setCalloutActive(true);

		int C_BPartner_ID = ctx.getContextAsInt( WindowNo, "C_BPartner_ID");
		BigDecimal Qty = (BigDecimal)mTab.getValue("PlannedQty");
		boolean IsSOTrx = true;
		MProductPricing pp = new MProductPricing (ctx.getAD_Client_ID(), ctx.getAD_Org_ID(),
				M_Product_ID.intValue(), C_BPartner_ID, Qty, IsSOTrx);
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
		Timestamp date = (Timestamp)mTab.getValue("PlannedDate");
		if (date == null)
		{
			date = (Timestamp)mTab.getValue("DateContract");
			if (date == null)
			{
				date = (Timestamp)mTab.getValue("DateFinish");
				if (date == null)
					date = new Timestamp(System.currentTimeMillis());
			}
		}
		pp.setPriceDate(date);
		//
		BigDecimal PriceList = pp.getPriceList();
		mTab.setValue("PriceList", PriceList);
		BigDecimal PlannedPrice = pp.getPriceStd();
		mTab.setValue("PlannedPrice", PlannedPrice);
		BigDecimal Discount = pp.getDiscount();
		mTab.setValue("Discount", Discount);
		//
		int curPrecision = 2;
		BigDecimal PlannedAmt = pp.getLineAmt(curPrecision);
		mTab.setValue("PlannedAmt", PlannedAmt);
		//
		log.fine("PlannedQty=" + Qty + " * PlannedPrice=" + PlannedPrice + " -> PlannedAmt=" + PlannedAmt);
		return "";
	}	//	product

}	//	CalloutProject
