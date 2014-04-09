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
/** Generated Model for C_Withholding
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Withholding.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Withholding extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Withholding_ID id
    @param trx transaction
    */
    public X_C_Withholding (Ctx ctx, int C_Withholding_ID, Trx trx)
    {
        super (ctx, C_Withholding_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Withholding_ID == 0)
        {
            setC_PaymentTerm_ID (0);
            setC_Withholding_ID (0);
            setIsPaidTo3Party (false);
            setIsPercentWithholding (false);
            setIsTaxProrated (false);
            setIsTaxWithholding (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Withholding (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=304 */
    public static final int Table_ID=304;
    
    /** TableName=C_Withholding */
    public static final String Table_Name="C_Withholding";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Beneficiary.
    @param Beneficiary_ID Business Partner to whom payment is made */
    public void setBeneficiary_ID (int Beneficiary_ID)
    {
        if (Beneficiary_ID <= 0) set_Value ("Beneficiary_ID", null);
        else
        set_Value ("Beneficiary_ID", Integer.valueOf(Beneficiary_ID));
        
    }
    
    /** Get Beneficiary.
    @return Business Partner to whom payment is made */
    public int getBeneficiary_ID() 
    {
        return get_ValueAsInt("Beneficiary_ID");
        
    }
    
    /** Set Payment Term.
    @param C_PaymentTerm_ID The terms of Payment (timing, discount) */
    public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
    {
        if (C_PaymentTerm_ID < 1) throw new IllegalArgumentException ("C_PaymentTerm_ID is mandatory.");
        set_Value ("C_PaymentTerm_ID", Integer.valueOf(C_PaymentTerm_ID));
        
    }
    
    /** Get Payment Term.
    @return The terms of Payment (timing, discount) */
    public int getC_PaymentTerm_ID() 
    {
        return get_ValueAsInt("C_PaymentTerm_ID");
        
    }
    
    /** Set Withholding.
    @param C_Withholding_ID Withholding type defined */
    public void setC_Withholding_ID (int C_Withholding_ID)
    {
        if (C_Withholding_ID < 1) throw new IllegalArgumentException ("C_Withholding_ID is mandatory.");
        set_ValueNoCheck ("C_Withholding_ID", Integer.valueOf(C_Withholding_ID));
        
    }
    
    /** Get Withholding.
    @return Withholding type defined */
    public int getC_Withholding_ID() 
    {
        return get_ValueAsInt("C_Withholding_ID");
        
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
    
    /** Set Fix amount.
    @param FixAmt Fixed amount to be levied or paid */
    public void setFixAmt (java.math.BigDecimal FixAmt)
    {
        set_Value ("FixAmt", FixAmt);
        
    }
    
    /** Get Fix amount.
    @return Fixed amount to be levied or paid */
    public java.math.BigDecimal getFixAmt() 
    {
        return get_ValueAsBigDecimal("FixAmt");
        
    }
    
    /** Set Paid to third party.
    @param IsPaidTo3Party Amount paid to someone other than the Business Partner */
    public void setIsPaidTo3Party (boolean IsPaidTo3Party)
    {
        set_Value ("IsPaidTo3Party", Boolean.valueOf(IsPaidTo3Party));
        
    }
    
    /** Get Paid to third party.
    @return Amount paid to someone other than the Business Partner */
    public boolean isPaidTo3Party() 
    {
        return get_ValueAsBoolean("IsPaidTo3Party");
        
    }
    
    /** Set Percent withholding.
    @param IsPercentWithholding Withholding amount is a percentage of the invoice amount */
    public void setIsPercentWithholding (boolean IsPercentWithholding)
    {
        set_Value ("IsPercentWithholding", Boolean.valueOf(IsPercentWithholding));
        
    }
    
    /** Get Percent withholding.
    @return Withholding amount is a percentage of the invoice amount */
    public boolean isPercentWithholding() 
    {
        return get_ValueAsBoolean("IsPercentWithholding");
        
    }
    
    /** Set Prorate tax.
    @param IsTaxProrated Tax is Prorated */
    public void setIsTaxProrated (boolean IsTaxProrated)
    {
        set_Value ("IsTaxProrated", Boolean.valueOf(IsTaxProrated));
        
    }
    
    /** Get Prorate tax.
    @return Tax is Prorated */
    public boolean isTaxProrated() 
    {
        return get_ValueAsBoolean("IsTaxProrated");
        
    }
    
    /** Set Tax withholding.
    @param IsTaxWithholding This is a tax related withholding */
    public void setIsTaxWithholding (boolean IsTaxWithholding)
    {
        set_Value ("IsTaxWithholding", Boolean.valueOf(IsTaxWithholding));
        
    }
    
    /** Get Tax withholding.
    @return This is a tax related withholding */
    public boolean isTaxWithholding() 
    {
        return get_ValueAsBoolean("IsTaxWithholding");
        
    }
    
    /** Set Max Amount.
    @param MaxAmt Maximum Amount in invoice currency */
    public void setMaxAmt (java.math.BigDecimal MaxAmt)
    {
        set_Value ("MaxAmt", MaxAmt);
        
    }
    
    /** Get Max Amount.
    @return Maximum Amount in invoice currency */
    public java.math.BigDecimal getMaxAmt() 
    {
        return get_ValueAsBigDecimal("MaxAmt");
        
    }
    
    /** Set Min Amount.
    @param MinAmt Minimum Amount in invoice currency */
    public void setMinAmt (java.math.BigDecimal MinAmt)
    {
        set_Value ("MinAmt", MinAmt);
        
    }
    
    /** Get Min Amount.
    @return Minimum Amount in invoice currency */
    public java.math.BigDecimal getMinAmt() 
    {
        return get_ValueAsBigDecimal("MinAmt");
        
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
    
    /** Set Percent.
    @param PercentWithholding Percent withholding */
    public void setPercentWithholding (java.math.BigDecimal PercentWithholding)
    {
        set_Value ("PercentWithholding", PercentWithholding);
        
    }
    
    /** Get Percent.
    @return Percent withholding */
    public java.math.BigDecimal getPercentWithholding() 
    {
        return get_ValueAsBigDecimal("PercentWithholding");
        
    }
    
    /** Set Threshold max.
    @param ThresholdMax Maximum gross amount for withholding calculation (0=no limit) */
    public void setThresholdMax (java.math.BigDecimal ThresholdMax)
    {
        set_Value ("ThresholdMax", ThresholdMax);
        
    }
    
    /** Get Threshold max.
    @return Maximum gross amount for withholding calculation (0=no limit) */
    public java.math.BigDecimal getThresholdMax() 
    {
        return get_ValueAsBigDecimal("ThresholdMax");
        
    }
    
    /** Set Threshold min.
    @param Thresholdmin Minimum gross amount for withholding calculation */
    public void setThresholdmin (java.math.BigDecimal Thresholdmin)
    {
        set_Value ("Thresholdmin", Thresholdmin);
        
    }
    
    /** Get Threshold min.
    @return Minimum gross amount for withholding calculation */
    public java.math.BigDecimal getThresholdmin() 
    {
        return get_ValueAsBigDecimal("Thresholdmin");
        
    }
    
    
}
