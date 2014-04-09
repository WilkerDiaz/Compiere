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

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;


/**
 *	LDAP Server Model
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class MLdapProcessor extends X_AD_LdapProcessor implements CompiereProcessor
{
    /** Logger for class MLdapProcessor */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MLdapProcessor.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Active LDAP Server
	 *	@return array of Servers
	 */
	public static MLdapProcessor[] getActive(Ctx ctx)
	{
		ArrayList<MLdapProcessor> list = new ArrayList<MLdapProcessor>();
		String sql = "SELECT * FROM AD_LdapProcessor WHERE IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MLdapProcessor (ctx, rs, null));
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MLdapProcessor[] retValue = new MLdapProcessor[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getActive
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MLdapProcessor.class);
	
	/**************************************************************************
	 * 	Ldap Processor
	 *	@param ctx context
	 *	@param AD_LdapProcessor_ID id
	 *	@param trx transaction
	 */
	public MLdapProcessor(Ctx ctx, int AD_LdapProcessor_ID, Trx trx)
	{
		super (ctx, AD_LdapProcessor_ID, trx);
	}	//	MLdapProcessor

	/**
	 * 	Ldap Processor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MLdapProcessor(Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MLdapProcessor
	
	/** Array of Clients		*/
	private MClient[]		m_clients = null;
	/** Array of Interest Areas	*/
	private MInterestArea[]	m_interests = null;
	
	private int				m_auth = 0;
	private int				m_ok = 0;
	private int				m_error = 0;
	
	/**
	 * 	Get Server ID
	 *	@return id
	 */
	public String getServerID ()
	{
		return "Ldap" + get_ID();
	}	//	getServerID

	/**
	 * 	Get Info
	 *	@return info
	 */
	public String getInfo()
	{
		return "Auth=" + m_auth 
			+ ", OK=" + m_ok + ", Error=" + m_error;
	}	//	getInfo
	
	/**
	 * 	Get Date Next Run
	 *	@param requery requery
	 *	@return date next run
	 */
	public Timestamp getDateNextRun (boolean requery)
	{
		if (requery)
			load(get_Trx());
		return getDateNextRun();
	}	//	getDateNextRun

	/**
	 * 	Get Logs
	 *	@return logs
	 */
	public CompiereProcessorLog[] getLogs ()
	{
		ArrayList<MLdapProcessorLog> list = new ArrayList<MLdapProcessorLog>();
		String sql = "SELECT * "
			+ "FROM AD_LdapProcessorLog "
			+ "WHERE AD_LdapProcessor_ID=? " 
			+ "ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getAD_LdapProcessor_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MLdapProcessorLog (getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MLdapProcessorLog[] retValue = new MLdapProcessorLog[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getLogs

	/**
	 * 	Delete old Request Log
	 *	@return number of records
	 */
	public int deleteLog()
	{
		if (getKeepLogDays() < 1)
			return 0;
		String sql = "DELETE FROM AD_LdapProcessorLog "
			+ "WHERE AD_LdapProcessor_ID= ? AND addDays(Created,?) < SysDate";
		Object[] params = new Object[]{getAD_LdapProcessor_ID(),getKeepLogDays()};
		int no = DB.executeUpdate(get_Trx(), sql,params);
		return no;
	}	//	deleteLog

	/**
	 * 	Get Frequency (n/a)
	 *	@return 1
	 */
	public int getFrequency()
	{
		return 1;
	}	//	getFrequency

	/**
	 * 	Get Frequency Type (n/a)
	 *	@return minute
	 */
	public String getFrequencyType()
	{
		return X_R_RequestProcessor.FREQUENCYTYPE_Minute;
	}	//	getFrequencyType
	
	/**
	 * 	Get AD_Schedule_ID
	 *	@return 0
	 */
	public int getAD_Schedule_ID()
	{
		return 0;
	}	//	getAD_Schedule_ID
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MLdapProcessor[");
		sb.append (get_ID()).append ("-").append (getName())
			.append (",Port=").append (getLdapPort())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	/**************************************************************************
	 * 	Authenticate and Authorize
	 *  @param ldapUser MLdapUser object
	 *	@param usr user name
	 *	@param o organization = Client Name
	 *	@param ou optional organization unit = Interest Group Value 
	 *		or Aa<M_Product_ID>aA = Active Asset of Product of user
	 *	@param remoteHost remote host name
	 *	@param remoteAddr remote host ip address
	 *  @return ldapUser MLdapUser with updated information
	 */
	public MLdapUser authenticate (MLdapUser ldapUser, String usr, String o, String ou,
				String remoteHost, String remoteAddr)
	{
		// Ensure something to return
		if (ldapUser == null)
			ldapUser = new MLdapUser();
		
		String error = null;
		String info = null;

		//	User
		if (usr == null || usr.trim().length () == 0)
		{
			error = "@NotFound@: User (empty)";
			ldapUser.setErrorString(error);
			m_error++;
			log.warning (error);
			return ldapUser;
		}
		usr = usr.trim();
		//	Client
		if (o == null || o.length () == 0)
		{
			error = "@NotFound@: O (Tenant Key missing)";
			ldapUser.setErrorString(error);
			m_error++;
			log.warning (error);
			return ldapUser;
		}
		int AD_Client_ID = findClient(o);
		if (AD_Client_ID == 0)
		{
			error = "@NotFound@: O=" + o + " (Tenant Key)";
			ldapUser.setErrorString(error);
			m_error++;
			log.config (error);
			return ldapUser;
		}
		//	Optional Interest Area or Asset
		int R_InterestArea_ID = 0;
		int M_Product_ID = 0;	//	Product of Asset
		if (ou != null && ou.length () > 0)
		{
			if (ou.startsWith("Aa") && ou.endsWith("aA"))
			{
				try
				{
					String s = ou.substring(2,ou.length()-2);
					M_Product_ID = Integer.parseInt(s);
				}
				catch (Exception e)
				{
				}
			}
			else
				R_InterestArea_ID = findInterestArea (AD_Client_ID, ou);
			if (R_InterestArea_ID == 0 && M_Product_ID == 0)
			{
				error = "@NotFound@ OU=" + ou;
				ldapUser.setErrorString(error);
				m_error++;
				log.config (error);
				return ldapUser;
			}
		}

		m_auth++;
		//	Query 1 - Validate User
		int AD_User_ID = 0;
		String Value = null;
		String LdapUser = null;
		String EMail = null;
		String Name = null;
		String Password = null;
		boolean isActive = false;
		String EMailVerify = null;	//	 is timestamp
		boolean isUnique = false;
		//
		String sql = "SELECT AD_User_ID, Value, LdapUser, EMail,"	//	1..4
			+ " Name, Password, IsActive, EMailVerify "
			+ "FROM AD_User "
			+ "WHERE AD_Client_ID=? AND (EMail=? OR Value=? OR LdapUser=?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, AD_Client_ID);
			pstmt.setString (2, usr);
			pstmt.setString (3, usr);
			pstmt.setString (4, usr);
			rs = pstmt.executeQuery ();
			if (rs.next())
			{
				AD_User_ID = rs.getInt (1);
				Value = rs.getString (2);
				LdapUser = rs.getString (3);
				EMail = rs.getString (4);
				//
				Name = rs.getString (5);
				Password = rs.getString (6);
				isActive = "Y".equals (rs.getString (7));
				EMailVerify	= rs.getString (8);
				isUnique = rs.next();
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
			error = "System Error";
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		if (error != null)
		{
			m_error++;
			ldapUser.setErrorString(error);
			return ldapUser;
		}
		//
		if (AD_User_ID == 0)
		{
			error = "@NotFound@ User=" + usr;
			info = "User not found - " + usr;
		}
		else if (!isActive)
		{
			error = "@NotFound@ User=" + usr;
			info = "User not active - " + usr;
		}
		else if (EMailVerify == null)
		{
			error = "@UserNotVerified@ User=" + usr;
			info = "User EMail not verified - " + usr;
		}
		else if (usr.equalsIgnoreCase(LdapUser))
			info = "User verified - Ldap=" + usr 
				+ (isUnique ? "" : " - Not Unique");
		else if (usr.equalsIgnoreCase(Value)) 
			info = "User verified - Value=" + usr 
				+ (isUnique ? "" : " - Not Unique");
		else if (usr.equalsIgnoreCase(EMail)) 
			info = "User verified - EMail=" + usr 
				+ (isUnique ? "" : " - Not Unique");
		else 
			info = "User verified ?? " + usr
				+ " - Name=" + Name 
				+ ", Ldap=" + LdapUser + ", Value=" + Value
				+ (isUnique ? "" : " - Not Unique");

		//	Error
		if (error != null)	//	should use Language of the User
		{
			logAccess (AD_Client_ID, AD_User_ID, R_InterestArea_ID, 0, info, error,
						remoteHost, remoteAddr);
			ldapUser.setErrorString(Msg.translate (getCtx(), error));
			return ldapUser;
		}
		//	User Info
		ldapUser.setOrg(o);
		ldapUser.setOrgUnit(ou);
		ldapUser.setUserId(usr);
		ldapUser.setPassword(Password);
		//	Done
		if (R_InterestArea_ID == 0 && M_Product_ID == 0)
		{
			logAccess (AD_Client_ID, AD_User_ID, 0, 0, info, null,
						remoteHost, remoteAddr);
			return ldapUser;
		}
		
		if (M_Product_ID != 0)
			return authenticateAsset (ldapUser, 
					AD_User_ID, usr, M_Product_ID, 
					AD_Client_ID, remoteHost, remoteAddr);
		
		return authenticateSubscription(ldapUser, 
				AD_User_ID, usr, R_InterestArea_ID, 
				AD_Client_ID, remoteHost, remoteAddr);
	}	//	authenticate
	
	/**
	 * 	Authenticate Subscription
	 *	@param ldapUser user
	 *	@param AD_User_ID user id
	 *	@param usr user authentification (email, ...)
	 *	@param R_InterestArea_ID interest area
	 *	@param AD_Client_ID client
	 *	@param remoteHost remote info
	 *	@param remoteAddr remote info
	 *	@return user with error message set if error
	 */
	private MLdapUser authenticateSubscription(MLdapUser ldapUser, 
			int AD_User_ID, String usr, int R_InterestArea_ID, 
			int AD_Client_ID, String remoteHost, String remoteAddr)
	{
		String error = null;
		String info = null;

		//	Query 2 - Validate Subscription
		String OptOutDate = null;
		boolean found = false;
		boolean isActive = false;
		String sql = "SELECT IsActive, OptOutDate "
			+ "FROM R_ContactInterest "
			+ "WHERE R_InterestArea_ID=? AND AD_User_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, R_InterestArea_ID);
			pstmt.setInt (2, AD_User_ID);
			rs = pstmt.executeQuery ();
			if (rs.next())
			{
				found = true;
				isActive = "Y".equals (rs.getString (1));
				OptOutDate	= rs.getString (2);
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
			error = "System Error (2)";
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		//	System Error
		if (error != null)
		{
			m_error++;
			ldapUser.setErrorString(error);
			return ldapUser;
		}
		
		if (!found)
		{
			error = "@UserNotSubscribed@ User=" + usr;
			info = "No User Interest - " + usr 
				+ " - R_InterestArea_ID=" + R_InterestArea_ID;
		}
		else if (OptOutDate != null)
		{
			error = "@UserNotSubscribed@ User=" + usr + " @OptOutDate@=" + OptOutDate;
			info = "Opted out - " + usr + " - OptOutDate=" + OptOutDate;
		}
		else if (!isActive)
		{
			error = "@UserNotSubscribed@ User=" + usr;
			info = "User Interest Not Active - " + usr; 
		}
		else
			info = "User subscribed - " + usr;
		
		
		if (error != null)	//	should use Language of the User
		{
			logAccess (AD_Client_ID, AD_User_ID, R_InterestArea_ID, 0, info, error, 
						remoteHost, remoteAddr);
			ldapUser.setErrorString(Msg.translate (getCtx(), error));
			return ldapUser;
		}
		//	Done
		logAccess (AD_Client_ID, AD_User_ID, R_InterestArea_ID, 0, info, null,
					remoteHost, remoteAddr);
		return ldapUser;
	}	//	authenticateSubscription

	/**
	 * 	Authenticate Product Asset
	 *	@param ldapUser user
	 *	@param AD_User_ID user id
	 *	@param usr user authentification (email, ...)
	 *	@param M_Product_ID product
	 *	@param AD_Client_ID client
	 *	@param remoteHost remote info
	 *	@param remoteAddr remote info
	 *	@return user with error message set if error
	 */
	private MLdapUser authenticateAsset(MLdapUser ldapUser, 
			int AD_User_ID, String usr, int M_Product_ID, 
			int AD_Client_ID, String remoteHost, String remoteAddr)
	{
		String error = null;
		String info = null;

		//	Query 2 - Validate Asset
		MAsset asset = null;
		String sql = "SELECT * "
			+ "FROM A_Asset "
			+ "WHERE M_Product_ID=?"
			+ " AND AD_User_ID=?";		//	only specific user
		//	Will have problems with multiple assets
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, M_Product_ID);
			pstmt.setInt (2, AD_User_ID);
			rs = pstmt.executeQuery ();
			if (rs.next())
			{
				asset = new MAsset(getCtx(), rs, get_Trx());
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
			error = "System Error (3)";
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//	System Error
		if (error != null)
		{
			m_error++;
			ldapUser.setErrorString(error);
			return ldapUser;
		}
		int A_Asset_ID = 0;
		if (asset == null)
		{
			error = "@UserNoAsset@ User=" + usr;
			info = "No Asset - " + usr + " - " + M_Product_ID;
		}
		else if (!asset.isActive())
		{
			A_Asset_ID = asset.getA_Asset_ID();
			error = "@UserNoAsset@ User=" + usr;
			info = "Asset not active - " + usr; 
		}
		else if (!asset.isActive(true))
		{
			A_Asset_ID = asset.getA_Asset_ID();
			error = "@UserNoAsset@ User=" + usr + " @GuaranteeDate@=" + asset.getGuaranteeDate();
			info = "Expired - " + usr + " - GuaranteeDate=" + asset.getGuaranteeDate();
		}
		else
			info = "Asset - " + usr;
		
		
		if (error != null)	//	should use Language of the User
		{
			logAccess (AD_Client_ID, AD_User_ID, 0, A_Asset_ID, info, error, 
						remoteHost, remoteAddr);
			ldapUser.setErrorString(Msg.translate (getCtx(), error));
			return ldapUser;
		}
		//	Done OK
		MLdapAccess log = logAccess (AD_Client_ID, AD_User_ID, 0, asset.getA_Asset_ID(), info, null,
					remoteHost, remoteAddr);
		MAssetDelivery ad = new MAssetDelivery(asset, null, log.toString(), AD_User_ID);
		ad.setRemote_Host(remoteHost);
		ad.setRemote_Addr(remoteAddr);
		ad.save();
		return ldapUser;
	}	//	authenticateAsset

	
	/**
	 * 	Find Client
	 *	@param client client name
	 *	@return AD_Client_ID
	 */
	private int findClient (String client)
	{
		if (m_clients == null)
			m_clients = MClient.getAll(getCtx());
		for (MClient element : m_clients) {
			if ((client.equalsIgnoreCase (element.getValue())))
				return element.getAD_Client_ID ();
		}
		return 0;
	}	//	findClient
	
	/**
	 * 	Find Interest Area
	 *	@param interset Name client name
	 *	@return AD_Client_ID
	 */
	private int findInterestArea (int AD_Client_ID, String interestArea)
	{
		if (m_interests == null)
			m_interests = MInterestArea.getAll(getCtx());
		for (MInterestArea element : m_interests) {
			if (AD_Client_ID == element.getAD_Client_ID()
				&& interestArea.equalsIgnoreCase (element.getValue ()))
				return element.getR_InterestArea_ID();
		}
		return 0;
	}	//	findInterestArea
	
	/**
	 * 	Log Access
	 * 	@param AD_Client_ID client
	 *	@param AD_User_ID user
	 *	@param R_InterestArea_ID interest area
	 *	@param info info
	 *	@param error error
	 */
	private MLdapAccess logAccess (int AD_Client_ID,
		int AD_User_ID, int R_InterestArea_ID, int A_Asset_ID,
		String info, String error,
		String remoteHost, String remoteAddr)
	{
		if (error != null)
		{
			log.log (Level.CONFIG, info);
			m_error++;
		}
		else
		{
			log.log (Level.INFO, info);
			m_ok++;
		}
		//
		MLdapAccess access = new MLdapAccess (getCtx(), 0, null);
		access.setAD_Client_ID (AD_Client_ID);
		access.setAD_Org_ID(0);
		access.setAD_LdapProcessor_ID(getAD_LdapProcessor_ID());
		access.setAD_User_ID (AD_User_ID);
		access.setR_InterestArea_ID (R_InterestArea_ID);
		access.setA_Asset_ID(A_Asset_ID);
		access.setRemote_Host(remoteHost);
		access.setRemote_Addr(remoteAddr);

		access.setIsError (error != null);
		access.setSummary (info);
		access.save();
		return access;
	}	//	logAccess

}	//	MLdapProcessor
