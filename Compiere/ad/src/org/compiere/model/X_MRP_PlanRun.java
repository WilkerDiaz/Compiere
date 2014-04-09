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
/** Generated Model for MRP_PlanRun
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_MRP_PlanRun.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_MRP_PlanRun extends PO
{
    /** Standard Constructor
    @param ctx context
    @param MRP_PlanRun_ID id
    @param trx transaction
    */
    public X_MRP_PlanRun (Ctx ctx, int MRP_PlanRun_ID, Trx trx)
    {
        super (ctx, MRP_PlanRun_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (MRP_PlanRun_ID == 0)
        {
            setC_Period_From_ID (0);
            setMRP_MasterDemand_ID (0);
            setMRP_PlanRun_ID (0);
            setMRP_Plan_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_MRP_PlanRun (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27522008961789L;
    /** Last Updated Timestamp 2009-04-16 04:07:25.0 */
    public static final long updatedMS = 1239883645000L;
    /** AD_Table_ID=2097 */
    public static final int Table_ID=2097;
    
    /** TableName=MRP_PlanRun */
    public static final String Table_Name="MRP_PlanRun";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Back Order Period From.
    @param C_Period_BackOrder_ID Period from which back orders should be considered for the plan run */
    public void setC_Period_BackOrder_ID (int C_Period_BackOrder_ID)
    {
        if (C_Period_BackOrder_ID <= 0) set_ValueNoCheck ("C_Period_BackOrder_ID", null);
        else
        set_ValueNoCheck ("C_Period_BackOrder_ID", Integer.valueOf(C_Period_BackOrder_ID));
        
    }
    
    /** Get Back Order Period From.
    @return Period from which back orders should be considered for the plan run */
    public int getC_Period_BackOrder_ID() 
    {
        return get_ValueAsInt("C_Period_BackOrder_ID");
        
    }
    
    /** Set Period From.
    @param C_Period_From_ID Starting period of a range of periods */
    public void setC_Period_From_ID (int C_Period_From_ID)
    {
        if (C_Period_From_ID < 1) throw new IllegalArgumentException ("C_Period_From_ID is mandatory.");
        set_ValueNoCheck ("C_Period_From_ID", Integer.valueOf(C_Period_From_ID));
        
    }
    
    /** Get Period From.
    @return Starting period of a range of periods */
    public int getC_Period_From_ID() 
    {
        return get_ValueAsInt("C_Period_From_ID");
        
    }
    
    /** Set Master Demand.
    @param MRP_MasterDemand_ID Master Demand for material requirements */
    public void setMRP_MasterDemand_ID (int MRP_MasterDemand_ID)
    {
        if (MRP_MasterDemand_ID < 1) throw new IllegalArgumentException ("MRP_MasterDemand_ID is mandatory.");
        set_ValueNoCheck ("MRP_MasterDemand_ID", Integer.valueOf(MRP_MasterDemand_ID));
        
    }
    
    /** Get Master Demand.
    @return Master Demand for material requirements */
    public int getMRP_MasterDemand_ID() 
    {
        return get_ValueAsInt("MRP_MasterDemand_ID");
        
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
    
    /** Set Plan.
    @param MRP_Plan_ID Material Requirements Plan */
    public void setMRP_Plan_ID (int MRP_Plan_ID)
    {
        if (MRP_Plan_ID < 1) throw new IllegalArgumentException ("MRP_Plan_ID is mandatory.");
        set_ValueNoCheck ("MRP_Plan_ID", Integer.valueOf(MRP_Plan_ID));
        
    }
    
    /** Get Plan.
    @return Material Requirements Plan */
    public int getMRP_Plan_ID() 
    {
        return get_ValueAsInt("MRP_Plan_ID");
        
    }
    
    
}
