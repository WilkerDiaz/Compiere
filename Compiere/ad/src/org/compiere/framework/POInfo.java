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
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Persistent Object Info.
 *  Provides structural information
 *
 *  @author Jorg Janke
 *  @version $Id: POInfo.java 8751 2010-05-12 16:49:31Z nnayak $
 */
public class POInfo implements Serializable
{
	/** Used by Remote FinReport			*/
	static final long serialVersionUID = -5976719579744948419L;

	/**
	 *  POInfo Factory
	 *  @param ctx context
	 *  @param AD_Table_ID AD_Table_ID
	 *  @return POInfo
	 */
	public static POInfo getPOInfo (Ctx ctx, int AD_Table_ID)
	{
		Integer key = Integer.valueOf(AD_Table_ID);
		POInfo retValue = s_cache.get(null, key);
		if (retValue == null)
		{
			String AD_Language = Env.getAD_Language(ctx);
			retValue = new POInfo(AD_Language, AD_Table_ID, false);
			if (retValue.getColumnCount() == 0)
				//	May be run before Language verification
				retValue = new POInfo(AD_Language, AD_Table_ID, true);
			else
				s_cache.put(key, retValue);
		}
		return retValue;
	}   //  getPOInfo

	/** Cache of POInfo     */
	private static final CCache<Integer,POInfo>  s_cache = new CCache<Integer,POInfo>("POInfo", 200, "AD_Table");

	/**************************************************************************
	 *  Create Persistent Info
	 * @param AD_Language AD_Language
	 * @param AD_Table_ID AD_ Table_ID
	 * @param baseLanguageOnly get in base language
	 */
	private POInfo (String AD_Language, int AD_Table_ID, boolean baseLanguageOnly)
	{
		m_AD_Table_ID = AD_Table_ID;
		boolean baseLanguage = baseLanguageOnly ? true : Env.isBaseLanguage(AD_Language, "AD_Table");
		loadInfo (baseLanguage, AD_Language);
		if(getColumnCount() != 0)
			setKeyInfo();
	}   //  PInfo

	/** Table_ID            	*/
	private final int         m_AD_Table_ID;
	/** Table Name          	*/
	private String      m_TableName = null;
	/** Access Level			*/
	private String		m_AccessLevel = X_AD_Table.ACCESSLEVEL_Organization;
	/** Transaction Table		*/
	private String		m_TableTrxType = X_AD_Table.TABLETRXTYPE_OptionalOrganization;
	/** Columns             	*/
	private POInfoColumn[]    m_columns = null;
	/** Table has Key Column	*/
	private boolean		m_hasKeyColumn = false;
	/** Hash map of column name to column index */
	private final HashMap<String,Integer> m_columnIndexes = new HashMap<String, Integer>();

	/** Key Columns					*/
	private String[]         	m_KeyColumns = null;

	/** Load SQL */
	private String m_loadSQL = null;
	/** Where Clause */
	private String m_whereClause = null;

