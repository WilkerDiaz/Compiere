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
/** Generated Model for XX_VMR_OrderRequestDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_OrderRequestDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_OrderRequestDetail_ID id
    @param trx transaction
    */
    public X_XX_VMR_OrderRequestDetail (Ctx ctx, int XX_VMR_OrderRequestDetail_ID, Trx trx)
    {
        super (ctx, XX_VMR_OrderRequestDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_OrderRequestDetail_ID == 0)
        {
            setXX_VMR_ORDERREQUESTDETAIL_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_OrderRequestDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27597975413789L;
    /** Last Updated Timestamp 2011-09-12 13:24:57.0 */
    public static final long updatedMS = 1315850097000L;
    /** AD_Table_ID=1000152 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_OrderRequestDetail");
        
    }
    ;
    
    /** TableName=XX_VMR_OrderRequestDetail */
    public static final String Table_Name="XX_VMR_OrderRequestDetail";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        throw new IllegalArgumentException ("C_BPartner_ID is virtual column");
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
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
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        throw new IllegalArgumentException ("M_AttributeSetInstance_ID is virtual column");
        
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
    
    /** Set Quantity Reserved.
    @param QtyReserved Quantity Reserved */
    public void setQtyReserved (int QtyReserved)
    {
        set_Value ("QtyReserved", Integer.valueOf(QtyReserved));
        
    }
    
    /** Get Quantity Reserved.
    @return Quantity Reserved */
    public int getQtyReserved() 
    {
        return get_ValueAsInt("QtyReserved");
        
    }
    
    /** Set Correlative Generator.
    @param XX_CorrelativeGenerator Correlative Generator */
    public void setXX_CorrelativeGenerator (java.math.BigDecimal XX_CorrelativeGenerator)
    {
        set_Value ("XX_CorrelativeGenerator", XX_CorrelativeGenerator);
        
    }
    
    /** Get Correlative Generator.
    @return Correlative Generator */
    public java.math.BigDecimal getXX_CorrelativeGenerator() 
    {
        return get_ValueAsBigDecimal("XX_CorrelativeGenerator");
        
    }
    
    /** Set PriceConsecutive.
    @param XX_PriceConsecutive PriceConsecutive */
    public void setXX_PriceConsecutive (int XX_PriceConsecutive)
    {
        set_Value ("XX_PriceConsecutive", Integer.valueOf(XX_PriceConsecutive));
        
    }
    
    /** Get PriceConsecutive.
    @return PriceConsecutive */
    public int getXX_PriceConsecutive() 
    {
        return get_ValueAsInt("XX_PriceConsecutive");
        
    }
    
    /** Set Product Batch.
    @param XX_ProductBatch_ID Product Batch */
    public void setXX_ProductBatch_ID (int XX_ProductBatch_ID)
    {
        if (XX_ProductBatch_ID <= 0) set_Value ("XX_ProductBatch_ID", null);
        else
        set_Value ("XX_ProductBatch_ID", Integer.valueOf(XX_ProductBatch_ID));
        
    }
    
    /** Get Product Batch.
    @return Product Batch */
    public int getXX_ProductBatch_ID() 
    {
        return get_ValueAsInt("XX_ProductBatch_ID");
        
    }
    
    /** Set Product Quantity.
    @param XX_ProductQuantity Product Quantity */
    public void setXX_ProductQuantity (int XX_ProductQuantity)
    {
        set_Value ("XX_ProductQuantity", Integer.valueOf(XX_ProductQuantity));
        
    }
    
    /** Get Product Quantity.
    @return Product Quantity */
    public int getXX_ProductQuantity() 
    {
        return get_ValueAsInt("XX_ProductQuantity");
        
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
    
    /** Set Synchronized.
    @param XX_Synchronized Indica si el registro ya fue exportado */
    public void setXX_Synchronized (boolean XX_Synchronized)
    {
        set_Value ("XX_Synchronized", Boolean.valueOf(XX_Synchronized));
        
    }
    
    /** Get Synchronized.
    @return Indica si el registro ya fue exportado */
    public boolean isXX_Synchronized() 
    {
        return get_ValueAsBoolean("XX_Synchronized");
        
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
    
    /** Set Season.
    @param XX_VMA_Season_ID Identifier used for a Season. */
    public void setXX_VMA_Season_ID (int XX_VMA_Season_ID)
    {
        if (XX_VMA_Season_ID <= 0) set_Value ("XX_VMA_Season_ID", null);
        else
        set_Value ("XX_VMA_Season_ID", Integer.valueOf(XX_VMA_Season_ID));
        
    }
    
    /** Get Season.
    @return Identifier used for a Season. */
    public int getXX_VMA_Season_ID() 
    {
        return get_ValueAsInt("XX_VMA_Season_ID");
        
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
    
    /** Set Motive.
    @param XX_VMR_CancellationMotive_ID Motivo de cancelaciones, devoluciones */
    public void setXX_VMR_CancellationMotive_ID (int XX_VMR_CancellationMotive_ID)
    {
        if (XX_VMR_CancellationMotive_ID <= 0) set_Value ("XX_VMR_CancellationMotive_ID", null);
        else
        set_Value ("XX_VMR_CancellationMotive_ID", Integer.valueOf(XX_VMR_CancellationMotive_ID));
        
    }
    
    /** Get Motive.
    @return Motivo de cancelaciones, devoluciones */
    public int getXX_VMR_CancellationMotive_ID() 
    {
        return get_ValueAsInt("XX_VMR_CancellationMotive_ID");
        
    }
    
    /** Set Category.
    @param XX_VMR_Category_ID Category */
    public void setXX_VMR_Category_ID (int XX_VMR_Category_ID)
    {
        if (XX_VMR_Category_ID <= 0) set_Value ("XX_VMR_Category_ID", null);
        else
        set_Value ("XX_VMR_Category_ID", Integer.valueOf(XX_VMR_Category_ID));
        
    }
    
    /** Get Category.
    @return Category */
    public int getXX_VMR_Category_ID() 
    {
        return get_ValueAsInt("XX_VMR_Category_ID");
        
    }
    
    /** Set Collection.
    @param XX_VMR_Collection_ID ID de Colección */
    public void setXX_VMR_Collection_ID (int XX_VMR_Collection_ID)
    {
        if (XX_VMR_Collection_ID <= 0) set_Value ("XX_VMR_Collection_ID", null);
        else
        set_Value ("XX_VMR_Collection_ID", Integer.valueOf(XX_VMR_Collection_ID));
        
    }
    
    /** Get Collection.
    @return ID de Colección */
    public int getXX_VMR_Collection_ID() 
    {
        return get_ValueAsInt("XX_VMR_Collection_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID <= 0) set_Value ("XX_VMR_Department_ID", null);
        else
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set Line.
    @param XX_VMR_Line_ID Line */
    public void setXX_VMR_Line_ID (int XX_VMR_Line_ID)
    {
        if (XX_VMR_Line_ID <= 0) set_Value ("XX_VMR_Line_ID", null);
        else
        set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    /** Set Placed Order.
    @param XX_VMR_Order_ID Placed Order */
    public void setXX_VMR_Order_ID (int XX_VMR_Order_ID)
    {
        if (XX_VMR_Order_ID <= 0) set_ValueNoCheck ("XX_VMR_Order_ID", null);
        else
        set_ValueNoCheck ("XX_VMR_Order_ID", Integer.valueOf(XX_VMR_Order_ID));
        
    }
    
    /** Get Placed Order.
    @return Placed Order */
    public int getXX_VMR_Order_ID() 
    {
        return get_ValueAsInt("XX_VMR_Order_ID");
        
    }
    
    /** Set XX_VMR_ORDERREQUESTDETAIL_ID.
    @param XX_VMR_ORDERREQUESTDETAIL_ID XX_VMR_ORDERREQUESTDETAIL_ID */
    public void setXX_VMR_ORDERREQUESTDETAIL_ID (int XX_VMR_ORDERREQUESTDETAIL_ID)
    {
        if (XX_VMR_ORDERREQUESTDETAIL_ID < 1) throw new IllegalArgumentException ("XX_VMR_ORDERREQUESTDETAIL_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_ORDERREQUESTDETAIL_ID", Integer.valueOf(XX_VMR_ORDERREQUESTDETAIL_ID));
        
    }
    
    /** Get XX_VMR_ORDERREQUESTDETAIL_ID.
    @return XX_VMR_ORDERREQUESTDETAIL_ID */
    public int getXX_VMR_ORDERREQUESTDETAIL_ID() 
    {
        return get_ValueAsInt("XX_VMR_ORDERREQUESTDETAIL_ID");
        
    }
    
    /** Set Package.
    @param XX_VMR_Package_ID Package */
    public void setXX_VMR_Package_ID (int XX_VMR_Package_ID)
    {
        if (XX_VMR_Package_ID <= 0) set_Value ("XX_VMR_Package_ID", null);
        else
        set_Value ("XX_VMR_Package_ID", Integer.valueOf(XX_VMR_Package_ID));
        
    }
    
    /** Get Package.
    @return Package */
    public int getXX_VMR_Package_ID() 
    {
        return get_ValueAsInt("XX_VMR_Package_ID");
        
    }
    
    /** Set Season.
    @param XX_VMR_Season_ID Season */
    public void setXX_VMR_Season_ID (int XX_VMR_Season_ID)
    {
        if (XX_VMR_Season_ID <= 0) set_Value ("XX_VMR_Season_ID", null);
        else
        set_Value ("XX_VMR_Season_ID", Integer.valueOf(XX_VMR_Season_ID));
        
    }
    
    /** Get Season.
    @return Season */
    public int getXX_VMR_Season_ID() 
    {
        return get_ValueAsInt("XX_VMR_Season_ID");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        if (XX_VMR_Section_ID <= 0) set_Value ("XX_VMR_Section_ID", null);
        else
        set_Value ("XX_VMR_Section_ID", Integer.valueOf(XX_VMR_Section_ID));
        
    }
    
    /** Get Section.
    @return Section */
    public int getXX_VMR_Section_ID() 
    {
        return get_ValueAsInt("XX_VMR_Section_ID");
        
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
