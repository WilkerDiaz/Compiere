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
/** Generated Model for M_InOutLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_InOutLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_InOutLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_InOutLine_ID id
    @param trx transaction
    */
    public X_M_InOutLine (Ctx ctx, int M_InOutLine_ID, Trx trx)
    {
        super (ctx, M_InOutLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_InOutLine_ID == 0)
        {
            setC_UOM_ID (0);	// @#C_UOM_ID@
            setIsDescription (false);	// N
            setIsInvoiced (false);
            setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_InOutLine WHERE M_InOut_ID=@M_InOut_ID@
            setM_InOutLine_ID (0);
            setM_InOut_ID (0);
            setMovementQty (Env.ZERO);	// 1
            setProcessed (false);	// N
            setQtyAllocated (Env.ZERO);	// 0
            setQtyEntered (Env.ZERO);	// 1
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_InOutLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27535393711789L;
    /** Last Updated Timestamp 2009-09-18 03:06:35.0 */
    public static final long updatedMS = 1253268395000L;
    /** AD_Table_ID=320 */
    public static final int Table_ID=320;
    
    /** TableName=M_InOutLine */
    public static final String Table_Name="M_InOutLine";
    
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
    
    /** Set Order Line.
    @param C_OrderLine_ID Order Line */
    public void setC_OrderLine_ID (int C_OrderLine_ID)
    {
        if (C_OrderLine_ID <= 0) set_ValueNoCheck ("C_OrderLine_ID", null);
        else
        set_ValueNoCheck ("C_OrderLine_ID", Integer.valueOf(C_OrderLine_ID));
        
    }
    
    /** Get Order Line.
    @return Order Line */
    public int getC_OrderLine_ID() 
    {
        return get_ValueAsInt("C_OrderLine_ID");
        
    }
    
    /** Set Project Phase.
    @param C_ProjectPhase_ID Phase of a Project */
    public void setC_ProjectPhase_ID (int C_ProjectPhase_ID)
    {
        if (C_ProjectPhase_ID <= 0) set_Value ("C_ProjectPhase_ID", null);
        else
        set_Value ("C_ProjectPhase_ID", Integer.valueOf(C_ProjectPhase_ID));
        
    }
    
    /** Get Project Phase.
    @return Phase of a Project */
    public int getC_ProjectPhase_ID() 
    {
        return get_ValueAsInt("C_ProjectPhase_ID");
        
    }
    
    /** Set Project Task.
    @param C_ProjectTask_ID Actual Project Task in a Phase */
    public void setC_ProjectTask_ID (int C_ProjectTask_ID)
    {
        if (C_ProjectTask_ID <= 0) set_Value ("C_ProjectTask_ID", null);
        else
        set_Value ("C_ProjectTask_ID", Integer.valueOf(C_ProjectTask_ID));
        
    }
    
    /** Get Project Task.
    @return Actual Project Task in a Phase */
    public int getC_ProjectTask_ID() 
    {
        return get_ValueAsInt("C_ProjectTask_ID");
        
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
        set_ValueNoCheck ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Confirmed Quantity.
    @param ConfirmedQty Confirmation of a received quantity */
    public void setConfirmedQty (java.math.BigDecimal ConfirmedQty)
    {
        set_Value ("ConfirmedQty", ConfirmedQty);
        
    }
    
    /** Get Confirmed Quantity.
    @return Confirmation of a received quantity */
    public java.math.BigDecimal getConfirmedQty() 
    {
        return get_ValueAsBigDecimal("ConfirmedQty");
        
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
    
    /** Set Description Only.
    @param IsDescription If true, the line is just description and no transaction */
    public void setIsDescription (boolean IsDescription)
    {
        set_Value ("IsDescription", Boolean.valueOf(IsDescription));
        
    }
    
    /** Get Description Only.
    @return If true, the line is just description and no transaction */
    public boolean isDescription() 
    {
        return get_ValueAsBoolean("IsDescription");
        
    }
    
    /** Set Invoiced.
    @param IsInvoiced Is this invoiced? */
    public void setIsInvoiced (boolean IsInvoiced)
    {
        set_Value ("IsInvoiced", Boolean.valueOf(IsInvoiced));
        
    }
    
    /** Get Invoiced.
    @return Is this invoiced? */
    public boolean isInvoiced() 
    {
        return get_ValueAsBoolean("IsInvoiced");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getLine()));
        
    }
    
    /** Unknown = ?? */
    public static final String LINEDOCSTATUS_Unknown = X_Ref__Document_Status.UNKNOWN.getValue();
    /** Approved = AP */
    public static final String LINEDOCSTATUS_Approved = X_Ref__Document_Status.APPROVED.getValue();
    /** Closed = CL */
    public static final String LINEDOCSTATUS_Closed = X_Ref__Document_Status.CLOSED.getValue();
    /** Completed = CO */
    public static final String LINEDOCSTATUS_Completed = X_Ref__Document_Status.COMPLETED.getValue();
    /** Drafted = DR */
    public static final String LINEDOCSTATUS_Drafted = X_Ref__Document_Status.DRAFTED.getValue();
    /** Invalid = IN */
    public static final String LINEDOCSTATUS_Invalid = X_Ref__Document_Status.INVALID.getValue();
    /** In Progress = IP */
    public static final String LINEDOCSTATUS_InProgress = X_Ref__Document_Status.IN_PROGRESS.getValue();
    /** Not Approved = NA */
    public static final String LINEDOCSTATUS_NotApproved = X_Ref__Document_Status.NOT_APPROVED.getValue();
    /** Reversed = RE */
    public static final String LINEDOCSTATUS_Reversed = X_Ref__Document_Status.REVERSED.getValue();
    /** Voided = VO */
    public static final String LINEDOCSTATUS_Voided = X_Ref__Document_Status.VOIDED.getValue();
    /** Waiting Confirmation = WC */
    public static final String LINEDOCSTATUS_WaitingConfirmation = X_Ref__Document_Status.WAITING_CONFIRMATION.getValue();
    /** Waiting Payment = WP */
    public static final String LINEDOCSTATUS_WaitingPayment = X_Ref__Document_Status.WAITING_PAYMENT.getValue();
    /** Set Line Document Status.
    @param LineDocStatus The current status of the document line */
    public void setLineDocStatus (String LineDocStatus)
    {
        if (!X_Ref__Document_Status.isValid(LineDocStatus))
        throw new IllegalArgumentException ("LineDocStatus Invalid value - " + LineDocStatus + " - Reference_ID=131 - ?? - AP - CL - CO - DR - IN - IP - NA - RE - VO - WC - WP");
        set_Value ("LineDocStatus", LineDocStatus);
        
    }
    
    /** Get Line Document Status.
    @return The current status of the document line */
    public String getLineDocStatus() 
    {
        return (String)get_Value("LineDocStatus");
        
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
        if (M_InOutLine_ID < 1) throw new IllegalArgumentException ("M_InOutLine_ID is mandatory.");
        set_ValueNoCheck ("M_InOutLine_ID", Integer.valueOf(M_InOutLine_ID));
        
    }
    
    /** Get Shipment/Receipt Line.
    @return Line on Shipment or Receipt document */
    public int getM_InOutLine_ID() 
    {
        return get_ValueAsInt("M_InOutLine_ID");
        
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
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID <= 0) set_Value ("M_Locator_ID", null);
        else
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
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** Set Orig Sales Order Line.
    @param Orig_OrderLine_ID Original Sales Order Line for Return Material Authorization */
    public void setOrig_OrderLine_ID (int Orig_OrderLine_ID)
    {
        throw new IllegalArgumentException ("Orig_OrderLine_ID is virtual column");
        
    }
    
    /** Get Orig Sales Order Line.
    @return Original Sales Order Line for Return Material Authorization */
    public int getOrig_OrderLine_ID() 
    {
        return get_ValueAsInt("Orig_OrderLine_ID");
        
    }
    
    /** Set Picked Quantity.
    @param PickedQty Picked Quantity */
    public void setPickedQty (java.math.BigDecimal PickedQty)
    {
        set_Value ("PickedQty", PickedQty);
        
    }
    
    /** Get Picked Quantity.
    @return Picked Quantity */
    public java.math.BigDecimal getPickedQty() 
    {
        return get_ValueAsBigDecimal("PickedQty");
        
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
    
    /** Set Quantity Allocated.
    @param QtyAllocated Quantity that has been picked and is awaiting shipment */
    public void setQtyAllocated (java.math.BigDecimal QtyAllocated)
    {
        if (QtyAllocated == null) throw new IllegalArgumentException ("QtyAllocated is mandatory.");
        set_Value ("QtyAllocated", QtyAllocated);
        
    }
    
    /** Get Quantity Allocated.
    @return Quantity that has been picked and is awaiting shipment */
    public java.math.BigDecimal getQtyAllocated() 
    {
        return get_ValueAsBigDecimal("QtyAllocated");
        
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
    
    /** Set Referenced Shipment Line.
    @param Ref_InOutLine_ID Referenced Shipment Line */
    public void setRef_InOutLine_ID (int Ref_InOutLine_ID)
    {
        if (Ref_InOutLine_ID <= 0) set_Value ("Ref_InOutLine_ID", null);
        else
        set_Value ("Ref_InOutLine_ID", Integer.valueOf(Ref_InOutLine_ID));
        
    }
    
    /** Get Referenced Shipment Line.
    @return Referenced Shipment Line */
    public int getRef_InOutLine_ID() 
    {
        return get_ValueAsInt("Ref_InOutLine_ID");
        
    }
    
    /** Set Scrapped Quantity.
    @param ScrappedQty The Quantity scrapped due to QA issues */
    public void setScrappedQty (java.math.BigDecimal ScrappedQty)
    {
        set_Value ("ScrappedQty", ScrappedQty);
        
    }
    
    /** Get Scrapped Quantity.
    @return The Quantity scrapped due to QA issues */
    public java.math.BigDecimal getScrappedQty() 
    {
        return get_ValueAsBigDecimal("ScrappedQty");
        
    }
    
    /** Set Target Quantity.
    @param TargetQty Target Movement Quantity */
    public void setTargetQty (java.math.BigDecimal TargetQty)
    {
        set_Value ("TargetQty", TargetQty);
        
    }
    
    /** Get Target Quantity.
    @return Target Movement Quantity */
    public java.math.BigDecimal getTargetQty() 
    {
        return get_ValueAsBigDecimal("TargetQty");
        
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
