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
/** Generated Model for CM_CStageTTable
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_CStageTTable.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_CStageTTable extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_CStageTTable_ID id
    @param trx transaction
    */
    public X_CM_CStageTTable (Ctx ctx, int CM_CStageTTable_ID, Trx trx)
    {
        super (ctx, CM_CStageTTable_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_CStageTTable_ID == 0)
        {
            setCM_CStageTTable_ID (0);
            setCM_CStage_ID (0);
            setCM_TemplateTable_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_CStageTTable (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=881 */
    public static final int Table_ID=881;
    
    /** TableName=CM_CStageTTable */
    public static final String Table_Name="CM_CStageTTable";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Stage T.Table.
    @param CM_CStageTTable_ID Container Stage Template Table */
    public void setCM_CStageTTable_ID (int CM_CStageTTable_ID)
    {
        if (CM_CStageTTable_ID < 1) throw new IllegalArgumentException ("CM_CStageTTable_ID is mandatory.");
        set_ValueNoCheck ("CM_CStageTTable_ID", Integer.valueOf(CM_CStageTTable_ID));
        
    }
    
    /** Get Stage T.Table.
    @return Container Stage Template Table */
    public int getCM_CStageTTable_ID() 
    {
        return get_ValueAsInt("CM_CStageTTable_ID");
        
    }
    
    /** Set Web Container Stage.
    @param CM_CStage_ID Web Container Stage contains the staging content like images, text etc. */
    public void setCM_CStage_ID (int CM_CStage_ID)
    {
        if (CM_CStage_ID < 1) throw new IllegalArgumentException ("CM_CStage_ID is mandatory.");
        set_ValueNoCheck ("CM_CStage_ID", Integer.valueOf(CM_CStage_ID));
        
    }
    
    /** Get Web Container Stage.
    @return Web Container Stage contains the staging content like images, text etc. */
    public int getCM_CStage_ID() 
    {
        return get_ValueAsInt("CM_CStage_ID");
        
    }
    
    /** Set Template Table.
    @param CM_TemplateTable_ID CM Template Table Link */
    public void setCM_TemplateTable_ID (int CM_TemplateTable_ID)
    {
        if (CM_TemplateTable_ID < 1) throw new IllegalArgumentException ("CM_TemplateTable_ID is mandatory.");
        set_ValueNoCheck ("CM_TemplateTable_ID", Integer.valueOf(CM_TemplateTable_ID));
        
    }
    
    /** Get Template Table.
    @return CM Template Table Link */
    public int getCM_TemplateTable_ID() 
    {
        return get_ValueAsInt("CM_TemplateTable_ID");
        
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
    
    /** Set Other SQL Clause.
    @param OtherClause Other SQL Clause */
    public void setOtherClause (String OtherClause)
    {
        set_Value ("OtherClause", OtherClause);
        
    }
    
    /** Get Other SQL Clause.
    @return Other SQL Clause */
    public String getOtherClause() 
    {
        return (String)get_Value("OtherClause");
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID <= 0) set_ValueNoCheck ("Record_ID", null);
        else
        set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
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
