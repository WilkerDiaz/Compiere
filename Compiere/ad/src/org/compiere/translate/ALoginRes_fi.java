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
 * Resource Bundle for Finnish language
 * 
 * @version $Id: ALoginRes_fi.java 8394 2010-02-04 22:55:56Z freyes $
 */
public final class ALoginRes_fi extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content */
	static final Object[][] contents = new String[][] {
		{ "Connection", "Yhteys"},
		{ "Defaults", "Oletusarvot"},
		{ "Login", "Compiere Login"},
		{ "File", "Tiedosto"},
		{ "Exit", "Poistu"},
		{ "Help", "Ohje"},
		{ "About", "About"},
		{ "Host", "Host"},
		{ "Database", "Tietokanta"},
		{ "User", "K\u00e4ytt\u00e4j\u00e4tunnus"},
		{ "EnterUser", "Anna sovelluksen k\u00e4ytt\u00e4j\u00e4tunnus"},
		{ "Password", "Salasana"},
		{ "EnterPassword", "Anna sovelluksen salasana"},
		{ "Language", "Kieli"},
		{ "SelectLanguage", "Valitse kieli"},
		{ "Role", "Rooli"},
		{ "Client", "Client"},
		{ "Organization", "Organisaatio"},
		{ "Date", "P\u00e4iv\u00e4m\u00e4\u00e4r\u00e4"},
		{ "Warehouse", "Tietovarasto"},
		{ "Printer", "Tulostin"},
		{ "Connected", "Yhdistetty"},
		{ "NotConnected", "Ei yhteytt\u00e4"},
		{ "DatabaseNotFound", "Tietokantaa ei l\u00f6ydy"},
		{ "UserPwdError", "K\u00e4ytt\u00e4j\u00e4tunnus ja salasana eiv\u00e4t vastaa toisiaan"},
		{ "RoleNotFound", "Roolia ei l\u00f6ydy tai se ei ole t\u00e4ydellinen"},
		{ "Authorized", "Valtuutettu"},
		{ "Ok", "Hyv\u00e4ksy"},
		{ "Cancel", "Peruuta"},
		{ "VersionConflict", "Versioristiriita:"},
		{ "VersionInfo", "Server <> Client"},
		{ "PleaseUpgrade", "Ole hyv\u00e4 ja aja p\u00e4ivitysohjelma"}};

	/**
     * Get Contents
     * 
     * @return context
     */
	@Override
	public Object[][] getContents ()
	{
		return contents;
	} // getContents
	
} // ALoginRes
