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
/** Generated Model for M_Product_Costing
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_Product_Costing.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_Product_Costing extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_Product_Costing_ID id
    @param trx transaction
    */
    public X_M_Product_Costing (Ctx ctx, int M_Product_Costing_ID, Trx trx)
    {
        super (ctx, M_Product_Costing_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Product_Costing_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setCostAverage (Env.ZERO);
            setCostAverageCumAmt (Env.ZERO);
            setCostAverageCumQty (Env.ZERO);
            setCostStandard (Env.ZERO);
            setCostStandardCumAmt (Env.ZERO);
            setCostStandardCumQty (Env.ZERO);
            setCostStandardPOAmt (Env.ZERO);
            setCostStandardPOQty (Env.ZERO);
            setCurrentCostPrice (Env.ZERO);
            setFutureCostPrice (Env.ZERO);
            setM_Product_ID (0);
            setPriceLastInv (Env.ZERO);
            setPriceLastPO (Env.ZERO);
            setTotalInvAmt (Env.ZERO);
            setTotalInvQty (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_Product_Costing (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=327 */
    public static final int Table_ID=327;
    
    /** TableName=M_Product_Costing */
    public static final String Table_Name="M_Product_Costing";
    
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_AcctSchema_ID()));
        
    }
    
    /** Set Average Cost.
    @param CostAverage Weighted average costs */
    public void setCostAverage (java.math.BigDecimal CostAverage)
    {
        if (CostAverage == null) throw new IllegalArgumentException ("CostAverage is mandatory.");
        set_ValueNoCheck ("CostAverage", CostAverage);
        
    }
    
    /** Get Average Cost.
    @return Weighted average costs */
    public java.math.BigDecimal getCostAverage() 
    {
        return get_ValueAsBigDecimal("CostAverage");
        
    }
    
    /** Set Average Cost Amount Sum.
    @param CostAverageCumAmt Cumulative average cost amounts (internal) */
    public void setCostAverageCumAmt (java.math.BigDecimal CostAverageCumAmt)
    {
        if (CostAverageCumAmt == null) throw new IllegalArgumentException ("CostAverageCumAmt is mandatory.");
        set_ValueNoCheck ("CostAverageCumAmt", CostAverageCumAmt);
        
    }
    
    /** Get Average Cost Amount Sum.
    @return Cumulative average cost amounts (internal) */
    public java.math.BigDecimal getCostAverageCumAmt() 
    {
        return get_ValueAsBigDecimal("CostAverageCumAmt");
        
    }
    
    /** Set Average Cost Quantity Sum.
    @param CostAverageCumQty Cumulative average cost quantities (internal) */
    public void setCostAverageCumQty (java.math.BigDecimal CostAverageCumQty)
    {
        if (CostAverageCumQty == null) throw new IllegalArgumentException ("CostAverageCumQty is mandatory.");
        set_ValueNoCheck ("CostAverageCumQty", CostAverageCumQty);
        
    }
    
    /** Get Average Cost Quantity Sum.
    @return Cumulative average cost quantities (internal) */
    public java.math.BigDecimal getCostAverageCumQty() 
    {
        return get_ValueAsBigDecimal("CostAverageCumQty");
        
    }
    
    /** Set Standard Cost.
    @param CostStandard Standard Costs */
    public void setCostStandard (java.math.BigDecimal CostStandard)
    {
        if (CostStandard == null) throw new IllegalArgumentException ("CostStandard is mandatory.");
        set_ValueNoCheck ("CostStandard", CostStandard);
        
    }
    
    /** Get Standard Cost.
    @return Standard Costs */
    public java.math.BigDecimal getCostStandard() 
    {
        return get_ValueAsBigDecimal("CostStandard");
        
    }
    
    /** Set Std Cost Amount Sum.
    @param CostStandardCumAmt Standard Cost Invoice Amount Sum (internal) */
    public void setCostStandardCumAmt (java.math.BigDecimal CostStandardCumAmt)
    {
        if (CostStandardCumAmt == null) throw new IllegalArgumentException ("CostStandardCumAmt is mandatory.");
        set_ValueNoCheck ("CostStandardCumAmt", CostStandardCumAmt);
        
    }
    
    /** Get Std Cost Amount Sum.
    @return Standard Cost Invoice Amount Sum (internal) */
    public java.math.BigDecimal getCostStandardCumAmt() 
    {
        return get_ValueAsBigDecimal("CostStandardCumAmt");
        
    }
    
    /** Set Std Cost Quantity Sum.
    @param CostStandardCumQty Standard Cost Invoice Quantity Sum (internal) */
    public void setCostStandardCumQty (java.math.BigDecimal CostStandardCumQty)
    {
        if (CostStandardCumQty == null) throw new IllegalArgumentException ("CostStandardCumQty is mandatory.");
        set_ValueNoCheck ("CostStandardCumQty", CostStandardCumQty);
        
    }
    
    /** Get Std Cost Quantity Sum.
    @return Standard Cost Invoice Quantity Sum (internal) */
    public java.math.BigDecimal getCostStandardCumQty() 
    {
        return get_ValueAsBigDecimal("CostStandardCumQty");
        
    }
    
    /** Set Std PO Cost Amount Sum.
    @param CostStandardPOAmt Standard Cost Purchase Order Amount Sum (internal) */
    public void setCostStandardPOAmt (java.math.BigDecimal CostStandardPOAmt)
    {
        if (CostStandardPOAmt == null) throw new IllegalArgumentException ("CostStandardPOAmt is mandatory.");
        set_ValueNoCheck ("CostStandardPOAmt", CostStandardPOAmt);
        
    }
    
    /** Get Std PO Cost Amount Sum.
    @return Standard Cost Purchase Order Amount Sum (internal) */
    public java.math.BigDecimal getCostStandardPOAmt() 
    {
        return get_ValueAsBigDecimal("CostStandardPOAmt");
        
    }
    
    /** Set Std PO Cost Quantity Sum.
    @param CostStandardPOQty Standard Cost Purchase Order Quantity Sum (internal) */
    public void setCostStandardPOQty (java.math.BigDecimal CostStandardPOQty)
    {
        if (CostStandardPOQty == null) throw new IllegalArgumentException ("CostStandardPOQty is mandatory.");
        set_ValueNoCheck ("CostStandardPOQty", CostStandardPOQty);
        
    }
    
    /** Get Std PO Cost Quantity Sum.
    @return Standard Cost Purchase Order Quantity Sum (internal) */
    public java.math.BigDecimal getCostStandardPOQty() 
    {
        return get_ValueAsBigDecimal("CostStandardPOQty");
        
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
    
    /** Set Future Cost.
    @param FutureCostPrice Future Cost */
    public void setFutureCostPrice (java.math.BigDecimal FutureCostPrice)
    {
        if (FutureCostPrice == null) throw new IllegalArgumentException ("FutureCostPrice is mandatory.");
        set_Value ("FutureCostPrice", FutureCostPrice);
        
    }
    
    /** Get Future Cost.
    @return Future Cost */
    public java.math.BigDecimal getFutureCostPrice() 
    {
        return get_ValueAsBigDecimal("FutureCostPrice");
        
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
    
    /** Set Last Invoice Price.
    @param PriceLastInv Price of the last invoice for the product */
    public void setPriceLastInv (java.math.BigDecimal PriceLastInv)
    {
        if (PriceLastInv == null) throw new IllegalArgumentException ("PriceLastInv is mandatory.");
        set_ValueNoCheck ("PriceLastInv", PriceLastInv);
        
    }
    
    /** Get Last Invoice Price.
    @return Price of the last invoice for the product */
    public java.math.BigDecimal getPriceLastInv() 
    {
        return get_ValueAsBigDecimal("PriceLastInv");
        
    }
    
    /** Set Last PO Price.
    @param PriceLastPO Price of the last purchase order for the product */
    public void setPriceLastPO (java.math.BigDecimal PriceLastPO)
    {
        if (PriceLastPO == null) throw new IllegalArgumentException ("PriceLastPO is mandatory.");
        set_ValueNoCheck ("PriceLastPO", PriceLastPO);
        
    }
    
    /** Get Last PO Price.
    @return Price of the last purchase order for the product */
    public java.math.BigDecimal getPriceLastPO() 
    {
        return get_ValueAsBigDecimal("PriceLastPO");
        
    }
    
    /** Set Total Invoice Amount.
    @param TotalInvAmt Cumulative total lifetime invoice amount */
    public void setTotalInvAmt (java.math.BigDecimal TotalInvAmt)
    {
        if (TotalInvAmt == null) throw new IllegalArgumentException ("TotalInvAmt is mandatory.");
        set_ValueNoCheck ("TotalInvAmt", TotalInvAmt);
        
    }
    
    /** Get Total Invoice Amount.
    @return Cumulative total lifetime invoice amount */
    public java.math.BigDecimal getTotalInvAmt() 
    {
        return get_ValueAsBigDecimal("TotalInvAmt");
        
    }
    
    /** Set Total Invoice Quantity.
    @param TotalInvQty Cumulative total lifetime invoice quantity */
    public void setTotalInvQty (java.math.BigDecimal TotalInvQty)
    {
        if (TotalInvQty == null) throw new IllegalArgumentException ("TotalInvQty is mandatory.");
        set_ValueNoCheck ("TotalInvQty", TotalInvQty);
        
    }
    
    /** Get Total Invoice Quantity.
    @return Cumulative total lifetime invoice quantity */
    public java.math.BigDecimal getTotalInvQty() 
    {
        return get_ValueAsBigDecimal("TotalInvQty");
        
    }
    
    
}
