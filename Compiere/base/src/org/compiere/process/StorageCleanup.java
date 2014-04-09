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
import org.compiere.vos.*;

/**
 * 	StorageCleanup
 *	
 *  @author Jorg Janke
 *  @version $Id: StorageCleanup.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class StorageCleanup extends SvrProcess
{
	/** Movement Document Type	*/
	private int	p_C_DocType_ID = 0;
	
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
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("");
		//	Clean up empty Storage
		String sql = "DELETE FROM M_StorageDetail d " +
			"WHERE NOT EXISTS ( " +
			"SELECT 1 FROM M_StorageDetail q " +
			"WHERE q.M_Product_ID = d.M_Product_ID " +
			"AND q.M_AttributeSetInstance_ID = d.M_AttributeSetInstance_ID " +
			"AND q.M_Locator_ID = d.M_Locator_ID " +
			"AND q.Qty != 0 " +
			"AND Created < addDays(SysDate,-3) " +
			")";
		int no = DB.executeUpdate(get_TrxName(), sql);
		log.info("Delete Empty #" + no);
		
		sql = "SELECT s.* "
			+ "FROM M_StorageDetail s "
			+ "WHERE AD_Client_ID = ?"
			+ " AND QtyType = 'H'"
			+ " AND Qty < 0"
			//	Instance Attribute
			+ " AND EXISTS (SELECT * FROM M_Product p"
				+ " INNER JOIN M_AttributeSet mas ON (p.M_AttributeSet_ID=mas.M_AttributeSet_ID) "
				+ "WHERE s.M_Product_ID=p.M_Product_ID AND mas.IsInstanceAttribute='Y')"
			//	Stock in same Warehouse
			+ " AND EXISTS (SELECT * FROM M_StorageDetail sw"
				+ " INNER JOIN M_Locator swl ON (sw.M_Locator_ID=swl.M_Locator_ID), M_Locator sl "
				+ "WHERE sw.Qty > 0"
				+ " AND sw.QtyType = 'H'"
				+ " AND s.M_Product_ID=sw.M_Product_ID"
				+ " AND s.M_Locator_ID=sl.M_Locator_ID"
				+ " AND sl.M_Warehouse_ID=swl.M_Warehouse_ID)"
			+ " ORDER BY s.M_Product_ID ";		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int lines = 0;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt(1, getCtx().getAD_Client_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				lines += move (new MStorageDetail(getCtx(), rs, get_TrxName()));
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return "#" + lines;
	}	//	doIt

	/**
	 * 	Move stock to location
	 *	@param targetOnHand target storage
	 *	@return no of movements
	 */
	private int move (MStorageDetail targetOnHand)
	{
		log.info(targetOnHand.toString());
		BigDecimal qty = targetOnHand.getQty().negate();

		//	Create Movement
		MMovement mh = new MMovement (getCtx(), 0, get_TrxName());
		mh.setAD_Org_ID(targetOnHand.getAD_Org_ID());
		mh.setC_DocType_ID(p_C_DocType_ID);
		mh.setDescription(getName());
		if (!mh.save())
			return 0;

		int lines = 0;
		List<MStorageDetail> sources = getSources(targetOnHand.getM_Product_ID(), targetOnHand.getM_Locator_ID());
		for (MStorageDetail sourceOnHand : sources) {
			//	Movement Line
			MMovementLine ml = new MMovementLine(mh);
			ml.setM_Product_ID(targetOnHand.getM_Product_ID());
			ml.setM_LocatorTo_ID(targetOnHand.getM_Locator_ID());
			ml.setM_AttributeSetInstanceTo_ID(targetOnHand.getM_AttributeSetInstance_ID());
			//	From
			ml.setM_Locator_ID(sourceOnHand.getM_Locator_ID());
			ml.setM_AttributeSetInstance_ID(sourceOnHand.getM_AttributeSetInstance_ID());
			
			BigDecimal qtyMove = qty;
			if (qtyMove.compareTo(sourceOnHand.getQty()) > 0)
				qtyMove = sourceOnHand.getQty();
			ml.setMovementQty(qtyMove);
			//
			lines++;
			ml.setLine(lines*10);
			if (!ml.save())
				return 0;
			
			qty = qty.subtract(qtyMove);
			if (qty.signum() <= 0)
				break;
		}	//	for all movements
		
		//	Process
		DocumentEngine.processIt(mh, DocActionConstants.ACTION_Complete);
		mh.save();
		
		addLog(0, null, new BigDecimal(lines), "@M_Movement_ID@ " + mh.getDocumentNo() + " (" 
			+ MRefList.get(getCtx(), X_Ref__Document_Status.AD_Reference_ID, 
				mh.getDocStatus(), get_TrxName()) + ")");

		eliminateReservation(targetOnHand);
		return lines;
	}	//	move

	/**
	 * 	Eliminate Reserved/Ordered
	 *	@param targetOnHand target Storage
	 */
	private void eliminateReservation(MStorageDetail targetOnHand)
	{
		//	Negative Ordered / Reserved Qty
		MStorageDetail targetReserved = MStorageDetail.getForRead(getCtx(), 
				targetOnHand.getM_Locator_ID(), targetOnHand.getM_Product_ID(), 
				targetOnHand.getM_AttributeSetInstance_ID(), X_Ref_Quantity_Type.RESERVED, get_TrxName());
		
		MStorageDetail targetOrdered = MStorageDetail.getForRead(getCtx(), 
				targetOnHand.getM_Locator_ID(), targetOnHand.getM_Product_ID(), 
				targetOnHand.getM_AttributeSetInstance_ID(), X_Ref_Quantity_Type.ORDERED, get_TrxName());
		
		if (targetReserved.getQty().signum() != 0 || targetOrdered.getQty().signum() != 0)
		{
			int M_Locator_ID = targetOnHand.getM_Locator_ID();
			MStorageDetail storageOnHand = MStorageDetail.getForRead(getCtx(), M_Locator_ID, 
					targetOnHand.getM_Product_ID(), 0, X_Ref_Quantity_Type.ON_HAND, get_TrxName());
			if (storageOnHand == null)
			{
				MLocator defaultLoc = MLocator.getDefault(getCtx(), M_Locator_ID);
				if (M_Locator_ID != defaultLoc.getM_Locator_ID())
				{
					M_Locator_ID = defaultLoc.getM_Locator_ID();
					storageOnHand  = MStorageDetail.getForRead(getCtx(), M_Locator_ID, 
							targetOnHand.getM_Product_ID(), 0, X_Ref_Quantity_Type.ON_HAND, get_TrxName());
				}
			}
			if (storageOnHand != null)
			{
				BigDecimal reserved = Env.ZERO;
				BigDecimal ordered = Env.ZERO;
				
				MStorageDetail storageReserved = MStorageDetail.getForRead(getCtx(), 
						storageOnHand.getM_Locator_ID(), storageOnHand.getM_Product_ID(), 
						targetOnHand.getM_AttributeSetInstance_ID(), X_Ref_Quantity_Type.RESERVED, get_TrxName());
				
				MStorageDetail storageOrdered = MStorageDetail.getForRead(getCtx(), 
						storageOnHand.getM_Locator_ID(), storageOnHand.getM_Product_ID(), 
						storageOnHand.getM_AttributeSetInstance_ID(), X_Ref_Quantity_Type.ORDERED, get_TrxName());
				
				if (targetReserved.getQty().add(storageReserved.getQty()).signum() >= 0)
					reserved = targetReserved.getQty();		//	negative
				if (targetOrdered.getQty().add(storageOrdered.getQty()).signum() >= 0)
					ordered = targetOrdered.getQty();		//	negative
				//	Eliminate Reservation
				if (reserved.signum() != 0 || ordered.signum() != 0)
				{
					if (Storage.addQtys(getCtx(), targetOnHand.getM_Warehouse_ID(), targetOnHand.getM_Locator_ID(), 
						targetOnHand.getM_Product_ID(), 
						targetOnHand.getM_AttributeSetInstance_ID(), 
						Env.ZERO, reserved.negate(), ordered.negate(), get_TrxName()))
					{
						if (Storage.addQtys(getCtx(), storageOnHand.getM_Warehouse_ID(), storageOnHand.getM_Locator_ID(), 
								storageOnHand.getM_Product_ID(), 
								storageOnHand.getM_AttributeSetInstance_ID(), 
								Env.ZERO, reserved, ordered, get_TrxName()))
							log.info("Reserved=" + reserved + ",Ordered=" + ordered);
						else
							log.warning("Failed Storage0 Update");
					}
					else
						log.warning("Failed Target Update");
				}
			}
		}
	}	//	eliminateReservation
	
	/**
	 * 	Get Storage Sources
	 *	@param M_Product_ID product
	 *	@param M_Locator_ID locator
	 *	@return sources
	 */
	private List<MStorageDetail> getSources (int M_Product_ID, int M_Locator_ID)
	{
		List<MStorageDetail> list = new ArrayList<MStorageDetail>();

		String sql = "SELECT s.* "
			+ "FROM M_StorageDetail s "
			+ "WHERE Qty > 0"
			+ " AND QtyType = 'H'"
			+ " AND M_Product_ID=?"
			//	Empty ASI
			+ " AND (M_AttributeSetInstance_ID=0"
			+ " OR EXISTS (SELECT * FROM M_AttributeSetInstance asi "
				+ "WHERE s.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID"
				+ " AND asi.Description IS NULL) )"
			//	Stock in same Warehouse
			+ " AND EXISTS (SELECT * FROM M_Locator sl, M_Locator x "
				+ "WHERE s.M_Locator_ID=sl.M_Locator_ID"
				+ " AND x.M_Locator_ID=?"
				+ " AND sl.M_Warehouse_ID=x.M_Warehouse_ID) "
			+ "ORDER BY M_AttributeSetInstance_ID";		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, M_Product_ID);
			pstmt.setInt (2, M_Locator_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MStorageDetail(getCtx(), rs, get_TrxName()));
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return list;
	}	//	getSources
	
}	//	StorageCleanup
