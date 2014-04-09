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
/** Generated Model for W_BasketLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_W_BasketLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_W_BasketLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param W_BasketLine_ID id
    @param trx transaction
    */
    public X_W_BasketLine (Ctx ctx, int W_BasketLine_ID, Trx trx)
    {
        super (ctx, W_BasketLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (W_BasketLine_ID == 0)
        {
            setDescription (null);
            setLine (0);
            setPrice (Env.ZERO);
            setProduct (null);
            setQty (Env.ZERO);
            setW_BasketLine_ID (0);
            setW_Basket_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_W_BasketLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=549 */
    public static final int Table_ID=549;
    
    /** TableName=W_BasketLine */
    public static final String Table_Name="W_BasketLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        if (Description == null) throw new IllegalArgumentException ("Description is mandatory.");
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (int Line)
    {
        set_Value ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getLine()));
        
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
    
    /** Set Price.
    @param Price Price */
    public void setPrice (java.math.BigDecimal Price)
    {
        if (Price == null) throw new IllegalArgumentException ("Price is mandatory.");
        set_Value ("Price", Price);
        
    }
    
    /** Get Price.
    @return Price */
    public java.math.BigDecimal getPrice() 
    {
        return get_ValueAsBigDecimal("Price");
        
    }
    
    /** Set Product.
    @param Product Product */
    public void setProduct (String Product)
    {
        if (Product == null) throw new IllegalArgumentException ("Product is mandatory.");
        set_Value ("Product", Product);
        
    }
    
    /** Get Product.
    @return Product */
    public String getProduct() 
    {
        return (String)get_Value("Product");
        
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
    
    /** Set Basket Line.
    @param W_BasketLine_ID Web Basket Line */
    public void setW_BasketLine_ID (int W_BasketLine_ID)
    {
        if (W_BasketLine_ID < 1) throw new IllegalArgumentException ("W_BasketLine_ID is mandatory.");
        set_ValueNoCheck ("W_BasketLine_ID", Integer.valueOf(W_BasketLine_ID));
        
    }
    
    /** Get Basket Line.
    @return Web Basket Line */
    public int getW_BasketLine_ID() 
    {
        return get_ValueAsInt("W_BasketLine_ID");
        
    }
    
    /** Set W_Basket_ID.
    @param W_Basket_ID Web Basket */
    public void setW_Basket_ID (int W_Basket_ID)
    {
        if (W_Basket_ID < 1) throw new IllegalArgumentException ("W_Basket_ID is mandatory.");
        set_ValueNoCheck ("W_Basket_ID", Integer.valueOf(W_Basket_ID));
        
    }
    
    /** Get W_Basket_ID.
    @return Web Basket */
    public int getW_Basket_ID() 
    {
        return get_ValueAsInt("W_Basket_ID");
        
    }
    
    
}
