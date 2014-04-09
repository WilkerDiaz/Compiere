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
/** Generated Model for M_Zone
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_Zone.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_Zone extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_Zone_ID id
    @param trx transaction
    */
    public X_M_Zone (Ctx ctx, int M_Zone_ID, Trx trx)
    {
        super (ctx, M_Zone_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Zone_ID == 0)
        {
            setIsAvailableForAllocation (true);	// Y
            setIsAvailableToPromise (true);	// Y
            setIsStatic (true);	// Y
            setM_Warehouse_ID (0);
            setM_Zone_ID (0);
            setName (null);
            setPickingSeqNo (0);
            setPutawaySeqNo (0);
            setReplenishmentSeqNo (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_Zone (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27503793469789L;
    /** Last Updated Timestamp 2008-09-17 09:15:53.0 */
    public static final long updatedMS = 1221668153000L;
    /** AD_Table_ID=1011 */
    public static final int Table_ID=1011;
    
    /** TableName=M_Zone */
    public static final String Table_Name="M_Zone";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Add Locators.
    @param AddLocator Add Locators to Zone */
    public void setAddLocator (String AddLocator)
    {
        set_Value ("AddLocator", AddLocator);
        
    }
    
    /** Get Add Locators.
    @return Add Locators to Zone */
    public String getAddLocator() 
    {
        return (String)get_Value("AddLocator");
        
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
    
    /** Set Available For Allocation.
    @param IsAvailableForAllocation Stock in this locator is available for allocation */
    public void setIsAvailableForAllocation (boolean IsAvailableForAllocation)
    {
        set_Value ("IsAvailableForAllocation", Boolean.valueOf(IsAvailableForAllocation));
        
    }
    
    /** Get Available For Allocation.
    @return Stock in this locator is available for allocation */
    public boolean isAvailableForAllocation() 
    {
        return get_ValueAsBoolean("IsAvailableForAllocation");
        
    }
    
    /** Set Available To Promise.
    @param IsAvailableToPromise Stock in this locator is available to promise */
    public void setIsAvailableToPromise (boolean IsAvailableToPromise)
    {
        set_Value ("IsAvailableToPromise", Boolean.valueOf(IsAvailableToPromise));
        
    }
    
    /** Get Available To Promise.
    @return Stock in this locator is available to promise */
    public boolean isAvailableToPromise() 
    {
        return get_ValueAsBoolean("IsAvailableToPromise");
        
    }
    
    /** Set Static Zone.
    @param IsStatic If checked, this zone cannot overlap other static zones */
    public void setIsStatic (boolean IsStatic)
    {
        set_Value ("IsStatic", Boolean.valueOf(IsStatic));
        
    }
    
    /** Get Static Zone.
    @return If checked, this zone cannot overlap other static zones */
    public boolean isStatic() 
    {
        return get_ValueAsBoolean("IsStatic");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Zone.
    @param M_Zone_ID Warehouse zone */
    public void setM_Zone_ID (int M_Zone_ID)
    {
        if (M_Zone_ID < 1) throw new IllegalArgumentException ("M_Zone_ID is mandatory.");
        set_ValueNoCheck ("M_Zone_ID", Integer.valueOf(M_Zone_ID));
        
    }
    
    /** Get Zone.
    @return Warehouse zone */
    public int getM_Zone_ID() 
    {
        return get_ValueAsInt("M_Zone_ID");
        
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
    
    /** Set Picking Sequence No.
    @param PickingSeqNo Picking Sequence Number of locator/zone */
    public void setPickingSeqNo (int PickingSeqNo)
    {
        set_Value ("PickingSeqNo", Integer.valueOf(PickingSeqNo));
        
    }
    
    /** Get Picking Sequence No.
    @return Picking Sequence Number of locator/zone */
    public int getPickingSeqNo() 
    {
        return get_ValueAsInt("PickingSeqNo");
        
    }
    
    /** Set Putaway Sequence No.
    @param PutawaySeqNo Putaway Sequence Number of locator/zone */
    public void setPutawaySeqNo (int PutawaySeqNo)
    {
        set_Value ("PutawaySeqNo", Integer.valueOf(PutawaySeqNo));
        
    }
    
    /** Get Putaway Sequence No.
    @return Putaway Sequence Number of locator/zone */
    public int getPutawaySeqNo() 
    {
        return get_ValueAsInt("PutawaySeqNo");
        
    }
    
    /** Set Replenishment Sequence No.
    @param ReplenishmentSeqNo Replenishment Sequence No of zone */
    public void setReplenishmentSeqNo (int ReplenishmentSeqNo)
    {
        set_Value ("ReplenishmentSeqNo", Integer.valueOf(ReplenishmentSeqNo));
        
    }
    
    /** Get Replenishment Sequence No.
    @return Replenishment Sequence No of zone */
    public int getReplenishmentSeqNo() 
    {
        return get_ValueAsInt("ReplenishmentSeqNo");
        
    }
    
    /** Set Synchronize Defaults.
    @param SynchronizeDefaults Copy the values of Available to Promise and Available for Allocation flags to all locators in this zone. */
    public void setSynchronizeDefaults (String SynchronizeDefaults)
    {
        set_Value ("SynchronizeDefaults", SynchronizeDefaults);
        
    }
    
    /** Get Synchronize Defaults.
    @return Copy the values of Available to Promise and Available for Allocation flags to all locators in this zone. */
    public String getSynchronizeDefaults() 
    {
        return (String)get_Value("SynchronizeDefaults");
        
    }
    
    
}
