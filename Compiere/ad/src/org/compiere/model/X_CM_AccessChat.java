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
/** Generated Model for CM_AccessChat
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_AccessChat.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_AccessChat extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_AccessChat_ID id
    @param trx transaction
    */
    public X_CM_AccessChat (Ctx ctx, int CM_AccessChat_ID, Trx trx)
    {
        super (ctx, CM_AccessChat_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_AccessChat_ID == 0)
        {
            setCM_AccessProfile_ID (0);
            setCM_Chat_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_AccessChat (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=907 */
    public static final int Table_ID=907;
    
    /** TableName=CM_AccessChat */
    public static final String Table_Name="CM_AccessChat";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Web Access Profile.
    @param CM_AccessProfile_ID Web Access Profile */
    public void setCM_AccessProfile_ID (int CM_AccessProfile_ID)
    {
        if (CM_AccessProfile_ID < 1) throw new IllegalArgumentException ("CM_AccessProfile_ID is mandatory.");
        set_ValueNoCheck ("CM_AccessProfile_ID", Integer.valueOf(CM_AccessProfile_ID));
        
    }
    
    /** Get Web Access Profile.
    @return Web Access Profile */
    public int getCM_AccessProfile_ID() 
    {
        return get_ValueAsInt("CM_AccessProfile_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getCM_AccessProfile_ID()));
        
    }
    
    /** Set Chat.
    @param CM_Chat_ID Chat or discussion thread */
    public void setCM_Chat_ID (int CM_Chat_ID)
    {
        if (CM_Chat_ID < 1) throw new IllegalArgumentException ("CM_Chat_ID is mandatory.");
        set_ValueNoCheck ("CM_Chat_ID", Integer.valueOf(CM_Chat_ID));
        
    }
    
    /** Get Chat.
    @return Chat or discussion thread */
    public int getCM_Chat_ID() 
    {
        return get_ValueAsInt("CM_Chat_ID");
        
    }
    
    
}
