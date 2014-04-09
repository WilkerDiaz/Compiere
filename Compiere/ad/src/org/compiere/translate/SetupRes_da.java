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
package org.compiere.translate;

import java.util.*;

/**
 *	Setup Resources
 *
 * 	@version 	$Id: SetupRes_da.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_da extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Compiere: Ops\u00c3\u00a6tning af server" },
	{ "Ok", 				"OK" },
	{ "File", 				"Fil" },
	{ "Exit", 				"Afslut" },
	{ "Help", 				"Hj\u00c3\u00a6lp" },
	{ "PleaseCheck", 		"Kontroll\u00c3\u00a9r" },
	{ "UnableToConnect",	"Kan ikke hente hj\u00c3\u00a6lp fra Compieres web-sted" },

	{ "CompiereHomeInfo", 	"Compiere Home er hovedmappen" },
	{ "CompiereHome", 		"Compiere Home" },
	{ "WebPortInfo", 		"Web (HTML)-port" },
	{ "WebPort", 			"Web-port" },
	{ "AppsServerInfo", 	"Programserverens navn" },
	{ "AppsServer", 		"Prog.-server" },
	{ "DatabaseTypeInfo", 	"Databasetype" },
	{ "DatabaseType", 		"Databasetype" },
	{ "DatabaseNameInfo", 	"Databasenavn " },
	{ "DatabaseName", 		"Databasenavn (SID)" },
	{ "DatabasePortInfo", 	"Database Listener Port" },
	{ "DatabasePort", 		"Databaseport" },
	{ "DatabaseUserInfo", 	"Database: Bruger-ID til Compiere" },
	{ "DatabaseUser", 		"Database: Bruger" },
	{ "DatabasePasswordInfo", "Database: Brugeradgangskode til Compiere" },
	{ "DatabasePassword", 	"Database: Adgangskode" },
	{ "TNSNameInfo", 		"TNS eller Global Database Name" },
	{ "TNSName", 			"TNS-navn" },
	{ "SystemPasswordInfo", "System: Brugeradgangskode" },
	{ "SystemPassword", 	"System-adgangskode" },
	{ "MailServerInfo", 	"Mail-server" },
	{ "MailServer", 		"Mail-server" },
	{ "AdminEMailInfo", 	"Compiere: Administrators e-mail" },
	{ "AdminEMail", 		"Admin. e-mail" },
	{ "DatabaseServerInfo", "Databaseservers navn" },
	{ "DatabaseServer", 	"Databaseserver" },
	{ "JavaHomeInfo", 		"Java Home-mappe" },
	{ "JavaHome", 			"Java Home" },
	{ "JNPPortInfo", 		"Programservers JNP-port" },
	{ "JNPPort", 			"JNP-port" },
	{ "MailUserInfo", 		"Compiere: Mail-bruger" },
	{ "MailUser", 			"Mail: Bruger" },
	{ "MailPasswordInfo", 	"Compiere: Brugeradgangskode til mail" },
	{ "MailPassword", 		"Adgangskode til mail" },
	{ "KeyStorePassword",		"Key Store Password" },
	{ "KeyStorePasswordInfo",	"Password for SSL Key Store" },
	//
	{ "JavaType",				"Java VM"},
	{ "JavaTypeInfo",			"Java VM Vendor"},
	{ "AppsType",				"Server Type"},
	{ "AppsTypeInfo",			"J2EE Application Server Type"},
	{ "DeployDir",				"Deployment"},
	{ "DeployDirInfo",			"J2EE Deployment Directory"},
	{ "ErrorDeployDir",			"Error Deployment Directory"},
	//
	{ "TestInfo", 			"Afpr\u00c3\u00b8v ops\u00c3\u00a6tning" },
	{ "Test", 				"Afpr\u00c3\u00b8v" },
	{ "SaveInfo", 			"Gem ops\u00c3\u00a6tning" },
	{ "Save", 				"Gem" },
	{ "HelpInfo", 			"Hj\u00c3\u00a6lp" },

	{ "ServerError", 		"Fejl: Serverops\u00ef\u00bf\u00bdtning" },
	{ "ErrorJavaHome", 		"Fejl: Java Home" },
	{ "ErrorCompiereHome", 	"Fejl: Compiere Home" },
	{ "ErrorAppsServer", 	"Fejl: Prog.-server (brug ikke localhost)" },
	{ "ErrorWebPort", 		"Fejl: Web-port" },
	{ "ErrorJNPPort", 		"Fejl: JNP-port" },
	{ "ErrorDatabaseServer", "Fejl: Databaseserver (brug ikke localhost)" },
	{ "ErrorDatabasePort", 	"Fejl: Databaseport" },
	{ "ErrorJDBC", 			"Fejl: JDBC-forbindelse" },
	{ "ErrorTNS", 			"Fejl: TNS-forbindelse" },
	{ "ErrorMailServer", 	"Fejl: Mailserver (brug ikke localhost)" },
	{ "ErrorMail", 			"Fejl: Mail" },
	{ "ErrorSave", 			"Fejl: Swing-fil" },

	{ "NewSecurityKey",			"Your new Security Key (lib/CompiereSecure.dat)\n"
		+ "was created for you to be used for Encryption.\n" 
		+ "Please make sure to back it up securely.\n"
		+ "If lost, you are not able to recover encrypted data." },
	{ "SecurityKeyError",		"Error ocured when creating/installing your Security Key.\n"
		+ "Please contact support with Log information." }, 	
	{ "Start",					"Start" },
	{ "Done",					"Finished" },
	{ "SelectOption",			"Select Option" },
	{ "ServerInstall",			"Server Install" },
	{ "CreateNewDatabase",		"Create new Database" },
	{ "DropOldCreateNewDatabase",	"DROP OLD and create NEW Database" },
	{ "MigrateExistingDatabase",	"Migrate Database" },
		
	{ "ErrorProcess",			"Process Error\nPlease check log for details" },
	{ "ServerSetupComplete",	"Server Setup Complete\nPlease Migrate or Import new Database." },
	{ "DropExistingDatabase",	"Do you want to DROP the existing database\n(loosing all existing data)\nand create a NEW database?" }, 
		
	{ "JNDIPort", 				"JNDI Port" },
	{ "ErrorWasClient", 		"Error WAS Client PATH" }
	};

	/**
	 * 	Get Contents
	 * 	@return contents
	 */
	@Override
	public Object[][] getContents()
	{
		return contents;
	}	//	getContents

}	//	SetupRes_da

