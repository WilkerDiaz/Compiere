/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.compiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@compiere.org 
 *****************************************************************************/
package org.compiere.install;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;

import oracle.jdbc.*;
import org.compiere.startup.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Create Database
 *	
 *  @author Jorg Janke
 *  @version $Id: DatabaseCreate.java 7904 2009-07-20 22:22:01Z freyes $
 */
public class DatabaseCreate implements Runnable
{
	/**
	 * 	Create Oracle Database
	 *	@param con connection to system
	 *	@param dbtype database type e.g. Oracle, PostgreSQL
	 *	@param dbname database name e.g. compiere, for Oracle it is the same as compiereUID
	 *	@param compiereUID user id e.g. compiere
	 *	@param compierePWD user password e.g. compiere
	 *	@param monitor publish progress and details via publish method
	 *	@param existingDatabase if true the DB needs to be dropped first
	 */
	public DatabaseCreate(Connection con, 
			String dbtype, String dbname,
			String compiereUID, String compierePWD,
			PublishInterface monitor, boolean existingDatabase) throws Exception
	{
		if (monitor == null)
			throw new IllegalArgumentException("SetupProgress monitor is NULL.");
		else
			m_monitor = monitor;
		
		if (con == null)
			throw new IllegalArgumentException("Database connection is NULL");
		else
			m_con = con;
		
		if (dbtype == null)
			throw new IllegalArgumentException("Database type is NULL");
		else
			m_dbType = dbtype;
		
		if (dbname == null)
			throw new IllegalArgumentException("Database name is NULL");
		else
			m_dbName = dbname;
		
		if (compiereUID == null)
			throw new IllegalArgumentException("Database user ID compiereUID is NULL");
		else
			m_cUID = compiereUID;		
		
		if (compierePWD == null)
			throw new IllegalArgumentException("Database user password compierePWD is NULL");
		else
			m_cPWD = compierePWD;		
		m_existingDatabase = existingDatabase;
	}	//	DatabaseCreate

	public DatabaseCreate(Connection con, 
			String dbtype, String dbname,
			String compiereUID, String compierePWD, 
			CLogger log, boolean existingDatabase) throws Exception
	{	
		if(log == null)
			throw new IllegalArgumentException("Logger is NULL");
		else
			m_log = log;
			
		if (con == null)
			throw new IllegalArgumentException("Database connection is NULL");
		else
			m_con = con;
		
		if (dbtype == null)
			throw new IllegalArgumentException("Database type is NULL");
		else
			m_dbType = dbtype;
		
		if (dbname == null)
			throw new IllegalArgumentException("Database name is NULL");
		else
			m_dbName = dbname;
		
		if (compiereUID == null)
			throw new IllegalArgumentException("Database user ID compiereUID is NULL");
		else
			m_cUID = compiereUID;		
		
		if (compierePWD == null)
			throw new IllegalArgumentException("Database user password compierePWD is NULL");
		else
			m_cPWD = compierePWD;		
		m_existingDatabase = existingDatabase;
	}	//	DatabaseCreate
	
	private int 				m_totalSteps = 1;
	private int 				m_completedSteps = 0;

	private String 				m_dbType = null;
	private String 				m_dbName = null;
	private Connection 			m_con = null;
	private String 				m_cUID = null;
	private String 				m_cPWD = null;
	private PublishInterface	m_monitor = null;
	private CLogger				m_log = null;
	private boolean				m_existingDatabase = false;
	private boolean 			m_success = false;

	
	/**
	 * 	Create Database
	 */
	public void run()
	{
		String[] drops = getSQLs(false);
		String[] creates = getSQLs(true);
		//
		m_totalSteps = creates.length + 1;
		m_completedSteps = 0;
		if (m_existingDatabase)
		{
			m_totalSteps += drops.length;
			if(m_monitor != null)
				m_monitor.publish(Level.INFO, "Dropping Database", m_completedSteps+1, m_totalSteps);
			else if(m_log != null)
				m_log.info("Dropping Database");
			m_success = execute(drops);
			m_completedSteps = drops.length;
		}
		else 
			m_success = true;
		
		if (m_success)
		{
			if(m_monitor != null)
				m_monitor.publish(Level.INFO, "Creating Database", m_completedSteps+1, m_totalSteps);
			else if(m_log != null)
				m_log.info("Creating Database");			
			m_success = false;	//	 in case execute crashes
			m_success = execute(creates);
		}
	}	//	run


