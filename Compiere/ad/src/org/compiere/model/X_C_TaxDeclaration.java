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
/** Generated Model for C_TaxDeclaration
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_TaxDeclaration.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_TaxDeclaration extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_TaxDeclaration_ID id
    @param trx transaction
    */
    public X_C_TaxDeclaration (Ctx ctx, int C_TaxDeclaration_ID, Trx trx)
    {
        super (ctx, C_TaxDeclaration_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_TaxDeclaration_ID == 0)
        {
            setC_TaxDeclaration_ID (0);
            setDateFrom (new Timestamp(System.currentTimeMillis()));
            setDateTo (new Timestamp(System.currentTimeMillis()));
            setDateTrx (new Timestamp(System.currentTimeMillis()));
            setName (null);
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_TaxDeclaration (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=818 */
    public static final int Table_ID=818;
    
    /** TableName=C_TaxDeclaration */
    public static final String Table_Name="C_TaxDeclaration";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Tax Declaration.
    @param C_TaxDeclaration_ID Define the declaration to the tax authorities */
    public void setC_TaxDeclaration_ID (int C_TaxDeclaration_ID)
    {
        if (C_TaxDeclaration_ID < 1) throw new IllegalArgumentException ("C_TaxDeclaration_ID is mandatory.");
        set_ValueNoCheck ("C_TaxDeclaration_ID", Integer.valueOf(C_TaxDeclaration_ID));
        
    }
    
    /** Get Tax Declaration.
    @return Define the declaration to the tax authorities */
    public int getC_TaxDeclaration_ID() 
    {
        return get_ValueAsInt("C_TaxDeclaration_ID");
        
    }
    
    /** Set Date From.
    @param DateFrom Starting date for a range */
    public void setDateFrom (Timestamp DateFrom)
    {
        if (DateFrom == null) throw new IllegalArgumentException ("DateFrom is mandatory.");
        set_Value ("DateFrom", DateFrom);
        
    }
    
    /** Get Date From.
    @return Starting date for a range */
    public Timestamp getDateFrom() 
    {
        return (Timestamp)get_Value("DateFrom");
        
    }
    
    /** Set Date To.
    @param DateTo End date of a date range */
    public void setDateTo (Timestamp DateTo)
    {
        if (DateTo == null) throw new IllegalArgumentException ("DateTo is mandatory.");
        set_Value ("DateTo", DateTo);
        
    }
    
    /** Get Date To.
    @return End date of a date range */
    public Timestamp getDateTo() 
    {
        return (Timestamp)get_Value("DateTo");
        
    }
    
    /** Set Transaction Date.
    @param DateTrx Transaction Date */
    public void setDateTrx (Timestamp DateTrx)
    {
        if (DateTrx == null) throw new IllegalArgumentException ("DateTrx is mandatory.");
        set_Value ("DateTrx", DateTrx);
        
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
    
    
}
