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
/** Generated Model for CM_MediaDeploy
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_MediaDeploy.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_MediaDeploy extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_MediaDeploy_ID id
    @param trx transaction
    */
    public X_CM_MediaDeploy (Ctx ctx, int CM_MediaDeploy_ID, Trx trx)
    {
        super (ctx, CM_MediaDeploy_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_MediaDeploy_ID == 0)
        {
            setCM_MediaDeploy_ID (0);
            setCM_Media_ID (0);
            setCM_Media_Server_ID (0);
            setIsDeployed (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_MediaDeploy (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=892 */
    public static final int Table_ID=892;
    
    /** TableName=CM_MediaDeploy */
    public static final String Table_Name="CM_MediaDeploy";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Media Deploy.
    @param CM_MediaDeploy_ID Media Deployment Log */
    public void setCM_MediaDeploy_ID (int CM_MediaDeploy_ID)
    {
        if (CM_MediaDeploy_ID < 1) throw new IllegalArgumentException ("CM_MediaDeploy_ID is mandatory.");
        set_ValueNoCheck ("CM_MediaDeploy_ID", Integer.valueOf(CM_MediaDeploy_ID));
        
    }
    
    /** Get Media Deploy.
    @return Media Deployment Log */
    public int getCM_MediaDeploy_ID() 
    {
        return get_ValueAsInt("CM_MediaDeploy_ID");
        
    }
    
    /** Set Media Item.
    @param CM_Media_ID Contains media content like images, flash movies etc. */
    public void setCM_Media_ID (int CM_Media_ID)
    {
        if (CM_Media_ID < 1) throw new IllegalArgumentException ("CM_Media_ID is mandatory.");
        set_ValueNoCheck ("CM_Media_ID", Integer.valueOf(CM_Media_ID));
        
    }
    
    /** Get Media Item.
    @return Contains media content like images, flash movies etc. */
    public int getCM_Media_ID() 
    {
        return get_ValueAsInt("CM_Media_ID");
        
    }
    
    /** Set Media Server.
    @param CM_Media_Server_ID Media Server list to which content should get transferred */
    public void setCM_Media_Server_ID (int CM_Media_Server_ID)
    {
        if (CM_Media_Server_ID < 1) throw new IllegalArgumentException ("CM_Media_Server_ID is mandatory.");
        set_ValueNoCheck ("CM_Media_Server_ID", Integer.valueOf(CM_Media_Server_ID));
        
    }
    
    /** Get Media Server.
    @return Media Server list to which content should get transferred */
    public int getCM_Media_Server_ID() 
    {
        return get_ValueAsInt("CM_Media_Server_ID");
        
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
    
    /** Set Deployed.
    @param IsDeployed Entity is deployed */
    public void setIsDeployed (boolean IsDeployed)
    {
        set_Value ("IsDeployed", Boolean.valueOf(IsDeployed));
        
    }
    
    /** Get Deployed.
    @return Entity is deployed */
    public boolean isDeployed() 
    {
        return get_ValueAsBoolean("IsDeployed");
        
    }
    
    /** Set Last Synchronized.
    @param LastSynchronized Date when last synchronized */
    public void setLastSynchronized (Timestamp LastSynchronized)
    {
        set_Value ("LastSynchronized", LastSynchronized);
        
    }
    
    /** Get Last Synchronized.
    @return Date when last synchronized */
    public Timestamp getLastSynchronized() 
    {
        return (Timestamp)get_Value("LastSynchronized");
        
    }
    
    
}
