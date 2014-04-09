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
/** Generated Model for M_StandardOperationResource
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_StandardOperationResource.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_StandardOperationResource extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_StandardOperationResource_ID id
    @param trx transaction
    */
    public X_M_StandardOperationResource (Ctx ctx, int M_StandardOperationResource_ID, Trx trx)
    {
        super (ctx, M_StandardOperationResource_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_StandardOperationResource_ID == 0)
        {
            setBasisType (null);	// I
            setC_UOM_ID (0);
            setChargeType (null);	// A
            setM_Product_ID (0);
            setM_StandardOperationResource_ID (0);
            setM_StandardOperation_ID (0);
            setQtyRequired (Env.ZERO);	// 1
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM M_StandardOperationResource WHERE M_StandardOperation_ID=@M_StandardOperation_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_StandardOperationResource (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27524965221789L;
    /** Last Updated Timestamp 2009-05-20 10:18:25.0 */
    public static final long updatedMS = 1242839905000L;
    /** AD_Table_ID=2090 */
    public static final int Table_ID=2090;
    
    /** TableName=M_StandardOperationResource */
    public static final String Table_Name="M_StandardOperationResource";
    
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
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Standard Operation Resource.
    @param M_StandardOperationResource_ID Identifier for a production resource required in a standard operation */
    public void setM_StandardOperationResource_ID (int M_StandardOperationResource_ID)
    {
        if (M_StandardOperationResource_ID < 1) throw new IllegalArgumentException ("M_StandardOperationResource_ID is mandatory.");
        set_ValueNoCheck ("M_StandardOperationResource_ID", Integer.valueOf(M_StandardOperationResource_ID));
        
    }
    
    /** Get Standard Operation Resource.
    @return Identifier for a production resource required in a standard operation */
    public int getM_StandardOperationResource_ID() 
    {
        return get_ValueAsInt("M_StandardOperationResource_ID");
        
    }
    
    /** Set Standard Operation.
    @param M_StandardOperation_ID Identifies a standard operation template */
    public void setM_StandardOperation_ID (int M_StandardOperation_ID)
    {
        if (M_StandardOperation_ID < 1) throw new IllegalArgumentException ("M_StandardOperation_ID is mandatory.");
        set_ValueNoCheck ("M_StandardOperation_ID", Integer.valueOf(M_StandardOperation_ID));
        
    }
    
    /** Get Standard Operation.
    @return Identifies a standard operation template */
    public int getM_StandardOperation_ID() 
    {
        return get_ValueAsInt("M_StandardOperation_ID");
        
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
