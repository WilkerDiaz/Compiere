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
package org.compiere.util;

import java.rmi.*;
import java.util.logging.*;

import org.compiere.db.*;
import org.compiere.interfaces.*;
import org.compiere.model.*;

/**
 *  Compiere Cache Management
 *
 *  @author Jorg Janke
 *  @version $Id: CacheMgt.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CacheMgt
{
	/**
	 * 	Get Cache Management
	 * 	@return Cache Mgr
	 */
	public static CacheMgt get()
	{
		if (s_cache == null)
			s_cache = new CacheMgt();
		return s_cache;
	}	//	get

	/**	Singleton					*/
	private static CacheMgt		s_cache = null;

	/**
	 *	Private Constructor
	 */
	private CacheMgt()
	{
	}	//	CacheMgt

	/**	List of Instances				*/
	private WeakArrayList<CacheInterface>	m_instances = new WeakArrayList<CacheInterface>();
	//private ArrayList<CacheInterface>	m_instances = new ArrayList<CacheInterface>();
	/** List of Table Names				*/
	//private ArrayList<String>	m_tableNames = new ArrayList<String>();
	private WeakArrayList<String>			m_tableNames = new WeakArrayList<String>();
	/** Logger							*/
	private static CLogger		log = CLogger.getCLogger(CacheMgt.class);

	
	/**************************************************************************
	 * 	Register Cache Instance
	 *	@param instance Cache
	 *	@return true if added
	 */
	public synchronized boolean register (CacheInterface instance)
	{
		if (instance == null)
			return false;
		String tableName = instance.getTableName(); 
		m_tableNames.add(tableName);
		return m_instances.add (instance);
	}	//	register

	/**
	 * 	Un-Register Cache Instance
	 *	@param instance Cache
	 *	@return true if removed
	 */
	public synchronized boolean unregister (CacheInterface instance)
	{
		if (instance == null)
			return false;
		boolean found = false;
		//	Could be included multiple times
		for (int i = m_instances.size()-1; i >= 0; i--)
		{
			CacheInterface stored = m_instances.get(i);
			if (instance.equals(stored))
			{
				m_instances.remove(i);
				found = true;
			}
		}
		return found;
	}	//	unregister

	/**************************************************************************
	 * 	Reset All registered Cache
	 * 	@return number of deleted cache entries
	 */
	public synchronized int reset()
	{
		int counter = 0;
		int total = 0;
		for (int i = 0; i < m_instances.size(); i++)
		{
			CacheInterface stored = m_instances.get(i);
			if (stored != null && stored.size() > 0)
			{
				log.info(stored.toString());
				total += stored.reset();
				counter++;
			}
		}
		MRole.reset(0);
		if (counter > 0)
			log.info("#" + counter + " (" + total + ")");
		return total;
	}	//	reset

	/**
	 * 	Reset registered Cache
	 * 	@param tableName table name
	 * 	@return number of deleted cache entries
	 */
	public synchronized int reset (String tableName)
	{
		return reset (tableName, 0);
	}	//	reset
	
	/**
	 * 	Reset registered Cache
	 * 	@param tableName table name
	 * 	@param Record_ID record if applicable or 0 for all
	 * 	@return number of deleted cache entries
	 */
	public synchronized int reset (String tableName, int Record_ID)
	{
		if (tableName == null || tableName.length() == 0)
			return reset();
	//	if (tableName.endsWith("Set"))
	//		tableName = tableName.substring(0, tableName.length()-3);
		if (!m_tableNames.contains(tableName))
			return 0;
		if (tableName.equals("AD_Role"))
			MRole.reset(Record_ID);
		//
		int counter = 0;
		int total = 0;
		for (int i = 0; i < m_instances.size(); i++)
		{
			CacheInterface stored = m_instances.get(i);
			if (stored == null)
				continue;
			int ii = stored.reset(tableName, Record_ID);
			if (ii >= 0)
				counter++;
			if (ii > 0)
				total += ii; 
		}
		if (counter > 0)
			log.fine(tableName + ": #" + counter + " (" + total + ")");
		//	Update Server
		if (DB.isRemoteObjects())
		{
			Server server = CConnection.get().getServer();
			try
			{
				if (server != null)
				{	//	See ServerBean
					int serverTotal = server.cacheReset(tableName, Record_ID); 
					if (CLogMgt.isLevelFinest())
						log.fine("Server => " + serverTotal);
				}
			}
			catch (RemoteException ex)
			{
				log.log(Level.SEVERE, "AppsServer error", ex);
			}
		}
		return total;
	}	//	reset
	
	/**
	 * 	Total Cached Elements
	 *	@return count
	 */
	public synchronized int getElementCount()
	{
		int total = 0;
		for (int i = 0; i < m_instances.size(); i++)
		{
			CacheInterface stored = m_instances.get(i);
			if (stored != null && stored.size() > 0)
			{
				log.fine(stored.toString());
				total += stored.size();
			}
		}
		return total;
	}	//	getElementCount
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("CacheMgt[");
		sb.append("Instances=")
			.append(m_instances.size())
			.append("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Extended String Representation
	 *	@return info
	 */
	public String toStringX ()
	{
		StringBuffer sb = new StringBuffer ("CacheMgt[");
		sb.append("Instances=")
			.append(m_instances.size())
			.append(", Elements=")
			.append(getElementCount())
			.append("]");
		return sb.toString ();
	}	//	toString

}	//	CCache
