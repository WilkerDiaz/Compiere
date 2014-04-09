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
import java.util.logging.*;

import org.compiere.common.constants.*;
import org.compiere.util.*;

/**
 *	Physical Inventory Callouts
 *
 *  @author Jorg Janke
 *  @version $Id: CalloutInventory.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class CalloutInventory extends CalloutEngine
{
	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());

	/**
	 *  Product/Locator/ASI modified.
	 * 		Set Attribute Set Instance
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String product (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		//	overkill - see new implementation
		Integer InventoryLine = (Integer)mTab.getValue("M_InventoryLine_ID");
		BigDecimal bd = null;

		/**
		 * kviiksaar: SourceForge 1778282
		 *
		 * Modified for update Book Qty on existing records.
		 * Also checks the old ASI and removes it if product has been change.
		 */
		if (InventoryLine != null && InventoryLine.intValue() != 0) {
			MInventoryLine _ILine = new MInventoryLine(ctx, InventoryLine, null);
			Integer M_Product_ID = (Integer)mTab.getValue("M_Product_ID");
			Integer M_Locator_ID = (Integer)mTab.getValue("M_Locator_ID");
			Integer M_AttributeSetInstance_ID = 0;
			// if product or locator has changed recalculate Book Qty
			if (M_Product_ID != null && M_Product_ID != _ILine.getM_Product_ID() ||
					M_Locator_ID !=null && M_Locator_ID != _ILine.getM_Locator_ID()) {
				setCalloutActive(true);
				// Check ASI - if product has been changed remove old ASI
				if (M_Product_ID == _ILine.getM_Product_ID()) {
					M_AttributeSetInstance_ID = (Integer)mTab.getValue("M_AttributeSetInstance_ID");
					if( M_AttributeSetInstance_ID == null )
						M_AttributeSetInstance_ID = 0;
				} else {
					mTab.setValue("M_AttributeSetInstance_ID", null);
				}
				try {
					bd = setQtyBook(M_AttributeSetInstance_ID, M_Product_ID, M_Locator_ID);
					mTab.setValue("QtyBook", bd);
				} catch (Exception e) {
					return mTab.setValue("QtyBook", bd);
				}
			}
			setCalloutActive(false);
			return "";
		}

		//	New Line - Get Book Value
		int M_Product_ID = 0;
		Integer Product = (Integer)mTab.getValue("M_Product_ID");
		if (Product != null)
			M_Product_ID = Product.intValue();
		if (M_Product_ID == 0)
			return "";
		int M_Locator_ID = 0;
		Integer Locator = (Integer)mTab.getValue("M_Locator_ID");
		if (Locator != null)
			M_Locator_ID = Locator.intValue();
		if (M_Locator_ID == 0)
			return "";

		setCalloutActive(true);
		//	Set Attribute
		int M_AttributeSetInstance_ID = 0;
		Integer ASI = (Integer)mTab.getValue("M_AttributeSetInstance_ID");
		if (ASI != null)
			M_AttributeSetInstance_ID = ASI.intValue();
		//	Product Selection
		if (ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Product_ID") == M_Product_ID)
		{
			M_AttributeSetInstance_ID = ctx.getContextAsInt( EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID");
			if (M_AttributeSetInstance_ID != 0)
				mTab.setValue("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
			else
				mTab.setValue("M_AttributeSetInstance_ID", null);
		}

		// kviiksaar: Call's now the extracted function
		try {
			bd = setQtyBook(M_AttributeSetInstance_ID, M_Product_ID, M_Locator_ID);
			mTab.setValue("QtyBook", bd);
		} catch (Exception e) {
			return mTab.setValue("QtyBook", bd);
		}

		//
		log.info("M_Product_ID=" + M_Product_ID
				+ ", M_Locator_ID=" + M_Locator_ID
				+ ", M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID
				+ " - QtyBook=" + bd);
		setCalloutActive(false);
		return "";
	}   //  product

	/**
	 * kviiksaar
	 *
	 * Returns the current Book Qty for given parameters or 0
	 *
	 * @param M_AttributeSetInstance_ID
	 * @param M_Product_ID
	 * @param M_Locator_ID
	 * @return
	 * @throws Exception
	 */
	private BigDecimal setQtyBook (int M_AttributeSetInstance_ID, int M_Product_ID, int M_Locator_ID) throws Exception {
		// Set QtyBook from first storage location
		BigDecimal bd = null;
		String sql = "SELECT Qty FROM M_StorageDetail "
			+ "WHERE QtyType='H'"		
			+ " AND M_Product_ID=?"		//	1
			+ " AND M_Locator_ID=?"		//	2
			+ " AND M_AttributeSetInstance_ID=?";
		if (M_AttributeSetInstance_ID == 0)
			sql = "SELECT SUM(Qty) FROM M_StorageDetail "
				+ "WHERE QtyType='H'"		
				+ " AND M_Product_ID=?"		//	1
				+ " AND M_Locator_ID=?";	//	2

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{	
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setInt(2, M_Locator_ID);
			if (M_AttributeSetInstance_ID != 0)
				pstmt.setInt(3, M_AttributeSetInstance_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				bd = rs.getBigDecimal(1);
			else {
				// gwu: 1719401: clear Booked Quantity to zero first in case the query returns no rows,
				// for example when the locator has never stored a particular product.
				bd = Env.ZERO;
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			setCalloutActive(false);
			throw new Exception(e.getLocalizedMessage());
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if(bd!=null)
			return bd;

		return Env.ZERO;
	}
}	//	setQtyBook
