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
/** Generated Model for AD_Ref_Table
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Ref_Table.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Ref_Table extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Ref_Table_ID id
    @param trx transaction
    */
    public X_AD_Ref_Table (Ctx ctx, int AD_Ref_Table_ID, Trx trx)
    {
        super (ctx, AD_Ref_Table_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Ref_Table_ID == 0)
        {
            setAD_Reference_ID (0);
            setAD_Table_ID (0);
            setColumn_Display_ID (0);
            setColumn_Key_ID (0);
            setEntityType (null);	// U
            setIsDisplayIdentifiers (false);	// N
            setIsValueDisplayed (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Ref_Table (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313671789L;
    /** Last Updated Timestamp 2009-03-04 09:39:15.0 */
    public static final long updatedMS = 1236188355000L;
    /** AD_Table_ID=103 */
    public static final int Table_ID=103;
    
    /** TableName=AD_Ref_Table */
    public static final String Table_Name="AD_Ref_Table";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Reference.
    @param AD_Reference_ID System Reference and Validation */
    public void setAD_Reference_ID (int AD_Reference_ID)
    {
        if (AD_Reference_ID < 1) throw new IllegalArgumentException ("AD_Reference_ID is mandatory.");
        set_ValueNoCheck ("AD_Reference_ID", Integer.valueOf(AD_Reference_ID));
        
    }
    
    /** Get Reference.
    @return System Reference and Validation */
    public int getAD_Reference_ID() 
    {
        return get_ValueAsInt("AD_Reference_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Reference_ID()));
        
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
    
    /** Set Display column.
    @param Column_Display_ID Column that will display */
    public void setColumn_Display_ID (int Column_Display_ID)
    {
        if (Column_Display_ID < 1) throw new IllegalArgumentException ("Column_Display_ID is mandatory.");
        set_Value ("Column_Display_ID", Integer.valueOf(Column_Display_ID));
        
    }
    
    /** Get Display column.
    @return Column that will display */
    public int getColumn_Display_ID() 
    {
        return get_ValueAsInt("Column_Display_ID");
        
    }
    
    /** Set Key column.
    @param Column_Key_ID Unique identifier of a record */
    public void setColumn_Key_ID (int Column_Key_ID)
    {
        if (Column_Key_ID < 1) throw new IllegalArgumentException ("Column_Key_ID is mandatory.");
        set_Value ("Column_Key_ID", Integer.valueOf(Column_Key_ID));
        
    }
    
    /** Get Key column.
    @return Unique identifier of a record */
    public int getColumn_Key_ID() 
    {
        return get_ValueAsInt("Column_Key_ID");
        
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
    
    /** Set Display Identifiers.
    @param IsDisplayIdentifiers Display Identifiers */
    public void setIsDisplayIdentifiers (boolean IsDisplayIdentifiers)
    {
        set_Value ("IsDisplayIdentifiers", Boolean.valueOf(IsDisplayIdentifiers));
        
    }
    
    /** Get Display Identifiers.
    @return Display Identifiers */
    public boolean isDisplayIdentifiers() 
    {
        return get_ValueAsBoolean("IsDisplayIdentifiers");
        
    }
    
    /** Set Display Value.
    @param IsValueDisplayed Displays Value column with the Display column */
    public void setIsValueDisplayed (boolean IsValueDisplayed)
    {
        set_Value ("IsValueDisplayed", Boolean.valueOf(IsValueDisplayed));
        
    }
    
    /** Get Display Value.
    @return Displays Value column with the Display column */
    public boolean isValueDisplayed() 
    {
        return get_ValueAsBoolean("IsValueDisplayed");
        
    }
    
    /** Set Sql ORDER BY.
    @param OrderByClause Fully qualified ORDER BY clause */
    public void setOrderByClause (String OrderByClause)
    {
        set_Value ("OrderByClause", OrderByClause);
        
    }
    
    /** Get Sql ORDER BY.
    @return Fully qualified ORDER BY clause */
    public String getOrderByClause() 
    {
        return (String)get_Value("OrderByClause");
        
    }
    
    /** Set Sql WHERE.
    @param WhereClause Fully qualified SQL WHERE clause */
    public void setWhereClause (String WhereClause)
    {
        set_Value ("WhereClause", WhereClause);
        
    }
    
    /** Get Sql WHERE.
    @return Fully qualified SQL WHERE clause */
    public String getWhereClause() 
    {
        return (String)get_Value("WhereClause");
        
    }
    
    
}
