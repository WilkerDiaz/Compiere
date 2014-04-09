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
 *  Norwegian Base Resource Bundle Translation
 *
 * 	@version 	$Id: ALoginRes_no.java 8394 2010-02-04 22:55:56Z freyes $
 */
public final class ALoginRes_no extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Forbindelse" },
	{ "Defaults",           "Vanlige" },
	{ "Login",              "Compiere Loginn" },
	{ "File",               "Fil" },
	{ "Exit",               "Avslutt" },
	{ "Help",               "Hjelp" },
	{ "About",              "Om" },
	{ "Host",               "Maskin" },
	{ "Database",           "Database" },
	{ "User",               "Bruker ID" },
	{ "EnterUser",          "Skriv  Applikasjon Bruker ID" },
	{ "Password",           "Passord" },
	{ "EnterPassword",      "Skriv Applikasjon Passordet" },
	{ "Language",           "Spr\u00e5k" },
	{ "SelectLanguage",     "Velg \u00f8nsket Spr\u00e5k" },
	{ "Role",               "Rolle" },
	{ "Client",             "Klient" },
	{ "Organization",       "Organisasjon" },
	{ "Date",               "Dato" },
	{ "Warehouse",          "Varehus" },
	{ "Printer",            "Skriver" },
	{ "Connected",          "Oppkoblett" },
	{ "NotConnected",       "Ikke Oppkoblet" },
	{ "DatabaseNotFound",   "Database ikke funnet" },
	{ "UserPwdError",       "Bruker passer ikke til passordet" },
	{ "RoleNotFound",       "Role not found/complete" },
	{ "Authorized",         "Autorisert" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Avbryt" },
	{ "VersionConflict",    "Versions Konflikt:" },
	{ "VersionInfo",        "Server <> Klient" },
	{ "PleaseUpgrade",      "Vennligst kj\u00f8r oppdaterings programet" }
	};

	/**
	 *  Get Contents
	 *  @return context
	 */
	@Override
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  ALoginRes
