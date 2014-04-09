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
 * 	@version 	$Id: ALoginRes_ca.java 8394 2010-02-04 22:55:56Z freyes $
 */
public final class ALoginRes_ca extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Connexi\u00f3" },
	{ "Defaults",           "Predeterminats" },
	{ "Login",              "Acc\u00e9s Compiere" },
	{ "File",               "Fitxer" },
	{ "Exit",               "Sortir" },
	{ "Help",               "Ajuda" },
	{ "About",              "Referent" },
	{ "Host",               "Servidor" },
	{ "Database",           "Base de Dades" },
	{ "User",               "ID Usuari" },
	{ "EnterUser",          "Entrar ID Usuari Aplicaci\u00f3" },
	{ "Password",           "Contrasenya" },
	{ "EnterPassword",      "Entrar Contrasenya Usuari Aplicaci\u00f3" },
	{ "Language",           "Idioma" },
	{ "SelectLanguage",     "Seleccioneu el Vostre Idioma" },
	{ "Role",               "Rol" },
	{ "Client",             "Client" },
	{ "Organization",       "Organitzaci\u00f3" },
	{ "Date",               "Data" },
	{ "Warehouse",          "Magatzem" },
	{ "Printer",            "Impressora" },
	{ "Connected",          "Connectat" },
	{ "NotConnected",       "No Connectat" },
	{ "DatabaseNotFound",   "No s'ha trobat la Base de Dades" },
	{ "UserPwdError",       "No coincid\u00e8ix l'Usuari i la Contrasenya" },
	{ "RoleNotFound",       "Rol no trobat/completat" },
	{ "Authorized",         "Autoritzat" },
	{ "Ok",                 "D'Acord" },
	{ "Cancel",             "Cancel.lar" },
	{ "VersionConflict",    "Conflicte Versions:" },
	{ "VersionInfo",        "Servidor <> Client" },
	{ "PleaseUpgrade",      "Sisplau Actualitzeu el Programa" }
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
