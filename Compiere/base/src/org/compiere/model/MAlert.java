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
 *	Alert Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAlert.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MAlert extends X_AD_Alert
{
    /** Logger for class MAlert */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAlert.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Alert_ID id
	 *	@param trx transaction
	 */
	public MAlert (Ctx ctx, int AD_Alert_ID, Trx trx)
	{
		super (ctx, AD_Alert_ID, trx);
		if (AD_Alert_ID == 0)
		{
		//	setAD_AlertProcessor_ID (0);
		//	setName (null);
		//	setAlertMessage (null);
		//	setAlertSubject (null);
			setEnforceClientSecurity (true);	// Y
			setEnforceRoleSecurity (true);	// Y
			setIsValid (true);	// Y
		}	
	}	//	MAlert

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAlert (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAlert

	
	/**	The Rules						*/
	private MAlertRule[]		m_rules	= null;
	/**	The Recipients					*/
	private MAlertRecipient[]	m_recipients = null;

	/**
	 * 	Get Rules
	 *	@param reload reload data
	 *	@return array of rules
	 */
	public MAlertRule[] getRules (boolean reload)
	{
		if (m_rules != null && !reload)
			return m_rules;
		String sql = "SELECT * FROM AD_AlertRule "
			+ "WHERE AD_Alert_ID=?";
		ArrayList<MAlertRule> list = new ArrayList<MAlertRule>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, getAD_Alert_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MAlertRule (getCtx(), rs, get_Trx()));
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
		//
		m_rules = new MAlertRule[list.size ()];
		list.toArray (m_rules);
		return m_rules;
	}	//	getRules
	
	/**
	 * 	Get Recipients
	 *	@param reload reload data
	 *	@return array of recipients
	 */
	public MAlertRecipient[] getRecipients (boolean reload)
	{
		if (m_recipients != null && !reload)
			return m_recipients;
		String sql = "SELECT * FROM AD_AlertRecipient " 
			+ "WHERE AD_Alert_ID=?";
		ArrayList<MAlertRecipient> list = new ArrayList<MAlertRecipient>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, getAD_Alert_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MAlertRecipient (getCtx(), rs, get_Trx()));
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
		//
		m_recipients = new MAlertRecipient[list.size ()];
		list.toArray (m_recipients);
		return m_recipients;
	}	//	getRecipients

	/**
	 * 	Get First Role if exist
	 *	@return AD_Role_ID or -1
	 */
	public int getFirstAD_Role_ID()
	{
		getRecipients(false);
		for (MAlertRecipient element : m_recipients) {
			if (element.getAD_Role_ID() != -1)
				return element.getAD_Role_ID();
		}
		return -1;
	}	//	getForstAD_Role_ID
	
	/**
	 * 	Get First User Role if exist
	 *	@return AD_Role_ID or -1
	 */
	public int getFirstUserAD_Role_ID()
	{
		getRecipients(false);
		int AD_User_ID = getFirstAD_User_ID();
		if (AD_User_ID != -1)
		{
			MUserRoles[] urs = MUserRoles.getOfUser(getCtx(), AD_User_ID);
			for (MUserRoles element : urs) {
				if (element.isActive())
					return element.getAD_Role_ID();
			}
		}
		return -1;
	}	//	getFirstUserAD_Role_ID

	/**
	 * 	Get First User if exist
	 *	@return AD_User_ID or -1
	 */
	public int getFirstAD_User_ID()
	{
		getRecipients(false);
		for (MAlertRecipient element : m_recipients) {
			if (element.getAD_User_ID() != -1)
				return element.getAD_User_ID();
		}
		return -1;
	}	//	getFirstAD_User_ID
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAlert[");
		sb.append(get_ID())
			.append("-").append(getName())
			.append(",Valid=").append(isValid());
		if (m_rules != null)
			sb.append(",Rules=").append(m_rules.length);
		if (m_recipients != null)
			sb.append(",Recipients=").append(m_recipients.length);
		sb.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MAlert
