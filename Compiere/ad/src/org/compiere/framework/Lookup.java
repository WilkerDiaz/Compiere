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
package org.compiere.framework;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import org.compiere.util.*;

/**
 * Base Class for MLookup, MLocator. as well as for MLocation, MAccount (only
 * single value) Maintains selectable data as NamePairs in ArrayList The objects
 * itself may be shared by the lookup implementation (ususally HashMap)
 * 
 * @author Jorg Janke
 * @version $Id: Lookup.java 8244 2009-12-04 23:25:29Z freyes $
 */
public abstract class Lookup extends AbstractListModel implements
		MutableComboBoxModel, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3922072007386668879L;

	//keep 20 threads and use max 1000 threads; in case where max threads are exhausted,
	//run the task immediately in the calling thread.. essentially synchronosly
	//thread reclaimed idling 1 minute
	protected final static ExecutorService s_exec = new ThreadPoolExecutor(5, 1000,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
	/**
	 * Lookup
	 * 
	 * @param ctx
	 *            context
	 * @param windowNo
	 *            window no
	 * @param displayType
	 *            display type
	 */
	public Lookup(Ctx ctx, int windowNo, int displayType) {
		setContext(ctx, windowNo);
		setDisplayType(displayType);
	} // Lookup

	/** The Data List */
	protected volatile ArrayList<NamePair> p_data = new ArrayList<NamePair>();

	/** The Selected Item */
	private volatile Object m_selectedObject;

	/** Logger */
	protected static CLogger log = CLogger.getCLogger(Lookup.class);

	/** Context */
	private Ctx m_ctx;
	/** Window No */
	protected int m_WindowNo;
	/** Display Type */
	private int m_displayType;
	/** Disable Validation */
	private boolean m_validationDisabled = false;

	/**
	 * Set Context
	 * 
	 * @param ctx
	 *            context
	 * @param windowNo
	 *            window
	 */
	public void setContext(Ctx ctx, int windowNo) {
		m_ctx = ctx;
		m_WindowNo = windowNo;
	} // getContext

	/**
	 * Get Ctx
	 * 
	 * @return context
	 */
	public Ctx getCtx() {
		return m_ctx;
	} // getCtx

	/**
	 * Get Window No
	 * 
	 * @return Window No
	 */
	public int getWindowNo() {
		return m_WindowNo;
	} // getWindowNo

	/**
	 * Get Display Type
	 * 
	 * @return display type
	 */
	public int getDisplayType() {
		return m_displayType;
	} // getDisplayType

	/**
	 * Set Display Type
	 * 
	 * @param displayType
	 *            display type
	 */
	public void setDisplayType(int displayType) {
		m_displayType = displayType;
	} // setDisplayType

	/**************************************************************************
	 * Set the value of the selected item. Checks, if value has changed first.
	 * The selected item may be null. If it is not valid, set to null.
	 * 
	 * @param anObject
	 *            The combo box value or null for no selection.
	 */
	public void setSelectedItem(Object anObject) {
		if (((m_selectedObject != null) && !m_selectedObject.equals(anObject))
				|| ((m_selectedObject == null) && (anObject != null))) {
			setSelectedItemAlways(anObject);
		}
	} // setSelectedItem

	/**
	 * Set the value of the selected item. The selected item may be null. If it
	 * is not valid, set to null.
	 * 
	 * @param anObject
	 *            The combo box value or null for no selection.
	 */
	public void setSelectedItemAlways(Object anObject) {
		if (p_data.contains(anObject) || (anObject == null)) {
			m_selectedObject = anObject;
			// Log.trace(s_ll, "Lookup.setSelectedItem", anObject);
		} else {
			m_selectedObject = null;
			log.fine(getColumnName() + ": - Set to NULL");
		}
		// if (m_worker == null || !m_worker.isAlive())
		fireContentsChanged(this, -1, -1);
	} // setSelectedItem

	/**
	 * Return previously selected Item
	 * 
	 * @return value
	 */
	public Object getSelectedItem() {
		return m_selectedObject;
	} // getSelectedItem

	/**
	 * Get Size of Model
	 * 
	 * @return size
	 */
	public int getSize() {
		return p_data.size();
	} // getSize

	/**
	 * Get Element at Index
	 * 
	 * @param index
	 *            index
	 * @return value
	 */
	public Object getElementAt(int index) {
		return p_data.get(index);
	} // getElementAt

	/**
	 * Returns the index-position of the specified object in the list.
	 * 
	 * @param anObject
	 *            object
	 * @return an int representing the index position, where 0 is the first
	 *         position
	 */
	public int getIndexOf(Object anObject) {
		return p_data.indexOf(anObject);
	} // getIndexOf

	/**
	 * Add Element at the end
	 * 
	 * @param anObject
	 *            object
	 */
	public void addElement(NamePair anObject) {
		p_data.add(anObject);
		fireIntervalAdded(this, p_data.size() - 1, p_data.size() - 1);
		if ((p_data.size() == 1) && (m_selectedObject == null)
				&& (anObject != null))
			setSelectedItem(anObject);
	} // addElement

	/**
	 * Add Element at the end
	 * 
	 * @param anObject
	 *            object
	 */
	public void addElement(Object anObject) {
		addElement((NamePair) anObject);
	} // addElement

	/**
	 * Insert Element At
	 * 
	 * @param anObject
	 *            object
	 * @param index
	 *            index
	 */
	public void insertElementAt(NamePair anObject, int index) {
		p_data.add(index, anObject);
		fireIntervalAdded(this, index, index);
	} // insertElementAt

	/**
	 * Insert Element At
	 * 
	 * @param anObject
	 *            object
	 * @param index
	 *            index
	 */
	public void insertElementAt(Object anObject, int index) {
		insertElementAt((NamePair) anObject, index);
	} // insertElementAt

	/**
	 * Remove Item at index
	 * 
	 * @param index
	 *            index
	 */
	public void removeElementAt(int index) {
		if (getElementAt(index) == m_selectedObject) {
			if (index == 0)
				setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
			else
				setSelectedItem(getElementAt(index - 1));
		}
		p_data.remove(index);
		fireIntervalRemoved(this, index, index);
	} // removeElementAt

	/**
	 * Remove Item
	 * 
	 * @param anObject
	 *            object
	 */
	public void removeElement(Object anObject) {
		int index = p_data.indexOf(anObject);
		if (index != -1)
			removeElementAt(index);
	} // removeItem

	/**
	 * Empties the list.
	 */
	public void removeAllElements() {
		if (p_data.size() > 0) {
			int firstIndex = 0;
			int lastIndex = p_data.size() - 1;
			p_data.clear();
			m_selectedObject = null;
			fireIntervalRemoved(this, firstIndex, lastIndex);
		}
	} // removeAllElements

	/**
	 * Clear Map
	 */
	public void clear() {
		removeAllElements();
	} // clear

	/**************************************************************************
	 * Put Value
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 */
	public void put(String key, String value) {
		NamePair pp = new ValueNamePair(key, value);
		addElement(pp);
	} // put

	/**
	 * Put Value
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 */
	public void put(int key, String value) {
		NamePair pp = new KeyNamePair(key, value);
		addElement(pp);
	} // put

	/**
	 * Fill ComboBox with lookup data (async using Worker). - try to maintain
	 * selected item
	 * 
	 * @param mandatory
	 *            has mandatory data only (i.e. no "null" selection)
	 * @param onlyValidated
	 *            only validated
	 * @param onlyActive
	 *            only active
	 * @param temporary
	 *            save current values - restore via fillComboBox (true) * 
	 */
	public void fillComboBox(boolean mandatory, boolean onlyValidated,
			boolean onlyActive, boolean temporary) {
		long startTime = System.currentTimeMillis();

		Object obj = m_selectedObject;
		if (p_data != null)
			p_data.clear();

		// may cause delay *** The Actual Work ***
		p_data = getData(mandatory, onlyValidated, onlyActive, temporary);
		int size = 0;
		if (p_data != null)
			size = p_data.size();
		// Selected Object changed
		if (obj != m_selectedObject) {
			log.finest(getColumnName() + ": SelectedValue Changed=" + obj
					+ "->" + m_selectedObject);
			obj = m_selectedObject;
		}

		// if nothing selected & mandatory, select first
		if ((obj == null) && mandatory && (size > 0)) {
			obj = p_data.get(0);
			m_selectedObject = obj;
			log.finest(getColumnName() + ": SelectedValue SetToFirst=" + obj);
			// fireContentsChanged(this, -1, -1);
		}
		fireContentsChanged(this, 0, size);
		if (size == 0)
			log.fine(getColumnName() + ": #0 - ms="
					+ String.valueOf(System.currentTimeMillis() - startTime));
		else
			log.fine(getColumnName() + ": #" + size + " - ms="
					+ String.valueOf(System.currentTimeMillis() - startTime));
	} // fillComboBox

	/**
	 * Fill ComboBox with old saved data (if exists) or all data available *
	 * 
	 * @param restore
	 *            if true, use saved data - else fill it with all data
	 */
	public void fillComboBox(boolean restore) {
		if (p_data != null)
			fillComboBox(false, false, false, false);
	} // fillComboBox

	/**************************************************************************
	 * Get Display of Key Value
	 * 
	 * @param key
	 *            key
	 * @return String
	 */
	public abstract String getDisplay(Object key);

	/**
	 * Get Object of Key Value
	 * 
	 * @param key
	 *            key
	 * @return Object or null
	 */
	public abstract NamePair get(Object key);

	/**
	 * Fill ComboBox with Data (Value/KeyNamePair)
	 * 
	 * @param mandatory
	 *            has mandatory data only (i.e. no "null" selection)
	 * @param onlyValidated
	 *            only validated
	 * @param onlyActive
	 *            only active
	 * @param temporary
	 *            force load for temporary display
	 * @return ArrayList
	 */
	public abstract ArrayList<NamePair> getData(boolean mandatory,
			boolean onlyValidated, boolean onlyActive, boolean temporary);

	/**
	 * Get underlying fully qualified Table.Column Name. Used for
	 * VLookup.actionButton (Zoom)
	 * 
	 * @return column name
	 */
	public abstract String getColumnName();

	/**
	 * The Lookup contains the key
	 * 
	 * @param key
	 *            key
	 * @return true if contains key
	 */
	public abstract boolean containsKey(Object key);

	/**************************************************************************
	 * Refresh Values - default implementation
	 * 
	 * @return size
	 */
	public int refresh() {
		return 0;
	} // refresh

	/**
	 * Disable Validation
	 */
	public void disableValidation() {
		String validationCode = getValidation();
		if ((validationCode != null) && (validationCode.length() > 0)) {
		}
		m_validationDisabled = true;
	} // disablevalidation

	/**
	 * Is Validation Disabled
	 * 
	 * @return true if disabled
	 */
	public boolean isValidationDisabled() {
		return m_validationDisabled;
	} // isValidationDisabled

	/**
	 * Is Validated - default implementation
	 * 
	 * @return true if validated
	 */
	public boolean isValidated() {
		return true;
	} // isValidated

	/**
	 * Get dynamic Validation SQL (none)
	 * 
	 * @return validation
	 */
	public String getValidation() {
		return "";
	} // getValidation

	/**
	 * Has Inactive records - default implementation
	 * 
	 * @return true if inactive
	 */
	public boolean hasInactive() {
		return false;
	}

	/**
	 * Get Zoom - default implementation
	 * 
	 * @return Zoom AD_Window_ID
	 */
	public int getZoomWindow() {
		return 0;
	} // getZoom

	/**
	 * Get Zoom - default implementation
	 * 
	 * @param query
	 *            query
	 * @return Zoom Window - here 0
	 */
	public int getZoomWindow(Query query) {
		return 0;
	} // getZoomWindow

	/**
	 * Get Zoom Query String - default implementation
	 * 
	 * @return Zoom Query
	 */
	public Query getZoomQuery() {
		return null;
	} // getZoomQuery

	/**
	 * Get Data Direct from Table. Default implementation - does not requery
	 * 
	 * @param key
	 *            key
	 * @param saveInCache
	 *            save in cache for r/w
	 * @param cacheLocal
	 *            cache locally for r/o
	 * @return value
	 */
	public NamePair getDirect(Object key, boolean saveInCache,
			boolean cacheLocal) {
		return get(key);
	} // getDirect

	/**
	 * Wait until async Load Complete
	 */
	public void loadComplete() {
	} // loadComplete

	/**
	 * String Representation
	 * 
	 * @return info
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Lookup[");
		if (p_data != null)
			sb.append("#").append(p_data.size());
		sb.append("]");
		return sb.toString();
	} // toString

} // Lookup
