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
package org.compiere.cm.invoice;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.cm.*;
import org.compiere.model.*;
import org.compiere.util.*;

public class Invoice extends HttpServletCM{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Stream invoice
	 * 	@param request request
	 * 	@param response response
	 * 	@return "" or error message
	 */
	public static String streamInvoice (HttpServletRequest request, HttpServletResponse response, Ctx ctx) {
			int MIN_SIZE = 2000; 	//	if not created size is 1015
			
			//	Get Invoice ID
			int C_Invoice_ID = WebUtil.getParameterAsInt (request, "Invoice_ID");
			if (C_Invoice_ID == 0)
			{
				return "No Invoice ID";
			}

			//	Get Invoice
			MInvoice invoice = new MInvoice (ctx, C_Invoice_ID, null);
			if (invoice.getC_Invoice_ID() != C_Invoice_ID)
			{
				return "Invoice not found";
			}
			//	Get WebUser & Compare with invoice
			HttpSession session = request.getSession(true);
			WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
			if (wu.getC_BPartner_ID() != invoice.getC_BPartner_ID())
			{
				return "Your invoice not found";
			}

			//	Check Directory
			String dirName = ctx.getContext("documentDir");
			if (dirName == null || dirName.length() == 0)
				dirName = ".";
			try
			{
				File dir = new File (dirName);
				if (!dir.exists ())
					dir.mkdir ();
			}
			catch (Exception ex)
			{
				return "Streaming error - directory";
			}
			//	Check if Invoice already created
			String fileName = invoice.getPDFFileName (dirName);
			File file = new File(fileName);
			if (!file.exists() || !file.isFile() || file.length() < MIN_SIZE)	
			{
				file = invoice.createPDF (file);
				if (file != null)
				{
					invoice.setDatePrinted (new Timestamp(System.currentTimeMillis()));
					invoice.save();
				}
			}
			//	Issue Error
			if (file == null || !file.exists() || file.length() < MIN_SIZE) 
			{
				return "Streaming error - file";
			}

			//	Send PDF
			try
			{
				int bufferSize = 2048; //	2k Buffer
				int fileLength = (int)file.length();
				//
				response.setContentType("application/pdf");
				response.setBufferSize(bufferSize);
				response.setContentLength(fileLength);
				//
				long time = System.currentTimeMillis();		//	timer start
				//
				FileInputStream in = new FileInputStream (file);
				ServletOutputStream out = response.getOutputStream ();
				byte[] buffer = new byte[bufferSize];
				double totalSize = 0;
				int count = 0;
				do
				{
					count = in.read(buffer, 0, bufferSize);
					if (count > 0)
					{
						totalSize += count;
						out.write (buffer, 0, count);
					}
				} while (count != -1);
				out.flush();
				out.close();
				//
				in.close();
				time = System.currentTimeMillis() - time;
			}
			catch (IOException ex)
			{
				return "Streaming error";
			}

			return null;
		}	//	streamInvoice

}	//	InvoiceServlet