	/**
	 * 	Get SQL commands
	 *	@param create true if create statements
	 *	@return
	 */
	private String[] getSQLs (boolean create)
	{
		if (Environment.DBTYPE_ORACLE.equals (m_dbType) || Environment.DBTYPE_ORACLEXE.equals (m_dbType))
		{
			if (!create){
				// get BVL users
				Statement stmt = null;
				ArrayList<String> users = new ArrayList<String>();
				try {
					stmt = m_con.createStatement();
					stmt.execute("SELECT COUNT(1) FROM All_Tables " +
								 "WHERE owner=UPPER('"+m_cUID+"') AND table_name='AD_EUL_USER'");
					ResultSet rs = stmt.getResultSet();
					if (rs.next() && rs.getInt(1)>0) {
						stmt.execute("SELECT name FROM "+m_cUID+".AD_EUL_User");
						ResultSet eulUsers = stmt.getResultSet();
						while (eulUsers.next()){
							String eulUser = eulUsers.getString(1);
							if (eulUser!=null && eulUser.length()>0)
								users.add(eulUser);
						}
						eulUsers.close();
					}
					rs.close();
				} catch (Exception ex) {
					if(m_monitor != null)
						m_monitor.publish(Level.SEVERE, "Cannot retrieve EUL users " + ex.getMessage(), m_totalSteps, m_totalSteps);
					else if(m_log != null)
						m_log.severe("Cannot retrieve EUL users " + ex.getMessage());
					return null;
				}
				try {
					stmt = m_con.createStatement();
					stmt.execute("SELECT COUNT(1) FROM All_Tables " +
								 "WHERE owner=UPPER('"+m_cUID+"') AND table_name='AD_EUL_SETUP'");
					ResultSet rs = stmt.getResultSet();
					if (rs.next() && rs.getInt(1)>0){
						stmt.execute("SELECT name FROM "+m_cUID+".AD_EUL_Setup");
						ResultSet eulUsers = stmt.getResultSet();
						if (eulUsers.next()){
							String eulUser = eulUsers.getString(1);
							if (eulUser!=null && eulUser.length()>0)
								users.add(eulUser);
						}
						eulUsers.close();
					}
					rs.close();			
				} catch (Exception ex) {
					if(m_monitor != null)
						m_monitor.publish(Level.SEVERE, "Cannot retrieve EUL setup " + ex.getMessage(), m_totalSteps, m_totalSteps);
					else 
						if(m_log != null)
							m_log.severe("Cannot retrieve EUL setup " + ex.getMessage());
					return null;
				}
				try
				{
					if (stmt!=null)
						stmt.close();
					stmt=null;
				} 
				catch (Exception e)
				{			
					stmt=null;
				}
				users.add(m_cUID);
				String dropMessage = "Database users to be dropped:";
				String[] returnValue = new String[users.size()];				
				for (int i=0; i<users.size();i++){
					returnValue[i]="DROP USER " + users.get(i) + " CASCADE";
					dropMessage = dropMessage + " " + users.get(i);
				}
				if(m_monitor != null)
					m_monitor.publish(Level.INFO, dropMessage, m_totalSteps, m_totalSteps);
				else if(m_log != null)
					m_log.info(dropMessage);	
				return returnValue;
			}
			else
				return new String[] 
				    {"CREATE USER " + m_cUID + " IDENTIFIED BY " + m_cPWD + 
				    	" DEFAULT TABLESPACE USERS TEMPORARY TABLESPACE TEMP PROFILE DEFAULT ACCOUNT UNLOCK",
				    "GRANT CONNECT, DBA, RESOURCE TO "+ m_cUID,
				    "GRANT UNLIMITED TABLESPACE TO " + m_cUID,
				    "ALTER USER " + m_cUID + " DEFAULT ROLE CONNECT, RESOURCE, DBA"
				    };
		}
		else if (Environment.DBTYPE_PG.equals (m_dbType))
		{
			if (!create)
				return new String[] 
				    {"DROP DATABASE " + m_dbName,
					"DROP ROLE " + m_cUID + " CASCADE"
				    };
			else
				return new String[] 
		            {"CREATE ROLE " + m_cUID + " LOGIN SUPERUSER INHERIT CREATEDB CREATEROLE",
					"ALTER USER " + m_cUID + " IDENTIFIED BY " + m_cPWD, 
					"CREATE DATABASE " + m_dbName + " ENCODING = 'UNICODE' TABLESPACE = pg_default",
				//	"\\c " + m_dbName,
					"CREATE SCHEMA " + m_cUID + " AUTHORIZATION " + m_cUID
		            };
		}
		else if (Environment.DBTYPE_MS.equals (m_dbType))
		{
			if (!create)
				return new String[] 
				    {"USE MASTER",
					"DROP DATABASE " + m_dbName,
					"DROP USER " + m_cUID,
					"DROP LOGIN " + m_cUID
					//"DROP SCHEMA " + m_cUID 
				    };
			else
				return new String[] 
		            {"USE MASTER",
					"DROP DATABASE " + m_dbName,
					"CREATE DATABASE " + m_dbName,
					"USE "  + m_dbName,
					"DROP LOGIN " + m_cUID,
					"CREATE LOGIN "  + m_cUID + " WITH PASSWORD = '" + m_cPWD + "' , DEFAULT_DATABASE = " + m_dbName,
					"CREATE USER " + m_cUID + " WITH DEFAULT_SCHEMA = "  + m_cUID,
					"CREATE SCHEMA " + m_cUID + " AUTHORIZATION " + m_cUID,
					"GRANT TAKE OWNERSHIP, CREATE DEFAULT, CREATE FUNCTION, CREATE PROCEDURE, CREATE RULE, CREATE TABLE, CREATE VIEW, CREATE SYNONYM ON DATABASE::" 
						+ m_dbName + " TO " + m_cUID,
		            "GRANT TAKE OWNERSHIP, ALTER, EXECUTE, INSERT, UPDATE, DELETE, SELECT, REFERENCES, VIEW DEFINITION ON DATABASE::"  + m_dbName + " TO "  + m_cUID,
		            "GRANT EXECUTE, INSERT, UPDATE, DELETE, SELECT, REFERENCES, VIEW DEFINITION ON SCHEMA::dbo TO "  + m_cUID,
		            "CREATE TABLE " +  m_cUID + ".C_NOTNULL (VIP_COL VARCHAR(28) NOT NULL)",
		            "INSERT INTO " +  m_cUID + ".C_NOTNULL VALUES ('DO NOT CHANGE ANYTHING HERE!')"
	            };
		}
		throw new IllegalArgumentException("Database Type not supported: " + m_dbType);
	}	//	getSQLs
	
