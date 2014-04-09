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
/** Generated Model for C_Recurring_Run
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Recurring_Run.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Recurring_Run extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Recurring_Run_ID id
    @param trx transaction
    */
    public X_C_Recurring_Run (Ctx ctx, int C_Recurring_Run_ID, Trx trx)
    {
        super (ctx, C_Recurring_Run_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Recurring_Run_ID == 0)
        {
            setC_Recurring_ID (0);
            setC_Recurring_Run_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Recurring_Run (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=573 */
    public static final int Table_ID=573;
    
    /** TableName=C_Recurring_Run */
    public static final String Table_Name="C_Recurring_Run";
    
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
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_ValueNoCheck ("C_Order_ID", null);
        else
        set_ValueNoCheck ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Payment.
    @param C_Payment_ID Payment identifier */
    public void setC_Payment_ID (int C_Payment_ID)
    {
        if (C_Payment_ID <= 0) set_ValueNoCheck ("C_Payment_ID", null);
        else
        set_ValueNoCheck ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
    }
    
    /** Get Payment.
    @return Payment identifier */
    public int getC_Payment_ID() 
    {
        return get_ValueAsInt("C_Payment_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID <= 0) set_ValueNoCheck ("C_Project_ID", null);
        else
        set_ValueNoCheck ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
    }
    
    /** Set Recurring.
    @param C_Recurring_ID Recurring Document */
    public void setC_Recurring_ID (int C_Recurring_ID)
    {
        if (C_Recurring_ID < 1) throw new IllegalArgumentException ("C_Recurring_ID is mandatory.");
        set_ValueNoCheck ("C_Recurring_ID", Integer.valueOf(C_Recurring_ID));
        
    }
    
    /** Get Recurring.
    @return Recurring Document */
    public int getC_Recurring_ID() 
    {
        return get_ValueAsInt("C_Recurring_ID");
        
    }
    
    /** Set Recurring Run.
    @param C_Recurring_Run_ID Recurring Document Run */
    public void setC_Recurring_Run_ID (int C_Recurring_Run_ID)
    {
        if (C_Recurring_Run_ID < 1) throw new IllegalArgumentException ("C_Recurring_Run_ID is mandatory.");
        set_ValueNoCheck ("C_Recurring_Run_ID", Integer.valueOf(C_Recurring_Run_ID));
        
    }
    
    /** Get Recurring Run.
    @return Recurring Document Run */
    public int getC_Recurring_Run_ID() 
    {
        return get_ValueAsInt("C_Recurring_Run_ID");
        
    }
    
    /** Set Document Date.
    @param DateDoc Date of the Document */
    public void setDateDoc (Timestamp DateDoc)
    {
        set_Value ("DateDoc", DateDoc);
        
    }
    
    /** Get Document Date.
    @return Date of the Document */
    public Timestamp getDateDoc() 
    {
        return (Timestamp)get_Value("DateDoc");
        
    }
    
    /** Set Journal Batch.
    @param GL_JournalBatch_ID General Ledger Journal Batch */
    public void setGL_JournalBatch_ID (int GL_JournalBatch_ID)
    {
        if (GL_JournalBatch_ID <= 0) set_ValueNoCheck ("GL_JournalBatch_ID", null);
        else
        set_ValueNoCheck ("GL_JournalBatch_ID", Integer.valueOf(GL_JournalBatch_ID));
        
    }
    
    /** Get Journal Batch.
    @return General Ledger Journal Batch */
    public int getGL_JournalBatch_ID() 
    {
        return get_ValueAsInt("GL_JournalBatch_ID");
        
    }
    
    
}
