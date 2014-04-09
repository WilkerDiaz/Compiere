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


import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *  Calendar Period Model
 *
 *	@author Jorg Janke
 *	@version $Id: MPeriod.java,v 1.4 2006/07/30 00:51:05 jjanke Exp $
 */
public class MPeriod extends X_C_Period
{
	/** Logger for class MPeriod */
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPeriod.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Period from Cache
	 *	@param ctx context
	 *	@param C_Period_ID id
	 *	@return MPeriod
	 */
	public static MPeriod get (Ctx ctx, int C_Period_ID)
	{
		Integer key = Integer.valueOf (C_Period_ID);
		MPeriod retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MPeriod (ctx, C_Period_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} 	//	get

	/**
	 * 	Find standard Period of DateAcct based on Client Calendar
	 *	@param ctx context
	 *	@param DateAcct date
	 *	@return active Period or null
	 */
	public static MPeriod getOfOrg (Ctx ctx, int AD_Org_ID,  Timestamp DateAcct)
	{
		int C_Calendar_ID = 0;
		if (AD_Org_ID != 0)
		{
			MOrgInfo info = MOrgInfo.get(ctx, AD_Org_ID, null);
			C_Calendar_ID = info.getC_Calendar_ID();
		}
		if (C_Calendar_ID == 0)
		{
			MClientInfo cInfo = MClientInfo.get(ctx);
			C_Calendar_ID = cInfo.getC_Calendar_ID();
		}

		return getOfCalendar(ctx, C_Calendar_ID, DateAcct);
	}	//	get

	/**
	 * 	Find standard Period of DateAcct based on Client Calendar
	 *	@param ctx context
	 *	@param C_Calendar_ID calendar
	 *	@param DateAcct date
	 *	@return active Period or null
	 */
	public static MPeriod getOfCalendar (Ctx ctx, int C_Calendar_ID, Timestamp DateAcct)
	{
		if (DateAcct == null)
		{
			s_log.warning("No DateAcct");
			return null;
		}
		if (C_Calendar_ID == 0)
		{
			s_log.warning("No Calendar");
			return null;
		}
		//	Search in Cache first
		Iterator<MPeriod> it = s_cache.values().iterator();
		while (it.hasNext())
		{
			MPeriod period = it.next();
			if (period.getC_Calendar_ID() == C_Calendar_ID 
					&& period.isStandardPeriod() 
					&& period.isInPeriod(DateAcct))
				return period;
		}

		//	Get it from DB
		MPeriod retValue = null;
		String sql = "SELECT * FROM C_Period "
			+ "WHERE C_Year_ID IN "
			+ "(SELECT C_Year_ID FROM C_Year WHERE C_Calendar_ID=?)"
			+ " AND ? BETWEEN TRUNC(StartDate,'DD') AND TRUNC(EndDate,'DD')"
			+ " AND IsActive='Y' AND PeriodType='S'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, C_Calendar_ID);
			pstmt.setTimestamp (2, TimeUtil.getDay(DateAcct));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MPeriod period = new MPeriod(ctx, rs, null);
				Integer key = Integer.valueOf (period.getC_Period_ID());
				s_cache.put (key, period);
				if (period.isStandardPeriod())
					retValue = period;
			}
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, "DateAcct=" + DateAcct, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		if (retValue == null)
			s_log.warning("No Standard Period for " + DateAcct 
					+ " (C_Calendar_ID=" + C_Calendar_ID + ")");
		return retValue;
	}	//	get


	/**
	 * 	Find valid standard Period of DateAcct based on Client Calendar
	 *	@param ctx context
	 *	@param DateAcct date
	 *	@return C_Period_ID or 0
	 */
	public static int getC_Period_ID (Ctx ctx, int AD_Org_ID, Timestamp DateAcct)
	{
		MPeriod period = getOfOrg(ctx, AD_Org_ID, DateAcct);
		if (period == null)
			return 0;
		return period.getC_Period_ID();
	}	//	getC_Period_ID

