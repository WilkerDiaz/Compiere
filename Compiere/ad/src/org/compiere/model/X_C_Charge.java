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
/** Generated Model for C_Charge
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Charge.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Charge extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Charge_ID id
    @param trx transaction
    */
    public X_C_Charge (Ctx ctx, int C_Charge_ID, Trx trx)
    {
        super (ctx, C_Charge_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Charge_ID == 0)
        {
            setC_Charge_ID (0);
            setC_TaxCategory_ID (0);
            setChargeAmt (Env.ZERO);
            setIsSameCurrency (false);
            setIsSameTax (false);
            setIsTaxIncluded (false);	// N
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Charge (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=313 */
    public static final int Table_ID=313;
    
    /** TableName=C_Charge */
    public static final String Table_Name="C_Charge";
    
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
    
    /** Set Charge.
    @param C_Charge_ID Additional document charges */
    public void setC_Charge_ID (int C_Charge_ID)
    {
        if (C_Charge_ID < 1) throw new IllegalArgumentException ("C_Charge_ID is mandatory.");
        set_ValueNoCheck ("C_Charge_ID", Integer.valueOf(C_Charge_ID));
        
    }
    
    /** Get Charge.
    @return Additional document charges */
    public int getC_Charge_ID() 
    {
        return get_ValueAsInt("C_Charge_ID");
        
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
    
    /** Set Charge amount.
    @param ChargeAmt Charge Amount */
    public void setChargeAmt (java.math.BigDecimal ChargeAmt)
    {
        if (ChargeAmt == null) throw new IllegalArgumentException ("ChargeAmt is mandatory.");
        set_Value ("ChargeAmt", ChargeAmt);
        
    }
    
    /** Get Charge amount.
    @return Charge Amount */
    public java.math.BigDecimal getChargeAmt() 
    {
        return get_ValueAsBigDecimal("ChargeAmt");
        
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
    
    /** Set Same Currency.
    @param IsSameCurrency Same Currency */
    public void setIsSameCurrency (boolean IsSameCurrency)
    {
        set_Value ("IsSameCurrency", Boolean.valueOf(IsSameCurrency));
        
    }
    
    /** Get Same Currency.
    @return Same Currency */
    public boolean isSameCurrency() 
    {
        return get_ValueAsBoolean("IsSameCurrency");
        
    }
    
    /** Set Same Tax.
    @param IsSameTax Use the same tax as the main transaction */
    public void setIsSameTax (boolean IsSameTax)
    {
        set_Value ("IsSameTax", Boolean.valueOf(IsSameTax));
        
    }
    
    /** Get Same Tax.
    @return Use the same tax as the main transaction */
    public boolean isSameTax() 
    {
        return get_ValueAsBoolean("IsSameTax");
        
    }
    
    /** Set Price includes Tax.
    @param IsTaxIncluded Tax is included in the price */
    public void setIsTaxIncluded (boolean IsTaxIncluded)
    {
        set_Value ("IsTaxIncluded", Boolean.valueOf(IsTaxIncluded));
        
    }
    
    /** Get Price includes Tax.
    @return Tax is included in the price */
    public boolean isTaxIncluded() 
    {
        return get_ValueAsBoolean("IsTaxIncluded");
        
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
    
    
}
