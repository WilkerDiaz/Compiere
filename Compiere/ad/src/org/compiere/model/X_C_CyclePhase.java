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
/** Generated Model for C_CyclePhase
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_CyclePhase.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_CyclePhase extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_CyclePhase_ID id
    @param trx transaction
    */
    public X_C_CyclePhase (Ctx ctx, int C_CyclePhase_ID, Trx trx)
    {
        super (ctx, C_CyclePhase_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_CyclePhase_ID == 0)
        {
            setC_CycleStep_ID (0);
            setC_Phase_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_CyclePhase (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=433 */
    public static final int Table_ID=433;
    
    /** TableName=C_CyclePhase */
    public static final String Table_Name="C_CyclePhase";
    
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_CycleStep_ID()));
        
    }
    
    /** Set Standard Phase.
    @param C_Phase_ID Standard Phase of the Project Type */
    public void setC_Phase_ID (int C_Phase_ID)
    {
        if (C_Phase_ID < 1) throw new IllegalArgumentException ("C_Phase_ID is mandatory.");
        set_ValueNoCheck ("C_Phase_ID", Integer.valueOf(C_Phase_ID));
        
    }
    
    /** Get Standard Phase.
    @return Standard Phase of the Project Type */
    public int getC_Phase_ID() 
    {
        return get_ValueAsInt("C_Phase_ID");
        
    }
    
    
}