	/**
	 * 	Is standard Period Open for Document Base Type - does not check Orgs
	 *	@param ctx context
	 *	@param DateAcct date
	 *	@param DocBaseType base type
	 *	@return true if open
	 *	@deprecated use new isOpen
	 */
	@Deprecated
	public static boolean isOpenOld (Ctx ctx, Timestamp DateAcct, String DocBaseType)
	{
		if (DateAcct == null)
		{
			s_log.warning("No DateAcct");
			return false;
		}
		if (DocBaseType == null)
		{
			s_log.warning("No DocBaseType");
			return false;
		}
		MPeriod period = MPeriod.getOfOrg(ctx, 0, DateAcct);
		if (period == null)
		{
			s_log.warning("No Period for " + DateAcct + " (" + DocBaseType + ")");
			return false;
		}
		String error = period.isOpen(DocBaseType, DateAcct);
		if (error != null)
			s_log.warning(error + " - " + period.getName());
		return error == null;
	}	//	isOpen


	/**
	 * Is standard Period Open for specified orgs for the client. For best
	 * performance, ensure that the list of orgs does not contain duplicates.
	 * 
	 * @param ctx
	 * @param AD_Client_ID
	 * @param orgs
	 * @param DateAcct
	 *            accounting date
	 * @param DocBaseType
	 *            document base type
	 * @return error message or null
	 */
	public static String isOpen(Ctx ctx, int AD_Client_ID, ArrayList<Integer> orgs, Timestamp DateAcct,
			String DocBaseType)	{
		if (DateAcct == null)
			return "@NotFound@ @DateAcct@";
		if (DocBaseType == null)
			return "@NotFound@ @DocBaseType@";

		MAcctSchema as = MClient.get(ctx, AD_Client_ID).getAcctSchema();
		if (as == null)
			return "@NotFound@ @C_AcctSchema_ID@ for AD_Client_ID=" + AD_Client_ID;
		if (as.isAutoPeriodControl())
		{
			if (as.isAutoPeriodControlOpen(DateAcct))
				return null;
			else
				return "@PeriodClosed@ - @AutoPeriodControl@";
		}

		//	Get all Calendars in line with Organizations
		MClientInfo clientInfo = MClientInfo.get(ctx, AD_Client_ID, null);
		ArrayList<Integer> orgCalendars = new ArrayList<Integer>();
		ArrayList<Integer> calendars = new ArrayList<Integer>();
		for (int org : orgs)
		{
			MOrgInfo orgInfo = MOrgInfo.get(ctx, org, null);
			int C_Calendar_ID = orgInfo.getC_Calendar_ID();
			if (C_Calendar_ID == 0)
				C_Calendar_ID = clientInfo.getC_Calendar_ID();
			orgCalendars.add(C_Calendar_ID);
			if (!calendars.contains(C_Calendar_ID))
				calendars.add(C_Calendar_ID);
		}
		//	Should not happen
		if (calendars.size() == 0)
			return "@NotFound@ @C_Calendar_ID@";

		//	For all Calendars get Periods
		for (int i = 0; i < calendars.size(); i++)
		{
			int C_Calendar_ID = calendars.get(i);
			MPeriod period = MPeriod.getOfCalendar (ctx, C_Calendar_ID, DateAcct);
			//	First Org for Calendar
			int AD_Org_ID = 0;
			for (int j = 0; j < orgCalendars.size(); j++)
			{
				if (orgCalendars.get(j) == C_Calendar_ID)
				{
					AD_Org_ID = orgs.get(j);
					break;
				}
			}
			if (period == null)
			{
				MCalendar cal = MCalendar.get(ctx, C_Calendar_ID);
				String date = DisplayType.getDateFormat(DisplayTypeConstants.Date)
				.format(DateAcct);
				if (cal != null)
					return "@NotFound@ @C_Period_ID@: " + date
					+ " - " + MOrg.get(ctx, AD_Org_ID).getName() 
					+ " -> " + cal.getName();
				else
					return "@NotFound@ @C_Period_ID@: " + date
					+ " - " + MOrg.get(ctx, AD_Org_ID).getName() 
					+ " -> C_Calendar_ID=" + C_Calendar_ID;
			}
			String error = period.isOpen(DocBaseType, DateAcct);
			if (error != null)
				return error
				+ " - " + MOrg.get(ctx, AD_Org_ID).getName()
				+ " -> " + MCalendar.get(ctx, C_Calendar_ID).getName();
		}
		return null;	//	open
	}	//	isOpen


