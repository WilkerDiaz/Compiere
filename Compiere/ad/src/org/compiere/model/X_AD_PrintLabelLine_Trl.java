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
/** Generated Model for AD_PrintLabelLine_Trl
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_PrintLabelLine_Trl.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_PrintLabelLine_Trl extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_PrintLabelLine_Trl_ID id
    @param trx transaction
    */
    public X_AD_PrintLabelLine_Trl (Ctx ctx, int AD_PrintLabelLine_Trl_ID, Trx trx)
    {
        super (ctx, AD_PrintLabelLine_Trl_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_PrintLabelLine_Trl_ID == 0)
        {
            setAD_Language (null);
            setAD_PrintLabelLine_ID (0);
            setIsTranslated (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_PrintLabelLine_Trl (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27523942662789L;
    /** Last Updated Timestamp 2009-05-08 14:15:46.0 */
    public static final long updatedMS = 1241817346000L;
    /** AD_Table_ID=568 */
    public static final int Table_ID=568;
    
    /** TableName=AD_PrintLabelLine_Trl */
    public static final String Table_Name="AD_PrintLabelLine_Trl";
    
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
    
    /** Set Print Label Line.
    @param AD_PrintLabelLine_ID Print Label Line Format */
    public void setAD_PrintLabelLine_ID (int AD_PrintLabelLine_ID)
    {
        if (AD_PrintLabelLine_ID < 1) throw new IllegalArgumentException ("AD_PrintLabelLine_ID is mandatory.");
        set_ValueNoCheck ("AD_PrintLabelLine_ID", Integer.valueOf(AD_PrintLabelLine_ID));
        
    }
    
    /** Get Print Label Line.
    @return Print Label Line Format */
    public int getAD_PrintLabelLine_ID() 
    {
        return get_ValueAsInt("AD_PrintLabelLine_ID");
        
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
    
    
}
