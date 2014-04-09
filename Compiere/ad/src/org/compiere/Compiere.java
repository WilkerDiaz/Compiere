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
package org.compiere;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.api.*;
import org.compiere.db.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.util.*;

/**
 *  Compiere Control Class
 *
 *  @author Jorg Janke
 *  @version $Id: Compiere.java 9436 2010-12-17 00:25:06Z freyes $
 */
public final class Compiere
{
	/** Main Version String         */
	static public final String	MAIN_VERSION	= "Release 3.6.2";
	/** Detail Version as date      Used for Client/Server		*/
	static public final String	DATE_VERSION	= "2010-12-16";
	/** Database Version as date    Compared with AD_System		*/
	static public final String	DB_VERSION		= "2010-12-16";

	/** Product Name            */
	static public final String	NAME 			= "Compiere\u00AE";
	/** URL of Product          */
	static public final String	URL				= "www.compiere.org";
	/** 16*16 Product Image.
	/** Removing/modifying the Compiere logo is a violation of the license	*/
	static private final String	s_File16x16		= "images/C16.png";
	/** 32*32 Product Image.
	/** Removing/modifying the Compiere logo is a violation of the license	*/
	static private final String	s_file32x32		= "images/C32.png";
	/** 100*30 Product Image.
	/** Removing/modifying the Compiere logo is a violation of the license	*/
	static private final String	s_fileMedium	= "images/Compiere120x30.png";
	/** 48*15 Product Image.
	/** Removing/modifying the Compiere logo is a violation of the license	*/
	static private final String	s_fileSmall		= "images/Compiere.png";
	/** Removing/modifying the Compiere logo is a violation of the license	*/
	static private final String	s_fileHR		= "images/CompiereHR.png";
	/** Support Email           */
	static private String		s_supportEmail	= "";

	/** Subtitle                */
	static public final String	SUB_TITLE		= " Smart ERP & CRM ";
	/** Compiere is a wordwide registered Trademark
	 *  - Don't modify this - Program will someday fail unexpectedly	*/
	static public final String	COMPIERE_R		= "Compiere\u00AE";
	/** Copyright Notice - Don't modify this - Program will someday fail unexpectedly
	 *  it also violates the license and you'll be held liable for any damage claims */
	static public final String	COPYRIGHT		= "\u00A9 1999-2010 Compiere \u00AE";

	static private String		s_ImplementationVersion = null;
	static private String		s_ImplementationVendor = null;

	static private Image 		s_image16;
	static private Image 		s_image48x15;
	static private Image 		s_imageLogo;
	static private ImageIcon 	s_imageIcon32;
	static private ImageIcon 	s_imageIconLogo;

	/**	Logging								*/
	private static CLogger		log = null;

	/**
	 *  Get Product Name
	 *  @return Application Name
	 */
	public static String getName()
	{
		return NAME;
	}   //  getName

	/**
	 *  Get Product Version
	 *  @return Application Version
	 */
	public static String getVersion()
	{
		return MAIN_VERSION + " - " + DATE_VERSION;
	}   //  getVersion

