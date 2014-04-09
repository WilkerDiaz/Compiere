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
/** Generated Model for I_GLJournal
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_I_GLJournal.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_I_GLJournal extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_GLJournal_ID id
    @param trx transaction
    */
    public X_I_GLJournal (Ctx ctx, int I_GLJournal_ID, Trx trx)
    {
        super (ctx, I_GLJournal_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_GLJournal_ID == 0)
        {
            setI_GLJournal_ID (0);
            setI_IsImported (null);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_GLJournal (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511144571789L;
    /** Last Updated Timestamp 2008-12-11 10:14:15.0 */
    public static final long updatedMS = 1229019255000L;
    /** AD_Table_ID=599 */
    public static final int Table_ID=599;
    
    /** TableName=I_GLJournal */
    public static final String Table_Name="I_GLJournal";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Document Org.
    @param AD_OrgDoc_ID Document Organization (independent from account organization) */
    public void setAD_OrgDoc_ID (int AD_OrgDoc_ID)
    {
        if (AD_OrgDoc_ID <= 0) set_Value ("AD_OrgDoc_ID", null);
        else
        set_Value ("AD_OrgDoc_ID", Integer.valueOf(AD_OrgDoc_ID));
        
    }
    
    /** Get Document Org.
    @return Document Organization (independent from account organization) */
    public int getAD_OrgDoc_ID() 
    {
        return get_ValueAsInt("AD_OrgDoc_ID");
        
    }
    
    /** Set Trx Organization.
    @param AD_OrgTrx_ID Performing or initiating organization */
    public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
    {
        if (AD_OrgTrx_ID <= 0) set_Value ("AD_OrgTrx_ID", null);
        else
        set_Value ("AD_OrgTrx_ID", Integer.valueOf(AD_OrgTrx_ID));
        
    }
    
    /** Get Trx Organization.
    @return Performing or initiating organization */
    public int getAD_OrgTrx_ID() 
    {
        return get_ValueAsInt("AD_OrgTrx_ID");
        
    }
    
    /** Set Account Key.
    @param AccountValue Key of Account Element */
    public void setAccountValue (String AccountValue)
    {
        set_Value ("AccountValue", AccountValue);
        
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
        if (Account_ID <= 0) set_Value ("Account_ID", null);
        else
        set_Value ("Account_ID", Integer.valueOf(Account_ID));
        
    }
    
    /** Get Account.
    @return Account used */
    public int getAccount_ID() 
    {
        return get_ValueAsInt("Account_ID");
        
    }
    
    /** Set Account Schema Name.
    @param AcctSchemaName Name of the Accounting Schema */
    public void setAcctSchemaName (String AcctSchemaName)
    {
        set_Value ("AcctSchemaName", AcctSchemaName);
        
    }
    
    /** Get Account Schema Name.
    @return Name of the Accounting Schema */
    public String getAcctSchemaName() 
    {
        return (String)get_Value("AcctSchemaName");
        
    }
    
    /** Set Accounted Credit.
    @param AmtAcctCr Accounted Credit Amount */
    public void setAmtAcctCr (java.math.BigDecimal AmtAcctCr)
    {
        set_Value ("AmtAcctCr", AmtAcctCr);
        
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
        set_Value ("AmtAcctDr", AmtAcctDr);
        
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
        set_Value ("AmtSourceCr", AmtSourceCr);
        
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
        set_Value ("AmtSourceDr", AmtSourceDr);
        
    }
    
    /** Get Source Debit.
    @return Source Debit Amount */
    public java.math.BigDecimal getAmtSourceDr() 
    {
        return get_ValueAsBigDecimal("AmtSourceDr");
        
    }
    
    /** Set Business Partner Key.
    @param BPartnerValue Key of the Business Partner */
    public void setBPartnerValue (String BPartnerValue)
    {
        set_Value ("BPartnerValue", BPartnerValue);
        
    }
    
    /** Get Business Partner Key.
    @return Key of the Business Partner */
    public String getBPartnerValue() 
    {
        return (String)get_Value("BPartnerValue");
        
    }
    
    /** Set Batch Description.
    @param BatchDescription Description of the Batch */
    public void setBatchDescription (String BatchDescription)
    {
        set_Value ("BatchDescription", BatchDescription);
        
    }
    
    /** Get Batch Description.
    @return Description of the Batch */
    public String getBatchDescription() 
    {
        return (String)get_Value("BatchDescription");
        
    }
    
    /** Set Batch Document No.
    @param BatchDocumentNo Document Number of the Batch */
    public void setBatchDocumentNo (String BatchDocumentNo)
    {
        set_Value ("BatchDocumentNo", BatchDocumentNo);
        
    }
    
    /** Get Batch Document No.
    @return Document Number of the Batch */
    public String getBatchDocumentNo() 
    {
        return (String)get_Value("BatchDocumentNo");
        
    }
    
    /** Set Budget Name.
    @param BudgetName Alphanumeric identifier for a Budget */
    public void setBudgetName (String BudgetName)
    {
        set_Value ("BudgetName", BudgetName);
        
    }
    
    /** Get Budget Name.
    @return Alphanumeric identifier for a Budget */
    public String getBudgetName() 
    {
        return (String)get_Value("BudgetName");
        
    }
    
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID <= 0) set_Value ("C_AcctSchema_ID", null);
        else
        set_Value ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
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
        if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
        else
        set_Value ("C_Activity_ID", Integer.valueOf(C_Activity_ID));
        
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
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
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
        if (C_Campaign_ID <= 0) set_Value ("C_Campaign_ID", null);
        else
        set_Value ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
    }
    
    /** Get Campaign.
    @return Marketing Campaign */
    public int getC_Campaign_ID() 
    {
        return get_ValueAsInt("C_Campaign_ID");
        
    }
    
    /** Set Rate Type.
    @param C_ConversionType_ID Currency Conversion Rate Type */
    public void setC_ConversionType_ID (int C_ConversionType_ID)
    {
        if (C_ConversionType_ID <= 0) set_Value ("C_ConversionType_ID", null);
        else
        set_Value ("C_ConversionType_ID", Integer.valueOf(C_ConversionType_ID));
        
    }
    
    /** Get Rate Type.
    @return Currency Conversion Rate Type */
    public int getC_ConversionType_ID() 
    {
        return get_ValueAsInt("C_ConversionType_ID");
        
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
    
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID <= 0) set_Value ("C_DocType_ID", null);
        else
        set_Value ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
    }
    
    /** Set Location From.
    @param C_LocFrom_ID Location that inventory was moved from */
    public void setC_LocFrom_ID (int C_LocFrom_ID)
    {
        if (C_LocFrom_ID <= 0) set_Value ("C_LocFrom_ID", null);
        else
        set_Value ("C_LocFrom_ID", Integer.valueOf(C_LocFrom_ID));
        
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
        if (C_LocTo_ID <= 0) set_Value ("C_LocTo_ID", null);
        else
        set_Value ("C_LocTo_ID", Integer.valueOf(C_LocTo_ID));
        
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
        if (C_Period_ID <= 0) set_Value ("C_Period_ID", null);
        else
        set_Value ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
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
        if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
        else
        set_Value ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
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
        if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
        else
        set_Value ("C_SalesRegion_ID", Integer.valueOf(C_SalesRegion_ID));
        
    }
    
    /** Get Sales Region.
    @return Sales coverage region */
    public int getC_SalesRegion_ID() 
    {
        return get_ValueAsInt("C_SalesRegion_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID <= 0) set_Value ("C_UOM_ID", null);
        else
        set_Value ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Combination.
    @param C_ValidCombination_ID Valid Account Combination */
    public void setC_ValidCombination_ID (int C_ValidCombination_ID)
    {
        if (C_ValidCombination_ID <= 0) set_Value ("C_ValidCombination_ID", null);
        else
        set_Value ("C_ValidCombination_ID", Integer.valueOf(C_ValidCombination_ID));
        
    }
    
    /** Get Combination.
    @return Valid Account Combination */
    public int getC_ValidCombination_ID() 
    {
        return get_ValueAsInt("C_ValidCombination_ID");
        
    }
    
    /** Set Category Name.
    @param CategoryName Name of the Category */
    public void setCategoryName (String CategoryName)
    {
        set_Value ("CategoryName", CategoryName);
        
    }
    
    /** Get Category Name.
    @return Name of the Category */
    public String getCategoryName() 
    {
        return (String)get_Value("CategoryName");
        
    }
    
    /** Set Tenant Key.
    @param ClientValue Key of the Tenant */
    public void setClientValue (String ClientValue)
    {
        set_Value ("ClientValue", ClientValue);
        
    }
    
    /** Get Tenant Key.
    @return Key of the Tenant */
    public String getClientValue() 
    {
        return (String)get_Value("ClientValue");
        
    }
    
    /** Set Currency Type Key.
    @param ConversionTypeValue Key value for the Currency Conversion Rate Type */
    public void setConversionTypeValue (String ConversionTypeValue)
    {
        set_Value ("ConversionTypeValue", ConversionTypeValue);
        
    }
    
    /** Get Currency Type Key.
    @return Key value for the Currency Conversion Rate Type */
    public String getConversionTypeValue() 
    {
        return (String)get_Value("ConversionTypeValue");
        
    }
    
    /** Set Rate.
    @param CurrencyRate Currency Conversion Rate */
    public void setCurrencyRate (java.math.BigDecimal CurrencyRate)
    {
        set_Value ("CurrencyRate", CurrencyRate);
        
    }
    
    /** Get Rate.
    @return Currency Conversion Rate */
    public java.math.BigDecimal getCurrencyRate() 
    {
        return get_ValueAsBigDecimal("CurrencyRate");
        
    }
    
    /** Set Account Date.
    @param DateAcct General Ledger Date */
    public void setDateAcct (Timestamp DateAcct)
    {
        set_Value ("DateAcct", DateAcct);
        
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
    
    /** Set Document Type Name.
    @param DocTypeName Name of the Document Type */
    public void setDocTypeName (String DocTypeName)
    {
        set_Value ("DocTypeName", DocTypeName);
        
    }
    
    /** Get Document Type Name.
    @return Name of the Document Type */
    public String getDocTypeName() 
    {
        return (String)get_Value("DocTypeName");
        
    }
    
    /** Set Budget.
    @param GL_Budget_ID General Ledger Budget */
    public void setGL_Budget_ID (int GL_Budget_ID)
    {
        if (GL_Budget_ID <= 0) set_Value ("GL_Budget_ID", null);
        else
        set_Value ("GL_Budget_ID", Integer.valueOf(GL_Budget_ID));
        
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
        if (GL_Category_ID <= 0) set_Value ("GL_Category_ID", null);
        else
        set_Value ("GL_Category_ID", Integer.valueOf(GL_Category_ID));
        
    }
    
    /** Get GL Category.
    @return General Ledger Category */
    public int getGL_Category_ID() 
    {
        return get_ValueAsInt("GL_Category_ID");
        
    }
    
    /** Set Journal Batch.
    @param GL_JournalBatch_ID General Ledger Journal Batch */
    public void setGL_JournalBatch_ID (int GL_JournalBatch_ID)
    {
        if (GL_JournalBatch_ID <= 0) set_Value ("GL_JournalBatch_ID", null);
        else
        set_Value ("GL_JournalBatch_ID", Integer.valueOf(GL_JournalBatch_ID));
        
    }
    
    /** Get Journal Batch.
    @return General Ledger Journal Batch */
    public int getGL_JournalBatch_ID() 
    {
        return get_ValueAsInt("GL_JournalBatch_ID");
        
    }
    
    /** Set Journal Line.
    @param GL_JournalLine_ID General Ledger Journal Line */
    public void setGL_JournalLine_ID (int GL_JournalLine_ID)
    {
        if (GL_JournalLine_ID <= 0) set_Value ("GL_JournalLine_ID", null);
        else
        set_Value ("GL_JournalLine_ID", Integer.valueOf(GL_JournalLine_ID));
        
    }
    
    /** Get Journal Line.
    @return General Ledger Journal Line */
    public int getGL_JournalLine_ID() 
    {
        return get_ValueAsInt("GL_JournalLine_ID");
        
    }
    
    /** Set Journal.
    @param GL_Journal_ID General Ledger Journal */
    public void setGL_Journal_ID (int GL_Journal_ID)
    {
        if (GL_Journal_ID <= 0) set_Value ("GL_Journal_ID", null);
        else
        set_Value ("GL_Journal_ID", Integer.valueOf(GL_Journal_ID));
        
    }
    
    /** Get Journal.
    @return General Ledger Journal */
    public int getGL_Journal_ID() 
    {
        return get_ValueAsInt("GL_Journal_ID");
        
    }
    
    /** Set ISO Currency Code.
    @param ISO_Code Three letter ISO 4217 Code of the Currency */
    public void setISO_Code (String ISO_Code)
    {
        set_Value ("ISO_Code", ISO_Code);
        
    }
    
    /** Get ISO Currency Code.
    @return Three letter ISO 4217 Code of the Currency */
    public String getISO_Code() 
    {
        return (String)get_Value("ISO_Code");
        
    }
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Set Import GL Journal.
    @param I_GLJournal_ID Import General Ledger Journal */
    public void setI_GLJournal_ID (int I_GLJournal_ID)
    {
        if (I_GLJournal_ID < 1) throw new IllegalArgumentException ("I_GLJournal_ID is mandatory.");
        set_ValueNoCheck ("I_GLJournal_ID", Integer.valueOf(I_GLJournal_ID));
        
    }
    
    /** Get Import GL Journal.
    @return Import General Ledger Journal */
    public int getI_GLJournal_ID() 
    {
        return get_ValueAsInt("I_GLJournal_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getI_GLJournal_ID()));
        
    }
    
    /** Error = E */
    public static final String I_ISIMPORTED_Error = X_Ref__IsImported.ERROR.getValue();
    /** No = N */
    public static final String I_ISIMPORTED_No = X_Ref__IsImported.NO.getValue();
    /** Yes = Y */
    public static final String I_ISIMPORTED_Yes = X_Ref__IsImported.YES.getValue();
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (String I_IsImported)
    {
        if (I_IsImported == null) throw new IllegalArgumentException ("I_IsImported is mandatory");
        if (!X_Ref__IsImported.isValid(I_IsImported))
        throw new IllegalArgumentException ("I_IsImported Invalid value - " + I_IsImported + " - Reference_ID=420 - E - N - Y");
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set Create New Batch.
    @param IsCreateNewBatch If selected a new batch is created */
    public void setIsCreateNewBatch (boolean IsCreateNewBatch)
    {
        set_Value ("IsCreateNewBatch", Boolean.valueOf(IsCreateNewBatch));
        
    }
    
    /** Get Create New Batch.
    @return If selected a new batch is created */
    public boolean isCreateNewBatch() 
    {
        return get_ValueAsBoolean("IsCreateNewBatch");
        
    }
    
    /** Set Create New Journal.
    @param IsCreateNewJournal If selected a new journal within the batch is created */
    public void setIsCreateNewJournal (boolean IsCreateNewJournal)
    {
        set_Value ("IsCreateNewJournal", Boolean.valueOf(IsCreateNewJournal));
        
    }
    
    /** Get Create New Journal.
    @return If selected a new journal within the batch is created */
    public boolean isCreateNewJournal() 
    {
        return get_ValueAsBoolean("IsCreateNewJournal");
        
    }
    
    /** Set Journal Document No.
    @param JournalDocumentNo Document number of the Journal */
    public void setJournalDocumentNo (String JournalDocumentNo)
    {
        set_Value ("JournalDocumentNo", JournalDocumentNo);
        
    }
    
    /** Get Journal Document No.
    @return Document number of the Journal */
    public String getJournalDocumentNo() 
    {
        return (String)get_Value("JournalDocumentNo");
        
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
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Trx Org Key.
    @param OrgTrxValue Key of the Transaction Organization */
    public void setOrgTrxValue (String OrgTrxValue)
    {
        set_Value ("OrgTrxValue", OrgTrxValue);
        
    }
    
    /** Get Trx Org Key.
    @return Key of the Transaction Organization */
    public String getOrgTrxValue() 
    {
        return (String)get_Value("OrgTrxValue");
        
    }
    
    /** Set Organization Key.
    @param OrgValue Key of the Organization */
    public void setOrgValue (String OrgValue)
    {
        set_Value ("OrgValue", OrgValue);
        
    }
    
    /** Get Organization Key.
    @return Key of the Organization */
    public String getOrgValue() 
    {
        return (String)get_Value("OrgValue");
        
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
        if (!X_Ref__Posting_Type.isValid(PostingType))
        throw new IllegalArgumentException ("PostingType Invalid value - " + PostingType + " - Reference_ID=125 - A - B - E - R - S");
        set_Value ("PostingType", PostingType);
        
    }
    
    /** Get PostingType.
    @return The type of posted amount for the transaction */
    public String getPostingType() 
    {
        return (String)get_Value("PostingType");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Product Key.
    @param ProductValue Key of the Product */
    public void setProductValue (String ProductValue)
    {
        set_Value ("ProductValue", ProductValue);
        
    }
    
    /** Get Product Key.
    @return Key of the Product */
    public String getProductValue() 
    {
        return (String)get_Value("ProductValue");
        
    }
    
    /** Set Project Key.
    @param ProjectValue Key of the Project */
    public void setProjectValue (String ProjectValue)
    {
        set_Value ("ProjectValue", ProjectValue);
        
    }
    
    /** Get Project Key.
    @return Key of the Project */
    public String getProjectValue() 
    {
        return (String)get_Value("ProjectValue");
        
    }
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        set_Value ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    /** Set SKU.
    @param SKU Stock Keeping Unit */
    public void setSKU (String SKU)
    {
        set_Value ("SKU", SKU);
        
    }
    
    /** Get SKU.
    @return Stock Keeping Unit */
    public String getSKU() 
    {
        return (String)get_Value("SKU");
        
    }
    
    /** Set UPC/EAN.
    @param UPC Bar Code (Universal Product Code or its superset European Article Number) */
    public void setUPC (String UPC)
    {
        set_Value ("UPC", UPC);
        
    }
    
    /** Get UPC/EAN.
    @return Bar Code (Universal Product Code or its superset European Article Number) */
    public String getUPC() 
    {
        return (String)get_Value("UPC");
        
    }
    
    /** Set User List 1.
    @param User1_ID User defined list element #1 */
    public void setUser1_ID (int User1_ID)
    {
        if (User1_ID <= 0) set_Value ("User1_ID", null);
        else
        set_Value ("User1_ID", Integer.valueOf(User1_ID));
        
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
        if (User2_ID <= 0) set_Value ("User2_ID", null);
        else
        set_Value ("User2_ID", Integer.valueOf(User2_ID));
        
    }
    
    /** Get User List 2.
    @return User defined list element #2 */
    public int getUser2_ID() 
    {
        return get_ValueAsInt("User2_ID");
        
    }
    
    
}
