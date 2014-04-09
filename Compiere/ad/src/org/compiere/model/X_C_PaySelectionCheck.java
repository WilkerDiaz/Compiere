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
/** Generated Model for C_PaySelectionCheck
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_C_PaySelectionCheck.java 8975 2010-06-24 06:10:26Z ragrawal $ */
public class X_C_PaySelectionCheck extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_PaySelectionCheck_ID id
    @param trx transaction
    */
    public X_C_PaySelectionCheck (Ctx ctx, int C_PaySelectionCheck_ID, Trx trx)
    {
        super (ctx, C_PaySelectionCheck_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_PaySelectionCheck_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_PaySelectionCheck_ID (0);
            setC_PaySelection_ID (0);
            setDiscountAmt (Env.ZERO);
            setIsPrinted (false);
            setIsReceipt (false);
            setPayAmt (Env.ZERO);
            setPaymentRule (null);
            setProcessed (false);	// N
            setQty (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_PaySelectionCheck (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27559439501789L;
    /** Last Updated Timestamp 2010-06-23 22:59:45.0 */
    public static final long updatedMS = 1277314185000L;
    /** AD_Table_ID=525 */
    public static final int Table_ID=525;
    
    /** TableName=C_PaySelectionCheck */
    public static final String Table_Name="C_PaySelectionCheck";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Partner Bank Account.
    @param C_BP_BankAccount_ID Bank Account of the Business Partner */
    public void setC_BP_BankAccount_ID (int C_BP_BankAccount_ID)
    {
        if (C_BP_BankAccount_ID <= 0) set_Value ("C_BP_BankAccount_ID", null);
        else
        set_Value ("C_BP_BankAccount_ID", Integer.valueOf(C_BP_BankAccount_ID));
        
    }
    
    /** Get Partner Bank Account.
    @return Bank Account of the Business Partner */
    public int getC_BP_BankAccount_ID() 
    {
        return get_ValueAsInt("C_BP_BankAccount_ID");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Pay Selection Check.
    @param C_PaySelectionCheck_ID Payment Selection Check */
    public void setC_PaySelectionCheck_ID (int C_PaySelectionCheck_ID)
    {
        if (C_PaySelectionCheck_ID < 1) throw new IllegalArgumentException ("C_PaySelectionCheck_ID is mandatory.");
        set_ValueNoCheck ("C_PaySelectionCheck_ID", Integer.valueOf(C_PaySelectionCheck_ID));
        
    }
    
    /** Get Pay Selection Check.
    @return Payment Selection Check */
    public int getC_PaySelectionCheck_ID() 
    {
        return get_ValueAsInt("C_PaySelectionCheck_ID");
        
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
    
    /** Set Payment.
    @param C_Payment_ID Payment identifier */
    public void setC_Payment_ID (int C_Payment_ID)
    {
        if (C_Payment_ID <= 0) set_Value ("C_Payment_ID", null);
        else
        set_Value ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
    }
    
    /** Get Payment.
    @return Payment identifier */
    public int getC_Payment_ID() 
    {
        return get_ValueAsInt("C_Payment_ID");
        
    }
    
    /** Set Check No.
    @param CheckNo Check Number */
    public void setCheckNo (String CheckNo)
    {
        set_Value ("CheckNo", CheckNo);
        
    }
    
    /** Get Check No.
    @return Check Number */
    public String getCheckNo() 
    {
        return (String)get_Value("CheckNo");
        
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
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Set Printed.
    @param IsPrinted Indicates if this document / line is printed */
    public void setIsPrinted (boolean IsPrinted)
    {
        set_Value ("IsPrinted", Boolean.valueOf(IsPrinted));
        
    }
    
    /** Get Printed.
    @return Indicates if this document / line is printed */
    public boolean isPrinted() 
    {
        return get_ValueAsBoolean("IsPrinted");
        
    }
    
    /** Set Receipt.
    @param IsReceipt This is a sales transaction (receipt) */
    public void setIsReceipt (boolean IsReceipt)
    {
        set_Value ("IsReceipt", Boolean.valueOf(IsReceipt));
        
    }
    
    /** Get Receipt.
    @return This is a sales transaction (receipt) */
    public boolean isReceipt() 
    {
        return get_ValueAsBoolean("IsReceipt");
        
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
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (int Qty)
    {
        set_Value ("Qty", Integer.valueOf(Qty));
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public int getQty() 
    {
        return get_ValueAsInt("Qty");
        
    }
    
    
}
