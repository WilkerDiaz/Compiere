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
/** Generated Model for M_WorkOrderComponent
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_WorkOrderComponent.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_WorkOrderComponent extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WorkOrderComponent_ID id
    @param trx transaction
    */
    public X_M_WorkOrderComponent (Ctx ctx, int M_WorkOrderComponent_ID, Trx trx)
    {
        super (ctx, M_WorkOrderComponent_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WorkOrderComponent_ID == 0)
        {
            setBasisType (null);	// I
            setC_UOM_ID (0);
            setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_WorkOrderComponent WHERE M_WorkOrderOperation_ID=@M_WorkOrderOperation_ID@
            setM_Product_ID (0);
            setM_WorkOrderComponent_ID (0);
            setM_WorkOrderOperation_ID (0);
            setProcessed (false);	// N
            setQtyAllocated (Env.ZERO);	// 0
            setQtyAvailable (Env.ZERO);	// 0
            setQtyDedicated (Env.ZERO);	// 0
            setQtyRequired (Env.ZERO);	// 0
            setQtySpent (Env.ZERO);	// 0
            setSupplyType (null);	// P
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WorkOrderComponent (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27524964055789L;
    /** Last Updated Timestamp 2009-05-20 09:58:59.0 */
    public static final long updatedMS = 1242838739000L;
    /** AD_Table_ID=1034 */
    public static final int Table_ID=1034;
    
    /** TableName=M_WorkOrderComponent */
    public static final String Table_Name="M_WorkOrderComponent";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Trx Organization.
    @param AD_OrgTrx_ID Performing or initiating organization */
    public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
    {
        if (AD_OrgTrx_ID <= 0) set_Value ("AD_OrgTrx_ID", null);
        else
        set_Value ("AD_OrgTrx_ID", Integer.valueOf(AD_OrgTrx_ID));
        
    }
    
    /** Get Trx Organization.
    @return Performing or initiating organization */
    public int getAD_OrgTrx_ID() 
    {
        return get_ValueAsInt("AD_OrgTrx_ID");
        
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
    
    /** Set Activity.
    @param C_Activity_ID Business Activity */
    public void setC_Activity_ID (int C_Activity_ID)
    {
        if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
        else
        set_Value ("C_Activity_ID", Integer.valueOf(C_Activity_ID));
        
    }
    
    /** Get Activity.
    @return Business Activity */
    public int getC_Activity_ID() 
    {
        return get_ValueAsInt("C_Activity_ID");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID <= 0) set_Value ("C_BPartner_Location_ID", null);
        else
        set_Value ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
    }
    
    /** Set Campaign.
    @param C_Campaign_ID Marketing Campaign */
    public void setC_Campaign_ID (int C_Campaign_ID)
    {
        if (C_Campaign_ID <= 0) set_Value ("C_Campaign_ID", null);
        else
        set_Value ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
    }
    
    /** Get Campaign.
    @return Marketing Campaign */
    public int getC_Campaign_ID() 
    {
        return get_ValueAsInt("C_Campaign_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
        else
        set_Value ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
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
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (int Line)
    {
        set_Value ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
    }
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID <= 0) set_Value ("M_AttributeSetInstance_ID", null);
        else
        set_Value ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get Attribute Set Instance.
    @return Product Attribute Set Instance */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
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
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Work Order Component.
    @param M_WorkOrderComponent_ID Work Order Component */
    public void setM_WorkOrderComponent_ID (int M_WorkOrderComponent_ID)
    {
        if (M_WorkOrderComponent_ID < 1) throw new IllegalArgumentException ("M_WorkOrderComponent_ID is mandatory.");
        set_ValueNoCheck ("M_WorkOrderComponent_ID", Integer.valueOf(M_WorkOrderComponent_ID));
        
    }
    
    /** Get Work Order Component.
    @return Work Order Component */
    public int getM_WorkOrderComponent_ID() 
    {
        return get_ValueAsInt("M_WorkOrderComponent_ID");
        
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
    
    /** Set Quantity Allocated.
    @param QtyAllocated Quantity that has been picked and is awaiting shipment */
    public void setQtyAllocated (java.math.BigDecimal QtyAllocated)
    {
        if (QtyAllocated == null) throw new IllegalArgumentException ("QtyAllocated is mandatory.");
        set_Value ("QtyAllocated", QtyAllocated);
        
    }
    
    /** Get Quantity Allocated.
    @return Quantity that has been picked and is awaiting shipment */
    public java.math.BigDecimal getQtyAllocated() 
    {
        return get_ValueAsBigDecimal("QtyAllocated");
        
    }
    
    /** Set Available Quantity.
    @param QtyAvailable Available Quantity (On Hand - Reserved) */
    public void setQtyAvailable (java.math.BigDecimal QtyAvailable)
    {
        if (QtyAvailable == null) throw new IllegalArgumentException ("QtyAvailable is mandatory.");
        set_ValueNoCheck ("QtyAvailable", QtyAvailable);
        
    }
    
    /** Get Available Quantity.
    @return Available Quantity (On Hand - Reserved) */
    public java.math.BigDecimal getQtyAvailable() 
    {
        return get_ValueAsBigDecimal("QtyAvailable");
        
    }
    
    /** Set Quantity Dedicated.
    @param QtyDedicated Quantity for which there is a pending Warehouse Task */
    public void setQtyDedicated (java.math.BigDecimal QtyDedicated)
    {
        if (QtyDedicated == null) throw new IllegalArgumentException ("QtyDedicated is mandatory.");
        set_Value ("QtyDedicated", QtyDedicated);
        
    }
    
    /** Get Quantity Dedicated.
    @return Quantity for which there is a pending Warehouse Task */
    public java.math.BigDecimal getQtyDedicated() 
    {
        return get_ValueAsBigDecimal("QtyDedicated");
        
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
    
    /** Assembly Pull = A */
    public static final String SUPPLYTYPE_AssemblyPull = X_Ref_M_BOMProduct_SupplyType.ASSEMBLY_PULL.getValue();
    /** Operation Pull = O */
    public static final String SUPPLYTYPE_OperationPull = X_Ref_M_BOMProduct_SupplyType.OPERATION_PULL.getValue();
    /** Push = P */
    public static final String SUPPLYTYPE_Push = X_Ref_M_BOMProduct_SupplyType.PUSH.getValue();
    /** Set Supply Type.
    @param SupplyType Supply type for components */
    public void setSupplyType (String SupplyType)
    {
        if (SupplyType == null) throw new IllegalArgumentException ("SupplyType is mandatory");
        if (!X_Ref_M_BOMProduct_SupplyType.isValid(SupplyType))
        throw new IllegalArgumentException ("SupplyType Invalid value - " + SupplyType + " - Reference_ID=444 - A - O - P");
        set_Value ("SupplyType", SupplyType);
        
    }
    
    /** Get Supply Type.
    @return Supply type for components */
    public String getSupplyType() 
    {
        return (String)get_Value("SupplyType");
        
    }
    
    /** Set User List 1.
    @param User1_ID User defined list element #1 */
    public void setUser1_ID (int User1_ID)
    {
        if (User1_ID <= 0) set_Value ("User1_ID", null);
        else
        set_Value ("User1_ID", Integer.valueOf(User1_ID));
        
    }
    
    /** Get User List 1.
    @return User defined list element #1 */
    public int getUser1_ID() 
    {
        return get_ValueAsInt("User1_ID");
        
    }
    
    /** Set User List 2.
    @param User2_ID User defined list element #2 */
    public void setUser2_ID (int User2_ID)
    {
        if (User2_ID <= 0) set_Value ("User2_ID", null);
        else
        set_Value ("User2_ID", Integer.valueOf(User2_ID));
        
    }
    
    /** Get User List 2.
    @return User defined list element #2 */
    public int getUser2_ID() 
    {
        return get_ValueAsInt("User2_ID");
        
    }
    
    
}
