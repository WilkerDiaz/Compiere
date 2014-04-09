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
/** Generated Model for C_InvoiceBatchLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_InvoiceBatchLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_InvoiceBatchLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_InvoiceBatchLine_ID id
    @param trx transaction
    */
    public X_C_InvoiceBatchLine (Ctx ctx, int C_InvoiceBatchLine_ID, Trx trx)
    {
        super (ctx, C_InvoiceBatchLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_InvoiceBatchLine_ID == 0)
        {
            setC_BPartner_ID (0);	// @C_BPartner_ID@
            setC_BPartner_Location_ID (0);	// @C_BPartner_Location_ID@
            setC_Charge_ID (0);
            setC_DocType_ID (0);	// @C_DocType_ID@
            setC_InvoiceBatchLine_ID (0);
            setC_InvoiceBatch_ID (0);
            setC_Tax_ID (0);
            setDateAcct (new Timestamp(System.currentTimeMillis()));	// @DateAcct@;
            @DateDoc@
            setDateInvoiced (new Timestamp(System.currentTimeMillis()));	// @DateInvoiced@;
            @DateDoc@
            setDocumentNo (null);	// @DocumentNo@
            setIsTaxIncluded (false);	// @IsTaxIncluded@
            setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_InvoiceBatchLine WHERE C_InvoiceBatch_ID=@C_InvoiceBatch_ID@
            setLineNetAmt (Env.ZERO);
            setLineTotalAmt (Env.ZERO);
            setPriceEntered (Env.ZERO);
            setProcessed (false);	// N
            setQtyEntered (Env.ZERO);	// 1
            setTaxAmt (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_InvoiceBatchLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=768 */
    public static final int Table_ID=768;
    
    /** TableName=C_InvoiceBatchLine */
    public static final String Table_Name="C_InvoiceBatchLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Trx Organization.
    @param AD_OrgTrx_ID Performing or initiating organization */
    public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
    {
        if (AD_OrgTrx_ID <= 0) set_Value ("AD_OrgTrx_ID", null);
        else
        set_Value ("AD_OrgTrx_ID", Integer.valueOf(AD_OrgTrx_ID));
        
    }
    
    /** Get Trx Organization.
    @return Performing or initiating organization */
    public int getAD_OrgTrx_ID() 
    {
        return get_ValueAsInt("AD_OrgTrx_ID");
        
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
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
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
        if (C_BPartner_Location_ID < 1) throw new IllegalArgumentException ("C_BPartner_Location_ID is mandatory.");
        set_Value ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
    }
    
    /** Set Charge.
    @param C_Charge_ID Additional document charges */
    public void setC_Charge_ID (int C_Charge_ID)
    {
        if (C_Charge_ID < 1) throw new IllegalArgumentException ("C_Charge_ID is mandatory.");
        set_Value ("C_Charge_ID", Integer.valueOf(C_Charge_ID));
        
    }
    
    /** Get Charge.
    @return Additional document charges */
    public int getC_Charge_ID() 
    {
        return get_ValueAsInt("C_Charge_ID");
        
    }
    
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID < 0) throw new IllegalArgumentException ("C_DocType_ID is mandatory.");
        set_Value ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
    }
    
    /** Set Invoice Batch Line.
    @param C_InvoiceBatchLine_ID Expense Invoice Batch Line */
    public void setC_InvoiceBatchLine_ID (int C_InvoiceBatchLine_ID)
    {
        if (C_InvoiceBatchLine_ID < 1) throw new IllegalArgumentException ("C_InvoiceBatchLine_ID is mandatory.");
        set_ValueNoCheck ("C_InvoiceBatchLine_ID", Integer.valueOf(C_InvoiceBatchLine_ID));
        
    }
    
    /** Get Invoice Batch Line.
    @return Expense Invoice Batch Line */
    public int getC_InvoiceBatchLine_ID() 
    {
        return get_ValueAsInt("C_InvoiceBatchLine_ID");
        
    }
    
    /** Set Invoice Batch.
    @param C_InvoiceBatch_ID Expense Invoice Batch Header */
    public void setC_InvoiceBatch_ID (int C_InvoiceBatch_ID)
    {
        if (C_InvoiceBatch_ID < 1) throw new IllegalArgumentException ("C_InvoiceBatch_ID is mandatory.");
        set_ValueNoCheck ("C_InvoiceBatch_ID", Integer.valueOf(C_InvoiceBatch_ID));
        
    }
    
    /** Get Invoice Batch.
    @return Expense Invoice Batch Header */
    public int getC_InvoiceBatch_ID() 
    {
        return get_ValueAsInt("C_InvoiceBatch_ID");
        
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
    
    /** Set Tax.
    @param C_Tax_ID Tax identifier */
    public void setC_Tax_ID (int C_Tax_ID)
    {
        if (C_Tax_ID < 1) throw new IllegalArgumentException ("C_Tax_ID is mandatory.");
        set_Value ("C_Tax_ID", Integer.valueOf(C_Tax_ID));
        
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
        set_Value ("DateAcct", DateAcct);
        
    }
    
    /** Get Account Date.
    @return General Ledger Date */
    public Timestamp getDateAcct() 
    {
        return (Timestamp)get_Value("DateAcct");
        
    }
    
    /** Set Date Invoiced.
    @param DateInvoiced Date printed on Invoice */
    public void setDateInvoiced (Timestamp DateInvoiced)
    {
        if (DateInvoiced == null) throw new IllegalArgumentException ("DateInvoiced is mandatory.");
        set_Value ("DateInvoiced", DateInvoiced);
        
    }
    
    /** Get Date Invoiced.
    @return Date printed on Invoice */
    public Timestamp getDateInvoiced() 
    {
        return (Timestamp)get_Value("DateInvoiced");
        
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
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory.");
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getLine()));
        
    }
    
    /** Set Line Amount.
    @param LineNetAmt Line Extended Amount (Quantity * Actual Price) without Freight and Charges */
    public void setLineNetAmt (java.math.BigDecimal LineNetAmt)
    {
        if (LineNetAmt == null) throw new IllegalArgumentException ("LineNetAmt is mandatory.");
        set_ValueNoCheck ("LineNetAmt", LineNetAmt);
        
    }
    
    /** Get Line Amount.
    @return Line Extended Amount (Quantity * Actual Price) without Freight and Charges */
    public java.math.BigDecimal getLineNetAmt() 
    {
        return get_ValueAsBigDecimal("LineNetAmt");
        
    }
    
    /** Set Line Total.
    @param LineTotalAmt Total line amount incl. Tax */
    public void setLineTotalAmt (java.math.BigDecimal LineTotalAmt)
    {
        if (LineTotalAmt == null) throw new IllegalArgumentException ("LineTotalAmt is mandatory.");
        set_ValueNoCheck ("LineTotalAmt", LineTotalAmt);
        
    }
    
    /** Get Line Total.
    @return Total line amount incl. Tax */
    public java.math.BigDecimal getLineTotalAmt() 
    {
        return get_ValueAsBigDecimal("LineTotalAmt");
        
    }
    
    /** Set Price.
    @param PriceEntered Price Entered - the price based on the selected/base UoM */
    public void setPriceEntered (java.math.BigDecimal PriceEntered)
    {
        if (PriceEntered == null) throw new IllegalArgumentException ("PriceEntered is mandatory.");
        set_Value ("PriceEntered", PriceEntered);
        
    }
    
    /** Get Price.
    @return Price Entered - the price based on the selected/base UoM */
    public java.math.BigDecimal getPriceEntered() 
    {
        return get_ValueAsBigDecimal("PriceEntered");
        
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
    
    /** Set Quantity.
    @param QtyEntered The Quantity Entered is based on the selected UoM */
    public void setQtyEntered (java.math.BigDecimal QtyEntered)
    {
        if (QtyEntered == null) throw new IllegalArgumentException ("QtyEntered is mandatory.");
        set_Value ("QtyEntered", QtyEntered);
        
    }
    
    /** Get Quantity.
    @return The Quantity Entered is based on the selected UoM */
    public java.math.BigDecimal getQtyEntered() 
    {
        return get_ValueAsBigDecimal("QtyEntered");
        
    }
    
    /** Set Tax Amount.
    @param TaxAmt Tax Amount for a document */
    public void setTaxAmt (java.math.BigDecimal TaxAmt)
    {
        if (TaxAmt == null) throw new IllegalArgumentException ("TaxAmt is mandatory.");
        set_Value ("TaxAmt", TaxAmt);
        
    }
    
    /** Get Tax Amount.
    @return Tax Amount for a document */
    public java.math.BigDecimal getTaxAmt() 
    {
        return get_ValueAsBigDecimal("TaxAmt");
        
    }
    
    /** Set User List 1.
    @param User1_ID User defined list element #1 */
    public void setUser1_ID (int User1_ID)
    {
        if (User1_ID <= 0) set_Value ("User1_ID", null);
        else
        set_Value ("User1_ID", Integer.valueOf(User1_ID));
        
    }
    
    /** Get User List 1.
    @return User defined list element #1 */
    public int getUser1_ID() 
    {
        return get_ValueAsInt("User1_ID");
        
    }
    
    /** Set User List 2.
    @param User2_ID User defined list element #2 */
    public void setUser2_ID (int User2_ID)
    {
        if (User2_ID <= 0) set_Value ("User2_ID", null);
        else
        set_Value ("User2_ID", Integer.valueOf(User2_ID));
        
    }
    
    /** Get User List 2.
    @return User defined list element #2 */
    public int getUser2_ID() 
    {
        return get_ValueAsInt("User2_ID");
        
    }
    
    
}
