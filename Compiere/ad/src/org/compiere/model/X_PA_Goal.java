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
/** Generated Model for PA_Goal
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_Goal.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_Goal extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_Goal_ID id
    @param trx transaction
    */
    public X_PA_Goal (Ctx ctx, int PA_Goal_ID, Trx trx)
    {
        super (ctx, PA_Goal_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_Goal_ID == 0)
        {
            setIsSummary (false);
            setMeasureScope (null);
            setMeasureTarget (Env.ZERO);
            setName (null);
            setPA_ColorSchema_ID (0);
            setPA_Goal_ID (0);
            setSeqNo (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_Goal (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27509346841789L;
    /** Last Updated Timestamp 2008-11-20 14:52:05.0 */
    public static final long updatedMS = 1227221525000L;
    /** AD_Table_ID=440 */
    public static final int Table_ID=440;
    
    /** TableName=PA_Goal */
    public static final String Table_Name="PA_Goal";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Role.
    @param AD_Role_ID Responsibility Role */
    public void setAD_Role_ID (int AD_Role_ID)
    {
        if (AD_Role_ID <= 0) set_Value ("AD_Role_ID", null);
        else
        set_Value ("AD_Role_ID", Integer.valueOf(AD_Role_ID));
        
    }
    
    /** Get Role.
    @return Responsibility Role */
    public int getAD_Role_ID() 
    {
        return get_ValueAsInt("AD_Role_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Date From.
    @param DateFrom Starting date for a range */
    public void setDateFrom (Timestamp DateFrom)
    {
        set_Value ("DateFrom", DateFrom);
        
    }
    
    /** Get Date From.
    @return Starting date for a range */
    public Timestamp getDateFrom() 
    {
        return (Timestamp)get_Value("DateFrom");
        
    }
    
    /** Set Date Last Run.
    @param DateLastRun Date the process was last run. */
    public void setDateLastRun (Timestamp DateLastRun)
    {
        set_ValueNoCheck ("DateLastRun", DateLastRun);
        
    }
    
    /** Get Date Last Run.
    @return Date the process was last run. */
    public Timestamp getDateLastRun() 
    {
        return (Timestamp)get_Value("DateLastRun");
        
    }
    
    /** Set Date To.
    @param DateTo End date of a date range */
    public void setDateTo (Timestamp DateTo)
    {
        set_Value ("DateTo", DateTo);
        
    }
    
    /** Get Date To.
    @return End date of a date range */
    public Timestamp getDateTo() 
    {
        return (Timestamp)get_Value("DateTo");
        
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
    
    /** Set Performance Goal.
    @param GoalPerformance Target achievement from 0..1 */
    public void setGoalPerformance (java.math.BigDecimal GoalPerformance)
    {
        set_ValueNoCheck ("GoalPerformance", GoalPerformance);
        
    }
    
    /** Get Performance Goal.
    @return Target achievement from 0..1 */
    public java.math.BigDecimal getGoalPerformance() 
    {
        return get_ValueAsBigDecimal("GoalPerformance");
        
    }
    
    /** Set Summary Level.
    @param IsSummary This is a summary entity */
    public void setIsSummary (boolean IsSummary)
    {
        set_Value ("IsSummary", Boolean.valueOf(IsSummary));
        
    }
    
    /** Get Summary Level.
    @return This is a summary entity */
    public boolean isSummary() 
    {
        return get_ValueAsBoolean("IsSummary");
        
    }
    
    /** Set Measure Actual.
    @param MeasureActual Actual value that has been measured. */
    public void setMeasureActual (java.math.BigDecimal MeasureActual)
    {
        set_ValueNoCheck ("MeasureActual", MeasureActual);
        
    }
    
    /** Get Measure Actual.
    @return Actual value that has been measured. */
    public java.math.BigDecimal getMeasureActual() 
    {
        return get_ValueAsBigDecimal("MeasureActual");
        
    }
    
    /** Total = 0 */
    public static final String MEASUREDISPLAY_Total = X_Ref_PA_Goal_Scope.TOTAL.getValue();
    /** Year = 1 */
    public static final String MEASUREDISPLAY_Year = X_Ref_PA_Goal_Scope.YEAR.getValue();
    /** Quarter = 3 */
    public static final String MEASUREDISPLAY_Quarter = X_Ref_PA_Goal_Scope.QUARTER.getValue();
    /** Month = 5 */
    public static final String MEASUREDISPLAY_Month = X_Ref_PA_Goal_Scope.MONTH.getValue();
    /** Week = 7 */
    public static final String MEASUREDISPLAY_Week = X_Ref_PA_Goal_Scope.WEEK.getValue();
    /** Day = 8 */
    public static final String MEASUREDISPLAY_Day = X_Ref_PA_Goal_Scope.DAY.getValue();
    /** Set Measure Display.
    @param MeasureDisplay Measure Scope initially displayed */
    public void setMeasureDisplay (String MeasureDisplay)
    {
        if (!X_Ref_PA_Goal_Scope.isValid(MeasureDisplay))
        throw new IllegalArgumentException ("MeasureDisplay Invalid value - " + MeasureDisplay + " - Reference_ID=367 - 0 - 1 - 3 - 5 - 7 - 8");
        set_Value ("MeasureDisplay", MeasureDisplay);
        
    }
    
    /** Get Measure Display.
    @return Measure Scope initially displayed */
    public String getMeasureDisplay() 
    {
        return (String)get_Value("MeasureDisplay");
        
    }
    
    /** Total = 0 */
    public static final String MEASURESCOPE_Total = X_Ref_PA_Goal_Scope.TOTAL.getValue();
    /** Year = 1 */
    public static final String MEASURESCOPE_Year = X_Ref_PA_Goal_Scope.YEAR.getValue();
    /** Quarter = 3 */
    public static final String MEASURESCOPE_Quarter = X_Ref_PA_Goal_Scope.QUARTER.getValue();
    /** Month = 5 */
    public static final String MEASURESCOPE_Month = X_Ref_PA_Goal_Scope.MONTH.getValue();
    /** Week = 7 */
    public static final String MEASURESCOPE_Week = X_Ref_PA_Goal_Scope.WEEK.getValue();
    /** Day = 8 */
    public static final String MEASURESCOPE_Day = X_Ref_PA_Goal_Scope.DAY.getValue();
    /** Set Measure Scope.
    @param MeasureScope Performance Measure Scope */
    public void setMeasureScope (String MeasureScope)
    {
        if (MeasureScope == null) throw new IllegalArgumentException ("MeasureScope is mandatory");
        if (!X_Ref_PA_Goal_Scope.isValid(MeasureScope))
        throw new IllegalArgumentException ("MeasureScope Invalid value - " + MeasureScope + " - Reference_ID=367 - 0 - 1 - 3 - 5 - 7 - 8");
        set_Value ("MeasureScope", MeasureScope);
        
    }
    
    /** Get Measure Scope.
    @return Performance Measure Scope */
    public String getMeasureScope() 
    {
        return (String)get_Value("MeasureScope");
        
    }
    
    /** Set Measure Target.
    @param MeasureTarget Target value for measure */
    public void setMeasureTarget (java.math.BigDecimal MeasureTarget)
    {
        if (MeasureTarget == null) throw new IllegalArgumentException ("MeasureTarget is mandatory.");
        set_Value ("MeasureTarget", MeasureTarget);
        
    }
    
    /** Get Measure Target.
    @return Target value for measure */
    public java.math.BigDecimal getMeasureTarget() 
    {
        return get_ValueAsBigDecimal("MeasureTarget");
        
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
    
    /** Set Note.
    @param Note Optional additional user defined information */
    public void setNote (String Note)
    {
        set_Value ("Note", Note);
        
    }
    
    /** Get Note.
    @return Optional additional user defined information */
    public String getNote() 
    {
        return (String)get_Value("Note");
        
    }
    
    /** Set Color Schema.
    @param PA_ColorSchema_ID Performance Color Schema */
    public void setPA_ColorSchema_ID (int PA_ColorSchema_ID)
    {
        if (PA_ColorSchema_ID < 1) throw new IllegalArgumentException ("PA_ColorSchema_ID is mandatory.");
        set_Value ("PA_ColorSchema_ID", Integer.valueOf(PA_ColorSchema_ID));
        
    }
    
    /** Get Color Schema.
    @return Performance Color Schema */
    public int getPA_ColorSchema_ID() 
    {
        return get_ValueAsInt("PA_ColorSchema_ID");
        
    }
    
    /** Set Parent Goal.
    @param PA_GoalParent_ID Parent Goal */
    public void setPA_GoalParent_ID (int PA_GoalParent_ID)
    {
        if (PA_GoalParent_ID <= 0) set_Value ("PA_GoalParent_ID", null);
        else
        set_Value ("PA_GoalParent_ID", Integer.valueOf(PA_GoalParent_ID));
        
    }
    
    /** Get Parent Goal.
    @return Parent Goal */
    public int getPA_GoalParent_ID() 
    {
        return get_ValueAsInt("PA_GoalParent_ID");
        
    }
    
    /** Set Goal.
    @param PA_Goal_ID Performance Goal */
    public void setPA_Goal_ID (int PA_Goal_ID)
    {
        if (PA_Goal_ID < 1) throw new IllegalArgumentException ("PA_Goal_ID is mandatory.");
        set_ValueNoCheck ("PA_Goal_ID", Integer.valueOf(PA_Goal_ID));
        
    }
    
    /** Get Goal.
    @return Performance Goal */
    public int getPA_Goal_ID() 
    {
        return get_ValueAsInt("PA_Goal_ID");
        
    }
    
    /** Set Measure.
    @param PA_Measure_ID Concrete Performance Measurement */
    public void setPA_Measure_ID (int PA_Measure_ID)
    {
        if (PA_Measure_ID <= 0) set_Value ("PA_Measure_ID", null);
        else
        set_Value ("PA_Measure_ID", Integer.valueOf(PA_Measure_ID));
        
    }
    
    /** Get Measure.
    @return Concrete Performance Measurement */
    public int getPA_Measure_ID() 
    {
        return get_ValueAsInt("PA_Measure_ID");
        
    }
    
    /** Set Relative Weight.
    @param RelativeWeight Relative weight of this step (0 = ignored) */
    public void setRelativeWeight (java.math.BigDecimal RelativeWeight)
    {
        set_Value ("RelativeWeight", RelativeWeight);
        
    }
    
    /** Get Relative Weight.
    @return Relative weight of this step (0 = ignored) */
    public java.math.BigDecimal getRelativeWeight() 
    {
        return get_ValueAsBigDecimal("RelativeWeight");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    
}
