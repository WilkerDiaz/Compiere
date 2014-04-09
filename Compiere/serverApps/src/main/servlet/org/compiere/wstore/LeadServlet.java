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
import java.util.*;
import java.util.logging.*;
import java.util.zip.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Lead Servlet
 *
 *  @author Jorg Janke
 *  @version $Id$
 */
public class LeadServlet extends HttpServlet
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**	Logging						*/
	private static CLogger			log = CLogger.getCLogger(LeadServlet.class);
	/** Name						*/
	static public final String		NAME = "leadServlet";

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
			throw new ServletException("LeadServlet.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	@Override
	public String getServletInfo()
	{
		return "Compiere Web Lead Servlet";
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
		Ctx ctx = JSPEnv.getCtx(request);
		HttpSession session = request.getSession(false);
		//
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
		WebInfo info = (WebInfo)session.getAttribute(WebInfo.NAME);
		if (info != null)
			info.setMessage("");

		String forwardTo = request.getParameter("ForwardTo");
		String errorTo = request.getParameter("ErrorTo");

		MLead lead = createLead (ctx, request);
//		if (info != null)
//			info.setMessage(Msg.parseTranslation(ctx, msg));

		Boolean success = download (lead, request, response, ctx);
		if (success != null && success.booleanValue())	//	OK
			return;
		if (success != null && !success.booleanValue() 	//	Error
			&& errorTo != null && errorTo.length() > 0)
			forwardTo = errorTo;

		if (forwardTo == null || forwardTo.length() == 0)
		{
			forwardTo = request.getHeader("referer");
			if (forwardTo == null || forwardTo.length() == 0)
				forwardTo = "/index.html";
		}

		log.info ("Forward to " + forwardTo);
		if (forwardTo.startsWith("http"))
		{
			WebUtil.createForwardPage(response, "Thank you!", forwardTo, 2);
		}
		else
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (forwardTo);
			dispatcher.forward (request, response);
		}
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
	 * 	Create Lead
	 * 	@param ctx context
	 *	@param request request
	 *	@return lead
	 */
	private MLead createLead(Ctx ctx, HttpServletRequest request)
	{
		HashMap<String,String> parameter = new HashMap<String,String>();
		Enumeration<?> ee = request.getParameterNames();
		while (ee.hasMoreElements())
		{
			String key = ee.nextElement().toString();
			String value = request.getParameter (key);
			if (value != null && value.length() > 0)
				parameter.put (key, value);
		}

		log.fine(parameter.toString());
		MLead lead = new MLead (ctx, parameter, null);
		lead.setRemote_Host(request.getRemoteHost());
		lead.setRemote_Addr(request.getRemoteAddr());
		if (lead.save())
			log.info (lead.toString());
		return lead;
	}	//	createLead

	/**
	 * 	Download Product
	 *	@return true if asset streamed, false if error null if n/a
	 */
	private Boolean download (MLead lead, HttpServletRequest request,
		HttpServletResponse response, Ctx ctx)
	{
		//	Log
		StringBuffer info = new StringBuffer("Download ");
		Enumeration<?> ee = request.getParameterNames();
		while (ee.hasMoreElements())
		{
			String key = ee.nextElement().toString();
			String value = request.getParameter (key);
			if (value != null && value.length() > 0)
				info.append(key).append("=").append(value).append(";");
		}
		MAccessLog al = new MAccessLog(ctx, lead.getEMail(),
			request.getRemoteHost(), request.getRemoteAddr(),
			info.toString(), null);
		al.setC_Lead_ID(lead.getC_Lead_ID());
		al.save();
		ctx = al.getCtx();		//	updated with user environment

		//	Download
		String download = request.getParameter("M_ProductDownload_ID");
		//	No Download
		if (download == null || download.length() == 0)
			return null;
		int M_ProductDownload_ID = 0;
		try
		{
			M_ProductDownload_ID = Integer.parseInt (download);
		}
		catch (Exception e)
		{
			log.warning("Parsing M_ProductDownload_ID=" + download + " - " + e.toString());
		}
		if (M_ProductDownload_ID == 0)
			return Boolean.FALSE;

		//	Test Download
		MProductDownload pd = MProductDownload.get(ctx, M_ProductDownload_ID);
		if (lead.getAD_Client_ID() != pd.getAD_Client_ID()
			|| !pd.isLeadDownload()
			|| pd.getDownloadName() == null
			)
		{
			log.warning("Invalid Download - " + lead);
			al.setReply("InvalidDownload: " + download);
			al.save();
			return Boolean.FALSE;
		}

		InputStream in = pd.getDownloadStream(ctx.getContext(WebSessionCtx.CTX_DOCUMENT_DIR));
		if (in == null)
		{
			String info1 = "Download not found: " + pd.getDownloadURL();
			log.warning(info1);
			al.setReply(info1);
			al.save();
			return Boolean.FALSE;
		}
		byte[] downloadInfo = String.valueOf(M_ProductDownload_ID).getBytes();
		log.info("Stream: " + pd + " - " + lead);

		/**
		Download SupportContract.pdf for Jorg Janke
		Thank you for your interest!
		**/
		Object[] args = new Object[] {
			pd.getName(), lead.getContactName()
			};
		String readme = Msg.getMsg(ctx, "LeadDeliveryTemplate", args);


		//	Send File
		al.setAD_Table_ID(X_M_ProductDownload.Table_ID);
		al.setRecord_ID (M_ProductDownload_ID);
		//
		float speed = 0;
		try
		{
			response.setContentType("application/zip");
			response.setHeader("Content-Location", "lead.zip");
		//	response.setContentLength(length);

			int bufferSize = 2048; //	2k Buffer
			response.setBufferSize(bufferSize);
			//
			log.fine(in + ", available=" + in.available());
			long time = System.currentTimeMillis();

			//	Zip Output Stream
			ServletOutputStream out = response.getOutputStream ();
			ZipOutputStream zip = new ZipOutputStream(out);		//	Servlet out
			zip.setMethod(ZipOutputStream.DEFLATED);
			zip.setLevel(Deflater.BEST_COMPRESSION);
			zip.setComment(readme);

			//	Readme File
			ZipEntry entry = new ZipEntry("readme.txt");
			entry.setExtra(downloadInfo);
			zip.putNextEntry(entry);
			zip.write(readme.getBytes(), 0, readme.length());
			zip.closeEntry();

			//	Payload
			entry = new ZipEntry(pd.getDownloadName());
			entry.setExtra(downloadInfo);
			zip.putNextEntry(entry);
			byte[] buffer = new byte[bufferSize];
			int count = 0;
			int totalSize = 0;
			do
			{
				count = in.read(buffer, 0, bufferSize);			//	read delivery
				if (count > 0)
				{
					totalSize += count;
					zip.write (buffer, 0, count);				//	write zip
				}
			} while (count != -1);
			zip.closeEntry();

			//	Fini
			zip.finish();
			zip.close();
			in.close();
			time = System.currentTimeMillis() - time;
			speed = (float)totalSize/1024 / ((float)time/1000);
			String msg = totalSize/1024  + "kB - " + time + " ms - " + speed + " kB/sec";
			log.fine(msg);

			//	Delivery Record
			al.setReply(msg);
			al.save();
		}
		catch (IOException ex)
		{
			String msg = ex.getMessage ();
			if (msg == null || msg.length () == 0)
				msg = ex.toString ();
			log.warning(msg);
			//	Delivery Record
			try
			{
				al.setReply(msg);
				al.save();
			}
			catch (Exception ex1)
			{
				log.log(Level.SEVERE, "2 - " + ex);
			}
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}	//	download

}	//	LeadServlet
