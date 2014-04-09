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
package org.compiere.wf;

import java.util.logging.*;

import org.compiere.process.*;
import org.compiere.util.*;

/**
 *	Move Workflow Customizations to Client
 *	
 *  @author Jorg Janke
 *  @version $Id: WorkflowMoveToClient.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class WorkflowMoveToClient extends SvrProcess
{
	/**	The new Client			*/
	private int		p_AD_Client_ID = 0;
	/** The Workflow			*/
	private int		p_AD_Workflow_ID = 0;	

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
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = element.getParameterAsInt();
			else if (name.equals("AD_Workflow_ID"))
				p_AD_Workflow_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
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
		log.info("doIt - AD_Client_ID=" + p_AD_Client_ID + ", AD_Workflow_ID=" + p_AD_Workflow_ID);
		
		int changes = 0;
		//	WF
		String sql = "UPDATE AD_Workflow SET AD_Client_ID=" + p_AD_Client_ID
			+ " WHERE AD_Client_ID=0 AND EntityType NOT IN ('D','C')"
			+ " AND AD_Workflow_ID=" + p_AD_Workflow_ID;
		int no = DB.executeUpdate(get_TrxName(), sql);
		if (no == -1)
			throw new CompiereSystemException ("Error updating Workflow");
		changes += no;
		
		//	Node
		sql = "UPDATE AD_WF_Node SET AD_Client_ID=" + p_AD_Client_ID
			+ " WHERE AD_Client_ID=0 AND EntityType NOT IN ('D','C')"
			+ " AND AD_Workflow_ID=" + p_AD_Workflow_ID;
		no = DB.executeUpdate(get_TrxName(), sql);
		if (no == -1)
			throw new CompiereSystemException ("Error updating Workflow Node");
		changes += no;

		//	Node Next
		sql = "UPDATE AD_WF_NodeNext SET AD_Client_ID=" + p_AD_Client_ID
			+ " WHERE AD_Client_ID=0 AND EntityType NOT IN ('D','C')"
			+ " AND (AD_WF_Node_ID IN (SELECT AD_WF_Node_ID FROM AD_WF_Node WHERE AD_Workflow_ID=" + p_AD_Workflow_ID
				+ ") OR AD_WF_Next_ID IN (SELECT AD_WF_Node_ID FROM AD_WF_Node WHERE AD_Workflow_ID=" + p_AD_Workflow_ID 
				+ "))";
		no = DB.executeUpdate(get_TrxName(), sql);
		if (no == -1)
			throw new CompiereSystemException ("Error updating Workflow Transition");
		changes += no;

		//	Node Parameters
		sql = "UPDATE AD_WF_Node_Para SET AD_Client_ID=" + p_AD_Client_ID
			+ " WHERE AD_Client_ID=0 AND EntityType NOT IN ('D','C')"
			+ " AND AD_WF_Node_ID IN (SELECT AD_WF_Node_ID FROM AD_WF_Node WHERE AD_Workflow_ID=" + p_AD_Workflow_ID 
			+ ")";
		no = DB.executeUpdate(get_TrxName(), sql);
		if (no == -1)
			throw new CompiereSystemException ("Error updating Workflow Node Parameters");
		changes += no;

		//	Node Next Condition
		sql = "UPDATE AD_WF_NextCondition SET AD_Client_ID=" + p_AD_Client_ID
			+ " WHERE AD_Client_ID=0 AND EntityType NOT IN ('D','C')"
			+ " AND AD_WF_NodeNext_ID IN ("
				+ "SELECT AD_WF_NodeNext_ID FROM AD_WF_NodeNext "
				+ "WHERE AD_WF_Node_ID IN (SELECT AD_WF_Node_ID FROM AD_WF_Node WHERE AD_Workflow_ID=" + p_AD_Workflow_ID
				+ ") OR AD_WF_Next_ID IN (SELECT AD_WF_Node_ID FROM AD_WF_Node WHERE AD_Workflow_ID=" + p_AD_Workflow_ID
				+ "))";
		no = DB.executeUpdate(get_TrxName(), sql);
		if (no == -1)
			throw new CompiereSystemException ("Error updating Workflow Transition Condition");
		changes += no;
		
		return "@Updated@ - #" + changes;
	}	//	doIt

}	//	WorkflowMoveToClient
