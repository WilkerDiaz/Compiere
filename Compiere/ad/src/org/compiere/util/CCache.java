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

import java.util.*;
import java.util.concurrent.*;

import org.compiere.framework.*;

import com.google.common.base.ReferenceType;
import com.google.common.collect.ReferenceMap;

/**
 * Compiere SoftReference Cache.
 *
 * The items held by this cache can be reclaimed by the garbage collector without warning, so make
 * sure that your code can handle that situation gracefully.
 *
 * @param <K> Key
 * @param <V> Value
 *
 * @author Jorg Janke, gwu
 * @version $Id: CCache.java 8729 2010-05-06 19:17:50Z nnayak $
 */
public class CCache<K,V> implements CacheInterface	//, Map<K, V>
{

	/**  */
    private static final long serialVersionUID = 7485872632348157628L;

	/**
	 * 	Compiere Cache - expires after 2 hours with 20 initial Capacity
	 * 	@param tableName table name of the cache
	 */
	public CCache (String tableName)
	{
		this (tableName, 20, 120, false, null);
	}	//	CCache

	/**
	 * 	Compiere Cache - expires after 2 hours
	 * 	@param tableName (table) name of the cache
	 * 	@param initialCapacity initial capacity
	 */
	public CCache (String tableName, int initialCapacity)
	{
		this (tableName, initialCapacity, 120, false, null);
	}	//	CCache

	public CCache (String tableName, int initialCapacity, String alternativeName)
	{
		this (tableName, initialCapacity, 120, false, alternativeName);
	}	//	CCache
	
	/**
	 * 	Compiere Cache
	 * 	@param tableName table name of the cache
	 * 	@param initialCapacity initial capacity
	 * 	@param expireMinutes expire after minutes (0=no expire)
	 */
	public CCache (String tableName, int initialCapacity, int expireMinutes)
	{
		this (tableName, initialCapacity, expireMinutes, false, null);
	}	//	CCache

	/**
	 * 	Compiere Cache
	 * 	@param tableName table name of the cache
	 * 	@param initialCapacity initial capacity
	 * 	@param expireMinutes expire after minutes (0=no expire)
	 * 	@param resetAll reset entire cash when one value changed (default: false)
	 */
	public CCache (String tableName, int initialCapacity, int expireMinutes, boolean resetAll)
	{
		this (tableName, initialCapacity, expireMinutes, resetAll, null);
	}	//	CCache

	/**
	 * 	Compiere Cache
	 * 	@param tableName table name of the cache
	 * 	@param initialCapacity initial capacity
	 * 	@param expireMinutes expire after minutes (0=no expire)
	 * 	@param resetAll reset entire cash when one value changed (default: false)
	 * 	@param tableName2 optional alternative table name
	 */
	public CCache (String tableName, int initialCapacity, int expireMinutes,
		boolean resetAll, String tableName2)
	{
		this(tableName, initialCapacity, expireMinutes, resetAll, ReferenceType.SOFT, tableName2);
	}	//	CCache

	protected CCache (String tableName, int initialCapacity, int expireMinutes,
			boolean resetAll, ReferenceType type, String tableName2)
		{
			m_cache = new ReferenceMap <K, V>(ReferenceType.STRONG, type, new ConcurrentHashMap<Object, Object>(initialCapacity));
			m_tableName = tableName;
			setExpireMinutes(expireMinutes);
			m_resetAll = resetAll;
			m_tableName2 = tableName2;
			CacheMgt.get().register(this);
		}	//	CCache

	/** The Cache					*/
    private final ReferenceMap<K, V> m_cache;
	/**	Table Name					*/
	private final String			m_tableName;
	/** Reset All Records			*/
	private boolean				m_resetAll = false;
	/**	Alternative Table Name		*/
	private String				m_tableName2 = null;
	/** Expire after minutes		*/
	private int					m_expire = 0;
	/** Time						*/
	private volatile long		m_timeExp = 0;

	/**
	 * 	Get Table Name
	 *	@return name
	 */
	public String getTableName()
	{
		return m_tableName;
	}	//	getTableName

	/**
	 * 	Set Expire Minutes and start it
	 *  Need to base calculation on current time to prevent race conditions
	 *	@param expireMinutes minutes or 0
	 */
	public void setExpireMinutes (int expireMinutes)
	{
		if (expireMinutes > 0)
		{
			m_expire = expireMinutes;
			long addMS = 60000L * m_expire;
			m_timeExp = System.currentTimeMillis() + addMS;
		}
		else
		{
			m_expire = 0;
			m_timeExp = 0;
		}
	}	//	setExpireMinutes

	/**
	 * 	Get Expire Minutes
	 *	@return expire minutes
	 */
	public int getExpireMinutes()
	{
		return m_expire;
	}	//	getExpireMinutes

