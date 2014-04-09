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
import java.util.logging.*;

import org.compiere.framework.*;
import org.compiere.model.*;

/**
 *	Reindex all Content
 *	
 *  @author Yves Sandfort
 *  @version $Id$
 */
public class KIndexRerun extends SvrProcess
{
	/**	WebProject Parameter		*/
	private int		p_CM_WebProject_ID = 0;

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
			else if (name.equals("CM_WebProject_ID"))
				p_CM_WebProject_ID = ((BigDecimal)element.getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		// ReIndex Container
		int[] containers = PO.getAllIDs("CM_Container","CM_WebProject_ID=" + p_CM_WebProject_ID, get_TrxName());
		for (int element : containers) {
			MContainer thisContainer = new MContainer(getCtx(),element, get_TrxName());
			thisContainer.reIndex(false);
		}
		// ReIndex News
		int[] newsChannels = PO.getAllIDs("CM_NewsChannel","CM_WebProject_ID=" + p_CM_WebProject_ID, get_TrxName());
		for (int element : newsChannels) {
			MNewsChannel thisChannel = new MNewsChannel(getCtx(),element, get_TrxName());
			thisChannel.reIndex(false);
			int[] newsItems = PO.getAllIDs("CM_NewsItem","CM_NewsChannel_ID=" + element, get_TrxName());
			for (int element2 : newsItems) {
				MNewsItem thisItem = new MNewsItem(getCtx(), element2, get_TrxName());
				thisItem.reIndex(false);
			}
		}
		return "finished...";
	}	//	doIt

}	//	KIndexRerun
