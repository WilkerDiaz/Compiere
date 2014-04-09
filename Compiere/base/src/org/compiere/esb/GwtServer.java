/*******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution * Copyright (C) 1999-2007
 * ComPiere, Inc. All Rights Reserved. * This program is free software, you can
 * redistribute it and/or modify it * under the terms version 2 of the GNU
 * General Public License as published * by the Free Software Foundation. This
 * program is distributed in the hope * that it will be useful, but WITHOUT ANY
 * WARRANTY, without even the implied * warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. * See the GNU General Public License for more
 * details. * You should have received a copy of the GNU General Public License
 * along * with this program, if not, write to the Free Software Foundation,
 * Inc., * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA. * For the text
 * or an alternative of this public license, you may reach us * ComPiere, Inc.,
 * 3600 Bridge Parkway #102, Redwood City, CA 94065, USA * or via
 * info@compiere.org or http://www.compiere.org/license.html *
 ******************************************************************************/
package org.compiere.esb;

import java.math.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.controller.*;
import org.compiere.framework.Query;
import org.compiere.framework.QueryRestriction;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.vos.*;
import java.util.regex.PatternSyntaxException;

/**
 * GWT Server Implementation. You maintain one instance per User
 * 
 * @author Jorg Janke, dzhao
 * @version $Id$
 */
public class GwtServer {

	public static void resetWinDefCache() {
		Userdef_Winids.reset();
		UIWindows.reset();
	}

	private static final CCache<WindowVOCacheKey, Integer> Userdef_Winids = new CCache<WindowVOCacheKey, Integer>(
			"AD_Global_WindowVO", 200, 120);

	// use only one window cache
	private static final CCache<WindowCacheKey, UIWindow> UIWindows = new CCache<WindowCacheKey, UIWindow>(
			"AD_Global_Window", 2000, 120);

	/** Logger */
	private static final CLogger log = CLogger.getCLogger(GwtServer.class);
	/** Server ID */
	private static AtomicInteger s_gwtServer_ID = new AtomicInteger(1);

	/***************************************************************************
	 * Gwt Server
	 */
	public GwtServer() {
		m_context = new GWTServerContext();
		m_context.setContext(MRole.GWTSERVERID, s_gwtServer_ID
				.getAndIncrement());
	} // GwtServer

	/** Context */
	private final GWTServerContext m_context;

	/** Login */
	private Login m_login = null;

	/** Locale */
	private Locale m_loc = null;

	/** Role for User */
	private MRole m_role = null;

	/** Window Cache */
	// private final HashMap<Integer, UIWindow> m_windows
	// = new HashMap<Integer, UIWindow>(20);
	/** Tab Cache */
	private final HashMap<Integer, UITab> m_tabs = new HashMap<Integer, UITab>(
			20);
	private final HashMap<Integer, UITab> m_referencetabs = new HashMap<Integer, UITab>(
			20);

	/** Field Cache */
	private final HashMap<Integer, UIField> m_fields = new HashMap<Integer, UIField>(
			200);

	/** Tab Results */
	private final HashMap<Integer, ArrayList<String[]>> m_results = new HashMap<Integer, ArrayList<String[]>>();
	
	/** Dashboard Drilldowns */
	private final HashMap<String, NodeVO> m_nodes = new HashMap<String, NodeVO>();

	/**
	 * Get Login
	 * 
	 * @return login
	 */
	public Login getLogin() {
		if (m_login == null)
			m_login = new Login(m_context);
		return m_login;
	} // getLogin

	/**
	 * Returns the context associated with this GwtServer
	 * 
	 * @return context
	 */
	public CContext getContext() {
		return m_context;
	} // getContext

	/**
	 * Get Role for User
	 * 
	 * @return role
	 */
	public MRole getRole() {
		if (m_role == null) {
			if (m_login == null || m_login.getRole() == null
					|| m_login.getAD_Role_ID() == -1
					|| m_login.getAD_User_ID() == -1)
				throw new IllegalArgumentException("Not logged in yet");
			m_role = m_login.getRole();
		}
		return m_role;
	} // getRole

	/**
	 * Set Locale
	 * 
	 * @param loc
	 *            locale (from login)
	 */
	public void setLocale(Locale loc) {
		m_loc = loc;
	} // setLocale

	/**
	 * Logout
	 * 
	 * @param expired
	 *            expire
	 */
	public void logout(boolean expired) {

		// End Session
		MSession session = MSession.get(m_context); // finish
		if (session != null) {
			if (expired) {
				if (session.getDescription() == null)
					session.setDescription("Expired");
				else
					session.setDescription(session.getDescription()
							+ " Expired");
			}
			session.logout(); // saves
		}
		if (m_context != null) {
			int gwtServerID = m_context.getContextAsInt(MRole.GWTSERVERID);
			if (gwtServerID > 0)
				MRole.resetGwt(gwtServerID);
		}
		// Clear Cache
		m_tabs.clear();
		m_fields.clear();
		// m_windows.clear();
		m_context.clear();
		m_results.clear();
		//

	} // logout

	public boolean isLogout() {
		return m_context.size() == 0;
	}

	/**
	 * Finalize. Remove Role
	 */
	@Override
	protected void finalize() throws Throwable {
		if (m_context != null) {
			int gwtServerID = m_context.getContextAsInt(MRole.GWTSERVERID);
			if (gwtServerID > 0)
				MRole.resetGwt(gwtServerID);
		}
		super.finalize();
	} // finalize

	/**
	 * Get Locale
	 * 
	 * @return Locale
	 */
	public Locale getLocale() {
		if (m_loc == null)
			return Locale.US;
		return m_loc;
	} // getLocale

	/**
	 * Get Menu
	 * 
	 * @return menu as array list
	 */
	public ArrayList<CTreeNode> getMenuTree() {
		int AD_Tree_ID = getTreeID();
		log.fine("AD_Tree_ID=" + AD_Tree_ID + " - "
				+ Env.getAD_Language(m_context));
		return getMenuTree(AD_Tree_ID, false);
	} // getMenuTree

	/**
	 * Get Tree ID for role
	 * 
	 * @return AD_Tree_ID as int
	 */
	private int getTreeID() {
		int AD_Role_ID = m_context.getAD_Role_ID(); 
		// Load Menu Structure ----------------------
		int AD_Tree_ID = QueryUtil
		.getSQLValue(
				null,
				"SELECT COALESCE(r.AD_Tree_Menu_ID, ci.AD_Tree_Menu_ID)"
				+ "FROM AD_ClientInfo ci"
				+ " INNER JOIN AD_Role r ON (ci.AD_Client_ID=r.AD_Client_ID) "
				+ "WHERE AD_Role_ID=?", AD_Role_ID);
		if (AD_Tree_ID <= 0)
			AD_Tree_ID = 10; // Menu
		return AD_Tree_ID;
	} // getTreeID

