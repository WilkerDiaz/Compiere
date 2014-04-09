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
/** Generated Model for M_BOMAlternative
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_BOMAlternative.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_BOMAlternative extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_BOMAlternative_ID id
    @param trx transaction
    */
    public X_M_BOMAlternative (Ctx ctx, int M_BOMAlternative_ID, Trx trx)
    {
        super (ctx, M_BOMAlternative_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_BOMAlternative_ID == 0)
        {
            setM_BOMAlternative_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_BOMAlternative (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27497746961789L;
    /** Last Updated Timestamp 2008-07-09 09:40:45.0 */
    public static final long updatedMS = 1215621645000L;
    /** AD_Table_ID=795 */
    public static final int Table_ID=795;
    
    /** TableName=M_BOMAlternative */
    public static final String Table_Name="M_BOMAlternative";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Alternative Group.
    @param M_BOMAlternative_ID Product BOM Alternative Group */
    public void setM_BOMAlternative_ID (int M_BOMAlternative_ID)
    {
        if (M_BOMAlternative_ID < 1) throw new IllegalArgumentException ("M_BOMAlternative_ID is mandatory.");
        set_ValueNoCheck ("M_BOMAlternative_ID", Integer.valueOf(M_BOMAlternative_ID));
        
    }
    
    /** Get Alternative Group.
    @return Product BOM Alternative Group */
    public int getM_BOMAlternative_ID() 
    {
        return get_ValueAsInt("M_BOMAlternative_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_ValueNoCheck ("M_Product_ID", null);
        else
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
