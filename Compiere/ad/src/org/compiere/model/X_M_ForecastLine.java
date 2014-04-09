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
/** Generated Model for M_ForecastLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_ForecastLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_ForecastLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_ForecastLine_ID id
    @param trx transaction
    */
    public X_M_ForecastLine (Ctx ctx, int M_ForecastLine_ID, Trx trx)
    {
        super (ctx, M_ForecastLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_ForecastLine_ID == 0)
        {
            setC_Period_ID (0);
            setM_ForecastLine_ID (0);
            setM_Forecast_ID (0);
            setM_Product_ID (0);
            setQty (Env.ZERO);
            setQtyCalculated (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_ForecastLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=722 */
    public static final int Table_ID=722;
    
    /** TableName=M_ForecastLine */
    public static final String Table_Name="M_ForecastLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Period.
    @param C_Period_ID Period of the Calendar */
    public void setC_Period_ID (int C_Period_ID)
    {
        if (C_Period_ID < 1) throw new IllegalArgumentException ("C_Period_ID is mandatory.");
        set_ValueNoCheck ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
    }
    
    /** Get Period.
    @return Period of the Calendar */
    public int getC_Period_ID() 
    {
        return get_ValueAsInt("C_Period_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Period_ID()));
        
    }
    
    /** Set Forecast Line.
    @param M_ForecastLine_ID Forecast Line */
    public void setM_ForecastLine_ID (int M_ForecastLine_ID)
    {
        if (M_ForecastLine_ID < 1) throw new IllegalArgumentException ("M_ForecastLine_ID is mandatory.");
        set_ValueNoCheck ("M_ForecastLine_ID", Integer.valueOf(M_ForecastLine_ID));
        
    }
    
    /** Get Forecast Line.
    @return Forecast Line */
    public int getM_ForecastLine_ID() 
    {
        return get_ValueAsInt("M_ForecastLine_ID");
        
    }
    
    /** Set Forecast.
    @param M_Forecast_ID Material Forecast */
    public void setM_Forecast_ID (int M_Forecast_ID)
    {
        if (M_Forecast_ID < 1) throw new IllegalArgumentException ("M_Forecast_ID is mandatory.");
        set_ValueNoCheck ("M_Forecast_ID", Integer.valueOf(M_Forecast_ID));
        
    }
    
    /** Get Forecast.
    @return Material Forecast */
    public int getM_Forecast_ID() 
    {
        return get_ValueAsInt("M_Forecast_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** Set Calculated Quantity.
    @param QtyCalculated Calculated Quantity */
    public void setQtyCalculated (java.math.BigDecimal QtyCalculated)
    {
        if (QtyCalculated == null) throw new IllegalArgumentException ("QtyCalculated is mandatory.");
        set_Value ("QtyCalculated", QtyCalculated);
        
    }
    
    /** Get Calculated Quantity.
    @return Calculated Quantity */
    public java.math.BigDecimal getQtyCalculated() 
    {
        return get_ValueAsBigDecimal("QtyCalculated");
        
    }
    
    
}
