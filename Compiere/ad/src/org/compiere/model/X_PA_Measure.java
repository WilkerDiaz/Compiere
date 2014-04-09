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
/** Generated Model for PA_Measure
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_Measure.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_Measure extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_Measure_ID id
    @param trx transaction
    */
    public X_PA_Measure (Ctx ctx, int PA_Measure_ID, Trx trx)
    {
        super (ctx, PA_Measure_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_Measure_ID == 0)
        {
            setMeasureDataType (null);	// T
            setMeasureType (null);	// M
            setName (null);
            setPA_Measure_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_Measure (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=441 */
    public static final int Table_ID=441;
    
    /** TableName=PA_Measure */
    public static final String Table_Name="PA_Measure";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Project Type.
    @param C_ProjectType_ID Type of the project */
    public void setC_ProjectType_ID (int C_ProjectType_ID)
    {
        if (C_ProjectType_ID <= 0) set_Value ("C_ProjectType_ID", null);
        else
        set_Value ("C_ProjectType_ID", Integer.valueOf(C_ProjectType_ID));
        
    }
    
    /** Get Project Type.
    @return Type of the project */
    public int getC_ProjectType_ID() 
    {
        return get_ValueAsInt("C_ProjectType_ID");
        
    }
    
    /** Set Calculation Class.
    @param CalculationClass Java Class for calculation, implementing Interface Measure */
    public void setCalculationClass (String CalculationClass)
    {
        set_Value ("CalculationClass", CalculationClass);
        
    }
    
    /** Get Calculation Class.
    @return Java Class for calculation, implementing Interface Measure */
    public String getCalculationClass() 
    {
        return (String)get_Value("CalculationClass");
        
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
    
    /** Set Manual Actual.
    @param ManualActual Manually entered actual value */
    public void setManualActual (java.math.BigDecimal ManualActual)
    {
        set_Value ("ManualActual", ManualActual);
        
    }
    
    /** Get Manual Actual.
    @return Manually entered actual value */
    public java.math.BigDecimal getManualActual() 
    {
        return get_ValueAsBigDecimal("ManualActual");
        
    }
    
    /** Set Note.
    @param ManualNote Note for manual entry */
    public void setManualNote (String ManualNote)
    {
        set_Value ("ManualNote", ManualNote);
        
    }
    
    /** Get Note.
    @return Note for manual entry */
    public String getManualNote() 
    {
        return (String)get_Value("ManualNote");
        
    }
    
    /** Status Qty/Amount = S */
    public static final String MEASUREDATATYPE_StatusQtyAmount = X_Ref_PA_Measure_DataType.STATUS_QTY_AMOUNT.getValue();
    /** Qty/Amount in Time = T */
    public static final String MEASUREDATATYPE_QtyAmountInTime = X_Ref_PA_Measure_DataType.QTY_AMOUNT_IN_TIME.getValue();
    /** Set Measure Data Type.
    @param MeasureDataType Type of data - Status or in Time */
    public void setMeasureDataType (String MeasureDataType)
    {
        if (MeasureDataType == null) throw new IllegalArgumentException ("MeasureDataType is mandatory");
        if (!X_Ref_PA_Measure_DataType.isValid(MeasureDataType))
        throw new IllegalArgumentException ("MeasureDataType Invalid value - " + MeasureDataType + " - Reference_ID=369 - S - T");
        set_Value ("MeasureDataType", MeasureDataType);
        
    }
    
    /** Get Measure Data Type.
    @return Type of data - Status or in Time */
    public String getMeasureDataType() 
    {
        return (String)get_Value("MeasureDataType");
        
    }
    
    /** Achievements = A */
    public static final String MEASURETYPE_Achievements = X_Ref_PA_Measure_Type.ACHIEVEMENTS.getValue();
    /** Calculated = C */
    public static final String MEASURETYPE_Calculated = X_Ref_PA_Measure_Type.CALCULATED.getValue();
    /** Manual = M */
    public static final String MEASURETYPE_Manual = X_Ref_PA_Measure_Type.MANUAL.getValue();
    /** Project = P */
    public static final String MEASURETYPE_Project = X_Ref_PA_Measure_Type.PROJECT.getValue();
    /** Request = Q */
    public static final String MEASURETYPE_Request = X_Ref_PA_Measure_Type.REQUEST.getValue();
    /** Ratio = R */
    public static final String MEASURETYPE_Ratio = X_Ref_PA_Measure_Type.RATIO.getValue();
    /** User defined = U */
    public static final String MEASURETYPE_UserDefined = X_Ref_PA_Measure_Type.USER_DEFINED.getValue();
    /** Set Measure Type.
    @param MeasureType Determines how the actual performance is derived */
    public void setMeasureType (String MeasureType)
    {
        if (MeasureType == null) throw new IllegalArgumentException ("MeasureType is mandatory");
        if (!X_Ref_PA_Measure_Type.isValid(MeasureType))
        throw new IllegalArgumentException ("MeasureType Invalid value - " + MeasureType + " - Reference_ID=231 - A - C - M - P - Q - R - U");
        set_Value ("MeasureType", MeasureType);
        
    }
    
    /** Get Measure Type.
    @return Determines how the actual performance is derived */
    public String getMeasureType() 
    {
        return (String)get_Value("MeasureType");
        
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
    
    /** Set Benchmark.
    @param PA_Benchmark_ID Performance Benchmark */
    public void setPA_Benchmark_ID (int PA_Benchmark_ID)
    {
        if (PA_Benchmark_ID <= 0) set_Value ("PA_Benchmark_ID", null);
        else
        set_Value ("PA_Benchmark_ID", Integer.valueOf(PA_Benchmark_ID));
        
    }
    
    /** Get Benchmark.
    @return Performance Benchmark */
    public int getPA_Benchmark_ID() 
    {
        return get_ValueAsInt("PA_Benchmark_ID");
        
    }
    
    /** Set Reporting Hierarchy.
    @param PA_Hierarchy_ID Optional Reporting Hierarchy - If not selected the default hierarchy trees are used. */
    public void setPA_Hierarchy_ID (int PA_Hierarchy_ID)
    {
        if (PA_Hierarchy_ID <= 0) set_Value ("PA_Hierarchy_ID", null);
        else
        set_Value ("PA_Hierarchy_ID", Integer.valueOf(PA_Hierarchy_ID));
        
    }
    
    /** Get Reporting Hierarchy.
    @return Optional Reporting Hierarchy - If not selected the default hierarchy trees are used. */
    public int getPA_Hierarchy_ID() 
    {
        return get_ValueAsInt("PA_Hierarchy_ID");
        
    }
    
    /** Set Measure Calculation.
    @param PA_MeasureCalc_ID Calculation method for measuring performance */
    public void setPA_MeasureCalc_ID (int PA_MeasureCalc_ID)
    {
        if (PA_MeasureCalc_ID <= 0) set_Value ("PA_MeasureCalc_ID", null);
        else
        set_Value ("PA_MeasureCalc_ID", Integer.valueOf(PA_MeasureCalc_ID));
        
    }
    
    /** Get Measure Calculation.
    @return Calculation method for measuring performance */
    public int getPA_MeasureCalc_ID() 
    {
        return get_ValueAsInt("PA_MeasureCalc_ID");
        
    }
    
    /** Set Measure.
    @param PA_Measure_ID Concrete Performance Measurement */
    public void setPA_Measure_ID (int PA_Measure_ID)
    {
        if (PA_Measure_ID < 1) throw new IllegalArgumentException ("PA_Measure_ID is mandatory.");
        set_ValueNoCheck ("PA_Measure_ID", Integer.valueOf(PA_Measure_ID));
        
    }
    
    /** Get Measure.
    @return Concrete Performance Measurement */
    public int getPA_Measure_ID() 
    {
        return get_ValueAsInt("PA_Measure_ID");
        
    }
    
    /** Set Ratio.
    @param PA_Ratio_ID Performance Ratio */
    public void setPA_Ratio_ID (int PA_Ratio_ID)
    {
        if (PA_Ratio_ID <= 0) set_Value ("PA_Ratio_ID", null);
        else
        set_Value ("PA_Ratio_ID", Integer.valueOf(PA_Ratio_ID));
        
    }
    
    /** Get Ratio.
    @return Performance Ratio */
    public int getPA_Ratio_ID() 
    {
        return get_ValueAsInt("PA_Ratio_ID");
        
    }
    
    /** Set Request Type.
    @param R_RequestType_ID Type of request (e.g. Inquiry, Complaint...) */
    public void setR_RequestType_ID (int R_RequestType_ID)
    {
        if (R_RequestType_ID <= 0) set_Value ("R_RequestType_ID", null);
        else
        set_Value ("R_RequestType_ID", Integer.valueOf(R_RequestType_ID));
        
    }
    
    /** Get Request Type.
    @return Type of request (e.g. Inquiry, Complaint...) */
    public int getR_RequestType_ID() 
    {
        return get_ValueAsInt("R_RequestType_ID");
        
    }
    
    
}
