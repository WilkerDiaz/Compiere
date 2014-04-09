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
/** Generated Model for C_TaxPostal
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_TaxPostal.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_TaxPostal extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_TaxPostal_ID id
    @param trx transaction
    */
    public X_C_TaxPostal (Ctx ctx, int C_TaxPostal_ID, Trx trx)
    {
        super (ctx, C_TaxPostal_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_TaxPostal_ID == 0)
        {
            setC_TaxPostal_ID (0);
            setC_Tax_ID (0);
            setPostal (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_TaxPostal (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=701 */
    public static final int Table_ID=701;
    
    /** TableName=C_TaxPostal */
    public static final String Table_Name="C_TaxPostal";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Tax ZIP.
    @param C_TaxPostal_ID Tax Postal/ZIP */
    public void setC_TaxPostal_ID (int C_TaxPostal_ID)
    {
        if (C_TaxPostal_ID < 1) throw new IllegalArgumentException ("C_TaxPostal_ID is mandatory.");
        set_ValueNoCheck ("C_TaxPostal_ID", Integer.valueOf(C_TaxPostal_ID));
        
    }
    
    /** Get Tax ZIP.
    @return Tax Postal/ZIP */
    public int getC_TaxPostal_ID() 
    {
        return get_ValueAsInt("C_TaxPostal_ID");
        
    }
    
    /** Set Tax.
    @param C_Tax_ID Tax identifier */
    public void setC_Tax_ID (int C_Tax_ID)
    {
        if (C_Tax_ID < 1) throw new IllegalArgumentException ("C_Tax_ID is mandatory.");
        set_ValueNoCheck ("C_Tax_ID", Integer.valueOf(C_Tax_ID));
        
    }
    
    /** Get Tax.
    @return Tax identifier */
    public int getC_Tax_ID() 
    {
        return get_ValueAsInt("C_Tax_ID");
        
    }
    
    /** Set ZIP.
    @param Postal Postal code */
    public void setPostal (String Postal)
    {
        if (Postal == null) throw new IllegalArgumentException ("Postal is mandatory.");
        set_Value ("Postal", Postal);
        
    }
    
    /** Get ZIP.
    @return Postal code */
    public String getPostal() 
    {
        return (String)get_Value("Postal");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getPostal());
        
    }
    
    /** Set ZIP To.
    @param Postal_To Postal code to */
    public void setPostal_To (String Postal_To)
    {
        set_Value ("Postal_To", Postal_To);
        
    }
    
    /** Get ZIP To.
    @return Postal code to */
    public String getPostal_To() 
    {
        return (String)get_Value("Postal_To");
        
    }
    
    
}
