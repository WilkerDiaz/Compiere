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
/** Generated Model for M_StorageDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.1 - $Id: GenerateModel.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_StorageDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_StorageDetail_ID id
    @param trx transaction
    */
    public X_M_StorageDetail (Ctx ctx, int M_StorageDetail_ID, Trx trx)
    {
        super (ctx, M_StorageDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_StorageDetail_ID == 0)
        {
            setM_AttributeSetInstance_ID (0);
            setM_Locator_ID (0);
            setM_Product_ID (0);
            setQty (Env.ZERO);
            setQtyType (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_StorageDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27552698825789L;
    /** Last Updated Timestamp 2010-04-06 10:05:09.0 */
    public static final long updatedMS = 1270573509000L;
    /** AD_Table_ID=2160 */
    public static final int Table_ID=2160;
    
    /** TableName=M_StorageDetail */
    public static final String Table_Name="M_StorageDetail";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Date last inventory count.
    @param DateLastInventory Date of Last Inventory Count */
    public void setDateLastInventory (Timestamp DateLastInventory)
    {
        set_ValueNoCheck ("DateLastInventory", DateLastInventory);
        
    }
    
    /** Get Date last inventory count.
    @return Date of Last Inventory Count */
    public Timestamp getDateLastInventory() 
    {
        return (Timestamp)get_Value("DateLastInventory");
        
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
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID < 1) throw new IllegalArgumentException ("M_Locator_ID is mandatory.");
        set_ValueNoCheck ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
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
        set_ValueNoCheck ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    /** Allocated = A */
    public static final String QTYTYPE_Allocated = X_Ref_Quantity_Type.ALLOCATED.getValue();
    /** Dedicated = D */
    public static final String QTYTYPE_Dedicated = X_Ref_Quantity_Type.DEDICATED.getValue();
    /** Expected = E */
    public static final String QTYTYPE_Expected = X_Ref_Quantity_Type.EXPECTED.getValue();
    /** On Hand = H */
    public static final String QTYTYPE_OnHand = X_Ref_Quantity_Type.ON_HAND.getValue();
    /** Ordered = O */
    public static final String QTYTYPE_Ordered = X_Ref_Quantity_Type.ORDERED.getValue();
    /** Reserved = R */
    public static final String QTYTYPE_Reserved = X_Ref_Quantity_Type.RESERVED.getValue();
    /** Set Quantity Type.
    @param QtyType Quantity Type */
    public void setQtyType (String QtyType)
    {
        if (QtyType == null) throw new IllegalArgumentException ("QtyType is mandatory");
        if (!X_Ref_Quantity_Type.isValid(QtyType))
        throw new IllegalArgumentException ("QtyType Invalid value - " + QtyType + " - Reference_ID=533 - A - D - E - H - O - R");
        set_ValueNoCheck ("QtyType", QtyType);
        
    }
    
    /** Get Quantity Type.
    @return Quantity Type */
    public String getQtyType() 
    {
        return (String)get_Value("QtyType");
        
    }
    
    
}
