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
/** Generated Model for A_RegistrationProduct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_A_RegistrationProduct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_A_RegistrationProduct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param A_RegistrationProduct_ID id
    @param trx transaction
    */
    public X_A_RegistrationProduct (Ctx ctx, int A_RegistrationProduct_ID, Trx trx)
    {
        super (ctx, A_RegistrationProduct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (A_RegistrationProduct_ID == 0)
        {
            setA_RegistrationAttribute_ID (0);
            setM_Product_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_A_RegistrationProduct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=715 */
    public static final int Table_ID=715;
    
    /** TableName=A_RegistrationProduct */
    public static final String Table_Name="A_RegistrationProduct";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Registration Attribute.
    @param A_RegistrationAttribute_ID Asset Registration Attribute */
    public void setA_RegistrationAttribute_ID (int A_RegistrationAttribute_ID)
    {
        if (A_RegistrationAttribute_ID < 1) throw new IllegalArgumentException ("A_RegistrationAttribute_ID is mandatory.");
        set_ValueNoCheck ("A_RegistrationAttribute_ID", Integer.valueOf(A_RegistrationAttribute_ID));
        
    }
    
    /** Get Registration Attribute.
    @return Asset Registration Attribute */
    public int getA_RegistrationAttribute_ID() 
    {
        return get_ValueAsInt("A_RegistrationAttribute_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getA_RegistrationAttribute_ID()));
        
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
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    
}
