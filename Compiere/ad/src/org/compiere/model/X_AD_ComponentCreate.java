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
/** Generated Model for AD_ComponentCreate
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ComponentCreate.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ComponentCreate extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ComponentCreate_ID id
    @param trx transaction
    */
    public X_AD_ComponentCreate (Ctx ctx, int AD_ComponentCreate_ID, Trx trx)
    {
        super (ctx, AD_ComponentCreate_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ComponentCreate_ID == 0)
        {
            setAD_ComponentCreate_ID (0);
            setAD_ComponentReg_ID (0);
            setEMail (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ComponentCreate (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=1006 */
    public static final int Table_ID=1006;
    
    /** TableName=AD_ComponentCreate */
    public static final String Table_Name="AD_ComponentCreate";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Component Create.
    @param AD_ComponentCreate_ID Component Create Log */
    public void setAD_ComponentCreate_ID (int AD_ComponentCreate_ID)
    {
        if (AD_ComponentCreate_ID < 1) throw new IllegalArgumentException ("AD_ComponentCreate_ID is mandatory.");
        set_ValueNoCheck ("AD_ComponentCreate_ID", Integer.valueOf(AD_ComponentCreate_ID));
        
    }
    
    /** Get Component Create.
    @return Component Create Log */
    public int getAD_ComponentCreate_ID() 
    {
        return get_ValueAsInt("AD_ComponentCreate_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_ComponentCreate_ID()));
        
    }
    
    /** Set Component Registration.
    @param AD_ComponentReg_ID Component Registration */
    public void setAD_ComponentReg_ID (int AD_ComponentReg_ID)
    {
        if (AD_ComponentReg_ID < 1) throw new IllegalArgumentException ("AD_ComponentReg_ID is mandatory.");
        set_ValueNoCheck ("AD_ComponentReg_ID", Integer.valueOf(AD_ComponentReg_ID));
        
    }
    
    /** Get Component Registration.
    @return Component Registration */
    public int getAD_ComponentReg_ID() 
    {
        return get_ValueAsInt("AD_ComponentReg_ID");
        
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
    
    /** Set EMail Address.
    @param EMail Electronic Mail Address */
    public void setEMail (String EMail)
    {
        if (EMail == null) throw new IllegalArgumentException ("EMail is mandatory.");
        set_Value ("EMail", EMail);
        
    }
    
    /** Get EMail Address.
    @return Electronic Mail Address */
    public String getEMail() 
    {
        return (String)get_Value("EMail");
        
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
    
    /** Set Response Text.
    @param ResponseText Request Response Text */
    public void setResponseText (String ResponseText)
    {
        set_Value ("ResponseText", ResponseText);
        
    }
    
    /** Get Response Text.
    @return Request Response Text */
    public String getResponseText() 
    {
        return (String)get_Value("ResponseText");
        
    }
    
    
}
