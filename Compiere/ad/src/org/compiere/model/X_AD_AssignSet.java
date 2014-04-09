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
/** Generated Model for AD_AssignSet
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_AssignSet.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_AssignSet extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_AssignSet_ID id
    @param trx transaction
    */
    public X_AD_AssignSet (Ctx ctx, int AD_AssignSet_ID, Trx trx)
    {
        super (ctx, AD_AssignSet_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_AssignSet_ID == 0)
        {
            setAD_AssignSet_ID (0);
            setAD_Table_ID (0);
            setAutoAssignRule (null);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_AssignSet (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=930 */
    public static final int Table_ID=930;
    
    /** TableName=AD_AssignSet */
    public static final String Table_Name="AD_AssignSet";
    
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
    
    /** Create and Update = B */
    public static final String AUTOASSIGNRULE_CreateAndUpdate = X_Ref_AD_AssignSet_Rule.CREATE_AND_UPDATE.getValue();
    /** Create only = C */
    public static final String AUTOASSIGNRULE_CreateOnly = X_Ref_AD_AssignSet_Rule.CREATE_ONLY.getValue();
    /** Create and Update if not Processed = P */
    public static final String AUTOASSIGNRULE_CreateAndUpdateIfNotProcessed = X_Ref_AD_AssignSet_Rule.CREATE_AND_UPDATE_IF_NOT_PROCESSED.getValue();
    /** Update if not Processed = Q */
    public static final String AUTOASSIGNRULE_UpdateIfNotProcessed = X_Ref_AD_AssignSet_Rule.UPDATE_IF_NOT_PROCESSED.getValue();
    /** Update only = U */
    public static final String AUTOASSIGNRULE_UpdateOnly = X_Ref_AD_AssignSet_Rule.UPDATE_ONLY.getValue();
    /** Set Auto Assign Rule.
    @param AutoAssignRule Timing of automatic assignment */
    public void setAutoAssignRule (String AutoAssignRule)
    {
        if (AutoAssignRule == null) throw new IllegalArgumentException ("AutoAssignRule is mandatory");
        if (!X_Ref_AD_AssignSet_Rule.isValid(AutoAssignRule))
        throw new IllegalArgumentException ("AutoAssignRule Invalid value - " + AutoAssignRule + " - Reference_ID=424 - B - C - P - Q - U");
        set_Value ("AutoAssignRule", AutoAssignRule);
        
    }
    
    /** Get Auto Assign Rule.
    @return Timing of automatic assignment */
    public String getAutoAssignRule() 
    {
        return (String)get_Value("AutoAssignRule");
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
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
    
    
}
