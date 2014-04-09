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
/** Generated Model for XX_VMR_PriceConsecutive
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_PriceConsecutive extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_PriceConsecutive_ID id
    @param trx transaction
    */
    public X_XX_VMR_PriceConsecutive (Ctx ctx, int XX_VMR_PriceConsecutive_ID, Trx trx)
    {
        super (ctx, XX_VMR_PriceConsecutive_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_PriceConsecutive_ID == 0)
        {
            setM_Product_ID (0);
            setValue (null);
            setXX_SalePrice (Env.ZERO);
            setXX_UnitPurchasePrice (Env.ZERO);
            setXX_VMR_PriceConsecutive_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_PriceConsecutive (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27624158533789L;
    /** Last Updated Timestamp 2012-07-11 14:30:17.0 */
    public static final long updatedMS = 1342033217000L;
    /** AD_Table_ID=1000258 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_PriceConsecutive");
        
    }
    ;
    
    /** TableName=XX_VMR_PriceConsecutive */
    public static final String Table_Name="XX_VMR_PriceConsecutive";
    
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
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
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
    
    /** Set PercentageIncrease.
    @param XX_PercentageIncrease PercentageIncrease */
    public void setXX_PercentageIncrease (java.math.BigDecimal XX_PercentageIncrease)
    {
        throw new IllegalArgumentException ("XX_PercentageIncrease is virtual column");
        
    }
    
    /** Get PercentageIncrease.
    @return PercentageIncrease */
    public java.math.BigDecimal getXX_PercentageIncrease() 
    {
        return get_ValueAsBigDecimal("XX_PercentageIncrease");
        
    }

    public void setXX_SalePlusPriceConsecutive (String XX_SalePlusPriceConsecutive)
    {
        set_Value ("XX_SalePlusPriceConsecutive", XX_SalePlusPriceConsecutive);
        
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
    
    /** Set Reprint Labels.
    @param XX_ReprintLabels Reprint Labels */
    public void setXX_ReprintLabels (String XX_ReprintLabels)
    {
        set_Value ("XX_ReprintLabels", XX_ReprintLabels);
        
    }
    
    /** Get Reprint Labels.
    @return Reprint Labels */
    public String getXX_ReprintLabels() 
    {
        return (String)get_Value("XX_ReprintLabels");
        
    }
    
    /** Set Sale Price.
    @param XX_SalePrice Sale Price */
    public void setXX_SalePrice (java.math.BigDecimal XX_SalePrice)
    {
        if (XX_SalePrice == null) throw new IllegalArgumentException ("XX_SalePrice is mandatory.");
        set_Value ("XX_SalePrice", XX_SalePrice);
        
    }
    
    /** Get Sale Price.
    @return Sale Price */
    public java.math.BigDecimal getXX_SalePrice() 
    {
        return get_ValueAsBigDecimal("XX_SalePrice");
        
    }
    
    /** Set Sincronization Status .
    @param XX_Status_Sinc Sincronization Status  */
    public void setXX_Status_Sinc (boolean XX_Status_Sinc)
    {
        set_Value ("XX_Status_Sinc", Boolean.valueOf(XX_Status_Sinc));
        
    }
    
    /** Get Sincronization Status .
    @return Sincronization Status  */
    public boolean isXX_Status_Sinc() 
    {
        return get_ValueAsBoolean("XX_Status_Sinc");
        
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
    
    /** Set DistributionHeader.
    @param XX_VMR_DistributionHeader_ID DistributionHeader */
    public void setXX_VMR_DistributionHeader_ID (int XX_VMR_DistributionHeader_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_DistributionHeader_ID is virtual column");
        
    }
    
    /** Get DistributionHeader.
    @return DistributionHeader */
    public int getXX_VMR_DistributionHeader_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionHeader_ID");
        
    }
    
    /** Set Generated By.
    @param XX_VMR_GeneratedBy_ID Generated By */
    public void setXX_VMR_GeneratedBy_ID (int XX_VMR_GeneratedBy_ID)
    {
        if (XX_VMR_GeneratedBy_ID <= 0) set_Value ("XX_VMR_GeneratedBy_ID", null);
        else
        set_Value ("XX_VMR_GeneratedBy_ID", Integer.valueOf(XX_VMR_GeneratedBy_ID));
        
    }
    
    /** Get Generated By.
    @return Generated By */
    public int getXX_VMR_GeneratedBy_ID() 
    {
        return get_ValueAsInt("XX_VMR_GeneratedBy_ID");
        
    }
    
    /** Set PriceConsecutive.
    @param XX_VMR_PriceConsecutive_ID PriceConsecutive */
    public void setXX_VMR_PriceConsecutive_ID (int XX_VMR_PriceConsecutive_ID)
    {
        if (XX_VMR_PriceConsecutive_ID < 1) throw new IllegalArgumentException ("XX_VMR_PriceConsecutive_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PriceConsecutive_ID", Integer.valueOf(XX_VMR_PriceConsecutive_ID));
        
    }
    
    /** Get PriceConsecutive.
    @return PriceConsecutive */
    public int getXX_VMR_PriceConsecutive_ID() 
    {
        return get_ValueAsInt("XX_VMR_PriceConsecutive_ID");
        
    }
    
    
}
