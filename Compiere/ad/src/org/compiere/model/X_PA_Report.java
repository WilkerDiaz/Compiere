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
/** Generated Model for PA_Report
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_Report.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_Report extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_Report_ID id
    @param trx transaction
    */
    public X_PA_Report (Ctx ctx, int PA_Report_ID, Trx trx)
    {
        super (ctx, PA_Report_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_Report_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setC_Calendar_ID (0);
            setListSources (false);
            setListTrx (false);
            setName (null);
            setPA_ReportColumnSet_ID (0);
            setPA_ReportLineSet_ID (0);
            setPA_Report_ID (0);
            setProcessing (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_Report (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27521257897789L;
    /** Last Updated Timestamp 2009-04-07 11:29:41.0 */
    public static final long updatedMS = 1239132581000L;
    /** AD_Table_ID=445 */
    public static final int Table_ID=445;
    
    /** TableName=PA_Report */
    public static final String Table_Name="PA_Report";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Print Format.
    @param AD_PrintFormat_ID Data Print Format */
    public void setAD_PrintFormat_ID (int AD_PrintFormat_ID)
    {
        if (AD_PrintFormat_ID <= 0) set_Value ("AD_PrintFormat_ID", null);
        else
        set_Value ("AD_PrintFormat_ID", Integer.valueOf(AD_PrintFormat_ID));
        
    }
    
    /** Get Print Format.
    @return Data Print Format */
    public int getAD_PrintFormat_ID() 
    {
        return get_ValueAsInt("AD_PrintFormat_ID");
        
    }
    
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
        set_Value ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
    }
    
    /** Set Calendar.
    @param C_Calendar_ID Accounting Calendar Name */
    public void setC_Calendar_ID (int C_Calendar_ID)
    {
        if (C_Calendar_ID < 1) throw new IllegalArgumentException ("C_Calendar_ID is mandatory.");
        set_Value ("C_Calendar_ID", Integer.valueOf(C_Calendar_ID));
        
    }
    
    /** Get Calendar.
    @return Accounting Calendar Name */
    public int getC_Calendar_ID() 
    {
        return get_ValueAsInt("C_Calendar_ID");
        
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
    
    /** Set List Sources.
    @param ListSources List Report Line Sources */
    public void setListSources (boolean ListSources)
    {
        set_Value ("ListSources", Boolean.valueOf(ListSources));
        
    }
    
    /** Get List Sources.
    @return List Report Line Sources */
    public boolean isListSources() 
    {
        return get_ValueAsBoolean("ListSources");
        
    }
    
    /** Set List Transactions.
    @param ListTrx List the report transactions */
    public void setListTrx (boolean ListTrx)
    {
        set_Value ("ListTrx", Boolean.valueOf(ListTrx));
        
    }
    
    /** Get List Transactions.
    @return List the report transactions */
    public boolean isListTrx() 
    {
        return get_ValueAsBoolean("ListTrx");
        
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
    
    /** Set Report Column Set.
    @param PA_ReportColumnSet_ID Collection of Columns for Report */
    public void setPA_ReportColumnSet_ID (int PA_ReportColumnSet_ID)
    {
        if (PA_ReportColumnSet_ID < 1) throw new IllegalArgumentException ("PA_ReportColumnSet_ID is mandatory.");
        set_Value ("PA_ReportColumnSet_ID", Integer.valueOf(PA_ReportColumnSet_ID));
        
    }
    
    /** Get Report Column Set.
    @return Collection of Columns for Report */
    public int getPA_ReportColumnSet_ID() 
    {
        return get_ValueAsInt("PA_ReportColumnSet_ID");
        
    }
    
    /** Set Report Line Set.
    @param PA_ReportLineSet_ID Report Line Set */
    public void setPA_ReportLineSet_ID (int PA_ReportLineSet_ID)
    {
        if (PA_ReportLineSet_ID < 1) throw new IllegalArgumentException ("PA_ReportLineSet_ID is mandatory.");
        set_Value ("PA_ReportLineSet_ID", Integer.valueOf(PA_ReportLineSet_ID));
        
    }
    
    /** Get Report Line Set.
    @return Report Line Set */
    public int getPA_ReportLineSet_ID() 
    {
        return get_ValueAsInt("PA_ReportLineSet_ID");
        
    }
    
    /** Set Financial Report.
    @param PA_Report_ID Financial Report */
    public void setPA_Report_ID (int PA_Report_ID)
    {
        if (PA_Report_ID < 1) throw new IllegalArgumentException ("PA_Report_ID is mandatory.");
        set_ValueNoCheck ("PA_Report_ID", Integer.valueOf(PA_Report_ID));
        
    }
    
    /** Get Financial Report.
    @return Financial Report */
    public int getPA_Report_ID() 
    {
        return get_ValueAsInt("PA_Report_ID");
        
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
    
    
}
