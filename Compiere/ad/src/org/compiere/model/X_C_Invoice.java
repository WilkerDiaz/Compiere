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
/** Generated Model for C_Invoice
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_C_Invoice.java 9090 2010-07-01 16:30:39Z ragrawal $ */
public class X_C_Invoice extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Invoice_ID id
    @param trx transaction
    */
    public X_C_Invoice (Ctx ctx, int C_Invoice_ID, Trx trx)
    {
        super (ctx, C_Invoice_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Invoice_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_BPartner_Location_ID (0);
            setC_Currency_ID (0);	// @C_Currency_ID@
            setC_DocTypeTarget_ID (0);
            setC_DocType_ID (0);	// 0
            setC_Invoice_ID (0);
            setC_PaymentTerm_ID (0);
            setDateAcct (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDateInvoiced (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDocAction (null);	// CO
            setDocStatus (null);	// DR
            setDocumentNo (null);
            setGrandTotal (Env.ZERO);
            setIsApproved (false);	// @IsApproved@
            setIsDiscountPrinted (false);
            setIsInDispute (false);	// N
            setIsPaid (false);
            setIsPayScheduleValid (false);
            setIsPrinted (false);
            setIsReturnTrx (false);	// N
            setIsSOTrx (false);	// @IsSOTrx@
            setIsSelfService (false);
            setIsTaxIncluded (false);
            setIsTransferred (false);
            setM_PriceList_ID (0);
            setPaymentRule (null);	// P
            setPosted (false);	// N
            setProcessed (false);	// N
            setSendEMail (false);
            setTotalLines (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Invoice (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27560080480789L;
    /** Last Updated Timestamp 2010-07-01 09:02:44.0 */
    public static final long updatedMS = 1277955164000L;
    /** AD_Table_ID=318 */
    public static final int Table_ID=318;
    
    /** TableName=C_Invoice */
    public static final String Table_Name="C_Invoice";
    
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
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
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
    
    /** Set Agent.
    @param C_BPartnerSR_ID Business Partner (Agent or Sales Rep) */
    public void setC_BPartnerSR_ID (int C_BPartnerSR_ID)
    {
        if (C_BPartnerSR_ID <= 0) set_Value ("C_BPartnerSR_ID", null);
        else
        set_Value ("C_BPartnerSR_ID", Integer.valueOf(C_BPartnerSR_ID));
        
    }
    
    /** Get Agent.
    @return Business Partner (Agent or Sales Rep) */
    public int getC_BPartnerSR_ID() 
    {
        return get_ValueAsInt("C_BPartnerSR_ID");
        
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
    
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID < 1) throw new IllegalArgumentException ("C_BPartner_Location_ID is mandatory.");
        set_Value ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
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
    
    /** Set Cash Book.
    @param C_CashBook_ID Cash Book for recording petty cash transactions */
    public void setC_CashBook_ID (int C_CashBook_ID)
    {
        if (C_CashBook_ID <= 0) set_Value ("C_CashBook_ID", null);
        else
        set_Value ("C_CashBook_ID", Integer.valueOf(C_CashBook_ID));
        
    }
    
    /** Get Cash Book.
    @return Cash Book for recording petty cash transactions */
    public int getC_CashBook_ID() 
    {
        return get_ValueAsInt("C_CashBook_ID");
        
    }
    
    /** Set Cash Journal Line.
    @param C_CashLine_ID Cash Journal Line */
    public void setC_CashLine_ID (int C_CashLine_ID)
    {
        if (C_CashLine_ID <= 0) set_Value ("C_CashLine_ID", null);
        else
        set_Value ("C_CashLine_ID", Integer.valueOf(C_CashLine_ID));
        
    }
    
    /** Get Cash Journal Line.
    @return Cash Journal Line */
    public int getC_CashLine_ID() 
    {
        return get_ValueAsInt("C_CashLine_ID");
        
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
    
    /** Set Target Doc Type.
    @param C_DocTypeTarget_ID Target document type for documents */
    public void setC_DocTypeTarget_ID (int C_DocTypeTarget_ID)
    {
        if (C_DocTypeTarget_ID < 1) throw new IllegalArgumentException ("C_DocTypeTarget_ID is mandatory.");
        set_Value ("C_DocTypeTarget_ID", Integer.valueOf(C_DocTypeTarget_ID));
        
    }
    
    /** Get Target Doc Type.
    @return Target document type for documents */
    public int getC_DocTypeTarget_ID() 
    {
        return get_ValueAsInt("C_DocTypeTarget_ID");
        
    }
    
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID < 0) throw new IllegalArgumentException ("C_DocType_ID is mandatory.");
        set_ValueNoCheck ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
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
        if (C_Invoice_ID < 1) throw new IllegalArgumentException ("C_Invoice_ID is mandatory.");
        set_ValueNoCheck ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
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
    
    /** Set Payment Term.
    @param C_PaymentTerm_ID The terms of Payment (timing, discount) */
    public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
    {
        if (C_PaymentTerm_ID < 1) throw new IllegalArgumentException ("C_PaymentTerm_ID is mandatory.");
        set_Value ("C_PaymentTerm_ID", Integer.valueOf(C_PaymentTerm_ID));
        
    }
    
    /** Get Payment Term.
    @return The terms of Payment (timing, discount) */
    public int getC_PaymentTerm_ID() 
    {
        return get_ValueAsInt("C_PaymentTerm_ID");
        
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
    
    /** Set Copy From.
    @param CopyFrom Copy From Record */
    public void setCopyFrom (String CopyFrom)
    {
        set_Value ("CopyFrom", CopyFrom);
        
    }
    
    /** Get Copy From.
    @return Copy From Record */
    public String getCopyFrom() 
    {
        return (String)get_Value("CopyFrom");
        
    }
    
    /** Set Create lines from.
    @param CreateFrom Process which will generate a new document lines based on an existing document */
    public void setCreateFrom (String CreateFrom)
    {
        set_Value ("CreateFrom", CreateFrom);
        
    }
    
    /** Get Create lines from.
    @return Process which will generate a new document lines based on an existing document */
    public String getCreateFrom() 
    {
        return (String)get_Value("CreateFrom");
        
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
    
    /** Set Date Invoiced.
    @param DateInvoiced Date printed on Invoice */
    public void setDateInvoiced (Timestamp DateInvoiced)
    {
        if (DateInvoiced == null) throw new IllegalArgumentException ("DateInvoiced is mandatory.");
        set_Value ("DateInvoiced", DateInvoiced);
        
    }
    
    /** Get Date Invoiced.
    @return Date printed on Invoice */
    public Timestamp getDateInvoiced() 
    {
        return (Timestamp)get_Value("DateInvoiced");
        
    }
    
    /** Set Date Ordered.
    @param DateOrdered Date of Order */
    public void setDateOrdered (Timestamp DateOrdered)
    {
        set_ValueNoCheck ("DateOrdered", DateOrdered);
        
    }
    
    /** Get Date Ordered.
    @return Date of Order */
    public Timestamp getDateOrdered() 
    {
        return (Timestamp)get_Value("DateOrdered");
        
    }
    
    /** Set Date printed.
    @param DatePrinted Date the document was printed. */
    public void setDatePrinted (Timestamp DatePrinted)
    {
        set_Value ("DatePrinted", DatePrinted);
        
    }
    
    /** Get Date printed.
    @return Date the document was printed. */
    public Timestamp getDatePrinted() 
    {
        return (Timestamp)get_Value("DatePrinted");
        
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
        set_ValueNoCheck ("DocumentNo", DocumentNo);
        
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
    
    /** Set Generate To.
    @param GenerateTo Generate To */
    public void setGenerateTo (String GenerateTo)
    {
        set_Value ("GenerateTo", GenerateTo);
        
    }
    
    /** Get Generate To.
    @return Generate To */
    public String getGenerateTo() 
    {
        return (String)get_Value("GenerateTo");
        
    }
    
    /** Set Grand Total.
    @param GrandTotal Total amount of document */
    public void setGrandTotal (java.math.BigDecimal GrandTotal)
    {
        if (GrandTotal == null) throw new IllegalArgumentException ("GrandTotal is mandatory.");
        set_ValueNoCheck ("GrandTotal", GrandTotal);
        
    }
    
    /** Get Grand Total.
    @return Total amount of document */
    public java.math.BigDecimal getGrandTotal() 
    {
        return get_ValueAsBigDecimal("GrandTotal");
        
    }
    
    /** Collection Agency = C */
    public static final String INVOICECOLLECTIONTYPE_CollectionAgency = X_Ref_C_Invoice_InvoiceCollectionType.COLLECTION_AGENCY.getValue();
    /** Dunning = D */
    public static final String INVOICECOLLECTIONTYPE_Dunning = X_Ref_C_Invoice_InvoiceCollectionType.DUNNING.getValue();
    /** Legal Procedure = L */
    public static final String INVOICECOLLECTIONTYPE_LegalProcedure = X_Ref_C_Invoice_InvoiceCollectionType.LEGAL_PROCEDURE.getValue();
    /** Uncollectable = U */
    public static final String INVOICECOLLECTIONTYPE_Uncollectable = X_Ref_C_Invoice_InvoiceCollectionType.UNCOLLECTABLE.getValue();
    /** Set Collection Status.
    @param InvoiceCollectionType Invoice Collection Status */
    public void setInvoiceCollectionType (String InvoiceCollectionType)
    {
        if (!X_Ref_C_Invoice_InvoiceCollectionType.isValid(InvoiceCollectionType))
        throw new IllegalArgumentException ("InvoiceCollectionType Invalid value - " + InvoiceCollectionType + " - Reference_ID=394 - C - D - L - U");
        set_Value ("InvoiceCollectionType", InvoiceCollectionType);
        
    }
    
    /** Get Collection Status.
    @return Invoice Collection Status */
    public String getInvoiceCollectionType() 
    {
        return (String)get_Value("InvoiceCollectionType");
        
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
    
    /** Set Discount Printed.
    @param IsDiscountPrinted Print Discount on Invoice and Order */
    public void setIsDiscountPrinted (boolean IsDiscountPrinted)
    {
        set_Value ("IsDiscountPrinted", Boolean.valueOf(IsDiscountPrinted));
        
    }
    
    /** Get Discount Printed.
    @return Print Discount on Invoice and Order */
    public boolean isDiscountPrinted() 
    {
        return get_ValueAsBoolean("IsDiscountPrinted");
        
    }
    
    /** Set In Dispute.
    @param IsInDispute Document is in dispute */
    public void setIsInDispute (boolean IsInDispute)
    {
        set_Value ("IsInDispute", Boolean.valueOf(IsInDispute));
        
    }
    
    /** Get In Dispute.
    @return Document is in dispute */
    public boolean isInDispute() 
    {
        return get_ValueAsBoolean("IsInDispute");
        
    }
    
    /** Set Paid.
    @param IsPaid The document is paid */
    public void setIsPaid (boolean IsPaid)
    {
        set_Value ("IsPaid", Boolean.valueOf(IsPaid));
        
    }
    
    /** Get Paid.
    @return The document is paid */
    public boolean isPaid() 
    {
        return get_ValueAsBoolean("IsPaid");
        
    }
    
    /** Set Pay Schedule valid.
    @param IsPayScheduleValid Is the Payment Schedule is valid */
    public void setIsPayScheduleValid (boolean IsPayScheduleValid)
    {
        set_ValueNoCheck ("IsPayScheduleValid", Boolean.valueOf(IsPayScheduleValid));
        
    }
    
    /** Get Pay Schedule valid.
    @return Is the Payment Schedule is valid */
    public boolean isPayScheduleValid() 
    {
        return get_ValueAsBoolean("IsPayScheduleValid");
        
    }
    
    /** Set Printed.
    @param IsPrinted Indicates if this document / line is printed */
    public void setIsPrinted (boolean IsPrinted)
    {
        set_ValueNoCheck ("IsPrinted", Boolean.valueOf(IsPrinted));
        
    }
    
    /** Get Printed.
    @return Indicates if this document / line is printed */
    public boolean isPrinted() 
    {
        return get_ValueAsBoolean("IsPrinted");
        
    }
    
    /** Set Return Transaction.
    @param IsReturnTrx This is a return transaction */
    public void setIsReturnTrx (boolean IsReturnTrx)
    {
        set_Value ("IsReturnTrx", Boolean.valueOf(IsReturnTrx));
        
    }
    
    /** Get Return Transaction.
    @return This is a return transaction */
    public boolean isReturnTrx() 
    {
        return get_ValueAsBoolean("IsReturnTrx");
        
    }
    
    /** Set Sales Transaction.
    @param IsSOTrx This is a Sales Transaction */
    public void setIsSOTrx (boolean IsSOTrx)
    {
        set_ValueNoCheck ("IsSOTrx", Boolean.valueOf(IsSOTrx));
        
    }
    
    /** Get Sales Transaction.
    @return This is a Sales Transaction */
    public boolean isSOTrx() 
    {
        return get_ValueAsBoolean("IsSOTrx");
        
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
    
    /** Set Price includes Tax.
    @param IsTaxIncluded Tax is included in the price */
    public void setIsTaxIncluded (boolean IsTaxIncluded)
    {
        set_Value ("IsTaxIncluded", Boolean.valueOf(IsTaxIncluded));
        
    }
    
    /** Get Price includes Tax.
    @return Tax is included in the price */
    public boolean isTaxIncluded() 
    {
        return get_ValueAsBoolean("IsTaxIncluded");
        
    }
    
    /** Set Transferred.
    @param IsTransferred Transferred to General Ledger (i.e. accounted) */
    public void setIsTransferred (boolean IsTransferred)
    {
        set_ValueNoCheck ("IsTransferred", Boolean.valueOf(IsTransferred));
        
    }
    
    /** Get Transferred.
    @return Transferred to General Ledger (i.e. accounted) */
    public boolean isTransferred() 
    {
        return get_ValueAsBoolean("IsTransferred");
        
    }
    
    /** Set Price List.
    @param M_PriceList_ID Unique identifier of a Price List */
    public void setM_PriceList_ID (int M_PriceList_ID)
    {
        if (M_PriceList_ID < 1) throw new IllegalArgumentException ("M_PriceList_ID is mandatory.");
        set_Value ("M_PriceList_ID", Integer.valueOf(M_PriceList_ID));
        
    }
    
    /** Get Price List.
    @return Unique identifier of a Price List */
    public int getM_PriceList_ID() 
    {
        return get_ValueAsInt("M_PriceList_ID");
        
    }
    
    /** Purchase Order and Receipt = B */
    public static final String MATCHREQUIREMENTI_PurchaseOrderAndReceipt = X_Ref_C_Client_Invoice_MatchRequirement.PURCHASE_ORDER_AND_RECEIPT.getValue();
    /** None = N */
    public static final String MATCHREQUIREMENTI_None = X_Ref_C_Client_Invoice_MatchRequirement.NONE.getValue();
    /** Purchase Order = P */
    public static final String MATCHREQUIREMENTI_PurchaseOrder = X_Ref_C_Client_Invoice_MatchRequirement.PURCHASE_ORDER.getValue();
    /** Receipt = R */
    public static final String MATCHREQUIREMENTI_Receipt = X_Ref_C_Client_Invoice_MatchRequirement.RECEIPT.getValue();
    /** Set Invoice Match Requirement.
    @param MatchRequirementI Matching Requirement for Invoice */
    public void setMatchRequirementI (String MatchRequirementI)
    {
        if (!X_Ref_C_Client_Invoice_MatchRequirement.isValid(MatchRequirementI))
        throw new IllegalArgumentException ("MatchRequirementI Invalid value - " + MatchRequirementI + " - Reference_ID=360 - B - N - P - R");
        set_Value ("MatchRequirementI", MatchRequirementI);
        
    }
    
    /** Get Invoice Match Requirement.
    @return Matching Requirement for Invoice */
    public String getMatchRequirementI() 
    {
        return (String)get_Value("MatchRequirementI");
        
    }
    
    /** Set Order Reference.
    @param POReference Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner */
    public void setPOReference (String POReference)
    {
        set_Value ("POReference", POReference);
        
    }
    
    /** Get Order Reference.
    @return Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner */
    public String getPOReference() 
    {
        return (String)get_Value("POReference");
        
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
        //if (!isPaymentRuleValid(PaymentRule))
        //throw new IllegalArgumentException ("PaymentRule Invalid value - " + PaymentRule + " - Reference_ID=195 - B - D - K - P - S - T");
        set_Value ("PaymentRule", PaymentRule);
        
    }
    
    /** Get Payment Method.
    @return How you pay the invoice */
    public String getPaymentRule() 
    {
        return (String)get_Value("PaymentRule");
        
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
    
    /** Set Referenced Invoice.
    @param Ref_Invoice_ID Referenced Invoice */
    public void setRef_Invoice_ID (int Ref_Invoice_ID)
    {
        if (Ref_Invoice_ID <= 0) set_Value ("Ref_Invoice_ID", null);
        else
        set_Value ("Ref_Invoice_ID", Integer.valueOf(Ref_Invoice_ID));
        
    }
    
    /** Get Referenced Invoice.
    @return Referenced Invoice */
    public int getRef_Invoice_ID() 
    {
        return get_ValueAsInt("Ref_Invoice_ID");
        
    }
    
    /** Set Representative.
    @param SalesRep_ID Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public void setSalesRep_ID (int SalesRep_ID)
    {
        if (SalesRep_ID <= 0) set_Value ("SalesRep_ID", null);
        else
        set_Value ("SalesRep_ID", Integer.valueOf(SalesRep_ID));
        
    }
    
    /** Get Representative.
    @return Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public int getSalesRep_ID() 
    {
        return get_ValueAsInt("SalesRep_ID");
        
    }
    
    /** Set Send EMail.
    @param SendEMail Enable sending Document EMail */
    public void setSendEMail (boolean SendEMail)
    {
        set_Value ("SendEMail", Boolean.valueOf(SendEMail));
        
    }
    
    /** Get Send EMail.
    @return Enable sending Document EMail */
    public boolean isSendEMail() 
    {
        return get_ValueAsBoolean("SendEMail");
        
    }
    
    /** Set SubTotal.
    @param TotalLines Total of all document lines (excluding Tax) */
    public void setTotalLines (java.math.BigDecimal TotalLines)
    {
        if (TotalLines == null) throw new IllegalArgumentException ("TotalLines is mandatory.");
        set_ValueNoCheck ("TotalLines", TotalLines);
        
    }
    
    /** Get SubTotal.
    @return Total of all document lines (excluding Tax) */
    public java.math.BigDecimal getTotalLines() 
    {
        return get_ValueAsBigDecimal("TotalLines");
        
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
    
    
}
