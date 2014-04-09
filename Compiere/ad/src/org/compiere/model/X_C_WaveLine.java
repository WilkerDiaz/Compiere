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
/** Generated Model for C_WaveLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_WaveLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_WaveLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_WaveLine_ID id
    @param trx transaction
    */
    public X_C_WaveLine (Ctx ctx, int C_WaveLine_ID, Trx trx)
    {
        super (ctx, C_WaveLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_WaveLine_ID == 0)
        {
            setC_WaveLine_ID (0);
            setC_Wave_ID (0);
            setMovementQty (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_WaveLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27541775265789L;
    /** Last Updated Timestamp 2009-11-30 22:45:49.0 */
    public static final long updatedMS = 1259649949000L;
    /** AD_Table_ID=1020 */
    public static final int Table_ID=1020;
    
    /** TableName=C_WaveLine */
    public static final String Table_Name="C_WaveLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Order Line.
    @param C_OrderLine_ID Order Line */
    public void setC_OrderLine_ID (int C_OrderLine_ID)
    {
        if (C_OrderLine_ID <= 0) set_Value ("C_OrderLine_ID", null);
        else
        set_Value ("C_OrderLine_ID", Integer.valueOf(C_OrderLine_ID));
        
    }
    
    /** Get Order Line.
    @return Order Line */
    public int getC_OrderLine_ID() 
    {
        return get_ValueAsInt("C_OrderLine_ID");
        
    }
    
    /** Set Wave Line.
    @param C_WaveLine_ID Selected order lines for which there is sufficient onhand quantity in the warehouse */
    public void setC_WaveLine_ID (int C_WaveLine_ID)
    {
        if (C_WaveLine_ID < 1) throw new IllegalArgumentException ("C_WaveLine_ID is mandatory.");
        set_ValueNoCheck ("C_WaveLine_ID", Integer.valueOf(C_WaveLine_ID));
        
    }
    
    /** Get Wave Line.
    @return Selected order lines for which there is sufficient onhand quantity in the warehouse */
    public int getC_WaveLine_ID() 
    {
        return get_ValueAsInt("C_WaveLine_ID");
        
    }
    
    /** Set Wave.
    @param C_Wave_ID Group of selected order lines for which there is sufficient onhand quantity in the warehouse */
    public void setC_Wave_ID (int C_Wave_ID)
    {
        if (C_Wave_ID < 1) throw new IllegalArgumentException ("C_Wave_ID is mandatory.");
        set_ValueNoCheck ("C_Wave_ID", Integer.valueOf(C_Wave_ID));
        
    }
    
    /** Get Wave.
    @return Group of selected order lines for which there is sufficient onhand quantity in the warehouse */
    public int getC_Wave_ID() 
    {
        return get_ValueAsInt("C_Wave_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Wave_ID()));
        
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
    
    /** Set Work Order Component.
    @param M_WorkOrderComponent_ID Work Order Component */
    public void setM_WorkOrderComponent_ID (int M_WorkOrderComponent_ID)
    {
        if (M_WorkOrderComponent_ID <= 0) set_Value ("M_WorkOrderComponent_ID", null);
        else
        set_Value ("M_WorkOrderComponent_ID", Integer.valueOf(M_WorkOrderComponent_ID));
        
    }
    
    /** Get Work Order Component.
    @return Work Order Component */
    public int getM_WorkOrderComponent_ID() 
    {
        return get_ValueAsInt("M_WorkOrderComponent_ID");
        
    }
    
    /** Set Movement Quantity.
    @param MovementQty Quantity of a product moved. */
    public void setMovementQty (java.math.BigDecimal MovementQty)
    {
        if (MovementQty == null) throw new IllegalArgumentException ("MovementQty is mandatory.");
        set_Value ("MovementQty", MovementQty);
        
    }
    
    /** Get Movement Quantity.
    @return Quantity of a product moved. */
    public java.math.BigDecimal getMovementQty() 
    {
        return get_ValueAsBigDecimal("MovementQty");
        
    }
    
    
}
