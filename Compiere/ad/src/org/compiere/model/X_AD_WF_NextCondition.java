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
/** Generated Model for AD_WF_NextCondition
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_WF_NextCondition.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_WF_NextCondition extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_WF_NextCondition_ID id
    @param trx transaction
    */
    public X_AD_WF_NextCondition (Ctx ctx, int AD_WF_NextCondition_ID, Trx trx)
    {
        super (ctx, AD_WF_NextCondition_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_WF_NextCondition_ID == 0)
        {
            setAD_Column_ID (0);
            setAD_WF_NextCondition_ID (0);
            setAD_WF_NodeNext_ID (0);
            setAndOr (null);	// O
            setEntityType (null);	// U
            setOperation (null);
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_WF_NextCondition WHERE AD_WF_NodeNext_ID=@AD_WF_NodeNext_ID@
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_WF_NextCondition (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=706 */
    public static final int Table_ID=706;
    
    /** TableName=AD_WF_NextCondition */
    public static final String Table_Name="AD_WF_NextCondition";
    
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
    
    /** Set Transition Condition.
    @param AD_WF_NextCondition_ID Workflow Node Transition Condition */
    public void setAD_WF_NextCondition_ID (int AD_WF_NextCondition_ID)
    {
        if (AD_WF_NextCondition_ID < 1) throw new IllegalArgumentException ("AD_WF_NextCondition_ID is mandatory.");
        set_ValueNoCheck ("AD_WF_NextCondition_ID", Integer.valueOf(AD_WF_NextCondition_ID));
        
    }
    
    /** Get Transition Condition.
    @return Workflow Node Transition Condition */
    public int getAD_WF_NextCondition_ID() 
    {
        return get_ValueAsInt("AD_WF_NextCondition_ID");
        
    }
    
    /** Set Node Transition.
    @param AD_WF_NodeNext_ID Workflow Node Transition */
    public void setAD_WF_NodeNext_ID (int AD_WF_NodeNext_ID)
    {
        if (AD_WF_NodeNext_ID < 1) throw new IllegalArgumentException ("AD_WF_NodeNext_ID is mandatory.");
        set_ValueNoCheck ("AD_WF_NodeNext_ID", Integer.valueOf(AD_WF_NodeNext_ID));
        
    }
    
    /** Get Node Transition.
    @return Workflow Node Transition */
    public int getAD_WF_NodeNext_ID() 
    {
        return get_ValueAsInt("AD_WF_NodeNext_ID");
        
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
    
    /** Set Entity Type.
    @param EntityType Dictionary Entity Type;
     Determines ownership and synchronization */
    public void setEntityType (String EntityType)
    {
        set_Value ("EntityType", EntityType);
        
    }
    
    /** Get Entity Type.
    @return Dictionary Entity Type;
     Determines ownership and synchronization */
    public String getEntityType() 
    {
        return (String)get_Value("EntityType");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getValue());
        
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
