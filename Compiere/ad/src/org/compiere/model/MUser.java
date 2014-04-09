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

import javax.mail.internet.*;
import javax.naming.*;
import javax.naming.directory.*;

import org.compiere.*;
import org.compiere.util.*;

import com.compiere.client.SysEnv;

/**
 *  User Model
 *
 *  @author Jorg Janke
 *  @version $Id: MUser.java 9434 2010-12-16 20:02:17Z rthng $
 */
public class MUser extends X_AD_User
{
    /** Logger for class MUser */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MUser.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get active Users of BPartner
	 *	@param ctx context
	 *	@param C_BPartner_ID id
	 *	@return array of users
	 */
	public static MUser[] getOfBPartner (Ctx ctx, int C_BPartner_ID)
	{
		ArrayList<MUser> list = new ArrayList<MUser>();
		String sql = "SELECT * FROM AD_User WHERE C_BPartner_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, C_BPartner_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MUser(ctx, rs, null));
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MUser[] retValue = new MUser[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfBPartner

	/**
	 * 	Get Users with Role
	 *	@param role role
	 *	@return array of users
	 */
	public static MUser[] getWithRole (MRole role)
	{
		ArrayList<MUser> list = new ArrayList<MUser>();
		String sql = "SELECT * FROM AD_User u "
			+ "WHERE u.IsActive='Y'"
			+ " AND EXISTS (SELECT * FROM AD_User_Roles ur "
				+ "WHERE ur.AD_User_ID=u.AD_User_ID AND ur.AD_Role_ID=? AND ur.IsActive='Y')";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, role.getAD_Role_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MUser(role.getCtx(), rs, null));
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MUser[] retValue = new MUser[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getWithRole


	/**
	 * 	Get User (cached)
	 * 	Also loads Administrator (0)
	 *	@param ctx context
	 *	@param AD_User_ID id
	 *	@return user
	 */
	public static MUser get (Ctx ctx, int AD_User_ID)
	{
		Integer key = Integer.valueOf(AD_User_ID);
		MUser retValue = s_cache.get(ctx, key);
		if (retValue == null)
		{
			retValue = new MUser (ctx, AD_User_ID, null);
			if (AD_User_ID == 0)
			{
				Trx trx = null;
				retValue.load(trx);	//	load System Record
			}
			s_cache.put(key, retValue);
		}
		return retValue;
	}	//	get

	/**
	 * 	Get Current User (cached)
	 *	@param ctx context
	 *	@return user
	 */
	public static MUser get (Ctx ctx)
	{
		return get(ctx, ctx.getAD_User_ID());
	}	//	get

	/**
	 * 	Get active User with name/pwd
	 *	@param ctx context
	 *	@param name name
	 *	@param password password
	 *	@return user or null
	 */
	public static MUser get (Ctx ctx, String name, String password, Trx trx)
	{
		if ((name == null) || (name.length() == 0) || (password == null) || (password.length() == 0))
		{
			s_log.warning ("Invalid Name/Password = " + name + "/" + password);
			return null;
		}
		int AD_Client_ID = ctx.getAD_Client_ID();

		MUser retValue = null;
		String sql = "SELECT * FROM AD_User "
			+ "WHERE Name=? AND Password=? AND IsActive='Y' AND AD_Client_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setString (1, name);
			pstmt.setString (2, password);
			pstmt.setInt(3, AD_Client_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MUser (ctx, rs, trx);
				if (rs.next())
					s_log.warning ("More then one user with Name/Password = " + name);
			}
			else
				s_log.fine("No record");
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get

	/**
	 * 	Get User from email and Name
	 *	@param ctx context
	 *	@param email email
	 *  @param name name
	 *	@return user or null
	 */
	public static MUser getUser (Ctx ctx, String email,String Name, Trx trx)
	{
		if ((Name == null)|| (Name.length()==0))
			return null;

		int AD_Client_ID = ctx.getAD_Client_ID();
		MUser retValue = null;
		String sql = "SELECT * FROM AD_User "
			+ "WHERE UPPER(Name)=? AND AD_Client_ID=? ";
		if(email !=null && email.length()==0)
			sql += " AND UPPER(EMail) = ? ";
		else
			sql += " AND EMail IS NULL ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setString(1, Name.toUpperCase());
			pstmt.setInt(2, AD_Client_ID);
			if(email !=null && email.length()==0)	
				pstmt.setString (3, email.toUpperCase());
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MUser (ctx, rs, trx);
				if (rs.next())
					s_log.warning ("More then one user with EMail = " + email);
			}
			else
				s_log.fine("No record");
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get
	
	/**
	 * 	Get User from email
	 *	@param ctx context
	 *	@param email email
	 *	@return user or null
	 */
	public static MUser get (Ctx ctx, String email, Trx trx)
	{
		if ((email == null) || (email.length() == 0))
			return null;

		int AD_Client_ID = ctx.getAD_Client_ID();
		MUser retValue = null;
		String sql = "SELECT * FROM AD_User "
			+ "WHERE UPPER(EMail)=? AND AD_Client_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setString (1, email.toUpperCase());
			pstmt.setInt(2, AD_Client_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MUser (ctx, rs, trx);
				if (rs.next())
					s_log.warning ("More then one user with EMail = " + email);
			}
			else
				s_log.fine("No record");
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get

	/**
	 *  Get Name of AD_User
	 *  @param  AD_User_ID   System User
	 *  @return Name of user or ?
	 */
	public static String getNameOfUser (int AD_User_ID)
	{
		String name = "?";
		//	Get ID
		String sql = "SELECT Name FROM AD_User WHERE AD_User_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_User_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				name = rs.getString(1);
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return name;
	}	//	getNameOfUser


	/**
	 * 	User is SalesRep
	 *	@param AD_User_ID user
	 *	@return true if sales rep
	 */
	public static boolean isSalesRep (int AD_User_ID)
	{
		if (AD_User_ID == 0)
			return false;
		String sql = "SELECT MAX(AD_User_ID) FROM AD_User u"
			+ " INNER JOIN C_BPartner bp ON (u.C_BPartner_ID=bp.C_BPartner_ID) "
			+ "WHERE bp.IsSalesRep='Y' AND AD_User_ID=?";
		int no = QueryUtil.getSQLValue(null, sql, AD_User_ID);
		return no == AD_User_ID;
	}	//	isSalesRep

	/**
	 * 	Get AD_User_ID
	 *	@param email mail
	 *	@param AD_Client_ID client
	 *	@return user id or 0
	 */
	public static int getAD_User_ID (String email, int AD_Client_ID)
	{
		int AD_User_ID = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT AD_User_ID FROM AD_User "
			+ "WHERE UPPER(EMail)=?"
			+ " AND AD_Client_ID=?";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString (1, email.toUpperCase());
			pstmt.setInt (2, AD_Client_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				AD_User_ID = rs.getInt(1);
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, email, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return AD_User_ID;
	}	//	getAD_User_ID

	/**
	 *  Get Email of AD_User
	 *	@param AD_User_ID user
	 *	@param AD_Client_ID client
	 *  @return Email of user or ?
	 */
	public static String getEmailOfUser (int AD_User_ID, int AD_Client_ID)
	{
		String email = "?";
		//	Get EMail
		String sql = "SELECT EMail FROM AD_User "
		+ "WHERE AD_User_ID=? AND IsActive='Y' AND AD_Client_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_User_ID);
			pstmt.setInt (2, AD_Client_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				email = rs.getString(1);
				if ((email == null) || "".equals(email))
					email = "?";
			}
			else
				s_log.fine("No record");
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return email;
	}	//	getEmailOfUser



	/**	Cache					*/
	private static final CCache<Integer,MUser> s_cache = new CCache<Integer,MUser>("AD_User", 30, 60);
	/**	Static Logger			*/
	private static final CLogger	s_log	= CLogger.getCLogger (MUser.class);


	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_User_ID id
	 * 	@param trx transaction
	 */
	public MUser (Ctx ctx, int AD_User_ID, Trx trx)
	{
		super (ctx, AD_User_ID, trx);	//	0 is also System
		if (AD_User_ID == 0)
		{
			setIsFullBPAccess (true);
			setNotificationType(NOTIFICATIONTYPE_EMail);
			setIsEMailBounced (false);
		}
	}	//	MUser

	/**
	 * 	Parent Constructor
	 *	@param partner partner
	 */
	public MUser (X_C_BPartner partner)
	{
		this (partner.getCtx(), 0, partner.get_Trx());
		setClientOrg(partner);
		setC_BPartner_ID (partner.getC_BPartner_ID());
		setName(partner.getName());
	}	//	MUser

	/**
	 * 	Load Constructor
	 * 	@param ctx context
	 * 	@param rs current row of result set to be loaded
	 * 	@param trx transaction
	 */
	public MUser (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MUser

	/**	Roles of User with Org	*/
	private MRole[] 			m_roles = null;
	/**	Roles of User with Org	*/
	private int		 			m_rolesAD_Org_ID = -1;
	/** Is Administrator		*/
	private Boolean				m_isAdministrator = null;
	/** Is System Admin			*/
	private Boolean				m_isSystemAdministrator = null;
	/** User Access Rights		*/
	private X_AD_UserBPAccess[]	m_bpAccess = null;
	/** User Preference			*/
	private MUserPreference		m_preference = null;


	/**
	 * 	Get Value - 7 bit lower case alpha numerics max length 8
	 *	@return value
	 */
	@Override
	public String getValue()
	{
		String s = super.getValue();
		if (s != null)
			return s;
		setValue(null);		//	set the 8 character value
		return super.getValue();
	}	//	getValue

	/**
	 * 	Set Value - 7 bit lower case alpha numerics max length 8
	 *	@param Value
	 */
	@Override
	public void setValue(String Value)
	{
		if ((Value == null) || (Value.trim().length () == 0))
			Value = getLDAPUser();
		if ((Value == null) || (Value.length () == 0))
			Value = getName();
		if ((Value == null) || (Value.length () == 0))
			Value = "noname";
		//
		String result = cleanValue(Value);
		if (result.length() > 8)
		{
			String first = getName(Value, true);
			String last = getName(Value, false);
			if (last.length() > 0)
			{
				String temp = last;
				if (first.length() > 0)
					temp = first.substring (0, 1) + last;
				result = cleanValue(temp);
			}
			else
				result = cleanValue(first);
		}
		if (result.length() > 8)
			result = result.substring (0, 8);
		super.setValue(result);
	}	//	setValue

	/**
	 * 	Clean Value
	 *	@param value value
	 *	@return lower case cleaned value
	 */
	private String cleanValue (String value)
	{
		char[] chars = value.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (char ch : chars) {
			ch = Character.toLowerCase (ch);
			if (((ch >= '0') && (ch <= '9'))		//	digits
				|| ((ch >= 'a') && (ch <= 'z')))	//	characters
				sb.append(ch);
		}
		return sb.toString ();
	}	//	cleanValue

	/**
	 * 	Get First Name
	 *	@return first name
	 */
	public String getFirstName()
	{
		return getName (getName(), true);
	}	//	getFirstName

	/**
	 * 	Get Last Name
	 *	@return first name
	 */
	public String getLastName()
	{
		return getName (getName(), false);
	}	//	getLastName

	/**
	 * 	Get First/Last Name
	 *	@param name name
	 *	@param getFirst if true first name is returned
	 *	@return first/last name
	 */
	private String getName (String name, boolean getFirst)
	{
		if ((name == null) || (name.length () == 0))
			return "";
		String first = null;
		String last = null;
		//	Janke, Jorg R - Jorg R Janke
		//	double names not handled gracefully nor titles
		//	nor (former) aristrocratic world de/la/von
		boolean lastFirst = name.indexOf(',') != -1;
		StringTokenizer st = null;
		if (lastFirst)
			st = new StringTokenizer(name, ",");
		else
			st = new StringTokenizer(name, " ");
		while (st.hasMoreTokens())
		{
			String s = st.nextToken().trim();
			if (lastFirst)
			{
				if (last == null)
					last = s;
				else if (first == null)
					first = s;
			}
			else
			{
				if (first == null)
					first = s;
				else
					last = s;
			}
		}
		if (getFirst)
		{
			if (first == null)
				return "";
			return first.trim();
		}
		if (last == null)
			return "";
		return last.trim();
	}	//	getName


	/**
	 * 	Add to Description
	 *	@param description description to be added
	 */
	public void addDescription (String description)
	{
		if ((description == null) || (description.length() == 0))
			return;
		String descr = getDescription();
		if ((descr == null) || (descr.length() == 0))
			setDescription (description);
		else
			setDescription (descr + " - " + description);
	}	//	addDescription

	/**
	 * 	Get User Preference
	 *	@return user preference
	 */
	public MUserPreference getPreference()
	{
		if (m_preference == null)
			m_preference = MUserPreference.getOfUser(this, true);
		return m_preference;
	}	//	getPreference

	/**
	 * 	String Representation
	 *	@return Info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MUser[")
			.append(get_ID())
			.append(",Name=").append(getName())
			.append(",EMailUserID=").append(getEMailUser())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Is it an Online Access User
	 *	@return true if it has an email and password
	 */
	public boolean isOnline ()
	{
		if ((getEMail() == null) || (getPassword() == null))
			return false;
		return true;
	}	//	isOnline

	/**
	 * 	Set EMail - reset validation
	 *	@param EMail email
	 */
	@Override
	public void setEMail(String EMail)
	{
		super.setEMail (EMail);
		setEMailVerifyDate (null);
	}	//	setEMail

	/**
	 * 	Convert EMail
	 *	@return Valid Internet Address
	 */
	public InternetAddress getInternetAddress ()
	{
		String email = getEMail();
		if ((email == null) || (email.length() == 0))
			return null;
		try
		{
			InternetAddress ia = new InternetAddress (email, true);
			if (ia != null)
				ia.validate();	//	throws AddressException
			return ia;
		}
		catch (AddressException ex)
		{
			log.warning(email + " - " + ex.getLocalizedMessage());
		}
		return null;
	}	//	getInternetAddress

	/**
	 * 	Validate Email (does not work).
	 * 	Check DNS MX record
	 * 	@param ia email
	 *	@return error message or ""
	 */
	private String validateEmail (InternetAddress ia)
	{
		if (ia == null)
			return "NoEmail";
		if (true)
			return null;

		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
	//	env.put(Context.PROVIDER_URL, "dns://admin.compiere.org");
		try
		{
			DirContext ctx = new InitialDirContext(env);
		//	Attributes atts = ctx.getAttributes("admin");
			Attributes atts = ctx.getAttributes("dns://admin.compiere.org", new String[] {"MX"});
			NamingEnumeration<? extends Attribute> en = atts.getAll();
	//		NamingEnumeration en = ctx.list("compiere.org");
			while (en.hasMore())
			{
				System.out.println(en.next());
			}
			/**/
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		return null;
	}	//	validateEmail

	/**
	 * 	Is the email valid
	 * 	@return return true if email is valid (artificial check)
	 */
	public boolean isEMailValid()
	{
		return !isEMailBounced()
			&& (validateEmail(getInternetAddress()) == null);
	}	//	isEMailValid

	/**
	 * 	Could we send an email
	 * 	@return true if EMail Uwer/PW exists
	 */
	public boolean isCanSendEMail()
	{
		if (isEMailBounced())
			return false;
		String s = getEMailUser();
		if ((s == null) || (s.length() == 0))
			return false;
		s = getEMailUserPW();
		return (s != null) && (s.length() > 0);
	}	//	isCanSendEMail

	/**
	 * 	Get EMail Validation Code
	 *	@return code
	 */
	public String getEMailVerifyCode()
	{
		String email = getEMail();
		if (email == null)
			email = getName();
		long code = getAD_User_ID()
			+ email.hashCode();
		return "C" + String.valueOf(Math.abs(code)) + "C";
	}	//	getEMailValidationCode

	/**
	 * 	Check & Set EMail Validation Code.
	 *	@param code code
	 *	@param info info
	 *	@return true if valid
	 */
	public boolean setEMailVerifyCode (String code, String info)
	{
		boolean ok = (code != null)
			&& code.equals(getEMailVerifyCode());
		if (ok)
			setEMailVerifyDate(new Timestamp(System.currentTimeMillis()));
		else
			setEMailVerifyDate(null);
		setEMailVerify(info);
		return ok;
	}	//	setEMailValidationCode

	/**
	 * 	Is EMail Verified by response
	 *	@return true if verified
	 */
	public boolean isEMailVerified()
	{
		//	UPDATE AD_User SET EMailVerifyDate=SysDate, EMailVerify='Direct' WHERE AD_User_ID=1
		return (getEMailVerifyDate() != null)
			&& (getEMailVerify() != null)
			&& (getEMailVerify().length() > 0);
	}	//	isEMailVerified

	/**
	 * 	Get Notification via EMail
	 *	@return true if email
	 */
	public boolean isNotificationEMail()
	{
		String s = getNotificationType();
		return (s == null) || NOTIFICATIONTYPE_EMail.equals(s);
	}	//	isNotificationEMail

	/**
	 * 	Get Notification via Note
	 *	@return true if note
	 */
	public boolean isNotificationNote()
	{
		String s = getNotificationType();
		return (s != null) && NOTIFICATIONTYPE_Notice.equals(s);
	}	//	isNotificationNote


	/**************************************************************************
	 * 	Get User Roles for Org
	 * 	@param AD_Org_ID org
	 *	@return array of roles
	 */
	public MRole[] getRoles (int AD_Org_ID)
	{
		if ((m_roles != null) && (m_rolesAD_Org_ID == AD_Org_ID))
			return m_roles;

		ArrayList<MRole> list = new ArrayList<MRole>();
		String sql = "SELECT * FROM AD_Role r "
			+ "WHERE r.IsActive='Y'"
			+ "AND ((EXISTS (SELECT * FROM AD_Role_OrgAccess ro"
			+ " WHERE r.AD_Role_ID=ro.AD_Role_ID AND ro.IsActive='Y' AND ro.AD_Org_ID=?)) OR"
	        + " (EXISTS (SELECT * FROM AD_User_OrgAccess uo, AD_User_Roles ur" 
			+ " WHERE ur.AD_User_ID=uo.AD_User_ID AND uo.IsActive='Y' AND uo.AD_Org_ID=?)))"
			+ " AND EXISTS (SELECT * FROM AD_User_Roles ur"
			+ " WHERE r.AD_Role_ID=ur.AD_Role_ID AND ur.IsActive='Y' AND ur.AD_User_ID=?) "
			+ " ORDER BY AD_Role_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, AD_Org_ID);
			pstmt.setInt (2, AD_Org_ID);
			pstmt.setInt (3, getAD_User_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRole(getCtx(), rs, get_Trx()));
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_rolesAD_Org_ID = AD_Org_ID;
		m_roles = new MRole[list.size()];
		list.toArray (m_roles);
		return m_roles;
	}	//	getRoles

	/**
	 * 	Has the user a role?
	 *	@return true if yes
	 */
	public boolean hasRole ()
	{
		if ((m_roles != null) && (m_roles.length > 0))
			return true;

		int roleCount=0;
		String sql = "SELECT COUNT(*) FROM AD_User_Roles ur "
				+ "WHERE ur.AD_User_ID=? AND ur.IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, getAD_User_ID());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				roleCount = rs.getInt(1);
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return roleCount != 0;
	}	//	hasRole

	/**
	 * 	Is User a System Administrator?
	 *	@return true if System Admin
	 */
	public boolean isSystemAdministrator()
	{
		if (m_isSystemAdministrator == null)
		{
			m_isSystemAdministrator = Boolean.FALSE;
			MRole[] roles = getRoles(0);
			for (MRole element : roles) {
				if (element.getAD_Role_ID() == 0)
				{
					m_isSystemAdministrator = Boolean.TRUE;
					break;
				}
			}
		}

		return m_isSystemAdministrator.booleanValue();
	}	//	isAdministrator

	/**
	 * 	Is User an Administrator?
	 *	@return true id Admin
	 */
	public boolean isAdministrator()
	{
		if (m_isAdministrator == null)
		{
			m_isAdministrator = Boolean.FALSE;
			MRole[] roles = getRoles(0);
			for (MRole element : roles) {
				if (element.isAdministrator())
				{
					m_isAdministrator = Boolean.TRUE;
					break;
				}
			}
		}
		return m_isAdministrator.booleanValue();
	}	//	isAdministrator

	/**
	 * 	Has the user Access to BP info and resources
	 *	@param BPAccessType access type
	 *	@param params opt parameter
	 *	@return true if access
	 */
	public boolean hasBPAccess (String BPAccessType, Object[] params)
	{
		if (isFullBPAccess())
			return true;
		getBPAccess(false);
		for (X_AD_UserBPAccess element : m_bpAccess) {
			if (element.getBPAccessType().equals(BPAccessType))
			{
				return true;
			}
		}
		return false;
	}	//	hasBPAccess

	/**
	 * 	Get active BP Access records
	 *	@param requery requery
	 *	@return access list
	 */
	public X_AD_UserBPAccess[] getBPAccess (boolean requery)
	{
		if ((m_bpAccess != null) && !requery)
			return m_bpAccess;
		String sql = "SELECT * FROM AD_UserBPAccess WHERE AD_User_ID=? AND IsActive='Y'";
		ArrayList<X_AD_UserBPAccess> list = new ArrayList<X_AD_UserBPAccess>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, getAD_User_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new X_AD_UserBPAccess (getCtx(), rs, null));
			}
		}
		catch (Exception e) {
			log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_bpAccess = new X_AD_UserBPAccess[list.size ()];
		list.toArray (m_bpAccess);
		return m_bpAccess;
	}	//	getBPAccess

	/**
	 * 	Add menu item to user's Favorite list.
	 * 	(Saves Tree if necessary)
	 *	@param AD_Menu_ID menu to be added
	 *	@param sequenceNo ordering to be added, 0 if unused
	 *	@return true if added
	 */
	public boolean addUserMenuFavorite (int AD_Menu_ID, int sequenceNo)
	{
		MTree menuTree = null;
		if((menuTree = getUserFavoriteTree()) == null)
			return false;

		MTreeNodeMM node = null;
		if((node = MTreeNodeMM.get(menuTree, AD_Menu_ID)) == null){
			node = new MTreeNodeMM(menuTree, AD_Menu_ID);
		}
		node.setSeqNo(sequenceNo);
		return node.save();
	}	//	addUserMenuFavorite



	/**
	 * 	Add menu item to user's Favorite "new" list
	 * 	(Saves Tree if necessary)
	 *	@param AD_Menu_ID menu to be added
	 *	@param sequenceNo ordering to be added, 0 if unused
	 *	@return true if added
	 */
	public boolean addUserMenuNewFavorite (int AD_Menu_ID, int sequenceNo)
	{
		MTree menuTree = null;
		if((menuTree = getUserNewFavoriteTree()) == null)
			return false;

		MTreeNodeMM node = null;
		if((node = MTreeNodeMM.get(menuTree, AD_Menu_ID)) == null){
			node = new MTreeNodeMM(menuTree, AD_Menu_ID);
		}
		if(sequenceNo != 0)
			node.setSeqNo(sequenceNo);

		return node.save();
	}	//	addUserMenuNewFavorite

	/**
	 * 	Gets the user's tree for create list favorites
	 *	@return MTree for user, or null if can't be created
	 */
	public MTree getUserNewFavoriteTree() {
		MTree menuTree;
		int AD_Tree_ID = getAD_Tree_MenuNew_ID();
		if (AD_Tree_ID == 0)
		{
			menuTree = new MTree (getCtx(), getName() + ": New",
				X_AD_Tree.TREETYPE_Menu, get_Trx());
			menuTree.setIsAllNodes(false);
			if (!menuTree.save())
				return null;
			setAD_Tree_MenuNew_ID(menuTree.getAD_Tree_ID());
			save();
		}
		else
			menuTree = new MTree (getCtx(), AD_Tree_ID, false, true, null);
		return menuTree;
	}

	/**
	 * 	Gets the user's tree for favorites
	 *	@return MTree for user, or null if can't be created
	 */
	public MTree getUserFavoriteTree() {
		MTree menuTree;
		int AD_Tree_ID = getAD_Tree_MenuFavorite_ID();
		if (AD_Tree_ID == 0)
		{
			menuTree = new MTree (getCtx(), getName() + ": Favorite",
				X_AD_Tree.TREETYPE_Menu, get_Trx());
			menuTree.setIsAllNodes(false);
			if (!menuTree.save())
				return null;
			setAD_Tree_MenuFavorite_ID(menuTree.getAD_Tree_ID());
			save();
		}
		else
			menuTree = new MTree (getCtx(), AD_Tree_ID, false, true, null);
		return menuTree;
	} 	//getUserFavoriteTree

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	New Address invalidates verification
		if (!newRecord && is_ValueChanged("EMail"))
			setEMailVerifyDate(null);
		if (newRecord || (super.getValue() == null) || is_ValueChanged("Value"))
			setValue(super.getValue());

		//	You cannot change a password of a user with a role
		//	unless you are the user or in sys admin role
		if (!newRecord && is_ValueChanged("Password"))
		{
			boolean canUpdate = true;		//	SelfService User
			if (hasRole())
			{
				canUpdate = false;			//	everyone else
				if (getCtx().getAD_User_ID() == getAD_User_ID())
					canUpdate = true;		//	change your own
				if (!canUpdate)
				{
					MRole role = MRole.getDefault(getCtx(), false);
					if (role.isAdministrator())
						canUpdate = true;	//	Admin
				}
			}
			if (!canUpdate)
			{
				log.saveError("Warning", Msg.getMsg(getCtx(), "UserCannotUpdate"));
				return false;
			}
		}		
		
		if ((newRecord || is_ValueChanged("IsActive"))){
			
			// only need to check license compliance if the user is new / made active
			if (!this.isActive()) return true;
			
			// only need to check license if there are users assigned to it
			String sql = "SELECT COUNT(1)"
				+ " FROM AD_User_Roles ur"
				+ " WHERE ur.isActive='Y'"
				+ " AND ur.AD_Client_ID<>11"
				+ " AND ur.AD_User_ID NOT IN (0,100)"
				+ " AND ur.AD_User_ID = ?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			int roles = 0;
			try {
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt (1, this.getAD_User_ID());
				rs = pstmt.executeQuery ();
				if (rs.next ())
					roles = rs.getInt (1);
			} catch (Exception e) {
				s_log.log(Level.SEVERE, sql, e);
			} finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			if (roles == 0) 
				return true;
			
			// check if actual number of users with roles exceed the licensed seats
			SysEnv se = SysEnv.get(null);
			if ((se == null) || !se.checkLicense())
				return true; // Community users
			int licensed = se.getLicensedUnitOne();
			int actual = 0;

			// select user count besides this user
			sql = "SELECT COUNT(DISTINCT (u.AD_User_ID)) AS iu "
				+ "FROM AD_User u"
				+ " INNER JOIN AD_User_Roles ur ON (u.AD_User_ID=ur.AD_User_ID) "
				+ "WHERE u.AD_Client_ID<>11"			//	no Demo
				+ " AND u.AD_User_ID NOT IN (0,100)"	//	no System/SuperUser
				+ " AND u.isActive='Y'"
				+ " AND ur.isActive='Y'"
				+ " AND u.AD_User_ID != ?";
			
			pstmt = null;
			rs = null;
			try {
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt (1, this.getAD_User_ID());
				rs = pstmt.executeQuery ();
				if (rs.next ())
					actual = rs.getInt (1);
			} catch (Exception e) {
				s_log.log(Level.SEVERE, sql, e);
			} finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			
			if (actual < licensed)
				return true; 
			
			String message = Msg.getMsg(Env.getCtx(), "InsufficientLicense", new Object[] {licensed,actual});
			log.saveError("Insufficient License", message);
			return false;
		}
		return true;
	}	//	beforeSave
	
	public boolean equals (	String Name, String Email)
		{
			if (!equalsNull(Name, getName()))
				return false;
			if (!equalsNull(Email, getEMail()))
				return false;
			return true;
		}	//	equals
		
		/**
		 * 	Equals if "" or Null
		 *	@param c1 c1
		 *	@param c2 c2
		 *	@return true if equal (ignore case)
		 */
		private boolean equalsNull (String c1, String c2)
		{
			if (c1 == null)
				c1 = "";
			if (c2 == null)
				c2 = "";
			return c1.equalsIgnoreCase(c2);
		}	//	equalsNull


	/**
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		Compiere.startup(true);
		MUser user = new MUser (Env.getCtx(), 101, null);
		String s = user.getPassword();
		System.out.println(s);

/**		user.setPassword("abcd");
		user.save();
		String s2 = user.getPassword();
		System.out.println(s2);
		user = new MUser (Env.getCtx(), 100, null);
		String s3 = user.getPassword();
		System.out.println(s3);

		try
		{
//			validateEmail(new InternetAddress("jjanke@compiere.org"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	//	org.compiere.Compiere.startupClient();
	//	System.out.println ( MUser.get(Env.getCtx(), "SuperUser", "22") );

	 	*/
	}	//	main	/* */
}	//	MUser
