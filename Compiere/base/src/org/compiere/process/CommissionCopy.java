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
 *	Copy Commission	
 *	
 *  @author Jorg Janke
 *  @version $Id: CommissionCopy.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class CommissionCopy extends SvrProcess
{
	/**	From Commission			*/
	private int 	p_C_Commission_ID = 0;
	/** To Commission			*/
	private int		p_C_CommissionTo_ID = 0;
	
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
			else if (name.equals("C_Commission_ID"))
				p_C_Commission_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_C_CommissionTo_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process - copy
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("doIt - C_Commission_ID=" + p_C_Commission_ID + " - copy to " + p_C_CommissionTo_ID);
		MCommission comFrom = new MCommission (getCtx(), p_C_Commission_ID, get_TrxName());
		if (comFrom.get_ID() == 0)
			throw new CompiereUserException ("No From Commission");
		MCommission comTo = new MCommission (getCtx(), p_C_CommissionTo_ID, get_TrxName());
		if (comTo.get_ID() == 0)
			throw new CompiereUserException ("No To Commission");
		
		//
		int no = comTo.copyLinesFrom(comFrom);
		return "@Copied@: " + no;
	}	//	doIt

}	//	CommissionCopy
