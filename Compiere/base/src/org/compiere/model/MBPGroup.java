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

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Business Partner Group Model
 *
 *  @author Jorg Janke
 *  @version $Id: MBPGroup.java,v 1.4 2006/09/23 15:54:22 jjanke Exp $
 */
public class MBPGroup extends X_C_BP_Group
{
    /** Logger for class MBPGroup */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPGroup.class);
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get MBPGroup from Cache
	 *	@param ctx context
	 *	@param C_BP_Group_ID id
	 *	@return MBPGroup
	 */
	public static MBPGroup get (Ctx ctx, int C_BP_Group_ID)
	{
		Integer key = Integer.valueOf (C_BP_Group_ID);
		MBPGroup retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MBPGroup (ctx, C_BP_Group_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Default MBPGroup
	 *	@param ctx context
	 *	@return MBPGroup
	 */
	public static MBPGroup getDefault (Ctx ctx)
	{
		int AD_Client_ID = ctx.getAD_Client_ID();
		Integer key = Integer.valueOf (AD_Client_ID);
		MBPGroup retValue = s_cacheDefault.get (ctx, key);
		if (retValue != null)
			return retValue;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM C_BP_Group g "
			+ "WHERE IsDefault='Y' AND AD_Client_ID=? "
			+ "ORDER BY IsActive DESC";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Client_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MBPGroup (ctx, rs, null);
				if (retValue.get_ID () != 0)
					s_cacheDefault.put (key, retValue);
			}
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		if (retValue == null)
			s_log.warning("No Default BP Group for AD_Client_ID=" + AD_Client_ID);
		return retValue;
	}	//	get

	/**
	 * 	Get MBPGroup from Business Partner
	 *	@param ctx context
	 *	@param C_BPartner_ID business partner id
	 *	@return MBPGroup
	 */
	public static MBPGroup getOfBPartner (Ctx ctx, int C_BPartner_ID)
	{
		MBPGroup retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM C_BP_Group g "
			+ "WHERE EXISTS (SELECT * FROM C_BPartner p "
				+ "WHERE p.C_BPartner_ID=? AND p.C_BP_Group_ID=g.C_BP_Group_ID)";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, C_BPartner_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MBPGroup (ctx, rs, null);
				Integer key = Integer.valueOf (retValue.getC_BP_Group_ID());
				if (retValue.get_ID () != 0)
					s_cache.put (key, retValue);
			}
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
	}	//	getOfBPartner

	/**	Cache						*/
	private static final CCache<Integer,MBPGroup>	s_cache
		= new CCache<Integer,MBPGroup>("C_BP_Group", 10);
	/**	Default Cache					*/
	private static final CCache<Integer,MBPGroup>	s_cacheDefault
		= new CCache<Integer,MBPGroup>("C_BP_Group", 5, 120, true);
	/**	Logger	*/
	private static final CLogger s_log = CLogger.getCLogger (MBPGroup.class);

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BP_Group_ID id
	 *	@param trx transaction
	 */
	public MBPGroup (Ctx ctx, int C_BP_Group_ID, Trx trx)
	{
		super (ctx, C_BP_Group_ID, trx);
		if (C_BP_Group_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setIsConfidentialInfo (false);	// N
			setIsDefault (false);
			setPriorityBase(PRIORITYBASE_Same);
		}
	}	//	MBPGroup

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MBPGroup (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MBPGroup


	/**
	 * 	Get Credit Watch Percent
	 *	@return 90 or defined percent
	 */
	@Override
	public BigDecimal getCreditWatchPercent ()
	{
		BigDecimal bd = super.getCreditWatchPercent();
		if (bd != null)
			return bd;
		return new BigDecimal(90);
	}	//	getCreditWatchPercent

	/**
	 * 	Get Credit Watch Ratio
	 *	@return 0.90 or defined percent
	 */
	public BigDecimal getCreditWatchRatio()
	{
		BigDecimal bd = super.getCreditWatchPercent();
		if (bd != null)
			return bd.divide(Env.ONEHUNDRED, 2, BigDecimal.ROUND_HALF_UP);
		return new BigDecimal(0.90);
	}	//	getCreditWatchRatio


	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 	After Save
	 *	@param newRecord new record
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord & success)
			return insert_Accounting("C_BP_Group_Acct", "C_AcctSchema_Default", null);
		return success;
	}	//	afterSave


	/**
	 * 	Before Delete
	 *	@return true
	 */
	@Override
	protected boolean beforeDelete ()
	{
		return delete_Accounting("C_BP_Group_Acct");
	}	//	beforeDelete

	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MBPGroup[").append(get_ID())
	        .append("-").append(getName());
	    sb.append("]");
	    return sb.toString();
    }	//	toString

}	//	MBPGroup
