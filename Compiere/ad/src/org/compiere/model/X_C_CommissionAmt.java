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
/** Generated Model for C_CommissionAmt
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_CommissionAmt.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_CommissionAmt extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_CommissionAmt_ID id
    @param trx transaction
    */
    public X_C_CommissionAmt (Ctx ctx, int C_CommissionAmt_ID, Trx trx)
    {
        super (ctx, C_CommissionAmt_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_CommissionAmt_ID == 0)
        {
            setActualQty (Env.ZERO);
            setC_CommissionAmt_ID (0);
            setC_CommissionLine_ID (0);
            setC_CommissionRun_ID (0);
            setCommissionAmt (Env.ZERO);
            setConvertedAmt (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_CommissionAmt (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=430 */
    public static final int Table_ID=430;
    
    /** TableName=C_CommissionAmt */
    public static final String Table_Name="C_CommissionAmt";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Actual Quantity.
    @param ActualQty The actual quantity */
    public void setActualQty (java.math.BigDecimal ActualQty)
    {
        if (ActualQty == null) throw new IllegalArgumentException ("ActualQty is mandatory.");
        set_Value ("ActualQty", ActualQty);
        
    }
    
    /** Get Actual Quantity.
    @return The actual quantity */
    public java.math.BigDecimal getActualQty() 
    {
        return get_ValueAsBigDecimal("ActualQty");
        
    }
    
    /** Set Generated Commission Amount.
    @param C_CommissionAmt_ID Generated Commission Amount */
    public void setC_CommissionAmt_ID (int C_CommissionAmt_ID)
    {
        if (C_CommissionAmt_ID < 1) throw new IllegalArgumentException ("C_CommissionAmt_ID is mandatory.");
        set_ValueNoCheck ("C_CommissionAmt_ID", Integer.valueOf(C_CommissionAmt_ID));
        
    }
    
    /** Get Generated Commission Amount.
    @return Generated Commission Amount */
    public int getC_CommissionAmt_ID() 
    {
        return get_ValueAsInt("C_CommissionAmt_ID");
        
    }
    
    /** Set Commission Line.
    @param C_CommissionLine_ID Commission Line */
    public void setC_CommissionLine_ID (int C_CommissionLine_ID)
    {
        if (C_CommissionLine_ID < 1) throw new IllegalArgumentException ("C_CommissionLine_ID is mandatory.");
        set_Value ("C_CommissionLine_ID", Integer.valueOf(C_CommissionLine_ID));
        
    }
    
    /** Get Commission Line.
    @return Commission Line */
    public int getC_CommissionLine_ID() 
    {
        return get_ValueAsInt("C_CommissionLine_ID");
        
    }
    
    /** Set Commission Run.
    @param C_CommissionRun_ID Commission Run or Process */
    public void setC_CommissionRun_ID (int C_CommissionRun_ID)
    {
        if (C_CommissionRun_ID < 1) throw new IllegalArgumentException ("C_CommissionRun_ID is mandatory.");
        set_ValueNoCheck ("C_CommissionRun_ID", Integer.valueOf(C_CommissionRun_ID));
        
    }
    
    /** Get Commission Run.
    @return Commission Run or Process */
    public int getC_CommissionRun_ID() 
    {
        return get_ValueAsInt("C_CommissionRun_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_CommissionRun_ID()));
        
    }
    
    /** Set Commission Amount.
    @param CommissionAmt Commission Amount */
    public void setCommissionAmt (java.math.BigDecimal CommissionAmt)
    {
        if (CommissionAmt == null) throw new IllegalArgumentException ("CommissionAmt is mandatory.");
        set_Value ("CommissionAmt", CommissionAmt);
        
    }
    
    /** Get Commission Amount.
    @return Commission Amount */
    public java.math.BigDecimal getCommissionAmt() 
    {
        return get_ValueAsBigDecimal("CommissionAmt");
        
    }
    
    /** Set Converted Amount.
    @param ConvertedAmt Converted Amount */
    public void setConvertedAmt (java.math.BigDecimal ConvertedAmt)
    {
        if (ConvertedAmt == null) throw new IllegalArgumentException ("ConvertedAmt is mandatory.");
        set_Value ("ConvertedAmt", ConvertedAmt);
        
    }
    
    /** Get Converted Amount.
    @return Converted Amount */
    public java.math.BigDecimal getConvertedAmt() 
    {
        return get_ValueAsBigDecimal("ConvertedAmt");
        
    }
    
    
}
