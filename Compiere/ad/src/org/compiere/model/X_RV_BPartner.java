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
/** Generated Model for RV_BPartner
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_RV_BPartner.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_RV_BPartner extends PO
{
    /** Standard Constructor
    @param ctx context
    @param RV_BPartner_ID id
    @param trx transaction
    */
    public X_RV_BPartner (Ctx ctx, int RV_BPartner_ID, Trx trx)
    {
        super (ctx, RV_BPartner_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (RV_BPartner_ID == 0)
        {
            setAD_User_ID (0);
            setC_BP_Group_ID (0);
            setC_BPartner_ID (0);
            setC_BPartner_Location_ID (0);
            setC_Country_ID (0);
            setContactName (null);
            setCountryName (null);
            setIsCustomer (false);
            setIsEmployee (false);
            setIsFullBPAccess (false);
            setIsOneTime (false);
            setIsProspect (false);
            setIsSalesRep (false);
            setIsSummary (false);
            setIsVendor (false);
            setLDAPUser (false);
            setName (null);
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
    public X_RV_BPartner (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27513999300789L;
    /** Last Updated Timestamp 2009-01-13 11:13:04.0 */
    public static final long updatedMS = 1231873984000L;
    /** AD_Table_ID=520 */
    public static final int Table_ID=520;
    
    /** TableName=RV_BPartner */
    public static final String Table_Name="RV_BPartner";
    
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
        set_ValueNoCheck ("AD_Language", AD_Language);
        
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
        if (AD_OrgBP_ID <= 0) set_ValueNoCheck ("AD_OrgBP_ID", null);
        else
        set_ValueNoCheck ("AD_OrgBP_ID", Integer.valueOf(AD_OrgBP_ID));
        
    }
    
    /** Get Linked Organization.
    @return The Business Partner is another Organization for explicit Inter-Org transactions */
    public int getAD_OrgBP_ID() 
    {
        return get_ValueAsInt("AD_OrgBP_ID");
        
    }
    
    /** Set Trx Organization.
    @param AD_OrgTrx_ID Performing or initiating organization */
    public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
    {
        if (AD_OrgTrx_ID <= 0) set_ValueNoCheck ("AD_OrgTrx_ID", null);
        else
        set_ValueNoCheck ("AD_OrgTrx_ID", Integer.valueOf(AD_OrgTrx_ID));
        
    }
    
    /** Get Trx Organization.
    @return Performing or initiating organization */
    public int getAD_OrgTrx_ID() 
    {
        return get_ValueAsInt("AD_OrgTrx_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_ValueNoCheck ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Acquisition Cost.
    @param AcqusitionCost The cost of gaining the prospect as a customer */
    public void setAcqusitionCost (java.math.BigDecimal AcqusitionCost)
    {
        set_ValueNoCheck ("AcqusitionCost", AcqusitionCost);
        
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
        set_ValueNoCheck ("ActualLifeTimeValue", ActualLifeTimeValue);
        
    }
    
    /** Get Life Time Value.
    @return Actual Life Time Revenue */
    public java.math.BigDecimal getActualLifeTimeValue() 
    {
        return get_ValueAsBigDecimal("ActualLifeTimeValue");
        
    }
    
    /** Set Address 1.
    @param Address1 Address line 1 for this location */
    public void setAddress1 (String Address1)
    {
        set_ValueNoCheck ("Address1", Address1);
        
    }
    
    /** Get Address 1.
    @return Address line 1 for this location */
    public String getAddress1() 
    {
        return (String)get_Value("Address1");
        
    }
    
    /** Set Address 2.
    @param Address2 Address line 2 for this location */
    public void setAddress2 (String Address2)
    {
        set_ValueNoCheck ("Address2", Address2);
        
    }
    
    /** Get Address 2.
    @return Address line 2 for this location */
    public String getAddress2() 
    {
        return (String)get_Value("Address2");
        
    }
    
    /** Set Address 3.
    @param Address3 Address Line 3 for the location */
    public void setAddress3 (String Address3)
    {
        set_ValueNoCheck ("Address3", Address3);
        
    }
    
    /** Get Address 3.
    @return Address Line 3 for the location */
    public String getAddress3() 
    {
        return (String)get_Value("Address3");
        
    }
    
    /** Set BP Contact Greeting.
    @param BPContactGreeting Greeting for Business Partner Contact */
    public void setBPContactGreeting (int BPContactGreeting)
    {
        set_ValueNoCheck ("BPContactGreeting", Integer.valueOf(BPContactGreeting));
        
    }
    
    /** Get BP Contact Greeting.
    @return Greeting for Business Partner Contact */
    public int getBPContactGreeting() 
    {
        return get_ValueAsInt("BPContactGreeting");
        
    }
    
    /** Set Partner Parent.
    @param BPartner_Parent_ID Business Partner Parent */
    public void setBPartner_Parent_ID (int BPartner_Parent_ID)
    {
        if (BPartner_Parent_ID <= 0) set_ValueNoCheck ("BPartner_Parent_ID", null);
        else
        set_ValueNoCheck ("BPartner_Parent_ID", Integer.valueOf(BPartner_Parent_ID));
        
    }
    
    /** Get Partner Parent.
    @return Business Partner Parent */
    public int getBPartner_Parent_ID() 
    {
        return get_ValueAsInt("BPartner_Parent_ID");
        
    }
    
    /** Set Birthday.
    @param Birthday Birthday or Anniversary day */
    public void setBirthday (Timestamp Birthday)
    {
        set_ValueNoCheck ("Birthday", Birthday);
        
    }
    
    /** Get Birthday.
    @return Birthday or Anniversary day */
    public Timestamp getBirthday() 
    {
        return (Timestamp)get_Value("Birthday");
        
    }
    
    /** Set Business Partner Group.
    @param C_BP_Group_ID Business Partner Group */
    public void setC_BP_Group_ID (int C_BP_Group_ID)
    {
        if (C_BP_Group_ID < 1) throw new IllegalArgumentException ("C_BP_Group_ID is mandatory.");
        set_ValueNoCheck ("C_BP_Group_ID", Integer.valueOf(C_BP_Group_ID));
        
    }
    
    /** Get Business Partner Group.
    @return Business Partner Group */
    public int getC_BP_Group_ID() 
    {
        return get_ValueAsInt("C_BP_Group_ID");
        
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
    
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID < 1) throw new IllegalArgumentException ("C_BPartner_Location_ID is mandatory.");
        set_ValueNoCheck ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
    }
    
    /** Set Country.
    @param C_Country_ID Country */
    public void setC_Country_ID (int C_Country_ID)
    {
        if (C_Country_ID < 1) throw new IllegalArgumentException ("C_Country_ID is mandatory.");
        set_ValueNoCheck ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
    }
    
    /** Set Dunning.
    @param C_Dunning_ID Dunning Rules for overdue invoices */
    public void setC_Dunning_ID (int C_Dunning_ID)
    {
        if (C_Dunning_ID <= 0) set_ValueNoCheck ("C_Dunning_ID", null);
        else
        set_ValueNoCheck ("C_Dunning_ID", Integer.valueOf(C_Dunning_ID));
        
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
        if (C_Greeting_ID <= 0) set_ValueNoCheck ("C_Greeting_ID", null);
        else
        set_ValueNoCheck ("C_Greeting_ID", Integer.valueOf(C_Greeting_ID));
        
    }
    
    /** Get Greeting.
    @return Greeting to print on correspondence */
    public int getC_Greeting_ID() 
    {
        return get_ValueAsInt("C_Greeting_ID");
        
    }
    
    /** Set Invoice Schedule.
    @param C_InvoiceSchedule_ID Schedule for generating Invoices */
    public void setC_InvoiceSchedule_ID (int C_InvoiceSchedule_ID)
    {
        if (C_InvoiceSchedule_ID <= 0) set_ValueNoCheck ("C_InvoiceSchedule_ID", null);
        else
        set_ValueNoCheck ("C_InvoiceSchedule_ID", Integer.valueOf(C_InvoiceSchedule_ID));
        
    }
    
    /** Get Invoice Schedule.
    @return Schedule for generating Invoices */
    public int getC_InvoiceSchedule_ID() 
    {
        return get_ValueAsInt("C_InvoiceSchedule_ID");
        
    }
    
    /** Set Position.
    @param C_Job_ID Job Position */
    public void setC_Job_ID (int C_Job_ID)
    {
        if (C_Job_ID <= 0) set_ValueNoCheck ("C_Job_ID", null);
        else
        set_ValueNoCheck ("C_Job_ID", Integer.valueOf(C_Job_ID));
        
    }
    
    /** Get Position.
    @return Job Position */
    public int getC_Job_ID() 
    {
        return get_ValueAsInt("C_Job_ID");
        
    }
    
    /** Set Payment Term.
    @param C_PaymentTerm_ID The terms of Payment (timing, discount) */
    public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
    {
        if (C_PaymentTerm_ID <= 0) set_ValueNoCheck ("C_PaymentTerm_ID", null);
        else
        set_ValueNoCheck ("C_PaymentTerm_ID", Integer.valueOf(C_PaymentTerm_ID));
        
    }
    
    /** Get Payment Term.
    @return The terms of Payment (timing, discount) */
    public int getC_PaymentTerm_ID() 
    {
        return get_ValueAsInt("C_PaymentTerm_ID");
        
    }
    
    /** Set Region.
    @param C_Region_ID Identifies a geographical Region */
    public void setC_Region_ID (int C_Region_ID)
    {
        if (C_Region_ID <= 0) set_ValueNoCheck ("C_Region_ID", null);
        else
        set_ValueNoCheck ("C_Region_ID", Integer.valueOf(C_Region_ID));
        
    }
    
    /** Get Region.
    @return Identifies a geographical Region */
    public int getC_Region_ID() 
    {
        return get_ValueAsInt("C_Region_ID");
        
    }
    
    /** Set City Name.
    @param City Identifies a City */
    public void setCity (String City)
    {
        set_ValueNoCheck ("City", City);
        
    }
    
    /** Get City Name.
    @return Identifies a City */
    public String getCity() 
    {
        return (String)get_Value("City");
        
    }
    
    /** Set Comments.
    @param Comments Comments or additional information */
    public void setComments (String Comments)
    {
        set_ValueNoCheck ("Comments", Comments);
        
    }
    
    /** Get Comments.
    @return Comments or additional information */
    public String getComments() 
    {
        return (String)get_Value("Comments");
        
    }
    
    /** Set Contact Description.
    @param ContactDescription Description of Contact */
    public void setContactDescription (String ContactDescription)
    {
        set_ValueNoCheck ("ContactDescription", ContactDescription);
        
    }
    
    /** Get Contact Description.
    @return Description of Contact */
    public String getContactDescription() 
    {
        return (String)get_Value("ContactDescription");
        
    }
    
    /** Set Contact Name.
    @param ContactName Business Partner Contact Name */
    public void setContactName (String ContactName)
    {
        if (ContactName == null) throw new IllegalArgumentException ("ContactName is mandatory.");
        set_ValueNoCheck ("ContactName", ContactName);
        
    }
    
    /** Get Contact Name.
    @return Business Partner Contact Name */
    public String getContactName() 
    {
        return (String)get_Value("ContactName");
        
    }
    
    /** Set Country Name.
    @param CountryName Country Name */
    public void setCountryName (String CountryName)
    {
        if (CountryName == null) throw new IllegalArgumentException ("CountryName is mandatory.");
        set_ValueNoCheck ("CountryName", CountryName);
        
    }
    
    /** Get Country Name.
    @return Country Name */
    public String getCountryName() 
    {
        return (String)get_Value("CountryName");
        
    }
    
    /** Set D-U-N-S.
    @param DUNS Creditor Check (Dun & Bradstreet) Number */
    public void setDUNS (String DUNS)
    {
        set_ValueNoCheck ("DUNS", DUNS);
        
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
        set_ValueNoCheck ("DeliveryRule", DeliveryRule);
        
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
        set_ValueNoCheck ("DeliveryViaRule", DeliveryViaRule);
        
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
        set_ValueNoCheck ("Description", Description);
        
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
        set_ValueNoCheck ("DocumentCopies", Integer.valueOf(DocumentCopies));
        
    }
    
    /** Get Document Copies.
    @return Number of additional copies to be printed */
    public int getDocumentCopies() 
    {
        return get_ValueAsInt("DocumentCopies");
        
    }
    
    /** Set EMail Address.
    @param EMail Electronic Mail Address */
    public void setEMail (String EMail)
    {
        set_ValueNoCheck ("EMail", EMail);
        
    }
    
    /** Get EMail Address.
    @return Electronic Mail Address */
    public String getEMail() 
    {
        return (String)get_Value("EMail");
        
    }
    
    /** Set EMail User ID.
    @param EMailUser User Name (ID) in the Mail System */
    public void setEMailUser (String EMailUser)
    {
        set_ValueNoCheck ("EMailUser", EMailUser);
        
    }
    
    /** Get EMail User ID.
    @return User Name (ID) in the Mail System */
    public String getEMailUser() 
    {
        return (String)get_Value("EMailUser");
        
    }
    
    /** Set Verification Info.
    @param EMailVerify Verification information of EMail Address */
    public void setEMailVerify (String EMailVerify)
    {
        set_ValueNoCheck ("EMailVerify", EMailVerify);
        
    }
    
    /** Get Verification Info.
    @return Verification information of EMail Address */
    public String getEMailVerify() 
    {
        return (String)get_Value("EMailVerify");
        
    }
    
    /** Set EMail Verify.
    @param EMailVerifyDate Date Email was verified */
    public void setEMailVerifyDate (Timestamp EMailVerifyDate)
    {
        set_ValueNoCheck ("EMailVerifyDate", EMailVerifyDate);
        
    }
    
    /** Get EMail Verify.
    @return Date Email was verified */
    public Timestamp getEMailVerifyDate() 
    {
        return (Timestamp)get_Value("EMailVerifyDate");
        
    }
    
    /** Set Fax.
    @param Fax Facsimile number */
    public void setFax (String Fax)
    {
        set_ValueNoCheck ("Fax", Fax);
        
    }
    
    /** Get Fax.
    @return Facsimile number */
    public String getFax() 
    {
        return (String)get_Value("Fax");
        
    }
    
    /** Set First Sale.
    @param FirstSale Date of First Sale */
    public void setFirstSale (Timestamp FirstSale)
    {
        set_ValueNoCheck ("FirstSale", FirstSale);
        
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
        set_ValueNoCheck ("FlatDiscount", FlatDiscount);
        
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
        set_ValueNoCheck ("FreightCostRule", FreightCostRule);
        
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
        set_ValueNoCheck ("InvoiceRule", InvoiceRule);
        
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
        if (Invoice_PrintFormat_ID <= 0) set_ValueNoCheck ("Invoice_PrintFormat_ID", null);
        else
        set_ValueNoCheck ("Invoice_PrintFormat_ID", Integer.valueOf(Invoice_PrintFormat_ID));
        
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
        set_ValueNoCheck ("IsCustomer", Boolean.valueOf(IsCustomer));
        
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
        set_ValueNoCheck ("IsDiscountPrinted", Boolean.valueOf(IsDiscountPrinted));
        
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
        set_ValueNoCheck ("IsEmployee", Boolean.valueOf(IsEmployee));
        
    }
    
    /** Get Employee.
    @return Indicates if this Business Partner is an employee */
    public boolean isEmployee() 
    {
        return get_ValueAsBoolean("IsEmployee");
        
    }
    
    /** Set Full BP Access.
    @param IsFullBPAccess The user/contact has full access to Business Partner information and resources */
    public void setIsFullBPAccess (boolean IsFullBPAccess)
    {
        set_ValueNoCheck ("IsFullBPAccess", Boolean.valueOf(IsFullBPAccess));
        
    }
    
    /** Get Full BP Access.
    @return The user/contact has full access to Business Partner information and resources */
    public boolean isFullBPAccess() 
    {
        return get_ValueAsBoolean("IsFullBPAccess");
        
    }
    
    /** Set One time transaction.
    @param IsOneTime One time transaction */
    public void setIsOneTime (boolean IsOneTime)
    {
        set_ValueNoCheck ("IsOneTime", Boolean.valueOf(IsOneTime));
        
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
        set_ValueNoCheck ("IsProspect", Boolean.valueOf(IsProspect));
        
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
        set_ValueNoCheck ("IsSalesRep", Boolean.valueOf(IsSalesRep));
        
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
        set_ValueNoCheck ("IsSummary", Boolean.valueOf(IsSummary));
        
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
        set_ValueNoCheck ("IsTaxExempt", Boolean.valueOf(IsTaxExempt));
        
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
        set_ValueNoCheck ("IsVendor", Boolean.valueOf(IsVendor));
        
    }
    
    /** Get Vendor.
    @return Indicates if this Business Partner is a Vendor */
    public boolean isVendor() 
    {
        return get_ValueAsBoolean("IsVendor");
        
    }
    
    /** Set LDAP User Name.
    @param LDAPUser User Name used for authorization via LDAP (directory) services */
    public void setLDAPUser (boolean LDAPUser)
    {
        set_ValueNoCheck ("LDAPUser", Boolean.valueOf(LDAPUser));
        
    }
    
    /** Get LDAP User Name.
    @return User Name used for authorization via LDAP (directory) services */
    public boolean isLDAPUser() 
    {
        return get_ValueAsBoolean("LDAPUser");
        
    }
    
    /** Set Last Contact.
    @param LastContact Date this individual was last contacted */
    public void setLastContact (Timestamp LastContact)
    {
        set_ValueNoCheck ("LastContact", LastContact);
        
    }
    
    /** Get Last Contact.
    @return Date this individual was last contacted */
    public Timestamp getLastContact() 
    {
        return (Timestamp)get_Value("LastContact");
        
    }
    
    /** Set Last Result.
    @param LastResult Result of last contact */
    public void setLastResult (String LastResult)
    {
        set_ValueNoCheck ("LastResult", LastResult);
        
    }
    
    /** Get Last Result.
    @return Result of last contact */
    public String getLastResult() 
    {
        return (String)get_Value("LastResult");
        
    }
    
    /** Set Discount Schema.
    @param M_DiscountSchema_ID Schema to calculate price lists or the trade discount percentage */
    public void setM_DiscountSchema_ID (int M_DiscountSchema_ID)
    {
        if (M_DiscountSchema_ID <= 0) set_ValueNoCheck ("M_DiscountSchema_ID", null);
        else
        set_ValueNoCheck ("M_DiscountSchema_ID", Integer.valueOf(M_DiscountSchema_ID));
        
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
        if (M_PriceList_ID <= 0) set_ValueNoCheck ("M_PriceList_ID", null);
        else
        set_ValueNoCheck ("M_PriceList_ID", Integer.valueOf(M_PriceList_ID));
        
    }
    
    /** Get Price List.
    @return Unique identifier of a Price List */
    public int getM_PriceList_ID() 
    {
        return get_ValueAsInt("M_PriceList_ID");
        
    }
    
    /** Set NAICS/SIC.
    @param NAICS Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html */
    public void setNAICS (String NAICS)
    {
        set_ValueNoCheck ("NAICS", NAICS);
        
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
        set_ValueNoCheck ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Name 2.
    @param Name2 Additional Name */
    public void setName2 (String Name2)
    {
        set_ValueNoCheck ("Name2", Name2);
        
    }
    
    /** Get Name 2.
    @return Additional Name */
    public String getName2() 
    {
        return (String)get_Value("Name2");
        
    }
    
    /** EMail+Notice = B */
    public static final String NOTIFICATIONTYPE_EMailPlusNotice = X_Ref_AD_User_NotificationType.E_MAIL_PLUS_NOTICE.getValue();
    /** EMail = E */
    public static final String NOTIFICATIONTYPE_EMail = X_Ref_AD_User_NotificationType.E_MAIL.getValue();
    /** Notice = N */
    public static final String NOTIFICATIONTYPE_Notice = X_Ref_AD_User_NotificationType.NOTICE.getValue();
    /** None = X */
    public static final String NOTIFICATIONTYPE_None = X_Ref_AD_User_NotificationType.NONE.getValue();
    /** Set Notification Type.
    @param NotificationType Type of Notifications */
    public void setNotificationType (String NotificationType)
    {
        if (!X_Ref_AD_User_NotificationType.isValid(NotificationType))
        throw new IllegalArgumentException ("NotificationType Invalid value - " + NotificationType + " - Reference_ID=344 - B - E - N - X");
        set_ValueNoCheck ("NotificationType", NotificationType);
        
    }
    
    /** Get Notification Type.
    @return Type of Notifications */
    public String getNotificationType() 
    {
        return (String)get_Value("NotificationType");
        
    }
    
    /** Set Employees.
    @param NumberEmployees Number of employees */
    public void setNumberEmployees (int NumberEmployees)
    {
        set_ValueNoCheck ("NumberEmployees", Integer.valueOf(NumberEmployees));
        
    }
    
    /** Get Employees.
    @return Number of employees */
    public int getNumberEmployees() 
    {
        return get_ValueAsInt("NumberEmployees");
        
    }
    
    /** Set Order Reference.
    @param POReference Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner */
    public void setPOReference (String POReference)
    {
        set_ValueNoCheck ("POReference", POReference);
        
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
        if (PO_DiscountSchema_ID <= 0) set_ValueNoCheck ("PO_DiscountSchema_ID", null);
        else
        set_ValueNoCheck ("PO_DiscountSchema_ID", Integer.valueOf(PO_DiscountSchema_ID));
        
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
        if (PO_PaymentTerm_ID <= 0) set_ValueNoCheck ("PO_PaymentTerm_ID", null);
        else
        set_ValueNoCheck ("PO_PaymentTerm_ID", Integer.valueOf(PO_PaymentTerm_ID));
        
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
        if (PO_PriceList_ID <= 0) set_ValueNoCheck ("PO_PriceList_ID", null);
        else
        set_ValueNoCheck ("PO_PriceList_ID", Integer.valueOf(PO_PriceList_ID));
        
    }
    
    /** Get Purchase Pricelist.
    @return Price List used by this Business Partner */
    public int getPO_PriceList_ID() 
    {
        return get_ValueAsInt("PO_PriceList_ID");
        
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
        set_ValueNoCheck ("PaymentRule", PaymentRule);
        
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
        set_ValueNoCheck ("PaymentRulePO", PaymentRulePO);
        
    }
    
    /** Get Payment Rule.
    @return Purchase payment option */
    public String getPaymentRulePO() 
    {
        return (String)get_Value("PaymentRulePO");
        
    }
    
    /** Set Phone.
    @param Phone Identifies a telephone number */
    public void setPhone (String Phone)
    {
        set_ValueNoCheck ("Phone", Phone);
        
    }
    
    /** Get Phone.
    @return Identifies a telephone number */
    public String getPhone() 
    {
        return (String)get_Value("Phone");
        
    }
    
    /** Set 2nd Phone.
    @param Phone2 Identifies an alternate telephone number. */
    public void setPhone2 (String Phone2)
    {
        set_ValueNoCheck ("Phone2", Phone2);
        
    }
    
    /** Get 2nd Phone.
    @return Identifies an alternate telephone number. */
    public String getPhone2() 
    {
        return (String)get_Value("Phone2");
        
    }
    
    /** Set ZIP.
    @param Postal Postal code */
    public void setPostal (String Postal)
    {
        set_ValueNoCheck ("Postal", Postal);
        
    }
    
    /** Get ZIP.
    @return Postal code */
    public String getPostal() 
    {
        return (String)get_Value("Postal");
        
    }
    
    /** Set Potential Life Time Value.
    @param PotentialLifeTimeValue Total Revenue expected */
    public void setPotentialLifeTimeValue (java.math.BigDecimal PotentialLifeTimeValue)
    {
        set_ValueNoCheck ("PotentialLifeTimeValue", PotentialLifeTimeValue);
        
    }
    
    /** Get Potential Life Time Value.
    @return Total Revenue expected */
    public java.math.BigDecimal getPotentialLifeTimeValue() 
    {
        return get_ValueAsBigDecimal("PotentialLifeTimeValue");
        
    }
    
    /** Set Rating.
    @param Rating Classification or Importance */
    public void setRating (String Rating)
    {
        set_ValueNoCheck ("Rating", Rating);
        
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
        set_ValueNoCheck ("ReferenceNo", ReferenceNo);
        
    }
    
    /** Get Reference No.
    @return Your customer or vendor number at the Business Partner's site */
    public String getReferenceNo() 
    {
        return (String)get_Value("ReferenceNo");
        
    }
    
    /** Set Region Name.
    @param RegionName Name of the Region */
    public void setRegionName (String RegionName)
    {
        set_ValueNoCheck ("RegionName", RegionName);
        
    }
    
    /** Get Region Name.
    @return Name of the Region */
    public String getRegionName() 
    {
        return (String)get_Value("RegionName");
        
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
        if (!X_Ref_C_BPartner_SOCreditStatus.isValid(SOCreditStatus))
        throw new IllegalArgumentException ("SOCreditStatus Invalid value - " + SOCreditStatus + " - Reference_ID=289 - H - O - S - W - X");
        set_ValueNoCheck ("SOCreditStatus", SOCreditStatus);
        
    }
    
    /** Get Credit Status.
    @return Business Partner Credit Status */
    public String getSOCreditStatus() 
    {
        return (String)get_Value("SOCreditStatus");
        
    }
    
    /** Set Credit Available.
    @param SO_CreditAvailable Available Credit based on Credit Limit (not Total Open Balance) and Credit Used */
    public void setSO_CreditAvailable (java.math.BigDecimal SO_CreditAvailable)
    {
        set_ValueNoCheck ("SO_CreditAvailable", SO_CreditAvailable);
        
    }
    
    /** Get Credit Available.
    @return Available Credit based on Credit Limit (not Total Open Balance) and Credit Used */
    public java.math.BigDecimal getSO_CreditAvailable() 
    {
        return get_ValueAsBigDecimal("SO_CreditAvailable");
        
    }
    
    /** Set Credit Limit.
    @param SO_CreditLimit Total outstanding invoice amounts allowed */
    public void setSO_CreditLimit (java.math.BigDecimal SO_CreditLimit)
    {
        set_ValueNoCheck ("SO_CreditLimit", SO_CreditLimit);
        
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
        set_ValueNoCheck ("SO_Description", SO_Description);
        
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
        if (SalesRep_ID <= 0) set_ValueNoCheck ("SalesRep_ID", null);
        else
        set_ValueNoCheck ("SalesRep_ID", Integer.valueOf(SalesRep_ID));
        
    }
    
    /** Get Representative.
    @return Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public int getSalesRep_ID() 
    {
        return get_ValueAsInt("SalesRep_ID");
        
    }
    
    /** Set Sales Volume.
    @param SalesVolume Total Volume of Sales in Thousands of Base Currency */
    public void setSalesVolume (java.math.BigDecimal SalesVolume)
    {
        set_ValueNoCheck ("SalesVolume", SalesVolume);
        
    }
    
    /** Get Sales Volume.
    @return Total Volume of Sales in Thousands of Base Currency */
    public java.math.BigDecimal getSalesVolume() 
    {
        return get_ValueAsBigDecimal("SalesVolume");
        
    }
    
    /** Set Send EMail.
    @param SendEMail Enable sending Document EMail */
    public void setSendEMail (boolean SendEMail)
    {
        set_ValueNoCheck ("SendEMail", Boolean.valueOf(SendEMail));
        
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
        set_ValueNoCheck ("ShareOfCustomer", Integer.valueOf(ShareOfCustomer));
        
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
        set_ValueNoCheck ("ShelfLifeMinPct", Integer.valueOf(ShelfLifeMinPct));
        
    }
    
    /** Get Min Shelf Life %.
    @return Minimum Shelf Life in percent based on Product Instance Guarantee Date */
    public int getShelfLifeMinPct() 
    {
        return get_ValueAsInt("ShelfLifeMinPct");
        
    }
    
    /** Set Supervisor.
    @param Supervisor_ID Supervisor for this user/organization - used for escalation and approval */
    public void setSupervisor_ID (int Supervisor_ID)
    {
        if (Supervisor_ID <= 0) set_ValueNoCheck ("Supervisor_ID", null);
        else
        set_ValueNoCheck ("Supervisor_ID", Integer.valueOf(Supervisor_ID));
        
    }
    
    /** Get Supervisor.
    @return Supervisor for this user/organization - used for escalation and approval */
    public int getSupervisor_ID() 
    {
        return get_ValueAsInt("Supervisor_ID");
        
    }
    
    /** Set Tax ID.
    @param TaxID Tax Identification */
    public void setTaxID (String TaxID)
    {
        set_ValueNoCheck ("TaxID", TaxID);
        
    }
    
    /** Get Tax ID.
    @return Tax Identification */
    public String getTaxID() 
    {
        return (String)get_Value("TaxID");
        
    }
    
    /** Set Title.
    @param Title Title of the Contact */
    public void setTitle (String Title)
    {
        set_ValueNoCheck ("Title", Title);
        
    }
    
    /** Get Title.
    @return Title of the Contact */
    public String getTitle() 
    {
        return (String)get_Value("Title");
        
    }
    
    /** Set Open Balance.
    @param TotalOpenBalance Total Open Balance Amount in primary Accounting Currency */
    public void setTotalOpenBalance (java.math.BigDecimal TotalOpenBalance)
    {
        set_ValueNoCheck ("TotalOpenBalance", TotalOpenBalance);
        
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
        set_ValueNoCheck ("URL", URL);
        
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
        set_ValueNoCheck ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    
}
