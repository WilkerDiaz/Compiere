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
/** Generated Model for A_Asset_Group
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_A_Asset_Group.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_A_Asset_Group extends PO
{
    /** Standard Constructor
    @param ctx context
    @param A_Asset_Group_ID id
    @param trx transaction
    */
    public X_A_Asset_Group (Ctx ctx, int A_Asset_Group_ID, Trx trx)
    {
        super (ctx, A_Asset_Group_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (A_Asset_Group_ID == 0)
        {
            setA_Asset_Group_ID (0);
            setIsCreateAsActive (true);	// Y
            setIsDepreciated (false);
            setIsOneAssetPerUOM (false);
            setIsOwned (false);
            setIsTrackIssues (false);	// N
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_A_Asset_Group (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=542 */
    public static final int Table_ID=542;
    
    /** TableName=A_Asset_Group */
    public static final String Table_Name="A_Asset_Group";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Asset Group.
    @param A_Asset_Group_ID Group of Assets */
    public void setA_Asset_Group_ID (int A_Asset_Group_ID)
    {
        if (A_Asset_Group_ID < 1) throw new IllegalArgumentException ("A_Asset_Group_ID is mandatory.");
        set_ValueNoCheck ("A_Asset_Group_ID", Integer.valueOf(A_Asset_Group_ID));
        
    }
    
    /** Get Asset Group.
    @return Group of Assets */
    public int getA_Asset_Group_ID() 
    {
        return get_ValueAsInt("A_Asset_Group_ID");
        
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
    
    /** Set Create As Active.
    @param IsCreateAsActive Create entity and activate it */
    public void setIsCreateAsActive (boolean IsCreateAsActive)
    {
        set_Value ("IsCreateAsActive", Boolean.valueOf(IsCreateAsActive));
        
    }
    
    /** Get Create As Active.
    @return Create entity and activate it */
    public boolean isCreateAsActive() 
    {
        return get_ValueAsBoolean("IsCreateAsActive");
        
    }
    
    /** Set Depreciate.
    @param IsDepreciated The asset will be depreciated */
    public void setIsDepreciated (boolean IsDepreciated)
    {
        set_Value ("IsDepreciated", Boolean.valueOf(IsDepreciated));
        
    }
    
    /** Get Depreciate.
    @return The asset will be depreciated */
    public boolean isDepreciated() 
    {
        return get_ValueAsBoolean("IsDepreciated");
        
    }
    
    /** Set One Asset per UOM.
    @param IsOneAssetPerUOM Create one asset per UOM */
    public void setIsOneAssetPerUOM (boolean IsOneAssetPerUOM)
    {
        set_Value ("IsOneAssetPerUOM", Boolean.valueOf(IsOneAssetPerUOM));
        
    }
    
    /** Get One Asset per UOM.
    @return Create one asset per UOM */
    public boolean isOneAssetPerUOM() 
    {
        return get_ValueAsBoolean("IsOneAssetPerUOM");
        
    }
    
    /** Set Owned.
    @param IsOwned The asset is owned by the organization */
    public void setIsOwned (boolean IsOwned)
    {
        set_Value ("IsOwned", Boolean.valueOf(IsOwned));
        
    }
    
    /** Get Owned.
    @return The asset is owned by the organization */
    public boolean isOwned() 
    {
        return get_ValueAsBoolean("IsOwned");
        
    }
    
    /** Set Track Issues.
    @param IsTrackIssues Enable tracking issues for this asset */
    public void setIsTrackIssues (boolean IsTrackIssues)
    {
        set_Value ("IsTrackIssues", Boolean.valueOf(IsTrackIssues));
        
    }
    
    /** Get Track Issues.
    @return Enable tracking issues for this asset */
    public boolean isTrackIssues() 
    {
        return get_ValueAsBoolean("IsTrackIssues");
        
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
    
    /** Enterprise = E */
    public static final String SUPPORTLEVEL_Enterprise = X_Ref_SupportLevel.ENTERPRISE.getValue();
    /** Standard = S */
    public static final String SUPPORTLEVEL_Standard = X_Ref_SupportLevel.STANDARD.getValue();
    /** Unsupported = U */
    public static final String SUPPORTLEVEL_Unsupported = X_Ref_SupportLevel.UNSUPPORTED.getValue();
    /** Self-Service = X */
    public static final String SUPPORTLEVEL_Self_Service = X_Ref_SupportLevel.SELF__SERVICE.getValue();
    /** Set Support Level.
    @param SupportLevel Subscribed Support level */
    public void setSupportLevel (String SupportLevel)
    {
        if (!X_Ref_SupportLevel.isValid(SupportLevel))
        throw new IllegalArgumentException ("SupportLevel Invalid value - " + SupportLevel + " - Reference_ID=412 - E - S - U - X");
        set_Value ("SupportLevel", SupportLevel);
        
    }
    
    /** Get Support Level.
    @return Subscribed Support level */
    public String getSupportLevel() 
    {
        return (String)get_Value("SupportLevel");
        
    }
    
    
}
