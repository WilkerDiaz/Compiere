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
package org.compiere.startup;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.jnlp.*;

/**
 *	Compiere Environment
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class Environment extends Properties
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** AS Host						*/
	public static final String	COMPIERE_APPS_SERVER 	= "COMPIERE_APPS_SERVER";
	/** AS Type						*/
	public static final String	COMPIERE_APPS_TYPE 		= "COMPIERE_APPS_TYPE";
	/**	AS Type JBoss (default)		*/
	public static final String		APPSTYPE_JBOSS = "jboss";
	/** AS Type Tomcat only			*/
	public static final String		APPSTYPE_TOMCAT = "tomcatOnly";
	/** AS Type IBM WS				*/
	public static final String		APPSTYPE_IBM = "ibmWAS";
	/** AS Type Oracle				*/
	public static final String		APPSTYPE_ORACLE = "<oracleAS>";
	/** AS WAS Client				*/
	public static final String	COMPIERE_WAS_CLIENT 	= "COMPIERE_WAS_CLIENT";
	/** AS Web Port					*/
	public static final String	COMPIERE_WEB_PORT 		= "COMPIERE_WEB_PORT";
	/** AS Web SSL Port				*/
	public static final String	COMPIERE_SSL_PORT 		= "COMPIERE_SSL_PORT";
	/** AS RMI Port					*/
	public static final String	COMPIERE_JNP_PORT 		= "COMPIERE_JNP_PORT";
	/** AS Deployment Directory		*/
	public static final String	COMPIERE_APPS_DEPLOY 	= "COMPIERE_APPS_DEPLOY";
	/** AS Alias					*/
	public static final String	COMPIERE_WEB_ALIAS 		= "COMPIERE_WEB_ALIAS";
	
	//
	/** DB Host						*/
	public static final String	COMPIERE_DB_SERVER 		= "COMPIERE_DB_SERVER";
	/** DB Type	e.g. oracleXE		*/
	public static final String	COMPIERE_DB_TYPE 		= "COMPIERE_DB_TYPE";
	/** DB Path	e.g. oracle			*/
	public static final String	COMPIERE_DB_PATH 		= "COMPIERE_DB_PATH";
	/** DB Type PostgreSQL			*/
	public static final String		DBTYPE_PG = "postgreSQL";
	/** DB Type Oracle Std			*/
	public static final String		DBTYPE_ORACLE = "oracle";
	/** DB Type Oracle XP			*/
	public static final String		DBTYPE_ORACLEXE = "oracleXE";
	/** DB Type DB/2				*/
	public static final String		DBTYPE_DB2 = "db2";
	/** DB Type MS SQL Server		*/
	//public static final String		DBTYPE_MS = "<sqlServer>";
	public static final String		DBTYPE_MS = "sqlServer";
	/** DB Name						*/
	public static final String	COMPIERE_DB_NAME 		= "COMPIERE_DB_NAME";
	/** DB Port						*/
	public static final String	COMPIERE_DB_PORT 		= "COMPIERE_DB_PORT";
	/** DB Compiere UID				*/
	public static final String	COMPIERE_DB_USER 		= "COMPIERE_DB_USER";
	/** DB Compiere User Exists	*/
	public static final String	COMPIERE_DB_USER_EXISTS	= "COMPIERE_DB_USER_EXISTS";
	/** DB Compiere PWD				*/
	public static final String	COMPIERE_DB_PASSWORD 	= "COMPIERE_DB_PASSWORD";
	/** DB URL						*/
	public static final String	COMPIERE_DB_URL 		= "COMPIERE_DB_URL";
	/** DB System PWD 				*/
	public static final String	COMPIERE_DB_SYSTEM 		= "COMPIERE_DB_SYSTEM";
	
	/** File Name				*/
	private static final String FILENAME = "Environment.properties";

	/** Singleton				*/
	private static Environment	s_env = null;
	
	/**
	 * 	Get Environment
	 *	@return environment
	 */
	public static synchronized Environment get()
	{
		if (s_env == null)
			s_env = new Environment();
		return s_env;
	}	//	get
	
	/**
	 * 	Get JNLP CodeBase
	 *	@return code base or null
	 */
	public static URL getCodeBase()
	{
		try
		{
			BasicService bs = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService"); 
			URL url = bs.getCodeBase();
	        return url;
		} 
		catch (UnavailableServiceException ue) 
		{
			return null; 
		} 
	}	//	getCodeBase

	/**
	 * 	Get JNLP CodeBase Host
	 *	@return code base or null
	 */
	public static String getCodeBaseHost()
	{
		URL url = getCodeBase();
		if (url == null)
			return null;
		return url.getHost();
	}	//	getCodeBase

	
	/**************************************************************************
	 * 	Environment Constructor
	 */
	private Environment()
	{
		load();
	}	//	Environment
	
	/**
	 * 	Load environment
	 */
	public boolean load()
	{
		boolean loadOK = true;
		InputStream in = null;
		try
		{
			in = getClass().getResourceAsStream (FILENAME);
		//	System.out.println("Available=" + in.available());
			load(in);
			in.close();
		}
		catch (Exception e)
		{
			System.err.println("Environment.load(1) - " + e);
			loadOK = false;
		}
		catch (Throwable t)
		{
			System.err.println("Environment.load(2) - " + t);
			loadOK = false;
		}
	//	System.out.println("#" + size());
		return loadOK;
	}	//	load
	
	/**
	 * 	Set AS & DB Host 
	 *	@param hostName optional host name
	 */
	public void setHost(String hostName)
	{
		if (hostName == null || hostName.length() == 0)
		{
			try
			{
				InetAddress localhost = InetAddress.getLocalHost();
				hostName = localhost.getHostName();
			}
			catch (Exception e)
			{
				hostName = "localhost";
			}
		}
		setProperty (COMPIERE_APPS_SERVER, hostName);
		setProperty (COMPIERE_DB_SERVER, hostName);
	}	//	setHost
	
	
	/**
	 * 	Save properties
	 *	@param pp properties
	 *	@return true if saved
	 */
	public boolean save (Properties pp, String compiereHome)
	{
		clear();
		setProperty (COMPIERE_APPS_SERVER, pp.getProperty (COMPIERE_APPS_SERVER));
		setProperty (COMPIERE_JNP_PORT, pp.getProperty (COMPIERE_JNP_PORT));
		// Added WAS support
		setProperty (COMPIERE_APPS_TYPE, pp.getProperty (COMPIERE_APPS_TYPE));
		setProperty (COMPIERE_WAS_CLIENT, (pp.getProperty (COMPIERE_WAS_CLIENT) == null)?"":pp.getProperty ("COMPIERE_WAS_CLIENT"));
		setProperty (COMPIERE_WEB_PORT, pp.getProperty (COMPIERE_WEB_PORT));
		//
		setProperty (COMPIERE_DB_SERVER, pp.getProperty (COMPIERE_DB_SERVER));
		setProperty (COMPIERE_DB_NAME, pp.getProperty (COMPIERE_DB_NAME));
		setProperty (COMPIERE_DB_PATH, pp.getProperty (COMPIERE_DB_PATH));
		setProperty (COMPIERE_DB_TYPE, pp.getProperty (COMPIERE_DB_TYPE));
		setProperty (COMPIERE_DB_PORT, pp.getProperty (COMPIERE_DB_PORT));
		setProperty (COMPIERE_DB_USER, pp.getProperty (COMPIERE_DB_USER));
		setProperty (COMPIERE_DB_PASSWORD, pp.getProperty (COMPIERE_DB_PASSWORD));
		//
		boolean saveOK = true;
		String fileName = compiereHome + File.separator + FILENAME;
		File file = new File(fileName);
		try
		{
			FileOutputStream out = new FileOutputStream(file, false);
			store (out, "(C) ComPiere, Inc.");
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			System.err.println("Environment.save - " + e);
			saveOK = false;
		}
		//
		return saveOK;
	}	//	save
	
	/**
	 * 	Empty Environment
	 *	@return true if empty
	 */
	@Override
	public boolean isEmpty()
	{
		return this.size() < 2;
	}	//	isEmpty
		
}	//	Environment
