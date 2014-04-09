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
/** Generated Model for XX_VLO_ImportCostDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_ImportCostDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_ImportCostDetail_ID id
    @param trx transaction
    */
    public X_XX_VLO_ImportCostDetail (Ctx ctx, int XX_VLO_ImportCostDetail_ID, Trx trx)
    {
        super (ctx, XX_VLO_ImportCostDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_ImportCostDetail_ID == 0)
        {
            setXX_OTHER (Env.ZERO);	// 0
            setXX_VLO_BoardingGuide_ID (0);
            setXX_VLO_ImportCostDetail_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_ImportCostDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27576380731789L;
    /** Last Updated Timestamp 2011-01-05 14:53:35.0 */
    public static final long updatedMS = 1294255415000L;
    /** AD_Table_ID=1000308 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_ImportCostDetail");
        
    }
    ;
    
    /** TableName=XX_VLO_ImportCostDetail */
    public static final String Table_Name="XX_VLO_ImportCostDetail";
    
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
    public void setC_Country_ID (String C_Country_ID)
    {
        throw new IllegalArgumentException ("C_Country_ID is virtual column");
        
    }
    
    /** Get Country.
    @return Country */
    public String getC_Country_ID() 
    {
        return (String)get_Value("C_Country_ID");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        throw new IllegalArgumentException ("Value is virtual column");
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Difference Bank Transfer / Credit Card.
    @param XX_BankTransferDifference Difference Bank Transfer / Credit Card */
    public void setXX_BankTransferDifference (java.math.BigDecimal XX_BankTransferDifference)
    {
        throw new IllegalArgumentException ("XX_BankTransferDifference is virtual column");
        
    }
    
    /** Get Difference Bank Transfer / Credit Card.
    @return Difference Bank Transfer / Credit Card */
    public java.math.BigDecimal getXX_BankTransferDifference() 
    {
        return get_ValueAsBigDecimal("XX_BankTransferDifference");
        
    }
    
    /** Set Customs Agent Invoice Prorrated Amount.
    @param XX_CustomAgentAmountPro Customs Agent Invoice Prorrated Amount */
    public void setXX_CustomAgentAmountPro (java.math.BigDecimal XX_CustomAgentAmountPro)
    {
        throw new IllegalArgumentException ("XX_CustomAgentAmountPro is virtual column");
        
    }
    
    /** Get Customs Agent Invoice Prorrated Amount.
    @return Customs Agent Invoice Prorrated Amount */
    public java.math.BigDecimal getXX_CustomAgentAmountPro() 
    {
        return get_ValueAsBigDecimal("XX_CustomAgentAmountPro");
        
    }
    
    /** Set Customs Agent Invoice Prorrated Amount  Estimated.
    @param XX_CustomAgentAmountProEst Customs Agent Invoice Prorrated Amount  Estimated */
    public void setXX_CustomAgentAmountProEst (java.math.BigDecimal XX_CustomAgentAmountProEst)
    {
        throw new IllegalArgumentException ("XX_CustomAgentAmountProEst is virtual column");
        
    }
    
    /** Get Customs Agent Invoice Prorrated Amount  Estimated.
    @return Customs Agent Invoice Prorrated Amount  Estimated */
    public java.math.BigDecimal getXX_CustomAgentAmountProEst() 
    {
        return get_ValueAsBigDecimal("XX_CustomAgentAmountProEst");
        
    }
    
    /** Set Customs Agent Invoice Difference.
    @param XX_CUSTOMSAGENTINVOICEDIF Customs Agent Invoice Difference */
    public void setXX_CUSTOMSAGENTINVOICEDIF (java.math.BigDecimal XX_CUSTOMSAGENTINVOICEDIF)
    {
        throw new IllegalArgumentException ("XX_CUSTOMSAGENTINVOICEDIF is virtual column");
        
    }
    
    /** Get Customs Agent Invoice Difference.
    @return Customs Agent Invoice Difference */
    public java.math.BigDecimal getXX_CUSTOMSAGENTINVOICEDIF() 
    {
        return get_ValueAsBigDecimal("XX_CUSTOMSAGENTINVOICEDIF");
        
    }
    
    /** Set Date Created.
    @param XX_DATECREATED Date Created */
    public void setXX_DATECREATED (String XX_DATECREATED)
    {
        throw new IllegalArgumentException ("XX_DATECREATED is virtual column");
        
    }
    
    /** Get Date Created.
    @return Date Created */
    public String getXX_DATECREATED() 
    {
        return (String)get_Value("XX_DATECREATED");
        
    }
    
    /** Set Date Reception.
    @param XX_DATERECEPTION Date Reception */
    public void setXX_DATERECEPTION (Timestamp XX_DATERECEPTION)
    {
        set_Value ("XX_DATERECEPTION", XX_DATERECEPTION);
        
    }
    
    /** Get Date Reception.
    @return Date Reception */
    public Timestamp getXX_DATERECEPTION() 
    {
        return (Timestamp)get_Value("XX_DATERECEPTION");
        
    }
    
    /** Set Estimated bank transfer.
    @param XX_EstimatedBankTransfer Bank Transfer / Letter of Credit Estimated */
    public void setXX_EstimatedBankTransfer (java.math.BigDecimal XX_EstimatedBankTransfer)
    {
        throw new IllegalArgumentException ("XX_EstimatedBankTransfer is virtual column");
        
    }
    
    /** Get Estimated bank transfer.
    @return Bank Transfer / Letter of Credit Estimated */
    public java.math.BigDecimal getXX_EstimatedBankTransfer() 
    {
        return get_ValueAsBigDecimal("XX_EstimatedBankTransfer");
        
    }
    
    /** Set Estimated Total.
    @param XX_EstimatedTotal Estimated Total */
    public void setXX_EstimatedTotal (java.math.BigDecimal XX_EstimatedTotal)
    {
        throw new IllegalArgumentException ("XX_EstimatedTotal is virtual column");
        
    }
    
    /** Get Estimated Total.
    @return Estimated Total */
    public java.math.BigDecimal getXX_EstimatedTotal() 
    {
        return get_ValueAsBigDecimal("XX_EstimatedTotal");
        
    }
    
    /** Set Expense Account .
    @param XX_EXPENSEACCOUNT Expense Account  */
    public void setXX_EXPENSEACCOUNT (String XX_EXPENSEACCOUNT)
    {
        throw new IllegalArgumentException ("XX_EXPENSEACCOUNT is virtual column");
        
    }
    
    /** Get Expense Account .
    @return Expense Account  */
    public String getXX_EXPENSEACCOUNT() 
    {
        return (String)get_Value("XX_EXPENSEACCOUNT");
        
    }
    
    /** Set Factor.
    @param XX_Factor Factor */
    public void setXX_Factor (java.math.BigDecimal XX_Factor)
    {
        throw new IllegalArgumentException ("XX_Factor is virtual column");
        
    }
    
    /** Get Factor.
    @return Factor */
    public java.math.BigDecimal getXX_Factor() 
    {
        return get_ValueAsBigDecimal("XX_Factor");
        
    }
    
    /** Set Gross Cost.
    @param XX_GROSSCOST Gross Cost */
    public void setXX_GROSSCOST (java.math.BigDecimal XX_GROSSCOST)
    {
        throw new IllegalArgumentException ("XX_GROSSCOST is virtual column");
        
    }
    
    /** Get Gross Cost.
    @return Gross Cost */
    public java.math.BigDecimal getXX_GROSSCOST() 
    {
        return get_ValueAsBigDecimal("XX_GROSSCOST");
        
    }
    
    /** Set Insurance Amount  Difference.
    @param XX_InsuranceAmDifference Insurance Amount  Difference */
    public void setXX_InsuranceAmDifference (java.math.BigDecimal XX_InsuranceAmDifference)
    {
        throw new IllegalArgumentException ("XX_InsuranceAmDifference is virtual column");
        
    }
    
    /** Get Insurance Amount  Difference.
    @return Insurance Amount  Difference */
    public java.math.BigDecimal getXX_InsuranceAmDifference() 
    {
        return get_ValueAsBigDecimal("XX_InsuranceAmDifference");
        
    }
    
    /** Set Insurance Prorrated Amount  Estimated.
    @param XX_InsuranceAmProEst Insurance Prorrated Amount  Estimated */
    public void setXX_InsuranceAmProEst (java.math.BigDecimal XX_InsuranceAmProEst)
    {
        throw new IllegalArgumentException ("XX_InsuranceAmProEst is virtual column");
        
    }
    
    /** Get Insurance Prorrated Amount  Estimated.
    @return Insurance Prorrated Amount  Estimated */
    public java.math.BigDecimal getXX_InsuranceAmProEst() 
    {
        return get_ValueAsBigDecimal("XX_InsuranceAmProEst");
        
    }
    
    /** Set Cargo Agent Invoice Amount Difference.
    @param XX_InterFretAmountDifference Cargo Agent Invoice Amount Difference */
    public void setXX_InterFretAmountDifference (java.math.BigDecimal XX_InterFretAmountDifference)
    {
        throw new IllegalArgumentException ("XX_InterFretAmountDifference is virtual column");
        
    }
    
    /** Get Cargo Agent Invoice Amount Difference.
    @return Cargo Agent Invoice Amount Difference */
    public java.math.BigDecimal getXX_InterFretAmountDifference() 
    {
        return get_ValueAsBigDecimal("XX_InterFretAmountDifference");
        
    }
    
    /** Set Cargo Agent Invoice Prorrated Amount Estimated.
    @param XX_InterFretAmountProEst Cargo Agent Invoice Prorrated Amount Estimated */
    public void setXX_InterFretAmountProEst (java.math.BigDecimal XX_InterFretAmountProEst)
    {
        throw new IllegalArgumentException ("XX_InterFretAmountProEst is virtual column");
        
    }
    
    /** Get Cargo Agent Invoice Prorrated Amount Estimated.
    @return Cargo Agent Invoice Prorrated Amount Estimated */
    public java.math.BigDecimal getXX_InterFretAmountProEst() 
    {
        return get_ValueAsBigDecimal("XX_InterFretAmountProEst");
        
    }
    
    /** Set International Freight Invoice Prorrated Amount.
    @param XX_InterFretRealAmountPro International Freight Invoice Prorrated Amount */
    public void setXX_InterFretRealAmountPro (java.math.BigDecimal XX_InterFretRealAmountPro)
    {
        throw new IllegalArgumentException ("XX_InterFretRealAmountPro is virtual column");
        
    }
    
    /** Get International Freight Invoice Prorrated Amount.
    @return International Freight Invoice Prorrated Amount */
    public java.math.BigDecimal getXX_InterFretRealAmountPro() 
    {
        return get_ValueAsBigDecimal("XX_InterFretRealAmountPro");
        
    }
    
    /** Set National Freight Invoice Difference.
    @param XX_NacFreInvoiceDifference National Freight Invoice Difference */
    public void setXX_NacFreInvoiceDifference (java.math.BigDecimal XX_NacFreInvoiceDifference)
    {
        throw new IllegalArgumentException ("XX_NacFreInvoiceDifference is virtual column");
        
    }
    
    /** Get National Freight Invoice Difference.
    @return National Freight Invoice Difference */
    public java.math.BigDecimal getXX_NacFreInvoiceDifference() 
    {
        return get_ValueAsBigDecimal("XX_NacFreInvoiceDifference");
        
    }
    
    /** Set National Freight Invoice Prorrated Amount.
    @param XX_NacInvoiceAmountPro National Freight Invoice Prorrated Amount */
    public void setXX_NacInvoiceAmountPro (java.math.BigDecimal XX_NacInvoiceAmountPro)
    {
        throw new IllegalArgumentException ("XX_NacInvoiceAmountPro is virtual column");
        
    }
    
    /** Get National Freight Invoice Prorrated Amount.
    @return National Freight Invoice Prorrated Amount */
    public java.math.BigDecimal getXX_NacInvoiceAmountPro() 
    {
        return get_ValueAsBigDecimal("XX_NacInvoiceAmountPro");
        
    }
    
    /** Set National Freight Invoice Prorrated Amount Estimated.
    @param XX_NacInvoiceAmountProEst National Freight Invoice Prorrated Amount Estimated */
    public void setXX_NacInvoiceAmountProEst (java.math.BigDecimal XX_NacInvoiceAmountProEst)
    {
        throw new IllegalArgumentException ("XX_NacInvoiceAmountProEst is virtual column");
        
    }
    
    /** Get National Freight Invoice Prorrated Amount Estimated.
    @return National Freight Invoice Prorrated Amount Estimated */
    public java.math.BigDecimal getXX_NacInvoiceAmountProEst() 
    {
        return get_ValueAsBigDecimal("XX_NacInvoiceAmountProEst");
        
    }
    
    /** Set National Treasure Amount Difference.
    @param XX_NatTreasAmDifference National Treasure Amount Difference */
    public void setXX_NatTreasAmDifference (java.math.BigDecimal XX_NatTreasAmDifference)
    {
        throw new IllegalArgumentException ("XX_NatTreasAmDifference is virtual column");
        
    }
    
    /** Get National Treasure Amount Difference.
    @return National Treasure Amount Difference */
    public java.math.BigDecimal getXX_NatTreasAmDifference() 
    {
        return get_ValueAsBigDecimal("XX_NatTreasAmDifference");
        
    }
    
    /** Set National Treasure Prorrated Amount Estimated.
    @param XX_NatTreasAmProEst National Treasure Prorrated Amount Estimated */
    public void setXX_NatTreasAmProEst (java.math.BigDecimal XX_NatTreasAmProEst)
    {
        throw new IllegalArgumentException ("XX_NatTreasAmProEst is virtual column");
        
    }
    
    /** Get National Treasure Prorrated Amount Estimated.
    @return National Treasure Prorrated Amount Estimated */
    public java.math.BigDecimal getXX_NatTreasAmProEst() 
    {
        return get_ValueAsBigDecimal("XX_NatTreasAmProEst");
        
    }
    
    /** Set National Treasure Real Prorrated Amount.
    @param XX_NatTreasRealAmPro National Treasure Real Prorrated Amount */
    public void setXX_NatTreasRealAmPro (java.math.BigDecimal XX_NatTreasRealAmPro)
    {
        throw new IllegalArgumentException ("XX_NatTreasRealAmPro is virtual column");
        
    }
    
    /** Get National Treasure Real Prorrated Amount.
    @return National Treasure Real Prorrated Amount */
    public java.math.BigDecimal getXX_NatTreasRealAmPro() 
    {
        return get_ValueAsBigDecimal("XX_NatTreasRealAmPro");
        
    }
    
    /** Set Boarding Guide Number.
    @param XX_NROBOARDINGGUIDE Boarding Guide Number */
    public void setXX_NROBOARDINGGUIDE (String XX_NROBOARDINGGUIDE)
    {
        throw new IllegalArgumentException ("XX_NROBOARDINGGUIDE is virtual column");
        
    }
    
    /** Get Boarding Guide Number.
    @return Boarding Guide Number */
    public String getXX_NROBOARDINGGUIDE() 
    {
        return (String)get_Value("XX_NROBOARDINGGUIDE");
        
    }
    
    /** Set Other Costs.
    @param XX_OTHER Other Costs */
    public void setXX_OTHER (java.math.BigDecimal XX_OTHER)
    {
        if (XX_OTHER == null) throw new IllegalArgumentException ("XX_OTHER is mandatory.");
        set_Value ("XX_OTHER", XX_OTHER);
        
    }
    
    /** Get Other Costs.
    @return Other Costs */
    public java.math.BigDecimal getXX_OTHER() 
    {
        return get_ValueAsBigDecimal("XX_OTHER");
        
    }
    
    /** Credit Note = 10000049 */
    public static final String XX_OTHERJUSTIFICATION_CreditNote = X_Ref_XX_OtherJustification.CREDIT_NOTE.getValue();
    /** Debit Note = 10000050 */
    public static final String XX_OTHERJUSTIFICATION_DebitNote = X_Ref_XX_OtherJustification.DEBIT_NOTE.getValue();
    /** Uninvoiced Services = 10000051 */
    public static final String XX_OTHERJUSTIFICATION_UninvoicedServices = X_Ref_XX_OtherJustification.UNINVOICED_SERVICES.getValue();
    /** Transfer = 10000052 */
    public static final String XX_OTHERJUSTIFICATION_Transfer = X_Ref_XX_OtherJustification.TRANSFER.getValue();
    /** Storage = 10000053 */
    public static final String XX_OTHERJUSTIFICATION_Storage = X_Ref_XX_OtherJustification.STORAGE.getValue();
    /** Set OtherJ ustification.
    @param XX_OtherJustification OtherJ ustification */
    public void setXX_OtherJustification (String XX_OtherJustification)
    {
        if (!X_Ref_XX_OtherJustification.isValid(XX_OtherJustification))
        throw new IllegalArgumentException ("XX_OtherJustification Invalid value - " + XX_OtherJustification + " - Reference_ID=1000287 - 10000049 - 10000050 - 10000051 - 10000052 - 10000053");
        set_Value ("XX_OtherJustification", XX_OtherJustification);
        
    }
    
    /** Get OtherJ ustification.
    @return OtherJ ustification */
    public String getXX_OtherJustification() 
    {
        return (String)get_Value("XX_OtherJustification");
        
    }
    
    /** Set Real Bank Transfer.
    @param XX_RealBankTransfer bank transfer / credit card real */
    public void setXX_RealBankTransfer (java.math.BigDecimal XX_RealBankTransfer)
    {
        throw new IllegalArgumentException ("XX_RealBankTransfer is virtual column");
        
    }
    
    /** Get Real Bank Transfer.
    @return bank transfer / credit card real */
    public java.math.BigDecimal getXX_RealBankTransfer() 
    {
        return get_ValueAsBigDecimal("XX_RealBankTransfer");
        
    }
    
    /** Set Real Insurance Prorrated Amount.
    @param XX_RealInsuranceAmPro Real Insurance Prorrated Amount */
    public void setXX_RealInsuranceAmPro (java.math.BigDecimal XX_RealInsuranceAmPro)
    {
        throw new IllegalArgumentException ("XX_RealInsuranceAmPro is virtual column");
        
    }
    
    /** Get Real Insurance Prorrated Amount.
    @return Real Insurance Prorrated Amount */
    public java.math.BigDecimal getXX_RealInsuranceAmPro() 
    {
        return get_ValueAsBigDecimal("XX_RealInsuranceAmPro");
        
    }
    
    /** Set SENIAT Amount Difference.
    @param XX_SenEstAmDifference SENIAT Amount Difference */
    public void setXX_SenEstAmDifference (java.math.BigDecimal XX_SenEstAmDifference)
    {
        throw new IllegalArgumentException ("XX_SenEstAmDifference is virtual column");
        
    }
    
    /** Get SENIAT Amount Difference.
    @return SENIAT Amount Difference */
    public java.math.BigDecimal getXX_SenEstAmDifference() 
    {
        return get_ValueAsBigDecimal("XX_SenEstAmDifference");
        
    }
    
    /** Set SENIAT Prorrated Amount Estimated.
    @param XX_SenEstAmProEst SENIAT Prorrated Amount Estimated */
    public void setXX_SenEstAmProEst (java.math.BigDecimal XX_SenEstAmProEst)
    {
        throw new IllegalArgumentException ("XX_SenEstAmProEst is virtual column");
        
    }
    
    /** Get SENIAT Prorrated Amount Estimated.
    @return SENIAT Prorrated Amount Estimated */
    public java.math.BigDecimal getXX_SenEstAmProEst() 
    {
        return get_ValueAsBigDecimal("XX_SenEstAmProEst");
        
    }
    
    /** Set SENIAT Real Prorrated Amount.
    @param XX_SenRealEstAmPro SENIAT Real Prorrated Amount */
    public void setXX_SenRealEstAmPro (java.math.BigDecimal XX_SenRealEstAmPro)
    {
        throw new IllegalArgumentException ("XX_SenRealEstAmPro is virtual column");
        
    }
    
    /** Get SENIAT Real Prorrated Amount.
    @return SENIAT Real Prorrated Amount */
    public java.math.BigDecimal getXX_SenRealEstAmPro() 
    {
        return get_ValueAsBigDecimal("XX_SenRealEstAmPro");
        
    }
    
    /** Set Total Difference.
    @param XX_TotalDifference Total Difference */
    public void setXX_TotalDifference (java.math.BigDecimal XX_TotalDifference)
    {
        throw new IllegalArgumentException ("XX_TotalDifference is virtual column");
        
    }
    
    /** Get Total Difference.
    @return Total Difference */
    public java.math.BigDecimal getXX_TotalDifference() 
    {
        return get_ValueAsBigDecimal("XX_TotalDifference");
        
    }
    
    /** Set Total Real.
    @param XX_TotalReal Total Real */
    public void setXX_TotalReal (java.math.BigDecimal XX_TotalReal)
    {
        throw new IllegalArgumentException ("XX_TotalReal is virtual column");
        
    }
    
    /** Get Total Real.
    @return Total Real */
    public java.math.BigDecimal getXX_TotalReal() 
    {
        return get_ValueAsBigDecimal("XX_TotalReal");
        
    }
    
    /** Set Vendor Invoice Amount (origin currency).
    @param XX_VendorInvoiceAmount Vendor Invoice Amount (origin currency) */
    public void setXX_VendorInvoiceAmount (java.math.BigDecimal XX_VendorInvoiceAmount)
    {
        throw new IllegalArgumentException ("XX_VendorInvoiceAmount is virtual column");
        
    }
    
    /** Get Vendor Invoice Amount (origin currency).
    @return Vendor Invoice Amount (origin currency) */
    public java.math.BigDecimal getXX_VendorInvoiceAmount() 
    {
        return get_ValueAsBigDecimal("XX_VendorInvoiceAmount");
        
    }
    
    /** Set File Number.
    @param XX_VLO_BoardingGuide_ID File Number */
    public void setXX_VLO_BoardingGuide_ID (int XX_VLO_BoardingGuide_ID)
    {
        if (XX_VLO_BoardingGuide_ID < 1) throw new IllegalArgumentException ("XX_VLO_BoardingGuide_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_BoardingGuide_ID", Integer.valueOf(XX_VLO_BoardingGuide_ID));
        
    }
    
    /** Get File Number.
    @return File Number */
    public int getXX_VLO_BoardingGuide_ID() 
    {
        return get_ValueAsInt("XX_VLO_BoardingGuide_ID");
        
    }
    
    /** Set Import cost relationship detail.
    @param XX_VLO_ImportCostDetail_ID Import cost relationship detail */
    public void setXX_VLO_ImportCostDetail_ID (int XX_VLO_ImportCostDetail_ID)
    {
        if (XX_VLO_ImportCostDetail_ID < 1) throw new IllegalArgumentException ("XX_VLO_ImportCostDetail_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_ImportCostDetail_ID", Integer.valueOf(XX_VLO_ImportCostDetail_ID));
        
    }
    
    /** Get Import cost relationship detail.
    @return Import cost relationship detail */
    public int getXX_VLO_ImportCostDetail_ID() 
    {
        return get_ValueAsInt("XX_VLO_ImportCostDetail_ID");
        
    }
    
    
}
