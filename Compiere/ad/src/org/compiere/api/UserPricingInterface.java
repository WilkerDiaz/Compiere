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
package org.compiere.api;

import java.math.*;
import java.sql.*;

/**
 * 	User Pricing Engine Interface.
 *	The class id defined on Tenant level in the info tab 
 *  @author Jorg Janke
 *  @version $Id: UserPricingInterface.java 8244 2009-12-04 23:25:29Z freyes $
 */
public interface UserPricingInterface
{
	/**
	 * 	User Pricing
	 * 	@param AD_Org_ID org of document
	 * 	@param isSOTrx sales order
	 *	@param M_PriceList_ID price list selected
	 *	@param C_BPartner_ID business partner
	 *	@param M_Product_ID product
	 *	@param Qty quantity quantity
	 *	@param PriceDate date for pricing
	 *	@return null if Compiere should price it or pricing info
	 */
	public UserPricingVO price (int AD_Org_ID, boolean isSOTrx,
		int M_PriceList_ID, int C_BPartner_ID, 
		int M_Product_ID, BigDecimal Qty, Timestamp PriceDate);
	
}	//	UserPricingInterface
