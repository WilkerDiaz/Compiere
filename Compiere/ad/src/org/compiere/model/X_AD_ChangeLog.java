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
/** Generated Model for AD_ChangeLog
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ChangeLog.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ChangeLog extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ChangeLog_ID id
    @param trx transaction
    */
    public X_AD_ChangeLog (Ctx ctx, int AD_ChangeLog_ID, Trx trx)
    {
        super (ctx, AD_ChangeLog_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ChangeLog_ID == 0)
        {
            setAD_ChangeLog_ID (0);
            setAD_Column_ID (0);
            setAD_Session_ID (0);
            setAD_Table_ID (0);
            setIsCustomization (false);
            setRecord_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ChangeLog (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511698980789L;
    /** Last Updated Timestamp 2008-12-17 20:14:24.0 */
    public static final long updatedMS = 1229573664000L;
    /** AD_Table_ID=580 */
    public static final int Table_ID=580;
    
    /** TableName=AD_ChangeLog */
    public static final String Table_Name="AD_ChangeLog";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Change Log.
    @param AD_ChangeLog_ID Log of data changes */
    public void setAD_ChangeLog_ID (int AD_ChangeLog_ID)
    {
        if (AD_ChangeLog_ID < 1) throw new IllegalArgumentException ("AD_ChangeLog_ID is mandatory.");
        set_ValueNoCheck ("AD_ChangeLog_ID", Integer.valueOf(AD_ChangeLog_ID));
        
    }
    
    /** Get Change Log.
    @return Log of data changes */
    public int getAD_ChangeLog_ID() 
    {
        return get_ValueAsInt("AD_ChangeLog_ID");
        
    }
    
    /** Set Column.
    @param AD_Column_ID Column in the table */
    public void setAD_Column_ID (int AD_Column_ID)
    {
        if (AD_Column_ID < 1) throw new IllegalArgumentException ("AD_Column_ID is mandatory.");
        set_ValueNoCheck ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
    }
    
    /** Set Role.
    @param AD_Role_ID Responsibility Role */
    public void setAD_Role_ID (int AD_Role_ID)
    {
        if (AD_Role_ID <= 0) set_ValueNoCheck ("AD_Role_ID", null);
        else
        set_ValueNoCheck ("AD_Role_ID", Integer.valueOf(AD_Role_ID));
        
    }
    
    /** Get Role.
    @return Responsibility Role */
    public int getAD_Role_ID() 
    {
        return get_ValueAsInt("AD_Role_ID");
        
    }
    
    /** Set Session.
    @param AD_Session_ID User Session Online or Web */
    public void setAD_Session_ID (int AD_Session_ID)
    {
        if (AD_Session_ID < 1) throw new IllegalArgumentException ("AD_Session_ID is mandatory.");
        set_ValueNoCheck ("AD_Session_ID", Integer.valueOf(AD_Session_ID));
        
    }
    
    /** Get Session.
    @return User Session Online or Web */
    public int getAD_Session_ID() 
    {
        return get_ValueAsInt("AD_Session_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Session_ID()));
        
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
    
    /** Delete = D */
    public static final String CHANGELOGTYPE_Delete = X_Ref_AD_ChangeLog_Type.DELETE.getValue();
    /** Insert = I */
    public static final String CHANGELOGTYPE_Insert = X_Ref_AD_ChangeLog_Type.INSERT.getValue();
    /** Update = U */
    public static final String CHANGELOGTYPE_Update = X_Ref_AD_ChangeLog_Type.UPDATE.getValue();
    /** Set Change Log Type.
    @param ChangeLogType Type of change */
    public void setChangeLogType (String ChangeLogType)
    {
        if (!X_Ref_AD_ChangeLog_Type.isValid(ChangeLogType))
        throw new IllegalArgumentException ("ChangeLogType Invalid value - " + ChangeLogType + " - Reference_ID=430 - D - I - U");
        set_ValueNoCheck ("ChangeLogType", ChangeLogType);
        
    }
    
    /** Get Change Log Type.
    @return Type of change */
    public String getChangeLogType() 
    {
        return (String)get_Value("ChangeLogType");
        
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
    
    /** Set Customization.
    @param IsCustomization The change is a customization of the data dictionary and can be applied after Migration */
    public void setIsCustomization (boolean IsCustomization)
    {
        set_Value ("IsCustomization", Boolean.valueOf(IsCustomization));
        
    }
    
    /** Get Customization.
    @return The change is a customization of the data dictionary and can be applied after Migration */
    public boolean isCustomization() 
    {
        return get_ValueAsBoolean("IsCustomization");
        
    }
    
    /** Set New Value.
    @param NewValue New field value */
    public void setNewValue (String NewValue)
    {
        set_ValueNoCheck ("NewValue", NewValue);
        
    }
    
    /** Get New Value.
    @return New field value */
    public String getNewValue() 
    {
        return (String)get_Value("NewValue");
        
    }
    
    /** Set Old Value.
    @param OldValue The old file data */
    public void setOldValue (String OldValue)
    {
        set_ValueNoCheck ("OldValue", OldValue);
        
    }
    
    /** Get Old Value.
    @return The old file data */
    public String getOldValue() 
    {
        return (String)get_Value("OldValue");
        
    }
    
    /** Set Record Key.
    @param Record2_ID Record Key (where clause) */
    public void setRecord2_ID (String Record2_ID)
    {
        set_ValueNoCheck ("Record2_ID", Record2_ID);
        
    }
    
    /** Get Record Key.
    @return Record Key (where clause) */
    public String getRecord2_ID() 
    {
        return (String)get_Value("Record2_ID");
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID < 0) throw new IllegalArgumentException ("Record_ID is mandatory.");
        set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set Redo.
    @param Redo Redo */
    public void setRedo (String Redo)
    {
        set_Value ("Redo", Redo);
        
    }
    
    /** Get Redo.
    @return Redo */
    public String getRedo() 
    {
        return (String)get_Value("Redo");
        
    }
    
    /** Set Transaction.
    @param TrxName Name of the transaction */
    public void setTrxName (String TrxName)
    {
        set_ValueNoCheck ("TrxName", TrxName);
        
    }
    
    /** Get Transaction.
    @return Name of the transaction */
    public String getTrxName() 
    {
        return (String)get_Value("TrxName");
        
    }
    
    /** Set Undo.
    @param Undo Undo */
    public void setUndo (String Undo)
    {
        set_Value ("Undo", Undo);
        
    }
    
    /** Get Undo.
    @return Undo */
    public String getUndo() 
    {
        return (String)get_Value("Undo");
        
    }
    
    
}
