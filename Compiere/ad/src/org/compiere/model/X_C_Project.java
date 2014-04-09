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
/** Generated Model for C_Project
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Project.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Project extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Project_ID id
    @param trx transaction
    */
    public X_C_Project (Ctx ctx, int C_Project_ID, Trx trx)
    {
        super (ctx, C_Project_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Project_ID == 0)
        {
            setC_Project_ID (0);
            setCommittedAmt (Env.ZERO);
            setCommittedQty (Env.ZERO);
            setInvoicedAmt (Env.ZERO);
            setInvoicedQty (Env.ZERO);
            setIsCommitCeiling (false);
            setIsCommitment (false);
            setIsSummary (false);
            setName (null);
            setPlannedAmt (Env.ZERO);
            setPlannedMarginAmt (Env.ZERO);
            setPlannedQty (Env.ZERO);
            setProcessed (false);	// N
            setProjInvoiceRule (null);	// -
            setProjectBalanceAmt (Env.ZERO);
            setProjectLineLevel (null);	// P
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Project (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=203 */
    public static final int Table_ID=203;
    
    /** TableName=C_Project */
    public static final String Table_Name="C_Project";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID <= 0) set_Value ("C_BPartner_Location_ID", null);
        else
        set_Value ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
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
    
    /** Set Standard Phase.
    @param C_Phase_ID Standard Phase of the Project Type */
    public void setC_Phase_ID (int C_Phase_ID)
    {
        if (C_Phase_ID <= 0) set_Value ("C_Phase_ID", null);
        else
        set_Value ("C_Phase_ID", Integer.valueOf(C_Phase_ID));
        
    }
    
    /** Get Standard Phase.
    @return Standard Phase of the Project Type */
    public int getC_Phase_ID() 
    {
        return get_ValueAsInt("C_Phase_ID");
        
    }
    
    /** Set Project Type.
    @param C_ProjectType_ID Type of the project */
    public void setC_ProjectType_ID (String C_ProjectType_ID)
    {
        set_Value ("C_ProjectType_ID", C_ProjectType_ID);
        
    }
    
    /** Get Project Type.
    @return Type of the project */
    public String getC_ProjectType_ID() 
    {
        return (String)get_Value("C_ProjectType_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID < 1) throw new IllegalArgumentException ("C_Project_ID is mandatory.");
        set_ValueNoCheck ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
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
    
    /** Set Committed Amount.
    @param CommittedAmt The (legal) commitment amount */
    public void setCommittedAmt (java.math.BigDecimal CommittedAmt)
    {
        if (CommittedAmt == null) throw new IllegalArgumentException ("CommittedAmt is mandatory.");
        set_Value ("CommittedAmt", CommittedAmt);
        
    }
    
    /** Get Committed Amount.
    @return The (legal) commitment amount */
    public java.math.BigDecimal getCommittedAmt() 
    {
        return get_ValueAsBigDecimal("CommittedAmt");
        
    }
    
    /** Set Committed Quantity.
    @param CommittedQty The (legal) commitment Quantity */
    public void setCommittedQty (java.math.BigDecimal CommittedQty)
    {
        if (CommittedQty == null) throw new IllegalArgumentException ("CommittedQty is mandatory.");
        set_Value ("CommittedQty", CommittedQty);
        
    }
    
    /** Get Committed Quantity.
    @return The (legal) commitment Quantity */
    public java.math.BigDecimal getCommittedQty() 
    {
        return get_ValueAsBigDecimal("CommittedQty");
        
    }
    
    /** Set Copy From.
    @param CopyFrom Copy From Record */
    public void setCopyFrom (String CopyFrom)
    {
        set_Value ("CopyFrom", CopyFrom);
        
    }
    
    /** Get Copy From.
    @return Copy From Record */
    public String getCopyFrom() 
    {
        return (String)get_Value("CopyFrom");
        
    }
    
    /** Set Contract Date.
    @param DateContract Indicates the (planned) effective date of this document. */
    public void setDateContract (Timestamp DateContract)
    {
        set_Value ("DateContract", DateContract);
        
    }
    
    /** Get Contract Date.
    @return Indicates the (planned) effective date of this document. */
    public Timestamp getDateContract() 
    {
        return (Timestamp)get_Value("DateContract");
        
    }
    
    /** Set Finish Date.
    @param DateFinish Indicates the (planned) completion date */
    public void setDateFinish (Timestamp DateFinish)
    {
        set_Value ("DateFinish", DateFinish);
        
    }
    
    /** Get Finish Date.
    @return Indicates the (planned) completion date */
    public Timestamp getDateFinish() 
    {
        return (Timestamp)get_Value("DateFinish");
        
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
    
    /** Set Generate To.
    @param GenerateTo Generate To */
    public void setGenerateTo (String GenerateTo)
    {
        set_Value ("GenerateTo", GenerateTo);
        
    }
    
    /** Get Generate To.
    @return Generate To */
    public String getGenerateTo() 
    {
        return (String)get_Value("GenerateTo");
        
    }
    
    /** Set Invoiced Amount.
    @param InvoicedAmt The amount invoiced */
    public void setInvoicedAmt (java.math.BigDecimal InvoicedAmt)
    {
        if (InvoicedAmt == null) throw new IllegalArgumentException ("InvoicedAmt is mandatory.");
        set_ValueNoCheck ("InvoicedAmt", InvoicedAmt);
        
    }
    
    /** Get Invoiced Amount.
    @return The amount invoiced */
    public java.math.BigDecimal getInvoicedAmt() 
    {
        return get_ValueAsBigDecimal("InvoicedAmt");
        
    }
    
    /** Set Quantity Invoiced.
    @param InvoicedQty The quantity invoiced */
    public void setInvoicedQty (java.math.BigDecimal InvoicedQty)
    {
        if (InvoicedQty == null) throw new IllegalArgumentException ("InvoicedQty is mandatory.");
        set_ValueNoCheck ("InvoicedQty", InvoicedQty);
        
    }
    
    /** Get Quantity Invoiced.
    @return The quantity invoiced */
    public java.math.BigDecimal getInvoicedQty() 
    {
        return get_ValueAsBigDecimal("InvoicedQty");
        
    }
    
    /** Set Commitment is Ceiling.
    @param IsCommitCeiling The commitment amount/quantity is the chargeable ceiling */
    public void setIsCommitCeiling (boolean IsCommitCeiling)
    {
        set_Value ("IsCommitCeiling", Boolean.valueOf(IsCommitCeiling));
        
    }
    
    /** Get Commitment is Ceiling.
    @return The commitment amount/quantity is the chargeable ceiling */
    public boolean isCommitCeiling() 
    {
        return get_ValueAsBoolean("IsCommitCeiling");
        
    }
    
    /** Set Commitment.
    @param IsCommitment Is this document a (legal) commitment? */
    public void setIsCommitment (boolean IsCommitment)
    {
        set_Value ("IsCommitment", Boolean.valueOf(IsCommitment));
        
    }
    
    /** Get Commitment.
    @return Is this document a (legal) commitment? */
    public boolean isCommitment() 
    {
        return get_ValueAsBoolean("IsCommitment");
        
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
    
    /** Set Price List Version.
    @param M_PriceList_Version_ID Identifies a unique instance of a Price List */
    public void setM_PriceList_Version_ID (int M_PriceList_Version_ID)
    {
        if (M_PriceList_Version_ID <= 0) set_Value ("M_PriceList_Version_ID", null);
        else
        set_Value ("M_PriceList_Version_ID", Integer.valueOf(M_PriceList_Version_ID));
        
    }
    
    /** Get Price List Version.
    @return Identifies a unique instance of a Price List */
    public int getM_PriceList_Version_ID() 
    {
        return get_ValueAsInt("M_PriceList_Version_ID");
        
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
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Note.
    @param Note Optional additional user defined information */
    public void setNote (String Note)
    {
        set_Value ("Note", Note);
        
    }
    
    /** Get Note.
    @return Optional additional user defined information */
    public String getNote() 
    {
        return (String)get_Value("Note");
        
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
    
    /** Set Planned Amount.
    @param PlannedAmt Planned amount for this project */
    public void setPlannedAmt (java.math.BigDecimal PlannedAmt)
    {
        if (PlannedAmt == null) throw new IllegalArgumentException ("PlannedAmt is mandatory.");
        set_Value ("PlannedAmt", PlannedAmt);
        
    }
    
    /** Get Planned Amount.
    @return Planned amount for this project */
    public java.math.BigDecimal getPlannedAmt() 
    {
        return get_ValueAsBigDecimal("PlannedAmt");
        
    }
    
    /** Set Planned Date.
    @param PlannedDate Date projected */
    public void setPlannedDate (Timestamp PlannedDate)
    {
        set_Value ("PlannedDate", PlannedDate);
        
    }
    
    /** Get Planned Date.
    @return Date projected */
    public Timestamp getPlannedDate() 
    {
        return (Timestamp)get_Value("PlannedDate");
        
    }
    
    /** Set Planned Margin.
    @param PlannedMarginAmt Project's planned margin amount */
    public void setPlannedMarginAmt (java.math.BigDecimal PlannedMarginAmt)
    {
        if (PlannedMarginAmt == null) throw new IllegalArgumentException ("PlannedMarginAmt is mandatory.");
        set_Value ("PlannedMarginAmt", PlannedMarginAmt);
        
    }
    
    /** Get Planned Margin.
    @return Project's planned margin amount */
    public java.math.BigDecimal getPlannedMarginAmt() 
    {
        return get_ValueAsBigDecimal("PlannedMarginAmt");
        
    }
    
    /** Set Planned Quantity.
    @param PlannedQty Planned quantity for this project */
    public void setPlannedQty (java.math.BigDecimal PlannedQty)
    {
        if (PlannedQty == null) throw new IllegalArgumentException ("PlannedQty is mandatory.");
        set_Value ("PlannedQty", PlannedQty);
        
    }
    
    /** Get Planned Quantity.
    @return Planned quantity for this project */
    public java.math.BigDecimal getPlannedQty() 
    {
        return get_ValueAsBigDecimal("PlannedQty");
        
    }
    
    /** Set Probability.
    @param Probability Probability in Percent */
    public void setProbability (int Probability)
    {
        set_Value ("Probability", Integer.valueOf(Probability));
        
    }
    
    /** Get Probability.
    @return Probability in Percent */
    public int getProbability() 
    {
        return get_ValueAsInt("Probability");
        
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
    
    /** None = - */
    public static final String PROJINVOICERULE_None = X_Ref_C_Project_InvoiceRule.NONE.getValue();
    /** Committed Amount = C */
    public static final String PROJINVOICERULE_CommittedAmount = X_Ref_C_Project_InvoiceRule.COMMITTED_AMOUNT.getValue();
    /** Product Quantity = P */
    public static final String PROJINVOICERULE_ProductQuantity = X_Ref_C_Project_InvoiceRule.PRODUCT_QUANTITY.getValue();
    /** Time&Material = T */
    public static final String PROJINVOICERULE_TimeMaterial = X_Ref_C_Project_InvoiceRule.TIME_MATERIAL.getValue();
    /** Time&Material max Committed = c */
    public static final String PROJINVOICERULE_TimeMaterialMaxCommitted = X_Ref_C_Project_InvoiceRule.TIME_MATERIAL_MAX_COMMITTED.getValue();
    /** Set Invoice Rule.
    @param ProjInvoiceRule Invoice Rule for the project */
    public void setProjInvoiceRule (String ProjInvoiceRule)
    {
        if (ProjInvoiceRule == null) throw new IllegalArgumentException ("ProjInvoiceRule is mandatory");
        if (!X_Ref_C_Project_InvoiceRule.isValid(ProjInvoiceRule))
        throw new IllegalArgumentException ("ProjInvoiceRule Invalid value - " + ProjInvoiceRule + " - Reference_ID=383 - - - C - P - T - c");
        set_Value ("ProjInvoiceRule", ProjInvoiceRule);
        
    }
    
    /** Get Invoice Rule.
    @return Invoice Rule for the project */
    public String getProjInvoiceRule() 
    {
        return (String)get_Value("ProjInvoiceRule");
        
    }
    
    /** Set Project Balance.
    @param ProjectBalanceAmt Total Project Balance */
    public void setProjectBalanceAmt (java.math.BigDecimal ProjectBalanceAmt)
    {
        if (ProjectBalanceAmt == null) throw new IllegalArgumentException ("ProjectBalanceAmt is mandatory.");
        set_ValueNoCheck ("ProjectBalanceAmt", ProjectBalanceAmt);
        
    }
    
    /** Get Project Balance.
    @return Total Project Balance */
    public java.math.BigDecimal getProjectBalanceAmt() 
    {
        return get_ValueAsBigDecimal("ProjectBalanceAmt");
        
    }
    
    /** Asset Project = A */
    public static final String PROJECTCATEGORY_AssetProject = X_Ref_C_ProjectType_Category.ASSET_PROJECT.getValue();
    /** General = N */
    public static final String PROJECTCATEGORY_General = X_Ref_C_ProjectType_Category.GENERAL.getValue();
    /** Service (Charge) Project = S */
    public static final String PROJECTCATEGORY_ServiceChargeProject = X_Ref_C_ProjectType_Category.SERVICE_CHARGE_PROJECT.getValue();
    /** Work Order (Job) = W */
    public static final String PROJECTCATEGORY_WorkOrderJob = X_Ref_C_ProjectType_Category.WORK_ORDER_JOB.getValue();
    /** Set Project Category.
    @param ProjectCategory Project Category */
    public void setProjectCategory (String ProjectCategory)
    {
        if (!X_Ref_C_ProjectType_Category.isValid(ProjectCategory))
        throw new IllegalArgumentException ("ProjectCategory Invalid value - " + ProjectCategory + " - Reference_ID=288 - A - N - S - W");
        set_Value ("ProjectCategory", ProjectCategory);
        
    }
    
    /** Get Project Category.
    @return Project Category */
    public String getProjectCategory() 
    {
        return (String)get_Value("ProjectCategory");
        
    }
    
    /** Phase = A */
    public static final String PROJECTLINELEVEL_Phase = X_Ref_C_Project_LineLevel.PHASE.getValue();
    /** Project = P */
    public static final String PROJECTLINELEVEL_Project = X_Ref_C_Project_LineLevel.PROJECT.getValue();
    /** Task = T */
    public static final String PROJECTLINELEVEL_Task = X_Ref_C_Project_LineLevel.TASK.getValue();
    /** Set Line Level.
    @param ProjectLineLevel Project Line Level */
    public void setProjectLineLevel (String ProjectLineLevel)
    {
        if (ProjectLineLevel == null) throw new IllegalArgumentException ("ProjectLineLevel is mandatory");
        if (!X_Ref_C_Project_LineLevel.isValid(ProjectLineLevel))
        throw new IllegalArgumentException ("ProjectLineLevel Invalid value - " + ProjectLineLevel + " - Reference_ID=384 - A - P - T");
        set_Value ("ProjectLineLevel", ProjectLineLevel);
        
    }
    
    /** Get Line Level.
    @return Project Line Level */
    public String getProjectLineLevel() 
    {
        return (String)get_Value("ProjectLineLevel");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getValue());
        
    }
    
    
}
