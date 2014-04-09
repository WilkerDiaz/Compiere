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
/** Generated Model for M_AttributeValue
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_AttributeValue.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_AttributeValue extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_AttributeValue_ID id
    @param trx transaction
    */
    public X_M_AttributeValue (Ctx ctx, int M_AttributeValue_ID, Trx trx)
    {
        super (ctx, M_AttributeValue_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_AttributeValue_ID == 0)
        {
            setM_AttributeValue_ID (0);
            setM_Attribute_ID (0);
            setName (null);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_AttributeValue (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=558 */
    public static final int Table_ID=558;
    
    /** TableName=M_AttributeValue */
    public static final String Table_Name="M_AttributeValue";
    
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
    
    /** Set Attribute Value.
    @param M_AttributeValue_ID Product Attribute Value */
    public void setM_AttributeValue_ID (int M_AttributeValue_ID)
    {
        if (M_AttributeValue_ID < 1) throw new IllegalArgumentException ("M_AttributeValue_ID is mandatory.");
        set_ValueNoCheck ("M_AttributeValue_ID", Integer.valueOf(M_AttributeValue_ID));
        
    }
    
    /** Get Attribute Value.
    @return Product Attribute Value */
    public int getM_AttributeValue_ID() 
    {
        return get_ValueAsInt("M_AttributeValue_ID");
        
    }
    
    /** Set Attribute.
    @param M_Attribute_ID Product Attribute */
    public void setM_Attribute_ID (int M_Attribute_ID)
    {
        if (M_Attribute_ID < 1) throw new IllegalArgumentException ("M_Attribute_ID is mandatory.");
        set_ValueNoCheck ("M_Attribute_ID", Integer.valueOf(M_Attribute_ID));
        
    }
    
    /** Get Attribute.
    @return Product Attribute */
    public int getM_Attribute_ID() 
    {
        return get_ValueAsInt("M_Attribute_ID");
        
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
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    
}
