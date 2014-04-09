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
/** Generated Model for AD_ComponentRegUpdate
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ComponentRegUpdate.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ComponentRegUpdate extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ComponentRegUpdate_ID id
    @param trx transaction
    */
    public X_AD_ComponentRegUpdate (Ctx ctx, int AD_ComponentRegUpdate_ID, Trx trx)
    {
        super (ctx, AD_ComponentRegUpdate_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ComponentRegUpdate_ID == 0)
        {
            setAD_ComponentRegUpdate_ID (0);
            setAD_ComponentReg_ID (0);
            setEMail (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ComponentRegUpdate (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=1002 */
    public static final int Table_ID=1002;
    
    /** TableName=AD_ComponentRegUpdate */
    public static final String Table_Name="AD_ComponentRegUpdate";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Component Registration Update.
    @param AD_ComponentRegUpdate_ID Component Registration Update */
    public void setAD_ComponentRegUpdate_ID (int AD_ComponentRegUpdate_ID)
    {
        if (AD_ComponentRegUpdate_ID < 1) throw new IllegalArgumentException ("AD_ComponentRegUpdate_ID is mandatory.");
        set_ValueNoCheck ("AD_ComponentRegUpdate_ID", Integer.valueOf(AD_ComponentRegUpdate_ID));
        
    }
    
    /** Get Component Registration Update.
    @return Component Registration Update */
    public int getAD_ComponentRegUpdate_ID() 
    {
        return get_ValueAsInt("AD_ComponentRegUpdate_ID");
        
    }
    
    /** Set Component Registration.
    @param AD_ComponentReg_ID Component Registration */
    public void setAD_ComponentReg_ID (int AD_ComponentReg_ID)
    {
        if (AD_ComponentReg_ID < 1) throw new IllegalArgumentException ("AD_ComponentReg_ID is mandatory.");
        set_ValueNoCheck ("AD_ComponentReg_ID", Integer.valueOf(AD_ComponentReg_ID));
        
    }
    
    /** Get Component Registration.
    @return Component Registration */
    public int getAD_ComponentReg_ID() 
    {
        return get_ValueAsInt("AD_ComponentReg_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_ComponentReg_ID()));
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Approval Comment.
    @param ApprovalComment Comment about approval */
    public void setApprovalComment (String ApprovalComment)
    {
        set_Value ("ApprovalComment", ApprovalComment);
        
    }
    
    /** Get Approval Comment.
    @return Comment about approval */
    public String getApprovalComment() 
    {
        return (String)get_Value("ApprovalComment");
        
    }
    
    /** Set BinaryData.
    @param BinaryData Binary Data */
    public void setBinaryData (byte[] BinaryData)
    {
        set_Value ("BinaryData", BinaryData);
        
    }
    
    /** Get BinaryData.
    @return Binary Data */
    public byte[] getBinaryData() 
    {
        return (byte[])get_Value("BinaryData");
        
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
    
    /** Set Documentation Text.
    @param DocumentationText Documentation */
    public void setDocumentationText (String DocumentationText)
    {
        set_Value ("DocumentationText", DocumentationText);
        
    }
    
    /** Get Documentation Text.
    @return Documentation */
    public String getDocumentationText() 
    {
        return (String)get_Value("DocumentationText");
        
    }
    
    /** Set EMail Address.
    @param EMail Electronic Mail Address */
    public void setEMail (String EMail)
    {
        if (EMail == null) throw new IllegalArgumentException ("EMail is mandatory.");
        set_Value ("EMail", EMail);
        
    }
    
    /** Get EMail Address.
    @return Electronic Mail Address */
    public String getEMail() 
    {
        return (String)get_Value("EMail");
        
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
    
    /** No = N */
    public static final String ISAPPROVED_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISAPPROVED_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Approved.
    @param IsApproved Indicates if this document requires approval */
    public void setIsApproved (String IsApproved)
    {
        if (!X_Ref__YesNo.isValid(IsApproved))
        throw new IllegalArgumentException ("IsApproved Invalid value - " + IsApproved + " - Reference_ID=319 - N - Y");
        set_Value ("IsApproved", IsApproved);
        
    }
    
    /** Get Approved.
    @return Indicates if this document requires approval */
    public String getIsApproved() 
    {
        return (String)get_Value("IsApproved");
        
    }
    
    /** No = N */
    public static final String ISPUBLISHED_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISPUBLISHED_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Published.
    @param IsPublished The entry is published and can be viewed */
    public void setIsPublished (String IsPublished)
    {
        if (!X_Ref__YesNo.isValid(IsPublished))
        throw new IllegalArgumentException ("IsPublished Invalid value - " + IsPublished + " - Reference_ID=319 - N - Y");
        set_Value ("IsPublished", IsPublished);
        
    }
    
    /** Get Published.
    @return The entry is published and can be viewed */
    public String getIsPublished() 
    {
        return (String)get_Value("IsPublished");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Standard Price.
    @param PriceStd Standard Price */
    public void setPriceStd (java.math.BigDecimal PriceStd)
    {
        set_Value ("PriceStd", PriceStd);
        
    }
    
    /** Get Standard Price.
    @return Standard Price */
    public java.math.BigDecimal getPriceStd() 
    {
        return get_ValueAsBigDecimal("PriceStd");
        
    }
    
    /** Set Referrer.
    @param Referrer Referring web address */
    public void setReferrer (String Referrer)
    {
        set_Value ("Referrer", Referrer);
        
    }
    
    /** Get Referrer.
    @return Referring web address */
    public String getReferrer() 
    {
        return (String)get_Value("Referrer");
        
    }
    
    /** Set Remote Addr.
    @param Remote_Addr Remote Address */
    public void setRemote_Addr (String Remote_Addr)
    {
        set_Value ("Remote_Addr", Remote_Addr);
        
    }
    
    /** Get Remote Addr.
    @return Remote Address */
    public String getRemote_Addr() 
    {
        return (String)get_Value("Remote_Addr");
        
    }
    
    /** Set Remote Host.
    @param Remote_Host Remote host Info */
    public void setRemote_Host (String Remote_Host)
    {
        set_Value ("Remote_Host", Remote_Host);
        
    }
    
    /** Get Remote Host.
    @return Remote host Info */
    public String getRemote_Host() 
    {
        return (String)get_Value("Remote_Host");
        
    }
    
    /** Set Reply.
    @param Reply Reply or Answer */
    public void setReply (String Reply)
    {
        set_Value ("Reply", Reply);
        
    }
    
    /** Get Reply.
    @return Reply or Answer */
    public String getReply() 
    {
        return (String)get_Value("Reply");
        
    }
    
    /** Set Prerequisite Version.
    @param RequireCompiereVersion Prerequisite Compiere Base Version */
    public void setRequireCompiereVersion (String RequireCompiereVersion)
    {
        set_Value ("RequireCompiereVersion", RequireCompiereVersion);
        
    }
    
    /** Get Prerequisite Version.
    @return Prerequisite Compiere Base Version */
    public String getRequireCompiereVersion() 
    {
        return (String)get_Value("RequireCompiereVersion");
        
    }
    
    /** Set Prerequisite Applications.
    @param RequireComponentVersion Prerequisite Applications with optional Version */
    public void setRequireComponentVersion (String RequireComponentVersion)
    {
        set_Value ("RequireComponentVersion", RequireComponentVersion);
        
    }
    
    /** Get Prerequisite Applications.
    @return Prerequisite Applications with optional Version */
    public String getRequireComponentVersion() 
    {
        return (String)get_Value("RequireComponentVersion");
        
    }
    
    /** Set Suggested Price.
    @param SuggestedPrice Suggested Price */
    public void setSuggestedPrice (java.math.BigDecimal SuggestedPrice)
    {
        set_Value ("SuggestedPrice", SuggestedPrice);
        
    }
    
    /** Get Suggested Price.
    @return Suggested Price */
    public java.math.BigDecimal getSuggestedPrice() 
    {
        return get_ValueAsBigDecimal("SuggestedPrice");
        
    }
    
    /** Set Version.
    @param Version Version of the table definition */
    public void setVersion (String Version)
    {
        set_Value ("Version", Version);
        
    }
    
    /** Get Version.
    @return Version of the table definition */
    public String getVersion() 
    {
        return (String)get_Value("Version");
        
    }
    
    
}
