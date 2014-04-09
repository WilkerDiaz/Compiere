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
 *	Scheduler Log
 *	
 *  @author Jorg Janke
 *  @version $Id: MSchedulerLog.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MSchedulerLog extends X_AD_SchedulerLog
	implements CompiereProcessorLog
{
    /** Logger for class MSchedulerLog */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MSchedulerLog.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_SchedulerLog_ID id
	 *	@param trx transaction
	 */
	public MSchedulerLog (Ctx ctx, int AD_SchedulerLog_ID, Trx trx)
	{
		super (ctx, AD_SchedulerLog_ID, trx);
		if (AD_SchedulerLog_ID == 0)
			setIsError(false);
	}	//	MSchedulerLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MSchedulerLog (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MSchedulerLog

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param summary summary
	 */
	public MSchedulerLog (MScheduler parent, String summary)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setAD_Scheduler_ID(parent.getAD_Scheduler_ID());
		setSummary(summary);
	}	//	MSchedulerLog

}	//	MSchedulerLog
