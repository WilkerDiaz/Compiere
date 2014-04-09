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
/** Generated Model for CM_CStage
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_CStage.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_CStage extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_CStage_ID id
    @param trx transaction
    */
    public X_CM_CStage (Ctx ctx, int CM_CStage_ID, Trx trx)
    {
        super (ctx, CM_CStage_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_CStage_ID == 0)
        {
            setCM_CStage_ID (0);
            setCM_WebProject_ID (0);
            setIsIndexed (true);	// Y
            setIsModified (false);
            setIsSecure (false);
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
    public X_CM_CStage (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27509346302789L;
    /** Last Updated Timestamp 2008-11-20 14:43:06.0 */
    public static final long updatedMS = 1227220986000L;
    /** AD_Table_ID=866 */
    public static final int Table_ID=866;
    
    /** TableName=CM_CStage */
    public static final String Table_Name="CM_CStage";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Container Link.
    @param CM_CStageLink_ID Stage Link to another Container in the Web Project */
    public void setCM_CStageLink_ID (int CM_CStageLink_ID)
    {
        if (CM_CStageLink_ID <= 0) set_Value ("CM_CStageLink_ID", null);
        else
        set_Value ("CM_CStageLink_ID", Integer.valueOf(CM_CStageLink_ID));
        
    }
    
    /** Get Container Link.
    @return Stage Link to another Container in the Web Project */
    public int getCM_CStageLink_ID() 
    {
        return get_ValueAsInt("CM_CStageLink_ID");
        
    }
    
    /** Set Web Container Stage.
    @param CM_CStage_ID Web Container Stage contains the staging content like images, text etc. */
    public void setCM_CStage_ID (int CM_CStage_ID)
    {
        if (CM_CStage_ID < 1) throw new IllegalArgumentException ("CM_CStage_ID is mandatory.");
        set_ValueNoCheck ("CM_CStage_ID", Integer.valueOf(CM_CStage_ID));
        
    }
    
    /** Get Web Container Stage.
    @return Web Container Stage contains the staging content like images, text etc. */
    public int getCM_CStage_ID() 
    {
        return get_ValueAsInt("CM_CStage_ID");
        
    }
    
    /** Set Template.
    @param CM_Template_ID Template defines how content is displayed */
    public void setCM_Template_ID (int CM_Template_ID)
    {
        if (CM_Template_ID <= 0) set_Value ("CM_Template_ID", null);
        else
        set_Value ("CM_Template_ID", Integer.valueOf(CM_Template_ID));
        
    }
    
    /** Get Template.
    @return Template defines how content is displayed */
    public int getCM_Template_ID() 
    {
        return get_ValueAsInt("CM_Template_ID");
        
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
    
    /** Set External Link (URL).
    @param ContainerLinkURL External Link (URL) for the Container */
    public void setContainerLinkURL (String ContainerLinkURL)
    {
        set_Value ("ContainerLinkURL", ContainerLinkURL);
        
    }
    
    /** Get External Link (URL).
    @return External Link (URL) for the Container */
    public String getContainerLinkURL() 
    {
        return (String)get_Value("ContainerLinkURL");
        
    }
    
    /** Document = D */
    public static final String CONTAINERTYPE_Document = X_Ref_CM_Container_Type.DOCUMENT.getValue();
    /** Internal Link = L */
    public static final String CONTAINERTYPE_InternalLink = X_Ref_CM_Container_Type.INTERNAL_LINK.getValue();
    /** External URL = U */
    public static final String CONTAINERTYPE_ExternalURL = X_Ref_CM_Container_Type.EXTERNAL_URL.getValue();
    /** Set Web Container Type.
    @param ContainerType Web Container Type */
    public void setContainerType (String ContainerType)
    {
        if (!X_Ref_CM_Container_Type.isValid(ContainerType))
        throw new IllegalArgumentException ("ContainerType Invalid value - " + ContainerType + " - Reference_ID=385 - D - L - U");
        set_Value ("ContainerType", ContainerType);
        
    }
    
    /** Get Web Container Type.
    @return Web Container Type */
    public String getContainerType() 
    {
        return (String)get_Value("ContainerType");
        
    }
    
    /** Set ContainerXML.
    @param ContainerXML Autogenerated Container definition as XML Code */
    public void setContainerXML (String ContainerXML)
    {
        set_ValueNoCheck ("ContainerXML", ContainerXML);
        
    }
    
    /** Get ContainerXML.
    @return Autogenerated Container definition as XML Code */
    public String getContainerXML() 
    {
        return (String)get_Value("ContainerXML");
        
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
    
    /** Set Indexed.
    @param IsIndexed Index the document for the internal search engine */
    public void setIsIndexed (boolean IsIndexed)
    {
        set_Value ("IsIndexed", Boolean.valueOf(IsIndexed));
        
    }
    
    /** Get Indexed.
    @return Index the document for the internal search engine */
    public boolean isIndexed() 
    {
        return get_ValueAsBoolean("IsIndexed");
        
    }
    
    /** Set Modified.
    @param IsModified The record is modified */
    public void setIsModified (boolean IsModified)
    {
        set_ValueNoCheck ("IsModified", Boolean.valueOf(IsModified));
        
    }
    
    /** Get Modified.
    @return The record is modified */
    public boolean isModified() 
    {
        return get_ValueAsBoolean("IsModified");
        
    }
    
    /** Set Secure content.
    @param IsSecure Defines whether content needs to get encrypted */
    public void setIsSecure (boolean IsSecure)
    {
        set_Value ("IsSecure", Boolean.valueOf(IsSecure));
        
    }
    
    /** Get Secure content.
    @return Defines whether content needs to get encrypted */
    public boolean isSecure() 
    {
        return get_ValueAsBoolean("IsSecure");
        
    }
    
    /** Set Summary Level.
    @param IsSummary This is a summary entity */
    public void setIsSummary (boolean IsSummary)
    {
        set_ValueNoCheck ("IsSummary", Boolean.valueOf(IsSummary));
        
    }
    
    /** Get Summary Level.
    @return This is a summary entity */
    public boolean isSummary() 
    {
        return get_ValueAsBoolean("IsSummary");
        
    }
    
    /** Set Valid.
    @param IsValid Element is valid */
    public void setIsValid (boolean IsValid)
    {
        set_Value ("IsValid", Boolean.valueOf(IsValid));
        
    }
    
    /** Get Valid.
    @return Element is valid */
    public boolean isValid() 
    {
        return get_ValueAsBoolean("IsValid");
        
    }
    
    /** Set Meta Author.
    @param Meta_Author Author of the content */
    public void setMeta_Author (String Meta_Author)
    {
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
        set_Value ("Meta_Copyright", Meta_Copyright);
        
    }
    
    /** Get Meta Copyright.
    @return Contains Copyright information for the content */
    public String getMeta_Copyright() 
    {
        return (String)get_Value("Meta_Copyright");
        
    }
    
    /** Set Meta Description.
    @param Meta_Description Meta info describing the contents of the page */
    public void setMeta_Description (String Meta_Description)
    {
        set_Value ("Meta_Description", Meta_Description);
        
    }
    
    /** Get Meta Description.
    @return Meta info describing the contents of the page */
    public String getMeta_Description() 
    {
        return (String)get_Value("Meta_Description");
        
    }
    
    /** Set Meta Keywords.
    @param Meta_Keywords Contains the keywords for the content */
    public void setMeta_Keywords (String Meta_Keywords)
    {
        set_Value ("Meta_Keywords", Meta_Keywords);
        
    }
    
    /** Get Meta Keywords.
    @return Contains the keywords for the content */
    public String getMeta_Keywords() 
    {
        return (String)get_Value("Meta_Keywords");
        
    }
    
    /** Set Meta Language.
    @param Meta_Language Language HTML Meta Tag */
    public void setMeta_Language (String Meta_Language)
    {
        set_Value ("Meta_Language", Meta_Language);
        
    }
    
    /** Get Meta Language.
    @return Language HTML Meta Tag */
    public String getMeta_Language() 
    {
        return (String)get_Value("Meta_Language");
        
    }
    
    /** Set Meta Publisher.
    @param Meta_Publisher Meta Publisher defines the publisher of the content */
    public void setMeta_Publisher (String Meta_Publisher)
    {
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
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Relative URL.
    @param RelativeURL Contains the relative URL for the container */
    public void setRelativeURL (String RelativeURL)
    {
        set_Value ("RelativeURL", RelativeURL);
        
    }
    
    /** Get Relative URL.
    @return Contains the relative URL for the container */
    public String getRelativeURL() 
    {
        return (String)get_Value("RelativeURL");
        
    }
    
    /** Set StructureXML.
    @param StructureXML Autogenerated Container definition as XML Code */
    public void setStructureXML (String StructureXML)
    {
        set_Value ("StructureXML", StructureXML);
        
    }
    
    /** Get StructureXML.
    @return Autogenerated Container definition as XML Code */
    public String getStructureXML() 
    {
        return (String)get_Value("StructureXML");
        
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
