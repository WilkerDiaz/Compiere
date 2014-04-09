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
/** Generated Model for C_PaymentAllocate
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_PaymentAllocate.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_PaymentAllocate extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_PaymentAllocate_ID id
    @param trx transaction
    */
    public X_C_PaymentAllocate (Ctx ctx, int C_PaymentAllocate_ID, Trx trx)
    {
        super (ctx, C_PaymentAllocate_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_PaymentAllocate_ID == 0)
        {
            setAmount (Env.ZERO);
            setC_Invoice_ID (0);
            setC_PaymentAllocate_ID (0);
            setC_Payment_ID (0);
            setDiscountAmt (Env.ZERO);
            setOverUnderAmt (Env.ZERO);
            setWriteOffAmt (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_PaymentAllocate (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=812 */
    public static final int Table_ID=812;
    
    /** TableName=C_PaymentAllocate */
    public static final String Table_Name="C_PaymentAllocate";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Amount.
    @param Amount Amount in a defined currency */
    public void setAmount (java.math.BigDecimal Amount)
    {
        if (Amount == null) throw new IllegalArgumentException ("Amount is mandatory.");
        set_Value ("Amount", Amount);
        
    }
    
    /** Get Amount.
    @return Amount in a defined currency */
    public java.math.BigDecimal getAmount() 
    {
        return get_ValueAsBigDecimal("Amount");
        
    }
    
    /** Set Allocation Line.
    @param C_AllocationLine_ID Allocation Line */
    public void setC_AllocationLine_ID (int C_AllocationLine_ID)
    {
        if (C_AllocationLine_ID <= 0) set_Value ("C_AllocationLine_ID", null);
        else
        set_Value ("C_AllocationLine_ID", Integer.valueOf(C_AllocationLine_ID));
        
    }
    
    /** Get Allocation Line.
    @return Allocation Line */
    public int getC_AllocationLine_ID() 
    {
        return get_ValueAsInt("C_AllocationLine_ID");
        
    }
    
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID < 1) throw new IllegalArgumentException ("C_Invoice_ID is mandatory.");
        set_Value ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Invoice_ID()));
        
    }
    
    /** Set Allocate Payment.
    @param C_PaymentAllocate_ID Allocate Payment to Invoices */
    public void setC_PaymentAllocate_ID (int C_PaymentAllocate_ID)
    {
        if (C_PaymentAllocate_ID < 1) throw new IllegalArgumentException ("C_PaymentAllocate_ID is mandatory.");
        set_ValueNoCheck ("C_PaymentAllocate_ID", Integer.valueOf(C_PaymentAllocate_ID));
        
    }
    
    /** Get Allocate Payment.
    @return Allocate Payment to Invoices */
    public int getC_PaymentAllocate_ID() 
    {
        return get_ValueAsInt("C_PaymentAllocate_ID");
        
    }
    
    /** Set Payment.
    @param C_Payment_ID Payment identifier */
    public void setC_Payment_ID (int C_Payment_ID)
    {
        if (C_Payment_ID < 1) throw new IllegalArgumentException ("C_Payment_ID is mandatory.");
        set_ValueNoCheck ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
    }
    
    /** Get Payment.
    @return Payment identifier */
    public int getC_Payment_ID() 
    {
        return get_ValueAsInt("C_Payment_ID");
        
    }
    
    /** Set Discount Amount.
    @param DiscountAmt Calculated amount of discount */
    public void setDiscountAmt (java.math.BigDecimal DiscountAmt)
    {
        if (DiscountAmt == null) throw new IllegalArgumentException ("DiscountAmt is mandatory.");
        set_Value ("DiscountAmt", DiscountAmt);
        
    }
    
    /** Get Discount Amount.
    @return Calculated amount of discount */
    public java.math.BigDecimal getDiscountAmt() 
    {
        return get_ValueAsBigDecimal("DiscountAmt");
        
    }
    
    /** Set Invoice Amt.
    @param InvoiceAmt Invoice Amt */
    public void setInvoiceAmt (java.math.BigDecimal InvoiceAmt)
    {
        set_Value ("InvoiceAmt", InvoiceAmt);
        
    }
    
    /** Get Invoice Amt.
    @return Invoice Amt */
    public java.math.BigDecimal getInvoiceAmt() 
    {
        return get_ValueAsBigDecimal("InvoiceAmt");
        
    }
    
    /** Set Over/Under Payment Amount.
    @param OverUnderAmt Over-Payment (unallocated) or Under-Payment (partial payment) Amount */
    public void setOverUnderAmt (java.math.BigDecimal OverUnderAmt)
    {
        if (OverUnderAmt == null) throw new IllegalArgumentException ("OverUnderAmt is mandatory.");
        set_Value ("OverUnderAmt", OverUnderAmt);
        
    }
    
    /** Get Over/Under Payment Amount.
    @return Over-Payment (unallocated) or Under-Payment (partial payment) Amount */
    public java.math.BigDecimal getOverUnderAmt() 
    {
        return get_ValueAsBigDecimal("OverUnderAmt");
        
    }
    
    /** Set Remaining Amt.
    @param RemainingAmt Remaining Amount */
    public void setRemainingAmt (java.math.BigDecimal RemainingAmt)
    {
        throw new IllegalArgumentException ("RemainingAmt is virtual column");
        
    }
    
    /** Get Remaining Amt.
    @return Remaining Amount */
    public java.math.BigDecimal getRemainingAmt() 
    {
        return get_ValueAsBigDecimal("RemainingAmt");
        
    }
    
    /** Set Write-off Amount.
    @param WriteOffAmt Amount to write-off */
    public void setWriteOffAmt (java.math.BigDecimal WriteOffAmt)
    {
        if (WriteOffAmt == null) throw new IllegalArgumentException ("WriteOffAmt is mandatory.");
        set_Value ("WriteOffAmt", WriteOffAmt);
        
    }
    
    /** Get Write-off Amount.
    @return Amount to write-off */
    public java.math.BigDecimal getWriteOffAmt() 
    {
        return get_ValueAsBigDecimal("WriteOffAmt");
        
    }
    
    
}
