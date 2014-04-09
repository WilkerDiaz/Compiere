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
 * 	@version 	$Id: ALoginRes_in.java 8394 2010-02-04 22:55:56Z freyes $
 */
public final class ALoginRes_in extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Koneksi" },
	{ "Defaults",           "Konfigurasi Dasar" },
	{ "Login",              "Login Compiere" },
	{ "File",               "File" },
	{ "Exit",               "Keluar" },
	{ "Help",               "Tolong" },
	{ "About",              "Tentang" },
	{ "Host",               "Pusat" },
	{ "Database",           "Database" },
	{ "User",               "ID Pengguna" },
	{ "EnterUser",          "Masukkan ID pengguna" },
	{ "Password",           "Kata Sandi" },
	{ "EnterPassword",      "Masukkan kata sandi applikasi" },
	{ "Language",           "Pilihan Bahasa" },
	{ "SelectLanguage",     "Pilihlah bahasa yang disukai" },
	{ "Role",               "Jabatan" },
	{ "Client",             "Klien" },
	{ "Organization",       "Organisasi" },
	{ "Date",               "Tanggal" },
	{ "Warehouse",          "Gudang" },
	{ "Printer",            "Pencetak" },
	{ "Connected",          "Sistem telah terkoneksi" },
	{ "NotConnected",       "Sistem tidak terkoneksi!" },
	{ "DatabaseNotFound",   "Database tidak ditemukan!" },
	{ "UserPwdError",       "Nama ID pengguna anda tidak sesuai dengan kata sandi" },
	{ "RoleNotFound",       "Jabatan tidak ditemukan" },
	{ "Authorized",         "Anda telah diotorisasi" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Batal" },
	{ "VersionConflict",    "Konflik Versi" },
	{ "VersionInfo",        "Info Versi" },
	{ "ChangeRole",         "Ganti Jabatan" },
	{ "PleaseUpgrade",      "Mohon hubungi partner Compiere anda untuk upgrade" }
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
