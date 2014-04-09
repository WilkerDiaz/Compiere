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

import java.io.StringWriter;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.compiere.*;
import org.compiere.framework.*;
import org.compiere.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *	Persistent Table Model
 *
 *  @author Jorg Janke
 *  @version $Id: MTable.java 8949 2010-06-15 18:43:08Z rthng $
 */
public class MTable extends X_AD_Table
{
    /** Logger for class MTable */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTable.class);
	/**  */
    private static final long serialVersionUID = 7343376567528428910L;

	/**
	 * 	Get all active Tables
	 * 	@param ctx context
	 *	@param entityType optional entity Type ignored
	 *	@return array of tables
	 */
	public static ArrayList<MTable> getTables(Ctx ctx, String entityType)
	{
		return getTables(ctx, null, entityType);
	}	//	getTables

	/**
	 * 	Get all active Tables
	 * 	@param ctx context
	 *  @param where where clause
	 *	@param entityType optional entity type
	 *	@return array of tables
	 */
	public static ArrayList<MTable> getTables (Ctx ctx, String where, String entityType,boolean excludeView)
	{
		String sql = "SELECT * FROM AD_Table WHERE IsActive='Y'";
		
		if (excludeView)
			sql += " AND IsView = 'N'";
		if (!Util.isEmpty(where))
			sql += " AND " + where;
		if (!Util.isEmpty(entityType))
			sql += " AND EntityType IN (" + entityType + ")";
		sql += " ORDER BY LoadSeq";

		return getTablesByQuery(ctx, sql);
	}	//	getTables
	
	/**
	 * 	Get all active Tables and Views
	 * 	@param ctx context
	 *  @param where where clause
	 *	@param entityType optional entity type
	 *	@param excludeView ignore Views
	 *	@return array of tables
	 */
	public static ArrayList<MTable> getTables (Ctx ctx, String where, String entityType){
		return getTables(ctx,where,entityType,true);
	}

	/**
	 * 	Get Sub Tables
	 * 	@param ctx context
	 *  @param Base_Table_ID base table
	 *	@return array of tables
	 */
	public static ArrayList<MTable> getSubTables (Ctx ctx, int Base_Table_ID)
	{
		ArrayList<MTable> retValue = s_subTables.get(ctx, Base_Table_ID);
		if (retValue != null)
			return retValue;

		String sql = "SELECT * FROM AD_Table "
			+ "WHERE IsActive='Y' AND IsView='N'"
			+ " AND SubTableType IS NOT NULL AND Base_Table_ID=?"
			+ " ORDER BY LoadSeq";
		retValue = getTablesByQuery(ctx, sql);
		s_subTables.put(Base_Table_ID, retValue);
		return retValue;
	}	//	getSubTables

	/**
	 * Get all active Tables with input sql
	 * @param ctx user context
	 * @param sql query to get the tables
	 * @return array of tables
	 */
	public static ArrayList<MTable> getTablesByQuery(Ctx ctx, String sql)
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<MTable> list = new ArrayList<MTable>();
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MTable table = new MTable (ctx, rs, null);
				/**
				String s = table.getSQLCreate();
				HashMap hmt = table.get_HashMap();
				MColumn[] columns = table.getColumns(false);
				HashMap hmc = columns[0].get_HashMap();
				 **/
				list.add(table);
			}
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return list;
	}	//	getTables


	/**
	 * 	Get Table from Cache
	 *	@param ctx context
	 *	@param AD_Table_ID id
	 *	@return MTable
	 */
	public static MTable get (Ctx ctx, int AD_Table_ID)
	{
		Integer key = Integer.valueOf(AD_Table_ID);
		MTable retValue = s_cache.get(ctx, key);
		if (retValue == null)
			return new MTable (ctx, AD_Table_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Table from Cache
	 *	@param ctx context
	 *	@param tableName case insensitive table name
	 *	@return Table or null
	 */
	public static MTable get (Ctx ctx, String tableName)
	{
		if (tableName == null)
			return null;
		//	Check cache
		Iterator<MTable> it = s_cache.values().iterator();
		while (it.hasNext())
		{
			MTable retValue = it.next();
			if (tableName.equalsIgnoreCase(retValue.getTableName()))
				return (MTable)PO.copy(ctx, retValue, null);
		}
		//	Get direct
		MTable retValue = null;
		String sql = "SELECT * FROM AD_Table WHERE UPPER(TableName)=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, tableName.toUpperCase());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MTable (ctx, rs, null);
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (retValue != null)
		{
			Integer key = Integer.valueOf (retValue.getAD_Table_ID());
			s_cache.put (key, retValue);
		}
		return retValue;
	}	//	get

	/**
	 * 	Get Table Name
	 *	@param ctx context
	 *	@param AD_Table_ID table
	 *	@return tavle name
	 */
	public static String getTableName (Ctx ctx, int AD_Table_ID)
	{
		return MTable.get(ctx, AD_Table_ID).getTableName();
	}	//	getTableName

	/**	Table Cache						*/
	private static final CCache<Integer,MTable>		s_cache
		= new CCache<Integer,MTable>("AD_Table", 20);
	/**	Class Cache			*/
	private static final CCache<String,Class<?>>	s_class
		= new CCache<String,Class<?>>("AD_Table", 20);
	/**	Sub Table Cache		*/
	private static final CCache<Integer,ArrayList<MTable>>	s_subTables
		= new CCache<Integer,ArrayList<MTable>>("AD_Table", 100);
	/**	Static Logger		*/
	private static final CLogger	s_log	= CLogger.getCLogger (MTable.class);

	/**	Packages for Model Classes			*/
	private static String[]			s_packages = null;
	/**	Default Packages for Model Classes	*/
	private static final String[]	s_packagesDefault = new String[] {
		"compiere.model",			//	Extensions
		"org.compiere.model", "org.compiere.wf",
		"org.compiere.print", "org.compiere.impexp"
	};

	/**	Special Classes				*/
	private static final String[]	s_special = new String[] {
		"AD_Element", "org.compiere.model.M_Element",
		"AD_Registration", "org.compiere.model.M_Registration",
		"AD_Tree", "org.compiere.model.MTree_Base",
		"R_Category", "org.compiere.model.MRequestCategory",
		"GL_Category", "org.compiere.model.MGLCategory",
		"K_Category", "org.compiere.model.MKCategory",
		"C_ValidCombination", "org.compiere.model.MAccount",
		"C_Phase", "org.compiere.model.MProjectTypePhase",
		"C_Task", "org.compiere.model.MProjectTypeTask",
		"K_Source", "org.compiere.model.X_K_Source"
		//	AD_Attribute_Value, AD_TreeNode
	};

	/**
	 * 	Get Model Packages
	 *	@return array of packages
	 */
	static String[] getPackages(Ctx ctx)
	{
		if (s_packages != null)
			return s_packages;

		ArrayList<String> list = new ArrayList<String>();
		String[] packages = MEntityType.getPackages(ctx);	//	clean list
		for (String element : packages)
			list.add(element);

		//	Default Packages
		for (String packageName : s_packagesDefault) {
			if (!list.contains(packageName))
				list.add(packageName);
		}

		s_packages = new String[list.size()];
		s_packages = list.toArray(s_packages);
		s_log.info("#" + s_packages.length);
		return s_packages;
	}	//	getPackages

	/**
	 * 	Get Persistency Class for Table
	 *	@param tableName table name
	 *	@return class or null
	 */
	public static Class<?> getClass (String tableName)
	{
		//	Not supported
		if (tableName == null)
			return null;
		Class<?> poClazz = s_class.get(null, tableName);	//	cached
		if (poClazz != null)
			return poClazz;

		//	Import Tables (Name conflict)
		if (tableName.startsWith("I_"))
		{
			poClazz = getPOclass("org.compiere.model.X_" + tableName);
			if (poClazz != null)
			{
				s_class.put(tableName, poClazz);
				return poClazz;
			}
			s_log.warning("No class for table: " + tableName);
			return null;
		}


		//	Special Naming
		for (int i = 0; i < s_special.length; i++)
		{
			if (s_special[i++].equals(tableName))
			{
				poClazz = getPOclass(s_special[i]);
				if (poClazz != null)
				{
					s_class.put(tableName, poClazz);
					return poClazz;
				}
				break;
			}
		}

		//	Strip table name prefix (e.g. AD_) Customizations are 3/4
		String className = tableName;
		int index = className.indexOf('_');
		if (index > 0)
		{
			if (index < 3)		//	AD_, A_
				className = className.substring(index+1);
		}
		//	Remove underlines
		className = Util.replace(className, "_", "");

		//	Search packages for M classes
		String[] packages = getPackages(Env.getCtx());
		for (String element : packages)
		{
			StringBuffer name = new StringBuffer(element).append(".M").append(className);
			poClazz = getPOclass(name.toString());
			if (poClazz != null)
			{
				s_class.put(tableName, poClazz);
				return poClazz;
			}
		}

		//	Search packages for X classes
		for (String element : packages)
		{
			StringBuffer name = new StringBuffer(element).append(".X_").append(tableName);
			poClazz = getPOclass(name.toString());
			if (poClazz != null)
			{
				s_class.put(tableName, poClazz);
				return poClazz;
			}
		}

		//	Default Extension
		if (poClazz == null)
			poClazz = getPOclass("compiere.model.X_" + tableName);

		//	Default
		if (poClazz == null)
			poClazz = getPOclass("org.compiere.model.X_" + tableName);
		if (poClazz == null)
			return null;
		//
		s_class.put(tableName, poClazz);
		return poClazz;
	}	//	getClass

	/**
	 * 	Get PO class
	 *	@param className fully qualified class name
	 *	@return class or null
	 */
	private static Class<?> getPOclass (String className)
	{
		try
		{
			Class<?> clazz = Class.forName(className);
			//	Make sure that it is a PO class
			Class<?> superClazz = clazz.getSuperclass();
			while (superClazz != null)
			{
				if (superClazz == PO.class)
				{
					s_log.fine("Use: " + className);
					return clazz;
				}
				superClazz = superClazz.getSuperclass();
			}
		}
		catch (Exception e)
		{
		}
		s_log.finest("Not found: " + className);
		return null;
	}	//	getPOclass


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Table_ID id
	 *	@param trx transaction
	 */
	public MTable (Ctx ctx, int AD_Table_ID, Trx trx)
	{
		super (ctx, AD_Table_ID, trx);
		if (AD_Table_ID == 0)
		{
			setAccessLevel (ACCESSLEVEL_SystemOnly);	// 4
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setChangeLogLevel(X_AD_Table.CHANGELOGLEVEL_None);
			setIsDeleteable (false);
			setIsHighVolume (false);
			setIsSecurityEnabled (false);
			setIsView (false);	// N
			setReplicationType (REPLICATIONTYPE_Local);
			setTableTrxType(TABLETRXTYPE_OptionalOrganization);
		}
	}	//	MTable

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MTable (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MTable

	/**	Columns					*/
	private MColumn[]			m_columns = null;
	/** View Components			*/
	private MViewComponent[]	m_vcs = null;
	/** Dependents of Table		*/
	private ArrayList<MTable> 	m_dependentsDirect = null;

	/** User Identifier			*/
	private String[]			m_identifierColumns = null;
	/** Unique ID				*/
	private String[]			m_uniqueIDColumns = null;

	/**
	 * 	Get Columns
	 *	@param requery requery
	 *	@return array of columns
	 */
	public MColumn[] getColumns (boolean requery)
	{
		if (m_columns != null && !requery)
			return m_columns;
		String sql = "SELECT * FROM AD_Column WHERE AD_Table_ID=? ORDER BY ColumnName";

		List<MColumn> list = getCols(sql);
		//
		m_columns = new MColumn[list.size ()];
		list.toArray (m_columns);
		return m_columns;
	}	//	getColumns

	/**
	 * 	Get Columns
	 *	@param entityType
	 *	@return array of columns
	 */
	public MColumn[] getColumns(String entityType)
	{
		String sql = "SELECT * FROM AD_Column WHERE AD_Table_ID=? "
			+ "AND EntityType IN ("
			+ entityType + ") ORDER BY ColumnName";

		List<MColumn> list = getCols(sql);
		//
		MColumn[] cols = new MColumn[list.size ()];
		list.toArray (cols);
		return cols;
	}	//	getColumns

	/**
	 * Get the MColumn
	 * @param sql
	 * @return list of columns
	 */
	private List<MColumn> getCols(String sql)
	{
		ArrayList<MColumn> list = new ArrayList<MColumn>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getAD_Table_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MColumn (getCtx(), rs, get_Trx()));
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return list;
	}  // getColumns()

	/**
	 * 	Get Column
	 *	@param columnName (case insensitive)
	 *	@return column if found
	 */
	public MColumn getColumn (String columnName)
	{
		if (Util.isEmpty(columnName))
			return null;
		getColumns(false);
		//
		for (MColumn element : m_columns)
		{
			if (columnName.equalsIgnoreCase(element.getColumnName()))
				return element;
		}
		return null;
	}	//	getColumn

	/**
	 * 	Table has a single Key
	 *	@return true if table has single key column
	 */
	public boolean isSingleKey()
	{
		String[] keys = getKeyColumns(false);
		return keys.length == 1;
	}	//	isSingleKey

	/**
	 * 	Get Key Columns of Table (might be parent)
	 *	@return key columns
	 */
	public String[] getKeyColumns()
	{
		return getKeyColumns(true);
	}	//	getKeyColumns

	/**
	 * 	Get Key Columns of Table
	 * 	@param withParents with parents
	 *	@return key columns
	 */
	public String[] getKeyColumns(boolean withParents)
	{
		getColumns(false);
		ArrayList<String> list = new ArrayList<String>();
		//
		for (MColumn column : m_columns)
		{
			if (column.isKey())
				return new String[]{column.getColumnName()};
			if (column.isParent() && withParents)
				list.add(column.getColumnName());
		}
		String[] retValue = new String[list.size()];
		retValue = list.toArray(retValue);
		return retValue;
	}	//	getKeyColumns

	/**
	 * 	Get User Identifier Columns of Table
	 *	@return key columns
	 */
	public String[] getIdentifierColumns()
	{
		if (m_identifierColumns != null)
			return m_identifierColumns;
		//
		getColumns(false);
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		//
		for (MColumn column : m_columns)
		{
			if (column.isIdentifier())
			{
				int seq = column.getSeqNo();
				String columnName = column.getColumnName();
				KeyNamePair pp = new KeyNamePair(seq, columnName);
				pp.setSortByName(false);
				list.add (pp);
			}
		}

		Collections.sort(list);
		m_identifierColumns = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
			m_identifierColumns[i] = list.get(i).getName();
		return m_identifierColumns;
	}	//	getIdentifierColumns

	/**
	 * 	Get Unique ID Columns of Table
	 *	@return key columns
	 */
	public String[] getUniqueIDColumns()
	{
		if (m_uniqueIDColumns != null)
			return m_uniqueIDColumns;
		//
		getColumns(false);
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		//
		for (MColumn column : m_columns)
		{
			int tableID = column.getTableUID();
			if (tableID > 0)
			{
				String columnName = column.getColumnName();
				KeyNamePair pp = new KeyNamePair(tableID, columnName);
				pp.setSortByName(false);
				list.add (pp);
			}
		}
		//	If none: Identifier
		if (list.size() == 0)
		{
			m_uniqueIDColumns = getIdentifierColumns();
			return m_uniqueIDColumns;
		}

		Collections.sort(list);
		m_uniqueIDColumns = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
			m_uniqueIDColumns[i] = list.get(i).getName();
		return m_uniqueIDColumns;
	}	//	getUniqeIDColumns


	/**
	 * 	Get list of columns for SELECT statement.
	 * 	Handles virtual columns
	 *	@return select columns
	 */
	public String getSelectColumns()
	{
		getColumns(false);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < m_columns.length; i++)
		{
			MColumn col = m_columns[i];
			if (i > 0)
				sb.append(",");
			if (col.isVirtualColumn())
				sb.append(col.getColumnSQL()).append(" AS ");
			sb.append(col.getColumnName());
		}
		return sb.toString();
	}	//	getSelectColumns


	/**
	 * 	Get Columns which are Foreign Keys
	 * 	@param includeList include list entities
	 *	@return array of FKs
	 */
	public ArrayList<MColumn> getFKs(boolean includeList)
	{
		getColumns(false);
		ArrayList<MColumn> retValue = new ArrayList<MColumn>();
		for (MColumn col : m_columns) {
			if (col.isFK())
				retValue.add(col);
			else if (includeList && col.isList())
				retValue.add(col);
		}
		return retValue;
	}	//	getFKs

	/**
	 * 	Get Direct Dependent Tables
	 *	@return list of dependent tables
	 */
	public ArrayList<MTable> getDependentsDirect(Trx trx)
	{
		if (m_dependentsDirect != null)
			return m_dependentsDirect;
		//	Get Key Column
		String[] keyColumns = getKeyColumns();
		if (keyColumns.length == 0)
		{
			log.config("No Key Columns for " + toString());
			m_dependentsDirect = new ArrayList<MTable>();
			return m_dependentsDirect;
		}
		else if (keyColumns.length > 1)
		{
			log.config("Multiple Key Columns for " + toString());
			m_dependentsDirect = new ArrayList<MTable>();
			return m_dependentsDirect;
		}
		String keyColumn = keyColumns[0];
		//	Get Direct References
		String sql = "SELECT * FROM AD_Table t "
			+ "WHERE IsView='N'"
			+ " AND EXISTS (SELECT * FROM AD_Column c "
			+ "WHERE t.AD_Table_ID=c.AD_Table_ID"
			+ " AND c.ColumnName=?) "
			+ "ORDER BY CASE WHEN LoadSeq=0 THEN 999 ELSE LoadSeq END";
		m_dependentsDirect = new ArrayList<MTable>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setString(1, keyColumn);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MTable table = new MTable(getCtx(), rs, trx);
				if (!table.getTableName().equals(getTableName()))
					m_dependentsDirect.add(table);
			}
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return m_dependentsDirect;
	}	//	getDependentsDirect

	/**
	 * 	Get Table Reference Dependent Tables
	 * 	@param entityType optional entity type
	 * 	@param refTables a list of tables that refers to this table indirectly (via a table reference)
	 * 	@param refColumns a list of columns that refers to this table indirectly
	 * 	@param srcColumns a list of columns in this table that are being referred to indirectly
	 */ 
	public void getDependentsTable(String entityType, ArrayList<MTable> refTables, ArrayList<String> refColumns, ArrayList<String> srcColumns)
	{
		//	Get Table References
		String sql = "SELECT refC.ColumnName, refC.AD_Table_ID, srcC.ColumnName "
			+ "FROM AD_Column refC"
			+ " INNER JOIN AD_Ref_Table rt ON (refC.AD_Reference_Value_ID=rt.AD_Reference_ID)"
			+ " INNER JOIN AD_Table refT ON (refC.AD_Table_ID=refT.AD_Table_ID) "
			+ " INNER JOIN AD_Column srcC ON (srcC.AD_Column_ID=rt.Column_Key_ID) "
			+ "WHERE rt.AD_Table_ID=?"
			+ " AND refC.ColumnSql IS NULL"
			+ " AND refT.IsView='N'";
		if (!Util.isEmpty(entityType))
			sql += " AND refCol.EntityType=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, getAD_Table_ID());
			if (!Util.isEmpty(entityType))
				pstmt.setString(2, entityType);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String columnName = rs.getString(1);
				int AD_Table_ID = rs.getInt(2);
				String srcColumnName = rs.getString(3);
				if (AD_Table_ID != getAD_Table_ID()){
					refTables.add(get(getCtx(), AD_Table_ID));
					refColumns.add(columnName);
					srcColumns.add(srcColumnName);
				}
			}
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

	}	//	getDependentsTable


	/**************************************************************************
	 * 	Get PO Class Instance
	 * 	@param ctx context for PO
	 *	@param Record_ID record - 0 = new
	 *	@param trx transaction
	 *	@return PO for Record or null
	 */
	public PO getPO (Ctx ctx, int Record_ID, Trx trx)
	{
		return getPO (ctx, Record_ID, trx, true);
	}	//	getPO

	/**************************************************************************
	 * 	Get PO Class Instance
	 * 	@param ctx context for PO
	 *	@param Record_ID record - loads valid 0 records if newRecord is false
	 *	@param trx transaction
	 *	@param newRecord new record
	 *	@return PO for Record or null
	 */
	public PO getPO (Ctx ctx, int Record_ID, Trx trx, boolean newRecord)
	{
		String tableName = getTableName();
		if (Record_ID != 0 && !isSingleKey())
		{
			log.log(Level.WARNING, "(id) - Multi-Key " + tableName);
			return null;
		}
		Class<?> clazz = getClass(tableName);
		if (clazz == null)
		{
			log.log(Level.WARNING, "(id) - Class not found for " + tableName);
			return new X(ctx, this, Record_ID, trx);
//			TODO check return null;
		}
		boolean errorLogged = false;
		try
		{
			Constructor<?> constructor = null;
			try
			{
				constructor = clazz.getDeclaredConstructor(new Class[]{Ctx.class, int.class, Trx.class});
			}
			catch (Exception e)
			{
				String msg = e.getMessage();
				if (msg == null)
					msg = e.toString();
				log.warning("No transaction Constructor for " + clazz + " (" + msg + ")");
			}

			if (constructor != null)
			{
				PO po = (PO)constructor.newInstance(new Object[] {ctx, Integer.valueOf(Record_ID), trx});
				//	Load record 0 - valid for System/Client/Org/Role/User
				if (!newRecord && Record_ID == 0)
					po.load(trx);
				//	Check if loaded correctly
				if (po != null && po.get_ID() != Record_ID && isSingleKey())
				{
					log.warning(po.get_TableName() + "_ID=" + po.get_ID() + " <> requested=" + Record_ID);
					return null;
				}
				return po;
			}
			else
				throw new Exception("No Std Constructor");
		}
		catch (Exception e)
		{
			if (e.getCause() != null)
			{
				Throwable t = e.getCause();
				log.log(Level.SEVERE, "(id) - Table=" + tableName + ",Class=" + clazz, t);
				errorLogged = true;
				if (t instanceof Exception)
					log.saveError("Error", (Exception)e.getCause());
				else
					log.saveError("Error", "Table=" + tableName + ",Class=" + clazz);
			}
			else
			{
				log.log(Level.SEVERE, "(id) - Table=" + tableName + ",Class=" + clazz, e);
				errorLogged = true;
				log.saveError("Error", "Table=" + tableName + ",Class=" + clazz);
			}
		}
		if (!errorLogged)
			log.log(Level.SEVERE, "(id) - Not found - Table=" + tableName
					+ ", Record_ID=" + Record_ID);
		return null;
	}	//	getPO

	/**
	 * 	Get PO Class Instance
	 * 	@param ctx context for PO
	 *	@param rs result set
	 *	@param trx transaction
	 *	@return PO for Record or null
	 */
	public PO getPO (Ctx ctx, ResultSet rs, Trx trx)
	{
		String tableName = getTableName();
		Class<?> clazz = getClass(tableName);
		if (clazz == null)
		{
			log.log(Level.WARNING, "(rs) - Class not found for " + tableName);
			return new X(ctx, this, rs, trx);
//			TODO check return null;
		}
		boolean errorLogged = false;
		try
		{
			Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[]{Ctx.class, ResultSet.class, Trx.class});
			PO po = (PO)constructor.newInstance(new Object[] {ctx, rs, trx});
			return po;
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "(rs) - Table=" + tableName + ",Class=" + clazz, e);
			errorLogged = true;
			log.saveError("Error", "Table=" + tableName + ",Class=" + clazz);
		}
		if (!errorLogged)
			log.log(Level.WARNING, "(rs) - Not found - Table=" + tableName);
		return null;
	}	//	getPO

	/**
	 * 	Get PO
	 * 	@param ctx general context for PO
	 *	@param context record context
	 *	@return PO
	 */
	public PO getPO (Ctx ctx, Map<String, String> context)
	{
		String tableName = getTableName();
		Class<?> clazz = getClass(tableName);
		if (clazz == null)
		{
			log.log(Level.WARNING, "(id) - Class not found for " + tableName);
			return null;
		}
		boolean errorLogged = false;
		try
		{
			Constructor<?> constructor = null;
			try
			{
				constructor = clazz.getDeclaredConstructor(new Class[]{Ctx.class, int.class, Trx.class});
			}
			catch (Exception e)
			{
				String msg = e.getMessage();
				if (msg == null)
					msg = e.toString();
				log.warning("No transaction Constructor for " + clazz + " (" + msg + ")");
			}
			if (constructor != null)
			{
				PO po = (PO)constructor.newInstance(new Object[] {ctx, Integer.valueOf(0), null});
				if (!po.load(context))
					throw new Exception("Could not load PO");
				return po;
			}
			else
				throw new Exception("No Std Constructor");
		}
		catch (Exception e)
		{
			if (e.getCause() != null)
			{
				Throwable t = e.getCause();
				log.log(Level.SEVERE, "(id) - Table=" + tableName + ",Class=" + clazz, t);
				errorLogged = true;
				if (t instanceof Exception)
					log.saveError("Error", (Exception)e.getCause());
				else
					log.saveError("Error", "Table=" + tableName + ",Class=" + clazz);
			}
			else
			{
				log.log(Level.SEVERE, "(id) - Table=" + tableName + ",Class=" + clazz, e);
				errorLogged = true;
				log.saveError("Error", "Table=" + tableName + ",Class=" + clazz);
			}
		}
		if (!errorLogged)
			log.log(Level.SEVERE, "(id) - Not found - Table=" + tableName);
		return null;
	}	//	getPO


	/**
	 * 	Get PO Class Instance
	 * 	@param ctx context for PO
	 *	@param whereClause where clause resulting in single record
	 *	@param trx transaction
	 *	@return PO for Record or null
	 */
	public PO getPO (Ctx ctx, String whereClause, Trx trx)
	{
		if (whereClause == null || whereClause.length() == 0)
			return null;
		//
		PO po = null;
		StringBuffer sql = new StringBuffer("SELECT ")
		.append(getSelectColumns())
		.append(" FROM ").append(getTableName())
		.append(" WHERE ").append(whereClause);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), trx);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				po = getPO(ctx, rs, trx);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql.toString(), e);
			log.saveError("Error", e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (po == null)
			return getPO(ctx, 0, trx);
		return po;
	}	//	getPO

	/**
	 * 	Get POs Class Instance
	 * 	@param ctx context for PO
	 * 	@param whereClause optional where clause
	 *	@param orderClause optional order by
	 *	@param trx transaction
	 *	@return PO for Record or null
	 */
	public PO[] getPOs (Ctx ctx, String whereClause, String orderClause, Trx trx)
	{
		ArrayList<PO> list = new ArrayList<PO>();
		String sql = "SELECT * FROM " + getTableName();
		if (whereClause != null && whereClause.length() > 0)
			sql += " WHERE " + whereClause;
		if (orderClause != null && orderClause.length() > 0)
			sql += " ORDER BY " + orderClause;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				PO po = getPO(ctx, rs, trx);
				list.add (po);
			}
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		PO[] retValue = new PO[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getPO

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (isView() && isDeleteable())
			setIsDeleteable(false);

		/**	Set Trx Type
		UPDATE AD_Table SET TableTrxType='N'
		WHERE AccessLevel='4'   -- System
		  OR AccessLevel='2'    -- Tenant
		  OR AccessLevel='6';   -- System+Tenant
		/
		UPDATE AD_Table SET TableTrxType='O'
		WHERE TableTrxType IS NULL
		  OR TableTrxType NOT IN ('M','N')
		  OR TableName LIKE '%Log'
		  OR TableName LIKE '%Audit%'
		  OR (TableName LIKE 'I%' AND TableName NOT LIKE 'IP%')
		/
		UPDATE AD_Table SET TableTrxType='M'
		WHERE AccessLevel='1'
		 OR TableName IN ('I_Invoice', 'I_Order', 'I_BankStatement',
		  'I_GLJournal', 'I_Payment', 'I_Inventory')
		/
		**/
	//	String p_trx = getTableTrxType();
		String access = getAccessLevel();
		if (ACCESSLEVEL_SystemOnly.equals(access))
			setTableTrxType(TABLETRXTYPE_NoOrganization);
		else if (ACCESSLEVEL_Organization.equals(access))
			setTableTrxType(TABLETRXTYPE_MandatoryOrganization);

		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Sync Table ID
		if (newRecord)
			MSequence.createTableSequence(getCtx(), getTableName(), get_Trx());
		else
		{
			MSequence seq = MSequence.get(getCtx(), getTableName(), get_Trx());
			if (seq == null || seq.get_ID() == 0)
				MSequence.createTableSequence(getCtx(), getTableName(), get_Trx());
			else if (!seq.getName().equals(getTableName()))
			{
				seq.setName(getTableName());
				seq.save();
			}
		}
		return success;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success success
	 *	@return true if success
	 */
	@Override
	protected boolean afterDelete(boolean success)
	{
		if (success)
			MSequence.deleteTableSequence(getCtx(), getTableName(), get_Trx());
		return success;
	}	//	afterDelete

	/**
	 * 	Get SQL Create statement
	 *	@return sql statement
	 */
	public String getSQLCreate()
	{
		return getSQLCreate(true);
	}	// getSQLCrete

	/**
	 * 	Get SQL Create
	 * 	@param requery refresh columns
	 *	@return create table DDL
	 */
	public String getSQLCreate(boolean requery)
	{
		StringBuffer sb = new StringBuffer("CREATE TABLE ")
		.append(getTableName()).append(" (");
		//
		boolean hasPK = false;
		boolean hasParents = false;
		boolean firstColumn = true;
		StringBuffer constraints = new StringBuffer();
		getColumns(requery);
		for (MColumn column : m_columns) {
			if (column.isVirtualColumn())
				continue;
			if (firstColumn)
				firstColumn=false;
			else
				sb.append(", ");
			sb.append(column.getSQLDDL());
			//
			if (column.isKey())
			{
				constraints.append (", CONSTRAINT PK").append (getAD_Table_ID())
				.append (" PRIMARY KEY (").append (column.getColumnName()).append (")");
				hasPK = true;
			}
			if (column.isParent())
				hasParents = true;
		}
		//	Multi Column PK
		if (!hasPK && hasParents)
		{
			// the order of index columns is determined by tableUID and column name.
			class indexCol implements Comparable<indexCol>{
				public String columnName;
				public int tableUID;
				public indexCol(String theColumnName, int theTableUID){
					super();
					columnName = theColumnName;
					tableUID = theTableUID;
				}
				@Override
				public int compareTo(indexCol o) {
					if (tableUID == o.tableUID)
						return (columnName.compareTo(o.columnName));
					if (tableUID > 0 && o.tableUID > 0)
						return (tableUID < o.tableUID)?-1:1;
					// columns with positive tableUIDs have priority over those with NULL/zero tableUIDs
					return (tableUID > 0)?-1:1;
				}
			}
			ArrayList<indexCol> indexCols = new ArrayList<indexCol>();
			for (MColumn column : m_columns) {
				if (!column.isParent())
					continue;
				indexCols.add(new indexCol(column.getColumnName(),column.getTableUID()));
			}			
			indexCol[] sortedIndexCols = new indexCol[indexCols.size()];
			indexCols.toArray(sortedIndexCols);
			Arrays.sort(sortedIndexCols);
			StringBuffer cols = new StringBuffer();
			for (indexCol sortedIndexCol : sortedIndexCols){
				if (cols.length() > 0) cols.append(", ");
				cols.append(sortedIndexCol.columnName);
			}			
			sb.append(", CONSTRAINT PK").append (getAD_Table_ID())
			.append(" PRIMARY KEY (").append(cols).append(")");
		}

		sb.append(constraints)
		.append(")");
		return sb.toString();
	}	//	getSQLCreate


	/**
	 * 	Get SQL Create View
	 * 	@param requery refresh columns
	 *	@return create create view DDL
	 */
	public String getViewDrop()
	{
		if (!isView() || !isActive())
			return null;

		return new String("DROP VIEW " + getTableName());
	}

	/**
	 * 	Get SQL Create View
	 * 	@param requery refresh columns
	 *	@return create create view DDL
	 */
	public String getViewCreate(boolean requery)
	{
		if (!isView() || !isActive())
			return null;

		StringBuffer sb = new StringBuffer("CREATE OR REPLACE VIEW ")
		.append(getTableName());
		//
		this.getViewComponent(requery);
		if (m_vcs == null || m_vcs.length == 0)
			return null;

		MViewColumn[] vCols = null;
		for (int i = 0; i < m_vcs.length; i++)
		{
			MViewComponent vc = m_vcs[i];
			if (i>0)
				sb.append(" UNION ");
			else
			{
				vCols = vc.getColumns(requery);
				if (vCols==null || vCols.length==0)
					return null;
				boolean right = false;
				for (int j=0; j<vCols.length; j++)
				{
					if (vCols[j].getColumnName().equals("*"))
						break;
					if (j==0)
					{
						sb.append("(");
						right = true;
					}
					else
						sb.append(", ");
					sb.append(vCols[j].getColumnName());
				}

				if (right)
					sb.append(")");
				sb.append(" AS ");
			}

			sb.append(vc.getSelect(false, vCols));
		}
		return sb.toString();
	}	//	getViewCreate


	/**
	 * 	Get MViewComponent Class Instances
	 *  @param reload boolean if it need to reload
	 *	@return Array of MViewComponent or null
	 */
	public MViewComponent[] getViewComponent (boolean reload)
	{
		if (!isView() || !isActive())
			return null;

		if (m_vcs != null && !reload)
			return m_vcs;
		//
		ArrayList<MViewComponent> list = new ArrayList<MViewComponent>();
		String sql = "SELECT * FROM AD_ViewComponent WHERE AD_Table_ID = " + this.getAD_Table_ID() + " AND IsActive = 'Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx ());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MViewComponent (getCtx(), rs, get_Trx ()));
		}
		catch (Exception e) {
			log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_vcs = new MViewComponent[list.size()];
		list.toArray (m_vcs);
		return m_vcs;
	}	//	getViewComponent

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MTable[");
		sb.append (get_ID()).append ("-").append (getTableName()).append ("]");
		return sb.toString ();
	}	//	toString

	private static String TAG_COLUMNS = "AD_Columns";

	public StringBuffer get_xmlComplete (StringBuffer xml, boolean dataOnly)
	{
		if (xml == null)
			xml = new StringBuffer();
		else
			xml.append(Env.NL);
		//
		try
		{
			Document doc = get_xmlDocument(xml.length()!=0, dataOnly);
			NodeList nl = doc.getElementsByTagName("AD_Table");
			if (nl.getLength() < 1)
				return null;
			
			Element winEl = (Element) nl.item(0);
			Element columnsEl = doc.createElement(TAG_COLUMNS);
			winEl.appendChild(columnsEl);
			
			MColumn[] columns = getColumns(true);
			for (MColumn column : columns) {
				if (!column.isActive())
					continue;
				Document columnDoc = column.get_xmlDocument(true, dataOnly);
				Element columnEl = columnDoc.getDocumentElement();
				doc.adoptNode(columnEl);
				columnsEl.appendChild(columnEl);
			}
						
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			DOMSource source = new DOMSource(doc);
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.transform (source, result);
			StringBuffer newXML = writer.getBuffer();
			//
			if (xml.length() != 0)
			{	//	//	<?xml version="1.0" encoding="UTF-8"?>
				int tagIndex = newXML.indexOf("?>");
				if (tagIndex != -1)
					xml.append(newXML.substring(tagIndex+2));
				else
					xml.append(newXML);
			}
			else
				xml.append(newXML);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		return xml;
	}	//	get_xmlComplete


	/**************************************************************************
	 * 	Test
	 *	@param args
	 */
	public static void main(String[] args)
	{
		Compiere.startup (true);
		//	getTables(Env.getCtx(), null);

		CLogMgt.setLevel(Level.FINE);
		Ctx ctx = Env.getCtx();
		String tableName = "M_Product";
		//	Object cc = getClass(tableName);
		MTable table = MTable.get(Env.getCtx(), tableName);
		table.getPO(ctx, 122, null);


		//
		String sql = "SELECT * FROM " + tableName;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Object po = table.getPO(ctx, rs, null);
				System.out.println(po.toString());
			}
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	main
}	//	MTable
