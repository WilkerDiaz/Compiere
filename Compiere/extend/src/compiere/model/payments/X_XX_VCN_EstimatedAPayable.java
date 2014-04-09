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
package compiere.model.payments;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_EstimatedAPayable
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_EstimatedAPayable extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_EstimatedAPayable_ID id
    @param trx transaction
    */
    public X_XX_VCN_EstimatedAPayable (Ctx ctx, int XX_VCN_EstimatedAPayable_ID, Trx trx)
    {
        super (ctx, XX_VCN_EstimatedAPayable_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_EstimatedAPayable_ID == 0)
        {
            setXX_VCN_EstimatedAPayable_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_EstimatedAPayable (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27661836418789L;
    /** Last Updated Timestamp 2013-09-20 16:35:02.0 */
    public static final long updatedMS = 1379711102000L;
    /** AD_Table_ID=1002659 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_EstimatedAPayable");
        
    }
    ;
    
    /** TableName=XX_VCN_EstimatedAPayable */
    public static final String Table_Name="XX_VCN_EstimatedAPayable";
    
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
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Country.
    @param C_Country_ID Country */
    public void setC_Country_ID (int C_Country_ID)
    {
        if (C_Country_ID <= 0) set_Value ("C_Country_ID", null);
        else
        set_Value ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
        else
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
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
    
    /** Set Payment Term.
    @param C_PaymentTerm_ID The terms of Payment (timing, discount) */
    public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
    {
        if (C_PaymentTerm_ID <= 0) set_Value ("C_PaymentTerm_ID", null);
        else
        set_Value ("C_PaymentTerm_ID", Integer.valueOf(C_PaymentTerm_ID));
        
    }
    
    /** Get Payment Term.
    @return The terms of Payment (timing, discount) */
    public int getC_PaymentTerm_ID() 
    {
        return get_ValueAsInt("C_PaymentTerm_ID");
        
    }
    
    /** Set Descripción.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Descripción.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Amount Foreign.
    @param XX_AmountForeign Payment in foreign currency */
    public void setXX_AmountForeign (java.math.BigDecimal XX_AmountForeign)
    {
        set_Value ("XX_AmountForeign", XX_AmountForeign);
        
    }
    
    /** Get Amount Foreign.
    @return Payment in foreign currency */
    public java.math.BigDecimal getXX_AmountForeign() 
    {
        return get_ValueAsBigDecimal("XX_AmountForeign");
        
    }
    
    /** Set Amount Local.
    @param XX_AmountLocal Payment in local currency */
    public void setXX_AmountLocal (java.math.BigDecimal XX_AmountLocal)
    {
        set_Value ("XX_AmountLocal", XX_AmountLocal);
        
    }
    
    /** Get Amount Local.
    @return Payment in local currency */
    public java.math.BigDecimal getXX_AmountLocal() 
    {
        return get_ValueAsBigDecimal("XX_AmountLocal");
        
    }
    
    /** Set Contract ID.
    @param XX_Contract_ID Contract ID */
    public void setXX_Contract_ID (int XX_Contract_ID)
    {
        if (XX_Contract_ID <= 0) set_Value ("XX_Contract_ID", null);
        else
        set_Value ("XX_Contract_ID", Integer.valueOf(XX_Contract_ID));
        
    }
    
    /** Get Contract ID.
    @return Contract ID */
    public int getXX_Contract_ID() 
    {
        return get_ValueAsInt("XX_Contract_ID");
        
    }
    
    /** Set Date Estimated.
    @param XX_DateEstimated Estimated date of payment */
    public void setXX_DateEstimated (Timestamp XX_DateEstimated)
    {
        set_Value ("XX_DateEstimated", XX_DateEstimated);
        
    }
    
    /** Get Date Estimated.
    @return Estimated date of payment */
    public Timestamp getXX_DateEstimated() 
    {
        return (Timestamp)get_Value("XX_DateEstimated");
        
    }
    
    /** Set Dispensable.
    @param XX_Dispensable Dispensable */
    public void setXX_Dispensable (boolean XX_Dispensable)
    {
        set_Value ("XX_Dispensable", Boolean.valueOf(XX_Dispensable));
        
    }
    
    /** Get Dispensable.
    @return Dispensable */
    public boolean isXX_Dispensable() 
    {
        return get_ValueAsBoolean("XX_Dispensable");
        
    }
    
    /** Set Import Company.
    @param XX_ImportingCompany_ID Import Company */
    public void setXX_ImportingCompany_ID (int XX_ImportingCompany_ID)
    {
        if (XX_ImportingCompany_ID <= 0) set_Value ("XX_ImportingCompany_ID", null);
        else
        set_Value ("XX_ImportingCompany_ID", Integer.valueOf(XX_ImportingCompany_ID));
        
    }
    
    /** Get Import Company.
    @return Import Company */
    public int getXX_ImportingCompany_ID() 
    {
        return get_ValueAsInt("XX_ImportingCompany_ID");
        
    }
    
    /** Importada = Importada */
    public static final String XX_ORDERTYPE_Importada = X_Ref_XX_OrderType.IMPORTADA.getValue();
    /** Nacional = Nacional */
    public static final String XX_ORDERTYPE_Nacional = X_Ref_XX_OrderType.NACIONAL.getValue();
    /** Set Order Type.
    @param XX_OrderType Tipo de Orden (Nacional / Internacional) */
    public void setXX_OrderType (String XX_OrderType)
    {
        if (!X_Ref_XX_OrderType.isValid(XX_OrderType))
        throw new IllegalArgumentException ("XX_OrderType Invalid value - " + XX_OrderType + " - Reference_ID=1000049 - Importada - Nacional");
        set_Value ("XX_OrderType", XX_OrderType);
        
    }
    
    /** Get Order Type.
    @return Tipo de Orden (Nacional / Internacional) */
    public String getXX_OrderType() 
    {
        return (String)get_Value("XX_OrderType");
        
    }
    
    /** Set XX_PAYCONTRACT_ID.
    @param XX_PAYCONTRACT_ID XX_PAYCONTRACT_ID */
    public void setXX_PAYCONTRACT_ID (int XX_PAYCONTRACT_ID)
    {
        if (XX_PAYCONTRACT_ID <= 0) set_Value ("XX_PAYCONTRACT_ID", null);
        else
        set_Value ("XX_PAYCONTRACT_ID", Integer.valueOf(XX_PAYCONTRACT_ID));
        
    }
    
    /** Get XX_PAYCONTRACT_ID.
    @return XX_PAYCONTRACT_ID */
    public int getXX_PAYCONTRACT_ID() 
    {
        return get_ValueAsInt("XX_PAYCONTRACT_ID");
        
    }
    
    /** Assets/Services PO = POA */
    public static final String XX_POTYPE_AssetsServicesPO = X_Ref_XX_POType_LV.ASSETS_SERVICES_PO.getValue();
    /** Purchase Order = POM */
    public static final String XX_POTYPE_PurchaseOrder = X_Ref_XX_POType_LV.PURCHASE_ORDER.getValue();
    /** Set PO Type.
    @param XX_POType PO Type */
    public void setXX_POType (String XX_POType)
    {
        if (!X_Ref_XX_POType_LV.isValid(XX_POType))
        throw new IllegalArgumentException ("XX_POType Invalid value - " + XX_POType + " - Reference_ID=1001659 - POA - POM");
        set_Value ("XX_POType", XX_POType);
        
    }
    
    /** Get PO Type.
    @return PO Type */
    public String getXX_POType() 
    {
        return (String)get_Value("XX_POType");
        
    }
    
    /** Set Type of Promotion.
    @param XX_TypePromotion Type of Promotion */
    public void setXX_TypePromotion (int XX_TypePromotion)
    {
        set_Value ("XX_TypePromotion", Integer.valueOf(XX_TypePromotion));
        
    }
    
    /** Get Type of Promotion.
    @return Type of Promotion */
    public int getXX_TypePromotion() 
    {
        return get_ValueAsInt("XX_TypePromotion");
        
    }
    
    /** Set XX_VCN_EstimatedAPayable_ID.
    @param XX_VCN_EstimatedAPayable_ID XX_VCN_EstimatedAPayable_ID */
    public void setXX_VCN_EstimatedAPayable_ID (int XX_VCN_EstimatedAPayable_ID)
    {
        if (XX_VCN_EstimatedAPayable_ID < 1) throw new IllegalArgumentException ("XX_VCN_EstimatedAPayable_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_EstimatedAPayable_ID", Integer.valueOf(XX_VCN_EstimatedAPayable_ID));
        
    }
    
    /** Get XX_VCN_EstimatedAPayable_ID.
    @return XX_VCN_EstimatedAPayable_ID */
    public int getXX_VCN_EstimatedAPayable_ID() 
    {
        return get_ValueAsInt("XX_VCN_EstimatedAPayable_ID");
        
    }
    
    /** Set Vendor Type.
    @param XX_VendorType_ID Describe el Tipo de Proveedor */
    public void setXX_VendorType_ID (int XX_VendorType_ID)
    {
        if (XX_VendorType_ID <= 0) set_Value ("XX_VendorType_ID", null);
        else
        set_Value ("XX_VendorType_ID", Integer.valueOf(XX_VendorType_ID));
        
    }
    
    /** Get Vendor Type.
    @return Describe el Tipo de Proveedor */
    public int getXX_VendorType_ID() 
    {
        return get_ValueAsInt("XX_VendorType_ID");
        
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
    
    /** Set Week Estimated.
    @param XX_WeekEstimated Estimated pay week */
    public void setXX_WeekEstimated (String XX_WeekEstimated)
    {
        set_Value ("XX_WeekEstimated", XX_WeekEstimated);
        
    }
    
    /** Get Week Estimated.
    @return Estimated pay week */
    public String getXX_WeekEstimated() 
    {
        return (String)get_Value("XX_WeekEstimated");
        
    }
    
    
}
