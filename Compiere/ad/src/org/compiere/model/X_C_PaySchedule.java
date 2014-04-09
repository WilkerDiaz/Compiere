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
/** Generated Model for C_PaySchedule
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_PaySchedule.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_PaySchedule extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_PaySchedule_ID id
    @param trx transaction
    */
    public X_C_PaySchedule (Ctx ctx, int C_PaySchedule_ID, Trx trx)
    {
        super (ctx, C_PaySchedule_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_PaySchedule_ID == 0)
        {
            setC_PaySchedule_ID (0);
            setC_PaymentTerm_ID (0);
            setDiscount (Env.ZERO);
            setDiscountDays (0);
            setGraceDays (0);
            setIsValid (false);
            setNetDays (0);
            setPercentage (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_PaySchedule (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=548 */
    public static final int Table_ID=548;
    
    /** TableName=C_PaySchedule */
    public static final String Table_Name="C_PaySchedule";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Payment Schedule.
    @param C_PaySchedule_ID Payment Schedule Template */
    public void setC_PaySchedule_ID (int C_PaySchedule_ID)
    {
        if (C_PaySchedule_ID < 1) throw new IllegalArgumentException ("C_PaySchedule_ID is mandatory.");
        set_ValueNoCheck ("C_PaySchedule_ID", Integer.valueOf(C_PaySchedule_ID));
        
    }
    
    /** Get Payment Schedule.
    @return Payment Schedule Template */
    public int getC_PaySchedule_ID() 
    {
        return get_ValueAsInt("C_PaySchedule_ID");
        
    }
    
    /** Set Payment Term.
    @param C_PaymentTerm_ID The terms of Payment (timing, discount) */
    public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
    {
        if (C_PaymentTerm_ID < 1) throw new IllegalArgumentException ("C_PaymentTerm_ID is mandatory.");
        set_ValueNoCheck ("C_PaymentTerm_ID", Integer.valueOf(C_PaymentTerm_ID));
        
    }
    
    /** Get Payment Term.
    @return The terms of Payment (timing, discount) */
    public int getC_PaymentTerm_ID() 
    {
        return get_ValueAsInt("C_PaymentTerm_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_PaymentTerm_ID()));
        
    }
    
    /** Set Discount %.
    @param Discount Discount in percent */
    public void setDiscount (java.math.BigDecimal Discount)
    {
        if (Discount == null) throw new IllegalArgumentException ("Discount is mandatory.");
        set_Value ("Discount", Discount);
        
    }
    
    /** Get Discount %.
    @return Discount in percent */
    public java.math.BigDecimal getDiscount() 
    {
        return get_ValueAsBigDecimal("Discount");
        
    }
    
    /** Set Discount Days.
    @param DiscountDays Number of days from invoice date to be eligible for discount */
    public void setDiscountDays (int DiscountDays)
    {
        set_Value ("DiscountDays", Integer.valueOf(DiscountDays));
        
    }
    
    /** Get Discount Days.
    @return Number of days from invoice date to be eligible for discount */
    public int getDiscountDays() 
    {
        return get_ValueAsInt("DiscountDays");
        
    }
    
    /** Set Grace Days.
    @param GraceDays Days after due date to send first dunning letter */
    public void setGraceDays (int GraceDays)
    {
        set_Value ("GraceDays", Integer.valueOf(GraceDays));
        
    }
    
    /** Get Grace Days.
    @return Days after due date to send first dunning letter */
    public int getGraceDays() 
    {
        return get_ValueAsInt("GraceDays");
        
    }
    
    /** Set Valid.
    @param IsValid Element is valid */
    public void setIsValid (boolean IsValid)
    {
        set_Value ("IsValid", Boolean.valueOf(IsValid));
        
    }
    
    /** Get Valid.
    @return Element is valid */
    public boolean isValid() 
    {
        return get_ValueAsBoolean("IsValid");
        
    }
    
    /** Monday = 1 */
    public static final String NETDAY_Monday = X_Ref_Weekdays.MONDAY.getValue();
    /** Tuesday = 2 */
    public static final String NETDAY_Tuesday = X_Ref_Weekdays.TUESDAY.getValue();
    /** Wednesday = 3 */
    public static final String NETDAY_Wednesday = X_Ref_Weekdays.WEDNESDAY.getValue();
    /** Thursday = 4 */
    public static final String NETDAY_Thursday = X_Ref_Weekdays.THURSDAY.getValue();
    /** Friday = 5 */
    public static final String NETDAY_Friday = X_Ref_Weekdays.FRIDAY.getValue();
    /** Saturday = 6 */
    public static final String NETDAY_Saturday = X_Ref_Weekdays.SATURDAY.getValue();
    /** Sunday = 7 */
    public static final String NETDAY_Sunday = X_Ref_Weekdays.SUNDAY.getValue();
    /** Set Net Day.
    @param NetDay Day when payment is due net */
    public void setNetDay (String NetDay)
    {
        if (!X_Ref_Weekdays.isValid(NetDay))
        throw new IllegalArgumentException ("NetDay Invalid value - " + NetDay + " - Reference_ID=167 - 1 - 2 - 3 - 4 - 5 - 6 - 7");
        set_Value ("NetDay", NetDay);
        
    }
    
    /** Get Net Day.
    @return Day when payment is due net */
    public String getNetDay() 
    {
        return (String)get_Value("NetDay");
        
    }
    
    /** Set Net Days.
    @param NetDays Net Days in which payment is due */
    public void setNetDays (int NetDays)
    {
        set_Value ("NetDays", Integer.valueOf(NetDays));
        
    }
    
    /** Get Net Days.
    @return Net Days in which payment is due */
    public int getNetDays() 
    {
        return get_ValueAsInt("NetDays");
        
    }
    
    /** Set Percentage.
    @param Percentage Percent of the entire amount */
    public void setPercentage (java.math.BigDecimal Percentage)
    {
        if (Percentage == null) throw new IllegalArgumentException ("Percentage is mandatory.");
        set_Value ("Percentage", Percentage);
        
    }
    
    /** Get Percentage.
    @return Percent of the entire amount */
    public java.math.BigDecimal getPercentage() 
    {
        return get_ValueAsBigDecimal("Percentage");
        
    }
    
    
}
