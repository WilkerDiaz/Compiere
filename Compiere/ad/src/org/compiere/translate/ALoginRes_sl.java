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
 *  Base Resource Bundle
 *
 * 	@version 	$Id: ALoginRes_sl.java 8394 2010-02-04 22:55:56Z freyes $
 */
public final class ALoginRes_sl extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Povezava" },
	{ "Defaults",           "Privzete vrednosti" },
	{ "Login",              "Prijava" },	
	{ "File",               "Datoteka" },
	{ "Exit",               "Izhod" },
	{ "Help",               "Pomo\u010d" },
	{ "About",              "O programu" },
	{ "Host",               "Stre\u017enik" },
	{ "Database",           "Baza podatkov" },
	{ "User",               "Uporabnik" },
	{ "EnterUser",          "Vpi\u0161i uporabnika" },
	{ "Password",           "Geslo" },
	{ "EnterPassword",      "Vpi\u0161i geslo" },
	{ "Language",           "Jezik" },
	{ "SelectLanguage",     "Izbira jezika" },
	{ "Role",               "Vloga" },
	{ "Client",             "Podjetje" },
	{ "Organization",       "Organizacija" },
	{ "Date",               "Datum" },
	{ "Warehouse",          "Skladi\u0161\u010de" },
	{ "Printer",            "Tiskalnik" },
	{ "Connected",          "Povezano" },
	{ "NotConnected",       "Ni povezano" },
	{ "DatabaseNotFound",   "Ne najdem baze podatkov" },
	{ "UserPwdError",       "Geslo ni pravilno" },
	{ "RoleNotFound",       "Ne najdem izbrane vloge" },
	{ "Authorized",         "Avtoriziran" },
	{ "Ok",                 "V redu" },
	{ "Cancel",             "Prekli\u010di" },
	{ "VersionConflict",    "Konflikt verzij" },
	{ "VersionInfo",        "Stre\u017enik <> Odjemalec" },
	{ "PleaseUpgrade",      "Prosim nadgradite program" }
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

