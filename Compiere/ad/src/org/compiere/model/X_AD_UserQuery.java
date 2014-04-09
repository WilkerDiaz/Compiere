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
/** Generated Model for AD_UserQuery
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_UserQuery.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_UserQuery extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_UserQuery_ID id
    @param trx transaction
    */
    public X_AD_UserQuery (Ctx ctx, int AD_UserQuery_ID, Trx trx)
    {
        super (ctx, AD_UserQuery_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_UserQuery_ID == 0)
        {
            setAD_Table_ID (0);
            setAD_UserQuery_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_UserQuery (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=814 */
    public static final int Table_ID=814;
    
    /** TableName=AD_UserQuery */
    public static final String Table_Name="AD_UserQuery";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Tab.
    @param AD_Tab_ID Tab within a Window */
    public void setAD_Tab_ID (int AD_Tab_ID)
    {
        if (AD_Tab_ID <= 0) set_Value ("AD_Tab_ID", null);
        else
        set_Value ("AD_Tab_ID", Integer.valueOf(AD_Tab_ID));
        
    }
    
    /** Get Tab.
    @return Tab within a Window */
    public int getAD_Tab_ID() 
    {
        return get_ValueAsInt("AD_Tab_ID");
        
    }
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set User Query.
    @param AD_UserQuery_ID Saved User Query */
    public void setAD_UserQuery_ID (int AD_UserQuery_ID)
    {
        if (AD_UserQuery_ID < 1) throw new IllegalArgumentException ("AD_UserQuery_ID is mandatory.");
        set_ValueNoCheck ("AD_UserQuery_ID", Integer.valueOf(AD_UserQuery_ID));
        
    }
    
    /** Get User Query.
    @return Saved User Query */
    public int getAD_UserQuery_ID() 
    {
        return get_ValueAsInt("AD_UserQuery_ID");
        
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
    
    /** Set Code.
    @param Code Code to execute or to validate */
    public void setCode (String Code)
    {
        set_Value ("Code", Code);
        
    }
    
    /** Get Code.
    @return Code to execute or to validate */
    public String getCode() 
    {
        return (String)get_Value("Code");
        
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
    
    
}
