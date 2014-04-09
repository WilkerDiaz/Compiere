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
/** Generated Model for K_Comment
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_K_Comment.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_K_Comment extends PO
{
    /** Standard Constructor
    @param ctx context
    @param K_Comment_ID id
    @param trx transaction
    */
    public X_K_Comment (Ctx ctx, int K_Comment_ID, Trx trx)
    {
        super (ctx, K_Comment_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (K_Comment_ID == 0)
        {
            setIsPublic (true);	// Y
            setK_Comment_ID (0);
            setK_Entry_ID (0);
            setRating (0);
            setTextMsg (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_K_Comment (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=613 */
    public static final int Table_ID=613;
    
    /** TableName=K_Comment */
    public static final String Table_Name="K_Comment";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Session.
    @param AD_Session_ID User Session Online or Web */
    public void setAD_Session_ID (int AD_Session_ID)
    {
        if (AD_Session_ID <= 0) set_ValueNoCheck ("AD_Session_ID", null);
        else
        set_ValueNoCheck ("AD_Session_ID", Integer.valueOf(AD_Session_ID));
        
    }
    
    /** Get Session.
    @return User Session Online or Web */
    public int getAD_Session_ID() 
    {
        return get_ValueAsInt("AD_Session_ID");
        
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
    
    /** Set Entry Comment.
    @param K_Comment_ID Knowledge Entry Comment */
    public void setK_Comment_ID (int K_Comment_ID)
    {
        if (K_Comment_ID < 1) throw new IllegalArgumentException ("K_Comment_ID is mandatory.");
        set_ValueNoCheck ("K_Comment_ID", Integer.valueOf(K_Comment_ID));
        
    }
    
    /** Get Entry Comment.
    @return Knowledge Entry Comment */
    public int getK_Comment_ID() 
    {
        return get_ValueAsInt("K_Comment_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getK_Comment_ID()));
        
    }
    
    /** Set Entry.
    @param K_Entry_ID Knowledge Entry */
    public void setK_Entry_ID (int K_Entry_ID)
    {
        if (K_Entry_ID < 1) throw new IllegalArgumentException ("K_Entry_ID is mandatory.");
        set_ValueNoCheck ("K_Entry_ID", Integer.valueOf(K_Entry_ID));
        
    }
    
    /** Get Entry.
    @return Knowledge Entry */
    public int getK_Entry_ID() 
    {
        return get_ValueAsInt("K_Entry_ID");
        
    }
    
    /** Set Rating.
    @param Rating Classification or Importance */
    public void setRating (int Rating)
    {
        set_Value ("Rating", Integer.valueOf(Rating));
        
    }
    
    /** Get Rating.
    @return Classification or Importance */
    public int getRating() 
    {
        return get_ValueAsInt("Rating");
        
    }
    
    /** Set Text Message.
    @param TextMsg Text Message */
    public void setTextMsg (String TextMsg)
    {
        if (TextMsg == null) throw new IllegalArgumentException ("TextMsg is mandatory.");
        set_Value ("TextMsg", TextMsg);
        
    }
    
    /** Get Text Message.
    @return Text Message */
    public String getTextMsg() 
    {
        return (String)get_Value("TextMsg");
        
    }
    
    
}
