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
/** Generated Model for AD_AssignCriteria
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_AssignCriteria.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_AssignCriteria extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_AssignCriteria_ID id
    @param trx transaction
    */
    public X_AD_AssignCriteria (Ctx ctx, int AD_AssignCriteria_ID, Trx trx)
    {
        super (ctx, AD_AssignCriteria_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_AssignCriteria_ID == 0)
        {
            setAD_AssignCriteria_ID (0);
            setAD_AssignTarget_ID (0);
            setAD_SourceColumn_ID (0);
            setAndOr (null);	// A
            setOperation (null);
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_AssignCriteria WHERE AD_AssignTarget_ID=@AD_AssignTarget_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_AssignCriteria (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27502178583789L;
    /** Last Updated Timestamp 2008-08-29 16:41:07.0 */
    public static final long updatedMS = 1220053267000L;
    /** AD_Table_ID=932 */
    public static final int Table_ID=932;
    
    /** TableName=AD_AssignCriteria */
    public static final String Table_Name="AD_AssignCriteria";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Assign Criteria.
    @param AD_AssignCriteria_ID Auto assignment Criteria */
    public void setAD_AssignCriteria_ID (int AD_AssignCriteria_ID)
    {
        if (AD_AssignCriteria_ID < 1) throw new IllegalArgumentException ("AD_AssignCriteria_ID is mandatory.");
        set_ValueNoCheck ("AD_AssignCriteria_ID", Integer.valueOf(AD_AssignCriteria_ID));
        
    }
    
    /** Get Assign Criteria.
    @return Auto assignment Criteria */
    public int getAD_AssignCriteria_ID() 
    {
        return get_ValueAsInt("AD_AssignCriteria_ID");
        
    }
    
    /** Set Assign Target.
    @param AD_AssignTarget_ID Automatic Assignment Target Column */
    public void setAD_AssignTarget_ID (int AD_AssignTarget_ID)
    {
        if (AD_AssignTarget_ID < 1) throw new IllegalArgumentException ("AD_AssignTarget_ID is mandatory.");
        set_ValueNoCheck ("AD_AssignTarget_ID", Integer.valueOf(AD_AssignTarget_ID));
        
    }
    
    /** Get Assign Target.
    @return Automatic Assignment Target Column */
    public int getAD_AssignTarget_ID() 
    {
        return get_ValueAsInt("AD_AssignTarget_ID");
        
    }
    
    /** Set Source Column.
    @param AD_SourceColumn_ID The column used as the criteria */
    public void setAD_SourceColumn_ID (int AD_SourceColumn_ID)
    {
        if (AD_SourceColumn_ID < 1) throw new IllegalArgumentException ("AD_SourceColumn_ID is mandatory.");
        set_Value ("AD_SourceColumn_ID", Integer.valueOf(AD_SourceColumn_ID));
        
    }
    
    /** Get Source Column.
    @return The column used as the criteria */
    public int getAD_SourceColumn_ID() 
    {
        return get_ValueAsInt("AD_SourceColumn_ID");
        
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
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID <= 0) set_Value ("Record_ID", null);
        else
        set_Value ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
        
    }
    
    /** Set Value.
    @param ValueString Value as String */
    public void setValueString (String ValueString)
    {
        set_Value ("ValueString", ValueString);
        
    }
    
    /** Get Value.
    @return Value as String */
    public String getValueString() 
    {
        return (String)get_Value("ValueString");
        
    }
    
    
}
