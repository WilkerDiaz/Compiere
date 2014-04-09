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
/** Generated Model for W_Store_Trl
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_W_Store_Trl.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_W_Store_Trl extends PO
{
    /** Standard Constructor
    @param ctx context
    @param W_Store_Trl_ID id
    @param trx transaction
    */
    public X_W_Store_Trl (Ctx ctx, int W_Store_Trl_ID, Trx trx)
    {
        super (ctx, W_Store_Trl_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (W_Store_Trl_ID == 0)
        {
            setAD_Language (null);
            setIsTranslated (false);
            setW_Store_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_W_Store_Trl (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27523942970789L;
    /** Last Updated Timestamp 2009-05-08 14:20:54.0 */
    public static final long updatedMS = 1241817654000L;
    /** AD_Table_ID=779 */
    public static final int Table_ID=779;
    
    /** TableName=W_Store_Trl */
    public static final String Table_Name="W_Store_Trl";
    
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
    
    /** Set EMail Footer.
    @param EMailFooter Footer added to EMails */
    public void setEMailFooter (String EMailFooter)
    {
        set_Value ("EMailFooter", EMailFooter);
        
    }
    
    /** Get EMail Footer.
    @return Footer added to EMails */
    public String getEMailFooter() 
    {
        return (String)get_Value("EMailFooter");
        
    }
    
    /** Set EMail Header.
    @param EMailHeader Header added to EMails */
    public void setEMailHeader (String EMailHeader)
    {
        set_Value ("EMailHeader", EMailHeader);
        
    }
    
    /** Get EMail Header.
    @return Header added to EMails */
    public String getEMailHeader() 
    {
        return (String)get_Value("EMailHeader");
        
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
    
    /** Set Web Store.
    @param W_Store_ID A Web Store of the Client */
    public void setW_Store_ID (int W_Store_ID)
    {
        if (W_Store_ID < 1) throw new IllegalArgumentException ("W_Store_ID is mandatory.");
        set_ValueNoCheck ("W_Store_ID", Integer.valueOf(W_Store_ID));
        
    }
    
    /** Get Web Store.
    @return A Web Store of the Client */
    public int getW_Store_ID() 
    {
        return get_ValueAsInt("W_Store_ID");
        
    }
    
    /** Set Web Store Info.
    @param WebInfo Web Store Header Information */
    public void setWebInfo (String WebInfo)
    {
        set_Value ("WebInfo", WebInfo);
        
    }
    
    /** Get Web Store Info.
    @return Web Store Header Information */
    public String getWebInfo() 
    {
        return (String)get_Value("WebInfo");
        
    }
    
    /** Set Web Parameter 1.
    @param WebParam1 Web Site Parameter 1 (default: header image) */
    public void setWebParam1 (String WebParam1)
    {
        set_Value ("WebParam1", WebParam1);
        
    }
    
    /** Get Web Parameter 1.
    @return Web Site Parameter 1 (default: header image) */
    public String getWebParam1() 
    {
        return (String)get_Value("WebParam1");
        
    }
    
    /** Set Web Parameter 2.
    @param WebParam2 Web Site Parameter 2 (default index page) */
    public void setWebParam2 (String WebParam2)
    {
        set_Value ("WebParam2", WebParam2);
        
    }
    
    /** Get Web Parameter 2.
    @return Web Site Parameter 2 (default index page) */
    public String getWebParam2() 
    {
        return (String)get_Value("WebParam2");
        
    }
    
    /** Set Web Parameter 3.
    @param WebParam3 Web Site Parameter 3 (default left - menu) */
    public void setWebParam3 (String WebParam3)
    {
        set_Value ("WebParam3", WebParam3);
        
    }
    
    /** Get Web Parameter 3.
    @return Web Site Parameter 3 (default left - menu) */
    public String getWebParam3() 
    {
        return (String)get_Value("WebParam3");
        
    }
    
    /** Set Web Parameter 4.
    @param WebParam4 Web Site Parameter 4 (default footer left) */
    public void setWebParam4 (String WebParam4)
    {
        set_Value ("WebParam4", WebParam4);
        
    }
    
    /** Get Web Parameter 4.
    @return Web Site Parameter 4 (default footer left) */
    public String getWebParam4() 
    {
        return (String)get_Value("WebParam4");
        
    }
    
    /** Set Web Parameter 5.
    @param WebParam5 Web Site Parameter 5 (default footer center) */
    public void setWebParam5 (String WebParam5)
    {
        set_Value ("WebParam5", WebParam5);
        
    }
    
    /** Get Web Parameter 5.
    @return Web Site Parameter 5 (default footer center) */
    public String getWebParam5() 
    {
        return (String)get_Value("WebParam5");
        
    }
    
    /** Set Web Parameter 6.
    @param WebParam6 Web Site Parameter 6 (default footer right) */
    public void setWebParam6 (String WebParam6)
    {
        set_Value ("WebParam6", WebParam6);
        
    }
    
    /** Get Web Parameter 6.
    @return Web Site Parameter 6 (default footer right) */
    public String getWebParam6() 
    {
        return (String)get_Value("WebParam6");
        
    }
    
    
}
