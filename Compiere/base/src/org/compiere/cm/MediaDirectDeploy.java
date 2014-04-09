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
package org.compiere.cm;

import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;

/**
 * 	CM Template Validation Process
 *	
 *  @author Yves Sandfort
 */
public class MediaDirectDeploy extends SvrProcess
{
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			//else if (name.equals("CM_WebProject_ID"))
				//p_CM_WebProject_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	} // prepare

	@Override
	protected String doIt ()
		throws Exception
	{
		MMedia thisMedia = new MMedia(getCtx(), getRecord_ID(), get_TrxName());
		MMediaServer[] theseServers = MMediaServer.getMediaServer (thisMedia.getWebProject ());
		if (theseServers!=null)
		{
			for (MMediaServer element : theseServers) {
				MMediaDeploy thisDeploy = MMediaDeploy.getByMedia (getCtx(), getRecord_ID(), element.get_ID(), true, get_TrxName());
				thisDeploy.setIsDeployed (false);
				thisDeploy.save(get_TrxName());
				thisDeploy.load (get_TrxName());
				get_TrxName().commit ();
				element.deploy ();
			}
		}
		return null;
	}
	
}	//	TemplateValidate
