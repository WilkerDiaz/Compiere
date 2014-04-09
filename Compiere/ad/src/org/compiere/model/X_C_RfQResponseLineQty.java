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
/** Generated Model for C_RfQResponseLineQty
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_RfQResponseLineQty.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_RfQResponseLineQty extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_RfQResponseLineQty_ID id
    @param trx transaction
    */
    public X_C_RfQResponseLineQty (Ctx ctx, int C_RfQResponseLineQty_ID, Trx trx)
    {
        super (ctx, C_RfQResponseLineQty_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_RfQResponseLineQty_ID == 0)
        {
            setC_RfQLineQty_ID (0);
            setC_RfQResponseLineQty_ID (0);
            setC_RfQResponseLine_ID (0);
            setPrice (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_RfQResponseLineQty (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=672 */
    public static final int Table_ID=672;
    
    /** TableName=C_RfQResponseLineQty */
    public static final String Table_Name="C_RfQResponseLineQty";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set RfQ Response Line Qty.
    @param C_RfQResponseLineQty_ID Request for Quotation Response Line Quantity */
    public void setC_RfQResponseLineQty_ID (int C_RfQResponseLineQty_ID)
    {
        if (C_RfQResponseLineQty_ID < 1) throw new IllegalArgumentException ("C_RfQResponseLineQty_ID is mandatory.");
        set_ValueNoCheck ("C_RfQResponseLineQty_ID", Integer.valueOf(C_RfQResponseLineQty_ID));
        
    }
    
    /** Get RfQ Response Line Qty.
    @return Request for Quotation Response Line Quantity */
    public int getC_RfQResponseLineQty_ID() 
    {
        return get_ValueAsInt("C_RfQResponseLineQty_ID");
        
    }
    
    /** Set RfQ Response Line.
    @param C_RfQResponseLine_ID Request for Quotation Response Line */
    public void setC_RfQResponseLine_ID (int C_RfQResponseLine_ID)
    {
        if (C_RfQResponseLine_ID < 1) throw new IllegalArgumentException ("C_RfQResponseLine_ID is mandatory.");
        set_ValueNoCheck ("C_RfQResponseLine_ID", Integer.valueOf(C_RfQResponseLine_ID));
        
    }
    
    /** Get RfQ Response Line.
    @return Request for Quotation Response Line */
    public int getC_RfQResponseLine_ID() 
    {
        return get_ValueAsInt("C_RfQResponseLine_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_RfQResponseLine_ID()));
        
    }
    
    /** Set Discount %.
    @param Discount Discount in percent */
    public void setDiscount (java.math.BigDecimal Discount)
    {
        set_Value ("Discount", Discount);
        
    }
    
    /** Get Discount %.
    @return Discount in percent */
    public java.math.BigDecimal getDiscount() 
    {
        return get_ValueAsBigDecimal("Discount");
        
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
    
    /** Set Ranking.
    @param Ranking Relative Rank Number */
    public void setRanking (int Ranking)
    {
        set_Value ("Ranking", Integer.valueOf(Ranking));
        
    }
    
    /** Get Ranking.
    @return Relative Rank Number */
    public int getRanking() 
    {
        return get_ValueAsInt("Ranking");
        
    }
    
    
}
