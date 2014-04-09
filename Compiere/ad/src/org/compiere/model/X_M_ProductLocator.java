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
/** Generated Model for M_ProductLocator
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_ProductLocator.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_ProductLocator extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_ProductLocator_ID id
    @param trx transaction
    */
    public X_M_ProductLocator (Ctx ctx, int M_ProductLocator_ID, Trx trx)
    {
        super (ctx, M_ProductLocator_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_ProductLocator_ID == 0)
        {
            setM_Locator_ID (0);
            setM_ProductLocator_ID (0);
            setM_Product_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_ProductLocator (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27526007259789L;
    /** Last Updated Timestamp 2009-06-01 11:45:43.0 */
    public static final long updatedMS = 1243881943000L;
    /** AD_Table_ID=915 */
    public static final int Table_ID=915;
    
    /** TableName=M_ProductLocator */
    public static final String Table_Name="M_ProductLocator";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID < 1) throw new IllegalArgumentException ("M_Locator_ID is mandatory.");
        set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
    }
    
    /** Set Product Locator.
    @param M_ProductLocator_ID The preferred Locators for a Product */
    public void setM_ProductLocator_ID (int M_ProductLocator_ID)
    {
        if (M_ProductLocator_ID < 1) throw new IllegalArgumentException ("M_ProductLocator_ID is mandatory.");
        set_ValueNoCheck ("M_ProductLocator_ID", Integer.valueOf(M_ProductLocator_ID));
        
    }
    
    /** Get Product Locator.
    @return The preferred Locators for a Product */
    public int getM_ProductLocator_ID() 
    {
        return get_ValueAsInt("M_ProductLocator_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
        throw new IllegalArgumentException ("PickingSeqNo is virtual column");
        
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
        throw new IllegalArgumentException ("Picking_UOM_ID is virtual column");
        
    }
    
    /** Get Picking UOM.
    @return Picking UOM of locator */
    public int getPicking_UOM_ID() 
    {
        return get_ValueAsInt("Picking_UOM_ID");
        
    }
    
    /** Set Relative Priority.
    @param PriorityNo Where inventory should be picked from first */
    public void setPriorityNo (int PriorityNo)
    {
        throw new IllegalArgumentException ("PriorityNo is virtual column");
        
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
        throw new IllegalArgumentException ("PutawaySeqNo is virtual column");
        
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
        throw new IllegalArgumentException ("Stocking_UOM_ID is virtual column");
        
    }
    
    /** Get Stocking UOM.
    @return Stocking UOM of locator */
    public int getStocking_UOM_ID() 
    {
        return get_ValueAsInt("Stocking_UOM_ID");
        
    }
    
    /** Set Volume Limit.
    @param VolumeLimit Volume Limit of a Locator */
    public void setVolumeLimit (java.math.BigDecimal VolumeLimit)
    {
        set_Value ("VolumeLimit", VolumeLimit);
        
    }
    
    /** Get Volume Limit.
    @return Volume Limit of a Locator */
    public java.math.BigDecimal getVolumeLimit() 
    {
        return get_ValueAsBigDecimal("VolumeLimit");
        
    }
    
    /** Set Weight Limit.
    @param WeightLimit Weight Limit of a Locator */
    public void setWeightLimit (java.math.BigDecimal WeightLimit)
    {
        set_Value ("WeightLimit", WeightLimit);
        
    }
    
    /** Get Weight Limit.
    @return Weight Limit of a Locator */
    public java.math.BigDecimal getWeightLimit() 
    {
        return get_ValueAsBigDecimal("WeightLimit");
        
    }
    
    
}
