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
 *	Update existing Inventory Count List with current Book value
 *	
 *  @author Jorg Janke
 *  @version $Id: InventoryCountUpdate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class InventoryCountUpdate extends SvrProcess
{
	/** Physical Inventory		*/
	private int		p_M_Inventory_ID = 0;
	/** Update to What			*/
	private boolean	p_InventoryCountSetZero = false;
	
	/**
	 *  Prepare - e.g., get Parameters.
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
			else if (name.equals("InventoryCountSet"))
				p_InventoryCountSetZero = "Z".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_M_Inventory_ID = getRecord_ID();
	}	//	prepare

	
	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("M_Inventory_ID=" + p_M_Inventory_ID);
		MInventory inventory = new MInventory (getCtx(), p_M_Inventory_ID, get_TrxName());
		if (inventory.get_ID() == 0)
			throw new CompiereSystemException ("Not found: M_Inventory_ID=" + p_M_Inventory_ID);

		//	Multiple Lines for one item
		//jz simple the SQL so that Derby also like it. To avoid testing Oracle by now, leave no change for Oracle
		String sql = null;
		if (DB.isOracle())
		{
			sql = "UPDATE M_InventoryLine SET IsActive='N' "
				+ "WHERE M_Inventory_ID=?"
				+ " AND (M_Product_ID, M_Locator_ID, M_AttributeSetInstance_ID) IN "
					+ "(SELECT M_Product_ID, M_Locator_ID, M_AttributeSetInstance_ID "
					+ "FROM M_InventoryLine "
					+ "WHERE M_Inventory_ID=?"
					+ " GROUP BY M_Product_ID, M_Locator_ID, M_AttributeSetInstance_ID "
					+ "HAVING COUNT(*) > 1)";
		}
		else
		{
			sql = "UPDATE M_InventoryLine SET IsActive='N' "
				+ "WHERE M_Inventory_ID=?"
				+ " AND EXISTS "
					+ "(SELECT COUNT(*) "
					+ "FROM M_InventoryLine "
					+ "WHERE M_Inventory_ID=?"
					+ " GROUP BY M_Product_ID, M_Locator_ID, M_AttributeSetInstance_ID "
					+ "HAVING COUNT(*) > 1)";
		}
		int multiple = DB.executeUpdate(get_TrxName(), sql, new Object[] {p_M_Inventory_ID, p_M_Inventory_ID});
		log.info("Multiple=" + multiple);

		int delMA = MInventoryLineMA.deleteInventoryMA(p_M_Inventory_ID, get_TrxName());
		log.info("DeletedMA=" + delMA);

		//	ASI
		sql = "UPDATE M_InventoryLine l "
			+ "SET (QtyBook,QtyCount) = "
				+ "(SELECT Qty,Qty FROM M_StorageDetail s "
				+ "WHERE s.QtyType='H' AND s.M_Product_ID=l.M_Product_ID AND s.M_Locator_ID=l.M_Locator_ID"
				+ " AND s.M_AttributeSetInstance_ID=l.M_AttributeSetInstance_ID),"
			+ " Updated=SysDate,"
			+ " UpdatedBy=?"
			//
			+ " WHERE M_Inventory_ID=?"
			+ " AND EXISTS (SELECT * FROM M_StorageDetail s "
				+ "WHERE s.M_Product_ID=l.M_Product_ID AND s.M_Locator_ID=l.M_Locator_ID"
				+ " AND s.M_AttributeSetInstance_ID=l.M_AttributeSetInstance_ID)";
		int no = DB.executeUpdate(get_TrxName(), sql, new Object[] { getAD_User_ID(), p_M_Inventory_ID});
		log.info("Update with ASI=" + no);

		//	No ASI
		int noMA = updateWithMA();

		//	Set Count to Zero
		if (p_InventoryCountSetZero)
		{
			sql = "UPDATE M_InventoryLine l "
				+ "SET QtyCount=0 "
				+ "WHERE M_Inventory_ID= ? ";
			no = DB.executeUpdate(get_TrxName(), sql,p_M_Inventory_ID);
			log.info("Set Cont to Zero=" + no);
		}
		
		if (multiple > 0)
			return "@M_InventoryLine_ID@ - #" + (no + noMA) + " --> @InventoryProductMultiple@";
		
		return "@M_InventoryLine_ID@ - #" + (no + noMA);
	}	//	doIt

	/**
	 * 	Update Inventory Lines With Material Allocation
	 *	@return no updated
	 */
	private int updateWithMA()
	{
		int no = 0;
		//
		String sql = "SELECT * FROM M_InventoryLine WHERE M_Inventory_ID=? AND COALESCE(M_AttributeSetInstance_ID,0)=0 ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, p_M_Inventory_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MInventoryLine il = new MInventoryLine (getCtx(), rs, get_TrxName());
				BigDecimal onHand = Env.ZERO;
				List<Storage.Record> storages = Storage.getAll(getCtx(), il.getM_Product_ID(), il.getM_Locator_ID(), null, get_TrxName());
				MInventoryLineMA ma = null;
				for (Storage.Record storage : storages) {
					if (storage.getQtyOnHand().signum() == 0)
						continue;
					onHand = onHand.add(storage.getQtyOnHand());
					//	No ASI
					if (storage.getM_AttributeSetInstance_ID() == 0 
						&& storages.size() == 1)
						continue;
					//	Save ASI
					ma = new MInventoryLineMA (il, 
						storage.getM_AttributeSetInstance_ID(), storage.getQtyOnHand());
					if (!ma.save())
						;
				}
				il.setQtyBook(onHand);
				il.setQtyCount(onHand);
				if (il.save())
					no++;
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		log.info("#" + no);
		return no;
	}	//	updateWithMA
	
	
}	//	InventoryCountUpdate
