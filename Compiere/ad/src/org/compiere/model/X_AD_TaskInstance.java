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
/** Generated Model for AD_TaskInstance
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_TaskInstance.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_TaskInstance extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_TaskInstance_ID id
    @param trx transaction
    */
    public X_AD_TaskInstance (Ctx ctx, int AD_TaskInstance_ID, Trx trx)
    {
        super (ctx, AD_TaskInstance_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_TaskInstance_ID == 0)
        {
            setAD_TaskInstance_ID (0);
            setAD_Task_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_TaskInstance (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511142251789L;
    /** Last Updated Timestamp 2008-12-11 09:35:35.0 */
    public static final long updatedMS = 1229016935000L;
    /** AD_Table_ID=125 */
    public static final int Table_ID=125;
    
    /** TableName=AD_TaskInstance */
    public static final String Table_Name="AD_TaskInstance";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Task Instance.
    @param AD_TaskInstance_ID Task Instance */
    public void setAD_TaskInstance_ID (int AD_TaskInstance_ID)
    {
        if (AD_TaskInstance_ID < 1) throw new IllegalArgumentException ("AD_TaskInstance_ID is mandatory.");
        set_ValueNoCheck ("AD_TaskInstance_ID", Integer.valueOf(AD_TaskInstance_ID));
        
    }
    
    /** Get Task Instance.
    @return Task Instance */
    public int getAD_TaskInstance_ID() 
    {
        return get_ValueAsInt("AD_TaskInstance_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_TaskInstance_ID()));
        
    }
    
    /** Set OS Task.
    @param AD_Task_ID Operation System Task */
    public void setAD_Task_ID (int AD_Task_ID)
    {
        if (AD_Task_ID < 1) throw new IllegalArgumentException ("AD_Task_ID is mandatory.");
        set_Value ("AD_Task_ID", Integer.valueOf(AD_Task_ID));
        
    }
    
    /** Get OS Task.
    @return Operation System Task */
    public int getAD_Task_ID() 
    {
        return get_ValueAsInt("AD_Task_ID");
        
    }
    
    
}