	/**
	 *	Short Summary (Windows)
	 *  @return summary
	 */
	public static String getSum()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(NAME).append(" ").append(MAIN_VERSION).append(SUB_TITLE);
		return sb.toString();
	}	//	getSum

	/**
	 *	Summary (Windows).
	 * 	Removing/modifying the Compiere copyright notice is a violation of the license
	 *	Compiere(tm) Version 2.5.1a_2004-03-15 - Smart ERP & CRM - Copyright (c) 1999-2005 Jorg Janke; Implementation: 2.5.1a 20040417-0243 - (C) 1999-2005 Jorg Janke, ComPiere Inc. USA
	 *  @return Summary in Windows character set
	 */
	public static String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(NAME).append(" ")
			.append(MAIN_VERSION).append("_").append(DATE_VERSION)
			.append(" -").append(SUB_TITLE)
			.append("- ").append(COPYRIGHT)
			.append("; Implementation: ").append(getImplementationVersion())
			.append(" - ").append(getImplementationVendor());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Set Package Info
	 */
	private static void setPackageInfo()
	{
		if (s_ImplementationVendor != null)
			return;

		Package compierePackage = Package.getPackage("org.compiere");
		s_ImplementationVendor = compierePackage.getImplementationVendor();
		s_ImplementationVersion = compierePackage.getImplementationVersion();
		if (s_ImplementationVendor == null)
		{
			s_ImplementationVendor = "not supported";
			s_ImplementationVersion = "unknown";
		}
	}	//	setPackageInfo

	/**
	 * 	Get Jar Implementation Version
	 * 	@return Implementation-Version
	 */
	public static String getImplementationVersion()
	{
		if (s_ImplementationVersion == null)
			setPackageInfo();
		return s_ImplementationVersion;
	}	//	getImplementationVersion

	/**
	 * 	Get Jar Implementation Vendor
	 * 	@return Implementation-Vendor
	 */
	public static String getImplementationVendor()
	{
		if (s_ImplementationVendor == null)
			setPackageInfo();
		return s_ImplementationVendor;
	}	//	getImplementationVendor

	/**
	 *  Get Checksum
	 *  @return checksum
	 */
	public static int getCheckSum()
	{
		return getSum().hashCode();
	}   //  getCheckSum

	/**
	 *	Summary in ASCII
	 *  @return Summary in ASCII
	 */
	public static String getSummaryAscii()
	{
		String retValue = getSummary();
		//  Registered Trademark
		retValue = Util.replace(retValue, "\u00AE", "(r)");
		//  Trademark
		retValue = Util.replace(retValue, "\u2122", "(tm)");
		//  Copyright
		retValue = Util.replace(retValue, "\u00A9", "(c)");
		//  Cr
		retValue = Util.replace(retValue, Env.NL, " ");
		retValue = Util.replace(retValue, "\n", " ");
		return retValue;
	}	//	getSummaryAscii

	/**
	 * 	Get Environment Info
	 *	@param isClient client
	 *	@param info additional info
	 *	@return info
	 */
	public static String getEnvInfo(boolean isClient, String info)
	{
		StringBuffer sb = new StringBuffer();
		if (info != null && info.length() > 0)
			sb.append(info).append(" ");
		else if (isClient)
			sb.append("Client ");
		else
			sb.append("Server ");
		try
		{
			InetAddress local = InetAddress.getLocalHost();
			sb.append(local).append(": ");
		}
		catch (Exception e)
		{
		}
		sb.append(getCompiereHome())
			.append(" - ").append(getJavaInfo())
			.append(" - ").append(getOSInfo());
		return sb.toString();
	}	//	getEnvInfo

	/**
	 * 	Get Java VM Info
	 *	@return VM info
	 */
	public static String getJavaInfo()
	{
		return System.getProperty("java.vm.name")
			+ " " + System.getProperty("java.version");
	}	//	getJavaInfo

	/**
	 * 	Get Operating System Info
	 *	@return OS info
	 */
	public static String getOSInfo()
	{
		return System.getProperty("os.name") + " "
			+ System.getProperty("os.version") + " "
			+ System.getProperty("sun.os.patch.level");
	}	//	getJavaInfo

	/**
	 *  Get full URL
	 *  @return URL
	 */
	public static String getURL()
	{
		return "http://" + URL;
	}   //  getURL

	/**
	 *  Get Sub Title
	 *  @return Subtitle
	 */
	public static String getSubtitle()
	{
		return SUB_TITLE;
	}   //  getSubitle

	/**
	 *  Get 16x16 Image.
	 *	Removing/modifying the Compiere logo is a violation of the license
	 *  @return Image Icon
	 */
	public static Image getImage16()
	{
		if (s_image16 == null)
		{
			Toolkit tk = Toolkit.getDefaultToolkit();
			URL url = Compiere.class.getResource(s_File16x16);
		//	System.out.println(url);
			if (url == null)
				return null;
			s_image16 = tk.getImage(url);
		}
		return s_image16;
	}   //  getImage16

	/**
	 *  Get 28*15 Logo Image.
	 *  @param hr high resolution
	 *  @return Image Icon
	 */
	public static Image getImageLogoSmall(boolean hr)
	{
		if (s_image48x15 == null)
		{
			Toolkit tk = Toolkit.getDefaultToolkit();
			URL url = null;
			if (hr)
				url = org.compiere.Compiere.class.getResource(s_fileHR);
			else
				url = org.compiere.Compiere.class.getResource(s_fileSmall);
		//	System.out.println(url);
			if (url == null)
				return null;
			s_image48x15 = tk.getImage(url);
		}
		return s_image48x15;
	}   //  getImageLogoSmall

	/**
	 *  Get Small Logo Image.
	 *  @return Image Logo
	 */
	public static Image getImageLogo()
	{
		if (s_imageLogo == null)
		{
			Toolkit tk = Toolkit.getDefaultToolkit();
			URL url = org.compiere.Compiere.class.getResource(s_fileMedium);
		//	System.out.println(url);
			if (url == null)
				return null;
			s_imageLogo = tk.getImage(url);
		}
		return s_imageLogo;
	}   //  getImageLogo

	/**
	 *  Get 32x32 ImageIcon.
	 *	Removing/modifying the Compiere logo is a violation of the license
	 *  @return Image Icon
	 */
	public static ImageIcon getImageIcon32()
	{
		if (s_imageIcon32 == null)
		{
			URL url = org.compiere.Compiere.class.getResource(s_file32x32);
		//	System.out.println(url);
			if (url == null)
				return null;
			s_imageIcon32 = new ImageIcon(url);
		}
		return s_imageIcon32;
	}   //  getImageIcon32

	/**
	 *  Get Medium ImageIcon.
	 *	Removing/modifying the Compiere logo is a violation of the license
	 *  @return Image Icon
	 */
	public static ImageIcon getImageIconLogo()
	{
		if (s_imageIconLogo == null)
		{
			URL url = org.compiere.Compiere.class.getResource(s_fileMedium);
		//	System.out.println(url);
			if (url == null)
				return null;
			s_imageIconLogo = new ImageIcon(url);
		}
		return s_imageIconLogo;
	}   //  getImageIconLogo

	/**
	 *  Get default (Home) directory
	 *  @return Home directory
	 */
	public static String getCompiereHome()
	{
		//  Try Environment
		String retValue = Ini.getCompiereHome();
		//	Look in current Directory
		if (retValue == null && System.getProperty("user.dir").indexOf("Compiere2") != -1)
		{
			retValue = System.getProperty("user.dir");
			int pos = retValue.indexOf("Compiere2");
			retValue = retValue.substring(pos+9);
		}
		if (retValue == null)
			retValue = File.separator + "Compiere2";
		return retValue;
	}   //  getHome

	/**
	 *  Get Support Email
	 *  @return Support mail address
	 */
	public static String getSupportEMail()
	{
		return s_supportEmail;
	}   //  getSupportEMail

	/**
	 *  Set Support Email
	 *  @param email Support mail address
	 */
	public static void setSupportEMail(String email)
	{
		s_supportEmail = email;
	}   //  setSupportEMail

	/**
	 * 	Startup - compatibility - for Test
	 *	@param isClient client session
	 *  @return successful startup
	 */
	public static synchronized boolean startup (boolean isClient)
	{
		return startup(isClient, null);
	}	//	startup

	/**
	 * 	Startup Environment - compatibility
	 *	@param isClient
	 *  @return successful startup
	 *	@deprecated
	 */
	@Deprecated
	public static synchronized boolean startupEnvironment (boolean isClient)
	{
		return startupEnvironment(isClient, null);
	}	//	startupEnvironment

	/*************************************************************************
	 *  Startup Client/Server.
	 *  - Print greeting,
	 *  - Check Java version and
	 *  - load Ini parameters
	 *  If it is a client, load/set PLAF and exit if error.
	 *  If Client, you need to call startupEnvironment explicitly!
	 * 	For testing call method startupEnvironment
	 *	@param isClient true for client
	 *  @param isUserProperty true if we should use the Compiere.properties
               in the user home directory, false if we should use the one in
               the compiere home directory
	 *	@param info info
	 *  @return successful startup
	 */
	public static synchronized boolean startup (boolean isClient, String info)
	{
		return startup(isClient, isClient, info);
	}

	public static synchronized boolean startup (boolean isClient, boolean isUserProperty, String info)
	{
		//	Already started
		if (log != null)
			return true;

		//	Check Version
		if (!Login.isJavaOK(isClient) && isClient)
			System.exit(1);

		CLogMgt.initialize(isClient);
		Ini.setClient (isUserProperty);		//	Ini requires Logging
		//	Init Log
		log = CLogger.getCLogger(Compiere.class);
		//	Greeting
		log.info(getSummaryAscii());
		log.info(getEnvInfo(isUserProperty, info));

		//  Load System environment
	//	EnvLoader.load(Ini.ENV_PREFIX);

		//  System properties
		Ini.loadProperties (false);

		//	Set up Log
		CLogMgt.setLevel(Ini.getProperty(Ini.P_TRACELEVEL));
		if (isClient && Ini.isPropertyBool(Ini.P_TRACEFILE)
			&& CLogFile.get(false, null, isClient) == null)
		{
			System.out.println( "Adding log handler: " + Ini.findCompiereHome() );
			CLogMgt.addHandler(CLogFile.get (true, Ini.findCompiereHome(), isClient));
		}

		//	Set UI
		if (isClient)
		{
			if (CLogMgt.isLevelAll())
				log.log(Level.FINEST, System.getProperties().toString());
			//
			CompiereTheme.load();
			CompierePLAF.setPLAF (null);
			Thread.yield();
		}

		//  Set Default Database Connection from Ini
		DB.setDBTarget(CConnection.get());

		if (isClient)		//	don't test connection
			return false;	//	need to call

		return startupEnvironment(isClient, info);
	}   //  startup

	/**
	 * 	Startup Compiere Environment.
	 * 	Automatically called for Server connections
	 * 	For testing call this method.
	 *	@param isClient true if client connection
	 *  @return successful startup
	 */
	public static boolean startupEnvironment (boolean isClient, String info)
	{
		startup(isClient, info);		//	returns if already initiated
		if (!DB.isConnected())
		{
			log.severe ("No Database");
			return false;
		}
		MSystem system = MSystem.get(Env.getCtx());	//	Initializes Base Context too
		if (system == null)
			return false;

		//	Initialize main cached Singletons
		ModelValidationEngine.get();
		try
		{
			String className = system.getEncryptionKey();
			if (className == null || className.length() == 0)
			{
				className = System.getProperty(SecureInterface.COMPIERE_SECURE);
				if (className != null && className.length() > 0
					&& !className.equals(SecureInterface.COMPIERE_SECURE_DEFAULT))
				{
					SecureEngine.init(className);	//	test it
					system.setEncryptionKey(className);
					system.save();
				}
			}
			SecureEngine.init(className);

			//
			if (isClient)
				MClient.get(Env.getCtx(),0);			//	Login Client loaded later
			else
				MClient.getAll(Env.getCtx());
			//Document.setKey(system.getSummary());
		}
		catch (Exception e)
		{
			log.warning("Environment problems: " + e.toString());
		}

		/**	Start Workflow Document Manager (in other package) for PO	*/
		String className = null;
		try
		{
			className = "org.compiere.wf.DocWorkflowManager";
			Class.forName(className);
			//	Initialize Archive Engine
			className = "org.compiere.print.ArchiveEngine";
			Class.forName(className);
		}
		catch (Exception e)
		{
			log.warning("Not started: " + className + " - " + e.getMessage());
		}
		/**	**/
		if (!isClient)
			DB.updateMail();
		Env.addSystemShutdownHook();
		return true;
	}	//	startupEnvironment


	/**
	 *  Main Method
	 *
	 *  @param args optional start class
	 */
	public static void main (String[] args)
	{
		Splash.getSplash();
		startup(true, null);     //  error exit and initUI

		//  Start with class as argument - or if nothing provided with Client
		String className = "org.compiere.apps.AMenu";
		for (int i = 0; i < args.length; i++)
		{
			if (!args[i].equals("-debug"))  //  ignore -debug
			{
				className = args[i];
				break;
			}
		}
		//
		try
		{
			Class<?> startClass = Class.forName(className);
			startClass.newInstance();
		}
		catch (Exception e)
		{
			System.err.println("Compiere starting: " + className + " - " + e.toString());
			e.printStackTrace();
		}
	}   //  main
}	//	Compiere
