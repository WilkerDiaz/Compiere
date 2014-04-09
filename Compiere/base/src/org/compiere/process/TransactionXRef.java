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

import java.util.logging.*;

import org.compiere.util.*;


/**
 *	Material Transaction Cross Reference
 *	
 *  @author Jorg Janke
 *  @version $Id: TransactionXRef.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class TransactionXRef extends SvrProcess
{
	private int		p_Search_InOut_ID = 0;
	private int 	p_Search_Order_ID = 0;
	private int		p_Search_Invoice_ID = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("Search_InOut_ID"))
				p_Search_InOut_ID = element.getParameterAsInt();
			else if (name.equals("Search_Order_ID"))
				p_Search_Order_ID = element.getParameterAsInt();
			else if (name.equals("Search_Invoice_ID"))
				p_Search_Invoice_ID = element.getParameterAsInt();
			else if (name.equals("#AD_PrintFormat_ID"))
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process it
	 *	@return info
	 *	@throws Exception if error
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("M_InOut_ID=" + p_Search_InOut_ID + ", C_Order_ID=" + p_Search_Order_ID
			+ ", C_Invoice_ID=" + p_Search_Invoice_ID);
		//
		if (p_Search_InOut_ID != 0)
			insertTrx(
				"SELECT NVL(ma.M_AttributeSetInstance_ID,iol.M_AttributeSetInstance_ID) "
				+ "FROM M_InOutLine iol"
				+ " LEFT OUTER JOIN M_InOutLineMA ma ON (iol.M_InOutLine_ID=ma.M_InOutLine_ID) "
				+ "WHERE M_InOut_ID=" + p_Search_InOut_ID
				);
		else if (p_Search_Order_ID != 0)
			insertTrx(
				"SELECT NVL(ma.M_AttributeSetInstance_ID,iol.M_AttributeSetInstance_ID) "
				+ "FROM M_InOutLine iol"
				+ " LEFT OUTER JOIN M_InOutLineMA ma ON (iol.M_InOutLine_ID=ma.M_InOutLine_ID) "
				+ " INNER JOIN M_InOut io ON (iol.M_InOut_ID=io.M_InOut_ID)"
				+ "WHERE io.C_Order_ID=" + p_Search_Order_ID
				);
		else if (p_Search_Invoice_ID != 0)
			insertTrx(
				"SELECT NVL(ma.M_AttributeSetInstance_ID,iol.M_AttributeSetInstance_ID) "
				+ "FROM M_InOutLine iol"
				+ " LEFT OUTER JOIN M_InOutLineMA ma ON (iol.M_InOutLine_ID=ma.M_InOutLine_ID) "
				+ " INNER JOIN C_InvoiceLine il ON (iol.M_InOutLine_ID=il.M_InOutLine_ID) "
				+ "WHERE il.C_Invoice_ID=" + p_Search_Invoice_ID
				);
		else
			throw new CompiereUserException("Select one Parameter");
		//
		return "";
	}	//	doIt
	
	/**
	 * 	Get Trx
	 *	@param sqlSubSelect sql
	 */
	private void insertTrx (String sqlSubSelect)
	{
		String sql = "INSERT INTO T_Transaction "
			+ "(AD_PInstance_ID, M_Transaction_ID,"
			+ " AD_Client_ID, AD_Org_ID, IsActive, Created,CreatedBy, Updated,UpdatedBy,"
			+ " MovementType, M_Locator_ID, M_Product_ID, M_AttributeSetInstance_ID,"
			+ " MovementDate, MovementQty,"
			+ " M_InOutLine_ID, M_InOut_ID,"
			+ " M_MovementLine_ID, M_Movement_ID,"
			+ " M_InventoryLine_ID, M_Inventory_ID, "
			+ " C_ProjectIssue_ID, C_Project_ID, "
			+ " M_ProductionLine_ID, M_Production_ID ";
		
		if(p_Search_Order_ID != 0)
			sql += ", Search_Order_ID ";
		if(p_Search_Invoice_ID != 0)
			sql += ", Search_Invoice_ID ";
		if(p_Search_InOut_ID != 0)
			sql += ", Search_InOut_ID ";
		
		
			
			
			/*+ " Search_Order_ID, Search_Invoice_ID, Search_InOut_ID) "*/
			//	Data
		sql +=	 ") SELECT " + getAD_PInstance_ID() + ", M_Transaction_ID,"
			+ " AD_Client_ID, AD_Org_ID, IsActive, Created,CreatedBy, Updated,UpdatedBy,"
			+ " MovementType, M_Locator_ID, M_Product_ID, M_AttributeSetInstance_ID,"
			+ " MovementDate, MovementQty,"
			+ " M_InOutLine_ID, M_InOut_ID, "
			+ " M_MovementLine_ID, M_Movement_ID,"
			+ " M_InventoryLine_ID, M_Inventory_ID, "
			+ " C_ProjectIssue_ID, C_Project_ID, "
			+ " M_ProductionLine_ID, M_Production_ID ";
		
			//	Parameter
		if(p_Search_Order_ID != 0)
			sql += ", " + p_Search_Order_ID;
		if(p_Search_Invoice_ID != 0)
			sql += ", " + p_Search_Invoice_ID;
		if(p_Search_InOut_ID != 0)
			sql += ", "+ p_Search_InOut_ID;

			//+ p_Search_Order_ID + ", " + p_Search_Invoice_ID + "," + p_Search_InOut_ID + " "
			//
		sql += " FROM M_Transaction_v "
			+ "WHERE M_AttributeSetInstance_ID > 0 AND M_AttributeSetInstance_ID IN (" 
			+ sqlSubSelect
			+ ") ORDER BY M_Transaction_ID";
		//
		int no = DB.executeUpdate(get_TrxName(), sql);
		log.fine(sql);
		log.config("#" + no);
		
		//	Multi-Level
		
	}	//	insertTrx
	
}	//	TransactionXRef
