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
 *  @version    $Id: DBRes_in.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class DBRes_in extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]{
	{ "CConnectionDialog", 	"Koneksi Ke Compiere" },
	{ "Name", 				"Nama" },
	{ "AppsHost", 			"Pusat Aplikasi" },
	{ "AppsPort", 			"Port Aplikasi" },
	{ "TestApps", 			"Uji Server Aplikasi" },
	{ "DBHost", 			"Pusat Database" },
	{ "DBPort", 			"Port Database" },
	{ "DBName", 			"Nama Database" },
	{ "DBUidPwd", 			"ID Pengguna / Kata Sandi" },
	{ "ViaFirewall", 		"lewat Firewall" },
	{ "FWHost", 			"Pusat Firewall" },
	{ "FWPort", 			"Port Firewall" },
	{ "TestConnection", 	"Uji Koneksi" },
	{ "Type", 				"Tipe Database" },
	{ "BequeathConnection", "Koneksi Warisan" },
	{ "Overwrite", 			"Timpakan" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError", 	"Kesalahan Koneksi" },
	{ "ServerNotActive", 	"Server tidak aktif" }
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
