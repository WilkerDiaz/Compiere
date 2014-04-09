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
 *  Connection Resource Strings (French)
 *
 *  @version    $Id: DBRes_fr.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class DBRes_fr extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "CConnectionDialog",      "Connexion Compiere" },
	{ "Name",                   "Nom" },
	{ "AppsHost",               "H\u00f4te d'Application" },
	{ "AppsPort",               "Port de l'Application" },
	{ "TestApps",               "Application de Test" },
	{ "DBHost",                 "H\u00f4e Base de Donn\u00e9es" },
	{ "DBPort",                 "Port Base de Donn\u00e9es" },
	{ "DBName",                 "Nom Base de Donn\u00e9es" },
	{ "DBUidPwd",               "Utilisateur / Mot de Passe" },
	{ "ViaFirewall",            "via Firewall" },
	{ "FWHost",                 "H\u00f4e Firewall" },
	{ "FWPort",                 "Port Firewall" },
	{ "TestConnection",         "Test Base de Donn\u00e9es" },
	{ "Type",                   "Type Base de Donn\u00e9es" },
	{ "BequeathConnection",     "Connexion d\u00e9di\u00e9e" },
	{ "Overwrite",              "Ecraser" },
	{ "ConnectionProfile",		"Connection" },
	{ "LAN",		 			"LAN" },
	{ "TerminalServer",			"Terminal Server" },
	{ "VPN",		 			"VPN" },
	{ "WAN", 					"WAN" },
	{ "ConnectionError",        "Erreur Connexion" },
	{ "ServerNotActive",        "Serveur Non Actif" }
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
}   //  DBRes_fr
