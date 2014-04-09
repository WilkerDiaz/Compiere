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

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.compiere.common.CompiereStateException;
import org.compiere.common.QueryRestrictionVO;
import org.compiere.common.QueryVO;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.db.QueryWithBindings;
import org.compiere.framework.Lookup;
import org.compiere.framework.PO;
import org.compiere.framework.Query;
import org.compiere.framework.QueryRestriction;
import org.compiere.framework.VO;
import org.compiere.model.MLocatorLookup;
import org.compiere.model.MRole;
import org.compiere.model.MSession;
import org.compiere.model.MTable;
import org.compiere.model.MUserQuery;
import org.compiere.util.CContext;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.CThreadUtil;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.DBException;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Evaluatee;
import org.compiere.util.Evaluator;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.NamePair;
import org.compiere.util.Null;
import org.compiere.util.QueryUtil;
import org.compiere.util.Trx;
import org.compiere.util.Util;
import org.compiere.util.ValueNamePair;
import org.compiere.vos.ChangeVO;

/**
 * User Interface Tab
 * 
 * @author Jorg Janke
 * @version $Id: UITab.java 9148 2010-07-29 06:11:41Z sdandapat $
 */
public class UITab extends UITabVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final int MAX_QUERY_RESULT_SIZE = 50000;

	/**
	 * UI Tab
	 * 
	 * @param vo
	 *            vo
	 */
	public UITab(UITabVO vo, UIWindow w) {		
		super(vo);
		m_window = w;
	} // UITab

	/** Tab No */
	private int m_tabNo = 0;
	/** Column Names */
	private String[] m_columnNames = null;
	/** Key Columns */
	private ArrayList<String> m_keyColumns = null;
	/** Key Column position */
	private int m_keyColumnPos = -1;
	
	// The window of this tab
	private UIWindow			m_window;

	/** Parent Columns */
	private ArrayList<String> m_parentColumns = null;
	/** Identifier Columns */
	private ArrayList<String> m_identifierColumns = null;
	/** Selection Columns */
	private ArrayList<String> m_selectionColumns = null;
	/** Summary Columns */
	private ArrayList<String> m_summaryColumns = null;
	/** Order By Clause */
	private String[] m_orderBys = new String[3];
	/** Tab depends on the following fields for UI */
	private ArrayList<String> m_dependsOnUI = new ArrayList<String>();
	/** Used Mnemonics */
	private ArrayList<Character> m_mnemonics = new ArrayList<Character>(30);
	/** Sales Order Window/Tab */
	private boolean m_isSOTrx = true;
	/** Logger */
	private static CLogger log = CLogger.getCLogger(UITab.class);

	/**
	 * Get relative Tab No
	 * 
	 * @return
	 */
	public int getTabNo() {
		return m_tabNo;
	} // getTabNo

	/**
	 * Get Saved Query Names
	 * 
	 * @param AD_Client_ID
	 *            client
	 * @return saved Queries
	 */
	public ArrayList<String> getSavedQueryNames(int AD_Client_ID) {
		return MUserQuery.getSavedQueryNames(AD_Client_ID, getAD_Tab_ID());
	} // getSavedQueryNames

	/**
	 * Get Saved Query Names
	 * 
	 * @param AD_User_ID
	 *            user
	 * @return saved Queries
	 */
	public ArrayList<String> getSavedQueryNamesForUser(int AD_User_ID) {
		return MUserQuery.getSavedQueryNamesForUser(AD_User_ID, getAD_Tab_ID());
	} // getSavedQueryNames

	/**
	 * Initialize
	 * 
	 * @param fields
	 *            fields
	 * @param previousTabs
	 *            previous tabs
	 * @param ctx
	 *            context
	 * @param windowNo
	 *            window
	 * @param tabNo
	 *            relative tab
	 */
	protected void initialize(ArrayList<UIField> fields,
			ArrayList<UITab> previousTabs, CContext ctx, int windowNo,
			int tabNo, boolean isSOTrx) {
		log.fine(toString());
		m_columnNames = null;
		m_tabNo = tabNo;
		m_isSOTrx = isSOTrx;
		//
		if (fields == null)
			p_vos = null;
		else {
			p_vos = new ArrayList<VO>(fields.size());
			ctx.setIsSOTrx(windowNo, isSOTrx);
			for (int i = 0; i < fields.size(); i++) {
				UIField field = fields.get(i);
				// don't initialize field, init when get it from cache
				// field.initialize(ctx, windowNo);
				// FIXME: mark here for further inspection, we shouldn't get
				// lookup data here 'cuz we'll do it later when the window is
				// really used
				// field.getAllLookupData(ctx, windowNo);
				p_vos.add(field);
			}
		}
		createColumnLists();
		createFieldMnemonics();

		// Set Link Column
		if (isDetailTab()) {
			// explicit
			String linkColumnName = getLinkColumnName();
			// implicit
			if (linkColumnName == null || linkColumnName.length() == 0) {
				ArrayList<String> parents = getParentColumnNames();
				// Single Parent
				if (parents.size() == 1) {
					linkColumnName = parents.get(0);
					setLinkColumnName(linkColumnName);
				}
				// Multiple Parents
				else {
					for (int i = 0; i < previousTabs.size(); i++) {
						UITab previousTab = previousTabs.get(i);
						String previousKeyColumn = previousTab
						.getKeyColumnName(); // single key
						if (previousTab.getTabLevel() < getTabLevel()) {
							for (int j = 0; j < parents.size(); j++) {
								String parentColumnName = parents.get(j);
								if (parentColumnName.equals(previousKeyColumn)) {
									// find the linkColumn as the previous
									// parent's key
									linkColumnName = parents.get(j);
									setLinkColumnName(linkColumnName);
									break;
								}
							}
						}
					}
				}
			}
			if (linkColumnName == null || linkColumnName.length() == 0)
				log.warning("No Link Column: " + toString());
			else
				log.fine("LinkColumnName=" + linkColumnName);
		}

	} // initialize

	/** Column with list of Columns depending */
	HashMap<String, ArrayList<String>> m_allDependents = new HashMap<String, ArrayList<String>>();

	/**
	 * Create Dependency Relations. Field Logic contains the columns it depends
	 * on [displayed if Active=Y] (dependensOn - created via
	 * Field.initialize()/createDependsOnLists()) This routine calculates the
	 * Impact [Active impacts] and saves that in the field.
	 */
	public void createDependencyRelations() {
		// Tab Display dependent
		Evaluator.parseDepends(m_dependsOnUI, getDisplayLogic());
		Evaluator.parseDepends(m_dependsOnUI, getReadOnlyLogic());
		for (int i = 0; i < m_dependsOnUI.size(); i++) {
			String impactColumnName = m_dependsOnUI.get(i);
			UIField impactField = getField(impactColumnName);
			if (impactField == null)
				log.finer("Not found (TabUI): " + impactColumnName);
			else
				impactField.setImpactsUITab(true);
		}

		// All Field DependsOn
		for (int i = 0; i < p_vos.size(); i++) {
			UIField field = (UIField) p_vos.get(i);
			String columnName = field.getColumnName();
			ArrayList<String> uis = field.getDependsOnUI();
			for (int j = 0; j < uis.size(); j++) {
				String impactColumnName = uis.get(j);
				UIField impactField = getField(impactColumnName);
				if (impactField == null)
					log.finer("Not found (FieldUI): " + impactColumnName);
				else
					impactField.addImpactsUIColumn(columnName);
			}
			ArrayList<String> values = field.getDependsOnValue();
			for (int j = 0; j < values.size(); j++) {
				String impactColumnName = values.get(j);
				UIField impactField = getField(impactColumnName);
				if (impactField == null)
					log.finer("Not found (FieldValue): " + impactColumnName);
				else
					impactField.addImpactsValueColumn(columnName);
			}
		}

		if (CLogMgt.isLevelFiner()) {
			for (int i = 0; i < p_vos.size(); i++) {
				UIField field = (UIField) p_vos.get(i);
				if (field.getImpactsUI().size() > 0)
					log.fine(field.getColumnName() + ": UI Impact on: "
							+ field.getImpactsUI());
				if (field.getImpactsValue().size() > 0)
					log.fine(field.getColumnName() + ": Value Impact on: "
							+ field.getImpactsValue());
				if (field.isImpactsUITab())
					log.fine(field.getColumnName() + ": Tab Impact");
			}
		} // debug
	} // createDependencyRelations

	/**
	 * Get the list of column names this tab Depends On for UI. (Display,
	 * ReadOnly, Mandatory)
	 * 
	 * @return list of columns
	 */
	public ArrayList<String> getDependsOnUI() {
		return m_dependsOnUI;
	} // getDependsOnUI

	/**
	 * Get Fields
	 * 
	 * @return Fields
	 */
	public ArrayList<UIField> getFields() {
		if (p_vos == null)
			return null;
		ArrayList<UIField> retValue = new ArrayList<UIField>(p_vos.size());
		for (int i = 0; i < p_vos.size(); i++) {
			UIField field = (UIField) p_vos.get(i);
			retValue.add(field);
		}
		return retValue;
	} // getFields

	/**
	 * Get Field
	 * 
	 * @return Field with name or null
	 */
	public UIField getField(String columnName) {
		if (p_vos == null)
			return null;
		for (int i = 0; i < p_vos.size(); i++) {
			UIField field = (UIField) p_vos.get(i);
			if (field.getColumnName().equals(columnName))
				return field;
		}
		return null;
	} // getField

	/**
	 * Get Field Index
	 * 
	 * @return Field with name or null
	 */
	public int getFieldIndex(int AD_Field_ID) {
		if (p_vos == null)
			return -1;
		for (int i = 0; i < p_vos.size(); i++) {
			UIField field = (UIField) p_vos.get(i);
			if (field.getAD_Field_ID() == AD_Field_ID)
				return i;
		}
		return -1;
	} // getField

	/**
	 * Get Field Index
	 * 
	 * @return Field with name or null
	 */
	public int getFieldIndex(String columnName) {
		if (p_vos == null)
			return -1;
		for (int i = 0; i < p_vos.size(); i++) {
			UIField field = (UIField) p_vos.get(i);
			if (field.getColumnName().equals(columnName))
				return i;
		}
		return -1;
	} // getField

	/**
	 * Get Column Names
	 * 
	 * @return column names
	 */
	public String[] getColumnNames() {
		if (m_columnNames == null && p_vos != null) {
			m_columnNames = new String[p_vos.size()];
			for (int i = 0; i < p_vos.size(); i++) {
				UIField field = (UIField) p_vos.get(i);
				m_columnNames[i] = field.getColumnName();
			}
		}
		return m_columnNames;
	} // getColumnNames

	/**
	 * Detail Tab
	 * 
	 * @return true if tab level is 0
	 */
	public boolean isDetailTab() {
		return getAD_Column_ID() != 0 || getParentColumnNames().size() > 0;
	} // isDetailTab

	/**
	 * Get Key Columns
	 * 
	 * @return list with one or more columns (if no PK)
	 */
	public ArrayList<String> getKeyColumnNames() {
		if (m_keyColumns == null)
			createColumnLists();
		return m_keyColumns;
	} // getKeyColumnNames

	/**
	 * Get Key Column Name
	 * 
	 * @return
	 */
	public String getKeyColumnName() {
		ArrayList<String> keyColumns = getKeyColumnNames();
		int size = keyColumns.size();
		if (size == 0) {
			log.warning("No KeyColumn - " + toString());
			return "";
		} else if (size > 1)
			log.fine("More than one KeyColumn - " + toString() + " : "
					+ keyColumns);
		return keyColumns.get(0);
	} // getKeyColumnName

	/**
	 * Get parent Columns
	 * 
	 * @return list with none or more columns
	 */
	public ArrayList<String> getParentColumnNames() {
		if (m_parentColumns == null)
			createColumnLists();
		return m_parentColumns;
	} // getParentColumnNames

	/**
	 * Get Identifier Column Names ordered
	 * 
	 * @return list with identifier columns
	 */
	public ArrayList<String> getIdentifierColumnNames() {
		if (m_identifierColumns == null)
			createColumnLists();
		return m_identifierColumns;
	} // getIdentifierColumnNames

	/**
	 * Get Selection Column Names ordered
	 * 
	 * @return list with selection columns
	 */
	public ArrayList<String> getSelectionColumnNames() {
		if (m_selectionColumns == null)
			createColumnLists();
		return m_selectionColumns;
	} // getSelectionColumnNames

	/**
	 * Get Summary Column Names ordered
	 * 
	 * @return list with summary columns
	 */
	public ArrayList<String> getSummaryColumnNames() {
		if (m_summaryColumns == null)
			createColumnLists();
		return m_summaryColumns;
	} // getSummaryColumnNames

	/**
	 * Create all Column Lists
	 */
	private synchronized void createColumnLists() {
		if (p_vos == null)
			return;
		//
		m_keyColumns = new ArrayList<String>(2);
		m_parentColumns = new ArrayList<String>(2);
		m_identifierColumns = new ArrayList<String>();
		m_selectionColumns = new ArrayList<String>();
		m_summaryColumns = new ArrayList<String>();

		m_keyColumnPos = -1;

		int parentColumnPos = -1;
		// Intermediate
		ArrayList<KeyNamePair> identifierPos = new ArrayList<KeyNamePair>();
		ArrayList<KeyNamePair> selectionPos = new ArrayList<KeyNamePair>();
		ArrayList<KeyNamePair> summaryPos = new ArrayList<KeyNamePair>();
		// Loop
		for (int i = 0; i < p_vos.size(); i++) {
			UIField field = (UIField) p_vos.get(i);
			String columnName = field.getColumnName();
			// Key
			if (field.isKey()) {
				m_keyColumns = new ArrayList<String>(1);
				m_keyColumns.add(columnName);
				if (m_keyColumnPos == -1)
					m_keyColumnPos = i;
			}
			// Parent
			if (field.isParent()) {
				m_parentColumns.add(columnName);
				if (parentColumnPos == -1)
					parentColumnPos = i;
			}
			// Identifier
			if (field.isIdentifier()) {
				KeyNamePair pp = new KeyNamePair(field.getSeqNo(), columnName);
				pp.setSortByName(false);
				identifierPos.add(pp);
			}
			// Selection
			if (field.isSelectionColumn()) {
				KeyNamePair pp = new KeyNamePair(field.getSelectionSeqNo(),
						columnName);
				pp.setSortByName(false);
				selectionPos.add(pp);
			} else if (columnName.startsWith("DocumentNo")
					|| columnName.equals("Value") || columnName.equals("Name")) {
				KeyNamePair pp = new KeyNamePair(0, columnName);
				pp.setSortByName(false);
				selectionPos.add(pp);
			}
			// Summary
			if (field.isSummaryColumn()) {
				KeyNamePair pp = new KeyNamePair(field.getSummarySeqNo(),
						columnName);
				pp.setSortByName(false);
				summaryPos.add(pp);
			} else if (columnName.startsWith("DocumentNo")
					|| columnName.equals("Value") || columnName.equals("Name")) {
				KeyNamePair pp = new KeyNamePair(0, columnName);
				pp.setSortByName(false);
				summaryPos.add(pp);
			}
			// OrderBy
			BigDecimal sort = field.getSortNo();
			if (sort != null && sort.signum() != 0) {
				int index = sort.abs().intValue() - 1;
				if (index < 3) {
					if (m_orderBys[index] == null) {
						m_orderBys[index] = columnName;
						if (sort.signum() < 0)
							m_orderBys[index] += " DESC";
					} else
						log.warning("Ignored OrderBy Duplicate Position "
								+ (index + 1) + ": " + columnName + " - "
								+ toString());
				} else
					log.warning("Ignored OrderBy " + columnName + " - "
							+ toString());
			}
		} // column loop
		if (m_keyColumns.size() == 0) {
			m_keyColumns = m_parentColumns;
			m_keyColumnPos = parentColumnPos;
		}

		// Sort them
		Collections.sort(identifierPos);
		for (int i = 0; i < identifierPos.size(); i++)
			m_identifierColumns.add(identifierPos.get(i).getName());
		Collections.sort(selectionPos);
		for (int i = 0; i < selectionPos.size(); i++)
			m_selectionColumns.add(selectionPos.get(i).getName());
		Collections.sort(summaryPos);
		for (int i = 0; i < summaryPos.size(); i++)
			m_summaryColumns.add(summaryPos.get(i).getName());
	} // createColumnLists

	/**
	 * Create Field Mnemonics
	 */
	private void createFieldMnemonics() {
		// Predefined in the text via &
		for (int i = 0; i < p_vos.size(); i++) {
			UIField field = (UIField) p_vos.get(i);
			if (field.isCreateMnemonic()) {
				String text = field.getName();
				// Predefined
				int pos = text.indexOf('&');
				if (pos != -1 && text.length() > pos) // We have a mnemonic -
					// creates Ctrl_Shift_
				{
					char mnemonic = text.toUpperCase().charAt(pos + 1);
					if (mnemonic != ' ') {
						if (!m_mnemonics.contains(mnemonic)) {
							field.setMnemonic(mnemonic);
							m_mnemonics.add(mnemonic);
						} else
							log.warning(field.getColumnName()
									+ " - Conflict - Already exists: "
									+ mnemonic + " (" + text + ")");
					}
				}
			} // mnemonic
			else
				field.setMnemonic((char) 0);
		} // for all

		// Search for first letter in word, then any character
		for (int i = 0; i < p_vos.size(); i++) {
			UIField field = (UIField) p_vos.get(i);
			if (field.getMnemonic() != 0)
				continue; // already set
			String text = field.getName();
			String oText = text;
			text = text.trim().toUpperCase();
			char mnemonic = text.charAt(0);
			if (m_mnemonics.contains(mnemonic)) {
				mnemonic = 0;
				// Beginning new word
				int index = text.indexOf(' ');
				while (index != -1 && text.length() > index) {
					char c = text.charAt(index + 1);
					if (Character.isLetterOrDigit(c)
							&& !m_mnemonics.contains(c)) {
						mnemonic = c;
						break;
					}
					index = text.indexOf(' ', index + 1);
				}
				// Any character
				if (mnemonic == 0) {
					for (int j = 1; j < text.length(); j++) {
						char c = text.charAt(j);
						if (Character.isLetterOrDigit(c)
								&& !m_mnemonics.contains(c)) {
							mnemonic = c;
							break;
						}
					}
				}
				// Nothing found
				if (mnemonic == 0)
					log.finest("None for: " + oText);
			}
			if (mnemonic != 0) {
				field.setMnemonic(mnemonic);
				m_mnemonics.add(mnemonic);
			}
		} // for all fields
	} // createMnemonics

	/**
	 * Is Window in SO Context
	 * 
	 * @return true if window SO
	 */
	public boolean isSOTrx() {
		return m_isSOTrx;
	} // isSOTrx

	/**
	 * Execute Query for Tab
	 * 
	 * @param queryVO
	 *            optional query
	 * @param context
	 *            record context for link columns and other variables
	 * @param ctx
	 *            user context
	 * @return number of records
	 */
	public Object[][] executeQuery(QueryVO queryVO,
			HashMap<String, String> context, CContext ctx) {
		MRole role = MRole.getDefault(ctx, false);
		QueryWithBindings whereClause = getWhereClauseWithBindings(queryVO,
				role, context, ctx, true);
		StringBuffer sql0 = new StringBuffer("SELECT ");
		ArrayList<UIField> fields = getFields();
		for (int i = 0; i < fields.size(); i++) {
			if (i > 0)
				sql0.append(",");
			UIField field = fields.get(i);
			if (field.isVirtualColumn())
				sql0.append(field.getColumnSQL()).append(" AS ");
			sql0.append(field.getColumnName());
		}
		sql0.append(" FROM ").append(getTableName()).append(whereClause.sql);
		String sql1 = role.addAccessSQL(sql0.toString(), getTableName(),
				MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		//
		// String[] columns = getColumnNames();


		Object[][] results = new Object[0][];
		results = QueryUtil.executeQueryMaxRows(null, sql1, whereClause.params.toArray(), 0, (MAX_QUERY_RESULT_SIZE + 1));

		if (results.length > 0) {
			MSession session = MSession.get(ctx);
			if (session == null)
				log.severe("Session not found!");
			else
				session.queryLog(ctx.getAD_Client_ID(), ctx.getAD_Org_ID(),
						getAD_Table_ID(), sql1, results.length);
		}
		// Results
		return results;
	} // executeQuery

	/**
	 * Execute Query for Tab and return results as String
	 * 
	 * @param queryVO
	 *            optional query
	 * @param context
	 *            record context for link columns and other variables
	 * @param ctx
	 *            user context
	 * @return number of records
	 */
	public ArrayList<String[]> executeQueryString(QueryVO queryVO,
			HashMap<String, String> context, CContext ctx) {
		Object[][] from = executeQuery(queryVO, context, ctx);
		ArrayList<String[]> to = new ArrayList<String[]>(from.length);
		for (Object[] fromRow : from) {
			to.add(convertToString(fromRow));
		}
		return to;
	} // executeQueryString

	/**
	 * Convert To String
	 * 
	 * @param fromRow
	 *            objects
	 * @return strings
	 */
	public String[] convertToString(Object[] fromRow) {
		String[] toRow = new String[fromRow.length];
		for (int i = 0; i < fromRow.length; i++) {
			Object fromValue = fromRow[i];
			if (fromValue == null)
				toRow[i] = null;
			else if (fromValue instanceof java.sql.Timestamp) {
				long time = ((java.sql.Timestamp) fromValue).getTime();
				toRow[i] = String.valueOf(time);
			}

			else if (fromValue instanceof java.sql.Date) {
				long time = ((java.sql.Date) fromValue).getTime();
				toRow[i] = String.valueOf(time);
			} else if (fromValue instanceof Boolean) {
				if (((Boolean) fromValue).booleanValue())
					toRow[i] = "Y";
				else
					toRow[i] = "N";
			} else
				toRow[i] = fromValue.toString();
		}
		return toRow;
	} // convertToString

	/**
	 * Get Where & Order By Clause
	 * 
	 * @param queryVO
	 *            optional restrictions
	 * @param role
	 *            security role
	 * @param context
	 *            context for link columns and other variables
	 * @param ctx
	 *            user context
	 * @param addOrderBy
	 *            if true add order by clause
	 * @return where clause with tailoring space
	 */
	private QueryWithBindings getWhereClauseWithBindings(QueryVO queryVO,
			MRole role, HashMap<String, String> context, CContext ctx,
			boolean addOrderBy) {
		StringBuffer sb = new StringBuffer();
		ArrayList<Object> params = new ArrayList<Object>();
		// Detail Tab - Link
		if (isDetailTab()) {
			String linkColumnName = getLinkColumnName();
			if (linkColumnName.length() == 0) {
				log.warning("No LinkColumn - " + toString());
				sb.append(" WHERE 2=3");
				return new QueryWithBindings(sb.toString(), params);
			}
			String linkColumnValue = context.get(linkColumnName);
			if (linkColumnValue == null) {
				log.warning("No Value for LinkColumn=" + linkColumnName + " - "
						+ toString());
				sb.append(" WHERE 2=3");
				return new QueryWithBindings(sb.toString(), params);
			} else if (linkColumnName.toUpperCase().endsWith("_ID")) {
				sb.append(" WHERE ").append(linkColumnName).append("= ?");
				params.add(Integer.parseInt(linkColumnValue));
			} else {
				sb.append(" WHERE ").append(linkColumnName).append("= ?");
				params.add(linkColumnValue);
			}
		}

		// Query
		int onlyCurrentDays = 0;
		boolean onlyCurrentCreated = true;
		if (queryVO != null && queryVO.onlyCurrentDays > 0) {
			onlyCurrentDays = queryVO.onlyCurrentDays;
			onlyCurrentCreated = queryVO.onlyCurrentCreated;
			//
			if (sb.length() == 0)
				sb.append(" WHERE ");
			else
				sb.append(" AND ");
			boolean showNotProcessed = getField("Processed") != null;
			if (showNotProcessed)
				sb.append("(Processed='N' OR ");
			if (onlyCurrentCreated)
				sb.append("Created>=");
			else
				sb.append("Updated>=");
			sb.append("addDays(SysDate, - ?)");
			params.add(onlyCurrentDays);
			if (showNotProcessed)
				sb.append(")");
		} else if (queryVO != null) {
			Query query = createQuery(ctx, queryVO);
			// Validate Query (zoom to sub-tabs)
			if (query != null && query.isActive()) {
				if (sb.length() == 0)
					sb.append(" WHERE ");
				else
					sb.append(" AND ");
				sb.append(validateQuery(query));
			}
		}

		// Static Tab Where Clause
		String where = getWhereClause();
		if (where != null && where.length() > 0) {
			if (sb.length() == 0)
				sb.append(" WHERE ");
			else
				sb.append(" AND ");
			if (where.indexOf("@") != -1) {
				int windowNo = 111111;
				ctx.addWindow(windowNo, context);
				where = Env.parseContext(ctx, windowNo, where, false);
			}
			sb.append(where);
		}

		if (addOrderBy)
			sb.append(getOrderByClause(onlyCurrentDays, onlyCurrentCreated));
		return new QueryWithBindings(sb.toString(), params);
	} // getWhereClause

	/**
	 * Check the security of the where clause. It disallows subquery,
	 * 
	 * @param where
	 * @return
	 */
	public static String[] FORBIDDEN = { "where", "insert", "update", "delete",
		"select", "from" };

	public static boolean checkWhereClause(String where) {
		if (where == null || where.length() == 0)
			return true;
		String[] tokens = where.toLowerCase().split("[\\s()]");
		for (String token : tokens) {
			if (token.length() == 0)
				continue;
			for (String forbidden : FORBIDDEN) {
				if (forbidden.equals(token)) {
					throw new CompiereStateException("Can't use word:" + token
							+ " and the where clause:" + where);
					// return false;
				}
			}
		}
		return true;
	}
	public Query createQueryForReport(CContext ctx, QueryVO queryVO){
		return createQuery(ctx, queryVO);
	}

	/**
	 * Create Query
	 * 
	 * @param qieryVO
	 *            optional query restrictions
	 * @return query or null
	 */
	private Query createQuery(CContext ctx, QueryVO queryVO) {
		if (queryVO == null)
			return null;

		// Query Restrictions
		Query query = null;
		if (queryVO.restrictions != null && queryVO.restrictions.size() > 0) {
			query = new Query(getTableName());
			for (int i = 0; i < queryVO.restrictions.size(); i++) {
				QueryRestrictionVO vo = queryVO.restrictions.get(i);
				if (checkWhereClause(vo.DirectWhereClause)) {

					Object qCode = DisplayType.convertFromString(
							vo.DisplayType, vo.Code);
					Object qCode_to = DisplayType.convertFromString(
							vo.DisplayType, vo.Code_to);
					QueryRestriction restriction = new QueryRestriction(
							vo.ColumnName, qCode, qCode_to, vo.InfoName,
							vo.InfoDisplay, vo.InfoDisplay_to, vo.Operator,
							vo.DirectWhereClause, vo.AndCondition);
					query.addRestriction(restriction);
				}
			}
		}

		// Save Query
		if (queryVO.savedQueryName != null
				&& queryVO.savedQueryName.length() > 0 && queryVO.saveQuery
				&& query != null) {
			// overwrite old saved query with new code if it already exists
			MUserQuery userQ = MUserQuery.getForUser(ctx, getAD_Tab_ID(),
					queryVO.savedQueryName);
			if (userQ == null) {
				userQ = new MUserQuery(ctx, 0, null);
				userQ.setName(queryVO.savedQueryName);
				userQ.setAD_Tab_ID(getAD_Tab_ID());
				userQ.setAD_Table_ID(getAD_Table_ID());
				userQ.setAD_User_ID(ctx.getAD_User_ID());
			}
			userQ.setCode(query.getWhereClause());

			userQ.save();
		}
		// Saved Query
		else if (queryVO.savedQueryName != null
				&& queryVO.savedQueryName.length() > 0) {
			MUserQuery userQ = MUserQuery.get(ctx, getAD_Tab_ID(),
					queryVO.savedQueryName);
			if (userQ == null)
				log.warning("SavedQuery nor found: " + queryVO.savedQueryName
						+ " - " + toString());
			else {
				query = new Query(getTableName());
				query.addRestriction(userQ.getCode());
			}
		}
		return query;
	} // createQuery

	/**
	 * Get Order By Clause
	 * 
	 * @param currentDays
	 *            if > 1 query only recent records
	 * @param currentCreated
	 *            if true get recent records based on Created otherwise Updated
	 * @return order by clause or ""
	 */
	private String getOrderByClause(int currentDays, boolean currentCreated) {
		StringBuffer ob = new StringBuffer(" ORDER BY ");
		// First Prio: Tab Order By
		String order = super.getOrderByClause();
		if (order != null && order.length() > 0)
			return ob.append(order).toString();

		// Second Prio: Fields (save it)
		StringBuffer fieldOrder = new StringBuffer();
		for (int i = 0; i < 3; i++) {
			order = m_orderBys[i];
			if (order != null && order.length() > 0) {
				if (fieldOrder.length() > 0)
					fieldOrder.append(",");
				fieldOrder.append(order);
			}
		}
		if (fieldOrder.length() > 0) {
			setOrderByClause(fieldOrder.toString()); // save for next
			return ob.append(fieldOrder).toString();
		}

		// Third Prio: currentRows
		if (currentDays > 0) {
			if (currentCreated)
				ob.append("Created DESC");
			else
				ob.append("Updated DESC");
			return ob.toString();
		}
		//
		log.fine("No OrderBy - " + toString());
		return "";
	} // getOrderByClause

	/**
	 * Validate Query. If query column is not a tab column create EXISTS query
	 * 
	 * @param query
	 *            query
	 * @return where clause
	 */
	private String validateQuery(Query query) {
		if (query == null || query.getRestrictionCount() == 0)
			return null;

		// Check: only one restriction
		if (query.getRestrictionCount() != 1) {
			log.fine("Ignored(More than 1 Restriction): " + query);
			return query.getWhereClause();
		}

		String colName = query.getColumnName(0);
		if (colName == null) {
			log.fine("Ignored(No Column): " + query);
			return query.getWhereClause();
		}
		// a '(' in the name = function - don't try to resolve
		if (colName.indexOf('(') != -1) {
			log.fine("Ignored(Function): " + colName);
			return query.getWhereClause();
		}
		// OK - Query is valid

		// Zooms to the same Window (Parents, ..)
		String refColName = null;
		if (colName.equals("R_RequestRelated_ID"))
			refColName = "R_Request_ID";
		else if (colName.startsWith("C_DocType")
				&& !colName.equals("C_DocTypeGroup_ID"))
			refColName = "C_DocType_ID";
		else if (colName.equals("Orig_Order_ID"))
			refColName = "C_Order_ID";
		else if (colName.equals("Orig_InOut_ID"))
			refColName = "M_InOut_ID";
		else if (colName.equals("CreatedBy") || colName.equals("UpdatedBy"))
			if (!this.getTableName().equals("R_Request")) // to allow searching
				// by these fields
				// in Request table
				refColName = "AD_User_ID";

		if (refColName != null) {
			query.setColumnName(0, refColName);
			if (getField(refColName) != null) {
				log.fine("Column " + colName + " replaced with synonym "
						+ refColName);
				return query.getWhereClause();
			}
			refColName = null;
		}

		// Simple Query.
		if (getField(colName) != null) {
			log.fine("Field Found: " + colName);
			return query.getWhereClause();
		}

		// Find Refernce Column e.g. BillTo_ID -> C_BPartner_Location_ID
		String sql = "SELECT cc.ColumnName "
			+ "FROM AD_Column c"
			+ " INNER JOIN AD_Ref_Table r ON (c.AD_Reference_Value_ID=r.AD_Reference_ID)"
			+ " INNER JOIN AD_Column cc ON (r.Column_Key_ID=cc.AD_Column_ID) "
			+ "WHERE c.AD_Reference_ID IN (18,30)" // Table/Search
			+ " AND c.ColumnName=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, colName);
			rs = pstmt.executeQuery();
			if (rs.next())
				refColName = rs.getString(1);
		} 
		catch (SQLException e) {
			log.log(Level.SEVERE, "(ref) - Column=" + colName, e);
			return query.getWhereClause();
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		// Reference Column found
		if (refColName != null) {
			query.setColumnName(0, refColName);
			if (getField(refColName) != null) {
				log.fine("Column " + colName + " replaced with " + refColName);
				return query.getWhereClause();
			}
			colName = refColName;
		}

		// Column NOT in Tab - create EXISTS subquery
		String tableName = null;
		String tabKeyColumn = getKeyColumnName(); // assumes single key
		// Column=SalesRep_ID, Key=AD_User_ID, Query=SalesRep_ID=101

		sql = "SELECT t.TableName "
			+ "FROM AD_Column c"
			+ " INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID) "
			+ "WHERE c.ColumnName=? AND IsKey='Y'" // #1 Link Column
			+ " AND EXISTS (SELECT * FROM AD_Column cc"
			+ " WHERE cc.AD_Table_ID=t.AD_Table_ID AND cc.ColumnName=?)"; // #2
		// Tab
		// Key
		// Column
		try {
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, colName);
			pstmt.setString(2, tabKeyColumn);
			rs = pstmt.executeQuery();
			if (rs.next())
				tableName = rs.getString(1);
		} 
		catch (SQLException e) {
			log.log(Level.SEVERE,
					"Column=" + colName + ", Key=" + tabKeyColumn, e);
			return null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		// Special Reference Handling
		if (tabKeyColumn.equals("AD_Reference_ID")) {
			// Column=AccessLevel, Key=AD_Reference_ID, Query=AccessLevel='6'
			sql = "SELECT AD_Reference_ID FROM AD_Column WHERE ColumnName=?";
			int AD_Reference_ID = QueryUtil.getSQLValue(null, sql, colName);
			return "AD_Reference_ID=" + AD_Reference_ID;
		}

		// Causes could be functions in query
		// e.g. Column=UPPER(Name), Key=AD_Element_ID,
		// Query=UPPER(AD_Element.Name) LIKE '%CUSTOMER%'
		if (tableName == null) {
			log.info("Not successfull - Column=" + colName + ", Key="
					+ tabKeyColumn + ", Query=" + query);
			return query.getWhereClause();
		}
		else if(tableName.equals("M_WorkOrderComponent") && tabKeyColumn.equals("M_WorkOrder_ID")){
			Object[][] results = QueryUtil.executeQuery((Trx)null, "SELECT M_WorkOrderOperation_ID FROM M_WorkOrderComponent WHERE "+query.getWhereClause(),new Object[]{});
			int M_WorkOrderOperation_ID = 0;
			if(results[0][0] instanceof BigDecimal)
				M_WorkOrderOperation_ID = ((BigDecimal)results[0][0]).intValue();
			else if(results[0][0] instanceof Integer)
				M_WorkOrderOperation_ID = (Integer)results[0][0];
			query.setTableName("xx");
			String result = "EXISTS (SELECT * FROM M_WorkOrderOperation xx WHERE xx.M_WorkOrderOperation_ID="+M_WorkOrderOperation_ID+" AND xx.M_WorkOrder_ID=M_WorkOrder.M_WorkOrder_ID)";
			return result;
		}

		query.setTableName("xx");
		StringBuffer result = new StringBuffer("EXISTS (SELECT * FROM ")
		.append(tableName).append(" xx WHERE ").append(
				query.getWhereClause(true)).append(" AND xx.").append(
						tabKeyColumn).append("=").append(getTableName())
						.append(".").append(tabKeyColumn).append(")");
		log.fine(result.toString());
		return result.toString();
	} // validateQuery

	private ArrayList<String> getUpdatedFieldsByOthers(PO po, String[] cachedRow) {
		ArrayList<String> conflictedFields = new ArrayList<String>();
		int size = p_vos.size();
		for (int i = 0; i < size; i++) {
			UIField field = (UIField) p_vos.get(i);
			String colName = field.getColumnName();
			if (colName.startsWith("Created") || colName.startsWith("Updated")
					|| colName.equals("IsActive")
					|| colName.equals("AD_Client_ID")
					|| colName.equals("AD_Org_ID"))
				continue;

			String v = cachedRow[i] == null ? "" : cachedRow[i];
			Object poValue = po.get_Value(colName);
			if(poValue!= null && poValue instanceof BigDecimal) {
				BigDecimal bdValue = new BigDecimal(v);
				if(bdValue.compareTo((BigDecimal) poValue) != 0)
					conflictedFields.add(colName);
			}
			else if (!v.equals(po.get_ValueAsString(colName)))
				conflictedFields.add(colName);
		}

		return conflictedFields;

	}

	/***************************************************************************
	 * Save (Insert/Update) Row of Tab
	 * 
	 * @param ctx
	 *            general context
	 * @param context
	 *            current (relevant) context of new row
	 * @return error message or null
	 */
	public ChangeVO saveRow(CContext ctx, int windowNo, boolean newRecord) {
		return saveRow(ctx, windowNo, newRecord, null);
	}

	/***************************************************************************
	 * Save (Insert/Update) Row of Tab
	 * 
	 * @param ctx
	 *            general context
	 * @param context
	 *            current (relevant) context of new row
	 * @param cachedRow
	 *            the row cached for this user session
	 * @return error message or null
	 */
	public ChangeVO saveRow(CContext ctx, int windowNo, boolean newRecord,
			String[] cachedRow) {
		
		// ReadOnly
		String error = checkReadOnly(ctx, windowNo);
		if (error != null) 
			return new ChangeVO(true, Msg.parseTranslation(ctx, error));

		// Mandatory Fields
		error = checkMandatory(ctx, windowNo);
		if (error != null)
			return new ChangeVO(true, Msg.parseTranslation(ctx, error));
		
		//check if there are changes in Updated and Processed.
		if (hasChangedCurrentTabAndParents(ctx, windowNo))
			return new ChangeVO(true, Msg.getMsg(ctx , "ParentRecordModified"));

		Trx trx = Trx.get("Save Row");

		PO po = getPO(ctx, windowNo, newRecord, trx);
		if (po == null) {
			trx.rollback();
			trx.close();
			return new ChangeVO(true, Msg.getMsg(ctx, "Error")
					+ " - No PO for " + toString());
		}

		ChangeVO poChangeVO = new ChangeVO();
		po.set_ChangeVO(poChangeVO);
		Map<String, String> windowMap = ctx.getMap(windowNo);

		// preprocess windowMap
		// those 4 columns should not be updated for po
		windowMap.remove("CreatedBy");
		windowMap.remove("UpdatedBy");
		windowMap.remove("CreatedOn");
		windowMap.remove("UpdatedOn");

		int size = p_vos.size();
		for (int i = 0; i < size; i++) {
			UIField field = (UIField) p_vos.get(i);
			// return an empty string for passwords etc
			if (field.isEncryptedField() || field.isEncryptedColumn()
					|| "Password".equals(field.getColumnName())) {
				String colName = field.getColumnName();
				if (windowMap.get(colName) == null
						|| windowMap.get(colName).length() == 0)
					windowMap.remove(colName);
			}
		}

		// dzhao, here we compare db values from the cached values and then
		// compare with
		// values that need to be changed, if no conflicts, then we update
		if (!newRecord && cachedRow != null) {
			ArrayList<String> fields = getUpdatedFieldsByOthers(po, cachedRow);
			if (fields.size() > 0) {
				ChangeVO c = new ChangeVO(true,
						Msg.getMsg(ctx, "RefreshRow")
						+ fields);
				trx.rollback();
				trx.close();
				return c;
			}
		}

		// Can we update?

		error = po.update(windowMap);
		if (error != null) {
			trx.rollback();
			trx.close();
			return new ChangeVO(true, Msg.getMsg(ctx, "Error") + " - " + error);
		}
		// Save
		if (!po.save()) {
			ChangeVO change = new ChangeVO(true, Msg.getMsg(ctx, "NotSaved"));

			ValueNamePair e = CLogger.retrieveError();
			if (e != null)
				change.addError(Msg.getMsg(ctx, e.getID(), e.getName()));

			Exception ex = CLogger.retrieveException();
			if (ex != null) {
				if (ex.getCause() != null)
					change.addError(ex.getCause().getLocalizedMessage());
				else
					change.addError(ex.getLocalizedMessage());
			}

			trx.rollback();
			trx.close();
			return change;
		}
		ChangeVO retValue = new ChangeVO(Msg.getMsg(ctx, "Saved"));
		trx.commit();
		trx.rollback();

		addLog(ctx, retValue);
		retValue.rowData = po.get_ValuesAsString(getColumnNames());
		// pass along whatever PO has into the retValue
		if (po.get_ChangeVO().isRefreshAll())
			retValue.setRefreshAll(true);
		if(po.get_ChangeVO().isRefreshChildRows())
			retValue.setRefreshChildRows(true);
		
		
		return retValue;
	} // saveRow

	private void addLog(CContext ctx, ChangeVO change) {
		ValueNamePair p = CLogger.retrieveWarning();
		if (p != null)
			change.addWarning(Msg.getMsg(ctx, p.getID(), p.getName()));

		p = CLogger.retrieveInfo();
		if (p != null)
			change.addSuccess(Msg.getMsg(ctx, p.getID(), p.getName()));
	}

	/***************************************************************************
	 * Refresh (Insert/Update) Row of Tab
	 * 
	 * @param ctx
	 *            general context
	 * @param context
	 *            current (relevant) context of new row
	 * @return error message or null
	 */
	public ChangeVO refreshRow(CContext ctx, int windowNo) {
		PO po = getPO(ctx, windowNo, false);
		// if po is null, just return a message,
		// this is so when a process delete a record, the process will not be
		// interrupted

		if (po == null) {
			ChangeVO c = new ChangeVO(false, "No PO for " + toString());
			c.rowData = new String[this.m_columnNames.length];
			for (int i = 0; i < c.rowData.length; i++)
				c.rowData[i] = null;
			return c;
		}

		ChangeVO retValue = new ChangeVO();
		retValue.rowData = po.get_ValuesAsString(getColumnNames());
		return retValue;
	} // refreshRow

	/**
	 * Delete existing Row
	 * 
	 * @param ctx
	 *            general context
	 * @param context
	 *            current (relevant) context of row
	 * @return error message or null
	 */
	public ChangeVO deleteRow(CContext ctx, int windowNo) {
		String error = checkReadOnly(ctx, windowNo);
		if (error != null)
			return new ChangeVO(true, Msg.parseTranslation(ctx, error));

		error = checkDeleteable(ctx, windowNo);
		if (error != null)
			return new ChangeVO(true, Msg.parseTranslation(ctx, error));

		PO po = getPO(ctx, windowNo, false);
		if (po == null)
			return new ChangeVO(true, Msg.getMsg(ctx, "Error")
					+ " - No PO for " + toString());
		if (!po.delete(false)) {
			ValueNamePair e = CLogger.retrieveError();
			ChangeVO change = new ChangeVO(true, Msg.getMsg(ctx, e.getID()));// ,
			// e.getName()));
			return change;

		}
		ChangeVO retValue = new ChangeVO(Msg.getMsg(ctx, "Deleted"));
		addLog(ctx, retValue);
		return retValue;
	} // deleteRow

	/**
	 * New Row
	 * 
	 * @param ctx
	 *            context
	 * @param windowNo
	 *            window
	 * @return default values for new row
	 */
	public ChangeVO newRow(CContext ctx, int windowNo) {
		int size = p_vos.size();
		ChangeVO retValue = new ChangeVO();
		HashMap<String, String> changedValues = new HashMap<String, String>();
		retValue.changedFields = changedValues;
		for (int i = 0; i < size; i++) {
			UIField field = (UIField) p_vos.get(i);
			String key = field.getColumnName();

			String value;
			if (field.isKey() && field.getColumnName().toUpperCase().endsWith("_ID")) {
				//for ad_org table, init org id to -1
				if(getTableName().equals("AD_Org"))
					value = "-1";
				else
					value = "0"; // explicit init
			}
			else 
				value = field.getDefaultAsString(ctx, windowNo, this);
			changedValues.put(key, value);

		}
		if (org.compiere.common.constants.Build.isDebug())
			log.info("changed values are  before callout:" + changedValues);

		// dizhao, very important
		// context here can only contains parent context not this child's
		// context
		// setContext would eliminate fields that has "" or null
		ctx.setContext(windowNo, changedValues);
		// for()
		// Callouts
		for (int i = 0; i < size; i++) {
			UIField field = (UIField) p_vos.get(i);
			if (field.isCallout() || !Util.isEmpty(field.getCallout())) {
				String key = field.getColumnName();
				String newValue = changedValues.get(key);
				ChangeVO calloutChange = processCallout(ctx, windowNo, field,
						null, newValue);
				if (org.compiere.common.constants.Build.isDebug())
					log.info("After callout for " + field.getColumnName()
							+ " the change is " + calloutChange);
				if (calloutChange != null) {
					ctx.setContext(windowNo, calloutChange.changedContext);
					ctx.setContext(windowNo, calloutChange.changedFields);
				}
				retValue.addAll(calloutChange);
			}
		}
		if (org.compiere.common.constants.Build.isDebug())
			log.info("field values are  after callout:"
					+ retValue.changedFields);
		return retValue;
	} // newRow

	/**
	 * Get PO
	 * 
	 * @param ctx
	 *            general context
	 * @param windowNo
	 *            Window for context current\
	 * @param newRecord
	 *            create new record
	 * @return PO or null
	 */
	private PO getPO(CContext ctx, int windowNo, boolean newRecord) {
		return getPO(ctx, windowNo, newRecord, null);
	}
	
	/**
	 * Get PO
	 * 
	 * @param ctx
	 *            general context
	 * @param windowNo
	 *            Window for context current\
	 * @param newRecord
	 *            create new record
	 * @return PO or null
	 */
	private PO getPO(CContext ctx, int windowNo, boolean newRecord, Trx trx) {
		MTable table = MTable.get(ctx, getAD_Table_ID());
		
		/** Parent Columns Class */
		Class<?> ColumnClass = null;

		if (newRecord) {
			PO po = table.getPO(ctx, 0, trx); // always returns new
			for (String columnName : m_parentColumns) {
				for(int i = 0;i<p_vos.size();i++)
				{
					UIField field = (UIField) p_vos.get(i);
					if(field.getColumnName().equals(columnName))
						ColumnClass = org.compiere.util.DisplayType.getClass(field.getAD_Reference_ID(), true);
				}
				if (ColumnClass == String.class)
					po.set_ValueNoCheck(columnName, ctx.getContext(windowNo,
							columnName));

				else
					po.set_ValueNoCheck(columnName, ctx.getContextAsInt(windowNo,
							columnName));
			}
			return po;
		}
		//
		ArrayList<String> keys = getKeyColumnNames();
		if (keys.size() < 1) {
			log.config("No Keys for " + toString());
			return null;
		}
		boolean singleKey = keys.size() == 1;
		boolean parentKeys = m_parentColumns.size() > 0;
		if (singleKey && parentKeys)
			singleKey = !m_parentColumns.contains(keys.get(0));

		// One Key
		if (singleKey) {
			String keyColumn = keys.get(0);
			String stringID = ctx.getContext(windowNo, keyColumn);
			if (stringID == null || stringID.length() == 0) {
				log.config("@NotFound@ " + keyColumn + " - " + toString());
				// New
				if (keyColumn.toUpperCase().endsWith("_ID"))
					stringID = "0";
				else
					return null;
			}
			if (keyColumn.toUpperCase().endsWith("_ID")) {
				int Record_ID = Integer.parseInt(stringID);
				if (Record_ID == 0) // valid for Role/User/Org/Client/System
				{
					PO po = table.getPO(ctx, Record_ID, trx, false);
					return po;
				}
				PO po = table.getPO(ctx, Record_ID, trx);
				return po;
			} else
				// KeyColumn not ID
			{
				StringBuffer where = new StringBuffer(keyColumn).append("='")
				.append(stringID).append("'");
				PO po = table.getPO(ctx, where.toString(), trx);
				return po;
			}
		} else
			// more then one key
		{
			StringBuffer where = new StringBuffer();
			for (int i = 0; i < keys.size(); i++) {
				String keyColumn = keys.get(i);
				String stringID = ctx.getContext(windowNo, keyColumn);
				if (stringID == null || stringID.length() == 0) {
					log.config("@NotFound@ " + keyColumn + " - " + toString());
					return null;
				}
				if (i > 0)
					where.append(" AND ");
				where.append(keyColumn);
				if (keyColumn.toUpperCase().endsWith("_ID"))
					where.append("=").append(stringID);
				else
					where.append("='").append(stringID).append("'");
			}
			PO po = table.getPO(ctx, where.toString(), trx);
			return po;
		}
	} // getPO

	/**
	 * Check ReadOnly
	 * 
	 * @param ctx
	 *            context
	 * @param windowNo
	 *            window no
	 * @return null if not read only or error
	 */
	private String checkReadOnly(CContext ctx, int windowNo) {
		if (isReadOnly())
			return "@IsReadOnly@";

		// Check Role
		int AD_Client_ID = ctx.getAD_Client_ID(windowNo);
		int AD_Org_ID = ctx.getAD_Org_ID(windowNo);
		// Assumes single key
		int Record_ID = ctx.getContextAsInt(windowNo, getKeyColumnName());
		boolean createError = true;
		MRole role = MRole.getDefault(ctx, false);
		if (!role.canUpdate(AD_Client_ID, AD_Org_ID, getAD_Table_ID(),
				Record_ID, createError)) {
			String errorMsg = "@AccessCannotUpdateOrDelete@";
			ValueNamePair error = CLogger.retrieveError();
			if (error != null)
				errorMsg = Msg.getMsg(ctx, error.getID(), error.getName());

			return errorMsg;
		}

		String logic = getReadOnlyLogic();
		if (logic == null || logic.length() == 0)
			return null;
		// TODO: R/O Logic

		return null;
	} // checkReadOnly

	/**
	 * Check Tab Read Only Logic
	 * 
	 * @param e
	 *            Evaluatee source
	 * @return read only logic evaluation result
	 */
	public boolean checkReadOnlyLogic(Evaluatee e) {
		boolean retValue = false;
		String logic = getReadOnlyLogic();
		if (logic != null && logic.length() > 0)
		{
			retValue = Evaluator.evaluateLogic(e, logic);
			if(org.compiere.common.constants.Build.isVerbose())
				log.finest(this + " R/O(" + logic + ") => R/W-" + retValue);
		}
		return retValue;
	}
	
	/**
	 * Check Deletable
	 * 
	 * @param ctx
	 *            context
	 * @param context
	 *            row context
	 * @return null if not read only or error
	 */
	private String checkDeleteable(CContext ctx, int windowNo) {
		if (!isDeleteable())
			return "@AccessNotDeleteable@";

		return null;
	} // checkReadOnly

	/**
	 * Check Mandatory
	 * 
	 * @param ctx
	 *            context
	 * @param context
	 *            row context
	 * @return column names with missing values or null
	 */
	private String checkMandatory(CContext ctx, int windowNo) {
		// see also => ProcessParameter.saveParameter
		StringBuffer sb = new StringBuffer();

		// Check all columns
		int size = p_vos.size();
		for (int i = 0; i < size; i++) {
			UIField field = (UIField) p_vos.get(i);
			if (field.isMandatory(ctx, windowNo)) // check context
			{
				Object data = ctx.getContext(windowNo, field.getColumnName());
				if (data == null || data.toString().length() == 0
						|| Null.NULLString.equals(data)) {
					// field.setInserting (true); // set editable otherwise
					// deadlock
					field.setError(true);
					if (sb.length() > 0)
						sb.append(", ");
					sb.append("@").append(field.getName()).append("@");
				} else
					field.setError(false);
			}
		}

		if (sb.length() == 0)
			return null;
		sb.insert(0, "@FillMandatory@: ");
		return sb.toString();
	} // checkMandatory

	/**
	 * Field Changed Consequences
	 * 
	 * @param ctx
	 *            context
	 * @param field
	 *            Field
	 * @param oldValue
	 *            old field value
	 * @param newValue
	 *            new Field value
	 * @param context
	 *            current row context
	 * @return Field Change VO
	 */
	public ChangeVO fieldChanged(CContext origCtx, CContext ctx, ArrayList<UIField> changedFields,  int windowNo, UIField field,
			String oldValue, String newValue) {
		ChangeVO retValue = new ChangeVO();
		if(changedFields.contains(field)) {
			log.info("field:"+field.getColumnName()+ " already run through fieldChanged.");
			return retValue;
		}
		log.info("processing field change on column:"+field.getColumnName());
		changedFields.add(field);
		/**
		 * Changed Values Map<ColumnName,ColumnValue>
		 */
		retValue.changedFields = new HashMap<String, String>();
		/**
		 * Changed Drop Down Lists Map<FieldName,ArrayList<NewValues>>
		 */
		retValue.changedDropDowns = new HashMap<String, ArrayList<NamePair>>();
		//
		String columnName = field.getColumnName();

		// New Value
		retValue.newConfirmedFieldValue = field.validateValueAsString(oldValue,
				newValue);
		// TODO: set error message if value is not confirmed
		// retValue.addError(message)
		retValue.addChangedValue(columnName, retValue.newConfirmedFieldValue);
		ctx.setContext(windowNo, columnName, retValue.newConfirmedFieldValue);

		//
		ArrayList<UIField> impactedFields = new ArrayList<UIField>(5);
		if (field.isImpactsValue()) {
			// Data Value Changes
			ArrayList<String> impactedColumnNames = field.getImpactsValue();
			for (int i = 0; i < impactedColumnNames.size(); i++) {
				String impactedColumnName = impactedColumnNames.get(i);

				UIField impactedField = getField(impactedColumnName);
				if (impactedField == null) {
					log.warning(columnName + ": Impact Not found - "
							+ impactedColumnName);
					continue;
				}
				else 
					log.info("reprocess impacted column:"+impactedColumnName);

				if(changedFields.contains(impactedField)) {
					log.info("field:"+impactedField.getColumnName()+" already changed. don't run again");
					continue;
				}
				String oldImpactedValue = origCtx.getContext(windowNo,
						impactedColumnName);
				String newImpactedValue = oldImpactedValue;
				if (impactedField.isLookup()) {
					Lookup impactedLookup = impactedField.getLookup();
					// Update the Locator info with new value
					if (impactedLookup instanceof MLocatorLookup) {
						MLocatorLookup locLookup = (MLocatorLookup) impactedLookup;
						int valueAsInt = 0;
						if (newValue != null) {
							try {
								valueAsInt = Integer.parseInt(newValue);
							} catch (Exception e) {
								log.warning("Cannot Parse " + columnName + "="
										+ newValue);
							}
						}
						if (columnName.equals("M_Warehouse_ID"))
							locLookup.setOnly_Warehouse_ID(valueAsInt);
						if (columnName.equals("M_Product_ID"))
							locLookup.setOnly_Product_ID(valueAsInt);
						locLookup.setOnly_Outgoing(ctx.isSOTrx(windowNo));
						locLookup.refresh();

						Integer M_Locator_ID = ctx.getContextAsInt(windowNo,
								impactedColumnName);

						if (!locLookup.isValid(M_Locator_ID))
							newImpactedValue = null;
						else
							newImpactedValue = M_Locator_ID.toString();

						// we should be changing the locator dropdowns even if
						// the
						// current locator remains valid in the new list
						retValue.addChangedValue(impactedColumnName,
								newImpactedValue);
						ctx.setContext(windowNo, impactedColumnName,
								newImpactedValue);
					}
					//if lookup is of type search or is created updated by, don't do anything
					else if(impactedLookup.getDisplayType() == DisplayTypeConstants.Search || (impactedField.getLookupInfo() != null && impactedField.getLookupInfo().IsCreadedUpdatedBy)) {
						
					}
					else {
						// if the lookup is dynamic (i.e. contains this
						// columnName as variable)
						// if
						// (mLookup.getValidation().indexOf("@"+columnName+"@")
						// != -1)
						ArrayList<NamePair> data = impactedField.getAllLookupData(ctx, windowNo);
						retValue.addChangedDropDown(impactedColumnName, data);
						String key = ctx.getContext(windowNo, impactedColumnName);
						//only set the new value to null if you cannot find the val in the new NamrPair
						if(key.length() == 0 || NamePair.indexOfKey(data, key) == -1) {
							newImpactedValue = null;
							retValue.addChangedValue(impactedColumnName,
									newImpactedValue);
							ctx.setContext(windowNo, impactedColumnName,
									newImpactedValue);
						}
					}
				} else
					log.warning(columnName + ": Impact Not Lookup - "
							+ impactedColumnName);

				// Cascading Dependencies,
				if (impactedField.isImpactsValue()) 
					impactedFields.add(impactedField);
			} // for all impacted fields

			ChangeVO calloutChange = processCallout(ctx, windowNo, field,
					oldValue, newValue);

			retValue.addAll(calloutChange);
			if(calloutChange != null) {
				if (calloutChange.changedContext != null) {
					ctx.setContext(windowNo, calloutChange.changedContext);
					if(log.isLoggable(Level.INFO))
						log.info("Changed context is:"+calloutChange.changedContext);
				}
				if (calloutChange.changedFields != null) {
					ctx.setContext(windowNo, calloutChange.changedFields);
					if(log.isLoggable(Level.INFO))
						log.info("Changed fields are:"+calloutChange.changedFields);
					for(String colName:calloutChange.changedFields.keySet()) {
						//if the changedfield includes myself, no need to call fieldChanged again.
						if(colName.equals(field.getColumnName()))
							continue;
						UIField impactedField = getField(colName);

						if(changedFields.contains(impactedField)) {
							log.info("(changed in callout)field:"+impactedField.getColumnName()+" already changed. don't run again");
							continue;
						}

					if (impactedField!= null){	
						if(!impactedFields.contains(impactedField))
								if(impactedField.isImpactsValue())
									impactedFields.add(impactedField);
					}
					}
				}
			}
			for(UIField impactedField:impactedFields) {
				ChangeVO cascadingChange = fieldChanged(origCtx, ctx, changedFields, windowNo,
						impactedField, origCtx.getContext(windowNo, impactedField.getColumnName()), ctx.getContext(windowNo, impactedField.getColumnName()));
				retValue.addAll(cascadingChange);
			}

		}
		// for org id and client id, we are just here to veryfiy if role level
		// security is ok
		if (field.getColumnName().equals("AD_Org_ID")
				|| field.getColumnName().equals("AD_Client_ID")) {
			canUpdate(ctx, windowNo, retValue);
		}
		//
		return retValue.cleanup();
	} // fieldChanged

	public void canUpdate(CContext ctx, int windowNo, ChangeVO change) {
		MRole role = MRole.get(ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(),
				false);
		int AD_Table_ID = getAD_Table_ID();
		int AD_Client_ID = Integer.parseInt(ctx.getContext(windowNo,
		"AD_Client_ID"));
		int AD_Org_ID = Integer.parseInt(ctx.getContext(windowNo, "AD_Org_ID"));

		if (!role.canUpdate(AD_Client_ID, AD_Org_ID, AD_Table_ID, 0, false))
			change.addParam(ChangeVO.CURRENT_ROW_UPDATEABLE_STRING, "N");
		else
			change.addParam(ChangeVO.CURRENT_ROW_UPDATEABLE_STRING, "Y");

	}

	/***************************************************************************
	 * Process Callout(s).
	 * <p>
	 * The Callout is in the string of "class.method;class.method;" If there is
	 * no class name, i.e. only a method name, the class is regarded as
	 * CalloutSystem. The class needs to comply with the Interface Callout. For
	 * a limited time, the old notation of Sx_matheod / Ux_menthod is
	 * maintained.
	 * 
	 * @see org.compiere.model.Callout
	 */
	private ChangeVO processCallout(CContext ctx, int windowNo, UIField field,
			String oldValue, String newValue) {
		String callout = field.getCallout();
		if (!field.isCallout() && Util.isEmpty(callout))
			return null;
		//
		if (ctx.isProcessed(windowNo)) { // only active records
			log.warning("Record processed - Ignored Callout "
					+ field.getColumnName());
			return null; // "DocProcessed";
		}

		ChangeVO retValue = new ChangeVO();
		// PO
		Map<String, String> context = ctx.getMap(windowNo);
		MTable table = MTable.get(ctx, getAD_Table_ID());
		PO po = null;
		try {
			po = table.getPO(ctx, context);
			po.set_ChangeVO(retValue);
		} catch (Exception e1) {
			retValue.addError("Cannot create PO - " + e1.getLocalizedMessage());
			log.log(Level.WARNING, "Cannot create PO", e1);
			return retValue;
		}

		if (field.isCallout()) {
			log.fine(field.getColumnName() + "=" + newValue + " - old="
					+ oldValue);
			processCalloutDirect(retValue, po, windowNo, field, oldValue,
					newValue);
			CThreadUtil.setCalloutActive(true);
			if (Util.isEmpty(callout)) // nothing else
				return retValue;
		}

		// External Callout
		StringTokenizer st = new StringTokenizer(callout, ";,", false);
		while (st.hasMoreTokens()) // for each callout
		{
			String cmd = st.nextToken().trim();
			log.fine(field.getColumnName() + "=" + newValue + " (" + callout
					+ ") - old=" + oldValue);
			processCalloutExternal(retValue, po, windowNo, field, oldValue,
					newValue, cmd);
			CThreadUtil.setCalloutActive(true);
			if (retValue.hasError())
				break;
		} // for each callout
		return retValue;
	} // processCallout

	/**
	 * Process individual external Callout
	 * 
	 * @param change
	 *            change VO
	 * @param po
	 *            the PO
	 * @param windowNo
	 *            window no
	 * @param field
	 *            field
	 * @param oldValue
	 *            old value
	 * @param newValue
	 *            new value
	 * @param cmd
	 *            command for individual callout (class.method)
	 */
	private void processCalloutExternal(ChangeVO change, PO po, int windowNo,
			UIField field, String oldValue, String newValue, String cmd) {
		if (Util.isEmpty(cmd))
			return;
		int index = cmd.lastIndexOf(".");
		if (index == -1)
			return;

		String className = cmd.substring(0, index);
		String methodName = cmd.substring(index + 1);
		Class<?> clazz = null;
		Object instance = null;
		try {
			clazz = Class.forName(className);
			instance = clazz.newInstance();
		} catch (Exception e) {
			log.log(Level.WARNING, "Class: " + cmd, e);
			change.addError("Class not found: " + className + " ("
					+ e.toString() + ")");
			return;
		}
		// (PO po, UIField field, String oldValue, String newValue)
		Method method = null;
		try {
			method = clazz.getMethod(methodName, PO.class, UIField.class,
					String.class, String.class, Integer.TYPE);
			method.invoke(instance, po, field, oldValue, newValue, windowNo);
		} catch (Exception e) {
			if (method == null) // not found
				return;
			log.log(Level.WARNING, "Method: " + cmd, e);
			change
			.addError("Callout error: " + cmd + " (" + e.toString()
					+ ")");
		}
	} // processCalloutExternal

	/**
	 * Direct Callout to model class.
	 * 
	 * @param change
	 *            change VO
	 * @param po
	 *            the po
	 * @param windowNo
	 * @param field
	 * @param oldValue
	 * @param newValue
	 */
	private void processCalloutDirect(ChangeVO change, PO po, int windowNo,
			UIField field, String oldValue, String newValue) {
		Method method = null;
		String methodName = "set" + field.getColumnName();
		try {
			Class<?>[] params = new Class[] { String.class, String.class,
					Integer.TYPE };
			method = po.getClass().getMethod(methodName, params);
		} catch (NoSuchMethodException e1) {
			return;
		} catch (Exception e2) {
			String msg = po.getClass() + ": No Method " + methodName;
			change.addError(msg + " - " + e2.getLocalizedMessage());
			log.log(Level.WARNING, msg, e2);
			return;
		}

		try {
			Object[] args = new Object[] { oldValue, newValue, windowNo };
			method.invoke(po, args); // void method
		} catch (Exception e3) {
			String msg;
			if (e3.getCause() != null)
				msg = e3.getCause().getLocalizedMessage();
			else
				msg = e3.getLocalizedMessage();
			if (msg == null || msg.length() == 0)
				msg = e3.toString();
			change.addError(po.getClass() + "." + methodName + ": " + msg);
			log.log(Level.WARNING, po.getClass() + "." + methodName, e3);
		}
		return;
	} // processCalloutDirect

	/**
	 * Whether this tab can be exported by the present role.
	 * 
	 * @param ctx
	 *            context
	 * @return true if role can export
	 */
	public boolean isCanExport(Ctx ctx) {
		MRole role = MRole.getDefault(ctx, false);
		return role.isCanExport(getAD_Table_ID());
	} // isCanExport

	/**
	 * Whether this tab can be reported by the present role.
	 * 
	 * @param ctx
	 *            context
	 * @return true if role can export
	 */
	public boolean isCanReport(Ctx ctx) {
		MRole role = MRole.getDefault(ctx, false);
		return role.isCanReport(getAD_Table_ID());
	} // isCanReport

	/**
	 * String Representation
	 * 
	 * @return info
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("UITab[").append(getAD_Tab_ID())
		.append("-").append(getName());
		if (p_vos != null)
			sb.append(";#Fields=").append(p_vos.size());
		sb.append("]");
		return sb.toString();
	} // toString

	public void refreshDropDowns(ChangeVO change, int windowNo, CContext newCtx) {
		// now make a new hashamp of the current state
		if (change.changedContext != null)
			newCtx.setContext(windowNo, change.changedContext);
		if (change.changedFields != null)
			newCtx.setContext(windowNo, change.changedFields);
		if (change.rowData != null) {
			log.info("found row data, now merge");
			int i = 0;
			for (UIField f : getFields()) {
				newCtx.setContext(windowNo, f.getColumnName(),
						change.rowData[i]);
				i++;
			}
		}

		// ctx.addWindow(windowNo, context);
		// now requery those dependent dropdowns
		for (UIField f : getFields()) {
			String colName = f.getColumnName();
			// TODO now i get values for all lookups, whereas i only need value
			// for info window
			if (f.isLookup() && f.isDependentValue()) {
				// we should refresh the dropdown as long as we've added the
				// name to the
				// changedFields hashmap
				if (change.changedFields.containsKey(colName)) {
					if (change.changedDropDowns == null)
						change.changedDropDowns = new HashMap<String, ArrayList<NamePair>>();
					ArrayList<NamePair> arry = change.changedDropDowns
					.get(colName);
					ArrayList<NamePair> data = f.getAllLookupData(newCtx,
							windowNo);
					if (arry == null) {
						// if it is a lookup, and depends on others for value,
						// requery
						// arry.add(new ValueNamePair(id,
						// f.getLookupDisplay(m_context, windowNo, id)));
						change.changedDropDowns.put(colName, data);
						// }
					} else {
						arry.clear();
						arry.addAll(data);
					}

				}
			}
		}

	}

	public int getRecord_ID(String[] row) {
		int keyColumnPos = getKeyColumnPos();
		if (keyColumnPos == -1)
			return -1;
		return Integer.parseInt(row[keyColumnPos]);

	}

	public int getKeyColumnPos() {
		if (m_keyColumns == null)
			createColumnLists();
		return m_keyColumnPos;
	}
	
	/**
	 * Validate if the current tab record or any parent record has changed in database 
	 * @return true if there are changes
	 */
	public boolean hasChangedCurrentTabAndParents(CContext ctx, int windowNo) 
	{
		String msg = null;		
				
		if (isDetailTab()) 
		{
			// get parent tab
			// the parent tab is the first tab above with level = this_tab_level-1
			int level = this.getTabLevel();
			for (int i = m_window.getTabIndex(this) - 1; i >= 0; i--) 
			{
				UITab parentTab = m_window.getTab(i);
				if (parentTab.getTabLevel() == level-1) 
				{
					// hack for SR 10021921 to work around fix for SR 10020916
					if(getTableName().equals("C_InvoicePaySchedule")){
						return false;
					}
					
					// this is parent tab					
					if (parentTab.hasChanged(ctx, windowNo)) 
					{
						// return error stating that current record has changed and it cannot be saved
						msg = Msg.getMsg(Env.getCtx(), "ParentRecordModified") + ": " + parentTab.getName();
						log.saveError("ParentRecordModified", msg, false);
						return true;
					} 
					else 
					{
						// search for the next parent
						if (parentTab.isDetailTab()) 
						{
							level = parentTab.getTabLevel();
						} 
						else 
						{
							break;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * verify if the current record has changed
	 * @param row current record
	 * @return true if changed, false otherwise
	 */
	public boolean hasChanged(CContext ctx, int windowNo) 
	{		
		// compare Updated, Processed.
		// get the Timestamp on Updated column and the value of Processed column
		// from result set and compare it against the DB.
		// if the values are different, the record is assumed to be changed.
		
			//check if row has updated and processed columns.
			int colUpdated = getFieldIndex("Updated");
			int colProcessed = getFieldIndex("Processed");
			boolean hasUpdated = (colUpdated > 0);
			boolean hasProcessed = (colProcessed > 0);

			String columns = null;
			if (hasUpdated && hasProcessed) 
			{
				columns = new String("Updated, Processed");
			} 
			else if (hasUpdated) 
			{
				columns = new String("Updated");
			} 
			else if (hasProcessed) 
			{
				columns = new String("Processed");
			} 
			else 
			{
				// no columns updated or processed to compare
				return false;
			}

			
			Timestamp dbUpdated = null;
			String dbProcessedS = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;			
			String stringID = ctx.getContext(windowNo, this.getKeyColumnName());
			int Record_ID = 0;
			
			try {
				Record_ID = Integer.parseInt(stringID);
			} catch (NumberFormatException e) {
				// sometimes the parent keys are obscured by the columns in child tabs.  In this case, skip the check
				// and assume ok.  see SR 10022088
				return false;
			}
			
			String sql = "SELECT " + columns + " FROM " + this.getTableName() + " WHERE " + this.getTableName() + "_ID=?";
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, Record_ID );
				rs = pstmt.executeQuery();
				if (rs.next()) 
				{
					int idx = 1;
					if (hasUpdated)
						dbUpdated = rs.getTimestamp(idx++);
					if (hasProcessed)
						dbProcessedS = rs.getString(idx++);
				}
				else
					log.info("No Value " + sql);
			}
			catch (SQLException e) {
				throw new DBException(e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			if (hasUpdated) 
			{
				//TODO : Check for Updated Timestamp
				//imestamp memUpdated = null;
				//memUpdated = (Timestamp) po.get_Value("Updated");
				//compare the Timestamps.
				//if (! memUpdated.equals(dbUpdated))
					//return true;
				
								
				//return true;
			}

			if(hasProcessed)
			{
				//compare the Processed column
				Boolean memProcessed = null;
				memProcessed = ctx.getContext(windowNo, "Processed").equalsIgnoreCase("Y");
				Boolean dbProcessed = Boolean.TRUE;
				if (! dbProcessedS.equals("Y"))
					dbProcessed = Boolean.FALSE;
				if (! memProcessed.equals(dbProcessed))
					return true;
			}
		

		return false;
	}
	
	

} // UITab

