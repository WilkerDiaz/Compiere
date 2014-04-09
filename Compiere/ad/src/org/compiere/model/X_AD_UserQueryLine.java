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
/** Generated Model for AD_UserQueryLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_UserQueryLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_UserQueryLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_UserQueryLine_ID id
    @param trx transaction
    */
    public X_AD_UserQueryLine (Ctx ctx, int AD_UserQueryLine_ID, Trx trx)
    {
        super (ctx, AD_UserQueryLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_UserQueryLine_ID == 0)
        {
            setAD_UserQueryLine_ID (0);
            setAD_UserQuery_ID (0);
            setIsAnd (true);	// Y
            setKeyName (null);
            setKeyValue (null);
            setOperator (null);
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_UserQueryLine WHERE AD_UserQuery_ID=@AD_UserQuery_ID@
            setValue1Name (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_UserQueryLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=981 */
    public static final int Table_ID=981;
    
    /** TableName=AD_UserQueryLine */
    public static final String Table_Name="AD_UserQueryLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User Query Line.
    @param AD_UserQueryLine_ID Line of the user query */
    public void setAD_UserQueryLine_ID (int AD_UserQueryLine_ID)
    {
        if (AD_UserQueryLine_ID < 1) throw new IllegalArgumentException ("AD_UserQueryLine_ID is mandatory.");
        set_ValueNoCheck ("AD_UserQueryLine_ID", Integer.valueOf(AD_UserQueryLine_ID));
        
    }
    
    /** Get User Query Line.
    @return Line of the user query */
    public int getAD_UserQueryLine_ID() 
    {
        return get_ValueAsInt("AD_UserQueryLine_ID");
        
    }
    
    /** Set User Query.
    @param AD_UserQuery_ID Saved User Query */
    public void setAD_UserQuery_ID (int AD_UserQuery_ID)
    {
        if (AD_UserQuery_ID < 1) throw new IllegalArgumentException ("AD_UserQuery_ID is mandatory.");
        set_ValueNoCheck ("AD_UserQuery_ID", Integer.valueOf(AD_UserQuery_ID));
        
    }
    
    /** Get User Query.
    @return Saved User Query */
    public int getAD_UserQuery_ID() 
    {
        return get_ValueAsInt("AD_UserQuery_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_UserQuery_ID()));
        
    }
    
    /** Set And.
    @param IsAnd Use AND Logic to concatenate lines (not OR) */
    public void setIsAnd (boolean IsAnd)
    {
        set_Value ("IsAnd", Boolean.valueOf(IsAnd));
        
    }
    
    /** Get And.
    @return Use AND Logic to concatenate lines (not OR) */
    public boolean isAnd() 
    {
        return get_ValueAsBoolean("IsAnd");
        
    }
    
    /** Set Key Name.
    @param KeyName Key Name */
    public void setKeyName (String KeyName)
    {
        if (KeyName == null) throw new IllegalArgumentException ("KeyName is mandatory.");
        set_Value ("KeyName", KeyName);
        
    }
    
    /** Get Key Name.
    @return Key Name */
    public String getKeyName() 
    {
        return (String)get_Value("KeyName");
        
    }
    
    /** Set Key Value.
    @param KeyValue Key Value */
    public void setKeyValue (String KeyValue)
    {
        if (KeyValue == null) throw new IllegalArgumentException ("KeyValue is mandatory.");
        set_Value ("KeyValue", KeyValue);
        
    }
    
    /** Get Key Value.
    @return Key Value */
    public String getKeyValue() 
    {
        return (String)get_Value("KeyValue");
        
    }
    
    /** Set Operator.
    @param Operator Operator */
    public void setOperator (String Operator)
    {
        if (Operator == null) throw new IllegalArgumentException ("Operator is mandatory.");
        set_Value ("Operator", Operator);
        
    }
    
    /** Get Operator.
    @return Operator */
    public String getOperator() 
    {
        return (String)get_Value("Operator");
        
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
    
    /** Set Value Name.
    @param Value1Name Value Name */
    public void setValue1Name (String Value1Name)
    {
        if (Value1Name == null) throw new IllegalArgumentException ("Value1Name is mandatory.");
        set_Value ("Value1Name", Value1Name);
        
    }
    
    /** Get Value Name.
    @return Value Name */
    public String getValue1Name() 
    {
        return (String)get_Value("Value1Name");
        
    }
    
    /** Set Value.
    @param Value1Value Value */
    public void setValue1Value (String Value1Value)
    {
        set_Value ("Value1Value", Value1Value);
        
    }
    
    /** Get Value.
    @return Value */
    public String getValue1Value() 
    {
        return (String)get_Value("Value1Value");
        
    }
    
    /** Set Value 2 Name.
    @param Value2Name Value 2 Name */
    public void setValue2Name (String Value2Name)
    {
        set_Value ("Value2Name", Value2Name);
        
    }
    
    /** Get Value 2 Name.
    @return Value 2 Name */
    public String getValue2Name() 
    {
        return (String)get_Value("Value2Name");
        
    }
    
    /** Set Value 2.
    @param Value2Value Value 2 */
    public void setValue2Value (String Value2Value)
    {
        set_Value ("Value2Value", Value2Value);
        
    }
    
    /** Get Value 2.
    @return Value 2 */
    public String getValue2Value() 
    {
        return (String)get_Value("Value2Value");
        
    }
    
    
}
