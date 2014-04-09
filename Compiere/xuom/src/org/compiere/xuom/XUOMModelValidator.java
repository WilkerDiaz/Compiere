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

import java.util.*;

import org.compiere.api.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	XUOM Model validator
 *	@author Jorg Janke
 */
public class XUOMModelValidator implements ModelValidator
{
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(XUOMModelValidator.class);
	/** Client			*/
	private int			m_AD_Client_ID;

	/**
	 * 	Initialize Validation
	 *	@return AD_Client_ID or 0 for ALL
	 * 	@param engine validation engine
	 */
	public void initialize (int AD_Client_ID, ModelValidationEngine engine)
	{
		log.info(toString());
		m_AD_Client_ID = AD_Client_ID;

		//	We want to be informed when M_Product is created/changed
		engine.addModelChange("M_Product", this);
		engine.addDocValidate("C_Order", this);
	}	//	initialize

	/**
	 * 	Get Client to be monitored
	 *	@return AD_Client_ID or 0 for ALL
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID

	/**
	 * 	User logged in
	 * 	Called before preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		return null;
	}	//	login


    /**
     * 	Model Change of a monitored Table.
     * 	Called after PO.beforeSave/PO.beforeDelete
     * 	when you called addModelChange for the table
     * 	@param po persistent object
     * 	@param changeType CHANGETYPE_
     *	@return error message or null
     *	@exception Exception if the recipient wishes the change to be not accept.
     */
	public String modelChange (PO po, int changeType) throws Exception
	{
		if (po.get_TableName().equals("M_Product"))
		{
			MProduct product = (MProduct)po;
			if (po.is_ValueChanged("C_UOMGroup_ID")		//	Group Changed
				|| po.is_ValueChanged("C_UOM_ID"))		//	UOM Changed
			{
				int C_UOMGroup_ID = product.get_ValueAsInt("C_UOMGroup_ID");	//	get Value
				if (C_UOMGroup_ID > 0)
				{
					MUOMGroup group = new MUOMGroup(product.getCtx(), C_UOMGroup_ID, null);
					if (group.getC_UOMGroup_ID() == C_UOMGroup_ID)
					{
						log.info(product.getName() + ": UOMGroup=" + group.getName());
						group.updateProduct(product, false);	//	no sync
					}
				}
			}
		}
		return null;
	}	//	modelChange


	/**
	 * 	Validate Document.
	 * 	Called as first step of DocAction.prepareIt
	 * 	or at the end of DocAction.completeIt
     * 	when you called addDocValidate for the table.
     * 	Note that totals, etc. may not be correct before the prepare stage.
	 *	@param po persistent object
	 *	@param docTiming see DOCTIMING_ constants
     *	@return error message or null -
     *	if not null, the document will be marked as Invalid.
	 */
	public String docValidate (PO po, int docTiming)
	{
		return null;
	}	//	docValidate


	/**
	 * 	Update Info Window Columns.
	 * 	- add new Columns
	 * 	- remove columns
	 * 	- change display sequence
	 *	@param columns array of columns
	 *	@param sqlFrom from clause, can be modified
	 *	@param sqlOrder order by clause, can me modified
	 *	@return true if you updated columns, sequence or sql From clause
	 */
	public boolean updateInfoColumns (ArrayList<Info_Column> columns,
		StringBuffer sqlFrom, StringBuffer sqlOrder)
	{
		return false;
	}	//	updateInfoColumns

	/**
     * 	String Representation
     *	@return info
     */
    @Override
    public String toString()
    {
	    StringBuffer sb = new StringBuffer("XUOMModelValidator[")
	    	.append("AD_Client_ID=").append(m_AD_Client_ID)
	    	.append("]");
	    return sb.toString();
    }	//	toString

}	//	ModelValidation
