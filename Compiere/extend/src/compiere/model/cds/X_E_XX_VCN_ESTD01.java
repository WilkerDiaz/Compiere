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
/** Generated Model for E_XX_VCN_ESTD01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VCN_ESTD01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VCN_ESTD01_ID id
    @param trx transaction
    */
    public X_E_XX_VCN_ESTD01 (Ctx ctx, int E_XX_VCN_ESTD01_ID, Trx trx)
    {
        super (ctx, E_XX_VCN_ESTD01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VCN_ESTD01_ID == 0)
        {
            setE_XX_VCN_ESTD01_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VCN_ESTD01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27571973628789L;
    /** Last Updated Timestamp 2010-11-15 14:41:52.0 */
    public static final long updatedMS = 1289848312000L;
    /** AD_Table_ID=1000364 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VCN_ESTD01");
        
    }
    ;
    
    /** TableName=E_XX_VCN_ESTD01 */
    public static final String Table_Name="E_XX_VCN_ESTD01";
    
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
    
    /** Set E_XX_VCN_ESTD01_ID.
    @param E_XX_VCN_ESTD01_ID E_XX_VCN_ESTD01_ID */
    public void setE_XX_VCN_ESTD01_ID (int E_XX_VCN_ESTD01_ID)
    {
        if (E_XX_VCN_ESTD01_ID < 1) throw new IllegalArgumentException ("E_XX_VCN_ESTD01_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VCN_ESTD01_ID", Integer.valueOf(E_XX_VCN_ESTD01_ID));
        
    }
    
    /** Get E_XX_VCN_ESTD01_ID.
    @return E_XX_VCN_ESTD01_ID */
    public int getE_XX_VCN_ESTD01_ID() 
    {
        return get_ValueAsInt("E_XX_VCN_ESTD01_ID");
        
    }
    
    /** Set Cedula/RIF.
    @param XX_CI_RIF Cedula o RIF del Proveedor o Cliente */
    public void setXX_CI_RIF (String XX_CI_RIF)
    {
        set_Value ("XX_CI_RIF", XX_CI_RIF);
        
    }
    
    /** Get Cedula/RIF.
    @return Cedula o RIF del Proveedor o Cliente */
    public String getXX_CI_RIF() 
    {
        return (String)get_Value("XX_CI_RIF");
        
    }
    
    /** Set Company ID.
    @param XX_Company Company ID */
    public void setXX_Company (int XX_Company)
    {
        set_Value ("XX_Company", Integer.valueOf(XX_Company));
        
    }
    
    /** Get Company ID.
    @return Company ID */
    public int getXX_Company() 
    {
        return get_ValueAsInt("XX_Company");
        
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
    
    /** Set Document Date.
    @param XX_DocumentDate Document Date */
    public void setXX_DocumentDate (int XX_DocumentDate)
    {
        set_Value ("XX_DocumentDate", Integer.valueOf(XX_DocumentDate));
        
    }
    
    /** Get Document Date.
    @return Document Date */
    public int getXX_DocumentDate() 
    {
        return get_ValueAsInt("XX_DocumentDate");
        
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
    public void setXX_DocumentNoOrder (int XX_DocumentNoOrder)
    {
        set_Value ("XX_DocumentNoOrder", Integer.valueOf(XX_DocumentNoOrder));
        
    }
    
    /** Get DOCORI.
    @return Número de control de orden de compra */
    public int getXX_DocumentNoOrder() 
    {
        return get_ValueAsInt("XX_DocumentNoOrder");
        
    }
    
    /** Set Exempt Base.
    @param XX_ExemptBase Exempt Base */
    public void setXX_ExemptBase (java.math.BigDecimal XX_ExemptBase)
    {
        set_Value ("XX_ExemptBase", XX_ExemptBase);
        
    }
    
    /** Get Exempt Base.
    @return Exempt Base */
    public java.math.BigDecimal getXX_ExemptBase() 
    {
        return get_ValueAsBigDecimal("XX_ExemptBase");
        
    }
    
    /** Set Expedient Number.
    @param XX_ExpedientNumber Expedient Number */
    public void setXX_ExpedientNumber (String XX_ExpedientNumber)
    {
        set_Value ("XX_ExpedientNumber", XX_ExpedientNumber);
        
    }
    
    /** Get Expedient Number.
    @return Expedient Number */
    public String getXX_ExpedientNumber() 
    {
        return (String)get_Value("XX_ExpedientNumber");
        
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
    
    /** Set NOMBRE.
    @param XX_NameOfVendor NOMBRE */
    public void setXX_NameOfVendor (String XX_NameOfVendor)
    {
        set_Value ("XX_NameOfVendor", XX_NameOfVendor);
        
    }
    
    /** Get NOMBRE.
    @return NOMBRE */
    public String getXX_NameOfVendor() 
    {
        return (String)get_Value("XX_NameOfVendor");
        
    }
    
    /** Set Not Subject Base.
    @param XX_NotSubjectBase Not Subject Base */
    public void setXX_NotSubjectBase (java.math.BigDecimal XX_NotSubjectBase)
    {
        set_Value ("XX_NotSubjectBase", XX_NotSubjectBase);
        
    }
    
    /** Get Not Subject Base.
    @return Not Subject Base */
    public java.math.BigDecimal getXX_NotSubjectBase() 
    {
        return get_ValueAsBigDecimal("XX_NotSubjectBase");
        
    }
    
    /** Set ORIGIN.
    @param XX_Origin ORIGIN */
    public void setXX_Origin (String XX_Origin)
    {
        set_Value ("XX_Origin", XX_Origin);
        
    }
    
    /** Get ORIGIN.
    @return ORIGIN */
    public String getXX_Origin() 
    {
        return (String)get_Value("XX_Origin");
        
    }
    
    /** Set NUMPLA.
    @param XX_PayrollNumber NUMPLA */
    public void setXX_PayrollNumber (int XX_PayrollNumber)
    {
        set_Value ("XX_PayrollNumber", Integer.valueOf(XX_PayrollNumber));
        
    }
    
    /** Get NUMPLA.
    @return NUMPLA */
    public int getXX_PayrollNumber() 
    {
        return get_ValueAsInt("XX_PayrollNumber");
        
    }
    
    /** Set STAELI.
    @param XX_StateOfElimination STAELI */
    public void setXX_StateOfElimination (String XX_StateOfElimination)
    {
        set_Value ("XX_StateOfElimination", XX_StateOfElimination);
        
    }
    
    /** Get STAELI.
    @return STAELI */
    public String getXX_StateOfElimination() 
    {
        return (String)get_Value("XX_StateOfElimination");
        
    }
    
    /** Set Store Code.
    @param XX_StoreCode Store Code */
    public void setXX_StoreCode (String XX_StoreCode)
    {
        set_Value ("XX_StoreCode", XX_StoreCode);
        
    }
    
    /** Get Store Code.
    @return Store Code */
    public String getXX_StoreCode() 
    {
        return (String)get_Value("XX_StoreCode");
        
    }
    
    /** Set XX_Tax.
    @param XX_Tax XX_Tax */
    public void setXX_Tax (java.math.BigDecimal XX_Tax)
    {
        set_Value ("XX_Tax", XX_Tax);
        
    }
    
    /** Get XX_Tax.
    @return XX_Tax */
    public java.math.BigDecimal getXX_Tax() 
    {
        return get_ValueAsBigDecimal("XX_Tax");
        
    }
    
    /** Set Taxable Base.
    @param XX_TaxableBase Taxable Base */
    public void setXX_TaxableBase (java.math.BigDecimal XX_TaxableBase)
    {
        set_Value ("XX_TaxableBase", XX_TaxableBase);
        
    }
    
    /** Get Taxable Base.
    @return Taxable Base */
    public java.math.BigDecimal getXX_TaxableBase() 
    {
        return get_ValueAsBigDecimal("XX_TaxableBase");
        
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
    
    /** Set Total Invoice Cost.
    @param XX_TotalInvCost Total Invoice Cost */
    public void setXX_TotalInvCost (java.math.BigDecimal XX_TotalInvCost)
    {
        set_Value ("XX_TotalInvCost", XX_TotalInvCost);
        
    }
    
    /** Get Total Invoice Cost.
    @return Total Invoice Cost */
    public java.math.BigDecimal getXX_TotalInvCost() 
    {
        return get_ValueAsBigDecimal("XX_TotalInvCost");
        
    }
    
    /** Set Type of Person.
    @param XX_TypePerson Tipo de Persona (Nat/Jur/Gub) */
    public void setXX_TypePerson (String XX_TypePerson)
    {
        set_Value ("XX_TypePerson", XX_TypePerson);
        
    }
    
    /** Get Type of Person.
    @return Tipo de Persona (Nat/Jur/Gub) */
    public String getXX_TypePerson() 
    {
        return (String)get_Value("XX_TypePerson");
        
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
    
    /** Set Withholding Tax.
    @param XX_WithholdingTax Withholding Tax */
    public void setXX_WithholdingTax (java.math.BigDecimal XX_WithholdingTax)
    {
        set_Value ("XX_WithholdingTax", XX_WithholdingTax);
        
    }
    
    /** Get Withholding Tax.
    @return Withholding Tax */
    public java.math.BigDecimal getXX_WithholdingTax() 
    {
        return get_ValueAsBigDecimal("XX_WithholdingTax");
        
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
    
    
}
