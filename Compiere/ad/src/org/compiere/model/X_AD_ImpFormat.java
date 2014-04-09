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
/** Generated Model for AD_ImpFormat
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ImpFormat.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ImpFormat extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ImpFormat_ID id
    @param trx transaction
    */
    public X_AD_ImpFormat (Ctx ctx, int AD_ImpFormat_ID, Trx trx)
    {
        super (ctx, AD_ImpFormat_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ImpFormat_ID == 0)
        {
            setAD_ImpFormat_ID (0);
            setAD_Table_ID (0);
            setFormatType (null);
            setName (null);
            setProcessing (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ImpFormat (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=381 */
    public static final int Table_ID=381;
    
    /** TableName=AD_ImpFormat */
    public static final String Table_Name="AD_ImpFormat";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Import Format.
    @param AD_ImpFormat_ID Import Format */
    public void setAD_ImpFormat_ID (int AD_ImpFormat_ID)
    {
        if (AD_ImpFormat_ID < 1) throw new IllegalArgumentException ("AD_ImpFormat_ID is mandatory.");
        set_ValueNoCheck ("AD_ImpFormat_ID", Integer.valueOf(AD_ImpFormat_ID));
        
    }
    
    /** Get Import Format.
    @return Import Format */
    public int getAD_ImpFormat_ID() 
    {
        return get_ValueAsInt("AD_ImpFormat_ID");
        
    }
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
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
    
    /** Comma Separated = C */
    public static final String FORMATTYPE_CommaSeparated = X_Ref_AD_ImpFormat_FormatType.COMMA_SEPARATED.getValue();
    /** Fixed Position = F */
    public static final String FORMATTYPE_FixedPosition = X_Ref_AD_ImpFormat_FormatType.FIXED_POSITION.getValue();
    /** Tab Separated = T */
    public static final String FORMATTYPE_TabSeparated = X_Ref_AD_ImpFormat_FormatType.TAB_SEPARATED.getValue();
    /** XML = X */
    public static final String FORMATTYPE_XML = X_Ref_AD_ImpFormat_FormatType.XML.getValue();
    /** Set Format.
    @param FormatType Format of the data */
    public void setFormatType (String FormatType)
    {
        if (FormatType == null) throw new IllegalArgumentException ("FormatType is mandatory");
        if (!X_Ref_AD_ImpFormat_FormatType.isValid(FormatType))
        throw new IllegalArgumentException ("FormatType Invalid value - " + FormatType + " - Reference_ID=209 - C - F - T - X");
        set_Value ("FormatType", FormatType);
        
    }
    
    /** Get Format.
    @return Format of the data */
    public String getFormatType() 
    {
        return (String)get_Value("FormatType");
        
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
    
    
}
