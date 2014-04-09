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
/** Generated Model for AD_AlertRule
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_AlertRule.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_AlertRule extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_AlertRule_ID id
    @param trx transaction
    */
    public X_AD_AlertRule (Ctx ctx, int AD_AlertRule_ID, Trx trx)
    {
        super (ctx, AD_AlertRule_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_AlertRule_ID == 0)
        {
            setAD_AlertRule_ID (0);
            setAD_Alert_ID (0);
            setFromClause (null);
            setIsValid (true);	// Y
            setName (null);
            setSelectClause (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_AlertRule (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=593 */
    public static final int Table_ID=593;
    
    /** TableName=AD_AlertRule */
    public static final String Table_Name="AD_AlertRule";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Alert Rule.
    @param AD_AlertRule_ID Definition of the alert element */
    public void setAD_AlertRule_ID (int AD_AlertRule_ID)
    {
        if (AD_AlertRule_ID < 1) throw new IllegalArgumentException ("AD_AlertRule_ID is mandatory.");
        set_ValueNoCheck ("AD_AlertRule_ID", Integer.valueOf(AD_AlertRule_ID));
        
    }
    
    /** Get Alert Rule.
    @return Definition of the alert element */
    public int getAD_AlertRule_ID() 
    {
        return get_ValueAsInt("AD_AlertRule_ID");
        
    }
    
    /** Set Alert.
    @param AD_Alert_ID Compiere Alert */
    public void setAD_Alert_ID (int AD_Alert_ID)
    {
        if (AD_Alert_ID < 1) throw new IllegalArgumentException ("AD_Alert_ID is mandatory.");
        set_ValueNoCheck ("AD_Alert_ID", Integer.valueOf(AD_Alert_ID));
        
    }
    
    /** Get Alert.
    @return Compiere Alert */
    public int getAD_Alert_ID() 
    {
        return get_ValueAsInt("AD_Alert_ID");
        
    }
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID <= 0) set_Value ("AD_Table_ID", null);
        else
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set Error Message.
    @param ErrorMsg Error Message */
    public void setErrorMsg (String ErrorMsg)
    {
        set_Value ("ErrorMsg", ErrorMsg);
        
    }
    
    /** Get Error Message.
    @return Error Message */
    public String getErrorMsg() 
    {
        return (String)get_Value("ErrorMsg");
        
    }
    
    /** Set SQL FROM.
    @param FromClause SQL FROM clause */
    public void setFromClause (String FromClause)
    {
        if (FromClause == null) throw new IllegalArgumentException ("FromClause is mandatory.");
        set_Value ("FromClause", FromClause);
        
    }
    
    /** Get SQL FROM.
    @return SQL FROM clause */
    public String getFromClause() 
    {
        return (String)get_Value("FromClause");
        
    }
    
    /** Set Valid.
    @param IsValid Element is valid */
    public void setIsValid (boolean IsValid)
    {
        set_Value ("IsValid", Boolean.valueOf(IsValid));
        
    }
    
    /** Get Valid.
    @return Element is valid */
    public boolean isValid() 
    {
        return get_ValueAsBoolean("IsValid");
        
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
    
    /** Set Post Processing.
    @param PostProcessing Process SQL after executing the query */
    public void setPostProcessing (String PostProcessing)
    {
        set_Value ("PostProcessing", PostProcessing);
        
    }
    
    /** Get Post Processing.
    @return Process SQL after executing the query */
    public String getPostProcessing() 
    {
        return (String)get_Value("PostProcessing");
        
    }
    
    /** Set Pre Processing.
    @param PreProcessing Process SQL before executing the query */
    public void setPreProcessing (String PreProcessing)
    {
        set_Value ("PreProcessing", PreProcessing);
        
    }
    
    /** Get Pre Processing.
    @return Process SQL before executing the query */
    public String getPreProcessing() 
    {
        return (String)get_Value("PreProcessing");
        
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
        set_Value ("WhereClause", WhereClause);
        
    }
    
    /** Get Sql WHERE.
    @return Fully qualified SQL WHERE clause */
    public String getWhereClause() 
    {
        return (String)get_Value("WhereClause");
        
    }
    
    
}
