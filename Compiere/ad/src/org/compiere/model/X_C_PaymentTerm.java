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
/** Generated Model for C_PaymentTerm
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_PaymentTerm.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_PaymentTerm extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_PaymentTerm_ID id
    @param trx transaction
    */
    public X_C_PaymentTerm (Ctx ctx, int C_PaymentTerm_ID, Trx trx)
    {
        super (ctx, C_PaymentTerm_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_PaymentTerm_ID == 0)
        {
            setAfterDelivery (false);
            setC_PaymentTerm_ID (0);
            setDiscount (Env.ZERO);
            setDiscount2 (Env.ZERO);
            setDiscountDays (0);
            setDiscountDays2 (0);
            setGraceDays (0);
            setIsDueFixed (false);
            setIsValid (false);
            setName (null);
            setNetDays (0);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_PaymentTerm (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495262821789L;
    /** Last Updated Timestamp 2008-06-10 15:38:25.0 */
    public static final long updatedMS = 1213137505000L;
    /** AD_Table_ID=113 */
    public static final int Table_ID=113;
    
    /** TableName=C_PaymentTerm */
    public static final String Table_Name="C_PaymentTerm";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set After Delivery.
    @param AfterDelivery Due after delivery rather than after invoicing */
    public void setAfterDelivery (boolean AfterDelivery)
    {
        set_Value ("AfterDelivery", Boolean.valueOf(AfterDelivery));
        
    }
    
    /** Get After Delivery.
    @return Due after delivery rather than after invoicing */
    public boolean isAfterDelivery() 
    {
        return get_ValueAsBoolean("AfterDelivery");
        
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
    
    /** Set Discount 2 %.
    @param Discount2 Discount in percent */
    public void setDiscount2 (java.math.BigDecimal Discount2)
    {
        if (Discount2 == null) throw new IllegalArgumentException ("Discount2 is mandatory.");
        set_Value ("Discount2", Discount2);
        
    }
    
    /** Get Discount 2 %.
    @return Discount in percent */
    public java.math.BigDecimal getDiscount2() 
    {
        return get_ValueAsBigDecimal("Discount2");
        
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
    
    /** Set Discount Days 2.
    @param DiscountDays2 Number of days from invoice date to be eligible for discount */
    public void setDiscountDays2 (int DiscountDays2)
    {
        set_Value ("DiscountDays2", Integer.valueOf(DiscountDays2));
        
    }
    
    /** Get Discount Days 2.
    @return Number of days from invoice date to be eligible for discount */
    public int getDiscountDays2() 
    {
        return get_ValueAsInt("DiscountDays2");
        
    }
    
    /** Set Document Note.
    @param DocumentNote Additional information for a Document */
    public void setDocumentNote (String DocumentNote)
    {
        set_Value ("DocumentNote", DocumentNote);
        
    }
    
    /** Get Document Note.
    @return Additional information for a Document */
    public String getDocumentNote() 
    {
        return (String)get_Value("DocumentNote");
        
    }
    
    /** Set Fix month cutoff.
    @param FixMonthCutoff Last day to include for next due date */
    public void setFixMonthCutoff (int FixMonthCutoff)
    {
        set_Value ("FixMonthCutoff", Integer.valueOf(FixMonthCutoff));
        
    }
    
    /** Get Fix month cutoff.
    @return Last day to include for next due date */
    public int getFixMonthCutoff() 
    {
        return get_ValueAsInt("FixMonthCutoff");
        
    }
    
    /** Set Fix month day.
    @param FixMonthDay Day of the month of the due date */
    public void setFixMonthDay (int FixMonthDay)
    {
        set_Value ("FixMonthDay", Integer.valueOf(FixMonthDay));
        
    }
    
    /** Get Fix month day.
    @return Day of the month of the due date */
    public int getFixMonthDay() 
    {
        return get_ValueAsInt("FixMonthDay");
        
    }
    
    /** Set Fix month offset.
    @param FixMonthOffset Number of months (0=same, 1=following) */
    public void setFixMonthOffset (int FixMonthOffset)
    {
        set_Value ("FixMonthOffset", Integer.valueOf(FixMonthOffset));
        
    }
    
    /** Get Fix month offset.
    @return Number of months (0=same, 1=following) */
    public int getFixMonthOffset() 
    {
        return get_ValueAsInt("FixMonthOffset");
        
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
    
    /** Set Fixed due date.
    @param IsDueFixed Payment is due on a fixed date */
    public void setIsDueFixed (boolean IsDueFixed)
    {
        set_Value ("IsDueFixed", Boolean.valueOf(IsDueFixed));
        
    }
    
    /** Get Fixed due date.
    @return Payment is due on a fixed date */
    public boolean isDueFixed() 
    {
        return get_ValueAsBoolean("IsDueFixed");
        
    }
    
    /** Set Next Business Day.
    @param IsNextBusinessDay Payment due on the next business day */
    public void setIsNextBusinessDay (boolean IsNextBusinessDay)
    {
        set_Value ("IsNextBusinessDay", Boolean.valueOf(IsNextBusinessDay));
        
    }
    
    /** Get Next Business Day.
    @return Payment due on the next business day */
    public boolean isNextBusinessDay() 
    {
        return get_ValueAsBoolean("IsNextBusinessDay");
        
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
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    
}
