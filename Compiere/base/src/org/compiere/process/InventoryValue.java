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

import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *  Inventory Valuation.
 *  Process to fill T_InventoryValue
 *
 *  @author     Jorg Janke
 *  @version    $Id: InventoryValue.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class InventoryValue extends SvrProcess
{
	/** Price List Used         */
	private int         p_M_PriceList_Version_ID;
	/** Valuation Date          */
	private Timestamp   p_DateValue;
	/** Warehouse               */
	private int         p_M_Warehouse_ID;
	/** Currency                */
	private int         p_C_Currency_ID;
	/** Optional Cost Element	*/
	private int			p_M_CostElement_ID;

	/**
	 *  Prepare - get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("M_PriceList_Version_ID"))
				p_M_PriceList_Version_ID = element.getParameterAsInt();
			else if (name.equals("DateValue"))
				p_DateValue = (Timestamp)element.getParameter();
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = element.getParameterAsInt();
			else if (name.equals("C_Currency_ID"))
				p_C_Currency_ID = element.getParameterAsInt();
			else if (name.equals("M_CostElement_ID"))
				p_M_CostElement_ID = element.getParameterAsInt();
			else if (name.equals("#AD_PrintFormat_ID"))
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (p_DateValue == null)
			p_DateValue = new Timestamp (System.currentTimeMillis());
	}   //  prepare

	/**
	 *  Perrform process.
	 *  <pre>
	 *  - Fill Table with QtyOnHand for Warehouse and Valuation Date
	 *  - Perform Price Calculations
	 *  </pre>
	 * @return Message
	 * @throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("M_Warehouse_ID=" + p_M_Warehouse_ID
			+ ",C_Currency_ID=" + p_C_Currency_ID
			+ ",DateValue=" + p_DateValue
			+ ",M_PriceList_Version_ID=" + p_M_PriceList_Version_ID
			+ ",M_CostElement_ID=" + p_M_CostElement_ID);
		
		MWarehouse wh = MWarehouse.get(getCtx(), p_M_Warehouse_ID);
		MClient c = MClient.get(getCtx(), wh.getAD_Client_ID());
		MAcctSchema as = c.getAcctSchema();
		
		//  Delete (just to be sure)
		StringBuffer sql = new StringBuffer ("DELETE FROM T_InventoryValue WHERE AD_PInstance_ID= ? ");
		int no = DB.executeUpdate(get_TrxName(), sql.toString(),getAD_PInstance_ID());

		//	Insert Standard Costs
		sql = new StringBuffer ("INSERT INTO T_InventoryValue "
			+ "(AD_PInstance_ID, M_Warehouse_ID, M_Product_ID, M_AttributeSetInstance_ID,"
			+ " AD_Client_ID, AD_Org_ID, CostStandard) "
			+ "SELECT ? , w.M_Warehouse_ID, c.M_Product_ID, c.M_AttributeSetInstance_ID,"
			+ " w.AD_Client_ID, w.AD_Org_ID, c.CurrentCostPrice "
			+ "FROM M_Warehouse w"
			+ " INNER JOIN AD_ClientInfo ci ON (w.AD_Client_ID=ci.AD_Client_ID)"
			+ " INNER JOIN C_AcctSchema acs ON (ci.C_AcctSchema1_ID=acs.C_AcctSchema_ID)"
			+ " INNER JOIN M_Cost c ON (acs.C_AcctSchema_ID=c.C_AcctSchema_ID AND acs.M_CostType_ID=c.M_CostType_ID AND c.AD_Org_ID IN (0, w.AD_Org_ID))"
			+ " INNER JOIN M_CostElement ce ON (c.M_CostElement_ID=ce.M_CostElement_ID AND ce.CostingMethod='S' AND ce.CostElementType='M') "
			+ "WHERE w.M_Warehouse_ID= ? ");
		int noInsertStd = DB.executeUpdate(get_TrxName(), sql.toString(),getAD_PInstance_ID(),p_M_Warehouse_ID);
		log.fine("Inserted Std=" + noInsertStd);
		if (noInsertStd == 0)
			return "No Standard Costs found";

		//	Insert addl Costs
		int noInsertCost = 0;
		if (p_M_CostElement_ID != 0)
		{
			sql = new StringBuffer ("INSERT INTO T_InventoryValue "
				+ "(AD_PInstance_ID, M_Warehouse_ID, M_Product_ID, M_AttributeSetInstance_ID,"
				+ " AD_Client_ID, AD_Org_ID, CostStandard, Cost, M_CostElement_ID) "
				+ " SELECT ?, w.M_Warehouse_ID, c.M_Product_ID, c.M_AttributeSetInstance_ID,"
				+ " w.AD_Client_ID, w.AD_Org_ID, 0, c.CurrentCostPrice, c.M_CostElement_ID "
				+ " FROM M_Warehouse w"
				+ " INNER JOIN AD_ClientInfo ci ON (w.AD_Client_ID=ci.AD_Client_ID)"
				+ " INNER JOIN C_AcctSchema acs ON (ci.C_AcctSchema1_ID=acs.C_AcctSchema_ID)"
				+ " INNER JOIN M_Cost c ON (acs.C_AcctSchema_ID=c.C_AcctSchema_ID AND acs.M_CostType_ID=c.M_CostType_ID AND c.AD_Org_ID IN (0, w.AD_Org_ID)) "
				+ " WHERE w.M_Warehouse_ID= ? " 
				+ " AND c.M_CostElement_ID= ? "
				+ " AND NOT EXISTS (SELECT 1 "
				                + " FROM T_InventoryValue iv "
					            + " WHERE iv.AD_PInstance_ID= ? "
					            + " AND iv.M_Warehouse_ID=w.M_Warehouse_ID"
					            + " AND iv.M_Product_ID=c.M_Product_ID"
					            + " AND iv.M_AttributeSetInstance_ID=c.M_AttributeSetInstance_ID)");
			noInsertCost = DB.executeUpdate(get_TrxName(), sql.toString(),
					getAD_PInstance_ID(),p_M_Warehouse_ID,p_M_CostElement_ID,getAD_PInstance_ID());
			log.fine("Inserted Cost=" + noInsertCost);
			
			//	Update Std Cost Records
			sql = new StringBuffer 
			         (" UPDATE T_InventoryValue iv "
				    + " SET Cost= (SELECT c.CurrentCostPrice "
 					           + " FROM M_Warehouse w "
					           + " INNER JOIN AD_ClientInfo ci ON (w.AD_Client_ID=ci.AD_Client_ID) "
					           + " INNER JOIN C_AcctSchema acs ON (ci.C_AcctSchema1_ID=acs.C_AcctSchema_ID) "
					           + " INNER JOIN M_Cost c ON (acs.C_AcctSchema_ID=c.C_AcctSchema_ID "
						                               + " AND acs.M_CostType_ID=c.M_CostType_ID "
						                               + " AND c.AD_Org_ID IN (0, w.AD_Org_ID)) "
					           + " WHERE c.M_CostElement_ID= ? " 
					           + " AND iv.M_Warehouse_ID=w.M_Warehouse_ID"
					           + " AND iv.M_Product_ID=c.M_Product_ID"
					           + " AND iv.M_AttributeSetInstance_ID=c.M_AttributeSetInstance_ID), "
					+ " M_CostElement_ID = ? "           
				    + " WHERE EXISTS (SELECT 1 "
				                  + " FROM T_InventoryValue ivv "
					              + " WHERE ivv.AD_PInstance_ID= ? "
					              + " AND ivv.M_CostElement_ID IS NULL)");
			int noUpdatedCost = DB.executeUpdate(get_TrxName(), sql.toString(),
					p_M_CostElement_ID,p_M_CostElement_ID,getAD_PInstance_ID());
			log.fine("Updated Cost=" + noUpdatedCost);
		}		
		if ((noInsertStd+noInsertCost) == 0)
			return "No Costs found";
		
		//  Update Constants
		//  YYYY-MM-DD HH24:MI:SS.mmmm  JDBC Timestamp format
		String myDate = p_DateValue.toString();
		sql = new StringBuffer ("UPDATE T_InventoryValue SET ")
			.append("DateValue=TO_DATE('").append(myDate.substring(0,10))
			.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS'),")
			.append("M_PriceList_Version_ID= ? ,")
			.append("C_Currency_ID= ? ");
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_M_PriceList_Version_ID,p_C_Currency_ID);
		log.fine("Constants=" + no);

		//  Get current QtyOnHand with ASI
		sql = new StringBuffer ("UPDATE T_InventoryValue iv SET QtyOnHand = "
				+ "(SELECT SUM(Qty) FROM M_StorageDetail s"
				+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID) "
				+ "WHERE iv.M_Product_ID=s.M_Product_ID AND s.QtyType='H' "
				+ " AND iv.M_Warehouse_ID=l.M_Warehouse_ID"
				+ " AND iv.M_AttributeSetInstance_ID=s.M_AttributeSetInstance_ID) "
			+ "WHERE AD_PInstance_ID= ? AND iv.M_AttributeSetInstance_ID<>0");
		no = DB.executeUpdate(get_TrxName(), sql.toString(),getAD_PInstance_ID());
		log.fine("QtHand with ASI=" + no);
		//  Get current QtyOnHand without ASI
		sql = new StringBuffer ("UPDATE T_InventoryValue iv SET QtyOnHand = "
				+ "(SELECT SUM(Qty) FROM M_StorageDetail s"
				+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID) "
				+ "WHERE iv.M_Product_ID=s.M_Product_ID AND s.QtyType='H' "
				+ " AND iv.M_Warehouse_ID=l.M_Warehouse_ID) "
			+ "WHERE AD_PInstance_ID= ? AND iv.M_AttributeSetInstance_ID=0");
		no = DB.executeUpdate(get_TrxName(), sql.toString(),getAD_PInstance_ID());
		log.fine("QtHand w/o ASI=" + no);
		
		//  Adjust for Valuation Date
		sql = new StringBuffer("UPDATE T_InventoryValue iv "
			+ "SET QtyOnHand="
				+ "(SELECT iv.QtyOnHand - NVL(SUM(t.MovementQty), 0) "
				+ "FROM M_Transaction t"
				+ " INNER JOIN M_Locator l ON (t.M_Locator_ID=l.M_Locator_ID) "
				+ "WHERE t.M_Product_ID=iv.M_Product_ID"
				+ " AND t.M_AttributeSetInstance_ID=iv.M_AttributeSetInstance_ID"
				+ " AND t.MovementDate > iv.DateValue"
				+ " AND l.M_Warehouse_ID=iv.M_Warehouse_ID) "
			+ "WHERE iv.M_AttributeSetInstance_ID<>0");
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.fine("Update with ASI=" + no);
		//
		sql = new StringBuffer("UPDATE T_InventoryValue iv "
			+ "SET QtyOnHand="
				+ "(SELECT iv.QtyOnHand - NVL(SUM(t.MovementQty), 0) "
				+ "FROM M_Transaction t"
				+ " INNER JOIN M_Locator l ON (t.M_Locator_ID=l.M_Locator_ID) "
				+ "WHERE t.M_Product_ID=iv.M_Product_ID"
				+ " AND t.MovementDate > iv.DateValue"
				+ " AND l.M_Warehouse_ID=iv.M_Warehouse_ID) "
			+ "WHERE iv.M_AttributeSetInstance_ID=0");
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.fine("Update w/o ASI=" + no);
		
		//  Delete Records w/o OnHand Qty
		sql = new StringBuffer("DELETE FROM T_InventoryValue "
			+ "WHERE (QtyOnHand=0 OR QtyOnHand IS NULL) AND AD_PInstance_ID= ? ");
		int noQty = DB.executeUpdate (get_TrxName(), sql.toString(),getAD_PInstance_ID());
		log.fine("NoQty Deleted=" + noQty);

		//  Update Prices
		no = DB.executeUpdate (get_TrxName()
				, "UPDATE T_InventoryValue iv "
					+ "SET PricePO = "
						+ "(SELECT MAX(currencyConvert (po.PriceList,po.C_Currency_ID,iv.C_Currency_ID,iv.DateValue,null, po.AD_Client_ID,po.AD_Org_ID))"
						+ " FROM M_Product_PO po WHERE po.M_Product_ID=iv.M_Product_ID"
						+ " AND po.IsCurrentVendor='Y'), "
					+ "PriceList = "
						+ "(SELECT currencyConvert(pp.PriceList,pl.C_Currency_ID,iv.C_Currency_ID,iv.DateValue,null, pl.AD_Client_ID,pl.AD_Org_ID)"
						+ " FROM M_PriceList pl, M_PriceList_Version plv, M_ProductPrice pp"
						+ " WHERE pp.M_Product_ID=iv.M_Product_ID AND pp.M_PriceList_Version_ID=iv.M_PriceList_Version_ID"
						+ " AND pp.M_PriceList_Version_ID=plv.M_PriceList_Version_ID"
						+ " AND plv.M_PriceList_ID=pl.M_PriceList_ID), "
					+ "PriceStd = "
						+ "(SELECT currencyConvert(pp.PriceStd,pl.C_Currency_ID,iv.C_Currency_ID,iv.DateValue,null, pl.AD_Client_ID,pl.AD_Org_ID)"
						+ " FROM M_PriceList pl, M_PriceList_Version plv, M_ProductPrice pp"
						+ " WHERE pp.M_Product_ID=iv.M_Product_ID AND pp.M_PriceList_Version_ID=iv.M_PriceList_Version_ID"
						+ " AND pp.M_PriceList_Version_ID=plv.M_PriceList_Version_ID"
						+ " AND plv.M_PriceList_ID=pl.M_PriceList_ID), "
					+ "PriceLimit = "
						+ "(SELECT currencyConvert(pp.PriceLimit,pl.C_Currency_ID,iv.C_Currency_ID,iv.DateValue,null, pl.AD_Client_ID,pl.AD_Org_ID)"
						+ " FROM M_PriceList pl, M_PriceList_Version plv, M_ProductPrice pp"
						+ " WHERE pp.M_Product_ID=iv.M_Product_ID AND pp.M_PriceList_Version_ID=iv.M_PriceList_Version_ID"
						+ " AND pp.M_PriceList_Version_ID=plv.M_PriceList_Version_ID"
						+ " AND plv.M_PriceList_ID=pl.M_PriceList_ID)");
		String msg = "";
		if (no == 0)
			msg = "No Prices";

		//	Convert if different Currency
		if (as.getC_Currency_ID() != p_C_Currency_ID)
		{
			sql = new StringBuffer 
			     (" UPDATE T_InventoryValue iv "
				+ " SET CostStandard= (SELECT currencyConvert(iv.CostStandard,acs.C_Currency_ID, "
				                                          + " iv.C_Currency_ID,iv.DateValue,null, "
				                                          + " iv.AD_Client_ID,iv.AD_Org_ID) "
					               + " FROM C_AcctSchema acs " 
					               + " WHERE acs.C_AcctSchema_ID= ? ), "
				+ "	Cost= (SELECT currencyConvert(iv.Cost,acs.C_Currency_ID,iv.C_Currency_ID,iv.DateValue, "
				                              + " null, iv.AD_Client_ID,iv.AD_Org_ID) "
					   + " FROM C_AcctSchema acs "
					   + " WHERE acs.C_AcctSchema_ID= ? ) "
				+ " WHERE AD_PInstance_ID= ? ");
			no = DB.executeUpdate (get_TrxName(), sql.toString(),
					as.getC_AcctSchema_ID(),as.getC_AcctSchema_ID(),getAD_PInstance_ID());
			log.fine("Convered=" + no);
		}
		
		//  Update Values
		no = DB.executeUpdate(get_TrxName()
			, "UPDATE T_InventoryValue SET "
				+ "PricePOAmt = QtyOnHand * PricePO, "
				+ "PriceListAmt = QtyOnHand * PriceList, "
				+ "PriceStdAmt = QtyOnHand * PriceStd, "
				+ "PriceLimitAmt = QtyOnHand * PriceLimit, "
				+ "CostStandardAmt = QtyOnHand * CostStandard, "
				+ "CostAmt = QtyOnHand * Cost "
				+ "WHERE AD_PInstance_ID= ? ", getAD_PInstance_ID());
		log.fine("Calculation=" + no);
		//
		return msg;
	}   //  doIt

}   //  InventoryValue
