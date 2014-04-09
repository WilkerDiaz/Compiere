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
/** Generated Model for I_XX_VMR_PriceConsecutive
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VMR_PriceConsecutive extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VMR_PriceConsecutive_ID id
    @param trx transaction
    */
    public X_I_XX_VMR_PriceConsecutive (Ctx ctx, int I_XX_VMR_PriceConsecutive_ID, Trx trx)
    {
        super (ctx, I_XX_VMR_PriceConsecutive_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VMR_PriceConsecutive_ID == 0)
        {
            setI_XX_VMR_PriceConsecutive_ID (0);
            setProcessed (false);	// N
            setProcessing (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VMR_PriceConsecutive (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27569291997789L;
    /** Last Updated Timestamp 2010-10-15 13:48:01.0 */
    public static final long updatedMS = 1287166681000L;
    /** AD_Table_ID=1000263 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VMR_PriceConsecutive");
        
    }
    ;
    
    /** TableName=I_XX_VMR_PriceConsecutive */
    public static final String Table_Name="I_XX_VMR_PriceConsecutive";
    
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
    
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (boolean I_IsImported)
    {
        set_Value ("I_IsImported", Boolean.valueOf(I_IsImported));
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public boolean isI_IsImported() 
    {
        return get_ValueAsBoolean("I_IsImported");
        
    }
    
    /** Set Data Mode.
    @param ImportExport Is data exported or imported? */
    public void setImportExport (String ImportExport)
    {
        set_Value ("ImportExport", ImportExport);
        
    }
    
    /** Get Data Mode.
    @return Is data exported or imported? */
    public String getImportExport() 
    {
        return (String)get_Value("ImportExport");
        
    }
    
    /** Set I_PriceConsecutive_ID.
    @param I_XX_VMR_PriceConsecutive_ID I_PriceConsecutive_ID */
    public void setI_XX_VMR_PriceConsecutive_ID (int I_XX_VMR_PriceConsecutive_ID)
    {
        if (I_XX_VMR_PriceConsecutive_ID < 1) throw new IllegalArgumentException ("I_XX_VMR_PriceConsecutive_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VMR_PriceConsecutive_ID", Integer.valueOf(I_XX_VMR_PriceConsecutive_ID));
        
    }
    
    /** Get I_PriceConsecutive_ID.
    @return I_PriceConsecutive_ID */
    public int getI_XX_VMR_PriceConsecutive_ID() 
    {
        return get_ValueAsInt("I_XX_VMR_PriceConsecutive_ID");
        
    }
    
    /** Set Lot No.
    @param Lot Lot number (alphanumeric) */
    public void setLot (String Lot)
    {
        set_Value ("Lot", Lot);
        
    }
    
    /** Get Lot No.
    @return Lot number (alphanumeric) */
    public String getLot() 
    {
        return (String)get_Value("Lot");
        
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
    
    /** Pedido = P */
    public static final String XX_CONSECUTIVEORIGIN_Pedido = X_Ref_XX_ConsecutiveOrigin.PEDIDO.getValue();
    /** Rebajas = R */
    public static final String XX_CONSECUTIVEORIGIN_Rebajas = X_Ref_XX_ConsecutiveOrigin.REBAJAS.getValue();
    /** Set Consecutive Origin.
    @param XX_ConsecutiveOrigin Consecutive Origin */
    public void setXX_ConsecutiveOrigin (String XX_ConsecutiveOrigin)
    {
        if (!X_Ref_XX_ConsecutiveOrigin.isValid(XX_ConsecutiveOrigin))
        throw new IllegalArgumentException ("XX_ConsecutiveOrigin Invalid value - " + XX_ConsecutiveOrigin + " - Reference_ID=1000250 - P - R");
        set_Value ("XX_ConsecutiveOrigin", XX_ConsecutiveOrigin);
        
    }
    
    /** Get Consecutive Origin.
    @return Consecutive Origin */
    public String getXX_ConsecutiveOrigin() 
    {
        return (String)get_Value("XX_ConsecutiveOrigin");
        
    }
    
    /** Set DayCreate.
    @param XX_DayCreate DayCreate */
    public void setXX_DayCreate (int XX_DayCreate)
    {
        set_Value ("XX_DayCreate", Integer.valueOf(XX_DayCreate));
        
    }
    
    /** Get DayCreate.
    @return DayCreate */
    public int getXX_DayCreate() 
    {
        return get_ValueAsInt("XX_DayCreate");
        
    }
    
    /** Set DayUpdate.
    @param XX_DayUpdate DayUpdate */
    public void setXX_DayUpdate (int XX_DayUpdate)
    {
        set_Value ("XX_DayUpdate", Integer.valueOf(XX_DayUpdate));
        
    }
    
    /** Get DayUpdate.
    @return DayUpdate */
    public int getXX_DayUpdate() 
    {
        return get_ValueAsInt("XX_DayUpdate");
        
    }
    
    /** Set MonthCreate.
    @param XX_MonthCreate MonthCreate */
    public void setXX_MonthCreate (int XX_MonthCreate)
    {
        set_Value ("XX_MonthCreate", Integer.valueOf(XX_MonthCreate));
        
    }
    
    /** Get MonthCreate.
    @return MonthCreate */
    public int getXX_MonthCreate() 
    {
        return get_ValueAsInt("XX_MonthCreate");
        
    }
    
    /** Set MonthUpdate.
    @param XX_MonthUpdate MonthUpdate */
    public void setXX_MonthUpdate (int XX_MonthUpdate)
    {
        set_Value ("XX_MonthUpdate", Integer.valueOf(XX_MonthUpdate));
        
    }
    
    /** Get MonthUpdate.
    @return MonthUpdate */
    public int getXX_MonthUpdate() 
    {
        return get_ValueAsInt("XX_MonthUpdate");
        
    }
    
    /** Set Order Number.
    @param XX_OrderNum Order Number */
    public void setXX_OrderNum (String XX_OrderNum)
    {
        set_Value ("XX_OrderNum", XX_OrderNum);
        
    }
    
    /** Get Order Number.
    @return Order Number */
    public String getXX_OrderNum() 
    {
        return (String)get_Value("XX_OrderNum");
        
    }
    
    /** Set Product_Value.
    @param XX_Product_Value Product_Value */
    public void setXX_Product_Value (String XX_Product_Value)
    {
        set_Value ("XX_Product_Value", XX_Product_Value);
        
    }
    
    /** Get Product_Value.
    @return Product_Value */
    public String getXX_Product_Value() 
    {
        return (String)get_Value("XX_Product_Value");
        
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
    
    /** Set PriceConsecutive.
    @param XX_VMR_PriceConsecutive_ID PriceConsecutive */
    public void setXX_VMR_PriceConsecutive_ID (int XX_VMR_PriceConsecutive_ID)
    {
        if (XX_VMR_PriceConsecutive_ID <= 0) set_Value ("XX_VMR_PriceConsecutive_ID", null);
        else
        set_Value ("XX_VMR_PriceConsecutive_ID", Integer.valueOf(XX_VMR_PriceConsecutive_ID));
        
    }
    
    /** Get PriceConsecutive.
    @return PriceConsecutive */
    public int getXX_VMR_PriceConsecutive_ID() 
    {
        return get_ValueAsInt("XX_VMR_PriceConsecutive_ID");
        
    }
    
    /** Set YearCreate.
    @param XX_YearCreate YearCreate */
    public void setXX_YearCreate (int XX_YearCreate)
    {
        set_Value ("XX_YearCreate", Integer.valueOf(XX_YearCreate));
        
    }
    
    /** Get YearCreate.
    @return YearCreate */
    public int getXX_YearCreate() 
    {
        return get_ValueAsInt("XX_YearCreate");
        
    }
    
    /** Set YearUpdate.
    @param XX_YearUpdate YearUpdate */
    public void setXX_YearUpdate (int XX_YearUpdate)
    {
        set_Value ("XX_YearUpdate", Integer.valueOf(XX_YearUpdate));
        
    }
    
    /** Get YearUpdate.
    @return YearUpdate */
    public int getXX_YearUpdate() 
    {
        return get_ValueAsInt("XX_YearUpdate");
        
    }
    
    
}
