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
/** Generated Model for AD_Attribute_Value
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Attribute_Value.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Attribute_Value extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Attribute_Value_ID id
    @param trx transaction
    */
    public X_AD_Attribute_Value (Ctx ctx, int AD_Attribute_Value_ID, Trx trx)
    {
        super (ctx, AD_Attribute_Value_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Attribute_Value_ID == 0)
        {
            setAD_Attribute_ID (0);
            setRecord_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Attribute_Value (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495260305789L;
    /** Last Updated Timestamp 2008-06-10 14:56:29.0 */
    public static final long updatedMS = 1213134989000L;
    /** AD_Table_ID=406 */
    public static final int Table_ID=406;
    
    /** TableName=AD_Attribute_Value */
    public static final String Table_Name="AD_Attribute_Value";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set System Attribute.
    @param AD_Attribute_ID Identifying attribute of a record */
    public void setAD_Attribute_ID (int AD_Attribute_ID)
    {
        if (AD_Attribute_ID < 1) throw new IllegalArgumentException ("AD_Attribute_ID is mandatory.");
        set_ValueNoCheck ("AD_Attribute_ID", Integer.valueOf(AD_Attribute_ID));
        
    }
    
    /** Get System Attribute.
    @return Identifying attribute of a record */
    public int getAD_Attribute_ID() 
    {
        return get_ValueAsInt("AD_Attribute_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Attribute_ID()));
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID < 0) throw new IllegalArgumentException ("Record_ID is mandatory.");
        set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set V_Date.
    @param V_Date V_Date */
    public void setV_Date (Timestamp V_Date)
    {
        set_Value ("V_Date", V_Date);
        
    }
    
    /** Get V_Date.
    @return V_Date */
    public Timestamp getV_Date() 
    {
        return (Timestamp)get_Value("V_Date");
        
    }
    
    /** Set V_Number.
    @param V_Number V_Number */
    public void setV_Number (java.math.BigDecimal V_Number)
    {
        set_Value ("V_Number", V_Number);
        
    }
    
    /** Get V_Number.
    @return V_Number */
    public java.math.BigDecimal getV_Number() 
    {
        return get_ValueAsBigDecimal("V_Number");
        
    }
    
    /** Set V_String.
    @param V_String V_String */
    public void setV_String (String V_String)
    {
        set_Value ("V_String", V_String);
        
    }
    
    /** Get V_String.
    @return V_String */
    public String getV_String() 
    {
        return (String)get_Value("V_String");
        
    }
    
    
}
