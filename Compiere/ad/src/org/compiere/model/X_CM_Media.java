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
/** Generated Model for CM_Media
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_Media.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_Media extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_Media_ID id
    @param trx transaction
    */
    public X_CM_Media (Ctx ctx, int CM_Media_ID, Trx trx)
    {
        super (ctx, CM_Media_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_Media_ID == 0)
        {
            setCM_Media_ID (0);
            setCM_WebProject_ID (0);
            setIsSummary (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_Media (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=857 */
    public static final int Table_ID=857;
    
    /** TableName=CM_Media */
    public static final String Table_Name="CM_Media";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Media Item.
    @param CM_Media_ID Contains media content like images, flash movies etc. */
    public void setCM_Media_ID (int CM_Media_ID)
    {
        if (CM_Media_ID < 1) throw new IllegalArgumentException ("CM_Media_ID is mandatory.");
        set_ValueNoCheck ("CM_Media_ID", Integer.valueOf(CM_Media_ID));
        
    }
    
    /** Get Media Item.
    @return Contains media content like images, flash movies etc. */
    public int getCM_Media_ID() 
    {
        return get_ValueAsInt("CM_Media_ID");
        
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
    
    /** Set Content.
    @param ContentText Content */
    public void setContentText (String ContentText)
    {
        set_Value ("ContentText", ContentText);
        
    }
    
    /** Get Content.
    @return Content */
    public String getContentText() 
    {
        return (String)get_Value("ContentText");
        
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
    
    /** Set Direct Deploy.
    @param DirectDeploy Direct Deploy */
    public void setDirectDeploy (String DirectDeploy)
    {
        set_Value ("DirectDeploy", DirectDeploy);
        
    }
    
    /** Get Direct Deploy.
    @return Direct Deploy */
    public String getDirectDeploy() 
    {
        return (String)get_Value("DirectDeploy");
        
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
    
    /** Set Summary Level.
    @param IsSummary This is a summary entity */
    public void setIsSummary (boolean IsSummary)
    {
        set_Value ("IsSummary", Boolean.valueOf(IsSummary));
        
    }
    
    /** Get Summary Level.
    @return This is a summary entity */
    public boolean isSummary() 
    {
        return get_ValueAsBoolean("IsSummary");
        
    }
    
    /** text/css = CSS */
    public static final String MEDIATYPE_TextCss = X_Ref_CM_Media_Media_Type.TEXT_CSS.getValue();
    /** image/gif = GIF */
    public static final String MEDIATYPE_ImageGif = X_Ref_CM_Media_Media_Type.IMAGE_GIF.getValue();
    /** image/jpeg = JPG */
    public static final String MEDIATYPE_ImageJpeg = X_Ref_CM_Media_Media_Type.IMAGE_JPEG.getValue();
    /** text/js = JS */
    public static final String MEDIATYPE_TextJs = X_Ref_CM_Media_Media_Type.TEXT_JS.getValue();
    /** application/pdf = PDF */
    public static final String MEDIATYPE_ApplicationPdf = X_Ref_CM_Media_Media_Type.APPLICATION_PDF.getValue();
    /** image/png = PNG */
    public static final String MEDIATYPE_ImagePng = X_Ref_CM_Media_Media_Type.IMAGE_PNG.getValue();
    /** text/xml = XML */
    public static final String MEDIATYPE_TextXml = X_Ref_CM_Media_Media_Type.TEXT_XML.getValue();
    /** Set Media Type.
    @param MediaType Defines the media type for the browser */
    public void setMediaType (String MediaType)
    {
        if (!X_Ref_CM_Media_Media_Type.isValid(MediaType))
        throw new IllegalArgumentException ("MediaType Invalid value - " + MediaType + " - Reference_ID=388 - CSS - GIF - JPG - JS - PDF - PNG - XML");
        set_Value ("MediaType", MediaType);
        
    }
    
    /** Get Media Type.
    @return Defines the media type for the browser */
    public String getMediaType() 
    {
        return (String)get_Value("MediaType");
        
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
