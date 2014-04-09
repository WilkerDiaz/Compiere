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
/** Generated Model for C_Country
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Country.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Country extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Country_ID id
    @param trx transaction
    */
    public X_C_Country (Ctx ctx, int C_Country_ID, Trx trx)
    {
        super (ctx, C_Country_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Country_ID == 0)
        {
            setC_Country_ID (0);
            setIsSummary (false);	// N
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Country (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27536891290789L;
    /** Last Updated Timestamp 2009-10-05 11:06:14.0 */
    public static final long updatedMS = 1254765974000L;
    /** AD_Table_ID=170 */
    public static final int Table_ID=170;
    
    /** TableName=C_Country */
    public static final String Table_Name="C_Country";
    
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
        set_Value ("AD_Language", AD_Language);
        
    }
    
    /** Get Language.
    @return Language for this entity */
    public String getAD_Language() 
    {
        return (String)get_Value("AD_Language");
        
    }
    
    /** Set Country.
    @param C_Country_ID Country */
    public void setC_Country_ID (int C_Country_ID)
    {
        if (C_Country_ID < 1) throw new IllegalArgumentException ("C_Country_ID is mandatory.");
        set_ValueNoCheck ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
        else
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set ISO Country Code.
    @param CountryCode Upper-case two-letter alphabetic ISO Country code according to ISO 3166-1 - http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html */
    public void setCountryCode (String CountryCode)
    {
        set_ValueNoCheck ("CountryCode", CountryCode);
        
    }
    
    /** Get ISO Country Code.
    @return Upper-case two-letter alphabetic ISO Country code according to ISO 3166-1 - http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html */
    public String getCountryCode() 
    {
        return (String)get_Value("CountryCode");
        
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
    
    /** Set Address Print Format.
    @param DisplaySequence Format for printing this Address */
    public void setDisplaySequence (String DisplaySequence)
    {
        set_Value ("DisplaySequence", DisplaySequence);
        
    }
    
    /** Get Address Print Format.
    @return Format for printing this Address */
    public String getDisplaySequence() 
    {
        return (String)get_Value("DisplaySequence");
        
    }
    
    /** Set Local Address Format.
    @param DisplaySequenceLocal Format for printing this Address locally */
    public void setDisplaySequenceLocal (String DisplaySequenceLocal)
    {
        set_Value ("DisplaySequenceLocal", DisplaySequenceLocal);
        
    }
    
    /** Get Local Address Format.
    @return Format for printing this Address locally */
    public String getDisplaySequenceLocal() 
    {
        return (String)get_Value("DisplaySequenceLocal");
        
    }
    
    /** Set Bank Account No Format.
    @param ExpressionBankAccountNo Format of the Bank Account */
    public void setExpressionBankAccountNo (String ExpressionBankAccountNo)
    {
        set_Value ("ExpressionBankAccountNo", ExpressionBankAccountNo);
        
    }
    
    /** Get Bank Account No Format.
    @return Format of the Bank Account */
    public String getExpressionBankAccountNo() 
    {
        return (String)get_Value("ExpressionBankAccountNo");
        
    }
    
    /** Set Bank Routing No Format.
    @param ExpressionBankRoutingNo Format of the Bank Routing Number */
    public void setExpressionBankRoutingNo (String ExpressionBankRoutingNo)
    {
        set_Value ("ExpressionBankRoutingNo", ExpressionBankRoutingNo);
        
    }
    
    /** Get Bank Routing No Format.
    @return Format of the Bank Routing Number */
    public String getExpressionBankRoutingNo() 
    {
        return (String)get_Value("ExpressionBankRoutingNo");
        
    }
    
    /** Set Phone Format.
    @param ExpressionPhone Format of the phone which can contain fixed format elements, Variables: "_lLoOaAcCa09" */
    public void setExpressionPhone (String ExpressionPhone)
    {
        set_Value ("ExpressionPhone", ExpressionPhone);
        
    }
    
    /** Get Phone Format.
    @return Format of the phone which can contain fixed format elements, Variables: "_lLoOaAcCa09" */
    public String getExpressionPhone() 
    {
        return (String)get_Value("ExpressionPhone");
        
    }
    
    /** Set Postal Code Format.
    @param ExpressionPostal Format of the postal code;
     Can contain fixed format elements, Variables: "_lLoOaAcCa09" */
    public void setExpressionPostal (String ExpressionPostal)
    {
        set_Value ("ExpressionPostal", ExpressionPostal);
        
    }
    
    /** Get Postal Code Format.
    @return Format of the postal code;
     Can contain fixed format elements, Variables: "_lLoOaAcCa09" */
    public String getExpressionPostal() 
    {
        return (String)get_Value("ExpressionPostal");
        
    }
    
    /** Set Additional Postal Format.
    @param ExpressionPostal_Add Format of the value which can contain fixed format elements, Variables: "_lLoOaAcCa09" */
    public void setExpressionPostal_Add (String ExpressionPostal_Add)
    {
        set_Value ("ExpressionPostal_Add", ExpressionPostal_Add);
        
    }
    
    /** Get Additional Postal Format.
    @return Format of the value which can contain fixed format elements, Variables: "_lLoOaAcCa09" */
    public String getExpressionPostal_Add() 
    {
        return (String)get_Value("ExpressionPostal_Add");
        
    }
    
    /** Set Additional Postal code.
    @param HasPostal_Add Has Additional Postal Code */
    public void setHasPostal_Add (boolean HasPostal_Add)
    {
        set_Value ("HasPostal_Add", Boolean.valueOf(HasPostal_Add));
        
    }
    
    /** Get Additional Postal code.
    @return Has Additional Postal Code */
    public boolean isHasPostal_Add() 
    {
        return get_ValueAsBoolean("HasPostal_Add");
        
    }
    
    /** Set Country has Region.
    @param HasRegion Country contains Regions */
    public void setHasRegion (boolean HasRegion)
    {
        set_Value ("HasRegion", Boolean.valueOf(HasRegion));
        
    }
    
    /** Get Country has Region.
    @return Country contains Regions */
    public boolean isHasRegion() 
    {
        return get_ValueAsBoolean("HasRegion");
        
    }
    
    /** Set Reverse Local Address Lines.
    @param IsAddressLinesLocalReverse Print Local Address in reverse Order */
    public void setIsAddressLinesLocalReverse (boolean IsAddressLinesLocalReverse)
    {
        set_ValueNoCheck ("IsAddressLinesLocalReverse", Boolean.valueOf(IsAddressLinesLocalReverse));
        
    }
    
    /** Get Reverse Local Address Lines.
    @return Print Local Address in reverse Order */
    public boolean isAddressLinesLocalReverse() 
    {
        return get_ValueAsBoolean("IsAddressLinesLocalReverse");
        
    }
    
    /** Set Reverse Address Lines.
    @param IsAddressLinesReverse Print Address in reverse Order */
    public void setIsAddressLinesReverse (boolean IsAddressLinesReverse)
    {
        set_Value ("IsAddressLinesReverse", Boolean.valueOf(IsAddressLinesReverse));
        
    }
    
    /** Get Reverse Address Lines.
    @return Print Address in reverse Order */
    public boolean isAddressLinesReverse() 
    {
        return get_ValueAsBoolean("IsAddressLinesReverse");
        
    }
    
    /** Set Summary Level.
    @param IsSummary This is a summary entity */
    public void setIsSummary (boolean IsSummary)
    {
        set_Value ("IsSummary", Boolean.valueOf(IsSummary));
        
    }
    
    /** Get Summary Level.
    @return This is a summary entity */
    public boolean isSummary() 
    {
        return get_ValueAsBoolean("IsSummary");
        
    }
    
    /** Set Media Size.
    @param MediaSize Java Media Size */
    public void setMediaSize (String MediaSize)
    {
        set_Value ("MediaSize", MediaSize);
        
    }
    
    /** Get Media Size.
    @return Java Media Size */
    public String getMediaSize() 
    {
        return (String)get_Value("MediaSize");
        
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
