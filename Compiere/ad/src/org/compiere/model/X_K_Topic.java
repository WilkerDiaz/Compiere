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
/** Generated Model for K_Topic
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_K_Topic.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_K_Topic extends PO
{
    /** Standard Constructor
    @param ctx context
    @param K_Topic_ID id
    @param trx transaction
    */
    public X_K_Topic (Ctx ctx, int K_Topic_ID, Trx trx)
    {
        super (ctx, K_Topic_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (K_Topic_ID == 0)
        {
            setIsPublic (true);	// Y
            setIsPublicWrite (true);	// Y
            setK_Topic_ID (0);
            setK_Type_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_K_Topic (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=607 */
    public static final int Table_ID=607;
    
    /** TableName=K_Topic */
    public static final String Table_Name="K_Topic";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Public.
    @param IsPublic Public can read entry */
    public void setIsPublic (boolean IsPublic)
    {
        set_Value ("IsPublic", Boolean.valueOf(IsPublic));
        
    }
    
    /** Get Public.
    @return Public can read entry */
    public boolean isPublic() 
    {
        return get_ValueAsBoolean("IsPublic");
        
    }
    
    /** Set Public Write.
    @param IsPublicWrite Public can write entries */
    public void setIsPublicWrite (boolean IsPublicWrite)
    {
        set_Value ("IsPublicWrite", Boolean.valueOf(IsPublicWrite));
        
    }
    
    /** Get Public Write.
    @return Public can write entries */
    public boolean isPublicWrite() 
    {
        return get_ValueAsBoolean("IsPublicWrite");
        
    }
    
    /** Set Knowledge Topic.
    @param K_Topic_ID Knowledge Topic */
    public void setK_Topic_ID (int K_Topic_ID)
    {
        if (K_Topic_ID < 1) throw new IllegalArgumentException ("K_Topic_ID is mandatory.");
        set_ValueNoCheck ("K_Topic_ID", Integer.valueOf(K_Topic_ID));
        
    }
    
    /** Get Knowledge Topic.
    @return Knowledge Topic */
    public int getK_Topic_ID() 
    {
        return get_ValueAsInt("K_Topic_ID");
        
    }
    
    /** Set Knowledge Type.
    @param K_Type_ID Knowledge Type */
    public void setK_Type_ID (int K_Type_ID)
    {
        if (K_Type_ID < 1) throw new IllegalArgumentException ("K_Type_ID is mandatory.");
        set_ValueNoCheck ("K_Type_ID", Integer.valueOf(K_Type_ID));
        
    }
    
    /** Get Knowledge Type.
    @return Knowledge Type */
    public int getK_Type_ID() 
    {
        return get_ValueAsInt("K_Type_ID");
        
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
