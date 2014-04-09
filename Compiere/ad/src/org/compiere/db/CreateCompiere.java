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
package org.compiere.db;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Class to Create a new Compiere Database from a reference DB.
 *  <pre>
 *  - Create User
 *  - Create DDL (table, procedures, functions, etc.)
 *  </pre>
 *
 *  @author     Jorg Janke
 *  @version    $Id: CreateCompiere.java 8751 2010-05-12 16:49:31Z nnayak $
 */
public class CreateCompiere
{
	/**
	 * 	Constructor
	 *	@param databaseType CompiereDatabase.TYPE_
	 *	@param databaseHost database host
	 *	@param databasePort database port 0 for default
	 *	@param systemPassword system password
	 */
	public CreateCompiere(String databaseType, String databaseHost, int databasePort,
		String systemPassword)
	{
		initDatabase(databaseType);
		m_databaseHost = databaseHost;
		if (databasePort == 0)
			m_databasePort = m_dbTarget.getStandardPort();
		else
			m_databasePort = databasePort;
		m_systemPassword = systemPassword;
		log.info(m_dbTarget.getName() + " on " + databaseHost);
	}   //  create

	/** Compiere Target Database */
	private CompiereDatabase 	m_dbTarget = null;
	/** Compiere Source Database */
	private CompiereDatabase 	m_dbSource = null;
	//
	private String				m_databaseHost = null;
	private int					m_databasePort = 0;
	private String 				m_systemPassword = null;
	private String 				m_compiereUser = null;
	private String 				m_compierePassword = null;
	private String 				m_databaseName = null;
	private String 				m_databaseDevice = null;
	//
	private Ctx					m_ctx = new Ctx();
	/** Cached connection		*/
	private Connection			m_conn = null;
	
	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (CreateCompiere.class);
	
	/**
	 * 	Create Compiere Database
	 *	@param databaseType Database.DB_
	 */
	private void initDatabase(String databaseType)
	{
		try
		{
			for (int i = 0; i < Database.DB_NAMES.length; i++)
			{
				if (Database.DB_NAMES[i].equals (databaseType))
				{
					m_dbTarget = (CompiereDatabase)Database.DB_CLASSES[i].
						   newInstance ();
					break;
				}
			}
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
			e.printStackTrace();
		}
		if (m_dbTarget == null)
			throw new CompiereStateException("No database: " + databaseType);
		
		//	Source Database
		m_dbSource = DB.getDatabase();
	}	//	createDatabase
	
	
	
	/**
	 *  Set Compiere User
	 *  @param compiereUser compiere id
	 *  @param compierePassword compiere password
	 */
	public void setCompiereUser (String compiereUser, String compierePassword)
	{
		m_compiereUser = compiereUser;
		m_compierePassword = compierePassword;
	}   //  setCompiereUser

