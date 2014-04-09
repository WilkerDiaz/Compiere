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
 *  Connection Resource Strings
 *
 *  @version    $Id: DBRes_ro.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class DBRes_ro extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]{
	{ "CConnectionDialog", 	"Conexiune" },
	{ "Name", 				"Nume" },
	{ "AppsHost", 			"Server de aplica\u0163ie" },
	{ "AppsPort", 			"Port de aplica\u0163ie" },
	{ "TestApps", 			"Testare a serverului de aplica\u0163ie" },
	{ "DBHost", 			"Server de baz\u0103 de date" },
	{ "DBPort", 			"Port de baz\u0103 de date" },
	{ "DBName", 			"Numele bazei de date" },
	{ "DBUidPwd", 			"Utilizator / parol\u0103" },
	{ "ViaFirewall", 		"Prin firewall" },
	{ "FWHost", 			"Gazd\u0103 de firewall" },
	{ "FWPort", 			"Port de firewall" },
	{ "TestConnection", 	"Testare a bazei de date" },
	{ "Type", 				"Tip al bazei de date" },
	{ "BequeathConnection", "Cedare de conexiune" },
	{ "Overwrite", 			"Suprascriere" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError", 	"Eroare de conexiune" },
	{ "ServerNotActive", 	"Serverul este inactiv" }
	};

	/**
	 * Get Contents
	 * @return contents
	 */
	@Override
	public Object[][] getContents()
	{
		return contents;
	}   //  getContent
}   //  Res
