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
/** Generated Model for W_MailMsg_Trl
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_W_MailMsg_Trl.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_W_MailMsg_Trl extends PO
{
    /** Standard Constructor
    @param ctx context
    @param W_MailMsg_Trl_ID id
    @param trx transaction
    */
    public X_W_MailMsg_Trl (Ctx ctx, int W_MailMsg_Trl_ID, Trx trx)
    {
        super (ctx, W_MailMsg_Trl_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (W_MailMsg_Trl_ID == 0)
        {
            setAD_Language (null);
            setIsTranslated (false);
            setMessage (null);
            setSubject (null);
            setW_MailMsg_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_W_MailMsg_Trl (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27523942959789L;
    /** Last Updated Timestamp 2009-05-08 14:20:43.0 */
    public static final long updatedMS = 1241817643000L;
    /** AD_Table_ID=781 */
    public static final int Table_ID=781;
    
    /** TableName=W_MailMsg_Trl */
    public static final String Table_Name="W_MailMsg_Trl";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Language.
    @param AD_Language Language for this entity */
    public void setAD_Language (String AD_Language)
    {
        set_ValueNoCheck ("AD_Language", AD_Language);
        
    }
    
    /** Get Language.
    @return Language for this entity */
    public String getAD_Language() 
    {
        return (String)get_Value("AD_Language");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Language()));
        
    }
    
    /** Set Translated.
    @param IsTranslated This column is translated */
    public void setIsTranslated (boolean IsTranslated)
    {
        set_Value ("IsTranslated", Boolean.valueOf(IsTranslated));
        
    }
    
    /** Get Translated.
    @return This column is translated */
    public boolean isTranslated() 
    {
        return get_ValueAsBoolean("IsTranslated");
        
    }
    
    /** Set Message.
    @param Message EMail Message */
    public void setMessage (String Message)
    {
        if (Message == null) throw new IllegalArgumentException ("Message is mandatory.");
        set_Value ("Message", Message);
        
    }
    
    /** Get Message.
    @return EMail Message */
    public String getMessage() 
    {
        return (String)get_Value("Message");
        
    }
    
    /** Set Message 2.
    @param Message2 Optional second part of the EMail Message */
    public void setMessage2 (String Message2)
    {
        set_Value ("Message2", Message2);
        
    }
    
    /** Get Message 2.
    @return Optional second part of the EMail Message */
    public String getMessage2() 
    {
        return (String)get_Value("Message2");
        
    }
    
    /** Set Message 3.
    @param Message3 Optional third part of the EMail Message */
    public void setMessage3 (String Message3)
    {
        set_Value ("Message3", Message3);
        
    }
    
    /** Get Message 3.
    @return Optional third part of the EMail Message */
    public String getMessage3() 
    {
        return (String)get_Value("Message3");
        
    }
    
    /** Set Subject.
    @param Subject Email Message Subject */
    public void setSubject (String Subject)
    {
        if (Subject == null) throw new IllegalArgumentException ("Subject is mandatory.");
        set_Value ("Subject", Subject);
        
    }
    
    /** Get Subject.
    @return Email Message Subject */
    public String getSubject() 
    {
        return (String)get_Value("Subject");
        
    }
    
    /** Set Mail Message.
    @param W_MailMsg_ID Web Store Mail Message Template */
    public void setW_MailMsg_ID (int W_MailMsg_ID)
    {
        if (W_MailMsg_ID < 1) throw new IllegalArgumentException ("W_MailMsg_ID is mandatory.");
        set_ValueNoCheck ("W_MailMsg_ID", Integer.valueOf(W_MailMsg_ID));
        
    }
    
    /** Get Mail Message.
    @return Web Store Mail Message Template */
    public int getW_MailMsg_ID() 
    {
        return get_ValueAsInt("W_MailMsg_ID");
        
    }
    
    
}
