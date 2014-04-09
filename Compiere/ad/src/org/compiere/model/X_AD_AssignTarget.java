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
/** Generated Model for AD_AssignTarget
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_AssignTarget.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_AssignTarget extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_AssignTarget_ID id
    @param trx transaction
    */
    public X_AD_AssignTarget (Ctx ctx, int AD_AssignTarget_ID, Trx trx)
    {
        super (ctx, AD_AssignTarget_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_AssignTarget_ID == 0)
        {
            setAD_AssignSet_ID (0);
            setAD_AssignTarget_ID (0);
            setAD_TargetColumn_ID (0);
            setAssignRule (null);	// A
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_AssignTarget (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27502502579789L;
    /** Last Updated Timestamp 2008-09-02 10:41:03.0 */
    public static final long updatedMS = 1220377263000L;
    /** AD_Table_ID=931 */
    public static final int Table_ID=931;
    
    /** TableName=AD_AssignTarget */
    public static final String Table_Name="AD_AssignTarget";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Auto Assignment.
    @param AD_AssignSet_ID Automatic Assignment of values */
    public void setAD_AssignSet_ID (int AD_AssignSet_ID)
    {
        if (AD_AssignSet_ID < 1) throw new IllegalArgumentException ("AD_AssignSet_ID is mandatory.");
        set_ValueNoCheck ("AD_AssignSet_ID", Integer.valueOf(AD_AssignSet_ID));
        
    }
    
    /** Get Auto Assignment.
    @return Automatic Assignment of values */
    public int getAD_AssignSet_ID() 
    {
        return get_ValueAsInt("AD_AssignSet_ID");
        
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
    
    /** Set Target Column.
    @param AD_TargetColumn_ID Target column to be set */
    public void setAD_TargetColumn_ID (int AD_TargetColumn_ID)
    {
        if (AD_TargetColumn_ID < 1) throw new IllegalArgumentException ("AD_TargetColumn_ID is mandatory.");
        set_Value ("AD_TargetColumn_ID", Integer.valueOf(AD_TargetColumn_ID));
        
    }
    
    /** Get Target Column.
    @return Target column to be set */
    public int getAD_TargetColumn_ID() 
    {
        return get_ValueAsInt("AD_TargetColumn_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_TargetColumn_ID()));
        
    }
    
    /** Always = A */
    public static final String ASSIGNRULE_Always = X_Ref_AD_AssignTarget_Rule.ALWAYS.getValue();
    /** Only if NULL = N */
    public static final String ASSIGNRULE_OnlyIfNULL = X_Ref_AD_AssignTarget_Rule.ONLY_IF_NULL.getValue();
    /** Only if NOT NULL = X */
    public static final String ASSIGNRULE_OnlyIfNOTNULL = X_Ref_AD_AssignTarget_Rule.ONLY_IF_NOTNULL.getValue();
    /** Set Assignment Rule.
    @param AssignRule Assignment Rule */
    public void setAssignRule (String AssignRule)
    {
        if (AssignRule == null) throw new IllegalArgumentException ("AssignRule is mandatory");
        if (!X_Ref_AD_AssignTarget_Rule.isValid(AssignRule))
        throw new IllegalArgumentException ("AssignRule Invalid value - " + AssignRule + " - Reference_ID=425 - A - N - X");
        set_Value ("AssignRule", AssignRule);
        
    }
    
    /** Get Assignment Rule.
    @return Assignment Rule */
    public String getAssignRule() 
    {
        return (String)get_Value("AssignRule");
        
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
    
    /** Set Value SQL.
    @param ValueSQL SQL to determine the value */
    public void setValueSQL (String ValueSQL)
    {
        set_Value ("ValueSQL", ValueSQL);
        
    }
    
    /** Get Value SQL.
    @return SQL to determine the value */
    public String getValueSQL() 
    {
        return (String)get_Value("ValueSQL");
        
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
