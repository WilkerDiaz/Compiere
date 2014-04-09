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

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.util.*;

/**
 * An intelligent MutableComboBoxModel, which determines what can be cached.
 * 
 * <pre>
 *      Validated   - SQL is final / not dynamic
 *      AllLoaded   - All Records are loaded
 * 
 * 	Get Info about Lookup
 * 	-	SQL
 * 	-	KeyColumn
 * 	-	Zoom Target
 * </pre>
 * 
 * @author Jorg Janke
 * @version $Id: MLookup.java 8924 2010-06-08 07:14:56Z ragrawal $
 */
public final class MLookup extends Lookup implements Serializable {
    /** Logger for class MLookup */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MLookup.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * MLookup Constructor. Must call initialize
	 * 
	 * @param ctx
	 *            context
	 * @param windowNo
	 *            window
	 * @param displayType
	 *            display type
	 */
	public MLookup(Ctx ctx, int windowNo, int displayType) {
		super(ctx, windowNo, displayType);
	} // MLookup

	/** Inactive Marker Start */
	public static final String INACTIVE_S = "~";
	/** Inactive Marker End */
	public static final String INACTIVE_E = "~";
	/** Number of max rows to load */
	public static final int MAX_ROWS = 10000;
	/** Indicator for Null */
	private static Integer MINUS_ONE = Integer.valueOf(-1);

	/** The Lookup Info Value Object */
	MLookupInfo m_info = null;

	/** Storage of data Key-NamePair */
	private Map<Object, NamePair> m_lookupMap = new LinkedHashMap<Object, NamePair>();
	//
	private Future<LinkedHashMap<Object, NamePair>> m_result;

	/** All Data loaded */
	boolean m_allLoaded = false;
	/** Inactive records exists */
	boolean m_hasInactive = false;
	/** Next Read for Parent */
	private long m_nextRead = 0;

	public MLookup initialize(MLookupInfo info) {
		return initialize(info, true);
	}
	/**
	 * Initialize
	 * 
	 * @param info
	 *            info
	 * @return this
	 */
	public MLookup initialize(MLookupInfo info, boolean load) {
		m_info = info;
		log.fine(m_info.KeyColumn);

		//BECO
		load = false;
		//FIN BECO
		
		// Don't load Search or CreatedBy/UpdatedBy
		if ((getDisplayType() == DisplayTypeConstants.Search)
				|| m_info.IsCreadedUpdatedBy)
			return this;
		// Don't load Parents/Keys
		if (m_info.IsParent || m_info.IsKey) {
			m_hasInactive = true; // creates focus listener for dynamic loading
			return this; // required when parent needs to be selected (e.g.
			// price from product)
		}
		//
		if(load) {
			m_result = s_exec.submit(m_loader);
		}
		// m_loader.run(); // test sync call
		return this;
	} // initialize

