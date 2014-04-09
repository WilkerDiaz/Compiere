/******************************************************************************
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
import java.rmi.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.db.*;
import org.compiere.interfaces.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;

import com.compiere.client.*;

/**
 *	Initial Setup Model.
 * 	Maintains own context and manages own transaction
 *
 *	@author Jorg Janke
 *	@version $Id: MSetup.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public final class MSetup
{
    /** Logger for class MSetup */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MSetup.class);
	/**
	 * 	Create new Client/Tenant
	 *	@param bp optional bp
	 *	@param clientName optional client
	 *	@return info
	 *	@throws Exception if problems
	 */
	static String createNewClient(MBPartner bp, String clientName) throws Exception
	{
		int C_Country_ID = 0;
		int C_Currency_ID = 0;
		int C_Region_ID = 0;
		String City = null;

		//	Get Info from BP
		if (bp != null)
		{
			if (Util.isEmpty(clientName))
				clientName = bp.getName();
			MBPartnerLocation[] locs = bp.getLocations(false);
			if (locs != null && locs.length > 0)
			{
				MBPartnerLocation loc = locs[0];	//	first
				MLocation addr = loc.getLocation(false);
				if (addr != null)
				{
					C_Country_ID = addr.getC_Country_ID();
					C_Region_ID = addr.getC_Region_ID();
					City = addr.getCity();
				}
			}
		}
		if (C_Country_ID != 0)
		{
			MCountry cc = MCountry.get(bp.getCtx(), C_Country_ID);
			C_Currency_ID = cc.getC_Currency_ID();
		}


		MSetup ms = new MSetup();
		boolean ok = ms.createClient(clientName, null, null, null);
		String info = ms.getInfo();
		if (!ok)
			throw new CompiereSystemException(info);
		ok = ms.createAccounting(C_Country_ID, C_Currency_ID, true, true, false, false, false, null);
		info += ms.getInfo();
		if (!ok)
			throw new CompiereSystemException(info);

		ok = ms.createEntities(City, C_Region_ID);
		info += ms.getInfo();
		if (!ok)
			throw new CompiereSystemException(info);

		//	Step 4 - Create Print Documents
		PrintUtil.setupPrintForm(ms.getAD_Client_ID());

		resetServer();	
		return info;
	}	//	createNewClient

	/**
	 * 	Reset Application Server/Processors and Cache
	 */
	public static void resetServer()
	{
		//	Reset Cache
		CacheMgt.get().reset(null, 0);
		//	Reset Apps Server
		if (CConnection.get().isAppsServerOK(true))
		{
			try
			{
				Server server = CConnection.get().getServer();
				if (server != null)
				{
					server.cacheReset(null, 0);
					server.restartProcesses();
				}
			}
			catch (RemoteException e)
			{
			}
			catch (Exception e)
			{
			}
		}
	}	//	resetServer


	/**************************************************************************
	 *  Constructor
	 */
	public MSetup()
	{
		m_ctx = new Ctx();
		m_lang = Env.getAD_Language(m_ctx);
		m_WindowNo = 1010;
		m_trx = Trx.get("Setup");
	}   //  MSetup

	private Trx				m_trx;
	private Ctx				m_ctx;
	private String          m_lang;
	private int             m_WindowNo;
	private StringBuffer    m_info;
	//
	private String          m_clientName;
	private int				m_C_Country_ID;
	private int				m_C_Currency_ID;
	//
	private String          m_stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
	private String          m_stdValues;
	private String          m_stdValuesOrg;
	//
	private NaturalAccountMap m_nap = null;
	//
	private MClient			m_client;
	private MOrg			m_org;
	private MAcctSchema		m_as;
	//
	private int     		AD_User_ID;
	private String  		AD_User_Name;
	private int     		AD_User_U_ID;
	private String  		AD_User_U_Name;
	private MCalendar		m_calendar;
	private int     		m_AD_Tree_Account_ID;
	private int     		C_Cycle_ID;
	//
	private boolean         m_hasProject = false;
	private boolean         m_hasMCampaign = false;
	private boolean         m_hasSRegion = false;
	/** Account Creation OK		*/
	private boolean m_accountsOK = false;

	/**
	 *  Create Client Info.
	 *  - Client, Trees, Org, Role, User, User_Role
	 *  @param clientName client name
	 *  @param orgName org name
	 *  @param userClient user id client
	 *  @param userOrg user id org
	 *  @return true if created
	 */
	public boolean createClient (String clientName, String orgName,
		String userClient, String userOrg)
	{
		if (Util.isEmpty(clientName))
			clientName = "Tenant_" + System.currentTimeMillis();
		if (Util.isEmpty(orgName))
			orgName = clientName + "_Org";
		if (Util.isEmpty(userClient))
			userClient = clientName + "_Admin";
		if (Util.isEmpty(userOrg))
			userOrg = clientName + "_User";
		//
		log.info(clientName);
		m_info = new StringBuffer();

		//	Test Client Name
		String sql = "UPDATE AD_Client SET CreatedBy=0 WHERE Name=?";
		if (DB.executeUpdate(m_trx, sql, new Object[]{clientName}) != 0)
		{
			String err = "Tenant Name NOT unique: " + clientName;
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}

		//  Standard columns
		String name = null;
		int no = 0;

		/**
		 *  Create Client
		 */
		name = clientName;
		m_clientName = name;
		m_client = new MClient(m_ctx, 0, true, m_trx);
		m_client.setValue(m_clientName);
		m_client.setName(m_clientName);
		if (!m_client.save())
		{
			String err = "Client NOT created";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		int AD_Client_ID = m_client.getAD_Client_ID();
		m_ctx.setAD_Client_ID(AD_Client_ID);
		m_ctx.setContext(m_WindowNo, "AD_Client_ID", AD_Client_ID);

		//	Standard Values
		m_stdValues = String.valueOf(AD_Client_ID) + ",0,'Y',SysDate,0,SysDate,0";
		//  Info - Client
		m_info.append(Msg.translate(m_lang, "AD_Client_ID")).append("=").append(name).append("\n");

		//	Setup Sequences
		if (!MSequence.checkClientSequences (m_ctx, AD_Client_ID, m_trx))
		{
			String err = "Sequences NOT created";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}

		//  Trees and Client Info
		if (!m_client.setupClientInfo(m_lang))
		{
			String err = "Client Info NOT created";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		m_AD_Tree_Account_ID = m_client.getSetup_AD_Tree_Account_ID();

		/**
		 *  Create Org
		 */
		name = orgName;
		m_org = new MOrg (m_client, name);
		if (!m_org.save())
		{
			String err = "Organization NOT created";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		m_ctx.setContext(m_WindowNo, "AD_Org_ID", getAD_Org_ID());
		m_ctx.setAD_Org_ID(getAD_Org_ID());
		m_stdValuesOrg = AD_Client_ID + "," + getAD_Org_ID() + ",'Y',SysDate,0,SysDate,0";
		//  Info
		m_info.append(Msg.translate(m_lang, "AD_Org_ID")).append("=").append(name).append("\n");

		/**
		 *  Create Roles
		 *  - Admin
		 *  - User
		 */
		name = m_clientName + " AdminRole";
		MRole admin = new MRole(m_ctx, 0, m_trx);
		admin.setClientOrg(m_client);
		admin.setName(name);
		admin.setUserLevel(X_AD_Role.USERLEVEL_TenantPlusOrganization);
		admin.setPreferenceType(X_AD_Role.PREFERENCETYPE_Tenant);
		admin.setIsShowAcct(true);
		if (!admin.save())
		{
			String err = "Admin Role NOT inserted";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		m_ctx.setAD_Role_ID(admin.getAD_Role_ID());
		//	OrgAccess x, 0
		MRoleOrgAccess adminClientAccess = new MRoleOrgAccess (admin, 0);
		if (!adminClientAccess.save())
			log.log(Level.WARNING, "Admin Role_OrgAccess 0 NOT created");
		//  OrgAccess x,y
		MRoleOrgAccess adminOrgAccess = new MRoleOrgAccess (admin, m_org.getAD_Org_ID());
		if (!adminOrgAccess.save())
			log.log(Level.WARNING, "Admin Role_OrgAccess NOT created");

		//  Info - Admin Role
		m_info.append(Msg.translate(m_lang, "AD_Role_ID")).append("=").append(name).append("\n");

		//
		name = m_clientName + " UserRole";
		MRole user = new MRole (m_ctx, 0, m_trx);
		user.setClientOrg(m_client);
		user.setName(name);
		if (!user.save())
		{
			String err = "User Role NOT inserted";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		//  OrgAccess x,y
		MRoleOrgAccess userOrgAccess = new MRoleOrgAccess (user, m_org.getAD_Org_ID());
		if (!userOrgAccess.save())
			log.log(Level.WARNING, "User Role_OrgAccess NOT created");

		//  Info - Client Role
		m_info.append(Msg.translate(m_lang, "AD_Role_ID")).append("=").append(name).append("\n");

		/**
		 *  Create Users
		 *  - Client
		 *  - Org
		 */
		name = userClient;
		AD_User_ID = getNextID(AD_Client_ID, "AD_User");
		AD_User_Name = name;
		name = DB.TO_STRING(name);
		sql = "INSERT INTO AD_User(" + m_stdColumns + ",AD_User_ID,"
			+ " Value,Name,Description,Password)"
			+ " VALUES (" + m_stdValues + "," + AD_User_ID + ","
			+ name + "," + name + "," + name + "," + name + ")";
		no = DB.executeUpdate(m_trx, sql);
		if (no != 1)
		{
			String err = "Admin User NOT inserted - " + AD_User_Name;
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		m_ctx.setAD_User_ID(AD_User_ID);
		//  Info
		m_info.append(Msg.translate(m_lang, "AD_User_ID")).append("=").append(AD_User_Name).append("/").append(AD_User_Name).append("\n");

		name = userOrg;
		if (name == null || name.length() == 0)
			name = m_clientName + "Org";
		AD_User_U_ID = getNextID(AD_Client_ID, "AD_User");
		AD_User_U_Name = name;
		name = DB.TO_STRING(name);
		sql = "INSERT INTO AD_User(" + m_stdColumns + ",AD_User_ID,"
			+ "Value,Name,Description,Password)"
			+ " VALUES (" + m_stdValues + "," + AD_User_U_ID + ","
			+ name + "," + name + "," + name + "," + name + ")";
		no = DB.executeUpdate(m_trx, sql);
		if (no != 1)
		{
			String err = "Org User NOT inserted - " + AD_User_U_Name;
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		//  Info
		m_info.append(Msg.translate(m_lang, "AD_User_ID")).append("=").append(AD_User_U_Name).append("/").append(AD_User_U_Name).append("\n");

		/**
		 *  Create User-Role
		 */
		//  ClientUser          - Admin & User
		sql = "INSERT INTO AD_User_Roles(" + m_stdColumns + ",AD_User_ID,AD_Role_ID)"
			+ " VALUES (" + m_stdValues + "," + AD_User_ID + "," + admin.getAD_Role_ID() + ")";
		no = DB.executeUpdate(m_trx, sql);
		if (no != 1)
			log.log(Level.WARNING, "UserRole ClientUser+Admin NOT inserted");
		sql = "INSERT INTO AD_User_Roles(" + m_stdColumns + ",AD_User_ID,AD_Role_ID)"
			+ " VALUES (" + m_stdValues + "," + AD_User_ID + "," + user.getAD_Role_ID() + ")";
		no = DB.executeUpdate(m_trx, sql);
		if (no != 1)
			log.log(Level.WARNING, "UserRole ClientUser+User NOT inserted");
		//  OrgUser             - User
		sql = "INSERT INTO AD_User_Roles(" + m_stdColumns + ",AD_User_ID,AD_Role_ID)"
			+ " VALUES (" + m_stdValues + "," + AD_User_U_ID + "," + user.getAD_Role_ID() + ")";
		no = DB.executeUpdate(m_trx, sql);
		if (no != 1)
			log.log(Level.WARNING, "UserRole OrgUser+Org NOT inserted");

		//	Processors
		MAcctProcessor ap = new MAcctProcessor(m_client, AD_User_ID);
		ap.save();

		MRequestProcessor rp = new MRequestProcessor (m_client, AD_User_ID);
		rp.save();

		log.info("fini");
		return true;
	}   //  createClient



	/**************************************************************************
	 *  Create Accounting elements.
	 *  - Calendar
	 *  - Account Trees
	 *  - Account Values
	 *  - Accounting Schema
	 *  - Default Accounts
	 *
	 *	@param C_Country_ID country
	 *	@param C_Currency_ID currency
	 *  @param hasProduct has product segment
	 *  @param hasBPartner has bp segment
	 *  @param hasProject has project segment
	 *  @param hasMCampaign has campaign segment
	 *  @param hasSRegion has sales region segment
	 *  @param AccountingFileName file name of accounting file
	 *  @return true if created
	 */
	public boolean createAccounting(int C_Country_ID, int C_Currency_ID,
		boolean hasProduct, boolean hasBPartner, boolean hasProject,
		boolean hasMCampaign, boolean hasSRegion,
		String AccountingFileName)
	{
		log.info(m_client.toString());
		if (C_Country_ID == 0)
			C_Country_ID = 100;		//	US
		if (C_Currency_ID == 0)
		{
			MCountry cc = new MCountry(m_ctx, C_Country_ID, m_trx);
			C_Currency_ID = cc.getC_Currency_ID();
		}
		if (C_Currency_ID == 0)
			C_Currency_ID = 100;	//	USD
		//
		m_C_Country_ID = C_Country_ID;
		m_C_Currency_ID = C_Currency_ID;
		m_hasProject = hasProject;
		m_hasMCampaign = hasMCampaign;
		m_hasSRegion = hasSRegion;

		//  Standard variables
		m_info = new StringBuffer("\n----\n");
		String name = null;
		StringBuffer sqlCmd = null;
		int no = 0;

		/**
		 *  Create Calendar
		 */
		m_calendar = new MCalendar(m_client);
		if (!m_calendar.save())
		{
			String err = "Calendar NOT inserted";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		//  Info
		m_info.append(Msg.translate(m_lang, "C_Calendar_ID")).append("=").append(m_calendar.getName()).append("\n");

		if (m_calendar.createYear(m_client.getLocale()) == null)
			log.log(Level.WARNING, "Year NOT inserted");

		//	Create Account Elements
		name = m_clientName + " " + Msg.translate(m_lang, "Account_ID");
		MElement element = new MElement (m_client, name,
			X_C_Element.ELEMENTTYPE_Account, m_AD_Tree_Account_ID);
		if (!element.save())
		{
			String err = "Acct Element NOT inserted";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		int C_Element_ID = element.getC_Element_ID();
		m_info.append(Msg.translate(m_lang, "C_Element_ID")).append("=").append(name).append("\n");

		//	Create Account Values
		if (Util.isEmpty(AccountingFileName))
			AccountingFileName = Compiere.getCompiereHome()
				+ File.separator + "data"
				+ File.separator + "import"
				+ File.separator + "AccountingUS.csv";
		File AccountingFile = new File(AccountingFileName);
		if (!AccountingFile.exists())
		{
			String errMsg = "Not found: " + AccountingFileName;
			m_info.append(errMsg);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		m_nap = new NaturalAccountMap(m_ctx, m_trx);
		String errMsg = m_nap.parseFile(AccountingFile);
		if (errMsg.length() != 0)
		{
			log.log(Level.WARNING, errMsg);
			m_info.append(errMsg);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		if (m_nap.saveAccounts(getAD_Client_ID(), getAD_Org_ID(), C_Element_ID))
			m_info.append(Msg.translate(m_lang, "C_ElementValue_ID")).append(" # ").append(m_nap.size()).append("\n");
		else
		{
			String err = "Acct Element Values NOT inserted";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}

		int C_ElementValue_ID = m_nap.getC_ElementValue_ID("DEFAULT_ACCT");
		log.fine("C_ElementValue_ID=" + C_ElementValue_ID);

		/**
		 *  Create AccountingSchema
		 */
		m_as = new MAcctSchema (m_client, m_C_Currency_ID);
		if (!m_as.save())
		{
			String err = "AcctSchema NOT inserted";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		//  Info
		m_info.append(Msg.translate(m_lang, "C_AcctSchema_ID")).append("=").append(m_as.getName()).append("\n");

		/**
		 *  Create AccountingSchema Elements (Structure)
		 */
		String sql2 = null;
		if (Env.isBaseLanguage(m_lang, "AD_Reference"))	//	Get ElementTypes & Name
			sql2 = "SELECT Value, Name FROM AD_Ref_List WHERE AD_Reference_ID=181";
		else
			sql2 = "SELECT l.Value, t.Name FROM AD_Ref_List l, AD_Ref_List_Trl t "
				+ "WHERE l.AD_Reference_ID=181 AND l.AD_Ref_List_ID=t.AD_Ref_List_ID";
		try
		{
			int AD_Client_ID = m_client.getAD_Client_ID();
			PreparedStatement stmt = DB.prepareStatement(sql2, m_trx);
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
			{
				String ElementType = rs.getString(1);
				name = rs.getString(2);
				//
				String IsMandatory = null;
				String IsBalanced = "N";
				int SeqNo = 0;
				int C_AcctSchema_Element_ID = 0;

				if (ElementType.equals("OO"))
				{
					C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					IsMandatory = "Y";
					IsBalanced = "Y";
					SeqNo = 10;
				}
				else if (ElementType.equals("AC"))
				{
					C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					IsMandatory = "Y";
					SeqNo = 20;
				}
				else if (ElementType.equals("PR") && hasProduct)
				{
					C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					IsMandatory = "N";
					SeqNo = 30;
				}
				else if (ElementType.equals("BP") && hasBPartner)
				{
					C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					IsMandatory = "N";
					SeqNo = 40;
				}
				else if (ElementType.equals("PJ") && hasProject)
				{
					C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					IsMandatory = "N";
					SeqNo = 50;
				}
				else if (ElementType.equals("MC") && hasMCampaign)
				{
					C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					IsMandatory = "N";
					SeqNo = 60;
				}
				else if (ElementType.equals("SR") && hasSRegion)
				{
					C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					IsMandatory = "N";
					SeqNo = 70;
				}
				//	Not OT, LF, LT, U1, U2, AY

				if (IsMandatory != null)
				{
					sqlCmd = new StringBuffer ("INSERT INTO C_AcctSchema_Element(");
					sqlCmd.append(m_stdColumns).append(",C_AcctSchema_Element_ID,C_AcctSchema_ID,")
						.append("ElementType,Name,SeqNo,IsMandatory,IsBalanced) VALUES (");
					sqlCmd.append(m_stdValues).append(",").append(C_AcctSchema_Element_ID).append(",").append(m_as.getC_AcctSchema_ID()).append(",")
						.append("'").append(ElementType).append("','").append(name).append("',").append(SeqNo).append(",'")
						.append(IsMandatory).append("','").append(IsBalanced).append("')");
					no = DB.executeUpdate(m_trx, sqlCmd.toString());
					if (no == 1)
						m_info.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\n");

					/** Default value for mandatory elements: OO and AC */
					if (ElementType.equals("OO"))
					{
						sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET Org_ID=");
						sqlCmd.append(getAD_Org_ID()).append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
						no = DB.executeUpdate(m_trx, sqlCmd.toString());
						if (no != 1)
							log.log(Level.WARNING, "Default Org in AcctSchamaElement NOT updated");
					}
					if (ElementType.equals("AC"))
					{
						sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET C_ElementValue_ID=");
						sqlCmd.append(C_ElementValue_ID).append(", C_Element_ID=").append(C_Element_ID);
						sqlCmd.append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
						no = DB.executeUpdate(m_trx, sqlCmd.toString());
						if (no != 1)
							log.log(Level.WARNING, "Default Account in AcctSchamaElement NOT updated");
					}
				}
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e1)
		{
			log.log(Level.WARNING, "Elements", e1);
			m_info.append(e1.getMessage());
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		//  Create AcctSchema


		//  Create GL Accounts
		m_accountsOK = true;
		sqlCmd = new StringBuffer ("INSERT INTO C_AcctSchema_GL (");
		sqlCmd.append(m_stdColumns).append(",C_AcctSchema_ID,"
		/*jz
			+ "USESUSPENSEBALANCING,SUSPENSEBALANCING_Acct,"
			+ "USESUSPENSEERROR,SUSPENSEERROR_Acct,"
			+ "USECURRENCYBALANCING,CURRENCYBALANCING_Acct,"
			+ "RETAINEDEARNING_Acct,INCOMESUMMARY_Acct,"
			+ "INTERCOMPANYDUETO_Acct,INTERCOMPANYDUEFROM_Acct,"
			+ "PPVOFFSET_Acct, CommitmentOffset_Acct) VALUES (");
		sqlCmd.append(m_stdValues).append(",").append(m_as.getC_AcctSchema_ID()).append(",")
			.append("'Y',").append(getAcct("SUSPENSEBALANCING_Acct")).append(",")
			.append("'Y',").append(getAcct("SUSPENSEERROR_Acct")).append(",")
			.append("'Y',").append(getAcct("CURRENCYBALANCING_Acct")).append(",");
		//  RETAINEDEARNING_Acct,INCOMESUMMARY_Acct,
		sqlCmd.append(getAcct("RETAINEDEARNING_Acct")).append(",")
			.append(getAcct("INCOMESUMMARY_Acct")).append(",")
		//  INTERCOMPANYDUETO_Acct,INTERCOMPANYDUEFROM_Acct)
			.append(getAcct("INTERCOMPANYDUETO_Acct")).append(",")
			.append(getAcct("INTERCOMPANYDUEFROM_Acct")).append(",")
			.append(getAcct("PPVOFFSET_Acct")).append(",")
			*/
			+ "UseSuspenseBalancing,SuspenseBalancing_Acct,"
			+ "UseSuspenseError,SuspenseError_Acct,"
			+ "UseCurrencyBalancing,CurrencyBalancing_Acct,"
			+ "RetainedEarning_Acct,IncomeSummary_Acct,"
			+ "IntercompanyDueTo_Acct,IntercompanyDueFrom_Acct,"
			+ "PPVOffset_Acct, CommitmentOffset_Acct) VALUES (");
		sqlCmd.append(m_stdValues).append(",").append(m_as.getC_AcctSchema_ID()).append(",")
			.append("'Y',").append(getAcct("SuspenseBalancing_Acct")).append(",")
			.append("'Y',").append(getAcct("SuspenseError_Acct")).append(",")
			.append("'Y',").append(getAcct("CurrencyBalancing_Acct")).append(",");
		//  RETAINEDEARNING_Acct,INCOMESUMMARY_Acct,
		sqlCmd.append(getAcct("RetainedEarning_Acct")).append(",")
			.append(getAcct("INCOMESUMMARY_Acct")).append(",")
		//  INTERCOMPANYDUETO_Acct,INTERCOMPANYDUEFROM_Acct)
			.append(getAcct("IntercompanyDueTo_Acct")).append(",")
			.append(getAcct("IntercompanyDueFrom_Acct")).append(",")
			.append(getAcct("PPVOffset_Acct")).append(",")
			.append(getAcct("CommitmentOffset_Acct"))
			.append(")");
		if (m_accountsOK)
			no = DB.executeUpdate(m_trx, sqlCmd.toString());
		else
			no = -1;
		if (no != 1)
		{
			String err = "GL Accounts NOT inserted";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}

		//	Create Std Accounts
		sqlCmd = new StringBuffer ("INSERT INTO C_AcctSchema_Default (");
		sqlCmd.append(m_stdColumns).append(",C_AcctSchema_ID,"
			+ " W_Inventory_Acct,W_Differences_Acct,W_Revaluation_Acct,W_InvActualAdjust_Acct, "
			+ " P_Revenue_Acct,P_Expense_Acct,P_CostAdjustment_Acct,P_InventoryClearing_Acct,P_Asset_Acct,P_COGS_Acct, "
			+ " P_PurchasePriceVariance_Acct,P_InvoicePriceVariance_Acct,P_TradeDiscountRec_Acct,P_TradeDiscountGrant_Acct, "
			+ " C_Receivable_Acct,C_Receivable_Services_Acct,C_Prepayment_Acct, "
			+ " V_Liability_Acct,V_Liability_Services_Acct,V_Prepayment_Acct, "
			+ " PayDiscount_Exp_Acct,PayDiscount_Rev_Acct,WriteOff_Acct, "
			+ " UnrealizedGain_Acct,UnrealizedLoss_Acct,RealizedGain_Acct,RealizedLoss_Acct, "
			+ " Withholding_Acct,E_Prepayment_Acct,E_Expense_Acct, "
			+ " PJ_Asset_Acct,PJ_WIP_Acct,"
			+ " T_Expense_Acct,T_Liability_Acct,T_Receivables_Acct,T_Due_Acct,T_Credit_Acct, "
			+ " B_InTransit_Acct,B_Asset_Acct,B_Expense_Acct,B_InterestRev_Acct,B_InterestExp_Acct,"
			+ " B_Unidentified_Acct,B_SettlementGain_Acct,B_SettlementLoss_Acct,"
			+ " B_RevaluationGain_Acct,B_RevaluationLoss_Acct,B_PaymentSelect_Acct,B_UnallocatedCash_Acct, "
			+ " Ch_Expense_Acct,Ch_Revenue_Acct, "
			+ " UnEarnedRevenue_Acct,NotInvoicedReceivables_Acct,NotInvoicedRevenue_Acct,NotInvoicedReceipts_Acct, "
			+ " CB_Asset_Acct,CB_CashTransfer_Acct,CB_Differences_Acct,CB_Expense_Acct,CB_Receipt_Acct," 
			+ " WO_Material_Acct, WO_MaterialOverhd_Acct,WO_Resource_Acct, "
			+ " WC_Overhead_Acct, WO_MaterialVariance_Acct,WO_MaterialOverhdVariance_Acct,WO_ResourceVariance_Acct," 
			+ " WO_OverhdVariance_Acct, P_MaterialOverhd_Acct, P_Resource_Absorption_Acct, Overhead_Absorption_Acct, WO_Scrap_Acct ) VALUES (");
		sqlCmd.append(m_stdValues).append(",").append(m_as.getC_AcctSchema_ID()).append(",");
		//  W_INVENTORY_Acct,W_Differences_Acct,W_REVALUATION_Acct,W_INVACTUALADJUST_Acct
		sqlCmd.append(getAcct("W_Inventory_Acct")).append(",");
		sqlCmd.append(getAcct("W_Differences_Acct")).append(",");
		sqlCmd.append(getAcct("W_Revaluation_Acct")).append(",");
		sqlCmd.append(getAcct("W_InvActualAdjust_Acct")).append(", ");
		//  P_Revenue_Acct,P_Expense_Acct,P_Asset_Acct,P_COGS_Acct,
		sqlCmd.append(getAcct("P_Revenue_Acct")).append(",");
		sqlCmd.append(getAcct("P_Expense_Acct")).append(",");
		sqlCmd.append(getAcct("P_CostAdjustment_Acct")).append(",");
		sqlCmd.append(getAcct("P_InventoryClearing_Acct")).append(",");
		sqlCmd.append(getAcct("P_Asset_Acct")).append(",");
		sqlCmd.append(getAcct("P_COGS_Acct")).append(", ");
		//  P_PURCHASEPRICEVARIANCE_Acct,P_INVOICEPRICEVARIANCE_Acct,P_TRADEDISCOUNTREC_Acct,P_TRADEDISCOUNTGRANT_Acct,
		sqlCmd.append(getAcct("P_PurchasePriceVariance_Acct")).append(",");
		sqlCmd.append(getAcct("P_InvoicePriceVariance_Acct")).append(",");
		sqlCmd.append(getAcct("P_TradeDiscountRec_Acct")).append(",");
		sqlCmd.append(getAcct("P_TradeDiscountGrant_Acct")).append(", ");
		//  C_RECEIVABLE_Acct,C_Receivable_Services_Acct,C_PREPAYMENT_Acct,
		sqlCmd.append(getAcct("C_Receivable_Acct")).append(",");
		sqlCmd.append(getAcct("C_Receivable_Services_Acct")).append(",");
		sqlCmd.append(getAcct("C_Prepayment_Acct")).append(", ");
		//  V_LIABILITY_Acct,V_LIABILITY_Services_Acct,V_Prepayment_Acct,
		sqlCmd.append(getAcct("V_Liability_Acct")).append(",");
		sqlCmd.append(getAcct("V_Liability_Services_Acct")).append(",");
		sqlCmd.append(getAcct("V_Prepayment_Acct")).append(", ");
		//  PAYDISCOUNT_EXP_Acct,PAYDISCOUNT_REV_Acct,WRITEOFF_Acct,
		sqlCmd.append(getAcct("PayDiscount_Exp_Acct")).append(",");
		sqlCmd.append(getAcct("PayDiscount_Rev_Acct")).append(",");
		sqlCmd.append(getAcct("WriteOff_Acct")).append(", ");
		//  UNREALIZEDGAIN_Acct,UNREALIZEDLOSS_Acct,REALIZEDGAIN_Acct,REALIZEDLOSS_Acct,
		sqlCmd.append(getAcct("UnrealizedGain_Acct")).append(",");
		sqlCmd.append(getAcct("UnrealizedLoss_Acct")).append(",");
		sqlCmd.append(getAcct("RealizedGain_Acct")).append(",");
		sqlCmd.append(getAcct("RealizedLoss_Acct")).append(", ");
		//  WITHHOLDING_Acct,E_Prepayment_Acct,E_Expense_Acct,
		sqlCmd.append(getAcct("Withholding_Acct")).append(",");
		sqlCmd.append(getAcct("E_Prepayment_Acct")).append(",");
		sqlCmd.append(getAcct("E_Expense_Acct")).append(", ");
		//  PJ_Asset_Acct,PJ_WIP_Acct,
		sqlCmd.append(getAcct("PJ_Asset_Acct")).append(",");
		sqlCmd.append(getAcct("PJ_WIP_Acct")).append(",");
		//  T_Expense_Acct,T_Liability_Acct,T_Receivables_Acct,T_DUE_Acct,T_CREDIT_Acct,
		sqlCmd.append(getAcct("T_Expense_Acct")).append(",");
		sqlCmd.append(getAcct("T_Liability_Acct")).append(",");
		sqlCmd.append(getAcct("T_Receivables_Acct")).append(",");
		sqlCmd.append(getAcct("T_Due_Acct")).append(",");
		sqlCmd.append(getAcct("T_Credit_Acct")).append(", ");
		//  B_INTRANSIT_Acct,B_Asset_Acct,B_Expense_Acct,B_INTERESTREV_Acct,B_INTERESTEXP_Acct,
		sqlCmd.append(getAcct("B_InTransit_Acct")).append(",");
		sqlCmd.append(getAcct("B_Asset_Acct")).append(",");
		sqlCmd.append(getAcct("B_Expense_Acct")).append(",");
		sqlCmd.append(getAcct("B_InterestREV_Acct")).append(",");
		sqlCmd.append(getAcct("B_InterestEXP_Acct")).append(",");
		//  B_UNIDENTIFIED_Acct,B_SETTLEMENTGAIN_Acct,B_SETTLEMENTLOSS_Acct,
		sqlCmd.append(getAcct("B_Unidentified_Acct")).append(",");
		sqlCmd.append(getAcct("B_SettlementGain_Acct")).append(",");
		sqlCmd.append(getAcct("B_SettlementLoss_Acct")).append(",");
		//  B_RevaluationGain_Acct,B_RevaluationLoss_Acct,B_PAYMENTSELECT_Acct,B_UnallocatedCash_Acct,
		sqlCmd.append(getAcct("B_RevaluationGain_Acct")).append(",");
		sqlCmd.append(getAcct("B_RevaluationLoss_Acct")).append(",");
		sqlCmd.append(getAcct("B_PaymentSelect_Acct")).append(",");
		sqlCmd.append(getAcct("B_UnallocatedCash_Acct")).append(", ");
		//  CH_Expense_Acct,CH_Revenue_Acct,
		sqlCmd.append(getAcct("Ch_Expense_Acct")).append(",");
		sqlCmd.append(getAcct("Ch_Revenue_Acct")).append(", ");
		//  UnEarnedRevenue_Acct,NotInvoicedReceivables_Acct,NotInvoicedRevenue_Acct,NotInvoicedReceipts_Acct,
		sqlCmd.append(getAcct("UnEarnedRevenue_Acct")).append(",");
		sqlCmd.append(getAcct("NotInvoicedReceivables_Acct")).append(",");
		sqlCmd.append(getAcct("NotInvoicedRevenue_Acct")).append(",");
		sqlCmd.append(getAcct("NotInvoicedReceipts_Acct")).append(", ");
		//  CB_Asset_Acct,CB_CashTransfer_Acct,CB_Differences_Acct,CB_Expense_Acct,CB_Receipt_Acct)
		sqlCmd.append(getAcct("CB_Asset_Acct")).append(",");
		sqlCmd.append(getAcct("CB_CashTransfer_Acct")).append(",");
		sqlCmd.append(getAcct("CB_Differences_Acct")).append(",");
		sqlCmd.append(getAcct("CB_Expense_Acct")).append(",");
		sqlCmd.append(getAcct("CB_Receipt_Acct")).append(",");
		sqlCmd.append(getAcct("WO_Material_Acct")).append(",");
		sqlCmd.append(getAcct("WO_MaterialOverhd_Acct")).append(",");
		sqlCmd.append(getAcct("WO_Resource_Acct")).append(",");
		sqlCmd.append(getAcct("WC_Overhead_Acct")).append(",");
		sqlCmd.append(getAcct("WO_MaterialVariance_Acct")).append(",");
		sqlCmd.append(getAcct("WO_MaterialOverhdVariance_Acct")).append(",");
		sqlCmd.append(getAcct("WO_ResourceVariance_Acct")).append(",");
		sqlCmd.append(getAcct("WO_OverhdVariance_Acct")).append(",");
		sqlCmd.append(getAcct("P_MaterialOverhd_Acct")).append(",");
		sqlCmd.append(getAcct("P_Resource_Absorption_Acct")).append(",");
		sqlCmd.append(getAcct("Overhead_Absorption_Acct")).append(",");
		sqlCmd.append(getAcct("WO_Scrap_Acct")).append(")");

		if (m_accountsOK)
			no = DB.executeUpdate(m_trx, sqlCmd.toString());
		else
			no = -1;
		if (no != 1)
		{
			String err = "Default Accounts Not inserted";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}

		//  GL Categories
		createGLCategory("Standard", X_GL_Category.CATEGORYTYPE_Manual, true);
		int GL_None = createGLCategory("None", X_GL_Category.CATEGORYTYPE_Document, false);
		int GL_GL = createGLCategory("Manual", X_GL_Category.CATEGORYTYPE_Manual, false);
		int GL_ARI = createGLCategory("AR Invoice", X_GL_Category.CATEGORYTYPE_Document, false);
		int GL_ARR = createGLCategory("AR Receipt", X_GL_Category.CATEGORYTYPE_Document, false);
		int GL_MM = createGLCategory("Material Management", X_GL_Category.CATEGORYTYPE_Document, false);
		int GL_API = createGLCategory("AP Invoice", X_GL_Category.CATEGORYTYPE_Document, false);
		int GL_APP = createGLCategory("AP Payment", X_GL_Category.CATEGORYTYPE_Document, false);
		int GL_CASH = createGLCategory("Cash/Payments", X_GL_Category.CATEGORYTYPE_Document, false);

		//	Base DocumentTypes
		int ii = createDocType("GL Journal", Msg.getElement(m_ctx, "GL_Journal_ID"),
			MDocBaseType.DOCBASETYPE_GLJournal, null, 0, 0,
			1000, GL_GL);
		if (ii == 0)
		{
			String err = "Document Type not created";
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		createDocType("GL Journal Batch", Msg.getElement(m_ctx, "GL_JournalBatch_ID"),
			MDocBaseType.DOCBASETYPE_GLJournal, null, 0, 0,
			100, GL_GL);
		//	MDocBaseType.DOCBASETYPE_GLDocument
		//
		int DT_I = createDocType("AR Invoice", Msg.getElement(m_ctx, "C_Invoice_ID", true),
			MDocBaseType.DOCBASETYPE_ARInvoice, null, 0, 0,
			100000, GL_ARI);
		int DT_II = createDocType("AR Invoice Indirect", Msg.getElement(m_ctx, "C_Invoice_ID", true),
			MDocBaseType.DOCBASETYPE_ARInvoice, null, 0, 0,
			150000, GL_ARI);
		int DT_IC = createDocType("AR Credit Memo", Msg.getMsg(m_ctx, "CreditMemo"),
			MDocBaseType.DOCBASETYPE_ARCreditMemo, null, 0, 0,
			170000, GL_ARI);
		//	MDocBaseType.DOCBASETYPE_ARProFormaInvoice

		createDocType("AP Invoice", Msg.getElement(m_ctx, "C_Invoice_ID", false),
			MDocBaseType.DOCBASETYPE_APInvoice, null, 0, 0,
			0, GL_API);
		createDocType("AP CreditMemo", Msg.getMsg(m_ctx, "CreditMemo"),
			MDocBaseType.DOCBASETYPE_APCreditMemo, null, 0, 0,
			0, GL_API);
		createDocType("Match Invoice", Msg.getElement(m_ctx, "M_MatchInv_ID", false),
			MDocBaseType.DOCBASETYPE_MatchInvoice, null, 0, 0,
			390000, GL_API);

		createDocType("AR Receipt", Msg.getElement(m_ctx, "C_Payment_ID", true),
			MDocBaseType.DOCBASETYPE_ARReceipt, null, 0, 0,
			0, GL_ARR);
		createDocType("AP Payment", Msg.getElement(m_ctx, "C_Payment_ID", false),
			MDocBaseType.DOCBASETYPE_APPayment, null, 0, 0,
			0, GL_APP);
		createDocType("Allocation", "Allocation",
			MDocBaseType.DOCBASETYPE_PaymentAllocation, null, 0, 0,
			490000, GL_CASH);

		int DT_S  = createDocType("MM Shipment", "Delivery Note",
			MDocBaseType.DOCBASETYPE_MaterialDelivery, null, 0, 0,
			500000, GL_MM);
		int DT_SI = createDocType("MM Shipment Indirect", "Delivery Note",
			MDocBaseType.DOCBASETYPE_MaterialDelivery, null, 0, 0,
			550000, GL_MM);
		int DT_SR = createDocType("MM Customer Return", "Customer Return",
			MDocBaseType.DOCBASETYPE_MaterialDelivery, null, 0, 0,
			590000, GL_MM, true, false);

		createDocType("MM Receipt", "Vendor Delivery",
			MDocBaseType.DOCBASETYPE_MaterialReceipt, null, 0, 0,
			0, GL_MM);
		createDocType("MM Vendor Return", "Vendor Return",
			MDocBaseType.DOCBASETYPE_MaterialReceipt, null, 0, 0,
			0, GL_MM, true, false);

		createDocType("Purchase Order", Msg.getElement(m_ctx, "C_Order_ID", false),
			MDocBaseType.DOCBASETYPE_PurchaseOrder, null, 0, 0,
			800000, GL_None);
		createDocType("Vendor RMA", "Vendor RMA",
			MDocBaseType.DOCBASETYPE_PurchaseOrder, null, 0, 0,
			890000, GL_None, true, false);
		createDocType("Match PO", Msg.getElement(m_ctx, "M_MatchPO_ID", false),
			MDocBaseType.DOCBASETYPE_MatchPO, null, 0, 0,
			880000, GL_None);
		createDocType("Purchase Requisition", Msg.getElement(m_ctx, "M_Requisition_ID", false),
			MDocBaseType.DOCBASETYPE_PurchaseRequisition, null, 0, 0,
			900000, GL_None);

		createDocType("Bank Statement", Msg.getElement(m_ctx, "C_BankStatemet_ID", true),
			MDocBaseType.DOCBASETYPE_BankStatement, null, 0, 0,
			700000, GL_CASH);
		createDocType("Cash Journal", Msg.getElement(m_ctx, "C_Cash_ID", true),
			MDocBaseType.DOCBASETYPE_CashJournal, null, 0, 0,
			750000, GL_CASH);

		createDocType("Material Movement", Msg.getElement(m_ctx, "M_Movement_ID", false),
			MDocBaseType.DOCBASETYPE_MaterialMovement, null, 0, 0,
			610000, GL_MM);
		createDocType("Physical Inventory", Msg.getElement(m_ctx, "M_Inventory_ID", false),
			MDocBaseType.DOCBASETYPE_MaterialPhysicalInventory, null, 0, 0,
			620000, GL_MM);
		createDocType("Material Production", Msg.getElement(m_ctx, "M_Production_ID", false),
			MDocBaseType.DOCBASETYPE_MaterialProduction, null, 0, 0,
			630000, GL_MM);
		createDocType("Project Issue", Msg.getElement(m_ctx, "C_ProjectIssue_ID", false),
			MDocBaseType.DOCBASETYPE_ProjectIssue, null, 0, 0,
			640000, GL_MM);

		//  Order Entry
		createDocType("Binding offer", "Quotation",
			MDocBaseType.DOCBASETYPE_SalesOrder, X_C_DocType.DOCSUBTYPESO_Quotation, 0, 0,
			10000, GL_None);
		createDocType("Non binding offer", "Proposal",
			MDocBaseType.DOCBASETYPE_SalesOrder, X_C_DocType.DOCSUBTYPESO_Proposal, 0, 0,
			20000, GL_None);
		createDocType("Prepay Order", "Prepay Order",
			MDocBaseType.DOCBASETYPE_SalesOrder, X_C_DocType.DOCSUBTYPESO_PrepayOrder, DT_S, DT_I,
			30000, GL_None);
		createDocType("Standard Order", "Order Confirmation",
			MDocBaseType.DOCBASETYPE_SalesOrder, X_C_DocType.DOCSUBTYPESO_StandardOrder, DT_S, DT_I,
			50000, GL_None);
		createDocType("Customer RMA", "Customer RMA",
			MDocBaseType.DOCBASETYPE_SalesOrder, X_C_DocType.DOCSUBTYPESO_StandardOrder, DT_SR, DT_IC,
			59000, GL_None, true, false);
		createDocType("Credit Order", "Order Confirmation",
			MDocBaseType.DOCBASETYPE_SalesOrder, X_C_DocType.DOCSUBTYPESO_OnCreditOrder, DT_SI, DT_I,
			60000, GL_None);   //  RE
		createDocType("Warehouse Order", "Order Confirmation",
			MDocBaseType.DOCBASETYPE_SalesOrder, X_C_DocType.DOCSUBTYPESO_WarehouseOrder, DT_S, DT_I,
			70000, GL_None);    //  LS
		int DT = createDocType("POS Order", "Order Confirmation",
			MDocBaseType.DOCBASETYPE_SalesOrder, X_C_DocType.DOCSUBTYPESO_POSOrder, DT_SI, DT_II,
			80000, GL_None);    // Bar
		//	POS As Default for window SO
		createPreference("C_DocTypeTarget_ID", String.valueOf(DT), 143);

		// Create WMS doc types if WMS is installed and licensed
		SysEnv se = SysEnv.get("CWMS", false);
        if (se!=null&& se.checkLicense())
        {
        	createDocType("Material Putaway", "Material Putaway",
        		MDocBaseType.DOCBASETYPE_MaterialPutaway, null, 0, 0, 0, GL_MM);
        	createDocType("Material Pick", "Material Pick",
            		MDocBaseType.DOCBASETYPE_MaterialPick, null, 0, 0, 0, GL_MM);
        	createDocType("Material Replenish", "Material Replenish",
            		MDocBaseType.DOCBASETYPE_MaterialReplenishment, null, 0, 0, 0, GL_MM);
        }
		// Create MFG doc types if MFG is installed and licensed
		se = SysEnv.get("CMFG", false);
        if (se!=null&& se.checkLicense())
        {
        	createDocType("Work Order", "Work Order",
        		MDocBaseType.DOCBASETYPE_WorkOrder, null, 0, 0, 0, GL_MM);
        	createDocType("Work Order Transaction", "Work Order Transaction",
            		MDocBaseType.DOCBASETYPE_WorkOrderTransaction, null, 0, 0, 0, GL_MM);
        }
        
        //Standard Cost Update
    	createDocType("Standard Cost Update", "Standard Cost Update",
        		MDocBaseType.DOCBASETYPE_StandardCostUpdate, null, 0, 0, 0, GL_MM);

		//  Update ClientInfo
		sqlCmd = new StringBuffer ("UPDATE AD_ClientInfo SET ");
		sqlCmd.append("C_AcctSchema1_ID=").append(m_as.getC_AcctSchema_ID())
			.append(", C_Calendar_ID=").append(m_calendar.getC_Calendar_ID())
			.append(" WHERE AD_Client_ID=").append(m_client.getAD_Client_ID());
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no != 1)
		{
			String err = "ClientInfo not updated";
			log.log(Level.WARNING, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
			return false;
		}

		//	Validate Completeness
		DocumentTypeVerify.createDocumentTypes(m_ctx, getAD_Client_ID(), null, m_trx);
		DocumentTypeVerify.createPeriodControls(m_ctx, getAD_Client_ID(), null, m_trx);
		//
		
		//Create Cash Expense/Receipt Types
		createCashExpenseReceiptType();
		log.info("fini");
		return true;
	}   //  createAccounting

	/**
	 *  Get Account ID for key
	 *  @param key key
	 *  @return C_ValidCombination_ID
	 */
	private int getAcct (String key)
	{
		log.fine(key);
		//  Element
		int C_ElementValue_ID = m_nap.getC_ElementValue_ID(key.toUpperCase());
		if (C_ElementValue_ID == 0)
		{
			log.warning("Account not defined: " + key);
			m_accountsOK = false;
			return 0;
		}

		MAccount vc = MAccount.getDefault(m_as, true);	//	optional null
		vc.setAD_Org_ID(0);		//	will be overwritten
		vc.setAccount_ID(C_ElementValue_ID);
		if (!vc.save())
		{
			log.warning("Not Saved - Key=" + key + ", C_ElementValue_ID=" + C_ElementValue_ID);
			m_accountsOK = false;
			return 0;
		}
		int C_ValidCombination_ID = vc.getC_ValidCombination_ID();
		if (C_ValidCombination_ID == 0)
		{
			log.warning("No account - Key=" + key + ", C_ElementValue_ID=" + C_ElementValue_ID);
			m_accountsOK = false;
		}
		return C_ValidCombination_ID;
	}   //  getAcct

	/**
	 *  Create GL Category
	 *  @param Name name
	 *  @param CategoryType category type MGLCategory.CATEGORYTYPE_*
	 *  @param isDefault is default value
	 *  @return GL_Category_ID
	 */
	private int createGLCategory (String Name, String CategoryType, boolean isDefault)
	{
		MGLCategory cat = new MGLCategory (m_ctx, 0, m_trx);
		cat.setName(Name);
		cat.setCategoryType(CategoryType);
		cat.setIsDefault(isDefault);
		if (!cat.save())
		{
			log.log(Level.WARNING, "GL Category NOT created - " + Name);
			return 0;
		}
		//
		return cat.getGL_Category_ID();
	}   //  createGLCategory

	/**
	 *  Create Document Types with Sequence
	 *  @param Name name
	 *  @param PrintName print name
	 *  @param DocBaseType document base type
	 *  @param DocSubTypeSO sales order sub type
	 *  @param C_DocTypeShipment_ID shipment doc
	 *  @param C_DocTypeInvoice_ID invoice doc
	 *  @param StartNo start doc no
	 *  @param GL_Category_ID gl category
	 *  @return C_DocType_ID doc type or 0 for error
	 */
	private int createDocType (String Name, String PrintName,
		String DocBaseType, String DocSubTypeSO,
		int C_DocTypeShipment_ID, int C_DocTypeInvoice_ID,
		int StartNo, int GL_Category_ID)
	{
		return createDocType(Name, PrintName, DocBaseType, DocSubTypeSO,
				C_DocTypeShipment_ID, C_DocTypeInvoice_ID,
				StartNo, GL_Category_ID, false, true);
	}	//	createDocType

	/**
	 *  Create Document Types with Sequence
	 *  @param Name name
	 *  @param PrintName print name
	 *  @param DocBaseType document base type
	 *  @param DocSubTypeSO sales order sub type
	 *  @param C_DocTypeShipment_ID shipment doc
	 *  @param C_DocTypeInvoice_ID invoice doc
	 *  @param StartNo start doc no
	 *  @param GL_Category_ID gl category
	 *  @return C_DocType_ID doc type or 0 for error
	 */
	private int createDocType (String Name, String PrintName,
		String DocBaseType, String DocSubTypeSO,
		int C_DocTypeShipment_ID, int C_DocTypeInvoice_ID,
		int StartNo, int GL_Category_ID, boolean isReturnTrx,
		boolean IsCreateCounter)
	{
		MSequence sequence = null;
		if (StartNo != 0)
		{
			sequence = new MSequence(m_ctx, getAD_Client_ID(), Name, StartNo, m_trx);
			if (!sequence.save())
			{
				log.log(Level.WARNING, "Sequence NOT created - " + Name);
				return 0;
			}
		}

		MDocType dt = new MDocType (m_ctx, DocBaseType, Name, m_trx);
		if (PrintName != null && PrintName.length() > 0)
			dt.setPrintName(PrintName);	//	Defaults to Name
		if (DocSubTypeSO != null)
			dt.setDocSubTypeSO(DocSubTypeSO);
		if (C_DocTypeShipment_ID != 0)
			dt.setC_DocTypeShipment_ID(C_DocTypeShipment_ID);
		if (C_DocTypeInvoice_ID != 0)
			dt.setC_DocTypeInvoice_ID(C_DocTypeInvoice_ID);
		if (GL_Category_ID != 0)
			dt.setGL_Category_ID(GL_Category_ID);
		if (sequence == null)
			dt.setIsDocNoControlled(false);
		else
		{
			dt.setIsDocNoControlled(true);
			dt.setDocNoSequence_ID(sequence.getAD_Sequence_ID());
		}
		dt.setIsSOTrx();
		dt.setIsReturnTrx(isReturnTrx);
		dt.setIsCreateCounter(IsCreateCounter);
		if (!dt.save())
		{
			log.log(Level.WARNING, "DocType NOT created - " + Name);
			return 0;
		}
		//
		return dt.getC_DocType_ID();
	}   //  createDocType


	/**************************************************************************
	 *  Create Default main entities.
	 *  - Dimensions & BPGroup, Prod Category)
	 *  - Location, Locator, Warehouse
	 *  - PriceList
	 *  - Cashbook, PaymentTerm
	 *  @param City city
	 *  @param C_Region_ID region
	 *  @return true if created
	 */
	public boolean createEntities (String City, int C_Region_ID)
	{
		if (m_as == null)
		{
			log.warning ("No AcctountingSChema");
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		log.info("C_Country_ID=" + m_C_Country_ID
			+ ", City=" + City + ", C_Region_ID=" + C_Region_ID);
		m_info = new StringBuffer("\n----\n");
		//
		String defaultName = Msg.translate(m_lang, "Standard");
		String defaultEntry = "'" + defaultName + "',";
		StringBuffer sqlCmd = null;
		int no = 0;

		//	Create Marketing Channel/Campaign
		int C_Channel_ID = getNextID(getAD_Client_ID(), "C_Channel");
		sqlCmd = new StringBuffer("INSERT INTO C_Channel ");
		sqlCmd.append("(C_Channel_ID,Name,");
		sqlCmd.append(m_stdColumns).append(") VALUES (");
		sqlCmd.append(C_Channel_ID).append(",").append(defaultEntry);
		sqlCmd.append(m_stdValues).append(")");
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no != 1)
			log.log(Level.WARNING, "Channel NOT inserted");
		int C_Campaign_ID = getNextID(getAD_Client_ID(), "C_Campaign");
		sqlCmd = new StringBuffer("INSERT INTO C_Campaign ");
		sqlCmd.append("(C_Campaign_ID,C_Channel_ID,").append(m_stdColumns).append(",");
		sqlCmd.append(" Value,Name,Costs) VALUES (");
		sqlCmd.append(C_Campaign_ID).append(",").append(C_Channel_ID).append(",").append(m_stdValues).append(",");
		sqlCmd.append(defaultEntry).append(defaultEntry).append("0)");
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no == 1)
			m_info.append(Msg.translate(m_lang, "C_Campaign_ID")).append("=").append(defaultName).append("\n");
		else
			log.log(Level.WARNING, "Campaign NOT inserted");
		if (m_hasMCampaign)
		{
			//  Default
			sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET ");
			sqlCmd.append("C_Campaign_ID=").append(C_Campaign_ID);
			sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as.getC_AcctSchema_ID());
			sqlCmd.append(" AND ElementType='MC'");
			no = DB.executeUpdate(m_trx, sqlCmd.toString());
			if (no != 1)
				log.log(Level.WARNING, "AcctSchema ELement Campaign NOT updated");
		}

		//	Create Sales Region
		int C_SalesRegion_ID = getNextID(getAD_Client_ID(), "C_SalesRegion");
		sqlCmd = new StringBuffer ("INSERT INTO C_SalesRegion ");
		sqlCmd.append("(C_SalesRegion_ID,").append(m_stdColumns).append(",");
		sqlCmd.append(" Value,Name,IsSummary) VALUES (");
		sqlCmd.append(C_SalesRegion_ID).append(",").append(m_stdValues).append(", ");
		sqlCmd.append(defaultEntry).append(defaultEntry).append("'N')");
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no == 1)
			m_info.append(Msg.translate(m_lang, "C_SalesRegion_ID")).append("=").append(defaultName).append("\n");
		else
			log.log(Level.WARNING, "SalesRegion NOT inserted");
		if (m_hasSRegion)
		{
			//  Default
			sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET ");
			sqlCmd.append("C_SalesRegion_ID=").append(C_SalesRegion_ID);
			sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as.getC_AcctSchema_ID());
			sqlCmd.append(" AND ElementType='SR'");
			no = DB.executeUpdate(m_trx, sqlCmd.toString());
			if (no != 1)
				log.log(Level.WARNING, "AcctSchema ELement SalesRegion NOT updated");
		}

		/**
		 *  Business Partner
		 */
		//  Create BP Group
		MBPGroup bpg = new MBPGroup (m_ctx, 0, m_trx);
		bpg.setValue(defaultName);
		bpg.setName(defaultName);
		bpg.setIsDefault(true);
		if (bpg.save())
			m_info.append(Msg.translate(m_lang, "C_BP_Group_ID")).append("=").append(defaultName).append("\n");
		else
			log.log(Level.WARNING, "BP Group NOT inserted");

		//	Create BPartner
		MBPartner bp = new MBPartner (m_ctx, 0, m_trx);
		bp.setValue(defaultName);
		bp.setName(defaultName);
		bp.setBPGroup(bpg);
		if (bp.save())
			m_info.append(Msg.translate(m_lang, "C_BPartner_ID")).append("=").append(defaultName).append("\n");
		else
			log.log(Level.WARNING, "BPartner NOT inserted");
		//  Location for Standard BP
		MLocation bpLoc = new MLocation(m_ctx, m_C_Country_ID, C_Region_ID, City, m_trx);
		bpLoc.save();
		MBPartnerLocation bpl = new MBPartnerLocation(bp);
		bpl.setC_Location_ID(bpLoc.getC_Location_ID());
		if (!bpl.save())
			log.log(Level.WARNING, "BP_Location (Standard) NOT inserted");
		//  Default
		sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET ");
		sqlCmd.append("C_BPartner_ID=").append(bp.getC_BPartner_ID());
		sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as.getC_AcctSchema_ID());
		sqlCmd.append(" AND ElementType='BP'");
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no != 1)
			log.log(Level.WARNING, "AcctSchema Element BPartner NOT updated");
		createPreference("C_BPartner_ID", String.valueOf(bp.getC_BPartner_ID()), 143);

		/**
		 *  Product
		 */
		//  Create Product Category
		MProductCategory pc = new MProductCategory(m_ctx, 0, m_trx);
		pc.setValue(defaultName);
		pc.setName(defaultName);
		pc.setIsDefault(true);
		if (pc.save())
			m_info.append(Msg.translate(m_lang, "M_Product_Category_ID")).append("=").append(defaultName).append("\n");
		else
			log.log(Level.WARNING, "Product Category NOT inserted");

		//  UOM (EA)
		int C_UOM_ID = 100;

		//  TaxCategory
		int C_TaxCategory_ID = getNextID(getAD_Client_ID(), "C_TaxCategory");
		sqlCmd = new StringBuffer ("INSERT INTO C_TaxCategory ");
		sqlCmd.append("(C_TaxCategory_ID,").append(m_stdColumns).append(",");
		sqlCmd.append(" Name,IsDefault) VALUES (");
		sqlCmd.append(C_TaxCategory_ID).append(",").append(m_stdValues).append(", ");
		if (m_C_Country_ID == 100)    // US
			sqlCmd.append("'Sales Tax','Y')");
		else
			sqlCmd.append(defaultEntry).append("'Y')");
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no != 1)
			log.log(Level.WARNING, "TaxCategory NOT inserted");

		//  Tax - Zero Rate
		MTax tax = new MTax (m_ctx, "Standard", Env.ZERO, C_TaxCategory_ID, m_trx);
		tax.setIsDefault(true);
		if (tax.save())
			m_info.append(Msg.translate(m_lang, "C_Tax_ID"))
				.append("=").append(tax.getName()).append("\n");
		else
			log.log(Level.WARNING, "Tax NOT inserted");

		//	Create Product
		MProduct product = new MProduct (m_ctx, 0, m_trx);
		product.setValue(defaultName);
		product.setName(defaultName);
		product.setC_UOM_ID(C_UOM_ID);
		product.setM_Product_Category_ID(pc.getM_Product_Category_ID());
		product.setC_TaxCategory_ID(C_TaxCategory_ID);
		if (product.save())
			m_info.append(Msg.translate(m_lang, "M_Product_ID")).append("=").append(defaultName).append("\n");
		else
			log.log(Level.WARNING, "Product NOT inserted");
		//  Default
		sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET ");
		sqlCmd.append("M_Product_ID=").append(product.getM_Product_ID());
		sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as.getC_AcctSchema_ID());
		sqlCmd.append(" AND ElementType='PR'");
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no != 1)
			log.log(Level.WARNING, "AcctSchema Element Product NOT updated");

		/**
		 *  Location, Warehouse, Locator
		 */
		//  Location (Company)
		MLocation loc = new MLocation(m_ctx, m_C_Country_ID, C_Region_ID, City, m_trx);
		loc.save();
		sqlCmd = new StringBuffer ("UPDATE AD_OrgInfo SET C_Location_ID=");
		sqlCmd.append(loc.getC_Location_ID()).append(" WHERE AD_Org_ID=").append(getAD_Org_ID());
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no != 1)
			log.log(Level.WARNING, "Location NOT inserted");
		createPreference("C_Country_ID", String.valueOf(m_C_Country_ID), 0);

		//  Default Warehouse
		MWarehouse wh = new MWarehouse(m_ctx, 0, m_trx);
		wh.setValue(defaultName);
		wh.setName(defaultName);
		wh.setC_Location_ID(loc.getC_Location_ID());
		if (!wh.save())
			log.log(Level.WARNING, "Warehouse NOT inserted");

		//   Locator
		MLocator locator = new MLocator(wh, defaultName);
		locator.setIsDefault(true);
		if (!locator.save())
			log.log(Level.WARNING, "Locator NOT inserted");

		//  Update ClientInfo
		sqlCmd = new StringBuffer ("UPDATE AD_ClientInfo SET ");
		sqlCmd.append("C_BPartnerCashTrx_ID=").append(bp.getC_BPartner_ID());
		sqlCmd.append(",M_ProductFreight_ID=").append(product.getM_Product_ID());
//		sqlCmd.append("C_UOM_Volume_ID=");
//		sqlCmd.append(",C_UOM_Weight_ID=");
//		sqlCmd.append(",C_UOM_Length_ID=");
//		sqlCmd.append(",C_UOM_Time_ID=");
		sqlCmd.append(" WHERE AD_Client_ID=").append(getAD_Client_ID());
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no != 1)
		{
			String err = "ClientInfo not updated";
			log.log(Level.WARNING, err);
			m_info.append(err);
			return false;
		}

		/**
		 *  Other
		 */
		//  PriceList
		MPriceList pl = new MPriceList(m_ctx, 0, m_trx);
		pl.setName(defaultName);
		pl.setC_Currency_ID(m_C_Currency_ID);
		pl.setIsDefault(true);
		if (!pl.save())
			log.log(Level.WARNING, "PriceList NOT inserted");
		//  Price List
		MDiscountSchema ds = new MDiscountSchema(m_ctx, 0, m_trx);
		ds.setName(defaultName);
		ds.setDiscountType(X_M_DiscountSchema.DISCOUNTTYPE_Pricelist);
		if (!ds.save())
			log.log(Level.WARNING, "DiscountSchema NOT inserted");
		//  PriceList Version
		MPriceListVersion plv = new MPriceListVersion(pl);
		plv.setName();
		plv.setM_DiscountSchema_ID(ds.getM_DiscountSchema_ID());
		if (!plv.save())
			log.log(Level.WARNING, "PriceList_Version NOT inserted");
		//  ProductPrice
		MProductPrice pp = new MProductPrice(plv, product.getM_Product_ID(),
			Env.ONE, Env.ONE, Env.ONE);
		if (!pp.save())
			log.log(Level.WARNING, "ProductPrice NOT inserted");


		//	Create Sales Rep for Client-User
		MBPartner bpCU = new MBPartner (m_ctx, 0, m_trx);
		bpCU.setValue(AD_User_U_Name);
		bpCU.setName(AD_User_U_Name);
		bpCU.setBPGroup(bpg);
		bpCU.setIsEmployee(true);
		bpCU.setIsSalesRep(true);
		if (bpCU.save())
			m_info.append(Msg.translate(m_lang, "SalesRep_ID")).append("=").append(AD_User_U_Name).append("\n");
		else
			log.log(Level.WARNING, "SalesRep (User) NOT inserted");
		//  Location for Client-User
		MLocation bpLocCU = new MLocation(m_ctx, m_C_Country_ID, C_Region_ID, City, m_trx);
		bpLocCU.save();
		MBPartnerLocation bplCU = new MBPartnerLocation(bpCU);
		bplCU.setC_Location_ID(bpLocCU.getC_Location_ID());
		if (!bplCU.save())
			log.log(Level.WARNING, "BP_Location (User) NOT inserted");
		//  Update User
		sqlCmd = new StringBuffer ("UPDATE AD_User SET C_BPartner_ID=");
		sqlCmd.append(bpCU.getC_BPartner_ID()).append(" WHERE AD_User_ID=").append(AD_User_U_ID);
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no != 1)
			log.log(Level.WARNING, "User of SalesRep (User) NOT updated");


		//	Create Sales Rep for Client-Admin
		MBPartner bpCA = new MBPartner (m_ctx, 0, m_trx);
		bpCA.setValue(AD_User_Name);
		bpCA.setName(AD_User_Name);
		bpCA.setBPGroup(bpg);
		bpCA.setIsEmployee(true);
		bpCA.setIsSalesRep(true);
		if (bpCA.save())
			m_info.append(Msg.translate(m_lang, "SalesRep_ID")).append("=").append(AD_User_Name).append("\n");
		else
			log.log(Level.WARNING, "SalesRep (Admin) NOT inserted");
		//  Location for Client-Admin
		MLocation bpLocCA = new MLocation(m_ctx, m_C_Country_ID, C_Region_ID, City, m_trx);
		bpLocCA.save();
		MBPartnerLocation bplCA = new MBPartnerLocation(bpCA);
		bplCA.setC_Location_ID(bpLocCA.getC_Location_ID());
		if (!bplCA.save())
			log.log(Level.WARNING, "BP_Location (Admin) NOT inserted");
		//  Update User
		sqlCmd = new StringBuffer ("UPDATE AD_User SET C_BPartner_ID=");
		sqlCmd.append(bpCA.getC_BPartner_ID()).append(" WHERE AD_User_ID=").append(AD_User_ID);
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no != 1)
			log.log(Level.WARNING, "User of SalesRep (Admin) NOT updated");


		//  Payment Term
		int C_PaymentTerm_ID = getNextID(getAD_Client_ID(), "C_PaymentTerm");
		sqlCmd = new StringBuffer ("INSERT INTO C_PaymentTerm ");
		sqlCmd.append("(C_PaymentTerm_ID,").append(m_stdColumns).append(",");
		sqlCmd.append("Value,Name,NetDays,GraceDays,DiscountDays,Discount,DiscountDays2,Discount2,IsDefault) VALUES (");
		sqlCmd.append(C_PaymentTerm_ID).append(",").append(m_stdValues).append(",");
		sqlCmd.append("'Immediate','Immediate',0,0,0,0,0,0,'Y')");
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no != 1)
			log.log(Level.WARNING, "PaymentTerm NOT inserted");

		//  Project Cycle
		C_Cycle_ID = getNextID(getAD_Client_ID(), "C_Cycle");
		sqlCmd = new StringBuffer ("INSERT INTO C_Cycle ");
		sqlCmd.append("(C_Cycle_ID,").append(m_stdColumns).append(",");
		sqlCmd.append(" Name,C_Currency_ID) VALUES (");
		sqlCmd.append(C_Cycle_ID).append(",").append(m_stdValues).append(", ");
		sqlCmd.append(defaultEntry).append(m_C_Currency_ID).append(")");
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no != 1)
			log.log(Level.WARNING, "Cycle NOT inserted");

		/**
		 *  Organization level data	===========================================
		 */

		//	Create Default Project
		int C_Project_ID = getNextID(getAD_Client_ID(), "C_Project");
		sqlCmd = new StringBuffer ("INSERT INTO C_Project ");
		sqlCmd.append("(C_Project_ID,").append(m_stdColumns).append(",");
		sqlCmd.append(" Value,Name,C_Currency_ID,IsSummary) VALUES (");
		sqlCmd.append(C_Project_ID).append(",").append(m_stdValuesOrg).append(", ");
		sqlCmd.append(defaultEntry).append(defaultEntry).append(m_C_Currency_ID).append(",'N')");
		no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no == 1)
			m_info.append(Msg.translate(m_lang, "C_Project_ID")).append("=").append(defaultName).append("\n");
		else
			log.log(Level.WARNING, "Project NOT inserted");
		//  Default Project
		if (m_hasProject)
		{
			sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET ");
			sqlCmd.append("C_Project_ID=").append(C_Project_ID);
			sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as.getC_AcctSchema_ID());
			sqlCmd.append(" AND ElementType='PJ'");
			no = DB.executeUpdate(m_trx, sqlCmd.toString());
			if (no != 1)
				log.log(Level.WARNING, "AcctSchema ELement Project NOT updated");
		}

		//  CashBook
		MCashBook cb = new MCashBook(m_ctx, 0, m_trx);
		cb.setName(defaultName);
		cb.setC_Currency_ID(m_C_Currency_ID);
		if (cb.save())
			m_info.append(Msg.translate(m_lang, "C_CashBook_ID")).append("=").append(defaultName).append("\n");
		else
			log.log(Level.WARNING, "CashBook NOT inserted");
		//
		
		m_trx.commit();
		m_trx.close();
		log.info("fini");
		return true;
	}   //  createEntities

	/**
	 *  Create Preference
	 *  @param Attribute attribute
	 *  @param Value value
	 *  @param AD_Window_ID window
	 */
	private void createPreference (String Attribute, String Value, int AD_Window_ID)
	{
		int AD_Preference_ID = getNextID(getAD_Client_ID(), "AD_Preference");
		StringBuffer sqlCmd = new StringBuffer ("INSERT INTO AD_Preference ");
		sqlCmd.append("(AD_Preference_ID,").append(m_stdColumns).append(",");
		sqlCmd.append("Attribute,Value,AD_Window_ID) VALUES (");
		sqlCmd.append(AD_Preference_ID).append(",").append(m_stdValues).append(",");
		sqlCmd.append("'").append(Attribute).append("','").append(Value).append("',");
		if (AD_Window_ID == 0)
			sqlCmd.append(DB.NULL("I", Types.INTEGER) + " )");  //jz nullif
		else
			sqlCmd.append(AD_Window_ID).append(")");
		int no = DB.executeUpdate(m_trx, sqlCmd.toString());
		if (no != 1)
			log.log(Level.WARNING, "Preference NOT inserted - " + Attribute);
	}   //  createPreference
	
	private void createCashExpenseReceiptType()
	{
		MCashExpenseReceiptType expense = new MCashExpenseReceiptType(m_ctx,0,m_trx);
		expense.setAD_Client_ID(getAD_Client_ID());
		expense.setAD_Org_ID(0);
		expense.setName("General Expenses");
		expense.setDescription("General Expenses");
		expense.setIsActive(true);
		expense.setIsDefault(true);
		expense.setIsExpense(true);
		if(!expense.save())
			log.log(Level.WARNING, "Default Expense/Receipt Type Not inserted");
		
		MCashExpenseReceiptType receipt = new MCashExpenseReceiptType(m_ctx,0,m_trx);
		receipt.setAD_Client_ID(getAD_Client_ID());
		receipt.setAD_Org_ID(0);
		receipt.setName("General Receipts");
		receipt.setDescription("General Receipts");
		receipt.setIsActive(true);
		receipt.setIsDefault(true);
		receipt.setIsExpense(false);
		if(!receipt.save())
			log.log(Level.WARNING, "Default Expense/Receipt Type Not inserted");

	}


	/**************************************************************************
	 * 	Get Next ID
	 * 	@param AD_Client_ID client
	 * 	@param TableName table name
	 * 	@return id
	 */
	private int getNextID (int AD_Client_ID, String TableName)
	{
		//	TODO: Exception
		return DB.getNextID (AD_Client_ID, TableName, m_trx);
	}	//	getNextID

	/**
	 *  Get Client
	 *  @return AD_Client_ID
	 */
	public int getAD_Client_ID()
	{
		return m_client.getAD_Client_ID();
	}
	/**
	 * 	Get AD_Org_ID
	 *	@return AD_Org_ID
	 */
	public int getAD_Org_ID()
	{
		return m_org.getAD_Org_ID();
	}
	/**
	 * 	Get AD_User_ID
	 *	@return AD_User_ID
	 */
	public int getAD_User_ID()
	{
		return AD_User_ID;
	}
	/**
	 * 	Get Info
	 *	@return Info
	 */
	public String getInfo()
	{
		return m_info.toString();
	}

	/**
	 * 	Get Administrator user name (Password is the same)
	 * 	@return user name
	 */
	public String getUserName()
	{
		return AD_User_Name;
	}	//	getUserName

	/**
	 * 	Test
	 * 	@param args name of client
	 */
	public static void main(String[] args)
    {
	    Compiere.startup(true);
	    try
	    {
	    	String clientName = null;
	    	if (args.length > 0)
	    		clientName = args[0];
	    	String info = createNewClient(null, clientName);
	    	System.out.print(info);
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
		}
    }
}   //  MSetup
