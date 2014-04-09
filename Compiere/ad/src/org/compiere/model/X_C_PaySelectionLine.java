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
/** Generated Model for C_PaySelectionLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_PaySelectionLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_PaySelectionLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_PaySelectionLine_ID id
    @param trx transaction
    */
    public X_C_PaySelectionLine (Ctx ctx, int C_PaySelectionLine_ID, Trx trx)
    {
        super (ctx, C_PaySelectionLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_PaySelectionLine_ID == 0)
        {
            setC_Invoice_ID (0);
            setC_PaySelectionLine_ID (0);
            setC_PaySelection_ID (0);
            setDifferenceAmt (Env.ZERO);
            setDiscountAmt (Env.ZERO);
            setIsManual (false);
            setIsSOTrx (false);
            setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_PaySelectionLine WHERE C_PaySelection_ID=@C_PaySelection_ID@
            setOpenAmt (Env.ZERO);
            setPayAmt (Env.ZERO);
            setPaymentRule (null);	// S
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_PaySelectionLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27529768289789L;
    /** Last Updated Timestamp 2009-07-15 00:29:33.0 */
    public static final long updatedMS = 1247642973000L;
    /** AD_Table_ID=427 */
    public static final int Table_ID=427;
    
    /** TableName=C_PaySelectionLine */
    public static final String Table_Name="C_PaySelectionLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
        else
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
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
        if (C_Invoice_ID < 1) throw new IllegalArgumentException ("C_Invoice_ID is mandatory.");
        set_Value ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
    }
    
    /** Set Pay Selection Check.
    @param C_PaySelectionCheck_ID Payment Selection Check */
    public void setC_PaySelectionCheck_ID (int C_PaySelectionCheck_ID)
    {
        if (C_PaySelectionCheck_ID <= 0) set_Value ("C_PaySelectionCheck_ID", null);
        else
        set_Value ("C_PaySelectionCheck_ID", Integer.valueOf(C_PaySelectionCheck_ID));
        
    }
    
    /** Get Pay Selection Check.
    @return Payment Selection Check */
    public int getC_PaySelectionCheck_ID() 
    {
        return get_ValueAsInt("C_PaySelectionCheck_ID");
        
    }
    
    /** Set Payment Selection Line.
    @param C_PaySelectionLine_ID Payment Selection Line */
    public void setC_PaySelectionLine_ID (int C_PaySelectionLine_ID)
    {
        if (C_PaySelectionLine_ID < 1) throw new IllegalArgumentException ("C_PaySelectionLine_ID is mandatory.");
        set_ValueNoCheck ("C_PaySelectionLine_ID", Integer.valueOf(C_PaySelectionLine_ID));
        
    }
    
    /** Get Payment Selection Line.
    @return Payment Selection Line */
    public int getC_PaySelectionLine_ID() 
    {
        return get_ValueAsInt("C_PaySelectionLine_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_PaySelectionLine_ID()));
        
    }
    
    /** Set Payment Selection.
    @param C_PaySelection_ID Payment Selection */
    public void setC_PaySelection_ID (int C_PaySelection_ID)
    {
        if (C_PaySelection_ID < 1) throw new IllegalArgumentException ("C_PaySelection_ID is mandatory.");
        set_ValueNoCheck ("C_PaySelection_ID", Integer.valueOf(C_PaySelection_ID));
        
    }
    
    /** Get Payment Selection.
    @return Payment Selection */
    public int getC_PaySelection_ID() 
    {
        return get_ValueAsInt("C_PaySelection_ID");
        
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
    
    /** Set Difference.
    @param DifferenceAmt Difference Amount */
    public void setDifferenceAmt (java.math.BigDecimal DifferenceAmt)
    {
        if (DifferenceAmt == null) throw new IllegalArgumentException ("DifferenceAmt is mandatory.");
        set_ValueNoCheck ("DifferenceAmt", DifferenceAmt);
        
    }
    
    /** Get Difference.
    @return Difference Amount */
    public java.math.BigDecimal getDifferenceAmt() 
    {
        return get_ValueAsBigDecimal("DifferenceAmt");
        
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
    
    /** Set Cancelled.
    @param IsCancelled The transaction was cancelled */
    public void setIsCancelled (boolean IsCancelled)
    {
        set_Value ("IsCancelled", Boolean.valueOf(IsCancelled));
        
    }
    
    /** Get Cancelled.
    @return The transaction was cancelled */
    public boolean isCancelled() 
    {
        return get_ValueAsBoolean("IsCancelled");
        
    }
    
    /** Set Manual.
    @param IsManual This is a manual process or entry */
    public void setIsManual (boolean IsManual)
    {
        set_Value ("IsManual", Boolean.valueOf(IsManual));
        
    }
    
    /** Get Manual.
    @return This is a manual process or entry */
    public boolean isManual() 
    {
        return get_ValueAsBoolean("IsManual");
        
    }
    
    /** Set Sales Transaction.
    @param IsSOTrx This is a Sales Transaction */
    public void setIsSOTrx (boolean IsSOTrx)
    {
        set_Value ("IsSOTrx", Boolean.valueOf(IsSOTrx));
        
    }
    
    /** Get Sales Transaction.
    @return This is a Sales Transaction */
    public boolean isSOTrx() 
    {
        return get_ValueAsBoolean("IsSOTrx");
        
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
    
    /** Set Open Amount.
    @param OpenAmt Open item amount */
    public void setOpenAmt (java.math.BigDecimal OpenAmt)
    {
        if (OpenAmt == null) throw new IllegalArgumentException ("OpenAmt is mandatory.");
        set_ValueNoCheck ("OpenAmt", OpenAmt);
        
    }
    
    /** Get Open Amount.
    @return Open item amount */
    public java.math.BigDecimal getOpenAmt() 
    {
        return get_ValueAsBigDecimal("OpenAmt");
        
    }
    
    /** Set Payment amount.
    @param PayAmt Amount being paid */
    public void setPayAmt (java.math.BigDecimal PayAmt)
    {
        if (PayAmt == null) throw new IllegalArgumentException ("PayAmt is mandatory.");
        set_Value ("PayAmt", PayAmt);
        
    }
    
    /** Get Payment amount.
    @return Amount being paid */
    public java.math.BigDecimal getPayAmt() 
    {
        return get_ValueAsBigDecimal("PayAmt");
        
    }
    
    /** Cash = B */
    public static final String PAYMENTRULE_Cash = X_Ref__Payment_Rule.CASH.getValue();
    /** Direct Debit = D */
    public static final String PAYMENTRULE_DirectDebit = X_Ref__Payment_Rule.DIRECT_DEBIT.getValue();
    /** Credit Card = K */
    public static final String PAYMENTRULE_CreditCard = X_Ref__Payment_Rule.CREDIT_CARD.getValue();
    /** On Credit = P */
    public static final String PAYMENTRULE_OnCredit = X_Ref__Payment_Rule.ON_CREDIT.getValue();
    /** Check = S */
    public static final String PAYMENTRULE_Check = X_Ref__Payment_Rule.CHECK.getValue();
    /** Direct Deposit = T */
    public static final String PAYMENTRULE_DirectDeposit = X_Ref__Payment_Rule.DIRECT_DEPOSIT.getValue();
    /** Set Payment Method.
    @param PaymentRule How you pay the invoice */
    public void setPaymentRule (String PaymentRule)
    {
        if (PaymentRule == null) throw new IllegalArgumentException ("PaymentRule is mandatory");
        if (!X_Ref__Payment_Rule.isValid(PaymentRule))
        throw new IllegalArgumentException ("PaymentRule Invalid value - " + PaymentRule + " - Reference_ID=195 - B - D - K - P - S - T");
        set_Value ("PaymentRule", PaymentRule);
        
    }
    
    /** Get Payment Method.
    @return How you pay the invoice */
    public String getPaymentRule() 
    {
        return (String)get_Value("PaymentRule");
        
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
    
    
}
