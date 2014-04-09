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
/** Generated Model for C_Period
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Period.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Period extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Period_ID id
    @param trx transaction
    */
    public X_C_Period (Ctx ctx, int C_Period_ID, Trx trx)
    {
        super (ctx, C_Period_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Period_ID == 0)
        {
            setC_Period_ID (0);
            setC_Year_ID (0);
            setName (null);
            setPeriodNo (0);	// @SQL=SELECT COALESCE(MAX(PeriodNo),0)+1 AS DefaultValue FROM C_Period WHERE C_Year_ID=@C_Year_ID@
            setPeriodType (null);	// S
            setStartDate (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Period (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=145 */
    public static final int Table_ID=145;
    
    /** TableName=C_Period */
    public static final String Table_Name="C_Period";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Period.
    @param C_Period_ID Period of the Calendar */
    public void setC_Period_ID (int C_Period_ID)
    {
        if (C_Period_ID < 1) throw new IllegalArgumentException ("C_Period_ID is mandatory.");
        set_ValueNoCheck ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
    }
    
    /** Get Period.
    @return Period of the Calendar */
    public int getC_Period_ID() 
    {
        return get_ValueAsInt("C_Period_ID");
        
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
    
    /** Set End Date.
    @param EndDate Last effective date (inclusive) */
    public void setEndDate (Timestamp EndDate)
    {
        set_Value ("EndDate", EndDate);
        
    }
    
    /** Get End Date.
    @return Last effective date (inclusive) */
    public Timestamp getEndDate() 
    {
        return (Timestamp)get_Value("EndDate");
        
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
    
    /** Set Period No.
    @param PeriodNo Unique Period Number */
    public void setPeriodNo (int PeriodNo)
    {
        set_Value ("PeriodNo", Integer.valueOf(PeriodNo));
        
    }
    
    /** Get Period No.
    @return Unique Period Number */
    public int getPeriodNo() 
    {
        return get_ValueAsInt("PeriodNo");
        
    }
    
    /** Adjustment Period = A */
    public static final String PERIODTYPE_AdjustmentPeriod = X_Ref_C_Period_Type.ADJUSTMENT_PERIOD.getValue();
    /** Standard Calendar Period = S */
    public static final String PERIODTYPE_StandardCalendarPeriod = X_Ref_C_Period_Type.STANDARD_CALENDAR_PERIOD.getValue();
    /** Set Period Type.
    @param PeriodType Period Type */
    public void setPeriodType (String PeriodType)
    {
        if (PeriodType == null) throw new IllegalArgumentException ("PeriodType is mandatory");
        if (!X_Ref_C_Period_Type.isValid(PeriodType))
        throw new IllegalArgumentException ("PeriodType Invalid value - " + PeriodType + " - Reference_ID=115 - A - S");
        set_ValueNoCheck ("PeriodType", PeriodType);
        
    }
    
    /** Get Period Type.
    @return Period Type */
    public String getPeriodType() 
    {
        return (String)get_Value("PeriodType");
        
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
    
    /** Set Start Date.
    @param StartDate First effective day (inclusive) */
    public void setStartDate (Timestamp StartDate)
    {
        if (StartDate == null) throw new IllegalArgumentException ("StartDate is mandatory.");
        set_Value ("StartDate", StartDate);
        
    }
    
    /** Get Start Date.
    @return First effective day (inclusive) */
    public Timestamp getStartDate() 
    {
        return (Timestamp)get_Value("StartDate");
        
    }
    
    
}
