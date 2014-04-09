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
/** Generated Model for T_WarehouseStock
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_WarehouseStock.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_WarehouseStock extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_WarehouseStock_ID id
    @param trx transaction
    */
    public X_T_WarehouseStock (Ctx ctx, int T_WarehouseStock_ID, Trx trx)
    {
        super (ctx, T_WarehouseStock_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_WarehouseStock_ID == 0)
        {
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_WarehouseStock (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511671720789L;
    /** Last Updated Timestamp 2008-12-17 12:40:04.0 */
    public static final long updatedMS = 1229546404000L;
    /** AD_Table_ID=1078 */
    public static final int Table_ID=1078;
    
    /** TableName=T_WarehouseStock */
    public static final String Table_Name="T_WarehouseStock";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Instance.
    @param AD_PInstance_ID Instance of the process */
    public void setAD_PInstance_ID (int AD_PInstance_ID)
    {
        if (AD_PInstance_ID <= 0) set_Value ("AD_PInstance_ID", null);
        else
        set_Value ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID <= 0) set_Value ("C_UOM_ID", null);
        else
        set_Value ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Consumed Past Seven Days.
    @param ConsumedPastSevenDays Consumed Past Seven Days */
    public void setConsumedPastSevenDays (java.math.BigDecimal ConsumedPastSevenDays)
    {
        set_Value ("ConsumedPastSevenDays", ConsumedPastSevenDays);
        
    }
    
    /** Get Consumed Past Seven Days.
    @return Consumed Past Seven Days */
    public java.math.BigDecimal getConsumedPastSevenDays() 
    {
        return get_ValueAsBigDecimal("ConsumedPastSevenDays");
        
    }
    
    /** Set Consumed Past Thirty Days.
    @param ConsumedPastThirtyDays Consumed Past Thirty Days */
    public void setConsumedPastThirtyDays (java.math.BigDecimal ConsumedPastThirtyDays)
    {
        set_Value ("ConsumedPastThirtyDays", ConsumedPastThirtyDays);
        
    }
    
    /** Get Consumed Past Thirty Days.
    @return Consumed Past Thirty Days */
    public java.math.BigDecimal getConsumedPastThirtyDays() 
    {
        return get_ValueAsBigDecimal("ConsumedPastThirtyDays");
        
    }
    
    /** Set Days Cover.
    @param DaysCover Estimated number of days current available quantity can fulfill Warehouse orders. */
    public void setDaysCover (java.math.BigDecimal DaysCover)
    {
        set_Value ("DaysCover", DaysCover);
        
    }
    
    /** Get Days Cover.
    @return Estimated number of days current available quantity can fulfill Warehouse orders. */
    public java.math.BigDecimal getDaysCover() 
    {
        return get_ValueAsBigDecimal("DaysCover");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
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
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Quantity Allocated.
    @param QtyAllocated Quantity that has been picked and is awaiting shipment */
    public void setQtyAllocated (java.math.BigDecimal QtyAllocated)
    {
        set_Value ("QtyAllocated", QtyAllocated);
        
    }
    
    /** Get Quantity Allocated.
    @return Quantity that has been picked and is awaiting shipment */
    public java.math.BigDecimal getQtyAllocated() 
    {
        return get_ValueAsBigDecimal("QtyAllocated");
        
    }
    
    /** Set Available Quantity.
    @param QtyAvailable Available Quantity (On Hand - Reserved) */
    public void setQtyAvailable (java.math.BigDecimal QtyAvailable)
    {
        set_Value ("QtyAvailable", QtyAvailable);
        
    }
    
    /** Get Available Quantity.
    @return Available Quantity (On Hand - Reserved) */
    public java.math.BigDecimal getQtyAvailable() 
    {
        return get_ValueAsBigDecimal("QtyAvailable");
        
    }
    
    /** Set On Hand Quantity.
    @param QtyOnHand On Hand Quantity */
    public void setQtyOnHand (java.math.BigDecimal QtyOnHand)
    {
        set_Value ("QtyOnHand", QtyOnHand);
        
    }
    
    /** Get On Hand Quantity.
    @return On Hand Quantity */
    public java.math.BigDecimal getQtyOnHand() 
    {
        return get_ValueAsBigDecimal("QtyOnHand");
        
    }
    
    
}
