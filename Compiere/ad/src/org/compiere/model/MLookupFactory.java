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
import java.util.logging.*;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *  Create MLookups
 *
 *  @author Jorg Janke
 *  @version  $Id: MLookupFactory.java 8793 2010-05-20 22:08:38Z rthng $
 */
public class MLookupFactory
{
    /** Logger for class MLookupFactory */
    private static final org.compiere.util.CLogger s_log = org.compiere.util.CLogger.getCLogger(MLookupFactory.class);

	/**
	 *  Create MLookup
	 *
	 *  @param ctx context for access
	 *  @param WindowNo window no
	 * 	@param AD_Reference_ID display type
	 *  @param Column_ID AD_Column_ID or AD_Process_Para_ID
	 *  @param language report language
	 * 	@param ColumnName key column name
	 * 	@param AD_Reference_Value_ID AD_Reference (List, Table)
	 * 	@param IsParent parent (prevents query to directly access value)
	 * 	@param ValidationCode optional SQL validation
	 *  @throws Exception if Lookup could not be created
	 *  @return MLookup
	 */
	public static MLookup get (Ctx ctx, int WindowNo, int Column_ID, int AD_Reference_ID,
			Language language, String ColumnName, int AD_Reference_Value_ID,
			boolean IsParent, String ValidationCode)
	{
		MLookup lookup = new MLookup(ctx, WindowNo, AD_Reference_ID); 
		MLookupInfo info = getLookupInfo (lookup, Column_ID, 
			language, ColumnName, AD_Reference_Value_ID, IsParent, ValidationCode);
		if (info == null)
			throw new IllegalArgumentException ("MLookup.create - no LookupInfo");
		return lookup.initialize(info);
	}   //  create

	public static MLookup get (Ctx ctx, int WindowNo, int Column_ID, int AD_Reference_ID) {
		return get(ctx, WindowNo, Column_ID, AD_Reference_ID, true);
	}

