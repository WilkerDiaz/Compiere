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
/** Generated Model for CM_WebProject
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_WebProject.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_WebProject extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_WebProject_ID id
    @param trx transaction
    */
    public X_CM_WebProject (Ctx ctx, int CM_WebProject_ID, Trx trx)
    {
        super (ctx, CM_WebProject_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_WebProject_ID == 0)
        {
            setAD_TreeCMC_ID (0);
            setAD_TreeCMM_ID (0);
            setAD_TreeCMS_ID (0);
            setAD_TreeCMT_ID (0);
            setCM_WebProject_ID (0);
            setMeta_Author (null);	// @AD_User_Name@
            setMeta_Content (null);	// text/html;
             charset=UTF-8
            setMeta_Copyright (null);	// @AD_Client_Name@
            setMeta_Publisher (null);	// @AD_Client_Name@
            setMeta_RobotsTag (null);	// 'INDEX,FOLLOW'
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_WebProject (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27506387251789L;
    /** Last Updated Timestamp 2008-10-17 09:45:35.0 */
    public static final long updatedMS = 1224261935000L;
    /** AD_Table_ID=853 */
    public static final int Table_ID=853;
    
    /** TableName=CM_WebProject */
    public static final String Table_Name="CM_WebProject";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Container Tree.
    @param AD_TreeCMC_ID Container Tree */
    public void setAD_TreeCMC_ID (int AD_TreeCMC_ID)
    {
        if (AD_TreeCMC_ID < 1) throw new IllegalArgumentException ("AD_TreeCMC_ID is mandatory.");
        set_ValueNoCheck ("AD_TreeCMC_ID", Integer.valueOf(AD_TreeCMC_ID));
        
    }
    
    /** Get Container Tree.
    @return Container Tree */
    public int getAD_TreeCMC_ID() 
    {
        return get_ValueAsInt("AD_TreeCMC_ID");
        
    }
    
    /** Set Media Tree.
    @param AD_TreeCMM_ID Media Tree */
    public void setAD_TreeCMM_ID (int AD_TreeCMM_ID)
    {
        if (AD_TreeCMM_ID < 1) throw new IllegalArgumentException ("AD_TreeCMM_ID is mandatory.");
        set_ValueNoCheck ("AD_TreeCMM_ID", Integer.valueOf(AD_TreeCMM_ID));
        
    }
    
    /** Get Media Tree.
    @return Media Tree */
    public int getAD_TreeCMM_ID() 
    {
        return get_ValueAsInt("AD_TreeCMM_ID");
        
    }
    
    /** Set Stage Tree.
    @param AD_TreeCMS_ID Stage Tree */
    public void setAD_TreeCMS_ID (int AD_TreeCMS_ID)
    {
        if (AD_TreeCMS_ID < 1) throw new IllegalArgumentException ("AD_TreeCMS_ID is mandatory.");
        set_ValueNoCheck ("AD_TreeCMS_ID", Integer.valueOf(AD_TreeCMS_ID));
        
    }
    
    /** Get Stage Tree.
    @return Stage Tree */
    public int getAD_TreeCMS_ID() 
    {
        return get_ValueAsInt("AD_TreeCMS_ID");
        
    }
    
    /** Set Template Tree.
    @param AD_TreeCMT_ID Template Tree */
    public void setAD_TreeCMT_ID (int AD_TreeCMT_ID)
    {
        if (AD_TreeCMT_ID < 1) throw new IllegalArgumentException ("AD_TreeCMT_ID is mandatory.");
        set_ValueNoCheck ("AD_TreeCMT_ID", Integer.valueOf(AD_TreeCMT_ID));
        
    }
    
    /** Get Template Tree.
    @return Template Tree */
    public int getAD_TreeCMT_ID() 
    {
        return get_ValueAsInt("AD_TreeCMT_ID");
        
    }
    
    /** Set Web Project.
    @param CM_WebProject_ID A web project is the main data container for Containers, URLs, Ads, and Media etc. */
    public void setCM_WebProject_ID (int CM_WebProject_ID)
    {
        if (CM_WebProject_ID < 1) throw new IllegalArgumentException ("CM_WebProject_ID is mandatory.");
        set_ValueNoCheck ("CM_WebProject_ID", Integer.valueOf(CM_WebProject_ID));
        
    }
    
    /** Get Web Project.
    @return A web project is the main data container for Containers, URLs, Ads, and Media etc. */
    public int getCM_WebProject_ID() 
    {
        return get_ValueAsInt("CM_WebProject_ID");
        
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
    
    /** Set Meta Author.
    @param Meta_Author Author of the content */
    public void setMeta_Author (String Meta_Author)
    {
        if (Meta_Author == null) throw new IllegalArgumentException ("Meta_Author is mandatory.");
        set_Value ("Meta_Author", Meta_Author);
        
    }
    
    /** Get Meta Author.
    @return Author of the content */
    public String getMeta_Author() 
    {
        return (String)get_Value("Meta_Author");
        
    }
    
    /** Set Meta Content Type.
    @param Meta_Content Defines the type of content i.e. "text/html;
     charset=UTF-8" */
    public void setMeta_Content (String Meta_Content)
    {
        if (Meta_Content == null) throw new IllegalArgumentException ("Meta_Content is mandatory.");
        set_Value ("Meta_Content", Meta_Content);
        
    }
    
    /** Get Meta Content Type.
    @return Defines the type of content i.e. "text/html;
     charset=UTF-8" */
    public String getMeta_Content() 
    {
        return (String)get_Value("Meta_Content");
        
    }
    
    /** Set Meta Copyright.
    @param Meta_Copyright Contains Copyright information for the content */
    public void setMeta_Copyright (String Meta_Copyright)
    {
        if (Meta_Copyright == null) throw new IllegalArgumentException ("Meta_Copyright is mandatory.");
        set_Value ("Meta_Copyright", Meta_Copyright);
        
    }
    
    /** Get Meta Copyright.
    @return Contains Copyright information for the content */
    public String getMeta_Copyright() 
    {
        return (String)get_Value("Meta_Copyright");
        
    }
    
    /** Set Meta Publisher.
    @param Meta_Publisher Meta Publisher defines the publisher of the content */
    public void setMeta_Publisher (String Meta_Publisher)
    {
        if (Meta_Publisher == null) throw new IllegalArgumentException ("Meta_Publisher is mandatory.");
        set_Value ("Meta_Publisher", Meta_Publisher);
        
    }
    
    /** Get Meta Publisher.
    @return Meta Publisher defines the publisher of the content */
    public String getMeta_Publisher() 
    {
        return (String)get_Value("Meta_Publisher");
        
    }
    
    /** Set Meta RobotsTag.
    @param Meta_RobotsTag RobotsTag defines how search robots should handle this content */
    public void setMeta_RobotsTag (String Meta_RobotsTag)
    {
        if (Meta_RobotsTag == null) throw new IllegalArgumentException ("Meta_RobotsTag is mandatory.");
        set_Value ("Meta_RobotsTag", Meta_RobotsTag);
        
    }
    
    /** Get Meta RobotsTag.
    @return RobotsTag defines how search robots should handle this content */
    public String getMeta_RobotsTag() 
    {
        return (String)get_Value("Meta_RobotsTag");
        
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
