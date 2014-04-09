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
/** Generated Model for C_City
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_City.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_City extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_City_ID id
    @param trx transaction
    */
    public X_C_City (Ctx ctx, int C_City_ID, Trx trx)
    {
        super (ctx, C_City_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_City_ID == 0)
        {
            setC_City_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_City (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27530423930789L;
    /** Last Updated Timestamp 2009-07-22 14:36:54.0 */
    public static final long updatedMS = 1248298614000L;
    /** AD_Table_ID=186 */
    public static final int Table_ID=186;
    
    /** TableName=C_City */
    public static final String Table_Name="C_City";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Area Code.
    @param AreaCode Phone Area Code */
    public void setAreaCode (String AreaCode)
    {
        set_Value ("AreaCode", AreaCode);
        
    }
    
    /** Get Area Code.
    @return Phone Area Code */
    public String getAreaCode() 
    {
        return (String)get_Value("AreaCode");
        
    }
    
    /** Set City.
    @param C_City_ID City */
    public void setC_City_ID (int C_City_ID)
    {
        if (C_City_ID < 1) throw new IllegalArgumentException ("C_City_ID is mandatory.");
        set_ValueNoCheck ("C_City_ID", Integer.valueOf(C_City_ID));
        
    }
    
    /** Get City.
    @return City */
    public int getC_City_ID() 
    {
        return get_ValueAsInt("C_City_ID");
        
    }
    
    /** Set Country.
    @param C_Country_ID Country */
    public void setC_Country_ID (int C_Country_ID)
    {
        if (C_Country_ID <= 0) set_ValueNoCheck ("C_Country_ID", null);
        else
        set_ValueNoCheck ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
    }
    
    /** Set Region.
    @param C_Region_ID Identifies a geographical Region */
    public void setC_Region_ID (int C_Region_ID)
    {
        if (C_Region_ID <= 0) set_Value ("C_Region_ID", null);
        else
        set_Value ("C_Region_ID", Integer.valueOf(C_Region_ID));
        
    }
    
    /** Get Region.
    @return Identifies a geographical Region */
    public int getC_Region_ID() 
    {
        return get_ValueAsInt("C_Region_ID");
        
    }
    
    /** Set Coordinates.
    @param Coordinates Location coordinate */
    public void setCoordinates (String Coordinates)
    {
        set_Value ("Coordinates", Coordinates);
        
    }
    
    /** Get Coordinates.
    @return Location coordinate */
    public String getCoordinates() 
    {
        return (String)get_Value("Coordinates");
        
    }
    
    /** Set Locode.
    @param Locode Location code - UN/LOCODE */
    public void setLocode (String Locode)
    {
        set_Value ("Locode", Locode);
        
    }
    
    /** Get Locode.
    @return Location code - UN/LOCODE */
    public String getLocode() 
    {
        return (String)get_Value("Locode");
        
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
    
    /** Set ZIP.
    @param Postal Postal code */
    public void setPostal (String Postal)
    {
        set_Value ("Postal", Postal);
        
    }
    
    /** Get ZIP.
    @return Postal code */
    public String getPostal() 
    {
        return (String)get_Value("Postal");
        
    }
    
    
}
