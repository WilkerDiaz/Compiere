/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.util;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.compiere.common.CompiereStateException;
import org.compiere.common.constants.*;
import org.compiere.model.*;


/**
 *	Web Session Context Value Object
 *	
 *  @author Jorg Janke
 *  @version $Id: WebSessionCtx.java,v 1.6 2006/09/23 11:04:19 comdivision Exp $
 */
public class WebSessionCtx implements Serializable
{
	/**	SV */
	private static final long serialVersionUID = -5069858671373130221L;

	/**
	 * 	Get/create Web Session Context
	 *	@param request request
	 *	@return ctx or null
	 */
	public static WebSessionCtx get (HttpServletRequest request, int W_Store_ID)
	{
		HttpSession session = request.getSession(false);
		if (session == null)
			session = request.getSession(true);
		if (session == null)
			return null;
		WebSessionCtx wsc = (WebSessionCtx)session.getAttribute(NAME);
		//	Create New
		if (wsc == null)
		{
			wsc = new WebSessionCtx(request, W_Store_ID);
			session.setAttribute(NAME, wsc);
		}
		return wsc;
	}	//	get

	/**
	 * 	Get/create Web Session Context
	 *	@param request request
	 *	@return ctx or null
	 */
	public static WebSessionCtx get (HttpServletRequest request, boolean createNew)
	{
		HttpSession session = request.getSession(false);
		if (session == null)
			session = request.getSession(true);
		if (session == null)
			return null;
		WebSessionCtx wsc = (WebSessionCtx)session.getAttribute(NAME);
		//	Create New
		if (createNew && wsc == null)
		{
			wsc = new WebSessionCtx (request);
			session.setAttribute(NAME, wsc);
		}
		return wsc;
	}	//	get
	
	/** Context						*/
	public final static String		CTX_SERVER_CONTEXT = "context";
	/** Document Directory	*/
	public final static String		CTX_DOCUMENT_DIR = "documentDir";
	/** Header (Error) Message			*/
	public final static String		HDR_MESSAGE = "hdrMessage";
	/** Header Info Message				*/
	public final static String		HDR_INFO = "hdrInfo";

	/**	Logger							*/
	static private CLogger			log = CLogger.getCLogger (WebSessionCtx.class);
	/** Cache 60 minutes				*/
	private static final CCache<Integer,Ctx> s_cacheCtx = new CCache<Integer,Ctx>("WebSessionCtx", 30, 60);

	public final static String		KEY_CWS = "compiereWS";
	
	/**************************************************************************
	 * 	Web Session Context
	 * 	@param request request
	 */
	private WebSessionCtx (HttpServletRequest request)
	{
		log.info (request.getContextPath() + " (" + request.getRemoteAddr() 
			+ " - " + request.getLocale() + ") #" + counter);
		ctx = new Ctx();
		setLanguage(request);
		
		HttpSession session = request.getSession(false);
		
		//	Add Servlet Init Parameters (webStore/src/web/WEB-INF/web.xml)
		ServletContext sc = session.getServletContext();
		Enumeration<?> en = sc.getInitParameterNames();
		while (en.hasMoreElements())
		{
			String key = (String)en.nextElement();
			String value = sc.getInitParameter(key);
			ctx.setContext(key, value);
			log.config (key + "=" + value); 
		}

		setWStore (request.getContextPath());
		ctx = getDefaults();
		
		//	ServerContext	- dev2/wstore
		String serverContext = (request.isSecure())?"https://":"http://";
		serverContext += request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		ctx.put(CTX_SERVER_CONTEXT, serverContext);
		
		//	Make Context directly available to jsp's
		session.setAttribute("ctx", ctx);
		//
		log.fine("#" + ctx.size());
	}	//	WebSessionCtx

	/**************************************************************************
	 * 	Web Session Context
	 * 	@param request request
	 */
	private WebSessionCtx (HttpServletRequest request, int W_Store_ID)
	{
		log.info (request.getContextPath() + " (" + request.getRemoteAddr() 
			+ " - " + request.getLocale() + ") #" + counter);
		ctx = new Ctx();
		setLanguage(request);
		
		HttpSession session = request.getSession(false);
		
		//	Add Servlet Init Parameters (webStore/src/web/WEB-INF/web.xml)
		ServletContext sc = session.getServletContext();
		Enumeration<?> en = sc.getInitParameterNames();
		while (en.hasMoreElements())
		{
			String key = (String)en.nextElement();
			String value = sc.getInitParameter(key);
			ctx.setContext(key, value);
			log.config (key + "=" + value); 
		}

		setWStore (W_Store_ID);
		ctx = getDefaults ();
		
		//	ServerContext	- dev2/wstore
		String serverContext = request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		if (request.isSecure())
		  serverContext = "https://"+serverContext;
		else
		  serverContext = "http://"+serverContext;		
		ctx.put(CTX_SERVER_CONTEXT, serverContext);
		//	Make Context directly availabe to jsp's
		session.setAttribute("ctx", ctx);
		//
		log.fine("#" + ctx.size());
	}	//	WebSessionCtx

