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
/** Generated Model for AD_System
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_System.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_System extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_System_ID id
    @param trx transaction
    */
    public X_AD_System (Ctx ctx, int AD_System_ID, Trx trx)
    {
        super (ctx, AD_System_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_System_ID == 0)
        {
            setAD_System_ID (0);
            setIsAllowStatistics (false);
            setIsAutoErrorReport (true);	// Y
            setName (null);
            setPassword (null);
            setReleaseNo (null);
            setReplicationType (null);	// L
            setSystemStatus (null);	// E
            setUserName (null);
            setVersion (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_System (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27506221244789L;
    /** Last Updated Timestamp 2008-10-15 11:38:48.0 */
    public static final long updatedMS = 1224095928000L;
    /** AD_Table_ID=531 */
    public static final int Table_ID=531;
    
    /** TableName=AD_System */
    public static final String Table_Name="AD_System";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set System.
    @param AD_System_ID System Definition */
    public void setAD_System_ID (int AD_System_ID)
    {
        if (AD_System_ID < 1) throw new IllegalArgumentException ("AD_System_ID is mandatory.");
        set_ValueNoCheck ("AD_System_ID", Integer.valueOf(AD_System_ID));
        
    }
    
    /** Get System.
    @return System Definition */
    public int getAD_System_ID() 
    {
        return get_ValueAsInt("AD_System_ID");
        
    }
    
    /** Set Custom Prefix.
    @param CustomPrefix Prefix for Custom entities */
    public void setCustomPrefix (String CustomPrefix)
    {
        set_Value ("CustomPrefix", CustomPrefix);
        
    }
    
    /** Get Custom Prefix.
    @return Prefix for Custom entities */
    public String getCustomPrefix() 
    {
        return (String)get_Value("CustomPrefix");
        
    }
    
    /** Set DB Address.
    @param DBAddress JDBC URL of the database server */
    public void setDBAddress (String DBAddress)
    {
        set_Value ("DBAddress", DBAddress);
        
    }
    
    /** Get DB Address.
    @return JDBC URL of the database server */
    public String getDBAddress() 
    {
        return (String)get_Value("DBAddress");
        
    }
    
    /** Set Database Name.
    @param DBInstance Database Name */
    public void setDBInstance (String DBInstance)
    {
        set_Value ("DBInstance", DBInstance);
        
    }
    
    /** Get Database Name.
    @return Database Name */
    public String getDBInstance() 
    {
        return (String)get_Value("DBInstance");
        
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
    
    /** Set Encryption Class.
    @param EncryptionKey Encryption Class used for securing data content */
    public void setEncryptionKey (String EncryptionKey)
    {
        set_ValueNoCheck ("EncryptionKey", EncryptionKey);
        
    }
    
    /** Get Encryption Class.
    @return Encryption Class used for securing data content */
    public String getEncryptionKey() 
    {
        return (String)get_Value("EncryptionKey");
        
    }
    
    /** Set ID Range End.
    @param IDRangeEnd End of the ID Range used */
    public void setIDRangeEnd (java.math.BigDecimal IDRangeEnd)
    {
        set_Value ("IDRangeEnd", IDRangeEnd);
        
    }
    
    /** Get ID Range End.
    @return End of the ID Range used */
    public java.math.BigDecimal getIDRangeEnd() 
    {
        return get_ValueAsBigDecimal("IDRangeEnd");
        
    }
    
    /** Set ID Range Start.
    @param IDRangeStart Start of the ID Range used */
    public void setIDRangeStart (java.math.BigDecimal IDRangeStart)
    {
        set_Value ("IDRangeStart", IDRangeStart);
        
    }
    
    /** Get ID Range Start.
    @return Start of the ID Range used */
    public java.math.BigDecimal getIDRangeStart() 
    {
        return get_ValueAsBigDecimal("IDRangeStart");
        
    }
    
    /** Set Info.
    @param Info Information */
    public void setInfo (String Info)
    {
        set_ValueNoCheck ("Info", Info);
        
    }
    
    /** Get Info.
    @return Information */
    public String getInfo() 
    {
        return (String)get_Value("Info");
        
    }
    
    /** Set Maintain Statistics.
    @param IsAllowStatistics Maintain general statistics */
    public void setIsAllowStatistics (boolean IsAllowStatistics)
    {
        set_Value ("IsAllowStatistics", Boolean.valueOf(IsAllowStatistics));
        
    }
    
    /** Get Maintain Statistics.
    @return Maintain general statistics */
    public boolean isAllowStatistics() 
    {
        return get_ValueAsBoolean("IsAllowStatistics");
        
    }
    
    /** Set Error Reporting.
    @param IsAutoErrorReport Automatically report Errors */
    public void setIsAutoErrorReport (boolean IsAutoErrorReport)
    {
        set_Value ("IsAutoErrorReport", Boolean.valueOf(IsAutoErrorReport));
        
    }
    
    /** Get Error Reporting.
    @return Automatically report Errors */
    public boolean isAutoErrorReport() 
    {
        return get_ValueAsBoolean("IsAutoErrorReport");
        
    }
    
    /** Set Just Migrated.
    @param IsJustMigrated Value set by Migration for post-Migration tasks. */
    public void setIsJustMigrated (boolean IsJustMigrated)
    {
        set_Value ("IsJustMigrated", Boolean.valueOf(IsJustMigrated));
        
    }
    
    /** Get Just Migrated.
    @return Value set by Migration for post-Migration tasks. */
    public boolean isJustMigrated() 
    {
        return get_ValueAsBoolean("IsJustMigrated");
        
    }
    
    /** Set LDAP Domain.
    @param LDAPDomain Directory service domain name - e.g. compiere.org */
    public void setLDAPDomain (String LDAPDomain)
    {
        set_Value ("LDAPDomain", LDAPDomain);
        
    }
    
    /** Get LDAP Domain.
    @return Directory service domain name - e.g. compiere.org */
    public String getLDAPDomain() 
    {
        return (String)get_Value("LDAPDomain");
        
    }
    
    /** Set LDAP URL.
    @param LDAPHost Connection String to LDAP server starting with ldap:// */
    public void setLDAPHost (String LDAPHost)
    {
        set_Value ("LDAPHost", LDAPHost);
        
    }
    
    /** Get LDAP URL.
    @return Connection String to LDAP server starting with ldap:// */
    public String getLDAPHost() 
    {
        return (String)get_Value("LDAPHost");
        
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
    
    /** Set Processors.
    @param NoProcessors Number of Database Processors */
    public void setNoProcessors (int NoProcessors)
    {
        set_Value ("NoProcessors", Integer.valueOf(NoProcessors));
        
    }
    
    /** Get Processors.
    @return Number of Database Processors */
    public int getNoProcessors() 
    {
        return get_ValueAsInt("NoProcessors");
        
    }
    
    /** Set Old Name.
    @param OldName Old Name */
    public void setOldName (String OldName)
    {
        set_ValueNoCheck ("OldName", OldName);
        
    }
    
    /** Get Old Name.
    @return Old Name */
    public String getOldName() 
    {
        return (String)get_Value("OldName");
        
    }
    
    /** Set Password.
    @param Password Password of any length (case sensitive) */
    public void setPassword (String Password)
    {
        if (Password == null) throw new IllegalArgumentException ("Password is mandatory.");
        set_Value ("Password", Password);
        
    }
    
    /** Get Password.
    @return Password of any length (case sensitive) */
    public String getPassword() 
    {
        return (String)get_Value("Password");
        
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
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID <= 0) set_Value ("Record_ID", null);
        else
        set_Value ("Record_ID", Integer.valueOf(Record_ID));
        
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
    
    /** Local = L */
    public static final String REPLICATIONTYPE_Local = X_Ref_AD_Table_Replication_Type.LOCAL.getValue();
    /** Merge = M */
    public static final String REPLICATIONTYPE_Merge = X_Ref_AD_Table_Replication_Type.MERGE.getValue();
    /** Reference = R */
    public static final String REPLICATIONTYPE_Reference = X_Ref_AD_Table_Replication_Type.REFERENCE.getValue();
    /** Set Replication Type.
    @param ReplicationType Type of Data Replication */
    public void setReplicationType (String ReplicationType)
    {
        if (ReplicationType == null) throw new IllegalArgumentException ("ReplicationType is mandatory");
        if (!X_Ref_AD_Table_Replication_Type.isValid(ReplicationType))
        throw new IllegalArgumentException ("ReplicationType Invalid value - " + ReplicationType + " - Reference_ID=126 - L - M - R");
        set_Value ("ReplicationType", ReplicationType);
        
    }
    
    /** Get Replication Type.
    @return Type of Data Replication */
    public String getReplicationType() 
    {
        return (String)get_Value("ReplicationType");
        
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
    
    /** Set Summary.
    @param Summary Textual summary of this request */
    public void setSummary (String Summary)
    {
        set_Value ("Summary", Summary);
        
    }
    
    /** Get Summary.
    @return Textual summary of this request */
    public String getSummary() 
    {
        return (String)get_Value("Summary");
        
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
    
    /** Set Support Expires.
    @param SupportExpDate Date when the Compiere support expires */
    public void setSupportExpDate (Timestamp SupportExpDate)
    {
        set_ValueNoCheck ("SupportExpDate", SupportExpDate);
        
    }
    
    /** Get Support Expires.
    @return Date when the Compiere support expires */
    public Timestamp getSupportExpDate() 
    {
        return (Timestamp)get_Value("SupportExpDate");
        
    }
    
    /** Enterprise = E */
    public static final String SUPPORTLEVEL_Enterprise = X_Ref_SupportLevel.ENTERPRISE.getValue();
    /** Standard = S */
    public static final String SUPPORTLEVEL_Standard = X_Ref_SupportLevel.STANDARD.getValue();
    /** Unsupported = U */
    public static final String SUPPORTLEVEL_Unsupported = X_Ref_SupportLevel.UNSUPPORTED.getValue();
    /** Self-Service = X */
    public static final String SUPPORTLEVEL_Self_Service = X_Ref_SupportLevel.SELF__SERVICE.getValue();
    /** Set Support Level.
    @param SupportLevel Subscribed Support level */
    public void setSupportLevel (String SupportLevel)
    {
        if (!X_Ref_SupportLevel.isValid(SupportLevel))
        throw new IllegalArgumentException ("SupportLevel Invalid value - " + SupportLevel + " - Reference_ID=412 - E - S - U - X");
        set_Value ("SupportLevel", SupportLevel);
        
    }
    
    /** Get Support Level.
    @return Subscribed Support level */
    public String getSupportLevel() 
    {
        return (String)get_Value("SupportLevel");
        
    }
    
    /** Set Support Units.
    @param SupportUnits Number of Support Units, e.g. Supported Internal Users */
    public void setSupportUnits (int SupportUnits)
    {
        set_ValueNoCheck ("SupportUnits", Integer.valueOf(SupportUnits));
        
    }
    
    /** Get Support Units.
    @return Number of Support Units, e.g. Supported Internal Users */
    public int getSupportUnits() 
    {
        return get_ValueAsInt("SupportUnits");
        
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
        set_Value ("UserName", UserName);
        
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
