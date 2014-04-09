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
/** Generated Model for C_Payment
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Payment.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Payment extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Payment_ID id
    @param trx transaction
    */
    public X_C_Payment (Ctx ctx, int C_Payment_ID, Trx trx)
    {
        super (ctx, C_Payment_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Payment_ID == 0)
        {
            setC_BankAccount_ID (0);
            setC_Currency_ID (0);
            setC_DocType_ID (0);
            setC_Payment_ID (0);
            setDateAcct (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDateTrx (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDocAction (null);	// CO
            setDocStatus (null);	// DR
            setDocumentNo (null);
            setIsAllocated (false);
            setIsApproved (false);	// N
            setIsDelayedCapture (false);
            setIsOnline (false);
            setIsOverUnderPayment (false);	// N
            setIsPrepayment (false);
            setIsReceipt (false);
            setIsReconciled (false);
            setIsSelfService (false);
            setPayAmt (Env.ZERO);	// 0
            setPosted (false);	// N
            setProcessed (false);	// N
            setTenderType (null);	// K
            setTrxType (null);	// S
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Payment (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27509346618789L;
    /** Last Updated Timestamp 2008-11-20 14:48:22.0 */
    public static final long updatedMS = 1227221302000L;
    /** AD_Table_ID=335 */
    public static final int Table_ID=335;
    
    /** TableName=C_Payment */
    public static final String Table_Name="C_Payment";
    
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
    
    /** Set Account City.
    @param A_City City or the Credit Card or Account Holder */
    public void setA_City (String A_City)
    {
        set_Value ("A_City", A_City);
        
    }
    
    /** Get Account City.
    @return City or the Credit Card or Account Holder */
    public String getA_City() 
    {
        return (String)get_Value("A_City");
        
    }
    
    /** Set Account Country.
    @param A_Country Country */
    public void setA_Country (String A_Country)
    {
        set_Value ("A_Country", A_Country);
        
    }
    
    /** Get Account Country.
    @return Country */
    public String getA_Country() 
    {
        return (String)get_Value("A_Country");
        
    }
    
    /** Set Account EMail.
    @param A_EMail Email Address */
    public void setA_EMail (String A_EMail)
    {
        set_Value ("A_EMail", A_EMail);
        
    }
    
    /** Get Account EMail.
    @return Email Address */
    public String getA_EMail() 
    {
        return (String)get_Value("A_EMail");
        
    }
    
    /** Set Driver License.
    @param A_Ident_DL Payment Identification - Driver License */
    public void setA_Ident_DL (String A_Ident_DL)
    {
        set_Value ("A_Ident_DL", A_Ident_DL);
        
    }
    
    /** Get Driver License.
    @return Payment Identification - Driver License */
    public String getA_Ident_DL() 
    {
        return (String)get_Value("A_Ident_DL");
        
    }
    
    /** Set Social Security No.
    @param A_Ident_SSN Payment Identification - Social Security No */
    public void setA_Ident_SSN (String A_Ident_SSN)
    {
        set_Value ("A_Ident_SSN", A_Ident_SSN);
        
    }
    
    /** Get Social Security No.
    @return Payment Identification - Social Security No */
    public String getA_Ident_SSN() 
    {
        return (String)get_Value("A_Ident_SSN");
        
    }
    
    /** Set Account Name.
    @param A_Name Name on Credit Card or Account holder */
    public void setA_Name (String A_Name)
    {
        set_Value ("A_Name", A_Name);
        
    }
    
    /** Get Account Name.
    @return Name on Credit Card or Account holder */
    public String getA_Name() 
    {
        return (String)get_Value("A_Name");
        
    }
    
    /** Set Account State.
    @param A_State State of the Credit Card or Account holder */
    public void setA_State (String A_State)
    {
        set_Value ("A_State", A_State);
        
    }
    
    /** Get Account State.
    @return State of the Credit Card or Account holder */
    public String getA_State() 
    {
        return (String)get_Value("A_State");
        
    }
    
    /** Set Account Street.
    @param A_Street Street address of the Credit Card or Account holder */
    public void setA_Street (String A_Street)
    {
        set_Value ("A_Street", A_Street);
        
    }
    
    /** Get Account Street.
    @return Street address of the Credit Card or Account holder */
    public String getA_Street() 
    {
        return (String)get_Value("A_Street");
        
    }
    
    /** Set Account Zip/Postal.
    @param A_Zip Zip Code of the Credit Card or Account Holder */
    public void setA_Zip (String A_Zip)
    {
        set_Value ("A_Zip", A_Zip);
        
    }
    
    /** Get Account Zip/Postal.
    @return Zip Code of the Credit Card or Account Holder */
    public String getA_Zip() 
    {
        return (String)get_Value("A_Zip");
        
    }
    
    /** Set Account No.
    @param AccountNo Account Number */
    public void setAccountNo (String AccountNo)
    {
        set_Value ("AccountNo", AccountNo);
        
    }
    
    /** Get Account No.
    @return Account Number */
    public String getAccountNo() 
    {
        return (String)get_Value("AccountNo");
        
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
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Bank Account.
    @param C_BankAccount_ID Account at the Bank */
    public void setC_BankAccount_ID (int C_BankAccount_ID)
    {
        if (C_BankAccount_ID < 1) throw new IllegalArgumentException ("C_BankAccount_ID is mandatory.");
        set_Value ("C_BankAccount_ID", Integer.valueOf(C_BankAccount_ID));
        
    }
    
    /** Get Bank Account.
    @return Account at the Bank */
    public int getC_BankAccount_ID() 
    {
        return get_ValueAsInt("C_BankAccount_ID");
        
    }
    
    /** Set Campaign.
    @param C_Campaign_ID Marketing Campaign */
    public void setC_Campaign_ID (int C_Campaign_ID)
    {
        if (C_Campaign_ID <= 0) set_Value ("C_Campaign_ID", null);
        else
        set_Value ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
    }
    
    /** Get Campaign.
    @return Marketing Campaign */
    public int getC_Campaign_ID() 
    {
        return get_ValueAsInt("C_Campaign_ID");
        
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
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID < 0) throw new IllegalArgumentException ("C_DocType_ID is mandatory.");
        set_Value ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
    }
    
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID <= 0) set_Value ("C_Invoice_ID", null);
        else
        set_Value ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
    }
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_Value ("C_Order_ID", null);
        else
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Payment Batch.
    @param C_PaymentBatch_ID Payment batch for EFT */
    public void setC_PaymentBatch_ID (int C_PaymentBatch_ID)
    {
        if (C_PaymentBatch_ID <= 0) set_Value ("C_PaymentBatch_ID", null);
        else
        set_Value ("C_PaymentBatch_ID", Integer.valueOf(C_PaymentBatch_ID));
        
    }
    
    /** Get Payment Batch.
    @return Payment batch for EFT */
    public int getC_PaymentBatch_ID() 
    {
        return get_ValueAsInt("C_PaymentBatch_ID");
        
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
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
        else
        set_Value ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
    }
    
    /** Set Charge amount.
    @param ChargeAmt Charge Amount */
    public void setChargeAmt (java.math.BigDecimal ChargeAmt)
    {
        set_Value ("ChargeAmt", ChargeAmt);
        
    }
    
    /** Get Charge amount.
    @return Charge Amount */
    public java.math.BigDecimal getChargeAmt() 
    {
        return get_ValueAsBigDecimal("ChargeAmt");
        
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
    
    /** Set Exp. Month.
    @param CreditCardExpMM Expiry Month */
    public void setCreditCardExpMM (int CreditCardExpMM)
    {
        set_Value ("CreditCardExpMM", Integer.valueOf(CreditCardExpMM));
        
    }
    
    /** Get Exp. Month.
    @return Expiry Month */
    public int getCreditCardExpMM() 
    {
        return get_ValueAsInt("CreditCardExpMM");
        
    }
    
    /** Set Exp. Year.
    @param CreditCardExpYY Expiry Year */
    public void setCreditCardExpYY (int CreditCardExpYY)
    {
        set_Value ("CreditCardExpYY", Integer.valueOf(CreditCardExpYY));
        
    }
    
    /** Get Exp. Year.
    @return Expiry Year */
    public int getCreditCardExpYY() 
    {
        return get_ValueAsInt("CreditCardExpYY");
        
    }
    
    /** Set Number.
    @param CreditCardNumber Credit Card Number */
    public void setCreditCardNumber (String CreditCardNumber)
    {
        set_Value ("CreditCardNumber", CreditCardNumber);
        
    }
    
    /** Get Number.
    @return Credit Card Number */
    public String getCreditCardNumber() 
    {
        return (String)get_Value("CreditCardNumber");
        
    }
    
    /** Amex = A */
    public static final String CREDITCARDTYPE_Amex = X_Ref_C_Payment_CreditCard_Type.AMEX.getValue();
    /** ATM = C */
    public static final String CREDITCARDTYPE_ATM = X_Ref_C_Payment_CreditCard_Type.ATM.getValue();
    /** Diners = D */
    public static final String CREDITCARDTYPE_Diners = X_Ref_C_Payment_CreditCard_Type.DINERS.getValue();
    /** MasterCard = M */
    public static final String CREDITCARDTYPE_MasterCard = X_Ref_C_Payment_CreditCard_Type.MASTER_CARD.getValue();
    /** Discover = N */
    public static final String CREDITCARDTYPE_Discover = X_Ref_C_Payment_CreditCard_Type.DISCOVER.getValue();
    /** Purchase Card = P */
    public static final String CREDITCARDTYPE_PurchaseCard = X_Ref_C_Payment_CreditCard_Type.PURCHASE_CARD.getValue();
    /** Visa = V */
    public static final String CREDITCARDTYPE_Visa = X_Ref_C_Payment_CreditCard_Type.VISA.getValue();
    /** Set Credit Card.
    @param CreditCardType Credit Card (Visa, MC, Amex) */
    public void setCreditCardType (String CreditCardType)
    {
        if (!X_Ref_C_Payment_CreditCard_Type.isValid(CreditCardType))
        throw new IllegalArgumentException ("CreditCardType Invalid value - " + CreditCardType + " - Reference_ID=149 - A - C - D - M - N - P - V");
        set_Value ("CreditCardType", CreditCardType);
        
    }
    
    /** Get Credit Card.
    @return Credit Card (Visa, MC, Amex) */
    public String getCreditCardType() 
    {
        return (String)get_Value("CreditCardType");
        
    }
    
    /** Set Account Date.
    @param DateAcct General Ledger Date */
    public void setDateAcct (Timestamp DateAcct)
    {
        if (DateAcct == null) throw new IllegalArgumentException ("DateAcct is mandatory.");
        set_Value ("DateAcct", DateAcct);
        
    }
    
    /** Get Account Date.
    @return General Ledger Date */
    public Timestamp getDateAcct() 
    {
        return (Timestamp)get_Value("DateAcct");
        
    }
    
    /** Set Transaction Date.
    @param DateTrx Transaction Date */
    public void setDateTrx (Timestamp DateTrx)
    {
        if (DateTrx == null) throw new IllegalArgumentException ("DateTrx is mandatory.");
        set_Value ("DateTrx", DateTrx);
        
    }
    
    /** Get Transaction Date.
    @return Transaction Date */
    public Timestamp getDateTrx() 
    {
        return (Timestamp)get_Value("DateTrx");
        
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
    
    /** <None> = -- */
    public static final String DOCACTION_None = X_Ref__Document_Action.NONE.getValue();
    /** Approve = AP */
    public static final String DOCACTION_Approve = X_Ref__Document_Action.APPROVE.getValue();
    /** Close = CL */
    public static final String DOCACTION_Close = X_Ref__Document_Action.CLOSE.getValue();
    /** Complete = CO */
    public static final String DOCACTION_Complete = X_Ref__Document_Action.COMPLETE.getValue();
    /** Invalidate = IN */
    public static final String DOCACTION_Invalidate = X_Ref__Document_Action.INVALIDATE.getValue();
    /** Post = PO */
    public static final String DOCACTION_Post = X_Ref__Document_Action.POST.getValue();
    /** Prepare = PR */
    public static final String DOCACTION_Prepare = X_Ref__Document_Action.PREPARE.getValue();
    /** Reverse - Accrual = RA */
    public static final String DOCACTION_Reverse_Accrual = X_Ref__Document_Action.REVERSE__ACCRUAL.getValue();
    /** Reverse - Correct = RC */
    public static final String DOCACTION_Reverse_Correct = X_Ref__Document_Action.REVERSE__CORRECT.getValue();
    /** Re-activate = RE */
    public static final String DOCACTION_Re_Activate = X_Ref__Document_Action.RE__ACTIVATE.getValue();
    /** Reject = RJ */
    public static final String DOCACTION_Reject = X_Ref__Document_Action.REJECT.getValue();
    /** Void = VO */
    public static final String DOCACTION_Void = X_Ref__Document_Action.VOID.getValue();
    /** Wait Complete = WC */
    public static final String DOCACTION_WaitComplete = X_Ref__Document_Action.WAIT_COMPLETE.getValue();
    /** Unlock = XL */
    public static final String DOCACTION_Unlock = X_Ref__Document_Action.UNLOCK.getValue();
    /** Set Document Action.
    @param DocAction The targeted status of the document */
    public void setDocAction (String DocAction)
    {
        if (DocAction == null) throw new IllegalArgumentException ("DocAction is mandatory");
        if (!X_Ref__Document_Action.isValid(DocAction))
        throw new IllegalArgumentException ("DocAction Invalid value - " + DocAction + " - Reference_ID=135 - -- - AP - CL - CO - IN - PO - PR - RA - RC - RE - RJ - VO - WC - XL");
        set_Value ("DocAction", DocAction);
        
    }
    
    /** Get Document Action.
    @return The targeted status of the document */
    public String getDocAction() 
    {
        return (String)get_Value("DocAction");
        
    }
    
    /** Unknown = ?? */
    public static final String DOCSTATUS_Unknown = X_Ref__Document_Status.UNKNOWN.getValue();
    /** Approved = AP */
    public static final String DOCSTATUS_Approved = X_Ref__Document_Status.APPROVED.getValue();
    /** Closed = CL */
    public static final String DOCSTATUS_Closed = X_Ref__Document_Status.CLOSED.getValue();
    /** Completed = CO */
    public static final String DOCSTATUS_Completed = X_Ref__Document_Status.COMPLETED.getValue();
    /** Drafted = DR */
    public static final String DOCSTATUS_Drafted = X_Ref__Document_Status.DRAFTED.getValue();
    /** Invalid = IN */
    public static final String DOCSTATUS_Invalid = X_Ref__Document_Status.INVALID.getValue();
    /** In Progress = IP */
    public static final String DOCSTATUS_InProgress = X_Ref__Document_Status.IN_PROGRESS.getValue();
    /** Not Approved = NA */
    public static final String DOCSTATUS_NotApproved = X_Ref__Document_Status.NOT_APPROVED.getValue();
    /** Reversed = RE */
    public static final String DOCSTATUS_Reversed = X_Ref__Document_Status.REVERSED.getValue();
    /** Voided = VO */
    public static final String DOCSTATUS_Voided = X_Ref__Document_Status.VOIDED.getValue();
    /** Waiting Confirmation = WC */
    public static final String DOCSTATUS_WaitingConfirmation = X_Ref__Document_Status.WAITING_CONFIRMATION.getValue();
    /** Waiting Payment = WP */
    public static final String DOCSTATUS_WaitingPayment = X_Ref__Document_Status.WAITING_PAYMENT.getValue();
    /** Set Document Status.
    @param DocStatus The current status of the document */
    public void setDocStatus (String DocStatus)
    {
        if (DocStatus == null) throw new IllegalArgumentException ("DocStatus is mandatory");
        if (!X_Ref__Document_Status.isValid(DocStatus))
        throw new IllegalArgumentException ("DocStatus Invalid value - " + DocStatus + " - Reference_ID=131 - ?? - AP - CL - CO - DR - IN - IP - NA - RE - VO - WC - WP");
        set_Value ("DocStatus", DocStatus);
        
    }
    
    /** Get Document Status.
    @return The current status of the document */
    public String getDocStatus() 
    {
        return (String)get_Value("DocStatus");
        
    }
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory.");
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
        
    }
    
    /** Set Allocated.
    @param IsAllocated Indicates if the payment has been allocated */
    public void setIsAllocated (boolean IsAllocated)
    {
        set_Value ("IsAllocated", Boolean.valueOf(IsAllocated));
        
    }
    
    /** Get Allocated.
    @return Indicates if the payment has been allocated */
    public boolean isAllocated() 
    {
        return get_ValueAsBoolean("IsAllocated");
        
    }
    
    /** Set Approved.
    @param IsApproved Indicates if this document requires approval */
    public void setIsApproved (boolean IsApproved)
    {
        set_ValueNoCheck ("IsApproved", Boolean.valueOf(IsApproved));
        
    }
    
    /** Get Approved.
    @return Indicates if this document requires approval */
    public boolean isApproved() 
    {
        return get_ValueAsBoolean("IsApproved");
        
    }
    
    /** Set Delayed Capture.
    @param IsDelayedCapture Charge after Shipment */
    public void setIsDelayedCapture (boolean IsDelayedCapture)
    {
        set_Value ("IsDelayedCapture", Boolean.valueOf(IsDelayedCapture));
        
    }
    
    /** Get Delayed Capture.
    @return Charge after Shipment */
    public boolean isDelayedCapture() 
    {
        return get_ValueAsBoolean("IsDelayedCapture");
        
    }
    
    /** Set Online Access.
    @param IsOnline Can be accessed online */
    public void setIsOnline (boolean IsOnline)
    {
        set_Value ("IsOnline", Boolean.valueOf(IsOnline));
        
    }
    
    /** Get Online Access.
    @return Can be accessed online */
    public boolean isOnline() 
    {
        return get_ValueAsBoolean("IsOnline");
        
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
    
    /** Set Prepayment.
    @param IsPrepayment The Payment/Receipt is a Prepayment */
    public void setIsPrepayment (boolean IsPrepayment)
    {
        set_Value ("IsPrepayment", Boolean.valueOf(IsPrepayment));
        
    }
    
    /** Get Prepayment.
    @return The Payment/Receipt is a Prepayment */
    public boolean isPrepayment() 
    {
        return get_ValueAsBoolean("IsPrepayment");
        
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
    
    /** Set Reconciled.
    @param IsReconciled Payment is reconciled with bank statement */
    public void setIsReconciled (boolean IsReconciled)
    {
        set_Value ("IsReconciled", Boolean.valueOf(IsReconciled));
        
    }
    
    /** Get Reconciled.
    @return Payment is reconciled with bank statement */
    public boolean isReconciled() 
    {
        return get_ValueAsBoolean("IsReconciled");
        
    }
    
    /** Set Self-Service.
    @param IsSelfService This is a Self-Service entry or this entry can be changed via Self-Service */
    public void setIsSelfService (boolean IsSelfService)
    {
        set_Value ("IsSelfService", Boolean.valueOf(IsSelfService));
        
    }
    
    /** Get Self-Service.
    @return This is a Self-Service entry or this entry can be changed via Self-Service */
    public boolean isSelfService() 
    {
        return get_ValueAsBoolean("IsSelfService");
        
    }
    
    /** Set Micr.
    @param Micr Combination of routing no, account and check no */
    public void setMicr (String Micr)
    {
        set_Value ("Micr", Micr);
        
    }
    
    /** Get Micr.
    @return Combination of routing no, account and check no */
    public String getMicr() 
    {
        return (String)get_Value("Micr");
        
    }
    
    /** Set Online Processing.
    @param OProcessing This payment can be processed online */
    public void setOProcessing (String OProcessing)
    {
        set_Value ("OProcessing", OProcessing);
        
    }
    
    /** Get Online Processing.
    @return This payment can be processed online */
    public String getOProcessing() 
    {
        return (String)get_Value("OProcessing");
        
    }
    
    /** Set Original Transaction ID.
    @param Orig_TrxID Original Transaction ID */
    public void setOrig_TrxID (String Orig_TrxID)
    {
        set_Value ("Orig_TrxID", Orig_TrxID);
        
    }
    
    /** Get Original Transaction ID.
    @return Original Transaction ID */
    public String getOrig_TrxID() 
    {
        return (String)get_Value("Orig_TrxID");
        
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
    
    /** Set PO Number.
    @param PONum Purchase Order Number */
    public void setPONum (String PONum)
    {
        set_Value ("PONum", PONum);
        
    }
    
    /** Get PO Number.
    @return Purchase Order Number */
    public String getPONum() 
    {
        return (String)get_Value("PONum");
        
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
    
    /** Set Posted.
    @param Posted Posting status */
    public void setPosted (boolean Posted)
    {
        set_ValueNoCheck ("Posted", Boolean.valueOf(Posted));
        
    }
    
    /** Get Posted.
    @return Posting status */
    public boolean isPosted() 
    {
        return get_ValueAsBoolean("Posted");
        
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
    
    /** Set Authorization Code.
    @param R_AuthCode Authorization Code returned */
    public void setR_AuthCode (String R_AuthCode)
    {
        set_ValueNoCheck ("R_AuthCode", R_AuthCode);
        
    }
    
    /** Get Authorization Code.
    @return Authorization Code returned */
    public String getR_AuthCode() 
    {
        return (String)get_Value("R_AuthCode");
        
    }
    
    /** Set Authorization Code (DC).
    @param R_AuthCode_DC Authorization Code Delayed Capture returned */
    public void setR_AuthCode_DC (String R_AuthCode_DC)
    {
        set_ValueNoCheck ("R_AuthCode_DC", R_AuthCode_DC);
        
    }
    
    /** Get Authorization Code (DC).
    @return Authorization Code Delayed Capture returned */
    public String getR_AuthCode_DC() 
    {
        return (String)get_Value("R_AuthCode_DC");
        
    }
    
    /** No Match = N */
    public static final String R_AVSADDR_NoMatch = X_Ref_C_Payment_AVS.NO_MATCH.getValue();
    /** Unavailable = X */
    public static final String R_AVSADDR_Unavailable = X_Ref_C_Payment_AVS.UNAVAILABLE.getValue();
    /** Match = Y */
    public static final String R_AVSADDR_Match = X_Ref_C_Payment_AVS.MATCH.getValue();
    /** Set Address verified.
    @param R_AvsAddr This address has been verified */
    public void setR_AvsAddr (String R_AvsAddr)
    {
        if (!X_Ref_C_Payment_AVS.isValid(R_AvsAddr))
        throw new IllegalArgumentException ("R_AvsAddr Invalid value - " + R_AvsAddr + " - Reference_ID=213 - N - X - Y");
        set_ValueNoCheck ("R_AvsAddr", R_AvsAddr);
        
    }
    
    /** Get Address verified.
    @return This address has been verified */
    public String getR_AvsAddr() 
    {
        return (String)get_Value("R_AvsAddr");
        
    }
    
    /** No Match = N */
    public static final String R_AVSZIP_NoMatch = X_Ref_C_Payment_AVS.NO_MATCH.getValue();
    /** Unavailable = X */
    public static final String R_AVSZIP_Unavailable = X_Ref_C_Payment_AVS.UNAVAILABLE.getValue();
    /** Match = Y */
    public static final String R_AVSZIP_Match = X_Ref_C_Payment_AVS.MATCH.getValue();
    /** Set Zip verified.
    @param R_AvsZip The Zip Code has been verified */
    public void setR_AvsZip (String R_AvsZip)
    {
        if (!X_Ref_C_Payment_AVS.isValid(R_AvsZip))
        throw new IllegalArgumentException ("R_AvsZip Invalid value - " + R_AvsZip + " - Reference_ID=213 - N - X - Y");
        set_ValueNoCheck ("R_AvsZip", R_AvsZip);
        
    }
    
    /** Get Zip verified.
    @return The Zip Code has been verified */
    public String getR_AvsZip() 
    {
        return (String)get_Value("R_AvsZip");
        
    }
    
    /** Set CVV Match.
    @param R_CVV2Match Credit Card Verification Code Match */
    public void setR_CVV2Match (boolean R_CVV2Match)
    {
        set_ValueNoCheck ("R_CVV2Match", Boolean.valueOf(R_CVV2Match));
        
    }
    
    /** Get CVV Match.
    @return Credit Card Verification Code Match */
    public boolean isR_CVV2Match() 
    {
        return get_ValueAsBoolean("R_CVV2Match");
        
    }
    
    /** Set Info.
    @param R_Info Response info */
    public void setR_Info (String R_Info)
    {
        set_ValueNoCheck ("R_Info", R_Info);
        
    }
    
    /** Get Info.
    @return Response info */
    public String getR_Info() 
    {
        return (String)get_Value("R_Info");
        
    }
    
    /** Set Reference.
    @param R_PnRef Payment reference */
    public void setR_PnRef (String R_PnRef)
    {
        set_ValueNoCheck ("R_PnRef", R_PnRef);
        
    }
    
    /** Get Reference.
    @return Payment reference */
    public String getR_PnRef() 
    {
        return (String)get_Value("R_PnRef");
        
    }
    
    /** Set Reference (DC).
    @param R_PnRef_DC Payment Reference Delayed Capture */
    public void setR_PnRef_DC (String R_PnRef_DC)
    {
        set_ValueNoCheck ("R_PnRef_DC", R_PnRef_DC);
        
    }
    
    /** Get Reference (DC).
    @return Payment Reference Delayed Capture */
    public String getR_PnRef_DC() 
    {
        return (String)get_Value("R_PnRef_DC");
        
    }
    
    /** Set Response Message.
    @param R_RespMsg Response message */
    public void setR_RespMsg (String R_RespMsg)
    {
        set_ValueNoCheck ("R_RespMsg", R_RespMsg);
        
    }
    
    /** Get Response Message.
    @return Response message */
    public String getR_RespMsg() 
    {
        return (String)get_Value("R_RespMsg");
        
    }
    
    /** Set Result.
    @param R_Result Result of transmission */
    public void setR_Result (String R_Result)
    {
        set_ValueNoCheck ("R_Result", R_Result);
        
    }
    
    /** Get Result.
    @return Result of transmission */
    public String getR_Result() 
    {
        return (String)get_Value("R_Result");
        
    }
    
    /** Set Referenced Payment.
    @param Ref_Payment_ID Referenced Payment */
    public void setRef_Payment_ID (int Ref_Payment_ID)
    {
        if (Ref_Payment_ID <= 0) set_ValueNoCheck ("Ref_Payment_ID", null);
        else
        set_ValueNoCheck ("Ref_Payment_ID", Integer.valueOf(Ref_Payment_ID));
        
    }
    
    /** Get Referenced Payment.
    @return Referenced Payment */
    public int getRef_Payment_ID() 
    {
        return get_ValueAsInt("Ref_Payment_ID");
        
    }
    
    /** Set Routing No.
    @param RoutingNo Bank Routing Number */
    public void setRoutingNo (String RoutingNo)
    {
        set_Value ("RoutingNo", RoutingNo);
        
    }
    
    /** Get Routing No.
    @return Bank Routing Number */
    public String getRoutingNo() 
    {
        return (String)get_Value("RoutingNo");
        
    }
    
    /** Set Swipe.
    @param Swipe Track 1 and 2 of the Credit Card */
    public void setSwipe (String Swipe)
    {
        set_ValueNoCheck ("Swipe", Swipe);
        
    }
    
    /** Get Swipe.
    @return Track 1 and 2 of the Credit Card */
    public String getSwipe() 
    {
        return (String)get_Value("Swipe");
        
    }
    
    /** Set Tax Amount.
    @param TaxAmt Tax Amount for a document */
    public void setTaxAmt (java.math.BigDecimal TaxAmt)
    {
        set_Value ("TaxAmt", TaxAmt);
        
    }
    
    /** Get Tax Amount.
    @return Tax Amount for a document */
    public java.math.BigDecimal getTaxAmt() 
    {
        return get_ValueAsBigDecimal("TaxAmt");
        
    }
    
    /** Direct Deposit = A */
    public static final String TENDERTYPE_DirectDeposit = X_Ref_C_Payment_Tender_Type.DIRECT_DEPOSIT.getValue();
    /** Credit Card = C */
    public static final String TENDERTYPE_CreditCard = X_Ref_C_Payment_Tender_Type.CREDIT_CARD.getValue();
    /** Direct Debit = D */
    public static final String TENDERTYPE_DirectDebit = X_Ref_C_Payment_Tender_Type.DIRECT_DEBIT.getValue();
    /** Check = K */
    public static final String TENDERTYPE_Check = X_Ref_C_Payment_Tender_Type.CHECK.getValue();
    /** Set Tender type.
    @param TenderType Method of Payment */
    public void setTenderType (String TenderType)
    {
        if (TenderType == null) throw new IllegalArgumentException ("TenderType is mandatory");
        if (!X_Ref_C_Payment_Tender_Type.isValid(TenderType))
        throw new IllegalArgumentException ("TenderType Invalid value - " + TenderType + " - Reference_ID=214 - A - C - D - K");
        set_Value ("TenderType", TenderType);
        
    }
    
    /** Get Tender type.
    @return Method of Payment */
    public String getTenderType() 
    {
        return (String)get_Value("TenderType");
        
    }
    
    /** Authorization = A */
    public static final String TRXTYPE_Authorization = X_Ref_C_Payment_Trx_Type.AUTHORIZATION.getValue();
    /** Credit (Payment) = C */
    public static final String TRXTYPE_CreditPayment = X_Ref_C_Payment_Trx_Type.CREDIT_PAYMENT.getValue();
    /** Delayed Capture = D */
    public static final String TRXTYPE_DelayedCapture = X_Ref_C_Payment_Trx_Type.DELAYED_CAPTURE.getValue();
    /** Voice Authorization = F */
    public static final String TRXTYPE_VoiceAuthorization = X_Ref_C_Payment_Trx_Type.VOICE_AUTHORIZATION.getValue();
    /** Sales = S */
    public static final String TRXTYPE_Sales = X_Ref_C_Payment_Trx_Type.SALES.getValue();
    /** Void = V */
    public static final String TRXTYPE_Void = X_Ref_C_Payment_Trx_Type.VOID.getValue();
    /** Set Transaction Type.
    @param TrxType Type of credit card transaction */
    public void setTrxType (String TrxType)
    {
        if (TrxType == null) throw new IllegalArgumentException ("TrxType is mandatory");
        if (!X_Ref_C_Payment_Trx_Type.isValid(TrxType))
        throw new IllegalArgumentException ("TrxType Invalid value - " + TrxType + " - Reference_ID=215 - A - C - D - F - S - V");
        set_Value ("TrxType", TrxType);
        
    }
    
    /** Get Transaction Type.
    @return Type of credit card transaction */
    public String getTrxType() 
    {
        return (String)get_Value("TrxType");
        
    }
    
    /** Set User List 1.
    @param User1_ID User defined list element #1 */
    public void setUser1_ID (int User1_ID)
    {
        if (User1_ID <= 0) set_Value ("User1_ID", null);
        else
        set_Value ("User1_ID", Integer.valueOf(User1_ID));
        
    }
    
    /** Get User List 1.
    @return User defined list element #1 */
    public int getUser1_ID() 
    {
        return get_ValueAsInt("User1_ID");
        
    }
    
    /** Set User List 2.
    @param User2_ID User defined list element #2 */
    public void setUser2_ID (int User2_ID)
    {
        if (User2_ID <= 0) set_Value ("User2_ID", null);
        else
        set_Value ("User2_ID", Integer.valueOf(User2_ID));
        
    }
    
    /** Get User List 2.
    @return User defined list element #2 */
    public int getUser2_ID() 
    {
        return get_ValueAsInt("User2_ID");
        
    }
    
    /** Set Voice authorization code.
    @param VoiceAuthCode Voice Authorization Code from credit card company */
    public void setVoiceAuthCode (String VoiceAuthCode)
    {
        set_Value ("VoiceAuthCode", VoiceAuthCode);
        
    }
    
    /** Get Voice authorization code.
    @return Voice Authorization Code from credit card company */
    public String getVoiceAuthCode() 
    {
        return (String)get_Value("VoiceAuthCode");
        
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
