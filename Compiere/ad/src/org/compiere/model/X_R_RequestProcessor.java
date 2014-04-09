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
/** Generated Model for R_RequestProcessor
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_R_RequestProcessor.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_R_RequestProcessor extends PO
{
    /** Standard Constructor
    @param ctx context
    @param R_RequestProcessor_ID id
    @param trx transaction
    */
    public X_R_RequestProcessor (Ctx ctx, int R_RequestProcessor_ID, Trx trx)
    {
        super (ctx, R_RequestProcessor_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (R_RequestProcessor_ID == 0)
        {
            setInactivityAlertDays (0);	// 0
            setKeepLogDays (0);	// 7
            setName (null);
            setOverdueAlertDays (0);	// 0
            setOverdueAssignDays (0);	// 0
            setR_RequestProcessor_ID (0);
            setRemindDays (0);	// 0
            setSupervisor_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_R_RequestProcessor (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=420 */
    public static final int Table_ID=420;
    
    /** TableName=R_RequestProcessor */
    public static final String Table_Name="R_RequestProcessor";
    
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
        if (AD_Schedule_ID <= 0) set_Value ("AD_Schedule_ID", null);
        else
        set_Value ("AD_Schedule_ID", Integer.valueOf(AD_Schedule_ID));
        
    }
    
    /** Get Schedule.
    @return Execution Schedule */
    public int getAD_Schedule_ID() 
    {
        return get_ValueAsInt("AD_Schedule_ID");
        
    }
    
    /** Set Date Last Run.
    @param DateLastRun Date the process was last run. */
    public void setDateLastRun (Timestamp DateLastRun)
    {
        set_Value ("DateLastRun", DateLastRun);
        
    }
    
    /** Get Date Last Run.
    @return Date the process was last run. */
    public Timestamp getDateLastRun() 
    {
        return (Timestamp)get_Value("DateLastRun");
        
    }
    
    /** Set Date Next Run.
    @param DateNextRun Date the process will run next */
    public void setDateNextRun (Timestamp DateNextRun)
    {
        set_Value ("DateNextRun", DateNextRun);
        
    }
    
    /** Get Date Next Run.
    @return Date the process will run next */
    public Timestamp getDateNextRun() 
    {
        return (Timestamp)get_Value("DateNextRun");
        
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
    
    /** Set Inactivity Alert Days.
    @param InactivityAlertDays Send Alert when there is no activity after days (0= no alert) */
    public void setInactivityAlertDays (int InactivityAlertDays)
    {
        set_Value ("InactivityAlertDays", Integer.valueOf(InactivityAlertDays));
        
    }
    
    /** Get Inactivity Alert Days.
    @return Send Alert when there is no activity after days (0= no alert) */
    public int getInactivityAlertDays() 
    {
        return get_ValueAsInt("InactivityAlertDays");
        
    }
    
    /** Set Days to keep Log.
    @param KeepLogDays Number of days to keep the log entries */
    public void setKeepLogDays (int KeepLogDays)
    {
        set_Value ("KeepLogDays", Integer.valueOf(KeepLogDays));
        
    }
    
    /** Get Days to keep Log.
    @return Number of days to keep the log entries */
    public int getKeepLogDays() 
    {
        return get_ValueAsInt("KeepLogDays");
        
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
    
    /** Set Alert after Days Due.
    @param OverdueAlertDays Send email alert after number of days due (0=no alerts) */
    public void setOverdueAlertDays (int OverdueAlertDays)
    {
        set_Value ("OverdueAlertDays", Integer.valueOf(OverdueAlertDays));
        
    }
    
    /** Get Alert after Days Due.
    @return Send email alert after number of days due (0=no alerts) */
    public int getOverdueAlertDays() 
    {
        return get_ValueAsInt("OverdueAlertDays");
        
    }
    
    /** Set Escalate after Days Due.
    @param OverdueAssignDays Escalation to superior after number of due days (0 = no) */
    public void setOverdueAssignDays (int OverdueAssignDays)
    {
        set_Value ("OverdueAssignDays", Integer.valueOf(OverdueAssignDays));
        
    }
    
    /** Get Escalate after Days Due.
    @return Escalation to superior after number of due days (0 = no) */
    public int getOverdueAssignDays() 
    {
        return get_ValueAsInt("OverdueAssignDays");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Request Processor.
    @param R_RequestProcessor_ID Processor for Requests */
    public void setR_RequestProcessor_ID (int R_RequestProcessor_ID)
    {
        if (R_RequestProcessor_ID < 1) throw new IllegalArgumentException ("R_RequestProcessor_ID is mandatory.");
        set_ValueNoCheck ("R_RequestProcessor_ID", Integer.valueOf(R_RequestProcessor_ID));
        
    }
    
    /** Get Request Processor.
    @return Processor for Requests */
    public int getR_RequestProcessor_ID() 
    {
        return get_ValueAsInt("R_RequestProcessor_ID");
        
    }
    
    /** Set Request Type.
    @param R_RequestType_ID Type of request (e.g. Inquiry, Complaint...) */
    public void setR_RequestType_ID (int R_RequestType_ID)
    {
        if (R_RequestType_ID <= 0) set_Value ("R_RequestType_ID", null);
        else
        set_Value ("R_RequestType_ID", Integer.valueOf(R_RequestType_ID));
        
    }
    
    /** Get Request Type.
    @return Type of request (e.g. Inquiry, Complaint...) */
    public int getR_RequestType_ID() 
    {
        return get_ValueAsInt("R_RequestType_ID");
        
    }
    
    /** Set Reminder Days.
    @param RemindDays Days between sending Reminder Emails for a due or inactive Document */
    public void setRemindDays (int RemindDays)
    {
        set_Value ("RemindDays", Integer.valueOf(RemindDays));
        
    }
    
    /** Get Reminder Days.
    @return Days between sending Reminder Emails for a due or inactive Document */
    public int getRemindDays() 
    {
        return get_ValueAsInt("RemindDays");
        
    }
    
    /** Set Supervisor.
    @param Supervisor_ID Supervisor for this user/organization - used for escalation and approval */
    public void setSupervisor_ID (int Supervisor_ID)
    {
        if (Supervisor_ID < 1) throw new IllegalArgumentException ("Supervisor_ID is mandatory.");
        set_Value ("Supervisor_ID", Integer.valueOf(Supervisor_ID));
        
    }
    
    /** Get Supervisor.
    @return Supervisor for this user/organization - used for escalation and approval */
    public int getSupervisor_ID() 
    {
        return get_ValueAsInt("Supervisor_ID");
        
    }
    
    
}
