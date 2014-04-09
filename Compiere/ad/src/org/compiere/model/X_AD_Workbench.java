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
/** Generated Model for AD_Workbench
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Workbench.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Workbench extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Workbench_ID id
    @param trx transaction
    */
    public X_AD_Workbench (Ctx ctx, int AD_Workbench_ID, Trx trx)
    {
        super (ctx, AD_Workbench_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Workbench_ID == 0)
        {
            setAD_Column_ID (0);
            setAD_Workbench_ID (0);
            setEntityType (null);	// U
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Workbench (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=468 */
    public static final int Table_ID=468;
    
    /** TableName=AD_Workbench */
    public static final String Table_Name="AD_Workbench";
    
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
    
    /** Set Column.
    @param AD_Column_ID Column in the table */
    public void setAD_Column_ID (int AD_Column_ID)
    {
        if (AD_Column_ID < 1) throw new IllegalArgumentException ("AD_Column_ID is mandatory.");
        set_Value ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
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
    
    /** Set Workbench.
    @param AD_Workbench_ID Collection of windows, reports */
    public void setAD_Workbench_ID (int AD_Workbench_ID)
    {
        if (AD_Workbench_ID < 1) throw new IllegalArgumentException ("AD_Workbench_ID is mandatory.");
        set_ValueNoCheck ("AD_Workbench_ID", Integer.valueOf(AD_Workbench_ID));
        
    }
    
    /** Get Workbench.
    @return Collection of windows, reports */
    public int getAD_Workbench_ID() 
    {
        return get_ValueAsInt("AD_Workbench_ID");
        
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
