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
/** Generated Model for AD_Window
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Window.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Window extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Window_ID id
    @param trx transaction
    */
    public X_AD_Window (Ctx ctx, int AD_Window_ID, Trx trx)
    {
        super (ctx, AD_Window_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Window_ID == 0)
        {
            setAD_Window_ID (0);
            setEntityType (null);	// U
            setIsBetaFunctionality (false);
            setIsDefault (false);
            setName (null);
            setWindowType (null);	// M
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Window (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313627789L;
    /** Last Updated Timestamp 2009-03-04 09:38:31.0 */
    public static final long updatedMS = 1236188311000L;
    /** AD_Table_ID=105 */
    public static final int Table_ID=105;
    
    /** TableName=AD_Window */
    public static final String Table_Name="AD_Window";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set System Color.
    @param AD_Color_ID Color for backgrounds or indicators */
    public void setAD_Color_ID (int AD_Color_ID)
    {
        if (AD_Color_ID <= 0) set_Value ("AD_Color_ID", null);
        else
        set_Value ("AD_Color_ID", Integer.valueOf(AD_Color_ID));
        
    }
    
    /** Get System Color.
    @return Color for backgrounds or indicators */
    public int getAD_Color_ID() 
    {
        return get_ValueAsInt("AD_Color_ID");
        
    }
    
    /** Set Context Area.
    @param AD_CtxArea_ID Business Domain Area Terminology */
    public void setAD_CtxArea_ID (int AD_CtxArea_ID)
    {
        if (AD_CtxArea_ID <= 0) set_Value ("AD_CtxArea_ID", null);
        else
        set_Value ("AD_CtxArea_ID", Integer.valueOf(AD_CtxArea_ID));
        
    }
    
    /** Get Context Area.
    @return Business Domain Area Terminology */
    public int getAD_CtxArea_ID() 
    {
        return get_ValueAsInt("AD_CtxArea_ID");
        
    }
    
    /** Set Image.
    @param AD_Image_ID Image or Icon */
    public void setAD_Image_ID (int AD_Image_ID)
    {
        if (AD_Image_ID <= 0) set_Value ("AD_Image_ID", null);
        else
        set_Value ("AD_Image_ID", Integer.valueOf(AD_Image_ID));
        
    }
    
    /** Get Image.
    @return Image or Icon */
    public int getAD_Image_ID() 
    {
        return get_ValueAsInt("AD_Image_ID");
        
    }
    
    /** Set Window.
    @param AD_Window_ID Data entry or display window */
    public void setAD_Window_ID (int AD_Window_ID)
    {
        if (AD_Window_ID < 1) throw new IllegalArgumentException ("AD_Window_ID is mandatory.");
        set_ValueNoCheck ("AD_Window_ID", Integer.valueOf(AD_Window_ID));
        
    }
    
    /** Get Window.
    @return Data entry or display window */
    public int getAD_Window_ID() 
    {
        return get_ValueAsInt("AD_Window_ID");
        
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
    
    /** Set Beta Functionality.
    @param IsBetaFunctionality This functionality is considered Beta */
    public void setIsBetaFunctionality (boolean IsBetaFunctionality)
    {
        set_Value ("IsBetaFunctionality", Boolean.valueOf(IsBetaFunctionality));
        
    }
    
    /** Get Beta Functionality.
    @return This functionality is considered Beta */
    public boolean isBetaFunctionality() 
    {
        return get_ValueAsBoolean("IsBetaFunctionality");
        
    }
    
    /** Set Customization Default.
    @param IsCustomDefault Default Customization */
    public void setIsCustomDefault (boolean IsCustomDefault)
    {
        set_Value ("IsCustomDefault", Boolean.valueOf(IsCustomDefault));
        
    }
    
    /** Get Customization Default.
    @return Default Customization */
    public boolean isCustomDefault() 
    {
        return get_ValueAsBoolean("IsCustomDefault");
        
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
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
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
    
    /** Maintain = M */
    public static final String WINDOWTYPE_Maintain = X_Ref_AD_Window_Types.MAINTAIN.getValue();
    /** Query Only = Q */
    public static final String WINDOWTYPE_QueryOnly = X_Ref_AD_Window_Types.QUERY_ONLY.getValue();
    /** Single Record = S */
    public static final String WINDOWTYPE_SingleRecord = X_Ref_AD_Window_Types.SINGLE_RECORD.getValue();
    /** Transaction = T */
    public static final String WINDOWTYPE_Transaction = X_Ref_AD_Window_Types.TRANSACTION.getValue();
    /** Set WindowType.
    @param WindowType Type or classification of a Window */
    public void setWindowType (String WindowType)
    {
        if (WindowType == null) throw new IllegalArgumentException ("WindowType is mandatory");
        if (!X_Ref_AD_Window_Types.isValid(WindowType))
        throw new IllegalArgumentException ("WindowType Invalid value - " + WindowType + " - Reference_ID=108 - M - Q - S - T");
        set_Value ("WindowType", WindowType);
        
    }
    
    /** Get WindowType.
    @return Type or classification of a Window */
    public String getWindowType() 
    {
        return (String)get_Value("WindowType");
        
    }
    
    
}
