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
 *	Setup Resources for Finnish language
 *
 * 	@version 	$Id: SetupRes_fi.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_fi extends ListResourceBundle
{
	/**	
    * Translation Info
    */
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", 	"Compiere-palvelimen Asetukset" },
	{ "Ok", 					"Hyv\u00e4ksy" },
	{ "File", 					"Tiedosto" },
	{ "Exit", 					"Poistu" },
	{ "Help", 					"Help" },
	{ "PleaseCheck", 			"Ole hyv\u00e4 ja valitse" },
	{ "UnableToConnect", 		"Yhteydenotto Compieren Web-Help:in ei onnistu" },
	//
	{ "CompiereHomeInfo", 		"Compiere Home on p\u00e4\u00e4kansio" },
	{ "CompiereHome", 			"Compiere Home" },
	{ "WebPortInfo", 			"Web (HTML) Portti" },
	{ "WebPort", 				"Web Portti" },
	{ "AppsServerInfo", 		"Sovelluspalvelimen Nimi" },
	{ "AppsServer", 			"Sovelluspalvelin" },
	{ "DatabaseTypeInfo", 		"Tietokantatyyppi" },
	{ "DatabaseType", 			"Tietokantatyyppi" },
	{ "DatabaseNameInfo", 		"Tietokannan Nimi" },
	{ "DatabaseName", 			"Tietokannan Nimi (SID)" },
	{ "DatabasePortInfo", 		"Tietokannan kuuntelijaportti" },
	{ "DatabasePort", 			"Tietokantaportti" },
	{ "DatabaseUserInfo", 		"Tietokannan Compiere-k\u00e4ytt\u00e4j\u00e4tunnus" },
	{ "DatabaseUser", 			"Tietokannan k\u00e4ytt\u00e4j\u00e4tunnus" },
	{ "DatabasePasswordInfo", 	"Tietokannan Compiere-salasana" },
	{ "DatabasePassword", 		"Tietokannan salasana" },
	{ "TNSNameInfo", 			"TNS tai Globaali Tietokannan Nimi" },
	{ "TNSName", 				"TNS Nimi" },
	{ "SystemPasswordInfo", 	"J\u00e4rjestelm\u00e4salasana" },
	{ "SystemPassword", 		"J\u00e4rjestelm\u00e4salasana" },
	{ "MailServerInfo", 		"S\u00e4hk\u00f6postipalvelin" },
	{ "MailServer", 			"S\u00e4hk\u00f6postipalvelin" },
	{ "AdminEMailInfo", 		"Compiere-yll\u00e4pit\u00e4j\u00e4n S\u00e4hk\u00f6posti" },
	{ "AdminEMail", 			"Yll\u00e4pit\u00e4j\u00e4n S\u00e4hk\u00f6posti" },
	{ "DatabaseServerInfo", 	"Tietokantapalvelimen Nimi" },
	{ "DatabaseServer", 		"Tietokantapalvelin" },
	{ "JavaHomeInfo", 			"Java-kotihakemisto" },
	{ "JavaHome", 				"Java-koti" },
	{ "JNPPortInfo", 			"Sovelluspalvelimen JNP-portti" },
	{ "JNPPort", 				"JNP-portti" },
	{ "MailUserInfo", 			"Compiere-s\u00e4hk\u00f6postik\u00e4ytt\u00e4j\u00e4" },
	{ "MailUser", 				"S\u00e4hk\u00f6postik\u00e4ytt\u00e4j\u00e4" },
	{ "MailPasswordInfo", 		"Compiere-s\u00e4hk\u00f6postisalasana" },
	{ "MailPassword", 			"S\u00e4hk\u00f6postisalasana" },
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
	{ "TestInfo", 				"Testaa Asetukset" },
	{ "Test", 					"Testaa" },
	{ "SaveInfo", 				"Tallenna Asetukset" },
	{ "Save", 					"Tallenna" },
	{ "HelpInfo", 				"Hae Apua" },
	//
	{ "ServerError", 			"Palvelimen Asetusvirhe" },
	{ "ErrorJavaHome", 			"Java-kotivirhe" },
	{ "ErrorCompiereHome", 		"Compiere-kotivirhe" },
	{ "ErrorAppsServer", 		"Sovelluspalvelinvirhe (\u00e4l\u00e4 k\u00e4yt\u00e4 paikallisverkkoasemaa)" },
	{ "ErrorWebPort", 			"Web-porttivirhe" },
	{ "ErrorJNPPort", 			"JNP-porttivirhe" },
	{ "ErrorDatabaseServer", 	"Tietokantapalvelinvirhe (\u00e4l\u00e4 k\u00e4yt\u00e4 paikallisverkkoasemaa)" },
	{ "ErrorDatabasePort", 		"Tietokantaporttivirhe" },
	{ "ErrorJDBC", 				"JDBC-yhteysvirhe" },
	{ "ErrorTNS", 				"TNS-yhteysvirhe" },
	{ "ErrorMailServer", 		"S\u00e4hk\u00f6postipalvelinvirhe (\u00e4l\u00e4 k\u00e4yt\u00e4 paikallisverkkoasemaa)" },
	{ "ErrorMail", 				"S\u00e4hk\u00f6postivirhe" },
	{ "ErrorSave", 				"Tiedostontallennusvirhe" },

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
