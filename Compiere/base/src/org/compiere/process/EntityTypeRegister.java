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
import java.math.*;
import java.net.*;
import java.util.logging.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.commons.httpclient.params.*;
import org.compiere.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Register Entity Type
 *	
 *  @author Jorg Janke
 *  @version $Id: EntityTypeRegister.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class EntityTypeRegister extends SvrProcess
{
	/** Register Entity Type			*/
	protected int		p_AD_EntityType_ID = 0;
	/** File Name to upload				*/
	private String		p_FileName = null;
	/** Suggested Price					*/
	private BigDecimal	p_SuggestedPrice = null;
	
	/** Stream Reader			*/
	private InputStreamReader	m_in = null;
	/** Encoding				*/
	static private final String		ENC = "UTF-8"; 
	/** URL						*/
	static private final String 	URLSTRING = "http://www.compiere.com/migrateApps/RegisterComponent";
	// static private final String URLSTRING = "http://ws-jj/migrateApps/RegisterComponent";
	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("FileName"))
				p_FileName = (String)element.getParameter();
			else if (name.equals("SuggestedPrice"))
				p_SuggestedPrice = (BigDecimal)element.getParameter();
		}
		p_AD_EntityType_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return summary
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("AD_EntityType_ID=" + p_AD_EntityType_ID
			+ ", FileName=" + p_FileName);
		MEntityType et = MEntityType.getEntityType(getCtx(), p_AD_EntityType_ID);
		if (et.isSystemMaintained())
			throw new CompiereUserException("You cannot register a System maintained entity");

		String EntityType = et.getEntityType();

		if (EntityType.toUpperCase().startsWith("C") || EntityType.toUpperCase().startsWith("X"))
			throw new CompiereUserException("Entity Types starting with C or X are reserved.");

		boolean ok = sendRegistration(et);
		if (!ok)
			throw new CompiereSystemException("Could not contact Compiere - Try later");
		
		String response = getResponse();
		if (response == null)
			throw new CompiereSystemException("No Response - Try later");
		
		String info = et.processRegistrationResponse(response);
		et.save();

		if (Util.isEmpty(p_FileName) || et.getRecord_ID() == 0)
			return info;

		info += " - " + uploadFile(et);

		return info;
	}	//	doIt

	/**
	 * Upload the deployment as attachment
	 * @param et
	 * @return
	 */
	private String uploadFile(MEntityType et)
	{
		File file = new File(p_FileName);
		if (!file.exists())
			return "File not exist: "  + p_FileName;
		
		if (file.length() <= 0)
			return "Empty file: " + p_FileName;
		
		PostMethod filePost = new PostMethod(URLSTRING);
		filePost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
		try 
		{
			MSystem system = MSystem.get(getCtx());
			
			Part[] parts = {
					new StringPart("USER", system.getUserName(), ENC),
					new StringPart("EntityType", et.getEntityType(), ENC),
					new StringPart("RecordID", String.valueOf(et.getRecord_ID()), ENC),
					new FilePart(file.getName(), file)
			};
			
			filePost.setRequestEntity(
					new MultipartRequestEntity(parts, filePost.getParams())
			);
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().
			getParams().setConnectionTimeout(5000);
			int status = client.executeMethod(filePost);
			if (status == HttpStatus.SC_OK) 
			{
				return "Upload complete, response=" + filePost.getResponseBodyAsString();
			} 
			else 
			{
				return "Upload failed, response=" + HttpStatus.getStatusText(status);
			}
		} 
		catch (Exception ex) 
		{
			log.log(Level.SEVERE, "ERROR: " + ex.getClass().getName(), ex);
		} 
		finally 
		{
			filePost.releaseConnection();
		}
		
		return "Upload complete";
	}  // uploadFile()
	
	/**
	 * 	Send Registration
	 *	@param et entity type
	 *	@return true if sent
	 */
	private boolean sendRegistration(MEntityType et)
	{
		URL url = null;
		//	Assemble request
		try
		{
			String tRelease = Compiere.MAIN_VERSION.substring(8);
			String tVersion = Compiere.DATE_VERSION;
			String from = InetAddress.getLocalHost().toString();

			StringBuffer urlString = new StringBuffer (URLSTRING);

			//	Sender
			MSystem system = MSystem.get(getCtx());
			urlString.append("?NAME=").append(URLEncoder.encode(system.getName(), ENC))
				.append("&USER=").append(URLEncoder.encode(system.getUserName(), ENC))
				.append("&PASSWORD=").append(URLEncoder.encode(system.getPassword(), ENC))
				.append("&FROM=").append(URLEncoder.encode(from, ENC));
			//
			urlString.append("&TRELEASENO=").append(URLEncoder.encode(tRelease, ENC))
				.append("&TVERSION=").append(URLEncoder.encode(tVersion, ENC));
			//
			urlString.append("&EntityType=").append(URLEncoder.encode(et.getEntityType(), ENC))
				.append("&ETName=").append(URLEncoder.encode(et.getName(), ENC));
			if (et.getRecord_ID() != 0)
				urlString.append("&RecordID=").append(et.getRecord_ID());
			//	
			if (et.getDescription() != null)
				urlString.append("&Description=").append(URLEncoder.encode(et.getDescription(), ENC));
			if (et.getHelp() != null)
				urlString.append("&Help=").append(URLEncoder.encode(et.getHelp(), ENC));
			if (et.getVersion() != null)
				urlString.append("&Version=").append(URLEncoder.encode(et.getVersion(), ENC));
			if (et.getRequireCompiereVersion() != null)
				urlString.append("&RequireCompiereVersion=").append(URLEncoder.encode(et.getRequireCompiereVersion(), ENC));
			if (et.getRequireComponentVersion() != null)
				urlString.append("&RequireComponentVersion=").append(URLEncoder.encode(et.getRequireComponentVersion(), ENC));
			if (et.getDocumentationText() != null)
				urlString.append("&DocumentationText=").append(URLEncoder.encode(et.getDocumentationText(), ENC));
			if (p_SuggestedPrice != null)
				urlString.append("&SuggestedPrice=").append(p_SuggestedPrice);
			//
			url = new URL (urlString.toString());
			log.config(url.toString());
		}
		catch (Exception e)
		{
			if (log != null)
				log.log(Level.SEVERE, "--", e);
			return false;
		}
		//	Send it
		try
		{
			URLConnection uc = url.openConnection();
			m_in = new InputStreamReader(uc.getInputStream());
		}
		catch (FileNotFoundException ex)
		{
			log.log(Level.WARNING, " Could not connect - Try later");
			return false;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "->", e);
			return false;
		}
		return true;
	}	//	sendRegistration
	
	/**
	 * 	Get Response
	 *	@return response
	 */
	private String getResponse()
	{
		if (m_in == null)
			return null;
		//
		StringBuffer sb = new StringBuffer();
		try		//	Get Summary
		{
			int c;
			while ((c = m_in.read()) != -1)
				sb.append((char)c);
			m_in.close();

			if (log != null)
				log.fine("(" + sb.length() + ") " + sb);
		}
		catch (Exception ex)
		{
			log.log(Level.WARNING, "<-", ex);
			return null;
		}
		log.config(sb.toString());
		return sb.toString();
	}	//	getResponse

	
}	//	EntityTypeRegister
