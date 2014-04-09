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
/** Generated Model for C_AcctSchema
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_AcctSchema.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_AcctSchema extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_AcctSchema_ID id
    @param trx transaction
    */
    public X_C_AcctSchema (Ctx ctx, int C_AcctSchema_ID, Trx trx)
    {
        super (ctx, C_AcctSchema_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_AcctSchema_ID == 0)
        {
            setAutoPeriodControl (false);
            setC_AcctSchema_ID (0);
            setC_Currency_ID (0);
            setCommitmentType (null);	// N
            setCostingLevel (null);	// C
            setCostingMethod (null);	// S
            setGAAP (null);
            setHasAlias (false);
            setHasCombination (false);
            setIsAccrual (true);	// Y
            setIsAdjustCOGS (false);
            setIsDiscountCorrectsTax (false);
            setIsExplicitCostAdjustment (false);	// N
            setIsPostServices (false);	// N
            setIsTradeDiscountPosted (false);
            setM_CostType_ID (0);
            setName (null);
            setSeparator (null);	// -
            setTaxCorrectionType (null);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_AcctSchema (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=265 */
    public static final int Table_ID=265;
    
    /** TableName=C_AcctSchema */
    public static final String Table_Name="C_AcctSchema";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Only Organization.
    @param AD_OrgOnly_ID Create posting entries only for this organization */
    public void setAD_OrgOnly_ID (int AD_OrgOnly_ID)
    {
        if (AD_OrgOnly_ID <= 0) set_Value ("AD_OrgOnly_ID", null);
        else
        set_Value ("AD_OrgOnly_ID", Integer.valueOf(AD_OrgOnly_ID));
        
    }
    
    /** Get Only Organization.
    @return Create posting entries only for this organization */
    public int getAD_OrgOnly_ID() 
    {
        return get_ValueAsInt("AD_OrgOnly_ID");
        
    }
    
    /** Set Automatic Period Control.
    @param AutoPeriodControl If selected, the periods are automatically opened and closed */
    public void setAutoPeriodControl (boolean AutoPeriodControl)
    {
        set_Value ("AutoPeriodControl", Boolean.valueOf(AutoPeriodControl));
        
    }
    
    /** Get Automatic Period Control.
    @return If selected, the periods are automatically opened and closed */
    public boolean isAutoPeriodControl() 
    {
        return get_ValueAsBoolean("AutoPeriodControl");
        
    }
    
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
        set_ValueNoCheck ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Period.
    @param C_Period_ID Period of the Calendar */
    public void setC_Period_ID (int C_Period_ID)
    {
        if (C_Period_ID <= 0) set_ValueNoCheck ("C_Period_ID", null);
        else
        set_ValueNoCheck ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
    }
    
    /** Get Period.
    @return Period of the Calendar */
    public int getC_Period_ID() 
    {
        return get_ValueAsInt("C_Period_ID");
        
    }
    
    /** Commitment & Reservation = B */
    public static final String COMMITMENTTYPE_CommitmentReservation = X_Ref_C_AcctSchema_CommitmentType.COMMITMENT_RESERVATION.getValue();
    /** Commitment only = C */
    public static final String COMMITMENTTYPE_CommitmentOnly = X_Ref_C_AcctSchema_CommitmentType.COMMITMENT_ONLY.getValue();
    /** None = N */
    public static final String COMMITMENTTYPE_None = X_Ref_C_AcctSchema_CommitmentType.NONE.getValue();
    /** Set Commitment Type.
    @param CommitmentType Create Commitment and/or Reservations for Budget Control */
    public void setCommitmentType (String CommitmentType)
    {
        if (CommitmentType == null) throw new IllegalArgumentException ("CommitmentType is mandatory");
        if (!X_Ref_C_AcctSchema_CommitmentType.isValid(CommitmentType))
        throw new IllegalArgumentException ("CommitmentType Invalid value - " + CommitmentType + " - Reference_ID=359 - B - C - N");
        set_Value ("CommitmentType", CommitmentType);
        
    }
    
    /** Get Commitment Type.
    @return Create Commitment and/or Reservations for Budget Control */
    public String getCommitmentType() 
    {
        return (String)get_Value("CommitmentType");
        
    }
    
    /** Batch/Lot = B */
    public static final String COSTINGLEVEL_BatchLot = X_Ref_C_AcctSchema_CostingLevel.BATCH_LOT.getValue();
    /** Tenant = C */
    public static final String COSTINGLEVEL_Tenant = X_Ref_C_AcctSchema_CostingLevel.TENANT.getValue();
    /** Organization = O */
    public static final String COSTINGLEVEL_Organization = X_Ref_C_AcctSchema_CostingLevel.ORGANIZATION.getValue();
    /** Set Costing Level.
    @param CostingLevel The lowest level to accumulate Costing Information */
    public void setCostingLevel (String CostingLevel)
    {
        if (CostingLevel == null) throw new IllegalArgumentException ("CostingLevel is mandatory");
        if (!X_Ref_C_AcctSchema_CostingLevel.isValid(CostingLevel))
        throw new IllegalArgumentException ("CostingLevel Invalid value - " + CostingLevel + " - Reference_ID=355 - B - C - O");
        set_Value ("CostingLevel", CostingLevel);
        
    }
    
    /** Get Costing Level.
    @return The lowest level to accumulate Costing Information */
    public String getCostingLevel() 
    {
        return (String)get_Value("CostingLevel");
        
    }
    
    /** Average PO = A */
    public static final String COSTINGMETHOD_AveragePO = X_Ref_C_AcctSchema_Costing_Method.AVERAGE_PO.getValue();
    /** FiFo = F */
    public static final String COSTINGMETHOD_FiFo = X_Ref_C_AcctSchema_Costing_Method.FI_FO.getValue();
    /** Average Invoice = I */
    public static final String COSTINGMETHOD_AverageInvoice = X_Ref_C_AcctSchema_Costing_Method.AVERAGE_INVOICE.getValue();
    /** LiFo = L */
    public static final String COSTINGMETHOD_LiFo = X_Ref_C_AcctSchema_Costing_Method.LI_FO.getValue();
    /** Standard Costing = S */
    public static final String COSTINGMETHOD_StandardCosting = X_Ref_C_AcctSchema_Costing_Method.STANDARD_COSTING.getValue();
    /** User Defined = U */
    public static final String COSTINGMETHOD_UserDefined = X_Ref_C_AcctSchema_Costing_Method.USER_DEFINED.getValue();
    /** Last Invoice = i */
    public static final String COSTINGMETHOD_LastInvoice = X_Ref_C_AcctSchema_Costing_Method.LAST_INVOICE.getValue();
    /** Last PO Price = p */
    public static final String COSTINGMETHOD_LastPOPrice = X_Ref_C_AcctSchema_Costing_Method.LAST_PO_PRICE.getValue();
    /** _ = x */
    public static final String COSTINGMETHOD__ = X_Ref_C_AcctSchema_Costing_Method._.getValue();
    /** Set Costing Method.
    @param CostingMethod Indicates how Costs will be calculated */
    public void setCostingMethod (String CostingMethod)
    {
        if (CostingMethod == null) throw new IllegalArgumentException ("CostingMethod is mandatory");
        if (!X_Ref_C_AcctSchema_Costing_Method.isValid(CostingMethod))
        throw new IllegalArgumentException ("CostingMethod Invalid value - " + CostingMethod + " - Reference_ID=122 - A - F - I - L - S - U - i - p - x");
        set_Value ("CostingMethod", CostingMethod);
        
    }
    
    /** Get Costing Method.
    @return Indicates how Costs will be calculated */
    public String getCostingMethod() 
    {
        return (String)get_Value("CostingMethod");
        
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
    
    /** German HGB = DE */
    public static final String GAAP_GermanHGB = X_Ref_C_AcctSchema_GAAP.GERMAN_HGB.getValue();
    /** French Accounting Standard = FR */
    public static final String GAAP_FrenchAccountingStandard = X_Ref_C_AcctSchema_GAAP.FRENCH_ACCOUNTING_STANDARD.getValue();
    /** International GAAP = UN */
    public static final String GAAP_InternationalGAAP = X_Ref_C_AcctSchema_GAAP.INTERNATIONAL_GAAP.getValue();
    /** US GAAP = US */
    public static final String GAAP_USGAAP = X_Ref_C_AcctSchema_GAAP.USGAAP.getValue();
    /** Custom Accounting Rules = XX */
    public static final String GAAP_CustomAccountingRules = X_Ref_C_AcctSchema_GAAP.CUSTOM_ACCOUNTING_RULES.getValue();
    /** Set GAAP.
    @param GAAP Generally Accepted Accounting Principles */
    public void setGAAP (String GAAP)
    {
        if (GAAP == null) throw new IllegalArgumentException ("GAAP is mandatory");
        if (!X_Ref_C_AcctSchema_GAAP.isValid(GAAP))
        throw new IllegalArgumentException ("GAAP Invalid value - " + GAAP + " - Reference_ID=123 - DE - FR - UN - US - XX");
        set_Value ("GAAP", GAAP);
        
    }
    
    /** Get GAAP.
    @return Generally Accepted Accounting Principles */
    public String getGAAP() 
    {
        return (String)get_Value("GAAP");
        
    }
    
    /** Set Use Account Alias.
    @param HasAlias Ability to select (partial) account combinations by an Alias */
    public void setHasAlias (boolean HasAlias)
    {
        set_Value ("HasAlias", Boolean.valueOf(HasAlias));
        
    }
    
    /** Get Use Account Alias.
    @return Ability to select (partial) account combinations by an Alias */
    public boolean isHasAlias() 
    {
        return get_ValueAsBoolean("HasAlias");
        
    }
    
    /** Set Use Account Combination Control.
    @param HasCombination Combination of account elements are checked */
    public void setHasCombination (boolean HasCombination)
    {
        set_Value ("HasCombination", Boolean.valueOf(HasCombination));
        
    }
    
    /** Get Use Account Combination Control.
    @return Combination of account elements are checked */
    public boolean isHasCombination() 
    {
        return get_ValueAsBoolean("HasCombination");
        
    }
    
    /** Set Accrual.
    @param IsAccrual Indicates if Accrual or Cash Based accounting will be used */
    public void setIsAccrual (boolean IsAccrual)
    {
        set_Value ("IsAccrual", Boolean.valueOf(IsAccrual));
        
    }
    
    /** Get Accrual.
    @return Indicates if Accrual or Cash Based accounting will be used */
    public boolean isAccrual() 
    {
        return get_ValueAsBoolean("IsAccrual");
        
    }
    
    /** Set Adjust COGS.
    @param IsAdjustCOGS Adjust Cost of Goods Sold */
    public void setIsAdjustCOGS (boolean IsAdjustCOGS)
    {
        set_Value ("IsAdjustCOGS", Boolean.valueOf(IsAdjustCOGS));
        
    }
    
    /** Get Adjust COGS.
    @return Adjust Cost of Goods Sold */
    public boolean isAdjustCOGS() 
    {
        return get_ValueAsBoolean("IsAdjustCOGS");
        
    }
    
    /** Set Correct tax for Discounts/Charges.
    @param IsDiscountCorrectsTax Correct the tax for payment discount and charges */
    public void setIsDiscountCorrectsTax (boolean IsDiscountCorrectsTax)
    {
        set_Value ("IsDiscountCorrectsTax", Boolean.valueOf(IsDiscountCorrectsTax));
        
    }
    
    /** Get Correct tax for Discounts/Charges.
    @return Correct the tax for payment discount and charges */
    public boolean isDiscountCorrectsTax() 
    {
        return get_ValueAsBoolean("IsDiscountCorrectsTax");
        
    }
    
    /** Set Explicit Cost Adjustment.
    @param IsExplicitCostAdjustment Post the cost adjustment explicitly */
    public void setIsExplicitCostAdjustment (boolean IsExplicitCostAdjustment)
    {
        set_Value ("IsExplicitCostAdjustment", Boolean.valueOf(IsExplicitCostAdjustment));
        
    }
    
    /** Get Explicit Cost Adjustment.
    @return Post the cost adjustment explicitly */
    public boolean isExplicitCostAdjustment() 
    {
        return get_ValueAsBoolean("IsExplicitCostAdjustment");
        
    }
    
    /** Set Post Services Separately.
    @param IsPostServices Differentiate between Services and Product Receivable/Payables */
    public void setIsPostServices (boolean IsPostServices)
    {
        set_Value ("IsPostServices", Boolean.valueOf(IsPostServices));
        
    }
    
    /** Get Post Services Separately.
    @return Differentiate between Services and Product Receivable/Payables */
    public boolean isPostServices() 
    {
        return get_ValueAsBoolean("IsPostServices");
        
    }
    
    /** Set Post Trade Discount.
    @param IsTradeDiscountPosted Generate postings for trade discounts */
    public void setIsTradeDiscountPosted (boolean IsTradeDiscountPosted)
    {
        set_Value ("IsTradeDiscountPosted", Boolean.valueOf(IsTradeDiscountPosted));
        
    }
    
    /** Get Post Trade Discount.
    @return Generate postings for trade discounts */
    public boolean isTradeDiscountPosted() 
    {
        return get_ValueAsBoolean("IsTradeDiscountPosted");
        
    }
    
    /** Set Cost Type.
    @param M_CostType_ID Type of Cost (e.g. Current, Plan, Future) */
    public void setM_CostType_ID (int M_CostType_ID)
    {
        if (M_CostType_ID < 1) throw new IllegalArgumentException ("M_CostType_ID is mandatory.");
        set_Value ("M_CostType_ID", Integer.valueOf(M_CostType_ID));
        
    }
    
    /** Get Cost Type.
    @return Type of Cost (e.g. Current, Plan, Future) */
    public int getM_CostType_ID() 
    {
        return get_ValueAsInt("M_CostType_ID");
        
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
    
    /** Set Future Days.
    @param Period_OpenFuture Number of days to be able to post to a future date (based on system date) */
    public void setPeriod_OpenFuture (int Period_OpenFuture)
    {
        set_Value ("Period_OpenFuture", Integer.valueOf(Period_OpenFuture));
        
    }
    
    /** Get Future Days.
    @return Number of days to be able to post to a future date (based on system date) */
    public int getPeriod_OpenFuture() 
    {
        return get_ValueAsInt("Period_OpenFuture");
        
    }
    
    /** Set History Days.
    @param Period_OpenHistory Number of days to be able to post in the past (based on system date) */
    public void setPeriod_OpenHistory (int Period_OpenHistory)
    {
        set_Value ("Period_OpenHistory", Integer.valueOf(Period_OpenHistory));
        
    }
    
    /** Get History Days.
    @return Number of days to be able to post in the past (based on system date) */
    public int getPeriod_OpenHistory() 
    {
        return get_ValueAsInt("Period_OpenHistory");
        
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
    
    /** Set Element Separator.
    @param Separator Element Separator */
    public void setSeparator (String Separator)
    {
        if (Separator == null) throw new IllegalArgumentException ("Separator is mandatory.");
        set_Value ("Separator", Separator);
        
    }
    
    /** Get Element Separator.
    @return Element Separator */
    public String getSeparator() 
    {
        return (String)get_Value("Separator");
        
    }
    
    /** Write-off and Discount = B */
    public static final String TAXCORRECTIONTYPE_Write_OffAndDiscount = X_Ref_C_AcctSchema_TaxCorrectionType.WRITE__OFF_AND_DISCOUNT.getValue();
    /** Discount only = D */
    public static final String TAXCORRECTIONTYPE_DiscountOnly = X_Ref_C_AcctSchema_TaxCorrectionType.DISCOUNT_ONLY.getValue();
    /** None = N */
    public static final String TAXCORRECTIONTYPE_None = X_Ref_C_AcctSchema_TaxCorrectionType.NONE.getValue();
    /** Write-off only = W */
    public static final String TAXCORRECTIONTYPE_Write_OffOnly = X_Ref_C_AcctSchema_TaxCorrectionType.WRITE__OFF_ONLY.getValue();
    /** Set Tax Correction.
    @param TaxCorrectionType Type of Tax Correction */
    public void setTaxCorrectionType (String TaxCorrectionType)
    {
        if (TaxCorrectionType == null) throw new IllegalArgumentException ("TaxCorrectionType is mandatory");
        if (!X_Ref_C_AcctSchema_TaxCorrectionType.isValid(TaxCorrectionType))
        throw new IllegalArgumentException ("TaxCorrectionType Invalid value - " + TaxCorrectionType + " - Reference_ID=392 - B - D - N - W");
        set_Value ("TaxCorrectionType", TaxCorrectionType);
        
    }
    
    /** Get Tax Correction.
    @return Type of Tax Correction */
    public String getTaxCorrectionType() 
    {
        return (String)get_Value("TaxCorrectionType");
        
    }
    
    
}
