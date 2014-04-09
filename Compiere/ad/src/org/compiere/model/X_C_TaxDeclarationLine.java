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
/** Generated Model for C_TaxDeclarationLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_TaxDeclarationLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_TaxDeclarationLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_TaxDeclarationLine_ID id
    @param trx transaction
    */
    public X_C_TaxDeclarationLine (Ctx ctx, int C_TaxDeclarationLine_ID, Trx trx)
    {
        super (ctx, C_TaxDeclarationLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_TaxDeclarationLine_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_Currency_ID (0);
            setC_TaxDeclarationLine_ID (0);
            setC_TaxDeclaration_ID (0);
            setC_Tax_ID (0);
            setDateAcct (new Timestamp(System.currentTimeMillis()));
            setIsManual (true);	// Y
            setLine (0);
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
    public X_C_TaxDeclarationLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=819 */
    public static final int Table_ID=819;
    
    /** TableName=C_TaxDeclarationLine */
    public static final String Table_Name="C_TaxDeclarationLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Allocation Line.
    @param C_AllocationLine_ID Allocation Line */
    public void setC_AllocationLine_ID (int C_AllocationLine_ID)
    {
        if (C_AllocationLine_ID <= 0) set_ValueNoCheck ("C_AllocationLine_ID", null);
        else
        set_ValueNoCheck ("C_AllocationLine_ID", Integer.valueOf(C_AllocationLine_ID));
        
    }
    
    /** Get Allocation Line.
    @return Allocation Line */
    public int getC_AllocationLine_ID() 
    {
        return get_ValueAsInt("C_AllocationLine_ID");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_ValueNoCheck ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Invoice Line.
    @param C_InvoiceLine_ID Invoice Detail Line */
    public void setC_InvoiceLine_ID (int C_InvoiceLine_ID)
    {
        if (C_InvoiceLine_ID <= 0) set_ValueNoCheck ("C_InvoiceLine_ID", null);
        else
        set_ValueNoCheck ("C_InvoiceLine_ID", Integer.valueOf(C_InvoiceLine_ID));
        
    }
    
    /** Get Invoice Line.
    @return Invoice Detail Line */
    public int getC_InvoiceLine_ID() 
    {
        return get_ValueAsInt("C_InvoiceLine_ID");
        
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
    
    /** Set Tax Declaration Line.
    @param C_TaxDeclarationLine_ID Tax Declaration Document Information */
    public void setC_TaxDeclarationLine_ID (int C_TaxDeclarationLine_ID)
    {
        if (C_TaxDeclarationLine_ID < 1) throw new IllegalArgumentException ("C_TaxDeclarationLine_ID is mandatory.");
        set_ValueNoCheck ("C_TaxDeclarationLine_ID", Integer.valueOf(C_TaxDeclarationLine_ID));
        
    }
    
    /** Get Tax Declaration Line.
    @return Tax Declaration Document Information */
    public int getC_TaxDeclarationLine_ID() 
    {
        return get_ValueAsInt("C_TaxDeclarationLine_ID");
        
    }
    
    /** Set Tax Declaration.
    @param C_TaxDeclaration_ID Define the declaration to the tax authorities */
    public void setC_TaxDeclaration_ID (int C_TaxDeclaration_ID)
    {
        if (C_TaxDeclaration_ID < 1) throw new IllegalArgumentException ("C_TaxDeclaration_ID is mandatory.");
        set_ValueNoCheck ("C_TaxDeclaration_ID", Integer.valueOf(C_TaxDeclaration_ID));
        
    }
    
    /** Get Tax Declaration.
    @return Define the declaration to the tax authorities */
    public int getC_TaxDeclaration_ID() 
    {
        return get_ValueAsInt("C_TaxDeclaration_ID");
        
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
    
    /** Set Account Date.
    @param DateAcct General Ledger Date */
    public void setDateAcct (Timestamp DateAcct)
    {
        if (DateAcct == null) throw new IllegalArgumentException ("DateAcct is mandatory.");
        set_ValueNoCheck ("DateAcct", DateAcct);
        
    }
    
    /** Get Account Date.
    @return General Ledger Date */
    public Timestamp getDateAcct() 
    {
        return (Timestamp)get_Value("DateAcct");
        
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
    
    /** Set Manual.
    @param IsManual This is a manual process or entry */
    public void setIsManual (boolean IsManual)
    {
        set_ValueNoCheck ("IsManual", Boolean.valueOf(IsManual));
        
    }
    
    /** Get Manual.
    @return This is a manual process or entry */
    public boolean isManual() 
    {
        return get_ValueAsBoolean("IsManual");
        
    }
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (int Line)
    {
        set_Value ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
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
