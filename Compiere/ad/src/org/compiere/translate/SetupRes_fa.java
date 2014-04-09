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
 * 	@version 	$Id: SetupRes_fa.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_fa extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", 	"\u0628\u0631\u067e\u0627\u06a9\u0631\u062f\u0646 \u0633\u0631\u0648\u0631 \u06a9\u0627\u0645\u067e\u064a\u0631\u0647" },
	{ "Ok", 					"\u0642\u0628\u0648\u0644" },
	{ "File", 					"\u0642\u0627\u064a\u0644" },
	{ "Exit", 					"\u062e\u0631\u0648\u062c" },
	{ "Help", 					"\u0631\u0627\u0647\u0646\u0645\u0627\u0626\u06cc" },
	{ "PleaseCheck", 			"\u0644\u0637\u0641\u0627 \u0628\u0631\u0631\u0633\u06cc \u06a9\u0646\u064a\u062f" },
	{ "UnableToConnect", 		"\u0642\u0627\u062f\u0631 \u0628\u0647 \u062f\u0631\u064a\u0627\u0641\u062a \u0631\u0627\u0647\u0646\u0645\u0627\u064a\u06cc \u0627\u0632 \u0633\u0627\u064a\u062a \u06a9\u0627\u0645\u067e\u064a\u0631\u0647 \u0646\u0634\u062f\u0645" },
	//
	{ "CompiereHomeInfo", 		"\u06a9\u0627\u0645\u067e\u064a\u0631\u0647 \u0647\u0648\u0645 \u0641\u0648\u0644\u062f\u0631 \u0627\u0635\u0644\u06cc \u0627\u0633\u062a" },
	{ "CompiereHome", 			"\u06a9\u0627\u0645\u067e\u064a\u0631\u0647 \u0647\u0648\u0645" },
	{ "WebPortInfo", 			"\u062f\u0631\u06af\u0627\u0647 \u0648\u0628" },
	{ "WebPort", 				"\u062f\u0631\u06af\u0627\u0647 \u0648\u0628" },
	{ "AppsServerInfo", 		"\u0646\u0627\u0645 \u0633\u0631\u0648\u0631 \u06a9\u0627\u0631\u0628\u0631\u062f" },
	{ "AppsServer", 			"\u0633\u0631\u0648\u0631 \u06a9\u0627\u0631\u0628\u0631\u062f" },
	{ "DatabaseTypeInfo", 		"\u0646\u0648\u0639 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a\u06cc" },
	{ "DatabaseType", 			"\u0646\u0648\u0639 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a\u06cc" },
	{ "DatabaseNameInfo", 		"\u0646\u0627\u0645 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a\u06cc" },
	{ "DatabaseName", 			"\u0646\u0627\u0645 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a\u06cc" },
	{ "DatabasePortInfo", 		"\u062f\u0631\u06af\u0627\u0647 \u0634\u0646\u0648\u062f \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a\u06cc" },
	{ "DatabasePort", 			"\u062f\u0631\u06af\u0627\u0647 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a\u06cc" },
	{ "DatabaseUserInfo", 		"\u0645\u0634\u062e\u0635\u0647 \u0627\u0633\u062a\u0641\u0627\u062f\u0647 \u06a9\u0646\u0646\u062f\u0647 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a\u06cc \u06a9\u0627\u0645\u067e\u064a\u0631\u0647" },
	{ "DatabaseUser", 			"\u0627\u0633\u062a\u0641\u0627\u062f\u0647 \u06a9\u0646\u0646\u062f\u0647 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a\u06cc" },
	{ "DatabasePasswordInfo", 	"\u06a9\u0644\u0645\u0647 \u0639\u0628\u0648\u0631 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a\u06cc \u06a9\u0627\u0645\u067e\u064a\u0631\u0647" },
	{ "DatabasePassword", 		"\u06a9\u0644\u0645\u0647 \u0639\u0628\u0648\u0631 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a\u06cc" },
	{ "TNSNameInfo", 			"\u062a\u06cc \u0627\u0646 \u0627\u0633 \u064a\u0627 \u0646\u0627\u0645 \u062c\u0647\u0627\u0646\u06cc \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a" },
	{ "TNSName", 				"\u0646\u0627\u0645 \u062a\u06cc \u0627\u0646 \u0627\u0633" },
	{ "SystemPasswordInfo", 	"\u06a9\u0644\u0645\u0647 \u0639\u0628\u0648\u0631 \u0627\u0633\u062a\u0641\u0627\u062f\u0647 \u06a9\u0646\u0646\u062f\u0647 \u0633\u064a\u0633\u062a\u0645" },
	{ "SystemPassword", 		"\u06a9\u0644\u0645\u0647 \u0639\u0628\u0648\u0631 \u0627\u0633\u062a\u0641\u0627\u062f\u0647 \u06a9\u0646\u0646\u062f\u0647 \u0633\u064a\u0633\u062a\u0645" },
	{ "MailServerInfo", 		"\u0633\u0631\u0648\u0631 \u0646\u0627\u0645\u0647" },
	{ "MailServer", 			"\u0633\u0631\u0648\u0631 \u0646\u0627\u0645\u0647" },
	{ "AdminEMailInfo", 		"\u0646\u0627\u0645\u0647 \u0627\u0644\u06a9\u062a\u0631\u0648\u0646\u064a\u06a9\u06cc \u0646\u0627\u0638\u0631 \u06a9\u0627\u0645\u067e\u064a\u0631\u0647" },
	{ "AdminEMail", 			"\u0646\u0627\u0645\u0647 \u0627\u0644\u06a9\u062a\u0631\u0648\u0646\u064a\u06a9 \u0646\u0627\u0638\u0631" },
	{ "DatabaseServerInfo", 	"\u0646\u0627\u0645 \u0633\u0631\u0648\u0631 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a" },
	{ "DatabaseServer", 		"\u0633\u0631\u0648\u0631 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a" },
	{ "JavaHomeInfo", 			"\u0641\u0648\u0644\u062f\u0631 \u062e\u0627\u0646\u06af\u06cc \u062c\u0627\u0648\u0627" },
	{ "JavaHome", 				"\u062e\u0627\u0646\u0647 \u062c\u0627\u0648\u0627" },
	{ "JNPPortInfo", 			"\u062f\u0631\u06af\u0627\u0647 \u062c\u06cc \u0627\u0646 \u067e\u06cc \u06a9\u0627\u0631\u0628\u0631\u062f" },
	{ "JNPPort", 				"\u062f\u0631\u06af\u0627\u0647 \u062c\u06cc \u0627\u0646 \u067e\u06cc" },
	{ "MailUserInfo", 			"\u0627\u0633\u062a\u0641\u0627\u062f\u0647 \u06a9\u0646\u0646\u062f\u0647 \u0646\u0627\u0645\u0647 \u06a9\u0627\u0645\u067e\u064a\u0631\u0647" },
	{ "MailUser", 				"\u0627\u0633\u062a\u0641\u0627\u062f\u0647 \u06a9\u0646\u0646\u062f\u0647 \u0646\u0627\u0645\u0647" },
	{ "MailPasswordInfo", 		"\u06a9\u0644\u0645\u0647 \u0639\u0628\u0648\u0631 \u0627\u0633\u062a\u0641\u0627\u062f\u0647 \u06a9\u0646\u0646\u062f\u0647 \u06a9\u0627\u0645\u067e\u064a\u0631\u0647" },
	{ "MailPassword", 			"\u06a9\u0644\u0645\u0647 \u0639\u0628\u0648\u0631 \u0646\u0627\u0645\u0647" },
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
	{ "TestInfo", 				"\u062a\u0646\u0638\u064a\u0645\u0627\u062a \u0631\u0627 \u0622\u0632\u0645\u0627\u064a\u0634 \u06a9\u0646\u064a\u062f" },
	{ "Test", 					"\u0622\u0632\u0645\u0627\u064a\u0634" },
	{ "SaveInfo", 				"\u062a\u0646\u0638\u064a\u0645\u0627\u062a \u0631\u0627 \u0630\u062e\u064a\u0631\u0647 \u06a9\u0646\u064a\u0623" },
	{ "Save", 					"\u0630\u062e\u064a\u0631\u0647" },
	{ "HelpInfo", 				"\u0631\u0627\u0647\u0646\u0645\u0627\u064a\u06cc \u0628\u06af\u064a\u0631\u064a\u062f" },
	//
	{ "ServerError", 			"\u062e\u0637\u0627 \u062f\u0631 \u062a\u0646\u0638\u064a\u0645\u0627\u062a \u0633\u0631\u0648\u0631" },
	{ "ErrorJavaHome", 			"\u062e\u0627\u0646\u0647 \u062c\u0627\u0648\u0627 \u0627\u0634\u062a\u0628\u0627\u0647 \u0627\u0633\u062a" },
	{ "ErrorCompiereHome", 		"\u062e\u0627\u0646\u0647 \u06a9\u0627\u0645\u067e\u064a\u0631\u0647 \u0627\u0634\u062a\u0628\u0627\u0647 \u0627\u0633\u062a" },
	{ "ErrorAppsServer", 		"\u062e\u0637\u0627 \u062f\u0631 \u0633\u0631\u0648\u0631 \u06a9\u0627\u0631\u0628\u0631\u062f" },
	{ "ErrorWebPort", 			"\u062e\u0637\u0627 \u062f\u0631 \u062f\u0631\u06af\u0627\u0647 \u0648\u0628" },
	{ "ErrorJNPPort", 			"\u062e\u0637\u0627 \u062f\u0631 \u062f\u0631\u06af\u0627\u0647 \u062c\u06cc \u0627\u0646 \u067e\u06cc" },
	{ "ErrorDatabaseServer", 	"\u062e\u0637\u0627 \u062f\u0631 \u0633\u0631\u0648\u0631 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a" },
	{ "ErrorDatabasePort", 		"\u062e\u0637\u0627 \u062f\u0631 \u062f\u0631\u06af\u0627\u0647 \u0628\u0627\u0646\u06a9 \u0627\u0637\u0644\u0627\u0639\u0627\u062a" },
	{ "ErrorJDBC", 				"\u062e\u0637\u0627 \u062f\u0631 \u0627\u062a\u0635\u0627\u0644 \u062c\u06cc \u062f\u06cc \u0628\u06cc \u0633\u06cc" },
	{ "ErrorTNS", 				"\u062e\u0637\u0627 \u062f\u0631 \u0627\u062a\u0635\u0627\u0644 \u062a\u06cc \u0627\u0646 \u0627\u0633" },
	{ "ErrorMailServer", 		"\u062e\u0637\u0627 \u062f\u0631 \u0633\u0631\u0648\u0631 \u0646\u0627\u0645\u0647" },
	{ "ErrorMail", 				"\u062e\u0637\u0627 \u062f\u0631 \u0646\u0627\u0645\u0647" },
	{ "ErrorSave", 				"\u062e\u0637\u0627 \u062f\u0631 \u0630\u062e\u064a\u0631\u0647 \u0641\u0627\u064a\u0644" },

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
