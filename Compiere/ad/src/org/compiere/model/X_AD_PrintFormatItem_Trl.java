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
/** Generated Model for AD_PrintFormatItem_Trl
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_AD_PrintFormatItem_Trl.java 9136 2010-07-20 16:18:39Z ragrawal $ */
public class X_AD_PrintFormatItem_Trl extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_PrintFormatItem_Trl_ID id
    @param trx transaction
    */
    public X_AD_PrintFormatItem_Trl (Ctx ctx, int AD_PrintFormatItem_Trl_ID, Trx trx)
    {
        super (ctx, AD_PrintFormatItem_Trl_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_PrintFormatItem_Trl_ID == 0)
        {
            setAD_Language (null);
            setAD_PrintFormatItem_ID (0);
            setIsTranslated (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_PrintFormatItem_Trl (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27561722591789L;
    /** Last Updated Timestamp 2010-07-20 09:11:15.0 */
    public static final long updatedMS = 1279597275000L;
    /** AD_Table_ID=522 */
    public static final int Table_ID=522;
    
    /** TableName=AD_PrintFormatItem_Trl */
    public static final String Table_Name="AD_PrintFormatItem_Trl";
    
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
    
    /** Set Print Format Item.
    @param AD_PrintFormatItem_ID Item/Column in the Print format */
    public void setAD_PrintFormatItem_ID (int AD_PrintFormatItem_ID)
    {
        if (AD_PrintFormatItem_ID < 1) throw new IllegalArgumentException ("AD_PrintFormatItem_ID is mandatory.");
        set_ValueNoCheck ("AD_PrintFormatItem_ID", Integer.valueOf(AD_PrintFormatItem_ID));
        
    }
    
    /** Get Print Format Item.
    @return Item/Column in the Print format */
    public int getAD_PrintFormatItem_ID() 
    {
        return get_ValueAsInt("AD_PrintFormatItem_ID");
        
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
    
    /** Set Print Text.
    @param PrintName The label text to be printed on a document or correspondence. */
    public void setPrintName (String PrintName)
    {
        set_Value ("PrintName", PrintName);
        
    }
    
    /** Get Print Text.
    @return The label text to be printed on a document or correspondence. */
    public String getPrintName() 
    {
        return (String)get_Value("PrintName");
        
    }
    
    /** Set Print Label Suffix.
    @param PrintNameSuffix The label text to be printed on a document or correspondence after the field */
    public void setPrintNameSuffix (String PrintNameSuffix)
    {
        set_Value ("PrintNameSuffix", PrintNameSuffix);
        
    }
    
    /** Get Print Label Suffix.
    @return The label text to be printed on a document or correspondence after the field */
    public String getPrintNameSuffix() 
    {
        return (String)get_Value("PrintNameSuffix");
        
    }
    
    
}
