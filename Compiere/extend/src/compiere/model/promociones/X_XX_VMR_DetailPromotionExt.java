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
/** Generated Model for XX_VMR_DetailPromotionExt
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_DetailPromotionExt extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_DetailPromotionExt_ID id
    @param trx transaction
    */
    public X_XX_VMR_DetailPromotionExt (Ctx ctx, int XX_VMR_DetailPromotionExt_ID, Trx trx)
    {
        super (ctx, XX_VMR_DetailPromotionExt_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_DetailPromotionExt_ID == 0)
        {
            setXX_VMR_DetailPromotionExt_ID (0);
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
    public X_XX_VMR_DetailPromotionExt (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27621821944789L;
    /** Last Updated Timestamp 2012-06-14 13:27:08.0 */
    public static final long updatedMS = 1339696628000L;
    /** AD_Table_ID=1000405 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_DetailPromotionExt");
        
    }
    ;
    
    /** TableName=XX_VMR_DetailPromotionExt */
    public static final String Table_Name="XX_VMR_DetailPromotionExt";
    
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
    
    /** Set Nombre.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        throw new IllegalArgumentException ("Name is virtual column");
        
    }
    
    /** Get Nombre.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
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
    public void setXX_QuantityPurchase (int XX_QuantityPurchase)
    {
        set_Value ("XX_QuantityPurchase", Integer.valueOf(XX_QuantityPurchase));
        
    }
    
    /** Get Quantity Purchase.
    @return Quantity Purchase */
    public int getXX_QuantityPurchase() 
    {
        return get_ValueAsInt("XX_QuantityPurchase");
        
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
    
    /** Set Type of Promotion.
    @param XX_TypePromotion Type of Promotion */
    public void setXX_TypePromotion (String XX_TypePromotion)
    {
        throw new IllegalArgumentException ("XX_TypePromotion is virtual column");
        
    }
    
    /** Get Type of Promotion.
    @return Type of Promotion */
    public String getXX_TypePromotion() 
    {
        return (String)get_Value("XX_TypePromotion");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        if (XX_VMR_Brand_ID <= 0) set_Value ("XX_VMR_Brand_ID", null);
        else
        set_Value ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
    }
    
    /** Set Category.
    @param XX_VMR_Category_ID Category */
    public void setXX_VMR_Category_ID (int XX_VMR_Category_ID)
    {
        if (XX_VMR_Category_ID <= 0) set_Value ("XX_VMR_Category_ID", null);
        else
        set_Value ("XX_VMR_Category_ID", Integer.valueOf(XX_VMR_Category_ID));
        
    }
    
    /** Get Category.
    @return Category */
    public int getXX_VMR_Category_ID() 
    {
        return get_ValueAsInt("XX_VMR_Category_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID <= 0) set_Value ("XX_VMR_Department_ID", null);
        else
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set Detail Promotion Extended ID.
    @param XX_VMR_DetailPromotionExt_ID Detail Promotion Extended ID */
    public void setXX_VMR_DetailPromotionExt_ID (int XX_VMR_DetailPromotionExt_ID)
    {
        if (XX_VMR_DetailPromotionExt_ID < 1) throw new IllegalArgumentException ("XX_VMR_DetailPromotionExt_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DetailPromotionExt_ID", Integer.valueOf(XX_VMR_DetailPromotionExt_ID));
        
    }
    
    /** Get Detail Promotion Extended ID.
    @return Detail Promotion Extended ID */
    public int getXX_VMR_DetailPromotionExt_ID() 
    {
        return get_ValueAsInt("XX_VMR_DetailPromotionExt_ID");
        
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
        if (XX_VMR_PriceConsecutive_ID <= 0) set_Value ("XX_VMR_PriceConsecutive_ID", null);
        else
        set_Value ("XX_VMR_PriceConsecutive_ID", Integer.valueOf(XX_VMR_PriceConsecutive_ID));
        
    }
    
    /** Get PriceConsecutive.
    @return PriceConsecutive */
    public int getXX_VMR_PriceConsecutive_ID() 
    {
        return get_ValueAsInt("XX_VMR_PriceConsecutive_ID");
        
    }
    
    /** Set Promo Condition Value.
    @param XX_VMR_PromoConditionValue_ID Promo Condition Value */
    public void setXX_VMR_PromoConditionValue_ID (int XX_VMR_PromoConditionValue_ID)
    {
        if (XX_VMR_PromoConditionValue_ID < 1) throw new IllegalArgumentException ("XX_VMR_PromoConditionValue_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PromoConditionValue_ID", Integer.valueOf(XX_VMR_PromoConditionValue_ID));
        
    }
    
    /** Get Promo Condition Value.
    @return Promo Condition Value */
    public int getXX_VMR_PromoConditionValue_ID() 
    {
        return get_ValueAsInt("XX_VMR_PromoConditionValue_ID");
        
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
    
    /** Set Vendor Product Reference.
    @param XX_VMR_VendorProdRef_ID Vendor Product Reference */
    public void setXX_VMR_VendorProdRef_ID (int XX_VMR_VendorProdRef_ID)
    {
        if (XX_VMR_VendorProdRef_ID <= 0) set_Value ("XX_VMR_VendorProdRef_ID", null);
        else
        set_Value ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
        
    }
    
    /** All stores = 000 */
    public static final String XX_WAREHOUSEBECONUMBER_AllStores = X_Ref_XX_WarehousePromotion.ALL_STORES.getValue();
    /** 01- Laboratorio = 001 */
    public static final String XX_WAREHOUSEBECONUMBER_01_Laboratorio = X_Ref_XX_WarehousePromotion._01__LABORATORIO.getValue();
    /** 02 - Puente Yanes = 002 */
    public static final String XX_WAREHOUSEBECONUMBER_02_PuenteYanes = X_Ref_XX_WarehousePromotion._02__PUENTE_YANES.getValue();
    /** 03 - Chacaito = 003 */
    public static final String XX_WAREHOUSEBECONUMBER_03_Chacaito = X_Ref_XX_WarehousePromotion._03__CHACAITO.getValue();
    /** 07 - Tamanaco = 007 */
    public static final String XX_WAREHOUSEBECONUMBER_07_Tamanaco = X_Ref_XX_WarehousePromotion._07__TAMANACO.getValue();
    /** 09 - La Granja = 009 */
    public static final String XX_WAREHOUSEBECONUMBER_09_LaGranja = X_Ref_XX_WarehousePromotion._09__LA_GRANJA.getValue();
    /** 10 - Las Trinitarias = 010 */
    public static final String XX_WAREHOUSEBECONUMBER_10_LasTrinitarias = X_Ref_XX_WarehousePromotion._10__LAS_TRINITARIAS.getValue();
    /** 15 - La Trinidad = 015 */
    public static final String XX_WAREHOUSEBECONUMBER_15_LaTrinidad = X_Ref_XX_WarehousePromotion._15__LA_TRINIDAD.getValue();
    /** 16 - Maracaibo = 016 */
    public static final String XX_WAREHOUSEBECONUMBER_16_Maracaibo = X_Ref_XX_WarehousePromotion._16__MARACAIBO.getValue();
    /** 17 - Millennium = 017 */
    public static final String XX_WAREHOUSEBECONUMBER_17_Millennium = X_Ref_XX_WarehousePromotion._17__MILLENNIUM.getValue();
    /** Set Store Number.
    @param XX_WarehouseBecoNumber Store Number */
    public void setXX_WarehouseBecoNumber (String XX_WarehouseBecoNumber)
    {
        if (!X_Ref_XX_WarehousePromotion.isValid(XX_WarehouseBecoNumber))
        throw new IllegalArgumentException ("XX_WarehouseBecoNumber Invalid value - " + XX_WarehouseBecoNumber + " - Reference_ID=1000310 - 000 - 001 - 002 - 003 - 007 - 009 - 010 - 015 - 016 - 017");
        set_Value ("XX_WarehouseBecoNumber", XX_WarehouseBecoNumber);
        
    }
    
    /** Get Store Number.
    @return Store Number */
    public String getXX_WarehouseBecoNumber() 
    {
        return (String)get_Value("XX_WarehouseBecoNumber");
        
    }
    
    
}
