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
/** Generated Model for M_WorkCenterCost
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_WorkCenterCost.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_WorkCenterCost extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WorkCenterCost_ID id
    @param trx transaction
    */
    public X_M_WorkCenterCost (Ctx ctx, int M_WorkCenterCost_ID, Trx trx)
    {
        super (ctx, M_WorkCenterCost_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WorkCenterCost_ID == 0)
        {
            setBasisType (null);	// I
            setC_AcctSchema_ID (0);
            setCurrentCostPrice (Env.ZERO);
            setM_CostElement_ID (0);
            setM_CostType_ID (0);
            setM_WorkCenter_ID (0);
            setOverhead_Absorption_Acct (0);	// @SQL=SELECT  OverHead_Absorption_Acct AS DefaultValue FROM C_AcctSchema_Default WHERE C_AcctSchema_ID = @C_AcctSchema_ID@ AND AD_Client_ID = @#AD_Client_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WorkCenterCost (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27521165843789L;
    /** Last Updated Timestamp 2009-04-06 09:55:27.0 */
    public static final long updatedMS = 1239040527000L;
    /** AD_Table_ID=2096 */
    public static final int Table_ID=2096;
    
    /** TableName=M_WorkCenterCost */
    public static final String Table_Name="M_WorkCenterCost";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Per Batch = B */
    public static final String BASISTYPE_PerBatch = X_Ref_M_Product_Basis_Type.PER_BATCH.getValue();
    /** Per Item = I */
    public static final String BASISTYPE_PerItem = X_Ref_M_Product_Basis_Type.PER_ITEM.getValue();
    /** Set Cost Basis Type.
    @param BasisType Indicates the option to consume and charge materials and resources */
    public void setBasisType (String BasisType)
    {
        if (BasisType == null) throw new IllegalArgumentException ("BasisType is mandatory");
        if (!X_Ref_M_Product_Basis_Type.isValid(BasisType))
        throw new IllegalArgumentException ("BasisType Invalid value - " + BasisType + " - Reference_ID=496 - B - I");
        set_Value ("BasisType", BasisType);
        
    }
    
    /** Get Cost Basis Type.
    @return Indicates the option to consume and charge materials and resources */
    public String getBasisType() 
    {
        return (String)get_Value("BasisType");
        
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
    
    /** Average PO = A */
    public static final String COSTINGMETHOD_AveragePO = X_Ref_C_AcctSchema_Costing_Method.AVERAGE_PO.getValue();
    /** FiFo = F */
    public static final String COSTINGMETHOD_FiFo = X_Ref_C_AcctSchema_Costing_Method.FI_FO.getValue();
    /** Average Invoice = I */
    public static final String COSTINGMETHOD_AverageInvoice = X_Ref_C_AcctSchema_Costing_Method.AVERAGE_INVOICE.getValue();
    /** LiFo = L */
    public static final String COSTINGMETHOD_LiFo = X_Ref_C_AcctSchema_Costing_Method.LI_FO.getValue();
    /** Standard Costing = S */
    public static final String COSTINGMETHOD_StandardCosting = X_Ref_C_AcctSchema_Costing_Method.STANDARD_COSTING.getValue();
    /** User Defined = U */
    public static final String COSTINGMETHOD_UserDefined = X_Ref_C_AcctSchema_Costing_Method.USER_DEFINED.getValue();
    /** Last Invoice = i */
    public static final String COSTINGMETHOD_LastInvoice = X_Ref_C_AcctSchema_Costing_Method.LAST_INVOICE.getValue();
    /** Last PO Price = p */
    public static final String COSTINGMETHOD_LastPOPrice = X_Ref_C_AcctSchema_Costing_Method.LAST_PO_PRICE.getValue();
    /** _ = x */
    public static final String COSTINGMETHOD__ = X_Ref_C_AcctSchema_Costing_Method._.getValue();
    /** Set Costing Method.
    @param CostingMethod Indicates how Costs will be calculated */
    public void setCostingMethod (String CostingMethod)
    {
        if (!X_Ref_C_AcctSchema_Costing_Method.isValid(CostingMethod))
        throw new IllegalArgumentException ("CostingMethod Invalid value - " + CostingMethod + " - Reference_ID=122 - A - F - I - L - S - U - i - p - x");
        throw new IllegalArgumentException ("CostingMethod is virtual column");
        
    }
    
    /** Get Costing Method.
    @return Indicates how Costs will be calculated */
    public String getCostingMethod() 
    {
        return (String)get_Value("CostingMethod");
        
    }
    
    /** Set Current Cost.
    @param CurrentCostPrice The currently used cost */
    public void setCurrentCostPrice (java.math.BigDecimal CurrentCostPrice)
    {
        if (CurrentCostPrice == null) throw new IllegalArgumentException ("CurrentCostPrice is mandatory.");
        set_Value ("CurrentCostPrice", CurrentCostPrice);
        
    }
    
    /** Get Current Cost.
    @return The currently used cost */
    public java.math.BigDecimal getCurrentCostPrice() 
    {
        return get_ValueAsBigDecimal("CurrentCostPrice");
        
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
    
    /** Set Future Cost.
    @param FutureCostPrice Future Cost */
    public void setFutureCostPrice (java.math.BigDecimal FutureCostPrice)
    {
        set_Value ("FutureCostPrice", FutureCostPrice);
        
    }
    
    /** Get Future Cost.
    @return Future Cost */
    public java.math.BigDecimal getFutureCostPrice() 
    {
        return get_ValueAsBigDecimal("FutureCostPrice");
        
    }
    
    /** Set Cost Element.
    @param M_CostElement_ID Product Cost Element */
    public void setM_CostElement_ID (int M_CostElement_ID)
    {
        if (M_CostElement_ID < 1) throw new IllegalArgumentException ("M_CostElement_ID is mandatory.");
        set_ValueNoCheck ("M_CostElement_ID", Integer.valueOf(M_CostElement_ID));
        
    }
    
    /** Get Cost Element.
    @return Product Cost Element */
    public int getM_CostElement_ID() 
    {
        return get_ValueAsInt("M_CostElement_ID");
        
    }
    
    /** Set Cost Type.
    @param M_CostType_ID Type of Cost (e.g. Current, Plan, Future) */
    public void setM_CostType_ID (int M_CostType_ID)
    {
        if (M_CostType_ID < 1) throw new IllegalArgumentException ("M_CostType_ID is mandatory.");
        set_ValueNoCheck ("M_CostType_ID", Integer.valueOf(M_CostType_ID));
        
    }
    
    /** Get Cost Type.
    @return Type of Cost (e.g. Current, Plan, Future) */
    public int getM_CostType_ID() 
    {
        return get_ValueAsInt("M_CostType_ID");
        
    }
    
    /** Set Work Center.
    @param M_WorkCenter_ID Identifies a production area within a warehouse consisting of people and equipment */
    public void setM_WorkCenter_ID (int M_WorkCenter_ID)
    {
        if (M_WorkCenter_ID < 1) throw new IllegalArgumentException ("M_WorkCenter_ID is mandatory.");
        set_ValueNoCheck ("M_WorkCenter_ID", Integer.valueOf(M_WorkCenter_ID));
        
    }
    
    /** Get Work Center.
    @return Identifies a production area within a warehouse consisting of people and equipment */
    public int getM_WorkCenter_ID() 
    {
        return get_ValueAsInt("M_WorkCenter_ID");
        
    }
    
    /** Set Overhead Absorption.
    @param Overhead_Absorption_Acct Overhead Absorption Account */
    public void setOverhead_Absorption_Acct (int Overhead_Absorption_Acct)
    {
        set_Value ("Overhead_Absorption_Acct", Integer.valueOf(Overhead_Absorption_Acct));
        
    }
    
    /** Get Overhead Absorption.
    @return Overhead Absorption Account */
    public int getOverhead_Absorption_Acct() 
    {
        return get_ValueAsInt("Overhead_Absorption_Acct");
        
    }
    
    
}
