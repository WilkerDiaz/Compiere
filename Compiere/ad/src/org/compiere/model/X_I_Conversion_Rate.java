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
/** Generated Model for I_Conversion_Rate
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_I_Conversion_Rate.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_I_Conversion_Rate extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_Conversion_Rate_ID id
    @param trx transaction
    */
    public X_I_Conversion_Rate (Ctx ctx, int I_Conversion_Rate_ID, Trx trx)
    {
        super (ctx, I_Conversion_Rate_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_Conversion_Rate_ID == 0)
        {
            setI_Conversion_Rate_ID (0);
            setI_IsImported (null);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_Conversion_Rate (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511157886789L;
    /** Last Updated Timestamp 2008-12-11 13:56:10.0 */
    public static final long updatedMS = 1229032570000L;
    /** AD_Table_ID=641 */
    public static final int Table_ID=641;
    
    /** TableName=I_Conversion_Rate */
    public static final String Table_Name="I_Conversion_Rate";
    
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
        if (C_ConversionType_ID <= 0) set_Value ("C_ConversionType_ID", null);
        else
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
        if (C_Conversion_Rate_ID <= 0) set_Value ("C_Conversion_Rate_ID", null);
        else
        set_Value ("C_Conversion_Rate_ID", Integer.valueOf(C_Conversion_Rate_ID));
        
    }
    
    /** Get Conversion Rate.
    @return Rate used for converting currencies */
    public int getC_Conversion_Rate_ID() 
    {
        return get_ValueAsInt("C_Conversion_Rate_ID");
        
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
    
    /** Set Currency To.
    @param C_Currency_To_ID Target currency */
    public void setC_Currency_To_ID (int C_Currency_To_ID)
    {
        if (C_Currency_To_ID <= 0) set_Value ("C_Currency_To_ID", null);
        else
        set_Value ("C_Currency_To_ID", Integer.valueOf(C_Currency_To_ID));
        
    }
    
    /** Get Currency To.
    @return Target currency */
    public int getC_Currency_To_ID() 
    {
        return get_ValueAsInt("C_Currency_To_ID");
        
    }
    
    /** Set Currency Type Key.
    @param ConversionTypeValue Key value for the Currency Conversion Rate Type */
    public void setConversionTypeValue (String ConversionTypeValue)
    {
        set_Value ("ConversionTypeValue", ConversionTypeValue);
        
    }
    
    /** Get Currency Type Key.
    @return Key value for the Currency Conversion Rate Type */
    public String getConversionTypeValue() 
    {
        return (String)get_Value("ConversionTypeValue");
        
    }
    
    /** Set Create Reciprocal Rate.
    @param CreateReciprocalRate Create Reciprocal Rate from current information */
    public void setCreateReciprocalRate (boolean CreateReciprocalRate)
    {
        set_Value ("CreateReciprocalRate", Boolean.valueOf(CreateReciprocalRate));
        
    }
    
    /** Get Create Reciprocal Rate.
    @return Create Reciprocal Rate from current information */
    public boolean isCreateReciprocalRate() 
    {
        return get_ValueAsBoolean("CreateReciprocalRate");
        
    }
    
    /** Set Divide Rate.
    @param DivideRate To convert Source number to Target number, the Source is divided */
    public void setDivideRate (java.math.BigDecimal DivideRate)
    {
        set_Value ("DivideRate", DivideRate);
        
    }
    
    /** Get Divide Rate.
    @return To convert Source number to Target number, the Source is divided */
    public java.math.BigDecimal getDivideRate() 
    {
        return get_ValueAsBigDecimal("DivideRate");
        
    }
    
    /** Set ISO Currency Code.
    @param ISO_Code Three letter ISO 4217 Code of the Currency */
    public void setISO_Code (String ISO_Code)
    {
        set_Value ("ISO_Code", ISO_Code);
        
    }
    
    /** Get ISO Currency Code.
    @return Three letter ISO 4217 Code of the Currency */
    public String getISO_Code() 
    {
        return (String)get_Value("ISO_Code");
        
    }
    
    /** Set ISO Currency To Code.
    @param ISO_Code_To Three letter ISO 4217 Code of the To Currency */
    public void setISO_Code_To (String ISO_Code_To)
    {
        set_Value ("ISO_Code_To", ISO_Code_To);
        
    }
    
    /** Get ISO Currency To Code.
    @return Three letter ISO 4217 Code of the To Currency */
    public String getISO_Code_To() 
    {
        return (String)get_Value("ISO_Code_To");
        
    }
    
    /** Set Import Conversion Rate.
    @param I_Conversion_Rate_ID Import Currency Conversion Rate */
    public void setI_Conversion_Rate_ID (int I_Conversion_Rate_ID)
    {
        if (I_Conversion_Rate_ID < 1) throw new IllegalArgumentException ("I_Conversion_Rate_ID is mandatory.");
        set_ValueNoCheck ("I_Conversion_Rate_ID", Integer.valueOf(I_Conversion_Rate_ID));
        
    }
    
    /** Get Import Conversion Rate.
    @return Import Currency Conversion Rate */
    public int getI_Conversion_Rate_ID() 
    {
        return get_ValueAsInt("I_Conversion_Rate_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getI_Conversion_Rate_ID()));
        
    }
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Error = E */
    public static final String I_ISIMPORTED_Error = X_Ref__IsImported.ERROR.getValue();
    /** No = N */
    public static final String I_ISIMPORTED_No = X_Ref__IsImported.NO.getValue();
    /** Yes = Y */
    public static final String I_ISIMPORTED_Yes = X_Ref__IsImported.YES.getValue();
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (String I_IsImported)
    {
        if (I_IsImported == null) throw new IllegalArgumentException ("I_IsImported is mandatory");
        if (!X_Ref__IsImported.isValid(I_IsImported))
        throw new IllegalArgumentException ("I_IsImported Invalid value - " + I_IsImported + " - Reference_ID=420 - E - N - Y");
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set Multiply Rate.
    @param MultiplyRate Multiply Rate */
    public void setMultiplyRate (java.math.BigDecimal MultiplyRate)
    {
        set_Value ("MultiplyRate", MultiplyRate);
        
    }
    
    /** Get Multiply Rate.
    @return Multiply Rate */
    public java.math.BigDecimal getMultiplyRate() 
    {
        return get_ValueAsBigDecimal("MultiplyRate");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
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
    
    /** Set Valid from.
    @param ValidFrom Valid from including this date (first day) */
    public void setValidFrom (Timestamp ValidFrom)
    {
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
