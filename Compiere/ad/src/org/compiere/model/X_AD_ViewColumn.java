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
/** Generated Model for AD_ViewColumn
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ViewColumn.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ViewColumn extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ViewColumn_ID id
    @param trx transaction
    */
    public X_AD_ViewColumn (Ctx ctx, int AD_ViewColumn_ID, Trx trx)
    {
        super (ctx, AD_ViewColumn_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ViewColumn_ID == 0)
        {
            setAD_ViewColumn_ID (0);
            setAD_ViewComponent_ID (0);
            setColumnName (null);
            setEntityType (null);	// U
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ViewColumn (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313910789L;
    /** Last Updated Timestamp 2009-03-04 09:43:14.0 */
    public static final long updatedMS = 1236188594000L;
    /** AD_Table_ID=935 */
    public static final int Table_ID=935;
    
    /** TableName=AD_ViewColumn */
    public static final String Table_Name="AD_ViewColumn";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set View Column.
    @param AD_ViewColumn_ID Select column in View */
    public void setAD_ViewColumn_ID (int AD_ViewColumn_ID)
    {
        if (AD_ViewColumn_ID < 1) throw new IllegalArgumentException ("AD_ViewColumn_ID is mandatory.");
        set_ValueNoCheck ("AD_ViewColumn_ID", Integer.valueOf(AD_ViewColumn_ID));
        
    }
    
    /** Get View Column.
    @return Select column in View */
    public int getAD_ViewColumn_ID() 
    {
        return get_ValueAsInt("AD_ViewColumn_ID");
        
    }
    
    /** Set View Component.
    @param AD_ViewComponent_ID Component (Select statement) of the view */
    public void setAD_ViewComponent_ID (int AD_ViewComponent_ID)
    {
        if (AD_ViewComponent_ID < 1) throw new IllegalArgumentException ("AD_ViewComponent_ID is mandatory.");
        set_ValueNoCheck ("AD_ViewComponent_ID", Integer.valueOf(AD_ViewComponent_ID));
        
    }
    
    /** Get View Component.
    @return Component (Select statement) of the view */
    public int getAD_ViewComponent_ID() 
    {
        return get_ValueAsInt("AD_ViewComponent_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_ViewComponent_ID()));
        
    }
    
    /** Set DB Column Name.
    @param ColumnName Name of the column in the database */
    public void setColumnName (String ColumnName)
    {
        if (ColumnName == null) throw new IllegalArgumentException ("ColumnName is mandatory.");
        set_Value ("ColumnName", ColumnName);
        
    }
    
    /** Get DB Column Name.
    @return Name of the column in the database */
    public String getColumnName() 
    {
        return (String)get_Value("ColumnName");
        
    }
    
    /** Set Column SQL.
    @param ColumnSQL Virtual Column (r/o) */
    public void setColumnSQL (String ColumnSQL)
    {
        set_Value ("ColumnSQL", ColumnSQL);
        
    }
    
    /** Get Column SQL.
    @return Virtual Column (r/o) */
    public String getColumnSQL() 
    {
        return (String)get_Value("ColumnSQL");
        
    }
    
    /** Binary LOB = B */
    public static final String DBDATATYPE_BinaryLOB = X_Ref_AD_Element_Data_Type.BINARY_LOB.getValue();
    /** Character Fixed = C */
    public static final String DBDATATYPE_CharacterFixed = X_Ref_AD_Element_Data_Type.CHARACTER_FIXED.getValue();
    /** Decimal = D */
    public static final String DBDATATYPE_Decimal = X_Ref_AD_Element_Data_Type.DECIMAL.getValue();
    /** Integer = I */
    public static final String DBDATATYPE_Integer = X_Ref_AD_Element_Data_Type.INTEGER.getValue();
    /** Character LOB = L */
    public static final String DBDATATYPE_CharacterLOB = X_Ref_AD_Element_Data_Type.CHARACTER_LOB.getValue();
    /** Number = N */
    public static final String DBDATATYPE_Number = X_Ref_AD_Element_Data_Type.NUMBER.getValue();
    /** Timestamp = T */
    public static final String DBDATATYPE_Timestamp = X_Ref_AD_Element_Data_Type.TIMESTAMP.getValue();
    /** Character Variable = V */
    public static final String DBDATATYPE_CharacterVariable = X_Ref_AD_Element_Data_Type.CHARACTER_VARIABLE.getValue();
    /** Set Data Type.
    @param DBDataType Database Data Type */
    public void setDBDataType (String DBDataType)
    {
        if (!X_Ref_AD_Element_Data_Type.isValid(DBDataType))
        throw new IllegalArgumentException ("DBDataType Invalid value - " + DBDataType + " - Reference_ID=422 - B - C - D - I - L - N - T - V");
        set_Value ("DBDataType", DBDataType);
        
    }
    
    /** Get Data Type.
    @return Database Data Type */
    public String getDBDataType() 
    {
        return (String)get_Value("DBDataType");
        
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
    
    
}
