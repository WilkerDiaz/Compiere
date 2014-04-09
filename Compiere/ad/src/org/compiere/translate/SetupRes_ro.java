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
 * 	@version 	$Id: SetupRes_ro.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_ro extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", 	"Configurarea serverului Compiere" },
	{ "Ok", 					"OK" },
	{ "File", 					"Aplica\u0163ie" },
	{ "Exit", 					"Ie\u015fire" },
	{ "Help", 					"Ajutor" },
	{ "PleaseCheck", 			"Consulta\u0163i" },
	{ "UnableToConnect", 		"Nu s-a putut ob\u0163ine ajutor de pe site-ul web al Compiere" },
	//
	{ "CompiereHomeInfo", 		"Loca\u0163ia Compiere reprezint\u0103 directorul s\u0103u de instalare" },
	{ "CompiereHome", 			"Loca\u0163ie Compiere" },
	{ "WebPortInfo", 			"Portul de web (HTML)" },
	{ "WebPort", 				"Port de web" },
	{ "AppsServerInfo", 		"Numele serverului de aplica\u0163ie" },
	{ "AppsServer", 			"Server de aplica\u0163ie" },
	{ "DatabaseTypeInfo", 		"Tipul bazei de date" },
	{ "DatabaseType", 			"Tip de baz\u0103 de date" },
	{ "DatabaseNameInfo", 		"Numele (serviciului) bazei de date" },
	{ "DatabaseName", 			"Nume de baz\u0103 de date" },
	{ "DatabasePortInfo", 		"Portul rezevat serviciului bazei de date" },
	{ "DatabasePort", 			"Port de baz\u0103 de date" },
	{ "DatabaseUserInfo", 		"Utilizatorul Compiere pentru baza de date" },
	{ "DatabaseUser", 			"Utilizator de baz\u0103 de date" },
	{ "DatabasePasswordInfo", 	"Parola utilizatorului Compiere pentru baza de date" },
	{ "DatabasePassword", 		"Parola pentru baza de date" },
	{ "TNSNameInfo", 			"Baze de date g\u0103site" },
	{ "TNSName", 				"C\u0103utare de baze de date" },
	{ "SystemPasswordInfo", 	"Parola utilizatorului System" },
	{ "SystemPassword", 		"Parol\u0103 pentru System" },
	{ "MailServerInfo", 		"Server de po\u015ft\u0103 electronic\u0103" },
	{ "MailServer", 			"Server de po\u015ft\u0103 electronic\u0103" },
	{ "AdminEMailInfo", 		"Adresa de po\u015ft\u0103 electronic\u0103 a administratorului Compiere" },
	{ "AdminEMail", 			"Adres\u0103 de e-mail a administratorului" },
	{ "DatabaseServerInfo", 	"Numele serverului de baz\u0103 de date" },
	{ "DatabaseServer", 		"Server de baz\u0103 de date" },
	{ "JavaHomeInfo", 			"Loca\u0163ia de instalare a Java" },
	{ "JavaHome", 				"Loca\u0163ie Java" },
	{ "JNPPortInfo", 			"Portul JNP al serverului de aplica\u0163ie" },
	{ "JNPPort", 				"Port JNP" },
	{ "MailUserInfo", 			"Utilizatorul Compiere pentru po\u015fta electronic\u0103" },
	{ "MailUser", 				"Utilizator de po\u015ft\u0103 electronic\u0103" },
	{ "MailPasswordInfo", 		"Parola utilizatorului Compiere pentru po\u015fta electronic\u0103" },
	{ "MailPassword", 			"Parol\u0103 de po\u015ft\u0103 electronic\u0103" },
	{ "KeyStorePassword",		"Parol\u0103 de keystore" },
	{ "KeyStorePasswordInfo",	"Parola de pentru arhiva de chei SSL" },
	//
	{ "JavaType",				"Ma\u015fina virtual\u0103 Java"},
	{ "JavaTypeInfo",			"Furnizorul ma\u015finii virtuale Java"},
	{ "AppsType",				"Tip de server"},
	{ "AppsTypeInfo",			"Tipul serverului de aplica\u0163ie J2EE"},
	{ "DeployDir",				"Director de instalare"},
	{ "DeployDirInfo",			"Directorul J2EE de instalare"},
	{ "ErrorDeployDir",			"Director de instalare incorect"},
	//
	{ "TestInfo", 				"Testarea configur\u0103rii" },
	{ "Test", 					"Testare" },
	{ "SaveInfo", 				"Salvarea configur\u0103rii" },
	{ "Save", 					"Salvare" },
	{ "HelpInfo", 				"Ob\u0163inere de ajutor" },
	//
	{ "ServerError", 			"Eroare de configurare a serverului" },
	{ "ErrorJavaHome", 			"Eroare de loca\u0163ie Java" },
	{ "ErrorCompiereHome", 		"Eroare de loca\u0163ie Compiere" },
	{ "ErrorAppsServer", 		"Eroare de server de aplica\u0163ie (nu folosi\u0163i 'localhost')" },
	{ "ErrorWebPort", 			"Eroare de port de web" },
	{ "ErrorJNPPort", 			"Eroare de port JNP" },
	{ "ErrorDatabaseServer", 	"Eroare de server de baz\u0103 de date (nu folosi\u0163i 'localhost')" },
	{ "ErrorDatabasePort", 		"Eroare de port de baz\u0103 de date" },
	{ "ErrorJDBC", 				"Eroare de conexiune JDBC" },
	{ "ErrorTNS", 				"Eroare de conexiune TNS" },
	{ "ErrorMailServer", 		"Eroare de server de po\u015ft\u0103 electronic\u0103 (nu folosi\u0163i 'localhost')" },
	{ "ErrorMail", 				"Eroare de po\u015ft\u0103 electronic\u0103" },
	{ "ErrorSave", 				"Eroare la salvarea fi\u015fierului" },

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

}	//	SetupRes
