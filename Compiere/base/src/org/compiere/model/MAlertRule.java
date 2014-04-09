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
import java.util.*;

import org.compiere.util.*;


/**
 *	Alert Rule Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAlertRule.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MAlertRule extends X_AD_AlertRule
{
    /** Logger for class MAlertRule */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAlertRule.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standatd Constructor
	 *	@param ctx context
	 *	@param AD_AlertRule_ID id
	 *	@param trx transaction
	 */
	public MAlertRule (Ctx ctx, int AD_AlertRule_ID, Trx trx)
	{
		super (ctx, AD_AlertRule_ID, trx);
	}	//	MAlertRule

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAlertRule (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAlertRule
	
	/**
	 * 	Get Sql
	 *	@return sql
	 */
	public String getSql()
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ").append(getSelectClause())
			.append(" FROM ").append(getFromClause());
		if (getWhereClause() != null && getWhereClause().length() > 0)
			sql.append(" WHERE ").append(getWhereClause());
		if (getOtherClause() != null && getOtherClause().length() > 0)
			sql.append(" ").append(getOtherClause());
		return sql.toString();
	}	//	getSql
	
	/**
	 * 	Get Table Name from FROM clause
	 *	@return table name or alias or null
	 */
	public String getTableName()
	{
		int AD_Table_ID = getAD_Table_ID();
		if (AD_Table_ID != 0)
		{
			MTable table = MTable.get(getCtx(), AD_Table_ID);
			String tableName = table.getTableName();
			if (!Util.isEmpty(tableName))
				return tableName;
		}
		//	FROM clause
		String from = getFromClause().trim();
		StringTokenizer st = new StringTokenizer(from, " ,\t\n\r\f", false);
		int tokens = st.countTokens();
		if (tokens == 0)
			return null;
		if (tokens == 1)
			return st.nextToken();
		String mainTable = st.nextToken();
		if (st.hasMoreTokens())
		{
			String next = st.nextToken();
			if (next.equalsIgnoreCase("RIGHT")
				|| next.equalsIgnoreCase("LEFT")
				|| next.equalsIgnoreCase("INNER")
				|| next.equalsIgnoreCase("FULL"))
				return mainTable;
			return next;
		}
		return mainTable;
	}	//	getTableName
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord)
			setIsValid(true);
		if (isValid())
			setErrorMsg(null);
		return true;
	}	//	beforeSave

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAlertRule[");
		sb.append(get_ID())
			.append("-").append(getName())
			.append(",Valid=").append(isValid())
			.append(",").append(getSql());
		sb.append ("]");
		return sb.toString ();
	}	//	toString
		
}	//	MAlertRule
