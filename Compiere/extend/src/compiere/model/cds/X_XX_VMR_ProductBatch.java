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
package compiere.model.cds;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_ProductBatch
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_ProductBatch extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_ProductBatch_ID id
    @param trx transaction
    */
    public X_XX_VMR_ProductBatch (Ctx ctx, int XX_VMR_ProductBatch_ID, Trx trx)
    {
        super (ctx, XX_VMR_ProductBatch_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_ProductBatch_ID == 0)
        {
            setIsCreated (false);
            setPriceActual (Env.ZERO);
            setProductValue (null);
            setXX_SalePrice (Env.ZERO);
            setXX_VMR_ProductBatch_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_ProductBatch (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27566101916789L;
    /** Last Updated Timestamp 2010-09-08 15:40:00.0 */
    public static final long updatedMS = 1283976600000L;
    /** AD_Table_ID=1000365 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_ProductBatch");
        
    }
    ;
    
    /** TableName=XX_VMR_ProductBatch */
    public static final String Table_Name="XX_VMR_ProductBatch";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Records created.
    @param IsCreated Records created */
    public void setIsCreated (boolean IsCreated)
    {
        set_Value ("IsCreated", Boolean.valueOf(IsCreated));
        
    }
    
    /** Get Records created.
    @return Records created */
    public boolean isCreated() 
    {
        return get_ValueAsBoolean("IsCreated");
        
    }
    
    /** Set Lot No.
    @param Lot Lot number (alphanumeric) */
    public void setLot (String Lot)
    {
        set_Value ("Lot", Lot);
        
    }
    
    /** Get Lot No.
    @return Lot number (alphanumeric) */
    public String getLot() 
    {
        return (String)get_Value("Lot");
        
    }
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID <= 0) set_Value ("M_AttributeSetInstance_ID", null);
        else
        set_Value ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get Attribute Set Instance.
    @return Product Attribute Set Instance */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
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
    
    /** Set Unit Price.
    @param PriceActual Actual Price */
    public void setPriceActual (java.math.BigDecimal PriceActual)
    {
        if (PriceActual == null) throw new IllegalArgumentException ("PriceActual is mandatory.");
        set_Value ("PriceActual", PriceActual);
        
    }
    
    /** Get Unit Price.
    @return Actual Price */
    public java.math.BigDecimal getPriceActual() 
    {
        return get_ValueAsBigDecimal("PriceActual");
        
    }
    
    /** Set Product Key.
    @param ProductValue Key of the Product */
    public void setProductValue (String ProductValue)
    {
        if (ProductValue == null) throw new IllegalArgumentException ("ProductValue is mandatory.");
        set_Value ("ProductValue", ProductValue);
        
    }
    
    /** Get Product Key.
    @return Key of the Product */
    public String getProductValue() 
    {
        return (String)get_Value("ProductValue");
        
    }
    
    /** Set Sale Price.
    @param XX_SalePrice Sale Price */
    public void setXX_SalePrice (java.math.BigDecimal XX_SalePrice)
    {
        if (XX_SalePrice == null) throw new IllegalArgumentException ("XX_SalePrice is mandatory.");
        set_Value ("XX_SalePrice", XX_SalePrice);
        
    }
    
    /** Get Sale Price.
    @return Sale Price */
    public java.math.BigDecimal getXX_SalePrice() 
    {
        return get_ValueAsBigDecimal("XX_SalePrice");
        
    }
    
    /** Set XX_VMR_ProductBatch_ID.
    @param XX_VMR_ProductBatch_ID XX_VMR_ProductBatch_ID */
    public void setXX_VMR_ProductBatch_ID (int XX_VMR_ProductBatch_ID)
    {
        if (XX_VMR_ProductBatch_ID < 1) throw new IllegalArgumentException ("XX_VMR_ProductBatch_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_ProductBatch_ID", Integer.valueOf(XX_VMR_ProductBatch_ID));
        
    }
    
    /** Get XX_VMR_ProductBatch_ID.
    @return XX_VMR_ProductBatch_ID */
    public int getXX_VMR_ProductBatch_ID() 
    {
        return get_ValueAsInt("XX_VMR_ProductBatch_ID");
        
    }
    
    
}