	/**
	 * Dispose
	 * 
	 * @Override final public void dispose() { if (m_info != null)
	 *           log.fine(m_info.KeyColumn + ": dispose"); if ((m_loader !=
	 *           null) && m_loader.isAlive()) m_loader.interrupt(); m_loader =
	 *           null; // if (m_lookupMap != null) m_lookupMap.clear();
	 *           m_lookupMap = null; if (m_lookupDirect != null)
	 *           m_lookupDirect.clear(); m_lookupDirect = null; // m_info =
	 *           null; // super.dispose(); } // dispose
	 */
	/**
	 * Wait until async Load Complete
	 */
	@Override
	public void loadComplete() {
		try {
			
			log.fine((m_info.KeyColumn == null ? "ID=" + m_info.Column_ID
					: m_info.KeyColumn)
					+ ": waiting for Loader");
			if( m_result != null )
				m_lookupMap = m_result.get();
		} catch (InterruptedException e) {
			log.log(Level.SEVERE, m_info.KeyColumn + ": Interrupted", e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // loadComplete

	/**
	 * Get value (name) for key. If not found return null;
	 * 
	 * @param key
	 *            key (Integer for Keys or String for Lists)
	 * @return value
	 */
	@Override
	public NamePair get(Object key) {
		if ((key == null) || MINUS_ONE.equals(key)) // indicator for null
			return null;

		if (m_info.IsParent && (m_nextRead < System.currentTimeMillis())) {
			m_lookupMap.clear();
			if (m_lookupDirect != null)
				m_lookupDirect.clear();
			m_nextRead = System.currentTimeMillis() + 500; // 1/2 sec
		}

		// try cache
		loadComplete();
		NamePair retValue = m_lookupMap.get(key);
		if (retValue != null)
			return retValue;

		// Always check for parents - not if we SQL was validated and completely
		// loaded
		if (!m_info.IsParent && m_info.isValidated() && m_allLoaded) {
			log.finer(m_info.KeyColumn + ": <NULL> - " + key // + "(" +
					// key.getClass()
					+ "; Size=" + m_lookupMap.size());
			// log.finest( m_lookup.keySet().toString(), "ContainsKey = " +
			// m_lookup.containsKey(key));
			// also for new values and inactive ones
			return getDirect(key, false, true); // cache locally
		}

		log.finest(m_info.KeyColumn + ": " + key + "; Size="
				+ m_lookupMap.size() + "; Validated=" + m_info.isValidated()
				+ "; All Loaded=" + m_allLoaded + "; HasInactive="
				+ m_hasInactive);
		// never loaded
		if (!m_allLoaded && (m_lookupMap.size() == 0)
				&& !m_info.IsCreadedUpdatedBy && !m_info.IsParent
				&& (getDisplayType() != DisplayTypeConstants.Search)) {
			m_result = s_exec.submit(m_loader);
			loadComplete();
			retValue = m_lookupMap.get(key);
			if (retValue != null)
				return retValue;
		}
		// Try to get it directly
		boolean cacheLocal = m_info.isValidated();
		return getDirect(key, false, cacheLocal); // do NOT cache
	} // get

	/**
	 * Get Display value (name). If not found return key embedded in inactive
	 * signs.
	 * 
	 * @param key
	 *            key
	 * @return value
	 */
	@Override
	public String getDisplay(Object key) {
		if (key == null)
			return "";
		//
		Object display = get(key);
		if (display == null)
			return "<" + key.toString() + ">";
		return display.toString();
	} // getDisplay

	/**
	 * The Lookup contains the key
	 * 
	 * @param key
	 *            key
	 * @return true if key is known
	 */
	@Override
	public boolean containsKey(Object key) {
		return m_lookupMap.containsKey(key);
	} // containsKey

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return "MLookup[" + m_info.KeyColumn + ",Column_ID=" + m_info.Column_ID
		+ ",Size=" + m_lookupMap.size() + ",Validated=" + isValidated()
		+ "-" + getValidation() + "]";
	} // toString

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param obj
	 *            the reference object with which to compare.
	 * @return <code>true</code> if this object is the same as the obj argument;
	 *         <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MLookup) {
			MLookup ll = (MLookup) obj;
			if (ll.m_info.Column_ID == this.m_info.Column_ID)
				return true;
		}
		return false;
	} // equals

	/**
	 * Return Size
	 * 
	 * @return size
	 */
	public int size() {
		return m_lookupMap.size();
	} // size

	/**
	 * Is it all loaded
	 * 
	 * @return true, if all loaded
	 */
	public boolean isAllLoaded() {
		return m_allLoaded;
	} // isAllLoaded

	/**
	 * Disable Validation (for Search/Find)
	 */
	@Override
	public void disableValidation() {
		String validationCode = m_info.ValidationCode;
		super.disableValidation();
		m_info.setValidated(true);
		m_info.ValidationCode = "";
		// Switch to Search for Table/Dir Validation
		if ((validationCode != null) && (validationCode.length() > 0)) {
			if (((validationCode.indexOf("@") != -1) && (getDisplayType() == DisplayTypeConstants.Table))
					|| (getDisplayType() == DisplayTypeConstants.TableDir)) {
				setDisplayType(DisplayTypeConstants.Search);
			}
		}
	} // disableValidation

