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
package compiere.model.cds;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_DistribProductDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_DistribProductDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_DistribProductDetail_ID id
    @param trx transaction
    */
    public X_XX_VMR_DistribProductDetail (Ctx ctx, int XX_VMR_DistribProductDetail_ID, Trx trx)
    {
        super (ctx, XX_VMR_DistribProductDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_DistribProductDetail_ID == 0)
        {
            setM_Product_ID (0);
            setXX_Quantity (0);
            setXX_VMR_DistribProductDetail_ID (0);
            setXX_VMR_DistributionDetail_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_DistribProductDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27656025437789L;
    /** Last Updated Timestamp 2013-07-15 10:25:21.0 */
    public static final long updatedMS = 1373900121000L;
    /** AD_Table_ID=1000204 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_DistribProductDetail");
        
    }
    ;
    
    /** TableName=XX_VMR_DistribProductDetail */
    public static final String Table_Name="XX_VMR_DistribProductDetail";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Tax Category.
    @param C_TaxCategory_ID Tax Category */
    public void setC_TaxCategory_ID (int C_TaxCategory_ID)
    {
        if (C_TaxCategory_ID <= 0) set_Value ("C_TaxCategory_ID", null);
        else
        set_Value ("C_TaxCategory_ID", Integer.valueOf(C_TaxCategory_ID));
        
    }
    
    /** Get Tax Category.
    @return Tax Category */
    public int getC_TaxCategory_ID() 
    {
        return get_ValueAsInt("C_TaxCategory_ID");
        
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
    
    /** Set Attribute Set.
    @param M_AttributeSet_ID Product Attribute Set */
    public void setM_AttributeSet_ID (int M_AttributeSet_ID)
    {
        if (M_AttributeSet_ID <= 0) set_Value ("M_AttributeSet_ID", null);
        else
        set_Value ("M_AttributeSet_ID", Integer.valueOf(M_AttributeSet_ID));
        
    }
    
    /** Get Attribute Set.
    @return Product Attribute Set */
    public int getM_AttributeSet_ID() 
    {
        return get_ValueAsInt("M_AttributeSet_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Unit Price.
    @param PriceActual Actual Price */
    public void setPriceActual (java.math.BigDecimal PriceActual)
    {
        set_Value ("PriceActual", PriceActual);
        
    }
    
    /** Get Unit Price.
    @return Actual Price */
    public java.math.BigDecimal getPriceActual() 
    {
        return get_ValueAsBigDecimal("PriceActual");
        
    }
    
    /** Set Can Set Definitive.
    @param XX_CanSetDefinitive Can Set Definitive */
    public void setXX_CanSetDefinitive (boolean XX_CanSetDefinitive)
    {
        set_Value ("XX_CanSetDefinitive", Boolean.valueOf(XX_CanSetDefinitive));
        
    }
    
    /** Get Can Set Definitive.
    @return Can Set Definitive */
    public boolean isXX_CanSetDefinitive() 
    {
        return get_ValueAsBoolean("XX_CanSetDefinitive");
        
    }
    
    /** Set Price Is Definitive (Whole Reference).
    @param XX_IsDefinitive Price Is Definitive (Whole Reference) */
    public void setXX_IsDefinitive (boolean XX_IsDefinitive)
    {
        set_Value ("XX_IsDefinitive", Boolean.valueOf(XX_IsDefinitive));
        
    }
    
    /** Get Price Is Definitive (Whole Reference).
    @return Price Is Definitive (Whole Reference) */
    public boolean isXX_IsDefinitive() 
    {
        return get_ValueAsBoolean("XX_IsDefinitive");
        
    }
    
    /** Set Price is Definitive (Only This Product).
    @param XX_IsDefinitiveIndividual Price is Definitive (Only This Product) */
    public void setXX_IsDefinitiveIndividual (boolean XX_IsDefinitiveIndividual)
    {
        set_Value ("XX_IsDefinitiveIndividual", Boolean.valueOf(XX_IsDefinitiveIndividual));
        
    }
    
    /** Get Price is Definitive (Only This Product).
    @return Price is Definitive (Only This Product) */
    public boolean isXX_IsDefinitiveIndividual() 
    {
        return get_ValueAsBoolean("XX_IsDefinitiveIndividual");
        
    }
    
    /** Set Price Reference.
    @param XX_LastSalePrice Price Reference */
    public void setXX_LastSalePrice (java.math.BigDecimal XX_LastSalePrice)
    {
        set_ValueNoCheck ("XX_LastSalePrice", XX_LastSalePrice);
        
    }
    
    /** Get Price Reference.
    @return Price Reference */
    public java.math.BigDecimal getXX_LastSalePrice() 
    {
        return get_ValueAsBigDecimal("XX_LastSalePrice");
        
    }
    
    /** Set Margin.
    @param XX_Margin Margin */
    public void setXX_Margin (java.math.BigDecimal XX_Margin)
    {
        set_Value ("XX_Margin", XX_Margin);
        
    }
    
    /** Get Margin.
    @return Margin */
    public java.math.BigDecimal getXX_Margin() 
    {
        return get_ValueAsBigDecimal("XX_Margin");
        
    }
    
    /** Set Minimum Competition Price.
    @param XX_MinCompetitionPrice Minimum Competition Price */
    public void setXX_MinCompetitionPrice (java.math.BigDecimal XX_MinCompetitionPrice)
    {
        set_Value ("XX_MinCompetitionPrice", XX_MinCompetitionPrice);
        
    }
    
    /** Get Minimum Competition Price.
    @return Minimum Competition Price */
    public java.math.BigDecimal getXX_MinCompetitionPrice() 
    {
        return get_ValueAsBigDecimal("XX_MinCompetitionPrice");
        
    }
    
    /** Set Quantity.
    @param XX_Quantity Quantity */
    public void setXX_Quantity (int XX_Quantity)
    {
        set_Value ("XX_Quantity", Integer.valueOf(XX_Quantity));
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public int getXX_Quantity() 
    {
        return get_ValueAsInt("XX_Quantity");
        
    }
    
    /** Set Sale Price.
    @param XX_SalePrice Sale Price */
    public void setXX_SalePrice (java.math.BigDecimal XX_SalePrice)
    {
        set_Value ("XX_SalePrice", XX_SalePrice);
        
    }
    
    /** Get Sale Price.
    @return Sale Price */
    public java.math.BigDecimal getXX_SalePrice() 
    {
        return get_ValueAsBigDecimal("XX_SalePrice");
        
    }
    
    /** Set Sale Price Plus Tax.
    @param XX_SalePricePlusTax Sale Price Plus Tax */
    public void setXX_SalePricePlusTax (java.math.BigDecimal XX_SalePricePlusTax)
    {
        set_Value ("XX_SalePricePlusTax", XX_SalePricePlusTax);
        
    }
    
    /** Get Sale Price Plus Tax.
    @return Sale Price Plus Tax */
    public java.math.BigDecimal getXX_SalePricePlusTax() 
    {
        return get_ValueAsBigDecimal("XX_SalePricePlusTax");
        
    }
    
    /** Set Tax Amount.
    @param XX_TaxAmount Tax Amount */
    public void setXX_TaxAmount (java.math.BigDecimal XX_TaxAmount)
    {
        set_Value ("XX_TaxAmount", XX_TaxAmount);
        
    }
    
    /** Get Tax Amount.
    @return Tax Amount */
    public java.math.BigDecimal getXX_TaxAmount() 
    {
        return get_ValueAsBigDecimal("XX_TaxAmount");
        
    }
    
    /** Set Origin Currency Cost.
    @param XX_UnitPurchasePrice Origin Currency Cost */
    public void setXX_UnitPurchasePrice (java.math.BigDecimal XX_UnitPurchasePrice)
    {
        set_Value ("XX_UnitPurchasePrice", XX_UnitPurchasePrice);
        
    }
    
    /** Get Origin Currency Cost.
    @return Origin Currency Cost */
    public java.math.BigDecimal getXX_UnitPurchasePrice() 
    {
        return get_ValueAsBigDecimal("XX_UnitPurchasePrice");
        
    }
    
    /** Set XX_UseSizeCurve.
    @param XX_UseSizeCurve XX_UseSizeCurve */
    public void setXX_UseSizeCurve (boolean XX_UseSizeCurve)
    {
        set_Value ("XX_UseSizeCurve", Boolean.valueOf(XX_UseSizeCurve));
        
    }
    
    /** Get XX_UseSizeCurve.
    @return XX_UseSizeCurve */
    public boolean isXX_UseSizeCurve() 
    {
        return get_ValueAsBoolean("XX_UseSizeCurve");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_Brand_ID is virtual column");
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_Department_ID is virtual column");
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set XX_VMR_DistribProductDetail_ID.
    @param XX_VMR_DistribProductDetail_ID XX_VMR_DistribProductDetail_ID */
    public void setXX_VMR_DistribProductDetail_ID (int XX_VMR_DistribProductDetail_ID)
    {
        if (XX_VMR_DistribProductDetail_ID < 1) throw new IllegalArgumentException ("XX_VMR_DistribProductDetail_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DistribProductDetail_ID", Integer.valueOf(XX_VMR_DistribProductDetail_ID));
        
    }
    
    /** Get XX_VMR_DistribProductDetail_ID.
    @return XX_VMR_DistribProductDetail_ID */
    public int getXX_VMR_DistribProductDetail_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistribProductDetail_ID");
        
    }
    
    /** Set XX_VMR_DistributionDetail_ID.
    @param XX_VMR_DistributionDetail_ID Distribution Detail */
    public void setXX_VMR_DistributionDetail_ID (int XX_VMR_DistributionDetail_ID)
    {
        if (XX_VMR_DistributionDetail_ID < 1) throw new IllegalArgumentException ("XX_VMR_DistributionDetail_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DistributionDetail_ID", Integer.valueOf(XX_VMR_DistributionDetail_ID));
        
    }
    
    /** Get XX_VMR_DistributionDetail_ID.
    @return Distribution Detail */
    public int getXX_VMR_DistributionDetail_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionDetail_ID");
        
    }
    
    /** Set Vendor Reference.
    @param XX_VMR_PO_LineRefProv_ID Vendor Reference */
    public void setXX_VMR_PO_LineRefProv_ID (int XX_VMR_PO_LineRefProv_ID)
    {
        if (XX_VMR_PO_LineRefProv_ID <= 0) set_Value ("XX_VMR_PO_LineRefProv_ID", null);
        else
        set_Value ("XX_VMR_PO_LineRefProv_ID", Integer.valueOf(XX_VMR_PO_LineRefProv_ID));
        
    }
    
    /** Get Vendor Reference.
    @return Vendor Reference */
    public int getXX_VMR_PO_LineRefProv_ID() 
    {
        return get_ValueAsInt("XX_VMR_PO_LineRefProv_ID");
        
    }
    
    /** Set Vendor Product Reference.
    @param XX_VMR_VendorProdRef_ID Vendor Product Reference */
    public void setXX_VMR_VendorProdRef_ID (int XX_VMR_VendorProdRef_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_VendorProdRef_ID is virtual column");
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
        
    }
    
    
}
