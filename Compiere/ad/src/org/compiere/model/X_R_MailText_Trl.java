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
/** Generated Model for R_MailText_Trl
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_R_MailText_Trl.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_R_MailText_Trl extends PO
{
    /** Standard Constructor
    @param ctx context
    @param R_MailText_Trl_ID id
    @param trx transaction
    */
    public X_R_MailText_Trl (Ctx ctx, int R_MailText_Trl_ID, Trx trx)
    {
        super (ctx, R_MailText_Trl_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (R_MailText_Trl_ID == 0)
        {
            setAD_Language (null);
            setIsTranslated (false);
            setMailHeader (null);
            setMailText (null);
            setName (null);
            setR_MailText_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_R_MailText_Trl (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27523942947789L;
    /** Last Updated Timestamp 2009-05-08 14:20:31.0 */
    public static final long updatedMS = 1241817631000L;
    /** AD_Table_ID=826 */
    public static final int Table_ID=826;
    
    /** TableName=R_MailText_Trl */
    public static final String Table_Name="R_MailText_Trl";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Language.
    @param AD_Language Language for this entity */
    public void setAD_Language (String AD_Language)
    {
        set_ValueNoCheck ("AD_Language", AD_Language);
        
    }
    
    /** Get Language.
    @return Language for this entity */
    public String getAD_Language() 
    {
        return (String)get_Value("AD_Language");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Language()));
        
    }
    
    /** Set Translated.
    @param IsTranslated This column is translated */
    public void setIsTranslated (boolean IsTranslated)
    {
        set_Value ("IsTranslated", Boolean.valueOf(IsTranslated));
        
    }
    
    /** Get Translated.
    @return This column is translated */
    public boolean isTranslated() 
    {
        return get_ValueAsBoolean("IsTranslated");
        
    }
    
    /** Set Subject.
    @param MailHeader Mail Header (Subject) */
    public void setMailHeader (String MailHeader)
    {
        if (MailHeader == null) throw new IllegalArgumentException ("MailHeader is mandatory.");
        set_Value ("MailHeader", MailHeader);
        
    }
    
    /** Get Subject.
    @return Mail Header (Subject) */
    public String getMailHeader() 
    {
        return (String)get_Value("MailHeader");
        
    }
    
    /** Set Mail Text.
    @param MailText Text used for Mail message */
    public void setMailText (String MailText)
    {
        if (MailText == null) throw new IllegalArgumentException ("MailText is mandatory.");
        set_Value ("MailText", MailText);
        
    }
    
    /** Get Mail Text.
    @return Text used for Mail message */
    public String getMailText() 
    {
        return (String)get_Value("MailText");
        
    }
    
    /** Set Mail Text 2.
    @param MailText2 Optional second text part used for Mail message */
    public void setMailText2 (String MailText2)
    {
        set_Value ("MailText2", MailText2);
        
    }
    
    /** Get Mail Text 2.
    @return Optional second text part used for Mail message */
    public String getMailText2() 
    {
        return (String)get_Value("MailText2");
        
    }
    
    /** Set Mail Text 3.
    @param MailText3 Optional third text part used for Mail message */
    public void setMailText3 (String MailText3)
    {
        set_Value ("MailText3", MailText3);
        
    }
    
    /** Get Mail Text 3.
    @return Optional third text part used for Mail message */
    public String getMailText3() 
    {
        return (String)get_Value("MailText3");
        
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
    
    /** Set Mail Template.
    @param R_MailText_ID Text templates for mailings */
    public void setR_MailText_ID (int R_MailText_ID)
    {
        if (R_MailText_ID < 1) throw new IllegalArgumentException ("R_MailText_ID is mandatory.");
        set_ValueNoCheck ("R_MailText_ID", Integer.valueOf(R_MailText_ID));
        
    }
    
    /** Get Mail Template.
    @return Text templates for mailings */
    public int getR_MailText_ID() 
    {
        return get_ValueAsInt("R_MailText_ID");
        
    }
    
    
}
