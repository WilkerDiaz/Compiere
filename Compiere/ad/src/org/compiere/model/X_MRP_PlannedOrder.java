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
/** Generated Model for MRP_PlannedOrder
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_MRP_PlannedOrder.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_MRP_PlannedOrder extends PO
{
    /** Standard Constructor
    @param ctx context
    @param MRP_PlannedOrder_ID id
    @param trx transaction
    */
    public X_MRP_PlannedOrder (Ctx ctx, int MRP_PlannedOrder_ID, Trx trx)
    {
        super (ctx, MRP_PlannedOrder_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (MRP_PlannedOrder_ID == 0)
        {
            setDateOrdered (new Timestamp(System.currentTimeMillis()));
            setLevelNo (0);	// 0
            setMRP_PlanRun_ID (0);
            setMRP_PlannedDemand_ID (0);
            setMRP_PlannedOrder_ID (0);
            setM_Product_ID (0);
            setOrderType (null);	// P
            setPlannedOrderStatus (null);	// N
            setQtyOrdered (Env.ZERO);	// 0
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_MRP_PlannedOrder (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27532675665789L;
    /** Last Updated Timestamp 2009-08-17 16:05:49.0 */
    public static final long updatedMS = 1250550349000L;
    /** AD_Table_ID=2111 */
    public static final int Table_ID=2111;
    
    /** TableName=MRP_PlannedOrder */
    public static final String Table_Name="MRP_PlannedOrder";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID <= 0) set_Value ("C_DocType_ID", null);
        else
        set_Value ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
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
    
    /** Set Date Ordered.
    @param DateOrdered Date of Order */
    public void setDateOrdered (Timestamp DateOrdered)
    {
        if (DateOrdered == null) throw new IllegalArgumentException ("DateOrdered is mandatory.");
        set_ValueNoCheck ("DateOrdered", DateOrdered);
        
    }
    
    /** Get Date Ordered.
    @return Date of Order */
    public Timestamp getDateOrdered() 
    {
        return (Timestamp)get_Value("DateOrdered");
        
    }
    
    /** Set Level no.
    @param LevelNo Level Number */
    public void setLevelNo (int LevelNo)
    {
        set_ValueNoCheck ("LevelNo", Integer.valueOf(LevelNo));
        
    }
    
    /** Get Level no.
    @return Level Number */
    public int getLevelNo() 
    {
        return get_ValueAsInt("LevelNo");
        
    }
    
    /** Set Plan Run.
    @param MRP_PlanRun_ID An execution of the plan */
    public void setMRP_PlanRun_ID (int MRP_PlanRun_ID)
    {
        if (MRP_PlanRun_ID < 1) throw new IllegalArgumentException ("MRP_PlanRun_ID is mandatory.");
        set_ValueNoCheck ("MRP_PlanRun_ID", Integer.valueOf(MRP_PlanRun_ID));
        
    }
    
    /** Get Plan Run.
    @return An execution of the plan */
    public int getMRP_PlanRun_ID() 
    {
        return get_ValueAsInt("MRP_PlanRun_ID");
        
    }
    
    /** Set Planned Demand.
    @param MRP_PlannedDemand_ID Exploded master demand calculated by the Plan Run */
    public void setMRP_PlannedDemand_ID (int MRP_PlannedDemand_ID)
    {
        if (MRP_PlannedDemand_ID < 1) throw new IllegalArgumentException ("MRP_PlannedDemand_ID is mandatory.");
        set_ValueNoCheck ("MRP_PlannedDemand_ID", Integer.valueOf(MRP_PlannedDemand_ID));
        
    }
    
    /** Get Planned Demand.
    @return Exploded master demand calculated by the Plan Run */
    public int getMRP_PlannedDemand_ID() 
    {
        return get_ValueAsInt("MRP_PlannedDemand_ID");
        
    }
    
    /** Set Planned Order.
    @param MRP_PlannedOrder_ID Recommended orders calculated by the plan engine */
    public void setMRP_PlannedOrder_ID (int MRP_PlannedOrder_ID)
    {
        if (MRP_PlannedOrder_ID < 1) throw new IllegalArgumentException ("MRP_PlannedOrder_ID is mandatory.");
        set_ValueNoCheck ("MRP_PlannedOrder_ID", Integer.valueOf(MRP_PlannedOrder_ID));
        
    }
    
    /** Get Planned Order.
    @return Recommended orders calculated by the plan engine */
    public int getMRP_PlannedOrder_ID() 
    {
        return get_ValueAsInt("MRP_PlannedOrder_ID");
        
    }
    
    /** Set Planned Order Parent.
    @param MRP_PlannedOrder_Parent_ID Parent Order of a Planned Order */
    public void setMRP_PlannedOrder_Parent_ID (int MRP_PlannedOrder_Parent_ID)
    {
        if (MRP_PlannedOrder_Parent_ID <= 0) set_ValueNoCheck ("MRP_PlannedOrder_Parent_ID", null);
        else
        set_ValueNoCheck ("MRP_PlannedOrder_Parent_ID", Integer.valueOf(MRP_PlannedOrder_Parent_ID));
        
    }
    
    /** Get Planned Order Parent.
    @return Parent Order of a Planned Order */
    public int getMRP_PlannedOrder_Parent_ID() 
    {
        return get_ValueAsInt("MRP_PlannedOrder_Parent_ID");
        
    }
    
    /** Set Planned Order Root.
    @param MRP_PlannedOrder_Root_ID Root Order of a Planned Order */
    public void setMRP_PlannedOrder_Root_ID (int MRP_PlannedOrder_Root_ID)
    {
        if (MRP_PlannedOrder_Root_ID <= 0) set_ValueNoCheck ("MRP_PlannedOrder_Root_ID", null);
        else
        set_ValueNoCheck ("MRP_PlannedOrder_Root_ID", Integer.valueOf(MRP_PlannedOrder_Root_ID));
        
    }
    
    /** Get Planned Order Root.
    @return Root Order of a Planned Order */
    public int getMRP_PlannedOrder_Root_ID() 
    {
        return get_ValueAsInt("MRP_PlannedOrder_Root_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_Product_ID()));
        
    }
    
    /** Set Work Order.
    @param M_WorkOrder_ID Work Order */
    public void setM_WorkOrder_ID (int M_WorkOrder_ID)
    {
        if (M_WorkOrder_ID <= 0) set_Value ("M_WorkOrder_ID", null);
        else
        set_Value ("M_WorkOrder_ID", Integer.valueOf(M_WorkOrder_ID));
        
    }
    
    /** Get Work Order.
    @return Work Order */
    public int getM_WorkOrder_ID() 
    {
        return get_ValueAsInt("M_WorkOrder_ID");
        
    }
    
    /** Purchase Order = P */
    public static final String ORDERTYPE_PurchaseOrder = X_Ref_MRP_PlannedOrder_OrderType.PURCHASE_ORDER.getValue();
    /** Work Order = W */
    public static final String ORDERTYPE_WorkOrder = X_Ref_MRP_PlannedOrder_OrderType.WORK_ORDER.getValue();
    /** Set Order Type.
    @param OrderType Document Base Type for Sales Orders */
    public void setOrderType (String OrderType)
    {
        if (OrderType == null) throw new IllegalArgumentException ("OrderType is mandatory");
        if (!X_Ref_MRP_PlannedOrder_OrderType.isValid(OrderType))
        throw new IllegalArgumentException ("OrderType Invalid value - " + OrderType + " - Reference_ID=508 - P - W");
        set_ValueNoCheck ("OrderType", OrderType);
        
    }
    
    /** Get Order Type.
    @return Document Base Type for Sales Orders */
    public String getOrderType() 
    {
        return (String)get_Value("OrderType");
        
    }
    
    /** Error  = E */
    public static final String PLANNEDORDERSTATUS_Error = X_Ref_MRP_PlannedOrder_Status.ERROR.getValue();
    /** Implemented = I */
    public static final String PLANNEDORDERSTATUS_Implemented = X_Ref_MRP_PlannedOrder_Status.IMPLEMENTED.getValue();
    /** Not Implemented = N */
    public static final String PLANNEDORDERSTATUS_NotImplemented = X_Ref_MRP_PlannedOrder_Status.NOT_IMPLEMENTED.getValue();
    /** Set Planned Order Status.
    @param PlannedOrderStatus Indicates if an order has been submitted or not */
    public void setPlannedOrderStatus (String PlannedOrderStatus)
    {
        if (PlannedOrderStatus == null) throw new IllegalArgumentException ("PlannedOrderStatus is mandatory");
        if (!X_Ref_MRP_PlannedOrder_Status.isValid(PlannedOrderStatus))
        throw new IllegalArgumentException ("PlannedOrderStatus Invalid value - " + PlannedOrderStatus + " - Reference_ID=509 - E - I - N");
        set_Value ("PlannedOrderStatus", PlannedOrderStatus);
        
    }
    
    /** Get Planned Order Status.
    @return Indicates if an order has been submitted or not */
    public String getPlannedOrderStatus() 
    {
        return (String)get_Value("PlannedOrderStatus");
        
    }
    
    /** Set Quantity Ordered.
    @param QtyOrdered Ordered Quantity */
    public void setQtyOrdered (java.math.BigDecimal QtyOrdered)
    {
        if (QtyOrdered == null) throw new IllegalArgumentException ("QtyOrdered is mandatory.");
        set_Value ("QtyOrdered", QtyOrdered);
        
    }
    
    /** Get Quantity Ordered.
    @return Ordered Quantity */
    public java.math.BigDecimal getQtyOrdered() 
    {
        return get_ValueAsBigDecimal("QtyOrdered");
        
    }
    
    
}