	/**
	 * Is the List fully Validated
	 * 
	 * @return true, if validated
	 */
	@Override
	public boolean isValidated() {
		if (isValidationDisabled())
			return true;
		if (m_info == null)
			return false;
		return m_info.isValidated();
	} // isValidated

	/**
	 * Get Validation SQL
	 * 
	 * @return Validation SQL
	 */
	@Override
	public String getValidation() {
		if (isValidationDisabled())
			return "";
		return m_info.ValidationCode;
	} // getValidation

	/**
	 * Get Reference Value
	 * 
	 * @return Reference Value
	 */
	public int getAD_Reference_Value_ID() {
		return m_info.AD_Reference_Value_ID;
	} // getAD_Reference_Value_ID

	/**
	 * Has inactive elements in list
	 * 
	 * @return true, if list contains inactive values
	 */
	@Override
	public boolean hasInactive() {
		return m_hasInactive;
	} // hasInactive

	/**
	 * Return info as ArrayList containing Value/KeyNamePair
	 * 
	 * @param onlyValidated
	 *            only validated
	 * @param loadParent
	 *            get Data even for parent lookups
	 * @return List
	 */
	private ArrayList<NamePair> getData(boolean onlyValidated,
			boolean loadParent) {
		loadComplete();
		// Never Loaded (correctly)
		if (!m_allLoaded || (m_lookupMap.size() == 0))
			refresh(loadParent);

		// already validation included
		if (m_info.isValidated())
			return new ArrayList<NamePair>(m_lookupMap.values());

		if (!m_info.isValidated() && onlyValidated) {
			refresh(loadParent);
			log.fine(m_info.KeyColumn + ": Validated - #" + m_lookupMap.size()
					+ (loadParent ? " (loadParent)" : " (not loadParent)"));
		}

		return new ArrayList<NamePair>(m_lookupMap.values());
	} // getData

	/**
	 * Return data as Array containing Value/KeyNamePair
	 * 
	 * @param mandatory
	 *            if not mandatory, an additional empty value is inserted
	 * @param onlyValidated
	 *            only validated
	 * @param onlyActive
	 *            only active
	 * @param temporary
	 *            force load for temporary display e.g. parent
	 * @return list
	 */
	@Override
	public ArrayList<NamePair> getData(boolean mandatory,
			boolean onlyValidated, boolean onlyActive, boolean temporary) {
		// create list
		ArrayList<NamePair> list = getData(onlyValidated, temporary);

		// Remove inactive choices
		if (onlyActive && m_hasInactive) {
			// list from the back
			for (int i = list.size(); i > 0; i--) {
				Object o = list.get(i - 1);
				if (o != null) {
					String s = o.toString();
					if (s.startsWith(INACTIVE_S) && s.endsWith(INACTIVE_E))
						list.remove(i - 1);
				}
			}
		}

		// Add Optional (empty) selection
		if (!mandatory) {
			NamePair p = null;
			if ((m_info.KeyColumn != null) && m_info.KeyColumn.toUpperCase().endsWith("_ID"))
				p = new KeyNamePair(-1, "");
			else
				p = new ValueNamePair("", "");
			list.add(0, p);
		}

		return list;
	} // getData

	/** Save getDirect last return value */
	private HashMap<Object, Object> m_lookupDirect = null;
	/** Save last unsuccessful */
	private Object m_directNullKey = null;

