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
import java.util.logging.*;
import org.compiere.util.*;

/**
 * 	Issue User Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MIssueUser.java 8780 2010-05-19 23:08:57Z nnayak $
 */
public class MIssueUser extends X_R_IssueUser
{
    /** Logger for class MIssueUser */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MIssueUser.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get/Set User for Issue
	 *	@param issue issue
	 *	@return User
	 */
	static public MIssueUser get (MIssue issue)
	{
		if (issue.getUserName() == null)
			return null;
		MIssueUser user = null;
		//	Find Issue User
		String sql = "SELECT * FROM R_IssueUser WHERE UserName=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString (1, issue.getUserName());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				user = new MIssueUser (issue.getCtx(), rs, null);
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//	New
		if (user == null)
		{
			user = new MIssueUser(issue.getCtx(), 0, null);
			user.setUserName(issue.getUserName());
			user.setAD_User_ID();
			if (!user.save())
				return null;
		}
		
		issue.setR_IssueUser_ID(user.getR_IssueUser_ID());
		return user;
	}	//	MIssueUser
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MIssueUser.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_IssueUser_ID id
	 *	@param trx p_trx
	 */
	public MIssueUser (Ctx ctx, int R_IssueUser_ID, Trx trx)
	{
		super (ctx, R_IssueUser_ID, trx);
	}	//	MIssueUser

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MIssueUser (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MIssueUser
	
	
	/**
	 * 	Set AD_User_ID
	 */
	public void setAD_User_ID ()
	{
		int AD_User_ID = QueryUtil.getSQLValue(null, 
			"SELECT AD_User_ID FROM AD_User WHERE EMail=?", getUserName());
		if (AD_User_ID != 0)
			super.setAD_User_ID (AD_User_ID);
	}	//	setAD_User_ID
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MIssueUser[");
		sb.append (get_ID())
			.append ("-").append(getUserName())
			.append(",AD_User_ID=").append(getAD_User_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
}	//	MIssueUser