	/**
	 * Get Menu favorites for a user
	 * 
	 * @return menu as array list
	 */
	public ArrayList<CTreeNode> getMenuFavorites() {
		MUser user = MUser.get(getContext());
		int AD_Tree_ID = user.getAD_Tree_MenuFavorite_ID();
		if (AD_Tree_ID == 0) // favorites has not yet been created
			return new ArrayList<CTreeNode>();
		return getMenuTree(AD_Tree_ID, false);
	}// get favorites menu

	/**
	 * Get Menu tree that directly enter "create new" mode
	 * 
	 * @return menu as array list
	 */
	public ArrayList<CTreeNode> getMenuCreateNew() {
		MUser user = MUser.get(getContext());
		int AD_Tree_ID = user.getAD_Tree_MenuNew_ID();
		if (AD_Tree_ID == 0) // create new has not yet been created
			return new ArrayList<CTreeNode>();
		return getMenuTree(AD_Tree_ID, false);
	}// getMenuCreateNew

	/**
	 * Get a menu tree representation based on a AD_Tree_ID
	 * 
	 * @param AD_Tree_ID
	 *            A tree based on AD_Menu
	 * @return menu as array list
	 */
	private ArrayList<CTreeNode> getMenuTree(int AD_Tree_ID, boolean edit) {
		MTree tree = new MTree(m_context, AD_Tree_ID, edit, true, true, null); // Language
		// set
		// in
		// WLogin
		// Trim tree
		CTreeNode root = tree.getRoot();
		Enumeration<?> en = root.preorderEnumeration();
		while (en.hasMoreElements()) {
			CTreeNode nd = (CTreeNode) en.nextElement();
			if (nd.isTask() || nd.isWorkbench() // || nd.isWorkFlow()
					// server
			) {
				CTreeNode parent = (CTreeNode) nd.getParent();
				parent.remove(nd);
			}
		}
		tree.trimTree();
		en = root.preorderEnumeration();
		ArrayList<CTreeNode> retValue = new ArrayList<CTreeNode>();
		while (en.hasMoreElements()) {
			CTreeNode nd = (CTreeNode) en.nextElement();
			// Issue #420: removed menu entries for un-implemented forms
			if (nd.getAD_Form_ID() == 119
					|| nd.getAD_Form_ID() == 102
					//					|| nd.getAD_Workflow_ID() == 106
					//					|| nd.getAD_Workflow_ID() == 104
					//					// Review
					//					|| nd.getAD_Workflow_ID() == 112
					//					// Setup
					//					|| nd.getAD_Workflow_ID() == 113
					//					|| nd.getAD_Workflow_ID() == 110
					//					|| nd.getAD_Workflow_ID() == 111
					// || nd.getAD_Process_ID() == 205
			) {
			} else
				retValue.add(nd);
		}
		return retValue;
	}

	/**
	 * Make Favorites add/remove persistent ("bar" in swing client)
	 * 
	 * @param add
	 *            true if add - otherwise remove
	 * @param Node_ID
	 *            Node ID
	 * @return true if updated
	 */
	public boolean updateFavorites(boolean add, int Node_ID) {
		/*
		 * Code logic now uses MUser to store favorites. TODO:
		 * VTreePanel.barDBupdate should be similarly updated or deprecated for
		 * Swing client.
		 */
		MUser user = MUser.get(getContext());
		return user.addUserMenuFavorite(Node_ID, 0);
	} // updateFavorites

	/**
	 * Update of user favorites for a user with specified ordering for favorites
	 * 
	 * @param menuIDs
	 *            List<Integer> ordered list of menuIDs to put in the tree
	 * @return true if updated
	 */
	public boolean updateFavorites(List<Integer> menuIDs) {
		MUser user = MUser.get(getContext());
		MTree menuTree = null;
		if ((menuTree = user.getUserFavoriteTree()) == null)
			return false;
		return updateUserTree(menuIDs, menuTree);
	}// updateFavorites

	/**
	 * Make create new add/remove persistent ("bar" in swing client)
	 * 
	 * @param add
	 *            true if add - otherwise remove
	 * @param Node_ID
	 *            Node ID
	 * @return true if updated
	 */
	public boolean updateCreateNew(boolean add, int Node_ID) {
		/*
		 * Code logic now uses MUser to store favorites. TODO:
		 * VTreePanel.barDBupdate should be similarly updated or deprecated for
		 * Swing client.
		 */
		MUser user = MUser.get(getContext());
		return user.addUserMenuNewFavorite(Node_ID, 0);
	} // updateCreateNew

	/**
	 * Update of user favorites for a user with specified ordering for favorites
	 * 
	 * @param menuIDs
	 *            List<Integer> ordered list of menuIDs to put in the tree
	 * @return true if updated
	 */
	public boolean updateCreateNew(List<Integer> menuIDs) {
		MUser user = MUser.get(getContext());
		MTree menuTree = null;
		if ((menuTree = user.getUserNewFavoriteTree()) == null)
			return false;
		return updateUserTree(menuIDs, menuTree);
	}// updateCreateNew

	/*
	 * Update of user tree for ordered menu nodes (favorites, create new list)
	 * favorites @param menuIDs List<Integer> ordered list of menuIDs to put in
	 * the tree @param menuTree MTree the tree to be reordered @return true if
	 * updated
	 */
	private boolean updateUserTree(List<Integer> menuIDs, MTree menuTree) {
		CTreeNode root = menuTree.getRoot();
		if (root != null) {
			Enumeration<?> nodes = root.preorderEnumeration();
			while (nodes.hasMoreElements()) {
				CTreeNode nd = (CTreeNode) nodes.nextElement();
				if (!menuIDs.contains(nd.getNode_ID())) {
					MTreeNodeMM node = null;
					if ((node = MTreeNodeMM.get(menuTree, nd.getNode_ID())) != null) {
						if (!node.delete(true))
							return false;
					}
				}
			}
		}
		int seq = 0;
		for (int id : menuIDs) {
			MTreeNodeMM node = null;
			if ((node = MTreeNodeMM.get(menuTree, id)) == null) {
				node = new MTreeNodeMM(menuTree, id);
			}
			node.setSeqNo(++seq);
			if (!node.save())
				return false;
		}
		return true;
	}

	/**
	 * Get Number of open Requests
	 * 
	 * @return number of requests
	 */
	public int getRequests() {
		return GwtServerUtil.getRequests(m_context);
	} // getRequests

