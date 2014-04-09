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
/** Generated Model for M_WorkOrderOperation
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_WorkOrderOperation.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_WorkOrderOperation extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WorkOrderOperation_ID id
    @param trx transaction
    */
    public X_M_WorkOrderOperation (Ctx ctx, int M_WorkOrderOperation_ID, Trx trx)
    {
        super (ctx, M_WorkOrderOperation_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WorkOrderOperation_ID == 0)
        {
            setC_UOM_ID (0);	// @SQL=SELECT C_UOM_ID FROM C_UOM WHERE X12DE355= 'DA'
            setIsHazmat (false);	// N
            setIsOptional (false);	// N
            setIsPermitRequired (false);	// N
            setM_Operation_ID (0);
            setM_WorkCenter_ID (0);
            setM_WorkOrderOperation_ID (0);
            setM_WorkOrder_ID (0);
            setProcessed (false);	// N
            setQtyAssembled (Env.ZERO);	// 0
            setQtyQueued (Env.ZERO);	// 0
            setQtyRun (Env.ZERO);	// 0
            setQtyScrapped (Env.ZERO);	// 0
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM M_WorkOrderOperation WHERE M_WorkOrder_ID=@M_WorkOrder_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WorkOrderOperation (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27526263790789L;
    /** Last Updated Timestamp 2009-06-04 11:01:14.0 */
    public static final long updatedMS = 1244138474000L;
    /** AD_Table_ID=1029 */
    public static final int Table_ID=1029;
    
    /** TableName=M_WorkOrderOperation */
    public static final String Table_Name="M_WorkOrderOperation";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID < 1) throw new IllegalArgumentException ("C_UOM_ID is mandatory.");
        set_Value ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Actual Date From.
    @param DateActualFrom Actual date an activity started */
    public void setDateActualFrom (Timestamp DateActualFrom)
    {
        set_Value ("DateActualFrom", DateActualFrom);
        
    }
    
    /** Get Actual Date From.
    @return Actual date an activity started */
    public Timestamp getDateActualFrom() 
    {
        return (Timestamp)get_Value("DateActualFrom");
        
    }
    
    /** Set Actual Date To.
    @param DateActualTo Actual date an activity ended */
    public void setDateActualTo (Timestamp DateActualTo)
    {
        set_Value ("DateActualTo", DateActualTo);
        
    }
    
    /** Get Actual Date To.
    @return Actual date an activity ended */
    public Timestamp getDateActualTo() 
    {
        return (Timestamp)get_Value("DateActualTo");
        
    }
    
    /** Set Date Processed.
    @param DateProcessed Date Processed */
    public void setDateProcessed (Timestamp DateProcessed)
    {
        set_Value ("DateProcessed", DateProcessed);
        
    }
    
    /** Get Date Processed.
    @return Date Processed */
    public Timestamp getDateProcessed() 
    {
        return (Timestamp)get_Value("DateProcessed");
        
    }
    
    /** Set Scheduled Date From.
    @param DateScheduleFrom Date an activity is scheduled to start */
    public void setDateScheduleFrom (Timestamp DateScheduleFrom)
    {
        set_Value ("DateScheduleFrom", DateScheduleFrom);
        
    }
    
    /** Get Scheduled Date From.
    @return Date an activity is scheduled to start */
    public Timestamp getDateScheduleFrom() 
    {
        return (Timestamp)get_Value("DateScheduleFrom");
        
    }
    
    /** Set Scheduled Date To.
    @param DateScheduleTo Date an activity is scheduled to end */
    public void setDateScheduleTo (Timestamp DateScheduleTo)
    {
        set_Value ("DateScheduleTo", DateScheduleTo);
        
    }
    
    /** Get Scheduled Date To.
    @return Date an activity is scheduled to end */
    public Timestamp getDateScheduleTo() 
    {
        return (Timestamp)get_Value("DateScheduleTo");
        
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
    
    /** Set Hazmat.
    @param IsHazmat Involves hazardous materials */
    public void setIsHazmat (boolean IsHazmat)
    {
        set_Value ("IsHazmat", Boolean.valueOf(IsHazmat));
        
    }
    
    /** Get Hazmat.
    @return Involves hazardous materials */
    public boolean isHazmat() 
    {
        return get_ValueAsBoolean("IsHazmat");
        
    }
    
    /** Set Optional.
    @param IsOptional Optional */
    public void setIsOptional (boolean IsOptional)
    {
        set_Value ("IsOptional", Boolean.valueOf(IsOptional));
        
    }
    
    /** Get Optional.
    @return Optional */
    public boolean isOptional() 
    {
        return get_ValueAsBoolean("IsOptional");
        
    }
    
    /** Set Permit Required.
    @param IsPermitRequired Indicates if a permit or similar authorization is required for use or execution of a product, resource or work order operation. */
    public void setIsPermitRequired (boolean IsPermitRequired)
    {
        set_Value ("IsPermitRequired", Boolean.valueOf(IsPermitRequired));
        
    }
    
    /** Get Permit Required.
    @return Indicates if a permit or similar authorization is required for use or execution of a product, resource or work order operation. */
    public boolean isPermitRequired() 
    {
        return get_ValueAsBoolean("IsPermitRequired");
        
    }
    
    /** Set Operation.
    @param M_Operation_ID Manufacturing Operation */
    public void setM_Operation_ID (int M_Operation_ID)
    {
        if (M_Operation_ID < 1) throw new IllegalArgumentException ("M_Operation_ID is mandatory.");
        set_ValueNoCheck ("M_Operation_ID", Integer.valueOf(M_Operation_ID));
        
    }
    
    /** Get Operation.
    @return Manufacturing Operation */
    public int getM_Operation_ID() 
    {
        return get_ValueAsInt("M_Operation_ID");
        
    }
    
    /** Set Standard Operation.
    @param M_StandardOperation_ID Identifies a standard operation template */
    public void setM_StandardOperation_ID (int M_StandardOperation_ID)
    {
        if (M_StandardOperation_ID <= 0) set_ValueNoCheck ("M_StandardOperation_ID", null);
        else
        set_ValueNoCheck ("M_StandardOperation_ID", Integer.valueOf(M_StandardOperation_ID));
        
    }
    
    /** Get Standard Operation.
    @return Identifies a standard operation template */
    public int getM_StandardOperation_ID() 
    {
        return get_ValueAsInt("M_StandardOperation_ID");
        
    }
    
    /** Set Work Center.
    @param M_WorkCenter_ID Identifies a production area within a warehouse consisting of people and equipment */
    public void setM_WorkCenter_ID (int M_WorkCenter_ID)
    {
        if (M_WorkCenter_ID < 1) throw new IllegalArgumentException ("M_WorkCenter_ID is mandatory.");
        set_ValueNoCheck ("M_WorkCenter_ID", Integer.valueOf(M_WorkCenter_ID));
        
    }
    
    /** Get Work Center.
    @return Identifies a production area within a warehouse consisting of people and equipment */
    public int getM_WorkCenter_ID() 
    {
        return get_ValueAsInt("M_WorkCenter_ID");
        
    }
    
    /** Set Operation.
    @param M_WorkOrderOperation_ID Production routing operation on a work order */
    public void setM_WorkOrderOperation_ID (int M_WorkOrderOperation_ID)
    {
        if (M_WorkOrderOperation_ID < 1) throw new IllegalArgumentException ("M_WorkOrderOperation_ID is mandatory.");
        set_ValueNoCheck ("M_WorkOrderOperation_ID", Integer.valueOf(M_WorkOrderOperation_ID));
        
    }
    
    /** Get Operation.
    @return Production routing operation on a work order */
    public int getM_WorkOrderOperation_ID() 
    {
        return get_ValueAsInt("M_WorkOrderOperation_ID");
        
    }
    
    /** Set Work Order.
    @param M_WorkOrder_ID Work Order */
    public void setM_WorkOrder_ID (int M_WorkOrder_ID)
    {
        if (M_WorkOrder_ID < 1) throw new IllegalArgumentException ("M_WorkOrder_ID is mandatory.");
        set_ValueNoCheck ("M_WorkOrder_ID", Integer.valueOf(M_WorkOrder_ID));
        
    }
    
    /** Get Work Order.
    @return Work Order */
    public int getM_WorkOrder_ID() 
    {
        return get_ValueAsInt("M_WorkOrder_ID");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Quantity Assembled.
    @param QtyAssembled Quantity finished at a production routing step */
    public void setQtyAssembled (java.math.BigDecimal QtyAssembled)
    {
        if (QtyAssembled == null) throw new IllegalArgumentException ("QtyAssembled is mandatory.");
        set_Value ("QtyAssembled", QtyAssembled);
        
    }
    
    /** Get Quantity Assembled.
    @return Quantity finished at a production routing step */
    public java.math.BigDecimal getQtyAssembled() 
    {
        return get_ValueAsBigDecimal("QtyAssembled");
        
    }
    
    /** Set Quantity Queued.
    @param QtyQueued Number of sub-assemblies in the Queue step of a work order operation */
    public void setQtyQueued (java.math.BigDecimal QtyQueued)
    {
        if (QtyQueued == null) throw new IllegalArgumentException ("QtyQueued is mandatory.");
        set_Value ("QtyQueued", QtyQueued);
        
    }
    
    /** Get Quantity Queued.
    @return Number of sub-assemblies in the Queue step of a work order operation */
    public java.math.BigDecimal getQtyQueued() 
    {
        return get_ValueAsBigDecimal("QtyQueued");
        
    }
    
    /** Set Quantity Run.
    @param QtyRun Number of sub-assemblies in the Run step of a work order operation */
    public void setQtyRun (java.math.BigDecimal QtyRun)
    {
        if (QtyRun == null) throw new IllegalArgumentException ("QtyRun is mandatory.");
        set_Value ("QtyRun", QtyRun);
        
    }
    
    /** Get Quantity Run.
    @return Number of sub-assemblies in the Run step of a work order operation */
    public java.math.BigDecimal getQtyRun() 
    {
        return get_ValueAsBigDecimal("QtyRun");
        
    }
    
    /** Set Quantity Scrapped.
    @param QtyScrapped This is the number of sub-assemblies in the Scrap step of an operation in Work Order. */
    public void setQtyScrapped (java.math.BigDecimal QtyScrapped)
    {
        if (QtyScrapped == null) throw new IllegalArgumentException ("QtyScrapped is mandatory.");
        set_Value ("QtyScrapped", QtyScrapped);
        
    }
    
    /** Get Quantity Scrapped.
    @return This is the number of sub-assemblies in the Scrap step of an operation in Work Order. */
    public java.math.BigDecimal getQtyScrapped() 
    {
        return get_ValueAsBigDecimal("QtyScrapped");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
        
    }
    
    /** Set Setup Time.
    @param SetupTime Setup time before starting Production */
    public void setSetupTime (java.math.BigDecimal SetupTime)
    {
        set_Value ("SetupTime", SetupTime);
        
    }
    
    /** Get Setup Time.
    @return Setup time before starting Production */
    public java.math.BigDecimal getSetupTime() 
    {
        return get_ValueAsBigDecimal("SetupTime");
        
    }
    
    /** Set Supervisor.
    @param Supervisor_ID Supervisor for this user/organization - used for escalation and approval */
    public void setSupervisor_ID (int Supervisor_ID)
    {
        if (Supervisor_ID <= 0) set_Value ("Supervisor_ID", null);
        else
        set_Value ("Supervisor_ID", Integer.valueOf(Supervisor_ID));
        
    }
    
    /** Get Supervisor.
    @return Supervisor for this user/organization - used for escalation and approval */
    public int getSupervisor_ID() 
    {
        return get_ValueAsInt("Supervisor_ID");
        
    }
    
    /** Set Runtime per Unit.
    @param UnitRuntime Time to produce one unit */
    public void setUnitRuntime (java.math.BigDecimal UnitRuntime)
    {
        set_Value ("UnitRuntime", UnitRuntime);
        
    }
    
    /** Get Runtime per Unit.
    @return Time to produce one unit */
    public java.math.BigDecimal getUnitRuntime() 
    {
        return get_ValueAsBigDecimal("UnitRuntime");
        
    }
    
    
}
