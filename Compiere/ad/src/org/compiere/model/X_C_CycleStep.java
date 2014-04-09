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
/** Generated Model for C_CycleStep
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_CycleStep.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_CycleStep extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_CycleStep_ID id
    @param trx transaction
    */
    public X_C_CycleStep (Ctx ctx, int C_CycleStep_ID, Trx trx)
    {
        super (ctx, C_CycleStep_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_CycleStep_ID == 0)
        {
            setC_CycleStep_ID (0);
            setC_Cycle_ID (0);
            setName (null);
            setRelativeWeight (Env.ZERO);	// 1
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM C_CycleStep WHERE C_Cycle_ID=@C_Cycle_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_CycleStep (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=590 */
    public static final int Table_ID=590;
    
    /** TableName=C_CycleStep */
    public static final String Table_Name="C_CycleStep";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Cycle Step.
    @param C_CycleStep_ID The step for this Cycle */
    public void setC_CycleStep_ID (int C_CycleStep_ID)
    {
        if (C_CycleStep_ID < 1) throw new IllegalArgumentException ("C_CycleStep_ID is mandatory.");
        set_ValueNoCheck ("C_CycleStep_ID", Integer.valueOf(C_CycleStep_ID));
        
    }
    
    /** Get Cycle Step.
    @return The step for this Cycle */
    public int getC_CycleStep_ID() 
    {
        return get_ValueAsInt("C_CycleStep_ID");
        
    }
    
    /** Set Project Cycle.
    @param C_Cycle_ID Identifier for this Project Reporting Cycle */
    public void setC_Cycle_ID (int C_Cycle_ID)
    {
        if (C_Cycle_ID < 1) throw new IllegalArgumentException ("C_Cycle_ID is mandatory.");
        set_ValueNoCheck ("C_Cycle_ID", Integer.valueOf(C_Cycle_ID));
        
    }
    
    /** Get Project Cycle.
    @return Identifier for this Project Reporting Cycle */
    public int getC_Cycle_ID() 
    {
        return get_ValueAsInt("C_Cycle_ID");
        
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
    
    /** Set Relative Weight.
    @param RelativeWeight Relative weight of this step (0 = ignored) */
    public void setRelativeWeight (java.math.BigDecimal RelativeWeight)
    {
        if (RelativeWeight == null) throw new IllegalArgumentException ("RelativeWeight is mandatory.");
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
