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
package org.compiere.server;

import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.wf.*;

/**
 *	Compiere Server Manager
 *
 *  @author Jorg Janke
 *  @version $Id: CompiereServerMgr.java 8601 2010-04-05 21:22:02Z nnayak $
 */
public class CompiereServerMgr
{
	/**
	 * 	Get Compiere Server Manager - and start if not exists
	 *	@return mgr
	 */
	public static synchronized CompiereServerMgr get()
	{
		if (s_serverMgr == null)
		{
			//	for faster subsequent calls
			s_serverMgr = new CompiereServerMgr();
			s_serverMgr.startAll(true);
			s_serverMgr.log.info(s_serverMgr.toString());
		}
		return s_serverMgr;
	}	//	get

	/**
	 * 	Restart Servers/Processors
	 *	@return true if restarted
	 */
	public static boolean restart()
	{
		if (s_serverMgr == null)
		{
			get();
			return true;
		}
		s_serverMgr.stopAll();
		return s_serverMgr.startAll(true);
	}

	/**	Singleton					*/
	private static CompiereServerMgr	s_serverMgr = null;
	/**	Logger						*/
	protected CLogger					log = CLogger.getCLogger(getClass());

	/**************************************************************************
	 * 	Compiere Server Manager
	 */
	private CompiereServerMgr ()
	{
		super();
		startEnvironment();
	//	m_serverMgr.startServers();
	}	//	CompiereServerMgr

	/**	The Servers				*/
	private final ArrayList<CompiereServer>	m_servers = new ArrayList<CompiereServer>();
	/** Context					*/
	private final Ctx				m_ctx = Env.getCtx();
	/** Start					*/
	private final Timestamp		m_start = new Timestamp(System.currentTimeMillis());

	/**
	 * 	Start Environment
	 *	@return true if started
	 */
	private boolean startEnvironment()
	{
		Compiere.startup(false, "ServerMgr");
		log.info("");

		String Remote_Addr = null;
		String Remote_Host = null;
		try
		{
			InetAddress lh = InetAddress.getLocalHost();
			Remote_Addr = lh.getHostAddress();
			Remote_Host = lh.getHostName();
		}
		catch (UnknownHostException e)
		{
			log.log(Level.SEVERE, "No Local Host", e);
		}

		//	Set Session
		MSession session = MSession.get(getCtx(), X_AD_Session.SESSIONTYPE_ApplicationServer, true,
			Remote_Addr, Remote_Host, "Server");
		//
		return session != null;
	}	//	startEnvironment

	/**
	 * Remove interrupted threads
	 */
	private void cleanupInactiveServers() {
		CompiereServer[] inactiveServers = getInactive();
		for (CompiereServer server : inactiveServers) {
			m_servers.remove(server);
		}
	}
	/**
	 * 	Add new servers to existing ones
	 */
	private void requeryAll()
	{
		if (m_servers.size() > 0)
			log.config("Current #" + m_servers.size());
		
		cleanupInactiveServers();
		
		//	Accounting
		MAcctProcessor[] acctModels = MAcctProcessor.getActive(m_ctx);
		for (int i = 0; i < acctModels.length; i++)
		{
			MAcctProcessor pModel = acctModels[i];
			CompiereServer server = CompiereServer.create(pModel);
			addServer(server);
		}
		//	Request
		MRequestProcessor[] requestModels = MRequestProcessor.getActive(m_ctx);
		for (int i = 0; i < requestModels.length; i++)
		{
			MRequestProcessor pModel = requestModels[i];
			CompiereServer server = CompiereServer.create(pModel);
			addServer(server);
		}
		//	Workflow
		MWorkflowProcessor[] workflowModels = MWorkflowProcessor.getActive(m_ctx);
		for (int i = 0; i < workflowModels.length; i++)
		{
			MWorkflowProcessor pModel = workflowModels[i];
			CompiereServer server = CompiereServer.create(pModel);
			addServer(server);
		}
		//	Alert
		MAlertProcessor[] alertModels = MAlertProcessor.getActive(m_ctx);
		for (int i = 0; i < alertModels.length; i++)
		{
			MAlertProcessor pModel = alertModels[i];
			CompiereServer server = CompiereServer.create(pModel);
			addServer(server);
		}
		//	Scheduler
		MScheduler[] schedulerModels = MScheduler.getActive(m_ctx);
		for (int i = 0; i < schedulerModels.length; i++)
		{
			MScheduler pModel = schedulerModels[i];
			CompiereServer server = CompiereServer.create(pModel);
			addServer(server);
		}
		//	LDAP
		MLdapProcessor[] ldapModels = MLdapProcessor.getActive(m_ctx);
		for (int i = 0; i < ldapModels.length; i++)
		{
			MLdapProcessor lp = ldapModels[i];
			CompiereServer server = CompiereServer.create(lp);
			addServer(server);
		}
		log.config("#" + m_servers.size());
	}	//	requeryAll