	/**
	 * Get number of open Notes
	 * 
	 * @return Number of notes
	 */
	public int getNotes() {
		return GwtServerUtil.getNotes(m_context);
	} // getNotes

	/***************************************************************************
	 * Get Window in default context based on Role
	 * 
	 * @param windowNO
	 *            relative window
	 * @param AD_Window_ID
	 *            window
	 * @param AD_Menu_ID
	 *            menu
	 * @return WindowVO or null
	 */
	public UIWindow getWindow(int windowNO, int AD_Window_ID, int AD_Menu_ID) {
		UIWindow win = null;
		// win = m_windows.get(AD_Window_ID);
		// if (win != null)
		// {
		// win.clearLookupCache();
		// return win;
		// }
		UIWindowVOFactory winFactory = new UIWindowVOFactory();
		UIWindowVO winVO = null;
		int AD_UserDef_Win_ID = -1;
		WindowVOCacheKey vokey = new WindowVOCacheKey(AD_Window_ID, m_context
				.getAD_Role_ID(), AD_Menu_ID, Env.getAD_Language(m_context));

		// note, the usage of m_context below in constructing window is only for
		// language, menu, role,
		// and those are already included in the cache key, so we can safely
		// assume the win is correctly cached
		if (Userdef_Winids.get(null, vokey) == null) {
			winVO = winFactory.get(m_context, AD_Window_ID, AD_Menu_ID);
			if(winVO == null) {
				log.config("No Window - AD_Window_ID=" + AD_Window_ID
						+ ",AD_Menu_ID=" + AD_Menu_ID);
				return null; 
			}
			int theAD_UserDef_Win_ID = winVO.getAD_UserDef_Win_ID();
			
			if(Userdef_Winids.putIfAbsent(vokey, theAD_UserDef_Win_ID) == null)
				AD_UserDef_Win_ID = theAD_UserDef_Win_ID;
		} else
			AD_UserDef_Win_ID = Userdef_Winids.get(m_context, vokey);

		WindowCacheKey key = new WindowCacheKey(AD_Window_ID,
				AD_UserDef_Win_ID, m_context.getAD_Role_ID(), AD_Menu_ID, Env
				.getAD_Language(m_context));
		win = UIWindows.get(null, key);
		if (win == null) {
			// log.warning("key:" + key + " not found, create");
			if (winVO == null)
				winVO = winFactory.get(m_context, AD_Window_ID, AD_Menu_ID);
			if (winVO == null) {
				log.config("No Window - AD_Window_ID=" + AD_Window_ID
						+ ",AD_Menu_ID=" + AD_Menu_ID);
				return null;
			}
			UIWindow newWin = new UIWindow(winVO);
			AD_Window_ID = newWin.getAD_Window_ID();
			//
			UIFieldVOFactory fieldFactory = new UIFieldVOFactory();
			newWin.setFields(fieldFactory.getAll(m_context, AD_Window_ID,
					AD_UserDef_Win_ID));
			//
			UITabVOFactory tabFactory = new UITabVOFactory();
			// setTabVOs initrlize tabs but not fields, 'cuz fields needs to be
			// copied over and initialized later
			newWin.setTabVOsWithFieldsUninitialized(m_context, tabFactory.getAll(
					m_context, AD_Window_ID, AD_UserDef_Win_ID), windowNO);
			win = UIWindows.putIfAbsent(key, newWin);
			if(win == null)
				win = newWin;
		}
		// deep copy the window object so we hold a separate window object for
		// each user session
		UIWindow duplicatedWin = (UIWindow) DeepCopy.copy(win);
		log.fine(duplicatedWin.toString());
		fillTabsFieldsAndInitFieldsAndCreateDependencyRelations(duplicatedWin, windowNO);

		MSession session = MSession.get(m_context);
		if (session != null)
			session.windowLog(m_context.getAD_Client_ID(), m_context
					.getAD_Org_ID(), duplicatedWin.getAD_Window_ID(), 0);

		return duplicatedWin;
	} // getWindowVO

	/**
	 * Get Tab with ID
	 * 
	 * @param AD_Tab_ID
	 * @return tab or null
	 */
	public UITab getTab(int AD_Tab_ID) {
		Integer tabKey = Integer.valueOf(AD_Tab_ID);
		UITab tab = m_tabs.get(tabKey);
		if (tab == null) {
			// Check added for referenced tabs
			if(m_referencetabs.get(tabKey) !=null)
				return m_referencetabs.get(tabKey);
			throw new CompiereStateException("No such tab:" + AD_Tab_ID);
		} // find in window
		return tab;
	} // getTab

	/**
	 * Get Field
	 * 
	 * @param AD_Field_ID
	 *            id
	 * @param windowNo
	 *            relative windowNo
	 * @return field or null
	 */
	public UIField getField(int AD_Field_ID, int windowNo) {
		Integer key = Integer.valueOf(AD_Field_ID);
		UIField field = m_fields.get(key);
		if (field == null) {
			UIFieldVOFactory fieldFactory = new UIFieldVOFactory();
			UIFieldVO vo = fieldFactory.get(m_context, AD_Field_ID);
			// m_context.setSOTrx(windowNo, isSOTrx);
			if (vo != null) {
				field = new UIField(vo);
				field.initialize(m_context, windowNo);
				log.warning("Loaded directly: " + field); // SOTrx may not
				// be correct
				m_fields.put(key, field); // save in cache
			}
		} // create new
		return field;
	} // getField

	/**
	 * Fill Tab and Field arrays
	 */
	private void fillTabsFieldsAndInitFieldsAndCreateDependencyRelations(UIWindow win, int windowNO) {
		ArrayList<UITab> tabs = win.getTabs();
		for (int j = 0; j < tabs.size(); j++) {
			UITab winTab = tabs.get(j);
			Integer tabKey = Integer.valueOf(winTab.getAD_Tab_ID());
			Integer ReferencetabKey = Integer.valueOf(winTab.getReferenced_Tab_ID());
			m_tabs.put(tabKey, winTab);
			m_referencetabs.put(ReferencetabKey,winTab);
			//
			ArrayList<UIField> fields = winTab.getFields();
			for (int k = 0; k < fields.size(); k++) {
				UIField field = fields.get(k);
				field.initialize(m_context, windowNO);
				Integer fieldKey = Integer.valueOf(field.getAD_Field_ID());
				// set the correct value
				if (field.isLookup())
					field.getLookup().setContext(m_context, windowNO);
				m_fields.put(fieldKey, field);
			}
			winTab.createDependencyRelations();
		}
	} // fillTabsFields

