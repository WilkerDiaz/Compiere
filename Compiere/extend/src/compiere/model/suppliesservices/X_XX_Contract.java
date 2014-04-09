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
/** Generated Model for XX_Contract
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_Contract extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_Contract_ID id
    @param trx transaction
    */
    public X_XX_Contract (Ctx ctx, int XX_Contract_ID, Trx trx)
    {
        super (ctx, XX_Contract_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_Contract_ID == 0)
        {
            setC_BPartner_ID (0);
            setDescription (null);
            setXX_ContractAmount (Env.ZERO);
            setXX_Contract_ID (0);
            setXX_DateFrom (new Timestamp(System.currentTimeMillis()));
            setXX_DateTo (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_Contract (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27630749006789L;
    /** Last Updated Timestamp 2012-09-25 21:11:30.0 */
    public static final long updatedMS = 1348623690000L;
    /** AD_Table_ID=1002454 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_Contract");
        
    }
    ;
    
    /** TableName=XX_Contract */
    public static final String Table_Name="XX_Contract";
    
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
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
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
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        if (Description == null) throw new IllegalArgumentException ("Description is mandatory.");
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
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
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_Value ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Invoice total amount = F */
    public static final String XX_APPLICABLEPERCENTAGE_InvoiceTotalAmount = X_Ref_XX_Percentage_LV.INVOICE_TOTAL_AMOUNT.getValue();
    /** Sales by Brand = M */
    public static final String XX_APPLICABLEPERCENTAGE_SalesByBrand = X_Ref_XX_Percentage_LV.SALES_BY_BRAND.getValue();
    /** Gross Sales = V */
    public static final String XX_APPLICABLEPERCENTAGE_GrossSales = X_Ref_XX_Percentage_LV.GROSS_SALES.getValue();
    /** Gross Sales per Store = W */
    public static final String XX_APPLICABLEPERCENTAGE_GrossSalesPerStore = X_Ref_XX_Percentage_LV.GROSS_SALES_PER_STORE.getValue();
    /** Set Applicable Percentage.
    @param XX_ApplicablePercentage Applicable Percentage */
    public void setXX_ApplicablePercentage (String XX_ApplicablePercentage)
    {
        if (!X_Ref_XX_Percentage_LV.isValid(XX_ApplicablePercentage))
        throw new IllegalArgumentException ("XX_ApplicablePercentage Invalid value - " + XX_ApplicablePercentage + " - Reference_ID=1001658 - F - M - V - W");
        set_Value ("XX_ApplicablePercentage", XX_ApplicablePercentage);
        
    }
    
    /** Get Applicable Percentage.
    @return Applicable Percentage */
    public String getXX_ApplicablePercentage() 
    {
        return (String)get_Value("XX_ApplicablePercentage");
        
    }
    
    /** Set Approve Contract.
    @param XX_ApproveContract Approve Contract */
    public void setXX_ApproveContract (String XX_ApproveContract)
    {
        set_Value ("XX_ApproveContract", XX_ApproveContract);
        
    }
    
    /** Get Approve Contract.
    @return Approve Contract */
    public String getXX_ApproveContract() 
    {
        return (String)get_Value("XX_ApproveContract");
        
    }
    
    /** Set Automatic Payment.
    @param XX_AutomaticPayment Automatic Payment */
    public void setXX_AutomaticPayment (boolean XX_AutomaticPayment)
    {
        set_Value ("XX_AutomaticPayment", Boolean.valueOf(XX_AutomaticPayment));
        
    }
    
    /** Get Automatic Payment.
    @return Automatic Payment */
    public boolean isXX_AutomaticPayment() 
    {
        return get_ValueAsBoolean("XX_AutomaticPayment");
        
    }
    
    /** Set Automatic Renovation.
    @param XX_AutomaticRenovation Automatic Renovation */
    public void setXX_AutomaticRenovation (boolean XX_AutomaticRenovation)
    {
        set_Value ("XX_AutomaticRenovation", Boolean.valueOf(XX_AutomaticRenovation));
        
    }
    
    /** Get Automatic Renovation.
    @return Automatic Renovation */
    public boolean isXX_AutomaticRenovation() 
    {
        return get_ValueAsBoolean("XX_AutomaticRenovation");
        
    }
    
    /** Set Amount.
    @param XX_ContractAmount Amount */
    public void setXX_ContractAmount (java.math.BigDecimal XX_ContractAmount)
    {
        if (XX_ContractAmount == null) throw new IllegalArgumentException ("XX_ContractAmount is mandatory.");
        set_Value ("XX_ContractAmount", XX_ContractAmount);
        
    }
    
    /** Get Amount.
    @return Amount */
    public java.math.BigDecimal getXX_ContractAmount() 
    {
        return get_ValueAsBigDecimal("XX_ContractAmount");
        
    }
    
    /** Set Date of Contract Approval.
    @param XX_ContractApproval Date of Contract Approval */
    public void setXX_ContractApproval (Timestamp XX_ContractApproval)
    {
        set_Value ("XX_ContractApproval", XX_ContractApproval);
        
    }
    
    /** Get Date of Contract Approval.
    @return Date of Contract Approval */
    public Timestamp getXX_ContractApproval() 
    {
        return (Timestamp)get_Value("XX_ContractApproval");
        
    }
    
    /** Set Contract ID.
    @param XX_Contract_ID Contract ID */
    public void setXX_Contract_ID (int XX_Contract_ID)
    {
        if (XX_Contract_ID < 1) throw new IllegalArgumentException ("XX_Contract_ID is mandatory.");
        set_ValueNoCheck ("XX_Contract_ID", Integer.valueOf(XX_Contract_ID));
        
    }
    
    /** Get Contract ID.
    @return Contract ID */
    public int getXX_Contract_ID() 
    {
        return get_ValueAsInt("XX_Contract_ID");
        
    }
    
    /** Set Contract Notification Date.
    @param XX_ContractNotifDate Contract Notification Date */
    public void setXX_ContractNotifDate (String XX_ContractNotifDate)
    {
        set_Value ("XX_ContractNotifDate", XX_ContractNotifDate);
        
    }
    
    /** Get Contract Notification Date.
    @return Contract Notification Date */
    public String getXX_ContractNotifDate() 
    {
        return (String)get_Value("XX_ContractNotifDate");
        
    }
    
    /** Amount = A */
    public static final String XX_CONTRACTPERCENTAGE_Amount = X_Ref_XX_Contract_LV.AMOUNT.getValue();
    /** Percentage = P */
    public static final String XX_CONTRACTPERCENTAGE_Percentage = X_Ref_XX_Contract_LV.PERCENTAGE.getValue();
    /** Set Contract Percentage.
    @param XX_ContractPercentage Contract Percentage */
    public void setXX_ContractPercentage (String XX_ContractPercentage)
    {
        if (!X_Ref_XX_Contract_LV.isValid(XX_ContractPercentage))
        throw new IllegalArgumentException ("XX_ContractPercentage Invalid value - " + XX_ContractPercentage + " - Reference_ID=1001652 - A - P");
        set_Value ("XX_ContractPercentage", XX_ContractPercentage);
        
    }
    
    /** Get Contract Percentage.
    @return Contract Percentage */
    public String getXX_ContractPercentage() 
    {
        return (String)get_Value("XX_ContractPercentage");
        
    }
    
    /** Set Contract Ready.
    @param XX_ContractReady Contract must have payments and distribution defined. Also verify if responsable, management ant payment type are defined */
    public void setXX_ContractReady (String XX_ContractReady)
    {
        set_Value ("XX_ContractReady", XX_ContractReady);
        
    }
    
    /** Get Contract Ready.
    @return Contract must have payments and distribution defined. Also verify if responsable, management ant payment type are defined */
    public String getXX_ContractReady() 
    {
        return (String)get_Value("XX_ContractReady");
        
    }
    
    /** Set Contract Type.
    @param XX_ContractType Contract Type (National/Import) */
    public void setXX_ContractType (String XX_ContractType)
    {
        set_Value ("XX_ContractType", XX_ContractType);
        
    }
    
    /** Get Contract Type.
    @return Contract Type (National/Import) */
    public String getXX_ContractType() 
    {
        return (String)get_Value("XX_ContractType");
        
    }
    
    /** Set Contract Value.
    @param XX_ContractValue Contract Value */
    public void setXX_ContractValue (java.math.BigDecimal XX_ContractValue)
    {
        set_Value ("XX_ContractValue", XX_ContractValue);
        
    }
    
    /** Get Contract Value.
    @return Contract Value */
    public java.math.BigDecimal getXX_ContractValue() 
    {
        return get_ValueAsBigDecimal("XX_ContractValue");
        
    }
    
    /** Set Date From.
    @param XX_DateFrom Date From */
    public void setXX_DateFrom (Timestamp XX_DateFrom)
    {
        if (XX_DateFrom == null) throw new IllegalArgumentException ("XX_DateFrom is mandatory.");
        set_Value ("XX_DateFrom", XX_DateFrom);
        
    }
    
    /** Get Date From.
    @return Date From */
    public Timestamp getXX_DateFrom() 
    {
        return (Timestamp)get_Value("XX_DateFrom");
        
    }
    
    /** Set Date of Signature.
    @param XX_DateOfSignature Date of Signature */
    public void setXX_DateOfSignature (Timestamp XX_DateOfSignature)
    {
        set_Value ("XX_DateOfSignature", XX_DateOfSignature);
        
    }
    
    /** Get Date of Signature.
    @return Date of Signature */
    public Timestamp getXX_DateOfSignature() 
    {
        return (Timestamp)get_Value("XX_DateOfSignature");
        
    }
    
    /** Set Date To.
    @param XX_DateTo Date To */
    public void setXX_DateTo (Timestamp XX_DateTo)
    {
        if (XX_DateTo == null) throw new IllegalArgumentException ("XX_DateTo is mandatory.");
        set_Value ("XX_DateTo", XX_DateTo);
        
    }
    
    /** Get Date To.
    @return Date To */
    public Timestamp getXX_DateTo() 
    {
        return (Timestamp)get_Value("XX_DateTo");
        
    }
    
    /** Set Deny Contract.
    @param XX_DenyContract Deny Contract */
    public void setXX_DenyContract (String XX_DenyContract)
    {
        set_Value ("XX_DenyContract", XX_DenyContract);
        
    }
    
    /** Get Deny Contract.
    @return Deny Contract */
    public String getXX_DenyContract() 
    {
        return (String)get_Value("XX_DenyContract");
        
    }
    
    /** AJUSTADA EN FACTURACIÓN = AF */
    public static final String XX_INVOICINGSTATUS_AJUSTADAENFACTURACIÓN = X_Ref_Invoicing_Status.AJUSTADAENFACTURACIÓN.getValue();
    /** APROBADA = AP */
    public static final String XX_INVOICINGSTATUS_APROBADA = X_Ref_Invoicing_Status.APROBADA.getValue();
    /** EN PROCESO = EP */
    public static final String XX_INVOICINGSTATUS_ENPROCESO = X_Ref_Invoicing_Status.ENPROCESO.getValue();
    /** PENDIENTE = PE */
    public static final String XX_INVOICINGSTATUS_PENDIENTE = X_Ref_Invoicing_Status.PENDIENTE.getValue();
    /** Set Invoicing Status.
    @param XX_InvoicingStatus Invoicing Status */
    public void setXX_InvoicingStatus (String XX_InvoicingStatus)
    {
        if (!X_Ref_Invoicing_Status.isValid(XX_InvoicingStatus))
        throw new IllegalArgumentException ("XX_InvoicingStatus Invalid value - " + XX_InvoicingStatus + " - Reference_ID=1000212 - AF - AP - EP - PE");
        set_Value ("XX_InvoicingStatus", XX_InvoicingStatus);
        
    }
    
    /** Get Invoicing Status.
    @return Invoicing Status */
    public String getXX_InvoicingStatus() 
    {
        return (String)get_Value("XX_InvoicingStatus");
        
    }
    
    /** Set Is Contract Ready.
    @param XX_IsContractReady Is Contract Ready */
    public void setXX_IsContractReady (boolean XX_IsContractReady)
    {
        set_Value ("XX_IsContractReady", Boolean.valueOf(XX_IsContractReady));
        
    }
    
    /** Get Is Contract Ready.
    @return Is Contract Ready */
    public boolean isXX_IsContractReady() 
    {
        return get_ValueAsBoolean("XX_IsContractReady");
        
    }
    
    /** Set Amount Distributed.
    @param XX_IsDistrbAmount Amount Distributed */
    public void setXX_IsDistrbAmount (boolean XX_IsDistrbAmount)
    {
        set_Value ("XX_IsDistrbAmount", Boolean.valueOf(XX_IsDistrbAmount));
        
    }
    
    /** Get Amount Distributed.
    @return Amount Distributed */
    public boolean isXX_IsDistrbAmount() 
    {
        return get_ValueAsBoolean("XX_IsDistrbAmount");
        
    }
    
    /** Set Percentage Distributed.
    @param XX_IsDistrbPercentage Percentage Distributed */
    public void setXX_IsDistrbPercentage (boolean XX_IsDistrbPercentage)
    {
        set_Value ("XX_IsDistrbPercentage", Boolean.valueOf(XX_IsDistrbPercentage));
        
    }
    
    /** Get Percentage Distributed.
    @return Percentage Distributed */
    public boolean isXX_IsDistrbPercentage() 
    {
        return get_ValueAsBoolean("XX_IsDistrbPercentage");
        
    }
    
    /** Set Lease.
    @param XX_Lease Lease */
    public void setXX_Lease (boolean XX_Lease)
    {
        set_Value ("XX_Lease", Boolean.valueOf(XX_Lease));
        
    }
    
    /** Get Lease.
    @return Lease */
    public boolean isXX_Lease() 
    {
        return get_ValueAsBoolean("XX_Lease");
        
    }
    
    /** Set Management.
    @param XX_Management_ID Management */
    public void setXX_Management_ID (int XX_Management_ID)
    {
        if (XX_Management_ID <= 0) set_Value ("XX_Management_ID", null);
        else
        set_Value ("XX_Management_ID", Integer.valueOf(XX_Management_ID));
        
    }
    
    /** Get Management.
    @return Management */
    public int getXX_Management_ID() 
    {
        return get_ValueAsInt("XX_Management_ID");
        
    }
    
    /** Set Observations.
    @param XX_Observations Observations */
    public void setXX_Observations (String XX_Observations)
    {
        set_Value ("XX_Observations", XX_Observations);
        
    }
    
    /** Get Observations.
    @return Observations */
    public String getXX_Observations() 
    {
        return (String)get_Value("XX_Observations");
        
    }
    
    /** Fixed and Variable = A */
    public static final String XX_PAYMENTTYPEDETAIL_FixedAndVariable = X_Ref_XX_PaymentType_LV.FIXED_AND_VARIABLE.getValue();
    /** Fixed = F */
    public static final String XX_PAYMENTTYPEDETAIL_Fixed = X_Ref_XX_PaymentType_LV.FIXED.getValue();
    /** Guaranteed Fixed Variable = G */
    public static final String XX_PAYMENTTYPEDETAIL_GuaranteedFixedVariable = X_Ref_XX_PaymentType_LV.GUARANTEED_FIXED_VARIABLE.getValue();
    /** Variable = V */
    public static final String XX_PAYMENTTYPEDETAIL_Variable = X_Ref_XX_PaymentType_LV.VARIABLE.getValue();
    /** Set Payment Type.
    @param XX_PaymentTypeDetail Payment Type */
    public void setXX_PaymentTypeDetail (String XX_PaymentTypeDetail)
    {
        if (!X_Ref_XX_PaymentType_LV.isValid(XX_PaymentTypeDetail))
        throw new IllegalArgumentException ("XX_PaymentTypeDetail Invalid value - " + XX_PaymentTypeDetail + " - Reference_ID=1001657 - A - F - G - V");
        set_Value ("XX_PaymentTypeDetail", XX_PaymentTypeDetail);
        
    }
    
    /** Get Payment Type.
    @return Payment Type */
    public String getXX_PaymentTypeDetail() 
    {
        return (String)get_Value("XX_PaymentTypeDetail");
        
    }
    
    /** Set Expenses Product.
    @param XX_PExpenses_ID Expenses Product */
    public void setXX_PExpenses_ID (int XX_PExpenses_ID)
    {
        if (XX_PExpenses_ID <= 0) set_Value ("XX_PExpenses_ID", null);
        else
        set_Value ("XX_PExpenses_ID", Integer.valueOf(XX_PExpenses_ID));
        
    }
    
    /** Get Expenses Product.
    @return Expenses Product */
    public int getXX_PExpenses_ID() 
    {
        return get_ValueAsInt("XX_PExpenses_ID");
        
    }
    
    /** Set Receivable.
    @param XX_Receivable Receivable */
    public void setXX_Receivable (boolean XX_Receivable)
    {
        set_Value ("XX_Receivable", Boolean.valueOf(XX_Receivable));
        
    }
    
    /** Get Receivable.
    @return Receivable */
    public boolean isXX_Receivable() 
    {
        return get_ValueAsBoolean("XX_Receivable");
        
    }
    
    /** Set Renewal Notification Date.
    @param XX_RenewalNotificationDate Renewal Notification Date. Indicate how many days notice is required for the system to dispose of the calendar date (Uploaded by the legal section) */
    public void setXX_RenewalNotificationDate (Timestamp XX_RenewalNotificationDate)
    {
        set_Value ("XX_RenewalNotificationDate", XX_RenewalNotificationDate);
        
    }
    
    /** Get Renewal Notification Date.
    @return Renewal Notification Date. Indicate how many days notice is required for the system to dispose of the calendar date (Uploaded by the legal section) */
    public Timestamp getXX_RenewalNotificationDate() 
    {
        return (Timestamp)get_Value("XX_RenewalNotificationDate");
        
    }
    
    /** Set Renewal Notification Days.
    @param XX_RenewalNotificationDays Renewal Notification Days */
    public void setXX_RenewalNotificationDays (int XX_RenewalNotificationDays)
    {
        set_Value ("XX_RenewalNotificationDays", Integer.valueOf(XX_RenewalNotificationDays));
        
    }
    
    /** Get Renewal Notification Days.
    @return Renewal Notification Days */
    public int getXX_RenewalNotificationDays() 
    {
        return get_ValueAsInt("XX_RenewalNotificationDays");
        
    }
    
    /** Set Responsable (Role).
    @param XX_Responsable_ID Responsable (Role) */
    public void setXX_Responsable_ID (int XX_Responsable_ID)
    {
        if (XX_Responsable_ID <= 0) set_Value ("XX_Responsable_ID", null);
        else
        set_Value ("XX_Responsable_ID", Integer.valueOf(XX_Responsable_ID));
        
    }
    
    /** Get Responsable (Role).
    @return Responsable (Role) */
    public int getXX_Responsable_ID() 
    {
        return get_ValueAsInt("XX_Responsable_ID");
        
    }
    
    /** Early Termination = A */
    public static final String XX_STATUS_EarlyTermination = X_Ref_XX_Status_LV.EARLY_TERMINATION.getValue();
    /** Approved by Legal = C */
    public static final String XX_STATUS_ApprovedByLegal = X_Ref_XX_Status_LV.APPROVED_BY_LEGAL.getValue();
    /** Rejected = E */
    public static final String XX_STATUS_Rejected = X_Ref_XX_Status_LV.REJECTED.getValue();
    /** Finished = F */
    public static final String XX_STATUS_Finished = X_Ref_XX_Status_LV.FINISHED.getValue();
    /** Review = R */
    public static final String XX_STATUS_Review = X_Ref_XX_Status_LV.REVIEW.getValue();
    /** Unsigned Approved = V */
    public static final String XX_STATUS_UnsignedApproved = X_Ref_XX_Status_LV.UNSIGNED_APPROVED.getValue();
    /** Set Status.
    @param XX_Status Status */
    public void setXX_Status (String XX_Status)
    {
        if (!X_Ref_XX_Status_LV.isValid(XX_Status))
        throw new IllegalArgumentException ("XX_Status Invalid value - " + XX_Status + " - Reference_ID=1001663 - A - C - E - F - R - V");
        set_Value ("XX_Status", XX_Status);
        
    }
    
    /** Get Status.
    @return Status */
    public String getXX_Status() 
    {
        return (String)get_Value("XX_Status");
        
    }
    
    /** Set Early Termination.
    @param XX_TermContract Early Termination */
    public void setXX_TermContract (String XX_TermContract)
    {
        set_Value ("XX_TermContract", XX_TermContract);
        
    }
    
    /** Get Early Termination.
    @return Early Termination */
    public String getXX_TermContract() 
    {
        return (String)get_Value("XX_TermContract");
        
    }
    
    /** Set Termination Date.
    @param XX_TerminationDate Termination Date */
    public void setXX_TerminationDate (Timestamp XX_TerminationDate)
    {
        set_Value ("XX_TerminationDate", XX_TerminationDate);
        
    }
    
    /** Get Termination Date.
    @return Termination Date */
    public Timestamp getXX_TerminationDate() 
    {
        return (Timestamp)get_Value("XX_TerminationDate");
        
    }
    
    /** Set Termination User.
    @param XX_TerminationUser Termination User */
    public void setXX_TerminationUser (String XX_TerminationUser)
    {
        set_Value ("XX_TerminationUser", XX_TerminationUser);
        
    }
    
    /** Get Termination User.
    @return Termination User */
    public String getXX_TerminationUser() 
    {
        return (String)get_Value("XX_TerminationUser");
        
    }
    
    /** Set User Contract Approval.
    @param XX_UserContractApproval_ID User Contract Approval */
    public void setXX_UserContractApproval_ID (int XX_UserContractApproval_ID)
    {
        if (XX_UserContractApproval_ID <= 0) set_Value ("XX_UserContractApproval_ID", null);
        else
        set_Value ("XX_UserContractApproval_ID", Integer.valueOf(XX_UserContractApproval_ID));
        
    }
    
    /** Get User Contract Approval.
    @return User Contract Approval */
    public int getXX_UserContractApproval_ID() 
    {
        return get_ValueAsInt("XX_UserContractApproval_ID");
        
    }
    /** Exclusive = E */
    public static final String XX_TYPEOFCONTRACT_Exclusive = X_Ref_XX_TypeOfContract.EXCLUSIVE.getValue();
    /** Normal = N */
    public static final String XX_TYPEOFCONTRACT_Normal = X_Ref_XX_TypeOfContract.NORMAL.getValue();
    /** Set Type of Contract.
    @param XX_TypeOfContract Type of Contract (normal or exclusive) */
    public void setXX_TypeOfContract (String XX_TypeOfContract)
    {
        if (!X_Ref_XX_TypeOfContract.isValid(XX_TypeOfContract))
        throw new IllegalArgumentException ("XX_TypeOfContract Invalid value - " + XX_TypeOfContract + " - Reference_ID=1002449 - E - N");
        set_Value ("XX_TypeOfContract", XX_TypeOfContract);
        
    }
    
    /** Get Type of Contract.
    @return Type of Contract (normal or exclusive) */
    public String getXX_TypeOfContract() 
    {
        return (String)get_Value("XX_TypeOfContract");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        if (XX_VMR_Brand_ID <= 0) set_Value ("XX_VMR_Brand_ID", null);
        else
        set_Value ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
    }
    
    
}
