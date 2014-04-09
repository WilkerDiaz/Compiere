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
/** Generated Model for T_Transaction
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_Transaction.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_Transaction extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_Transaction_ID id
    @param trx transaction
    */
    public X_T_Transaction (Ctx ctx, int T_Transaction_ID, Trx trx)
    {
        super (ctx, T_Transaction_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_Transaction_ID == 0)
        {
            setAD_PInstance_ID (0);
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
    public X_T_Transaction (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511671716789L;
    /** Last Updated Timestamp 2008-12-17 12:40:00.0 */
    public static final long updatedMS = 1229546400000L;
    /** AD_Table_ID=758 */
    public static final int Table_ID=758;
    
    /** TableName=T_Transaction */
    public static final String Table_Name="T_Transaction";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Instance.
    @param AD_PInstance_ID Instance of the process */
    public void setAD_PInstance_ID (int AD_PInstance_ID)
    {
        if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
        set_ValueNoCheck ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_PInstance_ID()));
        
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
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID < 0) throw new IllegalArgumentException ("M_AttributeSetInstance_ID is mandatory.");
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
        if (M_InOutLine_ID <= 0) set_Value ("M_InOutLine_ID", null);
        else
        set_Value ("M_InOutLine_ID", Integer.valueOf(M_InOutLine_ID));
        
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
        if (M_InOut_ID <= 0) set_Value ("M_InOut_ID", null);
        else
        set_Value ("M_InOut_ID", Integer.valueOf(M_InOut_ID));
        
    }
    
    /** Get Shipment/Receipt.
    @return Material Shipment Document */
    public int getM_InOut_ID() 
    {
        return get_ValueAsInt("M_InOut_ID");
        
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
    
    /** Set Inventory Move.
    @param M_Movement_ID Movement of Inventory */
    public void setM_Movement_ID (int M_Movement_ID)
    {
        if (M_Movement_ID <= 0) set_Value ("M_Movement_ID", null);
        else
        set_Value ("M_Movement_ID", Integer.valueOf(M_Movement_ID));
        
    }
    
    /** Get Inventory Move.
    @return Movement of Inventory */
    public int getM_Movement_ID() 
    {
        return get_ValueAsInt("M_Movement_ID");
        
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
    
    /** Set Production.
    @param M_Production_ID Plan for producing a product */
    public void setM_Production_ID (int M_Production_ID)
    {
        if (M_Production_ID <= 0) set_Value ("M_Production_ID", null);
        else
        set_Value ("M_Production_ID", Integer.valueOf(M_Production_ID));
        
    }
    
    /** Get Production.
    @return Plan for producing a product */
    public int getM_Production_ID() 
    {
        return get_ValueAsInt("M_Production_ID");
        
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
        set_Value ("MovementType", MovementType);
        
    }
    
    /** Get Movement Type.
    @return Method of moving the inventory */
    public String getMovementType() 
    {
        return (String)get_Value("MovementType");
        
    }
    
    /** Set Search Shipment/Receipt.
    @param Search_InOut_ID Material Shipment Document */
    public void setSearch_InOut_ID (int Search_InOut_ID)
    {
        if (Search_InOut_ID <= 0) set_Value ("Search_InOut_ID", null);
        else
        set_Value ("Search_InOut_ID", Integer.valueOf(Search_InOut_ID));
        
    }
    
    /** Get Search Shipment/Receipt.
    @return Material Shipment Document */
    public int getSearch_InOut_ID() 
    {
        return get_ValueAsInt("Search_InOut_ID");
        
    }
    
    /** Set Search Invoice.
    @param Search_Invoice_ID Search Invoice Identifier */
    public void setSearch_Invoice_ID (int Search_Invoice_ID)
    {
        if (Search_Invoice_ID <= 0) set_Value ("Search_Invoice_ID", null);
        else
        set_Value ("Search_Invoice_ID", Integer.valueOf(Search_Invoice_ID));
        
    }
    
    /** Get Search Invoice.
    @return Search Invoice Identifier */
    public int getSearch_Invoice_ID() 
    {
        return get_ValueAsInt("Search_Invoice_ID");
        
    }
    
    /** Set Search Order.
    @param Search_Order_ID Order Identifier */
    public void setSearch_Order_ID (int Search_Order_ID)
    {
        if (Search_Order_ID <= 0) set_Value ("Search_Order_ID", null);
        else
        set_Value ("Search_Order_ID", Integer.valueOf(Search_Order_ID));
        
    }
    
    /** Get Search Order.
    @return Order Identifier */
    public int getSearch_Order_ID() 
    {
        return get_ValueAsInt("Search_Order_ID");
        
    }
    
    
}
