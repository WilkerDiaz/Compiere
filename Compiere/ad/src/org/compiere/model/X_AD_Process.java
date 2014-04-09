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
/** Generated Model for AD_Process
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Process.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Process extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Process_ID id
    @param trx transaction
    */
    public X_AD_Process (Ctx ctx, int AD_Process_ID, Trx trx)
    {
        super (ctx, AD_Process_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Process_ID == 0)
        {
            setAD_Process_ID (0);
            setAccessLevel (null);
            setEntityType (null);	// U
            setIsBetaFunctionality (false);
            setIsDashboard (false);	// N
            setIsExternal (false);
            setIsReport (false);
            setIsServerProcess (false);
            setName (null);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Process (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27529978868789L;
    /** Last Updated Timestamp 2009-07-17 10:59:12.0 */
    public static final long updatedMS = 1247853552000L;
    /** AD_Table_ID=284 */
    public static final int Table_ID=284;
    
    /** TableName=AD_Process */
    public static final String Table_Name="AD_Process";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business View.
    @param AD_BView_ID The logical subset of related data for the purposes of reporting */
    public void setAD_BView_ID (int AD_BView_ID)
    {
        if (AD_BView_ID <= 0) set_Value ("AD_BView_ID", null);
        else
        set_Value ("AD_BView_ID", Integer.valueOf(AD_BView_ID));
        
    }
    
    /** Get Business View.
    @return The logical subset of related data for the purposes of reporting */
    public int getAD_BView_ID() 
    {
        return get_ValueAsInt("AD_BView_ID");
        
    }
    
    /** Set Context Area.
    @param AD_CtxArea_ID Business Domain Area Terminology */
    public void setAD_CtxArea_ID (int AD_CtxArea_ID)
    {
        if (AD_CtxArea_ID <= 0) set_Value ("AD_CtxArea_ID", null);
        else
        set_Value ("AD_CtxArea_ID", Integer.valueOf(AD_CtxArea_ID));
        
    }
    
    /** Get Context Area.
    @return Business Domain Area Terminology */
    public int getAD_CtxArea_ID() 
    {
        return get_ValueAsInt("AD_CtxArea_ID");
        
    }
    
    /** Set Print Format.
    @param AD_PrintFormat_ID Data Print Format */
    public void setAD_PrintFormat_ID (int AD_PrintFormat_ID)
    {
        if (AD_PrintFormat_ID <= 0) set_Value ("AD_PrintFormat_ID", null);
        else
        set_Value ("AD_PrintFormat_ID", Integer.valueOf(AD_PrintFormat_ID));
        
    }
    
    /** Get Print Format.
    @return Data Print Format */
    public int getAD_PrintFormat_ID() 
    {
        return get_ValueAsInt("AD_PrintFormat_ID");
        
    }
    
    /** Set Process.
    @param AD_Process_ID Process or Report */
    public void setAD_Process_ID (int AD_Process_ID)
    {
        if (AD_Process_ID < 1) throw new IllegalArgumentException ("AD_Process_ID is mandatory.");
        set_ValueNoCheck ("AD_Process_ID", Integer.valueOf(AD_Process_ID));
        
    }
    
    /** Get Process.
    @return Process or Report */
    public int getAD_Process_ID() 
    {
        return get_ValueAsInt("AD_Process_ID");
        
    }
    
    /** Set External Report.
    @param AD_ReportTemplate_ID External Report */
    public void setAD_ReportTemplate_ID (int AD_ReportTemplate_ID)
    {
        if (AD_ReportTemplate_ID <= 0) set_Value ("AD_ReportTemplate_ID", null);
        else
        set_Value ("AD_ReportTemplate_ID", Integer.valueOf(AD_ReportTemplate_ID));
        
    }
    
    /** Get External Report.
    @return External Report */
    public int getAD_ReportTemplate_ID() 
    {
        return get_ValueAsInt("AD_ReportTemplate_ID");
        
    }
    
    /** Set Report View.
    @param AD_ReportView_ID View used to generate this report */
    public void setAD_ReportView_ID (int AD_ReportView_ID)
    {
        if (AD_ReportView_ID <= 0) set_Value ("AD_ReportView_ID", null);
        else
        set_Value ("AD_ReportView_ID", Integer.valueOf(AD_ReportView_ID));
        
    }
    
    /** Get Report View.
    @return View used to generate this report */
    public int getAD_ReportView_ID() 
    {
        return get_ValueAsInt("AD_ReportView_ID");
        
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
    
    /** Set Add to Menu.
    @param AddMenu Add to Menu */
    public void setAddMenu (String AddMenu)
    {
        set_Value ("AddMenu", AddMenu);
        
    }
    
    /** Get Add to Menu.
    @return Add to Menu */
    public String getAddMenu() 
    {
        return (String)get_Value("AddMenu");
        
    }
    
    /** Set Classname.
    @param Classname Java Classname */
    public void setClassname (String Classname)
    {
        set_Value ("Classname", Classname);
        
    }
    
    /** Get Classname.
    @return Java Classname */
    public String getClassname() 
    {
        return (String)get_Value("Classname");
        
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
    
    /** Set Beta Functionality.
    @param IsBetaFunctionality This functionality is considered Beta */
    public void setIsBetaFunctionality (boolean IsBetaFunctionality)
    {
        set_Value ("IsBetaFunctionality", Boolean.valueOf(IsBetaFunctionality));
        
    }
    
    /** Get Beta Functionality.
    @return This functionality is considered Beta */
    public boolean isBetaFunctionality() 
    {
        return get_ValueAsBoolean("IsBetaFunctionality");
        
    }
    
    /** Set Dashboard.
    @param IsDashboard Indicates if this report should be rendered as a dashboard */
    public void setIsDashboard (boolean IsDashboard)
    {
        set_Value ("IsDashboard", Boolean.valueOf(IsDashboard));
        
    }
    
    /** Get Dashboard.
    @return Indicates if this report should be rendered as a dashboard */
    public boolean isDashboard() 
    {
        return get_ValueAsBoolean("IsDashboard");
        
    }
    
    /** Set Direct print.
    @param IsDirectPrint Print without dialog */
    public void setIsDirectPrint (boolean IsDirectPrint)
    {
        set_Value ("IsDirectPrint", Boolean.valueOf(IsDirectPrint));
        
    }
    
    /** Get Direct print.
    @return Print without dialog */
    public boolean isDirectPrint() 
    {
        return get_ValueAsBoolean("IsDirectPrint");
        
    }
    
    /** Set External.
    @param IsExternal External */
    public void setIsExternal (boolean IsExternal)
    {
        set_Value ("IsExternal", Boolean.valueOf(IsExternal));
        
    }
    
    /** Get External.
    @return External */
    public boolean isExternal() 
    {
        return get_ValueAsBoolean("IsExternal");
        
    }
    
    /** Set Report.
    @param IsReport Indicates a Report record */
    public void setIsReport (boolean IsReport)
    {
        set_Value ("IsReport", Boolean.valueOf(IsReport));
        
    }
    
    /** Get Report.
    @return Indicates a Report record */
    public boolean isReport() 
    {
        return get_ValueAsBoolean("IsReport");
        
    }
    
    /** Set Server Process.
    @param IsServerProcess Run this Process on Server only */
    public void setIsServerProcess (boolean IsServerProcess)
    {
        set_Value ("IsServerProcess", Boolean.valueOf(IsServerProcess));
        
    }
    
    /** Get Server Process.
    @return Run this Process on Server only */
    public boolean isServerProcess() 
    {
        return get_ValueAsBoolean("IsServerProcess");
        
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
    
    /** Set Procedure.
    @param ProcedureName Name of the Database Procedure */
    public void setProcedureName (String ProcedureName)
    {
        set_Value ("ProcedureName", ProcedureName);
        
    }
    
    /** Get Procedure.
    @return Name of the Database Procedure */
    public String getProcedureName() 
    {
        return (String)get_Value("ProcedureName");
        
    }
    
    /** Set Statistic Count.
    @param Statistic_Count Internal statistics how often the entity was used */
    public void setStatistic_Count (int Statistic_Count)
    {
        set_Value ("Statistic_Count", Integer.valueOf(Statistic_Count));
        
    }
    
    /** Get Statistic Count.
    @return Internal statistics how often the entity was used */
    public int getStatistic_Count() 
    {
        return get_ValueAsInt("Statistic_Count");
        
    }
    
    /** Set Statistic Seconds.
    @param Statistic_Seconds Internal statistics indicating how many seconds a process took */
    public void setStatistic_Seconds (java.math.BigDecimal Statistic_Seconds)
    {
        set_Value ("Statistic_Seconds", Statistic_Seconds);
        
    }
    
    /** Get Statistic Seconds.
    @return Internal statistics indicating how many seconds a process took */
    public java.math.BigDecimal getStatistic_Seconds() 
    {
        return get_ValueAsBigDecimal("Statistic_Seconds");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getValue());
        
    }
    
    /** Set Workflow Key.
    @param WorkflowValue Key of the Workflow to start */
    public void setWorkflowValue (String WorkflowValue)
    {
        set_Value ("WorkflowValue", WorkflowValue);
        
    }
    
    /** Get Workflow Key.
    @return Key of the Workflow to start */
    public String getWorkflowValue() 
    {
        return (String)get_Value("WorkflowValue");
        
    }
    
    
}
