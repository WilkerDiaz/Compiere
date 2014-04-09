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
/** Generated Model for C_LandedCostAllocation
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_LandedCostAllocation.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_LandedCostAllocation extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_LandedCostAllocation_ID id
    @param trx transaction
    */
    public X_C_LandedCostAllocation (Ctx ctx, int C_LandedCostAllocation_ID, Trx trx)
    {
        super (ctx, C_LandedCostAllocation_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_LandedCostAllocation_ID == 0)
        {
            setAmt (Env.ZERO);
            setBase (Env.ZERO);
            setC_InvoiceLine_ID (0);
            setC_LandedCostAllocation_ID (0);
            setM_CostElement_ID (0);
            setM_Product_ID (0);
            setQty (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_LandedCostAllocation (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=760 */
    public static final int Table_ID=760;
    
    /** TableName=C_LandedCostAllocation */
    public static final String Table_Name="C_LandedCostAllocation";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Amount.
    @param Amt Amount */
    public void setAmt (java.math.BigDecimal Amt)
    {
        if (Amt == null) throw new IllegalArgumentException ("Amt is mandatory.");
        set_Value ("Amt", Amt);
        
    }
    
    /** Get Amount.
    @return Amount */
    public java.math.BigDecimal getAmt() 
    {
        return get_ValueAsBigDecimal("Amt");
        
    }
    
    /** Set Base.
    @param Base Calculation Base */
    public void setBase (java.math.BigDecimal Base)
    {
        if (Base == null) throw new IllegalArgumentException ("Base is mandatory.");
        set_Value ("Base", Base);
        
    }
    
    /** Get Base.
    @return Calculation Base */
    public java.math.BigDecimal getBase() 
    {
        return get_ValueAsBigDecimal("Base");
        
    }
    
    /** Set Invoice Line.
    @param C_InvoiceLine_ID Invoice Detail Line */
    public void setC_InvoiceLine_ID (int C_InvoiceLine_ID)
    {
        if (C_InvoiceLine_ID < 1) throw new IllegalArgumentException ("C_InvoiceLine_ID is mandatory.");
        set_ValueNoCheck ("C_InvoiceLine_ID", Integer.valueOf(C_InvoiceLine_ID));
        
    }
    
    /** Get Invoice Line.
    @return Invoice Detail Line */
    public int getC_InvoiceLine_ID() 
    {
        return get_ValueAsInt("C_InvoiceLine_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_InvoiceLine_ID()));
        
    }
    
    /** Set Landed Cost Allocation.
    @param C_LandedCostAllocation_ID Allocation for Land Costs */
    public void setC_LandedCostAllocation_ID (int C_LandedCostAllocation_ID)
    {
        if (C_LandedCostAllocation_ID < 1) throw new IllegalArgumentException ("C_LandedCostAllocation_ID is mandatory.");
        set_ValueNoCheck ("C_LandedCostAllocation_ID", Integer.valueOf(C_LandedCostAllocation_ID));
        
    }
    
    /** Get Landed Cost Allocation.
    @return Allocation for Land Costs */
    public int getC_LandedCostAllocation_ID() 
    {
        return get_ValueAsInt("C_LandedCostAllocation_ID");
        
    }
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID <= 0) set_ValueNoCheck ("M_AttributeSetInstance_ID", null);
        else
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
        set_Value ("M_CostElement_ID", Integer.valueOf(M_CostElement_ID));
        
    }
    
    /** Get Cost Element.
    @return Product Cost Element */
    public int getM_CostElement_ID() 
    {
        return get_ValueAsInt("M_CostElement_ID");
        
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
    
    
}
