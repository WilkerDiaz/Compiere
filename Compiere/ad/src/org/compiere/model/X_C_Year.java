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
/** Generated Model for C_Year
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Year.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Year extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Year_ID id
    @param trx transaction
    */
    public X_C_Year (Ctx ctx, int C_Year_ID, Trx trx)
    {
        super (ctx, C_Year_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Year_ID == 0)
        {
            setC_Calendar_ID (0);
            setC_Year_ID (0);
            setFiscalYear (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Year (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=177 */
    public static final int Table_ID=177;
    
    /** TableName=C_Year */
    public static final String Table_Name="C_Year";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Calendar.
    @param C_Calendar_ID Accounting Calendar Name */
    public void setC_Calendar_ID (int C_Calendar_ID)
    {
        if (C_Calendar_ID < 1) throw new IllegalArgumentException ("C_Calendar_ID is mandatory.");
        set_ValueNoCheck ("C_Calendar_ID", Integer.valueOf(C_Calendar_ID));
        
    }
    
    /** Get Calendar.
    @return Accounting Calendar Name */
    public int getC_Calendar_ID() 
    {
        return get_ValueAsInt("C_Calendar_ID");
        
    }
    
    /** Set Year.
    @param C_Year_ID Calendar Year */
    public void setC_Year_ID (int C_Year_ID)
    {
        if (C_Year_ID < 1) throw new IllegalArgumentException ("C_Year_ID is mandatory.");
        set_ValueNoCheck ("C_Year_ID", Integer.valueOf(C_Year_ID));
        
    }
    
    /** Get Year.
    @return Calendar Year */
    public int getC_Year_ID() 
    {
        return get_ValueAsInt("C_Year_ID");
        
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
    
    /** Set Year.
    @param FiscalYear The Fiscal Year */
    public void setFiscalYear (String FiscalYear)
    {
        if (FiscalYear == null) throw new IllegalArgumentException ("FiscalYear is mandatory.");
        set_Value ("FiscalYear", FiscalYear);
        
    }
    
    /** Get Year.
    @return The Fiscal Year */
    public String getFiscalYear() 
    {
        return (String)get_Value("FiscalYear");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getFiscalYear());
        
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
