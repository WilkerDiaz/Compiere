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
 *  Login Resource Strings (French)
 *
 *  @version    $Id: ALoginRes_fr.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class ALoginRes_fr extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !!

	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",             "Connexion" },
	{ "Defaults",               "D\u00e9fauts" },
	{ "Login",                  "Login Compiere" },
	{ "File",                   "Fichier" },
	{ "Exit",                   "Sortir" },
	{ "Help",                   "Aide" },
	{ "About",                  "A propos de" },
	{ "Host",                   "Serveur" },
	{ "Database",               "Base de donn\u00e9es" },
	{ "User",                   "Utilisateur" },
	{ "EnterUser",              "Entrer le nom d'utilisateur" },
	{ "Password",               "Mot de passe" },
	{ "EnterPassword",          "Entrer le mot de passe" },
	{ "Language",               "Langue" },
	{ "SelectLanguage",         "S\u00e9lectionnez votre langue" },
	{ "Role",                   "R\u00f4le" },
	{ "Client",                 "Soci\u00e9t\u00e9" },
	{ "Organization",           "D\u00e9partement" },
	{ "Date",                   "Date" },
	{ "Warehouse",              "Stock" },
	{ "Printer",                "Imprimante" },
	{ "Connected",              "Connect\u00e9" },
	{ "NotConnected",           "Non Connect\u00e9" },
	{ "DatabaseNotFound",       "Base de donn\u00e9es non trouv\u00e9e" },
	{ "UserPwdError",           "Le nom d'utilisateur ou le mot de passe sont incorrects" },
	{ "RoleNotFound",           "R\u00f4le non trouv\u00e9" },
	{ "Authorized",             "Autoris\u00e9" },
	{ "Ok",                     "Ok" },
	{ "Cancel",                 "Annuler" },
	{ "VersionConflict",        "Conflit de Version:" },
	{ "VersionInfo",            "Serveur <> Client" },
	{ "ChangeRole",         	"S\u00e9lection du r\u00f4le" },
	{ "PleaseUpgrade",          "SVP, mettez \u00e0 jour le programme" }
	};

	/**
	 *  Get Contents
	 *  @return data
	 */
	@Override
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  ALoginRes_fr
