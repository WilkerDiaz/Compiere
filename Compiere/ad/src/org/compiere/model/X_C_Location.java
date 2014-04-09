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
/** Generated Model for C_Location
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Location.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Location extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Location_ID id
    @param trx transaction
    */
    public X_C_Location (Ctx ctx, int C_Location_ID, Trx trx)
    {
        super (ctx, C_Location_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Location_ID == 0)
        {
            setC_Country_ID (0);
            setC_Location_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Location (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=162 */
    public static final int Table_ID=162;
    
    /** TableName=C_Location */
    public static final String Table_Name="C_Location";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Address 1.
    @param Address1 Address line 1 for this location */
    public void setAddress1 (String Address1)
    {
        set_Value ("Address1", Address1);
        
    }
    
    /** Get Address 1.
    @return Address line 1 for this location */
    public String getAddress1() 
    {
        return (String)get_Value("Address1");
        
    }
    
    /** Set Address 2.
    @param Address2 Address line 2 for this location */
    public void setAddress2 (String Address2)
    {
        set_Value ("Address2", Address2);
        
    }
    
    /** Get Address 2.
    @return Address line 2 for this location */
    public String getAddress2() 
    {
        return (String)get_Value("Address2");
        
    }
    
    /** Set Address 3.
    @param Address3 Address Line 3 for the location */
    public void setAddress3 (String Address3)
    {
        set_Value ("Address3", Address3);
        
    }
    
    /** Get Address 3.
    @return Address Line 3 for the location */
    public String getAddress3() 
    {
        return (String)get_Value("Address3");
        
    }
    
    /** Set Address 4.
    @param Address4 Address Line 4 for the location */
    public void setAddress4 (String Address4)
    {
        set_Value ("Address4", Address4);
        
    }
    
    /** Get Address 4.
    @return Address Line 4 for the location */
    public String getAddress4() 
    {
        return (String)get_Value("Address4");
        
    }
    
    /** Set City.
    @param C_City_ID City */
    public void setC_City_ID (int C_City_ID)
    {
        if (C_City_ID <= 0) set_Value ("C_City_ID", null);
        else
        set_Value ("C_City_ID", Integer.valueOf(C_City_ID));
        
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
        if (C_Country_ID < 1) throw new IllegalArgumentException ("C_Country_ID is mandatory.");
        set_Value ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
    }
    
    /** Set Address.
    @param C_Location_ID Location or Address */
    public void setC_Location_ID (int C_Location_ID)
    {
        if (C_Location_ID < 1) throw new IllegalArgumentException ("C_Location_ID is mandatory.");
        set_ValueNoCheck ("C_Location_ID", Integer.valueOf(C_Location_ID));
        
    }
    
    /** Get Address.
    @return Location or Address */
    public int getC_Location_ID() 
    {
        return get_ValueAsInt("C_Location_ID");
        
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
    
    /** Set City Name.
    @param City Identifies a City */
    public void setCity (String City)
    {
        set_Value ("City", City);
        
    }
    
    /** Get City Name.
    @return Identifies a City */
    public String getCity() 
    {
        return (String)get_Value("City");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getCity());
        
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
    
    /** Set -.
    @param Postal_Add Additional ZIP or Postal code */
    public void setPostal_Add (String Postal_Add)
    {
        set_Value ("Postal_Add", Postal_Add);
        
    }
    
    /** Get -.
    @return Additional ZIP or Postal code */
    public String getPostal_Add() 
    {
        return (String)get_Value("Postal_Add");
        
    }
    
    /** Set Region Name.
    @param RegionName Name of the Region */
    public void setRegionName (String RegionName)
    {
        set_Value ("RegionName", RegionName);
        
    }
    
    /** Get Region Name.
    @return Name of the Region */
    public String getRegionName() 
    {
        return (String)get_Value("RegionName");
        
    }
    
    
}
