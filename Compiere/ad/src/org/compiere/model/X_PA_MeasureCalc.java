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
/** Generated Model for PA_MeasureCalc
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_MeasureCalc.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_MeasureCalc extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_MeasureCalc_ID id
    @param trx transaction
    */
    public X_PA_MeasureCalc (Ctx ctx, int PA_MeasureCalc_ID, Trx trx)
    {
        super (ctx, PA_MeasureCalc_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_MeasureCalc_ID == 0)
        {
            setAD_Table_ID (0);
            setDateColumn (null);	// x.Date
            setEntityType (null);	// U
            setKeyColumn (null);
            setName (null);
            setPA_MeasureCalc_ID (0);
            setSelectClause (null);	// SELECT ... FROM ...
            setWhereClause (null);	// WHERE ...
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_MeasureCalc (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=442 */
    public static final int Table_ID=442;
    
    /** TableName=PA_MeasureCalc */
    public static final String Table_Name="PA_MeasureCalc";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set B.Partner Column.
    @param BPartnerColumn Fully qualified Business Partner key column (C_BPartner_ID) */
    public void setBPartnerColumn (String BPartnerColumn)
    {
        set_Value ("BPartnerColumn", BPartnerColumn);
        
    }
    
    /** Get B.Partner Column.
    @return Fully qualified Business Partner key column (C_BPartner_ID) */
    public String getBPartnerColumn() 
    {
        return (String)get_Value("BPartnerColumn");
        
    }
    
    /** Set Date Column.
    @param DateColumn Fully qualified date column */
    public void setDateColumn (String DateColumn)
    {
        if (DateColumn == null) throw new IllegalArgumentException ("DateColumn is mandatory.");
        set_Value ("DateColumn", DateColumn);
        
    }
    
    /** Get Date Column.
    @return Fully qualified date column */
    public String getDateColumn() 
    {
        return (String)get_Value("DateColumn");
        
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
    
    /** Set Key Column.
    @param KeyColumn Key Column for Table */
    public void setKeyColumn (String KeyColumn)
    {
        if (KeyColumn == null) throw new IllegalArgumentException ("KeyColumn is mandatory.");
        set_Value ("KeyColumn", KeyColumn);
        
    }
    
    /** Get Key Column.
    @return Key Column for Table */
    public String getKeyColumn() 
    {
        return (String)get_Value("KeyColumn");
        
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
    
    /** Set Org Column.
    @param OrgColumn Fully qualified Organization column (AD_Org_ID) */
    public void setOrgColumn (String OrgColumn)
    {
        set_Value ("OrgColumn", OrgColumn);
        
    }
    
    /** Get Org Column.
    @return Fully qualified Organization column (AD_Org_ID) */
    public String getOrgColumn() 
    {
        return (String)get_Value("OrgColumn");
        
    }
    
    /** Set Measure Calculation.
    @param PA_MeasureCalc_ID Calculation method for measuring performance */
    public void setPA_MeasureCalc_ID (int PA_MeasureCalc_ID)
    {
        if (PA_MeasureCalc_ID < 1) throw new IllegalArgumentException ("PA_MeasureCalc_ID is mandatory.");
        set_ValueNoCheck ("PA_MeasureCalc_ID", Integer.valueOf(PA_MeasureCalc_ID));
        
    }
    
    /** Get Measure Calculation.
    @return Calculation method for measuring performance */
    public int getPA_MeasureCalc_ID() 
    {
        return get_ValueAsInt("PA_MeasureCalc_ID");
        
    }
    
    /** Set Product Column.
    @param ProductColumn Fully qualified Product column (M_Product_ID) */
    public void setProductColumn (String ProductColumn)
    {
        set_Value ("ProductColumn", ProductColumn);
        
    }
    
    /** Get Product Column.
    @return Fully qualified Product column (M_Product_ID) */
    public String getProductColumn() 
    {
        return (String)get_Value("ProductColumn");
        
    }
    
    /** Set Sql SELECT.
    @param SelectClause SQL SELECT clause */
    public void setSelectClause (String SelectClause)
    {
        if (SelectClause == null) throw new IllegalArgumentException ("SelectClause is mandatory.");
        set_Value ("SelectClause", SelectClause);
        
    }
    
    /** Get Sql SELECT.
    @return SQL SELECT clause */
    public String getSelectClause() 
    {
        return (String)get_Value("SelectClause");
        
    }
    
    /** Set Sql WHERE.
    @param WhereClause Fully qualified SQL WHERE clause */
    public void setWhereClause (String WhereClause)
    {
        if (WhereClause == null) throw new IllegalArgumentException ("WhereClause is mandatory.");
        set_Value ("WhereClause", WhereClause);
        
    }
    
    /** Get Sql WHERE.
    @return Fully qualified SQL WHERE clause */
    public String getWhereClause() 
    {
        return (String)get_Value("WhereClause");
        
    }
    
    
}
