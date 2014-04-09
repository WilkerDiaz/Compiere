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
/** Generated Model for M_DemandLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_DemandLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_DemandLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_DemandLine_ID id
    @param trx transaction
    */
    public X_M_DemandLine (Ctx ctx, int M_DemandLine_ID, Trx trx)
    {
        super (ctx, M_DemandLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_DemandLine_ID == 0)
        {
            setC_Period_ID (0);
            setM_DemandLine_ID (0);
            setM_Demand_ID (0);
            setM_Product_ID (0);
            setQty (Env.ZERO);
            setQtyCalculated (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_DemandLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=719 */
    public static final int Table_ID=719;
    
    /** TableName=M_DemandLine */
    public static final String Table_Name="M_DemandLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Period.
    @param C_Period_ID Period of the Calendar */
    public void setC_Period_ID (int C_Period_ID)
    {
        if (C_Period_ID < 1) throw new IllegalArgumentException ("C_Period_ID is mandatory.");
        set_ValueNoCheck ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
    }
    
    /** Get Period.
    @return Period of the Calendar */
    public int getC_Period_ID() 
    {
        return get_ValueAsInt("C_Period_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Period_ID()));
        
    }
    
    /** Set Demand Line.
    @param M_DemandLine_ID Material Demand Line */
    public void setM_DemandLine_ID (int M_DemandLine_ID)
    {
        if (M_DemandLine_ID < 1) throw new IllegalArgumentException ("M_DemandLine_ID is mandatory.");
        set_ValueNoCheck ("M_DemandLine_ID", Integer.valueOf(M_DemandLine_ID));
        
    }
    
    /** Get Demand Line.
    @return Material Demand Line */
    public int getM_DemandLine_ID() 
    {
        return get_ValueAsInt("M_DemandLine_ID");
        
    }
    
    /** Set Demand.
    @param M_Demand_ID Material Demand */
    public void setM_Demand_ID (int M_Demand_ID)
    {
        if (M_Demand_ID < 1) throw new IllegalArgumentException ("M_Demand_ID is mandatory.");
        set_ValueNoCheck ("M_Demand_ID", Integer.valueOf(M_Demand_ID));
        
    }
    
    /** Get Demand.
    @return Material Demand */
    public int getM_Demand_ID() 
    {
        return get_ValueAsInt("M_Demand_ID");
        
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
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        if (Qty == null) throw new IllegalArgumentException ("Qty is mandatory.");
        set_Value ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    /** Set Calculated Quantity.
    @param QtyCalculated Calculated Quantity */
    public void setQtyCalculated (java.math.BigDecimal QtyCalculated)
    {
        if (QtyCalculated == null) throw new IllegalArgumentException ("QtyCalculated is mandatory.");
        set_Value ("QtyCalculated", QtyCalculated);
        
    }
    
    /** Get Calculated Quantity.
    @return Calculated Quantity */
    public java.math.BigDecimal getQtyCalculated() 
    {
        return get_ValueAsBigDecimal("QtyCalculated");
        
    }
    
    
}
