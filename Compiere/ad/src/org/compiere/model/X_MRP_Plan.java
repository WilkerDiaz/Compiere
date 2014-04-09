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
/** Generated Model for MRP_Plan
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_MRP_Plan.java 9127 2010-07-15 07:56:13Z ssharma $ */
public class X_MRP_Plan extends PO
{
    /** Standard Constructor
    @param ctx context
    @param MRP_Plan_ID id
    @param trx transaction
    */
    public X_MRP_Plan (Ctx ctx, int MRP_Plan_ID, Trx trx)
    {
        super (ctx, MRP_Plan_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (MRP_Plan_ID == 0)
        {
            setC_Calendar_ID (0);
            setC_DocTypeTarget_ID (0);
            setC_Period_From_ID (0);
            setC_Period_To_ID (0);
            setIsConsolidatePO (false);	// N
            setMRP_Plan_ID (0);
            setM_PriceList_ID (0);	// @SQL= SELECT M_PriceList_ID FROM M_PriceList WHERE IsSOPriceList = 'N' AND AD_Client_ID = @AD_Client_ID@ AND AD_Org_ID IN (0,@AD_Org_ID@)
            setM_Warehouse_ID (0);
            setName (null);
            setPrioritizeOrderOverDemand (false);	// N
            setPriorityImplementation (null);	// P
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_MRP_Plan (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27561251662789L;
    /** Last Updated Timestamp 2010-07-14 22:22:26.0 */
    public static final long updatedMS = 1279126346000L;
    /** AD_Table_ID=2095 */
    public static final int Table_ID=2095;
    
    /** TableName=MRP_Plan */
    public static final String Table_Name="MRP_Plan";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Calendar.
    @param C_Calendar_ID Accounting Calendar Name */
    public void setC_Calendar_ID (int C_Calendar_ID)
    {
        if (C_Calendar_ID < 1) throw new IllegalArgumentException ("C_Calendar_ID is mandatory.");
        set_ValueNoCheck ("C_Calendar_ID", Integer.valueOf(C_Calendar_ID));
        
    }
    
    /** Get Calendar.
    @return Accounting Calendar Name */
    public int getC_Calendar_ID() 
    {
        return get_ValueAsInt("C_Calendar_ID");
        
    }
    
    /** Set Target Doc Type.
    @param C_DocTypeTarget_ID Target document type for documents */
    public void setC_DocTypeTarget_ID (int C_DocTypeTarget_ID)
    {
        if (C_DocTypeTarget_ID < 1) throw new IllegalArgumentException ("C_DocTypeTarget_ID is mandatory.");
        set_Value ("C_DocTypeTarget_ID", Integer.valueOf(C_DocTypeTarget_ID));
        
    }
    
    /** Get Target Doc Type.
    @return Target document type for documents */
    public int getC_DocTypeTarget_ID() 
    {
        return get_ValueAsInt("C_DocTypeTarget_ID");
        
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
    
    /** Set Period To.
    @param C_Period_To_ID Ending period of a range of periods */
    public void setC_Period_To_ID (int C_Period_To_ID)
    {
        if (C_Period_To_ID < 1) throw new IllegalArgumentException ("C_Period_To_ID is mandatory.");
        set_ValueNoCheck ("C_Period_To_ID", Integer.valueOf(C_Period_To_ID));
        
    }
    
    /** Get Period To.
    @return Ending period of a range of periods */
    public int getC_Period_To_ID() 
    {
        return get_ValueAsInt("C_Period_To_ID");
        
    }
    
    /** Set Date Last Run.
    @param DateLastRun Date the process was last run. */
    public void setDateLastRun (Timestamp DateLastRun)
    {
        set_Value ("DateLastRun", DateLastRun);
        
    }
    
    /** Get Date Last Run.
    @return Date the process was last run. */
    public Timestamp getDateLastRun() 
    {
        return (Timestamp)get_Value("DateLastRun");
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set Consolidate PO.
    @param IsConsolidatePO Consolidate PO */
    public void setIsConsolidatePO (boolean IsConsolidatePO)
    {
        set_Value ("IsConsolidatePO", Boolean.valueOf(IsConsolidatePO));
        
    }
    
    /** Get Consolidate PO.
    @return Consolidate PO */
    public boolean isConsolidatePO() 
    {
        return get_ValueAsBoolean("IsConsolidatePO");
        
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
    
    /** Set Price List.
    @param M_PriceList_ID Unique identifier of a Price List */
    public void setM_PriceList_ID (int M_PriceList_ID)
    {
        if (M_PriceList_ID < 1) throw new IllegalArgumentException ("M_PriceList_ID is mandatory.");
        set_Value ("M_PriceList_ID", Integer.valueOf(M_PriceList_ID));
        
    }
    
    /** Get Price List.
    @return Unique identifier of a Price List */
    public int getM_PriceList_ID() 
    {
        return get_ValueAsInt("M_PriceList_ID");
        
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
    
    /** Set Work Order Class.
    @param M_WorkOrderClass_ID Indicates the document types and accounts to be used for a work order */
    public void setM_WorkOrderClass_ID (int M_WorkOrderClass_ID)
    {
        if (M_WorkOrderClass_ID <= 0) set_Value ("M_WorkOrderClass_ID", null);
        else
        set_Value ("M_WorkOrderClass_ID", Integer.valueOf(M_WorkOrderClass_ID));
        
    }
    
    /** Get Work Order Class.
    @return Indicates the document types and accounts to be used for a work order */
    public int getM_WorkOrderClass_ID() 
    {
        return get_ValueAsInt("M_WorkOrderClass_ID");
        
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
    
    /** Set Prioritize Orders Over Demand.
    @param PrioritizeOrderOverDemand Firm orders are considered over demand if orders exceed demand */
    public void setPrioritizeOrderOverDemand (boolean PrioritizeOrderOverDemand)
    {
        set_Value ("PrioritizeOrderOverDemand", Boolean.valueOf(PrioritizeOrderOverDemand));
        
    }
    
    /** Get Prioritize Orders Over Demand.
    @return Firm orders are considered over demand if orders exceed demand */
    public boolean isPrioritizeOrderOverDemand() 
    {
        return get_ValueAsBoolean("PrioritizeOrderOverDemand");
        
    }
    
    /** Manufacture = M */
    public static final String PRIORITYIMPLEMENTATION_Manufacture = X_Ref_PriorityImplementation.MANUFACTURE.getValue();
    /** Purchase = P */
    public static final String PRIORITYIMPLEMENTATION_Purchase = X_Ref_PriorityImplementation.PURCHASE.getValue();
    /** Set Implementation Priority.
    @param PriorityImplementation Indicates preference to either procure or manufacture a product */
    public void setPriorityImplementation (String PriorityImplementation)
    {
        if (PriorityImplementation == null) throw new IllegalArgumentException ("PriorityImplementation is mandatory");
        if (!X_Ref_PriorityImplementation.isValid(PriorityImplementation))
        throw new IllegalArgumentException ("PriorityImplementation Invalid value - " + PriorityImplementation + " - Reference_ID=500 - M - P");
        set_Value ("PriorityImplementation", PriorityImplementation);
        
    }
    
    /** Get Implementation Priority.
    @return Indicates preference to either procure or manufacture a product */
    public String getPriorityImplementation() 
    {
        return (String)get_Value("PriorityImplementation");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    
}
