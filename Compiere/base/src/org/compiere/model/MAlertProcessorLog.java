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
 *	Alert Log
 *	
 *  @author Jorg Janke
 *  @version $Id: MAlertProcessorLog.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MAlertProcessorLog extends X_AD_AlertProcessorLog
	implements CompiereProcessorLog
{
    /** Logger for class MAlertProcessorLog */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAlertProcessorLog.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_AlertProcessorLog_ID id
	 *	@param trx transaction
	 */
	public MAlertProcessorLog (Ctx ctx, int AD_AlertProcessorLog_ID, Trx trx)
	{
		super (ctx, AD_AlertProcessorLog_ID, trx);
	}	//	MAlertProcessorLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAlertProcessorLog (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAlertProcessorLog
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param summary summary
	 */
	public MAlertProcessorLog (MAlertProcessor parent, String summary)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setAD_AlertProcessor_ID(parent.getAD_AlertProcessor_ID());
		setSummary(summary);
	}	//	MAlertProcessorLog

}	//	MAlertProcessorLog
