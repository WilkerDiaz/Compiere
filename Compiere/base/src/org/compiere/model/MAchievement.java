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
 * 	Performance Achievement
 *	
 *  @author Jorg Janke
 *  @version $Id: MAchievement.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MAchievement extends X_PA_Achievement
{
    /** Logger for class MAchievement */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAchievement.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get achieved Achievements Of Measure
	 *	@param measure Measure
	 *	@return array of Achievements
	 */
	public static MAchievement[] get (MMeasure measure)
	{
		return getOfMeasure(measure.getCtx(), measure.getPA_Measure_ID());
	}	//	get

	/**
	 * 	Get achieved Achievements Of Measure
	 * 	@param ctx context
	 * 	@param PA_Measure_ID measure id
	 *	@return array of Achievements
	 */
	public static MAchievement[] getOfMeasure (Ctx ctx, int PA_Measure_ID)
	{
		ArrayList<MAchievement> list = new ArrayList<MAchievement>();
		String sql = "SELECT * FROM PA_Achievement "
			+ "WHERE PA_Measure_ID=? AND IsAchieved='Y' ORDER BY SeqNo, DateDoc";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, PA_Measure_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MAchievement (ctx, rs, null));
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
		//
		MAchievement[] retValue = new MAchievement[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfMeasure


	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MAchievement.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_Achievement_ID id
	 *	@param trx p_trx
	 */
	public MAchievement (Ctx ctx, int PA_Achievement_ID, Trx trx)
	{
		super (ctx, PA_Achievement_ID, trx);
	}	//	MAchievement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MAchievement (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MAchievement
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAchievement[");
		sb.append (get_ID()).append ("-").append (getName()).append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (isAchieved())
		{
			if (getManualActual().signum() == 0)
				setManualActual(Env.ONE);
			if (getDateDoc() == null)
				setDateDoc(new Timestamp(System.currentTimeMillis()));
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (success)
			updateAchievementGoals();
		return success;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		if (success)
			updateAchievementGoals();
		return success;
	}	//	afterDelete

	/**
	 * 	Update Goals with Achievement
	 */
	private void updateAchievementGoals()
	{
		MMeasure measure = MMeasure.get (getCtx(), getPA_Measure_ID());
		measure.updateGoals();
	}	//	updateAchievementGoals
	
}	//	MAchievement
