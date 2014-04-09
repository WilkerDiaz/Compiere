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
/** Generated Model for XX_VMR_PO_LineRefProv
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_PO_LineRefProv extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_PO_LineRefProv_ID id
    @param trx transaction
    */
    public X_XX_VMR_PO_LineRefProv (Ctx ctx, int XX_VMR_PO_LineRefProv_ID, Trx trx)
    {
        super (ctx, XX_VMR_PO_LineRefProv_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_PO_LineRefProv_ID == 0)
        {
            setC_Order_ID (0);
            setLine (Env.ZERO);
            setXX_Margin (Env.ZERO);
            setXX_PackageMultiple (0);	// 1
            setXX_SaleUnit_ID (0);
            setXX_UnitPurchasePrice (Env.ZERO);
            setXX_VMR_PO_LineRefProv_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_PO_LineRefProv (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27659411441789L;
    /** Last Updated Timestamp 2013-08-23 14:58:45.0 */
    public static final long updatedMS = 1377286125000L;
    /** AD_Table_ID=1000113 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_PO_LineRefProv");
        
    }
    ;
    
    /** TableName=XX_VMR_PO_LineRefProv */
    public static final String Table_Name="XX_VMR_PO_LineRefProv";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID < 1) throw new IllegalArgumentException ("C_Order_ID is mandatory.");
        set_ValueNoCheck ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
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
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (java.math.BigDecimal Line)
    {
        if (Line == null) throw new IllegalArgumentException ("Line is mandatory.");
        set_Value ("Line", Line);
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public java.math.BigDecimal getLine() 
    {
        return get_ValueAsBigDecimal("Line");
        
    }
    
    /** Set Line Amount.
    @param LineNetAmt Line Extended Amount (Quantity * Actual Price) without Freight and Charges */
    public void setLineNetAmt (java.math.BigDecimal LineNetAmt)
    {
        set_Value ("LineNetAmt", LineNetAmt);
        
    }
    
    /** Get Line Amount.
    @return Line Extended Amount (Quantity * Actual Price) without Freight and Charges */
    public java.math.BigDecimal getLineNetAmt() 
    {
        return get_ValueAsBigDecimal("LineNetAmt");
        
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
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (int Qty)
    {
        set_Value ("Qty", Integer.valueOf(Qty));
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public int getQty() 
    {
        return get_ValueAsInt("Qty");
        
    }
    
    /** Set Sale Quantity.
    @param SaleQty Sale Quantity */
    public void setSaleQty (int SaleQty)
    {
        set_Value ("SaleQty", Integer.valueOf(SaleQty));
        
    }
    
    /** Get Sale Quantity.
    @return Sale Quantity */
    public int getSaleQty() 
    {
        return get_ValueAsInt("SaleQty");
        
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
    
    /** Set Associated Product.
    @param XX_AssociatedProduct Associated Product */
    public void setXX_AssociatedProduct (int XX_AssociatedProduct)
    {
        set_Value ("XX_AssociatedProduct", Integer.valueOf(XX_AssociatedProduct));
        
    }
    
    /** Get Associated Product.
    @return Associated Product */
    public int getXX_AssociatedProduct() 
    {
        return get_ValueAsInt("XX_AssociatedProduct");
        
    }
    
    /** Set See/Associate Reference.
    @param XX_AssociateReference Asociar una referencia sin caracteristica a un producto BECO */
    public void setXX_AssociateReference (String XX_AssociateReference)
    {
        set_Value ("XX_AssociateReference", XX_AssociateReference);
        
    }
    
    /** Get See/Associate Reference.
    @return Asociar una referencia sin caracteristica a un producto BECO */
    public String getXX_AssociateReference() 
    {
        return (String)get_Value("XX_AssociateReference");
        
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
    
    /** Set Characteristic 1.
    @param XX_Characteristic1_ID Characteristic 1 */
    public void setXX_Characteristic1_ID (int XX_Characteristic1_ID)
    {
        if (XX_Characteristic1_ID <= 0) set_Value ("XX_Characteristic1_ID", null);
        else
        set_Value ("XX_Characteristic1_ID", Integer.valueOf(XX_Characteristic1_ID));
        
    }
    
    /** Get Characteristic 1.
    @return Characteristic 1 */
    public int getXX_Characteristic1_ID() 
    {
        return get_ValueAsInt("XX_Characteristic1_ID");
        
    }
    
    /** Set Value 1.
    @param XX_Characteristic1Value1_ID Value 1 */
    public void setXX_Characteristic1Value1_ID (int XX_Characteristic1Value1_ID)
    {
        if (XX_Characteristic1Value1_ID <= 0) set_Value ("XX_Characteristic1Value1_ID", null);
        else
        set_Value ("XX_Characteristic1Value1_ID", Integer.valueOf(XX_Characteristic1Value1_ID));
        
    }
    
    /** Get Value 1.
    @return Value 1 */
    public int getXX_Characteristic1Value1_ID() 
    {
        return get_ValueAsInt("XX_Characteristic1Value1_ID");
        
    }
    
    /** Set Value 10.
    @param XX_Characteristic1Value10_ID Value 10 */
    public void setXX_Characteristic1Value10_ID (int XX_Characteristic1Value10_ID)
    {
        if (XX_Characteristic1Value10_ID <= 0) set_Value ("XX_Characteristic1Value10_ID", null);
        else
        set_Value ("XX_Characteristic1Value10_ID", Integer.valueOf(XX_Characteristic1Value10_ID));
        
    }
    
    /** Get Value 10.
    @return Value 10 */
    public int getXX_Characteristic1Value10_ID() 
    {
        return get_ValueAsInt("XX_Characteristic1Value10_ID");
        
    }
    
    /** Set Value 2.
    @param XX_Characteristic1Value2_ID Value 2 */
    public void setXX_Characteristic1Value2_ID (int XX_Characteristic1Value2_ID)
    {
        if (XX_Characteristic1Value2_ID <= 0) set_Value ("XX_Characteristic1Value2_ID", null);
        else
        set_Value ("XX_Characteristic1Value2_ID", Integer.valueOf(XX_Characteristic1Value2_ID));
        
    }
    
    /** Get Value 2.
    @return Value 2 */
    public int getXX_Characteristic1Value2_ID() 
    {
        return get_ValueAsInt("XX_Characteristic1Value2_ID");
        
    }
    
    /** Set Value 3.
    @param XX_Characteristic1Value3_ID Value 3 */
    public void setXX_Characteristic1Value3_ID (int XX_Characteristic1Value3_ID)
    {
        if (XX_Characteristic1Value3_ID <= 0) set_Value ("XX_Characteristic1Value3_ID", null);
        else
        set_Value ("XX_Characteristic1Value3_ID", Integer.valueOf(XX_Characteristic1Value3_ID));
        
    }
    
    /** Get Value 3.
    @return Value 3 */
    public int getXX_Characteristic1Value3_ID() 
    {
        return get_ValueAsInt("XX_Characteristic1Value3_ID");
        
    }
    
    /** Set Value 4.
    @param XX_Characteristic1Value4_ID Value 4 */
    public void setXX_Characteristic1Value4_ID (int XX_Characteristic1Value4_ID)
    {
        if (XX_Characteristic1Value4_ID <= 0) set_Value ("XX_Characteristic1Value4_ID", null);
        else
        set_Value ("XX_Characteristic1Value4_ID", Integer.valueOf(XX_Characteristic1Value4_ID));
        
    }
    
    /** Get Value 4.
    @return Value 4 */
    public int getXX_Characteristic1Value4_ID() 
    {
        return get_ValueAsInt("XX_Characteristic1Value4_ID");
        
    }
    
    /** Set Value 5.
    @param XX_Characteristic1Value5_ID Value 5 */
    public void setXX_Characteristic1Value5_ID (int XX_Characteristic1Value5_ID)
    {
        if (XX_Characteristic1Value5_ID <= 0) set_Value ("XX_Characteristic1Value5_ID", null);
        else
        set_Value ("XX_Characteristic1Value5_ID", Integer.valueOf(XX_Characteristic1Value5_ID));
        
    }
    
    /** Get Value 5.
    @return Value 5 */
    public int getXX_Characteristic1Value5_ID() 
    {
        return get_ValueAsInt("XX_Characteristic1Value5_ID");
        
    }
    
    /** Set Value 6.
    @param XX_Characteristic1Value6_ID Value 6 */
    public void setXX_Characteristic1Value6_ID (int XX_Characteristic1Value6_ID)
    {
        if (XX_Characteristic1Value6_ID <= 0) set_Value ("XX_Characteristic1Value6_ID", null);
        else
        set_Value ("XX_Characteristic1Value6_ID", Integer.valueOf(XX_Characteristic1Value6_ID));
        
    }
    
    /** Get Value 6.
    @return Value 6 */
    public int getXX_Characteristic1Value6_ID() 
    {
        return get_ValueAsInt("XX_Characteristic1Value6_ID");
        
    }
    
    /** Set Value 7.
    @param XX_Characteristic1Value7_ID Value 7 */
    public void setXX_Characteristic1Value7_ID (int XX_Characteristic1Value7_ID)
    {
        if (XX_Characteristic1Value7_ID <= 0) set_Value ("XX_Characteristic1Value7_ID", null);
        else
        set_Value ("XX_Characteristic1Value7_ID", Integer.valueOf(XX_Characteristic1Value7_ID));
        
    }
    
    /** Get Value 7.
    @return Value 7 */
    public int getXX_Characteristic1Value7_ID() 
    {
        return get_ValueAsInt("XX_Characteristic1Value7_ID");
        
    }
    
    /** Set Value 8.
    @param XX_Characteristic1Value8_ID Value 8 */
    public void setXX_Characteristic1Value8_ID (int XX_Characteristic1Value8_ID)
    {
        if (XX_Characteristic1Value8_ID <= 0) set_Value ("XX_Characteristic1Value8_ID", null);
        else
        set_Value ("XX_Characteristic1Value8_ID", Integer.valueOf(XX_Characteristic1Value8_ID));
        
    }
    
    /** Get Value 8.
    @return Value 8 */
    public int getXX_Characteristic1Value8_ID() 
    {
        return get_ValueAsInt("XX_Characteristic1Value8_ID");
        
    }
    
    /** Set Value 9.
    @param XX_Characteristic1Value9_ID Value 9 */
    public void setXX_Characteristic1Value9_ID (int XX_Characteristic1Value9_ID)
    {
        if (XX_Characteristic1Value9_ID <= 0) set_Value ("XX_Characteristic1Value9_ID", null);
        else
        set_Value ("XX_Characteristic1Value9_ID", Integer.valueOf(XX_Characteristic1Value9_ID));
        
    }
    
    /** Get Value 9.
    @return Value 9 */
    public int getXX_Characteristic1Value9_ID() 
    {
        return get_ValueAsInt("XX_Characteristic1Value9_ID");
        
    }
    
    /** Set Characteristic 2.
    @param XX_Characteristic2_ID Characteristic 2 */
    public void setXX_Characteristic2_ID (int XX_Characteristic2_ID)
    {
        if (XX_Characteristic2_ID <= 0) set_Value ("XX_Characteristic2_ID", null);
        else
        set_Value ("XX_Characteristic2_ID", Integer.valueOf(XX_Characteristic2_ID));
        
    }
    
    /** Get Characteristic 2.
    @return Characteristic 2 */
    public int getXX_Characteristic2_ID() 
    {
        return get_ValueAsInt("XX_Characteristic2_ID");
        
    }
    
    /** Set Value 1.
    @param XX_Characteristic2Value1_ID Value 1 */
    public void setXX_Characteristic2Value1_ID (int XX_Characteristic2Value1_ID)
    {
        if (XX_Characteristic2Value1_ID <= 0) set_Value ("XX_Characteristic2Value1_ID", null);
        else
        set_Value ("XX_Characteristic2Value1_ID", Integer.valueOf(XX_Characteristic2Value1_ID));
        
    }
    
    /** Get Value 1.
    @return Value 1 */
    public int getXX_Characteristic2Value1_ID() 
    {
        return get_ValueAsInt("XX_Characteristic2Value1_ID");
        
    }
    
    /** Set Value10.
    @param XX_Characteristic2Value10_ID Value10 */
    public void setXX_Characteristic2Value10_ID (int XX_Characteristic2Value10_ID)
    {
        if (XX_Characteristic2Value10_ID <= 0) set_Value ("XX_Characteristic2Value10_ID", null);
        else
        set_Value ("XX_Characteristic2Value10_ID", Integer.valueOf(XX_Characteristic2Value10_ID));
        
    }
    
    /** Get Value10.
    @return Value10 */
    public int getXX_Characteristic2Value10_ID() 
    {
        return get_ValueAsInt("XX_Characteristic2Value10_ID");
        
    }
    
    /** Set Value 2.
    @param XX_Characteristic2Value2_ID Value 2 */
    public void setXX_Characteristic2Value2_ID (int XX_Characteristic2Value2_ID)
    {
        if (XX_Characteristic2Value2_ID <= 0) set_Value ("XX_Characteristic2Value2_ID", null);
        else
        set_Value ("XX_Characteristic2Value2_ID", Integer.valueOf(XX_Characteristic2Value2_ID));
        
    }
    
    /** Get Value 2.
    @return Value 2 */
    public int getXX_Characteristic2Value2_ID() 
    {
        return get_ValueAsInt("XX_Characteristic2Value2_ID");
        
    }
    
    /** Set Value 3.
    @param XX_Characteristic2Value3_ID Value 3 */
    public void setXX_Characteristic2Value3_ID (int XX_Characteristic2Value3_ID)
    {
        if (XX_Characteristic2Value3_ID <= 0) set_Value ("XX_Characteristic2Value3_ID", null);
        else
        set_Value ("XX_Characteristic2Value3_ID", Integer.valueOf(XX_Characteristic2Value3_ID));
        
    }
    
    /** Get Value 3.
    @return Value 3 */
    public int getXX_Characteristic2Value3_ID() 
    {
        return get_ValueAsInt("XX_Characteristic2Value3_ID");
        
    }
    
    /** Set Value4.
    @param XX_Characteristic2Value4_ID Value4 */
    public void setXX_Characteristic2Value4_ID (int XX_Characteristic2Value4_ID)
    {
        if (XX_Characteristic2Value4_ID <= 0) set_Value ("XX_Characteristic2Value4_ID", null);
        else
        set_Value ("XX_Characteristic2Value4_ID", Integer.valueOf(XX_Characteristic2Value4_ID));
        
    }
    
    /** Get Value4.
    @return Value4 */
    public int getXX_Characteristic2Value4_ID() 
    {
        return get_ValueAsInt("XX_Characteristic2Value4_ID");
        
    }
    
    /** Set Value 5.
    @param XX_Characteristic2Value5_ID Value 5 */
    public void setXX_Characteristic2Value5_ID (int XX_Characteristic2Value5_ID)
    {
        if (XX_Characteristic2Value5_ID <= 0) set_Value ("XX_Characteristic2Value5_ID", null);
        else
        set_Value ("XX_Characteristic2Value5_ID", Integer.valueOf(XX_Characteristic2Value5_ID));
        
    }
    
    /** Get Value 5.
    @return Value 5 */
    public int getXX_Characteristic2Value5_ID() 
    {
        return get_ValueAsInt("XX_Characteristic2Value5_ID");
        
    }
    
    /** Set Value 6.
    @param XX_Characteristic2Value6_ID Value 6 */
    public void setXX_Characteristic2Value6_ID (int XX_Characteristic2Value6_ID)
    {
        if (XX_Characteristic2Value6_ID <= 0) set_Value ("XX_Characteristic2Value6_ID", null);
        else
        set_Value ("XX_Characteristic2Value6_ID", Integer.valueOf(XX_Characteristic2Value6_ID));
        
    }
    
    /** Get Value 6.
    @return Value 6 */
    public int getXX_Characteristic2Value6_ID() 
    {
        return get_ValueAsInt("XX_Characteristic2Value6_ID");
        
    }
    
    /** Set Value 7.
    @param XX_Characteristic2Value7_ID Value 7 */
    public void setXX_Characteristic2Value7_ID (int XX_Characteristic2Value7_ID)
    {
        if (XX_Characteristic2Value7_ID <= 0) set_Value ("XX_Characteristic2Value7_ID", null);
        else
        set_Value ("XX_Characteristic2Value7_ID", Integer.valueOf(XX_Characteristic2Value7_ID));
        
    }
    
    /** Get Value 7.
    @return Value 7 */
    public int getXX_Characteristic2Value7_ID() 
    {
        return get_ValueAsInt("XX_Characteristic2Value7_ID");
        
    }
    
    /** Set Characteristic 2 Value 8.
    @param XX_Characteristic2Value8_ID Characteristic 2 Value 8 */
    public void setXX_Characteristic2Value8_ID (int XX_Characteristic2Value8_ID)
    {
        if (XX_Characteristic2Value8_ID <= 0) set_Value ("XX_Characteristic2Value8_ID", null);
        else
        set_Value ("XX_Characteristic2Value8_ID", Integer.valueOf(XX_Characteristic2Value8_ID));
        
    }
    
    /** Get Characteristic 2 Value 8.
    @return Characteristic 2 Value 8 */
    public int getXX_Characteristic2Value8_ID() 
    {
        return get_ValueAsInt("XX_Characteristic2Value8_ID");
        
    }
    
    /** Set Value 9.
    @param XX_Characteristic2Value9_ID Value 9 */
    public void setXX_Characteristic2Value9_ID (int XX_Characteristic2Value9_ID)
    {
        if (XX_Characteristic2Value9_ID <= 0) set_Value ("XX_Characteristic2Value9_ID", null);
        else
        set_Value ("XX_Characteristic2Value9_ID", Integer.valueOf(XX_Characteristic2Value9_ID));
        
    }
    
    /** Get Value 9.
    @return Value 9 */
    public int getXX_Characteristic2Value9_ID() 
    {
        return get_ValueAsInt("XX_Characteristic2Value9_ID");
        
    }
    
    /** Set XX_ComesFromExcel.
    @param XX_ComesFromExcel XX_ComesFromExcel */
    public void setXX_ComesFromExcel (boolean XX_ComesFromExcel)
    {
        set_Value ("XX_ComesFromExcel", Boolean.valueOf(XX_ComesFromExcel));
        
    }
    
    /** Get XX_ComesFromExcel.
    @return XX_ComesFromExcel */
    public boolean isXX_ComesFromExcel() 
    {
        return get_ValueAsBoolean("XX_ComesFromExcel");
        
    }
    
    /** Set XX_CostPerOrder.
    @param XX_CostPerOrder XX_CostPerOrder */
    public void setXX_CostPerOrder (java.math.BigDecimal XX_CostPerOrder)
    {
        set_Value ("XX_CostPerOrder", XX_CostPerOrder);
        
    }
    
    /** Get XX_CostPerOrder.
    @return XX_CostPerOrder */
    public java.math.BigDecimal getXX_CostPerOrder() 
    {
        return get_ValueAsBigDecimal("XX_CostPerOrder");
        
    }
    
    /** Set Cost With Discounts.
    @param XX_CostWithDiscounts Cost With Discounts */
    public void setXX_CostWithDiscounts (java.math.BigDecimal XX_CostWithDiscounts)
    {
        set_Value ("XX_CostWithDiscounts", XX_CostWithDiscounts);
        
    }
    
    /** Get Cost With Discounts.
    @return Cost With Discounts */
    public java.math.BigDecimal getXX_CostWithDiscounts() 
    {
        return get_ValueAsBigDecimal("XX_CostWithDiscounts");
        
    }
    
    /** Set Delete Matrix.
    @param XX_DeleteMatrix ¿Está seguro que desea borrar la matriz de características dinámicas? */
    public void setXX_DeleteMatrix (String XX_DeleteMatrix)
    {
        set_Value ("XX_DeleteMatrix", XX_DeleteMatrix);
        
    }
    
    /** Get Delete Matrix.
    @return ¿Está seguro que desea borrar la matriz de características dinámicas? */
    public String getXX_DeleteMatrix() 
    {
        return (String)get_Value("XX_DeleteMatrix");
        
    }
    
    /** Set Generate Matrix.
    @param XX_GenerateMatrix Generate Matrix */
    public void setXX_GenerateMatrix (String XX_GenerateMatrix)
    {
        set_Value ("XX_GenerateMatrix", XX_GenerateMatrix);
        
    }
    
    /** Get Generate Matrix.
    @return Generate Matrix */
    public String getXX_GenerateMatrix() 
    {
        return (String)get_Value("XX_GenerateMatrix");
        
    }
    
    /** Set Gifts Quantity.
    @param XX_GiftsQty Gifts Quantity */
    public void setXX_GiftsQty (int XX_GiftsQty)
    {
        set_Value ("XX_GiftsQty", Integer.valueOf(XX_GiftsQty));
        
    }
    
    /** Get Gifts Quantity.
    @return Gifts Quantity */
    public int getXX_GiftsQty() 
    {
        return get_ValueAsInt("XX_GiftsQty");
        
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
    
    /** Set Characteristic 1 Value 1 Is Generated.
    @param XX_IsGeneratedCharac1Value1 Characteristic 1 Value 1 Is Generated */
    public void setXX_IsGeneratedCharac1Value1 (boolean XX_IsGeneratedCharac1Value1)
    {
        set_Value ("XX_IsGeneratedCharac1Value1", Boolean.valueOf(XX_IsGeneratedCharac1Value1));
        
    }
    
    /** Get Characteristic 1 Value 1 Is Generated.
    @return Characteristic 1 Value 1 Is Generated */
    public boolean isXX_IsGeneratedCharac1Value1() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac1Value1");
        
    }
    
    /** Set Characteristic 1 Value 10 Is Generated.
    @param XX_IsGeneratedCharac1Value10 Characteristic 1 Value 10 Is Generated */
    public void setXX_IsGeneratedCharac1Value10 (boolean XX_IsGeneratedCharac1Value10)
    {
        set_Value ("XX_IsGeneratedCharac1Value10", Boolean.valueOf(XX_IsGeneratedCharac1Value10));
        
    }
    
    /** Get Characteristic 1 Value 10 Is Generated.
    @return Characteristic 1 Value 10 Is Generated */
    public boolean isXX_IsGeneratedCharac1Value10() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac1Value10");
        
    }
    
    /** Set Characteristic 1 Value 2 Is Generated.
    @param XX_IsGeneratedCharac1Value2 Characteristic 1 Value 2 Is Generated */
    public void setXX_IsGeneratedCharac1Value2 (boolean XX_IsGeneratedCharac1Value2)
    {
        set_Value ("XX_IsGeneratedCharac1Value2", Boolean.valueOf(XX_IsGeneratedCharac1Value2));
        
    }
    
    /** Get Characteristic 1 Value 2 Is Generated.
    @return Characteristic 1 Value 2 Is Generated */
    public boolean isXX_IsGeneratedCharac1Value2() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac1Value2");
        
    }
    
    /** Set Characteristic 1 Value 3 Is Generated.
    @param XX_IsGeneratedCharac1Value3 Characteristic 1 Value 3 Is Generated */
    public void setXX_IsGeneratedCharac1Value3 (boolean XX_IsGeneratedCharac1Value3)
    {
        set_Value ("XX_IsGeneratedCharac1Value3", Boolean.valueOf(XX_IsGeneratedCharac1Value3));
        
    }
    
    /** Get Characteristic 1 Value 3 Is Generated.
    @return Characteristic 1 Value 3 Is Generated */
    public boolean isXX_IsGeneratedCharac1Value3() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac1Value3");
        
    }
    
    /** Set Characteristic 1 Value 4 Is Generated.
    @param XX_IsGeneratedCharac1Value4 Characteristic 1 Value 4 Is Generated */
    public void setXX_IsGeneratedCharac1Value4 (boolean XX_IsGeneratedCharac1Value4)
    {
        set_Value ("XX_IsGeneratedCharac1Value4", Boolean.valueOf(XX_IsGeneratedCharac1Value4));
        
    }
    
    /** Get Characteristic 1 Value 4 Is Generated.
    @return Characteristic 1 Value 4 Is Generated */
    public boolean isXX_IsGeneratedCharac1Value4() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac1Value4");
        
    }
    
    /** Set Characteristic 1 Value 5 Is Generated.
    @param XX_IsGeneratedCharac1Value5 Characteristic 1 Value 5 Is Generated */
    public void setXX_IsGeneratedCharac1Value5 (boolean XX_IsGeneratedCharac1Value5)
    {
        set_Value ("XX_IsGeneratedCharac1Value5", Boolean.valueOf(XX_IsGeneratedCharac1Value5));
        
    }
    
    /** Get Characteristic 1 Value 5 Is Generated.
    @return Characteristic 1 Value 5 Is Generated */
    public boolean isXX_IsGeneratedCharac1Value5() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac1Value5");
        
    }
    
    /** Set Characteristic 1 Value 6 Is Generated.
    @param XX_IsGeneratedCharac1Value6 Characteristic 1 Value 6 Is Generated */
    public void setXX_IsGeneratedCharac1Value6 (boolean XX_IsGeneratedCharac1Value6)
    {
        set_Value ("XX_IsGeneratedCharac1Value6", Boolean.valueOf(XX_IsGeneratedCharac1Value6));
        
    }
    
    /** Get Characteristic 1 Value 6 Is Generated.
    @return Characteristic 1 Value 6 Is Generated */
    public boolean isXX_IsGeneratedCharac1Value6() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac1Value6");
        
    }
    
    /** Set Characteristic 1 Value 7 Is Generated.
    @param XX_IsGeneratedCharac1Value7 Characteristic 1 Value 7 Is Generated */
    public void setXX_IsGeneratedCharac1Value7 (boolean XX_IsGeneratedCharac1Value7)
    {
        set_Value ("XX_IsGeneratedCharac1Value7", Boolean.valueOf(XX_IsGeneratedCharac1Value7));
        
    }
    
    /** Get Characteristic 1 Value 7 Is Generated.
    @return Characteristic 1 Value 7 Is Generated */
    public boolean isXX_IsGeneratedCharac1Value7() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac1Value7");
        
    }
    
    /** Set Characteristic 1 Value 8 Is Generated.
    @param XX_IsGeneratedCharac1Value8 Characteristic 1 Value 8 Is Generated */
    public void setXX_IsGeneratedCharac1Value8 (boolean XX_IsGeneratedCharac1Value8)
    {
        set_Value ("XX_IsGeneratedCharac1Value8", Boolean.valueOf(XX_IsGeneratedCharac1Value8));
        
    }
    
    /** Get Characteristic 1 Value 8 Is Generated.
    @return Characteristic 1 Value 8 Is Generated */
    public boolean isXX_IsGeneratedCharac1Value8() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac1Value8");
        
    }
    
    /** Set Characteristic 1 Value 9 Is Generated.
    @param XX_IsGeneratedCharac1Value9 Characteristic 1 Value 9 Is Generated */
    public void setXX_IsGeneratedCharac1Value9 (boolean XX_IsGeneratedCharac1Value9)
    {
        set_Value ("XX_IsGeneratedCharac1Value9", Boolean.valueOf(XX_IsGeneratedCharac1Value9));
        
    }
    
    /** Get Characteristic 1 Value 9 Is Generated.
    @return Characteristic 1 Value 9 Is Generated */
    public boolean isXX_IsGeneratedCharac1Value9() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac1Value9");
        
    }
    
    /** Set IsGeneratedCharac2Value1.
    @param XX_IsGeneratedCharac2Value1 IsGeneratedCharac2Value1 */
    public void setXX_IsGeneratedCharac2Value1 (boolean XX_IsGeneratedCharac2Value1)
    {
        set_Value ("XX_IsGeneratedCharac2Value1", Boolean.valueOf(XX_IsGeneratedCharac2Value1));
        
    }
    
    /** Get IsGeneratedCharac2Value1.
    @return IsGeneratedCharac2Value1 */
    public boolean isXX_IsGeneratedCharac2Value1() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac2Value1");
        
    }
    
    /** Set Characteristic 2 Value 10 Is Generated.
    @param XX_IsGeneratedCharac2Value10 Characteristic 2 Value 10 Is Generated */
    public void setXX_IsGeneratedCharac2Value10 (boolean XX_IsGeneratedCharac2Value10)
    {
        set_Value ("XX_IsGeneratedCharac2Value10", Boolean.valueOf(XX_IsGeneratedCharac2Value10));
        
    }
    
    /** Get Characteristic 2 Value 10 Is Generated.
    @return Characteristic 2 Value 10 Is Generated */
    public boolean isXX_IsGeneratedCharac2Value10() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac2Value10");
        
    }
    
    /** Set Characteristic 2 Value 2 Is Generated.
    @param XX_IsGeneratedCharac2Value2 Characteristic 2 Value 2 Is Generated */
    public void setXX_IsGeneratedCharac2Value2 (boolean XX_IsGeneratedCharac2Value2)
    {
        set_Value ("XX_IsGeneratedCharac2Value2", Boolean.valueOf(XX_IsGeneratedCharac2Value2));
        
    }
    
    /** Get Characteristic 2 Value 2 Is Generated.
    @return Characteristic 2 Value 2 Is Generated */
    public boolean isXX_IsGeneratedCharac2Value2() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac2Value2");
        
    }
    
    /** Set Characteristic 2 Value 3 Is Generated.
    @param XX_IsGeneratedCharac2Value3 Characteristic 2 Value 3 Is Generated */
    public void setXX_IsGeneratedCharac2Value3 (boolean XX_IsGeneratedCharac2Value3)
    {
        set_Value ("XX_IsGeneratedCharac2Value3", Boolean.valueOf(XX_IsGeneratedCharac2Value3));
        
    }
    
    /** Get Characteristic 2 Value 3 Is Generated.
    @return Characteristic 2 Value 3 Is Generated */
    public boolean isXX_IsGeneratedCharac2Value3() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac2Value3");
        
    }
    
    /** Set Characteristic 2 Value 4 Is Generated.
    @param XX_IsGeneratedCharac2Value4 Characteristic 2 Value 4 Is Generated */
    public void setXX_IsGeneratedCharac2Value4 (boolean XX_IsGeneratedCharac2Value4)
    {
        set_Value ("XX_IsGeneratedCharac2Value4", Boolean.valueOf(XX_IsGeneratedCharac2Value4));
        
    }
    
    /** Get Characteristic 2 Value 4 Is Generated.
    @return Characteristic 2 Value 4 Is Generated */
    public boolean isXX_IsGeneratedCharac2Value4() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac2Value4");
        
    }
    
    /** Set Characteristic 2 Value 5 Is Generated.
    @param XX_IsGeneratedCharac2Value5 Characteristic 2 Value 5 Is Generated */
    public void setXX_IsGeneratedCharac2Value5 (boolean XX_IsGeneratedCharac2Value5)
    {
        set_Value ("XX_IsGeneratedCharac2Value5", Boolean.valueOf(XX_IsGeneratedCharac2Value5));
        
    }
    
    /** Get Characteristic 2 Value 5 Is Generated.
    @return Characteristic 2 Value 5 Is Generated */
    public boolean isXX_IsGeneratedCharac2Value5() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac2Value5");
        
    }
    
    /** Set Characteristic 2 Value 6 Is Generated.
    @param XX_IsGeneratedCharac2Value6 Characteristic 2 Value 6 Is Generated */
    public void setXX_IsGeneratedCharac2Value6 (boolean XX_IsGeneratedCharac2Value6)
    {
        set_Value ("XX_IsGeneratedCharac2Value6", Boolean.valueOf(XX_IsGeneratedCharac2Value6));
        
    }
    
    /** Get Characteristic 2 Value 6 Is Generated.
    @return Characteristic 2 Value 6 Is Generated */
    public boolean isXX_IsGeneratedCharac2Value6() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac2Value6");
        
    }
    
    /** Set Characteristic 2 Value 7 Is Generated.
    @param XX_IsGeneratedCharac2Value7 Characteristic 2 Value 7 Is Generated */
    public void setXX_IsGeneratedCharac2Value7 (boolean XX_IsGeneratedCharac2Value7)
    {
        set_Value ("XX_IsGeneratedCharac2Value7", Boolean.valueOf(XX_IsGeneratedCharac2Value7));
        
    }
    
    /** Get Characteristic 2 Value 7 Is Generated.
    @return Characteristic 2 Value 7 Is Generated */
    public boolean isXX_IsGeneratedCharac2Value7() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac2Value7");
        
    }
    
    /** Set Characteristic 2 Value 8 Is Generated.
    @param XX_IsGeneratedCharac2Value8 Characteristic 2 Value 8 Is Generated */
    public void setXX_IsGeneratedCharac2Value8 (boolean XX_IsGeneratedCharac2Value8)
    {
        set_Value ("XX_IsGeneratedCharac2Value8", Boolean.valueOf(XX_IsGeneratedCharac2Value8));
        
    }
    
    /** Get Characteristic 2 Value 8 Is Generated.
    @return Characteristic 2 Value 8 Is Generated */
    public boolean isXX_IsGeneratedCharac2Value8() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac2Value8");
        
    }
    
    /** Set Characteristic 2 Value 9 Is Generated.
    @param XX_IsGeneratedCharac2Value9 Characteristic 2 Value 9 Is Generated */
    public void setXX_IsGeneratedCharac2Value9 (boolean XX_IsGeneratedCharac2Value9)
    {
        set_Value ("XX_IsGeneratedCharac2Value9", Boolean.valueOf(XX_IsGeneratedCharac2Value9));
        
    }
    
    /** Get Characteristic 2 Value 9 Is Generated.
    @return Characteristic 2 Value 9 Is Generated */
    public boolean isXX_IsGeneratedCharac2Value9() 
    {
        return get_ValueAsBoolean("XX_IsGeneratedCharac2Value9");
        
    }
    
    /** Set Price Reference.
    @param XX_LastSalePrice Price Reference */
    public void setXX_LastSalePrice (java.math.BigDecimal XX_LastSalePrice)
    {
        set_Value ("XX_LastSalePrice", XX_LastSalePrice);
        
    }
    
    /** Get Price Reference.
    @return Price Reference */
    public java.math.BigDecimal getXX_LastSalePrice() 
    {
        return get_ValueAsBigDecimal("XX_LastSalePrice");
        
    }
    
    /** Set XX_LineMargin.
    @param XX_LineMargin XX_LineMargin */
    public void setXX_LineMargin (java.math.BigDecimal XX_LineMargin)
    {
        set_Value ("XX_LineMargin", XX_LineMargin);
        
    }
    
    /** Get XX_LineMargin.
    @return XX_LineMargin */
    public java.math.BigDecimal getXX_LineMargin() 
    {
        return get_ValueAsBigDecimal("XX_LineMargin");
        
    }
    
    /** Set XX_LinePlusTaxAmount.
    @param XX_LinePlusTaxAmount XX_LinePlusTaxAmount */
    public void setXX_LinePlusTaxAmount (java.math.BigDecimal XX_LinePlusTaxAmount)
    {
        set_Value ("XX_LinePlusTaxAmount", XX_LinePlusTaxAmount);
        
    }
    
    /** Get XX_LinePlusTaxAmount.
    @return XX_LinePlusTaxAmount */
    public java.math.BigDecimal getXX_LinePlusTaxAmount() 
    {
        return get_ValueAsBigDecimal("XX_LinePlusTaxAmount");
        
    }
    
    /** Set XX_LinePVPAmount.
    @param XX_LinePVPAmount XX_LinePVPAmount */
    public void setXX_LinePVPAmount (java.math.BigDecimal XX_LinePVPAmount)
    {
        set_Value ("XX_LinePVPAmount", XX_LinePVPAmount);
        
    }
    
    /** Get XX_LinePVPAmount.
    @return XX_LinePVPAmount */
    public java.math.BigDecimal getXX_LinePVPAmount() 
    {
        return get_ValueAsBigDecimal("XX_LinePVPAmount");
        
    }
    
    /** Set XX_LineQty.
    @param XX_LineQty XX_LineQty */
    public void setXX_LineQty (int XX_LineQty)
    {
        set_Value ("XX_LineQty", Integer.valueOf(XX_LineQty));
        
    }
    
    /** Get XX_LineQty.
    @return XX_LineQty */
    public int getXX_LineQty() 
    {
        return get_ValueAsInt("XX_LineQty");
        
    }
    
    /** Set Margin.
    @param XX_Margin Margin */
    public void setXX_Margin (java.math.BigDecimal XX_Margin)
    {
        if (XX_Margin == null) throw new IllegalArgumentException ("XX_Margin is mandatory.");
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
    
    /** Set Name of Vendor Reference Print.
    @param XX_NameOfVendorReferencePrint Name of Vendor Reference Print */
    public void setXX_NameOfVendorReferencePrint (String XX_NameOfVendorReferencePrint)
    {
        throw new IllegalArgumentException ("XX_NameOfVendorReferencePrint is virtual column");
        
    }
    
    /** Get Name of Vendor Reference Print.
    @return Name of Vendor Reference Print */
    public String getXX_NameOfVendorReferencePrint() 
    {
        return (String)get_Value("XX_NameOfVendorReferencePrint");
        
    }
    
    /** Set Package Multiple.
    @param XX_PackageMultiple Package Multiple */
    public void setXX_PackageMultiple (int XX_PackageMultiple)
    {
        set_Value ("XX_PackageMultiple", Integer.valueOf(XX_PackageMultiple));
        
    }
    
    /** Get Package Multiple.
    @return Package Multiple */
    public int getXX_PackageMultiple() 
    {
        return get_ValueAsInt("XX_PackageMultiple");
        
    }
    
    /** Set Pieces By Sale.
    @param XX_PiecesBySale_ID Pieces By Sale */
    public void setXX_PiecesBySale_ID (int XX_PiecesBySale_ID)
    {
        if (XX_PiecesBySale_ID <= 0) set_Value ("XX_PiecesBySale_ID", null);
        else
        set_Value ("XX_PiecesBySale_ID", Integer.valueOf(XX_PiecesBySale_ID));
        
    }
    
    /** Get Pieces By Sale.
    @return Pieces By Sale */
    public int getXX_PiecesBySale_ID() 
    {
        return get_ValueAsInt("XX_PiecesBySale_ID");
        
    }
    
    /** Set Discount 1.
    @param XX_Rebate1 Discount 1 */
    public void setXX_Rebate1 (java.math.BigDecimal XX_Rebate1)
    {
        set_Value ("XX_Rebate1", XX_Rebate1);
        
    }
    
    /** Get Discount 1.
    @return Discount 1 */
    public java.math.BigDecimal getXX_Rebate1() 
    {
        return get_ValueAsBigDecimal("XX_Rebate1");
        
    }
    
    /** Set Discount 2.
    @param XX_Rebate2 Discount 2 */
    public void setXX_Rebate2 (java.math.BigDecimal XX_Rebate2)
    {
        set_Value ("XX_Rebate2", XX_Rebate2);
        
    }
    
    /** Get Discount 2.
    @return Discount 2 */
    public java.math.BigDecimal getXX_Rebate2() 
    {
        return get_ValueAsBigDecimal("XX_Rebate2");
        
    }
    
    /** Set Discount 3.
    @param XX_Rebate3 Discount 3 */
    public void setXX_Rebate3 (java.math.BigDecimal XX_Rebate3)
    {
        set_Value ("XX_Rebate3", XX_Rebate3);
        
    }
    
    /** Get Discount 3.
    @return Discount 3 */
    public java.math.BigDecimal getXX_Rebate3() 
    {
        return get_ValueAsBigDecimal("XX_Rebate3");
        
    }
    
    /** Set Discount 4.
    @param XX_Rebate4 Discount 4 */
    public void setXX_Rebate4 (java.math.BigDecimal XX_Rebate4)
    {
        set_Value ("XX_Rebate4", XX_Rebate4);
        
    }
    
    /** Get Discount 4.
    @return Discount 4 */
    public java.math.BigDecimal getXX_Rebate4() 
    {
        return get_ValueAsBigDecimal("XX_Rebate4");
        
    }
    
    /** Set Reference Code Print.
    @param XX_ReferenceCodePrint Reference Code Print */
    public void setXX_ReferenceCodePrint (String XX_ReferenceCodePrint)
    {
        throw new IllegalArgumentException ("XX_ReferenceCodePrint is virtual column");
        
    }
    
    /** Get Reference Code Print.
    @return Reference Code Print */
    public String getXX_ReferenceCodePrint() 
    {
        return (String)get_Value("XX_ReferenceCodePrint");
        
    }
    
    /** Set Reference Is Associated.
    @param XX_ReferenceIsAssociated Reference Is Associated */
    public void setXX_ReferenceIsAssociated (boolean XX_ReferenceIsAssociated)
    {
        set_Value ("XX_ReferenceIsAssociated", Boolean.valueOf(XX_ReferenceIsAssociated));
        
    }
    
    /** Get Reference Is Associated.
    @return Reference Is Associated */
    public boolean isXX_ReferenceIsAssociated() 
    {
        return get_ValueAsBoolean("XX_ReferenceIsAssociated");
        
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
    
    /** Set Sale Unit.
    @param XX_SaleUnit_ID Sale Unit */
    public void setXX_SaleUnit_ID (int XX_SaleUnit_ID)
    {
        if (XX_SaleUnit_ID < 1) throw new IllegalArgumentException ("XX_SaleUnit_ID is mandatory.");
        set_Value ("XX_SaleUnit_ID", Integer.valueOf(XX_SaleUnit_ID));
        
    }
    
    /** Get Sale Unit.
    @return Sale Unit */
    public int getXX_SaleUnit_ID() 
    {
        return get_ValueAsInt("XX_SaleUnit_ID");
        
    }
    
    /** Set Show Matrix.
    @param XX_ShowMatrix Show Matrix */
    public void setXX_ShowMatrix (String XX_ShowMatrix)
    {
        set_Value ("XX_ShowMatrix", XX_ShowMatrix);
        
    }
    
    /** Get Show Matrix.
    @return Show Matrix */
    public String getXX_ShowMatrix() 
    {
        return (String)get_Value("XX_ShowMatrix");
        
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
    
    /** Set Total Pieces By Reference.
    @param XX_TotalPiecesByReference Total Pieces By Reference */
    public void setXX_TotalPiecesByReference (int XX_TotalPiecesByReference)
    {
        throw new IllegalArgumentException ("XX_TotalPiecesByReference is virtual column");
        
    }
    
    /** Get Total Pieces By Reference.
    @return Total Pieces By Reference */
    public int getXX_TotalPiecesByReference() 
    {
        return get_ValueAsInt("XX_TotalPiecesByReference");
        
    }
    
    /** Set Trade Cost.
    @param XX_TradeCost Trade Cost */
    public void setXX_TradeCost (java.math.BigDecimal XX_TradeCost)
    {
        set_Value ("XX_TradeCost", XX_TradeCost);
        
    }
    
    /** Get Trade Cost.
    @return Trade Cost */
    public java.math.BigDecimal getXX_TradeCost() 
    {
        return get_ValueAsBigDecimal("XX_TradeCost");
        
    }
    
    /** Set Origin Currency Cost.
    @param XX_UnitPurchasePrice Origin Currency Cost */
    public void setXX_UnitPurchasePrice (java.math.BigDecimal XX_UnitPurchasePrice)
    {
        if (XX_UnitPurchasePrice == null) throw new IllegalArgumentException ("XX_UnitPurchasePrice is mandatory.");
        set_Value ("XX_UnitPurchasePrice", XX_UnitPurchasePrice);
        
    }
    
    /** Get Origin Currency Cost.
    @return Origin Currency Cost */
    public java.math.BigDecimal getXX_UnitPurchasePrice() 
    {
        return get_ValueAsBigDecimal("XX_UnitPurchasePrice");
        
    }
    
    /** Set Vendor Reference History.
    @param XX_VendorReferenceHistory Vendor Reference History */
    public void setXX_VendorReferenceHistory (String XX_VendorReferenceHistory)
    {
        set_Value ("XX_VendorReferenceHistory", XX_VendorReferenceHistory);
        
    }
    
    /** Get Vendor Reference History.
    @return Vendor Reference History */
    public String getXX_VendorReferenceHistory() 
    {
        return (String)get_Value("XX_VendorReferenceHistory");
        
    }
    
    /** Set Concept Value.
    @param XX_VME_ConceptValue_ID Concept Value */
    public void setXX_VME_ConceptValue_ID (int XX_VME_ConceptValue_ID)
    {
        if (XX_VME_ConceptValue_ID <= 0) set_Value ("XX_VME_ConceptValue_ID", null);
        else
        set_Value ("XX_VME_ConceptValue_ID", Integer.valueOf(XX_VME_ConceptValue_ID));
        
    }
    
    /** Get Concept Value.
    @return Concept Value */
    public int getXX_VME_ConceptValue_ID() 
    {
        return get_ValueAsInt("XX_VME_ConceptValue_ID");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        if (XX_VMR_Brand_ID <= 0) set_Value ("XX_VMR_Brand_ID", null);
        else
        set_Value ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
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
    
    /** Set Main Characteristic.
    @param XX_VMR_LongCharacteristic_ID Main Characteristic */
    public void setXX_VMR_LongCharacteristic_ID (int XX_VMR_LongCharacteristic_ID)
    {
        if (XX_VMR_LongCharacteristic_ID <= 0) set_Value ("XX_VMR_LongCharacteristic_ID", null);
        else
        set_Value ("XX_VMR_LongCharacteristic_ID", Integer.valueOf(XX_VMR_LongCharacteristic_ID));
        
    }
    
    /** Get Main Characteristic.
    @return Main Characteristic */
    public int getXX_VMR_LongCharacteristic_ID() 
    {
        return get_ValueAsInt("XX_VMR_LongCharacteristic_ID");
        
    }
    
    /** Set Vendor Reference.
    @param XX_VMR_PO_LineRefProv_ID Vendor Reference */
    public void setXX_VMR_PO_LineRefProv_ID (int XX_VMR_PO_LineRefProv_ID)
    {
        if (XX_VMR_PO_LineRefProv_ID < 1) throw new IllegalArgumentException ("XX_VMR_PO_LineRefProv_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PO_LineRefProv_ID", Integer.valueOf(XX_VMR_PO_LineRefProv_ID));
        
    }
    
    /** Get Vendor Reference.
    @return Vendor Reference */
    public int getXX_VMR_PO_LineRefProv_ID() 
    {
        return get_ValueAsInt("XX_VMR_PO_LineRefProv_ID");
        
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
    
    /** Set Unit Conversion.
    @param XX_VMR_UnitConversion_ID Unit Conversion */
    public void setXX_VMR_UnitConversion_ID (int XX_VMR_UnitConversion_ID)
    {
        if (XX_VMR_UnitConversion_ID <= 0) set_Value ("XX_VMR_UnitConversion_ID", null);
        else
        set_Value ("XX_VMR_UnitConversion_ID", Integer.valueOf(XX_VMR_UnitConversion_ID));
        
    }
    
    /** Get Unit Conversion.
    @return Unit Conversion */
    public int getXX_VMR_UnitConversion_ID() 
    {
        return get_ValueAsInt("XX_VMR_UnitConversion_ID");
        
    }
    
    /** Set Purchase Unit.
    @param XX_VMR_UnitPurchase_ID Purchase Unit */
    public void setXX_VMR_UnitPurchase_ID (int XX_VMR_UnitPurchase_ID)
    {
        if (XX_VMR_UnitPurchase_ID <= 0) set_Value ("XX_VMR_UnitPurchase_ID", null);
        else
        set_Value ("XX_VMR_UnitPurchase_ID", Integer.valueOf(XX_VMR_UnitPurchase_ID));
        
    }
    
    /** Get Purchase Unit.
    @return Purchase Unit */
    public int getXX_VMR_UnitPurchase_ID() 
    {
        return get_ValueAsInt("XX_VMR_UnitPurchase_ID");
        
    }
    
    /** Set Vendor Product Reference.
    @param XX_VMR_VendorProdRef_ID Vendor Product Reference */
    public void setXX_VMR_VendorProdRef_ID (int XX_VMR_VendorProdRef_ID)
    {
        if (XX_VMR_VendorProdRef_ID <= 0) set_Value ("XX_VMR_VendorProdRef_ID", null);
        else
        set_Value ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
        
    }
    
    /** Set With Characteristic.
    @param XX_WithCharacteristic With Characteristic */
    public void setXX_WithCharacteristic (boolean XX_WithCharacteristic)
    {
        set_Value ("XX_WithCharacteristic", Boolean.valueOf(XX_WithCharacteristic));
        
    }
    
    /** Get With Characteristic.
    @return With Characteristic */
    public boolean isXX_WithCharacteristic() 
    {
        return get_ValueAsBoolean("XX_WithCharacteristic");
        
    }
    
    
}
