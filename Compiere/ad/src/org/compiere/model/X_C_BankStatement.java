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
/** Generated Model for C_BankStatement
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_C_BankStatement.java 9109 2010-07-08 07:30:01Z srajamani $ */
public class X_C_BankStatement extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BankStatement_ID id
    @param trx transaction
    */
    public X_C_BankStatement (Ctx ctx, int C_BankStatement_ID, Trx trx)
    {
        super (ctx, C_BankStatement_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BankStatement_ID == 0)
        {
            setC_BankAccount_ID (0);
            setC_BankStatement_ID (0);
            setDocAction (null);	// CO
            setDocStatus (null);	// DR
            setEndingBalance (Env.ZERO);
            setIsApproved (false);	// N
            setIsManual (true);	// Y
            setName (null);	// @#Date@
            setPosted (false);	// N
            setProcessed (false);	// N
            setStatementDate (new Timestamp(System.currentTimeMillis()));	// @Date@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BankStatement (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27560698783789L;
    /** Last Updated Timestamp 2010-07-08 12:47:47.0 */
    public static final long updatedMS = 1278573467000L;
    /** AD_Table_ID=392 */
    public static final int Table_ID=392;
    
    /** TableName=C_BankStatement */
    public static final String Table_Name="C_BankStatement";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Beginning Balance.
    @param BeginningBalance Balance prior to any transactions */
    public void setBeginningBalance (java.math.BigDecimal BeginningBalance)
    {
        set_Value ("BeginningBalance", BeginningBalance);
        
    }
    
    /** Get Beginning Balance.
    @return Balance prior to any transactions */
    public java.math.BigDecimal getBeginningBalance() 
    {
        return get_ValueAsBigDecimal("BeginningBalance");
        
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
    
    /** Set Bank Statement.
    @param C_BankStatement_ID Bank Statement of account */
    public void setC_BankStatement_ID (int C_BankStatement_ID)
    {
        if (C_BankStatement_ID < 1) throw new IllegalArgumentException ("C_BankStatement_ID is mandatory.");
        set_ValueNoCheck ("C_BankStatement_ID", Integer.valueOf(C_BankStatement_ID));
        
    }
    
    /** Get Bank Statement.
    @return Bank Statement of account */
    public int getC_BankStatement_ID() 
    {
        return get_ValueAsInt("C_BankStatement_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_Bank_ID Bank Account Currency */
    public void setC_Currency_Bank_ID (int C_Currency_Bank_ID)
    {
        throw new IllegalArgumentException ("C_Currency_Bank_ID is virtual column");
        
    }
    
    /** Get Currency.
    @return Bank Account Currency */
    public int getC_Currency_Bank_ID() 
    {
        return get_ValueAsInt("C_Currency_Bank_ID");
        
    }
    
    /** Set Create lines from.
    @param CreateFrom Process which will generate a new document lines based on an existing document */
    public void setCreateFrom (String CreateFrom)
    {
        set_Value ("CreateFrom", CreateFrom);
        
    }
    
    /** Get Create lines from.
    @return Process which will generate a new document lines based on an existing document */
    public String getCreateFrom() 
    {
        return (String)get_Value("CreateFrom");
        
    }
    
    /** Set Current Balance.
    @param CurrentBalance Current Balance */
    public void setCurrentBalance (java.math.BigDecimal CurrentBalance)
    {
        throw new IllegalArgumentException ("CurrentBalance is virtual column");
        
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
    
    /** <None> = -- */
    public static final String DOCACTION_None = X_Ref__Document_Action.NONE.getValue();
    /** Approve = AP */
    public static final String DOCACTION_Approve = X_Ref__Document_Action.APPROVE.getValue();
    /** Close = CL */
    public static final String DOCACTION_Close = X_Ref__Document_Action.CLOSE.getValue();
    /** Complete = CO */
    public static final String DOCACTION_Complete = X_Ref__Document_Action.COMPLETE.getValue();
    /** Invalidate = IN */
    public static final String DOCACTION_Invalidate = X_Ref__Document_Action.INVALIDATE.getValue();
    /** Post = PO */
    public static final String DOCACTION_Post = X_Ref__Document_Action.POST.getValue();
    /** Prepare = PR */
    public static final String DOCACTION_Prepare = X_Ref__Document_Action.PREPARE.getValue();
    /** Reverse - Accrual = RA */
    public static final String DOCACTION_Reverse_Accrual = X_Ref__Document_Action.REVERSE__ACCRUAL.getValue();
    /** Reverse - Correct = RC */
    public static final String DOCACTION_Reverse_Correct = X_Ref__Document_Action.REVERSE__CORRECT.getValue();
    /** Re-activate = RE */
    public static final String DOCACTION_Re_Activate = X_Ref__Document_Action.RE__ACTIVATE.getValue();
    /** Reject = RJ */
    public static final String DOCACTION_Reject = X_Ref__Document_Action.REJECT.getValue();
    /** Void = VO */
    public static final String DOCACTION_Void = X_Ref__Document_Action.VOID.getValue();
    /** Wait Complete = WC */
    public static final String DOCACTION_WaitComplete = X_Ref__Document_Action.WAIT_COMPLETE.getValue();
    /** Unlock = XL */
    public static final String DOCACTION_Unlock = X_Ref__Document_Action.UNLOCK.getValue();
    /** Set Document Action.
    @param DocAction The targeted status of the document */
    public void setDocAction (String DocAction)
    {
        if (DocAction == null) throw new IllegalArgumentException ("DocAction is mandatory");
        if (!X_Ref__Document_Action.isValid(DocAction))
        throw new IllegalArgumentException ("DocAction Invalid value - " + DocAction + " - Reference_ID=135 - -- - AP - CL - CO - IN - PO - PR - RA - RC - RE - RJ - VO - WC - XL");
        set_Value ("DocAction", DocAction);
        
    }
    
    /** Get Document Action.
    @return The targeted status of the document */
    public String getDocAction() 
    {
        return (String)get_Value("DocAction");
        
    }
    
    /** Unknown = ?? */
    public static final String DOCSTATUS_Unknown = X_Ref__Document_Status.UNKNOWN.getValue();
    /** Approved = AP */
    public static final String DOCSTATUS_Approved = X_Ref__Document_Status.APPROVED.getValue();
    /** Closed = CL */
    public static final String DOCSTATUS_Closed = X_Ref__Document_Status.CLOSED.getValue();
    /** Completed = CO */
    public static final String DOCSTATUS_Completed = X_Ref__Document_Status.COMPLETED.getValue();
    /** Drafted = DR */
    public static final String DOCSTATUS_Drafted = X_Ref__Document_Status.DRAFTED.getValue();
    /** Invalid = IN */
    public static final String DOCSTATUS_Invalid = X_Ref__Document_Status.INVALID.getValue();
    /** In Progress = IP */
    public static final String DOCSTATUS_InProgress = X_Ref__Document_Status.IN_PROGRESS.getValue();
    /** Not Approved = NA */
    public static final String DOCSTATUS_NotApproved = X_Ref__Document_Status.NOT_APPROVED.getValue();
    /** Reversed = RE */
    public static final String DOCSTATUS_Reversed = X_Ref__Document_Status.REVERSED.getValue();
    /** Voided = VO */
    public static final String DOCSTATUS_Voided = X_Ref__Document_Status.VOIDED.getValue();
    /** Waiting Confirmation = WC */
    public static final String DOCSTATUS_WaitingConfirmation = X_Ref__Document_Status.WAITING_CONFIRMATION.getValue();
    /** Waiting Payment = WP */
    public static final String DOCSTATUS_WaitingPayment = X_Ref__Document_Status.WAITING_PAYMENT.getValue();
    /** Set Document Status.
    @param DocStatus The current status of the document */
    public void setDocStatus (String DocStatus)
    {
        if (DocStatus == null) throw new IllegalArgumentException ("DocStatus is mandatory");
        if (!X_Ref__Document_Status.isValid(DocStatus))
        throw new IllegalArgumentException ("DocStatus Invalid value - " + DocStatus + " - Reference_ID=131 - ?? - AP - CL - CO - DR - IN - IP - NA - RE - VO - WC - WP");
        set_Value ("DocStatus", DocStatus);
        
    }
    
    /** Get Document Status.
    @return The current status of the document */
    public String getDocStatus() 
    {
        return (String)get_Value("DocStatus");
        
    }
    
    /** Set EFT Statement Date.
    @param EftStatementDate Electronic Funds Transfer Statement Date */
    public void setEftStatementDate (Timestamp EftStatementDate)
    {
        set_Value ("EftStatementDate", EftStatementDate);
        
    }
    
    /** Get EFT Statement Date.
    @return Electronic Funds Transfer Statement Date */
    public Timestamp getEftStatementDate() 
    {
        return (Timestamp)get_Value("EftStatementDate");
        
    }
    
    /** Set EFT Statement Reference.
    @param EftStatementReference Electronic Funds Transfer Statement Reference */
    public void setEftStatementReference (String EftStatementReference)
    {
        set_Value ("EftStatementReference", EftStatementReference);
        
    }
    
    /** Get EFT Statement Reference.
    @return Electronic Funds Transfer Statement Reference */
    public String getEftStatementReference() 
    {
        return (String)get_Value("EftStatementReference");
        
    }
    
    /** Set Ending Balance.
    @param EndingBalance Ending or Closing Balance */
    public void setEndingBalance (java.math.BigDecimal EndingBalance)
    {
        if (EndingBalance == null) throw new IllegalArgumentException ("EndingBalance is mandatory.");
        set_Value ("EndingBalance", EndingBalance);
        
    }
    
    /** Get Ending Balance.
    @return Ending or Closing Balance */
    public java.math.BigDecimal getEndingBalance() 
    {
        return get_ValueAsBigDecimal("EndingBalance");
        
    }
    
    /** Set Approved.
    @param IsApproved Indicates if this document requires approval */
    public void setIsApproved (boolean IsApproved)
    {
        set_Value ("IsApproved", Boolean.valueOf(IsApproved));
        
    }
    
    /** Get Approved.
    @return Indicates if this document requires approval */
    public boolean isApproved() 
    {
        return get_ValueAsBoolean("IsApproved");
        
    }
    
    /** Set Manual.
    @param IsManual This is a manual process or entry */
    public void setIsManual (boolean IsManual)
    {
        set_Value ("IsManual", Boolean.valueOf(IsManual));
        
    }
    
    /** Get Manual.
    @return This is a manual process or entry */
    public boolean isManual() 
    {
        return get_ValueAsBoolean("IsManual");
        
    }
    
    /** Set Match Statement.
    @param MatchStatement Match Statement */
    public void setMatchStatement (String MatchStatement)
    {
        set_Value ("MatchStatement", MatchStatement);
        
    }
    
    /** Get Match Statement.
    @return Match Statement */
    public String getMatchStatement() 
    {
        return (String)get_Value("MatchStatement");
        
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
    
    /** Set Posted.
    @param Posted Posting status */
    public void setPosted (boolean Posted)
    {
        set_ValueNoCheck ("Posted", Boolean.valueOf(Posted));
        
    }
    
    /** Get Posted.
    @return Posting status */
    public boolean isPosted() 
    {
        return get_ValueAsBoolean("Posted");
        
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
    
    /** Set Statement date.
    @param StatementDate Date of the statement */
    public void setStatementDate (Timestamp StatementDate)
    {
        if (StatementDate == null) throw new IllegalArgumentException ("StatementDate is mandatory.");
        set_Value ("StatementDate", StatementDate);
        
    }
    
    /** Get Statement date.
    @return Date of the statement */
    public Timestamp getStatementDate() 
    {
        return (Timestamp)get_Value("StatementDate");
        
    }
    
    /** Set Statement difference.
    @param StatementDifference Difference between statement ending balance and actual ending balance */
    public void setStatementDifference (java.math.BigDecimal StatementDifference)
    {
        set_Value ("StatementDifference", StatementDifference);
        
    }
    
    /** Get Statement difference.
    @return Difference between statement ending balance and actual ending balance */
    public java.math.BigDecimal getStatementDifference() 
    {
        return get_ValueAsBigDecimal("StatementDifference");
        
    }
    
    
}
