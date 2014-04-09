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
/** Generated Model for AD_UserPreference
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_UserPreference.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_UserPreference extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_UserPreference_ID id
    @param trx transaction
    */
    public X_AD_UserPreference (Ctx ctx, int AD_UserPreference_ID, Trx trx)
    {
        super (ctx, AD_UserPreference_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_UserPreference_ID == 0)
        {
            setAD_UserPreference_ID (0);
            setAD_User_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_UserPreference (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=989 */
    public static final int Table_ID=989;
    
    /** TableName=AD_UserPreference */
    public static final String Table_Name="AD_UserPreference";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Role.
    @param AD_Role_ID Responsibility Role */
    public void setAD_Role_ID (int AD_Role_ID)
    {
        if (AD_Role_ID <= 0) set_Value ("AD_Role_ID", null);
        else
        set_Value ("AD_Role_ID", Integer.valueOf(AD_Role_ID));
        
    }
    
    /** Get Role.
    @return Responsibility Role */
    public int getAD_Role_ID() 
    {
        return get_ValueAsInt("AD_Role_ID");
        
    }
    
    /** Set Session.
    @param AD_Session_ID User Session Online or Web */
    public void setAD_Session_ID (int AD_Session_ID)
    {
        if (AD_Session_ID <= 0) set_Value ("AD_Session_ID", null);
        else
        set_Value ("AD_Session_ID", Integer.valueOf(AD_Session_ID));
        
    }
    
    /** Get Session.
    @return User Session Online or Web */
    public int getAD_Session_ID() 
    {
        return get_ValueAsInt("AD_Session_ID");
        
    }
    
    /** Set User Preference.
    @param AD_UserPreference_ID Application User Preferences */
    public void setAD_UserPreference_ID (int AD_UserPreference_ID)
    {
        if (AD_UserPreference_ID < 1) throw new IllegalArgumentException ("AD_UserPreference_ID is mandatory.");
        set_ValueNoCheck ("AD_UserPreference_ID", Integer.valueOf(AD_UserPreference_ID));
        
    }
    
    /** Get User Preference.
    @return Application User Preferences */
    public int getAD_UserPreference_ID() 
    {
        return get_ValueAsInt("AD_UserPreference_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_ValueNoCheck ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Auto Commit.
    @param IsAutoCommit Automatically save changes */
    public void setIsAutoCommit (boolean IsAutoCommit)
    {
        set_Value ("IsAutoCommit", Boolean.valueOf(IsAutoCommit));
        
    }
    
    /** Get Auto Commit.
    @return Automatically save changes */
    public boolean isAutoCommit() 
    {
        return get_ValueAsBoolean("IsAutoCommit");
        
    }
    
    /** Set Show Accounting.
    @param IsShowAcct Users with this role can see accounting information */
    public void setIsShowAcct (boolean IsShowAcct)
    {
        set_Value ("IsShowAcct", Boolean.valueOf(IsShowAcct));
        
    }
    
    /** Get Show Accounting.
    @return Users with this role can see accounting information */
    public boolean isShowAcct() 
    {
        return get_ValueAsBoolean("IsShowAcct");
        
    }
    
    /** Set Show Advanced.
    @param IsShowAdvanced Show Advanced Tabs */
    public void setIsShowAdvanced (boolean IsShowAdvanced)
    {
        set_Value ("IsShowAdvanced", Boolean.valueOf(IsShowAdvanced));
        
    }
    
    /** Get Show Advanced.
    @return Show Advanced Tabs */
    public boolean isShowAdvanced() 
    {
        return get_ValueAsBoolean("IsShowAdvanced");
        
    }
    
    /** Set Show Translation.
    @param IsShowTrl Show Translation Tabs */
    public void setIsShowTrl (boolean IsShowTrl)
    {
        set_Value ("IsShowTrl", Boolean.valueOf(IsShowTrl));
        
    }
    
    /** Get Show Translation.
    @return Show Translation Tabs */
    public boolean isShowTrl() 
    {
        return get_ValueAsBoolean("IsShowTrl");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Printer Name.
    @param PrinterName Name of the Printer */
    public void setPrinterName (String PrinterName)
    {
        set_Value ("PrinterName", PrinterName);
        
    }
    
    /** Get Printer Name.
    @return Name of the Printer */
    public String getPrinterName() 
    {
        return (String)get_Value("PrinterName");
        
    }
    
    /** Set No Table Rows.
    @param TableRows Number of Table Rows displayed */
    public void setTableRows (int TableRows)
    {
        set_Value ("TableRows", Integer.valueOf(TableRows));
        
    }
    
    /** Get No Table Rows.
    @return Number of Table Rows displayed */
    public int getTableRows() 
    {
        return get_ValueAsInt("TableRows");
        
    }
    
    /** Set UI Theme.
    @param UITheme User Interface Theme */
    public void setUITheme (String UITheme)
    {
        set_Value ("UITheme", UITheme);
        
    }
    
    /** Get UI Theme.
    @return User Interface Theme */
    public String getUITheme() 
    {
        return (String)get_Value("UITheme");
        
    }
    
    
}
