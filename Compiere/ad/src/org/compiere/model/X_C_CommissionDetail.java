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
/** Generated Model for C_CommissionDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_CommissionDetail.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_CommissionDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_CommissionDetail_ID id
    @param trx transaction
    */
    public X_C_CommissionDetail (Ctx ctx, int C_CommissionDetail_ID, Trx trx)
    {
        super (ctx, C_CommissionDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_CommissionDetail_ID == 0)
        {
            setActualAmt (Env.ZERO);
            setActualQty (Env.ZERO);
            setC_CommissionAmt_ID (0);
            setC_CommissionDetail_ID (0);
            setC_Currency_ID (0);
            setConvertedAmt (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_CommissionDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=437 */
    public static final int Table_ID=437;
    
    /** TableName=C_CommissionDetail */
    public static final String Table_Name="C_CommissionDetail";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Actual Amount.
    @param ActualAmt The actual amount */
    public void setActualAmt (java.math.BigDecimal ActualAmt)
    {
        if (ActualAmt == null) throw new IllegalArgumentException ("ActualAmt is mandatory.");
        set_Value ("ActualAmt", ActualAmt);
        
    }
    
    /** Get Actual Amount.
    @return The actual amount */
    public java.math.BigDecimal getActualAmt() 
    {
        return get_ValueAsBigDecimal("ActualAmt");
        
    }
    
    /** Set Actual Quantity.
    @param ActualQty The actual quantity */
    public void setActualQty (java.math.BigDecimal ActualQty)
    {
        if (ActualQty == null) throw new IllegalArgumentException ("ActualQty is mandatory.");
        set_Value ("ActualQty", ActualQty);
        
    }
    
    /** Get Actual Quantity.
    @return The actual quantity */
    public java.math.BigDecimal getActualQty() 
    {
        return get_ValueAsBigDecimal("ActualQty");
        
    }
    
    /** Set Generated Commission Amount.
    @param C_CommissionAmt_ID Generated Commission Amount */
    public void setC_CommissionAmt_ID (int C_CommissionAmt_ID)
    {
        if (C_CommissionAmt_ID < 1) throw new IllegalArgumentException ("C_CommissionAmt_ID is mandatory.");
        set_ValueNoCheck ("C_CommissionAmt_ID", Integer.valueOf(C_CommissionAmt_ID));
        
    }
    
    /** Get Generated Commission Amount.
    @return Generated Commission Amount */
    public int getC_CommissionAmt_ID() 
    {
        return get_ValueAsInt("C_CommissionAmt_ID");
        
    }
    
    /** Set Commission Detail.
    @param C_CommissionDetail_ID Supporting information for Commission Amounts */
    public void setC_CommissionDetail_ID (int C_CommissionDetail_ID)
    {
        if (C_CommissionDetail_ID < 1) throw new IllegalArgumentException ("C_CommissionDetail_ID is mandatory.");
        set_ValueNoCheck ("C_CommissionDetail_ID", Integer.valueOf(C_CommissionDetail_ID));
        
    }
    
    /** Get Commission Detail.
    @return Supporting information for Commission Amounts */
    public int getC_CommissionDetail_ID() 
    {
        return get_ValueAsInt("C_CommissionDetail_ID");
        
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
    
    /** Set Order Line.
    @param C_OrderLine_ID Order Line */
    public void setC_OrderLine_ID (int C_OrderLine_ID)
    {
        if (C_OrderLine_ID <= 0) set_ValueNoCheck ("C_OrderLine_ID", null);
        else
        set_ValueNoCheck ("C_OrderLine_ID", Integer.valueOf(C_OrderLine_ID));
        
    }
    
    /** Get Order Line.
    @return Order Line */
    public int getC_OrderLine_ID() 
    {
        return get_ValueAsInt("C_OrderLine_ID");
        
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
    
    /** Set Info.
    @param Info Information */
    public void setInfo (String Info)
    {
        set_Value ("Info", Info);
        
    }
    
    /** Get Info.
    @return Information */
    public String getInfo() 
    {
        return (String)get_Value("Info");
        
    }
    
    /** Set Reference.
    @param Reference Reference for this record */
    public void setReference (String Reference)
    {
        set_Value ("Reference", Reference);
        
    }
    
    /** Get Reference.
    @return Reference for this record */
    public String getReference() 
    {
        return (String)get_Value("Reference");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getReference());
        
    }
    
    
}
