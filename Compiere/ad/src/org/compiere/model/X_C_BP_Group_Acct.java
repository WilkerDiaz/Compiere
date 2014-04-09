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
/** Generated Model for C_BP_Group_Acct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_BP_Group_Acct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_BP_Group_Acct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BP_Group_Acct_ID id
    @param trx transaction
    */
    public X_C_BP_Group_Acct (Ctx ctx, int C_BP_Group_Acct_ID, Trx trx)
    {
        super (ctx, C_BP_Group_Acct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BP_Group_Acct_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setC_BP_Group_ID (0);
            setC_Prepayment_Acct (0);
            setC_Receivable_Acct (0);
            setC_Receivable_Services_Acct (0);
            setNotInvoicedReceipts_Acct (0);
            setNotInvoicedReceivables_Acct (0);
            setNotInvoicedRevenue_Acct (0);
            setPayDiscount_Exp_Acct (0);
            setPayDiscount_Rev_Acct (0);
            setUnEarnedRevenue_Acct (0);
            setV_Liability_Acct (0);
            setV_Liability_Services_Acct (0);
            setV_Prepayment_Acct (0);
            setWriteOff_Acct (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BP_Group_Acct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=395 */
    public static final int Table_ID=395;
    
    /** TableName=C_BP_Group_Acct */
    public static final String Table_Name="C_BP_Group_Acct";
    
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
    
    /** Set Business Partner Group.
    @param C_BP_Group_ID Business Partner Group */
    public void setC_BP_Group_ID (int C_BP_Group_ID)
    {
        if (C_BP_Group_ID < 1) throw new IllegalArgumentException ("C_BP_Group_ID is mandatory.");
        set_ValueNoCheck ("C_BP_Group_ID", Integer.valueOf(C_BP_Group_ID));
        
    }
    
    /** Get Business Partner Group.
    @return Business Partner Group */
    public int getC_BP_Group_ID() 
    {
        return get_ValueAsInt("C_BP_Group_ID");
        
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
