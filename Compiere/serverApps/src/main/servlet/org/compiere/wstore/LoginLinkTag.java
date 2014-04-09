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
 *  Login Link.
 * 	Creates Login/Logout Link
 *  <pre>
 *  <cws:loginLink />
 *  Variable used - "webUser"
 *	</pre>
 *
 *  @author Jorg Janke
 *  @version $Id: LoginLinkTag.java,v 1.3 2006/07/30 00:53:21 jjanke Exp $
 */
public class LoginLinkTag extends TagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Logger							*/
	protected static CLogger	log = CLogger.getCLogger (LoginLinkTag.class);

	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 * 	@throws JspException
	 */
	@Override
	public int doStartTag() throws JspException
	{
		Ctx ctx = JSPEnv.getCtx((HttpServletRequest)pageContext.getRequest());
		//
		WebUser wu = getWebUser(ctx);
		if (wu == null)
			pageContext.getSession().removeAttribute(WebUser.NAME);
		else
			pageContext.getSession().setAttribute (WebUser.NAME, wu);
		//
		String serverContext = ctx.getContext(WebSessionCtx.CTX_SERVER_CONTEXT);
	//	log.fine("doStartTag - ServerContext=" + serverContext);
		HtmlCode html = null;
		if (wu != null && wu.isValid())
			html = getWelcomeLink (serverContext, wu);
		else
			html = getLoginLink (serverContext);
		//
		JspWriter out = pageContext.getOut();
		/**
		//	Delete Cookie Call
		if (cookieUser != null && !cookieUser.equals(" "))
		{
			log.fine("- Cookie=" + cookieUser);
			html.addElement(" ");
			a a = new a("loginServlet?mode=deleteCookie");
			a.setClass("menuDetail");
			a.addElement("(Delete&nbsp;Cookie)");
			html.addElement(a);
		}
		**/
		html.output(out);
		//
		//
		return (SKIP_BODY);
	}   //  doStartTag

	/**
	 * 	End Tag
	 * 	@return EVAL_PAGE
	 * 	@throws JspException
	 */
	@Override
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}	//	doEndTag


	/**
	 *	Get WebUser.
	 * 	@param ctx context
	 * 	@return Web User or null
	 */
	private WebUser getWebUser (Ctx ctx)
	{
		String address = pageContext.getRequest().getRemoteAddr();
		//	Get stored User
		WebUser wu = (WebUser)pageContext.getSession().getAttribute (WebUser.NAME);
		if (wu != null)
		{
			log.finest("(" + address + ") - SessionContext: " + wu);
		}
		else
		{
			wu = (WebUser)pageContext.getAttribute(WebUser.NAME);
			if (wu != null)
				log.finest ("(" + address + ") - Context: " + wu);
		}
		if (wu != null)
			return wu;

		//	Check Coockie
		String cookieUser = JSPEnv.getCookieWebUser ((HttpServletRequest)pageContext.getRequest());
		if (cookieUser == null || cookieUser.trim().length() == 0)
			log.finer ("(" + address + ") - no cookie");
		else
		{
			//	Try to Load
			wu = WebUser.get (ctx, cookieUser);
			log.finer ("(" + address + ") - Cookie: " + wu);
		}
		if (wu != null)
			return wu;
		//
		return null;
	}	//	getWebUser

	
	/**************************************************************************
	 * 	Get Login Link
	 * 	@param	serverContext server context
	 * 	@return link
	 */
	private HtmlCode getLoginLink(String serverContext)
	{
		HtmlCode retValue = new HtmlCode();
		//	Login button
//		input button = new input(input.TYPE_BUTTON, "Login", "Login");
//		button.setOnClick("window.top.location.replace('" + serverContext + "/loginServlet');");
        a loginLink = new a("javascript: return false;");
        loginLink.addElement("Login");
        //loginLink.setClass(CLASS_LINK);
        loginLink.setOnClick ("window.top.location.replace('" + serverContext + "/loginServlet');");
		retValue.addElement(loginLink);


		/**	Link
		a a = new a("https://" + serverContext + "/login.jsp");
		a.setClass("menuMain");
		a.addElement("Login");
		retValue.addElement(a);
		**/

		retValue.addElement(" ");
		return retValue;
	}	//	getLoginLink

	private static String CLASS_LINK = "loginLink";
	
	/**
	 * 	Get Welcome Link
	 * 	@param	serverContext server Context
	 * 	@param wu web user
	 * 	@return link
	 */
	private HtmlCode getWelcomeLink(String serverContext, WebUser wu)
	{
		HtmlCode retValue = new HtmlCode();
		//
		a a = new a(serverContext + "/login.jsp");
		//a.setClass(CLASS_LINK);
		String msg = "Welcome " + wu.getName();
		a.addElement(msg);
		retValue.addElement(a);
		//
		retValue.addElement(" &nbsp; ");
		if (wu.isLoggedIn())
		{
			//	Verify
			if (!wu.isEMailVerified())
			{
//				input button = new input(input.TYPE_BUTTON, "Verify", "Verify EMail");
//				button.setOnClick("window.top.location.replace('emailVerify.jsp');");
//				retValue.addElement(button);
//				retValue.addElement(" ");
				
				a emailLink = new a("javascript: return false;");
				emailLink.addElement("Verify Email&nbsp;");
				emailLink.setOnClick("window.top.location.replace('emailVerify.jsp'); return false;");
				emailLink.setClass(CLASS_LINK);
	            retValue.addElement(emailLink);

//				cell2.addElement(emailLink);
//				cell2.addElement(" ");
			}
			
			//	Update
//			input button = new input(input.TYPE_BUTTON, "Update", "Update User Info");
//			button.setOnClick("window.top.location.replace('update.jsp');");
//			retValue.addElement(button);
//			retValue.addElement(" ");
			a updateLink = new a("javascript: return false;");
			updateLink.addElement("My Account");
			updateLink.setOnClick("window.top.location.replace('update.jsp'); return false;");
//			updateLink.setClass(CLASS_LINK);
//			cell2.addElement("|");
//			cell2.addElement(updateLink);
//			cell2.addElement(" ");
			
			//	Logout
//			button = new input(input.TYPE_BUTTON, "Logout", "Logout");
//			button.setOnClick("window.top.location.replace('loginServlet?mode=logout');");
//			retValue.addElement(button);

			a logoutLink = new a("javascript: return false;");
			logoutLink.addElement("Logout");
			logoutLink.setOnClick("window.top.location.replace('loginServlet?mode=logout'); return false;");
			//logoutLink.setClass(CLASS_LINK);
//			cell2.addElement("|");
//			cell2.addElement(logoutLink);
//			cell2.addElement(" ");
			
			/** Link
			a = new a ("loginServlet?mode=logout");
			a.setClass ("menuMain");
			a.addElement ("Logout");
			retValue.addElement (a);
			**/
            retValue.addElement(logoutLink);
		}
		else
		{
//			input button = new input (input.TYPE_BUTTON, "Login", "Login");
//			button.setOnClick ("window.top.location.replace('" + serverContext + "/login.jsp');");
//			retValue.addElement (button);
			
			a loginLink = new a("javascript: return false;");
			loginLink.addElement("Login");
//			loginLink.setClass(CLASS_LINK);
			loginLink.setOnClick ("window.top.location.replace('" + serverContext + "/login.jsp');");
//			cell2.addElement("|");
//			cell2.addElement(loginLink);
			retValue.addElement(loginLink);
		}
//		retValue.addElement (" ");
		
//		row.addElement(cell1); row.addElement(cell2);
//		tab.addElement(row);
//		retValue.addElement(tab);
		
		//
		return retValue;
	}	//	getWelcomeLink

}	//	LoginLinkTag
