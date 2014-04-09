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

import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.cm.cache.*;
import org.compiere.util.*;

/**
 * HttpServletCM we extended the normal HttpServlet to store some global
 * environment and cache here
 * 
 * @author Yves Sandfort
 * @version $Id$
 */
public class HttpServletCM extends HttpServlet
{

	/**
     * serialVersionUID for serializable HttpServlet
     */
	private static final long		serialVersionUID = 1L;

	/**
     * status if config is loaded... true if loaded false is default after
     * startup
     */
	protected static boolean		configLoaded = false;

	/**
     * fatalError stores a shared variable which will be set to yes whenever a
     * global Server Error from which we can not recover occurs. You should not
     * set it manually as will stop all servlet's and will not rerun them unless
     * set back to false true if there is a global error false if there is no
     * global error
     */
	protected static boolean		fatalError = false;

	/**
     * belongs to fatalError, should get a corresponding error message to
     * display in the web frontend. null if no error
     */
	protected static String			ErrorMessage = null;

	/**
     * DomainCache is storing the domains of the system, to reduce DB lookups
     */
	protected static Domain			domainCache = new Domain ();

	/**
     * WebProject is storing the projects often used by the system, to reduce DB
     * lookups
     */
	protected static WebProject		webProjectCache = new WebProject ();

	/**
     * ChatCache is storing the chat's often used by the system, to reduce DB
     * lookups
     */
	protected static Chat			chatCache = new Chat ();

	/**
     * ContainerCache is storing the containers's often used by the system, to
     * reduce DB lookups
     */
	protected static Container		containerCache = new Container ();

	/**
     * ContainerElementCache is storing the container's elements often used by
     * the system, to reduce DB lookups
     */
	protected static ContainerElement containerElementCache = new ContainerElement ();

	/**
     * ContainerTreeCache is storing the container's tree often used by the
     * system, to reduce DB lookups
     */
	protected static ContainerTree	containerTreeCache	= new ContainerTree ();

	/**
     * MediaServerCache is storing the project's media server in combination
     * with Request Info
     */
	protected static MediaServer	mediaServerCache	  = new MediaServer ();

	/**
     * TemplateCache stores the oftens used templates
     */
	protected static Template		templateCache		 = new Template ();

	/**
     * WikiCache stores the wiki tokens for faster resolution
     */
	protected static WikiToken		wikiCache		 = new WikiToken ();

	/**
     * XMLCache stores the oftens used templates
     */
	protected static XML			xmlCache		 = new XML ();

	/**
     * Context of this Servlet
     */
	protected Ctx			ctx				   = null;

	/**
     * String containing the internal media path (should not be used for real
     * page deployment) the normal broadcaster will replace this URL with the
     * correct media server for this client
     */
	protected String				internalMediaURL	  = null;

	/**
     * String containing the external media URL
     */
	protected String				externalMediaURL	  = null;

	/** Logger */
	protected CLogger				log  = CLogger.getCLogger (getClass());
	
	
	/** Global shared WebCM Trx */
	private static Trx s_trx;

	/**
     * Init
     * 
     * @param config
     * @throws ServletException
     */
	@Override
	public void init (ServletConfig config)
		throws ServletException
	{
		super.init (config);
		if (!WebEnv.initWeb (config))
			throw new ServletException ("Broadcast.init");
		if (!DB.isConnected ())
		{
			fatalError = true;
			ErrorMessage = "Connection to DB dropped!";
			log.severe ("No Database Connection!");
		}
		org.compiere.cm.utils.CMEnv cmEnv = new org.compiere.cm.utils.CMEnv ();
		ctx = cmEnv.getDefaults();
		chatCache.setCtx (ctx);
		containerCache.setCtx (ctx);
		containerElementCache.setCtx (ctx);
		containerTreeCache.setCtx (ctx);
		domainCache.setCtx (ctx);
		mediaServerCache.setCtx (ctx);
		templateCache.setCtx (ctx);
		webProjectCache.setCtx (ctx);
		wikiCache.init(ctx);
		xmlCache.setCtx(ctx);
		if (!fatalError)
			configLoaded = true;
	}

	/**
     * Returns the ContainerCache Object
     * 
     * @return CO Object Container
     */
	public org.compiere.cm.cache.Container getContainerCache ()
	{
		return containerCache;
	}

	/**
     * Returns the ChatCache Object
     * 
     * @return CO Object Chat
     */
	public org.compiere.cm.cache.Chat getChatCache ()
	{
		return chatCache;
	}

	/**
     * Returns the ContainerElementCache Object
     * 
     * @return CO Object ContainerElement
     */
	public org.compiere.cm.cache.ContainerElement getContainerElementCache ()
	{
		return containerElementCache;
	}

