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
/** Generated Model for C_Conversion_Rate
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Conversion_Rate.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Conversion_Rate extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Conversion_Rate_ID id
    @param trx transaction
    */
    public X_C_Conversion_Rate (Ctx ctx, int C_Conversion_Rate_ID, Trx trx)
    {
        super (ctx, C_Conversion_Rate_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Conversion_Rate_ID == 0)
        {
            setC_ConversionType_ID (0);
            setC_Conversion_Rate_ID (0);
            setC_Currency_ID (0);
            setC_Currency_To_ID (0);
            setDivideRate (Env.ZERO);
            setMultiplyRate (Env.ZERO);
            setValidFrom (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Conversion_Rate (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=140 */
    public static final int Table_ID=140;
    
    /** TableName=C_Conversion_Rate */
    public static final String Table_Name="C_Conversion_Rate";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Rate Type.
    @param C_ConversionType_ID Currency Conversion Rate Type */
    public void setC_ConversionType_ID (int C_ConversionType_ID)
    {
        if (C_ConversionType_ID < 1) throw new IllegalArgumentException ("C_ConversionType_ID is mandatory.");
        set_Value ("C_ConversionType_ID", Integer.valueOf(C_ConversionType_ID));
        
    }
    
    /** Get Rate Type.
    @return Currency Conversion Rate Type */
    public int getC_ConversionType_ID() 
    {
        return get_ValueAsInt("C_ConversionType_ID");
        
    }
    
    /** Set Conversion Rate.
    @param C_Conversion_Rate_ID Rate used for converting currencies */
    public void setC_Conversion_Rate_ID (int C_Conversion_Rate_ID)
    {
        if (C_Conversion_Rate_ID < 1) throw new IllegalArgumentException ("C_Conversion_Rate_ID is mandatory.");
        set_ValueNoCheck ("C_Conversion_Rate_ID", Integer.valueOf(C_Conversion_Rate_ID));
        
    }
    
    /** Get Conversion Rate.
    @return Rate used for converting currencies */
    public int getC_Conversion_Rate_ID() 
    {
        return get_ValueAsInt("C_Conversion_Rate_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Conversion_Rate_ID()));
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Discontinued.
    @param C_Currency_ID_To Discontinued */
    public void setC_Currency_ID_To (int C_Currency_ID_To)
    {
        set_ValueNoCheck ("C_Currency_ID_To", Integer.valueOf(C_Currency_ID_To));
        
    }
    
    /** Get Discontinued.
    @return Discontinued */
    public int getC_Currency_ID_To() 
    {
        return get_ValueAsInt("C_Currency_ID_To");
        
    }
    
    /** Set Currency To.
    @param C_Currency_To_ID Target currency */
    public void setC_Currency_To_ID (int C_Currency_To_ID)
    {
        if (C_Currency_To_ID < 1) throw new IllegalArgumentException ("C_Currency_To_ID is mandatory.");
        set_Value ("C_Currency_To_ID", Integer.valueOf(C_Currency_To_ID));
        
    }
    
    /** Get Currency To.
    @return Target currency */
    public int getC_Currency_To_ID() 
    {
        return get_ValueAsInt("C_Currency_To_ID");
        
    }
    
    /** Set Divide Rate.
    @param DivideRate To convert Source number to Target number, the Source is divided */
    public void setDivideRate (java.math.BigDecimal DivideRate)
    {
        if (DivideRate == null) throw new IllegalArgumentException ("DivideRate is mandatory.");
        set_Value ("DivideRate", DivideRate);
        
    }
    
    /** Get Divide Rate.
    @return To convert Source number to Target number, the Source is divided */
    public java.math.BigDecimal getDivideRate() 
    {
        return get_ValueAsBigDecimal("DivideRate");
        
    }
    
    /** Set Multiply Rate.
    @param MultiplyRate Multiply Rate */
    public void setMultiplyRate (java.math.BigDecimal MultiplyRate)
    {
        if (MultiplyRate == null) throw new IllegalArgumentException ("MultiplyRate is mandatory.");
        set_Value ("MultiplyRate", MultiplyRate);
        
    }
    
    /** Get Multiply Rate.
    @return Multiply Rate */
    public java.math.BigDecimal getMultiplyRate() 
    {
        return get_ValueAsBigDecimal("MultiplyRate");
        
    }
    
    /** Set Valid from.
    @param ValidFrom Valid from including this date (first day) */
    public void setValidFrom (Timestamp ValidFrom)
    {
        if (ValidFrom == null) throw new IllegalArgumentException ("ValidFrom is mandatory.");
        set_Value ("ValidFrom", ValidFrom);
        
    }
    
    /** Get Valid from.
    @return Valid from including this date (first day) */
    public Timestamp getValidFrom() 
    {
        return (Timestamp)get_Value("ValidFrom");
        
    }
    
    /** Set Valid to.
    @param ValidTo Valid to including this date (last day) */
    public void setValidTo (Timestamp ValidTo)
    {
        set_Value ("ValidTo", ValidTo);
        
    }
    
    /** Get Valid to.
    @return Valid to including this date (last day) */
    public Timestamp getValidTo() 
    {
        return (Timestamp)get_Value("ValidTo");
        
    }
    
    
}
