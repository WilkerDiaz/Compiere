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

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.sql.*;

import oracle.jdbc.*;
import oracle.jdbc.pool.*;

import org.compiere.*;
import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.startup.*;
import org.compiere.util.*;

/**
 *  Oracle Database Port
 *
 *  @author     Jorg Janke
 *  @version    $Id: DB_Oracle.java 9162 2010-08-04 08:39:23Z ragrawal $
 */
public class DB_Oracle implements CompiereDatabase
{
	/**
	 *  Oracle Database
	 */
	public DB_Oracle()
	{
		/**	Causes VPN problems ???
		try
		{
			getDriver();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage());
		}
		 **/
	}   //  DB_Oracle

	/** Static Driver           	*/
	private volatile static OracleDriver	   	s_driver = null;
	/** Driver Class Name			*/
	public static final String		DRIVER = "oracle.jdbc.OracleDriver";

	/** Default Port            	*/
	public static final int 		DEFAULT_PORT = 1521;
	/** Default Connection Manager Port */
	public static final int 		DEFAULT_CM_PORT = 1630;

	/** Connection String       	*/
	private String          		m_connectionURL;

	/** Data Source					*/
	private volatile OracleDataSource		m_ds = null;

	/** Use Connection Cache (false) Problems with closed connections otherwise */
	private static final boolean	USE_CACHE = true;
	/** Connection Cache			*/
	private volatile OracleConnectionCacheManager	m_cacheMgr = null;
	/** Connection Cache Name		*/
	private static final String 	CACHE_NAME = "CompiereCCache";
	/** Cached User Name			*/
	private String					m_userName = null;

	/**	Logger			*/
	private static CLogger			log	= CLogger.getCLogger (DB_Oracle.class);


	/**
	 *  Get Database Name
	 *  @return database short name
	 */
	@Override
	public String getName()
	{
		return Environment.DBTYPE_ORACLE;
	}   //  getName

	/**
	 *  Get Database Description
	 *  @return database long name and version
	 */
	@Override
	public String getDescription()
	{
		try
		{
			if (s_driver == null)
				getDriver();
		}
		catch (Exception e)
		{
		}
		if (s_driver != null)
			return s_driver.toString();
		return "No Driver";
	}   //  getDescription

	/**
	 *  Get Standard JDBC Port
	 *  @return standard port
	 */
	@Override
	public int getStandardPort()
	{
		return DEFAULT_PORT;
	}   //  getStandardPort

	/**
	 *  Get and register Database Driver
	 *  @return Driver
	 *	@throws SQLException
	 */
	@Override
	public Driver getDriver() throws SQLException
	{
		if (s_driver == null)
		{
			synchronized(this) {
				if(s_driver == null) {
					//	Speed up transfer rate
					System.setProperty("oracle.jdbc.TcpNoDelay", "true");
					//	Oracle Multi - Language
					System.setProperty("oracle.jdbc.defaultNChar", "true");
					//
					s_driver = new OracleDriver();
					DriverManager.registerDriver (s_driver);
					DriverManager.setLoginTimeout (Database.CONNECTION_TIMEOUT);
				}

			}
		}
		return s_driver;

	}   //  getDriver

