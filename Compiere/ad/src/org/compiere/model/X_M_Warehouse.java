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
/** Generated Model for M_Warehouse
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.2 Dev - $Id: X_M_Warehouse.java 8397 2010-02-05 07:04:22Z ragrawal $ */
public class X_M_Warehouse extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_Warehouse_ID id
    @param trx transaction
    */
    public X_M_Warehouse (Ctx ctx, int M_Warehouse_ID, Trx trx)
    {
        super (ctx, M_Warehouse_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Warehouse_ID == 0)
        {
            setC_Location_ID (0);
            setIsDisallowNegativeInv (false);	// N
            setIsWMSEnabled (false);	// N
            setM_Warehouse_ID (0);
            setName (null);
            setSeparator (null);	// *
            setValue (null);
            setWorkingDays (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_Warehouse (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27547430690789L;
    /** Last Updated Timestamp 2010-02-04 23:12:54.0 */
    public static final long updatedMS = 1265305374000L;
    /** AD_Table_ID=190 */
    public static final int Table_ID=190;
    
    /** TableName=M_Warehouse */
    public static final String Table_Name="M_Warehouse";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Address.
    @param C_Location_ID Location or Address */
    public void setC_Location_ID (int C_Location_ID)
    {
        if (C_Location_ID < 1) throw new IllegalArgumentException ("C_Location_ID is mandatory.");
        set_Value ("C_Location_ID", Integer.valueOf(C_Location_ID));
        
    }
    
    /** Get Address.
    @return Location or Address */
    public int getC_Location_ID() 
    {
        return get_ValueAsInt("C_Location_ID");
        
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
    
    /** Set Disallow Negative Inventory.
    @param IsDisallowNegativeInv Negative Inventory is not allowed in this warehouse */
    public void setIsDisallowNegativeInv (boolean IsDisallowNegativeInv)
    {
        set_Value ("IsDisallowNegativeInv", Boolean.valueOf(IsDisallowNegativeInv));
        
    }
    
    /** Get Disallow Negative Inventory.
    @return Negative Inventory is not allowed in this warehouse */
    public boolean isDisallowNegativeInv() 
    {
        return get_ValueAsBoolean("IsDisallowNegativeInv");
        
    }
    
    /** Set WMS Enabled.
    @param IsWMSEnabled Is this warehouse WMS enabled */
    public void setIsWMSEnabled (boolean IsWMSEnabled)
    {
        set_Value ("IsWMSEnabled", Boolean.valueOf(IsWMSEnabled));
        
    }
    
    /** Get WMS Enabled.
    @return Is this warehouse WMS enabled */
    public boolean isWMSEnabled() 
    {
        return get_ValueAsBoolean("IsWMSEnabled");
        
    }
    
    /** Set Receiving Locator.
    @param M_RcvLocator_ID Default Locator used in quick receiving windows */
    public void setM_RcvLocator_ID (int M_RcvLocator_ID)
    {
        if (M_RcvLocator_ID <= 0) set_Value ("M_RcvLocator_ID", null);
        else
        set_Value ("M_RcvLocator_ID", Integer.valueOf(M_RcvLocator_ID));
        
    }
    
    /** Get Receiving Locator.
    @return Default Locator used in quick receiving windows */
    public int getM_RcvLocator_ID() 
    {
        return get_ValueAsInt("M_RcvLocator_ID");
        
    }
    
    /** Set Staging Locator.
    @param M_StgLocator_ID Default Staging Locator used in the Wave Planning and Release process */
    public void setM_StgLocator_ID (int M_StgLocator_ID)
    {
        if (M_StgLocator_ID <= 0) set_Value ("M_StgLocator_ID", null);
        else
        set_Value ("M_StgLocator_ID", Integer.valueOf(M_StgLocator_ID));
        
    }
    
    /** Get Staging Locator.
    @return Default Staging Locator used in the Wave Planning and Release process */
    public int getM_StgLocator_ID() 
    {
        return get_ValueAsInt("M_StgLocator_ID");
        
    }
    
    /** Set Source Warehouse.
    @param M_WarehouseSource_ID Optional Warehouse to replenish from */
    public void setM_WarehouseSource_ID (int M_WarehouseSource_ID)
    {
        if (M_WarehouseSource_ID <= 0) set_Value ("M_WarehouseSource_ID", null);
        else
        set_Value ("M_WarehouseSource_ID", Integer.valueOf(M_WarehouseSource_ID));
        
    }
    
    /** Get Source Warehouse.
    @return Optional Warehouse to replenish from */
    public int getM_WarehouseSource_ID() 
    {
        return get_ValueAsInt("M_WarehouseSource_ID");
        
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
    
    /** Set Replenishment Class.
    @param ReplenishmentClass Custom class to calculate Quantity to Order */
    public void setReplenishmentClass (String ReplenishmentClass)
    {
        set_Value ("ReplenishmentClass", ReplenishmentClass);
        
    }
    
    /** Get Replenishment Class.
    @return Custom class to calculate Quantity to Order */
    public String getReplenishmentClass() 
    {
        return (String)get_Value("ReplenishmentClass");
        
    }
    
    /** Set Element Separator.
    @param Separator Element Separator */
    public void setSeparator (String Separator)
    {
        if (Separator == null) throw new IllegalArgumentException ("Separator is mandatory.");
        set_Value ("Separator", Separator);
        
    }
    
    /** Get Element Separator.
    @return Element Separator */
    public String getSeparator() 
    {
        return (String)get_Value("Separator");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Working Days.
    @param WorkingDays Number of working days in the year for the warehouse */
    public void setWorkingDays (int WorkingDays)
    {
        set_Value ("WorkingDays", Integer.valueOf(WorkingDays));
        
    }
    
    /** Get Working Days.
    @return Number of working days in the year for the warehouse */
    public int getWorkingDays() 
    {
        return get_ValueAsInt("WorkingDays");
        
    }
    
    
}
