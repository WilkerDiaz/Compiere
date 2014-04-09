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
/** Generated Model for AD_EUL_User
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_EUL_User.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_EUL_User extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_EUL_User_ID id
    @param trx transaction
    */
    public X_AD_EUL_User (Ctx ctx, int AD_EUL_User_ID, Trx trx)
    {
        super (ctx, AD_EUL_User_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_EUL_User_ID == 0)
        {
            setAD_EUL_User_ID (0);
            setAD_Role_ID (0);
            setName (null);
            setPassword (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_EUL_User (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27514109750789L;
    /** Last Updated Timestamp 2009-01-14 17:53:54.0 */
    public static final long updatedMS = 1231984434000L;
    /** AD_Table_ID=1058 */
    public static final int Table_ID=1058;
    
    /** TableName=AD_EUL_User */
    public static final String Table_Name="AD_EUL_User";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set EUL User.
    @param AD_EUL_User_ID EUL User */
    public void setAD_EUL_User_ID (int AD_EUL_User_ID)
    {
        if (AD_EUL_User_ID < 1) throw new IllegalArgumentException ("AD_EUL_User_ID is mandatory.");
        set_ValueNoCheck ("AD_EUL_User_ID", Integer.valueOf(AD_EUL_User_ID));
        
    }
    
    /** Get EUL User.
    @return EUL User */
    public int getAD_EUL_User_ID() 
    {
        return get_ValueAsInt("AD_EUL_User_ID");
        
    }
    
    /** Set Role.
    @param AD_Role_ID Responsibility Role */
    public void setAD_Role_ID (int AD_Role_ID)
    {
        if (AD_Role_ID < 0) throw new IllegalArgumentException ("AD_Role_ID is mandatory.");
        set_ValueNoCheck ("AD_Role_ID", Integer.valueOf(AD_Role_ID));
        
    }
    
    /** Get Role.
    @return Responsibility Role */
    public int getAD_Role_ID() 
    {
        return get_ValueAsInt("AD_Role_ID");
        
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
    
    /** Set Password.
    @param Password Password of any length (case sensitive) */
    public void setPassword (String Password)
    {
        if (Password == null) throw new IllegalArgumentException ("Password is mandatory.");
        set_Value ("Password", Password);
        
    }
    
    /** Get Password.
    @return Password of any length (case sensitive) */
    public String getPassword() 
    {
        return (String)get_Value("Password");
        
    }
    
    
}
