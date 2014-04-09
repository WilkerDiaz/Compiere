/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.db;

import java.sql.*;

import javax.sql.*;

/**
 *  Interface for Compiere Databases
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereDatabase.java 8615 2010-04-09 21:55:17Z sraval $
 */
public interface CompiereDatabase
{

	/**
	 *  Get Database Name
	 *  @return database short name
	 */
	public String getName();

	/**
	 *  Get Database Description
	 *  @return database long name and version
	 */
	public String getDescription();

	/**
	 *  Get and register Database Driver
	 *  @return Driver
	 *  @throws SQLException
	 */
	public Driver getDriver() throws SQLException;


	/**
	 *  Get Standard JDBC Port
	 *  @return standard port
	 */
	public int getStandardPort();

	/**
	 *  Get Database Connection String
	 *  @param connection Connection Descriptor
	 *  @return connection String
	 */
	public String getConnectionURL (CConnection connection);
	
	/**
	 * 	Get Connection URL
	 *	@param dbHost db Host
	 *	@param dbPort db Port
	 *	@param dbName db Name
	 *	@param userName user name
	 *	@return url
	 */
	public String getConnectionURL (String dbHost, int dbPort, String dbName,
		String userName);

	/**
	 *  Get Database Connection String
	 *  @param connectionURL Connection URL
	 *  @param userName user name
	 *  @return connection String
	 */
	public String getConnectionURL (String connectionURL, String userName);

	/**
	 * 	Get JDBC Catalog
	 *	@return catalog
	 */
	public String getCatalog();
	
	/**
	 * 	Get JDBC Schema
	 *	@return schema
	 */
	public String getSchema();

	/**
	 *  Supports BLOB
	 *  @return true if BLOB is supported
	 */
	public boolean supportsBLOB();

	/**
	 *  String Representation
	 *  @return info
	 */
	public String toString();

	
	/**************************************************************************
	 *  Convert an individual Oracle Style statements to target database statement syntax
	 *
	 *  @param oraStatement oracle statement
	 *  @return converted Statement
	 */
	public String convertStatement (String oraStatement);

	

	/**
	 *  Check if DBMS support the sql statement or the function
	 *  @sql SQL statement
	 *  @return true: yes
	 */
	public boolean isSupported(String sql);

	

	/**
	 *  change update set (...) = (select ... from ) standard format 
	 *  @param  sql update clause
	 *  @return new sql
	 */
	public String updateSetSelectList (String sql);
	

	/**
	 *  Get constraint type associated with the index
	 *  @conn connection
	 *  @tableName table name
	 *  @IXName Index name
	 *  @return String[0] = 0: do not know, 1: Primary Key  2: Foreign Key
	 *  		String[1] - String[n] = Constraint Name
	 */
	public String getConstraintType(Connection conn, String tableName, String IXName);
	

	/**
	 *  Check and generate an alternative SQL
	 *  @reExNo number of re-execution
	 *  @msg previous execution error message
	 *  @sql previous executed SQL
	 *  @return String, the alternative SQL, null if no alternative
	 */
	public String getAlternativeSQL(int reExNo, String msg, String sql);

	/**
	 *  Get Name of System User
	 *  @return e.g. sa, system
	 */
	public String getSystemUser();
	
	/**
	 *  Get Name of System Database
	 *  @param databaseName database Name
	 *  @return e.g. master or database Name
	 */
	public String getSystemDatabase(String databaseName);
	

	/**
	 *  Create SQL TO Date String from Timestamp
	 *
	 *  @param  time Date to be converted
	 *  @param  dayOnly true if time set to 00:00:00
	 *  @return date function
	 */
	public String TO_DATE (Timestamp time, boolean dayOnly);

	/**
	 *  Create SQL for formatted Date, Number
	 *
	 *  @param  columnName  the column name in the SQL
	 *  @param  displayType Display Type
	 *  @param  AD_Language 6 character language setting (from Env.LANG_*)
	 *
	 *  @return TRIM(TO_CHAR(columnName,'9G999G990D00','NLS_NUMERIC_CHARACTERS='',.'''))
	 *      or TRIM(TO_CHAR(columnName,'TM9')) depending on DisplayType and Language
	 *  @see org.compiere.util.DisplayType
	 *  @see org.compiere.util.Env
	 *
	 **/
	public String TO_CHAR (String columnName, int displayType, String AD_Language);

	
	/**
	 *  Get a string representation of literal used in SQL clause
	 *
	 *  @param  sqlClause "S", "U","I", "W"
	 *  @param  dataType java.sql.Types
	 *
	 *  @return db2: nullif(x,x)
	 */
	
	public String nullValue (String sqlClause, int dataType);

	
	
	/**
	 * 	Get new Cached Connection on Server
	 *	@param connection info
	 *  @param autoCommit true if autocommit connection
	 *  @param transactionIsolation Connection transaction level
	 *	@return connection or null
	 *  @throws Exception
	 */
	public Connection getCachedConnection (CConnection connection, 
		boolean autoCommit, int transactionIsolation) throws SQLException;

	/**
	 * 	Get new Connection from Driver
	 *	@param connection info
	 *	@return connection or null
	 *  @throws SQLException
	 */
	public Connection getDriverConnection (CConnection connection) throws SQLException;

	/**
	 * 	Get new Driver Connection
	 *	@param dbUrl URL
	 *	@param dbUid user
	 *	@param dbPwd password
	 *	@return connection
	 *	@throws SQLException
	 */
	public Connection getDriverConnection (String dbUrl, String dbUid, String dbPwd) 
		throws SQLException;

	/**
	 * 	Create DataSource
	 *	@param connection connection
	 *	@return data dource
	 */
	public DataSource getDataSource(CConnection connection);

	/**
	 * 	Get Status
	 * 	@return status info
	 */
	public String getStatus();

	/**
	 * 	Close
	 */
	public void close();

	/**
	 * 	Get Data Type
	 *	@param DisplayType display type
	 *	@return data type
	 */
//	public String getDataType (int displayType, int precision,
//		boolean defaultValue)
	
	/**
	 *  Get the Database specific Clob data type
	 *  @param connection connection
	 *  @param clobString clob string
	 *  @return Clob
	 */
	public Clob getClob(Connection connection, String clobString);
	
	/**
	 *  Get the Database specific Blob data type
	 *  @param connection connection
	 *  @param bytes bytes
	 *  @return Blob
	 */
	public Blob getBlob(Connection connection, byte[] bytes);
	
	public String[] getJDBCCacheInfo();
}   //  CompiereDatabase

