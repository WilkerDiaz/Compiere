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
/** Generated Model for C_Tax
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.2 Dev - $Id: X_C_Tax.java 8472 2010-02-19 23:14:11Z nnayak $ */
public class X_C_Tax extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Tax_ID id
    @param trx transaction
    */
    public X_C_Tax (Ctx ctx, int C_Tax_ID, Trx trx)
    {
        super (ctx, C_Tax_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Tax_ID == 0)
        {
            setC_TaxCategory_ID (0);
            setC_Tax_ID (0);
            setIsDefault (false);
            setIsDocumentLevel (false);
            setIsSalesTax (false);	// N
            setIsSummary (false);
            setIsTaxExempt (false);
            setIsUseShippingAddress (false);	// N
            setName (null);
            setRate (Env.ZERO);
            setRequiresTaxCertificate (false);
            setSOPOType (null);	// B
            setValidFrom (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Tax (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27548747303789L;
    /** Last Updated Timestamp 2010-02-19 15:26:27.0 */
    public static final long updatedMS = 1266621987000L;
    /** AD_Table_ID=261 */
    public static final int Table_ID=261;
    
    /** TableName=C_Tax */
    public static final String Table_Name="C_Tax";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Country.
    @param C_Country_ID Country */
    public void setC_Country_ID (int C_Country_ID)
    {
        if (C_Country_ID <= 0) set_Value ("C_Country_ID", null);
        else
        set_Value ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
    }
    
    /** Set Region.
    @param C_Region_ID Identifies a geographical Region */
    public void setC_Region_ID (int C_Region_ID)
    {
        if (C_Region_ID <= 0) set_Value ("C_Region_ID", null);
        else
        set_Value ("C_Region_ID", Integer.valueOf(C_Region_ID));
        
    }
    
    /** Get Region.
    @return Identifies a geographical Region */
    public int getC_Region_ID() 
    {
        return get_ValueAsInt("C_Region_ID");
        
    }
    
    /** Set Tax Category.
    @param C_TaxCategory_ID Tax Category */
    public void setC_TaxCategory_ID (int C_TaxCategory_ID)
    {
        if (C_TaxCategory_ID < 1) throw new IllegalArgumentException ("C_TaxCategory_ID is mandatory.");
        set_Value ("C_TaxCategory_ID", Integer.valueOf(C_TaxCategory_ID));
        
    }
    
    /** Get Tax Category.
    @return Tax Category */
    public int getC_TaxCategory_ID() 
    {
        return get_ValueAsInt("C_TaxCategory_ID");
        
    }
    
    /** Set Tax.
    @param C_Tax_ID Tax identifier */
    public void setC_Tax_ID (int C_Tax_ID)
    {
        if (C_Tax_ID < 1) throw new IllegalArgumentException ("C_Tax_ID is mandatory.");
        set_ValueNoCheck ("C_Tax_ID", Integer.valueOf(C_Tax_ID));
        
    }
    
    /** Get Tax.
    @return Tax identifier */
    public int getC_Tax_ID() 
    {
        return get_ValueAsInt("C_Tax_ID");
        
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
    
    /** Set Document Level.
    @param IsDocumentLevel Tax is calculated on document level (rather than line by line) */
    public void setIsDocumentLevel (boolean IsDocumentLevel)
    {
        set_Value ("IsDocumentLevel", Boolean.valueOf(IsDocumentLevel));
        
    }
    
    /** Get Document Level.
    @return Tax is calculated on document level (rather than line by line) */
    public boolean isDocumentLevel() 
    {
        return get_ValueAsBoolean("IsDocumentLevel");
        
    }
    
    /** Set Sales Tax.
    @param IsSalesTax This is a sales tax (i.e. not a value added tax) */
    public void setIsSalesTax (boolean IsSalesTax)
    {
        set_Value ("IsSalesTax", Boolean.valueOf(IsSalesTax));
        
    }
    
    /** Get Sales Tax.
    @return This is a sales tax (i.e. not a value added tax) */
    public boolean isSalesTax() 
    {
        return get_ValueAsBoolean("IsSalesTax");
        
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
    
    /** Set Tax exempt.
    @param IsTaxExempt Business partner is exempt from tax */
    public void setIsTaxExempt (boolean IsTaxExempt)
    {
        set_Value ("IsTaxExempt", Boolean.valueOf(IsTaxExempt));
        
    }
    
    /** Get Tax exempt.
    @return Business partner is exempt from tax */
    public boolean isTaxExempt() 
    {
        return get_ValueAsBoolean("IsTaxExempt");
        
    }
    
    /** Set Use Shipping Address.
    @param IsUseShippingAddress Use shipping address as basis for tax defaulting */
    public void setIsUseShippingAddress (boolean IsUseShippingAddress)
    {
        set_Value ("IsUseShippingAddress", Boolean.valueOf(IsUseShippingAddress));
        
    }
    
    /** Get Use Shipping Address.
    @return Use shipping address as basis for tax defaulting */
    public boolean isUseShippingAddress() 
    {
        return get_ValueAsBoolean("IsUseShippingAddress");
        
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
    
    /** Set Parent Tax.
    @param Parent_Tax_ID Parent Tax indicates a tax that is made up of multiple taxes */
    public void setParent_Tax_ID (int Parent_Tax_ID)
    {
        if (Parent_Tax_ID <= 0) set_Value ("Parent_Tax_ID", null);
        else
        set_Value ("Parent_Tax_ID", Integer.valueOf(Parent_Tax_ID));
        
    }
    
    /** Get Parent Tax.
    @return Parent Tax indicates a tax that is made up of multiple taxes */
    public int getParent_Tax_ID() 
    {
        return get_ValueAsInt("Parent_Tax_ID");
        
    }
    
    /** Set Rate.
    @param Rate Rate or Tax or Exchange */
    public void setRate (java.math.BigDecimal Rate)
    {
        if (Rate == null) throw new IllegalArgumentException ("Rate is mandatory.");
        set_Value ("Rate", Rate);
        
    }
    
    /** Get Rate.
    @return Rate or Tax or Exchange */
    public java.math.BigDecimal getRate() 
    {
        return get_ValueAsBigDecimal("Rate");
        
    }
    
    /** Set Requires Tax Certificate.
    @param RequiresTaxCertificate This tax rate requires the Business Partner to be tax exempt */
    public void setRequiresTaxCertificate (boolean RequiresTaxCertificate)
    {
        set_Value ("RequiresTaxCertificate", Boolean.valueOf(RequiresTaxCertificate));
        
    }
    
    /** Get Requires Tax Certificate.
    @return This tax rate requires the Business Partner to be tax exempt */
    public boolean isRequiresTaxCertificate() 
    {
        return get_ValueAsBoolean("RequiresTaxCertificate");
        
    }
    
    /** Both = B */
    public static final String SOPOTYPE_Both = X_Ref_C_Tax_SPPOType.BOTH.getValue();
    /** Purchase Tax = P */
    public static final String SOPOTYPE_PurchaseTax = X_Ref_C_Tax_SPPOType.PURCHASE_TAX.getValue();
    /** Sales Tax = S */
    public static final String SOPOTYPE_SalesTax = X_Ref_C_Tax_SPPOType.SALES_TAX.getValue();
    /** Set SO/PO Type.
    @param SOPOType Sales Tax applies to sales situations, Purchase Tax to purchase situations */
    public void setSOPOType (String SOPOType)
    {
        if (SOPOType == null) throw new IllegalArgumentException ("SOPOType is mandatory");
        if (!X_Ref_C_Tax_SPPOType.isValid(SOPOType))
        throw new IllegalArgumentException ("SOPOType Invalid value - " + SOPOType + " - Reference_ID=287 - B - P - S");
        set_Value ("SOPOType", SOPOType);
        
    }
    
    /** Get SO/PO Type.
    @return Sales Tax applies to sales situations, Purchase Tax to purchase situations */
    public String getSOPOType() 
    {
        return (String)get_Value("SOPOType");
        
    }
    
    /** Set Tax Indicator.
    @param TaxIndicator Short form for Tax to be printed on documents */
    public void setTaxIndicator (String TaxIndicator)
    {
        set_Value ("TaxIndicator", TaxIndicator);
        
    }
    
    /** Get Tax Indicator.
    @return Short form for Tax to be printed on documents */
    public String getTaxIndicator() 
    {
        return (String)get_Value("TaxIndicator");
        
    }
    
    /** Set To.
    @param To_Country_ID Receiving Country */
    public void setTo_Country_ID (int To_Country_ID)
    {
        if (To_Country_ID <= 0) set_Value ("To_Country_ID", null);
        else
        set_Value ("To_Country_ID", Integer.valueOf(To_Country_ID));
        
    }
    
    /** Get To.
    @return Receiving Country */
    public int getTo_Country_ID() 
    {
        return get_ValueAsInt("To_Country_ID");
        
    }
    
    /** Set To.
    @param To_Region_ID Receiving Region */
    public void setTo_Region_ID (int To_Region_ID)
    {
        if (To_Region_ID <= 0) set_Value ("To_Region_ID", null);
        else
        set_Value ("To_Region_ID", Integer.valueOf(To_Region_ID));
        
    }
    
    /** Get To.
    @return Receiving Region */
    public int getTo_Region_ID() 
    {
        return get_ValueAsInt("To_Region_ID");
        
    }
    
    /** Set Valid from.
    @param ValidFrom Valid from including this date (first day) */
    public void setValidFrom (Timestamp ValidFrom)
    {
        if (ValidFrom == null) throw new IllegalArgumentException ("ValidFrom is mandatory.");
        set_Value ("ValidFrom", ValidFrom);
        
    }
    
    /** Get Valid from.
    @return Valid from including this date (first day) */
    public Timestamp getValidFrom() 
    {
        return (Timestamp)get_Value("ValidFrom");
        
    }
    
    
}
