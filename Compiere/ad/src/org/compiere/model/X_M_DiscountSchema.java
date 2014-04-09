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
/** Generated Model for M_DiscountSchema
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_DiscountSchema.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_DiscountSchema extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_DiscountSchema_ID id
    @param trx transaction
    */
    public X_M_DiscountSchema (Ctx ctx, int M_DiscountSchema_ID, Trx trx)
    {
        super (ctx, M_DiscountSchema_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_DiscountSchema_ID == 0)
        {
            setDiscountType (null);
            setIsBPartnerFlatDiscount (false);
            setIsQuantityBased (true);	// Y
            setM_DiscountSchema_ID (0);
            setName (null);
            setValidFrom (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_DiscountSchema (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=475 */
    public static final int Table_ID=475;
    
    /** TableName=M_DiscountSchema */
    public static final String Table_Name="M_DiscountSchema";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Line = L */
    public static final String CUMULATIVELEVEL_Line = X_Ref_M_Discount_CumulativeLevel.LINE.getValue();
    /** Set Accumulation Level.
    @param CumulativeLevel Level for accumulative calculations */
    public void setCumulativeLevel (String CumulativeLevel)
    {
        if (!X_Ref_M_Discount_CumulativeLevel.isValid(CumulativeLevel))
        throw new IllegalArgumentException ("CumulativeLevel Invalid value - " + CumulativeLevel + " - Reference_ID=246 - L");
        set_Value ("CumulativeLevel", CumulativeLevel);
        
    }
    
    /** Get Accumulation Level.
    @return Level for accumulative calculations */
    public String getCumulativeLevel() 
    {
        return (String)get_Value("CumulativeLevel");
        
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
    
    /** Breaks = B */
    public static final String DISCOUNTTYPE_Breaks = X_Ref_M_Discount_Type.BREAKS.getValue();
    /** Flat Percent = F */
    public static final String DISCOUNTTYPE_FlatPercent = X_Ref_M_Discount_Type.FLAT_PERCENT.getValue();
    /** Pricelist = P */
    public static final String DISCOUNTTYPE_Pricelist = X_Ref_M_Discount_Type.PRICELIST.getValue();
    /** Formula = S */
    public static final String DISCOUNTTYPE_Formula = X_Ref_M_Discount_Type.FORMULA.getValue();
    /** Set Discount Type.
    @param DiscountType Type of trade discount calculation */
    public void setDiscountType (String DiscountType)
    {
        if (DiscountType == null) throw new IllegalArgumentException ("DiscountType is mandatory");
        if (!X_Ref_M_Discount_Type.isValid(DiscountType))
        throw new IllegalArgumentException ("DiscountType Invalid value - " + DiscountType + " - Reference_ID=247 - B - F - P - S");
        set_Value ("DiscountType", DiscountType);
        
    }
    
    /** Get Discount Type.
    @return Type of trade discount calculation */
    public String getDiscountType() 
    {
        return (String)get_Value("DiscountType");
        
    }
    
    /** Set Flat Discount %.
    @param FlatDiscount Flat discount percentage */
    public void setFlatDiscount (java.math.BigDecimal FlatDiscount)
    {
        set_Value ("FlatDiscount", FlatDiscount);
        
    }
    
    /** Get Flat Discount %.
    @return Flat discount percentage */
    public java.math.BigDecimal getFlatDiscount() 
    {
        return get_ValueAsBigDecimal("FlatDiscount");
        
    }
    
    /** Set B.Partner Flat Discount.
    @param IsBPartnerFlatDiscount Use flat discount defined on Business Partner Level */
    public void setIsBPartnerFlatDiscount (boolean IsBPartnerFlatDiscount)
    {
        set_Value ("IsBPartnerFlatDiscount", Boolean.valueOf(IsBPartnerFlatDiscount));
        
    }
    
    /** Get B.Partner Flat Discount.
    @return Use flat discount defined on Business Partner Level */
    public boolean isBPartnerFlatDiscount() 
    {
        return get_ValueAsBoolean("IsBPartnerFlatDiscount");
        
    }
    
    /** Set Quantity based.
    @param IsQuantityBased Trade discount break level based on Quantity (not value) */
    public void setIsQuantityBased (boolean IsQuantityBased)
    {
        set_Value ("IsQuantityBased", Boolean.valueOf(IsQuantityBased));
        
    }
    
    /** Get Quantity based.
    @return Trade discount break level based on Quantity (not value) */
    public boolean isQuantityBased() 
    {
        return get_ValueAsBoolean("IsQuantityBased");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
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
    
    /** Set Script.
    @param Script Dynamic Java Language Script to calculate result */
    public void setScript (String Script)
    {
        set_Value ("Script", Script);
        
    }
    
    /** Get Script.
    @return Dynamic Java Language Script to calculate result */
    public String getScript() 
    {
        return (String)get_Value("Script");
        
    }
    
    /** Set Valid from.
    @param ValidFrom Valid from including this date (first day) */
    public void setValidFrom (Timestamp ValidFrom)
    {
        if (ValidFrom == null) throw new IllegalArgumentException ("ValidFrom is mandatory.");
        set_Value ("ValidFrom", ValidFrom);
        
    }
    
    /** Get Valid from.
    @return Valid from including this date (first day) */
    public Timestamp getValidFrom() 
    {
        return (Timestamp)get_Value("ValidFrom");
        
    }
    
    
}
