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
/** Generated Model for M_Replenish
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_Replenish.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_Replenish extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_Replenish_ID id
    @param trx transaction
    */
    public X_M_Replenish (Ctx ctx, int M_Replenish_ID, Trx trx)
    {
        super (ctx, M_Replenish_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Replenish_ID == 0)
        {
            setLevel_Max (Env.ZERO);
            setLevel_Min (Env.ZERO);
            setM_Product_ID (0);
            setM_Warehouse_ID (0);
            setReplenishType (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_Replenish (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=249 */
    public static final int Table_ID=249;
    
    /** TableName=M_Replenish */
    public static final String Table_Name="M_Replenish";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Maximum Level.
    @param Level_Max Maximum Inventory level for this product */
    public void setLevel_Max (java.math.BigDecimal Level_Max)
    {
        if (Level_Max == null) throw new IllegalArgumentException ("Level_Max is mandatory.");
        set_Value ("Level_Max", Level_Max);
        
    }
    
    /** Get Maximum Level.
    @return Maximum Inventory level for this product */
    public java.math.BigDecimal getLevel_Max() 
    {
        return get_ValueAsBigDecimal("Level_Max");
        
    }
    
    /** Set Minimum Level.
    @param Level_Min Minimum Inventory level for this product */
    public void setLevel_Min (java.math.BigDecimal Level_Min)
    {
        if (Level_Min == null) throw new IllegalArgumentException ("Level_Min is mandatory.");
        set_Value ("Level_Min", Level_Min);
        
    }
    
    /** Get Minimum Level.
    @return Minimum Inventory level for this product */
    public java.math.BigDecimal getLevel_Min() 
    {
        return get_ValueAsBigDecimal("Level_Min");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_Product_ID()));
        
    }
    
    /** Set Source Warehouse.
    @param M_WarehouseSource_ID Optional Warehouse to replenish from */
    public void setM_WarehouseSource_ID (int M_WarehouseSource_ID)
    {
        if (M_WarehouseSource_ID <= 0) set_Value ("M_WarehouseSource_ID", null);
        else
        set_Value ("M_WarehouseSource_ID", Integer.valueOf(M_WarehouseSource_ID));
        
    }
    
    /** Get Source Warehouse.
    @return Optional Warehouse to replenish from */
    public int getM_WarehouseSource_ID() 
    {
        return get_ValueAsInt("M_WarehouseSource_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_ValueNoCheck ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Manual = 0 */
    public static final String REPLENISHTYPE_Manual = X_Ref_M_Replenish_Type.MANUAL.getValue();
    /** Reorder below Minimum Level = 1 */
    public static final String REPLENISHTYPE_ReorderBelowMinimumLevel = X_Ref_M_Replenish_Type.REORDER_BELOW_MINIMUM_LEVEL.getValue();
    /** Maintain Maximum Level = 2 */
    public static final String REPLENISHTYPE_MaintainMaximumLevel = X_Ref_M_Replenish_Type.MAINTAIN_MAXIMUM_LEVEL.getValue();
    /** Custom = 9 */
    public static final String REPLENISHTYPE_Custom = X_Ref_M_Replenish_Type.CUSTOM.getValue();
    /** Set Replenishment Type.
    @param ReplenishType Method for re-ordering a product */
    public void setReplenishType (String ReplenishType)
    {
        if (ReplenishType == null) throw new IllegalArgumentException ("ReplenishType is mandatory");
        if (!X_Ref_M_Replenish_Type.isValid(ReplenishType))
        throw new IllegalArgumentException ("ReplenishType Invalid value - " + ReplenishType + " - Reference_ID=164 - 0 - 1 - 2 - 9");
        set_Value ("ReplenishType", ReplenishType);
        
    }
    
    /** Get Replenishment Type.
    @return Method for re-ordering a product */
    public String getReplenishType() 
    {
        return (String)get_Value("ReplenishType");
        
    }
    
    
}
