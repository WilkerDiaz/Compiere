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
 * 	Component Registration
 *	@author Jorg Janke
 */
public class MComponentReg extends X_AD_ComponentReg
{
    /** Logger for class MComponentReg */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MComponentReg.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get MComponentReg from Cache
	 *	@param ctx context
	 *	@param AD_ComponentReg_ID id
	 *	@return registration
	 */
	public static MComponentReg get (Ctx ctx, int AD_ComponentReg_ID)
	{
		Integer key = Integer.valueOf (AD_ComponentReg_ID);
		MComponentReg retValue = s_cacheID.get(ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MComponentReg (ctx, AD_ComponentReg_ID, null);
		if (retValue.get_ID () != 0)
			s_cacheID.put(key, retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Get Component Registration
	 *	@param ctx context
	 *	@param entityType entity Type
	 *	@return registration or null
	 */
	public static MComponentReg get(Ctx ctx, String entityType)
	{
		return get(ctx, entityType, false);
	}  // get()
	
	/**
	 * 	Get Component Registration
	 *	@param ctx context
	 *	@param entityType entity Type
	 *	@return registration or null
	 */
	public static MComponentReg get(Ctx ctx, String entityType, boolean reload)
	{
		if (entityType == null || entityType.length() == 0)
			return null;
		
		MComponentReg retValue = null;
		if (!reload)
		{
			retValue = s_cacheET.get(ctx, entityType);
			if (retValue != null)
				return retValue;
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM AD_ComponentReg WHERE ComponentName=?";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, entityType);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MComponentReg(ctx, rs, null);

		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (retValue != null)
			s_cacheET.put(entityType, retValue);
		return retValue;
	}	//	get
	
	/**	Cache						*/
	private static final CCache<String,MComponentReg> s_cacheET 
		= new CCache<String,MComponentReg>("AD_ComponentReg", 20);  
	/**	Cache						*/
	private static final CCache<Integer,MComponentReg> s_cacheID 
		= new CCache<Integer,MComponentReg>("AD_ComponentReg", 20);  

	/**	Logger						*/
	protected static final CLogger s_log = CLogger.getCLogger(MComponentReg.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_ComponentReg_ID id
	 *	@param trx p_trx
	 */
	public MComponentReg(Ctx ctx, int AD_ComponentReg_ID, Trx trx)
	{
		super(ctx, AD_ComponentReg_ID, trx);
		if (AD_ComponentReg_ID == 0)
		{
		//	setAD_User_ID (0);
		//	setC_BPartner_ID (0);
		//	setName (null);
			setComponentType (COMPONENTTYPE_ApplicationComponent);	// A
			setDistributionType (DISTRIBUTIONTYPE_License);
			setIsApproved (false);
		}	
	}	//	MComponentReg

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MComponentReg(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MComponentReg

	/**
	 * 	Is This a Trial
	 *	@return true if trial
	 */
	public boolean isTrial()
	{
		return getTrialPhaseDays() > 0;
	}	//	isTrial
	
	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MComponentReg[")
			.append (get_ID()).append ("-").append (toStringX()).append ("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Extended Representation
	 * 	@return info
	 */
	@Override
	public String toStringX()
	{
		StringBuffer sb = new StringBuffer(":")
			.append (getComponentName()).append (": ").append (getName());
		if (!Util.isEmpty(getVersion()))
			sb.append(" - ").append(getVersion());
		if (isApproved())
			sb.append("; Approved");
		else
			sb.append("; Approval Pending");
		//
		if (getTrialPhaseDays() > 0)
			sb.append("; TrialPhaseDays=").append(getTrialPhaseDays());
		String type = MRefList.getListName(getCtx(), X_Ref_AD_ComponentReg_Type.AD_Reference_ID, getComponentType());
		if (type != null)
			sb.append("; Type=").append(type);
		type = MRefList.getListName(getCtx(), X_Ref_AD_Component_Distribution.AD_Reference_ID, getDistributionType());
		if (type != null)
			sb.append("; Distribution=").append(type);
		return sb.toString();
	}	//	toStringX
	
}	//	MComponentReg
