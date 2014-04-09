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
/** Generated Model for C_AcctSchema_GL
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_AcctSchema_GL.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_AcctSchema_GL extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_AcctSchema_GL_ID id
    @param trx transaction
    */
    public X_C_AcctSchema_GL (Ctx ctx, int C_AcctSchema_GL_ID, Trx trx)
    {
        super (ctx, C_AcctSchema_GL_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_AcctSchema_GL_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setCommitmentOffset_Acct (0);
            setIncomeSummary_Acct (0);
            setIntercompanyDueFrom_Acct (0);
            setIntercompanyDueTo_Acct (0);
            setPPVOffset_Acct (0);
            setRetainedEarning_Acct (0);
            setUseCurrencyBalancing (false);
            setUseSuspenseBalancing (false);
            setUseSuspenseError (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_AcctSchema_GL (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=266 */
    public static final int Table_ID=266;
    
    /** TableName=C_AcctSchema_GL */
    public static final String Table_Name="C_AcctSchema_GL";
    
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
    
    /** Set Commitment Offset.
    @param CommitmentOffset_Acct Budgetary Commitment Offset Account */
    public void setCommitmentOffset_Acct (int CommitmentOffset_Acct)
    {
        set_Value ("CommitmentOffset_Acct", Integer.valueOf(CommitmentOffset_Acct));
        
    }
    
    /** Get Commitment Offset.
    @return Budgetary Commitment Offset Account */
    public int getCommitmentOffset_Acct() 
    {
        return get_ValueAsInt("CommitmentOffset_Acct");
        
    }
    
    /** Set Currency Balancing Acct.
    @param CurrencyBalancing_Acct Account used when a currency is out of balance */
    public void setCurrencyBalancing_Acct (int CurrencyBalancing_Acct)
    {
        set_Value ("CurrencyBalancing_Acct", Integer.valueOf(CurrencyBalancing_Acct));
        
    }
    
    /** Get Currency Balancing Acct.
    @return Account used when a currency is out of balance */
    public int getCurrencyBalancing_Acct() 
    {
        return get_ValueAsInt("CurrencyBalancing_Acct");
        
    }
    
    /** Set Income Summary Acct.
    @param IncomeSummary_Acct Income Summary Account */
    public void setIncomeSummary_Acct (int IncomeSummary_Acct)
    {
        set_Value ("IncomeSummary_Acct", Integer.valueOf(IncomeSummary_Acct));
        
    }
    
    /** Get Income Summary Acct.
    @return Income Summary Account */
    public int getIncomeSummary_Acct() 
    {
        return get_ValueAsInt("IncomeSummary_Acct");
        
    }
    
    /** Set Intercompany Due From Acct.
    @param IntercompanyDueFrom_Acct Intercompany Due From / Receivables Account */
    public void setIntercompanyDueFrom_Acct (int IntercompanyDueFrom_Acct)
    {
        set_Value ("IntercompanyDueFrom_Acct", Integer.valueOf(IntercompanyDueFrom_Acct));
        
    }
    
    /** Get Intercompany Due From Acct.
    @return Intercompany Due From / Receivables Account */
    public int getIntercompanyDueFrom_Acct() 
    {
        return get_ValueAsInt("IntercompanyDueFrom_Acct");
        
    }
    
    /** Set Intercompany Due To Acct.
    @param IntercompanyDueTo_Acct Intercompany Due To / Payable Account */
    public void setIntercompanyDueTo_Acct (int IntercompanyDueTo_Acct)
    {
        set_Value ("IntercompanyDueTo_Acct", Integer.valueOf(IntercompanyDueTo_Acct));
        
    }
    
    /** Get Intercompany Due To Acct.
    @return Intercompany Due To / Payable Account */
    public int getIntercompanyDueTo_Acct() 
    {
        return get_ValueAsInt("IntercompanyDueTo_Acct");
        
    }
    
    /** Set PPV Offset.
    @param PPVOffset_Acct Purchase Price Variance Offset Account */
    public void setPPVOffset_Acct (int PPVOffset_Acct)
    {
        set_Value ("PPVOffset_Acct", Integer.valueOf(PPVOffset_Acct));
        
    }
    
    /** Get PPV Offset.
    @return Purchase Price Variance Offset Account */
    public int getPPVOffset_Acct() 
    {
        return get_ValueAsInt("PPVOffset_Acct");
        
    }
    
    /** Set Retained Earnings Acct.
    @param RetainedEarning_Acct Retained Earnings Acct */
    public void setRetainedEarning_Acct (int RetainedEarning_Acct)
    {
        set_Value ("RetainedEarning_Acct", Integer.valueOf(RetainedEarning_Acct));
        
    }
    
    /** Get Retained Earnings Acct.
    @return Retained Earnings Acct */
    public int getRetainedEarning_Acct() 
    {
        return get_ValueAsInt("RetainedEarning_Acct");
        
    }
    
    /** Set Suspense Balancing Acct.
    @param SuspenseBalancing_Acct Suspense Balancing Acct */
    public void setSuspenseBalancing_Acct (int SuspenseBalancing_Acct)
    {
        set_Value ("SuspenseBalancing_Acct", Integer.valueOf(SuspenseBalancing_Acct));
        
    }
    
    /** Get Suspense Balancing Acct.
    @return Suspense Balancing Acct */
    public int getSuspenseBalancing_Acct() 
    {
        return get_ValueAsInt("SuspenseBalancing_Acct");
        
    }
    
    /** Set Suspense Error Acct.
    @param SuspenseError_Acct Suspense Error Acct */
    public void setSuspenseError_Acct (int SuspenseError_Acct)
    {
        set_Value ("SuspenseError_Acct", Integer.valueOf(SuspenseError_Acct));
        
    }
    
    /** Get Suspense Error Acct.
    @return Suspense Error Acct */
    public int getSuspenseError_Acct() 
    {
        return get_ValueAsInt("SuspenseError_Acct");
        
    }
    
    /** Set Use Currency Balancing.
    @param UseCurrencyBalancing Use Currency Balancing */
    public void setUseCurrencyBalancing (boolean UseCurrencyBalancing)
    {
        set_Value ("UseCurrencyBalancing", Boolean.valueOf(UseCurrencyBalancing));
        
    }
    
    /** Get Use Currency Balancing.
    @return Use Currency Balancing */
    public boolean isUseCurrencyBalancing() 
    {
        return get_ValueAsBoolean("UseCurrencyBalancing");
        
    }
    
    /** Set Use Suspense Balancing.
    @param UseSuspenseBalancing Use Suspense Balancing */
    public void setUseSuspenseBalancing (boolean UseSuspenseBalancing)
    {
        set_Value ("UseSuspenseBalancing", Boolean.valueOf(UseSuspenseBalancing));
        
    }
    
    /** Get Use Suspense Balancing.
    @return Use Suspense Balancing */
    public boolean isUseSuspenseBalancing() 
    {
        return get_ValueAsBoolean("UseSuspenseBalancing");
        
    }
    
    /** Set Use Suspense Error.
    @param UseSuspenseError Use Suspense Error */
    public void setUseSuspenseError (boolean UseSuspenseError)
    {
        set_Value ("UseSuspenseError", Boolean.valueOf(UseSuspenseError));
        
    }
    
    /** Get Use Suspense Error.
    @return Use Suspense Error */
    public boolean isUseSuspenseError() 
    {
        return get_ValueAsBoolean("UseSuspenseError");
        
    }
    
    
}