	/**
	 * Execute Query for Tab
	 * 
	 * @param AD_Tab_ID
	 *            tab
	 * @param queryVO
	 *            optional query
	 * @param context
	 *            record context for link columns and other variables
	 * @param queryResultID
	 *            stored query identifier provided by client
	 * @return number of records or -1 if error
	 */
	public int executeQuery(int AD_Tab_ID, QueryVO queryVO,
			HashMap<String, String> context, int queryResultID) {
		UITab tab = getTab(AD_Tab_ID);
		if (tab == null) {
			log.config("Not found AD_Tab_ID=" + AD_Tab_ID);
			return -1;
		}
		ArrayList<String[]> result = tab.executeQueryString(queryVO, context,
				m_context);
		if (result == null) {
			log.config("Not Result for AD_Tab_ID=" + AD_Tab_ID);
			return -1;
		}
		MRole role = getRole();
		// return -1 to indicate query exceeds
		if (role.isQueryMax(result.size())) {
			m_results.put(queryResultID, new ArrayList<String[]>());
			return -1;
		}
		m_results.put(queryResultID, result);
		return result.size();
	} // executeQuery

	public Query createQuery(int AD_Tab_ID, QueryVO queryVO,
			WindowCtx ctx, String tableName) {
		UITab tab = getTab(AD_Tab_ID);
		String whereClause = tab.getWhereClause();
		if (tab == null) {
			log.config("Not found AD_Tab_ID=" + AD_Tab_ID);
			return null;
		}
		Query result = tab.createQueryForReport(m_context, queryVO);
		if (result == null) {
			result = new Query(tableName);
		}
		if(whereClause!=null && whereClause.length()!=0)
		{
			QueryRestriction restriction = new QueryRestriction(whereClause);
			result.addRestriction(restriction);
		}
		return result;
	} // executeQuery

	/**
	 * Retrieve results for Tab. If the from/to range does not exist, it returns
	 * existing rows
	 * 
	 * @param queryResultID
	 *            stored query identifier provided by client
	 * @param fromRow
	 *            from row first is 0
	 * @param noRows
	 *            number of rows
	 * @return array of rows of array of field values or null if error. You get
	 *         the columnNames via String[] columns = uiTab.getColumnNames();
	 */
	public String[][] getResults(int queryResultID, int fromRow, int noRows) {
		if (noRows < 0) {
			log.config("Invalid: fromRow=" + fromRow + ",noRows" + noRows);
		} else if (noRows == 0)
			return new String[][] {};
		//
		ArrayList<String[]> resultAll = m_results.get(queryResultID);
		if (resultAll == null) {
			log.config("No Results for queryResultID=" + queryResultID);
			return null;
		}
		if (resultAll.size() < fromRow) {
			log.config("Insufficient Results for queryResultID="
					+ queryResultID + ", Length=" + resultAll.size()
					+ ", fromRow=" + fromRow);
			return null;
		}
		// copy
		if (resultAll.size() < noRows) {
			log.config("Insufficient Rows for queryResultID=" + queryResultID
					+ ", Length=" + resultAll.size() + ", fromRow=" + fromRow
					+ ", noRows=" + noRows);
			noRows = resultAll.size();
		}
		String[][] result = new String[noRows][];
		for (int i = 0; i < noRows; i++) {
			int index = i + fromRow;
			if (index >= resultAll.size())
				break;
			result[i] = resultAll.get(index);
		}
		return result;
	} // getResult

	public void sortResults(int WindowNo, int AD_Tab_ID, int AD_Field_ID,
			int queryResultID, final boolean ascending) {
		class SortCell {

			String[] row;

			String sort;
		}
		ArrayList<String[]> results = m_results.get(queryResultID);
		if(results == null)
			log.severe("cannot sort. results non-existent for queryResultID:"+queryResultID);
		UITab tab = getTab(AD_Tab_ID);
		final UIField field = getField(AD_Field_ID, WindowNo);
		final int displayType = field.getAD_Reference_ID();
		final int idx = tab.getFieldIndex(AD_Field_ID);
		// if not a lookup, directly sort
		if (!field.isLookup()) {
			if (FieldType.isNumeric(displayType)) {
				Collections.sort(results, new Comparator<String[]>() {

					@Override
					public int compare(String[] o1, String[] o2) {
						if (o1[idx] == null)
							o1[idx] = "";
						if (o2[idx] == null)
							o2[idx] = "";
						BigDecimal s1 = new BigDecimal(
								o1[idx].equals("") ? "-1e-10" : o1[idx]);
						BigDecimal s2 = new BigDecimal(
								o2[idx].equals("") ? "-1e-10" : o2[idx]);
						return ascending ? s1.compareTo(s2) : s2.compareTo(s1);
					}
				});
			} else if (FieldType.isDate(displayType)) {
				Collections.sort(results, new Comparator<String[]>() {

					@Override
					public int compare(String[] o1, String[] o2) {
						if (o1[idx] == null)
							o1[idx] = "";
						if (o2[idx] == null)
							o2[idx] = "";
						Long s1 = new Long(o1[idx].equals("") ? "-1000000"
								: o1[idx]);
						Long s2 = new Long(o2[idx].equals("") ? "-1000000"
								: o2[idx]);
						return ascending ? s1.compareTo(s2) : s2.compareTo(s1);
					}
				});
			} else {
				Collections.sort(results, new Comparator<String[]>() {

					@Override
					public int compare(String[] o1, String[] o2) {
						if (o1[idx] == null)
							o1[idx] = "";
						if (o2[idx] == null)
							o2[idx] = "";
						String s1 = o1[idx];
						String s2 = o2[idx];
						return ascending ? s1.compareTo(s2) : s2.compareTo(s1);
					}
				});
			}
			return;
		}
		Comparator<SortCell> c = new Comparator<SortCell>() {

			public int compare(SortCell o1, SortCell o2) {
				if (ascending)
					return o1.sort.compareTo(o2.sort);
				else
					return o2.sort.compareTo(o1.sort);
			}
		};
		// for look up, first get id values
		ArrayList<String> fieldValues = new ArrayList<String>(results.size());
		for (String[] row : results) {
			fieldValues.add(row[idx]);
		}
		// then translate into real values
		ArrayList<String> sorts = getLookupValueOnlyDirect(AD_Field_ID,
				fieldValues, true);
		ArrayList<SortCell> toBeSorteds = new ArrayList<SortCell>(sorts.size());
		for (int i = 0; i < sorts.size(); i++) {
			SortCell toBeSorted = new SortCell();
			toBeSorted.row = results.get(i);
			toBeSorted.sort = sorts.get(i);
			toBeSorteds.add(toBeSorted);
		}
		// sort
		Collections.sort(toBeSorteds, c);
		// after sorting, replace col with original values
		int i = 0;
		for (SortCell toBeSorted : toBeSorteds) {
			results.set(i, toBeSorted.row);
			i++;
		}
	}