	/**
	 * 	Add New Server to Server List and start it if it does not exist
	 *	@param server new server
	 *	@return true if added and started
	 */
	private boolean addServer(CompiereServer server)
	{
		if (m_servers.contains(server))
			return false;

		m_servers.add(server);
		server.setPriority(Thread.NORM_PRIORITY-2);
		server.start();
		return true;
	}	//	addServer

	/**
	 * 	Get Server Context
	 *	@return ctx
	 */
	public Ctx getCtx()
	{
		return m_ctx;
	}	//	getCtx

	/**
	 * 	Start all servers
	 * 	@param requery requery all servers
	 *	@return true if started
	 */
	public synchronized boolean startAll(boolean requery)
	{
		log.info ("");
		
		if (requery) 
			requeryAll();
		for (CompiereServer server : m_servers)
		{
			try
			{
				if (server.isAlive())
					continue;
				//	Wait until dead
				if (server.isInterrupted())
				{
					int maxWait = 10;	//	10 iterations = 1 sec
					while (server.isAlive())
					{
						if (maxWait-- == 0)
						{
							log.severe ("Wait timeout for interrupted " + server);
							break;
						}
						try
						{
							Thread.sleep(100);		//	1/10 sec
						}
						catch (InterruptedException e)
						{
							log.log(Level.SEVERE, "While sleeping", e);
						}
					}
				}
				//	Do start
				if (!server.isAlive())
				{
					//	replace
					//server = CompiereServer.create (server.getModel());
					server.start();
					server.setPriority(Thread.NORM_PRIORITY-2);
				}
			}
			catch (Exception e)
			{
				log.log(Level.WARNING, "Server: " + server, e);
			}
		}	//	for all servers

		//	Final Check
		int noRunning = 0;
		int noStopped = 0;
		for (CompiereServer server : m_servers)
		{
			try
			{
				if (server.isAlive())
				{
					log.info("Alive: " + server);
					noRunning++;
				}
				else
				{
					log.warning("Dead: " + server);
					noStopped++;
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "(checking) - " + server, e);
				noStopped++;
			}
		}
		log.info("Running=" + noRunning + ", Stopped=" + noStopped);
		CompiereServerGroup.get().dump();
		return noStopped == 0;
	}	//	startAll

	/**
	 * 	Start Server if not started yet
	 * 	@param serverID server ID
	 *	@return true if started
	 */
	public boolean start (String serverID)
	{
		CompiereServer server = getServer(serverID);
		if (server == null)
			return false;
		if (server.isAlive())
			return true;

		try
		{
			//	replace
			int index = m_servers.indexOf(server);
			server = CompiereServer.create (server.getModel());
			if (server == null)
				m_servers.remove(index);
			else
				m_servers.set(index, server);
			server.start();
			server.setPriority(Thread.NORM_PRIORITY-2);
			Thread.yield();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Server=" + serverID, e);
			return false;
		}
		log.info(server.toString());
		CompiereServerGroup.get().dump();
		if (server == null)
			return false;
		return server.isAlive();
	}	//	startIt

