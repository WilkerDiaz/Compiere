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
/** Generated Model for K_EntryCategory
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_K_EntryCategory.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_K_EntryCategory extends PO
{
    /** Standard Constructor
    @param ctx context
    @param K_EntryCategory_ID id
    @param trx transaction
    */
    public X_K_EntryCategory (Ctx ctx, int K_EntryCategory_ID, Trx trx)
    {
        super (ctx, K_EntryCategory_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (K_EntryCategory_ID == 0)
        {
            setK_CategoryValue_ID (0);
            setK_Category_ID (0);
            setK_Entry_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_K_EntryCategory (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=611 */
    public static final int Table_ID=611;
    
    /** TableName=K_EntryCategory */
    public static final String Table_Name="K_EntryCategory";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Category Value.
    @param K_CategoryValue_ID The value of the category */
    public void setK_CategoryValue_ID (int K_CategoryValue_ID)
    {
        if (K_CategoryValue_ID < 1) throw new IllegalArgumentException ("K_CategoryValue_ID is mandatory.");
        set_Value ("K_CategoryValue_ID", Integer.valueOf(K_CategoryValue_ID));
        
    }
    
    /** Get Category Value.
    @return The value of the category */
    public int getK_CategoryValue_ID() 
    {
        return get_ValueAsInt("K_CategoryValue_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getK_CategoryValue_ID()));
        
    }
    
    /** Set Knowledge Category.
    @param K_Category_ID Knowledge Category */
    public void setK_Category_ID (int K_Category_ID)
    {
        if (K_Category_ID < 1) throw new IllegalArgumentException ("K_Category_ID is mandatory.");
        set_ValueNoCheck ("K_Category_ID", Integer.valueOf(K_Category_ID));
        
    }
    
    /** Get Knowledge Category.
    @return Knowledge Category */
    public int getK_Category_ID() 
    {
        return get_ValueAsInt("K_Category_ID");
        
    }
    
    /** Set Entry.
    @param K_Entry_ID Knowledge Entry */
    public void setK_Entry_ID (int K_Entry_ID)
    {
        if (K_Entry_ID < 1) throw new IllegalArgumentException ("K_Entry_ID is mandatory.");
        set_ValueNoCheck ("K_Entry_ID", Integer.valueOf(K_Entry_ID));
        
    }
    
    /** Get Entry.
    @return Knowledge Entry */
    public int getK_Entry_ID() 
    {
        return get_ValueAsInt("K_Entry_ID");
        
    }
    
    
}
