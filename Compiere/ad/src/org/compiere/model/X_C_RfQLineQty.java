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
/** Generated Model for C_RfQLineQty
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_RfQLineQty.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_RfQLineQty extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_RfQLineQty_ID id
    @param trx transaction
    */
    public X_C_RfQLineQty (Ctx ctx, int C_RfQLineQty_ID, Trx trx)
    {
        super (ctx, C_RfQLineQty_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_RfQLineQty_ID == 0)
        {
            setBenchmarkPrice (Env.ZERO);
            setC_RfQLineQty_ID (0);
            setC_RfQLine_ID (0);
            setC_UOM_ID (0);
            setIsOfferQty (false);
            setIsPurchaseQty (false);
            setIsRfQQty (true);	// Y
            setQty (Env.ZERO);	// 1
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_RfQLineQty (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=675 */
    public static final int Table_ID=675;
    
    /** TableName=C_RfQLineQty */
    public static final String Table_Name="C_RfQLineQty";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Benchmark Price.
    @param BenchmarkPrice Price to compare responses to */
    public void setBenchmarkPrice (java.math.BigDecimal BenchmarkPrice)
    {
        if (BenchmarkPrice == null) throw new IllegalArgumentException ("BenchmarkPrice is mandatory.");
        set_Value ("BenchmarkPrice", BenchmarkPrice);
        
    }
    
    /** Get Benchmark Price.
    @return Price to compare responses to */
    public java.math.BigDecimal getBenchmarkPrice() 
    {
        return get_ValueAsBigDecimal("BenchmarkPrice");
        
    }
    
    /** Set Best Response Amount.
    @param BestResponseAmt Best Response Amount */
    public void setBestResponseAmt (java.math.BigDecimal BestResponseAmt)
    {
        set_Value ("BestResponseAmt", BestResponseAmt);
        
    }
    
    /** Get Best Response Amount.
    @return Best Response Amount */
    public java.math.BigDecimal getBestResponseAmt() 
    {
        return get_ValueAsBigDecimal("BestResponseAmt");
        
    }
    
    /** Set RfQ Line Quantity.
    @param C_RfQLineQty_ID Request for Quotation Line Quantity */
    public void setC_RfQLineQty_ID (int C_RfQLineQty_ID)
    {
        if (C_RfQLineQty_ID < 1) throw new IllegalArgumentException ("C_RfQLineQty_ID is mandatory.");
        set_ValueNoCheck ("C_RfQLineQty_ID", Integer.valueOf(C_RfQLineQty_ID));
        
    }
    
    /** Get RfQ Line Quantity.
    @return Request for Quotation Line Quantity */
    public int getC_RfQLineQty_ID() 
    {
        return get_ValueAsInt("C_RfQLineQty_ID");
        
    }
    
    /** Set RfQ Line.
    @param C_RfQLine_ID Request for Quotation Line */
    public void setC_RfQLine_ID (int C_RfQLine_ID)
    {
        if (C_RfQLine_ID < 1) throw new IllegalArgumentException ("C_RfQLine_ID is mandatory.");
        set_ValueNoCheck ("C_RfQLine_ID", Integer.valueOf(C_RfQLine_ID));
        
    }
    
    /** Get RfQ Line.
    @return Request for Quotation Line */
    public int getC_RfQLine_ID() 
    {
        return get_ValueAsInt("C_RfQLine_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID < 1) throw new IllegalArgumentException ("C_UOM_ID is mandatory.");
        set_Value ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_UOM_ID()));
        
    }
    
    /** Set Offer Quantity.
    @param IsOfferQty This quantity is used in the Offer to the Customer */
    public void setIsOfferQty (boolean IsOfferQty)
    {
        set_Value ("IsOfferQty", Boolean.valueOf(IsOfferQty));
        
    }
    
    /** Get Offer Quantity.
    @return This quantity is used in the Offer to the Customer */
    public boolean isOfferQty() 
    {
        return get_ValueAsBoolean("IsOfferQty");
        
    }
    
    /** Set Purchase Quantity.
    @param IsPurchaseQty This quantity is used in the Purchase Order to the Supplier */
    public void setIsPurchaseQty (boolean IsPurchaseQty)
    {
        set_Value ("IsPurchaseQty", Boolean.valueOf(IsPurchaseQty));
        
    }
    
    /** Get Purchase Quantity.
    @return This quantity is used in the Purchase Order to the Supplier */
    public boolean isPurchaseQty() 
    {
        return get_ValueAsBoolean("IsPurchaseQty");
        
    }
    
    /** Set RfQ Quantity.
    @param IsRfQQty The quantity is used when generating RfQ Responses */
    public void setIsRfQQty (boolean IsRfQQty)
    {
        set_Value ("IsRfQQty", Boolean.valueOf(IsRfQQty));
        
    }
    
    /** Get RfQ Quantity.
    @return The quantity is used when generating RfQ Responses */
    public boolean isRfQQty() 
    {
        return get_ValueAsBoolean("IsRfQQty");
        
    }
    
    /** Set Margin %.
    @param Margin Margin for a product as a percentage */
    public void setMargin (java.math.BigDecimal Margin)
    {
        set_Value ("Margin", Margin);
        
    }
    
    /** Get Margin %.
    @return Margin for a product as a percentage */
    public java.math.BigDecimal getMargin() 
    {
        return get_ValueAsBigDecimal("Margin");
        
    }
    
    /** Set Offer Amount.
    @param OfferAmt Amount of the Offer */
    public void setOfferAmt (java.math.BigDecimal OfferAmt)
    {
        set_Value ("OfferAmt", OfferAmt);
        
    }
    
    /** Get Offer Amount.
    @return Amount of the Offer */
    public java.math.BigDecimal getOfferAmt() 
    {
        return get_ValueAsBigDecimal("OfferAmt");
        
    }
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        if (Qty == null) throw new IllegalArgumentException ("Qty is mandatory.");
        set_Value ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    
}
