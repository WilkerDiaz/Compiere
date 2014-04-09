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
 *  @version    $Id: IniRes_fa.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class IniRes_fa extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Compiere_License",   "\u0642\u0628\u0648\u0644 \u0645\u062c\u0648\u0632 \u0646\u0631\u0645 \u0627\u0641\u0632\u0627\u0631" },
	{ "Do_you_accept",      "\u0622\u064a\u0627 \u0645\u062c\u0648\u0632 \u0646\u0631\u0645 \u0627\u0641\u0632\u0627\u0631 \u0631\u0627 \u0642\u0628\u0648\u0644 \u062f\u0627\u0631\u064a\u062f\u061f" },
	{ "No",                 "\u062e\u064a\u0631" },
	{ "Yes_I_Understand",   "\u0628\u0644\u0647 \u0645\u0646 \u0645\u062c\u0648\u0632 \u0631\u0627 \u0641\u0647\u0645\u064a\u062f\u0647 \u0648 \u0642\u0628\u0648\u0644 \u062f\u0627\u0631\u0645" },
	{ "license_htm",        "org/compiere/license.htm" },
	{ "License_rejected",   "\u0645\u062c\u0648\u0632 \u0645\u0648\u0631\u062f \u0642\u0628\u0648\u0644 \u0648\u0627\u0642\u0639 \u0646\u0634\u062f \u064a\u0627 \u0645\u0646\u0642\u0636\u06cc \u0634\u062f\u0647" }
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
