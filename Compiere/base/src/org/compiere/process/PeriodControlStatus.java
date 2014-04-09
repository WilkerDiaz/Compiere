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
 *	Open/Close Period Control
 *	
 *  @author Jorg Janke
 *  @version $Id: PeriodControlStatus.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class PeriodControlStatus extends SvrProcess
{
	/** Period Control				*/
	private int 		p_C_PeriodControl_ID = 0;
	
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
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_PeriodControl_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info ("C_PeriodControl_ID=" + p_C_PeriodControl_ID);
		MPeriodControl pc = new MPeriodControl (getCtx(), p_C_PeriodControl_ID, get_TrxName());
		if (pc.get_ID() == 0)
			throw new CompiereUserException("@NotFound@  @C_PeriodControl_ID@=" + p_C_PeriodControl_ID);
		//	Permanently closed
		if (X_C_PeriodControl.PERIODACTION_PermanentlyClosePeriod.equals(pc.getPeriodStatus()))
			throw new CompiereUserException("@PeriodStatus@ = " + pc.getPeriodStatus());
		//	No Action
		if (X_C_PeriodControl.PERIODACTION_NoAction.equals(pc.getPeriodAction()))
			return "@OK@";
	
		//	Open
		if (X_C_PeriodControl.PERIODACTION_OpenPeriod.equals(pc.getPeriodAction()))
			pc.setPeriodStatus(X_C_PeriodControl.PERIODSTATUS_Open);
		//	Close
		if (X_C_PeriodControl.PERIODACTION_ClosePeriod.equals(pc.getPeriodAction()))
			pc.setPeriodStatus(X_C_PeriodControl.PERIODSTATUS_Closed);
		//	Close Permanently
		if (X_C_PeriodControl.PERIODACTION_PermanentlyClosePeriod.equals(pc.getPeriodAction()))
			pc.setPeriodStatus(X_C_PeriodControl.PERIODSTATUS_PermanentlyClosed);
		pc.setPeriodAction(X_C_PeriodControl.PERIODACTION_NoAction);
		//
		boolean ok = pc.save();
		
		//	Reset Cache
		CacheMgt.get().reset("C_PeriodControl", 0);
		CacheMgt.get().reset("C_Period", pc.getC_Period_ID());

		if (!ok)
			return "@Error@";
		return "@OK@";
	}	//	doIt

}	//	PeriodControlStatus
