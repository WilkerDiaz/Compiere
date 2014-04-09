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
/** Generated Model for MRP_PlannedAvailability
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_MRP_PlannedAvailability.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_MRP_PlannedAvailability extends PO
{
    /** Standard Constructor
    @param ctx context
    @param MRP_PlannedAvailability_ID id
    @param trx transaction
    */
    public X_MRP_PlannedAvailability (Ctx ctx, int MRP_PlannedAvailability_ID, Trx trx)
    {
        super (ctx, MRP_PlannedAvailability_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (MRP_PlannedAvailability_ID == 0)
        {
            setAvailabilityStatus (null);	// N
            setC_Period_ID (0);
            setC_UOM_ID (0);
            setDateExpected (new Timestamp(System.currentTimeMillis()));
            setMRP_PlanRun_ID (0);
            setM_Product_ID (0);
            setQtyCalculated (Env.ZERO);	// 0
            setQtyExpected (Env.ZERO);	// 0
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_MRP_PlannedAvailability (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27522015599789L;
    /** Last Updated Timestamp 2009-04-16 05:58:03.0 */
    public static final long updatedMS = 1239890283000L;
    /** AD_Table_ID=2108 */
    public static final int Table_ID=2108;
    
    /** TableName=MRP_PlannedAvailability */
    public static final String Table_Name="MRP_PlannedAvailability";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Not Updated = N */
    public static final String AVAILABILITYSTATUS_NotUpdated = X_Ref_AvailabilityStatus.NOT_UPDATED.getValue();
    /** Updated = U */
    public static final String AVAILABILITYSTATUS_Updated = X_Ref_AvailabilityStatus.UPDATED.getValue();
    /** Set Avaiability Status.
    @param AvailabilityStatus Processing status of Planned Availability */
    public void setAvailabilityStatus (String AvailabilityStatus)
    {
        if (AvailabilityStatus == null) throw new IllegalArgumentException ("AvailabilityStatus is mandatory");
        if (!X_Ref_AvailabilityStatus.isValid(AvailabilityStatus))
        throw new IllegalArgumentException ("AvailabilityStatus Invalid value - " + AvailabilityStatus + " - Reference_ID=512 - N - U");
        set_Value ("AvailabilityStatus", AvailabilityStatus);
        
    }
    
    /** Get Avaiability Status.
    @return Processing status of Planned Availability */
    public String getAvailabilityStatus() 
    {
        return (String)get_Value("AvailabilityStatus");
        
    }
    
    /** Set Period.
    @param C_Period_ID Period of the Calendar */
    public void setC_Period_ID (int C_Period_ID)
    {
        if (C_Period_ID < 1) throw new IllegalArgumentException ("C_Period_ID is mandatory.");
        set_ValueNoCheck ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
    }
    
    /** Get Period.
    @return Period of the Calendar */
    public int getC_Period_ID() 
    {
        return get_ValueAsInt("C_Period_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID < 1) throw new IllegalArgumentException ("C_UOM_ID is mandatory.");
        set_ValueNoCheck ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Date Expected.
    @param DateExpected Date on which the order is expected to be fulfilled */
    public void setDateExpected (Timestamp DateExpected)
    {
        if (DateExpected == null) throw new IllegalArgumentException ("DateExpected is mandatory.");
        set_ValueNoCheck ("DateExpected", DateExpected);
        
    }
    
    /** Get Date Expected.
    @return Date on which the order is expected to be fulfilled */
    public Timestamp getDateExpected() 
    {
        return (Timestamp)get_Value("DateExpected");
        
    }
    
    /** Set Plan Run.
    @param MRP_PlanRun_ID An execution of the plan */
    public void setMRP_PlanRun_ID (int MRP_PlanRun_ID)
    {
        if (MRP_PlanRun_ID < 1) throw new IllegalArgumentException ("MRP_PlanRun_ID is mandatory.");
        set_ValueNoCheck ("MRP_PlanRun_ID", Integer.valueOf(MRP_PlanRun_ID));
        
    }
    
    /** Get Plan Run.
    @return An execution of the plan */
    public int getMRP_PlanRun_ID() 
    {
        return get_ValueAsInt("MRP_PlanRun_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_Product_ID()));
        
    }
    
    /** Set Calculated Quantity.
    @param QtyCalculated Calculated Quantity */
    public void setQtyCalculated (java.math.BigDecimal QtyCalculated)
    {
        if (QtyCalculated == null) throw new IllegalArgumentException ("QtyCalculated is mandatory.");
        set_Value ("QtyCalculated", QtyCalculated);
        
    }
    
    /** Get Calculated Quantity.
    @return Calculated Quantity */
    public java.math.BigDecimal getQtyCalculated() 
    {
        return get_ValueAsBigDecimal("QtyCalculated");
        
    }
    
    /** Set Expected Quantity.
    @param QtyExpected Quantity expected to be received into a locator */
    public void setQtyExpected (java.math.BigDecimal QtyExpected)
    {
        if (QtyExpected == null) throw new IllegalArgumentException ("QtyExpected is mandatory.");
        set_ValueNoCheck ("QtyExpected", QtyExpected);
        
    }
    
    /** Get Expected Quantity.
    @return Quantity expected to be received into a locator */
    public java.math.BigDecimal getQtyExpected() 
    {
        return get_ValueAsBigDecimal("QtyExpected");
        
    }
    
    
}
