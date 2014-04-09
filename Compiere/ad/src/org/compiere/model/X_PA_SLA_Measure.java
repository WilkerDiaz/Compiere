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
/** Generated Model for PA_SLA_Measure
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_SLA_Measure.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_SLA_Measure extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_SLA_Measure_ID id
    @param trx transaction
    */
    public X_PA_SLA_Measure (Ctx ctx, int PA_SLA_Measure_ID, Trx trx)
    {
        super (ctx, PA_SLA_Measure_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_SLA_Measure_ID == 0)
        {
            setDateTrx (new Timestamp(System.currentTimeMillis()));
            setMeasureActual (Env.ZERO);
            setPA_SLA_Goal_ID (0);
            setPA_SLA_Measure_ID (0);
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_SLA_Measure (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=743 */
    public static final int Table_ID=743;
    
    /** TableName=PA_SLA_Measure */
    public static final String Table_Name="PA_SLA_Measure";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID <= 0) set_Value ("AD_Table_ID", null);
        else
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getDateTrx()));
        
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
    
    /** Set Measure Actual.
    @param MeasureActual Actual value that has been measured. */
    public void setMeasureActual (java.math.BigDecimal MeasureActual)
    {
        if (MeasureActual == null) throw new IllegalArgumentException ("MeasureActual is mandatory.");
        set_Value ("MeasureActual", MeasureActual);
        
    }
    
    /** Get Measure Actual.
    @return Actual value that has been measured. */
    public java.math.BigDecimal getMeasureActual() 
    {
        return get_ValueAsBigDecimal("MeasureActual");
        
    }
    
    /** Set SLA Goal.
    @param PA_SLA_Goal_ID Service Level Agreement Goal */
    public void setPA_SLA_Goal_ID (int PA_SLA_Goal_ID)
    {
        if (PA_SLA_Goal_ID < 1) throw new IllegalArgumentException ("PA_SLA_Goal_ID is mandatory.");
        set_ValueNoCheck ("PA_SLA_Goal_ID", Integer.valueOf(PA_SLA_Goal_ID));
        
    }
    
    /** Get SLA Goal.
    @return Service Level Agreement Goal */
    public int getPA_SLA_Goal_ID() 
    {
        return get_ValueAsInt("PA_SLA_Goal_ID");
        
    }
    
    /** Set SLA Measure.
    @param PA_SLA_Measure_ID Service Level Agreement Measure */
    public void setPA_SLA_Measure_ID (int PA_SLA_Measure_ID)
    {
        if (PA_SLA_Measure_ID < 1) throw new IllegalArgumentException ("PA_SLA_Measure_ID is mandatory.");
        set_ValueNoCheck ("PA_SLA_Measure_ID", Integer.valueOf(PA_SLA_Measure_ID));
        
    }
    
    /** Get SLA Measure.
    @return Service Level Agreement Measure */
    public int getPA_SLA_Measure_ID() 
    {
        return get_ValueAsInt("PA_SLA_Measure_ID");
        
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
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID <= 0) set_Value ("Record_ID", null);
        else
        set_Value ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    
}