	/**
	 * 	Reset All Records if one changes
	 *	@param resetAll true if reset all
	 */
	public void setResetAll(boolean resetAll)
	{
		m_resetAll = resetAll;
	}	//	setResetAll

	/**
	 * 	Reset All Records if one changes
	 *	@return true if reset all
	 */
	public boolean isResetAll()
	{
		return m_resetAll;
	}	//	isResetAll

	/**
	 * 	Set optional additional Table Name
	 *	@param tableName2 optional table name
	 */
	public void setTableName2(String tableName2)
	{
		m_tableName2 = tableName2;
	}	//	setTableName2

	/**
	 * 	Get optional additional Table Name
	 *	@return table name or null
	 */
	public String getTableName2()
	{
		return m_tableName2;
	}	//	getTableName2



	/**
	 *	Reset Cache unconditionally
	 *  Subject to race conditions and hence not completely reliable
	 * 	@return number of items cleared
	 *	@see org.compiere.util.CacheInterface#reset()
	 */
	public int reset()
	{
		int no = m_cache.size();
		//	Clear
		m_cache.clear();
		if (m_expire != 0)
		{
			long addMS = 60000L * m_expire;
			m_timeExp = System.currentTimeMillis() + addMS;
		}
		return no;
	}	//	reset

	/**
	 *	Reset all Cache if applies
	 *	@param tableName table name
	 * 	@return number of items cleared or -1 if n/a
	 *	@see org.compiere.util.CacheInterface#reset()
	 */
	public int reset(String tableName)
	{
		return reset(tableName, 0);
	}	//	reset

	/**
	 *	Reset Cache if applies
	 *	@param tableName table name
	 *	@param record_ID record id or 0 for all
	 * 	@return number of items cleared or -1 if n/a
	 *	@see org.compiere.util.CacheInterface#reset()
	 */
	public int reset(String tableName, int record_ID)
	{
		if (tableName == null)
			return -1;
		boolean exact = tableName.equals(m_tableName)
			|| tableName.equals(m_tableName2);
		if (exact
			|| m_tableName.startsWith(tableName))	//	include Child tables
		{
			if ((record_ID == 0) || m_resetAll)
				return reset();
			Integer key = Integer.valueOf(record_ID);	//	 might be wrong key
			if (m_cache.remove(key) != null)
				return 1;
			return 0;
		}
		return -1;
	}	//	reset

	/**
	 * 	Expire Cache if enabled
	 */
	private void expire()
	{
		if ((m_expire != 0) && (m_timeExp < System.currentTimeMillis()))
		{
		//	System.out.println ("------------ Expired: " + getName() + " --------------------");
			reset();
		}
	}	//	expire

	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("CCache[")
								.append(m_tableName)
								.append(",Exp=").append(m_expire)
								.append(",ResetAll=").append(m_resetAll);
		if (!Util.isEmpty(m_tableName2))
			sb.append(",tableName2=").append(m_tableName2);
		sb.append(", #").append(m_cache.size()).append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Contains entity with key
	 * 	@param key the key
	 * 	@return true if exists in cache
	 */
	public boolean containsKey(Object key)
	{
		expire();
		return m_cache.containsKey(key);
	}	//	containsKey

	/**
	 * 	Get cached object
	 * 	@param ctx context (only used when the cached object is a PO - otherwise ignored)
	 * 	@param key the key value
	 * 	@return cached value or if PO and ctx provided, the clone of a PO
	 *	@see java.util.Map#get(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
    public V get(Ctx ctx, Object key)
	{
		expire();
			V v = m_cache.get(key);
			if ((ctx != null) && (v instanceof PO))
				return (V)PO.copy(ctx, (PO)v, null);
			return v;
	}	//	get

	/**
	 * 	Put value
	 *	@param key key
	 *	@param value value
	 *	@return previous value
	 */
	public V put (K key, V value)
	{
		expire();
		return m_cache.put(key, value);
	}	// put

	/**
	 * 	Put value
	 *	@param key key
	 *	@param value value
	 *	@return previous value
	 */
	public V putIfAbsent (K key, V value)
	{
		expire();
		return m_cache.putIfAbsent(key, value);
	}	// put

	/**
	 *	@see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		expire();
		return m_cache.isEmpty();
	}	// isEmpty

	/**
	 *	@see java.util.Map#keySet()
	 */
	public Set<K> keySet()
	{
		expire();
		return m_cache.keySet();
	}	//	keySet

	/**
	 *	@see java.util.Map#size()
	 *	@return size
	 */
	public int size()
	{
		expire();
		return m_cache.size();
	}	//	size

	/**
	 *	@see java.util.Map#values()
	 *	@return the values
	 */
	public Collection<V> values()
	{
		expire();
		return m_cache.values();
	}	//	values

	/**
	 * 	@param key the key
	 *	@see java.util.Map#remove(K key)
	 */
	public void remove(K key)
	{
		m_cache.remove(key);
	}

}	//	CCache
