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
 *	Service Level Agreement Goals
 *	
 *  @author Jorg Janke
 *  @version $Id: MSLAGoal.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MSLAGoal extends X_PA_SLA_Goal
{
    /** Logger for class MSLAGoal */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MSLAGoal.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_SLA_Goal_ID id
	 *	@param trx transaction
	 */
	public MSLAGoal (Ctx ctx, int PA_SLA_Goal_ID, Trx trx)
	{
		super (ctx, PA_SLA_Goal_ID, trx);
		if (PA_SLA_Goal_ID == 0)
		{
			setMeasureActual (Env.ZERO);
			setMeasureTarget (Env.ZERO);
			setProcessed (false);
		}
	}	//	MSLAGoal

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MSLAGoal (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MSLAGoal

	/**
	 * 	Get All Measures
	 *	@return array of measures
	 */
	public MSLAMeasure[] getAllMeasures()
	{
		String sql = "SELECT * FROM PA_SLA_Measure "
			+ "WHERE PA_SLA_Goal_ID=? "
			+ "ORDER BY DateTrx";
		return getMeasures (sql);
	}	//	getAllMeasures

	/**
	 * 	Get New Measures only
	 *	@return array of unprocessed Measures
	 */
	public MSLAMeasure[] getNewMeasures()
	{
		String sql = "SELECT * FROM PA_SLA_Measure "
			+ "WHERE PA_SLA_Goal_ID=?"
			+ " AND Processed='N' "
			+ "ORDER BY DateTrx";
		return getMeasures (sql);
	}	//	getNewMeasures
	
	/**
	 * 	Get Measures
	 *	@param sql sql
	 *	@return array of measures
	 */
	private MSLAMeasure[] getMeasures (String sql)
	{
		ArrayList<MSLAMeasure> list = new ArrayList<MSLAMeasure>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getPA_SLA_Goal_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MSLAMeasure(getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MSLAMeasure[] retValue = new MSLAMeasure[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getMeasures
	
	/**
	 * 	Is the Date in the Valid Range
	 *	@param date date
	 *	@return true if valid
	 */
	public boolean isDateValid (Timestamp date)
	{
		if (date == null)
			return false;
		if (getValidFrom() != null && date.before(getValidFrom()))
			return false;
		if (getValidTo() != null && date.after(getValidTo()))
			return false;
		return true;
	}	//	isDateValid
	
}	//	MSLAGoal
