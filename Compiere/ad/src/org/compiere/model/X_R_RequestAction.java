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
/** Generated Model for R_RequestAction
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_R_RequestAction.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_R_RequestAction extends PO
{
    /** Standard Constructor
    @param ctx context
    @param R_RequestAction_ID id
    @param trx transaction
    */
    public X_R_RequestAction (Ctx ctx, int R_RequestAction_ID, Trx trx)
    {
        super (ctx, R_RequestAction_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (R_RequestAction_ID == 0)
        {
            setR_RequestAction_ID (0);
            setR_Request_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_R_RequestAction (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=418 */
    public static final int Table_ID=418;
    
    /** TableName=R_RequestAction */
    public static final String Table_Name="R_RequestAction";
    
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
        if (AD_Role_ID <= 0) set_ValueNoCheck ("AD_Role_ID", null);
        else
        set_ValueNoCheck ("AD_Role_ID", Integer.valueOf(AD_Role_ID));
        
    }
    
    /** Get Role.
    @return Responsibility Role */
    public int getAD_Role_ID() 
    {
        return get_ValueAsInt("AD_Role_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_ValueNoCheck ("AD_User_ID", null);
        else
        set_ValueNoCheck ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
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
        if (A_Asset_ID <= 0) set_ValueNoCheck ("A_Asset_ID", null);
        else
        set_ValueNoCheck ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
        
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
        if (C_Activity_ID <= 0) set_ValueNoCheck ("C_Activity_ID", null);
        else
        set_ValueNoCheck ("C_Activity_ID", Integer.valueOf(C_Activity_ID));
        
    }
    
    /** Get Activity.
    @return Business Activity */
    public int getC_Activity_ID() 
    {
        return get_ValueAsInt("C_Activity_ID");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_ValueNoCheck ("C_BPartner_ID", null);
        else
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
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
        if (C_Invoice_ID <= 0) set_ValueNoCheck ("C_Invoice_ID", null);
        else
        set_ValueNoCheck ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
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
        if (C_Order_ID <= 0) set_ValueNoCheck ("C_Order_ID", null);
        else
        set_ValueNoCheck ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
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
        if (C_Payment_ID <= 0) set_ValueNoCheck ("C_Payment_ID", null);
        else
        set_ValueNoCheck ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
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
        if (C_Project_ID <= 0) set_ValueNoCheck ("C_Project_ID", null);
        else
        set_ValueNoCheck ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
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
        if (!X_Ref_R_Request_Confidential.isValid(ConfidentialType))
        throw new IllegalArgumentException ("ConfidentialType Invalid value - " + ConfidentialType + " - Reference_ID=340 - A - C - I - P");
        set_ValueNoCheck ("ConfidentialType", ConfidentialType);
        
    }
    
    /** Get Confidentiality.
    @return Type of Confidentiality */
    public String getConfidentialType() 
    {
        return (String)get_Value("ConfidentialType");
        
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
    
    /** Set Date next action.
    @param DateNextAction Date that this request should be acted on */
    public void setDateNextAction (Timestamp DateNextAction)
    {
        set_ValueNoCheck ("DateNextAction", DateNextAction);
        
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
    
    /** Set End Date.
    @param EndDate Last effective date (inclusive) */
    public void setEndDate (Timestamp EndDate)
    {
        set_Value ("EndDate", EndDate);
        
    }
    
    /** Get End Date.
    @return Last effective date (inclusive) */
    public Timestamp getEndDate() 
    {
        return (Timestamp)get_Value("EndDate");
        
    }
    
    /** No = N */
    public static final String ISESCALATED_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISESCALATED_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Escalated.
    @param IsEscalated This request has been escalated */
    public void setIsEscalated (String IsEscalated)
    {
        if (!X_Ref__YesNo.isValid(IsEscalated))
        throw new IllegalArgumentException ("IsEscalated Invalid value - " + IsEscalated + " - Reference_ID=319 - N - Y");
        set_ValueNoCheck ("IsEscalated", IsEscalated);
        
    }
    
    /** Get Escalated.
    @return This request has been escalated */
    public String getIsEscalated() 
    {
        return (String)get_Value("IsEscalated");
        
    }
    
    /** No = N */
    public static final String ISINVOICED_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISINVOICED_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Invoiced.
    @param IsInvoiced Is this invoiced? */
    public void setIsInvoiced (String IsInvoiced)
    {
        if (!X_Ref__YesNo.isValid(IsInvoiced))
        throw new IllegalArgumentException ("IsInvoiced Invalid value - " + IsInvoiced + " - Reference_ID=319 - N - Y");
        set_ValueNoCheck ("IsInvoiced", IsInvoiced);
        
    }
    
    /** Get Invoiced.
    @return Is this invoiced? */
    public String getIsInvoiced() 
    {
        return (String)get_Value("IsInvoiced");
        
    }
    
    /** No = N */
    public static final String ISSELFSERVICE_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISSELFSERVICE_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Self-Service.
    @param IsSelfService This is a Self-Service entry or this entry can be changed via Self-Service */
    public void setIsSelfService (String IsSelfService)
    {
        if (!X_Ref__YesNo.isValid(IsSelfService))
        throw new IllegalArgumentException ("IsSelfService Invalid value - " + IsSelfService + " - Reference_ID=319 - N - Y");
        set_ValueNoCheck ("IsSelfService", IsSelfService);
        
    }
    
    /** Get Self-Service.
    @return This is a Self-Service entry or this entry can be changed via Self-Service */
    public String getIsSelfService() 
    {
        return (String)get_Value("IsSelfService");
        
    }
    
    /** Set Shipment/Receipt.
    @param M_InOut_ID Material Shipment Document */
    public void setM_InOut_ID (int M_InOut_ID)
    {
        if (M_InOut_ID <= 0) set_ValueNoCheck ("M_InOut_ID", null);
        else
        set_ValueNoCheck ("M_InOut_ID", Integer.valueOf(M_InOut_ID));
        
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
        if (M_Product_ID <= 0) set_ValueNoCheck ("M_Product_ID", null);
        else
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Null Columns.
    @param NullColumns Columns with NULL value */
    public void setNullColumns (String NullColumns)
    {
        set_ValueNoCheck ("NullColumns", NullColumns);
        
    }
    
    /** Get Null Columns.
    @return Columns with NULL value */
    public String getNullColumns() 
    {
        return (String)get_Value("NullColumns");
        
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
        if (!X_Ref__PriorityRule.isValid(Priority))
        throw new IllegalArgumentException ("Priority Invalid value - " + Priority + " - Reference_ID=154 - 1 - 3 - 5 - 7 - 9");
        set_ValueNoCheck ("Priority", Priority);
        
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
        set_ValueNoCheck ("PriorityUser", PriorityUser);
        
    }
    
    /** Get User Priority.
    @return Priority of the issue for the User */
    public String getPriorityUser() 
    {
        return (String)get_Value("PriorityUser");
        
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
        if (R_Category_ID <= 0) set_ValueNoCheck ("R_Category_ID", null);
        else
        set_ValueNoCheck ("R_Category_ID", Integer.valueOf(R_Category_ID));
        
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
        if (R_Group_ID <= 0) set_ValueNoCheck ("R_Group_ID", null);
        else
        set_ValueNoCheck ("R_Group_ID", Integer.valueOf(R_Group_ID));
        
    }
    
    /** Get Group.
    @return Request Group */
    public int getR_Group_ID() 
    {
        return get_ValueAsInt("R_Group_ID");
        
    }
    
    /** Set Request History.
    @param R_RequestAction_ID Request has been changed */
    public void setR_RequestAction_ID (int R_RequestAction_ID)
    {
        if (R_RequestAction_ID < 1) throw new IllegalArgumentException ("R_RequestAction_ID is mandatory.");
        set_ValueNoCheck ("R_RequestAction_ID", Integer.valueOf(R_RequestAction_ID));
        
    }
    
    /** Get Request History.
    @return Request has been changed */
    public int getR_RequestAction_ID() 
    {
        return get_ValueAsInt("R_RequestAction_ID");
        
    }
    
    /** Set Request Type.
    @param R_RequestType_ID Type of request (e.g. Inquiry, Complaint...) */
    public void setR_RequestType_ID (int R_RequestType_ID)
    {
        if (R_RequestType_ID <= 0) set_ValueNoCheck ("R_RequestType_ID", null);
        else
        set_ValueNoCheck ("R_RequestType_ID", Integer.valueOf(R_RequestType_ID));
        
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
        if (R_Resolution_ID <= 0) set_ValueNoCheck ("R_Resolution_ID", null);
        else
        set_ValueNoCheck ("R_Resolution_ID", Integer.valueOf(R_Resolution_ID));
        
    }
    
    /** Get Resolution.
    @return Request Resolution */
    public int getR_Resolution_ID() 
    {
        return get_ValueAsInt("R_Resolution_ID");
        
    }
    
    /** Set Status.
    @param R_Status_ID Request Status */
    public void setR_Status_ID (int R_Status_ID)
    {
        if (R_Status_ID <= 0) set_ValueNoCheck ("R_Status_ID", null);
        else
        set_ValueNoCheck ("R_Status_ID", Integer.valueOf(R_Status_ID));
        
    }
    
    /** Get Status.
    @return Request Status */
    public int getR_Status_ID() 
    {
        return get_ValueAsInt("R_Status_ID");
        
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
    
    /** Set Summary.
    @param Summary Textual summary of this request */
    public void setSummary (String Summary)
    {
        set_ValueNoCheck ("Summary", Summary);
        
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
