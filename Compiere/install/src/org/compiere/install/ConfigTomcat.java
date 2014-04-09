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

import java.io.*;
import java.net.*;
import org.compiere.startup.*;

/**
 * 	Tomcat 5.5.9 Configuration
 *	
 *  @author Jorg Janke
 *  @version $Id: ConfigTomcat.java 7904 2009-07-20 22:22:01Z freyes $
 */
public class ConfigTomcat extends Config
{
	/**
	 * 	ConfigJBoss
	 * 	@param data configuration
	 */
	public ConfigTomcat (ConfigurationData data)
	{
		super (data);
	}	//	ConfigTomcat
	
	/**
	 * 	Initialize
	 */
	@Override
	public void init()
	{
		p_data.setAppsServerDeployDir(getDeployDir());
		p_data.setAppsServerDeployDir(true);
		//
		p_data.setAppsServerJNPPort("1099");
		p_data.setAppsServerJNPPort(false);
		//
		p_data.setAppsServerWebPort("80");
		p_data.setAppsServerWebPort(true);
		p_data.setAppsServerSSLPort("443");
		p_data.setAppsServerSSLPort(true);
	}	//	init

	/**
	 * 	Get Notes
	 *	@return notes
	 */
	public String getNotes()
	{
		return "Compiere requires Tomcat 5.5.9"
			+ "\nPlease set the Web Port in $CATALINA_HOME//conf//server.xml"
			//	C:\Program Files\Apache Software Foundation\Tomcat 5.5\conf\server.xml
			+ "\n";
	}	//	getNotes
	
	
	/**
	 * 	Get Deployment Dir
	 *	@return deployment dir
	 */
	private String getDeployDir()
	{
		return "C:"
			+ File.separator + "Program Files"
			+ File.separator + "Apache Software Foundation"
			+ File.separator + "Tomcat 5.5"; 
	}	//	getDeployDir
	
	/**
	 * 	Test
	 *	@return error message or null if OK
	 */
	@Override
	public String test()
	{
		//	AppsServer
		String server = p_data.getAppsServer();
		boolean pass = server != null && server.length() > 0
			&& server.toLowerCase().indexOf("localhost") == -1
			&& !server.equals("127.0.0.1");
		InetAddress appsServer = null;
		String error = "Not correct: AppsServer = " + server; 
		try
		{
			if (pass)
				appsServer = InetAddress.getByName(server);
		}
		catch (Exception e)
		{
			error += " - " + e.getMessage();
			pass = false;
		}
		if((getPanel())!=null)
			signalOK(getPanel().okAppsServer, "ErrorAppsServer",
					pass, true, error); 
		if (!pass)
			return error;
		log.info("OK: AppsServer = " + appsServer);
		setProperty(Environment.COMPIERE_APPS_SERVER, appsServer.getHostName());
		setProperty(Environment.COMPIERE_APPS_TYPE, p_data.getAppsServerType());

		//	Deployment Dir
		File deploy = new File (p_data.getAppsServerDeployDir());
		pass = deploy.exists();
		error = "CATALINA_HOME Not found: " + deploy;
		if((getPanel())!=null)
			signalOK(getPanel().okDeployDir, "ErrorDeployDir", 
					pass, true, error);
		if (!pass)
			return error;
		setProperty(Environment.COMPIERE_APPS_DEPLOY, p_data.getAppsServerDeployDir());
		log.info("OK: Deploy Directory = " + deploy);

		String baseDir = p_data.getAppsServerDeployDir();
		if (!baseDir.endsWith(File.separator))
			baseDir += File.separator;
		//	Need to have /shared/lib
		String sharedLib = baseDir + "shared" + File.separator + "lib";
		File sharedLibDir = new File (sharedLib);
		pass = sharedLibDir.exists();
		error = "Not found (shared library): " + sharedLib;
		if((getPanel())!=null)
			signalOK(getPanel().okDeployDir, "ErrorDeployDir", 
					pass, true, error);
		if (!pass)
			return error;

		//	Need to have /webapps
		String webApps = baseDir + "webapps";
		File webAppsDir = new File (webApps);
		pass = webAppsDir.exists();
		error = "Not found (webapps): " + sharedLib;
		signalOK(getPanel().okDeployDir, "ErrorDeployDir", 
			pass, true, error);
		if (!pass)
			return error;
		//
		return null;
	}	//	test

}	//	ConfigTomcat
