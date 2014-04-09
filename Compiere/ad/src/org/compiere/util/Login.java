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
package org.compiere.util;

import java.math.*;
import java.net.*;
import java.security.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.*;
import org.compiere.common.CompiereStateException;
import org.compiere.common.constants.*;
import org.compiere.db.*;
import org.compiere.framework.*;
import org.compiere.model.*;


/**
 *	Login Manager
 *
 *  @author Jorg Janke
 *  @version $Id: Login.java 8798 2010-05-21 17:56:59Z nnayak $
 */
public class Login
{
	/**
	 * Default context values, cached by AD_Role_ID
	 */
	private static CCache<Integer, FutureTask<Map<String, String>>> s_roleDefaults = new CCache<Integer, FutureTask<Map<String, String>>>(
			"roleDefaults");
	
	
	
	/**
	 *  Test Init - Set Environment for tests
	 *	@param isClient client session
	 *	@return Context
	 */
	public static Ctx initTest (boolean isClient)
	{
	//	logger.entering("Env", "initTest");
		if (!Compiere.startupEnvironment(true, null))
			System.exit (1);
		//  Test Context
		Ctx ctx = Env.getCtx();
		Login login = new Login(ctx);
		KeyNamePair[] roles = login.getRoles(CConnection.get(),
			"System", "System", true);
		//  load role
		if (roles != null && roles.length > 0)
		{
			KeyNamePair[] clients = login.getClients (roles[0]);
			//  load client
			if (clients != null && clients.length > 0)
			{
				KeyNamePair[] orgs = login.getOrgs(clients[0]);
				//  load org
				if (orgs != null && orgs.length > 0)
				{
				//	KeyNamePair[] whs = login.getWarehouses(orgs[0]);
					login.loadPreferences(orgs[0], null, null, null);
				}
			}
		}
		//
		ctx.setContext("#Date", String.valueOf(System.currentTimeMillis()));
	//	logger.exiting("Env", "initTest");
		return ctx;
	}   //  testInit

	/**
	 *  Java Version Test
	 *  @param isClient client connection
	 *  @return true if Java Version is OK
	 */
	public static boolean isJavaOK (boolean isClient)
	{
		//	Java System version check
		String jVersion = System.getProperty("java.version");
		if (jVersion.startsWith("1.6"))
			return true;
		//  Warning
		boolean ok = false;
	//	if (jVersion.startsWith("1.6"))
	//		|| jVersion.startsWith("1.5.1"))	//  later/earlier release
	//		ok = true;

		//  Error Message
		StringBuffer msg = new StringBuffer();
		msg.append(System.getProperty("java.vm.name")).append(" - ").append(jVersion);
		if (ok)
			msg.append("(untested)");
		msg.append("  <>  1.6");
		//
		if (isClient)
			JOptionPane.showMessageDialog(null, msg.toString(),
				org.compiere.Compiere.getName() + " - Java Version Check",
				ok ? JOptionPane.WARNING_MESSAGE : JOptionPane.ERROR_MESSAGE);
		else
		{
			log.severe(msg.toString());
			throw new UnsupportedOperationException(msg.toString());
		}
		return ok;
	}   //  isJavaOK


	/**************************************************************************
	 * 	Login
	 * 	@param ctx context
	 */
	public Login (Ctx ctx)
	{
		if (ctx == null)
			throw new IllegalArgumentException("Context missing");
		m_ctx = ctx;
	}	//	Login

	/**	Logger				*/
	private static CLogger log = CLogger.getCLogger(Login.class);
	/** Context				*/
	private Ctx		 		m_ctx = null;
	/** Connection Profile	*/
	private String			m_connectionProfile = null;
	/** List of Roles		*/
	private ArrayList<KeyNamePair> m_roles = new ArrayList<KeyNamePair>();
	/** List of Users for Roles		*/
	private ArrayList<Integer> m_users = new ArrayList<Integer>();
	/** The Current User	*/
	private KeyNamePair		m_user = null;
	/** The Current Role	*/
	private KeyNamePair		m_role = null;
	/** The Current Org		*/
	private KeyNamePair		m_org = null;
	/** Web Store Login		*/
	private X_W_Store		m_store = null;
	/** The Current Role	*/
	private MRole			m_roleModel = null;

	/**
	 *	(Test) Client Login.
	 *	(Translation, print.Viewer)
	 *  <p>
	 *  - Get Connection
	 *  - Compare User info
	 *  <p>
	 *  Sets Context with login info
	 * @param cc connection
	 * @param app_user user
	 * @param app_pwd pwd
	 * @param force ignore pwd
	 * @return  Array of Role KeyNamePair or null if error
	 * The error (NoDatabase, UserPwdError, DBLogin) is saved in the log
	 */
	protected KeyNamePair[] getRoles (CConnection cc,
		String app_user, String app_pwd, boolean force)
	{
		//	Establish connection
		DB.setDBTarget(cc);
		m_ctx.setContext("#Host", cc.getAppsHost());
		m_ctx.setContext("#Database", cc.getDbName());

		if (DB.getDatabase() == null)
		{
			log.saveError("NoDatabase", "");
			return null;
		}
		if (app_pwd == null)
			return null;
		//
		return getRoles (app_user, app_pwd, force, false);
	}   //  getRoles

	/**
	 *  (Web) Client Login.
	 *  (Web Store)
	 *  <p>
	 *  Compare User Info
	 *  <p>
	 *  Sets Context with login info
	 *  @param app_user Principal
	 *  @return role array or null if in error.
	 *  The error (NoDatabase, UserPwdError, DBLogin) is saved in the log
	 */
	public KeyNamePair[] getRoles (Principal app_user)
	{
		if (app_user == null)
			return null;
		//  login w/o password as previously authorized
		return getRoles (app_user.getName(), null, false, false);
	}   //  getRoles

	/**
	 * Attempt to change roles using a previously authenticated Login.  This function should
	 * only be called after a successful login.
	 * @return role array or null if in error.
	 */
	public KeyNamePair[] getRoles()
	{
		return m_roles.toArray( new KeyNamePair[ m_roles.size() ] );
	}   //  login

	/**
	 *  Client Login.
	 *  (CM, Swing)
	 *  <p>
	 *  Compare User Info
	 *  <p>
	 *  Sets Conext with login info
	 *  @param app_user user id
	 *  @param app_pwd password
	 *  @return role array or null if in error.
	 *  The error (NoDatabase, UserPwdError, DBLogin) is saved in the log
	 */
	public KeyNamePair[] getRoles (String app_user, String app_pwd )
	{
		return getRoles (app_user, app_pwd, false, false);
	}   //  login


	/**
	 *  Client Login.
	 *  (Web UI)
	 *  <p>
	 *  Compare User Info
	 *  <p>
	 *  Sets Conext with login info
	 *  @param app_user user id (email or normal)
	 *  @param app_pwd password
	 *  @param W_Store_ID web store
	 *  @return role array or null if in error.
	 *  The error (NoDatabase, UserPwdError, DBLogin) is saved in the log
	 */
	public KeyNamePair[] getRoles (String app_user, String app_pwd, int W_Store_ID)
	{
		KeyNamePair[] roles = null;
		if (W_Store_ID >= 0)
		{
			roles = getRolesByEmail(app_user, app_pwd, W_Store_ID);
			if (roles != null)
			{
				m_store = new X_W_Store(m_ctx, W_Store_ID, null);
				if (W_Store_ID != m_store.get_ID())
				{
					log.warning("Cannot find W_Store_ID=" + W_Store_ID);
					m_store = null;
					return null;
				}
				m_ctx.setContext("#AD_Client_ID", m_store.getAD_Client_ID());
				m_ctx.setContext("#AD_Org_ID", m_store.getAD_Org_ID());
				m_ctx.setContext("#User_Level", "  O");  	//	Format 'SCO'
				m_ctx.setContext("#SalesRep_ID", m_store.getSalesRep_ID());
				m_ctx.setContext("#M_PriceList_ID", m_store.getM_PriceList_ID());
			}
		}
		if (roles == null)	//	Regular
			roles = getRoles (app_user, app_pwd, false, false);
		return roles;
	}   //  login


