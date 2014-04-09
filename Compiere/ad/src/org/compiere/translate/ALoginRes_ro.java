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
 * 	@version 	$Id: ALoginRes_ro.java 8394 2010-02-04 22:55:56Z freyes $
 */
public final class ALoginRes_ro extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Conexiune" },
	{ "Defaults",           "Valori implicite" },
	{ "Login",              "Autentificare" },
	{ "File",               "Aplica\u0163ie" },
	{ "Exit",               "Ie\u015fire" },
	{ "Help",               "Ajutor" },
	{ "About",              "Despre..." },
	{ "Host",               "Server" },
	{ "Database",           "Baz\u0103 de date" },
	{ "User",               "Utilizator" },
	{ "EnterUser",          "Introduce\u0163i identificatorul utilizatorului" },
	{ "Password",           "Parol\u0103" },
	{ "EnterPassword",      "Introduce\u0163i parola" },
	{ "Language",           "Limb\u0103" },
	{ "SelectLanguage",     "Alege\u0163i limba dumneavoastr\u0103" },
	{ "Role",               "Rol" },
	{ "Client",             "Titular" },
	{ "Organization",       "Organiza\u0163ie" },
	{ "Date",               "Dat\u0103" },
	{ "Warehouse",          "Depozit" },
	{ "Printer",            "Imprimant\u0103" },
	{ "Connected",          "Conectat" },
	{ "NotConnected",       "Neconectat" },
	{ "DatabaseNotFound",   "Baza de date nu a fost g\u0103sit\u0103" },
	{ "UserPwdError",       "Parola nu se potrive\u015fte cu utilizatorul" },
	{ "RoleNotFound",       "Rolul nu a fost g\u0103sit sau este incomplet" },
	{ "Authorized",         "Autorizat" },
	{ "Ok",                 "OK" },
	{ "Cancel",             "Anulare" },
	{ "VersionConflict",    "Conflict de versiune:" },
	{ "VersionInfo",        "server <> client" },
	{ "PleaseUpgrade",      "Rula\u0163i programul de actualizare" }
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
