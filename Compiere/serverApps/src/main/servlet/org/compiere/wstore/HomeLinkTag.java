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
package org.compiere.wstore;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.apache.ecs.xhtml.*;
import org.compiere.util.*;

/**
 *  Home Link 
 *  <pre>
 *  <cws:homeLink/>
 *	</pre>
 *
 */
public class HomeLinkTag extends TagSupport
{
	private static final long serialVersionUID = 4423247708207070433L;
	/**	Logger	*/
	
	private static String TYPE_BUTTON = "button";
	private static String TYPE_LINK = "link";
	
	private enum LinkType { BUTTON, LINK };
	
	private LinkType linkType = LinkType.BUTTON;
	private String styleClass = "";
	private String label = "Home";
	
	
	public void setType (String type)
	{
		if (type.equalsIgnoreCase(TYPE_LINK) ) {
			linkType = LinkType.LINK;
		}
		else {
			linkType = LinkType.BUTTON;
		}
	}	
	
	public String getType() {
		return linkType.toString();
	}
	
	public void setStyleClass(String cl) {
		styleClass = cl;
	}
	public String getStyleClass() {
		return styleClass;
	}

	public void setLabel(String lbl) {
		label = lbl;
	}
	public String getLabel() {
		return label;
	}

	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 * 	@throws JspException
	 */
	@Override
	public int doStartTag() throws JspException
	{
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		WebSessionCtx wsc = WebSessionCtx.get(request, true);
		JspWriter out = pageContext.getOut();
		
		HtmlCode html = new HtmlCode();
		
		if (linkType == LinkType.LINK)
		{ 
			a aitem = new a (wsc.wstore.getURL());
			aitem.addElement (label);
			html.addElement (aitem);
		}
		else {
			input btn = new input();
			btn.setType(TYPE_BUTTON);
			btn.setValue(label);
			btn.setOnClick("javascript: window.location='" + wsc.wstore.getURL() + "';");
			btn.setClass(styleClass);
			html.addElement (btn);
		}
		html.output(out);
		return (SKIP_BODY);
	}   //  doStartTag

	


}	//	HomeLinkTag
