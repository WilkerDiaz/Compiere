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
/** Generated Model for M_Product_Category_Acct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_Product_Category_Acct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_Product_Category_Acct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_Product_Category_Acct_ID id
    @param trx transaction
    */
    public X_M_Product_Category_Acct (Ctx ctx, int M_Product_Category_Acct_ID, Trx trx)
    {
        super (ctx, M_Product_Category_Acct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Product_Category_Acct_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setM_Product_Category_ID (0);
            setP_Asset_Acct (0);
            setP_COGS_Acct (0);
            setP_CostAdjustment_Acct (0);
            setP_Expense_Acct (0);
            setP_InventoryClearing_Acct (0);
            setP_InvoicePriceVariance_Acct (0);
            setP_PurchasePriceVariance_Acct (0);
            setP_Revenue_Acct (0);
            setP_TradeDiscountGrant_Acct (0);
            setP_TradeDiscountRec_Acct (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_Product_Category_Acct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518293210789L;
    /** Last Updated Timestamp 2009-03-04 03:58:14.0 */
    public static final long updatedMS = 1236167894000L;
    /** AD_Table_ID=401 */
    public static final int Table_ID=401;
    
    /** TableName=M_Product_Category_Acct */
    public static final String Table_Name="M_Product_Category_Acct";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_AcctSchema_ID()));
        
    }
    
    /** Batch/Lot = B */
    public static final String COSTINGLEVEL_BatchLot = X_Ref_C_AcctSchema_CostingLevel.BATCH_LOT.getValue();
    /** Tenant = C */
    public static final String COSTINGLEVEL_Tenant = X_Ref_C_AcctSchema_CostingLevel.TENANT.getValue();
    /** Organization = O */
    public static final String COSTINGLEVEL_Organization = X_Ref_C_AcctSchema_CostingLevel.ORGANIZATION.getValue();
    /** Set Costing Level.
    @param CostingLevel The lowest level to accumulate Costing Information */
    public void setCostingLevel (String CostingLevel)
    {
        if (!X_Ref_C_AcctSchema_CostingLevel.isValid(CostingLevel))
        throw new IllegalArgumentException ("CostingLevel Invalid value - " + CostingLevel + " - Reference_ID=355 - B - C - O");
        set_Value ("CostingLevel", CostingLevel);
        
    }
    
    /** Get Costing Level.
    @return The lowest level to accumulate Costing Information */
    public String getCostingLevel() 
    {
        return (String)get_Value("CostingLevel");
        
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
        set_Value ("CostingMethod", CostingMethod);
        
    }
    
    /** Get Costing Method.
    @return Indicates how Costs will be calculated */
    public String getCostingMethod() 
    {
        return (String)get_Value("CostingMethod");
        
    }
    
    /** Set Product Category.
    @param M_Product_Category_ID Category of a Product */
    public void setM_Product_Category_ID (int M_Product_Category_ID)
    {
        if (M_Product_Category_ID < 1) throw new IllegalArgumentException ("M_Product_Category_ID is mandatory.");
        set_ValueNoCheck ("M_Product_Category_ID", Integer.valueOf(M_Product_Category_ID));
        
    }
    
    /** Get Product Category.
    @return Category of a Product */
    public int getM_Product_Category_ID() 
    {
        return get_ValueAsInt("M_Product_Category_ID");
        
    }
    
    /** Set Product Asset.
    @param P_Asset_Acct Account for Product Asset (Inventory) */
    public void setP_Asset_Acct (int P_Asset_Acct)
    {
        set_Value ("P_Asset_Acct", Integer.valueOf(P_Asset_Acct));
        
    }
    
    /** Get Product Asset.
    @return Account for Product Asset (Inventory) */
    public int getP_Asset_Acct() 
    {
        return get_ValueAsInt("P_Asset_Acct");
        
    }
    
    /** Set Product COGS.
    @param P_COGS_Acct Account for Cost of Goods Sold */
    public void setP_COGS_Acct (int P_COGS_Acct)
    {
        set_Value ("P_COGS_Acct", Integer.valueOf(P_COGS_Acct));
        
    }
    
    /** Get Product COGS.
    @return Account for Cost of Goods Sold */
    public int getP_COGS_Acct() 
    {
        return get_ValueAsInt("P_COGS_Acct");
        
    }
    
    /** Set Cost Adjustment.
    @param P_CostAdjustment_Acct Product Cost Adjustment Account */
    public void setP_CostAdjustment_Acct (int P_CostAdjustment_Acct)
    {
        set_Value ("P_CostAdjustment_Acct", Integer.valueOf(P_CostAdjustment_Acct));
        
    }
    
    /** Get Cost Adjustment.
    @return Product Cost Adjustment Account */
    public int getP_CostAdjustment_Acct() 
    {
        return get_ValueAsInt("P_CostAdjustment_Acct");
        
    }
    
    /** Set Product Expense.
    @param P_Expense_Acct Account for Product Expense */
    public void setP_Expense_Acct (int P_Expense_Acct)
    {
        set_Value ("P_Expense_Acct", Integer.valueOf(P_Expense_Acct));
        
    }
    
    /** Get Product Expense.
    @return Account for Product Expense */
    public int getP_Expense_Acct() 
    {
        return get_ValueAsInt("P_Expense_Acct");
        
    }
    
    /** Set Inventory Clearing.
    @param P_InventoryClearing_Acct Product Inventory Clearing Account */
    public void setP_InventoryClearing_Acct (int P_InventoryClearing_Acct)
    {
        set_Value ("P_InventoryClearing_Acct", Integer.valueOf(P_InventoryClearing_Acct));
        
    }
    
    /** Get Inventory Clearing.
    @return Product Inventory Clearing Account */
    public int getP_InventoryClearing_Acct() 
    {
        return get_ValueAsInt("P_InventoryClearing_Acct");
        
    }
    
    /** Set Invoice Price Variance.
    @param P_InvoicePriceVariance_Acct Difference between Costs and Invoice Price (IPV) */
    public void setP_InvoicePriceVariance_Acct (int P_InvoicePriceVariance_Acct)
    {
        set_Value ("P_InvoicePriceVariance_Acct", Integer.valueOf(P_InvoicePriceVariance_Acct));
        
    }
    
    /** Get Invoice Price Variance.
    @return Difference between Costs and Invoice Price (IPV) */
    public int getP_InvoicePriceVariance_Acct() 
    {
        return get_ValueAsInt("P_InvoicePriceVariance_Acct");
        
    }
    
    /** Set Material Overhead.
    @param P_MaterialOverhd_Acct Material Overhead Account */
    public void setP_MaterialOverhd_Acct (int P_MaterialOverhd_Acct)
    {
        set_Value ("P_MaterialOverhd_Acct", Integer.valueOf(P_MaterialOverhd_Acct));
        
    }
    
    /** Get Material Overhead.
    @return Material Overhead Account */
    public int getP_MaterialOverhd_Acct() 
    {
        return get_ValueAsInt("P_MaterialOverhd_Acct");
        
    }
    
    /** Set Purchase Price Variance.
    @param P_PurchasePriceVariance_Acct Difference between Standard Cost and Purchase Price (PPV) */
    public void setP_PurchasePriceVariance_Acct (int P_PurchasePriceVariance_Acct)
    {
        set_Value ("P_PurchasePriceVariance_Acct", Integer.valueOf(P_PurchasePriceVariance_Acct));
        
    }
    
    /** Get Purchase Price Variance.
    @return Difference between Standard Cost and Purchase Price (PPV) */
    public int getP_PurchasePriceVariance_Acct() 
    {
        return get_ValueAsInt("P_PurchasePriceVariance_Acct");
        
    }
    
    /** Set Resource Absorption.
    @param P_Resource_Absorption_Acct Resource Absorption Account */
    public void setP_Resource_Absorption_Acct (int P_Resource_Absorption_Acct)
    {
        set_Value ("P_Resource_Absorption_Acct", Integer.valueOf(P_Resource_Absorption_Acct));
        
    }
    
    /** Get Resource Absorption.
    @return Resource Absorption Account */
    public int getP_Resource_Absorption_Acct() 
    {
        return get_ValueAsInt("P_Resource_Absorption_Acct");
        
    }
    
    /** Set Product Revenue.
    @param P_Revenue_Acct Account for Product Revenue (Sales Account) */
    public void setP_Revenue_Acct (int P_Revenue_Acct)
    {
        set_Value ("P_Revenue_Acct", Integer.valueOf(P_Revenue_Acct));
        
    }
    
    /** Get Product Revenue.
    @return Account for Product Revenue (Sales Account) */
    public int getP_Revenue_Acct() 
    {
        return get_ValueAsInt("P_Revenue_Acct");
        
    }
    
    /** Set Trade Discount Granted.
    @param P_TradeDiscountGrant_Acct Trade Discount Granted Account */
    public void setP_TradeDiscountGrant_Acct (int P_TradeDiscountGrant_Acct)
    {
        set_Value ("P_TradeDiscountGrant_Acct", Integer.valueOf(P_TradeDiscountGrant_Acct));
        
    }
    
    /** Get Trade Discount Granted.
    @return Trade Discount Granted Account */
    public int getP_TradeDiscountGrant_Acct() 
    {
        return get_ValueAsInt("P_TradeDiscountGrant_Acct");
        
    }
    
    /** Set Trade Discount Received.
    @param P_TradeDiscountRec_Acct Trade Discount Receivable Account */
    public void setP_TradeDiscountRec_Acct (int P_TradeDiscountRec_Acct)
    {
        set_Value ("P_TradeDiscountRec_Acct", Integer.valueOf(P_TradeDiscountRec_Acct));
        
    }
    
    /** Get Trade Discount Received.
    @return Trade Discount Receivable Account */
    public int getP_TradeDiscountRec_Acct() 
    {
        return get_ValueAsInt("P_TradeDiscountRec_Acct");
        
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
    
    
}