	/**
	 *  Set Database Name
	 *  @param databaseName db name
	 *  @param databaseDevice device or table space
	 */
	public void setDatabaseName (String databaseName, String databaseDevice)
	{
		m_databaseName = databaseName;
		m_databaseDevice = databaseDevice;
	}   //  createDatabase

	
	/**
	 * 	Test Connection
	 *	@return connection
	 */
	public boolean testConnection()
	{
		String dbUrl = m_dbTarget.getConnectionURL (m_databaseHost, m_databasePort, 
			m_databaseName, m_dbTarget.getSystemUser());	//	compiere may not be defined yet
		log.info(dbUrl + " - " + m_dbTarget.getSystemUser() + "/" + m_systemPassword);
		try
		{
			Connection conn = m_dbTarget.getDriverConnection(dbUrl, m_dbTarget.getSystemUser(), m_systemPassword);
			//
			JDBCInfo info = new JDBCInfo(conn);
			if (CLogMgt.isLevelFinest())
			{
				info.listCatalogs();
				info.listSchemas();
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "test", e);
			return false;
		}

		return true;
	}	//	testConnection
	
	
	
	
	/**
	 * 	Create Tables and copy data
	 * 	@param whereClause optional where clause
	 * 	@param dropFirst drop first
	 *	@return true if executed
	 */
	public boolean copy (String whereClause, boolean dropFirst)
	{
		log.info(whereClause);
		if (getConnection(false, true) == null)
			return false;
		//
		boolean success = true;
		int count = 0;
		ArrayList<String> list = new ArrayList<String>();
		String sql = "SELECT * FROM AD_Table";
		if (whereClause != null && whereClause.length() > 0)
			sql += " WHERE " + whereClause;
		sql += " ORDER BY TableName";
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			//jz: pstmt.getConnection() could be null
			Connection conn = pstmt.getConnection();
			DatabaseMetaData md = null;
			if (conn != null)
				md = conn.getMetaData();
			else
			{
				//jz: globalization issue??
				throw new DBException("No Connection");
			}
			
			rs = pstmt.executeQuery ();
			while (rs.next() && success)
			{
				MTable table = new MTable (m_ctx, rs, null);
				if (table.isView())
					continue;
				if (dropFirst)
				{
					executeCommands(new String[]
					    {"DROP TABLE " + table.getTableName()}, 
						m_conn, false, false);
				}
				//
				if (createTable (table, md))
				{
					list.add(table.getTableName());
					count++;
				}
				else
					success = false;
			}
		}
		catch (Exception e) {
			log.log (Level.SEVERE, sql, e);
			success = false;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (!success)
			return false;
		
		/**	Enable Contraints */
		enableConstraints(list);

		databaseBuild();
		
		log.info("#" + count);
		
		try
		{
			if (m_conn != null)
				m_conn.close();
		}
		catch (SQLException e2)
		{
			log.log(Level.SEVERE, "close connection", e2);
		}
		m_conn = null;
		return success;
	}	//	copy

	/**
	 * 	Execute Script
	 * 	@param script file with script
	 *	@return true if executed
	 */
	public boolean execute (File script)
	{
		return false;
	}	//	createTables
	

	
	/**
	 * 	Create Table
	 *	@param mTable table model
	 *	@param md meta data
	 *	@return true if created
	 */
	private boolean createTable (MTable mTable, DatabaseMetaData md)
	{
		String tableName = mTable.getTableName();
		log.info(tableName);
		String catalog = m_dbSource.getCatalog();
		String schema = m_dbSource.getSchema();
		String table = tableName.toUpperCase();
		//	
		MColumn[] columns = mTable.getColumns(false);
		
		StringBuffer sb = new StringBuffer("CREATE TABLE ");
		sb.append(tableName).append(" (");
		try
		{
			//	Columns
			boolean first = true;
			ResultSet sourceColumns = md.getColumns(catalog, schema, table, null);
			while (sourceColumns.next())
			{
				sb.append(first ? "" : ", ");
				first = false;
				//	Case sensitive Column Name
				MColumn column = null;
				String columnName = sourceColumns.getString("COLUMN_NAME");
				for (MColumn element : columns) {
					String cn = element.getColumnName();
					if (cn.equalsIgnoreCase(columnName))
					{
						columnName = cn;
						column = element;
						break;
					}
				}
				sb.append(columnName).append(" ");
				String typeName = sourceColumns.getString ("TYPE_NAME");	//	DB Dependent
				int size = sourceColumns.getInt ("COLUMN_SIZE");
				if (typeName.equals("NUMBER"))
				{
					/** Oracle Style	*
					if (decDigits == -1)
						sb.append(typeName);
					else
						sb.append(typeName).append("(")
							.append(size).append(",").append(decDigits).append(")");
					/** Other DBs		*/
					int dt = column.getAD_Reference_ID();
					if (FieldType.isID(dt))
						sb.append("INTEGER");
					else 
					{
						int scale = FieldType.getDefaultPrecision(dt);
						sb.append("DECIMAL(")
							.append(18+scale).append(",").append(scale).append(")");
					}
				}					
				else if (typeName.equals("DATE") || typeName.equals("BLOB") || typeName.equals("CLOB"))
					sb.append(typeName);
				else if (typeName.equals("CHAR") || typeName.startsWith("VARCHAR"))
					sb.append(typeName).append("(").append(size).append(")");
				else if (typeName.startsWith("NCHAR") || typeName.startsWith("NVAR"))
					sb.append(typeName).append("(").append(size/2).append(")");
				else if (typeName.startsWith("TIMESTAMP"))
					sb.append("DATE");
				else 
					log.severe("Do not support data type " + typeName);
				//	Default
				String def = sourceColumns.getString("COLUMN_DEF");
				if (def != null)
				{
					//jz: replace '' to \', otherwise exception
					def.replaceAll("''", "\\'");
					sb.append(" DEFAULT ").append(def);
				}
				//	Null
				if (sourceColumns.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls)
					sb.append(" NOT NULL");
				else
					sb.append(" NULL");
				
				//	Check Contraints


			}	//	for all columns
			sourceColumns.close();

			//	Primary Key
			ResultSet sourcePK = md.getPrimaryKeys(catalog, schema, table);
			//	TABLE_CAT=null, TABLE_SCHEM=REFERENCE, TABLE_NAME=A_ASSET, COLUMN_NAME=A_ASSET_ID, KEY_SEQ=1, PK_NAME=A_ASSET_KEY
			first = true;
			boolean hasPK = false;
			while (sourcePK.next())
			{
				hasPK = true;
				if (first)
					sb.append(", CONSTRAINT ").append(sourcePK.getString("PK_NAME")).append(" PRIMARY KEY (");
				else
					sb.append(",");
				first = false;
				String columnName = sourcePK.getString("COLUMN_NAME");
				sb.append(checkColumnName(columnName));
			}
			if (hasPK)	//	close constraint
				sb.append(")");	// USING INDEX TABLESPACE INDX
			sourcePK.close();
			//
			sb.append(")");	//	close create table
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "createTable", ex);
			return false;
		}

