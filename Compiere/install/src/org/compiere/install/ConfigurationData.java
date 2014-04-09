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
import java.util.*;
import java.util.logging.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;

import org.compiere.*;
import org.compiere.db.*;
import org.compiere.startup.*;
import org.compiere.util.*;


/**
 *	Configuration Data
 *
 *  @author Jorg Janke
 *  @version $Id: ConfigurationData.java 9202 2010-08-26 20:15:46Z rthng $
 */
public class ConfigurationData
{
	/**
	 * 	Constructor
	 * 	@param panel UI panel
	 *  @param properties configuration properties
	 */
	public ConfigurationData (ConfigurationPanel panel, Properties properties)
	{
		super ();
		p_panel = panel;
		p_properties = properties;
	}	//	ConfigurationData

	/** UI Panel				*/
	protected ConfigurationPanel	p_panel = null;
	/** Environment Properties	*/
	protected Properties		p_properties;
	/**	Compiere Home			*/
	private File				m_compiereHome;
	



	/**	Static Logger	*/
	static CLogger	log	= CLogger.getCLogger (ConfigurationData.class);


	/** Properties File	name		*/
	public static final String	COMPIERE_ENV_FILE		= "CompiereEnv.properties";
	/** Compiere Home				*/
	public static final String	COMPIERE_HOME 			= "COMPIERE_HOME";

	/** VM Home						*/
	public static final String	JAVA_HOME 				= "JAVA_HOME";
	/** VM Type						*/
	public static final String	JAVA_TYPE 				= "COMPIERE_JAVA_TYPE";

	/** VM Optione					*/
	public static final String	COMPIERE_JAVA_OPTIONS 	= "COMPIERE_JAVA_OPTIONS";


	/** KeyStore FileName			*/
	public static final String	COMPIERE_KEYSTORE 		= "COMPIERE_KEYSTORE";
	/** KeyStote Password			*/
	public static final String	COMPIERE_KEYSTOREPASS	= "COMPIERE_KEYSTOREPASS";
	/** Default Keysore Password	*/
	public static final String		KEYSTORE_PASSWORD	= "myPassword";
	/** KeyStore Code 				*/
	public static final String	COMPIERE_KEYSTORECODEALIAS	= "COMPIERE_KEYSTORECODEALIAS";
	/** KeyStore Web SSL			*/
	public static final String	COMPIERE_KEYSTOREWEBALIAS	= "COMPIERE_KEYSTOREWEBALIAS";

	/** 				*/
	public static final String	COMPIERE_MAIL_SERVER 	= "COMPIERE_MAIL_SERVER";
	/** 				*/
	public static final String	COMPIERE_MAIL_USER 		= "COMPIERE_MAIL_USER";
	/** 				*/
	public static final String	COMPIERE_MAIL_PASSWORD 	= "COMPIERE_MAIL_PASSWORD";
	/** 				*/
	public static final String	COMPIERE_ADMIN_EMAIL	= "COMPIERE_ADMIN_EMAIL";
	/** 				*/
	public static final String	COMPIERE_MAIL_UPDATED	= "COMPIERE_MAIL_UPDATED";

	/** 				*/
	public static final String	COMPIERE_FTP_SERVER		= "COMPIERE_FTP_SERVER";
	/** 				*/
	public static final String	COMPIERE_FTP_USER		= "COMPIERE_FTP_USER";
	/** 				*/
	public static final String	COMPIERE_FTP_PASSWORD	= "COMPIERE_FTP_PASSWORD";
	/** 				*/
	public static final String	COMPIERE_FTP_PREFIX		= "COMPIERE_FTP_PREFIX";

	/** 				*/
	public static final String	COMPIERE_WEBSTORES		= "COMPIERE_WEBSTORES";

	private String u_javaHome = null; 	
	private String u_javaType = null;
	private String u_compiereHome = null;
	private String u_keystorePass = null;
	private String u_appServHost = null;
	private String u_appServType = null;
	private String u_appServDeployDir = null;
	private String u_appServJNPPort = null;
	private String u_appServWebPort = null;
	private String u_appServSSLPort = null;
	private String u_dbHost = null;
	private String u_dbType = null;
	private String u_dbName = null;
	private String u_dbPort = null;
	private String u_dbSystemPass = null;
	private String u_dbUser = null;
	private String u_dbPass = null;
	private String u_mailServHost = null;
	private String u_adminEmail = null;
	private String u_mailUser = null;
	private String u_mailPass = null;
	
	private int javaTypeIndex = -1;
	private int appsTypeIndex = -1;
	private int dbTypeIndex = -1;
	
	private String d_javaHome = null; 	
	private String d_javaType = null;
	private String d_compiereHome = null;
	private String d_keystorePass = null;
	private String d_appServHost = null;
	private String d_appServType = null;
	private String d_appServDeployDir = null;
	private String d_appServJNPPort = null;
	private String d_appServWebPort = null;
	private String d_appServSSLPort = null;
	private String d_dbHost = null;
	private String d_dbType = null;
	private String d_dbName = null;
	private String d_dbPort = null;
	private String d_dbSystemPass = null;
	private String d_dbUser = null;
	private String d_dbPass = null;
	private String d_mailServHost = null;
	private String d_adminEmail = null;
	private String d_mailUser = null;
	private String d_mailPass = null;
	

	/**
	 * 	Load Configuration Data
	 * 	@return true if loaded
	 */
	public boolean load()
	{
		//	Load C:\Compiere2\CompiereEnv.properties
		String compiereHome = System.getProperty(COMPIERE_HOME);
		if ((compiereHome == null) || (compiereHome.length() == 0))
			compiereHome = System.getProperty("user.dir");

		boolean envLoaded = false;
		String fileName = compiereHome + File.separator + COMPIERE_ENV_FILE;
		File env = new File (fileName);
		if (env.exists())
		{
			try
			{
				FileInputStream fis = new FileInputStream(env);
				p_properties.load(fis);
				fis.close();
			}
			catch (Exception e)
			{
				log.warning(e.toString());
			}
			log.info(env.toString());
			if (p_properties.size() > 5)
				envLoaded = true;
			//
			setJavaType((String)p_properties.get(JAVA_TYPE));
			initJava();
			setJavaHome((String)p_properties.get(JAVA_HOME));
			//
			setCompiereHome((String)p_properties.get(COMPIERE_HOME));
			String s = (String)p_properties.get(COMPIERE_KEYSTOREPASS);
			if ((s == null) || (s.length() == 0))
				s = KEYSTORE_PASSWORD;
			setKeyStore(s);
			//
			setAppsServerType((String)p_properties.get(Environment.COMPIERE_APPS_TYPE));
			initAppsServer();
			setAppsServer((String)p_properties.get(Environment.COMPIERE_APPS_SERVER));
			setAppsServerDeployDir((String)p_properties.get(Environment.COMPIERE_APPS_DEPLOY));
			setAppsServerJNPPort((String)p_properties.get(Environment.COMPIERE_JNP_PORT));
			setAppsServerWebPort((String)p_properties.get(Environment.COMPIERE_WEB_PORT));
			setAppsServerSSLPort((String)p_properties.get(Environment.COMPIERE_SSL_PORT));
			//
			setDatabaseType((String)p_properties.get(Environment.COMPIERE_DB_TYPE));
			initDatabase((String)p_properties.get(Environment.COMPIERE_DB_NAME));	//	fills Database Options
			setDatabaseDiscovered((String)p_properties.get(Environment.COMPIERE_DB_NAME));
			setDatabaseServer((String)p_properties.get(Environment.COMPIERE_DB_SERVER));
			setDatabasePort((String)p_properties.get(Environment.COMPIERE_DB_PORT));
			setDatabaseName((String)p_properties.get(Environment.COMPIERE_DB_NAME));

			setDatabaseUser((String)p_properties.get(Environment.COMPIERE_DB_USER));
			setDatabasePassword((String)p_properties.get(Environment.COMPIERE_DB_PASSWORD));
			setDatabaseSystemPassword((String)p_properties.get(Environment.COMPIERE_DB_SYSTEM));

			p_panel.fMailServer.setText((String)p_properties.get(COMPIERE_MAIL_SERVER));
			p_panel.fMailUser.setText((String)p_properties.get(COMPIERE_MAIL_USER));
			p_panel.fMailPassword.setText((String)p_properties.get(COMPIERE_MAIL_PASSWORD));
			p_panel.fAdminEMail.setText((String)p_properties.get(COMPIERE_ADMIN_EMAIL));
		}

		InetAddress localhost = null;
		String hostName = "unknown";
		try
		{
			localhost = InetAddress.getLocalHost();
			hostName = localhost.getHostName();
		}
		catch (Exception e)
		{
			log.severe("Cannot get local host name");
		}

		//	No environment file found - defaults
	//	envLoaded = false;
		if (!envLoaded)
		{
			log.info("Defaults");
			initJava();
			//
			setCompiereHome(compiereHome);
			setKeyStore(KEYSTORE_PASSWORD);
			//	AppsServer
			initAppsServer();
			setAppsServer(hostName);
			//	Database Server
			initDatabase("");
			String dbName = getDatabaseDiscovered();
			if ((dbName != null) && (dbName.length() > 0))
				setDatabaseName(dbName);
			setDatabaseSystemPassword("");
			setDatabaseServer(hostName);
			setDatabaseUser("compiere");
			setDatabasePassword("compiere");
			//	Mail Server
			p_panel.fMailServer.setText(hostName);
			p_panel.fMailUser.setText("info");
			p_panel.fMailPassword.setText("");
			p_panel.fAdminEMail.setText("info@" + hostName);
			//
		}	//	!envLoaded

		//	Default FTP stuff
		if (!p_properties.containsKey(COMPIERE_FTP_SERVER))
		{
			p_properties.setProperty(COMPIERE_FTP_SERVER, "localhost");
			p_properties.setProperty(COMPIERE_FTP_USER, "anonymous");
			p_properties.setProperty(COMPIERE_FTP_PASSWORD, "user@host.com");
			p_properties.setProperty(COMPIERE_FTP_PREFIX, "my");
		}
		//	Default Java Options
		if (!p_properties.containsKey(COMPIERE_JAVA_OPTIONS))
			p_properties.setProperty(COMPIERE_JAVA_OPTIONS, "-Xms128M -Xmx1024M -XX:MaxPermSize=128m -XX:NewRatio=3 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled -XX:+UseAdaptiveGCBoundary -XX:SurvivorRatio=6 -XX:MinHeapFreeRatio=25 -XX:MaxHeapFreeRatio=75 -XX:+HeapDumpOnOutOfMemoryError");
		//	Web Alias
		if (!p_properties.containsKey(Environment.COMPIERE_WEB_ALIAS) && (localhost != null))
			p_properties.setProperty(Environment.COMPIERE_WEB_ALIAS, localhost.getCanonicalHostName());

		//	(String)p_properties.get(COMPIERE_DB_URL)	//	derived

		//	Keystore Alias
		if (!p_properties.containsKey(COMPIERE_KEYSTORECODEALIAS))
			p_properties.setProperty(COMPIERE_KEYSTORECODEALIAS, "compiere");
		if (!p_properties.containsKey(COMPIERE_KEYSTOREWEBALIAS))
			p_properties.setProperty(COMPIERE_KEYSTOREWEBALIAS, "compiere");

		return true;
	}	//	load


