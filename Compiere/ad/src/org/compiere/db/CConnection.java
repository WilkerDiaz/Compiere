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
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.naming.*;
import javax.sql.*;
import javax.swing.*;

import org.compiere.*;
import org.compiere.common.CompiereStateException;
import org.compiere.interfaces.*;
import org.compiere.startup.*;
import org.compiere.util.*;

/**
 *  Compiere Connection Descriptor
 *
 *  @author     Jorg Janke
 *  @version    $Id: CConnection.java 8926 2010-06-09 00:16:58Z rthng $
 */
public class CConnection implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/** Connection      */
	private static CConnection	s_cc = null;
	/** Logger			*/
	private static CLogger 		log = CLogger.getCLogger (CConnection.class);

	/** Connection profiles		*/
	public static ValueNamePair[] CONNECTIONProfiles = new ValueNamePair[]{
		new ValueNamePair("L", "LAN"),
		new ValueNamePair("T", "Terminal Server"),
		new ValueNamePair("V", "VPN"),
		new ValueNamePair("W", "WAN") };

	// Added WAS support - temporary that http tunneling won't work with oracle rowset
	public static ValueNamePair[] CONNECTIONProfilesWas = new ValueNamePair[]{
		new ValueNamePair("L", "LAN"),
		new ValueNamePair("T", "Terminal Server"),
		new ValueNamePair("V", "VPN") };

	/** Connection Profile LAN			*/
	public static final String	PROFILE_LAN = "L";
	/** Connection Profile Terminal Server	*/
	public static final String	PROFILE_TERMINAL = "T";
	/** Connection Profile VPM			*/
	public static final String	PROFILE_VPN = "V";
	/** Connection Profile WAN			*/
	public static final String	PROFILE_WAN = "W";

	/* Application server type profiles */
	public static ValueNamePair[] appsTypeProfiles = new ValueNamePair[]
	{
		new ValueNamePair(Environment.APPSTYPE_JBOSS, "JBoss"),
		new ValueNamePair(Environment.APPSTYPE_TOMCAT, "Tomcat"),
		new ValueNamePair(Environment.APPSTYPE_IBM, "IBM WAS"),
		new ValueNamePair(Environment.APPSTYPE_ORACLE, "Oracle AS")
	};

	/**
	 *  Get/Set default client/server Connection
	 *  @return Connection Descriptor
	 */
	public static CConnection get()
	{
		if (s_cc == null)
		{
			String attributes = Ini.getProperty (Ini.P_CONNECTION);
			if ((attributes == null) || (attributes.length() == 0))
			{
				CConnection cc = new CConnection();
				cc.setAttributes();		//	initial environment
				CConnectionDialog ccd = new CConnectionDialog (cc);
				s_cc = ccd.getConnection ();
				//  set also in ALogin and Ctrl
				Ini.setProperty (Ini.P_CONNECTION, s_cc.toStringLong ());
				Ini.saveProperties (Ini.isClient ());
			}
			else	//	existing environment properties
			{
				s_cc = new CConnection();
				s_cc.setAttributes (attributes);
			}
			log.fine(s_cc.toString());
		}

		return s_cc;
	} 	//  get


	/**
	 *  Get specific connection
	 *  @param type database Type, e.g. Database.DB_ORACLE
	 *  @param db_host db host
	 *  @param db_port db port
	 *  @param db_name db name
	 *  @return connection
	 */
	public static CConnection get (String type, String db_host, String db_port, String db_name)
	{
		return get (type, db_host, db_port, db_name, null, null);
	} 	//  get

	/**
	 *  Get specific connection
	 *  @param jdbcString e.g. dbc:oracle:thin:@//ws-jj:1521/xe
	 *  @param db_uid db user id
	 *  @param db_pwd db user password
	 *  @return connection
	 */
	public static CConnection get (String jdbcString, String db_uid, String db_pwd)
	{
		//	jdbc:oracle:thin:@//ws-jj:1521/xe
		String type = Environment.DBTYPE_ORACLE;
		if (jdbcString.indexOf("oracle") != -1)
			;
		else if (jdbcString.indexOf("db2") != -1)
			type = Environment.DBTYPE_DB2;
		else if (jdbcString.indexOf("edb") != -1)
			type = Environment.DBTYPE_PG;
		else if (jdbcString.indexOf("sql") != -1)
			type = Environment.DBTYPE_MS;
		//
		int indexDoubleSlash = jdbcString.indexOf("//");
		int indexColon = jdbcString.lastIndexOf(":");
		int indexSingleSlash = jdbcString.lastIndexOf("/");
		String db_host = jdbcString.substring(indexDoubleSlash + 2, indexColon);
		String db_port = jdbcString.substring(indexColon + 1, indexSingleSlash);
		String db_name = jdbcString.substring(indexSingleSlash + 1);
		return get(type, db_host, db_port, db_name, db_uid, db_pwd);
	} 	//  get


	/**
	 *  Get specific client connection
	 *  @param type database Type, e.g. Database.DB_ORACLE
	 *  @param db_host db host
	 *  @param db_port db port
	 *  @param db_name db name
	 *  @param db_uid db user id
	 *  @param db_pwd db user password
	 *  @return connection
	 */
	public static CConnection get (String type, String db_host, String db_port,
	  String db_name, String db_uid, String db_pwd)
	{
		CConnection cc = new CConnection();
		cc.setAppsHost (db_host);
		cc.setDBType (type);
		cc.setDbHost (db_host);
		cc.setDbPort (db_port);
		cc.setDbName (db_name);
		//
		if (db_uid != null)
			cc.setDbUid (db_uid);
		if (db_pwd != null)
			cc.setDbPwd (db_pwd);
		return cc;
	}	//  get



	/**************************************************************************
	 *  Compiere Connection
	 *  @param	environment optional application environment
	 */
	private CConnection ()
	{
		String hostName = "localhost";
		try
		{
			InetAddress localhost = InetAddress.getLocalHost();
			hostName = localhost.getHostName();
		}
		catch (Exception e)
		{
		}

		m_apps_host = hostName;
		m_db_host = hostName;
	} 	//  CConnection

	/** Name of Connection  */
	private String 		m_name = "Standard";

	/** Application Host    */
	private String 		m_apps_host = "MyAppsServer";
	/** Application Port    */
	private int 		m_apps_port = 1099;

	/** Application Type       */
	private String 		appsType = Environment.APPSTYPE_JBOSS;
	/** Web port				*/
	private int 		webPort = 80;

	/** Connection Profile		*/
	private String	 	m_connectionProfile = null;

	/** Database Type       */
	private String 		m_type = Environment.DBTYPE_PG;
	/** Database Host       */
	private String 		m_db_host = "MyDBServer";
	/** Database Port       */
	private int m_db_port = 5444;
	/** Database name       */
	private String 		m_db_name = "compiere";

	/** In Memory connection    */
	private boolean 	m_bequeath = false;

	/** Connection uses Firewall    */
	private boolean 	m_firewall = false;
	/** Firewall host       */
	private String 		m_fw_host = "";
	/** Firewall port       */
	private int 		m_fw_port = 0;

	/** DB User name        */
	private String 		m_db_uid = "compiere";
	/** DB User password    */
	private String 		m_db_pwd = "compiere";

	/** Database            */
	private CompiereDatabase m_db = null;
	/** ConnectionException */
	private Exception 	m_dbException = null;
	private Exception 	m_appsException = null;

	/** Database Connection 	*/
	private boolean 	m_okDB = false;
	/** Apps Server Connection  */
	private boolean 	m_okApps = false;

	/** Info                */
	private final String[] 	m_info = new String[2];

	/**	Server Version		*/
	private String 		m_version = null;

	/** DataSource      	*/
	private DataSource	m_ds = null;
	/**	Server Session		*/
	private Server		m_server = null;
	/** DB Info				*/
	private String		m_dbInfo = null;


	/*************************************************************************
	 *  Get Name
	 *  @return connection name
	 */
	public String getName ()
	{
		return m_name;
	}

	/**
	 *  Set Name
	 *  @param name connection name
	 */
	public void setName (String name)
	{
		m_name = name;
	}	//  setName

	/**
	 *  Set Name
	 */
	protected void setName ()
	{
		m_name = toString ();
	} 	//  setName


	/*************
	 *  Get Application Host
	 *  @return apps host
	 */
	public String getAppsHost ()
	{
		return m_apps_host;
	}

	/**
	 *  Set Application Host
	 *  @param apps_host apps host
	 */
	public void setAppsHost (String apps_host)
	{
		m_apps_host = apps_host;
		m_name = toString ();
		m_okApps = false;
	}

	/**
	 * Get Apps Port
	 * @return port
	 */
	public int getAppsPort ()
	{
		return m_apps_port;
	}

	/**
	 * Set Apps Port
	 * @param apps_port apps port
	 */
	public void setAppsPort (int apps_port)
	{
		m_apps_port = apps_port;
		m_okApps = false;
	}

	/**
	 * 	Set Apps Port
	 * 	@param apps_portString appd port as String
	 */
	public void setAppsPort (String apps_portString)
	{
		try
		{
			if ((apps_portString == null) || (apps_portString.length() == 0))
				;
			else
				setAppsPort (Integer.parseInt (apps_portString));
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
		}
	} 	//  setAppsPort

	// Added WAS support
	/**
	 * Get Application type
	 * @return application type
	 */
	public String getAppsType()
	{
		return appsType;
	}

	/**
	 *  Set Application Type
	 *  @param appsType application type
	 */
	public void setAppsType(String appsType)
	{
		this.appsType = appsType;
		m_okApps = false;
	}	//	setAppsType()

	/**
	 * Get Web Port
	 * @return port
	 */
	public int getWebPort ()
	{
		return webPort;
	}

	/**
	 * Set Web Port
	 * @param webPort web port
	 */
	public void setWebPort (int webPort)
	{
		this.webPort = webPort;
		m_okApps = false;
	}

	/**
	 * 	Set Web Port
	 * 	@param webPort String web port as String
	 */
	public void setWebPort (String webPort)
	{
		try
		{
			if ((webPort != null) && (webPort.length() > 0))
				setWebPort (Integer.parseInt (webPort));
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
		}
	} 	//  setWebPort
	/**
	 *  Is Application Server OK
	 *  @param tryContactAgain try to contact again
	 *  @return true if Apps Server exists
	 */
	public boolean isAppsServerOK (boolean tryContactAgain)
	{
		if (!tryContactAgain)
			return m_okApps;

		if (m_iContext == null)
		{
			getInitialContext (false);
			if (!m_okApps)
				return false;
		}

		//	Contact it
		try
		{
			Status status = getStatusEjbStub();
			m_version = status.getDateVersion ();
			status.remove ();
			m_okApps = true;
		}
		catch (Exception ce)
		{
			log.severe(ce.getMessage());
			m_okApps = false;
		}
		catch (Throwable t)
		{
			log.severe(t.getMessage());
			m_okApps = false;
		}
		return m_okApps;
	} 	//  isAppsOK

	/**
	 *  Test ApplicationServer
	 *  @return Exception or null
	 */
	public Exception testAppsServer ()
	{
		if (queryAppsServerInfo ())
			testDatabase (false);
		return getAppsServerException ();
	} 	//  testAppsServer

	/**
	 * 	Get Server
	 * 	@return Server
	 */
	public Server getServer()
	{
		if (m_server == null)
		{
			try
			{
				InitialContext ic = getInitialContext (true);
				if (ic != null)
				{
					ServerHome serverHome = null;
					if (!appsType.equals(Environment.APPSTYPE_IBM))
					{
						serverHome = (ServerHome)ic.lookup(ServerHome.JNDI_NAME);
					}
					else  // WAS using iiop
					{
						Object serverHomeObj = ic.lookup ("ejb/" + ServerHome.JNDI_NAME);
						serverHome = (ServerHome)javax.rmi.PortableRemoteObject.narrow(
								serverHomeObj, ServerHome.class);
					}

					if (serverHome != null)
						m_server = serverHome.create();
				}
			}
			catch (Exception ex)
			{
				log.log(Level.SEVERE, "", ex);
				m_iContext = null;
			}
		}
		return m_server;
	}	//	getServer


	/**
	 *  Get Apps Server Version
	 *  @return db host name
	 */
	public String getServerVersion ()
	{
		return m_version;
	}	//	getServerVersion


	/*************
	 *  Get Database Host name
	 *  @return db host name
	 */
	public String getDbHost ()
	{
		return m_db_host;
	}	//	getDbHost

	/**
	 *  Set Database host name
	 *  @param db_host db host
	 */
	public void setDbHost (String db_host)
	{
		m_db_host = db_host;
		m_name = toString ();
		m_okDB = false;
	}	//	setDbHost

	/**
	 *  Get Database Name (Service Name)
	 *  @return db name
	 */
	public String getDbName ()
	{
		return m_db_name;
	}	//	getDbName

	/**
	 *  Set Database Name (Service Name)
	 *  @param db_name db name
	 */
	public void setDbName (String db_name)
	{
		m_db_name = db_name;
		m_name = toString ();
		m_okDB = false;
	}	//	setDbName

	/**
	 * 	Get DB Port
	 * 	@return port
	 */
	public int getDbPort ()
	{
		return m_db_port;
	}	//	getDbPort

	/**
	 * Set DB Port
	 * @param db_port db port
	 */
	public void setDbPort (int db_port)
	{
		m_db_port = db_port;
		m_okDB = false;
	}	//	setDbPort

	/**
	 * Set DB Port
	 * @param db_portString db port as String
	 */
	public void setDbPort (String db_portString)
	{
		try
		{
			if ((db_portString == null) || (db_portString.length() == 0))
				;
			else
				setDbPort (Integer.parseInt (db_portString));
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
		}
	} 	//  setDbPort

	/**
	 *  Get Database Password
	 *  @return db password
	 */
	public String getDbPwd ()
	{
		return m_db_pwd;
	}	//	getDbPwd

	/**
	 *  Set DB password
	 *  @param db_pwd db user password
	 */
	public void setDbPwd (String db_pwd)
	{
		m_db_pwd = db_pwd;
		m_okDB = false;
	}	//	setDbPwd

	/**
	 *  Get Database User
	 *  @return db user
	 */
	public String getDbUid ()
	{
		return m_db_uid;
	}	//	getDbUid

	/**
	 *  Set Database User
	 *  @param db_uid db user id
	 */
	public void setDbUid (String db_uid)
	{
		m_db_uid = db_uid;
		m_name = toString ();
		m_okDB = false;
	}	//	setDbUid

	/**
	 * 	RMI over HTTP
	 * 	@return true if RMI over HTTP (Wan Connection Profile)
	 */
	public boolean isRMIoverHTTP ()
	{
		return Ini.isClient()
			&& getConnectionProfile().equals(PROFILE_WAN);
	}	//	isRMIoverHTTP

	/**
	 * 	Set Connection Profile
	 *	@param connectionProfile connection profile
	 */
	public void setConnectionProfile (ValueNamePair connectionProfile)
	{
		if (connectionProfile != null)
			setConnectionProfile(connectionProfile.getValue());
	}	//	setConnectionProfile

	/**
	 * 	Set Connection Profile
	 *	@param connectionProfile connection profile
	 */
	public void setConnectionProfile (String connectionProfile)
	{
		if ((connectionProfile == null)
			|| ((m_connectionProfile != null)
				&& m_connectionProfile.equals(connectionProfile)))	//	same
			return;

		if (PROFILE_LAN.equals(connectionProfile)
				|| PROFILE_TERMINAL.equals(connectionProfile)
				|| PROFILE_VPN.equals(connectionProfile)
				|| PROFILE_WAN.equals(connectionProfile))
		{
			if (m_connectionProfile != null)
			{
				log.config(m_connectionProfile + " -> " + connectionProfile);
				m_connectionProfile = connectionProfile;
				Ini.setProperty(Ini.P_CONNECTION, toStringLong());
			}
			else
				m_connectionProfile = connectionProfile;
		}
		else
			log.warning("Invalid: " + connectionProfile);
	}	//	setConnectionProfile

	/**
	 * 	Get Connection Profile
	 *	@return connection profile
	 */
	public String getConnectionProfile ()
	{
		if (m_connectionProfile != null)
			return m_connectionProfile;
		return PROFILE_LAN;
	}	//	getConnectionProfile

	/**
	 * 	Get Connection Profile Text
	 * 	@param connectionProfile
	 *	@return connection profile text
	 */
	public String getConnectionProfileText (String connectionProfile)
	{
		for (ValueNamePair element : CONNECTIONProfiles) {
			if (element.getValue().equals(connectionProfile))
				return element.getName();
		}
		return CONNECTIONProfiles[0].getName();
	}	//	getConnectionProfileText

	/**
	 * 	Get Connection Profile Text
	 *	@return connection profile text
	 */
	public String getConnectionProfileText ()
	{
		return getConnectionProfileText(getConnectionProfile());
	}	//	getConnectionProfileText

	/**
	 * 	Get Connection Profile
	 *	@return connection profile
	 */
	public ValueNamePair getConnectionProfilePair ()
	{
		for (ValueNamePair element : CONNECTIONProfiles) {
			if (element.getValue().equals(getConnectionProfile()))
				return element;
		}
		return CONNECTIONProfiles[0];
	}	//	getConnectionProfilePair

	/**
	 *  Should objects be created on Server ?
	 *  @return true if client and VPN/WAN
	 */
	public boolean isServerObjects()
	{
		return (Ini.isClient()
			&& (getConnectionProfile().equals(PROFILE_VPN)
				|| getConnectionProfile().equals(PROFILE_WAN) ));
	}   //  isServerObjects

	/**
	 *  Should processes be created on Server ?
	 *  @return true if client and VPN/WAN
	 */
	public boolean isServerProcess()
	{
		return (Ini.isClient()
			&& (getConnectionProfile().equals(PROFILE_VPN)
				|| getConnectionProfile().equals(PROFILE_WAN) ));
	}   //  isServerProcess

	/**
	 *  Is this a Terminal Server ?
	 *  @return true if client and Terminal
	 */
	public boolean isTerminalServer()
	{
		return Ini.isClient() && getConnectionProfile().equals(PROFILE_TERMINAL);
	}   //  isTerminalServer

	/**
	 *  Is DB via Firewall
	 *  @return true if via firewall
	 */
	public boolean isViaFirewall ()
	{
		return m_firewall;
	}

	/**
	 * Method setViaFirewall
	 * @param viaFirewall boolean
	 */
	public void setViaFirewall (boolean viaFirewall)
	{
		m_firewall = viaFirewall;
		m_okDB = false;
	}

	/**
	 * Method setViaFirewall
	 * @param viaFirewallString String
	 */
	public void setViaFirewall (String viaFirewallString)
	{
		try
		{
			setViaFirewall (Boolean.valueOf (viaFirewallString).booleanValue ());
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
		}
	}

	/**
	 * Method getFwHost
	 * @return String
	 */
	public String getFwHost ()
	{
		return m_fw_host;
	}

	/**
	 * Method setFwHost
	 * @param fw_host String
	 */
	public void setFwHost (String fw_host)
	{
		m_fw_host = fw_host;
		m_okDB = false;
	}

	/**
	 * Get Firewall port
	 * @return firewall port
	 */
	public int getFwPort ()
	{
		return m_fw_port;
	}

	/**
	 * Set Firewall port
	 * @param fw_port firewall port
	 */
	public void setFwPort (int fw_port)
	{
		m_fw_port = fw_port;
		m_okDB = false;
	}

	/**
	 * Set Firewall port
	 * @param fw_portString firewall port as String
	 */
	public void setFwPort (String fw_portString)
	{
		try
		{
			if ((fw_portString == null) || (fw_portString.length() == 0))
				;
			else
				setFwPort (Integer.parseInt (fw_portString));
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
		}
	}

	/**
	 *  Is it a bequeath connection
	 *  @return true if bequeath connection
	 */
	public boolean isBequeath ()
	{
		return m_bequeath;
	}

	/**
	 * Set Bequeath
	 * @param bequeath bequeath connection
	 */
	public void setBequeath (boolean bequeath)
	{
		m_bequeath = bequeath;
		m_okDB = false;
	}

	/**
	 * Set Bequeath
	 * @param bequeathString bequeath connection as String (true/false)
	 */
	public void setBequeath (String bequeathString)
	{
		try
		{
			setBequeath (Boolean.valueOf (bequeathString).booleanValue ());
		}
		catch (Exception e)
		{
			log.severe(e.toString ());
		}
	}	//	setBequeath

	/**
	 *  Get Database Type
	 *  @return database type
	 */
	public String getDBType ()
	{
		return m_type;
	}	//	getType

	/**
	 *  Set Database Type and default settings.
	 *  Checked against installed databases
	 *  @param type database Type, e.g. Environemnt.DBTYPE_ORACLE
	 */
	public void setDBType (String type)
	{
		if (type == null)
			return;
		for (String element : Database.DB_NAMES) {
			if (type.toUpperCase().startsWith (element.toUpperCase()))
			{
				m_type = element;
				m_okDB = false;
				break;
			}
		}
		
		if (isOracle())
		{
			setFwPort (DB_Oracle.DEFAULT_CM_PORT);
		}
		else
		{
			setBequeath (false);
			setViaFirewall (false);
		}
	} 	//  setType

	/**
	 *  Supports BLOB
	 *  @return true if BLOB is supported
	 */
	public boolean supportsBLOB ()
	{
		return m_db.supportsBLOB ();
	}	//  supportsBLOB


	/**
	 *  Is Oracle XE DB
	 *  @return true if Oracle XE
	 */
	public boolean isOracleXE()
	{
		if (Environment.DBTYPE_ORACLE.equals (m_type))
			return "XE".equalsIgnoreCase(m_db_name);
		return false;
	} 	//  isOracleXE

	/**
	 *  Is Oracle DB
	 *  @return true if Oracle
	 */
	public boolean isOracle ()
	{
		return Environment.DBTYPE_ORACLE.equals (m_type);
	} 	//  isOracle

	/**
	 *  Is IBM DB/2
	 *  @return true if DB/2
	 */
	public boolean isDB2 ()
	{
		return Environment.DBTYPE_DB2.equals (m_type);
	} 	//  isDB2

	/**
	 *  Is Microsoft SQL Server
	 *  @return true if Microsoft
	 */
	public boolean isMSSQLServer()
	{
		return Environment.DBTYPE_MS.equals (m_type);
	} 	//  isMSSQLServer

	/**
	 *  Is PostgrSQL
	 *  @return true if PostgreSQL
	 */
	public boolean isPostgreSQL()
	{
		return Environment.DBTYPE_PG.equals (m_type);
	} 	//  isPostgreSQL

	/**
	 *  Is Database Connection OK
	 *  @return true if database connection is OK
	 */
	public boolean isDatabaseOK ()
	{
		return m_okDB;
	} 	//  isDatabaseOK

	/**************************************************************************
	 *  Create DB Connection
	 * @return data source != null
	 */
	public boolean setDataSource()
	{
	//	System.out.println ("CConnection.setDataSource - " + m_ds + " - Client=" + Ini.isClient());
		if ((m_ds == null) && Ini.isClient())
		{
			if (getDatabase() != null)	//	no db selected
				m_ds = getDatabase().getDataSource(this);
		//	System.out.println ("CConnection.setDataSource - " + m_ds);
		}
		return m_ds != null;
	} 	//	setDataSource

	/**
	 * 	Set Data Source
	 *	@param ds data source
	 *	@return data source != null
	 */
	public boolean setDataSource(DataSource ds)
	{
		if ((ds == null) && (m_ds != null))
			getDatabase().close();
		m_ds = ds;
		return m_ds != null;
	} 	//	setDataSource

	/**
	 *  Get Server Connection
	 *  @return DataSource
	 */
	public DataSource getDataSource ()
	{
		return m_ds;
	} 	//	getDataSource

	/**
	 *  Has Server Connection
	 *  @return true if DataSource exists
	 */
	public boolean isDataSource ()
	{
		return m_ds != null;
	} 	//	isDataSource


	/**************************************************************************
	 *  Test Database Connection.
	 *  -- Example --
	 *  Database: PostgreSQL - 7.1.3
	 *  Driver:   PostgreSQL Native Driver - PostgreSQL 7.2 JDBC2
	 *  -- Example --
	 *  Database: Oracle - Oracle8i Enterprise Edition Release 8.1.7.0.0 - Production With the Partitioning option JServer Release 8.1.7.0.0 - Production
	 *  Driver:   Oracle JDBC driver - 9.0.1.1.0
	 *  @param retest
	 *  @return Exception or null
	 */
	public Exception testDatabase(boolean retest)
	{
		//	At this point Application Server Connection is tested.
		if (isRMIoverHTTP())
			return null;
		if (!retest && (m_ds != null) && m_okDB)
			return null;

		getDatabase().close();
		m_ds = null;
		setDataSource();
		//  the actual test
		Connection conn = createConnection (true,
			Connection.TRANSACTION_READ_COMMITTED);
		if (conn != null)
		{
			try
			{
				DatabaseMetaData dbmd = conn.getMetaData ();
				m_info[0] = "Database=" + dbmd.getDatabaseProductName ()
							+ " - " + dbmd.getDatabaseProductVersion ();
				m_info[0] = m_info[0].replace ('\n', ' ');
				m_info[1] = "Driver  =" + dbmd.getDriverName ()
							+ " - " + dbmd.getDriverVersion ();
				if (isDataSource())
					m_info[1] += " - via DataSource";
				m_info[1] = m_info[1].replace ('\n', ' ');
				log.config(m_info[0] + " - " + m_info[1]);
				conn.close ();
			}
			catch (Exception e)
			{
				log.severe (e.toString());
				return e;
			}
		}
		return m_dbException; //  from opening
	} 	//  testDatabase


	/*************************************************************************
	 *  Short String representation
	 *  @return appsHost{dbHost-dbName-uid}
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer (m_apps_host);
		sb.append ("{").append (m_db_host)
		  .append ("-").append (m_db_name)
		  .append ("#").append (m_db_uid)
		  .append ("}");
		return sb.toString ();
	} 	//  toString

	/**
	 * 	Detail Info
	 *	@return info
	 */
	public String toStringDetail ()
	{
		StringBuffer sb = new StringBuffer (m_apps_host);
		sb.append ("{").append (m_db_host)
		  .append ("-").append (m_db_name)
		  .append ("#").append (m_db_uid)
		  .append ("}");
		//
		Connection conn = createConnection (true,
			Connection.TRANSACTION_READ_COMMITTED);
		if (conn != null)
		{
			try
			{
				DatabaseMetaData dbmd = conn.getMetaData ();
				sb.append("\nDatabase=" + dbmd.getDatabaseProductName ()
							+ " - " + dbmd.getDatabaseProductVersion());
				sb.append("\nDriver  =" + dbmd.getDriverName ()
							+ " - " + dbmd.getDriverVersion ());
				if (isDataSource())
					sb.append(" - via DS");
				conn.close ();
			}
			catch (Exception e)
			{
			}
		}
		conn = null;
		return sb.toString ();
	} 	//  toStringDetail

	/**
	 * 	Get DB Version Info
	 *	@return info
	 */
	public String getDBInfo()
	{
		if (m_dbInfo != null)
			return m_dbInfo;
		StringBuffer sb = new StringBuffer ();
		Connection conn = createConnection (true,
			Connection.TRANSACTION_READ_COMMITTED);
		if (conn != null)
		{
			try
			{
				DatabaseMetaData dbmd = conn.getMetaData ();
				sb.append(dbmd.getDatabaseProductVersion())
					.append(";").append(dbmd.getDriverVersion());
				if (isDataSource())
					sb.append(";DS");
				conn.close ();
				m_dbInfo = sb.toString ();
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "", e);
				sb.append(e.getLocalizedMessage());
			}
		}
		conn = null;
		return sb.toString();
	} 	//  toStringDetail


	/**
	 *  String representation.
	 *  Used also for Instanciation
	 *  @return string representation
	 *	@see #setAttributes(String) setAttributes
	 */
	public String toStringLong ()
	{
		StringBuffer sb = new StringBuffer ("CConnection[");
		sb.append ("name=").append (m_name)
		  // Added WAS support
		  .append (",AppsType=").append (appsType)
		  .append (",AppsHost=").append (m_apps_host)
		  .append (",AppsPort=").append (m_apps_port)
		  .append (",Profile=").append (getConnectionProfile())
		  .append (",WebPort=").append (webPort)
		  .append (",type=").append (m_type)
		  .append (",DBhost=").append (m_db_host)
		  .append (",DBport=").append (m_db_port)
		  .append (",DBname=").append (m_db_name)
		  .append (",BQ=").append (m_bequeath)
		  .append (",FW=").append (m_firewall)
		  .append (",FWhost=").append (m_fw_host)
		  .append (",FWport=").append (m_fw_port)
		  .append (",UID=").append (m_db_uid)
		  .append (",PWD=").append (m_db_pwd)
		  ;		//	the format is read by setAttributes
		sb.append ("]");
		return sb.toString ();
	}	//  toStringLong

	/**
	 *  Set Attributes from String (pares toStringLong())
	 *  @param attributes attributes
	 */
	private void setAttributes (String attributes)
	{
		try
		{
			int index = attributes.indexOf ("name=");
			setName (attributes.substring (index + 5, attributes.indexOf (",", index)));
			// Added WAS support
			index = attributes.indexOf ("AppsType=");
			if (index > 0)
				setAppsType (attributes.substring (index + 9, attributes.indexOf (",", index)));
			//
			setAppsHost (attributes.substring (attributes.indexOf ("AppsHost=") + 9, attributes.indexOf (",AppsPort=")));
			index = attributes.indexOf("AppsPort=");
			setAppsPort (attributes.substring (index + 9, attributes.indexOf (",", index)));
			index = attributes.indexOf("Profile=");
			if (index > 0)	//	new attribute, may not exist
				setConnectionProfile(attributes.substring(index+8, attributes.indexOf (",", index)));
			// Added WAS support
			index = attributes.indexOf ("WebPort=");
			if (index > 0)
				setWebPort (attributes.substring (index + 8, attributes.indexOf (",", index)));
			//
			setDBType (attributes.substring (attributes.indexOf ("type=")+5, attributes.indexOf (",DBhost=")));
			setDbHost (attributes.substring (attributes.indexOf ("DBhost=") + 7, attributes.indexOf (",DBport=")));
			setDbPort (attributes.substring (attributes.indexOf ("DBport=") + 7, attributes.indexOf (",DBname=")));
			setDbName (attributes.substring (attributes.indexOf ("DBname=") + 7, attributes.indexOf (",BQ=")));
			//
			setBequeath (attributes.substring (attributes.indexOf ("BQ=") + 3, attributes.indexOf (",FW=")));
			setViaFirewall (attributes.substring (attributes.indexOf ("FW=") + 3, attributes.indexOf (",FWhost=")));
			setFwHost (attributes.substring (attributes.indexOf ("FWhost=") + 7, attributes.indexOf (",FWport=")));
			setFwPort (attributes.substring (attributes.indexOf ("FWport=") + 7, attributes.indexOf (",UID=")));
			//
			setDbUid (attributes.substring (attributes.indexOf ("UID=") + 4, attributes.indexOf (",PWD=")));
			setDbPwd (attributes.substring (attributes.indexOf ("PWD=") + 4, attributes.indexOf ("]")));
			//
		}
		catch (Exception e)
		{
			log.severe(attributes + " - " + e.toString ());
		}
	}	//  setAttributes

	/**
	 *  Set Attributes from Environment
	 */
	private void setAttributes ()
	{
		String host = Environment.getCodeBaseHost();
		if (host != null)
		{
			m_apps_host = host;
			m_db_host = host;
		}
		Environment env = Environment.get();
		if ((env != null) && !env.isEmpty())
		{
			String s = env.getProperty (Environment.COMPIERE_APPS_SERVER, "");
			if (s.length () > 0)
				setAppsHost(s);
			s = env.getProperty (Environment.COMPIERE_JNP_PORT, "");
			if (s.length () > 0)
				setAppsPort (s);

			//	Database
			s = env.getProperty (Environment.COMPIERE_DB_PATH, "");
			if (s.length () > 0)
				setDBType (s);
			s = env.getProperty (Environment.COMPIERE_DB_SERVER, "");
			if (s.length () > 0)
				setDbHost (s);
			s = env.getProperty (Environment.COMPIERE_DB_PORT, "");
			if (s.length () > 0)
				setDbPort (s);
			s = env.getProperty (Environment.COMPIERE_DB_NAME, "");
			if (s.length () > 0)
				setDbName (s);
			//
			s = env.getProperty (Environment.COMPIERE_DB_USER, "");
			if (s.length () > 0)
				setDbUid (s);
			s = env.getProperty (Environment.COMPIERE_DB_PASSWORD, "");
			if (s.length () > 0)
				setDbPwd (s);
			// Added WAS support
			s = env.getProperty (Environment.COMPIERE_APPS_TYPE, "");
			if (s.length () > 0)
				setAppsType (s);
			s = env.getProperty (Environment.COMPIERE_WEB_PORT, "");
			if (s.length () > 0)
				setWebPort (s);
		}
		setName();
	}	//  setAttributes


	/**
	 *  Equals
	 *  @param o object
	 *  @return true if o equals this
	 */
	@Override
	public boolean equals (Object o)
	{
		if (o instanceof CConnection)
		{
			CConnection cc = (CConnection)o;
			if (cc.getAppsHost().equals (m_apps_host)
			  && (cc.getAppsPort() == m_apps_port)
			  && cc.getDbHost().equals (m_db_host)
			  && (cc.getDbPort() == m_db_port)
			  && cc.getConnectionProfile().equals(getConnectionProfile())
			  && cc.getDbName().equals(m_db_name)
			  && cc.getDBType().equals(m_type)
			  && cc.getDbUid().equals(m_db_uid)
			  && cc.getDbPwd().equals(m_db_pwd))
				return true;
		}
		return false;
	}	//  equals

	/**
	 *  Get Info.
	 *  - Database, Driver, Status Info
	 *  @return info
	 */
	public String getInfo ()
	{
		StringBuffer sb = new StringBuffer();
		if ( (m_info[0] != null) && !(m_info[0].equals("")) )
			sb.append(m_info[0]);

		if ( (m_info[1] != null) && !(m_info[1].equals("")) )
			sb.append (" - ").append (m_info[1]);

		sb.append ("\n").append (getDatabase())
		  .append ("\nAppsServerOK=").append (isAppsServerOK (false))
		  .append (", DatabaseOK=").append (isDatabaseOK ());
		return sb.toString ();
	}	//  getInfo


	/*************************************************************************
	 *  Hashcode
	 *  @return hashcode of name
	 */
	@Override
	public int hashCode ()
	{
		return m_name.hashCode ();
	} 	//  hashCode

	/**
	 *  Get Database
	 *  @return database
	 */
	public CompiereDatabase getDatabase()
	{
		//  different driver
		if ((m_db != null) && !m_db.getName ().equals (m_type))
			m_db = null;

		if (m_db == null)
		{
			try
			{
				for (int i = 0; i < Database.DB_NAMES.length; i++)
				{
					if (Database.DB_NAMES[i].equals (m_type))
					{
						m_db = (CompiereDatabase)Database.DB_CLASSES[i].
							   newInstance ();
						break;
					}
				}
				if (m_db != null)		//	test class loader ability
					m_ds = m_db.getDataSource(this);
			}
			catch (NoClassDefFoundError ee)
			{
				System.err.println("Environment Error - Check Compiere.properties - " + ee);
				if (Ini.isClient())
				{
					if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog
						(null, "There is a configuration error:\n" + ee
							+ "\nDo you want to reset the saved configuration?",
							"Compiere Configuration Error",
							JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE))
						Ini.deletePropertyFile();
				}
				System.exit (1);
			}
			catch (Exception e)
			{
				log.severe(e.toString ());
			}
		}
		return m_db;
	} 	//  getDatabase

	/**
	 *  Get Connection String
	 *  @return connection string
	 */
	public String getConnectionURL ()
	{
		getDatabase (); //  updates m_db
		if (m_db != null)
			return m_db.getConnectionURL (this);
		else
			return "";
	} 	//  getConnectionURL

	/**
	 *  Get Server Connection - do close
	 *  @param autoCommit true if autocommit connection
	 *  @param trxLevel Connection transaction level
	 *  @return Connection
	 */
	public Connection getServerConnection (boolean autoCommit, int trxLevel)
	{
		Connection conn = null;
		//	Server Connection
		if (m_ds != null)
		{
			try
			{
				conn = m_ds.getConnection ();
				conn.setAutoCommit (autoCommit);
				conn.setTransactionIsolation (trxLevel);
				m_okDB = true;
			}
			catch (SQLException ex)
			{
				m_dbException = ex;
				log.log(Level.SEVERE, "", ex);
			}
		}

		//	Server
		return conn;
	} 	//	getServerConnection


	/**
	 *  Create Connection - no not close.
	 * 	Sets m_dbException
	 *  @param autoCommit true if autocommit connection
	 *  @param transactionIsolation Connection transaction level
	 *  @return Connection
	 */
	public Connection createConnection (boolean autoCommit, int transactionIsolation)
	{
		Connection conn = null;
		m_dbException = null;
		m_okDB = false;
		//
		getDatabase (); //  updates m_db
		if (m_db == null)
		{
			m_dbException = new CompiereStateException("No Database Connector");
			return null;
		}
		//

		try
		{
			Exception ee = null;
			try
			{
				conn = m_db.getCachedConnection(this, autoCommit, transactionIsolation);
			}
			catch (Exception e)
			{
				ee = e;
			}
			if (conn == null)
			{
				Thread.yield();
				log.config("retrying - " + ee);
				if (!DB.isDB2())
					conn = m_db.getCachedConnection(this, autoCommit, transactionIsolation);
				else
					conn = m_db.getDriverConnection(this);
			}
			//	System.err.println ("CConnection.getConnection(Cache) - " + getConnectionURL() + ", AutoCommit=" + autoCommit + ", TrxLevel=" + trxLevel);
		//	}
		//	else if (isDataSource())	//	Client
		//	{
		//		conn = m_ds.getConnection();
			//	System.err.println ("CConnection.getConnection(DataSource) - " + getConnectionURL() + ", AutoCommit=" + autoCommit + ", TrxLevel=" + trxLevel);
		//	}
		//	else
		//	{
		//		conn = m_db.getDriverConnection (this);
			//	System.err.println ("CConnection.getConnection(Driver) - " + getConnectionURL() + ", AutoCommit=" + autoCommit + ", TrxLevel=" + trxLevel);
		//	}
			//	Verify Connection
			if (conn != null)
			{
				if (conn.getTransactionIsolation() != transactionIsolation)
					conn.setTransactionIsolation (transactionIsolation);
				if (conn.getAutoCommit() != autoCommit)
					conn.setAutoCommit (autoCommit);
				m_okDB = true;
			}
		}
		catch (UnsatisfiedLinkError ule)
		{
			String msg = ule.getLocalizedMessage()
				+ " -> Did you set the LD_LIBRARY_PATH ? - " + getConnectionURL();
			m_dbException = new Exception(msg);
			log.warning(msg);
		}
		catch (SQLException ex)
		{
			m_dbException = ex;
			if (conn == null)
				log.log(Level.WARNING, getConnectionURL ()
					+ ", (1) AutoCommit=" + autoCommit + ",TrxIso=" + getTransactionIsolationInfo(transactionIsolation)
				//	+ " (" + getDbUid() + "/" + getDbPwd() + ")"
					+ " - " + ex.getMessage());
			else
			{
				try
				{
					log.warning(getConnectionURL ()
						+ ", (2) AutoCommit=" + conn.getAutoCommit() + "->" + autoCommit
						+ ", TrxIso=" + getTransactionIsolationInfo(conn.getTransactionIsolation()) + "->" + getTransactionIsolationInfo(transactionIsolation)
					//	+ " (" + getDbUid() + "/" + getDbPwd() + ")"
						+ " - " + ex.getMessage());
				}
				catch (Exception ee)
				{
					log.warning(getConnectionURL ()
						+ ", (3) AutoCommit=" + autoCommit + ", TrxIso=" + getTransactionIsolationInfo(transactionIsolation)
					//	+ " (" + getDbUid() + "/" + getDbPwd() + ")"
						+ " - " + ex.getMessage());
				}
			}
		}
		catch (Exception ex)
		{
			m_dbException = ex;
			log.log(Level.WARNING, getConnectionURL(), ex);
		}
	//	System.err.println ("CConnection.getConnection - " + conn);
		return conn;
	}	//  getConnection

	/**
	 *  Get Database Exception of last connection attempt
	 *  @return Exception or null
	 */
	public Exception getDatabaseException ()
	{
		return m_dbException;
	} 	//  getConnectionException

	/*************************************************************************/

	private InitialContext m_iContext = null;
	private Hashtable<String,String> m_env = null;

	/**
	 *  Get Application Server Initial Context
	 *  @param useCache if true, use existing cache
	 *  @return Initial Context or null
	 */
	public InitialContext getInitialContext (boolean useCache)
	{
		if (useCache && (m_iContext != null))
			return m_iContext;

		//	Set Environment
		if ((m_env == null) || !useCache)
			m_env = getInitialEnvironment(getAppsHost(), getAppsPort(),
							getAppsType(), isRMIoverHTTP(), getWebPort());
		String connect = m_env.get(Context.PROVIDER_URL);
		Env.getCtx().setContext(Context.PROVIDER_URL, connect);

		//	Get Context
		m_iContext = null;
		try
		{
			if (m_iContext != null)
			{
				m_iContext.close();
				m_iContext = null;
			}
			m_iContext = new InitialContext (m_env);
		}
		catch (Exception ex)
		{
			m_okApps = false;
			m_appsException = ex;
			if (connect == null)
				connect = m_env.get(Context.PROVIDER_URL);
			log.severe(connect
				+ "\n - " + ex.toString ()
				+ "\n - " + m_env);
			if (CLogMgt.isLevelFinest())
				ex.printStackTrace();
		}
		return m_iContext;
	}	//	getInitialContext

	/**
	 * 	Get Initial Environment
	 * 	@param AppsHost host
	 * 	@param AppsPort port
	 * 	@param RMIoverHTTP true if tunnel through HTTP
	 *	@return environment
	 */
	public static Hashtable<String,String> getInitialEnvironment (String AppsHost, int AppsPort,
		String appsType, boolean RMIoverHTTP, int webPort)
	{
		//	Set Environment
		Hashtable<String,String> env = new Hashtable<String,String>();
		String connect = AppsHost;
		if (appsType.equals(Environment.APPSTYPE_IBM))
		{
			if (RMIoverHTTP)
			{
				System.setProperty("com.ibm.CORBA.ForceTunnel", "always");
				System.setProperty("com.ibm.CORBA.FragmentSize", "0");
				connect = "http://" + AppsHost + ":" + webPort +
							"/servlet/com.ibm.CORBA.services.IIOPTunnelServlet";
				System.setProperty("com.ibm.CORBA.TunnelAgentURL", connect);
			}
			else
			{
				System.clearProperty("com.ibm.CORBA.ForceTunnel");
				System.clearProperty("com.ibm.CORBA.FragmentSize");
				System.clearProperty("com.ibm.CORBA.TunnelAgentURL");
			}
			if (AppsHost.indexOf("corbaloc:iiop:") == -1)
				connect = "corbaloc:iiop:" + AppsHost + ":" + AppsPort;
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
			env.put(Context.PROVIDER_URL, connect);
		}
		else if (appsType.equals(Environment.APPSTYPE_JBOSS))
		{
			if (RMIoverHTTP)
			{
				env.put (Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.HttpNamingContextFactory");
				if (AppsHost.indexOf("://") == -1)
					connect = "http://" + AppsHost + ":" + webPort
							+ "/invoker/JNDIFactory";
				env.put(Context.PROVIDER_URL, connect);
			}
			else
			{
				env.put (Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");
				if (AppsHost.indexOf("://") == -1)
					connect = "jnp://" + AppsHost + ":" + AppsPort;
				env.put (Context.PROVIDER_URL, connect);
			}
			env.put (Context.URL_PKG_PREFIXES, "org.jboss.naming.client");
			//	HTTP - default timeout 0
			env.put (org.jnp.interfaces.TimedSocketFactory.JNP_TIMEOUT, "2000");	//	timeout in ms
			env.put (org.jnp.interfaces.TimedSocketFactory.JNP_SO_TIMEOUT, "2000");
			//	JNP - default timeout 5 sec
			env.put(org.jnp.interfaces.NamingContext.JNP_DISCOVERY_TIMEOUT, "2000");
		}
		return env;
	}	//	getInitialEnvironment

	/**
	 * 	Get Initial Context
	 *	@param env environment
	 *	@return Initial Context
	 */
	public static InitialContext getInitialContext (Hashtable<String,String> env)
	{
		InitialContext iContext = null;
		try
		{
			iContext = new InitialContext (env);
		}
		catch (Exception ex)
		{
			log.warning ("URL=" + env.get(Context.PROVIDER_URL)
				+ "\n - " + ex.toString ()
				+ "\n - " + env);
			iContext = null;
			if (CLogMgt.isLevelFinest())
				ex.printStackTrace();
		}
		return iContext;
	}	//	getInitialContext


	/**
	 *  Query Application Server Status.
	 *  update okApps
	 *  @return true ik OK
	 */
	private boolean queryAppsServerInfo ()
	{
		log.finer(getAppsHost());
		long start = System.currentTimeMillis();
		m_okApps = false;
		m_appsException = null;
		//
		getInitialContext (false);
		if (m_iContext == null)
			return m_okApps;	//	false

		//	Prevent error trace
	//	CLogMgtLog4J.enable(false);
		try
		{
			Status status = getStatusEjbStub();
			if (status != null)
			{
				updateInfoFromServer(status);
				status.remove ();
				m_okApps = true;
			}
		}
		catch (CommunicationException ce)	//	not a "real" error
		{
			//	m_appsException = ce;
			String connect = m_env.get(Context.PROVIDER_URL);
			log.warning (connect
				+ "\n - " + ce.toString ()
				+ "\n - " + m_env);
		}
		catch (Exception e)
		{
			m_appsException = e;
			String connect = m_env.get(Context.PROVIDER_URL);
			log.warning (connect
				+ "\n - " + e.toString ()
				+ "\n - " + m_env);
		}
		CLogMgtLog4J.enable(true);
		log.fine("Success=" + m_okApps + " - " + (System.currentTimeMillis()-start) + "ms");
		return m_okApps;
	}	//  setAppsServerInfo

	/**
	 *  Get Last Exception of Apps Server Connection attempt
	 *  @return Exception or null
	 */
	public Exception getAppsServerException ()
	{
		return m_appsException;
	} 	//  getAppsServerException

	/**
	 *  Update Connection Info from Apps Server
	 *  @param svr Apps Server Status
	 *  @throws Exception
	 */
	private void updateInfoFromServer (Status svr) throws Exception
	{
		if (svr == null)
		{
		//	throw new IllegalArgumentException ("AppsServer was NULL");
			return;
		}

		setDBType (svr.getDbType());
		setDbHost (svr.getDbHost());
		setDbPort (svr.getDbPort ());
		setDbName (svr.getDbName ());
		setDbUid (svr.getDbUid ());
		setDbPwd (svr.getDbPwd ());
		setBequeath (false);
		//
		setFwHost (svr.getFwHost ());
		setFwPort (svr.getFwPort ());
		if (getFwHost ().length () == 0)
			setViaFirewall (false);
		m_version = svr.getDateVersion ();
		log.config("DB Server=" + getDbHost() + ", DB=" + getDbName());
	} 	//  update Info

	/**
	 *  Convert Statement
	 *  @param origStatement original statement (Oracle notation)
	 *  @return converted Statement
	 *  @throws Exception
	 */
	public String convertStatement (String origStatement)
	  throws Exception
	{
		//  make sure we have a good database
		if ((m_db != null) && !m_db.getName ().equals (m_type))
			getDatabase ();
		if (m_db != null)
			return m_db.convertStatement (origStatement);
		throw new Exception (
		  "CConnection.convertStatement - No Converstion Database");
	}	//  convertStatement

	/**
	 * 	Get Status Info
	 *	@return info
	 */
	public String getStatus()
	{
		StringBuffer sb = new StringBuffer (m_apps_host);
		sb.append ("{").append (m_db_host)
		  .append ("-").append (m_db_name)
		  .append ("-").append (m_db_uid)
		  .append ("}");
		if (m_db != null)
		  sb.append (m_db.getStatus());
		return sb.toString ();
	}	//	getStatus

	/**
	 * 	Get Transaction Isolation Info
	 *	@param transactionIsolation p_trx iso
	 *	@return clear test
	 */
	public static String getTransactionIsolationInfo(int transactionIsolation)
	{
		if (transactionIsolation == Connection.TRANSACTION_NONE)
			return "NONE";
		if (transactionIsolation == Connection.TRANSACTION_READ_COMMITTED)
			return "READ_COMMITTED";
		if (transactionIsolation == Connection.TRANSACTION_READ_UNCOMMITTED)
			return "READ_UNCOMMITTED";
		if (transactionIsolation == Connection.TRANSACTION_REPEATABLE_READ)
			return "REPEATABLE_READ";
		if (transactionIsolation == Connection.TRANSACTION_READ_COMMITTED)
			return "SERIALIZABLE";
		return "<?" + transactionIsolation + "?>";
	}	//	getTransactionIsolationInfo

	// Added WAS support
	/*
	 * Get the status ejb
	 * @param context context
	 */
	public Status getStatusEjbStub()
	{
		StatusHome staHome = null;
		Status sta = null;
		try
		{
			if (appsType.equalsIgnoreCase(Environment.APPSTYPE_IBM))
			{
				Object staHomeObj = m_iContext.lookup ("ejb/" + StatusHome.JNDI_NAME);
				staHome = (StatusHome)javax.rmi.PortableRemoteObject.narrow(
						staHomeObj, StatusHome.class);
			}
			else
			{
				staHome = (StatusHome)m_iContext.lookup(StatusHome.JNDI_NAME);
			}

			sta = staHome.create();
		}
		catch (Exception ex)
		{
			String connect = m_env.get(Context.PROVIDER_URL);
			log.warning (connect
				+ "\n - " + ex.toString ()
				+ "\n - " + m_env);
		//	log.log(Level.WARNING, "", ex);
		}

		return sta;
	}	// getStatusEjbStub()


	/**************************************************************************
	 *  Testing
	 *  @param args ignored
	 */
	public static void main (String[] args)
	{
		boolean server = true;
		if (args.length == 0)
			System.out.println("CConnection <server|client>");
		else
			server = "server".equals(args[0]);
		System.out.println("CConnection - " + (server ? "server" : "client"));
		//
		if (server)
		{
			Compiere.startup(false);
		}
		else
			Compiere.startup(true);
		//
		System.out.println ("Connection = ");
		//	CConnection[name=localhost{dev-dev1-compiere},AppsHost=localhost,AppsPort=1099,type=Oracle,DBhost=dev,DBport=1521,DBname=dev1,BQ=false,FW=false,FWhost=,FWport=1630,UID=compiere,PWD=compiere]
		System.out.println (Ini.getProperty (Ini.P_CONNECTION));

		CConnection cc = CConnection.get ();
		System.out.println (">> " + cc.toStringLong ());
	//	Connection con =
		cc.createConnection (false,
						 Connection.TRANSACTION_READ_COMMITTED);
		new CConnectionDialog(cc);
	}	//	main

}	//  CConnection
