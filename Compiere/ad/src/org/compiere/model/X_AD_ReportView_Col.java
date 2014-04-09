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
/** Generated Model for AD_ReportView_Col
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ReportView_Col.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ReportView_Col extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ReportView_Col_ID id
    @param trx transaction
    */
    public X_AD_ReportView_Col (Ctx ctx, int AD_ReportView_Col_ID, Trx trx)
    {
        super (ctx, AD_ReportView_Col_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ReportView_Col_ID == 0)
        {
            setAD_ReportView_Col_ID (0);
            setAD_ReportView_ID (0);
            setFunctionColumn (null);
            setIsGroupFunction (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ReportView_Col (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=428 */
    public static final int Table_ID=428;
    
    /** TableName=AD_ReportView_Col */
    public static final String Table_Name="AD_ReportView_Col";
    
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
        if (AD_Column_ID <= 0) set_Value ("AD_Column_ID", null);
        else
        set_Value ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
    }
    
    /** Set Report view Column.
    @param AD_ReportView_Col_ID Report view Column */
    public void setAD_ReportView_Col_ID (int AD_ReportView_Col_ID)
    {
        if (AD_ReportView_Col_ID < 1) throw new IllegalArgumentException ("AD_ReportView_Col_ID is mandatory.");
        set_ValueNoCheck ("AD_ReportView_Col_ID", Integer.valueOf(AD_ReportView_Col_ID));
        
    }
    
    /** Get Report view Column.
    @return Report view Column */
    public int getAD_ReportView_Col_ID() 
    {
        return get_ValueAsInt("AD_ReportView_Col_ID");
        
    }
    
    /** Set Report View.
    @param AD_ReportView_ID View used to generate this report */
    public void setAD_ReportView_ID (int AD_ReportView_ID)
    {
        if (AD_ReportView_ID < 1) throw new IllegalArgumentException ("AD_ReportView_ID is mandatory.");
        set_ValueNoCheck ("AD_ReportView_ID", Integer.valueOf(AD_ReportView_ID));
        
    }
    
    /** Get Report View.
    @return View used to generate this report */
    public int getAD_ReportView_ID() 
    {
        return get_ValueAsInt("AD_ReportView_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_ReportView_ID()));
        
    }
    
    /** Set Function Column.
    @param FunctionColumn Overwrite Column with Function */
    public void setFunctionColumn (String FunctionColumn)
    {
        if (FunctionColumn == null) throw new IllegalArgumentException ("FunctionColumn is mandatory.");
        set_Value ("FunctionColumn", FunctionColumn);
        
    }
    
    /** Get Function Column.
    @return Overwrite Column with Function */
    public String getFunctionColumn() 
    {
        return (String)get_Value("FunctionColumn");
        
    }
    
    /** Set SQL Group Function.
    @param IsGroupFunction This function will generate a Group By Clause */
    public void setIsGroupFunction (boolean IsGroupFunction)
    {
        set_Value ("IsGroupFunction", Boolean.valueOf(IsGroupFunction));
        
    }
    
    /** Get SQL Group Function.
    @return This function will generate a Group By Clause */
    public boolean isGroupFunction() 
    {
        return get_ValueAsBoolean("IsGroupFunction");
        
    }
    
    
}
