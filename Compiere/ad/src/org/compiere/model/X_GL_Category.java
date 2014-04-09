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
/** Generated Model for GL_Category
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_GL_Category.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_GL_Category extends PO
{
    /** Standard Constructor
    @param ctx context
    @param GL_Category_ID id
    @param trx transaction
    */
    public X_GL_Category (Ctx ctx, int GL_Category_ID, Trx trx)
    {
        super (ctx, GL_Category_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (GL_Category_ID == 0)
        {
            setCategoryType (null);	// M
            setGL_Category_ID (0);
            setIsDefault (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_GL_Category (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=218 */
    public static final int Table_ID=218;
    
    /** TableName=GL_Category */
    public static final String Table_Name="GL_Category";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Document = D */
    public static final String CATEGORYTYPE_Document = X_Ref_GL_Category_Type.DOCUMENT.getValue();
    /** Import = I */
    public static final String CATEGORYTYPE_Import = X_Ref_GL_Category_Type.IMPORT.getValue();
    /** Manual = M */
    public static final String CATEGORYTYPE_Manual = X_Ref_GL_Category_Type.MANUAL.getValue();
    /** System generated = S */
    public static final String CATEGORYTYPE_SystemGenerated = X_Ref_GL_Category_Type.SYSTEM_GENERATED.getValue();
    /** Set Category Type.
    @param CategoryType Source of the Journal with this category */
    public void setCategoryType (String CategoryType)
    {
        if (CategoryType == null) throw new IllegalArgumentException ("CategoryType is mandatory");
        if (!X_Ref_GL_Category_Type.isValid(CategoryType))
        throw new IllegalArgumentException ("CategoryType Invalid value - " + CategoryType + " - Reference_ID=207 - D - I - M - S");
        set_Value ("CategoryType", CategoryType);
        
    }
    
    /** Get Category Type.
    @return Source of the Journal with this category */
    public String getCategoryType() 
    {
        return (String)get_Value("CategoryType");
        
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
    
    /** Set GL Category.
    @param GL_Category_ID General Ledger Category */
    public void setGL_Category_ID (int GL_Category_ID)
    {
        if (GL_Category_ID < 1) throw new IllegalArgumentException ("GL_Category_ID is mandatory.");
        set_ValueNoCheck ("GL_Category_ID", Integer.valueOf(GL_Category_ID));
        
    }
    
    /** Get GL Category.
    @return General Ledger Category */
    public int getGL_Category_ID() 
    {
        return get_ValueAsInt("GL_Category_ID");
        
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
    
    
}
