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
 * 	Accounting Balances Accumulation Model
 *	@author Jorg Janke
 */
public class MFactAccumulation extends X_Fact_Accumulation
{
    /** Logger for class MFactAccumulation */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MFactAccumulation.class);
	/** */
    private static final long serialVersionUID = 4694907481460935528L;

    
    /**
     * 	Get All for Tenant
     *	@param ctx context
     *	@param C_AcctSchema_ID optional acct schema
     *	@return list of Accumulation Rules ordered by AcctSchema and DateTo
     */
    public static ArrayList<MFactAccumulation> getAll (Ctx ctx, int C_AcctSchema_ID)
    {
    	StringBuffer sql = new StringBuffer("SELECT * FROM Fact_Accumulation "
    		+ "WHERE IsActive='Y' AND AD_Client_ID=? ");
    	if (C_AcctSchema_ID > 0)
    		sql.append("AND C_AcctSchema_ID=? ");
    	sql.append("ORDER BY C_AcctSchema_ID ");
    	ArrayList<MFactAccumulation> list = new ArrayList<MFactAccumulation>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
	        pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
	        pstmt.setInt(1, ctx.getAD_Client_ID());
	        if (C_AcctSchema_ID > 0)
	        	pstmt.setInt(2, C_AcctSchema_ID);
	        rs = pstmt.executeQuery();
	        while (rs.next())
		        list.add(new MFactAccumulation(ctx, rs, null));
        }
        catch (Exception e)
        {
        	s_log.log(Level.SEVERE, sql.toString(), e);
        }
        finally
        {
        	DB.closeResultSet(rs);
        	DB.closeStatement(pstmt);
        }
        return list;
    }	//	getAll
    
    /**
     * 	Get Date From.
     * 	Assumes that closed periods are not changed
     *	@param accums accumulations with same acct schema ordered by dateTo
     *	@param dateFrom original
     *	@return dateFrom
     */
    public static Timestamp getDateFrom(MFactAccumulation accum, Timestamp dateFrom)
    {
    	if (accum == null || dateFrom == null)
    		return dateFrom;
    	//
    	Timestamp earliestOK = new Timestamp(0L);
    	if (dateFrom != null)
    		earliestOK = dateFrom;
 
    	earliestOK = accum.getDateFrom(dateFrom);	//	fix start date;
    	
    	if (dateFrom != null && !dateFrom.equals(earliestOK))
    		s_log.info("Changed from " + dateFrom + " to " + earliestOK);
    	return earliestOK;
    }	//	getDateFrom
    
    /**	Logger	*/
    private static CLogger s_log = CLogger.getCLogger(MFactAccumulation.class);
    
    
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param Fact_Accumulation_ID id
	 *	@param trx p_trx
	 */
	public MFactAccumulation(Ctx ctx, int Fact_Accumulation_ID, Trx trx)
	{
		super(ctx, Fact_Accumulation_ID, trx);
	}	//	MFactAccumulation


	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MFactAccumulation(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MFactAccumulation
	
	/**
	 * 	Get the first Date From date based on Accumulation
	 *	@param from date
	 *	@return  first date
	 */
	public Timestamp getDateFrom (Timestamp from)
	{
		if (from == null)
			return from;
		
		if (BALANCEACCUMULATION_CalendarMonth.equals(getBalanceAccumulation()))
		{
			return TimeUtil.trunc(from, TimeUtil.TRUNC_MONTH);
		}
		else if (BALANCEACCUMULATION_CalendarWeek.equals(getBalanceAccumulation()))
		{
			return TimeUtil.trunc(from, TimeUtil.TRUNC_WEEK);
		}
		else if (BALANCEACCUMULATION_PeriodOfACompiereCalendar.equals(getBalanceAccumulation())
			&& getC_Calendar_ID() != 0)
		{
			MPeriod period = MPeriod.getOfCalendar(getCtx(), getC_Calendar_ID(), from);
			if(period!=null)
			{
				return period.getStartDate();
			}
		}
		return from;
	}	//	getDateFrom
	
	/**
	 * 	Get the first Date From date of the next period based on Accumulation
	 *	@param from date
	 *	@return  first date of next period
	 */
	public Timestamp getDateFromNext (Timestamp from)
	{
		if (from == null)
			return from;
		
		Timestamp retValue = from;
		if (BALANCEACCUMULATION_Daily.equals(getBalanceAccumulation()))
			return null;
		if (BALANCEACCUMULATION_CalendarMonth.equals(getBalanceAccumulation()))
		{
			retValue = TimeUtil.addMonths(from, 1);
			retValue = TimeUtil.trunc(retValue, TimeUtil.TRUNC_MONTH);
		}
		else if (BALANCEACCUMULATION_CalendarWeek.equals(getBalanceAccumulation()))
		{
			retValue = TimeUtil.addDays(from, 7);
			retValue = TimeUtil.trunc(retValue, TimeUtil.TRUNC_WEEK);
		}
		else if (BALANCEACCUMULATION_PeriodOfACompiereCalendar.equals(getBalanceAccumulation())
			&& getC_Calendar_ID() != 0)
		{
			
		}
		return retValue;
	}	//	getDateFromNext

	/**
	 * 	Before Save - Check Calendar
	 * 	@param newRecord new
	 * 	@return true if it can be saved
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		//	Calendar
		if (BALANCEACCUMULATION_PeriodOfACompiereCalendar.equals(getBalanceAccumulation()))
		{
			if (getC_Calendar_ID() == 0)
			{
				log.saveError("FillMandatory", Msg.getElement(getCtx(), "C_Calendar_ID"));
				return false;
			}
		}
		else if (getC_Calendar_ID() != 0)
			setC_Calendar_ID(0);
				
		if(isDefault())
		{
			boolean exists = false;
			String sql = "SELECT * FROM Fact_Accumulation "
				+ " WHERE IsDefault = 'Y'"
				+ " AND IsActive = 'Y' "
				+ " AND AD_Client_ID = ? "
				+ " AND Fact_Accumulation_ID <> ?";
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getAD_Client_ID());
				pstmt.setInt(2, getFact_Accumulation_ID());
				rs = pstmt.executeQuery ();
				if (rs.next ())
				{
					exists = true;
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
			if(exists)
			{
				log.saveError("DefaultExists","Default Balance aggregation already exists");
				return false;
			}
		}
		if(!newRecord)
		{
			if(is_ValueChanged("C_AcctSchema_ID") ||
					is_ValueChanged("BalanceAccumulation")||
					is_ValueChanged("C_Calendar_ID") ||
					is_ValueChanged("IsActivity") ||
					is_ValueChanged("IsBudget") ||
					is_ValueChanged("IsBusinessPartner") ||
					is_ValueChanged("IsCampaign") ||
					is_ValueChanged("IsLocationFrom") ||
					is_ValueChanged("IsLocationTo") ||
					is_ValueChanged("IsProduct") ||
					is_ValueChanged("IsProject") ||
					is_ValueChanged("IsSalesRegion") ||
					is_ValueChanged("IsUserList1") ||
					is_ValueChanged("IsUserList2"))
			{
				boolean exists = false;
				String sql = "SELECT * FROM Fact_Acct_Balance "
					+ " WHERE Fact_Accumulation_ID = ?";
		        PreparedStatement pstmt = null;
		        ResultSet rs = null;
				try
				{
					pstmt = DB.prepareStatement(sql, get_Trx());
					pstmt.setInt (1, getFact_Accumulation_ID());
					rs = pstmt.executeQuery ();
					if (rs.next ())
					{
						exists = true;
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
				if(exists)
				{
					log.saveError("BalanceExists","Updates not allowed when balances exists for the Aggregation");
					return false;
				}
			}
		}
		
	    return true;
	}	//	beforeSave
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MFactAccumulation[")
	    	.append(get_ID())
	    	.append(",BalanceAccumulation=").append(getBalanceAccumulation());
	    if (getC_Calendar_ID() != 0)
	    	sb.append(",C_Calendar_ID=").append(getC_Calendar_ID());
	    sb.append("]");
	    return sb.toString();
    } //	toString
	
}	//	MFactAccumulation
