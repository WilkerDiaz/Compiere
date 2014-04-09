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
/** Generated Model for W_MailMsg
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_W_MailMsg.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_W_MailMsg extends PO
{
    /** Standard Constructor
    @param ctx context
    @param W_MailMsg_ID id
    @param trx transaction
    */
    public X_W_MailMsg (Ctx ctx, int W_MailMsg_ID, Trx trx)
    {
        super (ctx, W_MailMsg_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (W_MailMsg_ID == 0)
        {
            setMailMsgType (null);
            setMessage (null);
            setName (null);
            setSubject (null);
            setW_MailMsg_ID (0);
            setW_Store_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_W_MailMsg (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=780 */
    public static final int Table_ID=780;
    
    /** TableName=W_MailMsg */
    public static final String Table_Name="W_MailMsg";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Subscribe = LS */
    public static final String MAILMSGTYPE_Subscribe = X_Ref_W_MailMsg_Type.SUBSCRIBE.getValue();
    /** Unsubscribe = LU */
    public static final String MAILMSGTYPE_Unsubscribe = X_Ref_W_MailMsg_Type.UNSUBSCRIBE.getValue();
    /** Order Acknowledgement = OA */
    public static final String MAILMSGTYPE_OrderAcknowledgement = X_Ref_W_MailMsg_Type.ORDER_ACKNOWLEDGEMENT.getValue();
    /** Payment Acknowledgement = PA */
    public static final String MAILMSGTYPE_PaymentAcknowledgement = X_Ref_W_MailMsg_Type.PAYMENT_ACKNOWLEDGEMENT.getValue();
    /** Payment Error = PE */
    public static final String MAILMSGTYPE_PaymentError = X_Ref_W_MailMsg_Type.PAYMENT_ERROR.getValue();
    /** User Account = UA */
    public static final String MAILMSGTYPE_UserAccount = X_Ref_W_MailMsg_Type.USER_ACCOUNT.getValue();
    /** User Password = UP */
    public static final String MAILMSGTYPE_UserPassword = X_Ref_W_MailMsg_Type.USER_PASSWORD.getValue();
    /** User Verification = UV */
    public static final String MAILMSGTYPE_UserVerification = X_Ref_W_MailMsg_Type.USER_VERIFICATION.getValue();
    /** Request = WR */
    public static final String MAILMSGTYPE_Request = X_Ref_W_MailMsg_Type.REQUEST.getValue();
    /** Set Message Type.
    @param MailMsgType Mail Message Type */
    public void setMailMsgType (String MailMsgType)
    {
        if (MailMsgType == null) throw new IllegalArgumentException ("MailMsgType is mandatory");
        if (!X_Ref_W_MailMsg_Type.isValid(MailMsgType))
        throw new IllegalArgumentException ("MailMsgType Invalid value - " + MailMsgType + " - Reference_ID=342 - LS - LU - OA - PA - PE - UA - UP - UV - WR");
        set_Value ("MailMsgType", MailMsgType);
        
    }
    
    /** Get Message Type.
    @return Mail Message Type */
    public String getMailMsgType() 
    {
        return (String)get_Value("MailMsgType");
        
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
    
    /** Set Web Store.
    @param W_Store_ID A Web Store of the Client */
    public void setW_Store_ID (int W_Store_ID)
    {
        if (W_Store_ID < 1) throw new IllegalArgumentException ("W_Store_ID is mandatory.");
        set_Value ("W_Store_ID", Integer.valueOf(W_Store_ID));
        
    }
    
    /** Get Web Store.
    @return A Web Store of the Client */
    public int getW_Store_ID() 
    {
        return get_ValueAsInt("W_Store_ID");
        
    }
    
    
}
