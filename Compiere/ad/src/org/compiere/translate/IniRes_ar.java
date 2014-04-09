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
 * License Dialog Translation
 * @version     $Id: IniRes_ar.java 8394 2010-02-04 22:55:56Z freyes $
 */

public class IniRes_ar extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "\u0631\u062e\u0635\u0629 \u0643\u0645\u0628\u064a\u0631" },
	{ "Do_you_accept",      "\u0647\u0644 \u0623\u0646\u062a \u0645\u0648\u0627\u0641\u0642\u061f" },
	{ "No",                 "\u0644\u0627" },
	{ "Yes_I_Understand",   "\u0646\u0639\u0645\u060c \u0623\u0641\u0647\u0645 \u0648 \u0623\u0648\u0627\u0641\u0642" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "\u0627\u0644\u0631\u0651\u064f\u062e\u0635\u0629 \u0645\u0631\u0641\u0648\u0636\u0629 \u0623\u0648 \u0645\u0646\u062a\u0647\u064a\u0629" }
	};

	/**
	 * Get Content
	 * @return  Content
	 * @uml.property  name="contents"
	 */
	@Override
	public Object[][] getContents()
	{
		return contents;
	}   //  getContent
}   //  IniRes_ar_TN

