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
/** Generated Model for AD_Issue
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Issue.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Issue extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Issue_ID id
    @param trx transaction
    */
    public X_AD_Issue (Ctx ctx, int AD_Issue_ID, Trx trx)
    {
        super (ctx, AD_Issue_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Issue_ID == 0)
        {
            setAD_Issue_ID (0);
            setIssueSummary (null);
            setName (null);	// .
            setProcessed (false);	// N
            setReleaseNo (null);	// .
            setSystemStatus (null);	// E
            setUserName (null);	// .
            setVersion (null);	// .
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Issue (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=828 */
    public static final int Table_ID=828;
    
    /** TableName=AD_Issue */
    public static final String Table_Name="AD_Issue";
    
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
    
    /** Set System Issue.
    @param AD_Issue_ID Automatically created or manually entered System Issue */
    public void setAD_Issue_ID (int AD_Issue_ID)
    {
        if (AD_Issue_ID < 1) throw new IllegalArgumentException ("AD_Issue_ID is mandatory.");
        set_ValueNoCheck ("AD_Issue_ID", Integer.valueOf(AD_Issue_ID));
        
    }
    
    /** Get System Issue.
    @return Automatically created or manually entered System Issue */
    public int getAD_Issue_ID() 
    {
        return get_ValueAsInt("AD_Issue_ID");
        
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
    
    /** Set Asset.
    @param A_Asset_ID Asset used internally or by customers */
    public void setA_Asset_ID (int A_Asset_ID)
    {
        if (A_Asset_ID <= 0) set_ValueNoCheck ("A_Asset_ID", null);
        else
        set_ValueNoCheck ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
        
    }
    
    /** Get Asset.
    @return Asset used internally or by customers */
    public int getA_Asset_ID() 
    {
        return get_ValueAsInt("A_Asset_ID");
        
    }
    
    /** Set Comments.
    @param Comments Comments or additional information */
    public void setComments (String Comments)
    {
        set_Value ("Comments", Comments);
        
    }
    
    /** Get Comments.
    @return Comments or additional information */
    public String getComments() 
    {
        return (String)get_Value("Comments");
        
    }
    
    /** Set DB Address.
    @param DBAddress JDBC URL of the database server */
    public void setDBAddress (String DBAddress)
    {
        set_ValueNoCheck ("DBAddress", DBAddress);
        
    }
    
    /** Get DB Address.
    @return JDBC URL of the database server */
    public String getDBAddress() 
    {
        return (String)get_Value("DBAddress");
        
    }
    
    /** Set Database.
    @param DatabaseInfo Database Information */
    public void setDatabaseInfo (String DatabaseInfo)
    {
        set_ValueNoCheck ("DatabaseInfo", DatabaseInfo);
        
    }
    
    /** Get Database.
    @return Database Information */
    public String getDatabaseInfo() 
    {
        return (String)get_Value("DatabaseInfo");
        
    }
    
    /** Set Error Trace.
    @param ErrorTrace System Error Trace */
    public void setErrorTrace (String ErrorTrace)
    {
        set_Value ("ErrorTrace", ErrorTrace);
        
    }
    
    /** Get Error Trace.
    @return System Error Trace */
    public String getErrorTrace() 
    {
        return (String)get_Value("ErrorTrace");
        
    }
    
    /** No = N */
    public static final String ISREPRODUCIBLE_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISREPRODUCIBLE_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Reproducible.
    @param IsReproducible Problem can be reproduced in Gardenworld */
    public void setIsReproducible (String IsReproducible)
    {
        if (!X_Ref__YesNo.isValid(IsReproducible))
        throw new IllegalArgumentException ("IsReproducible Invalid value - " + IsReproducible + " - Reference_ID=319 - N - Y");
        set_Value ("IsReproducible", IsReproducible);
        
    }
    
    /** Get Reproducible.
    @return Problem can be reproduced in Gardenworld */
    public String getIsReproducible() 
    {
        return (String)get_Value("IsReproducible");
        
    }
    
    /** No = N */
    public static final String ISVANILLASYSTEM_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISVANILLASYSTEM_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Vanilla System.
    @param IsVanillaSystem The system was NOT compiled from Source - i.e. standard distribution */
    public void setIsVanillaSystem (String IsVanillaSystem)
    {
        if (!X_Ref__YesNo.isValid(IsVanillaSystem))
        throw new IllegalArgumentException ("IsVanillaSystem Invalid value - " + IsVanillaSystem + " - Reference_ID=319 - N - Y");
        set_Value ("IsVanillaSystem", IsVanillaSystem);
        
    }
    
    /** Get Vanilla System.
    @return The system was NOT compiled from Source - i.e. standard distribution */
    public String getIsVanillaSystem() 
    {
        return (String)get_Value("IsVanillaSystem");
        
    }
    
    /** Workbench = B */
    public static final String ISSUESOURCE_Workbench = X_Ref_AD_Menu_Action.WORKBENCH.getValue();
    /** WorkFlow = F */
    public static final String ISSUESOURCE_WorkFlow = X_Ref_AD_Menu_Action.WORK_FLOW.getValue();
    /** Process = P */
    public static final String ISSUESOURCE_Process = X_Ref_AD_Menu_Action.PROCESS.getValue();
    /** Report = R */
    public static final String ISSUESOURCE_Report = X_Ref_AD_Menu_Action.REPORT.getValue();
    /** Task = T */
    public static final String ISSUESOURCE_Task = X_Ref_AD_Menu_Action.TASK.getValue();
    /** Window = W */
    public static final String ISSUESOURCE_Window = X_Ref_AD_Menu_Action.WINDOW.getValue();
    /** Form = X */
    public static final String ISSUESOURCE_Form = X_Ref_AD_Menu_Action.FORM.getValue();
    /** Set Source.
    @param IssueSource Issue Source */
    public void setIssueSource (String IssueSource)
    {
        if (!X_Ref_AD_Menu_Action.isValid(IssueSource))
        throw new IllegalArgumentException ("IssueSource Invalid value - " + IssueSource + " - Reference_ID=104 - B - F - P - R - T - W - X");
        set_Value ("IssueSource", IssueSource);
        
    }
    
    /** Get Source.
    @return Issue Source */
    public String getIssueSource() 
    {
        return (String)get_Value("IssueSource");
        
    }
    
    /** Set Issue Summary.
    @param IssueSummary Issue Summary */
    public void setIssueSummary (String IssueSummary)
    {
        if (IssueSummary == null) throw new IllegalArgumentException ("IssueSummary is mandatory.");
        set_Value ("IssueSummary", IssueSummary);
        
    }
    
    /** Get Issue Summary.
    @return Issue Summary */
    public String getIssueSummary() 
    {
        return (String)get_Value("IssueSummary");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getIssueSummary());
        
    }
    
    /** Set Java Info.
    @param JavaInfo Java Version Info */
    public void setJavaInfo (String JavaInfo)
    {
        set_ValueNoCheck ("JavaInfo", JavaInfo);
        
    }
    
    /** Get Java Info.
    @return Java Version Info */
    public String getJavaInfo() 
    {
        return (String)get_Value("JavaInfo");
        
    }
    
    /** Set Line.
    @param LineNo Line No */
    public void setLineNo (int LineNo)
    {
        set_Value ("LineNo", Integer.valueOf(LineNo));
        
    }
    
    /** Get Line.
    @return Line No */
    public int getLineNo() 
    {
        return get_ValueAsInt("LineNo");
        
    }
    
    /** Set Local Host.
    @param Local_Host Local Host Info */
    public void setLocal_Host (String Local_Host)
    {
        set_ValueNoCheck ("Local_Host", Local_Host);
        
    }
    
    /** Get Local Host.
    @return Local Host Info */
    public String getLocal_Host() 
    {
        return (String)get_Value("Local_Host");
        
    }
    
    /** Set Logger.
    @param LoggerName Logger Name */
    public void setLoggerName (String LoggerName)
    {
        set_Value ("LoggerName", LoggerName);
        
    }
    
    /** Get Logger.
    @return Logger Name */
    public String getLoggerName() 
    {
        return (String)get_Value("LoggerName");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_ValueNoCheck ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Operating System.
    @param OperatingSystemInfo Operating System Info */
    public void setOperatingSystemInfo (String OperatingSystemInfo)
    {
        set_ValueNoCheck ("OperatingSystemInfo", OperatingSystemInfo);
        
    }
    
    /** Get Operating System.
    @return Operating System Info */
    public String getOperatingSystemInfo() 
    {
        return (String)get_Value("OperatingSystemInfo");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
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
    
    /** Set Profile.
    @param ProfileInfo Information to help profiling the system for solving support issues */
    public void setProfileInfo (String ProfileInfo)
    {
        set_ValueNoCheck ("ProfileInfo", ProfileInfo);
        
    }
    
    /** Get Profile.
    @return Information to help profiling the system for solving support issues */
    public String getProfileInfo() 
    {
        return (String)get_Value("ProfileInfo");
        
    }
    
    /** Set Known Issue.
    @param R_IssueKnown_ID Known Issue */
    public void setR_IssueKnown_ID (int R_IssueKnown_ID)
    {
        if (R_IssueKnown_ID <= 0) set_Value ("R_IssueKnown_ID", null);
        else
        set_Value ("R_IssueKnown_ID", Integer.valueOf(R_IssueKnown_ID));
        
    }
    
    /** Get Known Issue.
    @return Known Issue */
    public int getR_IssueKnown_ID() 
    {
        return get_ValueAsInt("R_IssueKnown_ID");
        
    }
    
    /** Set Issue Project.
    @param R_IssueProject_ID Implementation Projects */
    public void setR_IssueProject_ID (int R_IssueProject_ID)
    {
        if (R_IssueProject_ID <= 0) set_Value ("R_IssueProject_ID", null);
        else
        set_Value ("R_IssueProject_ID", Integer.valueOf(R_IssueProject_ID));
        
    }
    
    /** Get Issue Project.
    @return Implementation Projects */
    public int getR_IssueProject_ID() 
    {
        return get_ValueAsInt("R_IssueProject_ID");
        
    }
    
    /** Set Issue System.
    @param R_IssueSystem_ID System creating the issue */
    public void setR_IssueSystem_ID (int R_IssueSystem_ID)
    {
        if (R_IssueSystem_ID <= 0) set_Value ("R_IssueSystem_ID", null);
        else
        set_Value ("R_IssueSystem_ID", Integer.valueOf(R_IssueSystem_ID));
        
    }
    
    /** Get Issue System.
    @return System creating the issue */
    public int getR_IssueSystem_ID() 
    {
        return get_ValueAsInt("R_IssueSystem_ID");
        
    }
    
    /** Set Issue User.
    @param R_IssueUser_ID User who reported issues */
    public void setR_IssueUser_ID (int R_IssueUser_ID)
    {
        if (R_IssueUser_ID <= 0) set_Value ("R_IssueUser_ID", null);
        else
        set_Value ("R_IssueUser_ID", Integer.valueOf(R_IssueUser_ID));
        
    }
    
    /** Get Issue User.
    @return User who reported issues */
    public int getR_IssueUser_ID() 
    {
        return get_ValueAsInt("R_IssueUser_ID");
        
    }
    
    /** Set Request.
    @param R_Request_ID Request from a Business Partner or Prospect */
    public void setR_Request_ID (int R_Request_ID)
    {
        if (R_Request_ID <= 0) set_ValueNoCheck ("R_Request_ID", null);
        else
        set_ValueNoCheck ("R_Request_ID", Integer.valueOf(R_Request_ID));
        
    }
    
    /** Get Request.
    @return Request from a Business Partner or Prospect */
    public int getR_Request_ID() 
    {
        return get_ValueAsInt("R_Request_ID");
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID <= 0) set_ValueNoCheck ("Record_ID", null);
        else
        set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set Release No.
    @param ReleaseNo Internal Release Number */
    public void setReleaseNo (String ReleaseNo)
    {
        if (ReleaseNo == null) throw new IllegalArgumentException ("ReleaseNo is mandatory.");
        set_ValueNoCheck ("ReleaseNo", ReleaseNo);
        
    }
    
    /** Get Release No.
    @return Internal Release Number */
    public String getReleaseNo() 
    {
        return (String)get_Value("ReleaseNo");
        
    }
    
    /** Set Release Tag.
    @param ReleaseTag Release Tag */
    public void setReleaseTag (String ReleaseTag)
    {
        set_Value ("ReleaseTag", ReleaseTag);
        
    }
    
    /** Get Release Tag.
    @return Release Tag */
    public String getReleaseTag() 
    {
        return (String)get_Value("ReleaseTag");
        
    }
    
    /** Set Remote Addr.
    @param Remote_Addr Remote Address */
    public void setRemote_Addr (String Remote_Addr)
    {
        set_ValueNoCheck ("Remote_Addr", Remote_Addr);
        
    }
    
    /** Get Remote Addr.
    @return Remote Address */
    public String getRemote_Addr() 
    {
        return (String)get_Value("Remote_Addr");
        
    }
    
    /** Set Remote Host.
    @param Remote_Host Remote host Info */
    public void setRemote_Host (String Remote_Host)
    {
        set_ValueNoCheck ("Remote_Host", Remote_Host);
        
    }
    
    /** Get Remote Host.
    @return Remote host Info */
    public String getRemote_Host() 
    {
        return (String)get_Value("Remote_Host");
        
    }
    
    /** Set Request Document No.
    @param RequestDocumentNo Compiere Request Document No */
    public void setRequestDocumentNo (String RequestDocumentNo)
    {
        set_ValueNoCheck ("RequestDocumentNo", RequestDocumentNo);
        
    }
    
    /** Get Request Document No.
    @return Compiere Request Document No */
    public String getRequestDocumentNo() 
    {
        return (String)get_Value("RequestDocumentNo");
        
    }
    
    /** Set Response Text.
    @param ResponseText Request Response Text */
    public void setResponseText (String ResponseText)
    {
        set_ValueNoCheck ("ResponseText", ResponseText);
        
    }
    
    /** Get Response Text.
    @return Request Response Text */
    public String getResponseText() 
    {
        return (String)get_Value("ResponseText");
        
    }
    
    /** Set Source Class.
    @param SourceClassName Source Class Name */
    public void setSourceClassName (String SourceClassName)
    {
        set_Value ("SourceClassName", SourceClassName);
        
    }
    
    /** Get Source Class.
    @return Source Class Name */
    public String getSourceClassName() 
    {
        return (String)get_Value("SourceClassName");
        
    }
    
    /** Set Source Method.
    @param SourceMethodName Source Method Name */
    public void setSourceMethodName (String SourceMethodName)
    {
        set_Value ("SourceMethodName", SourceMethodName);
        
    }
    
    /** Get Source Method.
    @return Source Method Name */
    public String getSourceMethodName() 
    {
        return (String)get_Value("SourceMethodName");
        
    }
    
    /** Set Stack Trace.
    @param StackTrace System Log Trace */
    public void setStackTrace (String StackTrace)
    {
        set_Value ("StackTrace", StackTrace);
        
    }
    
    /** Get Stack Trace.
    @return System Log Trace */
    public String getStackTrace() 
    {
        return (String)get_Value("StackTrace");
        
    }
    
    /** Set Statistics.
    @param StatisticsInfo Information to help profiling the system for solving support issues */
    public void setStatisticsInfo (String StatisticsInfo)
    {
        set_ValueNoCheck ("StatisticsInfo", StatisticsInfo);
        
    }
    
    /** Get Statistics.
    @return Information to help profiling the system for solving support issues */
    public String getStatisticsInfo() 
    {
        return (String)get_Value("StatisticsInfo");
        
    }
    
    /** Set Support EMail.
    @param SupportEMail EMail address to send support information and updates to */
    public void setSupportEMail (String SupportEMail)
    {
        set_Value ("SupportEMail", SupportEMail);
        
    }
    
    /** Get Support EMail.
    @return EMail address to send support information and updates to */
    public String getSupportEMail() 
    {
        return (String)get_Value("SupportEMail");
        
    }
    
    /** Evaluation = E */
    public static final String SYSTEMSTATUS_Evaluation = X_Ref_AD_System_Status.EVALUATION.getValue();
    /** Implementation = I */
    public static final String SYSTEMSTATUS_Implementation = X_Ref_AD_System_Status.IMPLEMENTATION.getValue();
    /** Production = P */
    public static final String SYSTEMSTATUS_Production = X_Ref_AD_System_Status.PRODUCTION.getValue();
    /** Set System Status.
    @param SystemStatus Status of the system - Support priority depends on system status */
    public void setSystemStatus (String SystemStatus)
    {
        if (SystemStatus == null) throw new IllegalArgumentException ("SystemStatus is mandatory");
        if (!X_Ref_AD_System_Status.isValid(SystemStatus))
        throw new IllegalArgumentException ("SystemStatus Invalid value - " + SystemStatus + " - Reference_ID=374 - E - I - P");
        set_Value ("SystemStatus", SystemStatus);
        
    }
    
    /** Get System Status.
    @return Status of the system - Support priority depends on system status */
    public String getSystemStatus() 
    {
        return (String)get_Value("SystemStatus");
        
    }
    
    /** Set Registered EMail.
    @param UserName Email of the responsible for the System */
    public void setUserName (String UserName)
    {
        if (UserName == null) throw new IllegalArgumentException ("UserName is mandatory.");
        set_ValueNoCheck ("UserName", UserName);
        
    }
    
    /** Get Registered EMail.
    @return Email of the responsible for the System */
    public String getUserName() 
    {
        return (String)get_Value("UserName");
        
    }
    
    /** Set Version.
    @param Version Version of the table definition */
    public void setVersion (String Version)
    {
        if (Version == null) throw new IllegalArgumentException ("Version is mandatory.");
        set_ValueNoCheck ("Version", Version);
        
    }
    
    /** Get Version.
    @return Version of the table definition */
    public String getVersion() 
    {
        return (String)get_Value("Version");
        
    }
    
    
}
