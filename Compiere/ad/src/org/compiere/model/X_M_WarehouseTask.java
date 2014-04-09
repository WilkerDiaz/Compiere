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
/** Generated Model for M_WarehouseTask
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_M_WarehouseTask.java 8873 2010-05-31 10:15:25Z sdandapat $ */
public class X_M_WarehouseTask extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WarehouseTask_ID id
    @param trx transaction
    */
    public X_M_WarehouseTask (Ctx ctx, int M_WarehouseTask_ID, Trx trx)
    {
        super (ctx, M_WarehouseTask_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WarehouseTask_ID == 0)
        {
            setC_DocType_ID (0);
            setC_UOM_ID (0);
            setDocAction (null);	// CO
            setDocStatus (null);	// DR
            setDocumentNo (null);
            setIsApproved (false);	// N
            setM_LocatorTo_ID (0);
            setM_Locator_ID (0);
            setM_Product_ID (0);
            setM_WarehouseTask_ID (0);
            setM_Warehouse_ID (0);
            setMovementDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setMovementQty (Env.ZERO);
            setQtyDedicated (Env.ZERO);	// 0
            setQtyEntered (Env.ZERO);
            setQtySuggested (Env.ZERO);
            setTargetQty (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WarehouseTask (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27557377794789L;
    /** Last Updated Timestamp 2010-05-31 02:17:58.0 */
    public static final long updatedMS = 1275252478000L;
    /** AD_Table_ID=1018 */
    public static final int Table_ID=1018;
    
    /** TableName=M_WarehouseTask */
    public static final String Table_Name="M_WarehouseTask";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Order Line.
    @param C_OrderLine_ID Order Line */
    public void setC_OrderLine_ID (int C_OrderLine_ID)
    {
        if (C_OrderLine_ID <= 0) set_Value ("C_OrderLine_ID", null);
        else
        set_Value ("C_OrderLine_ID", Integer.valueOf(C_OrderLine_ID));
        
    }
    
    /** Get Order Line.
    @return Order Line */
    public int getC_OrderLine_ID() 
    {
        return get_ValueAsInt("C_OrderLine_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID < 1) throw new IllegalArgumentException ("C_UOM_ID is mandatory.");
        set_Value ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Wave Line.
    @param C_WaveLine_ID Selected order lines for which there is sufficient onhand quantity in the warehouse */
    public void setC_WaveLine_ID (int C_WaveLine_ID)
    {
        if (C_WaveLine_ID <= 0) set_Value ("C_WaveLine_ID", null);
        else
        set_Value ("C_WaveLine_ID", Integer.valueOf(C_WaveLine_ID));
        
    }
    
    /** Get Wave Line.
    @return Selected order lines for which there is sufficient onhand quantity in the warehouse */
    public int getC_WaveLine_ID() 
    {
        return get_ValueAsInt("C_WaveLine_ID");
        
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
    
    /** Set Actual Attribute Set Instance.
    @param M_ActualASI_ID Product Attribute Set Instance actually used for the warehouse task */
    public void setM_ActualASI_ID (int M_ActualASI_ID)
    {
        if (M_ActualASI_ID <= 0) set_Value ("M_ActualASI_ID", null);
        else
        set_Value ("M_ActualASI_ID", Integer.valueOf(M_ActualASI_ID));
        
    }
    
    /** Get Actual Attribute Set Instance.
    @return Product Attribute Set Instance actually used for the warehouse task */
    public int getM_ActualASI_ID() 
    {
        return get_ValueAsInt("M_ActualASI_ID");
        
    }
    
    /** Set Actual Destination Locator.
    @param M_ActualLocatorTo_ID Actual locator where the stock was moved to */
    public void setM_ActualLocatorTo_ID (int M_ActualLocatorTo_ID)
    {
        if (M_ActualLocatorTo_ID <= 0) set_Value ("M_ActualLocatorTo_ID", null);
        else
        set_Value ("M_ActualLocatorTo_ID", Integer.valueOf(M_ActualLocatorTo_ID));
        
    }
    
    /** Get Actual Destination Locator.
    @return Actual locator where the stock was moved to */
    public int getM_ActualLocatorTo_ID() 
    {
        return get_ValueAsInt("M_ActualLocatorTo_ID");
        
    }
    
    /** Set Actual Source Locator.
    @param M_ActualLocator_ID Actual locator from where the stock was moved */
    public void setM_ActualLocator_ID (int M_ActualLocator_ID)
    {
        if (M_ActualLocator_ID <= 0) set_Value ("M_ActualLocator_ID", null);
        else
        set_Value ("M_ActualLocator_ID", Integer.valueOf(M_ActualLocator_ID));
        
    }
    
    /** Get Actual Source Locator.
    @return Actual locator from where the stock was moved */
    public int getM_ActualLocator_ID() 
    {
        return get_ValueAsInt("M_ActualLocator_ID");
        
    }
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID <= 0) set_Value ("M_AttributeSetInstance_ID", null);
        else
        set_Value ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get Attribute Set Instance.
    @return Product Attribute Set Instance */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
    }
    
    /** Set Shipment/Receipt Line.
    @param M_InOutLine_ID Line on Shipment or Receipt document */
    public void setM_InOutLine_ID (int M_InOutLine_ID)
    {
        if (M_InOutLine_ID <= 0) set_ValueNoCheck ("M_InOutLine_ID", null);
        else
        set_ValueNoCheck ("M_InOutLine_ID", Integer.valueOf(M_InOutLine_ID));
        
    }
    
    /** Get Shipment/Receipt Line.
    @return Line on Shipment or Receipt document */
    public int getM_InOutLine_ID() 
    {
        return get_ValueAsInt("M_InOutLine_ID");
        
    }
    
    /** Set Locator To.
    @param M_LocatorTo_ID Location inventory is moved to */
    public void setM_LocatorTo_ID (int M_LocatorTo_ID)
    {
        if (M_LocatorTo_ID < 1) throw new IllegalArgumentException ("M_LocatorTo_ID is mandatory.");
        set_Value ("M_LocatorTo_ID", Integer.valueOf(M_LocatorTo_ID));
        
    }
    
    /** Get Locator To.
    @return Location inventory is moved to */
    public int getM_LocatorTo_ID() 
    {
        return get_ValueAsInt("M_LocatorTo_ID");
        
    }
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID < 1) throw new IllegalArgumentException ("M_Locator_ID is mandatory.");
        set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Split Warehouse Task.
    @param M_SplitWarehouseTask_ID Warehouse Task that this task was split from */
    public void setM_SplitWarehouseTask_ID (int M_SplitWarehouseTask_ID)
    {
        if (M_SplitWarehouseTask_ID <= 0) set_ValueNoCheck ("M_SplitWarehouseTask_ID", null);
        else
        set_ValueNoCheck ("M_SplitWarehouseTask_ID", Integer.valueOf(M_SplitWarehouseTask_ID));
        
    }
    
    /** Get Split Warehouse Task.
    @return Warehouse Task that this task was split from */
    public int getM_SplitWarehouseTask_ID() 
    {
        return get_ValueAsInt("M_SplitWarehouseTask_ID");
        
    }
    
    /** Set Warehouse Task.
    @param M_WarehouseTask_ID A Warehouse Task represents a basic warehouse operation such as putaway, picking or replenishment. */
    public void setM_WarehouseTask_ID (int M_WarehouseTask_ID)
    {
        if (M_WarehouseTask_ID < 1) throw new IllegalArgumentException ("M_WarehouseTask_ID is mandatory.");
        set_ValueNoCheck ("M_WarehouseTask_ID", Integer.valueOf(M_WarehouseTask_ID));
        
    }
    
    /** Get Warehouse Task.
    @return A Warehouse Task represents a basic warehouse operation such as putaway, picking or replenishment. */
    public int getM_WarehouseTask_ID() 
    {
        return get_ValueAsInt("M_WarehouseTask_ID");
        
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
    
    /** Set Work Order Component.
    @param M_WorkOrderComponent_ID Work Order Component */
    public void setM_WorkOrderComponent_ID (int M_WorkOrderComponent_ID)
    {
        if (M_WorkOrderComponent_ID <= 0) set_Value ("M_WorkOrderComponent_ID", null);
        else
        set_Value ("M_WorkOrderComponent_ID", Integer.valueOf(M_WorkOrderComponent_ID));
        
    }
    
    /** Get Work Order Component.
    @return Work Order Component */
    public int getM_WorkOrderComponent_ID() 
    {
        return get_ValueAsInt("M_WorkOrderComponent_ID");
        
    }
    
    /** Set Work Order Transaction Line.
    @param M_WorkOrderTransactionLine_ID Work Order Transaction Line */
    public void setM_WorkOrderTransactionLine_ID (int M_WorkOrderTransactionLine_ID)
    {
        if (M_WorkOrderTransactionLine_ID <= 0) set_Value ("M_WorkOrderTransactionLine_ID", null);
        else
        set_Value ("M_WorkOrderTransactionLine_ID", Integer.valueOf(M_WorkOrderTransactionLine_ID));
        
    }
    
    /** Get Work Order Transaction Line.
    @return Work Order Transaction Line */
    public int getM_WorkOrderTransactionLine_ID() 
    {
        return get_ValueAsInt("M_WorkOrderTransactionLine_ID");
        
    }
    
    /** Set Work Order.
    @param M_WorkOrder_ID Work Order */
    public void setM_WorkOrder_ID (int M_WorkOrder_ID)
    {
        throw new IllegalArgumentException ("M_WorkOrder_ID is virtual column");
        
    }
    
    /** Get Work Order.
    @return Work Order */
    public int getM_WorkOrder_ID() 
    {
        return get_ValueAsInt("M_WorkOrder_ID");
        
    }
    
    /** Set Movement Date.
    @param MovementDate Date a product was moved in or out of inventory */
    public void setMovementDate (Timestamp MovementDate)
    {
        if (MovementDate == null) throw new IllegalArgumentException ("MovementDate is mandatory.");
        set_ValueNoCheck ("MovementDate", MovementDate);
        
    }
    
    /** Get Movement Date.
    @return Date a product was moved in or out of inventory */
    public Timestamp getMovementDate() 
    {
        return (Timestamp)get_Value("MovementDate");
        
    }
    
    /** Set Movement Quantity.
    @param MovementQty Quantity of a product moved. */
    public void setMovementQty (java.math.BigDecimal MovementQty)
    {
        if (MovementQty == null) throw new IllegalArgumentException ("MovementQty is mandatory.");
        set_Value ("MovementQty", MovementQty);
        
    }
    
    /** Get Movement Quantity.
    @return Quantity of a product moved. */
    public java.math.BigDecimal getMovementQty() 
    {
        return get_ValueAsBigDecimal("MovementQty");
        
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
    
    /** Set Quantity Dedicated.
    @param QtyDedicated Quantity for which there is a pending Warehouse Task */
    public void setQtyDedicated (java.math.BigDecimal QtyDedicated)
    {
        if (QtyDedicated == null) throw new IllegalArgumentException ("QtyDedicated is mandatory.");
        set_Value ("QtyDedicated", QtyDedicated);
        
    }
    
    /** Get Quantity Dedicated.
    @return Quantity for which there is a pending Warehouse Task */
    public java.math.BigDecimal getQtyDedicated() 
    {
        return get_ValueAsBigDecimal("QtyDedicated");
        
    }
    
    /** Set Quantity.
    @param QtyEntered The Quantity Entered is based on the selected UoM */
    public void setQtyEntered (java.math.BigDecimal QtyEntered)
    {
        if (QtyEntered == null) throw new IllegalArgumentException ("QtyEntered is mandatory.");
        set_Value ("QtyEntered", QtyEntered);
        
    }
    
    /** Get Quantity.
    @return The Quantity Entered is based on the selected UoM */
    public java.math.BigDecimal getQtyEntered() 
    {
        return get_ValueAsBigDecimal("QtyEntered");
        
    }
    
    /** Set Suggested Quantity.
    @param QtySuggested Quantity suggested for Pick or Putaway by the Putaway or Pick process */
    public void setQtySuggested (java.math.BigDecimal QtySuggested)
    {
        if (QtySuggested == null) throw new IllegalArgumentException ("QtySuggested is mandatory.");
        set_Value ("QtySuggested", QtySuggested);
        
    }
    
    /** Get Suggested Quantity.
    @return Quantity suggested for Pick or Putaway by the Putaway or Pick process */
    public java.math.BigDecimal getQtySuggested() 
    {
        return get_ValueAsBigDecimal("QtySuggested");
        
    }
    
    /** Set Split Task.
    @param SplitTask Split Warehouse Task into two tasks */
    public void setSplitTask (String SplitTask)
    {
        set_Value ("SplitTask", SplitTask);
        
    }
    
    /** Get Split Task.
    @return Split Warehouse Task into two tasks */
    public String getSplitTask() 
    {
        return (String)get_Value("SplitTask");
        
    }
    
    /** Set Target Quantity.
    @param TargetQty Target Movement Quantity */
    public void setTargetQty (java.math.BigDecimal TargetQty)
    {
        if (TargetQty == null) throw new IllegalArgumentException ("TargetQty is mandatory.");
        set_Value ("TargetQty", TargetQty);
        
    }
    
    /** Get Target Quantity.
    @return Target Movement Quantity */
    public java.math.BigDecimal getTargetQty() 
    {
        return get_ValueAsBigDecimal("TargetQty");
        
    }
    
    
}
