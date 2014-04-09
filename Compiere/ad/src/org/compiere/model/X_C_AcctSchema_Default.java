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
/** Generated Model for C_AcctSchema_Default
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_AcctSchema_Default.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_AcctSchema_Default extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_AcctSchema_Default_ID id
    @param trx transaction
    */
    public X_C_AcctSchema_Default (Ctx ctx, int C_AcctSchema_Default_ID, Trx trx)
    {
        super (ctx, C_AcctSchema_Default_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_AcctSchema_Default_ID == 0)
        {
            setB_Asset_Acct (0);
            setB_Expense_Acct (0);
            setB_InTransit_Acct (0);
            setB_InterestExp_Acct (0);
            setB_InterestRev_Acct (0);
            setB_PaymentSelect_Acct (0);
            setB_RevaluationGain_Acct (0);
            setB_RevaluationLoss_Acct (0);
            setB_SettlementGain_Acct (0);
            setB_SettlementLoss_Acct (0);
            setB_UnallocatedCash_Acct (0);
            setB_Unidentified_Acct (0);
            setCB_Asset_Acct (0);
            setCB_CashTransfer_Acct (0);
            setCB_Differences_Acct (0);
            setCB_Expense_Acct (0);
            setCB_Receipt_Acct (0);
            setC_AcctSchema_ID (0);
            setC_Prepayment_Acct (0);
            setC_Receivable_Acct (0);
            setC_Receivable_Services_Acct (0);
            setCh_Expense_Acct (0);
            setCh_Revenue_Acct (0);
            setE_Expense_Acct (0);
            setE_Prepayment_Acct (0);
            setNotInvoicedReceipts_Acct (0);
            setNotInvoicedReceivables_Acct (0);
            setNotInvoicedRevenue_Acct (0);
            setPJ_Asset_Acct (0);
            setPJ_WIP_Acct (0);
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
            setPayDiscount_Exp_Acct (0);
            setPayDiscount_Rev_Acct (0);
            setRealizedGain_Acct (0);
            setRealizedLoss_Acct (0);
            setT_Credit_Acct (0);
            setT_Due_Acct (0);
            setT_Expense_Acct (0);
            setT_Liability_Acct (0);
            setT_Receivables_Acct (0);
            setUnEarnedRevenue_Acct (0);
            setUnrealizedGain_Acct (0);
            setUnrealizedLoss_Acct (0);
            setV_Liability_Acct (0);
            setV_Liability_Services_Acct (0);
            setV_Prepayment_Acct (0);
            setW_Differences_Acct (0);
            setW_InvActualAdjust_Acct (0);
            setW_Inventory_Acct (0);
            setW_Revaluation_Acct (0);
            setWithholding_Acct (0);
            setWriteOff_Acct (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_AcctSchema_Default (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27519571892789L;
    /** Last Updated Timestamp 2009-03-18 23:09:36.0 */
    public static final long updatedMS = 1237446576000L;
    /** AD_Table_ID=315 */
    public static final int Table_ID=315;
    
    /** TableName=C_AcctSchema_Default */
    public static final String Table_Name="C_AcctSchema_Default";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Bank Asset.
    @param B_Asset_Acct Bank Asset Account */
    public void setB_Asset_Acct (int B_Asset_Acct)
    {
        set_Value ("B_Asset_Acct", Integer.valueOf(B_Asset_Acct));
        
    }
    
    /** Get Bank Asset.
    @return Bank Asset Account */
    public int getB_Asset_Acct() 
    {
        return get_ValueAsInt("B_Asset_Acct");
        
    }
    
    /** Set Bank Expense.
    @param B_Expense_Acct Bank Expense Account */
    public void setB_Expense_Acct (int B_Expense_Acct)
    {
        set_Value ("B_Expense_Acct", Integer.valueOf(B_Expense_Acct));
        
    }
    
    /** Get Bank Expense.
    @return Bank Expense Account */
    public int getB_Expense_Acct() 
    {
        return get_ValueAsInt("B_Expense_Acct");
        
    }
    
    /** Set Bank In Transit.
    @param B_InTransit_Acct Bank In Transit Account */
    public void setB_InTransit_Acct (int B_InTransit_Acct)
    {
        set_Value ("B_InTransit_Acct", Integer.valueOf(B_InTransit_Acct));
        
    }
    
    /** Get Bank In Transit.
    @return Bank In Transit Account */
    public int getB_InTransit_Acct() 
    {
        return get_ValueAsInt("B_InTransit_Acct");
        
    }
    
    /** Set Bank Interest Expense.
    @param B_InterestExp_Acct Bank Interest Expense Account */
    public void setB_InterestExp_Acct (int B_InterestExp_Acct)
    {
        set_Value ("B_InterestExp_Acct", Integer.valueOf(B_InterestExp_Acct));
        
    }
    
    /** Get Bank Interest Expense.
    @return Bank Interest Expense Account */
    public int getB_InterestExp_Acct() 
    {
        return get_ValueAsInt("B_InterestExp_Acct");
        
    }
    
    /** Set Bank Interest Revenue.
    @param B_InterestRev_Acct Bank Interest Revenue Account */
    public void setB_InterestRev_Acct (int B_InterestRev_Acct)
    {
        set_Value ("B_InterestRev_Acct", Integer.valueOf(B_InterestRev_Acct));
        
    }
    
    /** Get Bank Interest Revenue.
    @return Bank Interest Revenue Account */
    public int getB_InterestRev_Acct() 
    {
        return get_ValueAsInt("B_InterestRev_Acct");
        
    }
    
    /** Set Payment Selection.
    @param B_PaymentSelect_Acct AP Payment Selection Clearing Account */
    public void setB_PaymentSelect_Acct (int B_PaymentSelect_Acct)
    {
        set_Value ("B_PaymentSelect_Acct", Integer.valueOf(B_PaymentSelect_Acct));
        
    }
    
    /** Get Payment Selection.
    @return AP Payment Selection Clearing Account */
    public int getB_PaymentSelect_Acct() 
    {
        return get_ValueAsInt("B_PaymentSelect_Acct");
        
    }
    
    /** Set Bank Revaluation Gain.
    @param B_RevaluationGain_Acct Bank Revaluation Gain Account */
    public void setB_RevaluationGain_Acct (int B_RevaluationGain_Acct)
    {
        set_Value ("B_RevaluationGain_Acct", Integer.valueOf(B_RevaluationGain_Acct));
        
    }
    
    /** Get Bank Revaluation Gain.
    @return Bank Revaluation Gain Account */
    public int getB_RevaluationGain_Acct() 
    {
        return get_ValueAsInt("B_RevaluationGain_Acct");
        
    }
    
    /** Set Bank Revaluation Loss.
    @param B_RevaluationLoss_Acct Bank Revaluation Loss Account */
    public void setB_RevaluationLoss_Acct (int B_RevaluationLoss_Acct)
    {
        set_Value ("B_RevaluationLoss_Acct", Integer.valueOf(B_RevaluationLoss_Acct));
        
    }
    
    /** Get Bank Revaluation Loss.
    @return Bank Revaluation Loss Account */
    public int getB_RevaluationLoss_Acct() 
    {
        return get_ValueAsInt("B_RevaluationLoss_Acct");
        
    }
    
    /** Set Bank Settlement Gain.
    @param B_SettlementGain_Acct Bank Settlement Gain Account */
    public void setB_SettlementGain_Acct (int B_SettlementGain_Acct)
    {
        set_Value ("B_SettlementGain_Acct", Integer.valueOf(B_SettlementGain_Acct));
        
    }
    
    /** Get Bank Settlement Gain.
    @return Bank Settlement Gain Account */
    public int getB_SettlementGain_Acct() 
    {
        return get_ValueAsInt("B_SettlementGain_Acct");
        
    }
    
    /** Set Bank Settlement Loss.
    @param B_SettlementLoss_Acct Bank Settlement Loss Account */
    public void setB_SettlementLoss_Acct (int B_SettlementLoss_Acct)
    {
        set_Value ("B_SettlementLoss_Acct", Integer.valueOf(B_SettlementLoss_Acct));
        
    }
    
    /** Get Bank Settlement Loss.
    @return Bank Settlement Loss Account */
    public int getB_SettlementLoss_Acct() 
    {
        return get_ValueAsInt("B_SettlementLoss_Acct");
        
    }
    
    /** Set Unallocated Cash.
    @param B_UnallocatedCash_Acct Unallocated Cash Clearing Account */
    public void setB_UnallocatedCash_Acct (int B_UnallocatedCash_Acct)
    {
        set_Value ("B_UnallocatedCash_Acct", Integer.valueOf(B_UnallocatedCash_Acct));
        
    }
    
    /** Get Unallocated Cash.
    @return Unallocated Cash Clearing Account */
    public int getB_UnallocatedCash_Acct() 
    {
        return get_ValueAsInt("B_UnallocatedCash_Acct");
        
    }
    
    /** Set Bank Unidentified Receipts.
    @param B_Unidentified_Acct Bank Unidentified Receipts Account */
    public void setB_Unidentified_Acct (int B_Unidentified_Acct)
    {
        set_Value ("B_Unidentified_Acct", Integer.valueOf(B_Unidentified_Acct));
        
    }
    
    /** Get Bank Unidentified Receipts.
    @return Bank Unidentified Receipts Account */
    public int getB_Unidentified_Acct() 
    {
        return get_ValueAsInt("B_Unidentified_Acct");
        
    }
    
    /** Set Cash Book Asset.
    @param CB_Asset_Acct Cash Book Asset Account */
    public void setCB_Asset_Acct (int CB_Asset_Acct)
    {
        set_Value ("CB_Asset_Acct", Integer.valueOf(CB_Asset_Acct));
        
    }
    
    /** Get Cash Book Asset.
    @return Cash Book Asset Account */
    public int getCB_Asset_Acct() 
    {
        return get_ValueAsInt("CB_Asset_Acct");
        
    }
    
    /** Set Cash Transfer.
    @param CB_CashTransfer_Acct Cash Transfer Clearing Account */
    public void setCB_CashTransfer_Acct (int CB_CashTransfer_Acct)
    {
        set_Value ("CB_CashTransfer_Acct", Integer.valueOf(CB_CashTransfer_Acct));
        
    }
    
    /** Get Cash Transfer.
    @return Cash Transfer Clearing Account */
    public int getCB_CashTransfer_Acct() 
    {
        return get_ValueAsInt("CB_CashTransfer_Acct");
        
    }
    
    /** Set Cash Book Differences.
    @param CB_Differences_Acct Cash Book Differences Account */
    public void setCB_Differences_Acct (int CB_Differences_Acct)
    {
        set_Value ("CB_Differences_Acct", Integer.valueOf(CB_Differences_Acct));
        
    }
    
    /** Get Cash Book Differences.
    @return Cash Book Differences Account */
    public int getCB_Differences_Acct() 
    {
        return get_ValueAsInt("CB_Differences_Acct");
        
    }
    
    /** Set Cash Book Expense.
    @param CB_Expense_Acct Cash Book Expense Account */
    public void setCB_Expense_Acct (int CB_Expense_Acct)
    {
        set_Value ("CB_Expense_Acct", Integer.valueOf(CB_Expense_Acct));
        
    }
    
    /** Get Cash Book Expense.
    @return Cash Book Expense Account */
    public int getCB_Expense_Acct() 
    {
        return get_ValueAsInt("CB_Expense_Acct");
        
    }
    
    /** Set Cash Book Receipt.
    @param CB_Receipt_Acct Cash Book Receipts Account */
    public void setCB_Receipt_Acct (int CB_Receipt_Acct)
    {
        set_Value ("CB_Receipt_Acct", Integer.valueOf(CB_Receipt_Acct));
        
    }
    
    /** Get Cash Book Receipt.
    @return Cash Book Receipts Account */
    public int getCB_Receipt_Acct() 
    {
        return get_ValueAsInt("CB_Receipt_Acct");
        
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
    
    /** Set Customer Prepayment.
    @param C_Prepayment_Acct Account for customer prepayments */
    public void setC_Prepayment_Acct (int C_Prepayment_Acct)
    {
        set_Value ("C_Prepayment_Acct", Integer.valueOf(C_Prepayment_Acct));
        
    }
    
    /** Get Customer Prepayment.
    @return Account for customer prepayments */
    public int getC_Prepayment_Acct() 
    {
        return get_ValueAsInt("C_Prepayment_Acct");
        
    }
    
    /** Set Customer Receivables.
    @param C_Receivable_Acct Account for Customer Receivables */
    public void setC_Receivable_Acct (int C_Receivable_Acct)
    {
        set_Value ("C_Receivable_Acct", Integer.valueOf(C_Receivable_Acct));
        
    }
    
    /** Get Customer Receivables.
    @return Account for Customer Receivables */
    public int getC_Receivable_Acct() 
    {
        return get_ValueAsInt("C_Receivable_Acct");
        
    }
    
    /** Set Receivable Services.
    @param C_Receivable_Services_Acct Customer Accounts Receivables Services Account */
    public void setC_Receivable_Services_Acct (int C_Receivable_Services_Acct)
    {
        set_Value ("C_Receivable_Services_Acct", Integer.valueOf(C_Receivable_Services_Acct));
        
    }
    
    /** Get Receivable Services.
    @return Customer Accounts Receivables Services Account */
    public int getC_Receivable_Services_Acct() 
    {
        return get_ValueAsInt("C_Receivable_Services_Acct");
        
    }
    
    /** Set Charge Expense.
    @param Ch_Expense_Acct Charge Expense Account */
    public void setCh_Expense_Acct (int Ch_Expense_Acct)
    {
        set_Value ("Ch_Expense_Acct", Integer.valueOf(Ch_Expense_Acct));
        
    }
    
    /** Get Charge Expense.
    @return Charge Expense Account */
    public int getCh_Expense_Acct() 
    {
        return get_ValueAsInt("Ch_Expense_Acct");
        
    }
    
    /** Set Charge Revenue.
    @param Ch_Revenue_Acct Charge Revenue Account */
    public void setCh_Revenue_Acct (int Ch_Revenue_Acct)
    {
        set_Value ("Ch_Revenue_Acct", Integer.valueOf(Ch_Revenue_Acct));
        
    }
    
    /** Get Charge Revenue.
    @return Charge Revenue Account */
    public int getCh_Revenue_Acct() 
    {
        return get_ValueAsInt("Ch_Revenue_Acct");
        
    }
    
    /** Set Employee Expense.
    @param E_Expense_Acct Account for Employee Expenses */
    public void setE_Expense_Acct (int E_Expense_Acct)
    {
        set_Value ("E_Expense_Acct", Integer.valueOf(E_Expense_Acct));
        
    }
    
    /** Get Employee Expense.
    @return Account for Employee Expenses */
    public int getE_Expense_Acct() 
    {
        return get_ValueAsInt("E_Expense_Acct");
        
    }
    
    /** Set Employee Prepayment.
    @param E_Prepayment_Acct Account for Employee Expense Prepayments */
    public void setE_Prepayment_Acct (int E_Prepayment_Acct)
    {
        set_Value ("E_Prepayment_Acct", Integer.valueOf(E_Prepayment_Acct));
        
    }
    
    /** Get Employee Prepayment.
    @return Account for Employee Expense Prepayments */
    public int getE_Prepayment_Acct() 
    {
        return get_ValueAsInt("E_Prepayment_Acct");
        
    }
    
    /** Set Not-invoiced Receipts.
    @param NotInvoicedReceipts_Acct Account for not-invoiced Material Receipts */
    public void setNotInvoicedReceipts_Acct (int NotInvoicedReceipts_Acct)
    {
        set_Value ("NotInvoicedReceipts_Acct", Integer.valueOf(NotInvoicedReceipts_Acct));
        
    }
    
    /** Get Not-invoiced Receipts.
    @return Account for not-invoiced Material Receipts */
    public int getNotInvoicedReceipts_Acct() 
    {
        return get_ValueAsInt("NotInvoicedReceipts_Acct");
        
    }
    
    /** Set Not-invoiced Receivables.
    @param NotInvoicedReceivables_Acct Account for not invoiced Receivables */
    public void setNotInvoicedReceivables_Acct (int NotInvoicedReceivables_Acct)
    {
        set_Value ("NotInvoicedReceivables_Acct", Integer.valueOf(NotInvoicedReceivables_Acct));
        
    }
    
    /** Get Not-invoiced Receivables.
    @return Account for not invoiced Receivables */
    public int getNotInvoicedReceivables_Acct() 
    {
        return get_ValueAsInt("NotInvoicedReceivables_Acct");
        
    }
    
    /** Set Not-invoiced Revenue.
    @param NotInvoicedRevenue_Acct Account for not invoiced Revenue */
    public void setNotInvoicedRevenue_Acct (int NotInvoicedRevenue_Acct)
    {
        set_Value ("NotInvoicedRevenue_Acct", Integer.valueOf(NotInvoicedRevenue_Acct));
        
    }
    
    /** Get Not-invoiced Revenue.
    @return Account for not invoiced Revenue */
    public int getNotInvoicedRevenue_Acct() 
    {
        return get_ValueAsInt("NotInvoicedRevenue_Acct");
        
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
    
    /** Set Project Asset.
    @param PJ_Asset_Acct Project Asset Account */
    public void setPJ_Asset_Acct (int PJ_Asset_Acct)
    {
        set_Value ("PJ_Asset_Acct", Integer.valueOf(PJ_Asset_Acct));
        
    }
    
    /** Get Project Asset.
    @return Project Asset Account */
    public int getPJ_Asset_Acct() 
    {
        return get_ValueAsInt("PJ_Asset_Acct");
        
    }
    
    /** Set Work In Progress.
    @param PJ_WIP_Acct Account for Work in Progress */
    public void setPJ_WIP_Acct (int PJ_WIP_Acct)
    {
        set_Value ("PJ_WIP_Acct", Integer.valueOf(PJ_WIP_Acct));
        
    }
    
    /** Get Work In Progress.
    @return Account for Work in Progress */
    public int getPJ_WIP_Acct() 
    {
        return get_ValueAsInt("PJ_WIP_Acct");
        
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
    
    /** Set Payment Discount Expense.
    @param PayDiscount_Exp_Acct Payment Discount Expense Account */
    public void setPayDiscount_Exp_Acct (int PayDiscount_Exp_Acct)
    {
        set_Value ("PayDiscount_Exp_Acct", Integer.valueOf(PayDiscount_Exp_Acct));
        
    }
    
    /** Get Payment Discount Expense.
    @return Payment Discount Expense Account */
    public int getPayDiscount_Exp_Acct() 
    {
        return get_ValueAsInt("PayDiscount_Exp_Acct");
        
    }
    
    /** Set Payment Discount Revenue.
    @param PayDiscount_Rev_Acct Payment Discount Revenue Account */
    public void setPayDiscount_Rev_Acct (int PayDiscount_Rev_Acct)
    {
        set_Value ("PayDiscount_Rev_Acct", Integer.valueOf(PayDiscount_Rev_Acct));
        
    }
    
    /** Get Payment Discount Revenue.
    @return Payment Discount Revenue Account */
    public int getPayDiscount_Rev_Acct() 
    {
        return get_ValueAsInt("PayDiscount_Rev_Acct");
        
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
    
    /** Set Realized Gain Acct.
    @param RealizedGain_Acct Realized Gain Account */
    public void setRealizedGain_Acct (int RealizedGain_Acct)
    {
        set_Value ("RealizedGain_Acct", Integer.valueOf(RealizedGain_Acct));
        
    }
    
    /** Get Realized Gain Acct.
    @return Realized Gain Account */
    public int getRealizedGain_Acct() 
    {
        return get_ValueAsInt("RealizedGain_Acct");
        
    }
    
    /** Set Realized Loss Acct.
    @param RealizedLoss_Acct Realized Loss Account */
    public void setRealizedLoss_Acct (int RealizedLoss_Acct)
    {
        set_Value ("RealizedLoss_Acct", Integer.valueOf(RealizedLoss_Acct));
        
    }
    
    /** Get Realized Loss Acct.
    @return Realized Loss Account */
    public int getRealizedLoss_Acct() 
    {
        return get_ValueAsInt("RealizedLoss_Acct");
        
    }
    
    /** Set Tax Credit.
    @param T_Credit_Acct Account for Tax you can reclaim */
    public void setT_Credit_Acct (int T_Credit_Acct)
    {
        set_Value ("T_Credit_Acct", Integer.valueOf(T_Credit_Acct));
        
    }
    
    /** Get Tax Credit.
    @return Account for Tax you can reclaim */
    public int getT_Credit_Acct() 
    {
        return get_ValueAsInt("T_Credit_Acct");
        
    }
    
    /** Set Tax Due.
    @param T_Due_Acct Account for Tax you have to pay */
    public void setT_Due_Acct (int T_Due_Acct)
    {
        set_Value ("T_Due_Acct", Integer.valueOf(T_Due_Acct));
        
    }
    
    /** Get Tax Due.
    @return Account for Tax you have to pay */
    public int getT_Due_Acct() 
    {
        return get_ValueAsInt("T_Due_Acct");
        
    }
    
    /** Set Tax Expense.
    @param T_Expense_Acct Account for paid tax you cannot reclaim */
    public void setT_Expense_Acct (int T_Expense_Acct)
    {
        set_Value ("T_Expense_Acct", Integer.valueOf(T_Expense_Acct));
        
    }
    
    /** Get Tax Expense.
    @return Account for paid tax you cannot reclaim */
    public int getT_Expense_Acct() 
    {
        return get_ValueAsInt("T_Expense_Acct");
        
    }
    
    /** Set Tax Liability.
    @param T_Liability_Acct Account for Tax declaration liability */
    public void setT_Liability_Acct (int T_Liability_Acct)
    {
        set_Value ("T_Liability_Acct", Integer.valueOf(T_Liability_Acct));
        
    }
    
    /** Get Tax Liability.
    @return Account for Tax declaration liability */
    public int getT_Liability_Acct() 
    {
        return get_ValueAsInt("T_Liability_Acct");
        
    }
    
    /** Set Tax Receivables.
    @param T_Receivables_Acct Account for Tax credit after tax declaration */
    public void setT_Receivables_Acct (int T_Receivables_Acct)
    {
        set_Value ("T_Receivables_Acct", Integer.valueOf(T_Receivables_Acct));
        
    }
    
    /** Get Tax Receivables.
    @return Account for Tax credit after tax declaration */
    public int getT_Receivables_Acct() 
    {
        return get_ValueAsInt("T_Receivables_Acct");
        
    }
    
    /** Set Unearned Revenue.
    @param UnEarnedRevenue_Acct Account for unearned revenue */
    public void setUnEarnedRevenue_Acct (int UnEarnedRevenue_Acct)
    {
        set_Value ("UnEarnedRevenue_Acct", Integer.valueOf(UnEarnedRevenue_Acct));
        
    }
    
    /** Get Unearned Revenue.
    @return Account for unearned revenue */
    public int getUnEarnedRevenue_Acct() 
    {
        return get_ValueAsInt("UnEarnedRevenue_Acct");
        
    }
    
    /** Set Unrealized Gain Acct.
    @param UnrealizedGain_Acct Unrealized Gain Account for currency revaluation */
    public void setUnrealizedGain_Acct (int UnrealizedGain_Acct)
    {
        set_Value ("UnrealizedGain_Acct", Integer.valueOf(UnrealizedGain_Acct));
        
    }
    
    /** Get Unrealized Gain Acct.
    @return Unrealized Gain Account for currency revaluation */
    public int getUnrealizedGain_Acct() 
    {
        return get_ValueAsInt("UnrealizedGain_Acct");
        
    }
    
    /** Set Unrealized Loss Acct.
    @param UnrealizedLoss_Acct Unrealized Loss Account for currency revaluation */
    public void setUnrealizedLoss_Acct (int UnrealizedLoss_Acct)
    {
        set_Value ("UnrealizedLoss_Acct", Integer.valueOf(UnrealizedLoss_Acct));
        
    }
    
    /** Get Unrealized Loss Acct.
    @return Unrealized Loss Account for currency revaluation */
    public int getUnrealizedLoss_Acct() 
    {
        return get_ValueAsInt("UnrealizedLoss_Acct");
        
    }
    
    /** Set Vendor Liability.
    @param V_Liability_Acct Account for Vendor Liability */
    public void setV_Liability_Acct (int V_Liability_Acct)
    {
        set_Value ("V_Liability_Acct", Integer.valueOf(V_Liability_Acct));
        
    }
    
    /** Get Vendor Liability.
    @return Account for Vendor Liability */
    public int getV_Liability_Acct() 
    {
        return get_ValueAsInt("V_Liability_Acct");
        
    }
    
    /** Set Vendor Service Liability.
    @param V_Liability_Services_Acct Account for Vender Service Liability */
    public void setV_Liability_Services_Acct (int V_Liability_Services_Acct)
    {
        set_Value ("V_Liability_Services_Acct", Integer.valueOf(V_Liability_Services_Acct));
        
    }
    
    /** Get Vendor Service Liability.
    @return Account for Vender Service Liability */
    public int getV_Liability_Services_Acct() 
    {
        return get_ValueAsInt("V_Liability_Services_Acct");
        
    }
    
    /** Set Vendor Prepayment.
    @param V_Prepayment_Acct Account for Vendor Prepayments */
    public void setV_Prepayment_Acct (int V_Prepayment_Acct)
    {
        set_Value ("V_Prepayment_Acct", Integer.valueOf(V_Prepayment_Acct));
        
    }
    
    /** Get Vendor Prepayment.
    @return Account for Vendor Prepayments */
    public int getV_Prepayment_Acct() 
    {
        return get_ValueAsInt("V_Prepayment_Acct");
        
    }
    
    /** Set Work Center Overhead.
    @param WC_Overhead_Acct Work Center Overhead Account */
    public void setWC_Overhead_Acct (int WC_Overhead_Acct)
    {
        set_Value ("WC_Overhead_Acct", Integer.valueOf(WC_Overhead_Acct));
        
    }
    
    /** Get Work Center Overhead.
    @return Work Center Overhead Account */
    public int getWC_Overhead_Acct() 
    {
        return get_ValueAsInt("WC_Overhead_Acct");
        
    }
    
    /** Set Work Order Material Overhead Variance.
    @param WO_MaterialOverhdVariance_Acct Work Order Material Overhead Variance Account */
    public void setWO_MaterialOverhdVariance_Acct (int WO_MaterialOverhdVariance_Acct)
    {
        set_Value ("WO_MaterialOverhdVariance_Acct", Integer.valueOf(WO_MaterialOverhdVariance_Acct));
        
    }
    
    /** Get Work Order Material Overhead Variance.
    @return Work Order Material Overhead Variance Account */
    public int getWO_MaterialOverhdVariance_Acct() 
    {
        return get_ValueAsInt("WO_MaterialOverhdVariance_Acct");
        
    }
    
    /** Set Work Order Material Overhead.
    @param WO_MaterialOverhd_Acct Work Order Material Overhead Account */
    public void setWO_MaterialOverhd_Acct (int WO_MaterialOverhd_Acct)
    {
        set_Value ("WO_MaterialOverhd_Acct", Integer.valueOf(WO_MaterialOverhd_Acct));
        
    }
    
    /** Get Work Order Material Overhead.
    @return Work Order Material Overhead Account */
    public int getWO_MaterialOverhd_Acct() 
    {
        return get_ValueAsInt("WO_MaterialOverhd_Acct");
        
    }
    
    /** Set Work Order Material Variance.
    @param WO_MaterialVariance_Acct Work Order Material Variance Account */
    public void setWO_MaterialVariance_Acct (int WO_MaterialVariance_Acct)
    {
        set_Value ("WO_MaterialVariance_Acct", Integer.valueOf(WO_MaterialVariance_Acct));
        
    }
    
    /** Get Work Order Material Variance.
    @return Work Order Material Variance Account */
    public int getWO_MaterialVariance_Acct() 
    {
        return get_ValueAsInt("WO_MaterialVariance_Acct");
        
    }
    
    /** Set Work Order Material.
    @param WO_Material_Acct Work Order Material Account */
    public void setWO_Material_Acct (int WO_Material_Acct)
    {
        set_Value ("WO_Material_Acct", Integer.valueOf(WO_Material_Acct));
        
    }
    
    /** Get Work Order Material.
    @return Work Order Material Account */
    public int getWO_Material_Acct() 
    {
        return get_ValueAsInt("WO_Material_Acct");
        
    }
    
    /** Set Work Order Outside Processing Variance.
    @param WO_OSPVariance_Acct Work Order Outside Processing Variance Account */
    public void setWO_OSPVariance_Acct (int WO_OSPVariance_Acct)
    {
        set_Value ("WO_OSPVariance_Acct", Integer.valueOf(WO_OSPVariance_Acct));
        
    }
    
    /** Get Work Order Outside Processing Variance.
    @return Work Order Outside Processing Variance Account */
    public int getWO_OSPVariance_Acct() 
    {
        return get_ValueAsInt("WO_OSPVariance_Acct");
        
    }
    
    /** Set Work Order Outside Processing.
    @param WO_OSP_Acct Work Order Outside Processing Account */
    public void setWO_OSP_Acct (int WO_OSP_Acct)
    {
        set_Value ("WO_OSP_Acct", Integer.valueOf(WO_OSP_Acct));
        
    }
    
    /** Get Work Order Outside Processing.
    @return Work Order Outside Processing Account */
    public int getWO_OSP_Acct() 
    {
        return get_ValueAsInt("WO_OSP_Acct");
        
    }
    
    /** Set Work Order Overhead Variance.
    @param WO_OverhdVariance_Acct Work Order Overhead Variance Account */
    public void setWO_OverhdVariance_Acct (int WO_OverhdVariance_Acct)
    {
        set_Value ("WO_OverhdVariance_Acct", Integer.valueOf(WO_OverhdVariance_Acct));
        
    }
    
    /** Get Work Order Overhead Variance.
    @return Work Order Overhead Variance Account */
    public int getWO_OverhdVariance_Acct() 
    {
        return get_ValueAsInt("WO_OverhdVariance_Acct");
        
    }
    
    /** Set Work Order Resource Variance.
    @param WO_ResourceVariance_Acct Work Order Resource Variance Account */
    public void setWO_ResourceVariance_Acct (int WO_ResourceVariance_Acct)
    {
        set_Value ("WO_ResourceVariance_Acct", Integer.valueOf(WO_ResourceVariance_Acct));
        
    }
    
    /** Get Work Order Resource Variance.
    @return Work Order Resource Variance Account */
    public int getWO_ResourceVariance_Acct() 
    {
        return get_ValueAsInt("WO_ResourceVariance_Acct");
        
    }
    
    /** Set Work Order Resource.
    @param WO_Resource_Acct Work Order Resource Account */
    public void setWO_Resource_Acct (int WO_Resource_Acct)
    {
        set_Value ("WO_Resource_Acct", Integer.valueOf(WO_Resource_Acct));
        
    }
    
    /** Get Work Order Resource.
    @return Work Order Resource Account */
    public int getWO_Resource_Acct() 
    {
        return get_ValueAsInt("WO_Resource_Acct");
        
    }
    
    /** Set Work Order Scrap.
    @param WO_Scrap_Acct Work Order Scrap Account */
    public void setWO_Scrap_Acct (int WO_Scrap_Acct)
    {
        set_Value ("WO_Scrap_Acct", Integer.valueOf(WO_Scrap_Acct));
        
    }
    
    /** Get Work Order Scrap.
    @return Work Order Scrap Account */
    public int getWO_Scrap_Acct() 
    {
        return get_ValueAsInt("WO_Scrap_Acct");
        
    }
    
    /** Set Warehouse Differences.
    @param W_Differences_Acct Warehouse Differences Account */
    public void setW_Differences_Acct (int W_Differences_Acct)
    {
        set_Value ("W_Differences_Acct", Integer.valueOf(W_Differences_Acct));
        
    }
    
    /** Get Warehouse Differences.
    @return Warehouse Differences Account */
    public int getW_Differences_Acct() 
    {
        return get_ValueAsInt("W_Differences_Acct");
        
    }
    
    /** Set Inventory Adjustment.
    @param W_InvActualAdjust_Acct Account for Inventory value adjustments for Actual Costing */
    public void setW_InvActualAdjust_Acct (int W_InvActualAdjust_Acct)
    {
        set_Value ("W_InvActualAdjust_Acct", Integer.valueOf(W_InvActualAdjust_Acct));
        
    }
    
    /** Get Inventory Adjustment.
    @return Account for Inventory value adjustments for Actual Costing */
    public int getW_InvActualAdjust_Acct() 
    {
        return get_ValueAsInt("W_InvActualAdjust_Acct");
        
    }
    
    /** Set (Not Used).
    @param W_Inventory_Acct Warehouse Inventory Asset Account - Currently not used */
    public void setW_Inventory_Acct (int W_Inventory_Acct)
    {
        set_Value ("W_Inventory_Acct", Integer.valueOf(W_Inventory_Acct));
        
    }
    
    /** Get (Not Used).
    @return Warehouse Inventory Asset Account - Currently not used */
    public int getW_Inventory_Acct() 
    {
        return get_ValueAsInt("W_Inventory_Acct");
        
    }
    
    /** Set Inventory Revaluation.
    @param W_Revaluation_Acct Account for Inventory Revaluation */
    public void setW_Revaluation_Acct (int W_Revaluation_Acct)
    {
        set_Value ("W_Revaluation_Acct", Integer.valueOf(W_Revaluation_Acct));
        
    }
    
    /** Get Inventory Revaluation.
    @return Account for Inventory Revaluation */
    public int getW_Revaluation_Acct() 
    {
        return get_ValueAsInt("W_Revaluation_Acct");
        
    }
    
    /** Set Withholding.
    @param Withholding_Acct Account for Withholdings */
    public void setWithholding_Acct (int Withholding_Acct)
    {
        set_Value ("Withholding_Acct", Integer.valueOf(Withholding_Acct));
        
    }
    
    /** Get Withholding.
    @return Account for Withholdings */
    public int getWithholding_Acct() 
    {
        return get_ValueAsInt("Withholding_Acct");
        
    }
    
    /** Set Write-off.
    @param WriteOff_Acct Account for Receivables write-off */
    public void setWriteOff_Acct (int WriteOff_Acct)
    {
        set_Value ("WriteOff_Acct", Integer.valueOf(WriteOff_Acct));
        
    }
    
    /** Get Write-off.
    @return Account for Receivables write-off */
    public int getWriteOff_Acct() 
    {
        return get_ValueAsInt("WriteOff_Acct");
        
    }
    
    
}
