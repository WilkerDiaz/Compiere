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
/** Generated Model for C_Currency
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Currency.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Currency extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Currency_ID id
    @param trx transaction
    */
    public X_C_Currency (Ctx ctx, int C_Currency_ID, Trx trx)
    {
        super (ctx, C_Currency_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Currency_ID == 0)
        {
            setC_Currency_ID (0);
            setCostingPrecision (0);	// 4
            setDescription (null);
            setISO_Code (null);
            setIsEMUMember (false);	// N
            setIsEuro (false);	// N
            setStdPrecision (0);	// 2
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Currency (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=141 */
    public static final int Table_ID=141;
    
    /** TableName=C_Currency */
    public static final String Table_Name="C_Currency";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_ValueNoCheck ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Costing Precision.
    @param CostingPrecision Rounding used costing calculations */
    public void setCostingPrecision (int CostingPrecision)
    {
        set_Value ("CostingPrecision", Integer.valueOf(CostingPrecision));
        
    }
    
    /** Get Costing Precision.
    @return Rounding used costing calculations */
    public int getCostingPrecision() 
    {
        return get_ValueAsInt("CostingPrecision");
        
    }
    
    /** Set Symbol.
    @param CurSymbol Symbol of the currency (opt used for printing only) */
    public void setCurSymbol (String CurSymbol)
    {
        set_Value ("CurSymbol", CurSymbol);
        
    }
    
    /** Get Symbol.
    @return Symbol of the currency (opt used for printing only) */
    public String getCurSymbol() 
    {
        return (String)get_Value("CurSymbol");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        if (Description == null) throw new IllegalArgumentException ("Description is mandatory.");
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set EMU Entry Date.
    @param EMUEntryDate Date when the currency joined / will join the EMU */
    public void setEMUEntryDate (Timestamp EMUEntryDate)
    {
        set_Value ("EMUEntryDate", EMUEntryDate);
        
    }
    
    /** Get EMU Entry Date.
    @return Date when the currency joined / will join the EMU */
    public Timestamp getEMUEntryDate() 
    {
        return (Timestamp)get_Value("EMUEntryDate");
        
    }
    
    /** Set EMU Rate.
    @param EMURate Official rate to the Euro */
    public void setEMURate (java.math.BigDecimal EMURate)
    {
        set_Value ("EMURate", EMURate);
        
    }
    
    /** Get EMU Rate.
    @return Official rate to the Euro */
    public java.math.BigDecimal getEMURate() 
    {
        return get_ValueAsBigDecimal("EMURate");
        
    }
    
    /** Set ISO Currency Code.
    @param ISO_Code Three letter ISO 4217 Code of the Currency */
    public void setISO_Code (String ISO_Code)
    {
        if (ISO_Code == null) throw new IllegalArgumentException ("ISO_Code is mandatory.");
        set_Value ("ISO_Code", ISO_Code);
        
    }
    
    /** Get ISO Currency Code.
    @return Three letter ISO 4217 Code of the Currency */
    public String getISO_Code() 
    {
        return (String)get_Value("ISO_Code");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getISO_Code());
        
    }
    
    /** Set EMU Member.
    @param IsEMUMember This currency is member if the European Monetary Union */
    public void setIsEMUMember (boolean IsEMUMember)
    {
        set_Value ("IsEMUMember", Boolean.valueOf(IsEMUMember));
        
    }
    
    /** Get EMU Member.
    @return This currency is member if the European Monetary Union */
    public boolean isEMUMember() 
    {
        return get_ValueAsBoolean("IsEMUMember");
        
    }
    
    /** Set The Euro Currency.
    @param IsEuro This currency is the Euro */
    public void setIsEuro (boolean IsEuro)
    {
        set_Value ("IsEuro", Boolean.valueOf(IsEuro));
        
    }
    
    /** Get The Euro Currency.
    @return This currency is the Euro */
    public boolean isEuro() 
    {
        return get_ValueAsBoolean("IsEuro");
        
    }
    
    /** Set Standard Precision.
    @param StdPrecision Rule for rounding calculated amounts */
    public void setStdPrecision (int StdPrecision)
    {
        set_Value ("StdPrecision", Integer.valueOf(StdPrecision));
        
    }
    
    /** Get Standard Precision.
    @return Rule for rounding calculated amounts */
    public int getStdPrecision() 
    {
        return get_ValueAsInt("StdPrecision");
        
    }
    
    
}
