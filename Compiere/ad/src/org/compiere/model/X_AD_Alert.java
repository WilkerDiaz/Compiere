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
/** Generated Model for AD_Alert
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Alert.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Alert extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Alert_ID id
    @param trx transaction
    */
    public X_AD_Alert (Ctx ctx, int AD_Alert_ID, Trx trx)
    {
        super (ctx, AD_Alert_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Alert_ID == 0)
        {
            setAD_AlertProcessor_ID (0);
            setAD_Alert_ID (0);
            setAlertMessage (null);
            setAlertSubject (null);
            setEnforceClientSecurity (true);	// Y
            setEnforceRoleSecurity (true);	// Y
            setIsValid (true);	// Y
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Alert (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=594 */
    public static final int Table_ID=594;
    
    /** TableName=AD_Alert */
    public static final String Table_Name="AD_Alert";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Alert Processor.
    @param AD_AlertProcessor_ID Alert Processor/Server Parameter */
    public void setAD_AlertProcessor_ID (int AD_AlertProcessor_ID)
    {
        if (AD_AlertProcessor_ID < 1) throw new IllegalArgumentException ("AD_AlertProcessor_ID is mandatory.");
        set_Value ("AD_AlertProcessor_ID", Integer.valueOf(AD_AlertProcessor_ID));
        
    }
    
    /** Get Alert Processor.
    @return Alert Processor/Server Parameter */
    public int getAD_AlertProcessor_ID() 
    {
        return get_ValueAsInt("AD_AlertProcessor_ID");
        
    }
    
    /** Set Alert.
    @param AD_Alert_ID Compiere Alert */
    public void setAD_Alert_ID (int AD_Alert_ID)
    {
        if (AD_Alert_ID < 1) throw new IllegalArgumentException ("AD_Alert_ID is mandatory.");
        set_ValueNoCheck ("AD_Alert_ID", Integer.valueOf(AD_Alert_ID));
        
    }
    
    /** Get Alert.
    @return Compiere Alert */
    public int getAD_Alert_ID() 
    {
        return get_ValueAsInt("AD_Alert_ID");
        
    }
    
    /** Set Alert Message.
    @param AlertMessage Message of the Alert */
    public void setAlertMessage (String AlertMessage)
    {
        if (AlertMessage == null) throw new IllegalArgumentException ("AlertMessage is mandatory.");
        set_Value ("AlertMessage", AlertMessage);
        
    }
    
    /** Get Alert Message.
    @return Message of the Alert */
    public String getAlertMessage() 
    {
        return (String)get_Value("AlertMessage");
        
    }
    
    /** Set Alert Subject.
    @param AlertSubject Subject of the Alert */
    public void setAlertSubject (String AlertSubject)
    {
        if (AlertSubject == null) throw new IllegalArgumentException ("AlertSubject is mandatory.");
        set_Value ("AlertSubject", AlertSubject);
        
    }
    
    /** Get Alert Subject.
    @return Subject of the Alert */
    public String getAlertSubject() 
    {
        return (String)get_Value("AlertSubject");
        
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
    
    /** Set Enforce Tenant Security.
    @param EnforceClientSecurity Send alerts to recipient only if the Tenant security rules of the role allows */
    public void setEnforceClientSecurity (boolean EnforceClientSecurity)
    {
        set_Value ("EnforceClientSecurity", Boolean.valueOf(EnforceClientSecurity));
        
    }
    
    /** Get Enforce Tenant Security.
    @return Send alerts to recipient only if the Tenant security rules of the role allows */
    public boolean isEnforceClientSecurity() 
    {
        return get_ValueAsBoolean("EnforceClientSecurity");
        
    }
    
    /** Set Enforce Role Security.
    @param EnforceRoleSecurity Send alerts to recipient only if the data security rules of the role allows */
    public void setEnforceRoleSecurity (boolean EnforceRoleSecurity)
    {
        set_Value ("EnforceRoleSecurity", Boolean.valueOf(EnforceRoleSecurity));
        
    }
    
    /** Get Enforce Role Security.
    @return Send alerts to recipient only if the data security rules of the role allows */
    public boolean isEnforceRoleSecurity() 
    {
        return get_ValueAsBoolean("EnforceRoleSecurity");
        
    }
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set Valid.
    @param IsValid Element is valid */
    public void setIsValid (boolean IsValid)
    {
        set_Value ("IsValid", Boolean.valueOf(IsValid));
        
    }
    
    /** Get Valid.
    @return Element is valid */
    public boolean isValid() 
    {
        return get_ValueAsBoolean("IsValid");
        
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
