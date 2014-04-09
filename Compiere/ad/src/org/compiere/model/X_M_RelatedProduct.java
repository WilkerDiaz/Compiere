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
/** Generated Model for M_RelatedProduct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_RelatedProduct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_RelatedProduct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_RelatedProduct_ID id
    @param trx transaction
    */
    public X_M_RelatedProduct (Ctx ctx, int M_RelatedProduct_ID, Trx trx)
    {
        super (ctx, M_RelatedProduct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_RelatedProduct_ID == 0)
        {
            setM_Product_ID (0);
            setName (null);
            setRelatedProductType (null);
            setRelatedProduct_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_RelatedProduct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=662 */
    public static final int Table_ID=662;
    
    /** TableName=M_RelatedProduct */
    public static final String Table_Name="M_RelatedProduct";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Alternative = A */
    public static final String RELATEDPRODUCTTYPE_Alternative = X_Ref_M_RelatedProduct_Type.ALTERNATIVE.getValue();
    /** Web Promotion = P */
    public static final String RELATEDPRODUCTTYPE_WebPromotion = X_Ref_M_RelatedProduct_Type.WEB_PROMOTION.getValue();
    /** Supplemental = S */
    public static final String RELATEDPRODUCTTYPE_Supplemental = X_Ref_M_RelatedProduct_Type.SUPPLEMENTAL.getValue();
    /** Set Related Product Type.
    @param RelatedProductType Related Product Type */
    public void setRelatedProductType (String RelatedProductType)
    {
        if (RelatedProductType == null) throw new IllegalArgumentException ("RelatedProductType is mandatory");
        if (!X_Ref_M_RelatedProduct_Type.isValid(RelatedProductType))
        throw new IllegalArgumentException ("RelatedProductType Invalid value - " + RelatedProductType + " - Reference_ID=313 - A - P - S");
        set_Value ("RelatedProductType", RelatedProductType);
        
    }
    
    /** Get Related Product Type.
    @return Related Product Type */
    public String getRelatedProductType() 
    {
        return (String)get_Value("RelatedProductType");
        
    }
    
    /** Set Related Product.
    @param RelatedProduct_ID Related Product */
    public void setRelatedProduct_ID (int RelatedProduct_ID)
    {
        if (RelatedProduct_ID < 1) throw new IllegalArgumentException ("RelatedProduct_ID is mandatory.");
        set_ValueNoCheck ("RelatedProduct_ID", Integer.valueOf(RelatedProduct_ID));
        
    }
    
    /** Get Related Product.
    @return Related Product */
    public int getRelatedProduct_ID() 
    {
        return get_ValueAsInt("RelatedProduct_ID");
        
    }
    
    
}
