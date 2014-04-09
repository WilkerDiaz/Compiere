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
/** Generated Model for MRP_PlannedDemand
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_MRP_PlannedDemand.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_MRP_PlannedDemand extends PO
{
    /** Standard Constructor
    @param ctx context
    @param MRP_PlannedDemand_ID id
    @param trx transaction
    */
    public X_MRP_PlannedDemand (Ctx ctx, int MRP_PlannedDemand_ID, Trx trx)
    {
        super (ctx, MRP_PlannedDemand_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (MRP_PlannedDemand_ID == 0)
        {
            setC_Period_ID (0);
            setC_UOM_ID (0);
            setDateRequired (new Timestamp(System.currentTimeMillis()));
            setLeadTime (Env.ZERO);	// 0
            setLevelNo (0);	// 0
            setMRP_PlanRun_ID (0);
            setMRP_PlannedDemand_ID (0);
            setM_Product_ID (0);
            setQtyRequired (Env.ZERO);	// 0
            setRunStatus (null);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_MRP_PlannedDemand (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27532675665789L;
    /** Last Updated Timestamp 2009-08-17 16:05:49.0 */
    public static final long updatedMS = 1250550349000L;
    /** AD_Table_ID=2110 */
    public static final int Table_ID=2110;
    
    /** TableName=MRP_PlannedDemand */
    public static final String Table_Name="MRP_PlannedDemand";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Period_ID()));
        
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
    
    /** Set Date Required.
    @param DateRequired Date when required */
    public void setDateRequired (Timestamp DateRequired)
    {
        if (DateRequired == null) throw new IllegalArgumentException ("DateRequired is mandatory.");
        set_ValueNoCheck ("DateRequired", DateRequired);
        
    }
    
    /** Get Date Required.
    @return Date when required */
    public Timestamp getDateRequired() 
    {
        return (Timestamp)get_Value("DateRequired");
        
    }
    
    /** Set Lead Time.
    @param LeadTime Manufacturing or procurement lead time in days */
    public void setLeadTime (java.math.BigDecimal LeadTime)
    {
        if (LeadTime == null) throw new IllegalArgumentException ("LeadTime is mandatory.");
        set_Value ("LeadTime", LeadTime);
        
    }
    
    /** Get Lead Time.
    @return Manufacturing or procurement lead time in days */
    public java.math.BigDecimal getLeadTime() 
    {
        return get_ValueAsBigDecimal("LeadTime");
        
    }
    
    /** Set Level no.
    @param LevelNo Level Number */
    public void setLevelNo (int LevelNo)
    {
        set_ValueNoCheck ("LevelNo", Integer.valueOf(LevelNo));
        
    }
    
    /** Get Level no.
    @return Level Number */
    public int getLevelNo() 
    {
        return get_ValueAsInt("LevelNo");
        
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
    
    /** Set Planned Demand.
    @param MRP_PlannedDemand_ID Exploded master demand calculated by the Plan Run */
    public void setMRP_PlannedDemand_ID (int MRP_PlannedDemand_ID)
    {
        if (MRP_PlannedDemand_ID < 1) throw new IllegalArgumentException ("MRP_PlannedDemand_ID is mandatory.");
        set_ValueNoCheck ("MRP_PlannedDemand_ID", Integer.valueOf(MRP_PlannedDemand_ID));
        
    }
    
    /** Get Planned Demand.
    @return Exploded master demand calculated by the Plan Run */
    public int getMRP_PlannedDemand_ID() 
    {
        return get_ValueAsInt("MRP_PlannedDemand_ID");
        
    }
    
    /** Set Planned Demand Parent.
    @param MRP_PlannedDemand_Parent_ID Planned Demand of the Immediate Parent Product */
    public void setMRP_PlannedDemand_Parent_ID (int MRP_PlannedDemand_Parent_ID)
    {
        if (MRP_PlannedDemand_Parent_ID <= 0) set_ValueNoCheck ("MRP_PlannedDemand_Parent_ID", null);
        else
        set_ValueNoCheck ("MRP_PlannedDemand_Parent_ID", Integer.valueOf(MRP_PlannedDemand_Parent_ID));
        
    }
    
    /** Get Planned Demand Parent.
    @return Planned Demand of the Immediate Parent Product */
    public int getMRP_PlannedDemand_Parent_ID() 
    {
        return get_ValueAsInt("MRP_PlannedDemand_Parent_ID");
        
    }
    
    /** Set Planned Demand Root.
    @param MRP_PlannedDemand_Root_ID Planned Demand of the Root Product (Top Most in the Tree) */
    public void setMRP_PlannedDemand_Root_ID (int MRP_PlannedDemand_Root_ID)
    {
        if (MRP_PlannedDemand_Root_ID <= 0) set_ValueNoCheck ("MRP_PlannedDemand_Root_ID", null);
        else
        set_ValueNoCheck ("MRP_PlannedDemand_Root_ID", Integer.valueOf(MRP_PlannedDemand_Root_ID));
        
    }
    
    /** Get Planned Demand Root.
    @return Planned Demand of the Root Product (Top Most in the Tree) */
    public int getMRP_PlannedDemand_Root_ID() 
    {
        return get_ValueAsInt("MRP_PlannedDemand_Root_ID");
        
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
    
    /** Set Required Quantity.
    @param QtyRequired Quantity required for an activity */
    public void setQtyRequired (java.math.BigDecimal QtyRequired)
    {
        if (QtyRequired == null) throw new IllegalArgumentException ("QtyRequired is mandatory.");
        set_Value ("QtyRequired", QtyRequired);
        
    }
    
    /** Get Required Quantity.
    @return Quantity required for an activity */
    public java.math.BigDecimal getQtyRequired() 
    {
        return get_ValueAsBigDecimal("QtyRequired");
        
    }
    
    /** Error During Run = E */
    public static final String RUNSTATUS_ErrorDuringRun = X_Ref_MRP_PlannedDemand_RunStatus.ERROR_DURING_RUN.getValue();
    /** In Progress = I */
    public static final String RUNSTATUS_InProgress = X_Ref_MRP_PlannedDemand_RunStatus.IN_PROGRESS.getValue();
    /** Not Running = N */
    public static final String RUNSTATUS_NotRunning = X_Ref_MRP_PlannedDemand_RunStatus.NOT_RUNNING.getValue();
    /** Setup Error = X */
    public static final String RUNSTATUS_SetupError = X_Ref_MRP_PlannedDemand_RunStatus.SETUP_ERROR.getValue();
    /** Completed Running = Y */
    public static final String RUNSTATUS_CompletedRunning = X_Ref_MRP_PlannedDemand_RunStatus.COMPLETED_RUNNING.getValue();
    /** Set Run Status.
    @param RunStatus Plan Run Status */
    public void setRunStatus (String RunStatus)
    {
        if (RunStatus == null) throw new IllegalArgumentException ("RunStatus is mandatory");
        if (!X_Ref_MRP_PlannedDemand_RunStatus.isValid(RunStatus))
        throw new IllegalArgumentException ("RunStatus Invalid value - " + RunStatus + " - Reference_ID=506 - E - I - N - X - Y");
        set_Value ("RunStatus", RunStatus);
        
    }
    
    /** Get Run Status.
    @return Plan Run Status */
    public String getRunStatus() 
    {
        return (String)get_Value("RunStatus");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (java.math.BigDecimal SeqNo)
    {
        set_Value ("SeqNo", SeqNo);
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public java.math.BigDecimal getSeqNo() 
    {
        return get_ValueAsBigDecimal("SeqNo");
        
    }
    
    
}
