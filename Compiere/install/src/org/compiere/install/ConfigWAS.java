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
import org.compiere.startup.*;


/**
 *	WAS 6.1 Apps Server Configuration
 *	
 *  @author William Wong
 *  @version $Id: ConfigWAS.java
 */
public class ConfigWAS extends Config
{

	/**
	 * 	ConfigWAS
	 * 	@param data configuration
	 */
	public ConfigWAS(ConfigurationData data)
	{
		super (data);
	}	//	ConfigWAS
	
	/**
	 * 	Initialize
	 */
	@Override
	public void init()
	{
		//
		String rmiPort = p_data.getProperties().getProperty(Environment.COMPIERE_JNP_PORT);
		if (rmiPort != null)
			p_data.setAppsServerJNPPort(rmiPort);
		else
			p_data.setAppsServerJNPPort("2809");
		p_data.setAppsServerJNPPort(true);
		p_data.setAppsServerWebPort("9080");
		p_data.setAppsServerWebPort(true);
		p_data.setAppsServerSSLPort("9443");
		p_data.setAppsServerSSLPort(true);
		
		// Change the jnp port label
		p_data.setAppsServerJNPPortLabel(Setup.res.getString("JNDIPort"));
		
		// Change the java vm type
		p_data.setJavaType(ConfigurationData.JAVATYPE_IBM);
	}	//	init

	
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
		
		//	JNP Port
		int JNPPort = p_data.getAppsServerJNPPort();
		pass = !p_data.testPort (appsServer, JNPPort, false) 
			&& p_data.testServerPort(JNPPort);
		error = "Not correct: JNP Port = " + JNPPort;
		
		if((getPanel())!=null)
			signalOK(getPanel().okJNPPort, "ErrorJNPPort", 
					pass, true, error);
		if (!pass)
			return error;
		log.info("OK: JNPPort = " + JNPPort);
		setProperty(Environment.COMPIERE_JNP_PORT, String.valueOf(JNPPort));

		//	Web Port
		int WebPort = p_data.getAppsServerWebPort();
		pass = !p_data.testPort ("http", appsServer.getHostName(), WebPort, "/") 
			&& p_data.testServerPort(WebPort);
		error = "Not correct: Web Port = " + WebPort;
		
		if((getPanel())!=null)
			signalOK(getPanel().okWebPort, "ErrorWebPort",
					pass, true, error); 
		if (!pass)
			return error;
		log.info("OK: Web Port = " + WebPort);
		setProperty(Environment.COMPIERE_WEB_PORT, String.valueOf(WebPort));
		
		//	SSL Port
		int sslPort = p_data.getAppsServerSSLPort();
		pass = !p_data.testPort ("https", appsServer.getHostName(), sslPort, "/") 
			&& p_data.testServerPort(sslPort);
		error = "Not correct: SSL Port = " + sslPort;
		
		if((getPanel())!=null)
			signalOK(getPanel().okSSLPort, "ErrorWebPort",
					pass, true, error); 
		if (!pass)
			return error;
		log.info("OK: SSL Port = " + sslPort);
		setProperty(Environment.COMPIERE_SSL_PORT, String.valueOf(sslPort));
		//
		return null;
	}	//	test
	
}	//	ConfigWAS
