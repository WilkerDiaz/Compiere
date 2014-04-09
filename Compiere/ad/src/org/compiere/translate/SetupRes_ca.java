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
 * 	@translator 	Jaume Teixi
 * 	@version 	$Id: SetupRes_ca.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class SetupRes_ca extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Configuraci\u00f3 Servidor Compiere" },
	{ "Ok", 				"D'Acord" },
	{ "File", 				"Fitxer" },
	{ "Exit", 				"Sortir" },
	{ "Help", 				"Ajuda" },
	{ "PleaseCheck", 		"Sisplau Comproveu" },
	{ "UnableToConnect",	"No s'ha pogut obtenir l'ajuda de la web del Compiere" },

	{ "CompiereHomeInfo", 	"Compiere Home \u00e9s la Carpeta Principal" },
	{ "CompiereHome", 		"Compiere Home" },
	{ "WebPortInfo", 		"Web (HTML) Port" },
	{ "WebPort", 			"Web Port" },
	{ "AppsServerInfo", 	"Nom Servidor Aplicaci\u00f3" },
	{ "AppsServer", 		"Servidor Aplicaci\u00f3" },
	{ "DatabaseTypeInfo", 	"Tipus Base de Dades" },
	{ "DatabaseType", 		"Tipus Base de Dades" },
	{ "DatabaseNameInfo", 	"Nom Base de Dades" },
	{ "DatabaseName", 		"Nom Base de Dades (SID)" },
	{ "DatabasePortInfo", 	"Port Listener Base de Dades" },
	{ "DatabasePort", 		"Port Base de Dades" },
	{ "DatabaseUserInfo", 	"ID Usuari Compiere Base de Dades" },
	{ "DatabaseUser", 		"Usuari Base de Dades" },
	{ "DatabasePasswordInfo", "Contrasenya Usuari Compiere Base de Dades" },
	{ "DatabasePassword", 	"Contrasenya Base de Dades" },
	{ "TNSNameInfo", 		"TNS o Nom Global Base de Dades" },
	{ "TNSName", 			"Nom TNS" },
	{ "SystemPasswordInfo", "Contrasenya Usuari System" },
	{ "SystemPassword", 	"Contrasenya System" },
	{ "MailServerInfo", 	"Servidor Correu" },
	{ "MailServer", 		"Servidor Correu" },
	{ "AdminEMailInfo", 	"Email Administrador Compiere" },
	{ "AdminEMail", 		"Email Admin" },
	{ "DatabaseServerInfo", "Nom Servidor Base de Dades" },
	{ "DatabaseServer", 	"Servidor Base de Dades" },
	{ "JavaHomeInfo", 		"Carpeta Java Home" },
	{ "JavaHome", 			"Java Home" },
	{ "JNPPortInfo", 		"Port JNP Servidor Aplicaci\u00f3" },
	{ "JNPPort", 			"Port JNP" },
	{ "MailUserInfo", 		"Usuari Correu Compiere" },
	{ "MailUser", 			"Usuari Correu" },
	{ "MailPasswordInfo", 	"Contrasenya Usuari Correu Compiere" },
	{ "MailPassword", 		"Contrasenya Correu" },
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
	{ "TestInfo", 			"Provar Configuraci\u00f3" },
	{ "Test", 				"Provar" },
	{ "SaveInfo", 			"Guardar Configuraci\u00f3" },
	{ "Save", 				"Guardar" },
	{ "HelpInfo", 			"Obtenir Ajuda" },

	{ "ServerError", 		"Error Configuraci\u00f3 Servidor" },
	{ "ErrorJavaHome", 		"Error Java Home" },
	{ "ErrorCompiereHome", 	"Error Compiere Home" },
	{ "ErrorAppsServer", 	"Error Servidor Aplicaci\u00f3 (no emprar localhost)" },
	{ "ErrorWebPort", 		"Error Port Web" },
	{ "ErrorJNPPort", 		"Error Port JNP" },
	{ "ErrorDatabaseServer", "Error Servidor Base de Dades (no emprar localhost)" },
	{ "ErrorDatabasePort", 	"Error Port Base de Dades" },
	{ "ErrorJDBC", 			"Error Connexi\u00f3 JDBC" },
	{ "ErrorTNS", 			"Error Connexi\u00f3 TNS" },
	{ "ErrorMailServer", 	"Error Servidor Correu (no emprar localhost)" },
	{ "ErrorMail", 			"Error Correu" },
	{ "ErrorSave", 			"Error Guardant Fitxer" },

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
