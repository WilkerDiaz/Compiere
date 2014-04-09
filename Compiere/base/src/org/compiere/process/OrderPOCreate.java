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

import java.awt.geom.*;
import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Generate PO from Sales Order
 *	
 *  @author Jorg Janke
 *  @version $Id: OrderPOCreate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class OrderPOCreate extends SvrProcess
{
	/**	Order Date From		*/
	private Timestamp	p_DateOrdered_From;
	/**	Order Date To		*/
	private Timestamp	p_DateOrdered_To;
	/**	Customer			*/
	private int			p_C_BPartner_ID;
	/**	Vendor				*/
	private int			p_Vendor_ID;
	/**	Sales Order			*/
	private int			p_C_Order_ID;
	/** Drop Ship			*/
	private String		p_IsDropShip;

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
			else if (name.equals("DateOrdered"))
			{
				p_DateOrdered_From = (Timestamp)element.getParameter();
				p_DateOrdered_To = (Timestamp)element.getParameter_To();
			}
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("Vendor_ID"))
				p_Vendor_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("C_Order_ID"))
				p_C_Order_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("IsDropShip"))
				p_IsDropShip = (String)element.getParameter();
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
		log.info("DateOrdered=" + p_DateOrdered_From + " - " + p_DateOrdered_To 
				+ " - C_BPartner_ID=" + p_C_BPartner_ID + " - Vendor_ID=" + p_Vendor_ID
				+ " - IsDropShip=" + p_IsDropShip + " - C_Order_ID=" + p_C_Order_ID);
		if (p_C_Order_ID == 0 && p_IsDropShip == null
				&& p_DateOrdered_From == null && p_DateOrdered_To == null
				&& p_C_BPartner_ID == 0 && p_Vendor_ID == 0)
			throw new IllegalPathStateException("You need to restrict selection");
		//
		String sql = "SELECT * FROM C_Order o "
			+ " WHERE o.IsSOTrx='Y'"
			+ " AND NOT EXISTS (SELECT 1 "
			                + " FROM C_OrderLine ol "
			                + " WHERE o.C_Order_ID=ol.C_Order_ID "
			                + " AND ol.Ref_OrderLine_ID IS NOT NULL)"; 
		if (p_C_Order_ID != 0)
			sql += " AND o.C_Order_ID=?";
		else
		{
			if (p_C_BPartner_ID != 0)
				sql += " AND o.C_BPartner_ID=?";
			if (p_IsDropShip != null)
				sql += " AND o.IsDropShip=?";
			if (p_Vendor_ID != 0)
				sql += " AND EXISTS (SELECT * FROM C_OrderLine ol"
					+ " INNER JOIN M_Product_PO po ON (ol.M_Product_ID=po.M_Product_ID) "
					+ "WHERE o.C_Order_ID=ol.C_Order_ID AND po.C_BPartner_ID=?)"; 
			if (p_DateOrdered_From != null && p_DateOrdered_To != null)
				sql += "AND TRUNC(o.DateOrdered,'DD') BETWEEN ? AND ?";
			else if (p_DateOrdered_From != null && p_DateOrdered_To == null)
				sql += "AND TRUNC(o.DateOrdered,'DD') >= ?";
			else if (p_DateOrdered_From == null && p_DateOrdered_To != null)
				sql += "AND TRUNC(o.DateOrdered,'DD') <= ?";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int counter = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			if (p_C_Order_ID != 0)
				pstmt.setInt (1, p_C_Order_ID);
			else
			{
				int index = 1;
				if (p_C_BPartner_ID != 0)
					pstmt.setInt (index++, p_C_BPartner_ID);
				if (p_IsDropShip != null)
					pstmt.setString(index++, p_IsDropShip);
				if (p_Vendor_ID != 0)
					pstmt.setInt (index++, p_Vendor_ID);
				if (p_DateOrdered_From != null && p_DateOrdered_To != null)
				{
					pstmt.setTimestamp(index++, p_DateOrdered_From);
					pstmt.setTimestamp(index++, p_DateOrdered_To);
				}
				else if (p_DateOrdered_From != null && p_DateOrdered_To == null)
					pstmt.setTimestamp(index++, p_DateOrdered_From);
				else if (p_DateOrdered_From == null && p_DateOrdered_To != null)
					pstmt.setTimestamp(index++, p_DateOrdered_To);
			}
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				counter += createPOFromSO (new MOrder (getCtx(), rs, get_TrxName()));
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (counter == 0)
			log.fine(sql);
		return "@Created@ " + counter;
	}	//	doIt

	/**
	 * 	Create PO From SO
	 *	@param so sales order
	 *	@return number of POs created
	 */
	private int createPOFromSO (MOrder so)
	{
		log.info(so.toString());
		MOrderLine[] soLines = so.getLines();
		if (soLines == null || soLines.length == 0)
		{
			log.warning("No Lines - " + so);
			return 0;
		}
		//
		int counter = 0;

		//Modified the below query for request no:10020594. (added po.IsActive='Y')

		//	Order Lines with a Product which has a current vendor 
		String sql = "SELECT DISTINCT po.C_BPartner_ID, po.M_Product_ID "
			+ "FROM M_Product_PO po" 
			+ " INNER JOIN C_OrderLine ol ON (po.M_Product_ID=ol.M_Product_ID) "
			+ "WHERE ol.C_Order_ID=? AND po.IsCurrentVendor='Y' and po.IsActive='Y'";
		if(p_Vendor_ID != 0)
			sql = sql.concat(" AND po.C_BPartner_ID=? ");
		sql = sql.concat("ORDER BY 1");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MOrder po = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, so.getC_Order_ID());
			if(p_Vendor_ID != 0)
				pstmt.setInt(2, p_Vendor_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				//	New Order
				int C_BPartner_ID = rs.getInt(1);
				if (po == null || po.getBill_BPartner_ID() != C_BPartner_ID)
				{
					po = createPOForVendor(rs.getInt(1), so);
					addLog(0, null, null, po.getDocumentNo());
					counter++;
				}

				//	Line
				int M_Product_ID = rs.getInt(2);
				for (MOrderLine element : soLines) {
					if (element.getM_Product_ID() == M_Product_ID)
					{
						MOrderLine poLine = new MOrderLine (po);
						poLine.setRef_OrderLine_ID(element.getC_OrderLine_ID());
						poLine.setM_Product_ID(element.getM_Product_ID());
						poLine.setM_AttributeSetInstance_ID(element.getM_AttributeSetInstance_ID());
						poLine.setC_UOM_ID(element.getC_UOM_ID());
						poLine.setQtyEntered(element.getQtyEntered());
						poLine.setQtyOrdered(element.getQtyOrdered());
						poLine.setDescription(element.getDescription());
						poLine.setDatePromised(element.getDatePromised());
						poLine.setPrice();
						if(!poLine.save())
						{
							String AD_MessageValue = "SaveError";
							MNote note = new MNote (getCtx(), AD_MessageValue, getAD_User_ID(),
									getAD_Client_ID(), so.getAD_Org_ID(), get_Trx());
							note.setRecord(element.get_Table_ID(), element.getC_OrderLine_ID());
							String textMsg = " Unable to Save PO Line for Sales order Line "+ element.toString();
							note.setTextMsg(textMsg);
							note.save();

						}
						poLine.updateHeaderTax();
					}
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//	Set Reference to PO
		if (counter == 1 && po != null)
		{
			so.setRef_Order_ID(po.getC_Order_ID());
			so.save();
		}
		return counter;
	}	//	createPOFromSO

	/**
	 *	Create PO for Vendor
	 *	@param C_BPartner_ID vendor
	 *	@param so sales order
	 */
	public MOrder createPOForVendor(int C_BPartner_ID, MOrder so)
	{
		MOrder po = new MOrder (getCtx(), 0, get_TrxName());
		po.setClientOrg(so.getAD_Client_ID(), so.getAD_Org_ID());
		po.setRef_Order_ID(so.getC_Order_ID());
		po.setIsSOTrx(false);
		po.setC_DocTypeTarget_ID();
		//
		po.setDescription(so.getDescription());
		po.setPOReference(so.getDocumentNo());
		po.setPriorityRule(so.getPriorityRule());
		po.setSalesRep_ID(so.getSalesRep_ID());
		po.setM_Warehouse_ID(so.getM_Warehouse_ID());
		//	Set Vendor
		MBPartner vendor = new MBPartner (getCtx(), C_BPartner_ID, get_TrxName());
		po.setBPartner(vendor);
		//	Drop Ship
		po.setIsDropShip(so.isDropShip());
		if (so.isDropShip())
		{
			po.setShip_BPartner_ID(so.getC_BPartner_ID());
			po.setShip_Location_ID(so.getC_BPartner_Location_ID());
			po.setShip_User_ID(so.getAD_User_ID());
		}
		//	References
		po.setC_Activity_ID(so.getC_Activity_ID());
		po.setC_Campaign_ID(so.getC_Campaign_ID());
		po.setC_Project_ID(so.getC_Project_ID());
		po.setUser1_ID(so.getUser1_ID());
		po.setUser2_ID(so.getUser2_ID());
		//
		if(!po.save())
		{
			String AD_MessageValue = "SaveError";
			MNote note = new MNote (getCtx(), AD_MessageValue, getAD_User_ID(),
					getAD_Client_ID(), so.getAD_Org_ID(), get_Trx());
			note.setRecord(so.get_Table_ID(), so.getC_Order_ID());
			String textMsg = " Unable to Save PO Header for Sales order "+ so.toString();
			note.setTextMsg(textMsg);
			note.save();
		}
		return po;
	}	//	createPOForVendor

}	//	doIt
