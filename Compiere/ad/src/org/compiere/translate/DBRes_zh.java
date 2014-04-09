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
 *  @version    $Id: DBRes_zh.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class DBRes_zh extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]
	{
	{ "CConnectionDialog",  "Compiere \u9023\u7dda" },
	{ "Name",               "\u540d\u7a31" },
	{ "AppsHost",           "\u61c9\u7528\u7a0b\u5f0f\u4e3b\u6a5f" },
	{ "AppsPort",           "\u61c9\u7528\u7a0b\u5f0f\u57e0" },
	{ "TestApps",           "\u6e2c\u8a66" },
	{ "DBHost",             "\u8cc7\u6599\u5eab\u4e3b\u6a5f" },
	{ "DBPort",             "\u8cc7\u6599\u5eab\u9023\u63a5\u57e0" },
	{ "DBName",             "\u8cc7\u6599\u5eab\u540d\u7a31" },
	{ "DBUidPwd",           "\u5e33\u865f / \u5bc6\u78bc" },
	{ "ViaFirewall",        "\u7d93\u904e\u9632\u706b\u7246" },
	{ "FWHost",             "\u9632\u706b\u7246\u4e3b\u6a5f" },
	{ "FWPort",             "\u9632\u706b\u7246\u57e0" },
	{ "TestConnection",     "\u6e2c\u8a66\u8cc7\u6599\u5eab" },
	{ "Type",               "\u8cc7\u6599\u5eab\u7a2e\u985e" },
	{ "BequeathConnection", "\u907a\u7559\u9023\u7dda" },
	{ "Overwrite",          "\u8986\u5beb" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",    "\u9023\u7dda\u932f\u8aa4" },
	{ "ServerNotActive",    "\u4f3a\u670d\u5668\u672a\u52d5\u4f5c" }
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
