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
/** Generated Model for C_BankStatementLoader
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_BankStatementLoader.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_BankStatementLoader extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BankStatementLoader_ID id
    @param trx transaction
    */
    public X_C_BankStatementLoader (Ctx ctx, int C_BankStatementLoader_ID, Trx trx)
    {
        super (ctx, C_BankStatementLoader_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BankStatementLoader_ID == 0)
        {
            setC_BankAccount_ID (0);
            setC_BankStatementLoader_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BankStatementLoader (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=640 */
    public static final int Table_ID=640;
    
    /** TableName=C_BankStatementLoader */
    public static final String Table_Name="C_BankStatementLoader";
    
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
        set_Value ("AccountNo", AccountNo);
        
    }
    
    /** Get Account No.
    @return Account Number */
    public String getAccountNo() 
    {
        return (String)get_Value("AccountNo");
        
    }
    
    /** Set Branch ID.
    @param BranchID Bank Branch ID */
    public void setBranchID (String BranchID)
    {
        set_Value ("BranchID", BranchID);
        
    }
    
    /** Get Branch ID.
    @return Bank Branch ID */
    public String getBranchID() 
    {
        return (String)get_Value("BranchID");
        
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
    
    /** Set Bank Statement Loader.
    @param C_BankStatementLoader_ID Definition of Bank Statement Loader (SWIFT, OFX) */
    public void setC_BankStatementLoader_ID (int C_BankStatementLoader_ID)
    {
        if (C_BankStatementLoader_ID < 1) throw new IllegalArgumentException ("C_BankStatementLoader_ID is mandatory.");
        set_ValueNoCheck ("C_BankStatementLoader_ID", Integer.valueOf(C_BankStatementLoader_ID));
        
    }
    
    /** Get Bank Statement Loader.
    @return Definition of Bank Statement Loader (SWIFT, OFX) */
    public int getC_BankStatementLoader_ID() 
    {
        return get_ValueAsInt("C_BankStatementLoader_ID");
        
    }
    
    /** Set Date Format.
    @param DateFormat Date format used in the input format */
    public void setDateFormat (String DateFormat)
    {
        set_Value ("DateFormat", DateFormat);
        
    }
    
    /** Get Date Format.
    @return Date format used in the input format */
    public String getDateFormat() 
    {
        return (String)get_Value("DateFormat");
        
    }
    
    /** Set Date Last Run.
    @param DateLastRun Date the process was last run. */
    public void setDateLastRun (Timestamp DateLastRun)
    {
        set_Value ("DateLastRun", DateLastRun);
        
    }
    
    /** Get Date Last Run.
    @return Date the process was last run. */
    public Timestamp getDateLastRun() 
    {
        return (Timestamp)get_Value("DateLastRun");
        
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
    
    /** Set File Name.
    @param FileName Name of the local file or URL */
    public void setFileName (String FileName)
    {
        set_Value ("FileName", FileName);
        
    }
    
    /** Get File Name.
    @return Name of the local file or URL */
    public String getFileName() 
    {
        return (String)get_Value("FileName");
        
    }
    
    /** Set Financial Institution ID.
    @param FinancialInstitutionID The ID of the Financial Institution / Bank */
    public void setFinancialInstitutionID (String FinancialInstitutionID)
    {
        set_Value ("FinancialInstitutionID", FinancialInstitutionID);
        
    }
    
    /** Get Financial Institution ID.
    @return The ID of the Financial Institution / Bank */
    public String getFinancialInstitutionID() 
    {
        return (String)get_Value("FinancialInstitutionID");
        
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
    
    /** Set PIN.
    @param PIN Personal Identification Number */
    public void setPIN (String PIN)
    {
        set_Value ("PIN", PIN);
        
    }
    
    /** Get PIN.
    @return Personal Identification Number */
    public String getPIN() 
    {
        return (String)get_Value("PIN");
        
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
    
    /** Set Statement Loader Class.
    @param StmtLoaderClass Class name of the bank statement loader */
    public void setStmtLoaderClass (String StmtLoaderClass)
    {
        set_Value ("StmtLoaderClass", StmtLoaderClass);
        
    }
    
    /** Get Statement Loader Class.
    @return Class name of the bank statement loader */
    public String getStmtLoaderClass() 
    {
        return (String)get_Value("StmtLoaderClass");
        
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
    
    
}
