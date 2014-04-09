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
import java.util.logging.*;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Asset (Delivery) Servlet.
 *
 *  @author Jorg Janke
 *  @version $Id: AssetServlet.java,v 1.3 2006/09/16 08:32:33 comdivision Exp $
 */
public class AssetServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Logging						*/
	private static CLogger			log = CLogger.getCLogger(AssetServlet.class);
	/** Name						*/
	static public final String		NAME = "assetServlet";

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
			throw new ServletException("AssetServlet.init");
	}   //  init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	@Override
	public String getServletInfo()
	{
		return "Compiere Web Assets Servlet";
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
		String url = "/assets.jsp";
		if (session == null 
			|| session.getAttribute(WebInfo.NAME) == null
			|| session.getAttribute(WebUser.NAME) == null)
			url = "/login.jsp";
		else
		{
			session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
			WebInfo info = (WebInfo)session.getAttribute(WebInfo.NAME);
			if (info != null)
				info.setMessage("");

			//	Parameter = Asset_ID - if invoice is valid and belongs to wu then create PDF & stream it
			String msg = streamAsset(request, response);
			if (info != null)
				info.setMessage(Msg.parseTranslation(ctx, msg));
			if (msg == null				//	OK 
				|| msg.length() == 0 
				|| msg.startsWith("**"))
			//	if not returned - results in exception: Cannot forward after response has been committed 
				return;
		}

		log.info ("Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
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
	protected String streamAsset (HttpServletRequest request, HttpServletResponse response)
	{
		//	Get Asset ID
		int A_Asset_ID = WebUtil.getParameterAsInt (request, "Asset_ID");
		if (A_Asset_ID == 0)
		{
			log.fine("No ID)");
			return "No Asset ID";
		}
		//	Get Asset
		Ctx ctx = JSPEnv.getCtx(request);
		HttpSession session = request.getSession(true);
		WebEnv.dump(request);
		MAsset asset = new MAsset(ctx, A_Asset_ID, null);
		if (asset.getA_Asset_ID() != A_Asset_ID)
		{
			log.fine("Asset not found - ID=" + A_Asset_ID);
			return "Asset not found";
		}
		//	Get WebUser & Compare with invoice
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		if (wu.getC_BPartner_ID() != asset.getC_BPartner_ID())
		{
			log.warning ("A_Asset_ID=" + A_Asset_ID
				+ " - BP_Invoice=" + asset.getC_BPartner_ID()
				+ " <> BP_User=" + wu.getC_BPartner_ID());
			return "Your asset not found";
		}
		if (!asset.isDownloadable() || wu.isCreditStopHold() || !wu.isEMailVerified())
			return "Asset not downloadable";

		//	Name & URL
		String pd = WebUtil.getParameter(request, "PD");
		String dl_name = null;
		String dl_url = null;
		InputStream dl_in = null;
		int M_ProductDownload_ID = 0;
		if (pd != null && pd.length() > 0)
		{
			MProductDownload[] pdls = asset.getProductDownloads();
			if (pdls != null)
			{
				for (MProductDownload element : pdls) {
					if (element.getDownloadURL().indexOf(pd) != -1)
					{
						M_ProductDownload_ID = element.getM_ProductDownload_ID();
						dl_name = pd;
						dl_url = element.getDownloadURL();
						dl_in = element.getDownloadStream(ctx.getContext(WebSessionCtx.CTX_DOCUMENT_DIR));
						break;
					}
				}
			}
		}
		log.fine(dl_name + " - " + dl_url);
		if (dl_name == null || dl_url == null || dl_in == null)
			return "@NotFound@ @A_Asset_ID@: " + pd;

		String dl_contentType = "application/zip";
		if (dl_url.endsWith(".html") || dl_url.endsWith(".htm"))
		{
			dl_contentType = "text/html";
			return streamDirect(request, response, 
					ctx, wu, asset, M_ProductDownload_ID, 
					dl_name, dl_url, dl_in, dl_contentType);
		}
		
		return streamZip(request, response, 
				ctx, wu, asset, M_ProductDownload_ID,
				dl_name, dl_url, dl_in);
	}	//	streamAsset
	
	/**
	 * 	Stream as Zip File	
	 * 	@param request
	 * 	@param response
	 * 	@param ctx
	 * 	@param wu
	 * 	@param asset
	 * 	@param M_ProductDownload_ID
	 * 	@param dl_name
	 * 	@param dl_url
	 * 	@param dl_in
	 * 	@return null or error message
	 */
	private String streamZip(HttpServletRequest request, HttpServletResponse response,
			Ctx ctx, WebUser wu, MAsset asset, int M_ProductDownload_ID,
			String dl_name, String dl_url, InputStream dl_in)
	{
		/**
		Download SupportContract.pdf for Jorg Janke - jjanke@compiere.org
		Version = 120 - Lot = . - SerNo = .
		Guarantee Date = 5/30/05
		Thank you for using Compiere Customer Asset Management
		**/
		String lot = asset.getLot();
		if (lot == null || lot.length() == 0)
			lot = ".";
		String ser = asset.getSerNo();
		if (ser == null || ser.length() == 0)
			ser = ".";
		Object[] args = new Object[] {
			dl_name, 
			wu.getName() + " - " + wu.getEmail(),
			asset.getVersionNo(), 
			lot, 
			ser, 
			asset.getGuaranteeDate()};
		String readme = Msg.getMsg(ctx, "AssetDeliveryTemplate", args);
		byte[] assetInfo = String.valueOf(asset.getA_Asset_ID()).getBytes();
		
		//	Send File
		MAssetDelivery ad = asset.confirmDelivery(request, wu.getAD_User_ID());
		if (M_ProductDownload_ID != 0)
			ad.setM_ProductDownload_ID(M_ProductDownload_ID);
		ad.setDescription(dl_name);
		//
		float speed = 0;
		try
		{
			response.setContentType("application/zip");
			response.setHeader("Content-Location", "asset.zip");
		//	response.setContentLength(length);
			
			int bufferSize = 2048; //	2k Buffer
			response.setBufferSize(bufferSize);
			//
			log.fine(dl_in + ", available=" + dl_in.available());
			long time = System.currentTimeMillis();
			
			//	Zip Output Stream
			ServletOutputStream out = response.getOutputStream ();
			ZipOutputStream zip = new ZipOutputStream(out);		//	Servlet out
			zip.setMethod(ZipOutputStream.DEFLATED);
			zip.setLevel(Deflater.BEST_COMPRESSION);
			zip.setComment(readme);
							
			//	Readme File
			ZipEntry entry = new ZipEntry("readme.txt");
			entry.setExtra(assetInfo);
			zip.putNextEntry(entry);
			zip.write(readme.getBytes(), 0, readme.length());
			zip.closeEntry();
			
			//	Payload
			entry = new ZipEntry(dl_name);
			entry.setExtra(assetInfo);
			zip.putNextEntry(entry);
			byte[] buffer = new byte[bufferSize];
			int count = 0;
			int totalSize = 0;
			do
			{
				count = dl_in.read(buffer, 0, bufferSize);			//	read delivery
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
			dl_in.close();
			time = System.currentTimeMillis() - time;
			speed = ((float)totalSize/1024) / ((float)time/1000);
			String msg = (totalSize/1024)  + "kB - " + time + " ms - " + speed + " kB/sec";
			log.fine(msg);
			
			//	Delivery Record
			ad.setDeliveryConfirmation(msg);
			ad.save();
			asset.save();
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
				if (msg.length () > 120)
					msg = msg.substring (0, 119);
				ad.setDeliveryConfirmation (msg);
				ad.save ();
			//	asset.save();	not delivered
			}
			catch (Exception ex1)
			{
				log.log(Level.SEVERE, "2 - " + ex);
			}
			//	need to differentiate error message as response committed
			return "** Streaming error; Please Retry"; 
		}
		//
		return null;
	}	//	streamZip
	
	
	private String streamDirect (HttpServletRequest request, HttpServletResponse response,
			Ctx ctx, WebUser wu, MAsset asset, int M_ProductDownload_ID,
			String dl_name, String dl_url, InputStream dl_in, String dl_contentType)
	{
		//	Send File
		MAssetDelivery ad = asset.confirmDelivery(request, wu.getAD_User_ID());
		if (M_ProductDownload_ID != 0)
			ad.setM_ProductDownload_ID(M_ProductDownload_ID);
		ad.setDescription(dl_name);
		//
		float speed = 0;
		try
		{
			response.setContentType(dl_contentType);
			
			int bufferSize = 2048; //	2k Buffer
			response.setBufferSize(bufferSize);
			//
			log.fine(dl_in + ", available=" + dl_in.available());
			long time = System.currentTimeMillis();
			
			//	Output Stream
			ServletOutputStream out = response.getOutputStream ();
			byte[] buffer = new byte[bufferSize];
			int count = 0;
			int totalSize = 0;
			do
			{
				count = dl_in.read(buffer, 0, bufferSize);			//	read delivery
				if (count > 0)
				{
					totalSize += count;
					out.write (buffer, 0, count);				//	write direct
				}
			} while (count != -1);
			
			//	Fini
			out.flush();
			dl_in.close();
			time = System.currentTimeMillis() - time;
			speed = ((float)totalSize/1024) / ((float)time/1000);
			String msg = (totalSize/1024)  + "kB - " + time + " ms - " + speed + " kB/sec";
			log.fine(msg);
			
			//	Delivery Record
			ad.setDeliveryConfirmation(msg);
			ad.save();
			asset.save();
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
				if (msg.length () > 120)
					msg = msg.substring (0, 119);
				ad.setDeliveryConfirmation (msg);
				ad.save ();
			//	asset.save();	not delivered
			}
			catch (Exception ex1)
			{
				log.log(Level.SEVERE, "2 - " + ex);
			}
			//	need to differentiate error message as response committed
			return "** Streaming error; Please Retry"; 
		}
		//
		return null;
	}	//	streamDirect


}	//	AssetServlet
