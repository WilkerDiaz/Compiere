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
/** Generated Model for I_ElementValue
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_I_ElementValue.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_I_ElementValue extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_ElementValue_ID id
    @param trx transaction
    */
    public X_I_ElementValue (Ctx ctx, int I_ElementValue_ID, Trx trx)
    {
        super (ctx, I_ElementValue_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_ElementValue_ID == 0)
        {
            setI_ElementValue_ID (0);
            setI_IsImported (null);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_ElementValue (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=534 */
    public static final int Table_ID=534;
    
    /** TableName=I_ElementValue */
    public static final String Table_Name="I_ElementValue";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Column.
    @param AD_Column_ID Column in the table */
    public void setAD_Column_ID (int AD_Column_ID)
    {
        if (AD_Column_ID <= 0) set_Value ("AD_Column_ID", null);
        else
        set_Value ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
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
    
    /** Set Account Element.
    @param C_ElementValue_ID Account Element */
    public void setC_ElementValue_ID (int C_ElementValue_ID)
    {
        if (C_ElementValue_ID <= 0) set_Value ("C_ElementValue_ID", null);
        else
        set_Value ("C_ElementValue_ID", Integer.valueOf(C_ElementValue_ID));
        
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
        if (C_Element_ID <= 0) set_Value ("C_Element_ID", null);
        else
        set_Value ("C_Element_ID", Integer.valueOf(C_Element_ID));
        
    }
    
    /** Get Element.
    @return Accounting Element */
    public int getC_Element_ID() 
    {
        return get_ValueAsInt("C_Element_ID");
        
    }
    
    /** Set Default Account.
    @param Default_Account Name of the Default Account Column */
    public void setDefault_Account (String Default_Account)
    {
        set_Value ("Default_Account", Default_Account);
        
    }
    
    /** Get Default Account.
    @return Name of the Default Account Column */
    public String getDefault_Account() 
    {
        return (String)get_Value("Default_Account");
        
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
    
    /** Set Element Name.
    @param ElementName Name of the Element */
    public void setElementName (String ElementName)
    {
        set_Value ("ElementName", ElementName);
        
    }
    
    /** Get Element Name.
    @return Name of the Element */
    public String getElementName() 
    {
        return (String)get_Value("ElementName");
        
    }
    
    /** Set Import Account.
    @param I_ElementValue_ID Import Account Value */
    public void setI_ElementValue_ID (int I_ElementValue_ID)
    {
        if (I_ElementValue_ID < 1) throw new IllegalArgumentException ("I_ElementValue_ID is mandatory.");
        set_ValueNoCheck ("I_ElementValue_ID", Integer.valueOf(I_ElementValue_ID));
        
    }
    
    /** Get Import Account.
    @return Import Account Value */
    public int getI_ElementValue_ID() 
    {
        return get_ValueAsInt("I_ElementValue_ID");
        
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
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Parent Account.
    @param ParentElementValue_ID The parent (summary) account */
    public void setParentElementValue_ID (int ParentElementValue_ID)
    {
        if (ParentElementValue_ID <= 0) set_Value ("ParentElementValue_ID", null);
        else
        set_Value ("ParentElementValue_ID", Integer.valueOf(ParentElementValue_ID));
        
    }
    
    /** Get Parent Account.
    @return The parent (summary) account */
    public int getParentElementValue_ID() 
    {
        return get_ValueAsInt("ParentElementValue_ID");
        
    }
    
    /** Set Parent Key.
    @param ParentValue Key if the Parent */
    public void setParentValue (String ParentValue)
    {
        set_Value ("ParentValue", ParentValue);
        
    }
    
    /** Get Parent Key.
    @return Key if the Parent */
    public String getParentValue() 
    {
        return (String)get_Value("ParentValue");
        
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
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
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
