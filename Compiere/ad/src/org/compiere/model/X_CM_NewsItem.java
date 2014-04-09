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
/** Generated Model for CM_NewsItem
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_NewsItem.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_NewsItem extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_NewsItem_ID id
    @param trx transaction
    */
    public X_CM_NewsItem (Ctx ctx, int CM_NewsItem_ID, Trx trx)
    {
        super (ctx, CM_NewsItem_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_NewsItem_ID == 0)
        {
            setCM_NewsChannel_ID (0);
            setCM_NewsItem_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_NewsItem (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=871 */
    public static final int Table_ID=871;
    
    /** TableName=CM_NewsItem */
    public static final String Table_Name="CM_NewsItem";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Author.
    @param Author Author/Creator of the Entity */
    public void setAuthor (String Author)
    {
        set_Value ("Author", Author);
        
    }
    
    /** Get Author.
    @return Author/Creator of the Entity */
    public String getAuthor() 
    {
        return (String)get_Value("Author");
        
    }
    
    /** Set News Channel.
    @param CM_NewsChannel_ID News channel for rss feed */
    public void setCM_NewsChannel_ID (int CM_NewsChannel_ID)
    {
        if (CM_NewsChannel_ID < 1) throw new IllegalArgumentException ("CM_NewsChannel_ID is mandatory.");
        set_ValueNoCheck ("CM_NewsChannel_ID", Integer.valueOf(CM_NewsChannel_ID));
        
    }
    
    /** Get News Channel.
    @return News channel for rss feed */
    public int getCM_NewsChannel_ID() 
    {
        return get_ValueAsInt("CM_NewsChannel_ID");
        
    }
    
    /** Set News Item / Article.
    @param CM_NewsItem_ID News item or article defines base content */
    public void setCM_NewsItem_ID (int CM_NewsItem_ID)
    {
        if (CM_NewsItem_ID < 1) throw new IllegalArgumentException ("CM_NewsItem_ID is mandatory.");
        set_ValueNoCheck ("CM_NewsItem_ID", Integer.valueOf(CM_NewsItem_ID));
        
    }
    
    /** Get News Item / Article.
    @return News item or article defines base content */
    public int getCM_NewsItem_ID() 
    {
        return get_ValueAsInt("CM_NewsItem_ID");
        
    }
    
    /** Set Content HTML.
    @param ContentHTML Contains the content itself */
    public void setContentHTML (String ContentHTML)
    {
        set_Value ("ContentHTML", ContentHTML);
        
    }
    
    /** Get Content HTML.
    @return Contains the content itself */
    public String getContentHTML() 
    {
        return (String)get_Value("ContentHTML");
        
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
    
    /** Set LinkURL.
    @param LinkURL Contains URL to a target */
    public void setLinkURL (String LinkURL)
    {
        set_Value ("LinkURL", LinkURL);
        
    }
    
    /** Get LinkURL.
    @return Contains URL to a target */
    public String getLinkURL() 
    {
        return (String)get_Value("LinkURL");
        
    }
    
    /** Set Publication Date.
    @param PubDate Date on which this article will / should get published */
    public void setPubDate (Timestamp PubDate)
    {
        set_Value ("PubDate", PubDate);
        
    }
    
    /** Get Publication Date.
    @return Date on which this article will / should get published */
    public Timestamp getPubDate() 
    {
        return (Timestamp)get_Value("PubDate");
        
    }
    
    /** Set Title.
    @param Title Title of the Contact */
    public void setTitle (String Title)
    {
        set_Value ("Title", Title);
        
    }
    
    /** Get Title.
    @return Title of the Contact */
    public String getTitle() 
    {
        return (String)get_Value("Title");
        
    }
    
    
}
