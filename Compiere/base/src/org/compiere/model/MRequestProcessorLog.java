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
 *	Request Processor Log
 *	
 *  @author Jorg Janke
 *  @version $Id: MRequestProcessorLog.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MRequestProcessorLog extends X_R_RequestProcessorLog
	implements CompiereProcessorLog
{
    /** Logger for class MRequestProcessorLog */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRequestProcessorLog.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_RequestProcessorLog_ID id
	 */
	public MRequestProcessorLog (Ctx ctx, int R_RequestProcessorLog_ID, Trx trx)
	{
		super (ctx, R_RequestProcessorLog_ID, trx);
		if (R_RequestProcessorLog_ID == 0)
		{
			setIsError (false);
		}	
	}	//	MRequestProcessorLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRequestProcessorLog (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MRequestProcessorLog

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param summary summary
	 */
	public MRequestProcessorLog (MRequestProcessor parent, String summary)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setR_RequestProcessor_ID(parent.getR_RequestProcessor_ID());
		setSummary(summary);
	}	//	MRequestProcessorLog

	
}	//	MRequestProcessorLog
