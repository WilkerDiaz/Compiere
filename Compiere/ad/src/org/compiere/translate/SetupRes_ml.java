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
 * 	@version 	$Id: SetupRes_ml.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_ml extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Compiere Server Setup" },
	{ "Ok", 				"Ok" },
	{ "File", 				"File" },
	{ "Exit", 				"Exit" },
	{ "Help", 				"Help" },
	{ "PleaseCheck", 		"Please Check" },
	{ "UnableToConnect",	"Unable get help from Compiere Web Site" },

	{ "CompiereHomeInfo", 	"Compiere Home is the main Folder" },
	{ "CompiereHome", 		"Compiere Home" },
	{ "WebPortInfo", 		"Web (HTML) Port" },
	{ "WebPort", 			"Web Port" },
	{ "AppsServerInfo", 	"Application Server Name" },
	{ "AppsServer", 		"Apps Server" },
	{ "DatabaseTypeInfo", 	"Database Type" },
	{ "DatabaseType", 		"Database Type" },
	{ "DatabaseNameInfo", 	"Database Name " },
	{ "DatabaseName", 		"Database Name (SID)" },
	{ "DatabasePortInfo", 	"Database Listener Port" },
	{ "DatabasePort", 		"Database Port" },
	{ "DatabaseUserInfo", 	"Database Compiere User ID" },
	{ "DatabaseUser", 		"Database User" },
	{ "DatabasePasswordInfo", "Database Compiere User Password" },
	{ "DatabasePassword", 	"Database Password" },
	{ "TNSNameInfo", 		"TNS or Global Database Name" },
	{ "TNSName", 			"TNS Name" },
	{ "SystemPasswordInfo", "System User Password" },
	{ "SystemPassword", 	"System Password" },
	{ "MailServerInfo", 	"Mail Server" },
	{ "MailServer", 		"Mail Server" },
	{ "AdminEMailInfo", 	"Compiere Administrator EMail" },
	{ "AdminEMail", 		"Admin EMail" },
	{ "DatabaseServerInfo", "Database Server Name" },
	{ "DatabaseServer", 	"Database Server" },
	{ "JavaHomeInfo", 		"Java Home Folder" },
	{ "JavaHome", 			"Java Home" },
	{ "JNPPortInfo", 		"Application Server JNP Port" },
	{ "JNPPort", 			"JNP Port" },
	{ "MailUserInfo", 		"Compiere Mail User" },
	{ "MailUser", 			"Mail User" },
	{ "MailPasswordInfo", 	"Compiere Mail User Password" },
	{ "MailPassword", 		"Mail Password" },
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
	{ "TestInfo", 			"Test the Setup" },
	{ "Test", 				"Test" },
	{ "SaveInfo", 			"Save the Setup" },
	{ "Save", 				"Save" },
	{ "HelpInfo", 			"Get Help" },

	{ "ServerError", 		"Server Setup Error" },
	{ "ErrorJavaHome", 		"Error Java Home" },
	{ "ErrorCompiereHome", 	"Error Compiere Home" },
	{ "ErrorAppsServer", 	"Error Apps Server (do not use localhost)" },
	{ "ErrorWebPort", 		"Error Web Port" },
	{ "ErrorJNPPort", 		"Error JNP Port" },
	{ "ErrorDatabaseServer", "Error Database Server (do not use localhost)" },
	{ "ErrorDatabasePort", 	"Error Database Port" },
	{ "ErrorJDBC", 			"Error JDBC Connection" },
	{ "ErrorTNS", 			"Error TNS Connection" },
	{ "ErrorMailServer", 	"Error Mail Server (do not use localhost)" },
	{ "ErrorMail", 			"Error Mail" },
	{ "ErrorSave", 			"Error Sving File" },

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

}	//	SerupRes
