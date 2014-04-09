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
/** Generated Model for AD_ImpFormat_Row
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ImpFormat_Row.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ImpFormat_Row extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ImpFormat_Row_ID id
    @param trx transaction
    */
    public X_AD_ImpFormat_Row (Ctx ctx, int AD_ImpFormat_Row_ID, Trx trx)
    {
        super (ctx, AD_ImpFormat_Row_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ImpFormat_Row_ID == 0)
        {
            setAD_Column_ID (0);
            setAD_ImpFormat_ID (0);
            setAD_ImpFormat_Row_ID (0);
            setDataType (null);
            setDecimalPoint (null);	// .
            setDivideBy100 (false);
            setName (null);
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_ImpFormat_Row WHERE AD_ImpFormat_ID=@AD_ImpFormat_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ImpFormat_Row (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=382 */
    public static final int Table_ID=382;
    
    /** TableName=AD_ImpFormat_Row */
    public static final String Table_Name="AD_ImpFormat_Row";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Column.
    @param AD_Column_ID Column in the table */
    public void setAD_Column_ID (int AD_Column_ID)
    {
        if (AD_Column_ID < 1) throw new IllegalArgumentException ("AD_Column_ID is mandatory.");
        set_Value ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
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
    
    /** Set Format Field.
    @param AD_ImpFormat_Row_ID Format Field */
    public void setAD_ImpFormat_Row_ID (int AD_ImpFormat_Row_ID)
    {
        if (AD_ImpFormat_Row_ID < 1) throw new IllegalArgumentException ("AD_ImpFormat_Row_ID is mandatory.");
        set_ValueNoCheck ("AD_ImpFormat_Row_ID", Integer.valueOf(AD_ImpFormat_Row_ID));
        
    }
    
    /** Get Format Field.
    @return Format Field */
    public int getAD_ImpFormat_Row_ID() 
    {
        return get_ValueAsInt("AD_ImpFormat_Row_ID");
        
    }
    
    /** Set Callout Code.
    @param Callout External Callout Code - Fully qualified class names and method - separated by semicolons */
    public void setCallout (String Callout)
    {
        set_Value ("Callout", Callout);
        
    }
    
    /** Get Callout Code.
    @return External Callout Code - Fully qualified class names and method - separated by semicolons */
    public String getCallout() 
    {
        return (String)get_Value("Callout");
        
    }
    
    /** Set Constant Value.
    @param ConstantValue Constant value */
    public void setConstantValue (String ConstantValue)
    {
        set_Value ("ConstantValue", ConstantValue);
        
    }
    
    /** Get Constant Value.
    @return Constant value */
    public String getConstantValue() 
    {
        return (String)get_Value("ConstantValue");
        
    }
    
    /** Set Data Format.
    @param DataFormat Format String in Java Notation, e.g. ddMMyy */
    public void setDataFormat (String DataFormat)
    {
        set_Value ("DataFormat", DataFormat);
        
    }
    
    /** Get Data Format.
    @return Format String in Java Notation, e.g. ddMMyy */
    public String getDataFormat() 
    {
        return (String)get_Value("DataFormat");
        
    }
    
    /** Constant = C */
    public static final String DATATYPE_Constant = X_Ref_AD_ImpFormat_Row_Type.CONSTANT.getValue();
    /** Date = D */
    public static final String DATATYPE_Date = X_Ref_AD_ImpFormat_Row_Type.DATE.getValue();
    /** Number = N */
    public static final String DATATYPE_Number = X_Ref_AD_ImpFormat_Row_Type.NUMBER.getValue();
    /** String = S */
    public static final String DATATYPE_String = X_Ref_AD_ImpFormat_Row_Type.STRING.getValue();
    /** Set Data Type.
    @param DataType Type of data */
    public void setDataType (String DataType)
    {
        if (DataType == null) throw new IllegalArgumentException ("DataType is mandatory");
        if (!X_Ref_AD_ImpFormat_Row_Type.isValid(DataType))
        throw new IllegalArgumentException ("DataType Invalid value - " + DataType + " - Reference_ID=210 - C - D - N - S");
        set_Value ("DataType", DataType);
        
    }
    
    /** Get Data Type.
    @return Type of data */
    public String getDataType() 
    {
        return (String)get_Value("DataType");
        
    }
    
    /** Set Decimal Point.
    @param DecimalPoint Decimal Point in the data file - if any */
    public void setDecimalPoint (String DecimalPoint)
    {
        if (DecimalPoint == null) throw new IllegalArgumentException ("DecimalPoint is mandatory.");
        set_Value ("DecimalPoint", DecimalPoint);
        
    }
    
    /** Get Decimal Point.
    @return Decimal Point in the data file - if any */
    public String getDecimalPoint() 
    {
        return (String)get_Value("DecimalPoint");
        
    }
    
    /** Set Divide by 100.
    @param DivideBy100 Divide number by 100 to get correct amount */
    public void setDivideBy100 (boolean DivideBy100)
    {
        set_Value ("DivideBy100", Boolean.valueOf(DivideBy100));
        
    }
    
    /** Get Divide by 100.
    @return Divide number by 100 to get correct amount */
    public boolean isDivideBy100() 
    {
        return get_ValueAsBoolean("DivideBy100");
        
    }
    
    /** Set End No.
    @param EndNo End No */
    public void setEndNo (int EndNo)
    {
        set_Value ("EndNo", Integer.valueOf(EndNo));
        
    }
    
    /** Get End No.
    @return End No */
    public int getEndNo() 
    {
        return get_ValueAsInt("EndNo");
        
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
    
    /** Set Script.
    @param Script Dynamic Java Language Script to calculate result */
    public void setScript (String Script)
    {
        set_Value ("Script", Script);
        
    }
    
    /** Get Script.
    @return Dynamic Java Language Script to calculate result */
    public String getScript() 
    {
        return (String)get_Value("Script");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    /** Set Start No.
    @param StartNo Starting number/position */
    public void setStartNo (int StartNo)
    {
        set_Value ("StartNo", Integer.valueOf(StartNo));
        
    }
    
    /** Get Start No.
    @return Starting number/position */
    public int getStartNo() 
    {
        return get_ValueAsInt("StartNo");
        
    }
    
    
}
