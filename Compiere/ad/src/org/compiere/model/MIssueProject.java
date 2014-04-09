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
 * 	Issue Project (and Asset Link)
 *	
 *  @author Jorg Janke
 *  @version $Id: MIssueProject.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public class MIssueProject extends X_R_IssueProject
{
    /** Logger for class MIssueProject */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MIssueProject.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get/Set Project
	 *	@param issue issue
	 *	@return project
	 */
	static public MIssueProject get (MIssue issue)
	{
		if (issue.getName() == null)
			return null;
		MIssueProject pj = null;
		String sql = "SELECT * FROM R_IssueProject WHERE Name=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString (1, issue.getName());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				pj = new MIssueProject(issue.getCtx(), rs, null);
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	New
		if (pj == null)
		{
			pj = new MIssueProject(issue.getCtx(), 0, null);
			pj.setName(issue.getName());
			pj.setA_Asset_ID(issue);
		}
		pj.setSystemStatus(issue.getSystemStatus());
		pj.setStatisticsInfo(issue.getStatisticsInfo());
		pj.setProfileInfo(issue.getProfileInfo());
		if (!pj.save())
			return null;
		
		//	Set 
		issue.setR_IssueProject_ID(pj.getR_IssueProject_ID());
		if (pj.getA_Asset_ID() != 0)
			issue.setA_Asset_ID(pj.getA_Asset_ID());
		return pj;
	}	//	get
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MIssueProject.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_IssueProject_ID id
	 *	@param trx p_trx
	 */
	public MIssueProject (Ctx ctx, int R_IssueProject_ID, Trx trx)
	{
		super (ctx, R_IssueProject_ID, trx);
	}	//	MIssueProject

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MIssueProject (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MIssueProject
	
	/**
	 * 	Set A_Asset_ID
	 *	@param issue issue
	 */
	public void setA_Asset_ID (MIssue issue)
	{
		int A_Asset_ID = 0;
		super.setA_Asset_ID (A_Asset_ID);
	}	//	setA_Asset_ID
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MIssueProject[");
		sb.append (get_ID())
			.append ("-").append (getName())
			.append(",A_Asset_ID=").append(getA_Asset_ID())
			.append(",C_Project_ID=").append(getC_Project_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MIssueProject