	public void copyQueryResults(int sourceID, int destID) {
		ArrayList<String[]> results = m_results.get(sourceID);
		m_results.put(destID, results);
	}

	// Method to return a list of matches according to fields for a tab, using
	// the cached results
	// this does not store the result in the cache - this behavior is deferred
	// to the caller
	private int searchTabResults(int WindowNo, UITab tab,
			List<Integer> fieldIds, int queryResultID, int searchResultID,
			String query, int rowCount) {
		ArrayList<String[]> results = m_results.get(queryResultID);
		if (query.trim().equals("")) {
			m_results.put(searchResultID, results);
			return results.size();
		}
		ScoreStrategy scorer = new ScoreStrategy(query);
		ScoreCell[] scores = new ScoreCell[results.size()];
		// first initialize score cells
		int j = 0;
		for (String[] result : results) {
			// initialize score cells
			ScoreCell score = new ScoreCell();
			score.row = result;
			score.score = scorer.createScore();
			scores[j++] = score;
		}
		for (int id : fieldIds) {
			UIField field = getField(id, WindowNo);
			final int idx = tab.getFieldIndex(id);
			if (field.isLookup()) {
				ArrayList<String> fieldValues = new ArrayList<String>(results
						.size());
				for (String[] row : results) {
					fieldValues.add(row[idx]);
				}
				ArrayList<String> sorts = getLookupValueOnlyDirect(id,
						fieldValues, true);
				int i = 0;
				for (String value : sorts) {
					scorer.getScore(value, scores[i].score);
					i++;
				}
			} else {
				int i = 0;
				for (String[] row : results) {
					String value = row[idx];
					scorer.getScore(value, scores[i].score);
					i++;
				}
			}
		}
		ArrayList<ScoreCell> matchingScores = new ArrayList<ScoreCell>();
		for (ScoreCell cell : scores) {
			if (cell.score.isMatch) {
				matchingScores.add(cell);
			}
		}
		Collections.sort(matchingScores, scorer);
		ArrayList<String[]> matches = new ArrayList<String[]>();
		for (ScoreCell score : matchingScores) {
			matches.add(score.row);
		}
		m_results.put(searchResultID, matches);
		return matches.size();
	}

	public String[][] getTabSearchResults(int searchResultID, int rowCount) {
		ArrayList<String[]> matches = m_results.get(searchResultID);
		if (matches != null) {
			if (matches.size() < rowCount) {
				rowCount = matches.size();
			}
			String[][] result = new String[rowCount][];
			int i = 0;
			for (String[] row : matches) {
				result[i++] = row;
				if (i == rowCount)
					break;
			}
			return result;
		} else {
			return new String[0][];
		}
	}

	/**
	 * Execute Query for Tab. If the from/to range does not exist, it returns
	 * existing rows
	 * 
	 * @param queryResultID
	 *            stored query identifier provided by client
	 * @param row
	 *            row number first is 0
	 * @return array of rows of array of field values or null if error. You get
	 *         the columnNames via String[] columns = uiTab.getColumnNames();
	 */
	public String[] requery(int queryResultID, int row) {
		// TODO requery
		String[][] results = getResults(queryResultID, row, 1);
		return results[0];
	} // requery

	/**
	 * Release Results
	 * 
	 * @param resultIDs
	 *            stored query identifier provided by client
	 */
	public void disposeWindow(ArrayList<Integer> resultIDs) {
		// System.out.println("before cached id:" + m_results.keySet());
		for (Integer queryResultID : resultIDs)
			m_results.remove(queryResultID);
		// System.out.println("after cached id:" + m_results.keySet());
		m_context.removeAllWindows();
	} // releaseResults

	/**
	 * Create new Row with Default values. The new Row is not saved in Results
	 * 
	 * @param windowNo
	 *            relative window
	 * @param AD_Tab_ID
	 *            tab
	 * @param context
	 *            record context for parent columns and other variables
	 * @return array of field values or null if error. You get the columnNames
	 *         via String[] columns = uiTab.getColumnNames();
	 */
	public ChangeVO newRow(int windowNo, int AD_Tab_ID,
			Map<String, String> context) {
		UITab tab = getTab(AD_Tab_ID);
		if (tab == null) {
			log.config("Not found AD_Tab_ID=" + AD_Tab_ID);
			return null;
		}
		CContext ctx = new CContext(m_context.entrySet());
		ctx.addWindow(windowNo, context);
		ctx.setIsSOTrx(windowNo, tab.isSOTrx());
		ChangeVO change = tab.newRow(ctx, windowNo);
		/**
		 * Very likely not needed if (change.changedDropDowns == null)
		 * change.changedDropDowns = new HashMap<String,ArrayList<NamePair>>();
		 * for(UIField f:tab.getFields()) { if (f.isDependentValue())
		 * change.changedDropDowns.put(f.getColumnName(),
		 * getLookupValues(windowNo, f.getAD_Field_ID(), change.changedFields));
		 * }
		 */
		tab.canUpdate(ctx, windowNo, change);
		return change;
	} // newRow

	/**
	 * Refresh current row of Tab
	 * 
	 * @param windowNo
	 *            relative window
	 * @param AD_Tab_ID
	 *            tab
	 * @param relRowNo
	 *            relative row number in results
	 * @param context
	 *            current (relevant) context of new row
	 * @return error message or null
	 */
	public ChangeVO refreshRow(int windowNo, int AD_Tab_ID, int queryResultID,
			int relRowNo, Map<String, String> context) {
		if (context == null || context.size() == 0)
			return new ChangeVO(true, "No Context");
		UITab tab = getTab(AD_Tab_ID);
		if (tab == null) {
			log.config("Not found AD_Tab_ID=" + AD_Tab_ID);
			return new ChangeVO(true, "@NotFound@ @AD_Tab_ID@=" + AD_Tab_ID);
		}
		CContext ctx = new CContext(m_context.entrySet());
		ctx.addWindow(windowNo, context);
		ChangeVO retValue = tab.refreshRow(ctx, windowNo);
		if (retValue.hasError())
			return retValue;
		// Update Results
		ArrayList<String[]> data = m_results.get(queryResultID);
		if (data == null)
			retValue.addError("Data Not Found");
		else {
			String[] dataRow = retValue.rowData.clone();
			data.set(relRowNo, dataRow);
			postProcessChangeVO(retValue, windowNo, context, dataRow, tab);
			retValue.trxInfo = GridTab.getTrxInfo(tab.getTableName(), ctx,
					windowNo, tab.getTabNo());
		}
		return retValue;
	} // refreshRow

