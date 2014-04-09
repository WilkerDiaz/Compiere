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
package org.compiere.install;

import java.net.*;
import java.sql.*;
import org.compiere.db.*;
import org.compiere.startup.*;


/**
 *	PostgreSQL Configuration
 *	
 *  @author Jorg Janke
 *  @version $Id: ConfigPostgreSQL.java 7904 2009-07-20 22:22:01Z freyes $
 */
public class ConfigPostgreSQL extends Config
{
	/**
	 * 	Config PostgreSQL
	 * 	@param data configuration
	 */
	public ConfigPostgreSQL (ConfigurationData data)
	{
		super (data);
	}	//	ConfigPostgreSQL
	
	/**	Oracle Driver			*/
	private static com.edb.Driver s_pgDriver = null;
	/** Last Connection			*/
	private Connection			m_con = null;
	
	/**
	 * 	Init
	 */
	@Override
	public void init()
	{
		p_data.setDatabasePort(String.valueOf(DB_PostgreSQL.DEFAULT_PORT));
		p_data.setDatabaseSystemPassword(true);
		// Database Discovered
		p_data.setDatabaseDiscovered(false);
		// Database name
		p_data.setDatabaseName(true);
		// Database User
		p_data.setDatabaseUser(true);
	}	//	init
	
	/**
	 * 	Discover Databases.
	 * 	To be overwritten by database configs
	 *	@param selected selected database
	 *	@return array of databases
	 */
	@Override
	public String[] discoverDatabases(String selected)
	{
		return new String[] {DB_PostgreSQL.DEFAULT_DBNAME};
	}	//	discoverDatabases
	
	/**************************************************************************
	 * 	Test
	 *	@return error message or null if OK
	 */
	@Override
	public String test()
	{
		//	Database Server
		String server = p_data.getDatabaseServer();
		boolean pass = server != null && server.length() > 0
			&& server.toLowerCase().indexOf("localhost") == -1 
			&& !server.equals("127.0.0.1");
		String error = "Not correct: DB Server = " + server;
		InetAddress databaseServer = null;
		try
		{
			if (pass)
				databaseServer = InetAddress.getByName(server);
		}
		catch (Exception e)
		{
			error += " - " + e.getMessage();
			pass = false;
		}
		
		if((getPanel())!=null)
			signalOK(getPanel().okDatabaseServer, "ErrorDatabaseServer", 
					pass, true, error);
		if (!pass)
		{
			log.info("NOT OK: Database Server = " + databaseServer);
			return error;
		}
		log.info("OK: Database Server = " + databaseServer);
		setProperty(Environment.COMPIERE_DB_SERVER, databaseServer.getHostName());
		setProperty(Environment.COMPIERE_DB_TYPE, Environment.DBTYPE_PG);
		setProperty(Environment.COMPIERE_DB_PATH, Environment.DBTYPE_PG);

		//	Database Port
		int databasePort = p_data.getDatabasePort();
		pass = p_data.testPort (databaseServer, databasePort, true);
		error = "DB Server Port = " + databasePort; 
		if((getPanel())!=null)
			signalOK(getPanel().okDatabaseServer, "ErrorDatabasePort",
					pass, true, error);
		if (!pass)
			return error;
		log.info("OK: Database Port = " + databasePort);
		setProperty(Environment.COMPIERE_DB_PORT, String.valueOf(databasePort));


		//	JDBC Database Info
		String databaseName = p_data.getDatabaseName();	//	Service Name
		String systemPassword = p_data.getDatabaseSystemPassword();
		pass = systemPassword != null && systemPassword.length() > 0;
		error = "No Database System Password entered";
		
		if((getPanel())!=null)
			signalOK(getPanel().okDatabaseSystem, "ErrorJDBC",
					pass, true,	error);
		if (!pass)
			return error;
		//
		//	URL (derived)	jdbc:edb://prod1:5444/prod1
		String url = "jdbc:edb://" + databaseServer.getHostName()
			+ ":" + databasePort + "/";
		String url1 = url + "edb";
		pass = testJDBC(url1, "enterprisedb", systemPassword);
		error = "Error connecting: " + url1 
			+ " - as enterprisedb/" + systemPassword;
		
		if((getPanel())!=null)
			signalOK(getPanel().okDatabaseSystem, "ErrorJDBC",
					pass, true, error);
		if (!pass)
			return error;
		log.info("OK: Connection = " + url1);
		log.info("OK: Database System User " + databaseName);
		setProperty(Environment.COMPIERE_DB_SYSTEM, systemPassword);


		//	Database User Info
		String databaseUser = p_data.getDatabaseUser();	//	UID
		if (databaseUser.equalsIgnoreCase("EnterpriseDB")){
			error="The database user must be different from EnterpriseDB";
			signalOK(getPanel().okDatabaseUser, "ErrorJDBC", false, true, error);
			return error;
		}
		String databasePassword = p_data.getDatabasePassword();	//	PWD
		pass = databasePassword != null && databasePassword.length() > 0;
		error = "Invalid Database User Password";
		
		if((getPanel())!=null)
			signalOK(getPanel().okDatabaseUser, "ErrorJDBC",
					pass, true, error); 
		if (!pass)
			return error;
		//	Ignore result as it might not be imported
		String url2 = url + databaseName;
		pass = testJDBC(url2, databaseUser, databasePassword);
		error = "Cannot connect to " + databaseName
			+ " User: " + databaseUser + "/" + databasePassword 
			+ " - Database may not be imported yet (OK on initial run).";
		
		if((getPanel())!=null)
			signalOK(getPanel().okDatabaseUser, "ErrorJDBC",
					pass, false, error);
		if (pass)
		{
			log.info("OK: Connection = " + url2);
			log.info("OK: Database User = " + databaseUser);
			if (m_con != null)
				setProperty(ConfigurationData.COMPIERE_WEBSTORES, getWebStores(m_con));
			setProperty(Environment.COMPIERE_DB_USER_EXISTS, "Y");
		}
		else
		{
			setProperty(Environment.COMPIERE_DB_USER_EXISTS, "N");
			log.warning(error);
		}
		
		setProperty(Environment.COMPIERE_DB_URL, url2);
		setProperty(Environment.COMPIERE_DB_NAME, databaseName);
		setProperty(Environment.COMPIERE_DB_USER, databaseUser);
		setProperty(Environment.COMPIERE_DB_PASSWORD, databasePassword);
		
		//
		if (m_con != null) {
			try {
				m_con.close();
				m_con = null;
			}
			catch (Exception e){
				log.warning("Cannot close connection: " + e);
			}
		}
		
		return null;
	}	//	test


	/**
	 * 	Test JDBC Connection to Server
	 * 	@param url connection string
	 *  @param uid user id
	 *  @param pwd password
	 * 	@return true if OK
	 */
	private boolean testJDBC (String url, String uid, String pwd)
	{
		log.fine("Url=" + url + ", UID=" + uid);
		try
		{
			if (s_pgDriver == null)
			{
				s_pgDriver = new com.edb.Driver();
				DriverManager.registerDriver(s_pgDriver);
			}
			m_con = DriverManager.getConnection(url, uid, pwd);
		}
		catch (Exception e)
		{
			log.warning(e.toString());
			return false;
		}
		return true;
	}	//	testJDBC
}	//	ConfigPostgreSQL