	/**************************************************************************
	 * 	Test
	 * 	@param finalIteration final test
	 *	@return true if test ok
	 */
	public boolean test(boolean finalIteration)
	{
		String error = testJava();
		if (error != null)
		{
			log.severe(error);
			return false;
		}

		error = testCompiere();
		if (error != null)
		{
			log.warning(error);
			return false;
		}
		if(p_panel != null)
			p_panel.setStatusBar(p_panel.lAppsServer.getText());
		
		error = testAppsServer();
		if (error != null)
		{
			log.warning(error);
			return false;
		}
		
		if(p_panel != null)
			p_panel.setStatusBar(p_panel.lDatabaseServer.getText());
		error = testDatabase();
		if (error != null)
		{
			log.warning(error);
			return false;
		}

		if(p_panel != null)
			p_panel.setStatusBar(p_panel.lMailServer.getText());
		
		error = testMail();
		if (error != null)
		{
			log.warning(error);
			return false;
		}

		return true;
	}	//	test


	/**
	 * 	Test Compiere and set CompiereHome
	 *	@return error message or null if OK
	 */
	private String testCompiere()
	{
		//	Compiere Home
		String compiereHome = getCompiereHome().trim();
		m_compiereHome = new File (getCompiereHome());
		boolean pass = m_compiereHome.exists();
		String error = "Not found: CompiereHome = " + m_compiereHome;
		if(p_panel != null)
			p_panel.signalOK(p_panel.okCompiereHome, "ErrorCompiereHome",
					pass, true, error);
		if (!pass)
			return error;
		pass = compiereHome.indexOf(' ') == -1;
		error = "CompiereHome cannot have spaces = " + m_compiereHome;
		if(p_panel != null)
			p_panel.signalOK(p_panel.okCompiereHome, "ErrorCompiereHome",
					pass, true, error);
		if (!pass)
			return error;

		log.info("OK: CompiereHome = " + m_compiereHome);
		p_properties.setProperty(COMPIERE_HOME, m_compiereHome.getAbsolutePath());
		System.setProperty(COMPIERE_HOME, m_compiereHome.getAbsolutePath());

		//	KeyStore
		String fileName = KeyStoreMgt.getKeystoreFileName(m_compiereHome.getAbsolutePath());
		p_properties.setProperty(COMPIERE_KEYSTORE, fileName);

		//	KeyStore Password
		//String pw = new String(p_panel.fKeyStore.getPassword());
		String pw = new String(getKeyStore());
		pass = (pw != null) && (pw.length() > 0);
		error = "Invalid Key Store Password = " + pw;
		if(p_panel != null)
			p_panel.signalOK(p_panel.okKeyStore, "KeyStorePassword",
					pass, true, error);
		if (!pass)
			return error;
		p_properties.setProperty(COMPIERE_KEYSTOREPASS, pw);
		KeyStoreMgt ks = new KeyStoreMgt (fileName, pw.toCharArray());
		if(p_panel != null)
			error = ks.verify((JFrame)SwingUtilities.getWindowAncestor(p_panel));
		else
			error = ks.verify(null);
		pass = error == null;
		if(p_panel != null)
			p_panel.signalOK(p_panel.okKeyStore, "KeyStorePassword",
					pass, true, error);
		if (!pass)
			return error;
		log.info("OK: KeyStore = " + fileName);
		return null;
	}	//	testCompiere


	/**************************************************************************
	 * 	Test (optional) Mail
	 *	@return error message or null, if OK
	 */
	private String testMail()
	{
		//	Mail Server
		String server;
		if(p_panel != null)
			server = p_panel.fMailServer.getText();
		else
			server = u_mailServHost;
		
		boolean pass = (server != null) && (server.length() > 0)
			&& (server.toLowerCase().indexOf("localhost") == -1)
			&& !server.equals("127.0.0.1");
		String error = "Error Mail Server = " + server;
		InetAddress	mailServer = null;
		try
		{
			if (pass)
				mailServer = InetAddress.getByName(server);
		}
		catch (Exception e)
		{
			error += " - " + e.getMessage();
			pass = false;
		}
		
		if(p_panel != null)
			p_panel.signalOK(p_panel.okMailServer, "ErrorMailServer",
					pass, true, error);
		if (!pass)
		{
			p_properties.setProperty(COMPIERE_MAIL_SERVER, "");
			return error;
		}
		p_properties.setProperty(COMPIERE_MAIL_SERVER, mailServer.getHostName());

		
		//	Mail User
		String mailUser;
		String mailPassword;
		String adminEMailString;
		
		if(p_panel != null)
		{
			mailUser = p_panel.fMailUser.getText();
			mailPassword = new String(p_panel.fMailPassword.getPassword());
			adminEMailString = p_panel.fAdminEMail.getText();
		}
		else
		{
			mailUser = u_mailUser;
			mailPassword = u_mailPass;
			adminEMailString = u_adminEmail;
		}
	//	m_errorString = "ErrorMailUser";
	//	log.config("Mail User = " + mailUser + "/" + mailPassword);

		//	Mail Address
		InternetAddress adminEMail = null;
		try
		{
			adminEMail = new InternetAddress (adminEMailString);
		}
		catch (Exception e)
		{
			error = "Not valid: " +  adminEMailString + " - " + e.getMessage();
			pass = false;
		}
		//
		if (pass)
		{
			error = "Not verified EMail = " + adminEMail;
			pass = testMailServer(mailServer, adminEMail, mailUser, mailPassword);
		}
		if(p_panel != null)
			p_panel.signalOK(p_panel.okMailUser, "ErrorMail",
					pass, false, error);
		if (pass)
		{
			log.info("OK: EMail = " + adminEMail);
			p_properties.setProperty(COMPIERE_ADMIN_EMAIL, adminEMail.toString());
			p_properties.setProperty(COMPIERE_MAIL_USER, mailUser);
			p_properties.setProperty(COMPIERE_MAIL_PASSWORD, mailPassword);
			p_properties.setProperty(COMPIERE_MAIL_UPDATED, "No");
		}
		else
		{
			log.warning(error);
			p_properties.setProperty(COMPIERE_ADMIN_EMAIL, "");
			p_properties.setProperty(COMPIERE_MAIL_USER, "");
			p_properties.setProperty(COMPIERE_MAIL_PASSWORD, "");
			p_properties.setProperty(COMPIERE_MAIL_UPDATED, "");
		}
		return null;
	}	//	testMail