	public ChangeVO updateRow(int windowNo, int AD_Tab_ID, int queryResultID,
			int relRowNo, Map<String, String> context, boolean force) {
		if (context == null || context.size() == 0)
			return new ChangeVO(true, Msg.translate(m_context, "NoContext"));
		ArrayList<String[]> data = m_results.get(queryResultID);
		if (data == null || data.size() == 0)
			return new ChangeVO(true, Msg.translate(m_context, "CachedDataNotFound"));
		UITab tab = getTab(AD_Tab_ID);
		if (tab == null) {
			log.config("Not found AD_Tab_ID=" + AD_Tab_ID);
			return new ChangeVO(true, Msg.translate(m_context,
					"@NotFound@ @AD_Tab_ID@=" + AD_Tab_ID));
		}
		CContext ctx = new CContext(m_context.entrySet());
		ctx.addWindow(windowNo, context);
		ChangeVO retValue;
		if (force)
			retValue = tab.saveRow(ctx, windowNo, false, null);
		else
			retValue = tab.saveRow(ctx, windowNo, false, data.get(relRowNo));
		if (retValue.hasError())
			return retValue;
		// Update Results
		String[] dataRow = retValue.rowData.clone();
		data.set(relRowNo, dataRow);
		postProcessChangeVO(retValue, windowNo, context, dataRow, tab);
		retValue.trxInfo = GridTab.getTrxInfo(tab.getTableName(), ctx,
				windowNo, tab.getTabNo());
		if (retValue.isRefreshAll()) {
		}
		return retValue;
	}

	private void postProcessChangeVO(ChangeVO change, int windowNo,
			Map<String, String> context, String[] dataRow, UITab tab) {
		// make an updated context to get the necessary listboxvos
		Map<String, String> contextAfterUpdate = new HashMap<String, String>(
				context);
		int j = 0;
		for (UIField field : tab.getFields()) {
			contextAfterUpdate.put(field.getColumnName(), dataRow[j]);
			j++;
		}
		// now change rowData to remove password, and reload the changed
		// listboxes
		j = 0;
		for (UIField field : tab.getFields()) {
			// return an empty string for passwords etc
			if (field.isEncryptedField() || field.isEncryptedColumn()
					|| "Password".equals(field.getColumnName()))
				change.rowData[j] = "";
			if (FieldType.isClientLookup(field.getAD_Reference_ID())
					&& field.isDependentValue()) {
				if (change.changedDropDowns == null)
					change.changedDropDowns = new HashMap<String, ArrayList<NamePair>>();
				ArrayList<NamePair> values;
				if (field.getAD_Reference_ID() == DisplayTypeConstants.Search) {
					ArrayList<String> t = new ArrayList<String>(1);
					t.add(context.get(field.getColumnName()));
					values = getLookupValueDirect(field.getAD_Field_ID(), t,
							true);
				} else
					values = getLookupData(windowNo, field.getAD_Field_ID(),
							context, true);
				change.changedDropDowns.put(field.getColumnName(), values);
			}
			j++;
		}
	}

	/**
	 * Save (Update existing) Row of Tab
	 * 
	 * @param windowNo
	 *            relative window
	 * @param AD_Tab_ID
	 *            tab
	 * @param relRowNo
	 *            relative row number in results
	 * @param context
	 *            current (relevant) context of new row
	 * @return error message or null
	 */
	public ChangeVO updateRow(int windowNo, int AD_Tab_ID, int queryResultID,
			int relRowNo, Map<String, String> context) {
		return updateRow(windowNo, AD_Tab_ID, queryResultID, relRowNo, context,
				false);
	} // updateRow

	/**
	 * Save (Insert new) Row of Tab
	 * 
	 * @param windowNo
	 *            relative window
	 * @param AD_Tab_ID
	 *            tab
	 * @param curRow
	 *            insert after relative row number in results
	 * @param context
	 *            current (relevant) context of new row
	 * @return error message or null
	 */
	public ChangeVO insertRow(int windowNo, int AD_Tab_ID, int queryResultID,
			int curRow, Map<String, String> context) {
		if (context == null || context.size() == 0)
			return new ChangeVO(true, "No Context");
		UITab tab = getTab(AD_Tab_ID);
		if (tab == null) {
			log.config("Not found AD_Tab_ID=" + AD_Tab_ID);
			return new ChangeVO(true, "@NotFound@ @AD_Tab_ID@=" + AD_Tab_ID);
		}

		log.info("Line Amt:"+context.get("LineNetAmt"));
		CContext ctx = new CContext(m_context.entrySet());
		ctx.addWindow(windowNo, context);
		ChangeVO retValue = tab.saveRow(ctx, windowNo, true);
		if (retValue.hasError())
			return retValue;
		// Update Results
		ArrayList<String[]> data = m_results.get(queryResultID);
		if (data == null)
			retValue.addError("Data Not Found");
		else {
			String[] dataRow = retValue.rowData;
			if (curRow >= data.size())
				data.add(dataRow);
			else
				data.add(curRow, dataRow);
			retValue.trxInfo = GridTab.getTrxInfo(tab.getTableName(), ctx,
					windowNo, tab.getTabNo());
		}
		return retValue;
	} // insertRow

	/**
	 * Delete existing Row
	 * 
	 * @param windowNo
	 *            relative window
	 * @param AD_Tab_ID
	 *            tab
	 * @param relRowNo
	 *            relative row number in results
	 * @return error message or null
	 */
	public ChangeVO deleteRow(int windowNo, int AD_Tab_ID, int queryResultID,
			int relRowNo) {
		UITab tab = getTab(AD_Tab_ID);
		if (tab == null) {
			log.config("Not found AD_Tab_ID=" + AD_Tab_ID);
			return new ChangeVO(true, "@NotFound@ @AD_Tab_ID@=" + AD_Tab_ID);
		}
		ArrayList<String[]> data = m_results.get(queryResultID);
		if (data == null)
			return new ChangeVO(true, "Data Not Found");
		String[] rowData = data.get(relRowNo);
		// Copy Data into Context
		Map<String, String> context = new HashMap<String, String>();
		String[] columns = tab.getColumnNames();
		for (int i = 0; i < columns.length; i++) {
			String column = columns[i];
			context.put(column, rowData[i]);
		}
		CContext ctx = new CContext(m_context.entrySet());
		ctx.addWindow(windowNo, context);
		ChangeVO retValue = tab.deleteRow(ctx, windowNo);
		if (retValue.hasError())
			return retValue;
		// Update Results
		data.remove(relRowNo);
		return retValue;
	} // deleteRow

