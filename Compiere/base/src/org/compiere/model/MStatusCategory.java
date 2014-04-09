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
 * 	Request Status Category Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MStatusCategory.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MStatusCategory extends X_R_StatusCategory
{
    /** Logger for class MStatusCategory */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MStatusCategory.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Default Status Categpru for Client
	 *	@param ctx context
	 *	@return status category or null
	 */
	public static MStatusCategory getDefault (Ctx ctx)
	{
		int AD_Client_ID = ctx.getAD_Client_ID();
		String sql = "SELECT * FROM R_StatusCategory "
			+ "WHERE AD_Client_ID in (0,?) AND IsDefault='Y' "
			+ "ORDER BY AD_Client_ID DESC";
		MStatusCategory retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Client_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MStatusCategory (ctx, rs, null);
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
		return retValue;
	}	//	getDefault
	
	/**
	 * 	Get Default Status Categpru for Client
	 *	@param ctx context
	 *	@return status category or null
	 */
	public static MStatusCategory createDefault (Ctx ctx)
	{
		int AD_Client_ID = ctx.getAD_Client_ID();
		MStatusCategory retValue = new MStatusCategory(ctx, 0, null);
		retValue.setClientOrg(AD_Client_ID, 0);
		retValue.setName(Msg.getMsg(ctx, "Standard"));
		retValue.setIsDefault(true);
		if (!retValue.save())
			return null;
		String sql = "UPDATE R_Status SET R_StatusCategory_ID= ?" 
			+ " WHERE R_StatusCategory_ID IS NULL AND AD_Client_ID= ? ";
		Object[] params = new Object[]{retValue.getR_StatusCategory_ID(),AD_Client_ID};
		int no = DB.executeUpdate((Trx) null, sql,params);
		s_log.info("Default for AD_Client_ID=" + AD_Client_ID + " - Status #" + no);
		return retValue;
	}	//	createDefault

	/**
	 * 	Get Request Status Category from Cache
	 *	@param ctx context
	 *	@param R_StatusCategory_ID id
	 *	@return RStatusCategory
	 */
	public static MStatusCategory get (Ctx ctx, int R_StatusCategory_ID)
	{
		Integer key = Integer.valueOf (R_StatusCategory_ID);
		MStatusCategory retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MStatusCategory (ctx, R_StatusCategory_ID, null);
		if (retValue.get_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer, MStatusCategory> s_cache 
		= new CCache<Integer, MStatusCategory> ("R_StatusCategory", 20);
	/**	Logger	*/
	private static final CLogger s_log = CLogger.getCLogger (MStatusCategory.class);
	
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param R_StatusCategory_ID id
	 *	@param trx p_trx
	 */
	public MStatusCategory (Ctx ctx, int R_StatusCategory_ID, Trx trx)
	{
		super (ctx, R_StatusCategory_ID, trx);
		if (R_StatusCategory_ID == 0)
		{
		//	setName (null);
			setIsDefault (false);
		}
	}	//	RStatusCategory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MStatusCategory (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	RStatusCategory

	/**	The Status						*/
	private MStatus[] m_status = null;
	
	/**
	 * 	Get all Status
	 *	@param reload reload
	 *	@return Status array 
	 */
	public MStatus[] getStatus(boolean reload)
	{
		if (m_status != null && !reload)
			return m_status;
		String sql = "SELECT * FROM R_Status "
			+ "WHERE R_StatusCategory_ID=? "
			+ "ORDER BY SeqNo";
		ArrayList<MStatus> list = new ArrayList<MStatus>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, getR_StatusCategory_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MStatus (getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		m_status = new MStatus[list.size ()];
		list.toArray (m_status);
		return m_status;
	}	//	getStatus
	
	/**
	 * 	Get Default R_Status_ID
	 *	@return id or 0
	 */
	public int getDefaultR_Status_ID()
	{
		if (m_status == null)
			getStatus(false);
		for (MStatus element : m_status) {
			if (element.isDefault() && element.isActive())
				return element.getR_Status_ID();
		}
		if (m_status.length > 0 
			&& m_status[0].isActive())
				return m_status[0].getR_Status_ID();
		return 0;
	}	//	getDefaultR_Status_ID

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("RStatusCategory[");
		sb.append (get_ID()).append ("-").append(getName()).append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	RStatusCategory
