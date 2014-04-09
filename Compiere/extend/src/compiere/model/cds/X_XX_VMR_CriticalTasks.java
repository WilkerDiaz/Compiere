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
package compiere.model.cds;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_CriticalTasks
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_CriticalTasks extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_CriticalTasks_ID id
    @param trx transaction
    */
    public X_XX_VMR_CriticalTasks (Ctx ctx, int XX_VMR_CriticalTasks_ID, Trx trx)
    {
        super (ctx, XX_VMR_CriticalTasks_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_CriticalTasks_ID == 0)
        {
            setValue (null);
            setXX_VMR_CriticalTasks_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_CriticalTasks (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27636687064789L;
    /** Last Updated Timestamp 2012-12-03 14:39:08.0 */
    public static final long updatedMS = 1354561748000L;
    /** AD_Table_ID=1000315 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_CriticalTasks");
        
    }
    ;
    
    /** TableName=XX_VMR_CriticalTasks */
    public static final String Table_Name="XX_VMR_CriticalTasks";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_ValueNoCheck ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Associate Manager.
    @param XX_AssociateManager_ID Responsible for performing the critical task */
    public void setXX_AssociateManager_ID (int XX_AssociateManager_ID)
    {
        if (XX_AssociateManager_ID <= 0) set_Value ("XX_AssociateManager_ID", null);
        else
        set_Value ("XX_AssociateManager_ID", Integer.valueOf(XX_AssociateManager_ID));
        
    }
    
    /** Get Associate Manager.
    @return Responsible for performing the critical task */
    public int getXX_AssociateManager_ID() 
    {
        return get_ValueAsInt("XX_AssociateManager_ID");
        
    }
    
    /** Set Associate Supervisor.
    @param XX_AssociateSupervisor_ID Supervisor associated with the critical task */
    public void setXX_AssociateSupervisor_ID (int XX_AssociateSupervisor_ID)
    {
        if (XX_AssociateSupervisor_ID <= 0) set_Value ("XX_AssociateSupervisor_ID", null);
        else
        set_Value ("XX_AssociateSupervisor_ID", Integer.valueOf(XX_AssociateSupervisor_ID));
        
    }
    
    /** Get Associate Supervisor.
    @return Supervisor associated with the critical task */
    public int getXX_AssociateSupervisor_ID() 
    {
        return get_ValueAsInt("XX_AssociateSupervisor_ID");
        
    }
    
    /** Set Duration Unit.
    @param XX_DurationUnit Duration Unit */
    public void setXX_DurationUnit (String XX_DurationUnit)
    {
        set_ValueNoCheck ("XX_DurationUnit", XX_DurationUnit);
        
    }
    
    /** Get Duration Unit.
    @return Duration Unit */
    public String getXX_DurationUnit() 
    {
        return (String)get_Value("XX_DurationUnit");
        
    }
    
    /** Set Task.
    @param XX_Task Task */
    public void setXX_Task (String XX_Task)
    {
        set_Value ("XX_Task", XX_Task);
        
    }
    
    /** Get Task.
    @return Task */
    public String getXX_Task() 
    {
        return (String)get_Value("XX_Task");
        
    }
    
    /** Set Time Critical Task.
    @param XX_TasksAlert Time Critical Task */
    public void setXX_TasksAlert (int XX_TasksAlert)
    {
        set_Value ("XX_TasksAlert", Integer.valueOf(XX_TasksAlert));
        
    }
    
    /** Get Time Critical Task.
    @return Time Critical Task */
    public int getXX_TasksAlert() 
    {
        return get_ValueAsInt("XX_TasksAlert");
        
    }
    
    /** Set Critical Tasks.
    @param XX_VMR_CriticalTasks_ID Critical Tasks */
    public void setXX_VMR_CriticalTasks_ID (int XX_VMR_CriticalTasks_ID)
    {
        if (XX_VMR_CriticalTasks_ID < 1) throw new IllegalArgumentException ("XX_VMR_CriticalTasks_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_CriticalTasks_ID", Integer.valueOf(XX_VMR_CriticalTasks_ID));
        
    }
    
    /** Get Critical Tasks.
    @return Critical Tasks */
    public int getXX_VMR_CriticalTasks_ID() 
    {
        return get_ValueAsInt("XX_VMR_CriticalTasks_ID");
        
    }
    
    
}
