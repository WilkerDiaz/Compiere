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
 * 	@version 	$Id: SetupRes_zh.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_zh extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Compiere \u4f3a\u670d\u5668\u8a2d\u5b9a" },
	{ "Ok", 				"\u78ba\u5b9a" },
	{ "File", 				"\u6a94\u6848" },
	{ "Exit", 				"\u96e2\u958b" },
	{ "Help", 				"\u8aaa\u660e" },
	{ "PleaseCheck", 		"\u8acb\u6aa2\u67e5" },
	{ "UnableToConnect",	"\u7121\u6cd5\u81ea Compiere \u7db2\u7ad9\u5f97\u5230\u8aaa\u660e" },

	{ "CompiereHomeInfo", 	"Compiere \u4e3b\u76ee\u9304" },
	{ "CompiereHome", 		"Compiere Home" },
	{ "WebPortInfo", 		"\u7db2\u9801\u4f3a\u670d\u5668\u9023\u63a5\u57e0" },
	{ "WebPort", 			"Web Port" },
	{ "AppsServerInfo", 	"\u61c9\u7528\u4f3a\u670d\u5668\u540d\u7a31" },
	{ "AppsServer", 		"Apps Server" },
	{ "DatabaseTypeInfo", 	"\u8cc7\u6599\u5eab\u7a2e\u985e" },
	{ "DatabaseType", 		"Database Type" },
	{ "DatabaseNameInfo", 	"\u8cc7\u6599\u5eab\u540d\u7a31 " },
	{ "DatabaseName", 		"Database Name (SID)" },
	{ "DatabasePortInfo", 	"\u8cc7\u6599\u5eab\u9023\u63a5\u57e0" },
	{ "DatabasePort", 		"Database Port" },
	{ "DatabaseUserInfo", 	"Compiere \u4f7f\u7528\u8cc7\u6599\u5eab\u7684\u5e33\u865f" },
	{ "DatabaseUser", 		"Database User" },
	{ "DatabasePasswordInfo", "Compiere \u4f7f\u7528\u8cc7\u6599\u5eab\u7684\u5bc6\u78bc" },
	{ "DatabasePassword", 	"Database Password" },
	{ "TNSNameInfo", 		"TNS or Global Database Name" },
	{ "TNSName", 			"TNS Name" },
	{ "SystemPasswordInfo", "\u7cfb\u7d71\u5bc6\u78bc" },
	{ "SystemPassword", 	"System Password" },
	{ "MailServerInfo", 	"\u90f5\u4ef6\u4f3a\u670d\u5668" },
	{ "MailServer", 		"Mail Server" },
	{ "AdminEMailInfo", 	"Compiere \u7ba1\u7406\u8005 EMail" },
	{ "AdminEMail", 		"Admin EMail" },
	{ "DatabaseServerInfo", "\u8cc7\u6599\u5eab\u540d\u7a31" },
	{ "DatabaseServer", 	"Database Server" },
	{ "JavaHomeInfo", 		"Java \u4e3b\u76ee\u9304" },
	{ "JavaHome", 			"Java Home" },
	{ "JNPPortInfo", 		"\u61c9\u7528\u4f3a\u670d\u5668\u7684 JNP \u9023\u63a5\u57e0" },
	{ "JNPPort", 			"JNP Port" },
	{ "MailUserInfo", 		"Compiere Mail \u5e33\u865f" },
	{ "MailUser", 			"Mail User" },
	{ "MailPasswordInfo", 	"Compiere Mail \u5e33\u865f\u7684\u5bc6\u78bc" },
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
	{ "TestInfo", 			"\u8a2d\u5b9a\u6e2c\u8a66" },
	{ "Test", 				"Test" },
	{ "SaveInfo", 			"\u5132\u5b58\u8a2d\u5b9a" },
	{ "Save", 				"Save" },
	{ "HelpInfo", 			"\u53d6\u5f97\u8aaa\u660e" },

	{ "ServerError", 		"\u4f3a\u670d\u5668\u8a2d\u5b9a\u932f\u8aa4" },
	{ "ErrorJavaHome", 		"Java \u4e3b\u76ee\u9304\u932f\u8aa4" },
	{ "ErrorCompiereHome", 	"Compiere \u4e3b\u76ee\u9304\u932f\u8aa4" },
	{ "ErrorAppsServer", 	"\u61c9\u7528\u4f3a\u670d\u5668\u932f\u8aa4 (do not use localhost)" },
	{ "ErrorWebPort", 		"\u7db2\u9801\u4f3a\u670d\u5668\u9023\u63a5\u57e0\u932f\u8aa4" },
	{ "ErrorJNPPort", 		"JNP \u9023\u63a5\u57e0\u932f\u8aa4" },
	{ "ErrorDatabaseServer", "\u8cc7\u6599\u5eab\u932f\u8aa4 (do not use localhost)" },
	{ "ErrorDatabasePort", 	"\u8cc7\u6599\u5eab Port \u932f\u8aa4" },
	{ "ErrorJDBC", 			"JDBC \u9023\u63a5\u932f\u8aa4" },
	{ "ErrorTNS", 			"TNS \u9023\u63a5\u932f\u8aa4" },
	{ "ErrorMailServer", 	"\u90f5\u4ef6\u4f3a\u670d\u5668\u932f\u8aa4 (do not use localhost)" },
	{ "ErrorMail", 			"\u90f5\u4ef6\u932f\u8aa4" },
	{ "ErrorSave", 			"\u5b58\u6a94\u932f\u8aa4" },

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
