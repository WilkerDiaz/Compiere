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
/** Generated Model for M_Product_PO
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_Product_PO.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_Product_PO extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_Product_PO_ID id
    @param trx transaction
    */
    public X_M_Product_PO (Ctx ctx, int M_Product_PO_ID, Trx trx)
    {
        super (ctx, M_Product_PO_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Product_PO_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_Currency_ID (0);
            setIsCurrentVendor (true);	// Y
            setM_Product_ID (0);	// @M_Product_ID@
            setVendorProductNo (null);	// @Value@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_Product_PO (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=210 */
    public static final int Table_ID=210;
    
    /** TableName=M_Product_PO */
    public static final String Table_Name="M_Product_PO";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
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
    
    /** Set Cost per Order.
    @param CostPerOrder Fixed Cost per Order */
    public void setCostPerOrder (java.math.BigDecimal CostPerOrder)
    {
        set_Value ("CostPerOrder", CostPerOrder);
        
    }
    
    /** Get Cost per Order.
    @return Fixed Cost per Order */
    public java.math.BigDecimal getCostPerOrder() 
    {
        return get_ValueAsBigDecimal("CostPerOrder");
        
    }
    
    /** Set Actual Delivery Time.
    @param DeliveryTime_Actual Actual days between order and delivery */
    public void setDeliveryTime_Actual (int DeliveryTime_Actual)
    {
        set_Value ("DeliveryTime_Actual", Integer.valueOf(DeliveryTime_Actual));
        
    }
    
    /** Get Actual Delivery Time.
    @return Actual days between order and delivery */
    public int getDeliveryTime_Actual() 
    {
        return get_ValueAsInt("DeliveryTime_Actual");
        
    }
    
    /** Set Promised Delivery Time.
    @param DeliveryTime_Promised Promised days between order and delivery */
    public void setDeliveryTime_Promised (int DeliveryTime_Promised)
    {
        set_Value ("DeliveryTime_Promised", Integer.valueOf(DeliveryTime_Promised));
        
    }
    
    /** Get Promised Delivery Time.
    @return Promised days between order and delivery */
    public int getDeliveryTime_Promised() 
    {
        return get_ValueAsInt("DeliveryTime_Promised");
        
    }
    
    /** Set Discontinued.
    @param Discontinued This product is no longer available */
    public void setDiscontinued (boolean Discontinued)
    {
        set_Value ("Discontinued", Boolean.valueOf(Discontinued));
        
    }
    
    /** Get Discontinued.
    @return This product is no longer available */
    public boolean isDiscontinued() 
    {
        return get_ValueAsBoolean("Discontinued");
        
    }
    
    /** Set Discontinued By.
    @param DiscontinuedBy Discontinued By */
    public void setDiscontinuedBy (Timestamp DiscontinuedBy)
    {
        set_Value ("DiscontinuedBy", DiscontinuedBy);
        
    }
    
    /** Get Discontinued By.
    @return Discontinued By */
    public Timestamp getDiscontinuedBy() 
    {
        return (Timestamp)get_Value("DiscontinuedBy");
        
    }
    
    /** Set Current vendor.
    @param IsCurrentVendor Use this Vendor for pricing and stock replenishment */
    public void setIsCurrentVendor (boolean IsCurrentVendor)
    {
        set_Value ("IsCurrentVendor", Boolean.valueOf(IsCurrentVendor));
        
    }
    
    /** Get Current vendor.
    @return Use this Vendor for pricing and stock replenishment */
    public boolean isCurrentVendor() 
    {
        return get_ValueAsBoolean("IsCurrentVendor");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_Product_ID()));
        
    }
    
    /** Set Manufacturer.
    @param Manufacturer Manufacturer of the Product */
    public void setManufacturer (String Manufacturer)
    {
        set_Value ("Manufacturer", Manufacturer);
        
    }
    
    /** Get Manufacturer.
    @return Manufacturer of the Product */
    public String getManufacturer() 
    {
        return (String)get_Value("Manufacturer");
        
    }
    
    /** Set Minimum Order Qty.
    @param Order_Min Minimum order quantity in UOM */
    public void setOrder_Min (java.math.BigDecimal Order_Min)
    {
        set_Value ("Order_Min", Order_Min);
        
    }
    
    /** Get Minimum Order Qty.
    @return Minimum order quantity in UOM */
    public java.math.BigDecimal getOrder_Min() 
    {
        return get_ValueAsBigDecimal("Order_Min");
        
    }
    
    /** Set Order Pack Qty.
    @param Order_Pack Package order size in UOM (e.g. order set of 5 units) */
    public void setOrder_Pack (java.math.BigDecimal Order_Pack)
    {
        set_Value ("Order_Pack", Order_Pack);
        
    }
    
    /** Get Order Pack Qty.
    @return Package order size in UOM (e.g. order set of 5 units) */
    public java.math.BigDecimal getOrder_Pack() 
    {
        return get_ValueAsBigDecimal("Order_Pack");
        
    }
    
    /** Set Price effective.
    @param PriceEffective Effective Date of Price */
    public void setPriceEffective (Timestamp PriceEffective)
    {
        set_Value ("PriceEffective", PriceEffective);
        
    }
    
    /** Get Price effective.
    @return Effective Date of Price */
    public Timestamp getPriceEffective() 
    {
        return (Timestamp)get_Value("PriceEffective");
        
    }
    
    /** Set Last Invoice Price.
    @param PriceLastInv Price of the last invoice for the product */
    public void setPriceLastInv (java.math.BigDecimal PriceLastInv)
    {
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
        set_ValueNoCheck ("PriceLastPO", PriceLastPO);
        
    }
    
    /** Get Last PO Price.
    @return Price of the last purchase order for the product */
    public java.math.BigDecimal getPriceLastPO() 
    {
        return get_ValueAsBigDecimal("PriceLastPO");
        
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
    
    /** Set Quality Rating.
    @param QualityRating Method for rating vendors */
    public void setQualityRating (int QualityRating)
    {
        set_Value ("QualityRating", Integer.valueOf(QualityRating));
        
    }
    
    /** Get Quality Rating.
    @return Method for rating vendors */
    public int getQualityRating() 
    {
        return get_ValueAsInt("QualityRating");
        
    }
    
    /** Set Royalty Amount.
    @param RoyaltyAmt (Included) Amount for copyright, etc. */
    public void setRoyaltyAmt (java.math.BigDecimal RoyaltyAmt)
    {
        set_Value ("RoyaltyAmt", RoyaltyAmt);
        
    }
    
    /** Get Royalty Amount.
    @return (Included) Amount for copyright, etc. */
    public java.math.BigDecimal getRoyaltyAmt() 
    {
        return get_ValueAsBigDecimal("RoyaltyAmt");
        
    }
    
    /** Set UPC/EAN.
    @param UPC Bar Code (Universal Product Code or its superset European Article Number) */
    public void setUPC (String UPC)
    {
        set_Value ("UPC", UPC);
        
    }
    
    /** Get UPC/EAN.
    @return Bar Code (Universal Product Code or its superset European Article Number) */
    public String getUPC() 
    {
        return (String)get_Value("UPC");
        
    }
    
    /** Set Partner Category.
    @param VendorCategory Product Category of the Business Partner */
    public void setVendorCategory (String VendorCategory)
    {
        set_Value ("VendorCategory", VendorCategory);
        
    }
    
    /** Get Partner Category.
    @return Product Category of the Business Partner */
    public String getVendorCategory() 
    {
        return (String)get_Value("VendorCategory");
        
    }
    
    /** Set Partner Product Key.
    @param VendorProductNo Product Key of the Business Partner */
    public void setVendorProductNo (String VendorProductNo)
    {
        if (VendorProductNo == null) throw new IllegalArgumentException ("VendorProductNo is mandatory.");
        set_Value ("VendorProductNo", VendorProductNo);
        
    }
    
    /** Get Partner Product Key.
    @return Product Key of the Business Partner */
    public String getVendorProductNo() 
    {
        return (String)get_Value("VendorProductNo");
        
    }
    
    
}
