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
/** Generated Model for M_WarehouseTaskMA
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_WarehouseTaskMA.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_WarehouseTaskMA extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WarehouseTaskMA_ID id
    @param trx transaction
    */
    public X_M_WarehouseTaskMA (Ctx ctx, int M_WarehouseTaskMA_ID, Trx trx)
    {
        super (ctx, M_WarehouseTaskMA_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WarehouseTaskMA_ID == 0)
        {
            setM_AttributeSetInstance_ID (0);
            setM_WarehouseTask_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WarehouseTaskMA (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27499734529789L;
    /** Last Updated Timestamp 2008-08-01 09:46:53.0 */
    public static final long updatedMS = 1217609213000L;
    /** AD_Table_ID=1033 */
    public static final int Table_ID=1033;
    
    /** TableName=M_WarehouseTaskMA */
    public static final String Table_Name="M_WarehouseTaskMA";
    
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
    
    /** Set Warehouse Task.
    @param M_WarehouseTask_ID A Warehouse Task represents a basic warehouse operation such as putaway, picking or replenishment. */
    public void setM_WarehouseTask_ID (int M_WarehouseTask_ID)
    {
        if (M_WarehouseTask_ID < 1) throw new IllegalArgumentException ("M_WarehouseTask_ID is mandatory.");
        set_ValueNoCheck ("M_WarehouseTask_ID", Integer.valueOf(M_WarehouseTask_ID));
        
    }
    
    /** Get Warehouse Task.
    @return A Warehouse Task represents a basic warehouse operation such as putaway, picking or replenishment. */
    public int getM_WarehouseTask_ID() 
    {
        return get_ValueAsInt("M_WarehouseTask_ID");
        
    }
    
    /** Set Movement Quantity.
    @param MovementQty Quantity of a product moved. */
    public void setMovementQty (java.math.BigDecimal MovementQty)
    {
        set_Value ("MovementQty", MovementQty);
        
    }
    
    /** Get Movement Quantity.
    @return Quantity of a product moved. */
    public java.math.BigDecimal getMovementQty() 
    {
        return get_ValueAsBigDecimal("MovementQty");
        
    }
    
    /** Set Quantity Dedicated.
    @param QtyDedicated Quantity for which there is a pending Warehouse Task */
    public void setQtyDedicated (java.math.BigDecimal QtyDedicated)
    {
        set_Value ("QtyDedicated", QtyDedicated);
        
    }
    
    /** Get Quantity Dedicated.
    @return Quantity for which there is a pending Warehouse Task */
    public java.math.BigDecimal getQtyDedicated() 
    {
        return get_ValueAsBigDecimal("QtyDedicated");
        
    }
    
    
}
