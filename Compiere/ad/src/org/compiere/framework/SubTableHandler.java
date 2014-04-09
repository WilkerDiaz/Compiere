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

package org.compiere.framework;

import java.util.*;

import org.compiere.model.*;

/**
 * 	Sub Table Handler
 *	@author Jorg Janke
 */
class SubTableHandler
{
	/** Insert Action				*/
	static final int 		Action_Insert = 1;
	/** Update Action				*/
	static final int 		Action_Update = 2;
	/** Delete Action				*/
	static final int 		Action_Delete = 3;
	
	/**
	 * 	Handle Sub Tables
	 *	@param po entity
	 *	@param action see Action_ constants
	 *	@return true if handled
	 */
	static public boolean handle (PO po, int action)
	{
		int Base_Table_ID = po.get_Table_ID();
		ArrayList<MTable> subTables = MTable.getSubTables(po.getCtx(), Base_Table_ID);
		for (MTable subTable : subTables)
		{
			SubTableHandler sub = new SubTableHandler(po, subTable);
			String type = subTable.getSubTableType();
			if (action == Action_Insert)
			{
				if (X_AD_Table.SUBTABLETYPE_History_Daily.equals(type))
					sub.handleHistoryDaily();
				else if (X_AD_Table.SUBTABLETYPE_History_Each.equals(type))
					sub.handleHistoryEach();
			}
			else if (action == Action_Update)
			{
				if (X_AD_Table.SUBTABLETYPE_History_Daily.equals(type))
					sub.handleHistoryDaily();
				else if (X_AD_Table.SUBTABLETYPE_History_Each.equals(type))
					sub.handleHistoryEach();
			}
			else if (action == Action_Delete)
			{
			}
			else
			{
			//	log("Invalid Action=" + action);
				return false;
			}
		}	//	for all sub tables
		return true;
	}	//	handle
	
	
	
	
	
	/**************************************************************************
	 * 	Cosntructor
	 *	@param basePO base PO
	 *	@param subTable 
	 */
	SubTableHandler (PO basePO, MTable subTable)
	{
		m_basePO = basePO;
		m_subTable = subTable;
	}	//	SubTableHandler
	
	private PO 		m_basePO;
	private MTable	m_subTable;
	
	/**
	 * 	Copy all into History Table
	 *	@return true if saved
	 */
	boolean handleHistoryEach()
	{
		PO subPO = m_subTable.getPO(m_basePO.getCtx(), 0, m_basePO.get_Trx());
		PO.copyValues(m_basePO, subPO);
		subPO.set_ValueNoCheck("Created", m_basePO.getUpdated());
		subPO.set_ValueNoCheck("CreatedBy", m_basePO.getUpdatedBy());
		return subPO.save();
	}	//	handleHistoryEach

	/**
	 * 	Copy into Daily History Table
	 *	@return true if saved
	 */
	boolean handleHistoryDaily()
	{
		PO subPO = m_subTable.getPO(m_basePO.getCtx(), 0, m_basePO.get_Trx());
		
		
		
		
		PO.copyValues(m_basePO, subPO);
		subPO.set_ValueNoCheck("Created", m_basePO.getUpdated());
		subPO.set_ValueNoCheck("CreatedBy", m_basePO.getUpdatedBy());
		return subPO.save();
	}	//	handleHistoryDaily

}	//	SubTableHandler
