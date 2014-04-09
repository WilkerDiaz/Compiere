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
/** Generated Model for T_InvoiceGL
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_InvoiceGL.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_InvoiceGL extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_InvoiceGL_ID id
    @param trx transaction
    */
    public X_T_InvoiceGL (Ctx ctx, int T_InvoiceGL_ID, Trx trx)
    {
        super (ctx, T_InvoiceGL_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_InvoiceGL_ID == 0)
        {
            setAD_PInstance_ID (0);
            setAmtAcctBalance (Env.ZERO);
            setAmtRevalCr (Env.ZERO);
            setAmtRevalCrDiff (Env.ZERO);
            setAmtRevalDr (Env.ZERO);
            setAmtRevalDrDiff (Env.ZERO);
            setAmtSourceBalance (Env.ZERO);
            setC_ConversionTypeReval_ID (0);
            setC_Invoice_ID (0);
            setDateReval (new Timestamp(System.currentTimeMillis()));
            setFact_Acct_ID (0);
            setGrandTotal (Env.ZERO);
            setIsAllCurrencies (false);
            setOpenAmt (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_InvoiceGL (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511671681789L;
    /** Last Updated Timestamp 2008-12-17 12:39:25.0 */
    public static final long updatedMS = 1229546365000L;
    /** AD_Table_ID=803 */
    public static final int Table_ID=803;
    
    /** TableName=T_InvoiceGL */
    public static final String Table_Name="T_InvoiceGL";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Instance.
    @param AD_PInstance_ID Instance of the process */
    public void setAD_PInstance_ID (int AD_PInstance_ID)
    {
        if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
        set_ValueNoCheck ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_PInstance_ID()));
        
    }
    
    /** Receivables & Payables = A */
    public static final String APAR_ReceivablesPayables = X_Ref_APAR.RECEIVABLES_PAYABLES.getValue();
    /** Payables only = P */
    public static final String APAR_PayablesOnly = X_Ref_APAR.PAYABLES_ONLY.getValue();
    /** Receivables only = R */
    public static final String APAR_ReceivablesOnly = X_Ref_APAR.RECEIVABLES_ONLY.getValue();
    /** Set AP - AR.
    @param APAR Include Receivables and/or Payables transactions */
    public void setAPAR (String APAR)
    {
        if (!X_Ref_APAR.isValid(APAR))
        throw new IllegalArgumentException ("APAR Invalid value - " + APAR + " - Reference_ID=332 - A - P - R");
        set_Value ("APAR", APAR);
        
    }
    
    /** Get AP - AR.
    @return Include Receivables and/or Payables transactions */
    public String getAPAR() 
    {
        return (String)get_Value("APAR");
        
    }
    
    /** Set Accounted Balance.
    @param AmtAcctBalance Accounted Balance Amount */
    public void setAmtAcctBalance (java.math.BigDecimal AmtAcctBalance)
    {
        if (AmtAcctBalance == null) throw new IllegalArgumentException ("AmtAcctBalance is mandatory.");
        set_Value ("AmtAcctBalance", AmtAcctBalance);
        
    }
    
    /** Get Accounted Balance.
    @return Accounted Balance Amount */
    public java.math.BigDecimal getAmtAcctBalance() 
    {
        return get_ValueAsBigDecimal("AmtAcctBalance");
        
    }
    
    /** Set Revaluated Amount Cr.
    @param AmtRevalCr Revaluated Cr Amount */
    public void setAmtRevalCr (java.math.BigDecimal AmtRevalCr)
    {
        if (AmtRevalCr == null) throw new IllegalArgumentException ("AmtRevalCr is mandatory.");
        set_Value ("AmtRevalCr", AmtRevalCr);
        
    }
    
    /** Get Revaluated Amount Cr.
    @return Revaluated Cr Amount */
    public java.math.BigDecimal getAmtRevalCr() 
    {
        return get_ValueAsBigDecimal("AmtRevalCr");
        
    }
    
    /** Set Revaluated Difference Cr.
    @param AmtRevalCrDiff Revaluated Cr Amount Difference */
    public void setAmtRevalCrDiff (java.math.BigDecimal AmtRevalCrDiff)
    {
        if (AmtRevalCrDiff == null) throw new IllegalArgumentException ("AmtRevalCrDiff is mandatory.");
        set_Value ("AmtRevalCrDiff", AmtRevalCrDiff);
        
    }
    
    /** Get Revaluated Difference Cr.
    @return Revaluated Cr Amount Difference */
    public java.math.BigDecimal getAmtRevalCrDiff() 
    {
        return get_ValueAsBigDecimal("AmtRevalCrDiff");
        
    }
    
    /** Set Revaluated Amount Dr.
    @param AmtRevalDr Revaluated Dr Amount */
    public void setAmtRevalDr (java.math.BigDecimal AmtRevalDr)
    {
        if (AmtRevalDr == null) throw new IllegalArgumentException ("AmtRevalDr is mandatory.");
        set_Value ("AmtRevalDr", AmtRevalDr);
        
    }
    
    /** Get Revaluated Amount Dr.
    @return Revaluated Dr Amount */
    public java.math.BigDecimal getAmtRevalDr() 
    {
        return get_ValueAsBigDecimal("AmtRevalDr");
        
    }
    
    /** Set Revaluated Difference Dr.
    @param AmtRevalDrDiff Revaluated Dr Amount Difference */
    public void setAmtRevalDrDiff (java.math.BigDecimal AmtRevalDrDiff)
    {
        if (AmtRevalDrDiff == null) throw new IllegalArgumentException ("AmtRevalDrDiff is mandatory.");
        set_Value ("AmtRevalDrDiff", AmtRevalDrDiff);
        
    }
    
    /** Get Revaluated Difference Dr.
    @return Revaluated Dr Amount Difference */
    public java.math.BigDecimal getAmtRevalDrDiff() 
    {
        return get_ValueAsBigDecimal("AmtRevalDrDiff");
        
    }
    
    /** Set Source Balance.
    @param AmtSourceBalance Source Balance Amount */
    public void setAmtSourceBalance (java.math.BigDecimal AmtSourceBalance)
    {
        if (AmtSourceBalance == null) throw new IllegalArgumentException ("AmtSourceBalance is mandatory.");
        set_Value ("AmtSourceBalance", AmtSourceBalance);
        
    }
    
    /** Get Source Balance.
    @return Source Balance Amount */
    public java.math.BigDecimal getAmtSourceBalance() 
    {
        return get_ValueAsBigDecimal("AmtSourceBalance");
        
    }
    
    /** Set Revaluation Conversion Type.
    @param C_ConversionTypeReval_ID Revaluation Currency Conversion Type */
    public void setC_ConversionTypeReval_ID (int C_ConversionTypeReval_ID)
    {
        if (C_ConversionTypeReval_ID < 1) throw new IllegalArgumentException ("C_ConversionTypeReval_ID is mandatory.");
        set_Value ("C_ConversionTypeReval_ID", Integer.valueOf(C_ConversionTypeReval_ID));
        
    }
    
    /** Get Revaluation Conversion Type.
    @return Revaluation Currency Conversion Type */
    public int getC_ConversionTypeReval_ID() 
    {
        return get_ValueAsInt("C_ConversionTypeReval_ID");
        
    }
    
    /** Set Revaluation Document Type.
    @param C_DocTypeReval_ID Document Type for Revaluation Journal */
    public void setC_DocTypeReval_ID (int C_DocTypeReval_ID)
    {
        if (C_DocTypeReval_ID <= 0) set_Value ("C_DocTypeReval_ID", null);
        else
        set_Value ("C_DocTypeReval_ID", Integer.valueOf(C_DocTypeReval_ID));
        
    }
    
    /** Get Revaluation Document Type.
    @return Document Type for Revaluation Journal */
    public int getC_DocTypeReval_ID() 
    {
        return get_ValueAsInt("C_DocTypeReval_ID");
        
    }
    
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID < 1) throw new IllegalArgumentException ("C_Invoice_ID is mandatory.");
        set_ValueNoCheck ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
    }
    
    /** Set Revaluation Date.
    @param DateReval Date of Revaluation */
    public void setDateReval (Timestamp DateReval)
    {
        if (DateReval == null) throw new IllegalArgumentException ("DateReval is mandatory.");
        set_Value ("DateReval", DateReval);
        
    }
    
    /** Get Revaluation Date.
    @return Date of Revaluation */
    public Timestamp getDateReval() 
    {
        return (Timestamp)get_Value("DateReval");
        
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
    
    /** Set Grand Total.
    @param GrandTotal Total amount of document */
    public void setGrandTotal (java.math.BigDecimal GrandTotal)
    {
        if (GrandTotal == null) throw new IllegalArgumentException ("GrandTotal is mandatory.");
        set_Value ("GrandTotal", GrandTotal);
        
    }
    
    /** Get Grand Total.
    @return Total amount of document */
    public java.math.BigDecimal getGrandTotal() 
    {
        return get_ValueAsBigDecimal("GrandTotal");
        
    }
    
    /** Set Include All Currencies.
    @param IsAllCurrencies Report not just foreign currency Invoices */
    public void setIsAllCurrencies (boolean IsAllCurrencies)
    {
        set_Value ("IsAllCurrencies", Boolean.valueOf(IsAllCurrencies));
        
    }
    
    /** Get Include All Currencies.
    @return Report not just foreign currency Invoices */
    public boolean isAllCurrencies() 
    {
        return get_ValueAsBoolean("IsAllCurrencies");
        
    }
    
    /** Set Open Amount.
    @param OpenAmt Open item amount */
    public void setOpenAmt (java.math.BigDecimal OpenAmt)
    {
        if (OpenAmt == null) throw new IllegalArgumentException ("OpenAmt is mandatory.");
        set_Value ("OpenAmt", OpenAmt);
        
    }
    
    /** Get Open Amount.
    @return Open item amount */
    public java.math.BigDecimal getOpenAmt() 
    {
        return get_ValueAsBigDecimal("OpenAmt");
        
    }
    
    /** Set Percent.
    @param PercentGL Gain / Loss Percentage */
    public void setPercentGL (java.math.BigDecimal PercentGL)
    {
        set_Value ("PercentGL", PercentGL);
        
    }
    
    /** Get Percent.
    @return Gain / Loss Percentage */
    public java.math.BigDecimal getPercentGL() 
    {
        return get_ValueAsBigDecimal("PercentGL");
        
    }
    
    
}
