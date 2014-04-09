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
/** Generated Model for AD_WF_EventAudit
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_WF_EventAudit.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_WF_EventAudit extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_WF_EventAudit_ID id
    @param trx transaction
    */
    public X_AD_WF_EventAudit (Ctx ctx, int AD_WF_EventAudit_ID, Trx trx)
    {
        super (ctx, AD_WF_EventAudit_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_WF_EventAudit_ID == 0)
        {
            setAD_Table_ID (0);
            setAD_WF_EventAudit_ID (0);
            setAD_WF_Node_ID (0);
            setAD_WF_Process_ID (0);
            setAD_WF_Responsible_ID (0);
            setElapsedTimeMS (Env.ZERO);
            setEventType (null);
            setRecord_ID (0);
            setWFState (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_WF_EventAudit (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=649 */
    public static final int Table_ID=649;
    
    /** TableName=AD_WF_EventAudit */
    public static final String Table_Name="AD_WF_EventAudit";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Workflow Event Audit.
    @param AD_WF_EventAudit_ID Workflow Process Activity Event Audit Information */
    public void setAD_WF_EventAudit_ID (int AD_WF_EventAudit_ID)
    {
        if (AD_WF_EventAudit_ID < 1) throw new IllegalArgumentException ("AD_WF_EventAudit_ID is mandatory.");
        set_ValueNoCheck ("AD_WF_EventAudit_ID", Integer.valueOf(AD_WF_EventAudit_ID));
        
    }
    
    /** Get Workflow Event Audit.
    @return Workflow Process Activity Event Audit Information */
    public int getAD_WF_EventAudit_ID() 
    {
        return get_ValueAsInt("AD_WF_EventAudit_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_WF_EventAudit_ID()));
        
    }
    
    /** Set Node.
    @param AD_WF_Node_ID Workflow Node (activity), step or process */
    public void setAD_WF_Node_ID (int AD_WF_Node_ID)
    {
        if (AD_WF_Node_ID < 1) throw new IllegalArgumentException ("AD_WF_Node_ID is mandatory.");
        set_Value ("AD_WF_Node_ID", Integer.valueOf(AD_WF_Node_ID));
        
    }
    
    /** Get Node.
    @return Workflow Node (activity), step or process */
    public int getAD_WF_Node_ID() 
    {
        return get_ValueAsInt("AD_WF_Node_ID");
        
    }
    
    /** Set Workflow Process.
    @param AD_WF_Process_ID Actual Workflow Process Instance */
    public void setAD_WF_Process_ID (int AD_WF_Process_ID)
    {
        if (AD_WF_Process_ID < 1) throw new IllegalArgumentException ("AD_WF_Process_ID is mandatory.");
        set_Value ("AD_WF_Process_ID", Integer.valueOf(AD_WF_Process_ID));
        
    }
    
    /** Get Workflow Process.
    @return Actual Workflow Process Instance */
    public int getAD_WF_Process_ID() 
    {
        return get_ValueAsInt("AD_WF_Process_ID");
        
    }
    
    /** Set Workflow Owner.
    @param AD_WF_Responsible_ID Responsible for Workflow Execution */
    public void setAD_WF_Responsible_ID (int AD_WF_Responsible_ID)
    {
        if (AD_WF_Responsible_ID < 1) throw new IllegalArgumentException ("AD_WF_Responsible_ID is mandatory.");
        set_Value ("AD_WF_Responsible_ID", Integer.valueOf(AD_WF_Responsible_ID));
        
    }
    
    /** Get Workflow Owner.
    @return Responsible for Workflow Execution */
    public int getAD_WF_Responsible_ID() 
    {
        return get_ValueAsInt("AD_WF_Responsible_ID");
        
    }
    
    /** Set Attribute Name.
    @param AttributeName Name of the Attribute */
    public void setAttributeName (String AttributeName)
    {
        set_Value ("AttributeName", AttributeName);
        
    }
    
    /** Get Attribute Name.
    @return Name of the Attribute */
    public String getAttributeName() 
    {
        return (String)get_Value("AttributeName");
        
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
    
    /** Set Elapsed Time ms.
    @param ElapsedTimeMS Elapsed Time in milliseconds */
    public void setElapsedTimeMS (java.math.BigDecimal ElapsedTimeMS)
    {
        if (ElapsedTimeMS == null) throw new IllegalArgumentException ("ElapsedTimeMS is mandatory.");
        set_Value ("ElapsedTimeMS", ElapsedTimeMS);
        
    }
    
    /** Get Elapsed Time ms.
    @return Elapsed Time in milliseconds */
    public java.math.BigDecimal getElapsedTimeMS() 
    {
        return get_ValueAsBigDecimal("ElapsedTimeMS");
        
    }
    
    /** Process Created = PC */
    public static final String EVENTTYPE_ProcessCreated = X_Ref_WF_EventType.PROCESS_CREATED.getValue();
    /** Process Completed = PX */
    public static final String EVENTTYPE_ProcessCompleted = X_Ref_WF_EventType.PROCESS_COMPLETED.getValue();
    /** State Changed = SC */
    public static final String EVENTTYPE_StateChanged = X_Ref_WF_EventType.STATE_CHANGED.getValue();
    /** Set Event Type.
    @param EventType Type of Event */
    public void setEventType (String EventType)
    {
        if (EventType == null) throw new IllegalArgumentException ("EventType is mandatory");
        if (!X_Ref_WF_EventType.isValid(EventType))
        throw new IllegalArgumentException ("EventType Invalid value - " + EventType + " - Reference_ID=306 - PC - PX - SC");
        set_Value ("EventType", EventType);
        
    }
    
    /** Get Event Type.
    @return Type of Event */
    public String getEventType() 
    {
        return (String)get_Value("EventType");
        
    }
    
    /** Set New Value.
    @param NewValue New field value */
    public void setNewValue (String NewValue)
    {
        set_Value ("NewValue", NewValue);
        
    }
    
    /** Get New Value.
    @return New field value */
    public String getNewValue() 
    {
        return (String)get_Value("NewValue");
        
    }
    
    /** Set Old Value.
    @param OldValue The old file data */
    public void setOldValue (String OldValue)
    {
        set_Value ("OldValue", OldValue);
        
    }
    
    /** Get Old Value.
    @return The old file data */
    public String getOldValue() 
    {
        return (String)get_Value("OldValue");
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID < 0) throw new IllegalArgumentException ("Record_ID is mandatory.");
        set_Value ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set Text Message.
    @param TextMsg Text Message */
    public void setTextMsg (String TextMsg)
    {
        set_Value ("TextMsg", TextMsg);
        
    }
    
    /** Get Text Message.
    @return Text Message */
    public String getTextMsg() 
    {
        return (String)get_Value("TextMsg");
        
    }
    
    /** Aborted = CA */
    public static final String WFSTATE_Aborted = X_Ref_WF_Instance_State.ABORTED.getValue();
    /** Completed = CC */
    public static final String WFSTATE_Completed = X_Ref_WF_Instance_State.COMPLETED.getValue();
    /** Terminated = CT */
    public static final String WFSTATE_Terminated = X_Ref_WF_Instance_State.TERMINATED.getValue();
    /** Not Started = ON */
    public static final String WFSTATE_NotStarted = X_Ref_WF_Instance_State.NOT_STARTED.getValue();
    /** Running = OR */
    public static final String WFSTATE_Running = X_Ref_WF_Instance_State.RUNNING.getValue();
    /** Suspended = OS */
    public static final String WFSTATE_Suspended = X_Ref_WF_Instance_State.SUSPENDED.getValue();
    /** Set Workflow State.
    @param WFState State of the execution of the workflow */
    public void setWFState (String WFState)
    {
        if (WFState == null) throw new IllegalArgumentException ("WFState is mandatory");
        if (!X_Ref_WF_Instance_State.isValid(WFState))
        throw new IllegalArgumentException ("WFState Invalid value - " + WFState + " - Reference_ID=305 - CA - CC - CT - ON - OR - OS");
        set_Value ("WFState", WFState);
        
    }
    
    /** Get Workflow State.
    @return State of the execution of the workflow */
    public String getWFState() 
    {
        return (String)get_Value("WFState");
        
    }
    
    
}
