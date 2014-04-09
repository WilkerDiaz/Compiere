/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.compiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@compiere.org 
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;

import org.compiere.util.*;


/**
 *	Query Access Log
 *	
 *  @author Jorg Janke
 *  @version $Id: MQueryLog.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MQueryLog extends X_AD_QueryLog
{
    /** Logger for class MQueryLog */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MQueryLog.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_QueryLog_ID id
	 *	@param trx p_trx
	 */
	public MQueryLog(Ctx ctx, int AD_QueryLog_ID, Trx trx)
	{
		super(ctx, AD_QueryLog_ID, trx);
		if (AD_QueryLog_ID == 0)
		{
			int AD_Role_ID = ctx.getAD_Role_ID();
			setAD_Role_ID(AD_Role_ID);
		}
	}	//	MQueryLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MQueryLog(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MQueryLog

	/**
	 * 	Full Constructor
	 *	@param ctx ctx
	 *	@param AD_Session_ID session
	 *	@param AD_Client_ID login client
	 *	@param AD_Org_ID login org
	 *	@param AD_Table_ID table
	 *	@param WhereClause where
	 *	@param RecordCount count
	 *	@param Parameter parameter
	 */
	public MQueryLog(Ctx ctx, int AD_Session_ID, 
		int AD_Client_ID, int AD_Org_ID,
		int AD_Table_ID, String WhereClause, int RecordCount, String Parameter)
	{
		this(ctx, 0, null);	//	out of p_trx
		setAD_Session_ID (AD_Session_ID);
		setClientOrg (AD_Client_ID, AD_Org_ID);
		//
		setAD_Table_ID (AD_Table_ID);
		setWhereClause(WhereClause);
		setRecordCount(RecordCount);
		setParameter(Parameter);
	}	//	MQueryLog

	/**
	 *	Set Where Clause
	 *	@param sql sql or where clause
	 */
	@Override
	public void setWhereClause(String sql)
	{
		String where = "";
		if (sql != null)
		{
			where = sql.trim();
			int indexW = where.indexOf(" WHERE ");
			if (indexW >= 0)
				where = where.substring(indexW + 7);
			int indexO = where.indexOf(" ORDER BY ");
			if (indexO >= 0)
				where = where.substring(0, indexO);
		}
		super.setWhereClause(where);
	}	//	setWhereClause
	
}	//	MQueryLog