	/**
	 *  Create MLookup
	 *
	 *  @param ctx context for access
	 *  @param WindowNo window no
	 *  @param Column_ID AD_Column_ID or AD_Process_Para_ID
	 * 	@param AD_Reference_ID display type
	 *  @return MLookup
	 */
	public static MLookup get (Ctx ctx, int WindowNo, int Column_ID, int AD_Reference_ID, boolean load)
	{
		String ColumnName = "";
		int AD_Reference_Value_ID = 0;
		boolean IsParent = false;
		String ValidationCode = "";
		//
		String sql = "SELECT c.ColumnName, c.AD_Reference_Value_ID, c.IsParent, vr.Code "
			+ "FROM AD_Column c"
			+ " LEFT OUTER JOIN AD_Val_Rule vr ON (c.AD_Val_Rule_ID=vr.AD_Val_Rule_ID) "
			+ "WHERE c.AD_Column_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, Column_ID);
			
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				ColumnName = rs.getString(1);
				AD_Reference_Value_ID = rs.getInt(2);
				IsParent = "Y".equals(rs.getString(3));
				ValidationCode = rs.getString(4);
			}
			else
				s_log.log(Level.SEVERE, "Column Not Found - AD_Column_ID=" + Column_ID);
		}
		catch (SQLException ex) {
			s_log.log(Level.SEVERE, sql, ex);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MLookup lookup = new MLookup(ctx, WindowNo, AD_Reference_ID); 
		MLookupInfo info = getLookupInfo (lookup, Column_ID, 
			Env.getLanguage(ctx), ColumnName, AD_Reference_Value_ID, IsParent, ValidationCode);
		if (info == null)
			throw new IllegalArgumentException ("MLookup.create - no LookupInfo");
		return lookup.initialize(info, load);
	}   //  get

	/**************************************************************************
	 *  Get Information for Lookups based on Column_ID for Table Columns or Process Parameters.
	 *
	 *	The SQL returns three columns:
	 *  <pre>
	 *		Key, Value, Name, IsActive	(where either key or value is null)
	 *  </pre>
	 *  @param ctx context for access
	 *  @param WindowNo window no
	 * 	@param AD_Reference_ID display type
	 *  @param Column_ID AD_Column_ID or AD_Process_Para_ID
	 *  @param language report language
	 * 	@param ColumnName key column name
	 * 	@param AD_Reference_Value_ID AD_Reference (List, Table)
	 * 	@param IsParent parent (prevents query to directly access value)
	 * 	@param ValidationCode optional SQL validation
	 *  @return lookup info structure
	 */
	static public MLookupInfo getLookupInfo (Lookup lookup, 
		int Column_ID, Language language, String ColumnName, int AD_Reference_Value_ID,
		boolean IsParent, String ValidationCode)
	{
		return getLookupInfo (lookup.getCtx(), lookup.getWindowNo(), lookup.getDisplayType(), 
			Column_ID, language, ColumnName, AD_Reference_Value_ID,
			IsParent, ValidationCode);
	}	//	getLookupInfo

	/**************************************************************************
	 *  Get Information for Lookups based on Column_ID for Table Columns or Process Parameters.
	 *
	 *	The SQL returns three columns:
	 *  <pre>
	 *		Key, Value, Name, IsActive	(where either key or value is null)
	 *  </pre>
	 *  @param ctx context for access
	 *  @param WindowNo window no
	 * 	@param AD_Reference_ID display type
	 *  @param Column_ID AD_Column_ID or AD_Process_Para_ID
	 *  @param language report language
	 * 	@param ColumnName key column name
	 * 	@param AD_Reference_Value_ID AD_Reference (List, Table)
	 * 	@param IsParent parent (prevents query to directly access value)
	 * 	@param ValidationCode optional SQL validation
	 *  @return lookup info structure
	 */
	static public MLookupInfo getLookupInfo (Ctx ctx, int WindowNo, int AD_Reference_ID, 
		int Column_ID, Language language, String ColumnName, int AD_Reference_Value_ID,
		boolean IsParent, String ValidationCode)
	{
		MLookupInfo info = null;
		boolean needToAddSecurity = true;
		//	List
		if (AD_Reference_ID == DisplayTypeConstants.List)	//	17
		{
			info = getLookup_List(language, AD_Reference_Value_ID);
			needToAddSecurity = false;
		}
		//	Table or Search with Reference_Value
		else if ((AD_Reference_ID == DisplayTypeConstants.Table || AD_Reference_ID == DisplayTypeConstants.Search)
			&& AD_Reference_Value_ID != 0)
		{
			info = getLookup_Table (ctx, language, WindowNo, AD_Reference_Value_ID);
		}
		//	Acct
		else if (AD_Reference_ID == DisplayTypeConstants.Account)
		{
			info = getLookup_Acct(ctx, Column_ID);
		}
		//	TableDir, Search, ID, ...
		else
		{
			info = getLookup_TableDir (ctx, language, WindowNo, ColumnName);
		}
		//  do we have basic info?
		if (info == null)
		{
			s_log.severe ("No SQL - " + ColumnName);
			return null;
		}
		//	remaining values
		info.Column_ID = Column_ID;
		info.AD_Reference_Value_ID = AD_Reference_Value_ID;
		info.IsParent = IsParent;
		info.ValidationCode = ValidationCode;
		if (info.ValidationCode == null)
			info.ValidationCode = "";

		//	Variables in SQL WHERE
		if (info.getQuery().indexOf("@") != -1)
		{
		//	String newSQL = Env.parseContext(ctx, WindowNo, info.Query, false);
			String newSQL = Env.parseContext(ctx, 0, info.getQuery(), false, false);	//	only global
			if (newSQL.length() == 0)
			{
				s_log.severe ("SQL parse error: " + info.getQuery());
				return null;
			}
			info.setQuery(newSQL);
			s_log.fine("SQL =" + newSQL);
		}

		//	Direct Query - NO Validation/Security
		int posOrder = info.getQuery().lastIndexOf(" ORDER BY ");
		boolean hasWhere = info.getQuery().lastIndexOf(" WHERE ") != -1;
		if (hasWhere)	//	might be for a select sub-query
		{
			//	SELECT (SELECT .. FROM .. WHERE ..) FROM ..
			//	SELECT .. FROM .. WHERE EXISTS (SELECT .. FROM .. WHERE ..)
			AccessSqlParser asp = new AccessSqlParser(info.getQuery());
			String mainQuery = asp.getMainSql();
			hasWhere = mainQuery.indexOf(" WHERE ") != -1;
		}
		if (posOrder == -1)
			info.QueryDirect = info.getQuery()
				+ (hasWhere ? " AND " : " WHERE ") + info.KeyColumn + "=?";
		else
			info.QueryDirect = info.getQuery().substring(0, posOrder)
				+ (hasWhere ? " AND " : " WHERE ") + info.KeyColumn + "=?";

		//	Validation
		String local_validationCode = "";
		boolean completelyValidated = true;
		if (info.ValidationCode.length() == 0)
			info.setValidated(true);
		else
		{
			local_validationCode = Env.parseContext (ctx, WindowNo, info.ValidationCode, true, false);
			//  returns "" if not all variables were parsed
			if (local_validationCode.length() == 0 
				|| info.ValidationCode.indexOf("@AD_Org_ID@") != -1)	//	don't validate Org
			{
				info.setValidated(false);
				local_validationCode = "";
			}
			else	//	check if completely validated
			{
				String inStr = info.ValidationCode;
				int i = inStr.indexOf('@');
				while (i != -1 && i != inStr.lastIndexOf ("@") && completelyValidated)
				{
					inStr = inStr.substring(i+1, inStr.length());	// from first @
					int j = inStr.indexOf('@');						// next @
					if (j < 0)
					{
						completelyValidated = false;
						break;						//	no second tag
					}
					String token = inStr.substring(0, j);
					int idx = token.indexOf("|");	//	or clause
					if (idx  >=  0) 
					{
						completelyValidated = false;
						break;
					}
					inStr = inStr.substring(j+1, inStr.length());	// from second @
					i = inStr.indexOf('@');
				}
				info.setValidated(completelyValidated);
			}
		}
		
		//	Add Local Validation
		if (local_validationCode.length() != 0 && completelyValidated)
		{
			String infoQuery = info.getQuery();
			if (posOrder > 0)
				info.setQuery(infoQuery.substring(0, posOrder)
					+ (hasWhere ? " AND " : " WHERE ") + local_validationCode
					+ infoQuery.substring(posOrder));
			else
				info.setQuery(infoQuery
				+ (hasWhere ? " AND " : " WHERE ") + local_validationCode);
		}
				
		//	Add Security
		if (needToAddSecurity)
			info.setQuery (MRole.getDefault(ctx, false).addAccessSQL(info.getQuery(), 
				info.TableName, MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO));
		//
	//	s_log.finest("Query:  " + info.Query);
	//	s_log.finest("Direct: " + info.QueryDirect);
		return info;
	}	//	createLookupInfo

	
	/**************************************************************************
	 *	Get Lookup SQL for Lists
	 *  @param language report language
	 *  @param AD_Reference_Value_ID reference value
	 *	@return SELECT NULL, Value, Name, IsActive FROM AD_Ref_List
	 */
	static public MLookupInfo getLookup_List(Language language, int AD_Reference_Value_ID)
	{
		StringBuffer realSQL = new StringBuffer ("SELECT NULL, AD_Ref_List.Value,");
		if (Env.isBaseLanguage(language, "AD_Ref_List"))
			realSQL.append("AD_Ref_List.Name,AD_Ref_List.IsActive FROM AD_Ref_List");
		else
			realSQL.append("trl.Name, AD_Ref_List.IsActive "
				+ "FROM AD_Ref_List INNER JOIN AD_Ref_List_Trl trl "
				+ " ON (AD_Ref_List.AD_Ref_List_ID=trl.AD_Ref_List_ID AND trl.AD_Language='")
					.append(language.getAD_Language()).append("')");
		realSQL.append(" WHERE AD_Ref_List.AD_Reference_ID=").append(AD_Reference_Value_ID);
		realSQL.append(" ORDER BY 2");
		//
		return new MLookupInfo(realSQL.toString(), "AD_Ref_List", "AD_Ref_List.Value",
			101,101, Query.getEqualQuery("AD_Reference_ID", AD_Reference_Value_ID));	//	Zoom Window+Query
	}	//	getLookup_List

	/**
	 * 	Get Lookup SQL for List
	 *	@param language report Language
	 *	@param AD_Reference_Value_ID reference value
	 *	@param linkColumnName link column name
	 *	@return SELECT Name FROM AD_Ref_List WHERE AD_Reference_ID=x AND Value=linkColumn
	 */
	static public String getLookup_ListEmbed(Language language, 
		int AD_Reference_Value_ID, String linkColumnName)
	{
		StringBuffer realSQL = new StringBuffer ("SELECT ");
		if (Env.isBaseLanguage(language, "AD_Ref_List"))
			realSQL.append("AD_Ref_List.Name FROM AD_Ref_List");
		else
			realSQL.append("trl.Name "
				+ "FROM AD_Ref_List INNER JOIN AD_Ref_List_Trl trl "
				+ " ON (AD_Ref_List.AD_Ref_List_ID=trl.AD_Ref_List_ID AND trl.AD_Language='")
					.append(language.getAD_Language()).append("')");
		realSQL.append(" WHERE AD_Ref_List.AD_Reference_ID=").append(AD_Reference_Value_ID)
			.append(" AND AD_Ref_List.Value=").append(linkColumnName);
		
		//
		return realSQL.toString();
	}	//	getLookup_ListEmbed
	
	/**
	 * 	Get Lookup SQL for List for virtual column
	 *	@param language report Language
	 *	@param AD_Reference_Value_ID reference value
	 *	@param ColumnSQL columnSQL 
	 *	@return SELECT Name FROM AD_Ref_List WHERE AD_Reference_ID=x AND Value=linkColumn
	 */
	static public String getLookup_ListVirtualEmbed(Language language, 
		int AD_Reference_Value_ID, String ColumnSQL)
	{
		StringBuffer realSQL = new StringBuffer ("SELECT ");
		if (Env.isBaseLanguage(language, "AD_Ref_List"))
			realSQL.append("AD_Ref_List.Name FROM AD_Ref_List");
		else
			realSQL.append("trl.Name "
				+ "FROM AD_Ref_List INNER JOIN AD_Ref_List_Trl trl "
				+ " ON (AD_Ref_List.AD_Ref_List_ID=trl.AD_Ref_List_ID AND trl.AD_Language='")
					.append(language.getAD_Language()).append("')");
		realSQL.append(" WHERE AD_Ref_List.AD_Reference_ID=").append(AD_Reference_Value_ID)
			.append(" AND AD_Ref_List.Value=").append(ColumnSQL);
		
		//
		return realSQL.toString();
	}	//	getLookup_ListVirtualEmbed
	
	/***************************************************************************
	 *	Get Lookup SQL for Table Lookup
	 *  @param ctx context for access and dynamic access
	 *  @param language report language
	 *  @param WindowNo window no
	 *  @param AD_Reference_Value_ID reference value
	 *	@return	SELECT Key, NULL, Name, IsActive FROM Table - if KeyColumn end with _ID
	 *	  otherwise	SELECT NULL, Key, Name, IsActive FROM Table
	 */
	static private MLookupInfo getLookup_Table (Ctx ctx, Language language,
		int WindowNo, int AD_Reference_Value_ID)
	{
		MLookupInfo retValue = null;
		//
		String sql0 = "SELECT t.TableName,ck.ColumnName AS KeyColumn,"				//	1..2
			+ "cd.ColumnName AS DisplayColumn,rt.IsValueDisplayed,cd.IsTranslated,"	//	3..5
			+ "rt.WhereClause,rt.OrderByClause,t.AD_Window_ID,t.PO_Window_ID, "		//	6..9
			+ "t.AD_Table_ID, rt.IsDisplayIdentifiers "								//	10..11
			+ "FROM AD_Ref_Table rt"
			+ " INNER JOIN AD_Table t ON (rt.AD_Table_ID=t.AD_Table_ID)"
			+ " INNER JOIN AD_Column ck ON (rt.Column_Key_ID=ck.AD_Column_ID)"
			+ " INNER JOIN AD_Column cd ON (rt.Column_Display_ID=cd.AD_Column_ID) "
			+ "WHERE rt.AD_Reference_ID=?"
			+ " AND rt.IsActive='Y' AND t.IsActive='Y'";
		//
		String	KeyColumn = null,  TableName = null, WhereClause = null, OrderByClause = null;
		String DisplayColumn=null;
		boolean IsTranslated = false, isValueDisplayed = false, isDisplayIdentifiers = false;
		int ZoomWindow = 0;
		int ZoomWindowPO = 0;
	//	int AD_Table_ID = 0;
		boolean loaded = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql0, (Trx) null);
			pstmt.setInt(1, AD_Reference_Value_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				TableName = rs.getString(1);
				KeyColumn = rs.getString(2);
				DisplayColumn = rs.getString(3);
				isValueDisplayed = "Y".equals(rs.getString(4));
				IsTranslated = "Y".equals(rs.getString(5));
				WhereClause = rs.getString(6);
				OrderByClause = rs.getString(7);
				ZoomWindow = rs.getInt(8);
				ZoomWindowPO = rs.getInt(9);
			//	AD_Table_ID = rs.getInt(10);
				isDisplayIdentifiers = "Y".equals(rs.getString(11));
				loaded = true;
			}
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql0, e);
			return null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (!loaded)
		{
			s_log.log(Level.SEVERE, "No Table Reference Table ID=" + AD_Reference_Value_ID);
			return null;
		}

		IsTranslated = IsTranslated && isTranslated(TableName);

		if (isDisplayIdentifiers)
			DisplayColumn = getLookup_DisplayColumn(language, TableName).toString();
		else
			DisplayColumn = TableName + ((IsTranslated && !Env.isBaseLanguage(language, TableName))?"_TRL.":".") + DisplayColumn;
		
		StringBuffer realSQL = new StringBuffer("SELECT ");
		if (!KeyColumn.toUpperCase().endsWith("_ID"))
			realSQL.append("NULL,");
					
		//	Translated
		if (IsTranslated && !Env.isBaseLanguage(language, TableName))
		{
			realSQL.append(TableName).append(".").append(KeyColumn).append(",");
			if (KeyColumn.toUpperCase().endsWith("_ID"))
				realSQL.append("NULL,");
			if (isValueDisplayed)
				realSQL.append(TableName).append(".Value || '-' || ");
			
			realSQL.append(DisplayColumn.toString());
			
			realSQL.append(",").append(TableName).append(".IsActive");
			realSQL.append(" FROM ").append(TableName)
				.append(" INNER JOIN ").append(TableName).append("_TRL ON (")
				.append(TableName).append(".").append(KeyColumn)
				.append("=").append(TableName).append("_Trl.").append(KeyColumn)
				.append(" AND ").append(TableName).append("_Trl.AD_Language='")
				.append(language.getAD_Language()).append("')");
		}
		//	Not Translated
		else
		{
			realSQL.append(TableName).append(".").append(KeyColumn).append(",");
			if (KeyColumn.toUpperCase().endsWith("_ID"))
				realSQL.append("NULL,");
			if (isValueDisplayed)
				realSQL.append(TableName).append(".Value || '-' || ");
			//jz EDB || problem
			if (DB.isPostgreSQL())
				realSQL.append("COALESCE(TO_CHAR(").append(DisplayColumn).append("),'')");
			else if (DB.isMSSQLServer())
					realSQL.append("COALESCE(CONVERT(VARCHAR,").append(DisplayColumn).append("),'')");
			else
				realSQL.append(DisplayColumn);
			realSQL.append(",").append(TableName).append(".IsActive");
			realSQL.append(" FROM ").append(TableName);
		}
		
		if (!isDisplayIdentifiers)
			realSQL.append(" WHERE "+ DisplayColumn + " IS NOT NULL ");

		//	add WHERE clause
		Query zoomQuery = null;
		if (WhereClause != null)
		{
			String where = WhereClause;
			if (where.indexOf("@") != -1)
				where = Env.parseContext(ctx, WindowNo, where, false);
			if (where.length() == 0 && WhereClause.length() != 0)
				s_log.severe ("Could not resolve: " + WhereClause);

			//	We have no context
			if (where.length() != 0)
			{
				if (isDisplayIdentifiers)
					realSQL.append(" WHERE ");
				else
					realSQL.append(" AND ");
				
				realSQL.append(where);
				if (where.indexOf(".") == -1)
					s_log.log(Level.SEVERE, "Table - " + TableName
						+ ": WHERE should be fully qualified: " + WhereClause);
				zoomQuery = new Query (TableName);
				zoomQuery.addRestriction(where);
			}
		}

		//	Order By qualified term or by Name
		if (OrderByClause != null)
		{
			realSQL.append(" ORDER BY ").append(OrderByClause);
			if (OrderByClause.indexOf(".") == -1)
				s_log.log(Level.SEVERE, "getLookup_Table - " + TableName
					+ ": ORDER BY must fully qualified: " + OrderByClause);
		}
		else
			realSQL.append(" ORDER BY 3");

		s_log.finest("AD_Reference_Value_ID=" + AD_Reference_Value_ID + " - " + realSQL);
		retValue = new MLookupInfo (realSQL.toString(), TableName, 
			TableName + "." + KeyColumn, ZoomWindow, ZoomWindowPO, zoomQuery);
		return retValue;
	}	//	getLookup_Table

	/**
	 *	Get Embedded Lookup SQL for Table Lookup
	 *  @param language report language
	 * 	@param BaseColumn base column name
	 * 	@param BaseTable base table name
	 *  @param AD_Reference_Value_ID reference value
	 *	@return	SELECT Name FROM Table
	 */
	static public String getLookup_TableEmbed (Language language,
		String BaseColumn, String BaseTable, int AD_Reference_Value_ID)
	{
		String sql = "SELECT t.TableName,ck.ColumnName AS KeyColumn,"
			+ "cd.ColumnName AS DisplayColumn,rt.IsValueDisplayed,cd.IsTranslated "
			+ "FROM AD_Ref_Table rt"
			+ " INNER JOIN AD_Table t ON (rt.AD_Table_ID=t.AD_Table_ID)"
			+ " INNER JOIN AD_Column ck ON (rt.Column_Key_ID=ck.AD_Column_ID)"
			+ " INNER JOIN AD_Column cd ON (rt.Column_Display_ID=cd.AD_Column_ID) "
			+ "WHERE rt.AD_Reference_ID=?"
			+ " AND rt.IsActive='Y' AND t.IsActive='Y'";
		//
		String	KeyColumn, DisplayColumn, TableName;
		boolean IsTranslated, isValueDisplayed;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Reference_Value_ID);
			rs = pstmt.executeQuery();
			if (!rs.next())
			{
				s_log.log(Level.SEVERE, "Cannot find Reference Table, ID=" + AD_Reference_Value_ID
					+ ", Base=" + BaseTable + "." + BaseColumn);
				return null;
			}

			TableName = rs.getString(1);
			KeyColumn = rs.getString(2);
			DisplayColumn = rs.getString(3);
			isValueDisplayed = rs.getString(4).equals("Y");
			IsTranslated = rs.getString(5).equals("Y");

		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql, e);
			return null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		StringBuffer embedSQL = new StringBuffer("SELECT ");

		//	Translated
		if (IsTranslated && !Env.isBaseLanguage(language, TableName))
		{
			if (isValueDisplayed)
				embedSQL.append(TableName).append(".Value||'-'||");
			embedSQL.append(TableName).append("_Trl.").append(DisplayColumn);
			//
			embedSQL.append(" FROM ").append(TableName)
				.append(" INNER JOIN ").append(TableName).append("_TRL ON (")
				.append(TableName).append(".").append(KeyColumn)
				.append("=").append(TableName).append("_Trl.").append(KeyColumn)
				.append(" AND ").append(TableName).append("_Trl.AD_Language='")
				.append(language.getAD_Language()).append("')");
		}
		//	Not Translated
		else
		{
			if (isValueDisplayed)
				embedSQL.append(TableName).append(".Value||'-'||");
			embedSQL.append(TableName).append(".").append(DisplayColumn);
			//
			embedSQL.append(" FROM ").append(TableName);
		}

		embedSQL.append(" WHERE ").append(BaseTable).append(".").append(BaseColumn);
		embedSQL.append("=").append(TableName).append(".").append(KeyColumn);

		return embedSQL.toString();
	}	//	getLookup_TableEmbed

	/**
	 *	Get Embedded Lookup SQL for Table Lookup for virtual column
	 *  @param language report language
	 *  @param AD_Reference_Value_ID reference value
	 *  @param columnSQL Column SQL for virtual column
	 *	@return	SELECT Name FROM Table
	 */
	static public String getLookup_TableVirtualEmbed (Language language,
		int AD_Reference_Value_ID, String columnSQL)
	{
		String sql = "SELECT t.TableName,ck.ColumnName AS KeyColumn,"
			+ "cd.ColumnName AS DisplayColumn,rt.IsValueDisplayed,cd.IsTranslated "
			+ "FROM AD_Ref_Table rt"
			+ " INNER JOIN AD_Table t ON (rt.AD_Table_ID=t.AD_Table_ID)"
			+ " INNER JOIN AD_Column ck ON (rt.Column_Key_ID=ck.AD_Column_ID)"
			+ " INNER JOIN AD_Column cd ON (rt.Column_Display_ID=cd.AD_Column_ID) "
			+ "WHERE rt.AD_Reference_ID=?"
			+ " AND rt.IsActive='Y' AND t.IsActive='Y'";
		//
		String	KeyColumn, DisplayColumn, TableName;
		boolean IsTranslated, isValueDisplayed;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Reference_Value_ID);
			rs = pstmt.executeQuery();
			if (!rs.next())
			{
				s_log.log(Level.SEVERE, "Cannot find Reference Table, ID=" + AD_Reference_Value_ID);
				return null;
			}

			TableName = rs.getString(1);
			KeyColumn = rs.getString(2);
			DisplayColumn = rs.getString(3);
			isValueDisplayed = rs.getString(4).equals("Y");
			IsTranslated = rs.getString(5).equals("Y");
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql, e);
			return null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		StringBuffer embedSQL = new StringBuffer("SELECT ");

		//	Translated
		if (IsTranslated && !Env.isBaseLanguage(language, TableName))
		{
			if (isValueDisplayed)
				embedSQL.append(TableName).append(".Value||'-'||");
			embedSQL.append(TableName).append("_Trl.").append(DisplayColumn);
			//
			embedSQL.append(" FROM ").append(TableName)
				.append(" INNER JOIN ").append(TableName).append("_TRL ON (")
				.append(TableName).append(".").append(KeyColumn)
				.append("=").append(TableName).append("_Trl.").append(KeyColumn)
				.append(" AND ").append(TableName).append("_Trl.AD_Language='")
				.append(language.getAD_Language()).append("')");
		}
		//	Not Translated
		else
		{
			if (isValueDisplayed)
				embedSQL.append(TableName).append(".Value||'-'||");
			embedSQL.append(TableName).append(".").append(DisplayColumn);
			//
			embedSQL.append(" FROM ").append(TableName);
		}

		embedSQL.append(" WHERE ");
		embedSQL.append(TableName).append(".").append(KeyColumn);
		embedSQL.append("=").append(columnSQL);

		return embedSQL.toString();
	}	//	getLookup_TableVirtualEmbed

	/**
	 *	Get Display Columns SQL for Table/Table Direct Lookup
	 *  @param language report language
	 * 	@param TableName table name
	 *	@return	SELECT DisplayColumns
	 */
	static public StringBuffer getLookup_DisplayColumn (Language language,String TableName)
	{
		//	get display column names
		String sql0 = "SELECT c.ColumnName,c.IsTranslated,c.AD_Reference_ID,"
			+ "c.AD_Reference_Value_ID,t.AD_Window_ID,t.PO_Window_ID "
			+ "FROM AD_Table t"
			+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID) "
			+ "WHERE TableName=?"
			+ " AND c.IsIdentifier='Y' "
			+ "ORDER BY c.SeqNo";

		ArrayList<LookupDisplayColumn> list = new ArrayList<LookupDisplayColumn>();
		boolean isTranslated = false;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql0, (Trx) null);
			pstmt.setString(1, TableName);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				LookupDisplayColumn ldc = new LookupDisplayColumn (rs.getString(1),
					"Y".equals(rs.getString(2)), rs.getInt(3), rs.getInt(4));
				list.add (ldc);
			//	s_log.fine("getLookup_TableDir: " + ColumnName + " - " + ldc);
				//
				if (!isTranslated && ldc.IsTranslated)
					isTranslated = true;
			}
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql0, e);
			return null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//  Do we have columns ?
		if (list.size() == 0)
		{
			s_log.log(Level.SEVERE, "No Identifier records found: " + TableName);
			return null;
		}

		StringBuffer displayColumn = new StringBuffer();
		int size = list.size();
		//  Get Display Column
		for (int i = 0; i < size; i++)
		{
			if (i > 0)
				displayColumn.append(" ||'_'|| " );
			LookupDisplayColumn ldc = list.get(i);
			//jz EDB || problem
			if (DB.isPostgreSQL())
				displayColumn.append("COALESCE(TO_CHAR(");
			else if (DB.isMSSQLServer())
				displayColumn.append("COALESCE(CONVERT(VARCHAR,");
			//  translated
			if (ldc.IsTranslated && !Env.isBaseLanguage(language, TableName))
				displayColumn.append(TableName).append("_Trl.").append(ldc.ColumnName);
			//  date
			else if (FieldType.isDate(ldc.DisplayType))
			{
				displayColumn.append(DB.TO_CHAR(TableName + "." + ldc.ColumnName, ldc.DisplayType, language.getAD_Language()));
			}
			//  TableDir
			else if ((ldc.DisplayType == DisplayTypeConstants.TableDir || ldc.DisplayType == DisplayTypeConstants.Search)
				&& ldc.ColumnName.toUpperCase().endsWith("_ID"))
			{
				String embeddedSQL = getLookup_TableDirEmbed(language, ldc.ColumnName, TableName);
				if (embeddedSQL != null)
					displayColumn.append("(").append(embeddedSQL).append(")");
			}
			//	Table
			else if (ldc.DisplayType == DisplayTypeConstants.Table && ldc.AD_Reference_ID != 0)
			{
				String embeddedSQL = getLookup_TableEmbed (language, ldc.ColumnName, TableName, ldc.AD_Reference_ID);
				if (embeddedSQL != null)
					displayColumn.append("(").append(embeddedSQL).append(")");
			}
			else if (ldc.DisplayType == DisplayTypeConstants.List && ldc.AD_Reference_ID !=0)
			{
				String embeddedSQL = getLookup_ListEmbed(language, ldc.AD_Reference_ID, ldc.ColumnName);
				if(embeddedSQL !=null)
					displayColumn.append("(").append(embeddedSQL).append(")");
			}
			//  number
			else if (FieldType.isNumeric(ldc.DisplayType))
			{
				displayColumn.append(DB.TO_CHAR(TableName + "." + ldc.ColumnName, ldc.DisplayType, language.getAD_Language()));
			}
			//  String
			else
			{
				//jz EDB || null issue
				if (DB.isPostgreSQL())
					displayColumn.append("COALESCE(TO_CHAR(").append(TableName).append(".").append(ldc.ColumnName).append("),'')");
				else if (DB.isMSSQLServer())
					displayColumn.append("COALESCE(CONVERT(VARCHAR,").append(TableName).append(".").append(ldc.ColumnName).append("),'')");
				else
					displayColumn.append(TableName).append(".").append(ldc.ColumnName);
			}
			
			//jz EDB || problem
			if (DB.isPostgreSQL() || DB.isMSSQLServer())
				displayColumn.append("),'')");

		}

		return displayColumn;
	}	//	getLookup_TableEmbed

	
	/**************************************************************************
	 * 	Get Lookup SQL for direct Table Lookup
	 *	@param ctx context for access
	 *	@param language report language
	 *	@param ColumnName column name
	 * 	@param WindowNo Window (for SOTrx)
	 *	@return SELECT Key, NULL, Name, IsActive from Table (fully qualified)
	 */
	static private MLookupInfo getLookup_TableDir (Ctx ctx, Language language,
		int WindowNo, String ColumnName)
	{
		if (!ColumnName.toUpperCase().endsWith("_ID"))
		{
			s_log.log(Level.SEVERE, "Key does not end with '_ID': " + ColumnName);
			return null;
		}
		
		//	Hardcoded BPartner Org
		if (ColumnName.equals("AD_OrgBP_ID"))
			ColumnName = "AD_Org_ID";
		
		if (ColumnName.indexOf("M_Locator") != -1 || ColumnName.indexOf("M_ActualLocator") != -1)
			ColumnName = "M_Locator_ID";
		
		

		String TableName = ColumnName.substring(0,ColumnName.length()-3);
	//	boolean isSOTrx = !"N".equals(ctx.getContext( WindowNo, "IsSOTrx"));
		int ZoomWindow = 0;
		int ZoomWindowPO = 0;
		boolean isTranslated = false;
		String KeyColumn = ColumnName;		
		
		String sql0 = "SELECT t.AD_Window_ID,t.PO_Window_ID "
			+ "FROM AD_Table t "
			+ "WHERE TableName=? ";
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		try
		{
			pstmt = DB.prepareStatement(sql0, (Trx) null);
			pstmt.setString(1, TableName);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				ZoomWindow = rs.getInt(1);
				ZoomWindowPO = rs.getInt(2);
			}
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql0, e);
			return null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		isTranslated = isTranslated(TableName);

		StringBuffer realSQL = new StringBuffer("SELECT ");
		realSQL.append(TableName).append(".").append(KeyColumn).append(",NULL,");

		StringBuffer displayColumn = getLookup_DisplayColumn(language, TableName);

		realSQL.append(displayColumn.toString());
		realSQL.append(",").append(TableName).append(".IsActive");

		//  Translation
		if (isTranslated && !Env.isBaseLanguage(language, TableName))
		{
			realSQL.append(" FROM ").append(TableName)
				.append(" INNER JOIN ").append(TableName).append("_TRL ON (")
				.append(TableName).append(".").append(KeyColumn)
				.append("=").append(TableName).append("_Trl.").append(KeyColumn)
				.append(" AND ").append(TableName).append("_Trl.AD_Language='")
				.append(language.getAD_Language()).append("')");
		}
		else	//	no translation
		{
			realSQL.append(" FROM ").append(TableName);
		}

		//	Order by Display    
		realSQL.append(" ORDER BY 3");
		//	((LookupDisplayColumn)list.get(3)).ColumnName);
		Query zoomQuery = null;	//	corrected in VLookup

		if (CLogMgt.isLevelFinest())
			s_log.fine("ColumnName=" + ColumnName + " - " + realSQL);
		MLookupInfo lInfo = new MLookupInfo(realSQL.toString(), TableName,
			TableName + "." + KeyColumn, ZoomWindow, ZoomWindowPO, zoomQuery);
		return lInfo;
	}	//	getLookup_TableDir

	/** Is Table Translated				*/
	private static final CCache< String, Boolean > s_isTranslated 
		= new CCache<String, Boolean>("AD_Table", 50);
	
	/**
	 * 	Not sure what the motivation of this - 
	 * 	the easy easy way to find out if the table is to check existence
	 * 	of translation table.
	 *	@param tableName
	 *	@return
	 */
	private static boolean isTranslated(String tableName)
	{
		boolean isTranslated = false;
		
		if (s_isTranslated.containsKey(tableName))
			return s_isTranslated.get(null, tableName);
		
		String sql1 = "SELECT count(*) "
			+ "FROM AD_Table t"
			+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID) "
			+ "WHERE TableName=?"
			+ " AND c.IsIdentifier='Y' " 
			+ " AND c.IsTranslated = 'Y' ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql1, (Trx) null);
			pstmt.setString(1, tableName);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				isTranslated = !(rs.getInt(1) == 0);
			}
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql1, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		s_isTranslated.put( tableName, isTranslated );
		return isTranslated;
	}	//	isTranslated

	/**
	 *  Get embedded SQL for TableDir Lookup (no translation)
	 *
	 *  @param language report language
	 *  @param ColumnName column name
	 *  @param BaseTable base table
	 *  @return SELECT Column FROM TableName WHERE BaseTable.ColumnName=TableName.ColumnName
	 */
	static public String getLookup_TableDirEmbed (Language language, String ColumnName, String BaseTable)
	{
		return getLookup_TableDirEmbed (language, ColumnName, BaseTable, ColumnName);
	}   //  getLookup_TableDirEmbed

	/**
	 *  Get embedded SQL for TableDir Lookup (no translation)
	 *
	 *  @param language report language
	 *  @param ColumnName column name
	 *  @param BaseTable base table
	 *  @param BaseColumn base column
	 *  @return SELECT Column FROM TableName WHERE BaseTable.BaseColumn=TableName.ColumnName
	 */
	static public String getLookup_TableDirEmbed (Language language,
		String ColumnName, String BaseTable, String BaseColumn)
	{
		String TableName = ColumnName.toUpperCase().endsWith("_ID") ? ColumnName.substring(0,ColumnName.length()-3) : ColumnName;
		String TrlTableName = TableName.concat("_Trl");
		boolean isTranslated = false;
		
		//	get display column name (first identifier column)
		String sql = "SELECT c.ColumnName,c.IsTranslated,c.AD_Reference_ID,c.AD_Reference_Value_ID "
			+ "FROM AD_Table t INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID) "
			+ "WHERE TableName=?"
			+ " AND c.IsIdentifier='Y' "
			+ "ORDER BY c.SeqNo";
		ArrayList<LookupDisplayColumn> list = new ArrayList<LookupDisplayColumn>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, TableName);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				LookupDisplayColumn ldc = new LookupDisplayColumn (rs.getString(1),
					"Y".equals(rs.getString(2)), rs.getInt(3), rs.getInt(4));
				list.add (ldc);
			//	s_log.fine("getLookup_TableDirEmbed: " + ColumnName + " - " + ldc);
			}
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql, e);
			return "";
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//  Do we have columns ?
		if (list.size() == 0)
		{
			s_log.log(Level.SEVERE, "No Identifier records found: " + ColumnName);
			return "";
		}

		//
		StringBuffer embedSQL = new StringBuffer("SELECT ");

		int size = list.size();
		for (int i = 0; i < size; i++)
		{
			if (i > 0)
				embedSQL.append("||' - '||" );
			LookupDisplayColumn ldc = list.get(i);

			//  date, number
			if (FieldType.isDate(ldc.DisplayType) || FieldType.isNumeric(ldc.DisplayType))
			{
				embedSQL.append(DB.TO_CHAR(TableName + "." + ldc.ColumnName, ldc.DisplayType, language.getAD_Language()));
			}
			//  TableDir
			else if ((ldc.DisplayType == DisplayTypeConstants.TableDir || ldc.DisplayType == DisplayTypeConstants.Search)
			  && ldc.ColumnName.toUpperCase().endsWith("_ID"))
			{
				String embeddedSQL = getLookup_TableDirEmbed(language, ldc.ColumnName, TableName);
				embedSQL.append("(").append(embeddedSQL).append(")");
			}
			//  String
			else
			{
				if(ldc.IsTranslated && !Env.isBaseLanguage(language, TableName)) {
					isTranslated = true;
					//jz EDB || problem
					if (DB.isPostgreSQL())
						embedSQL.append("COALESCE(TO_CHAR(").append(TrlTableName).append(".").append(ldc.ColumnName).append("),'')");
					else
						embedSQL.append(TrlTableName).append(".").append(ldc.ColumnName);
				}
				else {
					//jz EDB || problem
					if (DB.isPostgreSQL())
						embedSQL.append("COALESCE(TO_CHAR(").append(TableName).append(".").append(ldc.ColumnName).append("),'')");
					else
						embedSQL.append(TableName).append(".").append(ldc.ColumnName);
				}
					
			}
		}

		embedSQL.append(" FROM ").append(TableName);

	//  Translation
		if (isTranslated)
		{
				embedSQL.append(" INNER JOIN ").append(TableName).append("_Trl ON (")
					.append(TableName).append(".").append(ColumnName)
					.append("=").append(TableName).append("_Trl.").append(ColumnName)
					.append(" AND ").append(TableName).append("_Trl.AD_Language='")
					.append(language.getAD_Language()).append("')");
		}
		
		embedSQL.append(" WHERE ").append(BaseTable).append(".").append(BaseColumn);
		embedSQL.append("=").append(TableName).append(".").append(ColumnName);
		//
		return embedSQL.toString();
	}	//  getLookup_TableDirEmbed

	/**
	 *  Get embedded SQL for TableDir Lookup for virtual column
	 *
	 *  @param language report language
	 *  @param ColumnName column name
	 *  @param ColumnSQL columnSQL for virtual column
	 *  @return SELECT Column FROM TableName WHERE BaseTable.BaseColumn=columnSQL
	 */
	static public String getLookup_TableDirVirtualEmbed (Language language,
		String ColumnName, String ColumnSQL)
	{
		String TableName = ColumnName.toUpperCase().endsWith("_ID") ? ColumnName.substring(0,ColumnName.length()-3) : ColumnName;

		//	get display column name (first identifier column)
		String sql = "SELECT c.ColumnName,c.IsTranslated,c.AD_Reference_ID,c.AD_Reference_Value_ID "
			+ "FROM AD_Table t INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID) "
			+ "WHERE TableName=?"
			+ " AND c.IsIdentifier='Y' "
			+ "ORDER BY c.SeqNo";
		ArrayList<LookupDisplayColumn> list = new ArrayList<LookupDisplayColumn>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, TableName);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				LookupDisplayColumn ldc = new LookupDisplayColumn (rs.getString(1),
					"Y".equals(rs.getString(2)), rs.getInt(3), rs.getInt(4));
				list.add (ldc);
			//	s_log.fine("getLookup_TableDirEmbed: " + ColumnName + " - " + ldc);
			}
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql, e);
			return "";
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//  Do we have columns ?
		if (list.size() == 0)
		{
			s_log.log(Level.SEVERE, "No Identifier records found: " + ColumnName);
			return "";
		}

		//
		StringBuffer embedSQL = new StringBuffer("SELECT ");

		int size = list.size();
		for (int i = 0; i < size; i++)
		{
			if (i > 0)
				embedSQL.append("||' - '||" );
			LookupDisplayColumn ldc = list.get(i);

			//  date, number
			if (FieldType.isDate(ldc.DisplayType) || FieldType.isNumeric(ldc.DisplayType))
			{
				embedSQL.append(DB.TO_CHAR(TableName + "." + ldc.ColumnName, ldc.DisplayType, language.getAD_Language()));
			}
			//  TableDir
			else if ((ldc.DisplayType == DisplayTypeConstants.TableDir || ldc.DisplayType == DisplayTypeConstants.Search)
			  && ldc.ColumnName.toUpperCase().endsWith("_ID"))
			{
				String embeddedSQL = getLookup_TableDirEmbed(language, ldc.ColumnName, TableName);
				embedSQL.append("(").append(embeddedSQL).append(")");
			}
			//  String
			else
			{
				//jz EDB || problem
				if (DB.isPostgreSQL())
					embedSQL.append("COALESCE(TO_CHAR(").append(TableName).append(".").append(ldc.ColumnName).append("),'')");
				else
					embedSQL.append(TableName).append(".").append(ldc.ColumnName);
			}
		}

		embedSQL.append(" FROM ").append(TableName);
		embedSQL.append(" WHERE ");
		embedSQL.append(TableName).append(".").append(ColumnName);
		embedSQL.append("=").append(ColumnSQL);
		//
		return embedSQL.toString();
	}	//  getLookup_TableDirVirtualEmbed

	/**
	 * 	Lookup_Acct
	 *	@param ctx context
	 *	@param AD_Column_ID column
	 *	@return lookup for acct
	 */
	static private MLookupInfo getLookup_Acct (Ctx ctx, int AD_Column_ID)
	{
		MLookupInfo retValue = null;
		String sql = "SELECT C_ValidCombination_ID, NULL, Combination, IsActive FROM C_ValidCombination";
		int zoomWindow = 153;
		Query zoomQuery = new Query("C_ValidCombination");
		//
		retValue = new MLookupInfo (sql, "C_ValidCombination", 
			"C_ValidCombination.C_ValidCombination_ID", 
			zoomWindow, zoomWindow, zoomQuery);
		return retValue;
	}	//	getLookup_Acct

}   //  MLookupFactory

