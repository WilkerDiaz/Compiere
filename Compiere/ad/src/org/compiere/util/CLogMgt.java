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
package org.compiere.util;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.jar.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.db.*;
import org.compiere.model.*;

import com.compiere.client.*;
import com.qoppa.pdf.*;


/**
 *	Compiere Log Management.
 *	
 *  @author Jorg Janke
 *  @version $Id: CLogMgt.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CLogMgt
{
	/**
	 * 	Initialize Logging
	 * 	@param isClient client
	 */
	public static synchronized void initialize(boolean isClient)
	{
		if (s_handlers != null)
			return;
		s_handlers = new ArrayList<Handler>();

		/**
		Class<?> clazz = CLogMgt.class;
		System.err.println(clazz.getClassLoader().toString());
		System.err.println(clazz.toString() + " @" + Integer.toHexString(clazz.hashCode()));
		System.err.println(Runtime.getRuntime().toString());
		System.err.println(Thread.currentThread().toString());
		System.err.println("Handlers=" + s_handlers.hashCode());
		Thread.dumpStack();
		**/
		
		LogManager mgr = LogManager.getLogManager();
		try 
		{	//	Load Logging config from org.compiere.util.*properties
			String fileName = "logClient.properties";
			if (!isClient)
				fileName = "logServer.properties";
			InputStream in = CLogMgt.class.getResourceAsStream(fileName);
			BufferedInputStream bin = new BufferedInputStream(in);
			mgr.readConfiguration(bin);
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//	Create Handler List
		try
		{
			Logger rootLogger = Logger.getLogger("");
		//	System.out.println(rootLogger.getName() + " (" + rootLogger + ")");
			Handler[] handlers = rootLogger.getHandlers();
			for (int i = 0; i < handlers.length; i ++)
			{
			//	System.out.println("  > " + handlers[i]);
				if (!s_handlers.contains(handlers[i]))
					s_handlers.add(handlers[i]);
			}
			/** *
			Enumeration en = mgr.getLoggerNames();
			while (en.hasMoreElements())
			{
				Logger lll = Logger.getLogger(en.nextElement().toString());
				System.out.println(lll.getName() + " (" + lll + ")");
			//	System.out.println("- level=" + lll.getLevel());
			//	System.out.println("- parent=" + lll.getParent() + " - UseParentHandlers=" + lll.getUseParentHandlers());
			//	System.out.println("- filter=" + lll.getFilter());
				handlers = lll.getHandlers();
			//	System.out.println("- handlers=" + handlers.length);
				for (int i = 0; i < handlers.length; i ++)
				{
					System.out.println("  > " + handlers[i]);
					if (!s_handlers.contains(handlers[i]))
						s_handlers.add(handlers[i]);
				}
				//	System.out.println();
			}
			/** **/
		}
		catch (Exception e)
		{
			if (e instanceof ClassNotFoundException)	//	WebStart
				;
			/**
			Can't load log handler "org.compiere.util.CLogConsole"
			java.lang.ClassNotFoundException: org.compiere.util.CLogConsole
			java.lang.ClassNotFoundException: org.compiere.util.CLogConsole
				at java.net.URLClassLoader$1.run(Unknown Source)
				at java.security.AccessController.doPrivileged(Native Method)
				at java.net.URLClassLoader.findClass(Unknown Source)
				at java.lang.ClassLoader.loadClass(Unknown Source)
				at sun.misc.Launcher$AppClassLoader.loadClass(Unknown Source)
				at java.lang.ClassLoader.loadClass(Unknown Source)
				at java.util.logging.LogManager$7.run(Unknown Source)
				at java.security.AccessController.doPrivileged(Native Method)
				at java.util.logging.LogManager.initializeGlobalHandlers(Unknown Source)
				at java.util.logging.LogManager.access$900(Unknown Source)
				at java.util.logging.LogManager$RootLogger.getHandlers(Unknown Source)
				at org.compiere.util.CLogMgt.initialize(CLogMgt.java:67)
				at org.compiere.Compiere.startup(Compiere.java:389)
				at org.compiere.Compiere.main(Compiere.java:500)
				at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
				at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
				at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
				at java.lang.reflect.Method.invoke(Unknown Source)
				at com.sun.javaws.Launcher.executeApplication(Unknown Source)
				at com.sun.javaws.Launcher.executeMainClass(Unknown Source)
				at com.sun.javaws.Launcher.continueLaunch(Unknown Source)
				at com.sun.javaws.Launcher.handleApplicationDesc(Unknown Source)
				at com.sun.javaws.Launcher.handleLaunchFile(Unknown Source)
				at com.sun.javaws.Launcher.run(Unknown Source)
				at java.lang.Thread.run(Unknown Source)
			**/
			else
				System.err.println(e.toString());
		}
	//	System.out.println("Handlers=" + s_handlers.size());
		
		//	Check Loggers
		if (CLogErrorBuffer.get(false) == null)
			addHandler(CLogErrorBuffer.get(true));
		if (CLogConsole.get(false) == null && isClient)
			addHandler(CLogConsole.get(true));
		CLogFile fh = CLogFile.get (false, null, isClient); 
		if (fh == null && !isClient)
		{
			fh = CLogFile.get (true, null, isClient);
			addHandler(fh);
		}
		if (fh != null && !isClient)
			System.out.println(fh);
		
		setFormatter(CLogFormatter.get());
		setFilter(CLogFilter.get());
	//	setLevel(s_currentLevel);
	//	setLoggerLevel(Level.ALL, null);
		//
		CLogMgtLog4J.initialize(isClient);
	//	System.out.println("Handlers=" + s_handlers.size() + ", Level=" + s_currentLevel);
		
		//	Overwrite Logger
		Evaluator.log = CLogger.getCLogger(Evaluator.class);
		Ctx.log = CLogger.getCLogger(Ctx.class);
	}	//	initialize

	
	/** Handlers			*/
	private static ArrayList<Handler>	s_handlers = null;
	/** Current Log Level	*/
	private static Level				s_currentLevel = Level.INFO;

	/** Logger				*/
	private static Logger		log = Logger.getAnonymousLogger();
	/** LOG Levels			*/
	public static final Level[]	LEVELS = new Level[] 
		{Level.OFF, Level.SEVERE, Level.WARNING, Level.INFO,
		Level.CONFIG, Level.FINE, Level.FINER, Level.FINEST, Level.ALL};
	
	/** New Line			*/
	private static final String NL = System.getProperty("line.separator");

	/**
	 * 	Get Handlers
	 *	@return handlers
	 */
	protected static Handler[] getHandlers()
	{
		Handler[] handlers = new Handler[s_handlers.size()];
		for (int i = 0; i < s_handlers.size(); i++)
			handlers[i] = s_handlers.get(i);
		return handlers;
	}	//	getHandlers
	
	/**
	 * 	Add Handler (to root logger)
	 *	@param handler new Handler
	 */
	public static void addHandler(Handler handler)
	{
		if (handler == null)
			return;
		Logger rootLogger = Logger.getLogger("");
		rootLogger.addHandler(handler);
		//
		s_handlers.add(handler);
		log.log(Level.CONFIG, "Handler=" + handler);
	}	//	addHandler
	
	
	/**
	 * 	Set Formatter for all handlers
	 *	@param formatter formatter
	 */
	protected static void setFormatter (java.util.logging.Formatter formatter)
	{
		for (int i = 0; i < s_handlers.size(); i++)
		{
			Handler handler = s_handlers.get(i);
			handler.setFormatter(formatter);
		}
		log.log(Level.CONFIG, "Formatter=" + formatter);
	}	//	setFormatter

	/**
	 * 	Set Filter for all handlers
	 *	@param filter filter
	 */
	protected static void setFilter (Filter filter)
	{
		for (int i = 0; i < s_handlers.size(); i++)
		{
			Handler handler = s_handlers.get(i);
			handler.setFilter(filter);
		}
		log.log(Level.CONFIG, "Filter=" + filter);
	}	//	setFilter

	/**
	 * 	Set Level for all Loggers
	 *	@param level log level
	 *	@param loggerNamePart optional partial class/logger name
	 */
	public static void setLoggerLevel (Level level, String loggerNamePart)
	{
		if (level == null)
			return;
		LogManager mgr = LogManager.getLogManager();
		Enumeration<String> en = mgr.getLoggerNames();
		while (en.hasMoreElements())
		{
			String name = en.nextElement();
			if (loggerNamePart == null 
				|| name.indexOf(loggerNamePart) != -1)
			{
				Logger lll = Logger.getLogger(name);
				lll.setLevel(level);
			}
		}
	}	//	setLoggerLevel

	/**
	 * 	Set Level for all handlers
	 *	@param level log level
	 */
	public static void setLevel (Level level)
	{
		if (level == null)
			return;
		if (s_handlers == null)
			initialize(true);
		//
		for (int i = 0; i < s_handlers.size(); i++)
		{
			Handler handler = s_handlers.get(i);
			handler.setLevel(level);
		}
		//	JDBC if ALL
		setJDBCDebug(s_currentLevel.intValue() == Level.ALL.intValue());
		//
		if (level.intValue() != s_currentLevel.intValue())
		{
			setLoggerLevel(level, null);
			log.config(level.toString());
		}
		s_currentLevel = level;
	}	//	setHandlerLevel

	/**
	 * 	Set Level
	 *	@param intLevel integer value of level
	 */
	public static void setLevel (int intLevel)
	{
		setLevel(String.valueOf(intLevel));
	}	//	setLevel
	
	/**
	 * 	Set Level
	 *	@param levelString string representation of level
	 */
	public static void setLevel (String levelString)
	{
		if (levelString == null)
			return;
		//
		for (Level element : LEVELS) 
		{
		    if (element.getName().equals(levelString)) 
		    {
		    	setLevel(element);
		    	return;
		    }
		}
		log.log(Level.CONFIG, "Ignored: " + levelString);
	}	//	setLevel

	/**
	 * 	Set JDBC Debug
	 *	@param enable
	 */
	public static void setJDBCDebug(boolean enable)
	{
		if (enable)
			DriverManager.setLogWriter(new PrintWriter(System.err));
		else
			DriverManager.setLogWriter(null);
	}	//	setJDBCDebug
	
	/**
	 * 	Get logging Level of handlers
	 *	@return logging level
	 */
	public static Level getLevel()
	{
		return s_currentLevel;
	}	//	getLevel
	
	/**
	 * 	Get logging Level of handlers
	 *	@return logging level
	 */
	public static int getLevelAsInt()
	{
		return s_currentLevel.intValue();
	}	//	getLevel
	
	/**
	 * 	Is Logging Level logged
	 *	@param level level
	 *	@return true if it is logged
	 */
	public static boolean isLevel (Level level)
	{
		if (level == null)
			return false;
		return level.intValue() >= s_currentLevel.intValue(); 
	}	//	isLevel
	
	/**
	 * 	Is Logging Level FINEST logged
	 *	@return true if it is logged
	 */
	public static boolean isLevelAll ()
	{
		return Level.ALL.intValue() == s_currentLevel.intValue(); 
	}	//	isLevelFinest

	/**
	 * 	Is Logging Level FINEST logged
	 *	@return true if it is logged
	 */
	public static boolean isLevelFinest ()
	{
		return Level.FINEST.intValue() >= s_currentLevel.intValue(); 
	}	//	isLevelFinest
	
	/**
	 * 	Is Logging Level FINER logged
	 *	@return true if it is logged
	 */
	public static boolean isLevelFiner ()
	{
		return Level.FINER.intValue() >= s_currentLevel.intValue(); 
	}	//	isLevelFiner
	
	/**
	 * 	Is Logging Level FINE logged
	 *	@return true if it is logged
	 */
	public static boolean isLevelFine ()
	{
		return Level.FINE.intValue() >= s_currentLevel.intValue(); 
	}	//	isLevelFine

	/**
	 * 	Is Logging Level INFO logged
	 *	@return true if it is logged
	 */
	public static boolean isLevelInfo ()
	{
		return Level.INFO.intValue() >= s_currentLevel.intValue(); 
	}	//	isLevelFine

	/**
	 * 	Enable/Disable logging (of handlers)
	 *	@param enableLogging true if logging enabled
	 */
	public static void enable (boolean enableLogging)
	{
		if (enableLogging)
			setLevel(s_currentLevel);
		else
		{
			Level level = s_currentLevel;
			setLevel(Level.OFF);
			s_currentLevel = level;
		}
	}	//	enable
	

	
	/**
	 * 	Shutdown Logging system
	 */
	public static void shutdown ()
	{
		LogManager mgr = LogManager.getLogManager();
		mgr.reset();
	}	//	shutdown
	
	
	/**
	 *  Print Properties
	 *
	 *  @param p Properties to print
	 *  @param description Description of properties
	 *  @param logIt if true write to Log (Level.Config), else to System.out
	 */
	public static void printProperties (Properties p, String description, boolean logIt)
	{
		if (p == null)
			return;
		if (logIt)
			log.info(description + " - Size=" + p.size()
				+ ", Hash=" + p.hashCode() + "\n" + getLocalHost());
		else
			System.out.println("Log.printProperties = " + description + ", Size=" + p.size()
				+ ", Hash=" + p.hashCode() + "\n" + getLocalHost());

		Object[] pp = p.keySet().toArray();
		Arrays.sort(pp);
		for (Object element : pp) {
			String key = element.toString();
			String value = p.getProperty(key);
			if (logIt)
				log.config(key + "=" + value);
			else
				System.out.println("  " + key + " = " + value);
		}
	}   //  printProperties

	
	/**
	 *  Get Compiere System Info
	 *  @param sb buffer to append or null
	 *  @return Info as multiple Line String
	 */
	public static StringBuffer getInfo (Ctx ctx, StringBuffer sb)
	{
		if (sb == null)
			sb = new StringBuffer();
		final String eq = " = ";
		sb.append(Compiere.NAME)
			.append(" ").append(Compiere.MAIN_VERSION).append("_").append(Compiere.DATE_VERSION)
			.append(" Implementation:").append(Compiere.getImplementationVersion()).append(NL);
		sb.append(getMsg("Host")).append(eq)        .append(getServerInfo()).append(NL);
		sb.append(getMsg("Database")).append(eq)    .append(getDatabaseInfo()).append(NL);
		sb.append(getMsg("Schema")).append(eq)      .append(CConnection.get().getDbUid()).append(NL);
		//
		sb.append(getMsg("AD_User_ID")).append(eq)  .append(ctx.getContext("#AD_User_Name")).append(NL);
		sb.append(getMsg("AD_Role_ID")).append(eq)  .append(ctx.getContext("#AD_Role_Name")).append(NL);
		//
		sb.append(getMsg("AD_Client_ID")).append(eq).append(ctx.getContext("#AD_Client_Name")).append(NL);
		sb.append(getMsg("AD_Org_ID")).append(eq)   .append(ctx.getContext("#AD_Org_Name")).append(NL);
		//
		sb.append(getMsg("Date")).append(eq)        .append(ctx.getContext("#Date")).append(NL);
		sb.append(getMsg("Printer")).append(eq)     .append(ctx.getPrinterName()).append(NL);
		//
		Manifest mf = ZipUtil.getManifest("CClient.jar");
		if (mf == null)
			mf = ZipUtil.getManifest("CTools.jar");
		if (mf != null)
		{
			Attributes atts = mf.getMainAttributes();
			if (atts != null)
			{
				Iterator<?> it = atts.keySet().iterator();
				while (it.hasNext())
				{
					Object key = it.next();
					if (key.toString().startsWith("Impl") || key.toString().startsWith("Spec"))
						sb.append(key).append(eq).append(atts.get(key)).append(NL);
				}
			}
		}
		sb.append("CompiereHome = ").append(Compiere.getCompiereHome()).append(NL);
		sb.append(Env.getLanguage(Env.getCtx())).append(NL);
		MClient client = MClient.get(Env.getCtx());
		sb.append(client).append(NL);
		sb.append(getMsg("IsMultiLingualDocument"))
			.append(eq).append(client.isMultiLingualDocument()).append(NL);
		sb.append("BaseLanguage = ").append(Env.isBaseLanguage(Env.getCtx(), "AD_Window"))
			.append("/").append(Env.isBaseLanguage(Env.getCtx(), "C_UOM")).append(NL);
		sb.append(SysEnv.getInfo(null)).append(NL);
		sb.append("PDF License=").append(Document.isLicensed()).append(NL); 
		sb.append(Compiere.getJavaInfo()).append(NL);
		sb.append(Compiere.getOSInfo());
		//
		return sb;
	}   //  getInfo

	/**
	 *  Create System Info
	 *  @param sb Optional string buffer
	 *  @param ctx Environment
	 *  @return System Info
	 */
	public static StringBuffer getInfoDetail (StringBuffer sb, Ctx ctx)
	{
		if (sb == null)
			sb = new StringBuffer();
		if (ctx == null)
			ctx = Env.getCtx();
		//  Envoronment
		CConnection cc = CConnection.get();
		sb.append(NL)
			.append("=== Environment === ")
			.append(Compiere.getCheckSum()).append(NL)
			.append(Compiere.getSummaryAscii()).append(NL)
			.append(getLocalHost()).append(NL)
			.append(cc.getInfo()).append(NL);
		//  Context
		sb.append(NL)
			.append("=== Context ===").append(NL);
		String[] context = ctx.getEntireContext();
		for (String element : context)
			sb.append(element).append(NL);
		//  System
		sb.append(NL)
			.append("=== System ===").append(NL);
		Object[] pp = System.getProperties().keySet().toArray();
		Arrays.sort(pp);
		for (Object element : pp) {
			String key = element.toString();
			String value = System.getProperty(key);
			sb.append(key).append("=").append(value).append(NL);
		}
		return sb;
	}   //  getInfoDetail

	/**
	 *  Get Basic Compiere System Info. 
	 *  It does not include the database and apps server information.
	 *  @param sb buffer to append or null
	 *  @return Info as multiple Line String
	 */
	public static StringBuffer getInfoBasic (Ctx ctx, StringBuffer sb)
	{
		if (sb == null)
			sb = new StringBuffer();
		final String eq = " = ";
		sb.append(Compiere.NAME)
			.append(" ").append(Compiere.MAIN_VERSION).append("_").append(Compiere.DATE_VERSION)
			.append(" Implementation:").append(Compiere.getImplementationVersion()).append(NL);
		sb.append(getMsg("AD_User_ID")).append(eq)  .append(ctx.getContext("#AD_User_Name")).append(NL);
		sb.append(getMsg("AD_Role_ID")).append(eq)  .append(ctx.getContext("#AD_Role_Name")).append(NL);
		sb.append(getMsg("AD_Client_ID")).append(eq).append(ctx.getContext("#AD_Client_Name")).append(NL);
		sb.append(getMsg("AD_Org_ID")).append(eq)   .append(ctx.getContext("#AD_Org_Name")).append(NL);
		sb.append(getMsg("Date")).append(eq)        .append(ctx.getContext("#Date")).append(NL);
		sb.append(getMsg("Printer")).append(eq)     .append(ctx.getPrinterName()).append(NL);
		Manifest mf = ZipUtil.getManifest("CClient.jar");
		if (mf == null)
			mf = ZipUtil.getManifest("CTools.jar");
		if (mf != null)
		{
			Attributes atts = mf.getMainAttributes();
			if (atts != null)
			{
				Iterator<?> it = atts.keySet().iterator();
				while (it.hasNext())
				{
					Object key = it.next();
					if (key.toString().startsWith("Impl") || key.toString().startsWith("Spec"))
						sb.append(key).append(eq).append(atts.get(key)).append(NL);
				}
			}
		}
		sb.append(Env.getLanguage(Env.getCtx())).append(NL);
		MClient client = MClient.get(Env.getCtx());
		sb.append(client).append(NL);
		sb.append(getMsg("IsMultiLingualDocument"))
			.append(eq).append(client.isMultiLingualDocument()).append(NL);
		sb.append("BaseLanguage = ").append(Env.isBaseLanguage(Env.getCtx(), "AD_Window"))
			.append("/").append(Env.isBaseLanguage(Env.getCtx(), "C_UOM")).append(NL);
		sb.append(SysEnv.getInfo(null)).append(NL);
		sb.append("PDF License=").append(Document.isLicensed()).append(NL); 
		//  Context
		sb.append(NL)
			.append("=== Context ===").append(NL);
		String[] context = ctx.getEntireContext();
		for (String element : context)
			sb.append(element).append(NL);
		return sb;
	}   //  getInfoBasic
	
	/**
	 *  Get translated Message, if DB connection exists
	 *  @param msg AD_Message
	 *  @return translated msg if connected
	 */
	private static String getMsg (String msg)
	{
		if (DB.isConnected())
			return Msg.translate(Env.getCtx(), msg);
		return msg;
	}   //  getMsg


	/**
	 *  Get Server Info.
	 *  @return host : port (NotActive) via CMhost : port
	 */
	private static String getServerInfo()
	{
		StringBuffer sb = new StringBuffer();
		CConnection cc = CConnection.get();
		//  Host
		sb.append(cc.getAppsHost()).append(" : ")
			.append(cc.getAppsPort())
			.append(" (");

		//  Server
		if (cc.isAppsServerOK(false))
			sb.append(CConnection.get().getServerVersion());
		else
			sb.append(getMsg("NotActive"));
		//
		sb.append(")\n  ");
		//
		boolean remoteObjects = DB.isRemoteObjects();
		boolean remoteProcess = DB.isRemoteProcess();
		String realCP = CConnection.PROFILE_LAN;
		if (cc.isRMIoverHTTP() && cc.isAppsServerOK(false))
			realCP = CConnection.PROFILE_WAN;
		else if (remoteObjects && remoteProcess)
			realCP = CConnection.PROFILE_VPN;
		else if (remoteProcess)
			realCP = CConnection.PROFILE_TERMINAL;
		sb.append(cc.getConnectionProfileText(realCP));
		sb.append(": Tunnel=").append(cc.isRMIoverHTTP() && cc.isAppsServerOK(false))
			.append(", Objects=").append(remoteObjects)
			.append(", Process=").append(remoteProcess);

		return sb.toString();
	}   //  getServerInfo

	/**
	 *  Get Database Info
	 *  @return host : port : sid
	 */
	private static String getDatabaseInfo()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(CConnection.get().getDbHost()).append(" : ")
			.append(CConnection.get().getDbPort()).append(" / ")
			.append(CConnection.get().getDbName());
		//  Connection Manager
		if (CConnection.get().isViaFirewall())
			sb.append(getMsg("via")).append(" ")
				.append(CConnection.get().getFwHost()).append(" : ")
				.append(CConnection.get().getFwPort());

		return sb.toString();
	}   //  getDatabaseInfo
	
	/**
	 *  Get Localhost
	 *  @return local host
	 */
	private static String getLocalHost()
	{
		try
		{
			InetAddress id = InetAddress.getLocalHost();
			return id.toString();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getLocalHost", e);
		}
		return "-no local host info -";
	}   //  getLocalHost

	
	/**************************************************************************
	 * 	CLogMgt
	 */
	private CLogMgt()
	{
		testLog();
	}

	/**
	 * 	Test Log
	 */
	private void testLog()
	{
		final CLogger log1 = CLogger.getCLogger("test");
		//
		log1.log(Level.SEVERE, "severe");
		log1.warning("warning");
		log1.info("Info");
		log1.config("config");
		log1.fine("fine");
		log1.finer("finer");
		log1.entering("myClass", "myMethod", "parameter");
		log1.exiting("myClass", "myMethod", "result");
		log1.finest("finest");

		new Thread()
		{
			@Override
			public void run()
			{
				log1.info("thread info");
			}
		}.start();
		
		try
		{
			Integer.parseInt("ABC");
		}
		catch (Exception e)
		{
			log1.log(Level.SEVERE, "error message", e);
		}
		log1.log(Level.INFO, "info message 1", "1Param");
		log1.log(Level.INFO, "info message n", new Object[]{"1Param","2Param"});
	}	//	testLog
	
	/**
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		initialize(true);
		new CLogMgt();
	}	//	CLogMgt

}	//	CLogMgt
