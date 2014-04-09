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
/** Generated Model for AD_ClientInfo
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ClientInfo.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ClientInfo extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ClientInfo_ID id
    @param trx transaction
    */
    public X_AD_ClientInfo (Ctx ctx, int AD_ClientInfo_ID, Trx trx)
    {
        super (ctx, AD_ClientInfo_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ClientInfo_ID == 0)
        {
            setAD_Tree_Product_ID (0);
            setIsDiscountLineAmt (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ClientInfo (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=227 */
    public static final int Table_ID=227;
    
    /** TableName=AD_ClientInfo */
    public static final String Table_Name="AD_ClientInfo";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Activity Tree.
    @param AD_Tree_Activity_ID Tree to determine activity hierarchy */
    public void setAD_Tree_Activity_ID (int AD_Tree_Activity_ID)
    {
        if (AD_Tree_Activity_ID <= 0) set_Value ("AD_Tree_Activity_ID", null);
        else
        set_Value ("AD_Tree_Activity_ID", Integer.valueOf(AD_Tree_Activity_ID));
        
    }
    
    /** Get Activity Tree.
    @return Tree to determine activity hierarchy */
    public int getAD_Tree_Activity_ID() 
    {
        return get_ValueAsInt("AD_Tree_Activity_ID");
        
    }
    
    /** Set BPartner Tree.
    @param AD_Tree_BPartner_ID Tree to determine business partner hierarchy */
    public void setAD_Tree_BPartner_ID (int AD_Tree_BPartner_ID)
    {
        if (AD_Tree_BPartner_ID <= 0) set_Value ("AD_Tree_BPartner_ID", null);
        else
        set_Value ("AD_Tree_BPartner_ID", Integer.valueOf(AD_Tree_BPartner_ID));
        
    }
    
    /** Get BPartner Tree.
    @return Tree to determine business partner hierarchy */
    public int getAD_Tree_BPartner_ID() 
    {
        return get_ValueAsInt("AD_Tree_BPartner_ID");
        
    }
    
    /** Set Campaign Tree.
    @param AD_Tree_Campaign_ID Tree to determine marketing campaign hierarchy */
    public void setAD_Tree_Campaign_ID (int AD_Tree_Campaign_ID)
    {
        if (AD_Tree_Campaign_ID <= 0) set_Value ("AD_Tree_Campaign_ID", null);
        else
        set_Value ("AD_Tree_Campaign_ID", Integer.valueOf(AD_Tree_Campaign_ID));
        
    }
    
    /** Get Campaign Tree.
    @return Tree to determine marketing campaign hierarchy */
    public int getAD_Tree_Campaign_ID() 
    {
        return get_ValueAsInt("AD_Tree_Campaign_ID");
        
    }
    
    /** Set Menu Tree.
    @param AD_Tree_Menu_ID Tree of the menu */
    public void setAD_Tree_Menu_ID (int AD_Tree_Menu_ID)
    {
        if (AD_Tree_Menu_ID <= 0) set_Value ("AD_Tree_Menu_ID", null);
        else
        set_Value ("AD_Tree_Menu_ID", Integer.valueOf(AD_Tree_Menu_ID));
        
    }
    
    /** Get Menu Tree.
    @return Tree of the menu */
    public int getAD_Tree_Menu_ID() 
    {
        return get_ValueAsInt("AD_Tree_Menu_ID");
        
    }
    
    /** Set Organization Tree.
    @param AD_Tree_Org_ID Tree to determine organizational hierarchy */
    public void setAD_Tree_Org_ID (int AD_Tree_Org_ID)
    {
        if (AD_Tree_Org_ID <= 0) set_Value ("AD_Tree_Org_ID", null);
        else
        set_Value ("AD_Tree_Org_ID", Integer.valueOf(AD_Tree_Org_ID));
        
    }
    
    /** Get Organization Tree.
    @return Tree to determine organizational hierarchy */
    public int getAD_Tree_Org_ID() 
    {
        return get_ValueAsInt("AD_Tree_Org_ID");
        
    }
    
    /** Set Product Tree.
    @param AD_Tree_Product_ID Tree to determine product hierarchy */
    public void setAD_Tree_Product_ID (int AD_Tree_Product_ID)
    {
        if (AD_Tree_Product_ID < 1) throw new IllegalArgumentException ("AD_Tree_Product_ID is mandatory.");
        set_Value ("AD_Tree_Product_ID", Integer.valueOf(AD_Tree_Product_ID));
        
    }
    
    /** Get Product Tree.
    @return Tree to determine product hierarchy */
    public int getAD_Tree_Product_ID() 
    {
        return get_ValueAsInt("AD_Tree_Product_ID");
        
    }
    
    /** Set Project Tree.
    @param AD_Tree_Project_ID Tree to determine project hierarchy */
    public void setAD_Tree_Project_ID (int AD_Tree_Project_ID)
    {
        if (AD_Tree_Project_ID <= 0) set_Value ("AD_Tree_Project_ID", null);
        else
        set_Value ("AD_Tree_Project_ID", Integer.valueOf(AD_Tree_Project_ID));
        
    }
    
    /** Get Project Tree.
    @return Tree to determine project hierarchy */
    public int getAD_Tree_Project_ID() 
    {
        return get_ValueAsInt("AD_Tree_Project_ID");
        
    }
    
    /** Set Sales Region Tree.
    @param AD_Tree_SalesRegion_ID Tree to determine sales regional hierarchy */
    public void setAD_Tree_SalesRegion_ID (int AD_Tree_SalesRegion_ID)
    {
        if (AD_Tree_SalesRegion_ID <= 0) set_Value ("AD_Tree_SalesRegion_ID", null);
        else
        set_Value ("AD_Tree_SalesRegion_ID", Integer.valueOf(AD_Tree_SalesRegion_ID));
        
    }
    
    /** Get Sales Region Tree.
    @return Tree to determine sales regional hierarchy */
    public int getAD_Tree_SalesRegion_ID() 
    {
        return get_ValueAsInt("AD_Tree_SalesRegion_ID");
        
    }
    
    /** Set Bank Verification Class.
    @param BankVerificationClass Bank Data Verification Class */
    public void setBankVerificationClass (String BankVerificationClass)
    {
        set_Value ("BankVerificationClass", BankVerificationClass);
        
    }
    
    /** Get Bank Verification Class.
    @return Bank Data Verification Class */
    public String getBankVerificationClass() 
    {
        return (String)get_Value("BankVerificationClass");
        
    }
    
    /** Set Primary Accounting Schema.
    @param C_AcctSchema1_ID Primary rules for accounting */
    public void setC_AcctSchema1_ID (int C_AcctSchema1_ID)
    {
        if (C_AcctSchema1_ID <= 0) set_ValueNoCheck ("C_AcctSchema1_ID", null);
        else
        set_ValueNoCheck ("C_AcctSchema1_ID", Integer.valueOf(C_AcctSchema1_ID));
        
    }
    
    /** Get Primary Accounting Schema.
    @return Primary rules for accounting */
    public int getC_AcctSchema1_ID() 
    {
        return get_ValueAsInt("C_AcctSchema1_ID");
        
    }
    
    /** Set Template B.Partner.
    @param C_BPartnerCashTrx_ID Business Partner used for creating new Business Partners on the fly */
    public void setC_BPartnerCashTrx_ID (int C_BPartnerCashTrx_ID)
    {
        if (C_BPartnerCashTrx_ID <= 0) set_Value ("C_BPartnerCashTrx_ID", null);
        else
        set_Value ("C_BPartnerCashTrx_ID", Integer.valueOf(C_BPartnerCashTrx_ID));
        
    }
    
    /** Get Template B.Partner.
    @return Business Partner used for creating new Business Partners on the fly */
    public int getC_BPartnerCashTrx_ID() 
    {
        return get_ValueAsInt("C_BPartnerCashTrx_ID");
        
    }
    
    /** Set Calendar.
    @param C_Calendar_ID Accounting Calendar Name */
    public void setC_Calendar_ID (int C_Calendar_ID)
    {
        if (C_Calendar_ID <= 0) set_Value ("C_Calendar_ID", null);
        else
        set_Value ("C_Calendar_ID", Integer.valueOf(C_Calendar_ID));
        
    }
    
    /** Get Calendar.
    @return Accounting Calendar Name */
    public int getC_Calendar_ID() 
    {
        return get_ValueAsInt("C_Calendar_ID");
        
    }
    
    /** Set UOM for Length.
    @param C_UOM_Length_ID Standard Unit of Measure for Length */
    public void setC_UOM_Length_ID (int C_UOM_Length_ID)
    {
        if (C_UOM_Length_ID <= 0) set_Value ("C_UOM_Length_ID", null);
        else
        set_Value ("C_UOM_Length_ID", Integer.valueOf(C_UOM_Length_ID));
        
    }
    
    /** Get UOM for Length.
    @return Standard Unit of Measure for Length */
    public int getC_UOM_Length_ID() 
    {
        return get_ValueAsInt("C_UOM_Length_ID");
        
    }
    
    /** Set UOM for Time.
    @param C_UOM_Time_ID Standard Unit of Measure for Time */
    public void setC_UOM_Time_ID (int C_UOM_Time_ID)
    {
        if (C_UOM_Time_ID <= 0) set_Value ("C_UOM_Time_ID", null);
        else
        set_Value ("C_UOM_Time_ID", Integer.valueOf(C_UOM_Time_ID));
        
    }
    
    /** Get UOM for Time.
    @return Standard Unit of Measure for Time */
    public int getC_UOM_Time_ID() 
    {
        return get_ValueAsInt("C_UOM_Time_ID");
        
    }
    
    /** Set UOM for Volume.
    @param C_UOM_Volume_ID Standard Unit of Measure for Volume */
    public void setC_UOM_Volume_ID (int C_UOM_Volume_ID)
    {
        if (C_UOM_Volume_ID <= 0) set_Value ("C_UOM_Volume_ID", null);
        else
        set_Value ("C_UOM_Volume_ID", Integer.valueOf(C_UOM_Volume_ID));
        
    }
    
    /** Get UOM for Volume.
    @return Standard Unit of Measure for Volume */
    public int getC_UOM_Volume_ID() 
    {
        return get_ValueAsInt("C_UOM_Volume_ID");
        
    }
    
    /** Set UOM for Weight.
    @param C_UOM_Weight_ID Standard Unit of Measure for Weight */
    public void setC_UOM_Weight_ID (int C_UOM_Weight_ID)
    {
        if (C_UOM_Weight_ID <= 0) set_Value ("C_UOM_Weight_ID", null);
        else
        set_Value ("C_UOM_Weight_ID", Integer.valueOf(C_UOM_Weight_ID));
        
    }
    
    /** Get UOM for Weight.
    @return Standard Unit of Measure for Weight */
    public int getC_UOM_Weight_ID() 
    {
        return get_ValueAsInt("C_UOM_Weight_ID");
        
    }
    
    /** Set Discount calculated from Line Amounts.
    @param IsDiscountLineAmt Payment Discount calculation does not include Taxes and Charges */
    public void setIsDiscountLineAmt (boolean IsDiscountLineAmt)
    {
        set_Value ("IsDiscountLineAmt", Boolean.valueOf(IsDiscountLineAmt));
        
    }
    
    /** Get Discount calculated from Line Amounts.
    @return Payment Discount calculation does not include Taxes and Charges */
    public boolean isDiscountLineAmt() 
    {
        return get_ValueAsBoolean("IsDiscountLineAmt");
        
    }
    
    /** Set Days to keep Log.
    @param KeepLogDays Number of days to keep the log entries */
    public void setKeepLogDays (int KeepLogDays)
    {
        set_Value ("KeepLogDays", Integer.valueOf(KeepLogDays));
        
    }
    
    /** Get Days to keep Log.
    @return Number of days to keep the log entries */
    public int getKeepLogDays() 
    {
        return get_ValueAsInt("KeepLogDays");
        
    }
    
    /** Set Product for Freight.
    @param M_ProductFreight_ID Product for Freight */
    public void setM_ProductFreight_ID (int M_ProductFreight_ID)
    {
        if (M_ProductFreight_ID <= 0) set_Value ("M_ProductFreight_ID", null);
        else
        set_Value ("M_ProductFreight_ID", Integer.valueOf(M_ProductFreight_ID));
        
    }
    
    /** Get Product for Freight.
    @return Product for Freight */
    public int getM_ProductFreight_ID() 
    {
        return get_ValueAsInt("M_ProductFreight_ID");
        
    }
    
    /** Purchase Order and Receipt = B */
    public static final String MATCHREQUIREMENTI_PurchaseOrderAndReceipt = X_Ref_C_Client_Invoice_MatchRequirement.PURCHASE_ORDER_AND_RECEIPT.getValue();
    /** None = N */
    public static final String MATCHREQUIREMENTI_None = X_Ref_C_Client_Invoice_MatchRequirement.NONE.getValue();
    /** Purchase Order = P */
    public static final String MATCHREQUIREMENTI_PurchaseOrder = X_Ref_C_Client_Invoice_MatchRequirement.PURCHASE_ORDER.getValue();
    /** Receipt = R */
    public static final String MATCHREQUIREMENTI_Receipt = X_Ref_C_Client_Invoice_MatchRequirement.RECEIPT.getValue();
    /** Set Invoice Match Requirement.
    @param MatchRequirementI Matching Requirement for Invoice */
    public void setMatchRequirementI (String MatchRequirementI)
    {
        if (!X_Ref_C_Client_Invoice_MatchRequirement.isValid(MatchRequirementI))
        throw new IllegalArgumentException ("MatchRequirementI Invalid value - " + MatchRequirementI + " - Reference_ID=360 - B - N - P - R");
        set_Value ("MatchRequirementI", MatchRequirementI);
        
    }
    
    /** Get Invoice Match Requirement.
    @return Matching Requirement for Invoice */
    public String getMatchRequirementI() 
    {
        return (String)get_Value("MatchRequirementI");
        
    }
    
    /** Purchase Order and Invoice = B */
    public static final String MATCHREQUIREMENTR_PurchaseOrderAndInvoice = X_Ref_C_Client_Receipt_MatchRequirement.PURCHASE_ORDER_AND_INVOICE.getValue();
    /** Invoice = I */
    public static final String MATCHREQUIREMENTR_Invoice = X_Ref_C_Client_Receipt_MatchRequirement.INVOICE.getValue();
    /** None = N */
    public static final String MATCHREQUIREMENTR_None = X_Ref_C_Client_Receipt_MatchRequirement.NONE.getValue();
    /** Purchase Order = P */
    public static final String MATCHREQUIREMENTR_PurchaseOrder = X_Ref_C_Client_Receipt_MatchRequirement.PURCHASE_ORDER.getValue();
    /** Set Receipt Match Requirement.
    @param MatchRequirementR Matching Requirement for Receipts */
    public void setMatchRequirementR (String MatchRequirementR)
    {
        if (!X_Ref_C_Client_Receipt_MatchRequirement.isValid(MatchRequirementR))
        throw new IllegalArgumentException ("MatchRequirementR Invalid value - " + MatchRequirementR + " - Reference_ID=410 - B - I - N - P");
        set_Value ("MatchRequirementR", MatchRequirementR);
        
    }
    
    /** Get Receipt Match Requirement.
    @return Matching Requirement for Receipts */
    public String getMatchRequirementR() 
    {
        return (String)get_Value("MatchRequirementR");
        
    }
    
    /** Set Pricing Engine Class.
    @param PricingEngineClass Class used for calculating Prices */
    public void setPricingEngineClass (String PricingEngineClass)
    {
        set_Value ("PricingEngineClass", PricingEngineClass);
        
    }
    
    /** Get Pricing Engine Class.
    @return Class used for calculating Prices */
    public String getPricingEngineClass() 
    {
        return (String)get_Value("PricingEngineClass");
        
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
    
    
}
