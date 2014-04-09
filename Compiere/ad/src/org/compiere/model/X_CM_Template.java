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
/** Generated Model for CM_Template
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_Template.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_Template extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_Template_ID id
    @param trx transaction
    */
    public X_CM_Template (Ctx ctx, int CM_Template_ID, Trx trx)
    {
        super (ctx, CM_Template_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_Template_ID == 0)
        {
            setCM_Template_ID (0);
            setIsInclude (false);
            setIsNews (false);
            setIsSummary (false);
            setIsUseAd (false);
            setIsValid (false);
            setName (null);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_Template (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=854 */
    public static final int Table_ID=854;
    
    /** TableName=CM_Template */
    public static final String Table_Name="CM_Template";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Template.
    @param CM_Template_ID Template defines how content is displayed */
    public void setCM_Template_ID (int CM_Template_ID)
    {
        if (CM_Template_ID < 1) throw new IllegalArgumentException ("CM_Template_ID is mandatory.");
        set_ValueNoCheck ("CM_Template_ID", Integer.valueOf(CM_Template_ID));
        
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
        if (CM_WebProject_ID <= 0) set_ValueNoCheck ("CM_WebProject_ID", null);
        else
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
    
    /** Set Elements.
    @param Elements Contains list of elements separated by CR */
    public void setElements (String Elements)
    {
        set_Value ("Elements", Elements);
        
    }
    
    /** Get Elements.
    @return Contains list of elements separated by CR */
    public String getElements() 
    {
        return (String)get_Value("Elements");
        
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
    
    /** Set Included.
    @param IsInclude Defines whether this content / template is included into another one */
    public void setIsInclude (boolean IsInclude)
    {
        set_Value ("IsInclude", Boolean.valueOf(IsInclude));
        
    }
    
    /** Get Included.
    @return Defines whether this content / template is included into another one */
    public boolean isInclude() 
    {
        return get_ValueAsBoolean("IsInclude");
        
    }
    
    /** Set Uses News.
    @param IsNews Template or container uses news channels */
    public void setIsNews (boolean IsNews)
    {
        set_Value ("IsNews", Boolean.valueOf(IsNews));
        
    }
    
    /** Get Uses News.
    @return Template or container uses news channels */
    public boolean isNews() 
    {
        return get_ValueAsBoolean("IsNews");
        
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
    
    /** Set Use Ad.
    @param IsUseAd Whether or not this template uses Ad's */
    public void setIsUseAd (boolean IsUseAd)
    {
        set_Value ("IsUseAd", Boolean.valueOf(IsUseAd));
        
    }
    
    /** Get Use Ad.
    @return Whether or not this template uses Ad's */
    public boolean isUseAd() 
    {
        return get_ValueAsBoolean("IsUseAd");
        
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
    
    /** Set TemplateXST.
    @param TemplateXST Contains the template code itself */
    public void setTemplateXST (String TemplateXST)
    {
        set_Value ("TemplateXST", TemplateXST);
        
    }
    
    /** Get TemplateXST.
    @return Contains the template code itself */
    public String getTemplateXST() 
    {
        return (String)get_Value("TemplateXST");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    
}
