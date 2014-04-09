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
/** Generated Model for AD_QueryLog
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_QueryLog.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_QueryLog extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_QueryLog_ID id
    @param trx transaction
    */
    public X_AD_QueryLog (Ctx ctx, int AD_QueryLog_ID, Trx trx)
    {
        super (ctx, AD_QueryLog_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_QueryLog_ID == 0)
        {
            setAD_QueryLog_ID (0);
            setAD_Session_ID (0);
            setRecordCount (0);
            setWhereClause (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_QueryLog (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=942 */
    public static final int Table_ID=942;
    
    /** TableName=AD_QueryLog */
    public static final String Table_Name="AD_QueryLog";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Query Log.
    @param AD_QueryLog_ID Database query log */
    public void setAD_QueryLog_ID (int AD_QueryLog_ID)
    {
        if (AD_QueryLog_ID < 1) throw new IllegalArgumentException ("AD_QueryLog_ID is mandatory.");
        set_ValueNoCheck ("AD_QueryLog_ID", Integer.valueOf(AD_QueryLog_ID));
        
    }
    
    /** Get Query Log.
    @return Database query log */
    public int getAD_QueryLog_ID() 
    {
        return get_ValueAsInt("AD_QueryLog_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_QueryLog_ID()));
        
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
    
    /** Set Session.
    @param AD_Session_ID User Session Online or Web */
    public void setAD_Session_ID (int AD_Session_ID)
    {
        if (AD_Session_ID < 1) throw new IllegalArgumentException ("AD_Session_ID is mandatory.");
        set_ValueNoCheck ("AD_Session_ID", Integer.valueOf(AD_Session_ID));
        
    }
    
    /** Get Session.
    @return User Session Online or Web */
    public int getAD_Session_ID() 
    {
        return get_ValueAsInt("AD_Session_ID");
        
    }
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID <= 0) set_Value ("AD_Table_ID", null);
        else
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set Parameter.
    @param Parameter Parameter */
    public void setParameter (String Parameter)
    {
        set_Value ("Parameter", Parameter);
        
    }
    
    /** Get Parameter.
    @return Parameter */
    public String getParameter() 
    {
        return (String)get_Value("Parameter");
        
    }
    
    /** Set Record Count.
    @param RecordCount Number of Records */
    public void setRecordCount (int RecordCount)
    {
        set_Value ("RecordCount", Integer.valueOf(RecordCount));
        
    }
    
    /** Get Record Count.
    @return Number of Records */
    public int getRecordCount() 
    {
        return get_ValueAsInt("RecordCount");
        
    }
    
    /** Set Sql WHERE.
    @param WhereClause Fully qualified SQL WHERE clause */
    public void setWhereClause (String WhereClause)
    {
        if (WhereClause == null) throw new IllegalArgumentException ("WhereClause is mandatory.");
        set_Value ("WhereClause", WhereClause);
        
    }
    
    /** Get Sql WHERE.
    @return Fully qualified SQL WHERE clause */
    public String getWhereClause() 
    {
        return (String)get_Value("WhereClause");
        
    }
    
    
}
