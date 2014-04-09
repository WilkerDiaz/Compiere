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
/** Generated Model for AD_UserDef_Win
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_UserDef_Win.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_UserDef_Win extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_UserDef_Win_ID id
    @param trx transaction
    */
    public X_AD_UserDef_Win (Ctx ctx, int AD_UserDef_Win_ID, Trx trx)
    {
        super (ctx, AD_UserDef_Win_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_UserDef_Win_ID == 0)
        {
            setAD_UserDef_Win_ID (0);
            setAD_Window_ID (0);
            setCustomizationName (null);
            setEntityType (null);	// U
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_UserDef_Win (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=467 */
    public static final int Table_ID=467;
    
    /** TableName=AD_UserDef_Win */
    public static final String Table_Name="AD_UserDef_Win";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Language.
    @param AD_Language Language for this entity */
    public void setAD_Language (String AD_Language)
    {
        set_Value ("AD_Language", AD_Language);
        
    }
    
    /** Get Language.
    @return Language for this entity */
    public String getAD_Language() 
    {
        return (String)get_Value("AD_Language");
        
    }
    
    /** Set Role.
    @param AD_Role_ID Responsibility Role */
    public void setAD_Role_ID (int AD_Role_ID)
    {
        if (AD_Role_ID <= 0) set_Value ("AD_Role_ID", null);
        else
        set_Value ("AD_Role_ID", Integer.valueOf(AD_Role_ID));
        
    }
    
    /** Get Role.
    @return Responsibility Role */
    public int getAD_Role_ID() 
    {
        return get_ValueAsInt("AD_Role_ID");
        
    }
    
    /** Set User defined Window.
    @param AD_UserDef_Win_ID User defined Window */
    public void setAD_UserDef_Win_ID (int AD_UserDef_Win_ID)
    {
        if (AD_UserDef_Win_ID < 1) throw new IllegalArgumentException ("AD_UserDef_Win_ID is mandatory.");
        set_ValueNoCheck ("AD_UserDef_Win_ID", Integer.valueOf(AD_UserDef_Win_ID));
        
    }
    
    /** Get User defined Window.
    @return User defined Window */
    public int getAD_UserDef_Win_ID() 
    {
        return get_ValueAsInt("AD_UserDef_Win_ID");
        
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
    
    /** Set Window.
    @param AD_Window_ID Data entry or display window */
    public void setAD_Window_ID (int AD_Window_ID)
    {
        if (AD_Window_ID < 1) throw new IllegalArgumentException ("AD_Window_ID is mandatory.");
        set_Value ("AD_Window_ID", Integer.valueOf(AD_Window_ID));
        
    }
    
    /** Get Window.
    @return Data entry or display window */
    public int getAD_Window_ID() 
    {
        return get_ValueAsInt("AD_Window_ID");
        
    }
    
    /** Set Customization Name.
    @param CustomizationName Name of the customization */
    public void setCustomizationName (String CustomizationName)
    {
        if (CustomizationName == null) throw new IllegalArgumentException ("CustomizationName is mandatory.");
        set_Value ("CustomizationName", CustomizationName);
        
    }
    
    /** Get Customization Name.
    @return Name of the customization */
    public String getCustomizationName() 
    {
        return (String)get_Value("CustomizationName");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getCustomizationName());
        
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
    
    /** No = N */
    public static final String ISDEFAULT_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISDEFAULT_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (String IsDefault)
    {
        if (!X_Ref__YesNo.isValid(IsDefault))
        throw new IllegalArgumentException ("IsDefault Invalid value - " + IsDefault + " - Reference_ID=319 - N - Y");
        set_Value ("IsDefault", IsDefault);
        
    }
    
    /** Get Default.
    @return Default value */
    public String getIsDefault() 
    {
        return (String)get_Value("IsDefault");
        
    }
    
    /** No = N */
    public static final String ISREADONLY_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISREADONLY_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Read Only.
    @param IsReadOnly Field is read only */
    public void setIsReadOnly (String IsReadOnly)
    {
        if (!X_Ref__YesNo.isValid(IsReadOnly))
        throw new IllegalArgumentException ("IsReadOnly Invalid value - " + IsReadOnly + " - Reference_ID=319 - N - Y");
        set_Value ("IsReadOnly", IsReadOnly);
        
    }
    
    /** Get Read Only.
    @return Field is read only */
    public String getIsReadOnly() 
    {
        return (String)get_Value("IsReadOnly");
        
    }
    
    /** Set System Default.
    @param IsSystemDefault System Default value */
    public void setIsSystemDefault (boolean IsSystemDefault)
    {
        set_Value ("IsSystemDefault", Boolean.valueOf(IsSystemDefault));
        
    }
    
    /** Get System Default.
    @return System Default value */
    public boolean isSystemDefault() 
    {
        return get_ValueAsBoolean("IsSystemDefault");
        
    }
    
    /** Set User updateable.
    @param IsUserUpdateable The field can be updated by the user */
    public void setIsUserUpdateable (boolean IsUserUpdateable)
    {
        set_Value ("IsUserUpdateable", Boolean.valueOf(IsUserUpdateable));
        
    }
    
    /** Get User updateable.
    @return The field can be updated by the user */
    public boolean isUserUpdateable() 
    {
        return get_ValueAsBoolean("IsUserUpdateable");
        
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
    
    /** Set Window Height.
    @param WinHeight Window Height */
    public void setWinHeight (int WinHeight)
    {
        set_Value ("WinHeight", Integer.valueOf(WinHeight));
        
    }
    
    /** Get Window Height.
    @return Window Height */
    public int getWinHeight() 
    {
        return get_ValueAsInt("WinHeight");
        
    }
    
    /** Set Window Width.
    @param WinWidth Window Width */
    public void setWinWidth (int WinWidth)
    {
        set_Value ("WinWidth", Integer.valueOf(WinWidth));
        
    }
    
    /** Get Window Width.
    @return Window Width */
    public int getWinWidth() 
    {
        return get_ValueAsInt("WinWidth");
        
    }
    
    
}
