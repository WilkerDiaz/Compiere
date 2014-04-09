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
/** Generated Model for C_BankAccount
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_BankAccount.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_BankAccount extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BankAccount_ID id
    @param trx transaction
    */
    public X_C_BankAccount (Ctx ctx, int C_BankAccount_ID, Trx trx)
    {
        super (ctx, C_BankAccount_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BankAccount_ID == 0)
        {
            setAccountNo (null);
            setBankAccountType (null);
            setC_BankAccount_ID (0);
            setC_Bank_ID (0);
            setC_Currency_ID (0);	// @$C_Currency_ID@
            setCreditLimit (Env.ZERO);
            setCurrentBalance (Env.ZERO);
            setIsDefault (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BankAccount (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=297 */
    public static final int Table_ID=297;
    
    /** TableName=C_BankAccount */
    public static final String Table_Name="C_BankAccount";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Account No.
    @param AccountNo Account Number */
    public void setAccountNo (String AccountNo)
    {
        if (AccountNo == null) throw new IllegalArgumentException ("AccountNo is mandatory.");
        set_Value ("AccountNo", AccountNo);
        
    }
    
    /** Get Account No.
    @return Account Number */
    public String getAccountNo() 
    {
        return (String)get_Value("AccountNo");
        
    }
    
    /** Set BBAN.
    @param BBAN Basic Bank Account Number */
    public void setBBAN (String BBAN)
    {
        set_Value ("BBAN", BBAN);
        
    }
    
    /** Get BBAN.
    @return Basic Bank Account Number */
    public String getBBAN() 
    {
        return (String)get_Value("BBAN");
        
    }
    
    /** Checking = C */
    public static final String BANKACCOUNTTYPE_Checking = X_Ref_C_Bank_Account_Type.CHECKING.getValue();
    /** Savings = S */
    public static final String BANKACCOUNTTYPE_Savings = X_Ref_C_Bank_Account_Type.SAVINGS.getValue();
    /** Set Bank Account Type.
    @param BankAccountType Bank Account Type */
    public void setBankAccountType (String BankAccountType)
    {
        if (BankAccountType == null) throw new IllegalArgumentException ("BankAccountType is mandatory");
        if (!X_Ref_C_Bank_Account_Type.isValid(BankAccountType))
        throw new IllegalArgumentException ("BankAccountType Invalid value - " + BankAccountType + " - Reference_ID=216 - C - S");
        set_Value ("BankAccountType", BankAccountType);
        
    }
    
    /** Get Bank Account Type.
    @return Bank Account Type */
    public String getBankAccountType() 
    {
        return (String)get_Value("BankAccountType");
        
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
    
    /** Set Bank.
    @param C_Bank_ID Bank */
    public void setC_Bank_ID (int C_Bank_ID)
    {
        if (C_Bank_ID < 1) throw new IllegalArgumentException ("C_Bank_ID is mandatory.");
        set_ValueNoCheck ("C_Bank_ID", Integer.valueOf(C_Bank_ID));
        
    }
    
    /** Get Bank.
    @return Bank */
    public int getC_Bank_ID() 
    {
        return get_ValueAsInt("C_Bank_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Bank_ID()));
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Credit limit.
    @param CreditLimit Amount of Credit allowed */
    public void setCreditLimit (java.math.BigDecimal CreditLimit)
    {
        if (CreditLimit == null) throw new IllegalArgumentException ("CreditLimit is mandatory.");
        set_Value ("CreditLimit", CreditLimit);
        
    }
    
    /** Get Credit limit.
    @return Amount of Credit allowed */
    public java.math.BigDecimal getCreditLimit() 
    {
        return get_ValueAsBigDecimal("CreditLimit");
        
    }
    
    /** Set Current Balance.
    @param CurrentBalance Current Balance */
    public void setCurrentBalance (java.math.BigDecimal CurrentBalance)
    {
        if (CurrentBalance == null) throw new IllegalArgumentException ("CurrentBalance is mandatory.");
        set_Value ("CurrentBalance", CurrentBalance);
        
    }
    
    /** Get Current Balance.
    @return Current Balance */
    public java.math.BigDecimal getCurrentBalance() 
    {
        return get_ValueAsBigDecimal("CurrentBalance");
        
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
    
    /** Set IBAN.
    @param IBAN International Bank Account Number */
    public void setIBAN (String IBAN)
    {
        set_Value ("IBAN", IBAN);
        
    }
    
    /** Get IBAN.
    @return International Bank Account Number */
    public String getIBAN() 
    {
        return (String)get_Value("IBAN");
        
    }
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
    }
    
    
}
