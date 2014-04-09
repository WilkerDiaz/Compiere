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
/** Generated Model for XX_VLO_UnsolicitedProduct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_UnsolicitedProduct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_UnsolicitedProduct_ID id
    @param trx transaction
    */
    public X_XX_VLO_UnsolicitedProduct (Ctx ctx, int XX_VLO_UnsolicitedProduct_ID, Trx trx)
    {
        super (ctx, XX_VLO_UnsolicitedProduct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_UnsolicitedProduct_ID == 0)
        {
            setXX_VLO_UNSOLICITEDPRODUCT_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_UnsolicitedProduct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27572334570789L;
    /** Last Updated Timestamp 2010-11-19 18:57:34.0 */
    public static final long updatedMS = 1290209254000L;
    /** AD_Table_ID=1000240 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_UnsolicitedProduct");
        
    }
    ;
    
    /** TableName=XX_VLO_UnsolicitedProduct */
    public static final String Table_Name="XX_VLO_UnsolicitedProduct";
    
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
    
    /** Set Invoice Line.
    @param C_InvoiceLine_ID Invoice Detail Line */
    public void setC_InvoiceLine_ID (int C_InvoiceLine_ID)
    {
        if (C_InvoiceLine_ID <= 0) set_Value ("C_InvoiceLine_ID", null);
        else
        set_Value ("C_InvoiceLine_ID", Integer.valueOf(C_InvoiceLine_ID));
        
    }
    
    /** Get Invoice Line.
    @return Invoice Detail Line */
    public int getC_InvoiceLine_ID() 
    {
        return get_ValueAsInt("C_InvoiceLine_ID");
        
    }
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_Value ("C_Order_ID", null);
        else
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
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
    
    /** Set Shipment/Receipt.
    @param M_InOut_ID Material Shipment Document */
    public void setM_InOut_ID (int M_InOut_ID)
    {
        throw new IllegalArgumentException ("M_InOut_ID is virtual column");
        
    }
    
    /** Get Shipment/Receipt.
    @return Material Shipment Document */
    public int getM_InOut_ID() 
    {
        return get_ValueAsInt("M_InOut_ID");
        
    }
    
    /** Set Shipment/Receipt Line.
    @param M_InOutLine_ID Line on Shipment or Receipt document */
    public void setM_InOutLine_ID (int M_InOutLine_ID)
    {
        if (M_InOutLine_ID <= 0) set_Value ("M_InOutLine_ID", null);
        else
        set_Value ("M_InOutLine_ID", Integer.valueOf(M_InOutLine_ID));
        
    }
    
    /** Get Shipment/Receipt Line.
    @return Line on Shipment or Receipt document */
    public int getM_InOutLine_ID() 
    {
        return get_ValueAsInt("M_InOutLine_ID");
        
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
    
    /** Set Process Validate Product.
    @param ProcessValidateProduct Process Validate Product */
    public void setProcessValidateProduct (String ProcessValidateProduct)
    {
        set_Value ("ProcessValidateProduct", ProcessValidateProduct);
        
    }
    
    /** Get Process Validate Product.
    @return Process Validate Product */
    public String getProcessValidateProduct() 
    {
        return (String)get_Value("ProcessValidateProduct");
        
    }
    
    /** Set Product Key.
    @param ProductValue Key of the Product */
    public void setProductValue (String ProductValue)
    {
        throw new IllegalArgumentException ("ProductValue is virtual column");
        
    }
    
    /** Get Product Key.
    @return Key of the Product */
    public String getProductValue() 
    {
        return (String)get_Value("ProductValue");
        
    }
    
    /** Set Quantity.
    @param QtyEntered The Quantity Entered is based on the selected UoM */
    public void setQtyEntered (java.math.BigDecimal QtyEntered)
    {
        throw new IllegalArgumentException ("QtyEntered is virtual column");
        
    }
    
    /** Get Quantity.
    @return The Quantity Entered is based on the selected UoM */
    public java.math.BigDecimal getQtyEntered() 
    {
        return get_ValueAsBigDecimal("QtyEntered");
        
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
    
    /** Set Alert.
    @param XX_Alert Alert */
    public void setXX_Alert (boolean XX_Alert)
    {
        set_Value ("XX_Alert", Boolean.valueOf(XX_Alert));
        
    }
    
    /** Get Alert.
    @return Alert */
    public boolean isXX_Alert() 
    {
        return get_ValueAsBoolean("XX_Alert");
        
    }
    
    /** Set Validated Product.
    @param XX_Record_ID Validated Product */
    public void setXX_Record_ID (int XX_Record_ID)
    {
        if (XX_Record_ID <= 0) set_Value ("XX_Record_ID", null);
        else
        set_Value ("XX_Record_ID", Integer.valueOf(XX_Record_ID));
        
    }
    
    /** Get Validated Product.
    @return Validated Product */
    public int getXX_Record_ID() 
    {
        return get_ValueAsInt("XX_Record_ID");
        
    }
    
    /** Set XX_Validate.
    @param XX_Validate XX_Validate */
    public void setXX_Validate (String XX_Validate)
    {
        throw new IllegalArgumentException ("XX_Validate is virtual column");
        
    }
    
    /** Get XX_Validate.
    @return XX_Validate */
    public String getXX_Validate() 
    {
        return (String)get_Value("XX_Validate");
        
    }
    
    /** Set Validated Product.
    @param XX_ValidateProduct Indicates that the product ordered has been validated. */
    public void setXX_ValidateProduct (boolean XX_ValidateProduct)
    {
        set_Value ("XX_ValidateProduct", Boolean.valueOf(XX_ValidateProduct));
        
    }
    
    /** Get Validated Product.
    @return Indicates that the product ordered has been validated. */
    public boolean isXX_ValidateProduct() 
    {
        return get_ValueAsBoolean("XX_ValidateProduct");
        
    }
    
    /** Set XX_VLO_UNSOLICITEDPRODUCT_ID.
    @param XX_VLO_UNSOLICITEDPRODUCT_ID XX_VLO_UNSOLICITEDPRODUCT_ID */
    public void setXX_VLO_UNSOLICITEDPRODUCT_ID (int XX_VLO_UNSOLICITEDPRODUCT_ID)
    {
        if (XX_VLO_UNSOLICITEDPRODUCT_ID < 1) throw new IllegalArgumentException ("XX_VLO_UNSOLICITEDPRODUCT_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_UNSOLICITEDPRODUCT_ID", Integer.valueOf(XX_VLO_UNSOLICITEDPRODUCT_ID));
        
    }
    
    /** Get XX_VLO_UNSOLICITEDPRODUCT_ID.
    @return XX_VLO_UNSOLICITEDPRODUCT_ID */
    public int getXX_VLO_UNSOLICITEDPRODUCT_ID() 
    {
        return get_ValueAsInt("XX_VLO_UNSOLICITEDPRODUCT_ID");
        
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
    
    /** Set Line.
    @param XX_VMR_Line_ID Line */
    public void setXX_VMR_Line_ID (int XX_VMR_Line_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_Line_ID is virtual column");
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    /** Set Main Characteristic.
    @param XX_VMR_LongCharacteristic_ID Main Characteristic */
    public void setXX_VMR_LongCharacteristic_ID (int XX_VMR_LongCharacteristic_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_LongCharacteristic_ID is virtual column");
        
    }
    
    /** Get Main Characteristic.
    @return Main Characteristic */
    public int getXX_VMR_LongCharacteristic_ID() 
    {
        return get_ValueAsInt("XX_VMR_LongCharacteristic_ID");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_Section_ID is virtual column");
        
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
