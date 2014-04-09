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
package org.compiere.ldap;

import java.net.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.*;
import org.compiere.model.*;
import org.compiere.server.*;
import org.compiere.util.*;

/**
 * 	LDAP Server
 *	
 *  @author Jorg Janke
 *  @version $Id: LdapProcessor.java 7514 2009-04-20 21:57:40Z freyes $
 */
public class LdapProcessor extends CompiereServer
{
	/**
	 * 	Ldap Processor (Server)
	 *	@param model Ldap Model
	 */
	public LdapProcessor (MLdapProcessor model)
	{
		super (model, 300);
		m_model = model;
	}	//	LdapProcessor

	/**	The Concrete Model			*/
	private MLdapProcessor		m_model = null;
	/**	Last Summary				*/
	private StringBuffer 		m_summary = new StringBuffer();
	/** Server Socket				*/
	private ServerSocket 		m_serverSocket = null;
	/** Counter						*/
	private int					m_counter = 0;

	
	/**
	 * 	Do Work
	 */
	@Override
	protected void doWork()
	{
		//	Close Socket
		if (m_serverSocket != null)
		{
			try
			{
				m_serverSocket.close();
			}
			catch (Exception e)
			{
			}
		}
		m_counter = 0;
		//
		m_summary = new StringBuffer(m_model.toString())
			.append(" - ");
		//
		
		try
		{
			m_serverSocket = new ServerSocket(m_model.getLdapPort());
			log.log(Level.INFO, "Opened " + m_serverSocket.toString());
			while (!isInterrupted())
			{
				Socket socket = m_serverSocket.accept();	//	waits for connection
				log.log(Level.FINE, "Connection on: " + m_serverSocket.toString() + " - from: " + socket.toString());
				LdapConnectionHandler handler = 
					new LdapConnectionHandler (socket, m_model);
				handler.start();
				m_counter++;
			}
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "Port=" + m_model.getLdapPort(), e);
			m_summary.append(e.toString());
		}
		
		m_summary.append ("; ")
			.append (m_model.getInfo());
		
		int no = m_model.deleteLog();
		m_summary.append("; Logs deleted=").append(no);
		//
		MLdapProcessorLog pLog = new MLdapProcessorLog(m_model, m_summary.toString());
		pLog.setReference("#" + String.valueOf(p_runCount) 
			+ " - " + TimeUtil.formatElapsed(new Timestamp(p_startWork)));
		pLog.save();
	}	//	doWork

	
	/**
	 * 	Get Server Info
	 *	@return info
	 */
	@Override
	public String getServerInfo()
	{
		return "#" + p_runCount + " - Last=" + m_summary.toString() 
			+ "; Counter=" + m_counter
			+ "; " + m_model.getInfo();
	}	//	getServerInfo

	/**
	 * 	Test
	 *	@param args
	 */
	public static void main(String[] args)
	{
		Compiere.startup(true, false, "LdapProcessor");
		new LdapProcessor(new MLdapProcessor(new Ctx(), 0, null)).doWork();
	}	//	main
	
}	//	LdapProcessor

