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
/** Generated Model for M_WorkCenterCostDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_WorkCenterCostDetail.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_WorkCenterCostDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WorkCenterCostDetail_ID id
    @param trx transaction
    */
    public X_M_WorkCenterCostDetail (Ctx ctx, int M_WorkCenterCostDetail_ID, Trx trx)
    {
        super (ctx, M_WorkCenterCostDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WorkCenterCostDetail_ID == 0)
        {
            setAmt (Env.ZERO);
            setC_AcctSchema_ID (0);
            setM_CostElement_ID (0);
            setM_WorkCenterCostDetail_ID (0);
            setM_WorkCenter_ID (0);
            setProcessed (true);	// Y
            setQty (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WorkCenterCostDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520619335789L;
    /** Last Updated Timestamp 2009-03-31 02:06:59.0 */
    public static final long updatedMS = 1238494019000L;
    /** AD_Table_ID=2120 */
    public static final int Table_ID=2120;
    
    /** TableName=M_WorkCenterCostDetail */
    public static final String Table_Name="M_WorkCenterCostDetail";
    
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
        set_Value ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
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
    
    /** Set Cost Element.
    @param M_CostElement_ID Product Cost Element */
    public void setM_CostElement_ID (int M_CostElement_ID)
    {
        if (M_CostElement_ID < 1) throw new IllegalArgumentException ("M_CostElement_ID is mandatory.");
        set_Value ("M_CostElement_ID", Integer.valueOf(M_CostElement_ID));
        
    }
    
    /** Get Cost Element.
    @return Product Cost Element */
    public int getM_CostElement_ID() 
    {
        return get_ValueAsInt("M_CostElement_ID");
        
    }
    
    /** Set M_WorkCenterCostDetail_ID.
    @param M_WorkCenterCostDetail_ID M_WorkCenterCostDetail_ID */
    public void setM_WorkCenterCostDetail_ID (int M_WorkCenterCostDetail_ID)
    {
        if (M_WorkCenterCostDetail_ID < 1) throw new IllegalArgumentException ("M_WorkCenterCostDetail_ID is mandatory.");
        set_ValueNoCheck ("M_WorkCenterCostDetail_ID", Integer.valueOf(M_WorkCenterCostDetail_ID));
        
    }
    
    /** Get M_WorkCenterCostDetail_ID.
    @return M_WorkCenterCostDetail_ID */
    public int getM_WorkCenterCostDetail_ID() 
    {
        return get_ValueAsInt("M_WorkCenterCostDetail_ID");
        
    }
    
    /** Set Work Center.
    @param M_WorkCenter_ID Identifies a production area within a warehouse consisting of people and equipment */
    public void setM_WorkCenter_ID (int M_WorkCenter_ID)
    {
        if (M_WorkCenter_ID < 1) throw new IllegalArgumentException ("M_WorkCenter_ID is mandatory.");
        set_Value ("M_WorkCenter_ID", Integer.valueOf(M_WorkCenter_ID));
        
    }
    
    /** Get Work Center.
    @return Identifies a production area within a warehouse consisting of people and equipment */
    public int getM_WorkCenter_ID() 
    {
        return get_ValueAsInt("M_WorkCenter_ID");
        
    }
    
    /** Set Work Order Transaction.
    @param M_WorkOrderTransaction_ID Work Order Transaction */
    public void setM_WorkOrderTransaction_ID (int M_WorkOrderTransaction_ID)
    {
        if (M_WorkOrderTransaction_ID <= 0) set_Value ("M_WorkOrderTransaction_ID", null);
        else
        set_Value ("M_WorkOrderTransaction_ID", Integer.valueOf(M_WorkOrderTransaction_ID));
        
    }
    
    /** Get Work Order Transaction.
    @return Work Order Transaction */
    public int getM_WorkOrderTransaction_ID() 
    {
        return get_ValueAsInt("M_WorkOrderTransaction_ID");
        
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
