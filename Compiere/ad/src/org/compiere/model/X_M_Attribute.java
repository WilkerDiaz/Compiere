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
/** Generated Model for M_Attribute
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_Attribute.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_Attribute extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_Attribute_ID id
    @param trx transaction
    */
    public X_M_Attribute (Ctx ctx, int M_Attribute_ID, Trx trx)
    {
        super (ctx, M_Attribute_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Attribute_ID == 0)
        {
            setAttributeValueType (null);	// S
            setIsInstanceAttribute (false);
            setIsMandatory (false);
            setM_Attribute_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_Attribute (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=562 */
    public static final int Table_ID=562;
    
    /** TableName=M_Attribute */
    public static final String Table_Name="M_Attribute";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** List = L */
    public static final String ATTRIBUTEVALUETYPE_List = X_Ref_M_Attribute_Value_Type.LIST.getValue();
    /** Number = N */
    public static final String ATTRIBUTEVALUETYPE_Number = X_Ref_M_Attribute_Value_Type.NUMBER.getValue();
    /** String (max 40) = S */
    public static final String ATTRIBUTEVALUETYPE_StringMax40 = X_Ref_M_Attribute_Value_Type.STRING_MAX40.getValue();
    /** Set Attribute Value Type.
    @param AttributeValueType Type of Attribute Value */
    public void setAttributeValueType (String AttributeValueType)
    {
        if (AttributeValueType == null) throw new IllegalArgumentException ("AttributeValueType is mandatory");
        if (!X_Ref_M_Attribute_Value_Type.isValid(AttributeValueType))
        throw new IllegalArgumentException ("AttributeValueType Invalid value - " + AttributeValueType + " - Reference_ID=326 - L - N - S");
        set_Value ("AttributeValueType", AttributeValueType);
        
    }
    
    /** Get Attribute Value Type.
    @return Type of Attribute Value */
    public String getAttributeValueType() 
    {
        return (String)get_Value("AttributeValueType");
        
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
    
    /** Set Instance Attribute.
    @param IsInstanceAttribute The product attribute is specific to the instance (like Serial No, Lot or Guarantee Date) */
    public void setIsInstanceAttribute (boolean IsInstanceAttribute)
    {
        set_Value ("IsInstanceAttribute", Boolean.valueOf(IsInstanceAttribute));
        
    }
    
    /** Get Instance Attribute.
    @return The product attribute is specific to the instance (like Serial No, Lot or Guarantee Date) */
    public boolean isInstanceAttribute() 
    {
        return get_ValueAsBoolean("IsInstanceAttribute");
        
    }
    
    /** Set Mandatory.
    @param IsMandatory Data is required in this column */
    public void setIsMandatory (boolean IsMandatory)
    {
        set_Value ("IsMandatory", Boolean.valueOf(IsMandatory));
        
    }
    
    /** Get Mandatory.
    @return Data is required in this column */
    public boolean isMandatory() 
    {
        return get_ValueAsBoolean("IsMandatory");
        
    }
    
    /** Set Attribute Search.
    @param M_AttributeSearch_ID Common Search Attribute */
    public void setM_AttributeSearch_ID (int M_AttributeSearch_ID)
    {
        if (M_AttributeSearch_ID <= 0) set_Value ("M_AttributeSearch_ID", null);
        else
        set_Value ("M_AttributeSearch_ID", Integer.valueOf(M_AttributeSearch_ID));
        
    }
    
    /** Get Attribute Search.
    @return Common Search Attribute */
    public int getM_AttributeSearch_ID() 
    {
        return get_ValueAsInt("M_AttributeSearch_ID");
        
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
    
    
}
