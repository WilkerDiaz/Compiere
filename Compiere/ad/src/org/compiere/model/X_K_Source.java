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
/** Generated Model for K_Source
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_K_Source.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_K_Source extends PO
{
    /** Standard Constructor
    @param ctx context
    @param K_Source_ID id
    @param trx transaction
    */
    public X_K_Source (Ctx ctx, int K_Source_ID, Trx trx)
    {
        super (ctx, K_Source_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (K_Source_ID == 0)
        {
            setK_Source_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_K_Source (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=609 */
    public static final int Table_ID=609;
    
    /** TableName=K_Source */
    public static final String Table_Name="K_Source";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Description URL.
    @param DescriptionURL URL for the description */
    public void setDescriptionURL (String DescriptionURL)
    {
        set_Value ("DescriptionURL", DescriptionURL);
        
    }
    
    /** Get Description URL.
    @return URL for the description */
    public String getDescriptionURL() 
    {
        return (String)get_Value("DescriptionURL");
        
    }
    
    /** Set Knowledge Source.
    @param K_Source_ID Source of a Knowledge Entry */
    public void setK_Source_ID (int K_Source_ID)
    {
        if (K_Source_ID < 1) throw new IllegalArgumentException ("K_Source_ID is mandatory.");
        set_ValueNoCheck ("K_Source_ID", Integer.valueOf(K_Source_ID));
        
    }
    
    /** Get Knowledge Source.
    @return Source of a Knowledge Entry */
    public int getK_Source_ID() 
    {
        return get_ValueAsInt("K_Source_ID");
        
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
