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
/** Generated Model for I_BOMProduct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_I_BOMProduct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_I_BOMProduct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_BOMProduct_ID id
    @param trx transaction
    */
    public X_I_BOMProduct (Ctx ctx, int I_BOMProduct_ID, Trx trx)
    {
        super (ctx, I_BOMProduct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_BOMProduct_ID == 0)
        {
            setI_BOMProduct_ID (0);
            setI_IsImported (null);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_BOMProduct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27530901822789L;
    /** Last Updated Timestamp 2009-07-28 03:21:46.0 */
    public static final long updatedMS = 1248776506000L;
    /** AD_Table_ID=2133 */
    public static final int Table_ID=2133;
    
    /** TableName=I_BOMProduct */
    public static final String Table_Name="I_BOMProduct";
    
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
        set_Value ("BOMQty", BOMQty);
        
    }
    
    /** Get Quantity.
    @return Bill of Materials Quantity */
    public java.math.BigDecimal getBOMQty() 
    {
        return get_ValueAsBigDecimal("BOMQty");
        
    }
    
    /** Current Active = A */
    public static final String BOMTYPE_CurrentActive = X_Ref_M_BOM_Type.CURRENT_ACTIVE.getValue();
    /** Future = F */
    public static final String BOMTYPE_Future = X_Ref_M_BOM_Type.FUTURE.getValue();
    /** Maintenance = M */
    public static final String BOMTYPE_Maintenance = X_Ref_M_BOM_Type.MAINTENANCE.getValue();
    /** Make-To-Order = O */
    public static final String BOMTYPE_Make_To_Order = X_Ref_M_BOM_Type.MAKE__TO__ORDER.getValue();
    /** Previous = P */
    public static final String BOMTYPE_Previous = X_Ref_M_BOM_Type.PREVIOUS.getValue();
    /** Repair = R */
    public static final String BOMTYPE_Repair = X_Ref_M_BOM_Type.REPAIR.getValue();
    /** Spare = S */
    public static final String BOMTYPE_Spare = X_Ref_M_BOM_Type.SPARE.getValue();
    /** Set BOM Type.
    @param BOMType Type of BOM */
    public void setBOMType (String BOMType)
    {
        if (!X_Ref_M_BOM_Type.isValid(BOMType))
        throw new IllegalArgumentException ("BOMType Invalid value - " + BOMType + " - Reference_ID=347 - A - F - M - O - P - R - S");
        set_Value ("BOMType", BOMType);
        
    }
    
    /** Get BOM Type.
    @return Type of BOM */
    public String getBOMType() 
    {
        return (String)get_Value("BOMType");
        
    }
    
    /** Master = A */
    public static final String BOMUSE_Master = X_Ref_M_BOM_Use.MASTER.getValue();
    /** Engineering = E */
    public static final String BOMUSE_Engineering = X_Ref_M_BOM_Use.ENGINEERING.getValue();
    /** Manufacturing = M */
    public static final String BOMUSE_Manufacturing = X_Ref_M_BOM_Use.MANUFACTURING.getValue();
    /** Maintenance = N */
    public static final String BOMUSE_Maintenance = X_Ref_M_BOM_Use.MAINTENANCE.getValue();
    /** Planning = P */
    public static final String BOMUSE_Planning = X_Ref_M_BOM_Use.PLANNING.getValue();
    /** Repair = R */
    public static final String BOMUSE_Repair = X_Ref_M_BOM_Use.REPAIR.getValue();
    /** Set BOM Use.
    @param BOMUse The use of the Bill of Material */
    public void setBOMUse (String BOMUse)
    {
        if (!X_Ref_M_BOM_Use.isValid(BOMUse))
        throw new IllegalArgumentException ("BOMUse Invalid value - " + BOMUse + " - Reference_ID=348 - A - E - M - N - P - R");
        set_Value ("BOMUse", BOMUse);
        
    }
    
    /** Get BOM Use.
    @return The use of the Bill of Material */
    public String getBOMUse() 
    {
        return (String)get_Value("BOMUse");
        
    }
    
    /** Per Batch = B */
    public static final String BASISTYPE_PerBatch = X_Ref_M_Product_Basis_Type.PER_BATCH.getValue();
    /** Per Item = I */
    public static final String BASISTYPE_PerItem = X_Ref_M_Product_Basis_Type.PER_ITEM.getValue();
    /** Set Cost Basis Type.
    @param BasisType Indicates the option to consume and charge materials and resources */
    public void setBasisType (String BasisType)
    {
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
    
    /** Set Description URL.
    @param DescriptionURL URL for the description */
    public void setDescriptionURL (String DescriptionURL)
    {
        set_Value ("DescriptionURL", DescriptionURL);
        
    }
    
    /** Get Description URL.
    @return URL for the description */
    public String getDescriptionURL() 
    {
        return (String)get_Value("DescriptionURL");
        
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
    
    /** Set I_BOMProduct_ID.
    @param I_BOMProduct_ID I_BOMProduct_ID */
    public void setI_BOMProduct_ID (int I_BOMProduct_ID)
    {
        if (I_BOMProduct_ID < 1) throw new IllegalArgumentException ("I_BOMProduct_ID is mandatory.");
        set_ValueNoCheck ("I_BOMProduct_ID", Integer.valueOf(I_BOMProduct_ID));
        
    }
    
    /** Get I_BOMProduct_ID.
    @return I_BOMProduct_ID */
    public int getI_BOMProduct_ID() 
    {
        return get_ValueAsInt("I_BOMProduct_ID");
        
    }
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Error = E */
    public static final String I_ISIMPORTED_Error = X_Ref__IsImported.ERROR.getValue();
    /** No = N */
    public static final String I_ISIMPORTED_No = X_Ref__IsImported.NO.getValue();
    /** Yes = Y */
    public static final String I_ISIMPORTED_Yes = X_Ref__IsImported.YES.getValue();
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (String I_IsImported)
    {
        if (I_IsImported == null) throw new IllegalArgumentException ("I_IsImported is mandatory");
        if (!X_Ref__IsImported.isValid(I_IsImported))
        throw new IllegalArgumentException ("I_IsImported Invalid value - " + I_IsImported + " - Reference_ID=420 - E - N - Y");
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
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
        if (M_BOMProduct_ID <= 0) set_Value ("M_BOMProduct_ID", null);
        else
        set_Value ("M_BOMProduct_ID", Integer.valueOf(M_BOMProduct_ID));
        
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
        if (M_BOM_ID <= 0) set_Value ("M_BOM_ID", null);
        else
        set_Value ("M_BOM_ID", Integer.valueOf(M_BOM_ID));
        
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
        if (M_ProductBOM_ID <= 0) set_Value ("M_ProductBOM_ID", null);
        else
        set_Value ("M_ProductBOM_ID", Integer.valueOf(M_ProductBOM_ID));
        
    }
    
    /** Get Component.
    @return Bill of Materials Component (Product) */
    public int getM_ProductBOM_ID() 
    {
        return get_ValueAsInt("M_ProductBOM_ID");
        
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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Name 2.
    @param Name2 Additional Name */
    public void setName2 (String Name2)
    {
        set_Value ("Name2", Name2);
        
    }
    
    /** Get Name 2.
    @return Additional Name */
    public String getName2() 
    {
        return (String)get_Value("Name2");
        
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
    
    /** Set Product Name.
    @param ProductName Name of the Product */
    public void setProductName (String ProductName)
    {
        set_Value ("ProductName", ProductName);
        
    }
    
    /** Get Product Name.
    @return Name of the Product */
    public String getProductName() 
    {
        return (String)get_Value("ProductName");
        
    }
    
    /** Set Product Key.
    @param ProductValue Key of the Product */
    public void setProductValue (String ProductValue)
    {
        set_Value ("ProductValue", ProductValue);
        
    }
    
    /** Get Product Key.
    @return Key of the Product */
    public String getProductValue() 
    {
        return (String)get_Value("ProductValue");
        
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
