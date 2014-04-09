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
/** Generated Model for M_DiscountSchemaLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_M_DiscountSchemaLine.java 8943 2010-06-14 21:19:58Z kvora $ */
public class X_M_DiscountSchemaLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_DiscountSchemaLine_ID id
    @param trx transaction
    */
    public X_M_DiscountSchemaLine (Ctx ctx, int M_DiscountSchemaLine_ID, Trx trx)
    {
        super (ctx, M_DiscountSchemaLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_DiscountSchemaLine_ID == 0)
        {
            setC_ConversionType_ID (0);
            setConversionDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setLimit_AddAmt (Env.ZERO);
            setLimit_Base (null);	// X
            setLimit_Discount (Env.ZERO);
            setLimit_MaxAmt (Env.ZERO);
            setLimit_MinAmt (Env.ZERO);
            setLimit_Rounding (null);	// C
            setList_AddAmt (Env.ZERO);
            setList_Base (null);	// L
            setList_Discount (Env.ZERO);
            setList_MaxAmt (Env.ZERO);
            setList_MinAmt (Env.ZERO);
            setList_Rounding (null);	// C
            setM_DiscountSchemaLine_ID (0);
            setM_DiscountSchema_ID (0);
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM M_DiscountSchemaLine WHERE M_DiscountSchema_ID=@M_DiscountSchema_ID@
            setStd_AddAmt (Env.ZERO);
            setStd_Base (null);	// S
            setStd_Discount (Env.ZERO);
            setStd_MaxAmt (Env.ZERO);
            setStd_MinAmt (Env.ZERO);
            setStd_Rounding (null);	// C
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_DiscountSchemaLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27558675133789L;
    /** Last Updated Timestamp 2010-06-14 14:10:17.0 */
    public static final long updatedMS = 1276549817000L;
    /** AD_Table_ID=477 */
    public static final int Table_ID=477;
    
    /** TableName=M_DiscountSchemaLine */
    public static final String Table_Name="M_DiscountSchemaLine";
    
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
    
    /** Set Rate Type.
    @param C_ConversionType_ID Currency Conversion Rate Type */
    public void setC_ConversionType_ID (int C_ConversionType_ID)
    {
        if (C_ConversionType_ID < 1) throw new IllegalArgumentException ("C_ConversionType_ID is mandatory.");
        set_Value ("C_ConversionType_ID", Integer.valueOf(C_ConversionType_ID));
        
    }
    
    /** Get Rate Type.
    @return Currency Conversion Rate Type */
    public int getC_ConversionType_ID() 
    {
        return get_ValueAsInt("C_ConversionType_ID");
        
    }
    
    /** Set Conversion Date.
    @param ConversionDate Date for selecting conversion rate */
    public void setConversionDate (Timestamp ConversionDate)
    {
        if (ConversionDate == null) throw new IllegalArgumentException ("ConversionDate is mandatory.");
        set_Value ("ConversionDate", ConversionDate);
        
    }
    
    /** Get Conversion Date.
    @return Date for selecting conversion rate */
    public Timestamp getConversionDate() 
    {
        return (Timestamp)get_Value("ConversionDate");
        
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
    
    /** Set Sales Transaction.
    @param IsSOTrx This is a Sales Transaction */
    public void setIsSOTrx (boolean IsSOTrx)
    {
        throw new IllegalArgumentException ("IsSOTrx is virtual column");
        
    }
    
    /** Get Sales Transaction.
    @return This is a Sales Transaction */
    public boolean isSOTrx() 
    {
        return get_ValueAsBoolean("IsSOTrx");
        
    }
    
    /** Set Limit price Surcharge Amount.
    @param Limit_AddAmt Amount added to the converted/copied price before multiplying */
    public void setLimit_AddAmt (java.math.BigDecimal Limit_AddAmt)
    {
        if (Limit_AddAmt == null) throw new IllegalArgumentException ("Limit_AddAmt is mandatory.");
        set_Value ("Limit_AddAmt", Limit_AddAmt);
        
    }
    
    /** Get Limit price Surcharge Amount.
    @return Amount added to the converted/copied price before multiplying */
    public java.math.BigDecimal getLimit_AddAmt() 
    {
        return get_ValueAsBigDecimal("Limit_AddAmt");
        
    }
    
    /** Fixed Price = F */
    public static final String LIMIT_BASE_FixedPrice = X_Ref_M_DiscountPriceList_Base.FIXED_PRICE.getValue();
    /** List Price = L */
    public static final String LIMIT_BASE_ListPrice = X_Ref_M_DiscountPriceList_Base.LIST_PRICE.getValue();
    /** Standard Price = S */
    public static final String LIMIT_BASE_StandardPrice = X_Ref_M_DiscountPriceList_Base.STANDARD_PRICE.getValue();
    /** Limit (PO) Price = X */
    public static final String LIMIT_BASE_LimitPOPrice = X_Ref_M_DiscountPriceList_Base.LIMIT_PO_PRICE.getValue();
    /** Set Limit price Base.
    @param Limit_Base Base price for calculation of the new price */
    public void setLimit_Base (String Limit_Base)
    {
        if (Limit_Base == null) throw new IllegalArgumentException ("Limit_Base is mandatory");
        if (!X_Ref_M_DiscountPriceList_Base.isValid(Limit_Base))
        throw new IllegalArgumentException ("Limit_Base Invalid value - " + Limit_Base + " - Reference_ID=194 - F - L - S - X");
        set_Value ("Limit_Base", Limit_Base);
        
    }
    
    /** Get Limit price Base.
    @return Base price for calculation of the new price */
    public String getLimit_Base() 
    {
        return (String)get_Value("Limit_Base");
        
    }
    
    /** Set Limit price Discount %.
    @param Limit_Discount Discount in percent to be subtracted from base, if negative it will be added to base price */
    public void setLimit_Discount (java.math.BigDecimal Limit_Discount)
    {
        if (Limit_Discount == null) throw new IllegalArgumentException ("Limit_Discount is mandatory.");
        set_Value ("Limit_Discount", Limit_Discount);
        
    }
    
    /** Get Limit price Discount %.
    @return Discount in percent to be subtracted from base, if negative it will be added to base price */
    public java.math.BigDecimal getLimit_Discount() 
    {
        return get_ValueAsBigDecimal("Limit_Discount");
        
    }
    
    /** Set Fixed Limit Price.
    @param Limit_Fixed Fixed Limit Price (not calculated) */
    public void setLimit_Fixed (java.math.BigDecimal Limit_Fixed)
    {
        set_Value ("Limit_Fixed", Limit_Fixed);
        
    }
    
    /** Get Fixed Limit Price.
    @return Fixed Limit Price (not calculated) */
    public java.math.BigDecimal getLimit_Fixed() 
    {
        return get_ValueAsBigDecimal("Limit_Fixed");
        
    }
    
    /** Set Limit price max Margin.
    @param Limit_MaxAmt Maximum difference to original limit price;
     ignored if zero */
    public void setLimit_MaxAmt (java.math.BigDecimal Limit_MaxAmt)
    {
        if (Limit_MaxAmt == null) throw new IllegalArgumentException ("Limit_MaxAmt is mandatory.");
        set_Value ("Limit_MaxAmt", Limit_MaxAmt);
        
    }
    
    /** Get Limit price max Margin.
    @return Maximum difference to original limit price;
     ignored if zero */
    public java.math.BigDecimal getLimit_MaxAmt() 
    {
        return get_ValueAsBigDecimal("Limit_MaxAmt");
        
    }
    
    /** Set Limit price min Margin.
    @param Limit_MinAmt Minimum difference to original limit price;
     ignored if zero */
    public void setLimit_MinAmt (java.math.BigDecimal Limit_MinAmt)
    {
        if (Limit_MinAmt == null) throw new IllegalArgumentException ("Limit_MinAmt is mandatory.");
        set_Value ("Limit_MinAmt", Limit_MinAmt);
        
    }
    
    /** Get Limit price min Margin.
    @return Minimum difference to original limit price;
     ignored if zero */
    public java.math.BigDecimal getLimit_MinAmt() 
    {
        return get_ValueAsBigDecimal("Limit_MinAmt");
        
    }
    
    /** Whole Number .00 = 0 */
    public static final String LIMIT_ROUNDING_WholeNumber00 = X_Ref_M_DiscountPriceList_RoundingRule.WHOLE_NUMBER00.getValue();
    /** Nickel .05, .10, .15, ... = 5 */
    public static final String LIMIT_ROUNDING_Nickel051015 = X_Ref_M_DiscountPriceList_RoundingRule.NICKEL051015.getValue();
    /** Currency Precision = C */
    public static final String LIMIT_ROUNDING_CurrencyPrecision = X_Ref_M_DiscountPriceList_RoundingRule.CURRENCY_PRECISION.getValue();
    /** Dime .10, .20, .30... = D */
    public static final String LIMIT_ROUNDING_Dime102030 = X_Ref_M_DiscountPriceList_RoundingRule.DIME102030.getValue();
    /** No Rounding = N */
    public static final String LIMIT_ROUNDING_NoRounding = X_Ref_M_DiscountPriceList_RoundingRule.NO_ROUNDING.getValue();
    /** Quarter .25 .50 .75 = Q */
    public static final String LIMIT_ROUNDING_Quarter255075 = X_Ref_M_DiscountPriceList_RoundingRule.QUARTER255075.getValue();
    /** Ten 10.00, 20.00, .. = T */
    public static final String LIMIT_ROUNDING_Ten10002000 = X_Ref_M_DiscountPriceList_RoundingRule.TEN10002000.getValue();
    /** Hundred = h */
    public static final String LIMIT_ROUNDING_Hundred = X_Ref_M_DiscountPriceList_RoundingRule.HUNDRED.getValue();
    /** Thousand = t */
    public static final String LIMIT_ROUNDING_Thousand = X_Ref_M_DiscountPriceList_RoundingRule.THOUSAND.getValue();
    /** Set Limit price Rounding.
    @param Limit_Rounding Rounding of the final result */
    public void setLimit_Rounding (String Limit_Rounding)
    {
        if (Limit_Rounding == null) throw new IllegalArgumentException ("Limit_Rounding is mandatory");
        if (!X_Ref_M_DiscountPriceList_RoundingRule.isValid(Limit_Rounding))
        throw new IllegalArgumentException ("Limit_Rounding Invalid value - " + Limit_Rounding + " - Reference_ID=155 - 0 - 5 - C - D - N - Q - T - h - t");
        set_Value ("Limit_Rounding", Limit_Rounding);
        
    }
    
    /** Get Limit price Rounding.
    @return Rounding of the final result */
    public String getLimit_Rounding() 
    {
        return (String)get_Value("Limit_Rounding");
        
    }
    
    /** Set List price Surcharge Amount.
    @param List_AddAmt List Price Surcharge Amount */
    public void setList_AddAmt (java.math.BigDecimal List_AddAmt)
    {
        if (List_AddAmt == null) throw new IllegalArgumentException ("List_AddAmt is mandatory.");
        set_Value ("List_AddAmt", List_AddAmt);
        
    }
    
    /** Get List price Surcharge Amount.
    @return List Price Surcharge Amount */
    public java.math.BigDecimal getList_AddAmt() 
    {
        return get_ValueAsBigDecimal("List_AddAmt");
        
    }
    
    /** Fixed Price = F */
    public static final String LIST_BASE_FixedPrice = X_Ref_M_DiscountPriceList_Base.FIXED_PRICE.getValue();
    /** List Price = L */
    public static final String LIST_BASE_ListPrice = X_Ref_M_DiscountPriceList_Base.LIST_PRICE.getValue();
    /** Standard Price = S */
    public static final String LIST_BASE_StandardPrice = X_Ref_M_DiscountPriceList_Base.STANDARD_PRICE.getValue();
    /** Limit (PO) Price = X */
    public static final String LIST_BASE_LimitPOPrice = X_Ref_M_DiscountPriceList_Base.LIMIT_PO_PRICE.getValue();
    /** Set List price Base.
    @param List_Base Price used as the basis for price list calculations */
    public void setList_Base (String List_Base)
    {
        if (List_Base == null) throw new IllegalArgumentException ("List_Base is mandatory");
        if (!X_Ref_M_DiscountPriceList_Base.isValid(List_Base))
        throw new IllegalArgumentException ("List_Base Invalid value - " + List_Base + " - Reference_ID=194 - F - L - S - X");
        set_Value ("List_Base", List_Base);
        
    }
    
    /** Get List price Base.
    @return Price used as the basis for price list calculations */
    public String getList_Base() 
    {
        return (String)get_Value("List_Base");
        
    }
    
    /** Set List price Discount %.
    @param List_Discount Discount from list price as a percentage */
    public void setList_Discount (java.math.BigDecimal List_Discount)
    {
        if (List_Discount == null) throw new IllegalArgumentException ("List_Discount is mandatory.");
        set_Value ("List_Discount", List_Discount);
        
    }
    
    /** Get List price Discount %.
    @return Discount from list price as a percentage */
    public java.math.BigDecimal getList_Discount() 
    {
        return get_ValueAsBigDecimal("List_Discount");
        
    }
    
    /** Set Fixed List Price.
    @param List_Fixed Fixes List Price (not calculated) */
    public void setList_Fixed (java.math.BigDecimal List_Fixed)
    {
        set_Value ("List_Fixed", List_Fixed);
        
    }
    
    /** Get Fixed List Price.
    @return Fixes List Price (not calculated) */
    public java.math.BigDecimal getList_Fixed() 
    {
        return get_ValueAsBigDecimal("List_Fixed");
        
    }
    
    /** Set List price max Margin.
    @param List_MaxAmt Maximum margin for a product */
    public void setList_MaxAmt (java.math.BigDecimal List_MaxAmt)
    {
        if (List_MaxAmt == null) throw new IllegalArgumentException ("List_MaxAmt is mandatory.");
        set_Value ("List_MaxAmt", List_MaxAmt);
        
    }
    
    /** Get List price max Margin.
    @return Maximum margin for a product */
    public java.math.BigDecimal getList_MaxAmt() 
    {
        return get_ValueAsBigDecimal("List_MaxAmt");
        
    }
    
    /** Set List price min Margin.
    @param List_MinAmt Minimum margin for a product */
    public void setList_MinAmt (java.math.BigDecimal List_MinAmt)
    {
        if (List_MinAmt == null) throw new IllegalArgumentException ("List_MinAmt is mandatory.");
        set_Value ("List_MinAmt", List_MinAmt);
        
    }
    
    /** Get List price min Margin.
    @return Minimum margin for a product */
    public java.math.BigDecimal getList_MinAmt() 
    {
        return get_ValueAsBigDecimal("List_MinAmt");
        
    }
    
    /** Whole Number .00 = 0 */
    public static final String LIST_ROUNDING_WholeNumber00 = X_Ref_M_DiscountPriceList_RoundingRule.WHOLE_NUMBER00.getValue();
    /** Nickel .05, .10, .15, ... = 5 */
    public static final String LIST_ROUNDING_Nickel051015 = X_Ref_M_DiscountPriceList_RoundingRule.NICKEL051015.getValue();
    /** Currency Precision = C */
    public static final String LIST_ROUNDING_CurrencyPrecision = X_Ref_M_DiscountPriceList_RoundingRule.CURRENCY_PRECISION.getValue();
    /** Dime .10, .20, .30... = D */
    public static final String LIST_ROUNDING_Dime102030 = X_Ref_M_DiscountPriceList_RoundingRule.DIME102030.getValue();
    /** No Rounding = N */
    public static final String LIST_ROUNDING_NoRounding = X_Ref_M_DiscountPriceList_RoundingRule.NO_ROUNDING.getValue();
    /** Quarter .25 .50 .75 = Q */
    public static final String LIST_ROUNDING_Quarter255075 = X_Ref_M_DiscountPriceList_RoundingRule.QUARTER255075.getValue();
    /** Ten 10.00, 20.00, .. = T */
    public static final String LIST_ROUNDING_Ten10002000 = X_Ref_M_DiscountPriceList_RoundingRule.TEN10002000.getValue();
    /** Hundred = h */
    public static final String LIST_ROUNDING_Hundred = X_Ref_M_DiscountPriceList_RoundingRule.HUNDRED.getValue();
    /** Thousand = t */
    public static final String LIST_ROUNDING_Thousand = X_Ref_M_DiscountPriceList_RoundingRule.THOUSAND.getValue();
    /** Set List price Rounding.
    @param List_Rounding Rounding rule for final list price */
    public void setList_Rounding (String List_Rounding)
    {
        if (List_Rounding == null) throw new IllegalArgumentException ("List_Rounding is mandatory");
        if (!X_Ref_M_DiscountPriceList_RoundingRule.isValid(List_Rounding))
        throw new IllegalArgumentException ("List_Rounding Invalid value - " + List_Rounding + " - Reference_ID=155 - 0 - 5 - C - D - N - Q - T - h - t");
        set_Value ("List_Rounding", List_Rounding);
        
    }
    
    /** Get List price Rounding.
    @return Rounding rule for final list price */
    public String getList_Rounding() 
    {
        return (String)get_Value("List_Rounding");
        
    }
    
    /** Set Discount Pricelist.
    @param M_DiscountSchemaLine_ID Line of the pricelist trade discount schema */
    public void setM_DiscountSchemaLine_ID (int M_DiscountSchemaLine_ID)
    {
        if (M_DiscountSchemaLine_ID < 1) throw new IllegalArgumentException ("M_DiscountSchemaLine_ID is mandatory.");
        set_ValueNoCheck ("M_DiscountSchemaLine_ID", Integer.valueOf(M_DiscountSchemaLine_ID));
        
    }
    
    /** Get Discount Pricelist.
    @return Line of the pricelist trade discount schema */
    public int getM_DiscountSchemaLine_ID() 
    {
        return get_ValueAsInt("M_DiscountSchemaLine_ID");
        
    }
    
    /** Set Discount Schema.
    @param M_DiscountSchema_ID Schema to calculate price lists or the trade discount percentage */
    public void setM_DiscountSchema_ID (int M_DiscountSchema_ID)
    {
        if (M_DiscountSchema_ID < 1) throw new IllegalArgumentException ("M_DiscountSchema_ID is mandatory.");
        set_ValueNoCheck ("M_DiscountSchema_ID", Integer.valueOf(M_DiscountSchema_ID));
        
    }
    
    /** Get Discount Schema.
    @return Schema to calculate price lists or the trade discount percentage */
    public int getM_DiscountSchema_ID() 
    {
        return get_ValueAsInt("M_DiscountSchema_ID");
        
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
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
        
    }
    
    /** Set Standard price Surcharge Amount.
    @param Std_AddAmt Amount added to a price as a surcharge */
    public void setStd_AddAmt (java.math.BigDecimal Std_AddAmt)
    {
        if (Std_AddAmt == null) throw new IllegalArgumentException ("Std_AddAmt is mandatory.");
        set_Value ("Std_AddAmt", Std_AddAmt);
        
    }
    
    /** Get Standard price Surcharge Amount.
    @return Amount added to a price as a surcharge */
    public java.math.BigDecimal getStd_AddAmt() 
    {
        return get_ValueAsBigDecimal("Std_AddAmt");
        
    }
    
    /** Fixed Price = F */
    public static final String STD_BASE_FixedPrice = X_Ref_M_DiscountPriceList_Base.FIXED_PRICE.getValue();
    /** List Price = L */
    public static final String STD_BASE_ListPrice = X_Ref_M_DiscountPriceList_Base.LIST_PRICE.getValue();
    /** Standard Price = S */
    public static final String STD_BASE_StandardPrice = X_Ref_M_DiscountPriceList_Base.STANDARD_PRICE.getValue();
    /** Limit (PO) Price = X */
    public static final String STD_BASE_LimitPOPrice = X_Ref_M_DiscountPriceList_Base.LIMIT_PO_PRICE.getValue();
    /** Set Standard price Base.
    @param Std_Base Base price for calculating new standard price */
    public void setStd_Base (String Std_Base)
    {
        if (Std_Base == null) throw new IllegalArgumentException ("Std_Base is mandatory");
        if (!X_Ref_M_DiscountPriceList_Base.isValid(Std_Base))
        throw new IllegalArgumentException ("Std_Base Invalid value - " + Std_Base + " - Reference_ID=194 - F - L - S - X");
        set_Value ("Std_Base", Std_Base);
        
    }
    
    /** Get Standard price Base.
    @return Base price for calculating new standard price */
    public String getStd_Base() 
    {
        return (String)get_Value("Std_Base");
        
    }
    
    /** Set Standard price Discount %.
    @param Std_Discount Discount percentage to subtract from base price */
    public void setStd_Discount (java.math.BigDecimal Std_Discount)
    {
        if (Std_Discount == null) throw new IllegalArgumentException ("Std_Discount is mandatory.");
        set_Value ("Std_Discount", Std_Discount);
        
    }
    
    /** Get Standard price Discount %.
    @return Discount percentage to subtract from base price */
    public java.math.BigDecimal getStd_Discount() 
    {
        return get_ValueAsBigDecimal("Std_Discount");
        
    }
    
    /** Set Fixed Standard Price.
    @param Std_Fixed Fixed Standard Price (not calculated) */
    public void setStd_Fixed (java.math.BigDecimal Std_Fixed)
    {
        set_Value ("Std_Fixed", Std_Fixed);
        
    }
    
    /** Get Fixed Standard Price.
    @return Fixed Standard Price (not calculated) */
    public java.math.BigDecimal getStd_Fixed() 
    {
        return get_ValueAsBigDecimal("Std_Fixed");
        
    }
    
    /** Set Standard max Margin.
    @param Std_MaxAmt Maximum margin allowed for a product */
    public void setStd_MaxAmt (java.math.BigDecimal Std_MaxAmt)
    {
        if (Std_MaxAmt == null) throw new IllegalArgumentException ("Std_MaxAmt is mandatory.");
        set_Value ("Std_MaxAmt", Std_MaxAmt);
        
    }
    
    /** Get Standard max Margin.
    @return Maximum margin allowed for a product */
    public java.math.BigDecimal getStd_MaxAmt() 
    {
        return get_ValueAsBigDecimal("Std_MaxAmt");
        
    }
    
    /** Set Standard price min Margin.
    @param Std_MinAmt Minimum margin allowed for a product */
    public void setStd_MinAmt (java.math.BigDecimal Std_MinAmt)
    {
        if (Std_MinAmt == null) throw new IllegalArgumentException ("Std_MinAmt is mandatory.");
        set_Value ("Std_MinAmt", Std_MinAmt);
        
    }
    
    /** Get Standard price min Margin.
    @return Minimum margin allowed for a product */
    public java.math.BigDecimal getStd_MinAmt() 
    {
        return get_ValueAsBigDecimal("Std_MinAmt");
        
    }
    
    /** Whole Number .00 = 0 */
    public static final String STD_ROUNDING_WholeNumber00 = X_Ref_M_DiscountPriceList_RoundingRule.WHOLE_NUMBER00.getValue();
    /** Nickel .05, .10, .15, ... = 5 */
    public static final String STD_ROUNDING_Nickel051015 = X_Ref_M_DiscountPriceList_RoundingRule.NICKEL051015.getValue();
    /** Currency Precision = C */
    public static final String STD_ROUNDING_CurrencyPrecision = X_Ref_M_DiscountPriceList_RoundingRule.CURRENCY_PRECISION.getValue();
    /** Dime .10, .20, .30... = D */
    public static final String STD_ROUNDING_Dime102030 = X_Ref_M_DiscountPriceList_RoundingRule.DIME102030.getValue();
    /** No Rounding = N */
    public static final String STD_ROUNDING_NoRounding = X_Ref_M_DiscountPriceList_RoundingRule.NO_ROUNDING.getValue();
    /** Quarter .25 .50 .75 = Q */
    public static final String STD_ROUNDING_Quarter255075 = X_Ref_M_DiscountPriceList_RoundingRule.QUARTER255075.getValue();
    /** Ten 10.00, 20.00, .. = T */
    public static final String STD_ROUNDING_Ten10002000 = X_Ref_M_DiscountPriceList_RoundingRule.TEN10002000.getValue();
    /** Hundred = h */
    public static final String STD_ROUNDING_Hundred = X_Ref_M_DiscountPriceList_RoundingRule.HUNDRED.getValue();
    /** Thousand = t */
    public static final String STD_ROUNDING_Thousand = X_Ref_M_DiscountPriceList_RoundingRule.THOUSAND.getValue();
    /** Set Standard price Rounding.
    @param Std_Rounding Rounding rule for calculated price */
    public void setStd_Rounding (String Std_Rounding)
    {
        if (Std_Rounding == null) throw new IllegalArgumentException ("Std_Rounding is mandatory");
        if (!X_Ref_M_DiscountPriceList_RoundingRule.isValid(Std_Rounding))
        throw new IllegalArgumentException ("Std_Rounding Invalid value - " + Std_Rounding + " - Reference_ID=155 - 0 - 5 - C - D - N - Q - T - h - t");
        set_Value ("Std_Rounding", Std_Rounding);
        
    }
    
    /** Get Standard price Rounding.
    @return Rounding rule for calculated price */
    public String getStd_Rounding() 
    {
        return (String)get_Value("Std_Rounding");
        
    }
    
    
}
