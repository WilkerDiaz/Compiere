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
package compiere.model.suppliesservices;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_ContractDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_ContractDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_ContractDetail_ID id
    @param trx transaction
    */
    public X_XX_ContractDetail (Ctx ctx, int XX_ContractDetail_ID, Trx trx)
    {
        super (ctx, XX_ContractDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_ContractDetail_ID == 0)
        {
            setValue (null);
            setXX_ContractDetail_ID (0);	// @XX_Contract_ID@
            setXX_PAYCONTRACT_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_ContractDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27625911568789L;
    /** Last Updated Timestamp 2012-07-31 21:27:32.0 */
    public static final long updatedMS = 1343786252000L;
    /** AD_Table_ID=1002455 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_ContractDetail");
        
    }
    ;
    
    /** TableName=XX_ContractDetail */
    public static final String Table_Name="XX_ContractDetail";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Invoice total amount = F */
    public static final String XX_APPLICABLEPERCENT_InvoiceTotalAmount = X_Ref_XX_Percentage_LV.INVOICE_TOTAL_AMOUNT.getValue();
    /** Sales by Brand = M */
    public static final String XX_APPLICABLEPERCENT_SalesByBrand = X_Ref_XX_Percentage_LV.SALES_BY_BRAND.getValue();
    /** Gross Sales = V */
    public static final String XX_APPLICABLEPERCENT_GrossSales = X_Ref_XX_Percentage_LV.GROSS_SALES.getValue();
    /** Gross Sales per Store = W */
    public static final String XX_APPLICABLEPERCENT_GrossSalesPerStore = X_Ref_XX_Percentage_LV.GROSS_SALES_PER_STORE.getValue();
    /** Set Applicable Percentage.
    @param XX_ApplicablePercent Applicable Percentage */
    public void setXX_ApplicablePercent (String XX_ApplicablePercent)
    {
        if (!X_Ref_XX_Percentage_LV.isValid(XX_ApplicablePercent))
        throw new IllegalArgumentException ("XX_ApplicablePercent Invalid value - " + XX_ApplicablePercent + " - Reference_ID=1001658 - F - M - V - W");
        set_Value ("XX_ApplicablePercent", XX_ApplicablePercent);
        
    }
    
    /** Get Applicable Percentage.
    @return Applicable Percentage */
    public String getXX_ApplicablePercent() 
    {
        return (String)get_Value("XX_ApplicablePercent");
        
    }
    
    /** Set Amount.
    @param XX_ContractAmount Amount */
    public void setXX_ContractAmount (java.math.BigDecimal XX_ContractAmount)
    {
        set_Value ("XX_ContractAmount", XX_ContractAmount);
        
    }
    
    /** Get Amount.
    @return Amount */
    public java.math.BigDecimal getXX_ContractAmount() 
    {
        return get_ValueAsBigDecimal("XX_ContractAmount");
        
    }
    
    /** Set Contract Detail ID.
    @param XX_ContractDetail_ID Contract Detail ID */
    public void setXX_ContractDetail_ID (int XX_ContractDetail_ID)
    {
        if (XX_ContractDetail_ID < 1) throw new IllegalArgumentException ("XX_ContractDetail_ID is mandatory.");
        set_ValueNoCheck ("XX_ContractDetail_ID", Integer.valueOf(XX_ContractDetail_ID));
        
    }
    
    /** Get Contract Detail ID.
    @return Contract Detail ID */
    public int getXX_ContractDetail_ID() 
    {
        return get_ValueAsInt("XX_ContractDetail_ID");
        
    }
    
    /** Set Contract ID.
    @param XX_Contract_ID Contract ID */
    public void setXX_Contract_ID (int XX_Contract_ID)
    {
        if (XX_Contract_ID <= 0) set_ValueNoCheck ("XX_Contract_ID", null);
        else
        set_ValueNoCheck ("XX_Contract_ID", Integer.valueOf(XX_Contract_ID));
        
    }
    
    /** Get Contract ID.
    @return Contract ID */
    public int getXX_Contract_ID() 
    {
        return get_ValueAsInt("XX_Contract_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getXX_Contract_ID()));
        
    }
    
    /** Set Cost Center.
    @param XX_CostCenter_ID Cost Center */
    public void setXX_CostCenter_ID (int XX_CostCenter_ID)
    {
        if (XX_CostCenter_ID <= 0) set_Value ("XX_CostCenter_ID", null);
        else
        set_Value ("XX_CostCenter_ID", Integer.valueOf(XX_CostCenter_ID));
        
    }
    
    /** Get Cost Center.
    @return Cost Center */
    public int getXX_CostCenter_ID() 
    {
        return get_ValueAsInt("XX_CostCenter_ID");
        
    }
    
    /** Set XX_PAYCONTRACT_ID.
    @param XX_PAYCONTRACT_ID XX_PAYCONTRACT_ID */
    public void setXX_PAYCONTRACT_ID (int XX_PAYCONTRACT_ID)
    {
        if (XX_PAYCONTRACT_ID < 1) throw new IllegalArgumentException ("XX_PAYCONTRACT_ID is mandatory.");
        set_ValueNoCheck ("XX_PAYCONTRACT_ID", Integer.valueOf(XX_PAYCONTRACT_ID));
        
    }
    
    /** Get XX_PAYCONTRACT_ID.
    @return XX_PAYCONTRACT_ID */
    public int getXX_PAYCONTRACT_ID() 
    {
        return get_ValueAsInt("XX_PAYCONTRACT_ID");
        
    }
    
    /** Fixed and Variable = A */
    public static final String XX_PAYMENTTYPEDET_FixedAndVariable = X_Ref_XX_PaymentType_LV.FIXED_AND_VARIABLE.getValue();
    /** Fixed = F */
    public static final String XX_PAYMENTTYPEDET_Fixed = X_Ref_XX_PaymentType_LV.FIXED.getValue();
    /** Guaranteed Fixed Variable = G */
    public static final String XX_PAYMENTTYPEDET_GuaranteedFixedVariable = X_Ref_XX_PaymentType_LV.GUARANTEED_FIXED_VARIABLE.getValue();
    /** Variable = V */
    public static final String XX_PAYMENTTYPEDET_Variable = X_Ref_XX_PaymentType_LV.VARIABLE.getValue();
    /** Set Payment Type .
    @param XX_PaymentTypeDet Payment Type  */
    public void setXX_PaymentTypeDet (String XX_PaymentTypeDet)
    {
        if (!X_Ref_XX_PaymentType_LV.isValid(XX_PaymentTypeDet))
        throw new IllegalArgumentException ("XX_PaymentTypeDet Invalid value - " + XX_PaymentTypeDet + " - Reference_ID=1001657 - A - F - G - V");
        set_ValueNoCheck ("XX_PaymentTypeDet", XX_PaymentTypeDet);
        
    }
    
    /** Get Payment Type .
    @return Payment Type  */
    public String getXX_PaymentTypeDet() 
    {
        return (String)get_Value("XX_PaymentTypeDet");
        
    }
    
    /** Set Percentage (Amount).
    @param XX_PercenAmount Percentage (Amount) */
    public void setXX_PercenAmount (java.math.BigDecimal XX_PercenAmount)
    {
        set_Value ("XX_PercenAmount", XX_PercenAmount);
        
    }
    
    /** Get Percentage (Amount).
    @return Percentage (Amount) */
    public java.math.BigDecimal getXX_PercenAmount() 
    {
        return get_ValueAsBigDecimal("XX_PercenAmount");
        
    }
    
    
}
