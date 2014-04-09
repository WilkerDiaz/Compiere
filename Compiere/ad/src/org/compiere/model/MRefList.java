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
 *  Reference List Value
 *
 *  @author Jorg Janke
 *  @version $Id: MRefList.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MRefList extends X_AD_Ref_List
{
    /** Logger for class MRefList */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRefList.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Reference List 
	 *	@param ctx context
	 *	@param AD_Reference_ID reference
	 *	@param Value value
	 *	@param trx transaction
	 *	@return List or null
	 */
	public static MRefList get (Ctx ctx, int AD_Reference_ID, String Value, Trx trx)
	{
		MRefList retValue = null;
		String sql = "SELECT * FROM AD_Ref_List "
			+ "WHERE AD_Reference_ID=? AND Value=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, AD_Reference_ID);
			pstmt.setString (2, Value);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MRefList (ctx, rs, trx);
		}
		catch (SQLException ex) {
			s_log.log(Level.SEVERE, sql, ex);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get

	/**
	 * 	Get Reference List Value Name (cached)
	 *	@param ctx context
	 *	@param AD_Reference_ID reference
	 *	@param Value value
	 *	@return List or null
	 */
	public static String getListName (Ctx ctx, int AD_Reference_ID, String Value)
	{
		String AD_Language = Env.getAD_Language(ctx);
		String key = AD_Language + "_" + AD_Reference_ID + "_" + Value;
		String retValue = s_cache.get(null, key);
		if (retValue != null)
			return retValue;

		boolean isBaseLanguage = Env.isBaseLanguage(AD_Language, "AD_Ref_List");
		String sql = isBaseLanguage ?
			"SELECT Name FROM AD_Ref_List "
			+ "WHERE AD_Reference_ID=? AND Value=?" :
			"SELECT t.Name FROM AD_Ref_List_Trl t"
			+ " INNER JOIN AD_Ref_List r ON (r.AD_Ref_List_ID=t.AD_Ref_List_ID) "
			+ "WHERE r.AD_Reference_ID=? AND r.Value=? AND t.AD_Language=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Reference_ID);
			pstmt.setString(2, Value);
			if (!isBaseLanguage)
				pstmt.setString(3, AD_Language);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = rs.getString(1);
		}
		catch (SQLException ex) {
			s_log.log(Level.SEVERE, sql + " - " + key, ex);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	Save into Cache
		if (retValue == null)
		{
			retValue = "";
			s_log.warning("Not found " + key);
		}
		s_cache.put(key, retValue);
		//
		return retValue;
	}	//	getListName


	/**
	 * 	Get Reference List
	 *	@param AD_Reference_ID reference
	 *	@param optional if true add "",""
	 *	@return List or null
	 */
	public static ValueNamePair[] getList (Ctx ctx, int AD_Reference_ID, boolean optional)
	{
		String AD_Language = Env.getAD_Language(ctx);

		boolean isBaseLanguage = Env.isBaseLanguage(AD_Language, "AD_Ref_List");
		String sql = isBaseLanguage ?
				"SELECT Value, Name FROM AD_Ref_List "
			+ "WHERE AD_Reference_ID=? AND IsActive='Y' ORDER BY 1":
				"SELECT r.Value, COALESCE(rt.Name, r.Name) FROM AD_Ref_List r " +
				"LEFT OUTER JOIN AD_Ref_List_TRL rt ON (r.AD_Ref_List_ID=rt.AD_Ref_List_ID) " +
				"WHERE r.AD_Reference_ID=? AND r.IsActive='Y'AND rt.AD_Language=?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<ValueNamePair> list = new ArrayList<ValueNamePair>();
		if (optional)
			list.add(new ValueNamePair("", ""));
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Reference_ID);
			if (!isBaseLanguage)
				pstmt.setString(2, AD_Language);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new ValueNamePair(rs.getString(1), rs.getString(2)));
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		ValueNamePair[] retValue = new ValueNamePair[list.size()];
		list.toArray(retValue);
		return retValue;		
	}	//	getList


	/**	Logger							*/
	private static final CLogger		s_log = CLogger.getCLogger (MRefList.class);
	/** Value Cache						*/
	private static final CCache<String,String> s_cache = new CCache<String,String>("AD_Ref_List", 20);


	/**************************************************************************
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param AD_Ref_List_ID id
	 *	@param trx transaction
	 */
	public MRefList (Ctx ctx, int AD_Ref_List_ID, Trx trx)
	{
		super (ctx, AD_Ref_List_ID, trx);
		if (AD_Ref_List_ID == 0)
		{
		//	setAD_Reference_ID (0);
		//	setAD_Ref_List_ID (0);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
		//	setName (null);
		//	setValue (null);
		}
	}	//	MRef_List

	/**
	 * 	Load Contructor
	 *	@param ctx context
	 *	@param rs result
	 *	@param trx transaction
	 */
	public MRefList (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MRef_List

	/**
	 *	String Representation
	 * 	@return Name
	 */
	@Override
	public String toString()
	{
		return getName();
	}	//	toString

	/**
	 * 	String Info
	 * 	@return info
	 */
	@Override
	public String toStringX()
	{
		StringBuffer sb = new StringBuffer("MRefList[");
		sb.append(get_ID())
			.append("-").append(getValue()).append("=").append(getName())
			.append("]");
		return sb.toString();
	}	//	toStringX
	
}	//	MRef_List
