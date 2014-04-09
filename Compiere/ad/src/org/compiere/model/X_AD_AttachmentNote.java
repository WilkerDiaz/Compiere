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
/** Generated Model for AD_AttachmentNote
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_AttachmentNote.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_AttachmentNote extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_AttachmentNote_ID id
    @param trx transaction
    */
    public X_AD_AttachmentNote (Ctx ctx, int AD_AttachmentNote_ID, Trx trx)
    {
        super (ctx, AD_AttachmentNote_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_AttachmentNote_ID == 0)
        {
            setAD_AttachmentNote_ID (0);
            setAD_Attachment_ID (0);
            setAD_User_ID (0);
            setTextMsg (null);
            setTitle (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_AttachmentNote (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=705 */
    public static final int Table_ID=705;
    
    /** TableName=AD_AttachmentNote */
    public static final String Table_Name="AD_AttachmentNote";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Attachment Note.
    @param AD_AttachmentNote_ID Personal Attachment Note */
    public void setAD_AttachmentNote_ID (int AD_AttachmentNote_ID)
    {
        if (AD_AttachmentNote_ID < 1) throw new IllegalArgumentException ("AD_AttachmentNote_ID is mandatory.");
        set_ValueNoCheck ("AD_AttachmentNote_ID", Integer.valueOf(AD_AttachmentNote_ID));
        
    }
    
    /** Get Attachment Note.
    @return Personal Attachment Note */
    public int getAD_AttachmentNote_ID() 
    {
        return get_ValueAsInt("AD_AttachmentNote_ID");
        
    }
    
    /** Set Attachment.
    @param AD_Attachment_ID Attachment for the document */
    public void setAD_Attachment_ID (int AD_Attachment_ID)
    {
        if (AD_Attachment_ID < 1) throw new IllegalArgumentException ("AD_Attachment_ID is mandatory.");
        set_ValueNoCheck ("AD_Attachment_ID", Integer.valueOf(AD_Attachment_ID));
        
    }
    
    /** Get Attachment.
    @return Attachment for the document */
    public int getAD_Attachment_ID() 
    {
        return get_ValueAsInt("AD_Attachment_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Text Message.
    @param TextMsg Text Message */
    public void setTextMsg (String TextMsg)
    {
        if (TextMsg == null) throw new IllegalArgumentException ("TextMsg is mandatory.");
        set_Value ("TextMsg", TextMsg);
        
    }
    
    /** Get Text Message.
    @return Text Message */
    public String getTextMsg() 
    {
        return (String)get_Value("TextMsg");
        
    }
    
    /** Set Title.
    @param Title Title of the Contact */
    public void setTitle (String Title)
    {
        if (Title == null) throw new IllegalArgumentException ("Title is mandatory.");
        set_Value ("Title", Title);
        
    }
    
    /** Get Title.
    @return Title of the Contact */
    public String getTitle() 
    {
        return (String)get_Value("Title");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getTitle());
        
    }
    
    
}
