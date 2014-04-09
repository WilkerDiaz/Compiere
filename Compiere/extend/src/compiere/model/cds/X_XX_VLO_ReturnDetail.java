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
/** Generated Model for XX_VLO_ReturnDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_ReturnDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_ReturnDetail_ID id
    @param trx transaction
    */
    public X_XX_VLO_ReturnDetail (Ctx ctx, int XX_VLO_ReturnDetail_ID, Trx trx)
    {
        super (ctx, XX_VLO_ReturnDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_ReturnDetail_ID == 0)
        {
            setM_Product_ID (0);
            setXX_TotalPieces (0);
            setXX_VLO_RETURNDETAIL_ID (0);
            setXX_VLO_ReturnOfProduct_ID (0);	// @XX_VLO_ReturnOfProduct_ID@
            setXX_VMR_CancellationMotive_ID (0);	// @#XX_VMR_CancellationMotive_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_ReturnDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27594356346789L;
    /** Last Updated Timestamp 2011-08-01 16:07:10.0 */
    public static final long updatedMS = 1312231030000L;
    /** AD_Table_ID=1000238 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_ReturnDetail");
        
    }
    ;
    
    /** TableName=XX_VLO_ReturnDetail */
    public static final String Table_Name="XX_VLO_ReturnDetail";
    
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
    
    /** Set Tax Amount.
    @param TaxAmt Tax Amount for a document */
    public void setTaxAmt (java.math.BigDecimal TaxAmt)
    {
        set_Value ("TaxAmt", TaxAmt);
        
    }
    
    /** Get Tax Amount.
    @return Tax Amount for a document */
    public java.math.BigDecimal getTaxAmt() 
    {
        return get_ValueAsBigDecimal("TaxAmt");
        
    }
    
    /** Set TOTALCOST.
    @param TOTALCOST TOTALCOST */
    public void setTOTALCOST (java.math.BigDecimal TOTALCOST)
    {
        throw new IllegalArgumentException ("TOTALCOST is virtual column");
        
    }
    
    /** Get TOTALCOST.
    @return TOTALCOST */
    public java.math.BigDecimal getTOTALCOST() 
    {
        return get_ValueAsBigDecimal("TOTALCOST");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Total Pieces Returned.
    @param XX_TotalPieces Total Pieces Returned */
    public void setXX_TotalPieces (int XX_TotalPieces)
    {
        set_Value ("XX_TotalPieces", Integer.valueOf(XX_TotalPieces));
        
    }
    
    /** Get Total Pieces Returned.
    @return Total Pieces Returned */
    public int getXX_TotalPieces() 
    {
        return get_ValueAsInt("XX_TotalPieces");
        
    }
    
    /** Set XX_VLO_RETURNDETAIL_ID.
    @param XX_VLO_RETURNDETAIL_ID XX_VLO_RETURNDETAIL_ID */
    public void setXX_VLO_RETURNDETAIL_ID (int XX_VLO_RETURNDETAIL_ID)
    {
        if (XX_VLO_RETURNDETAIL_ID < 1) throw new IllegalArgumentException ("XX_VLO_RETURNDETAIL_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_RETURNDETAIL_ID", Integer.valueOf(XX_VLO_RETURNDETAIL_ID));
        
    }
    
    /** Get XX_VLO_RETURNDETAIL_ID.
    @return XX_VLO_RETURNDETAIL_ID */
    public int getXX_VLO_RETURNDETAIL_ID() 
    {
        return get_ValueAsInt("XX_VLO_RETURNDETAIL_ID");
        
    }
    
    /** Set Nro. Return (ID).
    @param XX_VLO_ReturnOfProduct_ID Id Return Of Product */
    public void setXX_VLO_ReturnOfProduct_ID (int XX_VLO_ReturnOfProduct_ID)
    {
        if (XX_VLO_ReturnOfProduct_ID < 1) throw new IllegalArgumentException ("XX_VLO_ReturnOfProduct_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_ReturnOfProduct_ID", Integer.valueOf(XX_VLO_ReturnOfProduct_ID));
        
    }
    
    /** Get Nro. Return (ID).
    @return Id Return Of Product */
    public int getXX_VLO_ReturnOfProduct_ID() 
    {
        return get_ValueAsInt("XX_VLO_ReturnOfProduct_ID");
        
    }
    
    /** Set Motive.
    @param XX_VMR_CancellationMotive_ID Motivo de cancelaciones, devoluciones */
    public void setXX_VMR_CancellationMotive_ID (int XX_VMR_CancellationMotive_ID)
    {
        if (XX_VMR_CancellationMotive_ID < 1) throw new IllegalArgumentException ("XX_VMR_CancellationMotive_ID is mandatory.");
        set_Value ("XX_VMR_CancellationMotive_ID", Integer.valueOf(XX_VMR_CancellationMotive_ID));
        
    }
    
    /** Get Motive.
    @return Motivo de cancelaciones, devoluciones */
    public int getXX_VMR_CancellationMotive_ID() 
    {
        return get_ValueAsInt("XX_VMR_CancellationMotive_ID");
        
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
