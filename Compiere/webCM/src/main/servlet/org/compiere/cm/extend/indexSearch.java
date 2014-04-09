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
package org.compiere.cm.extend;

import javax.servlet.http.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Search the index for results
 *	
 *  @author Yves Sandfort
 *  @version $Id$
 */
public class indexSearch extends org.compiere.cm.Extend
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public indexSearch (HttpServletRequest request, Ctx ctx)
	{
		super (request, ctx);
	}
	
	@Override
	public boolean doIt()
	{
		if (WebUtil.getParameter (e_request,"query")!=null) {
			appendXML("<searchResults>\n");
			appendXML("<query><![CDATA[" + WebUtil.getParameter (e_request,"query") + "]]></query>\n");
			MIndex [] searchResults = MIndex.getResults (WebUtil.getParameter(e_request, "query"), getCtx(), null);
			if (searchResults!=null && searchResults.length>0) {
				appendXML("<results>" + searchResults.length + "</results>\n");
			} else {
				appendXML("<results>0</results>\n");
			}
			for (MIndex searchResult : searchResults) {
				appendXML(searchResult.get_xmlString (new StringBuffer("")));
			}
			appendXML("</searchResults>\n");
		}
		return true;
	}
}
