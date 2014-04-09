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
 *	Swedish Setup Resource Translation
 *
 * 	@version 	$Id: SetupRes_sv.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_sv extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Compiere server installationsprogram" },
	{ "Ok", 				"Ok" },
	{ "File", 				"Fil" },
	{ "Exit", 				"Avsluta" },
	{ "Help", 				"Hj\u00e4lp" },
	{ "PleaseCheck", 		"Kolla" },
	{ "UnableToConnect",	"Kan inte f\u00e5 hj\u00e4lp fr\u00e5n Compiere Web Site" },

	{ "CompiereHomeInfo", 	"Compiere hem \u00e4r huvudkatalog" },
	{ "CompiereHome", 		"Compiere hem" },
	{ "WebPortInfo", 		"Web (HTML) port" },
	{ "WebPort", 			"Web port" },
	{ "AppsServerInfo", 	"Program server name" },
	{ "AppsServer", 		"Program server" },
	{ "DatabaseTypeInfo", 	"Databastyp" },
	{ "DatabaseType", 		"Databastyp" },
	{ "DatabaseNameInfo", 	"Databas namn " },
	{ "DatabaseName", 		"Databas namn (SID)" },
	{ "DatabasePortInfo", 	"Databas avlyssningsport" },
	{ "DatabasePort", 		"Databas port" },
	{ "DatabaseUserInfo", 	"Databas Compiere anv\u00e4ndarnamn" },
	{ "DatabaseUser", 		"Databas anv\u00e4ndarnamn" },
	{ "DatabasePasswordInfo", "Databas Compiere anv\u00e4ndare l\u00f6senord" },
	{ "DatabasePassword", 	"Databas l\u00f6senord" },
	{ "TNSNameInfo", 		"TNS eller global databas namn" },
	{ "TNSName", 			"TNS namn" },
	{ "SystemPasswordInfo", "System anv\u00e4ndare l\u00f6senord" },
	{ "SystemPassword", 	"System l\u00f6senord" },
	{ "MailServerInfo", 	"Post server" },
	{ "MailServer", 		"Post server" },
	{ "AdminEMailInfo", 	"Compiere administrat\u00f6r e-post" },
	{ "AdminEMail", 		"Admin e-post" },
	{ "DatabaseServerInfo", "Databas server namn" },
	{ "DatabaseServer", 	"Databas server" },
	{ "JavaHomeInfo", 		"Java hemkatalog" },
	{ "JavaHome", 			"Java hem" },
	{ "JNPPortInfo", 		"Program server JNP port" },
	{ "JNPPort", 			"JNP port" },
	{ "MailUserInfo", 		"Compiere post anv\u00e4ndare" },
	{ "MailUser", 			"Post anv\u00e4ndare" },
	{ "MailPasswordInfo", 	"Compiere post anv\u00e4ndare l\u00f6senord" },
	{ "MailPassword", 		"Post l\u00f6senord" },
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
	{ "TestInfo", 			"Testa inst\u00e4llningar" },
	{ "Test", 				"Testa" },
	{ "SaveInfo", 			"Spara inst\u00e4llningar" },
	{ "Save", 				"Spara" },
	{ "HelpInfo", 			"Hj\u00e4lp" },

	{ "ServerError", 		"Server inst\u00e4llningsfel" },
	{ "ErrorJavaHome", 		"Fel Java hem" },
	{ "ErrorCompiereHome", 	"Fel Compiere hem" },
	{ "ErrorAppsServer", 	"Fel program server (anv\u00e4nd ej localhost)" },
	{ "ErrorWebPort", 		"Fel web port" },
	{ "ErrorJNPPort", 		"Fel JNP port" },
	{ "ErrorDatabaseServer", "Fel databas server (anv\u00e4nd ej localhost)" },
	{ "ErrorDatabasePort", 	"Fel databas port" },
	{ "ErrorJDBC", 			"Fel JDBC anslutning" },
	{ "ErrorTNS", 			"Fel TNS anslutning" },
	{ "ErrorMailServer", 	"Fel post server (anv\u00e4nd ej localhost)" },
	{ "ErrorMail", 			"Fel post" },
	{ "ErrorSave", 			"Fel swing fil" },

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
