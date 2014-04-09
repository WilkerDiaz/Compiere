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
/** Generated Model for C_InvoicePaySchedule
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_InvoicePaySchedule.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_InvoicePaySchedule extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_InvoicePaySchedule_ID id
    @param trx transaction
    */
    public X_C_InvoicePaySchedule (Ctx ctx, int C_InvoicePaySchedule_ID, Trx trx)
    {
        super (ctx, C_InvoicePaySchedule_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_InvoicePaySchedule_ID == 0)
        {
            setC_InvoicePaySchedule_ID (0);
            setC_Invoice_ID (0);
            setDiscountAmt (Env.ZERO);
            setDiscountDate (new Timestamp(System.currentTimeMillis()));
            setDueAmt (Env.ZERO);
            setDueDate (new Timestamp(System.currentTimeMillis()));
            setIsValid (false);
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_InvoicePaySchedule (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=551 */
    public static final int Table_ID=551;
    
    /** TableName=C_InvoicePaySchedule */
    public static final String Table_Name="C_InvoicePaySchedule";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Invoice Payment Schedule.
    @param C_InvoicePaySchedule_ID Invoice Payment Schedule */
    public void setC_InvoicePaySchedule_ID (int C_InvoicePaySchedule_ID)
    {
        if (C_InvoicePaySchedule_ID < 1) throw new IllegalArgumentException ("C_InvoicePaySchedule_ID is mandatory.");
        set_ValueNoCheck ("C_InvoicePaySchedule_ID", Integer.valueOf(C_InvoicePaySchedule_ID));
        
    }
    
    /** Get Invoice Payment Schedule.
    @return Invoice Payment Schedule */
    public int getC_InvoicePaySchedule_ID() 
    {
        return get_ValueAsInt("C_InvoicePaySchedule_ID");
        
    }
    
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID < 1) throw new IllegalArgumentException ("C_Invoice_ID is mandatory.");
        set_ValueNoCheck ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
    }
    
    /** Set Payment Schedule.
    @param C_PaySchedule_ID Payment Schedule Template */
    public void setC_PaySchedule_ID (int C_PaySchedule_ID)
    {
        if (C_PaySchedule_ID <= 0) set_ValueNoCheck ("C_PaySchedule_ID", null);
        else
        set_ValueNoCheck ("C_PaySchedule_ID", Integer.valueOf(C_PaySchedule_ID));
        
    }
    
    /** Get Payment Schedule.
    @return Payment Schedule Template */
    public int getC_PaySchedule_ID() 
    {
        return get_ValueAsInt("C_PaySchedule_ID");
        
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
    
    /** Set Discount Date.
    @param DiscountDate Last Date for payments with discount */
    public void setDiscountDate (Timestamp DiscountDate)
    {
        if (DiscountDate == null) throw new IllegalArgumentException ("DiscountDate is mandatory.");
        set_Value ("DiscountDate", DiscountDate);
        
    }
    
    /** Get Discount Date.
    @return Last Date for payments with discount */
    public Timestamp getDiscountDate() 
    {
        return (Timestamp)get_Value("DiscountDate");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getDiscountDate()));
        
    }
    
    /** Set Amount due.
    @param DueAmt Amount of the payment due */
    public void setDueAmt (java.math.BigDecimal DueAmt)
    {
        if (DueAmt == null) throw new IllegalArgumentException ("DueAmt is mandatory.");
        set_Value ("DueAmt", DueAmt);
        
    }
    
    /** Get Amount due.
    @return Amount of the payment due */
    public java.math.BigDecimal getDueAmt() 
    {
        return get_ValueAsBigDecimal("DueAmt");
        
    }
    
    /** Set Due Date.
    @param DueDate Date when the payment is due */
    public void setDueDate (Timestamp DueDate)
    {
        if (DueDate == null) throw new IllegalArgumentException ("DueDate is mandatory.");
        set_Value ("DueDate", DueDate);
        
    }
    
    /** Get Due Date.
    @return Date when the payment is due */
    public Timestamp getDueDate() 
    {
        return (Timestamp)get_Value("DueDate");
        
    }
    
    /** Set Valid.
    @param IsValid Element is valid */
    public void setIsValid (boolean IsValid)
    {
        set_Value ("IsValid", Boolean.valueOf(IsValid));
        
    }
    
    /** Get Valid.
    @return Element is valid */
    public boolean isValid() 
    {
        return get_ValueAsBoolean("IsValid");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
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
    
    
}
