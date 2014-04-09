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
 *  License Dialog Translation (Thai)
 *
 *  @version    $Id: IniRes_th.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class IniRes_th extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "\u0e02\u0e49\u0e2d\u0e15\u0e01\u0e25\u0e07\u0e17\u0e32\u0e07\u0e25\u0e34\u0e02\u0e2a\u0e34\u0e17\u0e18\u0e34\u0e4c" },
	{ "Do_you_accept",      "\u0e17\u0e48\u0e32\u0e19\u0e44\u0e14\u0e49\u0e22\u0e2d\u0e21\u0e23\u0e31\u0e1a\u0e25\u0e34\u0e02\u0e2a\u0e34\u0e17\u0e18\u0e34\u0e4c\u0e2b\u0e23\u0e37\u0e2d\u0e44\u0e21\u0e48" },
	{ "No",                 "\u0e44\u0e21\u0e48\u0e22\u0e2d\u0e21\u0e23\u0e31\u0e1a" },
	{ "Yes_I_Understand",   "\u0e22\u0e2d\u0e21\u0e23\u0e31\u0e1a, \u0e02\u0e49\u0e32\u0e1e\u0e40\u0e08\u0e49\u0e32\u0e21\u0e35\u0e04\u0e27\u0e32\u0e21\u0e40\u0e02\u0e49\u0e32\u0e43\u0e08\u0e41\u0e25\u0e30\u0e22\u0e2d\u0e21\u0e23\u0e31\u0e1a\u0e15\u0e32\u0e21\u0e02\u0e49\u0e2d\u0e15\u0e01\u0e25\u0e07\u0e17\u0e32\u0e07\u0e25\u0e34\u0e02\u0e2a\u0e34\u0e17\u0e18\u0e34\u0e4c\u0e19\u0e35\u0e49" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "\u0e25\u0e34\u0e02\u0e2a\u0e34\u0e17\u0e18\u0e34\u0e4c" }
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
