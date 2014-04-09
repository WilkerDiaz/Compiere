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
/** Generated Model for W_Counter
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_W_Counter.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_W_Counter extends PO
{
    /** Standard Constructor
    @param ctx context
    @param W_Counter_ID id
    @param trx transaction
    */
    public X_W_Counter (Ctx ctx, int W_Counter_ID, Trx trx)
    {
        super (ctx, W_Counter_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (W_Counter_ID == 0)
        {
            setPageURL (null);
            setProcessed (false);	// N
            setW_Counter_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_W_Counter (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=403 */
    public static final int Table_ID=403;
    
    /** TableName=W_Counter */
    public static final String Table_Name="W_Counter";
    
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
    
    /** Set Accept Language.
    @param AcceptLanguage Language accepted based on browser information */
    public void setAcceptLanguage (String AcceptLanguage)
    {
        set_Value ("AcceptLanguage", AcceptLanguage);
        
    }
    
    /** Get Accept Language.
    @return Language accepted based on browser information */
    public String getAcceptLanguage() 
    {
        return (String)get_Value("AcceptLanguage");
        
    }
    
    /** Set EMail Address.
    @param EMail Electronic Mail Address */
    public void setEMail (String EMail)
    {
        set_Value ("EMail", EMail);
        
    }
    
    /** Get EMail Address.
    @return Electronic Mail Address */
    public String getEMail() 
    {
        return (String)get_Value("EMail");
        
    }
    
    /** Set Page URL.
    @param PageURL Page URL */
    public void setPageURL (String PageURL)
    {
        if (PageURL == null) throw new IllegalArgumentException ("PageURL is mandatory.");
        set_Value ("PageURL", PageURL);
        
    }
    
    /** Get Page URL.
    @return Page URL */
    public String getPageURL() 
    {
        return (String)get_Value("PageURL");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Referrer.
    @param Referrer Referring web address */
    public void setReferrer (String Referrer)
    {
        set_Value ("Referrer", Referrer);
        
    }
    
    /** Get Referrer.
    @return Referring web address */
    public String getReferrer() 
    {
        return (String)get_Value("Referrer");
        
    }
    
    /** Set Remote Addr.
    @param Remote_Addr Remote Address */
    public void setRemote_Addr (String Remote_Addr)
    {
        set_Value ("Remote_Addr", Remote_Addr);
        
    }
    
    /** Get Remote Addr.
    @return Remote Address */
    public String getRemote_Addr() 
    {
        return (String)get_Value("Remote_Addr");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getRemote_Addr());
        
    }
    
    /** Set Remote Host.
    @param Remote_Host Remote host Info */
    public void setRemote_Host (String Remote_Host)
    {
        set_Value ("Remote_Host", Remote_Host);
        
    }
    
    /** Get Remote Host.
    @return Remote host Info */
    public String getRemote_Host() 
    {
        return (String)get_Value("Remote_Host");
        
    }
    
    /** Set User Agent.
    @param UserAgent Browser Used */
    public void setUserAgent (String UserAgent)
    {
        set_Value ("UserAgent", UserAgent);
        
    }
    
    /** Get User Agent.
    @return Browser Used */
    public String getUserAgent() 
    {
        return (String)get_Value("UserAgent");
        
    }
    
    /** Set Counter Count.
    @param W_CounterCount_ID Web Counter Count Management */
    public void setW_CounterCount_ID (int W_CounterCount_ID)
    {
        if (W_CounterCount_ID <= 0) set_ValueNoCheck ("W_CounterCount_ID", null);
        else
        set_ValueNoCheck ("W_CounterCount_ID", Integer.valueOf(W_CounterCount_ID));
        
    }
    
    /** Get Counter Count.
    @return Web Counter Count Management */
    public int getW_CounterCount_ID() 
    {
        return get_ValueAsInt("W_CounterCount_ID");
        
    }
    
    /** Set Web Counter.
    @param W_Counter_ID Individual Count hit */
    public void setW_Counter_ID (int W_Counter_ID)
    {
        if (W_Counter_ID < 1) throw new IllegalArgumentException ("W_Counter_ID is mandatory.");
        set_ValueNoCheck ("W_Counter_ID", Integer.valueOf(W_Counter_ID));
        
    }
    
    /** Get Web Counter.
    @return Individual Count hit */
    public int getW_Counter_ID() 
    {
        return get_ValueAsInt("W_Counter_ID");
        
    }
    
    
}
