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
/** Generated Model for C_Tax_Acct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Tax_Acct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Tax_Acct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Tax_Acct_ID id
    @param trx transaction
    */
    public X_C_Tax_Acct (Ctx ctx, int C_Tax_Acct_ID, Trx trx)
    {
        super (ctx, C_Tax_Acct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Tax_Acct_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setC_Tax_ID (0);
            setT_Credit_Acct (0);
            setT_Due_Acct (0);
            setT_Expense_Acct (0);
            setT_Liability_Acct (0);
            setT_Receivables_Acct (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Tax_Acct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=399 */
    public static final int Table_ID=399;
    
    /** TableName=C_Tax_Acct */
    public static final String Table_Name="C_Tax_Acct";
    
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
    
    /** Set Tax.
    @param C_Tax_ID Tax identifier */
    public void setC_Tax_ID (int C_Tax_ID)
    {
        if (C_Tax_ID < 1) throw new IllegalArgumentException ("C_Tax_ID is mandatory.");
        set_ValueNoCheck ("C_Tax_ID", Integer.valueOf(C_Tax_ID));
        
    }
    
    /** Get Tax.
    @return Tax identifier */
    public int getC_Tax_ID() 
    {
        return get_ValueAsInt("C_Tax_ID");
        
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
    
    
}
