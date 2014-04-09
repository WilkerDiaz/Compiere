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
/** Generated Model for T_InventoryValue
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_InventoryValue.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_InventoryValue extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_InventoryValue_ID id
    @param trx transaction
    */
    public X_T_InventoryValue (Ctx ctx, int T_InventoryValue_ID, Trx trx)
    {
        super (ctx, T_InventoryValue_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_InventoryValue_ID == 0)
        {
            setAD_PInstance_ID (0);
            setM_AttributeSetInstance_ID (0);
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
    public X_T_InventoryValue (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511671677789L;
    /** Last Updated Timestamp 2008-12-17 12:39:21.0 */
    public static final long updatedMS = 1229546361000L;
    /** AD_Table_ID=478 */
    public static final int Table_ID=478;
    
    /** TableName=T_InventoryValue */
    public static final String Table_Name="T_InventoryValue";
    
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
        if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
        set_ValueNoCheck ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_PInstance_ID()));
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
        else
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Cost.
    @param Cost Cost information */
    public void setCost (java.math.BigDecimal Cost)
    {
        set_Value ("Cost", Cost);
        
    }
    
    /** Get Cost.
    @return Cost information */
    public java.math.BigDecimal getCost() 
    {
        return get_ValueAsBigDecimal("Cost");
        
    }
    
    /** Set Cost Value.
    @param CostAmt Value with Cost */
    public void setCostAmt (java.math.BigDecimal CostAmt)
    {
        set_Value ("CostAmt", CostAmt);
        
    }
    
    /** Get Cost Value.
    @return Value with Cost */
    public java.math.BigDecimal getCostAmt() 
    {
        return get_ValueAsBigDecimal("CostAmt");
        
    }
    
    /** Set Standard Cost.
    @param CostStandard Standard Costs */
    public void setCostStandard (java.math.BigDecimal CostStandard)
    {
        set_Value ("CostStandard", CostStandard);
        
    }
    
    /** Get Standard Cost.
    @return Standard Costs */
    public java.math.BigDecimal getCostStandard() 
    {
        return get_ValueAsBigDecimal("CostStandard");
        
    }
    
    /** Set Standard Cost Value.
    @param CostStandardAmt Value in Standard Costs */
    public void setCostStandardAmt (java.math.BigDecimal CostStandardAmt)
    {
        set_Value ("CostStandardAmt", CostStandardAmt);
        
    }
    
    /** Get Standard Cost Value.
    @return Value in Standard Costs */
    public java.math.BigDecimal getCostStandardAmt() 
    {
        return get_ValueAsBigDecimal("CostStandardAmt");
        
    }
    
    /** Set Valuation Date.
    @param DateValue Date of valuation */
    public void setDateValue (Timestamp DateValue)
    {
        set_Value ("DateValue", DateValue);
        
    }
    
    /** Get Valuation Date.
    @return Date of valuation */
    public Timestamp getDateValue() 
    {
        return (Timestamp)get_Value("DateValue");
        
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
        if (M_CostElement_ID <= 0) set_Value ("M_CostElement_ID", null);
        else
        set_Value ("M_CostElement_ID", Integer.valueOf(M_CostElement_ID));
        
    }
    
    /** Get Cost Element.
    @return Product Cost Element */
    public int getM_CostElement_ID() 
    {
        return get_ValueAsInt("M_CostElement_ID");
        
    }
    
    /** Set Price List Version.
    @param M_PriceList_Version_ID Identifies a unique instance of a Price List */
    public void setM_PriceList_Version_ID (int M_PriceList_Version_ID)
    {
        if (M_PriceList_Version_ID <= 0) set_Value ("M_PriceList_Version_ID", null);
        else
        set_Value ("M_PriceList_Version_ID", Integer.valueOf(M_PriceList_Version_ID));
        
    }
    
    /** Get Price List Version.
    @return Identifies a unique instance of a Price List */
    public int getM_PriceList_Version_ID() 
    {
        return get_ValueAsInt("M_PriceList_Version_ID");
        
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
    
    /** Set Limit Price.
    @param PriceLimit Lowest price for a product */
    public void setPriceLimit (java.math.BigDecimal PriceLimit)
    {
        set_Value ("PriceLimit", PriceLimit);
        
    }
    
    /** Get Limit Price.
    @return Lowest price for a product */
    public java.math.BigDecimal getPriceLimit() 
    {
        return get_ValueAsBigDecimal("PriceLimit");
        
    }
    
    /** Set Limit price Value.
    @param PriceLimitAmt Value with limit price */
    public void setPriceLimitAmt (java.math.BigDecimal PriceLimitAmt)
    {
        set_Value ("PriceLimitAmt", PriceLimitAmt);
        
    }
    
    /** Get Limit price Value.
    @return Value with limit price */
    public java.math.BigDecimal getPriceLimitAmt() 
    {
        return get_ValueAsBigDecimal("PriceLimitAmt");
        
    }
    
    /** Set List price.
    @param PriceList List price (Internally used vs. entered) */
    public void setPriceList (java.math.BigDecimal PriceList)
    {
        set_Value ("PriceList", PriceList);
        
    }
    
    /** Get List price.
    @return List price (Internally used vs. entered) */
    public java.math.BigDecimal getPriceList() 
    {
        return get_ValueAsBigDecimal("PriceList");
        
    }
    
    /** Set List price Value.
    @param PriceListAmt Valuation with List Price */
    public void setPriceListAmt (java.math.BigDecimal PriceListAmt)
    {
        set_Value ("PriceListAmt", PriceListAmt);
        
    }
    
    /** Get List price Value.
    @return Valuation with List Price */
    public java.math.BigDecimal getPriceListAmt() 
    {
        return get_ValueAsBigDecimal("PriceListAmt");
        
    }
    
    /** Set PO Price.
    @param PricePO Price based on a purchase order */
    public void setPricePO (java.math.BigDecimal PricePO)
    {
        set_Value ("PricePO", PricePO);
        
    }
    
    /** Get PO Price.
    @return Price based on a purchase order */
    public java.math.BigDecimal getPricePO() 
    {
        return get_ValueAsBigDecimal("PricePO");
        
    }
    
    /** Set PO Price Value.
    @param PricePOAmt Valuation with PO Price */
    public void setPricePOAmt (java.math.BigDecimal PricePOAmt)
    {
        set_Value ("PricePOAmt", PricePOAmt);
        
    }
    
    /** Get PO Price Value.
    @return Valuation with PO Price */
    public java.math.BigDecimal getPricePOAmt() 
    {
        return get_ValueAsBigDecimal("PricePOAmt");
        
    }
    
    /** Set Standard Price.
    @param PriceStd Standard Price */
    public void setPriceStd (java.math.BigDecimal PriceStd)
    {
        set_Value ("PriceStd", PriceStd);
        
    }
    
    /** Get Standard Price.
    @return Standard Price */
    public java.math.BigDecimal getPriceStd() 
    {
        return get_ValueAsBigDecimal("PriceStd");
        
    }
    
    /** Set Std Price Value.
    @param PriceStdAmt Valuation with standard price */
    public void setPriceStdAmt (java.math.BigDecimal PriceStdAmt)
    {
        set_Value ("PriceStdAmt", PriceStdAmt);
        
    }
    
    /** Get Std Price Value.
    @return Valuation with standard price */
    public java.math.BigDecimal getPriceStdAmt() 
    {
        return get_ValueAsBigDecimal("PriceStdAmt");
        
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