	/**
	 * 	Test Mail
	 * 	@param mailServer mail server
	 * 	@param adminEMail email of admin
	 * 	@param mailUser user ID
	 * 	@param mailPassword password
	 *  @return true of OK
	 */
	private boolean testMailServer(InetAddress	mailServer, InternetAddress adminEMail,
		String mailUser, String mailPassword)
	{
		boolean smtpOK = false;
		boolean imapOK = false;
		if (testPort (mailServer, 25, true))
		{
			log.config("OK: SMTP Server contacted");
			smtpOK = true;
		}
		else
			log.info("SMTP Server NOT available");
		//
		if (testPort (mailServer, 110, true))
			log.config("OK: POP3 Server contacted");
		else
			log.info("POP3 Server NOT available");
		if (testPort (mailServer, 143, true))
		{
			log.config("OK: IMAP4 Server contacted");
			imapOK = true;
		}
		else
			log.info("IMAP4 Server NOT available");
		//
		if (!smtpOK)
		{
			String error = "No active Mail Server";
			if(p_panel != null)
				p_panel.signalOK (p_panel.okMailServer, "ErrorMailServer",
						false, false, error);
			log.warning(error);
			return false;
		}
		//
		try
		{
			EMail email = new EMail (new Ctx(),
					mailServer.getHostName(), 25, false,
					adminEMail.getAddress(), adminEMail.getPersonal(),
					adminEMail.getAddress(), adminEMail.getPersonal(),
					"Compiere Server Setup Test",
					"Test: " + getProperties(), false);
			if (email == null)
				return false;
			email.createAuthenticator (mailUser, mailPassword);
			if (EMail.SENT_OK.equals (email.send ()))
			{
				log.info("OK: Send Test Email to " + adminEMail);
			}
			else
			{
				log.warning("Could NOT send Email to " + adminEMail);
			}
		}
		catch (Exception ex)
		{
			log.warning(ex.getLocalizedMessage());
			return false;
		}

		//
		if (!imapOK)
			return false;

		//	Test Read Mail Access
		Properties props = new Properties();
		props.put("mail.store.protocol", "smtp");
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.host", mailServer.getHostName());
		props.put("mail.user", mailUser);
		props.put("mail.smtp.auth", "true");
		log.config("Connecting to " + mailServer.getHostName());
		//
		Session session = null;
		Store store = null;
		try
		{
			EMailAuthenticator auth = new EMailAuthenticator (mailUser, mailPassword);
			session = Session.getDefaultInstance(props, auth);
			session.setDebug (CLogMgt.isLevelFinest());
			log.config("Session=" + session);
			//	Connect to Store
			store = session.getStore("imap");
			log.config("Store=" + store);
		}
		catch (NoSuchProviderException nsp)
		{
			log.warning("Mail IMAP Provider - " + nsp.getMessage());
			return false;
		}
		catch (Exception e)
		{
			log.warning("Mail IMAP - " + e.getMessage());
			return false;
		}
		try
		{
			store.connect(mailServer.getHostName(), mailUser, mailPassword);
			log.config("Store - connected");
			Folder folder = store.getDefaultFolder();
			Folder inbox = folder.getFolder("INBOX");
			log.info("OK: Mail Connect to " + inbox.getFullName() + " #Msg=" + inbox.getMessageCount());
			//
			store.close();
		}
		catch (MessagingException mex)
		{
			log.warning("Mail Connect " + mex.getMessage());
			return false;
		}
		return true;
	}	//	testMailServer


	/**************************************************************************
	 * 	Test Apps Server Port (client perspective)
	 *  @param protocol protocol (http, ..)
	 *  @param server server name
	 *  @param port port
	 *  @param file file name
	 *  @return true if able to connect
	 */
	protected boolean testPort (String protocol, String server, int port, String file)
	{
		URL url = null;
		try
		{
			url = new URL (protocol, server, port, file);
		}
		catch (MalformedURLException ex)
		{
			log.warning("No URL for Protocol=" + protocol
				+ ", Server=" + server
				+ ": " + ex.getMessage());
			return false;
		}
		try
		{
			URLConnection c = url.openConnection();
			Object o = c.getContent();
			if (o == null)
				log.warning("In use=" + url);	//	error
			else
				log.warning("In Use=" + url);	//	error
		}
		catch (Exception ex)
		{
			log.fine("Not used=" + url);	//	ok
			return false;
		}
		return true;
	}	//	testPort

	/**
	 * 	Test Server Port
	 *  @param port port
	 *  @return true if able to create
	 */
	protected boolean testServerPort (int port)
	{
		try
		{
			ServerSocket ss = new ServerSocket (port);
			log.fine(ss.getInetAddress() + ":" + ss.getLocalPort() + " - created");
			ss.close();
		}
		catch (Exception ex)
		{
			log.warning("Port " + port + ": " + ex.getMessage());
			return false;
		}
		return true;
	}	//	testPort


	/**
	 * 	Test Port
	 *  @param host host
	 *  @param port port
	 *  @param shouldBeUsed true if it should be used
	 *  @return true if some server answered on port
	 */
	protected boolean testPort (InetAddress host, int port, boolean shouldBeUsed)
	{
		Socket pingSocket = null;
		try
		{
			pingSocket = new Socket(host, port);
		}
		catch (Exception e)
		{
			if (shouldBeUsed)
				log.warning("Open Socket " + host + ":" + port + " - " + e.getMessage());
			else
				log.fine(host + ":" + port + " - " + e.getMessage());
			return false;
		}
		if (!shouldBeUsed)
			log.warning("Open Socket " + host + ":" + port + " - " + pingSocket);

		log.fine(host + ":" + port + " - " + pingSocket);
		if (pingSocket == null)
			return false;
		//	success
		try
		{
			pingSocket.close();
		}
		catch (IOException e)
		{
			log.warning("close socket=" + e.toString());
		}
		return true;
	}	//	testPort


