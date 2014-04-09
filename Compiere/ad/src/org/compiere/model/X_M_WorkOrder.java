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
/** Generated Model for M_WorkOrder
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_WorkOrder.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_WorkOrder extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WorkOrder_ID id
    @param trx transaction
    */
    public X_M_WorkOrder (Ctx ctx, int M_WorkOrder_ID, Trx trx)
    {
        super (ctx, M_WorkOrder_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WorkOrder_ID == 0)
        {
            setC_DocType_ID (0);
            setC_UOM_ID (0);
            setDateAcct (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDocAction (null);	// PR
            setDocStatus (null);	// DR
            setDocumentNo (null);
            setIsApproved (false);	// @IsApproved@
            setIsSOTrx (true);	// Y
            setM_Locator_ID (0);
            setM_Product_ID (0);
            setM_Warehouse_ID (0);	// @#M_Warehouse_ID@
            setM_WorkOrderClass_ID (0);
            setM_WorkOrder_ID (0);
            setPosted (false);	// N
            setPriorityRule (null);	// 5
            setProcessed (false);	// N
            setQtyAssembled (Env.ZERO);	// 0
            setQtyAvailable (Env.ZERO);	// 0
            setQtyEntered (Env.ZERO);	// 1
            setQtyScrapped (Env.ZERO);	// 0
            setSendEMail (false);	// N
            setWOType (null);	// S
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WorkOrder (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27526263729789L;
    /** Last Updated Timestamp 2009-06-04 11:00:13.0 */
    public static final long updatedMS = 1244138413000L;
    /** AD_Table_ID=1026 */
    public static final int Table_ID=1026;
    
    /** TableName=M_WorkOrder */
    public static final String Table_Name="M_WorkOrder";
    
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
    
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID <= 0) set_Value ("C_BPartner_Location_ID", null);
        else
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
    
    /** Set Actual Date From.
    @param DateActualFrom Actual date an activity started */
    public void setDateActualFrom (Timestamp DateActualFrom)
    {
        set_Value ("DateActualFrom", DateActualFrom);
        
    }
    
    /** Get Actual Date From.
    @return Actual date an activity started */
    public Timestamp getDateActualFrom() 
    {
        return (Timestamp)get_Value("DateActualFrom");
        
    }
    
    /** Set Actual Date To.
    @param DateActualTo Actual date an activity ended */
    public void setDateActualTo (Timestamp DateActualTo)
    {
        set_Value ("DateActualTo", DateActualTo);
        
    }
    
    /** Get Actual Date To.
    @return Actual date an activity ended */
    public Timestamp getDateActualTo() 
    {
        return (Timestamp)get_Value("DateActualTo");
        
    }
    
    /** Set Scheduled Date From.
    @param DateScheduleFrom Date an activity is scheduled to start */
    public void setDateScheduleFrom (Timestamp DateScheduleFrom)
    {
        set_Value ("DateScheduleFrom", DateScheduleFrom);
        
    }
    
    /** Get Scheduled Date From.
    @return Date an activity is scheduled to start */
    public Timestamp getDateScheduleFrom() 
    {
        return (Timestamp)get_Value("DateScheduleFrom");
        
    }
    
    /** Set Scheduled Date To.
    @param DateScheduleTo Date an activity is scheduled to end */
    public void setDateScheduleTo (Timestamp DateScheduleTo)
    {
        set_Value ("DateScheduleTo", DateScheduleTo);
        
    }
    
    /** Get Scheduled Date To.
    @return Date an activity is scheduled to end */
    public Timestamp getDateScheduleTo() 
    {
        return (Timestamp)get_Value("DateScheduleTo");
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
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
    
    /** Set Plan Run.
    @param MRP_PlanRun_ID An execution of the plan */
    public void setMRP_PlanRun_ID (int MRP_PlanRun_ID)
    {
        if (MRP_PlanRun_ID <= 0) set_ValueNoCheck ("MRP_PlanRun_ID", null);
        else
        set_ValueNoCheck ("MRP_PlanRun_ID", Integer.valueOf(MRP_PlanRun_ID));
        
    }
    
    /** Get Plan Run.
    @return An execution of the plan */
    public int getMRP_PlanRun_ID() 
    {
        return get_ValueAsInt("MRP_PlanRun_ID");
        
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
    
    /** Set BOM.
    @param M_BOM_ID Bill of Materials */
    public void setM_BOM_ID (int M_BOM_ID)
    {
        if (M_BOM_ID <= 0) set_Value ("M_BOM_ID", null);
        else
        set_Value ("M_BOM_ID", Integer.valueOf(M_BOM_ID));
        
    }
    
    /** Get BOM.
    @return Bill of Materials */
    public int getM_BOM_ID() 
    {
        return get_ValueAsInt("M_BOM_ID");
        
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
    
    /** Set Routing.
    @param M_Routing_ID Routing for an assembly */
    public void setM_Routing_ID (int M_Routing_ID)
    {
        if (M_Routing_ID <= 0) set_Value ("M_Routing_ID", null);
        else
        set_Value ("M_Routing_ID", Integer.valueOf(M_Routing_ID));
        
    }
    
    /** Get Routing.
    @return Routing for an assembly */
    public int getM_Routing_ID() 
    {
        return get_ValueAsInt("M_Routing_ID");
        
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
    
    /** Set Work Order Class.
    @param M_WorkOrderClass_ID Indicates the document types and accounts to be used for a work order */
    public void setM_WorkOrderClass_ID (int M_WorkOrderClass_ID)
    {
        if (M_WorkOrderClass_ID < 1) throw new IllegalArgumentException ("M_WorkOrderClass_ID is mandatory.");
        set_ValueNoCheck ("M_WorkOrderClass_ID", Integer.valueOf(M_WorkOrderClass_ID));
        
    }
    
    /** Get Work Order Class.
    @return Indicates the document types and accounts to be used for a work order */
    public int getM_WorkOrderClass_ID() 
    {
        return get_ValueAsInt("M_WorkOrderClass_ID");
        
    }
    
    /** Set Work Order.
    @param M_WorkOrder_ID Work Order */
    public void setM_WorkOrder_ID (int M_WorkOrder_ID)
    {
        if (M_WorkOrder_ID < 1) throw new IllegalArgumentException ("M_WorkOrder_ID is mandatory.");
        set_ValueNoCheck ("M_WorkOrder_ID", Integer.valueOf(M_WorkOrder_ID));
        
    }
    
    /** Get Work Order.
    @return Work Order */
    public int getM_WorkOrder_ID() 
    {
        return get_ValueAsInt("M_WorkOrder_ID");
        
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
    
    /** Set Quantity Assembled.
    @param QtyAssembled Quantity finished at a production routing step */
    public void setQtyAssembled (java.math.BigDecimal QtyAssembled)
    {
        if (QtyAssembled == null) throw new IllegalArgumentException ("QtyAssembled is mandatory.");
        set_Value ("QtyAssembled", QtyAssembled);
        
    }
    
    /** Get Quantity Assembled.
    @return Quantity finished at a production routing step */
    public java.math.BigDecimal getQtyAssembled() 
    {
        return get_ValueAsBigDecimal("QtyAssembled");
        
    }
    
    /** Set Available Quantity.
    @param QtyAvailable Available Quantity (On Hand - Reserved) */
    public void setQtyAvailable (java.math.BigDecimal QtyAvailable)
    {
        if (QtyAvailable == null) throw new IllegalArgumentException ("QtyAvailable is mandatory.");
        set_Value ("QtyAvailable", QtyAvailable);
        
    }
    
    /** Get Available Quantity.
    @return Available Quantity (On Hand - Reserved) */
    public java.math.BigDecimal getQtyAvailable() 
    {
        return get_ValueAsBigDecimal("QtyAvailable");
        
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
    
    /** Set Quantity Scrapped.
    @param QtyScrapped This is the number of sub-assemblies in the Scrap step of an operation in Work Order. */
    public void setQtyScrapped (java.math.BigDecimal QtyScrapped)
    {
        if (QtyScrapped == null) throw new IllegalArgumentException ("QtyScrapped is mandatory.");
        set_Value ("QtyScrapped", QtyScrapped);
        
    }
    
    /** Get Quantity Scrapped.
    @return This is the number of sub-assemblies in the Scrap step of an operation in Work Order. */
    public java.math.BigDecimal getQtyScrapped() 
    {
        return get_ValueAsBigDecimal("QtyScrapped");
        
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
    
    /** Set Supervisor.
    @param Supervisor_ID Supervisor for this user/organization - used for escalation and approval */
    public void setSupervisor_ID (int Supervisor_ID)
    {
        if (Supervisor_ID <= 0) set_Value ("Supervisor_ID", null);
        else
        set_Value ("Supervisor_ID", Integer.valueOf(Supervisor_ID));
        
    }
    
    /** Get Supervisor.
    @return Supervisor for this user/organization - used for escalation and approval */
    public int getSupervisor_ID() 
    {
        return get_ValueAsInt("Supervisor_ID");
        
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
    
    /** Sales Order = O */
    public static final String WOSOURCE_SalesOrder = X_Ref_M_WorkOrder_WOSource.SALES_ORDER.getValue();
    /** Set Source.
    @param WOSource Work Order source */
    public void setWOSource (String WOSource)
    {
        if (!X_Ref_M_WorkOrder_WOSource.isValid(WOSource))
        throw new IllegalArgumentException ("WOSource Invalid value - " + WOSource + " - Reference_ID=449 - O");
        set_Value ("WOSource", WOSource);
        
    }
    
    /** Get Source.
    @return Work Order source */
    public String getWOSource() 
    {
        return (String)get_Value("WOSource");
        
    }
    
    /** Refurbish = F */
    public static final String WOTYPE_Refurbish = X_Ref_M_WorkOrder_Type.REFURBISH.getValue();
    /** Repair = R */
    public static final String WOTYPE_Repair = X_Ref_M_WorkOrder_Type.REPAIR.getValue();
    /** Standard = S */
    public static final String WOTYPE_Standard = X_Ref_M_WorkOrder_Type.STANDARD.getValue();
    /** Set Work Order Type.
    @param WOType Work Order Type */
    public void setWOType (String WOType)
    {
        if (WOType == null) throw new IllegalArgumentException ("WOType is mandatory");
        if (!X_Ref_M_WorkOrder_Type.isValid(WOType))
        throw new IllegalArgumentException ("WOType Invalid value - " + WOType + " - Reference_ID=450 - F - R - S");
        set_ValueNoCheck ("WOType", WOType);
        
    }
    
    /** Get Work Order Type.
    @return Work Order Type */
    public String getWOType() 
    {
        return (String)get_Value("WOType");
        
    }
    
    
}
