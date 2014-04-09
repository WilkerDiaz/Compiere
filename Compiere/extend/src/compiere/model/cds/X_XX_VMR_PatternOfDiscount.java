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
/** Generated Model for XX_VMR_PatternOfDiscount
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_PatternOfDiscount extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_PatternOfDiscount_ID id
    @param trx transaction
    */
    public X_XX_VMR_PatternOfDiscount (Ctx ctx, int XX_VMR_PatternOfDiscount_ID, Trx trx)
    {
        super (ctx, XX_VMR_PatternOfDiscount_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_PatternOfDiscount_ID == 0)
        {
            setXX_VMR_PatternOfDiscount_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_PatternOfDiscount (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27564723794789L;
    /** Last Updated Timestamp 2010-08-23 16:51:18.0 */
    public static final long updatedMS = 1282598478000L;
    /** AD_Table_ID=1000343 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_PatternOfDiscount");
        
    }
    ;
    
    /** TableName=XX_VMR_PatternOfDiscount */
    public static final String Table_Name="XX_VMR_PatternOfDiscount";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Product.
    @param M_Product Product */
    public void setM_Product (String M_Product)
    {
        throw new IllegalArgumentException ("M_Product is virtual column");
        
    }
    
    /** Get Product.
    @return Product */
    public String getM_Product() 
    {
        return (String)get_Value("M_Product");
        
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
    
    /** Set Total Bs..
    @param XX_AmountPromotion20 Total Bs. */
    public void setXX_AmountPromotion20 (java.math.BigDecimal XX_AmountPromotion20)
    {
        set_Value ("XX_AmountPromotion20", XX_AmountPromotion20);
        
    }
    
    /** Get Total Bs..
    @return Total Bs. */
    public java.math.BigDecimal getXX_AmountPromotion20() 
    {
        return get_ValueAsBigDecimal("XX_AmountPromotion20");
        
    }
    
    /** Set Total Bs..
    @param XX_AmountPromotion30 Total Bs. */
    public void setXX_AmountPromotion30 (java.math.BigDecimal XX_AmountPromotion30)
    {
        set_Value ("XX_AmountPromotion30", XX_AmountPromotion30);
        
    }
    
    /** Get Total Bs..
    @return Total Bs. */
    public java.math.BigDecimal getXX_AmountPromotion30() 
    {
        return get_ValueAsBigDecimal("XX_AmountPromotion30");
        
    }
    
    /** Set Total Bs..
    @param XX_AmountPromotion50 Total Bs. */
    public void setXX_AmountPromotion50 (java.math.BigDecimal XX_AmountPromotion50)
    {
        set_Value ("XX_AmountPromotion50", XX_AmountPromotion50);
        
    }
    
    /** Get Total Bs..
    @return Total Bs. */
    public java.math.BigDecimal getXX_AmountPromotion50() 
    {
        return get_ValueAsBigDecimal("XX_AmountPromotion50");
        
    }
    
    /** Set Date.
    @param XX_DATE Date */
    public void setXX_DATE (String XX_DATE)
    {
        set_Value ("XX_DATE", XX_DATE);
        
    }
    
    /** Get Date.
    @return Date */
    public String getXX_DATE() 
    {
        return (String)get_Value("XX_DATE");
        
    }
    
    /** Set Total Bs..
    @param XX_FinalInventoryAmount Total Bs. */
    public void setXX_FinalInventoryAmount (java.math.BigDecimal XX_FinalInventoryAmount)
    {
        set_Value ("XX_FinalInventoryAmount", XX_FinalInventoryAmount);
        
    }
    
    /** Get Total Bs..
    @return Total Bs. */
    public java.math.BigDecimal getXX_FinalInventoryAmount() 
    {
        return get_ValueAsBigDecimal("XX_FinalInventoryAmount");
        
    }
    
    /** Set Pieces.
    @param XX_FinalInventoryQuantity Pieces */
    public void setXX_FinalInventoryQuantity (int XX_FinalInventoryQuantity)
    {
        set_Value ("XX_FinalInventoryQuantity", Integer.valueOf(XX_FinalInventoryQuantity));
        
    }
    
    /** Get Pieces.
    @return Pieces */
    public int getXX_FinalInventoryQuantity() 
    {
        return get_ValueAsInt("XX_FinalInventoryQuantity");
        
    }
    
    /** Set Pieces.
    @param XX_QuantityPromotion20 Pieces */
    public void setXX_QuantityPromotion20 (int XX_QuantityPromotion20)
    {
        set_Value ("XX_QuantityPromotion20", Integer.valueOf(XX_QuantityPromotion20));
        
    }
    
    /** Get Pieces.
    @return Pieces */
    public int getXX_QuantityPromotion20() 
    {
        return get_ValueAsInt("XX_QuantityPromotion20");
        
    }
    
    /** Set Pieces.
    @param XX_QuantityPromotion30 Pieces */
    public void setXX_QuantityPromotion30 (int XX_QuantityPromotion30)
    {
        set_Value ("XX_QuantityPromotion30", Integer.valueOf(XX_QuantityPromotion30));
        
    }
    
    /** Get Pieces.
    @return Pieces */
    public int getXX_QuantityPromotion30() 
    {
        return get_ValueAsInt("XX_QuantityPromotion30");
        
    }
    
    /** Set Pieces.
    @param XX_QuantityPromotion50 Pieces */
    public void setXX_QuantityPromotion50 (int XX_QuantityPromotion50)
    {
        set_Value ("XX_QuantityPromotion50", Integer.valueOf(XX_QuantityPromotion50));
        
    }
    
    /** Get Pieces.
    @return Pieces */
    public int getXX_QuantityPromotion50() 
    {
        return get_ValueAsInt("XX_QuantityPromotion50");
        
    }
    
    /** Set XX_VMR_PatternOfDiscount_ID.
    @param XX_VMR_PatternOfDiscount_ID XX_VMR_PatternOfDiscount_ID */
    public void setXX_VMR_PatternOfDiscount_ID (int XX_VMR_PatternOfDiscount_ID)
    {
        if (XX_VMR_PatternOfDiscount_ID < 1) throw new IllegalArgumentException ("XX_VMR_PatternOfDiscount_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PatternOfDiscount_ID", Integer.valueOf(XX_VMR_PatternOfDiscount_ID));
        
    }
    
    /** Get XX_VMR_PatternOfDiscount_ID.
    @return XX_VMR_PatternOfDiscount_ID */
    public int getXX_VMR_PatternOfDiscount_ID() 
    {
        return get_ValueAsInt("XX_VMR_PatternOfDiscount_ID");
        
    }
    
    
}
