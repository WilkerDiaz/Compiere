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
/** Generated Model for AD_Element_Trl
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Element_Trl.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Element_Trl extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Element_Trl_ID id
    @param trx transaction
    */
    public X_AD_Element_Trl (Ctx ctx, int AD_Element_Trl_ID, Trx trx)
    {
        super (ctx, AD_Element_Trl_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Element_Trl_ID == 0)
        {
            setAD_Element_ID (0);
            setAD_Language (null);
            setIsTranslated (false);
            setName (null);
            setPrintName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Element_Trl (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27523942088789L;
    /** Last Updated Timestamp 2009-05-08 14:06:12.0 */
    public static final long updatedMS = 1241816772000L;
    /** AD_Table_ID=277 */
    public static final int Table_ID=277;
    
    /** TableName=AD_Element_Trl */
    public static final String Table_Name="AD_Element_Trl";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set System Element.
    @param AD_Element_ID System Element enables the central maintenance of column description and help. */
    public void setAD_Element_ID (int AD_Element_ID)
    {
        if (AD_Element_ID < 1) throw new IllegalArgumentException ("AD_Element_ID is mandatory.");
        set_ValueNoCheck ("AD_Element_ID", Integer.valueOf(AD_Element_ID));
        
    }
    
    /** Get System Element.
    @return System Element enables the central maintenance of column description and help. */
    public int getAD_Element_ID() 
    {
        return get_ValueAsInt("AD_Element_ID");
        
    }
    
    /** Set Language.
    @param AD_Language Language for this entity */
    public void setAD_Language (String AD_Language)
    {
        set_ValueNoCheck ("AD_Language", AD_Language);
        
    }
    
    /** Get Language.
    @return Language for this entity */
    public String getAD_Language() 
    {
        return (String)get_Value("AD_Language");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Language()));
        
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
    
    /** Set Translated.
    @param IsTranslated This column is translated */
    public void setIsTranslated (boolean IsTranslated)
    {
        set_Value ("IsTranslated", Boolean.valueOf(IsTranslated));
        
    }
    
    /** Get Translated.
    @return This column is translated */
    public boolean isTranslated() 
    {
        return get_ValueAsBoolean("IsTranslated");
        
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
    
    /** Set PO Description.
    @param PO_Description Description in PO Screens */
    public void setPO_Description (String PO_Description)
    {
        set_Value ("PO_Description", PO_Description);
        
    }
    
    /** Get PO Description.
    @return Description in PO Screens */
    public String getPO_Description() 
    {
        return (String)get_Value("PO_Description");
        
    }
    
    /** Set PO Help.
    @param PO_Help Help for PO Screens */
    public void setPO_Help (String PO_Help)
    {
        set_Value ("PO_Help", PO_Help);
        
    }
    
    /** Get PO Help.
    @return Help for PO Screens */
    public String getPO_Help() 
    {
        return (String)get_Value("PO_Help");
        
    }
    
    /** Set PO Name.
    @param PO_Name Name on PO Screens */
    public void setPO_Name (String PO_Name)
    {
        set_Value ("PO_Name", PO_Name);
        
    }
    
    /** Get PO Name.
    @return Name on PO Screens */
    public String getPO_Name() 
    {
        return (String)get_Value("PO_Name");
        
    }
    
    /** Set PO Print name.
    @param PO_PrintName Print name on PO Screens/Reports */
    public void setPO_PrintName (String PO_PrintName)
    {
        set_Value ("PO_PrintName", PO_PrintName);
        
    }
    
    /** Get PO Print name.
    @return Print name on PO Screens/Reports */
    public String getPO_PrintName() 
    {
        return (String)get_Value("PO_PrintName");
        
    }
    
    /** Set Print Text.
    @param PrintName The label text to be printed on a document or correspondence. */
    public void setPrintName (String PrintName)
    {
        if (PrintName == null) throw new IllegalArgumentException ("PrintName is mandatory.");
        set_Value ("PrintName", PrintName);
        
    }
    
    /** Get Print Text.
    @return The label text to be printed on a document or correspondence. */
    public String getPrintName() 
    {
        return (String)get_Value("PrintName");
        
    }
    
    
}
