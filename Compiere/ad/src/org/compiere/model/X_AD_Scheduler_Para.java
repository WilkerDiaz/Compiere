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
/** Generated Model for AD_Scheduler_Para
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Scheduler_Para.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Scheduler_Para extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Scheduler_Para_ID id
    @param trx transaction
    */
    public X_AD_Scheduler_Para (Ctx ctx, int AD_Scheduler_Para_ID, Trx trx)
    {
        super (ctx, AD_Scheduler_Para_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Scheduler_Para_ID == 0)
        {
            setAD_Process_Para_ID (0);
            setAD_Scheduler_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Scheduler_Para (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=698 */
    public static final int Table_ID=698;
    
    /** TableName=AD_Scheduler_Para */
    public static final String Table_Name="AD_Scheduler_Para";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Parameter.
    @param AD_Process_Para_ID Process Parameter */
    public void setAD_Process_Para_ID (int AD_Process_Para_ID)
    {
        if (AD_Process_Para_ID < 1) throw new IllegalArgumentException ("AD_Process_Para_ID is mandatory.");
        set_ValueNoCheck ("AD_Process_Para_ID", Integer.valueOf(AD_Process_Para_ID));
        
    }
    
    /** Get Process Parameter.
    @return Process Parameter */
    public int getAD_Process_Para_ID() 
    {
        return get_ValueAsInt("AD_Process_Para_ID");
        
    }
    
    /** Set Scheduler.
    @param AD_Scheduler_ID Schedule Processes */
    public void setAD_Scheduler_ID (int AD_Scheduler_ID)
    {
        if (AD_Scheduler_ID < 1) throw new IllegalArgumentException ("AD_Scheduler_ID is mandatory.");
        set_ValueNoCheck ("AD_Scheduler_ID", Integer.valueOf(AD_Scheduler_ID));
        
    }
    
    /** Get Scheduler.
    @return Schedule Processes */
    public int getAD_Scheduler_ID() 
    {
        return get_ValueAsInt("AD_Scheduler_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Scheduler_ID()));
        
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
    
    /** Set Default Parameter.
    @param ParameterDefault Default value of the parameter */
    public void setParameterDefault (String ParameterDefault)
    {
        set_Value ("ParameterDefault", ParameterDefault);
        
    }
    
    /** Get Default Parameter.
    @return Default value of the parameter */
    public String getParameterDefault() 
    {
        return (String)get_Value("ParameterDefault");
        
    }
    
    
}