	/**
	 * Load Table/Column Info
	 * @param baseLanguage in English
	 * @param AD_Language TODO
	 */
	private void loadInfo (boolean baseLanguage, String AD_Language)
	{
		ArrayList<POInfoColumn> list = new ArrayList<POInfoColumn>(15);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.TableName, c.ColumnName,c.AD_Reference_ID,"    //  1..3
			+ "c.IsMandatory,c.IsUpdateable,c.DefaultValue,"                //  4..6
			+ "e.Name,e.Description, c.AD_Column_ID, "						//  7..9
			+ "c.IsKey,c.IsParent, "										//	10..11
			+ "c.AD_Reference_Value_ID, vr.Code, "							//	12..13
			+ "c.FieldLength, c.ValueMin, c.ValueMax, c.IsTranslated, "		//	14..17
			+ "t.AccessLevel, c.ColumnSQL, c.IsEncrypted, "					//	18..20
			+ "t.TableTrxType ");											//	21
		sql.append("FROM AD_Table t"
			+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID)"
			+ " LEFT OUTER JOIN AD_Val_Rule vr ON (c.AD_Val_Rule_ID=vr.AD_Val_Rule_ID)"
			+ " INNER JOIN AD_Element");
		if (!baseLanguage)
			sql.append("_Trl");
		sql.append(" e "
			+ " ON (c.AD_Element_ID=e.AD_Element_ID) "
			+ "WHERE t.AD_Table_ID=?"
			+ " AND c.IsActive='Y'");
		if (!baseLanguage)
			sql.append(" AND e.AD_Language='").append(AD_Language).append("'");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int columnIndex = 0;
			pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			pstmt.setInt(1, m_AD_Table_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				if (m_TableName == null)
					m_TableName = rs.getString(1);
				String ColumnName = rs.getString(2);
				int AD_Reference_ID = rs.getInt(3);
				boolean IsMandatory = "Y".equals(rs.getString(4));
				boolean IsUpdateable = "Y".equals(rs.getString(5));
				String DefaultLogic = rs.getString(6);
				String Name = rs.getString(7);
				String Description = rs.getString(8);
				int AD_Column_ID = rs.getInt(9);
				boolean IsKey = "Y".equals(rs.getString(10));
				if (IsKey)
					m_hasKeyColumn = true;
				boolean IsParent = "Y".equals(rs.getString(11));
				int AD_Reference_Value_ID = rs.getInt(12);
				String ValidationCode = rs.getString(13);
				int FieldLength = rs.getInt(14);
				String ValueMin = rs.getString(15);
				String ValueMax = rs.getString(16);
				boolean IsTranslated = "Y".equals(rs.getString(17));
				//
				m_AccessLevel = rs.getString(18);
				String ColumnSQL = rs.getString(19);
				boolean IsEncrypted = "Y".equals(rs.getString(20));
				m_TableTrxType = rs.getString(21);
				//
				POInfoColumn col = new POInfoColumn (
					AD_Column_ID, ColumnName, ColumnSQL, AD_Reference_ID,
					IsMandatory, IsUpdateable,
					DefaultLogic, Name, Description,
					IsKey, IsParent,
					AD_Reference_Value_ID, ValidationCode,
					FieldLength, ValueMin, ValueMax,
					IsTranslated, IsEncrypted);
				list.add(col);
				
				m_columnIndexes.put(ColumnName, Integer.valueOf(columnIndex));
				++columnIndex;
			}
		}
		catch (SQLException e) {
			CLogger.get().log(Level.SEVERE, sql.toString(), e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//  convert to array
		m_columns = new POInfoColumn[list.size()];
		list.toArray(m_columns);
	}   //  loadInfo

	/**
	 *  String representation
	 *  @return String Representation
	 */
	@Override
	public String toString()
	{
		return "POInfo[" + getTableName() + ",AD_Table_ID=" + getAD_Table_ID() + "]";
	}   //  toString

	/**
	 *  String representation for index
	 * 	@param index column index
	 *  @return String Representation
	 */
	public String toString (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return "POInfo[" + getTableName() + "-(InvalidColumnIndex=" + index + ")]";
		return "POInfo[" + getTableName() + "-" + m_columns[index].toString() + "]";
	}   //  toString

	/**
	 *  Get Table Name
	 *  @return Table Name
	 */
	public String getTableName()
	{
		return m_TableName;
	}   //  getTableName

	/**
	 *  Get AD_Table_ID
	 *  @return AD_Table_ID
	 */
	public int getAD_Table_ID()
	{
		return m_AD_Table_ID;
	}   //  getAD_Table_ID

	/**
	 * 	Table has a Key Column
	 *	@return true if has a key column
	 */
	public boolean hasKeyColumn()
	{
		return m_hasKeyColumn;
	}	//	hasKeyColumn

	/**
	 * 	Get Table Access Level
	 *	@return Table.ACCESS.. 1=Org
	 */
	public String getAccessLevel()
	{
		return m_AccessLevel;
	}	//	getAccessLevel

	/**
	 * 	Get Transaction Type
	 *	@return p_trx type
	 */
	public String getTableTrxType()
	{
		return m_TableTrxType;
	}	//	getTableTrxType

	public boolean isTrxTable()
	{
		return X_AD_Table.TABLETRXTYPE_MandatoryOrganization.equals(m_TableTrxType);
	}

	/**
	 * 	Is Log Table
	 *	@return true if table is used for logging
	 */
	public boolean isLogTable()
	{
		if (m_TableName.endsWith("Log")
			|| m_TableName.indexOf("Audit") != -1)
			return true;
		if (m_TableName.startsWith("AD_PInstance"))
			return true;
		if (m_TableName.startsWith("AD_Session"))
			return true;
		//	AD_TaskInstance ?
		return false;
	}	//	isLogTable

	/**************************************************************************
	 *  Get ColumnCount
	 *  @return column count
	 */
	public int getColumnCount()
	{
		return m_columns.length;
	}   //  getColumnCount

	/**
	 *  Get Column Index
	 *  @param ColumnName column name
	 *  @return index of column with ColumnName or -1 if not found
	 */
	public int getColumnIndex(String ColumnName) {
		Integer index = m_columnIndexes.get(ColumnName);
		if (index != null)
			return index.intValue();

		if (DB.isMSSQLServer()) {
			if (ColumnName.equals("LineNo")) {
				index = m_columnIndexes.get("[LineNo]");
			}
		}
		return index != null ? index.intValue() : -1;
	} // getColumnIndex

	/**
	 *  Get Column Index
	 *  @param AD_Column_ID column
	 *  @return index of column with ColumnName or -1 if not found
	 */
	public int getColumnIndex (int AD_Column_ID)
	{
		for (int i = 0; i < m_columns.length; i++)
		{
			if (AD_Column_ID == m_columns[i].AD_Column_ID)
				return i;
		}
		return -1;
	}   //  getColumnIndex

	/**
	 *  Get Column
	 *  @param columnName column name
	 *  @return column
	 */
	protected POInfoColumn getColumn (String columnName)
	{
		int index = getColumnIndex(columnName);
		return getColumn(index);
	}   //  getColumn

	/**
	 *  Get Column
	 *  @param index index
	 *  @return column
	 */
	protected POInfoColumn getColumn (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		return m_columns[index];
	}   //  getColumn

	/**
	 *  Get Column Name
	 *  @param index index
	 *  @return ColumnName column name
	 */
	public String getColumnName (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		return m_columns[index].ColumnName;
	}   //  getColumnName

	/**
	 *  Get Column SQL or Column Name
	 *  @param index index
	 *  @return ColumnSQL column sql or name
	 */
	public String getColumnSQL (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		if (m_columns[index].ColumnSQL != null && m_columns[index].ColumnSQL.length() > 0)
			return m_columns[index].ColumnSQL + " AS " + m_columns[index].ColumnName;
		return m_columns[index].ColumnName;
	}   //  getColumnSQL

	/**
	 *  Is Column Virtal?
	 *  @param index index
	 *  @return true if column is virtual
	 */
	public boolean isVirtualColumn (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return true;
		return m_columns[index].ColumnSQL != null
			&& m_columns[index].ColumnSQL.length() > 0;
	}   //  isVirtualColumn

	/**
	 *  Get Column Label
	 *  @param index index
	 *  @return column label
	 */
	public String getColumnLabel (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		return m_columns[index].ColumnLabel;
	}   //  getColumnLabel

	/**
	 *  Get Column Description
	 *  @param index index
	 *  @return column description
	 */
	public String getColumnDescription (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		return m_columns[index].ColumnDescription;
	}   //  getColumnDescription

	/**
	 *  Get Column Class
	 *  @param index index
	 *  @return Class
	 */
	public Class<?> getColumnClass (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		return m_columns[index].ColumnClass;
	}   //  getColumnClass

	/**
	 *  Get Column Display Type
	 *  @param index index
	 *  @return DisplayType
	 */
	public int getColumnDisplayType (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return DisplayTypeConstants.String;
		return m_columns[index].DisplayType;
	}   //  getColumnDisplayType

	/**
	 *  Get Column Default Logic
	 *  @param index index
	 *  @return Default Logic
	 */
	public String getDefaultLogic (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return null;
		return m_columns[index].DefaultLogic;
	}   //  getDefaultLogic

	/**
	 *  Is Column Mandatory
	 *  @param index index
	 *  @return true if column mandatory
	 */
	public boolean isColumnMandatory (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return m_columns[index].IsMandatory;
	}   //  isMandatory

	/**
	 *  Is Column Updateable
	 *  @param index index
	 *  @return true if column updateable
	 */
	public boolean isColumnUpdateable (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return m_columns[index].IsUpdateable;
	}   //  isUpdateable

	/**
	 *  Set Column Updateable
	 *  @param index index
	 *  @param updateable column updateable
	 */
	public void setColumnUpdateable (int index, boolean updateable)
	{
		if (index < 0 || index >= m_columns.length)
			return;
		m_columns[index].IsUpdateable = updateable;
	}	//	setColumnUpdateable

	/**
	 * 	Set all columns updateable
	 * 	@param updateable updateable
	 */
	public void setUpdateable (boolean updateable)
	{
		for (int i = 0; i < m_columns.length; i++)
			m_columns[i].IsUpdateable = updateable;
	}	//	setUpdateable

	/**
	 *  Is Lookup Column
	 *  @param index index
	 *  @return true if it is a lookup column
	 */
	public boolean isColumnLookup (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return FieldType.isLookup(m_columns[index].DisplayType);
	}   //  isColumnLookup

	/**
	 *  Get Lookup
	 * @param ctx TODO
	 * @param index index
	 *  @return Lookup
	 */
	public Lookup getColumnLookup (Ctx ctx, int index)
	{
		if (!isColumnLookup(index))
			return null;
		//
		int WindowNo = 0;
		//  List, Table, TableDir
		Lookup lookup = null;
		try
		{
			lookup = MLookupFactory.get (ctx, WindowNo,
				m_columns[index].AD_Column_ID, m_columns[index].DisplayType,
				Env.getLanguage(ctx), m_columns[index].ColumnName,
				m_columns[index].AD_Reference_Value_ID,
				m_columns[index].IsParent, m_columns[index].ValidationCode);
		}
		catch (Exception e)
		{
			lookup = null;          //  cannot create Lookup
		}
		return lookup;
		/** @todo other lookup types */
	}   //  getColumnLookup

	/**
	 *  Is Column Key
	 *  @param index index
	 *  @return true if column is the key
	 */
	public boolean isKey (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return m_columns[index].IsKey;
	}   //  isKey

	/**
	 *  Is Column Parent
	 *  @param index index
	 *  @return true if column is a Parent
	 */
	public boolean isColumnParent (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return m_columns[index].IsParent;
	}   //  isColumnParent

	/**
	 *  Is Column Translated
	 *  @param index index
	 *  @return true if column is translated
	 */
	public boolean isColumnTranslated (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return m_columns[index].IsTranslated;
	}   //  isColumnTranslated

	/**
	 *  Is Table Translated
	 *  @return true if table is translated
	 */
	public boolean isTranslated ()
	{
		for (POInfoColumn element : m_columns) {
			if (element.IsTranslated)
				return true;
		}
		return false;
	}   //  isTranslated

	/**
	 *  Is Column (data) Encrypted
	 *  @param index index
	 *  @return true if column is encrypted
	 */
	public boolean isEncrypted (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return false;
		return m_columns[index].IsEncrypted;
	}   //  isEncrypted

	/**
	 *  Get Column FieldLength
	 *  @param index index
	 *  @return field length or 0
	 */
	public int getFieldLength (int index)
	{
		if (index < 0 || index >= m_columns.length)
			return 0;
		return m_columns[index].FieldLength;
	}   //  getFieldLength

	/**
	 *  Get Column FieldLength
	 *  @param columnName
	 *  @return field length or 0
	 */
	public int getFieldLength (String columnName)
	{
		int index = getColumnIndex(columnName);
		if (index >= 0)
			return getFieldLength(index);
		return 0;
	}   //  getFieldLength

	/**
	 * 	Get Reference Value
	 *	@param index column index
	 *	@return AD_Reference_Value_ID
	 */
	public int getColumnAD_Reference_ID(int index)
	{
		if (index < 0 || index >= m_columns.length)
			return 0;
		return m_columns[index].AD_Reference_Value_ID;
	}	//	getFieldAD_Reference_ID

	/**
	 *  Validate Content
	 *  @param index index
	 * 	@param value new Value
	 *  @return null if all valid otherwise error message
	 */
	public String validate (int index, Object value)
	{
		if (index < 0 || index >= m_columns.length)
			return "RangeError";
		//	Mandatory (i.e. not null
		final POInfoColumn column = m_columns[index];
		if (column.IsMandatory && value == null)
		{
			return "IsMandatory";
		}
		if (value == null)
			return null;

		//	Length ignored here

		//
		if (column.ValueMin != null)
		{
			BigDecimal value_BD = null;
			try
			{
				if (column.ValueMin_BD != null)
					value_BD = new BigDecimal(value.toString());
			}
			catch (Exception ex){}
			//	Both are Numeric
			if (column.ValueMin_BD != null && value_BD != null)
			{	//	error: 1 - 0 => 1  -  OK: 1 - 1 => 0 & 1 - 10 => -1
				int comp = column.ValueMin_BD.compareTo(value_BD);
				if (comp > 0)
					return Msg.getMsg(Env.getCtx(), "ExceedMinValue", new Object[] {value_BD,column.ValueMin_BD});
			}
			else	//	String
			{

				int comp = column.ValueMin.compareTo(value.toString());
				if (comp > 0)
					return Msg.getMsg(Env.getCtx(), "ExceedMinValue", new Object[] {value,column.ValueMin});
			}
		}
		if (column.ValueMax != null)
		{
			BigDecimal value_BD = null;
			try
			{
				if (column.ValueMax_BD != null)
					value_BD = new BigDecimal(value.toString());
			}
			catch (Exception ex){}
			//	Both are Numeric
			if (column.ValueMax_BD != null && value_BD != null)
			{	//	error 12 - 20 => -1  -  OK: 12 - 12 => 0 & 12 - 10 => 1
				int comp = column.ValueMax_BD.compareTo(value_BD);
				if (comp < 0)
					return Msg.getMsg(Env.getCtx(), "ExceedMaxValue", new Object[] {value_BD,column.ValueMax_BD});
			}
			else	//	String
			{
				int comp = column.ValueMax.compareTo(value.toString());
				if (comp < 0)
					return Msg.getMsg(Env.getCtx(), "ExceedMaxValue", new Object[] {value_BD,column.ValueMax_BD});
			}
		}
		return null;
	}   //  validate

	public String getLoadSQL() {
		return m_loadSQL;
	}

	public String getWhereClause() {
		return m_whereClause;
	}

	private void setKeyInfo() {

		m_KeyColumns = null;

		// Search for Primary Key
		for (int i = 0; i < getColumnCount(); i++) {
			if (isKey(i)) {
				m_KeyColumns = new String[] { getColumnName(i) };
				break;
			}
		} // primary key search

		if( m_KeyColumns == null ){
			// Search for Parents
			ArrayList<String> parentColumns = new ArrayList<String>();
			for (int i = 0; i < getColumnCount(); i++) {
				if (isColumnParent(i))
					parentColumns.add(getColumnName(i));
			}
			// Set FKs
			int size = parentColumns.size();
			if (size == 0)
				throw new CompiereStateException("No PK nor FK - " + getTableName());
			m_KeyColumns = new String[size];
			for (int i = 0; i < size; i++) {
				m_KeyColumns[i] = parentColumns.get(i);
			}
		}

		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < m_KeyColumns.length; i++) {
			if (i != 0)
				sb.append("AND ");
			sb.append(m_KeyColumns[i]).append(" = ? ");
		}
		m_whereClause = sb.toString();

		StringBuilder sql = new StringBuilder("SELECT ");
		int size = getColumnCount();
		for (int i = 0; i < size; i++) {
			if (i != 0)
				sql.append(",");
			sql.append(getColumnSQL(i)); // Normal and Virtual Column
		}
		sql.append(" FROM ").append(getTableName()).append(" WHERE ").append(m_whereClause);
		m_loadSQL = sql.toString();
		
	}
	
	public String[] getKeyColumns(){
		return m_KeyColumns;
	}

	public boolean isKeyColumn(String columnName) {
		for (String element : m_KeyColumns) {
			if (element.equals(columnName))
				return true;
		}
		return false;
	}

}   //  POInfo
