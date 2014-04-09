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
 *  Connection Resource Strings (German)
 *
 *  @version    $Id: DBRes_de.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class DBRes_de extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]
	{
	{ "CConnectionDialog",  "Compiere Verbindung" },
	{ "Name",               "Name" },
	{ "AppsHost",           "AnwendungsRechner" },
	{ "AppsPort",           "Port AnwendungsRechner" },
	{ "TestApps",           "Test des AnwendungsRechners" },
	{ "DBHost",             "Datenbank Rechner" },
	{ "DBPort",             "Datenbank Port" },
	{ "DBName",             "Datenbank Name" },
	{ "DBUidPwd",           "Nutzer / Kennwort" },
	{ "ViaFirewall",        "\u00dcber Feuerwand" },
	{ "FWHost",             "Feuerwand Rechner" },
	{ "FWPort",             "Feuerwand Port" },
	{ "TestConnection",     "Test der Datenbankverbindung" },
	{ "Type",               "Datenbank Typ" },
	{ "BequeathConnection", "Bequeath Verbindung" },
	{ "Overwrite",          "\u00dceberschreiben" },
	{ "ConnectionProfile",	"Verbindungsprofil" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",    "Verbindungsfehler" },
	{ "ServerNotActive",    "Rechner nicht erreichbar" }
	};

	/**
	 * Get Contents
	 * @return contents
	 */
	@Override
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  Res_de
