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
 *  Swedish License Dialog Translation
 *
 *  @version    $Id: IniRes_sv.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class IniRes_sv extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "Licensavtal" },
	{ "Do_you_accept",      "Godk\u00e4nner du licensen ?" },
	{ "No",                 "Nej" },
	{ "Yes_I_Understand",   "Ja, jag f\u00f6rst\u00e5r och godk\u00e4nner" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "Licens inte godk\u00e4nd eller l\u00f6pt ut" }
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
