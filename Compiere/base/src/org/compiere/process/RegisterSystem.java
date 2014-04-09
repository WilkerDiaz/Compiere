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
package org.compiere.process;

import java.io.*;
import java.net.*;
import javax.net.ssl.*;

import java.security.*;
import java.util.logging.*;

import org.compiere.common.CompiereStateException;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	System Registration
 *	
 *  @author Jorg Janke
 *  @version $Id: RegisterSystem.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class RegisterSystem extends SvrProcess
{
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	DoIt
	 *	@return Message
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		int AD_Registration_ID = getRecord_ID();
		log.info("doIt - AD_Registration_ID=" + AD_Registration_ID);
		MSystem sys = MSystem.get(getCtx());
		M_Registration reg = new M_Registration (getCtx(), AD_Registration_ID, get_TrxName());

		//	Create Query String
		String enc = WebEnv.ENCODING;
		
		StringBuffer urlString = 
			new StringBuffer ("https://www.compiere.com/wstore/registrationServlet?");
		
		urlString.append("Name=").append(URLEncoder.encode(sys.getName(), enc));
		urlString.append("&UserName=").append(URLEncoder.encode(sys.getUserName(), enc));
		urlString.append("&Password=").append(URLEncoder.encode(sys.getPassword(), enc));
		urlString.append("&IsRegistered=").append(reg.isRegistered() ? "Y" : "N");
		urlString.append("&Record_ID=").append(URLEncoder.encode(String.valueOf(reg.getRecord_ID()), enc));
		urlString.append("&FirstName=").append(URLEncoder.encode(reg.getFirstName(), enc));				
		urlString.append("&LastName=").append(URLEncoder.encode(reg.getLastName(), enc));				
		urlString.append("&Email=").append(URLEncoder.encode(reg.getEMail(), enc));				
		String phone = reg.getPhone();
		if (phone!=null && phone.length()>0)
			urlString.append("&Phone=").append(URLEncoder.encode(phone, enc));				
		urlString.append("&Company=").append(URLEncoder.encode(reg.getCompany(), enc));
		urlString.append("&Employees=").append(URLEncoder.encode(reg.getEmployeeRange(), enc));
		urlString.append("&Industry=").append(URLEncoder.encode(reg.getIndustry(), enc));
		urlString.append("&C_Country_ID=").append(URLEncoder.encode(String.valueOf(reg.getC_Country_ID()), enc));
		String URL = reg.getURL();
		if (URL!=null && URL.length()>0)
			urlString.append("&URL=").append(URLEncoder.encode(URL, enc));				
		urlString.append("&SalesVolume=").append(URLEncoder.encode(String.valueOf(reg.getSalesVolume()), enc));
		urlString.append("&C_Currency_ID=").append(URLEncoder.encode(String.valueOf(reg.getC_Currency_ID()), enc));
		urlString.append("&OptIn=").append(URLEncoder.encode(reg.isOptIn()?"Y":"N", enc));
		
		//	Statistics
		if (sys.isAllowStatistics())
		{
			urlString.append("&NumClient=").append(URLEncoder.encode(String.valueOf(
					QueryUtil.getSQLValue(null, "SELECT Count(*) FROM AD_Client")), enc))
				.append("&NumOrg=").append(URLEncoder.encode(String.valueOf(
					QueryUtil.getSQLValue(null, "SELECT Count(*) FROM AD_Org")), enc))
				.append("&NumBPartner=").append(URLEncoder.encode(String.valueOf(
					QueryUtil.getSQLValue(null, "SELECT Count(*) FROM C_BPartner")), enc))
				.append("&NumUser=").append(URLEncoder.encode(String.valueOf(
					QueryUtil.getSQLValue(null, "SELECT Count(*) FROM AD_User")), enc))
				.append("&NumProduct=").append(URLEncoder.encode(String.valueOf(
					QueryUtil.getSQLValue(null, "SELECT Count(*) FROM M_Product")), enc))
				.append("&NumInvoice=").append(URLEncoder.encode(String.valueOf(
					QueryUtil.getSQLValue(null, "SELECT Count(*) FROM C_Invoice")), enc));
		}
		log.fine(urlString.toString());
		
		//	Create a trust manager that does not validate certificate chains
	    TrustManager[] trustAllCerts = new TrustManager[]{
	        new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	            public void checkClientTrusted(
	                java.security.cert.X509Certificate[] certs, String authType) {
	            }
	            public void checkServerTrusted(
	                java.security.cert.X509Certificate[] certs, String authType) {
	            }
	        }
	    };
	    
	    // Install the all-trusting trust manager
	    try {
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	    } 
	    catch (NoSuchAlgorithmException e) {
	    }
	    catch (KeyManagementException e) {
	    }	    
	    // Now you can access an https URL without having the certificate in the truststore

		//	Send it
	    URL url = new URL (urlString.toString());
		StringBuffer sb = new StringBuffer();
		try
		{
			URLConnection uc = url.openConnection();
			InputStreamReader in = new InputStreamReader(uc.getInputStream());
			int c;
			while ((c = in.read()) != -1)
				sb.append((char)c);
			in.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Connect - " + e.toString());
			throw new CompiereStateException("Cannot connect to Server - Please try later");
		}
		//
		String info = sb.toString();
		log.info("Response=" + info);
		//	Record at the end
		int index = sb.indexOf("Record_ID=");
		if (index != -1)
		{
			try
			{
				int Record_ID = Integer.parseInt(sb.substring(index+10));
				reg.setRecord_ID(Record_ID);
				reg.setIsRegistered(true);
				reg.save();
				//
				info = info.substring(0, index);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "Record - ", e);
			}
		}
		
		return info;
	}	//	doIt

}	//	RegisterSystem