	/**
     * Returns the ContainerTreeCache Object
     * 
     * @return CO Object ContainerElement
     */
	public org.compiere.cm.cache.ContainerTree getContainerTreeCache ()
	{
		return containerTreeCache;
	}

	/**
     * Returns the DomainCache Object
     * 
     * @return CO Object Domain
     */
	public org.compiere.cm.cache.Domain getDomainCache ()
	{
		return domainCache;
	}

	/**
     * Returns the MediaServerCache Object
     * 
     * @return CO Object MediaServer
     */
	public org.compiere.cm.cache.MediaServer getMediaServerCache ()
	{
		return mediaServerCache;
	}

	/**
     * Returns the WebProjectCache Object
     * 
     * @return CO Object WebProject
     */
	public org.compiere.cm.cache.WebProject getWebProjectCache ()
	{
		return webProjectCache;
	}

	/**
     * Returns the Template Object
     * 
     * @return CO Object Template
     */
	public org.compiere.cm.cache.Template getTemplateCache ()
	{
		return templateCache;
	}

	/**
     * Returns the WikiToken Object
     * 
     * @return CO Object WikiToken
     */
	public org.compiere.cm.cache.WikiToken getWikiTokenCache ()
	{
		return wikiCache;
	}

	/**
     * Returns the Template Object
     * 
     * @return CO Object Template
     */
	public org.compiere.cm.cache.XML getXMLCache ()
	{
		return xmlCache;
	}

	/**
     * Returns the internalMediaURL for replacement
     * 
     * @return String with internal MediaURL normally context + "/" + media +
     *         "/"
     */
	public String getInternalMediaURL ()
	{
		return internalMediaURL;
	}

	/**
     * Returns the sessionMediaURL, this is the ideal Media URL for this Request
     * 
     * @param request
     *            the Request for this
     * @param CM_WebProject_ID
     *            Returns the WebProject ID
     * @return String with session MediaURL, if none found we return the
     *         internal one
     */
	public String getSessionMediaURL (HttpServletRequest request,
		int CM_WebProject_ID)
	{
		String sessionMediaURL = getMediaServerCache ().getMediaServer (
			getCtx (), CM_WebProject_ID, null);
		if (sessionMediaURL == null)
			sessionMediaURL = getInternalMediaURL ();
		return internalMediaURL;
	}

	/**
     * Sets internal Media URL
     * 
     * @param request
     */
	public void resetInternalMediaURL (HttpServletRequest request)
	{
		internalMediaURL = request.getRequestURL ().toString ().substring (
			0,
			request.getRequestURL ().toString ().indexOf (
				request.getServerName ())
				+ request.getServerName ().length ())
			+ request.getContextPath () + "/media/";
	}

	/**
     * Returns the Context of the current session. This is a very sensitive
     * function as we will take care of all parameters here!
     * 
     * @return Context
     */
	public Ctx getCtx ()
	{
		return ctx;
	}

	/**
     * Returns the current External Media URL for the Project
     * 
     * @param CM_WebProject_ID
     *            WebProject
     * @return ExternalMediaURL as String
     */
	public String getExternalMediaURL (int CM_WebProject_ID)
	{
		externalMediaURL = getMediaServerCache ().getMediaServer (getCtx (),
			CM_WebProject_ID, null);
		if (externalMediaURL != null
			&& externalMediaURL.charAt (externalMediaURL.length () - 1) != '/')
			externalMediaURL = externalMediaURL + "/";
		return externalMediaURL;
	}

	/**
     * Returns the current External Media URL be carefull this Function needs to
     * be called after the URL is set!
     * 
     * @return ExternalMediaURL as String
     */
	public String getExternalMediaURL ()
	{
		return externalMediaURL;
	}
	
	/**
	 * 	setAD_Client_ID to update AD_Client as soon System has recognized the Client
	 *	@param newVal
	 */
	public void setAD_Client_ID(int newVal)
	{
		ctx.put ("#AD_Client_ID", (""+newVal));
	}
	
	/**
	 * 	get Servlet Logger
	 *	@return CLooger log
	 */
	public CLogger getLogger() 
	{
		return log;
	}
	

	/**
	 * Get the global shared WebCM Trx 
	 * @return
	 */
	public static synchronized Trx getTrx() {
		if (s_trx != null) {
			try {				
				if (s_trx.getConnection() == null || s_trx.getConnection().isClosed()
						|| (DB.isOracle() && !s_trx.getConnection().isValid(0))) {
					s_trx.close();
					s_trx = null;
				}
			} catch (SQLException e) {
				s_trx.close();
				s_trx = null;
			}
		}

		if (s_trx == null)
			s_trx = Trx.get("WebCM");
		return s_trx;
	}
	
}
