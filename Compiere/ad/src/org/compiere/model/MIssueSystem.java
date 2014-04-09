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
 * 	Issue System Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MIssueSystem.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public class MIssueSystem extends X_R_IssueSystem
{
    /** Logger for class MIssueSystem */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MIssueSystem.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get/Set System
	 *	@param issue issue
	 *	@return system
	 */
	static public MIssueSystem get (MIssue issue)
	{
		if (issue.getDBAddress() == null)
			return null;
		MIssueSystem system = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM R_IssueSystem WHERE DBAddress=?";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString (1, issue.getDBAddress());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				system = new MIssueSystem(issue.getCtx(), rs, null);
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	New
		if (system == null)
		{
			system = new MIssueSystem(issue.getCtx(), 0, null);
			system.setDBAddress(issue.getDBAddress());
			system.setA_Asset_ID(issue.getA_Asset_ID());
		}
		system.setSystemStatus(issue.getSystemStatus());
		system.setStatisticsInfo(issue.getStatisticsInfo());
		system.setProfileInfo(issue.getProfileInfo());
		if (issue.getA_Asset_ID() != 0 
			&& system.getA_Asset_ID() != issue.getA_Asset_ID())
			system.setA_Asset_ID(issue.getA_Asset_ID());
		//
		if (!system.save())
			return null;
		
		//	Set 
		issue.setR_IssueSystem_ID(system.getR_IssueSystem_ID());
		if (system.getA_Asset_ID() != 0)
			issue.setA_Asset_ID(system.getA_Asset_ID());
		return system;
	}	//	get
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MIssueSystem.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_IssueSystem_ID id
	 *	@param trx p_trx
	 */
	public MIssueSystem (Ctx ctx, int R_IssueSystem_ID, Trx trx)
	{
		super (ctx, R_IssueSystem_ID, trx);
	}	//	MIssueSystem

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MIssueSystem (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MIssueSystem
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MIssueSystem[");
		sb.append(get_ID())
			.append ("-").append (getDBAddress())
			.append(",A_Asset_ID=").append(getA_Asset_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
}	//	MIssueSystem
