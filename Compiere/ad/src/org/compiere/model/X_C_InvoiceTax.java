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
/** Generated Model for C_InvoiceTax
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_InvoiceTax.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_InvoiceTax extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_InvoiceTax_ID id
    @param trx transaction
    */
    public X_C_InvoiceTax (Ctx ctx, int C_InvoiceTax_ID, Trx trx)
    {
        super (ctx, C_InvoiceTax_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_InvoiceTax_ID == 0)
        {
            setC_Invoice_ID (0);
            setC_Tax_ID (0);
            setIsTaxIncluded (false);
            setProcessed (false);	// N
            setTaxAmt (Env.ZERO);
            setTaxBaseAmt (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_InvoiceTax (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=334 */
    public static final int Table_ID=334;
    
    /** TableName=C_InvoiceTax */
    public static final String Table_Name="C_InvoiceTax";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID < 1) throw new IllegalArgumentException ("C_Invoice_ID is mandatory.");
        set_ValueNoCheck ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Invoice_ID()));
        
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
    
    /** Set Tax Amount.
    @param TaxAmt Tax Amount for a document */
    public void setTaxAmt (java.math.BigDecimal TaxAmt)
    {
        if (TaxAmt == null) throw new IllegalArgumentException ("TaxAmt is mandatory.");
        set_ValueNoCheck ("TaxAmt", TaxAmt);
        
    }
    
    /** Get Tax Amount.
    @return Tax Amount for a document */
    public java.math.BigDecimal getTaxAmt() 
    {
        return get_ValueAsBigDecimal("TaxAmt");
        
    }
    
    /** Set Taxable Amount.
    @param TaxBaseAmt Base for calculating the tax amount */
    public void setTaxBaseAmt (java.math.BigDecimal TaxBaseAmt)
    {
        if (TaxBaseAmt == null) throw new IllegalArgumentException ("TaxBaseAmt is mandatory.");
        set_ValueNoCheck ("TaxBaseAmt", TaxBaseAmt);
        
    }
    
    /** Get Taxable Amount.
    @return Base for calculating the tax amount */
    public java.math.BigDecimal getTaxBaseAmt() 
    {
        return get_ValueAsBigDecimal("TaxBaseAmt");
        
    }
    
    
}
