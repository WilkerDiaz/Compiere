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
 *	User Query Model.
 *	User maintained for future use, Table for validation 
 *	
 *  @author Jorg Janke
 *  @version $Id: MUserQuery.java 8776 2010-05-19 17:50:56Z nnayak $
 */
public class MUserQuery extends X_AD_UserQuery
{
    /** Logger for class MUserQuery */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MUserQuery.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get all active queries of client for Tab
	 *	@param ctx context
	 *	@param AD_Tab_ID tab
	 *	@return array of queries
	 */
	public static MUserQuery[] get (Ctx ctx, int AD_Tab_ID, int AD_Table_ID)
	{
		String sql = "SELECT * FROM AD_UserQuery "
			+ "WHERE AD_Client_ID=? AND IsActive='Y'"
			+ " AND (AD_Tab_ID=? OR AD_Table_ID=?) "
			+ "ORDER BY Name";
		int AD_Client_ID = ctx.getAD_Client_ID();
		ArrayList<MUserQuery> list = new ArrayList<MUserQuery>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Client_ID);
			pstmt.setInt (2, AD_Tab_ID);
			pstmt.setInt (3, AD_Table_ID);
			rs = pstmt.executeQuery();
			while (rs.next ())
				list.add(new MUserQuery (ctx, rs, null));
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MUserQuery[] retValue = new MUserQuery[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Get Saved Query Names
	 *	@param AD_Client_ID client
	 *	@param AD_Tab_ID tab
	 *	@return saved Queries
	 */
	public static ArrayList<String> getSavedQueryNames(int AD_Client_ID, int AD_Tab_ID)
	{
		String sql = "SELECT Name FROM AD_UserQuery "
			+ "WHERE AD_Client_ID=? AND AD_Tab_ID=? AND IsActive='Y' "
			+ "ORDER BY Name";
		ArrayList<String> retValue = new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Client_ID);
			pstmt.setInt (2, AD_Tab_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String name = rs.getString(1);
				retValue.add(name);
			}
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	getSavedQueryNames

	/**
	 * 	Get Saved Query Names
	 *	@param AD_User_ID user
	 *	@param AD_Tab_ID tab
	 *	@return saved Queries
	 */
	public static ArrayList<String> getSavedQueryNamesForUser(int AD_User_ID, int AD_Tab_ID)
	{
		String sql = "SELECT Name FROM AD_UserQuery "
			+ "WHERE AD_User_ID=? AND AD_Tab_ID=? AND IsActive='Y' "
			+ "ORDER BY Name";
		ArrayList<String> retValue = new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_User_ID);
			pstmt.setInt (2, AD_Tab_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String name = rs.getString(1);
				retValue.add(name);
			}
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	getSavedQueryNamesForUser
	/**
	 * 	Get Specific Tab Query
	 *	@param ctx context
	 *	@param AD_Tab_ID tab
	 *	@param name name
	 *	@return query or null
	 */
	public static MUserQuery get (Ctx ctx, int AD_Tab_ID, String name)
	{
		String sql = "SELECT * FROM AD_UserQuery "
			+ "WHERE AD_Client_ID=? AND AD_Tab_ID=? AND UPPER(Name) LIKE ? AND IsActive='Y' "
			+ "ORDER BY Name";
		int AD_Client_ID = ctx.getAD_Client_ID();
		if (name == null)
			name = "%";
		MUserQuery retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Client_ID);
			pstmt.setInt (2, AD_Tab_ID);
			pstmt.setString (3, name.toUpperCase());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MUserQuery (ctx, rs, null);
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get

	/**
	 * 	Get Specific Tab Query
	 *	@param ctx context
	 *	@param AD_Tab_ID tab
	 *	@param name name
	 *	@return query or null
	 */
	public static MUserQuery getForUser (Ctx ctx, int AD_Tab_ID, String name)
	{
		String sql = "SELECT * FROM AD_UserQuery "
			+ "WHERE AD_User_ID=? AND AD_Tab_ID=? AND UPPER(Name) LIKE ? AND IsActive='Y' "
			+ "ORDER BY Name";
		int AD_User_ID = ctx.getAD_User_ID();
		if (name == null)
			name = "%";
		MUserQuery retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_User_ID);
			pstmt.setInt (2, AD_Tab_ID);
			pstmt.setString (3, name.toUpperCase());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MUserQuery (ctx, rs, null);
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	getForUser
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MUserQuery.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_UserQuery_ID id
	 *	@param trx p_trx
	 */
	public MUserQuery(Ctx ctx, int AD_UserQuery_ID, Trx trx)
	{
		super (ctx, AD_UserQuery_ID, trx);
	}	//	MUserQuery

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MUserQuery(Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MUserQuery


	/**	The Lines						*/
	private MUserQueryLine[] m_lines = null;

	/**
	 * 	Get Lines
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public MUserQueryLine[] getLines(boolean reload)
	{
		if (m_lines != null && !reload)
			return m_lines;
		ArrayList<MUserQueryLine> list = new ArrayList<MUserQueryLine>();
		String sql = "SELECT * FROM AD_UserQueryLine WHERE AD_UserQuery_ID=? ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getAD_UserQuery_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MUserQueryLine(getCtx(), rs, get_Trx()));
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_lines = new MUserQueryLine[list.size()];
		list.toArray(m_lines);
		return m_lines;
	}	//	getLines
	
	/**
	 * 	Delete all Lines
	 *	@return true if deleted
	 */
	public boolean deleteLines()
	{
		String sql = "DELETE FROM AD_UserQueryLine WHERE AD_UserQuery_ID=?";
		int no = DB.executeUpdate(null, sql, getAD_UserQuery_ID());	//	out of p_trx
		log.info("#" + no);
		m_lines = null;
		return no >= 0;
	}	//	deleteLines
	
	/**
	 * 	String Info
	 *	@return name
	 */
	@Override
	public String toString()
	{
	    return getName();
	}	//	toString
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toStringX()
    {
	    StringBuffer sb = new StringBuffer("MUserQuery[");
	    	sb.append(get_ID()).append("-").append(getName()).append("]");
	    return sb.toString();
    }	//	toString
    
}	//	MUserQuery
