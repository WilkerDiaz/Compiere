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
 * 	@version 	$Id: ALoginRes_ml.java 8394 2010-02-04 22:55:56Z freyes $
 */
public final class ALoginRes_ml extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Hubungan" },
	{ "Defaults",           "Defaults" },
	{ "Login",              "Compiere Login" },
	{ "File",               "Fail" },
	{ "Exit",               "Keluar" },
	{ "Help",               "Tolong" },
	{ "About",              "Tentang" },
	{ "Host",               "Host" },
	{ "Database",           "Pangkalan Data" },
	{ "User",               "ID Pengguna" },
	{ "EnterUser",          "Masukkan ID Pengguna" },
	{ "Password",           "Kata Laluan" },
	{ "EnterPassword",      "Masukkan Kata Laluan Applikasi" },
	{ "Language",           "Bahasa" },
	{ "SelectLanguage",     "Pilih Bahasa Anda" },
	{ "Role",               "Role" },
	{ "Client",             "Pengguna" },
	{ "Organization",       "Organisasi" },
	{ "Date",               "Tarikh" },
	{ "Warehouse",          "Warehouse" },
	{ "Printer",            "Printer" },
	{ "Connected",          "Telah dihubungi" },
	{ "NotConnected",       "Tiday dapat dihubungi" },
	{ "DatabaseNotFound",   "Pangkalan Data tidak dijumpai" },
	{ "UserPwdError",       "Pengguna tidak padan dengan kata laluan" },
	{ "RoleNotFound",       "Role not found/complete" },
	{ "Authorized",         "Authorized" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Batal" },
	{ "VersionConflict",    "Bertentangan versi:" },
	{ "VersionInfo",        "Pelayan <> Pengguna" },
	{ "PleaseUpgrade",      "Please run the update program" }
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
