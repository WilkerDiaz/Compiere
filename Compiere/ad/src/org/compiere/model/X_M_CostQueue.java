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
/** Generated Model for M_CostQueue
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_CostQueue.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_CostQueue extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_CostQueue_ID id
    @param trx transaction
    */
    public X_M_CostQueue (Ctx ctx, int M_CostQueue_ID, Trx trx)
    {
        super (ctx, M_CostQueue_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_CostQueue_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setCurrentCostPrice (Env.ZERO);
            setCurrentQty (Env.ZERO);
            setM_AttributeSetInstance_ID (0);
            setM_CostElement_ID (0);
            setM_CostQueue_ID (0);
            setM_CostType_ID (0);
            setM_Product_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_CostQueue (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=817 */
    public static final int Table_ID=817;
    
    /** TableName=M_CostQueue */
    public static final String Table_Name="M_CostQueue";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
        set_ValueNoCheck ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
    }
    
    /** Set Current Cost.
    @param CurrentCostPrice The currently used cost */
    public void setCurrentCostPrice (java.math.BigDecimal CurrentCostPrice)
    {
        if (CurrentCostPrice == null) throw new IllegalArgumentException ("CurrentCostPrice is mandatory.");
        set_Value ("CurrentCostPrice", CurrentCostPrice);
        
    }
    
    /** Get Current Cost.
    @return The currently used cost */
    public java.math.BigDecimal getCurrentCostPrice() 
    {
        return get_ValueAsBigDecimal("CurrentCostPrice");
        
    }
    
    /** Set Onhand Quantity.
    @param CurrentQty Onhand Quantity */
    public void setCurrentQty (java.math.BigDecimal CurrentQty)
    {
        if (CurrentQty == null) throw new IllegalArgumentException ("CurrentQty is mandatory.");
        set_Value ("CurrentQty", CurrentQty);
        
    }
    
    /** Get Onhand Quantity.
    @return Onhand Quantity */
    public java.math.BigDecimal getCurrentQty() 
    {
        return get_ValueAsBigDecimal("CurrentQty");
        
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
    
    /** Set Cost Element.
    @param M_CostElement_ID Product Cost Element */
    public void setM_CostElement_ID (int M_CostElement_ID)
    {
        if (M_CostElement_ID < 1) throw new IllegalArgumentException ("M_CostElement_ID is mandatory.");
        set_ValueNoCheck ("M_CostElement_ID", Integer.valueOf(M_CostElement_ID));
        
    }
    
    /** Get Cost Element.
    @return Product Cost Element */
    public int getM_CostElement_ID() 
    {
        return get_ValueAsInt("M_CostElement_ID");
        
    }
    
    /** Set Cost Queue.
    @param M_CostQueue_ID FiFo/LiFo Cost Queue */
    public void setM_CostQueue_ID (int M_CostQueue_ID)
    {
        if (M_CostQueue_ID < 1) throw new IllegalArgumentException ("M_CostQueue_ID is mandatory.");
        set_ValueNoCheck ("M_CostQueue_ID", Integer.valueOf(M_CostQueue_ID));
        
    }
    
    /** Get Cost Queue.
    @return FiFo/LiFo Cost Queue */
    public int getM_CostQueue_ID() 
    {
        return get_ValueAsInt("M_CostQueue_ID");
        
    }
    
    /** Set Cost Type.
    @param M_CostType_ID Type of Cost (e.g. Current, Plan, Future) */
    public void setM_CostType_ID (int M_CostType_ID)
    {
        if (M_CostType_ID < 1) throw new IllegalArgumentException ("M_CostType_ID is mandatory.");
        set_ValueNoCheck ("M_CostType_ID", Integer.valueOf(M_CostType_ID));
        
    }
    
    /** Get Cost Type.
    @return Type of Cost (e.g. Current, Plan, Future) */
    public int getM_CostType_ID() 
    {
        return get_ValueAsInt("M_CostType_ID");
        
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
    
    
}
