/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.compiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@compiere.org
 *****************************************************************************/
package org.compiere.util;

import java.util.*;

import org.compiere.common.constants.*;


/**
 *	Typed Context
 *
 *  @author Jorg Janke
 *  @version $Id: CContext.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CContext extends Ctx
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Context
	 */
	public CContext()
	{
		super();
	}	//	Context

	/**
	 * 	Context
	 *	@param map map
	 */
	public CContext(Map<String,String> map)
	{
		super(map);
	}	//	Context

	/**
	 * 	Context
	 *	@param stringRepresentation
	 */
	public CContext(String stringRepresentation)
	{
		super(stringRepresentation);
	}	//	Context

	/**
	 * 	Context
	 *	@param set set of Map entries with key and value
	 */
	public CContext(Set<Map.Entry<String,String>> set)
	{
		super(set);
	}	//	Context

	/** Windows with windowNo and Map		*/
	private final Map<Integer, Map<String,String>> m_windows = new HashMap<Integer, Map<String,String>>();

	/**
	 * 	Get Map with spaces as null
	 *	@param windowNo window no
	 *	@return map
	 */
	@Override
	public Map<String,String> getMap (int windowNo)
	{
		return getMap (windowNo, false);
	}	//	getMap

	/**
	 * 	Get Context for Window as Map
	 *	@param windowNo window
	 *	@return map
	 */
	@Override
	public Map<String,String> getMap (int windowNo, boolean convertNullToEmptyString)
	{
		Map<String,String> map = m_windows.get(windowNo);
		if (!convertNullToEmptyString)
			return map;
		//
		Map<String,String> newMap = new HashMap<String,String>();
		Set<Entry<String,String>> set = map.entrySet();
		Iterator<Entry<String,String>> it = set.iterator();
		while (it.hasNext())
		{
			Map.Entry<String,String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			if (convertNullToEmptyString && (value == null))
				newMap.put(key, "");
			else
				newMap.put(key, value);
		}
		return newMap;
	}	//	getMap

	/**
	 *	Remove context for Window (i.e. delete it)
	 *  @param WindowNo window
	 */
	@Override
	public void removeWindow (int windowNo)
	{
		Map<String,String> removed = m_windows.remove(windowNo);
		if (removed != null)
			log.fine("#" + windowNo + ": Removed=" + removed.size() + " - Remaining Windows=" + m_windows.size());
		else
			log.fine("#" + windowNo + ": not found - Remaining Windows=" + m_windows.size());
		super.removeWindow(windowNo);
	}	//	removeWindow

	/**
	 * 	Remove all Windows
	 */
	public void removeAllWindows()
	{
		m_windows.clear();
	}
	/**
	 * 	Add Window Context
	 *	@param windowNo window
	 *	@param map map
	 */
	public void addWindow (int windowNo, Map<String,String> map)
	{
		Set<Map.Entry<String, String>> set = map.entrySet();
		Iterator<Entry<String, String>> it = set.iterator();
		while (it.hasNext())
		{
			Map.Entry<String,String> entry = it.next();
			String key = entry.getKey().toString();
			int index = key.indexOf('|');
			/*if (key.indexOf('|') != -1)
			{
				int wNo = Integer.parseInt(key.substring(0,index));
				key = key.substring(index + 1);
				Object value = entry.getValue();
				String newValue = Null.NULLString;
				if (value != null)
					newValue = value.toString();
				setContext(wNo, key, newValue);
			}
			*/
			if(index != -1) {
				m_map.put(key, entry.getValue());
				it.remove();

			}
		}
		m_windows.put(windowNo, map);
	}	//	addWindow

	/**
	 *	Clear values
	 */
	@Override
	public void clear()
	{
		super.clear();
		m_windows.clear();
	}	//	clear

	/**
	 * 	Get Entire Context as String Array
	 * 	@return string array
	 */
	@Override
	public String[] getEntireContext()
	{
		Map<String,String> map = getMap();
		ArrayList<String> sList = new ArrayList<String>(size());
		Iterator<?> keyIterator = map.keySet().iterator();
		while (keyIterator.hasNext())
		{
			Object key = keyIterator.next();
			String s = key + " == " + map.get(key);
			sList.add(s);
		}
		String[] retValue = new String[sList.size()];
		sList.toArray(retValue);
		Arrays.sort(retValue);
		return retValue;
	}	//	getEntireContext

	/**
	 * 	Contains Key
	 *	@param key key
	 *	@return true if contains key
	 */
	public boolean containsKey(String key)
	{
		if (super.containsKey(key))
			return true;
		if (key == null)
			return false;
		Iterator<Entry<Integer, Map<String,String>>> itWin = m_windows.entrySet().iterator();
		while (itWin.hasNext())
		{
			Entry<Integer, Map<String,String>> entryWin = itWin.next();
			int windowNo = entryWin.getKey();
			if (!key.startsWith(String.valueOf(windowNo)))
				continue;
			Iterator<Entry<String,String>> itEntries = entryWin.getValue().entrySet().iterator();
			while (itEntries.hasNext())
			{
				Entry<String,String> entry = itEntries.next();
				if (key.equals(entry.getKey()))
					return true;
			}
		}
		return false;
	}	//	containsKey

	/**
	 * 	Contains Value
	 *	@param value
	 *	@return
	 */
	@Override
	public boolean containsValue(Object value)
	{
		if (super.containsValue(value))
			return true;
		if (value == null)
			return false;
		Iterator<Entry<Integer, Map<String,String>>> itWin = m_windows.entrySet().iterator();
		while (itWin.hasNext())
		{
			Entry<Integer, Map<String,String>> entryWin = itWin.next();
			Iterator<Entry<String,String>> itEntries = entryWin.getValue().entrySet().iterator();
			while (itEntries.hasNext())
			{
				Entry<String,String> entry = itEntries.next();
				if (value.equals(entry.getValue()))
					return true;
			}
		}
		return false;
	}	//	containsValue

	/**
	 * 	Entry Set of all windows
	 *	@return entry set
	 */
	@Override
	protected Map<String,String> getMap()
	{
		HashMap<String,String> map = new HashMap<String,String>(super.getMap());
		Iterator<Entry<Integer, Map<String,String>>> itWin = m_windows.entrySet().iterator();
		while (itWin.hasNext())
		{
			Entry<Integer, Map<String,String>> entryWin = itWin.next();
			int windowNo = entryWin.getKey();
		//	log.info("Adding #" + windowNo);
			Iterator<Entry<String,String>> itEntries = entryWin.getValue().entrySet().iterator();
			while (itEntries.hasNext())
			{
				Entry<String,String> entry = itEntries.next();
				map.put(windowNo + "|" + entry.getKey(), entry.getValue());
			}
		}
		return map;
	}	//	getMap

	/**
	 * 	Entry Set of all windows
	 *	@return entry set
	 */
	@Override
	public Set<Map.Entry<String,String>> entrySet()
	{
		return getMap().entrySet();
	}	//	entrySet

	/**
	 * 	Is Empty
	 *	@return true if all empty
	 */
	@Override
	public boolean isEmpty()
	{
		boolean empty = super.isEmpty();
		if (!empty)
			return false;
		Iterator<Entry<Integer, Map<String,String>>> itWin = m_windows.entrySet().iterator();
		while (itWin.hasNext())
		{
			Entry<Integer, Map<String,String>> entryWin = itWin.next();
			Set<Entry<String,String>> itEntries = entryWin.getValue().entrySet();
			if (!itEntries.isEmpty())
				return false;
		}
		return true;
	}	//	isEmpty

	/**
	 *	Key Set
	 *	@return
	 */
	@Override
	public Set<String> keySet()
	{
		return getMap().keySet();
	}	//	keySet

	/**
	 * 	Values
	 *	@return all values
	 */
	@Override
	public Collection<String> values()
	{
		return getMap().values();
	}	//	values


	/**************************************************************************
	 *	Set Context for Window to Value
	 *  @param windowNo window no
	 *  @param context context key
	 *  @param value context value
	 */
	@Override
	public void setContext (int windowNo, String context, String value)
	{
		if (context == null)
			return;
		if ((windowNo != EnvConstants.WINDOW_FIND)
			&& (windowNo != EnvConstants.WINDOW_MLOOKUP)
			&& (windowNo < EnvConstants.WINDOW_TEMP))
			log.finer("("+windowNo+"): " + context + "=" + value);
		//
		Map<String,String> map = m_windows.get(windowNo);
		if (map == null)
		{
			map = new HashMap<String,String>();
			m_windows.put(windowNo, map);
		}
		if ((value == null) || value.equals(""))
			map.put(context, null);
		//	map.remove(context);
		else
			map.put(context, value);
	}	//	setContext

	/**
	 *	Get Value of Context for Window.
	 *	if not found global context if available and enabled
	 *  @param windowNo window
	 *  @param context context key
	 *  @param  onlyWindow  if true, no defaults are used unless explicitly asked for
	 *  @return value or ""
	 */
	@Override
	public String getContext (int windowNo, String context, boolean onlyWindow)
	{
		if (context == null)
			throw new IllegalArgumentException ("Require Context");
		Map<String,String> map = m_windows.get(windowNo);
		if (map != null)
		{
			if (!map.containsKey(context))
			{
				//	Explicit Base Values
				if (context.startsWith("#") || context.startsWith("$"))
					return getContext(context);
				if (onlyWindow)			//	no Default values
					return "";
				return getContext("#" + context);
			}
			String value = map.get(context);
			if(value == null)
				return "";
			return value;
		}
		else
		{
			if (context.startsWith("#") || context.startsWith("$"))
				return getContext(context);
			if (!onlyWindow)
				return getContext("#" + context);
		}
		return "";
	}	//	getContext

	/**
	 * 	Add Map elements to context
	 *	@param WindowNo window
	 * 	@param addContext new context
	 */
	@Override
	public void setContext (int windowNo, Map<String,String> addContext)
	{
		if (addContext == null)
			return;
		Map<String,String> map = m_windows.get(windowNo);
		if (map == null)
		{
			map = new HashMap<String,String>();
			m_windows.put(windowNo, map);
		}

		//ZD, to keep the semantics right, remove keys that has "" or null values
		Iterator<String> it = addContext.keySet().iterator();
		while(it.hasNext())
		{
			String key = it.next();
			String value = addContext.get(key);
			if ((value != null) && (value.length()>0))
				map.put(key, value);
			else
				map.put(key, null);
		}

	}	//	setContext


}	//	Context
