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
 * 	Replication Run Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReplicationRun.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MReplicationRun extends X_AD_Replication_Run
{
    /** Logger for class MReplicationRun */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MReplicationRun.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Create new Run
	 *	@param ctx context
	 *	@param AD_Replication_ID id
	 *	@param dateRun date
	 */
	public MReplicationRun (Ctx ctx, int AD_Replication_ID, Timestamp dateRun, Trx trx)
	{
		super(ctx, 0, trx);
		setAD_Replication_ID (AD_Replication_ID);
		setName (dateRun.toString());
		super.setIsReplicated (false);
	}	//	MReplicationRun

	/**
	 * 	Set Replication Flag
	 * 	@param IsReplicated replicated
	 */
	@Override
	public void setIsReplicated (boolean IsReplicated)
	{
		super.setIsReplicated(IsReplicated);
	}	//	setIsReplicated

}	//	MReplicationRun