	/**
	 * Field Changed
	 * 
	 * @param windowNo
	 *            relative window
	 * @param AD_Field_ID
	 *            field
	 * @param AD_Tab_ID
	 *            tab
	 * @param oldValue
	 *            old field value
	 * @param newValue
	 *            new field value
	 * @param context
	 *            record context
	 * @return Field Change VO
	 */
	public ChangeVO fieldChanged(int windowNo, int AD_Field_ID, int AD_Tab_ID,
			String oldValue, String newValue, Map<String, String> context) {
		// Same Values
		if (oldValue == null || oldValue.equals(Null.NULLString))
			oldValue = "";
		if (newValue == null || newValue.equals(Null.NULLString))
			newValue = "";
		if (oldValue.equals(newValue))
			return null;
		//
		UITab tab = getTab(AD_Tab_ID);
		if (tab == null) {
			log.config("Not found AD_Tab_ID=" + AD_Tab_ID);
			return null;
		}
		UIField field = getField(AD_Field_ID, windowNo);
		if (field == null) {
			log.warning("Cannot find AD_Field_ID=" + AD_Field_ID);
			return null;
		}

		CContext ctx = new CContext(m_context.entrySet());
		ctx.addWindow(windowNo, context);
		CContext origCtx = new CContext(m_context.entrySet());
		origCtx.addWindow(windowNo, context);
		ChangeVO change = null;
		try {
			//reset the thread active flag, in case the thread is reused later on
			CThreadUtil.setCalloutActive(false);
			change = tab.fieldChanged(origCtx, ctx, new ArrayList<UIField>(5), windowNo, field, oldValue,
					newValue);
			CThreadUtil.setCalloutActive(false);
			ctx.setContext(windowNo, field.getColumnName(),
					change.newConfirmedFieldValue);
		} 
		catch(Exception e) {
			log.severe("fieldChange error:"+field.getColumnName()+e.getMessage());
		}
		finally {
			CThreadUtil.setCalloutActive(false);
		}

		return change;
	} // fieldChanged

	/**
	 * Get Field Lookup Value Direct
	 * 
	 * @param windowNo
	 *            Window
	 * @param AD_Field_ID
	 * @param keyValues
	 *            array of id values
	 * @param cache
	 * @return list of display values
	 */
	public ArrayList<NamePair> getLookupValueDirect(int AD_Field_ID,
			ArrayList<String> keyValues, boolean cache) {
		int windowNo = 0; // No Context
		ArrayList<NamePair> displayValues = new ArrayList<NamePair>();
		UIField field = getField(AD_Field_ID, windowNo);

		// if (cache && field.isLookup())
		// field.getLookup().removeAllElements();
		if (field == null)
			log.warning("Cannot find AD_Field_ID=" + AD_Field_ID);
		//
		for (int i = 0; i < keyValues.size(); i++) {
			String key = keyValues.get(i);
			String value = null;
			if (field != null)
				value = field.getLookupDisplay(m_context, windowNo, key, cache);
			if (value == null) {
				/*
				 * if(key == null) value = ""; else value = "<" + key + ">";
				 */
				value = "";
			}
			NamePair pp = new ValueNamePair(key, value);
			displayValues.add(pp);
		}
		return displayValues;
	} // getLookupValueDirect

	/**
	 * Get Field Lookup Value Direct
	 * 
	 * @param windowNo
	 *            Window
	 * @param AD_Field_ID
	 * @param keyValues
	 *            array of id values
	 * @param cache
	 * @return list of display values
	 */
	public ArrayList<String> getLookupValueOnlyDirect(int AD_Field_ID,
			ArrayList<String> keyValues, boolean cache) {
		int windowNo = 0; // No Context
		ArrayList<String> displayValues = new ArrayList<String>();
		UIField field = getField(AD_Field_ID, windowNo);
		if (field == null)
			log.warning("Cannot find AD_Field_ID=" + AD_Field_ID);
		//
		for (int i = 0; i < keyValues.size(); i++) {
			String key = keyValues.get(i);
			String value = null;
			if (field != null)
				value = field.getLookupDisplay(m_context, windowNo, key, cache);
			if (value == null) {
				/*
				 * if(key == null) value = ""; else value = "<" + key + ">";
				 */
				value = "";
			}
			displayValues.add(value);
		}
		return displayValues;
	} // getLookupValueDirect

	/**
	 * Get Lookup Data for Field in context
	 * 
	 * @param AD_Field_ID
	 *            field
	 * @param context
	 *            context
	 * @param refresh
	 *            requery
	 * @return lookup pair array
	 */
	public ArrayList<NamePair> getLookupData(int windowNo, int AD_Field_ID,
			Map<String, String> context, boolean refresh) {
		UIField field = getField(AD_Field_ID, windowNo);
		if (field == null) {
			log.warning("Cannot find AD_Field_ID=" + AD_Field_ID);
			return null;
		}
		CContext ctx = new CContext(m_context.entrySet());
		ctx.addWindow(windowNo, context);
		if (field.isLookup() || field.isButtonLookup())
			return field.getAllLookupData(ctx, windowNo);
		else
			log.warning("No Lookup: " + field.getColumnName());
		return null;
	} // getLookupData

	/***************************************************************************
	 * Get All Lookup Data for fields w/o context dependency
	 * 
	 * @param AD_Tab_ID
	 *            tab
	 * @return map if FiledName and lookup pair array public
	 *         Map<String,NamePair[]> getLookupDataAll (int AD_Tab_ID) { return
	 *         null; } // getLookuupDataAll /**
	 **************************************************************************/
	private static int curZoomWindowNO = 0;

	public int getZoomWindowNO() {
		curZoomWindowNO += 100;
		return curZoomWindowNO;
	}

