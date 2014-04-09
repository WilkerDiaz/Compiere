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
/** Generated Model for PA_Achievement
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_Achievement.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_Achievement extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_Achievement_ID id
    @param trx transaction
    */
    public X_PA_Achievement (Ctx ctx, int PA_Achievement_ID, Trx trx)
    {
        super (ctx, PA_Achievement_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_Achievement_ID == 0)
        {
            setIsAchieved (false);
            setManualActual (Env.ZERO);
            setName (null);
            setPA_Achievement_ID (0);
            setPA_Measure_ID (0);
            setSeqNo (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_Achievement (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=438 */
    public static final int Table_ID=438;
    
    /** TableName=PA_Achievement */
    public static final String Table_Name="PA_Achievement";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Document Date.
    @param DateDoc Date of the Document */
    public void setDateDoc (Timestamp DateDoc)
    {
        set_Value ("DateDoc", DateDoc);
        
    }
    
    /** Get Document Date.
    @return Date of the Document */
    public Timestamp getDateDoc() 
    {
        return (Timestamp)get_Value("DateDoc");
        
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
    
    /** Set Achieved.
    @param IsAchieved The goal is achieved */
    public void setIsAchieved (boolean IsAchieved)
    {
        set_Value ("IsAchieved", Boolean.valueOf(IsAchieved));
        
    }
    
    /** Get Achieved.
    @return The goal is achieved */
    public boolean isAchieved() 
    {
        return get_ValueAsBoolean("IsAchieved");
        
    }
    
    /** Set Manual Actual.
    @param ManualActual Manually entered actual value */
    public void setManualActual (java.math.BigDecimal ManualActual)
    {
        if (ManualActual == null) throw new IllegalArgumentException ("ManualActual is mandatory.");
        set_Value ("ManualActual", ManualActual);
        
    }
    
    /** Get Manual Actual.
    @return Manually entered actual value */
    public java.math.BigDecimal getManualActual() 
    {
        return get_ValueAsBigDecimal("ManualActual");
        
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
    
    /** Set Achievement.
    @param PA_Achievement_ID Performance Achievement */
    public void setPA_Achievement_ID (int PA_Achievement_ID)
    {
        if (PA_Achievement_ID < 1) throw new IllegalArgumentException ("PA_Achievement_ID is mandatory.");
        set_ValueNoCheck ("PA_Achievement_ID", Integer.valueOf(PA_Achievement_ID));
        
    }
    
    /** Get Achievement.
    @return Performance Achievement */
    public int getPA_Achievement_ID() 
    {
        return get_ValueAsInt("PA_Achievement_ID");
        
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
