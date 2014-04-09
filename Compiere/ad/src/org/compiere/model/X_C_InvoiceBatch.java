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
/** Generated Model for C_InvoiceBatch
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_InvoiceBatch.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_InvoiceBatch extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_InvoiceBatch_ID id
    @param trx transaction
    */
    public X_C_InvoiceBatch (Ctx ctx, int C_InvoiceBatch_ID, Trx trx)
    {
        super (ctx, C_InvoiceBatch_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_InvoiceBatch_ID == 0)
        {
            setC_Currency_ID (0);	// @$C_Currency_ID@
            setC_InvoiceBatch_ID (0);
            setControlAmt (Env.ZERO);	// 0
            setDateDoc (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setDocumentAmt (Env.ZERO);
            setDocumentNo (null);
            setIsSOTrx (false);	// N
            setProcessed (false);	// N
            setSalesRep_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_InvoiceBatch (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=767 */
    public static final int Table_ID=767;
    
    /** TableName=C_InvoiceBatch */
    public static final String Table_Name="C_InvoiceBatch";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Rate Type.
    @param C_ConversionType_ID Currency Conversion Rate Type */
    public void setC_ConversionType_ID (int C_ConversionType_ID)
    {
        if (C_ConversionType_ID <= 0) set_Value ("C_ConversionType_ID", null);
        else
        set_Value ("C_ConversionType_ID", Integer.valueOf(C_ConversionType_ID));
        
    }
    
    /** Get Rate Type.
    @return Currency Conversion Rate Type */
    public int getC_ConversionType_ID() 
    {
        return get_ValueAsInt("C_ConversionType_ID");
        
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
    
    /** Set Control Amount.
    @param ControlAmt If not zero, the Debit amount of the document must be equal this amount */
    public void setControlAmt (java.math.BigDecimal ControlAmt)
    {
        if (ControlAmt == null) throw new IllegalArgumentException ("ControlAmt is mandatory.");
        set_Value ("ControlAmt", ControlAmt);
        
    }
    
    /** Get Control Amount.
    @return If not zero, the Debit amount of the document must be equal this amount */
    public java.math.BigDecimal getControlAmt() 
    {
        return get_ValueAsBigDecimal("ControlAmt");
        
    }
    
    /** Set Document Date.
    @param DateDoc Date of the Document */
    public void setDateDoc (Timestamp DateDoc)
    {
        if (DateDoc == null) throw new IllegalArgumentException ("DateDoc is mandatory.");
        set_Value ("DateDoc", DateDoc);
        
    }
    
    /** Get Document Date.
    @return Date of the Document */
    public Timestamp getDateDoc() 
    {
        return (Timestamp)get_Value("DateDoc");
        
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
    
    /** Set Document Amt.
    @param DocumentAmt Document Amount */
    public void setDocumentAmt (java.math.BigDecimal DocumentAmt)
    {
        if (DocumentAmt == null) throw new IllegalArgumentException ("DocumentAmt is mandatory.");
        set_ValueNoCheck ("DocumentAmt", DocumentAmt);
        
    }
    
    /** Get Document Amt.
    @return Document Amount */
    public java.math.BigDecimal getDocumentAmt() 
    {
        return get_ValueAsBigDecimal("DocumentAmt");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
        
    }
    
    /** Set Sales Transaction.
    @param IsSOTrx This is a Sales Transaction */
    public void setIsSOTrx (boolean IsSOTrx)
    {
        set_Value ("IsSOTrx", Boolean.valueOf(IsSOTrx));
        
    }
    
    /** Get Sales Transaction.
    @return This is a Sales Transaction */
    public boolean isSOTrx() 
    {
        return get_ValueAsBoolean("IsSOTrx");
        
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
    
    /** Set Representative.
    @param SalesRep_ID Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public void setSalesRep_ID (int SalesRep_ID)
    {
        if (SalesRep_ID < 1) throw new IllegalArgumentException ("SalesRep_ID is mandatory.");
        set_Value ("SalesRep_ID", Integer.valueOf(SalesRep_ID));
        
    }
    
    /** Get Representative.
    @return Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public int getSalesRep_ID() 
    {
        return get_ValueAsInt("SalesRep_ID");
        
    }
    
    
}
