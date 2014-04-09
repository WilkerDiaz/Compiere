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
/** Generated Model for I_Product
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.1 - $Id: X_I_Product.java 8686 2010-04-27 05:07:07Z sdandapat $ */
public class X_I_Product extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_Product_ID id
    @param trx transaction
    */
    public X_I_Product (Ctx ctx, int I_Product_ID, Trx trx)
    {
        super (ctx, I_Product_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_Product_ID == 0)
        {
            setI_IsImported (null);	// N
            setI_Product_ID (0);
            setIsPurchasedToOrder (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_Product (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27554425596789L;
    /** Last Updated Timestamp 2010-04-26 22:14:40.0 */
    public static final long updatedMS = 1272300280000L;
    /** AD_Table_ID=532 */
    public static final int Table_ID=532;
    
    /** TableName=I_Product */
    public static final String Table_Name="I_Product";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner Key.
    @param BPartner_Value The Key of the Business Partner */
    public void setBPartner_Value (String BPartner_Value)
    {
        set_Value ("BPartner_Value", BPartner_Value);
        
    }
    
    /** Get Business Partner Key.
    @return The Key of the Business Partner */
    public String getBPartner_Value() 
    {
        return (String)get_Value("BPartner_Value");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
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
    
    /** Set Classification.
    @param Classification Classification for grouping */
    public void setClassification (String Classification)
    {
        set_Value ("Classification", Classification);
        
    }
    
    /** Get Classification.
    @return Classification for grouping */
    public String getClassification() 
    {
        return (String)get_Value("Classification");
        
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
    
    /** Set Description URL.
    @param DescriptionURL URL for the description */
    public void setDescriptionURL (String DescriptionURL)
    {
        set_Value ("DescriptionURL", DescriptionURL);
        
    }
    
    /** Get Description URL.
    @return URL for the description */
    public String getDescriptionURL() 
    {
        return (String)get_Value("DescriptionURL");
        
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
    
    /** Set Document Note.
    @param DocumentNote Additional information for a Document */
    public void setDocumentNote (String DocumentNote)
    {
        set_Value ("DocumentNote", DocumentNote);
        
    }
    
    /** Get Document Note.
    @return Additional information for a Document */
    public String getDocumentNote() 
    {
        return (String)get_Value("DocumentNote");
        
    }
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set ISO Currency Code.
    @param ISO_Code Three letter ISO 4217 Code of the Currency */
    public void setISO_Code (String ISO_Code)
    {
        set_Value ("ISO_Code", ISO_Code);
        
    }
    
    /** Get ISO Currency Code.
    @return Three letter ISO 4217 Code of the Currency */
    public String getISO_Code() 
    {
        return (String)get_Value("ISO_Code");
        
    }
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Error = E */
    public static final String I_ISIMPORTED_Error = X_Ref__IsImported.ERROR.getValue();
    /** No = N */
    public static final String I_ISIMPORTED_No = X_Ref__IsImported.NO.getValue();
    /** Yes = Y */
    public static final String I_ISIMPORTED_Yes = X_Ref__IsImported.YES.getValue();
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (String I_IsImported)
    {
        if (I_IsImported == null) throw new IllegalArgumentException ("I_IsImported is mandatory");
        if (!X_Ref__IsImported.isValid(I_IsImported))
        throw new IllegalArgumentException ("I_IsImported Invalid value - " + I_IsImported + " - Reference_ID=420 - E - N - Y");
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set Import Product.
    @param I_Product_ID Import Item or Service */
    public void setI_Product_ID (int I_Product_ID)
    {
        if (I_Product_ID < 1) throw new IllegalArgumentException ("I_Product_ID is mandatory.");
        set_ValueNoCheck ("I_Product_ID", Integer.valueOf(I_Product_ID));
        
    }
    
    /** Get Import Product.
    @return Import Item or Service */
    public int getI_Product_ID() 
    {
        return get_ValueAsInt("I_Product_ID");
        
    }
    
    /** Set Image URL.
    @param ImageURL URL of image */
    public void setImageURL (String ImageURL)
    {
        set_Value ("ImageURL", ImageURL);
        
    }
    
    /** Get Image URL.
    @return URL of image */
    public String getImageURL() 
    {
        return (String)get_Value("ImageURL");
        
    }
    
    /** Set Purchased To Order.
    @param IsPurchasedToOrder Products that are usually not kept in stock, but are purchased whenever there is a demand */
    public void setIsPurchasedToOrder (boolean IsPurchasedToOrder)
    {
        set_Value ("IsPurchasedToOrder", Boolean.valueOf(IsPurchasedToOrder));
        
    }
    
    /** Get Purchased To Order.
    @return Products that are usually not kept in stock, but are purchased whenever there is a demand */
    public boolean isPurchasedToOrder() 
    {
        return get_ValueAsBoolean("IsPurchasedToOrder");
        
    }
    
    /** Set Product Category.
    @param M_Product_Category_ID Category of a Product */
    public void setM_Product_Category_ID (int M_Product_Category_ID)
    {
        if (M_Product_Category_ID <= 0) set_Value ("M_Product_Category_ID", null);
        else
        set_Value ("M_Product_Category_ID", Integer.valueOf(M_Product_Category_ID));
        
    }
    
    /** Get Product Category.
    @return Category of a Product */
    public int getM_Product_Category_ID() 
    {
        return get_ValueAsInt("M_Product_Category_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
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
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Product Category Key.
    @param ProductCategory_Value Product Category Key */
    public void setProductCategory_Value (String ProductCategory_Value)
    {
        set_Value ("ProductCategory_Value", ProductCategory_Value);
        
    }
    
    /** Get Product Category Key.
    @return Product Category Key */
    public String getProductCategory_Value() 
    {
        return (String)get_Value("ProductCategory_Value");
        
    }
    
    /** Expense type = E */
    public static final String PRODUCTTYPE_ExpenseType = X_Ref_M_Product_ProductType.EXPENSE_TYPE.getValue();
    /** Item = I */
    public static final String PRODUCTTYPE_Item = X_Ref_M_Product_ProductType.ITEM.getValue();
    /** Online = O */
    public static final String PRODUCTTYPE_Online = X_Ref_M_Product_ProductType.ONLINE.getValue();
    /** Resource = R */
    public static final String PRODUCTTYPE_Resource = X_Ref_M_Product_ProductType.RESOURCE.getValue();
    /** Service = S */
    public static final String PRODUCTTYPE_Service = X_Ref_M_Product_ProductType.SERVICE.getValue();
    /** Set Product Type.
    @param ProductType Type of product */
    public void setProductType (String ProductType)
    {
        if (!X_Ref_M_Product_ProductType.isValid(ProductType))
        throw new IllegalArgumentException ("ProductType Invalid value - " + ProductType + " - Reference_ID=270 - E - I - O - R - S");
        set_Value ("ProductType", ProductType);
        
    }
    
    /** Get Product Type.
    @return Type of product */
    public String getProductType() 
    {
        return (String)get_Value("ProductType");
        
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
    
    /** Set SKU.
    @param SKU Stock Keeping Unit */
    public void setSKU (String SKU)
    {
        set_Value ("SKU", SKU);
        
    }
    
    /** Get SKU.
    @return Stock Keeping Unit */
    public String getSKU() 
    {
        return (String)get_Value("SKU");
        
    }
    
    /** Set Shelf Depth.
    @param ShelfDepth Shelf depth required */
    public void setShelfDepth (int ShelfDepth)
    {
        set_Value ("ShelfDepth", Integer.valueOf(ShelfDepth));
        
    }
    
    /** Get Shelf Depth.
    @return Shelf depth required */
    public int getShelfDepth() 
    {
        return get_ValueAsInt("ShelfDepth");
        
    }
    
    /** Set Shelf Height.
    @param ShelfHeight Shelf height required */
    public void setShelfHeight (int ShelfHeight)
    {
        set_Value ("ShelfHeight", Integer.valueOf(ShelfHeight));
        
    }
    
    /** Get Shelf Height.
    @return Shelf height required */
    public int getShelfHeight() 
    {
        return get_ValueAsInt("ShelfHeight");
        
    }
    
    /** Set Shelf Width.
    @param ShelfWidth Shelf width required */
    public void setShelfWidth (int ShelfWidth)
    {
        set_Value ("ShelfWidth", Integer.valueOf(ShelfWidth));
        
    }
    
    /** Get Shelf Width.
    @return Shelf width required */
    public int getShelfWidth() 
    {
        return get_ValueAsInt("ShelfWidth");
        
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
    
    /** Set Units per Pallet.
    @param UnitsPerPallet Units per Pallet */
    public void setUnitsPerPallet (int UnitsPerPallet)
    {
        set_Value ("UnitsPerPallet", Integer.valueOf(UnitsPerPallet));
        
    }
    
    /** Get Units per Pallet.
    @return Units per Pallet */
    public int getUnitsPerPallet() 
    {
        return get_ValueAsInt("UnitsPerPallet");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getValue());
        
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
        set_Value ("VendorProductNo", VendorProductNo);
        
    }
    
    /** Get Partner Product Key.
    @return Product Key of the Business Partner */
    public String getVendorProductNo() 
    {
        return (String)get_Value("VendorProductNo");
        
    }
    
    /** Set Volume.
    @param Volume Volume of a product */
    public void setVolume (java.math.BigDecimal Volume)
    {
        set_Value ("Volume", Volume);
        
    }
    
    /** Get Volume.
    @return Volume of a product */
    public java.math.BigDecimal getVolume() 
    {
        return get_ValueAsBigDecimal("Volume");
        
    }
    
    /** Set Weight.
    @param Weight Weight of a product */
    public void setWeight (java.math.BigDecimal Weight)
    {
        set_Value ("Weight", Weight);
        
    }
    
    /** Get Weight.
    @return Weight of a product */
    public java.math.BigDecimal getWeight() 
    {
        return get_ValueAsBigDecimal("Weight");
        
    }
    
    /** Set UOM Code.
    @param X12DE355 UOM EDI X12 Code */
    public void setX12DE355 (String X12DE355)
    {
        set_Value ("X12DE355", X12DE355);
        
    }
    
    /** Get UOM Code.
    @return UOM EDI X12 Code */
    public String getX12DE355() 
    {
        return (String)get_Value("X12DE355");
        
    }
    
    
}
