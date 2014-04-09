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
 *  @version    $Id: ALoginRes_pl.java 8394 2010-02-04 22:55:56Z freyes $
 */
public final class ALoginRes_pl extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Po\u0142\u0105czenie" },
	{ "Defaults",           "Domy\u015blne" },
	{ "Login",              "Logowanie" },
	{ "File",               "Plik" },
	{ "Exit",               "Zako\u0144cz" },
	{ "Help",               "Pomoc" },
	{ "About",              "O aplikacji" },
	{ "Host",               "Host" },
	{ "Database",           "Baza danych" },
	{ "User",               "U\u017cytkownik" },
	{ "EnterUser",          "Wprowad\u017a Identyfikator U\u017cytkownika Aplikacji" },
	{ "Password",           "Has\u0142o" },
	{ "EnterPassword",      "Wprowad\u017a Has\u0142o Aplikacji" },
	{ "Language",           "J\u0119zyk" },
	{ "SelectLanguage",     "Wybierz j\u0119zyk" },
	{ "Role",               "Funkcja" },
	{ "Client",             "Klient" },
	{ "Organization",       "Organizacja" },
	{ "Date",               "Data" },
	{ "Warehouse",          "Magazyn" },
	{ "Printer",            "Drukarka" },
	{ "Connected",          "Po\u0142\u0105czony" },
	{ "NotConnected",       "Nie Po\u0142\u0105czony" },
	{ "DatabaseNotFound",   "Nie znaleziono bazy danych" },
	{ "UserPwdError",       "Has\u0142o nie odpowiada U\u017cytkownikowi" },
	{ "RoleNotFound",       "Nie znaleziono zasady" },
	{ "Authorized",         "Autoryzowany" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Anuluj" },
	{ "VersionConflict",    "Konflikt wersji:" },
	{ "VersionInfo",        "Serwer <> Klienta" },
	{ "PleaseUpgrade",      "Uruchom now\u0105 wersj\u0119 programu !" }
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
