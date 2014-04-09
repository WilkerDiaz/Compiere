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
/** Generated Model for I_Request
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_I_Request.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_I_Request extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_Request_ID id
    @param trx transaction
    */
    public X_I_Request (Ctx ctx, int I_Request_ID, Trx trx)
    {
        super (ctx, I_Request_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_Request_ID == 0)
        {
            setI_IsImported (null);	// N
            setI_Request_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_Request (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511158643789L;
    /** Last Updated Timestamp 2008-12-11 14:08:47.0 */
    public static final long updatedMS = 1229033327000L;
    /** AD_Table_ID=940 */
    public static final int Table_ID=940;
    
    /** TableName=I_Request */
    public static final String Table_Name="I_Request";
    
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
        if (AD_Table_ID <= 0) set_Value ("AD_Table_ID", null);
        else
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
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
    
    /** Set Activity Name.
    @param ActivityName Alphanumeric identifier of an Activity */
    public void setActivityName (String ActivityName)
    {
        set_Value ("ActivityName", ActivityName);
        
    }
    
    /** Get Activity Name.
    @return Alphanumeric identifier of an Activity */
    public String getActivityName() 
    {
        return (String)get_Value("ActivityName");
        
    }
    
    /** Set Activity Key.
    @param ActivityValue Search Key for an Activity */
    public void setActivityValue (String ActivityValue)
    {
        set_Value ("ActivityValue", ActivityValue);
        
    }
    
    /** Get Activity Key.
    @return Search Key for an Activity */
    public String getActivityValue() 
    {
        return (String)get_Value("ActivityValue");
        
    }
    
    /** Set Asset Name.
    @param AssetName Alphanumeric identitfier of an Asset */
    public void setAssetName (String AssetName)
    {
        set_Value ("AssetName", AssetName);
        
    }
    
    /** Get Asset Name.
    @return Alphanumeric identitfier of an Asset */
    public String getAssetName() 
    {
        return (String)get_Value("AssetName");
        
    }
    
    /** Set Asset Key.
    @param AssetValue Search key for an Asset */
    public void setAssetValue (String AssetValue)
    {
        set_Value ("AssetValue", AssetValue);
        
    }
    
    /** Get Asset Key.
    @return Search key for an Asset */
    public String getAssetValue() 
    {
        return (String)get_Value("AssetValue");
        
    }
    
    /** Set Business Partner Name.
    @param BPartnerName Alphanumeric identitfier for a Business Partner */
    public void setBPartnerName (String BPartnerName)
    {
        set_Value ("BPartnerName", BPartnerName);
        
    }
    
    /** Get Business Partner Name.
    @return Alphanumeric identitfier for a Business Partner */
    public String getBPartnerName() 
    {
        return (String)get_Value("BPartnerName");
        
    }
    
    /** Set BPartner (Agent) Name.
    @param BPartnerSRName Agent or Sales Representative name */
    public void setBPartnerSRName (String BPartnerSRName)
    {
        set_Value ("BPartnerSRName", BPartnerSRName);
        
    }
    
    /** Get BPartner (Agent) Name.
    @return Agent or Sales Representative name */
    public String getBPartnerSRName() 
    {
        return (String)get_Value("BPartnerSRName");
        
    }
    
    /** Set BPartner (Agent) Key.
    @param BPartnerSRValue Search Key for an Agent or Sales Representative */
    public void setBPartnerSRValue (String BPartnerSRValue)
    {
        set_Value ("BPartnerSRValue", BPartnerSRValue);
        
    }
    
    /** Get BPartner (Agent) Key.
    @return Search Key for an Agent or Sales Representative */
    public String getBPartnerSRValue() 
    {
        return (String)get_Value("BPartnerSRValue");
        
    }
    
    /** Set Business Partner Key.
    @param BPartnerValue Key of the Business Partner */
    public void setBPartnerValue (String BPartnerValue)
    {
        set_Value ("BPartnerValue", BPartnerValue);
        
    }
    
    /** Get Business Partner Key.
    @return Key of the Business Partner */
    public String getBPartnerValue() 
    {
        return (String)get_Value("BPartnerValue");
        
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
    
    /** Set Campaign Name.
    @param CampaignName Alphanumeric identifier of a Campaign */
    public void setCampaignName (String CampaignName)
    {
        set_Value ("CampaignName", CampaignName);
        
    }
    
    /** Get Campaign Name.
    @return Alphanumeric identifier of a Campaign */
    public String getCampaignName() 
    {
        return (String)get_Value("CampaignName");
        
    }
    
    /** Set Campaign Key.
    @param CampaignValue Search key for a Campaign */
    public void setCampaignValue (String CampaignValue)
    {
        set_Value ("CampaignValue", CampaignValue);
        
    }
    
    /** Get Campaign Key.
    @return Search key for a Campaign */
    public String getCampaignValue() 
    {
        return (String)get_Value("CampaignValue");
        
    }
    
    /** Set Category Name.
    @param CategoryName Name of the Category */
    public void setCategoryName (String CategoryName)
    {
        set_Value ("CategoryName", CategoryName);
        
    }
    
    /** Get Category Name.
    @return Name of the Category */
    public String getCategoryName() 
    {
        return (String)get_Value("CategoryName");
        
    }
    
    /** Set Change Request Name.
    @param ChangeRequestName Change Request Name */
    public void setChangeRequestName (String ChangeRequestName)
    {
        set_Value ("ChangeRequestName", ChangeRequestName);
        
    }
    
    /** Get Change Request Name.
    @return Change Request Name */
    public String getChangeRequestName() 
    {
        return (String)get_Value("ChangeRequestName");
        
    }
    
    /** Set Tenant Name.
    @param ClientName Name of the Tenant */
    public void setClientName (String ClientName)
    {
        set_Value ("ClientName", ClientName);
        
    }
    
    /** Get Tenant Name.
    @return Name of the Tenant */
    public String getClientName() 
    {
        return (String)get_Value("ClientName");
        
    }
    
    /** Set Tenant Key.
    @param ClientValue Key of the Tenant */
    public void setClientValue (String ClientValue)
    {
        set_Value ("ClientValue", ClientValue);
        
    }
    
    /** Get Tenant Key.
    @return Key of the Tenant */
    public String getClientValue() 
    {
        return (String)get_Value("ClientValue");
        
    }
    
    /** Set Close Date.
    @param CloseDate Close Date */
    public void setCloseDate (Timestamp CloseDate)
    {
        set_ValueNoCheck ("CloseDate", CloseDate);
        
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
    
    /** Set Contact Name.
    @param ContactName Business Partner Contact Name */
    public void setContactName (String ContactName)
    {
        set_Value ("ContactName", ContactName);
        
    }
    
    /** Get Contact Name.
    @return Business Partner Contact Name */
    public String getContactName() 
    {
        return (String)get_Value("ContactName");
        
    }
    
    /** Set Contact Key.
    @param ContactValue Key of the Contact */
    public void setContactValue (String ContactValue)
    {
        set_Value ("ContactValue", ContactValue);
        
    }
    
    /** Get Contact Key.
    @return Key of the Contact */
    public String getContactValue() 
    {
        return (String)get_Value("ContactValue");
        
    }
    
    /** Set Complete Plan.
    @param DateCompletePlan Planned Completion Date */
    public void setDateCompletePlan (Timestamp DateCompletePlan)
    {
        set_ValueNoCheck ("DateCompletePlan", DateCompletePlan);
        
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
        set_ValueNoCheck ("DateLastAlert", DateLastAlert);
        
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
        set_ValueNoCheck ("DateStartPlan", DateStartPlan);
        
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
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
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
        set_ValueNoCheck ("EndTime", EndTime);
        
    }
    
    /** Get End Time.
    @return End of the time span */
    public Timestamp getEndTime() 
    {
        return (Timestamp)get_Value("EndTime");
        
    }
    
    /** Set Group Name.
    @param GroupName Group Name */
    public void setGroupName (String GroupName)
    {
        set_Value ("GroupName", GroupName);
        
    }
    
    /** Get Group Name.
    @return Group Name */
    public String getGroupName() 
    {
        return (String)get_Value("GroupName");
        
    }
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Error = E */
    public static final String I_ISIMPORTED_Error = X_Ref__IsImported.ERROR.getValue();
    /** No = N */
    public static final String I_ISIMPORTED_No = X_Ref__IsImported.NO.getValue();
    /** Yes = Y */
    public static final String I_ISIMPORTED_Yes = X_Ref__IsImported.YES.getValue();
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (String I_IsImported)
    {
        if (I_IsImported == null) throw new IllegalArgumentException ("I_IsImported is mandatory");
        if (!X_Ref__IsImported.isValid(I_IsImported))
        throw new IllegalArgumentException ("I_IsImported Invalid value - " + I_IsImported + " - Reference_ID=420 - E - N - Y");
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set I_Request_ID.
    @param I_Request_ID I_Request_ID */
    public void setI_Request_ID (int I_Request_ID)
    {
        if (I_Request_ID < 1) throw new IllegalArgumentException ("I_Request_ID is mandatory.");
        set_ValueNoCheck ("I_Request_ID", Integer.valueOf(I_Request_ID));
        
    }
    
    /** Get I_Request_ID.
    @return I_Request_ID */
    public int getI_Request_ID() 
    {
        return get_ValueAsInt("I_Request_ID");
        
    }
    
    /** Set Shipment/Receipt Document No.
    @param InOutDocumentNo Shipment/Receipt Document No */
    public void setInOutDocumentNo (String InOutDocumentNo)
    {
        set_Value ("InOutDocumentNo", InOutDocumentNo);
        
    }
    
    /** Get Shipment/Receipt Document No.
    @return Shipment/Receipt Document No */
    public String getInOutDocumentNo() 
    {
        return (String)get_Value("InOutDocumentNo");
        
    }
    
    /** Set Invoice Document No.
    @param InvoiceDocumentNo Document Number of the Invoice */
    public void setInvoiceDocumentNo (String InvoiceDocumentNo)
    {
        set_Value ("InvoiceDocumentNo", InvoiceDocumentNo);
        
    }
    
    /** Get Invoice Document No.
    @return Document Number of the Invoice */
    public String getInvoiceDocumentNo() 
    {
        return (String)get_Value("InvoiceDocumentNo");
        
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
        set_Value ("IsSelfService", Boolean.valueOf(IsSelfService));
        
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
    
    /** Set Lead Document No.
    @param LeadDocumentNo Lead Document No */
    public void setLeadDocumentNo (String LeadDocumentNo)
    {
        set_Value ("LeadDocumentNo", LeadDocumentNo);
        
    }
    
    /** Get Lead Document No.
    @return Lead Document No */
    public String getLeadDocumentNo() 
    {
        return (String)get_Value("LeadDocumentNo");
        
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
    
    /** Set Order No.
    @param OrderDocumentNo Order No */
    public void setOrderDocumentNo (String OrderDocumentNo)
    {
        set_Value ("OrderDocumentNo", OrderDocumentNo);
        
    }
    
    /** Get Order No.
    @return Order No */
    public String getOrderDocumentNo() 
    {
        return (String)get_Value("OrderDocumentNo");
        
    }
    
    /** Set Organization Name.
    @param OrgName Name of the Organization */
    public void setOrgName (String OrgName)
    {
        set_Value ("OrgName", OrgName);
        
    }
    
    /** Get Organization Name.
    @return Name of the Organization */
    public String getOrgName() 
    {
        return (String)get_Value("OrgName");
        
    }
    
    /** Set Organization Key.
    @param OrgValue Key of the Organization */
    public void setOrgValue (String OrgValue)
    {
        set_Value ("OrgValue", OrgValue);
        
    }
    
    /** Get Organization Key.
    @return Key of the Organization */
    public String getOrgValue() 
    {
        return (String)get_Value("OrgValue");
        
    }
    
    /** Set Payment Document No.
    @param PaymentDocumentNo Document number of the Payment */
    public void setPaymentDocumentNo (String PaymentDocumentNo)
    {
        set_Value ("PaymentDocumentNo", PaymentDocumentNo);
        
    }
    
    /** Get Payment Document No.
    @return Document number of the Payment */
    public String getPaymentDocumentNo() 
    {
        return (String)get_Value("PaymentDocumentNo");
        
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
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Product Name.
    @param ProductName Name of the Product */
    public void setProductName (String ProductName)
    {
        set_Value ("ProductName", ProductName);
        
    }
    
    /** Get Product Name.
    @return Name of the Product */
    public String getProductName() 
    {
        return (String)get_Value("ProductName");
        
    }
    
    /** Set Product Used Name.
    @param ProductSpentName Product Used Name */
    public void setProductSpentName (String ProductSpentName)
    {
        set_Value ("ProductSpentName", ProductSpentName);
        
    }
    
    /** Get Product Used Name.
    @return Product Used Name */
    public String getProductSpentName() 
    {
        return (String)get_Value("ProductSpentName");
        
    }
    
    /** Set Product Used Key.
    @param ProductSpentValue Product Used Key */
    public void setProductSpentValue (String ProductSpentValue)
    {
        set_Value ("ProductSpentValue", ProductSpentValue);
        
    }
    
    /** Get Product Used Key.
    @return Product Used Key */
    public String getProductSpentValue() 
    {
        return (String)get_Value("ProductSpentValue");
        
    }
    
    /** Set Product Key.
    @param ProductValue Key of the Product */
    public void setProductValue (String ProductValue)
    {
        set_Value ("ProductValue", ProductValue);
        
    }
    
    /** Get Product Key.
    @return Key of the Product */
    public String getProductValue() 
    {
        return (String)get_Value("ProductValue");
        
    }
    
    /** Set Project Name.
    @param ProjectName Name of the Project */
    public void setProjectName (String ProjectName)
    {
        set_Value ("ProjectName", ProjectName);
        
    }
    
    /** Get Project Name.
    @return Name of the Project */
    public String getProjectName() 
    {
        return (String)get_Value("ProjectName");
        
    }
    
    /** Set Project Key.
    @param ProjectValue Key of the Project */
    public void setProjectValue (String ProjectValue)
    {
        set_Value ("ProjectValue", ProjectValue);
        
    }
    
    /** Get Project Key.
    @return Key of the Project */
    public String getProjectValue() 
    {
        return (String)get_Value("ProjectValue");
        
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
        if (R_RequestType_ID <= 0) set_Value ("R_RequestType_ID", null);
        else
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
        if (R_Request_ID <= 0) set_Value ("R_Request_ID", null);
        else
        set_Value ("R_Request_ID", Integer.valueOf(R_Request_ID));
        
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
    
    /** Set Request Type Name.
    @param ReqTypeName Request Type Name */
    public void setReqTypeName (String ReqTypeName)
    {
        set_Value ("ReqTypeName", ReqTypeName);
        
    }
    
    /** Get Request Type Name.
    @return Request Type Name */
    public String getReqTypeName() 
    {
        return (String)get_Value("ReqTypeName");
        
    }
    
    /** Set Request Amount.
    @param RequestAmt Amount associated with this request */
    public void setRequestAmt (java.math.BigDecimal RequestAmt)
    {
        set_Value ("RequestAmt", RequestAmt);
        
    }
    
    /** Get Request Amount.
    @return Amount associated with this request */
    public java.math.BigDecimal getRequestAmt() 
    {
        return get_ValueAsBigDecimal("RequestAmt");
        
    }
    
    /** Set Request Related Doc No.
    @param RequestRelatedDocNo Request Related Doc No */
    public void setRequestRelatedDocNo (String RequestRelatedDocNo)
    {
        set_Value ("RequestRelatedDocNo", RequestRelatedDocNo);
        
    }
    
    /** Get Request Related Doc No.
    @return Request Related Doc No */
    public String getRequestRelatedDocNo() 
    {
        return (String)get_Value("RequestRelatedDocNo");
        
    }
    
    /** Set Resolution Name.
    @param ResolutionName Resolution Name */
    public void setResolutionName (String ResolutionName)
    {
        set_Value ("ResolutionName", ResolutionName);
        
    }
    
    /** Get Resolution Name.
    @return Resolution Name */
    public String getResolutionName() 
    {
        return (String)get_Value("ResolutionName");
        
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
    
    /** Set Role Name.
    @param RoleName Role Name */
    public void setRoleName (String RoleName)
    {
        set_Value ("RoleName", RoleName);
        
    }
    
    /** Get Role Name.
    @return Role Name */
    public String getRoleName() 
    {
        return (String)get_Value("RoleName");
        
    }
    
    /** Set Sales Region Name.
    @param SalesRegionName Sales Region Name */
    public void setSalesRegionName (String SalesRegionName)
    {
        set_Value ("SalesRegionName", SalesRegionName);
        
    }
    
    /** Get Sales Region Name.
    @return Sales Region Name */
    public String getSalesRegionName() 
    {
        return (String)get_Value("SalesRegionName");
        
    }
    
    /** Set Sales Region Key.
    @param SalesRegionValue Sales Region Key */
    public void setSalesRegionValue (String SalesRegionValue)
    {
        set_Value ("SalesRegionValue", SalesRegionValue);
        
    }
    
    /** Get Sales Region Key.
    @return Sales Region Key */
    public String getSalesRegionValue() 
    {
        return (String)get_Value("SalesRegionValue");
        
    }
    
    /** Set Representative Name.
    @param SalesRepName Representative Name */
    public void setSalesRepName (String SalesRepName)
    {
        set_Value ("SalesRepName", SalesRepName);
        
    }
    
    /** Get Representative Name.
    @return Representative Name */
    public String getSalesRepName() 
    {
        return (String)get_Value("SalesRepName");
        
    }
    
    /** Set Representative Key.
    @param SalesRepValue Representative Key */
    public void setSalesRepValue (String SalesRepValue)
    {
        set_Value ("SalesRepValue", SalesRepValue);
        
    }
    
    /** Get Representative Key.
    @return Representative Key */
    public String getSalesRepValue() 
    {
        return (String)get_Value("SalesRepValue");
        
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
    
    /** Set Source Name.
    @param SourceName Source Name */
    public void setSourceName (String SourceName)
    {
        set_Value ("SourceName", SourceName);
        
    }
    
    /** Get Source Name.
    @return Source Name */
    public String getSourceName() 
    {
        return (String)get_Value("SourceName");
        
    }
    
    /** Set Source Key.
    @param SourceValue Source Key */
    public void setSourceValue (String SourceValue)
    {
        set_Value ("SourceValue", SourceValue);
        
    }
    
    /** Get Source Key.
    @return Source Key */
    public String getSourceValue() 
    {
        return (String)get_Value("SourceValue");
        
    }
    
    /** Set Start Date.
    @param StartDate First effective day (inclusive) */
    public void setStartDate (Timestamp StartDate)
    {
        set_ValueNoCheck ("StartDate", StartDate);
        
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
        set_ValueNoCheck ("StartTime", StartTime);
        
    }
    
    /** Get Start Time.
    @return Time started */
    public Timestamp getStartTime() 
    {
        return (Timestamp)get_Value("StartTime");
        
    }
    
    /** Set Status Name.
    @param StatusName Status Name */
    public void setStatusName (String StatusName)
    {
        set_Value ("StatusName", StatusName);
        
    }
    
    /** Get Status Name.
    @return Status Name */
    public String getStatusName() 
    {
        return (String)get_Value("StatusName");
        
    }
    
    /** Set Summary.
    @param Summary Textual summary of this request */
    public void setSummary (String Summary)
    {
        set_Value ("Summary", Summary);
        
    }
    
    /** Get Summary.
    @return Textual summary of this request */
    public String getSummary() 
    {
        return (String)get_Value("Summary");
        
    }
    
    /** Set DB Table Name.
    @param TableName Name of the table in the database */
    public void setTableName (String TableName)
    {
        set_Value ("TableName", TableName);
        
    }
    
    /** Get DB Table Name.
    @return Name of the table in the database */
    public String getTableName() 
    {
        return (String)get_Value("TableName");
        
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
