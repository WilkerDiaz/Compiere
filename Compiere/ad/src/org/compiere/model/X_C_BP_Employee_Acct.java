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
/** Generated Model for C_BP_Employee_Acct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_BP_Employee_Acct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_BP_Employee_Acct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BP_Employee_Acct_ID id
    @param trx transaction
    */
    public X_C_BP_Employee_Acct (Ctx ctx, int C_BP_Employee_Acct_ID, Trx trx)
    {
        super (ctx, C_BP_Employee_Acct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BP_Employee_Acct_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setC_BPartner_ID (0);
            setE_Expense_Acct (0);
            setE_Prepayment_Acct (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BP_Employee_Acct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=184 */
    public static final int Table_ID=184;
    
    /** TableName=C_BP_Employee_Acct */
    public static final String Table_Name="C_BP_Employee_Acct";
    
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
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
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
    
    
}
