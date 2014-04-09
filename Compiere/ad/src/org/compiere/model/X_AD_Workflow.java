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
/** Generated Model for AD_Workflow
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Workflow.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Workflow extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Workflow_ID id
    @param trx transaction
    */
    public X_AD_Workflow (Ctx ctx, int AD_Workflow_ID, Trx trx)
    {
        super (ctx, AD_Workflow_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Workflow_ID == 0)
        {
            setAD_Workflow_ID (0);
            setAccessLevel (null);
            setAuthor (null);
            setDuration (0);
            setEntityType (null);	// U
            setIsDefault (false);
            setIsValid (false);
            setName (null);
            setPublishStatus (null);	// U
            setValue (null);
            setVersion (0);
            setWaitingTime (0);
            setWorkflowType (null);	// G
            setWorkingTime (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Workflow (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520148312789L;
    /** Last Updated Timestamp 2009-03-25 15:16:36.0 */
    public static final long updatedMS = 1238022996000L;
    /** AD_Table_ID=117 */
    public static final int Table_ID=117;
    
    /** TableName=AD_Workflow */
    public static final String Table_Name="AD_Workflow";
    
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
    
    /** Set Node.
    @param AD_WF_Node_ID Workflow Node (activity), step or process */
    public void setAD_WF_Node_ID (int AD_WF_Node_ID)
    {
        if (AD_WF_Node_ID <= 0) set_Value ("AD_WF_Node_ID", null);
        else
        set_Value ("AD_WF_Node_ID", Integer.valueOf(AD_WF_Node_ID));
        
    }
    
    /** Get Node.
    @return Workflow Node (activity), step or process */
    public int getAD_WF_Node_ID() 
    {
        return get_ValueAsInt("AD_WF_Node_ID");
        
    }
    
    /** Set Workflow Owner.
    @param AD_WF_Responsible_ID Responsible for Workflow Execution */
    public void setAD_WF_Responsible_ID (int AD_WF_Responsible_ID)
    {
        if (AD_WF_Responsible_ID <= 0) set_Value ("AD_WF_Responsible_ID", null);
        else
        set_Value ("AD_WF_Responsible_ID", Integer.valueOf(AD_WF_Responsible_ID));
        
    }
    
    /** Get Workflow Owner.
    @return Responsible for Workflow Execution */
    public int getAD_WF_Responsible_ID() 
    {
        return get_ValueAsInt("AD_WF_Responsible_ID");
        
    }
    
    /** Set Workflow Processor.
    @param AD_WorkflowProcessor_ID Workflow Processor Server */
    public void setAD_WorkflowProcessor_ID (int AD_WorkflowProcessor_ID)
    {
        if (AD_WorkflowProcessor_ID <= 0) set_Value ("AD_WorkflowProcessor_ID", null);
        else
        set_Value ("AD_WorkflowProcessor_ID", Integer.valueOf(AD_WorkflowProcessor_ID));
        
    }
    
    /** Get Workflow Processor.
    @return Workflow Processor Server */
    public int getAD_WorkflowProcessor_ID() 
    {
        return get_ValueAsInt("AD_WorkflowProcessor_ID");
        
    }
    
    /** Set Workflow.
    @param AD_Workflow_ID Workflow or combination of tasks */
    public void setAD_Workflow_ID (int AD_Workflow_ID)
    {
        if (AD_Workflow_ID < 1) throw new IllegalArgumentException ("AD_Workflow_ID is mandatory.");
        set_ValueNoCheck ("AD_Workflow_ID", Integer.valueOf(AD_Workflow_ID));
        
    }
    
    /** Get Workflow.
    @return Workflow or combination of tasks */
    public int getAD_Workflow_ID() 
    {
        return get_ValueAsInt("AD_Workflow_ID");
        
    }
    
    /** Organization = 1 */
    public static final String ACCESSLEVEL_Organization = X_Ref_AD_Table_Access_Levels.ORGANIZATION.getValue();
    /** Tenant only = 2 */
    public static final String ACCESSLEVEL_TenantOnly = X_Ref_AD_Table_Access_Levels.TENANT_ONLY.getValue();
    /** Tenant+Organization = 3 */
    public static final String ACCESSLEVEL_TenantPlusOrganization = X_Ref_AD_Table_Access_Levels.TENANT_PLUS_ORGANIZATION.getValue();
    /** System only = 4 */
    public static final String ACCESSLEVEL_SystemOnly = X_Ref_AD_Table_Access_Levels.SYSTEM_ONLY.getValue();
    /** System+Tenant = 6 */
    public static final String ACCESSLEVEL_SystemPlusTenant = X_Ref_AD_Table_Access_Levels.SYSTEM_PLUS_TENANT.getValue();
    /** All = 7 */
    public static final String ACCESSLEVEL_All = X_Ref_AD_Table_Access_Levels.ALL.getValue();
    /** Set Data Access Level.
    @param AccessLevel Access Level required */
    public void setAccessLevel (String AccessLevel)
    {
        if (AccessLevel == null) throw new IllegalArgumentException ("AccessLevel is mandatory");
        if (!X_Ref_AD_Table_Access_Levels.isValid(AccessLevel))
        throw new IllegalArgumentException ("AccessLevel Invalid value - " + AccessLevel + " - Reference_ID=5 - 1 - 2 - 3 - 4 - 6 - 7");
        set_Value ("AccessLevel", AccessLevel);
        
    }
    
    /** Get Data Access Level.
    @return Access Level required */
    public String getAccessLevel() 
    {
        return (String)get_Value("AccessLevel");
        
    }
    
    /** Set Author.
    @param Author Author/Creator of the Entity */
    public void setAuthor (String Author)
    {
        if (Author == null) throw new IllegalArgumentException ("Author is mandatory.");
        set_Value ("Author", Author);
        
    }
    
    /** Get Author.
    @return Author/Creator of the Entity */
    public String getAuthor() 
    {
        return (String)get_Value("Author");
        
    }
    
    /** Set Background Workflow Processor.
    @param Background_WFProcessor_ID Workflow Processor for Background Processes */
    public void setBackground_WFProcessor_ID (int Background_WFProcessor_ID)
    {
        if (Background_WFProcessor_ID <= 0) set_Value ("Background_WFProcessor_ID", null);
        else
        set_Value ("Background_WFProcessor_ID", Integer.valueOf(Background_WFProcessor_ID));
        
    }
    
    /** Get Background Workflow Processor.
    @return Workflow Processor for Background Processes */
    public int getBackground_WFProcessor_ID() 
    {
        return get_ValueAsInt("Background_WFProcessor_ID");
        
    }
    
    /** Set Cost.
    @param Cost Cost information */
    public void setCost (java.math.BigDecimal Cost)
    {
        set_Value ("Cost", Cost);
        
    }
    
    /** Get Cost.
    @return Cost information */
    public java.math.BigDecimal getCost() 
    {
        return get_ValueAsBigDecimal("Cost");
        
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
    
    /** Set Document Value Logic.
    @param DocValueLogic Logic to determine Workflow Start - If true, a workflow process is started for the document */
    public void setDocValueLogic (String DocValueLogic)
    {
        set_Value ("DocValueLogic", DocValueLogic);
        
    }
    
    /** Get Document Value Logic.
    @return Logic to determine Workflow Start - If true, a workflow process is started for the document */
    public String getDocValueLogic() 
    {
        return (String)get_Value("DocValueLogic");
        
    }
    
    /** Set Duration.
    @param Duration Normal Duration in Duration Unit */
    public void setDuration (int Duration)
    {
        set_Value ("Duration", Integer.valueOf(Duration));
        
    }
    
    /** Get Duration.
    @return Normal Duration in Duration Unit */
    public int getDuration() 
    {
        return get_ValueAsInt("Duration");
        
    }
    
    /** Set Duration Limit.
    @param DurationLimit Maximum Duration in Duration Unit */
    public void setDurationLimit (int DurationLimit)
    {
        set_Value ("DurationLimit", Integer.valueOf(DurationLimit));
        
    }
    
    /** Get Duration Limit.
    @return Maximum Duration in Duration Unit */
    public int getDurationLimit() 
    {
        return get_ValueAsInt("DurationLimit");
        
    }
    
    /** Day = D */
    public static final String DURATIONUNIT_Day = X_Ref_WF_DurationUnit.DAY.getValue();
    /** Month = M */
    public static final String DURATIONUNIT_Month = X_Ref_WF_DurationUnit.MONTH.getValue();
    /** Year = Y */
    public static final String DURATIONUNIT_Year = X_Ref_WF_DurationUnit.YEAR.getValue();
    /** hour = h */
    public static final String DURATIONUNIT_Hour = X_Ref_WF_DurationUnit.HOUR.getValue();
    /** minute = m */
    public static final String DURATIONUNIT_Minute = X_Ref_WF_DurationUnit.MINUTE.getValue();
    /** second = s */
    public static final String DURATIONUNIT_Second = X_Ref_WF_DurationUnit.SECOND.getValue();
    /** Set Duration Unit.
    @param DurationUnit Unit of Duration */
    public void setDurationUnit (String DurationUnit)
    {
        if (!X_Ref_WF_DurationUnit.isValid(DurationUnit))
        throw new IllegalArgumentException ("DurationUnit Invalid value - " + DurationUnit + " - Reference_ID=299 - D - M - Y - h - m - s");
        set_Value ("DurationUnit", DurationUnit);
        
    }
    
    /** Get Duration Unit.
    @return Unit of Duration */
    public String getDurationUnit() 
    {
        return (String)get_Value("DurationUnit");
        
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
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
    }
    
    /** Set Valid.
    @param IsValid Element is valid */
    public void setIsValid (boolean IsValid)
    {
        set_Value ("IsValid", Boolean.valueOf(IsValid));
        
    }
    
    /** Get Valid.
    @return Element is valid */
    public boolean isValid() 
    {
        return get_ValueAsBoolean("IsValid");
        
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
    
    /** Set Priority.
    @param Priority Indicates if this request is of a high, medium or low priority. */
    public void setPriority (int Priority)
    {
        set_Value ("Priority", Integer.valueOf(Priority));
        
    }
    
    /** Get Priority.
    @return Indicates if this request is of a high, medium or low priority. */
    public int getPriority() 
    {
        return get_ValueAsInt("Priority");
        
    }
    
    /** Released = R */
    public static final String PUBLISHSTATUS_Released = X_Ref__PublishStatus.RELEASED.getValue();
    /** Test = T */
    public static final String PUBLISHSTATUS_Test = X_Ref__PublishStatus.TEST.getValue();
    /** Under Revision = U */
    public static final String PUBLISHSTATUS_UnderRevision = X_Ref__PublishStatus.UNDER_REVISION.getValue();
    /** Void = V */
    public static final String PUBLISHSTATUS_Void = X_Ref__PublishStatus.VOID.getValue();
    /** Set Publication Status.
    @param PublishStatus Status of Publication */
    public void setPublishStatus (String PublishStatus)
    {
        if (PublishStatus == null) throw new IllegalArgumentException ("PublishStatus is mandatory");
        if (!X_Ref__PublishStatus.isValid(PublishStatus))
        throw new IllegalArgumentException ("PublishStatus Invalid value - " + PublishStatus + " - Reference_ID=310 - R - T - U - V");
        set_Value ("PublishStatus", PublishStatus);
        
    }
    
    /** Get Publication Status.
    @return Status of Publication */
    public String getPublishStatus() 
    {
        return (String)get_Value("PublishStatus");
        
    }
    
    /** Set Valid from.
    @param ValidFrom Valid from including this date (first day) */
    public void setValidFrom (Timestamp ValidFrom)
    {
        set_Value ("ValidFrom", ValidFrom);
        
    }
    
    /** Get Valid from.
    @return Valid from including this date (first day) */
    public Timestamp getValidFrom() 
    {
        return (Timestamp)get_Value("ValidFrom");
        
    }
    
    /** Set Valid to.
    @param ValidTo Valid to including this date (last day) */
    public void setValidTo (Timestamp ValidTo)
    {
        set_Value ("ValidTo", ValidTo);
        
    }
    
    /** Get Valid to.
    @return Valid to including this date (last day) */
    public Timestamp getValidTo() 
    {
        return (Timestamp)get_Value("ValidTo");
        
    }
    
    /** Set Validate Workflow.
    @param ValidateWorkflow Validate Workflow */
    public void setValidateWorkflow (String ValidateWorkflow)
    {
        set_Value ("ValidateWorkflow", ValidateWorkflow);
        
    }
    
    /** Get Validate Workflow.
    @return Validate Workflow */
    public String getValidateWorkflow() 
    {
        return (String)get_Value("ValidateWorkflow");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Version.
    @param Version Version of the table definition */
    public void setVersion (int Version)
    {
        set_Value ("Version", Integer.valueOf(Version));
        
    }
    
    /** Get Version.
    @return Version of the table definition */
    public int getVersion() 
    {
        return get_ValueAsInt("Version");
        
    }
    
    /** Set Waiting Time.
    @param WaitingTime Workflow Simulation Waiting time */
    public void setWaitingTime (int WaitingTime)
    {
        set_Value ("WaitingTime", Integer.valueOf(WaitingTime));
        
    }
    
    /** Get Waiting Time.
    @return Workflow Simulation Waiting time */
    public int getWaitingTime() 
    {
        return get_ValueAsInt("WaitingTime");
        
    }
    
    /** General = G */
    public static final String WORKFLOWTYPE_General = X_Ref_AD_Workflow_Type.GENERAL.getValue();
    /** Document Process = P */
    public static final String WORKFLOWTYPE_DocumentProcess = X_Ref_AD_Workflow_Type.DOCUMENT_PROCESS.getValue();
    /** Document Value = V */
    public static final String WORKFLOWTYPE_DocumentValue = X_Ref_AD_Workflow_Type.DOCUMENT_VALUE.getValue();
    /** Set Workflow Type.
    @param WorkflowType Type of Workflow */
    public void setWorkflowType (String WorkflowType)
    {
        if (WorkflowType == null) throw new IllegalArgumentException ("WorkflowType is mandatory");
        if (!X_Ref_AD_Workflow_Type.isValid(WorkflowType))
        throw new IllegalArgumentException ("WorkflowType Invalid value - " + WorkflowType + " - Reference_ID=328 - G - P - V");
        set_Value ("WorkflowType", WorkflowType);
        
    }
    
    /** Get Workflow Type.
    @return Type of Workflow */
    public String getWorkflowType() 
    {
        return (String)get_Value("WorkflowType");
        
    }
    
    /** Set Working Time.
    @param WorkingTime Workflow Simulation Execution Time */
    public void setWorkingTime (int WorkingTime)
    {
        set_Value ("WorkingTime", Integer.valueOf(WorkingTime));
        
    }
    
    /** Get Working Time.
    @return Workflow Simulation Execution Time */
    public int getWorkingTime() 
    {
        return get_ValueAsInt("WorkingTime");
        
    }
    
    
}