		//	Execute Create Table
		if (!executeCommands(new String[]{sb.toString()}, m_conn, false, true))
			return true;	// continue
		
		//	Create Inexes
		createTableIndexes(mTable, md);
		
		return createTableData(mTable);
	}	//	createTable
	
	/**
	 * 	Check Column Name
	 *	@param columnName column name
	 *	@return column name with correct case
	 */
	private String checkColumnName (String columnName)
	{
		return M_Element.getColumnName (columnName);
	}	//	checkColumnName
	
	/**
	 * 	Create Table Indexes
	 *	@param mTable table
	 *	@param md meta data
	 */
	private void createTableIndexes(MTable mTable, DatabaseMetaData md)
	{
		String tableName = mTable.getTableName();
		log.info(tableName);
		String catalog = m_dbSource.getCatalog();
		String schema = m_dbSource.getSchema();
		String table = tableName.toUpperCase();
		try
		{
		//	ResultSet sourceIndex = 
				md.getIndexInfo(catalog, schema, table, false, false);

		}
		catch (Exception e)
		{
			
		}
	}	//	createTableIndexes
	
	
	/**
	 * 	Create/Copy Table Data
	 *	@param mTable model table
	 *	@return true if data created/copied
	 */
	private boolean createTableData (MTable mTable)
	{
		boolean success = true;
		int count = 0;
		int errors = 0;
		long start = System.currentTimeMillis();
		
		//	Get Table Data
		String sql = "SELECT * FROM " + mTable.getTableName();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, mTable.get_Trx());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				if (createTableDataRow(rs, mTable))
					count++;
				else
					errors++;
			}
		}
		catch (Exception e) {
			log.log (Level.SEVERE, sql, e);
			success = false;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		long elapsed = System.currentTimeMillis() - start;
		log.config("Inserted=" + count + " - Errors=" + errors 
			+ " - " + elapsed + " ms");
		return success;
	}	//	createTableData
	
	/**
	 * 	Create Table Data Row
	 *	@param rs result set
	 *	@param mTable table
	 *	@return true if created
	 */
	private boolean createTableDataRow (ResultSet rs, MTable mTable)
	{
		StringBuffer insert = new StringBuffer ("INSERT INTO ")
			.append(mTable.getTableName()).append(" (");
		StringBuffer values = new StringBuffer ();
		//
		MColumn[] columns = mTable.getColumns(false);
		for (int i = 0; i < columns.length; i++)
		{
			if (i != 0)
			{
				insert.append(",");
				values.append(",");
			}
			MColumn column = columns[i];
			String columnName = column.getColumnName();
			insert.append(columnName);
			//
			int dt = column.getAD_Reference_ID();
			try
			{
				Object value = rs.getObject(columnName);
				if (rs.wasNull())
				{
					values.append("NULL");
				}
				else if (columnName.toUpperCase().endsWith("_ID")	// Record_ID, C_ProjectType defined as Button
					|| FieldType.isNumeric(dt) 
					|| (FieldType.isID(dt) && !columnName.equals("AD_Language"))) 
				{
					BigDecimal bd = rs.getBigDecimal(columnName);
					int scale = FieldType.getDefaultPrecision(dt);
					bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
					String s = bd.toString();
					values.append(s);
				}
				else if (FieldType.isDate(dt))
				{
					Timestamp ts = rs.getTimestamp(columnName);
					String tsString = m_dbTarget.TO_DATE(ts, dt == DisplayTypeConstants.Date);
					values.append(tsString);
				}
				else if (FieldType.isLOB(dt))
				{
					// ignored
					values.append("NULL");
				}
				else if (FieldType.isText(dt) || dt == DisplayTypeConstants.YesNo 
					|| dt == DisplayTypeConstants.List || dt == DisplayTypeConstants.Button
					|| columnName.equals("AD_Language"))
				{
					String s = rs.getString(columnName);
					values.append(DB.TO_STRING(s));
				}
				else
				{
					log.warning("Unknown DisplayType=" + dt 
						+ " - " + value + " [" + value.getClass().getName() + "]");
					values.append("NuLl");
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, columnName, e);
			}
		}	//	for all columns
		
		//
		insert.append(") VALUES (").append(values).append(")");
		return executeCommands(new String[]{insert.toString()}, 
			m_conn, false, false);	//	do not convert as text is converted
	}	//	createTableDataRow
	
	
	/**
	 * 	Enable Constraints
	 *	@param list list
	 *	@return true if constraints enabled/created
	 */
	private boolean enableConstraints (ArrayList<String> list)
	{
		log.info("");
		return false;
	}	//	enableConstraints
	

	private void databaseBuild()
	{
		//	Build Script
		//jz remove hard coded path later
		String fileName = "C:\\Compiere\\compiere-all2\\db\\database\\DatabaseBuild.sql";
		File file = new File (fileName);
		if (!file.exists())
			log.severe("No file: " + fileName);
		
	//	FileReader reader = new FileReader (file);
		
		
		
	}	//	databaseBuild
	
	/**
	 * 	Get Connection
	 * 	@param asSystem if true execute as db system administrator 
	 * 	@param createNew create new connection
	 *	@return connection or null
	 */
	private Connection getConnection (boolean asSystem, boolean createNew)
	{
		if (!createNew && m_conn != null)
			return m_conn;
		//
		String dbUrl = m_dbTarget.getConnectionURL(m_databaseHost, m_databasePort, 
			(asSystem ? m_dbTarget.getSystemDatabase(m_databaseName) : m_databaseName), 
			(asSystem ? m_dbTarget.getSystemUser() : m_compiereUser));
		try
		{
			if (asSystem)
				m_conn = m_dbTarget.getDriverConnection(dbUrl, m_dbTarget.getSystemUser(), m_systemPassword);
			else
				m_conn = m_dbTarget.getDriverConnection(dbUrl, m_compiereUser, m_compierePassword);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, dbUrl, e);
		}
		return m_conn;
	}	//	getConnection
	
	
	/**************************************************************************
	 * 	Execute Commands
	 * 	@param cmds array of SQL commands
	 * 	@param conn connection
	 * 	@param batch tf true commit as batch
	 * 	@param doConvert convert to DB specific notation
	 *	@return true if success
	 */
	private boolean executeCommands (String[] cmds, Connection conn, 
		boolean batch, boolean doConvert)
	{
		if (cmds == null || cmds.length == 0)
		{
			log.warning("No Commands");
			return false;
		}
		
		Statement stmt = null;
		String cmd = null;
		String cmdOriginal = null;
		try
		{
			if (conn == null)
			{
				conn = getConnection(false, false);
				if (conn == null)
					return false;
			}
			if (conn.getAutoCommit() == batch)
				conn.setAutoCommit(!batch);
			stmt = conn.createStatement();
			
			//	Commands
			for (String element : cmds) {
				cmd = element;
				cmdOriginal = element;
				if (cmd == null || cmd.length() == 0)
					continue;
				//
				if (cmd.indexOf('@') != -1)
				{
					cmd = Util.replace(cmd, "@SystemPassword@", m_systemPassword);
					cmd = Util.replace(cmd, "@CompiereUser@", m_compiereUser);
					cmd = Util.replace(cmd, "@CompierePassword@", m_compierePassword);
					cmd = Util.replace(cmd, "@SystemPassword@", m_systemPassword);
					cmd = Util.replace(cmd, "@DatabaseName@", m_databaseName);
					if (m_databaseDevice != null)
						cmd = Util.replace(cmd, "@DatabaseDevice@", m_databaseDevice);
				}
				if (doConvert)
					cmd = m_dbTarget.convertStatement(cmd);
				writeLog(cmd);
				log.finer(cmd);
				int no = stmt.executeUpdate(cmd);
				log.finest("# " + no);
			}
			//
			stmt.close();
			stmt = null;
			//
			if (batch)
				conn.commit();
			//
			return true;
		}
		catch (Exception e)
		{
			String msg = e.getMessage();
			if (msg == null || msg.length() == 0)
				msg = e.toString();
			msg += " (";
			if (e instanceof SQLException)
			{
				msg += "State=" + ((SQLException)e).getSQLState() 
					+ ",ErrorCode=" + ((SQLException)e).getErrorCode();
			}
			msg += ")";
			if (cmdOriginal != null && !cmdOriginal.equals(cmd))
				msg += " - " + cmdOriginal;
			msg += "\n=>" + cmd;
			log.log(Level.SEVERE, msg);
		}
		//	Error clean up
		try
		{
			if (stmt != null)
				stmt.close();
		}
		catch (SQLException e1)
		{
			log.log(Level.SEVERE, "close statement", e1);
		}
		stmt = null;
		return false;
	}	//	execureCommands

	
	/**
	 * 	Write to File Log
	 *	@param cmd cmd
	 */
	private void writeLog (String cmd)
	{
		try
		{
			if (m_writer == null)
			{
				File file = File.createTempFile("create", ".log");
				m_writer = new PrintWriter(new FileWriter(file));
				log.info(file.toString());
			}
			m_writer.println(cmd);
			m_writer.flush();
		}
		catch (Exception e)
		{
			log.severe(e.toString());
		}
	}	//	writeLog
	
	private PrintWriter 	m_writer = null;
	
	
	/**************************************************************************
	 * 	Create DB
	 *	@param args
	 */
	public static void main (String[] args)
	{
		Compiere.startup(true, false, "CreateCompiere");
		CLogMgt.setLevel(Level.FINE);
		CLogMgt.setLoggerLevel(Level.FINE,null);

		//	C_UOM_Conversion
		//	I_BankStatement
		//	
		/**
		CreateCompiere cc = new CreateCompiere (Database.DB_DERBY, "localhost", 1527, null);
		cc.setCompiereUser("compiere", "compiere");
		cc.setDatabaseName("compiere", "compiere");
		if (!cc.testConnection())
			return;
		cc.cleanStart();
		//
	//	cc.copy(null, false);
		cc.copy("TableName > 'C_RfQResponseLineQty'", false);
		**/
	}	//	main
	
}   //  CreateCompiere
