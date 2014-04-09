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
 *	Re-Open Request
 *	
 *  @author Jorg Janke
 *  @version $Id: RequestReOpen.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class RequestReOpen extends SvrProcess
{
	/** Request					*/
	private int	p_R_Request_ID = 0;
	
	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("R_Request_ID"))
				p_R_Request_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process It
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		MRequest request = new MRequest (getCtx(), p_R_Request_ID, get_TrxName());
		log.info(request.toString());
		if (request.get_ID() == 0)
			throw new CompiereUserException("@NotFound@ @R_Request_ID@ " + p_R_Request_ID);
		
		request.setR_Status_ID();	//	set default status
		request.setProcessed(false);
		if (request.save() && !request.isProcessed())
			return "@OK@";
		return "@Error@";
	}	//	doIt

}	//	RequestReOpen
