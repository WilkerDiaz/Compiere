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
/** Generated Model for C_Recurring
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Recurring.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Recurring extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Recurring_ID id
    @param trx transaction
    */
    public X_C_Recurring (Ctx ctx, int C_Recurring_ID, Trx trx)
    {
        super (ctx, C_Recurring_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Recurring_ID == 0)
        {
            setC_Recurring_ID (0);
            setFrequencyType (null);	// M
            setName (null);
            setRecurringType (null);
            setRunsMax (0);
            setRunsRemaining (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Recurring (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=574 */
    public static final int Table_ID=574;
    
    /** TableName=C_Recurring */
    public static final String Table_Name="C_Recurring";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID <= 0) set_Value ("C_Invoice_ID", null);
        else
        set_Value ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
    }
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_Value ("C_Order_ID", null);
        else
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Payment.
    @param C_Payment_ID Payment identifier */
    public void setC_Payment_ID (int C_Payment_ID)
    {
        if (C_Payment_ID <= 0) set_Value ("C_Payment_ID", null);
        else
        set_Value ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
    }
    
    /** Get Payment.
    @return Payment identifier */
    public int getC_Payment_ID() 
    {
        return get_ValueAsInt("C_Payment_ID");
        
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
    
    /** Set Recurring.
    @param C_Recurring_ID Recurring Document */
    public void setC_Recurring_ID (int C_Recurring_ID)
    {
        if (C_Recurring_ID < 1) throw new IllegalArgumentException ("C_Recurring_ID is mandatory.");
        set_ValueNoCheck ("C_Recurring_ID", Integer.valueOf(C_Recurring_ID));
        
    }
    
    /** Get Recurring.
    @return Recurring Document */
    public int getC_Recurring_ID() 
    {
        return get_ValueAsInt("C_Recurring_ID");
        
    }
    
    /** Set Date Last Run.
    @param DateLastRun Date the process was last run. */
    public void setDateLastRun (Timestamp DateLastRun)
    {
        set_ValueNoCheck ("DateLastRun", DateLastRun);
        
    }
    
    /** Get Date Last Run.
    @return Date the process was last run. */
    public Timestamp getDateLastRun() 
    {
        return (Timestamp)get_Value("DateLastRun");
        
    }
    
    /** Set Date Next Run.
    @param DateNextRun Date the process will run next */
    public void setDateNextRun (Timestamp DateNextRun)
    {
        set_Value ("DateNextRun", DateNextRun);
        
    }
    
    /** Get Date Next Run.
    @return Date the process will run next */
    public Timestamp getDateNextRun() 
    {
        return (Timestamp)get_Value("DateNextRun");
        
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
    
    /** Set Frequency.
    @param Frequency Frequency of events */
    public void setFrequency (int Frequency)
    {
        set_Value ("Frequency", Integer.valueOf(Frequency));
        
    }
    
    /** Get Frequency.
    @return Frequency of events */
    public int getFrequency() 
    {
        return get_ValueAsInt("Frequency");
        
    }
    
    /** Daily = D */
    public static final String FREQUENCYTYPE_Daily = X_Ref_C_Recurring_Frequency.DAILY.getValue();
    /** Monthly = M */
    public static final String FREQUENCYTYPE_Monthly = X_Ref_C_Recurring_Frequency.MONTHLY.getValue();
    /** Quarterly = Q */
    public static final String FREQUENCYTYPE_Quarterly = X_Ref_C_Recurring_Frequency.QUARTERLY.getValue();
    /** Weekly = W */
    public static final String FREQUENCYTYPE_Weekly = X_Ref_C_Recurring_Frequency.WEEKLY.getValue();
    /** Set Frequency Type.
    @param FrequencyType Frequency of event */
    public void setFrequencyType (String FrequencyType)
    {
        if (FrequencyType == null) throw new IllegalArgumentException ("FrequencyType is mandatory");
        if (!X_Ref_C_Recurring_Frequency.isValid(FrequencyType))
        throw new IllegalArgumentException ("FrequencyType Invalid value - " + FrequencyType + " - Reference_ID=283 - D - M - Q - W");
        set_Value ("FrequencyType", FrequencyType);
        
    }
    
    /** Get Frequency Type.
    @return Frequency of event */
    public String getFrequencyType() 
    {
        return (String)get_Value("FrequencyType");
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
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
    
    /** GL Journal = G */
    public static final String RECURRINGTYPE_GLJournal = X_Ref_C_Recurring_Type.GL_JOURNAL.getValue();
    /** Invoice = I */
    public static final String RECURRINGTYPE_Invoice = X_Ref_C_Recurring_Type.INVOICE.getValue();
    /** Project = J */
    public static final String RECURRINGTYPE_Project = X_Ref_C_Recurring_Type.PROJECT.getValue();
    /** Order = O */
    public static final String RECURRINGTYPE_Order = X_Ref_C_Recurring_Type.ORDER.getValue();
    /** Set Recurring Type.
    @param RecurringType Type of Recurring Document */
    public void setRecurringType (String RecurringType)
    {
        if (RecurringType == null) throw new IllegalArgumentException ("RecurringType is mandatory");
        if (!X_Ref_C_Recurring_Type.isValid(RecurringType))
        throw new IllegalArgumentException ("RecurringType Invalid value - " + RecurringType + " - Reference_ID=282 - G - I - J - O");
        set_Value ("RecurringType", RecurringType);
        
    }
    
    /** Get Recurring Type.
    @return Type of Recurring Document */
    public String getRecurringType() 
    {
        return (String)get_Value("RecurringType");
        
    }
    
    /** Set Maximum Runs.
    @param RunsMax Number of recurring runs */
    public void setRunsMax (int RunsMax)
    {
        set_Value ("RunsMax", Integer.valueOf(RunsMax));
        
    }
    
    /** Get Maximum Runs.
    @return Number of recurring runs */
    public int getRunsMax() 
    {
        return get_ValueAsInt("RunsMax");
        
    }
    
    /** Set Remaining Runs.
    @param RunsRemaining Number of recurring runs remaining */
    public void setRunsRemaining (int RunsRemaining)
    {
        set_ValueNoCheck ("RunsRemaining", Integer.valueOf(RunsRemaining));
        
    }
    
    /** Get Remaining Runs.
    @return Number of recurring runs remaining */
    public int getRunsRemaining() 
    {
        return get_ValueAsInt("RunsRemaining");
        
    }
    
    
}
