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
package org.compiere.cm.cache;

import java.util.*;

import org.compiere.util.*;

/**
 *  CO CacheObject
 *  we store parts of the content in caches on the webservers to reduce DB load and speed up page deployment
 *
 *  @author Yves Sandfort
 *  @version  $Id$
 */
public class CO {

	protected static int cacheSize = 100;
	protected Hashtable<String, Object> cache = new Hashtable<String, Object>(cacheSize);
	protected Hashtable<String, Long> cacheUsage = new Hashtable<String, Long>(cacheSize);
	protected Ctx ctx;

	/**	Logger			*/
	protected CLogger	log = CLogger.getCLogger(this.getClass());

	/**
	 * 	Cache Object
	 */
	public CO () {
	}
	
	/**
	 * 	set Context
	 *	@param thisCtx
	 */
	public void setCtx(Ctx thisCtx) {
		ctx = thisCtx;
	}
	
	/**
	 * 	get Context
	 *	@return Context
	 */
	public Ctx getCtx() {
		return ctx;
	}
	
	/**
	 * 	put
	 *	@param ID
	 *	@param thisObject
	 */
	public void put(String ID, Object thisObject) {
		cache.put(ID,thisObject);
		Long thisLong = new Long(new Date().getTime());
		cacheUsage.put(ID, thisLong);
		if (cacheUsage.size()>cacheSize-1) {
			cleanUp();
		}
	}

	/**
	 * 	remove
	 *	@param ID
	 */
	public void remove(int ID) {
		cache.remove("" + ID);
		cacheUsage.remove("" + ID);
	}
	
	/**
	 * 	remove
	 *	@param ID
	 */
	public void remove(String ID) {
		cache.remove(ID);
		cacheUsage.remove(ID);
	}
	
	/**
	 * 	getSize of current cache
	 *	@return number of cache entries
	 */
	public int getSize() {
		return cache.size();
	}

	/**
	 * 	get key enumeration
	 *	@return key enumeration
	 */
	public Enumeration<String> getKeys() {
		return cache.keys();
	}

	private void cleanUp () {
		Vector<Long> vecKeys = new Vector<Long>(); 
		//Gets keys from hashtable 

		Enumeration<Long> myEnum = cacheUsage.elements();

		while (myEnum.hasMoreElements()) 
		{ 
			vecKeys.add(myEnum.nextElement()); 
		} 
                 
		//Sorts vector in Ascending order 
		Collections.sort(vecKeys); 
		Collections.reverse(vecKeys);
                 
		//Displays values using Key 
		for(int i=0;i<vecKeys.size();i++) 
		{
			String value = vecKeys.get(i).toString();
			String key = "";
			Enumeration<String> keys = cacheUsage.keys();
			while (keys.hasMoreElements() && key.equals("")) {
				String thisKey = keys.nextElement().toString();
				String tempValue = cacheUsage.get(thisKey).toString(); 
				if (tempValue.equals(value)) {
					key = thisKey;
				}
			}
			// Use Maxelements -1 since i starts with 0
			if (i>cacheSize-1) {
				cache.remove(key);
				cacheUsage.remove(key);
				log.fine("Item: " + key + " from cache: " + this.getClass().getName() + " was removed.");
			}
		}
	}
	
	/**
	 * 	getSortedKeys of Cache Object
	 *	@return list of sorted Key Value
	 */
	public String[] getSortedKeys() {
		Enumeration<String> thisEnum = cache.keys ();
		ArrayList<String> thisList = new ArrayList<String>(cache.size ());
		while (thisEnum.hasMoreElements())
			thisList.add (thisEnum.nextElement ().toString ());
		Collections.sort (thisList);
		String [] sortedList = new String[thisList.size ()];
		for (int i=0;i<thisList.size();i++)
			sortedList[i] = thisList.get (i);
		return sortedList;
	}
	
	/**
	 * 	Update Usage value for cache optimization
	 *	@param ID
	 */
	public void use(int ID) {
		Long thisLong = new Long(new java.util.Date().getTime());
		cacheUsage.put("" + ID, thisLong);
	}
	
	/**
	 * 	Update Usage value for cache optimization
	 *	@param ID
	 */
	public void use(String ID) {
		Long thisLong = new Long(new java.util.Date().getTime());
		cacheUsage.put(ID, thisLong);
	}
	
    /**
     * 	empty complete Cache
     */
    public void empty() {
    		cache = new Hashtable<String, Object>(cacheSize);
		cacheUsage = new Hashtable<String, Long>(cacheSize);
		log.fine("Cache: " + this.getClass().getName() + " was cleared.");
    }
    
    /**
     * 	Show Cache Content
     *	@return XML String with CacheContent
     */
    public String show() {
    		StringBuffer tStrHTML = new StringBuffer();
		tStrHTML.append("      <size>" + this.getSize() + "</size>\n");
		Enumeration<String> thisEnum = this.getKeys();
		while (thisEnum.hasMoreElements()) { 
			tStrHTML.append("      <item>" + thisEnum.nextElement() + "</item>\n"); 
		} 
		return tStrHTML.toString();
    }
    
    public Hashtable<String, Object> getValueTable() {
    	return cache;
    }
}
