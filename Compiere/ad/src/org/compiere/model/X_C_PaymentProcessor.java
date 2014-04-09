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
/** Generated Model for C_PaymentProcessor
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_PaymentProcessor.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_PaymentProcessor extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_PaymentProcessor_ID id
    @param trx transaction
    */
    public X_C_PaymentProcessor (Ctx ctx, int C_PaymentProcessor_ID, Trx trx)
    {
        super (ctx, C_PaymentProcessor_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_PaymentProcessor_ID == 0)
        {
            setAcceptAMEX (false);
            setAcceptATM (false);
            setAcceptCheck (false);
            setAcceptCorporate (false);
            setAcceptDiners (false);
            setAcceptDirectDebit (false);
            setAcceptDirectDeposit (false);
            setAcceptDiscover (false);
            setAcceptMC (false);
            setAcceptVisa (false);
            setC_BankAccount_ID (0);
            setC_PaymentProcessor_ID (0);
            setCommission (Env.ZERO);
            setCostPerTrx (Env.ZERO);
            setName (null);
            setRequireVV (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_PaymentProcessor (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=398 */
    public static final int Table_ID=398;
    
    /** TableName=C_PaymentProcessor */
    public static final String Table_Name="C_PaymentProcessor";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Sequence.
    @param AD_Sequence_ID Document Sequence */
    public void setAD_Sequence_ID (int AD_Sequence_ID)
    {
        if (AD_Sequence_ID <= 0) set_Value ("AD_Sequence_ID", null);
        else
        set_Value ("AD_Sequence_ID", Integer.valueOf(AD_Sequence_ID));
        
    }
    
    /** Get Sequence.
    @return Document Sequence */
    public int getAD_Sequence_ID() 
    {
        return get_ValueAsInt("AD_Sequence_ID");
        
    }
    
    /** Set Accept AMEX.
    @param AcceptAMEX Accept American Express Card */
    public void setAcceptAMEX (boolean AcceptAMEX)
    {
        set_Value ("AcceptAMEX", Boolean.valueOf(AcceptAMEX));
        
    }
    
    /** Get Accept AMEX.
    @return Accept American Express Card */
    public boolean isAcceptAMEX() 
    {
        return get_ValueAsBoolean("AcceptAMEX");
        
    }
    
    /** Set Accept ATM.
    @param AcceptATM Accept Bank ATM Card */
    public void setAcceptATM (boolean AcceptATM)
    {
        set_Value ("AcceptATM", Boolean.valueOf(AcceptATM));
        
    }
    
    /** Get Accept ATM.
    @return Accept Bank ATM Card */
    public boolean isAcceptATM() 
    {
        return get_ValueAsBoolean("AcceptATM");
        
    }
    
    /** Set Accept Electronic Check.
    @param AcceptCheck Accept ECheck (Electronic Checks) */
    public void setAcceptCheck (boolean AcceptCheck)
    {
        set_Value ("AcceptCheck", Boolean.valueOf(AcceptCheck));
        
    }
    
    /** Get Accept Electronic Check.
    @return Accept ECheck (Electronic Checks) */
    public boolean isAcceptCheck() 
    {
        return get_ValueAsBoolean("AcceptCheck");
        
    }
    
    /** Set Accept Corporate.
    @param AcceptCorporate Accept Corporate Purchase Cards */
    public void setAcceptCorporate (boolean AcceptCorporate)
    {
        set_Value ("AcceptCorporate", Boolean.valueOf(AcceptCorporate));
        
    }
    
    /** Get Accept Corporate.
    @return Accept Corporate Purchase Cards */
    public boolean isAcceptCorporate() 
    {
        return get_ValueAsBoolean("AcceptCorporate");
        
    }
    
    /** Set Accept Diners.
    @param AcceptDiners Accept Diner's Club */
    public void setAcceptDiners (boolean AcceptDiners)
    {
        set_Value ("AcceptDiners", Boolean.valueOf(AcceptDiners));
        
    }
    
    /** Get Accept Diners.
    @return Accept Diner's Club */
    public boolean isAcceptDiners() 
    {
        return get_ValueAsBoolean("AcceptDiners");
        
    }
    
    /** Set Accept Direct Debit.
    @param AcceptDirectDebit Accept Direct Debits (vendor initiated) */
    public void setAcceptDirectDebit (boolean AcceptDirectDebit)
    {
        set_Value ("AcceptDirectDebit", Boolean.valueOf(AcceptDirectDebit));
        
    }
    
    /** Get Accept Direct Debit.
    @return Accept Direct Debits (vendor initiated) */
    public boolean isAcceptDirectDebit() 
    {
        return get_ValueAsBoolean("AcceptDirectDebit");
        
    }
    
    /** Set Accept Direct Deposit.
    @param AcceptDirectDeposit Accept Direct Deposit (payee initiated) */
    public void setAcceptDirectDeposit (boolean AcceptDirectDeposit)
    {
        set_Value ("AcceptDirectDeposit", Boolean.valueOf(AcceptDirectDeposit));
        
    }
    
    /** Get Accept Direct Deposit.
    @return Accept Direct Deposit (payee initiated) */
    public boolean isAcceptDirectDeposit() 
    {
        return get_ValueAsBoolean("AcceptDirectDeposit");
        
    }
    
    /** Set Accept Discover.
    @param AcceptDiscover Accept Discover Card */
    public void setAcceptDiscover (boolean AcceptDiscover)
    {
        set_Value ("AcceptDiscover", Boolean.valueOf(AcceptDiscover));
        
    }
    
    /** Get Accept Discover.
    @return Accept Discover Card */
    public boolean isAcceptDiscover() 
    {
        return get_ValueAsBoolean("AcceptDiscover");
        
    }
    
    /** Set Accept MasterCard.
    @param AcceptMC Accept Master Card */
    public void setAcceptMC (boolean AcceptMC)
    {
        set_Value ("AcceptMC", Boolean.valueOf(AcceptMC));
        
    }
    
    /** Get Accept MasterCard.
    @return Accept Master Card */
    public boolean isAcceptMC() 
    {
        return get_ValueAsBoolean("AcceptMC");
        
    }
    
    /** Set Accept Visa.
    @param AcceptVisa Accept Visa Cards */
    public void setAcceptVisa (boolean AcceptVisa)
    {
        set_Value ("AcceptVisa", Boolean.valueOf(AcceptVisa));
        
    }
    
    /** Get Accept Visa.
    @return Accept Visa Cards */
    public boolean isAcceptVisa() 
    {
        return get_ValueAsBoolean("AcceptVisa");
        
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
    
    /** Set Payment Processor.
    @param C_PaymentProcessor_ID Payment processor for electronic payments */
    public void setC_PaymentProcessor_ID (int C_PaymentProcessor_ID)
    {
        if (C_PaymentProcessor_ID < 1) throw new IllegalArgumentException ("C_PaymentProcessor_ID is mandatory.");
        set_ValueNoCheck ("C_PaymentProcessor_ID", Integer.valueOf(C_PaymentProcessor_ID));
        
    }
    
    /** Get Payment Processor.
    @return Payment processor for electronic payments */
    public int getC_PaymentProcessor_ID() 
    {
        return get_ValueAsInt("C_PaymentProcessor_ID");
        
    }
    
    /** Set Commission %.
    @param Commission Commission stated as a percentage */
    public void setCommission (java.math.BigDecimal Commission)
    {
        if (Commission == null) throw new IllegalArgumentException ("Commission is mandatory.");
        set_Value ("Commission", Commission);
        
    }
    
    /** Get Commission %.
    @return Commission stated as a percentage */
    public java.math.BigDecimal getCommission() 
    {
        return get_ValueAsBigDecimal("Commission");
        
    }
    
    /** Set Cost per transaction.
    @param CostPerTrx Fixed cost per transaction */
    public void setCostPerTrx (java.math.BigDecimal CostPerTrx)
    {
        if (CostPerTrx == null) throw new IllegalArgumentException ("CostPerTrx is mandatory.");
        set_Value ("CostPerTrx", CostPerTrx);
        
    }
    
    /** Get Cost per transaction.
    @return Fixed cost per transaction */
    public java.math.BigDecimal getCostPerTrx() 
    {
        return get_ValueAsBigDecimal("CostPerTrx");
        
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
    
    /** Set Host Address.
    @param HostAddress Host Address URL or DNS */
    public void setHostAddress (String HostAddress)
    {
        set_Value ("HostAddress", HostAddress);
        
    }
    
    /** Get Host Address.
    @return Host Address URL or DNS */
    public String getHostAddress() 
    {
        return (String)get_Value("HostAddress");
        
    }
    
    /** Set Host port.
    @param HostPort Host Communication Port */
    public void setHostPort (int HostPort)
    {
        set_Value ("HostPort", Integer.valueOf(HostPort));
        
    }
    
    /** Get Host port.
    @return Host Communication Port */
    public int getHostPort() 
    {
        return get_ValueAsInt("HostPort");
        
    }
    
    /** Set Minimum Amt.
    @param MinimumAmt Minimum Amount in Document Currency */
    public void setMinimumAmt (java.math.BigDecimal MinimumAmt)
    {
        set_Value ("MinimumAmt", MinimumAmt);
        
    }
    
    /** Get Minimum Amt.
    @return Minimum Amount in Document Currency */
    public java.math.BigDecimal getMinimumAmt() 
    {
        return get_ValueAsBigDecimal("MinimumAmt");
        
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
    
    /** Set Partner ID.
    @param PartnerID Partner ID or Account for the Payment Processor */
    public void setPartnerID (String PartnerID)
    {
        set_Value ("PartnerID", PartnerID);
        
    }
    
    /** Get Partner ID.
    @return Partner ID or Account for the Payment Processor */
    public String getPartnerID() 
    {
        return (String)get_Value("PartnerID");
        
    }
    
    /** Set Password.
    @param Password Password of any length (case sensitive) */
    public void setPassword (String Password)
    {
        set_Value ("Password", Password);
        
    }
    
    /** Get Password.
    @return Password of any length (case sensitive) */
    public String getPassword() 
    {
        return (String)get_Value("Password");
        
    }
    
    /** Set Payment Processor Class.
    @param PayProcessorClass Payment Processor Java Class */
    public void setPayProcessorClass (String PayProcessorClass)
    {
        set_Value ("PayProcessorClass", PayProcessorClass);
        
    }
    
    /** Get Payment Processor Class.
    @return Payment Processor Java Class */
    public String getPayProcessorClass() 
    {
        return (String)get_Value("PayProcessorClass");
        
    }
    
    /** Set Proxy address.
    @param ProxyAddress Address of your proxy server */
    public void setProxyAddress (String ProxyAddress)
    {
        set_Value ("ProxyAddress", ProxyAddress);
        
    }
    
    /** Get Proxy address.
    @return Address of your proxy server */
    public String getProxyAddress() 
    {
        return (String)get_Value("ProxyAddress");
        
    }
    
    /** Set Proxy logon.
    @param ProxyLogon Logon of your proxy server */
    public void setProxyLogon (String ProxyLogon)
    {
        set_Value ("ProxyLogon", ProxyLogon);
        
    }
    
    /** Get Proxy logon.
    @return Logon of your proxy server */
    public String getProxyLogon() 
    {
        return (String)get_Value("ProxyLogon");
        
    }
    
    /** Set Proxy password.
    @param ProxyPassword Password of your proxy server */
    public void setProxyPassword (String ProxyPassword)
    {
        set_Value ("ProxyPassword", ProxyPassword);
        
    }
    
    /** Get Proxy password.
    @return Password of your proxy server */
    public String getProxyPassword() 
    {
        return (String)get_Value("ProxyPassword");
        
    }
    
    /** Set Proxy port.
    @param ProxyPort Port of your proxy server */
    public void setProxyPort (int ProxyPort)
    {
        set_Value ("ProxyPort", Integer.valueOf(ProxyPort));
        
    }
    
    /** Get Proxy port.
    @return Port of your proxy server */
    public int getProxyPort() 
    {
        return get_ValueAsInt("ProxyPort");
        
    }
    
    /** Set Require CreditCard Verification Code.
    @param RequireVV Require 3/4 digit Credit Verification Code */
    public void setRequireVV (boolean RequireVV)
    {
        set_Value ("RequireVV", Boolean.valueOf(RequireVV));
        
    }
    
    /** Get Require CreditCard Verification Code.
    @return Require 3/4 digit Credit Verification Code */
    public boolean isRequireVV() 
    {
        return get_ValueAsBoolean("RequireVV");
        
    }
    
    /** Set User ID.
    @param UserID User ID or account number */
    public void setUserID (String UserID)
    {
        set_Value ("UserID", UserID);
        
    }
    
    /** Get User ID.
    @return User ID or account number */
    public String getUserID() 
    {
        return (String)get_Value("UserID");
        
    }
    
    /** Set Vendor ID.
    @param VendorID Vendor ID for the Payment Processor */
    public void setVendorID (String VendorID)
    {
        set_Value ("VendorID", VendorID);
        
    }
    
    /** Get Vendor ID.
    @return Vendor ID for the Payment Processor */
    public String getVendorID() 
    {
        return (String)get_Value("VendorID");
        
    }
    
    
}