	/**
	 * Is standard Period Open for Document Base Type
	 * 
	 * @param header
	 *            header document record
	 * @param lines
	 *            document lines optional
	 * @param DateAcct
	 *            accounting date
	 * @param DocBaseType
	 *            document base type
	 * @return error message or null
	 */
	@Deprecated
	public static String isOpen(PO header, PO[] lines, Timestamp DateAcct, String DocBaseType) {

		// Get All Orgs
		ArrayList<Integer> orgs = new ArrayList<Integer>();
		orgs.add(header.getAD_Org_ID());
		if (lines != null) {
			for (PO line : lines) {
				int AD_Org_ID = line.getAD_Org_ID();
				if (!orgs.contains(AD_Org_ID))
					orgs.add(AD_Org_ID);
			}
		}

		return isOpen(header.getCtx(), header.getAD_Client_ID(), orgs, DateAcct, DocBaseType);
	} // isOpen



	/**
	 * 	Is standard Period closed for all Document Base Types
	 *	@param ctx context for AD_Client
	 *	@param DateAcct accounting date
	 *	@return true if closed
	 */
	public static boolean isClosed (Ctx ctx, Timestamp DateAcct)
	{
		if (DateAcct == null)
			return false;
		MAcctSchema as = MClient.get(ctx, ctx.getAD_Client_ID())
		.getAcctSchema();
		if (as.isAutoPeriodControl())
			return !as.isAutoPeriodControlOpen(DateAcct);

		//	Get all Calendars in line with Organizations
		MClientInfo cInfo = MClientInfo.get(ctx, ctx.getAD_Client_ID(), null);
		ArrayList<Integer> calendars = new ArrayList<Integer>();
		MOrg[] orgs = MOrg.getOfClient(cInfo);
		for (MOrg org : orgs) {
			MOrgInfo info = MOrgInfo.get(ctx, org.getAD_Org_ID(), null);
			int C_Calendar_ID = info.getC_Calendar_ID();
			if (C_Calendar_ID == 0)
				C_Calendar_ID = cInfo.getC_Calendar_ID();
			if (!calendars.contains(C_Calendar_ID))
				calendars.add(C_Calendar_ID);
		}
		//	Should not happen
		if (calendars.size() == 0)
			throw new IllegalArgumentException("@NotFound@ @C_Calendar_ID@");

		//	For all Calendars get Periods
		for (int i = 0; i < calendars.size(); i++)
		{
			int C_Calendar_ID = calendars.get(i);
			MPeriod period = MPeriod.getOfCalendar (ctx, C_Calendar_ID, DateAcct);
			//	Period not found
			if (period == null)
				return false;
			if (!period.isClosed())
				return false;
		}
		return true;	//	closed
	}	//	isClosed


