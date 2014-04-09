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
/** Generated Model for AD_Language
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Language.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Language extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Language_ID id
    @param trx transaction
    */
    public X_AD_Language (Ctx ctx, int AD_Language_ID, Trx trx)
    {
        super (ctx, AD_Language_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Language_ID == 0)
        {
            setAD_Language (null);
            setAD_Language_ID (0);	// @SQL=SELECT NVL(MAX(AD_Language_ID),999999)+1 FROM AD_Language WHERE AD_Language_ID > 1000
            setCountryCode (null);
            setIsBaseLanguage (false);	// N
            setIsDecimalPoint (false);
            setIsSystemLanguage (false);
            setLanguageISO (null);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Language (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=111 */
    public static final int Table_ID=111;
    
    /** TableName=AD_Language */
    public static final String Table_Name="AD_Language";
    
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
        if (AD_Language == null) throw new IllegalArgumentException ("AD_Language is mandatory.");
        set_ValueNoCheck ("AD_Language", AD_Language);
        
    }
    
    /** Get Language.
    @return Language for this entity */
    public String getAD_Language() 
    {
        return (String)get_Value("AD_Language");
        
    }
    
    /** Set Language.
    @param AD_Language_ID System Language */
    public void setAD_Language_ID (int AD_Language_ID)
    {
        if (AD_Language_ID < 1) throw new IllegalArgumentException ("AD_Language_ID is mandatory.");
        set_ValueNoCheck ("AD_Language_ID", Integer.valueOf(AD_Language_ID));
        
    }
    
    /** Get Language.
    @return System Language */
    public int getAD_Language_ID() 
    {
        return get_ValueAsInt("AD_Language_ID");
        
    }
    
    /** Set ISO Country Code.
    @param CountryCode Upper-case two-letter alphabetic ISO Country code according to ISO 3166-1 - http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html */
    public void setCountryCode (String CountryCode)
    {
        if (CountryCode == null) throw new IllegalArgumentException ("CountryCode is mandatory.");
        set_Value ("CountryCode", CountryCode);
        
    }
    
    /** Get ISO Country Code.
    @return Upper-case two-letter alphabetic ISO Country code according to ISO 3166-1 - http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html */
    public String getCountryCode() 
    {
        return (String)get_Value("CountryCode");
        
    }
    
    /** Set Date Pattern.
    @param DatePattern Java Date Pattern */
    public void setDatePattern (String DatePattern)
    {
        set_Value ("DatePattern", DatePattern);
        
    }
    
    /** Get Date Pattern.
    @return Java Date Pattern */
    public String getDatePattern() 
    {
        return (String)get_Value("DatePattern");
        
    }
    
    /** Set Base Language.
    @param IsBaseLanguage The system information is maintained in this language */
    public void setIsBaseLanguage (boolean IsBaseLanguage)
    {
        set_ValueNoCheck ("IsBaseLanguage", Boolean.valueOf(IsBaseLanguage));
        
    }
    
    /** Get Base Language.
    @return The system information is maintained in this language */
    public boolean isBaseLanguage() 
    {
        return get_ValueAsBoolean("IsBaseLanguage");
        
    }
    
    /** Set Decimal Point.
    @param IsDecimalPoint The number notation has a decimal point (no decimal comma) */
    public void setIsDecimalPoint (boolean IsDecimalPoint)
    {
        set_Value ("IsDecimalPoint", Boolean.valueOf(IsDecimalPoint));
        
    }
    
    /** Get Decimal Point.
    @return The number notation has a decimal point (no decimal comma) */
    public boolean isDecimalPoint() 
    {
        return get_ValueAsBoolean("IsDecimalPoint");
        
    }
    
    /** Set System Language.
    @param IsSystemLanguage The screens, etc. are maintained in this Language */
    public void setIsSystemLanguage (boolean IsSystemLanguage)
    {
        set_Value ("IsSystemLanguage", Boolean.valueOf(IsSystemLanguage));
        
    }
    
    /** Get System Language.
    @return The screens, etc. are maintained in this Language */
    public boolean isSystemLanguage() 
    {
        return get_ValueAsBoolean("IsSystemLanguage");
        
    }
    
    /** Set ISO Language Code.
    @param LanguageISO Lower-case two-letter ISO-3166 code - http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt */
    public void setLanguageISO (String LanguageISO)
    {
        if (LanguageISO == null) throw new IllegalArgumentException ("LanguageISO is mandatory.");
        set_Value ("LanguageISO", LanguageISO);
        
    }
    
    /** Get ISO Language Code.
    @return Lower-case two-letter ISO-3166 code - http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt */
    public String getLanguageISO() 
    {
        return (String)get_Value("LanguageISO");
        
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
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Time Pattern.
    @param TimePattern Java Time Pattern */
    public void setTimePattern (String TimePattern)
    {
        set_Value ("TimePattern", TimePattern);
        
    }
    
    /** Get Time Pattern.
    @return Java Time Pattern */
    public String getTimePattern() 
    {
        return (String)get_Value("TimePattern");
        
    }
    
    
}
