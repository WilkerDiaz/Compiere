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
package org.compiere.cm.utils;

import java.lang.reflect.*;
import java.net.*;
import javax.servlet.http.*;
import org.compiere.cm.*;
import org.compiere.cm.cache.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * RequestAnalyzer
 * 
 * @author Yves Sandfort
 * @version $Id$
 */
public class RequestAnalyzer
{

	private final String				m_requestURL;

	private String				m_relativeURL;

	private final String				m_serverName;

	private final String				m_baseURL;

	private String				m_redirectURL;
	
	private String				m_procClassName = null;
	
	private String				m_userName;
	
	private String				m_passWord;

	private final MWebProjectDomain	m_WebProjectDomain;

	private MWebProject			m_WebProject;

	private MContainer			m_Container;
	
	private MCStage				m_CStage;

	private boolean				m_isValid	= false;

	private boolean				m_isRedirect = false;
	
	private boolean				m_isStageRequest = false;

	private final HttpServletRequest  m_request;
	
	private final Ctx					m_ctx;
	
	/**
	 * 	RequestAnalyzer
	 *	@param servlet servlet
	 *	@param request request
	 *	@param showStage show stage
	 *	@param servletExtend servlet extend
	 */
	public RequestAnalyzer (HttpServletCM servlet, HttpServletRequest request,
		boolean showStage, String servletExtend)
	{
		Domain domainCache = servlet.getDomainCache ();
		WebProject webProjectCache = servlet.getWebProjectCache ();
		Container containerCache = servlet.getContainerCache ();
		if (servletExtend==null) servletExtend="";
		m_request = request;
		m_ctx = servlet.getCtx ();
		m_requestURL = m_request.getRequestURL ().toString ();
		m_serverName = m_request.getServerName ();
		m_baseURL = m_requestURL.substring (0, m_requestURL.indexOf (m_serverName)
			+ m_serverName.length () + servletExtend.length ())
			+ m_request.getContextPath ();
		m_relativeURL = m_requestURL.substring (m_baseURL.length ());
		// If servletExtend is /xml we must get out the UserName and Password
		if (servletExtend.equals ("/xml")) 
		{
			// m_baseURL up to next / should be UserName
			if (m_relativeURL!=null && m_relativeURL.indexOf ("/",1)>0) {
				m_userName = m_relativeURL.substring (1,m_relativeURL.indexOf ("/",1));
				m_relativeURL = m_relativeURL.substring (m_userName.length ()+1);
				// So we should find a UserName
				if (m_relativeURL!=null && m_relativeURL.indexOf ("/",1)>0) {
					m_passWord = m_relativeURL.substring (1,m_relativeURL.indexOf ("/",1));
					m_relativeURL = m_relativeURL.substring (m_passWord.length ()+1);
				}
			}
		}
		if (servletExtend.equals ("/stage"))
		{
			if (showStage)
				m_isStageRequest=true;
		}
		// If RelativeURL is empty it should be /
		if (m_relativeURL== null || m_relativeURL.equals("")) m_relativeURL="/";
		// If URL ends with a / we should continue it with index.html
		if (m_relativeURL.substring (m_relativeURL.length () - 1).equals ("/"))
			m_relativeURL = m_relativeURL + "index.html";
		m_request.isSecure ();
		m_request.getServerPort ();
		m_WebProjectDomain = domainCache.getWebProjectDomain (m_serverName);
		if (m_WebProjectDomain != null)
		{
			// If we could identify the Domain we will have a project etc.
			m_WebProject = webProjectCache.getWebProject 
				(m_WebProjectDomain.getCM_WebProject_ID ());
		}
		else
		{
			// Since we have not found a sufficient WebProject Domain we will
            // fallback to the default
			int[] defaultID = PO.getAllIDs ("CM_WebProject",
				"AD_Client_ID=0", null);
			if (defaultID.length > 0)
				m_WebProject = webProjectCache.getWebProject (defaultID[0]);
			else	 {
				m_isRedirect = true;
				m_redirectURL = m_requestURL.substring(0, m_requestURL.indexOf(m_serverName) + m_serverName.length())
						+ ":" + request.getServerPort() + "/admin/";
			}
				// JJ
				//throw new IllegalStateException("Unknown context - Set up Web Project"); // no known context
		}
		// Check for compiere.jnlp
		if (m_relativeURL!=null) {
			if(m_relativeURL.equals("/compiere.jnlp") || m_relativeURL.equals("/compiereDirect.jnlp"))
			{
				m_isRedirect = true;
				m_redirectURL = m_requestURL.substring(0, m_requestURL.indexOf(m_serverName) + m_serverName.length())
						+ ":" + request.getServerPort() + "/admin" + m_relativeURL;
			}
		}
		if (!m_isRedirect) {
			if (m_relativeURL != null)
			{
				if (m_isStageRequest)
				{
					getCM_CStage();
				} else {
					// We have a URL, so let's see whether we can handle it...
					m_Container = containerCache.getCM_ContainerByURL (m_relativeURL,
						m_WebProject.get_ID (), true);
					if (m_Container == null)
						m_isValid = false;
					else
						m_isValid = true;
					if (m_isValid && !m_Container.getRelativeURL ().equals (m_relativeURL))
					{
						m_isRedirect = true;
						m_redirectURL = m_Container.getRelativeURL ();
					}
				}
			}
			else
			{
				// We have no or an invalid relative URL found, so we need to
	            // fallback to Domain or Error handling
				if (m_WebProjectDomain.getCM_Container_ID () > 0)
				{
					m_Container = containerCache.getCM_Container (
						m_WebProjectDomain.getCM_Container_ID (), 
							m_WebProject.get_ID ());
				}
				if (m_Container == null)
				{
					m_Container = containerCache.getCM_ContainerByURL 
						("/index.html", m_WebProject.get_ID (), true);
					if (m_Container == null)
					{
						m_isValid = false;
					}
					else
					{
						m_isValid = true;
					}
					if (m_isValid
						&& !m_Container.getRelativeURL ().equals ("/index.html"))
					{
						m_isRedirect = true;
						m_redirectURL = m_Container.getRelativeURL ();
					}
				}
			}
			if (m_isValid == false) {
				// Try to solve invalid requests
				if (m_WebProject==null || m_WebProject.getAD_Client_ID()==0) 
				{
					// If we endup with an invalid request in NULL or System Project we redirect to /admin/
					m_isRedirect = true;
					m_redirectURL = m_requestURL.substring(0, m_requestURL.indexOf(m_serverName)
							+ m_serverName.length())
							+ ":" + request.getServerPort() + "/admin/";
				}
			}
			if (m_isValid) {
				if (m_Container.getContainerType ().equals ("L")) {
					m_isRedirect = true;
					MContainer linkedContainer = containerCache.getCM_Container (m_Container.getCM_ContainerLink_ID (), m_WebProject.get_ID());
					if (linkedContainer!=null) 
						m_redirectURL = linkedContainer.getRelativeURL ();
				}
				servlet.setAD_Client_ID(m_WebProject.getAD_Client_ID());
				
			}
		}
		if (WebUtil.getParameter (m_request, "cn")!=null) {
			String className = WebUtil.getParameter(m_request, "cn");
			// First check compiere.cm.
			if (classChecker("compiere.cm." + className,servlet.getLogger())) 
				m_procClassName = "compiere.cm." + className;
			if (classChecker("org.compiere.cm.extend." + className,servlet.getLogger())) 
				m_procClassName = "org.compiere.cm.extend." + className;
			if (classChecker("org.compierenma.cm.extend." + className,servlet.getLogger())) 
				m_procClassName = "org.compierenma.cm.extend." + className;
		}
	}	//	RequestAnalyzer
	
