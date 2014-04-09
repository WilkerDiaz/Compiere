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

import java.awt.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Performance Goal
 *	
 *  @author Jorg Janke
 *  @version $Id: MGoal.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MGoal extends X_PA_Goal
{
    /** Logger for class MGoal */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MGoal.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get User Goals
	 *	@param ctx context
	 *	@param AD_User_ID user
	 *	@return array of goals
	 */
	public static MGoal[] getUserGoals(Ctx ctx)
	{
		int AD_Role_ID = ctx.getAD_Role_ID();
		MRole role = MRole.get(ctx, AD_Role_ID);
		int AD_User_ID = ctx.getAD_User_ID();
		
		if (AD_User_ID < 0)
			return getTestGoals(ctx);
		ArrayList<MGoal> list = new ArrayList<MGoal>();
		String sql = "SELECT * FROM PA_Goal g "
			+ "WHERE IsActive='Y'"
			+ " AND AD_Client_ID=?"	//	#1
			+ " AND (";
		if (!role.isWebStoreRole())
			sql += " (AD_User_ID IS NULL AND AD_Role_ID IS NULL) OR ";
		sql += " AD_User_ID=?"		//	#2
				+ " OR EXISTS (SELECT * FROM AD_User_Roles ur "
					+ "WHERE ?=ur.AD_User_ID AND g.AD_Role_ID=ur.AD_Role_ID AND ur.IsActive='Y')) "
			+ "ORDER BY SeqNo";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, ctx.getAD_Client_ID());
			pstmt.setInt (2, AD_User_ID);
			pstmt.setInt (3, AD_User_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MGoal goal = new MGoal (ctx, rs, null);
				goal.updateGoal(false);
				list.add (goal);
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
		MGoal[] retValue = new MGoal[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getUserGoals

	/**
	 * 	Get Accessible Goals
	 *	@param ctx context
	 *	@return array of goals
	 */
	public static MGoal[] getGoals(Ctx ctx)
	{
		ArrayList<MGoal> list = new ArrayList<MGoal>();
		String sql = "SELECT * FROM PA_Goal WHERE IsActive='Y' "
			+ "ORDER BY SeqNo";
		sql = MRole.getDefault(ctx, false).addAccessSQL(sql, "PA_Goal", 
			false, true);	//	RW to restrict Access
        PreparedStatement pstmt = null;
        ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MGoal goal = new MGoal (ctx, rs, null);
				goal.updateGoal(false);
				list.add (goal);
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
		MGoal[] retValue = new MGoal[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getGoals

	
	/**
	 * 	Create Test Goals
	 *	@param ctx context
	 *	@return array of goals
	 */
	public static MGoal[] getTestGoals(Ctx ctx)
	{
		MGoal[] retValue = new MGoal[4];
		retValue[0] = new MGoal (ctx, "Test 1", "Description 1", new BigDecimal (1000), null); 
		retValue[0].setMeasureActual(new BigDecimal (200)); 
		retValue[1] = new MGoal (ctx, "Test 2", "Description 2", new BigDecimal (1000), null); 
		retValue[1].setMeasureActual(new BigDecimal (900)); 
		retValue[2] = new MGoal (ctx, "Test 3", "Description 3", new BigDecimal (1000), null); 
		retValue[2].setMeasureActual(new BigDecimal (1200)); 
		retValue[3] = new MGoal (ctx, "Test 4", "Description 4", new BigDecimal (1000), null); 
		retValue[3].setMeasureActual(new BigDecimal (3200)); 
		return retValue;
	}	//	getTestGoals

	/**
	 * 	Get Goals with Measure
	 *	@param ctx context
	 *	@param PA_Measure_ID measure
	 *	@return goals
	 */
	public static MGoal[] getMeasureGoals (Ctx ctx, int PA_Measure_ID)
	{
		ArrayList<MGoal> list = new ArrayList<MGoal>();
		String sql = "SELECT * FROM PA_Goal WHERE IsActive='Y' AND PA_Measure_ID=? "
			+ "ORDER BY SeqNo";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, PA_Measure_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MGoal (ctx, rs, null));
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
		MGoal[] retValue = new MGoal[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getMeasureGoals
	
	/**
	 * 	Get Multiplier from Scope to Display
	 *	@param goal goal
	 *	@return null if error or multiplier
	 */
	public static BigDecimal getMultiplier (MGoal goal)
	{
		String MeasureScope = goal.getMeasureScope();
		String MeasureDisplay = goal.getMeasureDisplay();
		if (MeasureDisplay == null
			|| MeasureScope.equals(MeasureDisplay))
			return Env.ONE;		//	1:1
		
		if (MeasureScope.equals(MEASURESCOPE_Total) 
			||  MeasureDisplay.equals(MEASUREDISPLAY_Total))
			return null;		//	Error

		BigDecimal Multiplier = null;
		if (MeasureScope.equals(MEASURESCOPE_Year))
		{
			if (MeasureDisplay.equals(MEASUREDISPLAY_Quarter))
				Multiplier = new BigDecimal(1.0/4.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Month))
				Multiplier = new BigDecimal(1.0/12.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Week))
				Multiplier = new BigDecimal(1.0/52.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Day))
				Multiplier = new BigDecimal(1.0/364.0);
		}
		else if (MeasureScope.equals(MEASURESCOPE_Quarter))
		{
			if (MeasureDisplay.equals(MEASUREDISPLAY_Year))
				Multiplier = new BigDecimal(4.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Month))
				Multiplier = new BigDecimal(1.0/3.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Week))
				Multiplier = new BigDecimal(1.0/13.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Day))
				Multiplier = new BigDecimal(1.0/91.0);
		}
		else if (MeasureScope.equals(MEASURESCOPE_Month))
		{
			if (MeasureDisplay.equals(MEASUREDISPLAY_Year))
				Multiplier = new BigDecimal(12.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Quarter))
				Multiplier = new BigDecimal(3.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Week))
				Multiplier = new BigDecimal(1.0/4.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Day))
				Multiplier = new BigDecimal(1.0/30.0);
		}
		else if (MeasureScope.equals(MEASURESCOPE_Week))
		{
			if (MeasureDisplay.equals(MEASUREDISPLAY_Year))
				Multiplier = new BigDecimal(52.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Quarter))
				Multiplier = new BigDecimal(13.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Month))
				Multiplier = new BigDecimal(4.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Day))
				Multiplier = new BigDecimal(1.0/7.0);
		}
		else if (MeasureScope.equals(MEASURESCOPE_Day))
		{
			if (MeasureDisplay.equals(MEASUREDISPLAY_Year))
				Multiplier = new BigDecimal(364.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Quarter))
				Multiplier = new BigDecimal(91.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Month))
				Multiplier = new BigDecimal(30.0);
			else if (MeasureDisplay.equals(MEASUREDISPLAY_Week))
				Multiplier = new BigDecimal(7.0);
		}
		return Multiplier;
	}	//	getMultiplier
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MGoal.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_Goal_ID id
	 *	@param trx p_trx
	 */
	public MGoal (Ctx ctx, int PA_Goal_ID, Trx trx)
	{
		super (ctx, PA_Goal_ID, trx);
		if (PA_Goal_ID == 0)
		{
		//	setName (null);
		//	setAD_User_ID (0);
		//	setPA_ColorSchema_ID (0);
			setSeqNo (0);
			setIsSummary (false);
			setMeasureScope (MEASUREDISPLAY_Year);
			setGoalPerformance (Env.ZERO);
			setRelativeWeight (Env.ONE);
			setMeasureTarget (Env.ZERO);
			setMeasureActual (Env.ZERO);
		}
	}	//	MGoal

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MGoal (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MGoal

	/**
	 * 	Base Constructor
	 *	@param ctx context
	 *	@param Name Name
	 *	@param Description Decsription
	 *	@param MeasureTarget target
	 *	@param trx p_trx
	 */
	public MGoal (Ctx ctx, String Name, String Description,
		BigDecimal MeasureTarget, Trx trx)
	{
		super (ctx, 0, trx);
		setName(Name);
		setDescription(Description);
		setMeasureTarget(MeasureTarget);
	}	//	MGoal

	
	/** Restrictions					*/
	private MGoalRestriction[] 	m_restrictions = null;

	/**
	 * 	Get Restriction Lines
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public MGoalRestriction[] getRestrictions (boolean reload)
	{
		if (m_restrictions != null && !reload)
			return m_restrictions;
		ArrayList<MGoalRestriction> list = new ArrayList<MGoalRestriction>();
		//
		String sql = "SELECT * FROM PA_GoalRestriction "
			+ "WHERE PA_Goal_ID=? AND IsActive='Y' "
			+ "ORDER BY Org_ID, C_BPartner_ID, M_Product_ID";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getPA_Goal_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MGoalRestriction (getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		//
		m_restrictions = new MGoalRestriction[list.size ()];
		list.toArray (m_restrictions);
		return m_restrictions;
	}	//	getRestrictions

	/**
	 * 	Get Measure
	 *	@return measure or null
	 */
	public MMeasure getMeasure()
	{
		if (getPA_Measure_ID() != 0)
			return MMeasure.get(getCtx(), getPA_Measure_ID());
		return null;
	}	//	getMeasure
	
	
	/**************************************************************************
	 * 	Update/save Goals for the same measure
	 * 	@param force force to update goal (default once per day)
	 * 	@return true if updated
	 */
	public boolean updateGoal(boolean force)
	{
		log.config("Force=" + force);
		MMeasure measure = MMeasure.get(getCtx(), getPA_Measure_ID());
		if (force 
			|| getDateLastRun() == null
			|| !TimeUtil.isSameHour(getDateLastRun(), null))
		{
			if (measure.updateGoals())		//	saves
			{
				load(get_ID(), get_Trx());
				return true;
			}
		}
		return false;
	}	//	updateGoal
	
	/**
	 * 	Set Measure Actual
	 *	@param MeasureActual actual
	 */
	@Override
	public void setMeasureActual (BigDecimal MeasureActual)
	{
		if (MeasureActual == null)
			return;
		super.setMeasureActual (MeasureActual);
		setDateLastRun(new Timestamp(System.currentTimeMillis()));
		setGoalPerformance();
	}	//	setMeasureActual
	
	/**
	 * 	Calculate Performance Goal as multiplier
	 */
	public void setGoalPerformance ()
	{
		BigDecimal MeasureTarget = getMeasureTarget();
		BigDecimal MeasureActual = getMeasureActual();
		BigDecimal GoalPerformance = Env.ZERO;
		if (MeasureTarget.signum() != 0)
			GoalPerformance = MeasureActual.divide(MeasureTarget, 6, BigDecimal.ROUND_HALF_UP);
		super.setGoalPerformance (GoalPerformance);
	}	//	setGoalPerformance
	
	/**
	 * 	Get Goal Performance as Double
	 *	@return performance as multipier
	 */
	public double getGoalPerformanceDouble()
	{
		BigDecimal bd = getGoalPerformance();
		return bd.doubleValue();
	}	//	getGoalPerformanceDouble
	
	/**
	 * 	Get Goal Performance in Percent
	 *	@return performance in percent
	 */
	public int getPercent()
	{
		BigDecimal bd = getGoalPerformance().multiply(Env.ONEHUNDRED);
		return bd.intValue();
	}	//	getPercent

	/**
	 * 	Get Color
	 *	@return color - white if no target
	 */
	public Color getColor()
	{
		if (getMeasureTarget().signum() == 0)
			return Color.white;
		else
			return MColorSchema.getColor(getCtx(), getPA_ColorSchema_ID(), getPercent());
	}	//	getColor

    /**
     * Get the color schema for this goal.
     * 
     * @return the color schema, or null if the measure targer is 0
     */
    public MColorSchema getColorSchema()
    {
        return (getMeasureTarget().signum() == 0) ?
            null : MColorSchema.get(getCtx(), getPA_ColorSchema_ID());
    }
	
	/**
	 * 	Get Measure Display
	 *	@return Measure Display
	 */
	@Override
	public String getMeasureDisplay ()
	{
		String s = super.getMeasureDisplay ();
		if (s == null)
		{
			if (MEASURESCOPE_Week.equals(getMeasureScope()))
				s = MEASUREDISPLAY_Week;
			else if (MEASURESCOPE_Day.equals(getMeasureScope()))
				s = MEASUREDISPLAY_Day;
			else
				s = MEASUREDISPLAY_Month;
		}
		return s;
	}	//	getMeasureDisplay
	
	/**
	 * 	Get Measure Display Text
	 *	@return Measure Display Text
	 */
	public String getXAxisText ()
	{
		MMeasure measure = getMeasure();
		if (measure != null 
			&& X_PA_Measure.MEASUREDATATYPE_StatusQtyAmount.equals(measure.getMeasureDataType()))
		{
			if (X_PA_Measure.MEASURETYPE_Request.equals(measure.getMeasureType()))
				return Msg.getElement(getCtx(), "R_Status_ID");
			if (X_PA_Measure.MEASURETYPE_Project.equals(measure.getMeasureType()))
				return Msg.getElement(getCtx(), "C_Phase_ID");
		}
		String value = getMeasureDisplay();
		String display = MRefList.getListName(getCtx(), X_Ref_PA_Goal_Scope.AD_Reference_ID, value);
		return display==null ? value : display;
	}	//	getMeasureDisplayText
	
	/**
	 * 	Goal has Target
	 *	@return true if target
	 */
	public boolean isTarget()
	{
		return getMeasureTarget().signum() != 0;
	}	//	isTarget
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MGoal[");
		sb.append (get_ID ())
			.append ("-").append (getName())
			.append(",").append(getGoalPerformance())
			.append ("]");
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
	//	if (getMultiplier(this) == null)	//	error
	//		setMeasureDisplay(getMeasureScope());
		
		//	Measure required if nor Summary
		if (!isSummary() && getPA_Measure_ID() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "PA_Measure_ID"));
			return false;
		}
		if (isSummary() && getPA_Measure_ID() != 0)
			setPA_Measure_ID(0);
		
		//	User/Role Check
		if ((newRecord || is_ValueChanged("AD_User_ID") || is_ValueChanged("AD_Role_ID"))
			&& getAD_User_ID() != 0)
		{
			MUser user = MUser.get(getCtx(), getAD_User_ID());
			MRole[] roles = user.getRoles(getAD_Org_ID());
			if (roles.length == 0)		//	No Role
				setAD_Role_ID(0);
			else if (roles.length == 1)	//	One
				setAD_Role_ID(roles[0].getAD_Role_ID());
			else
			{
				int AD_Role_ID = getAD_Role_ID();
				if (AD_Role_ID != 0)	//	validate
				{
					boolean found = false;
					for (MRole element : roles) {
						if (AD_Role_ID == element.getAD_Role_ID())
						{
							found = true;
							break;
						}
					}
					if (!found)
						AD_Role_ID = 0;
				}
				if (AD_Role_ID == 0)		//	set to first one
					setAD_Role_ID(roles[0].getAD_Role_ID());
			}	//	multiple roles
		}	//	user check

		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
		//	Update Goal if Target / Scope Changed
		if (newRecord 
			|| is_ValueChanged("MeasureTarget") 
			|| is_ValueChanged("MeasureScope"))
			updateGoal(true);

		return success;
	}
	
	
}	//	MGoal
