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
package org.compiere.controller;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.*;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * User Interface Field
 * 
 * @author Jorg Janke
 * @version $Id: UIField.java 8751 2010-05-12 16:49:31Z nnayak $
 */
public class UIField extends UIFieldVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * UIField
	 * 
	 * @param vo
	 */
	public UIField(UIFieldVO vo) {
		super(vo);
	} // UIField

	/** Logger */
	private static CLogger log = CLogger.getCLogger(UIField.class);
	/** Error Status */
	private boolean m_error = false;
	/** Parent Check */
	private Boolean m_parentValue = null;

	/** Field depends on the following fields for UI */
	private final ArrayList<String> m_dependsOnUI = new ArrayList<String>();
	/** Field depends on the following fields for Value */
	private final ArrayList<String> m_dependsOnValue = new ArrayList<String>();
	/** Field impacts the following fields for UI */
	private final ArrayList<String> m_impactsUI = new ArrayList<String>();
	/** Field impacts the following fields for Value */
	private final ArrayList<String> m_impactsValue = new ArrayList<String>();
	/** Field value impacts Tab UI */
	private boolean m_impactsUITab = false;
	/** Field Mnemonic */
	private char m_mnemonic = 0;

	/**
	 * Get Sort No
	 * 
	 * @return sort number ASC/DESC
	 */
	public int getSortNoAsInt() {
		String value = get("SortNo");
		if (value == null)
			return 0;
		return Integer.parseInt(value);
	} // getSortNoAsInt

	/**
	 * Is Virtual Column
	 * 
	 * @return true if it has column sql
	 */
	public boolean isVirtualColumn() {
		String s = getColumnSQL();
		return (s != null) && (s.length() > 0);
	} // isVirtualColumn

	/**
	 * Is Field Mandatory
	 * 
	 * @param ctx
	 *            optional checks Context
	 * @return true if mandatory
	 */
	public boolean isMandatory(CContext ctx, int WindowNo) {
		String logic = getMandatoryLogic();

		if ((logic != null) && (logic.length() > 0)) {
			// TODO fix context
			if (Evaluator.evaluateLogic(ctx.getCtx(WindowNo), logic))
				return true;
		}

		if (!super.isMandatoryUI() || isVirtualColumn())
			return false;

		// Numeric Keys and Created/Updated as well as
		// DocumentNo/Value/ASI are not mandatory (persistency layer manages
		// them)
		String columnName = getColumnName();
		if ((isKey() && columnName.toUpperCase().endsWith("_ID"))
				|| columnName.startsWith("Created")
				|| columnName.startsWith("Updated")
				|| columnName.equals("Value")
				|| columnName.equals("DocumentNo")
				|| columnName.equals("M_AttributeSetInstance_ID")) // 0 is valid
			return false;

		// Mandatory Logic

		// Mandatory only if displayed
		return isDisplayed(ctx, WindowNo);
	} // isMandatory

	/**
	 * Field is Displayed
	 * 
	 * @param ctx
	 *            optional checks context
	 * @return true if displayed
	 */
	public boolean isDisplayed(CContext ctx, int WindowNo) {
		// ** static content **
		// not displayed
		if (!isDisplayed())
			return false;
		// no restrictions
		String logic = getDisplayLogic();
		if ((logic == null) || logic.equals(""))
			return true;

		// ** dynamic content **
		if (ctx != null) {
			// TODO fix context
			boolean retValue = Evaluator.evaluateLogic(ctx.getCtx(WindowNo),
					logic);
			log.finest(getColumnName() + " (" + logic + ") => " + retValue);
			return retValue;
		}
		return true;
	} // isDisplayed

	/**
	 * Is it Editable - checks IsActive, IsUpdateable, and isDisplayed
	 * 
	 * @param ctx
	 *            optional checks context for Active, IsProcessed, LinkColumn
	 * @param WindowNo
	 *            window
	 * @param tab
	 *            sibling tab
	 * @param inserting
	 * @return true, if editable
	 */
	public boolean isEditable(CContext ctx, int WindowNo, UITab tab,
			boolean inserting) {
		if (isVirtualColumn())
			return false;
		String columnName = getColumnName();
		int displayType = getAD_Reference_ID();
		// Fields always enabled (are usually not updatable)
		if (columnName.equals("Posted")
				|| (columnName.equals("Record_ID") && (displayType == DisplayTypeConstants.Button))) // Zoom
			return true;

		// Fields always updatable
		if (isAlwaysUpdateable()) // Zoom
			return true;

		// Tab or field is R/O
		if (tab.isReadOnly() || isReadOnly()) {
			log.finest(columnName + " NO - TabRO=" + tab.isReadOnly()
					+ ", FieldRO=" + isReadOnly());
			return false;
		}

		// Not Updatable - only editable if new updatable row
		if (!isUpdateable() && !inserting) {
			log.finest(columnName + " NO - FieldUpdateable=" + isUpdateable());
			return false;
		}

		// Field is the Link Column of the tab
		String linkColumn = tab.getLinkColumnName();
		if (columnName.equals(linkColumn)) {
			log.finest(columnName + " NO - LinkColumn");
			return false;
		}

		// Role Access & Column Access
		if (ctx != null) {
			int AD_Client_ID = ctx.getAD_Client_ID(WindowNo);
			int AD_Org_ID = ctx.getAD_Org_ID(WindowNo);
			String keyColumn = tab.getKeyColumnName(); // assumes single key
			if ("EntityType".equals(keyColumn))
				keyColumn = "AD_EntityType_ID";
			if (!keyColumn.toUpperCase().endsWith("_ID"))
				keyColumn += "_ID"; // AD_Language_ID
			int Record_ID = ctx.getContextAsInt(keyColumn);
			int AD_Table_ID = getAD_Table_ID();
			MRole role = MRole.getDefault(ctx, false);
			if (!role.canUpdate(AD_Client_ID, AD_Org_ID, AD_Table_ID,
					Record_ID, false))
				return false;
			if (!role.isColumnAccess(AD_Table_ID, getAD_Column_ID(), false))
				return false;
		}

		// Do we have a readonly rule
		String logic = getReadOnlyLogic();
		if ((ctx != null) && (logic != null) && (logic.length() > 0)) {
			// TODO fix context
			boolean retValue = !Evaluator.evaluateLogic(ctx.getCtx(WindowNo),
					logic);
			log.finest(columnName + " R/O(" + logic + ") => R/W-" + retValue);
			if (!retValue)
				return false;
		}

		// Always editable if Active
		if (columnName.equals("Processing") || columnName.equals("DocAction")
				|| columnName.equals("GenerateTo"))
			return true;

		// Record is Processed ***
		if ((ctx != null)
				&& (ctx.isProcessed(WindowNo) || ctx.isProcessing(WindowNo)))
			return false;

		// IsActive field is editable, if record not processed
		if (columnName.equals("IsActive"))
			return true;

		// Record is not Active
		if ((ctx != null) && !ctx.isActive(WindowNo))
			return false;

		// ultimately visibility decides
		return isDisplayed(ctx, WindowNo);
	} // isEditable

	/**
	 * Does the Role in the context have r/w access to this column?
	 * 
	 * @param ctx
	 *            context for role
	 * @return true if access
	 */
	public boolean isColumnAccess(Ctx ctx) {
		MRole role = MRole.getDefault(ctx, false);
		return role.isColumnAccess(getAD_Table_ID(), getAD_Column_ID(), false);
	} // isColumnAccess

	/**
	 * Is this field a Lookup?.
	 * 
	 * @return true if lookup field
	 */
	public boolean isLookup() {
		boolean retValue = false;
		// String columnName = getColumnName();
		int displayType = getAD_Reference_ID();
		if (isKey())
			retValue = false;
		// else if (columnName.equals("CreatedBy") ||
		// columnName.equals("UpdatedBy"))
		// retValue = false;
		else if (FieldType.isLookup(displayType))
			retValue = true;
		else if ((displayType == DisplayTypeConstants.Location)
				|| (displayType == DisplayTypeConstants.Locator)
				|| (displayType == DisplayTypeConstants.Account)
				|| (displayType == DisplayTypeConstants.PAttribute))
			retValue = true;

		return retValue;
	} // isLookup

	public boolean isButtonLookup() {
		if (getAD_Reference_ID() == DisplayTypeConstants.Button) { // not cached
			if (getColumnName().toUpperCase().endsWith("_ID")
					&& !getColumnName().equals("Record_ID"))
				return true;
			else if (getAD_Reference_Value_ID() != 0)
				return true;
		}
		return false;

	}

	/**
	 * Initialize Field
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window
	 * @return true if lookup created, false normally
	 */
	public boolean initialize(CContext ctx, int WindowNo) {
		m_lookup = createAllLookup(ctx, WindowNo, true);
		createDependsOnLists();
		log.finer(toString());
		return m_lookup != null;
	} // initialize

	/** Lookup */
	private Lookup m_lookup = null;
	/** Lookup-Info */
	private MLookupInfo m_lookupInfo = null;

	/**
	 * Create Lookup
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window
	 * @param assignInfo
	 *            TODO
	 * @return true if created
	 */
	private Lookup createAllLookup(Ctx ctx, int WindowNo, boolean assignInfo) {
		Lookup lookup = null;
		if (!isLookup() && !isButtonLookup())
			return null;
		String columnName = getColumnName();
		int displayType = getAD_Reference_ID();
		log.config("(" + columnName + ")");
		MLookupInfo lookupInfo = null;
		//
		if (FieldType.isLookup(displayType)) {
			// Lookup Info
			MLookup ml = new MLookup(ctx, WindowNo, displayType);
			lookupInfo = MLookupFactory
					.getLookupInfo(ml, getAD_Column_ID(), Env.getLanguage(ctx),
							columnName, getAD_Reference_Value_ID(), isParent(),
							getValidationCode());
			if (lookupInfo == null) {
				log.warning("(" + columnName + ") - No LookupInfo");
				return null;
			}
			// Prevent loading of CreatedBy/UpdatedBy
			if ((displayType == DisplayTypeConstants.Table)
					&& (columnName.equals("CreatedBy") || columnName
							.equals("UpdatedBy"))) {
				lookupInfo.IsCreadedUpdatedBy = true;
				ml.setDisplayType(DisplayTypeConstants.Search);
			}
			//
			lookupInfo.IsKey = isKey();
			lookup = ml.initialize(lookupInfo, false);
		} else if (displayType == DisplayTypeConstants.Location) // not cached
		{
			MLocationLookup ml = new MLocationLookup(ctx, WindowNo);
			lookup = ml;
		} else if (displayType == DisplayTypeConstants.Locator) {
			MLocatorLookup ml = new MLocatorLookup(ctx, WindowNo);
			lookupInfo = MLookupFactory
					.getLookupInfo(ml, getAD_Column_ID(), Env.getLanguage(ctx),
							columnName, getAD_Reference_Value_ID(), isParent(),
							getValidationCode());
			m_lookupInfo = lookupInfo;
			lookup = ml;
			// setDefaultValue(ml.getDefault());
			setLocatorProperties(lookup, ctx, WindowNo, columnName);
			lookup.refresh();
		} else if (displayType == DisplayTypeConstants.Account) // not cached
		{
			MAccountLookup ma = new MAccountLookup(ctx, WindowNo);
			lookup = ma;
		} else if (displayType == DisplayTypeConstants.PAttribute) // not cached
		{
			MPAttributeLookup pa = new MPAttributeLookup(ctx, WindowNo);
			lookup = pa;
		} else if (displayType == DisplayTypeConstants.Button) { // not cached

			if (getColumnName().toUpperCase().endsWith("_ID")
					&& !getColumnName().equals("Record_ID")) {
				lookup = MLookupFactory.get(ctx, WindowNo, getAD_Column_ID(),
						DisplayTypeConstants.Search, false);
			} else if (getAD_Reference_Value_ID() != 0) {
				// Assuming List
				lookup = MLookupFactory.get(ctx, WindowNo, getAD_Column_ID(),
						DisplayTypeConstants.List, false);
			}
		}
		if (assignInfo)
			m_lookupInfo = lookupInfo;
		return lookup;
	} // loadLookup

	/**
	 * Create DependsOn Lists
	 */
	private void createDependsOnLists() {
		String displayLogic = getDisplayLogic();
		String readOnlyLogic = getReadOnlyLogic();
		String mandatoryLogic = getMandatoryLogic();
		// String validationCode = getValidationCode();

		String columnName = getColumnName();
		int displayType = getAD_Reference_ID();

		// Implicit Value Dependencies
		if (columnName.equals("M_AttributeSetInstance_ID"))
			m_dependsOnValue.add("M_Product_ID");
		else if (columnName.equals("M_Locator_ID")
				|| columnName.equals("M_LocatorTo_ID")) {
			if (displayType == DisplayTypeConstants.Locator)
				m_dependsOnValue.add("M_Product_ID");
			m_dependsOnValue.add("M_Warehouse_ID");
		}
		// Value Dependencies
		if (m_lookup != null) // getValidationCode()
			Evaluator.parseDepends(m_dependsOnValue, m_lookup.getValidation());

		// Display dependent
		Evaluator.parseDepends(m_dependsOnUI, displayLogic);
		Evaluator.parseDepends(m_dependsOnUI, readOnlyLogic);
		Evaluator.parseDepends(m_dependsOnUI, mandatoryLogic);

		// Debug
		if (CLogMgt.isLevelFiner()) {
			if (m_dependsOnValue.size() > 0)
				log.finer("(" + columnName + ") Value "
						+ m_dependsOnValue.toString());
			if (m_dependsOnUI.size() > 0)
				log.finer("(" + columnName + ") UI - "
						+ m_dependsOnUI.toString());
		}
	} // createDependsOnLists

	/**
	 * Wait until Load is complete
	 */
	public void lookupLoadComplete() {
		if (m_lookup == null)
			return;
		m_lookup.loadComplete();
	} // loadCompete

	private void setLocatorProperties(Lookup lookup, Ctx ctx, int windowNo,
			String columnName) {

		MLocatorLookup ml = (MLocatorLookup) lookup;
		try {
			String warehouseID = ctx.getContext(windowNo, "M_Warehouse_ID",
					true);
			if (warehouseID.equals(""))
				ml.setOnly_Warehouse_ID(0);
			else
				ml.setOnly_Warehouse_ID(Integer.parseInt(warehouseID));
		} catch (Exception e) {
		}
		try {
			String productID = ctx.getContext(windowNo, "M_Product_ID", true);
			if (productID.equals(""))
				ml.setOnly_Product_ID(0);
			else
				ml.setOnly_Product_ID(Integer.parseInt(productID));

		} catch (Exception e) {
		}

		boolean IsReturnTrx = "Y".equals(ctx
				.getContext(windowNo, "IsReturnTrx"));
		boolean IsSOTrx = ctx.isSOTrx(windowNo);
		boolean isOnlyOutgoing = ((IsSOTrx && !IsReturnTrx) || (!IsSOTrx && IsReturnTrx))
				&& columnName.equals("M_Locator_ID");
		ml.setOnly_Outgoing(isOnlyOutgoing);
	}

	/**
	 * Get Lookup Data
	 * 
	 * @param ctx
	 *            context
	 * @param windowNo
	 *            window
	 * @return list
	 */
	public ArrayList<NamePair> getAllLookupData(Ctx ctx, int windowNo) {
		// Explicitly recreate lookups since the old lookup info may already
		// have hard-bound variable values in the where clause.
		// See bug 10019306.
		Lookup lookup = createAllLookup(ctx, windowNo, false);

		if (lookup == null)
			return null;
		boolean onlyValidated = true;
		boolean onlyActive = true;
		boolean temporary = true; // clean list
		String columnName = getColumnName();
		lookup.setContext(ctx, windowNo);

		int displayType = this.getAD_Reference_ID();
		if (displayType == DisplayTypeConstants.Locator)
			setLocatorProperties(lookup, ctx, windowNo, columnName);

		//we don't need to fillComboBox 'since getData does the loading. fillComboBox actually calls getData, no need
//		lookup.fillComboBox(isMandatory(), onlyValidated, onlyActive,
//						temporary);
		ArrayList<NamePair> list = lookup.getData(isMandatory(), onlyValidated,
				onlyActive, temporary);
		if (list == null) {
			log.warning("No Values: " + columnName);
			list = new ArrayList<NamePair>();
		} else
			log.finest("#" + list.size());
		return list;
	} // getLookupData

	/**
	 * Get Lookup
	 * 
	 * @return lookup if exists
	 */
	public Lookup getLookup() {
		return m_lookup;
	} // getLookup

	/**
	 * Get Lookup Info
	 * 
	 * @return lookup_info if exists
	 */
	public MLookupInfo getLookupInfo() {
		return m_lookupInfo;
	} // getLookup

	/**
	 * Get Lookup Data
	 * 
	 * @param ctx
	 *            context
	 * @param windowNo
	 *            window
	 * @param key
	 *            the string key value
	 * @return display or null if not found
	 */
	public String getLookupDisplay(CContext ctx, int windowNo, String key) {
		return getLookupDisplay(ctx, windowNo, key, true);
	} // getLookupData

	/**
	 * Get Lookup Data
	 * 
	 * @param ctx
	 *            context
	 * @param windowNo
	 *            window
	 * @param key
	 *            the string key value
	 * @param whether
	 *            read from cache or from table
	 * @return display or null if not found
	 */
	public String getLookupDisplay(CContext ctx, int windowNo, String key,
			boolean cache) {
		if (m_lookup == null)
			return null;
		Object lookupKey = key;
		if ((key == null) || (key.length() == 0))
			return null;
		if (getColumnName().toUpperCase().endsWith("_ID"))
			lookupKey = Integer.valueOf(key);
		//
		m_lookup.setContext(ctx, windowNo);
		NamePair pp = m_lookup.getDirect(lookupKey, false, cache);
		if (pp != null)
			return pp.getName();
		return null;
	} // getLookupData

	/**************************************************************************
	 * Create default value.
	 * 
	 * <pre>
	 * 	(a) Key/Parent/IsActive/SystemAccess
	 *      (b) SQL Default
	 * 	(c) Column Default		//	system integrity
	 *      (d) User Preference
	 * 	(e) System Preference
	 * 	(f) DataType Defaults
	 *  Don't default from Context =&gt; use explicit defaultValue
	 *  (would otherwise copy previous record)
	 * </pre>
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window
	 * @param tab
	 *            the containing tab
	 * @return default value or null
	 */
	public Object getDefault(CContext ctx, int WindowNo, UITab tab) {
		int displayType = getAD_Reference_ID();
		String DefaultValue = getDefaultValue();
		String ColumnName = getColumnName();

		/**
		 * (a) Key/Parent/IsActive/SystemAccess
		 */
		// No defaults for these fields
		if (isKey() || (displayType == DisplayTypeConstants.RowID)
				|| FieldType.isLOB(displayType))
			return null;
		// Set Parent to context if not explicitly set
		if (isParentValue(tab) && Util.isEmpty(DefaultValue)) {
			String parent = ctx.getContext(WindowNo, ColumnName);
			log.fine("[Parent] " + ColumnName + "=" + parent);
			return createDefault(ColumnName, parent);
		}
		// Always Active
		if (ColumnName.equals("IsActive")) {
			log.fine("[IsActive] " + ColumnName + "=Y");
			return "Y";
		}

		// Set Client & Org to System, if System access
		String AccessLevel = tab.getAccessLevel();
		if (X_AD_Table.ACCESSLEVEL_SystemOnly.equals(AccessLevel)
				&& (ColumnName.equals("AD_Client_ID") || ColumnName
						.equals("AD_Org_ID"))) {
			log.fine("[SystemAccess] " + ColumnName + "=0");
			return Integer.valueOf(0);
		}
		// Set Org to System, if Client access
		else if (X_AD_Table.ACCESSLEVEL_SystemPlusTenant.equals(AccessLevel)
				&& ColumnName.equals("AD_Org_ID")) {
			log.fine("[ClientAccess] " + ColumnName + "=0");
			return Integer.valueOf(0);
		}

		/**
		 * (b) SQL Statement (for data integity & consistency)
		 */
		String defStr = "";
		if (DefaultValue.startsWith("@SQL=")) {
			String sql0 = DefaultValue.substring(5); // w/o tag
			String sql = Env.parseContext(ctx, WindowNo, sql0, false, true); // replace
																				// variables
			String sqlTest = sql.toUpperCase();
			if ((sqlTest.indexOf("DELETE ") != -1)
					&& (sqlTest.indexOf("UPDATE ") != -1)
					&& (sqlTest.indexOf("DROP ") != -1))
				sql = ""; // Potential security issue
			if (sql.equals("")) {
				log.log(Level.WARNING, "(" + ColumnName
						+ ") - Default SQL variable parse failed: "
						+ DefaultValue);
			} 
			else {
				PreparedStatement stmt = null;
				ResultSet rs = null;
				try {
					stmt = DB.prepareStatement(sql,	(Trx) null);
					rs = stmt.executeQuery();
					if (rs.next())
						defStr = rs.getString(1);
					else
						log.log(Level.WARNING, "(" + ColumnName
								+ ") - no Result: " + sql);
				} 
				catch (SQLException e) {
					if (sql.endsWith("=")) // Variable Resolved empty
						log
								.log(Level.SEVERE, "(" + ColumnName + ") "
										+ sql0, e);
					else
						log
								.log(Level.WARNING, "(" + ColumnName + ") "
										+ sql, e);
				}
				finally {
					DB.closeResultSet(rs);
					DB.closeStatement(stmt);
				}
			}
			if ((defStr != null) && (defStr.length() > 0)) {
				log.fine("[SQL] " + ColumnName + "=" + defStr);
				return createDefault("", defStr);
			}
		} // SQL Statement

		/**
		 * (c) Field DefaultValue === similar code in AStartRPDialog.getDefault
		 * ===
		 */
		if (!DefaultValue.equals("") && !DefaultValue.startsWith("@SQL=")) {
			defStr = ""; // problem is with texts like 'sss;sss'
			// It is one or more variables/constants
			StringTokenizer st = new StringTokenizer(DefaultValue, ",;", false);
			while (st.hasMoreTokens()) {
				String variable = st.nextToken().trim();
				if (variable.equals("@SysDate@") || variable.equals("@Now@")) // System
																				// Time
					return new Timestamp(System.currentTimeMillis());
				else if (variable.indexOf('@') != -1) // it is a variable
					defStr = ctx.getContext(WindowNo, variable
							.replace('@', ' ').trim());
				else if (defStr.indexOf("'") != -1) // it is a 'String'
					defStr = variable.replace('\'', ' ').trim();
				else
					defStr = variable;

				if (defStr.length() > 0) {
					log.fine("[DefaultValue] " + ColumnName + "=" + defStr);
					return createDefault(variable, defStr);
				}
			} // while more Tokens
		} // Default value

		// No default for Dependent fields of IDs (if defined - assumed to be
		// correct)
		//leave it for client to decide whether dependent fields has value or not
//		if ((m_lookup != null) && !Util.isEmpty(m_lookup.getValidation())) {
//			String code = m_lookup.getValidation();
//			ArrayList<String> vars = Evaluator.getVariables(code);
//			boolean setNull = false;
//			for (String var : vars) {
//				String ctxValue = ctx.getContext(WindowNo, var);
//				if (!var.startsWith("#") && var.endsWith("_ID")
//						&& !var.equals(ColumnName)) {
//					setNull = Util.isEmpty(ctxValue);
//					if (setNull)
//						break;
//				}
//			}
//			if (setNull) {
//				if (CLogMgt.isLevelFiner())
//					log.fine("[Dependent] " + ColumnName + "=NULL - " + code);
//				else
//					log.fine("[Dependent] " + ColumnName + "=NULL");
//				return null;
//			}
//		} // dependent

		/**
		 * (d) Preference (user) - P|
		 */
		int AD_Window_ID = tab.getAD_Window_ID();
		defStr = Env.getPreference(ctx, AD_Window_ID, ColumnName, false);
		if (!defStr.equals("")) {
			log.fine("[UserPreference] " + ColumnName + "=" + defStr);
			return createDefault("", defStr);
		}

		/**
		 * (e) Preference (System) - # $
		 */
		defStr = Env.getPreference(ctx, AD_Window_ID, ColumnName, true);
		if (!defStr.equals("")) {
			log.fine("[SystemPreference] " + ColumnName + "=" + defStr);
			return createDefault("", defStr);
		}

		/**
		 * (f) DataType defaults
		 */

		// Button to N
		if ((displayType == DisplayTypeConstants.Button)
				&& !ColumnName.toUpperCase().endsWith("_ID")) {
			log.fine("[Button=N] " + ColumnName);
			return "N";
		}
		// CheckBoxes default to No
		if (displayType == DisplayTypeConstants.YesNo) {
			log.fine("[YesNo=N] " + ColumnName);
			return "N";
		}
		// lookups with one value
		// if (DisplayType.isLookup(displayType) && m_lookup.getSize() == 1)
		// {
		// /** @todo default if only one lookup value */
		// }
		// IDs remain null
		if (ColumnName.toUpperCase().endsWith("_ID")) {
			log.fine("[ID=null] " + ColumnName);
			return null;
		}
		// actual Numbers default to zero
		if (FieldType.isNumeric(displayType)) {
			// dzhao, can't return null
			// if(DefaultValue == null)
			// return null;
			// dzhao: don't default to 0, default to DefaultValue.. if not, then
			// send null back!
			log.fine("[Number=0] " + ColumnName);
			return createDefault("", "0");
		}

		/**
		 * No resolution
		 */
		log.fine("[NONE] " + ColumnName);
		return null;
	} // getDefault

	/**
	 * Create Default Object type.
	 * 
	 * <pre>
	 * 	Integer 	(IDs, Integer)
	 * 	BigDecimal 	(Numbers)
	 * 	Timestamp	(Dates)
	 * 	Boolean		(YesNo)
	 * 	default: String
	 * </pre>
	 * 
	 * @param variable
	 *            variable
	 * @param value
	 *            string
	 * @return type dependent converted object
	 */
	private Object createDefault(String variable, String value) {
		// true NULL
		if ((value == null) || (value.toString().length() == 0))
			return null;
		int displayType = getAD_Reference_ID();
		String ColumnName = getColumnName();
		// see also MTable.readData
		try {
			// IDs & Integer & CreatedBy/UpdatedBy
			if (ColumnName.endsWith("atedBy") || ColumnName.toUpperCase().endsWith("_ID")) {
				try // defaults -1 => null
				{
					int ii = Integer.parseInt(value);
					if (ii < 0)
						return null;
					return Integer.valueOf(ii);
				} catch (Exception e) {
					log.warning("Cannot parse: " + value + " - "
							+ e.getMessage());
				}
				return Integer.valueOf(0);
			}
			// Integer
			if (displayType == DisplayTypeConstants.Integer)
				return Integer.valueOf(value);

			// Number
			if (FieldType.isNumeric(displayType))
				return new BigDecimal(value);

			// Timestamps
			if (FieldType.isDate(displayType)) {
				// Time
				try {
					long time = Long.parseLong(value);
					return new Timestamp(time);
				} catch (Exception e) {
				}
				// Date yyyy-mm-dd hh:mm:ss.fffffffff
				String tsString = value
						+ "2007-01-01 00:00:00.000000000".substring(value
								.length());
				try {
					return Timestamp.valueOf(tsString);
				} catch (Exception e) {
					log.warning("Cannot convert to Timestamp: " + tsString);
				}
				return new Timestamp(System.currentTimeMillis());
			}

			// Boolean
			if (displayType == DisplayTypeConstants.YesNo)
				return Boolean.valueOf("Y".equals(value));

			// Default - String
			if (variable.equals("@#Date@")) {
				try { // 2007-06-27 00:00:00.0
					long time = Long.parseLong(value);
					value = new Timestamp(time).toString();
					value = value.substring(0, 10);
				} catch (Exception e) {
				}
			}
			return value;
		} catch (Exception e) {
			log.log(Level.SEVERE, ColumnName + " - " + e.getMessage());
		}
		return null;
	} // createDefault

	/**
	 * Get Default As String
	 * 
	 * @param ctx
	 *            context
	 * @param WindowNo
	 *            window no
	 * @param tab
	 *            tab
	 * @return default value
	 */
	public String getDefaultAsString(CContext ctx, int WindowNo, UITab tab) {
		Object retValue = getDefault(ctx, WindowNo, tab);
		return convertToString(retValue);
	} // getDefaultAsString

	/**
	 * Convert to BigDecimal. Checks also data type.
	 * 
	 * @param stringValue
	 *            value
	 * @return big decimal or null if error
	 */
	public BigDecimal convertToBigDecimal(String stringValue) {
		if (stringValue == null)
			return null;
		int displayType = getAD_Reference_ID();
		if (!FieldType.isNumeric(displayType)) {
			log.warning(getColumnName() + " not BigDecimal (" + stringValue
					+ ")");
			return null;
		}
		try {
			return new BigDecimal(stringValue);
		} catch (Exception e) {
			log.warning(getColumnName() + ": " + stringValue + " - "
					+ e.toString());
		}
		return null;
	} // convertToBigDecimal

	/**
	 * Convert to Integer Checks also data type.
	 * 
	 * @param stringValue
	 *            value
	 * @return integer or 0 if error
	 */
	public int convertToInteger(String stringValue) {
		if (stringValue == null)
			return 0;
		int displayType = getAD_Reference_ID();
		if (!(FieldType.isID(displayType) || (displayType == DisplayTypeConstants.Integer))) {
			log.warning(getColumnName() + " not Integer (" + stringValue + ")");
			return 0;
		}
		try {
			return Integer.parseInt(stringValue);
		} catch (Exception e) {
			log.warning(getColumnName() + ": " + stringValue + " - "
					+ e.toString());
		}
		return 0;
	} // convertToInteger

	/**
	 * Convert to Timestamp Checks also data type.
	 * 
	 * @param stringValue
	 *            value
	 * @return integer or null if error
	 */
	public Timestamp convertToTimestamp(String stringValue) {
		if (stringValue == null)
			return null;
		int displayType = getAD_Reference_ID();
		if (!FieldType.isDate(displayType)) {
			log.warning(getColumnName() + " not Date (" + stringValue + ")");
			return null;
		}
		try // CurrentTime format
		{
			long time = Long.parseLong(stringValue);
			return new Timestamp(time);
		} catch (Exception e) {
			try // JDBC format
			{
				return Timestamp.valueOf(stringValue);
			} catch (Exception e2) {
				log.warning(getColumnName() + ": " + stringValue + " - "
						+ e2.toString());
			}
		}
		return null;
	} // convertToTimestamp

	/**
	 * Convert to String
	 * 
	 * @param value
	 *            value
	 * @return String or ""
	 */
	public String convertToString(Object value) {
		if (value == null)
			return "";
		if (value instanceof Date) {
			long time = ((Date) value).getTime();
			return String.valueOf(time);
		} else if (value instanceof Boolean) {
			if (value.equals(Boolean.FALSE))
				return "N";
			else
				return "Y";
		}
		return value.toString();
	} // convertToString

	/**
	 * Parent Link Value
	 * 
	 * @param tab
	 *            tab
	 * @return parent value
	 */
	public boolean isParentValue(UITab tab) {
		if (m_parentValue != null)
			return m_parentValue.booleanValue();
		if (!FieldType.isID(getAD_Reference_ID()) || (tab.getTabNo() == 0))
			m_parentValue = Boolean.FALSE;
		else {
			String LinkColumnName = tab.getLinkColumnName();
			String ColumnName = getColumnName();
			if (LinkColumnName.length() == 0)
				m_parentValue = Boolean.FALSE;
			else
				m_parentValue = Boolean.valueOf(ColumnName
						.equals(LinkColumnName));
		}
		return m_parentValue.booleanValue();
	} // isParentValue

	/**
	 * Get the list of column names this field Depends On for UI. (Display,
	 * ReadOnly, Mandatory)
	 * 
	 * @return list of columns
	 */
	public ArrayList<String> getDependsOnUI() {
		return m_dependsOnUI;
	} // getDependsOnUI

	/**
	 * Get the list of column names this field Depends On for Value. (e.g.
	 * Business Partner for a Location field)
	 * 
	 * @return list of columns
	 */
	public ArrayList<String> getDependsOnValue() {
		return m_dependsOnValue;
	} // getDependsOnValue

	/**
	 * Field is Dependent on others for UI
	 * 
	 * @return true if dependent
	 */
	public boolean isDependentUI() {
		return m_dependsOnUI.size() > 0;
	} // isDependentUI

	/**
	 * Field is Dependent on other fields for Value
	 * 
	 * @return true if dependent
	 */
	public boolean isDependentValue() {
		return m_dependsOnValue.size() > 0;
	} // isDependentValue

	/**
	 * Add Column name Impacting UI of this column
	 * 
	 * @param columnName
	 *            column name
	 */
	protected void addImpactsUIColumn(String columnName) {
		if ((columnName != null) && (columnName.length() > 0)) {
			if (!m_impactsUI.contains(columnName))
				m_impactsUI.add(columnName);
		}
	} // addImpactUIColumn

	/**
	 * Get the list of column names this field impacts their UI. (Display,
	 * ReadOnly, Mandatory)
	 * 
	 * @return list of columns
	 */
	public ArrayList<String> getImpactsUI() {
		return m_impactsUI;
	} // getImpactsUI

	/**
	 * Add Column name Impacting Value of this column
	 * 
	 * @param columnName
	 *            column name
	 */
	protected void addImpactsValueColumn(String columnName) {
		if ((columnName != null) && (columnName.length() > 0)) {
			if (!m_impactsValue.contains(columnName))
				m_impactsValue.add(columnName);
		}
	} // addImpactsValueColumn

	/**
	 * Get the list of column names this field impacts their Value.
	 * 
	 * @return list of columns
	 */
	public ArrayList<String> getImpactsValue() {
		return m_impactsValue;
	} // getImpactsUI

	/**
	 * Set Tab UI is impacted by column
	 * 
	 * @param impacts
	 *            true if tab impacted
	 */
	protected void setImpactsUITab(boolean impacts) {
		m_impactsUITab = impacts;
	} // isImpactsUITab

	/**
	 * Columns impact Tab UI
	 * 
	 * @return true if column impacts
	 */
	public boolean isImpactsUITab() {
		return m_impactsUITab;
	} // isImpactsUITab

	/**
	 * Has Impact when value changes.
	 * 
	 * @return true if field has fields it impacts or callouts
	 */
	public boolean isImpactsValue() {
		String callOut = getCallout();
		return (m_impactsValue.size() > 0) || isCallout()
				|| ((callOut != null) && (callOut.length() > 0));
	} // isImpactsValue

	/**
	 * Has Impact on UI. Check also isImpactTab
	 * 
	 * @return true if field impacts UI
	 */
	public boolean isImpactsUI() {
		return m_impactsUI.size() > 0;
	} // isImpactsValue

	/**
	 * Set Value As String
	 * 
	 * @param newValue
	 *            new
	 * @param oldValue
	 *            old
	 * @return valid value if possible or null
	 */
	public String validateValueAsString(String oldValue, String newValue) {
		// Mandatory set back to old
		if ((newValue == null) && isMandatoryUI()) {
			if (oldValue != null)
				return oldValue;
		}
		if (newValue == null)
			return null;
		//

		return newValue;
	} // setValueAsString

	/**
	 * Get Label (Name w/o Mnemonic)
	 * 
	 * @return label
	 */
	public String getLabel() {
		String s = getName();
		s = Util.cleanMnemonic(s);
		return s;
	} // getLabel

	/**
	 * Create Mnemonic for field
	 * 
	 * @return no for r/o, client, org, document no
	 */
	protected boolean isCreateMnemonic() {
		String columnName = getColumnName();
		if (isReadOnly() || columnName.equals("AD_Client_ID")
				|| columnName.equals("AD_Org_ID")
				|| columnName.equals("DocumentNo"))
			return false;
		return true;
	} // isCreateMnemonic

	/**
	 * Get Label Mnemonic
	 * 
	 * @return Mnemonic
	 */
	public char getMnemonic() {
		return m_mnemonic;
	} // getMnemonic

	/**
	 * Set Label Mnemonic
	 * 
	 * @param mnemonic
	 *            Mnemonic
	 */
	protected void setMnemonic(char mnemonic) {
		m_mnemonic = mnemonic;
	} // setMnemonic

	/**************************************************************************
	 * Set Error. Used by editors to set the color
	 * 
	 * @param error
	 *            true if error
	 */
	public void setError(boolean error) {
		m_error = error;
	} // setBackground

	/**
	 * Get Background Error.
	 * 
	 * @return error
	 */
	public boolean isError() {
		return m_error;
	} // isError

	/**
	 * String Representation
	 * 
	 * @return info
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("UIField[").append(getAD_Field_ID())
				.append("-").append(getColumnName());
		if (m_lookup != null)
			sb.append(";").append(m_lookup.toString());
		if (isKey())
			sb.append("(Key)");
		if ((m_parentValue != null) && m_parentValue.booleanValue())
			sb.append("(Parent)");
		sb.append("]");
		return sb.toString();
	} // toString

} // UIField
