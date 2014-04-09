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

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Create BP Contact, Account, Location
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class LeadBPartner extends SvrProcess
{
	/** Lead				*/
	private int p_C_Lead_ID = 0;

	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare()
	{
		p_C_Lead_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Create BPartner
	 *	@return BPartner
	 *	@throws Exception
	 */
	@Override
	protected String doIt()
	    throws Exception
	{
		log.info("C_Lead_ID=" + p_C_Lead_ID);
		if (p_C_Lead_ID == 0)
			throw new CompiereUserException("@C_Lead_ID@ ID=0");
		MLead lead = new MLead (getCtx(), p_C_Lead_ID, get_TrxName());
		if (lead.get_ID() != p_C_Lead_ID)
			throw new CompiereUserException("@NotFound@: @C_Lead_ID@ ID=" + p_C_Lead_ID);
		//
		String retValue = lead.createBP();
		if (retValue != null)
			throw new CompiereSystemException(retValue);
		lead.save();
		//
		MBPartner bp = lead.getBPartner();
		if (bp != null)
			return "@C_BPartner_ID@: " + bp.getName();
		MUser user = lead.getUser();
		if (user != null)
			return "@AD_User_ID@: " + user.getName();
		return "@SaveError@";
	}	//	doIt

}
