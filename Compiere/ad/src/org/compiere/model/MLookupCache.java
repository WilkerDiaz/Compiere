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
package org.compiere.model;

import java.util.*;

import org.compiere.util.*;

/**
 * MLookup Data Cache. - not synchronized on purpose - Called from MLookup. Only
 * caches multiple use for a single window!
 * 
 * @author Jorg Janke
 * @version $Id: MLookupCache.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MLookupCache {
    /** Logger for class MLookupCache */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MLookupCache.class);
	/** Static Logger */
	private static final CLogger s_log = CLogger.getCLogger(MLookupCache.class);
	/** Static Lookup data with MLookupInfo -> HashMap */
	private static final CCache<String, Map<Object, NamePair>> s_loadedLookups = new CCache<String, Map<Object, NamePair>>(
			"MLookupCache", 50);

	/**
	 * MLookup Loader starts loading - ignore for now
	 * 
	 * @param info
	 *            MLookupInfo
	 */
	protected static void loadStart(MLookupInfo info) {
	} // loadStart

	/**
	 * MLookup Loader ends loading, so add it to cache
	 * 
	 * @param info
	 * @param lookup
	 */
	protected static void loadEnd(MLookupInfo info, ArrayList<Object> params, Map<Object, NamePair> lookup) {
		if (info.isValidated() && lookup.size() > 0)
			s_loadedLookups.put(getKey(info, params), lookup);
	} // loadEnd

	/**
	 * Get Storage Key
	 * 
	 * @param info
	 *            lookup info
	 * @return key
	 */
	private static String getKey(MLookupInfo info, ArrayList<Object> params) {
		if (info == null)
			return String.valueOf(System.currentTimeMillis());
		//
		StringBuffer sb = new StringBuffer();
		sb.append(info.Column_ID).append(":").append(info.KeyColumn).append(
				info.AD_Reference_Value_ID).append(info.getQuery()).append(
				info.ValidationCode);
		for(Object param:params)
			if(param != null)
				sb.append("|").append(param.toString());
		// does not include ctx
		return sb.toString();
	} // getKey

	/**
	 * Load from Cache if applicable Called from MLookup constructor
	 * 
	 * @param info
	 *            MLookupInfo to search
	 * @param lookupTarget
	 *            Target HashMap
	 * @return true, if lookup found
	 */
	protected static boolean loadFromCache(MLookupInfo info, ArrayList<Object> params, Map<Object, NamePair> lookupTarget) {
		String key = getKey(info, params);
		Map<Object, NamePair> cache = s_loadedLookups.get(null, key);
		if (cache == null)
			return false;
		// Nothing cached
		s_log.warning("get from cache key is:" + key);
		if (cache.size() == 0) {
			s_loadedLookups.remove(key);
			return false;
		}

		// copy cache
		// we can use iterator, as the lookup loading is complete (i.e. no
		// additional entries)
		Iterator<?> iterator = cache.keySet().iterator();
		while (iterator.hasNext()) {
			Object cacheKey = iterator.next();
			NamePair cacheData = cache.get(cacheKey);
			lookupTarget.put(cacheKey, cacheData);
		}

		s_log.fine("#" + lookupTarget.size());
		return true;
	} // loadFromCache

	/**
	 * Clear Static Lookup Cache for Window
	 * 
	 * @param WindowNo
	 *            WindowNo of Cache entries to delete
	 */
	public static void cacheReset(int WindowNo) {
		String key = String.valueOf(WindowNo) + ":";
		int startNo = s_loadedLookups.size();
		// find keys of Lookups to delete
		ArrayList<String> toDelete = new ArrayList<String>();
		Iterator<String> iterator = s_loadedLookups.keySet().iterator();
		while (iterator.hasNext()) {
			String info = iterator.next();
			if (info != null && info.startsWith(key))
				toDelete.add(info);
		}

		// Do the actual delete
		for (int i = 0; i < toDelete.size(); i++)
			s_loadedLookups.remove(toDelete.get(i));
		int endNo = s_loadedLookups.size();
		s_log.fine("WindowNo=" + WindowNo + " - " + startNo + " -> " + endNo);
	} // cacheReset

	/**************************************************************************
	 * Private constructor
	 */
	private MLookupCache() {
	} // MLookupCache

} // MLookupCache
