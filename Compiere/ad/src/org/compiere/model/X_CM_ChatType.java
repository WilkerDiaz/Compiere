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
/** Generated Model for CM_ChatType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_ChatType.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_ChatType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_ChatType_ID id
    @param trx transaction
    */
    public X_CM_ChatType (Ctx ctx, int CM_ChatType_ID, Trx trx)
    {
        super (ctx, CM_ChatType_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_ChatType_ID == 0)
        {
            setAD_Table_ID (0);
            setCM_ChatType_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_ChatType (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=874 */
    public static final int Table_ID=874;
    
    /** TableName=CM_ChatType */
    public static final String Table_Name="CM_ChatType";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set Chat Type.
    @param CM_ChatType_ID Type of discussion / chat */
    public void setCM_ChatType_ID (int CM_ChatType_ID)
    {
        if (CM_ChatType_ID < 1) throw new IllegalArgumentException ("CM_ChatType_ID is mandatory.");
        set_ValueNoCheck ("CM_ChatType_ID", Integer.valueOf(CM_ChatType_ID));
        
    }
    
    /** Get Chat Type.
    @return Type of discussion / chat */
    public int getCM_ChatType_ID() 
    {
        return get_ValueAsInt("CM_ChatType_ID");
        
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
    
    /** After Publishing = A */
    public static final String MODERATIONTYPE_AfterPublishing = X_Ref_CM_Chat_ModerationType.AFTER_PUBLISHING.getValue();
    /** Before Publishing = B */
    public static final String MODERATIONTYPE_BeforePublishing = X_Ref_CM_Chat_ModerationType.BEFORE_PUBLISHING.getValue();
    /** Not moderated = N */
    public static final String MODERATIONTYPE_NotModerated = X_Ref_CM_Chat_ModerationType.NOT_MODERATED.getValue();
    /** Set Moderation Type.
    @param ModerationType Type of moderation */
    public void setModerationType (String ModerationType)
    {
        if (!X_Ref_CM_Chat_ModerationType.isValid(ModerationType))
        throw new IllegalArgumentException ("ModerationType Invalid value - " + ModerationType + " - Reference_ID=395 - A - B - N");
        set_Value ("ModerationType", ModerationType);
        
    }
    
    /** Get Moderation Type.
    @return Type of moderation */
    public String getModerationType() 
    {
        return (String)get_Value("ModerationType");
        
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
