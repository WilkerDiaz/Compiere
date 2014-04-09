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
 *	Vietnamese Setup Resources
 *
 * 	@version 	$Id: SetupRes_vi.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_vi extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Setup m\u00e1y ch\u1EE7 C\u0103mpia-\u01A1" },
	{ "Ok", 				"\u0110\u1ED3ng \u01b0" },
	{ "File", 				"T\u1EC7p" },
	{ "Exit", 				"Tho\u00e1t" },
	{ "Help", 				"Gi\u00fap \u0111\u1EE1" },
	{ "PleaseCheck", 		"Vui l\u0323ng ki\u1EC3m tra" },
	{ "UnableToConnect",	"Kh\u00f4ng th\u1EC3 t\u0301m th\u1EA5y h\u1ED7 tr\u1EE3 t\u1EEB trang WEB C\u0103mpia-\u01A1" },

	{ "CompiereHomeInfo", 	"Th\u01B0 m\u1EE5c ch\u1EE9a C\u0103mpia-\u01A1" },
	{ "CompiereHome", 	"Th\u01B0 m\u1EE5c ch\u1EE9a C\u0103mpia-\u01A1" },
	{ "WebPortInfo", 	"C\u1ED5ng Web (HTML)" },
	{ "WebPort", 			"Web C\u1ED5ng" },
	{ "AppsServerInfo", 	"T\u00ean m\u00e1y ch\u1EE7 ch\u1EA1y \u1EE9ng d\u1EE5ng" },
	{ "AppsServer", 		"M\u00e1y ch\u1EE7 c\u00e1c \u1EE9ng d\u1EE5ng" },
	{ "DatabaseTypeInfo", 	"Lo\u1EA1i CSDL" },
	{ "DatabaseType", 		"Lo\u1EA1i CSDL" },
	{ "DatabaseNameInfo", 	"T\u00ean CSDL " },
	{ "DatabaseName", 		"T\u00ean CSDL (SID)" },
	{ "DatabasePortInfo", 	"C\u1ED5ng nghe CSDL" },
	{ "DatabasePort", 		"C\u1ED5ng CSDL" },
	{ "DatabaseUserInfo", 	"Ng\u01B0\u1EDDi d\u00f9ng CSDL C\u0103mpia-\u01A1" },
	{ "DatabaseUser", 		"Ng\u01B0\u1EDDi d\u00f9ng CSDL" },
	{ "DatabasePasswordInfo", "M\u1EADt kh\u1EA9u d\u00f9ng CSDL C\u0103mpia-\u01A1" },
	{ "DatabasePassword", 	"M\u1EADt kh\u1EA9u CSDL" },
	{ "TNSNameInfo", 		"TNS ho\u1EB7c Global Database Name" },
	{ "TNSName", 			"T\u00ean theo TNS" },
	{ "SystemPasswordInfo", "M\u1EADt kh\u1EA9u c\u1EE7a ng\u01B0\u1EDDi d\u00f9ng H\u1EC7 th\u1ED1ng" },
	{ "SystemPassword", 	"M\u1EADt kh\u1EA9u c\u1EE7a ng\u01B0\u1EDDi d\u00f9ng H\u1EC7 th\u1ED1ng" },
	{ "MailServerInfo", 	"M\u00e1y ch\u1EE7 th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "MailServer", 	"M\u00e1y ch\u1EE7 th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "AdminEMailInfo", 	"Email c\u1EE7a ng\u01B0\u1EDDi qu\u1EA3n tr\u1ECB C\u0103mpia-\u01A1" },
	{ "AdminEMail", 		"Email c\u1EE7a ng\u01B0\u1EDDi qu\u1EA3n tr\u1ECB" },
	{ "DatabaseServerInfo", "T\u00ean m\u00e1y ch\u1EE7 CSDL" },
	{ "DatabaseServer", 	"M\u00e1y ch\u1EE7 CSDL" },
	{ "JavaHomeInfo", 		"Th\u01B0 m\u1EE5c ch\u1EE9a Java" },
	{ "JavaHome", 			"Th\u01B0 m\u1EE5c ch\u1EE9a Java" },
	{ "JNPPortInfo", 		"C\u1ED5ng JNP c\u1EE7a m\u00e1y ch\u1EE7 \u1EE9ng d\u1EE5ng" },
	{ "JNPPort", 			"C\u1ED5ng JNP" },
	{ "MailUserInfo", 		"Ng\u01B0\u1EDDi d\u00f9ng th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "MailUser", 			"Ng\u01B0\u1EDDi d\u00f9ng th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "MailPasswordInfo", 	"M\u1EADt kh\u1EA9u c\u1EE7a ng\u01B0\u1EDDi d\u00f9ng th\u01B0 \u0111i\u1EC7n t\u1EED" },
	{ "MailPassword", 		"M\u1EADt kh\u1EA9u c\u1EE7a ng\u01B0\u1EDDi d\u00f9ng th\u01B0 \u0111i\u1EC7n t\u1EED" },
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
	{ "TestInfo", 	"Th\u1EED nghi\u1EC7m c\u00e1c c\u1EA5u h\u0301nh n\u00e0y" },
	{ "Test", 				"Th\u1EED nghi\u1EC7m" },
	{ "SaveInfo", 			"L\u01B0u c\u00e1c c\u1EA5u h\u0301nh n\u00e0y" },
	{ "Save", 				"L\u01B0u" },
	{ "HelpInfo", 			"Nh\u1EADn gi\u00fap \u0111\u1EE1" },

	{ "ServerError", 		"SL\u1ED7i khi c\u00e0i \u0111\u1EB7t ph\u1EA7n m\u1EC1m n\u00e0y \u1EDF m\u00e1y ch\u1EE7" },
	{ "ErrorJavaHome", 		"T\u00ean th\u01B0 m\u1EE5c ch\u1EE9a Java kh\u00f4ng \u0111\u00fang" },
	{ "ErrorCompiereHome", 	"T\u00ean th\u01B0 m\u1EE5c ch\u1EE9a C\u0103mpia-\u01A1 kh\u00f4ng \u0111\u00fang" },
	{ "ErrorAppsServer", 	"L\u1ED7i li\u00ean quan \u0111\u1EBFn m\u00e1y ch\u1EE7 \u1EE9ng d\u1EE5ng (\u0111\u1EEBng d\u00f9ng localhost)" },
	{ "ErrorWebPort", 		"C\u1ED5ng Web kh\u00f4ng \u0111\u00fang" },
	{ "ErrorJNPPort", 		"C\u1ED5ng JNP kh\u00f4ng \u0111\u00fang" },
	{ "ErrorDatabaseServer", "L\u1ED7i li\u00ean quan \u0111\u1EBFn m\u00e1y ch\u1EE7 CSDL (\u0111\u1EEBng d\u00f9ng localhost)" },
	{ "ErrorDatabasePort", 	"C\u1ED5ng CSDL kh\u00f4ng \u0111\u00fang" },
	{ "ErrorJDBC", 			"L\u1ED7i li\u00ean quan \u0111\u1EBFn k\u1EBFt n\u1ED1i JDBC" },
	{ "ErrorTNS", 			"L\u1ED7i li\u00ean quan \u0111\u1EBFn k\u1EBFt n\u1ED1i TNS" },
	{ "ErrorMailServer", 	"L\u1ED7i li\u00ean quan \u0111\u1EBFn Mail Server (\u0111\u1EEBng d\u00f9ng localhost)" },
	{ "ErrorMail", 		"L\u1ED7i li\u00ean quan \u0111\u1EBFn Mail" },
	{ "ErrorSave", 			"L\u1ED7i khi l\u01B0u file" },

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

}	//	SetupRes_vi