	/**
	 *  Get Database Connection String.
	 *  <pre>
	 *  Timing:
	 *  - CM with source_route not in address_list  = 28.5 sec
	 *  - CM with source_route in address_list      = 58.0 sec
	 *  - direct    = 4.3-8 sec  (no real difference if on other box)
	 *  - bequeath  = 3.4-8 sec
	 *  </pre>
	 *  @param connection Connection Descriptor
	 *  @return connection String
	 */
	@Override
	public String getConnectionURL (CConnection connection)
	{
		StringBuffer sb = null;
		//  Server Connections (bequeath)
		if (connection.isBequeath())
		{
			sb = new StringBuffer ("jdbc:oracle:oci8:@");
			//  bug: does not work if there is more than one db instance - use Net8
			//	sb.append(connection.getDbName());
		}
		else        //  thin driver
		{
			sb = new StringBuffer ("jdbc:oracle:thin:@");
			//  direct connection
			if (connection.isViaFirewall())
			{
				//  (description=(address_list=
				//  ( (source_route=yes)
				//    (address=(protocol=TCP)(host=cmhost)(port=1630))
				//    (address=(protocol=TCP)(host=dev)(port=1521))
				//  (connect_data=(service_name=dev1.compiere.org)))
				sb.append("(DESCRIPTION=(ADDRESS_LIST=")
				.append("(SOURCE_ROUTE=YES)")
				.append("(ADDRESS=(PROTOCOL=TCP)(HOST=").append(connection.getFwHost())
				.append(")(PORT=").append(connection.getFwPort()).append("))")
				.append("(ADDRESS=(PROTOCOL=TCP)(HOST=").append(connection.getDbHost())
				.append(")(PORT=").append(connection.getDbPort()).append(")))")
				.append("(CONNECT_DATA=(SERVICE_NAME=").append(connection.getDbName()).append(")))");
			}
			else
			{
				//  old: jdbc::thin:@dev2:1521:sid
				//	new: jdbc:oracle:thin:@//dev2:1521/serviceName
				sb.append("//")
				.append(connection.getDbHost())
				.append(":").append(connection.getDbPort())
				.append("/").append(connection.getDbName());
			}
		}
		m_connectionURL = sb.toString();
		//	log.config(m_connectionURL);
		//
		m_userName = connection.getDbUid();
		return m_connectionURL;
	}   //  getConnectionURL

	/**
	 * 	Get Connection URL.
	 * 	http://download-east.oracle.com/docs/cd/B14117_01/java.101/b10979/urls.htm#BEIDBFDF
	 *	@param dbHost db Host
	 *	@param dbPort db Port
	 *	@param dbName db Name
	 *	@param userName user name
	 *	@return connection
	 */
	@Override
	public String getConnectionURL (String dbHost, int dbPort, String dbName,
			String userName)
	{
		m_userName = userName;
		m_connectionURL = "jdbc:oracle:thin:@//"
			+ dbHost + ":" + dbPort + "/" + dbName;
		return m_connectionURL;
	}	//	getConnectionURL

	/**
	 *  Get Database Connection String
	 *  @param connectionURL Connection URL
	 *  @param userName user name
	 *  @return connection String
	 */
	@Override
	public String getConnectionURL (String connectionURL, String userName)
	{
		m_userName = userName;
		m_connectionURL = connectionURL;
		return m_connectionURL;
	}	//	getConnectionURL

	/**
	 * 	Get JDBC Catalog
	 *	@return null - not used
	 */
	@Override
	public String getCatalog()
	{
		return null;
	}	//	getCatalog

	/**
	 * 	Get JDBC Schema
	 *	@return user name
	 */
	@Override
	public String getSchema()
	{
		if (m_userName != null)
			return m_userName.toUpperCase();
		log.warning("User Name not set (yet) - call getConnectionURL first");
		return null;
	}	//	getSchema

	/**
	 *  Supports BLOB
	 *  @return true if BLOB is supported
	 */
	@Override
	public boolean supportsBLOB()
	{
		return true;
	}   //  supportsBLOB

