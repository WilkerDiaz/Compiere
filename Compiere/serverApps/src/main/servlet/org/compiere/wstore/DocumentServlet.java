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

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Documentation Streaming Servlet
 *	@author Jorg Janke
 */
public class DocumentServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Logging						*/
	private static CLogger			log = CLogger.getCLogger(DocumentServlet.class);
	/** Name						*/
	static public final String		NAME = "documentServlet";

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
			throw new ServletException("DocumentServlet.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	@Override
	public String getServletInfo()
	{
		return "Compiere Web Document Servlet";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	@Override
	public void destroy()
	{
		log.fine("");
	}   //  destroy


	/**
	 *  Process the HTTP Get request.
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
		log.info("Get from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		//
		//	Parameter = ID
		String msg = streamDocument(request, response);
		if (msg == null				//	OK 
			|| msg.length() == 0 
			|| msg.startsWith("**"))
			return;
		
		WebUtil.createErrorPage(request, response, this, msg);
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
		doGet(request,response);
	}	//	doPost

	/**
	 * 	Stream asset
	 * 	@param request request
	 * 	@param response response
	 * 	@return null or error message
	 */
	protected String streamDocument (HttpServletRequest request, HttpServletResponse response)
	{
		//	Get Parameter
		int AD_ComponentReg_ID = WebUtil.getParameterAsInt (request, "ID");
		if (AD_ComponentReg_ID == 0)
		{
			log.fine("No ID)");
			return "No Document ID";
		}
		//	Get Registration
		Ctx ctx = JSPEnv.getCtx(request);
		MComponentReg reg = MComponentReg.get(ctx, AD_ComponentReg_ID);
		if (reg == null || reg.getAD_ComponentReg_ID() != AD_ComponentReg_ID)
		{
			log.fine("Not found - AD_ComponentReg_ID=" + AD_ComponentReg_ID);
			return "Document not found";
		}
		
		String doc = reg.getDocumentationText();
		if (Util.isEmpty(doc))
			return "No Documentation for " + AD_ComponentReg_ID;

		try
		{
			response.setContentType("text/html; charset=UTF-8");
			
			int bufferSize = 2048; //	2k Buffer
			response.setBufferSize(bufferSize);
			response.setContentLength(doc.length());
			//
			PrintWriter out = response.getWriter();
			out.println(doc);
			
			//	Output Stream
		//	ServletOutputStream out = response.getOutputStream ();
		//	out.write (doc.getBytes());		//	write direct
			
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			return e.getLocalizedMessage();
		}
		return null;
	}	//	streamDocument
	
}	//	DocumentServlet