	/**
	 * Get Data Direct from Table.
	 * 
	 * @param key
	 *            key
	 * @param saveInCache
	 *            save in cache for r/w
	 * @param cacheLocal
	 *            cache locally for r/o
	 * @return value
	 */
	@Override
	public NamePair getDirect(Object key, boolean saveInCache,
			boolean cacheLocal) {
		// Nothing to query
		if ((key == null) || (m_info.QueryDirect == null)
				|| (m_info.QueryDirect.length() == 0))
			return null;
		if (key.equals(m_directNullKey))
			return null;
		//
		NamePair directValue = null;
		if (m_lookupDirect != null) // Lookup cache
		{
			directValue = (NamePair) m_lookupDirect.get(key);
			if (directValue != null)
				return directValue;
		}
		log.finer(m_info.KeyColumn + ": " + key + ", SaveInCache="
				+ saveInCache + ",Local=" + cacheLocal);
		boolean isNumber = m_info.KeyColumn.toUpperCase().endsWith("_ID");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// SELECT Key, Value, Name FROM ...
			pstmt = DB.prepareStatement(m_info.QueryDirect,
					(Trx) null);
			if (isNumber)
				pstmt.setInt(1, Integer.parseInt(key.toString()));
			else
				pstmt.setString(1, key.toString());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String name = rs.getString(3);

				boolean isActive = rs.getString(4).equals("Y");
				// bug 10019093, show ~ when record is "inactive"
				if (!isActive) {
					name = INACTIVE_S + name + INACTIVE_E;
					m_hasInactive = true;
				}

				if (isNumber) {
					int keyValue = rs.getInt(1);
					KeyNamePair p = new KeyNamePair(keyValue, name);
					if (saveInCache) // save if
						m_lookupMap.put(Integer.valueOf(keyValue), p);
					directValue = p;
				} else {
					String value = rs.getString(2);
					ValueNamePair p = new ValueNamePair(value, name);
					if (saveInCache) // save if
						m_lookupMap.put(value, p);
					directValue = p;
				}
				if (rs.next())
					log.log(Level.SEVERE, m_info.KeyColumn
							+ ": Not unique (first returned) for " + key
							+ " SQL=" + m_info.QueryDirect);
			} else {
				m_directNullKey = key;
				directValue = null;
			}

