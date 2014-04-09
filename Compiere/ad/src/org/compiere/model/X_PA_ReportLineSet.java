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
/** Generated Model for PA_ReportLineSet
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_ReportLineSet.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_ReportLineSet extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_ReportLineSet_ID id
    @param trx transaction
    */
    public X_PA_ReportLineSet (Ctx ctx, int PA_ReportLineSet_ID, Trx trx)
    {
        super (ctx, PA_ReportLineSet_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_ReportLineSet_ID == 0)
        {
            setName (null);
            setPA_ReportLineSet_ID (0);
            setProcessing (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_ReportLineSet (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27521257537789L;
    /** Last Updated Timestamp 2009-04-07 11:23:41.0 */
    public static final long updatedMS = 1239132221000L;
    /** AD_Table_ID=449 */
    public static final int Table_ID=449;
    
    /** TableName=PA_ReportLineSet */
    public static final String Table_Name="PA_ReportLineSet";
    
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
    
    /** Set Report Line Set.
    @param PA_ReportLineSet_ID Report Line Set */
    public void setPA_ReportLineSet_ID (int PA_ReportLineSet_ID)
    {
        if (PA_ReportLineSet_ID < 1) throw new IllegalArgumentException ("PA_ReportLineSet_ID is mandatory.");
        set_ValueNoCheck ("PA_ReportLineSet_ID", Integer.valueOf(PA_ReportLineSet_ID));
        
    }
    
    /** Get Report Line Set.
    @return Report Line Set */
    public int getPA_ReportLineSet_ID() 
    {
        return get_ValueAsInt("PA_ReportLineSet_ID");
        
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
