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
/** Generated Model for T_TrialBalance
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_TrialBalance.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_TrialBalance extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_TrialBalance_ID id
    @param trx transaction
    */
    public X_T_TrialBalance (Ctx ctx, int T_TrialBalance_ID, Trx trx)
    {
        super (ctx, T_TrialBalance_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_TrialBalance_ID == 0)
        {
            setAD_PInstance_ID (0);
            setAmtAcctBalance (Env.ZERO);
            setAmtAcctCr (Env.ZERO);
            setAmtAcctDr (Env.ZERO);
            setC_AcctSchema_ID (0);
            setDateAcct (new Timestamp(System.currentTimeMillis()));
            setFact_Acct_ID (0);
            setPostingType (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_TrialBalance (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511671718789L;
    /** Last Updated Timestamp 2008-12-17 12:40:02.0 */
    public static final long updatedMS = 1229546402000L;
    /** AD_Table_ID=753 */
    public static final int Table_ID=753;
    
    /** TableName=T_TrialBalance */
    public static final String Table_Name="T_TrialBalance";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Trx Organization.
    @param AD_OrgTrx_ID Performing or initiating organization */
    public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
    {
        if (AD_OrgTrx_ID <= 0) set_ValueNoCheck ("AD_OrgTrx_ID", null);
        else
        set_ValueNoCheck ("AD_OrgTrx_ID", Integer.valueOf(AD_OrgTrx_ID));
        
    }
    
    /** Get Trx Organization.
    @return Performing or initiating organization */
    public int getAD_OrgTrx_ID() 
    {
        return get_ValueAsInt("AD_OrgTrx_ID");
        
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
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID <= 0) set_ValueNoCheck ("AD_Table_ID", null);
        else
        set_ValueNoCheck ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set Asset.
    @param A_Asset_ID Asset used internally or by customers */
    public void setA_Asset_ID (int A_Asset_ID)
    {
        if (A_Asset_ID <= 0) set_ValueNoCheck ("A_Asset_ID", null);
        else
        set_ValueNoCheck ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
        
    }
    
    /** Get Asset.
    @return Asset used internally or by customers */
    public int getA_Asset_ID() 
    {
        return get_ValueAsInt("A_Asset_ID");
        
    }
    
    /** Set Account Key.
    @param AccountValue Key of Account Element */
    public void setAccountValue (String AccountValue)
    {
        set_ValueNoCheck ("AccountValue", AccountValue);
        
    }
    
    /** Get Account Key.
    @return Key of Account Element */
    public String getAccountValue() 
    {
        return (String)get_Value("AccountValue");
        
    }
    
    /** Set Account.
    @param Account_ID Account used */
    public void setAccount_ID (int Account_ID)
    {
        if (Account_ID <= 0) set_ValueNoCheck ("Account_ID", null);
        else
        set_ValueNoCheck ("Account_ID", Integer.valueOf(Account_ID));
        
    }
    
    /** Get Account.
    @return Account used */
    public int getAccount_ID() 
    {
        return get_ValueAsInt("Account_ID");
        
    }
    
    /** Set Accounted Balance.
    @param AmtAcctBalance Accounted Balance Amount */
    public void setAmtAcctBalance (java.math.BigDecimal AmtAcctBalance)
    {
        if (AmtAcctBalance == null) throw new IllegalArgumentException ("AmtAcctBalance is mandatory.");
        set_ValueNoCheck ("AmtAcctBalance", AmtAcctBalance);
        
    }
    
    /** Get Accounted Balance.
    @return Accounted Balance Amount */
    public java.math.BigDecimal getAmtAcctBalance() 
    {
        return get_ValueAsBigDecimal("AmtAcctBalance");
        
    }
    
    /** Set Accounted Credit.
    @param AmtAcctCr Accounted Credit Amount */
    public void setAmtAcctCr (java.math.BigDecimal AmtAcctCr)
    {
        if (AmtAcctCr == null) throw new IllegalArgumentException ("AmtAcctCr is mandatory.");
        set_ValueNoCheck ("AmtAcctCr", AmtAcctCr);
        
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
        if (AmtAcctDr == null) throw new IllegalArgumentException ("AmtAcctDr is mandatory.");
        set_ValueNoCheck ("AmtAcctDr", AmtAcctDr);
        
    }
    
    /** Get Accounted Debit.
    @return Accounted Debit Amount */
    public java.math.BigDecimal getAmtAcctDr() 
    {
        return get_ValueAsBigDecimal("AmtAcctDr");
        
    }
    
    /** Set Source Balance.
    @param AmtSourceBalance Source Balance Amount */
    public void setAmtSourceBalance (java.math.BigDecimal AmtSourceBalance)
    {
        set_ValueNoCheck ("AmtSourceBalance", AmtSourceBalance);
        
    }
    
    /** Get Source Balance.
    @return Source Balance Amount */
    public java.math.BigDecimal getAmtSourceBalance() 
    {
        return get_ValueAsBigDecimal("AmtSourceBalance");
        
    }
    
    /** Set Source Credit.
    @param AmtSourceCr Source Credit Amount */
    public void setAmtSourceCr (java.math.BigDecimal AmtSourceCr)
    {
        set_ValueNoCheck ("AmtSourceCr", AmtSourceCr);
        
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
        set_ValueNoCheck ("AmtSourceDr", AmtSourceDr);
        
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
    
    /** Set Activity.
    @param C_Activity_ID Business Activity */
    public void setC_Activity_ID (int C_Activity_ID)
    {
        if (C_Activity_ID <= 0) set_ValueNoCheck ("C_Activity_ID", null);
        else
        set_ValueNoCheck ("C_Activity_ID", Integer.valueOf(C_Activity_ID));
        
    }
    
    /** Get Activity.
    @return Business Activity */
    public int getC_Activity_ID() 
    {
        return get_ValueAsInt("C_Activity_ID");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_ValueNoCheck ("C_BPartner_ID", null);
        else
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Campaign.
    @param C_Campaign_ID Marketing Campaign */
    public void setC_Campaign_ID (int C_Campaign_ID)
    {
        if (C_Campaign_ID <= 0) set_ValueNoCheck ("C_Campaign_ID", null);
        else
        set_ValueNoCheck ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
    }
    
    /** Get Campaign.
    @return Marketing Campaign */
    public int getC_Campaign_ID() 
    {
        return get_ValueAsInt("C_Campaign_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_ValueNoCheck ("C_Currency_ID", null);
        else
        set_ValueNoCheck ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Location From.
    @param C_LocFrom_ID Location that inventory was moved from */
    public void setC_LocFrom_ID (int C_LocFrom_ID)
    {
        if (C_LocFrom_ID <= 0) set_ValueNoCheck ("C_LocFrom_ID", null);
        else
        set_ValueNoCheck ("C_LocFrom_ID", Integer.valueOf(C_LocFrom_ID));
        
    }
    
    /** Get Location From.
    @return Location that inventory was moved from */
    public int getC_LocFrom_ID() 
    {
        return get_ValueAsInt("C_LocFrom_ID");
        
    }
    
    /** Set Location To.
    @param C_LocTo_ID Location that inventory was moved to */
    public void setC_LocTo_ID (int C_LocTo_ID)
    {
        if (C_LocTo_ID <= 0) set_ValueNoCheck ("C_LocTo_ID", null);
        else
        set_ValueNoCheck ("C_LocTo_ID", Integer.valueOf(C_LocTo_ID));
        
    }
    
    /** Get Location To.
    @return Location that inventory was moved to */
    public int getC_LocTo_ID() 
    {
        return get_ValueAsInt("C_LocTo_ID");
        
    }
    
    /** Set Period.
    @param C_Period_ID Period of the Calendar */
    public void setC_Period_ID (int C_Period_ID)
    {
        if (C_Period_ID <= 0) set_ValueNoCheck ("C_Period_ID", null);
        else
        set_ValueNoCheck ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
    }
    
    /** Get Period.
    @return Period of the Calendar */
    public int getC_Period_ID() 
    {
        return get_ValueAsInt("C_Period_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID <= 0) set_ValueNoCheck ("C_Project_ID", null);
        else
        set_ValueNoCheck ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
    }
    
    /** Set Sales Region.
    @param C_SalesRegion_ID Sales coverage region */
    public void setC_SalesRegion_ID (int C_SalesRegion_ID)
    {
        if (C_SalesRegion_ID <= 0) set_ValueNoCheck ("C_SalesRegion_ID", null);
        else
        set_ValueNoCheck ("C_SalesRegion_ID", Integer.valueOf(C_SalesRegion_ID));
        
    }
    
    /** Get Sales Region.
    @return Sales coverage region */
    public int getC_SalesRegion_ID() 
    {
        return get_ValueAsInt("C_SalesRegion_ID");
        
    }
    
    /** Set Tax.
    @param C_Tax_ID Tax identifier */
    public void setC_Tax_ID (int C_Tax_ID)
    {
        if (C_Tax_ID <= 0) set_ValueNoCheck ("C_Tax_ID", null);
        else
        set_ValueNoCheck ("C_Tax_ID", Integer.valueOf(C_Tax_ID));
        
    }
    
    /** Get Tax.
    @return Tax identifier */
    public int getC_Tax_ID() 
    {
        return get_ValueAsInt("C_Tax_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID <= 0) set_ValueNoCheck ("C_UOM_ID", null);
        else
        set_ValueNoCheck ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Account Date.
    @param DateAcct General Ledger Date */
    public void setDateAcct (Timestamp DateAcct)
    {
        if (DateAcct == null) throw new IllegalArgumentException ("DateAcct is mandatory.");
        set_ValueNoCheck ("DateAcct", DateAcct);
        
    }
    
    /** Get Account Date.
    @return General Ledger Date */
    public Timestamp getDateAcct() 
    {
        return (Timestamp)get_Value("DateAcct");
        
    }
    
    /** Set Transaction Date.
    @param DateTrx Transaction Date */
    public void setDateTrx (Timestamp DateTrx)
    {
        set_ValueNoCheck ("DateTrx", DateTrx);
        
    }
    
    /** Get Transaction Date.
    @return Transaction Date */
    public Timestamp getDateTrx() 
    {
        return (Timestamp)get_Value("DateTrx");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_ValueNoCheck ("Description", Description);
        
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
    
    /** Set Budget.
    @param GL_Budget_ID General Ledger Budget */
    public void setGL_Budget_ID (int GL_Budget_ID)
    {
        if (GL_Budget_ID <= 0) set_ValueNoCheck ("GL_Budget_ID", null);
        else
        set_ValueNoCheck ("GL_Budget_ID", Integer.valueOf(GL_Budget_ID));
        
    }
    
    /** Get Budget.
    @return General Ledger Budget */
    public int getGL_Budget_ID() 
    {
        return get_ValueAsInt("GL_Budget_ID");
        
    }
    
    /** Set GL Category.
    @param GL_Category_ID General Ledger Category */
    public void setGL_Category_ID (int GL_Category_ID)
    {
        if (GL_Category_ID <= 0) set_ValueNoCheck ("GL_Category_ID", null);
        else
        set_ValueNoCheck ("GL_Category_ID", Integer.valueOf(GL_Category_ID));
        
    }
    
    /** Get GL Category.
    @return General Ledger Category */
    public int getGL_Category_ID() 
    {
        return get_ValueAsInt("GL_Category_ID");
        
    }
    
    /** Set Line ID.
    @param Line_ID Transaction line ID (internal) */
    public void setLine_ID (int Line_ID)
    {
        if (Line_ID <= 0) set_ValueNoCheck ("Line_ID", null);
        else
        set_ValueNoCheck ("Line_ID", Integer.valueOf(Line_ID));
        
    }
    
    /** Get Line ID.
    @return Transaction line ID (internal) */
    public int getLine_ID() 
    {
        return get_ValueAsInt("Line_ID");
        
    }
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID <= 0) set_ValueNoCheck ("M_Locator_ID", null);
        else
        set_ValueNoCheck ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_ValueNoCheck ("M_Product_ID", null);
        else
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Reporting Hierarchy.
    @param PA_Hierarchy_ID Optional Reporting Hierarchy - If not selected the default hierarchy trees are used. */
    public void setPA_Hierarchy_ID (int PA_Hierarchy_ID)
    {
        if (PA_Hierarchy_ID <= 0) set_Value ("PA_Hierarchy_ID", null);
        else
        set_Value ("PA_Hierarchy_ID", Integer.valueOf(PA_Hierarchy_ID));
        
    }
    
    /** Get Reporting Hierarchy.
    @return Optional Reporting Hierarchy - If not selected the default hierarchy trees are used. */
    public int getPA_Hierarchy_ID() 
    {
        return get_ValueAsInt("PA_Hierarchy_ID");
        
    }
    
    /** Actual = A */
    public static final String POSTINGTYPE_Actual = X_Ref__Posting_Type.ACTUAL.getValue();
    /** Budget = B */
    public static final String POSTINGTYPE_Budget = X_Ref__Posting_Type.BUDGET.getValue();
    /** Commitment = E */
    public static final String POSTINGTYPE_Commitment = X_Ref__Posting_Type.COMMITMENT.getValue();
    /** Reservation = R */
    public static final String POSTINGTYPE_Reservation = X_Ref__Posting_Type.RESERVATION.getValue();
    /** Statistical = S */
    public static final String POSTINGTYPE_Statistical = X_Ref__Posting_Type.STATISTICAL.getValue();
    /** Set PostingType.
    @param PostingType The type of posted amount for the transaction */
    public void setPostingType (String PostingType)
    {
        if (PostingType == null) throw new IllegalArgumentException ("PostingType is mandatory");
        if (!X_Ref__Posting_Type.isValid(PostingType))
        throw new IllegalArgumentException ("PostingType Invalid value - " + PostingType + " - Reference_ID=125 - A - B - E - R - S");
        set_ValueNoCheck ("PostingType", PostingType);
        
    }
    
    /** Get PostingType.
    @return The type of posted amount for the transaction */
    public String getPostingType() 
    {
        return (String)get_Value("PostingType");
        
    }
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        set_ValueNoCheck ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID <= 0) set_ValueNoCheck ("Record_ID", null);
        else
        set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set User List 1.
    @param User1_ID User defined list element #1 */
    public void setUser1_ID (int User1_ID)
    {
        if (User1_ID <= 0) set_ValueNoCheck ("User1_ID", null);
        else
        set_ValueNoCheck ("User1_ID", Integer.valueOf(User1_ID));
        
    }
    
    /** Get User List 1.
    @return User defined list element #1 */
    public int getUser1_ID() 
    {
        return get_ValueAsInt("User1_ID");
        
    }
    
    /** Set User List 2.
    @param User2_ID User defined list element #2 */
    public void setUser2_ID (int User2_ID)
    {
        if (User2_ID <= 0) set_ValueNoCheck ("User2_ID", null);
        else
        set_ValueNoCheck ("User2_ID", Integer.valueOf(User2_ID));
        
    }
    
    /** Get User List 2.
    @return User defined list element #2 */
    public int getUser2_ID() 
    {
        return get_ValueAsInt("User2_ID");
        
    }
    
    
}
