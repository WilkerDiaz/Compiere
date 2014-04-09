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
/** Generated Model for XX_VCN_PurchasesBook
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_PurchasesBook extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_PurchasesBook_ID id
    @param trx transaction
    */
    public X_XX_VCN_PurchasesBook (Ctx ctx, int XX_VCN_PurchasesBook_ID, Trx trx)
    {
        super (ctx, XX_VCN_PurchasesBook_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_PurchasesBook_ID == 0)
        {
            setXX_VCN_PurchasesBook_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_PurchasesBook (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27674856276789L;
    /** Last Updated Timestamp 2014-02-18 09:12:40.0 */
    public static final long updatedMS = 1392730960000L;
    /** AD_Table_ID=1000252 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_PurchasesBook");
        
    }
    ;
    
    /** TableName=XX_VCN_PurchasesBook */
    public static final String Table_Name="XX_VCN_PurchasesBook";
    
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
    
    /** Set Tax.
    @param C_Tax_ID Tax identifier */
    public void setC_Tax_ID (int C_Tax_ID)
    {
        if (C_Tax_ID <= 0) set_Value ("C_Tax_ID", null);
        else
        set_Value ("C_Tax_ID", Integer.valueOf(C_Tax_ID));
        
    }
    
    /** Get Tax.
    @return Tax identifier */
    public int getC_Tax_ID() 
    {
        return get_ValueAsInt("C_Tax_ID");
        
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
    
    /** Set Converted.
    @param XX_Converted Converted */
    public void setXX_Converted (boolean XX_Converted)
    {
        set_Value ("XX_Converted", Boolean.valueOf(XX_Converted));
        
    }
    
    /** Get Converted.
    @return Converted */
    public boolean isXX_Converted() 
    {
        return get_ValueAsBoolean("XX_Converted");
        
    }
    
    /** Set Creation Date.
    @param XX_Created Fecha de Creación de la O/C */
    public void setXX_Created (Timestamp XX_Created)
    {
        throw new IllegalArgumentException ("XX_Created is virtual column");
        
    }
    
    /** Get Creation Date.
    @return Fecha de Creación de la O/C */
    public Timestamp getXX_Created() 
    {
        return (Timestamp)get_Value("XX_Created");
        
    }
    
    /** Set Date.
    @param XX_DATE Date */
    public void setXX_DATE (Timestamp XX_DATE)
    {
        set_Value ("XX_DATE", XX_DATE);
        
    }
    
    /** Get Date.
    @return Date */
    public Timestamp getXX_DATE() 
    {
        return (Timestamp)get_Value("XX_DATE");
        
    }
    
    /** Set Document Date.
    @param XX_DocumentDate Document Date */
    public void setXX_DocumentDate (Timestamp XX_DocumentDate)
    {
        set_Value ("XX_DocumentDate", XX_DocumentDate);
        
    }
    
    /** Get Document Date.
    @return Document Date */
    public Timestamp getXX_DocumentDate() 
    {
        return (Timestamp)get_Value("XX_DocumentDate");
        
    }
    
    /** Set Document Number.
    @param XX_DocumentNo Document Number */
    public void setXX_DocumentNo (String XX_DocumentNo)
    {
        set_Value ("XX_DocumentNo", XX_DocumentNo);
        
    }
    
    /** Get Document Number.
    @return Document Number */
    public String getXX_DocumentNo() 
    {
        return (String)get_Value("XX_DocumentNo");
        
    }
    
    /** Set Document Number.
    @param XX_DocumentNo_ID Document Number */
    public void setXX_DocumentNo_ID (int XX_DocumentNo_ID)
    {
        if (XX_DocumentNo_ID <= 0) set_Value ("XX_DocumentNo_ID", null);
        else
        set_Value ("XX_DocumentNo_ID", Integer.valueOf(XX_DocumentNo_ID));
        
    }
    
    /** Get Document Number.
    @return Document Number */
    public int getXX_DocumentNo_ID() 
    {
        return get_ValueAsInt("XX_DocumentNo_ID");
        
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
    public void setXX_ExpedientNumber (int XX_ExpedientNumber)
    {
        set_Value ("XX_ExpedientNumber", Integer.valueOf(XX_ExpedientNumber));
        
    }
    
    /** Get Expedient Number.
    @return Expedient Number */
    public int getXX_ExpedientNumber() 
    {
        return get_ValueAsInt("XX_ExpedientNumber");
        
    }
    
    /** Set Form No.
    @param XX_FormNo Form No */
    public void setXX_FormNo (String XX_FormNo)
    {
        set_Value ("XX_FormNo", XX_FormNo);
        
    }
    
    /** Get Form No.
    @return Form No */
    public String getXX_FormNo() 
    {
        return (String)get_Value("XX_FormNo");
        
    }
    
    /** Set Is Manual.
    @param XX_isManual Purchases' Book Originated Manually or Automatically */
    public void setXX_isManual (boolean XX_isManual)
    {
        set_Value ("XX_isManual", Boolean.valueOf(XX_isManual));
        
    }
    
    /** Get Is Manual.
    @return Purchases' Book Originated Manually or Automatically */
    public boolean isXX_isManual() 
    {
        return get_ValueAsBoolean("XX_isManual");
        
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
    
    /** Set Synchronized.
    @param XX_Synchronized Indica si el registro ya fue exportado */
    public void setXX_Synchronized (boolean XX_Synchronized)
    {
        set_Value ("XX_Synchronized", Boolean.valueOf(XX_Synchronized));
        
    }
    
    /** Get Synchronized.
    @return Indica si el registro ya fue exportado */
    public boolean isXX_Synchronized() 
    {
        return get_ValueAsBoolean("XX_Synchronized");
        
    }
    
    /** Set XX_SynchronizedPB.
    @param XX_SynchronizedPB XX_SynchronizedPB */
    public void setXX_SynchronizedPB (boolean XX_SynchronizedPB)
    {
        set_Value ("XX_SynchronizedPB", Boolean.valueOf(XX_SynchronizedPB));
        
    }
    
    /** Get XX_SynchronizedPB.
    @return XX_SynchronizedPB */
    public boolean isXX_SynchronizedPB() 
    {
        return get_ValueAsBoolean("XX_SynchronizedPB");
        
    }
    
    /** Set Synchronized Withholding.
    @param XX_SynchronizedWithholding Indica si la retencion del registro actual ya fue exportada */
    public void setXX_SynchronizedWithholding (boolean XX_SynchronizedWithholding)
    {
        set_Value ("XX_SynchronizedWithholding", Boolean.valueOf(XX_SynchronizedWithholding));
        
    }
    
    /** Get Synchronized Withholding.
    @return Indica si la retencion del registro actual ya fue exportada */
    public boolean isXX_SynchronizedWithholding() 
    {
        return get_ValueAsBoolean("XX_SynchronizedWithholding");
        
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
    
    /** Set XX_VCN_ApplicationNumber_ID.
    @param XX_VCN_ApplicationNumber_ID XX_VCN_ApplicationNumber_ID */
    public void setXX_VCN_ApplicationNumber_ID (int XX_VCN_ApplicationNumber_ID)
    {
        if (XX_VCN_ApplicationNumber_ID <= 0) set_Value ("XX_VCN_ApplicationNumber_ID", null);
        else
        set_Value ("XX_VCN_ApplicationNumber_ID", Integer.valueOf(XX_VCN_ApplicationNumber_ID));
        
    }
    
    /** Get XX_VCN_ApplicationNumber_ID.
    @return XX_VCN_ApplicationNumber_ID */
    public int getXX_VCN_ApplicationNumber_ID() 
    {
        return get_ValueAsInt("XX_VCN_ApplicationNumber_ID");
        
    }
    
    /** Set Purchases' Book ID.
    @param XX_VCN_PurchasesBook_ID Purchases' Book ID */
    public void setXX_VCN_PurchasesBook_ID (int XX_VCN_PurchasesBook_ID)
    {
        if (XX_VCN_PurchasesBook_ID < 1) throw new IllegalArgumentException ("XX_VCN_PurchasesBook_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_PurchasesBook_ID", Integer.valueOf(XX_VCN_PurchasesBook_ID));
        
    }
    
    /** Get Purchases' Book ID.
    @return Purchases' Book ID */
    public int getXX_VCN_PurchasesBook_ID() 
    {
        return get_ValueAsInt("XX_VCN_PurchasesBook_ID");
        
    }
    
    /** Set File Number.
    @param XX_VLO_BoardingGuide_ID File Number */
    public void setXX_VLO_BoardingGuide_ID (int XX_VLO_BoardingGuide_ID)
    {
        if (XX_VLO_BoardingGuide_ID <= 0) set_Value ("XX_VLO_BoardingGuide_ID", null);
        else
        set_Value ("XX_VLO_BoardingGuide_ID", Integer.valueOf(XX_VLO_BoardingGuide_ID));
        
    }
    
    /** Get File Number.
    @return File Number */
    public int getXX_VLO_BoardingGuide_ID() 
    {
        return get_ValueAsInt("XX_VLO_BoardingGuide_ID");
        
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
    
    
}
