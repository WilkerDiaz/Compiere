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
 *  @version    $Id: DBRes_sl.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class DBRes_sl extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]{
	{ "CConnectionDialog", 	"Compiere povezava" },
	{ "Name", 				"Ime" },
	{ "AppsHost", 			"Programski stre\u017enik" },
	{ "AppsPort", 			"Vrata programskega stre\u017enika" },
	{ "TestApps", 			"Test programskega stre\u017enika" },
	{ "DBHost", 			"Stre\u017enik baze podatkov" },
	{ "DBPort", 			"Vrata baze podatkov" },
	{ "DBName", 			"Ime baze podatkov" },
	{ "DBUidPwd", 			"Uporabnik / geslo" },
	{ "ViaFirewall", 		"Skozi po\u017earni zid" },
	{ "FWHost", 			"Po\u017earni zid" },
	{ "FWPort", 			"Vrata po\u017earnega zidu" },
	{ "TestConnection", 	"Testiranje baze podatkov" },
	{ "Type", 				"Tip baze podatkov" },
	{ "BequeathConnection", "Bequeath Connection" },
	{ "Overwrite", 			"Prepi\u0161i" },
	{ "ConnectionProfile",	"Povezava" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError", 	"Napaka na povezavi" },
	{ "ServerNotActive", 	"Stre\u017enik ni aktiven" }
	};

	/**
	 * Get Contsnts
	 * @return contents
	 */
	@Override
	public Object[][] getContents()
	{
		return contents;
	}   //  getContent
}   //  Res

