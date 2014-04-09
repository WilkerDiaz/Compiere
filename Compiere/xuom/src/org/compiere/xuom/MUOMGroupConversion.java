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

import java.math.*;
import java.sql.*;

import org.compiere.api.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	UOM Group Conversion Model
 *	@author Jorg Janke
 */
public class MUOMGroupConversion extends X_C_UOMGroupConversion 
{
    /** Logger for class MUOMGroupConversion */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MUOMGroupConversion.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_UOMGroupConversion_ID id
	 *	@param trx p_trx
	 */
	public MUOMGroupConversion(Ctx ctx, int C_UOMGroupConversion_ID,
			Trx trx) 
	{
		super(ctx, C_UOMGroupConversion_ID, trx);
	}	//	MUOMGroupConversion

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MUOMGroupConversion(Ctx ctx, ResultSet rs, Trx trx) 
	{
		super(ctx, rs, trx);
	}	//	MUOMGroupConversion


	/**
	 * 	Callout
	 *	@param MultiplyRateOld old value
	 *	@param MultiplyRateNew new value
	 *	@param windowNo windowNo
	 */
	@UICallout public void setMultiplyRate (String MultiplyRateOld, 
			String MultiplyRateNew, int windowNo) throws Exception
	{
		setMultiplyRate(convertToBigDecimal(MultiplyRateNew));
	}	//	setMultiplyRate

	/**
	 * 	Set Multiply Rate
	 * 	Sets also Divide Rate
	 *	@param MultiplyRate multiply rate
	 */
	@Override
	public void setMultiplyRate (BigDecimal MultiplyRate)
	{
		if (MultiplyRate == null 
			|| MultiplyRate.signum() == 0 
			|| MultiplyRate.compareTo(Env.ONE) == 0)
		{
			super.setDivideRate(Env.ONE);
			super.setMultiplyRate(Env.ONE);
		}
		else
		{
			super.setMultiplyRate(MultiplyRate);
			double dd = 1 / MultiplyRate.doubleValue();
			super.setDivideRate(new BigDecimal(dd));
		}
	}	//	setMultiplyRate

	/**
	 * 	Callout
	 *	@param DivideRateOld old value
	 *	@param DivideRateNew new value
	 *	@param windowNo window no
	 */
	@UICallout public void setDivideRate (String DivideRateOld, 
			String DivideRateNew, int WindowNo) throws Exception
	{
		setDivideRate(convertToBigDecimal(DivideRateNew));
	}	//	setDivideRate

	/**
	 *	Set Divide Rate.
	 *	Sets also Multiply Rate
	 *	@param	DivideRate divide rate
	 */
	@Override
	public void setDivideRate (BigDecimal DivideRate)
	{
		if (DivideRate == null 
			|| DivideRate.signum() == 0 
			|| DivideRate.compareTo(Env.ONE) == 0)
		{
			super.setDivideRate(Env.ONE);
			super.setMultiplyRate(Env.ONE);
		}
		else
		{
			super.setDivideRate(DivideRate);
			double dd = 1 / DivideRate.doubleValue();
			super.setMultiplyRate(new BigDecimal(dd));
		}
	}	//	setDivideRate
	
	/**
	 * 	After Save - Update Product Conversions
	 */
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) 
	{
		if (!success || !newRecord)
			return success;

		//	Update Products with UOM Group - add Conversion  
		int count = 0;
		MProduct[] products = MUOMGroup.getProducts(getCtx(), getC_UOMGroup_ID());
		for (MProduct product : products) {
			MUOMConversion productConv = new MUOMConversion(product);
			productConv.setC_UOM_To_ID (getC_UOM_ID());
			productConv.setMultiplyRate(getMultiplyRate());
			productConv.setDivideRate(getDivideRate());
			if (productConv.save())
				count++;
		}

		log.info("Added #" + count);
		return true;
	}	//	afterSave
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MUOMGroupConversion[").append(
			get_ID()).append(",C_UOM_ID=").append(getC_UOM_ID());
		sb.append("]");
		return sb.toString();
	}	//	toString
	
}	//	MUOMGroupConversion
