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
/** Generated Model for C_DunningRunLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_DunningRunLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_DunningRunLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_DunningRunLine_ID id
    @param trx transaction
    */
    public X_C_DunningRunLine (Ctx ctx, int C_DunningRunLine_ID, Trx trx)
    {
        super (ctx, C_DunningRunLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_DunningRunLine_ID == 0)
        {
            setAmt (Env.ZERO);
            setC_DunningRunEntry_ID (0);
            setC_DunningRunLine_ID (0);
            setConvertedAmt (Env.ZERO);
            setDaysDue (0);
            setFeeAmt (Env.ZERO);
            setInterestAmt (Env.ZERO);
            setIsInDispute (false);
            setOpenAmt (Env.ZERO);
            setProcessed (false);	// N
            setTimesDunned (0);
            setTotalAmt (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_DunningRunLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=524 */
    public static final int Table_ID=524;
    
    /** TableName=C_DunningRunLine */
    public static final String Table_Name="C_DunningRunLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Amount.
    @param Amt Amount */
    public void setAmt (java.math.BigDecimal Amt)
    {
        if (Amt == null) throw new IllegalArgumentException ("Amt is mandatory.");
        set_Value ("Amt", Amt);
        
    }
    
    /** Get Amount.
    @return Amount */
    public java.math.BigDecimal getAmt() 
    {
        return get_ValueAsBigDecimal("Amt");
        
    }
    
    /** Set Dunning Run Entry.
    @param C_DunningRunEntry_ID Dunning Run Entry */
    public void setC_DunningRunEntry_ID (int C_DunningRunEntry_ID)
    {
        if (C_DunningRunEntry_ID < 1) throw new IllegalArgumentException ("C_DunningRunEntry_ID is mandatory.");
        set_ValueNoCheck ("C_DunningRunEntry_ID", Integer.valueOf(C_DunningRunEntry_ID));
        
    }
    
    /** Get Dunning Run Entry.
    @return Dunning Run Entry */
    public int getC_DunningRunEntry_ID() 
    {
        return get_ValueAsInt("C_DunningRunEntry_ID");
        
    }
    
    /** Set Dunning Run Line.
    @param C_DunningRunLine_ID Dunning Run Line */
    public void setC_DunningRunLine_ID (int C_DunningRunLine_ID)
    {
        if (C_DunningRunLine_ID < 1) throw new IllegalArgumentException ("C_DunningRunLine_ID is mandatory.");
        set_ValueNoCheck ("C_DunningRunLine_ID", Integer.valueOf(C_DunningRunLine_ID));
        
    }
    
    /** Get Dunning Run Line.
    @return Dunning Run Line */
    public int getC_DunningRunLine_ID() 
    {
        return get_ValueAsInt("C_DunningRunLine_ID");
        
    }
    
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID <= 0) set_Value ("C_Invoice_ID", null);
        else
        set_Value ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
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
    
    /** Set Payment.
    @param C_Payment_ID Payment identifier */
    public void setC_Payment_ID (int C_Payment_ID)
    {
        if (C_Payment_ID <= 0) set_Value ("C_Payment_ID", null);
        else
        set_Value ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
    }
    
    /** Get Payment.
    @return Payment identifier */
    public int getC_Payment_ID() 
    {
        return get_ValueAsInt("C_Payment_ID");
        
    }
    
    /** Set Converted Amount.
    @param ConvertedAmt Converted Amount */
    public void setConvertedAmt (java.math.BigDecimal ConvertedAmt)
    {
        if (ConvertedAmt == null) throw new IllegalArgumentException ("ConvertedAmt is mandatory.");
        set_Value ("ConvertedAmt", ConvertedAmt);
        
    }
    
    /** Get Converted Amount.
    @return Converted Amount */
    public java.math.BigDecimal getConvertedAmt() 
    {
        return get_ValueAsBigDecimal("ConvertedAmt");
        
    }
    
    /** Set Days due.
    @param DaysDue Number of days due (negative: due in number of days) */
    public void setDaysDue (int DaysDue)
    {
        set_Value ("DaysDue", Integer.valueOf(DaysDue));
        
    }
    
    /** Get Days due.
    @return Number of days due (negative: due in number of days) */
    public int getDaysDue() 
    {
        return get_ValueAsInt("DaysDue");
        
    }
    
    /** Set Fee Amount.
    @param FeeAmt Fee amount in invoice currency */
    public void setFeeAmt (java.math.BigDecimal FeeAmt)
    {
        if (FeeAmt == null) throw new IllegalArgumentException ("FeeAmt is mandatory.");
        set_Value ("FeeAmt", FeeAmt);
        
    }
    
    /** Get Fee Amount.
    @return Fee amount in invoice currency */
    public java.math.BigDecimal getFeeAmt() 
    {
        return get_ValueAsBigDecimal("FeeAmt");
        
    }
    
    /** Set Interest Amount.
    @param InterestAmt Interest Amount */
    public void setInterestAmt (java.math.BigDecimal InterestAmt)
    {
        if (InterestAmt == null) throw new IllegalArgumentException ("InterestAmt is mandatory.");
        set_Value ("InterestAmt", InterestAmt);
        
    }
    
    /** Get Interest Amount.
    @return Interest Amount */
    public java.math.BigDecimal getInterestAmt() 
    {
        return get_ValueAsBigDecimal("InterestAmt");
        
    }
    
    /** Set In Dispute.
    @param IsInDispute Document is in dispute */
    public void setIsInDispute (boolean IsInDispute)
    {
        set_Value ("IsInDispute", Boolean.valueOf(IsInDispute));
        
    }
    
    /** Get In Dispute.
    @return Document is in dispute */
    public boolean isInDispute() 
    {
        return get_ValueAsBoolean("IsInDispute");
        
    }
    
    /** Set Open Amount.
    @param OpenAmt Open item amount */
    public void setOpenAmt (java.math.BigDecimal OpenAmt)
    {
        if (OpenAmt == null) throw new IllegalArgumentException ("OpenAmt is mandatory.");
        set_Value ("OpenAmt", OpenAmt);
        
    }
    
    /** Get Open Amount.
    @return Open item amount */
    public java.math.BigDecimal getOpenAmt() 
    {
        return get_ValueAsBigDecimal("OpenAmt");
        
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
    
    /** Set Times Dunned.
    @param TimesDunned Number of times dunned previously */
    public void setTimesDunned (int TimesDunned)
    {
        set_Value ("TimesDunned", Integer.valueOf(TimesDunned));
        
    }
    
    /** Get Times Dunned.
    @return Number of times dunned previously */
    public int getTimesDunned() 
    {
        return get_ValueAsInt("TimesDunned");
        
    }
    
    /** Set Total Amount.
    @param TotalAmt Total Amount */
    public void setTotalAmt (java.math.BigDecimal TotalAmt)
    {
        if (TotalAmt == null) throw new IllegalArgumentException ("TotalAmt is mandatory.");
        set_Value ("TotalAmt", TotalAmt);
        
    }
    
    /** Get Total Amount.
    @return Total Amount */
    public java.math.BigDecimal getTotalAmt() 
    {
        return get_ValueAsBigDecimal("TotalAmt");
        
    }
    
    
}
