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
/** Generated Model for C_Cash_ExpenseReceiptType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: GenerateModel.java 8757 2010-05-12 21:32:32Z nnayak $ */
public class X_C_Cash_ExpenseReceiptType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Cash_ExpenseReceiptType_ID id
    @param trx transaction
    */
    public X_C_Cash_ExpenseReceiptType (Ctx ctx, int C_Cash_ExpenseReceiptType_ID, Trx trx)
    {
        super (ctx, C_Cash_ExpenseReceiptType_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Cash_ExpenseReceiptType_ID == 0)
        {
            setC_Cash_ExpenseReceiptType_ID (0);
            setIsDefault (false);
            setIsExpense (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Cash_ExpenseReceiptType (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27560569592789L;
    /** Last Updated Timestamp 2010-07-07 00:54:36.0 */
    public static final long updatedMS = 1278444276000L;
    /** AD_Table_ID=2169 */
    public static final int Table_ID=2169;
    
    /** TableName=C_Cash_ExpenseReceiptType */
    public static final String Table_Name="C_Cash_ExpenseReceiptType";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Expense/Receipt Type.
    @param C_Cash_ExpenseReceiptType_ID Expense/Receipt Type */
    public void setC_Cash_ExpenseReceiptType_ID (int C_Cash_ExpenseReceiptType_ID)
    {
        if (C_Cash_ExpenseReceiptType_ID < 1) throw new IllegalArgumentException ("C_Cash_ExpenseReceiptType_ID is mandatory.");
        set_ValueNoCheck ("C_Cash_ExpenseReceiptType_ID", Integer.valueOf(C_Cash_ExpenseReceiptType_ID));
        
    }
    
    /** Get Expense/Receipt Type.
    @return Expense/Receipt Type */
    public int getC_Cash_ExpenseReceiptType_ID() 
    {
        return get_ValueAsInt("C_Cash_ExpenseReceiptType_ID");
        
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
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
    }
    
    /** Set Expense Type.
    @param IsExpense Is this Expense Type */
    public void setIsExpense (boolean IsExpense)
    {
        set_Value ("IsExpense", Boolean.valueOf(IsExpense));
        
    }
    
    /** Get Expense Type.
    @return Is this Expense Type */
    public boolean isExpense() 
    {
        return get_ValueAsBoolean("IsExpense");
        
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
