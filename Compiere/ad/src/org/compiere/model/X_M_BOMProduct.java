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
/** Generated Model for M_BOMProduct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_BOMProduct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_BOMProduct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_BOMProduct_ID id
    @param trx transaction
    */
    public X_M_BOMProduct (Ctx ctx, int M_BOMProduct_ID, Trx trx)
    {
        super (ctx, M_BOMProduct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_BOMProduct_ID == 0)
        {
            setBOMProductType (null);	// S
            setBOMQty (Env.ZERO);	// 1
            setBasisType (null);	// I
            setIsPhantom (false);	// N
            setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_BOMProduct WHERE M_BOM_ID=@M_BOM_ID@
            setM_BOMProduct_ID (0);
            setM_BOM_ID (0);
            setM_ProductBOM_ID (0);
            setOperationSeqNo (0);	// 10
            setSupplyType (null);	// P
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_BOMProduct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518876856789L;
    /** Last Updated Timestamp 2009-03-10 22:05:40.0 */
    public static final long updatedMS = 1236751540000L;
    /** AD_Table_ID=801 */
    public static final int Table_ID=801;
    
    /** TableName=M_BOMProduct */
    public static final String Table_Name="M_BOMProduct";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Alternative = A */
    public static final String BOMPRODUCTTYPE_Alternative = X_Ref_M_BOMProduct_Type.ALTERNATIVE.getValue();
    /** Alternative (Default) = D */
    public static final String BOMPRODUCTTYPE_AlternativeDefault = X_Ref_M_BOMProduct_Type.ALTERNATIVE_DEFAULT.getValue();
    /** Optional Product = O */
    public static final String BOMPRODUCTTYPE_OptionalProduct = X_Ref_M_BOMProduct_Type.OPTIONAL_PRODUCT.getValue();
    /** Standard Product = S */
    public static final String BOMPRODUCTTYPE_StandardProduct = X_Ref_M_BOMProduct_Type.STANDARD_PRODUCT.getValue();
    /** Outside Processing = X */
    public static final String BOMPRODUCTTYPE_OutsideProcessing = X_Ref_M_BOMProduct_Type.OUTSIDE_PROCESSING.getValue();
    /** Set Component Type.
    @param BOMProductType BOM Product Type */
    public void setBOMProductType (String BOMProductType)
    {
        if (BOMProductType == null) throw new IllegalArgumentException ("BOMProductType is mandatory");
        if (!X_Ref_M_BOMProduct_Type.isValid(BOMProductType))
        throw new IllegalArgumentException ("BOMProductType Invalid value - " + BOMProductType + " - Reference_ID=349 - A - D - O - S - X");
        set_Value ("BOMProductType", BOMProductType);
        
    }
    
    /** Get Component Type.
    @return BOM Product Type */
    public String getBOMProductType() 
    {
        return (String)get_Value("BOMProductType");
        
    }
    
    /** Set Quantity.
    @param BOMQty Bill of Materials Quantity */
    public void setBOMQty (java.math.BigDecimal BOMQty)
    {
        if (BOMQty == null) throw new IllegalArgumentException ("BOMQty is mandatory.");
        set_Value ("BOMQty", BOMQty);
        
    }
    
    /** Get Quantity.
    @return Bill of Materials Quantity */
    public java.math.BigDecimal getBOMQty() 
    {
        return get_ValueAsBigDecimal("BOMQty");
        
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
        throw new IllegalArgumentException ("C_UOM_ID is virtual column");
        
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
    
    /** Set Phantom.
    @param IsPhantom Phantom Component */
    public void setIsPhantom (boolean IsPhantom)
    {
        set_Value ("IsPhantom", Boolean.valueOf(IsPhantom));
        
    }
    
    /** Get Phantom.
    @return Phantom Component */
    public boolean isPhantom() 
    {
        return get_ValueAsBoolean("IsPhantom");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getLine()));
        
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
    
    /** Set Alternative Group.
    @param M_BOMAlternative_ID Product BOM Alternative Group */
    public void setM_BOMAlternative_ID (int M_BOMAlternative_ID)
    {
        if (M_BOMAlternative_ID <= 0) set_Value ("M_BOMAlternative_ID", null);
        else
        set_Value ("M_BOMAlternative_ID", Integer.valueOf(M_BOMAlternative_ID));
        
    }
    
    /** Get Alternative Group.
    @return Product BOM Alternative Group */
    public int getM_BOMAlternative_ID() 
    {
        return get_ValueAsInt("M_BOMAlternative_ID");
        
    }
    
    /** Set BOM Component Line.
    @param M_BOMProduct_ID Bill of Materials Component Line */
    public void setM_BOMProduct_ID (int M_BOMProduct_ID)
    {
        if (M_BOMProduct_ID < 1) throw new IllegalArgumentException ("M_BOMProduct_ID is mandatory.");
        set_ValueNoCheck ("M_BOMProduct_ID", Integer.valueOf(M_BOMProduct_ID));
        
    }
    
    /** Get BOM Component Line.
    @return Bill of Materials Component Line */
    public int getM_BOMProduct_ID() 
    {
        return get_ValueAsInt("M_BOMProduct_ID");
        
    }
    
    /** Set BOM.
    @param M_BOM_ID Bill of Materials */
    public void setM_BOM_ID (int M_BOM_ID)
    {
        if (M_BOM_ID < 1) throw new IllegalArgumentException ("M_BOM_ID is mandatory.");
        set_ValueNoCheck ("M_BOM_ID", Integer.valueOf(M_BOM_ID));
        
    }
    
    /** Get BOM.
    @return Bill of Materials */
    public int getM_BOM_ID() 
    {
        return get_ValueAsInt("M_BOM_ID");
        
    }
    
    /** Set Component BOM.
    @param M_ProductBOMVersion_ID BOM for a component */
    public void setM_ProductBOMVersion_ID (int M_ProductBOMVersion_ID)
    {
        if (M_ProductBOMVersion_ID <= 0) set_Value ("M_ProductBOMVersion_ID", null);
        else
        set_Value ("M_ProductBOMVersion_ID", Integer.valueOf(M_ProductBOMVersion_ID));
        
    }
    
    /** Get Component BOM.
    @return BOM for a component */
    public int getM_ProductBOMVersion_ID() 
    {
        return get_ValueAsInt("M_ProductBOMVersion_ID");
        
    }
    
    /** Set Component.
    @param M_ProductBOM_ID Bill of Materials Component (Product) */
    public void setM_ProductBOM_ID (int M_ProductBOM_ID)
    {
        if (M_ProductBOM_ID < 1) throw new IllegalArgumentException ("M_ProductBOM_ID is mandatory.");
        set_Value ("M_ProductBOM_ID", Integer.valueOf(M_ProductBOM_ID));
        
    }
    
    /** Get Component.
    @return Bill of Materials Component (Product) */
    public int getM_ProductBOM_ID() 
    {
        return get_ValueAsInt("M_ProductBOM_ID");
        
    }
    
    /** Set Operation Sequence No.
    @param OperationSeqNo Indicates the work order operation sequence number in which the BOM component is to be consumed */
    public void setOperationSeqNo (int OperationSeqNo)
    {
        set_Value ("OperationSeqNo", Integer.valueOf(OperationSeqNo));
        
    }
    
    /** Get Operation Sequence No.
    @return Indicates the work order operation sequence number in which the BOM component is to be consumed */
    public int getOperationSeqNo() 
    {
        return get_ValueAsInt("OperationSeqNo");
        
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
    
    
}
