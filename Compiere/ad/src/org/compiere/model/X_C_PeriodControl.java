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
/** Generated Model for C_PeriodControl
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_PeriodControl.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_PeriodControl extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_PeriodControl_ID id
    @param trx transaction
    */
    public X_C_PeriodControl (Ctx ctx, int C_PeriodControl_ID, Trx trx)
    {
        super (ctx, C_PeriodControl_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_PeriodControl_ID == 0)
        {
            setC_PeriodControl_ID (0);
            setC_Period_ID (0);
            setDocBaseType (null);
            setPeriodAction (null);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_PeriodControl (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=229 */
    public static final int Table_ID=229;
    
    /** TableName=C_PeriodControl */
    public static final String Table_Name="C_PeriodControl";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Period Control.
    @param C_PeriodControl_ID Period Control */
    public void setC_PeriodControl_ID (int C_PeriodControl_ID)
    {
        if (C_PeriodControl_ID < 1) throw new IllegalArgumentException ("C_PeriodControl_ID is mandatory.");
        set_ValueNoCheck ("C_PeriodControl_ID", Integer.valueOf(C_PeriodControl_ID));
        
    }
    
    /** Get Period Control.
    @return Period Control */
    public int getC_PeriodControl_ID() 
    {
        return get_ValueAsInt("C_PeriodControl_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_PeriodControl_ID()));
        
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
    
    /** Set Document BaseType.
    @param DocBaseType Logical type of document */
    public void setDocBaseType (String DocBaseType)
    {
        set_ValueNoCheck ("DocBaseType", DocBaseType);
        
    }
    
    /** Get Document BaseType.
    @return Logical type of document */
    public String getDocBaseType() 
    {
        return (String)get_Value("DocBaseType");
        
    }
    
    /** Close Period = C */
    public static final String PERIODACTION_ClosePeriod = X_Ref_C_PeriodControl_Action.CLOSE_PERIOD.getValue();
    /** <No Action> = N */
    public static final String PERIODACTION_NoAction = X_Ref_C_PeriodControl_Action.NO_ACTION.getValue();
    /** Open Period = O */
    public static final String PERIODACTION_OpenPeriod = X_Ref_C_PeriodControl_Action.OPEN_PERIOD.getValue();
    /** Permanently Close Period = P */
    public static final String PERIODACTION_PermanentlyClosePeriod = X_Ref_C_PeriodControl_Action.PERMANENTLY_CLOSE_PERIOD.getValue();
    /** Set Period Action.
    @param PeriodAction Action taken for this period */
    public void setPeriodAction (String PeriodAction)
    {
        if (PeriodAction == null) throw new IllegalArgumentException ("PeriodAction is mandatory");
        if (!X_Ref_C_PeriodControl_Action.isValid(PeriodAction))
        throw new IllegalArgumentException ("PeriodAction Invalid value - " + PeriodAction + " - Reference_ID=176 - C - N - O - P");
        set_Value ("PeriodAction", PeriodAction);
        
    }
    
    /** Get Period Action.
    @return Action taken for this period */
    public String getPeriodAction() 
    {
        return (String)get_Value("PeriodAction");
        
    }
    
    /** Closed = C */
    public static final String PERIODSTATUS_Closed = X_Ref_C_PeriodControl_Status.CLOSED.getValue();
    /** Never opened = N */
    public static final String PERIODSTATUS_NeverOpened = X_Ref_C_PeriodControl_Status.NEVER_OPENED.getValue();
    /** Open = O */
    public static final String PERIODSTATUS_Open = X_Ref_C_PeriodControl_Status.OPEN.getValue();
    /** Permanently closed = P */
    public static final String PERIODSTATUS_PermanentlyClosed = X_Ref_C_PeriodControl_Status.PERMANENTLY_CLOSED.getValue();
    /** Set Period Status.
    @param PeriodStatus Current state of this period */
    public void setPeriodStatus (String PeriodStatus)
    {
        if (!X_Ref_C_PeriodControl_Status.isValid(PeriodStatus))
        throw new IllegalArgumentException ("PeriodStatus Invalid value - " + PeriodStatus + " - Reference_ID=177 - C - N - O - P");
        set_ValueNoCheck ("PeriodStatus", PeriodStatus);
        
    }
    
    /** Get Period Status.
    @return Current state of this period */
    public String getPeriodStatus() 
    {
        return (String)get_Value("PeriodStatus");
        
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
