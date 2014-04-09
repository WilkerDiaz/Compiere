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
/** Generated Model for M_WorkOrderResource
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_WorkOrderResource.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_WorkOrderResource extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WorkOrderResource_ID id
    @param trx transaction
    */
    public X_M_WorkOrderResource (Ctx ctx, int M_WorkOrderResource_ID, Trx trx)
    {
        super (ctx, M_WorkOrderResource_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WorkOrderResource_ID == 0)
        {
            setBasisType (null);	// I
            setC_UOM_ID (0);
            setChargeType (null);	// A
            setM_Product_ID (0);
            setM_WorkOrderOperation_ID (0);
            setM_WorkOrderResource_ID (0);
            setProcessed (false);	// N
            setQtyRequired (Env.ZERO);	// 1
            setQtySpent (Env.ZERO);	// 0
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM M_WorkOrderResource WHERE M_WorkOrderOperation_ID=@M_WorkOrderOperation_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WorkOrderResource (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27524964472789L;
    /** Last Updated Timestamp 2009-05-20 10:05:56.0 */
    public static final long updatedMS = 1242839156000L;
    /** AD_Table_ID=2092 */
    public static final int Table_ID=2092;
    
    /** TableName=M_WorkOrderResource */
    public static final String Table_Name="M_WorkOrderResource";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Per Batch = B */
    public static final String BASISTYPE_PerBatch = X_Ref_M_Product_Basis_Type.PER_BATCH.getValue();
    /** Per Item = I */
    public static final String BASISTYPE_PerItem = X_Ref_M_Product_Basis_Type.PER_ITEM.getValue();
    /** Set Cost Basis Type.
    @param BasisType Indicates the option to consume and charge materials and resources */
    public void setBasisType (String BasisType)
    {
        if (BasisType == null) throw new IllegalArgumentException ("BasisType is mandatory");
        if (!X_Ref_M_Product_Basis_Type.isValid(BasisType))
        throw new IllegalArgumentException ("BasisType Invalid value - " + BasisType + " - Reference_ID=496 - B - I");
        set_Value ("BasisType", BasisType);
        
    }
    
    /** Get Cost Basis Type.
    @return Indicates the option to consume and charge materials and resources */
    public String getBasisType() 
    {
        return (String)get_Value("BasisType");
        
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
    
    /** Automatic = A */
    public static final String CHARGETYPE_Automatic = X_Ref_M_Product_Charge_Type.AUTOMATIC.getValue();
    /** Manual = M */
    public static final String CHARGETYPE_Manual = X_Ref_M_Product_Charge_Type.MANUAL.getValue();
    /** Set Cost Charge Type.
    @param ChargeType Indicates how the production resource will be charged - automatically or manually */
    public void setChargeType (String ChargeType)
    {
        if (ChargeType == null) throw new IllegalArgumentException ("ChargeType is mandatory");
        if (!X_Ref_M_Product_Charge_Type.isValid(ChargeType))
        throw new IllegalArgumentException ("ChargeType Invalid value - " + ChargeType + " - Reference_ID=497 - A - M");
        set_Value ("ChargeType", ChargeType);
        
    }
    
    /** Get Cost Charge Type.
    @return Indicates how the production resource will be charged - automatically or manually */
    public String getChargeType() 
    {
        return (String)get_Value("ChargeType");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_WorkOrderOperation_ID()));
        
    }
    
    /** Set M_WorkOrderResource_ID.
    @param M_WorkOrderResource_ID Identifies the resource requirements in a work order operation */
    public void setM_WorkOrderResource_ID (int M_WorkOrderResource_ID)
    {
        if (M_WorkOrderResource_ID < 1) throw new IllegalArgumentException ("M_WorkOrderResource_ID is mandatory.");
        set_ValueNoCheck ("M_WorkOrderResource_ID", Integer.valueOf(M_WorkOrderResource_ID));
        
    }
    
    /** Get M_WorkOrderResource_ID.
    @return Identifies the resource requirements in a work order operation */
    public int getM_WorkOrderResource_ID() 
    {
        return get_ValueAsInt("M_WorkOrderResource_ID");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_Value ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
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
    
    /** Set Quantity Used.
    @param QtySpent Quantity used for this event */
    public void setQtySpent (java.math.BigDecimal QtySpent)
    {
        if (QtySpent == null) throw new IllegalArgumentException ("QtySpent is mandatory.");
        set_ValueNoCheck ("QtySpent", QtySpent);
        
    }
    
    /** Get Quantity Used.
    @return Quantity used for this event */
    public java.math.BigDecimal getQtySpent() 
    {
        return get_ValueAsBigDecimal("QtySpent");
        
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
    
    
}
