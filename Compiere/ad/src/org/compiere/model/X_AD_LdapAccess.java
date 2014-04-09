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
/** Generated Model for AD_LdapAccess
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_LdapAccess.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_LdapAccess extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_LdapAccess_ID id
    @param trx transaction
    */
    public X_AD_LdapAccess (Ctx ctx, int AD_LdapAccess_ID, Trx trx)
    {
        super (ctx, AD_LdapAccess_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_LdapAccess_ID == 0)
        {
            setAD_LdapAccess_ID (0);
            setAD_LdapProcessor_ID (0);
            setIsError (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_LdapAccess (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=904 */
    public static final int Table_ID=904;
    
    /** TableName=AD_LdapAccess */
    public static final String Table_Name="AD_LdapAccess";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Ldap Access.
    @param AD_LdapAccess_ID Ldap Access Log */
    public void setAD_LdapAccess_ID (int AD_LdapAccess_ID)
    {
        if (AD_LdapAccess_ID < 1) throw new IllegalArgumentException ("AD_LdapAccess_ID is mandatory.");
        set_ValueNoCheck ("AD_LdapAccess_ID", Integer.valueOf(AD_LdapAccess_ID));
        
    }
    
    /** Get Ldap Access.
    @return Ldap Access Log */
    public int getAD_LdapAccess_ID() 
    {
        return get_ValueAsInt("AD_LdapAccess_ID");
        
    }
    
    /** Set Ldap Processor.
    @param AD_LdapProcessor_ID LDAP Server to authenticate and authorize external systems based on Compiere */
    public void setAD_LdapProcessor_ID (int AD_LdapProcessor_ID)
    {
        if (AD_LdapProcessor_ID < 1) throw new IllegalArgumentException ("AD_LdapProcessor_ID is mandatory.");
        set_ValueNoCheck ("AD_LdapProcessor_ID", Integer.valueOf(AD_LdapProcessor_ID));
        
    }
    
    /** Get Ldap Processor.
    @return LDAP Server to authenticate and authorize external systems based on Compiere */
    public int getAD_LdapProcessor_ID() 
    {
        return get_ValueAsInt("AD_LdapProcessor_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_ValueNoCheck ("AD_User_ID", null);
        else
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
    
    /** Set Asset.
    @param A_Asset_ID Asset used internally or by customers */
    public void setA_Asset_ID (int A_Asset_ID)
    {
        if (A_Asset_ID <= 0) set_Value ("A_Asset_ID", null);
        else
        set_Value ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
        
    }
    
    /** Get Asset.
    @return Asset used internally or by customers */
    public int getA_Asset_ID() 
    {
        return get_ValueAsInt("A_Asset_ID");
        
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
    
    /** Set Error.
    @param IsError An Error that has occurred in the execution */
    public void setIsError (boolean IsError)
    {
        set_ValueNoCheck ("IsError", Boolean.valueOf(IsError));
        
    }
    
    /** Get Error.
    @return An Error that has occurred in the execution */
    public boolean isError() 
    {
        return get_ValueAsBoolean("IsError");
        
    }
    
    /** Set Interest Area.
    @param R_InterestArea_ID Interest Area or Topic */
    public void setR_InterestArea_ID (int R_InterestArea_ID)
    {
        if (R_InterestArea_ID <= 0) set_ValueNoCheck ("R_InterestArea_ID", null);
        else
        set_ValueNoCheck ("R_InterestArea_ID", Integer.valueOf(R_InterestArea_ID));
        
    }
    
    /** Get Interest Area.
    @return Interest Area or Topic */
    public int getR_InterestArea_ID() 
    {
        return get_ValueAsInt("R_InterestArea_ID");
        
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
    
    /** Set Summary.
    @param Summary Textual summary of this request */
    public void setSummary (String Summary)
    {
        set_ValueNoCheck ("Summary", Summary);
        
    }
    
    /** Get Summary.
    @return Textual summary of this request */
    public String getSummary() 
    {
        return (String)get_Value("Summary");
        
    }
    
    
}
