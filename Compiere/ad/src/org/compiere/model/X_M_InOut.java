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
/** Generated Model for M_InOut
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_InOut.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_InOut extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_InOut_ID id
    @param trx transaction
    */
    public X_M_InOut (Ctx ctx, int M_InOut_ID, Trx trx)
    {
        super (ctx, M_InOut_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_InOut_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_BPartner_Location_ID (0);
            setC_DocType_ID (0);
            setDateAcct (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDeliveryRule (null);	// A
            setDeliveryViaRule (null);	// P
            setDocAction (null);	// CO
            setDocStatus (null);	// DR
            setDocumentNo (null);
            setFreightCostRule (null);	// I
            setIsApproved (false);
            setIsInDispute (false);
            setIsInTransit (false);
            setIsPrinted (false);
            setIsReturnTrx (false);	// N
            setIsSOTrx (false);	// @IsSOTrx@
            setM_InOut_ID (0);
            setM_Warehouse_ID (0);
            setMovementDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setMovementType (null);
            setPosted (false);
            setPriorityRule (null);	// 5
            setProcessed (false);	// N
            setSendEMail (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_InOut (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27497677394789L;
    /** Last Updated Timestamp 2008-07-08 14:21:18.0 */
    public static final long updatedMS = 1215552078000L;
    /** AD_Table_ID=319 */
    public static final int Table_ID=319;
    
    /** TableName=M_InOut */
    public static final String Table_Name="M_InOut";
    
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
    
    /** Set Create Confirm.
    @param CreateConfirm Create Confirm */
    public void setCreateConfirm (String CreateConfirm)
    {
        set_Value ("CreateConfirm", CreateConfirm);
        
    }
    
    /** Get Create Confirm.
    @return Create Confirm */
    public String getCreateConfirm() 
    {
        return (String)get_Value("CreateConfirm");
        
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
    
    /** Set Create Package.
    @param CreatePackage Create Package */
    public void setCreatePackage (String CreatePackage)
    {
        set_Value ("CreatePackage", CreatePackage);
        
    }
    
    /** Get Create Package.
    @return Create Package */
    public String getCreatePackage() 
    {
        return (String)get_Value("CreatePackage");
        
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
    
    /** Set Date received.
    @param DateReceived Date a product was received */
    public void setDateReceived (Timestamp DateReceived)
    {
        set_Value ("DateReceived", DateReceived);
        
    }
    
    /** Get Date received.
    @return Date a product was received */
    public Timestamp getDateReceived() 
    {
        return (Timestamp)get_Value("DateReceived");
        
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
    
    /** Set Approved.
    @param IsApproved Indicates if this document requires approval */
    public void setIsApproved (boolean IsApproved)
    {
        set_Value ("IsApproved", Boolean.valueOf(IsApproved));
        
    }
    
    /** Get Approved.
    @return Indicates if this document requires approval */
    public boolean isApproved() 
    {
        return get_ValueAsBoolean("IsApproved");
        
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
    
    /** Set In Transit.
    @param IsInTransit Movement is in transit */
    public void setIsInTransit (boolean IsInTransit)
    {
        set_Value ("IsInTransit", Boolean.valueOf(IsInTransit));
        
    }
    
    /** Get In Transit.
    @return Movement is in transit */
    public boolean isInTransit() 
    {
        return get_ValueAsBoolean("IsInTransit");
        
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
    
    /** Set Shipment/Receipt.
    @param M_InOut_ID Material Shipment Document */
    public void setM_InOut_ID (int M_InOut_ID)
    {
        if (M_InOut_ID < 1) throw new IllegalArgumentException ("M_InOut_ID is mandatory.");
        set_ValueNoCheck ("M_InOut_ID", Integer.valueOf(M_InOut_ID));
        
    }
    
    /** Get Shipment/Receipt.
    @return Material Shipment Document */
    public int getM_InOut_ID() 
    {
        return get_ValueAsInt("M_InOut_ID");
        
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
        set_ValueNoCheck ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Purchase Order and Invoice = B */
    public static final String MATCHREQUIREMENTR_PurchaseOrderAndInvoice = X_Ref_C_Client_Receipt_MatchRequirement.PURCHASE_ORDER_AND_INVOICE.getValue();
    /** Invoice = I */
    public static final String MATCHREQUIREMENTR_Invoice = X_Ref_C_Client_Receipt_MatchRequirement.INVOICE.getValue();
    /** None = N */
    public static final String MATCHREQUIREMENTR_None = X_Ref_C_Client_Receipt_MatchRequirement.NONE.getValue();
    /** Purchase Order = P */
    public static final String MATCHREQUIREMENTR_PurchaseOrder = X_Ref_C_Client_Receipt_MatchRequirement.PURCHASE_ORDER.getValue();
    /** Set Receipt Match Requirement.
    @param MatchRequirementR Matching Requirement for Receipts */
    public void setMatchRequirementR (String MatchRequirementR)
    {
        if (!X_Ref_C_Client_Receipt_MatchRequirement.isValid(MatchRequirementR))
        throw new IllegalArgumentException ("MatchRequirementR Invalid value - " + MatchRequirementR + " - Reference_ID=410 - B - I - N - P");
        set_Value ("MatchRequirementR", MatchRequirementR);
        
    }
    
    /** Get Receipt Match Requirement.
    @return Matching Requirement for Receipts */
    public String getMatchRequirementR() 
    {
        return (String)get_Value("MatchRequirementR");
        
    }
    
    /** Set Movement Date.
    @param MovementDate Date a product was moved in or out of inventory */
    public void setMovementDate (Timestamp MovementDate)
    {
        if (MovementDate == null) throw new IllegalArgumentException ("MovementDate is mandatory.");
        set_Value ("MovementDate", MovementDate);
        
    }
    
    /** Get Movement Date.
    @return Date a product was moved in or out of inventory */
    public Timestamp getMovementDate() 
    {
        return (Timestamp)get_Value("MovementDate");
        
    }
    
    /** Customer Returns = C+ */
    public static final String MOVEMENTTYPE_CustomerReturns = X_Ref_M_Transaction_Movement_Type.CUSTOMER_RETURNS.getValue();
    /** Customer Shipment = C- */
    public static final String MOVEMENTTYPE_CustomerShipment = X_Ref_M_Transaction_Movement_Type.CUSTOMER_SHIPMENT.getValue();
    /** Inventory In = I+ */
    public static final String MOVEMENTTYPE_InventoryIn = X_Ref_M_Transaction_Movement_Type.INVENTORY_IN.getValue();
    /** Inventory Out = I- */
    public static final String MOVEMENTTYPE_InventoryOut = X_Ref_M_Transaction_Movement_Type.INVENTORY_OUT.getValue();
    /** Movement To = M+ */
    public static final String MOVEMENTTYPE_MovementTo = X_Ref_M_Transaction_Movement_Type.MOVEMENT_TO.getValue();
    /** Movement From = M- */
    public static final String MOVEMENTTYPE_MovementFrom = X_Ref_M_Transaction_Movement_Type.MOVEMENT_FROM.getValue();
    /** Production + = P+ */
    public static final String MOVEMENTTYPE_ProductionPlus = X_Ref_M_Transaction_Movement_Type.PRODUCTION_PLUS.getValue();
    /** Production - = P- */
    public static final String MOVEMENTTYPE_Production_ = X_Ref_M_Transaction_Movement_Type.PRODUCTION_.getValue();
    /** Vendor Receipts = V+ */
    public static final String MOVEMENTTYPE_VendorReceipts = X_Ref_M_Transaction_Movement_Type.VENDOR_RECEIPTS.getValue();
    /** Vendor Returns = V- */
    public static final String MOVEMENTTYPE_VendorReturns = X_Ref_M_Transaction_Movement_Type.VENDOR_RETURNS.getValue();
    /** Work Order + = W+ */
    public static final String MOVEMENTTYPE_WorkOrderPlus = X_Ref_M_Transaction_Movement_Type.WORK_ORDER_PLUS.getValue();
    /** Work Order - = W- */
    public static final String MOVEMENTTYPE_WorkOrder_ = X_Ref_M_Transaction_Movement_Type.WORK_ORDER_.getValue();
    /** Set Movement Type.
    @param MovementType Method of moving the inventory */
    public void setMovementType (String MovementType)
    {
        if (MovementType == null) throw new IllegalArgumentException ("MovementType is mandatory");
        if (!X_Ref_M_Transaction_Movement_Type.isValid(MovementType))
        throw new IllegalArgumentException ("MovementType Invalid value - " + MovementType + " - Reference_ID=189 - C+ - C- - I+ - I- - M+ - M- - P+ - P- - V+ - V- - W+ - W-");
        set_ValueNoCheck ("MovementType", MovementType);
        
    }
    
    /** Get Movement Type.
    @return Method of moving the inventory */
    public String getMovementType() 
    {
        return (String)get_Value("MovementType");
        
    }
    
    /** Set No Packages.
    @param NoPackages Number of packages shipped */
    public void setNoPackages (int NoPackages)
    {
        set_Value ("NoPackages", Integer.valueOf(NoPackages));
        
    }
    
    /** Get No Packages.
    @return Number of packages shipped */
    public int getNoPackages() 
    {
        return get_ValueAsInt("NoPackages");
        
    }
    
    /** Set Orig Shipment.
    @param Orig_InOut_ID Original shipment of the RMA */
    public void setOrig_InOut_ID (int Orig_InOut_ID)
    {
        throw new IllegalArgumentException ("Orig_InOut_ID is virtual column");
        
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
        throw new IllegalArgumentException ("Orig_Order_ID is virtual column");
        
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
    
    /** Set Pick Date.
    @param PickDate Date/Time when picked for Shipment */
    public void setPickDate (Timestamp PickDate)
    {
        set_Value ("PickDate", PickDate);
        
    }
    
    /** Get Pick Date.
    @return Date/Time when picked for Shipment */
    public Timestamp getPickDate() 
    {
        return (Timestamp)get_Value("PickDate");
        
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
    
    /** Set Referenced Shipment.
    @param Ref_InOut_ID Referenced Shipment */
    public void setRef_InOut_ID (int Ref_InOut_ID)
    {
        if (Ref_InOut_ID <= 0) set_Value ("Ref_InOut_ID", null);
        else
        set_Value ("Ref_InOut_ID", Integer.valueOf(Ref_InOut_ID));
        
    }
    
    /** Get Referenced Shipment.
    @return Referenced Shipment */
    public int getRef_InOut_ID() 
    {
        return get_ValueAsInt("Ref_InOut_ID");
        
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
    
    /** Set Ship Date.
    @param ShipDate Shipment Date/Time */
    public void setShipDate (Timestamp ShipDate)
    {
        set_Value ("ShipDate", ShipDate);
        
    }
    
    /** Get Ship Date.
    @return Shipment Date/Time */
    public Timestamp getShipDate() 
    {
        return (Timestamp)get_Value("ShipDate");
        
    }
    
    /** Set Tracking No.
    @param TrackingNo Number to track the shipment */
    public void setTrackingNo (String TrackingNo)
    {
        set_Value ("TrackingNo", TrackingNo);
        
    }
    
    /** Get Tracking No.
    @return Number to track the shipment */
    public String getTrackingNo() 
    {
        return (String)get_Value("TrackingNo");
        
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
