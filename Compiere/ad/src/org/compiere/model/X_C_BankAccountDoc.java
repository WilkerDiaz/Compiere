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
/** Generated Model for C_BankAccountDoc
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_BankAccountDoc.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_BankAccountDoc extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BankAccountDoc_ID id
    @param trx transaction
    */
    public X_C_BankAccountDoc (Ctx ctx, int C_BankAccountDoc_ID, Trx trx)
    {
        super (ctx, C_BankAccountDoc_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BankAccountDoc_ID == 0)
        {
            setC_BankAccountDoc_ID (0);
            setC_BankAccount_ID (0);
            setCurrentNext (0);
            setName (null);
            setPaymentRule (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BankAccountDoc (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=455 */
    public static final int Table_ID=455;
    
    /** TableName=C_BankAccountDoc */
    public static final String Table_Name="C_BankAccountDoc";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Bank Account Document.
    @param C_BankAccountDoc_ID Checks, Transfers, etc. */
    public void setC_BankAccountDoc_ID (int C_BankAccountDoc_ID)
    {
        if (C_BankAccountDoc_ID < 1) throw new IllegalArgumentException ("C_BankAccountDoc_ID is mandatory.");
        set_ValueNoCheck ("C_BankAccountDoc_ID", Integer.valueOf(C_BankAccountDoc_ID));
        
    }
    
    /** Get Bank Account Document.
    @return Checks, Transfers, etc. */
    public int getC_BankAccountDoc_ID() 
    {
        return get_ValueAsInt("C_BankAccountDoc_ID");
        
    }
    
    /** Set Bank Account.
    @param C_BankAccount_ID Account at the Bank */
    public void setC_BankAccount_ID (int C_BankAccount_ID)
    {
        if (C_BankAccount_ID < 1) throw new IllegalArgumentException ("C_BankAccount_ID is mandatory.");
        set_ValueNoCheck ("C_BankAccount_ID", Integer.valueOf(C_BankAccount_ID));
        
    }
    
    /** Get Bank Account.
    @return Account at the Bank */
    public int getC_BankAccount_ID() 
    {
        return get_ValueAsInt("C_BankAccount_ID");
        
    }
    
    /** Set Check Print Format.
    @param Check_PrintFormat_ID Print Format for printing Checks */
    public void setCheck_PrintFormat_ID (int Check_PrintFormat_ID)
    {
        if (Check_PrintFormat_ID <= 0) set_Value ("Check_PrintFormat_ID", null);
        else
        set_Value ("Check_PrintFormat_ID", Integer.valueOf(Check_PrintFormat_ID));
        
    }
    
    /** Get Check Print Format.
    @return Print Format for printing Checks */
    public int getCheck_PrintFormat_ID() 
    {
        return get_ValueAsInt("Check_PrintFormat_ID");
        
    }
    
    /** Set Current Next.
    @param CurrentNext The next number to be used */
    public void setCurrentNext (int CurrentNext)
    {
        set_Value ("CurrentNext", Integer.valueOf(CurrentNext));
        
    }
    
    /** Get Current Next.
    @return The next number to be used */
    public int getCurrentNext() 
    {
        return get_ValueAsInt("CurrentNext");
        
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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Cash = B */
    public static final String PAYMENTRULE_Cash = X_Ref__Payment_Rule.CASH.getValue();
    /** Direct Debit = D */
    public static final String PAYMENTRULE_DirectDebit = X_Ref__Payment_Rule.DIRECT_DEBIT.getValue();
    /** Credit Card = K */
    public static final String PAYMENTRULE_CreditCard = X_Ref__Payment_Rule.CREDIT_CARD.getValue();
    /** On Credit = P */
    public static final String PAYMENTRULE_OnCredit = X_Ref__Payment_Rule.ON_CREDIT.getValue();
    /** Check = S */
    public static final String PAYMENTRULE_Check = X_Ref__Payment_Rule.CHECK.getValue();
    /** Direct Deposit = T */
    public static final String PAYMENTRULE_DirectDeposit = X_Ref__Payment_Rule.DIRECT_DEPOSIT.getValue();
    /** Set Payment Method.
    @param PaymentRule How you pay the invoice */
    public void setPaymentRule (String PaymentRule)
    {
        if (PaymentRule == null) throw new IllegalArgumentException ("PaymentRule is mandatory");
        if (!X_Ref__Payment_Rule.isValid(PaymentRule))
        throw new IllegalArgumentException ("PaymentRule Invalid value - " + PaymentRule + " - Reference_ID=195 - B - D - K - P - S - T");
        set_Value ("PaymentRule", PaymentRule);
        
    }
    
    /** Get Payment Method.
    @return How you pay the invoice */
    public String getPaymentRule() 
    {
        return (String)get_Value("PaymentRule");
        
    }
    
    
}
