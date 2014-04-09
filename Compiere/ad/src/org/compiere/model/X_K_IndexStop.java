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
/** Generated Model for K_IndexStop
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_K_IndexStop.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_K_IndexStop extends PO
{
    /** Standard Constructor
    @param ctx context
    @param K_IndexStop_ID id
    @param trx transaction
    */
    public X_K_IndexStop (Ctx ctx, int K_IndexStop_ID, Trx trx)
    {
        super (ctx, K_IndexStop_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (K_IndexStop_ID == 0)
        {
            setIsManual (true);	// Y
            setK_IndexStop_ID (0);
            setKeyword (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_K_IndexStop (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=901 */
    public static final int Table_ID=901;
    
    /** TableName=K_IndexStop */
    public static final String Table_Name="K_IndexStop";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Web Project.
    @param CM_WebProject_ID A web project is the main data container for Containers, URLs, Ads, and Media etc. */
    public void setCM_WebProject_ID (int CM_WebProject_ID)
    {
        if (CM_WebProject_ID <= 0) set_Value ("CM_WebProject_ID", null);
        else
        set_Value ("CM_WebProject_ID", Integer.valueOf(CM_WebProject_ID));
        
    }
    
    /** Get Web Project.
    @return A web project is the main data container for Containers, URLs, Ads, and Media etc. */
    public int getCM_WebProject_ID() 
    {
        return get_ValueAsInt("CM_WebProject_ID");
        
    }
    
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID <= 0) set_Value ("C_DocType_ID", null);
        else
        set_Value ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
    }
    
    /** Set Manual.
    @param IsManual This is a manual process or entry */
    public void setIsManual (boolean IsManual)
    {
        set_Value ("IsManual", Boolean.valueOf(IsManual));
        
    }
    
    /** Get Manual.
    @return This is a manual process or entry */
    public boolean isManual() 
    {
        return get_ValueAsBoolean("IsManual");
        
    }
    
    /** Set Index Stop.
    @param K_IndexStop_ID Keyword not to be indexed */
    public void setK_IndexStop_ID (int K_IndexStop_ID)
    {
        if (K_IndexStop_ID < 1) throw new IllegalArgumentException ("K_IndexStop_ID is mandatory.");
        set_ValueNoCheck ("K_IndexStop_ID", Integer.valueOf(K_IndexStop_ID));
        
    }
    
    /** Get Index Stop.
    @return Keyword not to be indexed */
    public int getK_IndexStop_ID() 
    {
        return get_ValueAsInt("K_IndexStop_ID");
        
    }
    
    /** Set Keyword.
    @param Keyword Case insensitive keyword */
    public void setKeyword (String Keyword)
    {
        if (Keyword == null) throw new IllegalArgumentException ("Keyword is mandatory.");
        set_Value ("Keyword", Keyword);
        
    }
    
    /** Get Keyword.
    @return Case insensitive keyword */
    public String getKeyword() 
    {
        return (String)get_Value("Keyword");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getKeyword());
        
    }
    
    /** Set Request Type.
    @param R_RequestType_ID Type of request (e.g. Inquiry, Complaint...) */
    public void setR_RequestType_ID (int R_RequestType_ID)
    {
        if (R_RequestType_ID <= 0) set_Value ("R_RequestType_ID", null);
        else
        set_Value ("R_RequestType_ID", Integer.valueOf(R_RequestType_ID));
        
    }
    
    /** Get Request Type.
    @return Type of request (e.g. Inquiry, Complaint...) */
    public int getR_RequestType_ID() 
    {
        return get_ValueAsInt("R_RequestType_ID");
        
    }
    
    
}
