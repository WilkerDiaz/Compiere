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
/** Generated Model for C_Greeting
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Greeting.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Greeting extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Greeting_ID id
    @param trx transaction
    */
    public X_C_Greeting (Ctx ctx, int C_Greeting_ID, Trx trx)
    {
        super (ctx, C_Greeting_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Greeting_ID == 0)
        {
            setC_Greeting_ID (0);
            setIsDefault (false);
            setIsFirstNameOnly (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Greeting (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=346 */
    public static final int Table_ID=346;
    
    /** TableName=C_Greeting */
    public static final String Table_Name="C_Greeting";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Greeting.
    @param C_Greeting_ID Greeting to print on correspondence */
    public void setC_Greeting_ID (int C_Greeting_ID)
    {
        if (C_Greeting_ID < 1) throw new IllegalArgumentException ("C_Greeting_ID is mandatory.");
        set_ValueNoCheck ("C_Greeting_ID", Integer.valueOf(C_Greeting_ID));
        
    }
    
    /** Get Greeting.
    @return Greeting to print on correspondence */
    public int getC_Greeting_ID() 
    {
        return get_ValueAsInt("C_Greeting_ID");
        
    }
    
    /** Set Greeting.
    @param Greeting For letters, e.g. "Dear 
    {
        0
    }
    " or "Dear Mr. 
    {
        0
    }
    " - At runtime, "
    {
        0
    }
    " is replaced by the name */
    public void setGreeting (String Greeting)
    {
        set_Value ("Greeting", Greeting);
        
    }
    
    /** Get Greeting.
    @return For letters, e.g. "Dear 
    {
        0
    }
    " or "Dear Mr. 
    {
        0
    }
    " - At runtime, "
    {
        0
    }
    " is replaced by the name */
    public String getGreeting() 
    {
        return (String)get_Value("Greeting");
        
    }
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
    }
    
    /** Set First name only.
    @param IsFirstNameOnly Print only the first name in greetings */
    public void setIsFirstNameOnly (boolean IsFirstNameOnly)
    {
        set_Value ("IsFirstNameOnly", Boolean.valueOf(IsFirstNameOnly));
        
    }
    
    /** Get First name only.
    @return Print only the first name in greetings */
    public boolean isFirstNameOnly() 
    {
        return get_ValueAsBoolean("IsFirstNameOnly");
        
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
