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
import java.util.logging.*;

import javax.sql.*;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.startup.*;
import org.compiere.util.*;

import com.edb.ds.*;


/**
 *	PostfreSQL Database Port
 *
 *  @author Jorg Janke
 *  @version $Id: DB_PostgreSQL.java 9163 2010-08-04 08:40:36Z ragrawal $
 */
public class DB_PostgreSQL implements CompiereDatabase
{
	/**
	 *  PostgreSQL Database
	 */
	public DB_PostgreSQL()
	{
		/**	Causes VPN problems ??? */
		try
		{
			getDriver();
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, e.getMessage());
		}
		/** **/
	}   //  DB_PostgreSQL

	/** Static Driver           	*/
	private static com.edb.Driver	s_driver = null;
	/** Driver Class Name			*/
	public static final String		DRIVER = "com.edb.Driver";

	/** Default Port            	*/
	public static final int 		DEFAULT_PORT = 5444;

	/** Default database name          	*/
	public static final String 		DEFAULT_DBNAME = "compiere";

	/** Default database name          	*/
	public static final String 		DEFAULT_USER_NAME = "compiere";

	/** Statement Converter 	    */
	private final Convert			         m_convert = new Convert(Environment.DBTYPE_PG);

	/** Connection String       	*/
	private String          		m_connectionURL;

	/** Data Source					*/
	private volatile PGPoolingDataSource m_ds = null;

    /** Cached User Name			*/
    private String					m_userName = null;

    /**	Logger			*/
	private static CLogger			log	= CLogger.getCLogger (DB_PostgreSQL.class);



	/**
	 *  Get Database Name
	 *  @return database short name
	 */
	@Override
	public String getName()
	{
		return Environment.DBTYPE_PG;
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
	public com.edb.Driver getDriver() throws SQLException
	{
		if (s_driver == null)
		{
			s_driver = new com.edb.Driver();
			DriverManager.registerDriver (s_driver);
			DriverManager.setLoginTimeout (Database.CONNECTION_TIMEOUT);
		}
		return s_driver;
	}   //  getDriver

	/**
	 *  Get Database Connection String.
	 *  <pre>
	 *  Timing:
	 *  - direct    = ? sec  (no real difference if on other box)
	 *  </pre>
	 *  @param connection Connection Descriptor
	 *  @return connection String
	 */
	@Override
	public String getConnectionURL (CConnection connection)
	{
		StringBuffer sb = new StringBuffer ("jdbc:edb://")
			.append(connection.getDbHost())
			.append(":").append(connection.getDbPort())
			.append("/").append(connection.getDbName());
		m_connectionURL = sb.toString();
		log.config(m_connectionURL);
		//
		m_userName = connection.getDbUid();
		return m_connectionURL;
	}   //  getConnectionURL

	/**
	 * 	Get Connection URL.
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
		m_connectionURL = "jdbc:edb://" + dbHost + ":" + dbPort
			+ "/" + dbName;
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
			return m_userName;
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
		StringBuffer sb = new StringBuffer("DB_EnterpriseDB[");
		sb.append(m_connectionURL);
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
		StringBuffer sb = new StringBuffer("---");
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
		//replace all tabs with spaces and combine multiple spaces to single space
		oraStatement=oraStatement.replaceAll("\\t+", " ");
		oraStatement = oraStatement.replaceAll(" +", " ");
		oraStatement = oraStatement.trim();
		
		if (oraStatement.startsWith("ALTER TABLE") && (oraStatement.indexOf(" MODIFY ")>0))
		{
			String tokens[] = oraStatement.split(" ");
			String sql = "ALTER TABLE " + tokens[2] + " ALTER " + tokens[4];
			int idef = oraStatement.indexOf(" DEFAULT ");
			if ((idef>0) || (oraStatement.indexOf(" NULL")<0))
			{
				int i = sql.length() + 1; //alter v.s. modify
				if (idef > 0)
				{
					if (!("DEFAULT".equals(tokens[5])||(tokens[5].length()==0)))
					{
						sql += " TYPE " + oraStatement.substring(i, idef+1); //type stuff
						sql += ", ALTER " + tokens[4];
					}
					sql += " SET DEFAULT " + oraStatement.substring(idef + 9, oraStatement.length());
				}
				else
				{
					int rpDrop = 0;
					if ((oraStatement.indexOf(" MODIFY (")>0) || (oraStatement.indexOf(" MODIFY(")>0))
						rpDrop = 1;
					sql += " TYPE " + oraStatement.substring(i, oraStatement.length()-rpDrop);
				}
				oraStatement = sql;
			}
			else
			{
				if (oraStatement.indexOf(" NOT NULL")>0)
				{
					sql += " SET NOT NULL";
					return sql;
				}
				else if (oraStatement.indexOf(" NULL")>0)
				{
					sql += " DROP NOT NULL";
					return sql;
				}
			}
		}

		if (oraStatement.startsWith("CREATE UNIQUE INDEX ") && (oraStatement.indexOf("TO_NCHAR(AD_User_ID)")>0)) //jz hack number pad
		{
				oraStatement = oraStatement.replace("TO_NCHAR(AD_User_ID)", "TO_CHAR(AD_User_ID,'9999999')::VARCHAR");
		}

		if (oraStatement.startsWith("ALTER TABLE") && (oraStatement.indexOf(" ADD (")>0)) //jz remove () for add
		{
				oraStatement = oraStatement.replace(" ADD (", " ADD ");
				oraStatement = oraStatement.substring(0, oraStatement.length()-1);
		}

		if (oraStatement.startsWith("UPDATE"))
        {
                oraStatement = DBUtils.updateSetSelectList(oraStatement);
        }
		
		if (oraStatement.startsWith("DELETE FROM ")) //jz use tablename to replace co-relation id
		{
			String tokens[] = oraStatement.split(" ");
			if ((tokens.length>3) && !"WHERE".equals(tokens[3]))
			{
				String[] sep ={" ", "=", ">", "<", "(", ","};
				String crid = tokens[3]+".";
				String ncrid = tokens[2]+".";

				oraStatement = oraStatement.replace(" "+tokens[3]+" ", " ");
				for (String element : sep) {
					String crid1 = element+crid;
					String ncrid1 = element+ncrid;
					while (oraStatement.indexOf(crid1)>-1)
						oraStatement = oraStatement.replace(crid1, ncrid1);
				}
			}
		}

		//jz Postgre is sensitive between Numeric and Integer
		while (oraStatement.indexOf("NUMBER(10,0)")>-1)
			oraStatement = oraStatement.replace("NUMBER(10,0)", "INTEGER");
		while (oraStatement.indexOf("NUMBER(10)")>-1)
			oraStatement = oraStatement.replace("NUMBER(10)", "INTEGER");
		while (oraStatement.indexOf("NUMERIC(10,0)")>-1)
			oraStatement = oraStatement.replace("NUMERIC(10,0)", "INTEGER");
		while (oraStatement.indexOf("NUMERIC(10)")>-1)
			oraStatement = oraStatement.replace("NUMERIC(10)", "INTEGER");

		String retValue[] = m_convert.convert(oraStatement);
		/*//jz
		if (retValue == null)
			throw new IllegalArgumentException
				("DB_PostgreSQL - Not Converted (" + oraStatement + ") - "
					+ m_convert.getConversionError());
		if (retValue.length != 1)
			throw new IllegalArgumentException
				("DB_PostgreSQL - Convert Command Number=" + retValue.length
					+ " (" + oraStatement + ") - " + m_convert.getConversionError());
					*/
		if (retValue == null)
		{
			log.warning("Not Converted (" + oraStatement + ") - "
					+ m_convert.getConversionError());
			return oraStatement;
		}
		if (retValue.length != 1)
		{
			log.warning("Convert error! Converted statement Number=" + retValue.length
					+ " (" + oraStatement + ") - " + m_convert.getConversionError());
			return oraStatement;
		}
		//  Diagnostics (show changed, but not if AD_Error
		if (CLogMgt.isLevelFinest()
			&& !oraStatement.equals(retValue[0])
			&& (retValue[0].indexOf("AD_Error") == -1))
		{
			String statement1 = Util.cleanWhitespace (retValue[0]);
			String statement2 = Util.cleanWhitespace (oraStatement);
			System.out.println("PostgreSQL =>" + statement1 + "<=\n"
							+  "           =<" + statement2 + ">=");
		}
		//
		return retValue[0];
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
		if (IXName.toUpperCase().endsWith("_KEY"))
			return "1"+IXName;
		else
			return "0";
	}

	/**
	 *  Get Name of System User
	 *  @return system
	 */
	@Override
	public String getSystemUser()
	{
		return "enterprisedb";	//	sa
	}	//	getSystemUser

	/**
	 *  Get Name of System Database
	 *  @param databaseName database Name
	 *  @return e.g. master or database Name
	 */
	@Override
	public String getSystemDatabase(String databaseName)
	{
		return databaseName;	//	edb
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
							.append(" ELSE TRIM(TO_CHAR(").append(columnName).append(")) END) ");
				return retValue.toString();
				
			}
			//jz no TM9 format to EDB
			//else
			//	retValue.append(",'TM9'");
			//  TO_CHAR(GrandTotal,'9G999G990D00','NLS_NUMERIC_CHARACTERS='',.''')
			//if (!Language.isDecimalPoint(AD_Language))      //jz EDB not supported  reversed
			//	retValue.append(",'NLS_NUMERIC_CHARACTERS='',.'''");
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
	 * Create DataSource
	 * 
	 * @param connection
	 *            connection
	 * @return data dource
	 */
	@Override
	public DataSource getDataSource(CConnection connection) {
		if (m_ds == null) {
			try {
				PGPoolingDataSource ds = new PGPoolingDataSource();
				ds.setServerName(connection.getDbHost());
				ds.setDatabaseName(connection.getDbName());
				ds.setPortNumber(connection.getDbPort());
				ds.setUser(connection.getDbUid());
				ds.setPassword(connection.getDbPwd());
				//
				ds.setDataSourceName("CompiereDS");
				//
				if (Ini.isClient()) {
					ds.setInitialConnections(1);
					ds.setMaxConnections(6);
				} else // Server Settings
				{
					ds.setInitialConnections(1);
					// m_ds.setMaxConnections(50);
				}
				//
				log.config(toString());
				//
				m_ds = ds;
			} catch (Exception e) {
				log.log(Level.WARNING, toString(), e);
			}
		}
		return m_ds;
	} // getDataSource


	/**
	 * Get new Connection from cache
	 * 
	 * @param connection
	 *            info
	 * @param autoCommit
	 *            true if autocommit connection
	 * @param transactionIsolation
	 *            Connection transaction level
	 * @return connection or null
	 * @throws Exception
	 */
	@Override
	public Connection getCachedConnection (CConnection connection,
		boolean autoCommit, int transactionIsolation)
		throws SQLException
	{
		Connection conn = getDataSource(connection).getConnection();
		if (conn != null) {
			conn.rollback();
			if (conn.getTransactionIsolation() != transactionIsolation)
				conn.setTransactionIsolation(transactionIsolation);
			if (conn.getAutoCommit() != autoCommit)
				conn.setAutoCommit(autoCommit);
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
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		m_ds = null;
	}	//	close

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
			case DisplayTypeConstants.CostPrice:
			case DisplayTypeConstants.Quantity:
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
	 *  change update set (...) = (select ... from ) standard format
	 *  @param  sql update clause
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
		return null;
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
		return null;
	}  // getBlob()

	/**************************************************************************
	 * 	Testing
	 * 	@param args ignored
	 * @throws SQLException 
	 */
	public static void main (String[] args) throws SQLException
	{
		Ini.loadProperties (false);
		Ini.setProperty (Ini.P_CONNECTION, "");
		//
	//	DB_PostgreSQL pg = new DB_PostgreSQL();
		//
		Environment env = Environment.get();
		env.setHost(null);
		env.setProperty (Environment.COMPIERE_DB_TYPE, Environment.DBTYPE_PG);
		env.setProperty (Environment.COMPIERE_DB_PATH, Environment.DBTYPE_PG);
		env.setProperty (Environment.COMPIERE_DB_NAME, "compiere");
		env.setProperty (Environment.COMPIERE_DB_PORT, String.valueOf (DEFAULT_PORT));
		env.setProperty (Environment.COMPIERE_DB_USER, "compiere");
		env.setProperty (Environment.COMPIERE_DB_PASSWORD, "compiere");

		//
	//	Compiere.startupEnvironment(true);
		CConnection cc = CConnection.get();
		DB_PostgreSQL db = (DB_PostgreSQL)cc.getDatabase();
		DB.setDBTarget (cc);

		Connection connD = null;
		Connection connS = null;
		Connection connC = null;
		try
		{
			connD = db.getDriverConnection(cc);
			connD.setAutoCommit(false);
			System.out.println("Driver=" + connD);
			DataSource ds = db.getDataSource(cc);
			connS = ds.getConnection();
			System.out.println("DS=" + connS);
			connC = db.getCachedConnection(cc, true, Connection.TRANSACTION_READ_COMMITTED);
			System.out.println("Cached=" + connC);
			System.out.println(db);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

		for (int i = 0; i < 4; i++)
		{
			System.out.println ("----> " + i);
			String sql = "SELECT * FROM AD_System";
			int index = 1;
			//	Driver
			PreparedStatement psD = null;
			try
			{
				psD = connD.prepareStatement(sql);
				ResultSet rsD = psD.executeQuery();
				while (rsD.next())
				{
					rsD.getString(index);
				}
				rsD.close();
				psD.close();
				psD = null;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				if (psD != null)
				{
					connD.commit();
					psD.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			/**	DataSource	*
			PreparedStatement psS = null;
			try
			{
				psS = connS.prepareStatement(sql);
				ResultSet rsS = psS.executeQuery();
				while (rsS.next())
				{
					String s = rsS.getString(index);
				}
				rsS.close();
				psS.close();
				psS = null;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				if (psS != null)
					psS.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			//	Cached
			PreparedStatement psC = null;
			try
			{
				psC = connC.prepareStatement(sql);
				ResultSet rsC = psC.executeQuery();
				while (rsC.next())
				{
					String s = rsC.getString(index);
				}
				rsC.close();
				psC.close();
				psC = null;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				if (psC != null)
					psC.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			/** **/
		}	//	4 times




		System.out.println("--------------------------------------------------");
		/**
		DROP TABLE X_Test;
		CREATE TABLE X_Test
		(
		    Text1   NVARCHAR2(2000) NULL,
		    Text2   VARCHAR2(2000)  NULL
		);
		**
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
			/** **
			PreparedStatement pstmt = conn2.prepareStatement
				("INSERT INTO X_Test(Text1, Text2) values(?,?)");
			pstmt.setString(1, myString); // NVARCHAR2 column
			pstmt.setString(2, myString); // VARCHAR2 column
			System.out.println(pstmt.executeUpdate());

			Statement stmt = conn2.createStatement();
			System.out.println(stmt.executeUpdate
				("INSERT INTO X_Test(Text1, Text2) values('" + myString + "','" + myString + "')"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("--------------------------------------------------");
		System.exit(0);

		/** */
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

	//	System.exit(0);
		System.out.println("--------------------------------------------------");

		for (int i = 0; i < 1000; i++)
		{
			System.out.println(i + " - " + DB.createConnection(true, Connection.TRANSACTION_READ_COMMITTED));
		}




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

	@Override
	public String[] getJDBCCacheInfo() {		
		  String[] jdbcCacheMessage = new String[]{"",""};
		  return jdbcCacheMessage;
	}

}   //  DB_PostgreSQL
