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
import org.compiere.util.*;

/**
 * 	Create Project from Lead
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class LeadProject extends SvrProcess
{
	/** Project Type		*/
	private int p_C_ProjectType_ID = 0;
	/** Lead				*/
	private int p_C_Lead_ID = 0;
	
	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_ProjectType_ID"))
				p_C_ProjectType_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_Lead_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return summary
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("C_Lead_ID=" + p_C_Lead_ID + ",C_ProjectType_ID=" + p_C_ProjectType_ID);
		if (p_C_Lead_ID == 0)
			throw new CompiereUserException("@C_Lead_ID@ ID=0");
		if (p_C_ProjectType_ID == 0)
			throw new CompiereUserException("@C_ProjectType_ID@ ID=0");
		MLead lead = new MLead (getCtx(), p_C_Lead_ID, get_TrxName());
		if (lead.get_ID() != p_C_Lead_ID)
			throw new CompiereUserException("@NotFound@: @C_Lead_ID@ ID=" + p_C_Lead_ID);
		//
		String retValue = lead.createProject(p_C_ProjectType_ID);
		if (retValue != null)
			throw new CompiereSystemException(retValue);
		lead.save();
		MProject project = lead.getProject();
		//
		return "@C_Project_ID@ " + project.getName();
	}	//	doIt

}	//	LeadProject
