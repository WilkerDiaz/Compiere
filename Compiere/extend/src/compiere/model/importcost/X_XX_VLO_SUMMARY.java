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
package compiere.model.importcost;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VLO_SUMMARY
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_SUMMARY extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_SUMMARY_ID id
    @param trx transaction
    */
    public X_XX_VLO_SUMMARY (Ctx ctx, int XX_VLO_SUMMARY_ID, Trx trx)
    {
        super (ctx, XX_VLO_SUMMARY_ID, trx);
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_SUMMARY (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27602204237789L;
    /** Last Updated Timestamp 2011-10-31 12:05:21.0 */
    public static final long updatedMS = 1320078921000L;
    /** AD_Table_ID=1000330 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_SUMMARY");
        
    }
    ;
    
    /** TableName=XX_VLO_SUMMARY */
    public static final String Table_Name="XX_VLO_SUMMARY";
    
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
    
    /** Set Invoice.
    @param C_Invoice_ID */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID <= 0) set_Value ("C_Invoice_ID", null);
        else
        set_Value ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
    }
    
    /** Set Data Type.
    @param DataType Type of data */
    public void setDataType (String DataType)
    {
        set_Value ("DataType", DataType);
        
    }
    
    /** Get Data Type.
    @return Type of data */
    public String getDataType() 
    {
        return (String)get_Value("DataType");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Complete Adjustment.
    @param XX_CompleteAdjustment Complete Adjustment */
    public void setXX_CompleteAdjustment (String XX_CompleteAdjustment)
    {
        set_Value ("XX_CompleteAdjustment", XX_CompleteAdjustment);
        
    }
    
    /** Get Complete Adjustment.
    @return Complete Adjustment */
    public String getXX_CompleteAdjustment() 
    {
        return (String)get_Value("XX_CompleteAdjustment");
        
    }
    
    /** Set Control Number.
    @param XX_ControlNumber Número de Control */
    public void setXX_ControlNumber (String XX_ControlNumber)
    {
        set_Value ("XX_ControlNumber", XX_ControlNumber);
        
    }
    
    /** Get Control Number.
    @return Número de Control */
    public String getXX_ControlNumber() 
    {
        return (String)get_Value("XX_ControlNumber");
        
    }
    
    /** Set Amount Cost.
    @param XX_Cosant Monto de costo */
    public void setXX_Cosant (java.math.BigDecimal XX_Cosant)
    {
        set_Value ("XX_COSANT", XX_Cosant);
        
    }
    
    /** Get Amount Cost.
    @return Monto de costo */
    public java.math.BigDecimal getXX_Cosant() 
    {
        return get_ValueAsBigDecimal("XX_COSANT");
        
    }
    
    /** Set Cost Free.
    @param XX_CostFree Cost Free */
    public void setXX_CostFree (java.math.BigDecimal XX_CostFree)
    {
        set_Value ("XX_CostFree", XX_CostFree);
        
    }
    
    /** Get Cost Free.
    @return Cost Free */
    public java.math.BigDecimal getXX_CostFree() 
    {
        return get_ValueAsBigDecimal("XX_CostFree");
        
    }
    
    /** Set Cost Not Subject.
    @param XX_CostNotSubject Cost Not Subject */
    public void setXX_CostNotSubject (java.math.BigDecimal XX_CostNotSubject)
    {
        set_Value ("XX_CostNotSubject", XX_CostNotSubject);
        
    }
    
    /** Get Cost Not Subject.
    @return Cost Not Subject */
    public java.math.BigDecimal getXX_CostNotSubject() 
    {
        return get_ValueAsBigDecimal("XX_CostNotSubject");
        
    }
    
    /** Set Date of Registration.
    @param XX_DateRegistration Date of Registration */
    public void setXX_DateRegistration (Timestamp XX_DateRegistration)
    {
        set_Value ("XX_DateRegistration", XX_DateRegistration);
        
    }
    
    /** Get Date of Registration.
    @return Date of Registration */
    public Timestamp getXX_DateRegistration() 
    {
        return (Timestamp)get_Value("XX_DateRegistration");
        
    }
    
    /** Set Distribution by Store.
    @param XX_DistributionByStore Distribution by Store */
    public void setXX_DistributionByStore (boolean XX_DistributionByStore)
    {
        set_Value ("XX_DistributionByStore", Boolean.valueOf(XX_DistributionByStore));
        
    }
    
    /** Get Distribution by Store.
    @return Distribution by Store */
    public boolean isXX_DistributionByStore() 
    {
        return get_ValueAsBoolean("XX_DistributionByStore");
        
    }
    
    /** Set Invoice Date.
    @param XX_InvoiceDate Invoice Date */
    public void setXX_InvoiceDate (Timestamp XX_InvoiceDate)
    {
        set_Value ("XX_InvoiceDate", XX_InvoiceDate);
        
    }
    
    /** Get Invoice Date.
    @return Invoice Date */
    public Timestamp getXX_InvoiceDate() 
    {
        return (Timestamp)get_Value("XX_InvoiceDate");
        
    }
    
    /** Set Invoice Number.
    @param XX_InvoiceNro Invoice Number */
    public void setXX_InvoiceNro (String XX_InvoiceNro)
    {
        set_Value ("XX_INVOICENRO", XX_InvoiceNro);
        
    }
    
    /** Get Invoice Number.
    @return Invoice Number */
    public String getXX_InvoiceNro() 
    {
        return (String)get_Value("XX_INVOICENRO");
        
    }
    
    /** Set Sales Amount.
    @param XX_MontVentas Monto de Ventas */
    public void setXX_MontVentas (java.math.BigDecimal XX_MontVentas)
    {
        set_Value ("XX_MontVentas", XX_MontVentas);
        
    }
    
    /** Get Sales Amount.
    @return Monto de Ventas */
    public java.math.BigDecimal getXX_MontVentas() 
    {
        return get_ValueAsBigDecimal("XX_MontVentas");
        
    }
    
    /** Set Note Credit/Debit.
    @param XX_NoteCreditDebit Note Credit/Debit */
    public void setXX_NoteCreditDebit (String XX_NoteCreditDebit)
    {
        set_Value ("XX_NoteCreditDebit", XX_NoteCreditDebit);
        
    }
    
    /** Get Note Credit/Debit.
    @return Note Credit/Debit */
    public String getXX_NoteCreditDebit() 
    {
        return (String)get_Value("XX_NoteCreditDebit");
        
    }
    
    /** Set Order Date.
    @param XX_OrderDate Order Date */
    public void setXX_OrderDate (String XX_OrderDate)
    {
        throw new IllegalArgumentException ("XX_OrderDate is virtual column");
        
    }
    
    /** Get Order Date.
    @return Order Date */
    public String getXX_OrderDate() 
    {
        return (String)get_Value("XX_OrderDate");
        
    }
    
    /** Set Print Report Adjustment.
    @param XX_PrintReportAdjustment Print Report Adjustment */
    public void setXX_PrintReportAdjustment (String XX_PrintReportAdjustment)
    {
        set_Value ("XX_PrintReportAdjustment", XX_PrintReportAdjustment);
        
    }
    
    /** Get Print Report Adjustment.
    @return Print Report Adjustment */
    public String getXX_PrintReportAdjustment() 
    {
        return (String)get_Value("XX_PrintReportAdjustment");
        
    }
    
    /** Set Quantity.
    @param XX_Quantity Quantity */
    public void setXX_Quantity (java.math.BigDecimal XX_Quantity)
    {
        set_Value ("XX_Quantity", XX_Quantity);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getXX_Quantity() 
    {
        return get_ValueAsBigDecimal("XX_Quantity");
        
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
    
    /** Set Summary_ID.
    @param XX_VLO_Summary_ID Summary_ID */
    public void setXX_VLO_Summary_ID (int XX_VLO_Summary_ID)
    {
        if (XX_VLO_Summary_ID < 1) throw new IllegalArgumentException ("XX_VLO_Summary_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_Summary_ID", Integer.valueOf(XX_VLO_Summary_ID));
        
    }
    
    /** Get Summary_ID.
    @return Summary_ID */
    public int getXX_VLO_Summary_ID() 
    {
        return get_ValueAsInt("XX_VLO_Summary_ID");
        
    }
    
    /** Set Tax Cost.
    @param XX_VLO_TaxCost Tax Cost */
    public void setXX_VLO_TaxCost (java.math.BigDecimal XX_VLO_TaxCost)
    {
        set_Value ("XX_VLO_TaxCost", XX_VLO_TaxCost);
        
    }
    
    /** Get Tax Cost.
    @return Tax Cost */
    public java.math.BigDecimal getXX_VLO_TaxCost() 
    {
        return get_ValueAsBigDecimal("XX_VLO_TaxCost");
        
    }
    
    /** Set Tax Sales.
    @param XX_VLO_TaxSales Tax Sales */
    public void setXX_VLO_TaxSales (java.math.BigDecimal XX_VLO_TaxSales)
    {
        set_Value ("XX_VLO_TaxSales", XX_VLO_TaxSales);
        
    }
    
    /** Get Tax Sales.
    @return Tax Sales */
    public java.math.BigDecimal getXX_VLO_TaxSales() 
    {
        return get_ValueAsBigDecimal("XX_VLO_TaxSales");
        
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
    
    /** Set Withholding Tax Marck.
    @param XX_WithholdingTaxMarck Withholding Tax Marck */
    public void setXX_WithholdingTaxMarck (boolean XX_WithholdingTaxMarck)
    {
        set_Value ("XX_WithholdingTaxMarck", Boolean.valueOf(XX_WithholdingTaxMarck));
        
    }
    
    /** Get Withholding Tax Marck.
    @return Withholding Tax Marck */
    public boolean isXX_WithholdingTaxMarck() 
    {
        return get_ValueAsBoolean("XX_WithholdingTaxMarck");
        
    }
    
    /** Set Visible.
    @param XX_Visible Visible */
    public void setXX_Visible (boolean XX_Visible)
    {
        set_Value ("XX_Visible", Boolean.valueOf(XX_Visible));
        
    }
    
    /** Get Visible.
    @return Visible */
    public boolean isXX_Visible() 
    {
        return get_ValueAsBoolean("XX_Visible");
        
    }
    
    /** Set Tax.
    @param C_Tax_ID Tax */
    public void setC_Tax_ID (int C_Tax_ID)
    {
        if (C_Tax_ID <= 0) set_Value ("C_Tax_ID", null);
        else
        set_Value ("C_Tax_ID", Integer.valueOf(C_Tax_ID));
        
    }
    
    /** Get Tax.
    @return Tax */
    public int getC_Tax_ID() 
    {
        return get_ValueAsInt("C_Tax_ID");
    }
    
    /** Set Document Date.
    @param XX_DocumentDate Document Date */
    public void setXX_DocumentDate(Timestamp date)
    {
        set_Value ("XX_DocumentDate", date);
    }
    
    /** Get Document Date.
    @return Document Date */
    public Timestamp getXX_DocumentDate() 
    {
        return (Timestamp)get_Value("XX_DocumentDate");   
    }
}
