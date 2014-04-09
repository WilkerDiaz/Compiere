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
/** Generated Model for R_IssueSystem
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_R_IssueSystem.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_R_IssueSystem extends PO
{
    /** Standard Constructor
    @param ctx context
    @param R_IssueSystem_ID id
    @param trx transaction
    */
    public X_R_IssueSystem (Ctx ctx, int R_IssueSystem_ID, Trx trx)
    {
        super (ctx, R_IssueSystem_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (R_IssueSystem_ID == 0)
        {
            setDBAddress (null);
            setR_IssueSystem_ID (0);
            setSystemStatus (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_R_IssueSystem (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=843 */
    public static final int Table_ID=843;
    
    /** TableName=R_IssueSystem */
    public static final String Table_Name="R_IssueSystem";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Asset.
    @param A_Asset_ID Asset used internally or by customers */
    public void setA_Asset_ID (int A_Asset_ID)
    {
        if (A_Asset_ID <= 0) set_Value ("A_Asset_ID", null);
        else
        set_Value ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
        
    }
    
    /** Get Asset.
    @return Asset used internally or by customers */
    public int getA_Asset_ID() 
    {
        return get_ValueAsInt("A_Asset_ID");
        
    }
    
    /** Set DB Address.
    @param DBAddress JDBC URL of the database server */
    public void setDBAddress (String DBAddress)
    {
        if (DBAddress == null) throw new IllegalArgumentException ("DBAddress is mandatory.");
        set_ValueNoCheck ("DBAddress", DBAddress);
        
    }
    
    /** Get DB Address.
    @return JDBC URL of the database server */
    public String getDBAddress() 
    {
        return (String)get_Value("DBAddress");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDBAddress());
        
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
    
    /** Set Issue System.
    @param R_IssueSystem_ID System creating the issue */
    public void setR_IssueSystem_ID (int R_IssueSystem_ID)
    {
        if (R_IssueSystem_ID < 1) throw new IllegalArgumentException ("R_IssueSystem_ID is mandatory.");
        set_ValueNoCheck ("R_IssueSystem_ID", Integer.valueOf(R_IssueSystem_ID));
        
    }
    
    /** Get Issue System.
    @return System creating the issue */
    public int getR_IssueSystem_ID() 
    {
        return get_ValueAsInt("R_IssueSystem_ID");
        
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
    
    
}
