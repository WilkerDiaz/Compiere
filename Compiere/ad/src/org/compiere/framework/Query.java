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
package org.compiere.framework;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Query Descriptor.
 * 	Maintains QueryRestrictions (WHERE clause)
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Query.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public class Query implements Serializable
{
	//cache the table id, expires after 15 minutes
	private static final CCache<String,Integer>	s_tableCache	= new CCache<String,Integer>("AD_Table", 20, 15);
	/**
	 *	Get Query from Parameter
	 *	@param ctx context (to determine language)
	 *  @param AD_PInstance_ID instance
	 *  @param TableName table name
	 *  @return where clause
	 */
	static public Query get (Ctx ctx, int AD_PInstance_ID, String TableName)
	{
		s_log.info("AD_PInstance_ID=" + AD_PInstance_ID + ", TableName=" + TableName);
		Query query = new Query(TableName);
		//	Temporary Tables - add qualifier (not displayed)
		MTable table = MTable.get(ctx, TableName);
		if (table!=null && table.isReportingTable())
			query.addRestriction(TableName + ".AD_PInstance_ID=" + AD_PInstance_ID);

		//	How many rows do we have?
		int rows = 0;
		String SQL = "SELECT COUNT(*) FROM AD_PInstance_Para WHERE AD_PInstance_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(SQL, (Trx) null);
			pstmt.setInt(1, AD_PInstance_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				rows = rs.getInt(1);
		}
		catch (SQLException e1) {
			s_log.log(Level.SEVERE, SQL, e1);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (rows < 1)
			return query;

		//	Msg.getMsg(Env.getCtx(), "Parameter")
		boolean trl = !Env.isBaseLanguage(ctx, "AD_Process_Para");
		if (!trl)
			SQL = "SELECT ip.ParameterName,ip.P_String,ip.P_String_To,"			//	1..3
				+ "ip.P_Number,ip.P_Number_To,"									//	4..5
				+ "ip.P_Date,ip.P_Date_To, ip.Info,ip.Info_To, "				//	6..9
				+ "pp.Name, pp.IsRange "										//	10..11
				+ "FROM AD_PInstance_Para ip, AD_PInstance i, AD_Process_Para pp "
				+ "WHERE i.AD_PInstance_ID=ip.AD_PInstance_ID"
				+ " AND pp.AD_Process_ID=i.AD_Process_ID"
				+ " AND pp.ColumnName=ip.ParameterName"
				+ " AND ip.AD_PInstance_ID=?";
		else
			SQL = "SELECT ip.ParameterName,ip.P_String,ip.P_String_To, ip.P_Number,ip.P_Number_To,"
				+ "ip.P_Date,ip.P_Date_To, ip.Info,ip.Info_To, "
				+ "ppt.Name, pp.IsRange "
				+ "FROM AD_PInstance_Para ip, AD_PInstance i, AD_Process_Para pp, AD_Process_Para_Trl ppt "
				+ "WHERE i.AD_PInstance_ID=ip.AD_PInstance_ID"
				+ " AND pp.AD_Process_ID=i.AD_Process_ID"
				+ " AND pp.ColumnName=ip.ParameterName"
				+ " AND pp.AD_Process_Para_ID=ppt.AD_Process_Para_ID"
				+ " AND ip.AD_PInstance_ID=?"
				+ " AND ppt.AD_Language=?";
		try
		{
			pstmt = DB.prepareStatement(SQL, (Trx) null);
			pstmt.setInt(1, AD_PInstance_ID);
			if (trl)
				pstmt.setString(2, Env.getAD_Language(ctx));
			rs = pstmt.executeQuery();
			//	all records
			for (int row = 0; rs.next(); row++)
			{
				if (row == rows)
				{
					s_log.log(Level.SEVERE, "(Parameter) - more rows than expected");
					break;
				}
				String ParameterName = rs.getString(1);
				String P_String = rs.getString(2);
				String P_String_To = rs.getString(3);
				//
				Double P_Number = null;
				double d = rs.getDouble(4);
				if (!rs.wasNull())
					P_Number = new Double(d);
				Double P_Number_To = null;
				d = rs.getDouble(5);
				if (!rs.wasNull())
					P_Number_To = new Double(d);
				//
				Timestamp P_Date = rs.getTimestamp(6);
				Timestamp P_Date_To = rs.getTimestamp(7);
				//
				String Info = rs.getString(8);
				String Info_To = rs.getString(9);
				//
				String Name = rs.getString(10);
				boolean isRange = "Y".equals(rs.getString(11));
				//
				s_log.fine(ParameterName + " S=" + P_String + "-" + P_String_To
					+ ", N=" + P_Number + "-" + P_Number_To + ", D=" + P_Date + "-" + P_Date_To
					+ "; Name=" + Name + ", Info=" + Info + "-" + Info_To + ", Range=" + isRange);

				//-------------------------------------------------------------
				if (P_String != null)
				{
					if (P_String_To == null)
					{
						if (P_String.indexOf("%") == -1)
							query.addRestriction(ParameterName, Query.EQUAL, 
								P_String, Name, Info);
						else
							query.addRestriction(ParameterName, Query.LIKE, 
								P_String, Name, Info);
					}
					else
						query.addRangeRestriction(ParameterName, 
							P_String, P_String_To, Name, Info, Info_To);
				}
				//	Number
				else if (P_Number != null || P_Number_To != null)
				{
					if (P_Number_To == null)
					{
						if (isRange)
							query.addRestriction(ParameterName, Query.GREATER_EQUAL, 
								P_Number, Name, Info);
						else
							query.addRestriction(ParameterName, Query.EQUAL, 
								P_Number, Name, Info);
					}
					else	//	P_Number_To != null
					{
						if (P_Number == null)
							query.addRestriction("TRUNC("+ParameterName+",'DD')", Query.LESS_EQUAL, 
								P_Number_To, Name, Info);
						else
							query.addRangeRestriction(ParameterName, 
								P_Number, P_Number_To, Name, Info, Info_To);
					}
				}
				//	Date
				else if (P_Date != null || P_Date_To != null)
				{
					if (P_Date_To == null)
					{
						if (isRange)
							query.addRestriction("TRUNC("+ParameterName+",'DD')", Query.GREATER_EQUAL, 
								P_Date, Name, Info);
						else
							query.addRestriction("TRUNC("+ParameterName+",'DD')", Query.EQUAL, 
								P_Date, Name, Info);
					}
					else	//	P_Date_To != null
					{
						if (P_Date == null)
							query.addRestriction("TRUNC("+ParameterName+",'DD')", Query.LESS_EQUAL, 
								P_Date_To, Name, Info);
						else
							query.addRangeRestriction("TRUNC("+ParameterName+",'DD')", 
								P_Date, P_Date_To, Name, Info, Info_To);
					}
				}
			}
		}
		catch (SQLException e2) {
			s_log.log(Level.SEVERE, SQL, e2);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		s_log.info(query.toString());
		return query;
	}	//	get
	
	
	/**
	 * 	Get Zoom Column Name.
	 * 	Converts Synonyms like SalesRep_ID to AD_User_ID
	 *	@param columnName column name
	 *	@return column name
	 */
	public static String getZoomColumnName (String columnName)
	{
		if (columnName == null)
			return null;
		if (columnName.equals("SalesRep_ID"))
			return "AD_User_ID";
		if (columnName.equals("C_DocTypeTarget_ID"))
			return "C_DocType_ID";
		if (columnName.equals("Bill_BPartner_ID"))
			return "C_BPartner_ID";
		if (columnName.equals("Bill_Location_ID"))
			return "C_BPartner_Location_ID";
		if (columnName.equals("Account_ID"))
			return "C_ElementValue_ID"; 
		//	See also MTab.validateQuery
		//
		return columnName;
	}	//	getZoomColumnName
	
	/**
	 * 	Derive Zoom Table Name from column name.
	 * 	(e.g. drop _ID)
	 *	@param columnName  column name
	 *	@return table name
	 */
	public static String getZoomTableName (String columnName)
	{
		String tableName = getZoomColumnName(columnName);
		int index = tableName.lastIndexOf("_ID");
		if (index != -1)
			return tableName.substring(0, index);
		return tableName;
	}	//	getZoomTableName

	
	/*************************************************************************
	 * 	Create simple Equal Query.
	 *  Creates columnName=value or columnName='value'
	 * 	@param columnName columnName
	 * 	@param value value
	 * 	@return query
	 */
	public static Query getEqualQuery (String columnName, Object value)
	{
		Query query = new Query();
		query.addRestriction(columnName, EQUAL, value);
		query.setRecordCount(1);	//	guess
		return query;
	}	//	getEqualQuery

	/**
	 * 	Create simple Equal Query.
	 *  Creates columnName=value
	 * 	@param columnName columnName
	 * 	@param value value
	 * 	@return query
	 */
	public static Query getEqualQuery (String columnName, int value)
	{
		Query query = new Query();
		if (columnName.toUpperCase().endsWith("_ID"))
			query.setTableName(columnName.substring(0, columnName.length()-3));
		query.addRestriction(columnName, EQUAL, Integer.valueOf(value));
		query.setRecordCount(1);	//	guess
		return query;
	}	//	getEqualQuery

	/**
	 * 	Create No Record query.
	 * 	@param tableName table name
	 * 	@param newRecord new Record Indicator (2=3) 
	 * 	@return query
	 */
	public static Query getNoRecordQuery (String tableName, boolean newRecord)
	{
		Query query = new Query(tableName);
		if (newRecord)
			query.addRestriction(NEWRECORD);
		else
			query.addRestriction("1=2");
		query.setRecordCount(0);
		return query;
	}	//	getNoRecordQuery
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (Query.class);
	
	
	/**************************************************************************
	 *	Constructor w/o table name
	 */
	public Query ()
	{
	}	//	MQuery

	/**
	 *	Constructor
	 *  @param TableName Table Name
	 */
	public Query (String TableName)
	{
		m_TableName = TableName;
	}	//	MQuery

	/**
	 * 	Constructor get TableNAme from Table
	 * 	@param AD_Table_ID Table_ID
	 */
	public Query (int AD_Table_ID)
	{	//	Use Client Context as r/o
		m_TableName = MTable.getTableName (Env.getCtx(), AD_Table_ID);
	}	//	MQuery

	/**	Serialization Info	**/
	static final long serialVersionUID = 1511402030597166113L;

	/**	Table Name					*/
	private String		m_TableName = "";
	/**	List of QueryRestrictions		*/
	private ArrayList<QueryRestriction>	m_list = new ArrayList<QueryRestriction>();
	/**	Record Count				*/
	private int			m_recordCount = 999999;
	/** New Record Query			*/
	private boolean		m_newRecord = false;
	/** New Record String			*/
	private static final String	NEWRECORD = "2=3";
	
	/** This query represents Web Report parameters	only*/
	private boolean 	m_reportParameter = false;

	/**
	 * 	Get Record Count
	 *	@return count - default 999999
	 */
	public int getRecordCount()
	{
		return m_recordCount;
	}	//	getRecordCount
	
	/**
	 * 	Set Record Count
	 *	@param count count
	 */
	public void setRecordCount(int count)
	{
		m_recordCount = count;
	}	//	setRecordCount
	
	
	/** Equal 			*/
	public static final String	EQUAL = "=";
	/** Equal - 0		*/
	public static final int		EQUAL_INDEX = 0;
	/** Not Equal		*/
	public static final String	NOT_EQUAL = "!=";
	/** Like			*/
	public static final String	LIKE = " LIKE ";
	/** Not Like		*/
	public static final String	NOT_LIKE = " NOT LIKE ";
	/** Greater			*/
	public static final String	GREATER = ">";
	/** Greater Equal	*/
	public static final String	GREATER_EQUAL = ">=";
	/** Less			*/
	public static final String	LESS = "<";
	/** Less Equal		*/
	public static final String	LESS_EQUAL = "<=";
	/** Between			*/
	public static final String	BETWEEN = " BETWEEN ";
	/** Between - 8		*/
	public static final int		BETWEEN_INDEX = 8;
	/** IN			*/
	public static final String	IN = " IN ";
	/** NOT IN			*/
	public static final String	NOT_IN = " NOT IN ";

	/**	Operators for Strings				*/
	public static final ValueNamePair[]	OPERATORS = new ValueNamePair[] {
		new ValueNamePair (EQUAL,			" = "),		//	0
		new ValueNamePair (NOT_EQUAL,		" != "),
		new ValueNamePair (LIKE,			" ~ "),
		new ValueNamePair (NOT_LIKE,		" !~ "),
		new ValueNamePair (GREATER,			" > "),
		new ValueNamePair (GREATER_EQUAL,	" >= "),	//	5
		new ValueNamePair (LESS,			" < "),
		new ValueNamePair (LESS_EQUAL,		" <= "),
		new ValueNamePair (BETWEEN,			" >-< ")	//	8
	//	,new ValueNamePair (IN,				" () "),
	//	new ValueNamePair (NOT_IN,			" !() ")			
	};
	/**	Operators for IDs					*/
	public static final ValueNamePair[]	OPERATORS_ID = new ValueNamePair[] {
		new ValueNamePair (EQUAL,			" = "),		//	0
		new ValueNamePair (NOT_EQUAL,		" != ")
	//	,new ValueNamePair (IN,				" IN "),			
	//	new ValueNamePair (NOT_IN,			" !() ")			
	};
	/**	Operators for Boolean					*/
	public static final ValueNamePair[]	OPERATORS_YN = new ValueNamePair[] {
		new ValueNamePair (EQUAL,			" = ")
	};

	
	/*************************************************************************
	 * 	Add Query Restriction
	 * 	@param ColumnName ColumnName
	 * 	@param Operator Operator, e.g. = != ..
	 * 	@param Code Code, e.g 0, All%
	 *  @param InfoName Display Name
	 * 	@param InfoDisplay Display of Code (Lookup)
	 */
	public void addRestriction (String ColumnName, String Operator,
		Object Code, String InfoName, String InfoDisplay)
	{
		QueryRestriction r = new QueryRestriction (ColumnName, Operator,
			Code, InfoName, InfoDisplay);
		m_list.add(r);
	}	//	addRestriction

	/**
	 * 	Add Query Restriction
	 * 	@param ColumnName ColumnName
	 * 	@param Operator Operator, e.g. = != ..
	 * 	@param Code Code, e.g 0, All%
	 */
	public void addRestriction (String ColumnName, String Operator,
		Object Code)
	{
		QueryRestriction r = new QueryRestriction (ColumnName, Operator,
			Code, null, null);
		m_list.add(r);
	}	//	addRestriction

	/**
	 * 	Add Restriction
	 * 	@param ColumnName ColumnName
	 * 	@param Operator Operator, e.g. = != ..
	 * 	@param Code Code, e.g 0
	 */
	public void addRestriction (String ColumnName, String Operator, int Code)
	{
		QueryRestriction r = new QueryRestriction (ColumnName, Operator,
			Integer.valueOf(Code), null, null);
		m_list.add(r);
	}	//	addRestriction

	/**
	 * 	Add Range Restriction (BETWEEN)
	 * 	@param ColumnName ColumnName
	 * 	@param Code Code, e.g 0, All%
	 * 	@param Code_to Code, e.g 0, All%
	 *  @param InfoName Display Name
	 * 	@param InfoDisplay Display of Code (Lookup)
	 * 	@param InfoDisplay_to Display of Code (Lookup)
	 */
	public void addRangeRestriction (String ColumnName,
		Object Code, Object Code_to,
		String InfoName, String InfoDisplay, String InfoDisplay_to)
	{
		QueryRestriction r = new QueryRestriction (ColumnName, Code, Code_to,
			InfoName, InfoDisplay, InfoDisplay_to);
		m_list.add(r);
	}	//	addRangeRestriction

	/**
	 * 	Add Range Restriction (BETWEEN)
	 * 	@param ColumnName ColumnName
	 * 	@param Code Code, e.g 0, All%
	 * 	@param Code_to Code, e.g 0, All%
	 */
	public void addRangeRestriction (String ColumnName,
		Object Code, Object Code_to)
	{
		QueryRestriction r = new QueryRestriction (ColumnName, Code, Code_to,
			null, null, null);
		m_list.add(r);
	}	//	addRangeRestriction

	/**
	 * 	Add Query Restriction
	 * 	@param r QueryRestriction
	 */
	public void addRestriction (QueryRestriction r)
	{
		m_list.add(r);
	}	//	addRestriction

	/**
	 * 	Add QueryRestriction
	 * 	@param whereClause SQL WHERE clause
	 */
	public void addRestriction (String whereClause)
	{
		if (whereClause == null || whereClause.trim().length() == 0)
			return;
		QueryRestriction r = new QueryRestriction (whereClause);
		m_list.add(r);
		m_newRecord = whereClause.equals(NEWRECORD);
	}	//	addRestriction

	/**
	 * 	New Record Query
	 *	@return true if new record query
	 */
	public boolean isNewRecordQuery()
	{
		return m_newRecord;
	}	//	isNewRecord
	
	/*************************************************************************
	 * 	Create the resulting Query WHERE Clause
	 * 	@return Where Clause
	 */
	public String getWhereClause ()
	{
		return getWhereClause(false);
	}	//	getWhereClause

	/**
	 * 	Create the resulting Query WHERE Clause
	 * 	@param fullyQualified fully qualified Table.ColumnName
	 * 	@return Where Clause
	 */
	public String getWhereClause (boolean fullyQualified)
	{
		boolean qualified = fullyQualified;
		if (qualified && (m_TableName == null || m_TableName.length() == 0))
			qualified = false;
		//
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < m_list.size(); i++)
		{
			QueryRestriction r = m_list.get(i);
			if (i != 0)
				sb.append(r.AndCondition ? " AND " : " OR ");
			if (qualified)
				sb.append(r.getSQL(m_TableName));
			else
				sb.append(r.getSQL(null));
		}
		return sb.toString();
	}	//	getWhereClause

	/**
	 * 	Get printable Query Info
	 *	@return info
	 */
	public String getInfo ()
	{
		StringBuffer sb = new StringBuffer();
		if (m_TableName != null)
			sb.append(m_TableName).append(": ");
		//
		for (int i = 0; i < m_list.size(); i++)
		{
			QueryRestriction r = m_list.get(i);
			if (i != 0)
				sb.append(r.AndCondition ? " AND " : " OR ");
			//
			sb.append(r.getInfoName())
				.append(r.getInfoOperator())
				.append(r.getInfoDisplayAll());
		}
		return sb.toString();
	}	//	getInfo

	
	/**
	 * 	Create Query WHERE Clause.
	 *  Not fully qualified
	 * 	@param index QueryRestriction index
	 * 	@return Where Clause or "" if not valid
	 */
	public String getWhereClause (int index)
	{
		StringBuffer sb = new StringBuffer();
		if (index >= 0 && index < m_list.size())
		{
			QueryRestriction r = m_list.get(index);
			sb.append(r.getSQL(null));
		}
		return sb.toString();
	}	//	getWhereClause

	/**
	 * 	Get Query Restriction Count
	 * 	@return number of restricctions
	 */
	public int getRestrictionCount()
	{
		return m_list.size();
	}	//	getRestrictionCount

	/**
	 * 	Is Query Active
	 * 	@return true if number of restricctions > 0
	 */
	public boolean isActive()
	{
		return m_list.size() != 0;
	}	//	isActive

	/**
	 * 	Get Table Name
	 * 	@return Table Name
	 */
	public String getTableName ()
	{
		return m_TableName;
	}	//	getTableName

	/**
	 * 	Set Table Name
	 * 	@param TableName Table Name
	 */
	public void setTableName (String TableName)
	{
		m_TableName = TableName;
	}	//	setTableName

	
	/*************************************************************************
	 * 	Get ColumnName of index
	 * 	@param index index
	 * 	@return ColumnName
	 */
	public String getColumnName (int index)
	{
		if (index < 0 || index >= m_list.size())
			return null;
		QueryRestriction r = m_list.get(index);
		return r.ColumnName;
	}	//	getColumnName

	/**
	 * 	Set ColumnName of index
	 * 	@param index index
	 *  @param ColumnName new column name
	 */
	public void setColumnName (int index, String ColumnName)
	{
		if (index < 0 || index >= m_list.size())
			return;
		QueryRestriction r = m_list.get(index);
		r.ColumnName = ColumnName;
	}	//	setColumnName

	/**
	 * 	Get Operator of index
	 * 	@param index index
	 * 	@return Operator
	 */
	public String getOperator (int index)
	{
		if (index < 0 || index >= m_list.size())
			return null;
		QueryRestriction r = m_list.get(index);
		return r.Operator;
	}	//	getOperator

	/**
	 * 	Get Operator of index
	 * 	@param index index
	 * 	@return Operator
	 */
	public Object getCode (int index)
	{
		if (index < 0 || index >= m_list.size())
			return null;
		QueryRestriction r = m_list.get(index);
		return r.Code;
	}	//	getCode

	/**
	 * 	Get Operator of index
	 * 	@param index index
	 * 	@return Operator
	 */
	public Object getCodeTo (int index)
	{
		if (index < 0 || index >= m_list.size())
			return null;
		QueryRestriction r = m_list.get(index);
		return r.Code_to;
	}	//	getCodeTo
	
	/**
	 * 	Get QueryRestriction Display of index
	 * 	@param index index
	 * 	@return QueryRestriction Display
	 */
	public String getInfoDisplay (int index)
	{
		if (index < 0 || index >= m_list.size())
			return null;
		QueryRestriction r = m_list.get(index);
		return r.InfoDisplay;
	}	//	getOperator

	/**
	 * 	Get TO QueryRestriction Display of index
	 * 	@param index index
	 * 	@return QueryRestriction Display
	 */
	public String getInfoDisplay_to (int index)
	{
		if (index < 0 || index >= m_list.size())
			return null;
		QueryRestriction r = m_list.get(index);
		return r.InfoDisplay_to;
	}	//	getOperator

	/**
	 * 	Get Info Name
	 * 	@param index index
	 * 	@return Info Name
	 */
	public String getInfoName(int index)
	{
		if (index < 0 || index >= m_list.size())
			return null;
		QueryRestriction r = m_list.get(index);
		return r.InfoName;
	}	//	getInfoName

	/**
	 * 	Get Info Operator
	 * 	@param index index
	 * 	@return info Operator
	 */
	public String getInfoOperator(int index)
	{
		if (index < 0 || index >= m_list.size())
			return null;
		QueryRestriction r = m_list.get(index);
		return r.getInfoOperator();
	}	//	getInfoOperator

	/**
	 * 	Get Display with optional To
	 * 	@param index index
	 * 	@return info display
	 */
	public String getInfoDisplayAll (int index)
	{
		if (index < 0 || index >= m_list.size())
			return null;
		QueryRestriction r = m_list.get(index);
		return r.getInfoDisplayAll();
	}	//	getInfoDisplay

	/**
	 * 	String representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		if (isActive())
			return getWhereClause(true);
		return "MQuery[" + m_TableName + ",QueryRestrictions=0]";
	}	//	toString
	
	/**
	 * 	Get Display Name
	 *	@param ctx context
	 *	@return display Name
	 */
	public String getDisplayName(Ctx ctx)
	{
		String keyColumn = null;
		if (m_TableName != null)
			keyColumn = m_TableName + "_ID";
		else
			keyColumn = getColumnName(0);
		String retValue = Msg.translate(ctx, keyColumn);
		if (retValue != null && retValue.length() > 0)
			return retValue;
		return m_TableName;
	}	//	getDisplayName

	/**
	 * 	Clone Query
	 * 	@return Query
	 */
	public Query deepCopy()
	{
		Query newQuery = new Query(m_TableName);
		for (int i = 0; i < m_list.size(); i++)
			newQuery.addRestriction(m_list.get(i));
		return newQuery;
	}	//	clone
	
	/**
	 * 	Set Report Parameter
	 * 	@param value 
	 */
	public void setReportParameter(boolean value){
		m_reportParameter = value;
	}
	
	/**
	 * 	Is Report Parameter
	 * 	@return true if query is marked as a Report Parameter
	 */
	public boolean isReportParameter(){
		return m_reportParameter;
	}
	
	public static int getZoomAD_Window_ID (int AD_Table_ID, int Record_ID)
	{
		String TableName = null;
		int AD_Window_ID = 0;
		int PO_Window_ID = 0;
		String sql = "SELECT TableName, AD_Window_ID, PO_Window_ID FROM AD_Table WHERE AD_Table_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Table_ID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				TableName = rs.getString(1);
				AD_Window_ID = rs.getInt(2);
				PO_Window_ID = rs.getInt(3);
			}
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//  Nothing to Zoom to
		if (TableName == null || AD_Window_ID == 0)
			return -1;

		//	PO Zoom ?
		if (PO_Window_ID != 0)
		{
			String whereClause = TableName + "_ID=" + Record_ID;			
			ArrayList<KeyNamePair>	zoomList = ZoomTarget.getZoomTargets(TableName, 0, whereClause);

			if(zoomList != null && zoomList.size()>0)
				AD_Window_ID=zoomList.get(0).getKey();

			if (AD_Window_ID == 0)
				return -1;
		}
		return AD_Window_ID;
	}	//	zoom

	public static int getAD_TableID(String tableName){
		Integer ret = s_tableCache.get(null, tableName);
		if(ret != null)
			return ret;
		
		String sql="SELECT AD_Table_ID FROM AD_Table WHERE upper(TableName) = ?";
		int AD_Table_ID = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			pstmt = DB.prepareStatement(sql,(Trx) null);
			pstmt.setString(1, tableName);
			rs = pstmt.executeQuery();
			if(rs.next()){
				AD_Table_ID = rs.getInt(1);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		s_tableCache.put(tableName, AD_Table_ID);
		return AD_Table_ID;
	}
}	//	MQuery
