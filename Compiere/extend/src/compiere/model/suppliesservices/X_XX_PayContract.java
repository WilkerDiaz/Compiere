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
package compiere.model.suppliesservices;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_PayContract
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_PayContract extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_PayContract_ID id
    @param trx transaction
    */
    public X_XX_PayContract (Ctx ctx, int XX_PayContract_ID, Trx trx)
    {
        super (ctx, XX_PayContract_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_PayContract_ID == 0)
        {
            setValue (null);
            setXX_Contract_ID (0);
            setXX_PAYCONTRACT_ID (0);	// @XX_Contract_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_PayContract (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27640576834789L;
    /** Last Updated Timestamp 2013-01-17 15:08:38.0 */
    public static final long updatedMS = 1358451518000L;
    /** AD_Table_ID=1002456 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_PayContract");
        
    }
    ;
    
    /** TableName=XX_PayContract */
    public static final String Table_Name="XX_PayContract";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Anticipated.
    @param XX_Anticipated1 Anticipated */
    public void setXX_Anticipated1 (boolean XX_Anticipated1)
    {
        set_Value ("XX_Anticipated1", Boolean.valueOf(XX_Anticipated1));
        
    }
    
    /** Get Anticipated.
    @return Anticipated */
    public boolean isXX_Anticipated1() 
    {
        return get_ValueAsBoolean("XX_Anticipated1");
        
    }
    
    /** Set Anticipated.
    @param XX_Anticipated2 Anticipated */
    public void setXX_Anticipated2 (boolean XX_Anticipated2)
    {
        set_Value ("XX_Anticipated2", Boolean.valueOf(XX_Anticipated2));
        
    }
    
    /** Get Anticipated.
    @return Anticipated */
    public boolean isXX_Anticipated2() 
    {
        return get_ValueAsBoolean("XX_Anticipated2");
        
    }
    
    /** Set Amount.
    @param XX_ContractAmount Amount */
    public void setXX_ContractAmount (java.math.BigDecimal XX_ContractAmount)
    {
        set_Value ("XX_ContractAmount", XX_ContractAmount);
        
    }
    
    /** Get Amount.
    @return Amount */
    public java.math.BigDecimal getXX_ContractAmount() 
    {
        return get_ValueAsBigDecimal("XX_ContractAmount");
        
    }
    
    /** Set Percentage.
    @param XX_CPercentage Percentage */
    public void setXX_CPercentage (java.math.BigDecimal XX_CPercentage)
    {
        set_Value ("XX_CPercentage", XX_CPercentage);
        
    }
    
    /** Get Percentage.
    @return Percentage */
    public java.math.BigDecimal getXX_CPercentage() 
    {
        return get_ValueAsBigDecimal("XX_CPercentage");
        
    }
    
    /** Set Date From.
    @param XX_DateFrom Date From */
    public void setXX_DateFrom (Timestamp XX_DateFrom)
    {
        set_Value ("XX_DateFrom", XX_DateFrom);
        
    }
    
    /** Get Date From.
    @return Date From */
    public Timestamp getXX_DateFrom() 
    {
        return (Timestamp)get_Value("XX_DateFrom");
        
    }
    
    /** Set Date To.
    @param XX_DateTo Date To */
    public void setXX_DateTo (Timestamp XX_DateTo)
    {
        set_Value ("XX_DateTo", XX_DateTo);
        
    }
    
    /** Get Date To.
    @return Date To */
    public Timestamp getXX_DateTo() 
    {
        return (Timestamp)get_Value("XX_DateTo");
        
    }
    
    /** Set New Contract Amount (Recalculate).
    @param XX_NewContractAmount New Contract Amount (Recalculate) */
    public void setXX_NewContractAmount (java.math.BigDecimal XX_NewContractAmount)
    {
        set_Value ("XX_NewContractAmount", XX_NewContractAmount);
        
    }
    
    /** Get New Contract Amount (Recalculate).
    @return New Contract Amount (Recalculate) */
    public java.math.BigDecimal getXX_NewContractAmount() 
    {
        return get_ValueAsBigDecimal("XX_NewContractAmount");
        
    }
    
    /** Set New Contract Percentage (Recalculate).
    @param XX_NewCPercentage New Contract Percentage (Recalculate) */
    public void setXX_NewCPercentage (java.math.BigDecimal XX_NewCPercentage)
    {
        set_Value ("XX_NewCPercentage", XX_NewCPercentage);
        
    }
    
    /** Get New Contract Percentage (Recalculate).
    @return New Contract Percentage (Recalculate) */
    public java.math.BigDecimal getXX_NewCPercentage() 
    {
        return get_ValueAsBigDecimal("XX_NewCPercentage");
        
    }
    
    /** Set New Date From (Recalculate).
    @param XX_NewDateFrom New Date From (Recalculate) */
    public void setXX_NewDateFrom (Timestamp XX_NewDateFrom)
    {
        set_Value ("XX_NewDateFrom", XX_NewDateFrom);
        
    }
    
    /** Get New Date From (Recalculate).
    @return New Date From (Recalculate) */
    public Timestamp getXX_NewDateFrom() 
    {
        return (Timestamp)get_Value("XX_NewDateFrom");
        
    }
    
    /** Set XX_PAYCONTRACT_ID.
    @param XX_PAYCONTRACT_ID XX_PAYCONTRACT_ID */
    public void setXX_PAYCONTRACT_ID (int XX_PAYCONTRACT_ID)
    {
        if (XX_PAYCONTRACT_ID < 1) throw new IllegalArgumentException ("XX_PAYCONTRACT_ID is mandatory.");
        set_ValueNoCheck ("XX_PAYCONTRACT_ID", Integer.valueOf(XX_PAYCONTRACT_ID));
        
    }
    
    /** Get XX_PAYCONTRACT_ID.
    @return XX_PAYCONTRACT_ID */
    public int getXX_PAYCONTRACT_ID() 
    {
        return get_ValueAsInt("XX_PAYCONTRACT_ID");
        
    }
    
    /** Set Pay Day.
    @param XX_PayDay1 Pay Day */
    public void setXX_PayDay1 (int XX_PayDay1)
    {
        set_Value ("XX_PayDay1", Integer.valueOf(XX_PayDay1));
        
    }
    
    /** Get Pay Day.
    @return Pay Day */
    public int getXX_PayDay1() 
    {
        return get_ValueAsInt("XX_PayDay1");
        
    }
    
    /** Set Pay Day.
    @param XX_PayDay2 Pay Day */
    public void setXX_PayDay2 (int XX_PayDay2)
    {
        set_Value ("XX_PayDay2", Integer.valueOf(XX_PayDay2));
        
    }
    
    /** Get Pay Day.
    @return Pay Day */
    public int getXX_PayDay2() 
    {
        return get_ValueAsInt("XX_PayDay2");
        
    }
    
    /** Biannual = BIA */
    public static final String XX_PAYMENTRECORRENCY2_Biannual = X_Ref_XX_PaymentRecurrency_LV.BIANNUAL.getValue();
    /** Bimonthly = BIM */
    public static final String XX_PAYMENTRECORRENCY2_Bimonthly = X_Ref_XX_PaymentRecurrency_LV.BIMONTHLY.getValue();
    /** Biweekly = BIW */
    public static final String XX_PAYMENTRECORRENCY2_Biweekly = X_Ref_XX_PaymentRecurrency_LV.BIWEEKLY.getValue();
    /** Monthly = MON */
    public static final String XX_PAYMENTRECORRENCY2_Monthly = X_Ref_XX_PaymentRecurrency_LV.MONTHLY.getValue();
    /** Trimestral = TRI */
    public static final String XX_PAYMENTRECORRENCY2_Trimestral = X_Ref_XX_PaymentRecurrency_LV.TRIMESTRAL.getValue();
    /** Single Payment = UNI */
    public static final String XX_PAYMENTRECORRENCY2_SinglePayment = X_Ref_XX_PaymentRecurrency_LV.SINGLE_PAYMENT.getValue();
    /** Set Payment Recorrency.
    @param XX_PaymentRecorrency2 Payment Recorrency */
    public void setXX_PaymentRecorrency2 (String XX_PaymentRecorrency2)
    {
        if (!X_Ref_XX_PaymentRecurrency_LV.isValid(XX_PaymentRecorrency2))
        throw new IllegalArgumentException ("XX_PaymentRecorrency2 Invalid value - " + XX_PaymentRecorrency2 + " - Reference_ID=1001656 - BIA - BIM - BIW - MON - TRI - UNI");
        set_Value ("XX_PaymentRecorrency2", XX_PaymentRecorrency2);
        
    }
    
    /** Get Payment Recorrency.
    @return Payment Recorrency */
    public String getXX_PaymentRecorrency2() 
    {
        return (String)get_Value("XX_PaymentRecorrency2");
        
    }
    
    /** Biannual = BIA */
    public static final String XX_PAYMENTRECURRENCY1_Biannual = X_Ref_XX_PaymentRecurrency_LV.BIANNUAL.getValue();
    /** Bimonthly = BIM */
    public static final String XX_PAYMENTRECURRENCY1_Bimonthly = X_Ref_XX_PaymentRecurrency_LV.BIMONTHLY.getValue();
    /** Biweekly = BIW */
    public static final String XX_PAYMENTRECURRENCY1_Biweekly = X_Ref_XX_PaymentRecurrency_LV.BIWEEKLY.getValue();
    /** Monthly = MON */
    public static final String XX_PAYMENTRECURRENCY1_Monthly = X_Ref_XX_PaymentRecurrency_LV.MONTHLY.getValue();
    /** Trimestral = TRI */
    public static final String XX_PAYMENTRECURRENCY1_Trimestral = X_Ref_XX_PaymentRecurrency_LV.TRIMESTRAL.getValue();
    /** Single Payment = UNI */
    public static final String XX_PAYMENTRECURRENCY1_SinglePayment = X_Ref_XX_PaymentRecurrency_LV.SINGLE_PAYMENT.getValue();
    /** Set Payment Recurrency.
    @param XX_PaymentRecurrency1 Payment Recurrency */
    public void setXX_PaymentRecurrency1 (String XX_PaymentRecurrency1)
    {
        if (!X_Ref_XX_PaymentRecurrency_LV.isValid(XX_PaymentRecurrency1))
        throw new IllegalArgumentException ("XX_PaymentRecurrency1 Invalid value - " + XX_PaymentRecurrency1 + " - Reference_ID=1001656 - BIA - BIM - BIW - MON - TRI - UNI");
        set_Value ("XX_PaymentRecurrency1", XX_PaymentRecurrency1);
        
    }
    
    /** Get Payment Recurrency.
    @return Payment Recurrency */
    public String getXX_PaymentRecurrency1() 
    {
        return (String)get_Value("XX_PaymentRecurrency1");
        
    }
    
    /** Set Recalculate Estimated Payment.
    @param XX_RecalculateEPayment Recalculate Estimated Payment */
    public void setXX_RecalculateEPayment (String XX_RecalculateEPayment)
    {
        set_Value ("XX_RecalculateEPayment", XX_RecalculateEPayment);
        
    }
    
    /** Get Recalculate Estimated Payment.
    @return Recalculate Estimated Payment */
    public String getXX_RecalculateEPayment() 
    {
        return (String)get_Value("XX_RecalculateEPayment");
        
    }
    
    
}
