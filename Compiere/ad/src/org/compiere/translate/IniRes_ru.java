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
 *  @version    $Id: IniRes_ru.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class IniRes_ru extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u043e\u043d\u043d\u043e\u0435 \u0441\u043e\u0433\u043b\u0430\u0448\u0435\u043d\u0438\u0435" },
	{ "Do_you_accept",      "\u0412\u044b \u043f\u0440\u0438\u043d\u0438\u043c\u0430\u0435\u0442\u0435 \u043b\u0438\u0446\u0435\u043d\u0437\u0438\u043e\u043d\u043d\u043e\u0435 \u0441\u043e\u0433\u043b\u0430\u0448\u0435\u043d\u0438\u0435?" },
	{ "No",                 "\u041d\u0435\u0442" },
	{ "Yes_I_Understand",   "\u0414\u0430, \u042f \u043f\u0440\u0438\u043d\u0438\u043c\u0430\u044e" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u044f \u043d\u0435 \u043f\u0440\u0438\u043d\u044f\u0442\u0430 \u0438\u043b\u0438 \u0438\u0441\u0442\u0435\u043a \u0441\u0440\u043e\u043a \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u044f" }
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
