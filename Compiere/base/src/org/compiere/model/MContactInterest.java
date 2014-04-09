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
 *  Business Partner Contact Interest.
 *  Compiere complies with spam laws.
 *  If the opt out date is set (by the user), 
 *  you should not subscribe the user again.
 *  Internally, the isActive flag is used.
 *
 *  @author Jorg Janke
 *  @version $Id: MContactInterest.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MContactInterest extends X_R_ContactInterest
{
    /** Logger for class MContactInterest */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MContactInterest.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Contact Interest
	 *	@param ctx context
	 *	@param R_InterestArea_ID interest ares
	 *	@param AD_User_ID user
	 * 	@param isActive create as active
	 *	@param trx transaction
	 *	@return Contact Interest 
	 */
	public static MContactInterest get (Ctx ctx, 
		int R_InterestArea_ID, int AD_User_ID, boolean isActive, Trx trx)
	{
		MContactInterest retValue = null;
		String sql = "SELECT * FROM R_ContactInterest "
			+ "WHERE R_InterestArea_ID=? AND AD_User_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, R_InterestArea_ID);
			pstmt.setInt(2, AD_User_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MContactInterest (ctx, rs, trx);
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
		//	New
		if (retValue == null)
		{
			retValue = new MContactInterest (ctx, R_InterestArea_ID, AD_User_ID, 
				isActive, trx);
			s_log.fine("NOT found - " + retValue);
		}
		else
			s_log.fine("Found - " + retValue);
		return retValue;
	}	//	get

	
	/**************************************************************************
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trx transaction
	 */
	public MContactInterest (Ctx ctx, int ignored, Trx trx)
	{
		super(ctx, 0, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MContactInterest

	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param R_InterestArea_ID interest area
	 * 	@param AD_User_ID partner contact
	 * 	@param isActive create as active
	 *	@param trx transaction
	 */
	public MContactInterest (Ctx ctx, int R_InterestArea_ID, int AD_User_ID, 
		boolean isActive, Trx trx)
	{
		super(ctx, 0, trx);
		setR_InterestArea_ID (R_InterestArea_ID);
		setAD_User_ID (AD_User_ID);
		setIsActive(isActive);
	}	//	MContactInterest

	/**
	 *  Create & Load existing Persistent Object.
	 *  @param ctx context
	 *  @param rs load from current result set position (no navigation, not closed)
	 *	@param trx transaction
	 */
	public MContactInterest (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MContactInterest

	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (MContactInterest.class);

	/**
	 * 	Set OptOut Date
	 * 	User action only.
	 *	@param OptOutDate date
	 */
	@Override
	public void setOptOutDate (Timestamp OptOutDate)
	{
		if (OptOutDate == null)
			OptOutDate = new Timestamp(System.currentTimeMillis());
		log.fine("" + OptOutDate);
		super.setOptOutDate(OptOutDate);
		setIsActive(false);
	}	//	setOptOutDate
	
	/**
	 * 	Unsubscribe.
	 * 	User action only.
	 */
	public void unsubscribe()
	{
		setOptOutDate(null);
	}	//	unsubscribe

	/**
	 * 	Is Opted Out
	 *	@return true if opted out
	 */
	public boolean isOptOut()
	{
		return getOptOutDate() != null;
	}	//	isOptOut
	
	/**
	 * 	Set Subscribe Date
	 * 	User action only.
	 *	@param SubscribeDate date
	 */
	@Override
	public void setSubscribeDate (Timestamp SubscribeDate)
	{
		if (SubscribeDate == null)
			SubscribeDate = new Timestamp(System.currentTimeMillis());
		log.fine("" + SubscribeDate);
		super.setSubscribeDate(SubscribeDate);
		super.setOptOutDate(null);
		setIsActive(true);
	}	//	setSubscribeDate

	/**
	 * 	Subscribe
	 * 	User action only.
	 */
	public void subscribe()
	{
		setSubscribeDate(null);
		if (!isActive())
			setIsActive(true);
	}	//	subscribe

	/**
	 * 	Subscribe.
	 * 	User action only.
	 * 	@param subscribe subscribe
	 */
	public void subscribe (boolean subscribe)
	{
		if (subscribe)
			setSubscribeDate(null);
		else
			setOptOutDate(null);
	}	//	subscribe


	/**
	 * 	Is Subscribed.
	 * 	Active is set internally, 
	 * 	the opt out date is set by the user via the web UI.
	 *	@return true if subscribed
	 */
	public boolean isSubscribed()
	{
		return isActive() && getOptOutDate() == null;
	}	//	isSubscribed

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (success && newRecord && isSubscribed())
		{
			MInterestArea ia = MInterestArea.get(getCtx(), getR_InterestArea_ID());
			if (ia.getR_Source_ID() != 0)
			{
				String summary = "Subscribe: " + ia.getName();
				//
				MSource source = MSource.get(getCtx(), ia.getR_Source_ID());
				MUser user = null;
				if (get_Trx() == null)
					user = MUser.get(getCtx(), getAD_User_ID());
				else
					user = new MUser (getCtx(), getAD_User_ID(), get_Trx());
				//	Create Request
				if (X_R_Source.SOURCECREATETYPE_Both.equals(source.getSourceCreateType())
					|| X_R_Source.SOURCECREATETYPE_Request.equals(source.getSourceCreateType()))
				{
					MRequest request = new MRequest(getCtx(), 0, get_Trx());
					request.setClientOrg(this);
					request.setSummary(summary);
					request.setAD_User_ID(getAD_User_ID());
					request.setC_BPartner_ID(user.getC_BPartner_ID());
					request.setR_Source_ID(source.getR_Source_ID());
					request.save();
				}
				//	Create Lead
				if (X_R_Source.SOURCECREATETYPE_Both.equals(source.getSourceCreateType())
					|| X_R_Source.SOURCECREATETYPE_Lead.equals(source.getSourceCreateType()))
				{
					MLead lead = new MLead(getCtx(), 0, get_Trx());
					lead.setClientOrg(this);
					lead.setDescription(summary);
					lead.setAD_User_ID(getAD_User_ID());
					lead.setR_InterestArea_ID(getR_InterestArea_ID());
					lead.setC_BPartner_ID(user.getC_BPartner_ID());
					lead.setR_Source_ID(source.getR_Source_ID());
					lead.save();
				}
			}
		}
	    return success;
	}	//	afterSave

	/**
	 * 	String representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MContactInterest[")
			.append("R_InterestArea_ID=").append(getR_InterestArea_ID())
			.append(",AD_User_ID=").append(getAD_User_ID())
			.append(",Subscribed=").append(isSubscribed())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**************************************************************************
	 * 	@param args ignored
	 */
	public static void main (String[] args)
	{
		org.compiere.Compiere.startup(true);
		int R_InterestArea_ID = 1000002;
		int AD_User_ID = 1000002;
		MContactInterest ci = MContactInterest.get(Env.getCtx(), R_InterestArea_ID, AD_User_ID, false, null);
		ci.subscribe();
		ci.save();
		//
		ci = MContactInterest.get(Env.getCtx(), R_InterestArea_ID, AD_User_ID, false, null);
	}	//	main


}	//	MContactInterest
