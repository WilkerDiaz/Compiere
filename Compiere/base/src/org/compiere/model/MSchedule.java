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

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.util.CCache;
import org.compiere.util.CLogMgt;
import org.compiere.util.Ctx;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;


/**
 *	Schedule Model
 *
 *  @author Jorg Janke
 *  @version $Id$
 */
public class MSchedule extends X_AD_Schedule
{
    /** Logger for class MSchedule */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MSchedule.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Schedule from Cache
	 *	@param ctx context
	 *	@param AD_Schedule_ID id
	 *	@return MSchedule
	 */
	public static MSchedule get(Ctx ctx, int AD_Schedule_ID)
	{
		Integer key = Integer.valueOf (AD_Schedule_ID);
		MSchedule retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MSchedule (ctx, AD_Schedule_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer, MSchedule> s_cache
		= new CCache<Integer, MSchedule> ("AD_Schedule", 20);


	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_Schedule_ID id
	 *	@param trx p_trx
	 */
	public MSchedule(Ctx ctx, int AD_Schedule_ID, Trx trx)
	{
		super (ctx, AD_Schedule_ID, trx);
		if (AD_Schedule_ID == 0)
		{
		//	setName (null);
			setScheduleType (SCHEDULETYPE_Frequency);
			setFrequencyType (FREQUENCYTYPE_Day);
			setFrequency (1);
			//
			setOnMonday (true);	// Y
			setOnTuesday (true);	// Y
			setOnWednesday (true);	// Y
			setOnThursday (true);	// Y
			setOnFriday (true);	// Y
			setOnSaturday (false);	// N
			setOnSunday (false);	// N
			//
			setRunOnlySpecifiedTime (false);	// N
		}	}	//	MSchedule

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MSchedule(Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MSchedule


	/**
	 * 	Get Month Day
	 *	@return 1 .. 31
	 */
	@Override
	public int getMonthDay()
	{
		int day = super.getMonthDay();
		if (day < 1)
			day = 1;
		else if (day > 31)
			day = 31;
		return day;
	}	//	getMonthDay

	/**
	 * 	Get WeekDay
	 *	@return WeekDay
	 */
	@Override
	public String getWeekDay()
	{
		String wd = super.getWeekDay();
		if (wd == null || !X_Ref_Weekdays.isValid(wd))
			wd = WEEKDAY_Monday;
		return wd;
	}	//	getWeekDay


	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		if (getScheduleType() == null)
			setScheduleType (SCHEDULETYPE_Frequency);

		//	Set Schedule Type & Frequencies
		if (SCHEDULETYPE_Frequency.equals(getScheduleType()))
		{
			if (getFrequencyType() == null)
				setFrequencyType(FREQUENCYTYPE_Day);
			if (getFrequency() < 1)
				setFrequency(1);
			if (!isOnMonday() && !isOnTuesday() && !isOnWednesday() && !isOnThursday() &&
					!isOnFriday() && !isOnSaturday() && !isOnSunday() )
				setOnMonday(true);
		}
		else if (SCHEDULETYPE_MonthDay.equals(getScheduleType()))
		{
			if ((super.getMonthDay() < 1) || (super.getMonthDay() > 31))
				setMonthDay(1);
		}
		else	//	SCHEDULETYPE_WeekDay
		{
			if (getScheduleType() == null)
				setScheduleType(SCHEDULETYPE_WeekDay);
			if (super.getWeekDay() == null)
				setWeekDay(WEEKDAY_Monday);
		}
		//	Hour/Minute
		if ((getScheduleHour() > 23) || (getScheduleHour() < 0))
			setScheduleHour(0);
		if ((getScheduleMinute() > 59) || (getScheduleMinute() < 0))
			setScheduleMinute(0);
		return true;
	}	//	beforeSave


	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MSchedule[");
		sb.append (get_ID()).append ("-").append (getName());
		String scheduleType = getScheduleType();
		sb.append (",Type=").append(scheduleType);
		if (SCHEDULETYPE_Frequency.equals(scheduleType))
		{
			sb.append (",Frequency=").append(getFrequencyType())
				.append("*").append(getFrequency());
			if (isOnMonday())
				sb.append(",Mo");
			if (isOnTuesday())
				sb.append(",Tu");
			if (isOnWednesday())
				sb.append(",We");
			if (isOnThursday())
				sb.append(",Th");
			if (isOnFriday())
				sb.append(",Fr");
			if (isOnSaturday())
				sb.append(",Sa");
			if (isOnSunday())
				sb.append(",Su");
		}
		else if (SCHEDULETYPE_MonthDay.equals(scheduleType))
			sb.append(",Day=").append(getMonthDay());
		else
			sb.append(",Day=").append(getWeekDay());
		//
		sb.append (",HH=").append(getScheduleHour())
			.append (",MM=").append(getScheduleMinute());
		//
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Is it OK to Run process On IP of this box
	 *	@return
	 */
	public boolean isOKtoRunOnIP()
	{
		String ipOnly = getRunOnlyOnIP();
		if ((ipOnly == null) || (ipOnly.length() == 0))
			return true;

		StringTokenizer st = new StringTokenizer(ipOnly, ";");
		while (st.hasMoreElements())
		{
			String ip = st.nextToken();
			if (checkIP(ip))
				return true;
		}
		return false;
	}	//	isOKtoRunOnIP

	/**
	 * 	check whether this IP is allowed to process
	 *	@param ipOnly
	 *	@return true if IP is correct
	 */
	private boolean checkIP(String ipOnly)
	{
		try
		{
			InetAddress box = InetAddress.getLocalHost();
			String ip = box.getHostAddress();
			if (ipOnly.indexOf(ip) == -1)
			{
				// TODO: We need to handle this better, for the moment reduced to fine.
				log.fine ("Not allowed here - IP=" + ip + " does not match " + ipOnly);
				return false;
			}
			log.fine ("Allowed here - IP=" + ip + " matches " + ipOnly);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			return false;
		}
		return true;
	}	// checkIP


	/*
	 * 	GMartinelli/JPires, polimorfismo para manejo de parámetro que indica si
	 * se trata de la primera ejecucion del jBoss
	 */
	public long getNextRunMS (long last)
	{
		return getNextRunMS (last, false);
	}
	
	/**
	 * 	Get Next Run
	 *	@param last in MS
	 *	@return next run in MS
	 */
	public long getNextRunMS (long last, boolean firstRun)
	{
		Calendar calNow = Calendar.getInstance();
		calNow.setTimeInMillis (last);
		//
		Calendar calNext = Calendar.getInstance();
		calNext.setTimeInMillis (last);
		calNext.set (Calendar.SECOND, 0);
		calNext.set (Calendar.MILLISECOND, 0);
		//
		int hour = getScheduleHour();
		int minute = getScheduleMinute();
		//
		String scheduleType = getScheduleType();
		if (SCHEDULETYPE_Frequency.equals(scheduleType))
		{
			String frequencyType = getFrequencyType();
			int frequency = getFrequency();

			ArrayList<Integer> validDays = new ArrayList<Integer>();
			if(isOnMonday())
				validDays.add(new Integer(Calendar.MONDAY));
			if(isOnTuesday())
				validDays.add(new Integer(Calendar.TUESDAY));
			if(isOnWednesday())
				validDays.add(new Integer(Calendar.WEDNESDAY));
			if(isOnThursday())
				validDays.add(new Integer(Calendar.THURSDAY));
			if(isOnFriday())
				validDays.add(new Integer(Calendar.FRIDAY));
			if(isOnSaturday())
				validDays.add(new Integer(Calendar.SATURDAY));
			if(isOnSunday())
				validDays.add(new Integer(Calendar.SUNDAY));

			if(validDays.isEmpty())
			{
				log.warning("Incorrect Schedule setup. Please enable at least one of the weekdays");
				validDays.add(new Integer(Calendar.MONDAY));
			}

			boolean increment=true;
			int ct = 0;
			while ((ct < 8)
				&& !(validDays.contains(new Integer(calNext.get(Calendar.DAY_OF_WEEK)))))
			{
				calNext.add(Calendar.DAY_OF_YEAR, 1);
				ct++;
				increment=false;
			}


			/*****	DAY		******/
			if (X_R_RequestProcessor.FREQUENCYTYPE_Day.equals(frequencyType))
			{
				calNext.set (Calendar.HOUR_OF_DAY, hour);
				calNext.set (Calendar.MINUTE, minute);
				// JPires/GMartinelli: BugFixed, planificacion de tareas frecuencia diaria
				// para planificar en el dia en curso si la hora de ejecucion es mayor a 
				// la hora actual
				if((increment && !firstRun) || (calNext.before(calNow)))
				{
					calNext.add(Calendar.DAY_OF_YEAR, frequency);
				}
			}	//	Day

			/*****	HOUR	******/
			else if (X_R_RequestProcessor.FREQUENCYTYPE_Hour.equals(frequencyType))
			{
				calNext.set (Calendar.MINUTE, minute);
				if(increment)
				{
					calNext.add (Calendar.HOUR_OF_DAY, frequency);
				}

			}	//	Hour

			/*****	MINUTE	******/
			else if (X_R_RequestProcessor.FREQUENCYTYPE_Minute.equals(frequencyType))
			{
				if(increment)
				{
					calNext.add(Calendar.MINUTE, frequency);
				}
			}	//	Minute

		}

		/*****	MONTH	******/
		else if (SCHEDULETYPE_MonthDay.equals(scheduleType))
		{
			calNext.set (Calendar.HOUR, hour);
			calNext.set (Calendar.MINUTE, minute);
			//
			int day = getMonthDay();
			int dd = calNext.get(Calendar.DAY_OF_MONTH);
			int max = calNext.getActualMaximum (Calendar.DAY_OF_MONTH);
			int dayUsed = Math.min (day, max);
			//	Same Month
			if (dd < dayUsed)
				calNext.set (Calendar.DAY_OF_MONTH, dayUsed);
			else
			{
				if (calNext.get (Calendar.MONTH) == Calendar.DECEMBER)
				{
					calNext.add (Calendar.YEAR, 1);
					calNext.set (Calendar.MONTH, Calendar.JANUARY);
				}
				else
					calNext.add (Calendar.MONTH, 1);
				max = calNext.getActualMaximum (Calendar.DAY_OF_MONTH);
				dayUsed = Math.min (day, max);
				calNext.set (Calendar.DAY_OF_MONTH, dayUsed);
			}
		}	//	month

		/*****	WEEK	******/
		else // if (SCHEDULETYPE_WeekDay.equals(scheduleType))
		{
			String weekDay = getWeekDay();
			int dayOfWeek = 0;
			if (WEEKDAY_Monday.equals(weekDay))
				dayOfWeek = Calendar.MONDAY;
			else if (WEEKDAY_Tuesday.equals(weekDay))
				dayOfWeek = Calendar.TUESDAY;
			else if (WEEKDAY_Wednesday.equals(weekDay))
				dayOfWeek = Calendar.WEDNESDAY;
			else if (WEEKDAY_Thursday.equals(weekDay))
				dayOfWeek = Calendar.THURSDAY;
			else if (WEEKDAY_Friday.equals(weekDay))
				dayOfWeek = Calendar.FRIDAY;
			else if (WEEKDAY_Saturday.equals(weekDay))
				dayOfWeek = Calendar.SATURDAY;
			else if (WEEKDAY_Sunday.equals(weekDay))
				dayOfWeek = Calendar.SUNDAY;
			calNext.set (Calendar.DAY_OF_WEEK, dayOfWeek);
			calNext.set (Calendar.HOUR, hour);
			calNext.set (Calendar.MINUTE, minute);
			calNext.set (Calendar.SECOND, 0);
			calNext.set (Calendar.MILLISECOND, 0);
			//
			if (!calNext.after(calNow))
			{
				calNext.add (Calendar.WEEK_OF_YEAR, 1);
			}
		}	//	week

		long delta = calNext.getTimeInMillis() - calNow.getTimeInMillis();
		String info = "Now=" + calNow.getTime().toString()
			+ ", Next=" + calNext.getTime().toString()
			+ ", Delta=" + delta
			+ ", " + toString();
		if (delta < 0)
		{
			log.warning(info);
		}
		else
			log.info (info);

		return calNext.getTimeInMillis();
	}	//	getNextRunMS

	/**
	 * 	Get Next
	 *	@param start start time
	 *	@param iterations no iterations
	 *	@return array of next
	 */
	public Timestamp[] getNext (Timestamp start, int iterations)
	{
		Timestamp[]nexts = new Timestamp[iterations];
		long startMS = start.getTime();
		for (int i = 0; i < nexts.length; i++)
		{
			Timestamp next = new Timestamp (getNextRunMS (startMS));
			startMS = next.getTime();
			nexts[i] = next;
		}
		return nexts;
	}	//	getNext


	/**************************************************************************
	 * 	Test
	 *	@param args
	 */
	public static void main(String[] args)
	{
		Compiere.startup (true);
		CLogMgt.setLevel (Level.FINE);
		MSchedule s = null;
		Timestamp start = TimeUtil.getDay (2007, 12, 01);

		/**	Test Case - Day		**/
		s = new MSchedule(Env.getCtx (), 0, null);
		log.info ("*** Day 2 ***");
		s.setScheduleType (SCHEDULETYPE_Frequency);
		s.setFrequencyType (FREQUENCYTYPE_Day);
		s.setFrequency (2);
	//	start = new Timestamp(System.currentTimeMillis());
		s.getNext (start, 10);

		/**	Test Case - Hour 	**/
		s = new MSchedule(Env.getCtx (), 0, null);
		log.info ("*** Hour 5 ***");
		s.setScheduleType (SCHEDULETYPE_Frequency);
		s.setFrequencyType (FREQUENCYTYPE_Hour);
		s.setFrequency (5);
	//	start = new Timestamp(System.currentTimeMillis());
		s.getNext (start, 10);

		/**	Test Case - Minute	**/
		s = new MSchedule(Env.getCtx (), 0, null);
		log.info ("*** Minute 15 ***");
		s.setScheduleType (SCHEDULETYPE_Frequency);
		s.setFrequencyType (FREQUENCYTYPE_Minute);
		s.setFrequency (15);
	//	start = new Timestamp(System.currentTimeMillis());
		s.getNext (start, 10);

		/**	Test Case - WeekDay	**/
		s = new MSchedule(Env.getCtx (), 0, null);
		log.info ("*** WeekDay Mo ***");
		s.setScheduleType (SCHEDULETYPE_WeekDay);
		s.setWeekDay (WEEKDAY_Monday);
	//	start = new Timestamp(System.currentTimeMillis());
		s.getNext (start, 92);

		/**	Test Case - Month	**/
		s = new MSchedule(Env.getCtx (), 0, null);
		log.info ("*** MonthDay 31 ***");
		s.setScheduleType (SCHEDULETYPE_MonthDay);
		s.setMonthDay (31);
		//	start = new Timestamp(System.currentTimeMillis());
		s.getNext (start, 14);
		/** **/

	}	//	main

}	//	MSchedule