	/**
	 *  Actual DB login procedure.
	 *  @param app_user user
	 *  @param app_pwd pwd
	 *  @param force ignore pwd
	 *  @param ignore_pwd If true, indicates that the user had previously authenticated successfully, and therefore
	 *  there is no need to check password again.  This differs from the <b>force</b> parameter in that <b>force</b>
	 *  will force a login with System Administrator privileges.
	 *  @return role array or null if in error.
	 *  The error (NoDatabase, UserPwdError, DBLogin) is saved in the log
	 */
	private KeyNamePair[] getRoles (String app_user, String app_pwd, boolean force, boolean ignore_pwd )
	{
		log.info("User=" + app_user);
		long start = System.currentTimeMillis();
		if (app_user == null)
		{
			log.warning("No Apps User");
			return null;
		}

		//	Authenticate
		boolean authenticated = false;
		MSystem system = MSystem.get(m_ctx);
		if (system == null)
			throw new CompiereStateException("No System Info");

		if (system.isLDAP())
		{
			authenticated = system.isLDAP(app_user, app_pwd);
			if (authenticated)
				app_pwd = null;
			//	if not authenticated, use AD_User as backup
		}
		else if ( (app_pwd == null || app_pwd.length() == 0) && !ignore_pwd )
		{
			log.warning("No Apps Password");
			return null;
		}

		//	Cannot use encrypted password
		if (app_pwd != null && SecureEngine.isEncrypted(app_pwd))
		{
			log.warning("Cannot use Encrypted Password");
			return null;
		}

		KeyNamePair[] retValue = null;
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		//
		StringBuffer sql = new StringBuffer("SELECT u.AD_User_ID, r.AD_Role_ID,r.Name,")
			.append(" u.ConnectionProfile, u.Password ")	//	4,5
			.append("FROM AD_User u")
			.append(" INNER JOIN AD_User_Roles ur ON (u.AD_User_ID=ur.AD_User_ID AND ur.IsActive='Y')")
			.append(" INNER JOIN AD_Role r ON (ur.AD_Role_ID=r.AD_Role_ID AND r.IsActive='Y') ")
			.append("WHERE COALESCE(u.LDAPUser,u.Name)=?")		//	#1
			.append(" AND u.IsActive='Y'")
			.append(" AND EXISTS (SELECT * FROM AD_Client c WHERE u.AD_Client_ID=c.AD_Client_ID AND c.IsActive='Y')")
			.append(" AND EXISTS (SELECT * FROM AD_Client c WHERE r.AD_Client_ID=c.AD_Client_ID AND c.IsActive='Y')");
		if (app_pwd != null)
			sql.append(" AND (u.Password=? OR u.Password=?)");	//  #2/3
		sql.append(" ORDER BY r.Name");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			pstmt.setString(1, app_user);
			if (app_pwd != null)
			{
				pstmt.setString(2, app_pwd);
				pstmt.setString(3, SecureEngine.encrypt(app_pwd));
			}
			//	execute a query
			rs = pstmt.executeQuery();

			if (!rs.next())		//	no record found
				if (force)
				{
					m_ctx.setAD_User_ID(0);
					m_ctx.setContext("##AD_User_Name", "System (force)");
					m_ctx.setContext("##AD_User_Description", "System Forced Login");
					m_ctx.setContext("#User_Level", "S  ");  	//	Format 'SCO'
					m_ctx.setContext("#User_Client", "0");		//	Format c1, c2, ...
					m_ctx.setContext("#User_Org", "0"); 		//	Format o1, o2, ...
					m_user = new KeyNamePair(0, app_user + " (force)");
					retValue = new KeyNamePair[] {new KeyNamePair(0, "System Administrator (force)")};
					return retValue;
				}
				else
				{
					log.saveError("UserPwdError", app_user, false);
					return null;
				}

			int AD_User_ID = rs.getInt(1);
			m_ctx.setAD_User_ID(AD_User_ID);
			m_user = new KeyNamePair(AD_User_ID, app_user);
			m_ctx.setContext("##AD_User_Name", app_user);

			if (MUser.isSalesRep(AD_User_ID))
				m_ctx.setContext("#SalesRep_ID", AD_User_ID);
			//
			Ini.setProperty(Ini.P_UID, app_user);
			if (Ini.isPropertyBool(Ini.P_STORE_PWD))
				Ini.setProperty(Ini.P_PWD, app_pwd);

			m_connectionProfile = rs.getString(4);		//	User Based
			if (m_connectionProfile != null)
			{
				CConnection cc = CConnection.get();
				if (!cc.getConnectionProfile().equals(m_connectionProfile))
				{
					cc.setConnectionProfile(m_connectionProfile);
					Ini.setProperty(Ini.P_CONNECTION, cc.toStringLong());
					Ini.saveProperties(false);
				}
			}

			m_roles.clear();
			m_users.clear();
			do	//	read all roles
			{
				AD_User_ID = rs.getInt(1);
				m_users.add (AD_User_ID);	//	for role
				//
				int AD_Role_ID = rs.getInt(2);
				if (AD_Role_ID == 0)	//	User is a Sys Admin
					m_ctx.setContext("#SysAdmin", "Y");
				String Name = rs.getString(3);
				KeyNamePair p = new KeyNamePair(AD_Role_ID, Name);
				m_roles.add(p);
				list.add(p);
			}
			while (rs.next());

			retValue = new KeyNamePair[list.size()];
			list.toArray(retValue);
			log.fine("User=" + app_user + " - roles #" + retValue.length);
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql.toString(), ex);
			log.saveError("DBLogin", ex);
			retValue = null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		long ms = System.currentTimeMillis () - start;
		log.finest("ms=" + ms);


