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
/** Generated Model for C_UOM
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_UOM.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_UOM extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_UOM_ID id
    @param trx transaction
    */
    public X_C_UOM (Ctx ctx, int C_UOM_ID, Trx trx)
    {
        super (ctx, C_UOM_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_UOM_ID == 0)
        {
            setC_UOM_ID (0);
            setCostingPrecision (0);
            setIsDefault (false);
            setName (null);
            setStdPrecision (0);
            setX12DE355 (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_UOM (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=146 */
    public static final int Table_ID=146;
    
    /** TableName=C_UOM */
    public static final String Table_Name="C_UOM";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID < 1) throw new IllegalArgumentException ("C_UOM_ID is mandatory.");
        set_ValueNoCheck ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
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
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
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
    
    /** Set Symbol.
    @param UOMSymbol Symbol for a Unit of Measure */
    public void setUOMSymbol (String UOMSymbol)
    {
        set_Value ("UOMSymbol", UOMSymbol);
        
    }
    
    /** Get Symbol.
    @return Symbol for a Unit of Measure */
    public String getUOMSymbol() 
    {
        return (String)get_Value("UOMSymbol");
        
    }
    
    /** Set UOM Code.
    @param X12DE355 UOM EDI X12 Code */
    public void setX12DE355 (String X12DE355)
    {
        if (X12DE355 == null) throw new IllegalArgumentException ("X12DE355 is mandatory.");
        set_Value ("X12DE355", X12DE355);
        
    }
    
    /** Get UOM Code.
    @return UOM EDI X12 Code */
    public String getX12DE355() 
    {
        return (String)get_Value("X12DE355");
        
    }
    
    
}
