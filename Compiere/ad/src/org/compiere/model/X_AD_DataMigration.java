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
/** Generated Model for AD_DataMigration
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_DataMigration.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_DataMigration extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_DataMigration_ID id
    @param trx transaction
    */
    public X_AD_DataMigration (Ctx ctx, int AD_DataMigration_ID, Trx trx)
    {
        super (ctx, AD_DataMigration_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_DataMigration_ID == 0)
        {
            setAD_DataMigration_ID (0);
            setDataMigrationType (null);	// S
            setEntityType (null);	// U
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_DataMigration (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27499062607789L;
    /** Last Updated Timestamp 2008-07-24 15:08:11.0 */
    public static final long updatedMS = 1216937291000L;
    /** AD_Table_ID=1007 */
    public static final int Table_ID=1007;
    
    /** TableName=AD_DataMigration */
    public static final String Table_Name="AD_DataMigration";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Include Tenant.
    @param AD_ClientInclude_ID Include Tenant Data */
    public void setAD_ClientInclude_ID (int AD_ClientInclude_ID)
    {
        if (AD_ClientInclude_ID <= 0) set_Value ("AD_ClientInclude_ID", null);
        else
        set_Value ("AD_ClientInclude_ID", Integer.valueOf(AD_ClientInclude_ID));
        
    }
    
    /** Get Include Tenant.
    @return Include Tenant Data */
    public int getAD_ClientInclude_ID() 
    {
        return get_ValueAsInt("AD_ClientInclude_ID");
        
    }
    
    /** Set Data Migration.
    @param AD_DataMigration_ID Data Migration Definition */
    public void setAD_DataMigration_ID (int AD_DataMigration_ID)
    {
        if (AD_DataMigration_ID < 1) throw new IllegalArgumentException ("AD_DataMigration_ID is mandatory.");
        set_ValueNoCheck ("AD_DataMigration_ID", Integer.valueOf(AD_DataMigration_ID));
        
    }
    
    /** Get Data Migration.
    @return Data Migration Definition */
    public int getAD_DataMigration_ID() 
    {
        return get_ValueAsInt("AD_DataMigration_ID");
        
    }
    
    /** System and Tenant = A */
    public static final String DATAMIGRATIONTYPE_SystemAndTenant = X_Ref_AD_DataMigration_Type.SYSTEM_AND_TENANT.getValue();
    /** Tenant Only = C */
    public static final String DATAMIGRATIONTYPE_TenantOnly = X_Ref_AD_DataMigration_Type.TENANT_ONLY.getValue();
    /** System Only = S */
    public static final String DATAMIGRATIONTYPE_SystemOnly = X_Ref_AD_DataMigration_Type.SYSTEM_ONLY.getValue();
    /** Set Data Migration Type.
    @param DataMigrationType Data Migration Type */
    public void setDataMigrationType (String DataMigrationType)
    {
        if (DataMigrationType == null) throw new IllegalArgumentException ("DataMigrationType is mandatory");
        if (!X_Ref_AD_DataMigration_Type.isValid(DataMigrationType))
        throw new IllegalArgumentException ("DataMigrationType Invalid value - " + DataMigrationType + " - Reference_ID=439 - A - C - S");
        set_Value ("DataMigrationType", DataMigrationType);
        
    }
    
    /** Get Data Migration Type.
    @return Data Migration Type */
    public String getDataMigrationType() 
    {
        return (String)get_Value("DataMigrationType");
        
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
    
    
}
