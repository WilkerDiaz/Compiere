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
/** Generated Model for PA_SLA_Goal
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_SLA_Goal.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_SLA_Goal extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_SLA_Goal_ID id
    @param trx transaction
    */
    public X_PA_SLA_Goal (Ctx ctx, int PA_SLA_Goal_ID, Trx trx)
    {
        super (ctx, PA_SLA_Goal_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_SLA_Goal_ID == 0)
        {
            setC_BPartner_ID (0);
            setMeasureActual (Env.ZERO);
            setMeasureTarget (Env.ZERO);
            setName (null);
            setPA_SLA_Criteria_ID (0);
            setPA_SLA_Goal_ID (0);
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_SLA_Goal (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=745 */
    public static final int Table_ID=745;
    
    /** TableName=PA_SLA_Goal */
    public static final String Table_Name="PA_SLA_Goal";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Date Last Run.
    @param DateLastRun Date the process was last run. */
    public void setDateLastRun (Timestamp DateLastRun)
    {
        set_Value ("DateLastRun", DateLastRun);
        
    }
    
    /** Get Date Last Run.
    @return Date the process was last run. */
    public Timestamp getDateLastRun() 
    {
        return (Timestamp)get_Value("DateLastRun");
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
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
    
    /** Set SLA Criteria.
    @param PA_SLA_Criteria_ID Service Level Agreement Criteria */
    public void setPA_SLA_Criteria_ID (int PA_SLA_Criteria_ID)
    {
        if (PA_SLA_Criteria_ID < 1) throw new IllegalArgumentException ("PA_SLA_Criteria_ID is mandatory.");
        set_Value ("PA_SLA_Criteria_ID", Integer.valueOf(PA_SLA_Criteria_ID));
        
    }
    
    /** Get SLA Criteria.
    @return Service Level Agreement Criteria */
    public int getPA_SLA_Criteria_ID() 
    {
        return get_ValueAsInt("PA_SLA_Criteria_ID");
        
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
    
    /** Set Valid from.
    @param ValidFrom Valid from including this date (first day) */
    public void setValidFrom (Timestamp ValidFrom)
    {
        set_Value ("ValidFrom", ValidFrom);
        
    }
    
    /** Get Valid from.
    @return Valid from including this date (first day) */
    public Timestamp getValidFrom() 
    {
        return (Timestamp)get_Value("ValidFrom");
        
    }
    
    /** Set Valid to.
    @param ValidTo Valid to including this date (last day) */
    public void setValidTo (Timestamp ValidTo)
    {
        set_Value ("ValidTo", ValidTo);
        
    }
    
    /** Get Valid to.
    @return Valid to including this date (last day) */
    public Timestamp getValidTo() 
    {
        return (Timestamp)get_Value("ValidTo");
        
    }
    
    
}
