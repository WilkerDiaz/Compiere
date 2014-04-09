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

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.util.*;

/**
 *	Change Log Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MChangeLog.java 8951 2010-06-16 05:34:10Z ragrawal $
 */
public final class MChangeLog extends X_AD_ChangeLog
{
    /** Logger for class MChangeLog */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MChangeLog.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Do we track all changes for this table
	 *	@param AD_Table_ID table
	 *	@return true if changes are tracked
	 */
	public static boolean isLogged (int AD_Table_ID, String type)
	{
		if (s_logAllChanges == null && s_logUpdates == null)
			fillChangeLog();
		
		int index = Arrays.binarySearch(s_logAllChanges, AD_Table_ID);
			if (index >= 0) return true;
		
		if (!CHANGELOGTYPE_Insert.equals(type)) // Update and Deletes
			return Arrays.binarySearch(s_logUpdates, AD_Table_ID) >= 0;
		else
			return false;			
	}
	
	/**
	 * 	Not Logged
	 *	@param AD_Table_ID table
	 *	@param AD_Column_ID column
	 *	@param type type
	 *	@return true if not logged
	 */
	public static boolean isNotLogged (int AD_Table_ID, String tableName, 
		int AD_Column_ID, String type)
	{
		if (AD_Table_ID == X_AD_ChangeLog.Table_ID
			|| AD_Table_ID == X_AD_WindowLog.Table_ID
			|| AD_Table_ID == X_AD_QueryLog.Table_ID
			|| AD_Table_ID == X_AD_Issue.Table_ID
			|| AD_Column_ID == 6652 // AD_Process.Statistics_Count
			|| AD_Column_ID == 6653) //	AD_Process.Statistics_Seconds
			return true;
		
		//	Don't log Log entries
		if (CHANGELOGTYPE_Insert.equals(type)
			&& (tableName.indexOf("Log") != -1
				|| AD_Table_ID == X_AD_Session.Table_ID))
			return true;
		//
		return false;
	}	//	isNotLogged
	
	/**
	 *	Fill Log with tables to be logged 
	 */
	private static void fillChangeLog()
	{
		String sql = "SELECT t.AD_Table_ID, t.ChangeLogLevel FROM AD_Table t "
			+ "WHERE (t.ChangeLogLevel='A' or t.ChangeLogLevel='U') "					//	also inactive
			+ "OR EXISTS (SELECT * FROM AD_Column c "
			+ "WHERE t.AD_Table_ID=c.AD_Table_ID AND c.ColumnName='EntityType') "
			+ "ORDER BY t.AD_Table_ID";
		
		Object[][] tables = QueryUtil.executeQuery((Trx)null, sql);

		ArrayList<BigDecimal> allList = new ArrayList<BigDecimal>(40);
		ArrayList<BigDecimal> updateList = new ArrayList<BigDecimal>(40);

		for (Object[] table : tables) {
			String ChangeLogLevel = (String) table[1];
			BigDecimal AD_Table_ID = (BigDecimal) table[0];
			if (ChangeLogLevel.equals("A")) {
				allList.add(AD_Table_ID);
			} else if (ChangeLogLevel.equals("U")){
				updateList.add(AD_Table_ID);
			}
		}
		
		//	Convert to Array
		s_logAllChanges = new int [allList.size()];
		for (int i = 0; i < s_logAllChanges.length; i++)
		{
			BigDecimal id = allList.get(i);
			s_logAllChanges[i] = id.intValue();
		}
		s_log.info("#" + s_logAllChanges.length);

		s_logUpdates = new int [updateList.size()];
		for (int i = 0; i < s_logUpdates.length; i++)
		{
			BigDecimal id = updateList.get(i);
			s_logUpdates[i] = id.intValue();
		}
		s_log.info("#" + s_logUpdates.length);

	}	//	fillChangeLog

