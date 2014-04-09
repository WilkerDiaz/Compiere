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
 * 	@version 	$Id: SetupRes_sl.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_sl extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", 	"Namestavitve Compiere stre\u017enika" },
	{ "Ok", 			"V redu" },
	{ "File", 			"Datoteka" },
	{ "Exit", 			"Izhod" },
	{ "Help", 			"Pomo\u010d" },
	{ "PleaseCheck", 		"Prosim preverite" },
	{ "UnableToConnect", 		"Napaka pri povezavi na Compiere web pomo\u010d" },
	//
	{ "CompiereHomeInfo", 		"Compiere Home je glavni imenik" },
	{ "CompiereHome", 		"Compiere Home" },
	{ "WebPortInfo", 		"Web (HTML) vrata" },
	{ "WebPort", 			"Web vrata" },
	{ "AppsServerInfo", 		"Ime programskega stre\u017enika" },
	{ "AppsServer", 		"Programski stre\u017enik" },
	{ "DatabaseTypeInfo", 		"Tip baze podatkov" },
	{ "DatabaseType", 		"Tip baze podatakov" },
	{ "DatabaseNameInfo", 		"Ime baze podatkov " },
	{ "DatabaseName", 		"Ime baze (SID)" },
	{ "DatabasePortInfo", 		"Vrata Listener programa" },
	{ "DatabasePort", 		"Vrata baze podatkov" },
	{ "DatabaseUserInfo", 		"Uporabni\u0161ko ime Compiere baze podatkov" },
	{ "DatabaseUser", 		"Uporabnik baze podatkov" },
	{ "DatabasePasswordInfo", 	"Geslo uporabnika baze podatkov" },
	{ "DatabasePassword", 		"Geslo baze podatkov" },
	{ "TNSNameInfo", 		"TNS ali globalno ime baze podatkov" },
	{ "TNSName", 			"TNS Ime" },
	{ "SystemPasswordInfo", 	"Geslo uporabnika System" },
	{ "SystemPassword", 		"System geslo" },
	{ "MailServerInfo", 		"Stre\u017enik elektronske po\u0161te" },
	{ "MailServer", 		"Stre\u017enik elektronske po\u0161te" },
	{ "AdminEMailInfo", 		"Elektronski naslov Compiere Skrbnika" },
	{ "AdminEMail", 		"Elektronski naslov Skrbnika" },
	{ "DatabaseServerInfo", 	"Ime stre\u017enika baze podatkov" },
	{ "DatabaseServer", 		"Stre\u017enik baze podatkov" },
	{ "JavaHomeInfo", 		"Doma\u010d imenik Jave" },
	{ "JavaHome", 			"Java imenik" },
	{ "JNPPortInfo", 		"JNP vrata programskega stre\u017enika" },
	{ "JNPPort", 			"JNP vrata" },
	{ "MailUserInfo", 		"Uporabnik elektronske po\u0161te za Compiere" },
	{ "MailUser", 			"Uporabnik elektronske po\u0161te" },
	{ "MailPasswordInfo", 		"Geslo uporabnika elektronske po\u0161te Compiere" },
	{ "MailPassword", 		"Geslo uporabnika elektronske po\u0161te" },
	{ "KeyStorePassword",		"Geslo shrambe klju\u010dev" },
	{ "KeyStorePasswordInfo",	"Geslo za shrambo SSL klju\u010dev" },
	//
	{ "JavaType",				"Java VM"},
	{ "JavaTypeInfo",			"Java VM Dobavitelj"},
	{ "AppsType",				"Tip stre\u017enika"},
	{ "AppsTypeInfo",			"Tip J2EE aplikacijskega stre\u017enika"},
	{ "DeployDir",				"Namestitveni imenik"},
	{ "DeployDirInfo",			"Namestitveni imenik ya J2EE"},
	{ "ErrorDeployDir",			"Napaka namestitveni imenik"},
	//
	{ "TestInfo", 			"Test informacije" },
	{ "Test", 			"Test" },
	{ "SaveInfo", 			"Shrani informacije" },
	{ "Save", 			"Shrani" },
	{ "HelpInfo", 			"Pomo\u010d" },
	//
	{ "ServerError", 		"Napaka v nastavitvah programskega stre\u017enika" },
	{ "ErrorJavaHome", 		"Error napa\u010den doma\u010d imenik Java" },
	{ "ErrorCompiereHome", 		"Error napa\u010den Compiere Home imenik" },
	{ "ErrorAppsServer", 		"Error programski stre\u017enik (ne uporabljaj imena localhost)" },
	{ "ErrorWebPort", 		"Error napa\u010dna Web vrata" },
	{ "ErrorJNPPort", 		"Error napa\u010dna JNP vrata" },
	{ "ErrorDatabaseServer", 	"Error stre\u017enik baze podatkov (ne uporabljaj imena localhost)" },
	{ "ErrorDatabasePort", 		"Error napa\u010dna vrata baze podatkov" },
	{ "ErrorJDBC", 			"Error napaka v JDBC povezavi" },
	{ "ErrorTNS", 			"Error napaka v TNS povezavi" },
	{ "ErrorMailServer", 		"Error stre\u017enik elektronske po\u0161te (ne uporabljaj imena localhost)" },
	{ "ErrorMail", 			"Error napaka elektronska po\u0161ta" },
	{ "ErrorSave", 			"Error napaka pri shranjevanju datoteke" },

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
