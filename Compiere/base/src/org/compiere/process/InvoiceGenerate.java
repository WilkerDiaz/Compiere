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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

import org.compiere.Compiere;
import org.compiere.common.CompiereStateException;
import org.compiere.common.constants.*;
import org.compiere.framework.PO;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.vos.*;

/**
 *	Generate Invoices
 *	
 *  @author Jorg Janke
 *  @version $Id: InvoiceGenerate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class InvoiceGenerate extends SvrProcess
{
	/**	Manual Selection		*/
	private boolean 	p_Selection = false;
	/**	Date Invoiced			*/
	private Timestamp	p_DateInvoiced = null;
	/**	Org						*/
	private int			p_AD_Org_ID = 0;
	/** BPartner				*/
	private int			p_C_BPartner_ID = 0;
	/** user				    */
	private int			p_AD_User_ID = 0;
	/** Order					*/
	private int			p_C_Order_ID = 0;
	/** Consolidate				*/
	private boolean		p_ConsolidateDocument = true;
	/** Invoice Document Action	*/
	private String		p_docAction = DocActionConstants.ACTION_Complete;

	public ArrayList<MOrder> m_lockedOrders = new ArrayList<MOrder>();

	/**	The current Invoice	*/
	private MInvoice 	m_invoice = null;
	/**	The current Shipment	*/
	private MInOut	 	m_ship = null;
	/** Numner of Invoices		*/
	private int			m_created = 0;
	/**	Line Number				*/
	private int			m_line = 0;
	/**	Business Partner		*/
	private MBPartner	m_bp = null;
	/** Date Ordered From **/
	private Timestamp	p_DateOrdered_From = null;
	/** Date Ordered To **/
	private Timestamp	p_DateOrdered_To = null;

	/**	Invoice Lines List for bulk update */
	private List<MInvoiceLine> invoiceLinesToSave = new ArrayList<MInvoiceLine>();
	/**	Invoices Map for linking unsaved invoices to invoice lines */
	private Map<MInvoice, List<MInvoiceLine>> invoiceMap = new HashMap<MInvoice, List<MInvoiceLine>>();
	/** Invoices Cash Book Map */
	private Map<Integer, Integer> invoiceCashBookMap = new HashMap<Integer, Integer>();
	/** ShipmentLines list for bulk update */
	private List<MInOutLine> inoutLines = new ArrayList<MInOutLine>();

	private static final boolean TESTMODE = false;
	/** Commit every 100 entities	*/
	private static final int	COMMITCOUNT = TESTMODE?100:Integer.parseInt(Ini.getProperty(Ini.P_IMPORT_BATCH_SIZE));
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null && element.getParameter_To() == null)
				;
			else if (name.equals("Selection"))
				p_Selection = "Y".equals(element.getParameter());
			else if (name.equals("DateInvoiced"))
				p_DateInvoiced = (Timestamp)element.getParameter();
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = element.getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = element.getParameterAsInt();
			else if (name.equals("C_Order_ID"))
				p_C_Order_ID = element.getParameterAsInt();
			else if (name.equals("ConsolidateDocument"))
				p_ConsolidateDocument = "Y".equals(element.getParameter());
			else if (name.equals("DocAction"))
				p_docAction = (String)element.getParameter();
			else if (name.equals("DateOrdered"))
			{
				p_DateOrdered_From = (Timestamp)element.getParameter();
				p_DateOrdered_To = (Timestamp)element.getParameter_To();
			}
			else if (name.equals("AD_User_ID"))
			{
				p_AD_User_ID = element.getParameterAsInt();
			}    
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);

		}

		//	Login Date
		if (p_DateInvoiced == null)
			p_DateInvoiced = new Timestamp(getCtx().getContextAsTime("#Date"));

		//	DocAction check
		if (!DocActionConstants.ACTION_Complete.equals(p_docAction))
			p_docAction = DocActionConstants.ACTION_Prepare;
	}	//	prepare

	/**
	 * 	Generate Invoices
	 *	@return info
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("Selection=" + p_Selection + ", DateInvoiced=" + p_DateInvoiced
				+ ", AD_Org_ID=" + p_AD_Org_ID + ", C_BPartner_ID=" + p_C_BPartner_ID
				+ ", C_Order_ID=" + p_C_Order_ID + ", DocAction=" + p_docAction 
				+ ", Consolidate=" + p_ConsolidateDocument+", AD_User_ID=" + p_AD_User_ID
				+ ", DateOrdered=" + p_DateOrdered_From + "->" + p_DateOrdered_To);

		//
		String sql = null;
		if (p_Selection)	//	VInvoiceGen
		{
			sql = "SELECT * FROM C_Order "
				+ "WHERE IsSelected='Y' AND DocStatus IN('CO','CL') AND IsSOTrx='Y' "
				+ "ORDER BY M_Warehouse_ID, PriorityRule, C_BPartner_ID, Bill_Location_ID, C_PaymentTerm_ID, C_Order_ID";
		}
		else
		{
			sql = "SELECT * FROM C_Order o "
				+ "WHERE DocStatus IN('CO','CL') AND IsSOTrx='Y'";
			if (p_AD_Org_ID != 0)
				sql += " AND AD_Org_ID=?";
			if (p_C_BPartner_ID != 0)
				sql += " AND C_BPartner_ID=?";
			if (p_C_Order_ID != 0)
				sql += " AND C_Order_ID=?";
			if (p_DateOrdered_From != null)
				sql+=" AND TRUNC(DateOrdered,'DD') >= ?";
			if (p_DateOrdered_To != null)
				sql+=" AND TRUNC(DateOrdered,'DD') <= ?";
			if (p_AD_User_ID != 0)
				sql += " AND AD_User_ID=?";
			//
			sql += " AND EXISTS (SELECT * FROM C_OrderLine ol "
				+ "WHERE o.C_Order_ID=ol.C_Order_ID AND ol.QtyOrdered<>ol.QtyInvoiced) "
				+ "ORDER BY M_Warehouse_ID, PriorityRule, C_BPartner_ID, Bill_Location_ID, C_CashBook_ID, C_PaymentTerm_ID, C_Order_ID";
		}
		//	sql += " FOR UPDATE";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			int index = 1;
			if (!p_Selection && p_AD_Org_ID != 0)
				pstmt.setInt(index++, p_AD_Org_ID);
			if (!p_Selection && p_C_BPartner_ID != 0)
				pstmt.setInt(index++, p_C_BPartner_ID);
			if (!p_Selection && p_C_Order_ID != 0)
				pstmt.setInt(index++, p_C_Order_ID);
			if (!p_Selection && p_DateOrdered_From != null)
				pstmt.setTimestamp(index++, p_DateOrdered_From);
			if (!p_Selection && p_DateOrdered_To != null)
				pstmt.setTimestamp(index++, p_DateOrdered_To);
			if (!p_Selection && p_AD_User_ID != 0)
				pstmt.setInt(index++, p_AD_User_ID);	
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		return generate(pstmt);
	}	//	doIt


	/**
	 * 	Generate Shipments
	 * 	@param pstmt order query 
	 *	@return info
	 */
	private String generate (PreparedStatement pstmt)
	{
		ResultSet rs = null;
		try
		{
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MOrder order = new MOrder (getCtx(), rs, get_TrxName());

				/* Lock Orders so it does not get processed twice */
				if(!order.lockForInvoicing())
					continue;

				m_lockedOrders.add(order);

				//	New Invoice Location
				if (!p_ConsolidateDocument 
						|| (m_invoice != null 
								&& (m_invoice.getC_BPartner_Location_ID() != order.getBill_Location_ID()
										|| m_invoice.getC_PaymentTerm_ID() != order.getC_PaymentTerm_ID() 
										|| m_invoice.getC_DocTypeTarget_ID() != MDocType.get(getCtx(), order.getC_DocType_ID()).getC_DocTypeInvoice_ID()
										|| m_invoice.getC_CashBook_ID() != order.getC_CashBook_ID()))){
					m_invoice = null;
					m_ship = null;
					m_line = 0;
					if(invoiceMap.size() >= COMMITCOUNT){
						completeInvoice();
						invoiceMap.clear();
						invoiceLinesToSave.clear();
					}
				}
				boolean completeOrder = X_C_Order.INVOICERULE_AfterOrderDelivered.equals(order.getInvoiceRule());

				//	Schedule After Delivery
				boolean doInvoice= false;
				if (X_C_Order.INVOICERULE_CustomerScheduleAfterDelivery.equals(order.getInvoiceRule()))
				{
					m_bp = new MBPartner (getCtx(), order.getBill_BPartner_ID(), null);
					if (m_bp.getC_InvoiceSchedule_ID() == 0)
					{
						log.warning("BPartner has no Schedule - set to After Delivery");
						order.setInvoiceRule(X_C_Order.INVOICERULE_AfterDelivery);
						order.save();
					}
					else
					{
						MInvoiceSchedule is = MInvoiceSchedule.get(getCtx(), m_bp.getC_InvoiceSchedule_ID());
						if (is.canInvoice(order.getDateOrdered(), order.getGrandTotal()))
							doInvoice = true;
						else
							continue;
					}
				}	//	Schedule

				MOrderLine[] oLines = order.getLines();

				//	After Delivery
				if (doInvoice || X_C_Order.INVOICERULE_AfterDelivery.equals(order.getInvoiceRule()))
				{
					MInOut shipment = null;
					MInOutLine[] shipmentLines = order.getShipmentLines();

					// sraval: 10017443- Sort Shipment lines by Line Number 
					Arrays.sort(shipmentLines, new Comparator<MInOutLine>(){
						public int compare(MInOutLine o1, MInOutLine o2) {
							return o1.getLine()-o2.getLine();
						}});
					// end 10017443
					for (MInOutLine shipLine : shipmentLines) {
						if (shipLine.isInvoiced())
							continue;
						if (shipment == null 
								|| shipment.getM_InOut_ID() != shipLine.getM_InOut_ID())
							shipment = new MInOut(getCtx(), shipLine.getM_InOut_ID(), get_TrxName());
						if (!shipment.isComplete()		//	ignore incomplete or reversals 
								|| shipment.getDocStatus().equals(X_M_InOut.DOCSTATUS_Reversed))
							continue;
						//
						createLine (order, shipment, shipLine);
					}	//	shipment lines
					m_line += 1000;
				}
				//	After Order Delivered, Immediate
				else
				{
					for (MOrderLine oLine : oLines) {
						BigDecimal toInvoice = oLine.getQtyOrdered().subtract(oLine.getQtyInvoiced());
						if (toInvoice.compareTo(Env.ZERO) == 0 && oLine.getM_Product_ID() != 0)
							continue;
						//	BigDecimal notInvoicedShipment = oLine.getQtyDelivered().subtract(oLine.getQtyInvoiced());
						//
						boolean fullyDelivered = oLine.getQtyOrdered().compareTo(oLine.getQtyDelivered()) == 0;

						//	Complete Order
						if (completeOrder && !fullyDelivered)
						{
							log.fine("Failed CompleteOrder - " + oLine);
							completeOrder = false;
							break;
						}
						//	Immediate
						else if (X_C_Order.INVOICERULE_Immediate.equals(order.getInvoiceRule()))
						{
							log.fine("Immediate - ToInvoice=" + toInvoice + " - " + oLine);
							BigDecimal qtyEntered = toInvoice;
							//	Correct UOM for QtyEntered
							if (oLine.getQtyEntered().compareTo(oLine.getQtyOrdered()) != 0)
								qtyEntered = toInvoice
								.multiply(oLine.getQtyEntered())
								.divide(oLine.getQtyOrdered(), 12, BigDecimal.ROUND_HALF_UP);
							createLine (order, oLine, toInvoice, qtyEntered);
							log.info("ID "+oLine.get_ID() + "Qty Ordered " + oLine.getQtyOrdered() + " Qty Invoiced "+oLine.getQtyInvoiced());
						}
						else
						{
							log.fine("Failed: " + order.getInvoiceRule() 
									+ " - ToInvoice=" + toInvoice + " - " + oLine);
						}
					}	//	for all order lines
					if (X_C_Order.INVOICERULE_Immediate.equals(order.getInvoiceRule()))
						m_line += 1000;
				}

				//	Complete Order successful
				if (completeOrder && X_C_Order.INVOICERULE_AfterOrderDelivered.equals(order.getInvoiceRule()))
				{
					MInOut[] shipments = order.getShipments(true);
					for (MInOut ship : shipments) {
						if (!ship.isComplete()		//	ignore incomplete or reversals 
								|| ship.getDocStatus().equals(X_M_InOut.DOCSTATUS_Reversed))
							continue;
						MInOutLine[] shipLines = ship.getLines(false);
						for (MInOutLine shipLine : shipLines) {
							boolean isOrderLine = false;
							for (MOrderLine oLine : oLines) {
								if (oLine.getC_OrderLine_ID() == shipLine.getC_OrderLine_ID()) {
									isOrderLine = true;
									break;
								}
							}
							if (!isOrderLine)
								continue;
							if (!shipLine.isInvoiced())
								createLine (order, ship, shipLine);
						}	//	lines
						m_line += 1000;
					}	//	all shipments
				}	//	complete Order

			}	//	for all orders
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if(invoiceMap.size() > 0)
			completeInvoice();

		//	unlock all order that are still locked
		if(m_lockedOrders != null && m_lockedOrders.size()>0)
			for(MOrder order:m_lockedOrders)
				order.unlockForInvoicing(get_Trx());

		if(inoutLines != null && inoutLines.size()>0)
			if(!PO.saveAll((Trx)null, inoutLines))
				throw new CompiereStateException("Could not save shipment lines");
		
		return "@Created@ = " + m_created;
	}	//	generate



	/**************************************************************************
	 * 	Create Invoice Line from Order Line
	 *	@param order order
	 *	@param orderLine line
	 *	@param qtyInvoiced qty
	 *	@param qtyEntered qty
	 */
	private void createLine (MOrder order, MOrderLine orderLine, 
			BigDecimal qtyInvoiced, BigDecimal qtyEntered)
	{
		if (m_invoice == null)
		{
			m_invoice = new MInvoice (order, 0, p_DateInvoiced);
			invoiceLinesToSave = new ArrayList<MInvoiceLine>();
			invoiceCashBookMap.put(m_invoice.getC_Order_ID(), order.getC_CashBook_ID());
			invoiceMap.put(m_invoice, invoiceLinesToSave);
		}
		//	
		MInvoiceLine line = new MInvoiceLine(m_invoice.getCtx(), 0, m_invoice.get_Trx());
		line.setClientOrg(m_invoice.getAD_Client_ID(), m_invoice.getAD_Org_ID());
		line.setInvoice(m_invoice);
		line.setOrderLine(orderLine);
		line.setQtyInvoiced(qtyInvoiced);

		log.info("Qty Invoiced"+line.getQtyInvoiced());
		line.setQtyEntered(qtyEntered);
		line.setLine(m_line + orderLine.getLine());
		invoiceLinesToSave.add(line);
		log.fine(line.toString());
	}	//	createLine

	/**
	 * 	Create Invoice Line from Shipment
	 *	@param order order
	 *	@param ship shipment header
	 *	@param sLine shipment line
	 */
	private void createLine (MOrder order, MInOut ship, MInOutLine sLine)
	{
		if (m_invoice == null)
		{
			m_invoice = new MInvoice (order, 0, p_DateInvoiced);
			invoiceLinesToSave = new ArrayList<MInvoiceLine>();
			invoiceCashBookMap.put(m_invoice.getC_Order_ID(), order.getC_CashBook_ID());
			invoiceMap.put(m_invoice, invoiceLinesToSave);
		}
		//	Create Shipment Comment Line
		if (m_ship == null 
				|| m_ship.getM_InOut_ID() != ship.getM_InOut_ID())
		{
			MDocType dt = MDocType.get(getCtx(), ship.getC_DocType_ID());
			if (m_bp == null || m_bp.getC_BPartner_ID() != ship.getC_BPartner_ID())
				m_bp = new MBPartner (getCtx(), ship.getC_BPartner_ID(), get_TrxName());

			//	Reference: Delivery: 12345 - 12.12.12
			MClient client = MClient.get(getCtx(), order.getAD_Client_ID ());
			String AD_Language = client.getAD_Language();
			if (client.isMultiLingualDocument() && m_bp.getAD_Language() != null)
				AD_Language = m_bp.getAD_Language();
			if (AD_Language == null)
				AD_Language = Language.getBaseAD_Language();
			java.text.SimpleDateFormat format = DisplayType.getDateFormat 
			(DisplayTypeConstants.Date, Language.getLanguage(AD_Language));
			String reference = dt.getPrintName(m_bp.getAD_Language())
			+ ": " + ship.getDocumentNo() 
			+ " - " + format.format(ship.getMovementDate());
			m_ship = ship;
			//
			MInvoiceLine line = new MInvoiceLine(m_invoice.getCtx(), 0, m_invoice.get_Trx());
			line.setClientOrg(m_invoice.getAD_Client_ID(),m_invoice.getAD_Org_ID());
			line.setInvoice(m_invoice);

			line.setIsDescription(true);
			line.setDescription(reference);
			line.setLine(m_line + sLine.getLine() - 2);
			invoiceLinesToSave.add(line);
			//	Optional Ship Address if not Bill Address
			if (order.getBill_Location_ID() != ship.getC_BPartner_Location_ID())
			{
				MLocation addr = MLocation.getBPLocation(getCtx(), ship.getC_BPartner_Location_ID(), null);
				line = new MInvoiceLine (m_invoice.getCtx(), 0, m_invoice.get_Trx());
				line.setClientOrg(m_invoice.getAD_Client_ID(),m_invoice.getAD_Org_ID());
				line.setInvoice(m_invoice);
				line.setIsDescription(true);
				line.setDescription(addr.toString());
				line.setLine(m_line + sLine.getLine() - 1);
				invoiceLinesToSave.add(line);
			}
		}

		MInvoiceLine line = new MInvoiceLine (m_invoice.getCtx(), 0, m_invoice.get_Trx());
		line.setClientOrg(m_invoice.getAD_Client_ID(), m_invoice.getAD_Org_ID());
		line.setInvoice(m_invoice);
		line.setShipLine(sLine);
		line.setQtyEntered(sLine.getQtyEntered());
		line.setQtyInvoiced(sLine.getMovementQty());
		line.setLine(m_line + sLine.getLine());
		line.setM_AttributeSetInstance_ID(sLine.getM_AttributeSetInstance_ID());
		invoiceLinesToSave.add(line);
		//	Link
		sLine.setIsInvoiced(true);
		inoutLines.add(sLine);
		if(inoutLines.size() >= COMMITCOUNT)
			if(!PO.saveAll((Trx)null,inoutLines))
				throw new CompiereStateException("Could not update shipment lines");
			else
				inoutLines.clear();
		log.fine(line.toString());
	}	//	createLine


	/**
	 * 	Complete Invoice
	 */
	private void completeInvoice()
	{
		if(invoiceMap.isEmpty())
			return;

		List<MInvoice> invoicesToSave = new ArrayList<MInvoice>(invoiceMap.keySet());
		List<MInvoice> invoicesToSaveAgain = new ArrayList<MInvoice>();
		if(!PO.saveAll(get_Trx(), invoicesToSave))
			throw new CompiereStateException("Could not save invoices");
		
		// Cash Book ID may be reset to Zero. So Verify that once and if different, update it.
		for(MInvoice invoice : invoicesToSave)
		{
			int C_CashBook_ID = invoiceCashBookMap.get(invoice.getC_Order_ID());
			if (C_CashBook_ID != invoice.getC_CashBook_ID())
			{
				invoice.setC_CashBook_ID(C_CashBook_ID);
				invoicesToSaveAgain.add(invoice);
			}
		}

		if(!PO.saveAll(get_Trx(), invoicesToSaveAgain))
			throw new CompiereStateException("Could not save invoices");

		List<MInvoiceLine> linesToSave = new ArrayList<MInvoiceLine>();
		for(Map.Entry<MInvoice, List<MInvoiceLine>> entry : invoiceMap.entrySet()){
			MInvoice invoice = entry.getKey();
			for( MInvoiceLine invoiceLine : entry.getValue()){
				invoiceLine.setC_Invoice_ID(invoice.getC_Invoice_ID());
				linesToSave.add(invoiceLine);
			}
		}

		if(!PO.saveAll(get_Trx(), linesToSave))
			throw new CompiereStateException("Could not save invoice lines");

		for(MInvoice invoice : invoiceMap.keySet()){
			boolean processOK = DocumentEngine.processIt(invoice, p_docAction);
			invoice.save(get_Trx());

			if (processOK)
				addLog(invoice.getC_Invoice_ID(), invoice.getDateInvoiced(), null, invoice.getDocumentNo());
			else
			{
				log.warning("Failed: " + invoice);
				String msg = Msg.getMsg(getCtx(), "Error") + " " + invoice.getDocumentNo();
				addLog(invoice.getC_Invoice_ID(), invoice.getDateInvoiced(), null, msg);
			}
			m_created++;
		}

		//		List<Integer> orderIDs = new ArrayList<Integer>();
		if(m_lockedOrders !=null && m_lockedOrders.size()>0){
			List<Object> orderIDs = new ArrayList<Object>();
			StringBuffer sb = new StringBuffer("UPDATE C_Order SET Invoicing='N' WHERE Invoicing='Y' AND C_Order_ID IN (");
			for(MOrder order : m_lockedOrders){
				sb.append("?,");
				orderIDs.add(order.getC_Order_ID());
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			int no = DB.executeUpdate(get_Trx(), sb.toString(), orderIDs);
			if(no == -1)
				log.warning("Failed: Could not unlock orders - "+orderIDs);
			m_lockedOrders.clear();
		}
		get_Trx().commit();
	}	//	completeInvoice

	public static void main(String[] args)
	{
		//		System.setProperty ("PropertyFile", "/home/namitha/Useful/Compiere.properties");
		Compiere.startup(true);
		CLogMgt.setLoggerLevel(Level.INFO, null);
		CLogMgt.setLevel(Level.INFO);
		//	Same Login entries as entered
		Ini.setProperty(Ini.P_UID, "GardenAdmin");
		Ini.setProperty(Ini.P_PWD, "GardenAdmin");
		Ini.setProperty(Ini.P_ROLE, "GardenWorld Admin");
		Ini.setProperty(Ini.P_CLIENT, "GardenWorld");
		Ini.setProperty(Ini.P_ORG, "HQ");
		Ini.setProperty(Ini.P_WAREHOUSE, "HQ Warehouse");
		Ini.setProperty(Ini.P_LANGUAGE, "English");
		Ini.setProperty(Ini.P_IMPORT_BATCH_SIZE, "100");

		Ctx ctx = Env.getCtx();
		Login login = new Login(ctx);
		if (!login.batchLogin(null, null))
			System.exit(1);

		//	Reduce Log level for performance
		CLogMgt.setLoggerLevel(Level.WARNING, null);
		CLogMgt.setLevel(Level.WARNING);

		//	Data from Login Context
		int AD_Client_ID = ctx.getAD_Client_ID();
		int AD_User_ID = ctx.getAD_User_ID();
		//	Hardcoded
		int AD_Process_ID = 119;
		int AD_Table_ID = 0;
		int Record_ID = 0;

		//	Step 1: Setup Process
		MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, Record_ID);
		instance.save();

		ProcessInfo pi = new ProcessInfo("Import", AD_Process_ID, AD_Table_ID, Record_ID);
		pi.setAD_Client_ID(AD_Client_ID);
		pi.setAD_User_ID(AD_User_ID);
		pi.setIsBatch(false);  //  want to wait for result
		pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());

		DB.startLoggingUpdates();

		// Step 3: Run the process directly
		InvoiceGenerate test = new InvoiceGenerate();
		test.p_ConsolidateDocument = false;
		test.p_AD_Org_ID = 11;
		test.p_docAction = "PR";

		long start = System.currentTimeMillis();

		test.startProcess(ctx, pi, null);

		long end = System.currentTimeMillis();
		long durationMS = end - start;
		long duration = durationMS/1000;
		System.out.println("Total: " + duration + "s");

		// Step 4: get results
		if (pi.isError())
			System.err.println("Error: " + pi.getSummary());
		else
			System.out.println("OK: " + pi.getSummary());
		System.out.println(pi.getLogInfo());

		// stop logging database updates
		String logResult = DB.stopLoggingUpdates(0);
		System.out.println(logResult);

	}

}	//	InvoiceGenerate
