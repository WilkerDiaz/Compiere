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
/** Generated Model for AD_Replication_Log
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Replication_Log.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Replication_Log extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Replication_Log_ID id
    @param trx transaction
    */
    public X_AD_Replication_Log (Ctx ctx, int AD_Replication_Log_ID, Trx trx)
    {
        super (ctx, AD_Replication_Log_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Replication_Log_ID == 0)
        {
            setAD_Replication_Log_ID (0);
            setAD_Replication_Run_ID (0);
            setIsReplicated (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Replication_Log (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=604 */
    public static final int Table_ID=604;
    
    /** TableName=AD_Replication_Log */
    public static final String Table_Name="AD_Replication_Log";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Replication Table.
    @param AD_ReplicationTable_ID Data Replication Strategy Table Info */
    public void setAD_ReplicationTable_ID (int AD_ReplicationTable_ID)
    {
        if (AD_ReplicationTable_ID <= 0) set_Value ("AD_ReplicationTable_ID", null);
        else
        set_Value ("AD_ReplicationTable_ID", Integer.valueOf(AD_ReplicationTable_ID));
        
    }
    
    /** Get Replication Table.
    @return Data Replication Strategy Table Info */
    public int getAD_ReplicationTable_ID() 
    {
        return get_ValueAsInt("AD_ReplicationTable_ID");
        
    }
    
    /** Set Replication Log.
    @param AD_Replication_Log_ID Data Replication Log Details */
    public void setAD_Replication_Log_ID (int AD_Replication_Log_ID)
    {
        if (AD_Replication_Log_ID < 1) throw new IllegalArgumentException ("AD_Replication_Log_ID is mandatory.");
        set_ValueNoCheck ("AD_Replication_Log_ID", Integer.valueOf(AD_Replication_Log_ID));
        
    }
    
    /** Get Replication Log.
    @return Data Replication Log Details */
    public int getAD_Replication_Log_ID() 
    {
        return get_ValueAsInt("AD_Replication_Log_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Replication_Run_ID()));
        
    }
    
    /** Set Replicated.
    @param IsReplicated The data is successfully replicated */
    public void setIsReplicated (boolean IsReplicated)
    {
        set_Value ("IsReplicated", Boolean.valueOf(IsReplicated));
        
    }
    
    /** Get Replicated.
    @return The data is successfully replicated */
    public boolean isReplicated() 
    {
        return get_ValueAsBoolean("IsReplicated");
        
    }
    
    /** Set Process Message.
    @param P_Msg Process Message */
    public void setP_Msg (String P_Msg)
    {
        set_Value ("P_Msg", P_Msg);
        
    }
    
    /** Get Process Message.
    @return Process Message */
    public String getP_Msg() 
    {
        return (String)get_Value("P_Msg");
        
    }
    
    
}
