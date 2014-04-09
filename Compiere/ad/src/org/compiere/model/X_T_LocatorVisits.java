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
/** Generated Model for T_LocatorVisits
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_LocatorVisits.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_LocatorVisits extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_LocatorVisits_ID id
    @param trx transaction
    */
    public X_T_LocatorVisits (Ctx ctx, int T_LocatorVisits_ID, Trx trx)
    {
        super (ctx, T_LocatorVisits_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_LocatorVisits_ID == 0)
        {
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_LocatorVisits (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511671695789L;
    /** Last Updated Timestamp 2008-12-17 12:39:39.0 */
    public static final long updatedMS = 1229546379000L;
    /** AD_Table_ID=1074 */
    public static final int Table_ID=1074;
    
    /** TableName=T_LocatorVisits */
    public static final String Table_Name="T_LocatorVisits";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Instance.
    @param AD_PInstance_ID Instance of the process */
    public void setAD_PInstance_ID (int AD_PInstance_ID)
    {
        if (AD_PInstance_ID <= 0) set_Value ("AD_PInstance_ID", null);
        else
        set_Value ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID <= 0) set_Value ("M_Locator_ID", null);
        else
        set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** Set Movement Date.
    @param MovementDate Date a product was moved in or out of inventory */
    public void setMovementDate (Timestamp MovementDate)
    {
        set_Value ("MovementDate", MovementDate);
        
    }
    
    /** Get Movement Date.
    @return Date a product was moved in or out of inventory */
    public Timestamp getMovementDate() 
    {
        return (Timestamp)get_Value("MovementDate");
        
    }
    
    /** Set Pick Visits.
    @param PickVisits Pick Visits */
    public void setPickVisits (java.math.BigDecimal PickVisits)
    {
        set_Value ("PickVisits", PickVisits);
        
    }
    
    /** Get Pick Visits.
    @return Pick Visits */
    public java.math.BigDecimal getPickVisits() 
    {
        return get_ValueAsBigDecimal("PickVisits");
        
    }
    
    /** Set Putaway Visits.
    @param PutawayVisits Putaway Visits */
    public void setPutawayVisits (java.math.BigDecimal PutawayVisits)
    {
        set_Value ("PutawayVisits", PutawayVisits);
        
    }
    
    /** Get Putaway Visits.
    @return Putaway Visits */
    public java.math.BigDecimal getPutawayVisits() 
    {
        return get_ValueAsBigDecimal("PutawayVisits");
        
    }
    
    /** Set Replenishment Visits.
    @param ReplenishmentVisits Replenishment Visits */
    public void setReplenishmentVisits (java.math.BigDecimal ReplenishmentVisits)
    {
        set_Value ("ReplenishmentVisits", ReplenishmentVisits);
        
    }
    
    /** Get Replenishment Visits.
    @return Replenishment Visits */
    public java.math.BigDecimal getReplenishmentVisits() 
    {
        return get_ValueAsBigDecimal("ReplenishmentVisits");
        
    }
    
    /** Set Total Visits.
    @param TotalVisits Total Visits */
    public void setTotalVisits (java.math.BigDecimal TotalVisits)
    {
        set_Value ("TotalVisits", TotalVisits);
        
    }
    
    /** Get Total Visits.
    @return Total Visits */
    public java.math.BigDecimal getTotalVisits() 
    {
        return get_ValueAsBigDecimal("TotalVisits");
        
    }
    
    
}
