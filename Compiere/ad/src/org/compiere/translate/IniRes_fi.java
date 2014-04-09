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
 *  License Dialog Translation for Finnish language
 *
 * 	@version 	$Id: IniRes_fi.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class IniRes_fi extends ListResourceBundle
{
	/** Translation Content */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "Lisensointiehdot" },
	{ "Do_you_accept",      "Hyv\u00e4ksytk\u00f6 lisenssin?" },
	{ "No",                 "Ei" },
	{ "Yes_I_Understand",   "Kyll\u00e4, ymm\u00e4rr\u00e4n ja hyv\u00e4ksyn ehdot" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "Lisenssi hyl\u00e4tty tai vanhentunut" }
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
