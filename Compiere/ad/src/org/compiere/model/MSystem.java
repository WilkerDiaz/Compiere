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

import java.lang.management.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.constants.*;
import org.compiere.db.*;
import org.compiere.util.*;

/**
 * 	System Record (just one)
 *
 *  @author Jorg Janke
 *  @version $Id: MSystem.java 8776 2010-05-19 17:50:56Z nnayak $
 */
public class MSystem extends X_AD_System
{
    /** Logger for class MSystem */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MSystem.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Load System Record
	 *	@param ctx context
	 *	@return System
	 */
	public static MSystem get (Ctx ctx)
	{
		if (s_system != null)
			return s_system;
		//
		String sql = "SELECT * FROM AD_System ORDER BY AD_System_ID";	//	0 first
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			if (rs.next())
				s_system = new MSystem (ctx, rs, null);
		}
		catch (SQLException ex) {
			String info = "No System - " + DB.getDatabaseInfo() + " - " + ex.getLocalizedMessage();
			System.err.println(info);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (s_system == null)
			return null;
		//
		if (!Ini.isClient() && s_system.setInfo())
			s_system.save();
		return s_system;
	}	//	get

	/** System - cached					*/
	private static MSystem		s_system = null;
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param ignored id - if < 0 not loaded
	 *	@param mtrxName transaction
	 */
	public MSystem (Ctx ctx, int ignored, Trx mtrxName)
	{
		super(ctx, 0, mtrxName);
		Trx trx = null;
		if (ignored >= 0)
			load(trx);	//	load ID=0
		if (s_system == null)
			s_system = this;
	}	//	MSystem

	/**
	 * 	Load Constructor
	 * 	@param ctx context
	 * 	@param rs result set
	 * 	@param trx transaction
	 */
	public MSystem (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
		if (s_system == null)
			s_system = this;
	}	//	MSystem

	/**
	 * 	Constructor
	 */
	public MSystem ()
	{
		this (new Ctx(), 0, null);
	}	//	MSystem

	/**
	 * 	Is LDAP Authentification defined
	 *	@return true if ldap defined
	 */
	public boolean isLDAP()
	{
		String host = getLDAPHost();
		if (host == null || host.length() == 0)
			return false;
		String domain = getLDAPDomain();
		return domain != null 
			&& domain.length() > 0;
	}	//	isLDAP	
	
	/**
	 * 	LDAP Authentification. Assumes that LDAP is defined.
	 *	@param userName user name
	 *	@param password password
	 *	@return true if ldap authenticated
	 */
	public boolean isLDAP (String userName, String password)
	{
		return LDAP.validate(getLDAPHost(), getLDAPDomain(), userName, password);
	}	//	isLDAP

	/**
	 * 	Get DB Address
	 *	@return dbURL
	 */
	public String getDBAddress (boolean actual)
	{
		String s = super.getDBAddress ();
		if (actual || s == null || s.length() == 0)
		{
			CConnection cc = CConnection.get(); 
			s = cc.getConnectionURL() + "#" + cc.getDbUid();
			s = s.toLowerCase();
		}
		return s;
	}	//	getDBAddress
	
