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

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.util.*;

/**
 *  Web User Login.
 * 	<pre>
 * 	User posts Login
 * 	- OK = forward
 *  - Did not find user
 * 	- Invalid Password
 *	</pre>
 *  @author     Jorg Janke
 *  @version    $Id: LoginServlet.java,v 1.6 2006/10/08 18:17:43 comdivision Exp $
 */
public class LoginServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Logging						*/
	private CLogger				log = CLogger.getCLogger(getClass());
	/** Name						*/
	static public final String	NAME = "loginServlet";

	/**
	 *	Initialize global variables
	 *
	 *  @param config Configuration
	 *  @throws ServletException
	 */
	@Override
	public void init(ServletConfig config)
		throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("LoginServlet.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	@Override
	public String getServletInfo()
	{
		return "Compiere Web Login Servlet";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	@Override
	public void destroy()
	{
		log.fine("destroy");
	}   //  destroy

	/**
	 *  Process the HTTP Get request.
	 * 	(logout, deleteCookie)
	 *  Sends Web Request Page
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		HttpSession session = request.getSession(true);	//	create new
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
		//
	//	WEnv.dump(request);
		Ctx ctx = JSPEnv.getCtx(request);		//	create if required

		org.compiere.util.WebLogin thisLogin = new org.compiere.util.WebLogin(request, response, ctx);
		thisLogin.init ();
		//	JJ: cause needs to be fixed
		if (WebUtil.getParameter(request, "Mode") == null)
			if (WebUtil.getParameter(request, "mode") != null)
				thisLogin.setP_Action("mode");
		thisLogin.action();	
		if ("logout".equals(thisLogin.getMode()))
			return;		//	already forwarded

		String url = thisLogin.getLogin_RelURL ();
		
		if (!url.startsWith("/"))
			url = "/" + url;
		log.info ("Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
		return;
	}	//	doGet

	/**
	 *  Process the HTTP Post request
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		Ctx ctx = JSPEnv.getCtx(request);
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
	//	WEnv.dump(session);
	//	WEnv.dump(request);
		

		org.compiere.util.WebLogin thisLogin = new org.compiere.util.WebLogin(request, response, ctx);
		thisLogin.init ();

		//	Forward URL
		String url = thisLogin.getForward ();
		String salesRep = thisLogin.getSalesRep_ID ();	//	get SalesRep from request
		if (salesRep != null)
			session.setAttribute(thisLogin.getP_SalesRep_ID (), salesRep);
		boolean checkOut = "Y".equals(session.getAttribute(CheckOutServlet.ATTR_CHECKOUT));
		//	Set in login.jsp & addressInfo.jsp
		boolean addressConfirm = "Y".equals(WebUtil.getParameter (request, "AddressConfirm"));
		if (checkOut)
		{
//			if (addressConfirm)
				url = "/orderServlet";
//			else
//				url = "/addressInfo.jsp";
		}
		else
			addressConfirm = false;
		if (url == null || url.length() == 0)
		{
			url = (String)session.getAttribute(thisLogin.getP_ForwardTo ());	//	get from session
			if (url == null || url.length() == 0)
				url = "/index.jsp";
		}
		else
		{
			if (!url.startsWith("/"))
				url = "/" + url;
			session.setAttribute(thisLogin.getP_ForwardTo (), url);				//	save for log in issues
		}
		
		//	SalesRep Parameter
		salesRep = (String)session.getAttribute(thisLogin.getP_SalesRep_ID ());	//	get SalesRep from session
		if (salesRep != null)
			url += "?SalesRep_ID=" + salesRep;
		//
		//String mode = WebUtil.getParameter (request, "Mode");
		log.fine("- targeting url=" + url); // + " - mode=" + mode);

		//	Web User
		WebUser wu = WebUser.get(request);
		
		// Handover Loginprodcess to general WebLogin Handler
		// Frst set the URL from our current situation
		thisLogin.setForward (url);
		// Also handover adressConfirm
		thisLogin.setAddressConfirm (addressConfirm);
		// Since Mode Usage is not consequent we will try to figure it out.
		if (WebUtil.getParameter(request, "Mode")==null)
			if (WebUtil.getParameter(request, "mode")!=null)
			thisLogin.setP_Action("mode");
		// Start the process
		thisLogin.action ();
		// getback the URL
		url = thisLogin.getForward ();
		
		wu = thisLogin.getWebUser ();
		session.setAttribute (WebUser.NAME, wu);

		if (!url.startsWith("/"))
			url = "/" + url;
		log.info("doPost - Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}	//	doPost
	
}	//	LoginServlet
