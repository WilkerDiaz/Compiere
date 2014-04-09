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
import org.compiere.cm.invoice.*;
import org.compiere.util.*;

/**
 *	Request Servlet to handle Request create & Update etc.
 *	
 *  @author Kai Viiksaar
 *  @version $Id: RequestServlet.java,v 1.1 2006/10/11 06:30:11 comdivision Exp $
 */
public class InvoiceServlet extends HttpServletCM {
	/**	serialVersionUID	*/
	private static final long serialVersionUID = 6979583935052312291L;

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
		String url = "/invoices.html";
		
		ctx = getCtx();
		
		if (sess.getAttribute ("ctx")!=null)
			ctx = (Ctx) sess.getAttribute ("ctx");

		WebSessionCtx wsc = (WebSessionCtx)sess.getAttribute(WebSessionCtx.NAME);
		
		//	Get Invoice as PDF
		if (wsc != null) {
			String mode = WebUtil.getParameter(request, "Mode");
			if (mode != null && mode.equals("InvoiceAsPDF")) {
				Invoice.streamInvoice(request, response, ctx);
			}
		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);		
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
