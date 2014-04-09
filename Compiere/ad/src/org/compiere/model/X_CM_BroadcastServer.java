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
/** Generated Model for CM_BroadcastServer
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_BroadcastServer.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_BroadcastServer extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_BroadcastServer_ID id
    @param trx transaction
    */
    public X_CM_BroadcastServer (Ctx ctx, int CM_BroadcastServer_ID, Trx trx)
    {
        super (ctx, CM_BroadcastServer_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_BroadcastServer_ID == 0)
        {
            setCM_BroadcastServer_ID (0);
            setIP_Address (null);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_BroadcastServer (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=893 */
    public static final int Table_ID=893;
    
    /** TableName=CM_BroadcastServer */
    public static final String Table_Name="CM_BroadcastServer";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Broadcast Server.
    @param CM_BroadcastServer_ID Web Broadcast Server */
    public void setCM_BroadcastServer_ID (int CM_BroadcastServer_ID)
    {
        if (CM_BroadcastServer_ID < 1) throw new IllegalArgumentException ("CM_BroadcastServer_ID is mandatory.");
        set_ValueNoCheck ("CM_BroadcastServer_ID", Integer.valueOf(CM_BroadcastServer_ID));
        
    }
    
    /** Get Broadcast Server.
    @return Web Broadcast Server */
    public int getCM_BroadcastServer_ID() 
    {
        return get_ValueAsInt("CM_BroadcastServer_ID");
        
    }
    
    /** Set Web Project.
    @param CM_WebProject_ID A web project is the main data container for Containers, URLs, Ads, and Media etc. */
    public void setCM_WebProject_ID (int CM_WebProject_ID)
    {
        if (CM_WebProject_ID <= 0) set_Value ("CM_WebProject_ID", null);
        else
        set_Value ("CM_WebProject_ID", Integer.valueOf(CM_WebProject_ID));
        
    }
    
    /** Get Web Project.
    @return A web project is the main data container for Containers, URLs, Ads, and Media etc. */
    public int getCM_WebProject_ID() 
    {
        return get_ValueAsInt("CM_WebProject_ID");
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set IP Address.
    @param IP_Address Defines the IP address to transfer data to */
    public void setIP_Address (String IP_Address)
    {
        if (IP_Address == null) throw new IllegalArgumentException ("IP_Address is mandatory.");
        set_Value ("IP_Address", IP_Address);
        
    }
    
    /** Get IP Address.
    @return Defines the IP address to transfer data to */
    public String getIP_Address() 
    {
        return (String)get_Value("IP_Address");
        
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
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    
}
