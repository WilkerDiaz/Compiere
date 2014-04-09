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
 *  Vietnamese Resource Bundle
 *
 * 	@version 	$Id: ALoginRes_vi.java 8394 2010-02-04 22:55:56Z freyes $
 */
public final class ALoginRes_vi extends ListResourceBundle
{
	// TODO Run native2ascii to convert everything to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "K\u1EBFt n\u1ED1i" },
	{ "Defaults",           "M\u1EB7c nhi\u00ean" },
	{ "Login",              "\u0110\u0103ng nh\u1EADp" },
	{ "File",               "H\u1EC7 th\u1ED1ng" },
	{ "Exit",               "Tho\u00e1t" },
	{ "Help",               "Gi\u00fap \u0111\u1EE1" },
	{ "About",              "Gi\u1EDBi thi\u1EC7u" },
	{ "Host",               "M\u00e1y ch\u1EE7" },
	{ "Database",           "C\u01A1 s\u1EDF d\u1EEF li\u1EC7u" },
	{ "User",               "T\u00ean ng\u01B0\u1EDDi d\u00f9ng" },
	{ "EnterUser",          "H\u0103y nh\u1EADp t\u00ean ng\u01B0\u1EDDi d\u00f9ng" },
	{ "Password",           "M\u1EADt kh\u1EA9u" },
	{ "EnterPassword",      "H\u0103y nh\u1EADp m\u1EADt kh\u1EA9u" },
	{ "Language",           "Ng\u00f4n ng\u1EEF" },
	{ "SelectLanguage",     "H\u0103y ch\u1ECDn ng\u00f4n ng\u1EEF" },
	{ "Role",               "Vai tr\u0323" },
	{ "Client",             "C\u00f4ng ty" },
	{ "Organization",       "\u0110\u01A1n v\u1ECB" },
	{ "Date",               "Ng\u00e0y" },
	{ "Warehouse",          "Kho h\u00e0ng" },
	{ "Printer",            "M\u00e1y in" },
	{ "Connected",          "\u0110\u0103 k\u1EBFt n\u1ED1i" },
	{ "NotConnected",       "Ch\u01B0a k\u1EBFt n\u1ED1i \u0111\u01B0\u1EE3c" },
	{ "DatabaseNotFound",   "Kh\u00f4ng t\u0301m th\u1EA5y CSDL" },
	{ "UserPwdError",       "Ng\u01B0\u1EDDi d\u00f9ng v\u00e0 m\u1EADt kh\u1EA9u kh\u00f4ng kh\u1EDBp nhau" },
	{ "RoleNotFound",       "Kh\u00f4ng t\u0301m th\u1EA5y vai tr\u0323 n\u00e0y" },
	{ "Authorized",         "\u0110\u0103 \u0111\u01B0\u1EE3c ph\u00e9p" },
	{ "Ok",                 "\u0110\u1ED3ng \u01b0" },
	{ "Cancel",             "H\u1EE7y" },
	{ "VersionConflict",    "X\u1EA3y ra tranh ch\u1EA5p phi\u00ean b\u1EA3n:" },
	{ "VersionInfo",        "Th\u00f4ng tin v\u1EC1 phi\u00ean b\u1EA3n" },
	{ "PleaseUpgrade",      "Vui l\u0323ng n\u00e2ng c\u1EA5p ch\u01B0\u01A1ng tr\u0301nh" }
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
