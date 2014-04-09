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
import java.net.*;
import java.util.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Issue Reporting
 *	
 *  @author Jorg Janke
 *  @version $Id: IssueReportServlet.java,v 1.2 2006/07/30 00:53:21 jjanke Exp $
 */
public class IssueReportServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Logging						*/
	private static CLogger			log = CLogger.getCLogger(IssueReportServlet.class);

	/**
	 * 	Initialize global variables
	 *  @param config servlet configuration
	 *  @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("IssueReportServlet.init");
	}	//	init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	@Override
	public String getServletInfo()
	{
		return "Compiere Issue Reporting";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	@Override
	public void destroy()
	{
		log.info("");
	}   //  destroy

	
	/**************************************************************************
	 *  Process the initial HTTP Get request.
	 *  Reads the Parameter Amt and optional C_Invoice_ID
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
		Ctx ctx = JSPEnv.getCtx(request);
		request.getSession(true);

		int AD_Issue_ID = WebUtil.getParameterAsInt(request, "RECORDID");
		String DBAddress = WebUtil.getParameter(request, "DBADDRESS");
		String Comments = WebUtil.getParameter(request, "COMMENTS");
		String IssueString = WebUtil.getParameter(request, "ISSUE");
		//
		StringBuffer responseText = new StringBuffer("Compiere Support - ")
			.append(new Date().toString())
			.append("\n");
		MIssue issue = null;
		if (AD_Issue_ID != 0)
		{
			issue = new MIssue(ctx, AD_Issue_ID, null);
			if (issue.get_ID() != AD_Issue_ID)
				responseText.append("Issue Unknown - Request Ignored");
			else if (!issue.getDBAddress().equals(DBAddress))
				responseText.append("Not Issue Owner - Request Ignored");
			else
			{
				issue.addComments(Comments);
				responseText.append(issue.createAnswer());
			}
		}
		else if (IssueString == null || IssueString.length() == 0)
		{
			responseText.append("Unknown Request");
		}
		else
		{
			issue = MIssue.create(ctx, IssueString);
			if (issue == null || !issue.save())
				responseText.append("Could not save Issue");
			else
				responseText.append(issue.process());
		}
		
		//
		StringBuffer answer = new StringBuffer();
		if (issue != null && issue.get_ID() != 0)
		{
			answer.append("RECORDID=").append(issue.get_ID())
				.append(MIssue.DELIMITER);
		//	answer.append("DOCUMENTNO=").append(".")
		//		.append(MIssue.DELIMITER);
		}
		answer.append("RESPONSE=").append(responseText);
		//
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();     //  with character encoding support
		out.write(URLEncoder.encode(answer.toString(), "UTF-8"));
		out.flush();
		if (out.checkError())
			log.log(Level.SEVERE, "error writing");
		out.close();
	}   //  doGet

	/**
	 *  Process the HTTP Post request.
	 * 	The actual payment processing
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
		log.info("Post from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		doGet(request, response);
	}   //  doPost

}	//	IssueReportServlet
