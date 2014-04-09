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
package compiere.model.promociones;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_PromoConditionValue
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_PromoConditionValue extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_PromoConditionValue_ID id
    @param trx transaction
    */
    public X_XX_VMR_PromoConditionValue (Ctx ctx, int XX_VMR_PromoConditionValue_ID, Trx trx)
    {
        super (ctx, XX_VMR_PromoConditionValue_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_PromoConditionValue_ID == 0)
        {
            setXX_VMR_PromoConditionValue_ID (0);
            setXX_VMR_Promotion_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_PromoConditionValue (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27633408465789L;
    /** Last Updated Timestamp 2012-10-26 15:55:49.0 */
    public static final long updatedMS = 1351283149000L;
    /** AD_Table_ID=1003154 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_PromoConditionValue");
        
    }
    ;
    
    /** TableName=XX_VMR_PromoConditionValue */
    public static final String Table_Name="XX_VMR_PromoConditionValue";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Descripción.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Descripción.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
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
    
    /** Set Amount Gifted.
    @param XX_AmountGifted Amount Gifted */
    public void setXX_AmountGifted (int XX_AmountGifted)
    {
        set_Value ("XX_AmountGifted", Integer.valueOf(XX_AmountGifted));
        
    }
    
    /** Get Amount Gifted.
    @return Amount Gifted */
    public int getXX_AmountGifted() 
    {
        return get_ValueAsInt("XX_AmountGifted");
        
    }
    
    /** Set Final Price.
    @param XX_ComboAmount Final Price */
    public void setXX_ComboAmount (java.math.BigDecimal XX_ComboAmount)
    {
        set_Value ("XX_ComboAmount", XX_ComboAmount);
        
    }
    
    /** Get Final Price.
    @return Final Price */
    public java.math.BigDecimal getXX_ComboAmount() 
    {
        return get_ValueAsBigDecimal("XX_ComboAmount");
        
    }
    
    /** Set Conditions.
    @param XX_Conditions Conditions */
    public void setXX_Conditions (String XX_Conditions)
    {
        set_Value ("XX_Conditions", XX_Conditions);
        
    }
    
    /** Get Conditions.
    @return Conditions */
    public String getXX_Conditions() 
    {
        return (String)get_Value("XX_Conditions");
        
    }
    
    /** Set Discount Amount.
    @param XX_DiscountAmount Discount Amount */
    public void setXX_DiscountAmount (java.math.BigDecimal XX_DiscountAmount)
    {
        set_Value ("XX_DiscountAmount", XX_DiscountAmount);
        
    }
    
    /** Get Discount Amount.
    @return Discount Amount */
    public java.math.BigDecimal getXX_DiscountAmount() 
    {
        return get_ValueAsBigDecimal("XX_DiscountAmount");
        
    }
    
    /** Set Discount Rate.
    @param XX_DiscountRate Discount Rate */
    public void setXX_DiscountRate (java.math.BigDecimal XX_DiscountRate)
    {
        set_Value ("XX_DiscountRate", XX_DiscountRate);
        
    }
    
    /** Get Discount Rate.
    @return Discount Rate */
    public java.math.BigDecimal getXX_DiscountRate() 
    {
        return get_ValueAsBigDecimal("XX_DiscountRate");
        
    }
    
    /** Set Gift.
    @param XX_Gift Gift */
    public void setXX_Gift (String XX_Gift)
    {
        set_Value ("XX_Gift", XX_Gift);
        
    }
    
    /** Get Gift.
    @return Gift */
    public String getXX_Gift() 
    {
        return (String)get_Value("XX_Gift");
        
    }
    
    /** Set Gift Accumulate.
    @param XX_GiftAccumulate Gift Accumulate */
    public void setXX_GiftAccumulate (boolean XX_GiftAccumulate)
    {
        set_Value ("XX_GiftAccumulate", Boolean.valueOf(XX_GiftAccumulate));
        
    }
    
    /** Get Gift Accumulate.
    @return Gift Accumulate */
    public boolean isXX_GiftAccumulate() 
    {
        return get_ValueAsBoolean("XX_GiftAccumulate");
        
    }
    
    /** Set Group Discount.
    @param XX_GroupDiscount Group Discount */
    public void setXX_GroupDiscount (int XX_GroupDiscount)
    {
        set_Value ("XX_GroupDiscount", Integer.valueOf(XX_GroupDiscount));
        
    }
    
    /** Get Group Discount.
    @return Group Discount */
    public int getXX_GroupDiscount() 
    {
        return get_ValueAsInt("XX_GroupDiscount");
        
    }
    
    /** Set Minimum Purchase.
    @param XX_MinimumPurchase Minimum Purchase */
    public void setXX_MinimumPurchase (java.math.BigDecimal XX_MinimumPurchase)
    {
        set_Value ("XX_MinimumPurchase", XX_MinimumPurchase);
        
    }
    
    /** Get Minimum Purchase.
    @return Minimum Purchase */
    public java.math.BigDecimal getXX_MinimumPurchase() 
    {
        return get_ValueAsBigDecimal("XX_MinimumPurchase");
        
    }
    
    /** Set Modify Promotion.
    @param XX_ModifyPromotion Modify Promotion */
    public void setXX_ModifyPromotion (String XX_ModifyPromotion)
    {
        set_Value ("XX_ModifyPromotion", XX_ModifyPromotion);
        
    }
    
    /** Get Modify Promotion.
    @return Modify Promotion */
    public String getXX_ModifyPromotion() 
    {
        return (String)get_Value("XX_ModifyPromotion");
        
    }
    
    /** Set Product Selection.
    @param XX_ProductSelection Product Selection */
    public void setXX_ProductSelection (String XX_ProductSelection)
    {
        set_Value ("XX_ProductSelection", XX_ProductSelection);
        
    }
    
    /** Get Product Selection.
    @return Product Selection */
    public String getXX_ProductSelection() 
    {
        return (String)get_Value("XX_ProductSelection");
        
    }
    
    /** Set Quantity Purchase.
    @param XX_QuantityPurchase Quantity Purchase */
    public void setXX_QuantityPurchase (java.math.BigDecimal XX_QuantityPurchase)
    {
        set_Value ("XX_QuantityPurchase", XX_QuantityPurchase);
        
    }
    
    /** Get Quantity Purchase.
    @return Quantity Purchase */
    public java.math.BigDecimal getXX_QuantityPurchase() 
    {
        return get_ValueAsBigDecimal("XX_QuantityPurchase");
        
    }
    
    /** Set Promo Condition Value ID.
    @param XX_VMR_PromoConditionValue_ID Promo Condition Value ID */
    public void setXX_VMR_PromoConditionValue_ID (int XX_VMR_PromoConditionValue_ID)
    {
        if (XX_VMR_PromoConditionValue_ID < 1) throw new IllegalArgumentException ("XX_VMR_PromoConditionValue_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PromoConditionValue_ID", Integer.valueOf(XX_VMR_PromoConditionValue_ID));
        
    }
    
    /** Get Promo Condition Value ID.
    @return Promo Condition Value ID */
    public int getXX_VMR_PromoConditionValue_ID() 
    {
        return get_ValueAsInt("XX_VMR_PromoConditionValue_ID");
        
    }
    
    /** Set Promo Cond Warehouse.
    @param XX_VMR_PromoCondWarehouse Promo Cond Warehouse */
    public void setXX_VMR_PromoCondWarehouse (String XX_VMR_PromoCondWarehouse)
    {
        set_Value ("XX_VMR_PromoCondWarehouse", XX_VMR_PromoCondWarehouse);
        
    }
    
    /** Get Promo Cond Warehouse.
    @return Promo Cond Warehouse */
    public String getXX_VMR_PromoCondWarehouse() 
    {
        return (String)get_Value("XX_VMR_PromoCondWarehouse");
        
    }
    
    /** Set Promotion ID.
    @param XX_VMR_Promotion_ID Promotion ID */
    public void setXX_VMR_Promotion_ID (int XX_VMR_Promotion_ID)
    {
        if (XX_VMR_Promotion_ID < 1) throw new IllegalArgumentException ("XX_VMR_Promotion_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_Promotion_ID", Integer.valueOf(XX_VMR_Promotion_ID));
        
    }
    
    /** Get Promotion ID.
    @return Promotion ID */
    public int getXX_VMR_Promotion_ID() 
    {
        return get_ValueAsInt("XX_VMR_Promotion_ID");
        
    }
    
    
}
