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
/** Generated Model for XX_VMR_DiscountAppliDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_DiscountAppliDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_DiscountAppliDetail_ID id
    @param trx transaction
    */
    public X_XX_VMR_DiscountAppliDetail (Ctx ctx, int XX_VMR_DiscountAppliDetail_ID, Trx trx)
    {
        super (ctx, XX_VMR_DiscountAppliDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_DiscountAppliDetail_ID == 0)
        {
            setXX_VMR_DiscountAppliDetail_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_DiscountAppliDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27617659008789L;
    /** Last Updated Timestamp 2012-04-27 09:04:52.0 */
    public static final long updatedMS = 1335533692000L;
    /** AD_Table_ID=1000332 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_DiscountAppliDetail");
        
    }
    ;
    
    /** TableName=XX_VMR_DiscountAppliDetail */
    public static final String Table_Name="XX_VMR_DiscountAppliDetail";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_ValueNoCheck ("M_Product_ID", null);
        else
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Porcentaje de descuento.
    @param PorcDescuento Porcentaje de descuento */
    public void setPorcDescuento (int PorcDescuento)
    {
        throw new IllegalArgumentException ("PorcDescuento is virtual column");
        
    }
    
    /** Get Porcentaje de descuento.
    @return Porcentaje de descuento */
    public int getPorcDescuento() 
    {
        return get_ValueAsInt("PorcDescuento");
        
    }
    
    /** Set Product Key.
    @param ProductValue Key of the Product */
    public void setProductValue (String ProductValue)
    {
        throw new IllegalArgumentException ("ProductValue is virtual column");
        
    }
    
    /** Get Product Key.
    @return Key of the Product */
    public String getProductValue() 
    {
        return (String)get_Value("ProductValue");
        
    }
    
    /** Set Amount Rebated.
    @param XX_AmountRebated Amount Rebated */
    public void setXX_AmountRebated (java.math.BigDecimal XX_AmountRebated)
    {
        set_ValueNoCheck ("XX_AmountRebated", XX_AmountRebated);
        
    }
    
    /** Get Amount Rebated.
    @return Amount Rebated */
    public java.math.BigDecimal getXX_AmountRebated() 
    {
        return get_ValueAsBigDecimal("XX_AmountRebated");
        
    }
    
    /** Set Discount Price.
    @param XX_DiscountPrice Discount Price */
    public void setXX_DiscountPrice (java.math.BigDecimal XX_DiscountPrice)
    {
        set_Value ("XX_DiscountPrice", XX_DiscountPrice);
        
    }
    
    /** Get Discount Price.
    @return Discount Price */
    public java.math.BigDecimal getXX_DiscountPrice() 
    {
        return get_ValueAsBigDecimal("XX_DiscountPrice");
        
    }
    
    /** Set Lowering Quantity.
    @param XX_LoweringQuantity Lowering Quantity */
    public void setXX_LoweringQuantity (int XX_LoweringQuantity)
    {
        set_Value ("XX_LoweringQuantity", Integer.valueOf(XX_LoweringQuantity));
        
    }
    
    /** Get Lowering Quantity.
    @return Lowering Quantity */
    public int getXX_LoweringQuantity() 
    {
        return get_ValueAsInt("XX_LoweringQuantity");
        
    }
    
    /** Set Price Before Discount.
    @param XX_PriceBeforeDiscount Price Before Discount */
    public void setXX_PriceBeforeDiscount (java.math.BigDecimal XX_PriceBeforeDiscount)
    {
        set_ValueNoCheck ("XX_PriceBeforeDiscount", XX_PriceBeforeDiscount);
        
    }
    
    /** Get Price Before Discount.
    @return Price Before Discount */
    public java.math.BigDecimal getXX_PriceBeforeDiscount() 
    {
        return get_ValueAsBigDecimal("XX_PriceBeforeDiscount");
        
    }
    
    /** Set Consecutive Price After Discount.
    @param XX_PriceConsecutive_ID Consecutive Price After Discount */
    public void setXX_PriceConsecutive_ID (int XX_PriceConsecutive_ID)
    {
        if (XX_PriceConsecutive_ID <= 0) set_ValueNoCheck ("XX_PriceConsecutive_ID", null);
        else
        set_ValueNoCheck ("XX_PriceConsecutive_ID", Integer.valueOf(XX_PriceConsecutive_ID));
        
    }
    
    /** Get Consecutive Price After Discount.
    @return Consecutive Price After Discount */
    public int getXX_PriceConsecutive_ID() 
    {
        return get_ValueAsInt("XX_PriceConsecutive_ID");
        
    }
    
    /** Set Sale Price Plus Tax.
    @param XX_SalePricePlusTax Sale Price Plus Tax */
    public void setXX_SalePricePlusTax (java.math.BigDecimal XX_SalePricePlusTax)
    {
        set_Value ("XX_SalePricePlusTax", XX_SalePricePlusTax);
        
    }
    
    /** Get Sale Price Plus Tax.
    @return Sale Price Plus Tax */
    public java.math.BigDecimal getXX_SalePricePlusTax() 
    {
        return get_ValueAsBigDecimal("XX_SalePricePlusTax");
        
    }
    
    /** Set Spending Of Discount.
    @param XX_SpendingOfDiscount Spending Of Discount */
    public void setXX_SpendingOfDiscount (java.math.BigDecimal XX_SpendingOfDiscount)
    {
        set_Value ("XX_SpendingOfDiscount", XX_SpendingOfDiscount);
        
    }
    
    /** Get Spending Of Discount.
    @return Spending Of Discount */
    public java.math.BigDecimal getXX_SpendingOfDiscount() 
    {
        return get_ValueAsBigDecimal("XX_SpendingOfDiscount");
        
    }
    
    /** Set Synchronized.
    @param XX_Synchronized Indica si el registro ya fue exportado */
    public void setXX_Synchronized (boolean XX_Synchronized)
    {
        set_Value ("XX_Synchronized", Boolean.valueOf(XX_Synchronized));
        
    }
    
    /** Get Synchronized.
    @return Indica si el registro ya fue exportado */
    public boolean isXX_Synchronized() 
    {
        return get_ValueAsBoolean("XX_Synchronized");
        
    }
    
    /** Set XX_Tax.
    @param XX_Tax XX_Tax */
    public void setXX_Tax (java.math.BigDecimal XX_Tax)
    {
        set_Value ("XX_Tax", XX_Tax);
        
    }
    
    /** Get XX_Tax.
    @return XX_Tax */
    public java.math.BigDecimal getXX_Tax() 
    {
        return get_ValueAsBigDecimal("XX_Tax");
        
    }
    
    /** Set Total Price.
    @param XX_TotalPrice Total Price */
    public void setXX_TotalPrice (java.math.BigDecimal XX_TotalPrice)
    {
        set_Value ("XX_TotalPrice", XX_TotalPrice);
        
    }
    
    /** Get Total Price.
    @return Total Price */
    public java.math.BigDecimal getXX_TotalPrice() 
    {
        return get_ValueAsBigDecimal("XX_TotalPrice");
        
    }
    
    /** Set XX_VMR_DiscountAppliDetail_ID.
    @param XX_VMR_DiscountAppliDetail_ID XX_VMR_DiscountAppliDetail_ID */
    public void setXX_VMR_DiscountAppliDetail_ID (int XX_VMR_DiscountAppliDetail_ID)
    {
        if (XX_VMR_DiscountAppliDetail_ID < 1) throw new IllegalArgumentException ("XX_VMR_DiscountAppliDetail_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DiscountAppliDetail_ID", Integer.valueOf(XX_VMR_DiscountAppliDetail_ID));
        
    }
    
    /** Get XX_VMR_DiscountAppliDetail_ID.
    @return XX_VMR_DiscountAppliDetail_ID */
    public int getXX_VMR_DiscountAppliDetail_ID() 
    {
        return get_ValueAsInt("XX_VMR_DiscountAppliDetail_ID");
        
    }
    
    /** Set XX_VMR_DiscountRequest_ID.
    @param XX_VMR_DiscountRequest_ID XX_VMR_DiscountRequest_ID */
    public void setXX_VMR_DiscountRequest_ID (int XX_VMR_DiscountRequest_ID)
    {
        if (XX_VMR_DiscountRequest_ID <= 0) set_Value ("XX_VMR_DiscountRequest_ID", null);
        else
        set_Value ("XX_VMR_DiscountRequest_ID", Integer.valueOf(XX_VMR_DiscountRequest_ID));
        
    }
    
    /** Get XX_VMR_DiscountRequest_ID.
    @return XX_VMR_DiscountRequest_ID */
    public int getXX_VMR_DiscountRequest_ID() 
    {
        return get_ValueAsInt("XX_VMR_DiscountRequest_ID");
        
    }
    
    /** Set Discount Type.
    @param XX_VMR_DiscountType_ID Discount Type */
    public void setXX_VMR_DiscountType_ID (int XX_VMR_DiscountType_ID)
    {
        if (XX_VMR_DiscountType_ID <= 0) set_Value ("XX_VMR_DiscountType_ID", null);
        else
        set_Value ("XX_VMR_DiscountType_ID", Integer.valueOf(XX_VMR_DiscountType_ID));
        
    }
    
    /** Get Discount Type.
    @return Discount Type */
    public int getXX_VMR_DiscountType_ID() 
    {
        return get_ValueAsInt("XX_VMR_DiscountType_ID");
        
    }
    
    /** Set Line.
    @param XX_VMR_Line_ID Line */
    public void setXX_VMR_Line_ID (int XX_VMR_Line_ID)
    {
        if (XX_VMR_Line_ID <= 0) set_Value ("XX_VMR_Line_ID", null);
        else
        set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    /** Set PriceConsecutive.
    @param XX_VMR_PriceConsecutive_ID PriceConsecutive */
    public void setXX_VMR_PriceConsecutive_ID (int XX_VMR_PriceConsecutive_ID)
    {
        if (XX_VMR_PriceConsecutive_ID <= 0) set_ValueNoCheck ("XX_VMR_PriceConsecutive_ID", null);
        else
        set_ValueNoCheck ("XX_VMR_PriceConsecutive_ID", Integer.valueOf(XX_VMR_PriceConsecutive_ID));
        
    }
    
    /** Get PriceConsecutive.
    @return PriceConsecutive */
    public int getXX_VMR_PriceConsecutive_ID() 
    {
        return get_ValueAsInt("XX_VMR_PriceConsecutive_ID");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        if (XX_VMR_Section_ID <= 0) set_Value ("XX_VMR_Section_ID", null);
        else
        set_Value ("XX_VMR_Section_ID", Integer.valueOf(XX_VMR_Section_ID));
        
    }
    
    /** Get Section.
    @return Section */
    public int getXX_VMR_Section_ID() 
    {
        return get_ValueAsInt("XX_VMR_Section_ID");
        
    }
    
    
}
