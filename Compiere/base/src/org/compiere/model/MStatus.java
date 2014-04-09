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
 * 	Request Status Model
 *  @author Jorg Janke
 *  @version $Id: MStatus.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MStatus extends X_R_Status
{
    /** Logger for class MStatus */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MStatus.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Request Status (cached)
	 *	@param ctx context
	 *	@param R_Status_ID id
	 *	@return Request Status or null
	 */
	public static MStatus get (Ctx ctx, int R_Status_ID)
	{
		if (R_Status_ID == 0)
			return null;
		Integer key = Integer.valueOf (R_Status_ID);
		MStatus retValue = s_cache.get(ctx, key);
		if (retValue == null)
		{
			retValue = new MStatus (ctx, R_Status_ID, null);
			s_cache.put(key, retValue);
		}
		return retValue;
	}	//	get

	/**
	 * 	Get Default Request Status
	 *	@param ctx context
	 *	@param R_RequestType_ID request type
	 *	@return Request Type
	 */
	public static MStatus getDefault (Ctx ctx, int R_RequestType_ID)
	{
		Integer key = Integer.valueOf(R_RequestType_ID);
		MStatus retValue = s_cacheDefault.get(ctx, key);
		if (retValue != null)
			return retValue;
		//	Get New
		String sql = "SELECT * FROM R_Status s "
			+ "WHERE EXISTS (SELECT * FROM R_RequestType rt "
				+ "WHERE rt.R_StatusCategory_ID=s.R_StatusCategory_ID"
				+ " AND rt.R_RequestType_ID=?)"
			+ " AND IsDefault='Y' "
			+ "ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, R_RequestType_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MStatus (ctx, rs, null);
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (retValue != null)
			s_cacheDefault.put(key, retValue);
		return retValue;
	}	//	getDefault

	/**
	 * 	Get Closed Status
	 *	@param ctx context
	 *	@return Request Type
	 */
	public static MStatus[] getClosed (Ctx ctx, int R_StatusCategory_ID)
	{
		int AD_Client_ID = ctx.getAD_Client_ID();
		String sql = "SELECT * FROM R_Status "
			+ "WHERE AD_Client_ID=? AND IsActive='Y' AND IsClosed='Y' "
			+ " AND R_StatusCategory_ID=? "
			+ "ORDER BY Value";
		ArrayList<MStatus> list = new ArrayList<MStatus>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, R_StatusCategory_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				list.add(new MStatus (ctx, rs, null));
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MStatus[] retValue = new MStatus[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	get

	
	/** Static Logger					*/
	private static final CLogger s_log = CLogger.getCLogger(MStatus.class);
	/**	Cache							*/
	static private final CCache<Integer,MStatus> s_cache
		= new CCache<Integer,MStatus> ("R_Status", 10);
	/**	Default Cache (Key=Client)		*/
	static private final CCache<Integer,MStatus> s_cacheDefault
		= new CCache<Integer,MStatus>("R_Status", 10);


	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param R_Status_ID is
	 *	@param trx p_trx
	 */
	public MStatus (Ctx ctx, int R_Status_ID, Trx trx)
	{
		super (ctx, R_Status_ID, trx);
		if (R_Status_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setIsClosed (false);	// N
			setIsDefault (false);
			setIsFinalClose (false);	// N
			setIsOpen (false);
			setIsWebCanUpdate (true);
		}
	}	//	MStatus

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MStatus (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MStatus
	
	/**
	 * 	Is the Request Type valid for this Status
	 *	@param R_RequestType_ID id
	 *	@return true if valid
	 */
	public boolean isRequestType (int R_RequestType_ID)
	{
		if (R_RequestType_ID == 0)
			return false;
		MRequestType rt = MRequestType.get(getCtx(), R_RequestType_ID);
		if (rt.getR_RequestType_ID() != R_RequestType_ID)
			return false;
		return rt.getR_StatusCategory_ID() == getR_StatusCategory_ID();
	}	//	isRequestType
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (isOpen() && isClosed())
			setIsClosed(false);
		if (isFinalClose() && !isClosed())
			setIsFinalClose(false);
		//
		if (!isWebCanUpdate() && getUpdate_Status_ID() != 0)
			setUpdate_Status_ID(0);
		if (getTimeoutDays() == 0 && getNext_Status_ID() != 0)
			setNext_Status_ID(0);
		//
		return true;
	}	//	beforeSave
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MStatus[");
		sb.append(get_ID()).append("-").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MStatus
