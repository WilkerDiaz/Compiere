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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.framework.Lookup;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.KeyNamePair;
import org.compiere.util.NamePair;
import org.compiere.util.Trx;

/**
 * Warehouse Locator Lookup Model. (Lookup Model is model.Lookup.java)
 * 
 * @author Jorg Janke
 * @version $Id: MLocatorLookup.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public final class MLocatorLookup extends Lookup implements Serializable {
    /** Logger for class MLocatorLookup */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MLocatorLookup.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 */
	public MLocatorLookup(Ctx ctx, int WindowNo) {
		super(ctx, WindowNo, DisplayTypeConstants.TableDir);
		//
		m_result = s_exec.submit(m_loader);
	} // MLocator

	protected int C_Locator_ID;
	
	private Future<LinkedHashMap<Integer, KeyNamePair>> m_result;


	/** Only Warehouse */
	private int m_only_Warehouse_ID = 0;
	/** Only Product */
	private int m_only_Product_ID = 0;
	/** Only outgoing Trx */
	private Boolean m_only_Outgoing = null;

	/** Storage of data MLookups */
	private Map<Integer, KeyNamePair> m_lookup = new LinkedHashMap<Integer, KeyNamePair>();
	/** Max Locators per Lookup */
	private static final int s_maxRows = 10000; // how many rows to read

	private String m_defaultID = "";

	public String getDefault() {
		return m_defaultID;
	}

	/**
	 * Dispose
	 * 
	 * @Override public void dispose() { log.fine("C_Locator_ID=" +
	 *           C_Locator_ID); if (m_loader != null) { while
	 *           (m_loader.isAlive()) m_loader.interrupt(); } m_loader = null;
	 *           if (m_lookup != null) m_lookup.clear(); m_lookup = null; //
	 *           super.dispose(); } // dispose
	 */
	/**
	 * Set Warehouse restriction
	 * 
	 * @param only_Warehouse_ID
	 *            warehouse
	 */
	public void setOnly_Warehouse_ID(int only_Warehouse_ID) {
		m_only_Warehouse_ID = only_Warehouse_ID;
	} // setOnly_Warehouse_ID

	/**
	 * Get Only Wahrehouse
	 * 
	 * @return warehouse
	 */
	public int getOnly_Warehouse_ID() {
		return m_only_Warehouse_ID;
	} // getOnly_Warehouse_ID

	/**
	 * Set Product restriction
	 * 
	 * @param only_Product_ID
	 *            Product
	 */
	public void setOnly_Product_ID(int only_Product_ID) {
		m_only_Product_ID = only_Product_ID;
	} // setOnly_Product_ID

	/**
	 * Get Only Product
	 * 
	 * @return Product
	 */
	public int getOnly_Product_ID() {
		return m_only_Product_ID;
	} // getOnly_Product_ID

	/**
	 * Set Only Outgoing Trx
	 * 
	 * @param isOutgoing
	 *            SO Trx
	 */
	public void setOnly_Outgoing(Boolean isOutgoing) {
		m_only_Outgoing = isOutgoing;
	} // setOnly_Outgoing

	/**
	 * Get Outgoing Trx
	 * 
	 * @return Outgoing Trx if set
	 */
	public Boolean isOnly_Outgoing() {
		return m_only_Outgoing;
	} // isOnly_Outgoing

	/**
	 * Wait until async Load Complete
	 */
	@Override
	public void loadComplete() {
		try {
			m_lookup = m_result.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // loadComplete

	/**
	 * Get value
	 * 
	 * @param key
	 *            key
	 * @return value value
	 */
	@Override
	public NamePair get(Object key) {
		if (key == null)
			return null;

		// try cache
		NamePair pp = m_lookup.get(key);
		if (pp != null)
			return pp;

		// Not found and waiting for loader
		loadComplete();
		// is most current
		pp = m_lookup.get(key);
		if (pp != null)
			return pp;

		// Try to get it directly
		return getDirect(key, true, null);
	} // get

	/**
	 * Get Display value
	 * 
	 * @param value
	 *            value
	 * @return String to display
	 */
	@Override
	public String getDisplay(Object value) {
		if (value == null)
			return "";
		//
		NamePair display = get(value);
		if (display == null)
			return "<" + value.toString() + ">";
		return display.toString();
	} // getDisplay

	/**
	 * The Lookup contains the key
	 * 
	 * @param key
	 *            key
	 * @return true, if lookup contains key
	 */
	@Override
	public boolean containsKey(Object key) {
		return m_lookup.containsKey(key);
	} // containsKey

	/**
	 * Get Data Direct from Table
	 * 
	 * @param keyValue
	 *            integer key value
	 * @param saveInCache
	 *            save in cache
	 * @param trx
	 *            transaction
	 * @return Object directly loaded
	 */
	public NamePair getDirect(Object keyValue, boolean saveInCache, Trx trx) {
		MLocator loc = getMLocator(keyValue, trx);
		if (loc == null)
			return null;
		//
		int key = loc.getM_Locator_ID();
		KeyNamePair retValue = new KeyNamePair(key, loc.toString());
		if (saveInCache)
			m_lookup.put(Integer.valueOf(key), retValue);
		return retValue;
	} // getDirect

	/**
	 * Get Data Direct from Table
	 * 
	 * @param keyValue
	 *            integer key value
	 * @param trx
	 *            transaction
	 * @return Object directly loaded
	 */
	public MLocator getMLocator(Object keyValue, Trx trx) {
		// log.fine( "MLocatorLookup.getDirect " + keyValue.getClass() + "=" +
		// keyValue);
		int M_Locator_ID = -1;
		try {
			M_Locator_ID = Integer.parseInt(keyValue.toString());
		} catch (Exception e) {
		}
		if (M_Locator_ID == -1) {
			log.log(Level.SEVERE, "Invalid key=" + keyValue);
			return null;
		}
		//
		return new MLocator(getCtx(), M_Locator_ID, trx);
	} // getMLocator

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return "MLocatorLookup[Size=" + m_lookup.size() + "]";
	} // toString

	/**
	 * Is Locator with key valid (Warehouse)
	 * 
	 * @param key
	 *            key
	 * @return true if valid
	 */
	public boolean isValid(Object key) {
		if (key == null)
			return true;
		// try cache
		KeyNamePair pp = m_lookup.get(key);
		return pp != null;
	} // isValid


	
	private final Callable<LinkedHashMap<Integer, KeyNamePair>> m_loader = new Callable<LinkedHashMap<Integer, KeyNamePair>>() {
		
		@Override
		public LinkedHashMap<Integer, KeyNamePair> call() throws Exception {
			// log.config("MLocatorLookup Loader.run " + m_AD_Column_ID);
			// Set Info - see VLocator.actionText

			LinkedHashMap<Integer, KeyNamePair> lookup = new LinkedHashMap<Integer, KeyNamePair>();
			
			int only_Warehouse_ID = getOnly_Warehouse_ID();
			int only_Product_ID = getOnly_Product_ID();
			Boolean only_IsSOTrx = isOnly_Outgoing();
			m_defaultID = getCtx().get_ValueAsString("#M_Locator_ID");

			/* Set default rcv locator in WMS warehouses */
			if (only_Warehouse_ID != 0 && only_IsSOTrx != null
					&& !only_IsSOTrx.booleanValue()) {
				StringBuffer wmsLocatorSql = new StringBuffer("SELECT *");
				wmsLocatorSql.append(" FROM M_Locator l ");
				wmsLocatorSql.append(" WHERE l.M_Warehouse_ID = ? ");
				wmsLocatorSql
				.append(" AND EXISTS (SELECT 1 FROM M_Warehouse w ");
				wmsLocatorSql
				.append(" WHERE w.M_Warehouse_ID = l.M_Warehouse_ID ");
				wmsLocatorSql
				.append(" AND w.IsWMSEnabled='Y' AND w.M_RcvLocator_ID = l.M_Locator_ID)");

				String finalSql = MRole.getDefault(getCtx(), false)
				.addAccessSQL(wmsLocatorSql.toString(), "M_Locator",
						MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(finalSql,
							(Trx) null);
					pstmt.setInt(1, only_Warehouse_ID);
					rs = pstmt.executeQuery();
					//
					while (rs.next()) {
						MLocator loc = new MLocator(getCtx(), rs, null);
						int M_RcvLocator_ID = loc.getM_Locator_ID();

						if (M_RcvLocator_ID != 0) {
							m_defaultID = Integer.toString(M_RcvLocator_ID);
						}
					}
				} 
				catch (SQLException e) {
					log.log(Level.SEVERE, finalSql, e);
				}
				finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
			}

			StringBuffer sql = new StringBuffer("SELECT * FROM M_Locator ")
			.append(" WHERE IsActive='Y'");
			if (only_Warehouse_ID != 0)
				sql.append(" AND M_Warehouse_ID=?");
			if (only_Product_ID != 0) {
				sql.append(" AND (IsDefault='Y' "); // Default Locator

				// Something already stored
				sql
				.append("OR EXISTS (SELECT * FROM M_StorageDetail s ")
				// Storage Locator
				.append(
				"WHERE s.M_Locator_ID=M_Locator.M_Locator_ID AND s.M_Product_ID=?)");

				if (only_IsSOTrx == null || !only_IsSOTrx.booleanValue()) {
					// Default Product
					sql
					.append("OR EXISTS (SELECT * FROM M_Product p ")
					// Default Product Locator
					.append(
					"WHERE p.M_Locator_ID=M_Locator.M_Locator_ID AND p.M_Product_ID=?)");
					// Product Locators
					sql
					.append(
					"OR EXISTS (SELECT * FROM M_ProductLocator pl ")
					// Product Locator
					.append(
					"WHERE pl.M_Locator_ID=M_Locator.M_Locator_ID AND pl.M_Product_ID=?)");
					// No locators defined for the warehouse
					sql.append("OR 0 = (SELECT COUNT(*) ");
					sql.append("FROM M_ProductLocator pl");
					sql
					.append(" INNER JOIN M_Locator l2 ON (pl.M_Locator_ID=l2.M_Locator_ID) ");
					sql
					.append("WHERE pl.M_Product_ID=? AND l2.M_Warehouse_ID=M_Locator.M_Warehouse_ID )");
					// Added receiving locator for WMS warehouse
					sql.append(" OR EXISTS (SELECT 1 FROM M_Warehouse w ");
					sql
					.append(" WHERE w.M_Warehouse_ID = M_Locator.M_Warehouse_ID ");
					sql
					.append(" AND w.IsWMSEnabled='Y' AND w.M_RcvLocator_ID = M_Locator.M_Locator_ID)");
				}
				sql.append(" ) ");
			}
			String finalSql = MRole.getDefault(getCtx(), false).addAccessSQL(
					sql.toString(), "M_Locator", MRole.SQL_NOTQUALIFIED,
					MRole.SQL_RO);

			// Reset
			lookup.clear();
			int rows = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(finalSql,
						(Trx) null);
				int index = 1;
				if (only_Warehouse_ID != 0)
					pstmt.setInt(index++, only_Warehouse_ID);
				if (only_Product_ID != 0) {
					pstmt.setInt(index++, only_Product_ID);
					if (only_IsSOTrx == null || !only_IsSOTrx.booleanValue()) {
						pstmt.setInt(index++, only_Product_ID);
						pstmt.setInt(index++, only_Product_ID);
						pstmt.setInt(index++, only_Product_ID);
					}
				}
				rs = pstmt.executeQuery();
				//
				while (rs.next()) {
					// Max out
					if (rows++ > s_maxRows) {
						log.warning("Over Max Rows - " + rows);
						break;
					}
					MLocator loc = new MLocator(getCtx(), rs, null);
					int M_Locator_ID = loc.getM_Locator_ID();
					KeyNamePair pp = new KeyNamePair(M_Locator_ID, loc
							.toString());
					lookup.put(Integer.valueOf(M_Locator_ID), pp);
					// pick the default. At the very least, set default as first
					// row returned
					if (m_defaultID.length() == 0
							&& (loc.isDefault() || rows == 1)) {
						m_defaultID = Integer.toString(M_Locator_ID);
					}
				}
			} 
			catch (SQLException e) {
				log.log(Level.SEVERE, finalSql, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			log.fine("Complete #" + lookup.size());
			if (lookup.size() == 0)
				log.finer(finalSql);

			return lookup;
		}
	};
	
	/**
	 * Return info as ArrayList containing Locator, waits for the loader to
	 * finish
	 * 
	 * @return Collection of lookup values
	 */
	public Collection<KeyNamePair> getData() {
		loadComplete();
		return m_lookup.values();
	} // getData

	/**
	 * Return data as sorted ArrayList
	 * 
	 * @param mandatory
	 *            mandatory
	 * @param onlyValidated
	 *            only validated
	 * @param onlyActive
	 *            only active
	 * @param temporary
	 *            force load for temporary display
	 * @return ArrayList of lookup values
	 */
	@Override
	public ArrayList<NamePair> getData(boolean mandatory,
			boolean onlyValidated, boolean onlyActive, boolean temporary) {
		// create list
		Collection<KeyNamePair> collection = getData();
		ArrayList<NamePair> list = new ArrayList<NamePair>(
				collection.size() + 1);
		if (!mandatory)
			list.add(new KeyNamePair(-1, ""));
		list.addAll(collection);

		/**
		 * Sort Data MLocator l = new MLocator (m_ctx, 0); if (!mandatory)
		 * list.add (l); Collections.sort (list, l);
		 **/
		return list;
	} // getArray

	/**
	 * Refresh Values
	 * 
	 * @return new size of lookup
	 */
	@Override
	public int refresh() {
		log.fine("start");
		m_result = s_exec.submit(m_loader);
		loadComplete();
		log.info("#" + m_lookup.size());
		return m_lookup.size();
	} // refresh

	/**
	 * Get underlying fully qualified Table.Column Name
	 * 
	 * @return Table.ColumnName
	 */
	@Override
	public String getColumnName() {
		return "M_Locator.M_Locator_ID";
	} // getColumnName

} // MLocatorLookup
