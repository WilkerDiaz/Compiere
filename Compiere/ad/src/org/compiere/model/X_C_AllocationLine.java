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
/** Generated Model for C_AllocationLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_AllocationLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_AllocationLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_AllocationLine_ID id
    @param trx transaction
    */
    public X_C_AllocationLine (Ctx ctx, int C_AllocationLine_ID, Trx trx)
    {
        super (ctx, C_AllocationLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_AllocationLine_ID == 0)
        {
            setAmount (Env.ZERO);
            setC_AllocationHdr_ID (0);
            setC_AllocationLine_ID (0);
            setDiscountAmt (Env.ZERO);
            setWriteOffAmt (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_AllocationLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27502697835789L;
    /** Last Updated Timestamp 2008-09-04 16:55:19.0 */
    public static final long updatedMS = 1220572519000L;
    /** AD_Table_ID=390 */
    public static final int Table_ID=390;
    
    /** TableName=C_AllocationLine */
    public static final String Table_Name="C_AllocationLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Trx Organization.
    @param AD_OrgTrx_ID Performing or initiating organization */
    public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
    {
        if (AD_OrgTrx_ID <= 0) set_Value ("AD_OrgTrx_ID", null);
        else
        set_Value ("AD_OrgTrx_ID", Integer.valueOf(AD_OrgTrx_ID));
        
    }
    
    /** Get Trx Organization.
    @return Performing or initiating organization */
    public int getAD_OrgTrx_ID() 
    {
        return get_ValueAsInt("AD_OrgTrx_ID");
        
    }
    
    /** Set Amount.
    @param Amount Amount in a defined currency */
    public void setAmount (java.math.BigDecimal Amount)
    {
        if (Amount == null) throw new IllegalArgumentException ("Amount is mandatory.");
        set_ValueNoCheck ("Amount", Amount);
        
    }
    
    /** Get Amount.
    @return Amount in a defined currency */
    public java.math.BigDecimal getAmount() 
    {
        return get_ValueAsBigDecimal("Amount");
        
    }
    
    /** Set Activity.
    @param C_Activity_ID Business Activity */
    public void setC_Activity_ID (int C_Activity_ID)
    {
        if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
        else
        set_Value ("C_Activity_ID", Integer.valueOf(C_Activity_ID));
        
    }
    
    /** Get Activity.
    @return Business Activity */
    public int getC_Activity_ID() 
    {
        return get_ValueAsInt("C_Activity_ID");
        
    }
    
    /** Set Allocation.
    @param C_AllocationHdr_ID Payment allocation */
    public void setC_AllocationHdr_ID (int C_AllocationHdr_ID)
    {
        if (C_AllocationHdr_ID < 1) throw new IllegalArgumentException ("C_AllocationHdr_ID is mandatory.");
        set_ValueNoCheck ("C_AllocationHdr_ID", Integer.valueOf(C_AllocationHdr_ID));
        
    }
    
    /** Get Allocation.
    @return Payment allocation */
    public int getC_AllocationHdr_ID() 
    {
        return get_ValueAsInt("C_AllocationHdr_ID");
        
    }
    
    /** Set Allocation Line.
    @param C_AllocationLine_ID Allocation Line */
    public void setC_AllocationLine_ID (int C_AllocationLine_ID)
    {
        if (C_AllocationLine_ID < 1) throw new IllegalArgumentException ("C_AllocationLine_ID is mandatory.");
        set_ValueNoCheck ("C_AllocationLine_ID", Integer.valueOf(C_AllocationLine_ID));
        
    }
    
    /** Get Allocation Line.
    @return Allocation Line */
    public int getC_AllocationLine_ID() 
    {
        return get_ValueAsInt("C_AllocationLine_ID");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_ValueNoCheck ("C_BPartner_ID", null);
        else
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Cash Journal Line.
    @param C_CashLine_ID Cash Journal Line */
    public void setC_CashLine_ID (int C_CashLine_ID)
    {
        if (C_CashLine_ID <= 0) set_ValueNoCheck ("C_CashLine_ID", null);
        else
        set_ValueNoCheck ("C_CashLine_ID", Integer.valueOf(C_CashLine_ID));
        
    }
    
    /** Get Cash Journal Line.
    @return Cash Journal Line */
    public int getC_CashLine_ID() 
    {
        return get_ValueAsInt("C_CashLine_ID");
        
    }
    
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID <= 0) set_ValueNoCheck ("C_Invoice_ID", null);
        else
        set_ValueNoCheck ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
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
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_ValueNoCheck ("C_Order_ID", null);
        else
        set_ValueNoCheck ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Payment.
    @param C_Payment_ID Payment identifier */
    public void setC_Payment_ID (int C_Payment_ID)
    {
        if (C_Payment_ID <= 0) set_ValueNoCheck ("C_Payment_ID", null);
        else
        set_ValueNoCheck ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
    }
    
    /** Get Payment.
    @return Payment identifier */
    public int getC_Payment_ID() 
    {
        return get_ValueAsInt("C_Payment_ID");
        
    }
    
    /** Set Transaction Date.
    @param DateTrx Transaction Date */
    public void setDateTrx (Timestamp DateTrx)
    {
        set_ValueNoCheck ("DateTrx", DateTrx);
        
    }
    
    /** Get Transaction Date.
    @return Transaction Date */
    public Timestamp getDateTrx() 
    {
        return (Timestamp)get_Value("DateTrx");
        
    }
    
    /** Set Discount Amount.
    @param DiscountAmt Calculated amount of discount */
    public void setDiscountAmt (java.math.BigDecimal DiscountAmt)
    {
        if (DiscountAmt == null) throw new IllegalArgumentException ("DiscountAmt is mandatory.");
        set_ValueNoCheck ("DiscountAmt", DiscountAmt);
        
    }
    
    /** Get Discount Amount.
    @return Calculated amount of discount */
    public java.math.BigDecimal getDiscountAmt() 
    {
        return get_ValueAsBigDecimal("DiscountAmt");
        
    }
    
    /** Set Manual.
    @param IsManual This is a manual process or entry */
    public void setIsManual (boolean IsManual)
    {
        set_ValueNoCheck ("IsManual", Boolean.valueOf(IsManual));
        
    }
    
    /** Get Manual.
    @return This is a manual process or entry */
    public boolean isManual() 
    {
        return get_ValueAsBoolean("IsManual");
        
    }
    
    /** Set Over/Under Payment Amount.
    @param OverUnderAmt Over-Payment (unallocated) or Under-Payment (partial payment) Amount */
    public void setOverUnderAmt (java.math.BigDecimal OverUnderAmt)
    {
        set_Value ("OverUnderAmt", OverUnderAmt);
        
    }
    
    /** Get Over/Under Payment Amount.
    @return Over-Payment (unallocated) or Under-Payment (partial payment) Amount */
    public java.math.BigDecimal getOverUnderAmt() 
    {
        return get_ValueAsBigDecimal("OverUnderAmt");
        
    }
    
    /** Set Write-off Amount.
    @param WriteOffAmt Amount to write-off */
    public void setWriteOffAmt (java.math.BigDecimal WriteOffAmt)
    {
        if (WriteOffAmt == null) throw new IllegalArgumentException ("WriteOffAmt is mandatory.");
        set_ValueNoCheck ("WriteOffAmt", WriteOffAmt);
        
    }
    
    /** Get Write-off Amount.
    @return Amount to write-off */
    public java.math.BigDecimal getWriteOffAmt() 
    {
        return get_ValueAsBigDecimal("WriteOffAmt");
        
    }
    
    
}
