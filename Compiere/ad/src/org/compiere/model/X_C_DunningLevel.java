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
/** Generated Model for C_DunningLevel
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_DunningLevel.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_DunningLevel extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_DunningLevel_ID id
    @param trx transaction
    */
    public X_C_DunningLevel (Ctx ctx, int C_DunningLevel_ID, Trx trx)
    {
        super (ctx, C_DunningLevel_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_DunningLevel_ID == 0)
        {
            setC_DunningLevel_ID (0);
            setC_Dunning_ID (0);
            setChargeFee (false);
            setChargeInterest (false);
            setDaysAfterDue (Env.ZERO);
            setDaysBetweenDunning (0);
            setIsSetCreditStop (false);
            setIsSetPaymentTerm (false);
            setIsShowAllDue (false);
            setIsShowNotDue (false);
            setName (null);
            setPrintName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_DunningLevel (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=331 */
    public static final int Table_ID=331;
    
    /** TableName=C_DunningLevel */
    public static final String Table_Name="C_DunningLevel";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Dunning Level.
    @param C_DunningLevel_ID Dunning Level */
    public void setC_DunningLevel_ID (int C_DunningLevel_ID)
    {
        if (C_DunningLevel_ID < 1) throw new IllegalArgumentException ("C_DunningLevel_ID is mandatory.");
        set_ValueNoCheck ("C_DunningLevel_ID", Integer.valueOf(C_DunningLevel_ID));
        
    }
    
    /** Get Dunning Level.
    @return Dunning Level */
    public int getC_DunningLevel_ID() 
    {
        return get_ValueAsInt("C_DunningLevel_ID");
        
    }
    
    /** Set Dunning.
    @param C_Dunning_ID Dunning Rules for overdue invoices */
    public void setC_Dunning_ID (int C_Dunning_ID)
    {
        if (C_Dunning_ID < 1) throw new IllegalArgumentException ("C_Dunning_ID is mandatory.");
        set_ValueNoCheck ("C_Dunning_ID", Integer.valueOf(C_Dunning_ID));
        
    }
    
    /** Get Dunning.
    @return Dunning Rules for overdue invoices */
    public int getC_Dunning_ID() 
    {
        return get_ValueAsInt("C_Dunning_ID");
        
    }
    
    /** Set Payment Term.
    @param C_PaymentTerm_ID The terms of Payment (timing, discount) */
    public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
    {
        if (C_PaymentTerm_ID <= 0) set_Value ("C_PaymentTerm_ID", null);
        else
        set_Value ("C_PaymentTerm_ID", Integer.valueOf(C_PaymentTerm_ID));
        
    }
    
    /** Get Payment Term.
    @return The terms of Payment (timing, discount) */
    public int getC_PaymentTerm_ID() 
    {
        return get_ValueAsInt("C_PaymentTerm_ID");
        
    }
    
    /** Set Charge fee.
    @param ChargeFee Indicates if fees will be charged for overdue invoices */
    public void setChargeFee (boolean ChargeFee)
    {
        set_Value ("ChargeFee", Boolean.valueOf(ChargeFee));
        
    }
    
    /** Get Charge fee.
    @return Indicates if fees will be charged for overdue invoices */
    public boolean isChargeFee() 
    {
        return get_ValueAsBoolean("ChargeFee");
        
    }
    
    /** Set Charge Interest.
    @param ChargeInterest Indicates if interest will be charged on overdue invoices */
    public void setChargeInterest (boolean ChargeInterest)
    {
        set_Value ("ChargeInterest", Boolean.valueOf(ChargeInterest));
        
    }
    
    /** Get Charge Interest.
    @return Indicates if interest will be charged on overdue invoices */
    public boolean isChargeInterest() 
    {
        return get_ValueAsBoolean("ChargeInterest");
        
    }
    
    /** Set Days after due date.
    @param DaysAfterDue Days after due date to dun (if negative days until due) */
    public void setDaysAfterDue (java.math.BigDecimal DaysAfterDue)
    {
        if (DaysAfterDue == null) throw new IllegalArgumentException ("DaysAfterDue is mandatory.");
        set_Value ("DaysAfterDue", DaysAfterDue);
        
    }
    
    /** Get Days after due date.
    @return Days after due date to dun (if negative days until due) */
    public java.math.BigDecimal getDaysAfterDue() 
    {
        return get_ValueAsBigDecimal("DaysAfterDue");
        
    }
    
    /** Set Days between dunning.
    @param DaysBetweenDunning Days between sending dunning notices */
    public void setDaysBetweenDunning (int DaysBetweenDunning)
    {
        set_Value ("DaysBetweenDunning", Integer.valueOf(DaysBetweenDunning));
        
    }
    
    /** Get Days between dunning.
    @return Days between sending dunning notices */
    public int getDaysBetweenDunning() 
    {
        return get_ValueAsInt("DaysBetweenDunning");
        
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
    
    /** Set Dunning Print Format.
    @param Dunning_PrintFormat_ID Print Format for printing Dunning Letters */
    public void setDunning_PrintFormat_ID (int Dunning_PrintFormat_ID)
    {
        if (Dunning_PrintFormat_ID <= 0) set_Value ("Dunning_PrintFormat_ID", null);
        else
        set_Value ("Dunning_PrintFormat_ID", Integer.valueOf(Dunning_PrintFormat_ID));
        
    }
    
    /** Get Dunning Print Format.
    @return Print Format for printing Dunning Letters */
    public int getDunning_PrintFormat_ID() 
    {
        return get_ValueAsInt("Dunning_PrintFormat_ID");
        
    }
    
    /** Set Fee Amount.
    @param FeeAmt Fee amount in invoice currency */
    public void setFeeAmt (java.math.BigDecimal FeeAmt)
    {
        set_Value ("FeeAmt", FeeAmt);
        
    }
    
    /** Get Fee Amount.
    @return Fee amount in invoice currency */
    public java.math.BigDecimal getFeeAmt() 
    {
        return get_ValueAsBigDecimal("FeeAmt");
        
    }
    
    /** Set Interest in percent.
    @param InterestPercent Percentage interest to charge on overdue invoices */
    public void setInterestPercent (java.math.BigDecimal InterestPercent)
    {
        set_Value ("InterestPercent", InterestPercent);
        
    }
    
    /** Get Interest in percent.
    @return Percentage interest to charge on overdue invoices */
    public java.math.BigDecimal getInterestPercent() 
    {
        return get_ValueAsBigDecimal("InterestPercent");
        
    }
    
    /** Collection Agency = C */
    public static final String INVOICECOLLECTIONTYPE_CollectionAgency = X_Ref_C_Invoice_InvoiceCollectionType.COLLECTION_AGENCY.getValue();
    /** Dunning = D */
    public static final String INVOICECOLLECTIONTYPE_Dunning = X_Ref_C_Invoice_InvoiceCollectionType.DUNNING.getValue();
    /** Legal Procedure = L */
    public static final String INVOICECOLLECTIONTYPE_LegalProcedure = X_Ref_C_Invoice_InvoiceCollectionType.LEGAL_PROCEDURE.getValue();
    /** Uncollectable = U */
    public static final String INVOICECOLLECTIONTYPE_Uncollectable = X_Ref_C_Invoice_InvoiceCollectionType.UNCOLLECTABLE.getValue();
    /** Set Collection Status.
    @param InvoiceCollectionType Invoice Collection Status */
    public void setInvoiceCollectionType (String InvoiceCollectionType)
    {
        if (!X_Ref_C_Invoice_InvoiceCollectionType.isValid(InvoiceCollectionType))
        throw new IllegalArgumentException ("InvoiceCollectionType Invalid value - " + InvoiceCollectionType + " - Reference_ID=394 - C - D - L - U");
        set_Value ("InvoiceCollectionType", InvoiceCollectionType);
        
    }
    
    /** Get Collection Status.
    @return Invoice Collection Status */
    public String getInvoiceCollectionType() 
    {
        return (String)get_Value("InvoiceCollectionType");
        
    }
    
    /** Set Credit Stop.
    @param IsSetCreditStop Set the business partner to credit stop */
    public void setIsSetCreditStop (boolean IsSetCreditStop)
    {
        set_Value ("IsSetCreditStop", Boolean.valueOf(IsSetCreditStop));
        
    }
    
    /** Get Credit Stop.
    @return Set the business partner to credit stop */
    public boolean isSetCreditStop() 
    {
        return get_ValueAsBoolean("IsSetCreditStop");
        
    }
    
    /** Set Set Payment Term.
    @param IsSetPaymentTerm Set the payment term of the Business Partner */
    public void setIsSetPaymentTerm (boolean IsSetPaymentTerm)
    {
        set_Value ("IsSetPaymentTerm", Boolean.valueOf(IsSetPaymentTerm));
        
    }
    
    /** Get Set Payment Term.
    @return Set the payment term of the Business Partner */
    public boolean isSetPaymentTerm() 
    {
        return get_ValueAsBoolean("IsSetPaymentTerm");
        
    }
    
    /** Set Show All Due.
    @param IsShowAllDue Show/print all due invoices */
    public void setIsShowAllDue (boolean IsShowAllDue)
    {
        set_Value ("IsShowAllDue", Boolean.valueOf(IsShowAllDue));
        
    }
    
    /** Get Show All Due.
    @return Show/print all due invoices */
    public boolean isShowAllDue() 
    {
        return get_ValueAsBoolean("IsShowAllDue");
        
    }
    
    /** Set Show Not Due.
    @param IsShowNotDue Show/print all invoices which are not due (yet). */
    public void setIsShowNotDue (boolean IsShowNotDue)
    {
        set_Value ("IsShowNotDue", Boolean.valueOf(IsShowNotDue));
        
    }
    
    /** Get Show Not Due.
    @return Show/print all invoices which are not due (yet). */
    public boolean isShowNotDue() 
    {
        return get_ValueAsBoolean("IsShowNotDue");
        
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
    
    /** Set Note.
    @param Note Optional additional user defined information */
    public void setNote (String Note)
    {
        set_Value ("Note", Note);
        
    }
    
    /** Get Note.
    @return Optional additional user defined information */
    public String getNote() 
    {
        return (String)get_Value("Note");
        
    }
    
    /** Set Print Text.
    @param PrintName The label text to be printed on a document or correspondence. */
    public void setPrintName (String PrintName)
    {
        if (PrintName == null) throw new IllegalArgumentException ("PrintName is mandatory.");
        set_Value ("PrintName", PrintName);
        
    }
    
    /** Get Print Text.
    @return The label text to be printed on a document or correspondence. */
    public String getPrintName() 
    {
        return (String)get_Value("PrintName");
        
    }
    
    
}
