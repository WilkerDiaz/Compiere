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
/** Generated Model for AD_ReplicationTable
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ReplicationTable.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ReplicationTable extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ReplicationTable_ID id
    @param trx transaction
    */
    public X_AD_ReplicationTable (Ctx ctx, int AD_ReplicationTable_ID, Trx trx)
    {
        super (ctx, AD_ReplicationTable_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ReplicationTable_ID == 0)
        {
            setAD_ReplicationStrategy_ID (0);
            setAD_ReplicationTable_ID (0);
            setAD_Table_ID (0);
            setEntityType (null);	// U
            setReplicationType (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ReplicationTable (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=601 */
    public static final int Table_ID=601;
    
    /** TableName=AD_ReplicationTable */
    public static final String Table_Name="AD_ReplicationTable";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Replication Strategy.
    @param AD_ReplicationStrategy_ID Data Replication Strategy */
    public void setAD_ReplicationStrategy_ID (int AD_ReplicationStrategy_ID)
    {
        if (AD_ReplicationStrategy_ID < 1) throw new IllegalArgumentException ("AD_ReplicationStrategy_ID is mandatory.");
        set_ValueNoCheck ("AD_ReplicationStrategy_ID", Integer.valueOf(AD_ReplicationStrategy_ID));
        
    }
    
    /** Get Replication Strategy.
    @return Data Replication Strategy */
    public int getAD_ReplicationStrategy_ID() 
    {
        return get_ValueAsInt("AD_ReplicationStrategy_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_ReplicationStrategy_ID()));
        
    }
    
    /** Set Replication Table.
    @param AD_ReplicationTable_ID Data Replication Strategy Table Info */
    public void setAD_ReplicationTable_ID (int AD_ReplicationTable_ID)
    {
        if (AD_ReplicationTable_ID < 1) throw new IllegalArgumentException ("AD_ReplicationTable_ID is mandatory.");
        set_ValueNoCheck ("AD_ReplicationTable_ID", Integer.valueOf(AD_ReplicationTable_ID));
        
    }
    
    /** Get Replication Table.
    @return Data Replication Strategy Table Info */
    public int getAD_ReplicationTable_ID() 
    {
        return get_ValueAsInt("AD_ReplicationTable_ID");
        
    }
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_ValueNoCheck ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set Entity Type.
    @param EntityType Dictionary Entity Type;
     Determines ownership and synchronization */
    public void setEntityType (String EntityType)
    {
        set_Value ("EntityType", EntityType);
        
    }
    
    /** Get Entity Type.
    @return Dictionary Entity Type;
     Determines ownership and synchronization */
    public String getEntityType() 
    {
        return (String)get_Value("EntityType");
        
    }
    
    /** Local = L */
    public static final String REPLICATIONTYPE_Local = X_Ref_AD_Table_Replication_Type.LOCAL.getValue();
    /** Merge = M */
    public static final String REPLICATIONTYPE_Merge = X_Ref_AD_Table_Replication_Type.MERGE.getValue();
    /** Reference = R */
    public static final String REPLICATIONTYPE_Reference = X_Ref_AD_Table_Replication_Type.REFERENCE.getValue();
    /** Set Replication Type.
    @param ReplicationType Type of Data Replication */
    public void setReplicationType (String ReplicationType)
    {
        if (ReplicationType == null) throw new IllegalArgumentException ("ReplicationType is mandatory");
        if (!X_Ref_AD_Table_Replication_Type.isValid(ReplicationType))
        throw new IllegalArgumentException ("ReplicationType Invalid value - " + ReplicationType + " - Reference_ID=126 - L - M - R");
        set_Value ("ReplicationType", ReplicationType);
        
    }
    
    /** Get Replication Type.
    @return Type of Data Replication */
    public String getReplicationType() 
    {
        return (String)get_Value("ReplicationType");
        
    }
    
    
}