	/**
	 *  String Representation
	 *  @return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("DB_Oracle[");
		sb.append(m_connectionURL);
		try
		{
			if (m_ds != null)
				sb.append("-").append(m_ds.getDataSourceName())
				//	.append(",ExplCache=").append(m_ds.getExplicitCachingEnabled())
				.append(",ImplCache=").append(m_ds.getImplicitCachingEnabled())
				.append(",MaxStmts=").append(m_ds.getMaxStatements());
			//	.append(",Ref=").append(m_ds.getReference());
			if (m_cacheMgr != null)
				sb.append(";ConnectionActive=").append(m_cacheMgr.getNumberOfActiveConnections(CACHE_NAME))
				.append(",CacheAvailable=").append(m_cacheMgr.getNumberOfAvailableConnections(CACHE_NAME));
		}
		catch (Exception e)
		{
			sb.append("=").append(e.getLocalizedMessage());
		}
		sb.append("]");
		return sb.toString();
	}   //  toString

	/**
	 * 	Get Status
	 * 	@return status info
	 */
	@Override
	public String getStatus()
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			if ((m_cacheMgr != null) && m_cacheMgr.existsCache(CACHE_NAME))
				sb.append("-Connections=").append(m_cacheMgr.getNumberOfActiveConnections(CACHE_NAME))
				.append(",Cache=").append(m_cacheMgr.getNumberOfAvailableConnections(CACHE_NAME));
		}
		catch (Exception e)
		{}
		return sb.toString();
	}	//	getStatus


	/**************************************************************************
	 *  Convert an individual Oracle Style statements to target database statement syntax.
	 *  @param oraStatement oracle statement
	 *  @return converted Statement oracle statement
	 */
	@Override
	public String convertStatement (String oraStatement)
	{
		return oraStatement;
	}   //  convertStatement



	/**
	 *  Check if DBMS support the sql statement
	 *  @sql SQL statement
	 *  @return true: yes
	 */
	@Override
	public boolean isSupported(String sql)
	{
		return true;
		//jz temp, modify later
	}



	/**
	 *  Get constraint type associated with the index
	 *  @tableName table name
	 *  @IXName Index name
	 *  @return String[0] = 0: do not know, 1: Primary Key  2: Foreign Key
	 *  		String[1] - String[n] = Constraint Name
	 */
	@Override
	public String getConstraintType(Connection conn, String tableName, String IXName)
	{
		if ((IXName == null) || (IXName.length()==0))
			return "0";
		if (IXName.endsWith("_KEY"))
			return "1"+IXName;
		else
			return "0";
		//jz temp, modify later from user.constraints
	}

	/**
	 *  Get Name of System User
	 *  @return system
	 */
	@Override
	public String getSystemUser()
	{
		return "system";
	}	//	getSystemUser

	/**
	 *  Get Name of System Database
	 *  @param databaseName database Name
	 *  @return e.g. master or database Name
	 */
	@Override
	public String getSystemDatabase(String databaseName)
	{
		return databaseName;
	}	//	getSystemDatabase


	/**
	 *  Create SQL TO Date String from Timestamp
	 *
	 *  @param  time Date to be converted
	 *  @param  dayOnly true if time set to 00:00:00
	 *
	 *  @return TO_DATE('2001-01-30 18:10:20',''YYYY-MM-DD HH24:MI:SS')
	 *      or  TO_DATE('2001-01-30',''YYYY-MM-DD')
	 */
	@Override
	public String TO_DATE (Timestamp time, boolean dayOnly)
	{
		if (time == null)
		{
			if (dayOnly)
				return "TRUNC(SysDate)";
			return "SysDate";
		}

		StringBuffer dateString = new StringBuffer("TO_DATE('");
		//  YYYY-MM-DD HH24:MI:SS.mmmm  JDBC Timestamp format
		String myDate = time.toString();
		if (dayOnly)
		{
			dateString.append(myDate.substring(0,10));
			dateString.append("','YYYY-MM-DD')");
		}
		else
		{
			dateString.append(myDate.substring(0, myDate.indexOf(".")));	//	cut off miliseconds
			dateString.append("','YYYY-MM-DD HH24:MI:SS')");
		}
		return dateString.toString();
	}   //  TO_DATE

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
	 *   */
	@Override
	public String TO_CHAR (String columnName, int displayType, String AD_Language)
	{
		StringBuffer retValue = new StringBuffer("TRIM(TO_CHAR(");
		retValue.append(columnName);

		//  Numbers
		if (FieldType.isNumeric(displayType))
		{
			if (displayType == DisplayTypeConstants.Amount)
			{
				retValue = new StringBuffer(" (CASE WHEN ").append(columnName).append("< 9999999 THEN")
							.append(" TRIM(TO_CHAR(").append(columnName).append(",'9G999G990D00'))")
							.append(" ELSE TRIM(TO_CHAR(").append(columnName).append(",'9.99EEEE')) END) ");
				return retValue.toString();
				
			}
			else
				retValue.append(",'TM9'");
			//  TO_CHAR(GrandTotal,'9G999G990D00','NLS_NUMERIC_CHARACTERS='',.''')
			if (!Language.isDecimalPoint(AD_Language))      //  reversed
				retValue.append(",'NLS_NUMERIC_CHARACTERS='',.'''");
		}
		else if (FieldType.isDate(displayType))
		{
			retValue.append(",'")
			.append(Language.getLanguage(AD_Language).getDBdatePattern())
			.append("'");
		}
		retValue.append("))");
		//
		return retValue.toString();
	}   //  TO_CHAR


	/**
	 * 	Create DataSource
	 *	@param connection connection
	 *	@return data source
	 */
	@Override
	public DataSource getDataSource(CConnection connection) {
		if(m_ds == null) {
			synchronized(this) {
				if (m_ds == null) {
					try {
						OracleDataSource ds = new OracleDataSource();
						ds.setDriverType("thin");
						ds.setNetworkProtocol("tcp");
						ds.setServerName(connection.getDbHost());
						ds.setServiceName(connection.getDbName());
						ds.setPortNumber(connection.getDbPort());
						ds.setUser(connection.getDbUid());
						ds.setPassword(connection.getDbPwd());
						//
						ds.setDataSourceName("CompiereDS");
						ds.setDescription("Compiere Oracle Data Source");

						// http://download-east.oracle.com/docs/cd/B14117_01/java.101/b10979/oralob.htm#sthref1258

						Properties connProperties = new Properties();
						// As of bug: BUG-1568923 it is not UseClob and TryClob
						connProperties.setProperty("SetBigStringUseClob", "true");
						connProperties.setProperty("SetBigStringTryClob", "true");
						ds.setConnectionProperties(connProperties);
						//

						if (USE_CACHE) {
							Properties cacheProperties = new Properties();
							cacheProperties.setProperty("ValidateConnection", "true");
							cacheProperties.setProperty("ConnectionWaitTimeout", "30");
							if (Ini.isClient()) {
								// Client settings
								cacheProperties.setProperty("InitialLimit", "1");
								//cacheProperties.setProperty("MaxLimit", "100");
								cacheProperties.setProperty("InactivityTimeout", "900"); 
							} else {
								// Server settings
								cacheProperties.setProperty("InitialLimit", "10");
								cacheProperties.setProperty("MinLimit", "15");
								String maxLimit = Ini.getProperty(Ini.P_DB_ORACLE_MAX_CONNECTION_LIMIT);
								cacheProperties.setProperty("MaxLimit", maxLimit);
								
								//max idle time a *physical* connection can remain not active and without a 
								//logical handle associated with it before getting closed
								cacheProperties.setProperty("InactivityTimeout", "900"); 
								
								//max time before connection without SQL activity is reclaimed and returned to the cache for reuse
								cacheProperties.setProperty("AbandonedConnectionTimeout", "3600"); 
							}

							ds.setConnectionCachingEnabled(true);
							ds.setConnectionCacheProperties(cacheProperties);
							ds.setConnectionCacheName(CACHE_NAME);

							m_cacheMgr = OracleConnectionCacheManager.getConnectionCacheManagerInstance();
						}
						log.config(toString());

						m_ds = ds;
					} catch (Exception e) {
						log.log(Level.WARNING, toString(), e);
					}
				}
			}
		}
		return m_ds;
	} // getDataSource


	/**
	 * 	Get new Connection from cache
	 *	@param connection info
	 *  @param autoCommit true if autocommit connection
	 *  @param transactionIsolation Connection transaction level
	 *	@return connection or null
	 *	@throws Exception
	 */
	@Override
	public Connection getCachedConnection (CConnection connection,
			boolean autoCommit, int transactionIsolation)
	throws SQLException
	{
		OracleConnection conn = (OracleConnection) getDataSource(connection).getConnection();
		if (conn != null) {
			if (conn.getTransactionIsolation() != transactionIsolation) {
				conn.rollback();
				conn.setTransactionIsolation(transactionIsolation);
			}
			if (conn.getAutoCommit() != autoCommit)
				conn.setAutoCommit(autoCommit);
			conn.setDefaultRowPrefetch(20); // 10 default - reduces round trips

			//note 369236.1 CLIENTID, ACTION, MODULE
			String[] metrics = new String[OracleConnection.END_TO_END_STATE_INDEX_MAX]; 
			if (Ini.isClient()) {
				metrics[OracleConnection.END_TO_END_MODULE_INDEX]="Compiere Client";
				metrics[OracleConnection.END_TO_END_CLIENTID_INDEX] = Env.getCtx().getContext("#AD_Session_ID");
			}
			else {
				metrics[OracleConnection.END_TO_END_MODULE_INDEX]="Compiere Server";
				Ctx ctx = Env.getThreadLocalCtx();
				metrics[OracleConnection.END_TO_END_CLIENTID_INDEX] = ctx != null ? ctx.getContext("#AD_Session_ID")
						: null;
			}


			((OracleConnection)conn).setEndToEndMetrics(metrics,(short)0);
		}
		else {
			String[] jdbcCacheMessages = getJDBCCacheInfo(); 
			log.severe(jdbcCacheMessages[0]+"\n"+jdbcCacheMessages[1]);
		}		
		return conn;
	}	//	getCachedConnection

	/**
	 * 	Get new Connection from Driver
	 *	@param connection info
	 *	@return connection or null
	 *	@throws SQLException
	 */
	@Override
	public Connection getDriverConnection (CConnection connection) throws SQLException
	{
		getDriver();
		return DriverManager.getConnection (getConnectionURL (connection),
				connection.getDbUid(), connection.getDbPwd());
	}	//	getDriverConnection

	/**
	 * 	Get new Driver Connection
	 *	@param dbUrl URL
	 *	@param dbUid user
	 *	@param dbPwd password
	 *	@return connection
	 *	@throws SQLException
	 */
	@Override
	public Connection getDriverConnection (String dbUrl, String dbUid, String dbPwd)
	throws SQLException
	{
		getDriver();
		return DriverManager.getConnection (dbUrl, dbUid, dbPwd);
	}	//	getDriverConnection

	/**
	 * 	Close
	 */
	@Override
	public void close()
	{
		log.config(toString());
		if (m_ds != null)
		{
			try
			{
				m_ds.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		m_cacheMgr = null;
		m_ds = null;
	}	//	close

	/**
	 * 	Clean up
	 */
	public void cleanup()
	{
		if (!USE_CACHE)
			return;

		log.config("");
		try
		{
			if (m_cacheMgr == null)
				m_cacheMgr = OracleConnectionCacheManager.getConnectionCacheManagerInstance();
			String[] cacheNames = m_cacheMgr.getCacheNameList();
			for (String name : cacheNames) {
				System.out.println("  cleanup: " + name);
				System.out.println("    Before = Active=" + m_cacheMgr.getNumberOfActiveConnections(name)
						+ ", Available=" + m_cacheMgr.getNumberOfAvailableConnections(name));
				m_cacheMgr.purgeCache(name, false);
				System.out.println("    Cached = Active=" + m_cacheMgr.getNumberOfActiveConnections(name)
						+ ", Available=" + m_cacheMgr.getNumberOfAvailableConnections(name));
				m_cacheMgr.purgeCache(name, true);
				System.out.println("    All    = Active=" + m_cacheMgr.getNumberOfActiveConnections(name)
						+ ", Available=" + m_cacheMgr.getNumberOfAvailableConnections(name));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	//	cleanup
	
	/**************************************************************************
	 * 	Get JDBC connection cache connection status and properties
	 *	
	 */
	public String[] getJDBCCacheInfo()
	{
		String cacheInfo[]= {"",""};
		if (!USE_CACHE)
			return cacheInfo;
		try
		{
			if (m_cacheMgr == null)
				m_cacheMgr = OracleConnectionCacheManager.getConnectionCacheManagerInstance();
			String[] cacheNames = m_cacheMgr.getCacheNameList();
			Properties cacheProperties = new Properties();	
			for (String name : cacheNames) {
				cacheInfo[0] =  name + " Active=" + m_cacheMgr.getNumberOfActiveConnections(name)
						+ ", Available=" + m_cacheMgr.getNumberOfAvailableConnections(name);
				cacheProperties = m_cacheMgr.getCacheProperties(name);
				cacheInfo[1] = cacheProperties.toString();
				//m_cacheMgr.refreshCache(name, OracleConnectionCacheManager.REFRESH_INVALID_CONNECTIONS);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return cacheInfo;
	}	//	jdbcCacheInfo

	/**************************************************************************
	 * 	Handle Abandoned Connection
	 *	@param conn connection
	 *	@param userObject
	 *	@return true if close - false for keeping it
	 */
	public boolean handleAbandonedConnection (OracleConnection conn, Object userObject)
	{
		System.out.println("--------------------handleAbandonedConnection " + conn + " - " + userObject);
		return true;	//	reclaim it
	}	//	handleAbandonedConnection

	/**
	 * 	Release Connection
	 *	@param conn connection
	 *	@param userObject
	 */
	public void releaseConnection (OracleConnection conn, Object userObject)
	{
		System.out.println("----------------------releaseConnection " + conn + " - " + userObject);
	}	//	releaseConnection


	/**
	 * 	Get Data Type
	 *	@param displayType display type
	 *	@param precision precision
	 *	@param defaultValue if true adds default value
	 *	@return data type
	 */
	public String getDataType (int displayType, int precision,
			boolean defaultValue)
	{
		String retValue = null;
		switch (displayType)
		{
		//	IDs
		case DisplayTypeConstants.Account:
		case DisplayTypeConstants.Assignment:
		case DisplayTypeConstants.Color:
		case DisplayTypeConstants.ID:
		case DisplayTypeConstants.Location:
		case DisplayTypeConstants.Locator:
		case DisplayTypeConstants.PAttribute:
		case DisplayTypeConstants.Search:
		case DisplayTypeConstants.Table:
		case DisplayTypeConstants.TableDir:
		case DisplayTypeConstants.Image:
			retValue = "NUMBER(10)";
			break;

			// Dynamic Precision
		case DisplayTypeConstants.Amount:
			retValue = "NUMBER";
			if (defaultValue)
				retValue += " DEFAULT 0";
			break;

		case DisplayTypeConstants.Binary:
			retValue = "BLOB";
			break;

		case DisplayTypeConstants.Button:
			retValue = "CHAR(1)";
			break;

			// Number Dynamic Precision
		case DisplayTypeConstants.CostPrice:
			retValue = "NUMBER";
			if (defaultValue)
				retValue += " DEFAULT 0";
			break;

			//	Date
		case DisplayTypeConstants.Date:
		case DisplayTypeConstants.DateTime:
		case DisplayTypeConstants.Time:
			retValue = "DATE";
			if (defaultValue)
				retValue += " DEFAULT SYSDATE";
			break;

			// 	Number(10)
		case DisplayTypeConstants.Integer:
			retValue = "NUMBER(10)";
			break;

		case DisplayTypeConstants.List:
			retValue = "CHAR(" + precision + ")";
			break;

			//	NVARCHAR
		case DisplayTypeConstants.Memo:
		case DisplayTypeConstants.String:
		case DisplayTypeConstants.Text:
			retValue = "NVARCHAR(" + precision + ")";
			break;

		case DisplayTypeConstants.TextLong:
			retValue = "CLOB";
			break;

			//	Dyn Prec
		case DisplayTypeConstants.Quantity:
			retValue = "NUMBER";
			break;

		case DisplayTypeConstants.YesNo:
			retValue = "CHAR(1)";
			break;

		default:
			log.warning("Unknown: " + displayType);
		break;
		}
		return retValue;
	}	//	getDataType


	/**
	 *  Check and generate an alternative SQL
	 *  @reExNo number of re-execution
	 *  @msg previous execution error message
	 *  @sql previous executed SQL
	 *  @return String, the alternative SQL, null if no alternative
	 */
	@Override
	public String getAlternativeSQL(int reExNo, String msg, String sql)
	{
		//check reExNo or based on reExNo to do a decision. Currently none

		return null; //do not do re-execution of alternative SQL
	}

	/**
	 *
	 * jz
	 *
	 *  change update set (...) = (select ... from ) standard format
	 *
	 *  @param  sql update clause
	 *
	 *  @return new sql
	 */

	@Override
	public String updateSetSelectList (String sql)
	{
		return sql;
	}   //


	/**
	 *  Get a string representation of literal used in SQL clause
	 *
	 *  @param  sqlClause "S", "U","I", "W"
	 *  @param  dataType java.sql.Types
	 *
	 *  @return db2: nullif(x,x)
	 */
	@Override
	public String nullValue (String sqlClause, int dataType)
	{
		return "NULL";
	}   //	nullValue

	/**
	 *  Get the Database specific Clob data type
	 *  @param connection connection
	 *  @param clobString clob string
	 *  @return Clob
	 */
	@Override
	public Clob getClob(Connection con, String clobString)
	{
		if ((con == null) || (clobString == null))
			return null;
		oracle.sql.CLOB clob = null;
		try
		{
			clob = oracle.sql.CLOB.createTemporary(con, true, oracle.sql.CLOB.DURATION_SESSION);
			clob.open(oracle.sql.CLOB.MODE_READWRITE);
			clob.setString(1, clobString);
			clob.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		return clob;
	}  // getClob()

	/**
	 *  Get the Database specific Blob data type
	 *  @param connection connection
	 *  @param bytes bytes
	 *  @return Blob
	 */
	@Override
	public Blob getBlob(Connection con, byte[] bytes)
	{
		if ((con == null) || (bytes == null))
			return null;
		oracle.sql.BLOB blob = null;
		try
		{
			blob = oracle.sql.BLOB.createTemporary(con, true, oracle.sql.BLOB.DURATION_SESSION);
			blob.open(oracle.sql.BLOB.MODE_READWRITE);
			blob.setBytes(1, bytes);
			blob.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		return blob;
	}  // getBlob()

	/**************************************************************************
	 * 	Testing
	 * 	@param args ignored
	 * @throws SQLException 
	 */
	public static void main (String[] args) throws SQLException
	{
		Compiere.startup(true);
		CConnection cc = CConnection.get();
		DB_Oracle db = (DB_Oracle)cc.getDatabase();
		db.cleanup();

		try
		{
			Connection conn = null;
			//	System.out.println("Driver=" + db.getDriverConnection(cc));
			DataSource ds = db.getDataSource(cc);
			System.out.println("DS=" + ds.getConnection());
			conn = db.getCachedConnection(cc, true, Connection.TRANSACTION_READ_COMMITTED);
			System.out.println("Cached=" + conn);
			System.out.println(db);
			//////////////////////////
			System.out.println("JAVA classpath: [\n" +
					System.getProperty("java.class.path") + "\n]");
			DatabaseMetaData dmd = conn.getMetaData();
			System.out.println("DriverVersion: ["+
					dmd.getDriverVersion()+"]");
			System.out.println("DriverMajorVersion: ["+
					dmd.getDriverMajorVersion()+"]");
			System.out.println("DriverMinorVersion: ["+
					dmd.getDriverMinorVersion()+"]");
			System.out.println("DriverName: ["+
					dmd.getDriverName()+"]");
			System.out.println("ProductName: ["+
					dmd.getDatabaseProductName() +"]");
			System.out.println("ProductVersion: [\n"+
					dmd.getDatabaseProductVersion()+"\n]");
			//////////////////////////
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		db.cleanup();

		System.out.println("--------------------------------------------------");
		/**
		DROP TABLE X_Test;
		CREATE TABLE X_Test
		(
		    Text1   NVARCHAR2(2000) NULL,
		    Text2   VARCHAR2(2000)  NULL
		);
		 **/
		try
		{
			String myString1 = "123456789 12345678";
			String myString = "";
			for (int i = 0; i < 99; i++)
				myString += myString1 + (char)('a'+i) + "\n";
			System.out.println(myString.length());
			System.out.println(Util.size(myString));
			//
			myString = Util.trimSize(myString, 2000);
			System.out.println(myString.length());
			System.out.println(Util.size(myString));
			//
			Connection conn2 = db.getCachedConnection(cc, true, Connection.TRANSACTION_READ_COMMITTED);
			/** **/
			PreparedStatement pstmt = conn2.prepareStatement
			("INSERT INTO X_Test(Text1, Text2) values(?,?)");
			pstmt.setString(1, myString); // NVARCHAR2 column
			pstmt.setString(2, myString); // VARCHAR2 column
			System.out.println(pstmt.executeUpdate());
			/** **/
			Statement stmt = conn2.createStatement();
			System.out.println(stmt.executeUpdate
					("INSERT INTO X_Test(Text1, Text2) values('" + myString + "','" + myString + "')"));
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
		db.cleanup();
		System.out.println("--------------------------------------------------");
		//		System.exit(0);


		System.out.println("--------------------------------------------------");
		try
		{
			Connection conn1 = db.getCachedConnection(cc, false, Connection.TRANSACTION_READ_COMMITTED);
			Connection conn2 = db.getCachedConnection(cc, true, Connection.TRANSACTION_READ_COMMITTED);
			Connection conn3 = db.getCachedConnection(cc, false, Connection.TRANSACTION_READ_COMMITTED);
			System.out.println("3 -> " + db);
			conn1.close();
			conn2.close();
			conn1 = db.getCachedConnection(cc, true, Connection.TRANSACTION_READ_COMMITTED);
			conn2 = db.getCachedConnection(cc, true, Connection.TRANSACTION_READ_COMMITTED);
			System.out.println("3 -> " + db);
			conn1.close();
			conn2.close();
			conn3.close();
			System.out.println("0 -> " + db);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

		db.cleanup();

		//	System.exit(0);
		System.out.println("--------------------------------------------------");

		System.out.println(DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED));

		System.out.println(DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED));
		System.out.println(DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED));
		System.out.println(DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED));
		System.out.println(DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED));
		System.out.println(DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED));

		System.out.println(db);


		try
		{
			System.out.println("-- Sleeping --");
			Thread.sleep(60000);
			System.out.println(db);
			db.close();
			db.cleanup();
			System.out.println(db);
		}
		catch (InterruptedException e)
		{
		}



		/**
		//	Connection option 1
		try
		{
			System.setProperty("oracle.jdbc.Trace", "true");
			DriverManager.registerDriver(new OracleDriver());
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@//dev:1521/dev", "compiere", "compiere");
			System.out.println("Catalog=" + con.getCatalog());
			DatabaseMetaData md = con.getMetaData();
			System.out.println("URL=" + md.getURL());
			System.out.println("User=" + md.getUserName());
			//
			System.out.println("Catalog");
			ResultSet rs = md.getCatalogs();
			while (rs.next())
				System.out.println("- " + rs.getString(1));
			//
			System.out.println("Table");
			rs = md.getTables(null, "COMPIERE", null, new String[] {"TABLE"});
			while (rs.next())
				System.out.println("- User=" + rs.getString(2) + " | Table=" + rs.getString(3)
					+ " | Type=" + rs.getString(4) + " | " + rs.getString(5));
			//
			System.out.println("Column");
			rs = md.getColumns(null, "COMPIERE", "C_ORDER", null);
			while (rs.next())
				System.out.println("- Tab=" + rs.getString(3) + " | Col=" + rs.getString(4)
					+ " | Type=" + rs.getString(5) + ", " + rs.getString(6)
					+ " | Size=" + rs.getString(7) + " | " + rs.getString(8)
					+ " | Digits=" + rs.getString(9) + " | Radix=" + rs.getString(10)
					+ " | Null=" + rs.getString(11) + " | Rem=" + rs.getString(12)
					+ " | Def=" + rs.getString(13) + " | " + rs.getString(14)
					+ " | " + rs.getString(15) + " | " + rs.getString(16)
					+ " | Ord=" + rs.getString(17) + " | Null=" + rs.getString(18)
					);

			con.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		 **/
	}	//	main

}   //  DB_Oracle