	/**
	 * 	Get Statistics Info
	 * 	@param recalc recalculate
	 *	@return statistics
	 */
	public String getStatisticsInfo (boolean recalc)
	{
		String s = super.getStatisticsInfo();
		if (s == null || recalc)
		{
			String count = DB.TO_CHAR("COUNT(*)", DisplayTypeConstants.Number, Env.getAD_Language(Env.getCtx())); 
			String sql = "SELECT 'C'||(SELECT " + count + " FROM AD_Client)"
				+ " ||'U'||(SELECT " + count + " FROM AD_User)"
				+ " ||'B'||(SELECT " + count + " FROM C_BPartner)"
				+ " ||'P'||(SELECT " + count + " FROM M_Product)"
				+ " ||'I'||(SELECT " + count + " FROM C_Invoice)"
				+ " ||'L'||(SELECT " + count + " FROM C_InvoiceLine)"
				+ " ||'M'||(SELECT " + count + " FROM M_Transaction)"
				+ " ||'c'||(SELECT " + count + " FROM AD_Column WHERE EntityType NOT IN ('C','D'))"
				+ " ||'t'||(SELECT " + count + " FROM AD_Table WHERE EntityType NOT IN ('C','D'))"
				+ " ||'f'||(SELECT " + count + " FROM AD_Field WHERE EntityType NOT IN ('C','D'))"
				+ " FROM AD_System"; 
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				rs = pstmt.executeQuery ();
				if (rs.next ())
					s = rs.getString(1);
				setStatisticsInfo(s);
			}
			catch (Exception e) {
				log.log (Level.SEVERE, sql, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}
		return s;
	}	//	getStatisticsInfo
	
	/**
	 * 	Get Profile Info
	 * 	@param recalc recalculate
	 *	@return profile
	 */
	public String getProfileInfo (boolean recalc)
	{
		String s = super.getProfileInfo ();
		if (s == null || recalc)
		{
			String sql = "SELECT Value FROM AD_Client "
				+ "WHERE IsActive='Y' ORDER BY AD_Client_ID DESC";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuffer sb = new StringBuffer();
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				rs = pstmt.executeQuery ();
				while (rs.next ())
					sb.append(rs.getString(1)).append('|');
			}
			catch (Exception e) {
				log.log (Level.SEVERE, sql, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			s = sb.toString();
		}
		return s;
	}	//	getProfileInfo
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true/false
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Mandatory Values
		if (get_Value("IsAutoErrorReport") == null)
			setIsAutoErrorReport (true);
		//
		boolean userChange = Ini.isClient() &&
			(is_ValueChanged("Name")
			|| is_ValueChanged("UserName")
			|| is_ValueChanged("Password")
			|| is_ValueChanged("LDAPHost")
			|| is_ValueChanged("LDAPDomain")
			|| is_ValueChanged("CustomPrefix")
			);
		if (userChange)
		{
			String name = getName();
			if (name.equals("?") || name.length() < 2)
			{
				log.saveError("Error", "Define a unique System name (e.g. Company name) not " + name);
				return false;
			}
			if (getUserName().equals("?") || getUserName().length() < 2)
			{
				log.saveError("Error", "Use the same EMail address as in the Compiere Web Store");
				return false;
			}
			if (getPassword().equals("?") || getPassword().length() < 2)
			{
				log.saveError("Error", "Use the same Password as in the Compiere Web Store");
				return false;
			}
		}
		if (getSupportLevel() == null)
			setSupportLevel(SUPPORTLEVEL_Unsupported);
		//
		setInfo();
		s_system = this;	//	update singleton
		return true;
	}	//	beforeSave
	
	/**
	 * 	Save Record (ID=0)
	 * 	@return true if saved
	 */
	@Override
	public boolean save()
	{
		if (!beforeSave(false))
			return false;
		return saveUpdate();
	}	//	save

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		return "MSystem[" + getName()
			+ ",User=" + getUserName()
			+ ",ReleaseNo=" + getReleaseNo()
			+ "]";
	}	//	toString

	
	/**************************************************************************
	 * 	Check valididity
	 *	@return true if valid
	 */
	public boolean isValid()
	{
		if (getName() == null || getName().length() < 2)
		{
			log.log(Level.WARNING, "Name not valid: " + getName());
			return false;
		}
		if (getPassword() == null || getPassword().length() < 2)
		{
			log.log(Level.WARNING, "Password not valid: " + getPassword());
			return false;
		}
		if (getInfo() == null || getInfo().length() < 2)
		{
			log.log(Level.WARNING, "Need to run Migration once");
			return false;
		}
		return true;
	}	//	isValid

	/**
	 * 	Is there a PDF License
	 *	@return true if there is a PDF License
	 */
	public boolean isPDFLicense()
	{
		String key = getSummary();
		return key != null && key.length() > 25;
	}	//	isPDFLicense
	
	/**
	 * 	Get SupportLevel
	 *	@return Support Level
	 */
	@Override
	public String getSupportLevel()
	{
		String sl = null;
		if (get_ColumnIndex("SupportLevel") != -1)
			sl = super.getSupportLevel();
		if (sl == null)
			return SUPPORTLEVEL_Unsupported;
		return sl;
	}	//	getSupportLevel
	
	/**
	 * 	Get Record_ID
	 *	@return record ID
	 */
	@Override
	public int getRecord_ID()
	{
		if (get_ColumnIndex("Record_ID") == -1)
			return -1;
		return super.getRecord_ID ();
	}	//	getRecord_ID
	
	/**
	 * 	Get SupportUnits
	 *	@return SupportUnits
	 */
	@Override
	public int getSupportUnits()
	{
		if (get_ColumnIndex("SupportUnits") == -1)
			return 0;
		return super.getSupportUnits ();
	}	//	getSupportUnits
	
	/**
	 * 	Get System Status
	 *	@return	system status
	 */
	@Override
	public String getSystemStatus()
	{
		String ss = null;
		if (get_ColumnIndex("SystemStatus") != -1)
			ss = super.getSystemStatus();
		if (ss == null)
			ss = SYSTEMSTATUS_Evaluation;
		return ss;
	}	//	getSystemStatus
	
	
	/**************************************************************************
	 * 	Set/Derive Info if more then a day old
	 * 	@return true if set
	 */
	public boolean setInfo()
	{
	//	log.severe("setInfo");
		if (!TimeUtil.getDay(getUpdated()).before(TimeUtil.getDay(null)))
			return false;	
		try
		{
			setDBInfo();
			setInternalUsers();
			if (isAllowStatistics())
			{
				setStatisticsInfo(getStatisticsInfo(true));
				setProfileInfo(getProfileInfo(true));
			}
		}
		catch (Exception e)
		{
			setSupportUnits(9999);
			setInfo(e.getLocalizedMessage());
			log.log(Level.SEVERE, "", e);
		}
		return true;
	}	//	setInfo
	
