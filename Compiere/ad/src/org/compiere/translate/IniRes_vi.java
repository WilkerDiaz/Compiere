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
 *  License Dialog Translation
 *
 *  @version    $Id: IniRes_vi.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class IniRes_vi extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "Gi\u1EA5y ph\u00e9p s\u1EED d\u1EE5ng" },
	{ "Do_you_accept",      "B\u1EA1n c\u00f3 ch\u1EA5p nh\u1EADn gi\u1EA5y ph\u00e9p n\u00e0y kh\u00f4ng?" },
	{ "No",                 "Kh\u00f4ng" },
	{ "Yes_I_Understand",   "V\u00e2ng, t\u00f4i hi\u1EC3u v\u00e0 ch\u1EA5p nh\u1EADn gi\u1EA5p ph\u00e9p n\u00e0y" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "Gi\u1EA5y ph\u00e9p b\u1ECB kh\u01B0\u1EDBc t\u1EEB ho\u1EB7c h\u1EBFt h\u1EA1n" }
	};

	/**
	 *  Get Content
	 *  @return Content
	 */
	@Override
	public Object[][] getContents()
	{
		return contents;
	}   //  getContent
}   //  IniRes
