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
/** Generated Model for E_XX_VCN_CPPD01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VCN_CPPD01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VCN_CPPD01_ID id
    @param trx transaction
    */
    public X_E_XX_VCN_CPPD01 (Ctx ctx, int E_XX_VCN_CPPD01_ID, Trx trx)
    {
        super (ctx, E_XX_VCN_CPPD01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VCN_CPPD01_ID == 0)
        {
            setE_XX_VCN_CPPD01_ID (0);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VCN_CPPD01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27571982828789L;
    /** Last Updated Timestamp 2010-11-15 17:15:12.0 */
    public static final long updatedMS = 1289857512000L;
    /** AD_Table_ID=1000344 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VCN_CPPD01");
        
    }
    ;
    
    /** TableName=E_XX_VCN_CPPD01 */
    public static final String Table_Name="E_XX_VCN_CPPD01";
    
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
    
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID <= 0) set_Value ("C_Invoice_ID", null);
        else
        set_Value ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
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
    
    /** Set Country.
    @param CountryName Country Name */
    public void setCountryName (String CountryName)
    {
        set_Value ("CountryName", CountryName);
        
    }
    
    /** Get Country.
    @return Country Name */
    public String getCountryName() 
    {
        return (String)get_Value("CountryName");
        
    }
    
    /** Set E_XX_VCN_CPPD01_ID.
    @param E_XX_VCN_CPPD01_ID E_XX_VCN_CPPD01_ID */
    public void setE_XX_VCN_CPPD01_ID (int E_XX_VCN_CPPD01_ID)
    {
        if (E_XX_VCN_CPPD01_ID < 1) throw new IllegalArgumentException ("E_XX_VCN_CPPD01_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VCN_CPPD01_ID", Integer.valueOf(E_XX_VCN_CPPD01_ID));
        
    }
    
    /** Get E_XX_VCN_CPPD01_ID.
    @return E_XX_VCN_CPPD01_ID */
    public int getE_XX_VCN_CPPD01_ID() 
    {
        return get_ValueAsInt("E_XX_VCN_CPPD01_ID");
        
    }
    
    /** Set Receipt Number.
    @param NUMREC receipt number */
    public void setNUMREC (int NUMREC)
    {
        set_Value ("NUMREC", Integer.valueOf(NUMREC));
        
    }
    
    /** Get Receipt Number.
    @return receipt number */
    public int getNUMREC() 
    {
        return get_ValueAsInt("NUMREC");
        
    }
    
    /** Set Payment Method.
    @param PaymentRule How you pay the invoice */
    public void setPaymentRule (int PaymentRule)
    {
        set_Value ("PaymentRule", Integer.valueOf(PaymentRule));
        
    }
    
    /** Get Payment Method.
    @return How you pay the invoice */
    public int getPaymentRule() 
    {
        return get_ValueAsInt("PaymentRule");
        
    }
    
    /** Set Payment Term.
    @param PaymentTerm Payment Term */
    public void setPaymentTerm (String PaymentTerm)
    {
        set_Value ("PaymentTerm", PaymentTerm);
        
    }
    
    /** Get Payment Term.
    @return Payment Term */
    public String getPaymentTerm() 
    {
        return (String)get_Value("PaymentTerm");
        
    }
    
    /** Set SubTotal.
    @param TotalLines Total of all document lines (excluding Tax) */
    public void setTotalLines (java.math.BigDecimal TotalLines)
    {
        set_Value ("TotalLines", TotalLines);
        
    }
    
    /** Get SubTotal.
    @return Total of all document lines (excluding Tax) */
    public java.math.BigDecimal getTotalLines() 
    {
        return get_ValueAsBigDecimal("TotalLines");
        
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
    
    /** Set DIAAPR.
    @param XX_ApprovalDay DIAAPR */
    public void setXX_ApprovalDay (int XX_ApprovalDay)
    {
        set_Value ("XX_ApprovalDay", Integer.valueOf(XX_ApprovalDay));
        
    }
    
    /** Get DIAAPR.
    @return DIAAPR */
    public int getXX_ApprovalDay() 
    {
        return get_ValueAsInt("XX_ApprovalDay");
        
    }
    
    /** Set MESAPR.
    @param XX_ApprovalMonth MESAPR */
    public void setXX_ApprovalMonth (int XX_ApprovalMonth)
    {
        set_Value ("XX_ApprovalMonth", Integer.valueOf(XX_ApprovalMonth));
        
    }
    
    /** Get MESAPR.
    @return MESAPR */
    public int getXX_ApprovalMonth() 
    {
        return get_ValueAsInt("XX_ApprovalMonth");
        
    }
    
    /** Set AÑOAPR.
    @param XX_ApprovalYear AÑOAPR */
    public void setXX_ApprovalYear (int XX_ApprovalYear)
    {
        set_Value ("XX_ApprovalYear", Integer.valueOf(XX_ApprovalYear));
        
    }
    
    /** Get AÑOAPR.
    @return AÑOAPR */
    public int getXX_ApprovalYear() 
    {
        return get_ValueAsInt("XX_ApprovalYear");
        
    }
    
    /** Set DIAANU.
    @param XX_CancellationDay DIAANU */
    public void setXX_CancellationDay (int XX_CancellationDay)
    {
        set_Value ("XX_CancellationDay", Integer.valueOf(XX_CancellationDay));
        
    }
    
    /** Get DIAANU.
    @return DIAANU */
    public int getXX_CancellationDay() 
    {
        return get_ValueAsInt("XX_CancellationDay");
        
    }
    
    /** Set MESANU.
    @param XX_CancellationMonth MESANU */
    public void setXX_CancellationMonth (int XX_CancellationMonth)
    {
        set_Value ("XX_CancellationMonth", Integer.valueOf(XX_CancellationMonth));
        
    }
    
    /** Get MESANU.
    @return MESANU */
    public int getXX_CancellationMonth() 
    {
        return get_ValueAsInt("XX_CancellationMonth");
        
    }
    
    /** Set AÑOANU.
    @param XX_CancellationYear AÑOANU */
    public void setXX_CancellationYear (int XX_CancellationYear)
    {
        set_Value ("XX_CancellationYear", Integer.valueOf(XX_CancellationYear));
        
    }
    
    /** Get AÑOANU.
    @return AÑOANU */
    public int getXX_CancellationYear() 
    {
        return get_ValueAsInt("XX_CancellationYear");
        
    }
    
    /** Set Consecutivo.
    @param XX_Consecutive Consecutivo */
    public void setXX_Consecutive (int XX_Consecutive)
    {
        set_Value ("XX_Consecutive", Integer.valueOf(XX_Consecutive));
        
    }
    
    /** Get Consecutivo.
    @return Consecutivo */
    public int getXX_Consecutive() 
    {
        return get_ValueAsInt("XX_Consecutive");
        
    }
    
    /** Set Control Number.
    @param XX_ControlNumber Número de Control de la Factura */
    public void setXX_ControlNumber (String XX_ControlNumber)
    {
        set_Value ("XX_ControlNumber", XX_ControlNumber);
        
    }
    
    /** Get Control Number.
    @return Número de Control de la Factura */
    public String getXX_ControlNumber() 
    {
        return (String)get_Value("XX_ControlNumber");
        
    }
    
    /** Set DEBCRE.
    @param XX_ControlNumberDebCre Número del aviso de debito o crédito */
    public void setXX_ControlNumberDebCre (String XX_ControlNumberDebCre)
    {
        set_Value ("XX_ControlNumberDebCre", XX_ControlNumberDebCre);
        
    }
    
    /** Get DEBCRE.
    @return Número del aviso de debito o crédito */
    public String getXX_ControlNumberDebCre() 
    {
        return (String)get_Value("XX_ControlNumberDebCre");
        
    }
    
    /** Set DIAANT.
    @param XX_DayOfPaymentAdvance DIAANT */
    public void setXX_DayOfPaymentAdvance (int XX_DayOfPaymentAdvance)
    {
        set_Value ("XX_DayOfPaymentAdvance", Integer.valueOf(XX_DayOfPaymentAdvance));
        
    }
    
    /** Get DIAANT.
    @return DIAANT */
    public int getXX_DayOfPaymentAdvance() 
    {
        return get_ValueAsInt("XX_DayOfPaymentAdvance");
        
    }
    
    /** Set STACTA.
    @param XX_DebtBalanceState Status de cuenta por pagar */
    public void setXX_DebtBalanceState (int XX_DebtBalanceState)
    {
        set_Value ("XX_DebtBalanceState", Integer.valueOf(XX_DebtBalanceState));
        
    }
    
    /** Get STACTA.
    @return Status de cuenta por pagar */
    public int getXX_DebtBalanceState() 
    {
        return get_ValueAsInt("XX_DebtBalanceState");
        
    }
    
    /** Set DOCAFE.
    @param XX_DocumentNoInvoice Número de documento de la factura */
    public void setXX_DocumentNoInvoice (String XX_DocumentNoInvoice)
    {
        set_Value ("XX_DocumentNoInvoice", XX_DocumentNoInvoice);
        
    }
    
    /** Get DOCAFE.
    @return Número de documento de la factura */
    public String getXX_DocumentNoInvoice() 
    {
        return (String)get_Value("XX_DocumentNoInvoice");
        
    }
    
    /** Set DOCORI.
    @param XX_DocumentNoOrder Número de control de orden de compra */
    public void setXX_DocumentNoOrder (String XX_DocumentNoOrder)
    {
        set_Value ("XX_DocumentNoOrder", XX_DocumentNoOrder);
        
    }
    
    /** Get DOCORI.
    @return Número de control de orden de compra */
    public String getXX_DocumentNoOrder() 
    {
        return (String)get_Value("XX_DocumentNoOrder");
        
    }
    
    /** Set STAIND.
    @param XX_EmittedDraftStatus Estado del giro emitido */
    public void setXX_EmittedDraftStatus (String XX_EmittedDraftStatus)
    {
        set_Value ("XX_EmittedDraftStatus", XX_EmittedDraftStatus);
        
    }
    
    /** Get STAIND.
    @return Estado del giro emitido */
    public String getXX_EmittedDraftStatus() 
    {
        return (String)get_Value("XX_EmittedDraftStatus");
        
    }
    
    /** Set DIAVEN.
    @param XX_ExpirationDay Día de Vencimiento */
    public void setXX_ExpirationDay (int XX_ExpirationDay)
    {
        set_Value ("XX_ExpirationDay", Integer.valueOf(XX_ExpirationDay));
        
    }
    
    /** Get DIAVEN.
    @return Día de Vencimiento */
    public int getXX_ExpirationDay() 
    {
        return get_ValueAsInt("XX_ExpirationDay");
        
    }
    
    /** Set MESVEN.
    @param XX_ExpirationMonth Mes de Vencimiento */
    public void setXX_ExpirationMonth (int XX_ExpirationMonth)
    {
        set_Value ("XX_ExpirationMonth", Integer.valueOf(XX_ExpirationMonth));
        
    }
    
    /** Get MESVEN.
    @return Mes de Vencimiento */
    public int getXX_ExpirationMonth() 
    {
        return get_ValueAsInt("XX_ExpirationMonth");
        
    }
    
    /** Set AÑOVEN.
    @param XX_ExpirationYear Año de Vencimiento */
    public void setXX_ExpirationYear (int XX_ExpirationYear)
    {
        set_Value ("XX_ExpirationYear", Integer.valueOf(XX_ExpirationYear));
        
    }
    
    /** Get AÑOVEN.
    @return Año de Vencimiento */
    public int getXX_ExpirationYear() 
    {
        return get_ValueAsInt("XX_ExpirationYear");
        
    }
    
    /** Set FACCAM.
    @param XX_FieldFactor Factor de Campo */
    public void setXX_FieldFactor (java.math.BigDecimal XX_FieldFactor)
    {
        set_Value ("XX_FieldFactor", XX_FieldFactor);
        
    }
    
    /** Get FACCAM.
    @return Factor de Campo */
    public java.math.BigDecimal getXX_FieldFactor() 
    {
        return get_ValueAsBigDecimal("XX_FieldFactor");
        
    }
    
    /** Set MESANT.
    @param XX_MonthOfPaymentAdvance Mes de anticipo de pago */
    public void setXX_MonthOfPaymentAdvance (int XX_MonthOfPaymentAdvance)
    {
        set_Value ("XX_MonthOfPaymentAdvance", Integer.valueOf(XX_MonthOfPaymentAdvance));
        
    }
    
    /** Get MESANT.
    @return Mes de anticipo de pago */
    public int getXX_MonthOfPaymentAdvance() 
    {
        return get_ValueAsInt("XX_MonthOfPaymentAdvance");
        
    }
    
    /** Set DIAMOV.
    @param XX_MovementDay Día de generación de la aprobación */
    public void setXX_MovementDay (int XX_MovementDay)
    {
        set_Value ("XX_MovementDay", Integer.valueOf(XX_MovementDay));
        
    }
    
    /** Get DIAMOV.
    @return Día de generación de la aprobación */
    public int getXX_MovementDay() 
    {
        return get_ValueAsInt("XX_MovementDay");
        
    }
    
    /** Set MESMOV.
    @param XX_MovementMonth Mes de generación de la aprobación */
    public void setXX_MovementMonth (int XX_MovementMonth)
    {
        set_Value ("XX_MovementMonth", Integer.valueOf(XX_MovementMonth));
        
    }
    
    /** Get MESMOV.
    @return Mes de generación de la aprobación */
    public int getXX_MovementMonth() 
    {
        return get_ValueAsInt("XX_MovementMonth");
        
    }
    
    /** Set AÑOMOV.
    @param XX_MovementYear Año de generación de la aprobación */
    public void setXX_MovementYear (int XX_MovementYear)
    {
        set_Value ("XX_MovementYear", Integer.valueOf(XX_MovementYear));
        
    }
    
    /** Get AÑOMOV.
    @return Año de generación de la aprobación */
    public int getXX_MovementYear() 
    {
        return get_ValueAsInt("XX_MovementYear");
        
    }
    
    /** Set DESORP.
    @param XX_OrderDescription Descripción de la orden de pago */
    public void setXX_OrderDescription (String XX_OrderDescription)
    {
        set_Value ("XX_OrderDescription", XX_OrderDescription);
        
    }
    
    /** Get DESORP.
    @return Descripción de la orden de pago */
    public String getXX_OrderDescription() 
    {
        return (String)get_Value("XX_OrderDescription");
        
    }
    
    /** Set NUMORP.
    @param XX_PaymentOrder Orden de pago */
    public void setXX_PaymentOrder (int XX_PaymentOrder)
    {
        set_Value ("XX_PaymentOrder", Integer.valueOf(XX_PaymentOrder));
        
    }
    
    /** Get NUMORP.
    @return Orden de pago */
    public int getXX_PaymentOrder() 
    {
        return get_ValueAsInt("XX_PaymentOrder");
        
    }
    
    /** Set STAGRA.
    @param XX_RecordingStatus Estado grabacion */
    public void setXX_RecordingStatus (int XX_RecordingStatus)
    {
        set_Value ("XX_RecordingStatus", Integer.valueOf(XX_RecordingStatus));
        
    }
    
    /** Get STAGRA.
    @return Estado grabacion */
    public int getXX_RecordingStatus() 
    {
        return get_ValueAsInt("XX_RecordingStatus");
        
    }
    
    /** Set DIAREG.
    @param XX_RegistrationDay Día de generación de la aprobación */
    public void setXX_RegistrationDay (int XX_RegistrationDay)
    {
        set_Value ("XX_RegistrationDay", Integer.valueOf(XX_RegistrationDay));
        
    }
    
    /** Get DIAREG.
    @return Día de generación de la aprobación */
    public int getXX_RegistrationDay() 
    {
        return get_ValueAsInt("XX_RegistrationDay");
        
    }
    
    /** Set MESREG.
    @param XX_RegistrationMonth Mes de generación de la aprobación */
    public void setXX_RegistrationMonth (int XX_RegistrationMonth)
    {
        set_Value ("XX_RegistrationMonth", Integer.valueOf(XX_RegistrationMonth));
        
    }
    
    /** Get MESREG.
    @return Mes de generación de la aprobación */
    public int getXX_RegistrationMonth() 
    {
        return get_ValueAsInt("XX_RegistrationMonth");
        
    }
    
    /** Set AÑOREG.
    @param XX_RegistrationYear Año de generación de la aprobación */
    public void setXX_RegistrationYear (int XX_RegistrationYear)
    {
        set_Value ("XX_RegistrationYear", Integer.valueOf(XX_RegistrationYear));
        
    }
    
    /** Get AÑOREG.
    @return Año de generación de la aprobación */
    public int getXX_RegistrationYear() 
    {
        return get_ValueAsInt("XX_RegistrationYear");
        
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
    
    /** Set TIMOPA.
    @param XX_TypeOfMovement Tipo de movimiento cuenta por pagar */
    public void setXX_TypeOfMovement (String XX_TypeOfMovement)
    {
        set_Value ("XX_TypeOfMovement", XX_TypeOfMovement);
        
    }
    
    /** Get TIMOPA.
    @return Tipo de movimiento cuenta por pagar */
    public String getXX_TypeOfMovement() 
    {
        return (String)get_Value("XX_TypeOfMovement");
        
    }
    
    /** Set Vendor Class.
    @param XX_VendorClass Clase de Proveedor (Nac/Imp) */
    public void setXX_VendorClass (String XX_VendorClass)
    {
        set_Value ("XX_VendorClass", XX_VendorClass);
        
    }
    
    /** Get Vendor Class.
    @return Clase de Proveedor (Nac/Imp) */
    public String getXX_VendorClass() 
    {
        return (String)get_Value("XX_VendorClass");
        
    }
    
    /** Set Withholding Number.
    @param XX_Withholding Withholding Number */
    public void setXX_Withholding (int XX_Withholding)
    {
        set_Value ("XX_Withholding", Integer.valueOf(XX_Withholding));
        
    }
    
    /** Get Withholding Number.
    @return Withholding Number */
    public int getXX_Withholding() 
    {
        return get_ValueAsInt("XX_Withholding");
        
    }
    
    /** Set AÑOANT.
    @param XX_YearOfPaymentAdvance Año de anticipo de pago */
    public void setXX_YearOfPaymentAdvance (int XX_YearOfPaymentAdvance)
    {
        set_Value ("XX_YearOfPaymentAdvance", Integer.valueOf(XX_YearOfPaymentAdvance));
        
    }
    
    /** Get AÑOANT.
    @return Año de anticipo de pago */
    public int getXX_YearOfPaymentAdvance() 
    {
        return get_ValueAsInt("XX_YearOfPaymentAdvance");
        
    }
    
    
}
