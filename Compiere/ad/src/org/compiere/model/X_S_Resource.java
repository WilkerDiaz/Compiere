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
/** Generated Model for S_Resource
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_S_Resource.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_S_Resource extends PO
{
    /** Standard Constructor
    @param ctx context
    @param S_Resource_ID id
    @param trx transaction
    */
    public X_S_Resource (Ctx ctx, int S_Resource_ID, Trx trx)
    {
        super (ctx, S_Resource_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (S_Resource_ID == 0)
        {
            setIsAvailable (true);	// Y
            setM_Warehouse_ID (0);
            setName (null);
            setS_ResourceType_ID (0);
            setS_Resource_ID (0);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_S_Resource (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518321840789L;
    /** Last Updated Timestamp 2009-03-04 11:55:24.0 */
    public static final long updatedMS = 1236196524000L;
    /** AD_Table_ID=487 */
    public static final int Table_ID=487;
    
    /** TableName=S_Resource */
    public static final String Table_Name="S_Resource";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Chargeable Quantity.
    @param ChargeableQty Chargeable Quantity */
    public void setChargeableQty (java.math.BigDecimal ChargeableQty)
    {
        set_Value ("ChargeableQty", ChargeableQty);
        
    }
    
    /** Get Chargeable Quantity.
    @return Chargeable Quantity */
    public java.math.BigDecimal getChargeableQty() 
    {
        return get_ValueAsBigDecimal("ChargeableQty");
        
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
    
    /** Set Available.
    @param IsAvailable Resource is available */
    public void setIsAvailable (boolean IsAvailable)
    {
        set_Value ("IsAvailable", Boolean.valueOf(IsAvailable));
        
    }
    
    /** Get Available.
    @return Resource is available */
    public boolean isAvailable() 
    {
        return get_ValueAsBoolean("IsAvailable");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
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
    
    /** Equipment = E */
    public static final String RESOURCEGROUP_Equipment = X_Ref_M_Product_Resource_Group.EQUIPMENT.getValue();
    /** Other = O */
    public static final String RESOURCEGROUP_Other = X_Ref_M_Product_Resource_Group.OTHER.getValue();
    /** Person = P */
    public static final String RESOURCEGROUP_Person = X_Ref_M_Product_Resource_Group.PERSON.getValue();
    /** Set Resource Group.
    @param ResourceGroup First level grouping of resources into Person, Equipment or Other. */
    public void setResourceGroup (String ResourceGroup)
    {
        if (!X_Ref_M_Product_Resource_Group.isValid(ResourceGroup))
        throw new IllegalArgumentException ("ResourceGroup Invalid value - " + ResourceGroup + " - Reference_ID=498 - E - O - P");
        throw new IllegalArgumentException ("ResourceGroup is virtual column");
        
    }
    
    /** Get Resource Group.
    @return First level grouping of resources into Person, Equipment or Other. */
    public String getResourceGroup() 
    {
        return (String)get_Value("ResourceGroup");
        
    }
    
    /** Set Resource Type.
    @param S_ResourceType_ID Resource Type */
    public void setS_ResourceType_ID (int S_ResourceType_ID)
    {
        if (S_ResourceType_ID < 1) throw new IllegalArgumentException ("S_ResourceType_ID is mandatory.");
        set_Value ("S_ResourceType_ID", Integer.valueOf(S_ResourceType_ID));
        
    }
    
    /** Get Resource Type.
    @return Resource Type */
    public int getS_ResourceType_ID() 
    {
        return get_ValueAsInt("S_ResourceType_ID");
        
    }
    
    /** Set Resource.
    @param S_Resource_ID Resource */
    public void setS_Resource_ID (int S_Resource_ID)
    {
        if (S_Resource_ID < 1) throw new IllegalArgumentException ("S_Resource_ID is mandatory.");
        set_ValueNoCheck ("S_Resource_ID", Integer.valueOf(S_Resource_ID));
        
    }
    
    /** Get Resource.
    @return Resource */
    public int getS_Resource_ID() 
    {
        return get_ValueAsInt("S_Resource_ID");
        
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
