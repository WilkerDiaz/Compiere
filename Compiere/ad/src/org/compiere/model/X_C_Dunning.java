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
/** Generated Model for C_Dunning
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Dunning.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Dunning extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Dunning_ID id
    @param trx transaction
    */
    public X_C_Dunning (Ctx ctx, int C_Dunning_ID, Trx trx)
    {
        super (ctx, C_Dunning_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Dunning_ID == 0)
        {
            setC_Dunning_ID (0);
            setCreateLevelsSequentially (false);
            setIsDefault (false);
            setName (null);
            setSendDunningLetter (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Dunning (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=301 */
    public static final int Table_ID=301;
    
    /** TableName=C_Dunning */
    public static final String Table_Name="C_Dunning";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Dunning.
    @param C_Dunning_ID Dunning Rules for overdue invoices */
    public void setC_Dunning_ID (int C_Dunning_ID)
    {
        if (C_Dunning_ID < 1) throw new IllegalArgumentException ("C_Dunning_ID is mandatory.");
        set_ValueNoCheck ("C_Dunning_ID", Integer.valueOf(C_Dunning_ID));
        
    }
    
    /** Get Dunning.
    @return Dunning Rules for overdue invoices */
    public int getC_Dunning_ID() 
    {
        return get_ValueAsInt("C_Dunning_ID");
        
    }
    
    /** Set Create levels sequentially.
    @param CreateLevelsSequentially Create Dunning Letter by level sequentially */
    public void setCreateLevelsSequentially (boolean CreateLevelsSequentially)
    {
        set_Value ("CreateLevelsSequentially", Boolean.valueOf(CreateLevelsSequentially));
        
    }
    
    /** Get Create levels sequentially.
    @return Create Dunning Letter by level sequentially */
    public boolean isCreateLevelsSequentially() 
    {
        return get_ValueAsBoolean("CreateLevelsSequentially");
        
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
    
    /** Set Send dunning letters.
    @param SendDunningLetter Indicates if dunning letters will be sent */
    public void setSendDunningLetter (boolean SendDunningLetter)
    {
        set_Value ("SendDunningLetter", Boolean.valueOf(SendDunningLetter));
        
    }
    
    /** Get Send dunning letters.
    @return Indicates if dunning letters will be sent */
    public boolean isSendDunningLetter() 
    {
        return get_ValueAsBoolean("SendDunningLetter");
        
    }
    
    
}
