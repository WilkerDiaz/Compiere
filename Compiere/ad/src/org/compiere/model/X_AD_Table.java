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
/** Generated Model for AD_Table
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_AD_Table.java 8948 2010-06-15 18:42:10Z rthng $ */
public class X_AD_Table extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Table_ID id
    @param trx transaction
    */
    public X_AD_Table (Ctx ctx, int AD_Table_ID, Trx trx)
    {
        super (ctx, AD_Table_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Table_ID == 0)
        {
            setAD_Table_ID (0);
            setAccessLevel (null);	// 4
            setChangeLogLevel (null);	// N
            setEntityType (null);	// U
            setIsDeleteable (true);	// Y
            setIsHighVolume (false);
            setIsReportingTable (false);	// N
            setIsSecurityEnabled (false);
            setIsView (false);	// N
            setName (null);
            setReplicationType (null);	// L
            setTableName (null);
            setTableTrxType (null);	// O
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Table (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27558752301789L;
    /** Last Updated Timestamp 2010-06-15 11:36:25.0 */
    public static final long updatedMS = 1276626985000L;
    /** AD_Table_ID=100 */
    public static final int Table_ID=100;
    
    /** TableName=AD_Table */
    public static final String Table_Name="AD_Table";
    
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
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_ValueNoCheck ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set Dynamic Validation.
    @param AD_Val_Rule_ID Dynamic Validation Rule */
    public void setAD_Val_Rule_ID (int AD_Val_Rule_ID)
    {
        if (AD_Val_Rule_ID <= 0) set_Value ("AD_Val_Rule_ID", null);
        else
        set_Value ("AD_Val_Rule_ID", Integer.valueOf(AD_Val_Rule_ID));
        
    }
    
    /** Get Dynamic Validation.
    @return Dynamic Validation Rule */
    public int getAD_Val_Rule_ID() 
    {
        return get_ValueAsInt("AD_Val_Rule_ID");
        
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
    
    /** Set Base Table.
    @param Base_Table_ID Base Table for Sub-Tables */
    public void setBase_Table_ID (int Base_Table_ID)
    {
        if (Base_Table_ID <= 0) set_Value ("Base_Table_ID", null);
        else
        set_Value ("Base_Table_ID", Integer.valueOf(Base_Table_ID));
        
    }
    
    /** Get Base Table.
    @return Base Table for Sub-Tables */
    public int getBase_Table_ID() 
    {
        return get_ValueAsInt("Base_Table_ID");
        
    }
    
    /** All = A */
    public static final String CHANGELOGLEVEL_All = X_Ref_Change_Log_Level.ALL.getValue();
    /** None = N */
    public static final String CHANGELOGLEVEL_None = X_Ref_Change_Log_Level.NONE.getValue();
    /** Updates and deletes = U */
    public static final String CHANGELOGLEVEL_UpdatesAndDeletes = X_Ref_Change_Log_Level.UPDATES_AND_DELETES.getValue();
    /** Set Set Change Log.
    @param ChangeLogLevel Set what changes, if any, to log */
    public void setChangeLogLevel (String ChangeLogLevel)
    {
        if (ChangeLogLevel == null) throw new IllegalArgumentException ("ChangeLogLevel is mandatory");
        if (!X_Ref_Change_Log_Level.isValid(ChangeLogLevel))
        throw new IllegalArgumentException ("ChangeLogLevel Invalid value - " + ChangeLogLevel + " - Reference_ID=534 - A - N - U");
        set_Value ("ChangeLogLevel", ChangeLogLevel);
        
    }
    
    /** Get Set Change Log.
    @return Set what changes, if any, to log */
    public String getChangeLogLevel() 
    {
        return (String)get_Value("ChangeLogLevel");
        
    }
    
    /** Set Constraint Name.
    @param ConstraintName Constraint Name */
    public void setConstraintName (String ConstraintName)
    {
        throw new IllegalArgumentException ("ConstraintName is virtual column");
        
    }
    
    /** Get Constraint Name.
    @return Constraint Name */
    public String getConstraintName() 
    {
        return (String)get_Value("ConstraintName");
        
    }
    
    /** Set Date Column.
    @param DateColumn_ID Date Column in the table */
    public void setDateColumn_ID (int DateColumn_ID)
    {
        if (DateColumn_ID <= 0) set_ValueNoCheck ("DateColumn_ID", null);
        else
        set_ValueNoCheck ("DateColumn_ID", Integer.valueOf(DateColumn_ID));
        
    }
    
    /** Get Date Column.
    @return Date Column in the table */
    public int getDateColumn_ID() 
    {
        return get_ValueAsInt("DateColumn_ID");
        
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
    
    /** Set Export View.
    @param ExportView Create this view in the database */
    public void setExportView (String ExportView)
    {
        set_Value ("ExportView", ExportView);
        
    }
    
    /** Get Export View.
    @return Create this view in the database */
    public String getExportView() 
    {
        return (String)get_Value("ExportView");
        
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
    
    /** Set Import Table.
    @param ImportTable Import Table Columns from Database */
    public void setImportTable (String ImportTable)
    {
        set_Value ("ImportTable", ImportTable);
        
    }
    
    /** Get Import Table.
    @return Import Table Columns from Database */
    public String getImportTable() 
    {
        return (String)get_Value("ImportTable");
        
    }
    
    /** Set Import View.
    @param ImportView Import this view from a SQL file */
    public void setImportView (String ImportView)
    {
        set_Value ("ImportView", ImportView);
        
    }
    
    /** Get Import View.
    @return Import this view from a SQL file */
    public String getImportView() 
    {
        return (String)get_Value("ImportView");
        
    }
    
    /** Set Records deletable.
    @param IsDeleteable Indicates if records can be deleted from the database */
    public void setIsDeleteable (boolean IsDeleteable)
    {
        set_Value ("IsDeleteable", Boolean.valueOf(IsDeleteable));
        
    }
    
    /** Get Records deletable.
    @return Indicates if records can be deleted from the database */
    public boolean isDeleteable() 
    {
        return get_ValueAsBoolean("IsDeleteable");
        
    }
    
    /** Set High Volume.
    @param IsHighVolume Use Search instead of Pick list */
    public void setIsHighVolume (boolean IsHighVolume)
    {
        set_Value ("IsHighVolume", Boolean.valueOf(IsHighVolume));
        
    }
    
    /** Get High Volume.
    @return Use Search instead of Pick list */
    public boolean isHighVolume() 
    {
        return get_ValueAsBoolean("IsHighVolume");
        
    }
    
    /** Set Reporting Table.
    @param IsReportingTable This is a reporting table */
    public void setIsReportingTable (boolean IsReportingTable)
    {
        set_Value ("IsReportingTable", Boolean.valueOf(IsReportingTable));
        
    }
    
    /** Get Reporting Table.
    @return This is a reporting table */
    public boolean isReportingTable() 
    {
        return get_ValueAsBoolean("IsReportingTable");
        
    }
    
    /** Set Security enabled.
    @param IsSecurityEnabled If security is enabled, user access to data can be restricted via Roles */
    public void setIsSecurityEnabled (boolean IsSecurityEnabled)
    {
        set_Value ("IsSecurityEnabled", Boolean.valueOf(IsSecurityEnabled));
        
    }
    
    /** Get Security enabled.
    @return If security is enabled, user access to data can be restricted via Roles */
    public boolean isSecurityEnabled() 
    {
        return get_ValueAsBoolean("IsSecurityEnabled");
        
    }
    
    /** Set View.
    @param IsView This is a view */
    public void setIsView (boolean IsView)
    {
        set_Value ("IsView", Boolean.valueOf(IsView));
        
    }
    
    /** Get View.
    @return This is a view */
    public boolean isView() 
    {
        return get_ValueAsBoolean("IsView");
        
    }
    
    /** Set Sequence.
    @param LoadSeq Sequence */
    public void setLoadSeq (int LoadSeq)
    {
        set_ValueNoCheck ("LoadSeq", Integer.valueOf(LoadSeq));
        
    }
    
    /** Get Sequence.
    @return Sequence */
    public int getLoadSeq() 
    {
        return get_ValueAsInt("LoadSeq");
        
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
    
    /** Set PO Window.
    @param PO_Window_ID Purchase Order Window */
    public void setPO_Window_ID (int PO_Window_ID)
    {
        if (PO_Window_ID <= 0) set_Value ("PO_Window_ID", null);
        else
        set_Value ("PO_Window_ID", Integer.valueOf(PO_Window_ID));
        
    }
    
    /** Get PO Window.
    @return Purchase Order Window */
    public int getPO_Window_ID() 
    {
        return get_ValueAsInt("PO_Window_ID");
        
    }
    
    /** Set Referenced Table.
    @param Referenced_Table_ID Referenced Table */
    public void setReferenced_Table_ID (int Referenced_Table_ID)
    {
        if (Referenced_Table_ID <= 0) set_Value ("Referenced_Table_ID", null);
        else
        set_Value ("Referenced_Table_ID", Integer.valueOf(Referenced_Table_ID));
        
    }
    
    /** Get Referenced Table.
    @return Referenced Table */
    public int getReferenced_Table_ID() 
    {
        return get_ValueAsInt("Referenced_Table_ID");
        
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
    
    /** History - Daily = D */
    public static final String SUBTABLETYPE_History_Daily = X_Ref_AD_Table_SubTableType.HISTORY__DAILY.getValue();
    /** History - Each = E */
    public static final String SUBTABLETYPE_History_Each = X_Ref_AD_Table_SubTableType.HISTORY__EACH.getValue();
    /** Delta - System = S */
    public static final String SUBTABLETYPE_Delta_System = X_Ref_AD_Table_SubTableType.DELTA__SYSTEM.getValue();
    /** Delta - User = U */
    public static final String SUBTABLETYPE_Delta_User = X_Ref_AD_Table_SubTableType.DELTA__USER.getValue();
    /** Set Sub Table Type.
    @param SubTableType Type of Sub-Table */
    public void setSubTableType (String SubTableType)
    {
        if (!X_Ref_AD_Table_SubTableType.isValid(SubTableType))
        throw new IllegalArgumentException ("SubTableType Invalid value - " + SubTableType + " - Reference_ID=447 - D - E - S - U");
        set_Value ("SubTableType", SubTableType);
        
    }
    
    /** Get Sub Table Type.
    @return Type of Sub-Table */
    public String getSubTableType() 
    {
        return (String)get_Value("SubTableType");
        
    }
    
    /** Set DB Table Name.
    @param TableName Name of the table in the database */
    public void setTableName (String TableName)
    {
        if (TableName == null) throw new IllegalArgumentException ("TableName is mandatory.");
        set_Value ("TableName", TableName);
        
    }
    
    /** Get DB Table Name.
    @return Name of the table in the database */
    public String getTableName() 
    {
        return (String)get_Value("TableName");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getTableName());
        
    }
    
    /** Mandatory Organization = M */
    public static final String TABLETRXTYPE_MandatoryOrganization = X_Ref_AD_Table_Transaction.MANDATORY_ORGANIZATION.getValue();
    /** No Organization = N */
    public static final String TABLETRXTYPE_NoOrganization = X_Ref_AD_Table_Transaction.NO_ORGANIZATION.getValue();
    /** Optional Organization = O */
    public static final String TABLETRXTYPE_OptionalOrganization = X_Ref_AD_Table_Transaction.OPTIONAL_ORGANIZATION.getValue();
    /** Set Transaction Type.
    @param TableTrxType Table Transaction Type */
    public void setTableTrxType (String TableTrxType)
    {
        if (TableTrxType == null) throw new IllegalArgumentException ("TableTrxType is mandatory");
        if (!X_Ref_AD_Table_Transaction.isValid(TableTrxType))
        throw new IllegalArgumentException ("TableTrxType Invalid value - " + TableTrxType + " - Reference_ID=493 - M - N - O");
        set_Value ("TableTrxType", TableTrxType);
        
    }
    
    /** Get Transaction Type.
    @return Table Transaction Type */
    public String getTableTrxType() 
    {
        return (String)get_Value("TableTrxType");
        
    }
    
    
}
