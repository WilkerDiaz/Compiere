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
/** Generated Model for M_Locator
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_Locator.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_Locator extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_Locator_ID id
    @param trx transaction
    */
    public X_M_Locator (Ctx ctx, int M_Locator_ID, Trx trx)
    {
        super (ctx, M_Locator_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Locator_ID == 0)
        {
            setIsAvailableForAllocation (true);	// Y
            setIsAvailableToPromise (true);	// Y
            setIsDefault (false);
            setM_Locator_ID (0);
            setM_Warehouse_ID (0);
            setPickingSeqNo (0);
            setPriorityNo (0);	// 50
            setPutawaySeqNo (0);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_Locator (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27507277176789L;
    /** Last Updated Timestamp 2008-10-27 15:57:40.0 */
    public static final long updatedMS = 1225151860000L;
    /** AD_Table_ID=207 */
    public static final int Table_ID=207;
    
    /** TableName=M_Locator */
    public static final String Table_Name="M_Locator";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Bin.
    @param Bin Locator Bin segment */
    public void setBin (String Bin)
    {
        set_Value ("Bin", Bin);
        
    }
    
    /** Get Bin.
    @return Locator Bin segment */
    public String getBin() 
    {
        return (String)get_Value("Bin");
        
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
    
    /** Set Locator.
    @param LocatorCombination Delimited combination of locator segments */
    public void setLocatorCombination (String LocatorCombination)
    {
        set_Value ("LocatorCombination", LocatorCombination);
        
    }
    
    /** Get Locator.
    @return Delimited combination of locator segments */
    public String getLocatorCombination() 
    {
        return (String)get_Value("LocatorCombination");
        
    }
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID < 1) throw new IllegalArgumentException ("M_Locator_ID is mandatory.");
        set_ValueNoCheck ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_Warehouse_ID()));
        
    }
    
    /** Set Max Stocking Quantity.
    @param MaxQuantity Maximum stocking capacity of the locator in units */
    public void setMaxQuantity (java.math.BigDecimal MaxQuantity)
    {
        set_Value ("MaxQuantity", MaxQuantity);
        
    }
    
    /** Get Max Stocking Quantity.
    @return Maximum stocking capacity of the locator in units */
    public java.math.BigDecimal getMaxQuantity() 
    {
        return get_ValueAsBigDecimal("MaxQuantity");
        
    }
    
    /** Set Min Stocking Quantity.
    @param MinQuantity Minimum stocking quantity of locator in units */
    public void setMinQuantity (java.math.BigDecimal MinQuantity)
    {
        set_Value ("MinQuantity", MinQuantity);
        
    }
    
    /** Get Min Stocking Quantity.
    @return Minimum stocking quantity of locator in units */
    public java.math.BigDecimal getMinQuantity() 
    {
        return get_ValueAsBigDecimal("MinQuantity");
        
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
    
    /** Set Picking UOM.
    @param Picking_UOM_ID Picking UOM of locator */
    public void setPicking_UOM_ID (int Picking_UOM_ID)
    {
        if (Picking_UOM_ID <= 0) set_Value ("Picking_UOM_ID", null);
        else
        set_Value ("Picking_UOM_ID", Integer.valueOf(Picking_UOM_ID));
        
    }
    
    /** Get Picking UOM.
    @return Picking UOM of locator */
    public int getPicking_UOM_ID() 
    {
        return get_ValueAsInt("Picking_UOM_ID");
        
    }
    
    /** Set Position.
    @param Position Locator Position segment */
    public void setPosition (String Position)
    {
        set_Value ("Position", Position);
        
    }
    
    /** Get Position.
    @return Locator Position segment */
    public String getPosition() 
    {
        return (String)get_Value("Position");
        
    }
    
    /** Set Relative Priority.
    @param PriorityNo Where inventory should be picked from first */
    public void setPriorityNo (int PriorityNo)
    {
        set_Value ("PriorityNo", Integer.valueOf(PriorityNo));
        
    }
    
    /** Get Relative Priority.
    @return Where inventory should be picked from first */
    public int getPriorityNo() 
    {
        return get_ValueAsInt("PriorityNo");
        
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
    
    /** Set Stocking UOM.
    @param Stocking_UOM_ID Stocking UOM of locator */
    public void setStocking_UOM_ID (int Stocking_UOM_ID)
    {
        if (Stocking_UOM_ID <= 0) set_Value ("Stocking_UOM_ID", null);
        else
        set_Value ("Stocking_UOM_ID", Integer.valueOf(Stocking_UOM_ID));
        
    }
    
    /** Get Stocking UOM.
    @return Stocking UOM of locator */
    public int getStocking_UOM_ID() 
    {
        return get_ValueAsInt("Stocking_UOM_ID");
        
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
    
    /** Set Aisle.
    @param X Locator Aisle segment */
    public void setX (String X)
    {
        set_Value ("X", X);
        
    }
    
    /** Get Aisle.
    @return Locator Aisle segment */
    public String getX() 
    {
        return (String)get_Value("X");
        
    }
    
    /** Set Bay.
    @param Y Locator Bay segment */
    public void setY (String Y)
    {
        set_Value ("Y", Y);
        
    }
    
    /** Get Bay.
    @return Locator Bay segment */
    public String getY() 
    {
        return (String)get_Value("Y");
        
    }
    
    /** Set Row.
    @param Z Row segment of locator */
    public void setZ (String Z)
    {
        set_Value ("Z", Z);
        
    }
    
    /** Get Row.
    @return Row segment of locator */
    public String getZ() 
    {
        return (String)get_Value("Z");
        
    }
    
    
}
