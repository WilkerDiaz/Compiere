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
/** Generated Model for M_CycleCountLock
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.2 Dev - $Id: GenerateModel.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_CycleCountLock extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_CycleCountLock_ID id
    @param trx transaction
    */
    public X_M_CycleCountLock (Ctx ctx, int M_CycleCountLock_ID, Trx trx)
    {
        super (ctx, M_CycleCountLock_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_CycleCountLock_ID == 0)
        {
            setIsLocked (false);
            setM_CycleCountLock_ID (0);
            setM_Inventory_ID (0);
            setM_Locator_ID (0);
            setM_Product_ID (0);
            setM_Warehouse_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_CycleCountLock (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27545363576789L;
    /** Last Updated Timestamp 2010-01-12 01:01:00.0 */
    public static final long updatedMS = 1263238260000L;
    /** AD_Table_ID=2153 */
    public static final int Table_ID=2153;
    
    /** TableName=M_CycleCountLock */
    public static final String Table_Name="M_CycleCountLock";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Lock.
    @param IsLocked Lock */
    public void setIsLocked (boolean IsLocked)
    {
        set_Value ("IsLocked", Boolean.valueOf(IsLocked));
        
    }
    
    /** Get Lock.
    @return Lock */
    public boolean isLocked() 
    {
        return get_ValueAsBoolean("IsLocked");
        
    }
    
    /** Set Cycle Count Lock.
    @param M_CycleCountLock_ID Cycle Count Lock */
    public void setM_CycleCountLock_ID (int M_CycleCountLock_ID)
    {
        if (M_CycleCountLock_ID < 1) throw new IllegalArgumentException ("M_CycleCountLock_ID is mandatory.");
        set_ValueNoCheck ("M_CycleCountLock_ID", Integer.valueOf(M_CycleCountLock_ID));
        
    }
    
    /** Get Cycle Count Lock.
    @return Cycle Count Lock */
    public int getM_CycleCountLock_ID() 
    {
        return get_ValueAsInt("M_CycleCountLock_ID");
        
    }
    
    /** Set Phys.Inventory.
    @param M_Inventory_ID Parameters for a Physical Inventory */
    public void setM_Inventory_ID (int M_Inventory_ID)
    {
        if (M_Inventory_ID < 1) throw new IllegalArgumentException ("M_Inventory_ID is mandatory.");
        set_Value ("M_Inventory_ID", Integer.valueOf(M_Inventory_ID));
        
    }
    
    /** Get Phys.Inventory.
    @return Parameters for a Physical Inventory */
    public int getM_Inventory_ID() 
    {
        return get_ValueAsInt("M_Inventory_ID");
        
    }
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID < 1) throw new IllegalArgumentException ("M_Locator_ID is mandatory.");
        set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    
}
