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
/** Generated Model for AD_Message
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Message.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Message extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Message_ID id
    @param trx transaction
    */
    public X_AD_Message (Ctx ctx, int AD_Message_ID, Trx trx)
    {
        super (ctx, AD_Message_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Message_ID == 0)
        {
            setAD_Message_ID (0);
            setEntityType (null);	// U
            setMsgText (null);
            setMsgType (null);	// I
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Message (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313774789L;
    /** Last Updated Timestamp 2009-03-04 09:40:58.0 */
    public static final long updatedMS = 1236188458000L;
    /** AD_Table_ID=109 */
    public static final int Table_ID=109;
    
    /** TableName=AD_Message */
    public static final String Table_Name="AD_Message";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Message.
    @param AD_Message_ID System Message */
    public void setAD_Message_ID (int AD_Message_ID)
    {
        if (AD_Message_ID < 1) throw new IllegalArgumentException ("AD_Message_ID is mandatory.");
        set_ValueNoCheck ("AD_Message_ID", Integer.valueOf(AD_Message_ID));
        
    }
    
    /** Get Message.
    @return System Message */
    public int getAD_Message_ID() 
    {
        return get_ValueAsInt("AD_Message_ID");
        
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
    
    /** Set Message Text.
    @param MsgText Textual Informational, Menu or Error Message */
    public void setMsgText (String MsgText)
    {
        if (MsgText == null) throw new IllegalArgumentException ("MsgText is mandatory.");
        set_Value ("MsgText", MsgText);
        
    }
    
    /** Get Message Text.
    @return Textual Informational, Menu or Error Message */
    public String getMsgText() 
    {
        return (String)get_Value("MsgText");
        
    }
    
    /** Set Message Tip.
    @param MsgTip Additional tip or help for this message */
    public void setMsgTip (String MsgTip)
    {
        set_Value ("MsgTip", MsgTip);
        
    }
    
    /** Get Message Tip.
    @return Additional tip or help for this message */
    public String getMsgTip() 
    {
        return (String)get_Value("MsgTip");
        
    }
    
    /** Error = E */
    public static final String MSGTYPE_Error = X_Ref_AD_Message_Type.ERROR.getValue();
    /** Information = I */
    public static final String MSGTYPE_Information = X_Ref_AD_Message_Type.INFORMATION.getValue();
    /** Menu = M */
    public static final String MSGTYPE_Menu = X_Ref_AD_Message_Type.MENU.getValue();
    /** Set Message Type.
    @param MsgType Type of message (Informational, Menu or Error) */
    public void setMsgType (String MsgType)
    {
        if (MsgType == null) throw new IllegalArgumentException ("MsgType is mandatory");
        if (!X_Ref_AD_Message_Type.isValid(MsgType))
        throw new IllegalArgumentException ("MsgType Invalid value - " + MsgType + " - Reference_ID=103 - E - I - M");
        set_Value ("MsgType", MsgType);
        
    }
    
    /** Get Message Type.
    @return Type of message (Informational, Menu or Error) */
    public String getMsgType() 
    {
        return (String)get_Value("MsgType");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getValue());
        
    }
    
    
}
