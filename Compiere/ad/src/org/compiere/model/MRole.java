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

import java.lang.ref.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

import org.compiere.framework.*;
import org.compiere.framework.AccessSqlParser.*;
import org.compiere.util.*;

/**
 * Role Model. Includes AD_User runtime info for Personal Access The class is
 * final, so that you cannot overwrite the security rules.
 * 
 * @author Jorg Janke
 * @version $Id: MRole.java 9089 2010-07-01 09:49:28Z srajamani $
 */
public final class MRole extends X_AD_Role {
	/** Logger for class MRole */
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRole.class);
	/**	 */
	private static final long serialVersionUID = 835133739541171221L;

	/**
	 * Get Default (Client) Role
	 * 
	 * @return role
	 */
	public static MRole getDefault() {
		if (s_defaultRole == null) {
			if (!Ini.isClient()) {
				s_log.log(Level.WARNING, "Server trying to access Swing Role",
						new IllegalArgumentException("Access to Client Role"));
				return getDefault(Env.getCtx(), false);
			}
			s_defaultRole = getDefault(Env.getCtx(), false);
		}
		return s_defaultRole;
	} // getDefault

	/**
	 * Get/Set Default Role.
	 * 
	 * @param ctx
	 *            context
	 * @param reload
	 *            if true forces load
	 * @return role
	 * @see org.compiere.util.Login#loadPreferences(KeyNamePair, KeyNamePair,
	 *      java.sql.Timestamp, String)
	 */
	public static MRole getDefault(Ctx ctx, boolean reload) {
		if (s_defaultRole != null && "Y".equals(ctx.getContext(SWINGUI)))
			return s_defaultRole;
		int gwtServerID = ctx.getContextAsInt(GWTSERVERID);
		if (gwtServerID > 0) {
			SoftReference<MRole> wr = s_gwtRoles.get(gwtServerID);
			if (wr != null) {
				MRole role = wr.get();
				if (role != null)
					return role;
				else
					s_log.warning("MRole for ServerID=" + gwtServerID
							+ " has already been garbage collected");
			} else
				s_log.warning("Not Found ServerID=" + gwtServerID);
		}

		int AD_Role_ID = ctx.getAD_Role_ID();
		int AD_User_ID = ctx.getAD_User_ID();
		if (!Ini.isClient()) // none for Server
			AD_User_ID = 0;
		//
		return get(ctx, AD_Role_ID, AD_User_ID, reload);
	} // getDefault

	/**
	 * Get Role for User
	 * 
	 * @param ctx
	 *            context
	 * @param AD_Role_ID
	 *            role
	 * @param AD_User_ID
	 *            user
	 * @param reload
	 *            if true forces load
	 * @return role
	 */
	public static MRole get(Ctx ctx, int AD_Role_ID, int AD_User_ID,
			boolean reload) {
		// Create Swing
		if ("Y".equals(ctx.getContext(SWINGUI))) {
			s_defaultRole = createRole(ctx, AD_Role_ID, AD_User_ID);
			s_log.info("Swing: " + s_defaultRole.toString());
			return s_defaultRole;
		}
		// Create GwtServer
		int gwtServerID = ctx.getContextAsInt(GWTSERVERID);
		if (gwtServerID > 0) {
			MRole role = createRole(ctx, AD_Role_ID, AD_User_ID);
			s_gwtRoles.put(gwtServerID, new SoftReference<MRole>(role));
			s_log.info("ServerID=" + gwtServerID + ": " + role.toString());
			return role;
		}

		// General
		s_log.finer("AD_Role_ID=" + AD_Role_ID + ", AD_User_ID=" + AD_User_ID
				+ ", reload=" + reload);
		String key = AD_Role_ID + "_" + AD_User_ID;
		MRole role = s_roles.get(ctx, key);
		if (role == null) {
			role = createRole(ctx, AD_Role_ID, AD_User_ID);
			s_roles.put(key, role);
			s_log.info(role.toString());
		}
		role.loadAccess(reload);
		return role;
	} // get

	/**
	 * Create Role
	 * 
	 * @param ctx
	 *            context
	 * @param AD_Role_ID
	 *            role
	 * @param AD_User_ID
	 *            user
	 * @return new role for user
	 */
	private static MRole createRole(Ctx ctx, int AD_Role_ID, int AD_User_ID) {
		MRole role = new MRole(ctx, AD_Role_ID, null);
		if (AD_Role_ID == 0) {
			Trx trx = null;
			role.load(trx); // special Handling
		}
		role.setAD_User_ID(AD_User_ID);
		return role;
	} // createRole

	/**
	 * Reset Gwt Cache
	 * 
	 * @param gwtServerID
	 *            gwt server id
	 * @return counter
	 */
	public static int resetGwt(int gwtServerID) {
		Object oo = s_gwtRoles.remove(gwtServerID);
		if (oo != null)
			return 0;
		return 1;
	} // resetGwt

	/**
	 * Reset Cache
	 * 
	 * @param AD_Role_ID
	 *            or 0 for all
	 * @return counter
	 */
	public static int reset(int AD_Role_ID) {
		int counter = 0;
		if (AD_Role_ID == 0) {
			if (s_defaultRole != null)
				counter++;
			s_defaultRole = null;
			counter += s_gwtRoles.size();
			s_gwtRoles.clear();
			counter += s_roles.reset();
			s_log.info("#" + counter);
			return counter;
		}
		//
		if (s_defaultRole != null
				&& s_defaultRole.getAD_Role_ID() == AD_Role_ID) {
			s_defaultRole = null;
			counter++;
		}
		//
		Iterator<String> it1 = s_roles.keySet().iterator();
		String cmp = String.valueOf(AD_Role_ID);
		while (it1.hasNext()) {
			String key = it1.next();
			if (key.equals(cmp) || key.startsWith(cmp + "_")) {
				s_roles.remove(key);
				counter++;
			}
		}
		//
		int cleanup = 0;
		Iterator<Map.Entry<Integer, SoftReference<MRole>>> it2 = s_gwtRoles
		.entrySet().iterator();
		while (it2.hasNext()) {
			Map.Entry<Integer, SoftReference<MRole>> entry = it2.next();
			Integer key = entry.getKey();
			SoftReference<MRole> wr = entry.getValue();
			if (wr == null) {
				s_gwtRoles.remove(key); // cleanup
				cleanup++;
			} else {
				MRole role = wr.get();
				if (role == null) {
					s_gwtRoles.remove(key); // cleanup
					cleanup++;
				} else if (role.getAD_Role_ID() == AD_Role_ID) {
					s_gwtRoles.remove(key);
					counter++;
				}
			}
		}
		s_log.info("#" + counter + " - Cleanup #" + cleanup);
		return counter;
	} // reset

	/**
	 * Get Role === Does NOT set user - so no access loaded
	 * 
	 * @param ctx
	 *            context
	 * @param AD_Role_ID
	 *            role
	 * @return role
	 */
	public static MRole get(Ctx ctx, int AD_Role_ID) {
		String key = String.valueOf(AD_Role_ID);
		MRole role = s_roles.get(ctx, AD_Role_ID);
		Trx trx = null;
		if (role == null) {
			role = new MRole(ctx, AD_Role_ID, trx);
			if (AD_Role_ID == 0) // System Role
				role.load(trx); // special Handling
			s_roles.put(key, role);
		}
		return role;
	} // get

	/**
	 * Get Roles Of Client
	 * 
	 * @param ctx
	 *            context
	 * @return roles of client
	 */
	public static MRole[] getOfClient(Ctx ctx) {
		String sql = "SELECT * FROM AD_Role WHERE AD_Client_ID=?";
		ArrayList<MRole> list = new ArrayList<MRole>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, ctx.getAD_Client_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MRole(ctx, rs, null));
		} catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MRole[] retValue = new MRole[list.size()];
		list.toArray(retValue);
		return retValue;
	} // getOfClient

	/**
	 * Get Roles With where clause
	 * 
	 * @param ctx
	 *            context
	 * @param whereClause
	 *            where clause
	 * @return roles of client
	 */
	public static MRole[] getOf(Ctx ctx, String whereClause) {
		String sql = "SELECT * FROM AD_Role";
		if (whereClause != null && whereClause.length() > 0)
			sql += " WHERE " + whereClause;
		ArrayList<MRole> list = new ArrayList<MRole>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MRole(ctx, rs, null));
		} catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MRole[] retValue = new MRole[list.size()];
		list.toArray(retValue);
		return retValue;
	} // getOf

	/** Swing UI Property */
	public static final String SWINGUI = "SwingUI";
	/** Swing User Role */
	private static MRole s_defaultRole = null;

	/** Web UI Property */
	public static final String GWTSERVERID = "GwtServerID";
	/** GWT User Role */
	private static ConcurrentHashMap<Integer, SoftReference<MRole>> s_gwtRoles = new ConcurrentHashMap<Integer, SoftReference<MRole>>();
	/** General User Role Cache */
	private static final CCache<String, MRole> s_roles = new CCache<String, MRole>(
			"AD_Role", 5);

	/** Log */
	private static final CLogger s_log = CLogger.getCLogger(MRole.class);

	/** Access SQL Read Write */
	public static final boolean SQL_RW = true;
	/** Access SQL Read Only */
	public static final boolean SQL_RO = false;
	/** Access SQL Fully Qualified */
	public static final boolean SQL_FULLYQUALIFIED = true;
	/** Access SQL Not Fully Qualified */
	public static final boolean SQL_NOTQUALIFIED = false;

	/** The AD_User_ID of the SuperUser */
	public static final int SUPERUSER_USER_ID = 100;
	/** The AD_User_ID of the System Administrator */
	public static final int SYSTEM_USER_ID = 0;

	/**************************************************************************
	 * Standard Constructor
	 * 
	 * @param ctx
	 *            context
	 * @param AD_Role_ID
	 *            id
	 * @param trx
	 *            transaction
	 */
	public MRole(Ctx ctx, int AD_Role_ID, Trx trx) {
		super(ctx, AD_Role_ID, trx);
		// ID=0 == System Administrator
		if (AD_Role_ID == 0) {
			// setName (null);
			setIsCanExport(true);
			setIsCanReport(true);
			setIsManual(false);
			setIsPersonalAccess(false);
			setIsPersonalLock(false);
			setIsShowAcct(false);
			setIsAccessAllOrgs(false);
			setIsAdministrator(false);
			setUserLevel(USERLEVEL_Organization);
			setPreferenceType(PREFERENCETYPE_Organization);
			setIsChangeLog(false);
			setOverwritePriceLimit(false);
			setOverrideReturnPolicy(false);
			setIsUseUserOrgAccess(false);
			setIsCanApproveOwnDoc(false);
			setMaxQueryRecords(0);
			setConfirmQueryRecords(0);
			setDisplayClientOrg(DISPLAYCLIENTORG_AlwaysTenantOrganization);
			setWinUserDefLevel(WINUSERDEFLEVEL_UserOnly);
		}
	} // MRole

	/**
	 * Load Constructor
	 * 
	 * @param ctx
	 *            context
	 * @param rs
	 *            result set
	 * @param trx
	 *            transaction
	 */
	public MRole(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	} // MRole

	/** Web Store Role */
	private Boolean m_isWebStoreRole = null;

	/**
	 * Get Confirm Query Records
	 * 
	 * @return entered records or 500 (default)
	 */
	@Override
	public int getConfirmQueryRecords() {
		int no = super.getConfirmQueryRecords();
		if (no == 0)
			return 500;
		return no;
	} // getConfirmQueryRecords

	/**
	 * Require Query
	 * 
	 * @param noRecords
	 *            records
	 * @return true if query required
	 */
	public boolean isQueryRequire(int noRecords) {
		if (noRecords < 2)
			return false;
		int max = getMaxQueryRecords();
		if (max > 0 && noRecords > max)
			return true;
		int qu = getConfirmQueryRecords();
		return noRecords > qu;
	} // isQueryRequire

	/**
	 * Over max Query
	 * 
	 * @param noRecords
	 *            records
	 * @return true if over max query
	 */
	public boolean isQueryMax(int noRecords) {
		int max = getMaxQueryRecords();
		return max > 0 && noRecords > max;
	} // isQueryMax

	/**
	 * Get Display ClientOrg flag
	 * 
	 * @return display flag
	 */
	@Override
	public String getDisplayClientOrg() {
		String s = super.getDisplayClientOrg();
		if (s == null)
			return DISPLAYCLIENTORG_AlwaysTenantOrganization;
		return s;
	} // getDisplayClientOrg

	/**
	 * Get Window UserDef (Customization) Level
	 * 
	 * @return customization Level
	 */
	@Override
	public String getWinUserDefLevel() {
		String s = super.getWinUserDefLevel();
		if (s == null)
			return WINUSERDEFLEVEL_UserOnly;
		return s;
	} // getWinUserDefLevel

	/**
	 * Before Save
	 * 
	 * @param newRecord
	 *            new
	 * @return true if it can be saved
	 */
	@Override
	protected boolean beforeSave(boolean newRecord) {
		// if (newRecord || is_ValueChanged("UserLevel"))
		// {
		if (getAD_Client_ID() == 0)
			setUserLevel(USERLEVEL_System);
		else if (getUserLevel().trim().equals(USERLEVEL_System.trim())) {
			log.saveWarning("AccessTableNoUpdate", Msg.getElement(getCtx(),
			"UserLevel"));
			return false;
		}
		// }
		return true;
	} // beforeSave

	/**
	 * After Save
	 * 
	 * @param newRecord
	 *            new
	 * @param success
	 *            success
	 * @return success
	 */
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		if (newRecord && success) {
			// Add Role to SuperUser
			MUserRoles su = new MUserRoles(getCtx(), SUPERUSER_USER_ID,
					getAD_Role_ID(), get_Trx());
			su.save();
			// Add Role to User
			if (getCreatedBy() != SUPERUSER_USER_ID && getCreatedBy()!=SYSTEM_USER_ID) {
				MUserRoles ur = new MUserRoles(getCtx(), getCreatedBy(),
						getAD_Role_ID(), get_Trx());
				ur.save();
			}
			updateAccessRecords();
		}
		//
		else if (is_ValueChanged("UserLevel"))
			updateAccessRecords();

		// Default Role changed
		if (s_defaultRole != null && s_defaultRole.get_ID() == get_ID())
			s_defaultRole = this;
		return success;
	} // afterSave

	/**
	 * Create Access Records
	 * 
	 * @return info
	 */
	public String updateAccessRecords() {
		if (isManual())
			return "-";

		String roleClientOrgUser = getAD_Role_ID() + "," + getAD_Client_ID()
		+ "," + getAD_Org_ID() + ",'Y', SysDate," + getUpdatedBy()
		+ ", SysDate," + getUpdatedBy() + ",'Y' "; // IsReadWrite

		String sqlWindow = "INSERT INTO AD_Window_Access "
			+ "(AD_Window_ID, AD_Role_ID,"
			+ " AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,IsReadWrite) "
			+ "SELECT DISTINCT w.AD_Window_ID, "
			+ roleClientOrgUser
			+ "FROM AD_Window w"
			+ " INNER JOIN AD_Tab t ON (w.AD_Window_ID=t.AD_Window_ID)"
			+ " INNER JOIN AD_Table tt ON (t.AD_Table_ID=tt.AD_Table_ID) "
			+ "WHERE t.SeqNo=(SELECT MIN(SeqNo) FROM AD_Tab xt " // only
			// check
			// first
			// tab
			+ "WHERE xt.AD_Window_ID=w.AD_Window_ID)"
			+ "AND tt.AccessLevel IN ";

		String sqlProcess = "INSERT INTO AD_Process_Access "
			+ "(AD_Process_ID, AD_Role_ID,"
			+ " AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,IsReadWrite) "
			+ "SELECT DISTINCT p.AD_Process_ID, " + roleClientOrgUser
			+ "FROM AD_Process p " + "WHERE AccessLevel IN ";

		String sqlForm = "INSERT INTO AD_Form_Access "
			+ "(AD_Form_ID, AD_Role_ID,"
			+ " AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,IsReadWrite) "
			+ "SELECT f.AD_Form_ID, " + roleClientOrgUser
			+ "FROM AD_Form f " + "WHERE AccessLevel IN ";
		
		String sqlInfoWindow = "INSERT INTO AD_InfoWindow_Access "
			+ "(AD_InfoWindow_ID, AD_Role_ID,"
			+ " AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,IsReadWrite) "
			+ "SELECT i.AD_InfoWindow_ID, " + roleClientOrgUser
			+ "FROM AD_InfoWindow i " 			
			+ " INNER JOIN AD_Table tt ON (tt.AD_Table_ID=i.AD_Table_ID) "			
			+ " WHERE AD_InfoWindow_ID IN ('100','101','102','103','104','105','106','107','108','118')" 
			+ "AND tt.AccessLevel IN ";

		String sqlWorkflow = "INSERT INTO AD_Workflow_Access "
			+ "(AD_Workflow_ID, AD_Role_ID,"
			+ " AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,IsReadWrite) "
			+ "SELECT w.AD_Workflow_ID, " + roleClientOrgUser
			+ "FROM AD_Workflow w " + "WHERE AccessLevel IN ";

		/**
		 * Fill AD_xx_Access
		 * ----------------------------------------------------
		 * ----------------------- SCO# Levels S__ 100 4 System info SCO 111 7
		 * System shared info SC_ 110 6 System/Client info _CO 011 3 Client
		 * shared info _C_ 011 2 Client __O 001 1 Organization info Roles: S
		 * 4,7,6 _CO 7,6,3,2,1 __O 3,1,7
		 */
		String roleAccessLevel = null;
		String roleAccessLevelWin = null;
		String UserLevel = getUserLevel().trim();
		if (USERLEVEL_System.trim().equals(UserLevel))
			roleAccessLevel = "('4','7','6')";
		else if (USERLEVEL_Tenant.trim().equals(UserLevel))
			roleAccessLevel = "('7','6','3','2')";
		else if (USERLEVEL_TenantPlusOrganization.trim().equals(UserLevel))
			roleAccessLevel = "('7','6','3','2','1')";
		else // if (USERLEVEL_Organization.trim().equals(UserLevel))
		{
			roleAccessLevel = "('3','1','7')";
			// roleAccessLevelWin = roleAccessLevel
			// + " AND w.Name NOT LIKE '%(all)%'"; // Request (all)
		}
		if (roleAccessLevelWin == null)
			roleAccessLevelWin = roleAccessLevel;
		//
		String whereDel = " WHERE AD_Role_ID=" + getAD_Role_ID();
		//
		int winDel = DB.executeUpdate(
				get_Trx(), "DELETE FROM AD_Window_Access" + whereDel);
		int win = DB.executeUpdate(get_Trx(), sqlWindow + roleAccessLevelWin);
		int procDel = DB.executeUpdate(get_Trx(), "DELETE FROM AD_Process_Access"
						+ whereDel);
		int proc = DB.executeUpdate(get_Trx(), sqlProcess + roleAccessLevel);
		int formDel = DB.executeUpdate(get_Trx(),
				"DELETE FROM AD_Form_Access" + whereDel);
		int form = DB.executeUpdate(get_Trx(), sqlForm + roleAccessLevel);
		int infoWindowDel = DB.executeUpdate(get_Trx(),
				"DELETE FROM AD_InfoWindow_Access" + whereDel);
		int infoWindow = DB.executeUpdate(get_Trx(), sqlInfoWindow + roleAccessLevel);
		int wfDel = DB.executeUpdate(get_Trx(), "DELETE FROM AD_Workflow_Access"
						+ whereDel);
		int wf = DB.executeUpdate(get_Trx(), sqlWorkflow + roleAccessLevel);

		log.fine("AD_Window_ID=" + winDel + "+" + win + ", AD_Process_ID="
				+ procDel + "+" + proc + ", AD_Form_ID=" + formDel + "+" + form
				+ ", AD_InfoWindow_ID=" + infoWindowDel + "+" + infoWindow 
				+ ", AD_Workflow_ID=" + wfDel + "+" + wf);

		loadAccess(true);
		return "@AD_Window_ID@ #" + win + " -  @AD_Process_ID@ #" + proc
		+ " -  @AD_Form_ID@ #" + form + " -  @AD_Workflow_ID@ #" + wf;
	} // createAccessRecords

	/**
	 * String Representation
	 * 
	 * @return info
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("MRole[");
		sb.append(getAD_Role_ID()).append(",").append(getName()).append(
		",UserLevel=").append(getUserLevel()).append(",").append(
				getClientWhere(false)).append(",").append(getOrgWhere(null, false))
				.append("]");
		return sb.toString();
	} // toString

	/**
	 * Extended String Representation
	 * 
	 * @param ctx
	 *            context
	 * @return extended info
	 */
	public String toStringX(Ctx ctx) {
		StringBuffer sb = new StringBuffer();
		sb.append(Msg.translate(ctx, "AD_Role_ID")).append("=").append(
				getName()).append(" - ").append(
						Msg.translate(ctx, "IsCanExport")).append("=").append(
								isCanExport()).append(" - ").append(
										Msg.translate(ctx, "IsCanReport")).append("=").append(
												isCanReport()).append(Env.NL).append(Env.NL);
		//
		for (OrgAccess element : m_orgAccess)
			sb.append(element.toString()).append(Env.NL);
		sb.append(Env.NL);
		//
		loadTableAccess(false);
		for (MTableAccess element : m_tableAccess)
			sb.append(element.toStringX(ctx)).append(Env.NL);
		if (m_tableAccess.length > 0)
			sb.append(Env.NL);
		//
		loadColumnAccess(false);
		for (MColumnAccess element : m_columnAccess)
			sb.append(element.toStringX(ctx)).append(Env.NL);
		if (m_columnAccess.length > 0)
			sb.append(Env.NL);
		//
		loadRecordAccess(false);
		for (MRecordAccess element : m_recordAccess)
			sb.append(element.toStringX(ctx)).append(Env.NL);
		return sb.toString();
	} // toStringX

	/**
	 * Is this Role used as a public Role
	 * 
	 * @return true if it is a web store role
	 */
	public boolean isWebStoreRole() {
		if (m_isWebStoreRole != null)
			return m_isWebStoreRole.booleanValue();
		String sql = "SELECT W_Store_ID FROM W_Store WHERE AD_Role_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, getAD_Role_ID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				m_isWebStoreRole = Boolean.TRUE;
			}
		} catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (m_isWebStoreRole == null)
			m_isWebStoreRole = Boolean.FALSE;
		return m_isWebStoreRole.booleanValue();
	} // isWebStoreRole

	/*************************************************************************
	 * Access Management
	 ************************************************************************/

	/** User */
	private int m_AD_User_ID = -1;

	/** Positive List of Organizational Access */
	private OrgAccess[] m_orgAccess = null;
	/** List of Table Access */
	private MTableAccess[] m_tableAccess = null;
	/** List of Column Access */
	private MColumnAccess[] m_columnAccess = null;
	/** List of Record Access */
	private MRecordAccess[] m_recordAccess = null;
	/** List of Dependent Record Access */
	private MRecordAccess[] m_recordDependentAccess = null;


	/** Table Data Access Level */
	private static CCachePerm<Integer, String> m_tableAccessLevel = new CCachePerm<Integer, String>(
			"TableAccess", 20, 0, "AD_Table") {

		@Override
		public int reset() {
			int no = super.reset();
			m_tableName.clear();
			loadTableInfo(true);
			return no;
		}

	};
	/** Table Name */
	private static ConcurrentHashMap<String, Integer> m_tableName = new ConcurrentHashMap<String, Integer>(
			20);

	/** Window Access */
	private HashMap<Integer, Boolean> m_windowAccess = null;
	/** Process Access */
	private HashMap<Integer, Boolean> m_processAccess = null;
	/** Task Access */
	private HashMap<Integer, Boolean> m_taskAccess = null;
	/** Workflow Access */
	private HashMap<Integer, Boolean> m_workflowAccess = null;
	/** Form Access */
	private HashMap<Integer, Boolean> m_formAccess = null;
	/** InfoWindow Access */
	private HashMap<Integer, Boolean> m_infoWindowAccess = null;

	/**
	 * Set Logged in user
	 * 
	 * @param AD_User_ID
	 *            user requesting info
	 */
	public void setAD_User_ID(int AD_User_ID) {
		m_AD_User_ID = AD_User_ID;
	} // setAD_User_ID

	/**
	 * Get Logged in user
	 * 
	 * @return AD_User_ID user requesting info
	 */
	public int getAD_User_ID() {
		return m_AD_User_ID;
	} // getAD_User_ID

	/**************************************************************************
	 * Load Access Info
	 * 
	 * @param reload
	 *            re-load from disk
	 */
	public void loadAccess(boolean reload) {
		// if (reload)
		// log.warning("Reloading Start: " + getSizeInfo());
		loadOrgAccess(reload);
		loadTableAccess(reload);
		loadTableInfo(reload);
		loadColumnAccess(reload);
		loadRecordAccess(reload);
		if (reload) {
			m_windowAccess = null;
			m_processAccess = null;
			m_taskAccess = null;
			m_workflowAccess = null;
			m_formAccess = null;
			m_infoWindowAccess = null;
		}
		if (reload)
			log.config("Reloading End:  " + getSizeInfo());
	} // loadAccess

	/**
	 * Get Size of cached objects
	 * 
	 * @return info
	 */
	private String getSizeInfo() {
		int total = 0;
		StringBuffer sb = new StringBuffer();
		if (m_orgAccess == null)
			sb.append("Org=0");
		else {
			sb.append("Org=" + m_orgAccess.length);
			total += m_orgAccess.length;
		}
		if (m_tableAccess == null)
			sb.append(",Table=0");
		else {
			sb.append(",Table=" + m_tableAccess.length);
			total += m_tableAccess.length;
		}
		if (m_tableAccessLevel == null)
			sb.append("/0");
		else {
			sb.append("/" + m_tableAccessLevel.size());
			total += m_tableAccessLevel.size();
		}
		if (m_tableName == null)
			sb.append("/0");
		else {
			sb.append("/" + m_tableName.size());
			total += m_tableName.size();
		}
		//
		if (m_columnAccess == null)
			sb.append(",Col=0");
		else {
			sb.append(",Col=" + m_columnAccess.length);
			total += m_columnAccess.length;
		}
		if (m_recordAccess == null)
			sb.append(",Record=0");
		else {
			sb.append(",Record=" + m_recordAccess.length);
			total += m_recordAccess.length;
		}
		if (m_windowAccess == null)
			sb.append(",Win=0");
		else {
			sb.append(",Win=" + m_windowAccess.size());
			total += m_windowAccess.size();
		}
		if (m_processAccess == null)
			sb.append(",Process=0");
		else {
			sb.append(",Process=" + m_processAccess.size());
			total += m_processAccess.size();
		}
		if (m_taskAccess == null)
			sb.append(",Task=0");
		else {
			sb.append(",Task=" + m_taskAccess.size());
			total += m_taskAccess.size();
		}
		if (m_workflowAccess == null)
			sb.append(",WF=0");
		else {
			sb.append(",WF=" + m_workflowAccess.size());
			total += m_workflowAccess.size();
		}
		if (m_formAccess == null)
			sb.append(",Form=0");
		else {
			sb.append(",Form=" + m_formAccess.size());
			total += m_formAccess.size();
		}
		if (m_infoWindowAccess == null)
			sb.append(",InfoWindow=0");
		else {
			sb.append(",InfoWindow=" + m_infoWindowAccess.size());
			total += m_infoWindowAccess.size();
		}
		
		return sb.toString();
	} // getSizeInfo

	/**
	 * Load Org Access
	 * 
	 * @param reload
	 *            reload
	 */
	private void loadOrgAccess(boolean reload) {
		if (!(reload || m_orgAccess == null))
			return;
		//
		ArrayList<OrgAccess> list = new ArrayList<OrgAccess>();

		if (isUseUserOrgAccess())
			loadOrgAccessUser(list);
		else
			loadOrgAccessRole(list);

		m_orgAccess = new OrgAccess[list.size()];
		list.toArray(m_orgAccess);
		log.fine("#" + m_orgAccess.length + (reload ? " - reload" : ""));
		if (Ini.isClient()) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < m_orgAccess.length; i++) {
				if (i > 0)
					sb.append(",");
				sb.append(m_orgAccess[i].AD_Org_ID);
			}
			Env.getCtx().setContext("#User_Org", sb.toString());
		}
	} // loadOrgAccess

	/**
	 * Load Org Access User
	 * 
	 * @param list
	 *            list
	 */
	private void loadOrgAccessUser(ArrayList<OrgAccess> list) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM AD_User_OrgAccess "
			+ "WHERE AD_User_ID=? AND IsActive='Y'";
		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getAD_User_ID());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MUserOrgAccess oa = new MUserOrgAccess(getCtx(), rs, get_Trx());
				loadOrgAccessAdd(list, new OrgAccess(oa.getAD_Client_ID(), oa
						.getAD_Org_ID(), oa.isReadOnly()));
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

	} // loadOrgAccessRole

	/**
	 * Load Org Access Role
	 * 
	 * @param list
	 *            list
	 */
	private void loadOrgAccessRole(ArrayList<OrgAccess> list) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM AD_Role_OrgAccess "
			+ "WHERE AD_Role_ID=? AND IsActive='Y'";
		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getAD_Role_ID());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MRoleOrgAccess oa = new MRoleOrgAccess(getCtx(), rs, get_Trx());
				loadOrgAccessAdd(list, new OrgAccess(oa.getAD_Client_ID(), oa
						.getAD_Org_ID(), oa.isReadOnly()));
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

	} // loadOrgAccessRole

	/**
	 * Load Org Access Add Tree to List
	 * 
	 * @param list
	 *            list
	 * @param oa
	 *            org access
	 * @see org.compiere.util.Login
	 */
	private void loadOrgAccessAdd(ArrayList<OrgAccess> list, OrgAccess oa) {
		if (list.contains(oa))
			return;
		list.add(oa);
		// Do we look for trees?
		if (getAD_Tree_Org_ID() == 0)
			return;
		MOrg org = MOrg.get(getCtx(), oa.AD_Org_ID);
		if (!org.isSummary())
			return;
		// Summary Org - Get Dependents
		MTree tree = MTree.get(getCtx(), getAD_Tree_Org_ID(), get_Trx());
		String sql = "SELECT AD_Client_ID, AD_Org_ID FROM AD_Org "
			+ "WHERE IsActive='Y' AND AD_Org_ID IN (SELECT Node_ID FROM "
			+ tree.getNodeTableName()
			+ " WHERE AD_Tree_ID=? AND Parent_ID=? AND IsActive='Y')";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, tree.getAD_Tree_ID());
			pstmt.setInt(2, org.getAD_Org_ID());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int AD_Client_ID = rs.getInt(1);
				int AD_Org_ID = rs.getInt(2);
				loadOrgAccessAdd(list, new OrgAccess(AD_Client_ID, AD_Org_ID,
						oa.readOnly));
			}
		} 
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

	} // loadOrgAccessAdd

	/**
	 * Load Table Access
	 * 
	 * @param reload
	 *            reload
	 */
	private void loadTableAccess(boolean reload) {
		if (m_tableAccess != null && !reload)
			return;
		ArrayList<MTableAccess> list = new ArrayList<MTableAccess>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM AD_Table_Access "
			+ "WHERE AD_Role_ID=? AND IsActive='Y'";
		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getAD_Role_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MTableAccess(getCtx(), rs, get_Trx()));
		} 
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_tableAccess = new MTableAccess[list.size()];
		list.toArray(m_tableAccess);
		log.fine("#" + m_tableAccess.length);
	} // loadTableAccess

	/**
	 * Load Table Access and Name
	 * 
	 * @param reload
	 *            reload
	 */
	private static void loadTableInfo(boolean reload) {
		if ((m_tableAccessLevel.size() > 0 || m_tableName.size() > 0)
				&& !reload)
			return;
		// m_tableAccessLevel = new HashMap<Integer,String>(300);
		// m_tableName = new HashMap<String,Integer>(300);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT AD_Table_ID, AccessLevel, TableName "
			+ "FROM AD_Table WHERE IsActive='Y'";
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Integer ii = Integer.valueOf(rs.getInt(1));
				m_tableAccessLevel.put(ii, rs.getString(2));
				m_tableName.put(rs.getString(3), ii);
			}
		} 
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		s_log.fine("#" + m_tableAccessLevel.size());
	} // loadTableAccessLevel

	/**
	 * Load Column Access
	 * 
	 * @param reload
	 *            reload
	 */
	private void loadColumnAccess(boolean reload) {
		if (m_columnAccess != null && !reload)
			return;
		ArrayList<MColumnAccess> list = new ArrayList<MColumnAccess>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM AD_Column_Access "
			+ "WHERE AD_Role_ID=? AND IsActive='Y'";
		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getAD_Role_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MColumnAccess(getCtx(), rs, get_Trx()));
		} 
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_columnAccess = new MColumnAccess[list.size()];
		list.toArray(m_columnAccess);
		log.fine("#" + m_columnAccess.length);
	} // loadColumnAccess

	/**
	 * Load Record Access
	 * 
	 * @param reload
	 *            reload
	 */
	private void loadRecordAccess(boolean reload) {
		if (!(reload || m_recordAccess == null || m_recordDependentAccess == null))
			return;
		ArrayList<MRecordAccess> list = new ArrayList<MRecordAccess>();
		ArrayList<MRecordAccess> dependent = new ArrayList<MRecordAccess>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM AD_Record_Access "
			+ "WHERE AD_Role_ID=? AND IsActive='Y' ORDER BY AD_Table_ID";
		try {
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getAD_Role_ID());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MRecordAccess ra = new MRecordAccess(getCtx(), rs, get_Trx());
				list.add(ra);
				if (ra.isDependentEntities())
					dependent.add(ra);
			}
		} 
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_recordAccess = new MRecordAccess[list.size()];
		list.toArray(m_recordAccess);
		m_recordDependentAccess = new MRecordAccess[dependent.size()];
		dependent.toArray(m_recordDependentAccess);
		log.fine("#" + m_recordAccess.length + " - Dependent #"
				+ m_recordDependentAccess.length);
	} // loadRecordAccess


	/**
	 * Get Info Window Access
	 * 
	 * @param AD_InfoWindow_ID
	 *            Info Window
	 * @return null in no access, TRUE if r/w and FALSE if r/o
	 */
	public boolean getInfoWindowAccess(int AD_InfoWindow_ID) {

		if (m_infoWindowAccess == null) {
			m_infoWindowAccess = new HashMap<Integer, Boolean>(20);
			String sql = "SELECT AD_InfoWindow_ID, IsReadWrite FROM AD_InfoWindow_Access "
				+ "WHERE AD_Role_ID=? AND IsActive='Y'";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getAD_Role_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
					m_infoWindowAccess.put(Integer.valueOf(rs.getInt(1)), Boolean
							.valueOf("Y".equals(rs.getString(2))));
			} 
			catch (Exception e) {
				log.log(Level.SEVERE, sql, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

		} // reload
		if (m_infoWindowAccess.get(Integer.valueOf(AD_InfoWindow_ID)) == null)
			return false;
		else
			return m_infoWindowAccess.get(Integer.valueOf(AD_InfoWindow_ID));
	} // getInfoWindowAccess

	/**************************************************************************
	 * Get Client Where Clause Value
	 * 
	 * @param rw
	 *            read write
	 * @return "AD_Client_ID=0" or "AD_Client_ID IN(0,1)"
	 */
	public String getClientWhere(boolean rw) {
		// All Orgs - use Client of Role
		if (isAccessAllOrgs()) {
			if (rw || getAD_Client_ID() == 0)
				return "AD_Client_ID=" + getAD_Client_ID();
			return "AD_Client_ID IN (0," + getAD_Client_ID() + ")";
		}

		// Get Client from Org List
		loadOrgAccess(false);
		// Unique Strings
		HashSet<String> set = new HashSet<String>();
		if (!rw)
			set.add("0");
		// Positive List
		for (OrgAccess element : m_orgAccess)
			set.add(String.valueOf(element.AD_Client_ID));
		//
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = set.iterator();
		boolean oneOnly = true;
		while (it.hasNext()) {
			if (sb.length() > 0) {
				sb.append(",");
				oneOnly = false;
			}
			sb.append(it.next());
		}
		if (oneOnly) {
			if (sb.length() > 0)
				return "AD_Client_ID=" + sb.toString();
			else {
				log.log(Level.SEVERE, "No Access Org records");
				return "AD_Client_ID=-1"; // No Access Record
			}
		}
		return "AD_Client_ID IN(" + sb.toString() + ")";
	} // getClientWhereValue

	/**
	 * Access to Client
	 * 
	 * @param AD_Client_ID
	 *            client
	 * @param rw
	 *            read write access
	 * @return true if access
	 */
	public boolean isClientAccess(int AD_Client_ID, boolean rw) {
		if (AD_Client_ID == 0 && !rw) // can always read System
			return true;
		loadOrgAccess(false);
		// Positive List
		for (int i = 0; i < m_orgAccess.length; i++) {
			if (m_orgAccess[i].AD_Client_ID == AD_Client_ID) {
				if (!rw)
					return true;
				if (!m_orgAccess[i].readOnly) // rw
					return true;
			}
		}
		return false;
	} // isClientAccess

	/**
	 * Get Org Where Clause Value
	 * 
	 * @param rw
	 *            read write
	 * @return "AD_Org_ID=0" or "AD_Org_ID IN(0,1)" or null (if access all org)
	 */
	public String getOrgWhere(String tableName, boolean rw) {
		if (isAccessAllOrgs())
			return null;
		loadOrgAccess(false);
		// Unique Strings
		HashSet<String> set = new HashSet<String>();
		if (!rw)
			set.add("0");
		// Positive List
		for (int i = 0; i < m_orgAccess.length; i++) {
			if (!rw)
				set.add(String.valueOf(m_orgAccess[i].AD_Org_ID));
			else if (!m_orgAccess[i].readOnly) // rw
				set.add(String.valueOf(m_orgAccess[i].AD_Org_ID));
		}
		//
		if (set.size() == 1) {
			return "COALESCE(" + (tableName==null?"":tableName+".")+"AD_Org_ID,0)=" + set.iterator().next();
		} else if (set.size() == 0) {
			log.log(Level.SEVERE, "No Access Org records");
			return (tableName==null?"":tableName+".")+"AD_Org_ID=-1"; // No Access Record
		}
		StringBuffer sql = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = set.iterator();
		int count = 0;
		while (it.hasNext()) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(it.next());
			count++;
			//if there are 999 orgs already, or it reaches the end , reset
			//we do this 'cuz IN() cannot contain more than 1000 values
			if (count % 999 == 0 || count == set.size()) {
				if(sql.length()>0)
					sql.append(" OR ");

				sql.append("COALESCE(" + (tableName==null?"":tableName+".")+"AD_Org_ID,0) IN(").append(sb.toString()).append(")");
				sb = new StringBuffer();
			}
		}
		return "("+sql.toString()+")";
	} // getOrgWhereValue

	/**
	 * Get Doc Where Clause Value
	 * 
	 * @param tableName
	 *            TableName
	 * @return where clause or null (if access all doc)
	 */
	public String getDocWhere(String TableName) {
		if (!isUseBPRestrictions())
			return "";

		boolean hasBPColumn = false;
		String sql = "SELECT count(*) FROM AD_Table t "
			+ "INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID) "
			+ "WHERE t.TableName='" + TableName
			+ "' AND c.ColumnName='C_BPartner_ID' ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			if (rs.next())
				hasBPColumn = rs.getInt(1) != 0;
		} 
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (!hasBPColumn)
			return "";

		int AD_User_ID = getCtx().getAD_User_ID();

		String docAccess = "(EXISTS (SELECT 1 FROM C_BPartner bp INNER JOIN AD_User u "
			+ "ON (u.C_BPartner_ID=bp.C_BPartner_ID) "
			+ " WHERE u.AD_User_ID="
			+ AD_User_ID
			+ " AND bp.C_BPartner_ID="
			+ TableName
			+ ".C_BPartner_ID)"
			+ " OR EXISTS (SELECT 1 FROM C_BP_Relation bpr INNER JOIN AD_User u "
			+ "ON (u.C_BPartner_ID=bpr.C_BPartnerRelation_ID) "
			+ " WHERE u.AD_User_ID="
			+ AD_User_ID
			+ " AND bpr.C_BPartner_ID=" + TableName + ".C_BPartner_ID)";

		boolean hasUserColumn = false;
		String sql1 = "SELECT count(*) FROM AD_Table t "
			+ "INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID) "
			+ "WHERE t.tableName='" + TableName
			+ "' AND c.ColumnName='AD_User_ID' ";
		try {
			pstmt = DB.prepareStatement(sql1, (Trx) null);
			rs = pstmt.executeQuery();
			if (rs.next())
				hasUserColumn = rs.getInt(1) != 0;
		} 
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}


		if (hasUserColumn)
			docAccess += " OR " + TableName + ".AD_User_ID =" + AD_User_ID;
		docAccess += ")";

		return docAccess;
	}// getDocWhere

	/**
	 * Access to Org
	 * 
	 * @param AD_Org_ID
	 *            org
	 * @param rw
	 *            read write access
	 * @return true if access
	 */
	public boolean isOrgAccess(int AD_Org_ID, boolean rw) {
		if (isAccessAllOrgs())
			return true;
		if (AD_Org_ID == 0 && !rw) // can always read common org
			return true;
		loadOrgAccess(false);

		// Positive List
		for (int i = 0; i < m_orgAccess.length; i++) {
			if (m_orgAccess[i].AD_Org_ID == AD_Org_ID) {
				if (!rw)
					return true;
				if (!m_orgAccess[i].readOnly) // rw
					return true;
				return false;
			}
		}
		return false;
	} // isOrgAccess

	/**
	 * Can Report on table
	 * 
	 * @param AD_Table_ID
	 *            table
	 * @return true if access
	 */
	public boolean isCanReport(int AD_Table_ID) {
		if (!isCanReport()) // Role Level block
		{
			log.warning("Role denied");
			return false;
		}
		if (!isTableAccess(AD_Table_ID, true)) // No R/O Access to Table
			return false;
		//
		boolean canReport = true;
		for (int i = 0; i < m_tableAccess.length; i++) {
			if (!X_AD_Table_Access.ACCESSTYPERULE_Reporting
					.equals(m_tableAccess[i].getAccessTypeRule()))
				continue;
			if (m_tableAccess[i].isExclude()) // Exclude
			{
				if (m_tableAccess[i].getAD_Table_ID() == AD_Table_ID) {
					canReport = m_tableAccess[i].isCanReport();
					log.fine("Exclude " + AD_Table_ID + " - " + canReport);
					return canReport;
				}
			} else // Include
			{
				canReport = false;
				if (m_tableAccess[i].getAD_Table_ID() == AD_Table_ID) {
					canReport = m_tableAccess[i].isCanReport();
					log.fine("Include " + AD_Table_ID + " - " + canReport);
					return canReport;
				}
			}
		} // for all Table Access
		log.fine(AD_Table_ID + " - " + canReport);
		return canReport;
	} // isCanReport

	/**
	 * Can Export Table
	 * 
	 * @param AD_Table_ID
	 * @return true if access
	 */
	public boolean isCanExport(int AD_Table_ID) {
		if (!isCanExport()) // Role Level block
		{
			log.warning("Role denied");
			return false;
		}
		if (!isTableAccess(AD_Table_ID, true)) // No R/O Access to Table
			return false;
		if (!isCanReport(AD_Table_ID)) // We cannot Export if we cannot report
			return false;
		//
		boolean canExport = true;
		for (int i = 0; i < m_tableAccess.length; i++) {
			if (!X_AD_Table_Access.ACCESSTYPERULE_Exporting
					.equals(m_tableAccess[i].getAccessTypeRule()))
				continue;
			if (m_tableAccess[i].isExclude()) // Exclude
			{
				// bug 10018373: added if condition (similar to the if condition
				// in isCanReport)
				if (m_tableAccess[i].getAD_Table_ID() == AD_Table_ID) {
					canExport = m_tableAccess[i].isCanExport();
					log.fine("Exclude " + AD_Table_ID + " - " + canExport);
					return canExport;
				} // end bug 10018373
			} else // Include
			{
				canExport = false;
				// bug 10018373: added if condition (similar to the if condition
				// in isCanReport)
				if (m_tableAccess[i].getAD_Table_ID() == AD_Table_ID) {
					canExport = m_tableAccess[i].isCanExport();
					log.fine("Include " + AD_Table_ID + " - " + canExport);
					return canExport;
				}
			}
		} // for all Table Access
		log.fine(AD_Table_ID + " - " + canExport);
		return canExport;
	} // isCanExport

	/**
	 * Access to Table
	 * 
	 * @param AD_Table_ID
	 *            table
	 * @param ro
	 *            check read only access otherwise read write access level
	 * @return has RO/RW access to table
	 */
	public boolean isTableAccess(int AD_Table_ID, boolean ro) {
		if (!isTableAccessLevel(AD_Table_ID, ro)) // Role Based Access
			return false;
		loadTableAccess(false);
		//
		boolean hasAccess = true; // assuming exclusive rule
		for (int i = 0; i < m_tableAccess.length; i++) {
			if (!X_AD_Table_Access.ACCESSTYPERULE_Accessing
					.equals(m_tableAccess[i].getAccessTypeRule()))
				continue;
			if (m_tableAccess[i].isExclude()) // Exclude
				// If you Exclude Access to a table and select Read Only,
				// you can only read data (otherwise no access).
			{
				if (m_tableAccess[i].getAD_Table_ID() == AD_Table_ID) {
					if (ro)
						hasAccess = m_tableAccess[i].isReadOnly();
					else
						hasAccess = false;
					log.fine("Exclude AD_Table_ID=" + AD_Table_ID + " (ro="
							+ ro + ",TableAccessRO="
							+ m_tableAccess[i].isReadOnly() + ") = "
							+ hasAccess);
					return hasAccess;
				}
			} else // Include
				// If you Include Access to a table and select Read Only,
				// you can only read data (otherwise full access).
			{
				hasAccess = false;
				if (m_tableAccess[i].getAD_Table_ID() == AD_Table_ID) {
					if (!ro) // rw only if not r/o
						hasAccess = !m_tableAccess[i].isReadOnly();
					else
						hasAccess = true;
					log.fine("Include AD_Table_ID=" + AD_Table_ID + " (ro="
							+ ro + ",TableAccessRO="
							+ m_tableAccess[i].isReadOnly() + ") = "
							+ hasAccess);
					return hasAccess;
				}
			}
		} // for all Table Access
		if (!hasAccess)
			log.fine("AD_Table_ID=" + AD_Table_ID + "(ro=" + ro + ") = "
					+ hasAccess);
		return hasAccess;
	} // isTableAccess

	/**
	 * Access to Table based on Role User Level Table Access Level
	 * 
	 * @param AD_Table_ID
	 *            table
	 * @param ro
	 *            check read only access otherwise read write access level
	 * @return has RO/RW access to table
	 */
	public boolean isTableAccessLevel(int AD_Table_ID, boolean ro) {
		if (ro) // role can always read
			return true;
		//
		loadTableInfo(false);
		// AccessLevel
		// 1 = Org - 2 = Client - 4 = System
		// 3 = Org+Client - 6 = Client+System - 7 = All
		String tableAccessLevel = m_tableAccessLevel.get(null, Integer
				.valueOf(AD_Table_ID));
		if (tableAccessLevel == null) {
			log.fine("NO - No AccessLevel - AD_Table_ID=" + AD_Table_ID);
			return false;
		}
		// Access to all User Levels
		if (tableAccessLevel.equals(X_AD_Table.ACCESSLEVEL_All))
			return true;
		// User Level = SCO
		String userLevel = getUserLevel().trim();
		//
		if (userLevel.indexOf('S') != -1
				&& (tableAccessLevel.equals(X_AD_Table.ACCESSLEVEL_SystemOnly) || tableAccessLevel
						.equals(X_AD_Table.ACCESSLEVEL_SystemPlusTenant)))
			return true;
		if (userLevel.indexOf('C') != -1
				&& (tableAccessLevel.equals(X_AD_Table.ACCESSLEVEL_TenantOnly)
						|| tableAccessLevel
						.equals(X_AD_Table.ACCESSLEVEL_TenantPlusOrganization) || tableAccessLevel
						.equals(X_AD_Table.ACCESSLEVEL_SystemPlusTenant)))
			return true;
		if (userLevel.indexOf('O') != -1
				&& (tableAccessLevel
						.equals(X_AD_Table.ACCESSLEVEL_Organization) || tableAccessLevel
						.equals(X_AD_Table.ACCESSLEVEL_TenantPlusOrganization)))
			return true;
		log.fine("NO - AD_Table_ID=" + AD_Table_ID + ", UserLevel=" + userLevel
				+ ", AccessLevel=" + tableAccessLevel);
		return false;
	} // isTableAccessLevel

	/**
	 * Access to Column
	 * 
	 * @param AD_Table_ID
	 *            table
	 * @param AD_Column_ID
	 *            column
	 * @param ro
	 *            read only
	 * @return true if access. if ro is passed as false, return IsEditable, else
	 *         return IsDisplayed
	 */
	public boolean isColumnAccess(int AD_Table_ID, int AD_Column_ID, boolean ro) {
		if (!isTableAccess(AD_Table_ID, ro)) // No Access to Table
			return false;
		loadColumnAccess(false);

		boolean retValue = true; // assuming exclusive
		for (int i = 0; i < m_columnAccess.length; i++) {
			if (m_columnAccess[i].isExclude()) // Exclude
				// If you Exclude Access to a column and select Read Only,
				// you can only read data (otherwise no access).
			{
				if (m_columnAccess[i].getAD_Table_ID() == AD_Table_ID
						&& m_columnAccess[i].getAD_Column_ID() == AD_Column_ID) {
					if (!ro) // just R/O Access requested
						retValue = !m_columnAccess[i].isReadOnly();
					else
						retValue = false;
					if (!retValue)
						log.fine("Exclude AD_Table_ID=" + AD_Table_ID
								+ ", AD_Column_ID=" + AD_Column_ID + " (ro="
								+ ro + ",ColumnAccessRO="
								+ m_columnAccess[i].isReadOnly() + ") = "
								+ retValue);
					return retValue;
				}
			} else // Include
				// If you Include Access to a column and select Read Only,
				// you can only read data (otherwise full access).
			{
				if (m_columnAccess[i].getAD_Table_ID() == AD_Table_ID) {
					// retValue = false;
					if (m_columnAccess[i].getAD_Column_ID() == AD_Column_ID) {
						if (!ro) // rw only if not r/o
							retValue = !m_columnAccess[i].isReadOnly();
						else
							retValue = true;
						if (!retValue)
							log.fine("Include AD_Table_ID=" + AD_Table_ID
									+ ", AD_Column_ID=" + AD_Column_ID
									+ " (ro=" + ro + ",ColumnAccessRO="
									+ m_columnAccess[i].isReadOnly() + ") = "
									+ retValue);
						return retValue;
					}
				} // same table
			} // include
		} // for all Table Access
		if (!retValue)
			log.fine("AD_Table_ID=" + AD_Table_ID + ", AD_Column_ID="
					+ AD_Column_ID + " (ro=" + ro + ") = " + retValue);
		return retValue;
	} // isColumnAccess

	/**
	 * Access to Record (no check of table)
	 * 
	 * @param AD_Table_ID
	 *            table
	 * @param Record_ID
	 *            record
	 * @param ro
	 *            read only
	 * @return boolean
	 */
	public boolean isRecordAccess(int AD_Table_ID, int Record_ID, boolean ro) {
		// if (!isTableAccess(AD_Table_ID, ro)) // No Access to Table
		// return false;
		loadRecordAccess(false);
		boolean negativeList = true;
		for (MRecordAccess ra : m_recordAccess) {
			if (ra.getAD_Table_ID() != AD_Table_ID)
				continue;

			if (ra.isExclude()) // Exclude
				// If you Exclude Access to a column and select Read Only,
				// you can only read data (otherwise no access).
			{
				if (ra.getRecord_ID() == Record_ID) {
					if (ro)
						return ra.isReadOnly();
					else
						return false;
				}
			} else // Include
				// If you Include Access to a column and select Read Only,
				// you can only read data (otherwise full access).
			{
				negativeList = false; // has to be defined
				if (ra.getRecord_ID() == Record_ID) {
					if (!ro)
						return !ra.isReadOnly();
					else
						// ro
						return true;
				}
			}
		} // for all Table Access
		return negativeList;
	} // isRecordAccess

	/**
	 * Get Window Access
	 * 
	 * @param AD_Window_ID
	 *            window
	 * @return null in no access, TRUE if r/w and FALSE if r/o
	 */
	public Boolean getWindowAccess(int AD_Window_ID) {
		if (m_windowAccess == null) {
			m_windowAccess = new HashMap<Integer, Boolean>(100);
			String sql = "SELECT AD_Window_ID, IsReadWrite FROM AD_Window_Access WHERE AD_Role_ID=? AND IsActive='Y'";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getAD_Role_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
					m_windowAccess.put(Integer.valueOf(rs.getInt(1)), Boolean
							.valueOf("Y".equals(rs.getString(2))));
			} 
			catch (Exception e) {
				log.log(Level.SEVERE, sql, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			log.fine("#" + m_windowAccess.size());
		} // reload
		Boolean retValue = m_windowAccess.get(Integer.valueOf(AD_Window_ID));
		// log.fine("getWindowAccess - AD_Window_ID=" + AD_Window_ID + " - " +
		// retValue);
		return retValue;
	} // getWindowAccess

	/**
	 * Get Process Access
	 * 
	 * @param AD_Process_ID
	 *            process
	 * @return null in no access, TRUE if r/w and FALSE if r/o
	 */
	public Boolean getProcessAccess(int AD_Process_ID) {
		if (m_processAccess == null) {
			m_processAccess = new HashMap<Integer, Boolean>(50);
			String sql = "SELECT AD_Process_ID, IsReadWrite FROM AD_Process_Access WHERE AD_Role_ID=? AND IsActive='Y'";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getAD_Role_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
					m_processAccess.put(Integer.valueOf(rs.getInt(1)), Boolean
							.valueOf("Y".equals(rs.getString(2))));
			} 
			catch (Exception e) {
				log.log(Level.SEVERE, sql, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		} // reload
		return m_processAccess.get(Integer.valueOf(AD_Process_ID));
	} // getProcessAccess

	/**
	 * Get Task Access
	 * 
	 * @param AD_Task_ID
	 *            task
	 * @return null in no access, TRUE if r/w and FALSE if r/o
	 */
	public Boolean getTaskAccess(int AD_Task_ID) {
		if (m_taskAccess == null) {
			m_taskAccess = new HashMap<Integer, Boolean>(10);
			String sql = "SELECT AD_Task_ID, IsReadWrite FROM AD_Task_Access "
				+ "WHERE AD_Role_ID=? AND IsActive='Y'";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getAD_Role_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
					m_taskAccess.put(Integer.valueOf(rs.getInt(1)), Boolean
							.valueOf("Y".equals(rs.getString(2))));
			} 
			catch (Exception e) {
				log.log(Level.SEVERE, sql, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		} // reload
		return m_taskAccess.get(Integer.valueOf(AD_Task_ID));
	} // getTaskAccess

	/**
	 * Get Form Access
	 * 
	 * @param AD_Form_ID
	 *            form
	 * @return null in no access, TRUE if r/w and FALSE if r/o
	 */
	public Boolean getFormAccess(int AD_Form_ID) {
		if (m_formAccess == null) {
			m_formAccess = new HashMap<Integer, Boolean>(20);
			String sql = "SELECT AD_Form_ID, IsReadWrite FROM AD_Form_Access "
				+ "WHERE AD_Role_ID=? AND IsActive='Y'";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getAD_Role_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
					m_formAccess.put(Integer.valueOf(rs.getInt(1)), Boolean
							.valueOf("Y".equals(rs.getString(2))));
			} 
			catch (Exception e) {
				log.log(Level.SEVERE, sql, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		} // reload
		return m_formAccess.get(Integer.valueOf(AD_Form_ID));
	} // getTaskAccess

	/**
	 * Get Workflow Access
	 * 
	 * @param AD_Workflow_ID
	 *            workflow
	 * @return null in no access, TRUE if r/w and FALSE if r/o
	 */
	public Boolean getWorkflowAccess(int AD_Workflow_ID) {
		if (m_workflowAccess == null) {
			m_workflowAccess = new HashMap<Integer, Boolean>(20);
			String sql = "SELECT AD_Workflow_ID, IsReadWrite FROM AD_Workflow_Access "
				+ "WHERE AD_Role_ID=? AND IsActive='Y'";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getAD_Role_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
					m_workflowAccess.put(Integer.valueOf(rs.getInt(1)), Boolean
							.valueOf("Y".equals(rs.getString(2))));
			} 
			catch (Exception e) {
				log.log(Level.SEVERE, sql, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

		} // reload
		return m_workflowAccess.get(Integer.valueOf(AD_Workflow_ID));
	} // getTaskAccess

	public String addAccessSQL(String SQL, String TableNameIn,
			boolean fullyQualified, boolean rw) {
		return addAccessSQL(SQL, TableNameIn, fullyQualified, rw, false);
	}
	/*************************************************************************
	 * Appends where clause to SQL statement for Table
	 * 
	 * @param SQL
	 *            existing SQL statement
	 * @param TableNameIn
	 *            Table Name or list of table names AAA, BBB or AAA a, BBB b
	 * @param fullyQualified
	 *            fullyQualified names
	 * @param rw
	 *            if false, includes System Data
	 * @return updated SQL statement
	 */
	public String addAccessSQL(String SQL, String TableNameIn,
			boolean fullyQualified, boolean rw, boolean addOrgAccessForAll) {
		if (fullyQualified && Util.isEmpty(TableNameIn))
			fullyQualified = false;

		StringBuffer retSQL = new StringBuffer();

		// Cut off last ORDER BY clause
		String orderBy = "";
		int posOrder = SQL.lastIndexOf(" ORDER BY ");
		if (posOrder != -1) {
			orderBy = SQL.substring(posOrder);
			retSQL.append(SQL.substring(0, posOrder));
		} else
			retSQL.append(SQL);

		// Parse SQL
		AccessSqlParser asp = new AccessSqlParser(retSQL.toString());
		AccessSqlParser.TableInfo[] ti = asp
		.getTableInfo(asp.getMainSqlIndex());

		// Do we have to add WHERE or AND
		if (asp.getMainSql().indexOf(" WHERE ") == -1)
			retSQL.append(" WHERE ");
		else
			retSQL.append(" AND ");

		// Use First Table
		String tableName = "";
		if (ti.length > 0
				&& (ti[0].getTableName().equals(TableNameIn) || ti[0]
				                                                   .getSynonym().equals(TableNameIn))) {
			tableName = ti[0].getSynonym();
			if (Util.isEmpty(tableName))
				tableName = ti[0].getTableName();
		}
		// Check for error condition
		if (!Util.isEmpty(TableNameIn) && Util.isEmpty(tableName)) {
			String msg = "TableName not correctly parsed - TableNameIn="
				+ TableNameIn + " - " + asp;
			if (ti.length > 0)
				msg += " - #1 " + ti[0];
			msg += "\n = " + SQL;
			log.log(Level.SEVERE, msg);
			Trace.printStack();
			tableName = TableNameIn;
		}

		// Client Access
		if (fullyQualified && !Util.isEmpty(tableName))
			retSQL.append(tableName).append(".");
		
		//BECO
		if(tableName.equalsIgnoreCase("C_Order") 
				|| tableName.equalsIgnoreCase("M_InOut") 
					|| tableName.equalsIgnoreCase("XX_VMR_SaleOrder") 
						|| tableName.equalsIgnoreCase("C_Invoice"))
			retSQL.append(getClientWhere(true));
		else
			retSQL.append(getClientWhere(rw));
		//FIN BECO
		
		// Org Access
		if (!isAccessAllOrgs() && !addOrgAccessForAll) {
			retSQL.append(" AND ");
			if (fullyQualified && !Util.isEmpty(tableName))
				retSQL.append(getOrgWhere(tableName, rw));
			else
				retSQL.append(getOrgWhere(null, rw));
		} 

		if (isUseBPRestrictions()) {
			String documentWhere = getDocWhere(tableName);
			if (documentWhere.length() > 0) {
				retSQL.append(" AND ");
				retSQL.append(documentWhere);
			}
		}

		// ** Data Access **
		for (TableInfo element : ti) {
			String TableName = element.getTableName();
			int AD_Table_ID = getAD_Table_ID(TableName);
			
			// Org Access
			if(AD_Table_ID != 0 && !isAccessAllOrgs() && addOrgAccessForAll) {
				String TableSynonym = element.getSynonym();
				if(TableSynonym == null || TableSynonym.isEmpty())
					TableSynonym = TableName;
				
				retSQL.append(" AND ");
				retSQL.append(getOrgWhere(TableSynonym, rw));
			}
			
			
			// Data Table Access
			if (AD_Table_ID != 0 && !isTableAccess(AD_Table_ID, !rw)) {
				retSQL.append(" AND 1=3"); // prevent access at all
				log.fine("No access to AD_Table_ID=" + AD_Table_ID + " - "
						+ TableName + " - " + retSQL);
				break; // no need to check further
			}

			// Data Column Access

			// Data Record Access
			String keyColumnName = "";
			if (fullyQualified) {
				keyColumnName = element.getSynonym(); // table synonym
				if (keyColumnName.length() == 0)
					keyColumnName = TableName;
				keyColumnName += ".";
			}
			keyColumnName += TableName + "_ID"; // derived from table

			// log.fine("addAccessSQL - " + TableName + "(" + AD_Table_ID + ") "
			// + keyColumnName);
			String recordWhere = getRecordWhere(AD_Table_ID, keyColumnName, rw);
			if (recordWhere.length() > 0) {
				retSQL.append(" AND ").append(recordWhere);
				log.finest("Record access - " + recordWhere);
			}
		} // for all table info

		// Dependent Records (only for main SQL)
		String mainSql = asp.getMainSql();
		loadRecordAccess(false);
		int AD_Table_ID = 0;
		String whereColumnName = null;
		ArrayList<Integer> includes = new ArrayList<Integer>();
		ArrayList<Integer> excludes = new ArrayList<Integer>();
		MTable table = MTable.get(getCtx(), TableNameIn);

		for (int i = 0; i < m_recordDependentAccess.length; i++) {
			String columnName = m_recordDependentAccess[i].getKeyColumnName(asp
					.getTableInfo(asp.getMainSqlIndex()));
			if (columnName == null)
				continue; // no key column
			int posColumn = mainSql.indexOf(columnName);
			if (posColumn == -1)
				continue;
			// we found the column name - make sure it's a clumn name
			char charCheck = mainSql.charAt(posColumn - 1); // before
			if (!(charCheck == ',' || charCheck == '.' || charCheck == ' ' || charCheck == '('))
				continue;
			charCheck = mainSql.charAt(posColumn + columnName.length()); // after
			if (!(charCheck == ',' || charCheck == ' ' || charCheck == ')'))
				continue;

			if (AD_Table_ID != 0
					&& AD_Table_ID != m_recordDependentAccess[i]
					                                          .getAD_Table_ID())
				retSQL.append(getDependentAccess(whereColumnName, includes,
						excludes));

			AD_Table_ID = m_recordDependentAccess[i].getAD_Table_ID();
			// *** we found the column in the main query
			if (m_recordDependentAccess[i].isExclude()) {
				excludes.add(m_recordDependentAccess[i].getRecord_ID());
				log.fine("Exclude " + columnName + " - "
						+ m_recordDependentAccess[i]);
			} else if (!rw || !m_recordDependentAccess[i].isReadOnly()) {
				includes.add(m_recordDependentAccess[i].getRecord_ID());
				log.fine("Include " + columnName + " - "
						+ m_recordDependentAccess[i]);
			}

			MColumn column = table.getColumn(columnName);
			if (column != null) {
				String columnSQL = column.getColumnSQL();
				if (columnSQL == null || columnSQL.length() == 0)
					whereColumnName = getDependentRecordWhereColumn(mainSql,
							columnName);
				else
					whereColumnName = columnSQL;
			} else
				whereColumnName = getDependentRecordWhereColumn(mainSql,
						columnName);
		} // for all dependent records

		retSQL.append(getDependentAccess(whereColumnName, includes, excludes));
		//
		retSQL.append(orderBy);
		log.finest(retSQL.toString());
		return retSQL.toString();
	} // addAccessSQL

	/**
	 * Get Dependent Access
	 * 
	 * @param whereColumnName
	 *            column
	 * @param includes
	 *            ids to include
	 * @param excludes
	 *            ids to exclude
	 * @return where clause starting with AND or ""
	 */
	private String getDependentAccess(String whereColumnName,
			ArrayList<Integer> includes, ArrayList<Integer> excludes) {
		if (includes.size() == 0 && excludes.size() == 0)
			return "";
		if (includes.size() != 0 && excludes.size() != 0)
			log
			.warning("Mixing Include and Excluse rules - Will not return values");

		StringBuffer where = new StringBuffer(" AND ");
		if (includes.size() == 1)
			where.append(whereColumnName).append("=").append(includes.get(0));
		else if (includes.size() > 1) {
			where.append(whereColumnName).append(" IN (");
			for (int ii = 0; ii < includes.size(); ii++) {
				if (ii > 0)
					where.append(",");
				where.append(includes.get(ii));
			}
			where.append(")");
		} else if (excludes.size() == 1)
			where.append(whereColumnName).append("<>").append(excludes.get(0));
		else if (excludes.size() > 1) {
			where.append(whereColumnName).append(" NOT IN (");
			for (int ii = 0; ii < excludes.size(); ii++) {
				if (ii > 0)
					where.append(",");
				where.append(excludes.get(ii));
			}
			where.append(")");
		}
		log.finest(where.toString());
		return where.toString();
	} // getDependentAccess

	/**
	 * Get Dependent Record Where clause
	 * 
	 * @param mainSql
	 *            sql to examine
	 * @param columnName
	 *            columnName
	 * @return where clause column "x.columnName"
	 */
	private String getDependentRecordWhereColumn(String mainSql,
			String columnName) {
		String retValue = columnName; // if nothing else found
		int index = mainSql.indexOf(columnName);
		// see if there are table synonym
		int offset = index - 1;
		char c = mainSql.charAt(offset);
		if (c == '.') {
			StringBuffer sb = new StringBuffer();
			while (c != ' ' && c != ',' && c != '(') // delimeter
			{
				sb.insert(0, c);
				c = mainSql.charAt(--offset);
			}
			sb.append(columnName);
			return sb.toString();
		}
		return retValue;
	} // getDependentRecordWhereColumn

	/**
	 * UPADATE - Can I Update the record. Access error info
	 * (AccessTableNoUpdate) is saved in the log
	 * 
	 * @param AD_Client_ID
	 *            comntext to derive client/org/user level
	 * @param AD_Org_ID
	 *            number of the current window to retrieve context
	 * @param AD_Table_ID
	 *            table
	 * @param Record_ID
	 *            record id
	 * @param createError
	 *            boolean
	 * @return true if you can update see
	 *         org.compiere.model.MTable#dataSave(boolean)
	 **/
	public boolean canUpdate(int AD_Client_ID, int AD_Org_ID, int AD_Table_ID,
			int Record_ID, boolean createError) {
		String userLevel = getUserLevel().trim(); // Format 'SCO'

		boolean retValue = true;
		String whatMissing = "";

		// System == Client=0 & Org=0
		if (AD_Client_ID == 0 && AD_Org_ID == 0 && userLevel.indexOf('S') == -1) {
			retValue = false;
			whatMissing += "S";
		}

		// Client == Client!=0 & Org=0
		else if (AD_Client_ID != 0 && AD_Org_ID == 0
				&& userLevel.indexOf('C') == -1) {
			if (userLevel.indexOf('O') == -1 && isOrgAccess(AD_Org_ID, true))
				; // Client+Org with access to *
			else {
				retValue = false;
				whatMissing += "C";
			}
		}

		// Organization == Client!=0 & Org!=0
		else if (AD_Client_ID != 0 && AD_Org_ID != 0
				&& userLevel.indexOf('O') == -1) {
			retValue = false;
			whatMissing += "O";
		}

		// Data Access
		if (retValue)
			retValue = isTableAccess(AD_Table_ID, false);

		if (retValue && Record_ID != 0)
			retValue = isRecordAccess(AD_Table_ID, Record_ID, false);

		if (!retValue && createError) {
			log
			.saveWarning("AccessTableNoUpdate", "AD_Client_ID="
					+ AD_Client_ID + ", AD_Org_ID=" + AD_Org_ID
					+ ", UserLevel=" + userLevel + " => missing="
					+ whatMissing);
			log.warning(toString());
		}
		return retValue;
	} // canUpdate

	/**
	 * VIEW - Can I view record in Table with given TableLevel. <code>
	 * 	TableLevel			S__ 100		4	System info
	 * 						SCO	111		7	System shared infopwd
	 * 
	 * 						SC_ 110		6	System/Client info
	 * 						_CO	011		3	Client shared info
	 * 						_C_	011		2	Client shared info
	 * 						__O	001		1	Organization info
	 *  </code>
	 * 
	 * @param ctx
	 *            context
	 * @param TableLevel
	 *            AccessLevel
	 * @return true/false Access error info (AccessTableNoUpdate,
	 *         AccessTableNoView) is saved in the log see
	 *         org.compiere.model.MTabVO#loadTabDetails(MTabVO, ResultSet)
	 **/
	public boolean canView(Ctx ctx, String TableLevel) {
		String userLevel = getUserLevel().trim(); // Format 'SCO'

		boolean retValue = true;

		// 7 - All
		if (X_AD_Table.ACCESSLEVEL_All.equals(TableLevel))
			retValue = true;

		// 4 - System data requires S
		else if (X_AD_Table.ACCESSLEVEL_SystemOnly.equals(TableLevel)
				&& userLevel.indexOf('S') == -1)
			retValue = false;

		// 2 - Client data requires C
		else if (X_AD_Table.ACCESSLEVEL_TenantOnly.equals(TableLevel)
				&& userLevel.indexOf('C') == -1)
			retValue = false;

		// 1 - Organization data requires O
		else if (X_AD_Table.ACCESSLEVEL_Organization.equals(TableLevel)
				&& userLevel.indexOf('O') == -1)
			retValue = false;

		// 3 - Client Shared requires C or O
		else if (X_AD_Table.ACCESSLEVEL_TenantPlusOrganization
				.equals(TableLevel)
				&& !(userLevel.indexOf('C') != -1 || userLevel.indexOf('O') != -1))
			retValue = false;

		// 6 - System/Client requires S or C
		else if (X_AD_Table.ACCESSLEVEL_SystemPlusTenant.equals(TableLevel)
				&& !(userLevel.indexOf('S') != -1 || userLevel.indexOf('C') != -1))
			retValue = false;

		if (retValue)
			return retValue;

		// Notification
		/**
		 * if (forInsert) log.saveWarning("AccessTableNoUpdate", "(Required=" +
		 * TableLevel + "(" + getTableLevelString(Env.getAD_Language(ctx),
		 * TableLevel) + ") != UserLevel=" + userLevel); else
		 **/
		log.saveWarning("AccessTableNoView", "Required=" + TableLevel + "("
				+ getTableLevelString(Env.getAD_Language(ctx), TableLevel)
				+ ") != UserLevel=" + userLevel);
		log.info(toString());
		return retValue;
	} // canView

	/**
	 * Returns clear text String of TableLevel
	 * 
	 * @param AD_Language
	 *            language
	 * @param TableLevel
	 *            level
	 * @return info
	 */
	private String getTableLevelString(String AD_Language, String TableLevel) {
		String level = TableLevel + "??";
		if (TableLevel.equals("1"))
			level = "AccessOrg";
		else if (TableLevel.equals("2"))
			level = "AccessClient";
		else if (TableLevel.equals("3"))
			level = "AccessClientOrg";
		else if (TableLevel.equals("4"))
			level = "AccessSystem";
		else if (TableLevel.equals("6"))
			level = "AccessSystemClient";
		else if (TableLevel.equals("7"))
			level = "AccessShared";

		return Msg.getMsg(AD_Language, level);
	} // getTableLevelString

	/**
	 * Get Table ID from name
	 * 
	 * @param tableName
	 *            table name
	 * @return AD_Table_ID or 0
	 */
	private int getAD_Table_ID(String tableName) {
		loadTableInfo(false);
		Integer ii = m_tableName.get(tableName);
		if (ii != null)
			return ii.intValue();
		// log.log(Level.WARNING,"getAD_Table_ID - not found (" + tableName +
		// ")");
		return 0;
	} // getAD_Table_ID

	/**
	 * Return Where clause for Record Access
	 * 
	 * @param AD_Table_ID
	 *            table
	 * @param keyColumnName
	 *            (fully qualified) key column name
	 * @param rw
	 *            true if read write
	 * @return where clause or ""
	 */
	private String getRecordWhere(int AD_Table_ID, String keyColumnName,
			boolean rw) {
		loadRecordAccess(false);
		//
		StringBuffer sbInclude = new StringBuffer();
		StringBuffer sbExclude = new StringBuffer();
		// Role Access
		for (int i = 0; i < m_recordAccess.length; i++) {
			if (m_recordAccess[i].getAD_Table_ID() == AD_Table_ID) {
				// NOT IN (x)
				if (m_recordAccess[i].isExclude()) {
					if (sbExclude.length() == 0)
						sbExclude.append(keyColumnName).append(" NOT IN (");
					else
						sbExclude.append(",");
					sbExclude.append(m_recordAccess[i].getRecord_ID());
				}
				// IN (x)
				else if (!rw || !m_recordAccess[i].isReadOnly()) // include
				{
					if (sbInclude.length() == 0)
						sbInclude.append(keyColumnName).append(" IN (");
					else
						sbInclude.append(",");
					sbInclude.append(m_recordAccess[i].getRecord_ID());
				}
			}
		} // for all Table Access

		StringBuffer sb = new StringBuffer();
		if (sbExclude.length() > 0)
			sb.append(sbExclude).append(")");
		if (sbInclude.length() > 0) {
			if (sb.length() > 0)
				sb.append(" AND ");
			sb.append(sbInclude).append(")");
		}

		// Don't ignore Privacy Access
		if (!isPersonalAccess()) {
			String lockedIDs = MPrivateAccess.getLockedRecordWhere(AD_Table_ID,
					m_AD_User_ID);
			if (lockedIDs.length() > 0) {
				if (sb.length() > 0)
					sb.append(" AND ");
				sb.append(keyColumnName).append(lockedIDs);
			}
		}
		//
		return sb.toString();
	} // getRecordWhere

	/**
	 * Show (Value) Preference Menu
	 * 
	 * @return true if preference type is not None
	 */
	public boolean isShowPreference() {
		return !X_AD_Role.PREFERENCETYPE_None.equals(getPreferenceType());
	} // isShowPreference

	/**
	 * Display Client
	 * 
	 * @return true if display client
	 */
	public boolean isDisplayClient() {
		String s = getDisplayClientOrg();
		return s == null || DISPLAYCLIENTORG_AlwaysTenantOrganization.equals(s);
	} // isDisplayClient

	/**
	 * Display Org
	 * 
	 * @return true if Org should be displayed
	 */
	public boolean isDisplayOrg() {
		String s = getDisplayClientOrg();
		return s == null || DISPLAYCLIENTORG_AlwaysTenantOrganization.equals(s)
		|| DISPLAYCLIENTORG_OnlyOrganization.equals(s);
	} // isDisplayOrg

	/**
	 * Org Access Summary
	 */
	class OrgAccess {
		/**
		 * Org Access constructor
		 * 
		 * @param ad_Client_ID
		 *            client
		 * @param ad_Org_ID
		 *            org
		 * @param readonly
		 *            r/o
		 */
		public OrgAccess(int ad_Client_ID, int ad_Org_ID, boolean readonly) {
			this.AD_Client_ID = ad_Client_ID;
			this.AD_Org_ID = ad_Org_ID;
			this.readOnly = readonly;
		}

		/** Client */
		public int AD_Client_ID = 0;
		/** Organization */
		public int AD_Org_ID = 0;
		/** Read Only */
		public boolean readOnly = true;

		/**
		 * Equals
		 * 
		 * @param obj
		 *            object to compare
		 * @return true if equals
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj != null && obj instanceof OrgAccess) {
				OrgAccess comp = (OrgAccess) obj;
				return comp.AD_Client_ID == AD_Client_ID
				&& comp.AD_Org_ID == AD_Org_ID;
			}
			return false;
		} // equals

		/**
		 * Hash Code
		 * 
		 * @return hash Code
		 */
		@Override
		public int hashCode() {
			return AD_Client_ID * 7 + AD_Org_ID;
		} // hashCode

		/**
		 * Extended String Representation
		 * 
		 * @return extended info
		 */
		@Override
		public String toString() {
			String clientName = "System";
			if (AD_Client_ID != 0)
				clientName = MClient.get(getCtx(), AD_Client_ID).getName();
			String orgName = "*";
			if (AD_Org_ID != 0)
				orgName = MOrg.get(getCtx(), AD_Org_ID).getName();
			StringBuffer sb = new StringBuffer();
			sb.append(Msg.translate(getCtx(), "AD_Client_ID")).append("=")
			.append(clientName).append(" - ").append(
					Msg.translate(getCtx(), "AD_Org_ID")).append("=")
					.append(orgName);
			if (readOnly)
				sb.append(" r/o");
			return sb.toString();
		} // toString

	} // OrgAccess

} // MRole