	public Boolean savePreferences(Map<String, String> ctx) {
		CContext cContext = getContext();
		MUser user = MUser.get(cContext);
		MUserPreference preference = user.getPreference();
		String printerName = ctx.get("PrinterName");
		if (printerName != null && printerName.trim().equalsIgnoreCase("")) {
			cContext.setPrinterName(printerName);
			preference.setPrinterName(printerName);
		}
		String autoCommit = ctx.get("AutoCommit");
		if (autoCommit != null) {
			cContext.setAutoCommit(autoCommit.trim().equalsIgnoreCase("Y"));
			preference.setIsAutoCommit(autoCommit.trim().equalsIgnoreCase("Y"));
		}
		String showAdvanced = ctx.get("#ShowAdvanced");
		if (showAdvanced != null) {
			cContext.setContext("#ShowAdvanced", showAdvanced);
			preference.setIsShowAdvanced(showAdvanced.trim().equalsIgnoreCase(
			"Y"));
		}
		String showAccounting = ctx.get("#ShowAcct");
		if (showAccounting != null) {
			cContext.setContext("#ShowAcct", showAccounting);
			preference.setIsShowAcct(showAccounting.trim()
					.equalsIgnoreCase("Y"));
		}
		String showTranslation = ctx.get("#ShowTrl");
		if (showTranslation != null) {
			cContext.setContext("#ShowTrl", showTranslation);
			preference.setIsShowTrl(showTranslation.trim()
					.equalsIgnoreCase("Y"));
		}
		String uiTheme = ctx.get("#UITheme");
		if (uiTheme != null && !uiTheme.trim().equalsIgnoreCase("")) {
			cContext.setContext("#UITheme", uiTheme);
			preference.setUITheme(uiTheme);
		}

		String printPreview = ctx.get("#PrintPreview");
		if (printPreview != null) {
			cContext.setPrintPreview(printPreview.equalsIgnoreCase("Y"));
			Ini.setProperty(Ini.P_PRINTPREVIEW, printPreview
					.equalsIgnoreCase("Y"));
			Ini.saveProperties(Ini.isClient());
		}

		String date = ctx.get("#Date");
		cContext.setContext("#Date", date);

		return preference.save();
	}

	public Boolean deleteSavedSearch(int tab_ID, String savedSearchName) {
		CContext cContext = getContext();
		MUserQuery query = MUserQuery.getForUser(cContext, tab_ID,
				savedSearchName);
		if (query != null)
			if (query.deleteLines()) {
				if (query.delete(true)) {
					return true;
				}
			}
		return false;
	}// deleteSavedsearch

	private static class WindowCacheKey extends WindowVOCacheKey {

		WindowCacheKey(int AD_Window_ID, int AD_UserDef_Win_ID, int AD_Role_ID,
				int AD_Menu_ID, String AD_Language) {
			super(AD_Window_ID, AD_Role_ID, AD_Menu_ID, AD_Language);
			this.AD_UserDef_Win_ID = AD_UserDef_Win_ID;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof WindowCacheKey))
				return false;
			WindowCacheKey key = (WindowCacheKey) obj;
			return super.equals(obj)
			&& AD_UserDef_Win_ID == key.AD_UserDef_Win_ID;
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public String toString() {
			return super.toString() + AD_UserDef_Win_ID;
		}

		int AD_UserDef_Win_ID;
	}

	private static class WindowVOCacheKey {

		WindowVOCacheKey(int AD_Window_ID, int AD_Role_ID, int AD_Menu_ID,
				String AD_Language) {
			this.AD_Window_ID = AD_Window_ID;
			this.AD_Role_ID = AD_Role_ID;
			this.AD_Menu_ID = AD_Menu_ID;
			this.AD_Language = AD_Language;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof WindowVOCacheKey))
				return false;
			WindowVOCacheKey key = (WindowVOCacheKey) obj;
			return AD_Window_ID == key.AD_Window_ID
			// && AD_UserDef_Win_ID == key.AD_UserDef_Win_ID
			&& AD_Role_ID == key.AD_Role_ID
			&& AD_Menu_ID == key.AD_Menu_ID
			&& AD_Language.equals(key.AD_Language);
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public String toString() {
			return AD_Language + AD_Window_ID + AD_Role_ID + AD_Menu_ID;
		}

		int AD_Window_ID;

		int AD_Role_ID;

		int AD_Menu_ID;

		String AD_Language;
	}

	private static class ScoreStrategy implements Comparator<ScoreCell> {

		// TODO: populate from locale to strip out the/a/and etc...
		private String[] terms;

		private String query;

		public ScoreStrategy(String queryString) {
			HashSet<String> unique = new HashSet<String>();
			this.query = queryString.trim().toLowerCase();
			for (String term : this.query.split("\\s+")) {
				if (!unique.contains(term)) {
					unique.add(term);
				}
			}
			this.terms = new String[unique.size()];
			int i = 0;
			for (String term : unique) {
				this.terms[i++] = term;
			}
		}

		public Score createScore() {
			Score score = new Score();
			score.terms = new int[terms.length];
			return score;
		}

		public void getScore(String value3, Score score) {
			if (value3 == null || value3.trim().equals(""))
				return;
			String value2 = value3.toLowerCase();
			if (query.equals(value2)) {
				score.equalsMatches++;
				score.isMatch = true;
				return;
			}
			try {
				int occurrences = value2.length()
				- value2.replaceAll(query, "").length();
				if (occurrences > 0) {
					score.completeMatches += occurrences;
					score.isMatch = true;
				}
				if (terms.length > 1) {
					int termNo = 0;
					for (String term : terms) {
						int count = value2.length()
						- value2.replaceAll(term, "").length();
						if (count > 0) {
							score.terms[termNo++] += count;
							score.isMatch = true;
						}
					}
				}
			} catch (PatternSyntaxException pse) {
				// assume no match on pattern syntax error
			}
		}

		public int compare(ScoreCell o1, ScoreCell o2) {
			Score s1 = o1.score;
			Score s2 = o2.score;
			int difference = 0;
			if ((difference = s2.equalsMatches - s1.equalsMatches) != 0) {
				return difference;
			}
			if ((difference = s2.completeMatches - s1.completeMatches) != 0) {
				return difference;
			}
			int total1 = 0;
			int total2 = 0;
			int terms1 = 0;
			int terms2 = 0;
			// otherwise check if both terms are matched and tabulate total
			for (int score : s1.terms) {
				if (score > 0) {
					total1 += score;
					terms1++;
				}
			}
			for (int score : s2.terms) {
				if (score > 0) {
					total2 += score;
					terms2++;
				}
			}
			if ((difference = terms2 - terms1) != 0) {
				return difference;
			} else {
				return total2 - total1;
			}
		}
	}

	private static class ScoreCell {

		String[] row;

		Score score;
	}

	private static class Score {

		public int completeMatches = 0;

		public int equalsMatches = 0;

		public int[] terms;

		public boolean isMatch = false;
	}

	public int searchTabResults(int WindowNo, int AD_Tab_ID, int queryResultID,
			int searchResultID, String query, int rowCount) {
		UITab tab = getTab(AD_Tab_ID);
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (UIField field : tab.getFields()) {
			if (field.isSelectionColumn() || field.isIdentifier()
					&& FieldType.isText(field.getAD_Reference_ID())) {
				ids.add(field.getAD_Field_ID());
			}
		}
		return searchTabResults(WindowNo, tab, ids, queryResultID,
				searchResultID, query, rowCount);
	}
	
	public NodeVO getNode(String key){
		return m_nodes.get(key);
	}
	
	public void putNode(String key, NodeVO node){
		m_nodes.put(key, node);
	}	
} // GwtServer
