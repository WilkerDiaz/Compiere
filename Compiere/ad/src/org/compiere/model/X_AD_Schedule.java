/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2008 Compiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us at *
 * Compiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for AD_Schedule
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Schedule.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Schedule extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Schedule_ID id
    @param trx transaction
    */
    public X_AD_Schedule (Ctx ctx, int AD_Schedule_ID, Trx trx)
    {
        super (ctx, AD_Schedule_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Schedule_ID == 0)
        {
            setAD_Schedule_ID (0);
            setName (null);
            setOnFriday (true);	// Y
            setOnMonday (true);	// Y
            setOnSaturday (false);	// N
            setOnSunday (false);	// N
            setOnThursday (true);	// Y
            setOnTuesday (true);	// Y
            setOnWednesday (true);	// Y
            setRunOnlySpecifiedTime (false);	// N
            setScheduleType (null);	// F
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Schedule (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=916 */
    public static final int Table_ID=916;
    
    /** TableName=AD_Schedule */
    public static final String Table_Name="AD_Schedule";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Schedule.
    @param AD_Schedule_ID Execution Schedule */
    public void setAD_Schedule_ID (int AD_Schedule_ID)
    {
        if (AD_Schedule_ID < 1) throw new IllegalArgumentException ("AD_Schedule_ID is mandatory.");
        set_ValueNoCheck ("AD_Schedule_ID", Integer.valueOf(AD_Schedule_ID));
        
    }
    
    /** Get Schedule.
    @return Execution Schedule */
    public int getAD_Schedule_ID() 
    {
        return get_ValueAsInt("AD_Schedule_ID");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Frequency.
    @param Frequency Frequency of events */
    public void setFrequency (int Frequency)
    {
        set_Value ("Frequency", Integer.valueOf(Frequency));
        
    }
    
    /** Get Frequency.
    @return Frequency of events */
    public int getFrequency() 
    {
        return get_ValueAsInt("Frequency");
        
    }
    
    /** Day = D */
    public static final String FREQUENCYTYPE_Day = X_Ref__Frequency_Type.DAY.getValue();
    /** Hour = H */
    public static final String FREQUENCYTYPE_Hour = X_Ref__Frequency_Type.HOUR.getValue();
    /** Minute = M */
    public static final String FREQUENCYTYPE_Minute = X_Ref__Frequency_Type.MINUTE.getValue();
    /** Set Frequency Type.
    @param FrequencyType Frequency of event */
    public void setFrequencyType (String FrequencyType)
    {
        if (!X_Ref__Frequency_Type.isValid(FrequencyType))
        throw new IllegalArgumentException ("FrequencyType Invalid value - " + FrequencyType + " - Reference_ID=221 - D - H - M");
        set_Value ("FrequencyType", FrequencyType);
        
    }
    
    /** Get Frequency Type.
    @return Frequency of event */
    public String getFrequencyType() 
    {
        return (String)get_Value("FrequencyType");
        
    }
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set Day of the Month.
    @param MonthDay Day of the month 1 to 28/29/30/31 */
    public void setMonthDay (int MonthDay)
    {
        set_Value ("MonthDay", Integer.valueOf(MonthDay));
        
    }
    
    /** Get Day of the Month.
    @return Day of the month 1 to 28/29/30/31 */
    public int getMonthDay() 
    {
        return get_ValueAsInt("MonthDay");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Friday.
    @param OnFriday Available on Fridays */
    public void setOnFriday (boolean OnFriday)
    {
        set_Value ("OnFriday", Boolean.valueOf(OnFriday));
        
    }
    
    /** Get Friday.
    @return Available on Fridays */
    public boolean isOnFriday() 
    {
        return get_ValueAsBoolean("OnFriday");
        
    }
    
    /** Set Monday.
    @param OnMonday Available on Mondays */
    public void setOnMonday (boolean OnMonday)
    {
        set_Value ("OnMonday", Boolean.valueOf(OnMonday));
        
    }
    
    /** Get Monday.
    @return Available on Mondays */
    public boolean isOnMonday() 
    {
        return get_ValueAsBoolean("OnMonday");
        
    }
    
    /** Set Saturday.
    @param OnSaturday Available on Saturday */
    public void setOnSaturday (boolean OnSaturday)
    {
        set_Value ("OnSaturday", Boolean.valueOf(OnSaturday));
        
    }
    
    /** Get Saturday.
    @return Available on Saturday */
    public boolean isOnSaturday() 
    {
        return get_ValueAsBoolean("OnSaturday");
        
    }
    
    /** Set Sunday.
    @param OnSunday Available on Sundays */
    public void setOnSunday (boolean OnSunday)
    {
        set_Value ("OnSunday", Boolean.valueOf(OnSunday));
        
    }
    
    /** Get Sunday.
    @return Available on Sundays */
    public boolean isOnSunday() 
    {
        return get_ValueAsBoolean("OnSunday");
        
    }
    
    /** Set Thursday.
    @param OnThursday Available on Thursdays */
    public void setOnThursday (boolean OnThursday)
    {
        set_Value ("OnThursday", Boolean.valueOf(OnThursday));
        
    }
    
    /** Get Thursday.
    @return Available on Thursdays */
    public boolean isOnThursday() 
    {
        return get_ValueAsBoolean("OnThursday");
        
    }
    
    /** Set Tuesday.
    @param OnTuesday Available on Tuesdays */
    public void setOnTuesday (boolean OnTuesday)
    {
        set_Value ("OnTuesday", Boolean.valueOf(OnTuesday));
        
    }
    
    /** Get Tuesday.
    @return Available on Tuesdays */
    public boolean isOnTuesday() 
    {
        return get_ValueAsBoolean("OnTuesday");
        
    }
    
    /** Set Wednesday.
    @param OnWednesday Available on Wednesdays */
    public void setOnWednesday (boolean OnWednesday)
    {
        set_Value ("OnWednesday", Boolean.valueOf(OnWednesday));
        
    }
    
    /** Get Wednesday.
    @return Available on Wednesdays */
    public boolean isOnWednesday() 
    {
        return get_ValueAsBoolean("OnWednesday");
        
    }
    
    /** Set Only IP.
    @param RunOnlyOnIP Run only on this IP address */
    public void setRunOnlyOnIP (String RunOnlyOnIP)
    {
        set_Value ("RunOnlyOnIP", RunOnlyOnIP);
        
    }
    
    /** Get Only IP.
    @return Run only on this IP address */
    public String getRunOnlyOnIP() 
    {
        return (String)get_Value("RunOnlyOnIP");
        
    }
    
    /** Set Only Specified Time.
    @param RunOnlySpecifiedTime Run the Process only at Specified Time */
    public void setRunOnlySpecifiedTime (boolean RunOnlySpecifiedTime)
    {
        set_Value ("RunOnlySpecifiedTime", Boolean.valueOf(RunOnlySpecifiedTime));
        
    }
    
    /** Get Only Specified Time.
    @return Run the Process only at Specified Time */
    public boolean isRunOnlySpecifiedTime() 
    {
        return get_ValueAsBoolean("RunOnlySpecifiedTime");
        
    }
    
    /** Set Tolerance Minutes.
    @param RunOnlySpecifiedTolMin The tolerance in Minutes */
    public void setRunOnlySpecifiedTolMin (int RunOnlySpecifiedTolMin)
    {
        set_Value ("RunOnlySpecifiedTolMin", Integer.valueOf(RunOnlySpecifiedTolMin));
        
    }
    
    /** Get Tolerance Minutes.
    @return The tolerance in Minutes */
    public int getRunOnlySpecifiedTolMin() 
    {
        return get_ValueAsInt("RunOnlySpecifiedTolMin");
        
    }
    
    /** Set Target Hour.
    @param ScheduleHour 24 hour target start time of the process */
    public void setScheduleHour (int ScheduleHour)
    {
        set_Value ("ScheduleHour", Integer.valueOf(ScheduleHour));
        
    }
    
    /** Get Target Hour.
    @return 24 hour target start time of the process */
    public int getScheduleHour() 
    {
        return get_ValueAsInt("ScheduleHour");
        
    }
    
    /** Set Target Minute.
    @param ScheduleMinute Minute of process start time */
    public void setScheduleMinute (int ScheduleMinute)
    {
        set_Value ("ScheduleMinute", Integer.valueOf(ScheduleMinute));
        
    }
    
    /** Get Target Minute.
    @return Minute of process start time */
    public int getScheduleMinute() 
    {
        return get_ValueAsInt("ScheduleMinute");
        
    }
    
    /** Frequency = F */
    public static final String SCHEDULETYPE_Frequency = X_Ref_AD_Schedule_Type.FREQUENCY.getValue();
    /** Month Day = M */
    public static final String SCHEDULETYPE_MonthDay = X_Ref_AD_Schedule_Type.MONTH_DAY.getValue();
    /** Week Day = W */
    public static final String SCHEDULETYPE_WeekDay = X_Ref_AD_Schedule_Type.WEEK_DAY.getValue();
    /** Set Schedule Type.
    @param ScheduleType Type of schedule */
    public void setScheduleType (String ScheduleType)
    {
        if (ScheduleType == null) throw new IllegalArgumentException ("ScheduleType is mandatory");
        if (!X_Ref_AD_Schedule_Type.isValid(ScheduleType))
        throw new IllegalArgumentException ("ScheduleType Invalid value - " + ScheduleType + " - Reference_ID=318 - F - M - W");
        set_Value ("ScheduleType", ScheduleType);
        
    }
    
    /** Get Schedule Type.
    @return Type of schedule */
    public String getScheduleType() 
    {
        return (String)get_Value("ScheduleType");
        
    }
    
    /** Monday = 1 */
    public static final String WEEKDAY_Monday = X_Ref_Weekdays.MONDAY.getValue();
    /** Tuesday = 2 */
    public static final String WEEKDAY_Tuesday = X_Ref_Weekdays.TUESDAY.getValue();
    /** Wednesday = 3 */
    public static final String WEEKDAY_Wednesday = X_Ref_Weekdays.WEDNESDAY.getValue();
    /** Thursday = 4 */
    public static final String WEEKDAY_Thursday = X_Ref_Weekdays.THURSDAY.getValue();
    /** Friday = 5 */
    public static final String WEEKDAY_Friday = X_Ref_Weekdays.FRIDAY.getValue();
    /** Saturday = 6 */
    public static final String WEEKDAY_Saturday = X_Ref_Weekdays.SATURDAY.getValue();
    /** Sunday = 7 */
    public static final String WEEKDAY_Sunday = X_Ref_Weekdays.SUNDAY.getValue();
    /** Set Day of the Week.
    @param WeekDay Day of the Week */
    public void setWeekDay (String WeekDay)
    {
        if (!X_Ref_Weekdays.isValid(WeekDay))
        throw new IllegalArgumentException ("WeekDay Invalid value - " + WeekDay + " - Reference_ID=167 - 1 - 2 - 3 - 4 - 5 - 6 - 7");
        set_Value ("WeekDay", WeekDay);
        
    }
    
    /** Get Day of the Week.
    @return Day of the Week */
    public String getWeekDay() 
    {
        return (String)get_Value("WeekDay");
        
    }
    
    
}
