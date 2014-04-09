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
 * 	@version 	$Id: SetupRes_in.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class SetupRes_in extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", 	"Setup Compiere Server" },
	{ "Ok", 					"Ok" },
	{ "File", 					"File" },
	{ "Exit", 					"Keluar" },
	{ "Help", 					"Bantuan" },
	{ "PleaseCheck", 			"Mohon diperiksa" },
	{ "UnableToConnect", 		"Koneksi gagal. Saat ini bantuan tidak bisa didapatkan dari situs web Compiere" },
	//
	{ "CompiereHomeInfo", 		"Compiere Home adalah direktori utama" },
	{ "CompiereHome", 			"Compiere Home" },
	{ "WebPortInfo", 			"Port Web (HTML)" },
	{ "WebPort", 				"Port Web" },
	{ "AppsServerInfo", 		"Nama Server Aplikasi" },
	{ "AppsServer", 			"Server Aplikasi" },
	{ "DatabaseTypeInfo", 		"Tipe Database" },
	{ "DatabaseType", 			"Tipe Database" },
	{ "DatabaseNameInfo", 		"Nama Database (Service)" },
	{ "DatabaseName", 			"Nama Database" },
	{ "DatabasePortInfo", 		"Port Database Listener" },
	{ "DatabasePort", 			"Port Database" },
	{ "DatabaseUserInfo", 		"ID Pengguna untuk database Compiere" },
	{ "DatabaseUser", 			"Pengguna Database" },
	{ "DatabasePasswordInfo", 	"Kata sandi pengguna untuk database Compiere" },
	{ "DatabasePassword", 		"Kata Sandi Database" },
	{ "TNSNameInfo", 			"Database-database yang ditemukan" },
	{ "TNSName", 				"Pilih Database" },
	{ "SystemPasswordInfo", 	"Kata Sandi Pengguna Sistem" },
	{ "SystemPassword", 		"Kata Sandi Sistem" },
	{ "MailServerInfo", 		"Server Mail" },
	{ "MailServer", 			"Server Mail" },
	{ "AdminEMailInfo", 		"Email Compiere Administrator" },
	{ "AdminEMail", 			"EMail Admin" },
	{ "DatabaseServerInfo", 	"Nama Database Server" },
	{ "DatabaseServer", 		"Server Database" },
	{ "JavaHomeInfo", 			"Java Home Folder" },
	{ "JavaHome", 				"Java Home" },
	{ "JNPPortInfo", 			"Port JNP untuk Server Aplikasi" },
	{ "JNPPort", 				"Port JNP" },
	{ "MailUserInfo", 			"Pengguna Compiere Mail" },
	{ "MailUser", 				"Pengguna Mail" },
	{ "MailPasswordInfo", 		"Compiere Mail User Password" },
	{ "MailPassword", 			"Kata Sandi Mail" },
	{ "KeyStorePassword",		"Kata Sandi KeyStore" },
	{ "KeyStorePasswordInfo",	"Kata Sandi untuk SSL Key Store" },
	//
	{ "JavaType",				"Java VM"},
	{ "JavaTypeInfo",			"Vendor Java VM"},
	{ "AppsType",				"Tipe Server"},
	{ "AppsTypeInfo",			"Tipe Server Aplikasi J2EE"},
	{ "DeployDir",				"Direktori"},
	{ "DeployDirInfo",			"Direktori sebar aplikasi J2EE"},
	{ "ErrorDeployDir",			"Error direktori sebar aplikasi J2EE"},
	//
	{ "TestInfo", 				"Uji Setup" },
	{ "Test", 					"Uji" },
	{ "SaveInfo", 				"Simpan Setup" },
	{ "Save", 					"Simpan" },
	{ "HelpInfo", 				"Cari Bantuan" },
	//
	{ "ServerError", 			"Error Setup Server" },
	{ "ErrorJavaHome", 			"Error Java Home" },
	{ "ErrorCompiereHome", 		"Error Compiere Home" },
	{ "ErrorAppsServer", 		"Error Server Aplikasi (jangan gunakan localhost)" },
	{ "ErrorWebPort", 			"Error Web Port" },
	{ "ErrorJNPPort", 			"Error JNP Port" },
	{ "ErrorDatabaseServer", 	"Error Database Server (jangan gunakan localhost)" },
	{ "ErrorDatabasePort", 		"Error Port Database" },
	{ "ErrorJDBC", 				"Error Koneksi JDBC" },
	{ "ErrorTNS", 				"Error Koneksi TNS" },
	{ "ErrorMailServer", 		"Error Mail Server (jangan gunakan localhost)" },
	{ "ErrorMail", 				"Error Mail" },
	{ "ErrorSave", 				"Error Simpan File" },

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