	/**
	 * 	Execute statements
	 *	@param sqls array of commands
	 *	@return true if success
	 */
	private boolean execute (String[] sqls)
	{
		// Create the statement
		Statement stmt = null;
		//boolean notDroppedRole = false;
		try
		{
			stmt = m_con.createStatement();
		}
		catch (Exception ex)
		{
			if(m_monitor != null)
				m_monitor.publish(Level.SEVERE, "Statement: " + ex.getMessage(), m_totalSteps, m_totalSteps);
			else if (m_log != null)
				m_log.severe("Statement: " + ex.getMessage());
			return false;
		}

		for (int i= 0; i < sqls.length; i++)
		{
			String sql = sqls[i];
			try
			{
				if (Environment.DBTYPE_PG.equals (m_dbType) && sql.startsWith("CREATE SCHEMA"))
				{
					//switch a database in the connection
					String url = m_con.getMetaData().getURL();
					if (url.indexOf("mgmtsvr") > 0)
						url=url.replaceAll("mgmtsvr", m_dbName);
					else if (url.indexOf("edb")>0)
						url=url.replaceAll("edb", m_dbName);
					com.edb.Driver s_driver = new com.edb.Driver();
					DriverManager.registerDriver (s_driver);
					try
					{
						if (m_con != null)
							m_con.close();
						if (stmt != null)
							stmt.close();
					}
					catch (Exception e)
					{
					}
					m_con = DriverManager.getConnection (url, m_cUID, m_cPWD);
					stmt = m_con.createStatement();
				}	//	EDB Create

				if(m_monitor != null)
					m_monitor.publish(Level.FINE, "SQL: " + sql, m_completedSteps+i+1, m_totalSteps);
				else if (m_log != null)
					m_log.fine("SQL: " + sql);
				stmt.executeUpdate(sql);
				
				if (Environment.DBTYPE_MS.equals (m_dbType) && sql.startsWith("CREATE DATABASE "))
				{
					//ALTER DATABASE compiere COLLATE SQL_Latin1_General_CP1_CS_AS;
					String collate = null;
					try
					{
						ResultSet rs = stmt.executeQuery("SELECT collation_name FROM master.sys.databases WHERE Name='" +  m_dbName + "'");
;
						if (rs.next())
							collate = rs.getString(1);
						else
						{
							if(m_monitor != null)
								m_monitor.publish(Level.SEVERE, "Could not get COLLATENAME from database"  +  m_dbName, m_completedSteps+i+1, m_totalSteps);
							else if (m_log != null)
								m_log.severe("Could not get COLLATENAME from database"  +  m_dbName);
						}
						rs.close();
					}
					catch (Exception e)
					{
						
						if(m_monitor != null)
							m_monitor.publish(Level.SEVERE, "SELECT collation_name FROM master.sys.databases WHERE Name='" +  m_dbName + "'  " + e.getMessage(), m_completedSteps+i+1, m_totalSteps);
						else if (m_log != null)
								m_log.severe( "SELECT collation_name FROM master.sys.databases WHERE Name='" +  m_dbName + "'  " + e.getMessage());	
					}
					
					if (collate != null)
					{
						//ALTER DATABASE compiere COLLATE SQL_Latin1_General_Cp850_BIN2;
						collate = "SQL_Latin1_General_Cp850_BIN2";
						//collate = collate.replace("_CI_", "_CS_");
						sql = "ALTER DATABASE " +  m_dbName + " COLLATE " + collate;
						if(m_monitor != null)
							m_monitor.publish(Level.FINE, "SQL: " + sql, m_completedSteps+i+1, m_totalSteps);
						else if (m_log != null)
								m_log.fine( "SQL: " + sql);					
						stmt.executeUpdate(sql);
					}					
					else
					{
						if(m_monitor != null)
							m_monitor.publish(Level.SEVERE, "COLLATENAME is NULL from database"  +  m_dbName, m_completedSteps+i+1, m_totalSteps);
						else if (m_log != null)
							m_log.severe("COLLATENAME is NULL from database"  +  m_dbName);	
					}
				}//MS Create DB
			}
			catch (Exception ex)
			{
				if (Environment.DBTYPE_ORACLE.equals (m_dbType) || Environment.DBTYPE_ORACLEXE.equals (m_dbType))
				{
					if (sql.startsWith("DROP USER ")&&!sql.equals("DROP USER "+m_cUID+" CASCADE")){
						if(m_monitor != null)
							m_monitor.publish(Level.INFO, "Check if the user have been dropped. If so, ignore the error from executing SQL: " + sql + "\n " + ex.getMessage(), m_totalSteps, m_totalSteps);
						else if(m_log != null)
							m_log.info("Check if the user have been dropped. If so, ignore the error from executing SQL: " + sql + "\n " + ex.getMessage());
					}					
					else
					{
						if(m_monitor != null)
							m_monitor.publish(Level.SEVERE, sql + "\n " + ex.getMessage(), m_totalSteps, m_totalSteps);
						else if(m_log != null)
							m_log.severe(sql + "\n " + ex.getMessage());
						return false;
					}
				}
				else if (Environment.DBTYPE_PG.equals (m_dbType))
				{
					if (sql.startsWith("DROP ROLE"))
					{
						if(m_monitor != null)
							m_monitor.publish(Level.INFO, "Ignore the error from executing SQL: " + sql + "\n " + ex.getMessage(), m_totalSteps, m_totalSteps);
						else if(m_log != null)
							m_log.info("Ignore the error from executing SQL: " + sql + "\n " + ex.getMessage());
						//notDroppedRole = true;
					}
					else if (sql.startsWith("CREATE ROLE"))
					{
						if(m_monitor != null)
							m_monitor.publish(Level.INFO, "Ignore the error from executing SQL: " + sql + "\n " + ex.getMessage(), m_totalSteps, m_totalSteps);
						else if(m_log != null)
							m_log.info("Ignore the error from executing SQL: " + sql + "\n " + ex.getMessage());
					}
					else
					{
						if(m_monitor != null)
							m_monitor.publish(Level.SEVERE, sql + "\n " + ex.getMessage(), m_totalSteps, m_totalSteps);
						else if(m_log != null)
							m_log.severe(sql + "\n " + ex.getMessage());
						return false;
					}
				}//PG
				else if (Environment.DBTYPE_MS.equals (m_dbType))
				{
					if (sql.startsWith("DROP DATABASE") || 
							sql.startsWith("DROP USER") || 
							sql.startsWith("DROP LOGIN") ||
							sql.startsWith("DROP SCHEMA"))
					{
						if(m_monitor != null)
							m_monitor.publish(Level.INFO, "Please check if the Object exists or not. If yes, ignore the error from executing SQL: " + sql + "\n " + ex.getMessage(), m_totalSteps, m_totalSteps);
						else if(m_log != null)
							m_log.info("Please check if the Object exists or not. If yes, ignore the error from executing SQL: ");
						//notDroppedRole = true;
					}
					else
					{
						if(m_monitor != null)
							m_monitor.publish(Level.SEVERE, sql + "\n " + ex.getMessage(), m_totalSteps, m_totalSteps);
						else if(m_log != null)
							m_log.severe(sql + "\n " + ex.getMessage());
						return false;
					}
				}//MS
				else
				{
					if(m_monitor != null)
						m_monitor.publish(Level.SEVERE, sql + "\n " + ex.getMessage(), m_totalSteps, m_totalSteps);
					else if(m_log != null)
							m_log.severe(sql + "\n " + ex.getMessage());
						
					return false;
				}
			}	//	catch
		}	//	for all statements

		if (stmt != null)
		{
			try
			{
				stmt.close();
			}
			catch (Exception e)
			{				
			}
		}
		return true;
	}	//	execute
	
	
	/**
	 * 	Success
	 *	@return true if success
	 */
	public boolean isSuccess()
	{
		return m_success;
	}	//	isSuccess
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		ProgressPanel sp = new ProgressPanel();
		try
		{
			Connection con = null;
			if (Environment.DBTYPE_ORACLE.equals(args[1]))
			{
				//Class.forName("oracle.jdbc.OracleDriver");
				OracleDriver s_driver = new OracleDriver();
				DriverManager.registerDriver (s_driver);
				con = DriverManager.getConnection (args[0], 
						"system", "compiere");
			}
			else
			{
				//Class.forName("com.edb.Driver");
				com.edb.Driver s_driver = new com.edb.Driver();
				DriverManager.registerDriver (s_driver);
				con = DriverManager.getConnection (args[0], 
						"enterprisedb", "compiere");
			}
			DatabaseCreate dbc = new DatabaseCreate(con, args[1], "compiere", "compiere", "compiere", sp, false);
			dbc.run();
			//dbc = new DatabaseCreate(con, args[1], "compiere", "compiere", "compiere", sp, true);
			//dbc.run();
			if (con!=null)
				con.close();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}  // main()
	
}	//	DatabaseCreate
