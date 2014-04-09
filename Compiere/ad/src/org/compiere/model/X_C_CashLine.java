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
/** Generated Model for C_CashLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_C_CashLine.java 8982 2010-06-24 08:36:53Z ragrawal $ */
public class X_C_CashLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_CashLine_ID id
    @param trx transaction
    */
    public X_C_CashLine (Ctx ctx, int C_CashLine_ID, Trx trx)
    {
        super (ctx, C_CashLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_CashLine_ID == 0)
        {
            setAmount (Env.ZERO);
            setC_CashLine_ID (0);
            setC_Cash_ID (0);
            setCashType (null);	// E
            setIsOverUnderPayment (false);
            setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM C_CashLine WHERE C_Cash_ID=@C_Cash_ID@
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_CashLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27559448497789L;
    /** Last Updated Timestamp 2010-06-24 01:29:41.0 */
    public static final long updatedMS = 1277323181000L;
    /** AD_Table_ID=410 */
    public static final int Table_ID=410;
    
    /** TableName=C_CashLine */
    public static final String Table_Name="C_CashLine";
    
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
        set_Value ("Amount", Amount);
        
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
    
    /** Set Bank Account.
    @param C_BankAccount_ID Account at the Bank */
    public void setC_BankAccount_ID (int C_BankAccount_ID)
    {
        if (C_BankAccount_ID <= 0) set_Value ("C_BankAccount_ID", null);
        else
        set_Value ("C_BankAccount_ID", Integer.valueOf(C_BankAccount_ID));
        
    }
    
    /** Get Bank Account.
    @return Account at the Bank */
    public int getC_BankAccount_ID() 
    {
        return get_ValueAsInt("C_BankAccount_ID");
        
    }
    
    /** Set Cash Journal Line.
    @param C_CashLine_ID Cash Journal Line */
    public void setC_CashLine_ID (int C_CashLine_ID)
    {
        if (C_CashLine_ID < 1) throw new IllegalArgumentException ("C_CashLine_ID is mandatory.");
        set_ValueNoCheck ("C_CashLine_ID", Integer.valueOf(C_CashLine_ID));
        
    }
    
    /** Get Cash Journal Line.
    @return Cash Journal Line */
    public int getC_CashLine_ID() 
    {
        return get_ValueAsInt("C_CashLine_ID");
        
    }
    
    /** Set Expense/Receipt Type.
    @param C_Cash_ExpenseReceiptType_ID Expense/Receipt Type */
    public void setC_Cash_ExpenseReceiptType_ID (int C_Cash_ExpenseReceiptType_ID)
    {
        if (C_Cash_ExpenseReceiptType_ID <= 0) set_Value ("C_Cash_ExpenseReceiptType_ID", null);
        else
        set_Value ("C_Cash_ExpenseReceiptType_ID", Integer.valueOf(C_Cash_ExpenseReceiptType_ID));
        
    }
    
    /** Get Expense/Receipt Type.
    @return Expense/Receipt Type */
    public int getC_Cash_ExpenseReceiptType_ID() 
    {
        return get_ValueAsInt("C_Cash_ExpenseReceiptType_ID");
        
    }
    
    /** Set Cash Journal.
    @param C_Cash_ID Cash Journal */
    public void setC_Cash_ID (int C_Cash_ID)
    {
        if (C_Cash_ID < 1) throw new IllegalArgumentException ("C_Cash_ID is mandatory.");
        set_ValueNoCheck ("C_Cash_ID", Integer.valueOf(C_Cash_ID));
        
    }
    
    /** Get Cash Journal.
    @return Cash Journal */
    public int getC_Cash_ID() 
    {
        return get_ValueAsInt("C_Cash_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Cash_ID()));
        
    }
    
    /** Set Charge.
    @param C_Charge_ID Additional document charges */
    public void setC_Charge_ID (int C_Charge_ID)
    {
        if (C_Charge_ID <= 0) set_Value ("C_Charge_ID", null);
        else
        set_Value ("C_Charge_ID", Integer.valueOf(C_Charge_ID));
        
    }
    
    /** Get Charge.
    @return Additional document charges */
    public int getC_Charge_ID() 
    {
        return get_ValueAsInt("C_Charge_ID");
        
    }
    
    /** Set Rate Type.
    @param C_ConversionType_ID Currency Conversion Rate Type */
    public void setC_ConversionType_ID (int C_ConversionType_ID)
    {
        if (C_ConversionType_ID <= 0) set_Value ("C_ConversionType_ID", null);
        else
        set_Value ("C_ConversionType_ID", Integer.valueOf(C_ConversionType_ID));
        
    }
    
    /** Get Rate Type.
    @return Currency Conversion Rate Type */
    public int getC_ConversionType_ID() 
    {
        return get_ValueAsInt("C_ConversionType_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_ValueNoCheck ("C_Currency_ID", null);
        else
        set_ValueNoCheck ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
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
    
    /** Charge = C */
    public static final String CASHTYPE_Charge = X_Ref_C_Cash_Trx_Type.CHARGE.getValue();
    /** Difference = D */
    public static final String CASHTYPE_Difference = X_Ref_C_Cash_Trx_Type.DIFFERENCE.getValue();
    /** Expense = E */
    public static final String CASHTYPE_Expense = X_Ref_C_Cash_Trx_Type.EXPENSE.getValue();
    /** Invoice = I */
    public static final String CASHTYPE_Invoice = X_Ref_C_Cash_Trx_Type.INVOICE.getValue();
    /** Receipt = R */
    public static final String CASHTYPE_Receipt = X_Ref_C_Cash_Trx_Type.RECEIPT.getValue();
    /** Bank Account Transfer = T */
    public static final String CASHTYPE_BankAccountTransfer = X_Ref_C_Cash_Trx_Type.BANK_ACCOUNT_TRANSFER.getValue();
    /** Set Cash Type.
    @param CashType Source of Cash */
    public void setCashType (String CashType)
    {
        if (CashType == null) throw new IllegalArgumentException ("CashType is mandatory");
        if (!X_Ref_C_Cash_Trx_Type.isValid(CashType))
        throw new IllegalArgumentException ("CashType Invalid value - " + CashType + " - Reference_ID=217 - C - D - E - I - R - T");
        set_ValueNoCheck ("CashType", CashType);
        
    }
    
    /** Get Cash Type.
    @return Source of Cash */
    public String getCashType() 
    {
        return (String)get_Value("CashType");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Discount Amount.
    @param DiscountAmt Calculated amount of discount */
    public void setDiscountAmt (java.math.BigDecimal DiscountAmt)
    {
        set_Value ("DiscountAmt", DiscountAmt);
        
    }
    
    /** Get Discount Amount.
    @return Calculated amount of discount */
    public java.math.BigDecimal getDiscountAmt() 
    {
        return get_ValueAsBigDecimal("DiscountAmt");
        
    }
    
    /** Set Generated.
    @param IsGenerated This Line is generated */
    public void setIsGenerated (boolean IsGenerated)
    {
        set_ValueNoCheck ("IsGenerated", Boolean.valueOf(IsGenerated));
        
    }
    
    /** Get Generated.
    @return This Line is generated */
    public boolean isGenerated() 
    {
        return get_ValueAsBoolean("IsGenerated");
        
    }
    
    /** Set Over/Under Payment.
    @param IsOverUnderPayment Over-Payment (unallocated) or Under-Payment (partial payment) */
    public void setIsOverUnderPayment (boolean IsOverUnderPayment)
    {
        set_Value ("IsOverUnderPayment", Boolean.valueOf(IsOverUnderPayment));
        
    }
    
    /** Get Over/Under Payment.
    @return Over-Payment (unallocated) or Under-Payment (partial payment) */
    public boolean isOverUnderPayment() 
    {
        return get_ValueAsBoolean("IsOverUnderPayment");
        
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
    
    /** Set Write-off Amount.
    @param WriteOffAmt Amount to write-off */
    public void setWriteOffAmt (java.math.BigDecimal WriteOffAmt)
    {
        set_Value ("WriteOffAmt", WriteOffAmt);
        
    }
    
    /** Get Write-off Amount.
    @return Amount to write-off */
    public java.math.BigDecimal getWriteOffAmt() 
    {
        return get_ValueAsBigDecimal("WriteOffAmt");
        
    }
    
    
}
