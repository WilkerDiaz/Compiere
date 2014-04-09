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
/** Generated Model for PA_BenchmarkData
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_BenchmarkData.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_BenchmarkData extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_BenchmarkData_ID id
    @param trx transaction
    */
    public X_PA_BenchmarkData (Ctx ctx, int PA_BenchmarkData_ID, Trx trx)
    {
        super (ctx, PA_BenchmarkData_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_BenchmarkData_ID == 0)
        {
            setBenchmarkDate (new Timestamp(System.currentTimeMillis()));
            setBenchmarkValue (Env.ZERO);
            setName (null);
            setPA_BenchmarkData_ID (0);
            setPA_Benchmark_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_BenchmarkData (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=834 */
    public static final int Table_ID=834;
    
    /** TableName=PA_BenchmarkData */
    public static final String Table_Name="PA_BenchmarkData";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Date.
    @param BenchmarkDate Benchmark Date */
    public void setBenchmarkDate (Timestamp BenchmarkDate)
    {
        if (BenchmarkDate == null) throw new IllegalArgumentException ("BenchmarkDate is mandatory.");
        set_Value ("BenchmarkDate", BenchmarkDate);
        
    }
    
    /** Get Date.
    @return Benchmark Date */
    public Timestamp getBenchmarkDate() 
    {
        return (Timestamp)get_Value("BenchmarkDate");
        
    }
    
    /** Set Value.
    @param BenchmarkValue Benchmark Value */
    public void setBenchmarkValue (java.math.BigDecimal BenchmarkValue)
    {
        if (BenchmarkValue == null) throw new IllegalArgumentException ("BenchmarkValue is mandatory.");
        set_Value ("BenchmarkValue", BenchmarkValue);
        
    }
    
    /** Get Value.
    @return Benchmark Value */
    public java.math.BigDecimal getBenchmarkValue() 
    {
        return get_ValueAsBigDecimal("BenchmarkValue");
        
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
    
    /** Set Benchmark Data.
    @param PA_BenchmarkData_ID Performance Benchmark Data Point */
    public void setPA_BenchmarkData_ID (int PA_BenchmarkData_ID)
    {
        if (PA_BenchmarkData_ID < 1) throw new IllegalArgumentException ("PA_BenchmarkData_ID is mandatory.");
        set_ValueNoCheck ("PA_BenchmarkData_ID", Integer.valueOf(PA_BenchmarkData_ID));
        
    }
    
    /** Get Benchmark Data.
    @return Performance Benchmark Data Point */
    public int getPA_BenchmarkData_ID() 
    {
        return get_ValueAsInt("PA_BenchmarkData_ID");
        
    }
    
    /** Set Benchmark.
    @param PA_Benchmark_ID Performance Benchmark */
    public void setPA_Benchmark_ID (int PA_Benchmark_ID)
    {
        if (PA_Benchmark_ID < 1) throw new IllegalArgumentException ("PA_Benchmark_ID is mandatory.");
        set_ValueNoCheck ("PA_Benchmark_ID", Integer.valueOf(PA_Benchmark_ID));
        
    }
    
    /** Get Benchmark.
    @return Performance Benchmark */
    public int getPA_Benchmark_ID() 
    {
        return get_ValueAsInt("PA_Benchmark_ID");
        
    }
    
    
}
