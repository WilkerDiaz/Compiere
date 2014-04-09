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
/** Generated Model for XX_VMR_CriticalTaskForClose
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_CriticalTaskForClose extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_CriticalTaskForClose_ID id
    @param trx transaction
    */
    public X_XX_VMR_CriticalTaskForClose (Ctx ctx, int XX_VMR_CriticalTaskForClose_ID, Trx trx)
    {
        super (ctx, XX_VMR_CriticalTaskForClose_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_CriticalTaskForClose_ID == 0)
        {
            setXX_VMR_CriticalTaskForClose_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_CriticalTaskForClose (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27574383767789L;
    /** Last Updated Timestamp 2010-12-13 12:10:51.0 */
    public static final long updatedMS = 1292258451000L;
    /** AD_Table_ID=1000316 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_CriticalTaskForClose");
        
    }
    ;
    
    /** TableName=XX_VMR_CriticalTaskForClose */
    public static final String Table_Name="XX_VMR_CriticalTaskForClose";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set ID del registro.
    @param XX_ActualRecord_ID ID del registro */
    public void setXX_ActualRecord_ID (int XX_ActualRecord_ID)
    {
        if (XX_ActualRecord_ID <= 0) set_Value ("XX_ActualRecord_ID", null);
        else
        set_Value ("XX_ActualRecord_ID", Integer.valueOf(XX_ActualRecord_ID));
        
    }
    
    /** Get ID del registro.
    @return ID del registro */
    public int getXX_ActualRecord_ID() 
    {
        return get_ValueAsInt("XX_ActualRecord_ID");
        
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
    
    /** Set Completed On.
    @param XX_CompletedOn Completed On */
    public void setXX_CompletedOn (Timestamp XX_CompletedOn)
    {
        throw new IllegalArgumentException ("XX_CompletedOn is virtual column");
        
    }
    
    /** Get Completed On.
    @return Completed On */
    public Timestamp getXX_CompletedOn() 
    {
        return (Timestamp)get_Value("XX_CompletedOn");
        
    }
    
    /** Set Created On.
    @param XX_CreatedOn Created On */
    public void setXX_CreatedOn (Timestamp XX_CreatedOn)
    {
        throw new IllegalArgumentException ("XX_CreatedOn is virtual column");
        
    }
    
    /** Get Created On.
    @return Created On */
    public Timestamp getXX_CreatedOn() 
    {
        return (Timestamp)get_Value("XX_CreatedOn");
        
    }
    
    /** Set Department Number.
    @param XX_DepartmentNumber Department Number */
    public void setXX_DepartmentNumber (String XX_DepartmentNumber)
    {
        set_Value ("XX_DepartmentNumber", XX_DepartmentNumber);
        
    }
    
    /** Get Department Number.
    @return Department Number */
    public String getXX_DepartmentNumber() 
    {
        return (String)get_Value("XX_DepartmentNumber");
        
    }
    
    /** Set Duration Unit.
    @param XX_DurationUnit Duration Unit */
    public void setXX_DurationUnit (String XX_DurationUnit)
    {
        throw new IllegalArgumentException ("XX_DurationUnit is virtual column");
        
    }
    
    /** Get Duration Unit.
    @return Duration Unit */
    public String getXX_DurationUnit() 
    {
        return (String)get_Value("XX_DurationUnit");
        
    }
    
    /** Set Associate Manager.
    @param XX_ManagerRole_ID Associate Manager */
    public void setXX_ManagerRole_ID (int XX_ManagerRole_ID)
    {
        if (XX_ManagerRole_ID <= 0) set_Value ("XX_ManagerRole_ID", null);
        else
        set_Value ("XX_ManagerRole_ID", Integer.valueOf(XX_ManagerRole_ID));
        
    }
    
    /** Get Associate Manager.
    @return Associate Manager */
    public int getXX_ManagerRole_ID() 
    {
        return get_ValueAsInt("XX_ManagerRole_ID");
        
    }
    
    /** Set Ejecutar.
    @param XX_PWFValProd Ejecutar */
    public void setXX_PWFValProd (String XX_PWFValProd)
    {
        set_Value ("XX_PWFValProd", XX_PWFValProd);
        
    }
    
    /** Get Ejecutar.
    @return Ejecutar */
    public String getXX_PWFValProd() 
    {
        return (String)get_Value("XX_PWFValProd");
        
    }
    
    /** Abandonated = Aba */
    public static final String XX_STATUSCRITICALTASK_Abandonated = X_Ref_XX_Ref_StatusCriticalTask.ABANDONATED.getValue();
    /** Por Realizar = Act */
    public static final String XX_STATUSCRITICALTASK_PorRealizar = X_Ref_XX_Ref_StatusCriticalTask.POR_REALIZAR.getValue();
    /** Completado = Com */
    public static final String XX_STATUSCRITICALTASK_Completado = X_Ref_XX_Ref_StatusCriticalTask.COMPLETADO.getValue();
    /** Set Status.
    @param XX_StatusCriticalTask Status */
    public void setXX_StatusCriticalTask (String XX_StatusCriticalTask)
    {
        if (!X_Ref_XX_Ref_StatusCriticalTask.isValid(XX_StatusCriticalTask))
        throw new IllegalArgumentException ("XX_StatusCriticalTask Invalid value - " + XX_StatusCriticalTask + " - Reference_ID=1000248 - Aba - Act - Com");
        set_Value ("XX_StatusCriticalTask", XX_StatusCriticalTask);
        
    }
    
    /** Get Status.
    @return Status */
    public String getXX_StatusCriticalTask() 
    {
        return (String)get_Value("XX_StatusCriticalTask");
        
    }
    
    /** Set Associate Supervisor.
    @param XX_SupervisorRole_ID Associate Supervisor */
    public void setXX_SupervisorRole_ID (int XX_SupervisorRole_ID)
    {
        if (XX_SupervisorRole_ID <= 0) set_Value ("XX_SupervisorRole_ID", null);
        else
        set_Value ("XX_SupervisorRole_ID", Integer.valueOf(XX_SupervisorRole_ID));
        
    }
    
    /** Get Associate Supervisor.
    @return Associate Supervisor */
    public int getXX_SupervisorRole_ID() 
    {
        return get_ValueAsInt("XX_SupervisorRole_ID");
        
    }
    
    /** Set Task.
    @param XX_Task Task */
    public void setXX_Task (String XX_Task)
    {
        throw new IllegalArgumentException ("XX_Task is virtual column");
        
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
        throw new IllegalArgumentException ("XX_TasksAlert is virtual column");
        
    }
    
    /** Get Time Critical Task.
    @return Time Critical Task */
    public int getXX_TasksAlert() 
    {
        return get_ValueAsInt("XX_TasksAlert");
        
    }
    
    /** Set Task Type.
    @param XX_TypeTask Tipo de Tarea generada */
    public void setXX_TypeTask (String XX_TypeTask)
    {
        set_ValueNoCheck ("XX_TypeTask", XX_TypeTask);
        
    }
    
    /** Get Task Type.
    @return Tipo de Tarea generada */
    public String getXX_TypeTask() 
    {
        return (String)get_Value("XX_TypeTask");
        
    }
    
    /** Set Associated Number.
    @param XX_Value Associated Number */
    public void setXX_Value (String XX_Value)
    {
        set_Value ("XX_Value", XX_Value);
        
    }
    
    /** Get Associated Number.
    @return Associated Number */
    public String getXX_Value() 
    {
        return (String)get_Value("XX_Value");
        
    }
    
    /** Set Critical Tasks For Close.
    @param XX_VMR_CriticalTaskForClose_ID Critical Tasks For Close */
    public void setXX_VMR_CriticalTaskForClose_ID (int XX_VMR_CriticalTaskForClose_ID)
    {
        if (XX_VMR_CriticalTaskForClose_ID < 1) throw new IllegalArgumentException ("XX_VMR_CriticalTaskForClose_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_CriticalTaskForClose_ID", Integer.valueOf(XX_VMR_CriticalTaskForClose_ID));
        
    }
    
    /** Get Critical Tasks For Close.
    @return Critical Tasks For Close */
    public int getXX_VMR_CriticalTaskForClose_ID() 
    {
        return get_ValueAsInt("XX_VMR_CriticalTaskForClose_ID");
        
    }
    
    /** Set Critical Tasks.
    @param XX_VMR_CriticalTasks_ID Critical Tasks */
    public void setXX_VMR_CriticalTasks_ID (int XX_VMR_CriticalTasks_ID)
    {
        if (XX_VMR_CriticalTasks_ID <= 0) set_Value ("XX_VMR_CriticalTasks_ID", null);
        else
        set_Value ("XX_VMR_CriticalTasks_ID", Integer.valueOf(XX_VMR_CriticalTasks_ID));
        
    }
    
    /** Get Critical Tasks.
    @return Critical Tasks */
    public int getXX_VMR_CriticalTasks_ID() 
    {
        return get_ValueAsInt("XX_VMR_CriticalTasks_ID");
        
    }
    
    
}
