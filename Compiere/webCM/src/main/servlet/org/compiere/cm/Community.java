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
package org.compiere.cm;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.compiere.cm.utils.*;
import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *	Community Servlet to handle login, BPartner create & Update etc.
 *	
 *  @author Yves Sandfort
 *  @version $Id$
 */
public class Community extends HttpServletCM
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Logging				  				*/
	private final CLogger				log = CLogger.getCLogger(getClass());

	/**
	 * 	Process Get Request
	 *	@param request
	 *	@param response
	 *	@throws ServletException
	 *	@throws IOException
	 */
	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		HttpSession sess = request.getSession (true);
		sess.setMaxInactiveInterval (WebEnv.TIMEOUT);
		
		ctx = getCtx();
		
		if (sess.getAttribute ("ctx")!=null)
			ctx = (Ctx) sess.getAttribute ("ctx");

		if (ctx.get ("#AD_Client_ID")!=null) {
			new RequestAnalyzer(this, request, false, null);
		}

		
		WebSessionCtx wsc = (WebSessionCtx)sess.getAttribute(WebSessionCtx.NAME);
		//	Create New
		if (wsc == null)
		{
			int [] allIDs = PO.getAllIDs ("W_Store", "AD_Client_ID=" + ctx.get ("#AD_Client_ID"), HttpServletCM.getTrx());
			if (allIDs!=null && allIDs.length>0) 
			{
				wsc = WebSessionCtx.get(request, allIDs[0]);
				wsc.setWStore (allIDs[0]);
				sess.setAttribute(WebSessionCtx.NAME, wsc);
			}
		}
		
		WebLogin thisLogin = new WebLogin(request, response, ctx);
		thisLogin.init ();
		if (!thisLogin.action ())
		{
			WebUtil.reload(thisLogin.getMessage(), thisLogin.getUpdate_page (), sess, request, response, getServletContext());
			return;
		}
		String url = thisLogin.getForward ();
		if (!url.startsWith("/"))
			url = "/" + url;
		log.info("doPost - Forward to " + url);
		response.sendRedirect (url);
	}

	/**
     * Process Post Request (handled by get)
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
	@Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		doGet (request, response);
	} // doPost

}
