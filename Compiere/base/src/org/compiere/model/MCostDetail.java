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

import org.compiere.common.CompiereStateException;
import org.compiere.util.*;

/**
 * 	Cost Detail Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCostDetail.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MCostDetail extends X_M_CostDetail
{
    /** Logger for class MCostDetail */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCostDetail.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Create New Order Cost Detail for Purchase Orders.
	 * 	Called from Doc_MatchPO
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param C_OrderLine_ID order
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt total amount
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param trx transaction
	 *	@return true if created
	 */
	public static boolean createOrder (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int C_OrderLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, Trx trx)
	{
		//	Delete Unprocessed zero Differences
		String sql = "DELETE FROM M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND C_OrderLine_ID= ? " 
			+ " AND M_AttributeSetInstance_ID= ? " 
			+ " AND C_AcctSchema_ID = ? ";
		Object[] params = new Object[]{C_OrderLine_ID,M_AttributeSetInstance_ID,as.getC_AcctSchema_ID()};
		int no = DB.executeUpdate(trx, sql,params);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "C_OrderLine_ID=? AND M_AttributeSetInstance_ID=?", 
			C_OrderLine_ID, M_AttributeSetInstance_ID, trx);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trx);
			cd.setC_OrderLine_ID (C_OrderLine_ID);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createOrder

	/**
	 * 	Create New Invoice Cost Detail for AP Invoices.
	 * 	Called from Doc_Invoice - for Invoice Adjustments
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param C_InvoiceLine_ID invoice
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param trx transaction
	 *	@return true if created
	 */
	public static boolean createInvoice (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int C_InvoiceLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, Trx trx)
	{
		//	Delete Unprocessed zero Differences
		String sql = "DELETE FROM M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND C_InvoiceLine_ID=? "
			+ " AND M_AttributeSetInstance_ID= ? "
			+ " AND C_AcctSchema_ID = ? ";
		Object[] params = new Object[]{C_InvoiceLine_ID,M_AttributeSetInstance_ID,as.getC_AcctSchema_ID()};
		int no = DB.executeUpdate(trx, sql,params);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "C_InvoiceLine_ID=? AND M_AttributeSetInstance_ID=?", 
			C_InvoiceLine_ID, M_AttributeSetInstance_ID, trx);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trx);
			cd.setC_InvoiceLine_ID (C_InvoiceLine_ID);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createInvoice
	
	/**
	 * 	Create New Shipment Cost Detail for SO Shipments.
	 * 	Called from Doc_MInOut - for SO Shipments  
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_InOutLine_ID shipment
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param IsSOTrx sales order
	 *	@param trx transaction
	 *	@return true if no error
	 */
	public static boolean createShipment (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_InOutLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, boolean IsSOTrx, Trx trx)
	{
		//	Delete Unprocessed zero Differences
		String sql = "DELETE FROM M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_InOutLine_ID= ?"
			+ " AND M_AttributeSetInstance_ID= ? "
			+ " AND C_AcctSchema_ID = ? ";
		Object[] params = new Object[] {M_InOutLine_ID,M_AttributeSetInstance_ID,as.getC_AcctSchema_ID()};
		int no = DB.executeUpdate(trx, sql,params);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_InOutLine_ID=? AND M_AttributeSetInstance_ID=?", 
			M_InOutLine_ID, M_AttributeSetInstance_ID, trx);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trx);
			cd.setM_InOutLine_ID(M_InOutLine_ID);
			cd.setIsSOTrx(IsSOTrx);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createShipment
	
	/**
	 * 	Create New Work Order Transaction Cost detail
	 * 	Called from Doc_WorkOrderTransaction - for Work Order Transactions  
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_InOutLine_ID shipment
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param IsSOTrx sales order
	 *	@param trx transaction
	 *	@return true if no error
	 */
	public static boolean createWorkOrderTransaction (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_WorkOrderTransactionLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, boolean IsSOTrx, Trx trx)
	{
		//	Delete Unprocessed zero Differences
		String sql = "DELETE FROM M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_WorkOrderTransactionLine_ID=? " 
			+ " AND M_AttributeSetInstance_ID=? " 
			+ " AND C_AcctSchema_ID = ? " 
			+ " AND M_CostElement_ID = ? ";
		Object[] params = new Object[]{M_WorkOrderTransactionLine_ID,M_AttributeSetInstance_ID,
				as.getC_AcctSchema_ID(),M_CostElement_ID};
		int no = DB.executeUpdate(trx, sql,params);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_WorkOrderTransactionLine_ID=? AND M_AttributeSetInstance_ID=?", 
				M_WorkOrderTransactionLine_ID, M_AttributeSetInstance_ID, trx);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trx);
			cd.setM_WorkOrderTransactionLine_ID(M_WorkOrderTransactionLine_ID);
			cd.setIsSOTrx(IsSOTrx);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createWorkOrderTransaction
	

	/**
	 * 	Create New Work Order Resource Transaction Cost detail
	 * 	Called from Doc_WorkOrderTransaction - for Work Order Transactions  
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_InOutLine_ID shipment
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param IsSOTrx sales order
	 *	@param trx transaction
	 *	@return true if no error
	 */
	public static boolean createWorkOrderResourceTransaction (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_WorkOrderResourceTransactionLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, boolean IsSOTrx, Trx trx)
	{
		//	Delete Unprocessed zero Differences
		String sql = "DELETE FROM M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_WorkOrderResourceTxnLine_ID= ? "
			+ " AND M_AttributeSetInstance_ID=? " 
			+ " AND C_AcctSchema_ID = ? ";
		Object[] params = new Object[]{M_WorkOrderResourceTransactionLine_ID,
				M_AttributeSetInstance_ID,as.getC_AcctSchema_ID()};
		int no = DB.executeUpdate(trx, sql,params);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_WorkOrderResourceTxnLine_ID=? AND M_AttributeSetInstance_ID=?", 
				M_WorkOrderResourceTransactionLine_ID, M_AttributeSetInstance_ID, trx);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trx);
			cd.setM_WorkOrderResourceTxnLine_ID(M_WorkOrderResourceTransactionLine_ID);
			cd.setIsSOTrx(IsSOTrx);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createWorkOrderResourceTransaction

	
	/**
	 * 	Create New Order Cost Detail for Physical Inventory.
	 * 	Called from Doc_Inventory
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_InventoryLine_ID order
	 *	@param M_CostElement_ID optional cost element
	 *	@param Amt amt total amount
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param trx transaction
	 *	@return true if no error
	 */
	public static boolean createInventory (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_InventoryLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, Trx trx)
	{
		//	Delete Unprocessed zero Differences
		String sql = "DELETE FROM M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_InventoryLine_ID=? " 
			+ " AND M_AttributeSetInstance_ID= ? " 
			+ " AND C_AcctSchema_ID = ? ";
		Object[] params = new Object[]{M_InventoryLine_ID,M_AttributeSetInstance_ID,as.getC_AcctSchema_ID()};
		int no = DB.executeUpdate(trx, sql,params);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_InventoryLine_ID=? AND M_AttributeSetInstance_ID=?", 
			M_InventoryLine_ID, M_AttributeSetInstance_ID, trx);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trx);
			cd.setM_InventoryLine_ID(M_InventoryLine_ID);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createInventory
	
	/**
	 * 	Create New Order Cost Detail for Movements.
	 * 	Called from Doc_Movement
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_MovementLine_ID movement
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt total amount
	 *	@param Qty qty
	 *	@param from if true the from (reduction)
	 *	@param Description optional description
	 *	@param trx transaction
	 *	@return true if no error
	 */
	public static boolean createMovement (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_MovementLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty, boolean from,
		String Description, Trx trx)
	{
		//	Delete Unprocessed zero Differences
		String sql = "DELETE FROM M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_MovementLine_ID= ? "  
			+ " AND IsSOTrx=" + (from ? "'Y'" : "'N'")
			+ " AND M_AttributeSetInstance_ID=? " 
			+ " AND C_AcctSchema_ID = ? ";
		Object[] params = new Object[]{M_MovementLine_ID,M_AttributeSetInstance_ID,as.getC_AcctSchema_ID()};
		int no = DB.executeUpdate(trx, sql,params);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_MovementLine_ID=? AND M_AttributeSetInstance_ID=? AND IsSOTrx=" 
			+ (from ? "'Y'" : "'N'"), 
			M_MovementLine_ID, M_AttributeSetInstance_ID, trx);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trx);
			cd.setM_MovementLine_ID (M_MovementLine_ID);
			cd.setIsSOTrx(from);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createMovement

	/**
	 * 	Create New Order Cost Detail for Production.
	 * 	Called from Doc_Production
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_ProductionLine_ID production line
	 *	@param M_CostElement_ID optional cost element
	 *	@param Amt amt total amount
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param trx transaction
	 *	@return true if no error
	 */
	public static boolean createProduction (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_ProductionLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, Trx trx)
	{
		//	Delete Unprocessed zero Differences
		String sql = "DELETE FROM M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_ProductionLine_ID= ? "
			+ " AND M_AttributeSetInstance_ID= ? " 
			+ " AND C_AcctSchema_ID = ? ";
		Object[] params = new Object[] {M_ProductionLine_ID,M_AttributeSetInstance_ID,as.getC_AcctSchema_ID()};
		int no = DB.executeUpdate(trx, sql,params);
		if (no != 0)
			s_log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_ProductionLine_ID=? AND M_AttributeSetInstance_ID=?", 
			M_ProductionLine_ID, M_AttributeSetInstance_ID, trx);
		//
		if (cd == null)		//	createNew
		{
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trx);
			cd.setM_ProductionLine_ID(M_ProductionLine_ID);
		}
		else
		{
			cd.setDeltaAmt(cd.getAmt().subtract(Amt));
			cd.setDeltaQty(cd.getQty().subtract(Qty));
			if (cd.isDelta())
				cd.setProcessed(false);
			else
				return true;	//	nothing to do
		}
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate())
				cd.process();
		}
		s_log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createProduction
	
	
	/**************************************************************************
	 * 	Get Cost Detail
	 *	@param ctx context
	 *	@param whereClause where clause
	 *	@param ID 1st parameter
	 *	@param M_AttributeSetInstance_ID ASI
	 *	@param trx p_trx
	 *	@return cost detail
	 */
	private static MCostDetail get (Ctx ctx, String whereClause, 
		int ID, int M_AttributeSetInstance_ID, Trx trx)
	{
		String sql = "SELECT * FROM M_CostDetail WHERE " + whereClause;
		MCostDetail retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt (1, ID);
			pstmt.setInt (2, M_AttributeSetInstance_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MCostDetail (ctx, rs, trx);
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql + " - " + ID, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return retValue;
	}	//	get
	
	/**
	 * 	Process Cost Details for product
	 *	@param product product
	 *	@param trx transaction
	 *	@return true if no error
	 */
	public static boolean processProduct (MProduct product, Trx trx)
	{
		String sql = "SELECT * FROM M_CostDetail "
			+ "WHERE M_Product_ID=?"
			+ " AND Processed='N' "
			+ "ORDER BY C_AcctSchema_ID, M_CostElement_ID, AD_Org_ID, M_AttributeSetInstance_ID, Created";
		int counterOK = 0;
		int counterError = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, product.getM_Product_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MCostDetail cd = new MCostDetail(product.getCtx(), rs, trx);
				if (cd.process())	//	saves
					counterOK++;
				else
					counterError++;
			}
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
			counterError++;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		s_log.config(product.getValue() + ": OK=" + counterOK + ", Errors=" + counterError);
		return counterError == 0;
	}	//	processProduct
	
	/**	Logger	*/
	private static CLogger 	s_log = CLogger.getCLogger (MCostDetail.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_CostDetail_ID id
	 *	@param trx p_trx
	 */
	public MCostDetail (Ctx ctx, int M_CostDetail_ID, Trx trx)
	{
		super (ctx, M_CostDetail_ID, trx);
		if (M_CostDetail_ID == 0)
		{
		//	setC_AcctSchema_ID (0);
		//	setM_Product_ID (0);
			setM_AttributeSetInstance_ID (0);
		//	setC_OrderLine_ID (0);
		//	setM_InOutLine_ID(0);
		//	setC_InvoiceLine_ID (0);
			setProcessed (false);
			setAmt (Env.ZERO);
			setQty (Env.ZERO);
			setIsSOTrx (false);
			setDeltaAmt (Env.ZERO);
			setDeltaQty (Env.ZERO);
		}	
	}	//	MCostDetail

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MCostDetail (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MCostDetail

	/**
	 * 	New Constructor
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param trx transaction
	 */
	public MCostDetail (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_CostElement_ID, BigDecimal Amt, BigDecimal Qty,
		String Description, Trx trx)
	{
		this (as.getCtx(), 0, trx);
		setClientOrg(as.getAD_Client_ID(), AD_Org_ID);
		setC_AcctSchema_ID (as.getC_AcctSchema_ID());
		setM_Product_ID (M_Product_ID);
		setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
		//
		setM_CostElement_ID(M_CostElement_ID);
		//
		setAmt (Amt);
		setQty (Qty);
		setDescription(Description);
	}	//	MCostDetail
	
	/**
	 * 	Set Amt
	 *	@param Amt amt
	 */
	@Override
	public void setAmt (BigDecimal Amt)
	{
		if (isProcessed())
			throw new CompiereStateException("Cannot change Amt - processed");
		if (Amt == null)
			super.setAmt (Env.ZERO);
		else
			super.setAmt (Amt);
	}	//	setAmt
	
	/**
	 * 	Set Qty
	 *	@param Qty qty
	 */
	@Override
	public void setQty (BigDecimal Qty)
	{
		if (isProcessed())
			throw new CompiereStateException("Cannot change Qty - processed");
		if (Qty == null)
			super.setQty (Env.ZERO);
		else
			super.setQty (Qty);
	}	//	setQty

	/**
	 * 	Is Order
	 *	@return true if order line
	 */
	public boolean isOrder()
	{
		return getC_OrderLine_ID() != 0;
	}	//	isOrder

	/**
	 * 	Is Invoice
	 *	@return true if invoice line
	 */
	public boolean isInvoice()
	{
		return getC_InvoiceLine_ID() != 0;
	}	//	isInvoice

	/**
	 * 	Is Shipment
	 *	@return true if sales order shipment
	 */
	public boolean isShipment()
	{
		return isSOTrx() && getM_InOutLine_ID() != 0;
	}	//	isShipment
	
	/**
	 * 	Is this a Delta Record (previously processed)?
	 *	@return true if delta is not null
	 */
	public boolean isDelta()
	{
		return !(getDeltaAmt().signum() == 0 
			&& getDeltaQty().signum() == 0);
	}	//	isDelta
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		return true;
	}	//	afterSave
	
	/**
	 * 	Before Delete
	 *	@return false if processed
	 */
	@Override
	protected boolean beforeDelete ()
	{
		return !isProcessed();
	}	//	beforeDelete
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MCostDetail[");
		sb.append (get_ID());
		if (getC_OrderLine_ID() != 0)
			sb.append (",C_OrderLine_ID=").append (getC_OrderLine_ID());
		if (getM_InOutLine_ID() != 0)
			sb.append (",M_InOutLine_ID=").append (getM_InOutLine_ID());
		if (getC_InvoiceLine_ID() != 0)
			sb.append (",C_InvoiceLine_ID=").append (getC_InvoiceLine_ID());
		if (getC_ProjectIssue_ID() != 0)
			sb.append (",C_ProjectIssue_ID=").append (getC_ProjectIssue_ID());
		if (getM_MovementLine_ID() != 0)
			sb.append (",M_MovementLine_ID=").append (getM_MovementLine_ID());
		if (getM_InventoryLine_ID() != 0)
			sb.append (",M_InventoryLine_ID=").append (getM_InventoryLine_ID());
		if (getM_ProductionLine_ID() != 0)
			sb.append (",M_ProductionLine_ID=").append (getM_ProductionLine_ID());
		sb.append(",Amt=").append(getAmt())
			.append(",Qty=").append(getQty());
		if (isDelta())
			sb.append(",DeltaAmt=").append(getDeltaAmt())
				.append(",DeltaQty=").append(getDeltaQty());
		sb.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	/**************************************************************************
	 * 	Process Cost Detail Record.
	 * 	The record is saved if processed.
	 *	@return true if processed
	 */
	public synchronized boolean process()
	{
		if (isProcessed())
		{
			log.info("Already processed");
			return true;
		}
		boolean ok = false;

		//	get costing level for product
		MAcctSchema as = new MAcctSchema (getCtx(), getC_AcctSchema_ID(), null);
		String CostingLevel = as.getCostingLevel();
		MProduct product = MProduct.get(getCtx(), getM_Product_ID());
		MProductCategoryAcct pca = MProductCategoryAcct.get (getCtx(),
			product.getM_Product_Category_ID(), getC_AcctSchema_ID(), null);	
		if (pca.getCostingLevel() != null)
			CostingLevel = pca.getCostingLevel();
		//	Org Element
		int Org_ID = getAD_Org_ID();
		int M_ASI_ID = getM_AttributeSetInstance_ID();
		if (X_C_AcctSchema.COSTINGLEVEL_Tenant.equals(CostingLevel))
		{
			Org_ID = 0;
			M_ASI_ID = 0;
		}
		else if (X_C_AcctSchema.COSTINGLEVEL_Organization.equals(CostingLevel))
			M_ASI_ID = 0;
		else if (X_C_AcctSchema.COSTINGLEVEL_BatchLot.equals(CostingLevel))
			Org_ID = 0;

		//	Create Material Cost elements
		if (getM_CostElement_ID() == 0)
		{
			MCostElement[] ces = MCostElement.getCostingMethods(this);
			for (MCostElement ce : ces) {
				ok = process (as, product, ce, Org_ID, M_ASI_ID);
				if (!ok)
					break;
			}
		}	//	Material Cost elements
		else
		{
			MCostElement ce = new MCostElement(getCtx(), getM_CostElement_ID(), get_Trx());
			ok = process (as, product, ce, Org_ID, M_ASI_ID);
		}
		
		//	Save it
		if (ok)
		{
			setDeltaAmt(null);
			setDeltaQty(null);
			setProcessed(true);
			ok = save();
		}
		log.info(ok + " - " + toString());
		return ok;
	}	//	process
	
	/**
	 * 	Process cost detail for cost record
	 *	@param as accounting schema
	 *	@param product product
	 *	@param ce cost element
	 *	@param Org_ID org - corrected for costing level
	 *	@param M_ASI_ID - asi corrected for costing level
	 *	@return true if cost ok
	 */
	private boolean process (MAcctSchema as, MProduct product, MCostElement ce, 
		int Org_ID, int M_ASI_ID)
	{
		MCost cost = MCost.get(product, M_ASI_ID, as, 
			Org_ID, ce.getM_CostElement_ID());
	//	if (cost == null)
	//		cost = new MCost(product, M_ASI_ID, 
	//			as, Org_ID, ce.getM_CostElement_ID());

		BigDecimal qty = getQty();
		BigDecimal amt = getAmt();
		int precision = as.getCostingPrecision();
		BigDecimal price = amt;
		if (qty.signum() != 0)
			price = amt.divide(qty, precision, BigDecimal.ROUND_HALF_UP);
		
		
		/** All Costing Methods
		if (ce.isAverageInvoice())
		else if (ce.isAveragePO())
		else if (ce.isFifo())
		else if (ce.isLifo())
		else if (ce.isLastInvoice())
		else if (ce.isLastPOPrice())
		else if (ce.isStandardCosting())
		else if (ce.isUserDefined())
		else if (!ce.isCostingMethod())
		**/
		
		//	*** Purchase Order Detail Record ***
		if (getC_OrderLine_ID() != 0)
		{
			new MOrderLine (getCtx(), getC_OrderLine_ID(), null);
			boolean isReturnTrx = qty.signum() < 0;
			log.fine(" ");
			
			if (ce.isAveragePO())
			{
				if (!isReturnTrx)
					cost.setWeightedAverage(amt, qty);
				else
					cost.add(amt, qty);

				log.finer("PO - AveragePO - " + cost);
			}
			else if (ce.isLastPOPrice())
			{
				if(!isReturnTrx)
				{
					if (qty.signum() != 0)
						cost.setCurrentCostPrice(price);
					else
					{
						BigDecimal cCosts = cost.getCurrentCostPrice().add(amt);
						cost.setCurrentCostPrice(cCosts);
					}
				}
				cost.add(amt, qty);
				log.finer("PO - LastPO - " + cost);
			}
			else if (ce.isUserDefined())
			{
				//	Interface
				log.finer("PO - UserDef - " + cost);
			}
			else if (!ce.isCostingMethod())
			{
				log.finer("PO - " + ce + " - " + cost);
			}
		//	else
		//		log.warning("PO - " + ce + " - " + cost);
		}
		
		//	*** AP Invoice Detail Record ***
		else if (getC_InvoiceLine_ID() != 0)
		{
			boolean isReturnTrx = qty.signum() < 0;
			if (ce.isAverageInvoice())
			{
				if (!isReturnTrx)
					cost.setWeightedAverage(amt, qty);
				else
					cost.add(amt, qty);
				log.finer("Inv - AverageInv - " + cost);
			}
			else if (ce.isFifo()
				|| ce.isLifo())
			{
				//	Real ASI - costing level Org
				MCostQueue cq = MCostQueue.get(product, getM_AttributeSetInstance_ID(), 
					as, Org_ID, ce.getM_CostElement_ID(), get_Trx());
				cq.setCosts(amt, qty, precision);
				cq.save();
				//	Get Costs - costing level Org/ASI
				MCostQueue[] cQueue = MCostQueue.getQueue(product, M_ASI_ID, 
					as, Org_ID, ce, get_Trx());
				if (cQueue != null && cQueue.length > 0)
					cost.setCurrentCostPrice(cQueue[0].getCurrentCostPrice());
				cost.add(amt, qty);
				log.finer("Inv - FiFo/LiFo - " + cost);
			}
			else if (ce.isLastInvoice())
			{
				if (!isReturnTrx)
				{
					if (qty.signum() != 0)
						cost.setCurrentCostPrice(price);
					else
					{
						BigDecimal cCosts = cost.getCurrentCostPrice().add(amt);
						cost.setCurrentCostPrice(cCosts);
					}
				}
				cost.add(amt, qty);
				log.finer("Inv - LastInv - " + cost);
			}
			else if (ce.isStandardCosting())
			{
				if (cost.getCurrentCostPrice().signum() == 0)
				{
					cost.setCurrentCostPrice(price);
					//	seed initial price
					if (cost.getCurrentCostPrice().signum() == 0 
						&& cost.get_ID() == 0)
						cost.setCurrentCostPrice(
							MCost.getSeedCosts(product, M_ASI_ID, 
								as, Org_ID, ce.getCostingMethod(), getC_OrderLine_ID()));
				}
				cost.add(amt, qty);
				log.finer("Inv - Standard - " + cost);
			}
			else if (ce.isUserDefined())
			{
				//	Interface
				cost.add(amt, qty);
				log.finer("Inv - UserDef - " + cost);
			}
			else if (!ce.isCostingMethod())		//	Cost Adjustments
			{
				BigDecimal cCosts = ((cost.getCurrentCostPrice().multiply(cost.getCurrentQty())).add(amt));
				BigDecimal qty1 = cost.getCurrentQty().add(qty);
				if(qty1.compareTo(BigDecimal.ZERO)== 0)
					qty1 = BigDecimal.ONE;
				cCosts = cCosts.divide(qty1,precision,BigDecimal.ROUND_HALF_UP);
				cost.setCurrentCostPrice(cCosts);
				cost.add(amt, qty);
				log.finer("Inv - none - " + cost);
			}
		//	else
		//		log.warning("Inv - " + ce + " - " + cost);
		}
		//	*** Qty Adjustment Detail Record ***
		else if (getM_InOutLine_ID() != 0 		//	AR Shipment Detail Record  
			|| getM_MovementLine_ID() != 0 
			|| getM_InventoryLine_ID() != 0
			|| getM_ProductionLine_ID() != 0
			|| getC_ProjectIssue_ID() != 0
			|| getM_WorkOrderTransactionLine_ID()!=0
			|| getM_WorkOrderResourceTxnLine_ID() !=0)
		{
			boolean addition = qty.signum() > 0 && amt.signum() > 0;
			//
			if (ce.isAverageInvoice())
			{
				if (addition)
					cost.setWeightedAverage(amt, qty);
				else
					cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - AverageInv - " + cost);
			}
			else if (ce.isAveragePO())
			{
				if (addition)
					cost.setWeightedAverage(amt, qty);
				else
					cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - AveragePO - " + cost);
			}
			else if (ce.isFifo() || ce.isLifo())
			{
				if (addition)
				{
					//	Real ASI - costing level Org
					MCostQueue cq = MCostQueue.get(product, getM_AttributeSetInstance_ID(), 
						as, Org_ID, ce.getM_CostElement_ID(), get_Trx());
					cq.setCosts(amt, qty, precision);
					cq.save();
				}
				else
				{
					//	Adjust Queue - costing level Org/ASI
					MCostQueue.adjustQty(product, M_ASI_ID, 
						as, Org_ID, ce, qty.negate(), get_Trx());
				}
				//	Get Costs - costing level Org/ASI
				MCostQueue[] cQueue = MCostQueue.getQueue(product, M_ASI_ID, 
					as, Org_ID, ce, get_Trx());
				if (cQueue != null && cQueue.length > 0)
					cost.setCurrentCostPrice(cQueue[0].getCurrentCostPrice());
				cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - FiFo/Lifo - " + cost);
			}
			else if (ce.isLastInvoice())
			{
				cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - LastInv - " + cost);
			}
			else if (ce.isLastPOPrice())
			{
				cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - LastPO - " + cost);
			}
			else if (ce.isStandardCosting())
			{
				if (addition)
				{
					cost.add(amt, qty);
					//	Initial
					if (cost.getCurrentCostPrice().signum() == 0 
						&& cost.get_ID() == 0)
						cost.setCurrentCostPrice(price);
				}
				else
					cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - Standard - " + cost);
			}
			else if (ce.isUserDefined())
			{
				//	Interface
				if (addition)
					cost.add(amt, qty);
				else
					cost.setCurrentQty(cost.getCurrentQty().add(qty));
				log.finer("QtyAdjust - UserDef - " + cost);
			}
			else if (!ce.isCostingMethod())
			{
			//	Should not happen
				log.finer("QtyAdjust - ?none? - " + cost);
			}
			else
				log.warning("QtyAdjust - " + ce + " - " + cost);
		}
		else	//	unknown or no id
		{
			log.warning("Unknown Type: " + toString());
			return false;
		}
		
		return cost.save();
	}	//	process
	
}	//	MCostDetail
