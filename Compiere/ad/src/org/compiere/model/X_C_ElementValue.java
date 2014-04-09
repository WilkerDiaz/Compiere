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
/** Generated Model for C_ElementValue
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_ElementValue.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_ElementValue extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_ElementValue_ID id
    @param trx transaction
    */
    public X_C_ElementValue (Ctx ctx, int C_ElementValue_ID, Trx trx)
    {
        super (ctx, C_ElementValue_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_ElementValue_ID == 0)
        {
            setAccountSign (null);	// N
            setAccountType (null);	// E
            setC_ElementValue_ID (0);
            setC_Element_ID (0);
            setIsSummary (false);
            setName (null);
            setPostActual (true);	// Y
            setPostBudget (true);	// Y
            setPostEncumbrance (true);	// Y
            setPostStatistical (true);	// Y
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_ElementValue (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=188 */
    public static final int Table_ID=188;
    
    /** TableName=C_ElementValue */
    public static final String Table_Name="C_ElementValue";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Credit = C */
    public static final String ACCOUNTSIGN_Credit = X_Ref_C_ElementValue_Account_Sign.CREDIT.getValue();
    /** Debit = D */
    public static final String ACCOUNTSIGN_Debit = X_Ref_C_ElementValue_Account_Sign.DEBIT.getValue();
    /** Natural = N */
    public static final String ACCOUNTSIGN_Natural = X_Ref_C_ElementValue_Account_Sign.NATURAL.getValue();
    /** Set Account Sign.
    @param AccountSign Indicates the Natural Sign of the Account as a Debit or Credit */
    public void setAccountSign (String AccountSign)
    {
        if (AccountSign == null) throw new IllegalArgumentException ("AccountSign is mandatory");
        if (!X_Ref_C_ElementValue_Account_Sign.isValid(AccountSign))
        throw new IllegalArgumentException ("AccountSign Invalid value - " + AccountSign + " - Reference_ID=118 - C - D - N");
        set_Value ("AccountSign", AccountSign);
        
    }
    
    /** Get Account Sign.
    @return Indicates the Natural Sign of the Account as a Debit or Credit */
    public String getAccountSign() 
    {
        return (String)get_Value("AccountSign");
        
    }
    
    /** Asset = A */
    public static final String ACCOUNTTYPE_Asset = X_Ref_C_ElementValue_AccountType.ASSET.getValue();
    /** Expense = E */
    public static final String ACCOUNTTYPE_Expense = X_Ref_C_ElementValue_AccountType.EXPENSE.getValue();
    /** Liability = L */
    public static final String ACCOUNTTYPE_Liability = X_Ref_C_ElementValue_AccountType.LIABILITY.getValue();
    /** Memo = M */
    public static final String ACCOUNTTYPE_Memo = X_Ref_C_ElementValue_AccountType.MEMO.getValue();
    /** Owner's Equity = O */
    public static final String ACCOUNTTYPE_OwnerSEquity = X_Ref_C_ElementValue_AccountType.OWNER_S_EQUITY.getValue();
    /** Revenue = R */
    public static final String ACCOUNTTYPE_Revenue = X_Ref_C_ElementValue_AccountType.REVENUE.getValue();
    /** Set Account Type.
    @param AccountType Indicates the type of account */
    public void setAccountType (String AccountType)
    {
        if (AccountType == null) throw new IllegalArgumentException ("AccountType is mandatory");
        if (!X_Ref_C_ElementValue_AccountType.isValid(AccountType))
        throw new IllegalArgumentException ("AccountType Invalid value - " + AccountType + " - Reference_ID=117 - A - E - L - M - O - R");
        set_Value ("AccountType", AccountType);
        
    }
    
    /** Get Account Type.
    @return Indicates the type of account */
    public String getAccountType() 
    {
        return (String)get_Value("AccountType");
        
    }
    
    /** Set Bank Account.
    @param C_BankAccount_ID Account at the Bank */
    public void setC_BankAccount_ID (int C_BankAccount_ID)
    {
        if (C_BankAccount_ID <= 0) set_Value ("C_BankAccount_ID", null);
        else
        set_Value ("C_BankAccount_ID", Integer.valueOf(C_BankAccount_ID));
        
    }
    
    /** Get Bank Account.
    @return Account at the Bank */
    public int getC_BankAccount_ID() 
    {
        return get_ValueAsInt("C_BankAccount_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
        else
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Account Element.
    @param C_ElementValue_ID Account Element */
    public void setC_ElementValue_ID (int C_ElementValue_ID)
    {
        if (C_ElementValue_ID < 1) throw new IllegalArgumentException ("C_ElementValue_ID is mandatory.");
        set_ValueNoCheck ("C_ElementValue_ID", Integer.valueOf(C_ElementValue_ID));
        
    }
    
    /** Get Account Element.
    @return Account Element */
    public int getC_ElementValue_ID() 
    {
        return get_ValueAsInt("C_ElementValue_ID");
        
    }
    
    /** Set Element.
    @param C_Element_ID Accounting Element */
    public void setC_Element_ID (int C_Element_ID)
    {
        if (C_Element_ID < 1) throw new IllegalArgumentException ("C_Element_ID is mandatory.");
        set_ValueNoCheck ("C_Element_ID", Integer.valueOf(C_Element_ID));
        
    }
    
    /** Get Element.
    @return Accounting Element */
    public int getC_Element_ID() 
    {
        return get_ValueAsInt("C_Element_ID");
        
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
    
    /** Set Bank Account.
    @param IsBankAccount Indicates if this is the Bank Account */
    public void setIsBankAccount (boolean IsBankAccount)
    {
        set_Value ("IsBankAccount", Boolean.valueOf(IsBankAccount));
        
    }
    
    /** Get Bank Account.
    @return Indicates if this is the Bank Account */
    public boolean isBankAccount() 
    {
        return get_ValueAsBoolean("IsBankAccount");
        
    }
    
    /** Set Document Controlled.
    @param IsDocControlled Control account - If an account is controlled by a document, you cannot post manually to it */
    public void setIsDocControlled (boolean IsDocControlled)
    {
        set_Value ("IsDocControlled", Boolean.valueOf(IsDocControlled));
        
    }
    
    /** Get Document Controlled.
    @return Control account - If an account is controlled by a document, you cannot post manually to it */
    public boolean isDocControlled() 
    {
        return get_ValueAsBoolean("IsDocControlled");
        
    }
    
    /** Set Foreign Currency Account.
    @param IsForeignCurrency Balances in foreign currency accounts are held in the nominated currency */
    public void setIsForeignCurrency (boolean IsForeignCurrency)
    {
        set_Value ("IsForeignCurrency", Boolean.valueOf(IsForeignCurrency));
        
    }
    
    /** Get Foreign Currency Account.
    @return Balances in foreign currency accounts are held in the nominated currency */
    public boolean isForeignCurrency() 
    {
        return get_ValueAsBoolean("IsForeignCurrency");
        
    }
    
    /** Set Summary Level.
    @param IsSummary This is a summary entity */
    public void setIsSummary (boolean IsSummary)
    {
        set_Value ("IsSummary", Boolean.valueOf(IsSummary));
        
    }
    
    /** Get Summary Level.
    @return This is a summary entity */
    public boolean isSummary() 
    {
        return get_ValueAsBoolean("IsSummary");
        
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
    
    /** Set Post Actual.
    @param PostActual Actual Values can be posted */
    public void setPostActual (boolean PostActual)
    {
        set_Value ("PostActual", Boolean.valueOf(PostActual));
        
    }
    
    /** Get Post Actual.
    @return Actual Values can be posted */
    public boolean isPostActual() 
    {
        return get_ValueAsBoolean("PostActual");
        
    }
    
    /** Set Post Budget.
    @param PostBudget Budget values can be posted */
    public void setPostBudget (boolean PostBudget)
    {
        set_Value ("PostBudget", Boolean.valueOf(PostBudget));
        
    }
    
    /** Get Post Budget.
    @return Budget values can be posted */
    public boolean isPostBudget() 
    {
        return get_ValueAsBoolean("PostBudget");
        
    }
    
    /** Set Post Encumbrance.
    @param PostEncumbrance Post commitments to this account */
    public void setPostEncumbrance (boolean PostEncumbrance)
    {
        set_Value ("PostEncumbrance", Boolean.valueOf(PostEncumbrance));
        
    }
    
    /** Get Post Encumbrance.
    @return Post commitments to this account */
    public boolean isPostEncumbrance() 
    {
        return get_ValueAsBoolean("PostEncumbrance");
        
    }
    
    /** Set Post Statistical.
    @param PostStatistical Post statistical quantities to this account? */
    public void setPostStatistical (boolean PostStatistical)
    {
        set_Value ("PostStatistical", Boolean.valueOf(PostStatistical));
        
    }
    
    /** Get Post Statistical.
    @return Post statistical quantities to this account? */
    public boolean isPostStatistical() 
    {
        return get_ValueAsBoolean("PostStatistical");
        
    }
    
    /** Set Valid from.
    @param ValidFrom Valid from including this date (first day) */
    public void setValidFrom (Timestamp ValidFrom)
    {
        set_Value ("ValidFrom", ValidFrom);
        
    }
    
    /** Get Valid from.
    @return Valid from including this date (first day) */
    public Timestamp getValidFrom() 
    {
        return (Timestamp)get_Value("ValidFrom");
        
    }
    
    /** Set Valid to.
    @param ValidTo Valid to including this date (last day) */
    public void setValidTo (Timestamp ValidTo)
    {
        set_Value ("ValidTo", ValidTo);
        
    }
    
    /** Get Valid to.
    @return Valid to including this date (last day) */
    public Timestamp getValidTo() 
    {
        return (Timestamp)get_Value("ValidTo");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getValue());
        
    }
    
    
}
