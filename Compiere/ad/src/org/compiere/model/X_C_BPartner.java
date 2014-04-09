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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for C_BPartner
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_C_BPartner.java 8954 2010-06-16 08:11:35Z ragrawal $ */
public class X_C_BPartner extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BPartner_ID id
    @param trx transaction
    */
    public X_C_BPartner (Ctx ctx, int C_BPartner_ID, Trx trx)
    {
        super (ctx, C_BPartner_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BPartner_ID == 0)
        {
            setC_BP_Group_ID (0);
            setC_BPartner_ID (0);
            setIsCustomer (false);
            setIsEmployee (false);
            setIsOneTime (false);
            setIsProspect (false);
            setIsSalesRep (false);
            setIsSummary (false);
            setIsVendor (false);
            setName (null);
            setSOCreditStatus (null);	// X
            setSO_CreditLimit (Env.ZERO);
            setSO_CreditUsed (Env.ZERO);
            setSendEMail (false);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BPartner (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27558677818789L;
    /** Last Updated Timestamp 2010-06-15 03:25:02.0 */
    public static final long updatedMS = 1276552502000L;
    /** AD_Table_ID=291 */
    public static final int Table_ID=291;
    
    /** TableName=C_BPartner */
    public static final String Table_Name="C_BPartner";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Language.
    @param AD_Language Language for this entity */
    public void setAD_Language (String AD_Language)
    {
        set_Value ("AD_Language", AD_Language);
        
    }
    
    /** Get Language.
    @return Language for this entity */
    public String getAD_Language() 
    {
        return (String)get_Value("AD_Language");
        
    }
    
    /** Set Linked Organization.
    @param AD_OrgBP_ID The Business Partner is another Organization for explicit Inter-Org transactions */
    public void setAD_OrgBP_ID (int AD_OrgBP_ID)
    {
        if (AD_OrgBP_ID <= 0) set_Value ("AD_OrgBP_ID", null);
        else
        set_Value ("AD_OrgBP_ID", Integer.valueOf(AD_OrgBP_ID));
        
    }
    
    /** Get Linked Organization.
    @return The Business Partner is another Organization for explicit Inter-Org transactions */
    public int getAD_OrgBP_ID() 
    {
        return get_ValueAsInt("AD_OrgBP_ID");
        
    }
    
    /** Set Acquisition Cost.
    @param AcqusitionCost The cost of gaining the prospect as a customer */
    public void setAcqusitionCost (java.math.BigDecimal AcqusitionCost)
    {
        set_Value ("AcqusitionCost", AcqusitionCost);
        
    }
    
    /** Get Acquisition Cost.
    @return The cost of gaining the prospect as a customer */
    public java.math.BigDecimal getAcqusitionCost() 
    {
        return get_ValueAsBigDecimal("AcqusitionCost");
        
    }
    
    /** Set Life Time Value.
    @param ActualLifeTimeValue Actual Life Time Revenue */
    public void setActualLifeTimeValue (java.math.BigDecimal ActualLifeTimeValue)
    {
        set_Value ("ActualLifeTimeValue", ActualLifeTimeValue);
        
    }
    
    /** Get Life Time Value.
    @return Actual Life Time Revenue */
    public java.math.BigDecimal getActualLifeTimeValue() 
    {
        return get_ValueAsBigDecimal("ActualLifeTimeValue");
        
    }
    
    /** Set Partner Parent.
    @param BPartner_Parent_ID Business Partner Parent */
    public void setBPartner_Parent_ID (int BPartner_Parent_ID)
    {
        if (BPartner_Parent_ID <= 0) set_Value ("BPartner_Parent_ID", null);
        else
        set_Value ("BPartner_Parent_ID", Integer.valueOf(BPartner_Parent_ID));
        
    }
    
    /** Get Partner Parent.
    @return Business Partner Parent */
    public int getBPartner_Parent_ID() 
    {
        return get_ValueAsInt("BPartner_Parent_ID");
        
    }
    
    /** Set Business Partner Group.
    @param C_BP_Group_ID Business Partner Group */
    public void setC_BP_Group_ID (int C_BP_Group_ID)
    {
        if (C_BP_Group_ID < 1) throw new IllegalArgumentException ("C_BP_Group_ID is mandatory.");
        set_Value ("C_BP_Group_ID", Integer.valueOf(C_BP_Group_ID));
        
    }
    
    /** Get Business Partner Group.
    @return Business Partner Group */
    public int getC_BP_Group_ID() 
    {
        return get_ValueAsInt("C_BP_Group_ID");
        
    }
    
    /** Set BP Size.
    @param C_BP_Size_ID Business Partner Size */
    public void setC_BP_Size_ID (int C_BP_Size_ID)
    {
        if (C_BP_Size_ID <= 0) set_Value ("C_BP_Size_ID", null);
        else
        set_Value ("C_BP_Size_ID", Integer.valueOf(C_BP_Size_ID));
        
    }
    
    /** Get BP Size.
    @return Business Partner Size */
    public int getC_BP_Size_ID() 
    {
        return get_ValueAsInt("C_BP_Size_ID");
        
    }
    
    /** Set BP Status.
    @param C_BP_Status_ID Business Partner Status */
    public void setC_BP_Status_ID (int C_BP_Status_ID)
    {
        if (C_BP_Status_ID <= 0) set_Value ("C_BP_Status_ID", null);
        else
        set_Value ("C_BP_Status_ID", Integer.valueOf(C_BP_Status_ID));
        
    }
    
    /** Get BP Status.
    @return Business Partner Status */
    public int getC_BP_Status_ID() 
    {
        return get_ValueAsInt("C_BP_Status_ID");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Consolidation Reference.
    @param C_ConsolidationReference_ID This is a reference value that can be created and then associated with a Business Partner */
    public void setC_ConsolidationReference_ID (int C_ConsolidationReference_ID)
    {
        if (C_ConsolidationReference_ID <= 0) set_Value ("C_ConsolidationReference_ID", null);
        else
        set_Value ("C_ConsolidationReference_ID", Integer.valueOf(C_ConsolidationReference_ID));
        
    }
    
    /** Get Consolidation Reference.
    @return This is a reference value that can be created and then associated with a Business Partner */
    public int getC_ConsolidationReference_ID() 
    {
        return get_ValueAsInt("C_ConsolidationReference_ID");
        
    }
    
    /** Set Dunning.
    @param C_Dunning_ID Dunning Rules for overdue invoices */
    public void setC_Dunning_ID (int C_Dunning_ID)
    {
        if (C_Dunning_ID <= 0) set_Value ("C_Dunning_ID", null);
        else
        set_Value ("C_Dunning_ID", Integer.valueOf(C_Dunning_ID));
        
    }
    
    /** Get Dunning.
    @return Dunning Rules for overdue invoices */
    public int getC_Dunning_ID() 
    {
        return get_ValueAsInt("C_Dunning_ID");
        
    }
    
    /** Set Greeting.
    @param C_Greeting_ID Greeting to print on correspondence */
    public void setC_Greeting_ID (int C_Greeting_ID)
    {
        if (C_Greeting_ID <= 0) set_Value ("C_Greeting_ID", null);
        else
        set_Value ("C_Greeting_ID", Integer.valueOf(C_Greeting_ID));
        
    }
    
    /** Get Greeting.
    @return Greeting to print on correspondence */
    public int getC_Greeting_ID() 
    {
        return get_ValueAsInt("C_Greeting_ID");
        
    }
    
    /** Set Industry Code.
    @param C_IndustryCode_ID Business Partner Industry Classification */
    public void setC_IndustryCode_ID (int C_IndustryCode_ID)
    {
        if (C_IndustryCode_ID <= 0) set_Value ("C_IndustryCode_ID", null);
        else
        set_Value ("C_IndustryCode_ID", Integer.valueOf(C_IndustryCode_ID));
        
    }
    
    /** Get Industry Code.
    @return Business Partner Industry Classification */
    public int getC_IndustryCode_ID() 
    {
        return get_ValueAsInt("C_IndustryCode_ID");
        
    }
    
    /** Set Invoice Schedule.
    @param C_InvoiceSchedule_ID Schedule for generating Invoices */
    public void setC_InvoiceSchedule_ID (int C_InvoiceSchedule_ID)
    {
        if (C_InvoiceSchedule_ID <= 0) set_Value ("C_InvoiceSchedule_ID", null);
        else
        set_Value ("C_InvoiceSchedule_ID", Integer.valueOf(C_InvoiceSchedule_ID));
        
    }
    
    /** Get Invoice Schedule.
    @return Schedule for generating Invoices */
    public int getC_InvoiceSchedule_ID() 
    {
        return get_ValueAsInt("C_InvoiceSchedule_ID");
        
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
    
    /** Set D-U-N-S.
    @param DUNS Creditor Check (Dun & Bradstreet) Number */
    public void setDUNS (String DUNS)
    {
        set_Value ("DUNS", DUNS);
        
    }
    
    /** Get D-U-N-S.
    @return Creditor Check (Dun & Bradstreet) Number */
    public String getDUNS() 
    {
        return (String)get_Value("DUNS");
        
    }
    
    /** Availability = A */
    public static final String DELIVERYRULE_Availability = X_Ref_C_Order_DeliveryRule.AVAILABILITY.getValue();
    /** Force = F */
    public static final String DELIVERYRULE_Force = X_Ref_C_Order_DeliveryRule.FORCE.getValue();
    /** Complete Line = L */
    public static final String DELIVERYRULE_CompleteLine = X_Ref_C_Order_DeliveryRule.COMPLETE_LINE.getValue();
    /** Manual = M */
    public static final String DELIVERYRULE_Manual = X_Ref_C_Order_DeliveryRule.MANUAL.getValue();
    /** Complete Order = O */
    public static final String DELIVERYRULE_CompleteOrder = X_Ref_C_Order_DeliveryRule.COMPLETE_ORDER.getValue();
    /** After Receipt = R */
    public static final String DELIVERYRULE_AfterReceipt = X_Ref_C_Order_DeliveryRule.AFTER_RECEIPT.getValue();
    /** Set Shipping Rule.
    @param DeliveryRule Defines the timing of Shipping */
    public void setDeliveryRule (String DeliveryRule)
    {
        if (!X_Ref_C_Order_DeliveryRule.isValid(DeliveryRule))
        throw new IllegalArgumentException ("DeliveryRule Invalid value - " + DeliveryRule + " - Reference_ID=151 - A - F - L - M - O - R");
        set_Value ("DeliveryRule", DeliveryRule);
        
    }
    
    /** Get Shipping Rule.
    @return Defines the timing of Shipping */
    public String getDeliveryRule() 
    {
        return (String)get_Value("DeliveryRule");
        
    }
    
    /** Delivery = D */
    public static final String DELIVERYVIARULE_Delivery = X_Ref_C_Order_DeliveryViaRule.DELIVERY.getValue();
    /** Pickup = P */
    public static final String DELIVERYVIARULE_Pickup = X_Ref_C_Order_DeliveryViaRule.PICKUP.getValue();
    /** Shipper = S */
    public static final String DELIVERYVIARULE_Shipper = X_Ref_C_Order_DeliveryViaRule.SHIPPER.getValue();
    /** Set Shipping Method.
    @param DeliveryViaRule How the order will be delivered */
    public void setDeliveryViaRule (String DeliveryViaRule)
    {
        if (!X_Ref_C_Order_DeliveryViaRule.isValid(DeliveryViaRule))
        throw new IllegalArgumentException ("DeliveryViaRule Invalid value - " + DeliveryViaRule + " - Reference_ID=152 - D - P - S");
        set_Value ("DeliveryViaRule", DeliveryViaRule);
        
    }
    
    /** Get Shipping Method.
    @return How the order will be delivered */
    public String getDeliveryViaRule() 
    {
        return (String)get_Value("DeliveryViaRule");
        
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
    
    /** Set Document Copies.
    @param DocumentCopies Number of additional copies to be printed */
    public void setDocumentCopies (int DocumentCopies)
    {
        set_Value ("DocumentCopies", Integer.valueOf(DocumentCopies));
        
    }
    
    /** Get Document Copies.
    @return Number of additional copies to be printed */
    public int getDocumentCopies() 
    {
        return get_ValueAsInt("DocumentCopies");
        
    }
    
    /** Set First Sale.
    @param FirstSale Date of First Sale */
    public void setFirstSale (Timestamp FirstSale)
    {
        set_Value ("FirstSale", FirstSale);
        
    }
    
    /** Get First Sale.
    @return Date of First Sale */
    public Timestamp getFirstSale() 
    {
        return (Timestamp)get_Value("FirstSale");
        
    }
    
    /** Set Flat Discount %.
    @param FlatDiscount Flat discount percentage */
    public void setFlatDiscount (java.math.BigDecimal FlatDiscount)
    {
        set_Value ("FlatDiscount", FlatDiscount);
        
    }
    
    /** Get Flat Discount %.
    @return Flat discount percentage */
    public java.math.BigDecimal getFlatDiscount() 
    {
        return get_ValueAsBigDecimal("FlatDiscount");
        
    }
    
    /** Calculated = C */
    public static final String FREIGHTCOSTRULE_Calculated = X_Ref_C_Order_FreightCostRule.CALCULATED.getValue();
    /** Fix price = F */
    public static final String FREIGHTCOSTRULE_FixPrice = X_Ref_C_Order_FreightCostRule.FIX_PRICE.getValue();
    /** Freight included = I */
    public static final String FREIGHTCOSTRULE_FreightIncluded = X_Ref_C_Order_FreightCostRule.FREIGHT_INCLUDED.getValue();
    /** Line = L */
    public static final String FREIGHTCOSTRULE_Line = X_Ref_C_Order_FreightCostRule.LINE.getValue();
    /** Set Freight Cost Rule.
    @param FreightCostRule Method for charging Freight */
    public void setFreightCostRule (String FreightCostRule)
    {
        if (!X_Ref_C_Order_FreightCostRule.isValid(FreightCostRule))
        throw new IllegalArgumentException ("FreightCostRule Invalid value - " + FreightCostRule + " - Reference_ID=153 - C - F - I - L");
        set_Value ("FreightCostRule", FreightCostRule);
        
    }
    
    /** Get Freight Cost Rule.
    @return Method for charging Freight */
    public String getFreightCostRule() 
    {
        return (String)get_Value("FreightCostRule");
        
    }
    
    /** After Delivery = D */
    public static final String INVOICERULE_AfterDelivery = X_Ref_C_Order_InvoiceRule.AFTER_DELIVERY.getValue();
    /** Immediate = I */
    public static final String INVOICERULE_Immediate = X_Ref_C_Order_InvoiceRule.IMMEDIATE.getValue();
    /** After Order delivered = O */
    public static final String INVOICERULE_AfterOrderDelivered = X_Ref_C_Order_InvoiceRule.AFTER_ORDER_DELIVERED.getValue();
    /** Customer Schedule after Delivery = S */
    public static final String INVOICERULE_CustomerScheduleAfterDelivery = X_Ref_C_Order_InvoiceRule.CUSTOMER_SCHEDULE_AFTER_DELIVERY.getValue();
    /** Set Invoicing Rule.
    @param InvoiceRule Frequency and method of invoicing */
    public void setInvoiceRule (String InvoiceRule)
    {
        if (!X_Ref_C_Order_InvoiceRule.isValid(InvoiceRule))
        throw new IllegalArgumentException ("InvoiceRule Invalid value - " + InvoiceRule + " - Reference_ID=150 - D - I - O - S");
        set_Value ("InvoiceRule", InvoiceRule);
        
    }
    
    /** Get Invoicing Rule.
    @return Frequency and method of invoicing */
    public String getInvoiceRule() 
    {
        return (String)get_Value("InvoiceRule");
        
    }
    
    /** Set Invoice Print Format.
    @param Invoice_PrintFormat_ID Print Format for printing Invoices */
    public void setInvoice_PrintFormat_ID (int Invoice_PrintFormat_ID)
    {
        if (Invoice_PrintFormat_ID <= 0) set_Value ("Invoice_PrintFormat_ID", null);
        else
        set_Value ("Invoice_PrintFormat_ID", Integer.valueOf(Invoice_PrintFormat_ID));
        
    }
    
    /** Get Invoice Print Format.
    @return Print Format for printing Invoices */
    public int getInvoice_PrintFormat_ID() 
    {
        return get_ValueAsInt("Invoice_PrintFormat_ID");
        
    }
    
    /** Set Customer.
    @param IsCustomer Indicates if this Business Partner is a Customer */
    public void setIsCustomer (boolean IsCustomer)
    {
        set_Value ("IsCustomer", Boolean.valueOf(IsCustomer));
        
    }
    
    /** Get Customer.
    @return Indicates if this Business Partner is a Customer */
    public boolean isCustomer() 
    {
        return get_ValueAsBoolean("IsCustomer");
        
    }
    
    /** Set Discount Printed.
    @param IsDiscountPrinted Print Discount on Invoice and Order */
    public void setIsDiscountPrinted (boolean IsDiscountPrinted)
    {
        set_Value ("IsDiscountPrinted", Boolean.valueOf(IsDiscountPrinted));
        
    }
    
    /** Get Discount Printed.
    @return Print Discount on Invoice and Order */
    public boolean isDiscountPrinted() 
    {
        return get_ValueAsBoolean("IsDiscountPrinted");
        
    }
    
    /** Set Employee.
    @param IsEmployee Indicates if this Business Partner is an employee */
    public void setIsEmployee (boolean IsEmployee)
    {
        set_Value ("IsEmployee", Boolean.valueOf(IsEmployee));
        
    }
    
    /** Get Employee.
    @return Indicates if this Business Partner is an employee */
    public boolean isEmployee() 
    {
        return get_ValueAsBoolean("IsEmployee");
        
    }
    
    /** Set One time transaction.
    @param IsOneTime One time transaction */
    public void setIsOneTime (boolean IsOneTime)
    {
        set_Value ("IsOneTime", Boolean.valueOf(IsOneTime));
        
    }
    
    /** Get One time transaction.
    @return One time transaction */
    public boolean isOneTime() 
    {
        return get_ValueAsBoolean("IsOneTime");
        
    }
    
    /** Set Prospect.
    @param IsProspect Indicates this is a Prospect */
    public void setIsProspect (boolean IsProspect)
    {
        set_Value ("IsProspect", Boolean.valueOf(IsProspect));
        
    }
    
    /** Get Prospect.
    @return Indicates this is a Prospect */
    public boolean isProspect() 
    {
        return get_ValueAsBoolean("IsProspect");
        
    }
    
    /** Set Sales Rep.
    @param IsSalesRep Indicates if the business partner is a representative or company agent */
    public void setIsSalesRep (boolean IsSalesRep)
    {
        set_Value ("IsSalesRep", Boolean.valueOf(IsSalesRep));
        
    }
    
    /** Get Sales Rep.
    @return Indicates if the business partner is a representative or company agent */
    public boolean isSalesRep() 
    {
        return get_ValueAsBoolean("IsSalesRep");
        
    }
    
    /** Set Summary Level.
    @param IsSummary This is a summary entity */
    public void setIsSummary (boolean IsSummary)
    {
        set_Value ("IsSummary", Boolean.valueOf(IsSummary));
        
    }
    
    /** Get Summary Level.
    @return This is a summary entity */
    public boolean isSummary() 
    {
        return get_ValueAsBoolean("IsSummary");
        
    }
    
    /** Set Tax exempt.
    @param IsTaxExempt Business partner is exempt from tax */
    public void setIsTaxExempt (boolean IsTaxExempt)
    {
        set_Value ("IsTaxExempt", Boolean.valueOf(IsTaxExempt));
        
    }
    
    /** Get Tax exempt.
    @return Business partner is exempt from tax */
    public boolean isTaxExempt() 
    {
        return get_ValueAsBoolean("IsTaxExempt");
        
    }
    
    /** Set Vendor.
    @param IsVendor Indicates if this Business Partner is a Vendor */
    public void setIsVendor (boolean IsVendor)
    {
        set_Value ("IsVendor", Boolean.valueOf(IsVendor));
        
    }
    
    /** Get Vendor.
    @return Indicates if this Business Partner is a Vendor */
    public boolean isVendor() 
    {
        return get_ValueAsBoolean("IsVendor");
        
    }
    
    /** Set Discount Schema.
    @param M_DiscountSchema_ID Schema to calculate price lists or the trade discount percentage */
    public void setM_DiscountSchema_ID (int M_DiscountSchema_ID)
    {
        if (M_DiscountSchema_ID <= 0) set_Value ("M_DiscountSchema_ID", null);
        else
        set_Value ("M_DiscountSchema_ID", Integer.valueOf(M_DiscountSchema_ID));
        
    }
    
    /** Get Discount Schema.
    @return Schema to calculate price lists or the trade discount percentage */
    public int getM_DiscountSchema_ID() 
    {
        return get_ValueAsInt("M_DiscountSchema_ID");
        
    }
    
    /** Set Price List.
    @param M_PriceList_ID Unique identifier of a Price List */
    public void setM_PriceList_ID (int M_PriceList_ID)
    {
        if (M_PriceList_ID <= 0) set_Value ("M_PriceList_ID", null);
        else
        set_Value ("M_PriceList_ID", Integer.valueOf(M_PriceList_ID));
        
    }
    
    /** Get Price List.
    @return Unique identifier of a Price List */
    public int getM_PriceList_ID() 
    {
        return get_ValueAsInt("M_PriceList_ID");
        
    }
    
    /** Set Return Policy.
    @param M_ReturnPolicy_ID The Return Policy dictates the timeframe within which goods can be returned. */
    public void setM_ReturnPolicy_ID (int M_ReturnPolicy_ID)
    {
        if (M_ReturnPolicy_ID <= 0) set_Value ("M_ReturnPolicy_ID", null);
        else
        set_Value ("M_ReturnPolicy_ID", Integer.valueOf(M_ReturnPolicy_ID));
        
    }
    
    /** Get Return Policy.
    @return The Return Policy dictates the timeframe within which goods can be returned. */
    public int getM_ReturnPolicy_ID() 
    {
        return get_ValueAsInt("M_ReturnPolicy_ID");
        
    }
    
    /** Set Freight Carrier.
    @param M_Shipper_ID Method or manner of product delivery */
    public void setM_Shipper_ID (int M_Shipper_ID)
    {
        if (M_Shipper_ID <= 0) set_Value ("M_Shipper_ID", null);
        else
        set_Value ("M_Shipper_ID", Integer.valueOf(M_Shipper_ID));
        
    }
    
    /** Get Freight Carrier.
    @return Method or manner of product delivery */
    public int getM_Shipper_ID() 
    {
        return get_ValueAsInt("M_Shipper_ID");
        
    }
    
    /** Set NAICS/SIC.
    @param NAICS Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html */
    public void setNAICS (String NAICS)
    {
        set_Value ("NAICS", NAICS);
        
    }
    
    /** Get NAICS/SIC.
    @return Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html */
    public String getNAICS() 
    {
        return (String)get_Value("NAICS");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Name 2.
    @param Name2 Additional Name */
    public void setName2 (String Name2)
    {
        set_Value ("Name2", Name2);
        
    }
    
    /** Get Name 2.
    @return Additional Name */
    public String getName2() 
    {
        return (String)get_Value("Name2");
        
    }
    
    /** Set Employees.
    @param NumberEmployees Number of employees */
    public void setNumberEmployees (int NumberEmployees)
    {
        set_Value ("NumberEmployees", Integer.valueOf(NumberEmployees));
        
    }
    
    /** Get Employees.
    @return Number of employees */
    public int getNumberEmployees() 
    {
        return get_ValueAsInt("NumberEmployees");
        
    }
    
    /** Set Order Print Format.
    @param Order_PrintFormat_ID Print Format for Orders, Quotes, Offers */
    public void setOrder_PrintFormat_ID (int Order_PrintFormat_ID)
    {
        if (Order_PrintFormat_ID <= 0) set_Value ("Order_PrintFormat_ID", null);
        else
        set_Value ("Order_PrintFormat_ID", Integer.valueOf(Order_PrintFormat_ID));
        
    }
    
    /** Get Order Print Format.
    @return Print Format for Orders, Quotes, Offers */
    public int getOrder_PrintFormat_ID() 
    {
        return get_ValueAsInt("Order_PrintFormat_ID");
        
    }
    
    /** Set Order Reference.
    @param POReference Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner */
    public void setPOReference (String POReference)
    {
        set_Value ("POReference", POReference);
        
    }
    
    /** Get Order Reference.
    @return Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner */
    public String getPOReference() 
    {
        return (String)get_Value("POReference");
        
    }
    
    /** Set PO Discount Schema.
    @param PO_DiscountSchema_ID Schema to calculate the purchase trade discount percentage */
    public void setPO_DiscountSchema_ID (int PO_DiscountSchema_ID)
    {
        if (PO_DiscountSchema_ID <= 0) set_Value ("PO_DiscountSchema_ID", null);
        else
        set_Value ("PO_DiscountSchema_ID", Integer.valueOf(PO_DiscountSchema_ID));
        
    }
    
    /** Get PO Discount Schema.
    @return Schema to calculate the purchase trade discount percentage */
    public int getPO_DiscountSchema_ID() 
    {
        return get_ValueAsInt("PO_DiscountSchema_ID");
        
    }
    
    /** Set PO Payment Term.
    @param PO_PaymentTerm_ID Payment rules for a purchase order */
    public void setPO_PaymentTerm_ID (int PO_PaymentTerm_ID)
    {
        if (PO_PaymentTerm_ID <= 0) set_Value ("PO_PaymentTerm_ID", null);
        else
        set_Value ("PO_PaymentTerm_ID", Integer.valueOf(PO_PaymentTerm_ID));
        
    }
    
    /** Get PO Payment Term.
    @return Payment rules for a purchase order */
    public int getPO_PaymentTerm_ID() 
    {
        return get_ValueAsInt("PO_PaymentTerm_ID");
        
    }
    
    /** Set Purchase Pricelist.
    @param PO_PriceList_ID Price List used by this Business Partner */
    public void setPO_PriceList_ID (int PO_PriceList_ID)
    {
        if (PO_PriceList_ID <= 0) set_Value ("PO_PriceList_ID", null);
        else
        set_Value ("PO_PriceList_ID", Integer.valueOf(PO_PriceList_ID));
        
    }
    
    /** Get Purchase Pricelist.
    @return Price List used by this Business Partner */
    public int getPO_PriceList_ID() 
    {
        return get_ValueAsInt("PO_PriceList_ID");
        
    }
    
    /** Set Vendor Return Policy.
    @param PO_ReturnPolicy_ID The Return Policy that applies to goods being returned to the business partner. */
    public void setPO_ReturnPolicy_ID (int PO_ReturnPolicy_ID)
    {
        if (PO_ReturnPolicy_ID <= 0) set_Value ("PO_ReturnPolicy_ID", null);
        else
        set_Value ("PO_ReturnPolicy_ID", Integer.valueOf(PO_ReturnPolicy_ID));
        
    }
    
    /** Get Vendor Return Policy.
    @return The Return Policy that applies to goods being returned to the business partner. */
    public int getPO_ReturnPolicy_ID() 
    {
        return get_ValueAsInt("PO_ReturnPolicy_ID");
        
    }
    
    /** Cash = B */
    public static final String PAYMENTRULE_Cash = X_Ref__Payment_Rule.CASH.getValue();
    /** Direct Debit = D */
    public static final String PAYMENTRULE_DirectDebit = X_Ref__Payment_Rule.DIRECT_DEBIT.getValue();
    /** Credit Card = K */
    public static final String PAYMENTRULE_CreditCard = X_Ref__Payment_Rule.CREDIT_CARD.getValue();
    /** On Credit = P */
    public static final String PAYMENTRULE_OnCredit = X_Ref__Payment_Rule.ON_CREDIT.getValue();
    /** Check = S */
    public static final String PAYMENTRULE_Check = X_Ref__Payment_Rule.CHECK.getValue();
    /** Direct Deposit = T */
    public static final String PAYMENTRULE_DirectDeposit = X_Ref__Payment_Rule.DIRECT_DEPOSIT.getValue();
    /** Set Payment Method.
    @param PaymentRule How you pay the invoice */
    public void setPaymentRule (String PaymentRule)
    {
        if (!X_Ref__Payment_Rule.isValid(PaymentRule))
        throw new IllegalArgumentException ("PaymentRule Invalid value - " + PaymentRule + " - Reference_ID=195 - B - D - K - P - S - T");
        set_Value ("PaymentRule", PaymentRule);
        
    }
    
    /** Get Payment Method.
    @return How you pay the invoice */
    public String getPaymentRule() 
    {
        return (String)get_Value("PaymentRule");
        
    }
    
    /** Cash = B */
    public static final String PAYMENTRULEPO_Cash = X_Ref__Payment_Rule.CASH.getValue();
    /** Direct Debit = D */
    public static final String PAYMENTRULEPO_DirectDebit = X_Ref__Payment_Rule.DIRECT_DEBIT.getValue();
    /** Credit Card = K */
    public static final String PAYMENTRULEPO_CreditCard = X_Ref__Payment_Rule.CREDIT_CARD.getValue();
    /** On Credit = P */
    public static final String PAYMENTRULEPO_OnCredit = X_Ref__Payment_Rule.ON_CREDIT.getValue();
    /** Check = S */
    public static final String PAYMENTRULEPO_Check = X_Ref__Payment_Rule.CHECK.getValue();
    /** Direct Deposit = T */
    public static final String PAYMENTRULEPO_DirectDeposit = X_Ref__Payment_Rule.DIRECT_DEPOSIT.getValue();
    /** Set Payment Rule.
    @param PaymentRulePO Purchase payment option */
    public void setPaymentRulePO (String PaymentRulePO)
    {
        if (!X_Ref__Payment_Rule.isValid(PaymentRulePO))
        throw new IllegalArgumentException ("PaymentRulePO Invalid value - " + PaymentRulePO + " - Reference_ID=195 - B - D - K - P - S - T");
        set_Value ("PaymentRulePO", PaymentRulePO);
        
    }
    
    /** Get Payment Rule.
    @return Purchase payment option */
    public String getPaymentRulePO() 
    {
        return (String)get_Value("PaymentRulePO");
        
    }
    
    /** Set Potential Life Time Value.
    @param PotentialLifeTimeValue Total Revenue expected */
    public void setPotentialLifeTimeValue (java.math.BigDecimal PotentialLifeTimeValue)
    {
        set_Value ("PotentialLifeTimeValue", PotentialLifeTimeValue);
        
    }
    
    /** Get Potential Life Time Value.
    @return Total Revenue expected */
    public java.math.BigDecimal getPotentialLifeTimeValue() 
    {
        return get_ValueAsBigDecimal("PotentialLifeTimeValue");
        
    }
    
    /** Not Rated = - */
    public static final String RATING_NotRated = X_Ref__Rating__ABC_.NOT_RATED.getValue();
    /** A = A */
    public static final String RATING_A = X_Ref__Rating__ABC_.A.getValue();
    /** B = B */
    public static final String RATING_B = X_Ref__Rating__ABC_.B.getValue();
    /** C = C */
    public static final String RATING_C = X_Ref__Rating__ABC_.C.getValue();
    /** Set Rating.
    @param Rating Classification or Importance */
    public void setRating (String Rating)
    {
        if (!X_Ref__Rating__ABC_.isValid(Rating))
        throw new IllegalArgumentException ("Rating Invalid value - " + Rating + " - Reference_ID=419 - - - A - B - C");
        set_Value ("Rating", Rating);
        
    }
    
    /** Get Rating.
    @return Classification or Importance */
    public String getRating() 
    {
        return (String)get_Value("Rating");
        
    }
    
    /** Set Reference No.
    @param ReferenceNo Your customer or vendor number at the Business Partner's site */
    public void setReferenceNo (String ReferenceNo)
    {
        set_Value ("ReferenceNo", ReferenceNo);
        
    }
    
    /** Get Reference No.
    @return Your customer or vendor number at the Business Partner's site */
    public String getReferenceNo() 
    {
        return (String)get_Value("ReferenceNo");
        
    }
    
    /** Credit Hold = H */
    public static final String SOCREDITSTATUS_CreditHold = X_Ref_C_BPartner_SOCreditStatus.CREDIT_HOLD.getValue();
    /** Credit OK = O */
    public static final String SOCREDITSTATUS_CreditOK = X_Ref_C_BPartner_SOCreditStatus.CREDIT_OK.getValue();
    /** Credit Stop = S */
    public static final String SOCREDITSTATUS_CreditStop = X_Ref_C_BPartner_SOCreditStatus.CREDIT_STOP.getValue();
    /** Credit Watch = W */
    public static final String SOCREDITSTATUS_CreditWatch = X_Ref_C_BPartner_SOCreditStatus.CREDIT_WATCH.getValue();
    /** No Credit Check = X */
    public static final String SOCREDITSTATUS_NoCreditCheck = X_Ref_C_BPartner_SOCreditStatus.NO_CREDIT_CHECK.getValue();
    /** Set Credit Status.
    @param SOCreditStatus Business Partner Credit Status */
    public void setSOCreditStatus (String SOCreditStatus)
    {
        if (SOCreditStatus == null) throw new IllegalArgumentException ("SOCreditStatus is mandatory");
        if (!X_Ref_C_BPartner_SOCreditStatus.isValid(SOCreditStatus))
        throw new IllegalArgumentException ("SOCreditStatus Invalid value - " + SOCreditStatus + " - Reference_ID=289 - H - O - S - W - X");
        set_Value ("SOCreditStatus", SOCreditStatus);
        
    }
    
    /** Get Credit Status.
    @return Business Partner Credit Status */
    public String getSOCreditStatus() 
    {
        return (String)get_Value("SOCreditStatus");
        
    }
    
    /** Set Credit Limit.
    @param SO_CreditLimit Total outstanding invoice amounts allowed */
    public void setSO_CreditLimit (java.math.BigDecimal SO_CreditLimit)
    {
        if (SO_CreditLimit == null) throw new IllegalArgumentException ("SO_CreditLimit is mandatory.");
        set_Value ("SO_CreditLimit", SO_CreditLimit);
        
    }
    
    /** Get Credit Limit.
    @return Total outstanding invoice amounts allowed */
    public java.math.BigDecimal getSO_CreditLimit() 
    {
        return get_ValueAsBigDecimal("SO_CreditLimit");
        
    }
    
    /** Set Credit Used.
    @param SO_CreditUsed Current open balance */
    public void setSO_CreditUsed (java.math.BigDecimal SO_CreditUsed)
    {
        if (SO_CreditUsed == null) throw new IllegalArgumentException ("SO_CreditUsed is mandatory.");
        set_ValueNoCheck ("SO_CreditUsed", SO_CreditUsed);
        
    }
    
    /** Get Credit Used.
    @return Current open balance */
    public java.math.BigDecimal getSO_CreditUsed() 
    {
        return get_ValueAsBigDecimal("SO_CreditUsed");
        
    }
    
    /** Set Order Description.
    @param SO_Description Description to be used on orders */
    public void setSO_Description (String SO_Description)
    {
        set_Value ("SO_Description", SO_Description);
        
    }
    
    /** Get Order Description.
    @return Description to be used on orders */
    public String getSO_Description() 
    {
        return (String)get_Value("SO_Description");
        
    }
    
    /** Set Representative.
    @param SalesRep_ID Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public void setSalesRep_ID (int SalesRep_ID)
    {
        if (SalesRep_ID <= 0) set_Value ("SalesRep_ID", null);
        else
        set_Value ("SalesRep_ID", Integer.valueOf(SalesRep_ID));
        
    }
    
    /** Get Representative.
    @return Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public int getSalesRep_ID() 
    {
        return get_ValueAsInt("SalesRep_ID");
        
    }
    
    /** Set Sales Volume.
    @param SalesVolume Total Volume of Sales in Thousands of Base Currency */
    public void setSalesVolume (int SalesVolume)
    {
        set_Value ("SalesVolume", Integer.valueOf(SalesVolume));
        
    }
    
    /** Get Sales Volume.
    @return Total Volume of Sales in Thousands of Base Currency */
    public int getSalesVolume() 
    {
        return get_ValueAsInt("SalesVolume");
        
    }
    
    /** Set Send EMail.
    @param SendEMail Enable sending Document EMail */
    public void setSendEMail (boolean SendEMail)
    {
        set_Value ("SendEMail", Boolean.valueOf(SendEMail));
        
    }
    
    /** Get Send EMail.
    @return Enable sending Document EMail */
    public boolean isSendEMail() 
    {
        return get_ValueAsBoolean("SendEMail");
        
    }
    
    /** Set Share.
    @param ShareOfCustomer Share of Customer's business as a percentage */
    public void setShareOfCustomer (int ShareOfCustomer)
    {
        set_Value ("ShareOfCustomer", Integer.valueOf(ShareOfCustomer));
        
    }
    
    /** Get Share.
    @return Share of Customer's business as a percentage */
    public int getShareOfCustomer() 
    {
        return get_ValueAsInt("ShareOfCustomer");
        
    }
    
    /** Set Min Shelf Life %.
    @param ShelfLifeMinPct Minimum Shelf Life in percent based on Product Instance Guarantee Date */
    public void setShelfLifeMinPct (int ShelfLifeMinPct)
    {
        set_Value ("ShelfLifeMinPct", Integer.valueOf(ShelfLifeMinPct));
        
    }
    
    /** Get Min Shelf Life %.
    @return Minimum Shelf Life in percent based on Product Instance Guarantee Date */
    public int getShelfLifeMinPct() 
    {
        return get_ValueAsInt("ShelfLifeMinPct");
        
    }
    
    /** Set Tax ID.
    @param TaxID Tax Identification */
    public void setTaxID (String TaxID)
    {
        set_Value ("TaxID", TaxID);
        
    }
    
    /** Get Tax ID.
    @return Tax Identification */
    public String getTaxID() 
    {
        return (String)get_Value("TaxID");
        
    }
    
    /** Set Open Balance.
    @param TotalOpenBalance Total Open Balance Amount in primary Accounting Currency */
    public void setTotalOpenBalance (java.math.BigDecimal TotalOpenBalance)
    {
        set_Value ("TotalOpenBalance", TotalOpenBalance);
        
    }
    
    /** Get Open Balance.
    @return Total Open Balance Amount in primary Accounting Currency */
    public java.math.BigDecimal getTotalOpenBalance() 
    {
        return get_ValueAsBigDecimal("TotalOpenBalance");
        
    }
    
    /** Set URL.
    @param URL Full URL address - e.g. http://www.compiere.org */
    public void setURL (String URL)
    {
        set_Value ("URL", URL);
        
    }
    
    /** Get URL.
    @return Full URL address - e.g. http://www.compiere.org */
    public String getURL() 
    {
        return (String)get_Value("URL");
        
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
    
    
}
