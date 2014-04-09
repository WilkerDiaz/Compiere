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
/** Generated Model for C_TaxDeclarationAcct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_TaxDeclarationAcct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_TaxDeclarationAcct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_TaxDeclarationAcct_ID id
    @param trx transaction
    */
    public X_C_TaxDeclarationAcct (Ctx ctx, int C_TaxDeclarationAcct_ID, Trx trx)
    {
        super (ctx, C_TaxDeclarationAcct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_TaxDeclarationAcct_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setC_TaxDeclarationAcct_ID (0);
            setC_TaxDeclaration_ID (0);
            setFact_Acct_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_TaxDeclarationAcct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=820 */
    public static final int Table_ID=820;
    
    /** TableName=C_TaxDeclarationAcct */
    public static final String Table_Name="C_TaxDeclarationAcct";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Account.
    @param Account_ID Account used */
    public void setAccount_ID (int Account_ID)
    {
        throw new IllegalArgumentException ("Account_ID is virtual column");
        
    }
    
    /** Get Account.
    @return Account used */
    public int getAccount_ID() 
    {
        return get_ValueAsInt("Account_ID");
        
    }
    
    /** Set Accounted Credit.
    @param AmtAcctCr Accounted Credit Amount */
    public void setAmtAcctCr (java.math.BigDecimal AmtAcctCr)
    {
        throw new IllegalArgumentException ("AmtAcctCr is virtual column");
        
    }
    
    /** Get Accounted Credit.
    @return Accounted Credit Amount */
    public java.math.BigDecimal getAmtAcctCr() 
    {
        return get_ValueAsBigDecimal("AmtAcctCr");
        
    }
    
    /** Set Accounted Debit.
    @param AmtAcctDr Accounted Debit Amount */
    public void setAmtAcctDr (java.math.BigDecimal AmtAcctDr)
    {
        throw new IllegalArgumentException ("AmtAcctDr is virtual column");
        
    }
    
    /** Get Accounted Debit.
    @return Accounted Debit Amount */
    public java.math.BigDecimal getAmtAcctDr() 
    {
        return get_ValueAsBigDecimal("AmtAcctDr");
        
    }
    
    /** Set Source Credit.
    @param AmtSourceCr Source Credit Amount */
    public void setAmtSourceCr (java.math.BigDecimal AmtSourceCr)
    {
        throw new IllegalArgumentException ("AmtSourceCr is virtual column");
        
    }
    
    /** Get Source Credit.
    @return Source Credit Amount */
    public java.math.BigDecimal getAmtSourceCr() 
    {
        return get_ValueAsBigDecimal("AmtSourceCr");
        
    }
    
    /** Set Source Debit.
    @param AmtSourceDr Source Debit Amount */
    public void setAmtSourceDr (java.math.BigDecimal AmtSourceDr)
    {
        throw new IllegalArgumentException ("AmtSourceDr is virtual column");
        
    }
    
    /** Get Source Debit.
    @return Source Debit Amount */
    public java.math.BigDecimal getAmtSourceDr() 
    {
        return get_ValueAsBigDecimal("AmtSourceDr");
        
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
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        throw new IllegalArgumentException ("C_BPartner_ID is virtual column");
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        throw new IllegalArgumentException ("C_Currency_ID is virtual column");
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Tax Declaration Accounting.
    @param C_TaxDeclarationAcct_ID Tax Accounting Reconciliation */
    public void setC_TaxDeclarationAcct_ID (int C_TaxDeclarationAcct_ID)
    {
        if (C_TaxDeclarationAcct_ID < 1) throw new IllegalArgumentException ("C_TaxDeclarationAcct_ID is mandatory.");
        set_ValueNoCheck ("C_TaxDeclarationAcct_ID", Integer.valueOf(C_TaxDeclarationAcct_ID));
        
    }
    
    /** Get Tax Declaration Accounting.
    @return Tax Accounting Reconciliation */
    public int getC_TaxDeclarationAcct_ID() 
    {
        return get_ValueAsInt("C_TaxDeclarationAcct_ID");
        
    }
    
    /** Set Tax Declaration.
    @param C_TaxDeclaration_ID Define the declaration to the tax authorities */
    public void setC_TaxDeclaration_ID (int C_TaxDeclaration_ID)
    {
        if (C_TaxDeclaration_ID < 1) throw new IllegalArgumentException ("C_TaxDeclaration_ID is mandatory.");
        set_ValueNoCheck ("C_TaxDeclaration_ID", Integer.valueOf(C_TaxDeclaration_ID));
        
    }
    
    /** Get Tax Declaration.
    @return Define the declaration to the tax authorities */
    public int getC_TaxDeclaration_ID() 
    {
        return get_ValueAsInt("C_TaxDeclaration_ID");
        
    }
    
    /** Set Tax.
    @param C_Tax_ID Tax identifier */
    public void setC_Tax_ID (int C_Tax_ID)
    {
        throw new IllegalArgumentException ("C_Tax_ID is virtual column");
        
    }
    
    /** Get Tax.
    @return Tax identifier */
    public int getC_Tax_ID() 
    {
        return get_ValueAsInt("C_Tax_ID");
        
    }
    
    /** Set Account Date.
    @param DateAcct General Ledger Date */
    public void setDateAcct (Timestamp DateAcct)
    {
        throw new IllegalArgumentException ("DateAcct is virtual column");
        
    }
    
    /** Get Account Date.
    @return General Ledger Date */
    public Timestamp getDateAcct() 
    {
        return (Timestamp)get_Value("DateAcct");
        
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
    
    /** Set Accounting Fact.
    @param Fact_Acct_ID Accounting Fact */
    public void setFact_Acct_ID (int Fact_Acct_ID)
    {
        if (Fact_Acct_ID < 1) throw new IllegalArgumentException ("Fact_Acct_ID is mandatory.");
        set_ValueNoCheck ("Fact_Acct_ID", Integer.valueOf(Fact_Acct_ID));
        
    }
    
    /** Get Accounting Fact.
    @return Accounting Fact */
    public int getFact_Acct_ID() 
    {
        return get_ValueAsInt("Fact_Acct_ID");
        
    }
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (int Line)
    {
        set_Value ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
    }
    
    
}
