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
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Replenishment Report
 *	
 *  @author Jorg Janke
 *  @version $Id: ReplenishReport.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class ReplenishReport extends SvrProcess
{
	/** Warehouse				*/
	private int		p_M_Warehouse_ID = 0;
	/**	Optional BPartner		*/
	private int		p_C_BPartner_ID = 0;
	/** Create (POO)Purchse Order or (POR)Requisition or (MMM)Movements */
	private String	p_ReplenishmentCreate = null;
	/** Document Type			*/
	private int		p_C_DocType_ID = 0;
	/** Return Info				*/
	private String	m_info = "";
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = element.getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = element.getParameterAsInt();
			else if (name.equals("ReplenishmentCreate"))
				p_ReplenishmentCreate = (String)element.getParameter();
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = element.getParameterAsInt();
			else if (name.equals("#AD_PrintFormat_ID"))
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("M_Warehouse_ID=" + p_M_Warehouse_ID 
			+ ", C_BPartner_ID=" + p_C_BPartner_ID 
			+ " - ReplenishmentCreate=" + p_ReplenishmentCreate
			+ ", C_DocType_ID=" + p_C_DocType_ID);
		if (p_ReplenishmentCreate != null && p_C_DocType_ID == 0)
			throw new CompiereUserException("@FillMandatory@ @C_DocType_ID@");
		
		MWarehouse wh = MWarehouse.get(getCtx(), p_M_Warehouse_ID);
		if (wh.get_ID() == 0)  
			throw new CompiereSystemException("@FillMandatory@ @M_Warehouse_ID@");
		//
		prepareTable();
		fillTable(wh);
		//
		if (p_ReplenishmentCreate == null)
			return "OK";
		//
		MDocType dt = MDocType.get(getCtx(), p_C_DocType_ID);
		if (!dt.getDocBaseType().equals(p_ReplenishmentCreate))
			throw new CompiereSystemException("@C_DocType_ID@=" + dt.getName() + " <> " + p_ReplenishmentCreate);
		//
		if (p_ReplenishmentCreate.equals("POO"))
			createPO();
		else if (p_ReplenishmentCreate.equals("POR"))
			createRequisition();
		else if (p_ReplenishmentCreate.equals("MMM"))
			createMovements();
		return m_info;
	}	//	doIt

	/**
	 * 	Prepare/Check Replenishment Table
	 */
	private void prepareTable()
	{
		//	Level_Max must be >= Level_Max
		String sql = "UPDATE M_Replenish"
			+ " SET Level_Max = Level_Min "
			+ "WHERE Level_Max < Level_Min";
		int no = DB.executeUpdate(get_TrxName(), sql);
		if (no != 0)
			log.fine("Corrected Max_Level=" + no);
		
		//	Minimum Order should be 1
		sql = "UPDATE M_Product_PO"
			+ " SET Order_Min = 1 "
			+ "WHERE (Order_Min IS NULL OR Order_Min < 1) " 
			+ "AND  Discontinued = 'N'";
		no = DB.executeUpdate(get_TrxName(), sql);
		if (no != 0)
			log.fine("Corrected Order Min=" + no);
		
		//	Pack should be 1
		sql = "UPDATE M_Product_PO"
			+ " SET Order_Pack = 1 "
			+ "WHERE (Order_Pack IS NULL OR Order_Pack < 1) "
			+ "AND  Discontinued = 'N'";
		no = DB.executeUpdate(get_TrxName(), sql);
		if (no != 0)
			log.fine("Corrected Order Pack=" + no);

		//	Set Current Vendor where only one vendor
		sql = "UPDATE M_Product_PO p"
			+ " SET IsCurrentVendor='Y' "
			+ "WHERE IsCurrentVendor<>'Y'"
			+ " AND EXISTS (SELECT 1 FROM M_Product_PO pp "
				+ "WHERE p.M_Product_ID=pp.M_Product_ID "
				+ "GROUP BY pp.M_Product_ID "
				+ "HAVING COUNT(*) = 1) "
			+ "AND  Discontinued = 'N'";
		no = DB.executeUpdate(get_TrxName(), sql);
		if (no != 0)
			log.fine("Corrected CurrentVendor(Y)=" + no);

		//	More then one current vendor
		sql = "UPDATE M_Product_PO p"
			+ " SET IsCurrentVendor='N' "
			+ "WHERE IsCurrentVendor = 'Y'"
			+ " AND EXISTS (SELECT 1 FROM M_Product_PO pp "
				+ "WHERE p.M_Product_ID=pp.M_Product_ID AND pp.IsCurrentVendor='Y' "
				+ "GROUP BY pp.M_Product_ID "
				+ "HAVING COUNT(*) > 1) "
			+ "AND  Discontinued = 'N'";
		no = DB.executeUpdate(get_TrxName(), sql);
		if (no != 0)
			log.fine("Corrected CurrentVendor(N)=" + no);
		
		//	Just to be sure
		sql = "DELETE FROM T_Replenish WHERE AD_PInstance_ID= ?";
		no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no != 0)
			log.fine("Delete Existing Temp=" + no);
	}	//	prepareTable

	/**
	 * 	Fill Table
	 * 	@param wh warehouse
	 */
	private void fillTable (MWarehouse wh) throws Exception
	{
		String sql = "INSERT INTO T_Replenish "
			+ "(AD_PInstance_ID, M_Warehouse_ID, M_Product_ID, AD_Client_ID, AD_Org_ID,"
			+ " ReplenishType, Level_Min, Level_Max, QtyOnHand,QtyReserved,QtyOrdered,"
			+ " C_BPartner_ID, Order_Min, Order_Pack, QtyToOrder, ReplenishmentCreate) "
			+ "SELECT ? , r.M_Warehouse_ID, r.M_Product_ID, r.AD_Client_ID, r.AD_Org_ID,"
			+ " r.ReplenishType, r.Level_Min, r.Level_Max, 0,0,0,"
			+ " po.C_BPartner_ID, po.Order_Min, po.Order_Pack, 0, ? ";
		sql += " FROM M_Replenish r"
			+ " INNER JOIN M_Product_PO po ON (r.M_Product_ID=po.M_Product_ID) "
			+ " INNER JOIN M_Product mp ON (r.M_Product_ID=mp.M_Product_ID) "
			+ "WHERE po.IsCurrentVendor='Y'"	//	Only Current Vendor
			+ " AND r.ReplenishType<>'0'"
			+ " AND po.IsActive='Y' AND r.IsActive='Y' AND mp.IsActive='Y'"
			+ " AND  po.Discontinued = 'N'"
			+ " AND  mp.Discontinued = 'N'"
			+ " AND r.M_Warehouse_ID= ? ";
		if (p_C_BPartner_ID != 0)
			sql += " AND po.C_BPartner_ID= ? ";
		
		int no=0;
		if(p_C_BPartner_ID != 0)
			no = DB.executeUpdate(get_TrxName(), sql,
					getAD_PInstance_ID(),p_ReplenishmentCreate,p_M_Warehouse_ID,p_C_BPartner_ID);
		else
			no = DB.executeUpdate(get_TrxName(), sql,
					getAD_PInstance_ID(),p_ReplenishmentCreate,p_M_Warehouse_ID);
			
		log.finest(sql);
		log.fine("Insert (1) #" + no);
		
		if (p_C_BPartner_ID == 0)
		{
			sql = "INSERT INTO T_Replenish "
				+ "(AD_PInstance_ID, M_Warehouse_ID, M_Product_ID, AD_Client_ID, AD_Org_ID,"
				+ " ReplenishType, Level_Min, Level_Max,"
				+ " C_BPartner_ID, Order_Min, Order_Pack, QtyToOrder, ReplenishmentCreate) "
				+ "SELECT ? , r.M_Warehouse_ID, r.M_Product_ID, r.AD_Client_ID, r.AD_Org_ID,"
				+ " r.ReplenishType, r.Level_Min, r.Level_Max,"
				+ DB.NULL("I", Types.INTEGER) 
				+ " , 1, 1, 0, ? ";
			sql	+= " FROM M_Replenish r "
				+ "WHERE r.ReplenishType<>'0' AND r.IsActive='Y'"
				+ " AND r.M_Warehouse_ID= ? " 
				+ " AND NOT EXISTS (SELECT * FROM T_Replenish t "
					+ "WHERE r.M_Product_ID=t.M_Product_ID"
					+ " AND AD_PInstance_ID= ? )"
				+ " AND EXISTS (SELECT * FROM M_Product_PO po, M_Product mp "
				                + " WHERE po.M_Product_ID = r.M_Product_ID "
				                + " AND   mp.M_Product_ID = r.M_Product_ID "
				                + " AND   po.Discontinued = 'N' AND mp.Discontinued = 'N')";
			no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID(),
					p_ReplenishmentCreate,p_M_Warehouse_ID,getAD_PInstance_ID() );
			log.fine("Insert (BP) #" + no);
		}
		
		sql = "UPDATE T_Replenish t SET "
			+ "QtyOnHand = (SELECT COALESCE(SUM(Qty),0) FROM M_StorageDetail s, M_Locator l WHERE t.M_Product_ID=s.M_Product_ID"
			    + " AND s.QtyType='H' "
				+ " AND l.M_Locator_ID=s.M_Locator_ID AND l.M_Warehouse_ID=t.M_Warehouse_ID),"
			+ "QtyReserved = (SELECT COALESCE(SUM(Qty),0) FROM M_StorageDetail s, M_Locator l WHERE t.M_Product_ID=s.M_Product_ID"
				+ " AND s.QtyType='R' "
				+ " AND l.M_Locator_ID=s.M_Locator_ID AND l.M_Warehouse_ID=t.M_Warehouse_ID),"
			+ "QtyOrdered = (SELECT COALESCE(SUM(Qty),0) FROM M_StorageDetail s, M_Locator l WHERE t.M_Product_ID=s.M_Product_ID"
				+ " AND s.QtyType='O' "
				+ " AND l.M_Locator_ID=s.M_Locator_ID AND l.M_Warehouse_ID=t.M_Warehouse_ID)";
		if (p_C_DocType_ID != 0)
			sql += ", C_DocType_ID= ? ";
		sql += " WHERE AD_PInstance_ID= ? ";
		if(p_C_DocType_ID != 0)
			no = DB.executeUpdate(get_TrxName(), sql,p_C_DocType_ID,getAD_PInstance_ID());
		else
			no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no != 0)
			log.fine("Update #" + no);

		//	Delete inactive products and replenishments
		sql = "DELETE FROM T_Replenish r "
			+ "WHERE (EXISTS (SELECT * FROM M_Product p "
				+ "WHERE p.M_Product_ID=r.M_Product_ID AND p.IsActive='N')"
			+ " OR EXISTS (SELECT * FROM M_Replenish rr "
				+ " WHERE rr.M_Product_ID=r.M_Product_ID AND rr.M_Warehouse_ID=r.M_Warehouse_ID AND rr.IsActive='N'))"
			+ " AND AD_PInstance_ID= ? ";
		no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no != 0)
			log.fine("Delete Inactive=" + no);
	 
		//	Ensure Data consistency
		sql = "UPDATE T_Replenish SET QtyOnHand = 0 WHERE QtyOnHand IS NULL";
		no = DB.executeUpdate(get_TrxName(), sql);
		sql = "UPDATE T_Replenish SET QtyReserved = 0 WHERE QtyReserved IS NULL";
		no = DB.executeUpdate(get_TrxName(), sql);
		sql = "UPDATE T_Replenish SET QtyOrdered = 0 WHERE QtyOrdered IS NULL";
		no = DB.executeUpdate(get_TrxName(), sql);

		//	Set Minimum / Maximum Maintain Level
		//	X_M_Replenish.REPLENISHTYPE_ReorderBelowMinimumLevel
		sql = "UPDATE T_Replenish"
			+ " SET QtyToOrder = Level_Min - QtyOnHand + QtyReserved - QtyOrdered "
			+ "WHERE ReplenishType='1'" 
			+ " AND AD_PInstance_ID= ? ";
		no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no != 0)
			log.fine("Update Type-1=" + no);
		//
		//	X_M_Replenish.REPLENISHTYPE_MaintainMaximumLevel
		sql = "UPDATE T_Replenish"
			+ " SET QtyToOrder = Level_Max - QtyOnHand + QtyReserved - QtyOrdered "
			+ "WHERE ReplenishType='2'" 
			+ " AND AD_PInstance_ID= ? ";
		no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no != 0)
			log.fine("Update Type-2=" + no);
	
		//	Delete rows where nothing to order
		sql = "DELETE FROM T_Replenish "
			+ "WHERE QtyToOrder < 1"
			+ " AND AD_PInstance_ID= ? ";
		no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no != 0)
			log.fine("Delete No QtyToOrder=" + no);

		//	Minimum Order Quantity
		sql = "UPDATE T_Replenish"
			+ " SET QtyToOrder = Order_Min "
			+ "WHERE QtyToOrder < Order_Min"
			+ " AND AD_PInstance_ID= ? ";
		no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no != 0)
			log.fine("Set MinOrderQty=" + no);

		//	Even dividable by Pack
		sql = "UPDATE T_Replenish"
			+ " SET QtyToOrder = (TRUNC(QtyToOrder/Order_Pack,0)+1)*Order_Pack "
			+ "WHERE (QtyToOrder-TRUNC(QtyToOrder/Order_Pack,0)*Order_Pack) <> 0"
			+ " AND AD_PInstance_ID= ? " ;
		no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no != 0)
			log.fine("Set OrderPackQty=" + no);
		
		//	Source from other warehouse
		if (wh.getM_WarehouseSource_ID() != 0)
		{
			sql = "UPDATE T_Replenish"
				+ " SET M_WarehouseSource_ID= ? "  
				+ " WHERE AD_PInstance_ID= ? " ;
			no = DB.executeUpdate(get_TrxName(), sql,wh.getM_WarehouseSource_ID(),getAD_PInstance_ID());
			if (no != 0)
				log.fine("Set Warehouse Source Warehouse=" + no);
		}
		//	Replenishment on Product level overwrites 
		sql = "UPDATE T_Replenish "
			+ "SET M_WarehouseSource_ID=(SELECT M_WarehouseSource_ID FROM M_Replenish r "
				+ "WHERE r.M_Product_ID=T_Replenish.M_Product_ID"
				+ " AND r.M_Warehouse_ID= ? )"
			+ "WHERE AD_PInstance_ID= ? "
			+ " AND EXISTS (SELECT * FROM M_Replenish r "
				+ "WHERE r.M_Product_ID=T_Replenish.M_Product_ID"
				+ " AND r.M_Warehouse_ID= ? " 
				+ " AND r.M_WarehouseSource_ID > 0)";
		no = DB.executeUpdate(get_TrxName(), sql,p_M_Warehouse_ID,getAD_PInstance_ID(),p_M_Warehouse_ID);
		if (no != 0)
			log.fine("Set Product Source Warehouse=" + no);
		
		//	Check Source Warehouse
		sql = "UPDATE T_Replenish"
			+ " SET M_WarehouseSource_ID = NULL " 
			+ "WHERE M_Warehouse_ID=M_WarehouseSource_ID"
			+ " AND AD_PInstance_ID= ? ";
		no = DB.executeUpdate(get_TrxName(), sql,getAD_PInstance_ID());
		if (no != 0)
			log.fine("Set same Source Warehouse=" + no);
		
		//	Custom Replenishment
		String className = wh.getReplenishmentClass();
		if (className == null || className.length() == 0)
			return;
		//	Get Replenishment Class
		ReplenishInterface custom = null;
		try
		{
			Class<?> clazz = Class.forName(className);
			custom = (ReplenishInterface)clazz.newInstance();
		}
		catch (Exception e)
		{
			throw new CompiereUserException("No custom Replenishment class "
				+ className + " - " + e.toString());
		}
		
		X_T_Replenish[] replenishs = getReplenish("ReplenishType='9'");
		for (X_T_Replenish replenish : replenishs) {
			if (replenish.getReplenishType().equals(X_T_Replenish.REPLENISHTYPE_Custom))
			{
				BigDecimal qto = null;
				try
				{
					qto = custom.getQtyToOrder(wh, replenish);
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, custom.toString(), e);
				}
				if (qto == null)
					qto = Env.ZERO;
				replenish.setQtyToOrder(qto);
				replenish.save();
			}
		}
	}	//	fillTable

	/**
	 * 	Create PO's
	 */
	private void createPO()
	{
		int noOrders = 0;
		String info = "";
		//
		MOrder order = null;
		MWarehouse wh = null;
		X_T_Replenish[] replenishs = getReplenish("M_WarehouseSource_ID IS NULL");
		for (X_T_Replenish replenish : replenishs) {
			if (wh == null || wh.getM_Warehouse_ID() != replenish.getM_Warehouse_ID())
				wh = MWarehouse.get(getCtx(), replenish.getM_Warehouse_ID());
			//
			if (order == null 
				|| order.getC_BPartner_ID() != replenish.getC_BPartner_ID()
				|| order.getM_Warehouse_ID() != replenish.getM_Warehouse_ID())
			{
				if (order != null)
				{
					// touch order to recalculate tax and totals
					order.setIsActive(order.isActive());
					order.save();
				}

				order = new MOrder(getCtx(), 0, get_TrxName());
				order.setIsSOTrx(false);
				order.setC_DocTypeTarget_ID(p_C_DocType_ID);
				MBPartner bp = new MBPartner(getCtx(), replenish.getC_BPartner_ID(), get_TrxName());
				order.setBPartner(bp);
				order.setSalesRep_ID(getAD_User_ID());
				order.setDescription(Msg.getMsg(getCtx(), "Replenishment"));
				//	Set Org/WH
				order.setAD_Org_ID(wh.getAD_Org_ID());
				order.setM_Warehouse_ID(wh.getM_Warehouse_ID());
				if (!order.save())
					return;
				log.fine(order.toString());
				noOrders++;
				info += " - " + order.getDocumentNo();
			}
			MOrderLine line = new MOrderLine (order);
			line.setM_Product_ID(replenish.getM_Product_ID());
			line.setQty(replenish.getQtyToOrder());
			line.setPrice();
			line.save();
		}
		// touch order to recalculate tax and totals
		order.setIsActive(order.isActive());
		order.save();

		m_info = "#" + noOrders + info;
		log.info(m_info);
	}	//	createPO
	
	/**
	 * 	Create Requisition
	 */
	private void createRequisition()
	{
		int noReqs = 0;
		String info = "";
		//
		MRequisition requisition = null;
		MWarehouse wh = null;
		X_T_Replenish[] replenishs = getReplenish("M_WarehouseSource_ID IS NULL");
		for (X_T_Replenish replenish : replenishs) {
			if (wh == null || wh.getM_Warehouse_ID() != replenish.getM_Warehouse_ID())
				wh = MWarehouse.get(getCtx(), replenish.getM_Warehouse_ID());
			//
			if (requisition == null
				|| requisition.getM_Warehouse_ID() != replenish.getM_Warehouse_ID())
			{
				requisition = new MRequisition (getCtx(), 0, get_TrxName());
				requisition.setAD_User_ID (getAD_User_ID());
				requisition.setC_DocType_ID(p_C_DocType_ID);
				requisition.setDescription(Msg.getMsg(getCtx(), "Replenishment"));
				//	Set Org/WH
				requisition.setAD_Org_ID(wh.getAD_Org_ID());
				requisition.setM_Warehouse_ID(wh.getM_Warehouse_ID());
				if (!requisition.save())
					return;
				log.fine(requisition.toString());
				noReqs++;
				info += " - " + requisition.getDocumentNo();
			}
			//
			MRequisitionLine line = new MRequisitionLine(requisition);
			line.setM_Product_ID(replenish.getM_Product_ID());
			line.setC_BPartner_ID(replenish.getC_BPartner_ID());
			line.setQty(replenish.getQtyToOrder());
			line.setPrice();
			line.save();
		}
		m_info = "#" + noReqs + info;
		log.info(m_info);
	}	//	createRequisition

	/**
	 * 	Create Inventory Movements
	 */
	private void createMovements()
	{
		int noMoves = 0;
		String info = "";
		//
		MClient client = null;
		MMovement move = null;
		int M_Warehouse_ID = 0;
		int M_WarehouseSource_ID = 0;
		MWarehouse whSource = null;
		MWarehouse whTarget = null;
		X_T_Replenish[] replenishs = getReplenish("M_WarehouseSource_ID IS NOT NULL");
		for (X_T_Replenish replenish : replenishs) {
			if (whSource == null || whSource.getM_WarehouseSource_ID() != replenish.getM_WarehouseSource_ID())
				whSource = MWarehouse.get(getCtx(), replenish.getM_WarehouseSource_ID());
			if (whTarget == null || whTarget.getM_Warehouse_ID() != replenish.getM_Warehouse_ID())
				whTarget = MWarehouse.get(getCtx(), replenish.getM_Warehouse_ID());
			if (client == null || client.getAD_Client_ID() != whSource.getAD_Client_ID())
				client = MClient.get(getCtx(), whSource.getAD_Client_ID());
			//
			if (move == null
				|| M_WarehouseSource_ID != replenish.getM_WarehouseSource_ID()
				|| M_Warehouse_ID != replenish.getM_Warehouse_ID())
			{
				M_WarehouseSource_ID = replenish.getM_WarehouseSource_ID();
				M_Warehouse_ID = replenish.getM_Warehouse_ID();
				
				move = new MMovement (getCtx(), 0, get_TrxName());
				move.setC_DocType_ID(p_C_DocType_ID);
				move.setDescription(Msg.getMsg(getCtx(), "Replenishment")
					+ ": " + whSource.getName() + "->" + whTarget.getName());
				//	Set Org
				move.setAD_Org_ID(whSource.getAD_Org_ID());
				if (!move.save())
					return;
				log.fine(move.toString());
				noMoves++;
				info += " - " + move.getDocumentNo();
			}
			MProduct product = MProduct.get(getCtx(), replenish.getM_Product_ID());
			//	To
			int M_LocatorTo_ID = getLocator_ID(product, whTarget); 
			
			//	From: Look-up Storage
			MProductCategory pc = MProductCategory.get(getCtx(), product.getM_Product_Category_ID());
			String MMPolicy = pc.getMMPolicy();
			if (MMPolicy == null || MMPolicy.length() == 0)
				MMPolicy = client.getMMPolicy();
			//
			List<Storage.VO> storages = Storage.getWarehouse(getCtx(), 
				whSource.getM_Warehouse_ID(), replenish.getM_Product_ID(), 0, 0,
				true, null, 
				X_AD_Client.MMPOLICY_FiFo.equals(MMPolicy), get_TrxName());
			if (storages == null || storages.size() == 0)
			{
				addLog ("No Inventory in " + whSource.getName() 
					+ " for " + product.getName());
				continue;
			}
			//
			BigDecimal target = replenish.getQtyToOrder();
			for (Storage.VO storage : storages) {
				BigDecimal qtyAvailable = storage.getAvailableQty();
				if(qtyAvailable.signum() <= 0)
					continue;

				BigDecimal moveQty = target;
				if (qtyAvailable.compareTo(moveQty) < 0)
					moveQty = qtyAvailable;
				//
				MMovementLine line = new MMovementLine(move);
				line.setM_Product_ID(replenish.getM_Product_ID());
				line.setMovementQty(moveQty);
				if (replenish.getQtyToOrder().compareTo(moveQty) != 0)
					line.setDescription("Total: " + replenish.getQtyToOrder());
				line.setM_Locator_ID(storage.getM_Locator_ID());		//	from
				line.setM_AttributeSetInstance_ID(storage.getM_AttributeSetInstance_ID());
				line.setM_LocatorTo_ID(M_LocatorTo_ID);					//	to
				line.setM_AttributeSetInstanceTo_ID(storage.getM_AttributeSetInstance_ID());
				line.save();
				//
				target = target.subtract(moveQty);
				if (target.signum() == 0)
					break;
			}
			if (target.signum() != 0)
				addLog ("Insufficient Inventory in " + whSource.getName() 
					+ " for " + product.getName() + " Qty=" + target);
		}
		if (replenishs.length == 0)
		{
			m_info = "No Source Warehouse";
			log.warning(m_info);
		}
		else
		{
			m_info = "#" + noMoves + info;
			log.info(m_info);
		}
	}	//	createRequisition

	/**
	 * 	Get Locator_ID
	 *	@param product product 
	 *	@param wh warehouse
	 *	@return locator with highest priority
	 */
	private int getLocator_ID (MProduct product, MWarehouse wh)
	{
		int M_Locator_ID = MProductLocator.getFirstM_Locator_ID (product, wh.getM_Warehouse_ID());
		/**	
		MLocator[] locators = MProductLocator.getLocators (product, wh.getM_Warehouse_ID());
		for (int i = 0; i < locators.length; i++)
		{
			MLocator locator = locators[i];
			//	Storage/capacity restrictions come here
			return locator.getM_Locator_ID();
		}
		//	default
		**/
		if (M_Locator_ID == 0)
			M_Locator_ID = wh.getDefaultM_Locator_ID();
		return M_Locator_ID;
	}	//	getLocator_ID
	
	
	/**
	 * 	Get Replenish Records
	 *	@return replenish
	 */
	private X_T_Replenish[] getReplenish (String where)
	{
		String sql = "SELECT * FROM T_Replenish "
			+ "WHERE AD_PInstance_ID=? AND C_BPartner_ID > 0 ";
		if (where != null && where.length() > 0)
			sql += " AND " + where;
		sql	+= " ORDER BY M_Warehouse_ID, M_WarehouseSource_ID, C_BPartner_ID";
		ArrayList<X_T_Replenish> list = new ArrayList<X_T_Replenish>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAD_PInstance_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new X_T_Replenish (getCtx(), rs, get_TrxName()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		X_T_Replenish[] retValue = new X_T_Replenish[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getReplenish
	
}	//	Replenish
