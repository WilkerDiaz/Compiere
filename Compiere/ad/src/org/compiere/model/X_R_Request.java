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
/** Generated Model for R_Request
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_R_Request.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_R_Request extends PO
{
    /** Standard Constructor
    @param ctx context
    @param R_Request_ID id
    @param trx transaction
    */
    public X_R_Request (Ctx ctx, int R_Request_ID, Trx trx)
    {
        super (ctx, R_Request_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (R_Request_ID == 0)
        {
            setConfidentialType (null);
            setConfidentialTypeEntry (null);
            setDocumentNo (null);
            setDueType (null);	// 5
            setIsEscalated (false);
            setIsInvoiced (false);
            setIsSelfService (false);	// N
            setPriority (null);	// 5
            setProcessed (false);	// N
            setR_RequestType_ID (0);
            setR_Request_ID (0);
            setRequestAmt (Env.ZERO);
            setSummary (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_R_Request (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27509346968789L;
    /** Last Updated Timestamp 2008-11-20 14:54:12.0 */
    public static final long updatedMS = 1227221652000L;
    /** AD_Table_ID=417 */
    public static final int Table_ID=417;
    
    /** TableName=R_Request */
    public static final String Table_Name="R_Request";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Role.
    @param AD_Role_ID Responsibility Role */
    public void setAD_Role_ID (int AD_Role_ID)
    {
        if (AD_Role_ID <= 0) set_Value ("AD_Role_ID", null);
        else
        set_Value ("AD_Role_ID", Integer.valueOf(AD_Role_ID));
        
    }
    
    /** Get Role.
    @return Responsibility Role */
    public int getAD_Role_ID() 
    {
        return get_ValueAsInt("AD_Role_ID");
        
    }
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID <= 0) set_ValueNoCheck ("AD_Table_ID", null);
        else
        set_ValueNoCheck ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Asset.
    @param A_Asset_ID Asset used internally or by customers */
    public void setA_Asset_ID (int A_Asset_ID)
    {
        if (A_Asset_ID <= 0) set_Value ("A_Asset_ID", null);
        else
        set_Value ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
        
    }
    
    /** Get Asset.
    @return Asset used internally or by customers */
    public int getA_Asset_ID() 
    {
        return get_ValueAsInt("A_Asset_ID");
        
    }
    
    /** Set Activity.
    @param C_Activity_ID Business Activity */
    public void setC_Activity_ID (int C_Activity_ID)
    {
        if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
        else
        set_Value ("C_Activity_ID", Integer.valueOf(C_Activity_ID));
        
    }
    
    /** Get Activity.
    @return Business Activity */
    public int getC_Activity_ID() 
    {
        return get_ValueAsInt("C_Activity_ID");
        
    }
    
    /** Set Agent.
    @param C_BPartnerSR_ID Business Partner (Agent or Sales Rep) */
    public void setC_BPartnerSR_ID (int C_BPartnerSR_ID)
    {
        if (C_BPartnerSR_ID <= 0) set_Value ("C_BPartnerSR_ID", null);
        else
        set_Value ("C_BPartnerSR_ID", Integer.valueOf(C_BPartnerSR_ID));
        
    }
    
    /** Get Agent.
    @return Business Partner (Agent or Sales Rep) */
    public int getC_BPartnerSR_ID() 
    {
        return get_ValueAsInt("C_BPartnerSR_ID");
        
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
    
    /** Set Campaign.
    @param C_Campaign_ID Marketing Campaign */
    public void setC_Campaign_ID (int C_Campaign_ID)
    {
        if (C_Campaign_ID <= 0) set_Value ("C_Campaign_ID", null);
        else
        set_Value ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
    }
    
    /** Get Campaign.
    @return Marketing Campaign */
    public int getC_Campaign_ID() 
    {
        return get_ValueAsInt("C_Campaign_ID");
        
    }
    
    /** Set Request Invoice.
    @param C_InvoiceRequest_ID The generated invoice for this request */
    public void setC_InvoiceRequest_ID (int C_InvoiceRequest_ID)
    {
        if (C_InvoiceRequest_ID <= 0) set_ValueNoCheck ("C_InvoiceRequest_ID", null);
        else
        set_ValueNoCheck ("C_InvoiceRequest_ID", Integer.valueOf(C_InvoiceRequest_ID));
        
    }
    
    /** Get Request Invoice.
    @return The generated invoice for this request */
    public int getC_InvoiceRequest_ID() 
    {
        return get_ValueAsInt("C_InvoiceRequest_ID");
        
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
    
    /** Set Lead.
    @param C_Lead_ID Business Lead */
    public void setC_Lead_ID (int C_Lead_ID)
    {
        if (C_Lead_ID <= 0) set_Value ("C_Lead_ID", null);
        else
        set_Value ("C_Lead_ID", Integer.valueOf(C_Lead_ID));
        
    }
    
    /** Get Lead.
    @return Business Lead */
    public int getC_Lead_ID() 
    {
        return get_ValueAsInt("C_Lead_ID");
        
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
    
    /** Set Payment.
    @param C_Payment_ID Payment identifier */
    public void setC_Payment_ID (int C_Payment_ID)
    {
        if (C_Payment_ID <= 0) set_Value ("C_Payment_ID", null);
        else
        set_Value ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
    }
    
    /** Get Payment.
    @return Payment identifier */
    public int getC_Payment_ID() 
    {
        return get_ValueAsInt("C_Payment_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
        else
        set_Value ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
    }
    
    /** Set Sales Region.
    @param C_SalesRegion_ID Sales coverage region */
    public void setC_SalesRegion_ID (int C_SalesRegion_ID)
    {
        if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
        else
        set_Value ("C_SalesRegion_ID", Integer.valueOf(C_SalesRegion_ID));
        
    }
    
    /** Get Sales Region.
    @return Sales coverage region */
    public int getC_SalesRegion_ID() 
    {
        return get_ValueAsInt("C_SalesRegion_ID");
        
    }
    
    /** Set Close Date.
    @param CloseDate Close Date */
    public void setCloseDate (Timestamp CloseDate)
    {
        set_Value ("CloseDate", CloseDate);
        
    }
    
    /** Get Close Date.
    @return Close Date */
    public Timestamp getCloseDate() 
    {
        return (Timestamp)get_Value("CloseDate");
        
    }
    
    /** Public Information = A */
    public static final String CONFIDENTIALTYPE_PublicInformation = X_Ref_R_Request_Confidential.PUBLIC_INFORMATION.getValue();
    /** Partner Confidential = C */
    public static final String CONFIDENTIALTYPE_PartnerConfidential = X_Ref_R_Request_Confidential.PARTNER_CONFIDENTIAL.getValue();
    /** Internal = I */
    public static final String CONFIDENTIALTYPE_Internal = X_Ref_R_Request_Confidential.INTERNAL.getValue();
    /** Private Information = P */
    public static final String CONFIDENTIALTYPE_PrivateInformation = X_Ref_R_Request_Confidential.PRIVATE_INFORMATION.getValue();
    /** Set Confidentiality.
    @param ConfidentialType Type of Confidentiality */
    public void setConfidentialType (String ConfidentialType)
    {
        if (ConfidentialType == null) throw new IllegalArgumentException ("ConfidentialType is mandatory");
        if (!X_Ref_R_Request_Confidential.isValid(ConfidentialType))
        throw new IllegalArgumentException ("ConfidentialType Invalid value - " + ConfidentialType + " - Reference_ID=340 - A - C - I - P");
        set_Value ("ConfidentialType", ConfidentialType);
        
    }
    
    /** Get Confidentiality.
    @return Type of Confidentiality */
    public String getConfidentialType() 
    {
        return (String)get_Value("ConfidentialType");
        
    }
    
    /** Public Information = A */
    public static final String CONFIDENTIALTYPEENTRY_PublicInformation = X_Ref_R_Request_Confidential.PUBLIC_INFORMATION.getValue();
    /** Partner Confidential = C */
    public static final String CONFIDENTIALTYPEENTRY_PartnerConfidential = X_Ref_R_Request_Confidential.PARTNER_CONFIDENTIAL.getValue();
    /** Internal = I */
    public static final String CONFIDENTIALTYPEENTRY_Internal = X_Ref_R_Request_Confidential.INTERNAL.getValue();
    /** Private Information = P */
    public static final String CONFIDENTIALTYPEENTRY_PrivateInformation = X_Ref_R_Request_Confidential.PRIVATE_INFORMATION.getValue();
    /** Set Entry Access Level.
    @param ConfidentialTypeEntry Confidentiality of the individual entry */
    public void setConfidentialTypeEntry (String ConfidentialTypeEntry)
    {
        if (ConfidentialTypeEntry == null) throw new IllegalArgumentException ("ConfidentialTypeEntry is mandatory");
        if (!X_Ref_R_Request_Confidential.isValid(ConfidentialTypeEntry))
        throw new IllegalArgumentException ("ConfidentialTypeEntry Invalid value - " + ConfidentialTypeEntry + " - Reference_ID=340 - A - C - I - P");
        set_Value ("ConfidentialTypeEntry", ConfidentialTypeEntry);
        
    }
    
    /** Get Entry Access Level.
    @return Confidentiality of the individual entry */
    public String getConfidentialTypeEntry() 
    {
        return (String)get_Value("ConfidentialTypeEntry");
        
    }
    
    /** Set Complete Plan.
    @param DateCompletePlan Planned Completion Date */
    public void setDateCompletePlan (Timestamp DateCompletePlan)
    {
        set_Value ("DateCompletePlan", DateCompletePlan);
        
    }
    
    /** Get Complete Plan.
    @return Planned Completion Date */
    public Timestamp getDateCompletePlan() 
    {
        return (Timestamp)get_Value("DateCompletePlan");
        
    }
    
    /** Set Date last action.
    @param DateLastAction Date this request was last acted on */
    public void setDateLastAction (Timestamp DateLastAction)
    {
        set_ValueNoCheck ("DateLastAction", DateLastAction);
        
    }
    
    /** Get Date last action.
    @return Date this request was last acted on */
    public Timestamp getDateLastAction() 
    {
        return (Timestamp)get_Value("DateLastAction");
        
    }
    
    /** Set Last Alert.
    @param DateLastAlert Date when last alert were sent */
    public void setDateLastAlert (Timestamp DateLastAlert)
    {
        set_Value ("DateLastAlert", DateLastAlert);
        
    }
    
    /** Get Last Alert.
    @return Date when last alert were sent */
    public Timestamp getDateLastAlert() 
    {
        return (Timestamp)get_Value("DateLastAlert");
        
    }
    
    /** Set Date next action.
    @param DateNextAction Date that this request should be acted on */
    public void setDateNextAction (Timestamp DateNextAction)
    {
        set_Value ("DateNextAction", DateNextAction);
        
    }
    
    /** Get Date next action.
    @return Date that this request should be acted on */
    public Timestamp getDateNextAction() 
    {
        return (Timestamp)get_Value("DateNextAction");
        
    }
    
    /** Set Start Plan.
    @param DateStartPlan Planned Start Date */
    public void setDateStartPlan (Timestamp DateStartPlan)
    {
        set_Value ("DateStartPlan", DateStartPlan);
        
    }
    
    /** Get Start Plan.
    @return Planned Start Date */
    public Timestamp getDateStartPlan() 
    {
        return (Timestamp)get_Value("DateStartPlan");
        
    }
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory.");
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
        
    }
    
    /** Overdue = 3 */
    public static final String DUETYPE_Overdue = X_Ref_R_Request_Due_Type.OVERDUE.getValue();
    /** Due = 5 */
    public static final String DUETYPE_Due = X_Ref_R_Request_Due_Type.DUE.getValue();
    /** Scheduled = 7 */
    public static final String DUETYPE_Scheduled = X_Ref_R_Request_Due_Type.SCHEDULED.getValue();
    /** Set Aging Status.
    @param DueType Status of the next action for this Request */
    public void setDueType (String DueType)
    {
        if (DueType == null) throw new IllegalArgumentException ("DueType is mandatory");
        if (!X_Ref_R_Request_Due_Type.isValid(DueType))
        throw new IllegalArgumentException ("DueType Invalid value - " + DueType + " - Reference_ID=222 - 3 - 5 - 7");
        set_Value ("DueType", DueType);
        
    }
    
    /** Get Aging Status.
    @return Status of the next action for this Request */
    public String getDueType() 
    {
        return (String)get_Value("DueType");
        
    }
    
    /** Set End Time.
    @param EndTime End of the time span */
    public void setEndTime (Timestamp EndTime)
    {
        set_Value ("EndTime", EndTime);
        
    }
    
    /** Get End Time.
    @return End of the time span */
    public Timestamp getEndTime() 
    {
        return (Timestamp)get_Value("EndTime");
        
    }
    
    /** Set Escalated.
    @param IsEscalated This request has been escalated */
    public void setIsEscalated (boolean IsEscalated)
    {
        set_Value ("IsEscalated", Boolean.valueOf(IsEscalated));
        
    }
    
    /** Get Escalated.
    @return This request has been escalated */
    public boolean isEscalated() 
    {
        return get_ValueAsBoolean("IsEscalated");
        
    }
    
    /** Set Invoiced.
    @param IsInvoiced Is this invoiced? */
    public void setIsInvoiced (boolean IsInvoiced)
    {
        set_Value ("IsInvoiced", Boolean.valueOf(IsInvoiced));
        
    }
    
    /** Get Invoiced.
    @return Is this invoiced? */
    public boolean isInvoiced() 
    {
        return get_ValueAsBoolean("IsInvoiced");
        
    }
    
    /** Set Self-Service.
    @param IsSelfService This is a Self-Service entry or this entry can be changed via Self-Service */
    public void setIsSelfService (boolean IsSelfService)
    {
        set_ValueNoCheck ("IsSelfService", Boolean.valueOf(IsSelfService));
        
    }
    
    /** Get Self-Service.
    @return This is a Self-Service entry or this entry can be changed via Self-Service */
    public boolean isSelfService() 
    {
        return get_ValueAsBoolean("IsSelfService");
        
    }
    
    /** Set Last Result.
    @param LastResult Result of last contact */
    public void setLastResult (String LastResult)
    {
        set_Value ("LastResult", LastResult);
        
    }
    
    /** Get Last Result.
    @return Result of last contact */
    public String getLastResult() 
    {
        return (String)get_Value("LastResult");
        
    }
    
    /** Set Change Request.
    @param M_ChangeRequest_ID BOM (Engineering) Change Request */
    public void setM_ChangeRequest_ID (int M_ChangeRequest_ID)
    {
        if (M_ChangeRequest_ID <= 0) set_Value ("M_ChangeRequest_ID", null);
        else
        set_Value ("M_ChangeRequest_ID", Integer.valueOf(M_ChangeRequest_ID));
        
    }
    
    /** Get Change Request.
    @return BOM (Engineering) Change Request */
    public int getM_ChangeRequest_ID() 
    {
        return get_ValueAsInt("M_ChangeRequest_ID");
        
    }
    
    /** Set Fixed in.
    @param M_FixChangeNotice_ID Fixed in Change Notice */
    public void setM_FixChangeNotice_ID (int M_FixChangeNotice_ID)
    {
        if (M_FixChangeNotice_ID <= 0) set_Value ("M_FixChangeNotice_ID", null);
        else
        set_Value ("M_FixChangeNotice_ID", Integer.valueOf(M_FixChangeNotice_ID));
        
    }
    
    /** Get Fixed in.
    @return Fixed in Change Notice */
    public int getM_FixChangeNotice_ID() 
    {
        return get_ValueAsInt("M_FixChangeNotice_ID");
        
    }
    
    /** Set Shipment/Receipt.
    @param M_InOut_ID Material Shipment Document */
    public void setM_InOut_ID (int M_InOut_ID)
    {
        if (M_InOut_ID <= 0) set_Value ("M_InOut_ID", null);
        else
        set_Value ("M_InOut_ID", Integer.valueOf(M_InOut_ID));
        
    }
    
    /** Get Shipment/Receipt.
    @return Material Shipment Document */
    public int getM_InOut_ID() 
    {
        return get_ValueAsInt("M_InOut_ID");
        
    }
    
    /** Set Product Used.
    @param M_ProductSpent_ID Product/Resource/Service used in Request */
    public void setM_ProductSpent_ID (int M_ProductSpent_ID)
    {
        if (M_ProductSpent_ID <= 0) set_Value ("M_ProductSpent_ID", null);
        else
        set_Value ("M_ProductSpent_ID", Integer.valueOf(M_ProductSpent_ID));
        
    }
    
    /** Get Product Used.
    @return Product/Resource/Service used in Request */
    public int getM_ProductSpent_ID() 
    {
        return get_ValueAsInt("M_ProductSpent_ID");
        
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
    
    /** Follow up = F */
    public static final String NEXTACTION_FollowUp = X_Ref_R_Request_Next_Action.FOLLOW_UP.getValue();
    /** None = N */
    public static final String NEXTACTION_None = X_Ref_R_Request_Next_Action.NONE.getValue();
    /** Set Next action.
    @param NextAction Next Action to be taken */
    public void setNextAction (String NextAction)
    {
        if (!X_Ref_R_Request_Next_Action.isValid(NextAction))
        throw new IllegalArgumentException ("NextAction Invalid value - " + NextAction + " - Reference_ID=219 - F - N");
        set_Value ("NextAction", NextAction);
        
    }
    
    /** Get Next action.
    @return Next Action to be taken */
    public String getNextAction() 
    {
        return (String)get_Value("NextAction");
        
    }
    
    /** Urgent = 1 */
    public static final String PRIORITY_Urgent = X_Ref__PriorityRule.URGENT.getValue();
    /** High = 3 */
    public static final String PRIORITY_High = X_Ref__PriorityRule.HIGH.getValue();
    /** Medium = 5 */
    public static final String PRIORITY_Medium = X_Ref__PriorityRule.MEDIUM.getValue();
    /** Low = 7 */
    public static final String PRIORITY_Low = X_Ref__PriorityRule.LOW.getValue();
    /** Minor = 9 */
    public static final String PRIORITY_Minor = X_Ref__PriorityRule.MINOR.getValue();
    /** Set Priority.
    @param Priority Indicates if this request is of a high, medium or low priority. */
    public void setPriority (String Priority)
    {
        if (Priority == null) throw new IllegalArgumentException ("Priority is mandatory");
        if (!X_Ref__PriorityRule.isValid(Priority))
        throw new IllegalArgumentException ("Priority Invalid value - " + Priority + " - Reference_ID=154 - 1 - 3 - 5 - 7 - 9");
        set_Value ("Priority", Priority);
        
    }
    
    /** Get Priority.
    @return Indicates if this request is of a high, medium or low priority. */
    public String getPriority() 
    {
        return (String)get_Value("Priority");
        
    }
    
    /** Urgent = 1 */
    public static final String PRIORITYUSER_Urgent = X_Ref__PriorityRule.URGENT.getValue();
    /** High = 3 */
    public static final String PRIORITYUSER_High = X_Ref__PriorityRule.HIGH.getValue();
    /** Medium = 5 */
    public static final String PRIORITYUSER_Medium = X_Ref__PriorityRule.MEDIUM.getValue();
    /** Low = 7 */
    public static final String PRIORITYUSER_Low = X_Ref__PriorityRule.LOW.getValue();
    /** Minor = 9 */
    public static final String PRIORITYUSER_Minor = X_Ref__PriorityRule.MINOR.getValue();
    /** Set User Priority.
    @param PriorityUser Priority of the issue for the User */
    public void setPriorityUser (String PriorityUser)
    {
        if (!X_Ref__PriorityRule.isValid(PriorityUser))
        throw new IllegalArgumentException ("PriorityUser Invalid value - " + PriorityUser + " - Reference_ID=154 - 1 - 3 - 5 - 7 - 9");
        set_Value ("PriorityUser", PriorityUser);
        
    }
    
    /** Get User Priority.
    @return Priority of the issue for the User */
    public String getPriorityUser() 
    {
        return (String)get_Value("PriorityUser");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Quantity Invoiced.
    @param QtyInvoiced Invoiced Quantity */
    public void setQtyInvoiced (java.math.BigDecimal QtyInvoiced)
    {
        set_Value ("QtyInvoiced", QtyInvoiced);
        
    }
    
    /** Get Quantity Invoiced.
    @return Invoiced Quantity */
    public java.math.BigDecimal getQtyInvoiced() 
    {
        return get_ValueAsBigDecimal("QtyInvoiced");
        
    }
    
    /** Set Quantity Plan.
    @param QtyPlan Planned Quantity */
    public void setQtyPlan (java.math.BigDecimal QtyPlan)
    {
        set_Value ("QtyPlan", QtyPlan);
        
    }
    
    /** Get Quantity Plan.
    @return Planned Quantity */
    public java.math.BigDecimal getQtyPlan() 
    {
        return get_ValueAsBigDecimal("QtyPlan");
        
    }
    
    /** Set Quantity Used.
    @param QtySpent Quantity used for this event */
    public void setQtySpent (java.math.BigDecimal QtySpent)
    {
        set_Value ("QtySpent", QtySpent);
        
    }
    
    /** Get Quantity Used.
    @return Quantity used for this event */
    public java.math.BigDecimal getQtySpent() 
    {
        return get_ValueAsBigDecimal("QtySpent");
        
    }
    
    /** Set Category.
    @param R_Category_ID Request Category */
    public void setR_Category_ID (int R_Category_ID)
    {
        if (R_Category_ID <= 0) set_Value ("R_Category_ID", null);
        else
        set_Value ("R_Category_ID", Integer.valueOf(R_Category_ID));
        
    }
    
    /** Get Category.
    @return Request Category */
    public int getR_Category_ID() 
    {
        return get_ValueAsInt("R_Category_ID");
        
    }
    
    /** Set Group.
    @param R_Group_ID Request Group */
    public void setR_Group_ID (int R_Group_ID)
    {
        if (R_Group_ID <= 0) set_Value ("R_Group_ID", null);
        else
        set_Value ("R_Group_ID", Integer.valueOf(R_Group_ID));
        
    }
    
    /** Get Group.
    @return Request Group */
    public int getR_Group_ID() 
    {
        return get_ValueAsInt("R_Group_ID");
        
    }
    
    /** Set Mail Template.
    @param R_MailText_ID Text templates for mailings */
    public void setR_MailText_ID (int R_MailText_ID)
    {
        if (R_MailText_ID <= 0) set_Value ("R_MailText_ID", null);
        else
        set_Value ("R_MailText_ID", Integer.valueOf(R_MailText_ID));
        
    }
    
    /** Get Mail Template.
    @return Text templates for mailings */
    public int getR_MailText_ID() 
    {
        return get_ValueAsInt("R_MailText_ID");
        
    }
    
    /** Set Related Request.
    @param R_RequestRelated_ID Related Request (Master Issue...) */
    public void setR_RequestRelated_ID (int R_RequestRelated_ID)
    {
        if (R_RequestRelated_ID <= 0) set_Value ("R_RequestRelated_ID", null);
        else
        set_Value ("R_RequestRelated_ID", Integer.valueOf(R_RequestRelated_ID));
        
    }
    
    /** Get Related Request.
    @return Related Request (Master Issue...) */
    public int getR_RequestRelated_ID() 
    {
        return get_ValueAsInt("R_RequestRelated_ID");
        
    }
    
    /** Set Request Type.
    @param R_RequestType_ID Type of request (e.g. Inquiry, Complaint...) */
    public void setR_RequestType_ID (int R_RequestType_ID)
    {
        if (R_RequestType_ID < 1) throw new IllegalArgumentException ("R_RequestType_ID is mandatory.");
        set_Value ("R_RequestType_ID", Integer.valueOf(R_RequestType_ID));
        
    }
    
    /** Get Request Type.
    @return Type of request (e.g. Inquiry, Complaint...) */
    public int getR_RequestType_ID() 
    {
        return get_ValueAsInt("R_RequestType_ID");
        
    }
    
    /** Set Request.
    @param R_Request_ID Request from a Business Partner or Prospect */
    public void setR_Request_ID (int R_Request_ID)
    {
        if (R_Request_ID < 1) throw new IllegalArgumentException ("R_Request_ID is mandatory.");
        set_ValueNoCheck ("R_Request_ID", Integer.valueOf(R_Request_ID));
        
    }
    
    /** Get Request.
    @return Request from a Business Partner or Prospect */
    public int getR_Request_ID() 
    {
        return get_ValueAsInt("R_Request_ID");
        
    }
    
    /** Set Resolution.
    @param R_Resolution_ID Request Resolution */
    public void setR_Resolution_ID (int R_Resolution_ID)
    {
        if (R_Resolution_ID <= 0) set_Value ("R_Resolution_ID", null);
        else
        set_Value ("R_Resolution_ID", Integer.valueOf(R_Resolution_ID));
        
    }
    
    /** Get Resolution.
    @return Request Resolution */
    public int getR_Resolution_ID() 
    {
        return get_ValueAsInt("R_Resolution_ID");
        
    }
    
    /** Set Source.
    @param R_Source_ID Source for the Lead or Request */
    public void setR_Source_ID (int R_Source_ID)
    {
        if (R_Source_ID <= 0) set_Value ("R_Source_ID", null);
        else
        set_Value ("R_Source_ID", Integer.valueOf(R_Source_ID));
        
    }
    
    /** Get Source.
    @return Source for the Lead or Request */
    public int getR_Source_ID() 
    {
        return get_ValueAsInt("R_Source_ID");
        
    }
    
    /** Set Standard Response.
    @param R_StandardResponse_ID Request Standard Response */
    public void setR_StandardResponse_ID (int R_StandardResponse_ID)
    {
        if (R_StandardResponse_ID <= 0) set_Value ("R_StandardResponse_ID", null);
        else
        set_Value ("R_StandardResponse_ID", Integer.valueOf(R_StandardResponse_ID));
        
    }
    
    /** Get Standard Response.
    @return Request Standard Response */
    public int getR_StandardResponse_ID() 
    {
        return get_ValueAsInt("R_StandardResponse_ID");
        
    }
    
    /** Set Status.
    @param R_Status_ID Request Status */
    public void setR_Status_ID (int R_Status_ID)
    {
        if (R_Status_ID <= 0) set_Value ("R_Status_ID", null);
        else
        set_Value ("R_Status_ID", Integer.valueOf(R_Status_ID));
        
    }
    
    /** Get Status.
    @return Request Status */
    public int getR_Status_ID() 
    {
        return get_ValueAsInt("R_Status_ID");
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID <= 0) set_ValueNoCheck ("Record_ID", null);
        else
        set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set Request Amount.
    @param RequestAmt Amount associated with this request */
    public void setRequestAmt (java.math.BigDecimal RequestAmt)
    {
        if (RequestAmt == null) throw new IllegalArgumentException ("RequestAmt is mandatory.");
        set_Value ("RequestAmt", RequestAmt);
        
    }
    
    /** Get Request Amount.
    @return Amount associated with this request */
    public java.math.BigDecimal getRequestAmt() 
    {
        return get_ValueAsBigDecimal("RequestAmt");
        
    }
    
    /** Set Result.
    @param Result Result of the action taken */
    public void setResult (String Result)
    {
        set_Value ("Result", Result);
        
    }
    
    /** Get Result.
    @return Result of the action taken */
    public String getResult() 
    {
        return (String)get_Value("Result");
        
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
    
    /** Set Start Date.
    @param StartDate First effective day (inclusive) */
    public void setStartDate (Timestamp StartDate)
    {
        set_Value ("StartDate", StartDate);
        
    }
    
    /** Get Start Date.
    @return First effective day (inclusive) */
    public Timestamp getStartDate() 
    {
        return (Timestamp)get_Value("StartDate");
        
    }
    
    /** Set Start Time.
    @param StartTime Time started */
    public void setStartTime (Timestamp StartTime)
    {
        set_Value ("StartTime", StartTime);
        
    }
    
    /** Get Start Time.
    @return Time started */
    public Timestamp getStartTime() 
    {
        return (Timestamp)get_Value("StartTime");
        
    }
    
    /** Set Summary.
    @param Summary Textual summary of this request */
    public void setSummary (String Summary)
    {
        if (Summary == null) throw new IllegalArgumentException ("Summary is mandatory.");
        set_Value ("Summary", Summary);
        
    }
    
    /** Get Summary.
    @return Textual summary of this request */
    public String getSummary() 
    {
        return (String)get_Value("Summary");
        
    }
    
    /** 0% Not Started = 0 */
    public static final String TASKSTATUS_0NotStarted = X_Ref_R_Request_TaskStatus._0_NOT_STARTED.getValue();
    /** 20% Started = 2 */
    public static final String TASKSTATUS_20Started = X_Ref_R_Request_TaskStatus._20_STARTED.getValue();
    /** 40% Busy = 4 */
    public static final String TASKSTATUS_40Busy = X_Ref_R_Request_TaskStatus._40_BUSY.getValue();
    /** 60% Good Progress = 6 */
    public static final String TASKSTATUS_60GoodProgress = X_Ref_R_Request_TaskStatus._60_GOOD_PROGRESS.getValue();
    /** 80% Nearly Done = 8 */
    public static final String TASKSTATUS_80NearlyDone = X_Ref_R_Request_TaskStatus._80_NEARLY_DONE.getValue();
    /** 90% Finishing = 9 */
    public static final String TASKSTATUS_90Finishing = X_Ref_R_Request_TaskStatus._90_FINISHING.getValue();
    /** 95% Almost Done = A */
    public static final String TASKSTATUS_95AlmostDone = X_Ref_R_Request_TaskStatus._95_ALMOST_DONE.getValue();
    /** 99% Cleaning up = C */
    public static final String TASKSTATUS_99CleaningUp = X_Ref_R_Request_TaskStatus._99_CLEANING_UP.getValue();
    /** 100% Complete = D */
    public static final String TASKSTATUS_100Complete = X_Ref_R_Request_TaskStatus._100_COMPLETE.getValue();
    /** Set Task Status.
    @param TaskStatus Status of the Task */
    public void setTaskStatus (String TaskStatus)
    {
        if (!X_Ref_R_Request_TaskStatus.isValid(TaskStatus))
        throw new IllegalArgumentException ("TaskStatus Invalid value - " + TaskStatus + " - Reference_ID=366 - 0 - 2 - 4 - 6 - 8 - 9 - A - C - D");
        set_Value ("TaskStatus", TaskStatus);
        
    }
    
    /** Get Task Status.
    @return Status of the Task */
    public String getTaskStatus() 
    {
        return (String)get_Value("TaskStatus");
        
    }
    
    
}
