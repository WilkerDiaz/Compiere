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
/** Generated Model for S_ResourceAssignment
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_S_ResourceAssignment.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_S_ResourceAssignment extends PO
{
    /** Standard Constructor
    @param ctx context
    @param S_ResourceAssignment_ID id
    @param trx transaction
    */
    public X_S_ResourceAssignment (Ctx ctx, int S_ResourceAssignment_ID, Trx trx)
    {
        super (ctx, S_ResourceAssignment_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (S_ResourceAssignment_ID == 0)
        {
            setAssignDateFrom (new Timestamp(System.currentTimeMillis()));
            setIsConfirmed (false);
            setName (null);
            setS_ResourceAssignment_ID (0);
            setS_Resource_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_S_ResourceAssignment (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=485 */
    public static final int Table_ID=485;
    
    /** TableName=S_ResourceAssignment */
    public static final String Table_Name="S_ResourceAssignment";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Assign From.
    @param AssignDateFrom Assign resource from */
    public void setAssignDateFrom (Timestamp AssignDateFrom)
    {
        if (AssignDateFrom == null) throw new IllegalArgumentException ("AssignDateFrom is mandatory.");
        set_ValueNoCheck ("AssignDateFrom", AssignDateFrom);
        
    }
    
    /** Get Assign From.
    @return Assign resource from */
    public Timestamp getAssignDateFrom() 
    {
        return (Timestamp)get_Value("AssignDateFrom");
        
    }
    
    /** Set Assign To.
    @param AssignDateTo Assign resource until */
    public void setAssignDateTo (Timestamp AssignDateTo)
    {
        set_ValueNoCheck ("AssignDateTo", AssignDateTo);
        
    }
    
    /** Get Assign To.
    @return Assign resource until */
    public Timestamp getAssignDateTo() 
    {
        return (Timestamp)get_Value("AssignDateTo");
        
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
    
    /** Set Confirmed.
    @param IsConfirmed Assignment is confirmed */
    public void setIsConfirmed (boolean IsConfirmed)
    {
        set_ValueNoCheck ("IsConfirmed", Boolean.valueOf(IsConfirmed));
        
    }
    
    /** Get Confirmed.
    @return Assignment is confirmed */
    public boolean isConfirmed() 
    {
        return get_ValueAsBoolean("IsConfirmed");
        
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
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        set_ValueNoCheck ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    /** Set Assigned Resource.
    @param S_ResourceAssignment_ID Assigned Resource */
    public void setS_ResourceAssignment_ID (int S_ResourceAssignment_ID)
    {
        if (S_ResourceAssignment_ID < 1) throw new IllegalArgumentException ("S_ResourceAssignment_ID is mandatory.");
        set_ValueNoCheck ("S_ResourceAssignment_ID", Integer.valueOf(S_ResourceAssignment_ID));
        
    }
    
    /** Get Assigned Resource.
    @return Assigned Resource */
    public int getS_ResourceAssignment_ID() 
    {
        return get_ValueAsInt("S_ResourceAssignment_ID");
        
    }
    
    /** Set Resource.
    @param S_Resource_ID Resource */
    public void setS_Resource_ID (int S_Resource_ID)
    {
        if (S_Resource_ID < 1) throw new IllegalArgumentException ("S_Resource_ID is mandatory.");
        set_ValueNoCheck ("S_Resource_ID", Integer.valueOf(S_Resource_ID));
        
    }
    
    /** Get Resource.
    @return Resource */
    public int getS_Resource_ID() 
    {
        return get_ValueAsInt("S_Resource_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getS_Resource_ID()));
        
    }
    
    
}
