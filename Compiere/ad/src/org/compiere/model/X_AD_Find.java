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
/** Generated Model for AD_Find
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Find.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Find extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Find_ID id
    @param trx transaction
    */
    public X_AD_Find (Ctx ctx, int AD_Find_ID, Trx trx)
    {
        super (ctx, AD_Find_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Find_ID == 0)
        {
            setAD_Column_ID (0);
            setAD_Find_ID (0);
            setAndOr (null);	// A
            setFind_ID (Env.ZERO);
            setOperation (null);	// ==
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Find (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=404 */
    public static final int Table_ID=404;
    
    /** TableName=AD_Find */
    public static final String Table_Name="AD_Find";
    
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
    
    /** Set Find.
    @param AD_Find_ID Find */
    public void setAD_Find_ID (int AD_Find_ID)
    {
        if (AD_Find_ID < 1) throw new IllegalArgumentException ("AD_Find_ID is mandatory.");
        set_ValueNoCheck ("AD_Find_ID", Integer.valueOf(AD_Find_ID));
        
    }
    
    /** Get Find.
    @return Find */
    public int getAD_Find_ID() 
    {
        return get_ValueAsInt("AD_Find_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Find_ID()));
        
    }
    
    /** And = A */
    public static final String ANDOR_And = X_Ref__AndOr.AND.getValue();
    /** Or = O */
    public static final String ANDOR_Or = X_Ref__AndOr.OR.getValue();
    /** Set And/Or.
    @param AndOr Logical operation: AND or OR */
    public void setAndOr (String AndOr)
    {
        if (AndOr == null) throw new IllegalArgumentException ("AndOr is mandatory");
        if (!X_Ref__AndOr.isValid(AndOr))
        throw new IllegalArgumentException ("AndOr Invalid value - " + AndOr + " - Reference_ID=204 - A - O");
        set_Value ("AndOr", AndOr);
        
    }
    
    /** Get And/Or.
    @return Logical operation: AND or OR */
    public String getAndOr() 
    {
        return (String)get_Value("AndOr");
        
    }
    
    /** Set Find_ID.
    @param Find_ID Find_ID */
    public void setFind_ID (java.math.BigDecimal Find_ID)
    {
        if (Find_ID == null) throw new IllegalArgumentException ("Find_ID is mandatory.");
        set_Value ("Find_ID", Find_ID);
        
    }
    
    /** Get Find_ID.
    @return Find_ID */
    public java.math.BigDecimal getFind_ID() 
    {
        return get_ValueAsBigDecimal("Find_ID");
        
    }
    
    /** != = != */
    public static final String OPERATION_NotEq = X_Ref_AD_Find_Operation.NOT_EQ.getValue();
    /** < = << */
    public static final String OPERATION_Le = X_Ref_AD_Find_Operation.LE.getValue();
    /** <= = <= */
    public static final String OPERATION_LeEq = X_Ref_AD_Find_Operation.LE_EQ.getValue();
    /** = = == */
    public static final String OPERATION_Eq = X_Ref_AD_Find_Operation.EQ.getValue();
    /** >= = >= */
    public static final String OPERATION_GtEq = X_Ref_AD_Find_Operation.GT_EQ.getValue();
    /** > = >> */
    public static final String OPERATION_Gt = X_Ref_AD_Find_Operation.GT.getValue();
    /** |<x>| = AB */
    public static final String OPERATION_X = X_Ref_AD_Find_Operation.X.getValue();
    /** Sql = SQ */
    public static final String OPERATION_Sql = X_Ref_AD_Find_Operation.SQL.getValue();
    /** ~ = ~~ */
    public static final String OPERATION_Like = X_Ref_AD_Find_Operation.LIKE.getValue();
    /** Set Operation.
    @param Operation Compare Operation */
    public void setOperation (String Operation)
    {
        if (Operation == null) throw new IllegalArgumentException ("Operation is mandatory");
        if (!X_Ref_AD_Find_Operation.isValid(Operation))
        throw new IllegalArgumentException ("Operation Invalid value - " + Operation + " - Reference_ID=205 - != - << - <= - == - >= - >> - AB - SQ - ~~");
        set_Value ("Operation", Operation);
        
    }
    
    /** Get Operation.
    @return Compare Operation */
    public String getOperation() 
    {
        return (String)get_Value("Operation");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Value To.
    @param Value2 Value To */
    public void setValue2 (String Value2)
    {
        set_Value ("Value2", Value2);
        
    }
    
    /** Get Value To.
    @return Value To */
    public String getValue2() 
    {
        return (String)get_Value("Value2");
        
    }
    
    
}
