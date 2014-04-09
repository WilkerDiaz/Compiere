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
/** Generated Model for C_RfQ_TopicSubscriber
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_RfQ_TopicSubscriber.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_RfQ_TopicSubscriber extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_RfQ_TopicSubscriber_ID id
    @param trx transaction
    */
    public X_C_RfQ_TopicSubscriber (Ctx ctx, int C_RfQ_TopicSubscriber_ID, Trx trx)
    {
        super (ctx, C_RfQ_TopicSubscriber_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_RfQ_TopicSubscriber_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_BPartner_Location_ID (0);
            setC_RfQ_TopicSubscriber_ID (0);
            setC_RfQ_Topic_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_RfQ_TopicSubscriber (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=670 */
    public static final int Table_ID=670;
    
    /** TableName=C_RfQ_TopicSubscriber */
    public static final String Table_Name="C_RfQ_TopicSubscriber";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID < 1) throw new IllegalArgumentException ("C_BPartner_Location_ID is mandatory.");
        set_Value ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
    }
    
    /** Set RfQ Subscriber.
    @param C_RfQ_TopicSubscriber_ID Request for Quotation Topic Subscriber */
    public void setC_RfQ_TopicSubscriber_ID (int C_RfQ_TopicSubscriber_ID)
    {
        if (C_RfQ_TopicSubscriber_ID < 1) throw new IllegalArgumentException ("C_RfQ_TopicSubscriber_ID is mandatory.");
        set_ValueNoCheck ("C_RfQ_TopicSubscriber_ID", Integer.valueOf(C_RfQ_TopicSubscriber_ID));
        
    }
    
    /** Get RfQ Subscriber.
    @return Request for Quotation Topic Subscriber */
    public int getC_RfQ_TopicSubscriber_ID() 
    {
        return get_ValueAsInt("C_RfQ_TopicSubscriber_ID");
        
    }
    
    /** Set RfQ Topic.
    @param C_RfQ_Topic_ID Topic for Request for Quotations */
    public void setC_RfQ_Topic_ID (int C_RfQ_Topic_ID)
    {
        if (C_RfQ_Topic_ID < 1) throw new IllegalArgumentException ("C_RfQ_Topic_ID is mandatory.");
        set_ValueNoCheck ("C_RfQ_Topic_ID", Integer.valueOf(C_RfQ_Topic_ID));
        
    }
    
    /** Get RfQ Topic.
    @return Topic for Request for Quotations */
    public int getC_RfQ_Topic_ID() 
    {
        return get_ValueAsInt("C_RfQ_Topic_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_RfQ_Topic_ID()));
        
    }
    
    /** Set Opt-out Date.
    @param OptOutDate Date the contact opted out */
    public void setOptOutDate (Timestamp OptOutDate)
    {
        set_Value ("OptOutDate", OptOutDate);
        
    }
    
    /** Get Opt-out Date.
    @return Date the contact opted out */
    public Timestamp getOptOutDate() 
    {
        return (Timestamp)get_Value("OptOutDate");
        
    }
    
    /** Set Subscribe Date.
    @param SubscribeDate Date the contact actively subscribed */
    public void setSubscribeDate (Timestamp SubscribeDate)
    {
        set_Value ("SubscribeDate", SubscribeDate);
        
    }
    
    /** Get Subscribe Date.
    @return Date the contact actively subscribed */
    public Timestamp getSubscribeDate() 
    {
        return (Timestamp)get_Value("SubscribeDate");
        
    }
    
    
}
