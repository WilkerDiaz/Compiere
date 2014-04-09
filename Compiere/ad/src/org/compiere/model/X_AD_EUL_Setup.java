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
/** Generated Model for AD_EUL_Setup
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_EUL_Setup.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_EUL_Setup extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_EUL_Setup_ID id
    @param trx transaction
    */
    public X_AD_EUL_Setup (Ctx ctx, int AD_EUL_Setup_ID, Trx trx)
    {
        super (ctx, AD_EUL_Setup_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_EUL_Setup_ID == 0)
        {
            setAD_EUL_Setup_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_EUL_Setup (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27497759402789L;
    /** Last Updated Timestamp 2008-07-09 13:08:06.0 */
    public static final long updatedMS = 1215634086000L;
    /** AD_Table_ID=1059 */
    public static final int Table_ID=1059;
    
    /** TableName=AD_EUL_Setup */
    public static final String Table_Name="AD_EUL_Setup";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set BVL Setup.
    @param AD_EUL_Setup_ID Identifies the setup parameters for the Business View Layer */
    public void setAD_EUL_Setup_ID (int AD_EUL_Setup_ID)
    {
        if (AD_EUL_Setup_ID < 1) throw new IllegalArgumentException ("AD_EUL_Setup_ID is mandatory.");
        set_ValueNoCheck ("AD_EUL_Setup_ID", Integer.valueOf(AD_EUL_Setup_ID));
        
    }
    
    /** Get BVL Setup.
    @return Identifies the setup parameters for the Business View Layer */
    public int getAD_EUL_Setup_ID() 
    {
        return get_ValueAsInt("AD_EUL_Setup_ID");
        
    }
    
    /** Set Create BVL User.
    @param CreateEULUser Create BVL User */
    public void setCreateEULUser (String CreateEULUser)
    {
        set_Value ("CreateEULUser", CreateEULUser);
        
    }
    
    /** Get Create BVL User.
    @return Create BVL User */
    public String getCreateEULUser() 
    {
        return (String)get_Value("CreateEULUser");
        
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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
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
