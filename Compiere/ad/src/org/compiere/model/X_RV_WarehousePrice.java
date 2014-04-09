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
/** Generated Model for RV_WarehousePrice
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_RV_WarehousePrice.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_RV_WarehousePrice extends PO
{
    /** Standard Constructor
    @param ctx context
    @param RV_WarehousePrice_ID id
    @param trx transaction
    */
    public X_RV_WarehousePrice (Ctx ctx, int RV_WarehousePrice_ID, Trx trx)
    {
        super (ctx, RV_WarehousePrice_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (RV_WarehousePrice_ID == 0)
        {
            setC_UOM_ID (0);
            setM_PriceList_Version_ID (0);
            setM_Product_ID (0);
            setM_Warehouse_ID (0);
            setName (null);
            setValue (null);
            setWarehouseName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_RV_WarehousePrice (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=639 */
    public static final int Table_ID=639;
    
    /** TableName=RV_WarehousePrice */
    public static final String Table_Name="RV_WarehousePrice";
    
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
        if (C_UOM_ID < 1) throw new IllegalArgumentException ("C_UOM_ID is mandatory.");
        set_ValueNoCheck ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Instance Attribute.
    @param IsInstanceAttribute The product attribute is specific to the instance (like Serial No, Lot or Guarantee Date) */
    public void setIsInstanceAttribute (boolean IsInstanceAttribute)
    {
        set_ValueNoCheck ("IsInstanceAttribute", Boolean.valueOf(IsInstanceAttribute));
        
    }
    
    /** Get Instance Attribute.
    @return The product attribute is specific to the instance (like Serial No, Lot or Guarantee Date) */
    public boolean isInstanceAttribute() 
    {
        return get_ValueAsBoolean("IsInstanceAttribute");
        
    }
    
    /** Set Price List Version.
    @param M_PriceList_Version_ID Identifies a unique instance of a Price List */
    public void setM_PriceList_Version_ID (int M_PriceList_Version_ID)
    {
        if (M_PriceList_Version_ID < 1) throw new IllegalArgumentException ("M_PriceList_Version_ID is mandatory.");
        set_ValueNoCheck ("M_PriceList_Version_ID", Integer.valueOf(M_PriceList_Version_ID));
        
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
    
    /** Set Margin %.
    @param Margin Margin for a product as a percentage */
    public void setMargin (java.math.BigDecimal Margin)
    {
        set_ValueNoCheck ("Margin", Margin);
        
    }
    
    /** Get Margin %.
    @return Margin for a product as a percentage */
    public java.math.BigDecimal getMargin() 
    {
        return get_ValueAsBigDecimal("Margin");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_ValueNoCheck ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Limit Price.
    @param PriceLimit Lowest price for a product */
    public void setPriceLimit (java.math.BigDecimal PriceLimit)
    {
        set_ValueNoCheck ("PriceLimit", PriceLimit);
        
    }
    
    /** Get Limit Price.
    @return Lowest price for a product */
    public java.math.BigDecimal getPriceLimit() 
    {
        return get_ValueAsBigDecimal("PriceLimit");
        
    }
    
    /** Set List price.
    @param PriceList List price (Internally used vs. entered) */
    public void setPriceList (java.math.BigDecimal PriceList)
    {
        set_ValueNoCheck ("PriceList", PriceList);
        
    }
    
    /** Get List price.
    @return List price (Internally used vs. entered) */
    public java.math.BigDecimal getPriceList() 
    {
        return get_ValueAsBigDecimal("PriceList");
        
    }
    
    /** Set Standard Price.
    @param PriceStd Standard Price */
    public void setPriceStd (java.math.BigDecimal PriceStd)
    {
        set_ValueNoCheck ("PriceStd", PriceStd);
        
    }
    
    /** Get Standard Price.
    @return Standard Price */
    public java.math.BigDecimal getPriceStd() 
    {
        return get_ValueAsBigDecimal("PriceStd");
        
    }
    
    /** Set Available Quantity.
    @param QtyAvailable Available Quantity (On Hand - Reserved) */
    public void setQtyAvailable (java.math.BigDecimal QtyAvailable)
    {
        set_ValueNoCheck ("QtyAvailable", QtyAvailable);
        
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
        set_ValueNoCheck ("QtyOnHand", QtyOnHand);
        
    }
    
    /** Get On Hand Quantity.
    @return On Hand Quantity */
    public java.math.BigDecimal getQtyOnHand() 
    {
        return get_ValueAsBigDecimal("QtyOnHand");
        
    }
    
    /** Set Quantity Ordered.
    @param QtyOrdered Ordered Quantity */
    public void setQtyOrdered (java.math.BigDecimal QtyOrdered)
    {
        set_ValueNoCheck ("QtyOrdered", QtyOrdered);
        
    }
    
    /** Get Quantity Ordered.
    @return Ordered Quantity */
    public java.math.BigDecimal getQtyOrdered() 
    {
        return get_ValueAsBigDecimal("QtyOrdered");
        
    }
    
    /** Set Quantity Reserved.
    @param QtyReserved Quantity Reserved */
    public void setQtyReserved (java.math.BigDecimal QtyReserved)
    {
        set_ValueNoCheck ("QtyReserved", QtyReserved);
        
    }
    
    /** Get Quantity Reserved.
    @return Quantity Reserved */
    public java.math.BigDecimal getQtyReserved() 
    {
        return get_ValueAsBigDecimal("QtyReserved");
        
    }
    
    /** Set SKU.
    @param SKU Stock Keeping Unit */
    public void setSKU (String SKU)
    {
        set_ValueNoCheck ("SKU", SKU);
        
    }
    
    /** Get SKU.
    @return Stock Keeping Unit */
    public String getSKU() 
    {
        return (String)get_Value("SKU");
        
    }
    
    /** Set Symbol.
    @param UOMSymbol Symbol for a Unit of Measure */
    public void setUOMSymbol (String UOMSymbol)
    {
        set_ValueNoCheck ("UOMSymbol", UOMSymbol);
        
    }
    
    /** Get Symbol.
    @return Symbol for a Unit of Measure */
    public String getUOMSymbol() 
    {
        return (String)get_Value("UOMSymbol");
        
    }
    
    /** Set UPC/EAN.
    @param UPC Bar Code (Universal Product Code or its superset European Article Number) */
    public void setUPC (String UPC)
    {
        set_ValueNoCheck ("UPC", UPC);
        
    }
    
    /** Get UPC/EAN.
    @return Bar Code (Universal Product Code or its superset European Article Number) */
    public String getUPC() 
    {
        return (String)get_Value("UPC");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_ValueNoCheck ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Warehouse Name.
    @param WarehouseName Warehouse Name */
    public void setWarehouseName (String WarehouseName)
    {
        if (WarehouseName == null) throw new IllegalArgumentException ("WarehouseName is mandatory.");
        set_ValueNoCheck ("WarehouseName", WarehouseName);
        
    }
    
    /** Get Warehouse Name.
    @return Warehouse Name */
    public String getWarehouseName() 
    {
        return (String)get_Value("WarehouseName");
        
    }
    
    
}