			if (CLogMgt.isLevelFinest())
				log.finest(m_info.KeyColumn + ": " + directValue + " - "
						+ m_info);
		} 
		catch (Exception e) {
			log.log(Level.SEVERE, m_info.KeyColumn + ": SQL="
					+ m_info.QueryDirect + "; Key=" + key, e);
			directValue = null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		// Cache Local if not added to R/W cache
		if (cacheLocal && !saveInCache && (directValue != null)) {
			if (m_lookupDirect == null)
				m_lookupDirect = new HashMap<Object, Object>();
			m_lookupDirect.put(key, directValue);
		}
		m_hasInactive = true;
		return directValue;
	} // getDirect

	/**
	 * Get Zoom
	 * 
	 * @return Zoom AD_Window_ID
	 */
	@Override
	public int getZoomWindow() {
		return m_info.ZoomWindow;
	} // getZoomWindow

	/**
	 * Get Zoom
	 * 
	 * @param query
	 *            query
	 * @return Zoom Window
	 */
	@Override
	public int getZoomWindow(Query query) {

		/*
		 * Handle cases where you have multiple windows for a single table. So
		 * far it is just the tables that have a PO Window defined. For eg.,
		 * Order, Invoice and Shipments This will need to be expanded to add
		 * more tables if they have multiple windows.
		 */
		int AD_Window_ID = ZoomTarget.getZoomAD_Window_ID(m_info.TableName,
				m_WindowNo, query.getWhereClause(), getCtx()
				.isSOTrx(m_WindowNo));

		return AD_Window_ID;
	} // getZoomWindow

	/**
	 * Get Zoom Query String
	 * 
	 * @return Zoom SQL Where Clause
	 */
	@Override
	public Query getZoomQuery() {
		return m_info.ZoomQuery;
	} // getZoom

	/**
	 * Get underlying fully qualified Table.Column Name
	 * 
	 * @return Key Column
	 */
	@Override
	public String getColumnName() {
		return m_info.KeyColumn;
	} // g2etColumnName

	/**
	 * Refresh & return number of items read. Get get data of parent lookups
	 * 
	 * @return no of items read
	 */
	@Override
	public int refresh() {
		return refresh(true);
	} // refresh

	/**
	 * Refresh & return number of items read
	 * 
	 * @param loadParent
	 *            get data of parent lookups
	 * @return no of items read
	 */
	public int refresh(boolean loadParent) {
		// if (!loadParent && m_info.IsParent)
		// return 0; // issues with Role_OrgAccess - AD_Org_ID
		// Don't load Search or CreatedBy/UpdatedBy
		if ((getDisplayType() == DisplayTypeConstants.Search)
				|| m_info.IsCreadedUpdatedBy)
			return 0;
		//
		log.fine(m_info.KeyColumn + ": start");
		
		m_result = s_exec.submit(m_loader);
		
		// m_loader.run(); // test sync call
		loadComplete(); // wait
		log.fine(m_info.KeyColumn + ": #" + m_lookupMap.size());
		//
		return m_lookupMap.size();
	} // refresh

	/**
	 * Remove All cached Elements
	 * 
	 * @see org.compiere.model.Lookup#removeAllElements()
	 */
	@Override
	public void removeAllElements() {
		super.removeAllElements();
		m_lookupMap.clear();
		if (m_lookupDirect != null)
			m_lookupDirect.clear();
	} // removeAllElements

	/**
	 * Clear lookup map only
	 */
	@Override
	public void clear() {
		super.clear();
		m_lookupMap.clear();
	} // clear

	/**************************************************************************
	 * MLookup Loader
	 */
	private final Callable<LinkedHashMap<Object, NamePair>> m_loader = new Callable<LinkedHashMap<Object, NamePair>>() {
		
		private final long m_startTime = System.currentTimeMillis();


		/**
		 * Load Lookup
		 */
		@Override
		public LinkedHashMap<Object, NamePair> call() throws Exception {
			
			LinkedHashMap<Object, NamePair> lookup = new LinkedHashMap<Object, NamePair>(); 
			
			long startTime = System.currentTimeMillis();
			String sql = m_info.getQuery();

			List<Object> params = new ArrayList<Object>();
			// not validated
			if (!m_info.isValidated()) {
				Env.QueryParams queryParams = Env
				.parseContextUsingBindVariables(getCtx(),
						getWindowNo(), m_info.ValidationCode, false,
						false);
				String validation = queryParams.sql;
				params = queryParams.params;
				if ((validation.length() == 0)
						&& (m_info.ValidationCode.length() > 0)) {
					log.fine(m_info.KeyColumn + ": Loader NOT Validated: "
							+ m_info.ValidationCode);
					return lookup;
				} else {
					log.fine(m_info.KeyColumn + ": Loader Validated: "
							+ validation);

					int posFrom = sql.lastIndexOf(" FROM ");
					boolean hasWhere = sql.indexOf(" WHERE ", posFrom) != -1;
					//
					int posOrder = sql.lastIndexOf(" ORDER BY ");
					if (posOrder != -1)
						sql = sql.substring(0, posOrder)
						+ (hasWhere ? " AND " : " WHERE ") + validation
						+ sql.substring(posOrder);
					else
						sql += (hasWhere ? " AND " : " WHERE ")

						+ validation;
					if (CLogMgt.isLevelFinest())
						log.fine(m_info.KeyColumn + ": Validation="
								+ validation);
				}
			}
			//
			if (CLogMgt.isLevelFiner())
				getCtx().setContext(EnvConstants.WINDOW_MLOOKUP,
						m_info.Column_ID, m_info.KeyColumn, sql);
			if (CLogMgt.isLevelFinest())
				log.fine(m_info.KeyColumn + ": " + sql);

			//BECO JTrias (Buscando velocidad)
			int whereIndex = 0;
			whereIndex =sql.indexOf(" WHERE ");
			
			if(whereIndex > 0){
				
				int myOrderBy = sql.indexOf(" ORDER BY ");
				String sqlA = sql.substring(0, whereIndex);
				String sqlB = " WHERE " + sql.substring(whereIndex + 7, myOrderBy);
				
				if(!m_info.KeyColumn.equalsIgnoreCase("M_AttributeValue.M_AttributeValue_ID") && 
						!m_info.KeyColumn.equalsIgnoreCase("XX_VMR_LongCharacteristic.XX_VMR_LongCharacteristic_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("XX_VMR_Department.XX_VMR_Department_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("XX_VMR_Line.XX_VMR_Line_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("XX_VMR_Section.XX_VMR_Section_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("XX_VMR_Brand.XX_VMR_Brand_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("C_Country.C_Country_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("AD_User.AD_User_ID") && 
						!m_info.KeyColumn.equalsIgnoreCase("XX_VLO_Fleet.XX_VLO_Fleet_ID") && 
						!m_info.KeyColumn.equalsIgnoreCase("C_Currency.C_Currency_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("AD_Val_Rule.AD_Val_Rule_ID") && 
						!m_info.KeyColumn.equalsIgnoreCase("AD_Reference.AD_Reference_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("C_BPartner.C_BPartner_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("XX_VMR_Order.XX_VMR_Order_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("AD_Process.AD_Process_ID") && 
						!m_info.KeyColumn.equalsIgnoreCase("C_Job.C_Job_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("AD_Form.AD_Form_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("XX_VCN_EconoActivi.XX_VCN_EconoActivi_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("XX_VMR_PriceConsecutive.XX_VMR_PriceConsecutive_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("XX_VMR_UnitConversion.XX_VMR_UnitConversion_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("XX_Contract.XX_Contract_ID") &&
						!m_info.KeyColumn.equalsIgnoreCase("XX_VMR_DistributionHeader.XX_VMR_DistributionHeader_ID") 
					){
					sqlB += " AND ROWNUM < 31";
				}
				
				String sqlC = sql.substring(myOrderBy, sql.length());
				sql = sqlA + sqlB + sqlC;
			}
			//FIN BECO
			
			// Reset
			lookup.clear();

			//			MLookupCache.loadStart(m_info);


			boolean isNumber = m_info.KeyColumn.toUpperCase().endsWith("_ID");
			m_hasInactive = false;
			int rows = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				// SELECT Key, Value, Name, IsActive FROM ...
				pstmt = DB.prepareStatement(sql, (Trx) null);
				int i = 1;
				for (Object param : params) {
					if (param instanceof BigDecimal)
						pstmt.setBigDecimal(i, (BigDecimal) param);
					else if (param == null)
						pstmt.setNull(i, Types.NVARCHAR);
					else
						pstmt.setString(i, param.toString());
					++i;
				}
				rs = pstmt.executeQuery();

				// Get first ... rows
				m_allLoaded = true;
				while (rs.next()) {
					if (rows++ > MAX_ROWS) {
						log.fine(m_info.KeyColumn
								+ ": Loader - Too many records");
						m_allLoaded = false;
						break;
					}

					// load data
					String name = rs.getString(3);
					boolean isActive = rs.getString(4).equals("Y");
					if (!isActive) {
						name = INACTIVE_S + name + INACTIVE_E;
						m_hasInactive = true;
					}
					if (isNumber) {
						int key = rs.getInt(1);
						KeyNamePair p = new KeyNamePair(key, name);
						lookup.put(Integer.valueOf(key), p);
					} else {
						String value = rs.getString(2);
						ValueNamePair p = new ValueNamePair(value, name);
						lookup.put(value, p);
					}
					// log.fine( m_info.KeyColumn + ": " + name);
				}
			} 
			catch (SQLException e) {
				log
				.log(Level.SEVERE, m_info.KeyColumn + ": Loader - "
						+ sql, e);
			} 
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			
			int size = lookup.size();
			long duration = System.currentTimeMillis() - startTime;
			log.finer(m_info.KeyColumn
					+ " ("
					+ m_info.Column_ID
					+ "):"
					// + " ID=" + m_info.AD_Column_ID + " " +
					+ " - Loader complete #" + size + " - all=" + m_allLoaded
					+ " - ms="
					+ String.valueOf(System.currentTimeMillis() - m_startTime)
					+ " ("
					+ duration
					+ ")");
			return lookup;
		} // run
	}; // Loader

} // MLookup
