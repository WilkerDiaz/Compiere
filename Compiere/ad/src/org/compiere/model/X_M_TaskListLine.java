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
/** Generated Model for M_TaskListLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_TaskListLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_TaskListLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_TaskListLine_ID id
    @param trx transaction
    */
    public X_M_TaskListLine (Ctx ctx, int M_TaskListLine_ID, Trx trx)
    {
        super (ctx, M_TaskListLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_TaskListLine_ID == 0)
        {
            setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM M_TaskListLine WHERE M_TaskList_ID=@M_TaskList_ID@
            setM_TaskListLine_ID (0);
            setM_TaskList_ID (0);
            setM_WarehouseTask_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_TaskListLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27502000953789L;
    /** Last Updated Timestamp 2008-08-27 15:20:37.0 */
    public static final long updatedMS = 1219875637000L;
    /** AD_Table_ID=1024 */
    public static final int Table_ID=1024;
    
    /** TableName=M_TaskListLine */
    public static final String Table_Name="M_TaskListLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        throw new IllegalArgumentException ("C_UOM_ID is virtual column");
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
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
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (int Line)
    {
        set_Value ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
    }
    
    /** Set Locator To.
    @param M_LocatorTo_ID Location inventory is moved to */
    public void setM_LocatorTo_ID (int M_LocatorTo_ID)
    {
        throw new IllegalArgumentException ("M_LocatorTo_ID is virtual column");
        
    }
    
    /** Get Locator To.
    @return Location inventory is moved to */
    public int getM_LocatorTo_ID() 
    {
        return get_ValueAsInt("M_LocatorTo_ID");
        
    }
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        throw new IllegalArgumentException ("M_Locator_ID is virtual column");
        
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
        throw new IllegalArgumentException ("M_Product_ID is virtual column");
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Task List Line.
    @param M_TaskListLine_ID Tasks included in the Task List */
    public void setM_TaskListLine_ID (int M_TaskListLine_ID)
    {
        if (M_TaskListLine_ID < 1) throw new IllegalArgumentException ("M_TaskListLine_ID is mandatory.");
        set_ValueNoCheck ("M_TaskListLine_ID", Integer.valueOf(M_TaskListLine_ID));
        
    }
    
    /** Get Task List Line.
    @return Tasks included in the Task List */
    public int getM_TaskListLine_ID() 
    {
        return get_ValueAsInt("M_TaskListLine_ID");
        
    }
    
    /** Set Task List.
    @param M_TaskList_ID List of Warehouse Tasks */
    public void setM_TaskList_ID (int M_TaskList_ID)
    {
        if (M_TaskList_ID < 1) throw new IllegalArgumentException ("M_TaskList_ID is mandatory.");
        set_ValueNoCheck ("M_TaskList_ID", Integer.valueOf(M_TaskList_ID));
        
    }
    
    /** Get Task List.
    @return List of Warehouse Tasks */
    public int getM_TaskList_ID() 
    {
        return get_ValueAsInt("M_TaskList_ID");
        
    }
    
    /** Set Warehouse Task.
    @param M_WarehouseTask_ID A Warehouse Task represents a basic warehouse operation such as putaway, picking or replenishment. */
    public void setM_WarehouseTask_ID (int M_WarehouseTask_ID)
    {
        if (M_WarehouseTask_ID < 1) throw new IllegalArgumentException ("M_WarehouseTask_ID is mandatory.");
        set_Value ("M_WarehouseTask_ID", Integer.valueOf(M_WarehouseTask_ID));
        
    }
    
    /** Get Warehouse Task.
    @return A Warehouse Task represents a basic warehouse operation such as putaway, picking or replenishment. */
    public int getM_WarehouseTask_ID() 
    {
        return get_ValueAsInt("M_WarehouseTask_ID");
        
    }
    
    /** Set Slot No.
    @param SlotNo Slot Number of trolley where to product must be placed in upon picking */
    public void setSlotNo (int SlotNo)
    {
        set_Value ("SlotNo", Integer.valueOf(SlotNo));
        
    }
    
    /** Get Slot No.
    @return Slot Number of trolley where to product must be placed in upon picking */
    public int getSlotNo() 
    {
        return get_ValueAsInt("SlotNo");
        
    }
    
    /** Set StopNo.
    @param StopNo StopNo */
    public void setStopNo (int StopNo)
    {
        set_Value ("StopNo", Integer.valueOf(StopNo));
        
    }
    
    /** Get StopNo.
    @return StopNo */
    public int getStopNo() 
    {
        return get_ValueAsInt("StopNo");
        
    }
    
    /** Set Target Quantity.
    @param TargetQty Target Movement Quantity */
    public void setTargetQty (java.math.BigDecimal TargetQty)
    {
        throw new IllegalArgumentException ("TargetQty is virtual column");
        
    }
    
    /** Get Target Quantity.
    @return Target Movement Quantity */
    public java.math.BigDecimal getTargetQty() 
    {
        return get_ValueAsBigDecimal("TargetQty");
        
    }
    
    
}
