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
package compiere.model;

import java.math.*;
import java.util.*;

import org.compiere.api.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Validator Example Implementation
 *
 *	@author Jorg Janke
 *	@version $Id: MyValidator.java,v 1.2 2006/07/30 00:51:57 jjanke Exp $
 */
public class MyValidator implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instanciated when logging in and client is selected/known
	 */
	public MyValidator ()
	{
		super();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(MyValidator.class);
	/** Client			*/
	private int		m_AD_Client_ID = 11;


	/**
	 *	Initialize Validation
	 *	@return AD_Client_ID or 0 for ALL
	 *	@param engine validation engine
	 */
	public void initialize (int AD_Client_ID, ModelValidationEngine engine)
	{
		log.info(toString());
		assert AD_Client_ID == 11 : "AD_Client_ID=" + AD_Client_ID;
		m_AD_Client_ID = AD_Client_ID;

		//	We want to be informed when C_Order is created/changed
		engine.addModelChange(X_C_Order.Table_Name, this);
		//	We want to validate Order before preparing
		engine.addDocValidate(X_C_Order.Table_Name, this);
	}	//	initialize

	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID or 0 for ALL
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID

	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);
		return null;
	}	//	login

    /**
     *	Model Change of a monitored Table.
     *	Called after PO.beforeSave/PO.beforeDelete
     *	when you called addModelChange for the table
     *	@param po persistent object
     *	@param type TYPE_
     *	@return error message or null
     *	@exception Exception if the recipient wishes the change to be not accept.
     */
	public String modelChange (PO po, int type) throws Exception
	{
		if (po.get_TableName().equals("C_Order") && type == CHANGETYPE_CHANGE)
		{
			MOrder order = (MOrder)po;
			log.info(order.toString());
		}
		return null;
	}	//	modelChange

	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.
     *	Note that totals, etc. may not be correct.
	 *	@param po persistent object
	 *	@param timing see TIMING_ constants
     *	@return error message or null
	 */
	public String docValidate (PO po, int timing)
	{
		//	Gapless Document Sequence
		if (timing == DOCTIMING_AFTER_COMPLETE && po.get_TableName().equals(X_C_Order.Table_Name))
		{
		//	MOrder order = (MOrder)po;
		//	if (order.getDocStatus().equals(MOrder.DOCSTATUS_Completed))
		//		order.setDocumentNo(getGaplessSeqNo());
			return null;
		}
		//	TIMING_BEFORE_PREPARE
		if (po.get_TableName().equals(X_C_Order.Table_Name))
		{
			/**	Order Discount Example	*/
			if (false)
			{
				MOrder order = (MOrder)po;
				String error = orderDiscount(order);
				if (error != null)
					return error;
			}
			log.info(po.toString());
		}
		return null;
	}	//	docValidate

	/**
	 * 	Order Discount.
	 * 	Make sure that last line is discount and check correctness
	 *	@param order order
	 *	@return error message or null
	 */
	private String orderDiscount (MOrder order)
	{
		String DISCOUNT = "Discount";
		int C_Tax_ID = 0;
		BigDecimal totalLines = Env.ZERO;
		MOrderLine discountLine = null;
		//
		MOrderLine[] lines = order.getLines();
		for (MOrderLine oLine : lines) {
			String description = oLine.getDescription();
			if (description != null && description.equals(DISCOUNT))
				discountLine = oLine;
			else
			{
				totalLines = totalLines.add(oLine.getLineNetAmt());
				if (C_Tax_ID == 0)
					C_Tax_ID = oLine.getC_Tax_ID();
				else if (C_Tax_ID != oLine.getC_Tax_ID())
					return "Order has more then one Tax, cannot add discount";
			}
		}
		if (discountLine == null)
		{
			discountLine = new MOrderLine(order);
			discountLine.setDescription(DISCOUNT);
			discountLine.setQty(Env.ONE);
			discountLine.setC_Tax_ID(C_Tax_ID);
			discountLine.setLine(9999);
		}
		//	Calculate Discount
		BigDecimal discountPercent = new BigDecimal(3);	//	3% example
		BigDecimal discountAmt = totalLines.multiply(discountPercent);
		discountAmt = discountAmt.divide(Env.ONEHUNDRED, order.getPrecision(), BigDecimal.ROUND_HALF_UP);
		discountLine.setPrice(discountAmt.negate());
		if (!discountLine.save() || !discountLine.updateHeaderTax())
			return "Could not save discount line";

		log.info(discountLine.toString());
		order.getLines();
		return null;
	}	//	orderDiscount


	/**
	 * 	Update Info Window Columns.
	 * 	- add new Columns
	 * 	- remove columns
	 * 	- change dispay sequence
	 *	@param columns array of columns
	 *	@param sqlFrom from clause, can be modified
	 *	@param sqlOrder order by clause, can me modified
	 *	@return true if you updated columns, sequence or sql From clause
	 */
	public boolean updateInfoColumns (ArrayList<Info_Column> columns,
		StringBuffer sqlFrom, StringBuffer sqlOrder)
	{
		/** You can hide columns
		Info_Column column = columns.get(1);
		column.setWidth(0);	//	hide
		**/
		/**	Product Info Example	*
	//	int AD_Role_ID = Env.getCtx().getAD_Role_ID();	// Can be Role/User specific
		String from = sqlFrom.toString();
		if (from.startsWith ("M_Product"))
		{
			columns.add (new Info_Column("Header", "'sql'", String.class).seq(35));
			return true;
		}
		/**		*/
		return false;
	}	//	updateInfoColumns

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MyValidator[Order@Gardenworld");
		sb.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MyValidator
