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
/** Generated Model for AD_UserMail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_UserMail.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_UserMail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_UserMail_ID id
    @param trx transaction
    */
    public X_AD_UserMail (Ctx ctx, int AD_UserMail_ID, Trx trx)
    {
        super (ctx, AD_UserMail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_UserMail_ID == 0)
        {
            setAD_UserMail_ID (0);
            setAD_User_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_UserMail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=782 */
    public static final int Table_ID=782;
    
    /** TableName=AD_UserMail */
    public static final String Table_Name="AD_UserMail";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User Mail.
    @param AD_UserMail_ID Mail sent to the user */
    public void setAD_UserMail_ID (int AD_UserMail_ID)
    {
        if (AD_UserMail_ID < 1) throw new IllegalArgumentException ("AD_UserMail_ID is mandatory.");
        set_ValueNoCheck ("AD_UserMail_ID", Integer.valueOf(AD_UserMail_ID));
        
    }
    
    /** Get User Mail.
    @return Mail sent to the user */
    public int getAD_UserMail_ID() 
    {
        return get_ValueAsInt("AD_UserMail_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_ValueNoCheck ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_User_ID()));
        
    }
    
    /** Set Delivery Confirmation.
    @param DeliveryConfirmation EMail Delivery confirmation */
    public void setDeliveryConfirmation (String DeliveryConfirmation)
    {
        set_ValueNoCheck ("DeliveryConfirmation", DeliveryConfirmation);
        
    }
    
    /** Get Delivery Confirmation.
    @return EMail Delivery confirmation */
    public String getDeliveryConfirmation() 
    {
        return (String)get_Value("DeliveryConfirmation");
        
    }
    
    /** No = N */
    public static final String ISDELIVERED_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISDELIVERED_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Delivered.
    @param IsDelivered Delivered */
    public void setIsDelivered (String IsDelivered)
    {
        if (!X_Ref__YesNo.isValid(IsDelivered))
        throw new IllegalArgumentException ("IsDelivered Invalid value - " + IsDelivered + " - Reference_ID=319 - N - Y");
        set_ValueNoCheck ("IsDelivered", IsDelivered);
        
    }
    
    /** Get Delivered.
    @return Delivered */
    public String getIsDelivered() 
    {
        return (String)get_Value("IsDelivered");
        
    }
    
    /** Set Mail Text.
    @param MailText Text used for Mail message */
    public void setMailText (String MailText)
    {
        set_Value ("MailText", MailText);
        
    }
    
    /** Get Mail Text.
    @return Text used for Mail message */
    public String getMailText() 
    {
        return (String)get_Value("MailText");
        
    }
    
    /** Set Message ID.
    @param MessageID EMail Message ID */
    public void setMessageID (String MessageID)
    {
        set_ValueNoCheck ("MessageID", MessageID);
        
    }
    
    /** Get Message ID.
    @return EMail Message ID */
    public String getMessageID() 
    {
        return (String)get_Value("MessageID");
        
    }
    
    /** Set Mail Template.
    @param R_MailText_ID Text templates for mailings */
    public void setR_MailText_ID (int R_MailText_ID)
    {
        if (R_MailText_ID <= 0) set_ValueNoCheck ("R_MailText_ID", null);
        else
        set_ValueNoCheck ("R_MailText_ID", Integer.valueOf(R_MailText_ID));
        
    }
    
    /** Get Mail Template.
    @return Text templates for mailings */
    public int getR_MailText_ID() 
    {
        return get_ValueAsInt("R_MailText_ID");
        
    }
    
    /** Set Subject.
    @param Subject Email Message Subject */
    public void setSubject (String Subject)
    {
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
        if (W_MailMsg_ID <= 0) set_ValueNoCheck ("W_MailMsg_ID", null);
        else
        set_ValueNoCheck ("W_MailMsg_ID", Integer.valueOf(W_MailMsg_ID));
        
    }
    
    /** Get Mail Message.
    @return Web Store Mail Message Template */
    public int getW_MailMsg_ID() 
    {
        return get_ValueAsInt("W_MailMsg_ID");
        
    }
    
    
}
