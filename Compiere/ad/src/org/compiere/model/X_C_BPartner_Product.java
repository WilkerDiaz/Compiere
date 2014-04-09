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
/** Generated Model for C_BPartner_Product
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_BPartner_Product.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_BPartner_Product extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BPartner_Product_ID id
    @param trx transaction
    */
    public X_C_BPartner_Product (Ctx ctx, int C_BPartner_Product_ID, Trx trx)
    {
        super (ctx, C_BPartner_Product_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BPartner_Product_ID == 0)
        {
            setC_BPartner_ID (0);
            setM_Product_ID (0);
            setShelfLifeMinDays (0);
            setShelfLifeMinPct (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BPartner_Product (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=632 */
    public static final int Table_ID=632;
    
    /** TableName=C_BPartner_Product */
    public static final String Table_Name="C_BPartner_Product";
    
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
    
    /** Set Quality Rating.
    @param QualityRating Method for rating vendors */
    public void setQualityRating (java.math.BigDecimal QualityRating)
    {
        set_Value ("QualityRating", QualityRating);
        
    }
    
    /** Get Quality Rating.
    @return Method for rating vendors */
    public java.math.BigDecimal getQualityRating() 
    {
        return get_ValueAsBigDecimal("QualityRating");
        
    }
    
    /** Set Min Shelf Life Days.
    @param ShelfLifeMinDays Minimum Shelf Life in days based on Product Instance Guarantee Date */
    public void setShelfLifeMinDays (int ShelfLifeMinDays)
    {
        set_Value ("ShelfLifeMinDays", Integer.valueOf(ShelfLifeMinDays));
        
    }
    
    /** Get Min Shelf Life Days.
    @return Minimum Shelf Life in days based on Product Instance Guarantee Date */
    public int getShelfLifeMinDays() 
    {
        return get_ValueAsInt("ShelfLifeMinDays");
        
    }
    
    /** Set Min Shelf Life %.
    @param ShelfLifeMinPct Minimum Shelf Life in percent based on Product Instance Guarantee Date */
    public void setShelfLifeMinPct (int ShelfLifeMinPct)
    {
        set_Value ("ShelfLifeMinPct", Integer.valueOf(ShelfLifeMinPct));
        
    }
    
    /** Get Min Shelf Life %.
    @return Minimum Shelf Life in percent based on Product Instance Guarantee Date */
    public int getShelfLifeMinPct() 
    {
        return get_ValueAsInt("ShelfLifeMinPct");
        
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
    
    
}