	/**************************************************************************
	 * 	Save Settings
	 *	@return true if saved
	 */
	public boolean save()
	{
		//	Add
		p_properties.setProperty("COMPIERE_MAIN_VERSION", Compiere.MAIN_VERSION);
		p_properties.setProperty("COMPIERE_DATE_VERSION", Compiere.DATE_VERSION);
		p_properties.setProperty("COMPIERE_DB_VERSION", Compiere.DB_VERSION);

		log.finest(p_properties.toString());

		//	Before we save, load Ini
		Ini.setClient(false);		
		String fileName = m_compiereHome.getAbsolutePath() + File.separator + Ini.COMPIERE_PROPERTY_FILE;
		Ini.loadProperties(fileName);

		//	Save Deployment Info
		Environment.get().save(p_properties, m_compiereHome.getAbsolutePath());

		//	Save Environment
		fileName = m_compiereHome.getAbsolutePath() + File.separator + COMPIERE_ENV_FILE;
		try
		{
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			p_properties.store(fos, COMPIERE_ENV_FILE);
			fos.flush();
			fos.close();
		}
		catch (Exception e)
		{
			log.severe("Cannot save Properties to " + fileName + " - " + e.toString());
			if(p_panel != null)
			{
			JOptionPane.showConfirmDialog(p_panel,
				Setup.res.getString("ErrorSave"),
				Setup.res.getString("CompiereServerSetup"),
				JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			}
			return false;
		}
		catch (Throwable t)
		{
			log.severe("Cannot save Properties to " + fileName + " - " + t.toString());
			if(p_panel != null)
			{
			JOptionPane.showConfirmDialog(p_panel,
				Setup.res.getString("ErrorSave"),
				Setup.res.getString("CompiereServerSetup"),
				JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			}
			return false;
		}
		log.info(fileName);
		return saveIni();
	}	//	save

	/**
	 * 	Synchronize and save Connection Info in Ini
	 * 	@return true
	 */
	private boolean saveIni()
	{
		String compiereHome = m_compiereHome.getAbsolutePath();
		Ini.setCompiereHome(compiereHome);

		//	Create Connection
		String ccType = getDatabaseType();
		CConnection cc = null;
		try
		{
			cc = CConnection.get (ccType,
				getDatabaseServer(), getDatabasePortString(), getDatabaseName(),
				getDatabaseUser(), getDatabasePassword());
			if(p_panel != null)
			{
			cc.setAppsHost(p_panel.fAppsServer.getText());
			cc.setAppsPort(p_panel.fJNPPort.getText());
			cc.setWebPort(p_panel.fWebPort.getText());
			cc.setAppsType(APPSTYPE[p_panel.fAppsType.getSelectedIndex()]);
			}
			else
			{
				cc.setAppsHost(u_appServHost);
				cc.setAppsPort(u_appServJNPPort);
				cc.setWebPort(u_appServSSLPort);
				cc.setAppsType(APPSTYPE[appsTypeIndex]);
			}

			cc.setConnectionProfile(CConnection.PROFILE_LAN);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "connection", e);
			return false;
		}
		if (cc == null)
		{
			log.warning("No Connection");
			return false;
		}

		//	Create/Validate Security Key
		File keyFile = SecureUtil.getKeyFile(compiereHome);
		if (!keyFile.exists())
		{
			if (SecureUtil.installKey(compiereHome))
			{
				if(p_panel != null)
					JOptionPane.showConfirmDialog(p_panel,
							Setup.res.getString("NewSecurityKey"),
							Setup.res.getString("CompiereServerSetup"),
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
				else
					log.log(Level.INFO, "New security key created");
			}
			else
			{
				if(p_panel != null)
					JOptionPane.showConfirmDialog(p_panel,
							Setup.res.getString("SecurityKeyError"),
							Setup.res.getString("CompiereServerSetup"),
							JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				else
					log.log(Level.INFO, "Security Key error");
				return false;
			}
		}
		else
		{
			if (!SecureUtil.installKey(compiereHome))
			{
				if(p_panel != null)
					JOptionPane.showConfirmDialog(p_panel,
							Setup.res.getString("SecurityKeyError"),
							Setup.res.getString("CompiereServerSetup"),
							JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
					else
						log.log(Level.INFO, "Security Key error");
				return false;
			}
		}

		Ini.setClient (false);
		Ini.setProperty(Ini.P_CONNECTION, cc.toStringLong());
		Ini.saveProperties(false);	//	Server

		return true;
	}	//	saveIni


	/**
	 * 	Get Properties
	 *	@return properties
	 */
	Properties getProperties ()
	{
		return p_properties;
	}	//	getProperties

	/**
	 * 	Get Compiere Home
	 *	@return compiere home
	 */
	public String getCompiereHome()
	{
		if(p_panel != null)
			return p_panel.fCompiereHome.getText();
		else
			return u_compiereHome;
	}	//	getCompiereHome

	/**
	 * 	Set Compiere Home
	 *	@param compiereHome
	 */
	public void setCompiereHome (String compiereHome)
	{
		if(p_panel != null)
			p_panel.fCompiereHome.setText(compiereHome);
		else 
			d_compiereHome = compiereHome;
	}	//	setCompiereHome

	/**
	 * 	Get Key Store
	 *	@return password
	 */
	public String getKeyStore ()
	{
		if(p_panel != null)
		{
			char[] pw = p_panel.fKeyStore.getPassword();
			if (pw != null)
				return new String(pw);
		}
		else
		{
			return u_keystorePass;
		}
		return "";
	}	//	getKeyStore

	/**
	 * 	Set Key Store Password
	 *	@param password
	 */
	public void setKeyStore (String password)
	{
		if(p_panel != null)
			p_panel.fKeyStore.setText(password);
		else 
			d_keystorePass = password;
	}	//	setKeyStore


	/**************************************************************************
	 * 	Java Settings
	 *************************************************************************/

	/** SUN VM (default)	*/
	protected static String	JAVATYPE_SUN = "sun";
	/** Apple VM			*/
	private static String	JAVATYPE_MAC = "mac";
	/** IBM VM				*/
	protected static String	JAVATYPE_IBM = "ibm";
	/** Java VM Types		*/
	static String[]	JAVATYPE = new String[]
		{JAVATYPE_SUN, JAVATYPE_MAC, JAVATYPE_IBM};
	/** Database Configs	*/
	private final Config[] m_javaConfig = new Config[]
	    {new ConfigVMSun(this), new ConfigVMMac(this), new ConfigVMIBM(this)};

	/**
	 * 	Init Database
	 */
	public void initJava()
	{
		int index;
		if(p_panel != null)
			index = p_panel.fJavaType.getSelectedIndex();
		else
			index = javaTypeIndex;
		
		if ((index < 0) || (index >= JAVATYPE.length))
			log.warning("JavaType Index invalid: " + index);
		else if (m_javaConfig[index] == null)
		{
			log.warning("JavaType Config missing: " + JAVATYPE[index]);
			if(p_panel != null)
				p_panel.fJavaType.setSelectedIndex(0);
		}
		else
			m_javaConfig[index].init();
	}	//	initDatabase

	/**
	 * 	Test Java
	 *	@return error message or null of OK
	 */
	public String testJava()
	{
		int index;
		if(p_panel != null)
			index = p_panel.fJavaType.getSelectedIndex();
		else
			index=javaTypeIndex;
		if ((index < 0) || (index >= JAVATYPE.length))
			return "JavaType Index invalid: " + index;
		else if (m_javaConfig[index] == null)
			return "JavaType Config class missing: " + index;
		return m_javaConfig[index].test();
	}	//	testJava

	/**
	 * 	Set Java Type
	 *	@param javaType The javaType to set.
	 */
	public void setJavaType (String javaType)
	{
		int index = -1;
		for (int i = 0; i < JAVATYPE.length; i++)
		{
			if (JAVATYPE[i].equals(javaType))
			{
				index = i;
				break;
			}
		}
		if (index == -1)
		{
			index = 0;
			log.warning("Invalid JavaType=" + javaType);
		}
		if(p_panel != null)
			p_panel.fJavaType.setSelectedIndex(index);
		else
		{
			d_javaType= javaType;
			javaTypeIndex = index;
		}
	}	//	setJavaType

	/**
	 * @return Returns the javaType.
	 */
	public String getJavaType ()
	{
		if(p_panel != null)
			return (String)p_panel.fJavaType.getSelectedItem();
		else
			return u_javaType;
	}

	/**
	 * @return Returns the javaHome.
	 */
	public String getJavaHome ()
	{
		if(p_panel != null)
			return p_panel.fJavaHome.getText();
		else
			return u_javaHome;
	}
	/**
	 * @param javaHome The javaHome to set.
	 */
	public void setJavaHome (String javaHome)
	{
		if(p_panel != null)
			p_panel.fJavaHome.setText(javaHome);
		else 
			d_javaHome = javaHome;
			
			
	}

	/**************************************************************************
	 * 	Apps Server Settings
	 *************************************************************************/

	/** Application Server Type		*/
	static String[]	APPSTYPE = new String[]
		{Environment.APPSTYPE_JBOSS, Environment.APPSTYPE_TOMCAT,
		Environment.APPSTYPE_IBM, Environment.APPSTYPE_ORACLE};
	/** Database Configs	*/
	private final Config[] m_appsConfig = new Config[]
	    {new ConfigJBoss(this), new ConfigTomcat(this),
		 new ConfigWAS(this), null};

	/**
	 * 	Init Apps Server
	 */
	public void initAppsServer()
	{
		int index;
		if(p_panel != null)
			index=p_panel.fAppsType.getSelectedIndex();
		else
			index = appsTypeIndex;
		
		if ((index < 0) || (index >= APPSTYPE.length))
			log.warning("AppsServerType Index invalid: " + index);
		else if (m_appsConfig[index] == null)
		{
			log.warning("AppsServerType Config missing: " + APPSTYPE[index]);
			if(p_panel != null)
				p_panel.fAppsType.setSelectedIndex(0);
		}
		else
			m_appsConfig[index].init();
	}	//	initAppsServer

	/**
	 * 	Test Apps Server
	 *	@return error message or null of OK
	 */
	public String testAppsServer()
	{
		int index;
		if(p_panel != null)
			index= p_panel.fAppsType.getSelectedIndex();
		else
			index=appsTypeIndex;
		if ((index < 0) || (index >= APPSTYPE.length))
			return "AppsServerType Index invalid: " + index;
		else if (m_appsConfig[index] == null)
			return "AppsServerType Config class missing: " + index;
		return m_appsConfig[index].test();
	}	//	testAppsServer


	/**
	 * 	Set Apps Server Type
	 *	@param appsType The appsType to set.
	 */
	public void setAppsServerType (String appsType)
	{
		int index = -1;
		for (int i = 0; i < APPSTYPE.length; i++)
		{
			if (APPSTYPE[i].equals(appsType))
			{
				index = i;
				break;
			}
		}
		if (index == -1)
		{
			index = 0;
			log.warning("Invalid AppsType=" + appsType);
		}
		if(p_panel != null)
			p_panel.fAppsType.setSelectedIndex(index);
		else
		{
			appsTypeIndex=index;
			d_appServType=appsType;
		}
			
	}	//	setAppsServerType

	/**
	 * 	Get Apps Server Type
	 *	@return Apps Server Type
	 */
	public String getAppsServerType ()
	{
		if(p_panel != null)
			return (String)p_panel.fAppsType.getSelectedItem();
		else
			return u_appServType;
	}	//	setDatabaseType

	/**
	 * @return Returns the appsServer.
	 */
	public String getAppsServer ()
	{
		if(p_panel != null)
			return p_panel.fAppsServer.getText();
		else
			return u_appServHost;
	}
	/**
	 * @param appsServer The appsServer to set.
	 */
	public void setAppsServer (String appsServer)
	{
		if(p_panel != null)
			p_panel.fAppsServer.setText(appsServer);
		else
			d_appServHost=appsServer;
	}

	/**
	 * @return Returns the appsServerDeployDir.
	 */
	public String getAppsServerDeployDir ()
	{
		if(p_panel != null)
			return p_panel.fDeployDir.getText();
		else
			return u_appServDeployDir;
	}
	/**
	 * @param appsServerDeployDir The appsServerDeployDir to set.
	 */
	public void setAppsServerDeployDir (String appsServerDeployDir)
	{
		if(p_panel != null)
			p_panel.fDeployDir.setText(appsServerDeployDir);
		else
			d_appServDeployDir = appsServerDeployDir;
	}
	/**
	 * @param enable if true enable entry
	 */
	public void setAppsServerDeployDir (boolean enable)
	{
		if(p_panel != null)
		{
			p_panel.fDeployDir.setEnabled(enable);
			p_panel.bDeployDir.setEnabled(enable);
		}
	}
	/**
	 * @return Returns the appsServerJNPPort.
	 */
	public int getAppsServerJNPPort ()
	{
		try
		{
			if(p_panel != null)
				return Integer.parseInt(p_panel.fJNPPort.getText());
			else
				return Integer.parseInt(u_appServJNPPort);
		}
		catch (Exception e)
		{
			setAppsServerJNPPort("0");
		}
		return 0;
	}
	/**
	 * @param appsServerJNPPort The appsServerJNPPort to set.
	 */
	public void setAppsServerJNPPort (String appsServerJNPPort)
	{
		if(p_panel != null)
			p_panel.fJNPPort.setText(appsServerJNPPort);
		else
			d_appServJNPPort = appsServerJNPPort;
	}
	/**
	 * @param enable if enable JNP entry
	 */
	public void setAppsServerJNPPort (boolean enable)
	{
		if(p_panel != null)
			p_panel.fJNPPort.setEnabled(enable);
	}
	/**
	 * @param label The JNDI/JNP port label
	 */
	public void setAppsServerJNPPortLabel (String label)
	{
		if(p_panel != null)
			p_panel.lJNPPort.setText(label);
	}
	/**
	 * @return Returns the appsServerSSLPort.
	 */
	public int getAppsServerSSLPort ()
	{
		try
		{
			if(p_panel != null)
				return Integer.parseInt(p_panel.fSSLPort.getText());
			else
				return Integer.parseInt(u_appServSSLPort);
		}
		catch (Exception e)
		{
			setAppsServerSSLPort("0");
		}
		return 0;
	}
	/**
	 * @param appsServerSSLPort The appsServerSSLPort to set.
	 */
	public void setAppsServerSSLPort (String appsServerSSLPort)
	{
		if(p_panel != null)
			p_panel.fSSLPort.setText(appsServerSSLPort);
		else
			d_appServSSLPort = appsServerSSLPort;
	}
	/**
	 * @param enable if tre enable SSL entry
	 */
	public void setAppsServerSSLPort (boolean enable)
	{
		if(p_panel != null)
			p_panel.fSSLPort.setEnabled(enable);
	}
	/**
	 * @return Returns the appsServerWebPort.
	 */
	public int getAppsServerWebPort ()
	{
		try
		{
			if(p_panel != null)
				return Integer.parseInt(p_panel.fWebPort.getText());
			else
				return Integer.parseInt(u_appServWebPort);
		}
		catch (Exception e)
		{
			setAppsServerWebPort("0");
		}
		return 0;
	}
	/**
	 * @param appsServerWebPort The appsServerWebPort to set.
	 */
	public void setAppsServerWebPort (String appsServerWebPort)
	{
		if(p_panel != null)
			p_panel.fWebPort.setText(appsServerWebPort);
		else
			d_appServWebPort = appsServerWebPort;
	}
	/**
	 * @param enable if tre enable Web entry
	 */
	public void setAppsServerWebPort (boolean enable)
	{
		if(p_panel != null)
			p_panel.fWebPort.setEnabled(enable);
	}


	/**************************************************************************
	 * 	Database Settings
	 *************************************************************************/

	/** Database Types		*/
	static String[]	DBTYPE = new String[]
	    {Environment.DBTYPE_ORACLEXE,
		Environment.DBTYPE_ORACLE,
		Environment.DBTYPE_PG
		//Environment.DBTYPE_DB2,
		//Environment.DBTYPE_MS
		};
	/** Database Configs	*/
	private final Config[] m_databaseConfig = new Config[]
	    {
		new ConfigOracle(this, true),
		new ConfigOracle(this, false),
		new ConfigPostgreSQL (this),
		//new ConfigDB2(this),
		//new ConfigSQLServer(this),
		null
		};

	/**
	 * 	Init Database
	 * 	@param selected DB
	 */
	public void initDatabase(String selected)
	{
		int index;
		if(p_panel != null)
			index = p_panel.fDatabaseType.getSelectedIndex();
		else
			index=dbTypeIndex;
		
		if ((index < 0) || (index >= DBTYPE.length))
			log.warning("DatabaseType Index invalid: " + index);
		else if (m_databaseConfig[index] == null)
		{
			log.warning("DatabaseType Config missing: " + DBTYPE[index]);
			if(p_panel != null )
				p_panel.fDatabaseType.setSelectedIndex(0);
		}
		else
		{
			m_databaseConfig[index].init();
			String[] databases = m_databaseConfig[index].discoverDatabases(selected);
			if(p_panel != null)
			{
				DefaultComboBoxModel model = new DefaultComboBoxModel(databases);
				p_panel.fDatabaseDiscovered.setModel(model);
		//		p_panel.fDatabaseDiscovered.setEnabled(databases.length > 0);
			}
			if (databases.length > 0)
			{
				if(p_panel != null)
					p_panel.fDatabaseName.setText(databases[0]);
				else
					setDatabaseName(databases[0]);
			}
		}
	}	//	initDatabase

	/**
	 * 	Test Database
	 *	@return error message or null of OK
	 */
	public String testDatabase()
	{
		int index;
		if(p_panel != null)
			index = p_panel.fDatabaseType.getSelectedIndex();
		else
			index = dbTypeIndex;
		if ((index < 0) || (index >= DBTYPE.length))
			return "DatabaseType Index invalid: " + index;
		else if (m_databaseConfig[index] == null)
			return "DatabaseType Config class missing: " + index;
		return m_databaseConfig[index].test();
	}	//	testDatabase


	/**
	 * 	Set Database Type
	 *	@param databaseType The databaseType to set.
	 */
	public void setDatabaseType (String databaseType)
	{
		int index = -1;
		for (int i = 0; i < DBTYPE.length; i++)
		{
			if (DBTYPE[i].equals(databaseType))
			{
				index = i;
				break;
			}
		}
		if (index == -1)
		{
			index = 0;
			log.warning("Invalid DatabaseType=" + databaseType);
		}
		if(p_panel != null)
			p_panel.fDatabaseType.setSelectedIndex(index);
		else
		{
			dbTypeIndex=index;
			d_dbType = databaseType;
		}
	}	//	setDatabaseType

	/**
	 * @return Returns the databaseType.
	 */
	public String getDatabaseType ()
	{
		if(p_panel != null)
			return (String)p_panel.fDatabaseType.getSelectedItem();
		else
			return u_dbType;
	}
	/**
	 * @return Returns the database Discovered.
	 */
	public String getDatabaseDiscovered ()
	{
		return (String)p_panel.fDatabaseDiscovered.getSelectedItem();
	}
	/**
	 * @param databaseDiscovered The database Discovered to set.
	 */
	public void setDatabaseDiscovered (String databaseDiscovered)
	{
		if(p_panel != null)
			p_panel.fDatabaseDiscovered.setSelectedItem(databaseDiscovered);
	}
	/**
	 *	@param enable Database Discovered
	 */
	public void setDatabaseDiscovered (boolean enable)
	{
		if(p_panel != null)
		{
			p_panel.lDatabaseDiscovered.setEnabled(enable);
			p_panel.fDatabaseDiscovered.setEnabled(enable);
		}
	}

	/**
	 * @return Returns the databaseName.
	 */
	public String getDatabaseName ()
	{
		if(p_panel != null)
			return p_panel.fDatabaseName.getText();
		else
			return u_dbName;
	}
	/**
	 * @param databaseName The databaseName to set.
	 */
	public void setDatabaseName (String databaseName)
	{
		if(p_panel != null)
			p_panel.fDatabaseName.setText(databaseName);
		else
			d_dbName=databaseName;
	}
	/**
	 *	@param enable enable Database Name
	 */
	public void setDatabaseName (boolean enable)
	{
		if(p_panel != null)
			p_panel.fDatabaseName.setEnabled(enable);
	}
	/**
	 * @return Returns the database User Password.
	 */
	public String getDatabasePassword ()
	{
		if(p_panel != null)
		{
			char[] pw = p_panel.fDatabasePassword.getPassword();
			if (pw != null)
				return new String(pw);
		}
		else
			return u_dbPass;
		return "";
	}
	/**
	 * @param databasePassword The databasePassword to set.
	 */
	public void setDatabasePassword (String databasePassword)
	{
		if(p_panel != null)
			p_panel.fDatabasePassword.setText(databasePassword);
		else
			d_dbPass=databasePassword;
	}
	/**
	 * @return Returns the databasePort.
	 */
	public String getDatabasePortString()
	{
		if(p_panel != null)
			return p_panel.fDatabasePort.getText();
		else
			return u_dbPort;
	}	//	getDatabasePort
	/**
	 * @return Returns the databasePort.
	 */
	public int getDatabasePort()
	{
		try
		{
			if(p_panel != null)
				return Integer.parseInt(getDatabasePortString());
			else
				return Integer.parseInt(u_dbPort);
		}
		catch (Exception e)
		{
			setDatabasePort("0");
		}
		return 0;
	}	//	getDatabasePort
	/**
	 * @param databasePort The databasePort to set.
	 */
	public void setDatabasePort (String databasePort)
	{
		if(p_panel != null)
			p_panel.fDatabasePort.setText(databasePort);
		else
			d_dbPort = databasePort;
	}
	/**
	 * @return Returns the databaseServer.
	 */
	public String getDatabaseServer ()
	{
		if(p_panel != null)
			return p_panel.fDatabaseServer.getText();
		else
			return u_dbHost;
	}
	/**
	 * @param databaseServer The databaseServer to set.
	 */
	public void setDatabaseServer (String databaseServer)
	{
		if(p_panel != null)
			p_panel.fDatabaseServer.setText(databaseServer);
		else
			d_dbHost = databaseServer;
	}
	/**
	 * @return Returns the databaseSystemPassword.
	 */
	public String getDatabaseSystemPassword ()
	{
		if(p_panel != null)
		{
			char[] pw = p_panel.fSystemPassword.getPassword();
			if (pw != null)
				return new String(pw);
		}
		else
			return u_dbSystemPass;
		return "";
	}
	/**
	 * @param databaseSystemPassword The databaseSystemPassword to set.
	 */
	public void setDatabaseSystemPassword (String databaseSystemPassword)
	{
		if(p_panel != null)
			p_panel.fSystemPassword.setText(databaseSystemPassword);
		else
			d_dbSystemPass = databaseSystemPassword;
	}
	/**
	 *	@param enable enable Database System Password
	 */
	public void setDatabaseSystemPassword (boolean enable)
	{
		if(p_panel != null)
			p_panel.fSystemPassword.setEnabled(enable);
	}
	/**
	 * @return Returns the databaseUser.
	 */
	public String getDatabaseUser ()
	{
		if(p_panel != null)
			return p_panel.fDatabaseUser.getText();
		else
			return u_dbUser;
	}
	/**
	 * @param databaseUser The databaseUser to set.
	 */
	public void setDatabaseUser (String databaseUser)
	{
		if(p_panel != null)
			p_panel.fDatabaseUser.setText(databaseUser);
		else
			d_dbUser=databaseUser;
	}
	/**
	 *	@param enable enable Database User
	 */
	public void setDatabaseUser (boolean enable)
	{
		if(p_panel != null)
			p_panel.fDatabaseUser.setEnabled(enable);
	}
	
	
	
	
	public boolean createCompiereProperties(String [] args)
	{
		log.setLevel(Level.FINE);
		boolean ok = loadArgs(args);
		
		if(!(ok))
			return false;
		ok=test(true);
		if(!(ok))
			return false;
		
		Ini.setUi(false);
		ok=save();
		if(!(ok))
			return false;
		
		 
		
		return true;
	}
	
	private static void printUsage()
	{
		log.severe("Invalid arguments");
		String usage="--javahome xxx --javatype xxx --compierehome xxx --keystorepass xxx " +
		"--appservhost xxx --appservtype xxx --appservdeploydir xxx --appservjnpport xxx "+
		"--appservwebport xxx --appservsslport xxx --dbhost xxx --dbtype xxx --dbname xxx "+
		"--dbport xxx --dbsystempass xxx --dbuser xxx --dbpass xxx --mailservhost xxx "+
		" --adminemail xxx --mailuser xxx --mailpass xxx ";
		log.severe(usage);
		
	}  // printUsage()
	
	private static void printTestDatabaseUsage()
	{
		log.severe("Invalid arguments");
		String usage="testdatabase --dbhost xxx --dbtype xxx --dbname xxx "+
		"--dbport xxx --dbsystempass xxx --dbuser xxx --dbpass xxx";
		log.severe(usage);
		
	}  // printUsage()
	
	private static void printTestAppServerUsage()
	{
		log.severe("Invalid arguments");
		String usage="--appservhost xxx --appservtype xxx --appservdeploydir xxx --appservjnpport xxx "+
		"--appservwebport xxx --appservsslport xxx ";
		log.severe(usage);
		
	}  // printUsage()
	
	private static void printTestMailServerUsage()
	{
		log.severe("Invalid arguments");
		String usage="--mailservhost xxx "+
		" --adminemail xxx --mailuser xxx --mailpass xxx ";
		log.severe(usage);
		
	}  // printUsage()
	

	/**
	 * 	Process parameters.
	 * 	-t %COMPIERE_DB_TYPE% -h %COMPIERE_DB_SERVER% -p %COMPIERE_DB_PORT% -d %COMPIERE_DB_NAME% -U %COMPIERE_DB_USER% -P %COMPIERE_DB_PASSWORD% -n SystemName -e xxx@com -w pwd
	 *	@param args parameters
	 *	@return true if parsed
	 */
	private boolean loadArgs(String[] args)
	{
		
		
		if (args.length!=0 && args.length != 42)
		{
			printUsage();
			return false;
		}
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].compareTo("--javahome") == 0)
				u_javaHome = args[++i];
			else if (args[i].compareTo("--javatype") == 0)
				u_javaType = args[++i];
			else if (args[i].compareTo("--compierehome") == 0)
				u_compiereHome = args[++i];
			else if (args[i].compareTo("--keystorepass") == 0)
				u_keystorePass = args[++i];
			else if (args[i].compareTo("--appservhost") == 0)
				u_appServHost = args[++i];
			else if (args[i].compareTo("--appservtype") == 0)
				u_appServType = args[++i];
			else if (args[i].compareTo("--appservdeploydir") == 0)
				u_appServDeployDir = args[++i];
			else if (args[i].compareTo("--appservjnpport") == 0)
				u_appServJNPPort = args[++i];
			else if (args[i].compareTo("--appservwebport") == 0)
				u_appServWebPort = args[++i];
			else if (args[i].compareTo("--appservsslport") == 0)
				u_appServSSLPort = args[++i];
			else if (args[i].compareTo("--dbhost") == 0)
				u_dbHost = args[++i];
			else if (args[i].compareTo("--dbtype") == 0)
				u_dbType = args[++i];
			else if (args[i].compareTo("--dbname") == 0)
				u_dbName = args[++i];
			else if (args[i].compareTo("--dbport") == 0)
				u_dbPort = args[++i];
			else if (args[i].compareTo("--dbsystempass") == 0)
				u_dbSystemPass = args[++i];
			else if (args[i].compareTo("--dbuser") == 0)
				u_dbUser = args[++i];
			else if (args[i].compareTo("--dbpass") == 0)
				u_dbPass = args[++i];
			else if (args[i].compareTo("--mailservhost") == 0)
				u_mailServHost = args[++i];
			else if (args[i].compareTo("--adminemail") == 0)
				u_adminEmail = args[++i];
			else if (args[i].compareTo("--mailuser") == 0)
				u_mailUser = args[++i];
			else if (args[i].compareTo("--mailpass") == 0)
				u_mailPass = args[++i];
			else 
			{
				log.severe("Invalid option: " + args[i]);
				printUsage();
				return false;
			}
		}
		
		InetAddress localhost = null;
		String hostName = "unknown";
		try
		{
			localhost = InetAddress.getLocalHost();
			hostName = localhost.getHostName();
		}
		catch (Exception e)
		{
			log.severe("Cannot get local host name");
		}

		//Now load the parameters from env
		//	Load C:\Compiere2\CompiereEnv.properties
		String compiereHome;
		if(u_compiereHome != null)
			compiereHome=u_compiereHome;
		else			
			compiereHome= System.getProperty(COMPIERE_HOME);
		
		if ((compiereHome == null) || (compiereHome.length() == 0))
						compiereHome = System.getProperty("user.dir");
		
		//compiereHome="C:\\Compiere_321\\Compiere2";

		boolean envLoaded = false;
		String fileName = compiereHome + File.separator + COMPIERE_ENV_FILE;
		File env = new File (fileName);
		if (env.exists())
		{
			try
			{
				FileInputStream fis = new FileInputStream(env);
				p_properties.load(fis);
				fis.close();
			}
			catch (Exception e)
			{
				log.warning(e.toString());
			}
			log.info(env.toString());
			if (p_properties.size() > 5)
				envLoaded = true;
		}
			
		//Java Type
		if(u_javaType != null)
			setJavaType(u_javaType);
		else if(((String)p_properties.get(JAVA_TYPE))!=null)
		{	
			u_javaType = (String)p_properties.get(JAVA_TYPE);
			setJavaType((String)p_properties.get(JAVA_TYPE));
		}
		else
		{
			u_javaType = JAVATYPE[0];
			setJavaType(u_javaType);
		}


		initJava();

		if(u_javaHome != null)
			setJavaHome(u_javaHome);
		else if(((String)p_properties.get(JAVA_HOME))!=null)
		{
			u_javaHome=(String)p_properties.get(JAVA_HOME);
			setJavaHome((String)p_properties.get(JAVA_HOME));
		}
		else if(d_javaHome != null)
		{
			u_javaHome=d_javaHome;
			setJavaHome(d_javaHome);
		}



		//COMPIERE_HOME
		if(u_compiereHome != null)
			setCompiereHome(u_compiereHome);
		else if(((String)p_properties.get(COMPIERE_HOME))!=null) 
		{
			u_compiereHome=(String)p_properties.get(COMPIERE_HOME);
			setCompiereHome((String)p_properties.get(COMPIERE_HOME));
		}
		else
		{
			u_compiereHome = compiereHome;
			setCompiereHome(compiereHome);
		}


		if(u_keystorePass != null)
			setKeyStore(u_keystorePass);
		else
		{				
			String s = (String)p_properties.get(COMPIERE_KEYSTOREPASS);
			if ((s == null) || (s.length() == 0))
				s = KEYSTORE_PASSWORD;
			u_keystorePass = s;
			setKeyStore(s);
		}

		//AppsServerType
		if(u_appServType != null)
			setAppsServerType(u_appServType);
		else if (((String)p_properties.get(Environment.COMPIERE_APPS_TYPE)) != null)
		{ 
			u_appServType = (String)p_properties.get(Environment.COMPIERE_APPS_TYPE);
			setAppsServerType((String)p_properties.get(Environment.COMPIERE_APPS_TYPE));
		}
		else if(d_appServType != null)
		{
			d_appServType=u_appServType;
			setAppsServerType(u_appServType);
		}			

		initAppsServer();

		if(u_appServHost != null)
			setAppsServer(u_appServHost);
		else if((String)p_properties.get(Environment.COMPIERE_APPS_SERVER)!=null)
		{
			u_appServHost = (String)p_properties.get(Environment.COMPIERE_APPS_SERVER);
			setAppsServer((String)p_properties.get(Environment.COMPIERE_APPS_SERVER));
		}
		else
		{
			u_appServHost = hostName;
			setAppsServer(hostName);
		}

		if(u_appServDeployDir != null)
		{
			setAppsServerDeployDir(u_appServDeployDir);
		}
		else if(((String)p_properties.get(Environment.COMPIERE_APPS_DEPLOY)) !=null)
		{
			u_appServDeployDir=(String)p_properties.get(Environment.COMPIERE_APPS_DEPLOY);
			setAppsServerDeployDir((String)p_properties.get(Environment.COMPIERE_APPS_DEPLOY));
		}
		else if(d_appServDeployDir != null)
		{
			u_appServDeployDir = d_appServDeployDir;
			setAppsServerDeployDir(u_appServDeployDir);
		}

		if(u_appServJNPPort != null)
		{
			setAppsServerJNPPort(u_appServJNPPort);
		}
		else if (((String)p_properties.get(Environment.COMPIERE_JNP_PORT)) !=null)
		{
			u_appServJNPPort = (String)p_properties.get(Environment.COMPIERE_JNP_PORT);
			setAppsServerJNPPort((String)p_properties.get(Environment.COMPIERE_JNP_PORT));				
		}
		else if(d_appServJNPPort != null)
		{
			u_appServJNPPort=d_appServJNPPort;
			setAppsServerJNPPort(u_appServJNPPort);
		}


		if(u_appServWebPort != null)
		{
			setAppsServerWebPort(u_appServWebPort);
		}
		else if(((String)p_properties.get(Environment.COMPIERE_WEB_PORT)) !=null)
		{
			u_appServWebPort=(String)p_properties.get(Environment.COMPIERE_WEB_PORT);
			setAppsServerWebPort((String)p_properties.get(Environment.COMPIERE_WEB_PORT));	
		}
		else if(d_appServWebPort != null)
		{
			u_appServWebPort=d_appServWebPort;
			setAppsServerWebPort(u_appServWebPort);
		}

		if(u_appServSSLPort != null)
		{
			setAppsServerSSLPort(u_appServSSLPort);
		}
		else if(((String)p_properties.get(Environment.COMPIERE_SSL_PORT)) !=null)
		{
			u_appServSSLPort=(String)p_properties.get(Environment.COMPIERE_SSL_PORT);
			setAppsServerSSLPort((String)p_properties.get(Environment.COMPIERE_SSL_PORT));	
		}
		else if(d_appServSSLPort != null)
		{
			u_appServSSLPort=d_appServSSLPort;
			setAppsServerSSLPort(u_appServSSLPort);
		}


		//
		if(u_dbType != null)
		{
			setDatabaseType(u_dbType);				
		}
		else if(((String)p_properties.get(Environment.COMPIERE_DB_TYPE)) !=null)
		{
			u_dbType=(String)p_properties.get(Environment.COMPIERE_DB_TYPE);
			setDatabaseType((String)p_properties.get(Environment.COMPIERE_DB_TYPE));
		}
		else
		{				
			u_dbType=DBTYPE[0];
			setDatabaseType(u_dbType);	
		}

		if(u_dbName != null)
		{
			initDatabase(u_dbName); //	fills Database Options
		}
		else if(((String)p_properties.get(Environment.COMPIERE_DB_NAME)) != null)
		{
			u_dbName=(String)p_properties.get(Environment.COMPIERE_DB_NAME);
			initDatabase((String)p_properties.get(Environment.COMPIERE_DB_NAME));//	fills Database Options
		}
		else
			initDatabase("");


		setDatabaseDiscovered((String)p_properties.get(Environment.COMPIERE_DB_NAME));

		if(u_dbHost != null)
			setDatabaseServer(u_dbHost);
		else if (((String)p_properties.get(Environment.COMPIERE_DB_SERVER)) !=null)
		{
			u_dbHost=(String)p_properties.get(Environment.COMPIERE_DB_SERVER);
			setDatabaseServer((String)p_properties.get(Environment.COMPIERE_DB_SERVER));
		}
		else
		{
			u_dbHost=hostName;
			setDatabaseServer(hostName);				
		}

		if(u_dbPort != null)
			setDatabasePort(u_dbPort);
		else if(((String)p_properties.get(Environment.COMPIERE_DB_PORT)) !=null)
		{
			u_dbPort=(String)p_properties.get(Environment.COMPIERE_DB_PORT);
			setDatabasePort((String)p_properties.get(Environment.COMPIERE_DB_PORT));
		}
		else if (d_dbPort != null)
		{
			u_dbPort=d_dbPort;
			setDatabasePort(u_dbPort);
		}

		if(u_dbName != null)
			setDatabaseName(u_dbName);
		else if (((String)p_properties.get(Environment.COMPIERE_DB_NAME)) != null)
		{
			u_dbName=(String)p_properties.get(Environment.COMPIERE_DB_NAME);
			setDatabaseName((String)p_properties.get(Environment.COMPIERE_DB_NAME));
		}
		else if(d_dbName != null)
		{
			u_dbName=d_dbName;
			setDatabaseName(u_dbName);
		}

		if(u_dbUser != null)
			setDatabaseUser(u_dbUser);
		else if(((String)p_properties.get(Environment.COMPIERE_DB_USER)) != null)
		{
			u_dbUser = (String)p_properties.get(Environment.COMPIERE_DB_USER);
			setDatabaseUser((String)p_properties.get(Environment.COMPIERE_DB_USER));
		}
		else
		{
			u_dbUser="compiere";
			setDatabaseUser(u_dbUser);
		}


		if(u_dbPass != null)			
			setDatabasePassword(u_dbPass);
		else if(((String)p_properties.get(Environment.COMPIERE_DB_PASSWORD)) != null)
		{
			u_dbPass=(String)p_properties.get(Environment.COMPIERE_DB_PASSWORD);
			setDatabasePassword((String)p_properties.get(Environment.COMPIERE_DB_PASSWORD));
		}
		else
		{
			u_dbPass="compiere";
			setDatabasePassword(u_dbPass);
		}

		if(u_dbSystemPass != null)
		{
			setDatabaseSystemPassword(u_dbSystemPass);
		}
		else if (((String)p_properties.get(Environment.COMPIERE_DB_SYSTEM)) !=null)
		{
			u_dbSystemPass=(String)p_properties.get(Environment.COMPIERE_DB_SYSTEM);
			setDatabaseSystemPassword((String)p_properties.get(Environment.COMPIERE_DB_SYSTEM));
		}
		else
		{
			u_dbSystemPass="";
			setDatabaseSystemPassword(u_dbSystemPass);

		}

		if(u_mailServHost != null)
		{				
		}
		else if(((String)p_properties.get(COMPIERE_MAIL_SERVER)) !=null )
		{
			u_mailServHost=(String)p_properties.get(COMPIERE_MAIL_SERVER);
		}
		else				
		{
			u_mailServHost=hostName;
		}

		if(u_mailUser != null)
		{				
		}
		else if(((String)p_properties.get(COMPIERE_MAIL_USER)) !=null )
		{
			u_mailUser=(String)p_properties.get(COMPIERE_MAIL_USER);
		}
		else				
		{
			u_mailUser="info";
		}

		if(u_mailPass != null)
		{				
		}
		else if(((String)p_properties.get(COMPIERE_MAIL_PASSWORD)) !=null )
		{
			u_mailPass=(String)p_properties.get(COMPIERE_MAIL_PASSWORD);
		}
		else				
		{
			u_mailPass="";
		}

		if(u_adminEmail != null)
		{				
		}
		else if(((String)p_properties.get(COMPIERE_ADMIN_EMAIL)) !=null )
		{
			u_adminEmail=(String)p_properties.get(COMPIERE_ADMIN_EMAIL);
		}
		else				
		{
			u_adminEmail="info@" + hostName;
		}

		
		if (!p_properties.containsKey(COMPIERE_FTP_SERVER))
		{
			p_properties.setProperty(COMPIERE_FTP_SERVER, "localhost");
			p_properties.setProperty(COMPIERE_FTP_USER, "anonymous");
			p_properties.setProperty(COMPIERE_FTP_PASSWORD, "user@host.com");
			p_properties.setProperty(COMPIERE_FTP_PREFIX, "my");
		}
		//	Default Java Options
		if (!p_properties.containsKey(COMPIERE_JAVA_OPTIONS))
			p_properties.setProperty(COMPIERE_JAVA_OPTIONS, "-Xms128M -Xmx1024M -XX:MaxPermSize=128m -XX:NewRatio=3 -XX:+HeapDumpOnOutOfMemoryError");
		//	Web Alias
		if (!p_properties.containsKey(Environment.COMPIERE_WEB_ALIAS) && (localhost != null))
			p_properties.setProperty(Environment.COMPIERE_WEB_ALIAS, localhost.getCanonicalHostName());

		//	(String)p_properties.get(COMPIERE_DB_URL)	//	derived

		//	Keystore Alias
		if (!p_properties.containsKey(COMPIERE_KEYSTORECODEALIAS))
			p_properties.setProperty(COMPIERE_KEYSTORECODEALIAS, "compiere");
		if (!p_properties.containsKey(COMPIERE_KEYSTOREWEBALIAS))
			p_properties.setProperty(COMPIERE_KEYSTOREWEBALIAS, "compiere");		
		
		return true;
	}	// setArgs	
	
	public static void main(String[] args)
	{
		CLogMgt.initialize(true);
		Handler fileHandler = new CLogFile(System.getProperty("user.dir"), false, false);
		CLogMgt.addHandler(fileHandler);
		//	Log Level
		CLogMgt.setLevel(Level.INFO);

		//	File Loger at least FINE
		if (fileHandler.getLevel().intValue() > Level.FINE.intValue())
			fileHandler.setLevel(Level.FINE);
	
		String ret =null;
		if(args != null && args.length > 0 && args[0] != null)
		{
			if (args[0].equals("testdatabase"))
				ret=testDatabase(args);
			else if (args[0].equals("testappserver"))
				ret=testAppServer(args);
			else if (args[0].equals("testmailserver"))
				ret=testMailServer(args);
			else
				ret=createProperties(args);					
		}		
		if(ret != null && ret.length()>1)
		{
			log.info("RETURN-STATUS:"+ret);
			System.exit(1);		
		}
		else		
			log.info("RETURN-STATUS:OK");
		
		System.exit(0);
	}
	
	private static String createProperties(String[] args)
	{		
		Properties	p_properties = new Properties();
		ConfigurationData configData = new ConfigurationData(null,p_properties);
		
		boolean ok = configData.createCompiereProperties(args);
		if(!(ok))
			return "Failed to create Compiere Properties";
		else
			return null;
	}	//	main
	
	/**
	 * 	Test Database
	 *	@return error message or null of OK
	 */
	private static String testDatabase(String[] args)
	{	
		String dbType=null;
		String dbHost=null;
		String dbName=null;
		String dbPort=null;
		String dbSystemPass=null;
		String dbUser=null;
		String dbPass=null;
		
		
		if (args.length!=0 && args.length != 15)
		{
			printTestDatabaseUsage();
			return "Incorrect number of arguments";
		}
		
		for (int i = 1; i < args.length; i++)
		{
			if (args[i].compareTo("--dbhost") == 0)
				dbHost = args[++i];
			else if (args[i].compareTo("--dbtype") == 0)
				dbType = args[++i];
			else if (args[i].compareTo("--dbname") == 0)
				dbName = args[++i];
			else if (args[i].compareTo("--dbport") == 0)
				dbPort = args[++i];
			else if (args[i].compareTo("--dbsystempass") == 0)
				dbSystemPass = args[++i];
			else if (args[i].compareTo("--dbuser") == 0)
				dbUser = args[++i];
			else if (args[i].compareTo("--dbpass") == 0)
				dbPass = args[++i];
			else 
			{
				log.severe("Invalid option: " + args[i]);
				printTestDatabaseUsage();
				return "Invalid option: " + args[i];
			}
		}
		
		Properties	p_properties = new Properties();		
		ConfigurationData configData = new ConfigurationData(null,p_properties);
		
		if((dbType != null) && (dbHost != null) && (dbName != null) && (dbPort != null) &&
				(dbSystemPass != null) && (dbUser != null) && (dbPass != null))
		{
			return configData.validateDatabase(dbType, dbHost, dbName, dbPort,
					dbSystemPass, dbUser, dbPass);
		}
		else
			return "Database information missing";
	}
	
	/**
	 * 	Test App Server
	 *	@return error message or null of OK
	 */
	private static String testAppServer(String[] args)
	{	
		String appServType=null;
		String appServHost=null;
		String appServDeployDir=null;
		String appServWebPort=null;
		String appServJNPPort=null;
		String appServSSLPort=null;
		
		if (args.length!=0 && args.length != 13)
		{
			printTestAppServerUsage();
			return "Incorrect number of arguments";
		}
		for (int i = 1; i < args.length; i++)
		{
			if (args[i].compareTo("--appservhost") == 0)
				appServHost = args[++i];
			else if (args[i].compareTo("--appservtype") == 0)
				appServType = args[++i];
			else if (args[i].compareTo("--appservdeploydir") == 0)
				appServDeployDir = args[++i];
			else if (args[i].compareTo("--appservjnpport") == 0)
				appServJNPPort = args[++i];
			else if (args[i].compareTo("--appservwebport") == 0)
				appServWebPort = args[++i];
			else if (args[i].compareTo("--appservsslport") == 0)
				appServSSLPort = args[++i];
			else 
			{
				log.severe("Invalid option: " + args[i]);
				printTestAppServerUsage();
				return "Invalid option: " + args[i];
			}
		}
		
		Properties	p_properties = new Properties();		
		ConfigurationData configData = new ConfigurationData(null,p_properties);
		
		if((appServType != null) && (appServHost != null) && (appServDeployDir != null) && (appServWebPort != null) &&
				(appServJNPPort != null) && (appServSSLPort != null) )
		{			
			return configData.validateAppServer(appServType, appServHost, appServDeployDir, appServWebPort,
			appServJNPPort, appServSSLPort);

		}
		else
			return "Application Server Information missing";
	}
	
	/**
	 * 	Test Mail Server
	 *	@return error message or null of OK
	 */
	private static String testMailServer(String[] args)
	{	
		String mailServHost=null;
		String adminEmail=null;
		String mailUser=null;
		String mailPass=null;
		
		if (args.length!=0 && args.length != 9)
		{
			printTestMailServerUsage();
			return "Incorrect number of arguments";
		}
		for (int i = 1; i < args.length; i++)
		{
			if (args[i].compareTo("--mailservhost") == 0)
				mailServHost = args[++i];
			else if (args[i].compareTo("--adminemail") == 0)
				adminEmail = args[++i];
			else if (args[i].compareTo("--mailuser") == 0)
				mailUser = args[++i];
			else if (args[i].compareTo("--mailpass") == 0)
				mailPass = args[++i];
			else 
			{
				log.severe("Invalid option: " + args[i]);
				printTestMailServerUsage();
				return "Invalid option: " + args[i];
			}
		}
		
		Properties	p_properties = new Properties();
		
		ConfigurationData configData = new ConfigurationData(null,p_properties);		
		if((mailServHost != null) && (adminEmail != null) && (mailUser != null) && (mailPass != null))
		{			
			return configData.validateMailServer(mailServHost, adminEmail, mailUser, mailPass);

		}
		else
			return "Mail Server Information missing";
	}
	
	/**
	 * 	Validate Mail Server
	 *	@return error message or null of OK
	 */
	public String validateMailServer(String MailServHost, String AdminEmail, String MailUser, String MailPass)
	{
		this.u_adminEmail=AdminEmail;
		this.u_mailServHost=MailServHost;
		this.u_mailUser=MailUser;
		this.u_mailPass=MailPass;
		
		return this.testMail();
	}
	
	/**
	 * 	Validate Database
	 *	@return error message or null of OK
	 */
	public String validateDatabase(String DBType, String DBHost, String DBName, String DBPort,
			String DBSystemPass, String DBUser, String DBPass)
	{	
		
		this.u_dbType=DBType;
		this.u_dbHost=DBHost;
		this.u_dbName=DBName;
		this.u_dbPort=DBPort;
		this.u_dbSystemPass=DBSystemPass;
		this.u_dbUser=DBUser;
		this.u_dbPass=DBPass;
		
		setDatabaseType(DBType);				
		initDatabase(DBName); //	fills Database Options
		setDatabaseServer(DBHost);
		setDatabasePort(DBPort);
		setDatabaseName(DBName);			
		setDatabaseUser(DBUser);			
		setDatabasePassword(DBPass);
		setDatabaseSystemPassword(DBSystemPass);
		
		return testDatabase();
	}
	
	/**
	 * 	Validate Apps Server
	 *	@return error message or null of OK
	 */
	public String validateAppServer(String AppServType, String AppServHost, String AppServDeployDir, String AppServWebPort,
			String AppServJNPPort, String AppServSSLPort)
	{
		
		this.u_appServType=AppServType;
		this.u_appServHost=AppServHost;
		this.u_appServDeployDir=AppServDeployDir;
		this.u_appServWebPort=AppServWebPort;
		this.u_appServJNPPort=AppServJNPPort;
		this.u_appServSSLPort=AppServSSLPort;
		setAppsServerType(AppServType);
		initAppsServer();
		setAppsServer(AppServHost); //	fills Database Options
		setAppsServerDeployDir(AppServDeployDir);
		setAppsServerJNPPort(AppServJNPPort);
		setAppsServerWebPort(AppServWebPort);			
		setAppsServerSSLPort(AppServSSLPort);			
		
		return testAppsServer();

	}

	
}	//	ConfigurationData
