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
/** Generated Model for AD_Menu
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Menu.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Menu extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Menu_ID id
    @param trx transaction
    */
    public X_AD_Menu (Ctx ctx, int AD_Menu_ID, Trx trx)
    {
        super (ctx, AD_Menu_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Menu_ID == 0)
        {
            setAD_Menu_ID (0);
            setEntityType (null);	// U
            setIsReadOnly (false);	// N
            setIsSummary (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Menu (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313787789L;
    /** Last Updated Timestamp 2009-03-04 09:41:11.0 */
    public static final long updatedMS = 1236188471000L;
    /** AD_Table_ID=116 */
    public static final int Table_ID=116;
    
    /** TableName=AD_Menu */
    public static final String Table_Name="AD_Menu";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Special Form.
    @param AD_Form_ID Special Form */
    public void setAD_Form_ID (int AD_Form_ID)
    {
        if (AD_Form_ID <= 0) set_Value ("AD_Form_ID", null);
        else
        set_Value ("AD_Form_ID", Integer.valueOf(AD_Form_ID));
        
    }
    
    /** Get Special Form.
    @return Special Form */
    public int getAD_Form_ID() 
    {
        return get_ValueAsInt("AD_Form_ID");
        
    }
    
    /** Set Menu.
    @param AD_Menu_ID Identifies a Menu */
    public void setAD_Menu_ID (int AD_Menu_ID)
    {
        if (AD_Menu_ID < 1) throw new IllegalArgumentException ("AD_Menu_ID is mandatory.");
        set_ValueNoCheck ("AD_Menu_ID", Integer.valueOf(AD_Menu_ID));
        
    }
    
    /** Get Menu.
    @return Identifies a Menu */
    public int getAD_Menu_ID() 
    {
        return get_ValueAsInt("AD_Menu_ID");
        
    }
    
    /** Set Process.
    @param AD_Process_ID Process or Report */
    public void setAD_Process_ID (int AD_Process_ID)
    {
        if (AD_Process_ID <= 0) set_Value ("AD_Process_ID", null);
        else
        set_Value ("AD_Process_ID", Integer.valueOf(AD_Process_ID));
        
    }
    
    /** Get Process.
    @return Process or Report */
    public int getAD_Process_ID() 
    {
        return get_ValueAsInt("AD_Process_ID");
        
    }
    
    /** Set OS Task.
    @param AD_Task_ID Operation System Task */
    public void setAD_Task_ID (int AD_Task_ID)
    {
        if (AD_Task_ID <= 0) set_Value ("AD_Task_ID", null);
        else
        set_Value ("AD_Task_ID", Integer.valueOf(AD_Task_ID));
        
    }
    
    /** Get OS Task.
    @return Operation System Task */
    public int getAD_Task_ID() 
    {
        return get_ValueAsInt("AD_Task_ID");
        
    }
    
    /** Set Window.
    @param AD_Window_ID Data entry or display window */
    public void setAD_Window_ID (int AD_Window_ID)
    {
        if (AD_Window_ID <= 0) set_Value ("AD_Window_ID", null);
        else
        set_Value ("AD_Window_ID", Integer.valueOf(AD_Window_ID));
        
    }
    
    /** Get Window.
    @return Data entry or display window */
    public int getAD_Window_ID() 
    {
        return get_ValueAsInt("AD_Window_ID");
        
    }
    
    /** Set Workbench.
    @param AD_Workbench_ID Collection of windows, reports */
    public void setAD_Workbench_ID (int AD_Workbench_ID)
    {
        if (AD_Workbench_ID <= 0) set_Value ("AD_Workbench_ID", null);
        else
        set_Value ("AD_Workbench_ID", Integer.valueOf(AD_Workbench_ID));
        
    }
    
    /** Get Workbench.
    @return Collection of windows, reports */
    public int getAD_Workbench_ID() 
    {
        return get_ValueAsInt("AD_Workbench_ID");
        
    }
    
    /** Set Workflow.
    @param AD_Workflow_ID Workflow or combination of tasks */
    public void setAD_Workflow_ID (int AD_Workflow_ID)
    {
        if (AD_Workflow_ID <= 0) set_Value ("AD_Workflow_ID", null);
        else
        set_Value ("AD_Workflow_ID", Integer.valueOf(AD_Workflow_ID));
        
    }
    
    /** Get Workflow.
    @return Workflow or combination of tasks */
    public int getAD_Workflow_ID() 
    {
        return get_ValueAsInt("AD_Workflow_ID");
        
    }
    
    /** Workbench = B */
    public static final String ACTION_Workbench = X_Ref_AD_Menu_Action.WORKBENCH.getValue();
    /** WorkFlow = F */
    public static final String ACTION_WorkFlow = X_Ref_AD_Menu_Action.WORK_FLOW.getValue();
    /** Process = P */
    public static final String ACTION_Process = X_Ref_AD_Menu_Action.PROCESS.getValue();
    /** Report = R */
    public static final String ACTION_Report = X_Ref_AD_Menu_Action.REPORT.getValue();
    /** Task = T */
    public static final String ACTION_Task = X_Ref_AD_Menu_Action.TASK.getValue();
    /** Window = W */
    public static final String ACTION_Window = X_Ref_AD_Menu_Action.WINDOW.getValue();
    /** Form = X */
    public static final String ACTION_Form = X_Ref_AD_Menu_Action.FORM.getValue();
    /** Set Action.
    @param Action Indicates the Action to be performed */
    public void setAction (String Action)
    {
        if (!X_Ref_AD_Menu_Action.isValid(Action))
        throw new IllegalArgumentException ("Action Invalid value - " + Action + " - Reference_ID=104 - B - F - P - R - T - W - X");
        set_Value ("Action", Action);
        
    }
    
    /** Get Action.
    @return Indicates the Action to be performed */
    public String getAction() 
    {
        return (String)get_Value("Action");
        
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
    
    /** Set Entity Type.
    @param EntityType Dictionary Entity Type;
     Determines ownership and synchronization */
    public void setEntityType (String EntityType)
    {
        set_Value ("EntityType", EntityType);
        
    }
    
    /** Get Entity Type.
    @return Dictionary Entity Type;
     Determines ownership and synchronization */
    public String getEntityType() 
    {
        return (String)get_Value("EntityType");
        
    }
    
    /** Set Read Only.
    @param IsReadOnly Field is read only */
    public void setIsReadOnly (boolean IsReadOnly)
    {
        set_Value ("IsReadOnly", Boolean.valueOf(IsReadOnly));
        
    }
    
    /** Get Read Only.
    @return Field is read only */
    public boolean isReadOnly() 
    {
        return get_ValueAsBoolean("IsReadOnly");
        
    }
    
    /** Set Summary Level.
    @param IsSummary This is a summary entity */
    public void setIsSummary (boolean IsSummary)
    {
        set_Value ("IsSummary", Boolean.valueOf(IsSummary));
        
    }
    
    /** Get Summary Level.
    @return This is a summary entity */
    public boolean isSummary() 
    {
        return get_ValueAsBoolean("IsSummary");
        
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
    
    
}
