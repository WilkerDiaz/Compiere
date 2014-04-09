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
/** Generated Model for M_DiscountSchemaBreak
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_DiscountSchemaBreak.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_DiscountSchemaBreak extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_DiscountSchemaBreak_ID id
    @param trx transaction
    */
    public X_M_DiscountSchemaBreak (Ctx ctx, int M_DiscountSchemaBreak_ID, Trx trx)
    {
        super (ctx, M_DiscountSchemaBreak_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_DiscountSchemaBreak_ID == 0)
        {
            setBreakDiscount (Env.ZERO);
            setBreakValue (Env.ZERO);
            setIsBPartnerFlatDiscount (false);	// N
            setM_DiscountSchemaBreak_ID (0);
            setM_DiscountSchema_ID (0);
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM M_DiscountSchemaBreak WHERE M_DiscountSchema_ID=@M_DiscountSchema_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_DiscountSchemaBreak (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=476 */
    public static final int Table_ID=476;
    
    /** TableName=M_DiscountSchemaBreak */
    public static final String Table_Name="M_DiscountSchemaBreak";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Break Discount %.
    @param BreakDiscount Trade Discount in Percent for the break level */
    public void setBreakDiscount (java.math.BigDecimal BreakDiscount)
    {
        if (BreakDiscount == null) throw new IllegalArgumentException ("BreakDiscount is mandatory.");
        set_Value ("BreakDiscount", BreakDiscount);
        
    }
    
    /** Get Break Discount %.
    @return Trade Discount in Percent for the break level */
    public java.math.BigDecimal getBreakDiscount() 
    {
        return get_ValueAsBigDecimal("BreakDiscount");
        
    }
    
    /** Set Break Value.
    @param BreakValue Low Value of a trade discount break level */
    public void setBreakValue (java.math.BigDecimal BreakValue)
    {
        if (BreakValue == null) throw new IllegalArgumentException ("BreakValue is mandatory.");
        set_Value ("BreakValue", BreakValue);
        
    }
    
    /** Get Break Value.
    @return Low Value of a trade discount break level */
    public java.math.BigDecimal getBreakValue() 
    {
        return get_ValueAsBigDecimal("BreakValue");
        
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
    
    /** Set Discount Schema Break.
    @param M_DiscountSchemaBreak_ID Trade Discount Break */
    public void setM_DiscountSchemaBreak_ID (int M_DiscountSchemaBreak_ID)
    {
        if (M_DiscountSchemaBreak_ID < 1) throw new IllegalArgumentException ("M_DiscountSchemaBreak_ID is mandatory.");
        set_ValueNoCheck ("M_DiscountSchemaBreak_ID", Integer.valueOf(M_DiscountSchemaBreak_ID));
        
    }
    
    /** Get Discount Schema Break.
    @return Trade Discount Break */
    public int getM_DiscountSchemaBreak_ID() 
    {
        return get_ValueAsInt("M_DiscountSchemaBreak_ID");
        
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
    
    
}
