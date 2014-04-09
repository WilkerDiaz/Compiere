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
 *	Norwegian Setup Resource Translation
 *
 * 	@version 	$Id: SetupRes_no.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_no extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Compiere Server Oppsett" },
	{ "Ok", 				"Ok" },
	{ "File", 				"Fil" },
	{ "Exit", 				"Avslutt" },
	{ "Help", 				"Hjelp" },
	{ "PleaseCheck", 		"Vennligst Sjekk" },
	{ "UnableToConnect",	"Umulig \u00e5 hente hjelp fra Compiere Web Side" },

	{ "CompiereHomeInfo", 	"Compiere Hjem er i hoved Mappen" },
	{ "CompiereHome", 		"Compiere Hjem" },
	{ "WebPortInfo", 		"Web (HTML) Port" },
	{ "WebPort", 			"Web Port" },
	{ "AppsServerInfo", 	"Applikasion Server Navn" },
	{ "AppsServer", 		"App. Server" },
	{ "DatabaseTypeInfo", 	"Database Type" },
	{ "DatabaseType", 		"Database Type" },
	{ "DatabaseNameInfo", 	"Database Navn " },
	{ "DatabaseName", 		"Database Navn (SID)" },
	{ "DatabasePortInfo", 	"Database Listener Port" },
	{ "DatabasePort", 		"Database Port" },
	{ "DatabaseUserInfo", 	"Database Compiere Bruker ID" },
	{ "DatabaseUser", 		"Database Bruker" },
	{ "DatabasePasswordInfo", "Database Compiere Bruker Passord" },
	{ "DatabasePassword", 	"Database Passord" },
	{ "TNSNameInfo", 		"TNS eller Global Database Navn" },
	{ "TNSName", 			"TNS Navn" },
	{ "SystemPasswordInfo", "System Bruker Passord" },
	{ "SystemPassword", 	"System Passord" },
	{ "MailServerInfo", 	"Epost Server" },
	{ "MailServer", 		"Epost Server" },
	{ "AdminEMailInfo", 	"Compiere Administrator EPost" },
	{ "AdminEMail", 		"Admin EPost" },
	{ "DatabaseServerInfo", "Database Server Navn" },
	{ "DatabaseServer", 	"Database Server" },
	{ "JavaHomeInfo", 		"Java Hjem Katalog" },
	{ "JavaHome", 			"Java Hjem" },
	{ "JNPPortInfo", 		"Aplikasions Server JNP Port" },
	{ "JNPPort", 			"JNP Port" },
	{ "MailUserInfo", 		"Compiere EPost User" },
	{ "MailUser", 			"EPost User" },
	{ "MailPasswordInfo", 	"Compiere EPost Bruker Passord" },
	{ "MailPassword", 		"EPost Passord" },
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
	{ "TestInfo", 			"Test Oppsettet" },
	{ "Test", 				"Test" },
	{ "SaveInfo", 			"Lagre Oppsett" },
	{ "Save", 				"Lagre" },
	{ "HelpInfo", 			"Hent Hjelp" },

	{ "ServerError", 		"Server Oppsett Feil" },
	{ "ErrorJavaHome", 		"Feil Java Hjem" },
	{ "ErrorCompiereHome", 	"Feil Compiere Hjem" },
	{ "ErrorAppsServer", 	"Feil App. Server (ikke bruk localhost)" },
	{ "ErrorWebPort", 		"Feil Web Port" },
	{ "ErrorJNPPort", 		"Feil JNP Port" },
	{ "ErrorDatabaseServer", "Feil Database Server (ikke bruk localhost)" },
	{ "ErrorDatabasePort", 	"Feil Database Port" },
	{ "ErrorJDBC", 			"Feil ved JDBC Oppkobling" },
	{ "ErrorTNS", 			"Feil ved TNS Oppkobling" },
	{ "ErrorMailServer", 	"Feil EPost Server (ikke bruk localhost)" },
	{ "ErrorMail", 			"Feil EPost" },
	{ "ErrorSave", 			"Feil Sving Fil" },

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
