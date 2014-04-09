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
/** Generated Model for C_InvoiceSchedule
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_InvoiceSchedule.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_InvoiceSchedule extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_InvoiceSchedule_ID id
    @param trx transaction
    */
    public X_C_InvoiceSchedule (Ctx ctx, int C_InvoiceSchedule_ID, Trx trx)
    {
        super (ctx, C_InvoiceSchedule_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_InvoiceSchedule_ID == 0)
        {
            setAmt (Env.ZERO);
            setC_InvoiceSchedule_ID (0);
            setInvoiceFrequency (null);
            setIsAmount (false);
            setIsDefault (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_InvoiceSchedule (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=257 */
    public static final int Table_ID=257;
    
    /** TableName=C_InvoiceSchedule */
    public static final String Table_Name="C_InvoiceSchedule";
    
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
    
    /** Set Invoice Schedule.
    @param C_InvoiceSchedule_ID Schedule for generating Invoices */
    public void setC_InvoiceSchedule_ID (int C_InvoiceSchedule_ID)
    {
        if (C_InvoiceSchedule_ID < 1) throw new IllegalArgumentException ("C_InvoiceSchedule_ID is mandatory.");
        set_ValueNoCheck ("C_InvoiceSchedule_ID", Integer.valueOf(C_InvoiceSchedule_ID));
        
    }
    
    /** Get Invoice Schedule.
    @return Schedule for generating Invoices */
    public int getC_InvoiceSchedule_ID() 
    {
        return get_ValueAsInt("C_InvoiceSchedule_ID");
        
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
    
    /** Set Invoice on even weeks.
    @param EvenInvoiceWeek Send invoices on even weeks */
    public void setEvenInvoiceWeek (boolean EvenInvoiceWeek)
    {
        set_Value ("EvenInvoiceWeek", Boolean.valueOf(EvenInvoiceWeek));
        
    }
    
    /** Get Invoice on even weeks.
    @return Send invoices on even weeks */
    public boolean isEvenInvoiceWeek() 
    {
        return get_ValueAsBoolean("EvenInvoiceWeek");
        
    }
    
    /** Set Invoice Day.
    @param InvoiceDay Day of Invoice Generation */
    public void setInvoiceDay (int InvoiceDay)
    {
        set_Value ("InvoiceDay", Integer.valueOf(InvoiceDay));
        
    }
    
    /** Get Invoice Day.
    @return Day of Invoice Generation */
    public int getInvoiceDay() 
    {
        return get_ValueAsInt("InvoiceDay");
        
    }
    
    /** Set Invoice day cut-off.
    @param InvoiceDayCutoff Last day for including shipments */
    public void setInvoiceDayCutoff (int InvoiceDayCutoff)
    {
        set_Value ("InvoiceDayCutoff", Integer.valueOf(InvoiceDayCutoff));
        
    }
    
    /** Get Invoice day cut-off.
    @return Last day for including shipments */
    public int getInvoiceDayCutoff() 
    {
        return get_ValueAsInt("InvoiceDayCutoff");
        
    }
    
    /** Daily = D */
    public static final String INVOICEFREQUENCY_Daily = X_Ref_C_InvoiceSchedule_InvoiceFrequency.DAILY.getValue();
    /** Monthly = M */
    public static final String INVOICEFREQUENCY_Monthly = X_Ref_C_InvoiceSchedule_InvoiceFrequency.MONTHLY.getValue();
    /** Twice Monthly = T */
    public static final String INVOICEFREQUENCY_TwiceMonthly = X_Ref_C_InvoiceSchedule_InvoiceFrequency.TWICE_MONTHLY.getValue();
    /** Weekly = W */
    public static final String INVOICEFREQUENCY_Weekly = X_Ref_C_InvoiceSchedule_InvoiceFrequency.WEEKLY.getValue();
    /** Set Invoice Frequency.
    @param InvoiceFrequency How often invoices will be generated */
    public void setInvoiceFrequency (String InvoiceFrequency)
    {
        if (InvoiceFrequency == null) throw new IllegalArgumentException ("InvoiceFrequency is mandatory");
        if (!X_Ref_C_InvoiceSchedule_InvoiceFrequency.isValid(InvoiceFrequency))
        throw new IllegalArgumentException ("InvoiceFrequency Invalid value - " + InvoiceFrequency + " - Reference_ID=168 - D - M - T - W");
        set_Value ("InvoiceFrequency", InvoiceFrequency);
        
    }
    
    /** Get Invoice Frequency.
    @return How often invoices will be generated */
    public String getInvoiceFrequency() 
    {
        return (String)get_Value("InvoiceFrequency");
        
    }
    
    /** Monday = 1 */
    public static final String INVOICEWEEKDAY_Monday = X_Ref_Weekdays.MONDAY.getValue();
    /** Tuesday = 2 */
    public static final String INVOICEWEEKDAY_Tuesday = X_Ref_Weekdays.TUESDAY.getValue();
    /** Wednesday = 3 */
    public static final String INVOICEWEEKDAY_Wednesday = X_Ref_Weekdays.WEDNESDAY.getValue();
    /** Thursday = 4 */
    public static final String INVOICEWEEKDAY_Thursday = X_Ref_Weekdays.THURSDAY.getValue();
    /** Friday = 5 */
    public static final String INVOICEWEEKDAY_Friday = X_Ref_Weekdays.FRIDAY.getValue();
    /** Saturday = 6 */
    public static final String INVOICEWEEKDAY_Saturday = X_Ref_Weekdays.SATURDAY.getValue();
    /** Sunday = 7 */
    public static final String INVOICEWEEKDAY_Sunday = X_Ref_Weekdays.SUNDAY.getValue();
    /** Set Invoice Week Day.
    @param InvoiceWeekDay Day to generate invoices */
    public void setInvoiceWeekDay (String InvoiceWeekDay)
    {
        if (!X_Ref_Weekdays.isValid(InvoiceWeekDay))
        throw new IllegalArgumentException ("InvoiceWeekDay Invalid value - " + InvoiceWeekDay + " - Reference_ID=167 - 1 - 2 - 3 - 4 - 5 - 6 - 7");
        set_Value ("InvoiceWeekDay", InvoiceWeekDay);
        
    }
    
    /** Get Invoice Week Day.
    @return Day to generate invoices */
    public String getInvoiceWeekDay() 
    {
        return (String)get_Value("InvoiceWeekDay");
        
    }
    
    /** Monday = 1 */
    public static final String INVOICEWEEKDAYCUTOFF_Monday = X_Ref_Weekdays.MONDAY.getValue();
    /** Tuesday = 2 */
    public static final String INVOICEWEEKDAYCUTOFF_Tuesday = X_Ref_Weekdays.TUESDAY.getValue();
    /** Wednesday = 3 */
    public static final String INVOICEWEEKDAYCUTOFF_Wednesday = X_Ref_Weekdays.WEDNESDAY.getValue();
    /** Thursday = 4 */
    public static final String INVOICEWEEKDAYCUTOFF_Thursday = X_Ref_Weekdays.THURSDAY.getValue();
    /** Friday = 5 */
    public static final String INVOICEWEEKDAYCUTOFF_Friday = X_Ref_Weekdays.FRIDAY.getValue();
    /** Saturday = 6 */
    public static final String INVOICEWEEKDAYCUTOFF_Saturday = X_Ref_Weekdays.SATURDAY.getValue();
    /** Sunday = 7 */
    public static final String INVOICEWEEKDAYCUTOFF_Sunday = X_Ref_Weekdays.SUNDAY.getValue();
    /** Set Invoice weekday cutoff.
    @param InvoiceWeekDayCutoff Last day in the week for shipments to be included */
    public void setInvoiceWeekDayCutoff (String InvoiceWeekDayCutoff)
    {
        if (!X_Ref_Weekdays.isValid(InvoiceWeekDayCutoff))
        throw new IllegalArgumentException ("InvoiceWeekDayCutoff Invalid value - " + InvoiceWeekDayCutoff + " - Reference_ID=167 - 1 - 2 - 3 - 4 - 5 - 6 - 7");
        set_Value ("InvoiceWeekDayCutoff", InvoiceWeekDayCutoff);
        
    }
    
    /** Get Invoice weekday cutoff.
    @return Last day in the week for shipments to be included */
    public String getInvoiceWeekDayCutoff() 
    {
        return (String)get_Value("InvoiceWeekDayCutoff");
        
    }
    
    /** Set Amount Limit.
    @param IsAmount Send invoices only if the amount exceeds the limit */
    public void setIsAmount (boolean IsAmount)
    {
        set_Value ("IsAmount", Boolean.valueOf(IsAmount));
        
    }
    
    /** Get Amount Limit.
    @return Send invoices only if the amount exceeds the limit */
    public boolean isAmount() 
    {
        return get_ValueAsBoolean("IsAmount");
        
    }
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
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
