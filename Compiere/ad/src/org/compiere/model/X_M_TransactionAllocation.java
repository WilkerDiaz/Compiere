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
/** Generated Model for M_TransactionAllocation
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_TransactionAllocation.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_TransactionAllocation extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_TransactionAllocation_ID id
    @param trx transaction
    */
    public X_M_TransactionAllocation (Ctx ctx, int M_TransactionAllocation_ID, Trx trx)
    {
        super (ctx, M_TransactionAllocation_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_TransactionAllocation_ID == 0)
        {
            setAllocationStrategyType (null);
            setIsAllocated (false);	// N
            setIsManual (false);	// N
            setM_AttributeSetInstance_ID (0);
            setM_Product_ID (0);
            setM_Transaction_ID (0);
            setQty (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_TransactionAllocation (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=636 */
    public static final int Table_ID=636;
    
    /** TableName=M_TransactionAllocation */
    public static final String Table_Name="M_TransactionAllocation";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** FiFo = F */
    public static final String ALLOCATIONSTRATEGYTYPE_FiFo = X_Ref_M_TransactionAllocation_Type.FI_FO.getValue();
    /** LiFo = L */
    public static final String ALLOCATIONSTRATEGYTYPE_LiFo = X_Ref_M_TransactionAllocation_Type.LI_FO.getValue();
    /** Set Allocation Strategy.
    @param AllocationStrategyType Allocation Strategy */
    public void setAllocationStrategyType (String AllocationStrategyType)
    {
        if (AllocationStrategyType == null) throw new IllegalArgumentException ("AllocationStrategyType is mandatory");
        if (!X_Ref_M_TransactionAllocation_Type.isValid(AllocationStrategyType))
        throw new IllegalArgumentException ("AllocationStrategyType Invalid value - " + AllocationStrategyType + " - Reference_ID=294 - F - L");
        set_ValueNoCheck ("AllocationStrategyType", AllocationStrategyType);
        
    }
    
    /** Get Allocation Strategy.
    @return Allocation Strategy */
    public String getAllocationStrategyType() 
    {
        return (String)get_Value("AllocationStrategyType");
        
    }
    
    /** Set Allocated.
    @param IsAllocated Indicates if the payment has been allocated */
    public void setIsAllocated (boolean IsAllocated)
    {
        set_Value ("IsAllocated", Boolean.valueOf(IsAllocated));
        
    }
    
    /** Get Allocated.
    @return Indicates if the payment has been allocated */
    public boolean isAllocated() 
    {
        return get_ValueAsBoolean("IsAllocated");
        
    }
    
    /** Set Manual.
    @param IsManual This is a manual process or entry */
    public void setIsManual (boolean IsManual)
    {
        set_Value ("IsManual", Boolean.valueOf(IsManual));
        
    }
    
    /** Get Manual.
    @return This is a manual process or entry */
    public boolean isManual() 
    {
        return get_ValueAsBoolean("IsManual");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_Transaction_ID()));
        
    }
    
    /** Set Out Shipment Line.
    @param Out_M_InOutLine_ID Outgoing Shipment/Receipt */
    public void setOut_M_InOutLine_ID (int Out_M_InOutLine_ID)
    {
        if (Out_M_InOutLine_ID <= 0) set_Value ("Out_M_InOutLine_ID", null);
        else
        set_Value ("Out_M_InOutLine_ID", Integer.valueOf(Out_M_InOutLine_ID));
        
    }
    
    /** Get Out Shipment Line.
    @return Outgoing Shipment/Receipt */
    public int getOut_M_InOutLine_ID() 
    {
        return get_ValueAsInt("Out_M_InOutLine_ID");
        
    }
    
    /** Set Out Inventory Line.
    @param Out_M_InventoryLine_ID Outgoing Inventory Line */
    public void setOut_M_InventoryLine_ID (int Out_M_InventoryLine_ID)
    {
        if (Out_M_InventoryLine_ID <= 0) set_Value ("Out_M_InventoryLine_ID", null);
        else
        set_Value ("Out_M_InventoryLine_ID", Integer.valueOf(Out_M_InventoryLine_ID));
        
    }
    
    /** Get Out Inventory Line.
    @return Outgoing Inventory Line */
    public int getOut_M_InventoryLine_ID() 
    {
        return get_ValueAsInt("Out_M_InventoryLine_ID");
        
    }
    
    /** Set Out Production Line.
    @param Out_M_ProductionLine_ID Outgoing Production Line */
    public void setOut_M_ProductionLine_ID (int Out_M_ProductionLine_ID)
    {
        if (Out_M_ProductionLine_ID <= 0) set_Value ("Out_M_ProductionLine_ID", null);
        else
        set_Value ("Out_M_ProductionLine_ID", Integer.valueOf(Out_M_ProductionLine_ID));
        
    }
    
    /** Get Out Production Line.
    @return Outgoing Production Line */
    public int getOut_M_ProductionLine_ID() 
    {
        return get_ValueAsInt("Out_M_ProductionLine_ID");
        
    }
    
    /** Set Out Transaction.
    @param Out_M_Transaction_ID Outgoing Transaction */
    public void setOut_M_Transaction_ID (int Out_M_Transaction_ID)
    {
        if (Out_M_Transaction_ID <= 0) set_Value ("Out_M_Transaction_ID", null);
        else
        set_Value ("Out_M_Transaction_ID", Integer.valueOf(Out_M_Transaction_ID));
        
    }
    
    /** Get Out Transaction.
    @return Outgoing Transaction */
    public int getOut_M_Transaction_ID() 
    {
        return get_ValueAsInt("Out_M_Transaction_ID");
        
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