	/**	Sessition Attribute Name			*/
	public static final String	NAME = "WebSessionCtx";
	
	/**	Static counter				*/
	public static int 		s_counter = 0;
	/** Instance Counter			*/
	public int				counter = ++s_counter;
	
	
	/** Session Context 			*/
	public Ctx				ctx = null;
	/**	Session Language			*/
	public Language			language = null;
	
	/** Localized Date format       */
	public SimpleDateFormat dateFormat = null;
	/** Localized Timestamp format  */
	public SimpleDateFormat dateTimeFormat = null;

	/** Localized Amount format    */
	public DecimalFormat 	amountFormat = null;
	/** Localized Integer format    */
	public DecimalFormat 	integerFormat = null;
	/** Localized Number format     */
	public DecimalFormat 	numberFormat = null;
	/** Localized Quantity format   */
	public DecimalFormat 	quantityFormat = null;

	/** Login Info					*/
	public String			loginInfo = "";
	/** Web Store					*/
	public MStore			wstore = null;
	
	/**
	 * 	Set Web Store
	 *	@param contextPath web server context path
	 */
	private void setWStore (String contextPath)
	{
		//	get from context
		int W_Store_ID = ctx.getContextAsInt("W_Store_ID");
		if (W_Store_ID != 0)
		{
			wstore = MStore.get(ctx, W_Store_ID);
			if (wstore.getW_Store_ID() != 0)
			{
				log.info("From web.xml - " + wstore);
				return;
			}
		}
		if ("/compiere".equals(contextPath))	//	HTML UI
			return;
		//
		wstore = MStore.get(ctx, contextPath);
		if (wstore == null)
			throw new CompiereStateException("No Web Store found - " + contextPath);
	}	//	setWStore
	
	
	/**
	 * 	Set Web Store
	 *	@param W_Store_ID Web Store ID
	 */
	public void setWStore (int W_Store_ID)
	{
		//	get from context
		if (W_Store_ID != 0)
		{
			wstore = MStore.get(ctx, W_Store_ID);
			if (wstore.getW_Store_ID() != 0)
			{
				log.info("From web.xml - " + wstore);
				return;
			}
		}
		if (wstore == null)
			throw new CompiereStateException("No Web Store found - ID: " + W_Store_ID);
	}	//	setWStore
 	
