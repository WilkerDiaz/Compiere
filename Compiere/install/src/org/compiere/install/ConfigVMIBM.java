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
import org.compiere.startup.*;
import org.compiere.util.*;


/**
 *	Sun Java VM Configuration
 *	
 *  @author William Wong
 *  @version $Id: ConfigVMIBM.java
 */
public class ConfigVMIBM extends Config
{
	/**
	 * 	ConfigVMIBM
	 * 	@param data configuration
	 */
	public ConfigVMIBM (ConfigurationData data)
	{
		super (data);
	}	//	ConfigVMIBM()
	
	/**
	 * 	Init
	 */
	@Override
	public void init()
	{
		//	Java Home, e.g. D:\j2sdk1.4.1\jre
		String javaHome = System.getProperty("java.home");
		log.fine(javaHome);
		if (javaHome.endsWith("jre"))
			javaHome = javaHome.substring(0, javaHome.length()-4);
		p_data.setJavaHome(javaHome);
		
		// Change the jnp port label
		p_data.setAppsServerJNPPortLabel(Setup.res.getString("JNDIPort"));
		
		// Change the application server type 
		p_data.setAppsServerType(Environment.APPSTYPE_IBM);
	}	//	init
	
	/**
	 * 	Test
	 *	@return error message or null of OK
	 */
	@Override
	public String test()
	{
		//	Java Home
		File javaHome = new File (p_data.getJavaHome());
		boolean pass = javaHome.exists();
		String error = "Not found: Java Home";
		if(getPanel() != null)
			signalOK(getPanel().okJavaHome, "ErrorJavaHome",
					pass, true, error);
		if (!pass)
			return error;
		//	Look for tools.jar to make sure that it is not the JRE
		File tools = new File (p_data.getJavaHome() 
			+ File.separator + "lib" + File.separator + "tools.jar");
		pass = tools.exists();
		error = "Not found: Java SDK = " + tools;
		signalOK(getPanel().okJavaHome, "ErrorJavaHome",
			pass, true, error);
		if (!pass)
			return error;
		//
		if (CLogMgt.isLevelFinest())
			CLogMgt.printProperties(System.getProperties(), "System", true);
		//
		log.info("OK: JavaHome=" + javaHome.getAbsolutePath());
		setProperty(ConfigurationData.JAVA_HOME, javaHome.getAbsolutePath());
		System.setProperty(ConfigurationData.JAVA_HOME, javaHome.getAbsolutePath());
		
	/*	This won't work for ibm jdk; not sure how to check the java version ?
		//	Java Version
		final String VERSION = "1.6";
		final String VERSION2 = "1.6";	//	The real one
		pass = false;
		String jh = javaHome.getAbsolutePath();
		if (jh.indexOf(VERSION) != -1)	//	file name has version = assuming OK
			pass = true;
		if (!pass && jh.indexOf(VERSION2) != -1)	//
			pass = true;
		String thisJH = System.getProperty("java.home");
		if (thisJH.indexOf(jh) != -1)	//	we are running the version currently
		{
			String thisJV = System.getProperty("java.version");
			pass = thisJV.indexOf(VERSION) != -1;
			if (!pass && thisJV.indexOf(VERSION2) != -1)
				pass = true;
			if (pass)
			  log.info("OK: Version=" + thisJV);
		}
		error = "Wrong Java Version: Should be " + VERSION2;
		signalOK(getPanel().okJavaHome, "ErrorJavaHome",
				pass, true, error);
		if (!pass)
			return error;
		//
		 */
		setProperty(ConfigurationData.JAVA_TYPE, p_data.getJavaType());
		
		return null;
	}	//	test
	
}	//	ConfigVMIBM