		return retValue;
	}	//	getRoles

	/**
	 * 	Get Roles for the user with email in client with the web store.
	 * 	If the user does not have roles and the web store has a default role,
	 * 	it will return that.
	 *	@param eMail email address
	 *	@param password password
	 *	@param W_Store_ID web store
	 *	@return roles
	 */
	private KeyNamePair[] getRolesByEmail (String eMail, String password, int W_Store_ID)
	{
		log.info("EMail=" + eMail + ", W_Store_ID=" + W_Store_ID);
		long start = System.currentTimeMillis();
		if (eMail == null || eMail.length() == 0
			|| password == null || password.length() == 0
			|| W_Store_ID == 0)
		{
			log.warning("Invalid Arguments - EMail=" + eMail
				+ ", Password=" + password + ", W_Store_ID=" + W_Store_ID);
			return null;
		}
		//	Cannot use encrypted password
		if (SecureEngine.isEncrypted(password))
		{
			log.warning("Cannot use Encrypted Password");
			return null;
		}

		KeyNamePair[] retValue = null;
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		//
		String sql = "SELECT u.AD_User_ID, r.AD_Role_ID, u.Name "
			+ "FROM AD_User u"
			+ " INNER JOIN W_Store ws ON (u.AD_Client_ID=ws.AD_Client_ID) "
			+ " INNER JOIN AD_Role r ON (ws.AD_Role_ID=r.AD_Role_ID) "
			+ "WHERE u.EMail=?"
			+ " AND (u.Password=? OR u.Password=?)"
			+ " AND ws.W_Store_ID=?"
			+ " AND (r.IsActive='Y' OR r.IsActive IS NULL)"
			+ " AND u.IsActive='Y' AND ws.IsActive='Y'"
			+ " AND u.AD_Client_ID=ws.AD_Client_ID "
			+ "ORDER BY r.Name";
		m_roles.clear();
		m_users.clear();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			pstmt.setString(1, eMail);
			pstmt.setString(2, password);
			pstmt.setString(3, SecureEngine.encrypt(password));
			pstmt.setInt(4, W_Store_ID);
			//	execute a query
			rs = pstmt.executeQuery();

			if (!rs.next())		//	no record found
			{
				log.saveError("UserPwdError", eMail, false);
				return null;
			}

			int AD_User_ID = rs.getInt(1);
			m_ctx.setAD_User_ID(AD_User_ID);
			m_user = new KeyNamePair(AD_User_ID, eMail);
			m_users.add (AD_User_ID);	//	for role
			//
			int AD_Role_ID = rs.getInt(2);
			m_ctx.setAD_Role_ID(AD_Role_ID);
			String Name = rs.getString(3);
			m_ctx.setContext("##AD_User_Name", Name);
			if (AD_Role_ID == 0)	//	User is a Sys Admin
				m_ctx.setContext("#SysAdmin", "Y");
			KeyNamePair p = new KeyNamePair(AD_Role_ID, Name);
			m_roles.add(p);
			list.add(p);

			retValue = new KeyNamePair[list.size()];
			list.toArray(retValue);
			log.fine("EMail=" + eMail + " - roles #" + retValue.length);
		}
		catch (SQLException ex) {
			log.log(Level.SEVERE, sql.toString(), ex);
			log.saveError("DBLogin", ex);
			retValue = null;
			m_ctx.setContext("##AD_User_Name", eMail);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		long ms = System.currentTimeMillis () - start;
		log.finest("ms=" + ms);
		return retValue;
	}	//	getRoles

	/**
	 * 	Get Role
	 *	@return AD_Role_ID (or -1 - SystemAdmin is 0)
	 */
	public int getAD_Role_ID()
	{
		if (m_role != null)
			return m_role.getKey();
		return -1;
	}	//	getAD_Role_ID

	/**
	 * 	Get Role
	 *	@return role or null
	 */
	public MRole getRole()
	{
		return m_roleModel;
	}		//	getRole

	/**
	 * 	Get User
	 *	@return AD_User_ID (or -1 - System is 0)
	 */
	public int getAD_User_ID()
	{
		if (m_user != null)
			return m_user.getKey();
		return -1;
	}	//	getAD_User_ID



	/**************************************************************************
	 *  Load Clients.
	 *  <p>
	 *  Sets Role info in context and loads its clients
	 *  @param  role    role information
	 *  @return list of valid client KeyNodePairs or null if in error
	 */
	public KeyNamePair[] getClients (KeyNamePair role)
	{
		if (role == null)
			throw new IllegalArgumentException("Role missing");
		m_role = role;
		//	Web Store Login
		if (m_store != null)
			return new KeyNamePair[]
			      {new KeyNamePair(m_store.getAD_Client_ID(), m_store.getName() + " Tenant")};

		//	Set User for Role
		int AD_Role_ID = role.getKey();
		for (int i = 0; i < m_roles.size(); i++)
		{
			if (AD_Role_ID == m_roles.get(i).getKey())
			{
				int AD_User_ID = m_users.get(i);
				m_ctx.setAD_User_ID(AD_User_ID);
				if (MUser.isSalesRep(AD_User_ID))
					m_ctx.setContext("#SalesRep_ID", AD_User_ID);
				m_user = new KeyNamePair(AD_User_ID, m_user.getName());
				break;
			}
		}

		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		KeyNamePair[] retValue = null;
		String sql = "SELECT DISTINCT r.UserLevel, r.ConnectionProfile, "	//	1..2
			+ " c.AD_Client_ID,c.Name "								//	3..4
			+ "FROM AD_Role r"
			+ " INNER JOIN AD_Client c ON (r.AD_Client_ID=c.AD_Client_ID) "
			+ "WHERE r.AD_Role_ID=?"		//	#1
			+ " AND r.IsActive='Y' AND c.IsActive='Y'";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//	get Role details
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, role.getKey());
			rs = pstmt.executeQuery();

			if (!rs.next())
			{
				log.log(Level.SEVERE, "No Clients for Role: " + role.toStringX());
				return null;
			}

			//  Role Info
			m_ctx.setAD_Role_ID(role.getKey());
			m_ctx.setContext("#AD_Role_Name", role.getName());
			Ini.setProperty(Ini.P_ROLE, role.getName());
			//	User Level
			m_ctx.setContext("#User_Level", rs.getString(1));  	//	Format 'SCO'

			//	ConnectionProfile
			CConnection cc = CConnection.get();
			if (m_connectionProfile == null)			//	No User Based
			{
				m_connectionProfile = rs.getString(2);	//	Role Based
				if (m_connectionProfile != null
					&& !cc.getConnectionProfile().equals(m_connectionProfile))
				{
					cc.setConnectionProfile(m_connectionProfile);
					Ini.setProperty(Ini.P_CONNECTION, cc.toStringLong());
					Ini.saveProperties(false);
				}
			}

			//  load Clients
			do
			{
				int AD_Client_ID = rs.getInt(3);
				String Name = rs.getString(4);
				KeyNamePair p = new KeyNamePair(AD_Client_ID, Name);
				list.add(p);
			}
			while (rs.next());

			retValue = new KeyNamePair[list.size()];
			list.toArray(retValue);
			log.fine("Role: " + role.toStringX() + " - clients #" + retValue.length);
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
			retValue = null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}   //  getClients

	/**
	 *  Load Organizations.
	 *  <p>
	 *  Sets Client info in context and loads its organization, the role has access to
	 *  @param  client    client information
	 *  @return list of valid Org KeyNodePairs or null if in error
	 */
	public KeyNamePair[] getOrgs (KeyNamePair client)
	{
		if (client == null)
			throw new IllegalArgumentException("Client missing");
		//	Web Store Login
		if (m_store != null)
			return new KeyNamePair[]
			      {new KeyNamePair(m_store.getAD_Org_ID(), m_store.getName() + " Org")};

		if (m_ctx.getContext("#AD_Role_ID").length() == 0)	//	could be number 0
			throw new UnsupportedOperationException("Missing Context #AD_Role_ID");

		int AD_Role_ID = m_ctx.getAD_Role_ID();
		int AD_User_ID = m_ctx.getAD_User_ID();
	//	s_log.fine("Client: " + client.toStringX() + ", AD_Role_ID=" + AD_Role_ID);

		//	get Client details for role
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		/*KeyNamePair[] retValue = null;
		//
		String sql = "SELECT o.AD_Org_ID,o.Name,o.IsSummary "	//	1..3
			+ "FROM AD_Role r, AD_Client c"
			+ " INNER JOIN AD_Org o ON (c.AD_Client_ID=o.AD_Client_ID OR o.AD_Org_ID=0) "
			+ "WHERE r.AD_Role_ID=?" 	//	#1
			+ " AND c.AD_Client_ID=?"	//	#2
			+ " AND o.IsActive='Y' AND o.IsSummary='N'"
			+ " AND (r.IsAccessAllOrgs='Y' "
				+ "OR (r.IsUseUserOrgAccess='N' AND o.AD_Org_ID IN (SELECT AD_Org_ID FROM AD_Role_OrgAccess ra "
					+ "WHERE ra.AD_Role_ID=r.AD_Role_ID AND ra.IsActive='Y')) "
				+ "OR (r.IsUseUserOrgAccess='Y' AND o.AD_Org_ID IN (SELECT AD_Org_ID FROM AD_User_OrgAccess ua "
					+ "WHERE ua.AD_User_ID=? AND ua.IsActive='Y'))"		//	#3
				+ ") "
			+ "ORDER BY o.Name";
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Role_ID);
			pstmt.setInt(2, client.getKey());
			pstmt.setInt(3, AD_User_ID);
			rs = pstmt.executeQuery();
			//  load Orgs
			while (rs.next())
			{
				 isAllOrgs = new Boolean(rs_tmp.getInt(1)==0);
			}
			rs_tmp.close();
			pstmt_tmp.close();
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql_tmp, e);
			retValue = null;
		}
		//FIN TEMPORAL
		
		//BECO
		if(!"Y".equals(m_ctx.getContext("#SysAdmin")) && !isAllOrgs){
						
			String sql = "SELECT o.AD_Org_ID, o.Name, o.IsSummary FROM AD_User us, AD_Org o " +
						 "WHERE o.AD_Org_ID = us.AD_Org_ID " +
						 "AND us.AD_User_ID = ? AND us.IsActive='Y'";
			
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, AD_User_ID);
				ResultSet rs = pstmt.executeQuery();
				//  load Orgs
				while (rs.next())
				{
					if (m_roleModel == null
						|| m_roleModel.getAD_Role_ID() != AD_Role_ID
						|| m_roleModel.getAD_User_ID() != AD_User_ID)
						m_roleModel = MRole.get(m_ctx, AD_Role_ID, AD_User_ID, false);
					getOrgsAddSummary (list, AD_Org_ID, Name, m_roleModel);
				}
				rs.close();
				pstmt.close();
				pstmt = null;
				
				retValue = new KeyNamePair[list.size()];
				list.toArray(retValue);
				log.fine("Client: " + client.toStringX() 
					+ ", AD_Role_ID=" + AD_Role_ID
					+ ", AD_User_ID=" + AD_User_ID
					+ " - orgs #" + retValue.length);
			}
			catch (SQLException ex)
			{
				log.log(Level.SEVERE, sql, ex);
				retValue = null;
			}
			
			try
			{
				if (pstmt != null)
					pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				pstmt = null;
			}
		
		}
		else{
			//
			String sql = "SELECT o.AD_Org_ID,o.Name,o.IsSummary "	//	1..3
				+ "FROM AD_Role r, AD_Client c"
				+ " INNER JOIN AD_Org o ON (c.AD_Client_ID=o.AD_Client_ID OR o.AD_Org_ID=0) "
				+ "WHERE r.AD_Role_ID=?" 	//	#1
				+ " AND c.AD_Client_ID=?"	//	#2
				+ " AND o.IsActive='Y' AND o.IsSummary='N'"
				+ " AND (r.IsAccessAllOrgs='Y' "
					+ "OR (r.IsUseUserOrgAccess='N' AND o.AD_Org_ID IN (SELECT AD_Org_ID FROM AD_Role_OrgAccess ra "
						+ "WHERE ra.AD_Role_ID=r.AD_Role_ID AND ra.IsActive='Y')) "
					+ "OR (r.IsUseUserOrgAccess='Y' AND o.AD_Org_ID IN (SELECT AD_Org_ID FROM AD_User_OrgAccess ua "
						+ "WHERE ua.AD_User_ID=? AND ua.IsActive='Y'))"		//	#3
					+ ") "
				+ "ORDER BY o.Name";
			//
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, AD_Role_ID);
				pstmt.setInt(2, client.getKey());
				pstmt.setInt(3, AD_User_ID);
				ResultSet rs = pstmt.executeQuery();
				//  load Orgs
				while (rs.next())
				{
					int AD_Org_ID = rs.getInt(1);
					String Name = rs.getString(2);
					boolean summary = "Y".equals(rs.getString(3));
					if (summary)
					{
						if (m_roleModel == null 
							|| m_roleModel.getAD_Role_ID() != AD_Role_ID
							|| m_roleModel.getAD_User_ID() != AD_User_ID)
							m_roleModel = MRole.get(m_ctx, AD_Role_ID, AD_User_ID, false);
						getOrgsAddSummary (list, AD_Org_ID, Name, m_roleModel);
					}
					else
					{
						KeyNamePair p = new KeyNamePair(AD_Org_ID, Name);
						if (!list.contains(p))
							list.add(p);
					}
				}
				rs.close();
				pstmt.close();
				pstmt = null;
				//
				
				
				retValue = new KeyNamePair[list.size()];
				list.toArray(retValue);
				log.fine("Client: " + client.toStringX() 
					+ ", AD_Role_ID=" + AD_Role_ID
					+ ", AD_User_ID=" + AD_User_ID
					+ " - orgs #" + retValue.length);
			}
			retValue = new KeyNamePair[list.size()];
			list.toArray(retValue);
			log.fine("Client: " + client.toStringX()
				+ ", AD_Role_ID=" + AD_Role_ID
				+ ", AD_User_ID=" + AD_User_ID
				+ " - orgs #" + retValue.length);
		}
		catch (SQLException ex) {
			log.log(Level.SEVERE, sql, ex);
			retValue = null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}*/
		KeyNamePair[] retValue = null;		
		
		
		//TEMPORAL (QUITAR UNA VEZ TODOS LOS USUARIOS TENGAN SU AD_ORG ASIGNADO)
		String sql_tmp = "SELECT o.AD_Org_ID FROM AD_User us, AD_Org o " +
						 "WHERE o.AD_Org_ID = us.AD_Org_ID " +
						 "AND us.AD_User_ID = ? AND us.IsActive='Y'";
		
		PreparedStatement pstmt_tmp = null;
		boolean isAllOrgs = false;
		try
		{
			pstmt_tmp = DB.prepareStatement(sql_tmp, null);
			pstmt_tmp.setInt(1, AD_User_ID);
			ResultSet rs_tmp = pstmt_tmp.executeQuery();
			if (rs_tmp.next())
			{
				 isAllOrgs = new Boolean(rs_tmp.getInt(1)==0);
			}
			rs_tmp.close();
			pstmt_tmp.close();
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql_tmp, e);
			retValue = null;
		}
		//FIN TEMPORAL
		
		//BECO
		if(!"Y".equals(m_ctx.getContext("#SysAdmin")) && !isAllOrgs){
						
			String sql = "SELECT o.AD_Org_ID, o.Name, o.IsSummary FROM AD_User us, AD_Org o " +
						 "WHERE o.AD_Org_ID = us.AD_Org_ID " +
						 "AND us.AD_User_ID = ? AND us.IsActive='Y'";
			
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, AD_User_ID);
				ResultSet rs = pstmt.executeQuery();
				//  load Orgs
				while (rs.next())
				{
					int AD_Org_ID = rs.getInt(1);
					String Name = rs.getString(2);
					boolean summary = "Y".equals(rs.getString(3));
					if (summary)
					{
						if (m_roleModel == null 
							|| m_roleModel.getAD_Role_ID() != AD_Role_ID
							|| m_roleModel.getAD_User_ID() != AD_User_ID)
							m_roleModel = MRole.get(m_ctx, AD_Role_ID, AD_User_ID, false);
						getOrgsAddSummary (list, AD_Org_ID, Name, m_roleModel);
					}
					else
					{
						KeyNamePair p = new KeyNamePair(AD_Org_ID, Name);
						if (!list.contains(p))
							list.add(p);
					}
				}
				rs.close();
				pstmt.close();
				pstmt = null;
				
				retValue = new KeyNamePair[list.size()];
				list.toArray(retValue);
				log.fine("Client: " + client.toStringX() 
					+ ", AD_Role_ID=" + AD_Role_ID
					+ ", AD_User_ID=" + AD_User_ID
					+ " - orgs #" + retValue.length);
			}
			catch (SQLException ex)
			{
				log.log(Level.SEVERE, sql, ex);
				retValue = null;
			}
			
			try
			{
				if (pstmt != null)
					pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				pstmt = null;
			}
		
		}
		else{
			//
			String sql = "SELECT o.AD_Org_ID,o.Name,o.IsSummary "	//	1..3
				+ "FROM AD_Role r, AD_Client c"
				+ " INNER JOIN AD_Org o ON (c.AD_Client_ID=o.AD_Client_ID OR o.AD_Org_ID=0) "
				+ "WHERE r.AD_Role_ID=?" 	//	#1
				+ " AND c.AD_Client_ID=?"	//	#2
				+ " AND o.IsActive='Y' AND o.IsSummary='N'"
				+ " AND (r.IsAccessAllOrgs='Y' "
					+ "OR (r.IsUseUserOrgAccess='N' AND o.AD_Org_ID IN (SELECT AD_Org_ID FROM AD_Role_OrgAccess ra "
						+ "WHERE ra.AD_Role_ID=r.AD_Role_ID AND ra.IsActive='Y')) "
					+ "OR (r.IsUseUserOrgAccess='Y' AND o.AD_Org_ID IN (SELECT AD_Org_ID FROM AD_User_OrgAccess ua "
						+ "WHERE ua.AD_User_ID=? AND ua.IsActive='Y'))"		//	#3
					+ ") "
				+ "ORDER BY o.Name";
			//
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, AD_Role_ID);
				pstmt.setInt(2, client.getKey());
				pstmt.setInt(3, AD_User_ID);
				ResultSet rs = pstmt.executeQuery();
				//  load Orgs
				while (rs.next())
				{
					int AD_Org_ID = rs.getInt(1);
					String Name = rs.getString(2);
					boolean summary = "Y".equals(rs.getString(3));
					if (summary)
					{
						if (m_roleModel == null 
							|| m_roleModel.getAD_Role_ID() != AD_Role_ID
							|| m_roleModel.getAD_User_ID() != AD_User_ID)
							m_roleModel = MRole.get(m_ctx, AD_Role_ID, AD_User_ID, false);
						getOrgsAddSummary (list, AD_Org_ID, Name, m_roleModel);
					}
					else
					{
						KeyNamePair p = new KeyNamePair(AD_Org_ID, Name);
						if (!list.contains(p))
							list.add(p);
					}
				}
				rs.close();
				pstmt.close();
				pstmt = null;
				//
				
				
				retValue = new KeyNamePair[list.size()];
				list.toArray(retValue);
				log.fine("Client: " + client.toStringX() 
					+ ", AD_Role_ID=" + AD_Role_ID
					+ ", AD_User_ID=" + AD_User_ID
					+ " - orgs #" + retValue.length);
			}
			catch (SQLException ex)
			{
				log.log(Level.SEVERE, sql, ex);
				retValue = null;
			}
			
			try
			{
				if (pstmt != null)
					pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				pstmt = null;
			}
		}
		//	No Orgs
		if (retValue == null || retValue.length == 0)
		{
			log.log(Level.WARNING, "No Org for Client: " + client.toStringX()
				+ ", AD_Role_ID=" + AD_Role_ID
				+ ", AD_User_ID=" + AD_User_ID);
			return null;
		}

		//  Client Info
		m_ctx.setContext("#AD_Client_ID", client.getKey());
		m_ctx.setContext("#AD_Client_Name", client.getName());
		Ini.setProperty(Ini.P_CLIENT, client.getName());
		return retValue;
	}   //  getOrgs

	/**
	 * 	Get Orgs - Add Summary Org
	 *	@param list list
	 *	@param Summary_Org_ID summary org
	 *	@param Summary_Name name
	 *	@param role role
	 *	@see org.compiere.model.MRole#loadOrgAccessAdd
	 */
	private void getOrgsAddSummary (ArrayList<KeyNamePair> list, int Summary_Org_ID,
		String Summary_Name, MRole role)
	{
		if (role == null)
		{
			log.warning("Summary Org=" + Summary_Name + "(" + Summary_Org_ID + ") - No Role");
			return;
		}
		//	Do we look for trees?
		if (role.getAD_Tree_Org_ID() == 0)
		{
			log.config("Summary Org=" + Summary_Name + "(" + Summary_Org_ID + ") - No Org Tree: " + role);
			return;
		}
		//	Summary Org - Get Dependents
		MTree tree = MTree.get(m_ctx, role.getAD_Tree_Org_ID(), null);
		String sql =  "SELECT AD_Client_ID, AD_Org_ID, Name, IsSummary FROM AD_Org "
			+ "WHERE IsActive='Y' AND AD_Org_ID IN (SELECT Node_ID FROM "
			+ tree.getNodeTableName()
			+ " WHERE AD_Tree_ID=? AND Parent_ID=? AND IsActive='Y') "
			+ "ORDER BY Name";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, tree.getAD_Tree_ID());
			pstmt.setInt (2, Summary_Org_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
			//	int AD_Client_ID = rs.getInt(1);
				int AD_Org_ID = rs.getInt(2);
				String Name = rs.getString(3);
				boolean summary = "Y".equals(rs.getString(4));
				//
				if (summary)
					getOrgsAddSummary (list, AD_Org_ID, Name, role);
				else
				{
					KeyNamePair p = new KeyNamePair(AD_Org_ID, Name);
					if (!list.contains(p))
						list.add(p);
				}
			}
		}
		catch (Exception e) {
			log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	getOrgAddSummary


	/**
	 *  Load Warehouses
	 * @param org organization
	 * @return Array of Warehouse Info
	 */
	public KeyNamePair[] getWarehouses (KeyNamePair org)
	{
		if (org == null)
			throw new IllegalArgumentException("Org missing");
		m_org = org;
		if (m_store != null)
			return new KeyNamePair[] {new KeyNamePair(m_store.getM_Warehouse_ID(), m_store.getName() + " Warehouse")};

	//	s_log.info("loadWarehouses - Org: " + org.toStringX());

		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		KeyNamePair[] retValue = null;
		String sql = "SELECT M_Warehouse_ID, Name FROM M_Warehouse "
			+ "WHERE AD_Org_ID=? AND IsActive='Y' "
			+ "ORDER BY Name";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, org.getKey());
			rs = pstmt.executeQuery();

			if (!rs.next())
			{
				log.info("No Warehouses for Org: " + org.toStringX());
				return null;
			}

			//  load Warehouses
			do
			{
				int AD_Warehouse_ID = rs.getInt(1);
				String Name = rs.getString(2);
				KeyNamePair p = new KeyNamePair(AD_Warehouse_ID, Name);
				list.add(p);
			}
			while (rs.next());

			retValue = new KeyNamePair[list.size()];
			list.toArray(retValue);
			log.fine("Org: " + org.toStringX()
				+ " - warehouses #" + retValue.length);
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
			retValue = null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}   //  getWarehouses

	/**
	 * 	Create Swing Session
	 *	@return local swing session
	 */
	public MSession createSwingSession()
	{
		String Remote_Addr = null;
		String Remote_Host = null;
		try
		{
			InetAddress lh = InetAddress.getLocalHost();
			Remote_Addr = lh.getHostAddress();
			Remote_Host = lh.getHostName();
		}
		catch (UnknownHostException e)
		{
			log.log(Level.SEVERE, "No Local Host", e);
		}

		MSession session = MSession.get(m_ctx, X_AD_Session.SESSIONTYPE_SwingUI, true,
			Remote_Addr, Remote_Host, null);
		return session;
	}	//	createSwingSession

	/**
	 * 	Validate Login.
	 * 	Creates session and calls ModelValidationEngine
	 *	@param org log-in org
	 *	@return error message
	 */
	public String validateLogin (KeyNamePair org)
	{
		String info = m_user + ",R:" + m_role.toStringX()
			+ ",O=" + org.toStringX();
		int AD_Client_ID = m_ctx.getAD_Client_ID();
		int AD_Org_ID = org.getKey();
		int AD_Role_ID = m_ctx.getAD_Role_ID();
		int AD_User_ID = m_ctx.getAD_User_ID();
		//
		MSession session = MSession.get(m_ctx);
		if (session == null)
			return "No Session";
		if (AD_Client_ID != session.getAD_Client_ID())
			session.setAD_Client_ID(AD_Client_ID);
		if (AD_Org_ID != session.getAD_Org_ID())
			session.setAD_Org_ID(AD_Org_ID);
		if (AD_Role_ID != session.getAD_Role_ID())
			session.setAD_Role_ID(AD_Role_ID);
		session.save();
		//
		String error = ModelValidationEngine.get().loginComplete(AD_Client_ID, AD_Org_ID, AD_Role_ID, AD_User_ID);
		if (error != null && error.length() > 0)
		{
			log.severe("Refused: " + info + ": " + error);
			session.setDescription(error);
			session.save();
			return error;
		}
		//	Log
		log.info(info);
		return null;
	}	//	validateLogin

	/**
	 *	Load Preferences into Context for selected client.
	 *  <p>
	 *  Sets Org info in context and loads relevant field from
	 *	- AD_Client/Info,
	 *  - C_AcctSchema,
	 *  - C_AcctSchema_Elements
	 *	- AD_Preference
	 *  <p>
	 *  Assumes that the context is set for #AD_Client_ID, ##AD_User_ID, #AD_Role_ID
	 *
	 *  @param  org    org information
	 *  @param  warehouse   optional warehouse information
	 *  @param  timestamp   optional date
	 *  @param  printerName optional printer info
	 *  @return AD_Message of error (NoValidAcctInfo) or ""
	 */
	public String loadPreferences (KeyNamePair org,
		KeyNamePair warehouse, java.sql.Timestamp timestamp, String printerName)
	{
		m_org = org;
		log.info("Org: " + m_org.toStringX());

		if (m_ctx == null || org == null)
			throw new IllegalArgumentException("Required parameter missing");
		if (m_ctx.getContext("#AD_Client_ID").length() == 0)
			throw new UnsupportedOperationException("Missing Context #AD_Client_ID");
		int AD_Client_ID = m_ctx.getAD_Client_ID();
		if (m_ctx.getContext("##AD_User_ID").length() == 0)
			throw new UnsupportedOperationException("Missing Context ##AD_User_ID");
		int AD_User_ID = m_ctx.getAD_User_ID();
		if (m_ctx.getContext("#AD_Role_ID").length() == 0)
			throw new UnsupportedOperationException("Missing Context #AD_Role_ID");
		int AD_Role_ID =  m_ctx.getAD_Role_ID();

		//  Org Info - assumes that it is valid
		m_ctx.setAD_Org_ID(org.getKey());
		m_ctx.setContext("#AD_Org_Name", org.getName());
		Ini.setProperty(Ini.P_ORG, org.getName());

		//  Warehouse Info
		if (warehouse != null)
		{
			m_ctx.setContext("#M_Warehouse_ID", warehouse.getKey());
			Ini.setProperty(Ini.P_WAREHOUSE, warehouse.getName());
		}

		//	Date (default today)
		long today = System.currentTimeMillis();
		if (timestamp != null)
			today = timestamp.getTime();
		m_ctx.setContext("#Date", String.valueOf(today));

		//	Load User/Role Info
		MUser user = MUser.get(m_ctx, getAD_User_ID());
		MUserPreference preference = user.getPreference();
		if (m_roleModel == null
			|| m_roleModel.getAD_Role_ID() != AD_Role_ID
			|| m_roleModel.getAD_User_ID() != AD_User_ID)
			m_roleModel = MRole.get(m_ctx, AD_Role_ID, AD_User_ID, true);

		//	Optional Printer
		if (printerName == null)
			printerName = "";
		if (printerName.length() == 0 && preference.getPrinterName() != null)
			printerName = preference.getPrinterName();
		m_ctx.setPrinterName(printerName);
		if (preference.getPrinterName() == null && printerName.length() > 0)
			preference.setPrinterName(printerName);

		//	Other
		m_ctx.setAutoCommit (preference.isAutoCommit());
		m_ctx.setAutoNew (Ini.isPropertyBool(Ini.P_A_NEW));
		if (m_roleModel.isShowAcct())
		{
			m_ctx.setContext( "#CanShowAcct", "Y" );
			m_ctx.setContext("#ShowAcct", preference.isShowAcct());
		}
		else
			m_ctx.setContext("#ShowAcct", "N");
		
		if(m_roleModel.isEnableCreateUpdateBP())
			m_ctx.setContext("#EnableCreateUpdateBP", "Y");
		else
			m_ctx.setContext("#EnableCreateUpdateBP", "N");
		
		m_ctx.setContext("#ShowTrl", preference.isShowTrl());
		m_ctx.setContext("#ShowAdvanced", preference.isShowAdvanced());
		if(preference.getUITheme() != null)
			m_ctx.setContext("#UITheme", preference.getUITheme());

		//	MultiLingual supported at client level
		if(MClient.get(m_ctx).isMultiLingualDocument())
			m_ctx.setContext("#MultiLingual", "Y");
		else
			m_ctx.setContext("#MultiLingual", "N");

		m_ctx.setPrintPreview(Ini.isPropertyBool(Ini.P_PRINTPREVIEW));

		String retValue = "";

		//	Other Settings
		m_ctx.setContext("#YYYY", "Y");

		//	AccountSchema Info (first)
		String sql = "SELECT a.C_AcctSchema_ID, a.C_Currency_ID, a.HasAlias, c.ISO_Code, c.StdPrecision "
			+ "FROM C_AcctSchema a"
			+ " INNER JOIN AD_ClientInfo ci ON (a.C_AcctSchema_ID=ci.C_AcctSchema1_ID)"
			+ " INNER JOIN C_Currency c ON (a.C_Currency_ID=c.C_Currency_ID) "
			+ "WHERE ci.AD_Client_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int C_AcctSchema_ID = 0;
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();

			if (!rs.next())
			{
				//  No Warning for System
				if (AD_Role_ID != 0)
					retValue = "NoValidAcctInfo";
			}
			else
			{
				//	Accounting Info
				C_AcctSchema_ID = rs.getInt(1);
				m_ctx.setContext("$C_AcctSchema_ID", C_AcctSchema_ID);
				m_ctx.setContext("$C_Currency_ID", rs.getInt(2));
				m_ctx.setContext("$HasAlias", rs.getString(3));
				m_ctx.setContext("$CurrencyISO", rs.getString(4));
				m_ctx.setStdPrecision(rs.getInt(5));
			}
			
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			
			//	Accounting Elements
			sql = "SELECT ElementType "
				+ "FROM C_AcctSchema_Element "
				+ "WHERE C_AcctSchema_ID=?"
				+ " AND IsActive='Y'";
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_AcctSchema_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
				m_ctx.setContext("$Element_" + rs.getString("ElementType"), "Y");

			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			
			//	Last login date
			sql = "SELECT CREATED "
				+ "FROM AD_Session "
				+ "WHERE AD_Session_ID=?";
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, preference.getAD_Session_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				m_ctx.setContext(CtxConstants.LAST_LOGIN, rs.getTimestamp(1).getTime()+"");
			
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			
			
			//	This reads all relevant window neutral defaults
			//	overwriting superseeded ones.  Window specific is read in Maintain
			sql = "SELECT Attribute, Value, AD_Window_ID "
				+ "FROM AD_Preference "
				+ "WHERE AD_Client_ID IN (0, @#AD_Client_ID@)"
				+ " AND AD_Org_ID IN (0, @#AD_Org_ID@)"
				+ " AND (AD_User_ID IS NULL OR AD_User_ID=0 OR AD_User_ID=@##AD_User_ID@)"
				+ " AND IsActive='Y' "
				+ "ORDER BY Attribute, AD_Client_ID, AD_User_ID DESC, AD_Org_ID";
				//	the last one overwrites - System - Client - User - Org - Window
			
			Env.QueryParams queryParams = Env
			.parseContextUsingBindVariables(m_ctx,
					0, sql, false,
					false);
			sql = queryParams.sql;
			if (sql.length() == 0)
				log.log(Level.SEVERE, "Missing Environment");
			else
			{
				Object[][] results = QueryUtil.executeQuery(
						(Trx)null, sql, 
						queryParams.params.toArray());
				
				for (Object[] row : results) {
					String at = "";
					int AD_Window_ID = 0;
					if (row[2] == null) {
						at = "P|" + (String) row[0];
					} else {
						AD_Window_ID = ((BigDecimal) row[2]).intValue();
						at = "P" + AD_Window_ID + "|" + (String) row[0];
					}
					String va = (String) row[1];
					m_ctx.setContext(at, va);
				}
			}


			FutureTask<Map<String, String>> defaultsTask = s_roleDefaults.get(null, m_roleModel.getAD_Role_ID());
			if( defaultsTask == null )
			{
				Callable<Map<String, String>> callable = new Callable<Map<String, String>>() {

					@Override
					public Map<String, String> call() throws Exception {
						String sql;
						PreparedStatement pstmt = null;
						ResultSet rs = null;
						
						HashMap<String, String> defaults = new HashMap<String, String>();
						
						//	Default Values
						log.info("Default Values ...");
						sql = "SELECT t.TableName, c.ColumnName "
							+ "FROM AD_Column c "
							+ " INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID) "
							+ "WHERE c.IsKey='Y' AND t.IsActive='Y'"
							+ " AND EXISTS (SELECT * FROM AD_Column cc "
							+ " WHERE ColumnName = 'IsDefault' AND t.AD_Table_ID=cc.AD_Table_ID AND cc.IsActive='Y')";
						try {
							pstmt = DB.prepareStatement(sql, (Trx) null);
							rs = pstmt.executeQuery();
							while (rs.next())
							{
								String columnName = rs.getString(2);
								String value = loadDefault (rs.getString(1), columnName);
								if( value != null )
									defaults.put(columnName, value);
							}
						}
						finally {
							DB.closeResultSet(rs);
							DB.closeStatement(pstmt);
						}
						return defaults;
					}
				}; 
				FutureTask<Map<String, String>> newTask = new FutureTask<Map<String, String>>(callable);
				
				defaultsTask = s_roleDefaults.putIfAbsent(m_roleModel.getAD_Role_ID(), newTask);
				if( defaultsTask == null )
				{
					defaultsTask = newTask;
					defaultsTask.run();
				}
			}

			Map<String, String> defaults;
			try {
				defaults = defaultsTask.get();
				for( Map.Entry<String, String> entry : defaults.entrySet() )
				{
					m_ctx.setContext("#" + entry.getKey(), entry.getValue());
				}
			} catch (InterruptedException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} catch (ExecutionException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//
		Ini.saveProperties(Ini.isClient());
		//	Country
		m_ctx.setContext("#C_Country_ID", MCountry.getDefault(m_ctx).getC_Country_ID());

		if (warehouse != null)
		{
			MWarehouse whouse = new MWarehouse (m_ctx, warehouse.getKey(), null);
			m_ctx.setContext("#M_Locator_ID", whouse.getDefaultM_Locator_ID());
		}
		
		/*
		 * Hecho por CENTROBECO C.A.
		 * TODO : BECO Realizar la carga de las validaciones por ID
		 */
		cargaID();
		
		return retValue;
	}	//	loadPreferences

	/*
	 * Hecho por Jorge E. Pires G. / GMartinelli
	 * CENTROBECO C.A.
	 * Polimorfismo para carga de ID de la tabla XX_VSI_KEYNAMEINFO
	 * que se usan para las validaciones por ID para ser llamado desde
	 * los Schedulers de Compiere
	 */
	private void cargaID() {
		Login.cargaID(m_ctx);
	}

		/*
	 * Hecho por Jorge E. Pires G. 
	 * CENTROBECO C.A.
	 * Carga los ID de la tabla XX_VSI_KEYNAMEINFO
	 * que se usan para las validaciones por ID
	 */
	public static void cargaID(Ctx xx_ctx) {
		String sql = "SELECT * FROM XX_VSI_KEYNAMEINFO WHERE AD_CLIENT_ID = "+ xx_ctx.getContext("#AD_Client_ID");
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();

			if (rs.next()){
				for (int i = 1; i <= rsm.getColumnCount(); i++) {
					String nombre = rsm.getColumnName(i);
					String valor = null;
					if (!nombre.toUpperCase().equals("ISACTIVE") && 
						!nombre.toUpperCase().equals("CREATED") && 
						!nombre.toUpperCase().equals("CREATEDBY") && 
						!nombre.toUpperCase().equals("UPDATED") && 
						!nombre.toUpperCase().equals("UPDATEDBY") && 
						!nombre.toUpperCase().equals("UPDATEDBY") && 
						!nombre.toUpperCase().equals("AD_ORG_ID") && 
						!nombre.toUpperCase().equals("AD_CLIENT_ID") && 
						!nombre.toUpperCase().equals("XX_VSI_KEYNAMEINFO_ID"))
					{
						valor = rs.getString(nombre);
						//System.out.println("#"+nombre+": "+ valor);
						xx_ctx.setContext("#"+nombre, valor);
					}
				}				
			}		
			pstmt.close();
			rs.close();
		}catch (Exception e) {
			e.printStackTrace();
			log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}	
	}



	/**
	 *	Load Default Value for Table into Context.
	 *  @param TableName table name
	 *  @param ColumnName column name
	 */
	private String loadDefault (String TableName, String ColumnName)
	{
		if (TableName.startsWith("AD_Window")
			|| TableName.startsWith("AD_PrintFormat")
			|| TableName.equals("AD_Tree")
			|| TableName.startsWith("AD_Workflow") )
			return null;
		String value = null;
		//
		String sql = "SELECT " + ColumnName + " FROM " + TableName	//	most specific first
			+ " WHERE IsDefault='Y' AND IsActive='Y' ORDER BY AD_Client_ID DESC, AD_Org_ID DESC";
		sql = m_roleModel.addAccessSQL(sql,
			TableName, MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			if (rs.next())
				value = rs.getString(1);
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, TableName + " (" + sql + ")", e);
			return null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	Set Context Value
		if (value != null && value.length() != 0)
		{
			if (TableName.equals("C_DocType"))
				return null;	//	ignore general Doc Type settings
			else
				return value;
		}
		return null;
	}	//	loadDefault

	/**
	 * 	Batch Login using Ini values
	 * 	<code>
		Compiere.startup(true);
		Ini.setProperty(Ini.P_UID,"SuperUser");
		Ini.setProperty(Ini.P_PWD,"System");
		Ini.setProperty(Ini.P_ROLE,"GardenAdmin");
		Ini.setProperty(Ini.P_CLIENT, "Garden World");
		Ini.setProperty(Ini.P_ORG,"HQ");
		Ini.setProperty(Ini.P_WAREHOUSE,"HQ");
		Ini.setProperty(Ini.P_LANGUAGE,"English");
		Login login = new Login(Env.getCtx());
		login.batchLogin();
	 * 	</code>
	 * 	@param loginDate optional login date
	 * 	@param printerName optional printer name
	 * 	@return true if logged in using Ini values
	 */
	public boolean batchLogin(java.sql.Timestamp loginDate, String printerName)
	{
		//	User Login
		String uid = Ini.getProperty(Ini.P_UID);
		String pwd = Ini.getProperty(Ini.P_PWD);
		KeyNamePair[] roles = getRoles (uid, pwd);
		if (roles == null || roles.length == 0)
		{
			log.severe("User/Password invalid: " + uid);
			return false;
		}
		log.info("User: " + uid);

		//	Role
		String role = Ini.getProperty(Ini.P_ROLE);
		KeyNamePair rolePP = null;
		for (KeyNamePair pair : roles) {
			if (pair.getName().equalsIgnoreCase(role))
			{
				rolePP = pair;
				break;
			}
		}
		if (rolePP == null)
		{
			log.severe("Role invalid: " + role);
			for (KeyNamePair element : roles)
				log.info("Option: " + element);
			return false;
		}
		log.info("Role: " + role);

		//	Clients
		String client = Ini.getProperty(Ini.P_CLIENT);
		KeyNamePair[] clients = getClients(rolePP);
		if (clients == null || clients.length == 0)
		{
			log.severe("No Clients for Role: " + role);
			return false;
		}
		KeyNamePair clientPP = null;
		for (KeyNamePair pair : clients) {
			if (pair.getName().equalsIgnoreCase(client))
			{
				clientPP = pair;
				break;
			}
		}
		if (clientPP == null)
		{
			log.severe("Client invalid: " + client);
			for (KeyNamePair element : clients)
				log.info("Option: " + element);
			return false;
		}

		//	Organization
		String org = Ini.getProperty(Ini.P_ORG);
		KeyNamePair[] orgs = getOrgs(clientPP);
		if (orgs == null || orgs.length == 0)
		{
			log.severe("No Orgs for Client: " + client);
			return false;
		}
		KeyNamePair orgPP = null;
		for (KeyNamePair pair : orgs) {
			if (pair.getName().equalsIgnoreCase(org))
			{
				orgPP = pair;
				break;
			}
		}
		if (orgPP == null)
		{
			log.severe("Org invalid: " + org);
			for (KeyNamePair element : orgs)
				log.info("Option: " + element);
			return false;
		}

		MSession session = createSwingSession();
		session.setSessionType(X_AD_Session.SESSIONTYPE_API);
		session.save();

		String error = validateLogin(orgPP);
		if (error != null && error.length() > 0)
			return false;

		//	Warehouse
		String wh = Ini.getProperty(Ini.P_WAREHOUSE);
		KeyNamePair[] whs = getWarehouses(orgPP);
		if (whs == null || whs.length == 0)
		{
			log.severe("No Warehouses for Org: " + org);
			return false;
		}
		KeyNamePair whPP = null;
		for (KeyNamePair pair : whs) {
			if (pair.getName().equalsIgnoreCase(wh))
			{
				whPP = pair;
				break;
			}
		}
		if (whPP == null)
		{
			log.severe("Warehouse invalid: " + wh);
			for (KeyNamePair element : whs)
				log.info("Option: " + element);
			return false;
		}

		//	Language
		String langName = Ini.getProperty(Ini.P_LANGUAGE);
		Locale.setDefault(setLanguage (langName));

		//	Preferences
		if (loginDate == null)
			loginDate = new java.sql.Timestamp(System.currentTimeMillis());
		loadPreferences(orgPP, whPP, loginDate, printerName);
		//
		log.info("complete");
		return true;
	}	//	batchLogin

	/**
	 * 	Set Language
	 *	@param langInfo language (en) or locale (en-US) or display name (English)
	 *	@return locale (set as default for client)
	 */
	public Locale setLanguage(String langInfo)
	{
		Language language = Language.getLanguage(langInfo);
		Language.setLoginLanguage(language);
		//we get locale here before language verification. In case when laguange is not installed, we can still get the current locale
		Locale loc = language.getLocale();

		language = Env.verifyLanguage (language);

		m_ctx.setContext(Env.LANGUAGE, language.getAD_Language());
	//	Locale.setDefault(loc);
		Msg.getMsg(m_ctx, "0");
		return loc;
	}	//	setLanguage

	/**
	 * 	Batch Login with system date
	 *	@return true if logged in
	 */
	public boolean batchLogin()
	{
		return batchLogin(new java.sql.Timestamp (System.currentTimeMillis()), "");
	}	//	batchLogin

	/**
	 * 	Get SSO Principal
	 *	@return principal
	 */
	public Principal getPrincipal()
	{
		return null;
	}	//	getPrincipal

	/**
	public static void main(String[] args)
	{
		Compiere.startup(true);
		Login l = new Login(Env.getCtx());
		KeyNamePair[] roles = l.getRoles("jjanke@compiere.org", "aaa", 1000000);
		KeyNamePair[] clients = l.getClients(roles[0]);
		KeyNamePair[] orgs = l.getOrgs(clients[0]);
	}
	**/

}	//	Login
