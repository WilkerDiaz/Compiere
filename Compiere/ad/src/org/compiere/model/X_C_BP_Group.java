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
/** Generated Model for C_BP_Group
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_BP_Group.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_BP_Group extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BP_Group_ID id
    @param trx transaction
    */
    public X_C_BP_Group (Ctx ctx, int C_BP_Group_ID, Trx trx)
    {
        super (ctx, C_BP_Group_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BP_Group_ID == 0)
        {
            setC_BP_Group_ID (0);
            setIsConfidentialInfo (false);	// N
            setIsDefault (false);
            setName (null);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BP_Group (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27496372212789L;
    /** Last Updated Timestamp 2008-06-23 11:48:16.0 */
    public static final long updatedMS = 1214246896000L;
    /** AD_Table_ID=394 */
    public static final int Table_ID=394;
    
    /** TableName=C_BP_Group */
    public static final String Table_Name="C_BP_Group";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Print Color.
    @param AD_PrintColor_ID Color used for printing and display */
    public void setAD_PrintColor_ID (int AD_PrintColor_ID)
    {
        if (AD_PrintColor_ID <= 0) set_Value ("AD_PrintColor_ID", null);
        else
        set_Value ("AD_PrintColor_ID", Integer.valueOf(AD_PrintColor_ID));
        
    }
    
    /** Get Print Color.
    @return Color used for printing and display */
    public int getAD_PrintColor_ID() 
    {
        return get_ValueAsInt("AD_PrintColor_ID");
        
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
    
    /** Set Credit Watch %.
    @param CreditWatchPercent Credit Watch - Percent of Credit Limit when OK switches to Watch */
    public void setCreditWatchPercent (java.math.BigDecimal CreditWatchPercent)
    {
        set_Value ("CreditWatchPercent", CreditWatchPercent);
        
    }
    
    /** Get Credit Watch %.
    @return Credit Watch - Percent of Credit Limit when OK switches to Watch */
    public java.math.BigDecimal getCreditWatchPercent() 
    {
        return get_ValueAsBigDecimal("CreditWatchPercent");
        
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
    
    /** Set Confidential Info.
    @param IsConfidentialInfo Can enter confidential information */
    public void setIsConfidentialInfo (boolean IsConfidentialInfo)
    {
        set_Value ("IsConfidentialInfo", Boolean.valueOf(IsConfidentialInfo));
        
    }
    
    /** Get Confidential Info.
    @return Can enter confidential information */
    public boolean isConfidentialInfo() 
    {
        return get_ValueAsBoolean("IsConfidentialInfo");
        
    }
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
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
    
    /** Set Price Match Tolerance.
    @param PriceMatchTolerance PO-Invoice Match Price Tolerance in percent of the purchase price */
    public void setPriceMatchTolerance (java.math.BigDecimal PriceMatchTolerance)
    {
        set_Value ("PriceMatchTolerance", PriceMatchTolerance);
        
    }
    
    /** Get Price Match Tolerance.
    @return PO-Invoice Match Price Tolerance in percent of the purchase price */
    public java.math.BigDecimal getPriceMatchTolerance() 
    {
        return get_ValueAsBigDecimal("PriceMatchTolerance");
        
    }
    
    /** Higher = H */
    public static final String PRIORITYBASE_Higher = X_Ref_C_BP_Group_PriorityBase.HIGHER.getValue();
    /** Lower = L */
    public static final String PRIORITYBASE_Lower = X_Ref_C_BP_Group_PriorityBase.LOWER.getValue();
    /** Same = S */
    public static final String PRIORITYBASE_Same = X_Ref_C_BP_Group_PriorityBase.SAME.getValue();
    /** Set Priority Base.
    @param PriorityBase Base of Priority */
    public void setPriorityBase (String PriorityBase)
    {
        if (!X_Ref_C_BP_Group_PriorityBase.isValid(PriorityBase))
        throw new IllegalArgumentException ("PriorityBase Invalid value - " + PriorityBase + " - Reference_ID=350 - H - L - S");
        set_Value ("PriorityBase", PriorityBase);
        
    }
    
    /** Get Priority Base.
    @return Base of Priority */
    public String getPriorityBase() 
    {
        return (String)get_Value("PriorityBase");
        
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
