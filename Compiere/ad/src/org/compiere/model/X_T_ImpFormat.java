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
/** Generated Model for T_ImpFormat
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_ImpFormat.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_ImpFormat extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_ImpFormat_ID id
    @param trx transaction
    */
    public X_T_ImpFormat (Ctx ctx, int T_ImpFormat_ID, Trx trx)
    {
        super (ctx, T_ImpFormat_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_ImpFormat_ID == 0)
        {
            setT_ImpFormat_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_ImpFormat (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511671674789L;
    /** Last Updated Timestamp 2008-12-17 12:39:18.0 */
    public static final long updatedMS = 1229546358000L;
    /** AD_Table_ID=992 */
    public static final int Table_ID=992;
    
    /** TableName=T_ImpFormat */
    public static final String Table_Name="T_ImpFormat";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Import Format.
    @param T_ImpFormat_ID Import Format */
    public void setT_ImpFormat_ID (int T_ImpFormat_ID)
    {
        if (T_ImpFormat_ID < 1) throw new IllegalArgumentException ("T_ImpFormat_ID is mandatory.");
        set_ValueNoCheck ("T_ImpFormat_ID", Integer.valueOf(T_ImpFormat_ID));
        
    }
    
    /** Get Import Format.
    @return Import Format */
    public int getT_ImpFormat_ID() 
    {
        return get_ValueAsInt("T_ImpFormat_ID");
        
    }
    
    /** Set Web Session.
    @param WebSession Web Session ID */
    public void setWebSession (String WebSession)
    {
        set_Value ("WebSession", WebSession);
        
    }
    
    /** Get Web Session.
    @return Web Session ID */
    public String getWebSession() 
    {
        return (String)get_Value("WebSession");
        
    }
    
    
}