	private boolean classChecker(String className, CLogger log) {
		try
		{
			Class<?> clazz = Class.forName(className);
			//	Make sure that it is a cm.Extend class
			Class<?> superClazz = clazz.getSuperclass();
			while (superClazz != null)
			{
				if (superClazz == org.compiere.cm.Extend.class)
				{
					log.fine("Use: " + className);
					return true;
				}
			}
		}
		catch (Exception e)
		{
		}
		log.finest("Not found: " + className);
		return false;
	}
	
	/**
	 * 	Get Proc Class
	 *	@return extended ProcClass
	 */
	public org.compiere.cm.Extend getProcClass() 
	{
		if (m_procClassName==null)
			return null;
		try {
			Class<?> thisProcClass = Class.forName (m_procClassName);
			try
			{
				Constructor<?> constructor = thisProcClass.getDeclaredConstructor(new Class[]{HttpServletRequest.class, Ctx.class});
				Extend procClass = (Extend)constructor.newInstance(new Object[] {m_request, m_ctx});
				return procClass;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
		catch (Exception e)
		{
		}
		return null;
	}
	
	private int getCM_CStage_ID()
	{
		int cm_cstage_id = 0;
		if(m_relativeURL!=null)
		{
			String t_relativeURL = m_relativeURL;
			int lastParent = 0;
			if (t_relativeURL.indexOf ("/")==0)
				t_relativeURL = t_relativeURL.substring (1);
			while (t_relativeURL.contains("/"))
			{
				MCStage thisStage = MCStage.getByName(m_WebProject, t_relativeURL.substring (0,t_relativeURL.indexOf ("/")), lastParent);
				if (thisStage!=null) 
				{
					lastParent = thisStage.get_ID ();
					t_relativeURL = t_relativeURL.substring (t_relativeURL.indexOf ("/")+1);
				}
			}
			MCStage thisStage = MCStage.getByName(m_WebProject, t_relativeURL, lastParent);
			if (thisStage!=null) 
				cm_cstage_id = thisStage.get_ID ();
		}
		return cm_cstage_id;
	}
	
	public MCStage getCM_CStage()
	{
		if (m_CStage==null)
		{
			if (getCM_CStage_ID()>0)
				m_CStage = new MCStage(m_ctx, getCM_CStage_ID(), null);
			return m_CStage;
		} else {
			return m_CStage;
		}
	}

	/**
	 * 	Get Request URL
	 *	@return request url
	 */
	public String getRequestURL ()
	{
		return m_requestURL;
	}	//	getRequestURL

	/**
	 * 	Get ServerName
	 *	@return server name
	 */
	public String getServerName ()
	{
		return m_serverName;
	}	//	getServerName

	/**
	 * 	get WebProject_Domain
	 *	@return web project domain 
	 */
	public MWebProjectDomain getWebProjectDomain()
	{
		return m_WebProjectDomain;
	}	//	getWebProjectDomain

	/**
	 * 	Get WebProject
	 *	@return web project
	 */
	public MWebProject getWebProject ()
	{
		return m_WebProject;
	}	//	getWebProject

	/**
	 * 	Get CM_Container
	 *	@return container
	 */
	public MContainer getCM_Container ()
	{
		return m_Container;
	}	//	getCM_Container

	/**
	 * 	Valid
	 *	@return true if valid
	 */
	public boolean getIsValid ()
	{
		return m_isValid;
	}	//	getIsValid

	/**
	 * 	Redirect
	 *	@return true redirect
	 */
	public boolean getIsRedirect ()
	{
		return m_isRedirect;
	}	//	getIsRedirect
	
	/**
	 * 	setRedirectURL
	 *	@param redirectURL
	 */
	public void setRedirectURL(String redirectURL)
	{
		m_redirectURL = redirectURL;
	}

	/**
	 * 	Get Redirect URL
	 *	@return URL
	 */
	public String getRedirectURL ()
	{
		try {
			new URL(m_redirectURL);
			return m_redirectURL;
		} catch (MalformedURLException E) {
			if (m_redirectURL.equals ("/error404.html"))
			{
				return m_baseURL + m_redirectURL + "?errorURL=" + m_relativeURL;
			}
			else
			{
				return m_baseURL + m_redirectURL;
			}
		}
	}	//	getRedirectURL
	
	/**
	 * 	check Login
	 *	@return true if login successfull
	 */
	public boolean checkLogin() {
		if (m_userName != null && m_passWord != null && m_WebProject.getAD_Client_ID ()>0) {
			if (m_userName.indexOf ("%20")>=0) 
				m_userName = org.compiere.util.Util.replace(m_userName, "%20", " ");
			Login testLogin = new Login(m_ctx);
			KeyNamePair[] thisRoles = testLogin.getRoles (m_userName, m_passWord);
			if (thisRoles!=null && thisRoles.length>0) {
				for (KeyNamePair thisRole : thisRoles) {
					KeyNamePair[] thisClients = testLogin.getClients (thisRole);
					for (KeyNamePair thisClient : thisClients) {
						if (thisClient.getKey ()==m_WebProject.getAD_Client_ID ())
							return true;
					}
				}
			}
			return false;
		} else {
			return false;
		}
	}
	
	public Ctx getCtx()
	{
		return m_ctx;
	}
	
	/**
	 * 	get Username
	 *	@return username
	 */
	public String getUsername()
	{
		return m_userName;
	}
	
	/**
	 * 	get Password
	 *	@return password
	 */
	public String getPassword()
	{
		return m_passWord;
	}
	
	/**
	 * 	Get Processor Class Name
	 *	@return ClassName for Processor
	 */
	public String getProcClassName()
	{
		return m_procClassName;
	}
	
	public boolean isStage()
	{
		return m_isStageRequest;
	}
	
}	//	RequestAnalyzer
