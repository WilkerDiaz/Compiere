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
package org.compiere.model;

import java.sql.*;

import org.compiere.util.*;

/**
 * 	Recurring Run Model
 *
 *	@author Jorg Janke
 *	@version $Id: MRecurringRun.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MRecurringRun extends X_C_Recurring_Run
{
    /** Logger for class MRecurringRun */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRecurringRun.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MRecurringRun (Ctx ctx, int C_Recurring_Run_ID, Trx trx)
	{
		super (ctx, C_Recurring_Run_ID, trx);
	}	//	MRecurringRun

	public MRecurringRun (Ctx ctx, MRecurring recurring)
	{
		super(ctx, 0, recurring.get_Trx());
		if (recurring != null)
		{
			setAD_Client_ID(recurring.getAD_Client_ID());
			setAD_Org_ID(recurring.getAD_Org_ID());
			setC_Recurring_ID (recurring.getC_Recurring_ID ());
			setDateDoc(recurring.getDateNextRun());
		}
	}	//	MRecurringRun

	public MRecurringRun (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MRecurringRun

}	//	MRecurringRun
