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
/** Generated Model for C_PaymentBatch
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_PaymentBatch.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_PaymentBatch extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_PaymentBatch_ID id
    @param trx transaction
    */
    public X_C_PaymentBatch (Ctx ctx, int C_PaymentBatch_ID, Trx trx)
    {
        super (ctx, C_PaymentBatch_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_PaymentBatch_ID == 0)
        {
            setC_PaymentBatch_ID (0);
            setName (null);
            setProcessed (false);	// N
            setProcessing (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_PaymentBatch (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=411 */
    public static final int Table_ID=411;
    
    /** TableName=C_PaymentBatch */
    public static final String Table_Name="C_PaymentBatch";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Payment Batch.
    @param C_PaymentBatch_ID Payment batch for EFT */
    public void setC_PaymentBatch_ID (int C_PaymentBatch_ID)
    {
        if (C_PaymentBatch_ID < 1) throw new IllegalArgumentException ("C_PaymentBatch_ID is mandatory.");
        set_ValueNoCheck ("C_PaymentBatch_ID", Integer.valueOf(C_PaymentBatch_ID));
        
    }
    
    /** Get Payment Batch.
    @return Payment batch for EFT */
    public int getC_PaymentBatch_ID() 
    {
        return get_ValueAsInt("C_PaymentBatch_ID");
        
    }
    
    /** Set Payment Processor.
    @param C_PaymentProcessor_ID Payment processor for electronic payments */
    public void setC_PaymentProcessor_ID (int C_PaymentProcessor_ID)
    {
        if (C_PaymentProcessor_ID <= 0) set_Value ("C_PaymentProcessor_ID", null);
        else
        set_Value ("C_PaymentProcessor_ID", Integer.valueOf(C_PaymentProcessor_ID));
        
    }
    
    /** Get Payment Processor.
    @return Payment processor for electronic payments */
    public int getC_PaymentProcessor_ID() 
    {
        return get_ValueAsInt("C_PaymentProcessor_ID");
        
    }
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
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
    
    /** Set Processing date.
    @param ProcessingDate Processing date */
    public void setProcessingDate (Timestamp ProcessingDate)
    {
        set_Value ("ProcessingDate", ProcessingDate);
        
    }
    
    /** Get Processing date.
    @return Processing date */
    public Timestamp getProcessingDate() 
    {
        return (Timestamp)get_Value("ProcessingDate");
        
    }
    
    
}
