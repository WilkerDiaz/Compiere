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
/** Generated Model for M_Transaction
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_Transaction.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_Transaction extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_Transaction_ID id
    @param trx transaction
    */
    public X_M_Transaction (Ctx ctx, int M_Transaction_ID, Trx trx)
    {
        super (ctx, M_Transaction_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Transaction_ID == 0)
        {
            setM_AttributeSetInstance_ID (0);
            setM_Locator_ID (0);
            setM_Product_ID (0);
            setM_Transaction_ID (0);
            setMovementDate (new Timestamp(System.currentTimeMillis()));
            setMovementQty (Env.ZERO);
            setMovementType (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_Transaction (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=329 */
    public static final int Table_ID=329;
    
    /** TableName=M_Transaction */
    public static final String Table_Name="M_Transaction";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Project Issue.
    @param C_ProjectIssue_ID Project Issues (Material, Labor) */
    public void setC_ProjectIssue_ID (int C_ProjectIssue_ID)
    {
        if (C_ProjectIssue_ID <= 0) set_ValueNoCheck ("C_ProjectIssue_ID", null);
        else
        set_ValueNoCheck ("C_ProjectIssue_ID", Integer.valueOf(C_ProjectIssue_ID));
        
    }
    
    /** Get Project Issue.
    @return Project Issues (Material, Labor) */
    public int getC_ProjectIssue_ID() 
    {
        return get_ValueAsInt("C_ProjectIssue_ID");
        
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
        if (M_InventoryLine_ID <= 0) set_ValueNoCheck ("M_InventoryLine_ID", null);
        else
        set_ValueNoCheck ("M_InventoryLine_ID", Integer.valueOf(M_InventoryLine_ID));
        
    }
    
    /** Get Phys Inventory Line.
    @return Unique line in an Inventory document */
    public int getM_InventoryLine_ID() 
    {
        return get_ValueAsInt("M_InventoryLine_ID");
        
    }
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID < 1) throw new IllegalArgumentException ("M_Locator_ID is mandatory.");
        set_ValueNoCheck ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
    }
    
    /** Set Move Line.
    @param M_MovementLine_ID Inventory Move document Line */
    public void setM_MovementLine_ID (int M_MovementLine_ID)
    {
        if (M_MovementLine_ID <= 0) set_ValueNoCheck ("M_MovementLine_ID", null);
        else
        set_ValueNoCheck ("M_MovementLine_ID", Integer.valueOf(M_MovementLine_ID));
        
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
        if (M_ProductionLine_ID <= 0) set_ValueNoCheck ("M_ProductionLine_ID", null);
        else
        set_ValueNoCheck ("M_ProductionLine_ID", Integer.valueOf(M_ProductionLine_ID));
        
    }
    
    /** Get Production Line.
    @return Document Line representing a production */
    public int getM_ProductionLine_ID() 
    {
        return get_ValueAsInt("M_ProductionLine_ID");
        
    }
    
    /** Set Inventory Transaction.
    @param M_Transaction_ID Inventory Transaction */
    public void setM_Transaction_ID (int M_Transaction_ID)
    {
        if (M_Transaction_ID < 1) throw new IllegalArgumentException ("M_Transaction_ID is mandatory.");
        set_ValueNoCheck ("M_Transaction_ID", Integer.valueOf(M_Transaction_ID));
        
    }
    
    /** Get Inventory Transaction.
    @return Inventory Transaction */
    public int getM_Transaction_ID() 
    {
        return get_ValueAsInt("M_Transaction_ID");
        
    }
    
    /** Set Warehouse Task.
    @param M_WarehouseTask_ID A Warehouse Task represents a basic warehouse operation such as putaway, picking or replenishment. */
    public void setM_WarehouseTask_ID (int M_WarehouseTask_ID)
    {
        if (M_WarehouseTask_ID <= 0) set_ValueNoCheck ("M_WarehouseTask_ID", null);
        else
        set_ValueNoCheck ("M_WarehouseTask_ID", Integer.valueOf(M_WarehouseTask_ID));
        
    }
    
    /** Get Warehouse Task.
    @return A Warehouse Task represents a basic warehouse operation such as putaway, picking or replenishment. */
    public int getM_WarehouseTask_ID() 
    {
        return get_ValueAsInt("M_WarehouseTask_ID");
        
    }
    
    /** Set Work Order Transaction Line.
    @param M_WorkOrderTransactionLine_ID Work Order Transaction Line */
    public void setM_WorkOrderTransactionLine_ID (int M_WorkOrderTransactionLine_ID)
    {
        if (M_WorkOrderTransactionLine_ID <= 0) set_ValueNoCheck ("M_WorkOrderTransactionLine_ID", null);
        else
        set_ValueNoCheck ("M_WorkOrderTransactionLine_ID", Integer.valueOf(M_WorkOrderTransactionLine_ID));
        
    }
    
    /** Get Work Order Transaction Line.
    @return Work Order Transaction Line */
    public int getM_WorkOrderTransactionLine_ID() 
    {
        return get_ValueAsInt("M_WorkOrderTransactionLine_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getMovementDate()));
        
    }
    
    /** Set Movement Quantity.
    @param MovementQty Quantity of a product moved. */
    public void setMovementQty (java.math.BigDecimal MovementQty)
    {
        if (MovementQty == null) throw new IllegalArgumentException ("MovementQty is mandatory.");
        set_ValueNoCheck ("MovementQty", MovementQty);
        
    }
    
    /** Get Movement Quantity.
    @return Quantity of a product moved. */
    public java.math.BigDecimal getMovementQty() 
    {
        return get_ValueAsBigDecimal("MovementQty");
        
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
    
    
}