	/**
	 * 	Stop all Servers
	 *	@return true if stopped
	 */
	public boolean stopAll()
	{
		log.info ("");
		CompiereServer[] servers = getActive();
		//	Interrupt
		for (int i = 0; i < servers.length; i++)
		{
			CompiereServer server = servers[i];
			try
			{
				if (server.isAlive() && !server.isInterrupted())
				{
					server.setPriority(Thread.MAX_PRIORITY-1);
					server.interrupt();
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "(interrupting) - " + server, e);
			}
		}	//	for all servers
		Thread.yield();

		//	Wait for death
		for (int i = 0; i < servers.length; i++)
		{
			CompiereServer server = servers[i];
			try
			{
				int maxWait = 10;	//	10 iterations = 1 sec
				while (server.isAlive())
				{
					if (maxWait-- == 0)
					{
						log.severe ("Wait timeout for interrupted " + server);
						break;
					}
					Thread.sleep(100);		//	1/10
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "(waiting) - " + server, e);
			}
		}	//	for all servers

		//	Final Check
		int noRunning = 0;
		int noStopped = 0;
		for (int i = 0; i < servers.length; i++)
		{
			CompiereServer server = servers[i];
			try
			{
				if (server.isAlive())
				{
					log.warning ("Alive: " + server);
					noRunning++;
				}
				else
				{
					log.info ("Stopped: " + server);
					noStopped++;
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "(checking) - " + server, e);
				noRunning++;
			}
		}
		log.fine("Running=" + noRunning + ", Stopped=" + noStopped);
		CompiereServerGroup.get().dump();
		return noRunning == 0;
	}	//	stopAll

	/**
	 * 	Stop Server if not stopped
	 * 	@param serverID server ID
	 *	@return true if interrupted
	 */
	public boolean stop (String serverID)
	{
		CompiereServer server = getServer(serverID);
		if (server == null)
			return false;
		if (!server.isAlive())
			return true;

		try
		{
			server.interrupt();
			Thread.sleep(10);	//	1/100 sec
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "stop", e);
			return false;
		}
		log.info(server.toString());
		CompiereServerGroup.get().dump();
		return !server.isAlive();
	}	//	stop


	/**
	 * 	Destroy
	 */
	public void destroy ()
	{
		log.info ("");
		stopAll();
		m_servers.clear();
	}	//	destroy

	/**
	 * 	Get Active Servers
	 *	@return array of active servers
	 */
	protected CompiereServer[] getActive()
	{
		ArrayList<CompiereServer> list = new ArrayList<CompiereServer>();
		for (int i = 0; i < m_servers.size(); i++)
		{
			CompiereServer server = m_servers.get(i);
			if (server != null && server.isAlive() && !server.isInterrupted())
				list.add (server);
		}
		CompiereServer[] retValue = new CompiereServer[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getActive
	
	/**
	 * 	Get Active Servers
	 *	@return array of active servers
	 */
	protected CompiereServer[] getInactive()
	{
		ArrayList<CompiereServer> list = new ArrayList<CompiereServer>();
		for (int i = 0; i < m_servers.size(); i++)
		{
			CompiereServer server = m_servers.get(i);
			if (server != null && (!server.isAlive() || server.isInterrupted()))
				list.add (server);
		}
		CompiereServer[] retValue = new CompiereServer[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getActive

	/**
	 * 	Get all Servers
	 *	@return array of servers
	 */
	public ArrayList<CompiereServer> getAll()
	{
		return m_servers;
	}	//	getAll

	/**
	 * 	Get Server with ID
	 *	@param serverID server id
	 *	@return server or null
	 */
	public CompiereServer getServer (String serverID)
	{
		if (serverID == null)
			return null;
		for (int i = 0; i < m_servers.size(); i++)
		{
			CompiereServer server = m_servers.get(i);
			if (serverID.equals(server.getServerID()))
				return server;
		}
		return null;
	}	//	getServer

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("CompiereServerMgr[");
		sb.append("Servers=").append(m_servers.size())
			.append(",ContextSize=").append(m_ctx.size())
			.append(",Started=").append(m_start)
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Description
	 *	@return description
	 */
	public String getDescription()
	{
		return "$Revision: 1.4 $";
	}	//	getDescription

	/**
	 * 	Get Number Servers
	 *	@return no of servers
	 */
	public String getServerCount()
	{
		int noRunning = 0;
		int noStopped = 0;
		for (int i = 0; i < m_servers.size(); i++)
		{
			CompiereServer server = m_servers.get(i);
			if (server.isAlive())
				noRunning++;
			else
				noStopped++;
		}
		String info = String.valueOf(m_servers.size())
			+ " - Running=" + noRunning
			+ " - Stopped=" + noStopped;
		return info;
	}	//	getServerCount

	/**
	 * 	Get start date
	 *	@return start date
	 */
	public Timestamp getStartTime()
	{
		return m_start;
	}	//	getStartTime

}	//	CompiereServerMgr