	/**	Change Log				*/
	private static int[]		s_logAllChanges = null;
	private static int[]		s_logUpdates = null;
	/**	Logger					*/
	private static CLogger		s_log = CLogger.getCLogger(MChangeLog.class);
	/** NULL Value				*/
	public static final String		NULL = "NULL";
	
	
	/**************************************************************************
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MChangeLog(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MChangeLog

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_ChangeLog_ID id
	 *	@param trx transaction
	 */
	public MChangeLog (Ctx ctx, int AD_ChangeLog_ID, Trx trx)
	{
		super (ctx, AD_ChangeLog_ID, trx);
		if (AD_ChangeLog_ID == 0)
		{
			int AD_Role_ID = ctx.getAD_Role_ID();
			setAD_Role_ID(AD_Role_ID);
			setRecord_ID(0);
		}
	}	//	MChangeLog
	
	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param AD_ChangeLog_ID 0 for new change log
	 *	@param TrxName change transaction name
	 *	@param AD_Session_ID session
	 *	@param AD_Table_ID table
	 *	@param AD_Column_ID column
	 *	@param keyInfo record key(s)
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 *	@param OldValue old
	 *	@param NewValue new
	 *	@param type type (insert, delete or update)
	 */
	public MChangeLog (Ctx ctx, int AD_ChangeLog_ID, String TrxName, 
		int AD_Session_ID, 
		int AD_Table_ID, int AD_Column_ID, Object keyInfo,
		int AD_Client_ID, int AD_Org_ID,
		Object OldValue, Object NewValue, String type)
	{
		this (ctx, 0, null);	//	 out of p_trx
		if (AD_ChangeLog_ID == 0)
		{
			AD_ChangeLog_ID = DB.getNextID (AD_Client_ID, Table_Name, null);
			if (AD_ChangeLog_ID <= 0)
				log.severe("No NextID (" + AD_ChangeLog_ID + ")");
		}
		setAD_ChangeLog_ID (AD_ChangeLog_ID);
		setTrxName(TrxName);
		setAD_Session_ID (AD_Session_ID);
		//
		setAD_Table_ID (AD_Table_ID);
		setAD_Column_ID (AD_Column_ID);
		//	Key
		if (keyInfo == null)
			log.severe("No Key Info");
		else if (keyInfo instanceof Integer)
			setRecord_ID(((Integer)keyInfo).intValue());
		else
			setRecord2_ID(keyInfo.toString());
		//
		setClientOrg (AD_Client_ID, AD_Org_ID);
		//
		setOldValue (OldValue);
		setNewValue (NewValue);
		//	R2.5.2f_2005-09-25  2.5.2f_20050925-2201
		setDescription(Compiere.MAIN_VERSION + "_" 
			+ Compiere.DATE_VERSION + " " + Compiere.getImplementationVersion());
		setChangeLogType(type);
	}	//	MChangeLog

	
	/**
	 * 	Set Old Value
	 *	@param OldValue old
	 */
	public void setOldValue (Object OldValue)
	{
		if (OldValue == null)
			super.setOldValue (NULL);
		else
			super.setOldValue (OldValue.toString());
	}	//	setOldValue

	/**
	 * 	Is Old Value Null
	 *	@return true if null
	 */
	public boolean isOldNull()
	{
		String value = getOldValue();
		return value == null || value.equals(NULL);
	}	//	isOldNull
	
	/**
	 * 	Set New Value
	 *	@param NewValue new
	 */
	public void setNewValue (Object NewValue)
	{
		if (NewValue == null)
			super.setNewValue (NULL);
		else
			super.setNewValue (NewValue.toString());
	}	//	setNewValue
	
	/**
	 * 	Is New Value Null
	 *	@return true if null
	 */
	public boolean isNewNull()
	{
		String value = getNewValue();
		return value == null || value.equals(NULL);
	}	//	isNewNull
	
	/**
	 * 	Get Record2_ID (not null)
	 *	@return record key or ""
	 */
	@Override
	public String getRecord2_ID()
	{
		String s = super.getRecord2_ID();
		if (s == null)
			return "";
		return s;
	}	//	getRecord2_ID
	
}	//	MChangeLog
