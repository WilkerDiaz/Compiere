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
/** Generated Model for B_TopicCategory
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_B_TopicCategory.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_B_TopicCategory extends PO
{
    /** Standard Constructor
    @param ctx context
    @param B_TopicCategory_ID id
    @param trx transaction
    */
    public X_B_TopicCategory (Ctx ctx, int B_TopicCategory_ID, Trx trx)
    {
        super (ctx, B_TopicCategory_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (B_TopicCategory_ID == 0)
        {
            setB_TopicCategory_ID (0);
            setB_TopicType_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_B_TopicCategory (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=691 */
    public static final int Table_ID=691;
    
    /** TableName=B_TopicCategory */
    public static final String Table_Name="B_TopicCategory";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Topic Category.
    @param B_TopicCategory_ID Auction Topic Category */
    public void setB_TopicCategory_ID (int B_TopicCategory_ID)
    {
        if (B_TopicCategory_ID < 1) throw new IllegalArgumentException ("B_TopicCategory_ID is mandatory.");
        set_ValueNoCheck ("B_TopicCategory_ID", Integer.valueOf(B_TopicCategory_ID));
        
    }
    
    /** Get Topic Category.
    @return Auction Topic Category */
    public int getB_TopicCategory_ID() 
    {
        return get_ValueAsInt("B_TopicCategory_ID");
        
    }
    
    /** Set Topic Type.
    @param B_TopicType_ID Auction Topic Type */
    public void setB_TopicType_ID (int B_TopicType_ID)
    {
        if (B_TopicType_ID < 1) throw new IllegalArgumentException ("B_TopicType_ID is mandatory.");
        set_ValueNoCheck ("B_TopicType_ID", Integer.valueOf(B_TopicType_ID));
        
    }
    
    /** Get Topic Type.
    @return Auction Topic Type */
    public int getB_TopicType_ID() 
    {
        return get_ValueAsInt("B_TopicType_ID");
        
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
    
    
}
