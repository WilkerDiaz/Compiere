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
/** Generated Model for R_IssueKnown
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_R_IssueKnown.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_R_IssueKnown extends PO
{
    /** Standard Constructor
    @param ctx context
    @param R_IssueKnown_ID id
    @param trx transaction
    */
    public X_R_IssueKnown (Ctx ctx, int R_IssueKnown_ID, Trx trx)
    {
        super (ctx, R_IssueKnown_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (R_IssueKnown_ID == 0)
        {
            setIssueSummary (null);
            setR_IssueKnown_ID (0);
            setReleaseNo (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_R_IssueKnown (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=839 */
    public static final int Table_ID=839;
    
    /** TableName=R_IssueKnown */
    public static final String Table_Name="R_IssueKnown";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Issue Status.
    @param IssueStatus Current Status of the Issue */
    public void setIssueStatus (String IssueStatus)
    {
        set_Value ("IssueStatus", IssueStatus);
        
    }
    
    /** Get Issue Status.
    @return Current Status of the Issue */
    public String getIssueStatus() 
    {
        return (String)get_Value("IssueStatus");
        
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
    
    /** Set Known Issue.
    @param R_IssueKnown_ID Known Issue */
    public void setR_IssueKnown_ID (int R_IssueKnown_ID)
    {
        if (R_IssueKnown_ID < 1) throw new IllegalArgumentException ("R_IssueKnown_ID is mandatory.");
        set_ValueNoCheck ("R_IssueKnown_ID", Integer.valueOf(R_IssueKnown_ID));
        
    }
    
    /** Get Known Issue.
    @return Known Issue */
    public int getR_IssueKnown_ID() 
    {
        return get_ValueAsInt("R_IssueKnown_ID");
        
    }
    
    /** Set Issue Recommendation.
    @param R_IssueRecommendation_ID Recommendations how to fix an Issue */
    public void setR_IssueRecommendation_ID (int R_IssueRecommendation_ID)
    {
        if (R_IssueRecommendation_ID <= 0) set_Value ("R_IssueRecommendation_ID", null);
        else
        set_Value ("R_IssueRecommendation_ID", Integer.valueOf(R_IssueRecommendation_ID));
        
    }
    
    /** Get Issue Recommendation.
    @return Recommendations how to fix an Issue */
    public int getR_IssueRecommendation_ID() 
    {
        return get_ValueAsInt("R_IssueRecommendation_ID");
        
    }
    
    /** Set Issue Status.
    @param R_IssueStatus_ID Status of an Issue */
    public void setR_IssueStatus_ID (int R_IssueStatus_ID)
    {
        if (R_IssueStatus_ID <= 0) set_Value ("R_IssueStatus_ID", null);
        else
        set_Value ("R_IssueStatus_ID", Integer.valueOf(R_IssueStatus_ID));
        
    }
    
    /** Get Issue Status.
    @return Status of an Issue */
    public int getR_IssueStatus_ID() 
    {
        return get_ValueAsInt("R_IssueStatus_ID");
        
    }
    
    /** Set Request.
    @param R_Request_ID Request from a Business Partner or Prospect */
    public void setR_Request_ID (int R_Request_ID)
    {
        if (R_Request_ID <= 0) set_Value ("R_Request_ID", null);
        else
        set_Value ("R_Request_ID", Integer.valueOf(R_Request_ID));
        
    }
    
    /** Get Request.
    @return Request from a Business Partner or Prospect */
    public int getR_Request_ID() 
    {
        return get_ValueAsInt("R_Request_ID");
        
    }
    
    /** Set Release No.
    @param ReleaseNo Internal Release Number */
    public void setReleaseNo (String ReleaseNo)
    {
        if (ReleaseNo == null) throw new IllegalArgumentException ("ReleaseNo is mandatory.");
        set_Value ("ReleaseNo", ReleaseNo);
        
    }
    
    /** Get Release No.
    @return Internal Release Number */
    public String getReleaseNo() 
    {
        return (String)get_Value("ReleaseNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getReleaseNo());
        
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
    
    
}
