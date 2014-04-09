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
/** Generated Model for M_AttributeInstance
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_AttributeInstance.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_AttributeInstance extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_AttributeInstance_ID id
    @param trx transaction
    */
    public X_M_AttributeInstance (Ctx ctx, int M_AttributeInstance_ID, Trx trx)
    {
        super (ctx, M_AttributeInstance_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_AttributeInstance_ID == 0)
        {
            setM_AttributeSetInstance_ID (0);
            setM_Attribute_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_AttributeInstance (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=561 */
    public static final int Table_ID=561;
    
    /** TableName=M_AttributeInstance */
    public static final String Table_Name="M_AttributeInstance";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID < 0) throw new IllegalArgumentException ("M_AttributeSetInstance_ID is mandatory.");
        set_ValueNoCheck ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get Attribute Set Instance.
    @return Product Attribute Set Instance */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
    }
    
    /** Set Attribute Value.
    @param M_AttributeValue_ID Product Attribute Value */
    public void setM_AttributeValue_ID (int M_AttributeValue_ID)
    {
        if (M_AttributeValue_ID <= 0) set_Value ("M_AttributeValue_ID", null);
        else
        set_Value ("M_AttributeValue_ID", Integer.valueOf(M_AttributeValue_ID));
        
    }
    
    /** Get Attribute Value.
    @return Product Attribute Value */
    public int getM_AttributeValue_ID() 
    {
        return get_ValueAsInt("M_AttributeValue_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_AttributeValue_ID()));
        
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
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Value.
    @param ValueNumber Numeric Value */
    public void setValueNumber (java.math.BigDecimal ValueNumber)
    {
        set_Value ("ValueNumber", ValueNumber);
        
    }
    
    /** Get Value.
    @return Numeric Value */
    public java.math.BigDecimal getValueNumber() 
    {
        return get_ValueAsBigDecimal("ValueNumber");
        
    }
    
    
}
