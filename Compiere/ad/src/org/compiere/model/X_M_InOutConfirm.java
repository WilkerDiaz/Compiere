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
/** Generated Model for M_InOutConfirm
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_InOutConfirm.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_InOutConfirm extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_InOutConfirm_ID id
    @param trx transaction
    */
    public X_M_InOutConfirm (Ctx ctx, int M_InOutConfirm_ID, Trx trx)
    {
        super (ctx, M_InOutConfirm_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_InOutConfirm_ID == 0)
        {
            setConfirmType (null);
            setDocAction (null);	// CO
            setDocStatus (null);	// DR
            setDocumentNo (null);
            setIsApproved (false);
            setIsCancelled (false);
            setIsInDispute (false);	// N
            setM_InOutConfirm_ID (0);
            setM_InOut_ID (0);
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_InOutConfirm (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27534752389789L;
    /** Last Updated Timestamp 2009-09-10 16:57:53.0 */
    public static final long updatedMS = 1252627073000L;
    /** AD_Table_ID=727 */
    public static final int Table_ID=727;
    
    /** TableName=M_InOutConfirm */
    public static final String Table_Name="M_InOutConfirm";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Approval Amount.
    @param ApprovalAmt Document Approval Amount */
    public void setApprovalAmt (java.math.BigDecimal ApprovalAmt)
    {
        set_Value ("ApprovalAmt", ApprovalAmt);
        
    }
    
    /** Get Approval Amount.
    @return Document Approval Amount */
    public java.math.BigDecimal getApprovalAmt() 
    {
        return get_ValueAsBigDecimal("ApprovalAmt");
        
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
    
    /** Drop Ship Confirm = DS */
    public static final String CONFIRMTYPE_DropShipConfirm = X_Ref_M_InOutConfirm_Type.DROP_SHIP_CONFIRM.getValue();
    /** Pick/QA Confirm = PC */
    public static final String CONFIRMTYPE_PickQAConfirm = X_Ref_M_InOutConfirm_Type.PICK_QA_CONFIRM.getValue();
    /** Ship/Receipt Confirm = SC */
    public static final String CONFIRMTYPE_ShipReceiptConfirm = X_Ref_M_InOutConfirm_Type.SHIP_RECEIPT_CONFIRM.getValue();
    /** Customer Confirmation = XC */
    public static final String CONFIRMTYPE_CustomerConfirmation = X_Ref_M_InOutConfirm_Type.CUSTOMER_CONFIRMATION.getValue();
    /** Vendor Confirmation = XV */
    public static final String CONFIRMTYPE_VendorConfirmation = X_Ref_M_InOutConfirm_Type.VENDOR_CONFIRMATION.getValue();
    /** Set Confirmation Type.
    @param ConfirmType Type of confirmation */
    public void setConfirmType (String ConfirmType)
    {
        if (ConfirmType == null) throw new IllegalArgumentException ("ConfirmType is mandatory");
        if (!X_Ref_M_InOutConfirm_Type.isValid(ConfirmType))
        throw new IllegalArgumentException ("ConfirmType Invalid value - " + ConfirmType + " - Reference_ID=320 - DS - PC - SC - XC - XV");
        set_Value ("ConfirmType", ConfirmType);
        
    }
    
    /** Get Confirmation Type.
    @return Type of confirmation */
    public String getConfirmType() 
    {
        return (String)get_Value("ConfirmType");
        
    }
    
    /** Set Confirmation No.
    @param ConfirmationNo Confirmation Number */
    public void setConfirmationNo (String ConfirmationNo)
    {
        set_Value ("ConfirmationNo", ConfirmationNo);
        
    }
    
    /** Get Confirmation No.
    @return Confirmation Number */
    public String getConfirmationNo() 
    {
        return (String)get_Value("ConfirmationNo");
        
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
    
    /** Set Ship/Receipt Confirmation.
    @param M_InOutConfirm_ID Material Shipment or Receipt Confirmation */
    public void setM_InOutConfirm_ID (int M_InOutConfirm_ID)
    {
        if (M_InOutConfirm_ID < 1) throw new IllegalArgumentException ("M_InOutConfirm_ID is mandatory.");
        set_ValueNoCheck ("M_InOutConfirm_ID", Integer.valueOf(M_InOutConfirm_ID));
        
    }
    
    /** Get Ship/Receipt Confirmation.
    @return Material Shipment or Receipt Confirmation */
    public int getM_InOutConfirm_ID() 
    {
        return get_ValueAsInt("M_InOutConfirm_ID");
        
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
    
    /** Set Phys.Inventory.
    @param M_Inventory_ID Parameters for a Physical Inventory */
    public void setM_Inventory_ID (int M_Inventory_ID)
    {
        if (M_Inventory_ID <= 0) set_Value ("M_Inventory_ID", null);
        else
        set_Value ("M_Inventory_ID", Integer.valueOf(M_Inventory_ID));
        
    }
    
    /** Get Phys.Inventory.
    @return Parameters for a Physical Inventory */
    public int getM_Inventory_ID() 
    {
        return get_ValueAsInt("M_Inventory_ID");
        
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
