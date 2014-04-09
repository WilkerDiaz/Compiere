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
/** Generated Model for C_Cash_ExpenseReceipt_Acct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: GenerateModel.java 8757 2010-05-12 21:32:32Z nnayak $ */
public class X_C_Cash_ExpenseReceipt_Acct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Cash_ExpenseReceipt_Acct_ID id
    @param trx transaction
    */
    public X_C_Cash_ExpenseReceipt_Acct (Ctx ctx, int C_Cash_ExpenseReceipt_Acct_ID, Trx trx)
    {
        super (ctx, C_Cash_ExpenseReceipt_Acct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Cash_ExpenseReceipt_Acct_ID == 0)
        {
            setCB_Expense_Acct (0);
            setCB_Receipt_Acct (0);
            setC_AcctSchema_ID (0);
            setC_Cash_ExpenseReceiptType_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Cash_ExpenseReceipt_Acct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27559451290789L;
    /** Last Updated Timestamp 2010-06-24 02:16:14.0 */
    public static final long updatedMS = 1277325974000L;
    /** AD_Table_ID=2171 */
    public static final int Table_ID=2171;
    
    /** TableName=C_Cash_ExpenseReceipt_Acct */
    public static final String Table_Name="C_Cash_ExpenseReceipt_Acct";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Expense/Receipt Type.
    @param C_Cash_ExpenseReceiptType_ID Expense/Receipt Type */
    public void setC_Cash_ExpenseReceiptType_ID (int C_Cash_ExpenseReceiptType_ID)
    {
        if (C_Cash_ExpenseReceiptType_ID < 1) throw new IllegalArgumentException ("C_Cash_ExpenseReceiptType_ID is mandatory.");
        set_ValueNoCheck ("C_Cash_ExpenseReceiptType_ID", Integer.valueOf(C_Cash_ExpenseReceiptType_ID));
        
    }
    
    /** Get Expense/Receipt Type.
    @return Expense/Receipt Type */
    public int getC_Cash_ExpenseReceiptType_ID() 
    {
        return get_ValueAsInt("C_Cash_ExpenseReceiptType_ID");
        
    }
    
    
}
