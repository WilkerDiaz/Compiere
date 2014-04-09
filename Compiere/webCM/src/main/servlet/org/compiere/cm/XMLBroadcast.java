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
import org.compiere.cm.xml.*;

/**
 * @author YS
 * @version $Id$
 */
public class XMLBroadcast extends HttpServletCM
{
	/**	serialVersionUID	*/
	private static final long serialVersionUID = -1280320974132533949L;

	/**
	 * 	Get
	 *	@param request
	 *	@param response
	 *	@throws ServletException
	 *	@throws IOException
	 */
	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		RequestAnalyzer thisRequest = new RequestAnalyzer (this, request,
			false, "/xml");
		if (thisRequest.checkLogin()) 
		{
			// Even if we will only display the XML tree we are forced to build the Media URLs
			resetInternalMediaURL (request);
			if (externalMediaURL == null) {
				if (thisRequest.getWebProject()!=null)
					externalMediaURL = getExternalMediaURL (thisRequest
						.getWebProject ().get_ID ());
				else 
					externalMediaURL = getInternalMediaURL();
				
			}
			StringBuffer xmlAppend = new StringBuffer();
			// This Request has a Processor Class Name, so we should process it!
			if (thisRequest.getProcClassName ()!=null) {
				try {
					org.compiere.cm.Extend thisProcessor = thisRequest.getProcClass();
					thisProcessor.doIt ();
					xmlAppend.append(thisProcessor.getXML());
					if (thisProcessor.getRedirectURL()!=null)
						thisRequest.setRedirectURL(thisProcessor.getRedirectURL());
				}
				catch (Exception ex) 
				{
					ex.printStackTrace ();
				}
				
			}
			// Generate the needed XMLCode
			Generator thisXMLGen = new Generator (this, request, thisRequest, xmlAppend);
			String xmlCode = thisXMLGen.get ();
			response.setContentType ("text/xml; charset=UTF8");
			PrintWriter out;
			out = response.getWriter ();
			out.print (xmlCode);
			out.close ();
		} else {
			response.sendError (401, "Wrong syntax please use /xml/UserName/Password with a user who has at least role access to this client.");
		}
	}	//	doGet
	
}	//	HttpServletCM
