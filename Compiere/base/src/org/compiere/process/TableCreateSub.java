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

import org.compiere.model.*;
import org.compiere.tools.*;
import org.compiere.util.*;

/**
 * 	Create Sub Table
 *	@author Jorg Janke
 */
public class TableCreateSub extends SvrProcess
{
	/**	Sub Table Type			*/
	private String		p_SubTableType = null;
	/** Base Table				*/
	private int			p_Base_Table_ID = 0;
	/** Date Column				*/
	private int			p_DateColumn_ID = 0;
	
	/**
	 * 	Get Parameters
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
		//	else if (name.equals("EntityType"))
		//		p_EntityType = (String)para[i].getParameter();
			else if (name.equals("SubTableType"))
				p_SubTableType = (String)element.getParameter();
			else if (name.equals("Base_Table_ID"))
				p_Base_Table_ID = element.getParameterAsInt();
			else if (name.equals("DateColumn_ID"))
				p_DateColumn_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process it
	 * 	@return summary
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("SubTableType=" + p_SubTableType 
			+ ",Base_Table_ID=" + p_Base_Table_ID + ",DateColumn_ID=" + p_DateColumn_ID);
		if (p_Base_Table_ID == 0 || p_DateColumn_ID == 0)
			throw new CompiereUserException("No Base Table / Date Column");
		MTable base = new MTable(getCtx(), p_Base_Table_ID, null);
		if (base.get_ID() != p_Base_Table_ID)
			throw new CompiereUserException("@NotFound@ @Base_Table_ID@ ID=" + p_Base_Table_ID);
		MColumn dateCol = new MColumn(getCtx(), p_DateColumn_ID, null);
		if (dateCol.get_ID() != p_DateColumn_ID)
			throw new CompiereUserException("@NotFound@ @DateColumn_ID@ ID=" + p_DateColumn_ID);
		if (dateCol.getAD_Table_ID() != base.getAD_Table_ID())
			throw new CompiereUserException("@NotFound@ @DateColumn_ID@ - @Base_Table_ID@");
		//
		MTable table = new MTable(getCtx(), 0, get_TrxName());
		table.setBase_Table_ID(p_Base_Table_ID);
		table.setSubTableType(p_SubTableType);
		
		//	History
		if (X_AD_Table.SUBTABLETYPE_History_Daily.equals(p_SubTableType)
			|| X_AD_Table.SUBTABLETYPE_History_Each.equals(p_SubTableType))
		{
			SubTableUtil hist = new SubTableUtil(getCtx(), base.getTableName());
			log.finest(hist.toString());
		}
		//	Delta	
		else if (X_AD_Table.SUBTABLETYPE_Delta_System.equals(p_SubTableType)
			|| X_AD_Table.SUBTABLETYPE_Delta_User.equals(p_SubTableType))
		{
			boolean userDef = true;
			SubTableUtil delta = new SubTableUtil(getCtx(), base.getTableName(), userDef);
			log.finest(delta.toString());			
		}
		else
			throw new CompiereSystemException("Not supported SubTableType=" + p_SubTableType);
		
		return "NIY";
	}	//	doIt


}	//	TableCreateSub

