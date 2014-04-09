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
/** Generated Model for M_Warehouse_Acct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_Warehouse_Acct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_Warehouse_Acct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_Warehouse_Acct_ID id
    @param trx transaction
    */
    public X_M_Warehouse_Acct (Ctx ctx, int M_Warehouse_Acct_ID, Trx trx)
    {
        super (ctx, M_Warehouse_Acct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Warehouse_Acct_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setM_Warehouse_ID (0);
            setW_Differences_Acct (0);
            setW_InvActualAdjust_Acct (0);
            setW_Inventory_Acct (0);
            setW_Revaluation_Acct (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_Warehouse_Acct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=191 */
    public static final int Table_ID=191;
    
    /** TableName=M_Warehouse_Acct */
    public static final String Table_Name="M_Warehouse_Acct";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
        set_ValueNoCheck ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_AcctSchema_ID()));
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_ValueNoCheck ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Warehouse Differences.
    @param W_Differences_Acct Warehouse Differences Account */
    public void setW_Differences_Acct (int W_Differences_Acct)
    {
        set_Value ("W_Differences_Acct", Integer.valueOf(W_Differences_Acct));
        
    }
    
    /** Get Warehouse Differences.
    @return Warehouse Differences Account */
    public int getW_Differences_Acct() 
    {
        return get_ValueAsInt("W_Differences_Acct");
        
    }
    
    /** Set Inventory Adjustment.
    @param W_InvActualAdjust_Acct Account for Inventory value adjustments for Actual Costing */
    public void setW_InvActualAdjust_Acct (int W_InvActualAdjust_Acct)
    {
        set_Value ("W_InvActualAdjust_Acct", Integer.valueOf(W_InvActualAdjust_Acct));
        
    }
    
    /** Get Inventory Adjustment.
    @return Account for Inventory value adjustments for Actual Costing */
    public int getW_InvActualAdjust_Acct() 
    {
        return get_ValueAsInt("W_InvActualAdjust_Acct");
        
    }
    
    /** Set (Not Used).
    @param W_Inventory_Acct Warehouse Inventory Asset Account - Currently not used */
    public void setW_Inventory_Acct (int W_Inventory_Acct)
    {
        set_Value ("W_Inventory_Acct", Integer.valueOf(W_Inventory_Acct));
        
    }
    
    /** Get (Not Used).
    @return Warehouse Inventory Asset Account - Currently not used */
    public int getW_Inventory_Acct() 
    {
        return get_ValueAsInt("W_Inventory_Acct");
        
    }
    
    /** Set Inventory Revaluation.
    @param W_Revaluation_Acct Account for Inventory Revaluation */
    public void setW_Revaluation_Acct (int W_Revaluation_Acct)
    {
        set_Value ("W_Revaluation_Acct", Integer.valueOf(W_Revaluation_Acct));
        
    }
    
    /** Get Inventory Revaluation.
    @return Account for Inventory Revaluation */
    public int getW_Revaluation_Acct() 
    {
        return get_ValueAsInt("W_Revaluation_Acct");
        
    }
    
    
}
