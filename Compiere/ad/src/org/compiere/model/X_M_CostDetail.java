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
/** Generated Model for M_CostDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_CostDetail.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_CostDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_CostDetail_ID id
    @param trx transaction
    */
    public X_M_CostDetail (Ctx ctx, int M_CostDetail_ID, Trx trx)
    {
        super (ctx, M_CostDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_CostDetail_ID == 0)
        {
            setAmt (Env.ZERO);
            setC_AcctSchema_ID (0);
            setIsSOTrx (false);
            setM_AttributeSetInstance_ID (0);
            setM_CostDetail_ID (0);
            setM_Product_ID (0);
            setProcessed (false);	// N
            setQty (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_CostDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27522024096789L;
    /** Last Updated Timestamp 2009-04-16 08:19:40.0 */
    public static final long updatedMS = 1239898780000L;
    /** AD_Table_ID=808 */
    public static final int Table_ID=808;
    
    /** TableName=M_CostDetail */
    public static final String Table_Name="M_CostDetail";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Amount.
    @param Amt Amount */
    public void setAmt (java.math.BigDecimal Amt)
    {
        if (Amt == null) throw new IllegalArgumentException ("Amt is mandatory.");
        set_Value ("Amt", Amt);
        
    }
    
    /** Get Amount.
    @return Amount */
    public java.math.BigDecimal getAmt() 
    {
        return get_ValueAsBigDecimal("Amt");
        
    }
    
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
        set_ValueNoCheck ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
    }
    
    /** Set Invoice Line.
    @param C_InvoiceLine_ID Invoice Detail Line */
    public void setC_InvoiceLine_ID (int C_InvoiceLine_ID)
    {
        if (C_InvoiceLine_ID <= 0) set_ValueNoCheck ("C_InvoiceLine_ID", null);
        else
        set_ValueNoCheck ("C_InvoiceLine_ID", Integer.valueOf(C_InvoiceLine_ID));
        
    }
    
    /** Get Invoice Line.
    @return Invoice Detail Line */
    public int getC_InvoiceLine_ID() 
    {
        return get_ValueAsInt("C_InvoiceLine_ID");
        
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
    
    /** Set Project Issue.
    @param C_ProjectIssue_ID Project Issues (Material, Labor) */
    public void setC_ProjectIssue_ID (int C_ProjectIssue_ID)
    {
        if (C_ProjectIssue_ID <= 0) set_Value ("C_ProjectIssue_ID", null);
        else
        set_Value ("C_ProjectIssue_ID", Integer.valueOf(C_ProjectIssue_ID));
        
    }
    
    /** Get Project Issue.
    @return Project Issues (Material, Labor) */
    public int getC_ProjectIssue_ID() 
    {
        return get_ValueAsInt("C_ProjectIssue_ID");
        
    }
    
    /** Set Delta Amount.
    @param DeltaAmt Difference Amount */
    public void setDeltaAmt (java.math.BigDecimal DeltaAmt)
    {
        set_Value ("DeltaAmt", DeltaAmt);
        
    }
    
    /** Get Delta Amount.
    @return Difference Amount */
    public java.math.BigDecimal getDeltaAmt() 
    {
        return get_ValueAsBigDecimal("DeltaAmt");
        
    }
    
    /** Set Delta Quantity.
    @param DeltaQty Quantity Difference */
    public void setDeltaQty (java.math.BigDecimal DeltaQty)
    {
        set_Value ("DeltaQty", DeltaQty);
        
    }
    
    /** Get Delta Quantity.
    @return Quantity Difference */
    public java.math.BigDecimal getDeltaQty() 
    {
        return get_ValueAsBigDecimal("DeltaQty");
        
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
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID < 0) throw new IllegalArgumentException ("M_AttributeSetInstance_ID is mandatory.");
        set_ValueNoCheck ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get Attribute Set Instance.
    @return Product Attribute Set Instance */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
    }
    
    /** Set Cost Detail.
    @param M_CostDetail_ID Cost Detail Information */
    public void setM_CostDetail_ID (int M_CostDetail_ID)
    {
        if (M_CostDetail_ID < 1) throw new IllegalArgumentException ("M_CostDetail_ID is mandatory.");
        set_ValueNoCheck ("M_CostDetail_ID", Integer.valueOf(M_CostDetail_ID));
        
    }
    
    /** Get Cost Detail.
    @return Cost Detail Information */
    public int getM_CostDetail_ID() 
    {
        return get_ValueAsInt("M_CostDetail_ID");
        
    }
    
    /** Set Cost Element.
    @param M_CostElement_ID Product Cost Element */
    public void setM_CostElement_ID (int M_CostElement_ID)
    {
        if (M_CostElement_ID <= 0) set_ValueNoCheck ("M_CostElement_ID", null);
        else
        set_ValueNoCheck ("M_CostElement_ID", Integer.valueOf(M_CostElement_ID));
        
    }
    
    /** Get Cost Element.
    @return Product Cost Element */
    public int getM_CostElement_ID() 
    {
        return get_ValueAsInt("M_CostElement_ID");
        
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
    
    /** Set Phys Inventory Line.
    @param M_InventoryLine_ID Unique line in an Inventory document */
    public void setM_InventoryLine_ID (int M_InventoryLine_ID)
    {
        if (M_InventoryLine_ID <= 0) set_Value ("M_InventoryLine_ID", null);
        else
        set_Value ("M_InventoryLine_ID", Integer.valueOf(M_InventoryLine_ID));
        
    }
    
    /** Get Phys Inventory Line.
    @return Unique line in an Inventory document */
    public int getM_InventoryLine_ID() 
    {
        return get_ValueAsInt("M_InventoryLine_ID");
        
    }
    
    /** Set Move Line.
    @param M_MovementLine_ID Inventory Move document Line */
    public void setM_MovementLine_ID (int M_MovementLine_ID)
    {
        if (M_MovementLine_ID <= 0) set_Value ("M_MovementLine_ID", null);
        else
        set_Value ("M_MovementLine_ID", Integer.valueOf(M_MovementLine_ID));
        
    }
    
    /** Get Move Line.
    @return Inventory Move document Line */
    public int getM_MovementLine_ID() 
    {
        return get_ValueAsInt("M_MovementLine_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Production Line.
    @param M_ProductionLine_ID Document Line representing a production */
    public void setM_ProductionLine_ID (int M_ProductionLine_ID)
    {
        if (M_ProductionLine_ID <= 0) set_Value ("M_ProductionLine_ID", null);
        else
        set_Value ("M_ProductionLine_ID", Integer.valueOf(M_ProductionLine_ID));
        
    }
    
    /** Get Production Line.
    @return Document Line representing a production */
    public int getM_ProductionLine_ID() 
    {
        return get_ValueAsInt("M_ProductionLine_ID");
        
    }
    
    /** Set Work Order Resource Transaction Line.
    @param M_WorkOrderResourceTxnLine_ID Identifies the production resource detail lines in a work order transaction */
    public void setM_WorkOrderResourceTxnLine_ID (int M_WorkOrderResourceTxnLine_ID)
    {
        if (M_WorkOrderResourceTxnLine_ID <= 0) set_Value ("M_WorkOrderResourceTxnLine_ID", null);
        else
        set_Value ("M_WorkOrderResourceTxnLine_ID", Integer.valueOf(M_WorkOrderResourceTxnLine_ID));
        
    }
    
    /** Get Work Order Resource Transaction Line.
    @return Identifies the production resource detail lines in a work order transaction */
    public int getM_WorkOrderResourceTxnLine_ID() 
    {
        return get_ValueAsInt("M_WorkOrderResourceTxnLine_ID");
        
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
    
    /** Set Price.
    @param Price Price */
    public void setPrice (java.math.BigDecimal Price)
    {
        throw new IllegalArgumentException ("Price is virtual column");
        
    }
    
    /** Get Price.
    @return Price */
    public java.math.BigDecimal getPrice() 
    {
        return get_ValueAsBigDecimal("Price");
        
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
    public void setQty (java.math.BigDecimal Qty)
    {
        if (Qty == null) throw new IllegalArgumentException ("Qty is mandatory.");
        set_Value ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    
}