	/**
	 * 	Get Web Store Defaults
	 * 	@return context
	 */
	private Ctx getDefaults ()
	{
		//	No Web Store
		if (wstore == null)
			return new Ctx();
		//
		Integer key = Integer.valueOf (wstore.getW_Store_ID());
		Ctx newCtx = s_cacheCtx.get(null, key);
		
		/**	Create New Context		*/
		if (newCtx == null)
		{
			log.info(wstore.getWebContext());
			newCtx = new Ctx();
			//	copy explicitly
			Iterator<?> it = ctx.keySet().iterator();
			while (it.hasNext())
			{
				String pKey = (String)it.next();
				newCtx.setContext(pKey, ctx.getContext(pKey));
			}
			
			newCtx.setAD_Client_ID(wstore.getAD_Client_ID());
			newCtx.setAD_Org_ID(wstore.getAD_Org_ID());
			//
			newCtx.setContext("#SalesRep_ID", wstore.getSalesRep_ID());
			newCtx.setContext("#M_PriceList_ID", wstore.getM_PriceList_ID());
			newCtx.setContext("#M_Warehouse_ID", wstore.getM_Warehouse_ID());
			//
			String s = wstore.getWebParam1();
			newCtx.setContext("webParam1", s == null ? "" : s);
			s = wstore.getWebParam2();
			newCtx.setContext("webParam2", s == null ? "" : s);
			s = wstore.getWebParam3();
			newCtx.setContext("webParam3", s == null ? "" : s);
			s = wstore.getWebParam4();
			newCtx.setContext("webParam4", s == null ? "" : s);
			s = wstore.getWebParam5();
			newCtx.setContext("webParam5", s == null ? "" : s);
			s = wstore.getWebParam6();
			newCtx.setContext("webParam6", s == null ? "" : s);
			s = wstore.getStylesheet();
			if (s == null)
				s = "standard";
			else
			{
				int index = s.lastIndexOf('.');
				if (index != -1)
					s = s.substring(0, index);
			}
			newCtx.setContext("Stylesheet", s);
			//
			s = wstore.getWebInfo();
			if (s != null && s.length() > 0)
				newCtx.setContext(HDR_INFO, s);
			
			//	Payment Term
			newCtx.setContext("#M_PriceList_ID", wstore.getM_PriceList_ID());

			//	Default User - SalesRep
			if (newCtx.getAD_User_ID() == 0)
				newCtx.setAD_User_ID(wstore.getSalesRep_ID());
			
			//	Default Role for access
			if (newCtx.getAD_Role_ID() == 0)
			{
				int AD_Role_ID = 0;		//	HARDCODED - System
				newCtx.setAD_Role_ID(AD_Role_ID);
			}

			//	Client
			MClient client = MClient.get (newCtx, wstore.getAD_Client_ID());
			//	Name,Description, SMTPHost,RequestEMail,RequestUser, RequestUserPw
			newCtx.setContext("name", client.getName());
			newCtx.setContext("description", client.getDescription());
			
			//	AD_Language
			if (newCtx.getContext("#AD_Language") == null && client.getAD_Language() != null)
				newCtx.setContext("#AD_Language", client.getAD_Language());
			//	DocumentDir
			String docDir = client.getDocumentDir();
			newCtx.setContext(CTX_DOCUMENT_DIR, docDir == null ? "" : docDir);

			//	Default Language
			if (newCtx.getContext("#AD_Language") == null)
				newCtx.setContext("#AD_Language", "en_US");

			// flag for enabling/disabling compiere web store validation
			// TODO: parameterized
			s = wstore.getURL();
			if (s != null && s.contains("compiere.com")) {
				newCtx.setContext(KEY_CWS, true);
			}
			
			//	Save Context - Key is AD_Client_ID
			s_cacheCtx.put(key, newCtx);
		}
		//	return new Properties (pp);	seems not to work with JSP
		Iterator<?> it = newCtx.keySet().iterator();
		while (it.hasNext())
		{
			String pKey = (String)it.next();
			ctx.setContext(pKey, newCtx.getContext(pKey));
		}
		return ctx;
	}	//	getDefaults

	
	/**************************************************************************
	 *  Set Language from request or session in.
	 *  - Properties
	 *  - Cookie
	 *  - Session
	 *  @param request request
	 */
	private void setLanguage (HttpServletRequest request)
	{
		//  Get Cookie
		Properties cProp = WebUtil.getCookieProperties(request);
		
		//  Get/set Parameter:      Language
		String AD_Language = WebUtil.getParameter (request, Env.LANGUAGE);
		if (AD_Language == null)
		{
			//  Check Cookie
			AD_Language = cProp.getProperty(Env.LANGUAGE);
			if (AD_Language == null)
			{
				//  Check Request Locale
				Locale locale = request.getLocale();
				AD_Language = Language.getAD_Language (locale);
			}
		}
		if (AD_Language != null)
		{
			Language lang = Language.getLanguage(AD_Language);
			lang = Env.verifyLanguage (lang);
			ctx.setContext(Env.LANGUAGE, lang.getAD_Language());
			Msg.getMsg(ctx, "0");
			cProp.setProperty(Env.LANGUAGE, lang.getAD_Language());
			setLanguage(lang);
		}
		else if (language == null)	//	set base language
			setLanguage (Language.getBaseLanguage());
	}	//	setLanguage

	/**
	 * 	Set Language and init formats
	 *	@param lang language
	 */
	private void setLanguage (Language lang)
	{
		language = lang;
		//
		dateFormat = DisplayType.getDateFormat(DisplayTypeConstants.Date, language);
		dateTimeFormat = DisplayType.getDateFormat(DisplayTypeConstants.DateTime, language);
		//
		amountFormat = DisplayType.getNumberFormat(DisplayTypeConstants.Amount, language);
		integerFormat = DisplayType.getNumberFormat(DisplayTypeConstants.Integer, language);
		numberFormat = DisplayType.getNumberFormat(DisplayTypeConstants.Number, language);
		quantityFormat = DisplayType.getNumberFormat(DisplayTypeConstants.Quantity, language);
	}	//	setLanguage
	
	/**
	 * 	String representation
	 *	@return Session + count
	 */
	@Override
	public String toString ()
	{
		return "WSessionCtx#" + counter;
	}	//	toString
	
}	//	WSessionCtx
