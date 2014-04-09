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
/** Generated Model for K_Index
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_K_Index.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_K_Index extends PO
{
    /** Standard Constructor
    @param ctx context
    @param K_Index_ID id
    @param trx transaction
    */
    public X_K_Index (Ctx ctx, int K_Index_ID, Trx trx)
    {
        super (ctx, K_Index_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (K_Index_ID == 0)
        {
            setAD_Table_ID (0);
            setK_Index_ID (0);
            setKeyword (null);
            setRecord_ID (0);
            setSourceUpdated (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_K_Index (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=900 */
    public static final int Table_ID=900;
    
    /** TableName=K_Index */
    public static final String Table_Name="K_Index";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_ValueNoCheck ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
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
    
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID <= 0) set_ValueNoCheck ("C_DocType_ID", null);
        else
        set_ValueNoCheck ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
    }
    
    /** Set Excerpt.
    @param Excerpt Surrounding text of the keyword */
    public void setExcerpt (String Excerpt)
    {
        set_ValueNoCheck ("Excerpt", Excerpt);
        
    }
    
    /** Get Excerpt.
    @return Surrounding text of the keyword */
    public String getExcerpt() 
    {
        return (String)get_Value("Excerpt");
        
    }
    
    /** Set Index.
    @param K_Index_ID Text Search Index */
    public void setK_Index_ID (int K_Index_ID)
    {
        if (K_Index_ID < 1) throw new IllegalArgumentException ("K_Index_ID is mandatory.");
        set_ValueNoCheck ("K_Index_ID", Integer.valueOf(K_Index_ID));
        
    }
    
    /** Get Index.
    @return Text Search Index */
    public int getK_Index_ID() 
    {
        return get_ValueAsInt("K_Index_ID");
        
    }
    
    /** Set Keyword.
    @param Keyword Case insensitive keyword */
    public void setKeyword (String Keyword)
    {
        if (Keyword == null) throw new IllegalArgumentException ("Keyword is mandatory.");
        set_ValueNoCheck ("Keyword", Keyword);
        
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
        if (R_RequestType_ID <= 0) set_ValueNoCheck ("R_RequestType_ID", null);
        else
        set_ValueNoCheck ("R_RequestType_ID", Integer.valueOf(R_RequestType_ID));
        
    }
    
    /** Get Request Type.
    @return Type of request (e.g. Inquiry, Complaint...) */
    public int getR_RequestType_ID() 
    {
        return get_ValueAsInt("R_RequestType_ID");
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID < 0) throw new IllegalArgumentException ("Record_ID is mandatory.");
        set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set Source Updated.
    @param SourceUpdated Date the source document was updated */
    public void setSourceUpdated (Timestamp SourceUpdated)
    {
        if (SourceUpdated == null) throw new IllegalArgumentException ("SourceUpdated is mandatory.");
        set_Value ("SourceUpdated", SourceUpdated);
        
    }
    
    /** Get Source Updated.
    @return Date the source document was updated */
    public Timestamp getSourceUpdated() 
    {
        return (Timestamp)get_Value("SourceUpdated");
        
    }
    
    
}
