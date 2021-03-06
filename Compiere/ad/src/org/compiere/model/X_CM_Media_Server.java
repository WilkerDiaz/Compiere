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
/** Generated Model for CM_Media_Server
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_Media_Server.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_Media_Server extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_Media_Server_ID id
    @param trx transaction
    */
    public X_CM_Media_Server (Ctx ctx, int CM_Media_Server_ID, Trx trx)
    {
        super (ctx, CM_Media_Server_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_Media_Server_ID == 0)
        {
            setCM_Media_Server_ID (0);
            setCM_WebProject_ID (0);
            setIsPassive (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_Media_Server (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=859 */
    public static final int Table_ID=859;
    
    /** TableName=CM_Media_Server */
    public static final String Table_Name="CM_Media_Server";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Web Project.
    @param CM_WebProject_ID A web project is the main data container for Containers, URLs, Ads, and Media etc. */
    public void setCM_WebProject_ID (int CM_WebProject_ID)
    {
        if (CM_WebProject_ID < 1) throw new IllegalArgumentException ("CM_WebProject_ID is mandatory.");
        set_ValueNoCheck ("CM_WebProject_ID", Integer.valueOf(CM_WebProject_ID));
        
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
    
    /** Set Folder.
    @param Folder A folder on a local or remote system to store data into */
    public void setFolder (String Folder)
    {
        set_Value ("Folder", Folder);
        
    }
    
    /** Get Folder.
    @return A folder on a local or remote system to store data into */
    public String getFolder() 
    {
        return (String)get_Value("Folder");
        
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
        set_Value ("IP_Address", IP_Address);
        
    }
    
    /** Get IP Address.
    @return Defines the IP address to transfer data to */
    public String getIP_Address() 
    {
        return (String)get_Value("IP_Address");
        
    }
    
    /** Set Transfer passive.
    @param IsPassive FTP passive transfer */
    public void setIsPassive (boolean IsPassive)
    {
        set_Value ("IsPassive", Boolean.valueOf(IsPassive));
        
    }
    
    /** Get Transfer passive.
    @return FTP passive transfer */
    public boolean isPassive() 
    {
        return get_ValueAsBoolean("IsPassive");
        
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
    
    /** Set Password.
    @param Password Password of any length (case sensitive) */
    public void setPassword (String Password)
    {
        set_Value ("Password", Password);
        
    }
    
    /** Get Password.
    @return Password of any length (case sensitive) */
    public String getPassword() 
    {
        return (String)get_Value("Password");
        
    }
    
    /** Set URL.
    @param URL Full URL address - e.g. http://www.compiere.org */
    public void setURL (String URL)
    {
        set_Value ("URL", URL);
        
    }
    
    /** Get URL.
    @return Full URL address - e.g. http://www.compiere.org */
    public String getURL() 
    {
        return (String)get_Value("URL");
        
    }
    
    /** Set Registered EMail.
    @param UserName Email of the responsible for the System */
    public void setUserName (String UserName)
    {
        set_Value ("UserName", UserName);
        
    }
    
    /** Get Registered EMail.
    @return Email of the responsible for the System */
    public String getUserName() 
    {
        return (String)get_Value("UserName");
        
    }
    
    
}
