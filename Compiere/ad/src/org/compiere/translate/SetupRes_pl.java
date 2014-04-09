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
 * 	@version 	$Id: SetupRes_pl.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_pl extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Konfiguracja serwera Compiere" },
	{ "Ok", 				"Ok" },
	{ "File", 				"Plik" },
	{ "Exit", 				"Wyj\u015bcie" },
	{ "Help", 				"Pomoc" },
	{ "PleaseCheck", 		"Prosz\u0119 sprawdzi\u0107" },
	{ "UnableToConnect",	"Nie mo\u017cna po\u0142\u0105czy\u0107 si\u0119 ze stron\u0105 Compiere w celu uzyskania pomocy" },

	{ "CompiereHomeInfo", 	"Folder Compiere jest folderem g\u0142\u00f3wnym" },
	{ "CompiereHome", 		"Folder Compiere" },
	{ "WebPortInfo", 		"Web (HTML) Port" },
	{ "WebPort", 			"Web Port" },
	{ "AppsServerInfo", 	"Nazwa serwera aplikacji" },
	{ "AppsServer", 		"Serwer bazy danych" },
	{ "DatabaseTypeInfo", 	"Typ bazy danych" },
	{ "DatabaseType", 		"Typ bazy danych" },
	{ "DatabaseNameInfo", 	"Nazwa bazy danych " },
	{ "DatabaseName", 		"Nazwa bazy danych (SID)" },
	{ "DatabasePortInfo", 	"Port listenera bazy danych" },
	{ "DatabasePort", 		"Port bazy danych" },
	{ "DatabaseUserInfo", 	"U\u017cytkownik Compiere w bazie danych" },
	{ "DatabaseUser", 		"U\u017cytkownik bazy" },
	{ "DatabasePasswordInfo", "Has\u0142o u\u017cytkownika Compiere" },
	{ "DatabasePassword", 	"Has\u0142o u\u017cytkownika" },
	{ "TNSNameInfo", 		"TNS lub Globalna Nazwa Bazy (dla Oracle)" },
	{ "TNSName", 			"Nazwa TNS" },
	{ "SystemPasswordInfo", "Has\u0142o dla u\u017cytkownika System w bazie danych" },
	{ "SystemPassword", 	"Has\u0142o System" },
	{ "MailServerInfo", 	"Serwer pocztowy" },
	{ "MailServer", 		"Serwer pocztowy" },
	{ "AdminEMailInfo", 	"Adres email administartora Compiere" },
	{ "AdminEMail", 		"EMail administ." },
	{ "DatabaseServerInfo", "Nazwa serwera bazy danych" },
	{ "DatabaseServer", 	"Serwer bazy danych" },
	{ "JavaHomeInfo", 		"Folder Javy" },
	{ "JavaHome", 			"Folder Javy" },
	{ "JNPPortInfo", 		"Application Server JNP Port" },
	{ "JNPPort", 			"JNP Port" },
	{ "MailUserInfo", 		"U\u017cytkownik poczty dla cel\u00f3w administracyjnych Compiere" },
	{ "MailUser", 			"U\u017cytkownik poczty" },
	{ "MailPasswordInfo", 	"Has\u0142o dla konta pocztowego Compiere" },
	{ "MailPassword", 		"Has\u0142o poczty" },
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
	{ "TestInfo", 			"Sprawd\u017a ustawienia" },
	{ "Test", 				"Testuj" },
	{ "SaveInfo", 			"Zapisz ustawienia" },
	{ "Save", 				"Zapisz" },
	{ "HelpInfo", 			"Pomoc" },

	{ "ServerError", 		"B\u0142\u0119dne ustawienia" },
	{ "ErrorJavaHome", 		"Niepoprawny folder Javy" },
	{ "ErrorCompiereHome", 	"Nie stwierdzono zainstalowanego systemu Compiere w miescu wskazanym jako Folder Compiere" },
	{ "ErrorAppsServer", 	"Niepoprawny serwer aplikacji (nie mo\u017ce by\u0107 localhost)" },
	{ "ErrorWebPort", 		"Niepoprawny port WWW (by\u0107 mo\u017ce inna aplikacja u\u017cywa ju\u017c tego portu)" },
	{ "ErrorJNPPort", 		"Niepoprawny port JNP (by\u0107 mo\u017ce inna aplikacja u\u017cywa ju\u017c tego portu)" },
	{ "ErrorDatabaseServer", "Niepoprawny serwer bazy (nie mo\u017ce by\u0107 localhost)" },
	{ "ErrorDatabasePort", 	"Niepoprawny port serwer bazy" },
	{ "ErrorJDBC", 			"Wyst\u0105pi\u0142 b\u0142\u0105d przy pr\u00f3bie po\u0142\u0105cznia si\u0119 z baz\u0105 danych" },
	{ "ErrorTNS", 			"Wyst\u0105pi\u0142 b\u0142\u0105d przy pr\u00f3bie po\u0142\u0105cznia si\u0119 z baz\u0105 danych poprzez TNS" },
	{ "ErrorMailServer", 	"Niepoprawny serwer pocztowy (nie mo\u017ce by\u0107 localhost)" },
	{ "ErrorMail", 			"B\u0142\u0105d poczty" },
	{ "ErrorSave", 			"B\u0142\u0105d przy zapisywaniu konfiguracji" },

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