	/**
	 * 	Set Internal User Count
	 */
	private void setInternalUsers()
	{
		String sql = "SELECT COUNT(DISTINCT (u.AD_User_ID)) AS iu "
			+ "FROM AD_User u"
			+ " INNER JOIN AD_User_Roles ur ON (u.AD_User_ID=ur.AD_User_ID) "
			+ "WHERE u.AD_Client_ID<>11"			//	no Demo
			+ " AND u.AD_User_ID NOT IN (0,100)";	//	no System/SuperUser
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				int internalUsers = rs.getInt (1);
				setSupportUnits(internalUsers);
			}
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	setInternalUsers

	/**
	 * 	Set DB Info
	 */
	private void setDBInfo()
	{
		if (!DB.isRemoteObjects())
		{
			String dbAddress = getDBAddress(true);
			setDBAddress(dbAddress);
		}
		//
		if (!Ini.isClient())
		{
			int noProcessors = Runtime.getRuntime().availableProcessors();
			setNoProcessors(noProcessors);
		}
		//
		try
		{
			Trx trx = Trx.get("getDatabaseMetaData");
			DatabaseMetaData md = trx.getConnection().getMetaData();
			String db1 = md.getDatabaseProductName();
			String db2 = md.getDatabaseProductVersion();
			if (db2.startsWith(db1))
				db1 = db2;
			else
				db1 += "-" + db2;
			int fieldLength = p_info.getFieldLength("DBInstance");
			if (db1.length() > fieldLength)
			{
				db1 = Util.replace (db1, "Database ", "");
				db1 = Util.replace (db1, "Version ", "");
				db1 = Util.replace (db1, "Edition ", "");
				db1 = Util.replace (db1, "Release ", "");
			}
			db1 = Util.removeCRLF(db1);
			if (db1.length() > fieldLength)
				db1 = db1.substring(0, fieldLength);
			setDBInstance(db1);
			trx.close();
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "MetaData", e);
		}
	}	//	setDBInfo
		
	
	/**
	 * 	Print info
	 */
	public void info()
	{
		if (!CLogMgt.isLevelFine())
			return;
		//	OS
	//	OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
	//	log.fine(os.getName() + " " + os.getVersion() + " " + os.getArch() 
	//		+ " Processors=" + os.getAvailableProcessors());
		//	Runtime
		RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
		log.fine(rt.getName() + " (" + rt.getVmVersion() + ") Up=" + TimeUtil.formatElapsed(rt.getUptime()));
		//	Memory
		if (CLogMgt.isLevelFiner())
		{
			List<MemoryPoolMXBean> list = ManagementFactory.getMemoryPoolMXBeans();
			Iterator<MemoryPoolMXBean> it = list.iterator();
			while (it.hasNext())
			{
				MemoryPoolMXBean pool = it.next();
				log.finer(pool.getName() + " " + pool.getType() 
					+ ": " + new CMemoryUsage(pool.getUsage()));
			}
		}
		else
		{
			MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
			log.fine("VM: " + new CMemoryUsage(memory.getNonHeapMemoryUsage()));
			log.fine("Heap: " + new CMemoryUsage(memory.getHeapMemoryUsage()));
		}
		//	Thread
		ThreadMXBean th = ManagementFactory.getThreadMXBean();
		log.fine("Threads=" + th.getThreadCount()
			+ ", Peak=" + th.getPeakThreadCount()
			+ ", Demons=" + th.getDaemonThreadCount()
			+ ", Total=" + th.getTotalStartedThreadCount()
		);
	}	//	info
	
	
	/**
	 * 	Test
	 *	@param args
	 */
	public static void main (String[] args)
	{
		new MSystem();
	}	//	main
	
	/**
	 * Only save the license
	 * @return true if success; otherwise false
	 */
	public boolean saveLicenseOnly()
	{
		// Create the update statement for license
		StringBuffer sql = new StringBuffer ("UPDATE ");
		sql.append(p_info.getTableName())
		   .append(" SET Name=").append(DB.TO_STRING(getName()))
		   .append(", UserName=").append(DB.TO_STRING(getUserName()))
		   .append(", Password=").append(DB.TO_STRING(getPassword()))
		   .append(", Summary=").append(DB.TO_STRING(getSummary()))
		   .append(" WHERE AD_System_ID=").append(getAD_System_ID());

		log.fine(sql.toString());
		
		// Send to database
		int no = DB.executeUpdate((Trx) null, sql.toString());
		return no == 1;
	}  // saveLicenseOnly()
}	//	MSystem
