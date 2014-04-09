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

import java.math.*;
import javax.servlet.http.*;
import org.compiere.cm.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	AccessLogger saves any CM access for analysis
 *	
 *  @author Yves Sandfort
 *  @version $Id$
 */
public class AccessLogger extends Thread 
{
	private X_CM_WebAccessLog thisLog = null;
	
	/**
	 * 	AccessLogger constructor
	 *	@param tRequest Request
	 *	@param tServletCM Servlet of the access
	 *	@param tRequestAnalyzer Request Analyzer context
	 */
	public AccessLogger(HttpServletRequest tRequest, HttpServletCM tServletCM, RequestAnalyzer tRequestAnalyzer) 
	{
		Ctx ctx = tServletCM.getCtx();
		if (tRequestAnalyzer.getWebProject()!=null)
			ctx.setContext("#AD_Client_ID", "" + tRequestAnalyzer.getWebProject().getAD_Client_ID());
		//ctx.put()
		thisLog = new X_CM_WebAccessLog(ctx, 0, null);
		thisLog.setAD_Org_ID(0);
		// Set Access Type
		thisLog.setLogType("W");
		// Set Web Project
		if (tRequestAnalyzer.getWebProject()!=null)
			thisLog.setCM_WebProject_ID(tRequestAnalyzer.getWebProject().get_ID());
		// Set IP_Address
		thisLog.setIP_Address("217.171.192.001");
		// Set BroadcastServer
		
		// Set the RequestType
		if (tRequest.getMethod()!=null) 
			thisLog.setRequestType(tRequest.getMethod());
		// Set Page URL
		if (tRequest.getRequestURL()!=null)
			thisLog.setPageURL(tRequest.getRequestURL().toString());
		// Set Referrer
		if (tRequest.getHeader("Referer") != null)
			thisLog.setReferrer(tRequest.getHeader("Referer"));
		// Set RemoteHost
		if (tRequest.getRemoteHost()!=null)
			thisLog.setRemote_Host(tRequest.getRemoteHost());
		// set RemoteAddr
		if (tRequest.getRemoteAddr()!=null) 
			thisLog.setRemote_Addr(tRequest.getRemoteAddr());
		// set Useragent / Browser
		if (tRequest.getHeader("User-Agent")!=null)
			thisLog.setUserAgent(tRequest.getHeader("User-Agent"));
		// Set AcceptLanguage
		if (tRequest.getHeader("Accept-Language")!=null) 
			thisLog.setAcceptLanguage(tRequest.getHeader("Accept-Language"));
		// Set Websession ID
		if (tRequest.getSession()!=null) 
			thisLog.setWebSession(tRequest.getSession().getId());
		// Seth Hyphen
		
		// Set Protocol
		if (tRequest.getProtocol()!=null) 
			thisLog.setProtocol(tRequest.getProtocol());
		else
			// This is hardcoded we must fix this later!
			thisLog.setProtocol("unknown");
		// Statuscode
		if (tRequestAnalyzer.getIsRedirect())
			thisLog.setStatusCode(302);
		else 
			thisLog.setStatusCode(200);
		// Set Filesize
		if (tRequest.getContentLength()>0) {
			thisLog.setFileSize(new java.math.BigDecimal(tRequest.getContentLength()));
		}
		// Set AD_User
		if (tRequest.getSession ()!=null)
		{
			if (tRequest.getSession ().getAttribute (WebInfo.NAME)!=null) 
			{
				WebInfo wi = (WebInfo) tRequest.getSession ().getAttribute (WebInfo.NAME);
				if (wi.getUser_ID ()>0)
					thisLog.setAD_User_ID (wi.getUser_ID ());
			}
		}
		// Set CM_Media_ID

	}
	
	/**
	 * 	setFileSize set's file sizes 
	 *	@param fileSize
	 */
	public void setFileSize(BigDecimal fileSize)
	{
		thisLog.setFileSize(fileSize);
	}
	
	/**
	 * 	run currently only saves the entry
	 *	@see java.lang.Thread#run()
	 */
	@Override
	public void run() 
	{
		thisLog.save();
	}
}
