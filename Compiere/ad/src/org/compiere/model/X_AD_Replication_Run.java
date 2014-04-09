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
/** Generated Model for AD_Replication_Run
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Replication_Run.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Replication_Run extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Replication_Run_ID id
    @param trx transaction
    */
    public X_AD_Replication_Run (Ctx ctx, int AD_Replication_Run_ID, Trx trx)
    {
        super (ctx, AD_Replication_Run_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Replication_Run_ID == 0)
        {
            setAD_Replication_ID (0);
            setAD_Replication_Run_ID (0);
            setIsReplicated (false);	// N
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Replication_Run (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=603 */
    public static final int Table_ID=603;
    
    /** TableName=AD_Replication_Run */
    public static final String Table_Name="AD_Replication_Run";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Replication.
    @param AD_Replication_ID Data Replication Target */
    public void setAD_Replication_ID (int AD_Replication_ID)
    {
        if (AD_Replication_ID < 1) throw new IllegalArgumentException ("AD_Replication_ID is mandatory.");
        set_ValueNoCheck ("AD_Replication_ID", Integer.valueOf(AD_Replication_ID));
        
    }
    
    /** Get Replication.
    @return Data Replication Target */
    public int getAD_Replication_ID() 
    {
        return get_ValueAsInt("AD_Replication_ID");
        
    }
    
    /** Set Replication Run.
    @param AD_Replication_Run_ID Data Replication Run */
    public void setAD_Replication_Run_ID (int AD_Replication_Run_ID)
    {
        if (AD_Replication_Run_ID < 1) throw new IllegalArgumentException ("AD_Replication_Run_ID is mandatory.");
        set_ValueNoCheck ("AD_Replication_Run_ID", Integer.valueOf(AD_Replication_Run_ID));
        
    }
    
    /** Get Replication Run.
    @return Data Replication Run */
    public int getAD_Replication_Run_ID() 
    {
        return get_ValueAsInt("AD_Replication_Run_ID");
        
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
    
    /** Set Replicated.
    @param IsReplicated The data is successfully replicated */
    public void setIsReplicated (boolean IsReplicated)
    {
        set_ValueNoCheck ("IsReplicated", Boolean.valueOf(IsReplicated));
        
    }
    
    /** Get Replicated.
    @return The data is successfully replicated */
    public boolean isReplicated() 
    {
        return get_ValueAsBoolean("IsReplicated");
        
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
    
    
}
