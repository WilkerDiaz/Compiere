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
/** Generated Model for C_BP_BankAccount
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_BP_BankAccount.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_BP_BankAccount extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BP_BankAccount_ID id
    @param trx transaction
    */
    public X_C_BP_BankAccount (Ctx ctx, int C_BP_BankAccount_ID, Trx trx)
    {
        super (ctx, C_BP_BankAccount_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BP_BankAccount_ID == 0)
        {
            setC_BP_BankAccount_ID (0);
            setC_BPartner_ID (0);
            setIsACH (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BP_BankAccount (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27500006874789L;
    /** Last Updated Timestamp 2008-08-04 13:25:58.0 */
    public static final long updatedMS = 1217881558000L;
    /** AD_Table_ID=298 */
    public static final int Table_ID=298;
    
    /** TableName=C_BP_BankAccount */
    public static final String Table_Name="C_BP_BankAccount";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Account City.
    @param A_City City or the Credit Card or Account Holder */
    public void setA_City (String A_City)
    {
        set_Value ("A_City", A_City);
        
    }
    
    /** Get Account City.
    @return City or the Credit Card or Account Holder */
    public String getA_City() 
    {
        return (String)get_Value("A_City");
        
    }
    
    /** Set Account Country.
    @param A_Country Country */
    public void setA_Country (String A_Country)
    {
        set_Value ("A_Country", A_Country);
        
    }
    
    /** Get Account Country.
    @return Country */
    public String getA_Country() 
    {
        return (String)get_Value("A_Country");
        
    }
    
    /** Set Account EMail.
    @param A_EMail Email Address */
    public void setA_EMail (String A_EMail)
    {
        set_Value ("A_EMail", A_EMail);
        
    }
    
    /** Get Account EMail.
    @return Email Address */
    public String getA_EMail() 
    {
        return (String)get_Value("A_EMail");
        
    }
    
    /** Set Driver License.
    @param A_Ident_DL Payment Identification - Driver License */
    public void setA_Ident_DL (String A_Ident_DL)
    {
        set_Value ("A_Ident_DL", A_Ident_DL);
        
    }
    
    /** Get Driver License.
    @return Payment Identification - Driver License */
    public String getA_Ident_DL() 
    {
        return (String)get_Value("A_Ident_DL");
        
    }
    
    /** Set Social Security No.
    @param A_Ident_SSN Payment Identification - Social Security No */
    public void setA_Ident_SSN (String A_Ident_SSN)
    {
        set_Value ("A_Ident_SSN", A_Ident_SSN);
        
    }
    
    /** Get Social Security No.
    @return Payment Identification - Social Security No */
    public String getA_Ident_SSN() 
    {
        return (String)get_Value("A_Ident_SSN");
        
    }
    
    /** Set Account Name.
    @param A_Name Name on Credit Card or Account holder */
    public void setA_Name (String A_Name)
    {
        set_Value ("A_Name", A_Name);
        
    }
    
    /** Get Account Name.
    @return Name on Credit Card or Account holder */
    public String getA_Name() 
    {
        return (String)get_Value("A_Name");
        
    }
    
    /** Set Account State.
    @param A_State State of the Credit Card or Account holder */
    public void setA_State (String A_State)
    {
        set_Value ("A_State", A_State);
        
    }
    
    /** Get Account State.
    @return State of the Credit Card or Account holder */
    public String getA_State() 
    {
        return (String)get_Value("A_State");
        
    }
    
    /** Set Account Street.
    @param A_Street Street address of the Credit Card or Account holder */
    public void setA_Street (String A_Street)
    {
        set_Value ("A_Street", A_Street);
        
    }
    
    /** Get Account Street.
    @return Street address of the Credit Card or Account holder */
    public String getA_Street() 
    {
        return (String)get_Value("A_Street");
        
    }
    
    /** Set Account Zip/Postal.
    @param A_Zip Zip Code of the Credit Card or Account Holder */
    public void setA_Zip (String A_Zip)
    {
        set_Value ("A_Zip", A_Zip);
        
    }
    
    /** Get Account Zip/Postal.
    @return Zip Code of the Credit Card or Account Holder */
    public String getA_Zip() 
    {
        return (String)get_Value("A_Zip");
        
    }
    
    /** Set Account No.
    @param AccountNo Account Number */
    public void setAccountNo (String AccountNo)
    {
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
    
    /** Both = B */
    public static final String BPBANKACCTUSE_Both = X_Ref_C_BPartner_BPBankAcctUse.BOTH.getValue();
    /** Direct Debit = D */
    public static final String BPBANKACCTUSE_DirectDebit = X_Ref_C_BPartner_BPBankAcctUse.DIRECT_DEBIT.getValue();
    /** None = N */
    public static final String BPBANKACCTUSE_None = X_Ref_C_BPartner_BPBankAcctUse.NONE.getValue();
    /** Direct Deposit = T */
    public static final String BPBANKACCTUSE_DirectDeposit = X_Ref_C_BPartner_BPBankAcctUse.DIRECT_DEPOSIT.getValue();
    /** Set Account Usage.
    @param BPBankAcctUse Business Partner Bank Account usage */
    public void setBPBankAcctUse (String BPBankAcctUse)
    {
        if (!X_Ref_C_BPartner_BPBankAcctUse.isValid(BPBankAcctUse))
        throw new IllegalArgumentException ("BPBankAcctUse Invalid value - " + BPBankAcctUse + " - Reference_ID=393 - B - D - N - T");
        set_Value ("BPBankAcctUse", BPBankAcctUse);
        
    }
    
    /** Get Account Usage.
    @return Business Partner Bank Account usage */
    public String getBPBankAcctUse() 
    {
        return (String)get_Value("BPBankAcctUse");
        
    }
    
    /** Checking = C */
    public static final String BANKACCOUNTTYPE_Checking = X_Ref_C_Bank_Account_Type.CHECKING.getValue();
    /** Savings = S */
    public static final String BANKACCOUNTTYPE_Savings = X_Ref_C_Bank_Account_Type.SAVINGS.getValue();
    /** Set Bank Account Type.
    @param BankAccountType Bank Account Type */
    public void setBankAccountType (String BankAccountType)
    {
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
    
    /** Set Partner Bank Account.
    @param C_BP_BankAccount_ID Bank Account of the Business Partner */
    public void setC_BP_BankAccount_ID (int C_BP_BankAccount_ID)
    {
        if (C_BP_BankAccount_ID < 1) throw new IllegalArgumentException ("C_BP_BankAccount_ID is mandatory.");
        set_ValueNoCheck ("C_BP_BankAccount_ID", Integer.valueOf(C_BP_BankAccount_ID));
        
    }
    
    /** Get Partner Bank Account.
    @return Bank Account of the Business Partner */
    public int getC_BP_BankAccount_ID() 
    {
        return get_ValueAsInt("C_BP_BankAccount_ID");
        
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
    
    /** Set Bank.
    @param C_Bank_ID Bank */
    public void setC_Bank_ID (int C_Bank_ID)
    {
        if (C_Bank_ID <= 0) set_Value ("C_Bank_ID", null);
        else
        set_Value ("C_Bank_ID", Integer.valueOf(C_Bank_ID));
        
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
    
    /** Set Exp. Month.
    @param CreditCardExpMM Expiry Month */
    public void setCreditCardExpMM (int CreditCardExpMM)
    {
        set_Value ("CreditCardExpMM", Integer.valueOf(CreditCardExpMM));
        
    }
    
    /** Get Exp. Month.
    @return Expiry Month */
    public int getCreditCardExpMM() 
    {
        return get_ValueAsInt("CreditCardExpMM");
        
    }
    
    /** Set Exp. Year.
    @param CreditCardExpYY Expiry Year */
    public void setCreditCardExpYY (int CreditCardExpYY)
    {
        set_Value ("CreditCardExpYY", Integer.valueOf(CreditCardExpYY));
        
    }
    
    /** Get Exp. Year.
    @return Expiry Year */
    public int getCreditCardExpYY() 
    {
        return get_ValueAsInt("CreditCardExpYY");
        
    }
    
    /** Set Number.
    @param CreditCardNumber Credit Card Number */
    public void setCreditCardNumber (String CreditCardNumber)
    {
        set_Value ("CreditCardNumber", CreditCardNumber);
        
    }
    
    /** Get Number.
    @return Credit Card Number */
    public String getCreditCardNumber() 
    {
        return (String)get_Value("CreditCardNumber");
        
    }
    
    /** Amex = A */
    public static final String CREDITCARDTYPE_Amex = X_Ref_C_Payment_CreditCard_Type.AMEX.getValue();
    /** ATM = C */
    public static final String CREDITCARDTYPE_ATM = X_Ref_C_Payment_CreditCard_Type.ATM.getValue();
    /** Diners = D */
    public static final String CREDITCARDTYPE_Diners = X_Ref_C_Payment_CreditCard_Type.DINERS.getValue();
    /** MasterCard = M */
    public static final String CREDITCARDTYPE_MasterCard = X_Ref_C_Payment_CreditCard_Type.MASTER_CARD.getValue();
    /** Discover = N */
    public static final String CREDITCARDTYPE_Discover = X_Ref_C_Payment_CreditCard_Type.DISCOVER.getValue();
    /** Purchase Card = P */
    public static final String CREDITCARDTYPE_PurchaseCard = X_Ref_C_Payment_CreditCard_Type.PURCHASE_CARD.getValue();
    /** Visa = V */
    public static final String CREDITCARDTYPE_Visa = X_Ref_C_Payment_CreditCard_Type.VISA.getValue();
    /** Set Credit Card.
    @param CreditCardType Credit Card (Visa, MC, Amex) */
    public void setCreditCardType (String CreditCardType)
    {
        if (!X_Ref_C_Payment_CreditCard_Type.isValid(CreditCardType))
        throw new IllegalArgumentException ("CreditCardType Invalid value - " + CreditCardType + " - Reference_ID=149 - A - C - D - M - N - P - V");
        set_Value ("CreditCardType", CreditCardType);
        
    }
    
    /** Get Credit Card.
    @return Credit Card (Visa, MC, Amex) */
    public String getCreditCardType() 
    {
        return (String)get_Value("CreditCardType");
        
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
    
    /** Set ACH.
    @param IsACH Automatic Clearing House */
    public void setIsACH (boolean IsACH)
    {
        set_Value ("IsACH", Boolean.valueOf(IsACH));
        
    }
    
    /** Get ACH.
    @return Automatic Clearing House */
    public boolean isACH() 
    {
        return get_ValueAsBoolean("IsACH");
        
    }
    
    /** No Match = N */
    public static final String R_AVSADDR_NoMatch = X_Ref_C_Payment_AVS.NO_MATCH.getValue();
    /** Unavailable = X */
    public static final String R_AVSADDR_Unavailable = X_Ref_C_Payment_AVS.UNAVAILABLE.getValue();
    /** Match = Y */
    public static final String R_AVSADDR_Match = X_Ref_C_Payment_AVS.MATCH.getValue();
    /** Set Address verified.
    @param R_AvsAddr This address has been verified */
    public void setR_AvsAddr (String R_AvsAddr)
    {
        if (!X_Ref_C_Payment_AVS.isValid(R_AvsAddr))
        throw new IllegalArgumentException ("R_AvsAddr Invalid value - " + R_AvsAddr + " - Reference_ID=213 - N - X - Y");
        set_ValueNoCheck ("R_AvsAddr", R_AvsAddr);
        
    }
    
    /** Get Address verified.
    @return This address has been verified */
    public String getR_AvsAddr() 
    {
        return (String)get_Value("R_AvsAddr");
        
    }
    
    /** No Match = N */
    public static final String R_AVSZIP_NoMatch = X_Ref_C_Payment_AVS.NO_MATCH.getValue();
    /** Unavailable = X */
    public static final String R_AVSZIP_Unavailable = X_Ref_C_Payment_AVS.UNAVAILABLE.getValue();
    /** Match = Y */
    public static final String R_AVSZIP_Match = X_Ref_C_Payment_AVS.MATCH.getValue();
    /** Set Zip verified.
    @param R_AvsZip The Zip Code has been verified */
    public void setR_AvsZip (String R_AvsZip)
    {
        if (!X_Ref_C_Payment_AVS.isValid(R_AvsZip))
        throw new IllegalArgumentException ("R_AvsZip Invalid value - " + R_AvsZip + " - Reference_ID=213 - N - X - Y");
        set_ValueNoCheck ("R_AvsZip", R_AvsZip);
        
    }
    
    /** Get Zip verified.
    @return The Zip Code has been verified */
    public String getR_AvsZip() 
    {
        return (String)get_Value("R_AvsZip");
        
    }
    
    /** Set Routing No.
    @param RoutingNo Bank Routing Number */
    public void setRoutingNo (String RoutingNo)
    {
        set_Value ("RoutingNo", RoutingNo);
        
    }
    
    /** Get Routing No.
    @return Bank Routing Number */
    public String getRoutingNo() 
    {
        return (String)get_Value("RoutingNo");
        
    }
    
    
}
