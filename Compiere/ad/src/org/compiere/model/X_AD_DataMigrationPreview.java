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
/** Generated Model for AD_DataMigrationPreview
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_DataMigrationPreview.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_DataMigrationPreview extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_DataMigrationPreview_ID id
    @param trx transaction
    */
    public X_AD_DataMigrationPreview (Ctx ctx, int AD_DataMigrationPreview_ID, Trx trx)
    {
        super (ctx, AD_DataMigrationPreview_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_DataMigrationPreview_ID == 0)
        {
            setAD_DataMigrationPreview_ID (0);
            setAD_DataMigration_ID (0);
            setAD_Table_ID (0);
            setRecord_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_DataMigrationPreview (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27509071894789L;
    /** Last Updated Timestamp 2008-11-17 10:29:38.0 */
    public static final long updatedMS = 1226946578000L;
    /** AD_Table_ID=1009 */
    public static final int Table_ID=1009;
    
    /** TableName=AD_DataMigrationPreview */
    public static final String Table_Name="AD_DataMigrationPreview";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set AD_DataMigrationPreview.
    @param AD_DataMigrationPreview_ID Data Migration Record Preview */
    public void setAD_DataMigrationPreview_ID (int AD_DataMigrationPreview_ID)
    {
        if (AD_DataMigrationPreview_ID < 1) throw new IllegalArgumentException ("AD_DataMigrationPreview_ID is mandatory.");
        set_ValueNoCheck ("AD_DataMigrationPreview_ID", Integer.valueOf(AD_DataMigrationPreview_ID));
        
    }
    
    /** Get AD_DataMigrationPreview.
    @return Data Migration Record Preview */
    public int getAD_DataMigrationPreview_ID() 
    {
        return get_ValueAsInt("AD_DataMigrationPreview_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_DataMigration_ID()));
        
    }
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
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
    
    /** Set Info.
    @param Info Information */
    public void setInfo (String Info)
    {
        set_Value ("Info", Info);
        
    }
    
    /** Get Info.
    @return Information */
    public String getInfo() 
    {
        return (String)get_Value("Info");
        
    }
    
    /** Set Level no.
    @param LevelNo Level Number */
    public void setLevelNo (int LevelNo)
    {
        set_ValueNoCheck ("LevelNo", Integer.valueOf(LevelNo));
        
    }
    
    /** Get Level no.
    @return Level Number */
    public int getLevelNo() 
    {
        return get_ValueAsInt("LevelNo");
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID < 0) throw new IllegalArgumentException ("Record_ID is mandatory.");
        set_Value ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set TableUID.
    @param TableUID Unique Table ID */
    public void setTableUID (String TableUID)
    {
        set_ValueNoCheck ("TableUID", TableUID);
        
    }
    
    /** Get TableUID.
    @return Unique Table ID */
    public String getTableUID() 
    {
        return (String)get_Value("TableUID");
        
    }
    
    
}