	/**
	 * 	Find first Year Period of DateAcct based on Client Calendar
	 *	@param ctx context
	 *	@param C_Calendar_ID calendar
	 *	@param DateAcct date
	 *	@return active first Period
	 */
	public static MPeriod getFirstInYear (Ctx ctx, int C_Calendar_ID, Timestamp DateAcct)
	{
		MPeriod retValue = null;
		String sql = "SELECT * "
			+ "FROM C_Period "
			+ "WHERE C_Year_ID IN "
			+ "(SELECT p.C_Year_ID "
			+ "FROM C_Year y"
			+ " INNER JOIN C_Period p ON (y.C_Year_ID=p.C_Year_ID) "
			+ "WHERE y.C_Calendar_ID=?"
			+ "	AND ? BETWEEN StartDate AND EndDate)"
			+ " AND IsActive='Y' AND PeriodType='S' "
			+ "ORDER BY StartDate";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, C_Calendar_ID);
			pstmt.setTimestamp (2, DateAcct);
			rs = pstmt.executeQuery();
			if (rs.next())	//	first only
				retValue = new MPeriod(ctx, rs, null);
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return retValue;
	}	//	getFirstInYear

	/**	Cache							*/
	private static final CCache<Integer,MPeriod> s_cache = new CCache<Integer,MPeriod>("C_Period", 10);

	/**	Logger							*/
	private static final CLogger		s_log = CLogger.getCLogger (MPeriod.class);


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Period_ID id
	 *	@param trx transaction
	 */
	public MPeriod (Ctx ctx, int C_Period_ID, Trx trx)
	{
		super (ctx, C_Period_ID, trx);
		if (C_Period_ID == 0)
		{
			//	setC_Period_ID (0);		//	PK
			//  setC_Year_ID (0);		//	Parent
			//  setName (null);
			//  setPeriodNo (0);
			//  setStartDate (new Timestamp(System.currentTimeMillis()));
			setPeriodType (PERIODTYPE_StandardCalendarPeriod);
		}
	}	//	MPeriod

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPeriod (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPeriod

	/**
	 * 	Parent constructor
	 *	@param year year
	 *	@param PeriodNo no
	 *	@param name name
	 *	@param startDate start
	 *	@param endDate end
	 */
	public MPeriod (MYear year, int PeriodNo, String name, 
			Timestamp startDate,Timestamp endDate)
	{
		this (year.getCtx(), 0, year.get_Trx());
		setClientOrg(year);
		setC_Year_ID(year.getC_Year_ID());
		setPeriodNo(PeriodNo);
		setName(name);
		setStartDate(startDate);
		setEndDate(endDate);

	}	//	MPeriod


	/**	Period Controls			*/
	private MPeriodControl[]	m_controls = null;
	/** Calendar				*/
	private int					m_C_Calendar_ID = 0;

	/**
	 * 	Get Period Control
	 *	@param requery requery
	 *	@return period controls
	 */
	public MPeriodControl[] getPeriodControls (boolean requery)
	{
		if (m_controls != null && !requery)
			return m_controls;
		//
		ArrayList<MPeriodControl> list = new ArrayList<MPeriodControl>();
		String sql = "SELECT * FROM C_PeriodControl "
			+ "WHERE C_Period_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Period_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add (new MPeriodControl (getCtx(), rs, get_Trx()));
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
		m_controls = new MPeriodControl[list.size ()];
		list.toArray (m_controls);
		return m_controls;
	}	//	getPeriodControls

	/**
	 * 	Get Period Control
	 *	@param DocBaseType Document Base Type
	 *	@return period control or null
	 */
	public MPeriodControl getPeriodControl (String DocBaseType)
	{
		if (DocBaseType == null)
			return null;
		getPeriodControls(false);
		for (MPeriodControl element : m_controls) {
			//	log.fine("getPeriodControl - " + 1 + " - " + m_controls[i]);
			if (DocBaseType.equals(element.getDocBaseType()))
				return element;
		}
		return null;
	}	//	getPeriodControl

	/**
	 * 	Date In Period
	 *	@param date date
	 *	@return true if in period
	 */
	public boolean isInPeriod (Timestamp date)
	{
		if (date == null)
			return false;
		Timestamp dateOnly = TimeUtil.getDay(date);
		Timestamp from = TimeUtil.getDay(getStartDate());
		if (dateOnly.before(from))
			return false;
		Timestamp to = TimeUtil.getDay(getEndDate());
		if (dateOnly.after(to))
			return false;
		return true;
	}	//	isInPeriod

	/**
	 * 	Is Period Open for Doc Base Type
	 *	@param DocBaseType document base type
	 *	@param dateAcct accounting date
	 *	@return error message or null
	 */
	public String isOpen (String DocBaseType, Timestamp dateAcct)
	{
		if (!isActive())
		{
			s_log.warning("Period not active: " + getName());
			return "@C_Period_ID@ <> @IsActive@";
		}

		MAcctSchema as = MClient.get(getCtx(), getAD_Client_ID()).getAcctSchema();
		if (as != null && as.isAutoPeriodControl())
		{
			if (!as.isAutoPeriodControlOpen(dateAcct))
				return "@PeriodClosed@ - @AutoPeriodControl@";
			//	We are OK
			Timestamp today = new Timestamp (System.currentTimeMillis());
			if (isInPeriod(today) && as.getC_Period_ID() != getC_Period_ID())
			{
				as.setC_Period_ID(getC_Period_ID());
				as.save();
			}
			return null;
		}

		//	Standard Period Control
		if (DocBaseType == null)
		{
			log.warning(getName() + " - No DocBaseType");
			return "@NotFound@ @DocBaseType@";
		}
		MPeriodControl pc = getPeriodControl (DocBaseType);
		if (pc == null)
		{
			log.warning(getName() + " - Period Control not found for " + DocBaseType);
			return "@NotFound@ @C_PeriodControl_ID@: " + DocBaseType;
		}
		log.fine(getName() + ": " + DocBaseType);
		if (pc.isOpen())
			return null;
		return "@PeriodClosed@ - @C_PeriodControl_ID@ (" 
		+ DocBaseType + ", " + dateAcct + ")";
	}	//	isOpen

	/**
	 * 	Return true if all PC are closed
	 *	@return true if closed
	 */
	public boolean isClosed()
	{
		MPeriodControl[] pcs = getPeriodControls(false);
		for (MPeriodControl pc : pcs)
		{
			if (!pc.isClosed())
				return false;
		}
		return true;
	}	//	isClosed

	/**
	 * 	Standard Period
	 *	@return true if standard calendar period
	 */
	public boolean isStandardPeriod()
	{
		return PERIODTYPE_StandardCalendarPeriod.equals(getPeriodType());
	}	//	isStandardPeriod

	/**
	 * 	Get Calendar of Period
	 *	@return calendar
	 */
	public int getC_Calendar_ID()
	{
		if (m_C_Calendar_ID == 0)
		{
			MYear year = MYear.get(getCtx(), getC_Year_ID());
			if (year != null)
				m_C_Calendar_ID = year.getC_Calendar_ID();
			else
				log.severe("@NotFound@ C_Year_ID=" + getC_Year_ID());
		}
		return m_C_Calendar_ID;
	}	//	getC_Calendar_ID

	/**
	 * 	Before Save.
	 * 	Truncate Dates
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		Timestamp startdate = getStartDate(); 
		Timestamp enddate = getEndDate();

		if ( enddate!=null && startdate.after(enddate))
		{

			s_log.saveError("Error", Msg.getMsg(getCtx(), "CalPeriodInvalidDate"));
			return false;
		}
		//	Truncate Dates
		startdate=TimeUtil.getDay(startdate);
		setStartDate(startdate);


		if (enddate != null)
			enddate=TimeUtil.getDay(enddate);
		else
			enddate = TimeUtil.getMonthLastDay(getStartDate());

//		Adding the time component of 23:59:59 to the end date
		enddate=  new Timestamp (enddate.getTime() + 86399000);
		setEndDate(enddate);

		MPeriod[]periods=getAllPeriodsInYear(getC_Year_ID(),"S",getCtx(),get_Trx());
		MPeriod[] allperiods=getAllPeriodsInCalendar(getC_Calendar_ID(),"S",getCtx(),get_Trx());
//		Check for non-negative period number
		if(getPeriodNo() < 0)
		{
			s_log.saveError("Error", Msg.getMsg(getCtx(), "CalNegPeriodNo"));
			return false;
		}

//		Check for standard period
		if( isStandardPeriod() == true)
		{			
			// Check Period number is in ascending order		

			Timestamp nextPeriodStartDate=null;
			Timestamp prevPeriodStartDate=null;

			//Get the next standard period number Start Date in this year		 
			String sql = "SELECT StartDate FROM C_Period WHERE " +
			"C_Period.IsActive='Y' AND PeriodType='S' " +
			"AND C_Period.C_Year_ID =? " +
			"AND C_Period.C_Period_ID <> ?"+
			"AND  C_Period.PeriodNo " +
			" >  ?  ORDER BY  C_Period.PeriodNo ASC";
			Object[][] result=null;
			result  = QueryUtil.executeQuery(get_Trx(), sql, getC_Year_ID(),getC_Period_ID(),getPeriodNo());

			if (result.length != 0) 
				nextPeriodStartDate= (Timestamp)result[0][0] ;		

			//Get the previous standard period number Start Date in this year	
			sql = "SELECT StartDate FROM C_Period WHERE " +
			"C_Period.IsActive='Y' AND PeriodType='S'  " +
			"AND C_Period.C_Year_ID =? " +
			"AND C_Period.C_Period_ID <> ?"+
			"AND C_Period.PeriodNo " +
			"< ?  ORDER BY  C_Period.PeriodNo DESC";

			result  = QueryUtil.executeQuery(get_Trx(), sql, getC_Year_ID(),getC_Period_ID(),getPeriodNo());
			if (result.length != 0) 
				prevPeriodStartDate= (Timestamp)result[0][0] ;

			if ( (prevPeriodStartDate != null && TimeUtil.max(prevPeriodStartDate ,startdate) == prevPeriodStartDate ) )
			{
				s_log.saveError("Error", Msg.getMsg(getCtx(), "CalPeriodAsc"));
				return false;
			}

			if ( (nextPeriodStartDate != null && TimeUtil.max(nextPeriodStartDate ,startdate) == startdate) )
			{
				s_log.saveError("Error", Msg.getMsg(getCtx(), "CalPeriodAsc"));
				return false;
			}	


			//  Check if the Standard Period is overlapping other periods.

			for (MPeriod period : allperiods){
				if ((TimeUtil.isValid(period.getStartDate(),period.getEndDate(),startdate) == true ||
						TimeUtil.isValid(period.getStartDate(),period.getEndDate(),enddate) == true) && period.getC_Period_ID()!= getC_Period_ID())	
				{
					s_log.saveError("Error", Msg.getMsg(getCtx(), "CalPeriodOverlap"));
					return false;
				} 
			}

		}
//		Check for adjusting period
		else
		{   
			boolean  startflag= false;
			boolean  endflag= false;
			for (MPeriod period : periods)
			{
				if (TimeUtil.isValid(period.getStartDate(),period.getEndDate(),startdate) == true)
					startflag= true;
				if (TimeUtil.isValid(period.getStartDate(),period.getEndDate(),enddate) == true)
					endflag= true;
				if (startflag == true && endflag == true)
					break;
			}
			if (startflag == false  || endflag == false)
			{
				s_log.saveError("Error", Msg.getMsg(getCtx(), "CalAdjPeriod"));
				return false;
			}
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
		if (newRecord)
		{			
			//	SELECT Value FROM AD_Ref_List WHERE AD_Reference_ID=183
			MDocType[] types = MDocType.getOfClient(getCtx());
			int count = 0;
			ArrayList<String> baseTypes = new ArrayList<String>();
			for (MDocType type : types) {
				String DocBaseType = type.getDocBaseType();
				if (baseTypes.contains(DocBaseType))
					continue;
				MPeriodControl pc = new MPeriodControl(this, DocBaseType);
				if (pc.save())
					count++;
				baseTypes.add (DocBaseType);
			}
			log.fine("PeriodControl #" + count);
		}
		return success;
	}	//	afterSave


	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPeriod[");
		sb.append (get_ID())
		.append("-").append (getName())
		.append(", ").append(getStartDate()).append("-").append(getEndDate())
		.append ("]");
		return sb.toString ();
	}	//	toString


	/**
	 * Returns the next period forward
	 * @param period MPeriod	 
	 * @param trx trx
	 * @param ctx Ctx
	 * @return MPeriod
	 */
	public static MPeriod getNextPeriod(MPeriod period, Ctx ctx, Trx trx) 
	{

		MPeriod newPeriod = null;
		String sql = "SELECT * FROM C_Period WHERE " +
		"C_Period.IsActive='Y' AND PeriodType='S' " +
		"AND C_Period.C_Year_ID IN " +
		"(SELECT C_Year_ID FROM C_Year WHERE C_Year.C_Calendar_ID = ? ) " +
		"AND ((C_Period.C_Year_ID * 1000) + C_Period.PeriodNo) " +
		" > ((? * 1000) + ?) ORDER BY C_Period.C_Year_ID ASC, C_Period.PeriodNo ASC";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);			
			pstmt.setInt (1, period.getC_Calendar_ID());
			pstmt.setInt(2,period.getC_Year_ID());
			pstmt.setInt(3 , period.getPeriodNo() );			
			rs = pstmt.executeQuery ();
			if (rs.next ())
				newPeriod = new MPeriod(ctx , rs, trx);
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
		return newPeriod;
	}

	/**
	 * Returns the previous period 
	 * @param period MPeriod
	 * @param periodCount Count 
	 * @param trx trx
	 * @param ctx Ctx
	 * @return MPeriod
	 */
	public static MPeriod getPreviousPeriod(MPeriod period, Ctx ctx, Trx trx) 
	{

		MPeriod newPeriod = null;
		String sql = "SELECT * FROM C_Period WHERE " +
		"C_Period.IsActive='Y' AND PeriodType='S' " +
		"AND C_Period.C_Year_ID IN " +
		"(SELECT C_Year_ID FROM C_Year WHERE C_Year.C_Calendar_ID = ? ) " +
		"AND ((C_Period.C_Year_ID * 1000) + C_Period.PeriodNo) " +
		" < ((? * 1000) + ?) ORDER BY C_Period.C_Year_ID DESC, C_Period.PeriodNo DESC";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);			
			pstmt.setInt (1, period.getC_Calendar_ID());
			pstmt.setInt(2,period.getC_Year_ID());
			pstmt.setInt(3 , period.getPeriodNo() );			
			rs = pstmt.executeQuery ();
			if (rs.next ())
				newPeriod = new MPeriod(ctx , rs, trx);
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return newPeriod;
	}

	/**
	 * gets all Periods in the Range
	 * @param startPeriod
	 * @param endPeriod
	 * @param calendar_ID
	 * @return MPeriod[]
	 */
	public static MPeriod[] getAllPeriodsInRange(MPeriod startPeriod,
			MPeriod endPeriod, int calendar_ID, Ctx ctx, Trx trx) 
	{		
		if( (startPeriod.getC_Calendar_ID() != calendar_ID  ) ||
				(endPeriod.getC_Calendar_ID() != calendar_ID))
		{
			log.saveError("Error", "Periods do not belong to the calendar");
			return null;
		}




		ArrayList<MPeriod> periods = new ArrayList<MPeriod>();
		String sql = "SELECT * FROM C_Period WHERE " +
		"C_Period.IsActive='Y' AND PeriodType='S' " +
		"AND C_Period.C_Year_ID IN " +
		"(SELECT C_Year_ID FROM C_Year WHERE C_Year.C_Calendar_ID = ? ) " + //calendar_ID
		"AND ((C_Period.C_Year_ID * 1000) + C_Period.PeriodNo) BETWEEN" +
		" (? * 1000 + ?) AND (? * 1000 + ? )" + //start Period year ID, Period Number , End Period Year ID, Period Number
		" ORDER BY C_Period.C_Year_ID ASC, C_Period.PeriodNo ASC";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);			
			pstmt.setInt (1, calendar_ID);
			pstmt.setInt(2,startPeriod.getC_Year_ID());
			pstmt.setInt(3 , startPeriod.getPeriodNo() );
			pstmt.setInt(4,endPeriod.getC_Year_ID());
			pstmt.setInt(5 , endPeriod.getPeriodNo() );
			rs = pstmt.executeQuery ();
			while (rs.next ())
				periods.add(new MPeriod(ctx , rs, trx));
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally{			
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MPeriod[] retValue = new MPeriod[periods.size()];
		periods.toArray(retValue);
		return retValue;

	}

	/**
	 * 	Find Period of Date based on Client Calendar, 
	 *  it need not be a standard period (used in MRP)
	 *	@param ctx context
	 *	@param C_Calendar_ID calendar
	 *	@param Date date
	 *  @param trx trx
	 *	@return active Period or null
	 */
	public static MPeriod getPeriod (Ctx ctx, int C_Calendar_ID, Timestamp Date , Trx trx)
	{
		if (Date == null)
		{
			s_log.warning("No Date");
			return null;
		}
		if (C_Calendar_ID == 0)
		{
			s_log.warning("No Calendar");
			return null;
		}		

		//	Get it from DB
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MPeriod retValue = null;
		String sql = "SELECT * FROM C_Period "
			+ "WHERE C_Year_ID IN "
			+ "(SELECT C_Year_ID FROM C_Year WHERE C_Calendar_ID=?)"
			+ " AND ? BETWEEN TRUNC(StartDate,'DD') AND TRUNC(EndDate,'DD')"
			+ " AND IsActive='Y' ";
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt (1, C_Calendar_ID);
			pstmt.setTimestamp (2, TimeUtil.getDay(Date));
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				retValue  = new MPeriod(ctx, rs, trx);					 
			}
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, "DateAcct=" + Date, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (retValue == null)
			s_log.warning("No Period for " + Date 
					+ " (C_Calendar_ID=" + C_Calendar_ID + ")");
		return retValue;
	}	//	getPeriod

	/**
	 * 	Find the periods in a calendar 
	 *  it need not be a standard period (used in MRP)
	 *	@param C_Calendar_ID calendar
	 *  @param periodType Period Type
	 *	@param ctx context
	 *  @param trx trx
	 *	@return  MPeriod[]
	 */

	public static MPeriod[] getAllPeriodsInCalendar(int C_Calendar_ID, String periodType, Ctx ctx, Trx trx) 
	{		

		List<MPeriod> periods = new ArrayList<MPeriod>();
		String sql = "SELECT * FROM C_Period WHERE IsActive='Y'";

		sql =sql+ " AND C_Year_ID IN ( SELECT C_Year_ID FROM C_Year WHERE C_Calendar_ID=?)" ; 

		if ( periodType !=  null ) 
			sql = sql + " AND PeriodType = ? " ;  

		sql =sql+ " ORDER BY StartDate ";	

		PreparedStatement pstmt = null;
		ResultSet rs =null ;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, C_Calendar_ID);

			if ( periodType !=  null ) 
				pstmt.setString(2,periodType );

			rs = pstmt.executeQuery ();
			while (rs.next ())
				periods.add(new MPeriod(ctx , rs, trx));			 
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally{			
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MPeriod[] retValue = new MPeriod[periods.size()];
		periods.toArray(retValue);
		return retValue;			 

	}

	/**
	 * 	Find the periods in a calendar year 
	 *  it need not be a standard period (used in MRP)
	 *  @param C_Year_ID Year
	 *  @param periodType Period Type
	 *	@param ctx context
	 *  @param trx trx
	 *	@return  MPeriod[]
	 */

	public static MPeriod[] getAllPeriodsInYear(int C_Year_ID, String periodType, Ctx ctx, Trx trx) 
	{		

		List<MPeriod> periods = new ArrayList<MPeriod>();
		String sql = "SELECT * FROM C_Period WHERE IsActive='Y'";

		sql =sql+ " AND C_Year_ID = ?" ; 

		if ( periodType !=  null ) 
			sql = sql + " AND PeriodType = ? " ;  

		sql =sql+ " order by StartDate ";	

		PreparedStatement pstmt = null;
		ResultSet rs =null ;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, C_Year_ID);

			if ( periodType !=  null ) 
				pstmt.setString(2,periodType );

			rs = pstmt.executeQuery ();
			while (rs.next ())
				periods.add(new MPeriod(ctx , rs, trx));			 
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally{			
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MPeriod[] retValue = new MPeriod[periods.size()];
		periods.toArray(retValue);
		return retValue;			 

	}


	/**
	 * 	Find all the year records in a  Calendar, 
	 *  it need not be a standard period (used in MRP)
	 *	@param C_Calendar_ID calendar
	 *	@param ctx context
	 *  @param trx trx
	 *	@return  MYear[]
	 */
	public static MYear[] getAllYearsInCalendar( int C_Calendar_ID, Ctx ctx, Trx trx) 
	{		

		List<MYear> years = new ArrayList<MYear>();
		String sql = "SELECT * FROM C_Year WHERE " +
		"IsActive='Y' AND C_Calendar_ID = ? ";  

		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, C_Calendar_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				years.add(new MYear(ctx , rs, trx));

		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally{			

			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MYear[] retValue = new MYear[years.size()];
		years.toArray(retValue);
		return retValue;

	}
}	//	MPeriod
