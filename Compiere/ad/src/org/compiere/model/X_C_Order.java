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
/** Generated Model for C_Order
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_C_Order.java 9090 2010-07-01 16:30:39Z ragrawal $ */
public class X_C_Order extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Order_ID id
    @param trx transaction
    */
    public X_C_Order (Ctx ctx, int C_Order_ID, Trx trx)
    {
        super (ctx, C_Order_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Order_ID == 0)
        {
            setBill_BPartner_ID (0);
            setBill_Location_ID (0);
            setC_BPartner_ID (0);
            setC_BPartner_Location_ID (0);
            setC_Currency_ID (0);	// @C_Currency_ID@
            setC_DocTypeTarget_ID (0);
            setC_DocType_ID (0);	// 0
            setC_Order_ID (0);
            setC_PaymentTerm_ID (0);
            setDateAcct (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDateOrdered (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDeliveryRule (null);	// F
            setDeliveryViaRule (null);	// P
            setDocAction (null);	// CO
            setDocStatus (null);	// DR
            setDocumentNo (null);
            setFreightCostRule (null);	// I
            setGrandTotal (Env.ZERO);
            setInvoiceRule (null);	// I
            setInvoicing (false);	// N
            setIsApproved (false);
            setIsCreditApproved (false);
            setIsDelivered (false);
            setIsDiscountPrinted (false);
            setIsDropShip (false);	// N
            setIsInvoiced (false);
            setIsPrinted (false);
            setIsReturnTrx (false);	// N
            setIsSOTrx (false);	// @IsSOTrx@
            setIsSelected (false);
            setIsSelfService (false);
            setIsTaxIncluded (false);
            setIsTransferred (false);
            setM_PriceList_ID (0);
            setM_Warehouse_ID (0);
            setPaymentRule (null);	// B
            setPosted (false);	// N
            setPriorityRule (null);	// 5
            setProcessed (false);	// N
            setSalesRep_ID (0);
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
    public X_C_Order (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27560080435789L;
    /** Last Updated Timestamp 2010-07-01 09:01:59.0 */
    public static final long updatedMS = 1277955119000L;
    /** AD_Table_ID=259 */
    public static final int Table_ID=259;
    
    /** TableName=C_Order */
    public static final String Table_Name="C_Order";
    
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
    
    /** Set Bill To.
    @param Bill_BPartner_ID Business Partner to be invoiced */
    public void setBill_BPartner_ID (int Bill_BPartner_ID)
    {
        if (Bill_BPartner_ID < 1) throw new IllegalArgumentException ("Bill_BPartner_ID is mandatory.");
        set_Value ("Bill_BPartner_ID", Integer.valueOf(Bill_BPartner_ID));
        
    }
    
    /** Get Bill To.
    @return Business Partner to be invoiced */
    public int getBill_BPartner_ID() 
    {
        return get_ValueAsInt("Bill_BPartner_ID");
        
    }
    
    /** Set Bill To Location.
    @param Bill_Location_ID Business Partner Location for invoicing */
    public void setBill_Location_ID (int Bill_Location_ID)
    {
        if (Bill_Location_ID < 1) throw new IllegalArgumentException ("Bill_Location_ID is mandatory.");
        set_Value ("Bill_Location_ID", Integer.valueOf(Bill_Location_ID));
        
    }
    
    /** Get Bill To Location.
    @return Business Partner Location for invoicing */
    public int getBill_Location_ID() 
    {
        return get_ValueAsInt("Bill_Location_ID");
        
    }
    
    /** Set Invoice Contact.
    @param Bill_User_ID Business Partner Contact for invoicing */
    public void setBill_User_ID (int Bill_User_ID)
    {
        if (Bill_User_ID <= 0) set_Value ("Bill_User_ID", null);
        else
        set_Value ("Bill_User_ID", Integer.valueOf(Bill_User_ID));
        
    }
    
    /** Get Invoice Contact.
    @return Business Partner Contact for invoicing */
    public int getBill_User_ID() 
    {
        return get_ValueAsInt("Bill_User_ID");
        
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
        set_ValueNoCheck ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
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
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID < 1) throw new IllegalArgumentException ("C_Order_ID is mandatory.");
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
    
    /** Set Date Ordered.
    @param DateOrdered Date of Order */
    public void setDateOrdered (Timestamp DateOrdered)
    {
        if (DateOrdered == null) throw new IllegalArgumentException ("DateOrdered is mandatory.");
        set_Value ("DateOrdered", DateOrdered);
        
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
    
    /** Set Date Promised.
    @param DatePromised Date Order was promised */
    public void setDatePromised (Timestamp DatePromised)
    {
        set_Value ("DatePromised", DatePromised);
        
    }
    
    /** Get Date Promised.
    @return Date Order was promised */
    public Timestamp getDatePromised() 
    {
        return (Timestamp)get_Value("DatePromised");
        
    }
    
    /** Availability = A */
    public static final String DELIVERYRULE_Availability = X_Ref_C_Order_DeliveryRule.AVAILABILITY.getValue();
    /** Force = F */
    public static final String DELIVERYRULE_Force = X_Ref_C_Order_DeliveryRule.FORCE.getValue();
    /** Complete Line = L */
    public static final String DELIVERYRULE_CompleteLine = X_Ref_C_Order_DeliveryRule.COMPLETE_LINE.getValue();
    /** Manual = M */
    public static final String DELIVERYRULE_Manual = X_Ref_C_Order_DeliveryRule.MANUAL.getValue();
    /** Complete Order = O */
    public static final String DELIVERYRULE_CompleteOrder = X_Ref_C_Order_DeliveryRule.COMPLETE_ORDER.getValue();
    /** After Receipt = R */
    public static final String DELIVERYRULE_AfterReceipt = X_Ref_C_Order_DeliveryRule.AFTER_RECEIPT.getValue();
    /** Set Shipping Rule.
    @param DeliveryRule Defines the timing of Shipping */
    public void setDeliveryRule (String DeliveryRule)
    {
        if (DeliveryRule == null) throw new IllegalArgumentException ("DeliveryRule is mandatory");
        if (!X_Ref_C_Order_DeliveryRule.isValid(DeliveryRule))
        throw new IllegalArgumentException ("DeliveryRule Invalid value - " + DeliveryRule + " - Reference_ID=151 - A - F - L - M - O - R");
        set_Value ("DeliveryRule", DeliveryRule);
        
    }
    
    /** Get Shipping Rule.
    @return Defines the timing of Shipping */
    public String getDeliveryRule() 
    {
        return (String)get_Value("DeliveryRule");
        
    }
    
    /** Delivery = D */
    public static final String DELIVERYVIARULE_Delivery = X_Ref_C_Order_DeliveryViaRule.DELIVERY.getValue();
    /** Pickup = P */
    public static final String DELIVERYVIARULE_Pickup = X_Ref_C_Order_DeliveryViaRule.PICKUP.getValue();
    /** Shipper = S */
    public static final String DELIVERYVIARULE_Shipper = X_Ref_C_Order_DeliveryViaRule.SHIPPER.getValue();
    /** Set Shipping Method.
    @param DeliveryViaRule How the order will be delivered */
    public void setDeliveryViaRule (String DeliveryViaRule)
    {
        if (DeliveryViaRule == null) throw new IllegalArgumentException ("DeliveryViaRule is mandatory");
        if (!X_Ref_C_Order_DeliveryViaRule.isValid(DeliveryViaRule))
        throw new IllegalArgumentException ("DeliveryViaRule Invalid value - " + DeliveryViaRule + " - Reference_ID=152 - D - P - S");
        set_Value ("DeliveryViaRule", DeliveryViaRule);
        
    }
    
    /** Get Shipping Method.
    @return How the order will be delivered */
    public String getDeliveryViaRule() 
    {
        return (String)get_Value("DeliveryViaRule");
        
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
    
    /** Set Freight Amount.
    @param FreightAmt Freight Amount */
    public void setFreightAmt (java.math.BigDecimal FreightAmt)
    {
        set_Value ("FreightAmt", FreightAmt);
        
    }
    
    /** Get Freight Amount.
    @return Freight Amount */
    public java.math.BigDecimal getFreightAmt() 
    {
        return get_ValueAsBigDecimal("FreightAmt");
        
    }
    
    /** Calculated = C */
    public static final String FREIGHTCOSTRULE_Calculated = X_Ref_C_Order_FreightCostRule.CALCULATED.getValue();
    /** Fix price = F */
    public static final String FREIGHTCOSTRULE_FixPrice = X_Ref_C_Order_FreightCostRule.FIX_PRICE.getValue();
    /** Freight included = I */
    public static final String FREIGHTCOSTRULE_FreightIncluded = X_Ref_C_Order_FreightCostRule.FREIGHT_INCLUDED.getValue();
    /** Line = L */
    public static final String FREIGHTCOSTRULE_Line = X_Ref_C_Order_FreightCostRule.LINE.getValue();
    /** Set Freight Cost Rule.
    @param FreightCostRule Method for charging Freight */
    public void setFreightCostRule (String FreightCostRule)
    {
        if (FreightCostRule == null) throw new IllegalArgumentException ("FreightCostRule is mandatory");
        if (!X_Ref_C_Order_FreightCostRule.isValid(FreightCostRule))
        throw new IllegalArgumentException ("FreightCostRule Invalid value - " + FreightCostRule + " - Reference_ID=153 - C - F - I - L");
        set_Value ("FreightCostRule", FreightCostRule);
        
    }
    
    /** Get Freight Cost Rule.
    @return Method for charging Freight */
    public String getFreightCostRule() 
    {
        return (String)get_Value("FreightCostRule");
        
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
    
    /** After Delivery = D */
    public static final String INVOICERULE_AfterDelivery = X_Ref_C_Order_InvoiceRule.AFTER_DELIVERY.getValue();
    /** Immediate = I */
    public static final String INVOICERULE_Immediate = X_Ref_C_Order_InvoiceRule.IMMEDIATE.getValue();
    /** After Order delivered = O */
    public static final String INVOICERULE_AfterOrderDelivered = X_Ref_C_Order_InvoiceRule.AFTER_ORDER_DELIVERED.getValue();
    /** Customer Schedule after Delivery = S */
    public static final String INVOICERULE_CustomerScheduleAfterDelivery = X_Ref_C_Order_InvoiceRule.CUSTOMER_SCHEDULE_AFTER_DELIVERY.getValue();
    /** Set Invoicing Rule.
    @param InvoiceRule Frequency and method of invoicing */
    public void setInvoiceRule (String InvoiceRule)
    {
        if (InvoiceRule == null) throw new IllegalArgumentException ("InvoiceRule is mandatory");
        if (!X_Ref_C_Order_InvoiceRule.isValid(InvoiceRule))
        throw new IllegalArgumentException ("InvoiceRule Invalid value - " + InvoiceRule + " - Reference_ID=150 - D - I - O - S");
        set_Value ("InvoiceRule", InvoiceRule);
        
    }
    
    /** Get Invoicing Rule.
    @return Frequency and method of invoicing */
    public String getInvoiceRule() 
    {
        return (String)get_Value("InvoiceRule");
        
    }
    
    /** Set Invoicing.
    @param Invoicing Invoicing */
    public void setInvoicing (boolean Invoicing)
    {
        set_Value ("Invoicing", Boolean.valueOf(Invoicing));
        
    }
    
    /** Get Invoicing.
    @return Invoicing */
    public boolean isInvoicing() 
    {
        return get_ValueAsBoolean("Invoicing");
        
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
    
    /** Set Credit Approved.
    @param IsCreditApproved Credit has been approved */
    public void setIsCreditApproved (boolean IsCreditApproved)
    {
        set_ValueNoCheck ("IsCreditApproved", Boolean.valueOf(IsCreditApproved));
        
    }
    
    /** Get Credit Approved.
    @return Credit has been approved */
    public boolean isCreditApproved() 
    {
        return get_ValueAsBoolean("IsCreditApproved");
        
    }
    
    /** Set Delivered.
    @param IsDelivered Delivered */
    public void setIsDelivered (boolean IsDelivered)
    {
        set_ValueNoCheck ("IsDelivered", Boolean.valueOf(IsDelivered));
        
    }
    
    /** Get Delivered.
    @return Delivered */
    public boolean isDelivered() 
    {
        return get_ValueAsBoolean("IsDelivered");
        
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
    
    /** Set Drop Shipment.
    @param IsDropShip Drop Shipments are sent from the Vendor directly to the Customer */
    public void setIsDropShip (boolean IsDropShip)
    {
        set_ValueNoCheck ("IsDropShip", Boolean.valueOf(IsDropShip));
        
    }
    
    /** Get Drop Shipment.
    @return Drop Shipments are sent from the Vendor directly to the Customer */
    public boolean isDropShip() 
    {
        return get_ValueAsBoolean("IsDropShip");
        
    }
    
    /** Set Invoiced.
    @param IsInvoiced Is this invoiced? */
    public void setIsInvoiced (boolean IsInvoiced)
    {
        set_ValueNoCheck ("IsInvoiced", Boolean.valueOf(IsInvoiced));
        
    }
    
    /** Get Invoiced.
    @return Is this invoiced? */
    public boolean isInvoiced() 
    {
        return get_ValueAsBoolean("IsInvoiced");
        
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
        set_Value ("IsSOTrx", Boolean.valueOf(IsSOTrx));
        
    }
    
    /** Get Sales Transaction.
    @return This is a Sales Transaction */
    public boolean isSOTrx() 
    {
        return get_ValueAsBoolean("IsSOTrx");
        
    }
    
    /** Set Selected.
    @param IsSelected Selected */
    public void setIsSelected (boolean IsSelected)
    {
        set_Value ("IsSelected", Boolean.valueOf(IsSelected));
        
    }
    
    /** Get Selected.
    @return Selected */
    public boolean isSelected() 
    {
        return get_ValueAsBoolean("IsSelected");
        
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
    
    /** Set RMA Category.
    @param M_RMACategory_ID Return Material Authorization Category */
    public void setM_RMACategory_ID (int M_RMACategory_ID)
    {
        if (M_RMACategory_ID <= 0) set_Value ("M_RMACategory_ID", null);
        else
        set_Value ("M_RMACategory_ID", Integer.valueOf(M_RMACategory_ID));
        
    }
    
    /** Get RMA Category.
    @return Return Material Authorization Category */
    public int getM_RMACategory_ID() 
    {
        return get_ValueAsInt("M_RMACategory_ID");
        
    }
    
    /** Set Return Policy.
    @param M_ReturnPolicy_ID The Return Policy dictates the timeframe within which goods can be returned. */
    public void setM_ReturnPolicy_ID (int M_ReturnPolicy_ID)
    {
        if (M_ReturnPolicy_ID <= 0) set_Value ("M_ReturnPolicy_ID", null);
        else
        set_Value ("M_ReturnPolicy_ID", Integer.valueOf(M_ReturnPolicy_ID));
        
    }
    
    /** Get Return Policy.
    @return The Return Policy dictates the timeframe within which goods can be returned. */
    public int getM_ReturnPolicy_ID() 
    {
        return get_ValueAsInt("M_ReturnPolicy_ID");
        
    }
    
    /** Set Freight Carrier.
    @param M_Shipper_ID Method or manner of product delivery */
    public void setM_Shipper_ID (int M_Shipper_ID)
    {
        if (M_Shipper_ID <= 0) set_Value ("M_Shipper_ID", null);
        else
        set_Value ("M_Shipper_ID", Integer.valueOf(M_Shipper_ID));
        
    }
    
    /** Get Freight Carrier.
    @return Method or manner of product delivery */
    public int getM_Shipper_ID() 
    {
        return get_ValueAsInt("M_Shipper_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Quotation = OB */
    public static final String ORDERTYPE_Quotation = X_Ref_C_DocType_SubTypeSO.QUOTATION.getValue();
    /** Proposal = ON */
    public static final String ORDERTYPE_Proposal = X_Ref_C_DocType_SubTypeSO.PROPOSAL.getValue();
    /** Prepay Order = PR */
    public static final String ORDERTYPE_PrepayOrder = X_Ref_C_DocType_SubTypeSO.PREPAY_ORDER.getValue();
    /** Standard Order = SO */
    public static final String ORDERTYPE_StandardOrder = X_Ref_C_DocType_SubTypeSO.STANDARD_ORDER.getValue();
    /** On Credit Order = WI */
    public static final String ORDERTYPE_OnCreditOrder = X_Ref_C_DocType_SubTypeSO.ON_CREDIT_ORDER.getValue();
    /** Warehouse Order = WP */
    public static final String ORDERTYPE_WarehouseOrder = X_Ref_C_DocType_SubTypeSO.WAREHOUSE_ORDER.getValue();
    /** POS Order = WR */
    public static final String ORDERTYPE_POSOrder = X_Ref_C_DocType_SubTypeSO.POS_ORDER.getValue();
    /** Set Order Type.
    @param OrderType Document Base Type for Sales Orders */
    public void setOrderType (String OrderType)
    {
        if (!X_Ref_C_DocType_SubTypeSO.isValid(OrderType))
        throw new IllegalArgumentException ("OrderType Invalid value - " + OrderType + " - Reference_ID=148 - OB - ON - PR - SO - WI - WP - WR");
        throw new IllegalArgumentException ("OrderType is virtual column");
        
    }
    
    /** Get Order Type.
    @return Document Base Type for Sales Orders */
    public String getOrderType() 
    {
        return (String)get_Value("OrderType");
        
    }
    
    /** Set Orig Shipment.
    @param Orig_InOut_ID Original shipment of the RMA */
    public void setOrig_InOut_ID (int Orig_InOut_ID)
    {
        if (Orig_InOut_ID <= 0) set_Value ("Orig_InOut_ID", null);
        else
        set_Value ("Orig_InOut_ID", Integer.valueOf(Orig_InOut_ID));
        
    }
    
    /** Get Orig Shipment.
    @return Original shipment of the RMA */
    public int getOrig_InOut_ID() 
    {
        return get_ValueAsInt("Orig_InOut_ID");
        
    }
    
    /** Set Orig Sales Order.
    @param Orig_Order_ID Original Sales Order for Return Material Authorization */
    public void setOrig_Order_ID (int Orig_Order_ID)
    {
        if (Orig_Order_ID <= 0) set_Value ("Orig_Order_ID", null);
        else
        set_Value ("Orig_Order_ID", Integer.valueOf(Orig_Order_ID));
        
    }
    
    /** Get Orig Sales Order.
    @return Original Sales Order for Return Material Authorization */
    public int getOrig_Order_ID() 
    {
        return get_ValueAsInt("Orig_Order_ID");
        
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
    
    /** Set Payment BPartner.
    @param Pay_BPartner_ID Business Partner responsible for the payment */
    public void setPay_BPartner_ID (int Pay_BPartner_ID)
    {
        if (Pay_BPartner_ID <= 0) set_Value ("Pay_BPartner_ID", null);
        else
        set_Value ("Pay_BPartner_ID", Integer.valueOf(Pay_BPartner_ID));
        
    }
    
    /** Get Payment BPartner.
    @return Business Partner responsible for the payment */
    public int getPay_BPartner_ID() 
    {
        return get_ValueAsInt("Pay_BPartner_ID");
        
    }
    
    /** Set Payment Location.
    @param Pay_Location_ID Location of the Business Partner responsible for the payment */
    public void setPay_Location_ID (int Pay_Location_ID)
    {
        if (Pay_Location_ID <= 0) set_Value ("Pay_Location_ID", null);
        else
        set_Value ("Pay_Location_ID", Integer.valueOf(Pay_Location_ID));
        
    }
    
    /** Get Payment Location.
    @return Location of the Business Partner responsible for the payment */
    public int getPay_Location_ID() 
    {
        return get_ValueAsInt("Pay_Location_ID");
        
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
        
        if(isSOTrx()){
	        if (!X_Ref__Payment_Rule.isValid(PaymentRule))
	        throw new IllegalArgumentException ("PaymentRule Invalid value - " + PaymentRule + " - Reference_ID=195 - B - D - K - P - S - T");
	        set_Value ("PaymentRule", PaymentRule);
        }else{
        	set_Value ("PaymentRule", PaymentRule);	
        }
       
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
    
    /** Urgent = 1 */
    public static final String PRIORITYRULE_Urgent = X_Ref__PriorityRule.URGENT.getValue();
    /** High = 3 */
    public static final String PRIORITYRULE_High = X_Ref__PriorityRule.HIGH.getValue();
    /** Medium = 5 */
    public static final String PRIORITYRULE_Medium = X_Ref__PriorityRule.MEDIUM.getValue();
    /** Low = 7 */
    public static final String PRIORITYRULE_Low = X_Ref__PriorityRule.LOW.getValue();
    /** Minor = 9 */
    public static final String PRIORITYRULE_Minor = X_Ref__PriorityRule.MINOR.getValue();
    /** Set Priority.
    @param PriorityRule Priority of a document */
    public void setPriorityRule (String PriorityRule)
    {
        if (PriorityRule == null) throw new IllegalArgumentException ("PriorityRule is mandatory");
        if (!X_Ref__PriorityRule.isValid(PriorityRule))
        throw new IllegalArgumentException ("PriorityRule Invalid value - " + PriorityRule + " - Reference_ID=154 - 1 - 3 - 5 - 7 - 9");
        set_Value ("PriorityRule", PriorityRule);
        
    }
    
    /** Get Priority.
    @return Priority of a document */
    public String getPriorityRule() 
    {
        return (String)get_Value("PriorityRule");
        
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
    
    /** Set Referenced Order.
    @param Ref_Order_ID Reference to corresponding Sales/Purchase Order */
    public void setRef_Order_ID (int Ref_Order_ID)
    {
        if (Ref_Order_ID <= 0) set_Value ("Ref_Order_ID", null);
        else
        set_Value ("Ref_Order_ID", Integer.valueOf(Ref_Order_ID));
        
    }
    
    /** Get Referenced Order.
    @return Reference to corresponding Sales/Purchase Order */
    public int getRef_Order_ID() 
    {
        return get_ValueAsInt("Ref_Order_ID");
        
    }
    
    /** Set Representative.
    @param SalesRep_ID Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public void setSalesRep_ID (int SalesRep_ID)
    {
        if (SalesRep_ID < 1) throw new IllegalArgumentException ("SalesRep_ID is mandatory.");
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
    
    /** Set Ship Date.
    @param ShipDate Shipment Date/Time */
    public void setShipDate (Timestamp ShipDate)
    {
        throw new IllegalArgumentException ("ShipDate is virtual column");
        
    }
    
    /** Get Ship Date.
    @return Shipment Date/Time */
    public Timestamp getShipDate() 
    {
        return (Timestamp)get_Value("ShipDate");
        
    }
    
    /** Set Shipment Group.
    @param Shipment_Group_ID Shipment Group */
    public void setShipment_Group_ID (int Shipment_Group_ID)
    {
        if (Shipment_Group_ID <= 0) set_Value ("Shipment_Group_ID", null);
        else
        set_Value ("Shipment_Group_ID", Integer.valueOf(Shipment_Group_ID));
        
    }
    
    /** Get Shipment Group.
    @return Shipment Group */
    public int getShipment_Group_ID() 
    {
        return get_ValueAsInt("Shipment_Group_ID");
        
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
    
    /** Set Volume.
    @param Volume Volume of a product */
    public void setVolume (java.math.BigDecimal Volume)
    {
        set_Value ("Volume", Volume);
        
    }
    
    /** Get Volume.
    @return Volume of a product */
    public java.math.BigDecimal getVolume() 
    {
        return get_ValueAsBigDecimal("Volume");
        
    }
    
    /** Set Weight.
    @param Weight Weight of a product */
    public void setWeight (java.math.BigDecimal Weight)
    {
        set_Value ("Weight", Weight);
        
    }
    
    /** Get Weight.
    @return Weight of a product */
    public java.math.BigDecimal getWeight() 
    {
        return get_ValueAsBigDecimal("Weight");
        
    }
    
    
}
