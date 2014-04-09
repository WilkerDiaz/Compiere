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
/** Generated Model for IP_Requirement
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_IP_Requirement.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_IP_Requirement extends PO
{
    /** Standard Constructor
    @param ctx context
    @param IP_Requirement_ID id
    @param trx transaction
    */
    public X_IP_Requirement (Ctx ctx, int IP_Requirement_ID, Trx trx)
    {
        super (ctx, IP_Requirement_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (IP_Requirement_ID == 0)
        {
            setIP_Requirement_ID (0);
            setIsSummary (false);
            setName (null);
            setRequirementType (null);	// O
            setStandardQty (Env.ZERO);
            setTaskType (null);	// O
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_IP_Requirement (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=906 */
    public static final int Table_ID=906;
    
    /** TableName=IP_Requirement */
    public static final String Table_Name="IP_Requirement";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID <= 0) set_Value ("AD_Table_ID", null);
        else
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
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
    
    /** Set Requirement Phase.
    @param C_ProjectPhaseReq_ID Project Requirements Phase */
    public void setC_ProjectPhaseReq_ID (int C_ProjectPhaseReq_ID)
    {
        if (C_ProjectPhaseReq_ID <= 0) set_Value ("C_ProjectPhaseReq_ID", null);
        else
        set_Value ("C_ProjectPhaseReq_ID", Integer.valueOf(C_ProjectPhaseReq_ID));
        
    }
    
    /** Get Requirement Phase.
    @return Project Requirements Phase */
    public int getC_ProjectPhaseReq_ID() 
    {
        return get_ValueAsInt("C_ProjectPhaseReq_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
        else
        set_Value ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
    }
    
    /** Set Consequences.
    @param Consequences Consequences of the entry */
    public void setConsequences (String Consequences)
    {
        set_Value ("Consequences", Consequences);
        
    }
    
    /** Get Consequences.
    @return Consequences of the entry */
    public String getConsequences() 
    {
        return (String)get_Value("Consequences");
        
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
    
    /** Set Requirement.
    @param IP_Requirement_ID Business Requirement */
    public void setIP_Requirement_ID (int IP_Requirement_ID)
    {
        if (IP_Requirement_ID < 1) throw new IllegalArgumentException ("IP_Requirement_ID is mandatory.");
        set_ValueNoCheck ("IP_Requirement_ID", Integer.valueOf(IP_Requirement_ID));
        
    }
    
    /** Get Requirement.
    @return Business Requirement */
    public int getIP_Requirement_ID() 
    {
        return get_ValueAsInt("IP_Requirement_ID");
        
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
    
    /** Set Prerequisites.
    @param Prerequisites Prerequisites of this entity */
    public void setPrerequisites (String Prerequisites)
    {
        set_Value ("Prerequisites", Prerequisites);
        
    }
    
    /** Get Prerequisites.
    @return Prerequisites of this entity */
    public String getPrerequisites() 
    {
        return (String)get_Value("Prerequisites");
        
    }
    
    /** Other = O */
    public static final String REQUIREMENTTYPE_Other = X_Ref_IP_Requirement_Type.OTHER.getValue();
    /** Determine Scope = S */
    public static final String REQUIREMENTTYPE_DetermineScope = X_Ref_IP_Requirement_Type.DETERMINE_SCOPE.getValue();
    /** Implementation Task = T */
    public static final String REQUIREMENTTYPE_ImplementationTask = X_Ref_IP_Requirement_Type.IMPLEMENTATION_TASK.getValue();
    /** Set Requirement Type.
    @param RequirementType Requirement Type */
    public void setRequirementType (String RequirementType)
    {
        if (RequirementType == null) throw new IllegalArgumentException ("RequirementType is mandatory");
        if (!X_Ref_IP_Requirement_Type.isValid(RequirementType))
        throw new IllegalArgumentException ("RequirementType Invalid value - " + RequirementType + " - Reference_ID=407 - O - S - T");
        set_Value ("RequirementType", RequirementType);
        
    }
    
    /** Get Requirement Type.
    @return Requirement Type */
    public String getRequirementType() 
    {
        return (String)get_Value("RequirementType");
        
    }
    
    /** Set Standard Quantity.
    @param StandardQty Standard Quantity */
    public void setStandardQty (java.math.BigDecimal StandardQty)
    {
        if (StandardQty == null) throw new IllegalArgumentException ("StandardQty is mandatory.");
        set_Value ("StandardQty", StandardQty);
        
    }
    
    /** Get Standard Quantity.
    @return Standard Quantity */
    public java.math.BigDecimal getStandardQty() 
    {
        return get_ValueAsBigDecimal("StandardQty");
        
    }
    
    /** Personal Activity = A */
    public static final String TASKTYPE_PersonalActivity = X_Ref_C_Task_Type.PERSONAL_ACTIVITY.getValue();
    /** Delegation = D */
    public static final String TASKTYPE_Delegation = X_Ref_C_Task_Type.DELEGATION.getValue();
    /** Other = O */
    public static final String TASKTYPE_Other = X_Ref_C_Task_Type.OTHER.getValue();
    /** Research = R */
    public static final String TASKTYPE_Research = X_Ref_C_Task_Type.RESEARCH.getValue();
    /** Test/Verify = T */
    public static final String TASKTYPE_TestVerify = X_Ref_C_Task_Type.TEST_VERIFY.getValue();
    /** Set Task Type.
    @param TaskType Type of Project Task */
    public void setTaskType (String TaskType)
    {
        if (TaskType == null) throw new IllegalArgumentException ("TaskType is mandatory");
        if (!X_Ref_C_Task_Type.isValid(TaskType))
        throw new IllegalArgumentException ("TaskType Invalid value - " + TaskType + " - Reference_ID=408 - A - D - O - R - T");
        set_Value ("TaskType", TaskType);
        
    }
    
    /** Get Task Type.
    @return Type of Project Task */
    public String getTaskType() 
    {
        return (String)get_Value("TaskType");
        
    }
    
    
}
